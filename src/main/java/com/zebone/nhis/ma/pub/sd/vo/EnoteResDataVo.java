package com.zebone.nhis.ma.pub.sd.vo;

/**
 * 电子票据响应参数 data
 * @author Administrator
 *
 */
public class EnoteResDataVo {
	
	/**
	 *  返回节点为”result”,值为S0000，表示成功；
		返回节点为”result”,值为其其它值（result <> S0000），表示失败

	 */
	private String result;
	
	/**
	 * 读取data参数中的内容BASE64解码，读取解码结果。
	 */
	private String message;

	public String getResult() {
		return result;
	}

	public void setResult(String result) {
		this.result = result;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
}
