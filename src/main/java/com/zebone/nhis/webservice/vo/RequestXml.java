package com.zebone.nhis.webservice.vo;

import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.zebone.nhis.common.support.XmlElementAnno;

/**
 * 请求参数构造xml
 * @author frank
 *
 */
@XmlRootElement(name = "req")
public class RequestXml {

	@XmlElementAnno
	private String pkOrg;
	@XmlElementAnno
	private String pkOrgarea;
	@XmlElementAnno
	private String pkDept;
	@XmlElementAnno
	private String codeDept;
	@XmlElementAnno
	private String codeEmp;
	@XmlElementAnno
	private String pkEmp;
	@XmlElementAnno
	private String codeOrg;

	@XmlElementAnno
	private String namePi;
	@XmlElementAnno
	private String dtIdtype;

	@XmlElementAnno
	private String idNo;
	@XmlElementAnno
	private String mobile;
	@XmlElementAnno
	private String dtSex;
	@XmlElementAnno
	private String yzStatus;
	/**预约渠道*/
	@XmlElementAnno
	private String dtApptype;

	/**查询患者相关请求参数*/
	// 患者唯一标识符
	@XmlElementAnno
	private String pkPi;
	// 患者编码
	@XmlElementAnno
	private String codePi;
	// 身份证号
	@XmlElementAnno
	private String idno;
	// 住院号
	@XmlElementAnno
	private String codeIp;
	// 就诊卡号
	@XmlElementAnno
	private String codeCard;

	/**根据科室查询出诊专家相关请求参数*/
	// 0 普通；1 专家；2 特诊；9 急诊
	@XmlElementAnno
	private String euSrvtype;
	// 开始时间
	@XmlElementAnno
	private String dateBegin;
	// 结束时间
	@XmlElementAnno
	private String dateEnd;
	@XmlElementAnno
	private String dateWork;
	@XmlElementAnno
	private String pkPicate;

	@XmlElementAnno
	private String registerDate;
	@XmlElementAnno
	private String registerTime;
	@XmlElementAnno
	private String hicNo;
	
	@XmlElement(name = "dateWork")
	public String getDateWork() {
		return dateWork;
	}

	public void setDateWork(String dateWork) {
		this.dateWork = dateWork;
	}
	@XmlElement(name = "hicNo")
	public String getHicNo() {
		return hicNo;
	}

	public void setHicNo(String hicNo) {
		this.hicNo = hicNo;
	}

	@XmlElement(name = "pkOrgarea")
	public String getPkOrgarea() {
		return pkOrgarea;
	}

	public void setPkOrgarea(String pkOrgarea) {
		this.pkOrgarea = pkOrgarea;
	}
	@XmlElement(name = "pkPicate")
	public String getPkPicate() {
		return pkPicate;
	}

	public void setPkPicate(String pkPicate) {
		this.pkPicate = pkPicate;
	}

	@XmlElementAnno
	private String pkSch;
	@XmlElementAnno
	private String ticketNo;
	@XmlElementAnno
	private String pkPv;
	@XmlElementAnno
	private String pkSettle;

	// 费用日期
	@XmlElementAnno
	private String dateCg;
	@XmlElementAnno
	private String dtPaymode;
	@XmlElementAnno
	private String amount;
	@XmlElementAnno
	private String payInfo;
	@XmlElementAnno
	private String reptNo;
	@XmlElementAnno
	private String note;
	@XmlElementAnno
	private String codeEmpPay;
	@XmlElementAnno
	private String codePv;
	@XmlElementAnno
	private String bedNum;
	@XmlElementAnno
	private String beginDatetime;
	@XmlElementAnno
	private String endDatetime;
	@XmlElementAnno
	private String codeDrug;
	@XmlElementAnno
	private String codePy;
	@XmlElementAnno
	private String type;
	@XmlElementAnno
	private String pkDeptExec;
	@XmlElementAnno
	private String insurNo;
	@XmlElementAnno
	private String dataType;
	@XmlElementAnno
	private String pkDeptEx;
	@XmlElementAnno
	private String payOderSn;
	@XmlElementAnno
	private String tradeNo;

	@XmlElementAnno
	private String euStatus;

	@XmlElementAnno
	private String codeStore;
	@XmlElementAnno
	private String pkStore;
	
