package com.zebone.nhis.ex.pub.vo;

import java.util.Date;


@SuppressWarnings("serial")
public class GeneratePdApExListVo {

	private String pkPv;
    private String namePi;
    private String codeIp;//住院号
    private String pkInsu;
    private String bedNo;
    private String ordsnParent;
    private String ordsn;
    private String pkCnord;
    private String pkOrg;
    private String pkDept;
    private String pkDeptOcc;
    private String pkOrgOcc;
    private String nameDeptOcc;
    private String nameOrgOcc;
    private Date dateEx;
    private Double quan;//--用量
    private Double dosage;//--剂量
    private Double quanOcc;//--用量
    private String pkUnitDos;//--剂量单位
    private String pkUnit;//--用量单位
    private Double packSize;//包装量，基本包装量（住院用最小的）（零售/门诊用最大的）
    private Double quanBed;//--床边量
    private String pkPd;//--物品主键
    private Integer ords;//--付数
    private String flagMedout;//--出院带药
    private String pdname;//--药品名称
    private String pdcode;//--药品编码
    private String spec;//规格
    private Double price;//参考价格
    private String dtPois;//毒麻分类
    private String pkUnitMin;//物品表里面的基本包装单位
    private Double priceMin;//基本单位下的价格
    private String nameUnitMin;//基本单位名称
    private String euMuputype;//包装单位取整模式
    private String pkUnitPack;//包装单位
    private String pkUnitStore;//仓库对应的包装单位
    private String nameUnitStore;
    private Double packSizeP;//针对基本单位的包装数量
    private int packSizeStore;//仓库对应的包装量
    private Date dateLastEx;//
    private String codeOrdtype;
    private String euCycle;//--频次周期单位
    private String pkPres;
    private String flagSelf;
    private String flagBase;
    private String flagEmer;//加急标志
    private String pkExocc;
    private String euAlways;
    private String namesupply;
    private String freqname;
    private String flagPivas;//静配标志
    private String  sign;//同组标志
    
    private String euBoil;//处方发药类型（外部接口，内部发药）
    private String flagNew;//新开标志
    private String pkDeptExOrd;//医嘱开立时对应的执行科室--切换静配非静配使用
	private String nameDeptExOrd;//医嘱开立时对应的执行科室--切换静配非静配使用
	
	private String flagStop;//物品停用
	private String flagStoreStop;//仓库停用
	
	private String flagOrdStop;//医嘱停止
	private String flagStopChk;//医嘱停止核对
	private Date dateStop;//医嘱停止时间
	private Date datePlan;//计划执行时间
	
    private double amount;//金额
    private String batchNo;//批号
    private String pkCgip;//记费主键
    private String CodeOp;//门诊号
    private String pkCgop;//门诊计费主键
	private  String euSt; //皮试标志
	
	private String nameUnitOrd;//order表的用量单位
    private Integer packSizeOrd;//order表的里包装量

	private Double stockSum;//库存剩余总数
	public String getEuSt() {
		return euSt;
	}

	public void setEuSt(String euSt) {
		this.euSt = euSt;
	}




