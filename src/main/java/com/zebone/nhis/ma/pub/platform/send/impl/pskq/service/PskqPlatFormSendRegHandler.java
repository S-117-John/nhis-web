package com.zebone.nhis.ma.pub.platform.send.impl.pskq.service;

import java.text.ParseException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import com.zebone.nhis.ma.pub.platform.pskq.web.HttpRestTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.stereotype.Service;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import com.alibaba.fastjson.JSON;
import com.zebone.nhis.common.module.base.ou.BdOuDept;
import com.zebone.nhis.common.module.bl.BlDeposit;
import com.zebone.nhis.common.module.pay.BlExtPay;
import com.zebone.nhis.common.module.pv.PvEncounter;
import com.zebone.nhis.common.support.ApplicationUtils;
import com.zebone.nhis.common.support.CommonUtils;
import com.zebone.nhis.ma.pub.platform.pskq.factory.Message;
import com.zebone.nhis.ma.pub.platform.pskq.factory.MessageFactory;
import com.zebone.nhis.ma.pub.platform.pskq.model.Encounter;
import com.zebone.nhis.ma.pub.platform.pskq.model.Outpatient;
import com.zebone.nhis.ma.pub.platform.pskq.model.PatientInfo;
import com.zebone.nhis.ma.pub.platform.pskq.model.listener.ResultListener;
import com.zebone.nhis.ma.pub.platform.pskq.model.message.RequestBody;
import com.zebone.nhis.ma.pub.platform.pskq.model.message.ResponseBody;
import com.zebone.nhis.ma.pub.platform.pskq.repository.BdOuDeptRepository;
import com.zebone.nhis.ma.pub.platform.pskq.repository.PvEncounterRepository;
import com.zebone.nhis.ma.pub.platform.pskq.service.MyRestTemplateUtil;
import com.zebone.nhis.ma.pub.platform.pskq.utils.PskqDictionMapUtil;
import com.zebone.nhis.ma.pub.platform.send.impl.pskq.dao.PskqPlatFormSendOpMapper;
import com.zebone.nhis.webservice.pskq.config.SpringContextHolder;
import com.zebone.platform.common.support.NHISUUID;
import com.zebone.platform.modules.dao.DataBaseHelper;
import com.zebone.platform.modules.utils.JsonUtil;

/**
 * @author tjq
 * ??????
 */
