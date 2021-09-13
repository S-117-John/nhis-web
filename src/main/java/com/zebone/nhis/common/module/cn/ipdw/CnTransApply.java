package com.zebone.nhis.common.module.cn.ipdw;

import java.util.Date;

import com.zebone.nhis.common.module.BaseModule;
import com.zebone.platform.modules.dao.build.au.Field;
import com.zebone.platform.modules.dao.build.au.Field.FieldType;
import com.zebone.platform.modules.dao.build.au.Field.KeyId;
import com.zebone.platform.modules.dao.build.au.PK;
import com.zebone.platform.modules.dao.build.au.Table;


/**
 * Table: CN_TRANS_APPLY 
 *
 * @since 2016-09-12 10:30:28
 */
@Table(value="CN_TRANS_APPLY")
public class CnTransApply extends BaseModule  {

	@PK
	@Field(value="PK_ORDBT",id=KeyId.UUID)
    private String pkOrdbt;

	@Field(value="PK_CNORD")
    private String pkCnord;

	@Field(value="PK_DIAG")
    private String pkDiag;

	@Field(value="DESC_DIAG")
    private String descDiag;

	@Field(value="DT_BTTYPE")
    private String dtBttype;

	@Field(value="DT_BT_ABO")
    private String dtBtAbo;

	@Field(value="DT_BT_RH")
    private String dtBtRh;

	@Field(value="FLAG_LAB")
    private String flagLab;

	@Field(value="FLAG_BTHIS")
    private String flagBthis;

	@Field(value="BT_CONTENT")
    private String btContent;

	@Field(value="PK_UNIT_BT")
    private String pkUnitBt;

	@Field(value="QUAN_BT")
    private Double quanBt;

	@Field(value="DATE_PLAN")
    private Date datePlan;

	@Field(value="FLAG_PREG")
    private String flagPreg;

	@Field(value="FLAG_AL")
    private String flagAl;

	@Field(value="FLAG_BP")
    private String flagBp;

	@Field(value="DATE_BP")
    private Date dateBp;

	@Field(value="QUAN_BP")
    private Double quanBp;

	@Field(value="BARCODE_BP")
    private String barcodeBp;

	@Field(value="PK_EMP_BP")
    private String pkEmpBp;

	@Field(value="NAME_EMP_BP")
    private String nameEmpBp;

	@Field(value="EU_STATUS")
    private String euStatus;

	@Field(value="NOTE")
    private String note;

	@Field(userfield="pkEmp",userfieldscop=FieldType.ALL)
    private String modifier;

	@Field(value="MODITY_TIME")
    private Date modityTime;

	@Field(value="DESC_BTHIS")
	private  String descBthis; //输血史信息

    @Field(value="CNT_PREG")
    private  Integer cntPreg; //怀孕次数

    @Field(value="CNT_LABOR")
    private  Integer cntLabor; //分娩次数

    @Field(value="CODE_APPLY")
    private  String codeApply; //关联检验申请

    @Field(value="EU_PRE_ABORH")
    private String euPreAborh;//预定输血血型

    public String getPkOrdbt(){
        return this.pkOrdbt;
    }
    public void setPkOrdbt(String pkOrdbt){
        this.pkOrdbt = pkOrdbt;
    }

    public String getPkCnord(){
        return this.pkCnord;
    }
    public void setPkCnord(String pkCnord){
        this.pkCnord = pkCnord;
    }

    public String getPkDiag(){
        return this.pkDiag;
    }
    public void setPkDiag(String pkDiag){
        this.pkDiag = pkDiag;
    }

    public String getDescDiag(){
        return this.descDiag;
    }
    public void setDescDiag(String descDiag){
        this.descDiag = descDiag;
    }

    public String getDtBttype(){
        return this.dtBttype;
    }
    public void setDtBttype(String dtBttype){
        this.dtBttype = dtBttype;
    }

    public String getDtBtAbo(){
        return this.dtBtAbo;
    }
    public void setDtBtAbo(String dtBtAbo){
        this.dtBtAbo = dtBtAbo;
    }

    public String getDtBtRh(){
        return this.dtBtRh;
    }
    public void setDtBtRh(String dtBtRh){
        this.dtBtRh = dtBtRh;
    }

    public String getFlagLab(){
        return this.flagLab;
    }
    public void setFlagLab(String flagLab){
        this.flagLab = flagLab;
    }

    public String getFlagBthis(){
        return this.flagBthis;
    }
    public void setFlagBthis(String flagBthis){
        this.flagBthis = flagBthis;
    }

    public String getBtContent(){
        return this.btContent;
    }
    public void setBtContent(String btContent){
        this.btContent = btContent;
    }

    public String getPkUnitBt(){
        return this.pkUnitBt;
    }
    public void setPkUnitBt(String pkUnitBt){
        this.pkUnitBt = pkUnitBt;
    }

    public Double getQuanBt(){
        return this.quanBt;
    }
    public void setQuanBt(Double quanBt){
        this.quanBt = quanBt;
    }

    public Date getDatePlan(){
        return this.datePlan;
    }
    public void setDatePlan(Date datePlan){
        this.datePlan = datePlan;
    }

    public String getFlagPreg(){
        return this.flagPreg;
    }
    public void setFlagPreg(String flagPreg){
        this.flagPreg = flagPreg;
    }

    public String getFlagAl(){
        return this.flagAl;
    }
    public void setFlagAl(String flagAl){
        this.flagAl = flagAl;
    }

    public String getFlagBp(){
        return this.flagBp;
    }
    public void setFlagBp(String flagBp){
        this.flagBp = flagBp;
    }

    public Date getDateBp(){
        return this.dateBp;
    }
    public void setDateBp(Date dateBp){
        this.dateBp = dateBp;
    }

    public Double getQuanBp(){
        return this.quanBp;
    }
    public void setQuanBp(Double quanBp){
        this.quanBp = quanBp;
    }

    public String getBarcodeBp(){
        return this.barcodeBp;
    }
    public void setBarcodeBp(String barcodeBp){
        this.barcodeBp = barcodeBp;
    }

    public String getPkEmpBp(){
        return this.pkEmpBp;
    }
    public void setPkEmpBp(String pkEmpBp){
        this.pkEmpBp = pkEmpBp;
    }

    public String getNameEmpBp(){
        return this.nameEmpBp;
    }
    public void setNameEmpBp(String nameEmpBp){
        this.nameEmpBp = nameEmpBp;
    }

    public String getEuStatus(){
        return this.euStatus;
    }
    public void setEuStatus(String euStatus){
        this.euStatus = euStatus;
    }

    public String getNote(){
        return this.note;
    }
    public void setNote(String note){
        this.note = note;
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

    public String getDescBthis() {
        return descBthis;
    }

    public void setDescBthis(String descBthis) {
        this.descBthis = descBthis;
    }

    public Integer getCntPreg() {
        return cntPreg;
    }

    public void setCntPreg(Integer cntPreg) {
        this.cntPreg = cntPreg;
    }

    public Integer getCntLabor() {
        return cntLabor;
    }

    public void setCntLabor(Integer cntLabor) {
        this.cntLabor = cntLabor;
    }

    public String getCodeApply() {
        return codeApply;
    }

    public void setCodeApply(String codeApply) {
        this.codeApply = codeApply;
    }

    public String getEuPreAborh() {
        return euPreAborh;
    }

    public void setEuPreAborh(String euPreAborh) {
        this.euPreAborh = euPreAborh;
    }
}