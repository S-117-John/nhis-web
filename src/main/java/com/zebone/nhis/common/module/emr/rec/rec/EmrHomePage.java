package com.zebone.nhis.common.module.emr.rec.rec;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import com.zebone.nhis.emr.rec.rec.vo.EmrInfantRecVo;
import com.zebone.nhis.emr.rec.rec.vo.PvDiagVo;
import com.zebone.platform.modules.dao.build.au.Field;
import com.zebone.platform.modules.dao.build.au.PK;
import com.zebone.platform.modules.dao.build.au.Table;

/**
 *
 * @author 
 */
@Table(value = "EMR_HOME_PAGE")
public class EmrHomePage implements Serializable {

    @PK
    @Field(value = "PK_PAGE", id = Field.KeyId.UUID)
    private String pkPage;

    @Field(value = "PK_ORG")
    private String pkOrg;

    @Field(value = "PK_PI")
    private String pkPi;

    @Field(value = "TIMES")
    private Integer times;

    @Field(value = "PK_PV")
    private String pkPv;

    @Field(value = "MED_ORG_CODE")
    private String medOrgCode;

    @Field(value = "MED_ORG_NAME")
    private String medOrgName;

    @Field(value = "HEALTH_CARD_NO")
    private String healthCardNo;

    @Field(value = "MED_PAY_MODE")
    private String medPayMode;

    @Field(value = "PAT_NO")
    private String patNo;

    @Field(value = "NAME")
    private String name;

    @Field(value = "DT_SEX")
    private String dtSex;

    @Field(value = "BIRTH_DATE")
    private Date birthDate;

    @Field(value = "AGE_YEAR")
    private BigDecimal ageYear;

    @Field(value = "AGE_MONTH")
    private String ageMonth;

    @Field(value = "AGE_TXT")
    private String ageTxt;

    @Field(value = "NEWBORN_WEIGHT")
    private BigDecimal newbornWeight;

    @Field(value = "NEWBORN_IN_WEIGHT")
    private BigDecimal newbornInWeight;

    @Field(value = "BIRTH_ADDR_PROV")
    private String birthAddrProv;

    @Field(value = "BIRTH_ADDR_CITY")
    private String birthAddrCity;

    @Field(value = "BIRTH_ADDR_COUNTY")
    private String birthAddrCounty;

    @Field(value = "ORIGIN_ADDR_PROV")
    private String originAddrProv;

    @Field(value = "ORIGIN_ADDR_CITY")
    private String originAddrCity;

    @Field(value = "COUNTRY_CODE")
    private String countryCode;

    @Field(value = "COUNTRY_NAME")
    private String countryName;

    @Field(value = "NATION_CODE")
    private String nationCode;

    @Field(value = "NATION_NAME")
    private String nationName;

    @Field(value = "ID_TYPE")
    private String idType;

    @Field(value = "ID_NO")
    private String idNo;

    @Field(value = "OCCUP_CODE")
    private String occupCode;

    @Field(value = "OCCUP_NAME")
    private String occupName;

    @Field(value = "MARRY_CODE")
    private String marryCode;

    @Field(value = "MARRY_NAME")
    private String marryName;

    @Field(value = "CURR_ADDR_PROV")
    private String currAddrProv;

    @Field(value = "CURR_ADDR_CITY")
    private String currAddrCity;

    @Field(value = "CURR_ADDR_COUNTY")
    private String currAddrCounty;

    @Field(value = "CURR_PHONE")
    private String currPhone;

    @Field(value = "CURR_ZIP_CODE")
    private String currZipCode;

    @Field(value = "WORK_UNIT")
    private String workUnit;

    @Field(value = "WORK_UNIT_PHONE")
    private String workUnitPhone;

    @Field(value = "RESIDE_ADDR_PROV")
    private String resideAddrProv;

    @Field(value = "RESIDE_ADDR_CITY")
    private String resideAddrCity;

    @Field(value = "RESIDE_ADDR_COUNTY")
    private String resideAddrCounty;

    @Field(value = "RESIDE_ZIP_CODE")
    private String resideZipCode;

    @Field(value = "WORK_UNIT_ZIP_CODE")
    private String workUnitZipCode;

    @Field(value = "CONTACT_NAME")
    private String contactName;

    @Field(value = "CONTACT_RELAT_CODE")
    private String contactRelatCode;

    @Field(value = "CONTACT_RELAT_NAME")
    private String contactRelatName;

    @Field(value = "CONTACT_ADDR")
    private String contactAddr;

    @Field(value = "CONTACT_PHONE")
    private String contactPhone;

    @Field(value = "ADMIT_PATH_CODE")
    private String admitPathCode;

    @Field(value = "ADMIT_PATH_NAME")
    private String admitPathName;

    @Field(value = "ADMIT_TIME")
    private Date admitTime;

    @Field(value = "PK_ADMIT_DEPT")
    private String pkAdmitDept;

    @Field(value = "ADMIT_DEPT_NAME")
    private String admitDeptName;

    @Field(value = "ADMIT_WARD_NAME")
    private String admitWardName;

    @Field(value = "TRANS_DEPT_NAMES")
    private String transDeptNames;

    @Field(value = "DIS_TIME")
    private Date disTime;

    @Field(value = "PK_DEPT_DIS")
    private String pkDeptDis;

    @Field(value = "DIS_DEPT_NAME")
    private String disDeptName;

    @Field(value = "PK_WARD_DIS")
    private String pkWardDis;

    @Field(value = "DIS_WARD_NAME")
    private String disWardName;

    @Field(value = "PK_DIAG_CLINIC")
    private String pkDiagClinic;

    @Field(value = "IN_HOS_DAYS")
    private BigDecimal inHosDays;

    @Field(value = "DIAG_CODE_CLINIC")
    private String diagCodeClinic;

    @Field(value = "PK_DIAG_DIS")
    private String pkDiagDis;

    @Field(value = "DIAG_NAME_CLINIC")
    private String diagNameClinic;

