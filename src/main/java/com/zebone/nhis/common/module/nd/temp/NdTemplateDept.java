package com.zebone.nhis.common.module.nd.temp;

import java.util.Date;

import com.zebone.nhis.common.module.BaseModule;
import com.zebone.platform.modules.dao.build.au.Field;
import com.zebone.platform.modules.dao.build.au.PK;
import com.zebone.platform.modules.dao.build.au.Table;
import com.zebone.platform.modules.dao.build.au.Field.KeyId;


/**
 * Table: ND_TEMPLATE_DEPT 
 *
 * @since 2017-06-07 10:49:36
 */
@Table(value="ND_TEMPLATE_DEPT")
public class NdTemplateDept extends BaseModule  {

	@PK
	@Field(value="PK_TEMPLATEDEPT",id=KeyId.UUID)
    private String pkTemplatedept;

	@Field(value="PK_TEMPLATE")
    private String pkTemplate;

	@Field(value="PK_DEPT")
    private String pkDept;

	@Field(value="MODITY_TIME")
    private Date modityTime;


    public String getPkTemplatedept(){
        return this.pkTemplatedept;
    }
    public void setPkTemplatedept(String pkTemplatedept){
        this.pkTemplatedept = pkTemplatedept;
    }

    public String getPkTemplate(){
        return this.pkTemplate;
    }
    public void setPkTemplate(String pkTemplate){
        this.pkTemplate = pkTemplate;
    }

    public String getPkDept(){
        return this.pkDept;
    }
    public void setPkDept(String pkDept){
        this.pkDept = pkDept;
    }

    public Date getModityTime(){
        return this.modityTime;
    }
    public void setModityTime(Date modityTime){
        this.modityTime = modityTime;
    }
}