package com.zebone.nhis.webservice.pskq.model;

import com.zebone.nhis.webservice.pskq.annotation.MetadataDescribe;

/**
 * @author lijin
 * 医嘱项目消息模型
 */
public class OrderItem {

    @MetadataDescribe(id= "LHDE0014001",name = "医嘱项目字典ID",eName = "ORDER_ITEM_ID",property = "orderItemId")
    private String orderItemId;

    @MetadataDescribe(id= "LHDE0014002",name = "机构/院部代码",eName = "ORG_CODE",property = "orgCode")
    private String orgCode;

    @MetadataDescribe(id= "LHDE0014003",name = "机构/院部名称",eName = "ORG_NAME",property = "orgName")
    private String orgName;

    @MetadataDescribe(id= "LHDE0014004",name = "医嘱项目代码",eName = "ORDER_ITEM_CODE",property = "orderItemCode")
    private String orderItemCode;

    @MetadataDescribe(id= "LHDE0014005",name = "医嘱项目名称",eName = "ORDER_ITEM_NAME",property = "orderItemName")
    private String orderItemName;

    @MetadataDescribe(id= "LHDE0014006",name = "医嘱项目描述",eName = "ORDER_ITEM_DESC",property = "orderItemDesc")
    private String orderItemDesc;

    @MetadataDescribe(id= "LHDE0014007",name = "打印名称",eName = "PRINT_NAME",property = "printName")
    private String printName;

    @MetadataDescribe(id= "LHDE0014008",name = "拼音简码",eName = "SPELL_CODE",property = "spellCode")
    private String spellCode;

    @MetadataDescribe(id= "LHDE0014009",name = "五笔简码",eName = "WB_CODE",property = "wbCode")
    private String wbCode;

    @MetadataDescribe(id= "LHDE0014010",name = "自定义简码",eName = "CUSTOM_CODE",property = "customCode")
    private String customCode;

    @MetadataDescribe(id= "LHDE0014011",name = "医嘱分类代码",eName = "ORDER_CATEGORY_CODE",property = "orderCategoryCode")
    private String orderCategoryCode;

    @MetadataDescribe(id= "LHDE0014012",name = "医嘱分类名称",eName = "ORDER_CATEGORY_NAME",property = "orderCategoryName")
    private String orderCategoryName;

    @MetadataDescribe(id= "LHDE0014013",name = "医嘱项目主数据分类代码",eName = "ORDER_ITEM_TYPE_CODE",property = "orderItemTypeCode")
    private String orderItemTypeCode;

    @MetadataDescribe(id= "LHDE0014014",name = "医嘱项目主数据分类名称",eName = "ORDER_ITEM_TYPE_NAME",property = "orderItemTypeName")
    private String orderItemTypeName;

    @MetadataDescribe(id= "LHDE0014015",name = "门诊使用标志",eName = "OUTPATIENT_FLAG",property = "outpatientFlag")
    private String outpatientFlag;

    @MetadataDescribe(id= "LHDE0014016",name = "急诊使用标志",eName = "EMERGENCY_FLAG",property = "emergencyFlag")
    private String emergencyFlag;

    @MetadataDescribe(id= "LHDE0014017",name = "住院使用标志",eName = "INPATIENT_FLAG",property = "inpatientFlag")
    private String inpatientFlag;

    @MetadataDescribe(id= "LHDE0014018",name = "体检使用标志",eName = "PHYSICAL_FLAG",property = "physicalFlag")
    private String physicalFlag;

    @MetadataDescribe(id= "LHDE0014019",name = "紧急标志（'1'紧急'0'普通）",eName = "EMER_FLAG",property = "emerFlag")
    private String emerFlag;

    @MetadataDescribe(id= "LHDE0014020",name = "适用人群代码",eName = "SUIT_CROWD_CODE",property = "suitCrowdCode")
    private String suitCrowdCode;

    @MetadataDescribe(id= "LHDE0014021",name = "医疗机构医嘱项目价格",eName = "ORG_ORDER_ITEM_PRICE",property = "orgOrderItemPrice")
    private String orgOrderItemPrice;

    @MetadataDescribe(id= "LHDE0014022",name = "执行科室ID",eName = "EXEC_DEPT_ID",property = "execDeptId")
    private String execDeptId;

    @MetadataDescribe(id= "LHDE0014023",name = "执行科室名称",eName = "EXEC_DEPT_NAME",property = "execDeptName")
    private String execDeptName;

    @MetadataDescribe(id= "LHDE0014024",name = "源系统代码",eName = "SOURCE_SYSTEM_CODE",property = "sourceSystemCode")
    private String sourceSystemCode;

    @MetadataDescribe(id= "LHDE0014025",name = "执行系统代码",eName = "EXEC_SYSTEM_CODE",property = "execSystemCode")
    private String execSystemCode;

    @MetadataDescribe(id= "LHDE0014026",name = "生效时间",eName = "EFFECTIVE_DATE_TIME",property = "effectiveDateTime")
    private String effectiveDateTime;

