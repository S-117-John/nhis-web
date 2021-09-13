package com.zebone.nhis.ma.pub.platform.send.impl.zsrm.vo.bl;

import com.alibaba.fastjson.annotation.JSONField;
import org.codehaus.jackson.annotate.JsonProperty;

import java.util.Date;

public class OrderDetail {
    //明细ID
    @JsonProperty("orderdetailid")
    private String OrderDetailID;
    //项目类型代码
    @JsonProperty("ordtypecode")
    private String OrdTypeCode;
    //项目类型名称
    @JsonProperty("ordtypedesc")
    private String OrdTypeDesc;
    //项目代码
    @JsonProperty("orditemcode")
    private String OrdItemCode;
    //项目内容
    @JsonProperty("orditemdesc")
    private String OrdItemDesc;
    //用药途径代码
    @JsonProperty("routecode")
    private String RouteCode;
    //用药途径名称
    @JsonProperty("routedesc")
    private String RouteDesc;
    //用药剂量-单次
    @JsonProperty("dose")
    private String Dose;
    //用药剂量单位
    private String DoseUnit;
    //药物使用总剂量
    @JsonProperty("sumdose")
    private String SumDose;
    //药物名称
    @JsonProperty("drugname")
    private String DrugName;
    //单价
    @JsonProperty("orderfee")
    private String OrderFee;
    //数量
    @JsonProperty("amount")
    private String Amount;
    //总价
    @JsonProperty("ordersumfee")
    private String OrderSumFee;
    //开立日期时间
    @JsonProperty("orddate")
    private Date OrdDate;
    //医护人员标识
    @JsonProperty("oacaretype")
    private String OACareType;
    //开立者代码
    @JsonProperty("ordauthcode")
    private String OrdAuthCode;
    //开立者姓名
    @JsonProperty("ordauthname")
    private String OrdAuthName;
    //开立科室代码
    @JsonProperty("ordloccode")
    private String OrdLocCode;
    //执行科室
    @JsonProperty("excloccode")
    private String excLocCode;
    //记费科室
    @JsonProperty("feeloccode")
    private String feeLocCode;
    //开立科室名称
    @JsonProperty("ordlocdesc")
    private String OrdLocDesc;
    //医嘱状态代码
    @JsonProperty("oeoristatuscode")
    private String OEORIStatusCode;
    //医护人员标识
    @JsonProperty("ovcaretype")
    private String OVCareType;
    //医嘱状态代码
    @JsonProperty("ordvericode")
    private String OrdVeriCode;
    //医护人员名称
    @JsonProperty("ordveriname")
    private String OrdVeriName;
    //医嘱备注信息
    @JsonProperty("ordnote")
    private String OrdNote;

    public String getOrderDetailID() {
        return OrderDetailID;
    }

    public void setOrderDetailID(String orderDetailID) {
        OrderDetailID = orderDetailID;
    }

    public String getOrdTypeCode() {
        return OrdTypeCode;
    }

    public void setOrdTypeCode(String ordTypeCode) {
        OrdTypeCode = ordTypeCode;
    }

    public String getOrdTypeDesc() {
        return OrdTypeDesc;
    }

    public void setOrdTypeDesc(String ordTypeDesc) {
        OrdTypeDesc = ordTypeDesc;
    }

    public String getOrdItemCode() {
        return OrdItemCode;
    }

    public void setOrdItemCode(String ordItemCode) {
        OrdItemCode = ordItemCode;
    }

    public String getOrdItemDesc() {
        return OrdItemDesc;
    }

    public void setOrdItemDesc(String ordItemDesc) {
        OrdItemDesc = ordItemDesc;
    }

    public String getRouteCode() {
        return RouteCode;
    }

    public void setRouteCode(String routeCode) {
        RouteCode = routeCode;
    }

    public String getRouteDesc() {
        return RouteDesc;
    }

    public void setRouteDesc(String routeDesc) {
        RouteDesc = routeDesc;
    }

    public String getDose() {
        return Dose;
    }

    public void setDose(String dose) {
        Dose = dose;
    }

    public String getSumDose() {
        return SumDose;
    }

    public void setSumDose(String sumDose) {
        SumDose = sumDose;
    }

    public String getDrugName() {
        return DrugName;
    }

    public void setDrugName(String drugName) {
        DrugName = drugName;
    }

    public String getOrderFee() {
        return OrderFee;
    }

    public void setOrderFee(String orderFee) {
        OrderFee = orderFee;
    }

    public String getAmount() {
        return Amount;
    }

    public void setAmount(String amount) {
        Amount = amount;
    }

    public String getOrderSumFee() {
        return OrderSumFee;
    }

    public void setOrderSumFee(String orderSumFee) {
        OrderSumFee = orderSumFee;
    }

    public String getDoseUnit() {
        return DoseUnit;
    }

    public void setDoseUnit(String doseUnit) {
        DoseUnit = doseUnit;
    }

    public Date getOrdDate() {
        return OrdDate;
    }

    public void setOrdDate(Date ordDate) {
        OrdDate = ordDate;
    }

    public String getOACareType() {
        return OACareType;
    }

    public void setOACareType(String OACareType) {
        this.OACareType = OACareType;
    }

    public String getOrdAuthCode() {
        return OrdAuthCode;
    }

    public void setOrdAuthCode(String ordAuthCode) {
        OrdAuthCode = ordAuthCode;
    }

    public String getOrdAuthName() {
        return OrdAuthName;
    }

    public void setOrdAuthName(String ordAuthName) {
        OrdAuthName = ordAuthName;
    }

    public String getOrdLocCode() {
        return OrdLocCode;
    }

    public void setOrdLocCode(String ordLocCode) {
        OrdLocCode = ordLocCode;
    }

    public String getOrdLocDesc() {
        return OrdLocDesc;
    }

    public void setOrdLocDesc(String ordLocDesc) {
        OrdLocDesc = ordLocDesc;
    }

    public String getOEORIStatusCode() {
        return OEORIStatusCode;
    }

    public void setOEORIStatusCode(String OEORIStatusCode) {
        this.OEORIStatusCode = OEORIStatusCode;
    }

    public String getOVCareType() {
        return OVCareType;
    }

    public void setOVCareType(String OVCareType) {
        this.OVCareType = OVCareType;
    }

    public String getOrdVeriCode() {
        return OrdVeriCode;
    }

    public void setOrdVeriCode(String ordVeriCode) {
        OrdVeriCode = ordVeriCode;
    }

    public String getOrdVeriName() {
        return OrdVeriName;
    }

    public void setOrdVeriName(String ordVeriName) {
        OrdVeriName = ordVeriName;
    }

    public String getOrdNote() {
        return OrdNote;
    }

    public void setOrdNote(String ordNote) {
        OrdNote = ordNote;
    }

    public String getExcLocCode() {
        return excLocCode;
    }

    public void setExcLocCode(String excLocCode) {
        this.excLocCode = excLocCode;
    }

    public String getFeeLocCode() {
        return feeLocCode;
    }

    public void setFeeLocCode(String feeLocCode) {
        this.feeLocCode = feeLocCode;
    }
}
