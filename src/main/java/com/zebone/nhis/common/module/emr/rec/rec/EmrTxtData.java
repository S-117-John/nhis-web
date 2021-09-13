package com.zebone.nhis.common.module.emr.rec.rec;

import java.util.Date;
import java.math.BigDecimal;

import com.zebone.nhis.common.module.BaseModule;
import com.zebone.platform.modules.dao.build.au.Field;
import com.zebone.platform.modules.dao.build.au.PK;
import com.zebone.platform.modules.dao.build.au.Table;
import com.zebone.platform.modules.dao.build.au.Field.KeyId;



/**
 * emr_txt_data
 * @author chengjia
 *
 */
@Table(value="emr_txt_data")
public class EmrTxtData {

	@PK
	@Field(value="pk_txt_data",id=KeyId.UUID)
    private String pkTxtData;

	@Field(value="pk_pv")
    private String pkPv;
	
	@Field(value="pk_rec")
    private String pkRec;

	@Field(value="pk_doc")
    private String pkDoc;

	@Field(value="type_code")
    private String typeCode;

	@Field(value="para_code")
    private String paraCode;

	@Field(value="doc_txt")
    private String docTxt;
	
	@Field(value="remark")
    private String remark;
	
	@Field(value="del_flag")
	private String delFlag;
	
	@Field(value="creator")
    private String creator;

	@Field(value="create_time")
    private Date createTime;

	@Field(value="ts")
    private Date ts;

	public String getPkTxtData() {
		return pkTxtData;
	}

	public void setPkTxtData(String pkTxtData) {
		this.pkTxtData = pkTxtData;
	}

	public String getPkPv() {
		return pkPv;
	}

	public void setPkPv(String pkPv) {
		this.pkPv = pkPv;
	}

	public String getPkRec() {
		return pkRec;
	}

	public void setPkRec(String pkRec) {
		this.pkRec = pkRec;
	}

	public String getPkDoc() {
		return pkDoc;
	}

	public void setPkDoc(String pkDoc) {
		this.pkDoc = pkDoc;
	}

	public String getTypeCode() {
		return typeCode;
	}

	public void setTypeCode(String typeCode) {
		this.typeCode = typeCode;
	}

	public String getParaCode() {
		return paraCode;
	}

	public void setParaCode(String paraCode) {
		this.paraCode = paraCode;
	}

	public String getDocTxt() {
		return docTxt;
	}

	public void setDocTxt(String docTxt) {
		this.docTxt = docTxt;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getDelFlag() {
		return delFlag;
	}

	public void setDelFlag(String delFlag) {
		this.delFlag = delFlag;
	}

	public String getCreator() {
		return creator;
	}

	public void setCreator(String creator) {
		this.creator = creator;
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
	
    
}