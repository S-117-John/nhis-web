package com.zebone.nhis.common.module.scm.st;

import java.util.Date;

import com.zebone.nhis.common.module.BaseModule;
import com.zebone.platform.modules.dao.build.au.Field;
import com.zebone.platform.modules.dao.build.au.PK;
import com.zebone.platform.modules.dao.build.au.Table;
import com.zebone.platform.modules.dao.build.au.Field.KeyId;


/**
 * Table: PD_ST - pd_st 
 *
 * @since 2016-11-03 10:03:30
 */
@Table(value="PD_ST")
public class PdSt extends BaseModule  {

	@PK
	@Field(value="PK_PDST",id=KeyId.UUID)
    private String pkPdst;

	@Field(value="PK_DEPT_ST")
    private String pkDeptSt;

	@Field(value="PK_STORE_ST")
    private String pkStoreSt;

    /** DT_STTYPE - 码表080008 */
	@Field(value="DT_STTYPE")
    private String dtSttype;

	@Field(value="CODE_ST")
    private String codeSt;

	@Field(value="NAME_ST")
    private String nameSt;

    /** EU_DIRECT - 1入库，-1出库 */
	@Field(value="EU_DIRECT")
    private String euDirect;

	@Field(value="DATE_ST")
    private Date dateSt;

    /** EU_STATUS - 0 制单 1 审核 */
	@Field(value="EU_STATUS")
    private String euStatus;

	@Field(value="PK_ORG_LK")
    private String pkOrgLk;

	@Field(value="PK_DEPT_LK")
    private String pkDeptLk;

	@Field(value="PK_STORE_LK")
    private String pkStoreLk;

	@Field(value="PK_SUPPLYER")
    private String pkSupplyer;

	@Field(value="PK_PDPU")
    private String pkPdpu;

	@Field(value="PK_PDPLAN")
    private String pkPdplan;

	@Field(value="RECEIPT_NO")
    private String receiptNo;

	@Field(value="FLAG_PAY")
    private String flagPay;

	@Field(value="PK_EMP_OP")
    private String pkEmpOp;

	@Field(value="NAME_EMP_OP")
    private String nameEmpOp;

	@Field(value="FLAG_CHK")
    private String flagChk;

	@Field(value="PK_EMP_CHK")
    private String pkEmpChk;

	@Field(value="NAME_EMP_CHK")
    private String nameEmpChk;

	@Field(value="DATE_CHK")
    private Date dateChk;

	@Field(value="NOTE")
    private String note;

	@Field(value="PK_PDST_SR")
    private String pkPdstSr;

	@Field(value="FLAG_PU")
    private String flagPu;

	@Field(value="MODITY_TIME")
    private Date modityTime;


    public String getPkPdst(){
        return this.pkPdst;
    }
    public void setPkPdst(String pkPdst){
        this.pkPdst = pkPdst;
    }

    public String getPkDeptSt(){
        return this.pkDeptSt;
    }
    public void setPkDeptSt(String pkDeptSt){
        this.pkDeptSt = pkDeptSt;
    }

    public String getPkStoreSt(){
        return this.pkStoreSt;
    }
    public void setPkStoreSt(String pkStoreSt){
        this.pkStoreSt = pkStoreSt;
    }

    public String getDtSttype(){
        return this.dtSttype;
    }
    public void setDtSttype(String dtSttype){
        this.dtSttype = dtSttype;
    }

    public String getCodeSt(){
        return this.codeSt;
    }
    public void setCodeSt(String codeSt){
        this.codeSt = codeSt;
    }

    public String getNameSt(){
        return this.nameSt;
    }
    public void setNameSt(String nameSt){
        this.nameSt = nameSt;
    }

    public String getEuDirect(){
        return this.euDirect;
    }
    public void setEuDirect(String euDirect){
        this.euDirect = euDirect;
    }

    public Date getDateSt(){
        return this.dateSt;
    }
    public void setDateSt(Date dateSt){
        this.dateSt = dateSt;
    }

    public String getEuStatus(){
        return this.euStatus;
    }
    public void setEuStatus(String euStatus){
        this.euStatus = euStatus;
    }

    public String getPkOrgLk(){
        return this.pkOrgLk;
    }
    public void setPkOrgLk(String pkOrgLk){
        this.pkOrgLk = pkOrgLk;
    }

    public String getPkDeptLk(){
        return this.pkDeptLk;
    }
    public void setPkDeptLk(String pkDeptLk){
        this.pkDeptLk = pkDeptLk;
    }

    public String getPkStoreLk(){
        return this.pkStoreLk;
    }
    public void setPkStoreLk(String pkStoreLk){
        this.pkStoreLk = pkStoreLk;
    }

    public String getPkSupplyer(){
        return this.pkSupplyer;
    }
    public void setPkSupplyer(String pkSupplyer){
        this.pkSupplyer = pkSupplyer;
    }

    public String getPkPdpu(){
        return this.pkPdpu;
    }
    public void setPkPdpu(String pkPdpu){
        this.pkPdpu = pkPdpu;
    }

    public String getPkPdplan(){
        return this.pkPdplan;
    }
    public void setPkPdplan(String pkPdplan){
        this.pkPdplan = pkPdplan;
    }

    public String getReceiptNo(){
        return this.receiptNo;
    }
    public void setReceiptNo(String receiptNo){
        this.receiptNo = receiptNo;
    }

    public String getFlagPay(){
        return this.flagPay;
    }
    public void setFlagPay(String flagPay){
        this.flagPay = flagPay;
    }

    public String getPkEmpOp(){
        return this.pkEmpOp;
    }
    public void setPkEmpOp(String pkEmpOp){
        this.pkEmpOp = pkEmpOp;
    }

    public String getNameEmpOp(){
        return this.nameEmpOp;
    }
    public void setNameEmpOp(String nameEmpOp){
        this.nameEmpOp = nameEmpOp;
    }

    public String getFlagChk(){
        return this.flagChk;
    }
    public void setFlagChk(String flagChk){
        this.flagChk = flagChk;
    }

    public String getPkEmpChk(){
        return this.pkEmpChk;
    }
    public void setPkEmpChk(String pkEmpChk){
        this.pkEmpChk = pkEmpChk;
    }

    public String getNameEmpChk(){
        return this.nameEmpChk;
    }
    public void setNameEmpChk(String nameEmpChk){
        this.nameEmpChk = nameEmpChk;
    }

    public Date getDateChk(){
        return this.dateChk;
    }
    public void setDateChk(Date dateChk){
        this.dateChk = dateChk;
    }

    public String getNote(){
        return this.note;
    }
    public void setNote(String note){
        this.note = note;
    }

    public String getPkPdstSr(){
        return this.pkPdstSr;
    }
    public void setPkPdstSr(String pkPdstSr){
        this.pkPdstSr = pkPdstSr;
    }

    public String getFlagPu(){
        return this.flagPu;
    }
    public void setFlagPu(String flagPu){
        this.flagPu = flagPu;
    }

    public Date getModityTime(){
        return this.modityTime;
    }
    public void setModityTime(Date modityTime){
        this.modityTime = modityTime;
    }
}