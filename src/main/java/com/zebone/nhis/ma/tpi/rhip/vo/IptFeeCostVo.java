package com.zebone.nhis.ma.tpi.rhip.vo;

/**
 * 费用分类
 */
public class IptFeeCostVo {
    private String code;// 住院费用金额代码
    private String name;// 住院费用名称
    private String amount;// 费用总额

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }
}
