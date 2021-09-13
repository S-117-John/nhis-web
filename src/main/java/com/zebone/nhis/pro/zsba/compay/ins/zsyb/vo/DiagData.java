package com.zebone.nhis.pro.zsba.compay.ins.zsyb.vo;

/**
 * 查询待入院列表参数
 * @author zim
 *
 */
public class DiagData {

	private String pkDiag; //诊断主键
	private String diagcode; //诊断编码
	private String diagname; //诊断名称
	private String sort_no; //诊断顺序
	private String flagMaj; //主要诊断标志
	public String getPkDiag() {
		return pkDiag;
	}
	public void setPkDiag(String pkDiag) {
		this.pkDiag = pkDiag;
	}
	public String getDiagcode() {
		return diagcode;
	}
	public void setDiagcode(String diagcode) {
		this.diagcode = diagcode;
	}
	public String getDiagname() {
		return diagname;
	}
	public void setDiagname(String diagname) {
		this.diagname = diagname;
	}
	public String getSort_no() {
		return sort_no;
	}
	public void setSort_no(String sort_no) {
		this.sort_no = sort_no;
	}
	public String getFlagMaj() {
		return flagMaj;
	}
	public void setFlagMaj(String flagMaj) {
		this.flagMaj = flagMaj;
	}
}
