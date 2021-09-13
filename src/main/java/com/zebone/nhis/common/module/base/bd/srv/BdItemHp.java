package com.zebone.nhis.common.module.base.bd.srv;


import java.util.Date;

import com.zebone.nhis.common.module.BaseModule;
import com.zebone.platform.modules.dao.build.au.Field;
import com.zebone.platform.modules.dao.build.au.Field.KeyId;
import com.zebone.platform.modules.dao.build.au.PK;
import com.zebone.platform.modules.dao.build.au.Table;


/**
 * Table: BD_ITEM_HP  - bd_item_hp
 *
 * @since 2016-09-09 10:33:28
 */
@Table(value="BD_ITEM_HP")
public class BdItemHp extends BaseModule  {

	private static final long serialVersionUID = 1L;

	@PK
	@Field(value="PK_ITEMHP",id=KeyId.UUID)
    private String pkItemhp;

	@Field(value="PK_ITEM")
    private String pkItem;

	@Field(value="PK_HP")
    private String pkHp;

    /** EU_LEVEL - 0甲类 1乙类 2自费  参考基础码表 040001 */
	@Field(value="EU_LEVEL")
    private String euLevel;

	@Field(value="NOTE")
    private String note;

	@Field(value="CODE_HP")
	private String codeHp;

	/** 字典类型0收费项目1药品 */
	@Field(value="EU_ITEMTYPE")
	private String euItemType;
	/**自付比例*/
	@Field(value="RATIO_SELF")
	private String ratioSelf;

	@Field(value="DT_HPDICTTYPE")
	private String dtHpdicttype;

	/**
	 * 医保名称
	 */
	@Field(value="name_hp")
	private String nameHp;

	/**
	 * 备案时间
	 */
	@Field(value="hp_time")
	private Date hpTime;

	public Date getHpTime() {
		return hpTime;
	}

	public void setHpTime(Date hpTime) {
		this.hpTime = hpTime;
	}

	public String getNameHp() {
		return nameHp;
	}

	public void setNameHp(String nameHp) {
		this.nameHp = nameHp;
	}

	public String getPkItemhp(){
        return this.pkItemhp;
    }
    public void setPkItemhp(String pkItemhp){
        this.pkItemhp = pkItemhp;
    }

    public String getPkItem(){
        return this.pkItem;
    }
    public void setPkItem(String pkItem){
        this.pkItem = pkItem;
    }

    public String getPkHp(){
        return this.pkHp;
    }
    public void setPkHp(String pkHp){
        this.pkHp = pkHp;
    }

    public String getEuLevel(){
        return this.euLevel;
    }
    public void setEuLevel(String euLevel){
        this.euLevel = euLevel;
    }

    public String getNote(){
        return this.note;
    }
    public void setNote(String note){
        this.note = note;
    }
	public String getCodeHp() {
		return codeHp;
	}
	public void setCodeHp(String codeHp) {
		this.codeHp = codeHp;
	}
	public String getEuItemType() {
		return euItemType;
	}
	public void setEuItemType(String euItemType) {
		this.euItemType = euItemType;
	}
	public String getRatioSelf() {
		return ratioSelf;
	}
	public void setRatioSelf(String ratioSelf) {
		this.ratioSelf = ratioSelf;
	}
	public String getDtHpdicttype() {
		return dtHpdicttype;
	}
	public void setDtHpdicttype(String dtHpdicttype) {
		this.dtHpdicttype = dtHpdicttype;
	}


}