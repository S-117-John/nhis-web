package com.zebone.nhis.ma.pub.sd.vo;

/**
 * 请求入参类
 * @author Administrator
 *
 */
public class EnoteReqVo {
	
	/**
	 * 应用帐号
	 */
	private String appid;
	
	/**
	 * 请求业务参数
	 */
	private String data;
	
	/**
	 * 请求随机标识
	 */
	private String noise;
	
	/**
	 * 版本号
	 */
	private String version;
	
	/**
	 * 签名
	 */
	private String sign;

	public String getAppid() {
		return appid;
	}

	public void setAppid(String appid) {
		this.appid = appid;
	}

	public String getData() {
		return data;
	}

	public void setData(String data) {
		this.data = data;
	}

	public String getNoise() {
		return noise;
	}

	public void setNoise(String noise) {
		this.noise = noise;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public String getSign() {
		return sign;
	}

	public void setSign(String sign) {
		this.sign = sign;
	}
	
	
	
}