@Service
public class PskqPlatFormSendRegHandler {
    @Autowired
    private PskqPlatFormSendOpMapper pskqPlatFormSendOpMapper;
    @Autowired
    private HttpRestTemplate httpRestTemplate;
    /**
     * ??????????????????
     * @param paramMap
     * @param listener
     */
    public void sendRegisterInfo(Map<String, Object> paramMap, ResultListener listener){
        String result = "";
        String responseBody="";
        try {
            //????????????????????????
            Outpatient outpatient = pskqPlatFormSendOpMapper.getRegisterEncounterOp(paramMap);
            if(outpatient==null){
                listener.error("?????????????????????????????????","","");
                return;
            }
            //??????????????????
            paramMap.put("codePi",outpatient.getPkPatient());
            PatientInfo patientInfo = pskqPlatFormSendOpMapper.getPiMasterById(paramMap);
            if(patientInfo==null){
                listener.error("???????????????????????????","","");
                return;
            }

            //?????????????????????????????????
            patientInfo.setIdTypeCode(PskqDictionMapUtil.hisToPingIdnoTypeMap(patientInfo.getIdTypeCode()));

            //??????????????????
            Encounter encounter = new Encounter();
            encounter.setOutPatient(outpatient);
            encounter.setPatientInfo(patientInfo);
            //?????????????????????????????????????????????
            if(paramMap.containsKey("pkPv")){
                PvEncounter pvEncounter = PvEncounterRepository.getOne(paramMap.get("pkPv").toString());
                String pkDept = pvEncounter.getPkDept();
                BdOuDept bdOuDept = BdOuDeptRepository.getOne(pkDept);
                if(bdOuDept!=null){
                    outpatient.setClinicRoomCode(pkDept);
                    outpatient.setClinicRoomName(bdOuDept.getNameDept());
                }

            }



            MessageFactory messageFactory = new MessageFactory();
            Message message = messageFactory.getInstance(encounter);

            Map<String, Object> map=new HashMap<String, Object>();
            map.put("serviceCode", "S0023");
            map.put("serviceName", "??????????????????????????????");
            map.put("eventCode", "E002301");
            map.put("eventName", "??????");


            RequestBody requestBody = message.getRequestBody(map);
            result =  JSON.toJSONString(requestBody);
            //????????????
            responseBody = httpRestTemplate.postForString(result);
            ResponseBody paramVo = JsonUtil.readValue(responseBody,ResponseBody.class);
            //????????????
            if(paramVo !=null && paramVo.getAck()!=null && "AA".equals(paramVo.getAck().get("ackCode"))){
                listener.success(result,responseBody);
            }else {
                listener.error("??????????????????",result,responseBody);
            }
        }catch (Exception e){
            listener.exception(e.getMessage(),result,responseBody);
        }
    }
    /**
     * ?????????????????????
     * @param paramMap
     * @param listener
     */
    public void sendRetreatInfo(Map<String, Object> paramMap, ResultListener listener){
        String result = "";
        String responseBody="";
        try {
            //??????????????????
            Outpatient outpatient = pskqPlatFormSendOpMapper.getRetreatEncounterOp(paramMap);
            if(outpatient==null){
                listener.error("????????????????????????????????????","","");
                return;
            }
            //??????????????????
            paramMap.put("codePi",outpatient.getPkPatient());
            PatientInfo patientInfo = pskqPlatFormSendOpMapper.getPiMasterById(paramMap);
            if(patientInfo==null){
                listener.error("???????????????????????????","","");
                return;
            }
            
            //??????????????????????????????
            String channelCode = outpatient.getRegisterChannelCode();
            if("2510".equals(channelCode) || "2500".equals(channelCode) || "2520".equals(channelCode)) {
                BlDeposit blDeposit = DataBaseHelper.queryForBean("select * from BL_DEPOSIT where EU_DIRECT = '1' and PK_PV = ? ", BlDeposit.class,CommonUtils.getPropValueStr(paramMap, "pkPv"));
                if(blDeposit != null) {
                    BlDeposit blDepositRef = DataBaseHelper.queryForBean("select * from BL_DEPOSIT where EU_DIRECT = '-1' and PK_PV = ? ", BlDeposit.class,CommonUtils.getPropValueStr(paramMap, "pkPv"));
                	String pkDepo = blDeposit.getPkDepo();//???????????????
                	String pkDepoRef = "";
                	String pkSettle = "";
                	if(blDepositRef != null) {
                		pkDepoRef = blDepositRef.getPkDepo();//??????????????????
                		pkSettle = blDepositRef.getPkSettle();
                	}
                    saveBlExtPay(pkDepo,pkDepoRef,pkSettle); 
                }
            }
            
            //?????????????????????????????????
            patientInfo.setIdTypeCode(PskqDictionMapUtil.hisToPingIdnoTypeMap(patientInfo.getIdTypeCode()));
            
            //??????????????????
            Encounter encounter = new Encounter();
            encounter.setOutPatient(outpatient);
            encounter.setPatientInfo(patientInfo);

            MessageFactory messageFactory = new MessageFactory();
            Message message = messageFactory.getInstance(encounter);

            Map<String, Object> map=new HashMap<String, Object>();
            map.put("serviceCode", "S0024");
            map.put("serviceName", "??????????????????????????????");
            map.put("eventCode", "E002401");
            map.put("eventName", "??????");

            RequestBody requestBody = message.getRequestBody(map);
            result =  JSON.toJSONString(requestBody);
            //????????????
            responseBody = httpRestTemplate.postForString(result);
            ResponseBody paramVo = JsonUtil.readValue(responseBody,ResponseBody.class);
            //????????????
            if(paramVo !=null && paramVo.getAck()!=null && "AA".equals(paramVo.getAck().get("ackCode"))){
                listener.success(result,responseBody);
            }else {
                listener.error("??????????????????",result,responseBody);
            }
        }catch (Exception e){ 
            listener.exception(e.getMessage(),result,responseBody);
        }
    }
    
    /**
     * ???????????????????????????
     *
     * @throws ParseException
     */
    public void saveBlExtPay(String pkDepo,String pkDepoRef,String pkSettle){ 	
    	DefaultTransactionDefinition transDefinition = new DefaultTransactionDefinition();
        transDefinition.setPropagationBehavior(DefaultTransactionDefinition.PROPAGATION_REQUIRES_NEW);
        DataSourceTransactionManager dataSourceTransactionManager = SpringContextHolder.getApplicationContext().getBean(DataSourceTransactionManager.class);
        TransactionStatus transStatus = dataSourceTransactionManager.getTransaction(transDefinition);
        try {
        	BlExtPay blExtPay = DataBaseHelper.queryForBean("select * from BL_EXT_PAY where pk_depo = ? ", BlExtPay.class,pkDepo);
            if(blExtPay != null) {
            	BlExtPay newblExtPay = new BlExtPay();
            	ApplicationUtils.copyProperties(newblExtPay, blExtPay);
            	newblExtPay.setPkExtpay(NHISUUID.getKeyId());
            	newblExtPay.setAmount(blExtPay.getAmount().negate());
            	newblExtPay.setPkDepo(pkDepoRef);
            	newblExtPay.setPkSettle(pkSettle);
            	newblExtPay.setFlagPay("1");//????????????
            	newblExtPay.setDatePay(new Date());
            	newblExtPay.setCreateTime(new Date());
            	newblExtPay.setTs(new Date());
            	newblExtPay.setDelFlag("0");
                DataBaseHelper.insertBean(newblExtPay);
            }
            dataSourceTransactionManager.commit(transStatus);
		} catch (Exception e) {
			dataSourceTransactionManager.rollback(transStatus);
		}
    }
}
