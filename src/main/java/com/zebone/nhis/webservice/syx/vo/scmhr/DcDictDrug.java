package com.zebone.nhis.webservice.syx.vo.scmhr;

import java.math.BigDecimal;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "DC_DICT_DRUG")
public class DcDictDrug {
	@XmlElement(name="PK_DICT_DRUG")
	private String pKDictDrug;
	@XmlElement(name="CODE_DRUG")
    private String codeDrug;
	@XmlElement(name="CODE_CUSTOM")
    private String codeCustom;
	@XmlElement(name="CODE_COUNTRY")
    private String codeCountry;
	@XmlElement(name="NAME_DRUG")
    private String nameDrug;
	@XmlElement(name="NAME_GOODS")
    private String nameGoods;
	@XmlElement(name="SPECIFICATIONS")
    private String specifications;
	@XmlElement(name="RETAIL_PRICE")
    private BigDecimal retailPrice;
	@XmlElement(name="ZERO_SPREAD")
    private BigDecimal zeroSpread;
	@XmlElement(name="PACKING_UNIT")
    private String packingUnit;
	@XmlElement(name="PACKING_QTY")
    private BigDecimal packingQty;
	@XmlElement(name="MIN_UNIT")
    private String minUnit;
	@XmlElement(name="FIRST_DOSE")
    private BigDecimal firstDose;
	@XmlElement(name="FIRST_DOSE_UNIT")
    private String firstDoseUnit;
	@XmlElement(name="SECOND_DOSE")
    private BigDecimal secondDose;
	@XmlElement(name="SECOND_DOSE_UNIT")
    private String secondDoseUnit;
	@XmlElement(name="SPLIT_OUT_PATIENT")
    private String splitOutPatient;
	@XmlElement(name="SPLIT_TEMPORARY")
    private String splitTemporary;
	@XmlElement(name="SPLIT_LONG")
    private String splitLong;
	@XmlElement(name="DRUG_NATURE")
    private String drugNature;
	@XmlElement(name="CODE_SYSTYPE")
    private String codeSystype;
	@XmlElement(name="NAMA_SYSTYPE")
    private String nameSystype;
	@XmlElement(name="CODE_DRUGTYPE")
    private String codeDrugtype;
	@XmlElement(name="NAME_DRUGTYPE")
    private String nameDrugtype;
	@XmlElement(name="DOSAGE_FORM")
    private String dosageForm;
	@XmlElement(name="PRICETYPE")
    private String pricetype;
	@XmlElement(name="GRADE_DRUG")
    private String gradeDrug;
	@XmlElement(name="TRADE_PRICE")
    private BigDecimal tradePrice;
	@XmlElement(name="PURCHASING_PRICE")
    private BigDecimal purchasingPrice;
	@XmlElement(name="MAX_RETAIL_PRICE")
    private BigDecimal maxRetailPrice;
	@XmlElement(name="FLAG_SELF")
    private String flagSelf;
	@XmlElement(name="FLAG_ALLERGY")
    private String flagAllergy;
	@XmlElement(name="FLAG_GMP")
    private String flagGmp;
	@XmlElement(name="FLAG_OTC")
    private String flagOtc;
	@XmlElement(name="FLAG_SHOW")
    private String flagShow;
	@XmlElement(name="USAGE_METHOD")
    private String usageMethod;
	@XmlElement(name="PRIMARY_DOSAGE")
    private BigDecimal primaryDosage;
	@XmlElement(name="PRIMARY_DOSAGE_UNIT")
    private String primaryDosageUnit;
	@XmlElement(name="FREQUENCY")
    private String frequency;
	@XmlElement(name="MATTERS_NEEDING_ATTENTION")
    private String mattersNeedingAttention;
	@XmlElement(name="ACTIVE_INGREDIENT")
    private String activeIngredient;
	@XmlElement(name="CCTJ")
    private String cctj;
	@XmlElement(name="ZXBZ")
    private String zxbz;
	@XmlElement(name="zxbz")
    private String yjylzy;
	@XmlElement(name="EJYLZY")
    private String ejylzy;
	@XmlElement(name="SJYLZY")
    private String sjylzy;
	@XmlElement(name="ZBY")
    private String zby;
	@XmlElement(name="GHGS")
    private String ghgs;
	@XmlElement(name="SCCJ")
    private String sccj;
	
