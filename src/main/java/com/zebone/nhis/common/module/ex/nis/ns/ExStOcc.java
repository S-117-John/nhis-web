package com.zebone.nhis.common.module.ex.nis.ns;

import com.zebone.nhis.common.module.BaseModule;
import com.zebone.platform.modules.dao.build.au.Field;
import com.zebone.platform.modules.dao.build.au.Field.KeyId;
import com.zebone.platform.modules.dao.build.au.PK;
import com.zebone.platform.modules.dao.build.au.Table;

import java.util.Date;


/**
 * Table: EX_ST_OCC - ex_st_occ 
 *
 * @since 2016-10-24 03:39:35
 */
@Table(value="EX_ST_OCC")
public class ExStOcc extends BaseModule  {

	@PK
	@Field(value="PK_STOCC",id=KeyId.UUID)
    private String pkStocc;

	@Field(value="PK_PI")
    private String pkPi;

	@Field(value="PK_PV")
    private String pkPv;

	@Field(value="PK_CNORD")
    private String pkCnord;

	@Field(value="PK_ORG_OCC")
    private String pkOrgOcc;

	@Field(value="PK_DEPT_OCC")
    private String pkDeptOcc;

	@Field(value="PK_EMP_OCC")
    private String pkEmpOcc;

	@Field(value="NAME_EMP_OCC")
    private String nameEmpOcc;

	@Field(value="DATE_OCC")
    private Date dateOcc;

	@Field(value="DATE_BEGIN_ST")
    private Date dateBeginSt;

	@Field(value="DUARTION")
    private Integer duartion;

    /** EU_STMODE - 0皮内试验；1挑刺试验；2斑贴试验 */
	@Field(value="EU_STMODE")
    private String euStmode;

	@Field(value="LENGTH_CAL")
    private Double lengthCal;

	@Field(value="WIDTH_CAL")
    private Double widthCal;

	@Field(value="LENGTH_VEL")
    private Double lengthVel;

	@Field(value="WIDTH_VEL")
    private Double widthVel;

	@Field(value="LENGTH_SWO")
    private Double lengthSwo;

	@Field(value="WIDTH_SWO")
    private Double widthSwo;

	@Field(value="FLAG_NEC")
    private String flagNec;

	@Field(value="FLAG_LYM")
    private String flagLym;

	@Field(value="RESULT")
    private String result;

	@Field(value="PK_DEPT_AP")
    private String pkDeptAp;

	@Field(value="PK_DEPT_NS_AP")
    private String pkDeptNsAp;

	@Field(value="FLAG_CHK")
    private String flagChk;

	@Field(value="DATE_CHK")
    private Date dateChk;

	@Field(value="PK_EMP_CHK")
    private String pkEmpChk;

	@Field(value="NAME_EMP_CHK")
    private String nameEmpChk;

	@Field(value="MODITY_TIME")
    private Date modityTime;

    /** QUAN - 左手皮试单位数 */
    @Field(value="QUAN")
    private Double quan;

    /** FLAG_CAL - 左手硬结 */
    @Field(value="FLAG_CAL")
    private String flagCal;

    /** QUAN_RIGHT - 右手皮试单位数 */
    @Field(value="QUAN_RIGHT")
    private Double quanRight;

    /** FLAG_CAL_RIGHT - 右手硬结 */
    @Field(value="FLAG_CAL_RIGHT")
    private String flagCalRight;

    /** LENGTH_VAL_RIGHT - 右手硬结长度(纵径) */
    @Field(value="LENGTH_VAL_RIGHT")
    private Double lengthValRight;

    /** WIDTH_VAL_RIGHT - 右手硬结宽度(横径) */
    @Field(value="WIDTH_VAL_RIGHT")
    private Double widthValRight;

    /** EU_SKIN_RIGHT - 0 未见异常，1  双圈，2 水泡，3 坏死，4 淋巴管炎 */
    @Field(value="EU_SKIN_RIGHT")
    private String euSkinRight;

    /** EU_SKIN - 0 未见异常，1  双圈，2 水泡，3 坏死，4 淋巴管炎 */
    @Field(value="EU_SKIN")
    private String euSkin;
    
    @Field(value="BATCH_NO")
    private String batchNo;

    public String getBatchNo() {
		return batchNo;
	}
	public void setBatchNo(String batchNo) {
		this.batchNo = batchNo;
	}
    
    public Double getQuan() {
        return quan;
    }

    public void setQuan(Double quan) {
        this.quan = quan;
    }

    public String getFlagCal() {
        return flagCal;
    }

    public void setFlagCal(String flagCal) {
        this.flagCal = flagCal;
    }

