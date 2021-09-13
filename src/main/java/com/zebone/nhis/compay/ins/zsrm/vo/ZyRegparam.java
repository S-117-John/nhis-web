package com.zebone.nhis.compay.ins.zsrm.vo;


import com.alibaba.fastjson.annotation.JSONField;

public class ZyRegparam {
    /**
     * 交易号
     */
    @JSONField(name = "function_id")
    private String functionId;
    /**
     * 医疗机构编码
     */
    private String akb020;
    /**
     * 电脑号
     */
    private String aac001;
    /**
     * 业务类型
     */
    private String aka130;
    /**
     * 医疗待遇类型
     */
    private String bka006;
    /**
     * 住院时间
     */
    private String bka017;
    /**
     * 登记人员工号
     */
    private String bka014;
    /**
     * 登记人姓名
     */
    private String bka015;
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
     * 诊断，icd编码
     */
    private String bka026;
    /**
     * 床位号
     */
    private String bka023;
    /**
     * 床位类型
     */
    private String bka024;
    /**
     * 医师编码
     */
    private String bka503;
    /**
     * 备注
     */
    private String bka043;
    /**
     * 住院号
     */
    private String bka025;
    /**
     * 住院原因
     */
    private String ykc679;
    /**
     * 补助类型
     */
    private String ykc680;
    /**
     * 银行id
     */
    private String aaz065;
    /**
     * 银行户名
     */
    private String aae009;
    /**
     * 银行账号
     */
    private String aae010;
    /**
     * 中途结算
     */
    private String bkc500;


    private transient String pkPi;

    private transient ZyPersonInfo personInfo;

    public ZyPersonInfo getPersonInfo() {
        return personInfo;
    }

    public void setPersonInfo(ZyPersonInfo personInfo) {
        this.personInfo = personInfo;
    }

    public String getPkPi() {
        return pkPi;
    }

    public void setPkPi(String pkPi) {
        this.pkPi = pkPi;
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

    public String getAac001() {
        return aac001;
    }

    public void setAac001(String aac001) {
        this.aac001 = aac001;
    }

    public String getAka130() {
        return aka130;
    }

    public void setAka130(String aka130) {
        this.aka130 = aka130;
    }

    public String getBka006() {
        return bka006;
    }

    public void setBka006(String bka006) {
        this.bka006 = bka006;
    }

    public String getBka017() {
        return bka017;
    }

    public void setBka017(String bka017) {
        this.bka017 = bka017;
    }

    public String getBka014() {
        return bka014;
    }

    public void setBka014(String bka014) {
        this.bka014 = bka014;
    }

    public String getBka015() {
        return bka015;
    }

    public void setBka015(String bka015) {
        this.bka015 = bka015;
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

    public String getBka026() {
        return bka026;
    }

    public void setBka026(String bka026) {
        this.bka026 = bka026;
    }

    public String getBka023() {
        return bka023;
    }

    public void setBka023(String bka023) {
        this.bka023 = bka023;
    }

    public String getBka024() {
        return bka024;
    }

    public void setBka024(String bka024) {
        this.bka024 = bka024;
    }

    public String getBka503() {
        return bka503;
    }

    public void setBka503(String bka503) {
        this.bka503 = bka503;
    }

    public String getBka043() {
        return bka043;
    }

    public void setBka043(String bka043) {
        this.bka043 = bka043;
    }

    public String getBka025() {
        return bka025;
    }

    public void setBka025(String bka025) {
        this.bka025 = bka025;
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

    public String getAaz065() {
        return aaz065;
    }

    public void setAaz065(String aaz065) {
        this.aaz065 = aaz065;
    }

    public String getAae009() {
        return aae009;
    }

    public void setAae009(String aae009) {
        this.aae009 = aae009;
    }

    public String getAae010() {
        return aae010;
    }

    public void setAae010(String aae010) {
        this.aae010 = aae010;
    }

    public String getBkc500() {
        return bkc500;
    }

    public void setBkc500(String bkc500) {
        this.bkc500 = bkc500;
    }
}
