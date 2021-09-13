package com.zebone.nhis.ma.pub.platform.send.impl.zsrm.service;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.zebone.nhis.common.module.base.bd.code.BdAdminDivision;
import com.zebone.nhis.common.module.base.bd.code.BdDefdoc;
import com.zebone.nhis.common.module.base.bd.res.BedExtendVO;
import com.zebone.nhis.common.module.pi.PiMaster;
import com.zebone.nhis.common.support.EnumerateParameter;
import com.zebone.nhis.ma.pub.platform.send.impl.zsrm.dao.ZsrmDictMapper;
import com.zebone.nhis.ma.pub.platform.send.impl.zsrm.support.MsgIndexData;
import com.zebone.nhis.ma.pub.platform.send.impl.zsrm.support.RequestData;
import com.zebone.nhis.ma.pub.platform.send.impl.zsrm.support.RequestTemplate;
import com.zebone.nhis.ma.pub.platform.send.impl.zsrm.support.SendAndResolve;
import com.zebone.nhis.ma.pub.platform.send.impl.zsrm.vo.base.*;
import com.zebone.nhis.ma.pub.platform.send.impl.zsrm.vo.bd.CodeSystem;
import com.zebone.nhis.ma.pub.platform.send.impl.zsrm.vo.bd.Concept;
import com.zebone.nhis.ma.pub.platform.send.impl.zsrm.vo.dict.BedResource;
import com.zebone.nhis.ma.pub.platform.send.impl.zsrm.vo.patient.*;
import com.zebone.platform.common.support.NHISUUID;
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
import java.util.function.UnaryOperator;

@Service
public class ZsphPlatFormSendAdtHandler {
    private static Logger log = LoggerFactory.getLogger("nhis.lbHl7Log");

    @Resource
    private RequestTemplate sendTool;

    @Resource
    private ZsrmDictMapper zsrmDictMapper;

