package com.zebone.nhis.common.module.base.bd.mk;

import java.util.Date;

import com.zebone.nhis.common.module.BaseModule;
import com.zebone.platform.modules.dao.build.au.Field;
import com.zebone.platform.modules.dao.build.au.PK;
import com.zebone.platform.modules.dao.build.au.Table;
import com.zebone.platform.modules.dao.build.au.Field.KeyId;


/**
 * Table: BD_CNDIAG_COMT_DT 
 *
 * @since 2018-12-25 10:11:25
 */
@Table(value="BD_CNDIAG_COMT_DT")
public class BdCndiagComtDt extends BaseModule  {

	@PK
	@Field(value="PK_CNDIAGCOMTDT",id=KeyId.UUID)
    private String pkCndiagcomtdt;

	@Field(value="PK_CNDIAGCOMT")
    private String pkCndiagcomt;

	@Field(value="SORTNO")
    private Integer sortno;

	@Field(value="VAL_COMT")
    private String valComt;

	@Field(value="PK_DIAG")
    private String pkDiag;

	@Field(value="CODE_ICD")
    private String codeIcd;

	@Field(value="PK_DIAG_ADD")
    private String pkDiagAdd;

	@Field(value="CODE_ADD")
    private String codeAdd;

	@Field(value="PK_DIAG_ADD2")
    private String pkDiagAdd2;

	@Field(value="CODE_ADD2")
    private String codeAdd2;

	@Field(value="NOTE")
    private String note;

	@Field(value="MODIFIER")
    private String modifier;

	@Field(value="MODITY_TIME")
    private Date modityTime;
	
	@Field(value="CODE_OICD")
	private String codeOicd;
	
	@Field(value="CODE_PICD")
	private String codePicd;
	
	private String diagname;

    public String getCodeOicd() {
		return codeOicd;
	}
	public void setCodeOicd(String codeOicd) {
		this.codeOicd = codeOicd;
	}
	public String getCodePicd() {
		return codePicd;
	}
	public void setCodePicd(String codePicd) {
		this.codePicd = codePicd;
	}
	public String getPkCndiagcomtdt(){
        return this.pkCndiagcomtdt;
    }
    public void setPkCndiagcomtdt(String pkCndiagcomtdt){
        this.pkCndiagcomtdt = pkCndiagcomtdt;
    }

    public String getPkCndiagcomt(){
        return this.pkCndiagcomt;
    }
    public void setPkCndiagcomt(String pkCndiagcomt){
        this.pkCndiagcomt = pkCndiagcomt;
    }

    public Integer getSortno(){
        return this.sortno;
    }
    public void setSortno(Integer sortno){
        this.sortno = sortno;
    }

    public String getValComt(){
        return this.valComt;
    }
    public void setValComt(String valComt){
        this.valComt = valComt;
    }

    public String getPkDiag(){
        return this.pkDiag;
    }
    public void setPkDiag(String pkDiag){
        this.pkDiag = pkDiag;
    }

    public String getCodeIcd(){
        return this.codeIcd;
    }
    public void setCodeIcd(String codeIcd){
        this.codeIcd = codeIcd;
    }

    public String getPkDiagAdd(){
        return this.pkDiagAdd;
    }
    public void setPkDiagAdd(String pkDiagAdd){
        this.pkDiagAdd = pkDiagAdd;
    }

    public String getCodeAdd(){
        return this.codeAdd;
    }
    public void setCodeAdd(String codeAdd){
        this.codeAdd = codeAdd;
    }

    public String getPkDiagAdd2(){
        return this.pkDiagAdd2;
    }
    public void setPkDiagAdd2(String pkDiagAdd2){
        this.pkDiagAdd2 = pkDiagAdd2;
    }

    public String getCodeAdd2(){
        return this.codeAdd2;
    }
    public void setCodeAdd2(String codeAdd2){
        this.codeAdd2 = codeAdd2;
    }

    public String getNote(){
        return this.note;
    }
    public void setNote(String note){
        this.note = note;
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
	public String getDiagname() {
		return diagname;
	}
	public void setDiagname(String diagname) {
		this.diagname = diagname;
	}
    
}