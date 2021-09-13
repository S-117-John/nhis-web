package com.zebone.nhis.common.module.pv;

import com.zebone.nhis.common.module.BaseModule;
import com.zebone.platform.modules.dao.build.au.Field;
import com.zebone.platform.modules.dao.build.au.Field.KeyId;
import com.zebone.platform.modules.dao.build.au.PK;
import com.zebone.platform.modules.dao.build.au.Table;

import java.util.Date;


/**
 * Table: PV_INFANT 
 *
 * @since 2017-09-15 07:25:52
 */
@Table(value="PV_INFANT")
public class PvInfant extends BaseModule  {

	@PK
	@Field(value="PK_INFANT",id=KeyId.UUID)
    private String pkInfant;

	@Field(value="PK_LABORREC")
    private String pkLaborrec;

	@Field(value="PK_LABORRECDT")
    private String pkLaborrecdt;

	@Field(value="PK_PV")
    private String pkPv;

	@Field(value="PK_PI")
    private String pkPi;

	@Field(value="PK_PV_INFANT")
    private String pkPvInfant;

	@Field(value="CODE")
    private String code;

	@Field(value="NAME")
    private String name;

	@Field(value="DT_SEX")
    private String dtSex;
	
	private String Sex;

	@Field(value="SORT_NO")
    private Integer sortNo;

	@Field(value="EU_BIRTH")
    private String euBirth;

	@Field(value="DATE_BIRTH")
    private Date dateBirth;

	@Field(value="WEIGHT")
    private Double weight;

	@Field(value="LEN")
    private Double len;

	@Field(value="UMB_LEV")
    private String umbLev;

	@Field(value="EYE_LEV")
    private String eyeLev;

	@Field(value="EAR_LEV")
    private String earLev;

	@Field(value="NOSE_LEV")
    private String noseLev;

	@Field(value="LIP_LEV")
    private String lipLev;

	@Field(value="TONGUE_LEV")
    private String tongueLev;

	@Field(value="MOUTH_LEV")
    private String mouthLev;

	@Field(value="NECK_LEV")
    private String neckLev;

	@Field(value="LIMB_LEV")
    private String limbLev;

	@Field(value="ANUS_LEV")
    private String anusLev;

	@Field(value="GEN_LEV")
    private String genLev;

	@Field(value="EU_FRA")
    private String euFra;

	@Field(value="SKIN_GENE")
    private String skinGene;

	@Field(value="OXY")
    private Integer oxy;

	@Field(value="OXY_NOTE")
    private String oxyNote;

	@Field(value="EU_MEC")
    private String euMec;

	@Field(value="EU_PEE")
    private String euPee;

	@Field(value="OTHERS")
    private String others;

	@Field(value="EU_ANABIOSIS")
    private String euAnabiosis;

	@Field(value="EU_ANATYPE")
    private String euAnatype;

	@Field(value="DRUG_ANABIOSIS")
    private String drugAnabiosis;

	@Field(value="EU_STATUS_ADT")
    private String euStatusAdt;

	@Field(value="REASON_ADT")
    private String reasonAdt;

	@Field(value="DATE_ADT")
    private Date dateAdt;

	@Field(value="PK_PI_INFANT")
    private String pkPiInfant;

	@Field(value="REMARK")
    private String remark;

	@Field(value="NAME_APP")
    private String nameApp;

	@Field(value="DT_RELATION")
    private String dtRelation;

	@Field(value="DT_IDEN_APP")
    private String dtIdenApp;

	@Field(value="IDEN_NO_APP")
    private String idenNoApp;

	@Field(value="DT_PRO")
    private String dtPro;

	@Field(value="DT_CITY")
    private String dtCity;

	@Field(value="DT_REGION")
    private String dtRegion;

	@Field(value="STREET")
    private String street;

	@Field(value="PK_EMP_DIR")
    private String pkEmpDir;

	@Field(value="NAME_EMP_DIR")
    private String nameEmpDir;
	
	/** 婴儿转归 */
	@Field(value="DT_INFANTOUTCOME")
    private String dtInfantoutcome;
	
	/** 分娩结果 */
	@Field(value="DT_LABRESULT2")
    private String dtLabresult2;
	
	@Field(value="MODIFY_TIME")
    private Date modifyTime;

	@Field(value="CNT_RESCUE")
    private Integer cntRescue;

	@Field(value="CNT_SUCCEED")
    private Integer cntSucceed;

	@Field(value = "DT_BREATH")
    private String dtBreath;
	
	@Field(value="DT_OUT_MODE")
	private String dtOutMode;
	
	@Field(value="WEEKS_PREG")
	private String weeksPreg;
	
	@Field(value="NUM_PREG")
	private Double numPreg;
	
	@Field(value="NUM_PRODUCT")
	private Double numProduct;
	
