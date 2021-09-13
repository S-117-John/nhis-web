package com.zebone.nhis.common.module.scm.st;

import java.util.Date;

import com.zebone.nhis.common.module.BaseModule;
import com.zebone.platform.modules.dao.build.au.Field;
import com.zebone.platform.modules.dao.build.au.PK;
import com.zebone.platform.modules.dao.build.au.Table;
import com.zebone.platform.modules.dao.build.au.Field.KeyId;


/**
 * Table: PD_INV_DETAIL - pd_inv_detail 
 *
 * @since 2016-11-09 09:31:37
 */
@Table(value="PD_INV_DETAIL")
public class PdInvDetail extends BaseModule  {

	@PK
	@Field(value="PK_PDINVDT",id=KeyId.UUID)
    private String pkPdinvdt;

	@Field(value="PK_PDINV")
    private String pkPdinv;

	@Field(value="SORT_NO")
    private Integer sortNo;

	@Field(value="PK_PD")
    private String pkPd;

	@Field(value="PK_UNIT_PACK")
    private String pkUnitPack;

	@Field(value="PACK_SIZE")
    private Integer packSize;

	@Field(value="BATCH_NO")
    private String batchNo;

	@Field(value="DATE_EXPIRE")
    private Date dateExpire;

	@Field(value="POSI_NO")
    private String posiNo;

	@Field(value="PRICE")
    private Double price;

	@Field(value="PRICE_COST")
    private Double priceCost;

	@Field(value="QUAN_PACK_ACC")
    private Double quanPackAcc;

	@Field(value="QUAN_MIN_ACC")
    private Double quanMinAcc;

	@Field(value="QUAN_PACK")
    private Double quanPack;

	@Field(value="QUAN_MIN")
    private Double quanMin;

	@Field(value="QUAN_DIFF")
    private Double quanDiff;

    /** REASON_DIFF - 仅在有差异时，使用此字段 */
	@Field(value="REASON_DIFF")
    private String reasonDiff;

    /** PK_PDST - 仅在有差异时，使用此字段 */
	@Field(value="PK_PDST")
    private String pkPdst;

	@Field(value="PK_EMP_INPUT")
    private String pkEmpInput;

	@Field(value="NAME_EMP_INPUT")
    private String nameEmpInput;

    /** NOTE - 仅 */
	@Field(value="NOTE")
    private String note;

	@Field(value="MODITY_TIME")
    private Date modityTime;


    public String getPkPdinvdt(){
        return this.pkPdinvdt;
    }
    public void setPkPdinvdt(String pkPdinvdt){
        this.pkPdinvdt = pkPdinvdt;
    }

    public String getPkPdinv(){
        return this.pkPdinv;
    }
    public void setPkPdinv(String pkPdinv){
        this.pkPdinv = pkPdinv;
    }

    public Integer getSortNo(){
        return this.sortNo;
    }
    public void setSortNo(Integer sortNo){
        this.sortNo = sortNo;
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

    public String getPosiNo(){
        return this.posiNo;
    }
    public void setPosiNo(String posiNo){
        this.posiNo = posiNo;
    }

    public Double getPrice(){
        return this.price;
    }
    public void setPrice(Double price){
        this.price = price;
    }

    public Double getPriceCost(){
        return this.priceCost;
    }
    public void setPriceCost(Double priceCost){
        this.priceCost = priceCost;
    }

    public Double getQuanPackAcc(){
        return this.quanPackAcc;
    }
    public void setQuanPackAcc(Double quanPackAcc){
        this.quanPackAcc = quanPackAcc;
    }

    public Double getQuanMinAcc(){
        return this.quanMinAcc;
    }
    public void setQuanMinAcc(Double quanMinAcc){
        this.quanMinAcc = quanMinAcc;
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

    public Double getQuanDiff(){
        return this.quanDiff;
    }
    public void setQuanDiff(Double quanDiff){
        this.quanDiff = quanDiff;
    }

    public String getReasonDiff(){
        return this.reasonDiff;
    }
    public void setReasonDiff(String reasonDiff){
        this.reasonDiff = reasonDiff;
    }

    public String getPkPdst(){
        return this.pkPdst;
    }
    public void setPkPdst(String pkPdst){
        this.pkPdst = pkPdst;
    }

    public String getPkEmpInput(){
        return this.pkEmpInput;
    }
    public void setPkEmpInput(String pkEmpInput){
        this.pkEmpInput = pkEmpInput;
    }

    public String getNameEmpInput(){
        return this.nameEmpInput;
    }
    public void setNameEmpInput(String nameEmpInput){
        this.nameEmpInput = nameEmpInput;
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