    @Field(value = "DIAG_CODE_DIS")
    private String diagCodeDis;

    @Field(value = "PK_DIAG_EXTC_IP")
    private String pkDiagExtcIp;

    @Field(value = "DIAG_NAME_DIS")
    private String diagNameDis;

    @Field(value = "MED_REC_TYPE")
    private String medRecType;

    @Field(value = "FLAG_CP")
    private String flagCp;

    @Field(value = "NUM_RES")
    private Integer numRes;

    @Field(value = "NUM_SUCC")
    private Integer numSucc;

    @Field(value = "DIAG_CODE_EXTC_IP")
    private String diagCodeExtcIp;

    @Field(value = "PK_DIAG_PATHO")
    private String pkDiagPatho;

    @Field(value = "DIAG_NAME_EXTC_IP")
    private String diagNameExtcIp;

    @Field(value = "DIAG_CODE_PATHO")
    private String diagCodePatho;

    @Field(value = "DIAG_NAME_PATHO")
    private String diagNamePatho;

    @Field(value = "PATHO_NO")
    private String pathoNo;

    @Field(value = "FLAG_DRUG_ALLERGY")
    private String flagDrugAllergy;

    @Field(value = "ALLERGIC_DRUG")
    private String allergicDrug;

    @Field(value = "FLAG_AUTOPSY")
    private String flagAutopsy;

    @Field(value = "BLOOD_CODE_ABO")
    private String bloodCodeAbo;

    @Field(value = "BLOOD_NAME_ABO")
    private String bloodNameAbo;

    @Field(value = "BLOOD_CODE_RH")
    private String bloodCodeRh;

    @Field(value = "BLOOD_NAME_RH")
    private String bloodNameRh;

    @Field(value = "PK_EMP_DIRECTOR")
    private String pkEmpDirector;

    @Field(value = "DIRECTOR_NAME")
    private String directorName;

    @Field(value = "PK_EMP_CONSULT")
    private String pkEmpConsult;

    @Field(value = "CONSULT_NAME")
    private String consultName;

    @Field(value = "PK_EMP_REFER")
    private String pkEmpRefer;

    @Field(value = "REFER_NAME")
    private String referName;

    @Field(value = "PK_EMP_NURSE")
    private String pkEmpNurse;

    @Field(value = "NURSE_NAME")
    private String nurseName;

    @Field(value = "PK_EMP_LEARN")
    private String pkEmpLearn;

    @Field(value = "LEARN_NAME")
    private String learnName;

    @Field(value = "PK_EMP_INTERN")
    private String pkEmpIntern;

    @Field(value = "INTERN_NAME")
    private String internName;

    @Field(value = "PK_EMP_CODER")
    private String pkEmpCoder;

    @Field(value = "CODER_NAME")
    private String coderName;

    @Field(value = "QUALITY_CODE")
    private String qualityCode;

    @Field(value = "QUALITY_NAME")
    private String qualityName;

    @Field(value = "PK_EMP_QC_DOC")
    private String pkEmpQcDoc;

    @Field(value = "QC_DOC_NAME")
    private String qcDocName;

    @Field(value = "PK_EMP_QC_NURSE")
    private String pkEmpQcNurse;

    @Field(value = "QC_NURSE_NAME")
    private String qcNurseName;

    @Field(value = "QC_DATE")
    private Date qcDate;

    @Field(value = "LEAVE_HOS_CODE")
    private String leaveHosCode;

    @Field(value = "LEAVE_HOS_NAME")
    private String leaveHosName;

    @Field(value = "RECEIVE_ORG_CODE")
    private String receiveOrgCode;

    @Field(value = "RECEIVE_ORG_NAME")
    private String receiveOrgName;

    @Field(value = "FLAG_READMIT")
    private String flagReadmit;

    @Field(value = "READMIT_PURP")
    private String readmitPurp;

    @Field(value = "COMA_DAY_BEF")
    private Integer comaDayBef;

    @Field(value = "COMA_HOUR_BEF")
    private Integer comaHourBef;

    @Field(value = "COMA_MIN_BEF")
    private Integer comaMinBef;

    @Field(value = "COMA_DAY_AFTER")
    private Integer comaDayAfter;

    @Field(value = "COMA_HOUR_AFTER")
    private Integer comaHourAfter;

    @Field(value = "COMA_MIN_AFTER")
    private Integer comaMinAfter;

    @Field(value = "TOTAL_COST")
    private BigDecimal totalCost;

    @Field(value = "SELF_COST")
    private BigDecimal selfCost;

    @Field(value = "DEL_FLAG")
    private String delFlag;

    @Field(value = "REMARK")
    private String remark;

    @Field(userfield = "pkEmp", userfieldscop = Field.FieldType.INSERT)
    private String creator;

    @Field(value = "CREATE_TIME", date = Field.FieldType.INSERT)
    private Date createTime;

    @Field(value = "PK_ADMIT_WARD")
    private String pkAdmitWard;

    @Field(value = "PK_EMP_CHIEF")
    private String pkEmpChief;

    @Field(value = "CHIEF_NAME")
    private String chiefName;

    @Field(value = "TS",date = Field.FieldType.ALL)
    private Date ts;

    @Field(value = "PK_EMP_CLINIC")
    private String pkEmpClinic;

    @Field(value = "CLINIC_NAME")
    private String clinicName;

    @Field(value = "CURR_ADDR")
    private String currAddr;

    @Field(value = "RESIDE_ADDR")
    private String resideAddr;

    @Field(value = "ADMIT_COND_CODE")
    private String admitCondCode;

    @Field(value = "ADMIT_COND_NAME")
    private String admitCondName;

    @Field(value = "BIRTH_ADDR")
    private String birthAddr;

    @Field(value = "ORIGIN_ADDR")
    private String originAddr;

    @Field(value = "CURR_ADDR_DT")
    private String currAddrDt;

    @Field(value = "RESIDE_ADDR_DT")
    private String resideAddrDt;

    @Field(value = "DIAG_FIT_CODE_OI")
    private String diagFitCodeOi;

