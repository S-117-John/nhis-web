package com.zebone.nhis.webservice.pskq.factory.impl;


import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import com.zebone.nhis.webservice.pskq.factory.Message;
import com.zebone.nhis.webservice.pskq.model.InAppPay;
import com.zebone.nhis.webservice.pskq.model.message.AckElement;
import com.zebone.nhis.webservice.pskq.model.message.DataElement;
import com.zebone.nhis.webservice.pskq.model.message.EventElement;
import com.zebone.nhis.webservice.pskq.model.message.LabResultResponse;
import com.zebone.nhis.webservice.pskq.model.message.OrganizationElement;
import com.zebone.nhis.webservice.pskq.model.message.ReceiverElement;
import com.zebone.nhis.webservice.pskq.model.message.RequestBody;
import com.zebone.nhis.webservice.pskq.model.message.ResponseBody;
import com.zebone.nhis.webservice.pskq.model.message.SenderElement;
import com.zebone.nhis.webservice.pskq.model.message.ServiceElement;
import com.zebone.nhis.webservice.pskq.model.message.SoftwareNameElement;
import com.zebone.nhis.webservice.pskq.model.message.SoftwareProviderElement;
import com.zebone.nhis.webservice.pskq.utils.MessageBodyUtil;


public class InAppPayImpl implements Message{

    private InAppPay inAppPay;

    public InAppPayImpl() {

    }

    public InAppPayImpl(InAppPay inAppPay) {
    	this.inAppPay = inAppPay;
    }

    public InAppPay getInAppPay() {
		return inAppPay;
	}

	public void setInAppPay(InAppPay inAppPay) {
		this.inAppPay = inAppPay;
	}




	@Override
    public RequestBody getRequestBody(Map<String, Object> param) throws IllegalAccessException {
        return null;
    }

    @Override
    public ResponseBody getResponseBody(Map<String, Object> param) throws IllegalAccessException {
        ServiceElement serviceElement  = new ServiceElement("S1503","住院结算新增服务");
        EventElement eventElement = new EventElement("E150301","住院预交金");

        SenderElement senderElement = new SenderElement(
                "1200",
                new SoftwareNameElement("ESB","集成平台"),
                new SoftwareProviderElement("Caradigm","恺恩泰（北京）科技有限公司"),
                new OrganizationElement("10","南方医科大学深圳口腔医院（坪山）")
        );

        ReceiverElement receiverElement = new ReceiverElement(
                "2500",
                new SoftwareNameElement("ZZJ","自助机"),
                new SoftwareProviderElement("SUN-TUNNEL","环阳通"),
                new OrganizationElement("10","南方医科大学深圳口腔医院（坪山）")
        );


        List<DataElement> dataElements = MessageBodyUtil.dataElementsFactory(inAppPay);
        ResponseBody responseBody = new ResponseBody();
        responseBody.setService(serviceElement);
        responseBody.setEvent(eventElement);
        responseBody.setId(UUID.randomUUID().toString());
        responseBody.setCreationTime(new Date());
        responseBody.setSender(senderElement);
        responseBody.setReceiver(receiverElement);
        AckElement ackElement = (AckElement) param.get("ack");
        LabResultResponse labResultResponse = new LabResultResponse();
        labResultResponse.setLabMaster(dataElements);
        ackElement.setLabResult(labResultResponse);
        responseBody.setQueryAck(ackElement);
        return responseBody;
    }
}
