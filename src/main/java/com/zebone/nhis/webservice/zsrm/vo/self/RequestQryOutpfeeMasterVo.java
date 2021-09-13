package com.zebone.nhis.webservice.zsrm.vo.self;

public class RequestQryOutpfeeMasterVo extends CommonReqSelfVo{

	/**
	 * 患者ID
	 */
    private String patientid;
    
	/**
	 * 收费开始时间
	 */
    private String indate;
    
    /**
     * 收费结束时间
     */
    private String outdate;

	public String getPatientid() {
		return patientid;
	}

	public String getIndate() {
		return indate;
	}

	public String getOutdate() {
		return outdate;
	}

	public void setPatientid(String patientid) {
		this.patientid = patientid;
	}

	public void setIndate(String indate) {
		this.indate = indate;
	}

	public void setOutdate(String outdate) {
		this.outdate = outdate;
	}
    
}
