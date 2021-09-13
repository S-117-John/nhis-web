package com.zebone.nhis.ex.oi.vo;

import java.util.HashMap;
import java.util.Map;

import com.zebone.nhis.common.module.ex.oi.ExInfusionOcc;

public class OiSaveOccVO {
	private String saveMode;
	private OiPatientInfoVo registerInfo = new OiPatientInfoVo();
	private ExInfusionOcc occInfo = new ExInfusionOcc();
	private Map<String, Object> paramMap = new HashMap<String, Object>();
	
    public Map<String, Object> getParamMap() {
		return paramMap;
	}
	public void setParamMap(Map<String, Object> paramMap) {
		this.paramMap = paramMap;
	}
	public OiPatientInfoVo getRegisterInfo() {
		return registerInfo;
	}
	public void setRegisterInfo(OiPatientInfoVo registerInfo) {
		this.registerInfo = registerInfo;
	}

	public ExInfusionOcc getOccInfo() {
		return occInfo;
	}
	public void setOccInfo(ExInfusionOcc occInfo) {
		this.occInfo = occInfo;
	}
	
	public String getSaveMode() {
		return saveMode;
	}
	public void setSaveMode(String saveMode) {
		this.saveMode = saveMode;
	}
    
}
