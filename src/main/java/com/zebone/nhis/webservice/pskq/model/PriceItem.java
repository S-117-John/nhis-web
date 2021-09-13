package com.zebone.nhis.webservice.pskq.model;

import com.alibaba.fastjson.annotation.JSONField;
import com.zebone.nhis.webservice.pskq.annotation.MetadataDescribe;

import java.util.Date;

/**
 * @author lijin
 * 物价项目
 */
public class PriceItem {

    /**
     * 物价代码
     */
    @MetadataDescribe(id= "LHDE0013001",name = "患者主索引号码",eName = "PRICE_CODE",property = "priceCode")
    private String priceCode;

    /**
     * 物价名称
     */
    @MetadataDescribe(id= "LHDE0013002",name = "物价名称",eName = "PRICE_NAME",property = "priceName")
    private String priceName;

    /**
     * 拼音简码
     */
    @MetadataDescribe(id= "LHDE0013003",name = "拼音简码",eName = "SPELL_CODE",property = "spellCode")
    private String spellCode;

    /**
     * 五笔简码
     */
    @MetadataDescribe(id= "LHDE0013004",name = "五笔简码",eName = "WB_CODE",property = "wbCode")
    private String wbCode;

    /**
     * 自定义简码
     */
    @MetadataDescribe(id= "LHDE0013005",name = "自定义简码",eName = "CUSTOM_CODE",property = "customCode")
    private String customCode;

    /**
     * 计量单位
     */
    @MetadataDescribe(id= "LHDE0013006",name = "计量单位",eName = "UNIT",property = "unit")
    private String unit;

    /**
     * 第一档价格
     */
    @MetadataDescribe(id= "LHDE0013007",name = "第一档价格",eName = "UNIT_PRICE1",property = "unitPrice1")
    private String unitPrice1;

    /**
     * 第二档价格
     */
    @MetadataDescribe(id= "LHDE0013008",name = "第二档价格",eName = "UNIT_PRICE2",property = "unitPrice2")
    private String unitPrice2;

    /**
     * 第三档价格
     */
    @MetadataDescribe(id= "LHDE0013009",name = "第三档价格",eName = "UNIT_PRICE3",property = "unitPrice3")
    private String unitPrice3;

    /**
     * 第四档价格
     */
    @MetadataDescribe(id= "LHDE0013010",name = "第四档价格",eName = "UNIT_PRICE4",property = "unitPrice4")
    private String unitPrice4;

    /**
     * 财务会计分类代码
     */
    @MetadataDescribe(id= "LHDE0013011",name = "财务会计分类代码",eName = "ACCOUNT_CLASS_CODE",property = "accountClassCode")
    private String accountClassCode;

    /**
     * 财务会计分类名称
     */
    @MetadataDescribe(id= "LHDE0013012",name = "财务会计分类名称",eName = "ACCOUNT_CLASS_NAME",property = "accountClassName")
    private String accountClassName;

    /**
     * 病案分类代码
     */
    @MetadataDescribe(id= "LHDE0013013",name = "病案分类代码",eName = "MR_CLASS_CODE",property = "mrClassCode")
    private String mrClassCode;

    /**
     * 病案分类名称
     */
    @MetadataDescribe(id= "LHDE0013014",name = "病案分类名称",eName = "MR_CLASS_NAME",property = "mrClassName")
    private String mrClassName;

    /**
     * 发票/收据分类代码
     */
    @MetadataDescribe(id= "LHDE0013015",name = "发票/收据分类代码",eName = "RCPT_CLASS_CODE",property = "rcptClassCode")
    private String rcptClassCode;

    /**
     * 发票/收据分类名称
     */
    @MetadataDescribe(id= "LHDE0013016",name = "收据分类名称",eName = "RCPT_CLASS_NAME",property = "rcptClassName")
    private String rcptClassName;

    /**
     * 项目内涵
     */
    @MetadataDescribe(id= "LHDE0013017",name = "项目内涵",eName = "PROJECT_CONNOTATION",property = "projectConnotation")
    private String projectConnotation;

    /**
     * 除外说明
     */
    @MetadataDescribe(id= "LHDE0013018",name = "除外说明",eName = "EXCEPTED",property = "excepted")
    private String excepted;

    /**
     * 项目说明
     */
    @MetadataDescribe(id= "LHDE0013019",name = "项目说明",eName = "PROJECT_DESCRIPTION",property = "projectDescription")
    private String projectDescription;

    /**
     * 作废标识
     */
    @MetadataDescribe(id= "LHDE0013020",name = "作废标识",eName = "CANCEL_FLAG",property = "cancelFlag")
    private String cancelFlag;

    /**
     * 启用文号
     */
    @MetadataDescribe(id= "LHDE0013021",name = "启用文号",eName = "ENABLE_DUCOMENT_NUMBER",property = "enableDucomentNumber")
    private String enableDucomentNumber;

    /**
     * 启用日期时间
     */
    @JSONField(format="yyyyMMdd'T'HHmmss")
    @MetadataDescribe(id= "LHDE0013022",name = "启用日期时间",eName = "ENABLE_DATE_TIME",property = "enableDateTime")
    private Date enableDateTime;

