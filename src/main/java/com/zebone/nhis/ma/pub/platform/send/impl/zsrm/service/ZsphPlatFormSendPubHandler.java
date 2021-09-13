package com.zebone.nhis.ma.pub.platform.send.impl.zsrm.service;

import com.zebone.nhis.ma.pub.platform.send.PlatFormSendHandlerAdapter;
import com.zebone.nhis.ma.pub.platform.send.SendOtherPubHandler;
import com.zebone.nhis.ma.pub.platform.send.impl.zsrm.common.TransfTool;
import com.zebone.nhis.ma.pub.zsrm.handler.ZsrmOpHerbDrugPackHandler;
import com.zebone.nhis.ma.pub.zsrm.handler.ZsrmOpWesDrugPackHandler;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

@Service("ZsphPlatFormSendService")
public class ZsphPlatFormSendPubHandler extends PlatFormSendHandlerAdapter {
    //这些类中都不要去try..catch 会统一处理

    @Resource
    private ZsphPlatFormSendAdtHandler sendAdtHandler;
    @Resource
    private ZsrmPlatFormSendBdHandler sendBdHandler;
    @Resource
    private ZsphPlatFormSendBlHandler sendBlHandler;
    @Resource
    private ZsphPlatFormSendCnHandler sendCnHandler;
    @Resource
    private ZsphPlatFormSendExHandler sendExHandler;
    @Resource
    private ZsphPlatFormSendIpHandler sendIpHandler;
    @Resource
    private ZsphPlatFormSendOpHandler sendOpHandler;
    @Resource
    private ZsrmOpWesDrugPackHandler opWesDrugPackHandler;
    @Resource
    private ZsphPlatFormSendIpNurseHandler platFormSendIpNurseHandler;
    @Resource
    private ZsphPlatFormSendIpCnHandler platFormSendIpCnHandler;

    @Resource
    private ZsrmOpHerbDrugPackHandler opHerbDrugPackHandler;


    @Override
    public void sendBdTermDiagMsg(Map<String, Object> paramMap) {
        sendBdHandler.sendBdTermDiagMsg(paramMap);
    }

    @Override
    public void sendBdOrdMsg(Map<String, Object> paramMap) {
        sendBdHandler.sendBdOrdMsg(paramMap);
    }

    @Override
    public void sendBdItemMsg(Map<String, Object> paramMap) {
        sendBdHandler.sendBdItemMsg(paramMap);
    }

    @Override
    public void sendBdOuDeptMsg(Map<String, Object> paramMap) {
        if("_UPDATE".equals(TransfTool.getPropValueStr(paramMap,"STATUS"))){
            sendBdHandler.sendBdOuDeptUpdatrMsg(paramMap);
        }else{
            sendBdHandler.sendBdOuDeptMsg(paramMap);
        }
    }

    @Override
    public void sendBdOuEmpMsg(Map<String, Object> paramMap) {
        if("_UPDATE".equals(TransfTool.getPropValueStr(paramMap,"STATUS"))){
            sendBdHandler.sendBdOuEmpUpdateMsg(paramMap);
        }else{
            sendBdHandler.sendBdOuEmpMsg(paramMap);
        }

    }

    @Override
    public void sendBdPdMsg(Map<String, Object> paramMap) {
        sendBdHandler.sendBdPdMsg(paramMap);
    }

    /**
     * 发送机构信息
     * @param paramMap
     */
    @Override
    public void sendBdOuOrgMsg(Map<String, Object> paramMap) {
        sendBdHandler.sendBdOuOrgMsg(paramMap);
    }

    @Override
    public void sendPiMasterMsg(Map<String, Object> paramMap) {
        sendAdtHandler.sendPiMasterMsg(paramMap);
    }

    @Override
    public void sendPvOpRegMsg(Map<String, Object> paramMap) {
        sendOpHandler.sendPvOpRegMsg(paramMap);
    }

    @Override
    public void sendPvOpCancelRegMsg(Map<String, Object> paramMap) {
        paramMap.put("isAdd","1");
        sendOpHandler.sendPvOpRegMsg(paramMap);
    }

    @Override
    public void sendOpEmeOrdMag(Map<String, Object> paramMap) {
        sendCnHandler.sendOpEmeOrdMag(paramMap);
        sendOpHandler.sendWeChatApptFinishClinic(paramMap);
    }

    @Override
    public void sendBlOpSettleMsg(Map<String, Object> paramMap) {
        sendCnHandler.sendBlOpSettleMsg(paramMap);
        sendOpHandler.sendWeChatApptFinishSettle(paramMap);
    }

