package com.zebone.nhis.common.module.pi;

import java.util.Date;

import com.zebone.nhis.common.module.BaseModule;
import com.zebone.platform.modules.dao.build.au.Field;
import com.zebone.platform.modules.dao.build.au.PK;
import com.zebone.platform.modules.dao.build.au.Table;
import com.zebone.platform.modules.dao.build.au.Field.KeyId;


/**
 * Table: PI_LOCK_DT - pi_lock_dt 
 *
 * @since 2016-11-01 08:58:16
 */
@Table(value="PI_LOCK_DT")
public class PiLockDt extends BaseModule  {

	@PK
	@Field(value="PK_PILOCKDT",id=KeyId.UUID)
    private String pkPilockdt;

	@Field(value="PK_PI")
    private String pkPi;

    /** EU_LOCKTYPE - 0 预约挂号 */
	@Field(value="EU_LOCKTYPE")
    private String euLocktype;

    /** EU_DIRECT - 1 加锁 -1 解锁 */
	@Field(value="EU_DIRECT")
    private String euDirect;

	@Field(value="DATE_LOCK")
    private Date dateLock;

	@Field(value="PK_EMP_OPERA")
    private String pkEmpOpera;

	@Field(value="NAME_EMP_OPERA")
    private String nameEmpOpera;

	@Field(value="NOTE")
    private String note;

	@Field(value="MODITY_TIME")
    private Date modityTime;

	@Field(value="DATE_EFFECT")
    private Date dateEffect;
	
	@Field(value="pk_emp_effect")
    private String pkEmpEffect;
	
	@Field(value="flag_effect")
    private String flagEffect;
	
    public String getPkPilockdt(){
        return this.pkPilockdt;
    }
    public void setPkPilockdt(String pkPilockdt){
        this.pkPilockdt = pkPilockdt;
    }

    public String getPkPi(){
        return this.pkPi;
    }
    public void setPkPi(String pkPi){
        this.pkPi = pkPi;
    }

    public String getEuLocktype(){
        return this.euLocktype;
    }
    public void setEuLocktype(String euLocktype){
        this.euLocktype = euLocktype;
    }

    public String getEuDirect(){
        return this.euDirect;
    }
    public void setEuDirect(String euDirect){
        this.euDirect = euDirect;
    }

    public Date getDateLock(){
        return this.dateLock;
    }
    public void setDateLock(Date dateLock){
        this.dateLock = dateLock;
    }

    public String getPkEmpOpera(){
        return this.pkEmpOpera;
    }
    public void setPkEmpOpera(String pkEmpOpera){
        this.pkEmpOpera = pkEmpOpera;
    }

    public String getNameEmpOpera(){
        return this.nameEmpOpera;
    }
    public void setNameEmpOpera(String nameEmpOpera){
        this.nameEmpOpera = nameEmpOpera;
    }

    public String getNote(){
        return this.note;
    }
    public void setNote(String note){
        this.note = note;
    }

    public Date getModityTime(){
        return this.modityTime;
    }
    public void setModityTime(Date modityTime){
        this.modityTime = modityTime;
    }
	public Date getDateEffect() {
		return dateEffect;
	}
	public void setDateEffect(Date dateEffect) {
		this.dateEffect = dateEffect;
	}
	public String getPkEmpEffect() {
		return pkEmpEffect;
	}
	public void setPkEmpEffect(String pkEmpEffect) {
		this.pkEmpEffect = pkEmpEffect;
	}
	public String getFlagEffect() {
		return flagEffect;
	}
	public void setFlagEffect(String flagEffect) {
		this.flagEffect = flagEffect;
	}
}