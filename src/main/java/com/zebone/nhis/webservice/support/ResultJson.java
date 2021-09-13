package com.zebone.nhis.webservice.support;

public class ResultJson {
	private String retCode;// 状态值，参考异常状态列表  ,大于0成功 小于0失败异常
	private String retMsg;// 描述
	private String retData;// 业务数据
	
	public ResultJson (String response){
		String[] str = response.split("\\|");
		if(str.length != 3) return;
		setRetCode(str[0]);
		setRetMsg(str[1]);
		setRetData(str[2]);
	}
	
	@Override
	public String toString() {
		return "{ \"ret_code\":\"" + retCode + "\", \"ret_msg\":\"" + retMsg + "\" ,"
				+ " \"ret_data\":" + retData + "}";
	}
	
	public String getRetCode() {
		return retCode;
	}
	public void setRetCode(String retCode) {
		this.retCode = retCode;
	}
	public String getRetMsg() {
		return retMsg;
	}
	public void setRetMsg(String retMsg) {
		this.retMsg = retMsg;
	}
	public String getRetData() {
		return retData;
	}
	public void setRetData(String retData) {
		this.retData = retData;
	}
}
