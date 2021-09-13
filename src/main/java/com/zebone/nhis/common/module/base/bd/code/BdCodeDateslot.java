package com.zebone.nhis.common.module.base.bd.code;

import java.util.Date;
import java.math.BigDecimal;

import com.zebone.platform.modules.dao.build.au.Field;
import com.zebone.platform.modules.dao.build.au.PK;
import com.zebone.platform.modules.dao.build.au.Table;
import com.zebone.platform.modules.dao.build.au.Field.FieldType;
import com.zebone.platform.modules.dao.build.au.Field.KeyId;


/**
 * Table: BD_CODE_DATESLOT  - bd_code_dateslot 
 *
 * @since 2016-08-30 01:07:06
 */
@Table(value="BD_CODE_DATESLOT")
public class BdCodeDateslot   {

	@PK
	@Field(value="PK_DATESLOT",id=KeyId.UUID)
    private String pkDateslot;

	@Field(value="PK_ORG",userfield="pkOrg",userfieldscop=FieldType.INSERT)
    private String pkOrg;

	@Field(value="DT_DATESLOTTYPE")
    private String dtDateslottype;

    /** SORTNO - 日期分组的排列号 */
	@Field(value="SORTNO")
    private Integer sortno;

	@Field(value="CODE_DATESLOT")
    private String codeDateslot;

	@Field(value="NAME_DATESLOT")
    private String nameDateslot;

	@Field(value="DATESLOTDES")
    private String dateslotdes;

	@Field(value="TIME_BEGIN")
    private String timeBegin;

	@Field(value="TIME_END")
    private String timeEnd;

	@Field(value="FLAG_DAY")
    private String flagDay;

	@Field(value="FLAG_NIGHT")
    private String flagNight;

	@Field(value="FLAG_ACTIVE")
    private String flagActive;
	
	@Field(value="EU_NOON")
    private String euNoon;

	@Field(value="CREATOR",userfield="pkEmp",userfieldscop=FieldType.INSERT)
    private String creator;

	@Field(value="CREATE_TIME",date=FieldType.INSERT)
    private Date createTime;

	@Field(value="TS",date=FieldType.ALL)
    private Date ts;


    public String getPkDateslot(){
        return this.pkDateslot;
    }
    public void setPkDateslot(String pkDateslot){
        this.pkDateslot = pkDateslot;
    }

    public String getPkOrg(){
        return this.pkOrg;
    }
    public void setPkOrg(String pkOrg){
        this.pkOrg = pkOrg;
    }

    public String getDtDateslottype(){
        return this.dtDateslottype;
    }
    public void setDtDateslottype(String dtDateslottype){
        this.dtDateslottype = dtDateslottype;
    }

    public Integer getSortno(){
        return this.sortno;
    }
    public void setSortno(Integer sortno){
        this.sortno = sortno;
    }

    public String getCodeDateslot(){
        return this.codeDateslot;
    }
    public void setCodeDateslot(String codeDateslot){
        this.codeDateslot = codeDateslot;
    }

    public String getNameDateslot(){
        return this.nameDateslot;
    }
    public void setNameDateslot(String nameDateslot){
        this.nameDateslot = nameDateslot;
    }

    public String getDateslotdes(){
        return this.dateslotdes;
    }
    public void setDateslotdes(String dateslotdes){
        this.dateslotdes = dateslotdes;
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

    public String getFlagDay(){
        return this.flagDay;
    }
    public void setFlagDay(String flagDay){
        this.flagDay = flagDay;
    }

    public String getFlagNight(){
        return this.flagNight;
    }
    public void setFlagNight(String flagNight){
        this.flagNight = flagNight;
    }

    public String getFlagActive(){
        return this.flagActive;
    }
    public void setFlagActive(String flagActive){
        this.flagActive = flagActive;
    }

    public String getCreator(){
        return this.creator;
    }
    public void setCreator(String creator){
        this.creator = creator;
    }

    public Date getCreateTime(){
        return this.createTime;
    }
    public void setCreateTime(Date createTime){
        this.createTime = createTime;
    }

    public Date getTs(){
        return this.ts;
    }
    public void setTs(Date ts){
        this.ts = ts;
    }
	public String getEuNoon() {
		return euNoon;
	}
	public void setEuNoon(String euNoon) {
		this.euNoon = euNoon;
	}
    
}