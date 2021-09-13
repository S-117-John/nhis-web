package com.zebone.nhis.pro.zsba.compay.ins.pub.vo;

import com.zebone.nhis.pro.zsba.compay.ins.qgyb.vo.Business.InsZsbaPvQg;
import com.zebone.nhis.pro.zsba.compay.ins.sngsyb.vo.InHosInitData;
import com.zebone.nhis.pro.zsba.compay.ins.zsyb.vo.HosInitialData;

public class InsRegData {
	private HosInitialData hosInitialData;//中山医保
	private InHosInitData inHosInitData;//省内工伤医保入院登记界面数据
	private YdHosInitialData ydHosInitialData;//异地医保出入院登记界面初始数据
	private InsZsbaPvQg qgHosInitialData; //全国医保入院登记界面初始数据
	private String code; //状态 1中山医保  2:异地或跨省  3：省工伤 4：全国医保 <0失败
	private String message;//错误信息
	private String hpCode;//患者身份编码
	public HosInitialData getHosInitialData() {
		return hosInitialData;
	}
	public void setHosInitialData(HosInitialData hosInitialData) {
		this.hosInitialData = hosInitialData;
	}
	public InHosInitData getInHosInitData() {
		return inHosInitData;
	}
	public void setInHosInitData(InHosInitData inHosInitData) {
		this.inHosInitData = inHosInitData;
	}
	public YdHosInitialData getYdHosInitialData() {
		return ydHosInitialData;
	}
	public void setYdHosInitialData(YdHosInitialData ydHosInitialData) {
		this.ydHosInitialData = ydHosInitialData;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public String getHpCode() {
		return hpCode;
	}
	public void setHpCode(String hpCode) {
		this.hpCode = hpCode;
	}
	public InsZsbaPvQg getQgHosInitialData() {
		return qgHosInitialData;
	}
	public void setQgHosInitialData(InsZsbaPvQg qgHosInitialData) {
		this.qgHosInitialData = qgHosInitialData;
	}
}
