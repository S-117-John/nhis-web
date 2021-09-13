/**
  * Copyright 2019 bejson.com 
  */
package com.zebone.nhis.ma.pub.platform.syx.vo;

import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlType;

/**
 * Auto-generated: 2019-04-22 15:49:53
 *
 * @author bejson.com (i@bejson.com)
 * @website http://www.bejson.com/java2pojo/
 */
@XmlType(name = "LINKMAN_LIST")
public class LinkmanList {
//	@XmlElement(name = "LINKMAN")
//    private Linkman linkman;
	@XmlElementWrapper(name = "LINKMAN_LIST")  
	@XmlElement(name = "LINKMAN") 
	private List<Linkman> linkmans;
//	public Linkman getLinkman() {
//		return linkman;
//	}
//
//	public void setLinkman(Linkman linkman) {
//		this.linkman = linkman;
//	}

	public List<Linkman> getLinkmans() {
		return linkmans;
	}

	public void setLinkmans(List<Linkman> linkmans) {
		this.linkmans = linkmans;
	}
    

}