package com.zebone.nhis.compay.pub.vo;

import java.util.Date;

/**
 * 异地清分明细
 */
public class InsAllopatryClearDetailVo {



    private int seqno;//序号

    private String mdtrtarea;//就医地医保区划

    private String medinsNo;//医疗机构编号

    private String certno;//证件号码

    private String mdtrtId;//就诊登记号

    private Date mdtrtSetlTime;//就诊结算时间

    private String setlSn;//结算流水号

    private String fulamtAdvpayFlag;//全额垫付标志

    private double medfeeSumamt;//总费用

    private double optinsPaySumamt;//经办机构支付总额


    private String codeIp;//住院号

    private String namePi;//患者姓名

    private String insurNo;//医保卡号



    public String getCodeIp() {
        return codeIp;
    }

    public void setCodeIp(String codeIp) {
        this.codeIp = codeIp;
    }


    public String getNamePi() {
        return namePi;
    }

    public void setNamePi(String namePi) {
        this.namePi = namePi;
    }


    public String getInsurNo() {
        return insurNo;
    }

    public void setInsurNo(String insurNo) {
        this.insurNo = insurNo;
    }

    public int getSeqno() {
        return seqno;
    }

    public void setSeqno(int seqno) {
        this.seqno = seqno;
    }



    public String getMdtrtarea() {
        return mdtrtarea;
    }

    public void setMdtrtarea(String mdtrtarea) {
        this.mdtrtarea = mdtrtarea;
    }



    public String getMedinsNo() {
        return medinsNo;
    }

    public void setMedinsNo(String medinsNo) {
        this.medinsNo = medinsNo;
    }



    public String getCertno() {
        return certno;
    }

    public void setCertno(String certno) {
        this.certno = certno;
    }



    public String getMdtrtId() {
        return mdtrtId;
    }

    public void setMdtrtId(String mdtrtId) {
        this.mdtrtId = mdtrtId;
    }



    public Date getMdtrtSetlTime() {
        return mdtrtSetlTime;
    }

    public void setMdtrtSetlTime(Date mdtrtSetlTime) {
        this.mdtrtSetlTime = mdtrtSetlTime;
    }



    public String getSetlSn() {
        return setlSn;
    }

    public void setSetlSn(String setlSn) {
        this.setlSn = setlSn;
    }



    public String getFulamtAdvpayFlag() {
        return fulamtAdvpayFlag;
    }

    public void setFulamtAdvpayFlag(String fulamtAdvpayFlag) {
        this.fulamtAdvpayFlag = fulamtAdvpayFlag;
    }



    public double getMedfeeSumamt() {
        return medfeeSumamt;
    }

    public void setMedfeeSumamt(double medfeeSumamt) {
        this.medfeeSumamt = medfeeSumamt;
    }



    public double getOptinsPaySumamt() {
        return optinsPaySumamt;
    }

    public void setOptinsPaySumamt(double optinsPaySumamt) {
        this.optinsPaySumamt = optinsPaySumamt;
    }

}
