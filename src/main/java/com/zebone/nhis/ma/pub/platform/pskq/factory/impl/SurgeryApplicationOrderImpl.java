package com.zebone.nhis.ma.pub.platform.pskq.factory.impl;

import com.google.common.collect.Maps;
import com.zebone.nhis.ma.pub.platform.pskq.factory.Message;
import com.zebone.nhis.ma.pub.platform.pskq.model.SurgeryApplicationOrder;
import com.zebone.nhis.ma.pub.platform.pskq.model.message.*;
import com.zebone.nhis.ma.pub.platform.pskq.utils.MessageBodyUtil;
import org.springframework.util.StringUtils;

import java.util.HashMap;
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

        String serviceCode = "";
        String serviceName = "";
        String eventCode = "";
        String eventName = "";
        if(param.containsKey("control")&&param.get("control")!=null&& !StringUtils.isEmpty(param.get("control").toString())){
            if("CA".equalsIgnoreCase(param.get("control").toString())||"OC".equalsIgnoreCase(param.get("control").toString())){
                //取消
                serviceCode = "S0051";
                serviceName = "手术申请信息更新服务";
                eventCode = "E005101";
                eventName = "更新手术申请";
            }else if("NW".equalsIgnoreCase(param.get("control").toString())){
            	//开立
                serviceCode = "S0050";
                serviceName = "手术申请信息新增服务";
                eventCode = "E005001";
                eventName = "开立手术申请";
            }
        }

        ServiceElement serviceElement  = new ServiceElement(serviceCode,serviceName);
        EventElement eventElement = new EventElement(eventCode,eventName);
        SenderElement senderElement = new SenderElement(
                "2000",
                new SoftwareNameElement("HIS","医院HIS"),
                new SoftwareProviderElement("Zebone","江苏振邦智慧城市信息系统有限公司"),
                new OrganizationElement("10","南方医科大学深圳口腔医院（坪山）")
        );
        ReceiverElement receiverElement = new ReceiverElement(
                "1200",
                new SoftwareNameElement("ESB","集成平台"),
                new SoftwareProviderElement("Caradigm","恺恩泰（北京）科技有限公司"),
                new OrganizationElement("10","南方医科大学深圳口腔医院（坪山）")
        );
        List<DataElement> dataElements = MessageBodyUtil.dataElementsFactory(surgeryApplicationOrder);
        Map<String,Object> map = new HashMap<>();
        map.put("SURGERY_APPLY",dataElements);
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
