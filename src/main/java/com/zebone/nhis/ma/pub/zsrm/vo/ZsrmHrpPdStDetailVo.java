package com.zebone.nhis.ma.pub.zsrm.vo;

import com.zebone.platform.modules.dao.build.au.Field;
import com.zebone.platform.modules.dao.build.au.Table;

import java.util.Date;

@Table("pd_st_detail")
public class ZsrmHrpPdStDetailVo {

    /**
     * 库单明细主键
     */
    @Field("pk_pdstdt")
    private String pkPdstdt;

    /**
     * 当前机构
     */
    @Field("pk_org")
    private String pkOrg;

    /**
     * 关联出库单主键
     */
    @Field("pk_pdst")
    private String pkPdst;

    /**
     * 显示的排列顺序
     */
    @Field("sort_no")
    private Integer sortNo;

    /**
     * 物品主键
     */
    @Field("pk_pd")
    private String pkPd;

    /**
     * 物品包装单位
     */
    @Field("pk_unit_pack")
    private String pkUnitPack;

    /**
     *  包装单位下的基本单位数
     */
    @Field("pack_size")
    private Integer packSize;

    /**
     * 包装单位下的数量
     */
    @Field("quan_pack")
    private Double quanPack;

    /**
     * 基本单位下的数量
     */
    @Field("quan_min")
    private Double quanMin;

    @Field("quan_outstore")
    private Double quanOutstore;

    /**
     * 成本单价
     */
    @Field("price_cost")
    private Double  priceCost;

    /**
     * 零售单价
     */
    @Field("price")
    private Double price;

    /**
     * 成本金额
     */
    @Field("amount_cost")
    private Double amountCost;

    /**
     * 零售金额
     */
    @Field("amount")
    private Double  amount;

    /**
     * 扣率
     */
    @Field("disc")
    private Double disc;

    /**
     * 批号
     */
    @Field("batch_no")
    private String batchNo;

    /**
     * 生产日期
     */
    @Field("date_fac")
    private Date DateFac;

    /**
     * 失效日期
     */
    @Field("date_expire")
    private Date dateExpire;

    /**
     * 发货单号
     */
    @Field("inv_no")
    private String invNo;

    /**
     * 发货号
     */
    @Field("receipt_no")
    private String receiptNo;

    @Field("flag_chk_rpt")
    private String flagChkRpt;

    @Field("pk_emp_chk_rpt")
    private String pkEmpChkRpt;

    @Field("name_emp_chk_rpt")
    private String nameEmpChkRpt;

    /**
     * 开票供应商
     */
    @Field("pk_supplyer")
    private String pkSupplyer;

    @Field("pk_pdpay")
    private String pkPdpay;

    /**
     * 已付金额
     */
    @Field("amount_pay")
    private Double amountPay;

    /**
     * 已付款标志
     */
    @Field("flag_pay")
    private String flagPay;

    /**
     * 采购订单明细
     */
    @Field("pk_pdpudt")
    private String pkPdpudt;

    /**
     * 已完成出库标志
     */
    @Field("flag_finish")
    private String flagFinish;

    /**
     * 出库关联的入库明细
     */
    @Field("pk_pdstdt_rl")
    private String pkPdstdtRl;


    @Field("date_chk_rpt")
    private Date dateChkRpt;

    /**
     * 患者记费标志
     */
    @Field("flag_cg")
    private String flagCg;

    /**
     * 关联的患者就诊
     */
    @Field("pk_pv")
    private String pkPv;

    /**
     * 关联的患者记费
     */
    @Field("pk_cg")
    private String pkCg;

    /**
     * 平台返回明细编号
     */
    @Field("code_rtndt")
    private String codeRtndt;

    /**
     * 创建人
     */
    @Field("creator")
    private String creator;

    /**
     * 创建时间
     */
    @Field("create_time")
    private String createTime;

    /**
     * 修改时间
     */
    @Field("modity_time")
    private String modityTime;

    /**
     * 修改人
     */
    @Field("modifier")
    private String modifier;

    /**
     * 备注
     */
    @Field("note")
    private String note;

    /**
     * 删除标志
     */
    @Field("del_flag")
    private String delFlag;

    /**
     * 时间戳
     */
    @Field("ts")
    private String ts;

    public ZsrmHrpPdStDetailVo() {
    }

    public String getPkPdstdt() {
        return pkPdstdt;
    }

    public String getPkOrg() {
        return pkOrg;
    }

    public String getPkPdst() {
        return pkPdst;
    }

    public Integer getSortNo() {
        return sortNo;
    }

    public String getPkPd() {
        return pkPd;
    }

    public String getPkUnitPack() {
        return pkUnitPack;
    }

    public Integer getPackSize() {
        return packSize;
    }

    public Double getQuanPack() {
        return quanPack;
    }

    public Double getQuanMin() {
        return quanMin;
    }

    public Double getQuanOutstore() {
        return quanOutstore;
    }

    public Double getPriceCost() {
        return priceCost;
    }

    public Double getPrice() {
        return price;
    }

    public Double getAmountCost() {
        return amountCost;
    }

    public Double getAmount() {
        return amount;
    }

    public Double getDisc() {
        return disc;
    }

    public String getBatchNo() {
        return batchNo;
    }

