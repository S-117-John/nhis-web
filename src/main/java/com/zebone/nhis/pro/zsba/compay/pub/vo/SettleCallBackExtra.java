package com.zebone.nhis.pro.zsba.compay.pub.vo;

import com.alibaba.fastjson.annotation.JSONField;

/**
 * 电子结算回流入参extra
 * @author Administrator
 *
 */
public class SettleCallBackExtra {
	
	@JSONField(ordinal = 1)
	private String cId;//扫码墩或相关终端设备编号  存入 clientId，传POS机的终端号
	@JSONField(ordinal = 2)
	private String fixmedins_code;//两定机构代码 传全国统一编号
	@JSONField(ordinal = 3)
	private String fixmedins_name;//两定机构名称 传全国统一编号对应的名称
	public String getcId() {
		return cId;
	}
	public void setcId(String cId) {
		this.cId = cId;
	}
	public String getFixmedins_code() {
		return fixmedins_code;
	}
	public void setFixmedins_code(String fixmedins_code) {
		this.fixmedins_code = fixmedins_code;
	}
	public String getFixmedins_name() {
		return fixmedins_name;
	}
	public void setFixmedins_name(String fixmedins_name) {
		this.fixmedins_name = fixmedins_name;
	}
	
	
}
