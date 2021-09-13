package com.zebone.nhis.common.module.ex.oi;

import com.zebone.nhis.common.module.BaseModule;
import com.zebone.platform.modules.dao.build.au.Field;
import com.zebone.platform.modules.dao.build.au.Field.KeyId;
import com.zebone.platform.modules.dao.build.au.PK;
import com.zebone.platform.modules.dao.build.au.Table;

import java.util.Date;


/**
 * Table: EX_INFUSION_OCC 
 *
 * @since 2017-10-31 10:03:25
 */
@Table(value="EX_INFUSION_OCC")
public class ExInfusionOcc extends BaseModule  {

	@PK
	@Field(value="PK_INFUOCC",id=KeyId.UUID)
    private String pkInfuocc;

	@Field(value="PK_INFUREG")
    private String pkInfureg;

	@Field(value="REG_DT_NO")
    private String regDtNo;

	@Field(value="OCC_NO")
    private String occNo;

	@Field(value="EU_STATUS")
    private String euStatus;

	@Field(value="EXEC_TIMES")
    private Long execTimes;

	@Field(value="DATE_PLAN")
    private Date datePlan;

	@Field(value="DATE_RECEIVE")
    private Date dateReceive;

	@Field(value="EMP_RECEIVE")
    private String empReceive;

	@Field(value="DATE_ASSIGN")
    private Date dateAssign;

	@Field(value="EMP_ASSIGN")
    private String empAssign;

	@Field(value="DATE_EXEC")
    private Date dateExec;

	@Field(value="EMP_EXEC")
    private String empExec;

	@Field(value="SKIN_TEST_RET")
    private String skinTestRet;

	@Field(value="BEGIN_TIME")
    private Date beginTime;

	@Field(value="END_TIME")
    private Date endTime;

	@Field(value="EMP_WATCH")
    private String empWatch;

	@Field(value="EMP_EYRE")
    private String empEyre;

	@Field(value="EYRE_RECORD")
    private String eyreRecord;

	@Field(value="EMP_CHECK")
    private String empCheck;

	@Field(value="DATE_CHECK")
    private Date dateCheck;

	@Field(value="EMP_FINISH")
    private String empFinish;

	@Field(value="DATE_FINISH")
    private Date dateFinish;

	@Field(value="PK_BED")
    private String pkBed;

	@Field(value="SORT_NO")
    private String sortNo;

	@Field(value="COMMENT_STR")
    private String commentStr;

	@Field(value="MODITY_TIME")
    private Date modityTime;

    @Field(value="PK_PV")
    private String pkPv;

    @Field(value="PK_CNORD")
    private String pkCnord;

    @Field(value="ORDSN_PARENT")
    private Integer ordsnParent;

    @Field(value="BARCODE")
    private String barcode;

    @Field(value="EU_TYPE")
    private String euType;

    @Field(value="PK_DEPT_OCC")
    private String pkDeptOcc;

    @Field(value="SEAT_NO")
    private String seatNo;

    @Field(value="FLAG_PRINT")
    private String flagPrint;

    @Field(value="DATE_PRINT")
    private Date datePrint;

    @Field(value="TIMES_OCC")
    private Integer timesOcc;

    @Field(value="FLAG_ST")
    private String flagSt;

    @Field(value="PK_STOCC")
    private String pkStocc;

    @Field(value="PK_EMP_ADMIX")
    private String pkEmpAdmix;

    @Field(value="NAME_EMP_ADMIX")
    private String nameEmpAdmix;

    @Field(value="DATE_ADMIX")
    private Date dateAdmix;

    @Field(value="SPEED_DRIP")
    private Double speedDrip;

    @Field(value="DATE_BEGIN")
    private Date dateBegin;

    @Field(value="PK_EMP_BEGIN")
    private String pkEmpBegin;

    @Field(value="NAME_EMP_BEGIN")
    private String nameEmpBegin;

    @Field(value="DATE_END")
    private Date dateEnd;

