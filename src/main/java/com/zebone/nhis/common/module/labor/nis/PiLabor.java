package com.zebone.nhis.common.module.labor.nis;

import java.util.Date;

import com.zebone.nhis.common.module.BaseModule;
import com.zebone.platform.modules.dao.build.au.Field;
import com.zebone.platform.modules.dao.build.au.Field.FieldType;
import com.zebone.platform.modules.dao.build.au.Field.KeyId;
import com.zebone.platform.modules.dao.build.au.PK;
import com.zebone.platform.modules.dao.build.au.Table;


/**
 * Table: PI_LABOR - PI_LABOR 
 *
 * @since 2017-05-18 04:35:09
 */
@Table(value="PI_LABOR")
public class PiLabor extends BaseModule  {

	@PK
	@Field(value="PK_PILABOR",id=KeyId.UUID)
    private String pkPilabor;

	@Field(value="PK_PI")
    private String pkPi;

	@Field(value="CARD_NO")
    private String cardNo;

	@Field(value="DATE_IN")
    private Date dateIn;

	@Field(value="CODE_BIRTHPER")
    private String codeBirthper;

	@Field(value="CODE_TMPPER")
    private String codeTmpper;

	@Field(value="DATE_TMPPER")
    private Date dateTmpper;

	@Field(value="DT_REGTYPE")
    private String dtRegtype;

	@Field(value="DT_CENSTYPE")
    private String dtCenstype;

	@Field(value="FLAG_REASONABLE")
    private String flagReasonable;

	@Field(value="FLAG_SMOKE")
    private String flagSmoke;
	
	@Field(value="FLAG_PLAN")
    private String flagPlan;

	@Field(value="IDNO_HUS")
    private String idnoHus;

	@Field(value="NAME_HUS")
    private String nameHus;

	@Field(value="DT_OCCU_HUS")
    private String dtOccuHus;

	@Field(value="BIRTH_DATE_HUS")
    private Date birthDateHus;

	@Field(value="DT_COUNTRY_HUS")
    private String dtCountryHus;

	@Field(value="DT_NATION_HUS")
    private String dtNationHus;

	@Field(value="DT_BLOOD_HUS")
    private String dtBloodHus;

	@Field(value="MOBILE_HUS")
    private String mobileHus;
	
	@Field(value="DT_REGTYPE_HUS")
    private String dtRegtypeHus;

	/**丈夫户口地址描述编码（县区）*/
	@Field(value="ADDRCODE_REGI_HUS")
	private String addrcodeRegiHus;
	
	/**丈夫户口地址描述*/
	@Field(value="ADDR_REGI_HUS")
	private String addrRegiHus;
	
	/**丈夫户口地址详细（街道）*/
	@Field(value="ADDR_REGI_DT_HUS")
	private String addrRegiDtHus;
	
	/**丈夫现住址 编码（县区）*/
	@Field(value="ADDRCODE_CUR_HUS")
	private String addrcodeCurHus;
	
	/**丈夫现住址描述*/
	@Field(value="ADDR_CUR_HUS")
	private String addrCurHus;
	
	/**丈夫现住址详细（街道）*/
	@Field(value="ADDR_CUR_DT_HUS")
	private String addrCurDtHus;
	
	@Field(value="INCOME")
    private String income;

	@Field(value="FLAG_FOLIC")
    private String flagFolic;

	@Field(value="NUM_BEFORE")
    private Integer numBefore;

	@Field(value="NUM_EARLY")
    private Integer numEarly;

	@Field(value="FLAG_NORMAL")
    private String flagNormal;

	@Field(value="DATE_LAST")
    private Date dateLast;

	@Field(value="NUM_PREG")
    private Integer numPreg;

	@Field(value="NUM_PRODUCT")
    private Integer numProduct;

	@Field(value="WEEKNO")
    private String weekno;

	@Field(value="DATE_PREBIRTH")
    private Date datePrebirth;

	@Field(value="PERIOD_MENSES")
    private Integer periodMenses;

