package com.zebone.nhis.common.module.compay.ins.qgyb;

import com.zebone.nhis.common.module.BaseModule;
import com.zebone.platform.modules.dao.build.au.Field;
import com.zebone.platform.modules.dao.build.au.PK;
import com.zebone.platform.modules.dao.build.au.Table;

import java.util.Date;

@Table(value = "INS_QGYB_ST_CG")
public class InsQgybStCg extends BaseModule {

    @PK
    @Field(value="PK_INSST_CG" , id= Field.KeyId.UUID)
    private String pkInsstCg;

    @Field("pk_pv")
    private String pkPv;

    @Field("pk_pi")
    private String pkPi;

    @Field("pk_insst")
    private String pkInsst;

    /**
     *就诊ID
     */
    @Field("mdtrt_id")
    private String mdtrtId;

    /**
     *结算ID
     */
    @Field("setl_id")
    private String setlId;
    /**
     *费用明细流水号
     */
    @Field("feedetl_sn")
    private String feedetlSn;

    /**
     *处方/医嘱号
     */
    @Field("rx_drord_no")
    private String rxDrordNo;

    /**
     *医疗类别
     */
    @Field("med_type")
    private String medType;
    /**
     *费用发生时间
     */
    @Field("fee_ocur_time")
    private Date feeOcurTime;

    /**
     *数量
     */
    @Field("cnt")
    private Double cnt;

    /**
     *单价
     */
    @Field("pric")
    private String pric;
    /**
     *单次剂量描述
     */
    @Field("sin_dos_dscr")
    private String sinDosDscr;

    /**
     *使用频次描述
     */
    @Field("used_frqu_dscr")
    private String usedFrquDscr;

    /**
     *周期天数
     */
    @Field("prd_days")
    private String prdDays;

    /**
     *用药途径描述
     */
    @Field("medc_way_dscr")
    private String medcWayDscr;

    /**
     *明细项目费用总额
     */
    @Field("det_item_fee_sumamt")
    private Double detItemFeeSumamt;

    /**
     *定价上限金额
     */
    @Field("pric_uplmt_amt")
    private Double pricUplmtAmt;
    /**
     *自付比例
     */
    @Field("selfpay_prop")
    private Double selfpayProp;

    /**
     *全自费金额
     */
    @Field("fulamt_ownpay_amt")
    private Double fulamtOwnpayAmt;

    /**
     *超限价金额
     */
    @Field("overlmt_amt")
    private Double overlmtAmt;
    /**
     *先行自付金额
     */
    @Field("preselfpay_amt")
    private Double preselfpayAmt;

    /**
     *符合政策范围金额
     */
    @Field("inscp_scp_amt")
    private Double inscpScpAmt;

    /**
     *收费项目等级
     */
    @Field("chrgitm_lv")
    private String chrgitmLv;
    /**
     *医保目录编码
     */
    @Field("hilist_code")
    private String hilistCode;

    /**
     *医保目录名称
     */
    @Field("hilist_name")
    private String hilistName;

    /**
     *目录类别
     */
    @Field("list_type")
    private String listType;
    /**
     *医疗目录编码
     */
    @Field("med_list_codg")
    private String medListCodg;

    /**
     *医药机构目录编码
     */
    @Field("medins_list_codg")
    private String medinsListCodg;

    /**
     *医药机构目录名称
     */
    @Field("medins_list_name")
    private String medinsListName;
    /**
     *医疗收费项目类别
     */
    @Field("med_chrgitm_type")
    private String medChrgitmType;

    /**
     *商品名
     */
    @Field("prodname")
    private String prodname;

    /**
     *规格
     */
    @Field("spec")
    private String spec;
    /**
     *剂型名称
     */
    @Field("dosform_name")
    private String dosformName;

    /**
     *开单科室编码
     */
    @Field("bilg_dept_codg")
    private String bilgDeptCodg;

    /**
     *开单科室名称
     */
    @Field("bilg_dept_name")
    private String bilgDeptName;

    /**
     *开单医生编码
     */
    @Field("bilg_dr_codg")
    private String bilgDrCodg;

    /**
     *开单医师姓名
     */
    @Field("bilg_dr_name")
    private String bilgDrName;

