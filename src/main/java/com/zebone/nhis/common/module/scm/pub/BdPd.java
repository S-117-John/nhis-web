package com.zebone.nhis.common.module.scm.pub;


import java.util.Date;

import com.zebone.nhis.common.module.BaseModule;
import com.zebone.platform.modules.dao.build.au.Field;
import com.zebone.platform.modules.dao.build.au.PK;
import com.zebone.platform.modules.dao.build.au.Table;
import com.zebone.platform.modules.dao.build.au.Field.KeyId;


/**
 * Table: BD_PD - bd_pd 
 *
 * @since 2016-10-29 09:41:11
 */
@Table(value="BD_PD")
public class BdPd extends BaseModule  {

	@PK
	@Field(value="PK_PD",id=KeyId.UUID)
    private String pkPd;

	/** 通用名称-主键*/
	@Field(value="PK_PDGN")
    private String pkPdgn;
	
	@Field(value="CODE")
    private String code;

	@Field(value="NAME")
    private String name;

	@Field(value="SPEC")
    private String spec;

	@Field(value="SHORT_NAME")
    private String shortName;

	@Field(value="BARCODE")
    private String barcode;

	@Field(value="SPCODE")
    private String spcode;

	@Field(value="CONCENT")
    private Double concent;

	@Field(value="WEIGHT")
    private Double weight;

	@Field(value="PK_UNIT_WT")
    private String pkUnitWt;

	@Field(value="VOL")
    private Double vol;

	@Field(value="PK_UNIT_VOL")
    private String pkUnitVol;

    /** PK_UNIT_MIN - 最小单位-基本单位 */
	@Field(value="PK_UNIT_MIN")
    private String pkUnitMin;

	/** PACK_SIZE - 包装量 */
	@Field(value="PACK_SIZE")
    private Integer packSize;
	
	/** PACK_SIZE_MAX - 箱包装量 */
	@Field(value="PACK_SIZE_MAX")
    private Integer packSizeMax;
	
	 /** PK_UNIT_PACK - 包装单位 */
	@Field(value="PK_UNIT_PACK")
    private String pkUnitPack;

    /** EU_MUPUTYPE -包装单位取整模式： 0 按次取整， 1 按批取整，2 余量法，3 病区合用法 */
	@Field(value="EU_MUPUTYPE")
    private String euMuputype;

    /** EU_PDTYPE - 物品类型： 0药品、1卫生材料、2后勤物资、3医疗器械、4医疗设备 5通用设备 9 其他 */
	@Field(value="EU_PDTYPE")
    private String euPdtype;

	/** DT_PDTYPE - 物品类型：码表080001 */
	@Field(value="DT_PDTYPE")
    private String dtPdtype;
	
    /** EU_DRUGTYPE -药品类别： 0西药，1成药，2草药 */
	@Field(value="EU_DRUGTYPE")
    private String euDrugtype;

    /** NAME_CHEM -化学名称 - 拉丁文 */
	@Field(value="NAME_CHEM")
    private String nameChem;

	/** PK_FACTORY -生产厂家 */
	@Field(value="PK_FACTORY")
    private String pkFactory;

    /** APPR_NO - 当前有效的物品是批准文号或注册号. */
	@Field(value="APPR_NO")
    private String apprNo;

    /** EU_PDPRICE -零售价计算方式： 0 固定价格, 1 成本加成计算 */
	@Field(value="EU_PDPRICE")
    private String euPdprice;

	/** PRICE - 零售价格 */
	@Field(value="PRICE")
    private Double price;
	
    /** EU_PAP -加成率计算模式： 0 定额 1比例 */
	@Field(value="EU_PAP")
    private String euPap;

    /** AMT_PAP - 此字段在本物品计算模式下使用 */
	@Field(value="AMT_PAP")
    private Double amtPap;

    /** PAP_RATE - 此字段在本物品计算模式下使用 */
	@Field(value="PAP_RATE")
    private Double papRate;

    /** DT_ABRD -来源分类： 0 国产, 1 合资, 2 进口 */
	@Field(value="DT_ABRD")
    private String dtAbrd;

    /** EU_SOURCE -采购分类： 0 医院自采，1 政府采购 */
	@Field(value="EU_SOURCE")
    private String euSource;
	
