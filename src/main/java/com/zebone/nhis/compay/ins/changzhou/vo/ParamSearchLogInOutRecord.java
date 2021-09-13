package com.zebone.nhis.compay.ins.changzhou.vo;

import java.util.Date;

public class ParamSearchLogInOutRecord {
    /**
     * 开始时间
     */
    private Date beginTime;
    /**
     * 终止时间
     */
    private Date endTime;
    /**
     * 签到状态
     */
    private String status;
    /**
     * 页码
     */
    private Integer pageIndex;
    /**
     * 页面记录数
     */
    private Integer pageSize;
    /**
     * 机构
     */
    private String pkOrg;

    public Date getBeginTime() {
        return beginTime;
    }

    public void setBeginTime(Date beginTime) {
        this.beginTime = beginTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Integer getPageIndex() {
        return pageIndex;
    }

    public void setPageIndex(Integer pageIndex) {
        this.pageIndex = pageIndex;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    public String getPkOrg() {
        return pkOrg;
    }

    public void setPkOrg(String pkOrg) {
        this.pkOrg = pkOrg;
    }
}
