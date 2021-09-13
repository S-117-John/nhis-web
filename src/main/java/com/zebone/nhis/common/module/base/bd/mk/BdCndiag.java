package com.zebone.nhis.common.module.base.bd.mk;

import java.util.Date;
import java.util.List;

import com.zebone.nhis.common.module.BaseModule;
import com.zebone.platform.modules.dao.build.au.Field;
import com.zebone.platform.modules.dao.build.au.PK;
import com.zebone.platform.modules.dao.build.au.Table;
import com.zebone.platform.modules.dao.build.au.Field.KeyId;


/**
 * Table: BD_CNDIAG 
 *
 * @since 2018-12-25 10:06:29
 */
@Table(value="BD_CNDIAG")
public class BdCndiag extends BaseModule  {

	@PK
	@Field(value="PK_CNDIAG",id=KeyId.UUID)
    private String pkCndiag;

	@Field(value="CODE_CD")
    private String codeCd;

	@Field(value="NAME_CD")
    private String nameCd;

	@Field(value="SPCODE")
    private String spcode;

	@Field(value="D_CODE")
    private String dCode;

	@Field(value="DT_CNDIAGTYPE")
    private String dtCndiagtype;

	@Field(value="CODE_ICD")
    private String codeIcd;

	@Field(value="PK_DIAG")
    private String pkDiag;

	@Field(value="CODE_ADD")
    private String codeAdd;

	@Field(value="PK_DIAG_ADD")
    private String pkDiagAdd;

	@Field(value="CODE_ADD2")
    private String codeAdd2;

	@Field(value="PK_DIAG_ADD2")
    private String pkDiagAdd2;

	@Field(value="FLAG_NOMAJ")
    private String flagNomaj;

	@Field(value="NOTE")
    private String note;

	@Field(value="MODIFIER")
    private String modifier;

	@Field(value="MODITY_TIME")
    private Date modityTime;
	
	@Field(value="EU_OICD")
	private String euOicd;
	
	@Field(value="CODE_OICD")
	private String codeOicd;
	
	@Field(value="EU_PICD")
	private String euPicd;
	
	@Field(value="CODE_PICD")
	private String codePicd;
	
	private String diagname;
	
	private String diagaddname;
	
	private String diagadd2name;
	
	private String cndiagtype;
	
	private List<BdCndiagAs> bdCndiagAss;
	
	private List<BdCndiagComp> bdCndiagComps;
	
	private List<BdCndiagComt> bdCndiagComts;
	
    public String getEuOicd() {
		return euOicd;
	}
	public void setEuOicd(String euOicd) {
		this.euOicd = euOicd;
	}
	public String getCodeOicd() {
		return codeOicd;
	}
	public void setCodeOicd(String codeOicd) {
		this.codeOicd = codeOicd;
	}
	public String getEuPicd() {
		return euPicd;
	}
	public void setEuPicd(String euPicd) {
		this.euPicd = euPicd;
	}
	public String getCodePicd() {
		return codePicd;
	}
	public void setCodePicd(String codePicd) {
		this.codePicd = codePicd;
	}
	public String getPkCndiag(){
        return this.pkCndiag;
    }
    public void setPkCndiag(String pkCndiag){
        this.pkCndiag = pkCndiag;
    }

    public String getCodeCd(){
        return this.codeCd;
    }
    public void setCodeCd(String codeCd){
        this.codeCd = codeCd;
    }

    public String getNameCd(){
        return this.nameCd;
    }
    public void setNameCd(String nameCd){
        this.nameCd = nameCd;
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

    public String getDtCndiagtype(){
        return this.dtCndiagtype;
    }
    public void setDtCndiagtype(String dtCndiagtype){
        this.dtCndiagtype = dtCndiagtype;
    }

    public String getCodeIcd(){
        return this.codeIcd;
    }
    public void setCodeIcd(String codeIcd){
        this.codeIcd = codeIcd;
    }

    public String getPkDiag(){
        return this.pkDiag;
    }
    public void setPkDiag(String pkDiag){
        this.pkDiag = pkDiag;
    }

    public String getCodeAdd(){
        return this.codeAdd;
    }
    public void setCodeAdd(String codeAdd){
        this.codeAdd = codeAdd;
    }

    public String getPkDiagAdd(){
        return this.pkDiagAdd;
    }
    public void setPkDiagAdd(String pkDiagAdd){
        this.pkDiagAdd = pkDiagAdd;
    }

    public String getCodeAdd2(){
        return this.codeAdd2;
    }
    public void setCodeAdd2(String codeAdd2){
        this.codeAdd2 = codeAdd2;
    }

    public String getPkDiagAdd2(){
        return this.pkDiagAdd2;
    }
    public void setPkDiagAdd2(String pkDiagAdd2){
        this.pkDiagAdd2 = pkDiagAdd2;
    }

    public String getFlagNomaj(){
        return this.flagNomaj;
    }
    public void setFlagNomaj(String flagNomaj){
        this.flagNomaj = flagNomaj;
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
	public List<BdCndiagAs> getBdCndiagAss() {
		return bdCndiagAss;
	}
	public void setBdCndiagAs(List<BdCndiagAs> bdCndiagAss) {
		this.bdCndiagAss = bdCndiagAss;
	}
	public List<BdCndiagComp> getBdCndiagComps() {
		return bdCndiagComps;
	}
	public void setBdCndiagComps(List<BdCndiagComp> bdCndiagComps) {
		this.bdCndiagComps = bdCndiagComps;
	}
	public List<BdCndiagComt> getBdCndiagComts() {
		return bdCndiagComts;
	}
	public void setBdCndiagComts(List<BdCndiagComt> bdCndiagComts) {
		this.bdCndiagComts = bdCndiagComts;
	}
	public String getDiagname() {
		return diagname;
	}
	public void setDiagname(String diagname) {
		this.diagname = diagname;
	}
	public String getDiagaddname() {
		return diagaddname;
	}
	public void setDiagaddname(String diagaddname) {
		this.diagaddname = diagaddname;
	}
	public String getDiagadd2name() {
		return diagadd2name;
	}
	public void setDiagadd2name(String diagadd2name) {
		this.diagadd2name = diagadd2name;
	}
	public void setBdCndiagAss(List<BdCndiagAs> bdCndiagAss) {
		this.bdCndiagAss = bdCndiagAss;
	}
	public String getCndiagtype() {
		return cndiagtype;
	}
	public void setCndiagtype(String cndiagtype) {
		this.cndiagtype = cndiagtype;
	}
    
}