package com.zebone.nhis.common.module.cn.cp;

import java.util.Date;

import com.zebone.nhis.common.module.BaseModule;
import com.zebone.platform.modules.dao.build.au.Field;
import com.zebone.platform.modules.dao.build.au.Field.KeyId;
import com.zebone.platform.modules.dao.build.au.PK;
import com.zebone.platform.modules.dao.build.au.Table;

@Table(value="CP_TEMP_PHASE")
public class SyxCpTempPhase extends BaseModule {

	@PK
	@Field(value="PK_CPPHASE",id=KeyId.UUID)
    private String pkCpphase;

	@Field(value="PK_CPTEMP")
    private String pkCptemp;

	@Field(value="SORTNO")
    private Integer sortno;

	@Field(value="NAME_PHASE")
    private String namePhase;

	@Field(value="DAYS_MIN")
    private Integer daysMin;

	@Field(value="DAYS_MAX")
    private Integer daysMax;

	@Field(value="PK_CPPHASE_PRE")
    private String pkCpphasePre;

	@Field(value="EU_TYPE")
    private String euType;

	@Field(value="MODITY_TIME")
    private Date modityTime;

	public String getPkCpphase() {
		return pkCpphase;
	}

	public void setPkCpphase(String pkCpphase) {
		this.pkCpphase = pkCpphase;
	}

	public String getPkCptemp() {
		return pkCptemp;
	}

	public void setPkCptemp(String pkCptemp) {
		this.pkCptemp = pkCptemp;
	}

	public Integer getSortno() {
		return sortno;
	}

	public void setSortno(Integer sortno) {
		this.sortno = sortno;
	}

	public String getNamePhase() {
		return namePhase;
	}

	public void setNamePhase(String namePhase) {
		this.namePhase = namePhase;
	}

	public Integer getDaysMin() {
		return daysMin;
	}

	public void setDaysMin(Integer daysMin) {
		this.daysMin = daysMin;
	}

	public Integer getDaysMax() {
		return daysMax;
	}

	public void setDaysMax(Integer daysMax) {
		this.daysMax = daysMax;
	}

	public String getPkCpphasePre() {
		return pkCpphasePre;
	}

	public void setPkCpphasePre(String pkCpphasePre) {
		this.pkCpphasePre = pkCpphasePre;
	}

	public String getEuType() {
		return euType;
	}

	public void setEuType(String euType) {
		this.euType = euType;
	}

	public Date getModityTime() {
		return modityTime;
	}

	public void setModityTime(Date modityTime) {
		this.modityTime = modityTime;
	}
	
	
}
