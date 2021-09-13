package com.zebone.nhis.ma.pub.platform.send.impl.yh.vo.BdPd;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * BD_PD
 * @author 
 */
public class BdPd implements Serializable {
    private String pkPd;

    private String code;

    private String name;

    private String spec;

    private String shortName;

    private String barcode;

    private String spcode;

    private BigDecimal concent;

    private BigDecimal weight;

    private String pkUnitWt;

    private BigDecimal vol;

    private String pkUnitVol;

    private String pkUnitMin;

    private Integer packSize;

    private String pkUnitPack;

    private String euMuputype;

    private String euPdtype;

    private String euDrugtype;

    private String nameChem;

    private String pkFactory;

    private String apprNo;

    private String euPdprice;

    private String euPap;

    private BigDecimal amtPap;

    private BigDecimal papRate;

    private String dtAbrd;

    private String dtMade;

    private String dtDosage;

    private String dtPharm;

    private String dtPois;

    private String dtAnti;

    private String flagPrecious;

    private String euUsecate;

    private String dtStoretype;

    private String dtBase;

    private String flagRm;

    private String flagReag;

    private String flagVacc;

    private String flagSt;

    private String flagGmp;

    private String flagTpn;

    private String flagPed;

    private String note;

    private BigDecimal dosageDef;

    private String pkUnitDef;

    private String codeSupply;

    private String codeFreq;

    private String dtChcate;

    private String pkItemcate;

    private String pkOrdtype;

    private String creator;

    private Date createTime;

    private String modifier;

    private String delFlag;

    private String pkOrg;

    private BigDecimal price;

    private Integer validCnt;

    private String euValidUnit;

    private String flagStop;

    private String euSource;

    private Date ts;

    private String pkPdind;

    private String regNo;

    private Date dateValidReg;

    private String euStockmode;

    private String pkPdcate;

    private String codeCostitem;

    private String pkItem;

    private String flagSingle;

    private String flagImp;

    private String flagUse;

    private String dtUsagenote;

    private String pkPdgn;

    private Integer packSizeMax;

    private String dtPdtype;

    private String euHerbtype;

    private String flagChrt;

    private String dtInjtype;

    private String euHptype;

    private String euHpratio;

    private String euRisk;

    private String codeHp;

    private String codeStd;

    private Integer ageMin;

    private Integer ageMax;

    private String euSex;

    private BigDecimal quotaDos;

    private String nameGen;

    private String euLabeltype;

    private static final long serialVersionUID = 1L;

    public String getPkPd() {
        return pkPd;
    }

    public void setPkPd(String pkPd) {
        this.pkPd = pkPd;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSpec() {
        return spec;
    }

    public void setSpec(String spec) {
        this.spec = spec;
    }

    public String getShortName() {
        return shortName;
    }

    public void setShortName(String shortName) {
        this.shortName = shortName;
    }

    public String getBarcode() {
        return barcode;
    }

    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }

    public String getSpcode() {
        return spcode;
    }

    public void setSpcode(String spcode) {
        this.spcode = spcode;
    }

    public BigDecimal getConcent() {
        return concent;
    }

    public void setConcent(BigDecimal concent) {
        this.concent = concent;
    }

    public BigDecimal getWeight() {
        return weight;
    }

    public void setWeight(BigDecimal weight) {
        this.weight = weight;
    }

    public String getPkUnitWt() {
        return pkUnitWt;
    }

    public void setPkUnitWt(String pkUnitWt) {
        this.pkUnitWt = pkUnitWt;
    }

    public BigDecimal getVol() {
        return vol;
    }

    public void setVol(BigDecimal vol) {
        this.vol = vol;
    }

    public String getPkUnitVol() {
        return pkUnitVol;
    }

    public void setPkUnitVol(String pkUnitVol) {
        this.pkUnitVol = pkUnitVol;
    }

    public String getPkUnitMin() {
        return pkUnitMin;
    }

    public void setPkUnitMin(String pkUnitMin) {
        this.pkUnitMin = pkUnitMin;
    }

    public Integer getPackSize() {
        return packSize;
    }

    public void setPackSize(Integer packSize) {
        this.packSize = packSize;
    }

    public String getPkUnitPack() {
        return pkUnitPack;
    }

    public void setPkUnitPack(String pkUnitPack) {
        this.pkUnitPack = pkUnitPack;
    }

    public String getEuMuputype() {
        return euMuputype;
    }

    public void setEuMuputype(String euMuputype) {
        this.euMuputype = euMuputype;
    }

    public String getEuPdtype() {
        return euPdtype;
    }

    public void setEuPdtype(String euPdtype) {
        this.euPdtype = euPdtype;
    }

    public String getEuDrugtype() {
        return euDrugtype;
    }

    public void setEuDrugtype(String euDrugtype) {
        this.euDrugtype = euDrugtype;
    }

    public String getNameChem() {
        return nameChem;
    }

    public void setNameChem(String nameChem) {
        this.nameChem = nameChem;
    }

    public String getPkFactory() {
        return pkFactory;
    }

