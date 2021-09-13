package com.zebone.nhis.common.module.bl;

import java.util.Date;

import com.zebone.nhis.common.module.BaseModule;
import com.zebone.platform.modules.dao.build.au.Field;
import com.zebone.platform.modules.dao.build.au.PK;
import com.zebone.platform.modules.dao.build.au.Table;
import com.zebone.platform.modules.dao.build.au.Field.KeyId;


/**
 * Table: TEMP_BL_OP_DT 
 *
 * @since 2018-02-05 09:22:36
 */
@Table(value="TEMP_BL_OP_DT")
public class TempBlOpDt extends BaseModule  {

	private static final long serialVersionUID = -168375795264115738L;

	@PK
	@Field(value="PK_TEMP_CGOP",id=KeyId.UUID)
    private String pkTempCgop;

	@Field(value="PK_PI")
    private String pkPi;

	@Field(value="PK_PV")
    private String pkPv;

	@Field(value="FLAG_PD")
    private String flagPd;

    /** CODE_BILL - 对应bd_invcateitem.bill_code */
	@Field(value="CODE_BILL")
    private String codeBill;

    /** CODE_AUDIT - 对应bd_audit.audit_code */
	@Field(value="CODE_AUDIT")
    private String codeAudit;

	@Field(value="PK_ITEMCATE")
    private String pkItemcate;

	@Field(value="PK_ITEM")
    private String pkItem;

	@Field(value="NAME_CG")
    private String nameCg;

	@Field(value="PK_UNIT")
    private String pkUnit;

	@Field(value="SPEC")
    private String spec;

	@Field(value="PK_DISC")
    private String pkDisc;

	@Field(value="RATIO_DISC")
    private Long ratioDisc;

	@Field(value="PRICE_ORG")
    private Long priceOrg;

    /** PRICE - 对物品来说：取物品当前包装单位下单价 */
	@Field(value="PRICE")
    private Long price;

    /** QUAN - 对物品来说：取物品当前包装单位下单价 */
	@Field(value="QUAN")
    private Long quan;

	@Field(value="AMOUNT")
    private Double amount;

	@Field(value="RATIO_SELF")
    private Long ratioSelf;

	@Field(value="AMOUNT_PI")
    private Double amountPi;

	@Field(value="PK_ORG_APP")
    private String pkOrgApp;

	@Field(value="PK_DEPT_APP")
    private String pkDeptApp;

	@Field(value="PK_EMP_APP")
    private String pkEmpApp;

	@Field(value="NAME_EMP_APP")
    private String nameEmpApp;

	@Field(value="PK_ORG_EX")
    private String pkOrgEx;

	@Field(value="PK_DEPT_EX")
    private String pkDeptEx;

	@Field(value="PK_CNORD")
    private String pkCnord;

	@Field(value="DATE_HAP")
    private Date dateHap;

	@Field(value="PK_PD")
    private String pkPd;

	@Field(value="BATCH_NO")
    private String batchNo;

	@Field(value="PK_UNIT_PD")
    private String pkUnitPd;

	@Field(value="PACK_SIZE")
    private Long packSize;

	@Field(value="PRICE_COST")
    private Long priceCost;

	@Field(value="FLAG_PV")
    private String flagPv;

    /** FLAG_ACC - 标识是否已经由账户支付 */
	@Field(value="FLAG_ACC")
    private String flagAcc;

    /** PK_ACC - 支付账户主键 */
	@Field(value="PK_ACC")
    private String pkAcc;

	@Field(value="FLAG_SETTLE")
    private String flagSettle;

	@Field(value="PK_SETTLE")
    private String pkSettle;

	@Field(value="PK_PRES")
    private String pkPres;

    /** FLAG_INSU - 表示此记费项目已经上传至医保 */
	@Field(value="FLAG_INSU")
    private String flagInsu;

    /** PK_CGOP_BACK - 退费时，记录对应的收费主键。可为空表示无对应记费的退费 */
	@Field(value="PK_CGOP_BACK")
    private String pkCgopBack;

	@Field(value="CODE_CG")
    private String codeCg;

	@Field(value="DATE_CG")
    private Date dateCg;

	@Field(value="PK_DEPT_CG")
    private String pkDeptCg;

	@Field(value="PK_EMP_CG")
    private String pkEmpCg;

	@Field(value="NAME_EMP_CG")
    private String nameEmpCg;

	@Field(value="MODIFIER")
    private String modifier;

	@Field(value="MODITY_TIME")
    private Date modityTime;

    /** DATE_EXPIRE - 失效日期 */
	@Field(value="DATE_EXPIRE")
    private Date dateExpire;


    public String getPkTempCgop(){
        return this.pkTempCgop;
    }
    public void setPkTempCgop(String pkTempCgop){
        this.pkTempCgop = pkTempCgop;
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

    public Long getRatioDisc(){
        return this.ratioDisc;
    }
    public void setRatioDisc(Long ratioDisc){
        this.ratioDisc = ratioDisc;
    }

    public Long getPriceOrg(){
        return this.priceOrg;
    }
    public void setPriceOrg(Long priceOrg){
        this.priceOrg = priceOrg;
    }

    public Long getPrice(){
        return this.price;
    }
    public void setPrice(Long price){
        this.price = price;
    }

    public Long getQuan(){
        return this.quan;
    }
    public void setQuan(Long quan){
        this.quan = quan;
    }

    public Double getAmount(){
        return this.amount;
    }
    public void setAmount(Double amount){
        this.amount = amount;
    }

    public Long getRatioSelf(){
        return this.ratioSelf;
    }
    public void setRatioSelf(Long ratioSelf){
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

    public Long getPackSize(){
        return this.packSize;
    }
    public void setPackSize(Long packSize){
        this.packSize = packSize;
    }

    public Long getPriceCost(){
        return this.priceCost;
    }
    public void setPriceCost(Long priceCost){
        this.priceCost = priceCost;
    }

    public String getFlagPv(){
        return this.flagPv;
    }
    public void setFlagPv(String flagPv){
        this.flagPv = flagPv;
    }

    public String getFlagAcc(){
        return this.flagAcc;
    }
    public void setFlagAcc(String flagAcc){
        this.flagAcc = flagAcc;
    }

    public String getPkAcc(){
        return this.pkAcc;
    }
    public void setPkAcc(String pkAcc){
        this.pkAcc = pkAcc;
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

    public String getPkCgopBack(){
        return this.pkCgopBack;
    }
    public void setPkCgopBack(String pkCgopBack){
        this.pkCgopBack = pkCgopBack;
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

    public Date getDateExpire(){
        return this.dateExpire;
    }
    public void setDateExpire(Date dateExpire){
        this.dateExpire = dateExpire;
    }
}