    @Field(value="PK_EMP_END")
    private String pkEmpEnd;

    @Field(value="NAME_EMP_END")
    private String nameEmpEnd;

    @Field(value="FLAG_REACT")
    private String flagReact;

    @Field(value="DATE_REACT")
    private Date dateReact;

    @Field(value="DESC_REACT")
    private String descReact;

    @Field(value="DEAL")
    private String deal;

    @Field(value="DEAL_EFFE")
    private String dealEffe;

    @Field(value="PK_EMP_DEAL")
    private String pkEmpDeal;

    @Field(value="NAME_EMP_DEAL")
    private String nameEmpDeal;

    @Field(value="DATE_DEAL")
    private Date dateDeal;

    @Field(value="FLAG_INC")
    private String flagInc;

    @Field(value="DATE_INC")
    private Date dateInc;

    @Field(value="PK_EMP_INC")
    private String pkEmpInc;

    @Field(value="NAME_EMP_INC")
    private String nameEmpInc;

    @Field(value="DATE_CANC")
    private Date dateCanc;

    @Field(value="PK_EMP_CANC")
    private String pkEmpCanc;

    @Field(value="NAME_EMP_CANC")
    private String nameEmpCanc;

    @Field(value="NOTE")
    private String note;

    @Field(value="MODIFIER")
    private String modifier;

    public String getPkPv() {
        return pkPv;
    }

    public void setPkPv(String pkPv) {
        this.pkPv = pkPv;
    }

    public String getPkCnord() {
        return pkCnord;
    }

    public void setPkCnord(String pkCnord) {
        this.pkCnord = pkCnord;
    }

    public Integer getOrdsnParent() {
        return ordsnParent;
    }

    public void setOrdsnParent(Integer ordsnParent) {
        this.ordsnParent = ordsnParent;
    }

