package com.zebone.nhis.compay.ins.changzhou.vo;

public class DataResult<T> {
    /**
     * 中心交易流水号
     */
    private String zxjylsh;
    /**
     * 业务周期号
     */
    private String ywzqh;
    /**
     * 联机标志
     */
    private String onLine;
    /**
     * 发送方交易流水号
     */
    private String fsfjylsh;
    /**
     * 交易号
     */
    private String businessCode;

    private T value;

    public T getValue() {
        return value;
    }

    public void setValue(T value) {
        this.value = value;
    }

    public String getZxjylsh() {
        return zxjylsh;
    }

    public void setZxjylsh(String zxjylsh) {
        this.zxjylsh = zxjylsh;
    }

    public String getYwzqh() {
        return ywzqh;
    }

    public void setYwzqh(String ywzqh) {
        this.ywzqh = ywzqh;
    }

    public String getOnLine() {
        return onLine;
    }

    public void setOnLine(String onLine) {
        this.onLine = onLine;
    }

    public String getFsfjylsh() {
        return fsfjylsh;
    }

    public void setFsfjylsh(String fsfjylsh) {
        this.fsfjylsh = fsfjylsh;
    }

    public String getBusinessCode() {
        return businessCode;
    }

    public void setBusinessCode(String businessCode) {
        this.businessCode = businessCode;
    }
}
