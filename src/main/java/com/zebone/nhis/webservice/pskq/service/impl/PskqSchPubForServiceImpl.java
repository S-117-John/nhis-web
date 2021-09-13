package com.zebone.nhis.webservice.pskq.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.context.ApplicationContext;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.zebone.nhis.common.support.CommonUtils;
import com.zebone.nhis.webservice.pskq.config.SpringContextHolder;
import com.zebone.nhis.webservice.pskq.dao.PskqSchPubForWsMapper;
import com.zebone.nhis.webservice.pskq.factory.MessageFactory;
import com.zebone.nhis.webservice.pskq.model.ReserveOutpatient;
import com.zebone.nhis.webservice.pskq.model.listener.ResultListener;
import com.zebone.nhis.webservice.pskq.model.message.AckElement;
import com.zebone.nhis.webservice.pskq.model.message.DataElement;
import com.zebone.nhis.webservice.pskq.model.message.RequestBody;
import com.zebone.nhis.webservice.pskq.model.message.ResponseBody;
import com.zebone.nhis.webservice.pskq.service.PskqSchPubForService;
import com.zebone.nhis.webservice.pskq.utils.MessageBodyUtil;
import com.zebone.nhis.webservice.pskq.utils.PskqMesUtils;
import com.zebone.platform.common.support.User;
import com.zebone.platform.common.support.UserContext;

public class PskqSchPubForServiceImpl implements PskqSchPubForService {

    @Override
    public void findOrderByPkPi(String param, ResultListener listener) {
            try {
                Map<String,Object> responseMap = new HashMap<>();
                String result = "";
                Gson gson =  new GsonBuilder().setDateFormat("yyyyMMdd'T'HHmmss").create();
                RequestBody requestBody = gson.fromJson(param,RequestBody.class);
                ResponseBody responseBody = new ResponseBody();
                responseBody.setService(requestBody.getService());
                responseBody.setEvent(requestBody.getEvent());
                responseBody.setId(requestBody.getId());
                responseBody.setCreationTime(requestBody.getCreationTime());
                responseBody.setSender(requestBody.getSender());
                responseBody.setReceiver(requestBody.getReceiver());
                AckElement ackElement = new AckElement();
                ackElement.setTargetMessageId(requestBody.getId());
                Map<String,Object> query = requestBody.getQuery();
                if(query.containsKey("RESERVE_OUTPATIENT")&&query.get("RESERVE_OUTPATIENT")!=null){
                    List<DataElement> qryCostDeta = (List<DataElement>) query.get("RESERVE_OUTPATIENT");
                    ReserveOutpatient opSettle = (ReserveOutpatient) MessageFactory.deserialization(qryCostDeta, new ReserveOutpatient());
                  //校验非空
            		if(null== opSettle){
            			listener.error("患者信息不能为空");
            			return;
            		}
            		if (CommonUtils.isEmptyString(opSettle.getEmpiId()) || CommonUtils.isEmptyString(opSettle.getHisOrderNo())) {
            			listener.error("患者信息不能为空");
            			return;
            		}
            		User user = PskqMesUtils.getUserExt(requestBody.getSender().getId());
            		if(user == null) {
            			listener.error("发送方：【"+requestBody.getSender().getSoftwareName().getName()+ "】；未在his注册工号，请先联系his注册工号！");
            			return;
            		}
            		UserContext.setUser(user);
                    ApplicationContext applicationContext = SpringContextHolder.getApplicationContext();
                    PskqSchPubForWsMapper pskqSchPubForWsMapper = applicationContext.getBean("pskqSchPubForWsMapper", PskqSchPubForWsMapper.class);
                    Map<String, Object> mappar=new HashMap<String, Object>();
        			mappar.put("empiId", opSettle.getEmpiId());
        			mappar.put("hisOrderNo", opSettle.getHisOrderNo());
                    opSettle = pskqSchPubForWsMapper.queryOrders(mappar);
                    if(opSettle!=null){
                        ackElement.setAckCode("AA");
                        ackElement.setAckDetail("查询成功");
                        List<DataElement> dataElements = MessageBodyUtil.dataElementsFactory(opSettle);
                        ackElement.setReserveList(dataElements);
                        responseBody.setQueryAck(ackElement);
                        listener.success(responseBody);
                    }else {
                        listener.error("没有查询到预约信息");
                    }
                }else {
                    listener.error("没有查询到预约信息");
                }
            }catch (Exception e){
                listener.error(e.getMessage());
            }


    }
}
