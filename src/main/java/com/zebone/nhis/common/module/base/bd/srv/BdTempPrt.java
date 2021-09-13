package com.zebone.nhis.common.module.base.bd.srv;

import java.util.Date;

import com.zebone.nhis.common.module.BaseModule;
import com.zebone.platform.modules.dao.build.au.Field;
import com.zebone.platform.modules.dao.build.au.PK;
import com.zebone.platform.modules.dao.build.au.Table;
import com.zebone.platform.modules.dao.build.au.Field.KeyId;


/**
 * Table: BD_TEMP_PRT - bd_temp_prt 
 *
 * @since 2020-07-28 03:04:50
 */
@Table(value="BD_TEMP_PRT")
public class BdTempPrt extends BaseModule  {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6006968424010429978L;

	@PK
	@Field(value="PK_TEMPPRT",id=KeyId.UUID)
    private String pkTempprt;

	@Field(value="CODE")
    private String code;

	@Field(value="NAME")
    private String name;

	@Field(value="SPCODE")
    private String spcode;

	@Field(value="DATA_RPT")
    private byte[] dataRpt;

	@Field(value="MODITY_TIME")
    private Object modityTime;

	@Field(value="PRINTER")
    private String printer;

	@Field(value="END_DATE")
    private Date endDate;

	@Field(value="FLAG_SYS")
    private String flagSys;

	@Field(value="EU_TYPE")
    private String euType;

	@Field(value="DT_PRTTEMP")
    private String dtPrttemp;

	@Field(value="DT_ORDEXTYPE")
    private String dtOrdextype;


    public String getPkTempprt(){
        return this.pkTempprt;
    }
    public void setPkTempprt(String pkTempprt){
        this.pkTempprt = pkTempprt;
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

    public byte[] getDataRpt(){
        return this.dataRpt;
    }
    public void setDataRpt(byte[] dataRpt){
        this.dataRpt = dataRpt;
    }

    public Object getModityTime(){
        return this.modityTime;
    }
    public void setModityTime(Object modityTime){
        this.modityTime = modityTime;
    }

    public String getPrinter(){
        return this.printer;
    }
    public void setPrinter(String printer){
        this.printer = printer;
    }

    public Date getEndDate(){
        return this.endDate;
    }
    public void setEndDate(Date endDate){
        this.endDate = endDate;
    }

    public String getFlagSys(){
        return this.flagSys;
    }
    public void setFlagSys(String flagSys){
        this.flagSys = flagSys;
    }

    public String getEuType(){
        return this.euType;
    }
    public void setEuType(String euType){
        this.euType = euType;
    }

    public String getDtPrttemp(){
        return this.dtPrttemp;
    }
    public void setDtPrttemp(String dtPrttemp){
        this.dtPrttemp = dtPrttemp;
    }

    public String getDtOrdextype(){
        return this.dtOrdextype;
    }
    public void setDtOrdextype(String dtOrdextype){
        this.dtOrdextype = dtOrdextype;
    }
}