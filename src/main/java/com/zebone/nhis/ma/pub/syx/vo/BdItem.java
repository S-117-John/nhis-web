package com.zebone.nhis.ma.pub.syx.vo;

import java.util.Date;

import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 * 收费项目
 * @author 
 *
 */

@XStreamAlias("bdItem")
public class BdItem {
	private String pk_item; 
	private String code; 
	private String name; 
	private String name_prt; 
	private String spcode; 
	private String d_code; 
	private String pk_unit; 
	private String spec; 
	private String price; 
	private String flag_set; 
	private String flag_pd; 
	private String flag_active; 
	private String eu_pricemode; 
	private String pk_itemcate; 
	private String dt_chcate; 
	private String note; 
	private String creator; 
	private String create_time; 
	private String modifier; 
	private String modity_time; 
	private String del_flag; 
	private Date ts; 
	private String yb_id; 
	private String old_id; 
	private String old_type; 
	private String desc_item; 
	private String except_item; 
	private String code_hp; 
	private String code_std; 
	private String dt_itemtype;
	
	
	public String getPk_item() {
		return pk_item;
	}
	public String getCode() {
		return code;
	}
	public String getName() {
		return name;
	}
	public String getName_prt() {
		return name_prt;
	}
	public String getSpcode() {
		return spcode;
	}
	public String getD_code() {
		return d_code;
	}
	public String getPk_unit() {
		return pk_unit;
	}
	public String getSpec() {
		return spec;
	}
	public String getPrice() {
		return price;
	}
	public String getFlag_set() {
		return flag_set;
	}
	public String getFlag_pd() {
		return flag_pd;
	}
	public String getFlag_active() {
		return flag_active;
	}
	public String getEu_pricemode() {
		return eu_pricemode;
	}
	public String getPk_itemcate() {
		return pk_itemcate;
	}
	public String getDt_chcate() {
		return dt_chcate;
	}
	public String getNote() {
		return note;
	}
	public String getCreator() {
		return creator;
	}
	public String getCreate_time() {
		return create_time;
	}
	public String getModifier() {
		return modifier;
	}
	public String getModity_time() {
		return modity_time;
	}
	public String getDel_flag() {
		return del_flag;
	}
	public String getYb_id() {
		return yb_id;
	}
	public String getOld_id() {
		return old_id;
	}
	public String getOld_type() {
		return old_type;
	}
	public String getDesc_item() {
		return desc_item;
	}
	public String getExcept_item() {
		return except_item;
	}
	public String getCode_hp() {
		return code_hp;
	}
	public String getCode_std() {
		return code_std;
	}
	public String getDt_itemtype() {
		return dt_itemtype;
	}
	public void setPk_item(String pk_item) {
		this.pk_item = pk_item;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public void setName(String name) {
		this.name = name;
	}
	public void setName_prt(String name_prt) {
		this.name_prt = name_prt;
	}
	public void setSpcode(String spcode) {
		this.spcode = spcode;
	}
	public void setD_code(String d_code) {
		this.d_code = d_code;
	}
	public void setPk_unit(String pk_unit) {
		this.pk_unit = pk_unit;
	}
	public void setSpec(String spec) {
		this.spec = spec;
	}
	public void setPrice(String price) {
		this.price = price;
	}
	public void setFlag_set(String flag_set) {
		this.flag_set = flag_set;
	}
	public void setFlag_pd(String flag_pd) {
		this.flag_pd = flag_pd;
	}
	public void setFlag_active(String flag_active) {
		this.flag_active = flag_active;
	}
	public void setEu_pricemode(String eu_pricemode) {
		this.eu_pricemode = eu_pricemode;
	}
	public void setPk_itemcate(String pk_itemcate) {
		this.pk_itemcate = pk_itemcate;
	}
	public void setDt_chcate(String dt_chcate) {
		this.dt_chcate = dt_chcate;
	}
	public void setNote(String note) {
		this.note = note;
	}
	public void setCreator(String creator) {
		this.creator = creator;
	}
	public void setCreate_time(String create_time) {
		this.create_time = create_time;
	}
	public void setModifier(String modifier) {
		this.modifier = modifier;
	}
	public void setModity_time(String modity_time) {
		this.modity_time = modity_time;
	}
	public void setDel_flag(String del_flag) {
		this.del_flag = del_flag;
	}
	public void setYb_id(String yb_id) {
		this.yb_id = yb_id;
	}
	public void setOld_id(String old_id) {
		this.old_id = old_id;
	}
	public void setOld_type(String old_type) {
		this.old_type = old_type;
	}
	public void setDesc_item(String desc_item) {
		this.desc_item = desc_item;
	}
	public void setExcept_item(String except_item) {
		this.except_item = except_item;
	}
	public void setCode_hp(String code_hp) {
		this.code_hp = code_hp;
	}
	public void setCode_std(String code_std) {
		this.code_std = code_std;
	}
	public void setDt_itemtype(String dt_itemtype) {
		this.dt_itemtype = dt_itemtype;
	}
	public Date getTs() {
		return ts;
	}
	public void setTs(Date ts) {
		this.ts = ts;
	}
}
