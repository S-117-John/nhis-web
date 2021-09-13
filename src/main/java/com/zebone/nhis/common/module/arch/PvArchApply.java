package com.zebone.nhis.common.module.arch;

import java.util.Date;

import com.zebone.platform.modules.dao.build.au.Field;
import com.zebone.platform.modules.dao.build.au.Field.FieldType;
import com.zebone.platform.modules.dao.build.au.Field.KeyId;
import com.zebone.platform.modules.dao.build.au.PK;
import com.zebone.platform.modules.dao.build.au.Table;


/**
 * Table: PV_ARCH_APPLY 
 *
 * @since 2018-01-21 10:26:40
 */
@Table(value="PV_ARCH_APPLY")
public class PvArchApply   {

	@PK
	@Field(value="PK_APPLY",id=KeyId.UUID)
    private String pkApply;

	@Field(value="pk_org",userfield="pkOrg",userfieldscop=FieldType.INSERT)
    private String pkOrg;

	@Field(value="PK_ARCHIVE")
    private String pkArchive;

	@Field(value="EU_TYPE")
    private String euType;

	@Field(value="EU_STATUS")
    private String euStatus;

	@Field(value="PK_EMP_AP")
    private String pkEmpAp;

	@Field(value="NAME_EMP_AP")
    private String nameEmpAp;

	@Field(value="DATE_AP")
    private Date dateAp;

	@Field(value="NOTE_AP")
    private String noteAp;

	@Field(value="PK_EMP_RESP")
    private String pkEmpResp;

	@Field(value="NAME_EMP_RESP")
    private String nameEmpResp;

	@Field(value="DATE_RESP")
    private Date dateResp;

	@Field(value="NOTE_RESP")
    private String noteResp;

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

	@Field(value="pk_dept")
    private String pkDept;


    public String getPkApply(){
        return this.pkApply;
    }
    public void setPkApply(String pkApply){
        this.pkApply = pkApply;
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

    public String getEuType(){
        return this.euType;
    }
    public void setEuType(String euType){
        this.euType = euType;
    }

    public String getEuStatus(){
        return this.euStatus;
    }
    public void setEuStatus(String euStatus){
        this.euStatus = euStatus;
    }

    public String getPkEmpAp(){
        return this.pkEmpAp;
    }
    public void setPkEmpAp(String pkEmpAp){
        this.pkEmpAp = pkEmpAp;
    }

    public String getNameEmpAp(){
        return this.nameEmpAp;
    }
    public void setNameEmpAp(String nameEmpAp){
        this.nameEmpAp = nameEmpAp;
    }

    public Date getDateAp(){
        return this.dateAp;
    }
    public void setDateAp(Date dateAp){
        this.dateAp = dateAp;
    }

    public String getNoteAp(){
        return this.noteAp;
    }
    public void setNoteAp(String noteAp){
        this.noteAp = noteAp;
    }

    public String getPkEmpResp(){
        return this.pkEmpResp;
    }
    public void setPkEmpResp(String pkEmpResp){
        this.pkEmpResp = pkEmpResp;
    }

    public String getNameEmpResp(){
        return this.nameEmpResp;
    }
    public void setNameEmpResp(String nameEmpResp){
        this.nameEmpResp = nameEmpResp;
    }

    public Date getDateResp(){
        return this.dateResp;
    }
    public void setDateResp(Date dateResp){
        this.dateResp = dateResp;
    }

    public String getNoteResp(){
        return this.noteResp;
    }
    public void setNoteResp(String noteResp){
        this.noteResp = noteResp;
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

    public String getPkDept(){
        return this.pkDept;
    }
    public void setPkDept(String pkDept){
        this.pkDept = pkDept;
    }
}