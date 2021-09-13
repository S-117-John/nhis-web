package com.zebone.nhis.common.module.base.bd.code;

import java.util.Date;

import com.zebone.platform.modules.dao.build.au.Field;
import com.zebone.platform.modules.dao.build.au.PK;
import com.zebone.platform.modules.dao.build.au.Table;
import com.zebone.platform.modules.dao.build.au.Field.FieldType;
import com.zebone.platform.modules.dao.build.au.Field.KeyId;


/**
 * Table: BD_WORKCALENDARDATE  - bd_workcalendardate 
 *
 * @since 2016-08-30 01:08:08
 */
@Table(value="BD_WORKCALENDARDATE")
public class BdWorkcalendardate   {

	@PK
	@Field(value="PK_WORKCALENDARDATE",id=KeyId.UUID)
    private String pkWorkcalendardate;

	@Field(value="PK_WORKCALENDAR")
    private String pkWorkcalendar;

	@Field(value="CALENDARDATE")
    private Date calendardate;

    /** DATETYPE - 1）工作日，日期类型=1；
2）公休日，日期类型=0；
3）节假日，日期类型=2； */
	@Field(value="DATETYPE")
    private String datetype;

	@Field(value="ONDUTYTIME")
    private String ondutytime;

	@Field(value="OFFDUTYTIME")
    private String offdutytime;

	@Field(value="MEMO")
    private String memo;

	@Field(userfield="pkEmp",userfieldscop=FieldType.INSERT)
    private String creator;

	@Field(value="CREATE_TIME",date=FieldType.INSERT)
    private Date createTime;

    /** DEL_FLAG - 0:未删除  1:已删除 */
	@Field(value="DEL_FLAG")
    private String delFlag;

	@Field(date=FieldType.ALL)
    private Date ts;

    /** WEEKNO - 星期几 */
	@Field(value="WEEKNO")
    private String weekno;


    public String getPkWorkcalendardate(){
        return this.pkWorkcalendardate;
    }
    public void setPkWorkcalendardate(String pkWorkcalendardate){
        this.pkWorkcalendardate = pkWorkcalendardate;
    }

    public String getPkWorkcalendar(){
        return this.pkWorkcalendar;
    }
    public void setPkWorkcalendar(String pkWorkcalendar){
        this.pkWorkcalendar = pkWorkcalendar;
    }

    public Date getCalendardate(){
        return this.calendardate;
    }
    public void setCalendardate(Date calendardate){
        this.calendardate = calendardate;
    }

    public String getDatetype(){
        return this.datetype;
    }
    public void setDatetype(String datetype){
        this.datetype = datetype;
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

    public String getMemo(){
        return this.memo;
    }
    public void setMemo(String memo){
        this.memo = memo;
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

    public String getDelFlag(){
        return this.delFlag;
    }
    public void setDelFlag(String delFlag){
        this.delFlag = delFlag;
    }

    public Date getTs(){
        return this.ts;
    }
    public void setTs(Date ts){
        this.ts = ts;
    }

    public String getWeekno(){
        return this.weekno;
    }
    public void setWeekno(String weekno){
        this.weekno = weekno;
    }
}