package com.zebone.nhis.webservice.pskq.model;

import com.alibaba.fastjson.annotation.JSONField;
import com.zebone.nhis.webservice.pskq.annotation.MetadataDescribe;

import java.util.Date;

public class Department {

    @MetadataDescribe(id= "LHDE0003001",name = "科室ID",eName = "DEPT_ID")
    private String deptId;

    @MetadataDescribe(id= "LHDE0003002",name = "科室名称",eName = "DEPT_NAME")
    private String deptName;

    @MetadataDescribe(id= "LHDE0003003",name = "科室全称",eName = "DEPT_FULL_NAME")
    private String deptFullName;

    @MetadataDescribe(id= "LHDE0003004",name = "父类科室ID",eName = "DEPT_PARENT_ID")
    private String deptParentId;

    @MetadataDescribe(id= "LHDE0003005",name = "科室类型",eName = "DEPT_TYPE_CODE")
    private String deptTypeCode;

    @MetadataDescribe(id= "LHDE0003006",name = "学科代码",eName = "DEPT_DISCIPLINE_CODE")
    private String deptDisciplineCode;

    @MetadataDescribe(id= "LHDE0003007",name = "特殊科室属性",eName = "SPECIAL_DEPT_ATTRIBUTE")
    private String specialDeptAttribute;

    @MetadataDescribe(id= "LHDE0003008",name = "是否手术科室",eName = "OPERATION_INDICATOR")
    private String operationIndicator;

    @MetadataDescribe(id= "LHDE0003009",name = "是否挂号科室",eName = "REGISTER_INDICATOR")
    private String registerIndicator;

    @MetadataDescribe(id= "LHDE0003010",name = "是否核算科室",eName = "BUSINESS_ACCOUNTING_INDICATOR")
    private String businessAccountingIndicator;

    @MetadataDescribe(id= "LHDE0003011",name = "科室联系电话",eName = "DEPT_PHONE_NO")
    private String deptPhoneNo;

    @MetadataDescribe(id= "LHDE0003012",name = "领导姓名",eName = "LEADER_NAME")
    private String leaderName;

    @MetadataDescribe(id= "LHDE0003013",name = "领导联系电话",eName = "LEADER_PHONE_NO")
    private String leaderPhoneNo;

    @MetadataDescribe(id= "LHDE0003014",name = "科室位置",eName = "DEPT_LOCATION")
    private String deptLocation;

    @MetadataDescribe(id= "LHDE0003015",name = "排序码",eName = "SORT_NO")
    private String sortNo;

    @MetadataDescribe(id= "LHDE0003016",name = "拼音简码",eName = "SPELL_CODE")
    private String spellCode;

    @MetadataDescribe(id= "LHDE0003017",name = "五笔简码",eName = "WB_CODE")
    private String wbCode;

    @MetadataDescribe(id= "LHDE0003018",name = "自定义简码",eName = "CUSTOM_CODE")
    private String customCode;

    @MetadataDescribe(id= "LHDE0003019",name = "备注说明",eName = "CMMT")
    private String cmmt;

    @MetadataDescribe(id= "LHDE0003020",name = "有效性标志",eName = "VALID_STATE")
    private String validState;

    @MetadataDescribe(id= "LHDE0003021",name = "机构/院部代码",eName = "ORG_CODE")
    private String orgCode;

    @MetadataDescribe(id= "LHDE0003022",name = "机构/院部名称",eName = "ORG_NAME")
    private String orgName;

    @MetadataDescribe(id= "LHDE0003023",name = "源系统代码",eName = "SOURCE_SYSTEM_CODE")
    private String sourceSystemCode;

    @MetadataDescribe(id= "LHDE0003024",name = "录入人ID",eName = "ENTER_OPERA_ID")
    private String enterOperaId;

    @MetadataDescribe(id= "LHDE0003025",name = "录入人姓名",eName = "ENTER_OPERA_NAME")
    private String enterOperaName;

    @JSONField(format="yyyyMMdd'T'HHmmss")
    @MetadataDescribe(id= "LHDE0003026",name = "录入日期时间",eName = "ENTER_DATE_TIME")
    private Date enterDateTime;

    @MetadataDescribe(id= "LHDE0003027",name = "修改人ID",eName = "MODIFY_OPERA_ID")
    private String modifyOperaId;

    @MetadataDescribe(id= "LHDE0003028",name = "修改人姓名",eName = "MODIFY_OPERA_NAME")
    private String modifyOperaName;

    @JSONField(format="yyyyMMdd'T'HHmmss")
    @MetadataDescribe(id= "LHDE0003029",name = "修改日期",eName = "MODIFY_DATE_TIME")
    private Date modifyDateTime;

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

    public String getDeptFullName() {
        return deptFullName;
    }

    public void setDeptFullName(String deptFullName) {
        this.deptFullName = deptFullName;
    }

    public String getDeptParentId() {
        return deptParentId;
    }

    public void setDeptParentId(String deptParentId) {
        this.deptParentId = deptParentId;
    }

    public String getDeptTypeCode() {
        return deptTypeCode;
    }

    public void setDeptTypeCode(String deptTypeCode) {
        this.deptTypeCode = deptTypeCode;
    }

    public String getDeptDisciplineCode() {
        return deptDisciplineCode;
    }

    public void setDeptDisciplineCode(String deptDisciplineCode) {
        this.deptDisciplineCode = deptDisciplineCode;
    }

    public String getSpecialDeptAttribute() {
        return specialDeptAttribute;
    }

    public void setSpecialDeptAttribute(String specialDeptAttribute) {
        this.specialDeptAttribute = specialDeptAttribute;
    }

    public String getOperationIndicator() {
        return operationIndicator;
    }

    public void setOperationIndicator(String operationIndicator) {
        this.operationIndicator = operationIndicator;
    }

    public String getRegisterIndicator() {
        return registerIndicator;
    }

    public void setRegisterIndicator(String registerIndicator) {
        this.registerIndicator = registerIndicator;
    }

    public String getBusinessAccountingIndicator() {
        return businessAccountingIndicator;
    }

    public void setBusinessAccountingIndicator(String businessAccountingIndicator) {
        this.businessAccountingIndicator = businessAccountingIndicator;
    }

    public String getDeptPhoneNo() {
        return deptPhoneNo;
    }

    public void setDeptPhoneNo(String deptPhoneNo) {
        this.deptPhoneNo = deptPhoneNo;
    }

    public String getLeaderName() {
        return leaderName;
    }

    public void setLeaderName(String leaderName) {
        this.leaderName = leaderName;
    }

    public String getLeaderPhoneNo() {
        return leaderPhoneNo;
    }

    public void setLeaderPhoneNo(String leaderPhoneNo) {
        this.leaderPhoneNo = leaderPhoneNo;
    }

    public String getDeptLocation() {
        return deptLocation;
    }

    public void setDeptLocation(String deptLocation) {
        this.deptLocation = deptLocation;
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
}

