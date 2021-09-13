package com.zebone.nhis.webservice.vo.bapivas;

import org.codehaus.jackson.annotate.JsonProperty;

public class BaPivasSendDrugReq {
    @JsonProperty("as_operator")
    private String asOpertor;

    @JsonProperty("as_patient_id")
    private String asPatientId;

    @JsonProperty("as_visit_id")
    private String asVisitId;

    @JsonProperty("as_order_no")
    private String asOrderNo;

    @JsonProperty( "as_use_date")
    private String asUseDate;

    @JsonProperty( "as_freq_counter_sub")
    private String asFreqCounterSub;

    @JsonProperty( "as_drug_pzfs")
    private String asDrugPzfs;

    @JsonProperty( "as_lxh")
    private String asLxh;

    @JsonProperty("rs_flag")
    private String rsFlag;

    @JsonProperty( "rs_msg")
    private String rsMsg;

    @JsonProperty("apply_no")
    private String applyNo;

    @JsonProperty("code_dept_ex")
    private String codeDept;


    public String getAsOpertor() {
        return asOpertor;
    }

    public void setAsOpertor(String asOpertor) {
        this.asOpertor = asOpertor;
    }

    public String getAsPatientId() {
        return asPatientId;
    }

    public void setAsPatientId(String asPatientId) {
        this.asPatientId = asPatientId;
    }

    public String getAsVisitId() {
        return asVisitId;
    }

    public void setAsVisitId(String asVisitId) {
        this.asVisitId = asVisitId;
    }

    public String getAsOrderNo() {
        return asOrderNo;
    }

    public void setAsOrderNo(String asOrderNo) {
        this.asOrderNo = asOrderNo;
    }

    public String getAsUseDate() {
        return asUseDate;
    }

    public void setAsUseDate(String asUseDate) {
        this.asUseDate = asUseDate;
    }

    public String getAsFreqCounterSub() {
        return asFreqCounterSub;
    }

    public void setAsFreqCounterSub(String asFreqCounterSub) {
        this.asFreqCounterSub = asFreqCounterSub;
    }

    public String getAsDrugPzfs() {
        return asDrugPzfs;
    }

    public void setAsDrugPzfs(String asDrugPzfs) {
        this.asDrugPzfs = asDrugPzfs;
    }

    public String getAsLxh() {
        return asLxh;
    }

    public void setAsLxh(String asLxh) {
        this.asLxh = asLxh;
    }

    public String getRsFlag() {
        return rsFlag;
    }

    public void setRsFlag(String rsFlag) {
        this.rsFlag = rsFlag;
    }

    public String getRsMsg() {
        return rsMsg;
    }

    public void setRsMsg(String rsMsg) {
        this.rsMsg = rsMsg;
    }

    public String getApplyNo() {
        return applyNo;
    }

    public void setApplyNo(String applyNo) {
        this.applyNo = applyNo;
    }

    public String getCodeDept() {
        return codeDept;
    }

    public void setCodeDept(String codeDept) {
        this.codeDept = codeDept;
    }
}
