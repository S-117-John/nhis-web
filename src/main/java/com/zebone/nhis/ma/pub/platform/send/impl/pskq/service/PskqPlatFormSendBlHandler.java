package com.zebone.nhis.ma.pub.platform.send.impl.pskq.service;

import java.text.ParseException;
/**
 * @author tjq
 * 费用
 */
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.zebone.nhis.ma.pub.platform.pskq.model.*;
import com.zebone.nhis.ma.pub.platform.pskq.web.HttpRestTemplate;
import com.zebone.nhis.common.module.bl.BlDeposit;
import com.zebone.nhis.common.module.cn.ipdw.CnOrder;
import com.zebone.nhis.common.module.ma.msg.SysMsgRec;
import com.zebone.nhis.common.support.ApplicationUtils;
import com.zebone.nhis.common.support.DateUtils;
import com.zebone.nhis.common.support.XmlUtil;
import com.zebone.nhis.ma.pub.platform.pskq.model.message.MessageLog;
import com.zebone.nhis.ma.pub.platform.pskq.repository.CnOrderRepository;
import com.zebone.nhis.ma.pub.platform.pskq.service.impl.RestSysMessageServiceImpl;
import com.zebone.nhis.ma.pub.platform.pskq.web.HttpRestTemplate;
import com.zebone.nhis.ma.pub.platform.send.impl.pskq.model.StExInfoVo;
import com.zebone.nhis.ma.pub.platform.send.impl.pskq.model.StExReqVo;
import com.zebone.nhis.webservice.pskq.config.SpringContextHolder;
import com.zebone.platform.common.support.NHISUUID;
import com.zebone.platform.modules.core.spring.ServiceLocator;
import com.zebone.platform.modules.dao.DataBaseHelper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.alibaba.fastjson.JSON;
import com.zebone.nhis.common.module.pay.BlExtPay;
import com.zebone.nhis.common.support.ApplicationUtils;
import com.zebone.nhis.common.support.CommonUtils;
import com.zebone.nhis.ma.pub.platform.pskq.dao.SysMsgRecDao;
import com.zebone.nhis.ma.pub.platform.pskq.factory.Message;
import com.zebone.nhis.ma.pub.platform.pskq.factory.MessageFactory;
import com.zebone.nhis.ma.pub.platform.pskq.model.listener.ResultListener;
import com.zebone.nhis.ma.pub.platform.pskq.model.message.RequestBody;
import com.zebone.nhis.ma.pub.platform.pskq.model.message.ResponseBody;
import com.zebone.nhis.ma.pub.platform.pskq.utils.MapUtils;
import com.zebone.nhis.ma.pub.platform.pskq.utils.PskqMesUtils;
import com.zebone.nhis.ma.pub.platform.pskq.utils.RestTemplateUtil;
import com.zebone.nhis.ma.pub.platform.send.impl.pskq.dao.PskqPlatFormSendBlMapper;
import com.zebone.nhis.ma.pub.platform.send.impl.pskq.dao.PskqPlatFormSendCnMapper;
import com.zebone.platform.modules.dao.DataBaseHelper;
import com.zebone.platform.modules.utils.JsonUtil;

import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.DefaultTransactionDefinition;


@Service
public class PskqPlatFormSendBlHandler implements InitializingBean {
    @Autowired
    private PskqPlatFormSendBlMapper pskqPlatFormSendBlMapper;

    @Autowired
    SysMsgRecDao sysMsgRecDao;
    @Autowired
    private PskqPlatFormSendCnMapper pskqPlatFormSendCnMapper;

    @Autowired
    private HttpRestTemplate httpRestTemplate;

    //坪山口腔数据日志记录
    private static final RestSysMessageServiceImpl sysMessageService = new RestSysMessageServiceImpl();
    private Logger logger = LoggerFactory.getLogger("nhis.otherPlatLog");