	@Field(value="WEEKS_INPREG")
	private String weeksInpreg;
	
	//AGPRA评分
    @Field(value="APGAR")
    public String agpar;
    
    public String getAgpar() {
        return agpar;
    }

    public void setAgpar(String agpar) {
        this.agpar = agpar;
    }

    public String getPkInfant(){
        return this.pkInfant;
    }
    public void setPkInfant(String pkInfant){
        this.pkInfant = pkInfant;
    }

    public String getPkLaborrec(){
        return this.pkLaborrec;
    }
    public void setPkLaborrec(String pkLaborrec){
        this.pkLaborrec = pkLaborrec;
    }

    public String getPkLaborrecdt(){
        return this.pkLaborrecdt;
    }
    public void setPkLaborrecdt(String pkLaborrecdt){
        this.pkLaborrecdt = pkLaborrecdt;
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

    public String getPkPvInfant(){
        return this.pkPvInfant;
    }
    public void setPkPvInfant(String pkPvInfant){
        this.pkPvInfant = pkPvInfant;
    }

    public String getCode(){
        return this.code;
    }
    public void setCode(String code){
        this.code = code;
    }

    public String getName(){
        return this.name;
    }
    public void setName(String name){
        this.name = name;
    }

    public String getDtSex(){
        return this.dtSex;
    }
    public void setDtSex(String dtSex){
        this.dtSex = dtSex;
    }

    public String getSex() {
		return Sex;
	}
	public void setSex(String sex) {
		Sex = sex;
	}
	public Integer getSortNo(){
        return this.sortNo;
    }
    public void setSortNo(Integer sortNo){
        this.sortNo = sortNo;
    }

    public String getEuBirth(){
        return this.euBirth;
    }
    public void setEuBirth(String euBirth){
        this.euBirth = euBirth;
    }

    public Date getDateBirth(){
        return this.dateBirth;
    }
    public void setDateBirth(Date dateBirth){
        this.dateBirth = dateBirth;
    }

    public Double getWeight(){
        return this.weight;
    }
    public void setWeight(Double weight){
        this.weight = weight;
    }

    public Double getLen(){
        return this.len;
    }
    public void setLen(Double len){
        this.len = len;
    }

    public String getUmbLev(){
        return this.umbLev;
    }
    public void setUmbLev(String umbLev){
        this.umbLev = umbLev;
    }

    public String getEyeLev(){
        return this.eyeLev;
    }
    public void setEyeLev(String eyeLev){
        this.eyeLev = eyeLev;
    }

    public String getEarLev(){
        return this.earLev;
    }
    public void setEarLev(String earLev){
        this.earLev = earLev;
    }

    public String getNoseLev(){
        return this.noseLev;
    }
    public void setNoseLev(String noseLev){
        this.noseLev = noseLev;
    }

    public String getLipLev(){
        return this.lipLev;
    }
    public void setLipLev(String lipLev){
        this.lipLev = lipLev;
    }

    public String getTongueLev(){
        return this.tongueLev;
    }
    public void setTongueLev(String tongueLev){
        this.tongueLev = tongueLev;
    }

    public String getMouthLev(){
        return this.mouthLev;
    }
    public void setMouthLev(String mouthLev){
        this.mouthLev = mouthLev;
    }

    public String getNeckLev(){
        return this.neckLev;
    }
    public void setNeckLev(String neckLev){
        this.neckLev = neckLev;
    }

    public String getLimbLev(){
        return this.limbLev;
    }
    public void setLimbLev(String limbLev){
        this.limbLev = limbLev;
    }

    public String getAnusLev(){
        return this.anusLev;
    }
    public void setAnusLev(String anusLev){
        this.anusLev = anusLev;
    }

    public String getGenLev(){
        return this.genLev;
    }
    public void setGenLev(String genLev){
        this.genLev = genLev;
    }

    public String getEuFra(){
        return this.euFra;
    }
    public void setEuFra(String euFra){
        this.euFra = euFra;
    }

    public String getSkinGene(){
        return this.skinGene;
    }
    public void setSkinGene(String skinGene){
        this.skinGene = skinGene;
    }

    public Integer getOxy(){
        return this.oxy;
    }
    public void setOxy(Integer oxy){
        this.oxy = oxy;
    }

    public String getOxyNote(){
        return this.oxyNote;
    }
    public void setOxyNote(String oxyNote){
        this.oxyNote = oxyNote;
    }

    public String getEuMec(){
        return this.euMec;
    }
    public void setEuMec(String euMec){
        this.euMec = euMec;
    }

    public String getEuPee(){
        return this.euPee;
    }
    public void setEuPee(String euPee){
        this.euPee = euPee;
    }

    public String getOthers(){
        return this.others;
    }
    public void setOthers(String others){
        this.others = others;
    }

    public String getEuAnabiosis(){
        return this.euAnabiosis;
    }
    public void setEuAnabiosis(String euAnabiosis){
        this.euAnabiosis = euAnabiosis;
    }

    public String getEuAnatype(){
        return this.euAnatype;
    }
    public void setEuAnatype(String euAnatype){
        this.euAnatype = euAnatype;
    }

    public String getDrugAnabiosis(){
        return this.drugAnabiosis;
    }
    public void setDrugAnabiosis(String drugAnabiosis){
        this.drugAnabiosis = drugAnabiosis;
    }

    public String getEuStatusAdt(){
        return this.euStatusAdt;
    }
    public void setEuStatusAdt(String euStatusAdt){
        this.euStatusAdt = euStatusAdt;
    }

    public String getReasonAdt(){
        return this.reasonAdt;
    }
    public void setReasonAdt(String reasonAdt){
        this.reasonAdt = reasonAdt;
    }

    public Date getDateAdt(){
        return this.dateAdt;
    }
    public void setDateAdt(Date dateAdt){
        this.dateAdt = dateAdt;
    }

    public String getPkPiInfant(){
        return this.pkPiInfant;
    }
    public void setPkPiInfant(String pkPiInfant){
        this.pkPiInfant = pkPiInfant;
    }

    public String getRemark(){
        return this.remark;
    }
    public void setRemark(String remark){
        this.remark = remark;
    }

    public String getNameApp(){
        return this.nameApp;
    }
    public void setNameApp(String nameApp){
        this.nameApp = nameApp;
    }

    public String getDtRelation(){
        return this.dtRelation;
    }
    public void setDtRelation(String dtRelation){
        this.dtRelation = dtRelation;
    }

    public String getDtIdenApp(){
        return this.dtIdenApp;
    }
    public void setDtIdenApp(String dtIdenApp){
        this.dtIdenApp = dtIdenApp;
    }

    public String getIdenNoApp(){
        return this.idenNoApp;
    }
    public void setIdenNoApp(String idenNoApp){
        this.idenNoApp = idenNoApp;
    }

    public String getDtPro(){
        return this.dtPro;
    }
    public void setDtPro(String dtPro){
        this.dtPro = dtPro;
    }

    public String getDtCity(){
        return this.dtCity;
    }
    public void setDtCity(String dtCity){
        this.dtCity = dtCity;
    }

    public String getDtRegion(){
        return this.dtRegion;
    }
    public void setDtRegion(String dtRegion){
        this.dtRegion = dtRegion;
    }

    public String getStreet(){
        return this.street;
    }
    public void setStreet(String street){
        this.street = street;
    }

    public String getPkEmpDir(){
        return this.pkEmpDir;
    }
    public void setPkEmpDir(String pkEmpDir){
        this.pkEmpDir = pkEmpDir;
    }

    public String getNameEmpDir(){
        return this.nameEmpDir;
    }
    public void setNameEmpDir(String nameEmpDir){
        this.nameEmpDir = nameEmpDir;
    }

    public Date getModifyTime(){
        return this.modifyTime;
    }
    public void setModifyTime(Date modifyTime){
        this.modifyTime = modifyTime;
    }
	public Integer getCntRescue() {
		return cntRescue;
	}
	public void setCntRescue(Integer cntRescue) {
		this.cntRescue = cntRescue;
	}
	public Integer getCntSucceed() {
		return cntSucceed;
	}
	public void setCntSucceed(Integer cntSucceed) {
		this.cntSucceed = cntSucceed;
	}
	public String getDtInfantoutcome() {
		return dtInfantoutcome;
	}
	public void setDtInfantoutcome(String dtInfantoutcome) {
		this.dtInfantoutcome = dtInfantoutcome;
	}
	public String getDtLabresult2() {
		return dtLabresult2;
	}
	public void setDtLabresult2(String dtLabresult2) {
		this.dtLabresult2 = dtLabresult2;
	}

    public String getDtBreath() {
        return dtBreath;
    }

    public void setDtBreath(String dtBreath) {
        this.dtBreath = dtBreath;
    }

	public String getDtOutMode() {
		return dtOutMode;
	}

	public void setDtOutMode(String dtOutMode) {
		this.dtOutMode = dtOutMode;
	}

	public String getWeeksPreg() {
		return weeksPreg;
	}

	public void setWeeksPreg(String weeksPreg) {
		this.weeksPreg = weeksPreg;
	}

	public Double getNumPreg() {
		return numPreg;
	}

	public void setNumPreg(Double numPreg) {
		this.numPreg = numPreg;
	}

	public Double getNumProduct() {
		return numProduct;
	}

	public void setNumProduct(Double numProduct) {
		this.numProduct = numProduct;
	}

	public String getWeeksInpreg() {
		return weeksInpreg;
	}

	public void setWeeksInpreg(String weeksInpreg) {
		this.weeksInpreg = weeksInpreg;
	}
}