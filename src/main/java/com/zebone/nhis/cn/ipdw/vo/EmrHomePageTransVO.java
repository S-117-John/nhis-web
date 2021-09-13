package com.zebone.nhis.cn.ipdw.vo;

import java.util.Date;

/**
 * 重症监护信息
 */
public class EmrHomePageTransVO {

    private String pkTrans;
    private String pkOrg;
    private String pkPage;
    private String seqNo;
    private String pkDept;
    private String deptCode;
    private String deptName;
    private Date transDate;
    private String delFlag;
    private String remark;
    private String creator;
    private Date createTime;
    private Date ts;

    public String getPkTrans() {
        return pkTrans;
    }

    public void setPkTrans(String pkTrans) {
        this.pkTrans = pkTrans;
    }

    public String getPkOrg() {
        return pkOrg;
    }

    public void setPkOrg(String pkOrg) {
        this.pkOrg = pkOrg;
    }

    public String getPkPage() {
        return pkPage;
    }

    public void setPkPage(String pkPage) {
        this.pkPage = pkPage;
    }

    public String getSeqNo() {
        return seqNo;
    }

    public void setSeqNo(String seqNo) {
        this.seqNo = seqNo;
    }

    public String getPkDept() {
        return pkDept;
    }

    public void setPkDept(String pkDept) {
        this.pkDept = pkDept;
    }

    public String getDeptCode() {
        return deptCode;
    }

    public void setDeptCode(String deptCode) {
        this.deptCode = deptCode;
    }

    public String getDeptName() {
        return deptName;
    }

    public void setDeptName(String deptName) {
        this.deptName = deptName;
    }

    public Date getTransDate() {
        return transDate;
    }

    public void setTransDate(Date transDate) {
        this.transDate = transDate;
    }

    public String getDelFlag() {
        return delFlag;
    }

    public void setDelFlag(String delFlag) {
        this.delFlag = delFlag;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getTs() {
        return ts;
    }

    public void setTs(Date ts) {
        this.ts = ts;
    }
}
