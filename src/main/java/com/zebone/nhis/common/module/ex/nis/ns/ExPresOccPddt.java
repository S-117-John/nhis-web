package com.zebone.nhis.common.module.ex.nis.ns;

import java.util.Date;

import com.zebone.nhis.common.module.BaseModule;
import com.zebone.platform.modules.dao.build.au.Field;
import com.zebone.platform.modules.dao.build.au.PK;
import com.zebone.platform.modules.dao.build.au.Table;
import com.zebone.platform.modules.dao.build.au.Field.KeyId;


/**
 * Table: EX_PRES_OCC_PDDT - ex_pres_occ_pddt 
 *
 * @since 2016-12-02 04:17:59
 */
@Table(value="EX_PRES_OCC_PDDT")
public class ExPresOccPddt extends BaseModule  {

	@PK
	@Field(value="PK_OCCPDDT",id=KeyId.UUID)
    private String pkOccpddt;

	@Field(value="PK_PRESOCCDT")
    private String pkPresoccdt;

	@Field(value="DATE_DE")
    private Date dateDe;

    /** EU_DIRECT - 1发药 -1退药 */
	@Field(value="EU_DIRECT")
    private String euDirect;

	@Field(value="PK_PD")
    private String pkPd;

	@Field(value="BATCH_NO")
    private String batchNo;

	@Field(value="DATE_EXPIRE")
    private Date dateExpire;

	@Field(value="PK_DEPT_DE")
    private String pkDeptDe;

	@Field(value="PK_STORE")
    private String pkStore;

	@Field(value="PK_UNIT")
    private String pkUnit;

	@Field(value="PACK_SIZE")
    private Integer packSize;

	@Field(value="QUAN_PACK")
    private Double quanPack;

	@Field(value="QUAN_MIN")
    private Double quanMin;

	@Field(value="PRICE_COST")
    private Double priceCost;

	@Field(value="PRICE")
    private Double price;

	@Field(value="AMOUNT_COST")
    private Double amountCost;

	@Field(value="AMOUNT")
    private Double amount;

	@Field(value="PK_PDSTDT")
    private String pkPdstdt;

	@Field(value="NOTE")
    private String note;

    /** PK_OCCPDDT_BACK - 退药记录关联的发药主键 */
	@Field(value="PK_OCCPDDT_BACK")
    private String pkOccpddtBack;

	@Field(value="MODITY_TIME")
    private Date modityTime;

	@Field(value = "PRES_NO")
	private String presNo;

	public String getPkOccpddt(){
        return this.pkOccpddt;
    }
    public void setPkOccpddt(String pkOccpddt){
        this.pkOccpddt = pkOccpddt;
    }

    public String getPkPresoccdt(){
        return this.pkPresoccdt;
    }
    public void setPkPresoccdt(String pkPresoccdt){
        this.pkPresoccdt = pkPresoccdt;
    }

    public Date getDateDe(){
        return this.dateDe;
    }
    public void setDateDe(Date dateDe){
        this.dateDe = dateDe;
    }

    public String getEuDirect(){
        return this.euDirect;
    }
    public void setEuDirect(String euDirect){
        this.euDirect = euDirect;
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

    public Date getDateExpire(){
        return this.dateExpire;
    }
    public void setDateExpire(Date dateExpire){
        this.dateExpire = dateExpire;
    }

    public String getPkDeptDe(){
        return this.pkDeptDe;
    }
    public void setPkDeptDe(String pkDeptDe){
        this.pkDeptDe = pkDeptDe;
    }

    public String getPkStore(){
        return this.pkStore;
    }
    public void setPkStore(String pkStore){
        this.pkStore = pkStore;
    }

    public String getPkUnit(){
        return this.pkUnit;
    }
    public void setPkUnit(String pkUnit){
        this.pkUnit = pkUnit;
    }

    public Integer getPackSize(){
        return this.packSize;
    }
    public void setPackSize(Integer packSize){
        this.packSize = packSize;
    }

    public Double getQuanPack(){
        return this.quanPack;
    }
    public void setQuanPack(Double quanPack){
        this.quanPack = quanPack;
    }

    public Double getQuanMin(){
        return this.quanMin;
    }
    public void setQuanMin(Double quanMin){
        this.quanMin = quanMin;
    }

    public Double getPriceCost(){
        return this.priceCost;
    }
    public void setPriceCost(Double priceCost){
        this.priceCost = priceCost;
    }

    public Double getPrice(){
        return this.price;
    }
    public void setPrice(Double price){
        this.price = price;
    }

    public Double getAmountCost(){
        return this.amountCost;
    }
    public void setAmountCost(Double amountCost){
        this.amountCost = amountCost;
    }

    public Double getAmount(){
        return this.amount;
    }
    public void setAmount(Double amount){
        this.amount = amount;
    }

    public String getPkPdstdt(){
        return this.pkPdstdt;
    }
    public void setPkPdstdt(String pkPdstdt){
        this.pkPdstdt = pkPdstdt;
    }

    public String getNote(){
        return this.note;
    }
    public void setNote(String note){
        this.note = note;
    }

    public String getPkOccpddtBack(){
        return this.pkOccpddtBack;
    }
    public void setPkOccpddtBack(String pkOccpddtBack){
        this.pkOccpddtBack = pkOccpddtBack;
    }

    public Date getModityTime(){
        return this.modityTime;
    }
    public void setModityTime(Date modityTime){
        this.modityTime = modityTime;
    }

    public String getPresNo() {
        return presNo;
    }

    public void setPresNo(String presNo) {
        this.presNo = presNo;
    }
}