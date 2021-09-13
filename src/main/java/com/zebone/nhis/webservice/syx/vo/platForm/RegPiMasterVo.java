package com.zebone.nhis.webservice.syx.vo.platForm;

import com.zebone.nhis.bl.pub.vo.ItemPriceVo;
import com.zebone.nhis.common.module.bl.BlDeposit;
import com.zebone.nhis.common.module.bl.BlInvoiceDt;
import com.zebone.nhis.common.module.pi.PiMaster;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

public class RegPiMasterVo extends PiMaster {

    private String cardPv;//诊疗卡号
    private String pkHp;//医保类型

    //挂号信息

    private Date dateReg;//挂号日期--排班日期
    private String pkDept;//挂号科室
    private String pkSrv;//挂号类型，诊疗服务
    private String pkSchres;//排班资源
    private String pkSchsrv;//排班服务
    private String pkSch;//排班主键
    private String pkSchplan;//排班计划主键
    private String pkDateslot;//日期分组
    private String euSrvtype;//挂号类型的服务类型
    private String euSchclass;//排班类型
    private String outsideOrderId;//外部预约系统订单号
    private String orderSource;//外部预约来源

    //已挂号信息
    private String pkPv;
    private String codePv;//就诊流水号
    private String pkEmpReg;
    private String nameEmpReg;
    private String pkEmpCancel;
    private String nameEmpCancel;
    private Date dateCancel;
    private String euStatus;
    private String euPvtype;
    private String flagSpec;//特诊标志
    private String agePv;//年龄
    private String euRegtype;//挂号类型
    private Date datePrint;//打印时间
    private Date dateBegin;//就诊时间
    private String pkDeptunit;//诊室


    //发票信息
    private String pkEmpinv;//票据领用主键
    private String pkInvcate;//发票分类主键
    private String codeInv;//发票号码
    private String pkSettle;//结算信息

    //预约信息
    private Date dateAppt;//预约日期
    private String pkAppt;//预约登记主键
    private String ticketNo;//预约票号
    private String apptCode;//预约单号

    private List<ItemPriceVo> itemList;//收费项目明细
    private List<BlDeposit> depositList;//交易方式明细
    private List<BlInvoiceDt> blInvoiceDtList;//发票明细

    private BigDecimal amtInsuThird;//第三方医保支付金额

    public String getOrderSource() {
        return orderSource;
    }

    public void setOrderSource(String orderSource) {
        this.orderSource = orderSource;
    }

    public String getOutsideOrderId() {
        return outsideOrderId;
    }

    public void setOutsideOrderId(String outsideOrderId) {
        this.outsideOrderId = outsideOrderId;
    }

    public String getPkDeptunit() {
        return pkDeptunit;
    }

    public void setPkDeptunit(String pkDeptunit) {
        this.pkDeptunit = pkDeptunit;
    }

    public String getCardPv() {
        return cardPv;
    }

    public void setCardPv(String cardPv) {
        this.cardPv = cardPv;
    }

    public String getPkHp() {
        return pkHp;
    }

    public void setPkHp(String pkHp) {
        this.pkHp = pkHp;
    }

    public Date getDateReg() {
        return dateReg;
    }

    public void setDateReg(Date dateReg) {
        this.dateReg = dateReg;
    }

    public String getPkDept() {
        return pkDept;
    }

    public void setPkDept(String pkDept) {
        this.pkDept = pkDept;
    }

    public String getPkSrv() {
        return pkSrv;
    }

    public void setPkSrv(String pkSrv) {
        this.pkSrv = pkSrv;
    }

    public String getPkSchres() {
        return pkSchres;
    }

    public void setPkSchres(String pkSchres) {
        this.pkSchres = pkSchres;
    }

    public String getPkSchsrv() {
        return pkSchsrv;
    }

    public void setPkSchsrv(String pkSchsrv) {
        this.pkSchsrv = pkSchsrv;
    }

    public String getPkSchplan() {
        return pkSchplan;
    }

    public void setPkSchplan(String pkSchplan) {
        this.pkSchplan = pkSchplan;
    }

    public String getPkDateslot() {
        return pkDateslot;
    }

    public void setPkDateslot(String pkDateslot) {
        this.pkDateslot = pkDateslot;
    }

    public String getEuSrvtype() {
        return euSrvtype;
    }

    public void setEuSrvtype(String euSrvtype) {
        this.euSrvtype = euSrvtype;
    }

    public String getPkPv() {
        return pkPv;
    }

    public void setPkPv(String pkPv) {
        this.pkPv = pkPv;
    }

