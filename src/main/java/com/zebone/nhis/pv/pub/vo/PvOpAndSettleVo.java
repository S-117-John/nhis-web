package com.zebone.nhis.pv.pub.vo;

import com.zebone.nhis.common.module.bl.BlDeposit;
import com.zebone.nhis.common.module.bl.BlInvoiceDt;
import com.zebone.nhis.common.module.bl.BlSettleDetail;
import com.zebone.nhis.common.module.bl.opcg.BlOpDt;

import java.util.Date;
import java.util.List;

/**
 * 获取门诊患者基本信息以及挂号费用信息返回类：包含患者基本信息、就诊信息、结算信息、交款信息
 * 
 * @author wangpeng
 * @date 2016年9月27日
 *
 */
public class PvOpAndSettleVo {
	
	/** 排序号 */
	private String orderNo;
	
	/** 就诊主键*/
	private String pkPv;
	
	/** 名族 */
	private String dtNation;
	
	/** 名族名称 */
	private String dtNationName;
	
	/** 手机号码 */
	private String telNo;
	
	/** 号码 */
	private String mobile;
	
	/** 证件类型 */
	private String dtIdtype;
	
	/** 证件类型名称 */
	private String dtIdtypeName;
	
	/** 证件号码 */
	private String idNo;
	
	/** 出生日期 */
	private Date birthDate;
	
	/** 年龄 */
	private String agePv;
	
	/** 地址 */
	private String address;
	
	/** 医保号码 */
	private String insurNo;
	
	/** 就诊服务 */
	private String pkSchsrv;
	
	/** 服务名称 */
	private String schsrvName;

	public Date getDateBegin() {
		return dateBegin;
	}

	public void setDateBegin(Date dateBegin) {
		this.dateBegin = dateBegin;
	}

	/** 就诊时间 */
	private Date dateBegin;
	/** 就诊次数 */
	private Long opTimes;
	
	/** 科室 */
	private String pkDeptPv;
	
	/** 科室名称 */
	private String deptPvName;
	
	/** 医生 */
	private String pkEmpPv;
	
	/** 医生姓名 */
	private String nameEmpPv;
	
	/** 退号日期 */
	private Date dateCancel;
	
	/** 退号人 */
	private String pkEmpCancel;
	
	/** 退号人姓名 */
	private String nameEmpCancel;
	
	/** 排队号 */
	private Long ticketno;
	
	/** 发票号 */
	private String codeInv;
	
	/** 结算明细 */
	private List<BlSettleDetail> settleList;
	
	/** 付款方式 */
	private List<BlDeposit> depositList;
	
	/** 门急诊收费明细 */
	private List<BlOpDt> opDtList;
	
	/** 收费主键 **/
	private String pkSettle;
	
	/** 收费结算-发票明细 */
	private List<BlInvoiceDt> blInvoiceDtList;
	
	/** 就诊号**/
	private String codePv;
	
	/** 排班资源名称**/
	private String schResName;
	
