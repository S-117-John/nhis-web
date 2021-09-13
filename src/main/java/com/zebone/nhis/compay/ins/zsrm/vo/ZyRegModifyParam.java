package com.zebone.nhis.compay.ins.zsrm.vo;

import com.alibaba.fastjson.annotation.JSONField;

public class ZyRegModifyParam {
    @JSONField(name = "function_id")
    private String functionId;
    /**
     * 医疗机构编码
     */
    private String akb020;
    /**
     * 就诊登记号
     */
    private String aaz218;
    /**
     * 病区编码
     */
    private String bka021;
    /**
     * 病区名称
     */
    private String bka022;
    /**
     * 就诊科室
     */
    private String bka019;
    /**
     * 就诊科室名称
     */
    private String bka020;
    /**
     * 住院号
     */
    private String bka025;
    /**
     * 床位号
     */
    private String bka023;
    /**
     * 医师编码
     */
    private String bka503;
    /**
     * 住院原因
     */
    private String ykc679;
    /**
     * 补助类型
     */
    private String ykc680;

    private transient InsSgsybVisit zyRegInfo;

    public InsSgsybVisit getZyRegInfo() {
        return zyRegInfo;
    }

    public void setZyRegInfo(InsSgsybVisit zyRegInfo) {
        this.zyRegInfo = zyRegInfo;
    }

    public String getFunctionId() {
        return functionId;
    }

    public void setFunctionId(String functionId) {
        this.functionId = functionId;
    }

    public String getAkb020() {
        return akb020;
    }

    public void setAkb020(String akb020) {
        this.akb020 = akb020;
    }

    public String getAaz218() {
        return aaz218;
    }

    public void setAaz218(String aaz218) {
        this.aaz218 = aaz218;
    }

    public String getBka021() {
        return bka021;
    }

    public void setBka021(String bka021) {
        this.bka021 = bka021;
    }

    public String getBka022() {
        return bka022;
    }

    public void setBka022(String bka022) {
        this.bka022 = bka022;
    }

    public String getBka019() {
        return bka019;
    }

    public void setBka019(String bka019) {
        this.bka019 = bka019;
    }

    public String getBka020() {
        return bka020;
    }

    public void setBka020(String bka020) {
        this.bka020 = bka020;
    }

    public String getBka025() {
        return bka025;
    }

    public void setBka025(String bka025) {
        this.bka025 = bka025;
    }

    public String getBka023() {
        return bka023;
    }

    public void setBka023(String bka023) {
        this.bka023 = bka023;
    }

    public String getBka503() {
        return bka503;
    }

    public void setBka503(String bka503) {
        this.bka503 = bka503;
    }

    public String getYkc679() {
        return ykc679;
    }

    public void setYkc679(String ykc679) {
        this.ykc679 = ykc679;
    }

    public String getYkc680() {
        return ykc680;
    }

    public void setYkc680(String ykc680) {
        this.ykc680 = ykc680;
    }
}
