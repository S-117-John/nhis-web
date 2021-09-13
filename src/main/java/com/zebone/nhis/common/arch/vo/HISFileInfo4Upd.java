package com.zebone.nhis.common.arch.vo;

public class HISFileInfo4Upd {
	
	private String pkPv;
	
	private String fileName;
	
	private String chnName;
	
	private String fileType;

	private byte[] fileConetent;

	private String euOpType;
	
	
	public String getEuOpType() {
		return euOpType;
	}

	public void setEuOpType(String euOpType) {
		this.euOpType = euOpType;
	}

	public String getPkPv() {
		return pkPv;
	}

	public void setPkPv(String pkPv) {
		this.pkPv = pkPv;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getChnName() {
		return chnName;
	}

	public void setChnName(String chnName) {
		this.chnName = chnName;
	}

	public String getFileType() {
		return fileType;
	}

	public void setFileType(String fileType) {
		this.fileType = fileType;
	}

	public byte[] getFileConetent() {
		return fileConetent;
	}

	public void setFileConetent(byte[] fileConetent) {
		this.fileConetent = fileConetent;
	}
	
	

}

