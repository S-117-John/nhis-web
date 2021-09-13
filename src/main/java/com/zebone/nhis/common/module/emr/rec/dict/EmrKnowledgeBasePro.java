package com.zebone.nhis.common.module.emr.rec.dict;

import com.zebone.nhis.common.module.BaseModule;
import com.zebone.platform.modules.dao.build.au.Field;
import com.zebone.platform.modules.dao.build.au.PK;
import com.zebone.platform.modules.dao.build.au.Table;
import com.zebone.platform.modules.dao.build.au.Field.KeyId;


/**
 * Table: EMR_KNOWLEDGE_BASE_PRO 
 *
 */
@Table(value="EMR_KNOWLEDGE_BASE_PRO")
public class EmrKnowledgeBasePro extends BaseModule  {

	@PK
	@Field(value="PK_KB_PRO",id=KeyId.UUID)
    private String pkkbPro;
	
	@Field(value="PK_ORG")
    private String pkOrg;
	
	@Field(value="TYPE_CODE")
    private String typeCode;

	@Field(value="CODE")
    private String code;

	@Field(value="CODE_PARENT")
    private String codeParent;
	
	@Field(value="NAME")
    private String name;

	@Field(value="PY_CODE")
    private String pyCode;

	@Field(value="D_CODE")
    private String dCode;
	
	@Field(value="SORT_NUM")
    private Integer sortNum;

	@Field(value="KB_TEXT")
    private String kbText;

	@Field(value="DEL_FLAG")
    private String delFlag;
	
	@Field(value="REMARK")
    private String remark;

	private String status;

	public String getPkkbPro() {
		return pkkbPro;
	}

	public void setPkkbPro(String pkkbPro) {
		this.pkkbPro = pkkbPro;
	}

	public String getPkOrg() {
		return pkOrg;
	}

	public void setPkOrg(String pkOrg) {
		this.pkOrg = pkOrg;
	}

	public String getTypeCode() {
		return typeCode;
	}

	public void setTypeCode(String typeCode) {
		this.typeCode = typeCode;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getCodeParent() {
		return codeParent;
	}

	public void setCodeParent(String codeParent) {
		this.codeParent = codeParent;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPyCode() {
		return pyCode;
	}

	public void setPyCode(String pyCode) {
		this.pyCode = pyCode;
	}

	public String getdCode() {
		return dCode;
	}

	public void setdCode(String dCode) {
		this.dCode = dCode;
	}

	public Integer getSortNum() {
		return sortNum;
	}

	public void setSortNum(Integer sortNum) {
		this.sortNum = sortNum;
	}

	public String getKbText() {
		return kbText;
	}

	public void setKbText(String kbText) {
		this.kbText = kbText;
	}

	public String getDelFlag() {
		return delFlag;
	}

	public void setDelFlag(String delFlag) {
		this.delFlag = delFlag;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
	
}