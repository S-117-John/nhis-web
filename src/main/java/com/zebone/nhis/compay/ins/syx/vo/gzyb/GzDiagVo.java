package com.zebone.nhis.compay.ins.syx.vo.gzyb;

import com.zebone.platform.modules.dao.build.au.Field;

public class GzDiagVo {

	/** 诊断名称 */
	@Field(value = "DESC_DIAG")
	private String cescDiag;

	/** ICD编码 */
	@Field(value = "CODE_ICD")
	private String codeIcd;

	/** 主诊断 */
	@Field(value = "FLAG_MAJ")
	private String flagmaj;

	/** DT_DIAGTYPE - 诊断类型 */
	@Field(value = "DT_DIAGTYPE")
	private String dtDiagtype;

	/** SORT_NO - 诊断序号 */
	@Field(value = "SORT_NO")
	private String sortNo;
	
	/** PK_DIAG - 诊断编码 */
	@Field(value = "PK_DIAG")
	private String pkDiag;

	@Field(value="PK_EMP_DIAG")
    private String pkEmpDiag;

	@Field(value="NAME_EMP_DIAG")
    private String nameEmpDiag;
	
	
	public String getCescDiag() {
		return cescDiag;
	}

	public void setCescDiag(String cescDiag) {
		this.cescDiag = cescDiag;
	}

	public String getCodeIcd() {
		return codeIcd;
	}

	public void setCodeIcd(String codeIcd) {
		this.codeIcd = codeIcd;
	}

	public String getFlagmaj() {
		return flagmaj;
	}

	public void setFlagmaj(String flagmaj) {
		this.flagmaj = flagmaj;
	}

	public String getDtDiagtype() {
		return dtDiagtype;
	}

	public void setDtDiagtype(String dtDiagtype) {
		this.dtDiagtype = dtDiagtype;
	}

	public String getSortNo() {
		return sortNo;
	}

	public void setSortNo(String sortNo) {
		this.sortNo = sortNo;
	}

	public String getPkDiag() {
		return pkDiag;
	}

	public void setPkDiag(String pkDiag) {
		this.pkDiag = pkDiag;
	}

	public String getPkEmpDiag() {
		return pkEmpDiag;
	}

	public void setPkEmpDiag(String pkEmpDiag) {
		this.pkEmpDiag = pkEmpDiag;
	}

	public String getNameEmpDiag() {
		return nameEmpDiag;
	}

	public void setNameEmpDiag(String nameEmpDiag) {
		this.nameEmpDiag = nameEmpDiag;
	}

}
