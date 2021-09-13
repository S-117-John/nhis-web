package com.zebone.nhis.common.module.scm.purchase;

import java.util.Date;

import com.zebone.nhis.common.module.BaseModule;
import com.zebone.platform.modules.dao.build.au.Field;
import com.zebone.platform.modules.dao.build.au.PK;
import com.zebone.platform.modules.dao.build.au.Table;
import com.zebone.platform.modules.dao.build.au.Field.KeyId;


/**
 * Table: PD_PLAN_DETAIL - pd_plan_detail 
 *
 * @since 2016-10-31 11:24:42
 */
@Table(value="PD_PLAN_DETAIL")
public class PdPlanDetail extends BaseModule  {

	@PK
	@Field(value="PK_PDPLANDT",id=KeyId.UUID)
    private String pkPdplandt;

	@Field(value="PK_PDPLAN")
    private String pkPdplan;

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

	@Field(value="QUAN_MIN")
    private Double quanMin;

	@Field(value="DATE_NEED")
    private Date dateNeed;

    /** PK_ORG_SRV - 对应服务库存部门的机构 */
	@Field(value="PK_ORG_SRV")
    private String pkOrgSrv;

    /** PK_DEPT_SRV - 对应服务库存部门 */
	@Field(value="PK_DEPT_SRV")
    private String pkDeptSrv;

	@Field(value="PK_STORE_SRV")
    private String pkStoreSrv;

	@Field(value="PK_SUPPLYER")
    private String pkSupplyer;

	@Field(value="QUAN_DE")
    private Double quanDe;

	@Field(value="DATE_DE")
    private Date dateDe;

	@Field(value="PK_EMP_DE")
    private String pkEmpDe;

	@Field(value="NAME_EMP_DE")
    private String nameEmpDe;

	@Field(value="FLAG_FINISH")
    private String flagFinish;

	@Field(value="PK_PDPUDT")
    private String pkPdpudt;

	@Field(value="NOTE")
    private String note;

	@Field(value="MODITY_TIME")
    private Date modityTime;


    public String getPkPdplandt(){
        return this.pkPdplandt;
    }
    public void setPkPdplandt(String pkPdplandt){
        this.pkPdplandt = pkPdplandt;
    }

    public String getPkPdplan(){
        return this.pkPdplan;
    }
    public void setPkPdplan(String pkPdplan){
        this.pkPdplan = pkPdplan;
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

    public Date getDateNeed(){
        return this.dateNeed;
    }
    public void setDateNeed(Date dateNeed){
        this.dateNeed = dateNeed;
    }

    public String getPkOrgSrv(){
        return this.pkOrgSrv;
    }
    public void setPkOrgSrv(String pkOrgSrv){
        this.pkOrgSrv = pkOrgSrv;
    }

    public String getPkDeptSrv(){
        return this.pkDeptSrv;
    }
    public void setPkDeptSrv(String pkDeptSrv){
        this.pkDeptSrv = pkDeptSrv;
    }

    public String getPkStoreSrv(){
        return this.pkStoreSrv;
    }
    public void setPkStoreSrv(String pkStoreSrv){
        this.pkStoreSrv = pkStoreSrv;
    }

    public String getPkSupplyer(){
        return this.pkSupplyer;
    }
    public void setPkSupplyer(String pkSupplyer){
        this.pkSupplyer = pkSupplyer;
    }

    public Double getQuanDe(){
        return this.quanDe;
    }
    public void setQuanDe(Double quanDe){
        this.quanDe = quanDe;
    }

    public Date getDateDe(){
        return this.dateDe;
    }
    public void setDateDe(Date dateDe){
        this.dateDe = dateDe;
    }

    public String getPkEmpDe(){
        return this.pkEmpDe;
    }
    public void setPkEmpDe(String pkEmpDe){
        this.pkEmpDe = pkEmpDe;
    }

    public String getNameEmpDe(){
        return this.nameEmpDe;
    }
    public void setNameEmpDe(String nameEmpDe){
        this.nameEmpDe = nameEmpDe;
    }

    public String getFlagFinish(){
        return this.flagFinish;
    }
    public void setFlagFinish(String flagFinish){
        this.flagFinish = flagFinish;
    }

    public String getPkPdpudt(){
        return this.pkPdpudt;
    }
    public void setPkPdpudt(String pkPdpudt){
        this.pkPdpudt = pkPdpudt;
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