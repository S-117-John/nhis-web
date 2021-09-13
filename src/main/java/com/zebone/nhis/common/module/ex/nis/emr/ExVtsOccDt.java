package com.zebone.nhis.common.module.ex.nis.emr;

import java.math.BigDecimal;
import java.util.Date;

import com.zebone.nhis.common.module.BaseModule;
import com.zebone.platform.modules.dao.build.au.Field;
import com.zebone.platform.modules.dao.build.au.PK;
import com.zebone.platform.modules.dao.build.au.Table;
import com.zebone.platform.modules.dao.build.au.Field.KeyId;

/**
 * Table: EX_VTS_OCC_DT - ex_vts_occ_dt 
 *
 * @since 2016-10-10 11:54:37
 */
@Table(value="EX_VTS_OCC_DT")
public class ExVtsOccDt extends BaseModule  {

	@PK
	@Field(value="PK_VTSOCCDT",id=KeyId.UUID)
    private String pkVtsoccdt;

	@Field(value="PK_VTSOCC")
    private String pkVtsocc;

    /** EU_DATESLOT - 0 上午，1 下午 */
	@Field(value="EU_DATESLOT")
    private String euDateslot;

	@Field(value="HOUR_VTS")
    private int hourVts;

    /** EU_TEMPTYPE - 0 口温，1 腋温，2 肛温 */
	@Field(value="EU_TEMPTYPE")
    private String euTemptype;

	@Field(value="VAL_TEMP")
    private BigDecimal valTemp;

	@Field(value="VAL_DROP")
    private BigDecimal valDrop;

	@Field(value="VAL_PULSE")
    private BigDecimal valPulse;

    /** EU_HRTYPE - 0 自然，1 起搏器 */
	@Field(value="EU_HRTYPE")
    private String euHrtype;

	@Field(value="VAL_HR")
    private BigDecimal valHr;

    /** EU_BRTYPE - 0 自然，1 呼吸机 */
	@Field(value="EU_BRTYPE")
    private String euBrtype;

	@Field(value="VAL_BRE")
    private BigDecimal valBre;

	@Field(value="NOTE")
    private String note;

	@Field(value="MODITY_TIME")
    private Date modityTime;

	@Field(value="dt_vtscolltype")
    private String dtVtscolltype;

	@Field(value="FLAG_ADD")
    private String flagAdd;
	
	@Field(value="PK_VTSOCCDT_REL")
    private String pkVtsoccdtRel;
	
	@Field(value="MODIFIER")
	private String modifier;
	
	@Field(value="DATE_VTS")
	private Date dateVts;



    public String getPkVtsoccdt(){
        return this.pkVtsoccdt;
    }
    public void setPkVtsoccdt(String pkVtsoccdt){
        this.pkVtsoccdt = pkVtsoccdt;
    }

    public String getPkVtsocc(){
        return this.pkVtsocc;
    }
    public void setPkVtsocc(String pkVtsocc){
        this.pkVtsocc = pkVtsocc;
    }

    public String getEuDateslot(){
        return this.euDateslot;
    }
    public void setEuDateslot(String euDateslot){
        this.euDateslot = euDateslot;
    }

    public int getHourVts(){
        return this.hourVts;
    }
    public void setHourVts(int hourVts){
        this.hourVts = hourVts;
    }

    public String getEuTemptype(){
        return this.euTemptype;
    }
    public void setEuTemptype(String euTemptype){
        this.euTemptype = euTemptype;
    }

    public BigDecimal getValTemp(){
        return this.valTemp;
    }
    public void setValTemp(BigDecimal valTemp){
        this.valTemp = valTemp;
    }

    public BigDecimal getValDrop(){
        return this.valDrop;
    }
    public void setValDrop(BigDecimal valDrop){
        this.valDrop = valDrop;
    }

    public BigDecimal getValPulse(){
        return this.valPulse;
    }
    public void setValPulse(BigDecimal valPulse){
        this.valPulse = valPulse;
    }

    public String getEuHrtype(){
        return this.euHrtype;
    }
    public void setEuHrtype(String euHrtype){
        this.euHrtype = euHrtype;
    }

    public BigDecimal getValHr(){
        return this.valHr;
    }
    public void setValHr(BigDecimal valHr){
        this.valHr = valHr;
    }

    public String getEuBrtype(){
        return this.euBrtype;
    }
    public void setEuBrtype(String euBrtype){
        this.euBrtype = euBrtype;
    }

    public BigDecimal getValBre(){
        return this.valBre;
    }
    public void setValBre(BigDecimal valBre){
        this.valBre = valBre;
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

    public String getDtVtscolltype(){
        return this.dtVtscolltype;
    }
    public void setDtVtscolltype(String dtVtscolltype){
        this.dtVtscolltype = dtVtscolltype;
    }
	public String getFlagAdd() {
		return flagAdd;
	}
	public void setFlagAdd(String flagAdd) {
		this.flagAdd = flagAdd;
	}
	public String getPkVtsoccdtRel() {
		return pkVtsoccdtRel;
	}
	public void setPkVtsoccdtRel(String pkVtsoccdtRel) {
		this.pkVtsoccdtRel = pkVtsoccdtRel;
	}
	public String getModifier() {
		return modifier;
	}
	public void setModifier(String modifier) {
		this.modifier = modifier;
	}
	public Date getDateVts() {
		return dateVts;
	}
	public void setDateVts(Date dateVts) {
		this.dateVts = dateVts;
	}
	  
}