package com.zebone.nhis.common.module.ex.nis.ns;

import java.util.Date;

import com.zebone.nhis.common.module.BaseModule;
import com.zebone.platform.modules.dao.build.au.Field;
import com.zebone.platform.modules.dao.build.au.PK;
import com.zebone.platform.modules.dao.build.au.Table;
import com.zebone.platform.modules.dao.build.au.Field.FieldType;
import com.zebone.platform.modules.dao.build.au.Field.KeyId;


/**
 * Table: EX_ORDER_OCC  - EX_ORDER_OCC 
 *
 * @since 2016-09-27 05:31:19
 */
@Table(value="EX_ORDER_OCC")
public class ExOrderOcc extends BaseModule  {

	@PK
	@Field(value="PK_EXOCC",id=KeyId.UUID)
    private String pkExocc;

	@Field(value="PK_PV")
    private String pkPv;

	@Field(value="PK_PI")
    private String pkPi;

	@Field(value="PK_CNORD")
    private String pkCnord;

	@Field(value="DATE_PLAN")
    private Date datePlan;

	@Field(value="DATE_OCC")
    private Date dateOcc;

	@Field(value="QUAN_OCC")
    private Double quanOcc;

	@Field(value="PK_UNIT")
    private String pkUnit;

	@Field(value="PRICE_REF")
    private Double priceRef;

	@Field(value="QUAN_CG")
    private Double quanCg;

	@Field(value="AMOUNT")
    private Double amount;

	@Field(value="PK_UNIT_CG")
    private String pkUnitCg;

	@Field(value="PACK_SIZE")
    private Long packSize;

	@Field(value="DRIP_SPEED")
    private Long dripSpeed;
	
	@Field(value="FLAG_BASE")
	private String flagBase;
	
	@Field(value="FLAG_SELF")
	private String flagSelf;

	@Field(value="EU_STATUS")
    private String euStatus;

	@Field(value="PK_DEPT_OCC")
    private String pkDeptOcc;

	@Field(value="PK_ORG_OCC")
    private String pkOrgOcc;
	
	@Field(value="PK_EMP_OCC")
    private String pkEmpOcc;
	
	@Field(value="NAME_EMP_OCC")
    private String nameEmpOcc;

	@Field(value="FLAG_CANC")
    private String flagCanc;

	@Field(value="PK_DEPT_CANC")
    private String pkDeptCanc;

	@Field(value="PK_EMP_CANC")
    private String pkEmpCanc;

	@Field(value="NAME_EMP_CANC")
    private String nameEmpCanc;

	@Field(value="DATE_CANC")
    private Date dateCanc;

	@Field(value="PK_PDAPDT")
    private String pkPdapdt;

	@Field(value="PK_PDBACK")
    private String pkPdback;

	@Field(value="PK_EXEVENT")
    private String pkExevent;

	@Field(value="PK_PDDE")
    private String pkPdde;

	@Field(value="PK_CG")
    private String pkCg;

	@Field(value="PK_CG_CANCEL")
    private String pkCgCancel;
	
	@Field(value="FLAG_PIVAS")
    private String flagPivas;
	
	/**
	 * 静配修改标志
	 */
	@Field(value="FLAG_MODI")
	private String flagModi;
	
	@Field(userfield="pkEmp",userfieldscop=FieldType.ALL)
    private String modifier;

	@Field(value="MODITY_TIME")
    private Date modityTime;
	
	public String getFlagModi() {
		return flagModi;
	}
	public void setFlagModi(String flagModi) {
		this.flagModi = flagModi;
	}
	public String getPkExocc(){
        return this.pkExocc;
    }
    public void setPkExocc(String pkExocc){
        this.pkExocc = pkExocc;
    }

    public String getPkPv(){
        return this.pkPv;
    }
    public void setPkPv(String pkPv){
        this.pkPv = pkPv;
    }

    public String getPkPi(){
        return this.pkPi;
    }
    public void setPkPi(String pkPi){
        this.pkPi = pkPi;
    }

    public String getPkCnord(){
        return this.pkCnord;
    }
    public void setPkCnord(String pkCnord){
        this.pkCnord = pkCnord;
    }

    public Date getDatePlan(){
        return this.datePlan;
    }
    public void setDatePlan(Date datePlan){
        this.datePlan = datePlan;
    }

    public Date getDateOcc(){
        return this.dateOcc;
    }
    public void setDateOcc(Date dateOcc){
        this.dateOcc = dateOcc;
    }

