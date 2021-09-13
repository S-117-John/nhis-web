package com.zebone.nhis.common.module.pi;

import java.util.Date;

import com.zebone.platform.modules.dao.build.au.Field;
import com.zebone.platform.modules.dao.build.au.PK;
import com.zebone.platform.modules.dao.build.au.Table;
import com.zebone.platform.modules.dao.build.au.Field.FieldType;
import com.zebone.platform.modules.dao.build.au.Field.KeyId;


/**
 * Table: PI_INSURANCE  - 患者信息-保险计划 
 *
 * @since 2016-09-12 10:58:03
 */
@Table(value="PI_INSURANCE")
public class PiInsurance   {

    /** PK_INSURANCE - 保险计划主键 */
	@PK
	@Field(value="PK_INSURANCE",id=KeyId.UUID)
    private String pkInsurance;

    /** PK_PI - 患者主键 */
	@Field(value="PK_PI")
    private String pkPi;

    /** SORT_NO - 序号 */
	@Field(value="SORT_NO")
    private Long sortNo;

    /** PK_HP - 医保计划 */
	@Field(value="PK_HP")
    private String pkHp;

    /** DATE_BEGIN - 有效开始时间 */
	@Field(value="DATE_BEGIN")
    private Date dateBegin;

    /** DATE_END - 有效结束时间 */
	@Field(value="DATE_END")
    private Date dateEnd;

    /** FLAG_DEF - 只能同默认一个 */
	@Field(value="FLAG_DEF")
    private String flagDef;

    /** CREATOR - 创建人 */
	@Field(value="CREATOR",userfield="pkEmp",userfieldscop=FieldType.INSERT)
    private String creator;

    /** CREATE_TIME - 创建时间 */
	@Field(value="CREATE_TIME",date=FieldType.INSERT)
    private Date createTime;

    /** MODIFIER - 修改人 */
	@Field(value="MODIFIER",userfield="pkEmp",userfieldscop=FieldType.ALL)
    private String modifier;

    /** DEL_FLAG - 删除标志 */
	@Field(value="DEL_FLAG")
    private String delFlag;

    /** TS - 时间戳 */
	@Field(value="TS",date=FieldType.ALL)
    private Date ts;


    public String getPkInsurance(){
        return this.pkInsurance;
    }
    public void setPkInsurance(String pkInsurance){
        this.pkInsurance = pkInsurance;
    }

    public String getPkPi(){
        return this.pkPi;
    }
    public void setPkPi(String pkPi){
        this.pkPi = pkPi;
    }

    public Long getSortNo(){
        return this.sortNo;
    }
    public void setSortNo(Long sortNo){
        this.sortNo = sortNo;
    }

    public String getPkHp(){
        return this.pkHp;
    }
    public void setPkHp(String pkHp){
        this.pkHp = pkHp;
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

    public String getFlagDef(){
        return this.flagDef;
    }
    public void setFlagDef(String flagDef){
        this.flagDef = flagDef;
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