	@XmlElement(name="PWXX")
    private String pwxx;
	@XmlElement(name="ZCSB")
    private String zcsb;
	@XmlElement(name="CD")
    private String cd;
	@XmlElement(name="TXM")
    private String txm;
	@XmlElement(name="XM")
    private String xm;
	@XmlElement(name="BM")
    private String bm;
	@XmlElement(name="YWSPM")
    private String ywspm;
	@XmlElement(name="YWBM")
    private String ywbm;
	
	@XmlElement(name="YWTYM")	
    private String ywtym;
	
	@XmlElement(name="BZ")
    private String bz;
	@XmlElement(name="PYM")
    private String pym;
	@XmlElement(name="WBM")
    private String wbm;
	@XmlElement(name="TYMPYM")
    private String tympym;
	@XmlElement(name="TYMWBM")
    private String tymwbm;
	@XmlElement(name="TYMZDYM")
    private String tymzdym;
	@XmlElement(name="XMPYM")
    private String xmpym;
	@XmlElement(name="XMWBM")
    private String xmwbm;
	@XmlElement(name="XMZDYM")
    private String xmzdym;
	@XmlElement(name="BMPYM")
    private String bmpym;
	@XmlElement(name="BMWBM")
    private String bmwbm;
	@XmlElement(name="BMZDYM")
    private String bmzdym;
	@XmlElement(name="SFBYJJQ")
    private String sfbyjjq;
	@XmlElement(name="YBXZBZ")
    private String ybxzbz;
	@XmlElement(name="SFJK")
    private String sfjk;
	@XmlElement(name="QKYYBZ")
    private String qkyybz;
	@XmlElement(name="KSUDDDZ")
    private String kusdddz;
	