    public Double getQuanOcc(){
        return this.quanOcc;
    }
    public void setQuanOcc(Double quanOcc){
        this.quanOcc = quanOcc;
    }

    public String getPkUnit(){
        return this.pkUnit;
    }
    public void setPkUnit(String pkUnit){
        this.pkUnit = pkUnit;
    }

    public Double getPriceRef(){
        return this.priceRef;
    }
    public void setPriceRef(Double priceRef){
        this.priceRef = priceRef;
    }

    public Double getQuanCg(){
        return this.quanCg;
    }
    public void setQuanCg(Double quanCg){
        this.quanCg = quanCg;
    }

    public Double getAmount(){
        return this.amount;
    }
    public void setAmount(Double amount){
        this.amount = amount;
    }

    public String getPkUnitCg(){
        return this.pkUnitCg;
    }
    public void setPkUnitCg(String pkUnitCg){
        this.pkUnitCg = pkUnitCg;
    }

    public Long getPackSize(){
        return this.packSize;
    }
    public void setPackSize(Long packSize){
        this.packSize = packSize;
    }

    public Long getDripSpeed(){
        return this.dripSpeed;
    }
    public void setDripSpeed(Long dripSpeed){
        this.dripSpeed = dripSpeed;
    }

    public String getEuStatus(){
        return this.euStatus;
    }
    public void setEuStatus(String euStatus){
        this.euStatus = euStatus;
    }

    public String getPkDeptOcc(){
        return this.pkDeptOcc;
    }
    public void setPkDeptOcc(String pkDeptOcc){
        this.pkDeptOcc = pkDeptOcc;
    }

    public String getPkEmpOcc(){
        return this.pkEmpOcc;
    }
    public void setPkEmpOcc(String pkEmpOcc){
        this.pkEmpOcc = pkEmpOcc;
    }

    public String getNameEmpOcc(){
        return this.nameEmpOcc;
    }
    public void setNameEmpOcc(String nameEmpOcc){
        this.nameEmpOcc = nameEmpOcc;
    }

    public String getFlagCanc(){
        return this.flagCanc;
    }
    public void setFlagCanc(String flagCanc){
        this.flagCanc = flagCanc;
    }

    public String getPkDeptCanc(){
        return this.pkDeptCanc;
    }
    public void setPkDeptCanc(String pkDeptCanc){
        this.pkDeptCanc = pkDeptCanc;
    }

    public String getPkEmpCanc(){
        return this.pkEmpCanc;
    }
    public void setPkEmpCanc(String pkEmpCanc){
        this.pkEmpCanc = pkEmpCanc;
    }

    public String getNameEmpCanc(){
        return this.nameEmpCanc;
    }
    public void setNameEmpCanc(String nameEmpCanc){
        this.nameEmpCanc = nameEmpCanc;
    }

    public Date getDateCanc(){
        return this.dateCanc;
    }
    public void setDateCanc(Date dateCanc){
        this.dateCanc = dateCanc;
    }

    public String getPkPdapdt(){
        return this.pkPdapdt;
    }
    public void setPkPdapdt(String pkPdapdt){
        this.pkPdapdt = pkPdapdt;
    }

    public String getPkPdback(){
        return this.pkPdback;
    }
    public void setPkPdback(String pkPdback){
        this.pkPdback = pkPdback;
    }

    public String getPkExevent(){
        return this.pkExevent;
    }
    public void setPkExevent(String pkExevent){
        this.pkExevent = pkExevent;
    }

    public String getPkPdde(){
        return this.pkPdde;
    }
    public void setPkPdde(String pkPdde){
        this.pkPdde = pkPdde;
    }

    public String getPkCg(){
        return this.pkCg;
    }
    public void setPkCg(String pkCg){
        this.pkCg = pkCg;
    }

    public String getPkCgCancel(){
        return this.pkCgCancel;
    }
    public void setPkCgCancel(String pkCgCancel){
        this.pkCgCancel = pkCgCancel;
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
	public String getPkOrgOcc() {
		return pkOrgOcc;
	}
	public void setPkOrgOcc(String pkOrgOcc) {
		this.pkOrgOcc = pkOrgOcc;
	}
	public String getFlagBase() {
		return flagBase;
	}
	public void setFlagBase(String flagBase) {
		this.flagBase = flagBase;
	}
	public String getFlagSelf() {
		return flagSelf;
	}
	public void setFlagSelf(String flagSelf) {
		this.flagSelf = flagSelf;
	}
	public String getFlagPivas() {
		return flagPivas;
	}
	public void setFlagPivas(String flagPivas) {
		this.flagPivas = flagPivas;
	}
    
}