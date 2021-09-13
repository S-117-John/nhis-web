package com.zebone.nhis.ma.pub.platform.syx.vo;

import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("timeFrame")
public class TimeFrame {

	private Code code;
	
	private TotalFrameNumber totalFrameNumber;
	
	private EffectiveTime effectiveTime;
	
	private RemainNumber remainNumber;

	public Code getCode() {
		if(code == null) {
			code = new Code();
		}
		return code;
	}

	public void setCode(Code code) {
		this.code = code;
	}

	public TotalFrameNumber getTotalFrameNumber() {
		if(totalFrameNumber == null) {
			totalFrameNumber = new TotalFrameNumber();
		}
		return totalFrameNumber;
	}

	public void setTotalFrameNumber(TotalFrameNumber totalFrameNumber) {
		this.totalFrameNumber = totalFrameNumber;
	}

	public EffectiveTime getEffectiveTime() {
		if(effectiveTime == null) {
			effectiveTime = new EffectiveTime();
		}
		return effectiveTime;
	}

	public void setEffectiveTime(EffectiveTime effectiveTime) {
		this.effectiveTime = effectiveTime;
	}

	public RemainNumber getRemainNumber() {
		if(remainNumber == null) {
			remainNumber = new RemainNumber();
		}
		return remainNumber;
	}

	public void setRemainNumber(RemainNumber remainNumber) {
		this.remainNumber = remainNumber;
	}
	
	
}
