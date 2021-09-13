package com.zebone.nhis.pro.zsba.adt.vo;

import com.zebone.nhis.common.module.BaseModule;
import com.zebone.platform.modules.dao.build.au.Field;
import com.zebone.platform.modules.dao.build.au.PK;
import com.zebone.platform.modules.dao.build.au.Table;

@Table(value="t_patient_wx_mapper")
public class NHISPatientWxMapper extends BaseModule {

	@PK
	@Field(value="pk_patient_wx_mapper")
	private String pkPatientWxMapper;//主键
	
	@Field(value="pk_pi")
	private String pkPi; //住院患者主键
	
	@Field(value="modity_time")
	private String modityTime;//修改日期
	@Field(value="id")
	private int id; //his门诊记录id
	
	@Field(value="patient_id")
	private String patientId; //门诊患者id
	
	@Field(value="wx_open_id")
	private String wxOpenId;//微信openId
	
	@Field(value="relation_code")
	private String relationCode;//联系人类型编码

	@Field(value="ip_patient_id")
	private String ipPatientId; //住院患者id

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getPatientId() {
		return patientId;
	}

	public void setPatientId(String patientId) {
		this.patientId = patientId;
	}

	public String getWxOpenId() {
		return wxOpenId;
	}

	public void setWxOpenId(String wxOpenId) {
		this.wxOpenId = wxOpenId;
	}

	public String getRelationCode() {
		return relationCode;
	}

	public void setRelationCode(String relationCode) {
		this.relationCode = relationCode;
	}

	public String getIpPatientId() {
		return ipPatientId;
	}

	public void setIpPatientId(String ipPatientId) {
		this.ipPatientId = ipPatientId;
	}
	public String getPkPatientWxMapper() {
		return pkPatientWxMapper;
	}

	public void setPkPatientWxMapper(String pkPatientWxMapper) {
		this.pkPatientWxMapper = pkPatientWxMapper;
	}

	public String getModityTime() {
		return modityTime;
	}

	public void setModityTime(String modityTime) {
		this.modityTime = modityTime;
	}
	
	public String getPkPi() {
		return pkPi;
	}

	public void setPkPi(String pkPi) {
		this.pkPi = pkPi;
	}

}
