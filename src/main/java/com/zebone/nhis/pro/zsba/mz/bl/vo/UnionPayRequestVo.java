package com.zebone.nhis.pro.zsba.mz.bl.vo;

public class UnionPayRequestVo {
    private String groupId;// "COMM00000000104";
    private String settDate;//清算日期 ="20190926";
    private String trxDate;//交易日期
    private int pageSize;// = 2;
    private int pageIndex;// = 1;
    private String withTotalCount;// = "Y";
    private String dateMode;// = "00";

    private String settMerId;//商户编号
    private String settTermNo;//终端编号
    private String traceNo;//终端流水号
    private String refNo;//检索参考号
    private String merOrderNo;//商户订单号

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

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public int getPageIndex() {
        return pageIndex;
    }

    public void setPageIndex(int pageIndex) {
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
}
