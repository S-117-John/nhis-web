package com.zebone.nhis.common.module.base.bd.code;


import com.zebone.nhis.common.module.BaseModule;
import com.zebone.platform.modules.dao.build.au.Field;
import com.zebone.platform.modules.dao.build.au.PK;
import com.zebone.platform.modules.dao.build.au.Table;
import com.zebone.platform.modules.dao.build.au.Field.KeyId;


/**
 * Table: BD_WORKCALENDRULE  - bd_workcalendrule 
 *
 * @since 2016-08-30 01:08:14
 */
@Table(value="BD_WORKCALENDRULE")
public class BdWorkcalendrule extends BaseModule  {

	@PK
	@Field(value="PK_WORKCALENDRULE",id=KeyId.UUID)
    private String pkWorkcalendrule;

	@Field(value="CODE")
    private String code;

	@Field(value="NAME")
    private String name;

	@Field(value="MONDAY")
    private String monday;

	@Field(value="TUESDAY")
    private String tuesday;

	@Field(value="WEDNESDAY")
    private String wednesday;

	@Field(value="THURSDAY")
    private String thursday;

	@Field(value="FRIDAY")
    private String friday;

	@Field(value="SATURDAY")
    private String saturday;

	@Field(value="SUNDAY")
    private String sunday;

	@Field(value="ONDUTYTIME")
    private String ondutytime;

	@Field(value="OFFDUTYTIME")
    private String offdutytime;

	@Field(value="NOTE")
    private String note;


    public String getPkWorkcalendrule(){
        return this.pkWorkcalendrule;
    }
    public void setPkWorkcalendrule(String pkWorkcalendrule){
        this.pkWorkcalendrule = pkWorkcalendrule;
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

    public String getMonday(){
        return this.monday;
    }
    public void setMonday(String monday){
        this.monday = monday;
    }

    public String getTuesday(){
        return this.tuesday;
    }
    public void setTuesday(String tuesday){
        this.tuesday = tuesday;
    }

    public String getWednesday(){
        return this.wednesday;
    }
    public void setWednesday(String wednesday){
        this.wednesday = wednesday;
    }

    public String getThursday(){
        return this.thursday;
    }
    public void setThursday(String thursday){
        this.thursday = thursday;
    }

    public String getFriday(){
        return this.friday;
    }
    public void setFriday(String friday){
        this.friday = friday;
    }

    public String getSaturday(){
        return this.saturday;
    }
    public void setSaturday(String saturday){
        this.saturday = saturday;
    }

    public String getSunday(){
        return this.sunday;
    }
    public void setSunday(String sunday){
        this.sunday = sunday;
    }

    public String getOndutytime(){
        return this.ondutytime;
    }
    public void setOndutytime(String ondutytime){
        this.ondutytime = ondutytime;
    }

    public String getOffdutytime(){
        return this.offdutytime;
    }
    public void setOffdutytime(String offdutytime){
        this.offdutytime = offdutytime;
    }

    public String getNote(){
        return this.note;
    }
    public void setNote(String note){
        this.note = note;
    }
}