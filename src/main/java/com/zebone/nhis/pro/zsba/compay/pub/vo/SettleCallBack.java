package com.zebone.nhis.pro.zsba.compay.pub.vo;

import com.alibaba.fastjson.annotation.JSONField;

/**
 * 电子结算回流入参
 * @author Administrator
 *
 */
public class SettleCallBack {

	@JSONField(ordinal = 1)
	private SettleCallBackData data;// 推送数据 具体参数见入参
	@JSONField(ordinal = 2)
	private String appId;// 固定为1
	@JSONField(ordinal = 3)
	private String encType;// 加密类型 固定 plain 不加密 （预留字段暂时可为空）
	@JSONField(ordinal = 4)
	private String signType;// 签名方式   固定 plain 不加密 （预留字段暂时可为空）
	@JSONField(ordinal = 5)
	private String timestamp;// 时间戳 （预留字段暂时可为空）
	@JSONField(ordinal = 6)
	private String version;// 版本号（预留字段暂时可为空）
	
	public SettleCallBackData getData() {
		return data;
	}
	public void setData(SettleCallBackData data) {
		this.data = data;
	}
	public String getEncType() {
		return encType;
	}
	public void setEncType(String encType) {
		this.encType = encType;
	}
	public String getSignType() {
		return signType;
	}
	public void setSignType(String signType) {
		this.signType = signType;
	}
	public String getTimestamp() {
		return timestamp;
	}
	public void setTimestamp(String timestamp) {
		this.timestamp = timestamp;
	}
	public String getVersion() {
		return version;
	}
	public void setVersion(String version) {
		this.version = version;
	}
	public String getAppId() {
		return appId;
	}
	public void setAppId(String appId) {
		this.appId = appId;
	}
	
	
}
