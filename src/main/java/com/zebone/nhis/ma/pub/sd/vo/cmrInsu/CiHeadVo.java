package com.zebone.nhis.ma.pub.sd.vo.cmrInsu;

public class CiHeadVo {

    /**
     * 接口编号
     *详细设计中的接口编号
     * 是
     */
    private String busseID;

    /**
     *发送方交易流水号
     *时间
     * (12,YYYYMMDDHH24MISS)-发起
     * 方编号(最大 20,平台指定的医
     * 疗机构编码)-流水号(6)，之间
     * 用-分隔
     * 例：
     * 20150701083030-10011001-000
     * 001
     *
     *是
     */
    private String sendTradeNum;

    /**
     *发起方编号
     *平台统一提供
     *是
     */
    private String senderCode;

    /**
     *发起方名称
     *是
     */
    private String senderName;

    /**
     *receiverCode
     * 接收方编号
     * 是
     */
    private String receiverCode;

    /**
     *接收方名称
     * 是
     */
    private String receiverName;

    /**
     *提供方编号
     */
    private String providerCode;

    /**
     *提供方名称
     */
    private String providerName;

    /**
     *第三方编码
     *数据字典：
     * 1-市医保
     * 2-省医保
     * 3-新农合
     * 4-自费
     * 5-公费医疗
     * 6-其他
     * 是
     */
    private String intermediaryCode;

    /**
     *第三方名称
     */
    private String intermediaryName;

    /**
     *操作员编号
     * 是
     */
    private String hosorgNum;

    /**
     *操作员名称
     * 是
     */
    private String hosorgName;

    /**
     *系统类型
     *交易默认 1
     * 是
     */
    private String systemType;

    /**
     *业务类型
     * 数据字典：
     * 2-商保理赔
     * 6-商保调查
     * 是
     */
    private String busenissType;

    /**
     *标准的版本号信息
     * 是
     */
    private String standardVersionCode;

    /**
     *客户端mac地址
     */
    private String clientmacAddress;

    /**
     *记录数
     */
    private String recordCount;

    public String getBusseID() {
        return busseID;
    }

    public void setBusseID(String busseID) {
        this.busseID = busseID;
    }

    public String getSendTradeNum() {
        return sendTradeNum;
    }

    public void setSendTradeNum(String sendTradeNum) {
        this.sendTradeNum = sendTradeNum;
    }

    public String getSenderCode() {
        return senderCode;
    }

    public void setSenderCode(String senderCode) {
        this.senderCode = senderCode;
    }

    public String getSenderName() {
        return senderName;
    }

    public void setSenderName(String senderName) {
        this.senderName = senderName;
    }

    public String getReceiverCode() {
        return receiverCode;
    }

    public void setReceiverCode(String receiverCode) {
        this.receiverCode = receiverCode;
    }

    public String getReceiverName() {
        return receiverName;
    }

    public void setReceiverName(String receiverName) {
        this.receiverName = receiverName;
    }

    public String getProviderCode() {
        return providerCode;
    }

    public void setProviderCode(String providerCode) {
        this.providerCode = providerCode;
    }

    public String getProviderName() {
        return providerName;
    }

    public void setProviderName(String providerName) {
        this.providerName = providerName;
    }

    public String getIntermediaryCode() {
        return intermediaryCode;
    }

    public void setIntermediaryCode(String intermediaryCode) {
        this.intermediaryCode = intermediaryCode;
    }

    public String getIntermediaryName() {
        return intermediaryName;
    }

    public void setIntermediaryName(String intermediaryName) {
        this.intermediaryName = intermediaryName;
    }

    public String getHosorgNum() {
        return hosorgNum;
    }

    public void setHosorgNum(String hosorgNum) {
        this.hosorgNum = hosorgNum;
    }

    public String getHosorgName() {
        return hosorgName;
    }

    public void setHosorgName(String hosorgName) {
        this.hosorgName = hosorgName;
    }

    public String getSystemType() {
        return systemType;
    }

    public void setSystemType(String systemType) {
        this.systemType = systemType;
    }

    public String getBusenissType() {
        return busenissType;
    }

    public void setBusenissType(String busenissType) {
        this.busenissType = busenissType;
    }

    public String getStandardVersionCode() {
        return standardVersionCode;
    }

    public void setStandardVersionCode(String standardVersionCode) {
        this.standardVersionCode = standardVersionCode;
    }

    public String getClientmacAddress() {
        return clientmacAddress;
    }

    public void setClientmacAddress(String clientmacAddress) {
        this.clientmacAddress = clientmacAddress;
    }

    public String getRecordCount() {
        return recordCount;
    }

    public void setRecordCount(String recordCount) {
        this.recordCount = recordCount;
    }
}
