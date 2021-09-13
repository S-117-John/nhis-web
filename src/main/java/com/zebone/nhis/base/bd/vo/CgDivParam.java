package com.zebone.nhis.base.bd.vo;

import java.util.List;

import com.zebone.nhis.common.module.base.bd.price.BdHpCgdiv;
import com.zebone.nhis.common.module.base.bd.price.BdHpCgdivItem;
import com.zebone.nhis.common.module.base.bd.price.BdHpCgdivItemcate;

public class CgDivParam 
{

	public BdHpCgdiv cgDiv ;
	
	public List<BdHpCgdivItem> cgDivPd ;
    public List<BdHpCgdivItem> cgDivItem ;
    public List<BdHpCgdivItemcate> cgDivCate ;
    
    //保存删除的表格数据
    public List<BdHpCgdivItem> cgDivDeletePd ;
    public List<BdHpCgdivItem> cgDivDeleteItem ;
	public List<BdHpCgdivItemcate> cgDivDeleteCate ; 
	
	
    public List<BdHpCgdivItem> getCgDivDeletePd() {
		return cgDivDeletePd;
	}

	public void setCgDivDeletePd(List<BdHpCgdivItem> cgDivDeletePd) {
		this.cgDivDeletePd = cgDivDeletePd;
	}

	public List<BdHpCgdivItem> getCgDivDeleteItem() {
		return cgDivDeleteItem;
	}

	public void setCgDivDeleteItem(List<BdHpCgdivItem> cgDivDeleteItem) {
		this.cgDivDeleteItem = cgDivDeleteItem;
	}

	public List<BdHpCgdivItemcate> getCgDivDeleteCate() {
		return cgDivDeleteCate;
	}

	public void setCgDivDeleteCate(List<BdHpCgdivItemcate> cgDivDeleteCate) {
		this.cgDivDeleteCate = cgDivDeleteCate;
	}



	public BdHpCgdiv getCgDiv() {
		return cgDiv;
	}

	public void setCgDiv(BdHpCgdiv cgDiv) {
		this.cgDiv = cgDiv;
	}

	public List<BdHpCgdivItem> getCgDivPd() {
		return cgDivPd;
	}
	
	public void setCgDivPd(List<BdHpCgdivItem> cgDivPd) {
		this.cgDivPd = cgDivPd;
	}
	
	public List<BdHpCgdivItem> getCgDivItem() {
		return cgDivItem;
	}

	public void setCgDivItem(List<BdHpCgdivItem> cgDivItem) {
		this.cgDivItem = cgDivItem;
	}

	public List<BdHpCgdivItemcate> getCgDivCate() {
		return cgDivCate;
	}

	public void setCgDivCate(List<BdHpCgdivItemcate> cgDivCate) {
		this.cgDivCate = cgDivCate;
	}
     
     
}
