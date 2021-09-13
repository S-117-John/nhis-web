package com.zebone.nhis.compay.pub.vo;

public class InsChkAccSumVo {

    /** 险种*/
    private String insuType;
    /** 清算类别*/
    private String clrType;
    /** 对账开始日期*/
    private String dateBegin;
    /** 对账结束日期*/
    private String dateEnd;
    /** 医疗费总额*/
    private Double amountSt;
    /** 基金支付总额*/
    private Double amountInsu;
    /** 个人账户支付金额*/
    private Double amountPi;
    /** 定点医药机构结算笔数*/
    private Integer dataNum;

    /** 现金支付*/
    private Double amountCash;
    /**对账结果状态*/
    private String stmtRslt;
    /**对账结果状态-文字说明*/
    private String stmtRsltDes;
    /**对账结果说明*/
    private String stmtRsltDscr;

    public String getInsuType() {
        return insuType;
    }

    public void setInsuType(String insuType) {
        this.insuType = insuType;
    }

    public String getClrType() {
        return clrType;
    }

    public void setClrType(String clrType) {
        this.clrType = clrType;
    }

    public String getDateBegin() {
        return dateBegin;
    }

    public void setDateBegin(String dateBegin) {
        this.dateBegin = dateBegin;
    }

    public String getDateEnd() {
        return dateEnd;
    }

    public void setDateEnd(String dateEnd) {
        this.dateEnd = dateEnd;
    }

    public Double getAmountSt() {
        return amountSt;
    }

    public void setAmountSt(Double amountSt) {
        this.amountSt = amountSt;
    }

    public Double getAmountInsu() {
        return amountInsu;
    }

    public void setAmountInsu(Double amountInsu) {
        this.amountInsu = amountInsu;
    }

    public Double getAmountPi() {
        return amountPi;
    }

    public void setAmountPi(Double amountPi) {
        this.amountPi = amountPi;
    }

    public Integer getDataNum() {
        return dataNum;
    }

    public void setDataNum(Integer dataNum) {
        this.dataNum = dataNum;
    }

    public String getStmtRsltDscr() {
        return stmtRsltDscr;
    }

    public void setStmtRsltDscr(String stmtRsltDscr) {
        this.stmtRsltDscr = stmtRsltDscr;
    }

    public Double getAmountCash() {
        return amountCash;
    }

    public void setAmountCash(Double amountCash) {
        this.amountCash = amountCash;
    }

    public String getStmtRslt() {
        return stmtRslt;
    }

    public void setStmtRslt(String stmtRslt) {
        this.stmtRslt = stmtRslt;
    }

    public String getStmtRsltDes() {
        return stmtRsltDes;
    }

    public void setStmtRsltDes(String stmtRsltDes) {
        this.stmtRsltDes = stmtRsltDes;
    }
}
