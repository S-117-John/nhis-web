package com.zebone.nhis.common.module.base.bd.drg;

import java.util.Date;

import com.zebone.platform.modules.dao.build.au.Field;
import com.zebone.platform.modules.dao.build.au.Field.FieldType;
import com.zebone.platform.modules.dao.build.au.PK;
import com.zebone.platform.modules.dao.build.au.Table;
import com.zebone.platform.modules.dao.build.au.Field.KeyId;


/**
 * Table: BD_TERM_DCDT - 地区诊断术语 
 *@author ds
 * @since 2019-12-30 09:22:09
 */
@Table(value="BD_TERM_DCDT")
public class BdTermDcdt   {

    /** PK_DCDT - 主键 */
	@PK
	@Field(value="PK_DCDT",id=KeyId.UUID)
    private String pkDcdt;

    /** PK_ORG - 所属机构 */
	@Field(value="pk_org",userfield="pkOrg",userfieldscop=FieldType.INSERT)
    private String pkOrg;

    /** CODE_DIV - 行政区划 */
	@Field(value="CODE_DIV")
    private String codeDiv;

    /** CODE_DCDT - 地区诊断编码 */
	@Field(value="CODE_DCDT")
    private String codeDcdt;

    /** NAME_DCDT - 地区诊断名称 */
	@Field(value="NAME_DCDT")
    private String nameDcdt;

    /** SPCODE - 拼音码 */
	@Field(value="SPCODE")
    private String spcode;

    /** D_CODE - 自定义码 */
	@Field(value="D_CODE")
    private String dCode;

    /** PK_CCDT - 关联ccdt主键 */
	@Field(value="PK_CCDT")
    private String pkCcdt;

    /** CODE_CCDT - ccdt编码 */
	@Field(value="CODE_CCDT")
    private String codeCcdt;

    /** CREATOR - 创建人 */
	@Field(userfield="pkEmp",userfieldscop=FieldType.INSERT)
    private String creator;

    /** CREATE_TIME - 创建时间 */
	@Field(value="CREATE_TIME",date=FieldType.INSERT)
    private Date createTime;

    /** MODIFIER - 修改人 */
	@Field(userfield="pkEmp",userfieldscop=FieldType.ALL)
    private String modifier;

    /** FLAG_DEL - 删除标志 */
	@Field(value="FLAG_DEL")
    private String flagDel;

    /** TS - 时间戳 */
	@Field(date=FieldType.ALL)
    private Date ts;


    public String getPkDcdt(){
        return this.pkDcdt;
    }
    public void setPkDcdt(String pkDcdt){
        this.pkDcdt = pkDcdt;
    }

    public String getPkOrg(){
        return this.pkOrg;
    }
    public void setPkOrg(String pkOrg){
        this.pkOrg = pkOrg;
    }

    public String getCodeDiv(){
        return this.codeDiv;
    }
    public void setCodeDiv(String codeDiv){
        this.codeDiv = codeDiv;
    }

    public String getCodeDcdt(){
        return this.codeDcdt;
    }
    public void setCodeDcdt(String codeDcdt){
        this.codeDcdt = codeDcdt;
    }

    public String getNameDcdt(){
        return this.nameDcdt;
    }
    public void setNameDcdt(String nameDcdt){
        this.nameDcdt = nameDcdt;
    }

    public String getSpcode(){
        return this.spcode;
    }
    public void setSpcode(String spcode){
        this.spcode = spcode;
    }

    public String getdCode(){
        return this.dCode;
    }
    public void setdCode(String dCode){
        this.dCode = dCode;
    }

    public String getPkCcdt(){
        return this.pkCcdt;
    }
    public void setPkCcdt(String pkCcdt){
        this.pkCcdt = pkCcdt;
    }

    public String getCodeCcdt(){
        return this.codeCcdt;
    }
    public void setCodeCcdt(String codeCcdt){
        this.codeCcdt = codeCcdt;
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