    @Override
    public void sendCnDiagMsg(Map<String, Object> paramMap) {
        sendCnHandler.sendCnDiagMsg(paramMap);
    }

    @Override
    public void sendSchInfo(Map<String, Object> paramMap) {
        if(paramMap!=null && "1".equals(paramMap.get("isSendShortMsg"))) {
            SendOtherPubHandler.invokeOther(sendOpHandler.getClass(),"sendPvApptRegOfSchStop",paramMap);
            //sendOpHandler.sendShortMsgForSchUpdate(paramMap);
            sendOpHandler.sendNewShortMsgForSchUpdate(paramMap);
        }
        if(paramMap!=null && "1".equals(paramMap.get("isWeChat"))) {
            //sendOpHandler.sendShortMsgForSchUpdate(paramMap);
            sendOpHandler.sendWeChatApptStopRegMsg(paramMap);

        }

    }

    @Override
    public void sendSchAppt(Map<String, Object> paramMap) {
        sendOpHandler.sendPvApptRegMsg(paramMap);
    }

    @Override
    public void sendSchApptReg(Map<String, Object> paramMap) {
        sendOpHandler.sendPvApptRegMsg(paramMap);
        sendOpHandler.sendWeChatApptRegMsg(paramMap);
        //sendOpHandler.sendShortMsgApptRegMsg(paramMap);
        sendOpHandler.sendNewShortMsgApptRegMsg(paramMap);

    }


    @Override
    public String sendCnOpCall(Map<String, Object> paramMap) {
        sendOpHandler.sendCnOpCall(paramMap);
        return null;
    }

    @Override
    public void sendConfirmDosage(Map<String, Object> paramMap) {
        sendOpHandler.sendConfirmDosage(paramMap);
    }

    @Override
    public void sendShortMsgPhoneChk(Map<String, Object> paramMap) {
        sendOpHandler.sendShortMsgForCheckPhone(paramMap);
    }

    @Override
    public void sendDstributeCardMsg(Map<String, Object> paramMap) {

    }

    @Override
    public void sendCnPresOpMsg(Map<String, Object> paramMap) {
        opWesDrugPackHandler.sendOrderId(paramMap);
        opHerbDrugPackHandler.sendHerbPresInfo(paramMap);
        //发送门诊处方信息
        sendCnHandler.sendCnPresOpMsg(paramMap);
    }

    /**
     * 发送费用短信提醒信息
     * zsrm-门诊欠费短信提醒
     * @param paramMap
     */
    @Override
    public void sendOpFeeReminderMsg(Map<String, Object> paramMap) {
        sendBlHandler.sendOpFeeReminderMsg(paramMap);
    }

    /**
     * 发送门诊转住院消息
     *
     * @param paramMap
     */
    @Override
    public void sendOpToIpMsg(Map<String, Object> paramMap) {sendOpHandler.sendOpToIpMsg(paramMap);}

    /**
     * 入院登记
     * @param paramMap
     */
    @Override
    public void sendPvInfoMsg(Map<String, Object> paramMap) {
        sendIpHandler.sendPvInfoMsg(paramMap);
    }

    /**
     * 发送门诊诊毕消息 sendCancelClinicMsg
     * @param paramMap
     */
    @Override
    public void sendFinishClinicMsg(Map<String, Object> paramMap){
        paramMap.put("isAdd","1");
        sendOpHandler.sendPvOpRegMsg(paramMap);
    }

    /**
     * 发送门诊取消到诊消息
     * @param paramMap
     */
    @Override
    public void sendCancelClinicMsg(Map<String, Object> paramMap){
        paramMap.put("isAdd","1");
        sendOpHandler.sendPvOpRegMsg(paramMap);
    }

    /**
     * 发送医嘱核对信息
     * @param paramMap
     */
    @Override
    public void sendExOrderCheckMsg(Map<String, Object> paramMap){
        paramMap.put("isAdd","0");
        paramMap.put("chkStatus","Check");
        new Thread(()->{
            platFormSendIpCnHandler.sendExOrderCheckMsg(paramMap);
        }).start();
        sendCnHandler.checkOpLisOrRis(paramMap);
    }


    /**
     * 检查作废
     * @param paramMap
     */
    @Override
    public void sendCancleRisApplyListMsg(Map<String, Object> paramMap){
        paramMap.put("isAdd","1");
        sendCnHandler.checkOpLisOrRis(paramMap);
    }

    /**
     * 检验作废
     * @param paramMap
     */
    @Override
    public void sendCancleLisApplyListMsg(Map<String, Object> paramMap){
        paramMap.put("isAdd","1");
        sendCnHandler.checkOpLisOrRis(paramMap);
    }

