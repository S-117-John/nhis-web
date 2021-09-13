package com.zebone.nhis.ma.pub.platform.send.impl.zsrm.service;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.zebone.nhis.common.module.base.bd.code.BdDefdoc;
import com.zebone.nhis.common.module.base.ou.BdOuDept;
import com.zebone.nhis.common.module.pv.PvInfant;
import com.zebone.nhis.common.module.pv.PvStaff;
import com.zebone.nhis.common.support.DateUtils;
import com.zebone.nhis.common.support.IDictCodeConst;
import com.zebone.nhis.ma.pub.platform.send.impl.zsrm.build.RequestBuild;
import com.zebone.nhis.ma.pub.platform.send.impl.zsrm.dao.IpCnOperationMapper;
import com.zebone.nhis.ma.pub.platform.send.impl.zsrm.dao.IpNurseOperationMapper;
import com.zebone.nhis.ma.pub.platform.send.impl.zsrm.model.PvEncounterVo;
import com.zebone.nhis.ma.pub.platform.send.impl.zsrm.support.MsgIndexData;
import com.zebone.nhis.ma.pub.platform.send.impl.zsrm.support.RequestData;
import com.zebone.nhis.ma.pub.platform.send.impl.zsrm.support.SendAndResolve;
import com.zebone.nhis.ma.pub.platform.send.impl.zsrm.vo.base.*;
import com.zebone.nhis.ma.pub.platform.send.impl.zsrm.vo.cn.BabyAdmitResource;
import com.zebone.nhis.ma.pub.platform.send.impl.zsrm.vo.cn.LocalLocation;
import com.zebone.nhis.ma.pub.platform.send.impl.zsrm.vo.cn.Numerator;
import com.zebone.nhis.ma.pub.platform.send.impl.zsrm.vo.cn.NurseOperation;
import com.zebone.nhis.ma.pub.platform.send.impl.zsrm.vo.patient.Contact;
import com.zebone.nhis.ma.pub.platform.send.impl.zsrm.vo.patient.Patient;
import com.zebone.nhis.ma.pub.platform.send.impl.zsrm.vo.pv.Diagnosis;
import com.zebone.nhis.ma.pub.platform.send.impl.zsrm.vo.pv.Encounter;
import com.zebone.nhis.ma.pub.platform.send.impl.zsrm.vo.pv.Participant;
import com.zebone.nhis.ma.pub.platform.send.impl.zsrm.vo.pv.Period;
import com.zebone.platform.common.support.UserContext;
import com.zebone.platform.modules.dao.DataBaseHelper;
import com.zebone.platform.modules.exception.BusException;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class ZsphPlatFormSendIpNurseHandler {
    private static Logger log = LoggerFactory.getLogger("nhis.lbHl7Log");

    @Resource
    private IpNurseOperationMapper ipNurseOperationMapper;
    @Resource
    private IpCnOperationMapper ipCnOperationMapper;

    /**
     * 推送住院入科信息
     * @param paramMap
     */
    public void sendDeptInMsg(Map<String, Object> paramMap){
        //PvAdtService public void savePatisPvIn
        RequestData requestData = buildDeptInReq(paramMap, "ZYRKXX","arrived");
        log.info("住院入科信息构造完成id:{},data:{},remoteMethod:{},implicitRules:{}"
                ,requestData.getId(),requestData.getData(),requestData.getRemoteMethod(),"ZYRKXX");

        SendAndResolve.getInstance().send(HttpMethod.POST,requestData);
    }

    /**
     * 入科取消
     * @param paramMap
     */
    public void sendCancelDeptInMsg(Map<String, Object> paramMap){
        RequestData requestData = buildDeptInReq(paramMap, "ZYRKQX","cancelled");
        log.info("住院入科-取消入科信息构造完成id:{},data:{},remoteMethod:{},implicitRules:{}"
                ,requestData.getId(),requestData.getData(),requestData.getRemoteMethod(), "ZYRKQX");
        SendAndResolve.getInstance().send(HttpMethod.POST,requestData);
    }

    /**
     * 转科
     * @param paramMap
     */
    public void sendDeptChangeMsg(Map<String, Object> paramMap){
        RequestData requestData = buildDeptChange(paramMap, "ZYZKXX", "arrived");
        log.info("住院转科信息构造完成id:{},data:{},remoteMethod:{},implicitRules:{}"
                ,requestData.getId(),requestData.getData(),requestData.getRemoteMethod(), "ZYZKXX");
        SendAndResolve.getInstance().send(HttpMethod.POST,requestData);
    }

    /**
     * 转科--取消
     * @param paramMap
     */
    public void sendCancelDeptChangeMsg(Map<String, Object> paramMap){
        List<String> pkPvs = (List<String>) MapUtils.getObject(paramMap, "pkPvs");
        if(CollectionUtils.isNotEmpty(pkPvs)){
            for (String pkPv : pkPvs) {
                paramMap.put("pkPv",pkPv);
                RequestData requestData = buildDeptChange(paramMap, "ZYZKQX", "cancelled");
                log.info("住院转科-取消信息构造完成id:{},data:{},remoteMethod:{},implicitRules:{},pkPv:{}"
                        ,requestData.getId(),requestData.getData(),requestData.getRemoteMethod(), "ZYZKQX",pkPv);
                SendAndResolve.getInstance().send(HttpMethod.POST,requestData);
            }
        }
    }

    /**
     * 包床
     * @param paramMap
     */
    public void sendBedPackMsg(Map<String, Object> paramMap){
        sendBedInfoPrivate(paramMap,"ZYBCXX","arrived");
    }

    /**
     * 取消包床
     * @param paramMap
     */
    public void sendBedRtnPackMsg(Map<String, Object> paramMap){
        if(MapUtils.isEmpty(paramMap)){
            return;
        }
        paramMap.putAll(DataBaseHelper.queryForMap("select DATE_BEGIN,DATE_END,PK_DEPT,PK_DEPT_NS from PV_BED where PK_PV=?"
                ,MapUtils.getString(paramMap,"pkPv")));
        sendBedInfoPrivate(paramMap,"","cancelled");
    }

    /**
     * 换床
     * @param paramMap
     */
    public void sendBedChangeMsg(Map<String,Object> paramMap){
        //查询患者信息
        Map<String, Object> pvInfo = DataBaseHelper.queryForMap("select pv.CODE_PV,pi.CODE_PI,pi.CODE_OP,pi.CODE_IP,eo.code_emp,eo.name_emp,ip.IP_TIMES from PV_ENCOUNTER pv inner join PV_IP ip on pv.PK_PV=ip.PK_PV inner join PI_MASTER pi on pv.PK_PI=pi.PK_PI left join BD_OU_EMPLOYEE eo on pv.PK_EMP_TRE=eo.PK_EMP where pv.PK_PV=?",
                MapUtils.getString(paramMap, "pkPv"));
        Participant participant = new Participant();//责任医生
        participant.setType(Lists.newArrayList(new CodeableConcept(Lists.newArrayList(new Coding("OrigDocCode", MapUtils.getString(pvInfo,"codeEmp"), MapUtils.getString(pvInfo,"nameEmp"))))));

        //构造操作人，操作时间信息
        List<Extension> extensions = buildDeptInExt(paramMap);

        NurseOperation bedData = new NurseOperation();
        BusinessBase businessBase = RequestBuild.create(bedData);
        bedData.setId(businessBase.getId());
        bedData.setImplicitRules("ZYHCXX");
        bedData.setResourceType("Encounter");
        bedData.setIdentifier(Arrays.asList(new Identifier("id/visitno", MapUtils.getString(pvInfo,"codeIp"))
                ,new Identifier("id/visitTimes", MapUtils.getString(pvInfo,"ipTimes"))
                ,new Identifier("id/visitSerialNo", MapUtils.getString(pvInfo,"codePv"))
        ));
        bedData.setStatus("planned");
        bedData.setParticipant(Lists.newArrayList(participant));
        bedData.setHospitalization(new NurseOperation.Hospitalization("OrigBedCode",MapUtils.getString(paramMap,"bedOld")
                ,"TargBedCode",MapUtils.getString(paramMap,"bednoDes")));
        bedData.setExtension(extensions);

        RequestData requestData = buildRequestData(pvInfo, businessBase);
        log.info("住院换床-信息构造完成id:{},data:{},remoteMethod:{},implicitRules:{}"
                ,requestData.getId(),requestData.getData(),requestData.getRemoteMethod(), bedData.getImplicitRules());
        SendAndResolve.getInstance().send(HttpMethod.POST,requestData);
    }
    
    /**
     * @Description 推送住院新生儿登记
     */
    public void sendNewBornRegister(Map<String,Object> paramMap){
        String pkPvBb = MapUtils.getString(paramMap,"pkPvBb");
        String pkPvMother = MapUtils.getString(paramMap,"pkPv");
        String method = "_DELETE".equals(MapUtils.getString(paramMap, "STATUS"))?"XSRDJQX":"XSRDJXX";
        //查询婴儿就诊信息
        List<PvEncounterVo> babyInfos = ipCnOperationMapper.qryPiPv(pkPvBb);
        if(babyInfos.size()==0)
            return;
        PvEncounterVo baby = babyInfos.get(0);
        //查询婴儿登记信息
        PvInfant pvInfant = DataBaseHelper.queryForBean("select nvl(inf.WEIGHT,0) WEIGHT,nvl(inf.LEN,0) LEN,nvl(inf.num_product,1) num_product from PV_INFANT inf" +
                " where inf.PK_PV=? and inf.PK_PV_INFANT=?", PvInfant.class, new Object[]{pkPvMother, pkPvBb});
        //查询母亲信息
        List<PvEncounterVo> motherInfos = ipCnOperationMapper.qryPiPv(MapUtils.getString(paramMap, "pkPvBb"));
        if(motherInfos.size()==0)
            return;
        PvEncounterVo mother = motherInfos.get(0);
        //查询母亲床位信息
        List<Map<String, Object>> pvInfoList = ipNurseOperationMapper.qryPvInfo(pkPvMother);
        if(CollectionUtils.isEmpty(pvInfoList)){
            throw new BusException("未获取到就诊信息");
        }
        Map<String, Object> pvInfo = pvInfoList.get(0);
        //构造科室、病区信息
        Map<String, String> locationMap = Maps.newHashMap();
        locationMap.put("dept",MapUtils.getString(pvInfo,"pvDept"));
        locationMap.put("ward",MapUtils.getString(pvInfo,"pvDeptArea"));
        locationMap.put("bedno",MapUtils.getString(pvInfo,"bedNo"));
        //构造操作人，操作时间信息
        paramMap.put("codeEmp", UserContext.getUser().getCodeEmp());
        paramMap.put("nameEmp", UserContext.getUser().getNameEmp());
        List<Extension> extensions = buildDeptInExt(paramMap);


        //初始化数据
        BabyAdmitResource resource = new BabyAdmitResource();
        BusinessBase businessBase = RequestBuild.create(resource);
        //封装第二个Entry
        resource= (BabyAdmitResource) businessBase.getEntry().get(1).getResource();
        resource.setId(businessBase.getId());
        resource.setResourceType("Encounter");
        resource.setImplicitRules(method);
        resource.setStatus("finished");
        //封装diagnosis集合
        List<Diagnosis> diagnosisList = new ArrayList<>();
        Diagnosis diagnosis=new Diagnosis();
        diagnosis.setRank("诊断序号");
        //封装diagnosis集合下的Condition对象
        Condition condition = new Condition();
        condition.setResourceType("Condition");
        List<Coding> codingList = new ArrayList<>();
        CodeableConcept code = new CodeableConcept(codingList);
        code.getCoding().add(new Coding("诊断编码","诊断名称"));
        condition.setCode(code);
        diagnosis.setCondition(condition);
        //封装diagnosis集合下的use对象
        codingList = new ArrayList<>();
        CodeableConcept use = new CodeableConcept(codingList);
        use.getCoding().add(new Coding("diagnosisType","诊断类别编码","诊断类别名称"));
        diagnosis.setUse(use);
        diagnosisList.add(diagnosis);
        resource.setDiagnosis(diagnosisList);
        //封装extension集合
        extensions.add(new Extension("height",String.valueOf(pvInfant.getLen()),null));
        extensions.add(new Extension("weight",String.valueOf(pvInfant.getWeight()),null));
        extensions.add(new Extension("fetus_num","1",null));
        extensions.add(new Extension("parity_num",String.valueOf(pvInfant.getNumProduct()),null));
        resource.setExtension(extensions);
        //封装location集合
        resource.setLocation(buildDeptInLocation(locationMap));
        //封装Identifier集合
        resource.setIdentifier(Lists.newArrayList(
                 new Identifier("id/motherVisitNo",mother.getCodeIp())
                ,new Identifier("id/visitNo",baby.getCodeIp())
                ,new Identifier("id/visitTimes",baby.getIpTimes()!=null?baby.getIpTimes().toString():"")
                ,new Identifier("id/visitSerialNo",baby.getCodePv())));
        //封装subject
        Patient subject = new Patient();
        subject.setResourceType("Patient");
        subject.setBirthDate(baby.getBirthDate());//新生儿出生日期
        //封装subject下的Identifier集合
        subject.setIdentifier(Lists.newArrayList(new Identifier("code/patientId",baby.getCodePi())
                ,new Identifier("code/idNo",baby.getIdNo())));
        //封装subject下的name集合
        subject.setName(Lists.newArrayList(new TextElement(baby.getNamePi())));
        //封装subject下的contact集合
        List<Contact> contactList = new ArrayList<>();
        Contact contact=new Contact();
        //封装subject下的contact集合下的name对象
        contact.setName(new TextElement(mother.getNamePi()));
        //封装subject下的contact集合下的relationship集合
        BdDefdoc bdDefdoc = DataBaseHelper.queryForBean("SELECT name from bd_defdoc where code_defdoclist = ? and code = ?", BdDefdoc.class, new Object[]{"000013",baby.getDtRalation()});
        if(bdDefdoc != null){
            contact.setRelationship(Arrays.asList(new CodeableConcept(Arrays.asList(new Coding("relationship",baby.getDtRalation(),bdDefdoc.getName())))));
        }
        //封装subject下的contact集合下的telecom集合
        List<Identifier> telecom = new ArrayList<>();
        telecom.add(new Identifier("phone",mother.getTelNo()));
        contact.setTelecom(telecom);
        //封装subject下的contact集合下的extension集合
        List<Extension> extensionList = new ArrayList<>();
        Extension extension=new Extension();
        extension.setUrl("relationType");
        extension.setValueString("父亲");
        extensionList.add(extension);
        contact.setExtension(extensionList);
        //
        contactList.add(contact);
        subject.setContact(contactList);
        resource.setSubject(subject);


        RequestData requestData = RequestData.newBuilder()
                .id(businessBase.getId())
                .data(JSON.toJSONString(businessBase))
                .msgIndexData(MsgIndexData.newBuilder()
                        .codeOp(baby.getCodeOp()).codeIp(baby.getCodeIp())
                        .codePi(baby.getCodePi())
                        .codePv(baby.getCodePv())
                        .build())
                .remoteMethod("Encounter").build();
        log.info("新生儿住院登记或取消id:{},data:{},remoteMethod:{},implicitRules:{}"
                ,requestData.getId(),requestData.getData(),requestData.getRemoteMethod(),resource.getImplicitRules());

        SendAndResolve.getInstance().send(HttpMethod.POST,requestData);
    }

    private void sendBedInfoPrivate(Map<String, Object> paramMap,String method,String status){
        List<String> bedlist = (List<String>) paramMap.get("bednos");
        if(CollectionUtils.isNotEmpty(bedlist)){
            //查询患者信息
            Map<String, Object> pvInfo = DataBaseHelper.queryForMap("select pv.CODE_PV,pi.CODE_PI,pi.CODE_OP,pi.code_ip,ip.IP_TIMES from PV_ENCOUNTER pv inner join PV_IP ip on pv.PK_PV=ip.PK_PV inner join PI_MASTER pi on pv.PK_PI=pi.PK_PI where pv.PK_PV=?", MapUtils.getString(paramMap, "pkPv"));
            //查询床位科室、病区
            List<BdOuDept> list = ipNurseOperationMapper.qryBedInfo(paramMap);
            List<LocalLocation> locations = list.stream().map(dept -> {
                Location location = new Location();
                location.setResourceType("Location");
                location.setName(dept.getNameDept());
                location.setIdentifier(Lists.newArrayList(new Identifier(dept.getDeptType(), dept.getCodeDept())));
                return new LocalLocation(location);
            }).collect(Collectors.toList());
            //装入床位信息
            bedlist.forEach(vo ->{
                Location location = new Location();
                location.setResourceType("Location");
                location.setIdentifier(Lists.newArrayList(new Identifier("bedno", vo)));
                locations.add(new LocalLocation(location));
            });
            //构造操作人，操作时间信息
            List<Extension> extensions = buildDeptInExt(paramMap);
            Encounter encounter = new Encounter();
            BusinessBase businessBase = RequestBuild.create(encounter);
            encounter.setId(businessBase.getId());
            encounter.setResourceType("Encounter");
            encounter.setImplicitRules(method);
            encounter.setStatus(status);
            encounter.setPeriod(new Period((Date)MapUtils.getObject(paramMap, "dateBegin"),(Date)MapUtils.getObject(paramMap, "dateEnd")));
            encounter.setLocation(locations);
            encounter.setExtension(extensions);
            encounter.setIdentifier(Arrays.asList(new Identifier("id/visitno", MapUtils.getString(pvInfo,"codeIp"))
                    ,new Identifier("id/visitTimes", MapUtils.getString(pvInfo,"ipTimes"))
                    ,new Identifier("id/visitSerialNo", MapUtils.getString(pvInfo,"codePv"))));

            RequestData requestData = buildRequestData(pvInfo, businessBase);
            log.info("住院包床-{}信息构造完成id:{},status:{},data:{},remoteMethod:{},implicitRules:{}"
                    ,requestData.getId(),status,requestData.getData(),requestData.getRemoteMethod(), encounter.getImplicitRules());
            SendAndResolve.getInstance().send(HttpMethod.POST,requestData);
        }
    }


    private RequestData buildDeptInReq(Map<String, Object> paramMap,String method,String status){
        String pkPv;
        if(StringUtils.isBlank(pkPv=MapUtils.getString(paramMap,"pkPv"))){
            throw new BusException("未传入Pkpv");
        }
        List<Map<String, Object>> pvInfoList = ipNurseOperationMapper.qryPvInfo(pkPv);
        if(CollectionUtils.isEmpty(pvInfoList)){
            throw new BusException("未获取到就诊信息");
        }
        Map<String, Object> pvInfo = pvInfoList.get(0);
        //构造科室、病区信息
        boolean out = "ZYCKXX".equals(method) || "ZYCKQX".equals(method);//如果是出院（取消出院）
        Map<String, String> locationMap = Maps.newHashMap();
        locationMap.put(out?"outdept":"curdept",MapUtils.getString(pvInfo,"pvDept"));
        locationMap.put(out?"outward":"curward",MapUtils.getString(pvInfo,"pvDeptArea"));
        locationMap.put("indept",MapUtils.getString(pvInfo,"codeDept"));
        locationMap.put("inward",MapUtils.getString(pvInfo,"codeDeptArea"));
        locationMap.put("bedno",MapUtils.getString(pvInfo,"bedNo"));
        //构造操作人，操作时间信息
        List<Extension> extensions = buildDeptInExt(paramMap);
        //查询医生、护士信息
        List<PvStaff> pvStaffs = ipNurseOperationMapper.qryPvStaff(pkPv);
        List<Participant> participantList = pvStaffs.stream().filter(pvStaff -> Arrays.asList(IDictCodeConst.DT_EMPROLE_MAINNS, IDictCodeConst.DT_EMPROLE_MAINDOCT, IDictCodeConst.DT_EMPROLE_HEADDOCT).contains(pvStaff.getDtRole())).map(pvStaff -> {
            String system = null;
            if (IDictCodeConst.DT_EMPROLE_MAINNS.equals(pvStaff.getDtRole())) {
                system = "nurse";
            } else if (IDictCodeConst.DT_EMPROLE_MAINDOCT.equals(pvStaff.getDtRole())) {
                system = "attendDoc";
            }
            if (IDictCodeConst.DT_EMPROLE_HEADDOCT.equals(pvStaff.getDtRole())) {
                system = "directorDoc";
            }
            Participant participant = new Participant();
            participant.setType(Lists.newArrayList(new CodeableConcept(Lists.newArrayList(new Coding(system, pvStaff.getModifier(), pvStaff.getNameEmp())))));
            return participant;
        }).collect(Collectors.toList());
        Participant participant = new Participant();//责任医生--取了一个我们的开单医生
        participant.setType(Lists.newArrayList(new CodeableConcept(Lists.newArrayList(new Coding("dischargeDoc", MapUtils.getString(pvInfo,"codeEmp"), MapUtils.getString(pvInfo,"nameEmp"))))));
        participantList.add(participant);

        NurseOperation deptIn = new NurseOperation();
        BusinessBase businessBase = RequestBuild.create(deptIn);
        deptIn.setId(businessBase.getId());
        deptIn.setImplicitRules(method);
        deptIn.setResourceType("Encounter");
        deptIn.setIdentifier(Arrays.asList(new Identifier("id/visitno", MapUtils.getString(pvInfo,"codeIp"))
                ,new Identifier("id/visitTimes", MapUtils.getString(pvInfo,"ipTimes"))
                ,new Identifier("id/visitSerialNo", MapUtils.getString(pvInfo,"codePv"))
        ));
        deptIn.setStatus(status);
        deptIn.setParticipant(participantList);
        deptIn.setLocation(buildDeptInLocation(locationMap));
        deptIn.setExtension(extensions);

        return buildRequestData(pvInfo, businessBase);
    }

    private RequestData buildRequestData(Map<String, Object> pvInfo, BusinessBase businessBase) {
        return RequestData.newBuilder()
                .id(businessBase.getId())
                .data(JSON.toJSONString(businessBase))
                .msgIndexData(MsgIndexData.newBuilder()
                        .codeOp(MapUtils.getString(pvInfo, "codeOp"))
                        .codeIp(MapUtils.getString(pvInfo, "codeIp"))
                        .codePi(MapUtils.getString(pvInfo, "codePi"))
                        .codePv(MapUtils.getString(pvInfo, "codePv"))
                        .build())
                .remoteMethod("Encounter").build();
    }

    private RequestData buildDeptChange(Map<String, Object> paramMap,String method,String status){
        String pkPv;
        if (StringUtils.isBlank(pkPv = MapUtils.getString(paramMap, "pkPv"))) {
            throw new BusException("未传入Pkpv");
        }
        Map map = new HashMap<String, Object>();
        map.put("pkPv", pkPv);
        List<Map<String, Object>> pvInfoList = ipNurseOperationMapper.qryChangeDeptInfo(map);
        if (CollectionUtils.isEmpty(pvInfoList)) {
            throw new BusException("未获取到就诊信息");
        }
        Map<String, Object> pvInfo = pvInfoList.get(0);
        //构造科室、病区、床号信息
        Map<String, String> locationMap = Maps.newHashMap();
        locationMap.put("OrigWardCode", MapUtils.getString(pvInfo, "codeDeptArea"));
        locationMap.put("OrigBedCode", MapUtils.getString(pvInfo, "bedNo"));
        locationMap.put("TargWardCode", MapUtils.getString(pvInfo, "codeDeptTArea"));
        locationMap.put("TargBedCode", MapUtils.getString(pvInfo, "bedNo"));

        //构造操作人，操作时间信息
        List<Extension> extensions = buildDeptInExt(paramMap);
        //构造医生信息
        Participant participant = new Participant();//转出医生--取了一个我们的开单医生。没有转入医生
        participant.setType(Lists.newArrayList(new CodeableConcept(Lists.newArrayList(new Coding("OrigDocCode", MapUtils.getString(pvInfo,"codeEmp"), MapUtils.getString(pvInfo,"nameEmp"))))));

        NurseOperation.Hospitalization hospitalization = new NurseOperation.Hospitalization("OrigDeptCode",MapUtils.getString(pvInfo, "codeDept"), "TargDocCode",MapUtils.getString(pvInfo, "codeDeptT"));
        Numerator numerator = new Numerator();//转科序号标识,我们没有
        numerator.setValue("00000");
        hospitalization.setPreAdmissionIdentifier(numerator);

        NurseOperation deptIn = new NurseOperation();
        BusinessBase businessBase = RequestBuild.create(deptIn);
        deptIn.setId(businessBase.getId());
        deptIn.setImplicitRules(method);
        deptIn.setHospitalization(hospitalization);
        deptIn.setResourceType("Encounter");
        deptIn.setIdentifier(Arrays.asList(new Identifier("id/visitno", MapUtils.getString(pvInfo,"codeIp"))
                ,new Identifier("id/visitTimes", MapUtils.getString(pvInfo,"ipTimes"))
                ,new Identifier("id/visitSerialNo", MapUtils.getString(pvInfo,"codePv"))

        ));
        deptIn.setStatus(status);
        deptIn.setParticipant(Lists.newArrayList(participant));
        deptIn.setLocation(buildDeptInLocation(locationMap));
        deptIn.setExtension(extensions);

        return buildRequestData(pvInfo, businessBase);
    }

    private List<LocalLocation> buildDeptInLocation(Map<String,String> pvInfo){
        List<LocalLocation> locations = Lists.newArrayList();
        pvInfo.forEach((k,v) ->{
            Location location = new Location();
            location.setResourceType("Location");
            location.setIdentifier(Lists.newArrayList(new Identifier(k, v)));
            locations.add(new LocalLocation(location));
        });
        return locations;
    }

    private List<Extension> buildDeptInExt(Map<String,Object> pvInfo){
        Extension ext1 = new Extension();
        ext1.setUrl("operationTime");
        ext1.setValueDatetime(DateUtils.getDate("yyyy-MM-dd HH:mm:ss"));
        Extension ext2 = new Extension();
        ext2.setUrl("operatorCode");
        ext2.setValueString(MapUtils.getString(pvInfo,"codeEmp"));
        Extension ext3 = new Extension();
        ext3.setUrl("operatorName");
        ext3.setValueString(MapUtils.getString(pvInfo,"nameEmp"));
        return Lists.newArrayList(ext1,ext2,ext3);
    }

    /**
     * 发送出院消息
     * @param paramMap
     */
    public void sendPvOutMsg(Map<String, Object> paramMap) {
        String method = MapUtils.getString(paramMap,"implicitRules");
        RequestData requestData = buildDeptInReq(paramMap, method,"finished");
        log.info("住院出院/取消出院信息构造完成id:{},data:{},remoteMethod:{},implicitRules:{}"
                ,requestData.getId(),requestData.getData(),requestData.getRemoteMethod(),method);

        SendAndResolve.getInstance().send(HttpMethod.POST,requestData);
    }
}
