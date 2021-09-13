package com.zebone.nhis.common.module.base.bd.code;

import java.util.Date;
import java.math.BigDecimal;

import com.zebone.platform.modules.dao.build.au.Field;
import com.zebone.platform.modules.dao.build.au.PK;
import com.zebone.platform.modules.dao.build.au.Table;
import com.zebone.platform.modules.dao.build.au.Field.FieldType;
import com.zebone.platform.modules.dao.build.au.Field.KeyId;


/**
 * Table: BD_WORKCALENDAR  - bd_workcalendar 
 *
 * @since 2016-08-30 01:08:04
 */
@Table(value="BD_WORKCALENDAR")
public class BdWorkcalendar   {

	@PK
	@Field(value="PK_WORKCALENDAR",id=KeyId.UUID)
    private String pkWorkcalendar;

	@Field(value="PK_ORG")
    private String pkOrg;

	@Field(value="PK_WORKCALENDRULE")
    private String pkWorkcalendrule;

	@Field(value="CODE")
    private String code;

	@Field(value="NAME")
    private String name;

	@Field(value="CALENDAR_DEF")
    private String calendarDef;

	@Field(value="BEGINDATE")
    private Date begindate;

	@Field(value="ENDDATE")
    private Date enddate;

	@Field(value="YEARSTARTDATE")
    private Date yearstartdate;

	@Field(value="FIRSTWEEKDAY")
    private BigDecimal firstweekday;

	@Field(userfield="pkEmp",userfieldscop=FieldType.INSERT)
    private String creator;

	@Field(value="CREATE_TIME",date=FieldType.INSERT)
    private Date createTime;

	@Field(date=FieldType.ALL)
    private Date ts;


    public String getPkWorkcalendar(){
        return this.pkWorkcalendar;
    }
    public void setPkWorkcalendar(String pkWorkcalendar){
        this.pkWorkcalendar = pkWorkcalendar;
    }

    public String getPkOrg(){
        return this.pkOrg;
    }
    public void setPkOrg(String pkOrg){
        this.pkOrg = pkOrg;
    }

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

    public String getCalendarDef() {
		return calendarDef;
	}
	public void setCalendarDef(String calendarDef) {
		this.calendarDef = calendarDef;
	}
	public Date getBegindate(){
        return this.begindate;
    }
    public void setBegindate(Date begindate){
        this.begindate = begindate;
    }

    public Date getEnddate(){
        return this.enddate;
    }
    public void setEnddate(Date enddate){
        this.enddate = enddate;
    }

    public Date getYearstartdate(){
        return this.yearstartdate;
    }
    public void setYearstartdate(Date yearstartdate){
        this.yearstartdate = yearstartdate;
    }

    public BigDecimal getFirstweekday(){
        return this.firstweekday;
    }
    public void setFirstweekday(BigDecimal firstweekday){
        this.firstweekday = firstweekday;
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
}