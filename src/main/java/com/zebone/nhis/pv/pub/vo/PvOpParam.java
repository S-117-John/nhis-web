package com.zebone.nhis.pv.pub.vo;

import java.math.BigDecimal;
import java.util.List;

import com.zebone.nhis.common.module.bl.BlDeposit;
import com.zebone.nhis.common.module.bl.opcg.BlOpDt;
import com.zebone.nhis.common.module.pv.PvInsurance;

/**
 * 患者挂号参数
 * 
 * @author wangpeng
 * @date 2016年9月23日
 * 
 */
public class PvOpParam {
	
	/** 排序号 */
	private String orderNo;

	/** 患者主键 */
	private String pkPi;

	/** 患者分类 */
	private String pkPicate;

	/** 排班服务主键 */
	private String pkSchsrv;
	
	/** 服务类型 */
	private String euSrvtype;

	/** 排班资源主键 */
	private String pkRes;

	/** 日期分组主键 */
	private String pkDateslot;

	/** 对应排班主键 */
	private String pkSch;

	/** 对应预约主键 */
	private String pkAppo;
	
	/** 就诊类型 */
	private String euPvtype;

	/** 医保主计划 */
	private String pkInsu;

	/** 挂号科室 */
	private String pkDeptPv;

	/** 挂号医生 */
	private String pkEmpPv;

	/** 挂号医生姓名 */
	private String nameEmpPv;

	/**
	 * 发票领用主键
	 */
	private String pkEmpinv;

	/**
	 * 票据分类主键
	 */
	private String pkInvcate;

	/**
	 * 发票号码
	 */
	private String codeInv;

	/** 医保计划列表 */
	List<PvInsurance> insuList;

	/** 收费明细列表 */
	List<BlOpDt> opDtList;

	/** 交款记录列表 */
	List<BlDeposit> depositList;
	
	/** 是否初诊标记*/
	private String flagFirst;
	
	/**
	 * 第三方医保支付金额
	 */
	private BigDecimal amtInsuThird;
	

	public BigDecimal getAmtInsuThird() {
		return amtInsuThird;
	}

	public void setAmtInsuThird(BigDecimal amtInsuThird) {
		this.amtInsuThird = amtInsuThird;
	}

	public String getFlagFirst() {
		return flagFirst;
	}

	public void setFlagFirst(String flagFirst) {
		this.flagFirst = flagFirst;
	}

	public String getOrderNo() {
		return orderNo;
	}

	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}

	public String getPkPi() {

		return pkPi;
	}

	public void setPkPi(String pkPi) {

		this.pkPi = pkPi;
	}

	public String getPkPicate() {

		return pkPicate;
	}

	public void setPkPicate(String pkPicate) {

		this.pkPicate = pkPicate;
	}

	public String getPkSchsrv() {

		return pkSchsrv;
	}

	public void setPkSchsrv(String pkSchsrv) {

		this.pkSchsrv = pkSchsrv;
	}

	public String getEuSrvtype() {
		return euSrvtype;
	}

	public void setEuSrvtype(String euSrvtype) {
		this.euSrvtype = euSrvtype;
	}

	public String getPkRes() {

		return pkRes;
	}

	public void setPkRes(String pkRes) {

		this.pkRes = pkRes;
	}

	public String getPkDateslot() {

		return pkDateslot;
	}

	public void setPkDateslot(String pkDateslot) {

		this.pkDateslot = pkDateslot;
	}

	public String getPkSch() {

		return pkSch;
	}

	public void setPkSch(String pkSch) {

		this.pkSch = pkSch;
	}

	public String getPkAppo() {

		return pkAppo;
	}

	public void setPkAppo(String pkAppo) {

		this.pkAppo = pkAppo;
	}

	public String getEuPvtype() {
		return euPvtype;
	}

	public void setEuPvtype(String euPvtype) {
		this.euPvtype = euPvtype;
	}

	public String getPkInsu() {

		return pkInsu;
	}

	public void setPkInsu(String pkInsu) {

		this.pkInsu = pkInsu;
	}

	public String getPkDeptPv() {

		return pkDeptPv;
	}

	public void setPkDeptPv(String pkDeptPv) {

		this.pkDeptPv = pkDeptPv;
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

	public List<PvInsurance> getInsuList() {

		return insuList;
	}

	public void setInsuList(List<PvInsurance> insuList) {

		this.insuList = insuList;
	}

	public List<BlOpDt> getOpDtList() {

		return opDtList;
	}

	public void setOpDtList(List<BlOpDt> opDtList) {

		this.opDtList = opDtList;
	}

	public List<BlDeposit> getDepositList() {

		return depositList;
	}

	public void setDepositList(List<BlDeposit> depositList) {

		this.depositList = depositList;
	}

	public String getPkEmpinv() {

		return pkEmpinv;
	}

	public void setPkEmpinv(String pkEmpinv) {

		this.pkEmpinv = pkEmpinv;
	}

	public String getPkInvcate() {

		return pkInvcate;
	}

	public void setPkInvcate(String pkInvcate) {

		this.pkInvcate = pkInvcate;
	}

	public String getCodeInv() {

		return codeInv;
	}

	public void setCodeInv(String codeInv) {

		this.codeInv = codeInv;
	}

}
