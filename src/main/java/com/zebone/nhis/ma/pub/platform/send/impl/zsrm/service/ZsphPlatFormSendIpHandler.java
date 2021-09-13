package com.zebone.nhis.ma.pub.platform.send.impl.zsrm.service;

import com.alibaba.fastjson.JSONObject;
import com.zebone.nhis.common.module.base.bd.code.BdDefdoc;
import com.zebone.nhis.common.module.base.ou.BdOuDept;
import com.zebone.nhis.common.module.base.ou.BdOuEmployee;
import com.zebone.nhis.common.module.pi.PiMaster;
import com.zebone.nhis.common.module.pv.PvDiag;
import com.zebone.nhis.common.module.pv.PvEncounter;
import com.zebone.nhis.common.module.pv.PvIp;
import com.zebone.nhis.ma.pub.platform.send.impl.zsrm.common.EnumUrlType;
import com.zebone.nhis.ma.pub.platform.send.impl.zsrm.support.RequestData;
import com.zebone.nhis.ma.pub.platform.send.impl.zsrm.support.SendAndResolve;
import com.zebone.nhis.ma.pub.platform.send.impl.zsrm.vo.pv.HospitalizationInfo;
import com.zebone.platform.common.support.NHISUUID;
import com.zebone.platform.modules.dao.DataBaseHelper;
import com.zebone.platform.modules.exception.BusException;
import org.apache.commons.collections.MapUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
public class ZsphPlatFormSendIpHandler {
    private static Logger log = LoggerFactory.getLogger("nhis.lbHl7Log");


