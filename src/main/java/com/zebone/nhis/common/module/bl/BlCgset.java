package com.zebone.nhis.common.module.bl;

import java.util.Date;

import com.zebone.nhis.common.module.BaseModule;
import com.zebone.platform.modules.dao.build.au.Field;
import com.zebone.platform.modules.dao.build.au.PK;
import com.zebone.platform.modules.dao.build.au.Table;
import com.zebone.platform.modules.dao.build.au.Field.KeyId;


/**
 * Table: BL_CGSET - BL_CGSET 
 *
 * @since 2017-06-29 11:24:35
 */
@Table(value="BL_CGSET")
public class BlCgset extends BaseModule  {

	@PK
	@Field(value="PK_CGSET",id=KeyId.UUID)
    private String pkCgset;

	@Field(value="PK_DEPT")
    private String pkDept;

	@Field(value="CODE")
    private String code;

	@Field(value="NAME")
    private String name;

	@Field(value="FLAG_STOP")
    private String flagStop;

	@Field(value="MODIFY_TIME")
    private Date modifyTime;
	
	@Field(value="spcode")
	private String spcode;
	
	@Field(value="d_code")
	private String dCode;

    @Field(value="EU_TYPE")
    private String euType;

    public String getEuType() {
		return euType;
	}
	public void setEuType(String euType) {
		this.euType = euType;
	}
	public String getPkCgset(){
        return this.pkCgset;
    }
    public void setPkCgset(String pkCgset){
        this.pkCgset = pkCgset;
    }

    public String getPkDept(){
        return this.pkDept;
    }
    public void setPkDept(String pkDept){
        this.pkDept = pkDept;
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

    public String getFlagStop(){
        return this.flagStop;
    }
    public void setFlagStop(String flagStop){
        this.flagStop = flagStop;
    }

    public Date getModifyTime(){
        return this.modifyTime;
    }
    public void setModifyTime(Date modifyTime){
        this.modifyTime = modifyTime;
    }
	public String getdCode() {
		return dCode;
	}
	public void setdCode(String dCode) {
		this.dCode = dCode;
	}
	public String getSpcode() {
		return spcode;
	}
	public void setSpcode(String spcode) {
		this.spcode = spcode;
	}
    
}