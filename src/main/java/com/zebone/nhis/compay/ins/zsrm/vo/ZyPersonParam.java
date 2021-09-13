package com.zebone.nhis.compay.ins.zsrm.vo;


import com.alibaba.fastjson.annotation.JSONField;

/**
 * 住院获取患者信息入参
 */
public class ZyPersonParam {
    /**
     * 交易号
     */
    @JSONField(name = "function_id")
    private String functionId;
    /**
     * 参保人所属行政区划
     */
    private String aab301;
    /**
     * 入参类型
     */
    private String bka895;
    /**
     * 入参值
     */
    private String bka896;
    /**
     * 医疗机构编码
     */
    private String akb020;
    /**
     * 业务类型
     */
    private String aka130;
    /**
     * 住院时间
     */
    private String bka017;
    /**
     * 生育业务类型
     */
    private String amc050;
    /**
     * 生育类型
     */
    private String bka912;
    /**
     * 生育手术类别
     */
    private String amc029;
    /**
     * 工伤待遇类别
     */
    private String bka006;

    public String getFunctionId() {
        return functionId;
    }

    public void setFunctionId(String functionId) {
        this.functionId = functionId;
    }

    public String getAab301() {
        return aab301;
    }

    public void setAab301(String aab301) {
        this.aab301 = aab301;
    }

    public String getBka895() {
        return bka895;
    }

    public void setBka895(String bka895) {
        this.bka895 = bka895;
    }

    public String getBka896() {
        return bka896;
    }

    public void setBka896(String bka896) {
        this.bka896 = bka896;
    }

    public String getAkb020() {
        return akb020;
    }

    public void setAkb020(String akb020) {
        this.akb020 = akb020;
    }

    public String getAka130() {
        return aka130;
    }

    public void setAka130(String aka130) {
        this.aka130 = aka130;
    }

    public String getBka017() {
        return bka017;
    }

    public void setBka017(String bka017) {
        this.bka017 = bka017;
    }

    public String getAmc050() {
        return amc050;
    }

    public void setAmc050(String amc050) {
        this.amc050 = amc050;
    }

    public String getBka912() {
        return bka912;
    }

    public void setBka912(String bka912) {
        this.bka912 = bka912;
    }

    public String getAmc029() {
        return amc029;
    }

    public void setAmc029(String amc029) {
        this.amc029 = amc029;
    }

    public String getBka006() {
        return bka006;
    }

    public void setBka006(String bka006) {
        this.bka006 = bka006;
    }
}
