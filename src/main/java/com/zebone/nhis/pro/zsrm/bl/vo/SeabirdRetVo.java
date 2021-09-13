package com.zebone.nhis.pro.zsrm.bl.vo;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name="Response")
public class SeabirdRetVo {
    @XmlElement(name = "resultCode")
	private String resultCode;
    
    @XmlElement(name = "resultDesc")
	private String resultDesc;
    
    @XmlElement(name = "pageNo")
	private String pageNo;
    
    @XmlElement(name = "hasNextPage")
	private String hasNextPage;

    @XmlElement(name = "list")
    private List<SeabirdOrder> list;
    

	public String getResultCode() {
		return resultCode;
	}

	public void setResultCode(String resultCode) {
		this.resultCode = resultCode;
	}

	public String getResultDesc() {
		return resultDesc;
	}

	public void setResultDesc(String resultDesc) {
		this.resultDesc = resultDesc;
	}

	public String getPageNo() {
		return pageNo;
	}

	public void setPageNo(String pageNo) {
		this.pageNo = pageNo;
	}

	public String getHasNextPage() {
		return hasNextPage;
	}

	public void setHasNextPage(String hasNextPage) {
		this.hasNextPage = hasNextPage;
	}

	public List<SeabirdOrder> getList() {
		return list;
	}

	public void setList(List<SeabirdOrder> list) {
		this.list = list;
	}

}
