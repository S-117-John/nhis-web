package com.zebone.nhis.ma.pub.syx.vo;

import java.util.Date;
import java.math.BigDecimal;

import com.zebone.platform.modules.dao.build.au.Field;
import com.zebone.platform.modules.dao.build.au.PK;
import com.zebone.platform.modules.dao.build.au.Table;
import com.zebone.platform.modules.dao.build.au.Field.KeyId;


/**
 * 医院旧系统患者表
 * Table: tPatient 
 *
 * @since 2018-12-18 11:59:19
 */
@Table(value="tPatient")
public class Tpatient   {

//	@PK
	@Field(value="PatientID")
    private String patientid;

	@Field(value="PatientTypeListID")
    private String patienttypelistid;

	@Field(value="SpecialCompanyID")
    private String specialcompanyid;

	@Field(value="SexFlag")
    private String sexflag;

	@Field(value="PatientName")
    private String patientname;

	@Field(value="Birthday")
    private Date birthday;

	@Field(value="HeightCM")
    private String heightcm;

	@Field(value="WeightKG")
    private String weightkg;

	@Field(value="Phone")
    private String phone;

	@Field(value="PatientNo")
    private String patientno;

	@Field(value="IdentityCardNo")
    private String identitycardno;

	@Field(value="EmployeeID")
    private String employeeid;

	@Field(value="EmployeeFamilyID")
    private String employeefamilyid;

	@Field(value="MedicalCertificateNo")
    private String medicalcertificateno;

	@Field(value="PatientCardNo")
    private String patientcardno;

	@Field(value="MedicareCardNo")
    private String medicarecardno;

	@Field(value="DiagnoseFlag")
    private String diagnoseflag;

	@Field(value="OPSpecialItemIPFlag")
    private String opspecialitemipflag;

	@Field(value="InValidFlag")
    private String invalidflag;

	@Field(value="DWTranFlag")
    private String dwtranflag;

	@Field(value="InValidDateTime")
    private Date invaliddatetime;

	@Field(value="OPProportion")
    private String opproportion;

	@Field(value="IPProportion")
    private String ipproportion;

	@Field(value="ElderCertificateNo")
    private String eldercertificateno;

	@Field(value="VIPCardType")
    private String vipcardtype;

	@Field(value="VIPCardNo")
    private String vipcardno;

	@Field(value="SecretFlag")
    private String secretflag;

	@Field(value="PISSpecialCompanyID")
    private String pisspecialcompanyid;

	@Field(value="MediCareType")
    private String medicaretype;

	@Field(value="MediCarePersonnelNo")
    private String medicarepersonnelno;

	@Field(value="CompanyCode")
    private String companycode;

	@Field(value="CitizenCardNO")
    private String citizencardno;

	@Field(value="vip_code")
    private String vipCode;

	@Field(value="TurnNameRegisterFlag")
    private String turnnameregisterflag;

	@Field(value="AgricultureBankCardNo")
    private String agriculturebankcardno;

	@Field(value="HealthCardNo")
    private String healthcardno;

	@Field(value="TrueNameFlag")
    private String truenameflag;

	@Field(value="UnitTrueNameFlag")
    private String unittruenameflag;

	@Field(value="SpouseName")
    private String spousename;

	@Field(value="SpouseIDCardNo")
    private String spouseidcardno;


    public String getPatientid(){
        return this.patientid;
    }
    public void setPatientid(String patientid){
        this.patientid = patientid;
    }

    public String getPatienttypelistid(){
        return this.patienttypelistid;
    }
    public void setPatienttypelistid(String patienttypelistid){
        this.patienttypelistid = patienttypelistid;
    }

    public String getSpecialcompanyid(){
        return this.specialcompanyid;
    }
    public void setSpecialcompanyid(String specialcompanyid){
        this.specialcompanyid = specialcompanyid;
    }

    public String getSexflag(){
        return this.sexflag;
    }
    public void setSexflag(String sexflag){
        this.sexflag = sexflag;
    }

    public String getPatientname(){
        return this.patientname;
    }
    public void setPatientname(String patientname){
        this.patientname = patientname;
    }

    public Date getBirthday(){
        return this.birthday;
    }
    public void setBirthday(Date birthday){
        this.birthday = birthday;
    }

    public String getHeightcm(){
        return this.heightcm;
    }
    public void setHeightcm(String heightcm){
        this.heightcm = heightcm;
    }

    public String getWeightkg(){
        return this.weightkg;
    }
    public void setWeightkg(String weightkg){
        this.weightkg = weightkg;
    }

    public String getPhone(){
        return this.phone;
    }
    public void setPhone(String phone){
        this.phone = phone;
    }

    public String getPatientno(){
        return this.patientno;
    }
    public void setPatientno(String patientno){
        this.patientno = patientno;
    }

    public String getIdentitycardno(){
        return this.identitycardno;
    }
    public void setIdentitycardno(String identitycardno){
        this.identitycardno = identitycardno;
    }

    public String getEmployeeid(){
        return this.employeeid;
    }
    public void setEmployeeid(String employeeid){
        this.employeeid = employeeid;
    }