	@XmlElementAnno
	private String dtBank;
	@XmlElementAnno
	private String bankNo;

	@XmlElement(name = "tradeNo")
	public String getTradeNo() {
		return tradeNo;
	}

	public void setTradeNo(String tradeNo) {
		this.tradeNo = tradeNo;
	}

	@XmlElement(name = "payOderSn")
	public String getPayOderSn() {
		return payOderSn;
	}

	public void setPayOderSn(String payOderSn) {
		this.payOderSn = payOderSn;
	}

	public String getYzStatus() {
		return yzStatus;
	}

	public void setYzStatus(String yzStatus) {
		this.yzStatus = yzStatus;
	}

	public String getPkDeptEx() {
		return pkDeptEx;
	}

	public void setPkDeptEx(String pkDeptEx) {
		this.pkDeptEx = pkDeptEx;
	}

	@XmlElement(name = "dataType")
	public String getDataType() {
		return dataType;
	}

	public void setDataType(String dataType) {
		this.dataType = dataType;
	}

	@XmlElement(name = "insurNo")
	public String getInsurNo() {
		return insurNo;
	}

	public void setInsurNo(String insurNo) {
		this.insurNo = insurNo;
	}

	@XmlElement(name = "pkDeptExec")
	public String getPkDeptExec() {
		return pkDeptExec;
	}

	public void setPkDeptExec(String pkDeptExec) {
		this.pkDeptExec = pkDeptExec;
	}

	@XmlElement(name = "codeDrug")
	public String getCodeDrug() {
		return codeDrug;
	}

	public void setCodeDrug(String codeDrug) {
		this.codeDrug = codeDrug;
	}

	@XmlElement(name = "codePy")
	public String getCodePy() {
		return codePy;
	}

	public void setCodePy(String codePy) {
		this.codePy = codePy;
	}

	@XmlElement(name = "type")
	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	@XmlElement(name = "beginDatetime")
	public String getBeginDatetime() {
		return beginDatetime;
	}

	public void setBeginDatetime(String beginDatetime) {
		this.beginDatetime = beginDatetime;
	}

	@XmlElement(name = "endDatetime")
	public String getEndDatetime() {
		return endDatetime;
	}

	public void setEndDatetime(String endDatetime) {
		this.endDatetime = endDatetime;
	}

	@XmlElement(name = "codePv")
	public String getCodePv() {
		return codePv;
	}

	public void setCodePv(String codePv) {
		this.codePv = codePv;
	}

	@XmlElement(name = "bedNum")
	public String getBedNum() {
		return bedNum;
	}

	public void setBedNum(String bedNum) {
		this.bedNum = bedNum;
	}

	@XmlElement(name = "note")
	public String getNote() {
		return note;
	}

	@XmlElement(name = "codeEmpPay")
	public String getCodeEmpPay() {
		return codeEmpPay;
	}

	public void setCodeEmpPay(String codeEmpPay) {
		this.codeEmpPay = codeEmpPay;
	}

	public void setNote(String note) {
		this.note = note;
	}

	@XmlElement(name = "payInfo")
	public String getPayInfo() {
		return payInfo;
	}

	@XmlElement(name = "reptNo")
	public String getReptNo() {
		return reptNo;
	}

	public void setReptNo(String reptNo) {
		this.reptNo = reptNo;
	}

	public void setPayInfo(String payInfo) {
		this.payInfo = payInfo;
	}

	@XmlElement(name = "amount")
	public String getAmount() {
		return amount;
	}

	public void setAmount(String amount) {
		this.amount = amount;
	}

	@XmlElement(name = "dtPaymode")
	public String getDtPaymode() {
		return dtPaymode;
	}

	public void setDtPaymode(String dtPaymode) {
		this.dtPaymode = dtPaymode;
	}

	@XmlElement(name = "dateCg")
	public String getDateCg() {
		return dateCg;
	}

	public void setDateCg(String dateCg) {
		this.dateCg = dateCg;
	}

	@XmlElement(name = "pkSettle")
	public String getPkSettle() {
		return pkSettle;
	}

	public void setPkSettle(String pkSettle) {
		this.pkSettle = pkSettle;
	}