    @MetadataDescribe(id= "LHDE0014027",name = "失效时间",eName = "INEFFECTIVE_DATE_TIME",property = "ineffectiveDateTime")
    private String ineffectiveDateTime;

    @MetadataDescribe(id= "LHDE0014028",name = "录入人ID",eName = "ENTER_OPERA_ID",property = "enterOperaId")
    private String enterOperaId;

    @MetadataDescribe(id= "LHDE0014029",name = "录入人姓名",eName = "ENTER_OPERA_NAME",property = "enterOperaName")
    private String enterOperaName;

    @MetadataDescribe(id= "LHDE0014030",name = "录入日期时间",eName = "ENTER_DATE_TIME",property = "enterDateTime")
    private String enterDateTime;

    @MetadataDescribe(id= "LHDE0014031",name = "修改人ID",eName = "MODIFY_OPERA_ID",property = "modifyOperaId")
    private String modifyOperaId;

    @MetadataDescribe(id= "LHDE0014032",name = "修改人姓名",eName = "MODIFY_OPERA_NAME",property = "modifyOperaName")
    private String modifyOperaName;

    @MetadataDescribe(id= "LHDE0014033",name = "修改日期",eName = "MODIFY_DATE_TIME",property = "modifyDateTime")
    private String modifyDateTime;

    @MetadataDescribe(id= "LHDE0014034",name = "审核人ID",eName = "CHECK_OPERA_ID",property = "checkOperaId")
    private String checkOperaId;

    @MetadataDescribe(id= "LHDE0014035",name = "审核人姓名",eName = "CHECK_OPERA_NAME",property = "checkOperaName")
    private String checkOperaName;

    @MetadataDescribe(id= "LHDE0014036",name = "审核日期时间",eName = "CHECK_DATE_TIME",property = "checkDateTime")
    private String checkDateTime;

    @MetadataDescribe(id= "LHDE0014037",name = "复审标志",eName = "REVIEW_FLAG",property = "reviewFlag")
    private String reviewFlag;

    @MetadataDescribe(id= "LHDE0014038",name = "复审人ID",eName = "REVIEW_OPERA_ID",property = "reviewOperaId")
    private String reviewOperaId;

    @MetadataDescribe(id= "LHDE0014039",name = "复审人姓名",eName = "REVIEW_OPERA_NAME",property = "reviewOperaName")
    private String reviewOperaName;

    @MetadataDescribe(id= "LHDE0014040",name = "复审日期时间",eName = "REVIEW_DATE_TIME",property = "reviewDateTime")
    private String reviewDateTime;

    @MetadataDescribe(id= "LHDE0014041",name = "作废标志",eName = "CANCEL_FLAG",property = "cancelFlag")
    private String cancelFlag;

    @MetadataDescribe(id= "LHDE0014042",name = "撤销人ID",eName = "CANCEL_OPERA_ID",property = "cancelOperaId")
    private String cancelOperaId;

    @MetadataDescribe(id= "LHDE0014043",name = "撤销人姓名",eName = "CANCEL_OPERA_NAME",property = "cancelOperaName")
    private String cancelOperaName;

    @MetadataDescribe(id= "LHDE0014044",name = "取消日期时间",eName = "CANCEL_DATE_TIME",property = "cancelDateTime")
    private String cancelDateTime;

    @MetadataDescribe(id= "LHDE0014045",name = "撤销原因描述",eName = "CANCEL_REASON_DESC",property = "cancelReasonDesc")
    private String cancelReasonDesc;

    public String getOrderItemId() {
        return orderItemId;
    }

