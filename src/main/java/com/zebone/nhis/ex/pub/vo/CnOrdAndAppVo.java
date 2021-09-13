package com.zebone.nhis.ex.pub.vo;

import java.util.Date;

import com.zebone.nhis.ex.pub.vo.ExlistPubVo;

/**
 * 取执行单 + 申请单部分信息 【2018-03-23 扫码执行调试后修改】
 * @date 2018-03-23
 *
 */
public class CnOrdAndAppVo extends ExlistPubVo {
	
	private String appStatus;//申请单状态
	
	private String nameEmpCol;//采集人
	
	private String nameDeptCol;//采集科室
	
	private Date dateCol;//采集时间

	public String getNameEmpCol() {
		return nameEmpCol;
	}

	public void setNameEmpCol(String nameEmpCol) {
		this.nameEmpCol = nameEmpCol;
	}

	public String getNameDeptCol() {
		return nameDeptCol;
	}

	public void setNameDeptCol(String nameDeptCol) {
		this.nameDeptCol = nameDeptCol;
	}

	public Date getDateCol() {
		return dateCol;
	}

	public void setDateCol(Date dateCol) {
		this.dateCol = dateCol;
	}

	public String getAppStatus() {
		return appStatus;
	}

	public void setAppStatus(String appStatus) {
		this.appStatus = appStatus;
	}

}
