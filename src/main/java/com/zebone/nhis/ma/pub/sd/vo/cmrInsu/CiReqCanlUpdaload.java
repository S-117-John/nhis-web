package com.zebone.nhis.ma.pub.sd.vo.cmrInsu;

public class CiReqCanlUpdaload {

    /**
     * 就诊流水号
     */
    private String medicalNum;

    /**
     * 被撤销交易流水号
     */
    private String medicalNumm;

    /**
     * 处方流水号
     */
    private String recipeSerialNum;

    /**
     * 处方号
     */
    private String recipeNum;

    /**
     * 经办时间
     */
    private String updateDate;

    /**
     * 经办人
     */
    private String updateBy;

    public String getMedicalNum() {
        return medicalNum;
    }

    public void setMedicalNum(String medicalNum) {
        this.medicalNum = medicalNum;
    }

    public String getMedicalNumm() {
        return medicalNumm;
    }

    public void setMedicalNumm(String medicalNumm) {
        this.medicalNumm = medicalNumm;
    }

    public String getRecipeSerialNum() {
        return recipeSerialNum;
    }

    public void setRecipeSerialNum(String recipeSerialNum) {
        this.recipeSerialNum = recipeSerialNum;
    }

    public String getRecipeNum() {
        return recipeNum;
    }

    public void setRecipeNum(String recipeNum) {
        this.recipeNum = recipeNum;
    }

    public String getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(String updateDate) {
        this.updateDate = updateDate;
    }

    public String getUpdateBy() {
        return updateBy;
    }

    public void setUpdateBy(String updateBy) {
        this.updateBy = updateBy;
    }
}