    @Field(value = "DIAG_FIT_CODE_CP")
    private String diagFitCodeCp;

    @Field(value = "DIAG_CODE_CLINIC_ICD")
    private String diagCodeClinicIcd;

    @Field(value = "DIAG_NAME_CLINIC_ICD")
    private String diagNameClinicIcd;

    @Field(value = "DIAG_CODE_EXTC_IP_ICD")
    private String diagCodeExtcIpIcd;

    @Field(value = "DIAG_NAME_EXTC_IP_ICD")
    private String diagNameExtcIpIcd;

    @Field(value = "DIAG_CODE_PATHO_ICD")
    private String diagCodePathoIcd;

    @Field(value = "DIAG_NAME_PATHO_ICD")
    private String diagNamePathoIcd;

    @Field(value = "PATHO_NO_ICD")
    private String pathoNoIcd;

    @Field(value = "FLAG_DRUG_ALLERGY_ICD")
    private String flagDrugAllergyIcd;

    @Field(value = "ALLERGIC_DRUG_ICD")
    private String allergicDrugIcd;

    @Field(value = "PART_DISEASE")
    private String partDisease;

    @Field(value = "PAT_SOURCE")
    private String patSource;

    @Field(value = "CODE_DCDT_CLINIC")
    private String codeDcdtClinic;

    @Field(value = "DESC_BODYPART")
    private String descBodypart;

    @Field(value = "DESC_DRGPROP")
    private String descDrgprop;

    @Field(value = "DESC_DIAG")
    private String descDiag;

    @Field(value = "DAYS_GDI")
    private Integer daysGdi;

    @Field(value = "DAYS_GDII")
    private Integer daysGdii;

    @Field(value = "DAYS_GDIII")
    private Integer daysGdiii;

    @Field(value = "DAYS_GDS")
    private Integer daysGds;

    @Field(value = "HOURS_GDS")
    private Integer hoursGds;

    @Field(value = "HOURS_BM")
    private Integer hoursBm;

    @Field(value = "MINUTES_BM")
    private Integer minutesBm;

    @Field(value = "VAL_ADLIN")
    private BigDecimal valAdlin;

    @Field(value = "VAL_ADLOUT")
    private BigDecimal valAdlout;

    @Field(value = "DT_DRGCASETYPE")
    private String dtDrgcasetype;

    @Field(value = "CURR_ADDR_ICD")
    private String currAddrIcd;

    @Field(value = "BIRTH_ADDR_CODE")
    private String birthAddrCode;

    @Field(value = "ORIGIN_ADDR_CODE")
    private String originAddrCode;

    @Field(value = "CURR_ADDR_CODE")
    private String currAddrCode;

    @Field(value = "RESIDE_ADDR_CODE")
    private String resideAddrCode;
    
    @Field(value = "CODE_PV")
    private String codePv;

    @Field(value = "FLAG_DAY_SURGERY")
    private String flagDaySurgery;
    
    private List<EmrHomePageDiags> diags;

    private List<EmrHomePageCharges> charges;

    private List<EmrHomePageOps> ops;

    private List<EmrHomePageOps> opsHandle;
    
    private List<PvDiagVo> diagVos ;

    private List<EmrHomePageTrans> trans;

    private List<EmrInfantRecVo> laborRecDt;

    private List<EmrHomePageBr> brs;

    private EmrHomePageOr or;

    private List<EmrHomePageOrDt> ordts;
    
    private EmrHomePageZL homePageZL;

    private String status;

    private String  pageType;

    public String codePi;

    private String diagFitOps;//手术与诊断不符合标志

    private String codedNotCnfrm;//病案编码使用--已编码不确定
    public String getFlagDaySurgery() {
        return flagDaySurgery;
    }

    public void setFlagDaySurgery(String flagDaySurgery) {
        this.flagDaySurgery = flagDaySurgery;
    }

    public String getCodePv() {
		return codePv;
	}

	public void setCodePv(String codePv) {
		this.codePv = codePv;
	}

	private static final long serialVersionUID = 1L;

    public List<EmrHomePageDiags> getDiags() {
        return diags;
    }

    public void setDiags(List<EmrHomePageDiags> diags) {
        this.diags = diags;
    }

    public List<EmrHomePageCharges> getCharges() {
        return charges;
    }

    public void setCharges(List<EmrHomePageCharges> charges) {
        this.charges = charges;
    }

    
    public EmrHomePageZL getHomePageZL() {
		return homePageZL;
	}

	public void setHomePageZL(EmrHomePageZL homePageZL) {
		this.homePageZL = homePageZL;
	}

	public List<EmrHomePageOps> getOps() {
        return ops;
    }

    public void setOps(List<EmrHomePageOps> ops) {
        this.ops = ops;
    }

    public List<PvDiagVo> getDiagVos() {
        return diagVos;
    }

    public void setDiagVos(List<PvDiagVo> diagVos) {
        this.diagVos = diagVos;
    }

    public List<EmrHomePageTrans> getTrans() {
        return trans;
    }

    public void setTrans(List<EmrHomePageTrans> trans) {
        this.trans = trans;
    }

    public List<EmrInfantRecVo> getLaborRecDt() {
        return laborRecDt;
    }

    public void setLaborRecDt(List<EmrInfantRecVo> laborRecDt) {
        this.laborRecDt = laborRecDt;
    }

    public List<EmrHomePageBr> getBrs() {
        return brs;
    }

    public void setBrs(List<EmrHomePageBr> brs) {
        this.brs = brs;
    }

    public EmrHomePageOr getOr() {
        return or;
    }

    public void setOr(EmrHomePageOr or) {
        this.or = or;
    }

    public List<EmrHomePageOrDt> getOrdts() {
        return ordts;
    }

    public void setOrdts(List<EmrHomePageOrDt> ordts) {
        this.ordts = ordts;
    }

    public String getPkPage() {
        return pkPage;
    }

    public void setPkPage(String pkPage) {
        this.pkPage = pkPage;
    }

