package com.zebone.nhis.common.module.compay.ins.xa.xayb;

import java.util.Date;

import com.zebone.nhis.common.module.BaseModule;
import com.zebone.platform.modules.dao.build.au.Field;
import com.zebone.platform.modules.dao.build.au.PK;
import com.zebone.platform.modules.dao.build.au.Table;
import com.zebone.platform.modules.dao.build.au.Field.FieldType;
import com.zebone.platform.modules.dao.build.au.Field.KeyId;

/**
 * Table: INS_XIAN_ITEM_MAP 西安- 外部医保-医保目录对照 
 * @since 2017-10-18 16:42:10
 */
@Table(value="INS_XAYB_ITEM_MAP")
public class InsXaybItemMap extends BaseModule{
	
	@PK
	@Field(value="PK_INSITEMMAP",id=KeyId.UUID)
    private String pkInsitemmap;

	@Field(value="PK_HP")
    private String pkHp;

	@Field(value="PK_ITEM")
    private String pkItem;

	@Field(value="CODE_INSITEM")
    private String codeInsitem; // 医保项目编号

	@Field(value="NAME_INSITEM")
    private String nameInsitem; // 医保项目名称

	@Field(value="NOTE")
    private String note;

	@Field(value="MODITY_TIME",date=FieldType.UPDATE)
    private Date modityTime;
	
	@Field(value="EU_EXAMINE")
	private String euExamine; //审核标志
	
	@Field(value="EXAMINE_NOTE")
	private String examineNote; //审核原因
	
	@Field(value="EXAMINE_DATE")
	private Date examineDate; // 提交审核日期
	
	@Field(value="YBXXZCH")
	private String ybxxzch; // 医保信息注册号

	public String getPkInsitemmap() {
		return pkInsitemmap;
	}

	public void setPkInsitemmap(String pkInsitemmap) {
		this.pkInsitemmap = pkInsitemmap;
	}

	public String getPkHp() {
		return pkHp;
	}

	public void setPkHp(String pkHp) {
		this.pkHp = pkHp;
	}

	public String getPkItem() {
		return pkItem;
	}

	public void setPkItem(String pkItem) {
		this.pkItem = pkItem;
	}

	public String getCodeInsitem() {
		return codeInsitem;
	}

	public void setCodeInsitem(String codeInsitem) {
		this.codeInsitem = codeInsitem;
	}

	public String getNameInsitem() {
		return nameInsitem;
	}

	public void setNameInsitem(String nameInsitem) {
		this.nameInsitem = nameInsitem;
	}

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}

	public Date getModityTime() {
		return modityTime;
	}

	public void setModityTime(Date modityTime) {
		this.modityTime = modityTime;
	}

	public String getEuExamine() {
		return euExamine;
	}

	public void setEuExamine(String euExamine) {
		this.euExamine = euExamine;
	}

	public String getExamineNote() {
		return examineNote;
	}

	public void setExamineNote(String examineNote) {
		this.examineNote = examineNote;
	}

	public Date getExamineDate() {
		return examineDate;
	}

	public void setExamineDate(Date examineDate) {
		this.examineDate = examineDate;
	}

	public String getYbxxzch() {
		return ybxxzch;
	}

	public void setYbxxzch(String ybxxzch) {
		this.ybxxzch = ybxxzch;
	}

}