    /** DT_MADE -制造分类： 0 购入, 1 自制, 2 原研 */
	@Field(value="DT_MADE")
    private String dtMade;

    /** DT_DOSAGE -药品剂型： 仅药品使用 */
	@Field(value="DT_DOSAGE")
    private String dtDosage;

    /** DT_PHARM -药理分类： 仅药品使用 */
	@Field(value="DT_PHARM")
    private String dtPharm;

    /** DT_POIS - 毒麻分类： 仅药品使用 */
	@Field(value="DT_POIS")
    private String dtPois;

    /** DT_ANTI - 抗菌药分类： 仅药品使用 */
	@Field(value="DT_ANTI")
    private String dtAnti;

    /** FLAG_PRECIOUS -贵重标志 - 0 普通物品, 1 贵重物品 */
	@Field(value="FLAG_PRECIOUS")
    private String flagPrecious;

    /** EU_USECATE - 用法分类： 1 口服 2 外用 3 注射 4大输液 9 其他 */
	@Field(value="EU_USECATE")
    private String euUsecate;

	 /** DT_STORETYPE - 存储要求 */
	@Field(value="DT_STORETYPE")
    private String dtStoretype;

	/** DT_BASE - 基本 药物分类 */
	@Field(value="DT_BASE")
    private String dtBase;

	/** EU_HERBTYPE - 草药类型  */
	@Field(value="EU_HERBTYPE")
	private String euHerbtype;

	/** FLAG_RM - 原料 */
	@Field(value="FLAG_RM")
    private String flagRm;

	/** FLAG_REAG - 试剂 */
	@Field(value="FLAG_REAG")
    private String flagReag;

	/** FLAG_VACC - 疫苗 */
	@Field(value="FLAG_VACC")
    private String flagVacc;

	/** FLAG_ST - 皮试 */
	@Field(value="FLAG_ST")
    private String flagSt;

	/** FLAG_GMP - 诊断用药 */
	@Field(value="FLAG_GMP")
    private String flagGmp;

    /** FLAG_TPN - 全胃肠外营养用药 */
	@Field(value="FLAG_TPN")
    private String flagTpn;

	/** FLAG_PED - 儿科用药 */
	@Field(value="FLAG_PED")
    private String flagPed;
	
	/** FLAG_CHRT - 放化疗用药 */
	@Field(value="FLAG_CHRT")
	private String flagChrt;
	 
	/** DT_INJTYPE - 注射药类型 */
	@Field(value="DT_INJTYPE")
	private String dtInjtype;

	/** VALID_CNT - 有效期 */
	@Field(value="VALID_CNT")
    private Integer validCnt;

    /** EU_VALID_UNIT - 有效期单位： 0 月，1 天，2 小时 */
	@Field(value="EU_VALID_UNIT")
    private String euValidUnit;

	/** NOTE - 药品说明 */
	@Field(value="NOTE")
    private String note;

	/** DOSAGE_DEF - 默认用量 */
	@Field(value="DOSAGE_DEF")
    private Double dosageDef;

	/** PK_UNIT_DEF - 默认用量单位 */
	@Field(value="PK_UNIT_DEF")
    private String pkUnitDef;

	/** CODE_SUPPLY - 默认用法 */
	@Field(value="CODE_SUPPLY")
    private String codeSupply;

	/** CODE_FREQ - 默认频次 */
	@Field(value="CODE_FREQ")
    private String codeFreq;

	/** DT_CHCATE - 病案分类码 */
	@Field(value="DT_CHCATE")
    private String dtChcate;

	/** PK_ITEMCATE - 对应服务类型 */
	@Field(value="PK_ITEMCATE")
    private String pkItemcate;

	/** PK_ORDTYPE - 对应医嘱类型 */
	@Field(value="PK_ORDTYPE")
    private String pkOrdtype;
	
	/** PK_PDIND - 关联适应症用药 */
	@Field(value="PK_PDIND")
	private String pkPdind;

	/** FLAG_STOP - 停用标志 */
	@Field(value="FLAG_STOP")
    private String flagStop;
	
	/** EU_STOCKMODE - 存量模式 */
	@Field(value="EU_STOCKMODE")
	private String euStockmode;
	
