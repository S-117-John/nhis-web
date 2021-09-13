package com.zebone.nhis.common.module.bl;

import java.util.Date;

import com.zebone.nhis.common.module.BaseModule;
import com.zebone.platform.modules.dao.build.au.Field;
import com.zebone.platform.modules.dao.build.au.PK;
import com.zebone.platform.modules.dao.build.au.Table;
import com.zebone.platform.modules.dao.build.au.Field.KeyId;


/**
 * Table: BL_PI 
 * 非就诊收费明细表
 * @since 2018-01-17 01:19:12
 */
@Table(value="BL_PI")
public class BlPi extends BaseModule  {

	@PK
	@Field(value="PK_BLPI",id=KeyId.UUID)
    private String pkBlpi;

	@Field(value="PK_PI")
    private String pkPi;

	@Field(value="PK_ITEM")
    private String pkItem;

	@Field(value="NAME_BL")
    private String nameBl;

	@Field(value="UNIT")
    private String unit;

	@Field(value="SPEC")
    private String spec;

	@Field(value="PRICE")
    private Double price;

	@Field(value="QUAN")
    private Double quan;

	@Field(value="AMOUNT")
    private Double amount;

	@Field(value="FLAG_PD")
    private String flagPd;

	@Field(value="PK_PD")
    private String pkPd;

	@Field(value="PK_BLPI_BACK")
    private String pkBlpiBack;

	@Field(value="NOTE")
    private String note;

	@Field(value="EU_BUTYPE")
    private String euButype;

	@Field(value="PK_BU")
    private String pkBu;

	@Field(value="CODE_BL")
    private String codeBl;

	@Field(value="DATE_BL")
    private Date dateBl;

	@Field(value="PK_DEPT_BL")
    private String pkDeptBl;

	@Field(value="PK_EMP_BL")
    private String pkEmpBl;

	@Field(value="NAME_EMP_BL")
    private String nameEmpBl;

	@Field(value="FLAG_CC")
    private String flagCc;

	@Field(value="PK_CC")
    private String pkCc;

	@Field(value="MODIFIER")
    private String modifier;

	@Field(value="MODITY_TIME")
    private Date modityTime;

	@Field(value="DT_PAYMODE")
    private String dtPaymode;


    public String getPkBlpi(){
        return this.pkBlpi;
    }
    public void setPkBlpi(String pkBlpi){
        this.pkBlpi = pkBlpi;
    }

    public String getPkPi(){
        return this.pkPi;
    }
    public void setPkPi(String pkPi){
        this.pkPi = pkPi;
    }

    public String getPkItem(){
        return this.pkItem;
    }
    public void setPkItem(String pkItem){
        this.pkItem = pkItem;
    }

    public String getNameBl(){
        return this.nameBl;
    }
    public void setNameBl(String nameBl){
        this.nameBl = nameBl;
    }

    public String getUnit(){
        return this.unit;
    }
    public void setUnit(String unit){
        this.unit = unit;
    }

    public String getSpec(){
        return this.spec;
    }
    public void setSpec(String spec){
        this.spec = spec;
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

    public String getFlagPd(){
        return this.flagPd;
    }
    public void setFlagPd(String flagPd){
        this.flagPd = flagPd;
    }

    public String getPkPd(){
        return this.pkPd;
    }
    public void setPkPd(String pkPd){
        this.pkPd = pkPd;
    }

    public String getPkBlpiBack(){
        return this.pkBlpiBack;
    }
    public void setPkBlpiBack(String pkBlpiBack){
        this.pkBlpiBack = pkBlpiBack;
    }

    public String getNote(){
        return this.note;
    }
    public void setNote(String note){
        this.note = note;
    }

    public String getEuButype(){
        return this.euButype;
    }
    public void setEuButype(String euButype){
        this.euButype = euButype;
    }

    public String getPkBu(){
        return this.pkBu;
    }
    public void setPkBu(String pkBu){
        this.pkBu = pkBu;
    }

    public String getCodeBl(){
        return this.codeBl;
    }
    public void setCodeBl(String codeBl){
        this.codeBl = codeBl;
    }

    public Date getDateBl(){
        return this.dateBl;
    }
    public void setDateBl(Date dateBl){
        this.dateBl = dateBl;
    }

    public String getPkDeptBl(){
        return this.pkDeptBl;
    }
    public void setPkDeptBl(String pkDeptBl){
        this.pkDeptBl = pkDeptBl;
    }

    public String getPkEmpBl(){
        return this.pkEmpBl;
    }
    public void setPkEmpBl(String pkEmpBl){
        this.pkEmpBl = pkEmpBl;
    }

    public String getNameEmpBl(){
        return this.nameEmpBl;
    }
    public void setNameEmpBl(String nameEmpBl){
        this.nameEmpBl = nameEmpBl;
    }

    public String getFlagCc(){
        return this.flagCc;
    }
    public void setFlagCc(String flagCc){
        this.flagCc = flagCc;
    }

    public String getPkCc(){
        return this.pkCc;
    }
    public void setPkCc(String pkCc){
        this.pkCc = pkCc;
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

    public String getDtPaymode(){
        return this.dtPaymode;
    }
    public void setDtPaymode(String dtPaymode){
        this.dtPaymode = dtPaymode;
    }
}
