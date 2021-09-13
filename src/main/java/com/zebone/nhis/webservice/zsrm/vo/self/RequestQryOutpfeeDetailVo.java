package com.zebone.nhis.webservice.zsrm.vo.self;

public class RequestQryOutpfeeDetailVo extends CommonReqSelfVo{
	
	/**
	 * 患者ID
	 */
    private String patientid;
	
    /**
     * 票据号
     */
    private String receiptNo;

	public String getPatientid() {
		return patientid;
	}

	public String getReceiptNo() {
		return receiptNo;
	}

	public void setPatientid(String patientid) {
		this.patientid = patientid;
	}

	public void setReceiptNo(String receiptNo) {
		this.receiptNo = receiptNo;
	}

}
