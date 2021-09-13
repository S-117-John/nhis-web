package com.zebone.nhis.common.module.ex.oi;

import java.util.Date;

import com.zebone.nhis.common.module.BaseModule;
import com.zebone.platform.modules.dao.build.au.Field;
import com.zebone.platform.modules.dao.build.au.PK;
import com.zebone.platform.modules.dao.build.au.Table;
import com.zebone.platform.modules.dao.build.au.Field.KeyId;


/**
 * Table: BD_DEPT_IV  - bd_dept_iv 
 *
 * @since 2017-10-16 03:05:40
 */
@Table(value="BD_DEPT_IV")
public class BdDeptIv extends BaseModule  {

	@PK
	@Field(value="PK_DEPTIV",id=KeyId.UUID)
    private String pkDeptiv;

	@Field(value="CODE")
    private String code;

	@Field(value="NAME")
    private String name;

	@Field(value="PK_DEPT")
    private String pkDept;

	@Field(value="FLAG_PRO")
    private String flagPro;

	@Field(value="FLAG_ALERT")
    private String flagAlert;

	@Field(value="NOTE")
    private String note;

	@Field(value="MODIFIER")
    private String modifier;

	@Field(value="MODITY_TIME")
    private Date modityTime;


    public String getPkDeptiv(){
        return this.pkDeptiv;
    }
    public void setPkDeptiv(String pkDeptiv){
        this.pkDeptiv = pkDeptiv;
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

    public String getPkDept(){
        return this.pkDept;
    }
    public void setPkDept(String pkDept){
        this.pkDept = pkDept;
    }

    public String getFlagPro(){
        return this.flagPro;
    }
    public void setFlagPro(String flagPro){
        this.flagPro = flagPro;
    }

    public String getFlagAlert(){
        return this.flagAlert;
    }
    public void setFlagAlert(String flagAlert){
        this.flagAlert = flagAlert;
    }

    public String getNote(){
        return this.note;
    }
    public void setNote(String note){
        this.note = note;
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