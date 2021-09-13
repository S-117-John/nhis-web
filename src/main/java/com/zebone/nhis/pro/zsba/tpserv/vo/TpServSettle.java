package com.zebone.nhis.pro.zsba.tpserv.vo;

import java.math.BigDecimal;
import java.util.Date;

import com.zebone.nhis.common.module.BaseModule;
import com.zebone.platform.modules.dao.build.au.Field;
import com.zebone.platform.modules.dao.build.au.Field.FieldType;
import com.zebone.platform.modules.dao.build.au.Field.KeyId;
import com.zebone.platform.modules.dao.build.au.PK;
import com.zebone.platform.modules.dao.build.au.Table;


/**
 * Table: tp_serv_settle - 结算记录 
 *
 * @since 2017-09-07 12:29:02
 */
@Table(value="TP_SERV_SETTLE")
public class TpServSettle  extends BaseModule{

	/**
	 * 
	 */
	private static final long serialVersionUID = 2603385281435626917L;

	@PK
	@Field(value="PK_SERV_SETTLE",id=KeyId.UUID)
    private String pkServSettle;

    /** PK_PI - 患者主键 */
	@Field(value="PK_PI")
    private String pkPi;
	
	/** PK_PV - 就诊主键 */
	@Field(value="pk_pv")
    private String pkPv;
	
	/** IP_TIMES - 住院次数 */
	@Field(value="ip_times")
    private Integer ipTimes;
	
	/** OP_TIMES - 就诊次数 */
	@Field(value="op_times")
    private Integer opTimes;
	
    /** AMOUNT_ST - 结算日期 */
	@Field(value="AMOUNT_ST")
    private BigDecimal amountSt;
	
    /** DATE_ST - 结算日期 */
	@Field(value="DATE_ST")
    private Date dateSt;

    /** PK_ORG_ST - 结算机构 */
	@Field(value="PK_ORG_ST")
    private String pkOrgSt;

    /** PK_DEPT_ST - 结算部门 */
	@Field(value="PK_DEPT_ST")
    private String pkDeptSt;

	/** PK_EMP_ST - 结算人员 */
	@Field(value="PK_EMP_ST")
    private String pkEmpSt;
	
	/** NAME_EMP_ST - 结算人员姓名 */
	@Field(value="NAME_EMP_ST")
	private String nameEmpSt;
	
    /** CANC_FLAG - 取消标志 */
	@Field(value="CANC_FLAG")
    private String cancFlag;

    /** REASON_CANC - 取消原因描述 */
	@Field(value="REASON_CANC")
    private String reasonCanc;
	
    /** PK_SERV_SETTLE_CANC - 取消关联结算主键 */
	@Field(value="PK_SERV_SETTLE_CANC")
    private String pkServSettleCanc;
	
    /** MODITY_TIME - 修改时间 */
	@Field(value="MODITY_TIME",date=FieldType.UPDATE)
    private Date modityTime;

	public String getPkServSettle() {
		return pkServSettle;
	}

	public void setPkServSettle(String pkServSettle) {
		this.pkServSettle = pkServSettle;
	}

	public String getPkPi() {
		return pkPi;
	}

	public void setPkPi(String pkPi) {
		this.pkPi = pkPi;
	}

	public String getPkPv() {
		return pkPv;
	}

	public void setPkPv(String pkPv) {
		this.pkPv = pkPv;
	}

	public Integer getIpTimes() {
		return ipTimes;
	}

	public void setIpTimes(Integer ipTimes) {
		this.ipTimes = ipTimes;
	}

	public Integer getOpTimes() {
		return opTimes;
	}

	public void setOpTimes(Integer opTimes) {
		this.opTimes = opTimes;
	}
	
	public BigDecimal getAmountSt() {
		return amountSt;
	}

	public void setAmountSt(BigDecimal amountSt) {
		this.amountSt = amountSt;
	}

	public Date getDateSt() {
		return dateSt;
	}

	public void setDateSt(Date dateSt) {
		this.dateSt = dateSt;
	}

	public String getPkOrgSt() {
		return pkOrgSt;
	}

	public void setPkOrgSt(String pkOrgSt) {
		this.pkOrgSt = pkOrgSt;
	}

	public String getPkDeptSt() {
		return pkDeptSt;
	}

	public void setPkDeptSt(String pkDeptSt) {
		this.pkDeptSt = pkDeptSt;
	}

	public String getPkEmpSt() {
		return pkEmpSt;
	}

	public void setPkEmpSt(String pkEmpSt) {
		this.pkEmpSt = pkEmpSt;
	}

	public String getNameEmpSt() {
		return nameEmpSt;
	}

	public void setNameEmpSt(String nameEmpSt) {
		this.nameEmpSt = nameEmpSt;
	}

	public String getCancFlag() {
		return cancFlag;
	}

	public void setCancFlag(String cancFlag) {
		this.cancFlag = cancFlag;
	}

	public String getReasonCanc() {
		return reasonCanc;
	}

	public void setReasonCanc(String reasonCanc) {
		this.reasonCanc = reasonCanc;
	}

	public String getPkServSettleCanc() {
		return pkServSettleCanc;
	}

	public void setPkServSettleCanc(String pkServSettleCanc) {
		this.pkServSettleCanc = pkServSettleCanc;
	}

	public Date getModityTime() {
		return modityTime;
	}

	public void setModityTime(Date modityTime) {
		this.modityTime = modityTime;
	}
}