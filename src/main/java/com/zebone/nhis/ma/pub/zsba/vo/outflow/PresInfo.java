package com.zebone.nhis.ma.pub.zsba.vo.outflow;

import com.alibaba.fastjson.annotation.JSONField;

import java.util.Date;
import java.util.List;
/**
 * 处方信息
 */
public class PresInfo {
    private String doctorIdOutter;
    private String doctorIdcard;
    private String doctorNameOutter;
    private String doctorPhone;
    private String reviewerNameOutter;
    private String reviewerIdOutter;
    private String reviewerIdcard;
    private String reviewerPhone;
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private Date prescriptionTime;
    private String recipeIdOutter;
    private String recipeTypeCode;
    private String recipe_conclusion;
    private Integer recipeLock;
    private String reason;
    private List<PresDetail> details;

    public String getDoctorIdOutter() {
        return doctorIdOutter;
    }

    public void setDoctorIdOutter(String doctorIdOutter) {
        this.doctorIdOutter = doctorIdOutter;
    }

    public String getDoctorIdcard() {
        return doctorIdcard;
    }

    public void setDoctorIdcard(String doctorIdcard) {
        this.doctorIdcard = doctorIdcard;
    }

    public String getDoctorNameOutter() {
        return doctorNameOutter;
    }

    public void setDoctorNameOutter(String doctorNameOutter) {
        this.doctorNameOutter = doctorNameOutter;
    }

    public String getDoctorPhone() {
        return doctorPhone;
    }

    public void setDoctorPhone(String doctorPhone) {
        this.doctorPhone = doctorPhone;
    }

    public String getReviewerNameOutter() {
        return reviewerNameOutter;
    }

    public void setReviewerNameOutter(String reviewerNameOutter) {
        this.reviewerNameOutter = reviewerNameOutter;
    }

    public String getReviewerIdOutter() {
        return reviewerIdOutter;
    }

    public void setReviewerIdOutter(String reviewerIdOutter) {
        this.reviewerIdOutter = reviewerIdOutter;
    }

    public String getReviewerIdcard() {
        return reviewerIdcard;
    }

    public void setReviewerIdcard(String reviewerIdcard) {
        this.reviewerIdcard = reviewerIdcard;
    }

    public String getReviewerPhone() {
        return reviewerPhone;
    }

    public void setReviewerPhone(String reviewerPhone) {
        this.reviewerPhone = reviewerPhone;
    }

    public Date getPrescriptionTime() {
        return prescriptionTime;
    }

    public void setPrescriptionTime(Date prescriptionTime) {
        this.prescriptionTime = prescriptionTime;
    }

    public String getRecipeIdOutter() {
        return recipeIdOutter;
    }

    public void setRecipeIdOutter(String recipeIdOutter) {
        this.recipeIdOutter = recipeIdOutter;
    }

    public String getRecipeTypeCode() {
        return recipeTypeCode;
    }

    public void setRecipeTypeCode(String recipeTypeCode) {
        this.recipeTypeCode = recipeTypeCode;
    }

    public String getRecipe_conclusion() {
        return recipe_conclusion;
    }

    public void setRecipe_conclusion(String recipe_conclusion) {
        this.recipe_conclusion = recipe_conclusion;
    }

    public Integer getRecipeLock() {
        return recipeLock;
    }

    public void setRecipeLock(Integer recipeLock) {
        this.recipeLock = recipeLock;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public List<PresDetail> getDetails() {
        return details;
    }

    public void setDetails(List<PresDetail> details) {
        this.details = details;
    }
}