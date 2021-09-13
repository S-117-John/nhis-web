package com.zebone.nhis.compay.pub.vo;

import com.alibaba.fastjson.annotation.JSONField;

import java.math.BigDecimal;
public class InsQgybSetlinfo {
    @JSONField(name ="mdtrt_Id",ordinal = 0)
    private String mdtrtId;//就诊ID


    @JSONField(name ="setl_id",ordinal = 1)
    private String setlId;//结算ID

    @JSONField(name ="fixmedins_name",ordinal = 2)
    private String fixmedinsName;//定点医药机构名称

    @JSONField(name ="fixmedins_code",ordinal = 3)
    private String fixmedinsCode;//定点医药机构编号

    @JSONField(name ="medcas_no",ordinal = 4)
    private String medcasno;//病案号

    @JSONField(name ="psn_name",ordinal = 5)
    private String psnName;//人员姓名

    @JSONField(name ="gend",ordinal = 6)
    private String gend;//性别

    @JSONField(name ="brdy",ordinal = 7)
    private String brdy;//出生日期

    @JSONField(name ="ntly",ordinal = 8)
    private String ntly;//国籍

    @JSONField(name ="naty",ordinal = 9)
    private String naty;//民族

    @JSONField(name ="patn_cert_type",ordinal = 10)
    private String patnCertType ;//患者证件类别

    @JSONField(name ="certno",ordinal = 11)
    private String certno ;//证件号码

    @JSONField(name ="prfs",ordinal = 12)
    private String prfs ;//职业

    @JSONField(name ="coner_name",ordinal = 13)
    private String conerName;//联系人姓名

    @JSONField(name ="patn_rlts",ordinal = 14)
    private String patnRlts;//与患者关系

    @JSONField(name ="coner_addr",ordinal = 15)
    private String conerAddr;//联系人地址

    @JSONField(name ="coner_tel",ordinal = 16)
    private String conerTel;//联系人电话

    @JSONField(name ="hi_type",ordinal = 17)
    private String hiType;//医保类型

    @JSONField(name ="insuplc",ordinal = 18)
    private String  insuplc;//参保地

    @JSONField(name ="ipt_med_type",ordinal = 19)
    private String  iptMedType;//住院医疗类型

    @JSONField(name ="pk_admit_dept",ordinal = 20)
    private String  admCaty;//入院科别

    @JSONField(name ="pk_dept_dis",ordinal = 21)
    private String  dscgCaty;//出院科别

    @JSONField(name ="bill_code",ordinal = 22)
    private String  billCode;//票据代码

    @JSONField(name ="bill_no",ordinal = 23)
    private String  billNo;//票据号码

    @JSONField(name ="biz_sn",ordinal = 24)
    private String  bizSn;//业务流水号

    @JSONField(name ="setl_begn_date",ordinal = 25)
    private String  setlBegnDate;//结算开始日期

    @JSONField(name ="setl_end_date",ordinal = 26)
    private String  setlEndDate;//结算结束日期

    @JSONField(name ="psn_selfpay",ordinal = 27)
    private BigDecimal psnSelfpay;//个人自付

    @JSONField(name ="psn_ownpay",ordinal = 28)
    private BigDecimal  psnOwnpay;//个人自费

    @JSONField(name ="acct_pay",ordinal = 29)
    private BigDecimal  acctPay;//个人账户支出

    @JSONField(name ="psn_cashpay",ordinal = 30)
    private BigDecimal  psnCashpay;//个人现金支付

    @JSONField(name ="hi_paymtd",ordinal = 31)
    private String  hiPaymtd;//医保支付方式

    private String  hsorg;//医保机构

    private String  hsorgOpter;//医保机构经办人

    private String  medinsFillDept;//医疗机构填报部门

    private String  medinsFillPsn;//医疗机构填报人


    @JSONField(name ="hi_setl_lv")
    private String hiSetlLv;//医保结算等级

    @JSONField(name ="hi_no")
    private String hiNo;//医保编号



    @JSONField(name ="dcla_time")
    private String dclaTime;//申报时间



    @JSONField(name ="age")
    private int age;//年龄



    private int nwbAge;//（年龄不足1周岁）年龄





    @JSONField(name ="curr_addr")
    private String currAddr ;//现住址

