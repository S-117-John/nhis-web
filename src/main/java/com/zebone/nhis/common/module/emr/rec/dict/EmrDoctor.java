package com.zebone.nhis.common.module.emr.rec.dict;

import java.util.Date;

import com.zebone.platform.modules.dao.build.au.Field;
import com.zebone.platform.modules.dao.build.au.Field.FieldType;
import com.zebone.platform.modules.dao.build.au.PK;
import com.zebone.platform.modules.dao.build.au.Table;
import com.zebone.platform.modules.dao.build.au.Field.KeyId;


/**
 * Table: EMR_DOCTOR 
 *
 * @since 2016-10-13 10:21:22
 */
@Table(value="EMR_DOCTOR")
public class EmrDoctor   {

	@PK
	@Field(value="PK_DOCTOR",id=KeyId.UUID)
    private String pkDoctor;

	@Field(value="PK_ORG")
    private String pkOrg;

	@Field(value="CODE")
    private String code;
	
	@Field(value="PK_EMP")
    private String pkEmp;

	@Field(value="NAME")
    private String name;
	
	@Field(value="PY_CODE")
    private String pyCode;

	@Field(value="D_CODE")
    private String dCode;

	@Field(value="GB_CODE")
    private String gbCode;

	@Field(value="CONSULT")
    private String consult;

	@Field(value="DIRECTOR")
    private String director;

	@Field(value="SIGN_PWD")
    private String signPwd;

	@Field(value="SIGN_IMAGE")
    private byte[] signImage;

	@Field(value="EU_AUDIT_LEVEL")
    private String euAuditLevel;
	
	@Field(value="DEL_FLAG")
    private String delFlag;

	@Field(value="REMARK")
    private String remark;

	@Field(userfield="pkEmp",userfieldscop=FieldType.INSERT)
    private String creator;

	@Field(value="CREATE_TIME",date=FieldType.INSERT)
    private Date createTime;

	@Field(date=FieldType.ALL)
    private Date ts;

	@Field(value="PK_DEPT")
    private String pkDept;

	@Field(value="PK_WG")
    private String pkWg;
	
	private String status;
	
	private String consultName;
	private String directorName;
	private String euCerttype;
	private String caid;
	private String euAuditLevelCode;
	
	public String getEuAuditLevelCode(){
		return euAuditLevel;
	}
    public String getCode(){
        return this.code;
    }
    public void setCode(String code){
        this.code = code;
    }

    public String getPkEmp(){
        return this.pkEmp;
    }
    public void setPkEmp(String pkEmp){
        this.pkEmp = pkEmp;
    }

    public String getName(){
        return this.name;
    }
    public void setName(String name){
        this.name = name;
    }
    
    public String getPyCode(){
        return this.pyCode;
    }
    public void setPyCode(String pyCode){
        this.pyCode = pyCode;
    }

    public String getdCode(){
        return this.dCode;
    }
    public void setdCode(String dCode){
        this.dCode = dCode;
    }

    public String getGbCode(){
        return this.gbCode;
    }
    public void setGbCode(String gbCode){
        this.gbCode = gbCode;
    }
    
    public String getConsult(){
        return this.consult;
    }
    public void setConsult(String consult){
        this.consult = consult;
    }

    public String getDirector(){
        return this.director;
    }
    public void setDirector(String director){
        this.director = director;
    }

    public String getSignPwd(){
        return this.signPwd;
    }
    public void setSignPwd(String signPwd){
        this.signPwd = signPwd;
    }

    public byte[] getSignImage(){
        return this.signImage;
    }
    public void setSignImage(byte[] signImage){
        this.signImage = signImage;
    }

    public String getDelFlag(){
        return this.delFlag;
    }
    public void setDelFlag(String delFlag){
        this.delFlag = delFlag;
    }

    public String getRemark(){
        return this.remark;
    }
    public void setRemark(String remark){
        this.remark = remark;
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
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getConsultName() {
		return consultName;
	}
	public void setConsultName(String consultName) {
		this.consultName = consultName;
	}
	public String getDirectorName() {
		return directorName;
	}
	public void setDirectorName(String directorName) {
		this.directorName = directorName;
	}
	public String getPkOrg() {
		return pkOrg;
	}
	public void setPkOrg(String pkOrg) {
		this.pkOrg = pkOrg;
	}
	public String getPkDoctor() {
		return pkDoctor;
	}
	public void setPkDoctor(String pkDoctor) {
		this.pkDoctor = pkDoctor;
	}
	public String getEuCerttype() {
		return euCerttype;
	}
	public void setEuCerttype(String euCerttype) {
		this.euCerttype = euCerttype;
	}
	public String getCaid() {
		return caid;
	}
	public void setCaid(String caid) {
		this.caid = caid;
	}
	public String getEuAuditLevel() {
		return euAuditLevel;
	}
	public void setEuAuditLevel(String euAuditLevel) {
		this.euAuditLevel = euAuditLevel;
	}
	public String getPkDept() {
		return pkDept;
	}
	public void setPkDept(String pkDept) {
		this.pkDept = pkDept;
	}
	public String getPkWg() {
		return pkWg;
	}
	public void setPkWg(String pkWg) {
		this.pkWg = pkWg;
	}
    
}
