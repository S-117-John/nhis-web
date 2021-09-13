package com.zebone.nhis.common.module.base.bd.code;

import java.math.BigDecimal;

import com.zebone.nhis.common.module.BaseModule;
import com.zebone.platform.modules.dao.build.au.Field;
import com.zebone.platform.modules.dao.build.au.PK;
import com.zebone.platform.modules.dao.build.au.Table;
import com.zebone.platform.modules.dao.build.au.Field.KeyId;


/**
 * Table: BD_CODE_DATESLOT_TIME  - bd_code_dateslot_time 
 *
 * @since 2016-08-30 01:07:21
 */
@Table(value="BD_CODE_DATESLOT_TIME")
public class BdCodeDateslotTime extends BaseModule  {

	@PK
	@Field(value="PK_DATESLOTTIME",id=KeyId.UUID)
    private String pkDateslottime;

	@Field(value="PK_DATESLOT")
    private String pkDateslot;

	@Field(value="CODE")
    private String code;

	@Field(value="NAME")
    private String name;

	@Field(value="TIME_BEGIN")
    private String timeBegin;

	@Field(value="TIME_END")
    private String timeEnd;

	@Field(value="VALID_MONTH_BEGIN")
    private BigDecimal validMonthBegin;

	@Field(value="VALID_DAY_BEGIN")
    private BigDecimal validDayBegin;

	@Field(value="VALID_MONTH_END")
    private BigDecimal validMonthEnd;

	@Field(value="VALID_DAY_END")
    private BigDecimal validDayEnd;


    public String getPkDateslottime(){
        return this.pkDateslottime;
    }
    public void setPkDateslottime(String pkDateslottime){
        this.pkDateslottime = pkDateslottime;
    }

    public String getPkDateslot(){
        return this.pkDateslot;
    }
    public void setPkDateslot(String pkDateslot){
        this.pkDateslot = pkDateslot;
    }

    public String getCode(){
        return this.code;
    }
    public void setCode(String code){
        this.code = code;
    }

    public String getName(){
        return this.name;
    }
    public void setName(String name){
        this.name = name;
    }

    public String getTimeBegin(){
        return this.timeBegin;
    }
    public void setTimeBegin(String timeBegin){
        this.timeBegin = timeBegin;
    }

    public String getTimeEnd(){
        return this.timeEnd;
    }
    public void setTimeEnd(String timeEnd){
        this.timeEnd = timeEnd;
    }

    public BigDecimal getValidMonthBegin(){
        return this.validMonthBegin;
    }
    public void setValidMonthBegin(BigDecimal validMonthBegin){
        this.validMonthBegin = validMonthBegin;
    }

    public BigDecimal getValidDayBegin(){
        return this.validDayBegin;
    }
    public void setValidDayBegin(BigDecimal validDayBegin){
        this.validDayBegin = validDayBegin;
    }

    public BigDecimal getValidMonthEnd(){
        return this.validMonthEnd;
    }
    public void setValidMonthEnd(BigDecimal validMonthEnd){
        this.validMonthEnd = validMonthEnd;
    }

    public BigDecimal getValidDayEnd(){
        return this.validDayEnd;
    }
    public void setValidDayEnd(BigDecimal validDayEnd){
        this.validDayEnd = validDayEnd;
    }
}