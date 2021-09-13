package com.zebone.nhis.ma.pub.platform.syx.vo.scmhr;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlAccessorType(XmlAccessType.FIELD)  
@XmlRootElement(name="ROOT")
public class RequestHrVo {
	/*
	 * 系统名称
	 */
	@XmlElement(name="OPSYSTEM")
	private String opsystem;
	/*
	 * 操作终端窗口号
	 */
	@XmlElement(name="OPWINID")
    private String opwinid;
	/*
	 * 操作代码
	 */
	@XmlElement(name="OPTYPE")
    private String optype;
	/*
	 * 终端 ip 地址
	 */
	@XmlElement(name="OPIP")
    private String opip;
	/*
	 * 人员编码
	 */
	@XmlElement(name="OPMANNO")
    private String opmanno;
	/*
	 * 人员名称
	 */
	@XmlElement(name="OPMANNAME")
    private String opmanname;
	@XmlElement(name="CONSIS_PRESC_MSTVW")
	private List<ConsisPrescMstvw> prescMstvws;

	public String getOpsystem() {
		return opsystem;
	}

	public void setOpsystem(String opsystem) {
		this.opsystem = opsystem;
	}

	public String getOpwinid() {
		return opwinid;
	}

	public void setOpwinid(String opwinid) {
		this.opwinid = opwinid;
	}

	public String getOptype() {
		return optype;
	}

	public void setOptype(String optype) {
		this.optype = optype;
	}

	public String getOpip() {
		return opip;
	}

	public void setOpip(String opip) {
		this.opip = opip;
	}

	public String getOpmanno() {
		return opmanno;
	}

	public void setOpmanno(String opmanno) {
		this.opmanno = opmanno;
	}

	public String getOpmanname() {
		return opmanname;
	}

	public void setOpmanname(String opmanname) {
		this.opmanname = opmanname;
	}

	public List<ConsisPrescMstvw> getPrescMstvws() {
		return prescMstvws;
	}

	public void setPrescMstvws(List<ConsisPrescMstvw> prescMstvws) {
		this.prescMstvws = prescMstvws;
	}
	
	
}
