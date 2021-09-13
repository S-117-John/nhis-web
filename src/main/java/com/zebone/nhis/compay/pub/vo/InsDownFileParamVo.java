package com.zebone.nhis.compay.pub.vo;

public class InsDownFileParamVo {
    private String fileNo;
    private String fileName;

    public InsDownFileParamVo() {
    }

    public InsDownFileParamVo(String fileNo, String fileName) {
        this.fileNo = fileNo;
        this.fileName = fileName;
    }

    public String getFileNo() {
        return fileNo;
    }

    public void setFileNo(String fileNo) {
        this.fileNo = fileNo;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

}
