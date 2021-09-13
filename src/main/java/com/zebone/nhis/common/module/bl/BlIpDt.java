package com.zebone.nhis.common.module.bl;

import com.zebone.nhis.common.module.BaseModule;
import com.zebone.platform.modules.dao.build.au.Field;
import com.zebone.platform.modules.dao.build.au.Field.KeyId;
import com.zebone.platform.modules.dao.build.au.PK;
import com.zebone.platform.modules.dao.build.au.Table;

import java.util.Date;


/**
 * Table: BL_IP_DT  - 收费结算-住院收费明细 
 *
 * @since 2016-09-12 03:51:14
 */
@Table(value="BL_IP_DT")
public class BlIpDt extends BaseModule implements Cloneable {

	private static final long serialVersionUID = 1L;
	
	public Object clone() {  
	    BlIpDt o = null;  
        try {  
            o = (BlIpDt) super.clone();  
        } catch (CloneNotSupportedException e) {  
            e.printStackTrace();  
        }  
        return o;  
    }  

	/** PK_CGIP - 住院记费主键 */
	@PK
	@Field(value="PK_CGIP",id=KeyId.UUID)
    private String pkCgip;
	/** PK_ORDEXDT - 新增关联执行单主键 */
	@Field(value="PK_ORDEXDT")
    private String pkOrdexdt;
    /**DATE_EXPIRE - 新增药品失效日期*/
	@Field(value="DATE_EXPIRE")
	private Date dateExpire;

	/** PK_PI - 患者主键 */
	@Field(value="PK_PI")
    private String pkPi;

    /** PK_PV - 就诊主键 */
	@Field(value="PK_PV")
    private String pkPv;

    /** FLAG_PD - 物品标志 */
	@Field(value="FLAG_PD")
    private String flagPd;

    /** CODE_BILL - 账单码 对应bd_invcateitem.bill_code */
	@Field(value="CODE_BILL")
    private String codeBill;

    /** CODE_AUDIT - 核算码 对应bd_audit.audit_code */
	@Field(value="CODE_AUDIT")
    private String codeAudit;

    /** PK_ITEMCATE - 收费项目分类 */
	@Field(value="PK_ITEMCATE")
    private String pkItemcate;

    /** PK_ITEM - 收费项目 */
	@Field(value="PK_ITEM")
    private String pkItem;

    /** NAME_CG - 记费名称 */
	@Field(value="NAME_CG")
    private String nameCg;

    /** PK_UNIT - 为物品时记录物品当前包装单位 */
	@Field(value="PK_UNIT")
    private String pkUnit;

    /** SPEC - 规格 */
	@Field(value="SPEC")
    private String spec;

    /** PK_DISC - 优惠类型 */
	@Field(value="PK_DISC")
    private String pkDisc;

    /** RATIO_DISC - 折扣比例 */
	@Field(value="RATIO_DISC")
    private Double ratioDisc;

    /** PRICE_ORG - 原始单价 */
	@Field(value="PRICE_ORG")
    private Double priceOrg;

    /** PRICE - 当前单价 对物品来说：取物品当前包装单位下单价 */
	@Field(value="PRICE")
    private Double price;

    /** QUAN - 数量  对物品来说：取物品当前包装单位下单价 */
	@Field(value="QUAN")
    private Double quan;

    /** AMOUNT - 金额 */
	@Field(value="AMOUNT")
    private Double amount;

    /** RATIO_SELF - 自费比例 */
	@Field(value="RATIO_SELF")
    private Double ratioSelf;

    /** AMOUNT_PI - 金额_患者自费 */
	@Field(value="AMOUNT_PI")
    private Double amountPi;
	
	/** AMOUNT_HPPI - 医保金额_患者支付 */
	@Field(value="AMOUNT_HPPI")
	private Double  amountHppi;

    /** PK_ORG_APP - 开立机构 */
	@Field(value="PK_ORG_APP")
    private String pkOrgApp;

    /** PK_DEPT_APP - 开立科室 */
	@Field(value="PK_DEPT_APP")
    private String pkDeptApp;

    /** PK_DEPT_NS_APP - 开立病区 */
	@Field(value="PK_DEPT_NS_APP")
    private String pkDeptNsApp;

