package com.zebone.nhis.webservice.pskq.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.context.ApplicationContext;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.zebone.nhis.webservice.pskq.config.SpringContextHolder;
import com.zebone.nhis.webservice.pskq.dao.PskqOpSettleDao;
import com.zebone.nhis.webservice.pskq.factory.MessageFactory;
import com.zebone.nhis.webservice.pskq.model.OpSettlement;
import com.zebone.nhis.webservice.pskq.model.listener.ResultListener;
import com.zebone.nhis.webservice.pskq.model.message.AckElement;
import com.zebone.nhis.webservice.pskq.model.message.DataElement;
import com.zebone.nhis.webservice.pskq.model.message.EmployeeResultResponse;
import com.zebone.nhis.webservice.pskq.model.message.RequestBody;
import com.zebone.nhis.webservice.pskq.model.message.ResponseBody;
import com.zebone.nhis.webservice.pskq.service.PskqOpSettleService;
import com.zebone.nhis.webservice.pskq.utils.MessageBodyUtil;

public class PskqOpSettleServiceImpl implements PskqOpSettleService {

    public static PskqOpSettleService newInstance() {
        return new PskqOpSettleServiceImpl();
    }

    @Override
    public void findByPkPiTime(String param, ResultListener listener) {
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
                if(query.containsKey("COST_DETAILS_OUTPAT_LIST")&&query.get("COST_DETAILS_OUTPAT_LIST")!=null){
                    List<DataElement> qryCostDeta = (List<DataElement>) query.get("COST_DETAILS_OUTPAT_LIST");
                    OpSettlement opSettle = (OpSettlement) MessageFactory.deserialization(qryCostDeta, new OpSettlement());
                    ApplicationContext applicationContext = SpringContextHolder.getApplicationContext();
                    PskqOpSettleDao pskqOpSettleDao = applicationContext.getBean("pskqOpSettleDao", PskqOpSettleDao.class);
                    Map<String,Object> opSetMap = new HashMap<>();
                    opSetMap.put("orgCode",opSettle.getOrgCode());
                    opSetMap.put("pkPatient",opSettle.getPkPatient());
                    if(opSettle.getSettlementDateTime()!=null&&opSettle.getSettlementDateTime().indexOf("^")!=-1){
                        opSetMap.put("settlementDateTimeStart",opSettle.getSettlementDateTime().split("^")[0]);
                        opSetMap.put("settlementDateTimeEnd",opSettle.getSettlementDateTime().split("^")[1]);
                    }else {
                        listener.error("结算日期时间传入为:"+opSettle.getSettlementDateTime()+",请检查");
                        return;
                    }

                    opSettle = pskqOpSettleDao.getListOpSettlement(opSetMap);
                    if(opSettle!=null){
                        ackElement.setAckCode("AA");
                        ackElement.setAckDetail("查询成功");
                        List<DataElement> dataElements = MessageBodyUtil.dataElementsFactory(opSettle);
                        EmployeeResultResponse employeeResultResponse = new EmployeeResultResponse();
                        employeeResultResponse.setEmployee(dataElements);
                        ackElement.setEmployeeResultResponse(employeeResultResponse);
                        responseBody.setQueryAck(ackElement);
                        listener.success(responseBody);
                    }else {
                        listener.error("没有查询到人员信息");
                    }
                }else {
                    listener.error("没有查询到人员信息");
                }
            }catch (Exception e){
                listener.error(e.getMessage());
            }


    }
}
