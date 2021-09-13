package com.zebone.nhis.cn.opdw.vo;

import java.util.Date;
import java.util.List;

import com.zebone.nhis.common.module.cn.ipdw.CnOrder;

public class SyxCnOrderVo extends CnOrder{
   
	private int rNum;
	private int gNum;
    private String euHptype; 
	private String note;
	private String unit;
	private String unitDos;
	private String supply;
	private String freq;
	private String pkUnitMin;
	private String pkUnitVol;
	private String pkUnitWt;
	private Double vol;
	private Double amount ;
	private Double weight;
	private Double quotaDos;
	private String flagSt;
	private String euDrugtype;
	private String euSex;
	private Double ageMin;
	private Double ageMax;
	private String nameAgent;
	private String idnoAgent;
	private String addrAgent;
	private String telAgent;
	private String dtExtreason;
	private String flagPrt;
    private Integer ordNum;
    private Date datePres;
    private Integer ordParentNum;
    private String flagSettle;
    private String dtRistype;
    private String pkOrdris;
    private String pkOrdlis;
    private String dtTubetype;
    private double quanMin;
    private Double packSizePd;
    private Double pricePd;
    private String dtPois;
    private String formApp;
    private String purpose;/**检查目的*/
    private String noteDise;/**病情描述*/
	private String risNotice;/**检查注意事项*/
	private String dtExcardtype;
	private String euMuputypeOp;
	private String isCheck; /**是否选中**/
	private String hpRate;/**报销比例**/
	private String flagChangQuan;/**计费包装选用**/

	private String flagRescue;/**抢救标记**/

	private String flagZero;/**零元购标记**/
	private Double quanMax;/**可开立最大数量**/
	private Double zeroMax;/**允许开立最大数量**/
	private Double amountBa ;
    private List<SyxBlOpDt> addItems;
    private List<SyxBlOpDt> AddSupplyItems;
    private String euUsecate;//药品用法分类

	private String notePres;//处方备注

	private List<OrdBdPdAttVo> blPdItems;/**检查用药**/

	private Double bpsPackSize;//仓库包装量

	public String getBarcode() {
		return barcode;
	}

	public void setBarcode(String barcode) {
		this.barcode = barcode;
	}

	private String barcode;/**高值耗材**/

	public List<SyxBlOpDt> getAddSupplyItems() {
		return AddSupplyItems;
	}
	public void setAddSupplyItems(List<SyxBlOpDt> addSupplyItems) {
		AddSupplyItems = addSupplyItems;
	}
	public String getDtTubetype() {
		return dtTubetype;
	}
	public void setDtTubetype(String dtTubetype) {
		this.dtTubetype = dtTubetype;
	}
	public String getPkOrdris() {
		return pkOrdris;
	}
	public void setPkOrdris(String pkOrdris) {
		this.pkOrdris = pkOrdris;
	}
	public String getPkOrdlis() {
		return pkOrdlis;
	}
	public void setPkOrdlis(String pkOrdlis) {
		this.pkOrdlis = pkOrdlis;
	}
	public String getFlagSettle() {
		return flagSettle;
	}
	public void setFlagSettle(String flagSettle) {
		this.flagSettle = flagSettle;
	}
	public String getDtRistype() {
		return dtRistype;
	}
	public void setDtRistype(String dtRistype) {
		this.dtRistype = dtRistype;
	}
	
	public int getrNum() {
		return rNum;
	}
	public void setrNum(int rNum) {
		this.rNum = rNum;
	}
	public String getEuHptype() {
		return euHptype;
	}
	public void setEuHptype(String euHptype) {
		this.euHptype = euHptype;
	}
	public String getNote() {
		return note;
	}
	public void setNote(String note) {
		this.note = note;
	}
	public String getNameAgent() {
		return nameAgent;
	}
	public void setNameAgent(String nameAgent) {
		this.nameAgent = nameAgent;
	}
	public String getIdnoAgent() {
		return idnoAgent;
	}
	public void setIdnoAgent(String idnoAgent) {
		this.idnoAgent = idnoAgent;
	}
	public String getAddrAgent() {
		return addrAgent;
	}
	public void setAddrAgent(String addrAgent) {
		this.addrAgent = addrAgent;
	}
	public String getTelAgent() {
		return telAgent;
	}
	public void setTelAgent(String telAgent) {
		this.telAgent = telAgent;
	}
	public String getDtExtreason() {
		return dtExtreason;
	}
	public void setDtExtreason(String dtExtreason) {
		this.dtExtreason = dtExtreason;
	}
	public String getFlagPrt() {
		return flagPrt;
	}
	public void setFlagPrt(String flagPrt) {
		this.flagPrt = flagPrt;
	}
	public Integer getOrdNum() {
		return ordNum;
	}
	public void setOrdNum(Integer ordNum) {
		this.ordNum = ordNum;
	}
	public Integer getOrdParentNum() {
		return ordParentNum;
	}
	public void setOrdParentNum(Integer ordParentNum) {
		this.ordParentNum = ordParentNum;
	}
	public List<SyxBlOpDt> getAddItems() {
		return addItems;
	}
	public void setAddItems(List<SyxBlOpDt> addItems) {
		this.addItems = addItems;
	}
	public String getUnit() {
		return unit;
	}
	public void setUnit(String unit) {
		this.unit = unit;
	}
	public String getUnitDos() {
		return unitDos;
	}
	public void setUnitDos(String unitDos) {
		this.unitDos = unitDos;
	}
	public String getSupply() {
		return supply;
	}
	public void setSupply(String supply) {
		this.supply = supply;
	}
	public String getFreq() {
		return freq;
	}
	public void setFreq(String freq) {
		this.freq = freq;
	}
	public String getPkUnitMin() {
		return pkUnitMin;
	}
	public void setPkUnitMin(String pkUnitMin) {
		this.pkUnitMin = pkUnitMin;
	}
	public String getPkUnitVol() {
		return pkUnitVol;
	}
	public void setPkUnitVol(String pkUnitVol) {
		this.pkUnitVol = pkUnitVol;
	}
	public String getPkUnitWt() {
		return pkUnitWt;
	}
	public void setPkUnitWt(String pkUnitWt) {
		this.pkUnitWt = pkUnitWt;
	}
	public Double getVol() {
		return vol;
	}
	public void setVol(Double vol) {
		this.vol = vol;
	}
	public Double getAmount() {
		return amount;
	}
	public void setAmount(Double amount) {
		this.amount = amount;
	}
	public Double getWeight() {
		return weight;
	}
	public void setWeight(Double weight) {
		this.weight = weight;
	}
	public Double getQuotaDos() {
		return quotaDos;
	}
	public void setQuotaDos(Double quotaDos) {
		this.quotaDos = quotaDos;
	}
	public String getFlagSt() {
		return flagSt;
	}
	public void setFlagSt(String flagSt) {
		this.flagSt = flagSt;
	}
	public String getEuDrugtype() {
		return euDrugtype;
	}
	public void setEuDrugtype(String euDrugtype) {
		this.euDrugtype = euDrugtype;
	}
	public String getEuSex() {
		return euSex;
	}
	public void setEuSex(String euSex) {
		this.euSex = euSex;
	}

