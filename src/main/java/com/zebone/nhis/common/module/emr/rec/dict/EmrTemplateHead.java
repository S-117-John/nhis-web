package com.zebone.nhis.common.module.emr.rec.dict;

import java.util.List;

public class EmrTemplateHead {
	// 是否使用医院图片
	private String isUseHospitalImage;
	// 使用图片时, logo图片名称
	private String hospitalLogoFileName;
	// 模版默认标题元素
    private EmrDataElement templateTitleElement;
    // 如果不使用图片时,使用的医院名称元素
    private EmrDataElement hospitalLogoElement;
    //病人信息集合
    private List<EmrDataElement> patientInfoList;
    
	public String getIsUseHospitalImage() {
		return this.isUseHospitalImage;
	}

	public void setIsUseHospitalImage(String isUseHospitalImage) {
		this.isUseHospitalImage = isUseHospitalImage;
	}

	public String getHospitalLogoFileName() {
		return this.hospitalLogoFileName;
	}

	public void setHospitalLogoFileName(String hospitalLogoFileName) {
		this.hospitalLogoFileName = hospitalLogoFileName;
	}

	public EmrDataElement getTemplateTitleElement() {
		return this.templateTitleElement;
	}

	public void setTemplateTitleElement(EmrDataElement templateTitleElement) {
		this.templateTitleElement = templateTitleElement;
	}

	public EmrDataElement getHospitalLogoElement() {
		return this.hospitalLogoElement;
	}

	public void setHospitalLogoElement(EmrDataElement hospitalLogoElement) {
		this.hospitalLogoElement = hospitalLogoElement;
	}

	public List<EmrDataElement> getPatientInfoList() {
		return this.patientInfoList;
	}

	public void setPatientInfoList(List<EmrDataElement> patientInfoList) {
		this.patientInfoList = patientInfoList;
	}
}