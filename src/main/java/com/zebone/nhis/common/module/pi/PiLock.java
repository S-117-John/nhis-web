package com.zebone.nhis.common.module.pi;

import java.util.Date;

import com.zebone.nhis.common.module.BaseModule;
import com.zebone.platform.modules.dao.build.au.Field;
import com.zebone.platform.modules.dao.build.au.PK;
import com.zebone.platform.modules.dao.build.au.Table;
import com.zebone.platform.modules.dao.build.au.Field.KeyId;


/**
 * Table: PI_LOCK - pi_lock 
 *
 * @since 2016-11-01 09:09:53
 */
@Table(value="PI_LOCK")
public class PiLock extends BaseModule  {

	@PK
	@Field(value="PK_PILOCK",id=KeyId.UUID)
    private String pkPilock;

	@Field(value="PK_PI")
    private String pkPi;

    /** EU_LOCKTYPE - 0 预约挂号 */
	@Field(value="EU_LOCKTYPE")
    private String euLocktype;

	@Field(value="MODITY_TIME")
    private Date modityTime;
	
	/**
	 * 锁定开始时间
	 */
	@Field(value="DATE_BEGIN")
    private Date dateBegin;
	/**
	 * 锁定结束时间
	 */
	@Field(value="DATE_END")
    private Date dateEnd;
	
	/**
	 * 备注
	 */
	@Field(value="NOTE")
    private String note;

	
    public Date getDateBegin() {
		return dateBegin;
	}
	public void setDateBegin(Date dateBegin) {
		this.dateBegin = dateBegin;
	}
	public Date getDateEnd() {
		return dateEnd;
	}
	public void setDateEnd(Date dateEnd) {
		this.dateEnd = dateEnd;
	}
	public String getNote() {
		return note;
	}
	public void setNote(String note) {
		this.note = note;
	}
	public String getPkPilock(){
        return this.pkPilock;
    }
    public void setPkPilock(String pkPilock){
        this.pkPilock = pkPilock;
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

    public Date getModityTime(){
        return this.modityTime;
    }
    public void setModityTime(Date modityTime){
        this.modityTime = modityTime;
    }
}