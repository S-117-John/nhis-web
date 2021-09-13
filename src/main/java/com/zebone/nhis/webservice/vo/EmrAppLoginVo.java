package com.zebone.nhis.webservice.vo;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

import com.zebone.nhis.common.support.ApplicationUtils;
import com.zebone.platform.Application;
import com.zebone.platform.modules.dao.build.au.Field;
import com.zebone.platform.modules.utils.JsonUtil;



/**
 * @author chengjia
 *
 */
public class EmrAppLoginVo{

	private String ret_code;// 状态值，参考异常状态列表  ,大于等于0成功 小于0失败异常
	
	private String ret_msg;// 描述
	
    private String empCode;

    private String empName;

    private List<EmrEmpDeptVo> datalist;// 科室列表
    
	public String getRet_code() {
		return ret_code;
	}

	public void setRet_code(String ret_code) {
		this.ret_code = ret_code;
	}

	public String getRet_msg() {
		return ret_msg;
	}

	public void setRet_msg(String ret_msg) {
		this.ret_msg = ret_msg;
	}

	public String getEmpCode() {
		return empCode;
	}

	public void setEmpCode(String empCode) {
		this.empCode = empCode;
	}

	public String getEmpName() {
		return empName;
	}

	public void setEmpName(String empName) {
		this.empName = empName;
	}

	public List<EmrEmpDeptVo> getDatalist() {
		return datalist;
	}

	public void setDatalist(List<EmrEmpDeptVo> datalist) {
		this.datalist = datalist;
	}

    
    
}