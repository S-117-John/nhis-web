package com.zebone.nhis.pro.lb.arch.vo;

/**
 * 给前端上传使用的字样，不包含内容
 * @author Alvin
 *
 */
public class ArchDocUpVo {

	private String dtMeddoctype;
	private String nameDoc;
	private String pkArchpv;
	private String pkPv;
	private String pkArchdoc;
	private Integer sortno;
	
	public String getDtMeddoctype() {
		return dtMeddoctype;
	}
	public void setDtMeddoctype(String dtMeddoctype) {
		this.dtMeddoctype = dtMeddoctype;
	}
	public String getNameDoc() {
		return nameDoc;
	}
	public void setNameDoc(String nameDoc) {
		this.nameDoc = nameDoc;
	}
	public String getPkArchpv() {
		return pkArchpv;
	}
	public void setPkArchpv(String pkArchpv) {
		this.pkArchpv = pkArchpv;
	}
	public String getPkPv() {
		return pkPv;
	}
	public void setPkPv(String pkPv) {
		this.pkPv = pkPv;
	}
	public String getPkArchdoc() {
		return pkArchdoc;
	}
	public void setPkArchdoc(String pkArchdoc) {
		this.pkArchdoc = pkArchdoc;
	}
	
	public Integer getSortno() {
		return sortno;
	}
	public void setSortno(Integer sortno) {
		this.sortno = sortno;
	}
	
	
}
