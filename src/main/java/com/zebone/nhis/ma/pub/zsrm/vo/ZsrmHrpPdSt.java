package com.zebone.nhis.ma.pub.zsrm.vo;

import com.zebone.platform.modules.dao.build.au.Field;
import com.zebone.platform.modules.dao.build.au.PK;
import com.zebone.platform.modules.dao.build.au.Table;

@Table("pd_st")
public class ZsrmHrpPdSt {

    /**
     * 库单主键
     */
    @PK
    @Field("pk_pdst")
    private String pkPdst;
    /**
     * 当前机构
     */
    @Field("pk_org")
    private String pkOrg;

    /**
     * 当前仓库所属的部门
     */
    @Field("pk_dept_st")
    private String pkDeptSt;

    /**
     * 当前仓库
     */
    @Field("pk_store_st")
    private String pkStoreSt;

    /**
     * 业务类型
     */
    @Field("dt_sttype")
    private String dtSttype;

    /**
     * 出库单号
     */
    @Field("code_st")
    private String codeSt;

    /**
     * 库单名称
     */
    @Field("name_st")
    private String nameSt;

    /**
     * 出入库方向
     */
    @Field("eu_direct")
    private String euDirect;

    /**
     * 库单日期
     */
    @Field("date_st")
    private String dateSt;

    /**
     * 库单状态
     */
    @Field("eu_status")
    private String euStatus;

    /**
     * 对应的入库机构
     */
    @Field("pk_org_lk")
    private String pkOrgLk;

    /**
     * 对应的入库部门
     */
    @Field("pk_dept_lk")
    private String pkDeptLk;

    /**
     * 对应的入库仓库
     */
    @Field("pk_store_lk")
    private String pkStoreLk;

    /**
     * 供应商
     */
    @Field("pk_supplyer")
    private String pkSupplyer;

    /**
     * 采购订单
     */
    @Field("pk_pdpu")
    private String pkPdpu;

    /**
     * 请领单
     */
    @Field("pk_pdplan")
    private String pkPdplan;

    /**
     * 发票号
     */
    @Field("eceipt_no")
    private String eceiptNo;


    /**
     * 付款完成标志
     */
    @Field("flag_pay")
    private Integer flagPay;

    /**
     * 当前用户主键（经办人）
     */
    @Field("pk_emp_op")
    private String pkEmpOp;

    /**
     * 当前用户姓名
     */
    @Field("name_emp_op")
    private String nameEmpOp;

    /**
     * 审核标志
     */
    @Field("flag_chk")
    private Integer flagChk;

    /**
     * 审核人
     */
    @Field("pk_emp_chk")
    private String pkEmpChk;

    /**
     * 审核人姓名
     */
    @Field("name_emp_chk")
    private String nameEmpChk;

    /**
     * 审核时间
     */
    @Field("date_chk")
    private String dateChk;

    /**
     * 备注
     */
    @Field("note")
    private String note;

    /**
     * 源单据主键
     */
    @Field("pk_pdst_sr")
    private String pkPdstSr;

    /**
     * 采购标志
     */
    @Field("flag_pu")
    private Integer flagPu;


    /**
     * 平台返回出入
     */
    @Field("code_rtn")
    private String codeRtn;

    /**
     * 创建人
     */
    @Field("creator")
    private String creator;

    /**
     * 创建时间
     */
    @Field("create_time")
    private String createTime;

    /**
     * 修改人
     */
    @Field("modifier")
    private String modifier;

    /**
     * 修改时间
     */
    @Field("modity_time")
    private String modityTime;

    /**
     * 删除标志
     */
    @Field("del_flag")
    private String delFlag;

    /**
     * 时间戳
     */
    @Field("ts")
    private String ts;

    public ZsrmHrpPdSt() {
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

    public String getDateSt() {
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

    public Integer getFlagPay() {
        return flagPay;
    }

    public String getPkEmpOp() {
        return pkEmpOp;
    }

    public String getNameEmpOp() {
        return nameEmpOp;
    }

    public Integer getFlagChk() {
        return flagChk;
    }

    public String getPkEmpChk() {
        return pkEmpChk;
    }

    public String getNameEmpChk() {
        return nameEmpChk;
    }

    public String getDateChk() {
        return dateChk;
    }

    public String getNote() {
        return note;
    }

    public String getPkPdstSr() {
        return pkPdstSr;
    }

    public Integer getFlagPu() {
        return flagPu;
    }

    public String getCodeRtn() {
        return codeRtn;
    }

    public String getCreator() {
        return creator;
    }

    public String getCreateTime() {
        return createTime;
    }

    public String getModifier() {
        return modifier;
    }

    public String getModityTime() {
        return modityTime;
    }

    public String getDelFlag() {
        return delFlag;
    }

    public String getTs() {
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

    public void setDateSt(String dateSt) {
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

    public void setFlagPay(Integer flagPay) {
        this.flagPay = flagPay;
    }

    public void setPkEmpOp(String pkEmpOp) {
        this.pkEmpOp = pkEmpOp;
    }

    public void setNameEmpOp(String nameEmpOp) {
        this.nameEmpOp = nameEmpOp;
    }

    public void setFlagChk(Integer flagChk) {
        this.flagChk = flagChk;
    }

    public void setPkEmpChk(String pkEmpChk) {
        this.pkEmpChk = pkEmpChk;
    }

    public void setNameEmpChk(String nameEmpChk) {
        this.nameEmpChk = nameEmpChk;
    }

    public void setDateChk(String dateChk) {
        this.dateChk = dateChk;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public void setPkPdstSr(String pkPdstSr) {
        this.pkPdstSr = pkPdstSr;
    }

    public void setFlagPu(Integer flagPu) {
        this.flagPu = flagPu;
    }

    public void setCodeRtn(String codeRtn) {
        this.codeRtn = codeRtn;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public void setModifier(String modifier) {
        this.modifier = modifier;
    }

    public void setModityTime(String modityTime) {
        this.modityTime = modityTime;
    }

    public void setDelFlag(String delFlag) {
        this.delFlag = delFlag;
    }

    public void setTs(String ts) {
        this.ts = ts;
    }
}
