package com.zebone.nhis.ma.pub.platform.send.impl.zsrm.vo.sch;

import com.zebone.nhis.ma.pub.platform.send.impl.zsrm.vo.base.CodeableConcept;
import com.zebone.nhis.ma.pub.platform.send.impl.zsrm.vo.base.Outcome;
import com.zebone.nhis.ma.pub.platform.send.impl.zsrm.vo.base.TextElement;

import java.util.List;
import java.util.Map;

public class SchDoctorOutcome extends Outcome {

    /**医生简介*/
    private String description;
    /**医生出诊地点*/
    private List<TextElement> address;
    /**医生照片base64码 data:*/
    private List<Map<String,String>> photo;
    /**职称信息*/
    private List<CodeableConcept> qualification;
    /**扩展属性*/
    private List<Map<String,Object>> extension;

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<TextElement> getAddress() {
        return address;
    }

    public void setAddress(List<TextElement> address) {
        this.address = address;
    }

    public List<Map<String, String>> getPhoto() {
        return photo;
    }

    public void setPhoto(List<Map<String, String>> photo) {
        this.photo = photo;
    }

    public List<CodeableConcept> getQualification() {
        return qualification;
    }

    public void setQualification(List<CodeableConcept> qualification) {
        this.qualification = qualification;
    }

    public List<Map<String, Object>> getExtension() {
        return extension;
    }

    public void setExtension(List<Map<String, Object>> extension) {
        this.extension = extension;
    }
}