    /**
     * 手术作废
     * @param paramMap
     */
    @Override
    public void sendOpApplyMsg(Map<String, Object> paramMap) {
        if("OC".equals(MapUtils.getString(paramMap,"control"))){
            paramMap.put("isAdd","1");
            sendCnHandler.checkOpLisOrRis(paramMap);
        }
    }


    @Override
    public void sendDeptInMsg(Map<String, Object> paramMap) {
        new Thread(()->{
            if("新出生".equals(MapUtils.getString(paramMap,"adtType"))){
                platFormSendIpNurseHandler.sendNewBornRegister(paramMap);
            }
        }).start();
        platFormSendIpNurseHandler.sendDeptInMsg(paramMap);
    }

    @Override
    public void sendCancelDeptInMsg(Map<String, Object> paramMap) {
        new Thread(()->{
            if("新出生".equals(MapUtils.getString(paramMap,"adtType"))){
                platFormSendIpNurseHandler.sendNewBornRegister(paramMap);
            }
        }).start();
        platFormSendIpNurseHandler.sendCancelDeptInMsg(paramMap);
    }

    @Override
    public void sendDeptChangeMsg(Map<String, Object> paramMap) {
        platFormSendIpNurseHandler.sendDeptChangeMsg(paramMap);
    }

    @Override
    public void sendCancelDeptChangeMsg(Map<String, Object> paramMap) {
        platFormSendIpNurseHandler.sendCancelDeptChangeMsg(paramMap);
    }

    @Override
    public void sendBedChange(Map<String, Object> paramMap) {
        platFormSendIpNurseHandler.sendBedChangeMsg(paramMap);
    }

    @Override
    public void sendBedPackMsg(Map<String, Object> paramMap){
        platFormSendIpNurseHandler.sendBedPackMsg(paramMap);
    }

    @Override
    public void sendBedRtnPackMsg(Map<String, Object> paramMap){
        platFormSendIpNurseHandler.sendBedRtnPackMsg(paramMap);
    }

    @Override
    public void sendOperaOrderCancelMsg(Map<String, Object> paramMap){
        if(paramMap!=null){
            List<Map<String, Object>> list = (List<Map<String, Object>>) paramMap.get("changeOrdList");
            if(CollectionUtils.isEmpty(list))
                return;
            paramMap.put("chkStatus","Cancel");
            paramMap.put("pkPv", MapUtils.getString(list.get(0),"pkPv"));
            paramMap.put("ordlist", list);
            platFormSendIpCnHandler.sendExOrderCheckMsg(paramMap);
        }
    }

    @Override
    public void sendPvOutMsg(Map<String, Object> paramMap) {
        if(paramMap!=null){
            paramMap.put("implicitRules", "ZYCKXX");
        }
        platFormSendIpNurseHandler.sendPvOutMsg(paramMap);
    }

    @Override
    public void sendPvCancelOutMsg(Map<String, Object> paramMap) {
        if (paramMap != null) {
            paramMap.put("implicitRules", "ZYCKQX");
        }
        platFormSendIpNurseHandler.sendPvOutMsg(paramMap);
    }

    /**
     * 向微信推送当天未缴费信息模板内容
     */
    @Override
    public void sendWeiXinForNotPayCost(Map<String, Object> paramMap) {
        sendOpHandler.sendWeiXinForNotPayCost(paramMap);
    }

    /**
     * 发送用法信息
     *
     * @param paramMap {codeEmp,supply(BdSupply),STATUS:_ADD,_UPDATE,_DELETE}
     */
    @Override
    public void sendBdSupplyMsg(Map<String, Object> paramMap) {
        sendBdHandler.sendBdSupplyMsg(paramMap);
    }

    /**
     * 发送频次信息
     *
     * @param paramMap {codeEmp,freq(BdTermFreq),STATUS:_ADD,_UPDATE,_DELETE}
     */
    @Override
    public void sendBdTermFreqMsg(Map<String, Object> paramMap) {
        sendBdHandler.sendBdTermFreqMsg(paramMap);
    }

    /**
     * 发送医疗组信息
     *
     * @param paramMap
     */
    @Override
    public void sendOrgDeptWgMsg(Map<String, Object> paramMap) {
        sendBdHandler.sendOrgDeptWgMsg(paramMap);
    }

    @Override
    public void sendBdResBedMsg(Map<String, Object> paramMap) {
        //3.12床位信息字典
        sendAdtHandler.sendBdResBedMsg(paramMap);    }

    @Override
    public void sendBdItemcateMsg(Map<String, Object> paramMap) {
        sendAdtHandler.sendBdItemcateMsg(paramMap);
    }

}
