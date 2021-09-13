package com.zebone.nhis.ma.pub.platform.pskq.model;

import com.zebone.nhis.ma.pub.platform.pskq.annotation.MetadataDescribe;

public class MainData {

    @MetadataDescribe(id= "LHDE0009001",name = "主数据成员ID",eName = "MASTER_MEMBER_ID")
    private String masterMemberId;

    @MetadataDescribe(id= "LHDE0009002",name = "主数据成员代码",eName = "MASTER_MEMBER_CODE")
    private String masterMemberCode;

    @MetadataDescribe(id= "LHDE0009003",name = "主数据成员名称",eName = "MASTER_MEMBER_NAME")
    private String masterMemberName;

    @MetadataDescribe(id= "LHDE0009004",name = "主数据定义代码",eName = "MASTER_DEFINITION_CODE")
    private String masterDefinitionCode;

    @MetadataDescribe(id= "LHDE0009005",name = "主数据定义名称",eName = "MASTER_DEFINITION_NAME")
    private String masterDefinitionName;

    @MetadataDescribe(id= "LHDE0009006",name = "排序码",eName = "SORT_NO")
    private String sortNo;

    @MetadataDescribe(id= "LHDE0009007",name = "有效性标志",eName = "VALID_STATE")
    private String validState;

    @MetadataDescribe(id= "LHDE0009008",name = "主数据成员状态代码",eName = "MASTER_MEMBER_STATUS_CODE")
    private String masterMemberStatusCode;

    @MetadataDescribe(id= "LHDE0009009",name = "主数据成员状态名称",eName = "MASTER_MEMBER_STATUS_NAME")
    private String masterMemberStatusName;

    @MetadataDescribe(id= "LHDE0009010",name = "删除标志",eName = "DELETE_FLAG")
    private String deleteFlag;

    @MetadataDescribe(id= "LHDE0009011",name = "拼音简码",eName = "SPELL_CODE")
    private String spellCode;

    @MetadataDescribe(id= "LHDE0009012",name = "录入人ID",eName = "ENTER_OPERA_ID")
    private String enterOperaId;

    @MetadataDescribe(id= "LHDE0009013",name = "录入人姓名",eName = "ENTER_OPERA_NAME")
    private String enterOperaName;

    @MetadataDescribe(id= "LHDE0009014",name = "录入日期时间",eName = "ENTER_DATE_TIME")
    private String enterDateTime;

    @MetadataDescribe(id= "LHDE0009015",name = "修改人ID",eName = "MODIFY_OPERA_ID")
    private String modifyOperaId;

    @MetadataDescribe(id= "LHDE0009016",name = "修改人姓名",eName = "MODIFY_OPERA_NAME")
    private String modifyOperaName;

    @MetadataDescribe(id= "LHDE0009017",name = "修改日期",eName = "MODIFY_DATE_TIME")
    private String modifyDateTime;


    public String getMasterMemberId() {
        return masterMemberId;
    }

    public void setMasterMemberId(String masterMemberId) {
        this.masterMemberId = masterMemberId;
    }

    public String getMasterMemberCode() {
        return masterMemberCode;
    }

    public void setMasterMemberCode(String masterMemberCode) {
        this.masterMemberCode = masterMemberCode;
    }

    public String getMasterMemberName() {
        return masterMemberName;
    }

    public void setMasterMemberName(String masterMemberName) {
        this.masterMemberName = masterMemberName;
    }

    public String getMasterDefinitionCode() {
        return masterDefinitionCode;
    }

    public void setMasterDefinitionCode(String masterDefinitionCode) {
        this.masterDefinitionCode = masterDefinitionCode;
    }

    public String getMasterDefinitionName() {
        return masterDefinitionName;
    }

    public void setMasterDefinitionName(String masterDefinitionName) {
        this.masterDefinitionName = masterDefinitionName;
    }

    public String getSortNo() {
        return sortNo;
    }

    public void setSortNo(String sortNo) {
        this.sortNo = sortNo;
    }

    public String getValidState() {
        return validState;
    }

    public void setValidState(String validState) {
        this.validState = validState;
    }

    public String getMasterMemberStatusCode() {
        return masterMemberStatusCode;
    }

    public void setMasterMemberStatusCode(String masterMemberStatusCode) {
        this.masterMemberStatusCode = masterMemberStatusCode;
    }

    public String getMasterMemberStatusName() {
        return masterMemberStatusName;
    }

    public void setMasterMemberStatusName(String masterMemberStatusName) {
        this.masterMemberStatusName = masterMemberStatusName;
    }

    public String getDeleteFlag() {
        return deleteFlag;
    }

    public void setDeleteFlag(String deleteFlag) {
        this.deleteFlag = deleteFlag;
    }

    public String getSpellCode() {
        return spellCode;
    }

    public void setSpellCode(String spellCode) {
        this.spellCode = spellCode;
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
