package com.zebone.nhis.common.module.pv;

import java.util.Date;

import com.zebone.nhis.common.module.BaseModule;
import com.zebone.platform.modules.dao.build.au.Field;
import com.zebone.platform.modules.dao.build.au.Field.FieldType;
import com.zebone.platform.modules.dao.build.au.Field.KeyId;
import com.zebone.platform.modules.dao.build.au.PK;
import com.zebone.platform.modules.dao.build.au.Table;


/**
 * Table: PV_DAYCG  - 患者就诊-固定费用 
 *
 * @since 2016-09-23 09:52:54
 */
@Table(value="PV_DAYCG")
public class PvDaycg extends BaseModule  {

	@PK
	@Field(value="PK_DAYCG",id=KeyId.UUID)
    private String pkDaycg;

	@Field(value="PK_PV")
    private String pkPv;

    /** EU_DAYCGMODE - 0 继承科室，1 患者专有 */
	@Field(value="EU_DAYCGMODE")
    private String euDaycgmode;

    /** DATE_DAYCG - 固定费用记费时，写入记费的日期 */
	@Field(value="DATE_DAYCG")
    private Date dateDaycg;

	@Field(value="DATE_BED")
    private Date dateBed;

	@Field(value="FLAG_STOP_BED")
    private String flagStopBed;

	@Field(userfield="pkEmp",userfieldscop=FieldType.ALL)
    private String modifier;

	@Field(value="MODITY_TIME")
    private Date modityTime;


    public String getPkDaycg(){
        return this.pkDaycg;
    }
    public void setPkDaycg(String pkDaycg){
        this.pkDaycg = pkDaycg;
    }

    public String getPkPv(){
        return this.pkPv;
    }
    public void setPkPv(String pkPv){
        this.pkPv = pkPv;
    }

    public String getEuDaycgmode(){
        return this.euDaycgmode;
    }
    public void setEuDaycgmode(String euDaycgmode){
        this.euDaycgmode = euDaycgmode;
    }

    public Date getDateDaycg(){
        return this.dateDaycg;
    }
    public void setDateDaycg(Date dateDaycg){
        this.dateDaycg = dateDaycg;
    }

    public Date getDateBed(){
        return this.dateBed;
    }
    public void setDateBed(Date dateBed){
        this.dateBed = dateBed;
    }

    public String getFlagStopBed(){
        return this.flagStopBed;
    }
    public void setFlagStopBed(String flagStopBed){
        this.flagStopBed = flagStopBed;
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
}