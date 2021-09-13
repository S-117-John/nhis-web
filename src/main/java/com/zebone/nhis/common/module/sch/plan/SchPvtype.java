package com.zebone.nhis.common.module.sch.plan;

import java.util.Date;

import com.zebone.nhis.common.module.BaseModule;
import com.zebone.platform.modules.dao.build.au.Field;
import com.zebone.platform.modules.dao.build.au.PK;
import com.zebone.platform.modules.dao.build.au.Table;
import com.zebone.platform.modules.dao.build.au.Field.FieldType;
import com.zebone.platform.modules.dao.build.au.Field.KeyId;


/**
 * Table: SCH_PVTYPE  - sch_pvtype 
 *
 * @since 2016-09-23 09:14:38
 */
@Table(value="SCH_PVTYPE")
public class SchPvtype extends BaseModule  {

	@PK
	@Field(value="PK_SCHPVTYPE",id=KeyId.UUID)
    private String pkSchpvtype;

	@Field(value="PK_SCH")
    private String pkSch;

    /** EU_PVTYPE - 1 门诊，2 急诊，3 住院，4 体检，5 家床 */
	@Field(value="EU_PVTYPE")
    private String euPvtype;

	@Field(value="CNT_TOTAL")
    private Integer cntTotal;

	@Field(value="CNT_USED")
    private Integer cntUsed;

	@Field(value="FLAG_STOP")
    private String flagStop;

	@Field(userfield="pkEmp",userfieldscop=FieldType.ALL)
    private String modifier;

	@Field(value="MODITY_TIME")
    private Date modityTime;
	
	@Field(value="TICKETRULES")
    private String ticketrules;
	
	


    public String getPkSchpvtype(){
        return this.pkSchpvtype;
    }
    public void setPkSchpvtype(String pkSchpvtype){
        this.pkSchpvtype = pkSchpvtype;
    }

    public String getPkSch(){
        return this.pkSch;
    }
    public void setPkSch(String pkSch){
        this.pkSch = pkSch;
    }

    public String getEuPvtype(){
        return this.euPvtype;
    }
    public void setEuPvtype(String euPvtype){
        this.euPvtype = euPvtype;
    }

    public Integer getCntTotal(){
        return this.cntTotal;
    }
    public void setCntTotal(Integer cntTotal){
        this.cntTotal = cntTotal;
    }

    public Integer getCntUsed(){
        return this.cntUsed;
    }
    public void setCntUsed(Integer cntUsed){
        this.cntUsed = cntUsed;
    }

    public String getFlagStop(){
        return this.flagStop;
    }
    public void setFlagStop(String flagStop){
        this.flagStop = flagStop;
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
	public String getTicketrules() {
		return ticketrules;
	}
	public void setTicketrules(String ticketrules) {
		this.ticketrules = ticketrules;
	}
    
}