	public String getOrdsn() {
		return ordsn;
	}
	public void setOrdsn(String ordsn) {
		this.ordsn = ordsn;
	}
	public String getFlagStop() {
		return flagStop;
	}
	public void setFlagStop(String flagStop) {
		this.flagStop = flagStop;
	}
	public String getFlagStoreStop() {
		return flagStoreStop;
	}
	public void setFlagStoreStop(String flagStoreStop) {
		this.flagStoreStop = flagStoreStop;
	}
	public String getPkDeptExOrd() {
		return pkDeptExOrd;
	}
	public void setPkDeptExOrd(String pkDeptExOrd) {
		this.pkDeptExOrd = pkDeptExOrd;
	}
	public String getNameDeptExOrd() {
		return nameDeptExOrd;
	}
	public void setNameDeptExOrd(String nameDeptExOrd) {
		this.nameDeptExOrd = nameDeptExOrd;
	}
	public String getSign() {
		return sign;
	}
	public void setSign(String sign) {
		this.sign = sign;
	}
	public String getFlagNew() {
		return flagNew;
	}
	public void setFlagNew(String flagNew) {
		this.flagNew = flagNew;
	}
	public String getFlagPivas() {
		return flagPivas;
	}
	public void setFlagPivas(String flagPivas) {
		this.flagPivas = flagPivas;
	}
	public String getEuBoil() {
		return euBoil;
	}
	public void setEuBoil(String euBoil) {
		this.euBoil = euBoil;
	}
	public String getFlagEmer() {
		return flagEmer;
	}
	public void setFlagEmer(String flagEmer) {
		this.flagEmer = flagEmer;
	}
	public Double getPriceMin() {
		return priceMin;
	}
	public void setPriceMin(Double priceMin) {
		this.priceMin = priceMin;
	}
	public String getNameUnitMin() {
		return nameUnitMin;
	}
	public void setNameUnitMin(String nameUnitMin) {
		this.nameUnitMin = nameUnitMin;
	}
	public String getCodeIp() {
		return codeIp;
	}
	public void setCodeIp(String codeIp) {
		this.codeIp = codeIp;
	}
	public String getNameUnitStore() {
		return nameUnitStore;
	}
	public void setNameUnitStore(String nameUnitStore) {
		this.nameUnitStore = nameUnitStore;
	}
	public int getPackSizeStore() {
		return packSizeStore;
	}
	public void setPackSizeStore(int packSizeStore) {
		this.packSizeStore = packSizeStore;
	}
	public String getPkUnitStore() {
		return pkUnitStore;
	}
	public void setPkUnitStore(String pkUnitStore) {
		this.pkUnitStore = pkUnitStore;
	}
	public String getFlagBase() {
		return flagBase;
	}
	public void setFlagBase(String flagBase) {
		this.flagBase = flagBase;
	}
	public Double getQuanOcc() {
		return quanOcc;
	}
	public void setQuanOcc(Double quanOcc) {
		this.quanOcc = quanOcc;
	}
	public Double getPackSize() {
		return packSize;
	}
	public void setPackSize(Double packSize) {
		this.packSize = packSize;
	}
	public Double getQuanBed() {
		return quanBed;
	}
	public void setQuanBed(Double quanBed) {
		this.quanBed = quanBed;
	}
	public String getSpec() {
		return spec;
	}
	public void setSpec(String spec) {
		this.spec = spec;
	}
	public Double getPrice() {
		return price;
	}
	public void setPrice(Double price) {
		this.price = price;
	}
	public String getDtPois() {
		return dtPois;
	}
	public void setDtPois(String dtPois) {
		this.dtPois = dtPois;
	}
	public String getPkUnitMin() {
		return pkUnitMin;
	}
	public void setPkUnitMin(String pkUnitMin) {
		this.pkUnitMin = pkUnitMin;
	}
	public String getEuMuputype() {
		return euMuputype;
	}
	public void setEuMuputype(String euMuputype) {
		this.euMuputype = euMuputype;
	}
	public String getPkUnitPack() {
		return pkUnitPack;
	}
	public void setPkUnitPack(String pkUnitPack) {
		this.pkUnitPack = pkUnitPack;
	}
	public Double getPackSizeP() {
		return packSizeP;
	}
	public void setPackSizeP(Double packSizeP) {
		this.packSizeP = packSizeP;
	}
	public String getPkPv() {
		return pkPv;
	}
	public void setPkPv(String pkPv) {
		this.pkPv = pkPv;
	}
	public String getNamePi() {
		return namePi;
	}
	public void setNamePi(String namePi) {
		this.namePi = namePi;
	}
	public String getPkInsu() {
		return pkInsu;
	}
	public void setPkInsu(String pkInsu) {
		this.pkInsu = pkInsu;
	}
	public String getBedNo() {
		return bedNo;
	}
	public void setBedNo(String bedNo) {
		this.bedNo = bedNo;
	}
	public String getOrdsnParent() {
		return ordsnParent;
	}
	public void setOrdsnParent(String ordsnParent) {
		this.ordsnParent = ordsnParent;
	}
	public String getPkCnord() {
		return pkCnord;
	}
	public void setPkCnord(String pkCnord) {
		this.pkCnord = pkCnord;
	}
	public String getPkOrg() {
		return pkOrg;
	}
	public void setPkOrg(String pkOrg) {
		this.pkOrg = pkOrg;
	}
	public String getPkDept() {
		return pkDept;
	}
	public void setPkDept(String pkDept) {
		this.pkDept = pkDept;
	}
	public String getPkDeptOcc() {
		return pkDeptOcc;
	}
	public void setPkDeptOcc(String pkDeptOcc) {
		this.pkDeptOcc = pkDeptOcc;
	}
	public String getPkOrgOcc() {
		return pkOrgOcc;
	}
	public void setPkOrgOcc(String pkOrgOcc) {
		this.pkOrgOcc = pkOrgOcc;
	}
	public String getNameDeptOcc() {
		return nameDeptOcc;
	}
	public void setNameDeptOcc(String nameDeptOcc) {
		this.nameDeptOcc = nameDeptOcc;
	}
	public String getNameOrgOcc() {
		return nameOrgOcc;
	}
	public void setNameOrgOcc(String nameOrgOcc) {
		this.nameOrgOcc = nameOrgOcc;
	}
	public Date getDateEx() {
		return dateEx;
	}
	public void setDateEx(Date dateEx) {
		this.dateEx = dateEx;
	}
	public Double getQuan() {
		return quan;
	}
	public void setQuan(Double quan) {
		this.quan = quan;
	}
	public Double getDosage() {
		return dosage;
	}
	public void setDosage(Double dosage) {
		this.dosage = dosage;
	}
	
