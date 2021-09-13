package com.zebone.nhis.common.module.labor.nis;

import java.util.Date;

import com.zebone.nhis.common.module.BaseModule;
import com.zebone.platform.modules.dao.build.au.Field;
import com.zebone.platform.modules.dao.build.au.Field.KeyId;
import com.zebone.platform.modules.dao.build.au.PK;
import com.zebone.platform.modules.dao.build.au.Table;


/**
 * Table: PV_LABOR_INSTRU - PV_LABOR_INSTRU 
 *
 * @since 2017-05-18 05:52:16
 */
@Table(value="PV_LABOR_INSTRU")
public class PvLaborInstru extends BaseModule  {

	@PK
	@Field(value="PK_INSTRU",id=KeyId.UUID)
    private String pkInstru;

	@Field(value="PK_PV")
    private String pkPv;

	@Field(value="PK_LABORREC")
    private String pkLaborrec;

	@Field(value="NAME_OP")
    private String nameOp;

	@Field(value="DATE_BEGIN_OP")
    private Date dateBeginOp;

	@Field(value="DATE_END_OP")
    private Date dateEndOp;

	@Field(value="DT_ANSI")
    private String dtAnsi;
	
	@Field(value="PK_RECIVE")
    private String pkRecive;
	
	@Field(value="PK_NURSE")
    private String pkNurse;

	@Field(value="FLAG_CHECK")
    private String flagCheck;

	@Field(value="PK_OP")
    private String pkOp;
	
	@Field(value="NOTE")
    private String note;
	
	@Field(value="MODIFY_TIME")
    private Date modifyTime;

	public String getPkRecive() {
		return pkRecive;
	}
	public void setPkRecive(String pkRecive) {
		this.pkRecive = pkRecive;
	}
	public String getPkNurse() {
		return pkNurse;
	}
	public void setPkNurse(String pkNurse) {
		this.pkNurse = pkNurse;
	}
	public String getPkInstru(){
        return this.pkInstru;
    }
    public void setPkInstru(String pkInstru){
        this.pkInstru = pkInstru;
    }

    public String getNote() {
		return note;
	}
	public void setNote(String note) {
		this.note = note;
	}
	public String getPkOrg(){
        return this.pkOrg;
    }
    public void setPkOrg(String pkOrg){
        this.pkOrg = pkOrg;
    }

    public String getPkPv(){
        return this.pkPv;
    }
    public void setPkPv(String pkPv){
        this.pkPv = pkPv;
    }

    public String getPkLaborrec(){
        return this.pkLaborrec;
    }
    public void setPkLaborrec(String pkLaborrec){
        this.pkLaborrec = pkLaborrec;
    }

    public String getNameOp(){
        return this.nameOp;
    }
    public void setNameOp(String nameOp){
        this.nameOp = nameOp;
    }

    public Date getDateBeginOp(){
        return this.dateBeginOp;
    }
    public void setDateBeginOp(Date dateBeginOp){
        this.dateBeginOp = dateBeginOp;
    }

    public Date getDateEndOp(){
        return this.dateEndOp;
    }
    public void setDateEndOp(Date dateEndOp){
        this.dateEndOp = dateEndOp;
    }

    public String getDtAnsi(){
        return this.dtAnsi;
    }
    public void setDtAnsi(String dtAnsi){
        this.dtAnsi = dtAnsi;
    }
    
	public String getFlagCheck() {
		return flagCheck;
	}
	public void setFlagCheck(String flagCheck) {
		this.flagCheck = flagCheck;
	}
	public String getPkOp() {
		return pkOp;
	}
	public void setPkOp(String pkOp) {
		this.pkOp = pkOp;
	}
	public Date getModifyTime(){
        return this.modifyTime;
    }
    public void setModifyTime(Date modifyTime){
        this.modifyTime = modifyTime;
    }

   
}