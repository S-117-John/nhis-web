package com.zebone.nhis.cn.opdw.vo;

import com.zebone.nhis.common.module.cn.opdw.BdOpEmrTemp;

public class BdOpEmrTempVo extends BdOpEmrTemp {
	private String diagName;
	private String author;



	

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public String getDiagName() {
		return diagName;
	}

	public void setDiagName(String diagName) {
		this.diagName = diagName;
	}


}
