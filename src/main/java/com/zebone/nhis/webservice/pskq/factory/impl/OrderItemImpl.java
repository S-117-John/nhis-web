package com.zebone.nhis.webservice.pskq.factory.impl;

import com.google.common.collect.Maps;
import com.zebone.nhis.webservice.pskq.factory.Message;
import com.zebone.nhis.webservice.pskq.model.OrderItem;
import com.zebone.nhis.webservice.pskq.model.message.*;
import com.zebone.nhis.webservice.pskq.utils.MessageBodyUtil;

import java.util.List;
import java.util.Map;

public class OrderItemImpl implements Message {

    private OrderItem orderItem;

    public OrderItemImpl() {
    }

    public OrderItemImpl(OrderItem orderItem) {
        this.orderItem = orderItem;
    }

    public OrderItem getOrderItem() {
        return orderItem;
    }

    public void setOrderItem(OrderItem orderItem) {
        this.orderItem = orderItem;
    }

    @Override
    public RequestBody getRequestBody(Map<String, Object> param) throws IllegalAccessException {
        ServiceElement serviceElement  = new ServiceElement("S0002","个人信息更新服务");
        EventElement eventElement = new EventElement("E000201","修改患者信息");
        SenderElement senderElement = new SenderElement(
                "2000",
                new SoftwareNameElement("HIS","医院信息管理系统"),
                new SoftwareProviderElement("feisen","菲森"),
                new OrganizationElement("10","南方医科大学深圳口腔医院（坪山）")
        );
        ReceiverElement receiverElement = new ReceiverElement(
                "1100",
                new SoftwareNameElement("ESB","集成平台"),
                new SoftwareProviderElement("Caradigm","恺恩泰（北京）科技有限公司"),
                new OrganizationElement("10","南方医科大学深圳口腔医院（坪山）")
        );
        List<DataElement> dataElements = MessageBodyUtil.dataElementsFactory(orderItem);
        Map<String,Object> map = Maps.newHashMap();
        map.put("PATIENT",dataElements);
        RequestBody requestBody = new RequestBody(
                serviceElement,
                eventElement,
                senderElement,
                receiverElement,
                map
        );
        return requestBody;
    }

    @Override
    public ResponseBody getResponseBody(Map<String, Object> param) {
        return null;
    }
}
