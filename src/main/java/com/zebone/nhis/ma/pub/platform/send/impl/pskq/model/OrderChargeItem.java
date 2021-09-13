package com.zebone.nhis.ma.pub.platform.send.impl.pskq.model;




import com.zebone.nhis.ma.pub.platform.pskq.annotation.MetadataDescribe;

import java.util.Date;

/**
 * 医嘱项目与收费项目对照
 */
public class OrderChargeItem {

    @MetadataDescribe(id= "LHDE0017001",name = "医嘱项目与收费项目对照ID",eName = "ORDER_CHARGE_ITEM_ID")
    private String orderChargeItemId;

    @MetadataDescribe(id= "LHDE0017002",name = "医嘱项目字典ID",eName = "ORDER_ITEM_ID")
    private String orderItemId;

    @MetadataDescribe(id= "LHDE0017003",name = "物价代码",eName = "PRICE_CODE")
    private String priceCode;

    @MetadataDescribe(id= "LHDE0017004",name = "收费项目ID",eName = "CHARGE_ITEM_ID")
    private String chargeItemId;

    @MetadataDescribe(id= "LHDE0017005",name = "收费数量",eName = "CHARGE_QUANTITY")
    private String chargeQuantity;

    @MetadataDescribe(id= "LHDE0017006",name = "录入人ID",eName = "ENTER_OPERA_ID")
    private String enterOperaId;

    @MetadataDescribe(id= "LHDE0017007",name = "录入人姓名",eName = "ENTER_OPERA_NAME")
    private String enterOperaName;

    @MetadataDescribe(id= "LHDE0017008",name = "录入日期时间",eName = "ENTER_DATE_TIME")
    private Date enterDateTime;

    @MetadataDescribe(id= "LHDE0017009",name = "修改人ID",eName = "MODIFY_OPERA_ID")
    private String modifyOperaId;

    @MetadataDescribe(id= "LHDE0017010",name = "修改人姓名",eName = "MODIFY_OPERA_NAME")
    private String modifyOperaName;

    @MetadataDescribe(id= "LHDE0017011",name = "修改日期",eName = "MODIFY_DATE_TIME")
    private String modifyDateTime;

    @MetadataDescribe(id= "LHDE0017012",name = "复审标志",eName = "REVIEW_FLAG")
    private String reviewFlag;

    @MetadataDescribe(id= "LHDE0017013",name = "复审日期时间",eName = "REVIEW_DATE_TIME")
    private String reviewDateTime;

    @MetadataDescribe(id= "LHDE0017014",name = "复审人ID",eName = "REVIEW_OPERA_ID")
    private String reviewOperaId;


    @MetadataDescribe(id= "LHDE0017015",name = "复审人姓名",eName = "REVIEW_OPERA_NAME")
    private String reviewOperaName;


    @MetadataDescribe(id= "LHDE0017016",name = "作废标志",eName = "CANCEL_FLAG")
    private String cancelFlag;


    @MetadataDescribe(id= "LHDE0017017",name = "取消日期时间",eName = "CANCEL_DATE_TIME")
    private String cancelDateTime;

    @MetadataDescribe(id= "LHDE0017018",name = "撤销原因描述",eName = "CANCEL_REASON_DESC")
    private String cancelReasonDesc;

    @MetadataDescribe(id= "LHDE0017019",name = "撤销人ID",eName = "CANCEL_OPERA_ID")
    private String cancelOperaId;

    @MetadataDescribe(id= "LHDE0017020",name = "撤销人姓名",eName = "CANCEL_OPERA_NAME")
    private String cancelOperaName;


    public String getOrderChargeItemId() {
        return orderChargeItemId;
    }

    public void setOrderChargeItemId(String orderChargeItemId) {
        this.orderChargeItemId = orderChargeItemId;
    }

    public String getOrderItemId() {
        return orderItemId;
    }

    public void setOrderItemId(String orderItemId) {
        this.orderItemId = orderItemId;
    }

    public String getPriceCode() {
        return priceCode;
    }

    public void setPriceCode(String priceCode) {
        this.priceCode = priceCode;
    }

    public String getChargeItemId() {
        return chargeItemId;
    }

    public void setChargeItemId(String chargeItemId) {
        this.chargeItemId = chargeItemId;
    }

    public String getChargeQuantity() {
        return chargeQuantity;
    }

    public void setChargeQuantity(String chargeQuantity) {
        this.chargeQuantity = chargeQuantity;
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

    public String getModifyDateTime() {
        return modifyDateTime;
    }

    public void setModifyDateTime(String modifyDateTime) {
        this.modifyDateTime = modifyDateTime;
    }

    public String getReviewFlag() {
        return reviewFlag;
    }

    public void setReviewFlag(String reviewFlag) {
        this.reviewFlag = reviewFlag;
    }

    public String getReviewDateTime() {
        return reviewDateTime;
    }

    public void setReviewDateTime(String reviewDateTime) {
        this.reviewDateTime = reviewDateTime;
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

    public String getCancelFlag() {
        return cancelFlag;
    }

    public void setCancelFlag(String cancelFlag) {
        this.cancelFlag = cancelFlag;
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
}
