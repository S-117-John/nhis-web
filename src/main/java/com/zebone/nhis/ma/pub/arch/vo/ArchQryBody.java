package com.zebone.nhis.ma.pub.arch.vo;

import javax.xml.bind.annotation.XmlElement;

public class ArchQryBody {
	
	private String visitcode;//>123123123</visitcode>  --就诊编码
	private String paticode;//<paticode>13123123</paticode>   --患者编码
	private String opcode;//<opcode></opcode>   --门诊号码
	private String ipcode;//<ipcode></ipcode>     --住院号码
	private String visitdate;//<visitdate>2017-11-30</visitdate>  --就诊日期
	private String visittype;//>3</visittype> --就诊类型
	private String datestart;//开始时间
	private String dateend;//结束时间
	private String patiname;//患者姓名
	
	@XmlElement(name="patiname")
	public String getPatiname() {
		return patiname;
	}
	public void setPatiname(String patiname) {
		this.patiname = patiname;
	}
	@XmlElement(name="datestart")
	public String getDatestart() {
		return datestart;
	}
	public void setDatestart(String datestart) {
		this.datestart = datestart;
	}
	
	@XmlElement(name="dateend")
	public String getDateend() {
		return dateend;
	}
	public void setDateend(String dateend) {
		this.dateend = dateend;
	}
	
	@XmlElement(name="visitcode")
	public String getVisitcode() {
		return visitcode;
	}
	public void setVisitcode(String visitcode) {
		this.visitcode = visitcode;
	}
	@XmlElement(name="paticode")
	public String getPaticode() {
		return paticode;
	}
	public void setPaticode(String paticode) {
		this.paticode = paticode;
	}
	@XmlElement(name="opcode")
	public String getOpcode() {
		return opcode;
	}
	public void setOpcode(String opcode) {
		this.opcode = opcode;
	}
	@XmlElement(name="ipcode")
	public String getIpcode() {
		return ipcode;
	}
	public void setIpcode(String ipcode) {
		this.ipcode = ipcode;
	}
	@XmlElement(name="visitdate")
	public String getVisitdate() {
		return visitdate;
	}
	public void setVisitdate(String visitdate) {
		this.visitdate = visitdate;
	}
	@XmlElement(name="visittype")
	public String getVisittype() {
		return visittype;
	}
	public void setVisittype(String visittype) {
		this.visittype = visittype;
	}

}