	public String getPkUnitDos() {
		return pkUnitDos;
	}
	public void setPkUnitDos(String pkUnitDos) {
		this.pkUnitDos = pkUnitDos;
	}
	public String getPkUnit() {
		return pkUnit;
	}
	public void setPkUnit(String pkUnit) {
		this.pkUnit = pkUnit;
	}
	public String getPkPd() {
		return pkPd;
	}
	public void setPkPd(String pkPd) {
		this.pkPd = pkPd;
	}
	
	public Integer getOrds() {
		return ords;
	}
	public void setOrds(Integer ords) {
		this.ords = ords;
	}
	public String getFlagMedout() {
		return flagMedout;
	}
	public void setFlagMedout(String flagMedout) {
		this.flagMedout = flagMedout;
	}
	public String getPdname() {
		return pdname;
	}
	public void setPdname(String pdname) {
		this.pdname = pdname;
	}
	public Date getDateLastEx() {
		return dateLastEx;
	}
	public void setDateLastEx(Date dateLastEx) {
		this.dateLastEx = dateLastEx;
	}
	public String getCodeOrdtype() {
		return codeOrdtype;
	}
	public void setCodeOrdtype(String codeOrdtype) {
		this.codeOrdtype = codeOrdtype;
	}
	public String getEuCycle() {
		return euCycle;
	}
	public void setEuCycle(String euCycle) {
		this.euCycle = euCycle;
	}
	public String getPkPres() {
		return pkPres;
	}
	public void setPkPres(String pkPres) {
		this.pkPres = pkPres;
	}
	public String getFlagSelf() {
		return flagSelf;
	}
	public void setFlagSelf(String flagSelf) {
		this.flagSelf = flagSelf;
	}
	public String getPkExocc() {
		return pkExocc;
	}
	public void setPkExocc(String pkExocc) {
		this.pkExocc = pkExocc;
	}
	public String getEuAlways() {
		return euAlways;
	}
	public void setEuAlways(String euAlways) {
		this.euAlways = euAlways;
	}
	public String getNamesupply() {
		return namesupply;
	}
	public void setNamesupply(String namesupply) {
		this.namesupply = namesupply;
	}
	public String getFreqname() {
		return freqname;
	}
	public void setFreqname(String freqname) {
		this.freqname = freqname;
	}
	public double getAmount() {
		return amount;
	}
	public void setAmount(double amount) {
		this.amount = amount;
	}
	public String getBatchNo() {
		return batchNo;
	}
	public void setBatchNo(String batchNo) {
		this.batchNo = batchNo;
	}
	public String getPdcode() {
		return pdcode;
	}
	public void setPdcode(String pdcode) {
		this.pdcode = pdcode;
	}
	public String getFlagOrdStop() {
		return flagOrdStop;
	}
	public void setFlagOrdStop(String flagOrdStop) {
		this.flagOrdStop = flagOrdStop;
	}
	public String getFlagStopChk() {
		return flagStopChk;
	}
	public void setFlagStopChk(String flagStopChk) {
		this.flagStopChk = flagStopChk;
	}
	public Date getDateStop() {
		return dateStop;
	}
	public void setDateStop(Date dateStop) {
		this.dateStop = dateStop;
	}
	public Date getDatePlan() {
		return datePlan;
	}
	public void setDatePlan(Date datePlan) {
		this.datePlan = datePlan;
	}
	public String getPkCgip() {
		return pkCgip;
	}
	public void setPkCgip(String pkCgip) {
		this.pkCgip = pkCgip;
	}
	public String getCodeOp() {
		return CodeOp;
	}
	public void setCodeOp(String codeOp) {
		CodeOp = codeOp;
	}
	public String getPkCgop() {
		return pkCgop;
	}
	public void setPkCgop(String pkCgop) {
		this.pkCgop = pkCgop;
	}

	public String getNameUnitOrd() {
		return nameUnitOrd;
	}

	public void setNameUnitOrd(String nameUnitOrd) {
		this.nameUnitOrd = nameUnitOrd;
	}

	public Integer getPackSizeOrd() {
		return packSizeOrd;
	}

	public void setPackSizeOrd(Integer packSizeOrd) {
		this.packSizeOrd = packSizeOrd;
	}

	public Double getStockSum() {
		return stockSum;
	}

	public void setStockSum(Double stockSum) {
		this.stockSum = stockSum;
	}
}
