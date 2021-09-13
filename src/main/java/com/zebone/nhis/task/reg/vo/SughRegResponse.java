package com.zebone.nhis.task.reg.vo;

public class SughRegResponse {

	
    // 返回结果编码 0成功，其他失败
    private String code ;
    
    private String btime ;
    
    private String etime ;
    
    // 访问令牌
    private String access_token ;
    
    private String expires_second ;
    
    // 失败消息返回
    private String msg ;

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getBtime() {
		return btime;
	}

	public void setBtime(String btime) {
		this.btime = btime;
	}

	public String getEtime() {
		return etime;
	}

	public void setEtime(String etime) {
		this.etime = etime;
	}

	public String getAccess_token() {
		return access_token;
	}

	public void setAccess_token(String access_token) {
		this.access_token = access_token;
	}

	public String getExpires_second() {
		return expires_second;
	}

	public void setExpires_second(String expires_second) {
		this.expires_second = expires_second;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}
    
}
