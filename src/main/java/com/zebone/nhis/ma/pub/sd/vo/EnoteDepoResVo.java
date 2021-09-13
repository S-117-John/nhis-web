package com.zebone.nhis.ma.pub.sd.vo;

public class EnoteDepoResVo {

    /**
     *  预交金凭证代码
     */
    private String voucherBatchCode;

    /**
     * 预交金凭证号码
     */
    private String voucherNo;

    /**
     * 预交金凭证校验码
     */
    private String voucherRandom;


    /**
     * 电子票据代码
     * true
     */
    private String billBatchCode;

    /**
     * 电子票据号码
     * true
     */
    private String billNo;

    /**
     * 电子校验码
     * true
     */
    private String random;

    /**
     * 电子票据生成时间
     * true
     * yyyyMMddHHmmssSSS
     */
    private String createTime;

    /**
     * 电子票据二维码图片数据
     * true
     * 该值已Base64编码，解析时需要Base64解码,图片格式为:PNG
     */
    private String billQRCode;

    /**
     * 电子票据H5页面URL
     * true
     */
    private String pictureUrl;

    /**
     * 电子红票票据代码
     */
    private String eScarletBillBatchCode;
    /**
     * 电子红票票据号
     */
    private String eScarletBillNo;
    /**
     * 电子红票校验码
     */
    private String eScarletRandom;

    public String getVoucherBatchCode() {
        return voucherBatchCode;
    }

    public void setVoucherBatchCode(String voucherBatchCode) {
        this.voucherBatchCode = voucherBatchCode;
    }

    public String getVoucherNo() {
        return voucherNo;
    }

    public void setVoucherNo(String voucherNo) {
        this.voucherNo = voucherNo;
    }

    public String getVoucherRandom() {
        return voucherRandom;
    }

    public void setVoucherRandom(String voucherRandom) {
        this.voucherRandom = voucherRandom;
    }

    public String getBillBatchCode() {
        return billBatchCode;
    }

    public void setBillBatchCode(String billBatchCode) {
        this.billBatchCode = billBatchCode;
    }

    public String getBillNo() {
        return billNo;
    }

    public void setBillNo(String billNo) {
        this.billNo = billNo;
    }

    public String getRandom() {
        return random;
    }

    public void setRandom(String random) {
        this.random = random;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getBillQRCode() {
        return billQRCode;
    }

    public void setBillQRCode(String billQRCode) {
        this.billQRCode = billQRCode;
    }

    public String getPictureUrl() {
        return pictureUrl;
    }

    public void setPictureUrl(String pictureUrl) {
        this.pictureUrl = pictureUrl;
    }
}
