package com.zebone.nhis.pro.zsba.compay.ins.zsyb.vo;

import java.util.Date;

import com.zebone.nhis.common.module.BaseModule;
import com.zebone.platform.modules.dao.build.au.Field;
import com.zebone.platform.modules.dao.build.au.PK;
import com.zebone.platform.modules.dao.build.au.Table;
import com.zebone.platform.modules.dao.build.au.Field.FieldType;
import com.zebone.platform.modules.dao.build.au.Field.KeyId;


/**
 * Table: ins_item_map - 外部医保-医保目录对照：（医院三大目录对应关系维护[2021]、三大目录查询[2022]、医院三大目录对应关系上传和修改 
 *
 * @since 2017-09-06 10:42:10
 */
@Table(value="INS_ITEM_MAP")
public class InsZsBaYbItemMap extends BaseModule  {

	@PK
	@Field(value="PK_INSITEMMAP",id=KeyId.UUID)
    private String pkInsitemmap;

	@Field(value="PK_HP")
    private String pkHp;

	@Field(value="PK_ITEM")
    private String pkItem;
	
	@Field(value="PK_INSITEM")
    private String pkIinsitem;

	@Field(value="CODE")
    private String code;

	@Field(value="NAME")
    private String name;

    /** XMLB - 1 药品，2 材料，3 诊疗 */
	@Field(value="XMLB")
    private String xmlb;

	@Field(value="XMBH")
    private String xmbh;

	@Field(value="ZWMC")
    private String zwmc;

	@Field(value="YJJYPBM")
    private String yjjypbm;

	@Field(value="NOTE")
    private String note;

    /** XGLX - 上传、修改、删除 */
	@Field(value="XGLX")
    private String xglx;

	@Field(value="SPRQ")
    private String sprq;

	@Field(value="SPBZ")
    private String spbz;

	@Field(value="YXBZ")
    private String yxbz;

	@Field(value="MODITY_TIME",date=FieldType.UPDATE)
    private Date modityTime;

	@Field(value="YBXMBM")
    private String ybXmbm;
	
    public String getPkInsitemmap(){
        return this.pkInsitemmap;
    }
    public void setPkInsitemmap(String pkInsitemmap){
        this.pkInsitemmap = pkInsitemmap;
    }

    public String getPkHp(){
        return this.pkHp;
    }
    public void setPkHp(String pkHp){
        this.pkHp = pkHp;
    }

    public String getPkItem(){
        return this.pkItem;
    }
    public void setPkItem(String pkItem){
        this.pkItem = pkItem;
    }
    
    public String getPkIinsitem() {
		return pkIinsitem;
	}
	public void setPkIinsitem(String pkIinsitem) {
		this.pkIinsitem = pkIinsitem;
	}
	
	public String getCode(){
        return this.code;
    }
    public void setCode(String code){
        this.code = code;
    }

    public String getName(){
        return this.name;
    }
    public void setName(String name){
        this.name = name;
    }

    public String getXmlb(){
        return this.xmlb;
    }
    public void setXmlb(String xmlb){
        this.xmlb = xmlb;
    }

    public String getXmbh(){
        return this.xmbh;
    }
    public void setXmbh(String xmbh){
        this.xmbh = xmbh;
    }

    public String getZwmc(){
        return this.zwmc;
    }
    public void setZwmc(String zwmc){
        this.zwmc = zwmc;
    }

    public String getYjjypbm(){
        return this.yjjypbm;
    }
    public void setYjjypbm(String yjjypbm){
        this.yjjypbm = yjjypbm;
    }

    public String getNote(){
        return this.note;
    }
    public void setNote(String note){
        this.note = note;
    }

    public String getXglx(){
        return this.xglx;
    }
    public void setXglx(String xglx){
        this.xglx = xglx;
    }

    public String getSprq(){
        return this.sprq;
    }
    public void setSprq(String sprq){
        this.sprq = sprq;
    }

    public String getSpbz(){
        return this.spbz;
    }
    public void setSpbz(String spbz){
        this.spbz = spbz;
    }

    public String getYxbz(){
        return this.yxbz;
    }
    public void setYxbz(String yxbz){
        this.yxbz = yxbz;
    }

    public Date getModityTime(){
        return this.modityTime;
    }
    public void setModityTime(Date modityTime){
        this.modityTime = modityTime;
    }
	public String getYbXmbm() {
		return ybXmbm;
	}
	public void setYbXmbm(String ybXmbm) {
		this.ybXmbm = ybXmbm;
	}
}