	@Field(value="NAME_CREATE")
    private String nameCreate;

	@Field(value="NOTE")
    private String note;

	@Field(value="PK_EMP_DOC")
    private String pkEmpDoc;

	@Field(value="NAME_EMP_DOC")
    private String nameEmpDoc;

	@Field(value="MODIFY_TIME")
    private Date modifyTime;
    
	public String getPkPilabor(){
        return this.pkPilabor;
    }
    public void setPkPilabor(String pkPilabor){
        this.pkPilabor = pkPilabor;
    }

    public String getPkOrg(){
        return this.pkOrg;
    }
    public void setPkOrg(String pkOrg){
        this.pkOrg = pkOrg;
    }

    public String getPkPi(){
        return this.pkPi;
    }
    public void setPkPi(String pkPi){
        this.pkPi = pkPi;
    }

    public String getCardNo(){
        return this.cardNo;
    }
    public void setCardNo(String cardNo){
        this.cardNo = cardNo;
    }

    public Date getDateIn(){
        return this.dateIn;
    }
    public void setDateIn(Date dateIn){
        this.dateIn = dateIn;
    }

    public String getCodeBirthper(){
        return this.codeBirthper;
    }
    public void setCodeBirthper(String codeBirthper){
        this.codeBirthper = codeBirthper;
    }

    public String getCodeTmpper(){
        return this.codeTmpper;
    }
    public void setCodeTmpper(String codeTmpper){
        this.codeTmpper = codeTmpper;
    }

    public Date getDateTmpper(){
        return this.dateTmpper;
    }
    public void setDateTmpper(Date dateTmpper){
        this.dateTmpper = dateTmpper;
    }

    public String getDtRegtype(){
        return this.dtRegtype;
    }
    public void setDtRegtype(String dtRegtype){
        this.dtRegtype = dtRegtype;
    }

    public String getDtCenstype(){
        return this.dtCenstype;
    }
    public void setDtCenstype(String dtCenstype){
        this.dtCenstype = dtCenstype;
    }
    public String getIdnoHus(){
        return this.idnoHus;
    }
    public void setIdnoHus(String idnoHus){
        this.idnoHus = idnoHus;
    }

    public String getNameHus(){
        return this.nameHus;
    }
    public void setNameHus(String nameHus){
        this.nameHus = nameHus;
    }

    public String getDtOccuHus(){
        return this.dtOccuHus;
    }
    public void setDtOccuHus(String dtOccuHus){
        this.dtOccuHus = dtOccuHus;
    }

    public Date getBirthDateHus(){
        return this.birthDateHus;
    }
    public void setBirthDateHus(Date birthDateHus){
        this.birthDateHus = birthDateHus;
    }

    public String getDtCountryHus(){
        return this.dtCountryHus;
    }
    public void setDtCountryHus(String dtCountryHus){
        this.dtCountryHus = dtCountryHus;
    }

    public String getDtNationHus(){
        return this.dtNationHus;
    }
    public void setDtNationHus(String dtNationHus){
        this.dtNationHus = dtNationHus;
    }

    public String getDtBloodHus(){
        return this.dtBloodHus;
    }
    public void setDtBloodHus(String dtBloodHus){
        this.dtBloodHus = dtBloodHus;
    }

    public String getMobileHus(){
        return this.mobileHus;
    }
    public void setMobileHus(String mobileHus){
        this.mobileHus = mobileHus;
    }

    public String getDtRegtypeHus(){
        return this.dtRegtypeHus;
    }
    public void setDtRegtypeHus(String dtRegtypeHus){
        this.dtRegtypeHus = dtRegtypeHus;
    }

