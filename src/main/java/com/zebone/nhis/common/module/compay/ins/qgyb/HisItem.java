package com.zebone.nhis.common.module.compay.ins.qgyb;

import org.codehaus.jackson.annotate.JsonProperty;
import org.springframework.beans.factory.annotation.Value;

public class HisItem {
    //定点医药机构目录编号
    @JsonProperty("fixmedins_hilist_id")
    private String fixmedinsHilistId;
    //定点医药机构目录名称
    @JsonProperty("fixmedins_hilist_name")
    private String fixmedinsHilistName;
    //开始日期
    private String begndate;
    //结束日期
    private String enddate;

    //目录类别
    @JsonProperty("list_type")
    private String listType;
    //医疗目录编码
    @JsonProperty("med_list_codg")
    private String medListCodg;
    //定点医药机构编号
    @JsonProperty("fixmedins_code")
    private String fixmedinsCode;

    public String getFixmedinsHilistId() {
        return fixmedinsHilistId;
    }

    public void setFixmedinsHilistId(String fixmedinsHilistId) {
        this.fixmedinsHilistId = fixmedinsHilistId;
    }

    public String getFixmedinsHilistName() {
        return fixmedinsHilistName;
    }

    public void setFixmedinsHilistName(String fixmedinsHilistName) {
        this.fixmedinsHilistName = fixmedinsHilistName;
    }

    public String getBegndate() {
        return begndate;
    }

    public void setBegndate(String begndate) {
        this.begndate = begndate;
    }

    public String getEnddate() {
        return enddate;
    }

    public void setEnddate(String enddate) {
        this.enddate = enddate;
    }

    public String getListType() {
        return listType;
    }

    public void setListType(String listType) {
        this.listType = listType;
    }

    public String getMedListCodg() {
        return medListCodg;
    }

    public void setMedListCodg(String medListCodg) {
        this.medListCodg = medListCodg;
    }

    public String getFixmedinsCode() {
        return fixmedinsCode;
    }

    public void setFixmedinsCode(String fixmedinsCode) {
        this.fixmedinsCode = fixmedinsCode;
    }
}