    /**
     * 查询患者基本信息
     * @param paramMap
     */
    public void sendPiMasterMsg(Map<String, Object> paramMap){
        //1.根据pkPi查询患者就诊信息p
        PiMaster piMaster = DataBaseHelper.queryForBean("select * from pi_master where pk_pi = ? ",PiMaster.class,MapUtils.getString(paramMap, "pkPi"));
        if(piMaster==null){
            throw new BusException("依据pkPi未获取到患者信息:"+MapUtils.getString(paramMap, "pkPi"));
        }
        //2.组装数据
        boolean flagAdd = EnumerateParameter.ZERO.equals(MapUtils.getString(paramMap, "isAdd","1"));
        String id = NHISUUID.getKeyId();
        BusinessBase businessBase = new BusinessBase();
        businessBase.setResourceType("Bundle");
        businessBase.setId(id);
        businessBase.setType("message");
        businessBase.setTimestamp(new Date());
        businessBase.setEntry(new ArrayList<>());

        Entry entryHead = new Entry();

        entryHead.setResource(new PhResource());
        PhResource phResource = entryHead.getResource();
        phResource.setResourceType("MessageHeader");
        phResource.setId(NHISUUID.getKeyId());
        phResource.setDestination(Arrays.asList(new Destination("PT")));
        phResource.setSource(new Destination("HIS"));
        businessBase.getEntry().add(entryHead);
        Entry entry = new Entry();
        //组装患者信息
        Patient patient = new Patient();
        patient.setResourceType("Patient");
        patient.setImplicitRules(flagAdd?"HZXXXZ":"HZXXXG");//新增还是更新 （HZXXXZ 新增）（HZXXXG 更新）
        patient.setIdentifier(new ArrayList<>());
        patient.getIdentifier().add(new Identifier("id/patientno",piMaster.getCodeOp()));//病人唯一
        patient.getIdentifier().add(new Identifier("id/cardno",piMaster.getHicNo()));//病人健康卡
        patient.getIdentifier().add(new Identifier("id/healthcardno",piMaster.getHicNo()));//健康卡号
        patient.getIdentifier().add(new Identifier("id/medicalinsurance",piMaster.getInsurNo()));//医保卡号
        patient.getIdentifier().add(new Identifier("id/zshealthcardno",piMaster.getCitizenNo()));//中山健康卡号
        patient.getIdentifier().add(new Identifier("id/healthcardid",piMaster.getHicNo()==null?"":piMaster.getHicNo()));//电子健康卡id号
        BdDefdoc bdDefdocdIdtype = getBdDefdocInfo("000007",piMaster.getDtIdtype());
        if(bdDefdocdIdtype != null){
            patient.getIdentifier().add(new Identifier(null,"id/idno",new TextElement(bdDefdocdIdtype.getName()),piMaster.getIdNo(),"","",""));//身份证号
        }else{
            patient.getIdentifier().add(new Identifier(null,"id/idno",new TextElement(),piMaster.getIdNo(),"","",""));//身份证号
        }

        patient.setActive(EnumerateParameter.ONE.equals(piMaster.getDelFlag())?false:true);//是否可用
        patient.setName(Arrays.asList(new TextElement(piMaster.getNamePi())));//患者姓名
        patient.setTelecom(new ArrayList<>());
        patient.getTelecom().add(new Identifier("phone",piMaster.getMobile()));//联系方式
        patient.getTelecom().add(new Identifier("email",piMaster.getEmail()));//邮箱

        patient.setGender("02".equals(piMaster.getDtSex())?"male":"03".equals(piMaster.getDtSex())?"female":"other");//患者性别
        patient.setBirthDate(piMaster.getBirthDate());//出生日期
        patient.setAddress(new ArrayList<>());//患者地址

        //患者地址
        Address address = new Address();
        address.setUse("home");
        address.setText(piMaster.getAddrCur()+piMaster.getAddrCurDt());//现住详细地址
        address.setLine(piMaster.getAddrCurDt());//村（街、路、弄等）+门牌号
        address.setPostalCode(piMaster.getPostcodeCur());//邮政编码
        //查询省市区
        BdAdminDivision bdAdminDivision = DataBaseHelper.queryForBean("SELECT prov,city,dist from bd_admin_division where code_div = ? ",BdAdminDivision.class,piMaster.getAddrcodeCur());
        if(bdAdminDivision != null){
            address.setCity(bdAdminDivision.getCity());//市
            address.setDistrict(bdAdminDivision.getDist());//区
            address.setState(bdAdminDivision.getProv());//省
        }
        address.setExtension(new ArrayList<>());
        address.getExtension().add(new Extension("township",piMaster.getAddrCurDt(),null));
        address.getExtension().add(new Extension("houseno","",null));
        patient.getAddress().add(address);
        patient.getAddress().add(new Address("work",piMaster.getPostcodeWork()));
        patient.getAddress().add(new Address("birth",piMaster.getAddrBirth()));
        patient.getAddress().add(new Address("account",piMaster.getAddrRegiDt()));

        //婚姻状况
        BdDefdoc bdDefdocMarry = getBdDefdocInfo("000006",piMaster.getDtMarry());
        if(bdDefdocMarry != null){
            patient.setMaritalStatus(new CodeableConcept(Arrays.asList(new Coding(null,piMaster.getDtMarry(),bdDefdocMarry.getName()))));
        }
        patient.setContact(new ArrayList<>());//患者联系人

        //联系人关系
        Contact contact = new Contact();
        BdDefdoc bdDefdocdRalation = getBdDefdocInfo("000013",piMaster.getDtRalation());
        if(bdDefdocdRalation != null){
            contact.setRelationship(Arrays.asList(new CodeableConcept(Arrays.asList(new Coding("relationship",piMaster.getDtRalation(),bdDefdocdRalation.getName())))));
        }
        contact.setName(new TextElement(piMaster.getNameRel()));//联系人姓名
        contact.setTelecom(Arrays.asList(new Identifier("phone",piMaster.getTelRel())));//联系人联系方式

        //联系人地址
        Address addressRel = new Address();
        addressRel.setText(piMaster.getAddrRel());//联系人地址
        addressRel.setLine(piMaster.getAddrRel());//联系人地址
        //addressRel.setPostalCode("");//无
        //addressRel.setCity("");//无
        //addressRel.setDistrict("");//无
        //addressRel.setState("");//无
        contact.setAddress(addressRel);
        //contact.setGender("");//无
        patient.setContact(Arrays.asList(contact));

        Link link = new Link();
        BdDefdoc bdDefdocdIdtypeRel = getBdDefdocInfo("000007",piMaster.getDtIdtypeRel());
        if(bdDefdocdIdtypeRel != null){
            link.setOther(new Other("RelatedPerson",Arrays.asList(new Identifier(null,"id/idno",new TextElement(bdDefdocdIdtypeRel.getName()),piMaster.getIdnoRel(),"","",""))));
        }else{
            link.setOther(new Other("RelatedPerson",Arrays.asList(new Identifier(null,"id/idno",new TextElement(),piMaster.getIdnoRel(),"","",""))));
        }
        link.setName(Arrays.asList(new TextElement(piMaster.getNameRel())));
        link.setTelecom(Arrays.asList(new Identifier("phone",piMaster.getTelRel())));
        patient.setExtension(new ArrayList<>());
        //国籍 民族 籍贯 血型
        BdDefdoc bdDefdocdCountry = getBdDefdocInfo("000013",piMaster.getDtRalation());
        //国籍
        Extension etDtExt1 = new Extension();
        etDtExt1.setUrl("country");
        if(bdDefdocdCountry != null){
            etDtExt1.setValueCodeableConcept(new CodeableConcept(Arrays.asList(new Coding(null,piMaster.getDtCountry(),bdDefdocdCountry.getName()))));
        }else{
            etDtExt1.setValueCodeableConcept(new CodeableConcept(Arrays.asList(new Coding(null,"",""))));
        }

        //民族
        BdDefdoc bdDefdocdNation = getBdDefdocInfo("000003",piMaster.getDtNation());
        Extension etDtExt2 = new Extension();
        etDtExt2.setUrl("ethnicgroup");
        if(bdDefdocdNation != null){
            etDtExt2.setValueCodeableConcept(new CodeableConcept(Arrays.asList(new Coding(null,piMaster.getDtNation(),bdDefdocdNation.getName()))));
        }else{
            etDtExt2.setValueCodeableConcept(new CodeableConcept(Arrays.asList(new Coding(null,"",""))));
        }

        //籍贯
        Extension etDtExt3 = new Extension();
        etDtExt3.setUrl("nativeplace");
        etDtExt3.setValueString(piMaster.getAddrOrigin());

        //ABO血型
        BdDefdoc bdDefdocdBloodAbo = getBdDefdocInfo("000004",piMaster.getDtBloodAbo());
        Extension etDtExt4 = new Extension();
        etDtExt4.setUrl("aboblood");
        if(bdDefdocdBloodAbo != null){
            etDtExt4.setValueCodeableConcept(new CodeableConcept(Arrays.asList(new Coding(null,piMaster.getDtBloodAbo(),bdDefdocdBloodAbo.getName()))));
        }else{
            etDtExt4.setValueCodeableConcept(new CodeableConcept(Arrays.asList(new Coding(null,"",""))));
        }

        //RH血型
        BdDefdoc bdDefdocdBloodRh = getBdDefdocInfo("000005",piMaster.getDtBloodRh());
        Extension etDtExt5 = new Extension();
        etDtExt5.setUrl("rhblood");
        if(bdDefdocdBloodRh != null){
            etDtExt5.setValueCodeableConcept(new CodeableConcept(Arrays.asList(new Coding(null,piMaster.getDtBloodRh(),bdDefdocdBloodRh.getName()))));
        }else{
            etDtExt5.setValueCodeableConcept(new CodeableConcept(Arrays.asList(new Coding(null,"",""))));
        }

        //周期标识
        Extension etDtExt6 = new Extension();
        etDtExt6.setUrl("dateperiod");
        etDtExt6.setValuePeriod(new ValuePeriod(piMaster.getCreateTime(),piMaster.getTs()));

        //学历
        BdDefdoc bdDefdocdEdu= getBdDefdocInfo("010302",piMaster.getDtEdu());
        Extension etDtExt7 = new Extension();
        etDtExt7.setUrl("education");
        if(bdDefdocdEdu != null){
            etDtExt7.setValueString(bdDefdocdEdu.getName());
        }else{
            etDtExt7.setValueString("");
        }

        //职业
        BdDefdoc bdDefdocdOccu= getBdDefdocInfo("000010",piMaster.getDtOccu());
        Extension etDtExt8 = new Extension();
        etDtExt8.setUrl("occupation");
        if(bdDefdocdOccu != null){
            etDtExt8.setValueString(bdDefdocdOccu.getName());
        }else{
            etDtExt8.setValueString("");
        }

        patient.getExtension().add(etDtExt1);
        patient.getExtension().add(etDtExt2);
        patient.getExtension().add(etDtExt3);
        patient.getExtension().add(etDtExt4);
        patient.getExtension().add(etDtExt5);
        patient.getExtension().add(etDtExt6);
        patient.getExtension().add(etDtExt7);
        patient.getExtension().add(etDtExt8);
        patient.getExtension().add(new Extension("insurance","",null));//无
        patient.getExtension().add(new Extension("workcompany",piMaster.getUnitWork(),null));
        patient.getExtension().add(new Extension("insuredno",piMaster.getInsurNo(),null));
        patient.getExtension().add(new Extension("realnameflag","1".equals(piMaster.getFlagRealname()) ? true:false));
        patient.getExtension().add(new Extension("phoneflag","1".equals(piMaster.getFlagRealmobile()) ? true:false));
        entry.setResource(patient);
        businessBase.getEntry().add(entry);
        RequestData requestData = RequestData.newBuilder()
                .id(id)
                .data(JSON.toJSONString(businessBase))
                .remoteMethod("Patient")
                .msgIndexData(MsgIndexData.newBuilder().codeOp(piMaster.getCodeOp()).codePi(piMaster.getCodePi()).codeIp(piMaster.getCodeIp()).build())
                .build();
        log.info("json 实体：" + requestData.getData());
        SendAndResolve.getInstance().send(flagAdd ? HttpMethod.POST : HttpMethod.PUT, requestData);
    }


