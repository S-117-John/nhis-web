package com.zebone.nhis.common.module.cn.ipdw;

import java.util.Date;

import com.zebone.nhis.common.module.BaseModule;
import com.zebone.platform.modules.dao.build.au.Field;
import com.zebone.platform.modules.dao.build.au.Field.FieldType;
import com.zebone.platform.modules.dao.build.au.PK;
import com.zebone.platform.modules.dao.build.au.Table;
import com.zebone.platform.modules.dao.build.au.Field.KeyId;


/**
 * Table: BD_ORD_SET_DT - bd_ord_set_dt 
 *
 * @since 2016-10-25 12:04:17
 */
@Table(value="BD_ORD_SET_DT")
public class BdOrdSetDt { 

	@PK
	@Field(value="PK_ORDSETDT",id=KeyId.UUID)
    private String pkOrdsetdt;

	@Field(value="PK_ORDSET")
    private String pkOrdset;

	@Field(value="PK_ORD")
    private String pkOrd;

	@Field(value="flag_self")
    private String flagSelf;
	
	@Field(value="FLAG_PD")
    private String flagPd;

	@Field(value="ORDER_NO")
    private Long orderNo;

	@Field(value="PARENT_NO")
    private Long parentNo;

	@Field(value="CODE_FREQ")
    private String codeFreq;

	@Field(value="CODE_SUPPLY")
    private String codeSupply;

	@Field(value="DOSAGE")
    private Double dosage;

	@Field(value="PK_UNIT_DOS")
    private String pkUnitDos;

	@Field(value="SUPPLY_NOTE")
    private String supplyNote;

	@Field(value="QUAN")
    private Double quan;

	@Field(value="PK_UNIT")
    private String pkUnit;

	@Field(value="DAYS")
    private Long days;

	@Field(value="PK_DEPT_EXEC")
    private String pkDeptExec;
	
    @Field(value="PK_ORG_EXEC")
    private String pkOrgExec;
    
	@Field(value="FLAG_DEF")
    private String flagDef;

	@Field(value="NAME_ORD")
    private String nameOrd;

	@Field(value="NOTE")
    private String note;

	@Field(userfield="pkEmp",userfieldscop=FieldType.INSERT)
    private String creator;

	@Field(value="CREATE_TIME",date=FieldType.INSERT)
    private Date createTime;

	@Field(userfield="pkEmp",userfieldscop=FieldType.ALL)
    private String modifier;

	@Field(value="DEL_FLAG")
    private String delFlag;

	@Field(date=FieldType.ALL)
    private Date ts;
	
	@Field(value="SORT_NO")
	private Integer sortNo;
	
	@Field(value="DT_HERBUSAGE")
	private String dtHerbusage;

    @Field(value="FLAG_DISP")
    private String flagDisp; //不发药标志

    @Field(value = "EU_BOIL")
    private String euBoil; //煎药方式

    private String rowStatus;
    
    private String checked;

    /**
     * 处方类型
     */
    @Field(value = "DT_PRESTYPE")
    private String dtPrestype;

    /**
     * 处方号
     */
    @Field(value = "PRES_NO")
    private String presNo;
    
	public String getChecked() {
		return checked;
	}
	public void setChecked(String checked) {
		this.checked = checked;
	}
	public String getRowStatus() {
		return rowStatus;
	}
	public void setRowStatus(String rowStatus) {
		this.rowStatus = rowStatus;
	}

    public String getPkOrdsetdt(){
        return this.pkOrdsetdt;
    }
    public void setPkOrdsetdt(String pkOrdsetdt){
        this.pkOrdsetdt = pkOrdsetdt;
    }

    public String getPkOrdset(){
        return this.pkOrdset;
    }
    public void setPkOrdset(String pkOrdset){
        this.pkOrdset = pkOrdset;
    }

    public String getPkOrd(){
        return this.pkOrd;
    }
    public void setPkOrd(String pkOrd){
        this.pkOrd = pkOrd;
    }

    public String getFlagPd(){
        return this.flagPd;
    }
    public void setFlagPd(String flagPd){
        this.flagPd = flagPd;
    }

