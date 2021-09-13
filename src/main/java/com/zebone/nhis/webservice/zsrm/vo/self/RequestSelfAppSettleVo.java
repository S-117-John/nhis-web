package com.zebone.nhis.webservice.zsrm.vo.self;

import java.util.List;

/**
 * 自助机结算接口
 */
public class RequestSelfAppSettleVo extends CommonReqSelfVo {
    private String isElecTicket;

    private String isHp;
    
    private String isHpOnline;

    private String euPayType;

    private String tradeNo;

    private String dtBank;

    private String nameBank;

    private String cardNo;

    private String flagPay;

    private String sysname;

    private String resultPay;

    private String codeEmpSt;

    private String nameEmpSt;

    private String codeDeptSt;

    private String nameDeptSt;

    private String serialNo;

    private Double amountExt;

    private Double amountPi;

    private Double amountSt;

    private Double amountInsu;

    private String dateOrder;

    private ReqYbPreSettleInfovo ybPreSettleInfo;

    private ReqYbPreSettleParamVo ybPreSettleParam;
    
    private List<RequestSelfSettleDtVo> dataList;
    
    private RequestYbSettleVo setlinfo;
    
    private List<RequestYbSettleDtVo> setldetail;
   
    

    public String getIsElecTicket() {
        return isElecTicket;
    }

    public void setIsElecTicket(String isElecTicket) {
        this.isElecTicket = isElecTicket;
    }

    public String getEuPayType() {
        return euPayType;
    }

    public void setEuPayType(String euPayType) {
        this.euPayType = euPayType;
    }

    public String getTradeNo() {
        return tradeNo;
    }

    public void setTradeNo(String tradeNo) {
        this.tradeNo = tradeNo;
    }

    public String getSerialNo() {
        return serialNo;
    }

    public void setSerialNo(String serialNo) {
        this.serialNo = serialNo;
    }

    public Double getAmountExt() {
        return amountExt;
    }

    public void setAmountExt(Double amountExt) {
        this.amountExt = amountExt;
    }

    public Double getAmountPi() {
        return amountPi;
    }

    public void setAmountPi(Double amountPi) {
        this.amountPi = amountPi;
    }

    public Double getAmountSt() {
        return amountSt;
    }

    public void setAmountSt(Double amountSt) {
        this.amountSt = amountSt;
    }

    public Double getAmountInsu() {
        return amountInsu;
    }

    public void setAmountInsu(Double amountInsu) {
        this.amountInsu = amountInsu;
    }

    public List<RequestSelfSettleDtVo> getDataList() {
        return dataList;
    }

    public void setDataList(List<RequestSelfSettleDtVo> dataList) {
        this.dataList = dataList;
    }

    public String getDateOrder() {
        return dateOrder;
    }

    public void setDateOrder(String dateOrder) {
        this.dateOrder = dateOrder;
    }

    public String getIsHp() {
        return isHp;
    }

    public void setIsHp(String isHp) {
        this.isHp = isHp;
    }

    public String getDtBank() {
        return dtBank;
    }

    public void setDtBank(String dtBank) {
        this.dtBank = dtBank;
    }

    public String getNameBank() {
        return nameBank;
    }

    public void setNameBank(String nameBank) {
        this.nameBank = nameBank;
    }

    public String getCardNo() {
        return cardNo;
    }

    public void setCardNo(String cardNo) {
        this.cardNo = cardNo;
    }

    public String getFlagPay() {
        return flagPay;
    }

    public void setFlagPay(String flagPay) {
        this.flagPay = flagPay;
    }

    public String getSysname() {
        return sysname;
    }

    public void setSysname(String sysname) {
        this.sysname = sysname;
    }

    public String getResultPay() {
        return resultPay;
    }

    public void setResultPay(String resultPay) {
        this.resultPay = resultPay;
    }

    public String getCodeEmpSt() {
        return codeEmpSt;
    }

    public void setCodeEmpSt(String codeEmpSt) {
        this.codeEmpSt = codeEmpSt;
    }

    public String getNameEmpSt() {
        return nameEmpSt;
    }

    public void setNameEmpSt(String nameEmpSt) {
        this.nameEmpSt = nameEmpSt;
    }

    public String getCodeDeptSt() {
        return codeDeptSt;
    }

    public void setCodeDeptSt(String codeDeptSt) {
        this.codeDeptSt = codeDeptSt;
    }

    public String getNameDeptSt() {
        return nameDeptSt;
    }

    public void setNameDeptSt(String nameDeptSt) {
        this.nameDeptSt = nameDeptSt;
    }

    public ReqYbPreSettleInfovo getYbPreSettleInfo() {
        return ybPreSettleInfo;
    }

    public void setYbPreSettleInfo(ReqYbPreSettleInfovo ybPreSettleInfo) {
        this.ybPreSettleInfo = ybPreSettleInfo;
    }

    public ReqYbPreSettleParamVo getYbPreSettleParam() {
        return ybPreSettleParam;
    }

    public void setYbPreSettleParam(ReqYbPreSettleParamVo ybPreSettleParam) {
        this.ybPreSettleParam = ybPreSettleParam;
    }

	public String getIsHpOnline() {
		return isHpOnline;
	}

	public void setIsHpOnline(String isHpOnline) {
		this.isHpOnline = isHpOnline;
	}

	public RequestYbSettleVo getSetlinfo() {
		return setlinfo;
	}

	public void setSetlinfo(RequestYbSettleVo setlinfo) {
		this.setlinfo = setlinfo;
	}

	public List<RequestYbSettleDtVo> getSetldetail() {
		return setldetail;
	}

	public void setSetldetail(List<RequestYbSettleDtVo> setldetail) {
		this.setldetail = setldetail;
	}

    
}

