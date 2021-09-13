package com.zebone.nhis.common.module.base.bd.mk;

import java.util.Date;

import com.zebone.nhis.common.module.BaseModule;
import com.zebone.platform.modules.dao.build.au.Field;
import com.zebone.platform.modules.dao.build.au.PK;
import com.zebone.platform.modules.dao.build.au.Table;
import com.zebone.platform.modules.dao.build.au.Field.KeyId;


/**
 * Table: BD_TEMP_ORDEX 
 *
 * @since 2017-8-29 10:04:41
 */
@Table(value="BD_TEMP_ORDEX")
public class BdTempOrdex extends BaseModule  {

	@PK
	@Field(value="PK_TEMPORDEX",id=KeyId.UUID)
    private String pkTempordex;

	@Field(value="CODE")
    private String code;

	@Field(value="NAME")
    private String name;

	@Field(value="SPCODE")
    private String spcode;

	@Field(value="DATA_TEMP")
    private byte[] dataTemp;

	@Field(value="FLAG_ACTIVE")
    private String flagActive;

	@Field(value="NOTE")
    private String note;

	@Field(value="MODITY_TIME")
    private Date modityTime;

	@Field(value="PRINTER")
    private String printer;

	@Field(value="DT_DEFTEMPTYPE")
    private String dtDeftemptype;

    public String getDtDeftemptype() {
		return dtDeftemptype;
	}
	public void setDtDeftemptype(String dtDeftemptype) {
		this.dtDeftemptype = dtDeftemptype;
	}
	public String getPrinter() {
		return printer;
	}
	public void setPrinter(String printer) {
		this.printer = printer;
	}
	public String getPkTempordex(){
        return this.pkTempordex;
    }
    public void setPkTempordex(String pkTempordex){
        this.pkTempordex = pkTempordex;
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

    public byte[] getDataTemp(){
        return this.dataTemp;
    }
    public void setDataTemp(byte[] dataTemp){
        this.dataTemp = dataTemp;
    }

    public String getFlagActive(){
        return this.flagActive;
    }
    public void setFlagActive(String flagActive){
        this.flagActive = flagActive;
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
}