    public String getCodePv() {
        return codePv;
    }

    public void setCodePv(String codePv) {
        this.codePv = codePv;
    }

    public String getPkEmpReg() {
        return pkEmpReg;
    }

    public void setPkEmpReg(String pkEmpReg) {
        this.pkEmpReg = pkEmpReg;
    }

    public String getNameEmpReg() {
        return nameEmpReg;
    }

    public void setNameEmpReg(String nameEmpReg) {
        this.nameEmpReg = nameEmpReg;
    }

    public String getPkEmpCancel() {
        return pkEmpCancel;
    }

    public void setPkEmpCancel(String pkEmpCancel) {
        this.pkEmpCancel = pkEmpCancel;
    }

    public String getNameEmpCancel() {
        return nameEmpCancel;
    }

    public void setNameEmpCancel(String nameEmpCancel) {
        this.nameEmpCancel = nameEmpCancel;
    }

    public Date getDateCancel() {
        return dateCancel;
    }

    public void setDateCancel(Date dateCancel) {
        this.dateCancel = dateCancel;
    }

    public String getEuStatus() {
        return euStatus;
    }

    public void setEuStatus(String euStatus) {
        this.euStatus = euStatus;
    }

    public String getEuPvtype() {
        return euPvtype;
    }

    public void setEuPvtype(String euPvtype) {
        this.euPvtype = euPvtype;
    }

    public String getFlagSpec() {
        return flagSpec;
    }

    public void setFlagSpec(String flagSpec) {
        this.flagSpec = flagSpec;
    }

    public String getAgePv() {
        return agePv;
    }

    public void setAgePv(String agePv) {
        this.agePv = agePv;
    }

    public String getEuRegtype() {
        return euRegtype;
    }

    public void setEuRegtype(String euRegtype) {
        this.euRegtype = euRegtype;
    }

    public Date getDatePrint() {
        return datePrint;
    }

    public void setDatePrint(Date datePrint) {
        this.datePrint = datePrint;
    }

    public Date getDateBegin() {
        return dateBegin;
    }

    public void setDateBegin(Date dateBegin) {
        this.dateBegin = dateBegin;
    }

    public String getPkEmpinv() {
        return pkEmpinv;
    }

    public void setPkEmpinv(String pkEmpinv) {
        this.pkEmpinv = pkEmpinv;
    }

    public String getPkInvcate() {
        return pkInvcate;
    }

    public void setPkInvcate(String pkInvcate) {
        this.pkInvcate = pkInvcate;
    }

    public String getCodeInv() {
        return codeInv;
    }

    public void setCodeInv(String codeInv) {
        this.codeInv = codeInv;
    }

    public String getPkSettle() {
        return pkSettle;
    }

    public void setPkSettle(String pkSettle) {
        this.pkSettle = pkSettle;
    }

    public Date getDateAppt() {
        return dateAppt;
    }

    public void setDateAppt(Date dateAppt) {
        this.dateAppt = dateAppt;
    }

    public String getPkAppt() {
        return pkAppt;
    }

    public void setPkAppt(String pkAppt) {
        this.pkAppt = pkAppt;
    }

    public String getTicketNo() {
        return ticketNo;
    }

    public void setTicketNo(String ticketNo) {
        this.ticketNo = ticketNo;
    }

    public List<ItemPriceVo> getItemList() {
        return itemList;
    }

    public void setItemList(List<ItemPriceVo> itemList) {
        this.itemList = itemList;
    }

    public List<BlDeposit> getDepositList() {
        return depositList;
    }

    public void setDepositList(List<BlDeposit> depositList) {
        this.depositList = depositList;
    }

    public List<BlInvoiceDt> getBlInvoiceDtList() {
        return blInvoiceDtList;
    }

    public void setBlInvoiceDtList(List<BlInvoiceDt> blInvoiceDtList) {
        this.blInvoiceDtList = blInvoiceDtList;
    }

    public BigDecimal getAmtInsuThird() {
        return amtInsuThird;
    }

    public void setAmtInsuThird(BigDecimal amtInsuThird) {
        this.amtInsuThird = amtInsuThird;
    }

    public String getApptCode() {
        return apptCode;
    }

    public void setApptCode(String apptCode) {
        this.apptCode = apptCode;
    }

    public String getEuSchclass() {
        return euSchclass;
    }

    public void setEuSchclass(String euSchclass) {
        this.euSchclass = euSchclass;
    }

    public String getPkSch() {
        return pkSch;
    }

    public void setPkSch(String pkSch) {
        this.pkSch = pkSch;
    }
}
