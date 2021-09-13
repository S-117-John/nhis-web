package com.zebone.nhis.webservice.pskq.model;

import com.zebone.nhis.common.module.cn.ipdw.ExOpSch;
import com.zebone.platform.modules.dao.build.au.Field;

public class ExOpSchVo extends ExOpSch{
	@Field(value="NAME_CD")
    private String nameCd;
	@Field(value="DT_NAE_NAME")
    private String dtNaeName;
	@Field(value="PK_EMP_ANAE")
    private String pkEmpAnae;
	private String nameEmpAnae;
	@Field(value="PK_EMP_CIRCUL")
    private String pkEmpCircul;
	private String nameEmpCircul;
    @Field(value="PK_EMP_SCRUB")
    private String pkEmpScrub;
	private String nameEmpScrub;
    @Field(value="Pk_DEPT_EXEC")
    private String pkDeptExec;
    private String nameDeptExec;
	public String getDtNaeName() {
		return dtNaeName;
	}
	public void setDtNaeName(String dtNaeName) {
		this.dtNaeName = dtNaeName;
	}
	public String getPkEmpAnae() {
		return pkEmpAnae;
	}
	public void setPkEmpAnae(String pkEmpAnae) {
		this.pkEmpAnae = pkEmpAnae;
	}
	public String getPkEmpCircul() {
		return pkEmpCircul;
	}
	public void setPkEmpCircul(String pkEmpCircul) {
		this.pkEmpCircul = pkEmpCircul;
	}
	public String getPkEmpScrub() {
		return pkEmpScrub;
	}
	public void setPkEmpScrub(String pkEmpScrub) {
		this.pkEmpScrub = pkEmpScrub;
	}
	public String getPkDeptExec() {
		return pkDeptExec;
	}
	public void setPkDeptExec(String pkDeptExec) {
		this.pkDeptExec = pkDeptExec;
	}
	public String getNameEmpAnae() {
		return nameEmpAnae;
	}
	public void setNameEmpAnae(String nameEmpAnae) {
		this.nameEmpAnae = nameEmpAnae;
	}
	public String getNameEmpCircul() {
		return nameEmpCircul;
	}
	public void setNameEmpCircul(String nameEmpCircul) {
		this.nameEmpCircul = nameEmpCircul;
	}
	public String getNameEmpScrub() {
		return nameEmpScrub;
	}
	public void setNameEmpScrub(String nameEmpScrub) {
		this.nameEmpScrub = nameEmpScrub;
	}
	public String getNameDeptExec() {
		return nameDeptExec;
	}
	public void setNameDeptExec(String nameDeptExec) {
		this.nameDeptExec = nameDeptExec;
	}
	public String getNameCd() {
		return nameCd;
	}
	public void setNameCd(String nameCd) {
		this.nameCd = nameCd;
	}
}
