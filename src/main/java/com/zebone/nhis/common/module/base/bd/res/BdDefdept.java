package com.zebone.nhis.common.module.base.bd.res;

import java.util.Date;

import com.zebone.nhis.common.module.BaseModule;
import com.zebone.platform.modules.dao.build.au.Field;
import com.zebone.platform.modules.dao.build.au.PK;
import com.zebone.platform.modules.dao.build.au.Table;
import com.zebone.platform.modules.dao.build.au.Field.KeyId;


/**
 * Table: BD_DEFDEPT 
 *
 * @since 2018-12-08 04:57:07
 */
@Table(value="BD_DEFDEPT")
public class BdDefdept extends BaseModule  {

	@PK
	@Field(value="PK_DEFDEPT",id=KeyId.UUID)
    private String pkDefdept;

	@Field(value="DT_DEFDEPTTYPE")
    private String dtDefdepttype;

	@Field(value="SORTNO")
    private Integer sortno;

	@Field(value="CODE_DEPT")
    private String codeDept;

	@Field(value="NAME_DEPT")
    private String nameDept;

	@Field(value="NOTE")
    private String note;

	@Field(value="SPCODE")
    private String spcode;

	@Field(value="D_CODE")
    private String dCode;

	@Field(value="PK_FATHER")
    private String pkFather;

	@Field(value="MODIFIER")
    private String modifier;

	@Field(value="MODIFY_TIME")
    private Date modifyTime;

	
    public String getPkDefdept() {
		return pkDefdept;
	}
	public void setPkDefdept(String pkDefdept) {
		this.pkDefdept = pkDefdept;
	}
	public String getDtDefdepttype(){
        return this.dtDefdepttype;
    }
    public void setDtDefdepttype(String dtDefdepttype){
        this.dtDefdepttype = dtDefdepttype;
    }

    public Integer getSortno(){
        return this.sortno;
    }
    public void setSortno(Integer sortno){
        this.sortno = sortno;
    }

    public String getCodeDept(){
        return this.codeDept;
    }
    public void setCodeDept(String codeDept){
        this.codeDept = codeDept;
    }

    public String getNameDept(){
        return this.nameDept;
    }
    public void setNameDept(String nameDept){
        this.nameDept = nameDept;
    }

    public String getNote(){
        return this.note;
    }
    public void setNote(String note){
        this.note = note;
    }

    public String getSpcode(){
        return this.spcode;
    }
    public void setSpcode(String spcode){
        this.spcode = spcode;
    }

    public String getdCode(){
        return this.dCode;
    }
    public void setdCode(String dCode){
        this.dCode = dCode;
    }

    public String getPkFather(){
        return this.pkFather;
    }
    public void setPkFather(String pkFather){
        this.pkFather = pkFather;
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
}