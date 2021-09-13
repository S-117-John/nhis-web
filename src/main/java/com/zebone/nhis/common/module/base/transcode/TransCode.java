package com.zebone.nhis.common.module.base.transcode;


import java.util.Date;


import com.zebone.platform.modules.dao.build.au.Field;
import com.zebone.platform.modules.dao.build.au.PK;
import com.zebone.platform.modules.dao.build.au.Table;

@Table(value="sys_service_trans_code")
public class TransCode {
	@PK
	@Field(value="id")
	private String id;
	@Field(value="text")
	private String text;
	@Field(value="pid")
	private String pid;
	@Field(value="proxyname")
	private String proxyname;
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
	public String getPid() {
		return pid;
	}
	public void setPid(String pid) {
		this.pid = pid;
	}
	public String getProxyname() {
		return proxyname;
	}
	public void setProxyname(String proxyname) {
		this.proxyname = proxyname;
	}

}
