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
 * 挂号
 */
@Service
public class PskqPlatFormSendRegHandler {
    @Autowired
    private PskqPlatFormSendOpMapper pskqPlatFormSendOpMapper;
    @Autowired
    private HttpRestTemplate httpRestTemplate;
    /**
     * 发送挂号信息
     * @param paramMap
     * @param listener
     */
    public void sendRegisterInfo(Map<String, Object> paramMap, ResultListener listener){
        String result = "";
        String responseBody="";
        try {
            //查询挂号相关信息
            Outpatient outpatient = pskqPlatFormSendOpMapper.getRegisterEncounterOp(paramMap);
            if(outpatient==null){
                listener.error("未获取到相关挂号信息！","","");
                return;
            }
            //查询患者信息
            paramMap.put("codePi",outpatient.getPkPatient());
            PatientInfo patientInfo = pskqPlatFormSendOpMapper.getPiMasterById(paramMap);
            if(patientInfo==null){
                listener.error("未获取到患者信息！","","");
                return;
            }

            //设置证件类型：国家标准
            patientInfo.setIdTypeCode(PskqDictionMapUtil.hisToPingIdnoTypeMap(patientInfo.getIdTypeCode()));

            //创建挂号消息
            Encounter encounter = new Encounter();
            encounter.setOutPatient(outpatient);
            encounter.setPatientInfo(patientInfo);
            //查询就诊科室代码和就诊科室名称
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
            map.put("serviceName", "门诊挂号信息新增服务");
            map.put("eventCode", "E002301");
            map.put("eventName", "挂号");


            RequestBody requestBody = message.getRequestBody(map);
            result =  JSON.toJSONString(requestBody);
            //发送消息
            responseBody = httpRestTemplate.postForString(result);
            ResponseBody paramVo = JsonUtil.readValue(responseBody,ResponseBody.class);
            //消息成功
            if(paramVo !=null && paramVo.getAck()!=null && "AA".equals(paramVo.getAck().get("ackCode"))){
                listener.success(result,responseBody);
            }else {
                listener.error("发消息时出错",result,responseBody);
            }
        }catch (Exception e){
            listener.exception(e.getMessage(),result,responseBody);
        }
    }
    /**
     * 发送退挂号信息
     * @param paramMap
     * @param listener
     */
    public void sendRetreatInfo(Map<String, Object> paramMap, ResultListener listener){
        String result = "";
        String responseBody="";
        try {
            //查询退号信息
            Outpatient outpatient = pskqPlatFormSendOpMapper.getRetreatEncounterOp(paramMap);
            if(outpatient==null){
                listener.error("未获取到相关退挂号信息！","","");
                return;
            }
            //查询患者信息
            paramMap.put("codePi",outpatient.getPkPatient());
            PatientInfo patientInfo = pskqPlatFormSendOpMapper.getPiMasterById(paramMap);
            if(patientInfo==null){
                listener.error("未获取到患者信息！","","");
                return;
            }
            
            //写第三方退费记录信息
            String channelCode = outpatient.getRegisterChannelCode();
            if("2510".equals(channelCode) || "2500".equals(channelCode) || "2520".equals(channelCode)) {
                BlDeposit blDeposit = DataBaseHelper.queryForBean("select * from BL_DEPOSIT where EU_DIRECT = '1' and PK_PV = ? ", BlDeposit.class,CommonUtils.getPropValueStr(paramMap, "pkPv"));
                if(blDeposit != null) {
                    BlDeposit blDepositRef = DataBaseHelper.queryForBean("select * from BL_DEPOSIT where EU_DIRECT = '-1' and PK_PV = ? ", BlDeposit.class,CommonUtils.getPropValueStr(paramMap, "pkPv"));
                	String pkDepo = blDeposit.getPkDepo();//原结算主键
                	String pkDepoRef = "";
                	String pkSettle = "";
                	if(blDepositRef != null) {
                		pkDepoRef = blDepositRef.getPkDepo();//退费结算主键
                		pkSettle = blDepositRef.getPkSettle();
                	}
                    saveBlExtPay(pkDepo,pkDepoRef,pkSettle); 
                }
            }
            
            //设置证件类型：国家标准
            patientInfo.setIdTypeCode(PskqDictionMapUtil.hisToPingIdnoTypeMap(patientInfo.getIdTypeCode()));
            
            //创建挂号消息
            Encounter encounter = new Encounter();
            encounter.setOutPatient(outpatient);
            encounter.setPatientInfo(patientInfo);

            MessageFactory messageFactory = new MessageFactory();
            Message message = messageFactory.getInstance(encounter);

            Map<String, Object> map=new HashMap<String, Object>();
            map.put("serviceCode", "S0024");
            map.put("serviceName", "门诊挂号信息更新服务");
            map.put("eventCode", "E002401");
            map.put("eventName", "退号");

            RequestBody requestBody = message.getRequestBody(map);
            result =  JSON.toJSONString(requestBody);
            //发送消息
            responseBody = httpRestTemplate.postForString(result);
            ResponseBody paramVo = JsonUtil.readValue(responseBody,ResponseBody.class);
            //消息成功
            if(paramVo !=null && paramVo.getAck()!=null && "AA".equals(paramVo.getAck().get("ackCode"))){
                listener.success(result,responseBody);
            }else {
                listener.error("发消息时出错",result,responseBody);
            }
        }catch (Exception e){ 
            listener.exception(e.getMessage(),result,responseBody);
        }
    }
    
    /**
     * 保存第三方支付数据
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
            	newblExtPay.setFlagPay("1");//支付标志
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