	@XmlElement(name="FLAG_EFF")	
    private String flagEfe;
	@XmlElement(name="CREATE_TIME")
    private String createTime;
	@XmlElement(name="DICT_DRUG_SOURCE")
    private String dictDrugSource;
	@XmlElement(name="CODE_GRADE_DRUG")
    private String codeGradeDrug;
	@XmlElement(name="CODE_DRUG_NATURE")
    private String codeDrugNature;
	@XmlElement(name="FLAG_JY")
    private String flagJy;
	@XmlElement(name="EDIT_TIME")
    private String editTime;
	@XmlElement(name="MINSPECS")
    private String minspecs;
	@XmlElement(name="MAXUNIT")
    private String maxunit;
	@XmlElement(name="DOSAGE")
    private BigDecimal dosage;
	@XmlElement(name="CONVERTION1")
    private String convertion1;
	@XmlElement(name="CONVERTION2")
    private String convertion2;
	@XmlElement(name="STORAGETYPE")
    private String storagetype;
	@XmlElement(name="ALLOWIND")
    private String allowind;
	public String getpKDictDrug() {
		return pKDictDrug;
	}
	public void setpKDictDrug(String pKDictDrug) {
		this.pKDictDrug = pKDictDrug;
	}
	public String getCodeDrug() {
		return codeDrug;
	}
	public void setCodeDrug(String codeDrug) {
		this.codeDrug = codeDrug;
	}
	public String getCodeCustom() {
		return codeCustom;
	}
	public void setCodeCustom(String codeCustom) {
		this.codeCustom = codeCustom;
	}
	public String getCodeCountry() {
		return codeCountry;
	}
	public void setCodeCountry(String codeCountry) {
		this.codeCountry = codeCountry;
	}
	public String getNameDrug() {
		return nameDrug;
	}
	public void setNameDrug(String nameDrug) {
		this.nameDrug = nameDrug;
	}
	public String getNameGoods() {
		return nameGoods;
	}
	public void setNameGoods(String nameGoods) {
		this.nameGoods = nameGoods;
	}
	public String getSpecifications() {
		return specifications;
	}
	public void setSpecifications(String specifications) {
		this.specifications = specifications;
	}
	public BigDecimal getRetailPrice() {
		return retailPrice;
	}
	public void setRetailPrice(BigDecimal retailPrice) {
		this.retailPrice = retailPrice;
	}
	public BigDecimal getZeroSpread() {
		return zeroSpread;
	}
	public void setZeroSpread(BigDecimal zeroSpread) {
		this.zeroSpread = zeroSpread;
	}
	public String getPackingUnit() {
		return packingUnit;
	}
	public void setPackingUnit(String packingUnit) {
		this.packingUnit = packingUnit;
	}
	public BigDecimal getPackingQty() {
		return packingQty;
	}
	public void setPackingQty(BigDecimal packingQty) {
		this.packingQty = packingQty;
	}
	public String getMinUnit() {
		return minUnit;
	}
	public void setMinUnit(String minUnit) {
		this.minUnit = minUnit;
	}
	public BigDecimal getFirstDose() {
		return firstDose;
	}
	public void setFirstDose(BigDecimal firstDose) {
		this.firstDose = firstDose;
	}
	public String getFirstDoseUnit() {
		return firstDoseUnit;
	}
	public void setFirstDoseUnit(String firstDoseUnit) {
		this.firstDoseUnit = firstDoseUnit;
	}
	public BigDecimal getSecondDose() {
		return secondDose;
	}
	public void setSecondDose(BigDecimal secondDose) {
		this.secondDose = secondDose;
	}
	public String getSecondDoseUnit() {
		return secondDoseUnit;
	}
	public void setSecondDoseUnit(String secondDoseUnit) {
		this.secondDoseUnit = secondDoseUnit;
	}
	public String getSplitOutPatient() {
		return splitOutPatient;
	}
	public void setSplitOutPatient(String splitOutPatient) {
		this.splitOutPatient = splitOutPatient;
	}
	public String getSplitTemporary() {
		return splitTemporary;
	}
	public void setSplitTemporary(String splitTemporary) {
		this.splitTemporary = splitTemporary;
	}
	public String getSplitLong() {
		return splitLong;
	}
	public void setSplitLong(String splitLong) {
		this.splitLong = splitLong;
	}
	public String getDrugNature() {
		return drugNature;
	}
	public void setDrugNature(String drugNature) {
		this.drugNature = drugNature;
	}
	public String getCodeSystype() {
		return codeSystype;
	}
	public void setCodeSystype(String codeSystype) {
		this.codeSystype = codeSystype;
	}
	public String getNameSystype() {
		return nameSystype;
	}
	public void setNameSystype(String nameSystype) {
		this.nameSystype = nameSystype;
	}
	public String getCodeDrugtype() {
		return codeDrugtype;
	}
	public void setCodeDrugtype(String codeDrugtype) {
		this.codeDrugtype = codeDrugtype;
	}
	public String getNameDrugtype() {
		return nameDrugtype;
	}
	public void setNameDrugtype(String nameDrugtype) {
		this.nameDrugtype = nameDrugtype;
	}
	public String getDosageForm() {
		return dosageForm;
	}
	public void setDosageForm(String dosageForm) {
		this.dosageForm = dosageForm;
	}
	public String getPricetype() {
		return pricetype;
	}
	public void setPricetype(String pricetype) {
		this.pricetype = pricetype;
	}
	public String getGradeDrug() {
		return gradeDrug;
	}
	public void setGradeDrug(String gradeDrug) {
		this.gradeDrug = gradeDrug;
	}
	public BigDecimal getTradePrice() {
		return tradePrice;
	}
	public void setTradePrice(BigDecimal tradePrice) {
		this.tradePrice = tradePrice;
	}
	public BigDecimal getPurchasingPrice() {
		return purchasingPrice;
	}
	public void setPurchasingPrice(BigDecimal purchasingPrice) {
		this.purchasingPrice = purchasingPrice;
	}
	public BigDecimal getMaxRetailPrice() {
		return maxRetailPrice;
	}
	public void setMaxRetailPrice(BigDecimal maxRetailPrice) {
		this.maxRetailPrice = maxRetailPrice;
	}
	public String getFlagSelf() {
		return flagSelf;
	}
	public void setFlagSelf(String flagSelf) {
		this.flagSelf = flagSelf;
	}
	public String getFlagAllergy() {
		return flagAllergy;
	}
	public void setFlagAllergy(String flagAllergy) {
		this.flagAllergy = flagAllergy;
	}
	public String getFlagGmp() {
		return flagGmp;
	}
	public void setFlagGmp(String flagGmp) {
		this.flagGmp = flagGmp;
	}
	public String getFlagOtc() {
		return flagOtc;
	}
	public void setFlagOtc(String flagOtc) {
		this.flagOtc = flagOtc;
	}
	public String getFlagShow() {
		return flagShow;
	}
	public void setFlagShow(String flagShow) {
		this.flagShow = flagShow;
	}
	public String getUsageMethod() {
		return usageMethod;
	}
	public void setUsageMethod(String usageMethod) {
		this.usageMethod = usageMethod;
	}
	public BigDecimal getPrimaryDosage() {
		return primaryDosage;
	}
	public void setPrimaryDosage(BigDecimal primaryDosage) {
		this.primaryDosage = primaryDosage;
	}
	public String getPrimaryDosageUnit() {
		return primaryDosageUnit;
	}
	public void setPrimaryDosageUnit(String primaryDosageUnit) {
		this.primaryDosageUnit = primaryDosageUnit;
	}
	public String getFrequency() {
		return frequency;
	}
	public void setFrequency(String frequency) {
		this.frequency = frequency;
	}
	public String getMattersNeedingAttention() {
		return mattersNeedingAttention;
	}
	public void setMattersNeedingAttention(String mattersNeedingAttention) {
		this.mattersNeedingAttention = mattersNeedingAttention;
	}
	public String getActiveIngredient() {
		return activeIngredient;
	}
	public void setActiveIngredient(String activeIngredient) {
		this.activeIngredient = activeIngredient;
	}
	public String getCctj() {
		return cctj;
	}
	public void setCctj(String cctj) {
		this.cctj = cctj;
	}
	public String getZxbz() {
		return zxbz;
	}
	public void setZxbz(String zxbz) {
		this.zxbz = zxbz;
	}
	public String getYjylzy() {
		return yjylzy;
	}
	public void setYjylzy(String yjylzy) {
		this.yjylzy = yjylzy;
	}
	public String getEjylzy() {
		return ejylzy;
	}
	public void setEjylzy(String ejylzy) {
		this.ejylzy = ejylzy;
	}
	public String getSjylzy() {
		return sjylzy;
	}
	public void setSjylzy(String sjylzy) {
		this.sjylzy = sjylzy;
	}
	public String getZby() {
		return zby;
	}
	public void setZby(String zby) {
		this.zby = zby;
	}
	public String getGhgs() {
		return ghgs;
	}
	public void setGhgs(String ghgs) {
		this.ghgs = ghgs;
	}
	public String getSccj() {
		return sccj;
	}
	public void setSccj(String sccj) {
		this.sccj = sccj;
	}
	public String getPwxx() {
		return pwxx;
	}
	public void setPwxx(String pwxx) {
		this.pwxx = pwxx;
	}
	public String getZcsb() {
		return zcsb;
	}
	public void setZcsb(String zcsb) {
		this.zcsb = zcsb;
	}
	public String getCd() {
		return cd;
	}
	public void setCd(String cd) {
		this.cd = cd;
	}
	public String getTxm() {
		return txm;
	}
	public void setTxm(String txm) {
		this.txm = txm;
	}
	public String getXm() {
		return xm;
	}
	public void setXm(String xm) {
		this.xm = xm;
	}
	public String getBm() {
		return bm;
	}
	public void setBm(String bm) {
		this.bm = bm;
	}
	public String getYwspm() {
		return ywspm;
	}
	public void setYwspm(String ywspm) {
		this.ywspm = ywspm;
	}
	public String getYwbm() {
		return ywbm;
	}
	public void setYwbm(String ywbm) {
		this.ywbm = ywbm;
	}
	public String getYwtym() {
		return ywtym;
	}
	public void setYwtym(String ywtym) {
		this.ywtym = ywtym;
	}
	public String getBz() {
		return bz;
	}
	public void setBz(String bz) {
		this.bz = bz;
	}
	public String getPym() {
		return pym;
	}
	public void setPym(String pym) {
		this.pym = pym;
	}
	public String getWbm() {
		return wbm;
	}
	public void setWbm(String wbm) {
		this.wbm = wbm;
	}
	public String getTympym() {
		return tympym;
	}
	public void setTympym(String tympym) {
		this.tympym = tympym;
	}
	public String getTymwbm() {
		return tymwbm;
	}
	public void setTymwbm(String tymwbm) {
		this.tymwbm = tymwbm;
	}
	public String getTymzdym() {
		return tymzdym;
	}
	public void setTymzdym(String tymzdym) {
		this.tymzdym = tymzdym;
	}
	public String getXmpym() {
		return xmpym;
	}
	public void setXmpym(String xmpym) {
		this.xmpym = xmpym;
	}
	public String getXmwbm() {
		return xmwbm;
	}
	public void setXmwbm(String xmwbm) {
		this.xmwbm = xmwbm;
	}
	public String getXmzdym() {
		return xmzdym;
	}
	public void setXmzdym(String xmzdym) {
		this.xmzdym = xmzdym;
	}
	public String getBmpym() {
		return bmpym;
	}
	public void setBmpym(String bmpym) {
		this.bmpym = bmpym;
	}
	public String getBmwbm() {
		return bmwbm;
	}
	public void setBmwbm(String bmwbm) {
		this.bmwbm = bmwbm;
	}
	public String getBmzdym() {
		return bmzdym;
	}
	public void setBmzdym(String bmzdym) {
		this.bmzdym = bmzdym;
	}
	public String getSfbyjjq() {
		return sfbyjjq;
	}
	public void setSfbyjjq(String sfbyjjq) {
		this.sfbyjjq = sfbyjjq;
	}
	public String getYbxzbz() {
		return ybxzbz;
	}
	public void setYbxzbz(String ybxzbz) {
		this.ybxzbz = ybxzbz;
	}
	public String getSfjk() {
		return sfjk;
	}
	public void setSfjk(String sfjk) {
		this.sfjk = sfjk;
	}
	public String getQkyybz() {
		return qkyybz;
	}
	public void setQkyybz(String qkyybz) {
		this.qkyybz = qkyybz;
	}
	public String getKusdddz() {
		return kusdddz;
	}
	public void setKusdddz(String kusdddz) {
		this.kusdddz = kusdddz;
	}
	public String getFlagEfe() {
		return flagEfe;
	}
	public void setFlagEfe(String flagEfe) {
		this.flagEfe = flagEfe;
	}
	public String getCreateTime() {
		return createTime;
	}
	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}
	public String getDictDrugSource() {
		return dictDrugSource;
	}
	public void setDictDrugSource(String dictDrugSource) {
		this.dictDrugSource = dictDrugSource;
	}
	public String getCodeGradeDrug() {
		return codeGradeDrug;
	}
	public void setCodeGradeDrug(String codeGradeDrug) {
		this.codeGradeDrug = codeGradeDrug;
	}
	public String getCodeDrugNature() {
		return codeDrugNature;
	}
	public void setCodeDrugNature(String codeDrugNature) {
		this.codeDrugNature = codeDrugNature;
	}
	public String getFlagJy() {
		return flagJy;
	}
	public void setFlagJy(String flagJy) {
		this.flagJy = flagJy;
	}
	public String getEditTime() {
		return editTime;
	}
	public void setEditTime(String editTime) {
		this.editTime = editTime;
	}
	public String getMinspecs() {
		return minspecs;
	}
	public void setMinspecs(String minspecs) {
		this.minspecs = minspecs;
	}
	public String getMaxunit() {
		return maxunit;
	}
	public void setMaxunit(String maxunit) {
		this.maxunit = maxunit;
	}
	public BigDecimal getDosage() {
		return dosage;
	}
	public void setDosage(BigDecimal dosage) {
		this.dosage = dosage;
	}
	public String getConvertion1() {
		return convertion1;
	}
	public void setConvertion1(String convertion1) {
		this.convertion1 = convertion1;
	}
	public String getConvertion2() {
		return convertion2;
	}
	public void setConvertion2(String convertion2) {
		this.convertion2 = convertion2;
	}
	public String getStoragetype() {
		return storagetype;
	}
	public void setStoragetype(String storagetype) {
		this.storagetype = storagetype;
	}
	public String getAllowind() {
		return allowind;
	}
	public void setAllowind(String allowind) {
		this.allowind = allowind;
	}
	
	
	

}
