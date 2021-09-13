package com.zebone.nhis.cn.ipdw.vo;

import java.util.List;

public class OpHeadSignQryParam {
	 /// <summary>
    /// 手术时间开始
    /// </summary>
    private String datePlanBegin ;
    /// <summary>
    /// 手术时间结束
    /// </summary>
    private String datePlanEnd ;
    /// <summary>
    /// 床号
    /// </summary>
    private String bedNo ;
    /// <summary>
    /// 患者姓名
    /// </summary>
    private String namePi ;
    /// <summary>
    /// 主刀医师
    /// </summary>
    private String nameEmpPhyOp;
    /// <summary>
    /// 手术类型
    /// </summary>
    private String euOptype ;
    /// <summary>
    /// 手术名称
    /// </summary>
    private String opName ;
    /// <summary>
    /// 审核标志
    /// </summary>
    private String flagHead ;
	/**申请科室**/
	private String pkDept;
	/**申请科室列表**/
	private List<String> pkDeptList;

	public String getDatePlanBegin() {
		return datePlanBegin;
	}
	public void setDatePlanBegin(String datePlanBegin) {
		this.datePlanBegin = datePlanBegin;
	}
	public String getDatePlanEnd() {
		return datePlanEnd;
	}
	public void setDatePlanEnd(String datePlanEnd) {
		this.datePlanEnd = datePlanEnd;
	}
	public String getBedNo() {
		return bedNo;
	}
	public void setBedNo(String bedNo) {
		this.bedNo = bedNo;
	}
	public String getNamePi() {
		return namePi;
	}
	public void setNamePi(String namePi) {
		this.namePi = namePi;
	}
	public String getNameEmpPhyOp() {
		return nameEmpPhyOp;
	}
	public void setNameEmpPhyOp(String nameEmpPhyOp) {
		this.nameEmpPhyOp = nameEmpPhyOp;
	}
	public String getEuOptype() {
		return euOptype;
	}
	public void setEuOptype(String euOptype) {
		this.euOptype = euOptype;
	}
	public String getOpName() {
		return opName;
	}
	public void setOpName(String opName) {
		this.opName = opName;
	}
	public String getFlagHead() {
		return flagHead;
	}
	public void setFlagHead(String flagHead) {
		this.flagHead = flagHead;
	}

	public String getPkDept() {
		return pkDept;
	}

	public void setPkDept(String pkDept) {
		this.pkDept = pkDept;
	}

	public List<String> getPkDeptList() {
		return pkDeptList;
	}

	public void setPkDeptList(List<String> pkDeptList) {
		this.pkDeptList = pkDeptList;
	}
}
