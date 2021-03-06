package com.zebone.nhis.ma.pub.platform.zsrm.service.impl;

import com.google.common.collect.Lists;
import com.zebone.nhis.common.module.base.bd.code.BdAdminDivision;
import com.zebone.nhis.common.module.base.bd.code.BdDefdoc;
import com.zebone.nhis.common.module.pi.PiInsurance;
import com.zebone.nhis.common.module.pi.PiMaster;
import com.zebone.nhis.common.support.*;
import com.zebone.nhis.ma.pub.platform.send.impl.zsrm.common.TransfTool;
import com.zebone.nhis.ma.pub.platform.send.impl.zsrm.common.ZsphConstant;
import com.zebone.nhis.ma.pub.platform.send.impl.zsrm.vo.base.*;
import com.zebone.nhis.ma.pub.platform.send.impl.zsrm.vo.patient.*;
import com.zebone.nhis.ma.pub.platform.send.impl.zsrm.vo.pv.ConditionRecorder;
import com.zebone.nhis.ma.pub.platform.send.impl.zsrm.vo.pv.PvDiagCondition;
import com.zebone.nhis.ma.pub.platform.zsrm.dao.ZsrmPiMapper;
import com.zebone.nhis.ma.pub.platform.zsrm.model.message.RequestBody;
import com.zebone.nhis.ma.pub.platform.zsrm.service.ZsrmPiService;
import com.zebone.nhis.ma.pub.platform.zsrm.support.QueryUtils;
import com.zebone.nhis.ma.pub.platform.zsrm.support.ZsphMsgUtils;
import com.zebone.nhis.ma.pub.zsrm.service.ZsrmEhealthCodeService;
import com.zebone.platform.common.support.User;
import com.zebone.platform.common.support.UserContext;
import com.zebone.platform.modules.dao.DataBaseHelper;
import com.zebone.platform.modules.exception.BusException;
import com.zebone.platform.web.support.ResponseJson;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.cglib.beans.BeanMap;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;

