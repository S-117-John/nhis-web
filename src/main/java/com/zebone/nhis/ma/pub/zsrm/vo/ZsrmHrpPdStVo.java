package com.zebone.nhis.ma.pub.zsrm.vo;

import com.zebone.platform.modules.dao.build.au.Field;
import com.zebone.platform.modules.dao.build.au.Table;

import java.util.Date;

public class ZsrmHrpPdStVo<T> {

    /**
     * 库单主键
     */
    private String pkPdst;
    /**
     * 当前机构
     */
    private String pkOrg;

    /**
     * 当前仓库所属的部门
     */
    private String pkDeptSt;

    /**
     * 当前仓库
     */
    private String pkStoreSt;

    /**
     * 业务类型
     */
    private String dtSttype;

    /**
     * 出库单号
     */
    private String codeSt;

    /**
     * 库单名称
     */
    private String nameSt;

    /**
     * 出入库方向
     */
    private String euDirect;

    /**
     * 库单日期
     */
    private Date dateSt;

    /**
     * 库单状态
     */
    private String euStatus;

    /**
     * 对应的入库机构
     */
    private String pkOrgLk;

    /**
     * 对应的入库部门
     */
    private String pkDeptLk;

    /**
     * 对应的入库仓库
     */
    private String pkStoreLk;

    /**
     * 供应商
     */
    private String pkSupplyer;

    /**
     * 采购订单
     */
    private String pkPdpu;

    /**
     * 请领单
     */
    private String pkPdplan;

    /**
     * 发票号
     */
    private String eceiptNo;


    /**
     * 付款完成标志
     */
    private String flagPay;

    /**
     * 当前用户主键（经办人）
     */
    private String pkEmpOp;

    /**
     * 当前用户姓名
     */
    private String nameEmpOp;

    /**
     * 审核标志
     */
    private String flagChk;

    /**
     * 审核人
     */
    private String pkEmpChk;

    /**
     * 审核人姓名
     */
    private String nameEmpChk;

    /**
     * 审核时间
     */
    private Date dateChk;

    /**
     * 备注
     */
    private String note;

    /**
     * 源单据主键
     */
    private String pkPdstSr;

    /**
     * 采购标志
     */
    private String flagPu;


    /**
     * 平台返回出入
     */
    private String codeRtn;

    /**
     * 创建人
     */
    private String creator;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 修改人
     */
    private String modifier;

    /**
     * 修改时间
     */
    @Field("modity_time")
    private Date modityTime;

    /**
     * 删除标志
     */
    private String delFlag;

    /**
     * 时间戳
     */
    private Date ts;

    private T items;

    private String receiptNo;

    public String getReceiptNo() {
        return receiptNo;
    }

    public void setReceiptNo(String receiptNo) {
        this.receiptNo = receiptNo;
    }

    public T getItems() {
        return items;
    }

    public void setItems(T items) {
        this.items = items;
    }

    public ZsrmHrpPdStVo() {
    }

    public String getPkPdst() {
        return pkPdst;
    }

    public String getPkOrg() {
        return pkOrg;
    }

    public String getPkDeptSt() {
        return pkDeptSt;
    }

    public String getPkStoreSt() {
        return pkStoreSt;
    }

    public String getDtSttype() {
        return dtSttype;
    }

    public String getCodeSt() {
        return codeSt;
    }

    public String getNameSt() {
        return nameSt;
    }

    public String getEuDirect() {
        return euDirect;
    }

    public Date getDateSt() {
        return dateSt;
    }

    public String getEuStatus() {
        return euStatus;
    }

    public String getPkOrgLk() {
        return pkOrgLk;
    }

    public String getPkDeptLk() {
        return pkDeptLk;
    }

    public String getPkStoreLk() {
        return pkStoreLk;
    }

    public String getPkSupplyer() {
        return pkSupplyer;
    }

    public String getPkPdpu() {
        return pkPdpu;
    }

    public String getPkPdplan() {
        return pkPdplan;
    }

    public String getEceiptNo() {
        return eceiptNo;
    }

