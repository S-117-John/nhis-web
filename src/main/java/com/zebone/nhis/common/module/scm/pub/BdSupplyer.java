package com.zebone.nhis.common.module.scm.pub;


import java.util.Date;

import com.zebone.nhis.common.module.BaseModule;
import com.zebone.platform.modules.dao.build.au.Field;
import com.zebone.platform.modules.dao.build.au.PK;
import com.zebone.platform.modules.dao.build.au.Table;
import com.zebone.platform.modules.dao.build.au.Field.KeyId;


/**
 * Table: BD_SUPPLYER - bd_supplyer 
 *
 * @since 2016-10-21 04:40:29
 */
@Table(value="BD_SUPPLYER")
public class BdSupplyer extends BaseModule  {

	@PK
	@Field(value="PK_SUPPLYER",id=KeyId.UUID)
    private String pkSupplyer;

	@Field(value="CODE")
    private String code;

	@Field(value="NAME")
    private String name;

	@Field(value="DT_BANK")
    private String dtBank;

	@Field(value="ACC_NO")
    private String accNo;

	@Field(value="SHORT_NAME")
    private String shortName;

	@Field(value="ADDRESS")
    private String address;

	@Field(value="ZIP")
    private String zip;

	@Field(value="TEL")
    private String tel;

	@Field(value="URL")
    private String url;

	@Field(value="NAME_REL")
    private String nameRel;

	@Field(value="SPCODE")
    private String spcode;

	@Field(value="D_CODE")
    private String dCode;

	@Field(value="EMAIL")
    private String email;

	@Field(value="NOTE")
    private String note;

	@Field(value="FLAG_STOP")
    private String flagStop;
	
	@Field(value="reg_no")
	private String regNo;		//注册证号
	
	@Field(value="date_reg")
	private Date dateReg;		//成立日期
	
	@Field(value="date_valid_run")
	private Date dateValidRun;	//经营效期
	
	@Field(value="license_no")
	private String licenseNo;	//许可证号
	
	@Field(value="date_valid_license")
	private Date dateValidLicense;		//许可效期
	
    public String getRegNo() {
		return regNo;
	}
	public void setRegNo(String regNo) {
		this.regNo = regNo;
	}
	public Date getDateReg() {
		return dateReg;
	}
	public void setDateReg(Date dateReg) {
		this.dateReg = dateReg;
	}
	public Date getDateValidRun() {
		return dateValidRun;
	}
	public void setDateValidRun(Date dateValidRun) {
		this.dateValidRun = dateValidRun;
	}
	
	public String getLicenseNo() {
		return licenseNo;
	}
	public void setLicenseNo(String licenseNo) {
		this.licenseNo = licenseNo;
	}
	public Date getDateValidLicense() {
		return dateValidLicense;
	}
	public void setDateValidLicense(Date dateValidLicense) {
		this.dateValidLicense = dateValidLicense;
	}
	public String getPkSupplyer(){
        return this.pkSupplyer;
    }
    public void setPkSupplyer(String pkSupplyer){
        this.pkSupplyer = pkSupplyer;
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

    public String getDtBank(){
        return this.dtBank;
    }
    public void setDtBank(String dtBank){
        this.dtBank = dtBank;
    }

    public String getAccNo(){
        return this.accNo;
    }
    public void setAccNo(String accNo){
        this.accNo = accNo;
    }

    public String getShortName(){
        return this.shortName;
    }
    public void setShortName(String shortName){
        this.shortName = shortName;
    }

    public String getAddress(){
        return this.address;
    }
    public void setAddress(String address){
        this.address = address;
    }

    public String getZip(){
        return this.zip;
    }
    public void setZip(String zip){
        this.zip = zip;
    }

    public String getTel(){
        return this.tel;
    }
    public void setTel(String tel){
        this.tel = tel;
    }

    public String getUrl(){
        return this.url;
    }
    public void setUrl(String url){
        this.url = url;
    }

    public String getNameRel(){
        return this.nameRel;
    }
    public void setNameRel(String nameRel){
        this.nameRel = nameRel;
    }

    public String getSpcode(){
        return this.spcode;
    }
    public void setSpcode(String spcode){
        this.spcode = spcode;
    }

    public String getdCode(){
        return this.dCode;
    }
    public void setdCode(String dCode){
        this.dCode = dCode;
    }

    public String getEmail(){
        return this.email;
    }
    public void setEmail(String email){
        this.email = email;
    }

    public String getNote(){
        return this.note;
    }
    public void setNote(String note){
        this.note = note;
    }

    public String getFlagStop(){
        return this.flagStop;
    }
    public void setFlagStop(String flagStop){
        this.flagStop = flagStop;
    }
}