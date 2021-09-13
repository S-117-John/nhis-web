package com.zebone.nhis.ma.pub.syx.vo;

import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("head")
public class Head {
	private String company_num;
	private String key;
	private String sign;
	public String getCompany_num() {
		return company_num;
	}
	public void setCompany_num(String company_num) {
		this.company_num = company_num;
	}
	public String getKey() {
		return key;
	}
	public void setKey(String key) {
		this.key = key;
	}
	public String getSign() {
		return sign;
	}
	public void setSign(String sign) {
		this.sign = sign;
	}
	
}
