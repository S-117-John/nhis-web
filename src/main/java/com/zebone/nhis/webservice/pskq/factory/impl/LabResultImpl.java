package com.zebone.nhis.webservice.pskq.factory.impl;

import com.google.common.collect.Maps;
import com.zebone.nhis.webservice.pskq.factory.Message;
import com.zebone.nhis.webservice.pskq.model.LabResult;
import com.zebone.nhis.webservice.pskq.model.message.*;
import com.zebone.nhis.webservice.pskq.utils.MessageBodyUtil;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class LabResultImpl implements Message {

    private LabResult labResult;

    public LabResult getLabResult() {
        return labResult;
    }

    public void setLabResult(LabResult labResult) {
        this.labResult = labResult;
    }

    @Override
    public RequestBody getRequestBody(Map<String, Object> param) throws IllegalAccessException {
        return null;
    }

    @Override
    public ResponseBody getResponseBody(Map<String, Object> param) throws IllegalAccessException {
        ServiceElement serviceElement  = new ServiceElement("S0067","普通检验结果信息查询服务");
        EventElement eventElement = new EventElement("E006701","查询普通检验结果信息");
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

        List<DataElement> dataElements = MessageBodyUtil.dataElementsFactory(labResult);
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
