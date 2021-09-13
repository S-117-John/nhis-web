package com.zebone.nhis.common.module.emr.rec.dict;

import com.zebone.nhis.common.module.BaseModule;
import com.zebone.platform.modules.dao.build.au.Field;
import com.zebone.platform.modules.dao.build.au.PK;
import com.zebone.platform.modules.dao.build.au.Table;
import com.zebone.platform.modules.dao.build.au.Field.KeyId;


/**
 * Table: EMR_SWITCH 
 *
 * @since 2016-10-09 02:40:52
 */
@Table(value="EMR_SWITCH")
public class EmrSwitch extends BaseModule  {

	@PK
	@Field(value="PK_SWITCH",id=KeyId.UUID)
    private String pkSwitch;

	@Field(value="CODE")
    private String code;

	@Field(value="NAME")
    private String name;

	@Field(value="VALUE")
    private String value;

	@Field(value="REMARK")
    private String remark;

	private String status;

    public String getPkSwitch(){
        return this.pkSwitch;
    }
    public void setPkSwitch(String pkSwitch){
        this.pkSwitch = pkSwitch;
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

    public String getValue(){
        return this.value;
    }
    public void setValue(String value){
        this.value = value;
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