@Service
public class ZsrmPiServiceImpi implements ZsrmPiService {
    @Resource
    private ZsrmPiMapper zsphPiMapper;
    @Resource
    private ZsrmEhealthCodeService zsrmEhealthCodeService;
    @Override
    public List<Entry> getpiMasterZsInfo(String param) {
        RequestBody requestBody = ZsphMsgUtils.fromJson(param, RequestBody.class);
        List<Parameter> parameterList = ZsphMsgUtils.createParameterList(requestBody);
        if(CollectionUtils.isEmpty(parameterList)){
            throw new BusException("??????????????????????????????");
        }
        Map<String, Object> piMap = new HashMap<>();
        for (Parameter parameter:parameterList){
            if("patientno".equals(parameter.getName())){
                String codeOp = parameter.getValueString();
                if(StringUtils.isNotBlank(codeOp)){
                    piMap.put("codeOp", codeOp);
                }
            }else if("Id_card".equals(parameter.getName())){
                String idNo = parameter.getValueString();
                if(StringUtils.isNotBlank(idNo)){
                    piMap.put("idNo", idNo);
                }
            }else if("Name".equals(parameter.getName())){
                String namePi = parameter.getValueString();
                if(StringUtils.isNotBlank(namePi)){
                    piMap.put("namePi", namePi);
                }
            }else if("Date".equals(parameter.getName())){
                String birthDate = parameter.getValueString();
                if(StringUtils.isNotBlank(birthDate)){
                    piMap.put("birthDate", birthDate);
                }
            }else if("idtype".equals(parameter.getName())){
                String idtype = parameter.getValueString();
                if(StringUtils.isNotBlank(idtype)){
                    piMap.put("dtIdtype", idtype);
                }
            }else if("linkIdCard".equals(parameter.getName())){
                String idnoRel = parameter.getValueString();
                if(StringUtils.isNotBlank(idnoRel)){
                    piMap.put("idnoRel", idnoRel);
                }
            }else if("cardno".equals(parameter.getName())){
                String cardNo = parameter.getValueString();
                if(StringUtils.isNotBlank(cardNo)){
                    piMap.put("cardNo", cardNo);
                }
            }else if("medicalinsurance".equals(parameter.getName())){
                String insurNo = parameter.getValueString();
                if(StringUtils.isNotBlank(insurNo)){
                    piMap.put("insurNo", insurNo);
                }
            }else if("insuredno".equals(parameter.getName())){
                String mcno = parameter.getValueString();
                if(StringUtils.isNotBlank(mcno)){
                    piMap.put("mcno", mcno);
                }
            }else if("healthcardno".equals(parameter.getName())){
                String hicNo = parameter.getValueString();
                if(StringUtils.isNotBlank(hicNo)){
                    piMap.put("hicNo", hicNo);
                }
            }else if("healthEno".equals(parameter.getName())){
                String hicNo = parameter.getValueString();
                if(StringUtils.isNotBlank(hicNo)){
                    piMap.put("hicNo", hicNo);
                }
            }else if("healthQRno".equals(parameter.getName())){
                String hicNo = parameter.getValueString();
                Map<String,String> map = new HashMap<>();
                if(StringUtils.isNotBlank(hicNo)){
                    try {
                        map = zsrmEhealthCodeService.eHealthCodeEHCA1027(hicNo);
                    }catch (Exception e){
                        throw new BusException("????????????????????????????????????????????????");
                    }
                    if(StringUtils.isNotBlank(MapUtils.getString(map,"ehealthCode"))){
                        piMap.put("hicNo",MapUtils.getString(map,"ehealthCode"));
                    }else{
                        throw new BusException("????????????????????????????????????????????????");
                    }

                }
            }else if("healthcardEwm".equals(parameter.getName())){
                String hicNo = parameter.getValueString();
                if(StringUtils.isNotBlank(hicNo)){
                    piMap.put("hicNo", hicNo);
                }
            }
        }

        if(MapUtils.isEmpty(piMap)){
            throw new BusException("???????????????????????????");
        }
        //??????????????????
        String dtIdtype = MapUtils.getString(piMap,"dtIdtype");
        if(StringUtils.isNotBlank(dtIdtype)){
        	/*
            if(StringUtils.isBlank(MapUtils.getString(piMap,"namePi"))||StringUtils.isBlank(MapUtils.getString(piMap,"idNo"))){
                throw new BusException("?????????????????????Id_card+Name+idtype???");
            }
            */
        	if(StringUtils.isBlank(MapUtils.getString(piMap,"idNo"))){
                throw new BusException("????????????????????????Id_card+Name+idtype????????????Id_card+idtype??????");
            }
        	if("01".equals(dtIdtype)&&MapUtils.getString(piMap,"idNo").length()<=8){
                throw new BusException("??????????????????????????????Id_card???????????????????????????");
            }
            if("01".equals(dtIdtype)){
                piMap.put("dtIdtype","01");
            }else if("03".equals(dtIdtype)){
                piMap.put("dtIdtype","02");
            }else if("04".equals(dtIdtype)){
                piMap.put("dtIdtype","03");
            }else if("06".equals(dtIdtype)){
                piMap.put("dtIdtype","04");
            }else if("07".equals(dtIdtype)){
                piMap.put("dtIdtype","05");
            }else{
                piMap.put("dtIdtype","99");
            }
        }
        List<PiMaster> piMastersList =  zsphPiMapper.getPiMaster(piMap);
        //PiMaster piMaster = DataBaseHelper.queryForBean("select * from pi_master where code_pi=?", PiMaster.class, codePi);
        List<Entry> entryList = new ArrayList<>();
        if(piMastersList.size()<=0){
            Entry entry = new Entry(new Response());
            Response response = entry.getResponse();
            response.setStaus(ZsphConstant.RES_SUS_OTHER);
            response.setOutcome(null);
            entryList.add(entry);
            return entryList;
        }
        for (PiMaster piMaster:piMastersList){
            //??????????????????
            PatientQry patient = new PatientQry();
            patient.setResourceType("Patient");
            patient.setImplicitRules("HZXXXZ");
            patient.setIdentifier(new ArrayList<>());
            patient.getIdentifier().add(new Identifier("id/patientno",piMaster.getCodeOp()==null?"":piMaster.getCodeOp()));//????????????
            patient.getIdentifier().add(new Identifier("id/cardno",piMaster.getHicNo()==null?"":piMaster.getHicNo()));//???????????????
            patient.getIdentifier().add(new Identifier("id/healthcardno",piMaster.getHicNo()==null?"":piMaster.getHicNo()));//????????????
            patient.getIdentifier().add(new Identifier("id/medicalinsurance",piMaster.getInsurNo()==null?"":piMaster.getInsurNo()));//????????????
            patient.getIdentifier().add(new Identifier("id/zshealthcardno",piMaster.getCitizenNo()==null?"":piMaster.getCitizenNo()));//??????????????????
            patient.getIdentifier().add(new Identifier("id/healthcardid",piMaster.getHicNo()==null?"":piMaster.getHicNo()));//???????????????id???

            BdDefdoc bdDefdocdIdtype = getBdDefdocInfo("000007",piMaster.getDtIdtype());
            if(bdDefdocdIdtype != null){
                patient.getIdentifier().add(new Identifier(null,"id/idno",new TextElement(bdDefdocdIdtype.getName()),piMaster.getIdNo()==null?"":piMaster.getIdNo(),"","",""));//????????????
            }else{
                patient.getIdentifier().add(new Identifier(null,"id/idno",new TextElement(),piMaster.getIdNo(),"","",""));//????????????
            }

            patient.setActive(EnumerateParameter.ONE.equals(piMaster.getDelFlag())?false:true);//????????????
            patient.setName(Arrays.asList(new TextElement(piMaster.getNamePi())));//????????????
            patient.setTelecom(new ArrayList<>());
            patient.getTelecom().add(new Identifier("phone",piMaster.getMobile()==null?"":piMaster.getMobile()));//????????????
            patient.getTelecom().add(new Identifier("email",piMaster.getEmail()==null?"":piMaster.getEmail()));//??????

            patient.setGender("02".equals(piMaster.getDtSex())?"male":"03".equals(piMaster.getDtSex())?"female":"other");//????????????
            patient.setBirthDate(DateUtils.formatDate(piMaster.getBirthDate(),null));//????????????
            patient.setAddress(new ArrayList<>());//????????????

            //????????????
            Address address = new Address();
            address.setUse("home");
            address.setText(piMaster.getAddrCur()+piMaster.getAddrCurDt());//??????????????????
            address.setLine(piMaster.getAddrCurDt());//???????????????????????????+?????????
            address.setPostalCode(piMaster.getPostcodeCur());//????????????
            //???????????????
            BdAdminDivision bdAdminDivision = DataBaseHelper.queryForBean("SELECT prov,city,dist from bd_admin_division where code_div = ? ",BdAdminDivision.class,piMaster.getAddrcodeCur());
            if(bdAdminDivision != null){
                address.setCity(bdAdminDivision.getCity());//???
                address.setDistrict(bdAdminDivision.getDist());//???
                address.setState(bdAdminDivision.getProv());//???
            }
            address.setExtension(new ArrayList<>());
            address.getExtension().add(new Extension("township",piMaster.getAddrCurDt()==null?"":piMaster.getAddrCurDt(),null));
            address.getExtension().add(new Extension("houseno","",null));
            patient.getAddress().add(address);
            patient.getAddress().add(new Address("work",piMaster.getPostcodeWork()==null?"":piMaster.getPostcodeWork()));
            patient.getAddress().add(new Address("birth",piMaster.getAddrBirth()==null?"":piMaster.getAddrBirth()));
            patient.getAddress().add(new Address("account",piMaster.getAddrRegiDt()==null?"":piMaster.getAddrRegiDt()));

            //????????????
            BdDefdoc bdDefdocMarry = getBdDefdocInfo("000006",piMaster.getDtMarry());
            if(bdDefdocMarry != null){
                patient.setMaritalStatus(new CodeableConcept(Arrays.asList(new Coding(null,piMaster.getDtMarry(),bdDefdocMarry.getName()))));
            }
            patient.setContact(new ArrayList<>());//???????????????

            //???????????????
            Contact contact = new Contact();
            BdDefdoc bdDefdocdRalation = getBdDefdocInfo("000013",piMaster.getDtRalation());
            if(bdDefdocdRalation != null){
                contact.setRelationship(Arrays.asList(new CodeableConcept(Arrays.asList(new Coding("relationship",piMaster.getDtRalation(),bdDefdocdRalation.getName())))));
            }
            contact.setName(new TextElement(piMaster.getNameRel()==null?"":piMaster.getNameRel()));//???????????????
            contact.setTelecom(Arrays.asList(new Identifier("phone",piMaster.getTelRel()==null?"":piMaster.getTelRel())));//?????????????????????

            //???????????????
            Address addressRel = new Address();
            addressRel.setText(piMaster.getAddrRel()==null?"":piMaster.getAddrRel());//???????????????
            addressRel.setLine(piMaster.getAddrRel()==null?"":piMaster.getAddrRel());//???????????????
            //addressRel.setPostalCode("");//???
            //addressRel.setCity("");//???
            //addressRel.setDistrict("");//???
            //addressRel.setState("");//???
            contact.setAddress(addressRel);
            //contact.setGender("");//???
            patient.setContact(Arrays.asList(contact));

            Link link = new Link();
            BdDefdoc bdDefdocdIdtypeRel = getBdDefdocInfo("000007",piMaster.getDtIdtypeRel());
            if(bdDefdocdIdtypeRel != null){
                link.setOther(new Other("RelatedPerson",Arrays.asList(new Identifier(null,"id/idno",new TextElement(bdDefdocdIdtypeRel.getName()),piMaster.getIdnoRel(),"","",""))));
            }else{
                link.setOther(new Other("RelatedPerson",Arrays.asList(new Identifier(null,"id/idno",new TextElement(),piMaster.getIdnoRel(),"","",""))));
            }
            link.setName(Arrays.asList(new TextElement(piMaster.getNameRel())));
            link.setTelecom(Arrays.asList(new Identifier("phone",piMaster.getTelRel()==null?"":piMaster.getTelRel())));
            patient.setExtension(new ArrayList<>());
            //?????? ?????? ?????? ??????
            BdDefdoc bdDefdocdCountry = getBdDefdocInfo("000013",piMaster.getDtRalation());
            //??????
            Extension etDtExt1 = new Extension();
            etDtExt1.setUrl("country");
            if(bdDefdocdCountry != null){
                etDtExt1.setValueCodeableConcept(new CodeableConcept(Arrays.asList(new Coding(null,piMaster.getDtCountry(),bdDefdocdCountry.getName()))));
            }else{
                etDtExt1.setValueCodeableConcept(new CodeableConcept(Arrays.asList(new Coding(null,"",""))));
            }

            //??????
            BdDefdoc bdDefdocdNation = getBdDefdocInfo("000003",piMaster.getDtNation());
            Extension etDtExt2 = new Extension();
            etDtExt2.setUrl("ethnicgroup");
            if(bdDefdocdNation != null){
                etDtExt2.setValueCodeableConcept(new CodeableConcept(Arrays.asList(new Coding(null,piMaster.getDtNation(),bdDefdocdNation.getName()))));
            }else{
                etDtExt2.setValueCodeableConcept(new CodeableConcept(Arrays.asList(new Coding(null,"",""))));
            }

            //??????
            Extension etDtExt3 = new Extension();
            etDtExt3.setUrl("nativeplace");
            etDtExt3.setValueString(piMaster.getAddrOrigin());

            //ABO??????
            BdDefdoc bdDefdocdBloodAbo = getBdDefdocInfo("000004",piMaster.getDtBloodAbo());
            Extension etDtExt4 = new Extension();
            etDtExt4.setUrl("aboblood");
            if(bdDefdocdBloodAbo != null){
                etDtExt4.setValueCodeableConcept(new CodeableConcept(Arrays.asList(new Coding(null,piMaster.getDtBloodAbo(),bdDefdocdBloodAbo.getName()))));
            }else{
                etDtExt4.setValueCodeableConcept(new CodeableConcept(Arrays.asList(new Coding(null,"",""))));
            }

            //RH??????
            BdDefdoc bdDefdocdBloodRh = getBdDefdocInfo("000005",piMaster.getDtBloodRh());
            Extension etDtExt5 = new Extension();
            etDtExt5.setUrl("rhblood");
            if(bdDefdocdBloodRh != null){
                etDtExt5.setValueCodeableConcept(new CodeableConcept(Arrays.asList(new Coding(null,piMaster.getDtBloodRh(),bdDefdocdBloodRh.getName()))));
            }else{
                etDtExt5.setValueCodeableConcept(new CodeableConcept(Arrays.asList(new Coding(null,"",""))));
            }

            //????????????
            Extension etDtExt6 = new Extension();
            etDtExt6.setUrl("dateperiod");
            etDtExt6.setValuePeriod(new ValuePeriod(piMaster.getCreateTime(),piMaster.getTs()));

            //??????
            BdDefdoc bdDefdocdEdu= getBdDefdocInfo("010302",piMaster.getDtEdu());
            Extension etDtExt7 = new Extension();
            etDtExt7.setUrl("education");
            if(bdDefdocdEdu != null){
                etDtExt7.setValueString(bdDefdocdEdu.getName());
            }else{
                etDtExt7.setValueString("");
            }

            //??????
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
            patient.getExtension().add(new Extension("insurance","",null));//???
            patient.getExtension().add(new Extension("workcompany",piMaster.getUnitWork()==null?"":piMaster.getUnitWork(),null));
            patient.getExtension().add(new Extension("insuredno",piMaster.getInsurNo()==null?"":piMaster.getInsurNo(),null));
            patient.getExtension().add(new Extension("realnameflag","1".equals(piMaster.getFlagRealname()) ? true:false));
            patient.getExtension().add(new Extension("phoneflag","1".equals(piMaster.getFlagRealmobile()) ? true:false));

            //??????????????????
            Entry entry = new Entry(new Response());
            Response response = entry.getResponse();
            response.setStaus(ZsphConstant.RES_SUS_OTHER);
            response.setOutcome(BeanMap.create(patient));
            entryList.add(entry);
        }

        return entryList;
    }

