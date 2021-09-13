package com.zebone.nhis.webservice.pskq.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.context.ApplicationContext;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.zebone.nhis.webservice.pskq.config.SpringContextHolder;
import com.zebone.nhis.webservice.pskq.dao.PskqWebserviceOrganizationDao;
import com.zebone.nhis.webservice.pskq.factory.MessageFactory;
import com.zebone.nhis.webservice.pskq.model.Organization;
import com.zebone.nhis.webservice.pskq.model.listener.ResultListener;
import com.zebone.nhis.webservice.pskq.model.message.AckElement;
import com.zebone.nhis.webservice.pskq.model.message.DataElement;
import com.zebone.nhis.webservice.pskq.model.message.OrganizationResultResponse;
import com.zebone.nhis.webservice.pskq.model.message.RequestBody;
import com.zebone.nhis.webservice.pskq.model.message.ResponseBody;
import com.zebone.nhis.webservice.pskq.service.OrganizationService;
import com.zebone.nhis.webservice.pskq.utils.MessageBodyUtil;

public class PskqWebServiceOrganizationServiceImpl implements OrganizationService {
    @Override
    public void findByOrgCode(String param, ResultListener listener) {
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
            if(query.containsKey("ORGANIZATION")&&query.get("ORGANIZATION")!=null){
                List<DataElement> dataElement = (List<DataElement>) query.get("ORGANIZATION");
                Organization organization = (Organization) MessageFactory.deserialization(dataElement, new Organization());
                ApplicationContext applicationContext = SpringContextHolder.getApplicationContext();
                PskqWebserviceOrganizationDao pskqWebserviceOrganizationDao = applicationContext.getBean("pskqWebserviceOrganizationDao", PskqWebserviceOrganizationDao.class);
                organization = pskqWebserviceOrganizationDao.findOrgInfoByOrgCode(organization.getOrgCode());
                if(organization!=null){
                    ackElement.setAckCode("AA");
                    ackElement.setAckDetail("查询成功");
                    List<DataElement> dataElements = MessageBodyUtil.dataElementsFactory(organization);
                    OrganizationResultResponse organizationResultResponse = new OrganizationResultResponse();
                    organizationResultResponse.setOrganization(dataElements);
                    ackElement.setOrganizationResultResponse(organizationResultResponse);
                    responseBody.setQueryAck(ackElement);
                    listener.success(responseBody);
                }else {
                    listener.error("没有查询到机构信息");
                }
            }else {
                listener.error("没有查询到机构信息");
            }
        }catch (Exception e){
            listener.error(e.getMessage());
        }
    }
}