    public void setOrderItemId(String orderItemId) {
        this.orderItemId = orderItemId;
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

    public String getOrderItemCode() {
        return orderItemCode;
    }

    public void setOrderItemCode(String orderItemCode) {
        this.orderItemCode = orderItemCode;
    }

    public String getOrderItemName() {
        return orderItemName;
    }

    public void setOrderItemName(String orderItemName) {
        this.orderItemName = orderItemName;
    }

    public String getOrderItemDesc() {
        return orderItemDesc;
    }

    public void setOrderItemDesc(String orderItemDesc) {
        this.orderItemDesc = orderItemDesc;
    }

    public String getPrintName() {
        return printName;
    }

    public void setPrintName(String printName) {
        this.printName = printName;
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

    public String getOrderCategoryCode() {
        return orderCategoryCode;
    }

    public void setOrderCategoryCode(String orderCategoryCode) {
        this.orderCategoryCode = orderCategoryCode;
    }

    public String getOrderCategoryName() {
        return orderCategoryName;
    }

    public void setOrderCategoryName(String orderCategoryName) {
        this.orderCategoryName = orderCategoryName;
    }

    public String getOrderItemTypeCode() {
        return orderItemTypeCode;
    }

    public void setOrderItemTypeCode(String orderItemTypeCode) {
        this.orderItemTypeCode = orderItemTypeCode;
    }

    public String getOrderItemTypeName() {
        return orderItemTypeName;
    }

    public void setOrderItemTypeName(String orderItemTypeName) {
        this.orderItemTypeName = orderItemTypeName;
    }

    public String getOutpatientFlag() {
        return outpatientFlag;
    }

    public void setOutpatientFlag(String outpatientFlag) {
        this.outpatientFlag = outpatientFlag;
    }

    public String getEmergencyFlag() {
        return emergencyFlag;
    }

    public void setEmergencyFlag(String emergencyFlag) {
        this.emergencyFlag = emergencyFlag;
    }

    public String getInpatientFlag() {
        return inpatientFlag;
    }

    public void setInpatientFlag(String inpatientFlag) {
        this.inpatientFlag = inpatientFlag;
    }

    public String getPhysicalFlag() {
        return physicalFlag;
    }

    public void setPhysicalFlag(String physicalFlag) {
        this.physicalFlag = physicalFlag;
    }

    public String getEmerFlag() {
        return emerFlag;
    }

    public void setEmerFlag(String emerFlag) {
        this.emerFlag = emerFlag;
    }

    public String getSuitCrowdCode() {
        return suitCrowdCode;
    }

    public void setSuitCrowdCode(String suitCrowdCode) {
        this.suitCrowdCode = suitCrowdCode;
    }

    public String getOrgOrderItemPrice() {
        return orgOrderItemPrice;
    }

    public void setOrgOrderItemPrice(String orgOrderItemPrice) {
        this.orgOrderItemPrice = orgOrderItemPrice;
    }

    public String getExecDeptId() {
        return execDeptId;
    }

    public void setExecDeptId(String execDeptId) {
        this.execDeptId = execDeptId;
    }

    public String getExecDeptName() {
        return execDeptName;
    }

    public void setExecDeptName(String execDeptName) {
        this.execDeptName = execDeptName;
    }

    public String getSourceSystemCode() {
        return sourceSystemCode;
    }

    public void setSourceSystemCode(String sourceSystemCode) {
        this.sourceSystemCode = sourceSystemCode;
    }

    public String getExecSystemCode() {
        return execSystemCode;
    }

    public void setExecSystemCode(String execSystemCode) {
        this.execSystemCode = execSystemCode;
    }

    public String getEffectiveDateTime() {
        return effectiveDateTime;
    }

    public void setEffectiveDateTime(String effectiveDateTime) {
        this.effectiveDateTime = effectiveDateTime;
    }

    public String getIneffectiveDateTime() {
        return ineffectiveDateTime;
    }

    public void setIneffectiveDateTime(String ineffectiveDateTime) {
        this.ineffectiveDateTime = ineffectiveDateTime;
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

    public String getCheckOperaId() {
        return checkOperaId;
    }

    public void setCheckOperaId(String checkOperaId) {
        this.checkOperaId = checkOperaId;
    }

    public String getCheckOperaName() {
        return checkOperaName;
    }

    public void setCheckOperaName(String checkOperaName) {
        this.checkOperaName = checkOperaName;
    }

    public String getCheckDateTime() {
        return checkDateTime;
    }

    public void setCheckDateTime(String checkDateTime) {
        this.checkDateTime = checkDateTime;
    }

    public String getReviewFlag() {
        return reviewFlag;
    }

    public void setReviewFlag(String reviewFlag) {
        this.reviewFlag = reviewFlag;
    }

    public String getReviewOperaId() {
        return reviewOperaId;
    }

    public void setReviewOperaId(String reviewOperaId) {
        this.reviewOperaId = reviewOperaId;
    }

    public String getReviewOperaName() {
        return reviewOperaName;
    }

    public void setReviewOperaName(String reviewOperaName) {
        this.reviewOperaName = reviewOperaName;
    }

    public String getReviewDateTime() {
        return reviewDateTime;
    }

    public void setReviewDateTime(String reviewDateTime) {
        this.reviewDateTime = reviewDateTime;
    }

    public String getCancelFlag() {
        return cancelFlag;
    }

    public void setCancelFlag(String cancelFlag) {
        this.cancelFlag = cancelFlag;
    }

    public String getCancelOperaId() {
        return cancelOperaId;
    }

    public void setCancelOperaId(String cancelOperaId) {
        this.cancelOperaId = cancelOperaId;
    }

    public String getCancelOperaName() {
        return cancelOperaName;
    }

    public void setCancelOperaName(String cancelOperaName) {
        this.cancelOperaName = cancelOperaName;
    }

    public String getCancelDateTime() {
        return cancelDateTime;
    }

    public void setCancelDateTime(String cancelDateTime) {
        this.cancelDateTime = cancelDateTime;
    }

    public String getCancelReasonDesc() {
        return cancelReasonDesc;
    }

    public void setCancelReasonDesc(String cancelReasonDesc) {
        this.cancelReasonDesc = cancelReasonDesc;
    }
}
