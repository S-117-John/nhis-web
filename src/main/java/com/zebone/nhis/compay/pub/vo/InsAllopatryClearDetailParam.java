package com.zebone.nhis.compay.pub.vo;

import java.util.Date;

/**
 *异地清分结果确认明细
 */
public class InsAllopatryClearDetailParam {

    /**
     * 证件号码
     */
    public String certno;

    /**
     * 就诊登记号
     */
    public String mdtrtId;

    /**
     * 就诊结算时间
     */
    public Date mdtrt_setl_time;


    /**
     * 结算流水号
     */
    public String setlSn;

    /**
     * 总费用
     */
    public double medfeeSumamt;

    /**
     * 经办机构支付总额
     */
    public double optinsPaySumamt;

    /**
     * 确认标志
     */
    public String cnfmFlag;

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
    public Date getMdtrt_setl_time() {
        return mdtrt_setl_time;
    }

    public void setMdtrt_setl_time(Date mdtrt_setl_time) {
        this.mdtrt_setl_time = mdtrt_setl_time;
    }

    public String getSetlSn() {
        return setlSn;
    }

    public void setSetlSn(String setlSn) {
        this.setlSn = setlSn;
    }

    public double getOptinsPaySumamt() {
        return optinsPaySumamt;
    }

    public void setOptinsPaySumamt(double optinsPaySumamt) {
        this.optinsPaySumamt = optinsPaySumamt;
    }

    public double getMedfeeSumamt() {
        return medfeeSumamt;
    }

    public void setMedfeeSumamt(double medfeeSumamt) {
        this.medfeeSumamt = medfeeSumamt;
    }

    public String getCnfmFlag() {
        return cnfmFlag;
    }

    public void setCnfmFlag(String cnfmFlag) {
        this.cnfmFlag = cnfmFlag;
    }


}
