package com.zebone.nhis.common.module.emr.mgr;

import java.util.Date;
import java.math.BigDecimal;

import com.zebone.platform.modules.dao.build.au.Field;
import com.zebone.platform.modules.dao.build.au.Field.FieldType;
import com.zebone.platform.modules.dao.build.au.PK;
import com.zebone.platform.modules.dao.build.au.Table;
import com.zebone.platform.modules.dao.build.au.Field.KeyId;


/**
 * Table: EMR_OPERATE_LOGS 
 *
 * @since 2018-03-06 12:52:50
 */
@Table(value="EMR_OPERATE_LOGS")
public class EmrOperateLogs   {

	@PK
	@Field(value="PK_LOG",id=KeyId.UUID)
    private String pkLog;

	@Field(value="pk_org",userfield="pkOrg",userfieldscop=FieldType.INSERT)
    private String pkOrg;

	@Field(value="PK_PV")
    private String pkPv;

	@Field(value="PK_PI")
    private String pkPi;

	@Field(value="TIMES")
    private BigDecimal times;

	@Field(value="PK_REC")
    private String pkRec;

	@Field(value="TYPE_CODE")
    private String typeCode;

	@Field(value="TYPE_NAME")
    private String typeName;

    /** CODE - archive:病历归档
   archive_cancel:病历归档取消
   rhip_upload：区域上传
   page_upload：病案上传 */
	@Field(value="CODE")
    private String code;

	@Field(value="NAME")
    private String name;

    /** EU_STATUS - 0：失败
   1：成功 */
	@Field(value="EU_STATUS")
    private String euStatus;

	@Field(value="OPERATE_TXT")
    private String operateTxt;

	@Field(value="REMARK")
    private String remark;

	@Field(value="DEL_FLAG")
    private String delFlag;

	@Field(userfield="pkEmp",userfieldscop=FieldType.INSERT)
    private String creator;

	@Field(value="CREATE_TIME",date=FieldType.INSERT)
    private Date createTime;

	@Field(date=FieldType.ALL)
    private Date ts;


    public String getPkLog(){
        return this.pkLog;
    }
    public void setPkLog(String pkLog){
        this.pkLog = pkLog;
    }

    public String getPkOrg(){
        return this.pkOrg;
    }
    public void setPkOrg(String pkOrg){
        this.pkOrg = pkOrg;
    }

    public String getPkPv(){
        return this.pkPv;
    }
    public void setPkPv(String pkPv){
        this.pkPv = pkPv;
    }

    public String getPkPi(){
        return this.pkPi;
    }
    public void setPkPi(String pkPi){
        this.pkPi = pkPi;
    }

    public BigDecimal getTimes(){
        return this.times;
    }
    public void setTimes(BigDecimal times){
        this.times = times;
    }

    public String getPkRec(){
        return this.pkRec;
    }
    public void setPkRec(String pkRec){
        this.pkRec = pkRec;
    }

    public String getTypeCode(){
        return this.typeCode;
    }
    public void setTypeCode(String typeCode){
        this.typeCode = typeCode;
    }

    public String getTypeName(){
        return this.typeName;
    }
    public void setTypeName(String typeName){
        this.typeName = typeName;
    }

    public String getCode(){
        return this.code;
    }
    public void setCode(String code){
        this.code = code;
    }

    public String getName(){
        return this.name;
    }
    public void setName(String name){
        this.name = name;
    }

    public String getEuStatus(){
        return this.euStatus;
    }
    public void setEuStatus(String euStatus){
        this.euStatus = euStatus;
    }

    public String getOperateTxt(){
        return this.operateTxt;
    }
    public void setOperateTxt(String operateTxt){
        this.operateTxt = operateTxt;
    }

    public String getRemark(){
        return this.remark;
    }
    public void setRemark(String remark){
        this.remark = remark;
    }

    public String getDelFlag(){
        return this.delFlag;
    }
    public void setDelFlag(String delFlag){
        this.delFlag = delFlag;
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

    public Date getTs(){
        return this.ts;
    }
    public void setTs(Date ts){
        this.ts = ts;
    }
}