package com.zebone.nhis.compay.ins.zsrm.vo;

import com.alibaba.fastjson.annotation.JSONField;

public class ZyBusinessParam {
    /**
     * 交易号
     */
    @JSONField(name = "function_id")
    private String functionId;
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
     * 结算标志
     */
    private String bka891;
    /**
     * 开始时间
     */
    private String aae030;
    /**
     * 结束时间
     */
    private String aae031;

    public String getFunctionId() {
        return functionId;
    }

    public void setFunctionId(String functionId) {
        this.functionId = functionId;
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

    public String getBka891() {
        return bka891;
    }

    public void setBka891(String bka891) {
        this.bka891 = bka891;
    }

    public String getAae030() {
        return aae030;
    }

    public void setAae030(String aae030) {
        this.aae030 = aae030;
    }

    public String getAae031() {
        return aae031;
    }

    public void setAae031(String aae031) {
        this.aae031 = aae031;
    }
}
