package com.zebone.nhis.compay.pub.vo;

import java.util.Date;
import java.util.Map;

/**
 * 头部报文类
 *
 */
public class NationalHeadReqVo {

    /**
     *交易编号
     */
    private String infno;

    /**
     *发送方报文ID
     */
    private String msgid;

    /**
     *就医地医保区划
     */
    private String mdtrtareaAdmvs;

    /**
     *参保地医保区划
     */
    private String insuplcAdmdvs;

    /**
     *接收方系统代码
     */
    private String recerSysCode;

    /**
     *设备编号
     */
    private String devNo;

    /**
     *设备安全信息
     */
    private String devSafeInfo;

    /**
     *数字签名信息
     */
    private String cainfo;

    /**
     *签名类型
     */
    private String signtype;

    /**
     *接口版本号
     */
    private String infver;

    /**
     *经办人类别
     */
    private String opterType;

    /**
     *经办人
     */
    private String opter;

    /**
     *经办人姓名
     */
    private String opterName;

    /**
     *交易时间
     */
    private String infTime;

    /**
     *定点医药机构编号
     */
    private String fixmedinsCode;

    /**
     *定点医药机构名称
     */
    private String fixmedinsName;

    /**
     *交易签到流水号
     */
    private String signNo;

    /**
     *交易输入
     */
    private Map<String,Object> input;

    public String getInfno() {
        return infno;
    }

    public void setInfno(String infno) {
        this.infno = infno;
    }

    public String getMsgid() {
        return msgid;
    }

    public void setMsgid(String msgid) {
        this.msgid = msgid;
    }

    public String getMdtrtareaAdmvs() {
        return mdtrtareaAdmvs;
    }

    public void setMdtrtareaAdmvs(String mdtrtareaAdmvs) {
        this.mdtrtareaAdmvs = mdtrtareaAdmvs;
    }

    public String getInsuplcAdmdvs() {
        return insuplcAdmdvs;
    }

    public void setInsuplcAdmdvs(String insuplcAdmdvs) {
        this.insuplcAdmdvs = insuplcAdmdvs;
    }

    public String getRecerSysCode() {
        return recerSysCode;
    }

    public void setRecerSysCode(String recerSysCode) {
        this.recerSysCode = recerSysCode;
    }

    public String getDevNo() {
        return devNo;
    }

    public void setDevNo(String devNo) {
        this.devNo = devNo;
    }

    public String getDevSafeInfo() {
        return devSafeInfo;
    }

    public void setDevSafeInfo(String devSafeInfo) {
        this.devSafeInfo = devSafeInfo;
    }

    public String getCainfo() {
        return cainfo;
    }

    public void setCainfo(String cainfo) {
        this.cainfo = cainfo;
    }

    public String getSigntype() {
        return signtype;
    }

    public void setSigntype(String signtype) {
        this.signtype = signtype;
    }

    public String getInfver() {
        return infver;
    }

    public void setInfver(String infver) {
        this.infver = infver;
    }

    public String getOpterType() {
        return opterType;
    }

    public void setOpterType(String opterType) {
        this.opterType = opterType;
    }

    public String getOpter() {
        return opter;
    }

    public void setOpter(String opter) {
        this.opter = opter;
    }

    public String getOpterName() {
        return opterName;
    }

    public void setOpterName(String opterName) {
        this.opterName = opterName;
    }

    public String getInfTime() {
        return infTime;
    }

    public void setInfTime(String infTime) {
        this.infTime = infTime;
    }

    public String getFixmedinsCode() {
        return fixmedinsCode;
    }

    public void setFixmedinsCode(String fixmedinsCode) {
        this.fixmedinsCode = fixmedinsCode;
    }

    public String getFixmedinsName() {
        return fixmedinsName;
    }

    public void setFixmedinsName(String fixmedinsName) {
        this.fixmedinsName = fixmedinsName;
    }

    public String getSignNo() {
        return signNo;
    }

    public void setSignNo(String signNo) {
        this.signNo = signNo;
    }

    public Map<String, Object> getInput() {
        return input;
    }

    public void setInput(Map<String, Object> input) {
        this.input = input;
    }
}
