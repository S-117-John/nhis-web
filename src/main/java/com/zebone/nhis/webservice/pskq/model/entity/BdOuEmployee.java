package com.zebone.nhis.webservice.pskq.model.entity;

import java.util.Date;

import com.alibaba.fastjson.annotation.JSONField;
import com.zebone.nhis.common.module.BaseModule;
import com.zebone.platform.modules.dao.build.au.Field;
import com.zebone.platform.modules.dao.build.au.PK;
import com.zebone.platform.modules.dao.build.au.Table;
import com.zebone.platform.modules.dao.build.au.Field.FieldType;
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
	@JSONField(name = "employeCode")
	private String codeEmp;
	
	/**
	 * 人员名称
	 */
	@Field(value="name_emp")
	@JSONField(name = "employeName")
	private String nameEmp;
	
	/**
	 * 性别
	 */
	@Field(value="dt_sex")
	@JSONField(name = "genderCode")
	private String dtSex;
	
	/**
	 * 证件类型
	 */
	@Field(value="dt_identype")
	@JSONField(name = "idTypeCode")
	private String dtIdentype;
	
	/**
	 * 证件号码
	 */
	@Field(value="idno")
	@JSONField(name = "idNo")
	private String idno;
	
	/**
	 * 拼音码
	 */
	@Field(value="py_code")
	@JSONField(name = "spellCode")
	private String pyCode;
	
	/**
	 * 自定义码
	 */
	@Field(value="d_code")
	@JSONField(name = "customCode")
	private String dCode;
	
	/**
	 * 出生日期
	 */
	@Field(value="birthday")
	@JSONField(name = "dateOfBirth")
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
	@JSONField(name = "presentAddress")
	private String addr;
	
	/**
	 * 家庭电话
	 */
	@Field(value="homephone")
	@JSONField(name = "homePhoneNo")
	private String homephone;
	
	/**
	 * 工作电话
	 */
	@Field(value="workphone")
	@JSONField(name = "workPhoneNo")
	private String workphone;
	
	/**
	 * 手机
	 */
	@Field(value="mobile")
	@JSONField(name = "phoneNo")
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
	@JSONField(name = "validState")
	private String flagActive;
	
	/**
	 * 邮箱
	 */
	@Field(value="email")
	@JSONField(name = "email")
	private String email;
	
	/**
	 * 专长
	 */
	@Field(value="spec")
	@JSONField(name = "expertise")
	private String spec;
	
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
	/**
     * 机构主键
     */
	@Field(value="pk_org",userfield="pkOrg",userfieldscop=FieldType.INSERT)
	@JSONField(name = "orgCode")
    public String pkOrg;

	/**
     * 创建人
     */
	@Field(userfield="pkEmp",userfieldscop=FieldType.INSERT)
	@JSONField(name = "enterOperaId")
    public String creator;
	


	/**
     * 修改人
     */
	@Field(userfield="pkEmp",userfieldscop=FieldType.ALL)
	@JSONField(name = "modifyOperaId")
    private String modifier;

	/**
	 * 修改时间
	 */
	@Field(value="modity_time")
	@JSONField(name = "modifyDateTime")
	private Date modityTime;
	/**
     * 创建时间
     */
	@Field(value="create_time",date=FieldType.INSERT)
	@JSONField(name = "enterDateTime")
	public Date createTime;
	
	
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

	public String getPkOrg() {
		return pkOrg;
	}

	public void setPkOrg(String pkOrg) {
		this.pkOrg = pkOrg;
	}

	public String getCreator() {
		return creator;
	}

	public void setCreator(String creator) {
		this.creator = creator;
	}

	public String getModifier() {
		return modifier;
	}

	public void setModifier(String modifier) {
		this.modifier = modifier;
	}

	public Date getModityTime() {
		return modityTime;
	}

	public void setModityTime(Date modityTime) {
		this.modityTime = modityTime;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	
	
}
