package com.zebone.nhis.pro.zsba.compay.pub.vo;

import java.util.Date;

import com.zebone.nhis.common.module.BaseModule;
import com.zebone.nhis.common.module.bl.BlIpDt;
import com.zebone.platform.modules.dao.build.au.Field;
import com.zebone.platform.modules.dao.build.au.Field.FieldType;
import com.zebone.platform.modules.dao.build.au.Field.KeyId;
import com.zebone.platform.modules.dao.build.au.PK;
import com.zebone.platform.modules.dao.build.au.Table;

/**
 * Table: BL_OP_DT - bl_op_dt
 * 
 * @since 2016-09-13 09:31:31
 */
@Table(value = "BL_OP_DT")
public class ZsbaBlOpDt extends BaseModule implements Cloneable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 0000000000000000001L;

	@PK
	@Field(value = "PK_CGOP", id = KeyId.UUID)
	private String pkCgop;

	@Field(value = "PK_PI")
	private String pkPi;

	@Field(value = "PK_PV")
	private String pkPv;

	@Field(value = "FLAG_PD")
	private String flagPd;

	/** CODE_BILL - 对应bd_invcateitem.bill_code */
	@Field(value = "CODE_BILL")
	private String codeBill;

	/** CODE_AUDIT - 对应bd_audit.audit_code */
	@Field(value = "CODE_AUDIT")
	private String codeAudit;

	@Field(value = "PK_ITEMCATE")
	private String pkItemcate;

	@Field(value = "PK_ITEM")
	private String pkItem;

	@Field(value = "NAME_CG")
	private String nameCg;

	@Field(value = "PK_UNIT")
	private String pkUnit;

	@Field(value = "SPEC")
	private String spec;

	@Field(value = "PK_DISC")
	private String pkDisc;

	@Field(value = "RATIO_DISC")
	private Double ratioDisc;

	@Field(value = "PRICE_ORG")
	private Double priceOrg;

	/** PRICE - 对物品来说：取物品当前包装单位下单价 */
	@Field(value = "PRICE")
	private Double price;

	/** QUAN - 对物品来说：取物品当前包装单位下单价 */
	@Field(value = "QUAN")
	private Double quan;

	@Field(value = "AMOUNT")
	private Double amount;

	@Field(value = "RATIO_SELF")
	private Double ratioSelf;

	@Field(value = "AMOUNT_PI")
	private Double amountPi;
	/** AMOUNT_HPPI - 医保金额_患者支付 */
	@Field(value="AMOUNT_HPPI")
	private Double  amountHppi;

	@Field(value = "PK_ORG_APP")
	private String pkOrgApp;

	@Field(value = "PK_DEPT_APP")
	private String pkDeptApp;

	@Field(value = "PK_EMP_APP")
	private String pkEmpApp;

	@Field(value = "NAME_EMP_APP")
	private String nameEmpApp;

	@Field(value = "PK_ORG_EX")
	private String pkOrgEx;

	@Field(value = "PK_DEPT_EX")
	private String pkDeptEx;

	@Field(value = "PK_CNORD")
	private String pkCnord;

	@Field(value = "DATE_HAP")
	private Date dateHap;

	@Field(value = "PK_PD")
	private String pkPd;

	@Field(value = "BATCH_NO")
	private String batchNo;

	@Field(value = "PK_UNIT_PD")
	private String pkUnitPd;

	@Field(value = "PACK_SIZE")
	private Integer packSize;

	@Field(value = "PRICE_COST")
	private Double priceCost;

	@Field(value = "FLAG_PV")
	private String flagPv;

	/** FLAG_ACC - 标识是否已经由账户支付 */
	@Field(value = "FLAG_ACC")
	private String flagAcc;

	/** PK_ACC - 支付账户主键 */
	@Field(value = "PK_ACC")
	private String pkAcc;

	@Field(value = "FLAG_SETTLE")
	private String flagSettle;

	@Field(value = "PK_SETTLE")
	private String pkSettle;

	@Field(value = "PK_PRES")
	private String pkPres;

	/** FLAG_INSU - 表示此记费项目已经上传至医保 */
	@Field(value = "FLAG_INSU")
	private String flagInsu;

	/** PK_CGOP_BACK - 退费时，记录对应的收费主键。可为空表示无对应记费的退费 */
	@Field(value = "PK_CGOP_BACK")
	private String pkCgopBack;

	@Field(value = "CODE_CG")
	private String codeCg;

	@Field(value = "DATE_CG")
	private Date dateCg;

	@Field(value = "PK_DEPT_CG")
	private String pkDeptCg;

	@Field(value = "PK_EMP_CG")
	private String pkEmpCg;

	@Field(value = "NAME_EMP_CG")
	private String nameEmpCg;
	
	@Field(value = "PK_EMP_EX")
	private String pkEmpEx;

	@Field(value = "NAME_EMP_EX")
	private String nameEmpEx;

	@Field(userfield = "pkEmp", userfieldscop = FieldType.ALL)
	private String modifier;
	
	@Field(value = "EU_ADDITEM")
	private String euAdditem;//附加项目标志--杨雪添加 2018.8.6
	
	@Field(value = "SORTNO")
	private int sortno ;//序号 杨雪添加 2019.5.19
	/** 加收比例 */
	@Field(value="RATIO_ADD")
	private Double ratioAdd; 
	
	/** 加收金额 */
	@Field(value="AMOUNT_ADD")
	private Double amountAdd; 

	/** 收费项目分类名称 */
	private String temcateName;

	@Field(value = "DATE_EXPIRE")
	private Date dateExpire;
	
	@Field(value = "PK_SCHAPPT")
	private String pkSchAppt ;//预约主键 杨雪添加 2019.6.3
	
	@Field(value = "FLAG_RECHARGE")
	private String flagRecharge;//部分退费重收标志 杨雪添加 2019.6.23
	
	@Field(value = "PK_INVOICE")
	private String pkInvoice;//关联票据主键(灵璧用：结算时写入)
	
	@Field(value="PK_PDSTDT")
	private String pkPdstdt;
	
	@Field(value="BARCODE")
	private String barcode;

	@Field(value="RATIO_POCK")
	private Double ratioPock;//医嘱打折比例存放增加字段
	
	@Field(value="AMOUNT_POCK")
	private Double amountPock;//自付比例打折金额；
	
	/**开立医生考勤科室*/
	@Field(value="PK_DEPT_JOB")
	private String pkDeptJob;
	
	/**开立诊区*/
    @Field(value = "PK_DEPT_AREAAPP")
    private String pkDeptAreaapp;

	@Field(value = "PK_PAYER")
	private String pkPayer;
	
	@Field(value = "PK_SETTLE_BEFORE")
	private String pkSettleBefore;//复制处方改身份重结(记录原结算主键)

	// 患者主医保主键
	private String pkInsu;

	// 可退
	private Double canBack;
	// 原始记费数量
    private Double quanCg;

	// 实退
	private Double quanBack;

	private String codeOrdtype;
	
	//
	private String codeOrdType;
	
	private String pkCgopOld;//灵璧部分退费使用
	
	private String isPrintQrCode; //是否打印二维码
	
	private String itemCode;//药品编码或者是项目编码
	
	//发票号 hl7消息使用
	private String codeInv;
	
	//处方号 hl7消息使用
	private String presNo;

	//发药窗口
	private String winnoConf;

	//配药窗口
	private String winnoPrep;

	public Double getRatioPock() {
		return ratioPock;
	}

	public void setRatioPock(Double ratioPock) {
		this.ratioPock = ratioPock;
	}

	public String getPkCgopOld() {
		return pkCgopOld;
	}

	public void setPkCgopOld(String pkCgopOld) {
		this.pkCgopOld = pkCgopOld;
	}

	public String getPkInvoice() {
		return pkInvoice;
	}

	public void setPkInvoice(String pkInvoice) {
		this.pkInvoice = pkInvoice;
	}

	public String getFlagRecharge() {
		return flagRecharge;
	}

	public void setFlagRecharge(String flagRecharge) {
		this.flagRecharge = flagRecharge;
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

	public String getCodeOrdType() {
		return codeOrdType;
	}

	public void setCodeOrdType(String codeOrdType) {
		this.codeOrdType = codeOrdType;
	}
	
	public String getPkSchAppt() {
		return pkSchAppt;
	}

	public void setPkSchAppt(String pkSchAppt) {
		this.pkSchAppt = pkSchAppt;
	}

	public int getSortno() {
		return sortno;
	}

	public void setSortno(int sortno) {
		this.sortno = sortno;
	}

	public String getPresNo() {
		return presNo;
	}

	public String getItemCode() {
		return itemCode;
	}

	public void setItemCode(String itemCode) {
		this.itemCode = itemCode;
	}

	public void setPresNo(String presNo) {
		this.presNo = presNo;
	}

	public String getCodeInv() {
		return codeInv;
	}

	public void setCodeInv(String codeInv) {
		this.codeInv = codeInv;
	}
	
	public String getPkCgop() {

		return this.pkCgop;
	}

	public void setPkCgop(String pkCgop) {

		this.pkCgop = pkCgop;
	}

	public String getPkPi() {

		return this.pkPi;
	}

	public void setPkPi(String pkPi) {

		this.pkPi = pkPi;
	}

	public String getPkPv() {

		return this.pkPv;
	}

	public void setPkPv(String pkPv) {

		this.pkPv = pkPv;
	}

	public String getFlagPd() {

		return this.flagPd;
	}

	public void setFlagPd(String flagPd) {

		this.flagPd = flagPd;
	}

	public String getCodeBill() {

		return this.codeBill;
	}

	public void setCodeBill(String codeBill) {

		this.codeBill = codeBill;
	}

	public String getCodeAudit() {

		return this.codeAudit;
	}

	public void setCodeAudit(String codeAudit) {

		this.codeAudit = codeAudit;
	}

	public String getPkItemcate() {

		return this.pkItemcate;
	}

	public void setPkItemcate(String pkItemcate) {

		this.pkItemcate = pkItemcate;
	}

	public String getPkItem() {

		return this.pkItem;
	}

	public void setPkItem(String pkItem) {

		this.pkItem = pkItem;
	}

	public String getNameCg() {

		return this.nameCg;
	}

	public void setNameCg(String nameCg) {

		this.nameCg = nameCg;
	}

	public String getPkUnit() {

		return this.pkUnit;
	}

	public void setPkUnit(String pkUnit) {

		this.pkUnit = pkUnit;
	}

	public String getSpec() {

		return this.spec;
	}

	public void setSpec(String spec) {

		this.spec = spec;
	}

	public String getPkDisc() {

		return this.pkDisc;
	}

	public void setPkDisc(String pkDisc) {

		this.pkDisc = pkDisc;
	}

	public Double getRatioDisc() {

		return this.ratioDisc;
	}

	public void setRatioDisc(Double ratioDisc) {

		this.ratioDisc = ratioDisc;
	}

	public Double getPriceOrg() {

		return this.priceOrg;
	}

	public void setPriceOrg(Double priceOrg) {

		this.priceOrg = priceOrg;
	}

	public Double getPrice() {

		return this.price;
	}

	public String getIsPrintQrCode() {
		return isPrintQrCode;
	}

	public void setIsPrintQrCode(String isPrintQrCode) {
		this.isPrintQrCode = isPrintQrCode;
	}

	public void setPrice(Double price) {

		this.price = price;
	}

	public Double getQuan() {

		return this.quan;
	}

	public void setQuan(Double quan) {

		this.quan = quan;
	}

	public Double getAmount() {

		return this.amount;
	}

	public void setAmount(Double amount) {

		this.amount = amount;
	}

	public Double getRatioSelf() {

		return this.ratioSelf;
	}

	public void setRatioSelf(Double ratioSelf) {

		this.ratioSelf = ratioSelf;
	}

	public Double getAmountPi() {

		return this.amountPi;
	}

	public void setAmountPi(Double amountPi) {

		this.amountPi = amountPi;
	}

	public String getPkOrgApp() {

		return this.pkOrgApp;
	}

	public void setPkOrgApp(String pkOrgApp) {

		this.pkOrgApp = pkOrgApp;
	}

	public String getPkDeptApp() {

		return this.pkDeptApp;
	}

	public void setPkDeptApp(String pkDeptApp) {

		this.pkDeptApp = pkDeptApp;
	}

	public String getPkEmpApp() {

		return this.pkEmpApp;
	}

	public void setPkEmpApp(String pkEmpApp) {

		this.pkEmpApp = pkEmpApp;
	}

	public String getNameEmpApp() {

		return this.nameEmpApp;
	}

	public void setNameEmpApp(String nameEmpApp) {

		this.nameEmpApp = nameEmpApp;
	}

	public String getPkOrgEx() {

		return this.pkOrgEx;
	}

	public void setPkOrgEx(String pkOrgEx) {

		this.pkOrgEx = pkOrgEx;
	}

	public String getPkDeptEx() {

		return this.pkDeptEx;
	}

	public void setPkDeptEx(String pkDeptEx) {

		this.pkDeptEx = pkDeptEx;
	}

	public String getPkCnord() {

		return this.pkCnord;
	}

	public void setPkCnord(String pkCnord) {

		this.pkCnord = pkCnord;
	}

	public Date getDateHap() {

		return this.dateHap;
	}

	public void setDateHap(Date dateHap) {

		this.dateHap = dateHap;
	}

	public String getPkPd() {

		return this.pkPd;
	}

	public void setPkPd(String pkPd) {

		this.pkPd = pkPd;
	}

	public String getBatchNo() {

		return this.batchNo;
	}

	public void setBatchNo(String batchNo) {

		this.batchNo = batchNo;
	}

	public String getPkUnitPd() {

		return this.pkUnitPd;
	}

	public void setPkUnitPd(String pkUnitPd) {

		this.pkUnitPd = pkUnitPd;
	}

	public Integer getPackSize() {

		return this.packSize;
	}

	public void setPackSize(Integer packSize) {

		this.packSize = packSize;
	}

	public Double getPriceCost() {

		return this.priceCost;
	}

	public void setPriceCost(Double priceCost) {

		this.priceCost = priceCost;
	}

	public String getFlagPv() {

		return this.flagPv;
	}

	public void setFlagPv(String flagPv) {

		this.flagPv = flagPv;
	}

	public String getFlagAcc() {

		return this.flagAcc;
	}

	public void setFlagAcc(String flagAcc) {

		this.flagAcc = flagAcc;
	}

	public String getPkAcc() {

		return this.pkAcc;
	}

	public void setPkAcc(String pkAcc) {

		this.pkAcc = pkAcc;
	}

	public String getFlagSettle() {

		return this.flagSettle;
	}

	public void setFlagSettle(String flagSettle) {

		this.flagSettle = flagSettle;
	}

	public String getPkSettle() {

		return this.pkSettle;
	}

	public void setPkSettle(String pkSettle) {

		this.pkSettle = pkSettle;
	}

	public String getPkPres() {

		return this.pkPres;
	}

	public void setPkPres(String pkPres) {

		this.pkPres = pkPres;
	}

	public String getFlagInsu() {

		return this.flagInsu;
	}

	public void setFlagInsu(String flagInsu) {

		this.flagInsu = flagInsu;
	}

	public String getPkCgopBack() {

		return this.pkCgopBack;
	}

	public void setPkCgopBack(String pkCgopBack) {

		this.pkCgopBack = pkCgopBack;
	}

	public String getCodeCg() {

		return this.codeCg;
	}

	public void setCodeCg(String codeCg) {

		this.codeCg = codeCg;
	}

	public Double getQuanCg() {
		return quanCg;
	}

	public void setQuanCg(Double quanCg) {
		this.quanCg = quanCg;
	}

	public Date getDateCg() {

		return this.dateCg;
	}

	public void setDateCg(Date dateCg) {

		this.dateCg = dateCg;
	}

	public String getPkDeptCg() {

		return this.pkDeptCg;
	}

	public void setPkDeptCg(String pkDeptCg) {

		this.pkDeptCg = pkDeptCg;
	}

	public String getPkEmpCg() {

		return this.pkEmpCg;
	}

	public void setPkEmpCg(String pkEmpCg) {

		this.pkEmpCg = pkEmpCg;
	}

	public String getNameEmpCg() {

		return this.nameEmpCg;
	}

	public void setNameEmpCg(String nameEmpCg) {

		this.nameEmpCg = nameEmpCg;
	}

	@Override
	public String getModifier() {

		return this.modifier;
	}

	@Override
	public void setModifier(String modifier) {

		this.modifier = modifier;
	}

	public String getTemcateName() {

		return temcateName;
	}

	public void setTemcateName(String temcateName) {

		this.temcateName = temcateName;
	}

	public String getPkInsu() {

		return pkInsu;
	}

	public void setPkInsu(String pkInsu) {

		this.pkInsu = pkInsu;
	}

	public Date getDateExpire() {

		return dateExpire;
	}

	public void setDateExpire(Date dateExpire) {

		this.dateExpire = dateExpire;
	}

	public Double getQuanBack() {

		return quanBack;
	}

	public void setQuanBack(Double quanBack) {

		this.quanBack = quanBack;
	}

	public Double getCanBack() {

		return canBack;
	}

	public void setCanBack(Double canBack) {

		this.canBack = canBack;
	}

	public String getCodeOrdtype() {

		return codeOrdtype;
	}

	public void setCodeOrdtype(String codeOrdtype) {

		this.codeOrdtype = codeOrdtype;
	}
	
	public String getEuAdditem() {
		return euAdditem;
	}

	public void setEuAdditem(String euAdditem) {
		this.euAdditem = euAdditem;
	}

	public Double getAmountHppi() {
		return amountHppi;
	}

	public void setAmountHppi(Double amountHppi) {
		this.amountHppi = amountHppi;
	}

	public Object clone() throws CloneNotSupportedException{
         return super.clone();
    }
	
	public String getPkPdstdt() {
		return pkPdstdt;
	}

	public void setPkPdstdt(String pkPdstdt) {
		this.pkPdstdt = pkPdstdt;
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

	public String getBarcode() {
		return barcode;
	}

	public void setBarcode(String barcode) {
		this.barcode = barcode;
	}
	
	public String getPkDeptJob() {
		return pkDeptJob;
	}

	public void setPkDeptJob(String pkDeptJob) {
		this.pkDeptJob = pkDeptJob;
	}
	
	public Double getAmountPock() {
		return amountPock;
	}

	public void setAmountPock(Double amountPock) {
		this.amountPock = amountPock;
	}

	//透析计费使用
	public Object clone(String type) {  
		ZsbaBlOpDt o = null;  
        try {  
            o = (ZsbaBlOpDt) super.clone();  
        } catch (CloneNotSupportedException e) {  
            e.printStackTrace();  
        }  
        return o;  
    }

	public String getWinnoConf() {
		return winnoConf;
	}

	public void setWinnoConf(String winnoConf) {
		this.winnoConf = winnoConf;
	}

	public String getWinnoPrep() {
		return winnoPrep;
	}

	public void setWinnoPrep(String winnoPrep) {
		this.winnoPrep = winnoPrep;
	}

	public String getPkDeptAreaapp() {
		return pkDeptAreaapp;
	}

	public void setPkDeptAreaapp(String pkDeptAreaapp) {
		this.pkDeptAreaapp = pkDeptAreaapp;
	}

	public String getPkPayer() {
		return pkPayer;
	}

	public void setPkPayer(String pkPayer) {
		this.pkPayer = pkPayer;
	}

	public String getPkSettleBefore() {
		return pkSettleBefore;
	}

	public void setPkSettleBefore(String pkSettleBefore) {
		this.pkSettleBefore = pkSettleBefore;
	}
}
