package com.zebone.nhis.ma.pub.platform.send.impl.zsrm.vo.patient;

import com.alibaba.fastjson.annotation.JSONField;
import com.zebone.nhis.ma.pub.platform.send.impl.zsrm.common.ZsphJsonDateDeserializer;
import com.zebone.nhis.ma.pub.platform.send.impl.zsrm.vo.base.CodeableConcept;
import com.zebone.nhis.ma.pub.platform.send.impl.zsrm.vo.base.Identifier;
import com.zebone.nhis.ma.pub.platform.send.impl.zsrm.vo.base.PhResource;
import com.zebone.nhis.ma.pub.platform.send.impl.zsrm.vo.base.TextElement;
import com.zebone.nhis.ma.pub.platform.send.impl.zsrm.vo.pv.Participant;
import org.codehaus.jackson.map.annotate.JsonDeserialize;

import java.util.Date;
import java.util.List;

public class PatientSave{
    private String hospitalmark;
    private String patientname;
    private String sex;
    private String age;
    private String idno;
    private String idtype;
    private String address;
    private String phone;
    private String cardtype;
    private String cardnum;
    private String healthcode;
    private String hisoperator;
    private String dateofbirth;
    private String authentication;
    private String guarderName;
    private String guarderCardType;
    private String guarderNo;
    private String patientno;
    private String healthcard;

    public String getHealthcard() {
        return healthcard;
    }

    public void setHealthcard(String healthcard) {
        this.healthcard = healthcard;
    }

    public String getPatientno() {
        return patientno;
    }

    public void setPatientno(String patientno) {
        this.patientno = patientno;
    }

    public String getGuarderName() {
        return guarderName;
    }

    public void setGuarderName(String guarderName) {
        this.guarderName = guarderName;
    }

    public String getGuarderCardType() {
        return guarderCardType;
    }

    public void setGuarderCardType(String guarderCardType) {
        this.guarderCardType = guarderCardType;
    }

    public String getGuarderNo() {
        return guarderNo;
    }

    public void setGuarderNo(String guarderNo) {
        this.guarderNo = guarderNo;
    }

    public String getAuthentication() {
        return authentication;
    }

    public void setAuthentication(String authentication) {
        this.authentication = authentication;
    }

    public String getIdtype() {
        return idtype;
    }

    public void setIdtype(String idtype) {
        this.idtype = idtype;
    }

    public String getHospitalmark() {
        return hospitalmark;
    }

    public void setHospitalmark(String hospitalmark) {
        this.hospitalmark = hospitalmark;
    }

    public String getPatientname() {
        return patientname;
    }

    public void setPatientname(String patientname) {
        this.patientname = patientname;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getIdno() {
        return idno;
    }

    public void setIdno(String idno) {
        this.idno = idno;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getCardtype() {
        return cardtype;
    }

    public void setCardtype(String cardtype) {
        this.cardtype = cardtype;
    }

    public String getCardnum() {
        return cardnum;
    }

    public void setCardnum(String cardnum) {
        this.cardnum = cardnum;
    }

    public String getHealthcode() {
        return healthcode;
    }

    public void setHealthcode(String healthcode) {
        this.healthcode = healthcode;
    }

    public String getHisoperator() {
        return hisoperator;
    }

    public void setHisoperator(String hisoperator) {
        this.hisoperator = hisoperator;
    }

    public String getDateofbirth() {
        return dateofbirth;
    }

    public void setDateofbirth(String dateofbirth) {
        this.dateofbirth = dateofbirth;
    }
}