    /**
     * 停用文号
     */
    @MetadataDescribe(id= "LHDE0013023",name = "停用文号",eName = "DISABLE_DOCUMENT_NUMBER",property = "disableDocumentNumber")
    private String disableDocumentNumber;

    /**
     * 停用日期时间
     */
    @MetadataDescribe(id= "LHDE0013024",name = "停用日期时间",eName = "DISABLE_DATE_TIME",property = "disableDateTime")
    private String disableDateTime;

    /**
     * 录入人ID
     */
    @MetadataDescribe(id= "LHDE0013025",name = "录入人ID",eName = "ENTER_OPERA_ID",property = "enterOperaId")
    private String enterOperaId;

    /**
     * 录入人姓名
     */
    @MetadataDescribe(id= "LHDE0013026",name = "录入人姓名",eName = "ENTER_OPERA_NAME",property = "enterOperaName")
    private String enterOperaName;

    /**
     * 录入日期时间
     */
    @JSONField(format="yyyyMMdd'T'HHmmss")
    @MetadataDescribe(id= "LHDE0013027",name = "录入日期时间",eName = "ENTER_DATE_TIME",property = "enterDateTime")
    private Date enterDateTime;

    /**
     * 修改人ID
     */
    @MetadataDescribe(id= "LHDE0013028",name = "修改人ID",eName = "MODIFY_OPERA_ID",property = "modifyOperaId")
    private String modifyOperaId;

    /**
     * 修改人姓名
     */
    @MetadataDescribe(id= "LHDE0013029",name = "修改人姓名",eName = "MODIFY_OPERA_NAME",property = "modifyOperaName")
    private String modifyOperaName;

    /**
     * 修改日期
     */
    @JSONField(format="yyyyMMdd'T'HHmmss")
    @MetadataDescribe(id= "LHDE0013030",name = "修改日期",eName = "MODIFY_DATE_TIME",property = "modifyDateTime")
    private Date modifyDateTime;


    public String getPriceCode() {
        return priceCode;
    }

    public void setPriceCode(String priceCode) {
        this.priceCode = priceCode;
    }

    public String getPriceName() {
        return priceName;
    }

    public void setPriceName(String priceName) {
        this.priceName = priceName;
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

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public String getUnitPrice1() {
        return unitPrice1;
    }

    public void setUnitPrice1(String unitPrice1) {
        this.unitPrice1 = unitPrice1;
    }

    public String getUnitPrice2() {
        return unitPrice2;
    }

    public void setUnitPrice2(String unitPrice2) {
        this.unitPrice2 = unitPrice2;
    }

    public String getUnitPrice3() {
        return unitPrice3;
    }

    public void setUnitPrice3(String unitPrice3) {
        this.unitPrice3 = unitPrice3;
    }

    public String getUnitPrice4() {
        return unitPrice4;
    }

    public void setUnitPrice4(String unitPrice4) {
        this.unitPrice4 = unitPrice4;
    }

    public String getAccountClassCode() {
        return accountClassCode;
    }

    public void setAccountClassCode(String accountClassCode) {
        this.accountClassCode = accountClassCode;
    }

    public String getAccountClassName() {
        return accountClassName;
    }

    public void setAccountClassName(String accountClassName) {
        this.accountClassName = accountClassName;
    }

    public String getMrClassCode() {
        return mrClassCode;
    }

    public void setMrClassCode(String mrClassCode) {
        this.mrClassCode = mrClassCode;
    }

    public String getMrClassName() {
        return mrClassName;
    }

    public void setMrClassName(String mrClassName) {
        this.mrClassName = mrClassName;
    }

    public String getRcptClassCode() {
        return rcptClassCode;
    }

    public void setRcptClassCode(String rcptClassCode) {
        this.rcptClassCode = rcptClassCode;
    }

    public String getRcptClassName() {
        return rcptClassName;
    }

    public void setRcptClassName(String rcptClassName) {
        this.rcptClassName = rcptClassName;
    }

    public String getProjectConnotation() {
        return projectConnotation;
    }

    public void setProjectConnotation(String projectConnotation) {
        this.projectConnotation = projectConnotation;
    }

    public String getExcepted() {
        return excepted;
    }

    public void setExcepted(String excepted) {
        this.excepted = excepted;
    }

    public String getProjectDescription() {
        return projectDescription;
    }

    public void setProjectDescription(String projectDescription) {
        this.projectDescription = projectDescription;
    }

    public String getCancelFlag() {
        return cancelFlag;
    }

    public void setCancelFlag(String cancelFlag) {
        this.cancelFlag = cancelFlag;
    }

    public String getEnableDucomentNumber() {
        return enableDucomentNumber;
    }

    public void setEnableDucomentNumber(String enableDucomentNumber) {
        this.enableDucomentNumber = enableDucomentNumber;
    }

    public Date getEnableDateTime() {
        return enableDateTime;
    }

    public void setEnableDateTime(Date enableDateTime) {
        this.enableDateTime = enableDateTime;
    }

    public String getDisableDocumentNumber() {
        return disableDocumentNumber;
    }

    public void setDisableDocumentNumber(String disableDocumentNumber) {
        this.disableDocumentNumber = disableDocumentNumber;
    }

    public String getDisableDateTime() {
        return disableDateTime;
    }

    public void setDisableDateTime(String disableDateTime) {
        this.disableDateTime = disableDateTime;
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
