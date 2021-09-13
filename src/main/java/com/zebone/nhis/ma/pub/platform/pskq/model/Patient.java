package com.zebone.nhis.ma.pub.platform.pskq.model;

import java.util.Date;

import com.alibaba.fastjson.annotation.JSONField;
import com.zebone.nhis.ma.pub.platform.pskq.annotation.CvIdType;
import com.zebone.nhis.ma.pub.platform.pskq.annotation.GbMarriage;
import com.zebone.nhis.ma.pub.platform.pskq.annotation.MetadataDescribe;
import com.zebone.nhis.ma.pub.platform.pskq.annotation.SourceSystemCode;

/**
 * @author lijin
 * 患者信息
 */
public class Patient {


    @MetadataDescribe(id= "LHDE0019001",name = "患者主索引号码",eName = "EMPI_ID")
    private String empiId;


    @MetadataDescribe(id= "LHDE0019002",name = "患者主键",eName = "PK_PATIENT")
    private String pkPatient;

    /**
     * 电子健康卡号
     * （规则：院部代码_系统代码_患者编号）
     */

    @MetadataDescribe(id= "LHDE0019003",name = "电子健康卡号",eName = "EHEALTH_CARD_NO")
    private String ehealthCardNo;

    /**
     * 居民健康卡号
     * 指患者持有的“中华人民共和国健康卡”的编号
     */

    @MetadataDescribe(id= "LHDE0019004",name = "居民健康卡号",eName = "HEALTH_CARD_NO")
    private String healthCardNo;

    /**
     * 病人姓名
     */

    @MetadataDescribe(id= "LHDE0019005",name = "病人姓名",eName = "PATIENT_NAME")
    private String patientName;

    /**
     * 性别代码
     */

    @MetadataDescribe(id= "LHDE0019006",name = "性别代码",eName = "GENDER_CODE")
    private String genderCode;

    @MetadataDescribe(id= "LHDE0019007",name = "性别名称",eName = "GENDER_NAME")
    private String genderName;

    @JSONField(format="yyyyMMdd")
    @MetadataDescribe(id= "LHDE0019008",name = "出生日期",eName = "DATE_OF_BIRTH",property = "dateOfBirth")
    private Date dateOfBirth;

    //@CvIdType(value = "code")
    @MetadataDescribe(id= "LHDE0019009",name = "证件类型代码",eName = "ID_TYPE_CODE")
    private String idTypeCode;

    //@CvIdType(value = "name")
    @MetadataDescribe(id= "LHDE0019010",name = "证件类型名称",eName = "ID_TYPE_NAME")
    private String idTypeName;


    @MetadataDescribe(id= "LHDE0019011",name = "证件号",eName = "ID_NO")
    private String idNo;


    @MetadataDescribe(id= "LHDE0019012",name = "医保卡号",eName = "MED_INSURANCE_NO")
    private String medInsuranceNo;


    @MetadataDescribe(id= "LHDE0019013",name = "社保电脑号",eName = "COMPUTER_NO")
    private String computerNo;


    @MetadataDescribe(id= "LHDE0019014",name = "电子社会保障卡号",eName = "E_MED_INSURANCE_NO")
    private String eMedInsuranceNo;


    @MetadataDescribe(id= "LHDE0019015",name = "国籍代码",eName = "NATIONALITY_CODE")
    private String nationalityCode;


    @MetadataDescribe(id= "LHDE0019016",name = "国籍名称",eName = "NATIONALITY_NAME")
    private String nationalityName;


    @MetadataDescribe(id= "LHDE0019017",name = "民族代码",eName = "ETHNIC_CODE")
    private String ethnicCode;


    @MetadataDescribe(id= "LHDE0019018",name = "民族名称",eName = "ETHNIC_NAME")
    private String ethnicName;


    @MetadataDescribe(id= "LHDE0019019",name = "职业代码",eName = "OCCUPATION_CODE")
    private String occupationCode;


    @MetadataDescribe(id= "LHDE0019020",name = "职业名称",eName = "OCCUPATION_NAME")
    private String occupationName;


    @GbMarriage(type = "code")
    @MetadataDescribe(id= "LHDE0019021",name = "婚姻状况代码",eName = "MARITAL_STATUS_CODE")
    private String maritalStatusCode;