	@XmlElement(name = "pkPv")
	public String getPkPv() {
		return pkPv;
	}

	public void setPkPv(String pkPv) {
		this.pkPv = pkPv;
	}

	/**根据患者查询未就诊预约挂号信息*/
	// 预约主键
	@XmlElementAnno
	private String pkSchappt;
	// 就诊治疗日期
	@XmlElementAnno
	private String dateAppt;

	@XmlElement(name = "codeOp")
	public String getCodeOp() {
		return codeOp;
	}

	public void setCodeOp(String codeOp) {
		this.codeOp = codeOp;
	}

	// 资源名称
	@XmlElementAnno
	private String schres;

	@XmlElementAnno
	private String flagOp;
	@XmlElementAnno
	private String codeOp;
	@XmlElementAnno
	private String onlyBlFlag;

	@XmlElement(name = "onlyBlFlag")
	public String getOnlyBlFlag() {
		return onlyBlFlag;
	}

	public void setOnlyBlFlag(String onlyBlFlag) {
		this.onlyBlFlag = onlyBlFlag;
	}

	private List<String> euHptypes;

	public List<String> getEuHptypes() {
		return euHptypes;
	}

	public void setEuHptypes(List<String> euHptypes) {
		this.euHptypes = euHptypes;
	}

	@XmlElement(name = "flagOp")
	public String getFlagOp() {
		return flagOp;
	}

	public void setFlagOp(String flagOp) {
		this.flagOp = flagOp;
	}

	/**6.1.查询是否存在就诊卡入参 */
	// 卡类型(身份证: 01)
	@XmlElementAnno
	private String dtCardtype;

	@XmlElement(name = "dtCardtype")
	public String getDtCardtype() {
		return dtCardtype;
	}

	public void setDtCardtype(String dtCardtype) {
		this.dtCardtype = dtCardtype;
	}

	@XmlElement(name = "pkSchappt")
	public String getPkSchappt() {
		return pkSchappt;
	}

	public void setPkSchappt(String pkSchappt) {
		this.pkSchappt = pkSchappt;
	}

	@XmlElement(name = "dateAppt")
	public String getDateAppt() {
		return dateAppt;
	}

	public void setDateAppt(String dateAppt) {
		this.dateAppt = dateAppt;
	}

	@XmlElement(name = "schres")
	public String getSchres() {
		return schres;
	}

	public void setSchres(String schres) {
		this.schres = schres;
	}

	@XmlElement(name = "ticketNo")
	public String getTicketNo() {
		return ticketNo;
	}

	public void setTicketNo(String ticketNo) {
		this.ticketNo = ticketNo;
	}

	@XmlElement(name = "pkSch")
	public String getPkSch() {
		return pkSch;
	}

	public void setPkSch(String pkSch) {
		this.pkSch = pkSch;
	}

	public String getEuSrvtype() {
		return euSrvtype;
	}

	public void setEuSrvtype(String euSrvtype) {
		this.euSrvtype = euSrvtype;
	}

	public String getDateBegin() {
		return dateBegin;
	}

	public void setDateBegin(String dateBegin) {
		this.dateBegin = dateBegin;
	}

	public String getDateEnd() {
		return dateEnd;
	}

	public void setDateEnd(String dateEnd) {
		this.dateEnd = dateEnd;
	}

	@XmlElement(name = "pkPi")
	public String getPkPi() {
		return pkPi;
	}

	public void setPkPi(String pkPi) {
		this.pkPi = pkPi;
	}

	@XmlElement(name = "codePi")
	public String getCodePi() {
		return codePi;
	}

	public void setCodePi(String codePi) {
		this.codePi = codePi;
	}

	@XmlElement(name = "idno")
	public String getIdno() {
		return idno;
	}

	public void setIdno(String idno) {
		this.idno = idno;
	}

	@XmlElement(name = "codeIp")
	public String getCodeIp() {
		return codeIp;
	}

	public void setCodeIp(String codeIp) {
		this.codeIp = codeIp;
	}

	@XmlElement(name = "codeCard")
	public String getCodeCard() {
		return codeCard;
	}

	public void setCodeCard(String codeCard) {
		this.codeCard = codeCard;
	}

	@XmlElement(name = "namePi")
	public String getNamePi() {
		return namePi;
	}

