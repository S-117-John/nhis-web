package com.zebone.nhis.common.module.pi;

import java.util.Date;

import com.zebone.platform.modules.dao.build.au.Field;
import com.zebone.platform.modules.dao.build.au.PK;
import com.zebone.platform.modules.dao.build.au.Table;
import com.zebone.platform.modules.dao.build.au.Field.FieldType;
import com.zebone.platform.modules.dao.build.au.Field.KeyId;


/**
 * Table: PI_FAMILY  - 患者信息-家庭关系 
 *
 * @since 2016-09-14 01:46:41
 */
@Table(value="PI_FAMILY")
public class PiFamily   {

    /** PK_FAMILY - 亲属主键 */
	@PK
	@Field(value="PK_FAMILY",id=KeyId.UUID)
    private String pkFamily;

    /** PK_PI - 患者主键 */
	@Field(value="PK_PI")
    private String pkPi;

    /** DT_RELATION - 亲属关系 */
	@Field(value="DT_RELATION")
    private String dtRelation;

    /** NAME - 亲属姓名 */
	@Field(value="NAME")
    private String name;

    /** TEL - 联系电话 */
	@Field(value="TEL")
    private String tel;

    /** CREATOR - 创建人 */
	@Field(value="CREATOR",userfield="pkEmp",userfieldscop=FieldType.INSERT)
    private String creator;

    /** CREATE_TIME - 创建时间 */
	@Field(value="CREATE_TIME",date=FieldType.INSERT)
    private Date createTime;

    /** MODIFIER - 修改人 */
	@Field(userfield="pkEmp",userfieldscop=FieldType.ALL)
    private String modifier;

    /** DEL_FLAG - 删除标志*/
	@Field(value="DEL_FLAG")
    private String delFlag;

    /** TS - 时间戳 */
	@Field(value="TS",date=FieldType.ALL)
    private Date ts;


    public String getPkFamily(){
        return this.pkFamily;
    }
    public void setPkFamily(String pkFamily){
        this.pkFamily = pkFamily;
    }

    public String getPkPi(){
        return this.pkPi;
    }
    public void setPkPi(String pkPi){
        this.pkPi = pkPi;
    }

    public String getDtRelation(){
        return this.dtRelation;
    }
    public void setDtRelation(String dtRelation){
        this.dtRelation = dtRelation;
    }

    public String getName(){
        return this.name;
    }
    public void setName(String name){
        this.name = name;
    }

    public String getTel(){
        return this.tel;
    }
    public void setTel(String tel){
        this.tel = tel;
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