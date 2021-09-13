package com.zebone.nhis.common.module.sch.pub;

import java.util.Date;

import com.zebone.nhis.common.module.BaseModule;
import com.zebone.platform.modules.dao.build.au.Field;
import com.zebone.platform.modules.dao.build.au.PK;
import com.zebone.platform.modules.dao.build.au.Table;
import com.zebone.platform.modules.dao.build.au.Field.KeyId;


/**
 * Table: SCH_TICKETRULES  - sch_ticketrules 
 *
 * @since 2016-09-13 02:10:06
 */
@Table(value="SCH_TICKETRULES")
public class SchTicketrules extends BaseModule  {

	@PK
	@Field(value="PK_TICKETRULES",id=KeyId.UUID)
    private String pkTicketrules;

	@Field(value="CODE")
    private String code;

	@Field(value="NAME")
    private String name;

    /** EU_RULETYPE - 0 全部可约，1 全部不可约，2 偶数可约，3 奇数可约，4 号段可约，5 号码枚举，9 外挂函数 */
	@Field(value="EU_RULETYPE")
    private String euRuletype;

	@Field(value="BEGIN_NO")
    private Integer beginNo;

	@Field(value="END_NO")
    private Integer endNo;

	@Field(value="TICKETS")
    private String tickets;

	@Field(value="FUNC")
    private String func;

	@Field(value="FLAG_STOP")
    private String flagStop;

	@Field(value="FLAG_DEF")
    private String flagDef;

	@Field(value="MODITY_TIME")
    private Date modityTime;
	
	@Field(value="NOTE")
    private String note;


    public String getPkTicketrules(){
        return this.pkTicketrules;
    }
    public void setPkTicketrules(String pkTicketrules){
        this.pkTicketrules = pkTicketrules;
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

    public String getEuRuletype(){
        return this.euRuletype;
    }
    public void setEuRuletype(String euRuletype){
        this.euRuletype = euRuletype;
    }

    public Integer getBeginNo(){
        return this.beginNo;
    }
    public void setBeginNo(Integer beginNo){
        this.beginNo = beginNo;
    }

    public Integer getEndNo(){
        return this.endNo;
    }
    public void setEndNo(Integer endNo){
        this.endNo = endNo;
    }

    public String getTickets(){
        return this.tickets;
    }
    public void setTickets(String tickets){
        this.tickets = tickets;
    }

    public String getFunc(){
        return this.func;
    }
    public void setFunc(String func){
        this.func = func;
    }

    public String getFlagStop(){
        return this.flagStop;
    }
    public void setFlagStop(String flagStop){
        this.flagStop = flagStop;
    }

    public String getFlagDef(){
        return this.flagDef;
    }
    public void setFlagDef(String flagDef){
        this.flagDef = flagDef;
    }

    public Date getModityTime(){
        return this.modityTime;
    }
    public void setModityTime(Date modityTime){
        this.modityTime = modityTime;
    }
    
	public String getNote() {
		return note;
	}
	public void setNote(String note) {
		this.note = note;
	}
    
    
}