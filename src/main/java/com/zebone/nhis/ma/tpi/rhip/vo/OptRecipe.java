package com.zebone.nhis.ma.tpi.rhip.vo;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Opt_Recipe门诊处方记录表
 * @author chengjia
 *
 */
@XmlRootElement(name = "Opt_Recipe")
@XmlAccessorType(XmlAccessType.FIELD)
public class OptRecipe {
    @XmlElement(name = "JZLSH", required = true)
    protected String jzlsh;
    @XmlElement(name = "CFID", required = true)
    protected String cfid;
    @XmlElement(name = "CFLY", required = true)
    protected String cfly;
    @XmlElement(name = "KFKSDM", required = true)
    protected String kfksdm;
    @XmlElement(name = "CFZT", required = true)
    protected String cfzt;
    @XmlElement(name = "CFLB", required = true)
    protected String cflb;
    @XmlElement(name = "KFSJ", required = true)
    protected String kfsj;
    @XmlElement(name = "FYSJ")
    protected String fysj;
    @XmlElement(name = "BZ")
    protected String bz;
    @XmlElement(name = "HZXM")
    protected String hzxm;
    @XmlElement(name = "XBDM")
    protected String xbdm;
    @XmlElement(name = "CSRQ")
    protected String csrq;
    @XmlElement(name = "YZXDYSGH")
    protected String yzxdysgh;
    @XmlElement(name = "YZXDYSXM")
    protected String yzxdysxm;
    @XmlElement(name = "GHLSH")
    protected String ghlsh;
    @XmlElement(name = "KFKSMC")
    protected String kfksmc;    
    
    
    @XmlAttribute(name = "Name")
    protected String name;
    
    @XmlElementWrapper(name="Opt_RecipeDetails")
    @XmlElement(name = "Opt_RecipeDetail")  
    protected List<OptRecipeDetail> optRecipeDetails;

	public String getJzlsh() {
		return jzlsh;
	}

	public void setJzlsh(String jzlsh) {
		this.jzlsh = jzlsh;
	}

	public String getCfid() {
		return cfid;
	}

	public void setCfid(String cfid) {
		this.cfid = cfid;
	}

	public String getCfly() {
		return cfly;
	}

	public void setCfly(String cfly) {
		this.cfly = cfly;
	}

	public String getKfksdm() {
		return kfksdm;
	}

	public void setKfksdm(String kfksdm) {
		this.kfksdm = kfksdm;
	}

	public String getCfzt() {
		return cfzt;
	}

	public void setCfzt(String cfzt) {
		this.cfzt = cfzt;
	}

	public String getCflb() {
		return cflb;
	}

	public void setCflb(String cflb) {
		this.cflb = cflb;
	}

	public String getKfsj() {
		return kfsj;
	}

	public void setKfsj(String kfsj) {
		this.kfsj = kfsj;
	}

	public String getFysj() {
		return fysj;
	}

	public void setFysj(String fysj) {
		this.fysj = fysj;
	}

	public String getBz() {
		return bz;
	}

	public void setBz(String bz) {
		this.bz = bz;
	}

	public String getHzxm() {
		return hzxm;
	}

	public void setHzxm(String hzxm) {
		this.hzxm = hzxm;
	}

	public String getXbdm() {
		return xbdm;
	}

	public void setXbdm(String xbdm) {
		this.xbdm = xbdm;
	}

	public String getCsrq() {
		return csrq;
	}

	public void setCsrq(String csrq) {
		this.csrq = csrq;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	
	public List<OptRecipeDetail> getOptRecipeDetails() {
		return optRecipeDetails;
	}

	public void setOptRecipeDetails(List<OptRecipeDetail> optRecipeDetails) {
		this.optRecipeDetails = optRecipeDetails;
	}

	public String getSourceId() {
		return cfid;
	}


	public String getYzxdysgh() {
		return yzxdysgh;
	}

	public void setYzxdysgh(String yzxdysgh) {
		this.yzxdysgh = yzxdysgh;
	}

	public String getYzxdysxm() {
		return yzxdysxm;
	}

	public void setYzxdysxm(String yzxdysxm) {
		this.yzxdysxm = yzxdysxm;
	}

	public String getGhlsh() {
		return ghlsh;
	}

	public void setGhlsh(String ghlsh) {
		this.ghlsh = ghlsh;
	}

	public String getKfksmc() {
		return kfksmc;
	}

	public void setKfksmc(String kfksmc) {
		this.kfksmc = kfksmc;
	}

    
}