    public String getAddrcodeRegiHus() {
		return addrcodeRegiHus;
	}
	public void setAddrcodeRegiHus(String addrcodeRegiHus) {
		this.addrcodeRegiHus = addrcodeRegiHus;
	}
	public String getAddrRegiHus() {
		return addrRegiHus;
	}
	public void setAddrRegiHus(String addrRegiHus) {
		this.addrRegiHus = addrRegiHus;
	}
	public String getAddrRegiDtHus() {
		return addrRegiDtHus;
	}
	public void setAddrRegiDtHus(String addrRegiDtHus) {
		this.addrRegiDtHus = addrRegiDtHus;
	}
	public String getAddrcodeCurHus() {
		return addrcodeCurHus;
	}
	public void setAddrcodeCurHus(String addrcodeCurHus) {
		this.addrcodeCurHus = addrcodeCurHus;
	}
	public String getAddrCurHus() {
		return addrCurHus;
	}
	public void setAddrCurHus(String addrCurHus) {
		this.addrCurHus = addrCurHus;
	}
	public String getAddrCurDtHus() {
		return addrCurDtHus;
	}
	public void setAddrCurDtHus(String addrCurDtHus) {
		this.addrCurDtHus = addrCurDtHus;
	}
	public String getIncome(){
        return this.income;
    }
    public void setIncome(String income){
        this.income = income;
    }

    public Integer getNumBefore(){
        return this.numBefore;
    }
    public void setNumBefore(Integer numBefore){
        this.numBefore = numBefore;
    }

    public Integer getNumEarly(){
        return this.numEarly;
    }
    public void setNumEarly(Integer numEarly){
        this.numEarly = numEarly;
    }

    public Date getDateLast(){
        return this.dateLast;
    }
    public void setDateLast(Date dateLast){
        this.dateLast = dateLast;
    }

    public Integer getNumPreg(){
        return this.numPreg;
    }
    public void setNumPreg(Integer numPreg){
        this.numPreg = numPreg;
    }

    public Integer getNumProduct(){
        return this.numProduct;
    }
    public void setNumProduct(Integer numProduct){
        this.numProduct = numProduct;
    }

    public String getWeekno(){
        return this.weekno;
    }
    public void setWeekno(String weekno){
        this.weekno = weekno;
    }

    public Date getDatePrebirth(){
        return this.datePrebirth;
    }
    public void setDatePrebirth(Date datePrebirth){
        this.datePrebirth = datePrebirth;
    }

    public Integer getPeriodMenses(){
        return this.periodMenses;
    }
    public void setPeriodMenses(Integer periodMenses){
        this.periodMenses = periodMenses;
    }

    public String getNameCreate(){
        return this.nameCreate;
    }
    public void setNameCreate(String nameCreate){
        this.nameCreate = nameCreate;
    }

    public String getNote(){
        return this.note;
    }
    public void setNote(String note){
        this.note = note;
    }
    public String getFlagReasonable() {
		return flagReasonable;
	}
	public void setFlagReasonable(String flagReasonable) {
		this.flagReasonable = flagReasonable;
	}
	public String getFlagSmoke() {
		return flagSmoke;
	}
	public void setFlagSmoke(String flagSmoke) {
		this.flagSmoke = flagSmoke;
	}
	public String getFlagPlan() {
		return flagPlan;
	}
	public void setFlagPlan(String flagPlan) {
		this.flagPlan = flagPlan;
	}
	public String getFlagFolic() {
		return flagFolic;
	}
	public void setFlagFolic(String flagFolic) {
		this.flagFolic = flagFolic;
	}
	public String getFlagNormal() {
		return flagNormal;
	}
	public void setFlagNormal(String flagNormal) {
		this.flagNormal = flagNormal;
	}
	public String getPkEmpDoc() {
		return pkEmpDoc;
	}
	public void setPkEmpDoc(String pkEmpDoc) {
		this.pkEmpDoc = pkEmpDoc;
	}
	public String getNameEmpDoc() {
		return nameEmpDoc;
	}
	public void setNameEmpDoc(String nameEmpDoc) {
		this.nameEmpDoc = nameEmpDoc;
	}
	public Date getModifyTime(){
        return this.modifyTime;
    }
    public void setModifyTime(Date modifyTime){
        this.modifyTime = modifyTime;
    }

}