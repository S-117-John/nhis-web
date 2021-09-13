package com.zebone.nhis.common.module.base.bd.wf;

import java.util.Date;

import com.zebone.platform.modules.dao.build.au.Field;
import com.zebone.platform.modules.dao.build.au.PK;
import com.zebone.platform.modules.dao.build.au.Table;
import com.zebone.platform.modules.dao.build.au.Field.FieldType;
import com.zebone.platform.modules.dao.build.au.Field.KeyId;


/**
 * Table: BD_WF  - bd_wf 
 *
 * @since 2016-08-30 01:10:08
 */
@Table(value="BD_WF")
public class BdWf   {

	@PK
	@Field(value="PK_WF",id=KeyId.UUID)
    private String pkWf;

	@Field(value="PK_ORG")
    private String pkOrg;

	@Field(value="PK_WFCATE")
    private String pkWfcate;

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

	@Field(value="WFTYPE")
    private String wftype;


    public String getPkWf(){
        return this.pkWf;
    }
    public void setPkWf(String pkWf){
        this.pkWf = pkWf;
    }

    public String getPkOrg(){
        return this.pkOrg;
    }
    public void setPkOrg(String pkOrg){
        this.pkOrg = pkOrg;
    }

    public String getPkWfcate(){
        return this.pkWfcate;
    }
    public void setPkWfcate(String pkWfcate){
        this.pkWfcate = pkWfcate;
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

    public String getWftype(){
        return this.wftype;
    }
    public void setWftype(String wftype){
        this.wftype = wftype;
    }
}