    public String getPkOrg() {
        return pkOrg;
    }

    public void setPkOrg(String pkOrg) {
        this.pkOrg = pkOrg;
    }

    public String getPkPi() {
        return pkPi;
    }

    public void setPkPi(String pkPi) {
        this.pkPi = pkPi;
    }

    public Integer getTimes() {
        return times;
    }

    public void setTimes(Integer times) {
        this.times = times;
    }

    public String getPkPv() {
        return pkPv;
    }

    public void setPkPv(String pkPv) {
        this.pkPv = pkPv;
    }

    public String getMedOrgCode() {
        return medOrgCode;
    }

    public void setMedOrgCode(String medOrgCode) {
        this.medOrgCode = medOrgCode;
    }

    public String getMedOrgName() {
        return medOrgName;
    }

    public void setMedOrgName(String medOrgName) {
        this.medOrgName = medOrgName;
    }

    public String getHealthCardNo() {
        return healthCardNo;
    }

    public void setHealthCardNo(String healthCardNo) {
        this.healthCardNo = healthCardNo;
    }

    public String getMedPayMode() {
        return medPayMode;
    }

    public void setMedPayMode(String medPayMode) {
        this.medPayMode = medPayMode;
    }

    public String getPatNo() {
        return patNo;
    }

    public void setPatNo(String patNo) {
        this.patNo = patNo;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDtSex() {
        return dtSex;
    }

    public void setDtSex(String dtSex) {
        this.dtSex = dtSex;
    }

    public Date getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(Date birthDate) {
        this.birthDate = birthDate;
    }


    public String getAgeMonth() {
        return ageMonth;
    }

    public void setAgeMonth(String ageMonth) {
        this.ageMonth = ageMonth;
    }

    public String getAgeTxt() {
        return ageTxt;
    }

    public void setAgeTxt(String ageTxt) {
        this.ageTxt = ageTxt;
    }



    public String getBirthAddrProv() {
        return birthAddrProv;
    }

    public void setBirthAddrProv(String birthAddrProv) {
        this.birthAddrProv = birthAddrProv;
    }

    public String getBirthAddrCity() {
        return birthAddrCity;
    }

    public void setBirthAddrCity(String birthAddrCity) {
        this.birthAddrCity = birthAddrCity;
    }

    public String getBirthAddrCounty() {
        return birthAddrCounty;
    }

    public void setBirthAddrCounty(String birthAddrCounty) {
        this.birthAddrCounty = birthAddrCounty;
    }

    public String getOriginAddrProv() {
        return originAddrProv;
    }

    public void setOriginAddrProv(String originAddrProv) {
        this.originAddrProv = originAddrProv;
    }

    public String getOriginAddrCity() {
        return originAddrCity;
    }

    public void setOriginAddrCity(String originAddrCity) {
        this.originAddrCity = originAddrCity;
    }

    public String getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }

    public String getCountryName() {
        return countryName;
    }

    public void setCountryName(String countryName) {
        this.countryName = countryName;
    }

    public String getNationCode() {
        return nationCode;
    }

    public void setNationCode(String nationCode) {
        this.nationCode = nationCode;
    }

    public String getNationName() {
        return nationName;
    }

    public void setNationName(String nationName) {
        this.nationName = nationName;
    }

    public String getIdType() {
        return idType;
    }

    public void setIdType(String idType) {
        this.idType = idType;
    }

    public String getIdNo() {
        return idNo;
    }

    public void setIdNo(String idNo) {
        this.idNo = idNo;
    }

    public String getOccupCode() {
        return occupCode;
    }

    public void setOccupCode(String occupCode) {
        this.occupCode = occupCode;
    }

    public String getOccupName() {
        return occupName;
    }

    public void setOccupName(String occupName) {
        this.occupName = occupName;
    }

    public String getMarryCode() {
        return marryCode;
    }

    public void setMarryCode(String marryCode) {
        this.marryCode = marryCode;
    }

    public String getMarryName() {
        return marryName;
    }

    public void setMarryName(String marryName) {
        this.marryName = marryName;
    }

    public String getCurrAddrProv() {
        return currAddrProv;
    }

    public void setCurrAddrProv(String currAddrProv) {
        this.currAddrProv = currAddrProv;
    }

    public String getCurrAddrCity() {
        return currAddrCity;
    }

    public void setCurrAddrCity(String currAddrCity) {
        this.currAddrCity = currAddrCity;
    }

    public String getCurrAddrCounty() {
        return currAddrCounty;
    }

    public void setCurrAddrCounty(String currAddrCounty) {
        this.currAddrCounty = currAddrCounty;
    }

    public String getCurrPhone() {
        return currPhone;
    }

    public void setCurrPhone(String currPhone) {
        this.currPhone = currPhone;
    }

    public String getCurrZipCode() {
        return currZipCode;
    }

    public void setCurrZipCode(String currZipCode) {
        this.currZipCode = currZipCode;
    }

    public String getWorkUnit() {
        return workUnit;
    }

    public void setWorkUnit(String workUnit) {
        this.workUnit = workUnit;
    }

    public String getWorkUnitPhone() {
        return workUnitPhone;
    }

    public void setWorkUnitPhone(String workUnitPhone) {
        this.workUnitPhone = workUnitPhone;
    }

    public String getResideAddrProv() {
        return resideAddrProv;
    }

    public void setResideAddrProv(String resideAddrProv) {
        this.resideAddrProv = resideAddrProv;
    }

    public String getResideAddrCity() {
        return resideAddrCity;
    }

    public void setResideAddrCity(String resideAddrCity) {
        this.resideAddrCity = resideAddrCity;
    }

    public String getResideAddrCounty() {
        return resideAddrCounty;
    }

    public void setResideAddrCounty(String resideAddrCounty) {
        this.resideAddrCounty = resideAddrCounty;
    }

    public String getResideZipCode() {
        return resideZipCode;
    }

    public void setResideZipCode(String resideZipCode) {
        this.resideZipCode = resideZipCode;
    }

