package com.zebone.nhis.pro.zsba.compay.ins.qgyb.vo.BottomClass;

/**
 * 9001签到入参
 * @author Administrator
 *
 */
public class Input9001SignIn {
	
	private String	opter_no;//	操作员编号
	private String	mac;//	签到MAC地址
	private String	ip;//	签到IP地址
	public String getOpter_no() {
		return opter_no;
	}
	public void setOpter_no(String opter_no) {
		this.opter_no = opter_no;
	}
	public String getMac() {
		return mac;
	}
	public void setMac(String mac) {
		this.mac = mac;
	}
	public String getIp() {
		return ip;
	}
	public void setIp(String ip) {
		this.ip = ip;
	}
	
	
}
