package com.zebone.nhis.pi.pub.vo;

public class CommonParam {
	
	/** 需要进行查询的字段名 */
	private String fieldName;
	
	/** 需要进行查询的字段值 */
	private String fieldValue;
	
	/** 产房|病区使用 */
	private String flagLabor;
	
	/** 当前产程状态 */
	private String status;
	
	/** 是否包含照片 */
	private String photoFlag;
	
	/** 就诊状态编码数组 */
	private String[] euStatuss;
	
	/** 是否判断就诊状态 */
	private String flagStatus = "1";
	
	/** 过滤就诊病区 */
	private String pkDeptNs ;
	
	/** 过滤就诊科室 */
	private String pkDept ;
	
	/** 过滤就诊类型 */
	private String euPvtype ;
	
	/** 过滤多个就诊类型 */
	private String[] euPvtypes ;
	
	/**返回多条就诊记录*/
	private String isBackMorePv;

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getFlagLabor() {
		return flagLabor;
	}

	public void setFlagLabor(String flagLabor) {
		this.flagLabor = flagLabor;
	}

	public String getPkDeptNs() {
		return pkDeptNs;
	}

	public void setPkDeptNs(String pkDeptNs) {
		this.pkDeptNs = pkDeptNs;
	}

	public String getFieldName() {
		return fieldName;
	}

	public void setFieldName(String fieldName) {
		this.fieldName = fieldName;
	}

	public String getFieldValue() {
		return fieldValue;
	}

	public void setFieldValue(String fieldValue) {
		this.fieldValue = fieldValue;
	}

	public String getPhotoFlag() {
		return photoFlag;
	}

	public void setPhotoFlag(String photoFlag) {
		this.photoFlag = photoFlag;
	}

	public String[] getEuStatuss() {
		return euStatuss;
	}

	public void setEuStatuss(String[] euStatuss) {
		this.euStatuss = euStatuss;
	}

	public String getFlagStatus() {
		return flagStatus;
	}

	public void setFlagStatus(String flagStatus) {
		this.flagStatus = flagStatus;
	}

	public String getPkDept() {
		return pkDept;
	}

	public void setPkDept(String pkDept) {
		this.pkDept = pkDept;
	}

	public String getEuPvtype() {
		return euPvtype;
	}

	public void setEuPvtype(String euPvtype) {
		this.euPvtype = euPvtype;
	}

	public String[] getEuPvtypes() {
		return euPvtypes;
	}

	public void setEuPvtypes(String[] euPvtypes) {
		this.euPvtypes = euPvtypes;
	}

	public String getIsBackMorePv() {
		return isBackMorePv;
	}

	public void setIsBackMorePv(String isBackMorePv) {
		this.isBackMorePv = isBackMorePv;
	}
}
