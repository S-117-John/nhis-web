package com.zebone.nhis.common.module.scm.purchase;

import java.util.Date;

import com.zebone.nhis.common.module.BaseModule;
import com.zebone.platform.modules.dao.build.au.Field;
import com.zebone.platform.modules.dao.build.au.PK;
import com.zebone.platform.modules.dao.build.au.Table;
import com.zebone.platform.modules.dao.build.au.Field.KeyId;


/**
 * Table: PD_PURCHASE_DT - pd_purchase_dt 
 *
 * @since 2016-10-31 11:25:37
 */
@Table(value="PD_PURCHASE_DT")
public class PdPurchaseDt extends BaseModule  {

	@PK
	@Field(value="PK_PDPUDT",id=KeyId.UUID)
    private String pkPdpudt;

	@Field(value="PK_PDPU")
    private String pkPdpu;

    /** PK_ORG_USE - 入库对应的机构 */
	@Field(value="PK_ORG_USE")
    private String pkOrgUse;

    /** PK_DEPT_USE - 入库对应的部门 */
	@Field(value="PK_DEPT_USE")
    private String pkDeptUse;

	@Field(value="PK_STORE_USE")
    private String pkStoreUse;

	@Field(value="SORT_NO")
    private Integer sortNo;

	@Field(value="PK_PD")
    private String pkPd;

	@Field(value="PK_UNIT")
    private String pkUnit;

	@Field(value="PACK_SIZE")
    private Integer packSize;

	@Field(value="QUAN_PACK")
    private Double quanPack;

    /** QUAN_MIN - 最小单位 */
	@Field(value="QUAN_MIN")
    private Double quanMin;

	@Field(value="DATE_REQ")
    private Date dateReq;

	@Field(value="QUAN_IN_MIN")
    private Double quanInMin;

	@Field(value="LASTDATE_IN")
    private Date lastdateIn;

	@Field(value="PK_EMP_ACC")
    private String pkEmpAcc;

	@Field(value="NAME_EMP_ACC")
    private String nameEmpAcc;

	@Field(value="FLAG_ACC")
    private String flagAcc;

	@Field(value="FLAG_PAY")
    private String flagPay;

	@Field(value="MODITY_TIME")
    private Date modityTime;


    public String getPkPdpudt(){
        return this.pkPdpudt;
    }
    public void setPkPdpudt(String pkPdpudt){
        this.pkPdpudt = pkPdpudt;
    }

    public String getPkPdpu(){
        return this.pkPdpu;
    }
    public void setPkPdpu(String pkPdpu){
        this.pkPdpu = pkPdpu;
    }

    public String getPkOrgUse(){
        return this.pkOrgUse;
    }
    public void setPkOrgUse(String pkOrgUse){
        this.pkOrgUse = pkOrgUse;
    }

    public String getPkDeptUse(){
        return this.pkDeptUse;
    }
    public void setPkDeptUse(String pkDeptUse){
        this.pkDeptUse = pkDeptUse;
    }

    public String getPkStoreUse(){
        return this.pkStoreUse;
    }
    public void setPkStoreUse(String pkStoreUse){
        this.pkStoreUse = pkStoreUse;
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

    public Date getDateReq(){
        return this.dateReq;
    }
    public void setDateReq(Date dateReq){
        this.dateReq = dateReq;
    }

    public Double getQuanInMin(){
        return this.quanInMin;
    }
    public void setQuanInMin(Double quanInMin){
        this.quanInMin = quanInMin;
    }

    public Date getLastdateIn(){
        return this.lastdateIn;
    }
    public void setLastdateIn(Date lastdateIn){
        this.lastdateIn = lastdateIn;
    }

    public String getPkEmpAcc(){
        return this.pkEmpAcc;
    }
    public void setPkEmpAcc(String pkEmpAcc){
        this.pkEmpAcc = pkEmpAcc;
    }

    public String getNameEmpAcc(){
        return this.nameEmpAcc;
    }
    public void setNameEmpAcc(String nameEmpAcc){
        this.nameEmpAcc = nameEmpAcc;
    }

    public String getFlagAcc(){
        return this.flagAcc;
    }
    public void setFlagAcc(String flagAcc){
        this.flagAcc = flagAcc;
    }

    public String getFlagPay(){
        return this.flagPay;
    }
    public void setFlagPay(String flagPay){
        this.flagPay = flagPay;
    }

    public Date getModityTime(){
        return this.modityTime;
    }
    public void setModityTime(Date modityTime){
        this.modityTime = modityTime;
    }
}