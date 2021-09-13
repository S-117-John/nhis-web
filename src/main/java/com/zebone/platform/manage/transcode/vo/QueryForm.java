package com.zebone.platform.manage.transcode.vo;

/**
 * 查询条件对象
 * 
 * @author
 *
 */
public class QueryForm {

	private String transcode;

	private String name;
	
	private String zt;
	
	public String getTranscode() {
		return transcode;
	}

	public void setTranscode(String transcode) {
		this.transcode = transcode;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getZt() {
		return zt;
	}

	public void setZt(String zt) {
		this.zt = zt;
	}

	@Override
	public String toString() {
		return "QueryForm [transcode=" + transcode + ", zt=" + zt + "]";
	}
}
