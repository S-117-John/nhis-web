package com.zebone.platform.common.support;

import com.zebone.platform.framework.support.IRole;
import com.zebone.platform.framework.support.IUser;

import java.util.List;
import java.util.Map;


public class User implements IUser {

	private static final long serialVersionUID = 2649237161144786422L;
	
	/**
	 * 用户ID(用户主键)
	 */
	private String id;
	/**
	 * 用户主键
	 */
	private String pkUser;

	/**
	 * 用户登录名称(用户编码)
	 */
	private String loginName;

	/**
	 * 用户姓名
	 */
	private String userName;

	/**
	 * 密码
	 */
	private String password;
	
	private String euCerttype;
	
	/**
	 * 机构主键
	 */
	private String pkOrg;
	
	/**
	 * 机构编码
	 */
	private String codeOrg;
	/**
	 * 用户组
	 */
	private String pkUsrgrp;
	
	/**
	 * 关联人员
	 */
	private String pkEmp;
	
	/**
	 * 关联人员姓名
	 */
	private String nameEmp;
	/**
	 * 是否锁定  1:是0:否
	 */
	private String isLock;
	/**
	 * 启用状态   1:是 0:否
	 */
	private String flagActive;
	
	/**
	 * 当前科室
	 */
	private String pkDept;
	
	/**
	 * 当前仓库
	 */
	private String pkStore;
	
	/**
	 * 用户组科室关系
	 */
	private List<UserDept> depts =  null;
	
	/**
	 * 功能菜单集合
	 */
	public List<UserMenu> opers = null;
	
	private String caid;
	
	private String euUsertype;
	
	private String status;
	private String type ="3";   // 类型 1：集团超级管理员     2：机构管理员    3 ：普通用户
	private String salt;
	private Boolean isCaLogin;
	private String dtEmpsrvtype;//医疗项目权限
	private String empsrvtypeName;//医疗项目权限名称
	private String codeEmp;		//人员编码
	private String dbIp;
	private String dbName;
	private boolean isIntern;	//是否实习生
	private String pkEmpTeach;  //教师pkEmp
	private String nameEmpTeach;//导师姓名
	
	public String getPkUser() {
		return pkUser;
	}

	public void setPkUser(String pkUser) {
		this.pkUser = pkUser;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getLoginName() {
		return loginName;
	}

	public void setLoginName(String loginName) {
		this.loginName = loginName;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getSalt() {
		return salt;
	}

	public void setSalt(String salt) {
		this.salt = salt;
	}

	public String getCodeOrg() {
		return codeOrg;
	}

	public void setCodeOrg(String codeOrg) {
		this.codeOrg = codeOrg;
	}

	public String getPkOrg() {
		return pkOrg;
	}

	public void setPkOrg(String pkOrg) {
		this.pkOrg = pkOrg;
	}

	public String getPkUsrgrp() {
		return pkUsrgrp;
	}

	public void setPkUsrgrp(String pkUsrgrp) {
		this.pkUsrgrp = pkUsrgrp;
	}

	public String getPkEmp() {
		return pkEmp;
	}

	public void setPkEmp(String pkEmp) {
		this.pkEmp = pkEmp;
	}

	public String getIsLock() {
		return isLock;
	}

	public void setIsLock(String isLock) {
		this.isLock = isLock;
	}

	public String getFlagActive() {
		return flagActive;
	}

	public void setFlagActive(String flagActive) {
		this.flagActive = flagActive;
	}

	public String getCaid() {
		return caid;
	}

	public void setCaid(String caid) {
		this.caid = caid;
	}
	

	public String getPkDept() {
		return pkDept;
	}

	public void setPkDept(String pkDept) {
		this.pkDept = pkDept;
	}



	public List<UserDept> getDepts() {
		return depts;
	}

	public void setDepts(List<UserDept> depts) {
		this.depts = depts;
	}


	public List<UserMenu> getOpers() {
		return opers;
	} 

	public void setOpers(List<UserMenu> opers) {
		this.opers = opers;
	}

	public String getNameEmp() {
		return nameEmp;
	}

	public void setNameEmp(String nameEmp) {
		this.nameEmp = nameEmp;
	}


	public String getPkStore() {
		return pkStore;
	}

	public void setPkStore(String pkStore) {
		this.pkStore = pkStore;
	}

	public List<IRole> getRoles() {
		return null;
	}


	public Map<String, Object> getProperties() {
		return null;
	}

	public String getEuUsertype() {
		return euUsertype;
	}

	public void setEuUsertype(String euUsertype) {
		this.euUsertype = euUsertype;
	}

	public Boolean getIsCaLogin() {
		return isCaLogin;
	}

	public void setIsCaLogin(Boolean isCaLogin) {
		this.isCaLogin = isCaLogin;
	}

	public String getEuCerttype() {
		return euCerttype;
	}

	public void setEuCerttype(String euCerttype) {
		this.euCerttype = euCerttype;
	}

	public String getDtEmpsrvtype() {
		return dtEmpsrvtype;
	}

	public void setDtEmpsrvtype(String dtEmpsrvtype) {
		this.dtEmpsrvtype = dtEmpsrvtype;
	}

	public String getEmpsrvtypeName() {
		return empsrvtypeName;
	}

	public void setEmpsrvtypeName(String empsrvtypeName) {
		this.empsrvtypeName = empsrvtypeName;
	}

	public String getCodeEmp() {
		return codeEmp;
	}

	public void setCodeEmp(String codeEmp) {
		this.codeEmp = codeEmp;
	}

	public String getDbIp() {
		return dbIp;
	}

	public void setDbIp(String dbIp) {
		this.dbIp = dbIp;
	}

	public String getDbName() {
		return dbName;
	}

	public void setDbName(String dbName) {
		this.dbName = dbName;
	}

	public String getPkEmpTeach() {
		return pkEmpTeach;
	}

	public void setPkEmpTeach(String pkEmpTeach) {
		this.pkEmpTeach = pkEmpTeach;
	}

	public boolean isIntern() {
		return isIntern;
	}

	public void setIntern(boolean isIntern) {
		this.isIntern = isIntern;
	}

	public String getNameEmpTeach() {
		return nameEmpTeach;
	}

	public void setNameEmpTeach(String nameEmpTeach) {
		this.nameEmpTeach = nameEmpTeach;
	}

	
	
}