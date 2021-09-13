package com.zebone.nhis.common.module.cn.ipdw;

import java.util.Date;

import com.zebone.platform.modules.dao.build.au.Field;
import com.zebone.platform.modules.dao.build.au.Field.FieldType;
import com.zebone.platform.modules.dao.build.au.Field.KeyId;
import com.zebone.platform.modules.dao.build.au.PK;
import com.zebone.platform.modules.dao.build.au.Table;


/**
 * Table: CN_OP_JION 
 *
 * @since 2016-09-12 10:30:28
 */
@Table(value="CN_OP_JION")
public class CnOpJoin   {

	@PK
	@Field(value="PK_OP_JION",id=KeyId.UUID)
    private String pkOpJion;

	@Field(value="PK_ORDOP")
    private String pkOrdop;

	@Field(value="DT_OPJION")
    private String dtOpjion;

	@Field(value="PK_EMP_OPT")
    private String pkEmpOpt;

	@Field(value="NAME_EMP_OPT")
    private String nameEmpOpt;

	@Field(value="TIME_WORK")
    private Integer timeWork;

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


    public String getPkOpJion(){
        return this.pkOpJion;
    }
    public void setPkOpJion(String pkOpJion){
        this.pkOpJion = pkOpJion;
    }

    public String getPkOrdop(){
        return this.pkOrdop;
    }
    public void setPkOrdop(String pkOrdop){
        this.pkOrdop = pkOrdop;
    }

    public String getDtOpjion(){
        return this.dtOpjion;
    }
    public void setDtOpjion(String dtOpjion){
        this.dtOpjion = dtOpjion;
    }

    public String getPkEmpOpt(){
        return this.pkEmpOpt;
    }
    public void setPkEmpOpt(String pkEmpOpt){
        this.pkEmpOpt = pkEmpOpt;
    }

    public String getNameEmpOpt(){
        return this.nameEmpOpt;
    }
    public void setNameEmpOpt(String nameEmpOpt){
        this.nameEmpOpt = nameEmpOpt;
    }

    public Integer getTimeWork(){
        return this.timeWork;
    }
    public void setTimeWork(Integer timeWork){
        this.timeWork = timeWork;
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
    public String rowStatus;


	public String getRowStatus() {
		return rowStatus;
	}
	public void setRowStatus(String rowStatus) {
		this.rowStatus = rowStatus;
	}
    
}