    public void sendPvInfoMsg(Map<String, Object> paramMap){
        String pkPv = MapUtils.getString(paramMap,"pkPv");
        if(StringUtils.isEmpty(pkPv)){
            throw new BusException("未获取到pkPv");
        }
        String sql = "select * from PV_ENCOUNTER where PK_PV = ?";
        PvEncounter pvEncounter = DataBaseHelper.queryForBean(sql,PvEncounter.class,pkPv);
        if(pvEncounter==null){
            throw new BusException("未获取到就诊信息");
        }

        String sqlIp = "select IP_TIMES from PV_IP where PK_PV = ?";
        PvIp pvIp = DataBaseHelper.queryForBean(sqlIp, PvIp.class,pkPv);
        if(pvIp==null){
            throw new BusException("未获取到患者就诊住院属性");
        }

        String pkPi = pvEncounter.getPkPi();
        if(StringUtils.isEmpty(pkPi)){
            throw new BusException("未获取到pkPi");
        }
        sql = "select * from PI_MASTER where PK_PI = ?";
        PiMaster piMaster = DataBaseHelper.queryForBean(sql,PiMaster.class,pkPi);
        if(piMaster==null){
            throw new BusException("未获取到患者信息");
        }

        //主管医生
        String pkEmpPhy = pvEncounter.getPkEmpPhy();
        sql = "select * from BD_OU_EMPLOYEE where PK_EMP = ?";
        BdOuEmployee attendDoc = DataBaseHelper.queryForBean(sql,BdOuEmployee.class,pkEmpPhy);

        sql = "select * from PV_DIAG where PK_PV = ?";
        List<PvDiag> pvDiagList = DataBaseHelper.queryForList(sql,PvDiag.class,pkPv);
        PvDiag pvDiag = pvDiagList.stream().findFirst().get();


        String id = NHISUUID.getKeyId();
        HospitalizationInfo hospitalizationInfo = new HospitalizationInfo();
        hospitalizationInfo.setResourceType("Bundle");
        hospitalizationInfo.setId(id);
        hospitalizationInfo.setType("message");
        hospitalizationInfo.setTimestamp(new Date());
        List<HospitalizationInfo.Entry> entryList = new ArrayList<>();
        hospitalizationInfo.setEntry(entryList);

        {
            HospitalizationInfo.Entry entry = hospitalizationInfo.new Entry();
            HospitalizationInfo.Resource resource = hospitalizationInfo.new Resource();
            resource.setResourceType("MessageHeader");
            resource.setId(id);
            List<HospitalizationInfo.Destination> destinationList = new ArrayList<>();
            HospitalizationInfo.Destination destination = hospitalizationInfo.new Destination();
            destination.setEndpoint("PT");
            destinationList.add(destination);
            resource.setDestination(destinationList);
            HospitalizationInfo.Source source = hospitalizationInfo.new Source();
            source.setEndpoint("ZYHIS");
            resource.setSource(source);

            entry.setResource(resource);
            entryList.add(entry);

        }


        {
            HospitalizationInfo.Entry entry = hospitalizationInfo.new Entry();
            entryList.add(entry);
            HospitalizationInfo.Resource resource = hospitalizationInfo.new Resource();
            resource.setId(id);
            resource.setImplicitRules("ZYDJ");
            resource.setResourceType("Encounter");

            HospitalizationInfo.Identifier identifier = hospitalizationInfo.new Identifier();
            //门诊号，住院号
            identifier.setSystem("id/visitno");
            identifier.setValue(pvEncounter.getCodeIp());
            HospitalizationInfo.Identifier identifierTimes = hospitalizationInfo.new Identifier();
            //住院次数
            identifierTimes.setSystem("id/visitTimes");
            identifierTimes.setValue(pvIp.getIpTimes()!=null?pvIp.getIpTimes().toString():null);
            HospitalizationInfo.Identifier identifierSerialNo = hospitalizationInfo.new Identifier();
            //住院流水号
            identifierSerialNo.setSystem("id/visitSerialNo");
            identifierSerialNo.setValue(pvEncounter.getCodePv());
            List<HospitalizationInfo.Identifier> identifierList = new ArrayList<>();
            identifierList.add(identifier);
            identifierList.add(identifierTimes);
            identifierList.add(identifierSerialNo);
            resource.setIdentifier(identifierList);
            resource.setStatus("finished");
            HospitalizationInfo.ServiceType serviceType = hospitalizationInfo.new ServiceType();
            HospitalizationInfo.Coding coding = hospitalizationInfo.new Coding();
            coding.setSystem("");
            coding.setCode("");
            coding.setDisplay("");
            List<HospitalizationInfo.Coding> codingList = new ArrayList<>();
            codingList.add(coding);
            serviceType.setCoding(codingList);
            resource.setServiceType(serviceType);

            HospitalizationInfo.Clazz clazz = hospitalizationInfo.new Clazz();
            clazz.setSystem("ActCode");
            clazz.setCode("IMP");
            clazz.setDisplay("住院");
            resource.setClazz(clazz);

            HospitalizationInfo.Subject subject = hospitalizationInfo.new Subject();
            subject.setResourceType("Patient");
            HospitalizationInfo.Identifier identifier_subject_1 = hospitalizationInfo.new Identifier();
            //病人ID标识
            identifier_subject_1.setSystem("code/patientId");
            //病人id
            identifier_subject_1.setValue(pvEncounter.getCodeIp());
            HospitalizationInfo.Identifier identifier_subject_2 = hospitalizationInfo.new Identifier();
            //病人身份证标识
            identifier_subject_2.setSystem("code/idNo");
            //病人身份证
            identifier_subject_2.setValue(piMaster.getIdNo());

            List<HospitalizationInfo.Identifier> identifier_subject_list = new ArrayList<>();
            identifier_subject_list.add(identifier_subject_1);
            identifier_subject_list.add(identifier_subject_2);
            subject.setIdentifier(identifier_subject_list);

            HospitalizationInfo.Name name = hospitalizationInfo.new Name();
            //病人姓名
            name.setText(piMaster.getNamePi());
            List<HospitalizationInfo.Name> nameList = new ArrayList<>();
            nameList.add(name);
            subject.setName(nameList);


            HospitalizationInfo.Telecom telecom = hospitalizationInfo.new Telecom();
            //联系方式代码
            telecom.setSystem("phone");
            telecom.setValue(piMaster.getTelNo());
            List<HospitalizationInfo.Telecom> telecomList = new ArrayList<>();
            telecomList.add(telecom);
            subject.setTelecom(telecomList);

            subject.setBirthDate(piMaster.getBirthDate());

            HospitalizationInfo.Address address = hospitalizationInfo.new Address();
            address.setText("");
            address.setCity("");
            address.setDistrict("");
            address.setState("");
            address.setPostalCode("");
            address.setCountry("");
            List<HospitalizationInfo.Address> addressList = new ArrayList<>();
            addressList.add(address);

            HospitalizationInfo.MaritalStatus maritalStatus = hospitalizationInfo.new MaritalStatus();
            HospitalizationInfo.Coding coding1 = hospitalizationInfo.new Coding();
            coding1.setSystem("");
            coding1.setCode("");
            coding1.setDisplay("");
            List<HospitalizationInfo.Coding> codingList1 = new ArrayList<>();
            codingList1.add(coding1);
            maritalStatus.setCoding(codingList1);

            //患者联系人
            HospitalizationInfo.Contact contact = hospitalizationInfo.new Contact();
            HospitalizationInfo.Name name1 = hospitalizationInfo.new Name();
            name1.setText("");
            contact.setName(name1);
            List<HospitalizationInfo.Contact> contactList = new ArrayList<>();
            contactList.add(contact);



            subject.setContact(contactList);
            subject.setMaritalStatus(maritalStatus);
            subject.setAddress(addressList);


            //就诊时间周期
            HospitalizationInfo.Period period = hospitalizationInfo.new Period();
            //入院时间
            period.setStart(pvEncounter.getDateBegin());
            //出院时间
            period.setEnd(null);

            HospitalizationInfo.ReasonCode reasonCode = hospitalizationInfo.new ReasonCode();
            reasonCode.setText("");
            List<HospitalizationInfo.ReasonCode> reasonCodeList = new ArrayList<>();
            reasonCodeList.add(reasonCode);


            HospitalizationInfo.Diagnosis diagnosis = hospitalizationInfo.new Diagnosis();
            HospitalizationInfo.Condition condition = hospitalizationInfo.new Condition();
            condition.setResourceType("Condition");
            HospitalizationInfo.Code code = hospitalizationInfo.new Code();
            HospitalizationInfo.Coding coding2 = hospitalizationInfo.new Coding();
            //诊断编码
            coding2.setCode(pvDiag!=null?pvDiag.getCodeIcd():"");
            //诊断名称
            coding2.setDisplay(pvDiag!=null?pvDiag.getNameDiag():"");
            List<HospitalizationInfo.Coding> codingList2 = new ArrayList<>();
            codingList2.add(coding2);
            code.setCoding(codingList2);
            condition.setCode(code);
            diagnosis.setCondition(condition);
            List<HospitalizationInfo.Diagnosis> diagnosisList = new ArrayList<>();
            diagnosisList.add(diagnosis);


            sql = "select * from bd_defdoc where code_defdoclist = '060005' and CODE = ?";
            BdDefdoc bdDefdoc = new BdDefdoc();
            if(pvDiag!=null&&!StringUtils.isEmpty(pvDiag.getDtDiagtype())){
                bdDefdoc = DataBaseHelper.queryForBean(sql,BdDefdoc.class,pvDiag.getDtDiagtype());
            }

            HospitalizationInfo.Use use = hospitalizationInfo.new Use();
            HospitalizationInfo.Coding coding3 = hospitalizationInfo.new Coding();
            coding3.setDisplay(bdDefdoc!=null?bdDefdoc.getName():"");
            coding3.setCode(bdDefdoc!=null?bdDefdoc.getCode():"");
            coding3.setSystem("");
            List<HospitalizationInfo.Coding> codingList3 = new ArrayList<>();
            codingList3.add(coding3);
            use.setCoding(codingList3);
            diagnosis.setUse(use);

            //诊断序号
            diagnosis.setRank("");


            sql = "select * from bd_defdoc where code_defdoclist = '000126' and CODE = ?";
            BdDefdoc bdDefdoc1 = new BdDefdoc();
            if(pvEncounter!=null&&!StringUtils.isEmpty(pvEncounter.getDtPvsource())){
                bdDefdoc1 = DataBaseHelper.queryForBean(sql,BdDefdoc.class,pvEncounter.getDtPvsource());
            }

            //就诊详细信息
            HospitalizationInfo.Hospitalization hospitalization = hospitalizationInfo.new Hospitalization();
            //病人来源
            HospitalizationInfo.AdmitSource admitSource = hospitalizationInfo.new AdmitSource();
            HospitalizationInfo.Coding coding4 = hospitalizationInfo.new Coding();
            //病人来源编码系统
            coding4.setSystem("");
            //病人来源代码
            //门诊|急诊|其他医疗机构转入|其他
            //1|2|3|9
            coding4.setCode(bdDefdoc1!=null?bdDefdoc1.getCode():"");
            //病人来源名称
            coding4.setDisplay(bdDefdoc1!=null?bdDefdoc1.getName():"");
            List<HospitalizationInfo.Coding> codingList4 = new ArrayList<>();
            codingList4.add(coding4);
            admitSource.setCoding(codingList4);
            hospitalization.setAdmitSource(admitSource);

            //饮食类型
            HospitalizationInfo.DietPreference dietPreference = hospitalizationInfo.new DietPreference();
            //饮食类型编码
            HospitalizationInfo.Coding coding5 = hospitalizationInfo.new Coding();
            //饮食类型名称
            coding5.setDisplay("");
            //饮食类型代码
            coding5.setCode("");
            //饮食类型编码系统
            coding5.setSystem("");
            List<HospitalizationInfo.Coding> codingList5 = new ArrayList<>();
            codingList5.add(coding5);
            dietPreference.setCoding(codingList5);
            List<HospitalizationInfo.DietPreference> dietPreferenceList = new ArrayList<>();
            dietPreferenceList.add(dietPreference);
            hospitalization.setDietPreference(dietPreferenceList);

            //离院方式
            HospitalizationInfo.SpecialArrangement specialArrangement = hospitalizationInfo.new SpecialArrangement();
            //离院方式编码
            HospitalizationInfo.Coding coding6 = hospitalizationInfo.new Coding();
            //离院方式编码系统
            coding6.setSystem("");
            //离院方式代码
            //1|2|3|4|5|9
            //医嘱离院|医嘱转院|医嘱转社区卫生服务机构/乡镇卫生院|非医嘱离院|死亡|其他
            coding6.setCode("");
            //离院方式名称
            coding6.setDisplay("");
            List<HospitalizationInfo.Coding> codingList6 = new ArrayList<>();
            codingList6.add(coding6);
            specialArrangement.setCoding(codingList6);
            List<HospitalizationInfo.SpecialArrangement> specialArrangementList = new ArrayList<>();
            specialArrangementList.add(specialArrangement);
            hospitalization.setSpecialArrangement(specialArrangementList);

            //患者去向
            HospitalizationInfo.DischargeDisposition dischargeDisposition = hospitalizationInfo.new DischargeDisposition();
            //患者去向编码
            HospitalizationInfo.Coding coding7 = hospitalizationInfo.new Coding();
            //患者去向名称
            coding7.setDisplay("");
            //患者去向代码
            coding7.setCode("");
            //患者去向编码系统
            coding7.setSystem("");
            List<HospitalizationInfo.Coding> codingList7 = new ArrayList<>();
            codingList7.add(coding7);
            dischargeDisposition.setCoding(codingList7);
            hospitalization.setDischargeDisposition(dischargeDisposition);


            //科室信息
            HospitalizationInfo.Location location = hospitalizationInfo.new Location();
            sql = "select * from BD_OU_DEPT where PK_DEPT = ?";
            BdOuDept dept = DataBaseHelper.queryForBean(sql,BdOuDept.class,pvEncounter.getPkDept());
            location.setResourceType("");
            //科室标识
            HospitalizationInfo.Identifier identifier1 = hospitalizationInfo.new Identifier();
            //科室标识代码的值
            identifier1.setValue(dept!=null?dept.getCodeDept():"");
            //科室标识代码
            //挂号科室regdept|入院科室indept|入院病区inward|所属科室curdept|所属病区curward|出院科室outdept|出院病区outward
            identifier1.setSystem("indept");
            List<HospitalizationInfo.Identifier> identifierList1 = new ArrayList<>();
            identifierList1.add(identifier1);
            location.setIdentifier(identifierList1);
            List<HospitalizationInfo.Location> locationList = new ArrayList<>();
            locationList.add(location);

            //操作员
            HospitalizationInfo.Extension extension = hospitalizationInfo.new Extension();
            sql = "select * from BD_OU_EMPLOYEE where PK_EMP = ?";
            BdOuEmployee creater = DataBaseHelper.queryForBean(sql,BdOuEmployee.class,pvEncounter.getCreator());
            //操作时间标识符
            extension.setUrl("operationTime");
            //操作时间
            extension.setValueDatetime(new Date());
            List<HospitalizationInfo.Extension> extensionList = new ArrayList<>();
            extensionList.add(extension);

            resource.setExtension(extensionList);
            resource.setLocation(locationList);
            resource.setHospitalization(hospitalization);
            resource.setDiagnosis(diagnosisList);
            resource.setReasonCode(reasonCodeList);
            resource.setPeriod(period);
//            resource.setParticipant();
            resource.setSubject(subject);
            entry.setResource(resource);
        }


        String result = JSONObject.toJSONString(hospitalizationInfo);
        //创建推送消息格式，将组装消息实体转json
        RequestData requestData = RequestData.newBuilder()
                .id(id)
                .data(result)
                .urlType(EnumUrlType.BUSSINESS)
                .remoteMethod("Medication").build();
        log.info("json 实体：" + requestData.getData());
        //消息推送
        SendAndResolve.getInstance().send(HttpMethod.POST,requestData);
    }

}
