package com.zebone.nhis.common.module.nd.record;

import java.util.Date;

import com.zebone.nhis.common.module.BaseModule;
import com.zebone.platform.modules.dao.build.au.Field;
import com.zebone.platform.modules.dao.build.au.PK;
import com.zebone.platform.modules.dao.build.au.Table;
import com.zebone.platform.modules.dao.build.au.Field.KeyId;


/**
 * Table: ND_RECORD 
 *
 * @since 2017-06-08 11:29:10
 */
@Table(value="ND_RECORD")
public class NdRecord extends BaseModule  {
	@PK
	@Field(value="PK_RECORD",id=KeyId.UUID)
    private String pkRecord;

	@Field(value="PK_TEMPLATE")
    private String pkTemplate;

	@Field(value="CONTENT")
    private String content;
	
	@Field(value="DOC_DATA")
    private byte[] docData;
	
	@Field(value="DOC_XML")
    private String docXml;

	@Field(value="PK_PV")
    private String pkPv;

	@Field(value="NOTE")
    private String note;

	@Field(value="DATE_BEGIN")
    private Date dateBegin;

	@Field(value="PK_EMP")
    private String pkEmp;

	@Field(value="NAME_EMP")
    private String nameEmp;

	@Field(value="MODITY_TIME")
    private Date modityTime;

	@Field(value="DATE_CHK")
    private Date dateChk;
	
	@Field(value="FLAG_CHK")
    private String flagChk;
	
	@Field(value="PK_EMP_CHK")
    private String pkEmpChk;
	
	@Field(value="NAME_EMP_CHK")
    private String nameEmpChk;
	
	
	public Date getDateChk() {
		return dateChk;
	}
	public void setDateChk(Date dateChk) {
		this.dateChk = dateChk;
	}
	public String getFlagChk() {
		return flagChk;
	}
	public void setFlagChk(String flagChk) {
		this.flagChk = flagChk;
	}
	public String getPkEmpChk() {
		return pkEmpChk;
	}
	public void setPkEmpChk(String pkEmpChk) {
		this.pkEmpChk = pkEmpChk;
	}
	public String getNameEmpChk() {
		return nameEmpChk;
	}
	public void setNameEmpChk(String nameEmpChk) {
		this.nameEmpChk = nameEmpChk;
	}
	public byte[] getDocData() {
		return docData;
	}
	public void setDocData(byte[] docData) {
		this.docData = docData;
	}
	public String getDocXml() {
		return docXml;
	}
	public void setDocXml(String docXml) {
		this.docXml = docXml;
	}
	public String getPkRecord(){
        return this.pkRecord;
    }
    public void setPkRecord(String pkRecord){
        this.pkRecord = pkRecord;
    }

    public String getPkTemplate(){
        return this.pkTemplate;
    }
    public void setPkTemplate(String pkTemplate){
        this.pkTemplate = pkTemplate;
    }

    public String getContent(){
        return this.content;
    }
    public void setContent(String content){
        this.content = content;
    }

    public String getPkPv(){
        return this.pkPv;
    }
    public void setPkPv(String pkPv){
        this.pkPv = pkPv;
    }

    public String getNote(){
        return this.note;
    }
    public void setNote(String note){
        this.note = note;
    }

    public Date getDateBegin(){
        return this.dateBegin;
    }
    public void setDateBegin(Date dateBegin){
        this.dateBegin = dateBegin;
    }

    public String getPkEmp(){
        return this.pkEmp;
    }
    public void setPkEmp(String pkEmp){
        this.pkEmp = pkEmp;
    }

    public String getNameEmp(){
        return this.nameEmp;
    }
    public void setNameEmp(String nameEmp){
        this.nameEmp = nameEmp;
    }

    public Date getModityTime(){
        return this.modityTime;
    }
    public void setModityTime(Date modityTime){
        this.modityTime = modityTime;
    }
}