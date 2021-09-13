package com.zebone.nhis.common.module.labor.nis;

import java.util.Date;

import com.zebone.nhis.common.module.BaseModule;
import com.zebone.platform.modules.dao.build.au.Field;
import com.zebone.platform.modules.dao.build.au.PK;
import com.zebone.platform.modules.dao.build.au.Table;
import com.zebone.platform.modules.dao.build.au.Field.KeyId;


/**
 * Table: PV_LABOR_GRAM 
 *
 * @since 2017-08-08 11:38:32
 */
@Table(value="PV_LABOR_GRAM")
public class PvLaborGram extends BaseModule  {

	@PK
	@Field(value="PK_LABORGRAM",id=KeyId.UUID)
    private String pkLaborgram;

	@Field(value="PK_LABORREC")
    private String pkLaborrec;

	@Field(value="PK_PV")
    private String pkPv;

	@Field(value="DATE_ENTRY")
    private Date dateEntry;
	
	@Field(value="PK_EMP")
    private String pkEmp;
	
	@Field(value="NAME_EMP")
    private String nameEmp;

	@Field(value="VAL_FHR")
    private String valFhr;

	@Field(value="VAL_DFC")
    private String valDfc;

	@Field(value="VAL_SO")
    private String valSo;

	@Field(value="VAL_SBP")
    private String valSbp;

	@Field(value="VAL_DBP")
    private String valDbp;

	@Field(value="VAL_JX")
    private String valJx;

	@Field(value="VAL_CX")
    private String valCx;
	
	@Field(value="NOTE")
    private String note;
	
	@Field(value="DT_OUT_MODE")
    private String dtOutMode;
	
	@Field(value="FLAG_LC")
	private String flagLc;
	
	@Field(value="MODIFY_TIME")
    private Date modifyTime;
	
    public String getFlagLc() {
		return flagLc;
	}
	public void setFlagLc(String flagLc) {
		this.flagLc = flagLc;
	}
	public String getNameEmp() {
		return nameEmp;
	}
	public void setNameEmp(String nameEmp) {
		this.nameEmp = nameEmp;
	}
	public String getDtOutMode() {
		return dtOutMode;
	}
	public void setDtOutMode(String dtOutMode) {
		this.dtOutMode = dtOutMode;
	}
	public String getNote() {
		return note;
	}
	public void setNote(String note) {
		this.note = note;
	}
	public String getPkEmp() {
		return pkEmp;
	}
	public void setPkEmp(String pkEmp) {
		this.pkEmp = pkEmp;
	}
	public String getPkLaborgram(){
        return this.pkLaborgram;
    }
    public void setPkLaborgram(String pkLaborgram){
        this.pkLaborgram = pkLaborgram;
    }

    public String getPkLaborrec(){
        return this.pkLaborrec;
    }
    public void setPkLaborrec(String pkLaborrec){
        this.pkLaborrec = pkLaborrec;
    }

    public String getPkPv(){
        return this.pkPv;
    }
    public void setPkPv(String pkPv){
        this.pkPv = pkPv;
    }

    public Date getDateEntry(){
        return this.dateEntry;
    }
    public void setDateEntry(Date dateEntry){
        this.dateEntry = dateEntry;
    }

    public String getValFhr(){
        return this.valFhr;
    }
    public void setValFhr(String valFhr){
        this.valFhr = valFhr;
    }

    public String getValDfc(){
        return this.valDfc;
    }
    public void setValDfc(String valDfc){
        this.valDfc = valDfc;
    }

    public String getValSo(){
        return this.valSo;
    }
    public void setValSo(String valSo){
        this.valSo = valSo;
    }

    public String getValSbp(){
        return this.valSbp;
    }
    public void setValSbp(String valSbp){
        this.valSbp = valSbp;
    }

    public String getValDbp(){
        return this.valDbp;
    }
    public void setValDbp(String valDbp){
        this.valDbp = valDbp;
    }

    public String getValJx(){
        return this.valJx;
    }
    public void setValJx(String valJx){
        this.valJx = valJx;
    }

    public String getValCx(){
        return this.valCx;
    }
    public void setValCx(String valCx){
        this.valCx = valCx;
    }

    public Date getModifyTime(){
        return this.modifyTime;
    }
    public void setModifyTime(Date modifyTime){
        this.modifyTime = modifyTime;
    }
}