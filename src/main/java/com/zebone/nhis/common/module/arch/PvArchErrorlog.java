package com.zebone.nhis.common.module.arch;

import java.util.Date;

import com.zebone.platform.modules.dao.build.au.Field;
import com.zebone.platform.modules.dao.build.au.Field.FieldType;
import com.zebone.platform.modules.dao.build.au.Table;


/**
 * Table: PV_ARCH_ERRORLOG 
 *
 * @since 2018-03-13 11:22:32
 */
@Table(value="PV_ARCH_ERRORLOG")
public class PvArchErrorlog   {

	@Field(value="PK_ERR_LOG")
    private String pkErrLog;

	@Field(value="pk_org",userfield="pkOrg",userfieldscop=FieldType.INSERT)
    private String pkOrg;

	@Field(value="ERR_RESON")
    private String errReson;
	
	@Field(value="FLAG_DEL")
	private String flagDel;

	@Field(value="XML_INFO")
    private String xmlInfo;

	@Field(date=FieldType.ALL)
    private Date ts;

	@Field(value="FILE_NAME")
    private String fileName;

	@Field(value="PATINAME")
    private String patiname;

	@Field(value="PATICODE")
    private String paticode;

	@Field(value="VISITCODE")
    private String visitcode;

	@Field(value="NAME_EMP_OPER")
    private String nameEmpOper;

	@Field(value="DEPTRPT")
    private String deptrpt;

	@Field(value="PVTYPE")
    private String pvtype;

	@Field(value="DEPT")
    private String dept;

	@Field(value="IPTIMES")
    private String iptimes;


    public String getPkErrLog(){
        return this.pkErrLog;
    }
    public void setPkErrLog(String pkErrLog){
        this.pkErrLog = pkErrLog;
    }

    public String getPkOrg(){
        return this.pkOrg;
    }
    public void setPkOrg(String pkOrg){
        this.pkOrg = pkOrg;
    }

    public String getErrReson(){
        return this.errReson;
    }
    public void setErrReson(String errReson){
        this.errReson = errReson;
    }

    public String getXmlInfo(){
        return this.xmlInfo;
    }
    public void setXmlInfo(String xmlInfo){
        this.xmlInfo = xmlInfo;
    }

    public Date getTs(){
        return this.ts;
    }
    public void setTs(Date ts){
        this.ts = ts;
    }

    public String getFileName(){
        return this.fileName;
    }
    public void setFileName(String fileName){
        this.fileName = fileName;
    }

    public String getPatiname(){
        return this.patiname;
    }
    public void setPatiname(String patiname){
        this.patiname = patiname;
    }

    public String getPaticode(){
        return this.paticode;
    }
    public void setPaticode(String paticode){
        this.paticode = paticode;
    }

    public String getVisitcode(){
        return this.visitcode;
    }
    public void setVisitcode(String visitcode){
        this.visitcode = visitcode;
    }

    public String getNameEmpOper(){
        return this.nameEmpOper;
    }
    public void setNameEmpOper(String nameEmpOper){
        this.nameEmpOper = nameEmpOper;
    }

    public String getDeptrpt(){
        return this.deptrpt;
    }
    public void setDeptrpt(String deptrpt){
        this.deptrpt = deptrpt;
    }

    public String getPvtype(){
        return this.pvtype;
    }
    public void setPvtype(String pvtype){
        this.pvtype = pvtype;
    }

    public String getDept(){
        return this.dept;
    }
    public void setDept(String dept){
        this.dept = dept;
    }

    public String getIptimes(){
        return this.iptimes;
    }
    public void setIptimes(String iptimes){
        this.iptimes = iptimes;
    }
	public String getFlagDel() {
		return flagDel;
	}
	public void setFlagDel(String flagDel) {
		this.flagDel = flagDel;
	}
    
}