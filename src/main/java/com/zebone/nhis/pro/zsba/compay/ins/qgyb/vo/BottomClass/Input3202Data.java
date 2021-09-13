package com.zebone.nhis.pro.zsba.compay.ins.qgyb.vo.BottomClass;

/**
 * 6.3.2.2【3202】医药机构费用结算对明细账入参
 * @author Administrator
 *
 */
public class Input3202Data {
	
	private String	setl_optins;//	Y　	结算经办机构	
	private String	file_qury_no;//	Y　	文件查询号	上传明细文件后返回的号码
	private String	stmt_begndate;//	Y　	对账开始日期	yyyy-MM-dd
	private String	stmt_enddate;//	Y　	对账结束日期	yyyy-MM-dd
	private String	medfee_sumamt;//	Y	医疗费总额	
	private String	fund_pay_sumamt;//	Y	基金支付总额	
	private String	cash_payamt;//	Y	现金支付金额	
	private String	fixmedins_setl_cnt;//	Y　	定点医药机构结算笔数	
	private String	REFD_SETL_FLAG;//	医保中心退费结算标志
	private String  clr_type;//清算类别  11	门诊	41	药店购药 21	住院	99	其他
	
	public String getSetl_optins() {
		return setl_optins;
	}
	public void setSetl_optins(String setl_optins) {
		this.setl_optins = setl_optins;
	}
	public String getFile_qury_no() {
		return file_qury_no;
	}
	public void setFile_qury_no(String file_qury_no) {
		this.file_qury_no = file_qury_no;
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
	public String getCash_payamt() {
		return cash_payamt;
	}
	public void setCash_payamt(String cash_payamt) {
		this.cash_payamt = cash_payamt;
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
	public String getClr_type() {
		return clr_type;
	}
	public void setClr_type(String clr_type) {
		this.clr_type = clr_type;
	}
}
