package com.zebone.nhis.ma.pub.platform.pskq.factory.impl;

import com.zebone.nhis.ma.pub.platform.pskq.factory.Message;
import com.zebone.nhis.ma.pub.platform.pskq.model.LabApply;
import com.zebone.nhis.ma.pub.platform.pskq.model.OrdDiagInfo;
import com.zebone.nhis.ma.pub.platform.pskq.model.message.*;
import com.zebone.nhis.ma.pub.platform.pskq.utils.MessageBodyUtil;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class OrdDiagInfoImpl implements Message {

    private OrdDiagInfo ordDiagInfo;

    public OrdDiagInfo getOrdDiagInfo() {
        return ordDiagInfo;
    }

    public void setOrdDiagInfo(OrdDiagInfo ordDiagInfo) {
        this.ordDiagInfo = ordDiagInfo;
    }

    public OrdDiagInfoImpl(OrdDiagInfo ordDiagInfo) {
        this.ordDiagInfo = ordDiagInfo;
    }

    @Override
    public RequestBody getRequestBody(Map<String, Object> param) throws IllegalAccessException {
        String serviceCode = "";
        String serviceName = "";
        String eventCode = "";
        String eventName = "";
        if(param.containsKey("Control")&&param.get("Control")!=null&& !StringUtils.isEmpty(param.get("Control").toString())){
            if("NW".equalsIgnoreCase(param.get("Control").toString())){
                //新增
                serviceCode = "S1601";
                serviceName = "诊断信息新增服务";
                eventCode = "E160101";
                eventName = "下诊断";
            }else if("UP".equalsIgnoreCase(param.get("Control").toString())) {
                //更新
                serviceCode = "S1602";
                serviceName = "诊断信息更新服务";
                eventCode = "E160201";
                eventName = "更新诊断";
            }
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
        List<DataElement> dataElements = MessageBodyUtil.dataElementsFactory(ordDiagInfo);
        Map<String,Object> map = new HashMap<>();
        map.put("DIAGNOSE",dataElements);
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
