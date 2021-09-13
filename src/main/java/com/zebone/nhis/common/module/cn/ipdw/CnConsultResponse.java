package com.zebone.nhis.common.module.cn.ipdw;

import java.util.Date;

import com.zebone.nhis.common.module.BaseModule;
import com.zebone.platform.modules.dao.build.au.Field;
import com.zebone.platform.modules.dao.build.au.Field.FieldType;
import com.zebone.platform.modules.dao.build.au.Field.KeyId;
import com.zebone.platform.modules.dao.build.au.PK;
import com.zebone.platform.modules.dao.build.au.Table;


/**
 * Table: CN_CONSULT_RESPONSE 
 *
 * @since 2016-09-12 10:30:28
 */
@Table(value="CN_CONSULT_RESPONSE")
public class CnConsultResponse  extends BaseModule {

	@PK
	@Field(value="PK_CONSREP",id=KeyId.UUID)
    private String pkConsrep;

	@Field(value="PK_ORG_REP")
    private String pkOrgRep;

	@Field(value="PK_DEPT_REP")
    private String pkDeptRep;

	@Field(value="PK_CONS")
    private String pkCons;

	@Field(value="DATE_REP")
    private Date dateRep;

	@Field(value="FLAG_REP")
    private String flagRep;

	@Field(value="PK_EMP_REP")
    private String pkEmpRep;

	@Field(value="NAME_EMP_REP")
    private String nameEmpRep;

	@Field(value="CON_REPLY")
    private String conReply;

	@Field(value="CON_ADVICE")
    private String conAdvice;

	@Field(value="DATE_INPUT")
    private Date dateInput;

	@Field(value="PK_EMP_INPUT")
    private String pkEmpInput;

	@Field(value="NAME_EMP_INPUT")
    private String nameEmpInput;

	@Field(value="MODITY_TIME")
    private Date modityTime;
	
	@Field(value="NAME_ORG_REP")
	private String nameOrgRep;
	
	@Field(value="NAME_DEPT_REP")
	private String nameDeptRep;
	
	@Field(value="FLAG_FINISH")
	private String flagFinish;

	@Field(value = "DT_CONLEVEL")
    private  String dtConlevel; //会诊应答表中的会诊级别

    @Field(value = "PLACE_DEPT_REP")
    private String placeDeptRep; /**应答科室位置*/

    public String getNameOrgRep() {
		return nameOrgRep;
	}
	public void setNameOrgRep(String nameOrgRep) {
		this.nameOrgRep = nameOrgRep;
	}
	public String getNameDeptRep() {
		return nameDeptRep;
	}
	public void setNameDeptRep(String nameDeptRep) {
		this.nameDeptRep = nameDeptRep;
	}
	public String getPkConsrep(){
        return this.pkConsrep;
    }
    public void setPkConsrep(String pkConsrep){
        this.pkConsrep = pkConsrep;
    }

    public String getPkOrgRep(){
        return this.pkOrgRep;
    }
    public void setPkOrgRep(String pkOrgRep){
        this.pkOrgRep = pkOrgRep;
    }

    public String getPkDeptRep(){
        return this.pkDeptRep;
    }
    public void setPkDeptRep(String pkDeptRep){
        this.pkDeptRep = pkDeptRep;
    }

    public String getPkCons(){
        return this.pkCons;
    }
    public void setPkCons(String pkCons){
        this.pkCons = pkCons;
    }

    public Date getDateRep(){
        return this.dateRep;
    }
    public void setDateRep(Date dateRep){
        this.dateRep = dateRep;
    }

    public String getFlagRep(){
        return this.flagRep;
    }
    public void setFlagRep(String flagRep){
        this.flagRep = flagRep;
    }

    public String getPkEmpRep(){
        return this.pkEmpRep;
    }
    public void setPkEmpRep(String pkEmpRep){
        this.pkEmpRep = pkEmpRep;
    }

    public String getNameEmpRep(){
        return this.nameEmpRep;
    }
    public void setNameEmpRep(String nameEmpRep){
        this.nameEmpRep = nameEmpRep;
    }

    public String getConReply(){
        return this.conReply;
    }
    public void setConReply(String conReply){
        this.conReply = conReply;
    }

    public String getConAdvice(){
        return this.conAdvice;
    }
    public void setConAdvice(String conAdvice){
        this.conAdvice = conAdvice;
    }

    public Date getDateInput(){
        return this.dateInput;
    }
    public void setDateInput(Date dateInput){
        this.dateInput = dateInput;
    }

    public String getPkEmpInput(){
        return this.pkEmpInput;
    }
    public void setPkEmpInput(String pkEmpInput){
        this.pkEmpInput = pkEmpInput;
    }

    public String getNameEmpInput(){
        return this.nameEmpInput;
    }
    public void setNameEmpInput(String nameEmpInput){
        this.nameEmpInput = nameEmpInput;
    }

    public Date getModityTime(){
        return this.modityTime;
    }
    public void setModityTime(Date modityTime){
        this.modityTime = modityTime;
    }
	public String getFlagFinish() {
		return flagFinish;
	}
	public void setFlagFinish(String flagFinish) {
		this.flagFinish = flagFinish;
	}

    public String getDtConlevel() {
        return dtConlevel;
    }

    public void setDtConlevel(String dtConlevel) {
        this.dtConlevel = dtConlevel;
    }

    public String getPlaceDeptRep() {
        return placeDeptRep;
    }

    public void setPlaceDeptRep(String placeDeptRep) {
        this.placeDeptRep = placeDeptRep;
    }
}