    public String getEmployeefamilyid(){
        return this.employeefamilyid;
    }
    public void setEmployeefamilyid(String employeefamilyid){
        this.employeefamilyid = employeefamilyid;
    }

    public String getMedicalcertificateno(){
        return this.medicalcertificateno;
    }
    public void setMedicalcertificateno(String medicalcertificateno){
        this.medicalcertificateno = medicalcertificateno;
    }

    public String getPatientcardno(){
        return this.patientcardno;
    }
    public void setPatientcardno(String patientcardno){
        this.patientcardno = patientcardno;
    }

    public String getMedicarecardno(){
        return this.medicarecardno;
    }
    public void setMedicarecardno(String medicarecardno){
        this.medicarecardno = medicarecardno;
    }

    public String getDiagnoseflag(){
        return this.diagnoseflag;
    }
    public void setDiagnoseflag(String diagnoseflag){
        this.diagnoseflag = diagnoseflag;
    }

    public String getOpspecialitemipflag(){
        return this.opspecialitemipflag;
    }
    public void setOpspecialitemipflag(String opspecialitemipflag){
        this.opspecialitemipflag = opspecialitemipflag;
    }

    public String getInvalidflag(){
        return this.invalidflag;
    }
    public void setInvalidflag(String invalidflag){
        this.invalidflag = invalidflag;
    }

    public String getDwtranflag(){
        return this.dwtranflag;
    }
    public void setDwtranflag(String dwtranflag){
        this.dwtranflag = dwtranflag;
    }

    public Date getInvaliddatetime(){
        return this.invaliddatetime;
    }
    public void setInvaliddatetime(Date invaliddatetime){
        this.invaliddatetime = invaliddatetime;
    }

    public String getOpproportion(){
        return this.opproportion;
    }
    public void setOpproportion(String opproportion){
        this.opproportion = opproportion;
    }

    public String getIpproportion(){
        return this.ipproportion;
    }
    public void setIpproportion(String ipproportion){
        this.ipproportion = ipproportion;
    }

    public String getEldercertificateno(){
        return this.eldercertificateno;
    }
    public void setEldercertificateno(String eldercertificateno){
        this.eldercertificateno = eldercertificateno;
    }

    public String getVipcardtype(){
        return this.vipcardtype;
    }
    public void setVipcardtype(String vipcardtype){
        this.vipcardtype = vipcardtype;
    }

    public String getVipcardno(){
        return this.vipcardno;
    }
    public void setVipcardno(String vipcardno){
        this.vipcardno = vipcardno;
    }

    public String getSecretflag(){
        return this.secretflag;
    }
    public void setSecretflag(String secretflag){
        this.secretflag = secretflag;
    }

    public String getPisspecialcompanyid(){
        return this.pisspecialcompanyid;
    }
    public void setPisspecialcompanyid(String pisspecialcompanyid){
        this.pisspecialcompanyid = pisspecialcompanyid;
    }

    public String getMedicaretype(){
        return this.medicaretype;
    }
    public void setMedicaretype(String medicaretype){
        this.medicaretype = medicaretype;
    }

    public String getMedicarepersonnelno(){
        return this.medicarepersonnelno;
    }
    public void setMedicarepersonnelno(String medicarepersonnelno){
        this.medicarepersonnelno = medicarepersonnelno;
    }

    public String getCompanycode(){
        return this.companycode;
    }
    public void setCompanycode(String companycode){
        this.companycode = companycode;
    }

    public String getCitizencardno(){
        return this.citizencardno;
    }
    public void setCitizencardno(String citizencardno){
        this.citizencardno = citizencardno;
    }

    public String getVipCode(){
        return this.vipCode;
    }
    public void setVipCode(String vipCode){
        this.vipCode = vipCode;
    }

    public String getTurnnameregisterflag(){
        return this.turnnameregisterflag;
    }
    public void setTurnnameregisterflag(String turnnameregisterflag){
        this.turnnameregisterflag = turnnameregisterflag;
    }

    public String getAgriculturebankcardno(){
        return this.agriculturebankcardno;
    }
    public void setAgriculturebankcardno(String agriculturebankcardno){
        this.agriculturebankcardno = agriculturebankcardno;
    }

    public String getHealthcardno(){
        return this.healthcardno;
    }
    public void setHealthcardno(String healthcardno){
        this.healthcardno = healthcardno;
    }

    public String getTruenameflag(){
        return this.truenameflag;
    }
    public void setTruenameflag(String truenameflag){
        this.truenameflag = truenameflag;
    }

    public String getUnittruenameflag(){
        return this.unittruenameflag;
    }
    public void setUnittruenameflag(String unittruenameflag){
        this.unittruenameflag = unittruenameflag;
    }

    public String getSpousename(){
        return this.spousename;
    }
    public void setSpousename(String spousename){
        this.spousename = spousename;
    }

    public String getSpouseidcardno(){
        return this.spouseidcardno;
    }
    public void setSpouseidcardno(String spouseidcardno){
        this.spouseidcardno = spouseidcardno;
    }
}