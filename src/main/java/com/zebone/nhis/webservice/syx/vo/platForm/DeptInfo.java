package com.zebone.nhis.webservice.syx.vo.platForm;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class DeptInfo {
	
	/**
     * 科室代码
     */
    @XmlElement(name = "deptId")
    private String deptId;
    
    /**
     * 科室名称
     */
    @XmlElement(name = "deptName")
    private String deptName;
    
    /**
     * 上级科室代码,没有填-1
     */
    @XmlElement(name = "parentId")
    private String parentId;
    
    /**
     * 科室简介
     */
    @XmlElement(name = "desc")
    private String deptDesc;
    
    /**
     * 片区代码
     */
    @XmlElement(name = "districtDeptId")
    private String districtDeptId;
    
    /**
     * 片区名称
     */
    @XmlElement(name = "districtDeptName")
    private String districtDeptName;
    
    @XmlElement(name = "resultCode")
	private String resultCode;
	
	@XmlElement(name = "resultDesc")
	private String resultDesc;
	
	

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

	public String getDeptId() {
		return deptId;
	}

	public void setDeptId(String deptId) {
		this.deptId = deptId;
	}

	public String getDeptName() {
		return deptName;
	}

	public void setDeptName(String deptName) {
		this.deptName = deptName;
	}

	public String getParentId() {
		return parentId;
	}

	public void setParentId(String parentId) {
		this.parentId = parentId;
	}

	public String getDeptDesc() {
		return deptDesc;
	}

	public void setDeptDesc(String deptDesc) {
		this.deptDesc = deptDesc;
	}

	public String getDistrictDeptId() {
		return districtDeptId;
	}

	public void setDistrictDeptId(String districtDeptId) {
		this.districtDeptId = districtDeptId;
	}

	public String getDistrictDeptName() {
		return districtDeptName;
	}

	public void setDistrictDeptName(String districtDeptName) {
		this.districtDeptName = districtDeptName;
	}
    
    

}
