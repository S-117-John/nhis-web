package com.zebone.nhis.common.module.emr.rec.dict;

import com.zebone.nhis.common.module.BaseModule;
import com.zebone.platform.modules.dao.build.au.Field;
import com.zebone.platform.modules.dao.build.au.PK;
import com.zebone.platform.modules.dao.build.au.Table;
import com.zebone.platform.modules.dao.build.au.Field.KeyId;


/**
 * Table: EMR_COMMON_WORDS 
 *
 * @since 2016-09-19 11:10:03
 */
@Table(value="EMR_COMMON_WORDS")
public class EmrCommonWords extends BaseModule  {

	@PK
	@Field(value="PK_CWORD",id=KeyId.UUID)
    private String pkCword;

	@Field(value="NAME")
    private String name;

	@Field(value="EU_LEVEL")
    private Integer euLevel;

	@Field(value="PY_CODE")
    private String pyCode;

	@Field(value="D_CODE")
    private String dCode;

	@Field(value="GB_CODE")
    private String gbCode;

	@Field(value="SORT_NUM")
    private Long sortNum;

	@Field(value="PK_DEPT")
    private String pkDept;

	@Field(value="WORD_TEXT")
    private String wordText;

	@Field(value="REMARK")
    private String remark;

	@Field(value="EU_USED")
    private String euUsed;
	
	@Field(value="DT_TYPE")
    private Integer dtType;
	
	private String codeStatus;

	private String status;
	
	private String nameDept;
	
    public String getPkCword(){
        return this.pkCword;
    }
    public void setPkCword(String pkCword){
        this.pkCword = pkCword;
    }

    public String getName(){
        return this.name;
    }
    public void setName(String name){
        this.name = name;
    }

    public Integer getEuLevel(){
        return this.euLevel;
    }
    public void setEuLevel(Integer euLevel){
        this.euLevel = euLevel;
    }

    public String getPyCode(){
        return this.pyCode;
    }
    public void setPyCode(String pyCode){
        this.pyCode = pyCode;
    }

    public String getdCode(){
        return this.dCode;
    }
    public void setdCode(String dCode){
        this.dCode = dCode;
    }

    public String getGbCode(){
        return this.gbCode;
    }
    public void setGbCode(String gbCode){
        this.gbCode = gbCode;
    }

    public Long getSortNum(){
        return this.sortNum;
    }
    public void setSortNum(Long sortNum){
        this.sortNum = sortNum;
    }

    public String getPkDept(){
        return this.pkDept;
    }
    public void setPkDept(String pkDept){
        this.pkDept = pkDept;
    }

    public String getWordText(){
        return this.wordText;
    }
    public void setWordText(String wordText){
        this.wordText = wordText;
    }

    public String getRemark(){
        return this.remark;
    }
    public void setRemark(String remark){
        this.remark = remark;
    }   
    public String getCodeStatus() {
		return codeStatus;
	}

	public void setCodeStatus(String codeStatus) {
		this.codeStatus = codeStatus;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getEuUsed() {
		return euUsed;
	}
	public void setEuUsed(String euUsed) {
		this.euUsed = euUsed;
	}
	public Integer getDtType() {
		return dtType;
	}
	public void setDtType(Integer dtType) {
		this.dtType = dtType;
	}
	public String getNameDept() {
		return nameDept;
	}
	public void setNameDept(String nameDept) {
		this.nameDept = nameDept;
	}
	
}