    public Double getQuanRight() {
        return quanRight;
    }

    public void setQuanRight(Double quanRight) {
        this.quanRight = quanRight;
    }

    public String getFlagCalRight() {
        return flagCalRight;
    }

    public void setFlagCalRight(String flagCalRight) {
        this.flagCalRight = flagCalRight;
    }

    public Double getLengthValRight() {
        return lengthValRight;
    }

    public void setLengthValRight(Double lengthValRight) {
        this.lengthValRight = lengthValRight;
    }

    public Double getWidthValRight() {
        return widthValRight;
    }

    public void setWidthValRight(Double widthValRight) {
        this.widthValRight = widthValRight;
    }

    public String getEuSkinRight() {
        return euSkinRight;
    }

    public void setEuSkinRight(String euSkinRight) {
        this.euSkinRight = euSkinRight;
    }

    public String getEuSkin() {
        return euSkin;
    }

    public void setEuSkin(String euSkin) {
        this.euSkin = euSkin;
    }

    public String getPkStocc(){
        return this.pkStocc;
    }
    public void setPkStocc(String pkStocc){
        this.pkStocc = pkStocc;
    }

    public String getPkPi(){
        return this.pkPi;
    }
    public void setPkPi(String pkPi){
        this.pkPi = pkPi;
    }

    public String getPkPv(){
        return this.pkPv;
    }
    public void setPkPv(String pkPv){
        this.pkPv = pkPv;
    }

    public String getPkCnord(){
        return this.pkCnord;
    }
    public void setPkCnord(String pkCnord){
        this.pkCnord = pkCnord;
    }

    public String getPkOrgOcc(){
        return this.pkOrgOcc;
    }
    public void setPkOrgOcc(String pkOrgOcc){
        this.pkOrgOcc = pkOrgOcc;
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

    public Date getDateOcc(){
        return this.dateOcc;
    }
    public void setDateOcc(Date dateOcc){
        this.dateOcc = dateOcc;
    }

    public Date getDateBeginSt(){
        return this.dateBeginSt;
    }
    public void setDateBeginSt(Date dateBeginSt){
        this.dateBeginSt = dateBeginSt;
    }

    public Integer getDuartion(){
        return this.duartion;
    }
    public void setDuartion(Integer duartion){
        this.duartion = duartion;
    }

    public String getEuStmode(){
        return this.euStmode;
    }
    public void setEuStmode(String euStmode){
        this.euStmode = euStmode;
    }

    public Double getLengthCal(){
        return this.lengthCal;
    }
    public void setLengthCal(Double lengthCal){
        this.lengthCal = lengthCal;
    }

    public Double getWidthCal(){
        return this.widthCal;
    }
    public void setWidthCal(Double widthCal){
        this.widthCal = widthCal;
    }

    public Double getLengthVel(){
        return this.lengthVel;
    }
    public void setLengthVel(Double lengthVel){
        this.lengthVel = lengthVel;
    }

    public Double getWidthVel(){
        return this.widthVel;
    }
    public void setWidthVel(Double widthVel){
        this.widthVel = widthVel;
    }

    public Double getLengthSwo(){
        return this.lengthSwo;
    }
    public void setLengthSwo(Double lengthSwo){
        this.lengthSwo = lengthSwo;
    }

    public Double getWidthSwo(){
        return this.widthSwo;
    }
    public void setWidthSwo(Double widthSwo){
        this.widthSwo = widthSwo;
    }

    public String getFlagNec(){
        return this.flagNec;
    }
    public void setFlagNec(String flagNec){
        this.flagNec = flagNec;
    }

    public String getFlagLym(){
        return this.flagLym;
    }
    public void setFlagLym(String flagLym){
        this.flagLym = flagLym;
    }
    public String getResult() {
		return result;
	}
	public void setResult(String result) {
		this.result = result;
	}
	public String getPkDeptAp(){
        return this.pkDeptAp;
    }
    public void setPkDeptAp(String pkDeptAp){
        this.pkDeptAp = pkDeptAp;
    }

    public String getPkDeptNsAp(){
        return this.pkDeptNsAp;
    }
    public void setPkDeptNsAp(String pkDeptNsAp){
        this.pkDeptNsAp = pkDeptNsAp;
    }

    public String getFlagChk(){
        return this.flagChk;
    }
    public void setFlagChk(String flagChk){
        this.flagChk = flagChk;
    }

    public Date getDateChk(){
        return this.dateChk;
    }
    public void setDateChk(Date dateChk){
        this.dateChk = dateChk;
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

    public Date getModityTime(){
        return this.modityTime;
    }
    public void setModityTime(Date modityTime){
        this.modityTime = modityTime;
    }
}