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
     * ????????????
     */
	public void cancel(List<Map<String,Object>> orderList){
        try{
            HttpRestTemplate httpRestTemplate = SpringContextHolder.getApplicationContext().getBean(HttpRestTemplate.class);
            if(httpRestTemplate==null){
                throw new BusException("???????????????HttpRestTemplate??????");
            }
            if(orderList.size()==0){
                throw new BusException("???????????????????????????????????????");
            }
            SenderElement senderElement = new SenderElement(
                    "2000",
                    new SoftwareNameElement("HIS","????????????????????????"),
                    new SoftwareProviderElement("Zebone","????????????????????????????????????????????????"),
                    new OrganizationElement("10","????????????????????????????????????????????????")
            );
            ReceiverElement receiverElement = new ReceiverElement(
                    "1200",
                    new SoftwareNameElement("ESB","????????????"),
                    new SoftwareProviderElement("Caradigm","???????????????????????????????????????"),
                    new OrganizationElement("10","????????????????????????????????????????????????")
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
                    //??????????????????
                    orderExecRecord.setOrderCardCode(bdSupply.getDtExcardtype());
                    switch (bdSupply.getDtExcardtype()){
                        case "1":
                            orderExecRecord.setOrderCardName("?????????");
                            break;
                        case "2":
                            orderExecRecord.setOrderCardName("?????????");
                            break;
                        case "3":
                            orderExecRecord.setOrderCardName("?????????");
                            break;
                        case "4":
                            orderExecRecord.setOrderCardName("?????????");
                            break;
                        case "0402":
                            orderExecRecord.setOrderCardName("????????????");
                            break;
                        case "0404":
                            orderExecRecord.setOrderCardName("?????????");
                            break;
                        case "5":
                            orderExecRecord.setOrderCardName("?????????");
                            break;
                        case "06":
                            orderExecRecord.setOrderCardName("??????");
                            break;
                        case "07":
                            orderExecRecord.setOrderCardName("??????");
                            break;
                        case "08":
                            orderExecRecord.setOrderCardName("??????");
                            break;
                        case "99":
                            orderExecRecord.setOrderCardName("?????????");
                            break;
                    }

                }

                //??????????????????
                orderExecRecord.setOrderCategoryCode(PskqDictionMapUtil.hisToPingOrdtypeCodeMap(orderExecRecord.getOrderCategoryCode()));
                orderExecRecord.setOrderCategoryName(PskqDictionMapUtil.hisToPingOrdtypeNameMap(orderExecRecord.getOrderCategoryCode()));

                orderExecRecord.setEncounterId(PskqMesUtils.pskqHisCode+orderExecRecord.getEncounterTypeCode()+"_"+PskqMesUtils.pskqSysCode+orderExecRecord.getEncounterId());
                orderExecRecord.setVisitId(PskqMesUtils.pskqHisCode+orderExecRecord.getEncounterTypeCode()+"_"+PskqMesUtils.pskqSysCode+orderExecRecord.getVisitId());


                //????????????
                Map<String, Object> dosageFormMap = PskqDictionMapUtil.hisToPingDosageFormMap(orderExecRecord.getDrugFormCode());
                orderExecRecord.setDrugFormCode(CommonUtils.getPropValueStr(dosageFormMap, "code"));
                orderExecRecord.setDrugFormName(CommonUtils.getPropValueStr(dosageFormMap, "name"));
                orderExecRecord.setOrderId(PskqMesUtils.pskqSysCode+orderExecRecord.getEncounterTypeCode()+"_"+PskqMesUtils.pskqHisCode+orderExecRecord.getOrderId());
                orderExecRecord.setOrgCode("10");
                orderExecRecord.setOrgName("????????????????????????????????????????????????");
                OrderInpat orderInpat = new OrderInpat();
                ApplicationUtils.copyProperties(orderInpat, orderExecRecord);
                RequestBody requestBody = new RequestBody.Builder()
                        .service("S0036","????????????????????????")
                        .event("E003604","????????????????????????")
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
//            throw new BusException("????????????????????????????????????");
        }
    }


	public void creat(List<Map<String,Object>> orderList, ResultListener listener){
        try{
            HttpRestTemplate httpRestTemplate = SpringContextHolder.getApplicationContext().getBean(HttpRestTemplate.class);
            if(httpRestTemplate==null){
                listener.error("???????????????HttpRestTemplate??????","","");
                return;
            }
            if(orderList.size()==0){
                listener.error("???????????????????????????????????????","","");
                return;
            }
            SenderElement senderElement = new SenderElement(
                    "2000",
                    new SoftwareNameElement("HIS","????????????????????????"),
                    new SoftwareProviderElement("Zebone","????????????????????????????????????????????????"),
                    new OrganizationElement("10","????????????????????????????????????????????????")
            );
            ReceiverElement receiverElement = new ReceiverElement(
                    "1200",
                    new SoftwareNameElement("ESB","????????????"),
                    new SoftwareProviderElement("Caradigm","???????????????????????????????????????"),
                    new OrganizationElement("10","????????????????????????????????????????????????")
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
                    //??????????????????
                    orderExecRecord.setOrderCardCode(bdSupply.getDtExcardtype());
                    switch (bdSupply.getDtExcardtype()){
                        case "1":
                            orderExecRecord.setOrderCardName("?????????");
                            break;
                        case "2":
                            orderExecRecord.setOrderCardName("?????????");
                            break;
                        case "3":
                            orderExecRecord.setOrderCardName("?????????");
                            break;
                        case "4":
                            orderExecRecord.setOrderCardName("?????????");
                            break;
                        case "0402":
                            orderExecRecord.setOrderCardName("????????????");
                            break;
                        case "0404":
                            orderExecRecord.setOrderCardName("?????????");
                            break;
                        case "5":
                            orderExecRecord.setOrderCardName("?????????");
                            break;
                        case "06":
                            orderExecRecord.setOrderCardName("??????");
                            break;
                        case "07":
                            orderExecRecord.setOrderCardName("??????");
                            break;
                        case "08":
                            orderExecRecord.setOrderCardName("??????");
                            break;
                        case "99":
                            orderExecRecord.setOrderCardName("?????????");
                            break;
                    }

                }

                //??????????????????
                orderExecRecord.setOrderCategoryCode(PskqDictionMapUtil.hisToPingOrdtypeCodeMap(orderExecRecord.getOrderCategoryCode()));
                orderExecRecord.setOrderCategoryName(PskqDictionMapUtil.hisToPingOrdtypeNameMap(orderExecRecord.getOrderCategoryCode()));

            	orderExecRecord.setEncounterId(PskqMesUtils.pskqHisCode+orderExecRecord.getEncounterTypeCode()+"_"+PskqMesUtils.pskqSysCode+orderExecRecord.getEncounterId());
                orderExecRecord.setVisitId(PskqMesUtils.pskqHisCode+orderExecRecord.getEncounterTypeCode()+"_"+PskqMesUtils.pskqSysCode+orderExecRecord.getVisitId());

                
                //????????????
                Map<String, Object> dosageFormMap = PskqDictionMapUtil.hisToPingDosageFormMap(orderExecRecord.getDrugFormCode());
                orderExecRecord.setDrugFormCode(CommonUtils.getPropValueStr(dosageFormMap, "code"));
                orderExecRecord.setDrugFormName(CommonUtils.getPropValueStr(dosageFormMap, "name"));
                orderExecRecord.setOrderId(PskqMesUtils.pskqSysCode+orderExecRecord.getEncounterTypeCode()+"_"+PskqMesUtils.pskqHisCode+orderExecRecord.getOrderId());
                orderExecRecord.setOrgCode("10");
                orderExecRecord.setOrgName("????????????????????????????????????????????????");
                OrderInpat orderInpat = new OrderInpat();
            	ApplicationUtils.copyProperties(orderInpat, orderExecRecord);
            	RequestBody requestBody = new RequestBody.Builder()
		                .service("S0035","????????????????????????")
		                .event("E003502","??????????????????")
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
            listener.error("????????????????????????????????????","","");
        }
    }



    /**
     * ??????????????????
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
                    //??????????????????
                    orderExecRecord.setOrderCategoryCode(PskqDictionMapUtil.hisToPingOrdtypeCodeMap(orderExecRecord.getOrderCategoryCode()));
                    orderExecRecord.setOrderCategoryName(PskqDictionMapUtil.hisToPingOrdtypeNameMap(orderExecRecord.getOrderCategoryCode()));
                    
                    //????????????
                    Map<String, Object> dosageFormMap = PskqDictionMapUtil.hisToPingDosageFormMap(orderExecRecord.getDrugFormCode());
                    orderExecRecord.setDrugFormCode(CommonUtils.getPropValueStr(dosageFormMap, "code"));
                    orderExecRecord.setDrugFormName(CommonUtils.getPropValueStr(dosageFormMap, "name"));
                    orderExecRecord.setOrderId(PskqMesUtils.pskqSysCode+orderExecRecord.getEncounterTypeCode()+"_"+PskqMesUtils.pskqHisCode+orderExecRecord.getOrderId());
                    orderExecRecord.setOrgCode("10");
                    orderExecRecord.setOrgName("????????????????????????????????????????????????");
                	OrderInpat orderInpat = new OrderInpat();
                	ApplicationUtils.copyProperties(orderInpat, orderExecRecord);
                	
                	MessageFactory messageFactory = new MessageFactory();
                    Message message = messageFactory.getInstance(orderInpat);
                    Map<String,Object> map=new HashMap<>();
                    map.put("serviceCode","S0036");
                    map.put("serviceName","????????????????????????");
                    map.put("eventCode","E003605");
                    map.put("eventName","??????????????????");
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
                    //??????????????????
                    orderExecRecord.setOrderCategoryCode(PskqDictionMapUtil.hisToPingOrdtypeCodeMap(orderExecRecord.getOrderCategoryCode()));
                    orderExecRecord.setOrderCategoryName(PskqDictionMapUtil.hisToPingOrdtypeNameMap(orderExecRecord.getOrderCategoryCode()));
                    
                    //????????????
                    Map<String, Object> dosageFormMap = PskqDictionMapUtil.hisToPingDosageFormMap(orderExecRecord.getDrugFormCode());
                    orderExecRecord.setDrugFormCode(CommonUtils.getPropValueStr(dosageFormMap, "code"));
                    orderExecRecord.setDrugFormName(CommonUtils.getPropValueStr(dosageFormMap, "name"));
                    orderExecRecord.setOrderId(PskqMesUtils.pskqSysCode+orderExecRecord.getEncounterTypeCode()+"_"+PskqMesUtils.pskqHisCode+orderExecRecord.getOrderId());
                    orderExecRecord.setOrgCode("10");
                    orderExecRecord.setOrgName("????????????????????????????????????????????????");
                	OrderInpat orderInpat = new OrderInpat();
                	ApplicationUtils.copyProperties(orderInpat, orderExecRecord);
                	
                	MessageFactory messageFactory = new MessageFactory();
                    Message message = messageFactory.getInstance(orderInpat);
                    Map<String,Object> map=new HashMap<>();
                    map.put("serviceCode","S0036");
                    map.put("serviceName","????????????????????????");
                    map.put("eventCode","E003604");
                    map.put("eventName","????????????????????????");
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
                    //??????????????????
                    orderExecRecord.setOrderCategoryCode(PskqDictionMapUtil.hisToPingOrdtypeCodeMap(orderExecRecord.getOrderCategoryCode()));
                    orderExecRecord.setOrderCategoryName(PskqDictionMapUtil.hisToPingOrdtypeNameMap(orderExecRecord.getOrderCategoryCode()));
                    
                    //????????????
                    Map<String, Object> dosageFormMap = PskqDictionMapUtil.hisToPingDosageFormMap(orderExecRecord.getDrugFormCode());
                    orderExecRecord.setDrugFormCode(CommonUtils.getPropValueStr(dosageFormMap, "code"));
                    orderExecRecord.setDrugFormName(CommonUtils.getPropValueStr(dosageFormMap, "name"));
                    orderExecRecord.setOrderId(PskqMesUtils.pskqSysCode+orderExecRecord.getEncounterTypeCode()+"_"+PskqMesUtils.pskqHisCode+orderExecRecord.getOrderId());
                    orderExecRecord.setOrgCode("10");
                    orderExecRecord.setOrgName("????????????????????????????????????????????????");
                	OrderInpat orderInpat = new OrderInpat();
                	ApplicationUtils.copyProperties(orderInpat, orderExecRecord);
                	
                	MessageFactory messageFactory = new MessageFactory();
                    Message message = messageFactory.getInstance(orderInpat);
                    Map<String,Object> map=new HashMap<>();
                    map.put("serviceCode","S0035");
                    map.put("serviceName","????????????????????????");
                    map.put("eventCode","E003502");
                    map.put("eventName","??????????????????");
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
