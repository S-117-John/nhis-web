package com.zebone.nhis.common.module.sch.plan;

import com.zebone.nhis.common.module.BaseModule;
import com.zebone.platform.modules.dao.build.au.Field;
import com.zebone.platform.modules.dao.build.au.Field.KeyId;
import com.zebone.platform.modules.dao.build.au.PK;
import com.zebone.platform.modules.dao.build.au.Table;

import java.util.Date;


/**
 * Table: SCH_TICKET  - sch_ticket 
 *
 * @since 2016-09-23 09:14:38
 */
@Table(value="SCH_TICKET")
public class SchTicket extends BaseModule  {

	@PK
	@Field(value="PK_SCHTICKET",id=KeyId.UUID)
    private String pkSchticket;

	@Field(value="PK_SCH")
    private String pkSch;

	@Field(value="TICKETNO")
    private String ticketno;

	@Field(value="FLAG_APPT")
    private String flagAppt;

	@Field(value="FLAG_USED")
    private String flagUsed;

	@Field(value="BEGIN_TIME")
    private Date beginTime;

	@Field(value="END_TIME")
    private Date endTime;

	@Field(value="FLAG_STOP")
    private String flagStop;

	@Field(value="EU_PVTYPE")
    private String euPvtype;

    @Field(value="FLAG_LOCK")
    private String flagLock;

    @Field(value="DT_APPTYPE")
    private String dtApptype;

    @Field(value="ID_LOCK")
    private String idLock;

    @Field(value = "PK_SCHTICKET_MAIN")
    private String pkSchticketMain;

    @Field(value = "SUG_TIME")
    private Date sugTime;

    private String strBeginTime;

    public Date getSugTime() {
        return sugTime;
    }

    public void setSugTime(Date sugTime) {
        this.sugTime = sugTime;
    }

    public String getFlagLock() {
        return flagLock;
    }

    public void setFlagLock(String flagLock) {
        this.flagLock = flagLock;
    }

    public String getDtApptype() {
        return dtApptype;
    }

    public void setDtApptype(String dtApptype) {
        this.dtApptype = dtApptype;
    }

    public String getIdLock() {
        return idLock;
    }

    public void setIdLock(String idLock) {
        this.idLock = idLock;
    }

    public String getPkSchticket(){
        return this.pkSchticket;
    }
    public void setPkSchticket(String pkSchticket){
        this.pkSchticket = pkSchticket;
    }

    public String getPkSch(){
        return this.pkSch;
    }
    public void setPkSch(String pkSch){
        this.pkSch = pkSch;
    }

    public String getTicketno(){
        return this.ticketno;
    }
    public void setTicketno(String ticketno){
        this.ticketno = ticketno;
    }

    public String getFlagAppt(){
        return this.flagAppt;
    }
    public void setFlagAppt(String flagAppt){
        this.flagAppt = flagAppt;
    }

    public String getFlagUsed(){
        return this.flagUsed;
    }
    public void setFlagUsed(String flagUsed){
        this.flagUsed = flagUsed;
    }

    public Date getBeginTime(){
        return this.beginTime;
    }
    public void setBeginTime(Date beginTime){
        this.beginTime = beginTime;
    }

    public Date getEndTime(){
        return this.endTime;
    }
    public void setEndTime(Date endTime){
        this.endTime = endTime;
    }

    public String getFlagStop(){
        return this.flagStop;
    }
    public void setFlagStop(String flagStop){
        this.flagStop = flagStop;
    }

    public String getEuPvtype(){
        return this.euPvtype;
    }
    public void setEuPvtype(String euPvtype){
        this.euPvtype = euPvtype;
    }

	public String getStrBeginTime() {
		return strBeginTime;
	}

	public void setStrBeginTime(String strBeginTime) {
		this.strBeginTime = strBeginTime;
	}

    public String getPkSchticketMain() {
        return pkSchticketMain;
    }

    public void setPkSchticketMain(String pkSchticketMain) {
        this.pkSchticketMain = pkSchticketMain;
    }
}