    @GbMarriage(type = "name")
    @MetadataDescribe(id= "LHDE0019022",name = "婚姻状况名称",eName = "MARITAL_STATUS_NAME")
    private String maritalStatusName;


    @MetadataDescribe(id= "LHDE0019023",name = "学历代码",eName = "EDUCATION_CODE")
    private String educationCode;


    @MetadataDescribe(id= "LHDE0019024",name = "学历名称",eName = "EDUCATION_NAME")
    private String educationName;


    @MetadataDescribe(id= "LHDE0019025",name = "出生地-省(自治区、直辖市)",eName = "BIRTH_PLACE_PROVINCE")
    private String birthPlaceProvince;


    @MetadataDescribe(id= "LHDE0019026",name = "出生地-市(地区、州)",eName = "BIRTH_PLACE_CITY")
    private String birthPlaceCity;


    @MetadataDescribe(id= "LHDE0019027",name = "出生地-县(区)",eName = "BIRTH_PLACE_COUNTY")
    private String birthPlaceCounty;


    @MetadataDescribe(id= "LHDE0019028",name = "出生地",eName = "BIRTH_PLACE")
    private String birthPlace;


    @MetadataDescribe(id= "LHDE0019029",name = "籍贯-省(自治区、直辖市)",eName = "NATIVE_PLACE_PROVINCE")
    private String nativePlaceProvince;


    @MetadataDescribe(id= "LHDE0019030",name = "籍贯-市(地区、州)",eName = "NATIVE_PLACE_CITY")
    private String nativePlaceCity;


    @MetadataDescribe(id= "LHDE0019031",name = "籍贯",eName = "NATIVE_PLACE")
    private String nativePlace;


    @MetadataDescribe(id= "LHDE0019032",name = "户口地址-省(自治区、直辖市)",eName = "HOUSEHOLD_ADDRESS_PROVINCE")
    private String householdAddressProvince;


    @MetadataDescribe(id= "LHDE0019033",name = "户口地址-市(地区、州)",eName = "HOUSEHOLD_ADDRESS_CITY")
    private String householdAddressCity;


    @MetadataDescribe(id= "LHDE0019034",name = "户口地址-县(区)",eName = "HOUSEHOLD_ADDRESS_COUNTY")
    private String householdAddressCounty;


    @MetadataDescribe(id= "LHDE0019035",name = "户口地址-乡(镇、街道办事处)",eName = "HOUSEHOLD_ADDRESS_COUNTRY")
    private String householdAddressCountry;


    @MetadataDescribe(id= "LHDE0019036",name = "户口地址-村(街、路、弄等)",eName = "HOUSEHOLD_ADDRESS_VILLAGE")
    private String householdAddressVillage;


    @MetadataDescribe(id= "LHDE0019037",name = "户口地址-门牌号码",eName = "HOUSEHOLD_ADDRESS_HOUSE_NO")
    private String householdAddressHouseNo;


    @MetadataDescribe(id= "LHDE0019038",name = "户口地址",eName = "HOUSEHOLD_ADDRESS")
    private String householdAddress;


    @MetadataDescribe(id= "LHDE0019039",name = "户口地址（邮编）",eName = "HOUSEHOLD_ADDRESS_POSTAL_CODE")
    private String householdAddressPostalCode;


    @MetadataDescribe(id= "LHDE0019040",name = "家庭电话",eName = "HOME_PHONE_NO")
    private String homePhoneNo;


    @MetadataDescribe(id= "LHDE0019041",name = "现住址-省(自治区、直辖市)",eName = "PRESENT_ADDRESS_PROVINCE")
    private String presentAddressProvince;


    @MetadataDescribe(id= "LHDE0019042",name = "现住址-市(地区、州)",eName = "PRESENT_ADDRESS_CITY")
    private String presentAddressCity;


    @MetadataDescribe(id= "LHDE0019043",name = "现住址-县(区)",eName = "PRESENT_ADDRESS_COUNTY")
    private String presentAddressCounty;


    @MetadataDescribe(id= "LHDE0019044",name = "现住址-乡(镇、街道办事处)",eName = "PRESENT_ADDRESS_COUNTRY")
    private String presentAddressCountry;


