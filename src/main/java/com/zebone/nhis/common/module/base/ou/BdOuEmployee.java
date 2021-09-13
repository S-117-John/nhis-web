package com.zebone.nhis.common.module.base.ou;

import java.util.Date;

import com.zebone.nhis.common.module.BaseModule;
import com.zebone.platform.modules.dao.build.au.Field;
import com.zebone.platform.modules.dao.build.au.PK;
import com.zebone.platform.modules.dao.build.au.Table;
import com.zebone.platform.modules.dao.build.au.Field.KeyId;

/**
 * 人员
 * @author Xulj
 *
 */
@Table(value="bd_ou_employee")
public class BdOuEmployee extends BaseModule {

	/**
	 * 人员主键
	 */
	@PK
	@Field(value="pk_emp",id=KeyId.UUID)
	private String pkEmp;
	
	/**
	 * 人员编码
	 */
	@Field(value="code_emp")
	private String codeEmp;
	
	/**
	 * 人员名称
	 */
	@Field(value="name_emp")
	private String nameEmp;
	
	/**
	 * 性别
	 */
	@Field(value="dt_sex")
	private String dtSex;
	
	/**
	 * 证件类型
	 */
	@Field(value="dt_identype")
	private String dtIdentype;
	
	/**
	 * 证件号码
	 */
	@Field(value="idno")
	private String idno;
	
	/**
	 * 拼音码
	 */
	@Field(value="py_code")
	private String pyCode;
	
	/**
	 * 自定义码
	 */
	@Field(value="d_code")
	private String dCode;
	
	/**
	 * 出生日期
	 */
	@Field(value="birthday")
	private Date birthday;
	
	/**
	 * 参加工作日期
	 */
	@Field(value="work_date")
	private Date workDate;
	
	/**
	 * 家庭地址
	 */
	@Field(value="addr")
	private String addr;
	
	/**
	 * 家庭电话
	 */
	@Field(value="homephone")
	private String homephone;
	
	/**
	 * 工作电话
	 */
	@Field(value="workphone")
	private String workphone;
	
	/**
	 * 手机
	 */
	@Field(value="mobile")
	private String mobile;
	
	/**
	 * 人员职业类型
	 */
	@Field(value="dt_emptype")
	private String dtEmptype;
	
	/**
	 * 医疗项目权限
	 */
	@Field(value="dt_empsrvtype")
	private String dtEmpsrvtype;
	
	/**
	 * 处方权
	 */
	@Field(value="flag_pres")
	private String flagPres;
	
	/**
	 * 麻醉处方权
	 */
	@Field(value="flag_anes")
	private String flagAnes;
	
	/**
	 * 精一处方权
	 */
	@Field(value="flag_spir_one")
	private String flagSpirOne;
	
	/**
	 * 精二处方权
	 */
	@Field(value="flag_spir_sec")
	private String flagSpirSec;
	
	/**
	 * 毒药处方权
	 */
	@Field(value="flag_poi")
	private String flagPoi;
	
	/**
	 * 专家出诊标识
	 */
	@Field(value="flag_spec")
	private String flagSpec;
	
	/**
	 * 启用状态
	 */
	@Field(value="flag_active")
	private String flagActive;
	
	/**
	 * 邮箱
	 */
	@Field(value="email")
	private String email;
	
	/**
	 * 专长
	 */
	@Field(value="spec")
	private String spec;

	/**
	 * 医生简介
	 */
	@Field(value="introduction")
	private String introduction;
	
	/**
	 * 照片
	 */
	@Field(value="photo")
	private byte[] photo;

	
	
	/**
	 * CA序列号
	 */
	@Field(value="caid")
	private String caid;
	/**
	 * CA序列号2
	 */	
	@Field(value="caid2")
	private String caid2;
	/**
	 * CA序列号3
	 */	
	@Field(value="caid3")
	private String caid3;
	/**
	 * CA序列号4
	 */	
	@Field(value="caid4")
	private String caid4;
	/**
	 * CA序列号5
	 */	
	@Field(value="caid5")
	private String caid5;
	/**
	 * 手签图片
	 */		
	@Field(value="img_sign")
	private byte[] imgSign;
	
	/**
	 * 主数据ID
	 */	
	@Field(value="id_master")
	private String idMaster;
	
	//医师类别
	@Field(value="eu_drtype")
	private String euDrtype;
	
	//抗菌药等级
	@Field(value="dt_anti")
	private String dtAnti;
	
	//手术分级
	@Field(value="dt_oplevel")
	private String dtOplevel;

	public String getIntroduction() {
		return introduction;
	}

	public void setIntroduction(String introduction) {
		this.introduction = introduction;
	}

	public String getEuDrtype() {
		return euDrtype;
	}

	public void setEuDrtype(String euDrtype) {
		this.euDrtype = euDrtype;
	}

	public String getDtAnti() {
		return dtAnti;
	}

