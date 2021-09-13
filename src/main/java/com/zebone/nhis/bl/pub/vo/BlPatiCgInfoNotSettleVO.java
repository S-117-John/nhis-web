package com.zebone.nhis.bl.pub.vo;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 未收费项目返回vo
 */
public class BlPatiCgInfoNotSettleVO{

	private String pkInsu;
	private String pkPv;
	private String pkPi;
	private String pkPres;
	private String dtPrestype;
	private String presNo;
	private String nameDept;//开立科室名称
	private Date dateEnter;
	private String nameEmpOrd;
	private String nameEmpInput;
	private String diagname;
	private Long ords;
	private String codeOrdtype;
	private String picate;
	private String pkCnord;
	private String nameCg;
	private String nameOrd;
	private String spec;
	private String unit;
	private Double dosage;
	private String unitDos;
	private String supply;
	private String freq;
	private Long days;
	private Double quan;
	private String herbPkpd;
	private Double quanCg;
	private BigDecimal priceOrg;
	private String freqName;
	private String dosTypeName;
	
	private String pkCgop;
	private BigDecimal price;
	private BigDecimal amount;
	private BigDecimal ratioSelf;//自费比例
	private BigDecimal amountHppi;//医保金额_患者支付
	private String pkDisc;//优惠类型
	private BigDecimal ratioDisc;//优惠比例
	private BigDecimal ratioAdd;//加收比例
	private BigDecimal amountAdd;//加收金额
	private String flagInsu;//医保上传标志
	private String euAdditem;//附加费类型  0 非附加，1 附加项目
	private String flagSettle;//结算标志，挂号费使用
	private String pkSettle;//结算主键 挂号费使用
	private String flagRecharge;//退费重收标志
	
	private String qrCode;   //是否打印二维码
	private String nameDeptEx;
	private Integer ordsn;
	private Integer ordsnParent;
	private String pkDeptApp; //开单科室主键
	private String pkEmpApp; //开单医生
	private String pkDeptEx; //执行科室
	private String pkItem; //收费项目主键
	private String pkPd; //药品主键
	private String flagPd; //物品标记
	private String codeBill; //发票分类编码
	private String nameBill; //发票分类名称
	private String pkInvcate; //发票分类主键
	private String itemcate; //项目分类
	private BigDecimal amountPi; //自付金额
	private String flagPv; //挂号费标志
//	private String euDrugType; //药品类别
	private String itemCode;//药品编码项目编码
	private String codeDept;
	private String codeEmp;
	private Date ts;//记费时间戳
	private Date dateHap;//费用发生日期
	private Date dateCg;//记费日期
	private String flagFit;//医嘱限制用药
	private String codeHp;//药品上传编码
	private String dtHpprop;//处方类型
	private String hppropName;//处方类型名称
	private String creatDocterNo;//开立医生工号
	private String nameEmpPhy;//开立医生姓名

	private String winnoPrep;//配药窗口号
	private String winnoConf;//发药窗口

	private String pkDeptAreaapp;//诊区
	/**自备药标识 1自备药 0非自备*/
	private String flagSelf;
	
	/**医嘱确费标识*/
	private String flagOcc;

	private String pkUnitCg;

	public String getNameEmpPhy() {
		return nameEmpPhy;
	}

	public void setNameEmpPhy(String nameEmpPhy) {
		this.nameEmpPhy = nameEmpPhy;
	}

	public String getCreatDocterNo() {
		return creatDocterNo;
	}

	public void setCreatDocterNo(String creatDocterNo) {
		this.creatDocterNo = creatDocterNo;
	}


	public String getFreqName() {
		return freqName;
	}
	public void setFreqName(String freqName) {
		this.freqName = freqName;
	}
	
