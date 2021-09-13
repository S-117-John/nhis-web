package com.zebone.nhis.webservice.zhongshan.vo;

import javax.xml.bind.annotation.XmlElement;

public class CriticalXmlBody {
	private String codePi;
	private String pvType;
	private String codePv;
	private String namePi;
	private String subject;
	private String content;
	private String reply;
	private String level;
	private String codeDept;
	private String nameDept;
	private String codeSender;
	private String nameSender;
	private String note;
	private String srccode;
	private CriticalXmlCust cust;
	@XmlElement(name="code_pi")
	public String getCodePi() {
		return codePi;
	}
	public void setCodePi(String codePi) {
		this.codePi = codePi;
	}
	@XmlElement(name="pvtype")
	public String getPvType() {
		return pvType;
	}
	public void setPvType(String pvType) {
		this.pvType = pvType;
	}
	@XmlElement(name="code_pv")
	public String getCodePv() {
		return codePv;
	}
	public void setCodePv(String codePv) {
		this.codePv = codePv;
	}
	@XmlElement(name="name_pi")
	public String getNamePi() {
		return namePi;
	}
	public void setNamePi(String namePi) {
		this.namePi = namePi;
	}
	@XmlElement(name="subject")
	public String getSubject() {
		return subject;
	}
	public void setSubject(String subject) {
		this.subject = subject;
	}
	@XmlElement(name="content")
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	@XmlElement(name="reply")
	public String getReply() {
		return reply;
	}
	public void setReply(String reply) {
		this.reply = reply;
	}
	@XmlElement(name="level")
	public String getLevel() {
		return level;
	}
	public void setLevel(String level) {
		this.level = level;
	}
	@XmlElement(name="code_dept")
	public String getCodeDept() {
		return codeDept;
	}
	public void setCodeDept(String codeDept) {
		this.codeDept = codeDept;
	}
	@XmlElement(name="name_dept")
	public String getNameDept() {
		return nameDept;
	}
	public void setNameDept(String nameDept) {
		this.nameDept = nameDept;
	}
	@XmlElement(name="code_sender")
	public String getCodeSender() {
		return codeSender;
	}
	public void setCodeSender(String codeSender) {
		this.codeSender = codeSender;
	}
	@XmlElement(name="name_sender")
	public String getNameSender() {
		return nameSender;
	}
	public void setNameSender(String nameSender) {
		this.nameSender = nameSender;
	}
	@XmlElement(name="note")
	public String getNote() {
		return note;
	}
	public void setNote(String note) {
		this.note = note;
	}
	@XmlElement(name="srccode")
	public String getSrccode() {
		return srccode;
	}
	public void setSrccode(String srccode) {
		this.srccode = srccode;
	}
	@XmlElement(name="cust")
	public CriticalXmlCust getCust() {
		return cust;
	}
	public void setCust(CriticalXmlCust cust) {
		this.cust = cust;
	}
	
}
