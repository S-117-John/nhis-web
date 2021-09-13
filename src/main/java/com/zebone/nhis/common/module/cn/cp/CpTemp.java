package com.zebone.nhis.common.module.cn.cp;

import java.util.Date;
import java.util.List;

import com.zebone.nhis.common.module.BaseModule;
import com.zebone.platform.modules.dao.build.au.Field;
import com.zebone.platform.modules.dao.build.au.Field.KeyId;
import com.zebone.platform.modules.dao.build.au.PK;
import com.zebone.platform.modules.dao.build.au.Table;


/**
 * Table: CP_TEMP  - cp_temp 
 *
 * @since 2016-12-12 02:43:26
 */
@Table(value="CP_TEMP")
public class CpTemp extends BaseModule  {

	@PK
	@Field(value="PK_CPTEMP",id=KeyId.UUID)
    private String pkCptemp;

	@Field(value="CODE_CP")
    private String codeCp;

	@Field(value="NAME_CP")
    private String nameCp;

	@Field(value="VERSION")
    private Double version;

    /** EU_STATUS - 0保存，1提交，2审核，9审核未通过 */
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

	@Field(value="MODIFIER")
    private String modifier;

	@Field(value="MODITY_TIME")
    private Date modityTime;

	@Field(value="SPCODE")
    private String spcode;
	
	@Field(value="D_CODE")
    private String dCode;

	private List<CpTempPhase> phaseList;
	
    public String getPkCptemp(){
        return this.pkCptemp;
    }
    public void setPkCptemp(String pkCptemp){
        this.pkCptemp = pkCptemp;
    }

    public String getCodeCp(){
        return this.codeCp;
    }
    public void setCodeCp(String codeCp){
        this.codeCp = codeCp;
    }

    public String getNameCp(){
        return this.nameCp;
    }
    public void setNameCp(String nameCp){
        this.nameCp = nameCp;
    }

    public Double getVersion(){
        return this.version;
    }
    public void setVersion(Double version){
        this.version = version;
    }

    public String getEuStatus(){
        return this.euStatus;
    }
    public void setEuStatus(String euStatus){
        this.euStatus = euStatus;
    }

    public String getPkEmpEntry(){
        return this.pkEmpEntry;
    }
    public void setPkEmpEntry(String pkEmpEntry){
        this.pkEmpEntry = pkEmpEntry;
    }

    public String getNameEmpEntry(){
        return this.nameEmpEntry;
    }
    public void setNameEmpEntry(String nameEmpEntry){
        this.nameEmpEntry = nameEmpEntry;
    }

    public Date getDateEntry(){
        return this.dateEntry;
    }
    public void setDateEntry(Date dateEntry){
        this.dateEntry = dateEntry;
    }

    public String getPkEmpChk(){
        return this.pkEmpChk;
    }
    public void setPkEmpChk(String pkEmpChk){
        this.pkEmpChk = pkEmpChk;
    }

    public String getNameEmpChk(){
        return this.nameEmpChk;
    }
    public void setNameEmpChk(String nameEmpChk){
        this.nameEmpChk = nameEmpChk;
    }

    public Date getDateChk(){
        return this.dateChk;
    }
    public void setDateChk(Date dateChk){
        this.dateChk = dateChk;
    }

    public Double getAmountRef(){
        return this.amountRef;
    }
    public void setAmountRef(Double amountRef){
        this.amountRef = amountRef;
    }

    public Integer getDaysMin(){
        return this.daysMin;
    }
    public void setDaysMin(Integer daysMin){
        this.daysMin = daysMin;
    }

    public Integer getDaysMax(){
        return this.daysMax;
    }
    public void setDaysMax(Integer daysMax){
        this.daysMax = daysMax;
    }

    public String getModifier(){
        return this.modifier;
    }
    public void setModifier(String modifier){
        this.modifier = modifier;
    }

    public Date getModityTime(){
        return this.modityTime;
    }
    public void setModityTime(Date modityTime){
        this.modityTime = modityTime;
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
	public List<CpTempPhase> getPhaseList() {
		return phaseList;
	}
	public void setPhaseList(List<CpTempPhase> phaseList) {
		this.phaseList = phaseList;
	}

	/**
	 * 数据更新状态
	 */
	private String rowStatus;
	private List<CpTempPhase> allPhases;
	private List<CpTempOrd> allOrders;
	private List<CpTempWork> allWorks;
	private String copyTemp;
	
	public String getRowStatus() {
		return rowStatus;
	}
	public void setRowStatus(String rowStatus) {
		this.rowStatus = rowStatus;
	}
	public List<CpTempOrd> getAllOrders() {
		return allOrders;
	}
	public void setAllOrders(List<CpTempOrd> allOrders) {
		this.allOrders = allOrders;
	}
	public List<CpTempWork> getAllWorks() {
		return allWorks;
	}
	public void setAllWorks(List<CpTempWork> allWorks) {
		this.allWorks = allWorks;
	}
	public List<CpTempPhase> getAllPhases() {
		return allPhases;
	}
	public void setAllPhases(List<CpTempPhase> allPhases) {
		this.allPhases = allPhases;
	}
	public String getCopyTemp() {
		return copyTemp;
	}
	public void setCopyTemp(String copyTemp) {
		this.copyTemp = copyTemp;
	}
	
}