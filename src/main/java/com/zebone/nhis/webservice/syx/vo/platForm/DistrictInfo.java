package com.zebone.nhis.webservice.syx.vo.platForm;

import javax.xml.bind.annotation.*;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class DistrictInfo {

    /**
     * 分院编码
     */
    @XmlElement(name = "districtDeptId")
    private String districtDeptId;
    /**
     * 分院名称
     */
    @XmlElement(name = "districtDeptName")
    private String districtDeptName;
    /**
     * 分院地址
     */
    @XmlElement(name = "districtAddr")
    private String districtAddr;

//----------------------------------------------------

    /**
     * 医院名称
     */
    @XmlTransient
    private String hospitalName;

    /**
     * 医院地址
     */
    @XmlTransient
    private String addr;
    /**
     * 医院电话
     */
    @XmlTransient
    private String tel;
    /**
     * 医院网址
     */
    @XmlTransient
    private String webSite;
    /**
     * 医院简介
     */
    @XmlTransient
    private String hospDesc;
    /**
     * 最大预约天数
     */
    @XmlTransient
    private String maxRegDays;

    public String getHospitalName() {
        return hospitalName;
    }

    public void setHospitalName(String hospitalName) {
        this.hospitalName = hospitalName;
    }

    public String getAddr() {
        return addr;
    }

    public void setAddr(String addr) {
        this.addr = addr;
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public String getWebSite() {
        return webSite;
    }

    public void setWebSite(String webSite) {
        this.webSite = webSite;
    }

    public String getHospDesc() {
        return hospDesc;
    }

    public void setHospDesc(String hospDesc) {
        this.hospDesc = hospDesc;
    }

    public String getMaxRegDays() {
        return maxRegDays;
    }

    public void setMaxRegDays(String maxRegDays) {
        this.maxRegDays = maxRegDays;
    }

    public String getDistrictDeptName() {
        return districtDeptName;
    }

    public void setDistrictDeptName(String districtDeptName) {
        this.districtDeptName = districtDeptName;
    }

    public String getDistrictAddr() {
        return districtAddr;
    }

    public void setDistrictAddr(String districtAddr) {
        this.districtAddr = districtAddr;
    }

    public String getDistrictDeptId() {
        return districtDeptId;
    }

    public void setDistrictDeptId(String districtDeptId) {
        this.districtDeptId = districtDeptId;
    }
}
