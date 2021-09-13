package com.zebone.nhis.ma.pub.platform.send.impl.zsrm.vo.sch;

import com.zebone.nhis.ma.pub.platform.send.impl.zsrm.vo.base.PhResource;

/**
 * 短信推送请求构建vo（自定义模板推送实例）-实时推送
 */
public class SchShortMsgResource extends PhResource {

    public SchShortMsgResource(){}

    public SchShortMsgResource(String telNo,String message){
        this.telNo=telNo;
        this.message=message;
    }

    public SchShortMsgResource(String telNo,String minute,String checkCode){
        this.telNo=telNo;
        this.minute=minute;
        this.checkCode=checkCode;
    }

    /**
     * 电话号码
     */
    private String telNo;

    /**
     * 自定义模板推送内容
     */
    private String message;

    /**
     * 有效分钟数
     */
    private String minute;

    /**
     * 验证码
     */
    private String checkCode;

    /**
     * 服务名
     */
    private String serviceDomain;

    public String getTelNo() {
        return telNo;
    }

    public void setTelNo(String telNo) {
        this.telNo = telNo;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getMinute() {
        return minute;
    }

    public void setMinute(String minute) {
        this.minute = minute;
    }

    public String getCheckCode() {
        return checkCode;
    }

    public void setCheckCode(String checkCode) {
        this.checkCode = checkCode;
    }

    public String getServiceDomain() {
        return serviceDomain;
    }

    public void setServiceDomain(String serviceDomain) {
        this.serviceDomain = serviceDomain;
    }
}