	public String getOrderNo() {
		return orderNo;
	}

	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}

	public String getPkPv() {
		return pkPv;
	}

	public void setPkPv(String pkPv) {
		this.pkPv = pkPv;
	}

	public String getDtNation() {
		return dtNation;
	}

	public void setDtNation(String dtNation) {
		this.dtNation = dtNation;
	}

	public String getDtNationName() {
		return dtNationName;
	}

	public void setDtNationName(String dtNationName) {
		this.dtNationName = dtNationName;
	}

	public String getTelNo() {
		return telNo;
	}

	public void setTelNo(String telNo) {
		this.telNo = telNo;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getDtIdtype() {
		return dtIdtype;
	}

	public void setDtIdtype(String dtIdtype) {
		this.dtIdtype = dtIdtype;
	}

	public String getDtIdtypeName() {
		return dtIdtypeName;
	}

	public void setDtIdtypeName(String dtIdtypeName) {
		this.dtIdtypeName = dtIdtypeName;
	}

	public String getIdNo() {
		return idNo;
	}

	public void setIdNo(String idNo) {
		this.idNo = idNo;
	}

	public Date getBirthDate() {
		return birthDate;
	}

	public void setBirthDate(Date birthDate) {
		this.birthDate = birthDate;
	}

	public String getAgePv() {
		return agePv;
	}

	public void setAgePv(String agePv) {
		this.agePv = agePv;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getInsurNo() {
		return insurNo;
	}

	public void setInsurNo(String insurNo) {
		this.insurNo = insurNo;
	}

	public String getPkSchsrv() {
		return pkSchsrv;
	}

	public void setPkSchsrv(String pkSchsrv) {
		this.pkSchsrv = pkSchsrv;
	}

	public String getSchsrvName() {
		return schsrvName;
	}

	public void setSchsrvName(String schsrvName) {
		this.schsrvName = schsrvName;
	}

	public Long getOpTimes() {
		return opTimes;
	}

	public void setOpTimes(Long opTimes) {
		this.opTimes = opTimes;
	}

	public String getPkDeptPv() {
		return pkDeptPv;
	}

	public void setPkDeptPv(String pkDeptPv) {
		this.pkDeptPv = pkDeptPv;
	}

	public String getDeptPvName() {
		return deptPvName;
	}

	public void setDeptPvName(String deptPvName) {
		this.deptPvName = deptPvName;
	}

	public String getPkEmpPv() {
		return pkEmpPv;
	}

	public void setPkEmpPv(String pkEmpPv) {
		this.pkEmpPv = pkEmpPv;
	}

	public String getNameEmpPv() {
		return nameEmpPv;
	}

	public void setNameEmpPv(String nameEmpPv) {
		this.nameEmpPv = nameEmpPv;
	}

	public Date getDateCancel() {
		return dateCancel;
	}

	public void setDateCancel(Date dateCancel) {
		this.dateCancel = dateCancel;
	}

	public String getPkEmpCancel() {
		return pkEmpCancel;
	}

	public void setPkEmpCancel(String pkEmpCancel) {
		this.pkEmpCancel = pkEmpCancel;
	}

	public String getNameEmpCancel() {
		return nameEmpCancel;
	}

	public void setNameEmpCancel(String nameEmpCancel) {
		this.nameEmpCancel = nameEmpCancel;
	}

	public Long getTicketno() {
		return ticketno;
	}

	public void setTicketno(Long ticketno) {
		this.ticketno = ticketno;
	}

	public String getCodeInv() {
		return codeInv;
	}

	public void setCodeInv(String codeInv) {
		this.codeInv = codeInv;
	}

	public List<BlSettleDetail> getSettleList() {
		return settleList;
	}

	public void setSettleList(List<BlSettleDetail> settleList) {
		this.settleList = settleList;
	}

	public List<BlDeposit> getDepositList() {
		return depositList;
	}

	public void setDepositList(List<BlDeposit> depositList) {
		this.depositList = depositList;
	}

	public List<BlOpDt> getOpDtList() {
		return opDtList;
	}

	public void setOpDtList(List<BlOpDt> opDtList) {
		this.opDtList = opDtList;
	}

	public List<BlInvoiceDt> getBlInvoiceDtList() {
		return blInvoiceDtList;
	}

	public void setBlInvoiceDtList(List<BlInvoiceDt> blInvoiceDtList) {
		this.blInvoiceDtList = blInvoiceDtList;
	}

	public String getPkSettle() {
		return pkSettle;
	}

	public void setPkSettle(String pkSettle) {
		this.pkSettle = pkSettle;
	}

	public String getSchResName() {
		return schResName;
	}

	public void setSchResName(String schResName) {
		this.schResName = schResName;
	}

	public String getCodePv() {
		return codePv;
	}

	public void setCodePv(String codePv) {
		this.codePv = codePv;
	}

}