    /**
     *受单科室编码
     */
    @Field("acord_dept_codg")
    private String acordDeptCodg;

    /**
     *受单科室名称
     */
    @Field("acord_dept_name")
    private String acordDeptName;
    /**
     *受单医生编码
     */
    @Field("orders_dr_code")
    private String ordersDrCode;

    /**
     *受单医生姓名
     */
    @Field("orders_dr_name")
    private String ordersDrName;

    /**
     *限制使用标志
     */
    @Field("lmt_used_flag")
    private String lmtUsedFlag;
    /**
     *医院制剂标志
     */
    @Field("hosp_prep_flag")
    private String hospPrepFlag;

    /**
     *医院审批标志
     */
    @Field("hosp_appr_flag")
    private String hospApprFlag;

    /**
     *中药使用方式
     */
    @Field("tcmdrug_used_way")
    private String tcmdrugUsedWay;
    /**
     *生产地类别
     */
    @Field("prodplac_type")
    private String prodplacType;

    /**
     *基本药物标志
     */
    @Field("bas_medn_flag")
    private String basMednFlag;
    /**
     *医保谈判药品标志
     */
    @Field("hi_nego_drug_flag")
    private String hiNegoDrugFlag;

    /**
     *儿童用药标志
     */
    @Field("chld_medc_flag")
    private String chldMedcFlag;
    /**
     *外检标志
     */
    @Field("etip_flag")
    private String etipFlag;

    /**
     *外检医院编码
     */
    @Field("etip_hosp_code")
    private String etipHospCode;
    /**
     *出院带药标志
     */
    @Field("dscg_tkdrug_flag")
    private String dscgTkdrugFlag;

    /**
     *目录特项标志
     */
    @Field("list_sp_item_flag")
    private String listSpItemFlag;
    /**
     *生育费用标志
     */
    @Field("matn_fee_flag")
    private String matnFeeFlag;

    /**
     *直报标志
     */
    @Field("drt_reim_flag")
    private String drtReimFlag;
    /**
     *备注
     */
    @Field("memo")
    private String memo;

    /**
     *经办人ID
     */
    @Field("opter_id")
    private String opterId;
    /**
     *经办人姓名
     */
    @Field("opter_name")
    private String opterName;

    /**
     *经办时间
     */
    @Field("opt_time")
    private Date optTime;

    @Field("chrg_bchno")
    private String chrgBchno;

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

    public String getFeedetlSn() {
        return feedetlSn;
    }

    public void setFeedetlSn(String feedetlSn) {
        this.feedetlSn = feedetlSn;
    }

    public String getRxDrordNo() {
        return rxDrordNo;
    }

    public void setRxDrordNo(String rxDrordNo) {
        this.rxDrordNo = rxDrordNo;
    }

    public String getMedType() {
        return medType;
    }

    public void setMedType(String medType) {
        this.medType = medType;
    }

    public Date getFeeOcurTime() {
        return feeOcurTime;
    }

    public void setFeeOcurTime(Date feeOcurTime) {
        this.feeOcurTime = feeOcurTime;
    }

    public Double getCnt() {
        return cnt;
    }

    public void setCnt(Double cnt) {
        this.cnt = cnt;
    }

    public String getPric() {
        return pric;
    }

    public void setPric(String pric) {
        this.pric = pric;
    }

    public String getSinDosDscr() {
        return sinDosDscr;
    }

    public void setSinDosDscr(String sinDosDscr) {
        this.sinDosDscr = sinDosDscr;
    }

    public String getUsedFrquDscr() {
        return usedFrquDscr;
    }

    public void setUsedFrquDscr(String usedFrquDscr) {
        this.usedFrquDscr = usedFrquDscr;
    }

    public String getPrdDays() {
        return prdDays;
    }

    public void setPrdDays(String prdDays) {
        this.prdDays = prdDays;
    }

    public String getMedcWayDscr() {
        return medcWayDscr;
    }

    public void setMedcWayDscr(String medcWayDscr) {
        this.medcWayDscr = medcWayDscr;
    }

    public Double getDetItemFeeSumamt() {
        return detItemFeeSumamt;
    }