    public String getWorkUnitZipCode() {
        return workUnitZipCode;
    }

    public void setWorkUnitZipCode(String workUnitZipCode) {
        this.workUnitZipCode = workUnitZipCode;
    }

    public String getContactName() {
        return contactName;
    }

    public void setContactName(String contactName) {
        this.contactName = contactName;
    }

    public String getContactRelatCode() {
        return contactRelatCode;
    }

    public void setContactRelatCode(String contactRelatCode) {
        this.contactRelatCode = contactRelatCode;
    }

    public String getContactRelatName() {
        return contactRelatName;
    }

    public void setContactRelatName(String contactRelatName) {
        this.contactRelatName = contactRelatName;
    }

    public String getContactAddr() {
        return contactAddr;
    }

    public void setContactAddr(String contactAddr) {
        this.contactAddr = contactAddr;
    }

    public String getContactPhone() {
        return contactPhone;
    }

    public void setContactPhone(String contactPhone) {
        this.contactPhone = contactPhone;
    }

    public String getAdmitPathCode() {
        return admitPathCode;
    }

    public void setAdmitPathCode(String admitPathCode) {
        this.admitPathCode = admitPathCode;
    }

    public String getAdmitPathName() {
        return admitPathName;
    }

    public void setAdmitPathName(String admitPathName) {
        this.admitPathName = admitPathName;
    }

    public Date getAdmitTime() {
        return admitTime;
    }

    public void setAdmitTime(Date admitTime) {
        this.admitTime = admitTime;
    }

    public String getPkAdmitDept() {
        return pkAdmitDept;
    }

    public void setPkAdmitDept(String pkAdmitDept) {
        this.pkAdmitDept = pkAdmitDept;
    }

    public String getAdmitDeptName() {
        return admitDeptName;
    }

    public void setAdmitDeptName(String admitDeptName) {
        this.admitDeptName = admitDeptName;
    }

    public String getAdmitWardName() {
        return admitWardName;
    }

    public void setAdmitWardName(String admitWardName) {
        this.admitWardName = admitWardName;
    }

    public String getTransDeptNames() {
        return transDeptNames;
    }

    public void setTransDeptNames(String transDeptNames) {
        this.transDeptNames = transDeptNames;
    }

    public Date getDisTime() {
        return disTime;
    }

    public void setDisTime(Date disTime) {
        this.disTime = disTime;
    }

    public String getPkDeptDis() {
        return pkDeptDis;
    }

    public void setPkDeptDis(String pkDeptDis) {
        this.pkDeptDis = pkDeptDis;
    }

    public String getDisDeptName() {
        return disDeptName;
    }

    public void setDisDeptName(String disDeptName) {
        this.disDeptName = disDeptName;
    }

    public String getPkWardDis() {
        return pkWardDis;
    }

    public void setPkWardDis(String pkWardDis) {
        this.pkWardDis = pkWardDis;
    }

    public String getDisWardName() {
        return disWardName;
    }

    public void setDisWardName(String disWardName) {
        this.disWardName = disWardName;
    }

    public String getPkDiagClinic() {
        return pkDiagClinic;
    }

    public void setPkDiagClinic(String pkDiagClinic) {
        this.pkDiagClinic = pkDiagClinic;
    }


    public String getDiagCodeClinic() {
        return diagCodeClinic;
    }

    public void setDiagCodeClinic(String diagCodeClinic) {
        this.diagCodeClinic = diagCodeClinic;
    }

    public String getPkDiagDis() {
        return pkDiagDis;
    }

    public void setPkDiagDis(String pkDiagDis) {
        this.pkDiagDis = pkDiagDis;
    }

    public String getDiagNameClinic() {
        return diagNameClinic;
    }

    public void setDiagNameClinic(String diagNameClinic) {
        this.diagNameClinic = diagNameClinic;
    }

    public String getDiagCodeDis() {
        return diagCodeDis;
    }

    public void setDiagCodeDis(String diagCodeDis) {
        this.diagCodeDis = diagCodeDis;
    }

    public String getPkDiagExtcIp() {
        return pkDiagExtcIp;
    }

    public void setPkDiagExtcIp(String pkDiagExtcIp) {
        this.pkDiagExtcIp = pkDiagExtcIp;
    }

    public String getDiagNameDis() {
        return diagNameDis;
    }

    public void setDiagNameDis(String diagNameDis) {
        this.diagNameDis = diagNameDis;
    }

    public String getMedRecType() {
        return medRecType;
    }

    public void setMedRecType(String medRecType) {
        this.medRecType = medRecType;
    }

    public String getFlagCp() {
        return flagCp;
    }

    public void setFlagCp(String flagCp) {
        this.flagCp = flagCp;
    }

    public Integer getNumRes() {
        return numRes;
    }

    public void setNumRes(Integer numRes) {
        this.numRes = numRes;
    }

    public Integer getNumSucc() {
        return numSucc;
    }

    public void setNumSucc(Integer numSucc) {
        this.numSucc = numSucc;
    }

    public String getDiagCodeExtcIp() {
        return diagCodeExtcIp;
    }

    public void setDiagCodeExtcIp(String diagCodeExtcIp) {
        this.diagCodeExtcIp = diagCodeExtcIp;
    }

    public String getPkDiagPatho() {
        return pkDiagPatho;
    }

    public void setPkDiagPatho(String pkDiagPatho) {
        this.pkDiagPatho = pkDiagPatho;
    }

    public String getDiagNameExtcIp() {
        return diagNameExtcIp;
    }

    public void setDiagNameExtcIp(String diagNameExtcIp) {
        this.diagNameExtcIp = diagNameExtcIp;
    }

    public String getDiagCodePatho() {
        return diagCodePatho;
    }

    public void setDiagCodePatho(String diagCodePatho) {
        this.diagCodePatho = diagCodePatho;
    }

    public String getDiagNamePatho() {
        return diagNamePatho;
    }

    public void setDiagNamePatho(String diagNamePatho) {
        this.diagNamePatho = diagNamePatho;
    }

