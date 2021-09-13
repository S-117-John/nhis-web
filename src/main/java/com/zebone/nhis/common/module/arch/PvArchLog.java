package com.zebone.nhis.common.module.arch;

import java.util.Date;

import com.zebone.platform.modules.dao.build.au.Field;
import com.zebone.platform.modules.dao.build.au.Field.FieldType;
import com.zebone.platform.modules.dao.build.au.Field.KeyId;
import com.zebone.platform.modules.dao.build.au.PK;
import com.zebone.platform.modules.dao.build.au.Table;


/**
 * Table: PV_ARCH_LOG - 博爱医院上传日志表 
 *
 * @since 2018-02-26 10:25:48
 */
@Table(value="PV_ARCH_LOG")
public class PvArchLog   {

	@PK
	@Field(value="PK_LOG",id=KeyId.UUID)
    private String pkLog;

	@Field(value="pk_org",userfield="pkOrg",userfieldscop=FieldType.INSERT)
    private String pkOrg;

	@Field(value="PATICODE")
    private String paticode;

	@Field(value="PID")
    private String pId;
	
	@Field(value="CODE_PV")
    private String codePv;

	@Field(value="FILE_NAME")
    private String fileName;

	@Field(value="FILE_PATH")
    private String filePath;

	@Field(value="PATINAME")
    private String patiname;

	@Field(value="NAME_EMP_OPER")
    private String nameEmpOper;

	@Field(value="FILE_TYPE")
    private String fileType;

	@Field(value="DATE_UPLOAD")
    private Date dateUpload;

	@Field(value="OLD_FILE")
    private String oldFile;

	@Field(value="OLD_PATH")
    private String oldPath;

	@Field(value="VERSION")
    private String version;

	@Field(value="ITEM_NAME")
    private String itemName;

	@Field(date=FieldType.ALL)
    private Date ts;

	@Field(value="DETPRPT")
    private String detprpt;

	@Field(value="PVTYPE")
    private String pvtype;

	@Field(value="AGE")
    private String age;

	@Field(value="SEX")
    private String sex;

	@Field(value="SYS_NAME")
    private String sysName;

	@Field(value="DEPT")
    private String dept;

	@Field(value="RID")
    private String rid;

	@Field(value="RSERIAL")
    private String rserial;

	@Field(value="IPTIMES")
    private String iptimes;

	@Field(value="VISITDATE")
    private String visitdate;

	@Field(value="DOCTYPE")
    private String doctype;

	@Field(value="DATERPT")
    private String daterpt;

	@Field(value="TIMEUSED")
    private String timeused;
	
	@Field(value="VISITTYPE")
    private String visittype;

	@Field(value="DEL_FLAG")
    private String delFlag;
	
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

    public String getPaticode(){
        return this.paticode;
    }
    public void setPaticode(String paticode){
        this.paticode = paticode;
    }

    public String getCodePv(){
        return this.codePv;
    }
    public void setCodePv(String codePv){
        this.codePv = codePv;
    }

    public String getFileName(){
        return this.fileName;
    }
    public void setFileName(String fileName){
        this.fileName = fileName;
    }

    public String getFilePath(){
        return this.filePath;
    }
    public void setFilePath(String filePath){
        this.filePath = filePath;
    }

    public String getPatiname(){
        return this.patiname;
    }
    public void setPatiname(String patiname){
        this.patiname = patiname;
    }

    public String getNameEmpOper(){
        return this.nameEmpOper;
    }
    public void setNameEmpOper(String nameEmpOper){
        this.nameEmpOper = nameEmpOper;
    }

    public String getFileType(){
        return this.fileType;
    }
    public void setFileType(String fileType){
        this.fileType = fileType;
    }

    public Date getDateUpload(){
        return this.dateUpload;
    }
    public void setDateUpload(Date dateUpload){
        this.dateUpload = dateUpload;
    }

    public String getOldFile(){
        return this.oldFile;
    }
    public void setOldFile(String oldFile){
        this.oldFile = oldFile;
    }

    public String getOldPath(){
        return this.oldPath;
    }
    public void setOldPath(String oldPath){
        this.oldPath = oldPath;
    }

    public String getVersion(){
        return this.version;
    }
    public void setVersion(String version){
        this.version = version;
    }

    public String getItemName(){
        return this.itemName;
    }
    public void setItemName(String itemName){
        this.itemName = itemName;
    }

    public Date getTs(){
        return this.ts;
    }
    public void setTs(Date ts){
        this.ts = ts;
    }

    public String getDetprpt(){
        return this.detprpt;
    }
    public void setDetprpt(String detprpt){
        this.detprpt = detprpt;
    }

    public String getPvtype(){
        return this.pvtype;
    }
    public void setPvtype(String pvtype){
        this.pvtype = pvtype;
    }

    public String getAge(){
        return this.age;
    }
    public void setAge(String age){
        this.age = age;
    }

    public String getSex(){
        return this.sex;
    }
    public void setSex(String sex){
        this.sex = sex;
    }

    public String getSysName(){
        return this.sysName;
    }
    public void setSysName(String sysName){
        this.sysName = sysName;
    }

    public String getDept(){
        return this.dept;
    }
    public void setDept(String dept){
        this.dept = dept;
    }

    public String getRid(){
        return this.rid;
    }
    public void setRid(String rid){
        this.rid = rid;
    }

    public String getRserial(){
        return this.rserial;
    }
    public void setRserial(String rserial){
        this.rserial = rserial;
    }

    public String getIptimes(){
        return this.iptimes;
    }
    public void setIptimes(String iptimes){
        this.iptimes = iptimes;
    }

    public String getVisitdate(){
        return this.visitdate;
    }
    public void setVisitdate(String visitdate){
        this.visitdate = visitdate;
    }

    public String getDoctype(){
        return this.doctype;
    }
    public void setDoctype(String doctype){
        this.doctype = doctype;
    }

    public String getDaterpt(){
        return this.daterpt;
    }
    public void setDaterpt(String daterpt){
        this.daterpt = daterpt;
    }

    public String getTimeused(){
        return this.timeused;
    }
    public void setTimeused(String timeused){
        this.timeused = timeused;
    }
	public String getpId() {
		return pId;
	}
	public void setpId(String pId) {
		this.pId = pId;
	}
	public String getVisittype() {
		return visittype;
	}
	public void setVisittype(String visittype) {
		this.visittype = visittype;
	}
	public String getDelFlag() {
		return delFlag;
	}
	public void setDelFlag(String delFlag) {
		this.delFlag = delFlag;
	}
    
    
}