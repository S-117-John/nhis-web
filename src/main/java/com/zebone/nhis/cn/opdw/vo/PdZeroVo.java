package com.zebone.nhis.cn.opdw.vo;

/**
 * 零元购药品统计
 */
public class PdZeroVo {
    private String pkPd;//药品主键
    private String name;//名称
    private Double sumQuan;//可开立的最大数量
    private Double amount;//已开立数量
    private String pkUnit;//单位

    public String getPkPd() {
        return pkPd;
    }

    public void setPkPd(String pkPd) {
        this.pkPd = pkPd;
    }

    public Double getSumQuan() {
        return sumQuan;
    }

    public void setSumQuan(Double sumQuan) {
        this.sumQuan = sumQuan;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPkUnit() {
        return pkUnit;
    }

    public void setPkUnit(String pkUnit) {
        this.pkUnit = pkUnit;
    }
}