    public void setPkFactory(String pkFactory) {
        this.pkFactory = pkFactory;
    }

    public String getApprNo() {
        return apprNo;
    }

    public void setApprNo(String apprNo) {
        this.apprNo = apprNo;
    }

    public String getEuPdprice() {
        return euPdprice;
    }

    public void setEuPdprice(String euPdprice) {
        this.euPdprice = euPdprice;
    }

    public String getEuPap() {
        return euPap;
    }

    public void setEuPap(String euPap) {
        this.euPap = euPap;
    }

    public BigDecimal getAmtPap() {
        return amtPap;
    }

    public void setAmtPap(BigDecimal amtPap) {
        this.amtPap = amtPap;
    }

    public BigDecimal getPapRate() {
        return papRate;
    }

    public void setPapRate(BigDecimal papRate) {
        this.papRate = papRate;
    }

    public String getDtAbrd() {
        return dtAbrd;
    }

    public void setDtAbrd(String dtAbrd) {
        this.dtAbrd = dtAbrd;
    }

    public String getDtMade() {
        return dtMade;
    }

    public void setDtMade(String dtMade) {
        this.dtMade = dtMade;
    }

    public String getDtDosage() {
        return dtDosage;
    }

    public void setDtDosage(String dtDosage) {
        this.dtDosage = dtDosage;
    }

    public String getDtPharm() {
        return dtPharm;
    }

    public void setDtPharm(String dtPharm) {
        this.dtPharm = dtPharm;
    }

    public String getDtPois() {
        return dtPois;
    }

    public void setDtPois(String dtPois) {
        this.dtPois = dtPois;
    }

    public String getDtAnti() {
        return dtAnti;
    }

    public void setDtAnti(String dtAnti) {
        this.dtAnti = dtAnti;
    }

    public String getFlagPrecious() {
        return flagPrecious;
    }

    public void setFlagPrecious(String flagPrecious) {
        this.flagPrecious = flagPrecious;
    }

    public String getEuUsecate() {
        return euUsecate;
    }

    public void setEuUsecate(String euUsecate) {
        this.euUsecate = euUsecate;
    }

    public String getDtStoretype() {
        return dtStoretype;
    }

    public void setDtStoretype(String dtStoretype) {
        this.dtStoretype = dtStoretype;
    }

    public String getDtBase() {
        return dtBase;
    }

    public void setDtBase(String dtBase) {
        this.dtBase = dtBase;
    }

    public String getFlagRm() {
        return flagRm;
    }

    public void setFlagRm(String flagRm) {
        this.flagRm = flagRm;
    }

    public String getFlagReag() {
        return flagReag;
    }

    public void setFlagReag(String flagReag) {
        this.flagReag = flagReag;
    }

    public String getFlagVacc() {
        return flagVacc;
    }

    public void setFlagVacc(String flagVacc) {
        this.flagVacc = flagVacc;
    }

    public String getFlagSt() {
        return flagSt;
    }

    public void setFlagSt(String flagSt) {
        this.flagSt = flagSt;
    }

    public String getFlagGmp() {
        return flagGmp;
    }

    public void setFlagGmp(String flagGmp) {
        this.flagGmp = flagGmp;
    }

    public String getFlagTpn() {
        return flagTpn;
    }

    public void setFlagTpn(String flagTpn) {
        this.flagTpn = flagTpn;
    }

    public String getFlagPed() {
        return flagPed;
    }

