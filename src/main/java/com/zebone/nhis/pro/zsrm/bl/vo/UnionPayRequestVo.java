package com.zebone.nhis.pro.zsrm.bl.vo;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "request")
@XmlAccessorType(XmlAccessType.FIELD)
public class UnionPayRequestVo {
    //银联入参
    private String groupId;// "COMM00000000104";
    private String settDate;//清算日期 ="20190926";
    private String trxDate;//交易日期
    private String pageSize;// = 2;
    private String pageIndex;// = 1;
    private String withTotalCount;// = "Y";
    private String dateMode;// = "00";

    private String settMerId;//商户编号
    private String settTermNo;//终端编号
    private String traceNo;//终端流水号
    private String refNo;//检索参考号
    private String merOrderNo;//商户订单号

    //海鹚支付入参
    @XmlElement(name="serviceCode")
    private String serviceCode;//接口名称
    @XmlElement(name="partnerId")
    private String partnerId;//合作方ID
    @XmlElement(name="timeStamp")
    private String timeStamp;//交易时间格式：YYYY-MM-DD 24HH:MI:SS
    @XmlElement(name="password")
    private String password;//加密密码
    @XmlElement(name="orderDate")
    private String orderDate;//账务日期 格式：YYYYMMDD
    @XmlElement(name="payMode")
    private String payMode;//交易渠道 WX：微信支付 ZFB：支付宝
    @XmlElement(name="merchantId")
    private String merchantId;//商户号
    @XmlElement(name="pageNo")
    private String pageNo;//页号从1开始，默认为1，表示第1页
    @XmlElement(name="pageNumber")
    private String pageNumber;//每页条数为空默认为10条，最大1000条

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public String getSettDate() {
        return settDate;
    }

    public void setSettDate(String settDate) {
        this.settDate = settDate;
    }

    public String getPageSize() {
        return pageSize;
    }

    public void setPageSize(String pageSize) {
        this.pageSize = pageSize;
    }

    public String getPageIndex() {
        return pageIndex;
    }

    public void setPageIndex(String pageIndex) {
        this.pageIndex = pageIndex;
    }

    public String getWithTotalCount() {
        return withTotalCount;
    }

    public void setWithTotalCount(String withTotalCount) {
        this.withTotalCount = withTotalCount;
    }

    public String getDateMode() {
        return dateMode;
    }

    public void setDateMode(String dateMode) {
        this.dateMode = dateMode;
    }

    public String getSettMerId() {
        return settMerId;
    }

    public void setSettMerId(String settMerId) {
        this.settMerId = settMerId;
    }

    public String getSettTermNo() {
        return settTermNo;
    }

    public void setSettTermNo(String settTermNo) {
        this.settTermNo = settTermNo;
    }

    public String getTraceNo() {
        return traceNo;
    }

    public void setTraceNo(String traceNo) {
        this.traceNo = traceNo;
    }

    public String getRefNo() {
        return refNo;
    }

    public void setRefNo(String refNo) {
        this.refNo = refNo;
    }

    public String getMerOrderNo() {
        return merOrderNo;
    }

    public void setMerOrderNo(String merOrderNo) {
        this.merOrderNo = merOrderNo;
    }

    public String getTrxDate() {
        return trxDate;
    }

    public void setTrxDate(String trxDate) {
        this.trxDate = trxDate;
    }

    public String getServiceCode() {
        return serviceCode;
    }

    public void setServiceCode(String serviceCode) {
        this.serviceCode = serviceCode;
    }

    public String getPartnerId() {
        return partnerId;
    }

    public void setPartnerId(String partnerId) {
        this.partnerId = partnerId;
    }

    public String getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(String timeStamp) {
        this.timeStamp = timeStamp;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(String orderDate) {
        this.orderDate = orderDate;
    }

    public String getPayMode() {
        return payMode;
    }

    public void setPayMode(String payMode) {
        this.payMode = payMode;
    }

    public String getMerchantId() {
        return merchantId;
    }

    public void setMerchantId(String merchantId) {
        this.merchantId = merchantId;
    }

    public String getPageNo() {
        return pageNo;
    }

    public void setPageNo(String pageNo) {
        this.pageNo = pageNo;
    }

    public String getPageNumber() {
        return pageNumber;
    }

    public void setPageNumber(String pageNumber) {
        this.pageNumber = pageNumber;
    }
}
