package com.zebone.nhis.common.module.arch;

import com.zebone.nhis.common.module.BaseModule;
import com.zebone.platform.modules.dao.build.au.Field;
import com.zebone.platform.modules.dao.build.au.PK;
import com.zebone.platform.modules.dao.build.au.Table;
import java.util.Date;

@Table(value="ARCH_DOC")
public class ArchDoc extends BaseModule {
    @PK
    @Field(value="PK_ARCHDOC")
    private String pkArchdoc;

    @Field(value="PK_ARCHPV")
    private String pkArchpv;

    @Field(value="SORTNO")
    private Integer sortno;

    @Field(value="DT_MEDDOCTYPE")
    private String dtMeddoctype;

    @Field(value="NAME_DOC")
    private String nameDoc;

    @Field(value="PATH")
    private String path;

    @Field(value="DATE_UPLOAD")
    private Date dateUpload;

    @Field(value="PK_EMP_UPLOAD")
    private String pkEmpUpload;

    @Field(value="NAME_EMP_UPLOAD")
    private String nameEmpUpload;

    @Field(value="CNT_PRINT")
    private Integer cntPrint;

    @Field(value="FLAG_CANCEL")
    private String flagCancel;

    @Field(value="DATE_CANCEL")
    private Date dateCancel;

    @Field(value="PK_EMP_CANCEL")
    private String pkEmpCancel;

    @Field(value="NAME_EMP_CANCEL")
    private String nameEmpCancel;

    @Field(value="NOTE")
    private String note;

    @Field(value="CONTENT")
    private byte[] content;

    @Field(value="FLAG_LOOK")
    private String flagLook;

    public String getFlagLook() {
        return flagLook;
    }

    public void setFlagLook(String flagLook) {
        this.flagLook = flagLook;
    }

    public String getPkArchdoc() {
        return pkArchdoc;
    }

    public void setPkArchdoc(String pkArchdoc) {
        this.pkArchdoc = pkArchdoc == null ? null : pkArchdoc.trim();
    }

    public String getPkArchpv() {
        return pkArchpv;
    }

    public void setPkArchpv(String pkArchpv) {
        this.pkArchpv = pkArchpv == null ? null : pkArchpv.trim();
    }

    public Integer getSortno() {
        return sortno;
    }

    public void setSortno(Integer sortno) {
        this.sortno = sortno;
    }

    public String getDtMeddoctype() {
        return dtMeddoctype;
    }

    public void setDtMeddoctype(String dtMeddoctype) {
        this.dtMeddoctype = dtMeddoctype == null ? null : dtMeddoctype.trim();
    }

    public String getNameDoc() {
        return nameDoc;
    }

    public void setNameDoc(String nameDoc) {
        this.nameDoc = nameDoc == null ? null : nameDoc.trim();
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path == null ? null : path.trim();
    }

    public Date getDateUpload() {
        return dateUpload;
    }

    public void setDateUpload(Date dateUpload) {
        this.dateUpload = dateUpload;
    }

    public String getPkEmpUpload() {
        return pkEmpUpload;
    }

    public void setPkEmpUpload(String pkEmpUpload) {
        this.pkEmpUpload = pkEmpUpload == null ? null : pkEmpUpload.trim();
    }

    public String getNameEmpUpload() {
        return nameEmpUpload;
    }

    public void setNameEmpUpload(String nameEmpUpload) {
        this.nameEmpUpload = nameEmpUpload == null ? null : nameEmpUpload.trim();
    }

    public Integer getCntPrint() {
        return cntPrint;
    }

    public void setCntPrint(Integer cntPrint) {
        this.cntPrint = cntPrint;
    }

    public String getFlagCancel() {
        return flagCancel;
    }

    public void setFlagCancel(String flagCancel) {
        this.flagCancel = flagCancel == null ? null : flagCancel.trim();
    }

    public Date getDateCancel() {
        return dateCancel;
    }

    public void setDateCancel(Date dateCancel) {
        this.dateCancel = dateCancel;
    }

    public String getPkEmpCancel() {
        return pkEmpCancel;
    }

    public void setPkEmpCancel(String pkEmpCancel) {
        this.pkEmpCancel = pkEmpCancel == null ? null : pkEmpCancel.trim();
    }

    public String getNameEmpCancel() {
        return nameEmpCancel;
    }

    public void setNameEmpCancel(String nameEmpCancel) {
        this.nameEmpCancel = nameEmpCancel == null ? null : nameEmpCancel.trim();
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note == null ? null : note.trim();
    }

    public byte[] getContent() {
        return content;
    }

    public void setContent(byte[] content) {
        this.content = content;
    }
}