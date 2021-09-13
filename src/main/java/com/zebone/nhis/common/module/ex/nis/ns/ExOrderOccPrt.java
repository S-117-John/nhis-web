package com.zebone.nhis.common.module.ex.nis.ns;

import java.util.Date;

import com.zebone.nhis.common.module.BaseModule;
import com.zebone.platform.modules.dao.build.au.Field;
import com.zebone.platform.modules.dao.build.au.PK;
import com.zebone.platform.modules.dao.build.au.Table;
import com.zebone.platform.modules.dao.build.au.Field.KeyId;

/**
 * Table: EX_ORDER_OCC_PRT - ex_order_occ_prt 
 *
 * @since 2018-09-13 10:07:35
 */
@Table(value="EX_ORDER_OCC_PRT")
public class ExOrderOccPrt extends BaseModule  {

	@PK
	@Field(value="PK_EXOCCPRT",id=KeyId.UUID)
    private String pkExoccprt;

	@Field(value="PK_DEPT_PRT")
    private String pkDeptPrt;

	@Field(value="PK_DEPT_NS_PRT")
    private String pkDeptNsPrt;

	@Field(value="PK_EMP_PRT")
    private String pkEmpPrt;

	@Field(value="NAME_EMP_PRT")
    private String nameEmpPrt;

    /** PK_EXOCC - 当执行单类型为变更单时，该字段信息为空 */
	@Field(value="PK_EXOCC")
    private String pkExocc;

    /** EU_EXPTTYPE - 1变更单 2执行单 3明细执行单 4大输液卡5小输液卡 */
	@Field(value="EU_EXPTTYPE")
    private String euExpttype;

	@Field(value="MODITY_TIME")
    private Date modityTime;

	@Field(value="DT_EXCARDTYPE")
    private String dtExcardtype;

	@Field(value="PK_TEMPORDEX")
    private String pkTempordex;

	@Field(value="DATE_PRT")
    private Date datePrt;


    public String getPkExoccprt(){
        return this.pkExoccprt;
    }
    public void setPkExoccprt(String pkExoccprt){
        this.pkExoccprt = pkExoccprt;
    }

    public String getPkDeptPrt(){
        return this.pkDeptPrt;
    }
    public void setPkDeptPrt(String pkDeptPrt){
        this.pkDeptPrt = pkDeptPrt;
    }

    public String getPkDeptNsPrt(){
        return this.pkDeptNsPrt;
    }
    public void setPkDeptNsPrt(String pkDeptNsPrt){
        this.pkDeptNsPrt = pkDeptNsPrt;
    }

    public String getPkEmpPrt(){
        return this.pkEmpPrt;
    }
    public void setPkEmpPrt(String pkEmpPrt){
        this.pkEmpPrt = pkEmpPrt;
    }

    public String getNameEmpPrt(){
        return this.nameEmpPrt;
    }
    public void setNameEmpPrt(String nameEmpPrt){
        this.nameEmpPrt = nameEmpPrt;
    }

    public String getPkExocc(){
        return this.pkExocc;
    }
    public void setPkExocc(String pkExocc){
        this.pkExocc = pkExocc;
    }

    public String getEuExpttype(){
        return this.euExpttype;
    }
    public void setEuExpttype(String euExpttype){
        this.euExpttype = euExpttype;
    }

    public Date getModityTime(){
        return this.modityTime;
    }
    public void setModityTime(Date modityTime){
        this.modityTime = modityTime;
    }

    public String getDtExcardtype(){
        return this.dtExcardtype;
    }
    public void setDtExcardtype(String dtExcardtype){
        this.dtExcardtype = dtExcardtype;
    }

    public String getPkTempordex(){
        return this.pkTempordex;
    }
    public void setPkTempordex(String pkTempordex){
        this.pkTempordex = pkTempordex;
    }

    public Date getDatePrt(){
        return this.datePrt;
    }
    public void setDatePrt(Date datePrt){
        this.datePrt = datePrt;
    }
}