package com.zebone.nhis.ma.pub.platform.send.impl.syx.vo;

import com.zebone.nhis.common.module.cn.ipdw.CnOrder;

/**
 * 发送医嘱核对消息对象
 * @author yangxue
 *
 */
public class OrderCheckMsgVo extends CnOrder{
	
	private String pkDeptNsCur;//当前就诊病区
	private String bedNo;//床号
	private String namePi;//患者姓名
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
	
}
