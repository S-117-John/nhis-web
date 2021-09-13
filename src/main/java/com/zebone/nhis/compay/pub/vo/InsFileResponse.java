package com.zebone.nhis.compay.pub.vo;

public class InsFileResponse {

    /** 文件查询号	字符型*/
    private String fileQuryNo;

    /** 文件名	字符型*/
    private String filename;

    /** 医药机构编号	字符型*/
    private String fixmedinsCode;

    /** 截止时间	字符型*/
    private String dldEndtime;

    public String getFileQuryNo() {
        return fileQuryNo;
    }

    public void setFileQuryNo(String fileQuryNo) {
        this.fileQuryNo = fileQuryNo;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public String getFixmedinsCode() {
        return fixmedinsCode;
    }

    public void setFixmedinsCode(String fixmedinsCode) {
        this.fixmedinsCode = fixmedinsCode;
    }

    public String getDldEndtime() {
        return dldEndtime;
    }

    public void setDldEndtime(String dldEndtime) {
        this.dldEndtime = dldEndtime;
    }
}