    @MetadataDescribe(id= "LHDE0019045",name = "现住址-村(街、路、弄等)",eName = "PRESENT_ADDRESS_VILLAGE")
    private String presentAddressVillage;


    @MetadataDescribe(id= "LHDE0019046",name = "现住址-门牌号码",eName = "PRESENT_ADDRESS_HOUSE_NO")
    private String presentAddressHouseNo;


    @MetadataDescribe(id= "LHDE0019047",name = "现住址",eName = "PRESENT_ADDRESS")
    private String presentAddress;


    @MetadataDescribe(id= "LHDE0019048",name = "现住址（邮编）",eName = "PRESENT_ADDRESS_POSTAL_CODE")
    private String presentAddressPostalCode;


    @MetadataDescribe(id= "LHDE0019049",name = "联系电话",eName = "PHONE_NO")
    private String phoneNo;


    @MetadataDescribe(id= "LHDE0019050",name = "工作单位名称",eName = "WORK_UNIT_NAME")
    private String workUnitName;


    @MetadataDescribe(id= "LHDE0019051",name = "工作单位-省(自治区、直辖市)",eName = "WORK_ADDRESS_PROVINCE")
    private String workAddressProvince;


    @MetadataDescribe(id= "LHDE0019052",name = "工作单位-市(地区、州)",eName = "WORK_ADDRESS_CITY")
    private String workAddressCity;


    @MetadataDescribe(id= "LHDE0019053",name = "工作单位-县(区)",eName = "WORK_ADDRESS_COUNTY")
    private String workAddressCounty;


    @MetadataDescribe(id= "LHDE0019054",name = "工作单位地址",eName = "WORK_ADDRESS")
    private String workAddress;


    @MetadataDescribe(id= "LHDE0019055",name = "工作单位地址（邮编）",eName = "WORK_ADDRESS_POSTAL_CODE")
    private String workAddressPostalCode;


    @MetadataDescribe(id= "LHDE0019056",name = "工作单位电话",eName = "WORK_PHONE_NO")
    private String workPhoneNo;


    @MetadataDescribe(id= "LHDE0019057",name = "联系人姓名",eName = "CONTACT_NAME")
    private String contactName;


    @MetadataDescribe(id= "LHDE0019058",name = "联系人同本人关系代码",eName = "CONTACT_RELATIONSHIP_CODE")
    private String contactRelationshipCode;


    @MetadataDescribe(id= "LHDE0019059",name = "联系人同本人关系名称",eName = "CONTACT_RELATIONSHIP_NAME")
    private String contactRelationshipName;


    @MetadataDescribe(id= "LHDE0019060",name = "联系人地址",eName = "CONTACT_ADDRESS")
    private String contactAddress;


    @MetadataDescribe(id= "LHDE0019061",name = "联系人地址（邮编）",eName = "CONTACT_ADDRESS_POSTAL_CODE")
    private String contactAddressPostalCode;


    @MetadataDescribe(id= "LHDE0019062",name = "联系人电话",eName = "CONTACT_PHONE_NO")
    private String contactPhoneNo;


    @MetadataDescribe(id= "LHDE0019063",name = "联系人身份证号",eName = "CONTACT_ID_NO")
    private String contactIdNo;


    @MetadataDescribe(id= "LHDE0019064",name = "ABO血型代码",eName = "ABO_BLOOD_TYPE_CODE")
    private String aboBloodTypeCode;


    @MetadataDescribe(id= "LHDE0019065",name = "ABO血型名称",eName = "ABO_BLOOD_TYPE_NAME")
    private String aboBloodTypeName;


    @MetadataDescribe(id= "LHDE0019066",name = "RH血型代码",eName = "RH_BLOOD_TYPE_CODE")
    private String rhBloodTypeCode;


    @MetadataDescribe(id= "LHDE0019067",name = "RH血型名称",eName = "RH_BLOOD_TYPE_NAME")
    private String rhBloodTypeName;


    @MetadataDescribe(id= "LHDE0019068",name = "过敏史",eName = "ALLERGY_DESC")
    private String allergyDesc;

    @MetadataDescribe(id= "LHDE0019069",name = "过敏史标志",eName = "ALLERGY_FLAG")
    private String allergyFlag;

    //无
    @MetadataDescribe(id= "LHDE0019070",name = "家庭医生主索引",eName = "FAMILY_DOCTOR_ID")
    private String familyDoctorId;

