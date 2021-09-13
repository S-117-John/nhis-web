package com.zebone.nhis.ma.pub.sd.vo.cmrInsu;

public class CiTokenDataVo {
	//token编码
    private String access_token;

    //过期时间,单位秒
    private int expires_in;

    public int getExpires_in() {
		return expires_in;
	}

	public void setExpires_in(int expires_in) {
		this.expires_in = expires_in;
	}

	public String getAccess_token() {
        return access_token;
    }

    public void setAccess_token(String access_token) {
        this.access_token = access_token;
    }
    
}
