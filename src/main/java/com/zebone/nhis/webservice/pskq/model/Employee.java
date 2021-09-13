package com.zebone.nhis.webservice.pskq.model;

import com.zebone.nhis.webservice.pskq.annotation.MetadataDescribe;

public class Employee {

    @MetadataDescribe(id= "LHDE0004001",name = "员工主索引",eName = "EMPLOYEE_ID")
    private String employeeId;

    @MetadataDescribe(id= "LHDE0004002",name = "员工主键",eName = "PK_EMPLOYEE")
    private String pkEmployee;

    @MetadataDescribe(id= "LHDE0004003",name = "员工工号",eName = "EMPLOYE_CODE")
    private String employeCode;

    @MetadataDescribe(id= "LHDE0004004",name = "员工姓名",eName = "EMPLOYE_NAME")
    private String employeName;

    @MetadataDescribe(id= "LHDE0004005",name = "性别代码",eName = "GENDER_CODE")
    private String genderCode;

    @MetadataDescribe(id= "LHDE0004006",name = "性别名称",eName = "GENDER_NAME")
    private String genderName;

    @MetadataDescribe(id= "LHDE0004007",name = "出生日期",eName = "DATE_OF_BIRTH")
    private String dateOfBirth;

    @MetadataDescribe(id= "LHDE0004008",name = "证件类型代码",eName = "ID_TYPE_CODE")
    private String idTypeCode;

    @MetadataDescribe(id= "LHDE0004009",name = "证件类型名称",eName = "ID_TYPE_NAME")
    private String idTypeName;

