package com.zebone.nhis.ma.pub.zsba.vo.outflow;

public class AuditResult {

    /** 审核结果	string	2	是	0：不通过，1：通过*/
    private String result;
    /** 外部机构处方id*/
    private String recipeIdOutter;
    /** 审核药师	string	20	否	药师姓名*/
    private String checkDr;
    /**签名文件地址 */
    private String fileId;
    /** 签名文件图片地址*/
    private String fileUrl;
    /** 备注*/
    private String remark;

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getRecipeIdOutter() {
        return recipeIdOutter;
    }

    public void setRecipeIdOutter(String recipeIdOutter) {
        this.recipeIdOutter = recipeIdOutter;
    }

    public String getCheckDr() {
        return checkDr;
    }

    public void setCheckDr(String checkDr) {
        this.checkDr = checkDr;
    }

    public String getFileId() {
        return fileId;
    }

    public void setFileId(String fileId) {
        this.fileId = fileId;
    }

    public String getFileUrl() {
        return fileUrl;
    }

    public void setFileUrl(String fileUrl) {
        this.fileUrl = fileUrl;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}
