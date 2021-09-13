package com.zebone.nhis.webservice.pskq.model;

import com.zebone.nhis.webservice.pskq.annotation.MetadataDescribe;

/**
 * 医疗机构信息
 */
public class Organization {

    @MetadataDescribe(id= "LHDE0002001",name = "机构/院部代码",eName = "ORG_CODE")
    private String orgCode;

    @MetadataDescribe(id= "LHDE0002002",name = "机构/院部名称",eName = "ORG_NAME")
    private String orgName;

    @MetadataDescribe(id= "LHDE0002003",name = "集团代码",eName = "GROUP_CODE")
    private String groupCode;

    @MetadataDescribe(id= "LHDE0002004",name = "集团名称",eName = "GROUP_NAME")
    private String groupName;

    @MetadataDescribe(id= "LHDE0002005",name = "医疗机构组织机构代码",eName = "ORGANIZATION_CODE")
    private String organizationCode;

    @MetadataDescribe(id= "LHDE0002006",name = "统一社会信用代码",eName = "UNIFIED_SOCIAL_CREDIT_CODE")
    private String unifiedSocialCreditCode;

    @MetadataDescribe(id= "LHDE0002007",name = "医疗机构类别代码",eName = "ORGANIZATION_CATEGORY_CODE")
    private String organizationCategoryCode;

    @MetadataDescribe(id= "LHDE0002008",name = "医疗机构类别名称",eName = "ORGANIZATION_CATEGORY_NAME")
    private String organizationCategoryName;

    @MetadataDescribe(id= "LHDE0002009",name = "医院级别代码",eName = "HOSPITAL_LEVEL_CODE")
    private String hospitalLevelCode;

    @MetadataDescribe(id= "LHDE0002010",name = "医院级别名称",eName = "HOSPITAL_LEVEL_NAME")
    private String hospitalLevelName;

    @MetadataDescribe(id= "LHDE0002011",name = "医疗服务价格-政府指导价格档次代码",eName = "PRICE_GRADE_CODE")
    private String priceGradeCode;

    @MetadataDescribe(id= "LHDE0002012",name = "医疗服务价格-政府指导价格档次名称",eName = "PRICE_GRADE_NAME")
    private String priceGradeName;

    @MetadataDescribe(id= "LHDE0002013",name = "组织机构联系电话",eName = "ORGANIZATION_PHONE_NO")
    private String organizationPhoneNo;

    @MetadataDescribe(id= "LHDE0002014",name = "医疗机构负责人（法人）姓名",eName = "LEGAL_PERSON_NAME")
    private String legalPersonName;

    @MetadataDescribe(id= "LHDE0002015",name = "医疗机构负责人联系电话",eName = "LEGAL_PERSON_PHONE_NO")
    private String legalPersonPhoneNo;

    @MetadataDescribe(id= "LHDE0002016",name = "地址-省(自治区、直辖市)",eName = "ORGANIZATION_ADDRESS_PROVINCE")
    private String organizationAddressProvince;

    @MetadataDescribe(id= "LHDE0002017",name = "地址-市(地区、州)",eName = "ORGANIZATION_ADDRESS_CITY")
    private String organizationAddressCity;

    @MetadataDescribe(id= "LHDE0002018",name = "地址-县(区)",eName = "ORGANIZATION_ADDRESS_COUNTY")
    private String organizationAddressCounty;

    @MetadataDescribe(id= "LHDE0002019",name = "地址-乡(镇、街道办事处)",eName = "ORGANIZATION_ADDRESS_COUNTRY")
    private String organizationAddressCountry;

    @MetadataDescribe(id= "LHDE0002020",name = "地址-村(街、路、弄等)",eName = "ORGANIZATION_ADDRESS_VILLAGE")
    private String organizationAddressVillage;

    @MetadataDescribe(id= "LHDE0002021",name = "地址-门牌号码",eName = "ORGANIZATION_ADDRESS_HOUSE_NO")
    private String organizationAddressHouseNo;

    @MetadataDescribe(id= "LHDE0002022",name = "地址",eName = "ORGANIZATION_ADDRESS")
    private String organizationAddress;

    @MetadataDescribe(id= "LHDE0002023",name = "邮政编码",eName = "ORGANIZATION_ADDRESS_POSTAL_CODE")
    private String organizationAddressPostalCode;

    @MetadataDescribe(id= "LHDE0002024",name = "排序码",eName = "SORT_NO")
    private String sortNo;

    @MetadataDescribe(id= "LHDE0002025",name = "拼音简码",eName = "SPELL_CODE")
    private String spellCode;

    @MetadataDescribe(id= "LHDE0002026",name = "五笔简码",eName = "WB_CODE")
    private String wbCode;

    @MetadataDescribe(id= "LHDE0002027",name = "自定义简码",eName = "CUSTOM_CODE")
    private String customCode;

    @MetadataDescribe(id= "LHDE0002028",name = "备注说明",eName = "CMMT")
    private String cmmt;

    @MetadataDescribe(id= "LHDE0002029",name = "有效性标志",eName = "VALID_STATE")
    private String validState;

    @MetadataDescribe(id= "LHDE0002030",name = "源系统代码",eName = "SOURCE_SYSTEM_CODE")
    private String sourceSystemCode;

