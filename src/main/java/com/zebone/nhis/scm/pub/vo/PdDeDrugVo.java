package com.zebone.nhis.scm.pub.vo;

import org.springframework.stereotype.Component;

/**
 * 发药数据传输层
 * @author Administrator
 * 
 */
@Component
public class PdDeDrugVo {

	// 发药主键
	private String pkPdde;

	// 请领明细主键
	private String pkPdapdt;

	// 发药单号
	private String codeDe;

	public String getPkPdde() {

		return pkPdde;
	}

	public void setPkPdde(String pkPdde) {

		this.pkPdde = pkPdde;
	}

	public String getPkPdapdt() {

		return pkPdapdt;
	}

	public void setPkPdapdt(String pkPdapdt) {

		this.pkPdapdt = pkPdapdt;
	}

	public String getCodeDe() {

		return codeDe;
	}

	public void setCodeDe(String codeDe) {

		this.codeDe = codeDe;
	}

}
