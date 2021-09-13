package com.zebone.nhis.ma.pub.lb.vo;

import com.alibaba.fastjson.annotation.JSONField;

import java.math.BigDecimal;
import java.util.Date;

public class MedicalAdviceVo {
    private String pkPv;
    /**
     * 就诊流水号
     */

    @JSONField(name = "Visitno")
    private String visitno;
    /**
     * 医嘱号
     */
    private String inpatientRxno;
    /**
     * 医嘱组号
     */

    private String setno;
    /**
     * 费用发生日期
     */
    @JSONField(format ="yyyy-MM-dd HH:mm:ss")
    private Date useDate;
    /**
     * 医院收费项目编码
     *
     */
    private String hiscode;
    /**
     * 医院收费项目名称
     */
    private String hisname;
    /**
     * 医保项目编码
     */
    private String insureId;
    /**
     * 医保项目名称
     */
    private String insureName;
    /**
     * 剂型
     */
    private String conf;
    /**
     * 规格
     */
    private String spec;
    /**
     * 单位
     */
    private String unit;
    /**
     * 单价
     */
    private BigDecimal price;
    /**
     * 数量
     */
    private BigDecimal quantity;
    /**
     * 合计
     */
    private BigDecimal money;
    /**
     * 药品单次用量
     */
    private BigDecimal ord11;
    /**
     * 药品剂量单位
     */
    private String ord12;
    /**
     * 频次
     */
    private String ord13;
    /**
     * 给药途径
     */
    private String ord14;
    /**
     * 医嘱类型
     */
    private int type;

    public String getPkPv() {
        return pkPv;
    }

    public void setPkPv(String pkPv) {
        this.pkPv = pkPv;
    }

    public String getVisitno() {
        return visitno;
    }

    public void setVisitno(String visitno) {
        this.visitno = visitno;
    }

    public String getInpatientRxno() {
        return inpatientRxno;
    }

    public void setInpatientRxno(String inpatientRxno) {
        this.inpatientRxno = inpatientRxno;
    }

    public Date getUseDate() {
        return useDate;
    }

    public void setUseDate(Date useDate) {
        this.useDate = useDate;
    }

    public String getHiscode() {
        return hiscode;
    }

    public void setHiscode(String hiscode) {
        this.hiscode = hiscode;
    }

    public String getHisname() {
        return hisname;
    }

    public void setHisname(String hisname) {
        this.hisname = hisname;
    }

    public String getInsureId() {
        return insureId;
    }

    public void setInsureId(String insureId) {
        this.insureId = insureId;
    }

    public String getInsureName() {
        return insureName;
    }

    public void setInsureName(String insureName) {
        this.insureName = insureName;
    }

    public String getConf() {
        return conf;
    }

    public void setConf(String conf) {
        this.conf = conf;
    }

    public String getSpec() {
        return spec;
    }

    public void setSpec(String spec) {
        this.spec = spec;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public BigDecimal getMoney() {
        return money;
    }

    public void setMoney(BigDecimal money) {
        this.money = money;
    }

    public BigDecimal getQuantity() {
        return quantity;
    }

    public void setQuantity(BigDecimal quantity) {
        this.quantity = quantity;
    }

    public BigDecimal getOrd11() {
        return ord11;
    }

    public void setOrd11(BigDecimal ord11) {
        this.ord11 = ord11;
    }

    public String getOrd12() {
        return ord12;
    }

    public void setOrd12(String ord12) {
        this.ord12 = ord12;
    }

    public String getOrd13() {
        return ord13;
    }

    public void setOrd13(String ord13) {
        this.ord13 = ord13;
    }

    public String getOrd14() {
        return ord14;
    }

    public void setOrd14(String ord14) {
        this.ord14 = ord14;
    }

    public String getSetno() {
        return setno;
    }

    public void setSetno(String setno) {
        this.setno = setno;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
