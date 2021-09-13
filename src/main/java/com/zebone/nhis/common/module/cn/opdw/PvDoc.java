package com.zebone.nhis.common.module.cn.opdw;

import java.util.Date;

import com.zebone.nhis.common.module.BaseModule;
import com.zebone.platform.modules.dao.build.au.Field;
import com.zebone.platform.modules.dao.build.au.Field.KeyId;
import com.zebone.platform.modules.dao.build.au.PK;
import com.zebone.platform.modules.dao.build.au.Table;

@Table(value="PV_DOC")
public class PvDoc extends BaseModule  {

	@PK
	@Field(value="PK_PVDOC",id=KeyId.UUID)
    private String pkPvdoc;

	@Field(value="PK_PI")
    private String pkPi;

	@Field(value="PK_PV")
    private String pkPv;

	@Field(value="DATE_DOC")
    private Date dateDoc;

	@Field(value="PK_EMP_DOC")
    private String pkEmpDoc;

	@Field(value="NAME_EMP_DOC")
    private String nameEmpDoc;

	@Field(value="PK_PVDOCTEMP")
    private String pkPvdoctemp;

	@Field(value="DATA_DOC")
    private byte[] dataDoc;

	@Field(value="MODIFIER")
    private String modifier;

	@Field(value="MODITY_TIME")
    private Date modityTime;

    @Field(value="FLAG_EMR")
    private String flagEmr;

    @Field(value="NAME")
    private String tempName;

    @Field(value="FLAG_PRINT")
    private String flagPrint;

    @Field(value="FLAG_SECRET")
    private String flagSecret;
    
    @Field(value="EU_SECRET")
    private String euSecret;
    
    @Field(value="PK_SECRET")
    private String pkSecret;
    
    @Field(value="FILE_PATH")
    private String filePath;

    @Field(value="DOC_XML")
    private String docXml;
    
    @Field(value="REMARK")
    private String remark;
    
    private byte[] docExpData;
    
    private String dbName;
    
    private String pkPvdocBak;
    
    public String getFlagEmr() {
        return flagEmr;
    }
    public void setFlagEmr(String flagEmr)
    {
        this.flagEmr=flagEmr;
    }
    public String getPkPvdoc(){
        return this.pkPvdoc;
    }
    public void setPkPvdoc(String pkPvdoc){
        this.pkPvdoc = pkPvdoc;
    }

    public String getPkPi(){
        return this.pkPi;
    }
    public void setPkPi(String pkPi){
        this.pkPi = pkPi;
    }

    public String getPkPv(){
        return this.pkPv;
    }
    public void setPkPv(String pkPv){
        this.pkPv = pkPv;
    }

    public Date getDateDoc(){
        return this.dateDoc;
    }
    public void setDateDoc(Date dateDoc){
        this.dateDoc = dateDoc;
    }

    public String getPkEmpDoc(){
        return this.pkEmpDoc;
    }
    public void setPkEmpDoc(String pkEmpDoc){
        this.pkEmpDoc = pkEmpDoc;
    }

    public String getNameEmpDoc(){
        return this.nameEmpDoc;
    }
    public void setNameEmpDoc(String nameEmpDoc){
        this.nameEmpDoc = nameEmpDoc;
    }

    public String getPkPvdoctemp(){
        return this.pkPvdoctemp;
    }
    public void setPkPvdoctemp(String pkPvdoctemp){
        this.pkPvdoctemp = pkPvdoctemp;
    }

    public byte[] getDataDoc(){
        return this.dataDoc;
    }
    public void setDataDoc(byte[] dataDoc){
        this.dataDoc = dataDoc;
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

    public String getTempName() {
        return tempName;
    }

    public void setTempName(String tempName) {
        this.tempName = tempName;
    }

    public String getFlagPrint() {
        return flagPrint;
    }

    public void setFlagPrint(String flagPrint) {
        this.flagPrint = flagPrint;
    }

    public String getFlagSecret() {
        return flagSecret;
    }

    public void setFlagSecret(String flagSecret) {
        this.flagSecret = flagSecret;
    }
	public String getFilePath() {
		return filePath;
	}
	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}
	public byte[] getDocExpData() {
		return docExpData;
	}
	public void setDocExpData(byte[] docExpData) {
		this.docExpData = docExpData;
	}
	public String getEuSecret() {
		return euSecret;
	}
	public void setEuSecret(String euSecret) {
		this.euSecret = euSecret;
	}
	public String getPkSecret() {
		return pkSecret;
	}
	public void setPkSecret(String pkSecret) {
		this.pkSecret = pkSecret;
	}
	public String getDocXml() {
		return docXml;
	}
	public void setDocXml(String docXml) {
		this.docXml = docXml;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public String getDbName() {
		return dbName;
	}
	public void setDbName(String dbName) {
		this.dbName = dbName;
	}
	public String getPkPvdocBak() {
		return pkPvdocBak;
	}
	public void setPkPvdocBak(String pkPvdocBak) {
		this.pkPvdocBak = pkPvdocBak;
	}
    
}
