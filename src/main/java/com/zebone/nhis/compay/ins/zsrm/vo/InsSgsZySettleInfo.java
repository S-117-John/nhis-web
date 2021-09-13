package com.zebone.nhis.compay.ins.zsrm.vo;

import java.util.List;

public class InsSgsZySettleInfo extends InsSgsPubParam {
    /**
     * 医疗机构名称
     */
    private String akb021;
    /**
     * 医院等级
     */
    private String bkc110;
    /**
     * 工伤保险就医登记号
     */
    private String aaz218;
    /**
     * 是否政府办基层医疗机构
     */
    private String bkc119;
    /**
     * 先行支付
     */
    private String bka989;
    /**
     * 姓名
     */
    private String aac003;
    /**
     * 人员类别
     */
    private String bka004;
    /**
     * 性别
     */
    private String aac004;
    /**
     * 个人电脑号
     */
    private String aac001;
    /**
     * 社保卡号
     */
    private String bka100;
    /**
     * 待遇类别
     */
    private String bka006;
    /**
     * 业务类别
     */
    private String aka130;
    /**
     * 结算时间
     */
    private String bka045;
    /**
     * 科别
     */
    private String bka020;
    /**
     * 住院号
     */
    private String bka025;
    /**
     * 住院时间
     */
    private String bka017;
    /**
     * 出院时间
     */
    private String bka032;
    /**
     * 住院天数
     */
    private String bka030;
    /**
     * 制单人
     */
    private String aae011;
    /**
     * 打印日期
     */
    private String bka890;
    private List<ZyFundInfo> fundList;

    public String getAkb021() {
        return akb021;
    }

    public void setAkb021(String akb021) {
        this.akb021 = akb021;
    }

    public String getBkc110() {
        return bkc110;
    }

    public void setBkc110(String bkc110) {
        this.bkc110 = bkc110;
    }

    public String getAaz218() {
        return aaz218;
    }

    public void setAaz218(String aaz218) {
        this.aaz218 = aaz218;
    }

    public String getBkc119() {
        return bkc119;
    }

    public void setBkc119(String bkc119) {
        this.bkc119 = bkc119;
    }

    public String getBka989() {
        return bka989;
    }

    public void setBka989(String bka989) {
        this.bka989 = bka989;
    }

    public String getAac003() {
        return aac003;
    }

    public void setAac003(String aac003) {
        this.aac003 = aac003;
    }

    public String getBka004() {
        return bka004;
    }

    public void setBka004(String bka004) {
        this.bka004 = bka004;
    }

    public String getAac004() {
        return aac004;
    }

    public void setAac004(String aac004) {
        this.aac004 = aac004;
    }

    public String getAac001() {
        return aac001;
    }

    public void setAac001(String aac001) {
        this.aac001 = aac001;
    }

    public String getBka100() {
        return bka100;
    }

    public void setBka100(String bka100) {
        this.bka100 = bka100;
    }

    public String getBka006() {
        return bka006;
    }

    public void setBka006(String bka006) {
        this.bka006 = bka006;
    }

    public String getAka130() {
        return aka130;
    }

    public void setAka130(String aka130) {
        this.aka130 = aka130;
    }

    public String getBka045() {
        return bka045;
    }

    public void setBka045(String bka045) {
        this.bka045 = bka045;
    }

    public String getBka020() {
        return bka020;
    }

    public void setBka020(String bka020) {
        this.bka020 = bka020;
    }

    public String getBka025() {
        return bka025;
    }

    public void setBka025(String bka025) {
        this.bka025 = bka025;
    }

    public String getBka017() {
        return bka017;
    }

    public void setBka017(String bka017) {
        this.bka017 = bka017;
    }

    public String getBka032() {
        return bka032;
    }

    public void setBka032(String bka032) {
        this.bka032 = bka032;
    }

    public String getBka030() {
        return bka030;
    }

    public void setBka030(String bka030) {
        this.bka030 = bka030;
    }

    public String getAae011() {
        return aae011;
    }

    public void setAae011(String aae011) {
        this.aae011 = aae011;
    }

    public String getBka890() {
        return bka890;
    }

    public void setBka890(String bka890) {
        this.bka890 = bka890;
    }

    public List<ZyFundInfo> getFundList() {
        return fundList;
    }

    public void setFundList(List<ZyFundInfo> fundList) {
        this.fundList = fundList;
    }
}
