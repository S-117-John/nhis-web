package com.zebone.nhis.ma.pub.sd.vo.cmrInsu;

public class CiOperationVo {

    /**
     * 手术编码
     */
    private String operationCode;    

	/**
     * 手术名称
     */
    private String operationName;

    /**
     * 手术时间
     * 格式YYYYMMDDHH24MISS
     */
    private String operationTime;

    public String getOperationCode() {
		return operationCode;
	}

	public void setOperationCode(String operationCode) {
		this.operationCode = operationCode;
	}

	public String getOperationName() {
		return operationName;
	}

	public void setOperationName(String operationName) {
		this.operationName = operationName;
	}

	public String getOperationTime() {
		return operationTime;
	}

	public void setOperationTime(String operationTime) {
		this.operationTime = operationTime;
	}
}
