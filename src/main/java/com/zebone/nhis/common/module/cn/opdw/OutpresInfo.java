package com.zebone.nhis.common.module.cn.opdw;

import com.zebone.nhis.common.module.BaseModule;
import com.zebone.platform.modules.dao.build.au.Field;
import com.zebone.platform.modules.dao.build.au.PK;
import com.zebone.platform.modules.dao.build.au.Table;

import java.math.BigDecimal;
import java.util.Date;

@Table(value="OUTPRES_INFO")
public class OutpresInfo extends BaseModule {
    @PK
    @Field(value="PK_PRESINFO")
    private String pkPresinfo;

    @Field(value="PK_PRES")
    private String pkPres;

    @Field(value="PRES_ID")
    private String presId;

    @Field(value="EU_STATUS")
    private String euStatus;

    @Field(value="FILEID")
    private String fileid;

    @Field(value="FILEURL")
    private String fileurl;

    @Field(value="BUYDRUGCODE")
    private String buydrugcode;

    @Field(value="DRUGSTORES")
    private String drugstores;

    @Field(value="checkdr")
    private String checkdr;

    @Field(value="remark")
    private String remark;

    @Field(value="PAYSTATUS")
    private String paystatus;

    @Field(value="PAYTIME")
    private Date paytime;

    @Field(value="PAYAMT")
    private BigDecimal payamt;

    @Field(value="ORDER_TIME")
    private Date orderTime;

    @Field(value="ORDER_NO")
    private String orderNo;

    @Field(value="RESULT")
    private String result;

    public String getPkPresinfo() {
        return pkPresinfo;
    }

    public void setPkPresinfo(String pkPresinfo) {
        this.pkPresinfo = pkPresinfo;
    }

    public String getPkPres() {
        return pkPres;
    }

    public void setPkPres(String pkPres) {
        this.pkPres = pkPres;
    }

    public String getPresId() {
        return presId;
    }

    public void setPresId(String presId) {
        this.presId = presId;
    }

    public String getEuStatus() {
        return euStatus;
    }

    public void setEuStatus(String euStatus) {
        this.euStatus = euStatus;
    }

    public String getFileid() {
        return fileid;
    }

    public void setFileid(String fileid) {
        this.fileid = fileid;
    }

    public String getFileurl() {
        return fileurl;
    }

    public void setFileurl(String fileurl) {
        this.fileurl = fileurl;
    }

    public String getBuydrugcode() {
        return buydrugcode;
    }

    public void setBuydrugcode(String buydrugcode) {
        this.buydrugcode = buydrugcode;
    }

    public String getDrugstores() {
        return drugstores;
    }

    public void setDrugstores(String drugstores) {
        this.drugstores = drugstores;
    }

    public String getCheckdr() {
        return checkdr;
    }

    public void setCheckdr(String checkdr) {
        this.checkdr = checkdr;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getPaystatus() {
        return paystatus;
    }

    public void setPaystatus(String paystatus) {
        this.paystatus = paystatus;
    }

    public Date getPaytime() {
        return paytime;
    }

    public void setPaytime(Date paytime) {
        this.paytime = paytime;
    }

    public BigDecimal getPayamt() {
        return payamt;
    }

    public void setPayamt(BigDecimal payamt) {
        this.payamt = payamt;
    }

    public Date getOrderTime() {
        return orderTime;
    }

    public void setOrderTime(Date orderTime) {
        this.orderTime = orderTime;
    }

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

}