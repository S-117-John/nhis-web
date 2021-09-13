package com.zebone.nhis.common.module.base.bd.code;


import com.zebone.nhis.common.module.BaseModule;
import com.zebone.platform.modules.dao.build.au.Field;
import com.zebone.platform.modules.dao.build.au.PK;
import com.zebone.platform.modules.dao.build.au.Table;
import com.zebone.platform.modules.dao.build.au.Field.KeyId;


/**
 * Table: BD_DEFDOCLIST  - bd_defdoclist 
 *
 * @since 2016-08-30 01:07:46
 */
@Table(value="BD_DEFDOCLIST")
public class BdDefdoclist extends BaseModule  {

	@PK
	@Field(value="PK_DEFDOCLIST",id=KeyId.UUID)
    private String pkDefdoclist;

	@Field(value="CODE")
    private String code;

	@Field(value="NAME")
    private String name;

	@Field(value="PY_CODE")
    private String pyCode;

	@Field(value="D_CODE")
    private String dCode;
	
	@Field(value="EU_TYPE")
    private String euType;


    public String getPkDefdoclist(){
        return this.pkDefdoclist;
    }
    public void setPkDefdoclist(String pkDefdoclist){
        this.pkDefdoclist = pkDefdoclist;
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
	public String getEuType() {
		return euType;
	}
	public void setEuType(String euType) {
		this.euType = euType;
	}
    
}