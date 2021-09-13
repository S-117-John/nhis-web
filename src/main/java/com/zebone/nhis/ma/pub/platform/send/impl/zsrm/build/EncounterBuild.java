package com.zebone.nhis.ma.pub.platform.send.impl.zsrm.build;

import com.google.common.collect.Lists;
import com.zebone.nhis.common.module.pv.PvDiag;
import com.zebone.nhis.ma.pub.platform.send.impl.zsrm.common.TransfTool;
import com.zebone.nhis.ma.pub.platform.send.impl.zsrm.model.PvEncounterVo;
import com.zebone.nhis.ma.pub.platform.send.impl.zsrm.vo.base.*;
import com.zebone.nhis.ma.pub.platform.send.impl.zsrm.vo.cn.LocalLocation;
import com.zebone.nhis.ma.pub.platform.send.impl.zsrm.vo.patient.Patient;
import com.zebone.nhis.ma.pub.platform.send.impl.zsrm.vo.pv.Diagnosis;
import com.zebone.nhis.ma.pub.platform.send.impl.zsrm.vo.pv.Encounter;
import com.zebone.nhis.ma.pub.platform.send.impl.zsrm.vo.pv.Participant;
import com.zebone.nhis.ma.pub.platform.send.impl.zsrm.vo.pv.Period;
import com.zebone.platform.common.support.NHISUUID;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class EncounterBuild {

    public static Encounter create(PvEncounterVo pvEncounterMsg){
        Encounter encounter = new Encounter();
        encounter.setResourceType("Encounter");
        encounter.setId(NHISUUID.getKeyId());
        RequestBuild.setExtension(encounter);
        //门诊号，住院号 ---因为新增修改同一个调用，codeOp多次就诊是一样的，这里先放codePv
        encounter.setIdentifier(Arrays.asList(new Identifier("id/visitno",pvEncounterMsg.getCodePv())));
        //就诊状态
        // "planned|arrived|triaged|in-progress|onleave|finished|cancelled|enterd-in-error|unknown 未开始|候诊|诊前评估|诊中|暂离|诊疗结束|取消就诊|错误|未知"
        encounter.setStatus(TransfTool.getEncStatus(pvEncounterMsg.getEuStatus()));
        Coding clas = TransfTool.getPvtype(pvEncounterMsg.getEuPvtype());//就诊类型
        clas.setSystem("ActCode");
        encounter.setClas(clas);
        //挂号类别信息
        encounter.setType(Arrays.asList(new CodeableConcept(Arrays.asList(new Coding("reglevelcode",pvEncounterMsg.getEuSrvtype(),TransfTool.getSrvText(pvEncounterMsg.getEuSrvtype()))))));
        //病人信息
        Patient patient = new Patient();
        encounter.setSubject(patient);
        patient.setResourceType("Patient");
        patient.setIdentifier(new ArrayList<>());
        patient.getIdentifier().add(new Identifier("code/patientId",pvEncounterMsg.getCodeOp()));//病人标识
        patient.getIdentifier().add(new Identifier("code/idNo",pvEncounterMsg.getIdNo()));//病人ID
        patient.setName(Arrays.asList(new TextElement(pvEncounterMsg.getNamePi())));//病人姓名
        patient.setTelecom(Arrays.asList(new Identifier("phone",pvEncounterMsg.getTelNo())));//联系方式
        String gender="";
        switch (pvEncounterMsg.getDtSex()){
            case "02":
                gender="男";
                break;
            case "03":
                gender="女";
                break;
            default:
                gender="不明";
                break;
        }
        patient.setGender(gender);
        patient.setBirthDate(pvEncounterMsg.getBirthDate());
        patient.setAddress(Arrays.asList());//患者地址
        patient.setMaritalStatus(new CodeableConcept());//婚姻状况
        patient.setContact(new ArrayList<>());//患者联系人
        patient.setParticipant(new ArrayList<>());//医生信息
        Participant participant = new Participant();
        participant.setType(Arrays.asList(new CodeableConcept(Arrays.asList(new Coding("regDoc",pvEncounterMsg.getCodeEmp(),pvEncounterMsg.getNameEmp())))));
        patient.getParticipant().add(participant);
        //接诊医生信息
        if(StringUtils.isNotBlank(pvEncounterMsg.getNameEmpPhy())){
            Participant participant2 = new Participant();
            participant2.setType(Arrays.asList(new CodeableConcept(Arrays.asList(new Coding("visitDoc",pvEncounterMsg.getCodeEmpPhy(),pvEncounterMsg.getNameEmpPhy())))));
            patient.getParticipant().add(participant2);
        }
        encounter.setPeriod(new Period(pvEncounterMsg.getDateBegin(),pvEncounterMsg.getDateEnd()));//就诊时间周期
        encounter.setReasonCode(Arrays.asList(new TextElement("")));//就诊原因
        encounter.setHospitalization(new Hospitalization());//就诊详细信息
        //科室信息
        Location regDept = new Location();
        regDept.setResourceType("Location");
        regDept.setIdentifier(new ArrayList<>());
        regDept.getIdentifier().add(new Identifier("regdept",pvEncounterMsg.getCodeDept()));//所属科室
        regDept.getIdentifier().add(new Identifier("regName",pvEncounterMsg.getNameDept()));//所属科室
        encounter.setLocation(Lists.newArrayList(new LocalLocation(regDept)));

        return encounter;
    }

    public static List<Diagnosis> createDiag(List<PvDiag> diagList){
        List<Diagnosis> diagnosisList = new ArrayList<>();
        if(CollectionUtils.isNotEmpty(diagList)){
            for(int i=0;i<diagList.size();i++){
                PvDiag pvDiag = diagList.get(i);
                Diagnosis dn = new Diagnosis();
                dn.setRank(String.valueOf((i+1)));
                Condition condition = new Condition();
                condition.setResourceType("Condition");
                condition.setCode(new CodeableConcept(Arrays.asList(new Coding(null,pvDiag.getCodeIcd(),pvDiag.getNameDiag()))));
                dn.setCondition(condition);
                dn.setUse(new CodeableConcept(Arrays.asList(new Coding("diagnosisType","1","门诊诊断"))));
                diagnosisList.add(dn);
            }
        }
        return diagnosisList.size()>0?diagnosisList:null;
    }
}