	/** REG_NO - 注册证号 */
	@Field(value="REG_NO")
	private String regNo;
	
	/** DATE_VALID_REG - 注册效期 */
	@Field(value="DATE_VALID_REG")
	private Date dateValidReg;
	
	/** PK_PDCATE - 物品分类 */
	@Field(value="PK_PDCATE")
	private String pkPdcate;
	
	/** CODE_COSTITEM - 对应成本项目 */
	@Field(value="CODE_COSTITEM")
	private String codeCostitem;
	
	/** PK_ITEM - 对应收费项目 */
	@Field(value="PK_ITEM")
	private String pkItem;
	
	/** FLAG_SINGLE - 单品管理标志 */
	@Field(value="FLAG_SINGLE")
	private String flagSingle;
	
	/** FLAG_IMP - 植入标志 */
	@Field(value="FLAG_IMP")
	private String flagImp;
	
	/** FLAG_USE - 在用管理标志 */
	@Field(value="FLAG_USE")
	private String flagUse;

	@Field(value="DT_USAGENOTE")
	private String dtUsagenote;

	//适用性别
	@Field(value="EU_SEX")
	private String euSex;
	
	//适用年龄下限
	@Field(value="AGE_MIN")
	private String ageMin;
	
	//适用年龄上限
	@Field(value="AGE_MAX")
	private String ageMax;
	
	//用药上限
	@Field(value="QUOTA_DOS")
	private String quotaDos;
	
	//医保上传编码
	@Field(value="CODE_HP")
	private String codeHp;
	
	//国家编码
	@Field(value="CODE_STD")
	private String codeStd;
	
	//药品全名
	@Field(value="NAME_GEN")
	private String nameGen;
	
	//标签计量类型 0 按医疗单位，1 按基本单位
	@Field(value="EU_LABELTYPE")
	private String euLabeltype;
	
	//核算分类主键
	@Field(value="PK_AUDIT")
	private String pkAudit;
	
	//外部系统编码
	@Field(value="CODE_EXT")
	private String codeExt;
	
	@Field(value="DT_DRUGEFFECT")
	private String dtDrugeffect;
	
	@Field(value="VAL_DDD")
	private double valDdd;
	
	//门诊取整模式
	@Field(value="EU_MUPUTYPE_OP")
	private String euMuputypeOp;

    @Field(value="REMARK")
    private String remark;

    /**
     * 统计编码
     */
    @Field(value = "DT_UNIFY")
    private String dtUnify;

    @Field(value = "DT_RAY")
    private String dtRay;
    /**
     * 中药服法
     */
    @Field(value = "DT_HERBUSAGE")
    private String dtHerbusage;

    /**
     * 价格
     */
    @Field(value = "CODE_EXT2")
    private String codeExt2;

	private String factory;

    /**
     * 旧系统编码
     */
    @Field(value = "OLD_CODE")
    private String oldCode;

    public String getCodeExt2() {
        return codeExt2;
    }

    public void setCodeExt2(String codeExt2) {
        this.codeExt2 = codeExt2;
    }

