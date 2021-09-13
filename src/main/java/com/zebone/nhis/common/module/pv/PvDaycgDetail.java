package com.zebone.nhis.common.module.pv;


import com.zebone.nhis.common.module.BaseModule;
import com.zebone.platform.modules.dao.build.au.Field;
import com.zebone.platform.modules.dao.build.au.Field.FieldType;
import com.zebone.platform.modules.dao.build.au.Field.KeyId;
import com.zebone.platform.modules.dao.build.au.PK;
import com.zebone.platform.modules.dao.build.au.Table;


/**
 * Table: PV_DAYCG_DETAIL  - 患者就诊-固定费用-明细 
 *
 * @since 2016-09-23 09:51:16
 */
@Table(value="PV_DAYCG_DETAIL")
public class PvDaycgDetail extends BaseModule  {

	@PK
	@Field(value="PK_DAYCGDT",id=KeyId.UUID)
    private String pkDaycgdt;

	@Field(value="PK_DAYCG")
    private String pkDaycg;

	@Field(value="PK_ITEM")
    private String pkItem;

	@Field(value="BEGIN_MONTH")
    private Long beginMonth;

	@Field(value="END_MONTH")
    private Long endMonth;

	@Field(value="BEGIN_DAY")
    private Long beginDay;

	@Field(value="END_DAY")
    private Long endDay;

    /** EU_CGMODE - 0按人收费1按床收费 */
	@Field(value="EU_CGMODE")
    private String euCgmode;

	@Field(value="FLAG_ACTIVE")
    private String flagActive;

	@Field(userfield="pkEmp",userfieldscop=FieldType.ALL)
    private String modifier;


    public String getPkDaycgdt(){
        return this.pkDaycgdt;
    }
    public void setPkDaycgdt(String pkDaycgdt){
        this.pkDaycgdt = pkDaycgdt;
    }

    public String getPkDaycg(){
        return this.pkDaycg;
    }
    public void setPkDaycg(String pkDaycg){
        this.pkDaycg = pkDaycg;
    }

    public String getPkItem(){
        return this.pkItem;
    }
    public void setPkItem(String pkItem){
        this.pkItem = pkItem;
    }

    public Long getBeginMonth(){
        return this.beginMonth;
    }
    public void setBeginMonth(Long beginMonth){
        this.beginMonth = beginMonth;
    }

    public Long getEndMonth(){
        return this.endMonth;
    }
    public void setEndMonth(Long endMonth){
        this.endMonth = endMonth;
    }

    public Long getBeginDay(){
        return this.beginDay;
    }
    public void setBeginDay(Long beginDay){
        this.beginDay = beginDay;
    }

    public Long getEndDay(){
        return this.endDay;
    }
    public void setEndDay(Long endDay){
        this.endDay = endDay;
    }

    public String getEuCgmode(){
        return this.euCgmode;
    }
    public void setEuCgmode(String euCgmode){
        this.euCgmode = euCgmode;
    }

    public String getFlagActive(){
        return this.flagActive;
    }
    public void setFlagActive(String flagActive){
        this.flagActive = flagActive;
    }

    public String getModifier(){
        return this.modifier;
    }
    public void setModifier(String modifier){
        this.modifier = modifier;
    }
}