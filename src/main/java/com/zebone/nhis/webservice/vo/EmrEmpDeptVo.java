package com.zebone.nhis.webservice.vo;

import java.sql.Timestamp;
import java.util.Date;

import com.zebone.nhis.common.support.ApplicationUtils;
import com.zebone.platform.Application;
import com.zebone.platform.modules.dao.build.au.Field;
import com.zebone.platform.modules.utils.JsonUtil;



/**
 * @author chengjia
 *
 */
public class EmrEmpDeptVo{

	private String pkDept;

    private String deptCode;
       
    private String deptName;

	
	public String getDeptCode() {
		return deptCode;
	}

	public void setDeptCode(String deptCode) {
		this.deptCode = deptCode;
	}

	public String getDeptName() {
		return deptName;
	}

	public void setDeptName(String deptName) {
		this.deptName = deptName;
	}

	public String getPkDept() {
		return pkDept;
	}

	public void setPkDept(String pkDept) {
		this.pkDept = pkDept;
	}

    
}