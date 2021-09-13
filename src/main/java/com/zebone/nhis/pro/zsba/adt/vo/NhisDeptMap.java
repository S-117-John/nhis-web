package com.zebone.nhis.pro.zsba.adt.vo;

import java.util.Date;

import com.zebone.nhis.common.module.BaseModule;
import com.zebone.platform.modules.dao.build.au.Field;
import com.zebone.platform.modules.dao.build.au.PK;
import com.zebone.platform.modules.dao.build.au.Table;

@Table(value="NHIS_DEPT_MAP")
public class NhisDeptMap extends BaseModule {
	
	@PK
    @Field(value="PK_NHIS_DEPT_MAP")
    private String pkNhisDeptMap;
	
	@Field(value="HIS_WARD_SN")
	private String hisWardSn;

    @Field(value="HIS_WARD_CODE")
    private String hisWardCode;
    
    @Field(value="HIS_WARD_NAME")
    private String hisWardName;
    
    @Field(value="HIS_WARD_TYPE")
    private String hisWardType ;
    
    @Field(value="HIS_WARD_MZ_FLAG")
    private String hisWardMzFlag;
    
    @Field(value="NHIS_CODE_DEPT")
    private String nhisCodeDept; 
    
    @Field(value="NHIS_CODE_NS")
    private String nhisCodeNs;
    
    @Field(value="MODITY_TIME")
    private Date modityTime;

	public Date getModityTime() {
		return modityTime;
	}

	public void setModityTime(Date modityTime) {
		this.modityTime = modityTime;
	}

	public String getPkNhisDeptMap() {
		return pkNhisDeptMap;
	}

	public void setPkNhisDeptMap(String pkNhisDeptMap) {
		this.pkNhisDeptMap = pkNhisDeptMap;
	}

	public String getHisWardCode() {
		return hisWardCode;
	}

	public void setHisWardCode(String hisWardCode) {
		this.hisWardCode = hisWardCode;
	}

	public String getHisWardSn() {
		return hisWardSn;
	}

	public void setHisWardSn(String hisWardSn) {
		this.hisWardSn = hisWardSn;
	}

	public String getHisWardName() {
		return hisWardName;
	}

	public void setHisWardName(String hisWardName) {
		this.hisWardName = hisWardName;
	}

	public String getHisWardType() {
		return hisWardType;
	}

	public void setHisWardType(String hisWardType) {
		this.hisWardType = hisWardType;
	}

	public String getHisWardMzFlag() {
		return hisWardMzFlag;
	}

	public void setHisWardMzFlag(String hisWardMzFlag) {
		this.hisWardMzFlag = hisWardMzFlag;
	}

	public String getNhisCodeDept() {
		return nhisCodeDept;
	}

	public void setNhisCodeDept(String nhisCodeDept) {
		this.nhisCodeDept = nhisCodeDept;
	}

	public String getNhisCodeNs() {
		return nhisCodeNs;
	}

	public void setNhisCodeNs(String nhisCodeNs) {
		this.nhisCodeNs = nhisCodeNs;
	} 
}
