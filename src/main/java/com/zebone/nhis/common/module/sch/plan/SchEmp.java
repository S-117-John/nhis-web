package com.zebone.nhis.common.module.sch.plan;

import java.util.Date;

import com.zebone.nhis.common.module.BaseModule;
import com.zebone.platform.modules.dao.build.au.Field;
import com.zebone.platform.modules.dao.build.au.PK;
import com.zebone.platform.modules.dao.build.au.Table;
import com.zebone.platform.modules.dao.build.au.Field.FieldType;
import com.zebone.platform.modules.dao.build.au.Field.KeyId;


/**
 * Table: SCH_EMP  - sch_emp 
 *
 * @since 2016-09-23 09:14:38
 */
@Table(value="SCH_EMP")
public class SchEmp extends BaseModule  {

	@PK
	@Field(value="PK_SCHEMP",id=KeyId.UUID)
    private String pkSchemp;

	@Field(value="PK_SCH")
    private String pkSch;

	@Field(value="PK_EMP")
    private String pkEmp;

	@Field(value="NAME_EMP")
    private String nameEmp;

	@Field(value="CNT_TOTAL")
    private Integer cntTotal;

	@Field(value="CNT_USED")
    private Integer cntUsed;

	@Field(userfield="pkEmp",userfieldscop=FieldType.ALL)
    private String modifier;

	@Field(value="MODITY_TIME")
    private Date modityTime;


    public String getPkSchemp(){
        return this.pkSchemp;
    }
    public void setPkSchemp(String pkSchemp){
        this.pkSchemp = pkSchemp;
    }

    public String getPkSch(){
        return this.pkSch;
    }
    public void setPkSch(String pkSch){
        this.pkSch = pkSch;
    }

    public String getPkEmp(){
        return this.pkEmp;
    }
    public void setPkEmp(String pkEmp){
        this.pkEmp = pkEmp;
    }

    public String getNameEmp(){
        return this.nameEmp;
    }
    public void setNameEmp(String nameEmp){
        this.nameEmp = nameEmp;
    }

    public Integer getCntTotal(){
        return this.cntTotal;
    }
    public void setCntTotal(Integer cntTotal){
        this.cntTotal = cntTotal;
    }

    public Integer getCntUsed(){
        return this.cntUsed;
    }
    public void setCntUsed(Integer cntUsed){
        this.cntUsed = cntUsed;
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