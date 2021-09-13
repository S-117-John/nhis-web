package com.zebone.nhis.webservice.zhongshan.vo;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 * @author wq
 * @Classname TaiKangInvoiceCareStr
 * @Description 二级费用信息
 * @Date 2020-11-23 18:45
 */
@XmlAccessorType(XmlAccessType.NONE)
public class TaiKangInvoiceCareStr {

    /*
     * 金额
     * */

    @XmlElement(name = "Money")
    private Double money;

    /*
     * 二级费用分类名称
     * */

    @XmlElement(name = "Name")

    private String name;

    /*
     * 发票号
     * */

    @XmlTransient
    private String invoiceNo;
    /*
     * 结算日期
     * */

    @XmlTransient
    private String settleDate;
    /*
     * 二级费用分类集合
     * */

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getMoney() {
        return money;
    }

    public void setMoney(Double money) {
        this.money = money;
    }

    public String getInvoiceNo() {
        return invoiceNo;
    }

    public void setInvoiceNo(String invoiceNo) {
        this.invoiceNo = invoiceNo;
    }


    public String getSettleDate() {
        return settleDate;
    }

    public void setSettleDate(String settleDate) {
        this.settleDate = settleDate;
    }
}