    /** PK_WG - 开立医生对应的医疗组，可以为空 */
	@Field(value="PK_WG")
    private String pkWg;

    /** PK_EMP_APP - 开立医生 */
	@Field(value="PK_EMP_APP")
    private String pkEmpApp;

    /** NAME_EMP_APP - 开立医生姓名 */
	@Field(value="NAME_EMP_APP")
    private String nameEmpApp;

    /** PK_ORG_EX - 执行机构 */
	@Field(value="PK_ORG_EX")
    private String pkOrgEx;

    /** PK_DEPT_EX - 执行科室 */
	@Field(value="PK_DEPT_EX")
    private String pkDeptEx;

    /** PK_CNORD - 医嘱主键 */
	@Field(value="PK_CNORD")
    private String pkCnord;

    /** DATE_HAP - 费用发生日期 */
	@Field(value="DATE_HAP")
    private Date dateHap;

    /** PK_PD - 物品 */
	@Field(value="PK_PD")
    private String pkPd;

    /** BATCH_NO - 物品批次 */
	@Field(value="BATCH_NO")
    private String batchNo;

    /** PK_UNIT_PD - 物品单位 */
	@Field(value="PK_UNIT_PD")
    private String pkUnitPd;

    /** PACK_SIZE - 包装量 */
	@Field(value="PACK_SIZE")
    private Double packSize;

    /** PRICE_COST - 购入价_当前包装单位 */
	@Field(value="PRICE_COST")
    private Double priceCost;

    /** FLAG_SETTLE - 结算标志 */
	@Field(value="FLAG_SETTLE")
    private String flagSettle;

    /** PK_SETTLE - 结算主键 */
	@Field(value="PK_SETTLE")
    private String pkSettle;

    /** INFANT_NO - 婴儿序号 */
	@Field(value="INFANT_NO")
    private String infantNo;

    /** PK_PRES - 处方主键 */
	@Field(value="PK_PRES")
    private String pkPres;

    /** FLAG_INSU - 表示此记费项目已经上传至医保 */
	@Field(value="FLAG_INSU")
    private String flagInsu;

    /** PK_CGIP_BACK - 退费时，记录对应的收费主键。可为空表示无对应记费的退费 */
	@Field(value="PK_CGIP_BACK")
    private String pkCgipBack;

    /** CODE_CG - 记费编码 */
	@Field(value="CODE_CG")
    private String codeCg;

    /** DATE_CG - 记费日期 */
	@Field(value="DATE_CG")
    private Date dateCg;

    /** PK_DEPT_CG - 记费部门 */
	@Field(value="PK_DEPT_CG")
    private String pkDeptCg;

    /** PK_EMP_CG - 记费人员 */
	@Field(value="PK_EMP_CG")
    private String pkEmpCg;

    /** NAME_EMP_CG - 记费人员名称 */
	@Field(value="NAME_EMP_CG")
    private String nameEmpCg;
	
	/** 加收比例 */
	@Field(value="RATIO_ADD")
	private Double ratioAdd; 
	
	/** 加收金额 */
	@Field(value="AMOUNT_ADD")
	private Double amountAdd; 
	
	@Field(value="BARCODE")
	private String barcode;
	
	/**执行医生*/
	@Field(value="PK_EMP_EX")
	private String pkEmpEx;
	
	/** 执行医生姓名*/
	@Field(value="NAME_EMP_EX")
	private String nameEmpEx;
	
	/** 记费类型*/
	@Field(value="EU_BLTYPE")
	private String euBltype;
	
	/**关联医嘱*/
	@Field(value="PK_CNORD_RL")
	private String pkCnordRl;
	
	/**组套主键*/
	@Field(value="PK_ITEMSET")
	private String pkItemset;
	
	/**组套名称*/
	@Field(value="NAME_ITEMSET")
	private String nameItemset;
	
	/**
	 * 序号--中二
	 */
	@Field(value="SORTNO")
	private int sortno;
	
	/**
	 * 用量--中二
	 */
	@Field(value="DOSAGE")
	private Double dosage;
	
