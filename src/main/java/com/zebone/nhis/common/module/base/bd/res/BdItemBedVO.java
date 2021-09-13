package com.zebone.nhis.common.module.base.bd.res;


import com.zebone.nhis.common.module.BaseModule;
import com.zebone.platform.modules.dao.build.au.Field;
import com.zebone.platform.modules.dao.build.au.PK;
import com.zebone.platform.modules.dao.build.au.Table;
import com.zebone.platform.modules.dao.build.au.Field.KeyId;


/**
 * Table: BD_ITEM_BED  - bd_item_bed 
 *
 * @since 2020-03-02 16:00:00
 */
@Table(value="BD_ITEM_BED")
public class BdItemBedVO extends BaseModule  {

	@PK
	@Field(value="PK_BEDITEM",id=KeyId.UUID)
    private String pkBeditem;

	@Field(value="PK_BED")
    private String pkBed;

	@Field(value="PK_ITEM")
    private String pkItem;
	
	@Field(value="QUAN")
    private String quan;

	@Field(value="FLAG_ADD")
    private String flagAdd;
	
	@Field(value="NOTE")
    private String note;

	public String getPkBeditem() {
		return pkBeditem;
	}

	public void setPkBeditem(String pkBeditem) {
		this.pkBeditem = pkBeditem;
	}

	public String getPkBed() {
		return pkBed;
	}

	public void setPkBed(String pkBed) {
		this.pkBed = pkBed;
	}

	public String getPkItem() {
		return pkItem;
	}

	public void setPkItem(String pkItem) {
		this.pkItem = pkItem;
	}

	public String getQuan() {
		return quan;
	}

	public void setQuan(String quan) {
		this.quan = quan;
	}

	public String getFlagAdd() {
		return flagAdd;
	}

	public void setFlagAdd(String flagAdd) {
		this.flagAdd = flagAdd;
	}

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}
}