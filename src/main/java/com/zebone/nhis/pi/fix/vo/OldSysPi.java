package com.zebone.nhis.pi.fix.vo;

import com.zebone.nhis.pi.pub.vo.PiMasterVo;

public class OldSysPi extends PiMasterVo {
	
	private String ipTimes;

	private String isOldPi;
	
	public String getIsOldPi() {
		return isOldPi;
	}

	public void setIsOldPi(String isOldPi) {
		this.isOldPi = isOldPi;
	}

	public String getIpTimes() {
		return ipTimes;
	}

	public void setIpTimes(String ipTimes) {
		this.ipTimes = ipTimes;
	}

}