	/**
	 * 用量单位--中二
	 */
	@Field(value="UNIT_DOS")
	private String unitDos;
	
	/**
	 * 用法--中二
	 */
	@Field(value="NAME_SUPPLY")
	private String nameSupply;
	
	/**
	 * 频次--中二
	 */
	@Field(value="NAME_FREQ")
	private String nameFreq;
	
	/**
	 * 录入日期
	 */
	@Field(value="DATE_ENTRY")
	private Date dateEntry;
	/**
	 * 出库明细主键--发药用
	 */
	@Field(value="PK_PDSTDT")
	private String pkPdstdt;
    /**
     * 关联领药明细-科室领药用
     */
    @Field(value="PK_PDAPDT")
    private String pkPdapdt;
    /**
     * 取消原因
     */
	@Field(value = "NOTE_CG")
	private String noteCg;
    /**
     * 科研医嘱优惠付款方
     */
    @Field(value = "PK_PAYER")
    private String pkPayer;

    /**
     * 原医疗组
     */
    @Field(value="PK_WG_ORG")
    private String pkWgOrg;
    /**
     * 执行医疗组
     */
    @Field(value="PK_WG_EX")
    private String pkWgEx;
    
    /**
              * 医嘱开立医生考勤科室
     */
    @Field(value="PK_DEPT_JOB")
    private String pkDeptJob;

