package com.zebone.nhis.webservice.vo.dataUp;


import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "req")
@XmlAccessorType(XmlAccessType.FIELD)
public class DataUpRequestVo {
	/**
	 * 医院代码
	 */
	@XmlElement(name="hospitalId")	
	private String hospitalId;
	
	/**
	 * 科室代码
	 */
	@XmlElement(name="deptId")	
	private String deptId;
	
	/**
	 * 查询项目
	 */
	@XmlElement(name="itemId")	
	private String itemId;
	
	/**
     *开始日期
     */
    @XmlElement(name="startDate")
    private String startDate;
    /**
     *结束日期
     */
    @XmlElement(name="endDate")
    private String endDate;
    
    /**
	 * 病区代码
	 */
	@XmlElement(name="wardId")	
	private String wardId;
	
	/**
	 * 查询日期
	 */
	@XmlElement(name="date")	
	private String dateStr;
	
	/**
	 * D:日期 M:月份(取日期月份)
	 * Y:年份(取日期年份)
	 */
	@XmlElement(name="type")	
	private String type;
	

	public String getHospitalId() {
		return hospitalId;
	}

	public void setHospitalId(String hospitalId) {
		this.hospitalId = hospitalId;
	}

	public String getDeptId() {
		return deptId;
	}

	public void setDeptId(String deptId) {
		this.deptId = deptId;
	}

	public String getItemId() {
		return itemId;
	}

	public void setItemId(String itemId) {
		this.itemId = itemId;
	}

	public String getStartDate() {
		return startDate;
	}

	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}

	public String getEndDate() {
		return endDate;
	}

	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}

	public String getWardId() {
		return wardId;
	}

	public void setWardId(String wardId) {
		this.wardId = wardId;
	}

	public String getDateStr() {
		return dateStr;
	}

	public void setDateStr(String dateStr) {
		this.dateStr = dateStr;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
	
}
