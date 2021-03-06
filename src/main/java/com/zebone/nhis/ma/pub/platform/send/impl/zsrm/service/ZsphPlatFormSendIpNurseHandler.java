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
     * ????????????????????????
     * @param paramMap
     */
    public void sendDeptInMsg(Map<String, Object> paramMap){
        //PvAdtService public void savePatisPvIn
        RequestData requestData = buildDeptInReq(paramMap, "ZYRKXX","arrived");
        log.info("??????????????????????????????id:{},data:{},remoteMethod:{},implicitRules:{}"
                ,requestData.getId(),requestData.getData(),requestData.getRemoteMethod(),"ZYRKXX");

        SendAndResolve.getInstance().send(HttpMethod.POST,requestData);
    }

    /**
     * ????????????
     * @param paramMap
     */
    public void sendCancelDeptInMsg(Map<String, Object> paramMap){
        RequestData requestData = buildDeptInReq(paramMap, "ZYRKQX","cancelled");
        log.info("????????????-??????????????????????????????id:{},data:{},remoteMethod:{},implicitRules:{}"
                ,requestData.getId(),requestData.getData(),requestData.getRemoteMethod(), "ZYRKQX");
        SendAndResolve.getInstance().send(HttpMethod.POST,requestData);
    }

    /**
     * ??????
     * @param paramMap
     */
    public void sendDeptChangeMsg(Map<String, Object> paramMap){
        RequestData requestData = buildDeptChange(paramMap, "ZYZKXX", "arrived");
        log.info("??????????????????????????????id:{},data:{},remoteMethod:{},implicitRules:{}"
                ,requestData.getId(),requestData.getData(),requestData.getRemoteMethod(), "ZYZKXX");
        SendAndResolve.getInstance().send(HttpMethod.POST,requestData);
    }

    /**
     * ??????--??????
     * @param paramMap
     */
    public void sendCancelDeptChangeMsg(Map<String, Object> paramMap){
        List<String> pkPvs = (List<String>) MapUtils.getObject(paramMap, "pkPvs");
        if(CollectionUtils.isNotEmpty(pkPvs)){
            for (String pkPv : pkPvs) {
                paramMap.put("pkPv",pkPv);
                RequestData requestData = buildDeptChange(paramMap, "ZYZKQX", "cancelled");
                log.info("????????????-????????????????????????id:{},data:{},remoteMethod:{},implicitRules:{},pkPv:{}"
                        ,requestData.getId(),requestData.getData(),requestData.getRemoteMethod(), "ZYZKQX",pkPv);
                SendAndResolve.getInstance().send(HttpMethod.POST,requestData);
            }
        }
    }

    /**
     * ??????
     * @param paramMap
     */
    public void sendBedPackMsg(Map<String, Object> paramMap){
        sendBedInfoPrivate(paramMap,"ZYBCXX","arrived");
    }

    /**
     * ????????????
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
     * ??????
     * @param paramMap
     */
    public void sendBedChangeMsg(Map<String,Object> paramMap){
        //??????????????????
        Map<String, Object> pvInfo = DataBaseHelper.queryForMap("select pv.CODE_PV,pi.CODE_PI,pi.CODE_OP,pi.CODE_IP,eo.code_emp,eo.name_emp,ip.IP_TIMES from PV_ENCOUNTER pv inner join PV_IP ip on pv.PK_PV=ip.PK_PV inner join PI_MASTER pi on pv.PK_PI=pi.PK_PI left join BD_OU_EMPLOYEE eo on pv.PK_EMP_TRE=eo.PK_EMP where pv.PK_PV=?",
                MapUtils.getString(paramMap, "pkPv"));
        Participant participant = new Participant();//????????????
        participant.setType(Lists.newArrayList(new CodeableConcept(Lists.newArrayList(new Coding("OrigDocCode", MapUtils.getString(pvInfo,"codeEmp"), MapUtils.getString(pvInfo,"nameEmp"))))));

        //????????????????????????????????????
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
        log.info("????????????-??????????????????id:{},data:{},remoteMethod:{},implicitRules:{}"
                ,requestData.getId(),requestData.getData(),requestData.getRemoteMethod(), bedData.getImplicitRules());
        SendAndResolve.getInstance().send(HttpMethod.POST,requestData);
    }
    
    /**
     * @Description ???????????????????????????
     */
    public void sendNewBornRegister(Map<String,Object> paramMap){
        String pkPvBb = MapUtils.getString(paramMap,"pkPvBb");
        String pkPvMother = MapUtils.getString(paramMap,"pkPv");
        String method = "_DELETE".equals(MapUtils.getString(paramMap, "STATUS"))?"XSRDJQX":"XSRDJXX";
        //????????????????????????
        List<PvEncounterVo> babyInfos = ipCnOperationMapper.qryPiPv(pkPvBb);
        if(babyInfos.size()==0)
            return;
        PvEncounterVo baby = babyInfos.get(0);
        //????????????????????????
        PvInfant pvInfant = DataBaseHelper.queryForBean("select nvl(inf.WEIGHT,0) WEIGHT,nvl(inf.LEN,0) LEN,nvl(inf.num_product,1) num_product from PV_INFANT inf" +
                " where inf.PK_PV=? and inf.PK_PV_INFANT=?", PvInfant.class, new Object[]{pkPvMother, pkPvBb});
        //??????????????????
        List<PvEncounterVo> motherInfos = ipCnOperationMapper.qryPiPv(MapUtils.getString(paramMap, "pkPvBb"));
        if(motherInfos.size()==0)
            return;
        PvEncounterVo mother = motherInfos.get(0);
        //????????????????????????
        List<Map<String, Object>> pvInfoList = ipNurseOperationMapper.qryPvInfo(pkPvMother);
        if(CollectionUtils.isEmpty(pvInfoList)){
            throw new BusException("????????????????????????");
        }
        Map<String, Object> pvInfo = pvInfoList.get(0);
        //???????????????????????????
        Map<String, String> locationMap = Maps.newHashMap();
        locationMap.put("dept",MapUtils.getString(pvInfo,"pvDept"));
        locationMap.put("ward",MapUtils.getString(pvInfo,"pvDeptArea"));
        locationMap.put("bedno",MapUtils.getString(pvInfo,"bedNo"));
        //????????????????????????????????????
        paramMap.put("codeEmp", UserContext.getUser().getCodeEmp());
        paramMap.put("nameEmp", UserContext.getUser().getNameEmp());
        List<Extension> extensions = buildDeptInExt(paramMap);


        //???????????????
        BabyAdmitResource resource = new BabyAdmitResource();
        BusinessBase businessBase = RequestBuild.create(resource);
        //???????????????Entry
        resource= (BabyAdmitResource) businessBase.getEntry().get(1).getResource();
        resource.setId(businessBase.getId());
        resource.setResourceType("Encounter");
        resource.setImplicitRules(method);
        resource.setStatus("finished");
        //??????diagnosis??????
        List<Diagnosis> diagnosisList = new ArrayList<>();
        Diagnosis diagnosis=new Diagnosis();
        diagnosis.setRank("????????????");
        //??????diagnosis????????????Condition??????
        Condition condition = new Condition();
        condition.setResourceType("Condition");
        List<Coding> codingList = new ArrayList<>();
        CodeableConcept code = new CodeableConcept(codingList);
        code.getCoding().add(new Coding("????????????","????????????"));
        condition.setCode(code);
        diagnosis.setCondition(condition);
        //??????diagnosis????????????use??????
        codingList = new ArrayList<>();
        CodeableConcept use = new CodeableConcept(codingList);
        use.getCoding().add(new Coding("diagnosisType","??????????????????","??????????????????"));
        diagnosis.setUse(use);
        diagnosisList.add(diagnosis);
        resource.setDiagnosis(diagnosisList);
        //??????extension??????
        extensions.add(new Extension("height",String.valueOf(pvInfant.getLen()),null));
        extensions.add(new Extension("weight",String.valueOf(pvInfant.getWeight()),null));
        extensions.add(new Extension("fetus_num","1",null));
        extensions.add(new Extension("parity_num",String.valueOf(pvInfant.getNumProduct()),null));
        resource.setExtension(extensions);
        //??????location??????
        resource.setLocation(buildDeptInLocation(locationMap));
        //??????Identifier??????
        resource.setIdentifier(Lists.newArrayList(
                 new Identifier("id/motherVisitNo",mother.getCodeIp())
                ,new Identifier("id/visitNo",baby.getCodeIp())
                ,new Identifier("id/visitTimes",baby.getIpTimes()!=null?baby.getIpTimes().toString():"")
                ,new Identifier("id/visitSerialNo",baby.getCodePv())));
        //??????subject
        Patient subject = new Patient();
        subject.setResourceType("Patient");
        subject.setBirthDate(baby.getBirthDate());//?????????????????????
        //??????subject??????Identifier??????
        subject.setIdentifier(Lists.newArrayList(new Identifier("code/patientId",baby.getCodePi())
                ,new Identifier("code/idNo",baby.getIdNo())));
        //??????subject??????name??????
        subject.setName(Lists.newArrayList(new TextElement(baby.getNamePi())));
        //??????subject??????contact??????
        List<Contact> contactList = new ArrayList<>();
        Contact contact=new Contact();
        //??????subject??????contact????????????name??????
        contact.setName(new TextElement(mother.getNamePi()));
        //??????subject??????contact????????????relationship??????
        BdDefdoc bdDefdoc = DataBaseHelper.queryForBean("SELECT name from bd_defdoc where code_defdoclist = ? and code = ?", BdDefdoc.class, new Object[]{"000013",baby.getDtRalation()});
        if(bdDefdoc != null){
            contact.setRelationship(Arrays.asList(new CodeableConcept(Arrays.asList(new Coding("relationship",baby.getDtRalation(),bdDefdoc.getName())))));
        }
        //??????subject??????contact????????????telecom??????
        List<Identifier> telecom = new ArrayList<>();
        telecom.add(new Identifier("phone",mother.getTelNo()));
        contact.setTelecom(telecom);
        //??????subject??????contact????????????extension??????
        List<Extension> extensionList = new ArrayList<>();
        Extension extension=new Extension();
        extension.setUrl("relationType");
        extension.setValueString("??????");
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
        log.info("??????????????????????????????id:{},data:{},remoteMethod:{},implicitRules:{}"
                ,requestData.getId(),requestData.getData(),requestData.getRemoteMethod(),resource.getImplicitRules());

        SendAndResolve.getInstance().send(HttpMethod.POST,requestData);
    }

    private void sendBedInfoPrivate(Map<String, Object> paramMap,String method,String status){
        List<String> bedlist = (List<String>) paramMap.get("bednos");
        if(CollectionUtils.isNotEmpty(bedlist)){
            //??????????????????
            Map<String, Object> pvInfo = DataBaseHelper.queryForMap("select pv.CODE_PV,pi.CODE_PI,pi.CODE_OP,pi.code_ip,ip.IP_TIMES from PV_ENCOUNTER pv inner join PV_IP ip on pv.PK_PV=ip.PK_PV inner join PI_MASTER pi on pv.PK_PI=pi.PK_PI where pv.PK_PV=?", MapUtils.getString(paramMap, "pkPv"));
            //???????????????????????????
            List<BdOuDept> list = ipNurseOperationMapper.qryBedInfo(paramMap);
            List<LocalLocation> locations = list.stream().map(dept -> {
                Location location = new Location();
                location.setResourceType("Location");
                location.setName(dept.getNameDept());
                location.setIdentifier(Lists.newArrayList(new Identifier(dept.getDeptType(), dept.getCodeDept())));
                return new LocalLocation(location);
            }).collect(Collectors.toList());
            //??????????????????
            bedlist.forEach(vo ->{
                Location location = new Location();
                location.setResourceType("Location");
                location.setIdentifier(Lists.newArrayList(new Identifier("bedno", vo)));
                locations.add(new LocalLocation(location));
            });
            //????????????????????????????????????
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
            log.info("????????????-{}??????????????????id:{},status:{},data:{},remoteMethod:{},implicitRules:{}"
                    ,requestData.getId(),status,requestData.getData(),requestData.getRemoteMethod(), encounter.getImplicitRules());
            SendAndResolve.getInstance().send(HttpMethod.POST,requestData);
        }
    }


    private RequestData buildDeptInReq(Map<String, Object> paramMap,String method,String status){
        String pkPv;
        if(StringUtils.isBlank(pkPv=MapUtils.getString(paramMap,"pkPv"))){
            throw new BusException("?????????Pkpv");
        }
        List<Map<String, Object>> pvInfoList = ipNurseOperationMapper.qryPvInfo(pkPv);
        if(CollectionUtils.isEmpty(pvInfoList)){
            throw new BusException("????????????????????????");
        }
        Map<String, Object> pvInfo = pvInfoList.get(0);
        //???????????????????????????
        boolean out = "ZYCKXX".equals(method) || "ZYCKQX".equals(method);//?????????????????????????????????
        Map<String, String> locationMap = Maps.newHashMap();
        locationMap.put(out?"outdept":"curdept",MapUtils.getString(pvInfo,"pvDept"));
        locationMap.put(out?"outward":"curward",MapUtils.getString(pvInfo,"pvDeptArea"));
        locationMap.put("indept",MapUtils.getString(pvInfo,"codeDept"));
        locationMap.put("inward",MapUtils.getString(pvInfo,"codeDeptArea"));
        locationMap.put("bedno",MapUtils.getString(pvInfo,"bedNo"));
        //????????????????????????????????????
        List<Extension> extensions = buildDeptInExt(paramMap);
        //???????????????????????????
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
        Participant participant = new Participant();//????????????--?????????????????????????????????
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
            throw new BusException("?????????Pkpv");
        }
        Map map = new HashMap<String, Object>();
        map.put("pkPv", pkPv);
        List<Map<String, Object>> pvInfoList = ipNurseOperationMapper.qryChangeDeptInfo(map);
        if (CollectionUtils.isEmpty(pvInfoList)) {
            throw new BusException("????????????????????????");
        }
        Map<String, Object> pvInfo = pvInfoList.get(0);
        //????????????????????????????????????
        Map<String, String> locationMap = Maps.newHashMap();
        locationMap.put("OrigWardCode", MapUtils.getString(pvInfo, "codeDeptArea"));
        locationMap.put("OrigBedCode", MapUtils.getString(pvInfo, "bedNo"));
        locationMap.put("TargWardCode", MapUtils.getString(pvInfo, "codeDeptTArea"));
        locationMap.put("TargBedCode", MapUtils.getString(pvInfo, "bedNo"));

        //????????????????????????????????????
        List<Extension> extensions = buildDeptInExt(paramMap);
        //??????????????????
        Participant participant = new Participant();//????????????--??????????????????????????????????????????????????????
        participant.setType(Lists.newArrayList(new CodeableConcept(Lists.newArrayList(new Coding("OrigDocCode", MapUtils.getString(pvInfo,"codeEmp"), MapUtils.getString(pvInfo,"nameEmp"))))));

        NurseOperation.Hospitalization hospitalization = new NurseOperation.Hospitalization("OrigDeptCode",MapUtils.getString(pvInfo, "codeDept"), "TargDocCode",MapUtils.getString(pvInfo, "codeDeptT"));
        Numerator numerator = new Numerator();//??????????????????,????????????
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
     * ??????????????????
     * @param paramMap
     */
    public void sendPvOutMsg(Map<String, Object> paramMap) {
        String method = MapUtils.getString(paramMap,"implicitRules");
        RequestData requestData = buildDeptInReq(paramMap, method,"finished");
        log.info("????????????/??????????????????????????????id:{},data:{},remoteMethod:{},implicitRules:{}"
                ,requestData.getId(),requestData.getData(),requestData.getRemoteMethod(),method);

        SendAndResolve.getInstance().send(HttpMethod.POST,requestData);
    }
}
