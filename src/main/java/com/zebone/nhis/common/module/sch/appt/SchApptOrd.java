package com.zebone.nhis.common.module.sch.appt;

import java.util.Date;

import com.zebone.nhis.common.module.BaseModule;
import com.zebone.platform.modules.dao.build.au.Field;
import com.zebone.platform.modules.dao.build.au.PK;
import com.zebone.platform.modules.dao.build.au.Table;
import com.zebone.platform.modules.dao.build.au.Field.KeyId;


/**
 * Table: SCH_APPT_ORD - sch_appt_ord 
 *
 * @since 2016-10-21 03:54:04
 */
@Table(value="SCH_APPT_ORD")
public class SchApptOrd extends BaseModule  {

	@PK
	@Field(value="PK_APPTORD",id=KeyId.UUID)
    private String pkApptord;

	@Field(value="PK_SCHAPPT")
    private String pkSchappt;

    /** EU_PVTYPE - 用于检查预约 */
	@Field(value="EU_PVTYPE")
    private String euPvtype;

    /** PK_MSP - 对于本系统来说：根据排班类型，确定对应的具体资源。对于第三方HIS系统来说，对应HIS的医技资源编码 */
	@Field(value="PK_MSP")
    private String pkMsp;

    /** PK_CNORD - 排班资源为医技排班时，对于本身系统，对应与医嘱表pk_ord。对于第三方HIS，对应HIS 医嘱编码 */
	@Field(value="PK_CNORD")
    private String pkCnord;

	@Field(value="FLAG_NOTICE")
    private String flagNotice;

    /** FLAG_EXEC - 医技签到或确认时产生 */
	@Field(value="FLAG_EXEC")
    private String flagExec;

	@Field(value="PK_ASSOCC")
    private String pkAssocc;

	@Field(value="PK_EXOCC")
    private String pkExocc;

	@Field(value="FLAG_OCCUPY")
    private String flagOccupy;

	@Field(value="DESC_OCCUPY")
    private String descOccupy;

	@Field(value="MODITY_TIME")
    private Date modityTime;


    public String getPkApptord(){
        return this.pkApptord;
    }
    public void setPkApptord(String pkApptord){
        this.pkApptord = pkApptord;
    }

    public String getPkSchappt(){
        return this.pkSchappt;
    }
    public void setPkSchappt(String pkSchappt){
        this.pkSchappt = pkSchappt;
    }

    public String getEuPvtype(){
        return this.euPvtype;
    }
    public void setEuPvtype(String euPvtype){
        this.euPvtype = euPvtype;
    }

    public String getPkMsp(){
        return this.pkMsp;
    }
    public void setPkMsp(String pkMsp){
        this.pkMsp = pkMsp;
    }

    public String getPkCnord(){
        return this.pkCnord;
    }
    public void setPkCnord(String pkCnord){
        this.pkCnord = pkCnord;
    }

    public String getFlagNotice(){
        return this.flagNotice;
    }
    public void setFlagNotice(String flagNotice){
        this.flagNotice = flagNotice;
    }

    public String getFlagExec(){
        return this.flagExec;
    }
    public void setFlagExec(String flagExec){
        this.flagExec = flagExec;
    }

    public String getPkAssocc(){
        return this.pkAssocc;
    }
    public void setPkAssocc(String pkAssocc){
        this.pkAssocc = pkAssocc;
    }

    public String getPkExocc(){
        return this.pkExocc;
    }
    public void setPkExocc(String pkExocc){
        this.pkExocc = pkExocc;
    }

    public String getFlagOccupy(){
        return this.flagOccupy;
    }
    public void setFlagOccupy(String flagOccupy){
        this.flagOccupy = flagOccupy;
    }

    public String getDescOccupy(){
        return this.descOccupy;
    }
    public void setDescOccupy(String descOccupy){
        this.descOccupy = descOccupy;
    }

    public Date getModityTime(){
        return this.modityTime;
    }
    public void setModityTime(Date modityTime){
        this.modityTime = modityTime;
    }
}