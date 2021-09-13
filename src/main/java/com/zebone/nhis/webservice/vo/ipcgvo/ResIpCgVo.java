package com.zebone.nhis.webservice.vo.ipcgvo;

import javax.xml.bind.annotation.XmlElement;

/**
 * 7.15.查询住院费用明细
 * @ClassName: ResIpCgVo   
 * @Description:TODO(这里用一句话描述这个类的作用)   
 * @author: zhangheng 
 * @date: 2019年4月20日 下午4:49:39     
 * @Copyright: 2019
 */
public class ResIpCgVo {
	// 患者自付金额
	private String amnoutPi;
	// 总金额
	private String amount;
	// 费用分类
	private String catename;
	// 记费日期
	private String dateCg;
	// 收费项目名称
	private String nameCg;
	// 就诊主键
	private String pkPv;
	// 单价
	private String price;
	// 数量
	private String quan;
	// 单位名称
	private String unitname;

	@XmlElement(name = "amnoutPi")
	public String getAmnoutPi() {
		return amnoutPi;
	}

	public void setAmnoutPi(String amnoutPi) {
		this.amnoutPi = amnoutPi;
	}

	@XmlElement(name = "amount")
	public String getAmount() {
		return amount;
	}

	public void setAmount(String amount) {
		this.amount = amount;
	}

	@XmlElement(name = "catename")
	public String getCatename() {
		return catename;
	}

	public void setCatename(String catename) {
		this.catename = catename;
	}

	@XmlElement(name = "dateCg")
	public String getDateCg() {
		return dateCg;
	}

	public void setDateCg(String dateCg) {
		this.dateCg = dateCg;
	}

	@XmlElement(name = "nameCg")
	public String getNameCg() {
		return nameCg;
	}

	public void setNameCg(String nameCg) {
		this.nameCg = nameCg;
	}

	@XmlElement(name = "pkPv")
	public String getPkPv() {
		return pkPv;
	}

	public void setPkPv(String pkPv) {
		this.pkPv = pkPv;
	}

	@XmlElement(name = "price")
	public String getPrice() {
		return price;
	}

	public void setPrice(String price) {
		this.price = price;
	}

	@XmlElement(name = "quan")
	public String getQuan() {
		return quan;
	}

	public void setQuan(String quan) {
		this.quan = quan;
	}

	@XmlElement(name = "unitname")
	public String getUnitname() {
		return unitname;
	}

	public void setUnitname(String unitname) {
		this.unitname = unitname;
	}

}
