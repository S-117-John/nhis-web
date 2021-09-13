package com.zebone.nhis.bl.pub.vo;

/***
 * 中银聚合支付入参
 * @Auther: wuqiang
 * @Date: 2018/12/5 16:06
 * @Description:
 */

public class ZyPayInputVo {


    /**
     * 订单号唯一：
     * 1.下单支付时无需传入，支付成功后会返回订单号
     * 2.撤销，查询，退款时此号必须传
     */
     private String outTradeNo;
    /**
     * 请求IP地址(必须传)
     */
    private String createIp;

    /**
     * 请求端口(默认80)
     */
    private String noticePort;
    /**
     * 平台商户号 (无需传后台获取)
     */
    private String merchantId;
    /**
     * 支付场景 (可为空)
     */
    private String scenesPay;
    /**
     * 方法名称 (无需传)
     */
    private String serviceName;
    /**
     * 授权码(必传)
     */
    private String authCode;
    /**
     *  订单金额，单位为分(必传)，
     */
    private int totalFee;
    /**
     * 支付标题(必传)
     */
    private String body;
    /**
     * 支付详情(必传)
     */
    private String detail;
    /**
     * 支付类型(无需传)
     */
    private String tradeType;
    /**
     * 门店ID (选传)
     */
    private String storeId;
    /**
     * 设备标识(必传)
     */
    private String deviceInfo;
    /**
     * 操作员ID(必传)
     */
    private String operateId;
    /**
     * 限制信用卡使用 (选传)
     */
    private String limitPay;
//退款
    /**
     * 退款金额，单位分 (退款时必传)
     */
    private int  refundFee;
    /**
     * 退款原因 (退款时必传)
     */
    private String refundReason;

    public String getOutRefundNo() {
        return outRefundNo;
    }

    public void setOutRefundNo(String outRefundNo) {
        this.outRefundNo = outRefundNo;
    }

    /**
     * 退款单号 (退款成功后返回，退款查询时传入)
     */
    private String outRefundNo;
    /**
     *
     * 业务请求类型：(必传，一般用到1支付，4退款)
     * 1：统一下单
     * 2：查询订单
     *:3：撤销订单
     *:4：申请退款
     * 5：查询退款
     */
    private String inaPayType;

    public int getRefundFee() {
        return refundFee;
    }

    public void setRefundFee(int refundFee) {
        this.refundFee = refundFee;
    }

    public String getRefundReason() {
        return refundReason;
    }

    public void setRefundReason(String refundReason) {
        this.refundReason = refundReason;
    }




    public String getCreateIp() {
        return createIp;
    }


    public void setCreateIp(String createIp) {
        this.createIp = createIp;
    }

    public String getNoticePort() {
        return noticePort;
    }

    public void setNoticePort(String noticePort) {
        this.noticePort = noticePort;
    }

    public String getMerchantId() {
        return merchantId;
    }

    public void setMerchantId(String merchantId) {
        this.merchantId = merchantId;
    }

    public String getScenesPay() {
        return scenesPay;
    }

    public void setScenesPay(String scenesPay) {
        this.scenesPay = scenesPay;
    }

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public String getAuthCode() {
        return authCode;
    }

    public void setAuthCode(String authCode) {
        this.authCode = authCode;
    }

    public int getTotalFee() {
        return totalFee;
    }

    public void setTotalFee(int totalFee) {
        this.totalFee = totalFee;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public String getTradeType() {
        return tradeType;
    }

    public void setTradeType(String tradeType) {
        this.tradeType = tradeType;
    }

    public String getStoreId() {
        return storeId;
    }

    public void setStoreId(String storeId) {
        this.storeId = storeId;
    }

    public String getDeviceInfo() {
        return deviceInfo;
    }

    public void setDeviceInfo(String deviceInfo) {
        this.deviceInfo = deviceInfo;
    }

    public String getOperateId() {
        return operateId;
    }

    public void setOperateId(String operateId) {
        this.operateId = operateId;
    }

    public String getLimitPay() {
        return limitPay;
    }

    public void setLimitPay(String limitPay) {
        this.limitPay = limitPay;
    }
    public String getOutTradeNo() {
        return outTradeNo;
    }

    public void setOutTradeNo(String outTradeNo) {
        this.outTradeNo = outTradeNo;
    }

    public String getInaPayType() {
        return inaPayType;
    }

    public void setInaPayType(String inaPayType) {
        this.inaPayType = inaPayType;
    }

}
