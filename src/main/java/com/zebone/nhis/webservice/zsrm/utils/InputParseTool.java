package com.zebone.nhis.webservice.zsrm.utils;

import com.zebone.nhis.webservice.zsrm.vo.pack.MachInParam.MachInputVoHeader;
import com.zebone.platform.modules.exception.BusException;
import org.apache.commons.collections.CollectionUtils;

import javax.xml.bind.JAXB;
import javax.xml.bind.Marshaller;
import java.io.ByteArrayInputStream;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

public class InputParseTool {

    public static MachInputVoHeader getMachInput(String input) {
        try {
            return JAXB.unmarshal(new ByteArrayInputStream(input.getBytes("gb2312")),MachInputVoHeader.class);
        } catch (UnsupportedEncodingException e) {
            throw new BusException("结果转换异常："+e.getMessage());
        }
    }

    public static String getRemoteMethod(MachInputVoHeader header){
        return header!=null&& CollectionUtils.isNotEmpty(header.getItems())?header.getItems().get(0).getMethodName():null;
    }

    public static Map<String,Object> getCustomerProperties(){
        Map<String,Object> properties = new HashMap<>();
        properties.put(Marshaller.JAXB_ENCODING,"gb2312");
        return properties;
    }
}