    /**
     * ??????????????????
     * @param code
     * @return
     */
    public BdDefdoc getBdDefdocInfo(String codeDefdoclist,String code){
        return DataBaseHelper.queryForBean("SELECT name from bd_defdoc where code_defdoclist = ? and code = ?", BdDefdoc.class,codeDefdoclist,code);
    }

    /**
     * ??????????????????????????????
     * @param param
     * @return
     */
    @Override
    public List<Entry> getPvDiagZsInfo(String param) {
        RequestBody requestBody = ZsphMsgUtils.fromJson(param, RequestBody.class);
        List<Parameter> parameterList = ZsphMsgUtils.createParameterList(requestBody);
        if(CollectionUtils.isEmpty(parameterList)){
            throw new BusException("??????????????????????????????");
        }
        String codePv = null;
        for(Parameter parameter:parameterList){
            if("visitno".equals(parameter.getName())){
                codePv = parameterList.get(0).getValueString();
            }
        }
        if(StringUtils.isBlank(codePv)){
            throw new BusException("?????????????????????????????????????????????");
        }

        List<Entry> entryList = new ArrayList<>();
        //?????????????????????????????????????????????
        List<Map<String, Object>> pvDiagList = zsphPiMapper.getPvDiagCodePv(codePv);
        for(Map<String, Object> pvDiagMap:pvDiagList){
            PvDiagCondition pvDiagCondition = new PvDiagCondition();
            pvDiagCondition.setResourceType("Condition");
            pvDiagCondition.setId(requestBody.getId());
            pvDiagCondition.setImplicitRules("getdiagnosisinfo");
            if(EnumerateParameter.ONE.equals(TransfTool.getPropValueStr(pvDiagMap,"flagMaj"))){
                pvDiagCondition.setExtension(Arrays.asList(new Extension("isprimary",true)));
            }else{
                pvDiagCondition.setExtension(Arrays.asList(new Extension("isprimary",false)));
            }
            pvDiagCondition.setIdentifier(Arrays.asList(new Identifier("id/diagNo",TransfTool.getPropValueStr(pvDiagMap,"sortNo"))));
            //?????????????????????
            pvDiagCondition.setCategory(Lists.newArrayList(new CodeableConcept(Arrays.asList(new Coding(null,TransfTool.getPropValueStr(pvDiagMap,"dtDiagtype"),TransfTool.getBdDefdocName("060005",TransfTool.getPropValueStr(pvDiagMap,"dtDiagtype")))))));
            //????????????
            pvDiagCondition.setCode(new CodeableConcept(Arrays.asList(new Coding(null,TransfTool.getPropValueStr(pvDiagMap,"diagcode"),TransfTool.getPropValueStr(pvDiagMap,"diagname")))));
            //????????????
            pvDiagCondition.setSubject(new ConditionRecorder("Patient",null,Arrays.asList(new Identifier("code/patientId",TransfTool.getPropValueStr(pvDiagMap,"codeOp"))),Arrays.asList(new TextElement(TransfTool.getPropValueStr(pvDiagMap,"namePi")))));
            //????????????
            pvDiagCondition.setEncounter(new ConditionRecorder("Encounter",TransfTool.getEuPvtype(TransfTool.getPropValueStr(pvDiagMap,"euPvtype")),new Identifier("id/visitno",TransfTool.getPropValueStr(pvDiagMap,"codePv"))));
            //????????????
            pvDiagCondition.setRecordedDate(TransfTool.getPropValueDates(pvDiagMap,"dateDiag"));
            //????????????
            pvDiagCondition.setRecorder(new ConditionRecorder("Practitioner",null,Arrays.asList(new Identifier("doctorCode",TransfTool.getPropValueStr(pvDiagMap,"codeEmp"))),Arrays.asList(new TextElement(TransfTool.getPropValueStr(pvDiagMap,"nameEmp")))));

            //??????????????????
            Entry entry = new Entry(new Response());
            Response response = entry.getResponse();
            response.setStaus(ZsphConstant.RES_SUS_OTHER);
            response.setOutcome(BeanMap.create(pvDiagCondition));
            entryList.add(entry);
        }
        return entryList;
    }