    @MetadataDescribe(id= "LHDE0004010",name = "证件号",eName = "ID_NO")
    private String idNo;
    @MetadataDescribe(id= "LHDE0004011",name = "国籍代码",eName = "NATIONALITY_CODE")
    private String nationalityCode;
    @MetadataDescribe(id= "LHDE0004012",name = "国籍名称",eName = "NATIONALITY_NAME")
    private String nationalityName;
    @MetadataDescribe(id= "LHDE0004013",name = "民族代码",eName = "ETHNIC_CODE")
    private String ethnicCode;
    @MetadataDescribe(id= "LHDE0004014",name = "民族名称",eName = "ETHNIC_NAME")
    private String ethnicName;
    @MetadataDescribe(id= "LHDE0004015",name = "婚姻状况代码",eName = "MARITAL_STATUS_CODE")
    private String maritalStatusCode;
    @MetadataDescribe(id= "LHDE0004016",name = "婚姻状况名称",eName = "MARITAL_STATUS_NAME")
    private String maritalStatusName;
    @MetadataDescribe(id= "LHDE0004017",name = "学历代码",eName = "EDUCATION_CODE")
    private String educationCode;
    @MetadataDescribe(id= "LHDE0004018",name = "学历名称",eName = "EDUCATION_NAME")
    private String educationName;
    @MetadataDescribe(id= "LHDE0004019",name = "学位代码",eName = "DEGREE_CODE")
    private String degreeCode;
    @MetadataDescribe(id= "LHDE0004020",name = "学位名称",eName = "DEGREE_NAME")
    private String degreeName;
    @MetadataDescribe(id= "LHDE0004021",name = "政治面貌代码",eName = "POLITICAL_CODE")
    private String politicalCode;
    @MetadataDescribe(id= "LHDE0004022",name = "政治面貌名称",eName = "POLITICAL_NAME")
    private String politicalName;
    @MetadataDescribe(id= "LHDE0004023",name = "出生地-省(自治区、直辖市)",eName = "BIRTH_PLACE_PROVINCE")
    private String birthPlaceProvince;
    @MetadataDescribe(id= "LHDE0004024",name = "出生地-市(地区、州)",eName = "BIRTH_PLACE_CITY")
    private String birthPlaceCity;
    @MetadataDescribe(id= "LHDE0004025",name = "出生地-县(区)",eName = "BIRTH_PLACE_COUNTY")
    private String birthPlaceCounty;
    @MetadataDescribe(id= "LHDE0004026",name = "出生地",eName = "BIRTH_PLACE")
    private String birthPlace;
    @MetadataDescribe(id= "LHDE0004027",name = "籍贯-省(自治区、直辖市)",eName = "NATIVE_PLACE_PROVINCE")
    private String nativePlaceProvince;
    @MetadataDescribe(id= "LHDE0004028",name = "籍贯-市(地区、州)",eName = "NATIVE_PLACE_CITY")
    private String nativePlaceCity;
    @MetadataDescribe(id= "LHDE0004029",name = "籍贯",eName = "NATIVE_PLACE")
    private String nativePlace;
    @MetadataDescribe(id= "LHDE0004030",name = "家庭电话",eName = "HOME_PHONE_NO")
    private String homePhoneNo;
    @MetadataDescribe(id= "LHDE0004031",name = "现住址-省(自治区、直辖市)",eName = "PRESENT_ADDRESS_PROVINCE")
    private String presentAddressProvince;
    @MetadataDescribe(id= "LHDE0004032",name = "现住址-市(地区、州)",eName = "PRESENT_ADDRESS_CITY")
    private String presentAddressCity;
    @MetadataDescribe(id= "LHDE0004033",name = "现住址-县(区)",eName = "PRESENT_ADDRESS_COUNTY")
    private String presentAddressCounty;
    @MetadataDescribe(id= "LHDE0004034",name = "现住址-乡(镇、街道办事处)",eName = "PRESENT_ADDRESS_COUNTRY")
    private String presentAddressCountry;
    @MetadataDescribe(id= "LHDE0004035",name = "现住址-村(街、路、弄等)",eName = "PRESENT_ADDRESS_VILLAGE")
    private String presentAddressVillage;
    @MetadataDescribe(id= "LHDE0004036",name = "现住址-门牌号码",eName = "PRESENT_ADDRESS_HOUSE_NO")
    private String presentAddressHouseNo;
    @MetadataDescribe(id= "LHDE0004037",name = "现住址",eName = "PRESENT_ADDRESS")
    private String presentAddress;
    @MetadataDescribe(id= "LHDE0004038",name = "现住址（邮编）",eName = "PRESENT_ADDRESS_POSTAL_CODE")
    private String presentAddressPostalCode;
    @MetadataDescribe(id= "LHDE0004039",name = "联系电话",eName = "PHONE_NO")
    private String phoneNo;
    @MetadataDescribe(id= "LHDE0004040",name = "工作单位电话",eName = "WORK_PHONE_NO")
    private String workPhoneNo;
    @MetadataDescribe(id= "LHDE0004041",name = "职称代码",eName = "TITLE_CODE")
    private String titleCode;
    @MetadataDescribe(id= "LHDE0004042",name = "职称名称",eName = "TITLE_NAME")
    private String titleName;
    @MetadataDescribe(id= "LHDE0004043",name = "职务代码",eName = "JOB_CODE")
    private String jobCode;
    @MetadataDescribe(id= "LHDE0004044",name = "职务名称",eName = "JOB_NAME")
    private String jobName;
    @MetadataDescribe(id= "LHDE0004045",name = "职位代码（岗位）",eName = "POSITION_CODE")
    private String positionCode;
    @MetadataDescribe(id= "LHDE0004046",name = "职位名称（岗位）",eName = "POSITION_NAME")
    private String positionName;
    @MetadataDescribe(id= "LHDE0004047",name = "职位类别代码",eName = "POSITION_TYPE_CODE")
    private String positionTypeCode;
    @MetadataDescribe(id= "LHDE0004048",name = "职位类别名称",eName = "POSITION_TYPE_NAME")
    private String positionTypeName;
    @MetadataDescribe(id= "LHDE0004049",name = "岗位类别代码",eName = "POSTTION_CATEGORY_CODE")
    private String posttionCategoryCode;
    @MetadataDescribe(id= "LHDE0004050",name = "岗位类别名称",eName = "POSTTION_CATEGORY_NAME")
    private String posttionCategoryName;
    @MetadataDescribe(id= "LHDE0004051",name = "岗位等级代码",eName = "POSTTION_GRADE_CODE")
    private String posttionGradeCode;
    @MetadataDescribe(id= "LHDE0004052",name = "岗位等级名称",eName = "POSTTION_GRADE_NAME")
    private String posttionGradeName;
    @MetadataDescribe(id= "LHDE0004053",name = "邮箱",eName = "EMAIL")
    private String email;
    @MetadataDescribe(id= "LHDE0004054",name = "用人形式代码",eName = "EMPLOYE_CLASS_CODE")
    private String employeClassCode;
    @MetadataDescribe(id= "LHDE0004055",name = "用人形式名称",eName = "EMPLOYE_CLASS_NAME")
    private String employeClassName;
    @MetadataDescribe(id= "LHDE0004056",name = "科室ID",eName = "DEPT_ID")
    private String deptId;
    @MetadataDescribe(id= "LHDE0004057",name = "科室名称",eName = "DEPT_NAME")
    private String deptName;
    @MetadataDescribe(id= "LHDE0004058",name = "排序码",eName = "SORT_NO")
    private String sortNo;
    @MetadataDescribe(id= "LHDE0004059",name = "拼音简码",eName = "SPELL_CODE")
    private String spellCode;
    @MetadataDescribe(id= "LHDE0004060",name = "五笔简码",eName = "WB_CODE")
    private String wbCode;
    @MetadataDescribe(id= "LHDE0004061",name = "自定义简码",eName = "CUSTOM_CODE")
    private String customCode;
    @MetadataDescribe(id= "LHDE0004062",name = "备注说明",eName = "CMMT")
    private String cmmt;
    @MetadataDescribe(id= "LHDE0004063",name = "有效性标志",eName = "VALID_STATE")
    private String validState;
    @MetadataDescribe(id= "LHDE0004064",name = "机构/院部代码",eName = "ORG_CODE")
    private String orgCode;
    @MetadataDescribe(id= "LHDE0004065",name = "机构/院部名称",eName = "ORG_NAME")
    private String orgName;
    @MetadataDescribe(id= "LHDE0004066",name = "源系统代码",eName = "SOURCE_SYSTEM_CODE")
    private String sourceSystemCode;
    @MetadataDescribe(id= "LHDE0004067",name = "录入人ID",eName = "ENTER_OPERA_ID")
    private String enterOperaId;
    @MetadataDescribe(id= "LHDE0004068",name = "录入人姓名",eName = "ENTER_OPERA_NAME")
    private String enterOperaName;
    @MetadataDescribe(id= "LHDE0004069",name = "录入日期时间",eName = "ENTER_DATE_TIME")
    private String enterDateTime;
    @MetadataDescribe(id= "LHDE0004070",name = "修改人ID",eName = "MODIFY_OPERA_ID")
    private String modifyOperaId;
    @MetadataDescribe(id= "LHDE0004071",name = "修改人姓名",eName = "MODIFY_OPERA_NAME")
    private String modifyOperaName;
    @MetadataDescribe(id= "LHDE0004072",name = "修改日期",eName = "MODIFY_DATE_TIME")
    private String modifyDateTime;
    @MetadataDescribe(id= "LHDE0004073",name = "个人专长",eName = "EXPERTISE")
    private String expertise;
    @MetadataDescribe(id= "LHDE0004074",name = "个人简介",eName = "INTRODUCTION")
    private String introduction;

