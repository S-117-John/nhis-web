package com.zebone.nhis.pro.zsba.ex.vo;

import com.zebone.nhis.common.module.cn.ipdw.CnOrder;

/**
 * 医嘱核对主医嘱列表,继承医嘱实体，扩展非医嘱实体字段
 * @author yangxue
 *
 */
public class OrderCheckVO extends CnOrder{
	
	private String pkDeptNsCur;//当前就诊病区
	private String bedNo;//床号
	private String namePi;//患者姓名
	private String valAtt;//危重
	private String nameUnitDosage;//剂量单位名称
	private String nameUnit;//用量单位名称
	private String nameUsage;//用法名称
	private String nameUnitCg;//计费单位名称
	private String nameFreq;//频次名称
	private String newbornName;//新生儿姓名
	private String nameDeptExec;//执行科室名称
	private String sign;
	private Integer cnt;//周期执行次数
	private String euCycle;//周期类型0按天1按周2按小时
	private String nameSupplyItem;//用法附加费名称
	private String nameTubeItem;//检验容器费用名称
	private String nameOrdtype;//医嘱类型名称
	private String nameHp;//医保名称
	private String euBoil;//代煎方式
	private String flagSpec;//特诊标志
	private String birthDate;//出生日期
	private Integer countOcc;//执行单数量
	private String pkSupplycate;//用法分类
	private String labName;//检验标本名称
	/**
	 * 注射药类型
	 */
	private String dtInjtype;
	
	public String getNameTubeItem() {
		return nameTubeItem;
	}
	public void setNameTubeItem(String nameTubeItem) {
		this.nameTubeItem = nameTubeItem;
	}
	public String getNameSupplyItem() {
		return nameSupplyItem;
	}
	public void setNameSupplyItem(String nameSupplyItem) {
		this.nameSupplyItem = nameSupplyItem;
	}
	public String getEuCycle() {
		return euCycle;
	}
	public void setEuCycle(String euCycle) {
		this.euCycle = euCycle;
	}
	public Integer getCnt() {
		return cnt;
	}
	public void setCnt(Integer cnt) {
		this.cnt = cnt;
	}
	public String getSign() {
		return sign;
	}
	public void setSign(String sign) {
		this.sign = sign;
	}
	public String getPkDeptNsCur() {
		return pkDeptNsCur;
	}
	public void setPkDeptNsCur(String pkDeptNsCur) {
		this.pkDeptNsCur = pkDeptNsCur;
	}
	public String getBedNo() {
		return bedNo;
	}
	public void setBedNo(String bedNo) {
		this.bedNo = bedNo;
	}
	public String getNamePi() {
		return namePi;
	}
	public void setNamePi(String namePi) {
		this.namePi = namePi;
	}
	public String getNameUnitDosage() {
		return nameUnitDosage;
	}
	public void setNameUnitDosage(String nameUnitDosage) {
		this.nameUnitDosage = nameUnitDosage;
	}
	public String getNameUnit() {
		return nameUnit;
	}
	public void setNameUnit(String nameUnit) {
		this.nameUnit = nameUnit;
	}
	public String getNameUsage() {
		return nameUsage;
	}
	public void setNameUsage(String nameUsage) {
		this.nameUsage = nameUsage;
	}
	public String getNameUnitCg() {
		return nameUnitCg;
	}
	public void setNameUnitCg(String nameUnitCg) {
		this.nameUnitCg = nameUnitCg;
	}
	public String getNameFreq() {
		return nameFreq;
	}
	public void setNameFreq(String nameFreq) {
		this.nameFreq = nameFreq;
	}
	public String getNewbornName() {
		return newbornName;
	}
	public void setNewbornName(String newbornName) {
		this.newbornName = newbornName;
	}
	public String getNameDeptExec() {
		return nameDeptExec;
	}
	public void setNameDeptExec(String nameDeptExec) {
		this.nameDeptExec = nameDeptExec;
	}
	public String getNameOrdtype() {
		return nameOrdtype;
	}
	public void setNameOrdtype(String nameOrdtype) {
		this.nameOrdtype = nameOrdtype;
	}
	public String getNameHp() {
		return nameHp;
	}
	public void setNameHp(String nameHp) {
		this.nameHp = nameHp;
	}
	public String getEuBoil() {
		return euBoil;
	}
	public void setEuBoil(String euBoil) {
		this.euBoil = euBoil;
	}
	public String getFlagSpec() {
		return flagSpec;
	}
	public void setFlagSpec(String flagSpec) {
		this.flagSpec = flagSpec;
	}
	public String getBirthDate() {
		return birthDate;
	}
	public void setBirthDate(String birthDate) {
		this.birthDate = birthDate;
	}
	public String getDtInjtype() {
		return dtInjtype;
	}
	public void setDtInjtype(String dtInjtype) {
		this.dtInjtype = dtInjtype;
	}
	public String getValAtt() {
		return valAtt;
	}
	public void setValAtt(String valAtt) {
		this.valAtt = valAtt;
	}
	public Integer getCountOcc() {
		return countOcc;
	}
	public void setCountOcc(Integer countOcc) {
		this.countOcc = countOcc;
	}
	public String getPkSupplycate() {return pkSupplycate;}
	public void setPkSupplycate(String pkSupplycate) {this.pkSupplycate = pkSupplycate;}
	public String getLabName() { return labName; }
	public void setLabName(String labName) { this.labName = labName; }
}
