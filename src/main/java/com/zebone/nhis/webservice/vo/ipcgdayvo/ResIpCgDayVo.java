package com.zebone.nhis.webservice.vo.ipcgdayvo;

import javax.xml.bind.annotation.XmlElement;

/**
 * 7.16.查询住院一日清单
 * @ClassName: ResIpCgDayVo   
 * @Description:TODO(这里用一句话描述这个类的作用)   
 * @author: zhangheng 
 * @date: 2019年4月22日 上午10:51:54     
 * @Copyright: 2019
 */
public class ResIpCgDayVo {
	// 患者自付金额
	private String amnoutPi;
	// 总金额
	private String amount;
	// 费用分类
	private String catename;
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
