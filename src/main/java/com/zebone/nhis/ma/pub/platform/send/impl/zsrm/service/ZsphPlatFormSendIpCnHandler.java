package com.zebone.nhis.ma.pub.platform.send.impl.zsrm.service;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.zebone.nhis.common.support.DateUtils;
import com.zebone.nhis.ma.pub.platform.send.impl.zsrm.build.RequestBuild;
import com.zebone.nhis.ma.pub.platform.send.impl.zsrm.common.TransfTool;
import com.zebone.nhis.ma.pub.platform.send.impl.zsrm.dao.IpCnOperationMapper;
import com.zebone.nhis.ma.pub.platform.send.impl.zsrm.model.CnOrderVo;
import com.zebone.nhis.ma.pub.platform.send.impl.zsrm.model.PvEncounterVo;
import com.zebone.nhis.ma.pub.platform.send.impl.zsrm.support.MsgIndexData;
import com.zebone.nhis.ma.pub.platform.send.impl.zsrm.support.RequestData;
import com.zebone.nhis.ma.pub.platform.send.impl.zsrm.support.SendAndResolve;
import com.zebone.nhis.ma.pub.platform.send.impl.zsrm.vo.base.*;
import com.zebone.nhis.ma.pub.platform.send.impl.zsrm.vo.cn.CnOrderOperation;
import com.zebone.nhis.ma.pub.platform.send.impl.zsrm.vo.cn.OrderReference;
import com.zebone.nhis.ma.pub.platform.send.impl.zsrm.vo.cn.Timing;
import com.zebone.nhis.ma.pub.platform.send.impl.zsrm.vo.lisris.Actor;
import com.zebone.platform.modules.exception.BusException;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class ZsphPlatFormSendIpCnHandler {
    private static Logger log = LoggerFactory.getLogger("nhis.lbHl7Log");

    @Resource
    private IpCnOperationMapper ipCnOperationMapper;

    /**
     * 发送医嘱核对信息/执行/停止/作废
     * @param paramMap
     */
    public void sendExOrderCheckMsg(Map<String, Object> paramMap){
        if(MapUtils.isEmpty(paramMap))
            return;
        List<Map<String,Object>> list = (List<Map<String, Object>>) MapUtils.getObject(paramMap,"ordlist");
        if(CollectionUtils.isEmpty(list))
            return;
        String pkPv = MapUtils.getString(list.get(0),"pkPv");
        //获取患者信息
        List<PvEncounterVo> piPvs = ipCnOperationMapper.qryPiPv(pkPv);
        if(CollectionUtils.isEmpty(piPvs)){
            throw new BusException("未获取到患者信息");
        }
        PvEncounterVo encounterVo = piPvs.get(0);
        //获取医嘱信息
        Map<String,Object> paramOrder = Maps.newHashMap();
        List<String> pkCnords = list.stream().map(vo -> MapUtils.getString(vo, "pkCnord")).filter(StringUtils::isNotBlank).collect(Collectors.toList());
        //状态为2时的数据查不到的
        if(pkCnords.size()==0)
            return;
        paramOrder.put("pkCnOds" , pkCnords);
        paramOrder.put("pkPv", pkPv);
        List<CnOrderVo> orderVos = ipCnOperationMapper.qryCnOrd(paramOrder);
        if(CollectionUtils.isEmpty(orderVos)){
            log.warn("发送医嘱核对信息时未获取到医嘱信息");
            return;
        }
        //查询执行时间集合
        BusinessBase businessBase = RequestBuild.create(null);
        for (CnOrderVo orderVo : orderVos) {
            CnOrderOperation cnOrder = new CnOrderOperation();
            cnOrder.setImplicitRules("ZYYZXX");
            cnOrder.setId(businessBase.getId());
            cnOrder.setIdentifier(Lists.newArrayList(new Identifier("orderNo", String.valueOf(orderVo.getOrdsn()))));
            cnOrder.setStatus(orderVo.getEuStatusOrd());
            cnOrder.setStatusReason(new TextElement(""));
            cnOrder.setCategory(Lists.newArrayList(new CodeableConcept(Lists.newArrayList(new Coding(orderVo.getCodeOrdtype(),orderVo.getNameOrdtype())))));
            cnOrder.setMedicationCodeableConcept(new CodeableConcept(Lists.newArrayList(new Coding(orderVo.getCodeOrd(),orderVo.getNameOrd()))));
            cnOrder.setMedicationReference(new OrderReference());
            cnOrder.getMedicationReference().setForm(new CodeableConcept(Lists.newArrayList(new Coding(orderVo.getCodeDosage(),orderVo.getNameDosage()))));
            cnOrder.setSubject(new Actor());
            cnOrder.getSubject().setIdentifier(Lists.newArrayList(new Identifier("patientId", encounterVo.getCodePi())));
            cnOrder.getSubject().setName(new TextElement(encounterVo.getNamePi()));
            cnOrder.getSubject().setGender(encounterVo.getDtSex());
            cnOrder.getSubject().setBirthDate(DateUtils.formatDate(encounterVo.getBirthDate(),"yyyy-MM-dd"));
            cnOrder.setEncounter(new OrderReference());
            cnOrder.getEncounter().setClas(TransfTool.getPvtype(encounterVo.getEuPvtype()));
            cnOrder.getEncounter().setIdentifiers(Lists.newArrayList(new Identifier("visitNo",encounterVo.getCodeIp())
                    ,new Identifier("times",String.valueOf((encounterVo.getIpTimes()==null?1:encounterVo.getIpTimes())))
                    ,new Identifier("bedNo",encounterVo.getBedNo())));
            cnOrder.setSupportingInformation(Lists.newArrayList(
                    new OrderReference(Lists.newArrayList(new Identifier("orderDeptId",orderVo.getCodeDept())),orderVo.getNameDept()),
                    new OrderReference(Lists.newArrayList(new Identifier("orderWardId",orderVo.getCodeDeptArea())),orderVo.getNameDeptArea())));
            cnOrder.setAuthoredOn(orderVo.getDateCreate());
            cnOrder.setRequester(new OrderReference(Lists.newArrayList(new Identifier("enterDoc",orderVo.getCodeEmpOrd())),new TextElement(orderVo.getNameEmpOrd())));
            cnOrder.setPerformer(new OrderReference(Lists.newArrayList(new Identifier("performNurse",orderVo.getCodeEmpChk())),new TextElement(orderVo.getNameEmpChk())));
            cnOrder.setPerformerType(new TextElement(orderVo.getEuAlwaysCode()));
            cnOrder.setRecorder(new OrderReference(Lists.newArrayList(new Identifier("recordDoc",orderVo.getCodeEmpOrd())),new TextElement(orderVo.getNameEmpOrd())));
            cnOrder.setGroupIdentifier(new Identifier("code/groupId",String.valueOf(orderVo.getOrdsnParent())));
            cnOrder.setNote(Lists.newArrayList(new TextElement(StringUtils.isBlank(orderVo.getNoteOrd())?"":orderVo.getNoteOrd())));
            //无法识别和对照平台提供的医保？暂写为普通
            cnOrder.setInsurance(Lists.newArrayList(new CnOrderOperation.Insurance(Lists.newArrayList(new CnOrderOperation.InsuranceClass("04","普通病人")))));
            cnOrder.setDosageInstruction(Lists.newArrayList(new OrderReference(new Timing(Lists.newArrayList())
                    ,new CodeableConcept(Lists.newArrayList(new Coding(orderVo.getCodeSupply(),orderVo.getNameSupply())))
                    ,null,new Identifier(new CodeableConcept(Lists.newArrayList(new Coding(orderVo.getCodeFreq(),orderVo.getNameFreq()))),new Mumerator(String.valueOf(orderVo.getDosage()),orderVo.getDosName())))));

            cnOrder.setDispenseRequest(new OrderReference(new ValuePeriod(orderVo.getDateStart(),orderVo.getDateSend()),new OrderReference()));
            cnOrder.getDispenseRequest().getPerformer().setIdentifiers(Lists.newArrayList(new Identifier("execDept",orderVo.getCodeDeptEx())));
            cnOrder.getDispenseRequest().getPerformer().setName(orderVo.getNameDeptEx());
            cnOrder.getDispenseRequest().setQuantity(new Mumerator(String.valueOf(orderVo.getQuanCg()),orderVo.getUnitCgName()));
            businessBase.getEntry().add(new Entry(cnOrder));
        }

        RequestData requestData = RequestData.newBuilder()
                .id(businessBase.getId())
                .data(JSON.toJSONString(businessBase))
                .msgIndexData(MsgIndexData.newBuilder()
                        .codeOp(encounterVo.getCodeOp())
                        .codeIp(encounterVo.getCodeIp())
                        .codePi(encounterVo.getCodePi())
                        .codePv(encounterVo.getCodePv())
                        .build())
                .remoteMethod("MedicationRequest").build();

        log.info("住院医嘱信息构造完成id:{},status,{},data:{},remoteMethod:{},implicitRules:{}"
                ,requestData.getId(),MapUtils.getString(paramMap,"chkStatus"),requestData.getData(),requestData.getRemoteMethod(), "ZYYZXX");
        SendAndResolve.getInstance().send(HttpMethod.POST,requestData);
    }
}
