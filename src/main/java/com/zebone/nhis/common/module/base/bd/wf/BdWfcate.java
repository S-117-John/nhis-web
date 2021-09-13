package com.zebone.nhis.common.module.base.bd.wf;

import java.util.Date;

import com.zebone.platform.modules.dao.build.au.Field;
import com.zebone.platform.modules.dao.build.au.PK;
import com.zebone.platform.modules.dao.build.au.Table;
import com.zebone.platform.modules.dao.build.au.Field.FieldType;
import com.zebone.platform.modules.dao.build.au.Field.KeyId;


/**
 * Table: BD_WFCATE  - bd_wfcate 
 *
 * @since 2016-08-30 01:10:14
 */
@Table(value="BD_WFCATE")
public class BdWfcate   {

	@PK
	@Field(value="PK_WFCATE",id=KeyId.UUID)
    private String pkWfcate;

	@Field(value="PK_ORG")
    private String pkOrg;

	@Field(value="CODE")
    private String code;

	@Field(value="NAME")
    private String name;

	@Field(userfield="pkEmp",userfieldscop=FieldType.INSERT)
    private String creator;

	@Field(value="CREATE_TIME",date=FieldType.INSERT)
    private Date createTime;

	@Field(date=FieldType.ALL)
    private Date ts;

	@Field(value="D_CODE")
    private String dCode;

	@Field(value="PY_CODE")
    private String pyCode;


    public String getPkWfcate(){
        return this.pkWfcate;
    }
    public void setPkWfcate(String pkWfcate){
        this.pkWfcate = pkWfcate;
    }

    public String getPkOrg(){
        return this.pkOrg;
    }
    public void setPkOrg(String pkOrg){
        this.pkOrg = pkOrg;
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

    public String getCreator(){
        return this.creator;
    }
    public void setCreator(String creator){
        this.creator = creator;
    }

    public Date getCreateTime(){
        return this.createTime;
    }
    public void setCreateTime(Date createTime){
        this.createTime = createTime;
    }

    public Date getTs(){
        return this.ts;
    }
    public void setTs(Date ts){
        this.ts = ts;
    }

    public String getdCode(){
        return this.dCode;
    }
    public void setdCode(String dCode){
        this.dCode = dCode;
    }

    public String getPyCode(){
        return this.pyCode;
    }
    public void setPyCode(String pyCode){
        this.pyCode = pyCode;
    }
}