	public void setDtAnti(String dtAnti) {
		this.dtAnti = dtAnti;
	}

	public String getDtOplevel() {
		return dtOplevel;
	}

	public void setDtOplevel(String dtOplevel) {
		this.dtOplevel = dtOplevel;
	}

	public String getIdMaster() {
		return idMaster;
	}

	public void setIdMaster(String idMaster) {
		this.idMaster = idMaster;
	}

	public String getPkEmp() {
		return pkEmp;
	}

	public void setPkEmp(String pkEmp) {
		this.pkEmp = pkEmp;
	}

	public String getCodeEmp() {
		return codeEmp;
	}

	public void setCodeEmp(String codeEmp) {
		this.codeEmp = codeEmp;
	}

	public String getNameEmp() {
		return nameEmp;
	}

	public void setNameEmp(String nameEmp) {
		this.nameEmp = nameEmp;
	}

	public String getDtSex() {
		return dtSex;
	}

	public void setDtSex(String dtSex) {
		this.dtSex = dtSex;
	}

	public String getDtIdentype() {
		return dtIdentype;
	}

	public void setDtIdentype(String dtIdentype) {
		this.dtIdentype = dtIdentype;
	}

	public String getIdno() {
		return idno;
	}

	public void setIdno(String idno) {
		this.idno = idno;
	}

	public String getPyCode() {
		return pyCode;
	}

	public void setPyCode(String pyCode) {
		this.pyCode = pyCode;
	}

	public String getdCode() {
		return dCode;
	}

	public void setdCode(String dCode) {
		this.dCode = dCode;
	}

	public Date getBirthday() {
		return birthday;
	}

	public void setBirthday(Date birthday) {
		this.birthday = birthday;
	}

	public Date getWorkDate() {
		return workDate;
	}

	public void setWorkDate(Date workDate) {
		this.workDate = workDate;
	}

	public String getAddr() {
		return addr;
	}

	public void setAddr(String addr) {
		this.addr = addr;
	}

	public String getHomephone() {
		return homephone;
	}

	public void setHomephone(String homephone) {
		this.homephone = homephone;
	}

	public String getWorkphone() {
		return workphone;
	}

	public void setWorkphone(String workphone) {
		this.workphone = workphone;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getDtEmptype() {
		return dtEmptype;
	}

	public void setDtEmptype(String dtEmptype) {
		this.dtEmptype = dtEmptype;
	}

	public String getDtEmpsrvtype() {
		return dtEmpsrvtype;
	}

	public void setDtEmpsrvtype(String dtEmpsrvtype) {
		this.dtEmpsrvtype = dtEmpsrvtype;
	}

	public String getFlagPres() {
		return flagPres;
	}

	public void setFlagPres(String flagPres) {
		this.flagPres = flagPres;
	}

	public String getFlagAnes() {
		return flagAnes;
	}

	public void setFlagAnes(String flagAnes) {
		this.flagAnes = flagAnes;
	}

	public String getFlagSpirOne() {
		return flagSpirOne;
	}

	public void setFlagSpirOne(String flagSpirOne) {
		this.flagSpirOne = flagSpirOne;
	}

	public String getFlagSpirSec() {
		return flagSpirSec;
	}

	public void setFlagSpirSec(String flagSpirSec) {
		this.flagSpirSec = flagSpirSec;
	}

	public String getFlagPoi() {
		return flagPoi;
	}

	public void setFlagPoi(String flagPoi) {
		this.flagPoi = flagPoi;
	}

	public String getFlagSpec() {
		return flagSpec;
	}

	public void setFlagSpec(String flagSpec) {
		this.flagSpec = flagSpec;
	}

	public String getFlagActive() {
		return flagActive;
	}

	public void setFlagActive(String flagActive) {
		this.flagActive = flagActive;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getSpec() {
		return spec;
	}

	public void setSpec(String spec) {
		this.spec = spec;
	}

	public byte[] getPhoto() {
		return photo;
	}

	public void setPhoto(byte[] photo) {
		this.photo = photo;
	}

	public String getCaid() {
		return caid;
	}

	public void setCaid(String caid) {
		this.caid = caid;
	}

	public String getCaid2() {
		return caid2;
	}

	public void setCaid2(String caid2) {
		this.caid2 = caid2;
	}

	public byte[] getImgSign() {
		return imgSign;
	}

	public void setImgSign(byte[] imgSign) {
		this.imgSign = imgSign;
	}

	public String getCaid3() {
		return caid3;
	}

	public void setCaid3(String caid3) {
		this.caid3 = caid3;
	}

	public String getCaid4() {
		return caid4;
	}

	public void setCaid4(String caid4) {
		this.caid4 = caid4;
	}

	public String getCaid5() {
		return caid5;
	}

	public void setCaid5(String caid5) {
		this.caid5 = caid5;
	}
	
}
