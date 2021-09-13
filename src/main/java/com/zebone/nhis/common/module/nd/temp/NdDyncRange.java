package com.zebone.nhis.common.module.nd.temp;

import java.util.Date;
import java.math.BigDecimal;

import com.zebone.nhis.common.module.BaseModule;
import com.zebone.platform.modules.dao.build.au.Field;
import com.zebone.platform.modules.dao.build.au.PK;
import com.zebone.platform.modules.dao.build.au.Table;
import com.zebone.platform.modules.dao.build.au.Field.KeyId;


/**
 * Table: ND_DYNC_RANGE 
 *
 * @since 2017-07-27 11:09:39
 */
@Table(value="ND_DYNC_RANGE")
public class NdDyncRange extends BaseModule  {

	@PK
	@Field(value="PK_DYNCRANGE",id=KeyId.UUID)
    private String pkDyncrange;

	@Field(value="SORTNO")
    private BigDecimal sortno;

	@Field(value="NAME")
    private String name;

	@Field(value="CODE_DE")
    private String codeDe;
	
	@Field(value="SPCODE")
	private String spcode;

	@Field(value="DT_NDCOLTYPE")
    private String dtNdcoltype;

	@Field(value="NOTE")
    private String note;

	@Field(value="FLAG_STOP")
    private String flagStop;
	
	@Field(value="PK_DEPT")
	private String pkDept;

	@Field(value="MODIFIER")
    private String modifier;

	@Field(value="MODITY_TIME")
    private Date modityTime;
	
	@Field(value="FLAG_ASSESS")
    private String flagAssess;
	
	@Field(value="ASSESS_TMP_CODE")
    private String assessTmpCode;
	
	
	
    public String getSpcode() {
		return spcode;
	}
	public void setSpcode(String spcode) {
		this.spcode = spcode;
	}
	public String getPkDept() {
		return pkDept;
	}
	public void setPkDept(String pkDept) {
		this.pkDept = pkDept;
	}
	public String getPkDyncrange(){
        return this.pkDyncrange;
    }
    public void setPkDyncrange(String pkDyncrange){
        this.pkDyncrange = pkDyncrange;
    }

    public BigDecimal getSortno(){
        return this.sortno;
    }
    public void setSortno(BigDecimal sortno){
        this.sortno = sortno;
    }

    public String getName(){
        return this.name;
    }
    public void setName(String name){
        this.name = name;
    }

    public String getCodeDe(){
        return this.codeDe;
    }
    public void setCodeDe(String codeDe){
        this.codeDe = codeDe;
    }

    public String getDtNdcoltype() {
		return dtNdcoltype;
	}
	public void setDtNdcoltype(String dtNdcoltype) {
		this.dtNdcoltype = dtNdcoltype;
	}
	public String getNote(){
        return this.note;
    }
    public void setNote(String note){
        this.note = note;
    }

    public String getFlagStop(){
        return this.flagStop;
    }
    public void setFlagStop(String flagStop){
        this.flagStop = flagStop;
    }

    public String getModifier(){
        return this.modifier;
    }
    public void setModifier(String modifier){
        this.modifier = modifier;
    }

    public Date getModityTime(){
        return this.modityTime;
    }
    public void setModityTime(Date modityTime){
        this.modityTime = modityTime;
    }
    
    public String getFlagAssess() {
		return flagAssess;
	}
	public void setFlagAssess(String flagAssess) {
		this.flagAssess = flagAssess;
	}
	public String getAssessTmpCode() {
		return assessTmpCode;
	}
	public void setAssessTmpCode(String assessTmpCode) {
		this.assessTmpCode = assessTmpCode;
	}
}