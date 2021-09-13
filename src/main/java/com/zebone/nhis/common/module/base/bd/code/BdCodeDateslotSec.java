package com.zebone.nhis.common.module.base.bd.code;

import java.util.Date;
import java.math.BigDecimal;

import com.zebone.platform.modules.dao.build.au.Field;
import com.zebone.platform.modules.dao.build.au.PK;
import com.zebone.platform.modules.dao.build.au.Table;
import com.zebone.platform.modules.dao.build.au.Field.FieldType;
import com.zebone.platform.modules.dao.build.au.Field.KeyId;


/**
 * Table: BD_CODE_DATESLOT_SEC  - bd_code_dateslot_sec 
 *
 * @since 2016-08-30 01:07:15
 */
@Table(value="BD_CODE_DATESLOT_SEC")
public class BdCodeDateslotSec   {

	@PK
	@Field(value="PK_DATESLOTSEC",id=KeyId.UUID)
    private String pkDateslotsec;

	@Field(value="PK_ORG",userfield="pkOrg",userfieldscop=FieldType.INSERT)
    private String pkOrg;

	@Field(value="PK_DATESLOT")
    private String pkDateslot;

	@Field(value="SORTNO")
    private Integer sortno;

	@Field(value="NAME_TIME")
    private String nameTime;

	@Field(value="SECMIN")
    private Integer secmin;

	@Field(value="CREATOR",userfield="pkEmp",userfieldscop=FieldType.INSERT)
    private String creator;

	@Field(value="CREATE_TIME",date=FieldType.INSERT)
    private Date createTime;

	@Field(value="TIME_BEGIN")
    private String timeBegin;
	
	@Field(value="TIME_END")
    private String timeEnd;
	
	@Field(value="TS",date=FieldType.ALL)
    private Date ts;

	public String getPkDateslotsec(){
        return this.pkDateslotsec;
    }
    public void setPkDateslotsec(String pkDateslotsec){
        this.pkDateslotsec = pkDateslotsec;
    }

    public String getPkOrg(){
        return this.pkOrg;
    }
    public void setPkOrg(String pkOrg){
        this.pkOrg = pkOrg;
    }

    public String getPkDateslot(){
        return this.pkDateslot;
    }
    public void setPkDateslot(String pkDateslot){
        this.pkDateslot = pkDateslot;
    }

    public Integer getSortno(){
        return this.sortno;
    }
    public void setSortno(Integer sortno){
        this.sortno = sortno;
    }

    public String getNameTime(){
        return this.nameTime;
    }
    public void setNameTime(String nameTime){
        this.nameTime = nameTime;
    }

    public Integer getSecmin(){
        return this.secmin;
    }
    public void setSecmin(Integer secmin){
        this.secmin = secmin;
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
	public String getTimeBegin() {
		return timeBegin;
	}
	public void setTimeBegin(String timeBegin) {
		this.timeBegin = timeBegin;
	}
	public String getTimeEnd() {
		return timeEnd;
	}
	public void setTimeEnd(String timeEnd) {
		this.timeEnd = timeEnd;
	}
    
    
}