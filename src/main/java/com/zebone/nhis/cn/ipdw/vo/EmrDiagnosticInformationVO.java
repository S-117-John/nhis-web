package com.zebone.nhis.cn.ipdw.vo;

/**
 * 诊断信息
 */
public class EmrDiagnosticInformationVO {

    private int seqNo;

    private String dtDiagType;

    private String diagCode;

    private String diagName;

    private String diagDesc;

    private String admitCondCode;

    private String flagPrimary;

    private String codeDcdt;
    
    private String nameDcdt;//地区诊断名称

    private String descBodypart;

    private String descDrgprop;

    private String flagCure;

    public int getSeqNo() {
        return seqNo;
    }

    public void setSeqNo(int seqNo) {
        this.seqNo = seqNo;
    }

    public String getDtDiagType() {
        return dtDiagType;
    }

    public void setDtDiagType(String dtDiagType) {
        this.dtDiagType = dtDiagType;
    }

    public String getDiagCode() {
        return diagCode;
    }

    public void setDiagCode(String diagCode) {
        this.diagCode = diagCode;
    }

    public String getDiagName() {
        return diagName;
    }

    public void setDiagName(String diagName) {
        this.diagName = diagName;
    }

    public String getDiagDesc() {
        return diagDesc;
    }

    public void setDiagDesc(String diagDesc) {
        this.diagDesc = diagDesc;
    }

    public String getAdmitCondCode() {
        return admitCondCode;
    }

    public void setAdmitCondCode(String admitCondCode) {
        this.admitCondCode = admitCondCode;
    }

    public String getFlagPrimary() {
        return flagPrimary;
    }

    public void setFlagPrimary(String flagPrimary) {
        this.flagPrimary = flagPrimary;
    }

    public String getCodeDcdt() {
        return codeDcdt;
    }

    public void setCodeDcdt(String codeDcdt) {
        this.codeDcdt = codeDcdt;
    }

    public String getDescBodypart() {
        return descBodypart;
    }

    public void setDescBodypart(String descBodypart) {
        this.descBodypart = descBodypart;
    }

    public String getDescDrgprop() {
        return descDrgprop;
    }

    public void setDescDrgprop(String descDrgprop) {
        this.descDrgprop = descDrgprop;
    }

    public String getFlagCure() {
        return flagCure;
    }

    public void setFlagCure(String flagCure) {
        this.flagCure = flagCure;
    }

	public String getNameDcdt() {
		return nameDcdt;
	}

	public void setNameDcdt(String nameDcdt) {
		this.nameDcdt = nameDcdt;
	}
}
