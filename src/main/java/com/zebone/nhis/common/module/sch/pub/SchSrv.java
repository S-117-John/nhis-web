package com.zebone.nhis.common.module.sch.pub;

import java.util.Date;

import com.zebone.nhis.common.module.BaseModule;
import com.zebone.platform.modules.dao.build.au.Field;
import com.zebone.platform.modules.dao.build.au.PK;
import com.zebone.platform.modules.dao.build.au.Table;
import com.zebone.platform.modules.dao.build.au.Field.KeyId;


/**
 * Table: SCH_SRV 
 *
 * @since 2016-09-12 09:33:21
 */
@Table(value="SCH_SRV")
public class SchSrv extends BaseModule  {

	@PK
	@Field(value="PK_SCHSRV",id=KeyId.UUID)
    private String pkSchsrv;

	@Field(value="EU_SCHCLASS")
    private String euSchclass;

	@Field(value="CODE")
    private String code;

	@Field(value="NAME")
    private String name;

	@Field(value="SPCODE")
    private String spcode;

	@Field(value="D_CODE")
    private String dCode;

	@Field(value="EU_SRVTYPE")
    private String euSrvtype;

	@Field(value="MODIFY_TIME")
    private Date modifyTime;
	
	@Field(value="NOTE")
    private String note;
	
	@Field(value="FLAG_STOP")
    private String flagStop;
	
	@Field(value="EU_ADDTYPE")
	private String euAddType;
	
	@Field(value="PK_ITEM")
	private String pkItem;
	
	@Field(value="PK_TICKETADDCS")
	private String pkTicketaddcs;
	
	@Field(value="PK_ITEM_SPEC")
	private String pkItemSpec;

    public String getPkItemSpec() {
		return pkItemSpec;
	}
	public void setPkItemSpec(String pkItemSpec) {
		this.pkItemSpec = pkItemSpec;
	}
	public String getPkSchsrv(){
        return this.pkSchsrv;
    }
    public void setPkSchsrv(String pkSchsrv){
        this.pkSchsrv = pkSchsrv;
    }

    public String getEuSchclass(){
        return this.euSchclass;
    }
    public void setEuSchclass(String euSchclass){
        this.euSchclass = euSchclass;
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

    public String getSpcode(){
        return this.spcode;
    }
    public void setSpcode(String spcode){
        this.spcode = spcode;
    }

    public String getdCode(){
        return this.dCode;
    }
    public void setdCode(String dCode){
        this.dCode = dCode;
    }

    public String getEuSrvtype(){
        return this.euSrvtype;
    }
    public void setEuSrvtype(String euSrvtype){
        this.euSrvtype = euSrvtype;
    }

    public Date getModifyTime(){
        return this.modifyTime;
    }
    public void setModifyTime(Date modifyTime){
        this.modifyTime = modifyTime;
    }
	public String getNote() {
		return note;
	}
	public void setNote(String note) {
		this.note = note;
	}
	public String getFlagStop() {
		return flagStop;
	}
	public void setFlagStop(String flagStop) {
		this.flagStop = flagStop;
	}
	public String getEuAddType() {
		return euAddType;
	}
	public void setEuAddType(String euAddType) {
		this.euAddType = euAddType;
	}
	public String getPkItem() {
		return pkItem;
	}
	public void setPkItem(String pkItem) {
		this.pkItem = pkItem;
	}
	public String getPkTicketaddcs() {
		return pkTicketaddcs;
	}
	public void setPkTicketaddcs(String pkTicketaddcs) {
		this.pkTicketaddcs = pkTicketaddcs;
	}
}