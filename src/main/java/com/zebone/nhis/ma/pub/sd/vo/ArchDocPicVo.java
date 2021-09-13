package com.zebone.nhis.ma.pub.sd.vo;

import com.zebone.nhis.common.module.arch.ArchDoc;

public class ArchDocPicVo extends ArchDoc {
    /**
     * 图片二进制
     */
    private byte[] picByte;

    /**
     * 文件名称，包含扩展名
     */
    private String fileName;

    /**
     * 修改前的记录主键
     */
    private String oldPkDoc;

    public String getOldPkDoc() {
        return oldPkDoc;
    }

    public void setOldPkDoc(String oldPkDoc) {
        this.oldPkDoc = oldPkDoc;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public byte[] getPicByte() {
        return picByte;
    }

    public void setPicByte(byte[] picByte) {
        this.picByte = picByte;
    }
}