    @JSONField(name ="emp_name")
    private String empName;//单位名称

    private String empAddr;//单位地址

    @JSONField(name ="emp_tel")
    private String empTel;//单位电话

    @JSONField(name ="poscode")
    private String poscode;//邮编



    @JSONField(name ="sp_psn_type")
    private String  spPsnType;//特殊人员类型

    private String  nwbAdmType;//新生儿入院类型

    @JSONField(name ="nwb_bir_wt")
    private String  nwbBirWt;//新生儿出生体重

    @JSONField(name ="nwb_adm_type")
    private int  nwbAdmWt;//新生儿入院体重

    private String  opspDiagCaty;//门诊慢特病诊断科别

    private String  opspMdtrtDate;//门诊慢特病就诊日期



    @JSONField(name ="adm_way")
    private String  admWay;//入院途径

    private String  trtType;//治疗类别

    @JSONField(name ="adm_time")
    private String  admTime;//入院时间



    @JSONField(name ="trans_dept_names")
    private String  refldeptDept;//转科科别

    @JSONField(name ="dis_time")
    private String  dscgTime;//出院时间



    @JSONField(name ="in_hos_days")
    private int  actIptDays;//实际住院天数

    @JSONField(name ="otp_wm_dise")
    private String  otpWmDise;//门（急）诊西医诊断

    @JSONField(name ="wm_dise_code")
    private String  wmDiseCode;//西医诊断疾病代码

    @JSONField(name ="otp_tcm_dise")
    private String  otpTcmDise;//门（急）诊中医诊断

    @JSONField(name ="tcm_dise_code")
    private String  tcmDiseCode;//中医诊断代码

    private int  diagCodeCnt;//诊断代码计数

    private int  oprnOprtCodeCnt;//手术操作代码计数

    @JSONField(name ="vent_used_dura")
    private String  ventUsedDura;//呼吸机使用时长

    @JSONField(name ="pwcry_bfadm_coma_dura")
    private String  pwcryBfadmComaDura;//颅脑损伤患者入院前昏迷时长

    @JSONField(name ="pwcry_afadm_coma_dura")
    private String  pwcryAfadmComaDura;//颅脑损伤患者入院后昏迷时长

    private String  bldCat;//输血品种

    private int  bldAmt;//输血量

    private String  bldUnt;//输血计量单位

    @JSONField(name ="spga_nurscare_days")
    private String  spgaNurscareDays;//特级护理天数

    @JSONField(name ="lv1_nurscare_days")
    private String  lv1NurscareDays;//一级护理天数

    @JSONField(name ="scd_nurscare_days")
    private String  scdNurscareDays;//二级护理天数

    @JSONField(name ="lv3_nurscare_days")
    private String  lv3NurscareDays;//三级护理天数

    @JSONField(name ="dscg_way")
    private String  dscgWay;//离院方式

    @JSONField(name ="acp_medins_name")
    private String  acpMedinsName;//拟接收机构名称

    @JSONField(name ="acp_optins_code")
    private String  acpOptinsCode;//拟接收机构代码



    @JSONField(name ="days_rinp_flag_31")
    private String  daysRinpFlag31;//出院31天内再住院计划标志

    @JSONField(name ="days_rinp_pup_31")
    private String  daysRinpPup31;//出院31天内再住院目的

    @JSONField(name ="chfpdr_name")
    private String  chfpdrName;//主诊医师姓名

    @JSONField(name ="chfpdr_code")
    private String  chfpdrCode;//主诊医师代码

    public String getMdtrtId() {
        return mdtrtId;
    }

    public void setMdtrtId(String mdtrtId) {
        this.mdtrtId = mdtrtId;
    }

    public String getSetlId() {
        return setlId;
    }

    public void setSetlId(String setlId) {
        this.setlId = setlId;
    }

    public String getFixmedinsName() {
        return fixmedinsName;
    }

    public void setFixmedinsName(String fixmedinsName) {
        this.fixmedinsName = fixmedinsName;
    }

    public String getFixmedinsCode() {
        return fixmedinsCode;
    }

    public void setFixmedinsCode(String fixmedinsCode) {
        this.fixmedinsCode = fixmedinsCode;
    }

    public String getMedcasno() {
        return medcasno;
    }

    public void setMedcasno(String medcasno) {
        this.medcasno = medcasno;
    }

