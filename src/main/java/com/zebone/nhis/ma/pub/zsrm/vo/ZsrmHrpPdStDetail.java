package com.zebone.nhis.ma.pub.zsrm.vo;

import com.sun.org.apache.bcel.internal.generic.FLOAD;
import com.zebone.platform.modules.dao.build.au.Field;
import com.zebone.platform.modules.dao.build.au.PK;
import com.zebone.platform.modules.dao.build.au.Table;

@Table("pd_st_detail")
public class ZsrmHrpPdStDetail {

    /**
     * 库单明细主键
     */
    @PK
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
    private String pk_pdst;

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
    private Integer pack_size;

    /**
     * 包装单位下的数量
     */
    @Field("quan_pack")
    private Float quanPack;

    /**
     * 基本单位下的数量
     */
    @Field("quan_min")
    private Float quanMin;

    @Field("quan_outstore")
    private Float quanOutstore;

    /**
     * 成本单价
     */
    @Field("price_cost")
    private Float  priceCost;

    /**
     * 零售单价
     */
    @Field("price")
    private Float price;

    /**
     * 成本金额
     */
    @Field("amount_cost")
    private Float amountCost;

    /**
     * 零售金额
     */
    @Field("amount")
    private Float  amount;

    /**
     * 扣率
     */
    @Field("disc")
    private Float disc;

    /**
     * 批号
     */
    @Field("batch_no")
    private String batchNo;

    /**
     * 生产日期
     */
    @Field("date_fac")
    private String DateFac;

    /**
     * 失效日期
     */
    @Field("date_expire")
    private String dateExpire;

    /**
     * 发货单号
     */
    @Field("inv_no")
    private String inv_no;

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
    private Float amountPay;

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
    private String dateChkRpt;

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

    public String getPkPdstdt() {
        return pkPdstdt;
    }

    public String getPkOrg() {
        return pkOrg;
    }

    public String getPk_pdst() {
        return pk_pdst;
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

    public Integer getPack_size() {
        return pack_size;
    }

    public Float getQuanPack() {
        return quanPack;
    }

    public Float getQuanMin() {
        return quanMin;
    }

    public Float getQuanOutstore() {
        return quanOutstore;
    }

    public Float getPriceCost() {
        return priceCost;
    }

    public Float getPrice() {
        return price;
    }

    public Float getAmountCost() {
        return amountCost;
    }

    public Float getAmount() {
        return amount;
    }

    public Float getDisc() {
        return disc;
    }

    public String getBatchNo() {
        return batchNo;
    }

    public String getDateFac() {
        return DateFac;
    }

    public String getDateExpire() {
        return dateExpire;
    }

    public String getInv_no() {
        return inv_no;
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

    public Float getAmountPay() {
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

    public void setPk_pdst(String pk_pdst) {
        this.pk_pdst = pk_pdst;
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

    public void setPack_size(Integer pack_size) {
        this.pack_size = pack_size;
    }

    public void setQuanPack(Float quanPack) {
        this.quanPack = quanPack;
    }

    public void setQuanMin(Float quanMin) {
        this.quanMin = quanMin;
    }

    public void setQuanOutstore(Float quanOutstore) {
        this.quanOutstore = quanOutstore;
    }

    public void setPriceCost(Float priceCost) {
        this.priceCost = priceCost;
    }

    public void setPrice(Float price) {
        this.price = price;
    }

    public void setAmountCost(Float amountCost) {
        this.amountCost = amountCost;
    }

    public void setAmount(Float amount) {
        this.amount = amount;
    }

    public void setDisc(Float disc) {
        this.disc = disc;
    }

    public void setBatchNo(String batchNo) {
        this.batchNo = batchNo;
    }

    public void setDateFac(String dateFac) {
        DateFac = dateFac;
    }

    public void setDateExpire(String dateExpire) {
        this.dateExpire = dateExpire;
    }

    public void setInv_no(String inv_no) {
        this.inv_no = inv_no;
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

    public void setAmountPay(Float amountPay) {
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

    public String getDateChkRpt() {
        return dateChkRpt;
    }

    public void setDateChkRpt(String dateChkRpt) {
        this.dateChkRpt = dateChkRpt;
    }
}