    public String getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(String employeeId) {
        this.employeeId = employeeId;
    }

    public String getPkEmployee() {
        return pkEmployee;
    }

    public void setPkEmployee(String pkEmployee) {
        this.pkEmployee = pkEmployee;
    }

    public String getEmployeCode() {
        return employeCode;
    }

    public void setEmployeCode(String employeCode) {
        this.employeCode = employeCode;
    }

    public String getEmployeName() {
        return employeName;
    }

    public void setEmployeName(String employeName) {
        this.employeName = employeName;
    }

    public String getGenderCode() {
        return genderCode;
    }

    public void setGenderCode(String genderCode) {
        this.genderCode = genderCode;
    }

    public String getGenderName() {
        return genderName;
    }

    public void setGenderName(String genderName) {
        this.genderName = genderName;
    }

    public String getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(String dateOfBirth) {
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

    public String getDegreeCode() {
        return degreeCode;
    }

    public void setDegreeCode(String degreeCode) {
        this.degreeCode = degreeCode;
    }

    public String getDegreeName() {
        return degreeName;
    }

    public void setDegreeName(String degreeName) {
        this.degreeName = degreeName;
    }

    public String getPoliticalCode() {
        return politicalCode;
    }

    public void setPoliticalCode(String politicalCode) {
        this.politicalCode = politicalCode;
    }

    public String getPoliticalName() {
        return politicalName;
    }

    public void setPoliticalName(String politicalName) {
        this.politicalName = politicalName;
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

    public String getWorkPhoneNo() {
        return workPhoneNo;
    }

    public void setWorkPhoneNo(String workPhoneNo) {
        this.workPhoneNo = workPhoneNo;
    }

    public String getTitleCode() {
        return titleCode;
    }

    public void setTitleCode(String titleCode) {
        this.titleCode = titleCode;
    }

    public String getTitleName() {
        return titleName;
    }

    public void setTitleName(String titleName) {
        this.titleName = titleName;
    }

    public String getJobCode() {
        return jobCode;
    }

    public void setJobCode(String jobCode) {
        this.jobCode = jobCode;
    }

    public String getJobName() {
        return jobName;
    }

    public void setJobName(String jobName) {
        this.jobName = jobName;
    }

    public String getPositionCode() {
        return positionCode;
    }

    public void setPositionCode(String positionCode) {
        this.positionCode = positionCode;
    }

    public String getPositionName() {
        return positionName;
    }

    public void setPositionName(String positionName) {
        this.positionName = positionName;
    }

    public String getPositionTypeCode() {
        return positionTypeCode;
    }

    public void setPositionTypeCode(String positionTypeCode) {
        this.positionTypeCode = positionTypeCode;
    }

    public String getPositionTypeName() {
        return positionTypeName;
    }

    public void setPositionTypeName(String positionTypeName) {
        this.positionTypeName = positionTypeName;
    }

    public String getPosttionCategoryCode() {
        return posttionCategoryCode;
    }

    public void setPosttionCategoryCode(String posttionCategoryCode) {
        this.posttionCategoryCode = posttionCategoryCode;
    }

    public String getPosttionCategoryName() {
        return posttionCategoryName;
    }

    public void setPosttionCategoryName(String posttionCategoryName) {
        this.posttionCategoryName = posttionCategoryName;
    }

    public String getPosttionGradeCode() {
        return posttionGradeCode;
    }

    public void setPosttionGradeCode(String posttionGradeCode) {
        this.posttionGradeCode = posttionGradeCode;
    }

    public String getPosttionGradeName() {
        return posttionGradeName;
    }

    public void setPosttionGradeName(String posttionGradeName) {
        this.posttionGradeName = posttionGradeName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getEmployeClassCode() {
        return employeClassCode;
    }

    public void setEmployeClassCode(String employeClassCode) {
        this.employeClassCode = employeClassCode;
    }

    public String getEmployeClassName() {
        return employeClassName;
    }

    public void setEmployeClassName(String employeClassName) {
        this.employeClassName = employeClassName;
    }

    public String getDeptId() {
        return deptId;
    }

    public void setDeptId(String deptId) {
        this.deptId = deptId;
    }

    public String getDeptName() {
        return deptName;
    }

    public void setDeptName(String deptName) {
        this.deptName = deptName;
    }

    public String getSortNo() {
        return sortNo;
    }

    public void setSortNo(String sortNo) {
        this.sortNo = sortNo;
    }

    public String getSpellCode() {
        return spellCode;
    }

    public void setSpellCode(String spellCode) {
        this.spellCode = spellCode;
    }

    public String getWbCode() {
        return wbCode;
    }

    public void setWbCode(String wbCode) {
        this.wbCode = wbCode;
    }

    public String getCustomCode() {
        return customCode;
    }

    public void setCustomCode(String customCode) {
        this.customCode = customCode;
    }

    public String getCmmt() {
        return cmmt;
    }

    public void setCmmt(String cmmt) {
        this.cmmt = cmmt;
    }

    public String getValidState() {
        return validState;
    }

    public void setValidState(String validState) {
        this.validState = validState;
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

    public String getEnterDateTime() {
        return enterDateTime;
    }

    public void setEnterDateTime(String enterDateTime) {
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

    public String getModifyDateTime() {
        return modifyDateTime;
    }

    public void setModifyDateTime(String modifyDateTime) {
        this.modifyDateTime = modifyDateTime;
    }

    public String getExpertise() {
        return expertise;
    }

    public void setExpertise(String expertise) {
        this.expertise = expertise;
    }

    public String getIntroduction() {
        return introduction;
    }

    public void setIntroduction(String introduction) {
        this.introduction = introduction;
    }
}
