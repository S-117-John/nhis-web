package com.zebone.nhis.webservice.vo;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "pageInfo")
@XmlAccessorType(XmlAccessType.FIELD)
public class LbPageInfo {
	/**
     *起始页
     */
    @XmlElement(name="pageStart")
    private String pageStart;
    /**
     * 每页记录数
     */
    @XmlElement(name="pageSize")
    private String pageSize;
	public String getPageStart() {
		return pageStart;
	}
	public void setPageStart(String pageStart) {
		this.pageStart = pageStart;
	}
	public String getPageSize() {
		return pageSize;
	}
	public void setPageSize(String pageSize) {
		this.pageSize = pageSize;
	}
    
}