    public Date getDateFac() {
        return DateFac;
    }

    public Date getDateExpire() {
        return dateExpire;
    }

    public String getInvNo() {
        return invNo;
    }

    public String getReceiptNo() {
        return receiptNo;
    }

    public String getFlagChkRpt() {
        return flagChkRpt;
    }

    public String getPkEmpChkRpt() {
        return pkEmpChkRpt;
    }

    public String getNameEmpChkRpt() {
        return nameEmpChkRpt;
    }

    public String getPkSupplyer() {
        return pkSupplyer;
    }

    public String getPkPdpay() {
        return pkPdpay;
    }

    public Double getAmountPay() {
        return amountPay;
    }

    public String getFlagPay() {
        return flagPay;
    }

    public String getPkPdpudt() {
        return pkPdpudt;
    }

    public String getFlagFinish() {
        return flagFinish;
    }

    public String getPkPdstdtRl() {
        return pkPdstdtRl;
    }

    public String getFlagCg() {
        return flagCg;
    }

    public String getPkPv() {
        return pkPv;
    }

    public String getPkCg() {
        return pkCg;
    }

    public String getCodeRtndt() {
        return codeRtndt;
    }

    public String getCreator() {
        return creator;
    }

    public String getCreateTime() {
        return createTime;
    }

    public String getModityTime() {
        return modityTime;
    }

    public String getModifier() {
        return modifier;
    }

    public String getNote() {
        return note;
    }

    public String getDelFlag() {
        return delFlag;
    }

    public String getTs() {
        return ts;
    }

    public void setPkPdstdt(String pkPdstdt) {
        this.pkPdstdt = pkPdstdt;
    }

    public void setPkOrg(String pkOrg) {
        this.pkOrg = pkOrg;
    }

    public void setPkPdst(String pkPdst) {
        this.pkPdst = pkPdst;
    }

    public void setSortNo(Integer sortNo) {
        this.sortNo = sortNo;
    }

    public void setPkPd(String pkPd) {
        this.pkPd = pkPd;
    }

    public void setPkUnitPack(String pkUnitPack) {
        this.pkUnitPack = pkUnitPack;
    }

    public void setPackSize(Integer packSize) {
        this.packSize = packSize;
    }

    public void setQuanPack(Double quanPack) {
        this.quanPack = quanPack;
    }

    public void setQuanMin(Double quanMin) {
        this.quanMin = quanMin;
    }

    public void setQuanOutstore(Double quanOutstore) {
        this.quanOutstore = quanOutstore;
    }

    public void setPriceCost(Double priceCost) {
        this.priceCost = priceCost;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public void setAmountCost(Double amountCost) {
        this.amountCost = amountCost;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public void setDisc(Double disc) {
        this.disc = disc;
    }

    public void setBatchNo(String batchNo) {
        this.batchNo = batchNo;
    }

    public void setDateFac(Date dateFac) {
        DateFac = dateFac;
    }

    public void setDateExpire(Date dateExpire) {
        this.dateExpire = dateExpire;
    }

    public void setInv_no(String invNo) {
        this.invNo = invNo;
    }

    public void setReceiptNo(String receiptNo) {
        this.receiptNo = receiptNo;
    }

    public void setFlagChkRpt(String flagChkRpt) {
        this.flagChkRpt = flagChkRpt;
    }

    public void setPkEmpChkRpt(String pkEmpChkRpt) {
        this.pkEmpChkRpt = pkEmpChkRpt;
    }

    public void setNameEmpChkRpt(String nameEmpChkRpt) {
        this.nameEmpChkRpt = nameEmpChkRpt;
    }

    public void setPkSupplyer(String pkSupplyer) {
        this.pkSupplyer = pkSupplyer;
    }

    public void setPkPdpay(String pkPdpay) {
        this.pkPdpay = pkPdpay;
    }

    public void setAmountPay(Double amountPay) {
        this.amountPay = amountPay;
    }

    public void setFlagPay(String flagPay) {
        this.flagPay = flagPay;
    }

    public void setPkPdpudt(String pkPdpudt) {
        this.pkPdpudt = pkPdpudt;
    }

    public void setFlagFinish(String flagFinish) {
        this.flagFinish = flagFinish;
    }

    public void setPkPdstdtRl(String pkPdstdtRl) {
        this.pkPdstdtRl = pkPdstdtRl;
    }

    public void setFlagCg(String flagCg) {
        this.flagCg = flagCg;
    }

    public void setPkPv(String pkPv) {
        this.pkPv = pkPv;
    }

    public void setPkCg(String pkCg) {
        this.pkCg = pkCg;
    }

    public void setCodeRtndt(String codeRtndt) {
        this.codeRtndt = codeRtndt;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public void setModityTime(String modityTime) {
        this.modityTime = modityTime;
    }

    public void setModifier(String modifier) {
        this.modifier = modifier;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public void setDelFlag(String delFlag) {
        this.delFlag = delFlag;
    }

    public void setTs(String ts) {
        this.ts = ts;
    }

    public Date getDateChkRpt() {
        return dateChkRpt;
    }

    public void setDateChkRpt(Date dateChkRpt) {
        this.dateChkRpt = dateChkRpt;
    }
}
