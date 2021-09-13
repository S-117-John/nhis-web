package com.zebone.nhis.webservice.syx.vo.platForm;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class HospitalInfo {

	@XmlElement(name = "resultCode")
    private String resultCode;
    
    @XmlElement(name = "resultDesc")
    private String resultDesc;
    /**
     * 医院名称
     */
    @XmlElement(name = "hospitalName")
    private String hospitalName;

    /**
     * 医院地址
     */
    @XmlElement(name = "addr")
    private String addr;
    /**
     * 医院电话
     */
    @XmlElement(name = "tel")
    private String tel;
    /**
     * 医院网址
     */
    @XmlElement(name = "webSite")
    private String webSite;
    /**
     * 医院简介
     */
    @XmlElement(name = "hospDesc")
    private String hospDesc;
    /**
     * 最大预约天数
     */
    @XmlElement(name = "maxRegDays")
    private String maxRegDays;

    @XmlElement(name = "districtInfo")
    private List<DistrictInfo> districtInfo;
    
    
    
    
    public String getResultCode() {
		return resultCode;
	}

	public void setResultCode(String resultCode) {
		this.resultCode = resultCode;
	}

	public String getResultDesc() {
		return resultDesc;
	}

	public void setResultDesc(String resultDesc) {
		this.resultDesc = resultDesc;
	}

	public List<DistrictInfo> getDistrictInfo() {
        return districtInfo;
    }

    public void setDistrictInfo(List<DistrictInfo> districtInfo) {
        this.districtInfo = districtInfo;
    }

    public String getAddr() {
        return addr;
    }

    public void setAddr(String addr) {
        this.addr = addr;
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public String getWebSite() {
        return webSite;
    }

    public void setWebSite(String webSite) {
        this.webSite = webSite;
    }

    public String getHospDesc() {
        return hospDesc;
    }

    public void setHospDesc(String hospDesc) {
        this.hospDesc = hospDesc;
    }

    public String getMaxRegDays() {
        return maxRegDays;
    }

    public void setMaxRegDays(String maxRegDays) {
        this.maxRegDays = maxRegDays;
    }

    public String getHospitalName() {
        return hospitalName;
    }

    public void setHospitalName(String hospitalName) {
        this.hospitalName = hospitalName;
    }
}