    public String getPathoNo() {
        return pathoNo;
    }

    public void setPathoNo(String pathoNo) {
        this.pathoNo = pathoNo;
    }

    public String getFlagDrugAllergy() {
        return flagDrugAllergy;
    }

    public void setFlagDrugAllergy(String flagDrugAllergy) {
        this.flagDrugAllergy = flagDrugAllergy;
    }

    public String getAllergicDrug() {
        return allergicDrug;
    }

    public void setAllergicDrug(String allergicDrug) {
        this.allergicDrug = allergicDrug;
    }

    public String getFlagAutopsy() {
        return flagAutopsy;
    }

    public void setFlagAutopsy(String flagAutopsy) {
        this.flagAutopsy = flagAutopsy;
    }

    public String getBloodCodeAbo() {
        return bloodCodeAbo;
    }

    public void setBloodCodeAbo(String bloodCodeAbo) {
        this.bloodCodeAbo = bloodCodeAbo;
    }

    public String getBloodNameAbo() {
        return bloodNameAbo;
    }

    public void setBloodNameAbo(String bloodNameAbo) {
        this.bloodNameAbo = bloodNameAbo;
    }

    public String getBloodCodeRh() {
        return bloodCodeRh;
    }

    public void setBloodCodeRh(String bloodCodeRh) {
        this.bloodCodeRh = bloodCodeRh;
    }

    public String getBloodNameRh() {
        return bloodNameRh;
    }

    public void setBloodNameRh(String bloodNameRh) {
        this.bloodNameRh = bloodNameRh;
    }

    public String getPkEmpDirector() {
        return pkEmpDirector;
    }

    public void setPkEmpDirector(String pkEmpDirector) {
        this.pkEmpDirector = pkEmpDirector;
    }

    public String getDirectorName() {
        return directorName;
    }

    public void setDirectorName(String directorName) {
        this.directorName = directorName;
    }

    public String getPkEmpConsult() {
        return pkEmpConsult;
    }

    public void setPkEmpConsult(String pkEmpConsult) {
        this.pkEmpConsult = pkEmpConsult;
    }

    public String getConsultName() {
        return consultName;
    }

    public void setConsultName(String consultName) {
        this.consultName = consultName;
    }

    public String getPkEmpRefer() {
        return pkEmpRefer;
    }

    public void setPkEmpRefer(String pkEmpRefer) {
        this.pkEmpRefer = pkEmpRefer;
    }

    public String getReferName() {
        return referName;
    }

    public void setReferName(String referName) {
        this.referName = referName;
    }

    public String getPkEmpNurse() {
        return pkEmpNurse;
    }

    public void setPkEmpNurse(String pkEmpNurse) {
        this.pkEmpNurse = pkEmpNurse;
    }

    public String getNurseName() {
        return nurseName;
    }

    public void setNurseName(String nurseName) {
        this.nurseName = nurseName;
    }

    public String getPkEmpLearn() {
        return pkEmpLearn;
    }

    public void setPkEmpLearn(String pkEmpLearn) {
        this.pkEmpLearn = pkEmpLearn;
    }

    public String getLearnName() {
        return learnName;
    }

    public void setLearnName(String learnName) {
        this.learnName = learnName;
    }

    public String getPkEmpIntern() {
        return pkEmpIntern;
    }

    public void setPkEmpIntern(String pkEmpIntern) {
        this.pkEmpIntern = pkEmpIntern;
    }

    public String getInternName() {
        return internName;
    }

    public void setInternName(String internName) {
        this.internName = internName;
    }

    public String getPkEmpCoder() {
        return pkEmpCoder;
    }

    public void setPkEmpCoder(String pkEmpCoder) {
        this.pkEmpCoder = pkEmpCoder;
    }

    public String getCoderName() {
        return coderName;
    }

    public void setCoderName(String coderName) {
        this.coderName = coderName;
    }

    public String getQualityCode() {
        return qualityCode;
    }

    public void setQualityCode(String qualityCode) {
        this.qualityCode = qualityCode;
    }

    public String getQualityName() {
        return qualityName;
    }

    public void setQualityName(String qualityName) {
        this.qualityName = qualityName;
    }

    public String getPkEmpQcDoc() {
        return pkEmpQcDoc;
    }

    public void setPkEmpQcDoc(String pkEmpQcDoc) {
        this.pkEmpQcDoc = pkEmpQcDoc;
    }

    public String getQcDocName() {
        return qcDocName;
    }

    public void setQcDocName(String qcDocName) {
        this.qcDocName = qcDocName;
    }

    public String getPkEmpQcNurse() {
        return pkEmpQcNurse;
    }

    public void setPkEmpQcNurse(String pkEmpQcNurse) {
        this.pkEmpQcNurse = pkEmpQcNurse;
    }

    public String getQcNurseName() {
        return qcNurseName;
    }

    public void setQcNurseName(String qcNurseName) {
        this.qcNurseName = qcNurseName;
    }

    public Date getQcDate() {
        return qcDate;
    }

    public void setQcDate(Date qcDate) {
        this.qcDate = qcDate;
    }

    public String getLeaveHosCode() {
        return leaveHosCode;
    }

    public void setLeaveHosCode(String leaveHosCode) {
        this.leaveHosCode = leaveHosCode;
    }

    public String getLeaveHosName() {
        return leaveHosName;
    }

    public void setLeaveHosName(String leaveHosName) {
        this.leaveHosName = leaveHosName;
    }

    public String getReceiveOrgCode() {
        return receiveOrgCode;
    }

    public void setReceiveOrgCode(String receiveOrgCode) {
        this.receiveOrgCode = receiveOrgCode;
    }

    public String getReceiveOrgName() {
        return receiveOrgName;
    }

    public void setReceiveOrgName(String receiveOrgName) {
        this.receiveOrgName = receiveOrgName;
    }

    public String getFlagReadmit() {
        return flagReadmit;
    }

    public void setFlagReadmit(String flagReadmit) {
        this.flagReadmit = flagReadmit;
    }

