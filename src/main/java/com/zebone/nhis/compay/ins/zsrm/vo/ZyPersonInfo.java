package com.zebone.nhis.compay.ins.zsrm.vo;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name="personinfo")
public class ZyPersonInfo {
    /**
     * 电脑号
     */
    @XmlElement(name = "aac001")
    private String aac001;
    /**
     * 姓名
     */
    @XmlElement(name = "aac003")
    private String aac003;
    /**
     * 性别
     */
    @XmlElement(name = "aac004")
    private String aac004;
    /**
     * 人员类别
     */
    @XmlElement(name = "bka004")
    private String bka004;
    /**
     * 用工形式
     */
    @XmlElement(name = "aac013")
    private String aac013;
    /**
     * 行政职务级别
     */
    @XmlElement(name = "bka005")
    private String bka005;
    /**
     * 社会保障号码
     */
    @XmlElement(name = "aac002")
    private String aac002;
    /**
     * 联系电话
     */
    @XmlElement(name = "aae005")
    private String aae005;
    /**
     * 出生日期
     */
    @XmlElement(name = "aac006")
    private String aac006;
    /**
     * 参保地区编码
     */
    @XmlElement(name = "baa027")
    private String baa027;
    /**
     * 单位编码
     */
    @XmlElement(name = "aab001")
    private String aab001;
    /**
     * 单位名称
     */
    @XmlElement(name = "bka008")
    private String bka008;
    /**
     * 险种编码
     */
    @XmlElement(name = "aae140")
    private String aae140;
    /**
     * 个人基金状态
     */
    @XmlElement(name = "bka888")
    private String bka888;
    /**
     * 工伤保险中心编码
     */
    @XmlElement(name = "aaa027")
    private String aaa027;
    /**
     * 申请号
     */
    @XmlElement(name = "aaz267")
    private String aaz267;
    /**
     * 待遇类别
     */
    @XmlElement(name = "bka006")
    private String bka006;
    /**
     * 疾病编码
     */
    @XmlElement(name = "bka026")
    private String bka026;
    /**
     * 工伤凭证号
     */
    @XmlElement(name = "bka042")
    private String bka042;
    /**
     * 开始日期
     */
    @XmlElement(name = "aae030")
    private String aae030;
    /**
     * 结束日期
     */
    @XmlElement(name = "aae031")
    private String aae031;

    public String getAac001() {
        return aac001;
    }

    public void setAac001(String aac001) {
        this.aac001 = aac001;
    }

    public String getAac003() {
        return aac003;
    }

    public void setAac003(String aac003) {
        this.aac003 = aac003;
    }

    public String getAac004() {
        return aac004;
    }

    public void setAac004(String aac004) {
        this.aac004 = aac004;
    }

    public String getBka004() {
        return bka004;
    }

    public void setBka004(String bka004) {
        this.bka004 = bka004;
    }

    public String getAac013() {
        return aac013;
    }

    public void setAac013(String aac013) {
        this.aac013 = aac013;
    }

    public String getBka005() {
        return bka005;
    }

    public void setBka005(String bka005) {
        this.bka005 = bka005;
    }

    public String getAac002() {
        return aac002;
    }

    public void setAac002(String aac002) {
        this.aac002 = aac002;
    }

    public String getAae005() {
        return aae005;
    }

    public void setAae005(String aae005) {
        this.aae005 = aae005;
    }

    public String getAac006() {
        return aac006;
    }

    public void setAac006(String aac006) {
        this.aac006 = aac006;
    }

    public String getBaa027() {
        return baa027;
    }

    public void setBaa027(String baa027) {
        this.baa027 = baa027;
    }

    public String getAab001() {
        return aab001;
    }

    public void setAab001(String aab001) {
        this.aab001 = aab001;
    }

    public String getBka008() {
        return bka008;
    }

    public void setBka008(String bka008) {
        this.bka008 = bka008;
    }

    public String getAae140() {
        return aae140;
    }

    public void setAae140(String aae140) {
        this.aae140 = aae140;
    }

    public String getBka888() {
        return bka888;
    }

    public void setBka888(String bka888) {
        this.bka888 = bka888;
    }

    public String getAaa027() {
        return aaa027;
    }

    public void setAaa027(String aaa027) {
        this.aaa027 = aaa027;
    }

    public String getAaz267() {
        return aaz267;
    }

    public void setAaz267(String aaz267) {
        this.aaz267 = aaz267;
    }

    public String getBka006() {
        return bka006;
    }

    public void setBka006(String bka006) {
        this.bka006 = bka006;
    }

    public String getBka026() {
        return bka026;
    }

    public void setBka026(String bka026) {
        this.bka026 = bka026;
    }

    public String getBka042() {
        return bka042;
    }

    public void setBka042(String bka042) {
        this.bka042 = bka042;
    }

    public String getAae030() {
        return aae030;
    }

    public void setAae030(String aae030) {
        this.aae030 = aae030;
    }

    public String getAae031() {
        return aae031;
    }

    public void setAae031(String aae031) {
        this.aae031 = aae031;
    }
}
