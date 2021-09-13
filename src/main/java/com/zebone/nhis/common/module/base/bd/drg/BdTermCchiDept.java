package com.zebone.nhis.common.module.base.bd.drg;
import java.util.Date;

import com.zebone.platform.modules.dao.build.au.Field;
import com.zebone.platform.modules.dao.build.au.Field.FieldType;
import com.zebone.platform.modules.dao.build.au.PK;
import com.zebone.platform.modules.dao.build.au.Table;
import com.zebone.platform.modules.dao.build.au.Field.KeyId;


/**
 * Table: BD_TERM_CCHI_DEPT - CCHI科室模板 
 *
 * @since 2020-01-03 09:19:59
 */
@Table(value="BD_TERM_CCHI_DEPT")
public class BdTermCchiDept   {

    /** PK_CCHIDEPT - 主键 */
	@PK
	@Field(value="PK_CCHIDEPT",id=KeyId.UUID)
    private String pkCchidept;

    /** PK_ORG - 所属机构 */
	@Field(value="pk_org",userfield="pkOrg",userfieldscop=FieldType.INSERT)
    private String pkOrg;

    /** PK_DEPT - 科室 */
	@Field(value="PK_DEPT")
    private String pkDept;

    /** EU_CHILD - 类别 */
	@Field(value="EU_CHILD")
    private String euChild;

    /** SORTNO - 分类序号 */
	@Field(value="SORTNO")
    private Integer sortno;

    /** NAME_CATE - 分类名称 */
	@Field(value="NAME_CATE")
    private String nameCate;

    /** PK_CCHI - cchi主键 */
	@Field(value="PK_CCHI")
    private String pkCchi;

    /** CODE_CCHI - cchi编码 */
	@Field(value="CODE_CCHI")
    private String codeCchi;

    /** PK_ITEM - 关联收费项目 */
	@Field(value="PK_ITEM")
    private String pkItem;

    /** PK_ORD - 关联医嘱项目 */
	@Field(value="PK_ORD")
    private String pkOrd;

    /** PK_DIAG - 关联手术项目 */
	@Field(value="PK_DIAG")
    private String pkDiag;

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


    public String getPkCchidept(){
        return this.pkCchidept;
    }
    public void setPkCchidept(String pkCchidept){
        this.pkCchidept = pkCchidept;
    }

    public String getPkOrg(){
        return this.pkOrg;
    }
    public void setPkOrg(String pkOrg){
        this.pkOrg = pkOrg;
    }

    public String getPkDept(){
        return this.pkDept;
    }
    public void setPkDept(String pkDept){
        this.pkDept = pkDept;
    }

    public String getEuChild(){
        return this.euChild;
    }
    public void setEuChild(String euChild){
        this.euChild = euChild;
    }

    public Integer getSortno(){
        return this.sortno;
    }
    public void setSortno(Integer sortno){
        this.sortno = sortno;
    }

    public String getNameCate(){
        return this.nameCate;
    }
    public void setNameCate(String nameCate){
        this.nameCate = nameCate;
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

    public String getPkOrd(){
        return this.pkOrd;
    }
    public void setPkOrd(String pkOrd){
        this.pkOrd = pkOrd;
    }

    public String getPkDiag(){
        return this.pkDiag;
    }
    public void setPkDiag(String pkDiag){
        this.pkDiag = pkDiag;
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