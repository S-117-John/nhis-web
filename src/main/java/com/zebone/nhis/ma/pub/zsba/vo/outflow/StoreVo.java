package com.zebone.nhis.ma.pub.zsba.vo.outflow;

import java.util.List;
import java.util.Map;

public class StoreVo {
    /** 药店id -处方内部药店唯一标识*/
    private String storeId;
    private String storeName;
    private String phone;
    /** 是否支持配送：0否、1是*/
    private Integer deliveryFlag;
    /** 配送模式-1.在线支付 2.货到付款*/
    private Integer deliveryMode;
    /** 参考运费	double	（8,2）	是	默认同城参考运费*/
    private Double deliveryFee;
    /** 距离	string	36	是	单位 km*/
    private String distance;
    /** 药店的代煎包配置	[{}]		否	{"capacity":100}*/
    private List<Map<String,Object>> drugCapacities;

    private List<DrugDetailVo> drugInfo;
    /** 院内药房标识	int	2	是	0=外部药房；1=院内药房 用于医院医保结算判断*/
    private Integer innerFlag;
    /** 医保标识	int	1	是	是否支持医保：0否、1是*/
    private Integer insuranceFlag;
    /** latitude	纬度	String	36	是	药店所在位置*/
    private String latitude;
    /** 经度	String	36	是	药店所在位置*/
    private String longitude;
    /**运营平台药店id(36)*/
    private String operatePlatformStoreId;
    /** 订单总额	double	（8,2）	是	*/
    private Double orderAmount;
    /** 订单总重	double	（8,2）	是	单位 g*/
    private Double orderWeightAmount;
    /**药店地址id(50)*/
    private String orgAddress;
    /** 支持支付方式标识	string	12	是	支付方式：1线上、2线下 多个以英文逗号","分隔*/
    private String payType;
    /** 自提标识	int	1	是	是否支持自提：0否、1是*/
    private Integer selfgetFlag;

    public String getStoreId() {
        return storeId;
    }

    public void setStoreId(String storeId) {
        this.storeId = storeId;
    }

    public String getStoreName() {
        return storeName;
    }

    public void setStoreName(String storeName) {
        this.storeName = storeName;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Integer getDeliveryFlag() {
        return deliveryFlag;
    }

    public void setDeliveryFlag(Integer deliveryFlag) {
        this.deliveryFlag = deliveryFlag;
    }

    public Integer getDeliveryMode() {
        return deliveryMode;
    }

    public void setDeliveryMode(Integer deliveryMode) {
        this.deliveryMode = deliveryMode;
    }

    public String getDistance() {
        return distance;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }

    public List<Map<String, Object>> getDrugCapacities() {
        return drugCapacities;
    }

    public void setDrugCapacities(List<Map<String, Object>> drugCapacities) {
        this.drugCapacities = drugCapacities;
    }

    public List<DrugDetailVo> getDrugInfo() {
        return drugInfo;
    }

    public void setDrugInfo(List<DrugDetailVo> drugInfo) {
        this.drugInfo = drugInfo;
    }

    public Integer getInnerFlag() {
        return innerFlag;
    }

    public void setInnerFlag(Integer innerFlag) {
        this.innerFlag = innerFlag;
    }

    public Integer getInsuranceFlag() {
        return insuranceFlag;
    }

    public void setInsuranceFlag(Integer insuranceFlag) {
        this.insuranceFlag = insuranceFlag;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getOperatePlatformStoreId() {
        return operatePlatformStoreId;
    }

    public void setOperatePlatformStoreId(String operatePlatformStoreId) {
        this.operatePlatformStoreId = operatePlatformStoreId;
    }

    public Double getOrderAmount() {
        return orderAmount;
    }

    public void setOrderAmount(Double orderAmount) {
        this.orderAmount = orderAmount;
    }

    public Double getOrderWeightAmount() {
        return orderWeightAmount;
    }

    public void setOrderWeightAmount(Double orderWeightAmount) {
        this.orderWeightAmount = orderWeightAmount;
    }

    public String getOrgAddress() {
        return orgAddress;
    }

    public void setOrgAddress(String orgAddress) {
        this.orgAddress = orgAddress;
    }

    public String getPayType() {
        return payType;
    }

    public void setPayType(String payType) {
        this.payType = payType;
    }

    public Integer getSelfgetFlag() {
        return selfgetFlag;
    }

    public void setSelfgetFlag(Integer selfgetFlag) {
        this.selfgetFlag = selfgetFlag;
    }

    public Double getDeliveryFee() {
        return deliveryFee;
    }

    public void setDeliveryFee(Double deliveryFee) {
        this.deliveryFee = deliveryFee;
    }
}
