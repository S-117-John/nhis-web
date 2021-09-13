package com.zebone.nhis.webservice.syx.vo.platForm;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class PiAddrInfo {
    /**
     * 地址唯一标识
     */
    @XmlElement(name = "addrId")
    private String addrId;
    /**
     * 省
     */
    @XmlElement(name = "province")
    private String province;
    /**
     * 市
     */
    @XmlElement(name = "city")
    private String city;
    /**
     * 区/县
     */
    @XmlElement(name = "district")
    private String district;
    /**
     * 详细地址
     */
    @XmlElement(name = "address")
    private String address;
    /**
     * 联系人姓名
     */
    @XmlElement(name = "contact")
    private String contact;
    /**
     * 联系电话
     */
    @XmlElement(name = "phone")
    private String phone;
    /**
     * 顺序号
     */
    @XmlElement(name = "sortno")
    private String sortno;
    /**
     * 默认值
     */
    @XmlElement(name = "flagDef")
    private String flagDef;
    /**
     * 用户证件类型
     * 0-患者身份证件号码
     * 1-患者诊疗卡号码
     * 2-患者市民卡号码
     * 3-患者医保卡号码
     * 4-患者监护人身份证件号码
     * 5-患者电话
     */
    @XmlElement(name = "userCardType")
    private String userCardType;
    /**
     * 用户证件号码
     */
    @XmlElement(name = "userCardId")
    private String userCardId;

    public String getAddrId() {
        return addrId;
    }

    public void setAddrId(String addrId) {
        this.addrId = addrId;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getSortno() {
        return sortno;
    }

    public void setSortno(String sortno) {
        this.sortno = sortno;
    }

    public String getFlagDef() {
        return flagDef;
    }

    public void setFlagDef(String flagDef) {
        this.flagDef = flagDef;
    }

    public String getUserCardType() {
        return userCardType;
    }

    public void setUserCardType(String userCardType) {
        this.userCardType = userCardType;
    }

    public String getUserCardId() {
        return userCardId;
    }

    public void setUserCardId(String userCardId) {
        this.userCardId = userCardId;
    }
}