    public String getPsnName() {
        return psnName;
    }

    public void setPsnName(String psnName) {
        this.psnName = psnName;
    }

    public String getGend() {
        return gend;
    }

    public void setGend(String gend) {
        this.gend = gend;
    }

    public String getBrdy() {
        return brdy;
    }

    public void setBrdy(String brdy) {
        this.brdy = brdy;
    }

    public String getNtly() {
        return ntly;
    }

    public void setNtly(String ntly) {
        this.ntly = ntly;
    }

    public String getNaty() {
        return naty;
    }

    public void setNaty(String naty) {
        this.naty = naty;
    }

    public String getPatnCertType() {
        return patnCertType;
    }

    public void setPatnCertType(String patnCertType) {
        this.patnCertType = patnCertType;
    }

    public String getCertno() {
        return certno;
    }

    public void setCertno(String certno) {
        this.certno = certno;
    }

    public String getPrfs() {
        return prfs;
    }

    public void setPrfs(String prfs) {
        this.prfs = prfs;
    }

    public String getConerName() {
        return conerName;
    }

    public void setConerName(String conerName) {
        this.conerName = conerName;
    }

    public String getPatnRlts() {
        return patnRlts;
    }

    public void setPatnRlts(String patnRlts) {
        this.patnRlts = patnRlts;
    }

    public String getConerAddr() {
        return conerAddr;
    }

    public void setConerAddr(String conerAddr) {
        this.conerAddr = conerAddr;
    }

    public String getConerTel() {
        return conerTel;
    }

    public void setConerTel(String conerTel) {
        this.conerTel = conerTel;
    }

    public String getHiType() {
        return hiType;
    }

    public void setHiType(String hiType) {
        this.hiType = hiType;
    }

    public String getInsuplc() {
        return insuplc;
    }

    public void setInsuplc(String insuplc) {
        this.insuplc = insuplc;
    }

    public String getIptMedType() {
        return iptMedType;
    }

    public void setIptMedType(String iptMedType) {
        this.iptMedType = iptMedType;
    }

    public String getAdmCaty() {
        return admCaty;
    }

    public void setAdmCaty(String admCaty) {
        this.admCaty = admCaty;
    }

    public String getDscgCaty() {
        return dscgCaty;
    }

    public void setDscgCaty(String dscgCaty) {
        this.dscgCaty = dscgCaty;
    }

    public String getBillCode() {
        return billCode;
    }

    public void setBillCode(String billCode) {
        this.billCode = billCode;
    }

    public String getBillNo() {
        return billNo;
    }

    public void setBillNo(String billNo) {
        this.billNo = billNo;
    }

    public String getBizSn() {
        return bizSn;
    }

    public void setBizSn(String bizSn) {
        this.bizSn = bizSn;
    }

    public String getSetlBegnDate() {
        return setlBegnDate;
    }

    public void setSetlBegnDate(String setlBegnDate) {
        this.setlBegnDate = setlBegnDate;
    }

    public String getSetlEndDate() {
        return setlEndDate;
    }

    public void setSetlEndDate(String setlEndDate) {
        this.setlEndDate = setlEndDate;
    }

    public BigDecimal getPsnSelfpay() {
        return psnSelfpay;
    }

    public void setPsnSelfpay(BigDecimal psnSelfpay) {
        this.psnSelfpay = psnSelfpay;
    }

    public BigDecimal getPsnOwnpay() {
        return psnOwnpay;
    }

    public void setPsnOwnpay(BigDecimal psnOwnpay) {
        this.psnOwnpay = psnOwnpay;
    }

    public BigDecimal getAcctPay() {
        return acctPay;
    }

    public void setAcctPay(BigDecimal acctPay) {
        this.acctPay = acctPay;
    }

    public BigDecimal getPsnCashpay() {
        return psnCashpay;
    }

    public void setPsnCashpay(BigDecimal psnCashpay) {
        this.psnCashpay = psnCashpay;
    }

    public String getHiPaymtd() {
        return hiPaymtd;
    }

    public void setHiPaymtd(String hiPaymtd) {
        this.hiPaymtd = hiPaymtd;
    }

    public String getHsorg() {
        return hsorg;
    }

    public void setHsorg(String hsorg) {
        this.hsorg = hsorg;
    }

