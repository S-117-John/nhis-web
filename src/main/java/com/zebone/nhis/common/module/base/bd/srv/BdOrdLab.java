package com.zebone.nhis.common.module.base.bd.srv;


import com.zebone.nhis.common.module.BaseModule;
import com.zebone.platform.modules.dao.build.au.Field;
import com.zebone.platform.modules.dao.build.au.Field.KeyId;
import com.zebone.platform.modules.dao.build.au.PK;
import com.zebone.platform.modules.dao.build.au.Table;


/**
 * Table: BD_ORD_LAB  - bd_ord_lab 
 *
 * @since 2016-09-08 02:02:08
 */
@Table(value="BD_ORD_LAB")
public class BdOrdLab extends BaseModule  {

	@PK
	@Field(value="PK_ORDLAB",id=KeyId.UUID)
    private String pkOrdlab;

	@Field(value="PK_ORD")
    private String pkOrd;

	@Field(value="DT_COLLTYPE")
    private String dtColltype;

	@Field(value="DT_LISGROUP")
    private String dtLisgroup;

	@Field(value="DT_CONTYPE")
    private String dtContype;

	@Field(value="NOTE")
    private String note;

	@Field(value="DT_SAMPTYPE")
    private String dtSamptype;

	@Field(value="SPCODE")
    private String spcode;

	@Field(value="D_CODE")
    private String dCode;
	
	@Field(value="DURATION_RPT")
	private String durationRpt;
	
	@Field(value="AMT_SAMP")
	private String amtSamp;
	
	@Field(value="DATESLOT")
	private String dateslot;

    @Field(value="CODE_COMB")
	private String codeComb;

    @Field(value="DT_INSTRUMENT")
    private String dtInstrument;

    @Field(value="FLAG_COMB")
    private String flagComb;

    //是否加收采集方法附加费
    @Field(value="FLAG_ADDCOL")
    private String flagAddcol;
    
    
    public String getFlagAddcol() {
		return flagAddcol;
	}
	public void setFlagAddcol(String flagAddcol) {
		this.flagAddcol = flagAddcol;
	}
	public String getCodeComb() {
        return codeComb;
    }
    public void setCodeComb(String codeComb) {
        this.codeComb = codeComb;
    }
    public String getAmtSamp() {
		return amtSamp;
	}
	public void setAmtSamp(String amtSamp) {
		this.amtSamp = amtSamp;
	}
	public String getDateslot() {
		return dateslot;
	}
	public void setDateslot(String dateslot) {
		this.dateslot = dateslot;
	}
    public String getDurationRpt() {
		return durationRpt;
	}
	public void setDurationRpt(String durationRpt) {
		this.durationRpt = durationRpt;
	}
	public String getPkOrdlab(){
        return this.pkOrdlab;
    }
    public void setPkOrdlab(String pkOrdlab){
        this.pkOrdlab = pkOrdlab;
    }

    public String getPkOrd(){
        return this.pkOrd;
    }
    public void setPkOrd(String pkOrd){
        this.pkOrd = pkOrd;
    }

    public String getDtColltype(){
        return this.dtColltype;
    }
    public void setDtColltype(String dtColltype){
        this.dtColltype = dtColltype;
    }

    public String getDtLisgroup(){
        return this.dtLisgroup;
    }
    public void setDtLisgroup(String dtLisgroup){
        this.dtLisgroup = dtLisgroup;
    }

    public String getDtContype(){
        return this.dtContype;
    }
    public void setDtContype(String dtContype){
        this.dtContype = dtContype;
    }

    public String getNote(){
        return this.note;
    }
    public void setNote(String note){
        this.note = note;
    }

    public String getDtSamptype(){
        return this.dtSamptype;
    }
    public void setDtSamptype(String dtSamptype){
        this.dtSamptype = dtSamptype;
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

    public String getDtInstrument() {
        return dtInstrument;
    }

    public void setDtInstrument(String dtInstrument) {
        this.dtInstrument = dtInstrument;
    }

    public String getFlagComb() {
        return flagComb;
    }

    public void setFlagComb(String flagComb) {
        this.flagComb = flagComb;
    }
}