   //无
    @MetadataDescribe(id= "LHDE0019071",name = "家庭医生姓名",eName = "FAMILY_DOCTOR_NAME")
    private String familyDoctorName;


    @MetadataDescribe(id= "LHDE0019072",name = "机构/院部代码",eName = "ORG_CODE")
    private String orgCode;


    @MetadataDescribe(id= "LHDE0019073",name = "机构/院部名称",eName = "ORG_NAME")
    private String orgName;

    @SourceSystemCode
    @MetadataDescribe(id= "LHDE0019074",name = "源系统代码",eName = "SOURCE_SYSTEM_CODE")
    private String sourceSystemCode;


    @MetadataDescribe(id= "LHDE0019075",name = "录入人ID",eName = "ENTER_OPERA_ID")
    private String enterOperaId;


    @MetadataDescribe(id= "LHDE0019076",name = "录入人姓名",eName = "ENTER_OPERA_NAME")
    private String enterOperaName;

    @JSONField(format="yyyyMMdd'T'HHmmss")
    @MetadataDescribe(id= "LHDE0019077",name = "录入日期时间",eName = "ENTER_DATE_TIME")
    private Date enterDateTime;


    @MetadataDescribe(id= "LHDE0019078",name = "修改人ID",eName = "MODIFY_OPERA_ID")
    private String modifyOperaId;


    @MetadataDescribe(id= "LHDE0019079",name = "修改人姓名",eName = "MODIFY_OPERA_NAME")
    private String modifyOperaName;

    @JSONField(format="yyyyMMdd'T'HHmmss")
    @MetadataDescribe(id= "LHDE0019080",name = "修改日期",eName = "MODIFY_DATE_TIME")
    private Date modifyDateTime;
    
    @MetadataDescribe(id= "LHDE0019081",name = "患者编号",eName = "PATIENT_ID")
    private String patientId;


    public String getEmpiId() {
        return empiId;
    }


    public void setEmpiId(String empiId) {
        this.empiId = empiId;
    }

    public String getPkPatient() {
        return pkPatient;
    }

    public void setPkPatient(String pkPatient) {
        this.pkPatient = pkPatient;
    }

    public String getEhealthCardNo() {
        return ehealthCardNo;
    }

    public void setEhealthCardNo(String ehealthCardNo) {
        this.ehealthCardNo = ehealthCardNo;
    }

    public String getHealthCardNo() {
        return healthCardNo;
    }

    public void setHealthCardNo(String healthCardNo) {
        this.healthCardNo = healthCardNo;
    }

    public String getPatientName() {
        return patientName;
    }

    public void setPatientName(String patientName) {
        this.patientName = patientName;
    }

    public String getGenderCode() {
        return genderCode;
    }

    public void setGenderCode(String genderCode) {
        switch(genderCode){
            case "02":
                this.genderCode = "1";
                break;
            case "03":
                this.genderCode = "2";
                break;
            default:
                this.genderCode = "9";
                break;
        }
    }
    public String getGenderName() {
        return genderName;
    }

    public void setGenderName(String genderName) {
    	String name = "未说明的性别";
        switch(genderName){
            case "男":
                name = "男性";
                break;
            case "女":
                name = "女性";
                break;
  
        }
        this.genderName = name;
    }

