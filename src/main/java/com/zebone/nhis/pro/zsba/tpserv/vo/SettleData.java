package com.zebone.nhis.pro.zsba.tpserv.vo;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;



/**
 * 非医疗费用结算信息
 *  
 * @author zhengrj
 * @date 2017年10月17日
 *
 */
public class SettleData {
	
	/** 就诊主键 */
	private String pkPv;
	
	/** 结算类型 (0:出院 1：中途) */
	private String euSttype;
	
	/** 患者住院天数(护工费天数) */
	private int hzzyts;
	
	/** 护工费 */
	private BigDecimal hgf;
	
	/** 项目出租信息 */
	private List<TpServItemRent> rentList;
	
	/** 需缴纳金额 */
	private BigDecimal payAmount;
	
	/** 结算方式 1:缴费 2：抵消 3：退费(包含抵消加退费) */
	private String settleType;
	
	/** 支付方式 1:银联 2：支付宝 3：龙闪付 4：现金 */
	private String payType;
	
	/** 银联交易记录 */
	private TpServUnionpayTrading unionpayTrading;

	private Date dateBegin;
	
	private Date dateEnd;
	
	/** 结算机构 */
	private String pkOrgSt;
	
	/** 收付款部门 */
	private String pkDept;
	
	/** 收款人 */
	private String pkEmpPay;
	
	/** 收款人名称 */
	private String nameEmpPay;

	/** 操作员编码 */
	private String codeEmp;
	
	public String getPkPv() {
		return pkPv;
	}

	public void setPkPv(String pkPv) {
		this.pkPv = pkPv;
	}

	public String getEuSttype() {
		return euSttype;
	}

	public void setEuSttype(String euSttype) {
		this.euSttype = euSttype;
	}

	public int getHzzyts() {
		return hzzyts;
	}

	public void setHzzyts(int hzzyts) {
		this.hzzyts = hzzyts;
	}

	public BigDecimal getHgf() {
		return hgf;
	}

	public void setHgf(BigDecimal hgf) {
		this.hgf = hgf;
	}

	public List<TpServItemRent> getRentList() {
		return rentList;
	}

	public void setRentList(List<TpServItemRent> rentList) {
		this.rentList = rentList;
	}

	public BigDecimal getPayAmount() {
		return payAmount;
	}

	public void setPayAmount(BigDecimal payAmount) {
		this.payAmount = payAmount;
	}

	public String getSettleType() {
		return settleType;
	}

	public void setSettleType(String settleType) {
		this.settleType = settleType;
	}

	public String getPayType() {
		return payType;
	}

	public void setPayType(String payType) {
		this.payType = payType;
	}

	public TpServUnionpayTrading getUnionpayTrading() {
		return unionpayTrading;
	}

	public void setUnionpayTrading(TpServUnionpayTrading unionpayTrading) {
		this.unionpayTrading = unionpayTrading;
	}

	public Date getDateBegin() {
		return dateBegin;
	}

	public void setDateBegin(Date dateBegin) {
		this.dateBegin = dateBegin;
	}

	public Date getDateEnd() {
		return dateEnd;
	}

	public void setDateEnd(Date dateEnd) {
		this.dateEnd = dateEnd;
	}

	public String getPkOrgSt() {
		return pkOrgSt;
	}

	public void setPkOrgSt(String pkOrgSt) {
		this.pkOrgSt = pkOrgSt;
	}

	public String getPkDept() {
		return pkDept;
	}

	public void setPkDept(String pkDept) {
		this.pkDept = pkDept;
	}

	public String getPkEmpPay() {
		return pkEmpPay;
	}

	public void setPkEmpPay(String pkEmpPay) {
		this.pkEmpPay = pkEmpPay;
	}

	public String getNameEmpPay() {
		return nameEmpPay;
	}

	public void setNameEmpPay(String nameEmpPay) {
		this.nameEmpPay = nameEmpPay;
	}

	public String getCodeEmp() {
		return codeEmp;
	}

	public void setCodeEmp(String codeEmp) {
		this.codeEmp = codeEmp;
	}
}
