package com.zebone.nhis.webservice.vo;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
@XmlRootElement(name = "recipes")
@XmlAccessorType(XmlAccessType.FIELD)
public class LbRecipesVo {
	@XmlElement(name="recipeType")
	private String recipeType;

	private String recipeId;
	@XmlElement(name="recipeName")
	private String recipeName;
	@XmlElement(name="recipeTime")
	private String recipeTime;
	@XmlElement(name="recipeFee")
	private String recipeFee;
	//执行科室
	@XmlElement(name="execDept")
	private String execDept;
	//执行位置
	@XmlElement(name="execLocation")
	private String execLocation;
	//窗口
	@XmlElement(name="wicket")
	private String wicket;
	
	/**
	 *费用明细响应
	 */
	@XmlElement(name = "items")
    public List<LbBlCgIpVo> lbBlCgIpVo;

	public String getRecipeType() {
		return recipeType;
	}

	public void setRecipeType(String recipeType) {
		this.recipeType = recipeType;
	}

	public String getRecipeId() {
		return recipeId;
	}

	public void setRecipeId(String recipeId) {
		this.recipeId = recipeId;
	}

	public String getRecipeName() {
		return recipeName;
	}

	public void setRecipeName(String recipeName) {
		this.recipeName = recipeName;
	}

	public String getRecipeTime() {
		return recipeTime;
	}

	public void setRecipeTime(String recipeTime) {
		this.recipeTime = recipeTime;
	}

	public String getRecipeFee() {
		return recipeFee;
	}

	public void setRecipeFee(String recipeFee) {
		this.recipeFee = recipeFee;
	}

	public List<LbBlCgIpVo> getLbBlCgIpVo() {
		return lbBlCgIpVo;
	}

	public void setLbBlCgIpVo(List<LbBlCgIpVo> lbBlCgIpVo) {
		this.lbBlCgIpVo = lbBlCgIpVo;
	}

	public String getWicket() {
		return wicket;
	}

	public void setWicket(String wicket) {
		this.wicket = wicket;
	}

	public String getExecDept() {
		return execDept;
	}

	public void setExecDept(String execDept) {
		this.execDept = execDept;
	}

	public String getExecLocation() {
		return execLocation;
	}

	public void setExecLocation(String execLocation) {
		this.execLocation = execLocation;
	}
	
	
}
