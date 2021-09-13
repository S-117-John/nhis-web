package com.zebone.nhis.common.module.arch;

import java.util.Date;

import com.zebone.platform.modules.dao.build.au.Field;
import com.zebone.platform.modules.dao.build.au.PK;
import com.zebone.platform.modules.dao.build.au.Table;
import com.zebone.platform.modules.dao.build.au.Field.KeyId;
import com.zebone.platform.modules.dao.build.au.Field.FieldType;

/**
 * Table: PV_ARCH_COMT 
 *
 * @since 2017-04-27 10:55:56
 */
@Table(value="PV_ARCH_COMT")
public class PvArchComt   {

	@PK
	@Field(value="PK_COMT",id=KeyId.UUID)
    private String pkComt;

	@Field(value="pk_org",userfield="pkOrg",userfieldscop=FieldType.INSERT)
    private String pkOrg;

	@Field(value="PK_ARCHIVE")
    private String pkArchive;

	@Field(value="DATE_BEGIN")
    private Date dateBegin;

	@Field(value="DATE_END")
    private Date dateEnd;

	@Field(value="PK_EMP_COMT")
    private String pkEmpComt;

	@Field(value="NAME_EMP_COMT")
    private String nameEmpComt;

	@Field(value="FLAG_FINISH")
    private String flagFinish;

	@Field(userfield="pkEmp",userfieldscop=FieldType.INSERT)
    private String creator;

	@Field(value="CREATE_TIME",date=FieldType.INSERT)
    private Date createTime;

	@Field(userfield="pkEmp",userfieldscop=FieldType.ALL)
    private String modifier;

	@Field(value="MODIFY_TIME")
    private Date modifyTime;

	@Field(value="FLAG_DEL")
    private String flagDel;

	@Field(date=FieldType.ALL)
    private Date ts;


    public String getPkComt(){
        return this.pkComt;
    }
    public void setPkComt(String pkComt){
        this.pkComt = pkComt;
    }

    public String getPkOrg(){
        return this.pkOrg;
    }
    public void setPkOrg(String pkOrg){
        this.pkOrg = pkOrg;
    }

    public String getPkArchive(){
        return this.pkArchive;
    }
    public void setPkArchive(String pkArchive){
        this.pkArchive = pkArchive;
    }

    public Date getDateBegin(){
        return this.dateBegin;
    }
    public void setDateBegin(Date dateBegin){
        this.dateBegin = dateBegin;
    }

    public Date getDateEnd(){
        return this.dateEnd;
    }
    public void setDateEnd(Date dateEnd){
        this.dateEnd = dateEnd;
    }

    public String getPkEmpComt(){
        return this.pkEmpComt;
    }
    public void setPkEmpComt(String pkEmpComt){
        this.pkEmpComt = pkEmpComt;
    }

    public String getNameEmpComt(){
        return this.nameEmpComt;
    }
    public void setNameEmpComt(String nameEmpComt){
        this.nameEmpComt = nameEmpComt;
    }

    public String getFlagFinish(){
        return this.flagFinish;
    }
    public void setFlagFinish(String flagFinish){
        this.flagFinish = flagFinish;
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

    public String getModifier(){
        return this.modifier;
    }
    public void setModifier(String modifier){
        this.modifier = modifier;
    }

    public Date getModifyTime(){
        return this.modifyTime;
    }
    public void setModifyTime(Date modifyTime){
        this.modifyTime = modifyTime;
    }

    public String getFlagDel(){
        return this.flagDel;
    }
    public void setFlagDel(String flagDel){
        this.flagDel = flagDel;
    }

    public Date getTs(){
        return this.ts;
    }
    public void setTs(Date ts){
        this.ts = ts;
    }
}