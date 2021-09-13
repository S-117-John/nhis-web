package com.zebone.nhis.common.module.base.ou;

import java.util.Date;
import java.util.List;

import com.zebone.nhis.base.ou.vo.BdOuOrgArea;
import com.zebone.platform.modules.dao.build.au.Field;
import com.zebone.platform.modules.dao.build.au.Field.FieldType;
import com.zebone.platform.modules.dao.build.au.Field.KeyId;
import com.zebone.platform.modules.dao.build.au.PK;
import com.zebone.platform.modules.dao.build.au.Table;

/**
 * 机构
 * @author Xulj
 *
 */
@Table(value="bd_ou_org")
public class BdOuOrg {

	/**
	 * 组织机构
	 */
	@PK
	@Field(value="pk_org",id=KeyId.UUID)
	private String pkOrg;

	/**
	 * 组织编码
	 */
	@Field(value="code_org")
	private String codeOrg;

	/**
	 * 组织名称
	 */
	@Field(value="name_org")
	private String nameOrg;

	/**
	 * 简称
	 */
	@Field(value="shortname")
	private String shortname;

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
	 * 机构类型
	 */
	@Field(value="org_type")
	private String orgType;

	/**
	 * 上级机构
	 */
	@Field(value="pk_father")
	private String pkFather;

	/**
	 * 级别
	 */
	@Field(value="eu_level")
	private String euLevel;

	/** DT_HOSPTYPE - 医院类型 码表010100 */
	@Field(value="DT_HOSPTYPE")
    private String dtHosptype;

    /** DT_GRADE - 医院等级 码表010101 */
	@Field(value="DT_GRADE")
    private String dtGrade;

    /** CODE_HOSP - 组织机构编码 */
	@Field(value="CODE_HOSP")
    private String codeHosp;

    /** CODE_DIVISION - 行政区划编码 */
	@Field(value="CODE_DIVISION")
    private String codeDivision;

    /** BEDNUM - 核定床位 */
	@Field(value="BEDNUM")
    private Integer bednum;

	/**
	 * 启用状态
	 */
	@Field(value="flag_active")
	private String flagActive;

	//联系电话
	@Field(value="phone")
	private String phone;

	//联系人
	@Field(value="CONTACTER")
	private String contacter;

	//单位负责人
	@Field(value="LEADER")
	private String leader;




	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getContacter() {
		return contacter;
	}

	public void setContacter(String contacter) {
		this.contacter = contacter;
	}

	public String getLeader() {
		return leader;
	}

	public void setLeader(String leader) {
		this.leader = leader;
	}

	/**
	 * 机构地址
	 */
	@Field(value="addr")
	private String addr;

	/**
	 * 机构网址
	 */
	@Field(value="website")
	private String website;

	/**
	 * 简介
	 */
	@Field(value="intro")
	private String intro;

	/**
     * 创建人
     */
	@Field(userfield="pkEmp",userfieldscop=FieldType.INSERT)
	private String creator;

	/**
     * 创建时间
     */
	@Field(value="create_time",date=FieldType.INSERT)
	private Date createTime;


	/**
     * 时间戳
     */
	@Field(date=FieldType.ALL)
	private Date ts;

	public List<BdOuOrgArea> orgAreas;



	public List<BdOuOrgArea> getOrgAreas() {
		return orgAreas;
	}

	public void setOrgArea(List<BdOuOrgArea> orgAreas) {
		this.orgAreas = orgAreas;
	}

	/**
     *删除标志
     */
	@Field(value="del_flag")
	private String delFlag = "0";  // 0未删除  1：删除

	public String getAddr() {
		return addr;
	}

	public void setAddr(String addr) {
		this.addr = addr;
	}

	public String getWebsite() {
		return website;
	}

	public void setWebsite(String website) {
		this.website = website;
	}

	public String getIntro() {
		return intro;
	}

	public void setIntro(String intro) {
		this.intro = intro;
	}

	public String getPkOrg() {
		return pkOrg;
	}

	public void setPkOrg(String pkOrg) {
		this.pkOrg = pkOrg;
	}

	public String getCodeOrg() {
		return codeOrg;
	}

	public void setCodeOrg(String codeOrg) {
		this.codeOrg = codeOrg;
	}

	public String getNameOrg() {
		return nameOrg;
	}

	public void setNameOrg(String nameOrg) {
		this.nameOrg = nameOrg;
	}

	public String getShortname() {
		return shortname;
	}

	public void setShortname(String shortname) {
		this.shortname = shortname;
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

	public String getOrgType() {
		return orgType;
	}

	public void setOrgType(String orgType) {
		this.orgType = orgType;
	}

	public String getPkFather() {
		return pkFather;
	}

	public void setPkFather(String pkFather) {
		this.pkFather = pkFather;
	}

	public String getEuLevel() {
		return euLevel;
	}

	public void setEuLevel(String euLevel) {
		this.euLevel = euLevel;
	}

	public String getDtHosptype() {
		return dtHosptype;
	}

	public void setDtHosptype(String dtHosptype) {
		this.dtHosptype = dtHosptype;
	}

	public String getDtGrade() {
		return dtGrade;
	}

	public void setDtGrade(String dtGrade) {
		this.dtGrade = dtGrade;
	}

	public String getCodeHosp() {
		return codeHosp;
	}

	public void setCodeHosp(String codeHosp) {
		this.codeHosp = codeHosp;
	}

	public String getCodeDivision() {
		return codeDivision;
	}

	public void setCodeDivision(String codeDivision) {
		this.codeDivision = codeDivision;
	}

	public Integer getBednum() {
		return bednum;
	}

	public void setBednum(Integer bednum) {
		this.bednum = bednum;
	}

	public String getFlagActive() {
		return flagActive;
	}

	public void setFlagActive(String flagActive) {
		this.flagActive = flagActive;
	}

	public String getCreator() {
		return creator;
	}

	public void setCreator(String creator) {
		this.creator = creator;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public Date getTs() {
		return ts;
	}

	public void setTs(Date ts) {
		this.ts = ts;
	}

	public String getDelFlag() {
		return delFlag;
	}

	public void setDelFlag(String delFlag) {
		this.delFlag = delFlag;
	}

}
