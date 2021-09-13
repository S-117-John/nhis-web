package com.zebone.nhis.common.module.cn.cp;

import java.util.Date;

import com.zebone.nhis.common.module.BaseModule;
import com.zebone.platform.modules.dao.build.au.Field;
import com.zebone.platform.modules.dao.build.au.Field.KeyId;
import com.zebone.platform.modules.dao.build.au.PK;
import com.zebone.platform.modules.dao.build.au.Table;

@Table(value="CP_TEMP")
public class SyxCpTemp extends BaseModule{

	@PK
	@Field(value="PK_CPTEMP",id=KeyId.UUID)
    private String pkCptemp;

	@Field(value="CODE_CP")
    private String codeCp;

	@Field(value="NAME_CP")
    private String nameCp;
	
	@Field(value="SPCODE")
    private String spcode;
	
	@Field(value="D_CODE")
    private String dCode;
	
	@Field(value="AGE_BEGIN")
    private Integer ageBegin;
	
	@Field(value="AGE_END")
	private Integer ageEnd;
	
	@Field(value="EU_SEX")
	private String euSex;

	@Field(value="VERSION")
    private Double version;
	
	@Field(value="PK_DEPT")
	private String pkDept;

    /** EU_STATUS - 0保存，1提交，2审核，8发布 9停用 */
	@Field(value="EU_STATUS")
    private String euStatus;

	@Field(value="PK_EMP_ENTRY")
    private String pkEmpEntry;

	@Field(value="NAME_EMP_ENTRY")
    private String nameEmpEntry;

	@Field(value="DATE_ENTRY")
    private Date dateEntry;

	@Field(value="PK_EMP_CHK")
    private String pkEmpChk;

	@Field(value="NAME_EMP_CHK")
    private String nameEmpChk;

	@Field(value="DATE_CHK")
    private Date dateChk;

	@Field(value="AMOUNT_REF")
    private Double amountRef;

	@Field(value="DAYS_MIN")
    private Integer daysMin;

	@Field(value="DAYS_MAX")
    private Integer daysMax;
	
	@Field(value="PK_EMP_PUB")
    private String pkEmpPub;

	@Field(value="NAME_EMP_PUB")
    private String nameEmpPub;
	
	@Field(value="DATE_PUB")
    private Date datePub;
	
	@Field(value="PK_EMP_STOP")
    private String pkEmpStop;

	@Field(value="NAME_EMP_STOP")
    private String nameEmpStop;
	
	@Field(value="DATE_STOP")
    private Date dateStop;

	@Field(value="MODITY_TIME")
    private Date modityTime;

	public String getPkCptemp() {
		return pkCptemp;
	}

	public void setPkCptemp(String pkCptemp) {
		this.pkCptemp = pkCptemp;
	}

	public String getCodeCp() {
		return codeCp;
	}

	public void setCodeCp(String codeCp) {
		this.codeCp = codeCp;
	}

	public String getNameCp() {
		return nameCp;
	}

	public void setNameCp(String nameCp) {
		this.nameCp = nameCp;
	}

	public String getSpcode() {
		return spcode;
	}

	public void setSpcode(String spcode) {
		this.spcode = spcode;
	}

	public String getdCode() {
		return dCode;
	}

	public void setdCode(String dCode) {
		this.dCode = dCode;
	}

	public Integer getAgeBegin() {
		return ageBegin;
	}

	public void setAgeBegin(Integer ageBegin) {
		this.ageBegin = ageBegin;
	}

	public Integer getAgeEnd() {
		return ageEnd;
	}

	public void setAgeEnd(Integer ageEnd) {
		this.ageEnd = ageEnd;
	}

	public String getEuSex() {
		return euSex;
	}

	public void setEuSex(String euSex) {
		this.euSex = euSex;
	}

	public Double getVersion() {
		return version;
	}

	public void setVersion(Double version) {
		this.version = version;
	}

	public String getPkDept() {
		return pkDept;
	}

	public void setPkDept(String pkDept) {
		this.pkDept = pkDept;
	}

	public String getEuStatus() {
		return euStatus;
	}

	public void setEuStatus(String euStatus) {
		this.euStatus = euStatus;
	}

	public String getPkEmpEntry() {
		return pkEmpEntry;
	}

	public void setPkEmpEntry(String pkEmpEntry) {
		this.pkEmpEntry = pkEmpEntry;
	}

	public String getNameEmpEntry() {
		return nameEmpEntry;
	}

	public void setNameEmpEntry(String nameEmpEntry) {
		this.nameEmpEntry = nameEmpEntry;
	}

	public Date getDateEntry() {
		return dateEntry;
	}

	public void setDateEntry(Date dateEntry) {
		this.dateEntry = dateEntry;
	}

	public String getPkEmpChk() {
		return pkEmpChk;
	}

	public void setPkEmpChk(String pkEmpChk) {
		this.pkEmpChk = pkEmpChk;
	}

	public String getNameEmpChk() {
		return nameEmpChk;
	}

	public void setNameEmpChk(String nameEmpChk) {
		this.nameEmpChk = nameEmpChk;
	}

	public Date getDateChk() {
		return dateChk;
	}

	public void setDateChk(Date dateChk) {
		this.dateChk = dateChk;
	}

	public Double getAmountRef() {
		return amountRef;
	}

	public void setAmountRef(Double amountRef) {
		this.amountRef = amountRef;
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

	public String getPkEmpPub() {
		return pkEmpPub;
	}

	public void setPkEmpPub(String pkEmpPub) {
		this.pkEmpPub = pkEmpPub;
	}

	public String getNameEmpPub() {
		return nameEmpPub;
	}

	public void setNameEmpPub(String nameEmpPub) {
		this.nameEmpPub = nameEmpPub;
	}

	public Date getDatePub() {
		return datePub;
	}

	public void setDatePub(Date datePub) {
		this.datePub = datePub;
	}

	public String getPkEmpStop() {
		return pkEmpStop;
	}

	public void setPkEmpStop(String pkEmpStop) {
		this.pkEmpStop = pkEmpStop;
	}

	public String getNameEmpStop() {
		return nameEmpStop;
	}

	public void setNameEmpStop(String nameEmpStop) {
		this.nameEmpStop = nameEmpStop;
	}

	public Date getDateStop() {
		return dateStop;
	}

	public void setDateStop(Date dateStop) {
		this.dateStop = dateStop;
	}

	public Date getModityTime() {
		return modityTime;
	}

	public void setModityTime(Date modityTime) {
		this.modityTime = modityTime;
	}
}
