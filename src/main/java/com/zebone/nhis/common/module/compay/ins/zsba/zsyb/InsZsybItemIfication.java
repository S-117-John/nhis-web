package com.zebone.nhis.common.module.compay.ins.zsba.zsyb;

import java.util.Date;

import com.zebone.nhis.common.module.BaseModule;
import com.zebone.platform.modules.dao.build.au.Field;
import com.zebone.platform.modules.dao.build.au.PK;
import com.zebone.platform.modules.dao.build.au.Table;
import com.zebone.platform.modules.dao.build.au.Field.FieldType;
import com.zebone.platform.modules.dao.build.au.Field.KeyId;


/**
 * Table: ins_item_ification - 外部医保-大类代码对照 
 *
 * @since 2017-09-06 10:42:10
 */
@Table(value="INS_ITEM_IFICATION")
public class InsZsybItemIfication extends BaseModule  {

	@PK
	@Field(value="PK_INSITEMIFICATION",id=KeyId.UUID)
    private String pkInsitemification;

	@Field(value="PK_HP")
    private String pkHp;

	@Field(value="PK_INVCATEITEM")
    private String pkInvcateitem;
	
	@Field(value="PK_INSDICT")
	private String pkInsdict;
	
	@Field(value="CODE_INSCLASS")
    private String codeInsclass;

	@Field(value="NAME_INSCLASS")
    private String nameInsclass;

	@Field(value="NOTE")
    private String note;

	@Field(value="MODITY_TIME",date=FieldType.UPDATE)
    private Date modityTime;

	public String getPkInsitemification() {
		return pkInsitemification;
	}

	public void setPkInsitemification(String pkInsitemification) {
		this.pkInsitemification = pkInsitemification;
	}

	public String getPkHp() {
		return pkHp;
	}

	public void setPkHp(String pkHp) {
		this.pkHp = pkHp;
	}

	public String getPkInvcateitem() {
		return pkInvcateitem;
	}

	public void setPkInvcateitem(String pkInvcateitem) {
		this.pkInvcateitem = pkInvcateitem;
	}

	public String getPkInsdict() {
		return pkInsdict;
	}

	public void setPkInsdict(String pkInsdict) {
		this.pkInsdict = pkInsdict;
	}

	public String getCodeInsclass() {
		return codeInsclass;
	}

	public void setCodeInsclass(String codeInsclass) {
		this.codeInsclass = codeInsclass;
	}

	public String getNameInsclass() {
		return nameInsclass;
	}

	public void setNameInsclass(String nameInsclass) {
		this.nameInsclass = nameInsclass;
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


}