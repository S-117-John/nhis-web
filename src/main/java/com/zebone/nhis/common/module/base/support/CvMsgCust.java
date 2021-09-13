package com.zebone.nhis.common.module.base.support;

import java.util.Date;

import com.zebone.platform.modules.dao.build.au.Field;
import com.zebone.platform.modules.dao.build.au.Field.FieldType;
import com.zebone.platform.modules.dao.build.au.PK;
import com.zebone.platform.modules.dao.build.au.Table;
import com.zebone.platform.modules.dao.build.au.Field.KeyId;


/**
 * Table: CV_MSG_CUST - CV_MSG_CUST 
 *
 * @since 2017-8-15 16:20:20
 */
@Table(value="CV_MSG_CUST")
public class CvMsgCust   {

	@PK
	@Field(value="PK_MSGCUST",id=KeyId.UUID)
    private String pkMsgcust;

	@Field(value="PK_MSG")
    private String pkMsg;

    /** EU_ROLE - 0 个人，1 医疗组，2 科室 */
	@Field(value="EU_ROLE")
    private String euRole;

	@Field(value="CODE_CUST")
    private String codeCust;

	@Field(value="NAME_CUST")
    private String nameCust;

	@Field(value="NOTE")
    private String note;

	@Field(userfield="pkEmp",userfieldscop=FieldType.INSERT)
    private String creator;

	@Field(value="CREATE_TIME",date=FieldType.INSERT)
    private Date createTime;

	@Field(userfield="pkEmp",userfieldscop=FieldType.ALL)
    private String modifier;

	@Field(value="MODITY_TIME")
    private Date modityTime;

	@Field(value="DEL_FLAG")
    private String delFlag;

	@Field(date=FieldType.ALL)
    private Date ts;


    public String getPkMsgcust(){
        return this.pkMsgcust;
    }
    public void setPkMsgcust(String pkMsgcust){
        this.pkMsgcust = pkMsgcust;
    }

    public String getPkMsg(){
        return this.pkMsg;
    }
    public void setPkMsg(String pkMsg){
        this.pkMsg = pkMsg;
    }

    public String getEuRole(){
        return this.euRole;
    }
    public void setEuRole(String euRole){
        this.euRole = euRole;
    }

    public String getCodeCust(){
        return this.codeCust;
    }
    public void setCodeCust(String codeCust){
        this.codeCust = codeCust;
    }

    public String getNameCust(){
        return this.nameCust;
    }
    public void setNameCust(String nameCust){
        this.nameCust = nameCust;
    }

    public String getNote(){
        return this.note;
    }
    public void setNote(String note){
        this.note = note;
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

    public Date getModityTime(){
        return this.modityTime;
    }
    public void setModityTime(Date modityTime){
        this.modityTime = modityTime;
    }

    public String getDelFlag(){
        return this.delFlag;
    }
    public void setDelFlag(String delFlag){
        this.delFlag = delFlag;
    }

    public Date getTs(){
        return this.ts;
    }
    public void setTs(Date ts){
        this.ts = ts;
    }
}