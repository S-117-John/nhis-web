package com.zebone.nhis.ma.pub.platform.send.impl.pskq.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.Date;
import java.util.List;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "AppCheckList")
public class StExInfoVo {

    @XmlElement(name = "ClinicCode")
    private String clinicCode;

    @XmlElement(name = "PatName")
    private String patName;

    @XmlElement(name = "Gender")
    private String gender;

    @XmlElement(name = "Birthday")
    private String birthday;

    @XmlElement(name = "PatientAge")
    private int patientAge;

    @XmlElement(name = "Tel")
    private String tel;

    @XmlElement(name = "IDCard")
    private String iDCard;

    @XmlElement(name = "Address")
    private String address;

    @XmlElement(name = "ApplyCode")
    private String applyCode;

    @XmlElement(name = "ApplyDocNo")
    private String applyDocNo;

    @XmlElement(name = "ApplyDocName")
    private String applyDocName;

    @XmlElement(name = "ClincDiagnose")
    private String clincDiagnose;

    @XmlElement(name = "His")
    private String his;

    @XmlElement(name = "ApplyDate")
    private String applyDate;

    @XmlElement(name = "CheckAim")
    private String checkAim;

    @XmlElement(name = "Bodypart")
    private String bodypart;

    @XmlElement(name = "Item")
    private List<StExItemInfoVo> itemList;

    public String getClinicCode() {
        return clinicCode;
    }

    public void setClinicCode(String clinicCode) {
        this.clinicCode = clinicCode;
    }

    public String getPatName() {
        return patName;
    }

    public void setPatName(String patName) {
        this.patName = patName;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public int getPatientAge() {
        return patientAge;
    }

    public void setPatientAge(int patientAge) {
        this.patientAge = patientAge;
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public String getiDCard() {
        return iDCard;
    }

    public void setiDCard(String iDCard) {
        this.iDCard = iDCard;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getApplyCode() {
        return applyCode;
    }

    public void setApplyCode(String applyCode) {
        this.applyCode = applyCode;
    }

    public String getApplyDocNo() {
        return applyDocNo;
    }

    public void setApplyDocNo(String applyDocNo) {
        this.applyDocNo = applyDocNo;
    }

    public String getApplyDocName() {
        return applyDocName;
    }

    public void setApplyDocName(String applyDocName) {
        this.applyDocName = applyDocName;
    }

    public String getClincDiagnose() {
        return clincDiagnose;
    }

    public void setClincDiagnose(String clincDiagnose) {
        this.clincDiagnose = clincDiagnose;
    }

    public String getHis() {
        return his;
    }

    public void setHis(String his) {
        this.his = his;
    }

    public String getApplyDate() {
        return applyDate;
    }

    public void setApplyDate(String applyDate) {
        this.applyDate = applyDate;
    }

    public String getCheckAim() {
        return checkAim;
    }

    public void setCheckAim(String checkAim) {
        this.checkAim = checkAim;
    }

    public String getBodypart() {
        return bodypart;
    }

    public void setBodypart(String bodypart) {
        this.bodypart = bodypart;
    }

    public List<StExItemInfoVo> getItemList() {
        return itemList;
    }

    public void setItemList(List<StExItemInfoVo> itemList) {
        this.itemList = itemList;
    }
}