    public String getFlagPay() {
        return flagPay;
    }

    public String getPkEmpOp() {
        return pkEmpOp;
    }

    public String getNameEmpOp() {
        return nameEmpOp;
    }

    public String getFlagChk() {
        return flagChk;
    }

    public String getPkEmpChk() {
        return pkEmpChk;
    }

    public String getNameEmpChk() {
        return nameEmpChk;
    }

    public Date getDateChk() {
        return dateChk;
    }

    public String getNote() {
        return note;
    }

    public String getPkPdstSr() {
        return pkPdstSr;
    }

    public String getFlagPu() {
        return flagPu;
    }

    public String getCodeRtn() {
        return codeRtn;
    }

    public String getCreator() {
        return creator;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public String getModifier() {
        return modifier;
    }

    public Date getModityTime() {
        return modityTime;
    }

    public String getDelFlag() {
        return delFlag;
    }

    public Date getTs() {
        return ts;
    }

    public void setPkPdst(String pkPdst) {
        this.pkPdst = pkPdst;
    }

    public void setPkOrg(String pkOrg) {
        this.pkOrg = pkOrg;
    }

    public void setPkDeptSt(String pkDeptSt) {
        this.pkDeptSt = pkDeptSt;
    }

    public void setPkStoreSt(String pkStoreSt) {
        this.pkStoreSt = pkStoreSt;
    }

    public void setDtSttype(String dtSttype) {
        this.dtSttype = dtSttype;
    }

    public void setCodeSt(String codeSt) {
        this.codeSt = codeSt;
    }

    public void setNameSt(String nameSt) {
        this.nameSt = nameSt;
    }

    public void setEuDirect(String euDirect) {
        this.euDirect = euDirect;
    }

    public void setDateSt(Date dateSt) {
        this.dateSt = dateSt;
    }

    public void setEuStatus(String euStatus) {
        this.euStatus = euStatus;
    }

    public void setPkOrgLk(String pkOrgLk) {
        this.pkOrgLk = pkOrgLk;
    }

    public void setPkDeptLk(String pkDeptLk) {
        this.pkDeptLk = pkDeptLk;
    }

    public void setPkStoreLk(String pkStoreLk) {
        this.pkStoreLk = pkStoreLk;
    }

    public void setPkSupplyer(String pkSupplyer) {
        this.pkSupplyer = pkSupplyer;
    }

    public void setPkPdpu(String pkPdpu) {
        this.pkPdpu = pkPdpu;
    }

    public void setPkPdplan(String pkPdplan) {
        this.pkPdplan = pkPdplan;
    }

    public void setEceiptNo(String eceiptNo) {
        this.eceiptNo = eceiptNo;
    }

    public void setFlagPay(String flagPay) {
        this.flagPay = flagPay;
    }

    public void setPkEmpOp(String pkEmpOp) {
        this.pkEmpOp = pkEmpOp;
    }

    public void setNameEmpOp(String nameEmpOp) {
        this.nameEmpOp = nameEmpOp;
    }

    public void setFlagChk(String flagChk) {
        this.flagChk = flagChk;
    }

    public void setPkEmpChk(String pkEmpChk) {
        this.pkEmpChk = pkEmpChk;
    }

    public void setNameEmpChk(String nameEmpChk) {
        this.nameEmpChk = nameEmpChk;
    }

    public void setDateChk(Date dateChk) {
        this.dateChk = dateChk;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public void setPkPdstSr(String pkPdstSr) {
        this.pkPdstSr = pkPdstSr;
    }

    public void setFlagPu(String flagPu) {
        this.flagPu = flagPu;
    }

    public void setCodeRtn(String codeRtn) {
        this.codeRtn = codeRtn;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public void setModifier(String modifier) {
        this.modifier = modifier;
    }

    public void setModityTime(Date modityTime) {
        this.modityTime = modityTime;
    }

    public void setDelFlag(String delFlag) {
        this.delFlag = delFlag;
    }

    public void setTs(Date ts) {
        this.ts = ts;
    }
}
