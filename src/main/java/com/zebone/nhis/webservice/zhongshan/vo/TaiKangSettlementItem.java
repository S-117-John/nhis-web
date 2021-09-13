package com.zebone.nhis.webservice.zhongshan.vo;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import java.util.List;

/**
 * @author wq
 * @Classname TaiKangSettlementItem
 * @Description 发票信息+二级费用信息
 * @Date 2020-11-23 18:48
 */
@XmlAccessorType(XmlAccessType.NONE)
public class TaiKangSettlementItem {

    /*
     * 发票号
     * */

    @XmlElement(name = "InvoiceNo")
    private String invoiceNo;
    /*
     * 结算日期
     * */

    @XmlElement(name = "SettleDate")

    private String settleDate;
    /*
     * 二级费用分类集合
     * */

    @XmlElementWrapper(name = "InvoiceCareStrList")
    @XmlElement(name = "InvoiceCareStr")
    private List<TaiKangInvoiceCareStr > taiKangInvoiceCareStrs;

    public String getInvoiceNo() {
        return invoiceNo;
    }

    public void setInvoiceNo(String invoiceNo) {
        this.invoiceNo = invoiceNo;
    }

    public List<TaiKangInvoiceCareStr> getTaiKangInvoiceCareStrs() {
        return taiKangInvoiceCareStrs;
    }

    public void setTaiKangInvoiceCareStrs(List<TaiKangInvoiceCareStr> taiKangInvoiceCareStrs) {
        this.taiKangInvoiceCareStrs = taiKangInvoiceCareStrs;
    }

    public String getSettleDate() {
        return settleDate;
    }

    public void setSettleDate(String settleDate) {
        this.settleDate = settleDate;
    }
}
