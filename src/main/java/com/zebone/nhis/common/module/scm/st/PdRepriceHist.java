package com.zebone.nhis.common.module.scm.st;

import java.util.Date;

import com.zebone.nhis.common.module.BaseModule;
import com.zebone.platform.modules.dao.build.au.Field;
import com.zebone.platform.modules.dao.build.au.PK;
import com.zebone.platform.modules.dao.build.au.Table;
import com.zebone.platform.modules.dao.build.au.Field.KeyId;


/**
 * Table: PD_REPRICE_HIST - pd_reprice_hist 
 *
 * @since 2016-11-10 01:37:17
 */
@Table(value="PD_REPRICE_HIST")
public class PdRepriceHist extends BaseModule  {

	@PK
	@Field(value="PK_PDREPHIST",id=KeyId.UUID)
    private String pkPdrephist;

    /** DT_REPTYPE - 0政策条件， 1企业调价，2其他调价 */
	@Field(value="DT_REPTYPE")
    private String dtReptype;

	@Field(value="CODE_REP")
    private String codeRep;

	@Field(value="APPROVAL")
    private String approval;

	@Field(value="DATE_REPRICE")
    private Date dateReprice;

	@Field(value="PK_PDREPDT")
    private String pkPdrepdt;

	@Field(value="PK_DEPT")
    private String pkDept;

	@Field(value="PK_STORE")
    private String pkStore;

	@Field(value="PK_PD")
    private String pkPd;

	@Field(value="PK_UNIT_PACK")
    private String pkUnitPack;

	@Field(value="PACK_SIZE")
    private Integer packSize;

	@Field(value="PRICE_ORG")
    private Double priceOrg;

	@Field(value="PRICE")
    private Double price;

	@Field(value="QUAN_REP")
    private Double quanRep;

	@Field(value="AMOUNT_ORG")
    private Double amountOrg;

	@Field(value="AMOUNT")
    private Double amount;

	@Field(value="AMOUNT_REP")
    private Double amountRep;

	@Field(value="NOTE")
    private String note;

	@Field(value="MODITY_TIME")
    private Date modityTime;


    public String getPkPdrephist(){
        return this.pkPdrephist;
    }
    public void setPkPdrephist(String pkPdrephist){
        this.pkPdrephist = pkPdrephist;
    }

    public String getDtReptype(){
        return this.dtReptype;
    }
    public void setDtReptype(String dtReptype){
        this.dtReptype = dtReptype;
    }

    public String getCodeRep(){
        return this.codeRep;
    }
    public void setCodeRep(String codeRep){
        this.codeRep = codeRep;
    }

    public String getApproval(){
        return this.approval;
    }
    public void setApproval(String approval){
        this.approval = approval;
    }

    public Date getDateReprice(){
        return this.dateReprice;
    }
    public void setDateReprice(Date dateReprice){
        this.dateReprice = dateReprice;
    }

    public String getPkPdrepdt(){
        return this.pkPdrepdt;
    }
    public void setPkPdrepdt(String pkPdrepdt){
        this.pkPdrepdt = pkPdrepdt;
    }

    public String getPkDept(){
        return this.pkDept;
    }
    public void setPkDept(String pkDept){
        this.pkDept = pkDept;
    }

    public String getPkStore(){
        return this.pkStore;
    }
    public void setPkStore(String pkStore){
        this.pkStore = pkStore;
    }

    public String getPkPd(){
        return this.pkPd;
    }
    public void setPkPd(String pkPd){
        this.pkPd = pkPd;
    }

    public String getPkUnitPack(){
        return this.pkUnitPack;
    }
    public void setPkUnitPack(String pkUnitPack){
        this.pkUnitPack = pkUnitPack;
    }

    public Integer getPackSize(){
        return this.packSize;
    }
    public void setPackSize(Integer packSize){
        this.packSize = packSize;
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

    public Double getQuanRep(){
        return this.quanRep;
    }
    public void setQuanRep(Double quanRep){
        this.quanRep = quanRep;
    }

    public Double getAmountOrg(){
        return this.amountOrg;
    }
    public void setAmountOrg(Double amountOrg){
        this.amountOrg = amountOrg;
    }

    public Double getAmount(){
        return this.amount;
    }
    public void setAmount(Double amount){
        this.amount = amount;
    }

    public Double getAmountRep(){
        return this.amountRep;
    }
    public void setAmountRep(Double amountRep){
        this.amountRep = amountRep;
    }

    public String getNote(){
        return this.note;
    }
    public void setNote(String note){
        this.note = note;
    }

    public Date getModityTime(){
        return this.modityTime;
    }
    public void setModityTime(Date modityTime){
        this.modityTime = modityTime;
    }
}