    public void setFlagPed(String flagPed) {
        this.flagPed = flagPed;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public BigDecimal getDosageDef() {
        return dosageDef;
    }

    public void setDosageDef(BigDecimal dosageDef) {
        this.dosageDef = dosageDef;
    }

    public String getPkUnitDef() {
        return pkUnitDef;
    }

    public void setPkUnitDef(String pkUnitDef) {
        this.pkUnitDef = pkUnitDef;
    }

    public String getCodeSupply() {
        return codeSupply;
    }

    public void setCodeSupply(String codeSupply) {
        this.codeSupply = codeSupply;
    }

    public String getCodeFreq() {
        return codeFreq;
    }

    public void setCodeFreq(String codeFreq) {
        this.codeFreq = codeFreq;
    }

    public String getDtChcate() {
        return dtChcate;
    }

    public void setDtChcate(String dtChcate) {
        this.dtChcate = dtChcate;
    }

    public String getPkItemcate() {
        return pkItemcate;
    }

    public void setPkItemcate(String pkItemcate) {
        this.pkItemcate = pkItemcate;
    }

    public String getPkOrdtype() {
        return pkOrdtype;
    }

    public void setPkOrdtype(String pkOrdtype) {
        this.pkOrdtype = pkOrdtype;
    }

    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getModifier() {
        return modifier;
    }

    public void setModifier(String modifier) {
        this.modifier = modifier;
    }

    public String getDelFlag() {
        return delFlag;
    }

    public void setDelFlag(String delFlag) {
        this.delFlag = delFlag;
    }

    public String getPkOrg() {
        return pkOrg;
    }

    public void setPkOrg(String pkOrg) {
        this.pkOrg = pkOrg;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public Integer getValidCnt() {
        return validCnt;
    }

    public void setValidCnt(Integer validCnt) {
        this.validCnt = validCnt;
    }

    public String getEuValidUnit() {
        return euValidUnit;
    }

    public void setEuValidUnit(String euValidUnit) {
        this.euValidUnit = euValidUnit;
    }

    public String getFlagStop() {
        return flagStop;
    }

    public void setFlagStop(String flagStop) {
        this.flagStop = flagStop;
    }

    public String getEuSource() {
        return euSource;
    }

    public void setEuSource(String euSource) {
        this.euSource = euSource;
    }

    public Date getTs() {
        return ts;
    }

    public void setTs(Date ts) {
        this.ts = ts;
    }

    public String getPkPdind() {
        return pkPdind;
    }

    public void setPkPdind(String pkPdind) {
        this.pkPdind = pkPdind;
    }

    public String getRegNo() {
        return regNo;
    }

    public void setRegNo(String regNo) {
        this.regNo = regNo;
    }

    public Date getDateValidReg() {
        return dateValidReg;
    }

    public void setDateValidReg(Date dateValidReg) {
        this.dateValidReg = dateValidReg;
    }

    public String getEuStockmode() {
        return euStockmode;
    }

    public void setEuStockmode(String euStockmode) {
        this.euStockmode = euStockmode;
    }

    public String getPkPdcate() {
        return pkPdcate;
    }

    public void setPkPdcate(String pkPdcate) {
        this.pkPdcate = pkPdcate;
    }

    public String getCodeCostitem() {
        return codeCostitem;
    }

    public void setCodeCostitem(String codeCostitem) {
        this.codeCostitem = codeCostitem;
    }

    public String getPkItem() {
        return pkItem;
    }

    public void setPkItem(String pkItem) {
        this.pkItem = pkItem;
    }

    public String getFlagSingle() {
        return flagSingle;
    }

    public void setFlagSingle(String flagSingle) {
        this.flagSingle = flagSingle;
    }

    public String getFlagImp() {
        return flagImp;
    }

    public void setFlagImp(String flagImp) {
        this.flagImp = flagImp;
    }

    public String getFlagUse() {
        return flagUse;
    }

    public void setFlagUse(String flagUse) {
        this.flagUse = flagUse;
    }

    public String getDtUsagenote() {
        return dtUsagenote;
    }

    public void setDtUsagenote(String dtUsagenote) {
        this.dtUsagenote = dtUsagenote;
    }

    public String getPkPdgn() {
        return pkPdgn;
    }

    public void setPkPdgn(String pkPdgn) {
        this.pkPdgn = pkPdgn;
    }

    public Integer getPackSizeMax() {
        return packSizeMax;
    }

    public void setPackSizeMax(Integer packSizeMax) {
        this.packSizeMax = packSizeMax;
    }

    public String getDtPdtype() {
        return dtPdtype;
    }

    public void setDtPdtype(String dtPdtype) {
        this.dtPdtype = dtPdtype;
    }

    public String getEuHerbtype() {
        return euHerbtype;
    }

    public void setEuHerbtype(String euHerbtype) {
        this.euHerbtype = euHerbtype;
    }

    public String getFlagChrt() {
        return flagChrt;
    }

    public void setFlagChrt(String flagChrt) {
        this.flagChrt = flagChrt;
    }

    public String getDtInjtype() {
        return dtInjtype;
    }

    public void setDtInjtype(String dtInjtype) {
        this.dtInjtype = dtInjtype;
    }

    public String getEuHptype() {
        return euHptype;
    }

    public void setEuHptype(String euHptype) {
        this.euHptype = euHptype;
    }

    public String getEuHpratio() {
        return euHpratio;
    }

    public void setEuHpratio(String euHpratio) {
        this.euHpratio = euHpratio;
    }

    public String getEuRisk() {
        return euRisk;
    }

    public void setEuRisk(String euRisk) {
        this.euRisk = euRisk;
    }

    public String getCodeHp() {
        return codeHp;
    }

    public void setCodeHp(String codeHp) {
        this.codeHp = codeHp;
    }

    public String getCodeStd() {
        return codeStd;
    }

    public void setCodeStd(String codeStd) {
        this.codeStd = codeStd;
    }

    public Integer getAgeMin() {
        return ageMin;
    }

    public void setAgeMin(Integer ageMin) {
        this.ageMin = ageMin;
    }

    public Integer getAgeMax() {
        return ageMax;
    }

    public void setAgeMax(Integer ageMax) {
        this.ageMax = ageMax;
    }

    public String getEuSex() {
        return euSex;
    }

    public void setEuSex(String euSex) {
        this.euSex = euSex;
    }

    public BigDecimal getQuotaDos() {
        return quotaDos;
    }

    public void setQuotaDos(BigDecimal quotaDos) {
        this.quotaDos = quotaDos;
    }

    public String getNameGen() {
        return nameGen;
    }

    public void setNameGen(String nameGen) {
        this.nameGen = nameGen;
    }

    public String getEuLabeltype() {
        return euLabeltype;
    }

    public void setEuLabeltype(String euLabeltype) {
        this.euLabeltype = euLabeltype;
    }

    @Override
    public boolean equals(Object that) {
        if (this == that) {
            return true;
        }
        if (that == null) {
            return false;
        }
        if (getClass() != that.getClass()) {
            return false;
        }
        BdPd other = (BdPd) that;
        return (this.getPkPd() == null ? other.getPkPd() == null : this.getPkPd().equals(other.getPkPd()))
            && (this.getCode() == null ? other.getCode() == null : this.getCode().equals(other.getCode()))
            && (this.getName() == null ? other.getName() == null : this.getName().equals(other.getName()))
            && (this.getSpec() == null ? other.getSpec() == null : this.getSpec().equals(other.getSpec()))
            && (this.getShortName() == null ? other.getShortName() == null : this.getShortName().equals(other.getShortName()))
            && (this.getBarcode() == null ? other.getBarcode() == null : this.getBarcode().equals(other.getBarcode()))
            && (this.getSpcode() == null ? other.getSpcode() == null : this.getSpcode().equals(other.getSpcode()))
            && (this.getConcent() == null ? other.getConcent() == null : this.getConcent().equals(other.getConcent()))
            && (this.getWeight() == null ? other.getWeight() == null : this.getWeight().equals(other.getWeight()))
            && (this.getPkUnitWt() == null ? other.getPkUnitWt() == null : this.getPkUnitWt().equals(other.getPkUnitWt()))
            && (this.getVol() == null ? other.getVol() == null : this.getVol().equals(other.getVol()))
            && (this.getPkUnitVol() == null ? other.getPkUnitVol() == null : this.getPkUnitVol().equals(other.getPkUnitVol()))
            && (this.getPkUnitMin() == null ? other.getPkUnitMin() == null : this.getPkUnitMin().equals(other.getPkUnitMin()))
            && (this.getPackSize() == null ? other.getPackSize() == null : this.getPackSize().equals(other.getPackSize()))
            && (this.getPkUnitPack() == null ? other.getPkUnitPack() == null : this.getPkUnitPack().equals(other.getPkUnitPack()))
            && (this.getEuMuputype() == null ? other.getEuMuputype() == null : this.getEuMuputype().equals(other.getEuMuputype()))
            && (this.getEuPdtype() == null ? other.getEuPdtype() == null : this.getEuPdtype().equals(other.getEuPdtype()))
            && (this.getEuDrugtype() == null ? other.getEuDrugtype() == null : this.getEuDrugtype().equals(other.getEuDrugtype()))
            && (this.getNameChem() == null ? other.getNameChem() == null : this.getNameChem().equals(other.getNameChem()))
            && (this.getPkFactory() == null ? other.getPkFactory() == null : this.getPkFactory().equals(other.getPkFactory()))
            && (this.getApprNo() == null ? other.getApprNo() == null : this.getApprNo().equals(other.getApprNo()))
            && (this.getEuPdprice() == null ? other.getEuPdprice() == null : this.getEuPdprice().equals(other.getEuPdprice()))
            && (this.getEuPap() == null ? other.getEuPap() == null : this.getEuPap().equals(other.getEuPap()))
            && (this.getAmtPap() == null ? other.getAmtPap() == null : this.getAmtPap().equals(other.getAmtPap()))
            && (this.getPapRate() == null ? other.getPapRate() == null : this.getPapRate().equals(other.getPapRate()))
            && (this.getDtAbrd() == null ? other.getDtAbrd() == null : this.getDtAbrd().equals(other.getDtAbrd()))
            && (this.getDtMade() == null ? other.getDtMade() == null : this.getDtMade().equals(other.getDtMade()))
            && (this.getDtDosage() == null ? other.getDtDosage() == null : this.getDtDosage().equals(other.getDtDosage()))
            && (this.getDtPharm() == null ? other.getDtPharm() == null : this.getDtPharm().equals(other.getDtPharm()))
            && (this.getDtPois() == null ? other.getDtPois() == null : this.getDtPois().equals(other.getDtPois()))
            && (this.getDtAnti() == null ? other.getDtAnti() == null : this.getDtAnti().equals(other.getDtAnti()))
            && (this.getFlagPrecious() == null ? other.getFlagPrecious() == null : this.getFlagPrecious().equals(other.getFlagPrecious()))
            && (this.getEuUsecate() == null ? other.getEuUsecate() == null : this.getEuUsecate().equals(other.getEuUsecate()))
            && (this.getDtStoretype() == null ? other.getDtStoretype() == null : this.getDtStoretype().equals(other.getDtStoretype()))
            && (this.getDtBase() == null ? other.getDtBase() == null : this.getDtBase().equals(other.getDtBase()))
            && (this.getFlagRm() == null ? other.getFlagRm() == null : this.getFlagRm().equals(other.getFlagRm()))
            && (this.getFlagReag() == null ? other.getFlagReag() == null : this.getFlagReag().equals(other.getFlagReag()))
            && (this.getFlagVacc() == null ? other.getFlagVacc() == null : this.getFlagVacc().equals(other.getFlagVacc()))
            && (this.getFlagSt() == null ? other.getFlagSt() == null : this.getFlagSt().equals(other.getFlagSt()))
            && (this.getFlagGmp() == null ? other.getFlagGmp() == null : this.getFlagGmp().equals(other.getFlagGmp()))
            && (this.getFlagTpn() == null ? other.getFlagTpn() == null : this.getFlagTpn().equals(other.getFlagTpn()))
            && (this.getFlagPed() == null ? other.getFlagPed() == null : this.getFlagPed().equals(other.getFlagPed()))
            && (this.getNote() == null ? other.getNote() == null : this.getNote().equals(other.getNote()))
            && (this.getDosageDef() == null ? other.getDosageDef() == null : this.getDosageDef().equals(other.getDosageDef()))
            && (this.getPkUnitDef() == null ? other.getPkUnitDef() == null : this.getPkUnitDef().equals(other.getPkUnitDef()))
            && (this.getCodeSupply() == null ? other.getCodeSupply() == null : this.getCodeSupply().equals(other.getCodeSupply()))
            && (this.getCodeFreq() == null ? other.getCodeFreq() == null : this.getCodeFreq().equals(other.getCodeFreq()))
            && (this.getDtChcate() == null ? other.getDtChcate() == null : this.getDtChcate().equals(other.getDtChcate()))
            && (this.getPkItemcate() == null ? other.getPkItemcate() == null : this.getPkItemcate().equals(other.getPkItemcate()))
            && (this.getPkOrdtype() == null ? other.getPkOrdtype() == null : this.getPkOrdtype().equals(other.getPkOrdtype()))
            && (this.getCreator() == null ? other.getCreator() == null : this.getCreator().equals(other.getCreator()))
            && (this.getCreateTime() == null ? other.getCreateTime() == null : this.getCreateTime().equals(other.getCreateTime()))
            && (this.getModifier() == null ? other.getModifier() == null : this.getModifier().equals(other.getModifier()))
            && (this.getDelFlag() == null ? other.getDelFlag() == null : this.getDelFlag().equals(other.getDelFlag()))
            && (this.getPkOrg() == null ? other.getPkOrg() == null : this.getPkOrg().equals(other.getPkOrg()))
            && (this.getPrice() == null ? other.getPrice() == null : this.getPrice().equals(other.getPrice()))
            && (this.getValidCnt() == null ? other.getValidCnt() == null : this.getValidCnt().equals(other.getValidCnt()))
            && (this.getEuValidUnit() == null ? other.getEuValidUnit() == null : this.getEuValidUnit().equals(other.getEuValidUnit()))
            && (this.getFlagStop() == null ? other.getFlagStop() == null : this.getFlagStop().equals(other.getFlagStop()))
            && (this.getEuSource() == null ? other.getEuSource() == null : this.getEuSource().equals(other.getEuSource()))
            && (this.getTs() == null ? other.getTs() == null : this.getTs().equals(other.getTs()))
            && (this.getPkPdind() == null ? other.getPkPdind() == null : this.getPkPdind().equals(other.getPkPdind()))
            && (this.getRegNo() == null ? other.getRegNo() == null : this.getRegNo().equals(other.getRegNo()))
            && (this.getDateValidReg() == null ? other.getDateValidReg() == null : this.getDateValidReg().equals(other.getDateValidReg()))
            && (this.getEuStockmode() == null ? other.getEuStockmode() == null : this.getEuStockmode().equals(other.getEuStockmode()))
            && (this.getPkPdcate() == null ? other.getPkPdcate() == null : this.getPkPdcate().equals(other.getPkPdcate()))
            && (this.getCodeCostitem() == null ? other.getCodeCostitem() == null : this.getCodeCostitem().equals(other.getCodeCostitem()))
            && (this.getPkItem() == null ? other.getPkItem() == null : this.getPkItem().equals(other.getPkItem()))
            && (this.getFlagSingle() == null ? other.getFlagSingle() == null : this.getFlagSingle().equals(other.getFlagSingle()))
            && (this.getFlagImp() == null ? other.getFlagImp() == null : this.getFlagImp().equals(other.getFlagImp()))
            && (this.getFlagUse() == null ? other.getFlagUse() == null : this.getFlagUse().equals(other.getFlagUse()))
            && (this.getDtUsagenote() == null ? other.getDtUsagenote() == null : this.getDtUsagenote().equals(other.getDtUsagenote()))
            && (this.getPkPdgn() == null ? other.getPkPdgn() == null : this.getPkPdgn().equals(other.getPkPdgn()))
            && (this.getPackSizeMax() == null ? other.getPackSizeMax() == null : this.getPackSizeMax().equals(other.getPackSizeMax()))
            && (this.getDtPdtype() == null ? other.getDtPdtype() == null : this.getDtPdtype().equals(other.getDtPdtype()))
            && (this.getEuHerbtype() == null ? other.getEuHerbtype() == null : this.getEuHerbtype().equals(other.getEuHerbtype()))
            && (this.getFlagChrt() == null ? other.getFlagChrt() == null : this.getFlagChrt().equals(other.getFlagChrt()))
            && (this.getDtInjtype() == null ? other.getDtInjtype() == null : this.getDtInjtype().equals(other.getDtInjtype()))
            && (this.getEuHptype() == null ? other.getEuHptype() == null : this.getEuHptype().equals(other.getEuHptype()))
            && (this.getEuHpratio() == null ? other.getEuHpratio() == null : this.getEuHpratio().equals(other.getEuHpratio()))
            && (this.getEuRisk() == null ? other.getEuRisk() == null : this.getEuRisk().equals(other.getEuRisk()))
            && (this.getCodeHp() == null ? other.getCodeHp() == null : this.getCodeHp().equals(other.getCodeHp()))
            && (this.getCodeStd() == null ? other.getCodeStd() == null : this.getCodeStd().equals(other.getCodeStd()))
            && (this.getAgeMin() == null ? other.getAgeMin() == null : this.getAgeMin().equals(other.getAgeMin()))
            && (this.getAgeMax() == null ? other.getAgeMax() == null : this.getAgeMax().equals(other.getAgeMax()))
            && (this.getEuSex() == null ? other.getEuSex() == null : this.getEuSex().equals(other.getEuSex()))
            && (this.getQuotaDos() == null ? other.getQuotaDos() == null : this.getQuotaDos().equals(other.getQuotaDos()))
            && (this.getNameGen() == null ? other.getNameGen() == null : this.getNameGen().equals(other.getNameGen()))
            && (this.getEuLabeltype() == null ? other.getEuLabeltype() == null : this.getEuLabeltype().equals(other.getEuLabeltype()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getPkPd() == null) ? 0 : getPkPd().hashCode());
        result = prime * result + ((getCode() == null) ? 0 : getCode().hashCode());
        result = prime * result + ((getName() == null) ? 0 : getName().hashCode());
        result = prime * result + ((getSpec() == null) ? 0 : getSpec().hashCode());
        result = prime * result + ((getShortName() == null) ? 0 : getShortName().hashCode());
        result = prime * result + ((getBarcode() == null) ? 0 : getBarcode().hashCode());
        result = prime * result + ((getSpcode() == null) ? 0 : getSpcode().hashCode());
        result = prime * result + ((getConcent() == null) ? 0 : getConcent().hashCode());
        result = prime * result + ((getWeight() == null) ? 0 : getWeight().hashCode());
        result = prime * result + ((getPkUnitWt() == null) ? 0 : getPkUnitWt().hashCode());
        result = prime * result + ((getVol() == null) ? 0 : getVol().hashCode());
        result = prime * result + ((getPkUnitVol() == null) ? 0 : getPkUnitVol().hashCode());
        result = prime * result + ((getPkUnitMin() == null) ? 0 : getPkUnitMin().hashCode());
        result = prime * result + ((getPackSize() == null) ? 0 : getPackSize().hashCode());
        result = prime * result + ((getPkUnitPack() == null) ? 0 : getPkUnitPack().hashCode());
        result = prime * result + ((getEuMuputype() == null) ? 0 : getEuMuputype().hashCode());
        result = prime * result + ((getEuPdtype() == null) ? 0 : getEuPdtype().hashCode());
        result = prime * result + ((getEuDrugtype() == null) ? 0 : getEuDrugtype().hashCode());
        result = prime * result + ((getNameChem() == null) ? 0 : getNameChem().hashCode());
        result = prime * result + ((getPkFactory() == null) ? 0 : getPkFactory().hashCode());
        result = prime * result + ((getApprNo() == null) ? 0 : getApprNo().hashCode());
        result = prime * result + ((getEuPdprice() == null) ? 0 : getEuPdprice().hashCode());
        result = prime * result + ((getEuPap() == null) ? 0 : getEuPap().hashCode());
        result = prime * result + ((getAmtPap() == null) ? 0 : getAmtPap().hashCode());
        result = prime * result + ((getPapRate() == null) ? 0 : getPapRate().hashCode());
        result = prime * result + ((getDtAbrd() == null) ? 0 : getDtAbrd().hashCode());
        result = prime * result + ((getDtMade() == null) ? 0 : getDtMade().hashCode());
        result = prime * result + ((getDtDosage() == null) ? 0 : getDtDosage().hashCode());
        result = prime * result + ((getDtPharm() == null) ? 0 : getDtPharm().hashCode());
        result = prime * result + ((getDtPois() == null) ? 0 : getDtPois().hashCode());
        result = prime * result + ((getDtAnti() == null) ? 0 : getDtAnti().hashCode());
        result = prime * result + ((getFlagPrecious() == null) ? 0 : getFlagPrecious().hashCode());
        result = prime * result + ((getEuUsecate() == null) ? 0 : getEuUsecate().hashCode());
        result = prime * result + ((getDtStoretype() == null) ? 0 : getDtStoretype().hashCode());
        result = prime * result + ((getDtBase() == null) ? 0 : getDtBase().hashCode());
        result = prime * result + ((getFlagRm() == null) ? 0 : getFlagRm().hashCode());
        result = prime * result + ((getFlagReag() == null) ? 0 : getFlagReag().hashCode());
        result = prime * result + ((getFlagVacc() == null) ? 0 : getFlagVacc().hashCode());
        result = prime * result + ((getFlagSt() == null) ? 0 : getFlagSt().hashCode());
        result = prime * result + ((getFlagGmp() == null) ? 0 : getFlagGmp().hashCode());
        result = prime * result + ((getFlagTpn() == null) ? 0 : getFlagTpn().hashCode());
        result = prime * result + ((getFlagPed() == null) ? 0 : getFlagPed().hashCode());
        result = prime * result + ((getNote() == null) ? 0 : getNote().hashCode());
        result = prime * result + ((getDosageDef() == null) ? 0 : getDosageDef().hashCode());
        result = prime * result + ((getPkUnitDef() == null) ? 0 : getPkUnitDef().hashCode());
        result = prime * result + ((getCodeSupply() == null) ? 0 : getCodeSupply().hashCode());
        result = prime * result + ((getCodeFreq() == null) ? 0 : getCodeFreq().hashCode());
        result = prime * result + ((getDtChcate() == null) ? 0 : getDtChcate().hashCode());
        result = prime * result + ((getPkItemcate() == null) ? 0 : getPkItemcate().hashCode());
        result = prime * result + ((getPkOrdtype() == null) ? 0 : getPkOrdtype().hashCode());
        result = prime * result + ((getCreator() == null) ? 0 : getCreator().hashCode());
        result = prime * result + ((getCreateTime() == null) ? 0 : getCreateTime().hashCode());
        result = prime * result + ((getModifier() == null) ? 0 : getModifier().hashCode());
        result = prime * result + ((getDelFlag() == null) ? 0 : getDelFlag().hashCode());
        result = prime * result + ((getPkOrg() == null) ? 0 : getPkOrg().hashCode());
        result = prime * result + ((getPrice() == null) ? 0 : getPrice().hashCode());
        result = prime * result + ((getValidCnt() == null) ? 0 : getValidCnt().hashCode());
        result = prime * result + ((getEuValidUnit() == null) ? 0 : getEuValidUnit().hashCode());
        result = prime * result + ((getFlagStop() == null) ? 0 : getFlagStop().hashCode());
        result = prime * result + ((getEuSource() == null) ? 0 : getEuSource().hashCode());
        result = prime * result + ((getTs() == null) ? 0 : getTs().hashCode());
        result = prime * result + ((getPkPdind() == null) ? 0 : getPkPdind().hashCode());
        result = prime * result + ((getRegNo() == null) ? 0 : getRegNo().hashCode());
        result = prime * result + ((getDateValidReg() == null) ? 0 : getDateValidReg().hashCode());
        result = prime * result + ((getEuStockmode() == null) ? 0 : getEuStockmode().hashCode());
        result = prime * result + ((getPkPdcate() == null) ? 0 : getPkPdcate().hashCode());
        result = prime * result + ((getCodeCostitem() == null) ? 0 : getCodeCostitem().hashCode());
        result = prime * result + ((getPkItem() == null) ? 0 : getPkItem().hashCode());
        result = prime * result + ((getFlagSingle() == null) ? 0 : getFlagSingle().hashCode());
        result = prime * result + ((getFlagImp() == null) ? 0 : getFlagImp().hashCode());
        result = prime * result + ((getFlagUse() == null) ? 0 : getFlagUse().hashCode());
        result = prime * result + ((getDtUsagenote() == null) ? 0 : getDtUsagenote().hashCode());
        result = prime * result + ((getPkPdgn() == null) ? 0 : getPkPdgn().hashCode());
        result = prime * result + ((getPackSizeMax() == null) ? 0 : getPackSizeMax().hashCode());
        result = prime * result + ((getDtPdtype() == null) ? 0 : getDtPdtype().hashCode());
        result = prime * result + ((getEuHerbtype() == null) ? 0 : getEuHerbtype().hashCode());
        result = prime * result + ((getFlagChrt() == null) ? 0 : getFlagChrt().hashCode());
        result = prime * result + ((getDtInjtype() == null) ? 0 : getDtInjtype().hashCode());
        result = prime * result + ((getEuHptype() == null) ? 0 : getEuHptype().hashCode());
        result = prime * result + ((getEuHpratio() == null) ? 0 : getEuHpratio().hashCode());
        result = prime * result + ((getEuRisk() == null) ? 0 : getEuRisk().hashCode());
        result = prime * result + ((getCodeHp() == null) ? 0 : getCodeHp().hashCode());
        result = prime * result + ((getCodeStd() == null) ? 0 : getCodeStd().hashCode());
        result = prime * result + ((getAgeMin() == null) ? 0 : getAgeMin().hashCode());
        result = prime * result + ((getAgeMax() == null) ? 0 : getAgeMax().hashCode());
        result = prime * result + ((getEuSex() == null) ? 0 : getEuSex().hashCode());
        result = prime * result + ((getQuotaDos() == null) ? 0 : getQuotaDos().hashCode());
        result = prime * result + ((getNameGen() == null) ? 0 : getNameGen().hashCode());
        result = prime * result + ((getEuLabeltype() == null) ? 0 : getEuLabeltype().hashCode());
        return result;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", pkPd=").append(pkPd);
        sb.append(", code=").append(code);
        sb.append(", name=").append(name);
        sb.append(", spec=").append(spec);
        sb.append(", shortName=").append(shortName);
        sb.append(", barcode=").append(barcode);
        sb.append(", spcode=").append(spcode);
        sb.append(", concent=").append(concent);
        sb.append(", weight=").append(weight);
        sb.append(", pkUnitWt=").append(pkUnitWt);
        sb.append(", vol=").append(vol);
        sb.append(", pkUnitVol=").append(pkUnitVol);
        sb.append(", pkUnitMin=").append(pkUnitMin);
        sb.append(", packSize=").append(packSize);
        sb.append(", pkUnitPack=").append(pkUnitPack);
        sb.append(", euMuputype=").append(euMuputype);
        sb.append(", euPdtype=").append(euPdtype);
        sb.append(", euDrugtype=").append(euDrugtype);
        sb.append(", nameChem=").append(nameChem);
        sb.append(", pkFactory=").append(pkFactory);
        sb.append(", apprNo=").append(apprNo);
        sb.append(", euPdprice=").append(euPdprice);
        sb.append(", euPap=").append(euPap);
        sb.append(", amtPap=").append(amtPap);
        sb.append(", papRate=").append(papRate);
        sb.append(", dtAbrd=").append(dtAbrd);
        sb.append(", dtMade=").append(dtMade);
        sb.append(", dtDosage=").append(dtDosage);
        sb.append(", dtPharm=").append(dtPharm);
        sb.append(", dtPois=").append(dtPois);
        sb.append(", dtAnti=").append(dtAnti);
        sb.append(", flagPrecious=").append(flagPrecious);
        sb.append(", euUsecate=").append(euUsecate);
        sb.append(", dtStoretype=").append(dtStoretype);
        sb.append(", dtBase=").append(dtBase);
        sb.append(", flagRm=").append(flagRm);
        sb.append(", flagReag=").append(flagReag);
        sb.append(", flagVacc=").append(flagVacc);
        sb.append(", flagSt=").append(flagSt);
        sb.append(", flagGmp=").append(flagGmp);
        sb.append(", flagTpn=").append(flagTpn);
        sb.append(", flagPed=").append(flagPed);
        sb.append(", note=").append(note);
        sb.append(", dosageDef=").append(dosageDef);
        sb.append(", pkUnitDef=").append(pkUnitDef);
        sb.append(", codeSupply=").append(codeSupply);
        sb.append(", codeFreq=").append(codeFreq);
        sb.append(", dtChcate=").append(dtChcate);
        sb.append(", pkItemcate=").append(pkItemcate);
        sb.append(", pkOrdtype=").append(pkOrdtype);
        sb.append(", creator=").append(creator);
        sb.append(", createTime=").append(createTime);
        sb.append(", modifier=").append(modifier);
        sb.append(", delFlag=").append(delFlag);
        sb.append(", pkOrg=").append(pkOrg);
        sb.append(", price=").append(price);
        sb.append(", validCnt=").append(validCnt);
        sb.append(", euValidUnit=").append(euValidUnit);
        sb.append(", flagStop=").append(flagStop);
        sb.append(", euSource=").append(euSource);
        sb.append(", ts=").append(ts);
        sb.append(", pkPdind=").append(pkPdind);
        sb.append(", regNo=").append(regNo);
        sb.append(", dateValidReg=").append(dateValidReg);
        sb.append(", euStockmode=").append(euStockmode);
        sb.append(", pkPdcate=").append(pkPdcate);
        sb.append(", codeCostitem=").append(codeCostitem);
        sb.append(", pkItem=").append(pkItem);
        sb.append(", flagSingle=").append(flagSingle);
        sb.append(", flagImp=").append(flagImp);
        sb.append(", flagUse=").append(flagUse);
        sb.append(", dtUsagenote=").append(dtUsagenote);
        sb.append(", pkPdgn=").append(pkPdgn);
        sb.append(", packSizeMax=").append(packSizeMax);
        sb.append(", dtPdtype=").append(dtPdtype);
        sb.append(", euHerbtype=").append(euHerbtype);
        sb.append(", flagChrt=").append(flagChrt);
        sb.append(", dtInjtype=").append(dtInjtype);
        sb.append(", euHptype=").append(euHptype);
        sb.append(", euHpratio=").append(euHpratio);
        sb.append(", euRisk=").append(euRisk);
        sb.append(", codeHp=").append(codeHp);
        sb.append(", codeStd=").append(codeStd);
        sb.append(", ageMin=").append(ageMin);
        sb.append(", ageMax=").append(ageMax);
        sb.append(", euSex=").append(euSex);
        sb.append(", quotaDos=").append(quotaDos);
        sb.append(", nameGen=").append(nameGen);
        sb.append(", euLabeltype=").append(euLabeltype);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}