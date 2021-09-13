package com.zebone.nhis.common.module.arch;

import java.util.Date;
import java.math.BigDecimal;

import com.zebone.platform.modules.dao.build.au.Field;
import com.zebone.platform.modules.dao.build.au.PK;
import com.zebone.platform.modules.dao.build.au.Table;
import com.zebone.platform.modules.dao.build.au.Field.KeyId;
import com.zebone.platform.modules.dao.build.au.Field.FieldType;


/**
 * Table: PV_ARCH_DOC 
 *
 * @since 2017-04-27 10:55:10
 */
@Table(value="PV_ARCH_DOC")
public class PvArchDoc   {

	@PK
	@Field(value="PK_DOC",id=KeyId.UUID)
    private String pkDoc;

	@Field(value="pk_org",userfield="pkOrg",userfieldscop=FieldType.INSERT)
    private String pkOrg;

	@Field(value="PK_ARCHIVE")
    private String pkArchive;

	@Field(value="PK_DOCTYPE")
    private String pkDoctype;

	@Field(value="NAME_DOC")
    private String nameDoc;

	@Field(value="POSITION")
    private String position;

	@Field(value="PAGES")
    private int  pages;

	@Field(value="SIZE_DOC")
    private double sizeDoc;

	@Field(value="FLAG_UPLOAD")
    private String flagUpload;

	@Field(value="DATE_UPLOAD")
    private Date dateUpload;

	@Field(value="PK_EMP_UPLOAD")
    private String pkEmpUpload;

	@Field(value="NAME_EMP_UPLOAD")
    private String nameEmpUpload;

	@Field(value="FLAG_VALID")
    private String flagValid;

	@Field(userfield="pkEmp",userfieldscop=FieldType.INSERT)
    private String creator;

	@Field(value="CREATE_TIME",date=FieldType.INSERT)
    private Date createTime;

	@Field(userfield="pkEmp",userfieldscop=FieldType.ALL)
    private String modifier;

	@Field(value="MODIFY_TIME")
    private Date modifyTime;

	@Field(value="FLAG_DEL")
    private String flagDel;

	@Field(date=FieldType.ALL)
    private Date ts;
	
	@Field(value="CODE_RPT")
	private String codeRpt;
	
	@Field(value="NAME_RPT")
	private String nameRpt;
	
	@Field(value="DATE_DOC")
    private Date dateDoc;
	
	@Field(value="SORT_NO")
	private int sortNo;
	
	@Field(value="DOC_DATA")
	private byte[] docData;
	
	@Field(value="ISARCH")
	private String isArch;
	
    public String getNameRpt() {
		return nameRpt;
	}
	public void setNameRpt(String nameRpt) {
		this.nameRpt = nameRpt;
	}
	public String getCodeRpt() {
		return codeRpt;
	}
	public void setCodeRpt(String codeRpt) {
		this.codeRpt = codeRpt;
	}
	public String getPkDoc(){
        return this.pkDoc;
    }
    public void setPkDoc(String pkDoc){
        this.pkDoc = pkDoc;
    }

    public String getPkOrg(){
        return this.pkOrg;
    }
    public void setPkOrg(String pkOrg){
        this.pkOrg = pkOrg;
    }

    public String getPkArchive(){
        return this.pkArchive;
    }
    public void setPkArchive(String pkArchive){
        this.pkArchive = pkArchive;
    }

    public String getPkDoctype(){
        return this.pkDoctype;
    }
    public void setPkDoctype(String pkDoctype){
        this.pkDoctype = pkDoctype;
    }

    public String getNameDoc(){
        return this.nameDoc;
    }
    public void setNameDoc(String nameDoc){
        this.nameDoc = nameDoc;
    }



    public String getPosition() {
		return position;
	}
	public void setPosition(String position) {
		this.position = position;
	}
	public int getPages() {
		return pages;
	}
	public void setPages(int pages) {
		this.pages = pages;
	}
	public double getSizeDoc() {
		return sizeDoc;
	}
	public void setSizeDoc(double sizeDoc) {
		this.sizeDoc = sizeDoc;
	}
	public String getFlagUpload(){
        return this.flagUpload;
    }
    public void setFlagUpload(String flagUpload){
        this.flagUpload = flagUpload;
    }

    public Date getDateUpload(){
        return this.dateUpload;
    }
    public void setDateUpload(Date dateUpload){
        this.dateUpload = dateUpload;
    }

    public String getPkEmpUpload(){
        return this.pkEmpUpload;
    }
    public void setPkEmpUpload(String pkEmpUpload){
        this.pkEmpUpload = pkEmpUpload;
    }

    public String getNameEmpUpload(){
        return this.nameEmpUpload;
    }
    public void setNameEmpUpload(String nameEmpUpload){
        this.nameEmpUpload = nameEmpUpload;
    }

    public String getFlagValid(){
        return this.flagValid;
    }
    public void setFlagValid(String flagValid){
        this.flagValid = flagValid;
    }

    public String getCreator(){
        return this.creator;
    }
    public void setCreator(String creator){
        this.creator = creator;
    }

    public Date getCreateTime(){
        return this.createTime;
    }
    public void setCreateTime(Date createTime){
        this.createTime = createTime;
    }

    public String getModifier(){
        return this.modifier;
    }
    public void setModifier(String modifier){
        this.modifier = modifier;
    }

    public Date getModifyTime(){
        return this.modifyTime;
    }
    public void setModifyTime(Date modifyTime){
        this.modifyTime = modifyTime;
    }

    public String getFlagDel(){
        return this.flagDel;
    }
    public void setFlagDel(String flagDel){
        this.flagDel = flagDel;
    }

    public Date getTs(){
        return this.ts;
    }
    public void setTs(Date ts){
        this.ts = ts;
    }
	public int getSortNo() {
		return sortNo;
	}
	public void setSortNo(int sortNo) {
		this.sortNo = sortNo;
	}
	public byte[] getDocData() {
		return docData;
	}
	public void setDocData(byte[] docData) {
		this.docData = docData;
	}
	public String getIsArch() {
		return isArch;
	}
	public void setIsArch(String isArch) {
		this.isArch = isArch;
	}
	public Date getDateDoc() {
		return dateDoc;
	}
	public void setDateDoc(Date dateDoc) {
		this.dateDoc = dateDoc;
	}
    
}