    public void setDetItemFeeSumamt(Double detItemFeeSumamt) {
        this.detItemFeeSumamt = detItemFeeSumamt;
    }

    public Double getPricUplmtAmt() {
        return pricUplmtAmt;
    }

    public void setPricUplmtAmt(Double pricUplmtAmt) {
        this.pricUplmtAmt = pricUplmtAmt;
    }

    public Double getSelfpayProp() {
        return selfpayProp;
    }

    public void setSelfpayProp(Double selfpayProp) {
        this.selfpayProp = selfpayProp;
    }

    public Double getFulamtOwnpayAmt() {
        return fulamtOwnpayAmt;
    }

    public void setFulamtOwnpayAmt(Double fulamtOwnpayAmt) {
        this.fulamtOwnpayAmt = fulamtOwnpayAmt;
    }

    public Double getOverlmtAmt() {
        return overlmtAmt;
    }

    public void setOverlmtAmt(Double overlmtAmt) {
        this.overlmtAmt = overlmtAmt;
    }

    public Double getPreselfpayAmt() {
        return preselfpayAmt;
    }

    public void setPreselfpayAmt(Double preselfpayAmt) {
        this.preselfpayAmt = preselfpayAmt;
    }

    public Double getInscpScpAmt() {
        return inscpScpAmt;
    }

    public void setInscpScpAmt(Double inscpScpAmt) {
        this.inscpScpAmt = inscpScpAmt;
    }

    public String getChrgitmLv() {
        return chrgitmLv;
    }

    public void setChrgitmLv(String chrgitmLv) {
        this.chrgitmLv = chrgitmLv;
    }

    public String getHilistCode() {
        return hilistCode;
    }

    public void setHilistCode(String hilistCode) {
        this.hilistCode = hilistCode;
    }

    public String getHilistName() {
        return hilistName;
    }

