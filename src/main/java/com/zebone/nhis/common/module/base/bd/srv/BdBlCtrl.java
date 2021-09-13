package com.zebone.nhis.common.module.base.bd.srv;

import com.zebone.nhis.common.module.BaseModule;
import com.zebone.platform.modules.dao.build.au.Field;
import com.zebone.platform.modules.dao.build.au.PK;
import com.zebone.platform.modules.dao.build.au.Table;
import com.zebone.platform.modules.dao.build.au.Field.KeyId;

@Table(value="BD_BL_CTRL")
public class BdBlCtrl extends BaseModule{

	@PK
	@Field(value="PK_BLCTRL",id=KeyId.UUID)
    private String pkBlctrl;
	
	@Field(value="SORTNO")
    private Integer sortno;
	
	@Field(value="NAME_BLC")
    private String nameBlc;
	
	@Field(value="PK_ITEM")
    private String pkItem;
	
	@Field(value="EU_TYPE")
    private String euType;
	
	@Field(value="INC")
    private Integer inc;
	
	@Field(value="NOTE")
    private String note;
	
	@Field(value="FLAG_CHD")
    private String flagChd;
	
	private String isAdd;

	@Field(value="RATIO")
	private int ratio;

	public String getIsAdd() {
		return isAdd;
	}

	public void setIsAdd(String isAdd) {
		this.isAdd = isAdd;
	}

	public String getPkBlctrl() {
		return pkBlctrl;
	}

	public void setPkBlctrl(String pkBlctrl) {
		this.pkBlctrl = pkBlctrl;
	}

	public Integer getSortno() {
		return sortno;
	}

	public void setSortno(Integer sortno) {
		this.sortno = sortno;
	}

	public String getNameBlc() {
		return nameBlc;
	}

	public void setNameBlc(String nameBlc) {
		this.nameBlc = nameBlc;
	}

	public String getPkItem() {
		return pkItem;
	}

	public void setPkItem(String pkItem) {
		this.pkItem = pkItem;
	}

	public String getEuType() {
		return euType;
	}

	public void setEuType(String euType) {
		this.euType = euType;
	}

	public Integer getInc() {
		return inc;
	}

	public void setInc(Integer inc) {
		this.inc = inc;
	}

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}

	public String getFlagChd() {
		return flagChd;
	}

	public void setFlagChd(String flagChd) {
		this.flagChd = flagChd;
	}


	public int getRatio() {return ratio;}

	public void setRatio(int ratio) {this.ratio = ratio;}
}
