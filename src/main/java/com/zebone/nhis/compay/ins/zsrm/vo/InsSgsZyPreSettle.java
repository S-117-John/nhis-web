package com.zebone.nhis.compay.ins.zsrm.vo;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "program")
public class InsSgsZyPreSettle extends InsSgsPubParam {
    /**
     *医院编号
     */
    @XmlElement(name = "akb020")
    private String akb020;

    /**
     * 就医登记号
     */
    @XmlElement(name = "aaz218")
    private String aaz218;

    /**
     * 医疗总费用
     */
    @XmlElement(name = "akc264")
    private String akc264;

    /**
     * 个人自付
     */
    @XmlElement(name = "bka831")
    private String bka831;
    /**
     * 工伤保险支付
     */
    @XmlElement(name = "bka832")
    private String bka832;

    /**
     * 全自费费用
     */
    @XmlElement(name = "bka825")
    private String bka825;

    /**
     * 部分自费费用
     */
    @XmlElement(name = "bka826")
    private String bka826;

    /**
     * 起付线费用
     */
    @XmlElement(name = "aka151")
    private String aka151;

    /**
     * 超共付端费用个人自付
     */
    @XmlElement(name = "bka838")
    private String bka838;

    /**
     * 个人现金支付
     */
    @XmlElement(name = "akb067")
    private String akb067;

    /**
     * 个人账户支付
     */
    @XmlElement(name = "akb066")
    private String akb066;

    /**
     * 民政救助金支付
     */
    @XmlElement(name = "bka821")
    private String bka821;

    /**
     * 其他支付
     */
    @XmlElement(name = "bka839")
    private String bka839;

    /**
     * 工伤保险统筹基金支付
     */
    @XmlElement(name = "ake039")
    private String ake039;

    /**
     * 公务员医疗补助基金支付
     */
    @XmlElement(name = "ake035")
    private String ake035;

    /**
     * 企业补充工伤保险基金支付
     */
    @XmlElement(name = "ake026")
    private String ake026;

    /**
     * 大额医疗费用补助基金支付
     */
    @XmlElement(name = "ake029")
    private String ake029;

    /**
     * 单位支付
     */
    @XmlElement(name = "bka841")
    private String bka841;

    /**
     * 医院垫付
     */
    @XmlElement(name = "bka842")
    private String bka842;

    /**
     * 其他基金支付
     */
    @XmlElement(name = "bka840")
    private String bka840;

    public String getAkb020() {
        return akb020;
    }

    public void setAkb020(String akb020) {
        this.akb020 = akb020;
    }

    public String getAaz218() {
        return aaz218;
    }

    public void setAaz218(String aaz218) {
        this.aaz218 = aaz218;
    }

    public String getAkc264() {
        return akc264;
    }

    public void setAkc264(String akc264) {
        this.akc264 = akc264;
    }

    public String getBka831() {
        return bka831;
    }

    public void setBka831(String bka831) {
        this.bka831 = bka831;
    }

    public String getBka832() {
        return bka832;
    }

    public void setBka832(String bka832) {
        this.bka832 = bka832;
    }

    public String getBka825() {
        return bka825;
    }

    public void setBka825(String bka825) {
        this.bka825 = bka825;
    }

    public String getBka826() {
        return bka826;
    }

    public void setBka826(String bka826) {
        this.bka826 = bka826;
    }

    public String getAka151() {
        return aka151;
    }

    public void setAka151(String aka151) {
        this.aka151 = aka151;
    }

    public String getBka838() {
        return bka838;
    }

    public void setBka838(String bka838) {
        this.bka838 = bka838;
    }

    public String getAkb067() {
        return akb067;
    }

    public void setAkb067(String akb067) {
        this.akb067 = akb067;
    }

    public String getAkb066() {
        return akb066;
    }

    public void setAkb066(String akb066) {
        this.akb066 = akb066;
    }

    public String getBka821() {
        return bka821;
    }

    public void setBka821(String bka821) {
        this.bka821 = bka821;
    }

    public String getBka839() {
        return bka839;
    }

    public void setBka839(String bka839) {
        this.bka839 = bka839;
    }

    public String getAke039() {
        return ake039;
    }

    public void setAke039(String ake039) {
        this.ake039 = ake039;
    }

    public String getAke035() {
        return ake035;
    }

    public void setAke035(String ake035) {
        this.ake035 = ake035;
    }

    public String getAke026() {
        return ake026;
    }

    public void setAke026(String ake026) {
        this.ake026 = ake026;
    }

    public String getAke029() {
        return ake029;
    }

    public void setAke029(String ake029) {
        this.ake029 = ake029;
    }

    public String getBka841() {
        return bka841;
    }

    public void setBka841(String bka841) {
        this.bka841 = bka841;
    }

    public String getBka842() {
        return bka842;
    }

    public void setBka842(String bka842) {
        this.bka842 = bka842;
    }

    public String getBka840() {
        return bka840;
    }

    public void setBka840(String bka840) {
        this.bka840 = bka840;
    }
}