    /**
     * 获取公共字典
     * @param code
     * @return
     */
    public BdDefdoc getBdDefdocInfo(String codeDefdoclist,String code){
        return DataBaseHelper.queryForBean("SELECT name from bd_defdoc where code_defdoclist = ? and code = ?", BdDefdoc.class,codeDefdoclist,code);
    }


    /**
     * 发送床位变更信息
     * @param paramMap
     */
    public void sendBdResBedMsg(Map<String, Object> paramMap) {
        Map<String,Object> delBed;
        List<BedExtendVO> bedList;
        final Map<String,Object> objectMap = Maps.newHashMap();
        if(MapUtils.isNotEmpty(delBed = MapUtils.getMap(paramMap,"delBed"))){
            UnaryOperator<Map<String,Object>> operator = (x)-> x==null?Maps.newHashMap():x;
            objectMap.putAll(delBed);
            objectMap.put("status","inactive");

            objectMap.putAll(operator.apply(DataBaseHelper.queryForMap("select CODE_DEPT,NAME_DEPT from bd_ou_dept where PK_DEPT=?"
                    ,new Object[]{MapUtils.getString(delBed,"pkDept")})));
            objectMap.putAll(operator.apply((DataBaseHelper.queryForMap("select CODE_DEPT code_ward ,NAME_DEPT name_ward from bd_ou_dept where PK_DEPT=?"
                    ,new Object[]{MapUtils.getString(delBed,"pkWard")}))));
            objectMap.putAll(operator.apply((DataBaseHelper.queryForMap("select code code_type,name name_type from BD_DEFDOC where CODE_DEFDOCLIST='010400' and CODE=?"
                    ,new Object[]{MapUtils.getString(delBed,"dtBedtype")}))));
            objectMap.putAll(operator.apply((DataBaseHelper.queryForMap("select CODE_EMP code_doctor,NAME_EMP name_doctor from bd_ou_employee where PK_EMP=?"
                    ,new Object[]{MapUtils.getString(delBed,"pkPhyEmp")}))));
            objectMap.putAll(operator.apply((DataBaseHelper.queryForMap("select CODE_EMP code_nurse,NAME_EMP name_nurse from bd_ou_employee where PK_EMP=?"
                    ,new Object[]{MapUtils.getString(delBed,"pkNsEmp")}))));
        } else if(CollectionUtils.isNotEmpty(bedList = (List<BedExtendVO>)MapUtils.getObject(paramMap,"resbedList"))){
            List<Map<String,Object>> mapList  = zsrmDictMapper.queryBedInfoByPk(bedList.get(0).getPkBed());
            if(mapList.size()>0){
                objectMap.putAll(mapList.get(0));
            }
        }
        if(MapUtils.isEmpty(objectMap)) {
            log.info("发送床位变更信息-未获取到数据");
            return;
        }

        log.info("发送床位变更信息开始");
        String id = NHISUUID.getKeyId();
        BedResource bedResource = new BedResource();
        bedResource.setResourceType("Location");
        bedResource.setId(id);
        bedResource.setImplicitRules("CWXXZD");
        bedResource.setStatus(MapUtils.getString(objectMap,"status"));
        bedResource.setName(MapUtils.getString(objectMap,"name"));
        bedResource.setIdentifier(Lists.newArrayList(new Identifier("bedNo",MapUtils.getString(objectMap,"code"))
                ,new Identifier("deptCode",MapUtils.getString(objectMap,"codeDept",""))
                ,new Identifier("wardCode",MapUtils.getString(objectMap,"codeWard",""))));
        bedResource.setPhysicalType(new CodeableConcept(Lists.newArrayList(new Coding(MapUtils.getString(objectMap,"codeType",""),MapUtils.getString(objectMap,"nameType","")))));

        Coding doctor = new Coding(MapUtils.getString(objectMap,"codeDoctor",""),MapUtils.getString(objectMap,"nameDoctor",""));
        Coding nurse = new Coding(MapUtils.getString(objectMap,"codeNurse",""),MapUtils.getString(objectMap,"nameNurse",""));
        Extension doctorExt = new Extension();
        doctorExt.setUrl("doctor");
        doctorExt.setValueCoding(doctor);
        Extension nurseExt = new Extension();
        nurseExt.setUrl("nurse");
        nurseExt.setValueCoding(nurse);
        bedResource.setExtension(Lists.newArrayList(doctorExt,nurseExt));

        RequestData requestData = RequestData.newBuilder()
                .id(id)
                .data(JSON.toJSONString(bedResource))
                .remoteMethod("Location")
                .build();
        log.info("发送床位变更信息构造的数据{}",requestData.getData());
        SendAndResolve.getInstance().send(HttpMethod.POST,requestData);
    }

    public void sendBdItemcateMsg(Map<String, Object> paramMap) {
        Map<String,Object> item = MapUtils.getMap(paramMap, "item");
        boolean flagDel = "_DELETE".equals(MapUtils.getString(paramMap, "status"));
        CodeSystem cs = new CodeSystem();
        cs.setResourceType("CodeSystem");
        cs.setId(NHISUUID.getKeyId());
        cs.setImplicitRules("SFXMFLZD");
        cs.setConcept(Lists.newArrayList(new Concept(MapUtils.getString(item, "code")
                ,MapUtils.getString(item, "name")
                ,Lists.newArrayList(new Property("isactive",!flagDel)))));

        RequestData requestData = RequestData.newBuilder()
                .id(cs.getId())
                .data(JSON.toJSONString(cs))
                .remoteMethod("Location")
                .build();
        log.info("收费项目类型字典构造的数据{}",requestData.getData());
        SendAndResolve.getInstance().send(HttpMethod.POST,requestData);
    }
}