    public String getReadmitPurp() {
        return readmitPurp;
    }

    public void setReadmitPurp(String readmitPurp) {
        this.readmitPurp = readmitPurp;
    }

    public Integer getComaDayBef() {
        return comaDayBef;
    }

    public void setComaDayBef(Integer comaDayBef) {
        this.comaDayBef = comaDayBef;
    }

    public Integer getComaHourBef() {
        return comaHourBef;
    }

    public void setComaHourBef(Integer comaHourBef) {
        this.comaHourBef = comaHourBef;
    }

    public Integer getComaMinBef() {
        return comaMinBef;
    }

    public void setComaMinBef(Integer comaMinBef) {
        this.comaMinBef = comaMinBef;
    }

    public Integer getComaDayAfter() {
        return comaDayAfter;
    }

    public void setComaDayAfter(Integer comaDayAfter) {
        this.comaDayAfter = comaDayAfter;
    }

    public Integer getComaHourAfter() {
        return comaHourAfter;
    }

    public void setComaHourAfter(Integer comaHourAfter) {
        this.comaHourAfter = comaHourAfter;
    }

    public Integer getComaMinAfter() {
        return comaMinAfter;
    }

    public void setComaMinAfter(Integer comaMinAfter) {
        this.comaMinAfter = comaMinAfter;
    }

    public BigDecimal getTotalCost() {
        return totalCost;
    }

    public void setTotalCost(BigDecimal totalCost) {
        this.totalCost = totalCost;
    }

    public BigDecimal getSelfCost() {
        return selfCost;
    }

    public void setSelfCost(BigDecimal selfCost) {
        this.selfCost = selfCost;
    }

    public String getDelFlag() {
        return delFlag;
    }