    /**
     * ????????????
     * @param param
     * @return
     */
    @Override
    public List<Entry> savePiMaster(String param) {
        //????????????
        Map<String,Object> requestBody = ZsphMsgUtils.fromJson(param, Map.class);
        Object objEntry = MapUtils.getObject(requestBody, "entry");
        if(objEntry ==null){
            throw new BusException("?????????entry");
        }
        List<Map<String,Object>> entryList = (List<Map<String,Object>>) objEntry;
        //?????????????????????
        List<PatientSave> patientList = new ArrayList<>();
        entryList.stream().forEach(mp -> {
            Map<String, Object> resourceMap = null;
            if (mp != null && (resourceMap = (Map<String, Object>) mp.get("resource")) != null && StringUtils.isNotBlank(MapUtils.getString(resourceMap, "implicitRules"))) {
                for(Map<String, Object> pi : (List<Map<String, Object>>)resourceMap.get("parameter")){
                    patientList.add(TransfTool.mapToBean(PatientSave.class, pi));
                }
            }
        });
        if(CollectionUtils.isEmpty(patientList)){
            throw new BusException("??????????????????????????????");
        }
        //??????????????????
        List<Entry> entryResList = new ArrayList<>();
        for(PatientSave pa : patientList){
            Map<String,Object> result = new HashMap<>();
            if(pa == null){
                throw new BusException("?????????????????????");
            }
            if(StringUtils.isBlank(pa.getPatientno())){
                if(CommonUtils.isEmptyString(pa.getGuarderName())||CommonUtils.isEmptyString(pa.getGuarderCardType())||CommonUtils.isEmptyString(pa.getGuarderNo())){
                    if(CommonUtils.isEmptyString(pa.getIdtype())){
                        throw new BusException("???????????????????????????");
                    }
                    if(CommonUtils.isEmptyString(pa.getIdno())){
                        throw new BusException("???idNo???????????????????????????");
                    }
                }
                if(CommonUtils.isEmptyString(pa.getPatientname())){
                    throw new BusException("???????????????????????????");
                }
                if(CommonUtils.isEmptyString(pa.getSex())){
                    throw new BusException("?????????????????????");
                }
                if(CommonUtils.isEmptyString(pa.getPhone())){
                    throw new BusException("????????????????????????");
                }
                if(pa.getDateofbirth() == null){
                    throw new BusException("???????????????????????????");
                }
                if(CommonUtils.isEmptyString(pa.getAddress())){
                    throw new BusException("???????????????????????????");
                }
            }
            if(CommonUtils.isEmptyString(pa.getHisoperator())){
                throw new BusException("??????????????????????????????");
            }
            User user = QueryUtils.getDefaultUser(pa.getHisoperator());
            if(!BeanUtils.isNotNull(user)){
                throw new BusException("???????????????????????????????????????his??????????????????");
            }
            UserContext.setUser(user);
            //??????????????????????????????????????????????????????
            if(!CommonUtils.isEmptyString(pa.getPatientname())&&!CommonUtils.isEmptyString(pa.getGuarderNo())){
                List<Map<String,Object>> tempPi = DataBaseHelper.queryForList("select code_pi,name_pi,pk_pi,code_op from pi_master where name_pi = ? and idno_rel=? ", pa.getPatientname(),pa.getGuarderNo());
                if(tempPi!=null&&tempPi.size()>0&&StringUtils.isBlank(pa.getPatientno())){
                    throw new BusException("????????????????????????????????????????????????????????????????????????????????????????????????");
                }
            }
            PiMaster tempPi=null;
            if(StringUtils.isNotBlank(pa.getPatientno())){
                tempPi = DataBaseHelper.queryForBean("select * from pi_master where code_op = ? ",PiMaster.class, pa.getPatientno());
                if(tempPi==null){
                    throw new BusException("??????patientno???????????????????????????????????????");
                }
            }

            //????????????
            Map<String,Object> paramMap = new HashMap<>();
            paramMap.put("pkOrg",user.getPkOrg());
            if("01".equals(pa.getIdtype())){
                paramMap.put("dtIdType","01");
            }else if("03".equals(pa.getIdtype())){
                paramMap.put("dtIdType","02");
            }else if("04".equals(pa.getIdtype())){
                paramMap.put("dtIdType","03");
            }else if("06".equals(pa.getIdtype())){
                paramMap.put("dtIdType","04");
            }else if("07".equals(pa.getIdtype())){
                paramMap.put("dtIdType","05");
            }else{
                paramMap.put("dtIdType","99");
            }
            //paramMap.put("dtIdType",pa.getIdtype());
            paramMap.put("address",pa.getAddress());
            paramMap.put("birthDate",pa.getDateofbirth());
            paramMap.put("idNo",pa.getIdno());
            paramMap.put("mobile",pa.getPhone());
            paramMap.put("age",pa.getAge());
            if("1".equals(pa.getSex())){
                paramMap.put("dtSex","02");
            }else if("2".equals(pa.getSex())){
                paramMap.put("dtSex","03");
            }else{
                if(CommonUtils.isNotNull(pa.getSex()) || CommonUtils.isNull(tempPi.getDtSex())) {
                    paramMap.put("dtSex", "04");
                }else{
                    paramMap.put("dtSex", tempPi.getDtSex());
                }
            }
            paramMap.put("namePi",pa.getPatientname());
            if("0".equals(pa.getCardtype())){//0.?????????
                paramMap.put("hicNo",pa.getCardnum());

            }else if("1".equals(pa.getCardtype())){//1.?????????
                paramMap.put("insurNo",pa.getCardnum());
            }
            //his??????????????????????????????
            if(StringUtils.isNotBlank(pa.getHealthcard())){
                paramMap.put("hicNo",pa.getHealthcard());
            }
            if(StringUtils.isNotBlank(pa.getHealthcode())){
                paramMap.put("hicNo",pa.getHealthcode());
            }
            paramMap.put("flagRealmobile",pa.getAuthentication());
            if(!CommonUtils.isEmptyString(pa.getGuarderCardType())){
                if("01".equals(pa.getGuarderCardType())){
                    paramMap.put("dtIdtypeRel","01");
                }else if("03".equals(pa.getGuarderCardType())){
                    paramMap.put("dtIdtypeRel","02");
                }else if("04".equals(pa.getGuarderCardType())){
                    paramMap.put("dtIdtypeRel","03");
                }else if("06".equals(pa.getGuarderCardType())){
                    paramMap.put("dtIdtypeRel","04");
                }else if("07".equals(pa.getGuarderCardType())){
                    paramMap.put("dtIdtypeRel","05");
                }else{
                    paramMap.put("dtIdtypeRel","99");
                }
            }
            paramMap.put("idnoRel",pa.getGuarderNo());
            paramMap.put("nameRel",pa.getGuarderName());
            paramMap.put("codeOp",pa.getPatientno());
            if(!"99".equals(MapUtils.getString(paramMap,"dtIdType"))){
                List<Map<String,Object>> codePi = DataBaseHelper.queryForList("select code_pi,name_pi,pk_pi,code_op from pi_master where dt_idtype = ? and id_no=? ", MapUtils.getString(paramMap,"dtIdType"),MapUtils.getString(paramMap,"idNo"));
                if(codePi!=null&&codePi.size()>0){
                    throw new BusException("?????????????????????????????????????????????????????????????????????");
                }
            }
            result = piMasterRegister(paramMap,user);

            Entry entry = new Entry(new Response());
            if(result == null ||"false".equals(result.get("result"))){

                Response response = entry.getResponse();
                OutcomePi outcome = new OutcomePi();
                outcome.setResourceType("OperationOutcome");
                response.setStaus(ZsphConstant.RES_ERR_OTHER);
                Issue issue = new Issue();
                issue.setCode("informational");
                issue.setDiagnostics("????????????????????????his?????????");
                issue.setSeverity("error");
                outcome.setIssue(issue);
                response.setOutcome(BeanMap.create(outcome));
            }else{
                Response response = entry.getResponse();
                OutcomePi outcome = new OutcomePi();
                outcome.setResourceType("OperationOutcome");
                response.setStaus(ZsphConstant.RES_SUS_OTHER);
                Issue issue = new Issue();
                issue.setCode("informational");
                issue.setDiagnostics("??????");
                issue.setCardtype(MapUtils.getString(result, "cardType"));
                issue.setPatientid(MapUtils.getString(result, "codeOp"));
                issue.setHealthcode(MapUtils.getString(result, "hicNo"));
                outcome.setIssue(issue);
                response.setOutcome(BeanMap.create(outcome));
            }
            entryResList.add(entry);
        }

        return entryResList;
    }
    /**
     * ??????????????????
     *
     * @param paramMap ??????{pkOrg,dtIdtype,idNo,mobile,dtSex,birthDate}
     * @return
     */
    public Map<String, Object> piMasterRegister(Map<String, Object> paramMap,User user) {
        ApplicationUtils apputil = new ApplicationUtils();
        String dtIdtype = CommonUtils.getPropValueStr(paramMap, "dtIdType");
        String idNo = CommonUtils.getPropValueStr(paramMap, "idNo");
        String codeOp = CommonUtils.getPropValueStr(paramMap, "codeOp");
        Map<String, Object> result = new HashMap<>();
        //????????????????????????????????????????????????
        if("01".equals(dtIdtype)||StringUtils.isNotBlank(codeOp)) {
            PiMaster tempPi= null;
            if(StringUtils.isNotBlank(codeOp)){
                tempPi = DataBaseHelper.queryForBean("select * from pi_master where code_op = ? ",PiMaster.class, codeOp);
            }else if("01".equals(dtIdtype)){
                tempPi = DataBaseHelper.queryForBean("select * from pi_master where id_no = ? and dt_idtype=? ",PiMaster.class, idNo,dtIdtype);
            }

            if(tempPi != null){
                //?????????
                if(!CommonUtils.isEmptyString(CommonUtils.getPropValueStr(paramMap, "mobile"))){
                    tempPi.setMobile(CommonUtils.getPropValueStr(paramMap, "mobile"));
                }
                if(!CommonUtils.isEmptyString(CommonUtils.getPropValueStr(paramMap, "dtIdType"))){
                    tempPi.setDtIdtype(CommonUtils.getPropValueStr(paramMap, "dtIdType"));
                }
                if(!CommonUtils.isEmptyString(CommonUtils.getPropValueStr(paramMap, "idNo"))){
                    tempPi.setIdNo(CommonUtils.getPropValueStr(paramMap, "idNo"));
                }
                //????????????
                if(!CommonUtils.isEmptyString(CommonUtils.getPropValueStr(paramMap, "namePi"))){
                    tempPi.setNamePi(CommonUtils.getPropValueStr(paramMap, "namePi"));
                }
                //??????
                if(!CommonUtils.isEmptyString(CommonUtils.getPropValueStr(paramMap, "dtSex"))){
                    tempPi.setDtSex(CommonUtils.getPropValueStr(paramMap, "dtSex"));
                }
                //????????????
                if(!CommonUtils.isEmptyString(CommonUtils.getPropValueStr(paramMap, "hicNo"))){
                    tempPi.setHicNo(CommonUtils.getPropValueStr(paramMap, "hicNo"));
                }
                //????????????
                if(!CommonUtils.isEmptyString(CommonUtils.getPropValueStr(paramMap, "insurNo"))){
                    tempPi.setInsurNo(CommonUtils.getPropValueStr(paramMap, "insurNo"));
                }
                if(!CommonUtils.isEmptyString(CommonUtils.getPropValueStr(paramMap, "address"))){
                    tempPi.setAddrRegiDt(CommonUtils.getPropValueStr(paramMap, "address"));
                }
                //?????????????????????
                if(!CommonUtils.isEmptyString(CommonUtils.getPropValueStr(paramMap, "dtIdtypeRel"))){
                    tempPi.setDtIdtypeRel(CommonUtils.getPropValueStr(paramMap, "dtIdtypeRel"));
                }
                //??????????????????
                if(!CommonUtils.isEmptyString(CommonUtils.getPropValueStr(paramMap, "idnoRel"))){
                    tempPi.setIdnoRel(CommonUtils.getPropValueStr(paramMap, "idnoRel"));
                }
                //???????????????
                if(!CommonUtils.isEmptyString(CommonUtils.getPropValueStr(paramMap, "nameRel"))){
                    tempPi.setNameRel(CommonUtils.getPropValueStr(paramMap, "nameRel"));
                }
                if(!CommonUtils.isEmptyString(CommonUtils.getPropValueStr(paramMap, "birthDate"))){
                    tempPi.setBirthDate(DateUtils.strToDate(CommonUtils.getPropValueStr(paramMap, "birthDate"), "yyyy-MM-dd"));
                }

                tempPi.setModifier(user.getPkEmp());// ?????????
                tempPi.setPkOrg(user.getPkOrg());
                tempPi.setDelFlag("0");
                tempPi.setTs(new Date());
                tempPi.setModifier(user.getPkEmp());
                tempPi.setFlagRealmobile(CommonUtils.getPropValueStr(paramMap, "flagRealmobile"));
                DataBaseHelper.updateBeanByPk(tempPi, false);
                result.put("codeOp", tempPi.getCodeOp());
                if(StringUtils.isNotBlank(tempPi.getHicNo())){
                    result.put("hicNo", tempPi.getHicNo());
                    result.put("cardType", "0");
                }else{
                    result.put("hicNo", "");
                    result.put("cardType", "");
                }
                return result;
            }
        }
        String codeInsu = CommonUtils.getPropValueStr(paramMap, "codeInsu");
        List<PiInsurance> insuranceList = new ArrayList<>();
        PiInsurance piInsurance = new PiInsurance();
        String sql = " select pk_hp from bd_hp where  code = ? and del_flag = '0'";
        if(codeInsu == null || codeInsu == ""){
            codeInsu = "01";
        }
        Map<String, Object> hpMap = DataBaseHelper.queryForMap(sql, codeInsu);
        if(CommonUtils.isNull(CommonUtils.getPropValueStr(hpMap, "pkHp"))){
            hpMap = DataBaseHelper.queryForMap(sql, "01");
        }
        piInsurance.setPkHp(CommonUtils.getPropValueStr(hpMap, "pkHp"));
        insuranceList.add(piInsurance);
        paramMap.put("insuranceList", insuranceList);
        PiMaster master = new PiMaster();
        master.setIdNo(idNo);
        master.setDtIdtype(dtIdtype);
        master.setNamePi(CommonUtils.getPropValueStr(paramMap, "namePi"));
        master.setMobile(CommonUtils.getPropValueStr(paramMap, "mobile"));
        master.setDtSex(CommonUtils.getPropValueStr(paramMap, "dtSex"));
        master.setHicNo(CommonUtils.getPropValueStr(paramMap, "hicNo"));
        master.setAddrRegiDt(CommonUtils.getPropValueStr(paramMap, "address"));
        master.setInsurNo(CommonUtils.getPropValueStr(paramMap, "insurNo"));//????????????
        if("01".equals(dtIdtype)){
            master.setFlagRealname("1");
        }
        master.setFlagRealmobile(CommonUtils.getPropValueStr(paramMap, "flagRealmobile"));
        master.setBirthDate(DateUtils.strToDate(CommonUtils.getPropValueStr(paramMap, "birthDate"), "yyyy-MM-dd"));
        //?????????????????????
        if(!CommonUtils.isEmptyString(CommonUtils.getPropValueStr(paramMap, "dtIdtypeRel"))){
            master.setDtIdtypeRel(CommonUtils.getPropValueStr(paramMap, "dtIdtypeRel"));
        }
        //??????????????????
        if(!CommonUtils.isEmptyString(CommonUtils.getPropValueStr(paramMap, "idnoRel"))){
            master.setIdnoRel(CommonUtils.getPropValueStr(paramMap, "idnoRel"));
        }
        //???????????????
        if(!CommonUtils.isEmptyString(CommonUtils.getPropValueStr(paramMap, "nameRel"))){
            master.setNameRel(CommonUtils.getPropValueStr(paramMap, "nameRel"));
        }
        paramMap.put("master", master);
        ResponseJson rs = apputil.execService("PI", "PiPubService","savePiMasterParam", paramMap, user);
        master = (PiMaster) rs.getData();
        if(master == null){
            result.put("message", "????????????????????????his?????????!");
            result.put("result", "false");
            return result;
        }
        result.put("codeOp", master.getCodeOp());
        if(StringUtils.isNotBlank(master.getHicNo())){
            result.put("hicNo", master.getHicNo());
            result.put("cardType", "0");
        }else{
            result.put("hicNo", "");
            result.put("cardType", "");
        }


        return result;
    }


