package com.zebone.nhis.ma.pub.lb.vo;

import java.util.Date;

public class RequConsumablesQuerVo {
	 //HRP条形码
    private String HRPBARCODE;
    //请求时间
    private Date REUESTTIME;
	
	public String getHRPBARCODE() {
		return HRPBARCODE;
	}
	public void setHRPBARCODE(String hRPBARCODE) {
		HRPBARCODE = hRPBARCODE;
	}
	public Date getREUESTTIME() {
		return REUESTTIME;
	}
	public void setREUESTTIME(Date rEUESTTIME) {
		REUESTTIME = rEUESTTIME;
	}
    
    
}
