package com.zebone.nhis.webservice.pskq.factory.impl;

import com.google.common.collect.Maps;
import com.zebone.nhis.webservice.pskq.factory.Message;
import com.zebone.nhis.webservice.pskq.model.message.*;
import com.zebone.nhis.webservice.pskq.utils.MessageBodyUtil;

import java.util.List;
import java.util.Map;

public class AckImpl implements Message {

    private AckElement ackElement;

    public AckImpl() {
    }

    public AckImpl(AckElement ackElement) {
        this.ackElement = ackElement;
    }

    public AckElement getAckElement() {
        return ackElement;
    }

    public void setAckElement(AckElement ackElement) {
        this.ackElement = ackElement;
    }

    @Override
    public RequestBody getRequestBody(Map<String, Object> param) throws IllegalAccessException {
        return null;
    }

    @Override
    public ResponseBody getResponseBody(Map<String, Object> param) {
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

        Map<String,Object> map = Maps.newHashMap();
        map.put("ackCode",ackElement.getAckCode());
        map.put("targetMessageId",ackElement.getTargetMessageId());
        map.put("ackDetail",ackElement.getAckDetail());
        ResponseBody responseBody = new ResponseBody();
        responseBody.setService(serviceElement);
        responseBody.setSender(senderElement);
        responseBody.setEvent(eventElement);
        responseBody.setReceiver(receiverElement);
        responseBody.setAck(map);
        return responseBody;
    }
}
