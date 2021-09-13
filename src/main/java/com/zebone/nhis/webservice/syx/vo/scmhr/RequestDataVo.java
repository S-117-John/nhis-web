package com.zebone.nhis.webservice.syx.vo.scmhr;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * 华润发送数据vo
 * @author jd
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)  
@XmlRootElement(name="ROOT")
public class RequestDataVo  {
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
	private ConsisPrescMstvw consisPrescMstvw;
	
	@XmlElement(name="CONSIS_PRESC_MSTVW")
	private List<ConsisPrescMstvw> consisPresList;

	public ConsisPrescMstvw getConsisPrescMstvw() {
		if(consisPrescMstvw==null) consisPrescMstvw=new ConsisPrescMstvw();
		return consisPrescMstvw;
	}

	public void setConsisPrescMstvw(ConsisPrescMstvw consisPrescMstvw) {
		this.consisPrescMstvw = consisPrescMstvw;
	}

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

	public List<ConsisPrescMstvw> getConsisPresList() {
		return consisPresList;
	}

	public void setConsisPresList(List<ConsisPrescMstvw> consisPresList) {
		this.consisPresList = consisPresList;
	}
	
	
}
