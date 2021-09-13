package com.zebone.nhis.common.module.base.bd.srv;

import java.util.Date;

import com.zebone.nhis.common.module.BaseModule;
import com.zebone.platform.modules.dao.build.au.Field;
import com.zebone.platform.modules.dao.build.au.Field.FieldType;
import com.zebone.platform.modules.dao.build.au.Field.KeyId;
import com.zebone.platform.modules.dao.build.au.PK;
import com.zebone.platform.modules.dao.build.au.Table;


/**
 * Table: BD_ORD_EXCLU 
 *
 * @since 2016-09-09 04:48:33
 */
@Table(value="BD_ORD_EXCLU")
public class BdOrdExclu extends BaseModule  {

	@PK
	@Field(value="PK_EXCLU",id=KeyId.UUID)
    private String pkExclu;
	
	@Field(value="eu_excType")
    private String euExcType;

	@Field(value="CODE")
    private String code;

	@Field(value="NAME")
    private String name;

	@Field(value="PY_CODE")
    private String pyCode;

	@Field(value="D_CODE")
    private String dCode;

	@Field(value = "NOTE")
    private String note; //备注


    public String getPkExclu(){
        return this.pkExclu;
    }
    public void setPkExclu(String pkExclu){
        this.pkExclu = pkExclu;
    }

    public String getCode(){
        return this.code;
    }
    public void setCode(String code){
        this.code = code;
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

	public String getEuExcType() {
		return euExcType;
	}
	public void setEuExcType(String euExcType) {
		this.euExcType = euExcType;
	}

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }
}