    public Date getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(Date dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getIdTypeCode() {
        return idTypeCode;
    }

    public void setIdTypeCode(String idTypeCode) {
        this.idTypeCode = idTypeCode;
    }

    public String getIdTypeName() {
        return idTypeName;
    }

    public void setIdTypeName(String idTypeName) {
        this.idTypeName = idTypeName;
    }

    public String getIdNo() {
        return idNo;
    }

    public void setIdNo(String idNo) {
        this.idNo = idNo;
    }

    public String getMedInsuranceNo() {
        return medInsuranceNo;
    }

    public void setMedInsuranceNo(String medInsuranceNo) {
        this.medInsuranceNo = medInsuranceNo;
    }

    public String getComputerNo() {
        return computerNo;
    }

    public void setComputerNo(String computerNo) {
        this.computerNo = computerNo;
    }

    public String geteMedInsuranceNo() {
        return eMedInsuranceNo;
    }

    public void seteMedInsuranceNo(String eMedInsuranceNo) {
        this.eMedInsuranceNo = eMedInsuranceNo;
    }

    public String getNationalityCode() {
        return nationalityCode;
    }

    public void setNationalityCode(String nationalityCode) {
        this.nationalityCode = nationalityCode;
    }

    public String getNationalityName() {
        return nationalityName;
    }

    public void setNationalityName(String nationalityName) {
        this.nationalityName = nationalityName;
    }

    public String getEthnicCode() {
        return ethnicCode;
    }

    public void setEthnicCode(String ethnicCode) {
        this.ethnicCode = ethnicCode;
    }

    public String getEthnicName() {
        return ethnicName;
    }

    public void setEthnicName(String ethnicName) {
        this.ethnicName = ethnicName;
    }

    public String getOccupationCode() {
        return occupationCode;
    }

    public void setOccupationCode(String occupationCode) {
        this.occupationCode = occupationCode;
    }

    public String getOccupationName() {
        return occupationName;
    }

    public void setOccupationName(String occupationName) {
        this.occupationName = occupationName;
    }

    public String getMaritalStatusCode() {
        return maritalStatusCode;
    }

    public void setMaritalStatusCode(String maritalStatusCode) {
        this.maritalStatusCode = maritalStatusCode;
    }

    public String getMaritalStatusName() {
        return maritalStatusName;
    }

    public void setMaritalStatusName(String maritalStatusName) {
        this.maritalStatusName = maritalStatusName;
    }

    public String getEducationCode() {
        return educationCode;
    }

    public void setEducationCode(String educationCode) {
        this.educationCode = educationCode;
    }

    public String getEducationName() {
        return educationName;
    }

    public void setEducationName(String educationName) {
        this.educationName = educationName;
    }

    public String getBirthPlaceProvince() {
        return birthPlaceProvince;
    }

    public void setBirthPlaceProvince(String birthPlaceProvince) {
        this.birthPlaceProvince = birthPlaceProvince;
    }

    public String getBirthPlaceCity() {
        return birthPlaceCity;
    }

    public void setBirthPlaceCity(String birthPlaceCity) {
        this.birthPlaceCity = birthPlaceCity;
    }

    public String getBirthPlaceCounty() {
        return birthPlaceCounty;
    }

    public void setBirthPlaceCounty(String birthPlaceCounty) {
        this.birthPlaceCounty = birthPlaceCounty;
    }

    public String getBirthPlace() {
        return birthPlace;
    }

    public void setBirthPlace(String birthPlace) {
        this.birthPlace = birthPlace;
    }

    public String getNativePlaceProvince() {
        return nativePlaceProvince;
    }

    public void setNativePlaceProvince(String nativePlaceProvince) {
        this.nativePlaceProvince = nativePlaceProvince;
    }

    public String getNativePlaceCity() {
        return nativePlaceCity;
    }

    public void setNativePlaceCity(String nativePlaceCity) {
        this.nativePlaceCity = nativePlaceCity;
    }

    public String getNativePlace() {
        return nativePlace;
    }

    public void setNativePlace(String nativePlace) {
        this.nativePlace = nativePlace;
    }

    public String getHouseholdAddressProvince() {
        return householdAddressProvince;
    }

    public void setHouseholdAddressProvince(String householdAddressProvince) {
        this.householdAddressProvince = householdAddressProvince;
    }

    public String getHouseholdAddressCity() {
        return householdAddressCity;
    }

    public void setHouseholdAddressCity(String householdAddressCity) {
        this.householdAddressCity = householdAddressCity;
    }

    public String getHouseholdAddressCounty() {
        return householdAddressCounty;
    }

    public void setHouseholdAddressCounty(String householdAddressCounty) {
        this.householdAddressCounty = householdAddressCounty;
    }

    public String getHouseholdAddressCountry() {
        return householdAddressCountry;
    }

    public void setHouseholdAddressCountry(String householdAddressCountry) {
        this.householdAddressCountry = householdAddressCountry;
    }

    public String getHouseholdAddressVillage() {
        return householdAddressVillage;
    }

    public void setHouseholdAddressVillage(String householdAddressVillage) {
        this.householdAddressVillage = householdAddressVillage;
    }

    public String getHouseholdAddressHouseNo() {
        return householdAddressHouseNo;
    }

    public void setHouseholdAddressHouseNo(String householdAddressHouseNo) {
        this.householdAddressHouseNo = householdAddressHouseNo;
    }

    public String getHouseholdAddress() {
        return householdAddress;
    }

    public void setHouseholdAddress(String householdAddress) {
        this.householdAddress = householdAddress;
    }

    public String getHouseholdAddressPostalCode() {
        return householdAddressPostalCode;
    }

    public void setHouseholdAddressPostalCode(String householdAddressPostalCode) {
        this.householdAddressPostalCode = householdAddressPostalCode;
    }

    public String getHomePhoneNo() {
        return homePhoneNo;
    }

    public void setHomePhoneNo(String homePhoneNo) {
        this.homePhoneNo = homePhoneNo;
    }

    public String getPresentAddressProvince() {
        return presentAddressProvince;
    }

    public void setPresentAddressProvince(String presentAddressProvince) {
        this.presentAddressProvince = presentAddressProvince;
    }

    public String getPresentAddressCity() {
        return presentAddressCity;
    }

    public void setPresentAddressCity(String presentAddressCity) {
        this.presentAddressCity = presentAddressCity;
    }

    public String getPresentAddressCounty() {
        return presentAddressCounty;
    }

    public void setPresentAddressCounty(String presentAddressCounty) {
        this.presentAddressCounty = presentAddressCounty;
    }

    public String getPresentAddressCountry() {
        return presentAddressCountry;
    }

    public void setPresentAddressCountry(String presentAddressCountry) {
        this.presentAddressCountry = presentAddressCountry;
    }

    public String getPresentAddressVillage() {
        return presentAddressVillage;
    }

    public void setPresentAddressVillage(String presentAddressVillage) {
        this.presentAddressVillage = presentAddressVillage;
    }

    public String getPresentAddressHouseNo() {
        return presentAddressHouseNo;
    }

    public void setPresentAddressHouseNo(String presentAddressHouseNo) {
        this.presentAddressHouseNo = presentAddressHouseNo;
    }

    public String getPresentAddress() {
        return presentAddress;
    }

    public void setPresentAddress(String presentAddress) {
        this.presentAddress = presentAddress;
    }

    public String getPresentAddressPostalCode() {
        return presentAddressPostalCode;
    }

    public void setPresentAddressPostalCode(String presentAddressPostalCode) {
        this.presentAddressPostalCode = presentAddressPostalCode;
    }

    public String getPhoneNo() {
        return phoneNo;
    }

    public void setPhoneNo(String phoneNo) {
        this.phoneNo = phoneNo;
    }

    public String getWorkUnitName() {
        return workUnitName;
    }

    public void setWorkUnitName(String workUnitName) {
        this.workUnitName = workUnitName;
    }

    public String getWorkAddressProvince() {
        return workAddressProvince;
    }

    public void setWorkAddressProvince(String workAddressProvince) {
        this.workAddressProvince = workAddressProvince;
    }

    public String getWorkAddressCity() {
        return workAddressCity;
    }

    public void setWorkAddressCity(String workAddressCity) {
        this.workAddressCity = workAddressCity;
    }

    public String getWorkAddressCounty() {
        return workAddressCounty;
    }

    public void setWorkAddressCounty(String workAddressCounty) {
        this.workAddressCounty = workAddressCounty;
    }

    public String getWorkAddress() {
        return workAddress;
    }

    public void setWorkAddress(String workAddress) {
        this.workAddress = workAddress;
    }

    public String getWorkAddressPostalCode() {
        return workAddressPostalCode;
    }

    public void setWorkAddressPostalCode(String workAddressPostalCode) {
        this.workAddressPostalCode = workAddressPostalCode;
    }

    public String getWorkPhoneNo() {
        return workPhoneNo;
    }

    public void setWorkPhoneNo(String workPhoneNo) {
        this.workPhoneNo = workPhoneNo;
    }

    public String getContactName() {
        return contactName;
    }

    public void setContactName(String contactName) {
        this.contactName = contactName;
    }

    public String getContactRelationshipCode() {
        return contactRelationshipCode;
    }

    public void setContactRelationshipCode(String contactRelationshipCode) {
        this.contactRelationshipCode = "";
    }

    public String getContactRelationshipName() {
        return contactRelationshipName;
    }

    public void setContactRelationshipName(String contactRelationshipName) {
        this.contactRelationshipName = "";
    }

    public String getContactAddress() {
        return contactAddress;
    }

    public void setContactAddress(String contactAddress) {
        this.contactAddress = contactAddress;
    }

    public String getContactAddressPostalCode() {
        return contactAddressPostalCode;
    }

    public void setContactAddressPostalCode(String contactAddressPostalCode) {
        this.contactAddressPostalCode = contactAddressPostalCode;
    }

    public String getContactPhoneNo() {
        return contactPhoneNo;
    }

    public void setContactPhoneNo(String contactPhoneNo) {
        this.contactPhoneNo = contactPhoneNo;
    }

    public String getContactIdNo() {
        return contactIdNo;
    }

    public void setContactIdNo(String contactIdNo) {
        this.contactIdNo = contactIdNo;
    }

    public String getAboBloodTypeCode() {
        return aboBloodTypeCode;
    }

    public void setAboBloodTypeCode(String aboBloodTypeCode) {
        this.aboBloodTypeCode = aboBloodTypeCode;
    }

    public String getAboBloodTypeName() {
        return aboBloodTypeName;
    }

    public void setAboBloodTypeName(String aboBloodTypeName) {
        this.aboBloodTypeName = aboBloodTypeName;
    }

    public String getRhBloodTypeCode() {
        return rhBloodTypeCode;
    }

    public void setRhBloodTypeCode(String rhBloodTypeCode) {
        this.rhBloodTypeCode = rhBloodTypeCode;
    }

    public String getRhBloodTypeName() {
        return rhBloodTypeName;
    }

    public void setRhBloodTypeName(String rhBloodTypeName) {
        this.rhBloodTypeName = rhBloodTypeName;
    }

    public String getAllergyDesc() {
        return allergyDesc;
    }

    public void setAllergyDesc(String allergyDesc) {
        this.allergyDesc = allergyDesc;
    }

    public String getAllergyFlag() {
        return allergyFlag;
    }

    public void setAllergyFlag(String allergyFlag) {
        this.allergyFlag = allergyFlag;
    }

    public String getFamilyDoctorId() {
        return familyDoctorId;
    }

    public void setFamilyDoctorId(String familyDoctorId) {
        this.familyDoctorId = familyDoctorId;
    }

    public String getFamilyDoctorName() {
        return familyDoctorName;
    }

    public void setFamilyDoctorName(String familyDoctorName) {
        this.familyDoctorName = familyDoctorName;
    }

    public String getOrgCode() {
        return orgCode;
    }

    public void setOrgCode(String orgCode) {
        this.orgCode = orgCode;
    }

    public String getOrgName() {
        return orgName;
    }

    public void setOrgName(String orgName) {
        this.orgName = orgName;
    }

    public String getSourceSystemCode() {
        return sourceSystemCode;
    }

    public void setSourceSystemCode(String sourceSystemCode) {
        this.sourceSystemCode = sourceSystemCode;
    }

    public String getEnterOperaId() {
        return enterOperaId;
    }

    public void setEnterOperaId(String enterOperaId) {
        this.enterOperaId = enterOperaId;
    }

    public String getEnterOperaName() {
        return enterOperaName;
    }

    public void setEnterOperaName(String enterOperaName) {
        this.enterOperaName = enterOperaName;
    }

    public Date getEnterDateTime() {
        return enterDateTime;
    }

    public void setEnterDateTime(Date enterDateTime) {
        this.enterDateTime = enterDateTime;
    }

    public String getModifyOperaId() {
        return modifyOperaId;
    }

    public void setModifyOperaId(String modifyOperaId) {
        this.modifyOperaId = modifyOperaId;
    }

    public String getModifyOperaName() {
        return modifyOperaName;
    }

    public void setModifyOperaName(String modifyOperaName) {
        this.modifyOperaName = modifyOperaName;
    }

    public Date getModifyDateTime() {
        return modifyDateTime;
    }

    public void setModifyDateTime(Date modifyDateTime) {
        this.modifyDateTime = modifyDateTime;
    }

	public String getPatientId() {
		return patientId;
	}

	public void setPatientId(String patientId) {
		this.patientId = patientId;
	}
    
}
