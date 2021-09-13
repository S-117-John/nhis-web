package com.zebone.nhis.pro.zsba.compay.ins.qgyb.vo.BottomClass;

/**
 * 6.3.2.1医药机构费用结算对总账
 * @author Administrator
 *
 */
public class Output3201Stmtinfo {
	
	private String	setl_optins;//	Y	结算经办机构
	private String	stmt_rslt;//	Y	对账结果
	private String	stmt_rslt_dscr;//	Y　	对账结果说明
	
	public String getSetl_optins() {
		return setl_optins;
	}
	public void setSetl_optins(String setl_optins) {
		this.setl_optins = setl_optins;
	}
	public String getStmt_rslt() {
		return stmt_rslt;
	}
	public void setStmt_rslt(String stmt_rslt) {
		this.stmt_rslt = stmt_rslt;
	}
	public String getStmt_rslt_dscr() {
		return stmt_rslt_dscr;
	}
	public void setStmt_rslt_dscr(String stmt_rslt_dscr) {
		this.stmt_rslt_dscr = stmt_rslt_dscr;
	}
}
