package com.zebone.nhis.common.module.cn.opdw;

import java.util.Date;

import com.zebone.nhis.common.module.BaseModule;
import com.zebone.platform.modules.dao.build.au.Field;
import com.zebone.platform.modules.dao.build.au.PK;
import com.zebone.platform.modules.dao.build.au.Table;
import com.zebone.platform.modules.dao.build.au.Field.KeyId;

/**
 * Table: BD_OPEMR_TEMPCATE  - BD_OPEMR_TEMPCATE 
 *
 * @since 2017-02-17 04:09:53
 */
@Table(value="BD_OPEMR_TEMPCATE")
public class BdOpEmrTempCate extends BaseModule  {

	@PK
	@Field(value="PK_TEMPCATE",id=KeyId.UUID)
    private String pkTempcate;

	@Field(value="CODE")
    private String code;

	@Field(value="NAME")
    private String name;

	@Field(value="PK_FATHER")
    private String pkFather;

    /** EU_RANGE - 0 全院，1科室，2个人 */
	@Field(value="EU_RANGE")
    private String euRange;

	@Field(value="PK_DEPT")
    private String pkDept;

	@Field(value="PK_EMP")
    private String pkEmp;

	@Field(value="MODIFIER")
    private String modifier;

	@Field(value="MODITY_TIME")
    private Date modityTime;


    public String getPkTempcate(){
        return this.pkTempcate;
    }
    public void setPkTempcate(String pkTempcate){
        this.pkTempcate = pkTempcate;
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

    public String getPkFather(){
        return this.pkFather;
    }
    public void setPkFather(String pkFather){
        this.pkFather = pkFather;
    }

    public String getEuRange(){
        return this.euRange;
    }
    public void setEuRange(String euRange){
        this.euRange = euRange;
    }

    public String getPkDept(){
        return this.pkDept;
    }
    public void setPkDept(String pkDept){
        this.pkDept = pkDept;
    }

    public String getPkEmp(){
        return this.pkEmp;
    }
    public void setPkEmp(String pkEmp){
        this.pkEmp = pkEmp;
    }

    public String getModifier(){
        return this.modifier;
    }
    public void setModifier(String modifier){
        this.modifier = modifier;
    }

    public Date getModityTime(){
        return this.modityTime;
    }
    public void setModityTime(Date modityTime){
        this.modityTime = modityTime;
    }
}
