package com.zebone.nhis.ma.pub.platform.pskq.factory.impl;

import com.google.common.collect.Maps;
import com.zebone.nhis.ma.pub.platform.pskq.factory.Message;
import com.zebone.nhis.ma.pub.platform.pskq.model.MaterialItem;
import com.zebone.nhis.ma.pub.platform.pskq.model.message.*;
import com.zebone.nhis.ma.pub.platform.pskq.utils.MessageBodyUtil;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Map;

public class MaterialItemImpl implements Message {

    private MaterialItem materialItem;

    public MaterialItemImpl() {
    }

    public MaterialItemImpl(MaterialItem materialItem) {
        this.materialItem = materialItem;
    }

    public MaterialItem getMaterialItem() {
        return materialItem;
    }

    public void setMaterialItem(MaterialItem materialItem) {
        this.materialItem = materialItem;
    }

    @Override
    public RequestBody getRequestBody(Map<String, Object> param) throws IllegalAccessException {
        String serviceCode = "";
        String serviceName = "";
        String eventCode = "";
        String eventName = "";
        if(param.containsKey("STATUS")&&param.get("STATUS")!=null&& !StringUtils.isEmpty(param.get("STATUS").toString())){
            if("_ADD".equalsIgnoreCase(param.get("STATUS").toString())){
                //新增
                serviceCode = "S0011";
                serviceName = "术语注册服务";
                eventCode = "E001108";
                eventName = "新增物资材料项目";
            }else if("_UPDATE".equalsIgnoreCase(param.get("STATUS").toString())) {
                //更新
                serviceCode = "S0011";
                serviceName = "术语更新服务";
                eventCode = "E001208";
                eventName = "更新物资材料项目";
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
        List<DataElement> dataElements = MessageBodyUtil.dataElementsFactory(materialItem);
        Map<String,Object> map = Maps.newHashMap();
        map.put("MATERIAL_PROPERTY",dataElements);
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
