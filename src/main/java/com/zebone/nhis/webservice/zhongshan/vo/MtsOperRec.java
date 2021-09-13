package com.zebone.nhis.webservice.zhongshan.vo;

import java.util.Date;

import com.zebone.platform.modules.dao.build.au.Field;
import com.zebone.platform.modules.dao.build.au.Field.FieldType;
import com.zebone.platform.modules.dao.build.au.PK;
import com.zebone.platform.modules.dao.build.au.Table;
import com.zebone.platform.modules.dao.build.au.Field.KeyId;


/**
 * 中山博爱-医技系统操作记录
 * @author chengjia
 *
 */
@Table(value="MTS_OPER_REC")
public class MtsOperRec   {

	@PK
	@Field(value="PK_REC",id=KeyId.UUID)
    private String pkRec;

	@Field(value="pk_org",userfield="pkOrg",userfieldscop=FieldType.INSERT)
    private String pkOrg;

	@Field(value="PK_PV")
    private String pkPv;

	@Field(value="CODE_PI")
    private String codePi;

	@Field(value="CODE")
    private String code;

	@Field(value="TIMES")
    private Integer times;

	@Field(value="PV_TYPE")
    private String pvType;

	@Field(value="PV_NAME")
    private String pvName;

	@Field(value="MTS_TYPE")
    private String mtsType;

	@Field(value="MTS_NAME")
    private String mtsName;

	@Field(value="OPER_TYPE")
    private String operType;

	@Field(value="OPER_NAME")
    private String operName;

	@Field(value="REQ_NO")
    private String reqNo;

	@Field(value="REC_NO")
    private String recNo;

	@Field(value="OPER_TIME")
    private Date operTime;

	@Field(value="EMP_CODE")
    private String empCode;

	@Field(value="EMP_NAME")
    private String empName;

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


    public String getPkRec(){
        return this.pkRec;
    }
    public void setPkRec(String pkRec){
        this.pkRec = pkRec;
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

    public String getCodePi(){
        return this.codePi;
    }
    public void setCodePi(String codePi){
        this.codePi = codePi;
    }

    public String getCode(){
        return this.code;
    }
    public void setCode(String code){
        this.code = code;
    }

    public Integer getTimes(){
        return this.times;
    }
    public void setTimes(Integer times){
        this.times = times;
    }

    public String getPvType(){
        return this.pvType;
    }
    public void setPvType(String pvType){
        this.pvType = pvType;
    }

    public String getPvName(){
        return this.pvName;
    }
    public void setPvName(String pvName){
        this.pvName = pvName;
    }

    public String getMtsType(){
        return this.mtsType;
    }
    public void setMtsType(String mtsType){
        this.mtsType = mtsType;
    }

    public String getMtsName(){
        return this.mtsName;
    }
    public void setMtsName(String mtsName){
        this.mtsName = mtsName;
    }

    public String getOperType(){
        return this.operType;
    }
    public void setOperType(String operType){
        this.operType = operType;
    }

    public String getOperName(){
        return this.operName;
    }
    public void setOperName(String operName){
        this.operName = operName;
    }

    public String getReqNo(){
        return this.reqNo;
    }
    public void setReqNo(String reqNo){
        this.reqNo = reqNo;
    }

    public String getRecNo(){
        return this.recNo;
    }
    public void setRecNo(String recNo){
        this.recNo = recNo;
    }

    public Date getOperTime(){
        return this.operTime;
    }
    public void setOperTime(Date operTime){
        this.operTime = operTime;
    }

    public String getEmpCode(){
        return this.empCode;
    }
    public void setEmpCode(String empCode){
        this.empCode = empCode;
    }

    public String getEmpName(){
        return this.empName;
    }
    public void setEmpName(String empName){
        this.empName = empName;
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