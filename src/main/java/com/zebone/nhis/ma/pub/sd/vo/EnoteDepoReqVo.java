package com.zebone.nhis.ma.pub.sd.vo;

import com.alibaba.fastjson.annotation.JSONField;

import java.util.Date;
import java.util.List;

public class EnoteDepoReqVo {

    /**
     * 业务标识
     * true
     * 07:标识住院预交金
     * 08:标识体检预交金
     */
    @JSONField(ordinal = 0)
    private String busType;

    /**
     * 预交金业务流水号
     * true
     */
    @JSONField(ordinal = 1)
    private String busNo;

    /**
     * 患者姓名
     * true
     */
    @JSONField(ordinal = 2)
    private String payer;

    /**
     * 业务发生时间
     * true
     * yyyyMMddHHmmssSSS
     */
    @JSONField(ordinal = 3,format = "yyyyMMddHHmmssSSS")
    private Date busDateTime;

    /**
     * 开票点编码
     * true
     */
    @JSONField(ordinal = 4)
    private String placeCode;

    /**
     * 收费员
     * true
     */
    @JSONField(ordinal = 5)
    private String payee;

    /**
     * 缴款人
     * true
     */
    @JSONField(ordinal = 6)
    private String drawee;

    /**
     * 票据编制人
     * true
     */
    @JSONField(ordinal = 7)
    private String author;

    /**
     * 患者手机号码
     * false
     */
    @JSONField(ordinal = 8)
    private String tel;

    /**
     * 患者邮箱地址
     * false
     */
    @JSONField(ordinal = 9)
    private String email;

    /**
     * 统一社会信用代码
     * false
     * 个人患者可填身份证号码
     */
    @JSONField(ordinal = 10)
    private String idCardNo;

    /**
     * 卡类型
     * true
     */
    @JSONField(ordinal = 11)
    private String cardType;

    /**
     * 卡号
     * true
     */
    @JSONField(ordinal = 12)
    private String cardNo;

    /**
     * 预缴金金额,保留两位小数
     */
    @JSONField(ordinal = 13)
    private Double amt;

    /**
     * 预缴金账户余额
     */
    @JSONField(ordinal = 14)
    private Double ownAcBalance;

    /**
     * 入院科室名称
     * false
     */
    @JSONField(ordinal = 15)
    private String category;

    /**
     * 入院科室编码
     * false
     */
    @JSONField(ordinal = 16)
    private String categoryCode;

    /**
     * 住院日期
     * false
     * yyyy-MM-dd
     */
    @JSONField(ordinal = 17,format = "yyyy-MM-dd")
    private Date inHospitalDate;

    /**
     * 患者住院号
     * true
     */
    @JSONField(ordinal = 18)
    private String hospitalNo;

    /**
     * 住院就诊编号
     * true
     */
    @JSONField(ordinal = 19)
    private String visitNo;

    /**
     * 患者唯一ID
     * false
     */
    @JSONField(ordinal = 20)
    private String patientId;

    /**
     * 患者就诊编号
     * false
     */
    @JSONField(ordinal = 21)
    private String patientNo;

    /**
     * 病历号
     * false
     */
    @JSONField(ordinal = 22)
    private String caseNumber;

    /**
     * 交费渠道列表
     * true
     */
    @JSONField(ordinal = 23)
    private List<PayChannelDetailVo> payChannelDetail;

    /**
     * 账户开户行
     */
    @JSONField(ordinal = 24)
    private String accountName;

    /**
     * 账户号码
     */
    @JSONField(ordinal = 25)
    private String accountNo;

    /**
     * 账户开户行
     */
    @JSONField(ordinal = 26)
    private String accountBank;

    /**
     * 备注
     * false
     */
    @JSONField(ordinal = 27)
    private String remark;

    /**
     * 工作单位或地址
     */
    @JSONField(ordinal = 28)
    private String workUnit;

    public String getBusNo() {
        return busNo;
    }

    public void setBusNo(String busNo) {
        this.busNo = busNo;
    }

    public String getBusType() {
        return busType;
    }

    public void setBusType(String busType) {
        this.busType = busType;
    }

    public String getPayer() {
        return payer;
    }

    public void setPayer(String payer) {
        this.payer = payer;
    }



    public String getPlaceCode() {
        return placeCode;
    }

    public void setPlaceCode(String placeCode) {
        this.placeCode = placeCode;
    }

    public String getPayee() {
        return payee;
    }

    public void setPayee(String payee) {
        this.payee = payee;
    }

    public String getDrawee() {
        return drawee;
    }

    public void setDrawee(String drawee) {
        this.drawee = drawee;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getIdCardNo() {
        return idCardNo;
    }

    public void setIdCardNo(String idCardNo) {
        this.idCardNo = idCardNo;
    }

    public String getCardType() {
        return cardType;
    }

    public void setCardType(String cardType) {
        this.cardType = cardType;
    }

    public String getCardNo() {
        return cardNo;
    }

    public void setCardNo(String cardNo) {
        this.cardNo = cardNo;
    }

    public Double getAmt() {
        return amt;
    }

    public void setAmt(Double amt) {
        this.amt = amt;
    }

    public Double getOwnAcBalance() {
        return ownAcBalance;
    }

    public void setOwnAcBalance(Double ownAcBalance) {
        this.ownAcBalance = ownAcBalance;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getCategoryCode() {
        return categoryCode;
    }

    public void setCategoryCode(String categoryCode) {
        this.categoryCode = categoryCode;
    }

    public Date getBusDateTime() {
        return busDateTime;
    }

    public void setBusDateTime(Date busDateTime) {
        this.busDateTime = busDateTime;
    }

    public Date getInHospitalDate() {
        return inHospitalDate;
    }

    public void setInHospitalDate(Date inHospitalDate) {
        this.inHospitalDate = inHospitalDate;
    }

    public String getHospitalNo() {
        return hospitalNo;
    }

    public void setHospitalNo(String hospitalNo) {
        this.hospitalNo = hospitalNo;
    }

    public String getVisitNo() {
        return visitNo;
    }

    public void setVisitNo(String visitNo) {
        this.visitNo = visitNo;
    }

    public String getPatientId() {
        return patientId;
    }

    public void setPatientId(String patientId) {
        this.patientId = patientId;
    }

    public String getPatientNo() {
        return patientNo;
    }

    public void setPatientNo(String patientNo) {
        this.patientNo = patientNo;
    }

    public String getCaseNumber() {
        return caseNumber;
    }

    public void setCaseNumber(String caseNumber) {
        this.caseNumber = caseNumber;
    }

    public List<PayChannelDetailVo> getPayChannelDetail() {
        return payChannelDetail;
    }

    public void setPayChannelDetail(List<PayChannelDetailVo> payChannelDetail) {
        this.payChannelDetail = payChannelDetail;
    }

    public String getAccountName() {
        return accountName;
    }

    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }

    public String getAccountNo() {
        return accountNo;
    }

    public void setAccountNo(String accountNo) {
        this.accountNo = accountNo;
    }

    public String getAccountBank() {
        return accountBank;
    }

    public void setAccountBank(String accountBank) {
        this.accountBank = accountBank;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getWorkUnit() {
        return workUnit;
    }

    public void setWorkUnit(String workUnit) {
        this.workUnit = workUnit;
    }
}