    public void setDelFlag(String delFlag) {
        this.delFlag = delFlag;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getPkAdmitWard() {
        return pkAdmitWard;
    }

    public void setPkAdmitWard(String pkAdmitWard) {
        this.pkAdmitWard = pkAdmitWard;
    }

    public String getPkEmpChief() {
        return pkEmpChief;
    }

    public void setPkEmpChief(String pkEmpChief) {
        this.pkEmpChief = pkEmpChief;
    }

    public String getChiefName() {
        return chiefName;
    }

    public void setChiefName(String chiefName) {
        this.chiefName = chiefName;
    }

    public Date getTs() {
        return ts;
    }

    public void setTs(Date ts) {
        this.ts = ts;
    }

    public String getPkEmpClinic() {
        return pkEmpClinic;
    }

    public void setPkEmpClinic(String pkEmpClinic) {
        this.pkEmpClinic = pkEmpClinic;
    }

    public String getClinicName() {
        return clinicName;
    }

    public void setClinicName(String clinicName) {
        this.clinicName = clinicName;
    }

    public String getCurrAddr() {
        return currAddr;
    }

    public void setCurrAddr(String currAddr) {
        this.currAddr = currAddr;
    }

    public String getResideAddr() {
        return resideAddr;
    }

    public void setResideAddr(String resideAddr) {
        this.resideAddr = resideAddr;
    }

    public String getAdmitCondCode() {
        return admitCondCode;
    }

    public void setAdmitCondCode(String admitCondCode) {
        this.admitCondCode = admitCondCode;
    }

    public String getAdmitCondName() {
        return admitCondName;
    }

    public void setAdmitCondName(String admitCondName) {
        this.admitCondName = admitCondName;
    }

    public String getBirthAddr() {
        return birthAddr;
    }

    public void setBirthAddr(String birthAddr) {
        this.birthAddr = birthAddr;
    }

    public String getOriginAddr() {
        return originAddr;
    }

    public void setOriginAddr(String originAddr) {
        this.originAddr = originAddr;
    }

    public String getCurrAddrDt() {
        return currAddrDt;
    }

    public void setCurrAddrDt(String currAddrDt) {
        this.currAddrDt = currAddrDt;
    }

    public String getResideAddrDt() {
        return resideAddrDt;
    }

    public void setResideAddrDt(String resideAddrDt) {
        this.resideAddrDt = resideAddrDt;
    }

    public String getDiagFitCodeOi() {
        return diagFitCodeOi;
    }

    public void setDiagFitCodeOi(String diagFitCodeOi) {
        this.diagFitCodeOi = diagFitCodeOi;
    }

    public String getDiagFitCodeCp() {
        return diagFitCodeCp;
    }

    public void setDiagFitCodeCp(String diagFitCodeCp) {
        this.diagFitCodeCp = diagFitCodeCp;
    }

    public String getDiagCodeClinicIcd() {
        return diagCodeClinicIcd;
    }

    public void setDiagCodeClinicIcd(String diagCodeClinicIcd) {
        this.diagCodeClinicIcd = diagCodeClinicIcd;
    }

    public String getDiagNameClinicIcd() {
        return diagNameClinicIcd;
    }

    public void setDiagNameClinicIcd(String diagNameClinicIcd) {
        this.diagNameClinicIcd = diagNameClinicIcd;
    }

    public String getDiagCodeExtcIpIcd() {
        return diagCodeExtcIpIcd;
    }

    public void setDiagCodeExtcIpIcd(String diagCodeExtcIpIcd) {
        this.diagCodeExtcIpIcd = diagCodeExtcIpIcd;
    }

    public String getDiagNameExtcIpIcd() {
        return diagNameExtcIpIcd;
    }

    public void setDiagNameExtcIpIcd(String diagNameExtcIpIcd) {
        this.diagNameExtcIpIcd = diagNameExtcIpIcd;
    }

    public String getDiagCodePathoIcd() {
        return diagCodePathoIcd;
    }

    public void setDiagCodePathoIcd(String diagCodePathoIcd) {
        this.diagCodePathoIcd = diagCodePathoIcd;
    }

    public String getDiagNamePathoIcd() {
        return diagNamePathoIcd;
    }

    public void setDiagNamePathoIcd(String diagNamePathoIcd) {
        this.diagNamePathoIcd = diagNamePathoIcd;
    }

    public String getPathoNoIcd() {
        return pathoNoIcd;
    }

    public void setPathoNoIcd(String pathoNoIcd) {
        this.pathoNoIcd = pathoNoIcd;
    }

    public String getFlagDrugAllergyIcd() {
        return flagDrugAllergyIcd;
    }

    public void setFlagDrugAllergyIcd(String flagDrugAllergyIcd) {
        this.flagDrugAllergyIcd = flagDrugAllergyIcd;
    }

    public String getAllergicDrugIcd() {
        return allergicDrugIcd;
    }

    public void setAllergicDrugIcd(String allergicDrugIcd) {
        this.allergicDrugIcd = allergicDrugIcd;
    }

    public String getPartDisease() {
        return partDisease;
    }

    public void setPartDisease(String partDisease) {
        this.partDisease = partDisease;
    }

    public String getPatSource() {
        return patSource;
    }

    public void setPatSource(String patSource) {
        this.patSource = patSource;
    }

    public String getCodeDcdtClinic() {
        return codeDcdtClinic;
    }

    public void setCodeDcdtClinic(String codeDcdtClinic) {
        this.codeDcdtClinic = codeDcdtClinic;
    }

    public String getDescBodypart() {
        return descBodypart;
    }

    public void setDescBodypart(String descBodypart) {
        this.descBodypart = descBodypart;
    }

    public String getDescDrgprop() {
        return descDrgprop;
    }

    public void setDescDrgprop(String descDrgprop) {
        this.descDrgprop = descDrgprop;
    }

    public String getDescDiag() {
        return descDiag;
    }

    public void setDescDiag(String descDiag) {
        this.descDiag = descDiag;
    }

    public Integer getDaysGdi() {
        return daysGdi;
    }

    public void setDaysGdi(Integer daysGdi) {
        this.daysGdi = daysGdi;
    }

    public Integer getDaysGdii() {
        return daysGdii;
    }

    public void setDaysGdii(Integer daysGdii) {
        this.daysGdii = daysGdii;
    }

    public Integer getDaysGdiii() {
        return daysGdiii;
    }

    public void setDaysGdiii(Integer daysGdiii) {
        this.daysGdiii = daysGdiii;
    }

    public Integer getDaysGds() {
        return daysGds;
    }

    public void setDaysGds(Integer daysGds) {
        this.daysGds = daysGds;
    }

    public Integer getHoursGds() {
        return hoursGds;
    }

    public void setHoursGds(Integer hoursGds) {
        this.hoursGds = hoursGds;
    }

    public Integer getHoursBm() {
        return hoursBm;
    }

    public void setHoursBm(Integer hoursBm) {
        this.hoursBm = hoursBm;
    }

    public Integer getMinutesBm() {
        return minutesBm;
    }

    public void setMinutesBm(Integer minutesBm) {
        this.minutesBm = minutesBm;
    }

    public BigDecimal getValAdlin() {
        return valAdlin;
    }

    public void setValAdlin(BigDecimal valAdlin) {
        this.valAdlin = valAdlin;
    }

    public BigDecimal getValAdlout() {
        return valAdlout;
    }

    public void setValAdlout(BigDecimal valAdlout) {
        this.valAdlout = valAdlout;
    }

    public String getDtDrgcasetype() {
        return dtDrgcasetype;
    }

    public void setDtDrgcasetype(String dtDrgcasetype) {
        this.dtDrgcasetype = dtDrgcasetype;
    }

    public String getCurrAddrIcd() {
        return currAddrIcd;
    }

    public void setCurrAddrIcd(String currAddrIcd) {
        this.currAddrIcd = currAddrIcd;
    }

    public String getBirthAddrCode() {
        return birthAddrCode;
    }

    public void setBirthAddrCode(String birthAddrCode) {
        this.birthAddrCode = birthAddrCode;
    }

    public String getOriginAddrCode() {
        return originAddrCode;
    }

    public void setOriginAddrCode(String originAddrCode) {
        this.originAddrCode = originAddrCode;
    }

    public String getCurrAddrCode() {
        return currAddrCode;
    }

    public void setCurrAddrCode(String currAddrCode) {
        this.currAddrCode = currAddrCode;
    }

    public String getResideAddrCode() {
        return resideAddrCode;
    }

    public void setResideAddrCode(String resideAddrCode) {
        this.resideAddrCode = resideAddrCode;
    }

    public BigDecimal getAgeYear() {
        return ageYear;
    }

    public void setAgeYear(BigDecimal ageYear) {
        this.ageYear = ageYear;
    }

	public BigDecimal getNewbornWeight() {
		return newbornWeight;
	}

	public void setNewbornWeight(BigDecimal newbornWeight) {
		this.newbornWeight = newbornWeight;
	}

	public BigDecimal getNewbornInWeight() {
		return newbornInWeight;
	}

	public void setNewbornInWeight(BigDecimal newbornInWeight) {
		this.newbornInWeight = newbornInWeight;
	}

	public BigDecimal getInHosDays() {
        return inHosDays;
    }

    public void setInHosDays(BigDecimal inHosDays) {
        this.inHosDays = inHosDays;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getPageType() {
        return pageType;
    }

    public void setPageType(String pageType) {
        this.pageType = pageType;
    }

    public String getCodePi() {
        return codePi;
    }

    public void setCodePi(String codePi) {
        this.codePi = codePi;
    }
    
    public String getDiagFitOps() {
		return diagFitOps;
	}

	public void setDiagFitOps(String diagFitOps) {
		this.diagFitOps = diagFitOps;
	}

	public List<EmrHomePageOps> getOpsHandle() {
		return opsHandle;
	}

	public void setOpsHandle(List<EmrHomePageOps> opsHandle) {
		this.opsHandle = opsHandle;
	}

	public String getCodedNotCnfrm() {
		return codedNotCnfrm;
	}

	public void setCodedNotCnfrm(String codedNotCnfrm) {
		this.codedNotCnfrm = codedNotCnfrm;
	}
	
}