package com.zebone.nhis.webservice.vo;

import com.zebone.nhis.common.module.pi.PiCard;

public class LbPiCardVo extends PiCard{
    private String dtPaymode;//支付类型
    private String payInfo;//订单号
	public String getDtPaymode() {
		return dtPaymode;
	}
	public void setDtPaymode(String dtPaymode) {
		this.dtPaymode = dtPaymode;
	}
	public String getPayInfo() {
		return payInfo;
	}
	public void setPayInfo(String payInfo) {
		this.payInfo = payInfo;
	}
    
    
}
