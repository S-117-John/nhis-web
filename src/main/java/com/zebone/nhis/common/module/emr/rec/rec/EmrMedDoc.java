package com.zebone.nhis.common.module.emr.rec.rec;

import java.util.Date;

/**
 * 患者病历记录
 * @author chengjia
 *
 */
public class EmrMedDoc{
    /**
     * 
     */
    private String pkDoc;
    /**
     * 
     */
    private String pkOrg;
    /**
     * 
     */
    private byte[] docData;
    /**
     * 
     */
    private String docXml;
    /**
     * 
     */
    private byte[] docDataBak;
    /**
     * 
     */
    private String docXmlBak;
    /**
     * 
     */
    private String delFlag;
    /**
     * 
     */
    private String creator;
    /**
     * 
     */
    private Date createTime;
    /**
     * 
     */
    private Date ts;

    private String status;
    
    private byte[] docExpData;
  
    private String fileName;
    
    private String filePath;
    
    private String fileType;
    
    private String dbName;
    
    public String getPkDoc() {
		return pkDoc;
	}

	public void setPkDoc(String pkDoc) {
		this.pkDoc = pkDoc;
	}

	/**
     * 
     */
    public String getPkOrg(){
        return this.pkOrg;
    }

    /**
     * 
     */
    public void setPkOrg(String pkOrg){
        this.pkOrg = pkOrg;
    }    
    /**
     * 
     */
    public byte[] getDocData(){
        return this.docData;
    }

    /**
     * 
     */
    public void setDocData(byte[] docData){
        this.docData = docData;
    }    
    /**
     * 
     */
    public String getDocXml(){
        return this.docXml;
    }

    /**
     * 
     */
    public void setDocXml(String docXml){
        this.docXml = docXml;
    }    
    /**
     * 
     */
    public byte[] getDocDataBak(){
        return this.docDataBak;
    }

    /**
     * 
     */
    public void setDocDataBak(byte[] docDataBak){
        this.docDataBak = docDataBak;
    }    
    /**
     * 
     */
    public String getDocXmlBak(){
        return this.docXmlBak;
    }

    /**
     * 
     */
    public void setDocXmlBak(String docXmlBak){
        this.docXmlBak = docXmlBak;
    }    
    /**
     * 
     */
    public String getDelFlag(){
        return this.delFlag;
    }

    /**
     * 
     */
    public void setDelFlag(String delFlag){
        this.delFlag = delFlag;
    }    
    /**
     * 
     */
    public String getCreator(){
        return this.creator;
    }

    /**
     * 
     */
    public void setCreator(String creator){
        this.creator = creator;
    }    
    /**
     * 
     */
    public Date getCreateTime(){
        return this.createTime;
    }

    /**
     * 
     */
    public void setCreateTime(Date createTime){
        this.createTime = createTime;
    }    
    /**
     * 
     */
    public Date getTs(){
        return this.ts;
    }

    /**
     * 
     */
    public void setTs(Date ts){
        this.ts = ts;
    }

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public byte[] getDocExpData() {
		return docExpData;
	}

	public void setDocExpData(byte[] docExpData) {
		this.docExpData = docExpData;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getFilePath() {
		return filePath;
	}

	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}

	public String getFileType() {
		return fileType;
	}

	public void setFileType(String fileType) {
		this.fileType = fileType;
	}

	public String getDbName() {
		return dbName;
	}

	public void setDbName(String dbName) {
		this.dbName = dbName;
	}    
    
}