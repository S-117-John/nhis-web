package com.zebone.nhis.ma.pub.platform.pskq.service;


import com.alibaba.fastjson.JSON;
import com.zebone.nhis.common.module.base.bd.mk.BdSupply;
import com.zebone.nhis.common.module.cn.ipdw.CnOrder;
import com.zebone.nhis.common.module.pi.PiMaster;
import com.zebone.nhis.common.module.pv.PvEncounter;
import com.zebone.nhis.common.support.ApplicationUtils;
import com.zebone.nhis.common.support.CommonUtils;
import com.zebone.nhis.ma.pub.platform.pskq.factory.Message;
import com.zebone.nhis.ma.pub.platform.pskq.factory.MessageFactory;
import com.zebone.nhis.ma.pub.platform.pskq.model.OrderExecRecord;
import com.zebone.nhis.ma.pub.platform.pskq.model.OrderInpat;
import com.zebone.nhis.ma.pub.platform.pskq.model.listener.ResultListener;


import com.zebone.nhis.ma.pub.platform.pskq.model.message.DataElement;
import com.zebone.nhis.ma.pub.platform.pskq.model.message.OrganizationElement;
import com.zebone.nhis.ma.pub.platform.pskq.model.message.ReceiverElement;
import com.zebone.nhis.ma.pub.platform.pskq.model.message.RequestBody;
import com.zebone.nhis.ma.pub.platform.pskq.model.message.ResponseBody;
import com.zebone.nhis.ma.pub.platform.pskq.model.message.SenderElement;
import com.zebone.nhis.ma.pub.platform.pskq.model.message.SoftwareNameElement;
import com.zebone.nhis.ma.pub.platform.pskq.model.message.SoftwareProviderElement;
import com.zebone.nhis.ma.pub.platform.pskq.model.param.OrderParam;
import com.zebone.nhis.ma.pub.platform.pskq.repository.CnOrderRepository;
import com.zebone.nhis.ma.pub.platform.pskq.repository.PiMasterRepository;
import com.zebone.nhis.ma.pub.platform.pskq.repository.PvEncounterRepository;
import com.zebone.nhis.ma.pub.platform.pskq.utils.MapUtils;
import com.zebone.nhis.ma.pub.platform.pskq.utils.MessageBodyUtil;
import com.zebone.nhis.ma.pub.platform.pskq.utils.PskqDictionMapUtil;
import com.zebone.nhis.ma.pub.platform.pskq.utils.PskqMesUtils;
import com.zebone.nhis.ma.pub.platform.pskq.utils.RestTemplateUtil;
import com.zebone.nhis.ma.pub.platform.pskq.web.HttpRestTemplate;
import com.zebone.nhis.ma.pub.platform.send.impl.pskq.dao.PskqPlatFormSendCnMapper;
import com.zebone.nhis.webservice.pskq.config.SpringContextHolder;
import com.zebone.platform.common.support.UserContext;
import com.zebone.platform.modules.dao.DataBaseHelper;
import com.zebone.platform.modules.exception.BusException;
import com.zebone.platform.modules.utils.JsonUtil;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class OrderInpatService {

	@Autowired
    private PskqPlatFormSendCnMapper pskqPlatFormSendCnMapper;

    /**
     * 取消签署
     */
	public void cancel(List<Map<String,Object>> orderList){
        try{
            HttpRestTemplate httpRestTemplate = SpringContextHolder.getApplicationContext().getBean(HttpRestTemplate.class);
            if(httpRestTemplate==null){
                throw new BusException("没有获取到HttpRestTemplate对象");
            }
            if(orderList.size()==0){
                throw new BusException("没有传入需要核对的医嘱信息");
            }
            SenderElement senderElement = new SenderElement(
                    "2000",
                    new SoftwareNameElement("HIS","医院信息管理系统"),
                    new SoftwareProviderElement("Zebone","江苏振邦智慧城市信息系统有限公司"),
                    new OrganizationElement("10","南方医科大学深圳口腔医院（坪山）")
            );
            ReceiverElement receiverElement = new ReceiverElement(
                    "1200",
                    new SoftwareNameElement("ESB","集成平台"),
                    new SoftwareProviderElement("Caradigm","恺恩泰（北京）科技有限公司"),
                    new OrganizationElement("10","南方医科大学深圳口腔医院（坪山）")
            );
            List<String> pkCnords = new ArrayList<>();
            List<OrderExecRecord> orderExecRecords = new ArrayList<>();
            for(int i=0;i<orderList.size();i++){
                CnOrder cnOrder = CnOrderRepository.findByOrdsn(MapUtils.getInteger(orderList.get(i),"ordsn",null));
                pkCnords.add(cnOrder.getPkCnord());
            }
            orderExecRecords = pskqPlatFormSendCnMapper.queryIpCnorderProject(pkCnords);
            orderExecRecords.addAll(pskqPlatFormSendCnMapper.queryIpCnorderDrugs(pkCnords));
            for (OrderExecRecord orderExecRecord : orderExecRecords) {
                String pkCnord = orderExecRecord.getOrderId();
                String sql = "select * from CN_ORDER where PK_CNORD = ?";
                CnOrder cnOrder = DataBaseHelper.queryForBean(sql,CnOrder.class,pkCnord);
                String supplyCode = cnOrder.getCodeSupply();
                sql = "select * from BD_SUPPLY where CODE = ?";
                if(!StringUtils.isEmpty(supplyCode)){
                    BdSupply bdSupply = DataBaseHelper.queryForBean(sql,BdSupply.class,supplyCode);
                    //医嘱类型转换
                    orderExecRecord.setOrderCardCode(bdSupply.getDtExcardtype());
                    switch (bdSupply.getDtExcardtype()){
                        case "1":
                            orderExecRecord.setOrderCardName("护理卡");
                            break;
                        case "2":
                            orderExecRecord.setOrderCardName("口服卡");
                            break;
                        case "3":
                            orderExecRecord.setOrderCardName("注射卡");
                            break;
                        case "4":
                            orderExecRecord.setOrderCardName("输液卡");
                            break;
                        case "0402":
                            orderExecRecord.setOrderCardName("输液瓶贴");
                            break;
                        case "0404":
                            orderExecRecord.setOrderCardName("执行单");
                            break;
                        case "5":
                            orderExecRecord.setOrderCardName("饮食卡");
                            break;
                        case "06":
                            orderExecRecord.setOrderCardName("检验");
                            break;
                        case "07":
                            orderExecRecord.setOrderCardName("检查");
                            break;
                        case "08":
                            orderExecRecord.setOrderCardName("检验");
                            break;
                        case "99":
                            orderExecRecord.setOrderCardName("其它卡");
                            break;
                    }

                }

                //医嘱类型转换
                orderExecRecord.setOrderCategoryCode(PskqDictionMapUtil.hisToPingOrdtypeCodeMap(orderExecRecord.getOrderCategoryCode()));
                orderExecRecord.setOrderCategoryName(PskqDictionMapUtil.hisToPingOrdtypeNameMap(orderExecRecord.getOrderCategoryCode()));

                orderExecRecord.setEncounterId(PskqMesUtils.pskqHisCode+orderExecRecord.getEncounterTypeCode()+"_"+PskqMesUtils.pskqSysCode+orderExecRecord.getEncounterId());
                orderExecRecord.setVisitId(PskqMesUtils.pskqHisCode+orderExecRecord.getEncounterTypeCode()+"_"+PskqMesUtils.pskqSysCode+orderExecRecord.getVisitId());


                //剂型转换
                Map<String, Object> dosageFormMap = PskqDictionMapUtil.hisToPingDosageFormMap(orderExecRecord.getDrugFormCode());
                orderExecRecord.setDrugFormCode(CommonUtils.getPropValueStr(dosageFormMap, "code"));
                orderExecRecord.setDrugFormName(CommonUtils.getPropValueStr(dosageFormMap, "name"));
                orderExecRecord.setOrderId(PskqMesUtils.pskqSysCode+orderExecRecord.getEncounterTypeCode()+"_"+PskqMesUtils.pskqHisCode+orderExecRecord.getOrderId());
                orderExecRecord.setOrgCode("10");
                orderExecRecord.setOrgName("南方医科大学深圳口腔医院（坪山）");
                OrderInpat orderInpat = new OrderInpat();
                ApplicationUtils.copyProperties(orderInpat, orderExecRecord);
                RequestBody requestBody = new RequestBody.Builder()
                        .service("S0036","医嘱信息更新服务")
                        .event("E003604","取消确认住院医嘱")
                        .sender()
                        .receiver()
                        .build();
                Map<String,Object> map = new HashMap<>(16);
                List<DataElement> dataElementList = MessageBodyUtil.dataElementsFactory(orderInpat);
                map.put("ORDER_INPAT",dataElementList);
                requestBody.setMessage(map);
                requestBody.setSender(senderElement);
                requestBody.setReceiver(receiverElement);
                String requestString = JSON.toJSONString(requestBody);
                String responseString = httpRestTemplate.postForString(requestString);
                ResponseBody responseBody = JsonUtil.readValue(responseString,ResponseBody.class);
                if (responseBody != null && responseBody.getAck() != null && "AA".equals(responseBody.getAck().get("ackCode"))) {

                } else {
//                    throw new BusException(responseBody.getAck().get("ackDetail").toString());
                }
            }
        }catch (Exception e){
//            throw new BusException("发送取消住院医嘱信息失败");
        }
    }


	public void creat(List<Map<String,Object>> orderList, ResultListener listener){
        try{
            HttpRestTemplate httpRestTemplate = SpringContextHolder.getApplicationContext().getBean(HttpRestTemplate.class);
            if(httpRestTemplate==null){
                listener.error("没有获取到HttpRestTemplate对象","","");
                return;
            }
            if(orderList.size()==0){
                listener.error("没有传入需要核对的医嘱信息","","");
                return;
            }
            SenderElement senderElement = new SenderElement(
                    "2000",
                    new SoftwareNameElement("HIS","医院信息管理系统"),
                    new SoftwareProviderElement("Zebone","江苏振邦智慧城市信息系统有限公司"),
                    new OrganizationElement("10","南方医科大学深圳口腔医院（坪山）")
            );
            ReceiverElement receiverElement = new ReceiverElement(
                    "1200",
                    new SoftwareNameElement("ESB","集成平台"),
                    new SoftwareProviderElement("Caradigm","恺恩泰（北京）科技有限公司"),
                    new OrganizationElement("10","南方医科大学深圳口腔医院（坪山）")
            );
            List<String> pkCnords = new ArrayList<>();
            List<OrderExecRecord> orderExecRecords = new ArrayList<>();
            for(int i=0;i<orderList.size();i++){
				CnOrder cnOrder = CnOrderRepository.findByOrdsn(MapUtils.getInteger(orderList.get(i),"ordsn",null));
                pkCnords.add(cnOrder.getPkCnord());
            }
            orderExecRecords = pskqPlatFormSendCnMapper.queryIpCnorderProject(pkCnords);
            orderExecRecords.addAll(pskqPlatFormSendCnMapper.queryIpCnorderDrugs(pkCnords));
            for (OrderExecRecord orderExecRecord : orderExecRecords) {
                String pkCnord = orderExecRecord.getOrderId();
                String sql = "select * from CN_ORDER where PK_CNORD = ?";
                CnOrder cnOrder = DataBaseHelper.queryForBean(sql,CnOrder.class,pkCnord);
                String supplyCode = cnOrder.getCodeSupply();
                sql = "select * from BD_SUPPLY where CODE = ?";
                if(!StringUtils.isEmpty(supplyCode)){
                    BdSupply bdSupply = DataBaseHelper.queryForBean(sql,BdSupply.class,supplyCode);
                    //医嘱类型转换
                    orderExecRecord.setOrderCardCode(bdSupply.getDtExcardtype());
                    switch (bdSupply.getDtExcardtype()){
                        case "1":
                            orderExecRecord.setOrderCardName("护理卡");
                            break;
                        case "2":
                            orderExecRecord.setOrderCardName("口服卡");
                            break;
                        case "3":
                            orderExecRecord.setOrderCardName("注射卡");
                            break;
                        case "4":
                            orderExecRecord.setOrderCardName("输液卡");
                            break;
                        case "0402":
                            orderExecRecord.setOrderCardName("输液瓶贴");
                            break;
                        case "0404":
                            orderExecRecord.setOrderCardName("执行单");
                            break;
                        case "5":
                            orderExecRecord.setOrderCardName("饮食卡");
                            break;
                        case "06":
                            orderExecRecord.setOrderCardName("检验");
                            break;
                        case "07":
                            orderExecRecord.setOrderCardName("检查");
                            break;
                        case "08":
                            orderExecRecord.setOrderCardName("检验");
                            break;
                        case "99":
                            orderExecRecord.setOrderCardName("其它卡");
                            break;
                    }

                }

                //医嘱类型转换
                orderExecRecord.setOrderCategoryCode(PskqDictionMapUtil.hisToPingOrdtypeCodeMap(orderExecRecord.getOrderCategoryCode()));
                orderExecRecord.setOrderCategoryName(PskqDictionMapUtil.hisToPingOrdtypeNameMap(orderExecRecord.getOrderCategoryCode()));

            	orderExecRecord.setEncounterId(PskqMesUtils.pskqHisCode+orderExecRecord.getEncounterTypeCode()+"_"+PskqMesUtils.pskqSysCode+orderExecRecord.getEncounterId());
                orderExecRecord.setVisitId(PskqMesUtils.pskqHisCode+orderExecRecord.getEncounterTypeCode()+"_"+PskqMesUtils.pskqSysCode+orderExecRecord.getVisitId());

                
                //剂型转换
                Map<String, Object> dosageFormMap = PskqDictionMapUtil.hisToPingDosageFormMap(orderExecRecord.getDrugFormCode());
                orderExecRecord.setDrugFormCode(CommonUtils.getPropValueStr(dosageFormMap, "code"));
                orderExecRecord.setDrugFormName(CommonUtils.getPropValueStr(dosageFormMap, "name"));
                orderExecRecord.setOrderId(PskqMesUtils.pskqSysCode+orderExecRecord.getEncounterTypeCode()+"_"+PskqMesUtils.pskqHisCode+orderExecRecord.getOrderId());
                orderExecRecord.setOrgCode("10");
                orderExecRecord.setOrgName("南方医科大学深圳口腔医院（坪山）");
                OrderInpat orderInpat = new OrderInpat();
            	ApplicationUtils.copyProperties(orderInpat, orderExecRecord);
            	RequestBody requestBody = new RequestBody.Builder()
		                .service("S0035","医嘱信息新增服务")
		                .event("E003502","开立住院医嘱")
		                .sender()
		                .receiver()
		                .build();
		        Map<String,Object> map = new HashMap<>(16);
		        List<DataElement> dataElementList = MessageBodyUtil.dataElementsFactory(orderInpat);
		        map.put("ORDER_INPAT",dataElementList);
		        requestBody.setMessage(map);
		        requestBody.setSender(senderElement);
		        requestBody.setReceiver(receiverElement);
		        String requestString = JSON.toJSONString(requestBody);
		        String responseString = httpRestTemplate.postForString(requestString);
		        ResponseBody responseBody = JsonUtil.readValue(responseString,ResponseBody.class);
                if (responseBody != null && responseBody.getAck() != null && "AA".equals(responseBody.getAck().get("ackCode"))) {
                    listener.success(responseString, JSON.toJSONString(responseBody));
                } else {
                    listener.error(responseBody.getAck().get("ackDetail").toString(), responseString,JSON.toJSONString(responseBody));
                }
            }
        }catch (Exception e){
            listener.error("发送开立住院医嘱信息失败","","");
        }
    }



    /**
     * 发送医嘱消息
     * @param
     * @param listener
     */
    public void sendOrderInpat(OrderParam list, ResultListener listener){
        try{
            String cancel = list.getFlagCancleStop();
            String state = "";
            if(list.getNewOrdList().size()>0){
                state = "ADD";
            }
            if("1".equals(cancel)){
                state = "CANCEL";
            }

            if("1".equals(list.getStop())){
                state = "STOP";
            }

            StrategyFactory.creator(state).doOperate(list,listener);
        }catch (Exception e){
            listener.exception(e.getMessage(),"","");
        }

    }

    public interface Strategy {

        void doOperate(OrderParam list,ResultListener listener);


    }

    public static class StopStrategy implements Strategy{


//        @Autowired
//        private HttpRestTemplate httpRestTemplate;
//        @Autowired
//        private PskqPlatFormSendCnMapper pskqPlatFormSendCnMapper;
        @Override
        public void doOperate(OrderParam list, ResultListener listener) {
            HttpRestTemplate httpRestTemplate = SpringContextHolder.getApplicationContext().getBean(HttpRestTemplate.class);
            PskqPlatFormSendCnMapper pskqPlatFormSendCnMapper = SpringContextHolder.getApplicationContext().getBean(PskqPlatFormSendCnMapper.class);
            try{
            	List<CnOrder> orderList = list.getChangeOrdList();
                List<String> pkCnords = new ArrayList<>();
                List<OrderExecRecord> orderExecRecords = new ArrayList<>();
                for(CnOrder cnOrder : orderList){
                	Integer ordsn = cnOrder.getOrdsn();
                    cnOrder = CnOrderRepository.findByOrdsn(ordsn);
                    pkCnords.add(cnOrder.getPkCnord());
                }
                orderExecRecords = pskqPlatFormSendCnMapper.queryIpCnorderProject(pkCnords);
                orderExecRecords.addAll(pskqPlatFormSendCnMapper.queryIpCnorderDrugs(pkCnords));
                for (OrderExecRecord orderExecRecord : orderExecRecords) {
                	orderExecRecord.setEncounterId(PskqMesUtils.pskqHisCode+orderExecRecord.getEncounterTypeCode()+"_"+PskqMesUtils.pskqSysCode+orderExecRecord.getEncounterId());
                    orderExecRecord.setVisitId(PskqMesUtils.pskqHisCode+orderExecRecord.getEncounterTypeCode()+"_"+PskqMesUtils.pskqSysCode+orderExecRecord.getVisitId());
                    //医嘱类型转换
                    orderExecRecord.setOrderCategoryCode(PskqDictionMapUtil.hisToPingOrdtypeCodeMap(orderExecRecord.getOrderCategoryCode()));
                    orderExecRecord.setOrderCategoryName(PskqDictionMapUtil.hisToPingOrdtypeNameMap(orderExecRecord.getOrderCategoryCode()));
                    
                    //剂型转换
                    Map<String, Object> dosageFormMap = PskqDictionMapUtil.hisToPingDosageFormMap(orderExecRecord.getDrugFormCode());
                    orderExecRecord.setDrugFormCode(CommonUtils.getPropValueStr(dosageFormMap, "code"));
                    orderExecRecord.setDrugFormName(CommonUtils.getPropValueStr(dosageFormMap, "name"));
                    orderExecRecord.setOrderId(PskqMesUtils.pskqSysCode+orderExecRecord.getEncounterTypeCode()+"_"+PskqMesUtils.pskqHisCode+orderExecRecord.getOrderId());
                    orderExecRecord.setOrgCode("10");
                    orderExecRecord.setOrgName("南方医科大学深圳口腔医院（坪山）");
                	OrderInpat orderInpat = new OrderInpat();
                	ApplicationUtils.copyProperties(orderInpat, orderExecRecord);
                	
                	MessageFactory messageFactory = new MessageFactory();
                    Message message = messageFactory.getInstance(orderInpat);
                    Map<String,Object> map=new HashMap<>();
                    map.put("serviceCode","S0036");
                    map.put("serviceName","医嘱信息更新服务");
                    map.put("eventCode","E003605");
                    map.put("eventName","停止住院医嘱");
                    RequestBody requestBody = message.getRequestBody(map);
                    String requestBodyString = JSON.toJSONString(requestBody);
                    String responseBodyString = httpRestTemplate.postForString(requestBodyString);
                    ResponseBody responseBody = JsonUtil.readValue(responseBodyString,ResponseBody.class);
                    if (responseBody != null && responseBody.getAck() != null && "AA".equals(responseBody.getAck().get("ackCode"))) {
                        listener.success(requestBodyString, JSON.toJSONString(responseBody));
                    } else {
                        listener.error(responseBody.getAck().get("ackDetail").toString(), requestBodyString,JSON.toJSONString(responseBody));
                    }
				}
            }catch (Exception e){
                listener.exception(e.getMessage(),"","");
            }
        }
    }

    public static class CancelStrategy implements Strategy{

//        @Autowired
//        private HttpRestTemplate httpRestTemplate;
//        @Autowired
//        private PskqPlatFormSendCnMapper pskqPlatFormSendCnMapper;
        @Override
        public void doOperate(OrderParam list, ResultListener listener) {
            HttpRestTemplate httpRestTemplate = SpringContextHolder.getApplicationContext().getBean(HttpRestTemplate.class);
            PskqPlatFormSendCnMapper pskqPlatFormSendCnMapper = SpringContextHolder.getApplicationContext().getBean(PskqPlatFormSendCnMapper.class);
            try{
            	List<CnOrder> orderList = list.getChangeOrdList();
                List<String> pkCnords = new ArrayList<>();
                List<OrderExecRecord> orderExecRecords = new ArrayList<>();
                for(CnOrder cnOrder : orderList){
                	Integer ordsn = cnOrder.getOrdsn();
                    cnOrder = CnOrderRepository.findByOrdsn(ordsn);
                    pkCnords.add(cnOrder.getPkCnord());
                }
                orderExecRecords = pskqPlatFormSendCnMapper.queryIpCnorderProject(pkCnords);
                orderExecRecords.addAll(pskqPlatFormSendCnMapper.queryIpCnorderDrugs(pkCnords));
                for (OrderExecRecord orderExecRecord : orderExecRecords) {
                	orderExecRecord.setEncounterId(PskqMesUtils.pskqHisCode+orderExecRecord.getEncounterTypeCode()+"_"+PskqMesUtils.pskqSysCode+orderExecRecord.getEncounterId());
                    orderExecRecord.setVisitId(PskqMesUtils.pskqHisCode+orderExecRecord.getEncounterTypeCode()+"_"+PskqMesUtils.pskqSysCode+orderExecRecord.getVisitId());
                    //医嘱类型转换
                    orderExecRecord.setOrderCategoryCode(PskqDictionMapUtil.hisToPingOrdtypeCodeMap(orderExecRecord.getOrderCategoryCode()));
                    orderExecRecord.setOrderCategoryName(PskqDictionMapUtil.hisToPingOrdtypeNameMap(orderExecRecord.getOrderCategoryCode()));
                    
                    //剂型转换
                    Map<String, Object> dosageFormMap = PskqDictionMapUtil.hisToPingDosageFormMap(orderExecRecord.getDrugFormCode());
                    orderExecRecord.setDrugFormCode(CommonUtils.getPropValueStr(dosageFormMap, "code"));
                    orderExecRecord.setDrugFormName(CommonUtils.getPropValueStr(dosageFormMap, "name"));
                    orderExecRecord.setOrderId(PskqMesUtils.pskqSysCode+orderExecRecord.getEncounterTypeCode()+"_"+PskqMesUtils.pskqHisCode+orderExecRecord.getOrderId());
                    orderExecRecord.setOrgCode("10");
                    orderExecRecord.setOrgName("南方医科大学深圳口腔医院（坪山）");
                	OrderInpat orderInpat = new OrderInpat();
                	ApplicationUtils.copyProperties(orderInpat, orderExecRecord);
                	
                	MessageFactory messageFactory = new MessageFactory();
                    Message message = messageFactory.getInstance(orderInpat);
                    Map<String,Object> map=new HashMap<>();
                    map.put("serviceCode","S0036");
                    map.put("serviceName","医嘱信息更新服务");
                    map.put("eventCode","E003604");
                    map.put("eventName","取消确认住院医嘱");
                    RequestBody requestBody = message.getRequestBody(map);
                    String requestBodyString = JSON.toJSONString(requestBody);
                    String responseBodyString = httpRestTemplate.postForString(requestBodyString);
                    ResponseBody responseBody = JsonUtil.readValue(responseBodyString,ResponseBody.class);
                    if (responseBody != null && responseBody.getAck() != null && "AA".equals(responseBody.getAck().get("ackCode"))) {
                        listener.success(requestBodyString, JSON.toJSONString(responseBody));
                    } else {
                        listener.error(responseBody.getAck().get("ackDetail").toString(), requestBodyString,JSON.toJSONString(responseBody));
                    }
				}
            }catch (Exception e){
                listener.exception(e.getMessage(),"","");
            }
        }
    }

    public static class AddStateStrategy implements Strategy{
//
//        @Autowired
//        private HttpRestTemplate httpRestTemplate;
//        @Autowired
//        private PskqPlatFormSendCnMapper pskqPlatFormSendCnMapper;
        @Override
        public void doOperate(OrderParam list,ResultListener listener) {
            HttpRestTemplate httpRestTemplate = SpringContextHolder.getApplicationContext().getBean(HttpRestTemplate.class);
            PskqPlatFormSendCnMapper pskqPlatFormSendCnMapper = SpringContextHolder.getApplicationContext().getBean(PskqPlatFormSendCnMapper.class);
            try{
            	List<CnOrder> orderList = list.getChangeOrdList();
                List<String> pkCnords = new ArrayList<>();
                List<OrderExecRecord> orderExecRecords = new ArrayList<>();
                for(CnOrder cnOrder : orderList){
                	Integer ordsn = cnOrder.getOrdsn();
                    cnOrder = CnOrderRepository.findByOrdsn(ordsn);
                    pkCnords.add(cnOrder.getPkCnord());
                }
                orderExecRecords = pskqPlatFormSendCnMapper.queryIpCnorderProject(pkCnords);
                orderExecRecords.addAll(pskqPlatFormSendCnMapper.queryIpCnorderDrugs(pkCnords));
                for (OrderExecRecord orderExecRecord : orderExecRecords) {
                	orderExecRecord.setEncounterId(PskqMesUtils.pskqHisCode+orderExecRecord.getEncounterTypeCode()+"_"+PskqMesUtils.pskqSysCode+orderExecRecord.getEncounterId());
                    orderExecRecord.setVisitId(PskqMesUtils.pskqHisCode+orderExecRecord.getEncounterTypeCode()+"_"+PskqMesUtils.pskqSysCode+orderExecRecord.getVisitId());
                    //医嘱类型转换
                    orderExecRecord.setOrderCategoryCode(PskqDictionMapUtil.hisToPingOrdtypeCodeMap(orderExecRecord.getOrderCategoryCode()));
                    orderExecRecord.setOrderCategoryName(PskqDictionMapUtil.hisToPingOrdtypeNameMap(orderExecRecord.getOrderCategoryCode()));
                    
                    //剂型转换
                    Map<String, Object> dosageFormMap = PskqDictionMapUtil.hisToPingDosageFormMap(orderExecRecord.getDrugFormCode());
                    orderExecRecord.setDrugFormCode(CommonUtils.getPropValueStr(dosageFormMap, "code"));
                    orderExecRecord.setDrugFormName(CommonUtils.getPropValueStr(dosageFormMap, "name"));
                    orderExecRecord.setOrderId(PskqMesUtils.pskqSysCode+orderExecRecord.getEncounterTypeCode()+"_"+PskqMesUtils.pskqHisCode+orderExecRecord.getOrderId());
                    orderExecRecord.setOrgCode("10");
                    orderExecRecord.setOrgName("南方医科大学深圳口腔医院（坪山）");
                	OrderInpat orderInpat = new OrderInpat();
                	ApplicationUtils.copyProperties(orderInpat, orderExecRecord);
                	
                	MessageFactory messageFactory = new MessageFactory();
                    Message message = messageFactory.getInstance(orderInpat);
                    Map<String,Object> map=new HashMap<>();
                    map.put("serviceCode","S0035");
                    map.put("serviceName","医嘱信息新增服务");
                    map.put("eventCode","E003502");
                    map.put("eventName","开立住院医嘱");
                    RequestBody requestBody = message.getRequestBody(map);
                    String requestBodyString = JSON.toJSONString(requestBody);
                    String responseBodyString = httpRestTemplate.postForString(requestBodyString);
                    ResponseBody responseBody = JsonUtil.readValue(responseBodyString,ResponseBody.class);
                    if (responseBody != null && responseBody.getAck() != null && "AA".equals(responseBody.getAck().get("ackCode"))) {
                        listener.success(requestBodyString, JSON.toJSONString(responseBody));
                    } else {
                        listener.error(responseBody.getAck().get("ackDetail").toString(), requestBodyString,JSON.toJSONString(responseBody));
                    }
				}
            }catch (Exception e){
                listener.exception(e.getMessage(),"","");
            }
        }
    }

    public static class StrategyFactory {
        public static final String ADD_STATE = "ADD";
        public static final String CANCEL_STATE = "CANCEL";
        public static final String STOP_STATE = "STOP";

        private static Map<String, Strategy> strategyMap = new HashMap<>();

        static {
            strategyMap.put(ADD_STATE, new AddStateStrategy());
            strategyMap.put(CANCEL_STATE, new CancelStrategy());
            strategyMap.put(STOP_STATE, new StopStrategy());
        }

        public static Strategy creator(String event) {
            return strategyMap.get(event);
        }


    }
}
