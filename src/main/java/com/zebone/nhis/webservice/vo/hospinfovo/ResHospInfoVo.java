package com.zebone.nhis.webservice.vo.hospinfovo;

import javax.xml.bind.annotation.XmlElement;

/**
 * 查询患者住院就诊信息 vo
 * @ClassName: ResHospInfoVo   
 * @Description:TODO(这里用一句话描述这个类的作用)   
 * @author: zhangheng 
 * @date: 2019年4月18日 下午5:21:17     
 * @Copyright: 2019
 */
public class ResHospInfoVo {
	// 科室
	private String pkDept;
	// 住院号[可选]
	private String codeIp;
	// 患者唯一标识
	private String pkPi;
	// 床号
	private String bedNo;
	// 就诊开始日期
	private String dateBegin;
	// 就诊结束日期
	private String dateEnd;
	// 当前就诊科室名称
	private String nameDept;
	// 当前所在病区名称
	private String nameDeptNs;
	// 责任护士名称
	private String nameEmpNs;
	// 主治医生名称
	private String nameEmpPhy;
	// 主医保计划名称
	private String nameInsu;
	// 患者分类名称
	private String namePicate;
	// 当前所在病区唯一标识
	private String pkDeptNs;
	// 责任护士唯一标识
	private String pkEmpNs;
	// 主治医生唯一标识
	private String pkEmpPhy;
	// 主医保计划唯一标识
	private String pkInsu;
	// 患者分类唯一标识
	private String pkPicate;
	
	//就诊主键pkPv
	private String pkPv;
	
	private String euStatus;
    
	@XmlElement(name = "euStatus")
	public String getEuStatus() {
		return euStatus;
	}

	public void setEuStatus(String euStatus) {
		this.euStatus = euStatus;
	}

	@XmlElement(name = "pkPv")
	public String getPkPv() {
		return pkPv;
	}

	public void setPkPv(String pkPv) {
		this.pkPv = pkPv;
	}

	@XmlElement(name = "bedNo")
	public String getBedNo() {
		return bedNo;
	}

	public void setBedNo(String bedNo) {
		this.bedNo = bedNo;
	}

	@XmlElement(name = "dateBegin")
	public String getDateBegin() {
		return dateBegin;
	}

	public void setDateBegin(String dateBegin) {
		this.dateBegin = dateBegin;
	}

	@XmlElement(name = "dateEnd")
	public String getDateEnd() {
		return dateEnd;
	}

	public void setDateEnd(String dateEnd) {
		this.dateEnd = dateEnd;
	}

	@XmlElement(name = "nameDept")
	public String getNameDept() {
		return nameDept;
	}

	public void setNameDept(String nameDept) {
		this.nameDept = nameDept;
	}

	@XmlElement(name = "nameDeptNs")
	public String getNameDeptNs() {
		return nameDeptNs;
	}

	public void setNameDeptNs(String nameDeptNs) {
		this.nameDeptNs = nameDeptNs;
	}

	@XmlElement(name = "nameEmpNs")
	public String getNameEmpNs() {
		return nameEmpNs;
	}

	public void setNameEmpNs(String nameEmpNs) {
		this.nameEmpNs = nameEmpNs;
	}

	@XmlElement(name = "nameEmpPhy")
	public String getNameEmpPhy() {
		return nameEmpPhy;
	}

	public void setNameEmpPhy(String nameEmpPhy) {
		this.nameEmpPhy = nameEmpPhy;
	}

	@XmlElement(name = "nameInsu")
	public String getNameInsu() {
		return nameInsu;
	}

	public void setNameInsu(String nameInsu) {
		this.nameInsu = nameInsu;
	}

	@XmlElement(name = "namePicate")
	public String getNamePicate() {
		return namePicate;
	}

	public void setNamePicate(String namePicate) {
		this.namePicate = namePicate;
	}

	@XmlElement(name = "pkDeptNs")
	public String getPkDeptNs() {
		return pkDeptNs;
	}

	public void setPkDeptNs(String pkDeptNs) {
		this.pkDeptNs = pkDeptNs;
	}

	@XmlElement(name = "pkEmpNs")
	public String getPkEmpNs() {
		return pkEmpNs;
	}

	public void setPkEmpNs(String pkEmpNs) {
		this.pkEmpNs = pkEmpNs;
	}

	@XmlElement(name = "pkEmpPhy")
	public String getPkEmpPhy() {
		return pkEmpPhy;
	}

	public void setPkEmpPhy(String pkEmpPhy) {
		this.pkEmpPhy = pkEmpPhy;
	}

	@XmlElement(name = "pkInsu")
	public String getPkInsu() {
		return pkInsu;
	}

	public void setPkInsu(String pkInsu) {
		this.pkInsu = pkInsu;
	}

	@XmlElement(name = "pkPicate")
	public String getPkPicate() {
		return pkPicate;
	}

	public void setPkPicate(String pkPicate) {
		this.pkPicate = pkPicate;
	}

	@XmlElement(name = "codeIp")
	public String getCodeIp() {
		return codeIp;
	}

	public void setCodeIp(String codeIp) {
		this.codeIp = codeIp;
	}

	@XmlElement(name = "pkPi")
	public String getPkPi() {
		return pkPi;
	}

	public void setPkPi(String pkPi) {
		this.pkPi = pkPi;
	}

	@XmlElement(name = "pkDept")
	public String getPkDept() {
		return pkDept;
	}

	public void setPkDept(String pkDept) {
		this.pkDept = pkDept;
	}

}