    public void setHilistName(String hilistName) {
        this.hilistName = hilistName;
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

    public String getMedinsListCodg() {
        return medinsListCodg;
    }

    public void setMedinsListCodg(String medinsListCodg) {
        this.medinsListCodg = medinsListCodg;
    }

    public String getMedinsListName() {
        return medinsListName;
    }

    public void setMedinsListName(String medinsListName) {
        this.medinsListName = medinsListName;
    }

    public String getMedChrgitmType() {
        return medChrgitmType;
    }

    public void setMedChrgitmType(String medChrgitmType) {
        this.medChrgitmType = medChrgitmType;
    }

    public String getProdname() {
        return prodname;
    }

    public void setProdname(String prodname) {
        this.prodname = prodname;
    }

    public String getSpec() {
        return spec;
    }

    public void setSpec(String spec) {
        this.spec = spec;
    }

    public String getDosformName() {
        return dosformName;
    }

    public void setDosformName(String dosformName) {
        this.dosformName = dosformName;
    }

    public String getBilgDeptCodg() {
        return bilgDeptCodg;
    }

    public void setBilgDeptCodg(String bilgDeptCodg) {
        this.bilgDeptCodg = bilgDeptCodg;
    }

    public String getBilgDeptName() {
        return bilgDeptName;
    }

    public void setBilgDeptName(String bilgDeptName) {
        this.bilgDeptName = bilgDeptName;
    }

    public String getBilgDrCodg() {
        return bilgDrCodg;
    }

    public void setBilgDrCodg(String bilgDrCodg) {
        this.bilgDrCodg = bilgDrCodg;
    }

    public String getBilgDrName() {
        return bilgDrName;
    }

    public void setBilgDrName(String bilgDrName) {
        this.bilgDrName = bilgDrName;
    }

    public String getAcordDeptCodg() {
        return acordDeptCodg;
    }

    public void setAcordDeptCodg(String acordDeptCodg) {
        this.acordDeptCodg = acordDeptCodg;
    }

    public String getAcordDeptName() {
        return acordDeptName;
    }

    public void setAcordDeptName(String acordDeptName) {
        this.acordDeptName = acordDeptName;
    }

    public String getOrdersDrCode() {
        return ordersDrCode;
    }

    public void setOrdersDrCode(String ordersDrCode) {
        this.ordersDrCode = ordersDrCode;
    }

    public String getOrdersDrName() {
        return ordersDrName;
    }

    public void setOrdersDrName(String ordersDrName) {
        this.ordersDrName = ordersDrName;
    }

    public String getLmtUsedFlag() {
        return lmtUsedFlag;
    }

    public void setLmtUsedFlag(String lmtUsedFlag) {
        this.lmtUsedFlag = lmtUsedFlag;
    }

    public String getHospPrepFlag() {
        return hospPrepFlag;
    }

    public void setHospPrepFlag(String hospPrepFlag) {
        this.hospPrepFlag = hospPrepFlag;
    }

    public String getHospApprFlag() {
        return hospApprFlag;
    }

    public void setHospApprFlag(String hospApprFlag) {
        this.hospApprFlag = hospApprFlag;
    }

    public String getTcmdrugUsedWay() {
        return tcmdrugUsedWay;
    }

    public void setTcmdrugUsedWay(String tcmdrugUsedWay) {
        this.tcmdrugUsedWay = tcmdrugUsedWay;
    }

    public String getProdplacType() {
        return prodplacType;
    }

    public void setProdplacType(String prodplacType) {
        this.prodplacType = prodplacType;
    }

    public String getBasMednFlag() {
        return basMednFlag;
    }

    public void setBasMednFlag(String basMednFlag) {
        this.basMednFlag = basMednFlag;
    }

    public String getHiNegoDrugFlag() {
        return hiNegoDrugFlag;
    }

    public void setHiNegoDrugFlag(String hiNegoDrugFlag) {
        this.hiNegoDrugFlag = hiNegoDrugFlag;
    }

    public String getChldMedcFlag() {
        return chldMedcFlag;
    }

    public void setChldMedcFlag(String chldMedcFlag) {
        this.chldMedcFlag = chldMedcFlag;
    }

    public String getEtipFlag() {
        return etipFlag;
    }

    public void setEtipFlag(String etipFlag) {
        this.etipFlag = etipFlag;
    }

    public String getEtipHospCode() {
        return etipHospCode;
    }

    public void setEtipHospCode(String etipHospCode) {
        this.etipHospCode = etipHospCode;
    }

    public String getDscgTkdrugFlag() {
        return dscgTkdrugFlag;
    }

    public void setDscgTkdrugFlag(String dscgTkdrugFlag) {
        this.dscgTkdrugFlag = dscgTkdrugFlag;
    }

    public String getListSpItemFlag() {
        return listSpItemFlag;
    }

    public void setListSpItemFlag(String listSpItemFlag) {
        this.listSpItemFlag = listSpItemFlag;
    }

    public String getMatnFeeFlag() {
        return matnFeeFlag;
    }

    public void setMatnFeeFlag(String matnFeeFlag) {
        this.matnFeeFlag = matnFeeFlag;
    }

    public String getDrtReimFlag() {
        return drtReimFlag;
    }

    public void setDrtReimFlag(String drtReimFlag) {
        this.drtReimFlag = drtReimFlag;
    }

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }

    public String getOpterId() {
        return opterId;
    }

    public void setOpterId(String opterId) {
        this.opterId = opterId;
    }

    public String getOpterName() {
        return opterName;
    }

    public void setOpterName(String opterName) {
        this.opterName = opterName;
    }

    public Date getOptTime() {
        return optTime;
    }

    public void setOptTime(Date optTime) {
        this.optTime = optTime;
    }

    public String getPkPv() {
        return pkPv;
    }

    public void setPkPv(String pkPv) {
        this.pkPv = pkPv;
    }

    public String getPkPi() {
        return pkPi;
    }

    public void setPkPi(String pkPi) {
        this.pkPi = pkPi;
    }

    public String getPkInsst() {
        return pkInsst;
    }

    public void setPkInsst(String pkInsst) {
        this.pkInsst = pkInsst;
    }

    public String getPkInsstCg() {
        return pkInsstCg;
    }

    public void setPkInsstCg(String pkInsstCg) {
        this.pkInsstCg = pkInsstCg;
    }

    public String getChrgBchno() {
        return chrgBchno;
    }

    public void setChrgBchno(String chrgBchno) {
        this.chrgBchno = chrgBchno;
    }
}
