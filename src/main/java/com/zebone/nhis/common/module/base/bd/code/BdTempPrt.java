package com.zebone.nhis.common.module.base.bd.code;

import java.util.Date;

import com.zebone.nhis.common.module.BaseModule;
import com.zebone.platform.modules.dao.build.au.Field;
import com.zebone.platform.modules.dao.build.au.Field.FieldType;
import com.zebone.platform.modules.dao.build.au.PK;
import com.zebone.platform.modules.dao.build.au.Table;
import com.zebone.platform.modules.dao.build.au.Field.KeyId;


/**
 * Table: BD_TEMP_PRT  - bd_temp_prt 
 *
 * @since 2018-01-19 09:45:41
 */
@Table(value="BD_TEMP_PRT")
public class BdTempPrt extends BaseModule  {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@PK
	@Field(value="PK_TEMPPRT",id=KeyId.UUID)
	private String pkTempprt;

	@Field(value="PK_ORG")
	private String pkOrg;

	@Field(value="CODE")
	private String code;

	@Field(value="NAME")
	private String name;

	@Field(value="SPCODE")
	private String spcode;

	@Field(value="DATA_RPT")
	private byte[] dataRpt;

	@Field(value="MODITY_TIME",date=FieldType.ALL)
    private Date modityTime;

	@Field(value="PRINTER")
	private String printer;

	@Field(value="FLAG_SYS")
	private String flagSys;

	@Field(value = "DT_PRTTEMP")
	private String dtPrtTemp;

	@Field(value = "DT_ORDEXTYPE")
	private String dtOrdExType;
	
	@Field(value = "NOTE")
	private String note;
	
	@Field(value = "CNT_PRINT")
	private int cntPrint;
	
	public int getCntPrint() {
		return cntPrint;
	}

	public void setCntPrint(int cntPrint) {
		this.cntPrint = cntPrint;
	}

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

	public Date getModityTime(){
		return this.modityTime;
	}
	public void setModityTime(Date modityTime){
		this.modityTime = modityTime;
	}

	public String getPrinter(){
		return this.printer;
	}
	public void setPrinter(String printer){
		this.printer = printer;
	}

	public String getFlagSys(){
		return this.flagSys;
	}
	public void setFlagSys(String flagSys){
		this.flagSys = flagSys;
	}

	public String getDtPrtTemp() {
		return dtPrtTemp;
	}
	public void setDtPrtTemp(String dtPrtTemp) {
		this.dtPrtTemp = dtPrtTemp;
	}
	
	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}

	public String getDtOrdExType() {
		return dtOrdExType;
	}

	public void setDtOrdExType(String dtOrdExType) {
		this.dtOrdExType = dtOrdExType;
	}
}