    /**
     * ??????????????????????????????
     * @param param
     * @return
     */
    @Override
    public List<Entry> getPiMasterEmpiInfo(String param) {
        Map<String,Object> requestBody = ZsphMsgUtils.fromJson(param, Map.class);
        Object entry = MapUtils.getObject(requestBody, "entry");
        if(entry ==null){
            throw new BusException("?????????entry");
        }
        List<Map<String,Object>> entryMapList = (List<Map<String,Object>>) entry;
        //???????????????????????????
        List<PatientEmpi> patientList = new ArrayList<>();
        entryMapList.stream().forEach(mp -> {
            Map<String, Object> resourceMap = null;
            if (mp != null && (resourceMap = (Map<String, Object>) mp.get("resource")) != null && StringUtils.isNotBlank(MapUtils.getString(resourceMap, "implicitRules"))) {
                for(Map<String, Object> pi : (List<Map<String, Object>>)resourceMap.get("parameter")){
                    patientList.add(TransfTool.mapToBean(PatientEmpi.class, pi));
                }
            }
        });
        if(CollectionUtils.isEmpty(patientList)){
            throw new BusException("??????????????????????????????");
        }
        List<Entry> entryList = new ArrayList<>();
        List<String> updateList = new ArrayList<>();
        for(PatientEmpi pi:patientList){
            if(StringUtils.isBlank(pi.getEmpi())){
                throw new BusException("?????????EMPI???");
            }
            if(StringUtils.isBlank(pi.getPatientid())){
                throw new BusException("?????????HIS??????ID");
            }
            StringBuilder updateSql = new StringBuilder("update PI_MASTER  set ");
            updateSql.append("mpi = '" + pi.getEmpi() + "' ");
            updateSql.append("  where  code_op='" + pi.getPatientid() + "'");
            updateList.add(updateSql.toString());
        }
        int[] cnt = DataBaseHelper.batchUpdate(updateList.toArray(new String[0]));
        Entry entryRes = new Entry(new Response());
        int count = 0;
        for (int i : cnt) {
            if (i > 0) {
                count += 1;
            }
        }
        if(count != patientList.size()){
            Response response = entryRes.getResponse();
            OutcomePi outcome = new OutcomePi();
            outcome.setResourceType("OperationOutcome");
            response.setStaus(ZsphConstant.RES_ERR_OTHER);
            Issue issue = new Issue();
            issue.setCode("informational");
            issue.setDiagnostics("???????????????his???????????????");
            issue.setSeverity("error");
            outcome.setIssue(issue);
            response.setOutcome(BeanMap.create(outcome));
            entryList.add(entryRes);
        }else{
            Response response = entryRes.getResponse();
            OutcomePi outcome = new OutcomePi();
            outcome.setResourceType("OperationOutcome");
            response.setStaus(ZsphConstant.RES_SUS_OTHER);
            Issue issue = new Issue();
            issue.setCode("informational");
            issue.setDiagnostics("???????????????");
            issue.setSeverity("informational");
            outcome.setIssue(issue);
            response.setOutcome(BeanMap.create(outcome));
            entryList.add(entryRes);
        }
        return entryList;
    }
}
