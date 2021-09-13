package com.zebone.nhis.ma.pub.platform.pskq.factory.impl;

import com.zebone.nhis.ma.pub.platform.pskq.factory.Message;
import com.zebone.nhis.ma.pub.platform.pskq.model.CostDetailInpat;
import com.zebone.nhis.ma.pub.platform.pskq.model.CostDetailInpatSend;
import com.zebone.nhis.ma.pub.platform.pskq.model.OrderOutpat;
import com.zebone.nhis.ma.pub.platform.pskq.model.OrderOutpatList;
import com.zebone.nhis.ma.pub.platform.pskq.model.OrderOutpatSend;
import com.zebone.nhis.ma.pub.platform.pskq.model.message.*;
import com.zebone.nhis.ma.pub.platform.pskq.utils.MessageBodyUtil;

import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class OrderOutpatImpl implements Message {

    private OrderOutpatList orderOutpatList;


    public OrderOutpatList getOrderOutpatList() {
		return orderOutpatList;
	}

	public void setOrderOutpatList(OrderOutpatList orderOutpatList) {
		this.orderOutpatList = orderOutpatList;
	}

	public OrderOutpatImpl(OrderOutpatList orderOutpatList) {
        this.orderOutpatList = orderOutpatList;
    }

    @Override
    public RequestBody getRequestBody(Map<String, Object> param) throws IllegalAccessException {
        String serviceCode = "";
        String serviceName = "";
        String eventCode = "";
        String eventName = "";
        if(param.containsKey("serviceCode")&&param.get("serviceCode")!=null&& !StringUtils.isEmpty(param.get("serviceCode").toString())){
            serviceCode = (String) param.get("serviceCode");
            serviceName = (String) param.get("serviceName");
        }
        if(param.containsKey("eventCode")&&param.get("eventCode")!=null&& !StringUtils.isEmpty(param.get("eventCode").toString())){
            eventCode = (String) param.get("eventCode");
            eventName = (String) param.get("eventName");
        }
        ServiceElement serviceElement  = new ServiceElement(serviceCode,serviceName);
        EventElement eventElement = new EventElement(eventCode,eventName);
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
       // List<DataElementmini> dataElements = MessageBodyUtil.dataElementsFactoryMini(orderOutpatList);
        //Map<String,Object> map = new HashMap<>();
        //map.put("ORDER_OUTPAT",dataElements);
        
        List<DataElementmini> dataElements = MessageBodyUtil.dataElementsFactoryMini(orderOutpatList);
        //费用明细信息
        List<OrderOutpat> orderOutpatListS =  orderOutpatList.getOrderOutpats();
        List<OrderOutpatSend> orderOutpatSendList = new ArrayList<>();
        for (int i=0;i <orderOutpatListS.size();i++){
            List<DataElementmini> CostDetailOutpatList = MessageBodyUtil.dataElementsFactoryMini(orderOutpatListS.get(i));
            OrderOutpatSend costDetailInpatSend = new OrderOutpatSend();
            costDetailInpatSend.setSettlementDetailOutpat(CostDetailOutpatList);
            orderOutpatSendList.add(costDetailInpatSend);
        }
        Map<String,Object> map = new HashMap<>();
        map.put("ORDER_OUTPAT_LIST",orderOutpatSendList);
        RequestBody requestBody = new RequestBody(
                serviceElement,
                eventElement,
                senderElement,
                receiverElement,
                map
        );
        return requestBody;
    }
}