	public String getPkDeptJob() {
		return pkDeptJob;
	}
	public void setPkDeptJob(String pkDeptJob) {
		this.pkDeptJob = pkDeptJob;
	}
	public Date getDateEntry() {
		return dateEntry;
	}
	public void setDateEntry(Date dateEntry) {
		this.dateEntry = dateEntry;
	}
	public int getSortno() {
		return sortno;
	}
	public void setSortno(int sortno) {
		this.sortno = sortno;
	}
	public Double getDosage() {
		return dosage;
	}
	public void setDosage(Double dosage) {
		this.dosage = dosage;
	}
	public String getUnitDos() {
		return unitDos;
	}
	public void setUnitDos(String unitDos) {
		this.unitDos = unitDos;
	}
	public String getNameSupply() {
		return nameSupply;
	}
	public void setNameSupply(String nameSupply) {
		this.nameSupply = nameSupply;
	}
	public String getNameFreq() {
		return nameFreq;
	}
	public void setNameFreq(String nameFreq) {
		this.nameFreq = nameFreq;
	}
	public String getPkItemset() {
		return pkItemset;
	}
	public void setPkItemset(String pkItemset) {
		this.pkItemset = pkItemset;
	}
	public String getNameItemset() {
		return nameItemset;
	}
	public void setNameItemset(String nameItemset) {
		this.nameItemset = nameItemset;
	}
	public String getPkEmpEx() {
		return pkEmpEx;
	}
	public void setPkEmpEx(String pkEmpEx) {
		this.pkEmpEx = pkEmpEx;
	}
	public String getNameEmpEx() {
		return nameEmpEx;
	}
	public void setNameEmpEx(String nameEmpEx) {
		this.nameEmpEx = nameEmpEx;
	}
	public String getEuBltype() {
		return euBltype;
	}
	public void setEuBltype(String euBltype) {
		this.euBltype = euBltype;
	}
	public String getPkCnordRl() {
		return pkCnordRl;
	}
	public void setPkCnordRl(String pkCnordRl) {
		this.pkCnordRl = pkCnordRl;
	}
	public String getBarcode() {
		return barcode;
	}
	public void setBarcode(String barcode) {
		this.barcode = barcode;
	}
	public Double getRatioAdd() {
		return ratioAdd;
	}
	public void setRatioAdd(Double ratioAdd) {
		this.ratioAdd = ratioAdd;
	}
	public Double getAmountAdd() {
		return amountAdd;
	}
	public void setAmountAdd(Double amountAdd) {
		this.amountAdd = amountAdd;
	}
	public Double getAmountHppi() {
		return amountHppi;
	}
	public void setAmountHppi(Double amountHppi) {
		this.amountHppi = amountHppi;
	}
	public String getPkCgip(){
        return this.pkCgip;
    }
    public void setPkCgip(String pkCgip){
        this.pkCgip = pkCgip;
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

    public String getFlagPd(){
        return this.flagPd;
    }
    public void setFlagPd(String flagPd){
        this.flagPd = flagPd;
    }

    public String getCodeBill(){
        return this.codeBill;
    }
    public void setCodeBill(String codeBill){
        this.codeBill = codeBill;
    }

    public String getCodeAudit(){
        return this.codeAudit;
    }
    public void setCodeAudit(String codeAudit){
        this.codeAudit = codeAudit;
    }

    public String getPkItemcate(){
        return this.pkItemcate;
    }
    public void setPkItemcate(String pkItemcate){
        this.pkItemcate = pkItemcate;
    }

    public String getPkItem(){
        return this.pkItem;
    }
    public void setPkItem(String pkItem){
        this.pkItem = pkItem;
    }

    public String getNameCg(){
        return this.nameCg;
    }
    public void setNameCg(String nameCg){
        this.nameCg = nameCg;
    }

    public String getPkUnit(){
        return this.pkUnit;
    }
    public void setPkUnit(String pkUnit){
        this.pkUnit = pkUnit;
    }

    public String getSpec(){
        return this.spec;
    }
    public void setSpec(String spec){
        this.spec = spec;
    }

    public String getPkDisc(){
        return this.pkDisc;
    }
    public void setPkDisc(String pkDisc){
        this.pkDisc = pkDisc;
    }

    public Double getRatioDisc(){
        return this.ratioDisc;
    }
    public void setRatioDisc(Double ratioDisc){
        this.ratioDisc = ratioDisc;
    }

    public Double getPriceOrg(){
        return this.priceOrg;
    }
    public void setPriceOrg(Double priceOrg){
        this.priceOrg = priceOrg;
    }

    public Double getPrice(){
        return this.price;
    }
    public void setPrice(Double price){
        this.price = price;
    }

    public Double getQuan(){
        return this.quan;
    }
    public void setQuan(Double quan){
        this.quan = quan;
    }

    public Double getAmount(){
        return this.amount;
    }
    public void setAmount(Double amount){
        this.amount = amount;
    }

    public Double getRatioSelf(){
        return this.ratioSelf;
    }
    public void setRatioSelf(Double ratioSelf){
        this.ratioSelf = ratioSelf;
    }

    public Double getAmountPi(){
        return this.amountPi;
    }
    public void setAmountPi(Double amountPi){
        this.amountPi = amountPi;
    }

    public String getPkOrgApp(){
        return this.pkOrgApp;
    }
    public void setPkOrgApp(String pkOrgApp){
        this.pkOrgApp = pkOrgApp;
    }

    public String getPkDeptApp(){
        return this.pkDeptApp;
    }
    public void setPkDeptApp(String pkDeptApp){
        this.pkDeptApp = pkDeptApp;
    }

    public String getPkDeptNsApp(){
        return this.pkDeptNsApp;
    }
    public void setPkDeptNsApp(String pkDeptNsApp){
        this.pkDeptNsApp = pkDeptNsApp;
    }

    public String getPkWg(){
        return this.pkWg;
    }
    public void setPkWg(String pkWg){
        this.pkWg = pkWg;
    }

    public String getPkEmpApp(){
        return this.pkEmpApp;
    }
    public void setPkEmpApp(String pkEmpApp){
        this.pkEmpApp = pkEmpApp;
    }

    public String getNameEmpApp(){
        return this.nameEmpApp;
    }
    public void setNameEmpApp(String nameEmpApp){
        this.nameEmpApp = nameEmpApp;
    }

    public String getPkOrgEx(){
        return this.pkOrgEx;
    }
    public void setPkOrgEx(String pkOrgEx){
        this.pkOrgEx = pkOrgEx;
    }

    public String getPkDeptEx(){
        return this.pkDeptEx;
    }
    public void setPkDeptEx(String pkDeptEx){
        this.pkDeptEx = pkDeptEx;
    }

    public String getPkCnord(){
        return this.pkCnord;
    }
    public void setPkCnord(String pkCnord){
        this.pkCnord = pkCnord;
    }

    public Date getDateHap(){
        return this.dateHap;
    }
    public void setDateHap(Date dateHap){
        this.dateHap = dateHap;
    }

    public String getPkPd(){
        return this.pkPd;
    }
    public void setPkPd(String pkPd){
        this.pkPd = pkPd;
    }

    public String getBatchNo(){
        return this.batchNo;
    }
    public void setBatchNo(String batchNo){
        this.batchNo = batchNo;
    }

    public String getPkUnitPd(){
        return this.pkUnitPd;
    }
    public void setPkUnitPd(String pkUnitPd){
        this.pkUnitPd = pkUnitPd;
    }

    public Double getPackSize(){
        return this.packSize;
    }
    public void setPackSize(Double packSize){
        this.packSize = packSize;
    }

    public Double getPriceCost(){
        return this.priceCost;
    }
    public void setPriceCost(Double priceCost){
        this.priceCost = priceCost;
    }

    public String getFlagSettle(){
        return this.flagSettle;
    }
    public void setFlagSettle(String flagSettle){
        this.flagSettle = flagSettle;
    }

    public String getPkSettle(){
        return this.pkSettle;
    }
    public void setPkSettle(String pkSettle){
        this.pkSettle = pkSettle;
    }

    public String getInfantNo(){
        return this.infantNo;
    }
    public void setInfantNo(String infantNo){
        this.infantNo = infantNo;
    }

    public String getPkPres(){
        return this.pkPres;
    }
    public void setPkPres(String pkPres){
        this.pkPres = pkPres;
    }

    public String getFlagInsu(){
        return this.flagInsu;
    }
    public void setFlagInsu(String flagInsu){
        this.flagInsu = flagInsu;
    }

    public String getPkCgipBack(){
        return this.pkCgipBack;
    }
    public void setPkCgipBack(String pkCgipBack){
        this.pkCgipBack = pkCgipBack;
    }

    public String getCodeCg(){
        return this.codeCg;
    }
    public void setCodeCg(String codeCg){
        this.codeCg = codeCg;
    }

    public Date getDateCg(){
        return this.dateCg;
    }
    public void setDateCg(Date dateCg){
        this.dateCg = dateCg;
    }

    public String getPkDeptCg(){
        return this.pkDeptCg;
    }
    public void setPkDeptCg(String pkDeptCg){
        this.pkDeptCg = pkDeptCg;
    }

    public String getPkEmpCg(){
        return this.pkEmpCg;
    }
    public void setPkEmpCg(String pkEmpCg){
        this.pkEmpCg = pkEmpCg;
    }

    public String getNameEmpCg(){
        return this.nameEmpCg;
    }
    public void setNameEmpCg(String nameEmpCg){
        this.nameEmpCg = nameEmpCg;
    }
	public String getPkPdstdt() {
		return pkPdstdt;
	}
	public void setPkPdstdt(String pkPdstdt) {
		this.pkPdstdt = pkPdstdt;
	}

    public String getNoteCg() {
        return noteCg;
    }

    public void setNoteCg(String noteCg) {
        this.noteCg = noteCg;
    }

    public String getPkPdapdt() {
        return pkPdapdt;
    }

    public void setPkPdapdt(String pkPdapdt) {
        this.pkPdapdt = pkPdapdt;
    }

    public String getPkPayer() {
        return pkPayer;
    }

    public void setPkPayer(String pkPayer) {
        this.pkPayer = pkPayer;
    }

    public String getPkWgOrg() {
        return pkWgOrg;
    }

    public void setPkWgOrg(String pkWgOrg) {
        this.pkWgOrg = pkWgOrg;
    }

    public String getPkWgEx() {
        return pkWgEx;
    }

    public void setPkWgEx(String pkWgEx) {
        this.pkWgEx = pkWgEx;
    }
    public Date getDateExpire() {
        return dateExpire;
    }
    public void setDateExpire(Date dateExpire) {
        this.dateExpire = dateExpire;
    }
    public String getPkOrdexdt() {
        return pkOrdexdt;
    }
    public void setPkOrdexdt(String pkOrdexdt) {
        this.pkOrdexdt = pkOrdexdt;
    }
}