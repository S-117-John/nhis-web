package com.zebone.nhis.common.module.base.bd.srv;

import java.util.Date;

import com.zebone.platform.modules.dao.build.au.Field;
import com.zebone.platform.modules.dao.build.au.Field.FieldType;
import com.zebone.platform.modules.dao.build.au.Field.KeyId;
import com.zebone.platform.modules.dao.build.au.PK;
import com.zebone.platform.modules.dao.build.au.Table;


/**
 * Table: BD_ORD_EXCLU_DT 
 *
 * @since 2016-09-09 04:48:33
 */
@Table(value="BD_ORD_EXCLU_DT")
public class BdOrdExcluDt   {

	@PK
	@Field(value="PK_EXCLUDT",id=KeyId.UUID)
    private String pkExcludt;

	@Field(value="PK_EXCLU")
    private String pkExclu;

	@Field(value="PK_ORD")
    private String pkOrd;

	@Field(value="CODE_ORD")
    private String codeOrd;

	@Field(value="NAME_ORD")
    private String nameOrd;

	@Field(userfield="pkEmp",userfieldscop=FieldType.INSERT)
    private String creator;

	@Field(value="CREATE_TIME",date=FieldType.INSERT)
    private Date createTime;

	@Field(userfield="pkEmp",userfieldscop=FieldType.ALL)
    private String modifier;

	@Field(value="DEL_FLAG")
    private String delFlag = "0";

	@Field(date=FieldType.ALL)
    private Date ts;

	@Field(value = "EU_LAST")
	private String euLast; //0 默认末次为0  ，1 按停嘱时间计算末次


    public String getPkExcludt(){
        return this.pkExcludt;
    }
    public void setPkExcludt(String pkExcludt){
        this.pkExcludt = pkExcludt;
    }

    public String getPkExclu(){
        return this.pkExclu;
    }
    public void setPkExclu(String pkExclu){
        this.pkExclu = pkExclu;
    }

    public String getPkOrd(){
        return this.pkOrd;
    }
    public void setPkOrd(String pkOrd){
        this.pkOrd = pkOrd;
    }

    public String getCodeOrd(){
        return this.codeOrd;
    }
    public void setCodeOrd(String codeOrd){
        this.codeOrd = codeOrd;
    }

    public String getNameOrd(){
        return this.nameOrd;
    }
    public void setNameOrd(String nameOrd){
        this.nameOrd = nameOrd;
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

    public String getEuLast() {
        return euLast;
    }

    public void setEuLast(String euLast) {
        this.euLast = euLast;
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