    public String getEuMuputypeOp() {
		return euMuputypeOp;
	}
	public void setEuMuputypeOp(String euMuputypeOp) {
		this.euMuputypeOp = euMuputypeOp;
	}
	public String getPkPdgn() {
		return pkPdgn;
	}
	public void setPkPdgn(String pkPdgn) {
		this.pkPdgn = pkPdgn;
	}
	public String getPkPdind() {
		return pkPdind;
	}
	public void setPkPdind(String pkPdind) {
		this.pkPdind = pkPdind;
	}
	public String getPkPd(){
        return this.pkPd;
    }
    public void setPkPd(String pkPd){
        this.pkPd = pkPd;
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

    public String getSpec(){
        return this.spec;
    }
    public void setSpec(String spec){
        this.spec = spec;
    }

    public String getShortName(){
        return this.shortName;
    }
    public void setShortName(String shortName){
        this.shortName = shortName;
    }

    public String getBarcode(){
        return this.barcode;
    }
    public void setBarcode(String barcode){
        this.barcode = barcode;
    }

    public String getSpcode(){
        return this.spcode;
    }
    public void setSpcode(String spcode){
        this.spcode = spcode;
    }

    public Double getConcent(){
        return this.concent;
    }
    public void setConcent(Double concent){
        this.concent = concent;
    }

    public Double getWeight(){
        return this.weight;
    }
    public void setWeight(Double weight){
        this.weight = weight;
    }

    public String getPkUnitWt(){
        return this.pkUnitWt;
    }
    public void setPkUnitWt(String pkUnitWt){
        this.pkUnitWt = pkUnitWt;
    }

    public Double getVol(){
        return this.vol;
    }
    public void setVol(Double vol){
        this.vol = vol;
    }

    public String getPkUnitVol(){
        return this.pkUnitVol;
    }
    public void setPkUnitVol(String pkUnitVol){
        this.pkUnitVol = pkUnitVol;
    }

    public String getPkUnitMin(){
        return this.pkUnitMin;
    }
    public void setPkUnitMin(String pkUnitMin){
        this.pkUnitMin = pkUnitMin;
    }

    public Integer getPackSizeMax() {
		return packSizeMax;
	}
	public void setPackSizeMax(Integer packSizeMax) {
		this.packSizeMax = packSizeMax;
	}
	public Integer getPackSize(){
        return this.packSize;
    }
    public void setPackSize(Integer packSize){
        this.packSize = packSize;
    }

    public String getPkUnitPack(){
        return this.pkUnitPack;
    }
    public void setPkUnitPack(String pkUnitPack){
        this.pkUnitPack = pkUnitPack;
    }

    public String getEuMuputype(){
        return this.euMuputype;
    }
    public void setEuMuputype(String euMuputype){
        this.euMuputype = euMuputype;
    }

    public String getEuPdtype(){
        return this.euPdtype;
    }
    public void setEuPdtype(String euPdtype){
        this.euPdtype = euPdtype;
    }

    public String getDtPdtype() {
		return dtPdtype;
	}
	public void setDtPdtype(String dtPdtype) {
		this.dtPdtype = dtPdtype;
	}
	public String getEuDrugtype(){
        return this.euDrugtype;
    }
    public void setEuDrugtype(String euDrugtype){
        this.euDrugtype = euDrugtype;
    }

    public String getNameChem(){
        return this.nameChem;
    }
    public void setNameChem(String nameChem){
        this.nameChem = nameChem;
    }

    public String getPkFactory(){
        return this.pkFactory;
    }
    public void setPkFactory(String pkFactory){
        this.pkFactory = pkFactory;
    }

    public String getApprNo(){
        return this.apprNo;
    }
    public void setApprNo(String apprNo){
        this.apprNo = apprNo;
    }

    public String getEuPdprice(){
        return this.euPdprice;
    }
    public void setEuPdprice(String euPdprice){
        this.euPdprice = euPdprice;
    }

    public String getEuPap(){
        return this.euPap;
    }
    public void setEuPap(String euPap){
        this.euPap = euPap;
    }

    public Double getAmtPap(){
        return this.amtPap;
    }
    public void setAmtPap(Double amtPap){
        this.amtPap = amtPap;
    }

    public Double getPapRate(){
        return this.papRate;
    }
    public void setPapRate(Double papRate){
        this.papRate = papRate;
    }

    public String getDtAbrd(){
        return this.dtAbrd;
    }
    public void setDtAbrd(String dtAbrd){
        this.dtAbrd = dtAbrd;
    }

    public String getDtMade(){
        return this.dtMade;
    }
    public void setDtMade(String dtMade){
        this.dtMade = dtMade;
    }

    public String getDtDosage(){
        return this.dtDosage;
    }
    public void setDtDosage(String dtDosage){
        this.dtDosage = dtDosage;
    }

    public String getDtPharm(){
        return this.dtPharm;
    }
    public void setDtPharm(String dtPharm){
        this.dtPharm = dtPharm;
    }

    public String getDtPois(){
        return this.dtPois;
    }
    public void setDtPois(String dtPois){
        this.dtPois = dtPois;
    }

    public String getDtAnti(){
        return this.dtAnti;
    }
    public void setDtAnti(String dtAnti){
        this.dtAnti = dtAnti;
    }

    public String getFlagPrecious(){
        return this.flagPrecious;
    }
    public void setFlagPrecious(String flagPrecious){
        this.flagPrecious = flagPrecious;
    }

    public String getEuUsecate(){
        return this.euUsecate;
    }
    public void setEuUsecate(String euUsecate){
        this.euUsecate = euUsecate;
    }

    public String getDtStoretype(){
        return this.dtStoretype;
    }
    public void setDtStoretype(String dtStoretype){
        this.dtStoretype = dtStoretype;
    }

    public String getDtBase(){
        return this.dtBase;
    }
    public void setDtBase(String dtBase){
        this.dtBase = dtBase;
    }

    public String getFlagRm(){
        return this.flagRm;
    }
    public void setFlagRm(String flagRm){
        this.flagRm = flagRm;
    }

    public String getFlagReag(){
        return this.flagReag;
    }
    public void setFlagReag(String flagReag){
        this.flagReag = flagReag;
    }

    public String getFlagVacc(){
        return this.flagVacc;
    }
    public void setFlagVacc(String flagVacc){
        this.flagVacc = flagVacc;
    }

    public String getFlagSt(){
        return this.flagSt;
    }
    public void setFlagSt(String flagSt){
        this.flagSt = flagSt;
    }

    public String getFlagGmp(){
        return this.flagGmp;
    }
    public void setFlagGmp(String flagGmp){
        this.flagGmp = flagGmp;
    }

    public String getFlagTpn(){
        return this.flagTpn;
    }
    public void setFlagTpn(String flagTpn){
        this.flagTpn = flagTpn;
    }

    public String getFlagPed(){
        return this.flagPed;
    }
    public void setFlagPed(String flagPed){
        this.flagPed = flagPed;
    }

    public String getNote(){
        return this.note;
    }
    public void setNote(String note){
        this.note = note;
    }

    public Double getDosageDef(){
        return this.dosageDef;
    }
    public void setDosageDef(Double dosageDef){
        this.dosageDef = dosageDef;
    }

    public String getPkUnitDef(){
        return this.pkUnitDef;
    }
    public void setPkUnitDef(String pkUnitDef){
        this.pkUnitDef = pkUnitDef;
    }

    public String getCodeSupply(){
        return this.codeSupply;
    }
    public void setCodeSupply(String codeSupply){
        this.codeSupply = codeSupply;
    }

    public String getCodeFreq(){
        return this.codeFreq;
    }
    public void setCodeFreq(String codeFreq){
        this.codeFreq = codeFreq;
    }

    public String getDtChcate(){
        return this.dtChcate;
    }
    public void setDtChcate(String dtChcate){
        this.dtChcate = dtChcate;
    }

    public String getPkItemcate(){
        return this.pkItemcate;
    }
    public void setPkItemcate(String pkItemcate){
        this.pkItemcate = pkItemcate;
    }

    public String getPkOrdtype(){
        return this.pkOrdtype;
    }
    public void setPkOrdtype(String pkOrdtype){
        this.pkOrdtype = pkOrdtype;
    }

    public Double getPrice(){
        return this.price;
    }
    public void setPrice(Double price){
        this.price = price;
    }

    public Integer getValidCnt(){
        return this.validCnt;
    }
    public void setValidCnt(Integer validCnt){
        this.validCnt = validCnt;
    }

    public String getEuValidUnit(){
        return this.euValidUnit;
    }
    public void setEuValidUnit(String euValidUnit){
        this.euValidUnit = euValidUnit;
    }

    public String getFlagStop(){
        return this.flagStop;
    }
    public void setFlagStop(String flagStop){
        this.flagStop = flagStop;
    }

    public String getEuSource(){
        return this.euSource;
    }
    public void setEuSource(String euSource){
        this.euSource = euSource;
    }
	public String getDtUsagenote() {
		return dtUsagenote;
	}
	public void setDtUsagenote(String dtUsagenote) {
		this.dtUsagenote = dtUsagenote;
	}
	public String getPkPdcate() {
		return pkPdcate;
	}
	public void setPkPdcate(String pkPdcate) {
		this.pkPdcate = pkPdcate;
	}
	public String getRegNo() {
		return regNo;
	}
	public void setRegNo(String regNo) {
		this.regNo = regNo;
	}
	public Date getDateValidReg() {
		return dateValidReg;
	}
	public void setDateValidReg(Date dateValidReg) {
		this.dateValidReg = dateValidReg;
	}
	public String getEuStockmode() {
		return euStockmode;
	}
	public void setEuStockmode(String euStockmode) {
		this.euStockmode = euStockmode;
	}
	public String getCodeCostitem() {
		return codeCostitem;
	}
	public void setCodeCostitem(String codeCostitem) {
		this.codeCostitem = codeCostitem;
	}
	public String getPkItem() {
		return pkItem;
	}
	public void setPkItem(String pkItem) {
		this.pkItem = pkItem;
	}
	public String getFlagSingle() {
		return flagSingle;
	}
	public void setFlagSingle(String flagSingle) {
		this.flagSingle = flagSingle;
	}
	public String getFlagImp() {
		return flagImp;
	}
	public void setFlagImp(String flagImp) {
		this.flagImp = flagImp;
	}
	public String getFlagUse() {
		return flagUse;
	}
	public void setFlagUse(String flagUse) {
		this.flagUse = flagUse;
	}
	public String getFlagChrt() {
		return flagChrt;
	}
	public void setFlagChrt(String flagChrt) {
		this.flagChrt = flagChrt;
	}
	public String getDtInjtype() {
		return dtInjtype;
	}
	public void setDtInjtype(String dtInjtype) {
		this.dtInjtype = dtInjtype;
	}
	public String getEuHerbtype() {
		return euHerbtype;
	}
	public void setEuHerbtype(String euHerbtype) {
		this.euHerbtype = euHerbtype;
	}
	public String getEuSex() {
		return euSex;
	}
	public void setEuSex(String euSex) {
		this.euSex = euSex;
	}
	public String getAgeMin() {
		return ageMin;
	}
	public void setAgeMin(String ageMin) {
		this.ageMin = ageMin;
	}
	public String getAgeMax() {
		return ageMax;
	}
	public void setAgeMax(String ageMax) {
		this.ageMax = ageMax;
	}
	public String getQuotaDos() {
		return quotaDos;
	}
	public void setQuotaDos(String quotaDos) {
		this.quotaDos = quotaDos;
	}
	public String getCodeHp() {
		return codeHp;
	}
	public void setCodeHp(String codeHp) {
		this.codeHp = codeHp;
	}
	public String getCodeStd() {
		return codeStd;
	}
	public void setCodeStd(String codeStd) {
		this.codeStd = codeStd;
	}
	public String getNameGen() {
		return nameGen;
	}
	public void setNameGen(String nameGen) {
		this.nameGen = nameGen;
	}
	public String getEuLabeltype() {
		return euLabeltype;
	}
	public void setEuLabeltype(String euLabeltype) {
		this.euLabeltype = euLabeltype;
	}
	public String getPkAudit() {
		return pkAudit;
	}
	public void setPkAudit(String pkAudit) {
		this.pkAudit = pkAudit;
	}
	public String getCodeExt() {
		return codeExt;
	}
	public void setCodeExt(String codeExt) {
		this.codeExt = codeExt;
	}
	public String getDtDrugeffect() {
		return dtDrugeffect;
	}
	public void setDtDrugeffect(String dtDrugeffect) {
		this.dtDrugeffect = dtDrugeffect;
	}
	public double getValDdd() {
		return valDdd;
	}
	public void setValDdd(double valDdd) {
		this.valDdd = valDdd;
	}

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getDtUnify() {
        return dtUnify;
    }

    public void setDtUnify(String dtUnify) {
        this.dtUnify = dtUnify;
    }

    public String getDtRay() {
        return dtRay;
    }

    public void setDtRay(String dtRay) {
        this.dtRay = dtRay;
    }

    public String getFactory() {
        return factory;
    }

    public void setFactory(String factory) {
        this.factory = factory;
    }

    public String getDtHerbusage() {
        return dtHerbusage;
    }

    public void setDtHerbusage(String dtHerbusage) {
        this.dtHerbusage = dtHerbusage;
    }

    public String getOldCode() {
        return oldCode;
    }

    public void setOldCode(String oldCode){
        this.oldCode = oldCode;
    }
}