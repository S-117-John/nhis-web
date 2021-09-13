package com.zebone.nhis.webservice.zhongshan.vo;

import org.hibernate.validator.constraints.NotBlank;

/**
 * @Classname ThiIpChargeRet
 * @Description 06号接口出参
 * @Date 2021-04-08 11:16
 * @Created by wuqiang
 */
public class ThiIpChargeRet {

    /**
     *记费主键
     */
    @NotBlank(message = "记费主键不能为空")
    private String pkCgIp;
    /**
     *就诊序号
     */
    private  String pkPv;
    /**
     *记费名称
     */
    private  String nameCg;
    /**
     *单价
     */
    private  String price;
    /**
     *数量
     */
    @NotBlank(message = "退费数量不能为空")
    private  String quan;
    /**
     *金额
     */
    private  String amount;
    /**
     *患者自付金额
     */
    private  String amountPi;
    @NotBlank(message = "退费时间不能为空")
    private String dataCg;

    /**
     * 退费原因
     */
    private String noteCg;

    public String getNoteCg() {
        return noteCg;
    }

    public void setNoteCg(String noteCg) {
        this.noteCg = noteCg;
    }

    public String getAmountPi() {
        return amountPi;
    }

    public void setAmountPi(String amountPi) {
        this.amountPi = amountPi;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getQuan() {
        return quan;
    }

    public void setQuan(String quan) {
        this.quan = quan;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getNameCg() {
        return nameCg;
    }

    public void setNameCg(String nameCg) {
        this.nameCg = nameCg;
    }



    public String getPkCgIp() {
        return pkCgIp;
    }

    public void setPkCgIp(String pkCgIp) {
        this.pkCgIp = pkCgIp;
    }

    @Override
    public String toString() {
        return "ThiIpChargeRet{" +
                "pkCgIp='" + pkCgIp + '\'' +
                ", codePv='" + pkPv + '\'' +
                ", nameCg='" + nameCg + '\'' +
                ", price='" + price + '\'' +
                ", quan='" + quan + '\'' +
                ", amount='" + amount + '\'' +
                ", amountPi='" + amountPi + '\'' +
                '}';
    }

    public String getDataCg() {
        return dataCg;
    }

    public void setDataCg(String dataCg) {
        this.dataCg = dataCg;
    }

    public String getPkPv() {
        return pkPv;
    }

    public void setPkPv(String pkPv) {
        this.pkPv = pkPv;
    }
}
