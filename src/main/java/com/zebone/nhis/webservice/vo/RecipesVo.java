package com.zebone.nhis.webservice.vo;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;


@XmlAccessorType(XmlAccessType.FIELD)
public class RecipesVo {

	public List<ResUnpaidItemsVo> getItems() {
		return items;
	}
	public void setItems(List<ResUnpaidItemsVo> items) {
		this.items = items;
	}
	@XmlElement(name="recipeType")
	private String recipeType;
	@XmlElement(name="recipeId")
	private String recipeId;
	@XmlElement(name="recipeName")
	private String recipeName;
	@XmlElement(name="recipeTime")
	private String recipeTime;
	
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
	public String getCanPay() {
		return canPay;
	}
	public void setCanPay(String canPay) {
		this.canPay = canPay;
	}
	@XmlElement(name="recipeFee")
	private String recipeFee;
	@XmlElement(name="canPay")
	private String canPay;
	@XmlElement(name="items")
	private List<ResUnpaidItemsVo>  items;
	
	
}
