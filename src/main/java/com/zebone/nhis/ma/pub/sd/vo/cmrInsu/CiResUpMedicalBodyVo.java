package com.zebone.nhis.ma.pub.sd.vo.cmrInsu;

/**
 * S340接口返回的body Vo
 */
public class CiResUpMedicalBodyVo {
	/**
     * 就诊流水号
     */
    private String medicalNum;
    
    /**
     * 平台病历ID
     */
    private String medicalHistoryId;

	public String getMedicalNum() {
		return medicalNum;
	}

	public void setMedicalNum(String medicalNum) {
		this.medicalNum = medicalNum;
	}

	public String getMedicalHistoryId() {
		return medicalHistoryId;
	}

	public void setMedicalHistoryId(String medicalHistoryId) {
		this.medicalHistoryId = medicalHistoryId;
	}
    
    

    
}
