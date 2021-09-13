package com.zebone.nhis.pro.zsba.compay.ins.qgyb.vo.BottomClass;

/**
 * 医药机构费用结算对总账入参
 * @author Administrator
 *
 */
public class Input3201Data {
	
	private String	insutype;//	Y	险种
	private String	clr_type;//	Y	清算类别
	private String	setl_optins;//	Y　	结算经办机构
	private String	stmt_begndate;//	Y　	对账开始日期
	private String	stmt_enddate;//	Y　	对账结束日期
	private String	medfee_sumamt;//	Y	医疗费总额
	private String	fund_pay_sumamt;//	Y	基金支付总额
	private String	acct_pay;//	Y	个人账户支付金额
	private String	fixmedins_setl_cnt;//	Y　	定点医药机构结算笔数
	private String REFD_SETL_FLAG;// 退费结算标志 REFD_SETL_FLAG为1、空值、null、缺省时总账对账数据包含退费数据，为0时，总账对账数据不包含退费数据
	public String getInsutype() {
		return insutype;
	}
	public void setInsutype(String insutype) {
		this.insutype = insutype;
	}
	public String getClr_type() {
		return clr_type;
	}
	public void setClr_type(String clr_type) {
		this.clr_type = clr_type;
	}
	public String getSetl_optins() {
		return setl_optins;
	}
	public void setSetl_optins(String setl_optins) {
		this.setl_optins = setl_optins;
	}
	public String getStmt_begndate() {
		return stmt_begndate;
	}
	public void setStmt_begndate(String stmt_begndate) {
		this.stmt_begndate = stmt_begndate;
	}
	public String getStmt_enddate() {
		return stmt_enddate;
	}
	public void setStmt_enddate(String stmt_enddate) {
		this.stmt_enddate = stmt_enddate;
	}
	public String getMedfee_sumamt() {
		return medfee_sumamt;
	}
	public void setMedfee_sumamt(String medfee_sumamt) {
		this.medfee_sumamt = medfee_sumamt;
	}
	public String getFund_pay_sumamt() {
		return fund_pay_sumamt;
	}
	public void setFund_pay_sumamt(String fund_pay_sumamt) {
		this.fund_pay_sumamt = fund_pay_sumamt;
	}
	public String getAcct_pay() {
		return acct_pay;
	}
	public void setAcct_pay(String acct_pay) {
		this.acct_pay = acct_pay;
	}
	public String getFixmedins_setl_cnt() {
		return fixmedins_setl_cnt;
	}
	public void setFixmedins_setl_cnt(String fixmedins_setl_cnt) {
		this.fixmedins_setl_cnt = fixmedins_setl_cnt;
	}
	public String getREFD_SETL_FLAG() {
		return REFD_SETL_FLAG;
	}
	public void setREFD_SETL_FLAG(String rEFD_SETL_FLAG) {
		REFD_SETL_FLAG = rEFD_SETL_FLAG;
	}

	
}
