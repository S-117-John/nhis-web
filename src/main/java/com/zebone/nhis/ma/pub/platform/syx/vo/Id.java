package com.zebone.nhis.ma.pub.platform.syx.vo;

import java.util.ArrayList;
import java.util.List;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;
import com.thoughtworks.xstream.annotations.XStreamImplicit;

/**
 * 通用id
 * 
 * @author IBM
 * 
 */
@XStreamAlias("id")
public class Id {
	@XStreamAsAttribute
	private String root;
	@XStreamAsAttribute
	private String extension;
	@XStreamImplicit
	private List<Item> items;
	@XStreamImplicit(itemFieldName = "item1")
	private List<Item1> itemOnes;
	
	private Item item ;
	public Item getItem() {
		if(item==null)item= new Item();
		return item;
	}

	public void setItem(Item item) {
		this.item = item;
	}

	public String getRoot() {
		return root;
	}

	public void setRoot(String root) {
		this.root = root;
	}

	public String getExtension() {
		return extension;
	}

	public void setExtension(String extension) {
		this.extension = extension;
	}

	public List<Item> getItems() {
		if(items==null)items=new ArrayList<Item>();
		return items;
	}

	public void setItems(List<Item> items) {
		this.items = items;
	}

	public List<Item1> getItemOnes() {
		return itemOnes;
	}

	public void setItemOnes(List<Item1> itemOnes) {
		this.itemOnes = itemOnes;
	}
	
}