    /**
     * 发送门诊退费消息
     * @param paramMap
     * @param listener
     */
    public void sendOpRefundNews(Map<String, Object> paramMap, ResultListener listener){
        String result = "";
        String responseBody="";
        try {
            //查询关系信息
            @SuppressWarnings("unchecked")
            List<Map<String,Object>> opDtList = PskqMesUtils.lisBToLisMap((List<Object>)paramMap.get("blOpDts"));

            List<String> pkCgops=new ArrayList<>();
            for(int i=0;i<opDtList.size();i++){
                pkCgops.add(CommonUtils.getPropValueStr(opDtList.get(i), "pkCgop"));
            }

            //查询退费信息
            List<CostDetailOutpat> costDetailOutpat = pskqPlatFormSendBlMapper.queBlOpDtRefund(pkCgops);
            if(costDetailOutpat.size()<=0){
                listener.error("未获取到相关门诊退费信息信息！","","");
                return;
            }
            for (CostDetailOutpat costDtl : costDetailOutpat) {
            	costDtl.setEncounterId(PskqMesUtils.pskqHisCode+costDtl.getEncounterTypeCode()+"_"+PskqMesUtils.pskqSysCode+costDtl.getEncounterId());
            	costDtl.setOrderId(PskqMesUtils.pskqSysCode+costDtl.getEncounterTypeCode()+"_"+PskqMesUtils.pskqHisCode+costDtl.getOrderId());
			}
            //根据费用明细查询门诊退费结算信息
            List<SettlementMasterOutpat> settlementMasterInfo =  pskqPlatFormSendBlMapper.queRefundSelectSettleMasterInfo(pkCgops);
            //根据费用明细查询发票信息
            List<SettlementDetailOutpat> settlementDetailInfo =  pskqPlatFormSendBlMapper.queRefundSelectSettleDetailInfo(pkCgops);
            
            //写第三方退费记录信息
            String extCdoe = settlementDetailInfo.get(0).getSourceSystemCode();//源系统代码，标志是不是第三方收费
            if("2510".equals(extCdoe) || "2500".equals(extCdoe) || "2520".equals(extCdoe)) {
            	String settlementNo = settlementMasterInfo.get(0).getSettlementNo();//第三方支付单
                String pkDepoRef = "";//退费结算主键
                for (SettlementMasterOutpat settlementMaster: settlementMasterInfo) {
                	pkDepoRef = settlementMaster.getSettlementMasterId();
    			}
            	saveBlExtPay(settlementNo,pkDepoRef);
            }
            SettlementOutpat settlementOutpat = new SettlementOutpat();
            settlementOutpat.setSettlementMasterOutpat(settlementMasterInfo);
            settlementOutpat.setSettlementDetailOutpat(settlementDetailInfo);
            settlementOutpat.setCostDetailOutpats(costDetailOutpat);

            MessageFactory messageFactory = new MessageFactory();
            Message message = messageFactory.getInstance(settlementOutpat);

            RequestBody requestBody = message.getRequestBody(paramMap);
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
     * 门诊收费发送给坪山口腔消息实现
     * @param paramMap
     * @param listener
     */
    public void sendBlOpSettleIncomeMsg(Map<String, Object> paramMap, ResultListener listener){
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        String result = "";
        String responseBody="";
        try {

            if(paramMap== null || paramMap.get("pkSettle")==null){
                listener.error("未获取到查询门诊收费主键参数信息！","","");
                return;
            }
            List<SettlementMasterOutpat> settlementMasterInfo =  sysMsgRecDao.selectSettleMasterInfo(paramMap);
            List<SettlementDetailOutpat> settlementDetailInfo =  sysMsgRecDao.selectSettleDetailInfo(paramMap);

          /*  //Base64 加密
            if(settlementDetailInfo.get(0).getSettlementDetailPdfUrl()!=null&&settlementDetailInfo.get(0).getSettlementDetailPdfUrl()!=""){
                byte[] bytes = settlementDetailInfo.get(0).getSettlementDetailPdfUrl().getBytes();
                String encoded = Base64.getEncoder().encodeToString(bytes);
                settlementDetailInfo.get(0).setSettlementDetailPdfUrl(encoded);
            }*/

            List<CostDetailOutpat> costDetailOutpat = sysMsgRecDao.selectCostDetailInfo(paramMap);
            for (CostDetailOutpat costDtl : costDetailOutpat) {
            	costDtl.setEncounterId(PskqMesUtils.pskqHisCode+costDtl.getEncounterTypeCode()+"_"+PskqMesUtils.pskqSysCode+costDtl.getEncounterId());
            	costDtl.setOrderId(PskqMesUtils.pskqSysCode+costDtl.getEncounterTypeCode()+"_"+PskqMesUtils.pskqHisCode+costDtl.getOrderId());
			}
            CostDetailsOutpat costDetailsOutpat = new CostDetailsOutpat();
            costDetailsOutpat.setCostDetailOutpats(costDetailOutpat);

            SettlementOutpat settlementOutpat = new SettlementOutpat();
            settlementOutpat.setSettlementMasterOutpat(settlementMasterInfo);
            settlementOutpat.setSettlementDetailOutpat(settlementDetailInfo);
            settlementOutpat.setCostDetailOutpats(costDetailOutpat);

            MessageFactory messageFactory = new MessageFactory();
            Message message = messageFactory.getInstance(settlementOutpat);

            RequestBody requestBody = message.getRequestBody(paramMap);
            result =  JSON.toJSONString(requestBody);
            //发送消息
            responseBody = httpRestTemplate.postForString(result);
            ResponseBody paramVo = JsonUtil.readValue(responseBody, ResponseBody.class);
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
     * 发送住院预交金消息
     *
     * 预交金充值记录ID/预交金充值单号 待确定
     *
     *
     * @param paramMap
     * @param listener
     */
    public void getIpDepositMsg(Map<String, Object> paramMap, ResultListener listener){

        String result = "";
        String responseBody="";
        try {

            if(paramMap== null || paramMap.get("pkPv")==null){
                listener.error("未获取到患者就诊主键参数信息！","","");
                return;
            }

            AdvancePayment advancePayment = sysMsgRecDao.selectIpAdvancePayment(paramMap);
            advancePayment.setOrgCode("10");
            advancePayment.setOrgName("南方医科大学深圳口腔医院（坪山）");


            switch (PskqMesUtils.getPropValueStr(paramMap,"status")){
                case "OK":
                    paramMap.put("eventCode","E150301");
                    paramMap.put("eventName","住院预交金");
                    break;
                case "CR":
                    paramMap.put("eventCode","E150302");
                    paramMap.put("eventName","取消住院预交金");
                    break;
                default:
                    listener.error("未获取到住院预交金收退费信息！请联系信息科管理员！","","");
                    return;
            }


            MessageFactory messageFactory = new MessageFactory();
            Message message = messageFactory.getInstance(advancePayment);

            RequestBody requestBody = message.getRequestBody(paramMap);
            result =  JSON.toJSONString(requestBody);
            //发送消息
            responseBody = httpRestTemplate.postForString(result);
            ResponseBody paramVo = JsonUtil.readValue(responseBody, ResponseBody.class);
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
     * 门诊收费发送检查申请数据至牙医管家
     * @param paramMap
     */
    public void wbSendStRisInfo(Map<String, Object> paramMap){
        String url = ApplicationUtils.getPropertyValue("denHk.url", "");

        if(!CommonUtils.isEmptyString(url)){
            String result = "";
            String responseBody="";
            try {
                if(paramMap== null || paramMap.get("pkSettle")==null){
                    logger.info("发送检查申请至牙医管家失败,结果:pkSettle入参为空");
                    return;
                }

                //查询检查医嘱信息
                List<Map<String,Object>> stRisList = pskqPlatFormSendBlMapper.qryRisOrdInfoByPkSettle(paramMap);
                if(stRisList!=null && stRisList.size()>0){
                    //查询医嘱下具体的收费项目信息、
                    for(Map<String,Object> ordMap : stRisList){
                        StExInfoVo ordVo = ApplicationUtils.mapToBean(ordMap,StExInfoVo.class);

                        try{
                            //计算年龄
                            ordVo.setPatientAge(DateUtils.getAge(DateUtils.strToDate(ordVo.getBirthday(),"yyyy-MM-dd")));
                        }catch(Exception e){
                            logger.info("发送检查申请至牙医管家失败,结果:{}",e.getMessage());
                            e.printStackTrace();
                        }
                        //查询收费项目信息
                        if(!CommonUtils.isEmptyString(CommonUtils.getPropValueStr(ordMap,"pkCnord"))){
                            ordVo.setItemList(pskqPlatFormSendBlMapper.qryOpdtByPkCnord(CommonUtils.getPropValueStr(ordMap,"pkCnord")));
                        }

                        StExReqVo req = new StExReqVo();
                        req.setExInfoVo(ordVo);

                        //组装Http参数信息
                        result = XmlUtil.beanToXml(req, StExReqVo.class);
                        //result = result.replace("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>","").replace("\n","").replace(" ","");
                        result = result.replace("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>","");

                        //构造soap请求体
                        String soap = "<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:ns=\"http://192.168.5.13:8066/\">\n" +
                                "   <soapenv:Header/>\n" +
                                "   <soapenv:Body>\n" +
                                "      <ns:AppCheckList>\n" +
                                "         <ns:paramXml>\n" +
                                "<![CDATA["+
                                result +
                                "]]>"+
                                "         </ns:paramXml>\n" +
                                "      </ns:AppCheckList>\n" +
                                "   </soapenv:Body>\n" +
                                "</soapenv:Envelope>";
                        String contentType = "text/xml;charset=UTF-8";
                        //接收到为xml报文
                        responseBody = HttpRestTemplate.httpClient(url,soap,contentType);

                        MessageLog messageLog = new MessageLog();
                        messageLog.setMsgId(NHISUUID.getKeyId());
                        messageLog.setTransType("send");
                        messageLog.setSysCode("NHIS");
                        messageLog.setMsgType("wbSendStRisInfo");
                        messageLog.setMsgContent(soap);
                        messageLog.setErrTxt(responseBody);

                        if(CommonUtils.isEmptyString(responseBody)){
                            //失败
                            messageLog.setMsgStatus("ERROR");
                        }else{
                            //成功
                            messageLog.setMsgStatus("AA");
                        }
                        //写入日志
//                        ServiceLocator.getInstance().getBean(PskqPlatFormSendBlHandler.class).addMsg(messageLog);
                    }
                }
            }catch (Exception e){
                logger.info("发送检查申请至牙医管家失败,结果:{}",e.getMessage());
            }
        }
    }
    /**
     * 发送住院检验退费信息
     * @param paramMap
     * @param listener
     */
    @SuppressWarnings("unchecked")
    public void getIpLisCanCostInfo(Map<String, Object> paramMap, ResultListener listener){

        String result = "";
        String responseBody="";
        try {
            if(paramMap== null || paramMap.get("exlist")==null){
                listener.error("未获取到取消执行参数！","","");
                return;
            }
            List<Map<String,Object>> l=(List<Map<String,Object>>) paramMap.get("exlist");
            for (Map<String,Object> cnMap : l) {
         	   String pkcnord=MapUtils.getString(cnMap,"pkCnord","");
         	   CnOrder cnOrder = CnOrderRepository.getOne(pkcnord);
         	   if ("03".equals(cnOrder.getCodeOrdtype().substring(0, 2))){
	         	   Map<String,Object> m=new HashMap<String, Object>();
	         	   m.put("pkCnord", pkcnord);
	         	   m.put("Control", "CR");
	         	   List<CostDetailInpat> listCode = pskqPlatFormSendCnMapper.queryIpCostDetailInfo(m);
	         	   for (CostDetailInpat costDetailInpat : listCode) {
	         		   if(costDetailInpat==null){
	                        listener.error("未获取到医嘱信息！","","");
	                        return;
	                   }
	             	   costDetailInpat.setOrgCode("10");
	             	   costDetailInpat.setOrgName("南方医科大学深圳口腔医院（坪山）");
	             	   costDetailInpat.setOrderId("10_"+costDetailInpat.getEncounterTypeCode()+"_"+costDetailInpat.getOrderId());
	             	   costDetailInpat.setEncounterId(PskqMesUtils.pskqHisCode+costDetailInpat.getEncounterTypeCode()+"_"+PskqMesUtils.pskqSysCode+costDetailInpat.getEncounterId());
	             	   costDetailInpat.setOrderId(PskqMesUtils.pskqSysCode+costDetailInpat.getEncounterTypeCode()+"_"+PskqMesUtils.pskqHisCode+costDetailInpat.getOrderId());
	             	   costDetailInpat.setChargeStatus("2");
	         	   }
	         	  CostDetailInpatList costDetailInpatList=new CostDetailInpatList();
	        	  costDetailInpatList.setCostDetailInpats(listCode);
	         	  MessageFactory messageFactory = new MessageFactory();
                  Message message = messageFactory.getInstance(costDetailInpatList);
                  Map<String, Object> map=new HashMap<String, Object>();
                  map.put("eventCode", "E150304");map.put("eventName", "住院退费");
                  RequestBody requestBody = message.getRequestBody(map);
                  result =  JSON.toJSONString(requestBody);
                  //发送消息
                  responseBody = httpRestTemplate.postForString(result);
                  ResponseBody paramVo = JsonUtil.readValue(responseBody, ResponseBody.class);
                  //消息成功
                  if(paramVo !=null && paramVo.getAck()!=null && "AA".equals(paramVo.getAck().get("ackCode"))){
                      listener.success(result,responseBody);
                  }else {
                      listener.error(paramVo.getAck().get("ackDetail").toString(),result,responseBody);
                  }
            	}
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
    public void saveBlExtPay(String serialNo,String pkDepo){
    	DefaultTransactionDefinition transDefinition = new DefaultTransactionDefinition();
        transDefinition.setPropagationBehavior(DefaultTransactionDefinition.PROPAGATION_REQUIRES_NEW);
        DataSourceTransactionManager dataSourceTransactionManager = SpringContextHolder.getApplicationContext().getBean(DataSourceTransactionManager.class);
        TransactionStatus transStatus = dataSourceTransactionManager.getTransaction(transDefinition);
        try {
        	BlExtPay blExtPay = DataBaseHelper.queryForBean("select * from BL_EXT_PAY where serial_no = ? ", BlExtPay.class,serialNo);

        	BlDeposit blDeposit = DataBaseHelper.queryForBean("select * from bl_deposit where pk_depo = ? ", BlDeposit.class,pkDepo);
            String pkSettle = "";//Bl_Settle主键
            if(blDeposit != null) {
            	pkSettle = blDeposit.getPkSettle();
            }
            if(blExtPay != null) {
                BlExtPay newblExtPay = new BlExtPay();
                ApplicationUtils.copyProperties(newblExtPay, blExtPay);
                newblExtPay.setPkExtpay(NHISUUID.getKeyId());
                newblExtPay.setAmount(blExtPay.getAmount().negate());
                newblExtPay.setPkDepo(pkDepo);
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




    @Transactional(propagation= Propagation.REQUIRES_NEW)
    public void addMsg(MessageLog messageLog){
        SysMsgRec rec = new SysMsgRec();
        rec.setTransType(messageLog.getTransType());
        rec.setMsgType(messageLog.getMsgType());
        rec.setMsgId(messageLog.getMsgId());
        rec.setTransDate(new Date());
        rec.setMsgContent(messageLog.getMsgContent());
        rec.setSysCode(messageLog.getSysCode());
        rec.setMsgStatus(messageLog.getMsgStatus());
        DataBaseHelper.insertBean(rec);
    }

    @Override
    public void afterPropertiesSet() throws Exception {

    }

}





