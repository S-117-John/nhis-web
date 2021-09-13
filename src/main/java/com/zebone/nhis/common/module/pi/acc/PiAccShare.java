package com.zebone.nhis.common.module.pi.acc;

import java.util.Date;

import com.zebone.nhis.common.module.BaseModule;
import com.zebone.platform.modules.dao.build.au.Field;
import com.zebone.platform.modules.dao.build.au.Field.FieldType;
import com.zebone.platform.modules.dao.build.au.Field.KeyId;
import com.zebone.platform.modules.dao.build.au.PK;
import com.zebone.platform.modules.dao.build.au.Table;


/**
 * Table: PI_ACC_SHARE  - PI_ACC_SHARE 
 *
 * @since 2016-09-23 04:07:52
 */
@Table(value="PI_ACC_SHARE")
public class PiAccShare extends BaseModule  {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@PK
	@Field(value="PK_ACCSHARE",id=KeyId.UUID)
    private String pkAccshare;

	@Field(value="PK_PIACC")
    private String pkPiacc;

	@Field(value="PK_PI")
    private String pkPi;

    /** PK_PI_USE - 如果此人已有账户，优先使用共享账户 */
	@Field(value="PK_PI_USE")
    private String pkPiUse;

	@Field(value="DATE_OPERA")
    private Date dateOpera;

	@Field(value="PK_EMP_OPERA")
    private String pkEmpOpera;

	@Field(value="NAME_EMP_OPERA")
    private String nameEmpOpera;

	@Field(value="NOTE")
    private String note;

	@Field(userfield="pkEmp",userfieldscop=FieldType.ALL)
    private String modifier;

	@Field(value="MODITY_TIME")
    private Date modityTime;


    public String getPkAccshare(){
        return this.pkAccshare;
    }
    public void setPkAccshare(String pkAccshare){
        this.pkAccshare = pkAccshare;
    }

    public String getPkPiacc(){
        return this.pkPiacc;
    }
    public void setPkPiacc(String pkPiacc){
        this.pkPiacc = pkPiacc;
    }

    public String getPkPi(){
        return this.pkPi;
    }
    public void setPkPi(String pkPi){
        this.pkPi = pkPi;
    }

    public String getPkPiUse(){
        return this.pkPiUse;
    }
    public void setPkPiUse(String pkPiUse){
        this.pkPiUse = pkPiUse;
    }

    public Date getDateOpera(){
        return this.dateOpera;
    }
    public void setDateOpera(Date dateOpera){
        this.dateOpera = dateOpera;
    }

    public String getPkEmpOpera(){
        return this.pkEmpOpera;
    }
    public void setPkEmpOpera(String pkEmpOpera){
        this.pkEmpOpera = pkEmpOpera;
    }

    public String getNameEmpOpera(){
        return this.nameEmpOpera;
    }
    public void setNameEmpOpera(String nameEmpOpera){
        this.nameEmpOpera = nameEmpOpera;
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