package com.zebone.nhis.webservice.zhongshan.vo;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import java.util.Date;
import java.util.List;

/**
 * @author wq
 * @Classname TaiKangMedicalDirectory
 * @Description 医疗目录返回实体类
 * @Date 2020-11-23 18:07
 */
@XmlAccessorType(XmlAccessType.NONE)
public class TaiKangMedicalDirectory {

    /*
     * 剂型
     * */

    @XmlElement(name = "BetakeGenre")
    private String betakeGenre;

    /*
     * 医疗目录名称
     * */

    @XmlElement(name = "HospitalListName")
    private String hospitalListName;
    /*
     *医疗项目编码
     * */

    @XmlElement(name = "HospitalListNumber")
    private String hospitalListNumber;
    /*
     * 自费比例
     * */

    @XmlElement(name = "LiableExpense")
    private Double liableExpense;
    /*
     * 指导限价
     * */

    @XmlElement(name = "LimitedPrice")
    private Double limitedPrice;
    /*
     * 费用属性
     * */

    @XmlElement(name = "PaymentProperty")
    private List<String> paymentProperty;
    /*
     * 医疗目录类别	西药、中成药、手术等
     * */

    @XmlElement(name = "Sort")
    private String sort;
    /*
     *规格
     * */

    @XmlElement(name = "Spec")
    private String spec;
    /*
     * 三方编码
     * */

    @XmlElement(name = "TripartiteNumber")
    private String tripartiteNumber;
    /*
     * 单位
     * */

    @XmlElement(name = "Unit")
    private String unit;
    /*
     * 医院单价
     * */

    @XmlElement(name = "UnitPrice")
    private Double unitPrice;
    /*
     * 更新时间
     * */

    @XmlElement(name = "UpdateTime")
    private Date updateTime;

    public String getBetakeGenre() {
        return betakeGenre;
    }

    public void setBetakeGenre(String betakeGenre) {
        this.betakeGenre = betakeGenre;
    }

    public String getHospitalListName() {
        return hospitalListName;
    }

    public void setHospitalListName(String hospitalListName) {
        this.hospitalListName = hospitalListName;
    }

    public String getHospitalListNumber() {
        return hospitalListNumber;
    }

    public void setHospitalListNumber(String hospitalListNumber) {
        this.hospitalListNumber = hospitalListNumber;
    }

    public Double getLiableExpense() {
        return liableExpense;
    }

    public void setLiableExpense(Double liableExpense) {
        this.liableExpense = liableExpense;
    }

    public Double getLimitedPrice() {
        return limitedPrice;
    }

    public void setLimitedPrice(Double limitedPrice) {
        this.limitedPrice = limitedPrice;
    }

    public List<String> getPaymentProperty() {
        return paymentProperty;
    }

    public void setPaymentProperty(List<String> paymentProperty) {
        this.paymentProperty = paymentProperty;
    }

    public String getSort() {
        return sort;
    }

    public void setSort(String sort) {
        this.sort = sort;
    }

    public String getSpec() {
        return spec;
    }

    public void setSpec(String spec) {
        this.spec = spec;
    }

    public String getTripartiteNumber() {
        return tripartiteNumber;
    }

    public void setTripartiteNumber(String tripartiteNumber) {
        this.tripartiteNumber = tripartiteNumber;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public Double getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(Double unitPrice) {
        this.unitPrice = unitPrice;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }
}
