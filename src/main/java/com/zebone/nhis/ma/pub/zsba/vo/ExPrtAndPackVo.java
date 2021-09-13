package com.zebone.nhis.ma.pub.zsba.vo;

public class ExPrtAndPackVo extends OrderExVo {
	private String birthday;
	private String takeDt;
	private String doseList;
	private String hsptCd;
	private String dptmtCd;
	private String wardCd;
	private String doctorNm;
	private String medCd;
	private String orderNum;

	/**
	 *药袋编码
	 */
	private String codeBag;


	/**
	 *请领明细主键
	 */
	private String pkPdapdt;

	/**
	 *药品发放主键
	 */
	private String  pkPdde;

	/**
	 *是否走包药机
	 */
	private String  valAtt;
	/**
	 *计费主键
	 */
	private String  pkCgip;

	public String getBirthday() {
		return birthday;
	}

	public void setBirthday(String birthday) {
		this.birthday = birthday;
	}

	public String getTakeDt() {
		return takeDt;
	}

	public void setTakeDt(String takeDt) {
		this.takeDt = takeDt;
	}

	public String getDoseList() {
		return doseList;
	}

	public void setDoseList(String doseList) {
		this.doseList = doseList;
	}

	public String getHsptCd() {
		return hsptCd;
	}

	public void setHsptCd(String hsptCd) {
		this.hsptCd = hsptCd;
	}

	public String getDptmtCd() {
		return dptmtCd;
	}

	public void setDptmtCd(String dptmtCd) {
		this.dptmtCd = dptmtCd;
	}

	public String getWardCd() {
		return wardCd;
	}

	public void setWardCd(String wardCd) {
		this.wardCd = wardCd;
	}

	public String getDoctorNm() {
		return doctorNm;
	}

	public void setDoctorNm(String doctorNm) {
		this.doctorNm = doctorNm;
	}

	public String getMedCd() {
		return medCd;
	}

	public void setMedCd(String medCd) {
		this.medCd = medCd;
	}

	public String getOrderNum() {
		return orderNum;
	}

	public void setOrderNum(String orderNum) {
		this.orderNum = orderNum;
	}

}
