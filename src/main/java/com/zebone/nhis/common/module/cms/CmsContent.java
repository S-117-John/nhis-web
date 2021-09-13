package com.zebone.nhis.common.module.cms;

import java.util.Date;

import com.zebone.nhis.common.module.BaseModule;
import com.zebone.platform.modules.dao.build.au.Field;
import com.zebone.platform.modules.dao.build.au.PK;
import com.zebone.platform.modules.dao.build.au.Table;
import com.zebone.platform.modules.dao.build.au.Field.KeyId;


/**
 * Table: CMS_CONTENT - cms_content 
 *
 * @since 2016-11-15 02:36:21
 */
@Table(value="CMS_CONTENT")
public class CmsContent extends BaseModule  {

	@PK
	@Field(value="PK_CMSCONT",id=KeyId.UUID)
    private String pkCmscont;

	@Field(value="TITLE")
    private String title;

	@Field(value="TITLE_SUB")
    private String titleSub;

	@Field(value="CONTENT")
    private String content;

	@Field(value="PK_CMSCOL")
    private String pkCmscol;

	@Field(value="PK_CMSCONTCATE")
    private String pkCmscontcate;

    /** DT_CONTTYPE - 00图文，01图片，02视频，03下载 */
	@Field(value="DT_CONTTYPE")
    private String dtConttype;

	@Field(value="PK_EMP_ENTRY")
    private String pkEmpEntry;

	@Field(value="NAME_EMP_ENTRY")
    private String nameEmpEntry;

	@Field(value="DATE_ENTRY")
    private Date dateEntry;

	@Field(value="FLAG_PUB")
    private String flagPub;

	@Field(value="PK_EMP_PUB")
    private String pkEmpPub;

	@Field(value="NAME_EMP_PUB")
    private String nameEmpPub;

	@Field(value="DATE_PUB")
    private Date datePub;

	@Field(value="MODITY_TIME")
    private Date modityTime;
	
	@Field(value="EU_STATUS")
    private String euStatus;
	
	@Field(value="EU_RANGE")
    private String euRange;
	
	@Field(value="SORTNO")
    private Integer sortno;

	public Integer getSortno() {
		return sortno;
	}
	public void setSortno(Integer sortno) {
		this.sortno = sortno;
	}
	public String getEuStatus() {
		return euStatus;
	}
	public void setEuStatus(String euStatus) {
		this.euStatus = euStatus;
	}
	public String getEuRange() {
		return euRange;
	}
	public void setEuRange(String euRange) {
		this.euRange = euRange;
	}
	public String getPkCmscont(){
        return this.pkCmscont;
    }
    public void setPkCmscont(String pkCmscont){
        this.pkCmscont = pkCmscont;
    }

    public String getTitle(){
        return this.title;
    }
    public void setTitle(String title){
        this.title = title;
    }

    public String getTitleSub(){
        return this.titleSub;
    }
    public void setTitleSub(String titleSub){
        this.titleSub = titleSub;
    }

    public String getContent(){
        return this.content;
    }
    public void setContent(String content){
        this.content = content;
    }

    public String getPkCmscol(){
        return this.pkCmscol;
    }
    public void setPkCmscol(String pkCmscol){
        this.pkCmscol = pkCmscol;
    }

    public String getPkCmscontcate(){
        return this.pkCmscontcate;
    }
    public void setPkCmscontcate(String pkCmscontcate){
        this.pkCmscontcate = pkCmscontcate;
    }

    public String getDtConttype(){
        return this.dtConttype;
    }
    public void setDtConttype(String dtConttype){
        this.dtConttype = dtConttype;
    }

    public String getPkEmpEntry(){
        return this.pkEmpEntry;
    }
    public void setPkEmpEntry(String pkEmpEntry){
        this.pkEmpEntry = pkEmpEntry;
    }

    public String getNameEmpEntry(){
        return this.nameEmpEntry;
    }
    public void setNameEmpEntry(String nameEmpEntry){
        this.nameEmpEntry = nameEmpEntry;
    }

    public Date getDateEntry(){
        return this.dateEntry;
    }
    public void setDateEntry(Date dateEntry){
        this.dateEntry = dateEntry;
    }

    public String getFlagPub(){
        return this.flagPub;
    }
    public void setFlagPub(String flagPub){
        this.flagPub = flagPub;
    }

    public String getPkEmpPub(){
        return this.pkEmpPub;
    }
    public void setPkEmpPub(String pkEmpPub){
        this.pkEmpPub = pkEmpPub;
    }

    public String getNameEmpPub(){
        return this.nameEmpPub;
    }
    public void setNameEmpPub(String nameEmpPub){
        this.nameEmpPub = nameEmpPub;
    }

    public Date getDatePub(){
        return this.datePub;
    }
    public void setDatePub(Date datePub){
        this.datePub = datePub;
    }

    public Date getModityTime(){
        return this.modityTime;
    }
    public void setModityTime(Date modityTime){
        this.modityTime = modityTime;
    }
}