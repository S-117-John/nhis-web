package com.zebone.nhis.webservice.zhongshan.vo;

import javax.xml.bind.annotation.*;
import java.util.List;

/**
 * @author chengjia
 * @Description 患者病历入院记录输出
 */
@XmlAccessorType(XmlAccessType.NONE)
@XmlRootElement(name = "MedicalHistoryInfo")
public class TaiKangRespEmrAdmitRec {

	@XmlElement(name = "MedicalHistoryAdmission")
    private List<TaiKangRespEmrAdmitRecInfo> recList;

	public List<TaiKangRespEmrAdmitRecInfo> getRecList() {
		return recList;
	}

	public void setRecList(List<TaiKangRespEmrAdmitRecInfo> recList) {
		this.recList = recList;
	}

  
}