	public String getHppropName() {
		return hppropName;
	}
	public void setHppropName(String hppropName) {
		this.hppropName = hppropName;
	}
	public String getDtHpprop() {
		return dtHpprop;
	}
	public void setDtHpprop(String dtHpprop) {
		this.dtHpprop = dtHpprop;
	}
	public String getFlagRecharge() {
		return flagRecharge;
	}
	public void setFlagRecharge(String flagRecharge) {
		this.flagRecharge = flagRecharge;
	}
	public String getPkSettle() {
		return pkSettle;
	}
	public void setPkSettle(String pkSettle) {
		this.pkSettle = pkSettle;
	}
	public String getFlagSettle() {
		return flagSettle;
	}
	public void setFlagSettle(String flagSettle) {
		this.flagSettle = flagSettle;
	}
	public String getPkPi() {
		return pkPi;
	}
	public void setPkPi(String pkPi) {
		this.pkPi = pkPi;
	}
	public BigDecimal getRatioSelf() {
		return ratioSelf;
	}
	public void setRatioSelf(BigDecimal ratioSelf) {
		this.ratioSelf = ratioSelf;
	}
	public BigDecimal getAmountHppi() {
		return amountHppi;
	}
	public void setAmountHppi(BigDecimal amountHppi) {
		this.amountHppi = amountHppi;
	}
	public String getPkDisc() {
		return pkDisc;
	}
	public void setPkDisc(String pkDisc) {
		this.pkDisc = pkDisc;
	}
	public BigDecimal getRatioDisc() {
		return ratioDisc;
	}
	public void setRatioDisc(BigDecimal ratioDisc) {
		this.ratioDisc = ratioDisc;
	}
	public BigDecimal getRatioAdd() {
		return ratioAdd;
	}
	public void setRatioAdd(BigDecimal ratioAdd) {
		this.ratioAdd = ratioAdd;
	}
	public BigDecimal getAmountAdd() {
		return amountAdd;
	}
	public void setAmountAdd(BigDecimal amountAdd) {
		this.amountAdd = amountAdd;
	}
	public String getFlagInsu() {
		return flagInsu;
	}
	public void setFlagInsu(String flagInsu) {
		this.flagInsu = flagInsu;
	}
	public Date getTs() {
		return ts;
	}
	public void setTs(Date ts) {
		this.ts = ts;
	}
	public BigDecimal getPrice() {
		return price;
	}
	public void setPrice(BigDecimal price) {
		this.price = price;
	}
	public String getEuAdditem() {
		return euAdditem;
	}
	public void setEuAdditem(String euAdditem) {
		this.euAdditem = euAdditem;
	}
	public String getCodeEmp() {
		return codeEmp;
	}
	public void setCodeEmp(String codeEmp) {
		this.codeEmp = codeEmp;
	}
	public String getCodeDept() {
		return codeDept;
	}
	public void setCodeDept(String codeDept) {
		this.codeDept = codeDept;
	}
	public String getItemCode() {
		return itemCode;
	}
	public void setItemCode(String itemCode) {
		this.itemCode = itemCode;
	}
	public String getItemcate() {
		return itemcate;
	}
	public void setItemcate(String itemcate) {
		this.itemcate = itemcate;
	}
	public BigDecimal getAmountPi() {
		return amountPi;
	}
	public void setAmountPi(BigDecimal amountPi) {
		this.amountPi = amountPi;
	}
	public String getFlagPv() {
		return flagPv;
	}
	public void setFlagPv(String flagPv) {
		this.flagPv = flagPv;
	}
//	public String getEuDrugType() {
//		return euDrugType;
//	}
//	public void setEuDrugType(String euDrugType) {
//		this.euDrugType = euDrugType;
//	}
	public String getPkInsu() {
		return pkInsu;
	}
	public void setPkInsu(String pkInsu) {
		this.pkInsu = pkInsu;
	}
	public String getPkPv() {
		return pkPv;
	}
	public void setPkPv(String pkPv) {
		this.pkPv = pkPv;
	}
	public String getPkPres() {
		return pkPres;
	}
	public void setPkPres(String pkPres) {
		this.pkPres = pkPres;
	}
	public String getDtPrestype() {
		return dtPrestype;
	}
	public void setDtPrestype(String dtPrestype) {
		this.dtPrestype = dtPrestype;
	}
	public String getQrCode() {
		return qrCode;
	}
	public void setQrCode(String qrCode) {
		this.qrCode = qrCode;
	}
	public String getPresNo() {
		return presNo;
	}
	public void setPresNo(String presNo) {
		this.presNo = presNo;
	}
	public String getNameDept() {
		return nameDept;
	}
	public void setNameDept(String nameDept) {
		this.nameDept = nameDept;
	}
	public Date getDateEnter() {
		return dateEnter;
	}
	public void setDateEnter(Date dateEnter) {
		this.dateEnter = dateEnter;
	}
	public String getNameEmpOrd() {
		return nameEmpOrd;
	}
	public void setNameEmpOrd(String nameEmpOrd) {
		this.nameEmpOrd = nameEmpOrd;
	}
	public String getNameEmpInput() {
		return nameEmpInput;
	}
	public void setNameEmpInput(String nameEmpInput) {
		this.nameEmpInput = nameEmpInput;
	}
	public String getDiagname() {
		return diagname;
	}
	public void setDiagname(String diagname) {
		this.diagname = diagname;
	}
	public Long getOrds() {
		return ords;
	}
	public void setOrds(Long ords) {
		this.ords = ords;
	}
	public String getCodeOrdtype() {
		return codeOrdtype;
	}
	public void setCodeOrdtype(String codeOrdtype) {
		this.codeOrdtype = codeOrdtype;
	}
	public String getPicate() {
		return picate;
	}
	public void setPicate(String picate) {
		this.picate = picate;
	}
	public String getPkCnord() {
		return pkCnord;
	}
	public void setPkCnord(String pkCnord) {
		this.pkCnord = pkCnord;
	}
	public String getPkCgop() {
		return pkCgop;
	}
	public String getPkInvcate() {
		return pkInvcate;
	}
	public void setPkInvcate(String pkInvcate) {
		this.pkInvcate = pkInvcate;
	}
	public void setPkCgop(String pkCgop) {
		this.pkCgop = pkCgop;
	}
	public String getNameCg() {
		return nameCg;
	}
	public void setNameCg(String nameCg) {
		this.nameCg = nameCg;
	}
	public String getNameOrd() {
		return nameOrd;
	}
	public void setNameOrd(String nameOrd) {
		this.nameOrd = nameOrd;
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
	public Double getDosage() {
		return dosage;
	}
	public void setDosage(Double dosage) {
		this.dosage = dosage;
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
	public Long getDays() {
		return days;
	}
	public void setDays(Long days) {
		this.days = days;
	}
	public Double getQuan() {
		return quan;
	}
	public void setQuan(Double quan) {
		this.quan = quan;
	}
	public String getHerbPkpd() {
		return herbPkpd;
	}
	public void setHerbPkpd(String herbPkpd) {
		this.herbPkpd = herbPkpd;
	}
	public Double getQuanCg() {
		return quanCg;
	}
	public void setQuanCg(Double quanCg) {
		this.quanCg = quanCg;
	}
	public BigDecimal getPriceOrg() {
		return priceOrg;
	}
	public void setPriceOrg(BigDecimal priceOrg) {
		this.priceOrg = priceOrg;
	}
	public BigDecimal getAmount() {
		return amount;
	}
	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}
	public String getNameDeptEx() {
		return nameDeptEx;
	}
	public void setNameDeptEx(String nameDeptEx) {
		this.nameDeptEx = nameDeptEx;
	}
	public Integer getOrdsn() {
		return ordsn;
	}
	public void setOrdsn(Integer ordsn) {
		this.ordsn = ordsn;
	}
	public Integer getOrdsnParent() {
		return ordsnParent;
	}
	public void setOrdsnParent(Integer ordsnParent) {
		this.ordsnParent = ordsnParent;
	}
	public String getPkDeptApp() {
		return pkDeptApp;
	}
	public void setPkDeptApp(String pkDeptApp) {
		this.pkDeptApp = pkDeptApp;
	}
	public String getPkEmpApp() {
		return pkEmpApp;
	}
	public void setPkEmpApp(String pkEmpApp) {
		this.pkEmpApp = pkEmpApp;
	}
	public String getPkDeptEx() {
		return pkDeptEx;
	}
	public void setPkDeptEx(String pkDeptEx) {
		this.pkDeptEx = pkDeptEx;
	}
	public String getPkItem() {
		return pkItem;
	}
	public void setPkItem(String pkItem) {
		this.pkItem = pkItem;
	}
	public String getPkPd() {
		return pkPd;
	}
	public void setPkPd(String pkPd) {
		this.pkPd = pkPd;
	}
	public String getFlagPd() {
		return flagPd;
	}
	public void setFlagPd(String flagPd) {
		this.flagPd = flagPd;
	}
	public String getCodeBill() {
		return codeBill;
	}
	public void setCodeBill(String codeBill) {
		this.codeBill = codeBill;
	}
	public String getNameBill() {
		return nameBill;
	}
	public void setNameBill(String nameBill) {
		this.nameBill = nameBill;
	}
	public Date getDateHap() {
		return dateHap;
	}
	public void setDateHap(Date dateHap) {
		this.dateHap = dateHap;
	}
	public Date getDateCg() {
		return dateCg;
	}
	public void setDateCg(Date dateCg) {
		this.dateCg = dateCg;
	}
	public String getFlagFit() {
		return flagFit;
	}
	public void setFlagFit(String flagFit) {
		this.flagFit = flagFit;
	}
	public String getCodeHp() {
		return codeHp;
	}
	public void setCodeHp(String codeHp) {
		this.codeHp = codeHp;
	}
	public String getDosTypeName() {
		return dosTypeName;
	}
	public void setDosTypeName(String dosTypeName) {
		this.dosTypeName = dosTypeName;
	}
	public String getFlagOcc() {
		return flagOcc;
	}
	public void setFlagOcc(String flagOcc) {
		this.flagOcc = flagOcc;
	}

	public String getWinnoPrep() {
		return winnoPrep;
	}

	public void setWinnoPrep(String winnoPrep) {
		this.winnoPrep = winnoPrep;
	}

	public String getWinnoConf() {
		return winnoConf;
	}

	public void setWinnoConf(String winnoConf) {
		this.winnoConf = winnoConf;
	}

	public String getPkDeptAreaapp() {
		return pkDeptAreaapp;
	}

	public void setPkDeptAreaapp(String pkDeptAreaapp) {
		this.pkDeptAreaapp = pkDeptAreaapp;
	}

	public String getPkUnitCg() {
		return pkUnitCg;
	}

	public void setPkUnitCg(String pkUnitCg) {
		this.pkUnitCg = pkUnitCg;
	}

	public String getFlagSelf() {
		return flagSelf;
	}

	public void setFlagSelf(String flagSelf) {
		this.flagSelf = flagSelf;
	}
}
