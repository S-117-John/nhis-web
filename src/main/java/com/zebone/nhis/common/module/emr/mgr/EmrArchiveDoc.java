package com.zebone.nhis.common.module.emr.mgr;


import com.zebone.nhis.common.module.BaseModule;
import com.zebone.platform.modules.dao.build.au.Field;
import com.zebone.platform.modules.dao.build.au.PK;
import com.zebone.platform.modules.dao.build.au.Table;
import com.zebone.platform.modules.dao.build.au.Field.KeyId;


/**
 * Table: EMR_ARCHIVE_DOC 
 *
 * @since 2017-12-11 04:15:27
 */
@Table(value="EMR_ARCHIVE_DOC")
public class EmrArchiveDoc extends BaseModule  {

	@PK
	@Field(value="PK_ARCHIVE_DOC",id=KeyId.UUID)
    private String pkArchiveDoc;

	@Field(value="PK_ARCHIVE")
    private String pkArchive;

    /** PK_DOC - 评分记录ID */
	@Field(value="PK_DOC")
    private String pkDoc;

	@Field(value="PK_PATREC")
    private String pkPatrec;

	@Field(value="DOC_DATA")
    private byte[] docData;

	@Field(value="DOC_XML")
    private String docXml;

	@Field(value="CDA_XML")
    private String cdaXml;


    public String getPkArchiveDoc(){
        return this.pkArchiveDoc;
    }
    public void setPkArchiveDoc(String pkArchiveDoc){
        this.pkArchiveDoc = pkArchiveDoc;
    }

    public String getPkArchive(){
        return this.pkArchive;
    }
    public void setPkArchive(String pkArchive){
        this.pkArchive = pkArchive;
    }

    public String getPkDoc(){
        return this.pkDoc;
    }
    public void setPkDoc(String pkDoc){
        this.pkDoc = pkDoc;
    }

    public String getPkPatrec(){
        return this.pkPatrec;
    }
    public void setPkPatrec(String pkPatrec){
        this.pkPatrec = pkPatrec;
    }

    public byte[] getDocData(){
        return this.docData;
    }
    public void setDocData(byte[] docData){
        this.docData = docData;
    }

    public String getDocXml(){
        return this.docXml;
    }
    public void setDocXml(String docXml){
        this.docXml = docXml;
    }

    public String getCdaXml(){
        return this.cdaXml;
    }
    public void setCdaXml(String cdaXml){
        this.cdaXml = cdaXml;
    }
}