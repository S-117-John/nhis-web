package com.zebone.nhis.arch.vo;

import com.zebone.nhis.common.module.arch.BdArchDoctype;

public class ArchDoctypeVo extends BdArchDoctype {
	
	private String systype;
	
	private String choiceType;
	
	private String fileName;
	
	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	private byte[] data;
	
	 public byte[] getData() {
		return data;
	}

	public void setData(byte[] data) {
		this.data = data;
	}

	public static String CHOSEN = "1";
	 public static String ALTER = "0";

	public String getSystype() {
		return systype;
	}

	public void setSystype(String systype) {
		this.systype = systype;
	}

	public String getChoiceType() {
		return choiceType;
	}

	public void setChoiceType(String choiceType) {
		this.choiceType = choiceType;
	}
	

}
