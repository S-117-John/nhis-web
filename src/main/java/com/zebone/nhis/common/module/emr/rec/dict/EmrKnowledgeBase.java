package com.zebone.nhis.common.module.emr.rec.dict;

import com.zebone.nhis.common.module.BaseModule;
import com.zebone.platform.modules.dao.build.au.Field;
import com.zebone.platform.modules.dao.build.au.PK;
import com.zebone.platform.modules.dao.build.au.Table;
import com.zebone.platform.modules.dao.build.au.Field.KeyId;


/**
 * Table: EMR_KNOWLEDGE_BASE 
 *
 * @since 2016-09-13 11:15:37
 */
@Table(value="EMR_KNOWLEDGE_BASE")
public class EmrKnowledgeBase extends BaseModule  {

	@PK
	@Field(value="PK_KNOW_BASE",id=KeyId.UUID)
    private String pkKnowBase;
	
	@Field(value="CODE")
    private String code;

	@Field(value="TYPE_CODE")
    private String typeCode;

	@Field(value="NAME")
    private String name;

	@Field(value="PY_CODE")
    private String pyCode;

	@Field(value="D_CODE")
    private String dCode;

	@Field(value="GB_CODE")
    private String gbCode;

	@Field(value="SORT_NUM")
    private Integer sortNum;

	@Field(value="KB_TEXT")
    private String kbText;

	@Field(value="REMARK")
    private String remark;

	private String status;
	
    public String getPkKnowBase(){
        return this.pkKnowBase;
    }
    public void setPkKnowBase(String pkKnowBase){
        this.pkKnowBase = pkKnowBase;
    }

    public String getCode(){
        return this.code;
    }
    public void setCode(String code){
        this.code = code;
    }

    public String getTypeCode(){
        return this.typeCode;
    }
    public void setTypeCode(String typeCode){
        this.typeCode = typeCode;
    }

    public String getName(){
        return this.name;
    }
    public void setName(String name){
        this.name = name;
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

    public Integer getSortNum(){
        return this.sortNum;
    }
    public void setSortNum(Integer sortNum){
        this.sortNum = sortNum;
    }

    public String getKbText(){
        return this.kbText;
    }
    public void setKbText(String kbText){
        this.kbText = kbText;
    }

    public String getRemark(){
        return this.remark;
    }
    public void setRemark(String remark){
        this.remark = remark;
    }
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
    
}