	public void setNamePi(String namePi) {
		this.namePi = namePi;
	}

	@XmlElement(name = "dtIdtype")
	public String getDtIdtype() {
		return dtIdtype;
	}

	public void setDtIdtype(String dtIdtype) {
		this.dtIdtype = dtIdtype;
	}

	@XmlElement(name = "idNo")
	public String getIdNo() {
		return idNo;
	}

	public void setIdNo(String idNo) {
		this.idNo = idNo;
	}

	@XmlElement(name = "mobile")
	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	@XmlElement(name = "dtSex")
	public String getDtSex() {
		return dtSex;
	}

	public void setDtSex(String dtSex) {
		this.dtSex = dtSex;
	}

	@XmlElement(name = "pkOrg")
	public String getPkOrg() {
		return pkOrg;
	}

	public void setPkOrg(String pkOrg) {
		this.pkOrg = pkOrg;
	}

	@XmlElement(name = "pkDept")
	public String getPkDept() {
		return pkDept;
	}

	public void setPkDept(String pkDept) {
		this.pkDept = pkDept;
	}

	@XmlElement(name = "codeDept")
	public String getCodeDept() {
		return codeDept;
	}

	public void setCodeDept(String codeDept) {
		this.codeDept = codeDept;
	}

	@XmlElement(name = "codeEmp")
	public String getCodeEmp() {
		return codeEmp;
	}

	public void setCodeEmp(String codeEmp) {
		this.codeEmp = codeEmp;
	}

	@XmlElement(name = "pkEmp")
	public String getPkEmp() {
		return pkEmp;
	}

	public void setPkEmp(String pkEmp) {
		this.pkEmp = pkEmp;
	}

	@XmlElement(name = "codeOrg")
	public String getCodeOrg() {
		return codeOrg;
	}

	public void setCodeOrg(String codeOrg) {
		this.codeOrg = codeOrg;
	}

	@XmlElement(name = "euStatus")
	public String getEuStatus() {
		return euStatus;
	}

	public void setEuStatus(String euStatus) {
		this.euStatus = euStatus;
	}

	@XmlElement(name = "codeStore")
	public String getCodeStore() {
		return codeStore;
	}

	public void setCodeStore(String codeStore) {
		this.codeStore = codeStore;
	}

	@XmlElement(name = "pkStore")
	public String getPkStore() {
		return pkStore;
	}

	public void setPkStore(String pkStore) {
		this.pkStore = pkStore;
	}

	@XmlElement(name = "dtApptype")
	public String getDtApptype() {
		return dtApptype;
	}

	public void setDtApptype(String dtApptype) {
		this.dtApptype = dtApptype;
	}
	@XmlElement(name = "dtBank")
	public String getDtBank() {
		return dtBank;
	}

	public void setDtBank(String dtBank) {
		this.dtBank = dtBank;
	}
	@XmlElement(name = "bankNo")
	public String getBankNo() {
		return bankNo;
	}

	public void setBankNo(String bankNo) {
		this.bankNo = bankNo;
	}

	/**同步床位的参数 */
	// 上次同步时间
	@XmlElementAnno
	private String dateLast;

	//床位类型
	@XmlElementAnno
	private String dtBedtype;

	//所属病区
	@XmlElementAnno
	private String pkWard;

	@XmlElement(name = "dateLast")
	public String getDateLast() {
		return dateLast;
	}

	public void setDateLast(String dateLast) {
		this.dateLast = dateLast;
	}

	@XmlElement(name = "dtBedtype")
	public String getDtBedtype() {
		return dtBedtype;
	}

	public void setDtBedtype(String dtBedtype) {
		this.dtBedtype = dtBedtype;
	}

	@XmlElement(name = "pkWard")
	public String getPkWard() {
		return pkWard;
	}

	public void setPkWard(String pkWard) {
		this.pkWard = pkWard;
	}
	@XmlElement(name = "registerDate")
	public String getRegisterDate() {
		return registerDate;
	}

	public void setRegisterDate(String registerDate) {
		this.registerDate = registerDate;
	}
	@XmlElement(name = "registerTime")
	public String getRegisterTime() {
		return registerTime;
	}

	public void setRegisterTime(String registerTime) {
		this.registerTime = registerTime;
	}


}
