package com.zebone.nhis.base.ou.vo;

/**
 * 密码修改参数类
 * @author wangpeng
 * @date 2017年3月2日
 *
 */
public class UserAndPwdParam {
	
	/** 用户编码 */
	private String userCode;
	
	/** 旧密码 */
	private String oldPwd;
	
	/** 新密码 */
	private String newPwd;

	private String pkEmp;
	

	public String getPkEmp() {
		return pkEmp;
	}

	public void setPkEmp(String pkEmp) {
		this.pkEmp = pkEmp;
	}

	public String getUserCode() {
		return userCode;
	}

	public void setUserCode(String userCode) {
		this.userCode = userCode;
	}

	public String getOldPwd() {
		return oldPwd;
	}

	public void setOldPwd(String oldPwd) {
		this.oldPwd = oldPwd;
	}

	public String getNewPwd() {
		return newPwd;
	}

	public void setNewPwd(String newPwd) {
		this.newPwd = newPwd;
	}

}
