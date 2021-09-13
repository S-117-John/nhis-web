package com.zebone.nhis.webservice.zsrm.vo.self;

import java.math.BigDecimal;

public class ReqYbPreSettleParamVo {
    private BigDecimal medfeeSumamt;

    private String psnNo;

    private String port;

    private String mdtrtId;

    private String mdtrtCertType;

    private String medType;

    private String mdtrtCertNo;

    private String chrgBchno;

    private String insutype;

    private String acctUsedFlag;

    private String psnSetlway;

    public BigDecimal getMedfeeSumamt() {
        return medfeeSumamt;
    }

    public void setMedfeeSumamt(BigDecimal medfeeSumamt) {
        this.medfeeSumamt = medfeeSumamt;
    }

    public String getPsnNo() {
        return psnNo;
    }

    public void setPsnNo(String psnNo) {
        this.psnNo = psnNo;
    }

    public String getPort() {
        return port;
    }

    public void setPort(String port) {
        this.port = port;
    }

    public String getMdtrtId() {
        return mdtrtId;
    }

    public void setMdtrtId(String mdtrtId) {
        this.mdtrtId = mdtrtId;
    }

    public String getMdtrtCertType() {
        return mdtrtCertType;
    }

    public void setMdtrtCertType(String mdtrtCertType) {
        this.mdtrtCertType = mdtrtCertType;
    }

    public String getMedType() {
        return medType;
    }

    public void setMedType(String medType) {
        this.medType = medType;
    }

    public String getMdtrtCertNo() {
        return mdtrtCertNo;
    }

    public void setMdtrtCertNo(String mdtrtCertNo) {
        this.mdtrtCertNo = mdtrtCertNo;
    }

    public String getChrgBchno() {
        return chrgBchno;
    }

    public void setChrgBchno(String chrgBchno) {
        this.chrgBchno = chrgBchno;
    }

    public String getInsutype() {
        return insutype;
    }

    public void setInsutype(String insutype) {
        this.insutype = insutype;
    }

    public String getAcctUsedFlag() {
        return acctUsedFlag;
    }

    public void setAcctUsedFlag(String acctUsedFlag) {
        this.acctUsedFlag = acctUsedFlag;
    }

    public String getPsnSetlway() {
        return psnSetlway;
    }

    public void setPsnSetlway(String psnSetlway) {
        this.psnSetlway = psnSetlway;
    }
}
