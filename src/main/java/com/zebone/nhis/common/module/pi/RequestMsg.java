package com.zebone.nhis.common.module.pi;

public class RequestMsg {
	
	private String ORGAN_CODE;
	
	private String SYS_CODE;
	
	private String SYS_PAT_TYPE;
	
	private String SYS_PAT_ID;
	
	private String SYS_PAT_PK_ID;
	
	private String APPLY_DATE;
	
	private PatInfo PAT_INFO;

	public String getORGAN_CODE() {
		return ORGAN_CODE;
	}

	public void setORGAN_CODE(String oRGAN_CODE) {
		ORGAN_CODE = oRGAN_CODE;
	}

	public String getSYS_CODE() {
		return SYS_CODE;
	}

	public void setSYS_CODE(String sYS_CODE) {
		SYS_CODE = sYS_CODE;
	}

	public String getSYS_PAT_TYPE() {
		return SYS_PAT_TYPE;
	}

	public void setSYS_PAT_TYPE(String sYS_PAT_TYPE) {
		SYS_PAT_TYPE = sYS_PAT_TYPE;
	}

	public String getSYS_PAT_ID() {
		return SYS_PAT_ID;
	}

	public void setSYS_PAT_ID(String sYS_PAT_ID) {
		SYS_PAT_ID = sYS_PAT_ID;
	}

	public String getSYS_PAT_PK_ID() {
		return SYS_PAT_PK_ID;
	}

	public void setSYS_PAT_PK_ID(String sYS_PAT_PK_ID) {
		SYS_PAT_PK_ID = sYS_PAT_PK_ID;
	}

	public String getAPPLY_DATE() {
		return APPLY_DATE;
	}

	public void setAPPLY_DATE(String aPPLY_DATE) {
		APPLY_DATE = aPPLY_DATE;
	}

	public PatInfo getPAT_INFO() {
		return PAT_INFO;
	}

	public void setPAT_INFO(PatInfo pAT_INFO) {
		PAT_INFO = pAT_INFO;
	}
	
	
	
}
