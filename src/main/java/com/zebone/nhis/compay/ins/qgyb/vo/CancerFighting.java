package com.zebone.nhis.compay.ins.qgyb.vo;

import java.util.Date;

import com.zebone.platform.modules.dao.build.au.Field;
import com.zebone.platform.modules.dao.build.au.Field.FieldType;
import com.zebone.platform.modules.dao.build.au.Field.KeyId;
import com.zebone.platform.modules.dao.build.au.PK;
import com.zebone.platform.modules.dao.build.au.Table;

@Table(value="ins_qgyb_pd")
public class CancerFighting {
	
	//主键
//	@PK
//	@Field(value="pk_inspd",id=KeyId.UUID)
	@PK
	@Field(value="pk_inspd")
	private String pkInspd;
	
	//所属机构
	@Field(value="pk_org")
	private String pkOrg;
	
	//患者主键
	@Field(value="pk_pi")
	private String pkPi;
	
	//患者编码
	@Field(value="code_pi")
	private String codePi;
	
	//药品医保编码
	@Field(value="med_list_code")
	private String medListCode;
	
	//申请医生
	@Field(value="pk_emp")
	private String pkEmp;
	
	//创建人
	@Field(value="creator")
	private String creator;
	
	//创建时间
	@Field(value = "CREATE_TIME")
	private Date createTime;
	
	//删除标志
	@Field(value="del_flag")
	private String delFlag;
	
	//时间戳
	@Field(date = FieldType.ALL)
	private Date ts;
	
	//修改人
	@Field(value="modifier")
	private String modifier;

	public String getPkInspd() {
		return pkInspd;
	}

	public void setPkInspd(String pkInspd) {
		this.pkInspd = pkInspd;
	}

	public String getPkOrg() {
		return pkOrg;
	}

	public void setPkOrg(String pkOrg) {
		this.pkOrg = pkOrg;
	}

	public String getPkPi() {
		return pkPi;
	}

	public void setPkPi(String pkPi) {
		this.pkPi = pkPi;
	}

	public String getCodePi() {
		return codePi;
	}

	public void setCodePi(String codePi) {
		this.codePi = codePi;
	}

	public String getMedListCode() {
		return medListCode;
	}

	public void setMedListCode(String medListCode) {
		this.medListCode = medListCode;
	}

	public String getPkEmp() {
		return pkEmp;
	}

	public void setPkEmp(String pkEmp) {
		this.pkEmp = pkEmp;
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

	public String getDelFlag() {
		return delFlag;
	}

	public void setDelFlag(String delFlag) {
		this.delFlag = delFlag;
	}

	public Date getTs() {
		return ts;
	}

	public void setTs(Date ts) {
		this.ts = ts;
	}

	public String getModifier() {
		return modifier;
	}

	public void setModifier(String modifier) {
		this.modifier = modifier;
	}

	
}