    public String getHsorgOpter() {
        return hsorgOpter;
    }

    public void setHsorgOpter(String hsorgOpter) {
        this.hsorgOpter = hsorgOpter;
    }

    public String getMedinsFillDept() {
        return medinsFillDept;
    }

    public void setMedinsFillDept(String medinsFillDept) {
        this.medinsFillDept = medinsFillDept;
    }

    public String getMedinsFillPsn() {
        return medinsFillPsn;
    }

    public void setMedinsFillPsn(String medinsFillPsn) {
        this.medinsFillPsn = medinsFillPsn;
    }

    public String getHiSetlLv() {
        return hiSetlLv;
    }

    public void setHiSetlLv(String hiSetlLv) {
        this.hiSetlLv = hiSetlLv;
    }

    public String getHiNo() {
        return hiNo;
    }

    public void setHiNo(String hiNo) {
        this.hiNo = hiNo;
    }

    public String getDclaTime() {
        return dclaTime;
    }

    public void setDclaTime(String dclaTime) {
        this.dclaTime = dclaTime;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public int getNwbAge() {
        return nwbAge;
    }

    public void setNwbAge(int nwbAge) {
        this.nwbAge = nwbAge;
    }

    public String getCurrAddr() {
        return currAddr;
    }

    public void setCurrAddr(String currAddr) {
        this.currAddr = currAddr;
    }

    public String getEmpName() {
        return empName;
    }

    public void setEmpName(String empName) {
        this.empName = empName;
    }

    public String getEmpAddr() {
        return empAddr;
    }

    public void setEmpAddr(String empAddr) {
        this.empAddr = empAddr;
    }

    public String getEmpTel() {
        return empTel;
    }

    public void setEmpTel(String empTel) {
        this.empTel = empTel;
    }

    public String getPoscode() {
        return poscode;
    }

    public void setPoscode(String poscode) {
        this.poscode = poscode;
    }

    public String getSpPsnType() {
        return spPsnType;
    }

    public void setSpPsnType(String spPsnType) {
        this.spPsnType = spPsnType;
    }

    public String getNwbAdmType() {
        return nwbAdmType;
    }

    public void setNwbAdmType(String nwbAdmType) {
        this.nwbAdmType = nwbAdmType;
    }

    public String getNwbBirWt() {
        return nwbBirWt;
    }

    public void setNwbBirWt(String nwbBirWt) {
        this.nwbBirWt = nwbBirWt;
    }

    public int getNwbAdmWt() {
        return nwbAdmWt;
    }

    public void setNwbAdmWt(int nwbAdmWt) {
        this.nwbAdmWt = nwbAdmWt;
    }

    public String getOpspDiagCaty() {
        return opspDiagCaty;
    }

    public void setOpspDiagCaty(String opspDiagCaty) {
        this.opspDiagCaty = opspDiagCaty;
    }

    public String getOpspMdtrtDate() {
        return opspMdtrtDate;
    }

    public void setOpspMdtrtDate(String opspMdtrtDate) {
        this.opspMdtrtDate = opspMdtrtDate;
    }

    public String getAdmWay() {
        return admWay;
    }

    public void setAdmWay(String admWay) {
        this.admWay = admWay;
    }

    public String getTrtType() {
        return trtType;
    }

    public void setTrtType(String trtType) {
        this.trtType = trtType;
    }

    public String getAdmTime() {
        return admTime;
    }

    public void setAdmTime(String admTime) {
        this.admTime = admTime;
    }

    public String getRefldeptDept() {
        return refldeptDept;
    }

    public void setRefldeptDept(String refldeptDept) {
        this.refldeptDept = refldeptDept;
    }

    public String getDscgTime() {
        return dscgTime;
    }

    public void setDscgTime(String dscgTime) {
        this.dscgTime = dscgTime;
    }

    public int getActIptDays() {
        return actIptDays;
    }

    public void setActIptDays(int actIptDays) {
        this.actIptDays = actIptDays;
    }

    public String getOtpWmDise() {
        return otpWmDise;
    }

    public void setOtpWmDise(String otpWmDise) {
        this.otpWmDise = otpWmDise;
    }

    public String getWmDiseCode() {
        return wmDiseCode;
    }

    public void setWmDiseCode(String wmDiseCode) {
        this.wmDiseCode = wmDiseCode;
    }

    public String getOtpTcmDise() {
        return otpTcmDise;
    }

    public void setOtpTcmDise(String otpTcmDise) {
        this.otpTcmDise = otpTcmDise;
    }

    public String getTcmDiseCode() {
        return tcmDiseCode;
    }

    public void setTcmDiseCode(String tcmDiseCode) {
        this.tcmDiseCode = tcmDiseCode;
    }

    public int getDiagCodeCnt() {
        return diagCodeCnt;
    }

    public void setDiagCodeCnt(int diagCodeCnt) {
        this.diagCodeCnt = diagCodeCnt;
    }

    public int getOprnOprtCodeCnt() {
        return oprnOprtCodeCnt;
    }

    public void setOprnOprtCodeCnt(int oprnOprtCodeCnt) {
        this.oprnOprtCodeCnt = oprnOprtCodeCnt;
    }

    public String getVentUsedDura() {
        return ventUsedDura;
    }

    public void setVentUsedDura(String ventUsedDura) {
        this.ventUsedDura = ventUsedDura;
    }

    public String getPwcryBfadmComaDura() {
        return pwcryBfadmComaDura;
    }

    public void setPwcryBfadmComaDura(String pwcryBfadmComaDura) {
        this.pwcryBfadmComaDura = pwcryBfadmComaDura;
    }

    public String getPwcryAfadmComaDura() {
        return pwcryAfadmComaDura;
    }

    public void setPwcryAfadmComaDura(String pwcryAfadmComaDura) {
        this.pwcryAfadmComaDura = pwcryAfadmComaDura;
    }

    public String getBldCat() {
        return bldCat;
    }

    public void setBldCat(String bldCat) {
        this.bldCat = bldCat;
    }

    public int getBldAmt() {
        return bldAmt;
    }

    public void setBldAmt(int bldAmt) {
        this.bldAmt = bldAmt;
    }

    public String getBldUnt() {
        return bldUnt;
    }

    public void setBldUnt(String bldUnt) {
        this.bldUnt = bldUnt;
    }

    public String getSpgaNurscareDays() {
        return spgaNurscareDays;
    }

    public void setSpgaNurscareDays(String spgaNurscareDays) {
        this.spgaNurscareDays = spgaNurscareDays;
    }

    public String getLv1NurscareDays() {
        return lv1NurscareDays;
    }

    public void setLv1NurscareDays(String lv1NurscareDays) {
        this.lv1NurscareDays = lv1NurscareDays;
    }

    public String getScdNurscareDays() {
        return scdNurscareDays;
    }

    public void setScdNurscareDays(String scdNurscareDays) {
        this.scdNurscareDays = scdNurscareDays;
    }

    public String getLv3NurscareDays() {
        return lv3NurscareDays;
    }

    public void setLv3NurscareDays(String lv3NurscareDays) {
        this.lv3NurscareDays = lv3NurscareDays;
    }

    public String getDscgWay() {
        return dscgWay;
    }

    public void setDscgWay(String dscgWay) {
        this.dscgWay = dscgWay;
    }

    public String getAcpMedinsName() {
        return acpMedinsName;
    }

    public void setAcpMedinsName(String acpMedinsName) {
        this.acpMedinsName = acpMedinsName;
    }

    public String getAcpOptinsCode() {
        return acpOptinsCode;
    }

    public void setAcpOptinsCode(String acpOptinsCode) {
        this.acpOptinsCode = acpOptinsCode;
    }

    public String getDaysRinpFlag31() {
        return daysRinpFlag31;
    }

    public void setDaysRinpFlag31(String daysRinpFlag31) {
        this.daysRinpFlag31 = daysRinpFlag31;
    }

    public String getDaysRinpPup31() {
        return daysRinpPup31;
    }

    public void setDaysRinpPup31(String daysRinpPup31) {
        this.daysRinpPup31 = daysRinpPup31;
    }

    public String getChfpdrName() {
        return chfpdrName;
    }

    public void setChfpdrName(String chfpdrName) {
        this.chfpdrName = chfpdrName;
    }

    public String getChfpdrCode() {
        return chfpdrCode;
    }

    public void setChfpdrCode(String chfpdrCode) {
        this.chfpdrCode = chfpdrCode;
    }
}
