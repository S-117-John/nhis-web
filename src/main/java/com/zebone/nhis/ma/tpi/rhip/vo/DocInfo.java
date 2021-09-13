
package com.zebone.nhis.ma.tpi.rhip.vo;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.datatype.XMLGregorianCalendar;


@XmlRootElement(name = "DocInfo")
@XmlAccessorType(XmlAccessType.FIELD)
public class DocInfo {

    @XmlElement(name = "RecordClassifying", required = true)
    private String recordClassifying;
    @XmlElement(name = "RecordTitle", required = true)
    private String recordTitle;
    @XmlElement(name = "EffectiveTime", required = true)
    private XMLGregorianCalendar effectiveTime;
    //@XmlElement(name = "Authororganization", required = true)
    //private String authororganization;
    @XmlElement(name = "Authororganization", required = true)
    private Authororganization authororganization;
    @XmlElement(name = "SourceID", required = true)
    private String sourceID;
    @XmlElement(name = "VersionNumber", required = true)
    private String versionNumber;
    @XmlElement(name = "AuthorID", required = true)
    private String authorID;
    @XmlElement(name = "Author", required = true)
    private String author;
    @XmlElement(name = "SystemTime", required = true)
    private XMLGregorianCalendar systemTime;
    
	public void setRecordClassifying(String recordClassifying) {
		this.recordClassifying = recordClassifying;
	}
	public void setRecordTitle(String recordTitle) {
		this.recordTitle = recordTitle;
	}
	public void setEffectiveTime(XMLGregorianCalendar effectiveTime) {
		this.effectiveTime = effectiveTime;
	}


	public void setAuthororganization(Authororganization authororganization) {
		this.authororganization = authororganization;
	}
	public void setSourceID(String sourceID) {
		this.sourceID = sourceID;
	}
	public void setVersionNumber(String versionNumber) {
		this.versionNumber = versionNumber;
	}
	public void setAuthorID(String authorID) {
		this.authorID = authorID;
	}
	public void setAuthor(String author) {
		this.author = author;
	}
	public void setSystemTime(XMLGregorianCalendar systemTime) {
		this.systemTime = systemTime;
	}
	public String getRecordClassifying() {
		return recordClassifying;
	}
	public String getRecordTitle() {
		return recordTitle;
	}
	public XMLGregorianCalendar getEffectiveTime() {
		return effectiveTime;
	}
	public Authororganization getAuthororganization() {
		return authororganization;
	}
	public String getSourceID() {
		return sourceID;
	}
	public String getVersionNumber() {
		return versionNumber;
	}
	public String getAuthorID() {
		return authorID;
	}
	public String getAuthor() {
		return author;
	}
	public XMLGregorianCalendar getSystemTime() {
		return systemTime;
	}

    
	
}
