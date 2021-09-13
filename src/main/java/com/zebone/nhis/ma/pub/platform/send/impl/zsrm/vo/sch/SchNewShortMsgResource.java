package com.zebone.nhis.ma.pub.platform.send.impl.zsrm.vo.sch;

import com.zebone.nhis.ma.pub.platform.send.impl.zsrm.vo.base.PhResource;

import java.util.Map;

/**
 * 短信推送请求构建vo（自定义模板推送实例）-实时推送
 */
public class SchNewShortMsgResource extends PhResource {

    /**
     * 接口服务域
     */
    private String serviceDomain;

    /**
     * 接收短信的电话号码 不能为空
     */
    private String telNo;

    /**
     * 短信模板代码
     */
    private String templateCode;

    /**
     * Json字符串
     */
    private Map jsonStr;



    public String getServiceDomain() {
        return serviceDomain;
    }

    public void setServiceDomain(String serviceDomain) {
        this.serviceDomain = serviceDomain;
    }

    public String getTelNo() {
        return telNo;
    }

    public void setTelNo(String telNo) {
        this.telNo = telNo;
    }

    public String getTemplateCode() {
        return templateCode;
    }

    public void setTemplateCode(String templateCode) {
        this.templateCode = templateCode;
    }

    public Map getJsonStr() {
        return jsonStr;
    }

    public void setJsonStr(Map jsonStr) {
        this.jsonStr = jsonStr;
    }
}
