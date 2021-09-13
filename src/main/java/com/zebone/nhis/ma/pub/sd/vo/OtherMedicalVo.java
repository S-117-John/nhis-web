package com.zebone.nhis.ma.pub.sd.vo;

/**
 * 其它医保信息列表
 * @author Administrator
 *
 */
public class OtherMedicalVo {
	/**
	 * 序号
	 * true
	 */
	private Integer infoNo;
	
	/**
	 * 医保信息名称
	 * true
	 */
	private String infoName;
	
	/**
	 * 医保信息值
	 * true
	 */
	private String infoValue;
	
	/**
	 * 医保其它信息
	 * false
	 */
	private String infoOther;

	public Integer getInfoNo() {
		return infoNo;
	}

	public void setInfoNo(Integer infoNo) {
		this.infoNo = infoNo;
	}

	public String getInfoName() {
		return infoName;
	}

	public void setInfoName(String infoName) {
		this.infoName = infoName;
	}

	public String getInfoValue() {
		return infoValue;
	}

	public void setInfoValue(String infoValue) {
		this.infoValue = infoValue;
	}

	public String getInfoOther() {
		return infoOther;
	}

	public void setInfoOther(String infoOther) {
		this.infoOther = infoOther;
	}
	
	
}
