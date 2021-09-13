package com.zebone.nhis.sch.pub.support;

/**
 * 排班审核状态枚举
 * 
 * @author alvin
 *
 */
public enum SchEuStatus {
	/** 排班 */
	INIT("0"),
	/** 提交 */
	REFER("1"),
	/** 审核 */
	AUDIT("8");

	private String status;

	private SchEuStatus(String status) {
		this.status = status;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public static SchEuStatus getEuStatuValue(String status) {
		if(status == null || "".equals(status))
			return null;
		if(INIT.getStatus().equals(status))
			return INIT;
		if(REFER.getStatus().equals(status))
			return REFER;
		if(AUDIT.getStatus().equals(status))
			return AUDIT;
		return null;
	}
	
}