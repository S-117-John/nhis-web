package com.zebone.nhis.task.emr.vo;

import com.zebone.platform.modules.dao.build.au.Field;
import com.zebone.platform.modules.dao.build.au.Field.KeyId;
import com.zebone.platform.modules.dao.build.au.PK;
import com.zebone.platform.modules.dao.build.au.Table;

import java.util.Date;

@Table(value="EMR_LUCENE_INFO")
public class EmrLuceneInfo {
	@PK
	@Field(value="PK_LUCENE",id=KeyId.UUID)
    private String pkLucene;
	
	@Field(value="PK_REC")
    private String pkRec;

	@Field(value="PK_TMP")
    private String pkTmp;

	@Field(value="DOC_NAME")
    private String docName;

	@Field(value="CREATE_TIME")
    private Date createTime;

	@Field(value="TS")
    private Date ts;

	@Field(value="DEL_FLAG")
    private String delFlag;



	public String getPkLucene() {
		return pkLucene;
	}

	public void setPkLucene(String pkLucene) {
		this.pkLucene = pkLucene;
	}

	public String getPkRec() {
		return pkRec;
	}

	public void setPkRec(String pkRec) {
		this.pkRec = pkRec;
	}

	public String getPkTmp() {
		return pkTmp;
	}

	public void setPkTmp(String pkTmp) {
		this.pkTmp = pkTmp;
	}

	public String getDocName() {
		return docName;
	}

	public void setDocName(String docName) {
		this.docName = docName;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public Date getTs() {
		return ts;
	}

	public void setTs(Date ts) {
		this.ts = ts;
	}

	public String getDelFlag() {
		return delFlag;
	}

	public void setDelFlag(String delFlag) {
		this.delFlag = delFlag;
	}

    
}