package com.zebone.nhis.common.module.base.bd.drg;
import java.util.Date;

import com.zebone.platform.modules.dao.build.au.Field;
import com.zebone.platform.modules.dao.build.au.Field.FieldType;
import com.zebone.platform.modules.dao.build.au.PK;
import com.zebone.platform.modules.dao.build.au.Table;
import com.zebone.platform.modules.dao.build.au.Field.KeyId;


/**
 * Table: BD_CCHI_ITEM - DRG项目关联 
 *@author ds
 * @since 2019-12-31 03:37:03
 */
@Table(value="BD_CCHI_ITEM")
public class BdCchiItem   {

    /** PK_CCHIITEM - 主键 */
	@PK
	@Field(value="PK_CCHIITEM",id=KeyId.UUID)
    private String pkCchiitem;

    /** PK_ORG - 所属机构 */
	@Field(value="pk_org",userfield="pkOrg",userfieldscop=FieldType.INSERT)
    private String pkOrg;

    /** PK_CCHI - 关联cchi主键 */
	@Field(value="PK_CCHI")
    private String pkCchi;

    /** CODE_CCHI - cchi编码 */
	@Field(value="CODE_CCHI")
    private String codeCchi;

    /** PK_ITEM - 关联收费项目主键 */
	@Field(value="PK_ITEM")
    private String pkItem;

    /** CODE_ITEM - 收费项目编码 */
	@Field(value="CODE_ITEM")
    private String codeItem;

    /** PK_DIAG - 关联icd9主键 */
	@Field(value="PK_DIAG")
    private String pkDiag;

    /** CODE_ICD - icd9编码 */
	@Field(value="CODE_ICD")
    private String codeIcd;

    /** PK_ORD - 关联医嘱 */
	@Field(value="PK_ORD")
    private String pkOrd;

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


    public String getPkCchiitem(){
        return this.pkCchiitem;
    }
    public void setPkCchiitem(String pkCchiitem){
        this.pkCchiitem = pkCchiitem;
    }

    public String getPkOrg(){
        return this.pkOrg;
    }
    public void setPkOrg(String pkOrg){
        this.pkOrg = pkOrg;
    }

    public String getPkCchi(){
        return this.pkCchi;
    }
    public void setPkCchi(String pkCchi){
        this.pkCchi = pkCchi;
    }

    public String getCodeCchi(){
        return this.codeCchi;
    }
    public void setCodeCchi(String codeCchi){
        this.codeCchi = codeCchi;
    }

    public String getPkItem(){
        return this.pkItem;
    }
    public void setPkItem(String pkItem){
        this.pkItem = pkItem;
    }

    public String getCodeItem(){
        return this.codeItem;
    }
    public void setCodeItem(String codeItem){
        this.codeItem = codeItem;
    }

    public String getPkDiag(){
        return this.pkDiag;
    }
    public void setPkDiag(String pkDiag){
        this.pkDiag = pkDiag;
    }

    public String getCodeIcd(){
        return this.codeIcd;
    }
    public void setCodeIcd(String codeIcd){
        this.codeIcd = codeIcd;
    }

    public String getPkOrd(){
        return this.pkOrd;
    }
    public void setPkOrd(String pkOrd){
        this.pkOrd = pkOrd;
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