    @MetadataDescribe(id= "LHDE0002031",name = "录入人ID",eName = "ENTER_OPERA_ID")
    private String enterOperaId;

    @MetadataDescribe(id= "LHDE0002032",name = "录入人姓名",eName = "ENTER_OPERA_NAME")
    private String enterOperaName;

    @MetadataDescribe(id= "LHDE0002033",name = "录入日期时间",eName = "ENTER_DATE_TIME")
    private String enterDateTime;

    @MetadataDescribe(id= "LHDE0002034",name = "修改人ID",eName = "MODIFY_OPERA_ID")
    private String modifyOperaId;

    @MetadataDescribe(id= "LHDE0002035",name = "修改人姓名",eName = "MODIFY_OPERA_NAME")
    private String modifyOperaName;

    @MetadataDescribe(id= "LHDE0002036",name = "修改日期",eName = "MODIFY_DATE_TIME")
    private String modifyDateTime;

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

    public String getGroupCode() {
        return groupCode;
    }

    public void setGroupCode(String groupCode) {
        this.groupCode = groupCode;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public String getOrganizationCode() {
        return organizationCode;
    }

    public void setOrganizationCode(String organizationCode) {
        this.organizationCode = organizationCode;
    }

    public String getUnifiedSocialCreditCode() {
        return unifiedSocialCreditCode;
    }

    public void setUnifiedSocialCreditCode(String unifiedSocialCreditCode) {
        this.unifiedSocialCreditCode = unifiedSocialCreditCode;
    }

    public String getOrganizationCategoryCode() {
        return organizationCategoryCode;
    }

    public void setOrganizationCategoryCode(String organizationCategoryCode) {
        this.organizationCategoryCode = organizationCategoryCode;
    }

    public String getOrganizationCategoryName() {
        return organizationCategoryName;
    }

    public void setOrganizationCategoryName(String organizationCategoryName) {
        this.organizationCategoryName = organizationCategoryName;
    }

    public String getHospitalLevelCode() {
        return hospitalLevelCode;
    }

    public void setHospitalLevelCode(String hospitalLevelCode) {
        this.hospitalLevelCode = hospitalLevelCode;
    }

    public String getHospitalLevelName() {
        return hospitalLevelName;
    }

    public void setHospitalLevelName(String hospitalLevelName) {
        this.hospitalLevelName = hospitalLevelName;
    }

    public String getPriceGradeCode() {
        return priceGradeCode;
    }

    public void setPriceGradeCode(String priceGradeCode) {
        this.priceGradeCode = priceGradeCode;
    }

    public String getPriceGradeName() {
        return priceGradeName;
    }

    public void setPriceGradeName(String priceGradeName) {
        this.priceGradeName = priceGradeName;
    }

    public String getOrganizationPhoneNo() {
        return organizationPhoneNo;
    }

    public void setOrganizationPhoneNo(String organizationPhoneNo) {
        this.organizationPhoneNo = organizationPhoneNo;
    }

    public String getLegalPersonName() {
        return legalPersonName;
    }

    public void setLegalPersonName(String legalPersonName) {
        this.legalPersonName = legalPersonName;
    }

    public String getLegalPersonPhoneNo() {
        return legalPersonPhoneNo;
    }

    public void setLegalPersonPhoneNo(String legalPersonPhoneNo) {
        this.legalPersonPhoneNo = legalPersonPhoneNo;
    }

    public String getOrganizationAddressProvince() {
        return organizationAddressProvince;
    }

    public void setOrganizationAddressProvince(String organizationAddressProvince) {
        this.organizationAddressProvince = organizationAddressProvince;
    }

    public String getOrganizationAddressCity() {
        return organizationAddressCity;
    }

    public void setOrganizationAddressCity(String organizationAddressCity) {
        this.organizationAddressCity = organizationAddressCity;
    }

    public String getOrganizationAddressCounty() {
        return organizationAddressCounty;
    }

    public void setOrganizationAddressCounty(String organizationAddressCounty) {
        this.organizationAddressCounty = organizationAddressCounty;
    }

    public String getOrganizationAddressCountry() {
        return organizationAddressCountry;
    }

    public void setOrganizationAddressCountry(String organizationAddressCountry) {
        this.organizationAddressCountry = organizationAddressCountry;
    }

    public String getOrganizationAddressVillage() {
        return organizationAddressVillage;
    }

    public void setOrganizationAddressVillage(String organizationAddressVillage) {
        this.organizationAddressVillage = organizationAddressVillage;
    }

    public String getOrganizationAddressHouseNo() {
        return organizationAddressHouseNo;
    }

    public void setOrganizationAddressHouseNo(String organizationAddressHouseNo) {
        this.organizationAddressHouseNo = organizationAddressHouseNo;
    }

    public String getOrganizationAddress() {
        return organizationAddress;
    }

    public void setOrganizationAddress(String organizationAddress) {
        this.organizationAddress = organizationAddress;
    }

    public String getOrganizationAddressPostalCode() {
        return organizationAddressPostalCode;
    }

    public void setOrganizationAddressPostalCode(String organizationAddressPostalCode) {
        this.organizationAddressPostalCode = organizationAddressPostalCode;
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
}
