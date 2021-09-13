package com.zebone.nhis.arch.vo;

public class ArchDocSys {

	public String nameDoctype;//文档类型名称
	
	public String docSpcode;//文档助记码
	
	public String nameDoc;//文档名称
	
	public String status;//文档状态(是否归档)

	public String dateUpload;//上传时间
	
	public String getNameDoctype() {
		return nameDoctype;
	}

	public void setNameDoctype(String nameDoctype) {
		this.nameDoctype = nameDoctype;
	}

	public String getDocSpcode() {
		return docSpcode;
	}

	public void setDocSpcode(String docSpcode) {
		this.docSpcode = docSpcode;
	}

	public String getNameDoc() {
		return nameDoc;
	}

	public void setNameDoc(String nameDoc) {
		this.nameDoc = nameDoc;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getDateUpload() {
		return dateUpload;
	}

	public void setDateUpload(String dateUpload) {
		this.dateUpload = dateUpload;
	}
	
	
}