    public String getBarcode() {
        return barcode;
    }

    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }

    public String getEuType() {
        return euType;
    }

    public void setEuType(String euType) {
        this.euType = euType;
    }

    public String getPkDeptOcc() {
        return pkDeptOcc;
    }

    public void setPkDeptOcc(String pkDeptOcc) {
        this.pkDeptOcc = pkDeptOcc;
    }

    public String getSeatNo() {
        return seatNo;
    }

    public void setSeatNo(String seatNo) {
        this.seatNo = seatNo;
    }

    public String getFlagPrint() {
        return flagPrint;
    }

    public void setFlagPrint(String flagPrint) {
        this.flagPrint = flagPrint;
    }

    public Date getDatePrint() {
        return datePrint;
    }

    public void setDatePrint(Date datePrint) {
        this.datePrint = datePrint;
    }

    public Integer getTimesOcc() {
        return timesOcc;
    }

    public void setTimesOcc(Integer timesOcc) {
        this.timesOcc = timesOcc;
    }

    public String getFlagSt() {
        return flagSt;
    }

    public void setFlagSt(String flagSt) {
        this.flagSt = flagSt;
    }

    public String getPkStocc() {
        return pkStocc;
    }

    public void setPkStocc(String pkStocc) {
        this.pkStocc = pkStocc;
    }

    public String getPkEmpAdmix() {
        return pkEmpAdmix;
    }

    public void setPkEmpAdmix(String pkEmpAdmix) {
        this.pkEmpAdmix = pkEmpAdmix;
    }

    public String getNameEmpAdmix() {
        return nameEmpAdmix;
    }

    public void setNameEmpAdmix(String nameEmpAdmix) {
        this.nameEmpAdmix = nameEmpAdmix;
    }

    public Date getDateAdmix() {
        return dateAdmix;
    }

    public void setDateAdmix(Date dateAdmix) {
        this.dateAdmix = dateAdmix;
    }

    public Double getSpeedDrip() {
        return speedDrip;
    }

    public void setSpeedDrip(Double speedDrip) {
        this.speedDrip = speedDrip;
    }

    public Date getDateBegin() {
        return dateBegin;
    }

    public void setDateBegin(Date dateBegin) {
        this.dateBegin = dateBegin;
    }

    public String getPkEmpBegin() {
        return pkEmpBegin;
    }

    public void setPkEmpBegin(String pkEmpBegin) {
        this.pkEmpBegin = pkEmpBegin;
    }

    public String getNameEmpBegin() {
        return nameEmpBegin;
    }

    public void setNameEmpBegin(String nameEmpBegin) {
        this.nameEmpBegin = nameEmpBegin;
    }

    public Date getDateEnd() {
        return dateEnd;
    }

    public void setDateEnd(Date dateEnd) {
        this.dateEnd = dateEnd;
    }

    public String getPkEmpEnd() {
        return pkEmpEnd;
    }

    public void setPkEmpEnd(String pkEmpEnd) {
        this.pkEmpEnd = pkEmpEnd;
    }

    public String getNameEmpEnd() {
        return nameEmpEnd;
    }

    public void setNameEmpEnd(String nameEmpEnd) {
        this.nameEmpEnd = nameEmpEnd;
    }

    public String getFlagReact() {
        return flagReact;
    }

    public void setFlagReact(String flagReact) {
        this.flagReact = flagReact;
    }

    public Date getDateReact() {
        return dateReact;
    }

    public void setDateReact(Date dateReact) {
        this.dateReact = dateReact;
    }

    public String getDescReact() {
        return descReact;
    }

    public void setDescReact(String descReact) {
        this.descReact = descReact;
    }

    public String getDeal() {
        return deal;
    }

    public void setDeal(String deal) {
        this.deal = deal;
    }

    public String getDealEffe() {
        return dealEffe;
    }

    public void setDealEffe(String dealEffe) {
        this.dealEffe = dealEffe;
    }

    public String getPkEmpDeal() {
        return pkEmpDeal;
    }

    public void setPkEmpDeal(String pkEmpDeal) {
        this.pkEmpDeal = pkEmpDeal;
    }

    public String getNameEmpDeal() {
        return nameEmpDeal;
    }

    public void setNameEmpDeal(String nameEmpDeal) {
        this.nameEmpDeal = nameEmpDeal;
    }

    public Date getDateDeal() {
        return dateDeal;
    }

    public void setDateDeal(Date dateDeal) {
        this.dateDeal = dateDeal;
    }

    public String getFlagInc() {
        return flagInc;
    }

    public void setFlagInc(String flagInc) {
        this.flagInc = flagInc;
    }

    public Date getDateInc() {
        return dateInc;
    }

    public void setDateInc(Date dateInc) {
        this.dateInc = dateInc;
    }

    public String getPkEmpInc() {
        return pkEmpInc;
    }

    public void setPkEmpInc(String pkEmpInc) {
        this.pkEmpInc = pkEmpInc;
    }

    public String getNameEmpInc() {
        return nameEmpInc;
    }

    public void setNameEmpInc(String nameEmpInc) {
        this.nameEmpInc = nameEmpInc;
    }

    public Date getDateCanc() {
        return dateCanc;
    }

    public void setDateCanc(Date dateCanc) {
        this.dateCanc = dateCanc;
    }

    public String getPkEmpCanc() {
        return pkEmpCanc;
    }

    public void setPkEmpCanc(String pkEmpCanc) {
        this.pkEmpCanc = pkEmpCanc;
    }

    public String getNameEmpCanc() {
        return nameEmpCanc;
    }

    public void setNameEmpCanc(String nameEmpCanc) {
        this.nameEmpCanc = nameEmpCanc;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    @Override
    public String getModifier() {
        return modifier;
    }

    @Override
    public void setModifier(String modifier) {
        this.modifier = modifier;
    }

    public String getPkInfuocc(){
        return this.pkInfuocc;
    }
    public void setPkInfuocc(String pkInfuocc){
        this.pkInfuocc = pkInfuocc;
    }

    public String getPkInfureg(){
        return this.pkInfureg;
    }
    public void setPkInfureg(String pkInfureg){
        this.pkInfureg = pkInfureg;
    }

    public String getRegDtNo(){
        return this.regDtNo;
    }
    public void setRegDtNo(String regDtNo){
        this.regDtNo = regDtNo;
    }

    public String getOccNo(){
        return this.occNo;
    }
    public void setOccNo(String occNo){
        this.occNo = occNo;
    }

    public String getEuStatus(){
        return this.euStatus;
    }
    public void setEuStatus(String euStatus){
        this.euStatus = euStatus;
    }

    public Long getExecTimes(){
        return this.execTimes;
    }
    public void setExecTimes(Long execTimes){
        this.execTimes = execTimes;
    }

    public Date getDatePlan(){
        return this.datePlan;
    }
    public void setDatePlan(Date datePlan){
        this.datePlan = datePlan;
    }

    public Date getDateReceive(){
        return this.dateReceive;
    }
    public void setDateReceive(Date dateReceive){
        this.dateReceive = dateReceive;
    }

    public String getEmpReceive(){
        return this.empReceive;
    }
    public void setEmpReceive(String empReceive){
        this.empReceive = empReceive;
    }

    public Date getDateAssign(){
        return this.dateAssign;
    }
    public void setDateAssign(Date dateAssign){
        this.dateAssign = dateAssign;
    }

    public String getEmpAssign(){
        return this.empAssign;
    }
    public void setEmpAssign(String empAssign){
        this.empAssign = empAssign;
    }

    public Date getDateExec(){
        return this.dateExec;
    }
    public void setDateExec(Date dateExec){
        this.dateExec = dateExec;
    }

    public String getEmpExec(){
        return this.empExec;
    }
    public void setEmpExec(String empExec){
        this.empExec = empExec;
    }

    public String getSkinTestRet(){
        return this.skinTestRet;
    }
    public void setSkinTestRet(String skinTestRet){
        this.skinTestRet = skinTestRet;
    }

    public Date getBeginTime(){
        return this.beginTime;
    }
    public void setBeginTime(Date beginTime){
        this.beginTime = beginTime;
    }

    public Date getEndTime(){
        return this.endTime;
    }
    public void setEndTime(Date endTime){
        this.endTime = endTime;
    }

    public String getEmpWatch(){
        return this.empWatch;
    }
    public void setEmpWatch(String empWatch){
        this.empWatch = empWatch;
    }

    public String getEmpEyre(){
        return this.empEyre;
    }
    public void setEmpEyre(String empEyre){
        this.empEyre = empEyre;
    }

    public String getEyreRecord(){
        return this.eyreRecord;
    }
    public void setEyreRecord(String eyreRecord){
        this.eyreRecord = eyreRecord;
    }

    public String getEmpCheck(){
        return this.empCheck;
    }
    public void setEmpCheck(String empCheck){
        this.empCheck = empCheck;
    }

    public Date getDateCheck(){
        return this.dateCheck;
    }
    public void setDateCheck(Date dateCheck){
        this.dateCheck = dateCheck;
    }

    public String getEmpFinish(){
        return this.empFinish;
    }
    public void setEmpFinish(String empFinish){
        this.empFinish = empFinish;
    }

    public Date getDateFinish(){
        return this.dateFinish;
    }
    public void setDateFinish(Date dateFinish){
        this.dateFinish = dateFinish;
    }

    public String getPkBed(){
        return this.pkBed;
    }
    public void setPkBed(String pkBed){
        this.pkBed = pkBed;
    }

    public String getSortNo(){
        return this.sortNo;
    }
    public void setSortNo(String sortNo){
        this.sortNo = sortNo;
    }

    public String getCommentStr(){
        return this.commentStr;
    }
    public void setCommentStr(String commentStr){
        this.commentStr = commentStr;
    }

    public Date getModityTime(){
        return this.modityTime;
    }
    public void setModityTime(Date modityTime){
        this.modityTime = modityTime;
    }
}