    public Long getOrderNo(){
        return this.orderNo;
    }
    public void setOrderNo(Long orderNo){
        this.orderNo = orderNo;
    }

    public Long getParentNo(){
        return this.parentNo;
    }
    public void setParentNo(Long parentNo){
        this.parentNo = parentNo;
    }

    public String getCodeFreq(){
        return this.codeFreq;
    }
    public void setCodeFreq(String codeFreq){
        this.codeFreq = codeFreq;
    }

    public String getCodeSupply(){
        return this.codeSupply;
    }
    public void setCodeSupply(String codeSupply){
        this.codeSupply = codeSupply;
    }

    public Double getDosage(){
        return this.dosage;
    }
    public void setDosage(Double dosage){
        this.dosage = dosage;
    }

    public String getPkUnitDos() {
		return pkUnitDos;
	}
	public void setPkUnitDos(String pkUnitDos) {
		this.pkUnitDos = pkUnitDos;
	}
	public String getPkUnit() {
		return pkUnit;
	}
	public void setPkUnit(String pkUnit) {
		this.pkUnit = pkUnit;
	}
	public String getSupplyNote(){
        return this.supplyNote;
    }
    public void setSupplyNote(String supplyNote){
        this.supplyNote = supplyNote;
    }

    public Double getQuan(){
        return this.quan;
    }
    public void setQuan(Double quan){
        this.quan = quan;
    }

    public Long getDays(){
        return this.days;
    }
    public void setDays(Long days){
        this.days = days;
    }

    public String getPkDeptExec(){
        return this.pkDeptExec;
    }
    public void setPkDeptExec(String pkDeptExec){
        this.pkDeptExec = pkDeptExec;
    }
    public String getPkOrgExec() {
		return pkOrgExec;
	}
	public void setPkOrgExec(String pkOrgExec) {
		this.pkOrgExec = pkOrgExec;
	}
	public String getFlagDef(){
        return this.flagDef;
    }
    public void setFlagDef(String flagDef){
        this.flagDef = flagDef;
    }

    public String getNameOrd(){
        return this.nameOrd;
    }
    public void setNameOrd(String nameOrd){
        this.nameOrd = nameOrd;
    }

    public String getNote(){
        return this.note;
    }
    public void setNote(String note){
        this.note = note;
    }

    public String getCreator(){
        return this.creator;
    }
    public void setCreator(String creator){
        this.creator = creator;
    }

    public Date getCreateTime(){
        return this.createTime;
    }
    public void setCreateTime(Date createTime){
        this.createTime = createTime;
    }

    public String getModifier(){
        return this.modifier;
    }
    public void setModifier(String modifier){
        this.modifier = modifier;
    }

    public String getDelFlag(){
        return this.delFlag;
    }
    public void setDelFlag(String delFlag){
        this.delFlag = delFlag;
    }

    public Date getTs(){
        return this.ts;
    }
    public void setTs(Date ts){
        this.ts = ts;
    }
	public Integer getSortNo() {
		return sortNo;
	}
	public void setSortNo(Integer sortNo) {
		this.sortNo = sortNo;
	}
	public String getFlagSelf() {
		return flagSelf;
	}
	public void setFlagSelf(String flagSelf) {
		this.flagSelf = flagSelf;
	}
	public String getDtHerbusage() {
		return dtHerbusage;
	}
	public void setDtHerbusage(String dtHerbusage) {
		this.dtHerbusage = dtHerbusage;
	}
    public String getFlagDisp() {
        return flagDisp;
    }

    public void setFlagDisp(String flagDisp) {
        this.flagDisp = flagDisp;
    }

    public String getDtPrestype() {
        return dtPrestype;
    }

    public void setDtPrestype(String dtPrestype) {
        this.dtPrestype = dtPrestype;
    }

    public String getPresNo() {
        return presNo;
    }

    public void setPresNo(String presNo) {
        this.presNo = presNo;
    }

    public String getEuBoil() {
        return euBoil;
    }

    public void setEuBoil(String euBoil) {
        this.euBoil = euBoil;
    }
}