	public Double getAgeMin() {
		return ageMin;
	}
	public void setAgeMin(Double ageMin) {
		this.ageMin = ageMin;
	}
	public Double getAgeMax() {
		return ageMax;
	}
	public void setAgeMax(Double ageMax) {
		this.ageMax = ageMax;
	}
	public Date getDatePres() {
		return datePres;
	}
	public void setDatePres(Date datePres) {
		this.datePres = datePres;
	}
	public double getQuanMin() {
		return quanMin;
	}
	public void setQuanMin(double quanMin) {
		this.quanMin = quanMin;
	}
	public Double getPackSizePd() {
		return packSizePd;
	}
	public void setPackSizePd(Double packSizePd) {
		this.packSizePd = packSizePd;
	}
	public Double getPricePd() {
		return pricePd;
	}
	public void setPricePd(Double pricePd) {
		this.pricePd = pricePd;
	}
	public String getDtPois() {
		return dtPois;
	}
	public void setDtPois(String dtPois) {
		this.dtPois = dtPois;
	}
	public String getFormApp() {
		return formApp;
	}
	public void setFormApp(String formApp) {
		this.formApp = formApp;
	}

	public String getPurpose() {
		return purpose;
	}

	public void setPurpose(String purpose) {
		this.purpose = purpose;
	}

	public String getNoteDise() {
		return noteDise;
	}

	public void setNoteDise(String noteDise) {
		this.noteDise = noteDise;
	}

	public String getRisNotice() {
		return risNotice;
	}

	public void setRisNotice(String risNotice) {
		this.risNotice = risNotice;
	}

	public String getDtExcardtype() {
		return dtExcardtype;
	}

	public void setDtExcardtype(String dtExcardtype) {
		this.dtExcardtype = dtExcardtype;
	}

	public String getEuMuputypeOp() {
		return euMuputypeOp;
	}

	public void setEuMuputypeOp(String euMuputypeOp) {
		this.euMuputypeOp = euMuputypeOp;
	}

	public String getIsCheck() {
		return isCheck;
	}

	public void setIsCheck(String isCheck) {
		this.isCheck = isCheck;
	}

	public String getHpRate() {
		return hpRate;
	}

	public void setHpRate(String hpRate) {
		this.hpRate = hpRate;
	}

	public int getgNum() {
		return gNum;
	}

	public void setgNum(int gNum) {
		this.gNum = gNum;
	}

	public String getFlagChangQuan() {
		return flagChangQuan;
	}

	public void setFlagChangQuan(String flagChangQuan) {
		this.flagChangQuan = flagChangQuan;
	}

	public Double getAmountBa() {
		return amountBa;
	}

	public void setAmountBa(Double amountBa) {
		this.amountBa = amountBa;
	}

	public String getFlagRescue() {
		return flagRescue;
	}

	public void setFlagRescue(String flagRescue) {
		this.flagRescue = flagRescue;
	}

	public String getFlagZero() {
		return flagZero;
	}

	public void setFlagZero(String flagZero) {
		this.flagZero = flagZero;
	}

	public Double getQuanMax() {
		return quanMax;
	}

	public void setQuanMax(Double quanMax) {
		this.quanMax = quanMax;
	}

	public Double getZeroMax() {
		return zeroMax;
	}

	public void setZeroMax(Double zeroMax) {
		this.zeroMax = zeroMax;
	}

	public String getEuUsecate() {
		return euUsecate;
	}

	public void setEuUsecate(String euUsecate) {
		this.euUsecate = euUsecate;
	}

	public List<OrdBdPdAttVo> getBlPdItems() {
		return blPdItems;
	}

	public void setBlPdItems(List<OrdBdPdAttVo> blPdItems) {
		this.blPdItems = blPdItems;
	}

	public String getNotePres() {
		return notePres;
	}

	public void setNotePres(String notePres) {
		this.notePres = notePres;
	}

	public Double getBpspackSize() {
		return bpsPackSize;
	}

	public void setBpspackSize(Double bpsPackSize) {
		this.bpsPackSize = bpsPackSize;
	}
}
