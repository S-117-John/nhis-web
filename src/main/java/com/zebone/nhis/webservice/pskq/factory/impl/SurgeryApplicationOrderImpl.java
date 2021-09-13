package com.zebone.nhis.webservice.pskq.factory.impl;

import com.google.common.collect.Maps;
import com.zebone.nhis.webservice.pskq.factory.Message;
import com.zebone.nhis.webservice.pskq.model.SurgeryApplicationOrder;
import com.zebone.nhis.webservice.pskq.model.message.*;
import com.zebone.nhis.webservice.pskq.utils.MessageBodyUtil;

import java.util.List;
import java.util.Map;

public class SurgeryApplicationOrderImpl implements Message {

    private SurgeryApplicationOrder surgeryApplicationOrder;


    public SurgeryApplicationOrderImpl() {
    }

    public SurgeryApplicationOrder getSurgeryApplicationOrder() {
        return surgeryApplicationOrder;
    }

    public void setSurgeryApplicationOrder(SurgeryApplicationOrder surgeryApplicationOrder) {
        this.surgeryApplicationOrder = surgeryApplicationOrder;
    }

    public SurgeryApplicationOrderImpl(SurgeryApplicationOrder surgeryApplicationOrder) {
        this.surgeryApplicationOrder = surgeryApplicationOrder;
    }

    @Override
    public RequestBody getRequestBody(Map<String, Object> param) throws IllegalAccessException {
        ServiceElement serviceElement  = new ServiceElement("S0011","术语注册服务");
        EventElement eventElement = new EventElement("E001103","新增物价项目");
        SenderElement senderElement = new SenderElement(
                "2000",
                new SoftwareNameElement("HIS","医院HIS"),
                new SoftwareProviderElement("HIS","医院HIS"),
                new OrganizationElement("10","南方医科大学深圳口腔医院（坪山）")
        );
        ReceiverElement receiverElement = new ReceiverElement(
                "1000",
                new SoftwareNameElement("MDM","主数据管理系统"),
                new SoftwareProviderElement("Caradigm","恺恩泰（北京）科技有限公司"),
                new OrganizationElement("10","南方医科大学深圳口腔医院（坪山）")
        );
        List<DataElement> dataElements = MessageBodyUtil.dataElementsFactory(surgeryApplicationOrder);
        Map<String,Object> map = Maps.newHashMap();
        map.put("PRICE",dataElements);
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
