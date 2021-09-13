package com.zebone.nhis.common.module.base.bd.code;


import java.util.Date;

import com.zebone.nhis.common.module.BaseModule;
import com.zebone.platform.modules.dao.build.au.Field;
import com.zebone.platform.modules.dao.build.au.Field.KeyId;
import com.zebone.platform.modules.dao.build.au.PK;
import com.zebone.platform.modules.dao.build.au.Table;


/**
 * Table: BD_DEFDOC  - bd_defdoc 
 *
 * @since 2016-08-30 01:07:38
 */
@Table(value="BD_DEFDOC")
public class BdDefdoc extends BaseModule  {

	@PK
	@Field(value="PK_DEFDOC",id=KeyId.UUID)
    private String pkDefdoc;


	@Field(value="CODE_DEFDOCLIST")
    private String codeDefdoclist;

	@Field(value="CODE")
    private String code;

	@Field(value="BA_CODE")
    private String baCode;

	@Field(value="NAME")
    private String name;

	@Field(value="SHORTNAME")
    private String shortname;

	@Field(value="PY_CODE")
    private String pyCode;

	@Field(value="D_CODE")
    private String dCode;

	@Field(value="MEMO")
    private String memo;

	@Field(value="FLAG_DEF")
    private String flagDef;

	@Field(value="CODE_PARENT")
    private String codeParent;

	@Field(value="PK_DEFDOCLIST")
    private String pkDefdoclist;

	@Field(value="SPCODE")
    private String spcode;

	@Field(value="MODITY_TIME")
    private Date modityTime;

	@Field(value="CODE_STD")
    private String codeStd;
	
	@Field(value="NAME_STD")
	private String nameStd;
	
	@Field(value="ATTR_DESC")
	private String attrDesc;
	
	@Field(value="VAL_ATTR")
	private String valAttr;
	
	public String getAttrDesc() {
		return attrDesc;
	}
	public void setAttrDesc(String attrDesc) {
		this.attrDesc = attrDesc;
	}
	public String getValAttr() {
		return valAttr;
	}
	public void setValAttr(String valAttr) {
		this.valAttr = valAttr;
	}
	public String getPkDefdoc(){
        return this.pkDefdoc;
    }
    public void setPkDefdoc(String pkDefdoc){
        this.pkDefdoc = pkDefdoc;
    }

    public String getCodeDefdoclist(){
        return this.codeDefdoclist;
    }
    public void setCodeDefdoclist(String codeDefdoclist){
        this.codeDefdoclist = codeDefdoclist;
    }

    public String getCode(){
        return this.code;
    }
    public void setCode(String code){
        this.code = code;
    }

    public String getBaCode(){
        return this.baCode;
    }
    public void setBaCode(String baCode){
        this.baCode = baCode;
    }

    public String getName(){
        return this.name;
    }
    public void setName(String name){
        this.name = name;
    }

    public String getShortname(){
        return this.shortname;
    }
    public void setShortname(String shortname){
        this.shortname = shortname;
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

    public String getMemo(){
        return this.memo;
    }
    public void setMemo(String memo){
        this.memo = memo;
    }
	public String getFlagDef() {
		return flagDef;
	}
	public void setFlagDef(String flagDef) {
		this.flagDef = flagDef;
	}
	public String getCodeParent() {
		return codeParent;
	}
	public void setCodeParent(String codeParent) {
		this.codeParent = codeParent;
	}
	public String getCodeStd() {
		return codeStd;
	}
	public void setCodeStd(String codeStd) {
		this.codeStd = codeStd;
	}
	public String getPkDefdoclist() {
		return pkDefdoclist;
	}
	public void setPkDefdoclist(String pkDefdoclist) {
		this.pkDefdoclist = pkDefdoclist;
	}
	public String getSpcode() {
		return spcode;
	}
	public void setSpcode(String spcode) {
		this.spcode = spcode;
	}
	public Date getModityTime() {
		return modityTime;
	}
	public void setModityTime(Date modityTime) {
		this.modityTime = modityTime;
	}
	public String getNameStd() {
		return nameStd;
	}
	public void setNameStd(String nameStd) {
		this.nameStd = nameStd;
	}
	
}