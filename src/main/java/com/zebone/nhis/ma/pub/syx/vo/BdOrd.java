package com.zebone.nhis.ma.pub.syx.vo;

import com.thoughtworks.xstream.annotations.XStreamAlias;
/**
 * 医嘱项目新增更新实体
 * @author wwx
 *
 */
@XStreamAlias("bdOrd")
public class BdOrd {
	private String pk_ord;
	private String pk_ordtype;
	private String code_ordtype;
	private String code;
	private String name;
	private String name_prt;
	private String spec;
	private String spcode;
	private String d_code;
	private String eu_exclude;
	private String flag_ns;
	private String flag_dr;
	private String code_freq;
	private String quan_def;
	private String flag_ip;
	private String flag_op;
	private String flag_er;
	private String flag_hm;
	private String flag_pe;
	private String flag_emr;
	private String flag_cg;
	private String flag_pd;
	private String flag_active;
	private String note;
	private String creator;
	private String create_time;
	private String modifier;
	private String del_flag;
	private String ts;
	private String flag_unit;
	private String pk_unit;
	private String eu_sex;
	private String flag_ped;
	private String dt_ordcate;
	private String old_id;
	private String age_min;
	private String age_max;
	private String item_id;
	private String desc_ord;
	private String except_ord;
	private String old_type;
	private String flag_noc;
	public String getPk_ord() {
		return pk_ord;
	}
	public String getPk_ordtype() {
		return pk_ordtype;
	}
	public String getCode_ordtype() {
		return code_ordtype;
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
	public String getSpec() {
		return spec;
	}
	public String getSpcode() {
		return spcode;
	}
	public String getD_code() {
		return d_code;
	}
	public String getEu_exclude() {
		return eu_exclude;
	}
	public String getFlag_ns() {
		return flag_ns;
	}
	public String getFlag_dr() {
		return flag_dr;
	}
	public String getCode_freq() {
		return code_freq;
	}
	public String getQuan_def() {
		return quan_def;
	}
	public String getFlag_ip() {
		return flag_ip;
	}
	public String getFlag_op() {
		return flag_op;
	}
	public String getFlag_er() {
		return flag_er;
	}
	public String getFlag_hm() {
		return flag_hm;
	}
	public String getFlag_pe() {
		return flag_pe;
	}
	public String getFlag_emr() {
		return flag_emr;
	}
	public String getFlag_cg() {
		return flag_cg;
	}
	public String getFlag_pd() {
		return flag_pd;
	}
	public String getFlag_active() {
		return flag_active;
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
	public String getDel_flag() {
		return del_flag;
	}
	public String getTs() {
		return ts;
	}
	public String getFlag_unit() {
		return flag_unit;
	}
	public String getPk_unit() {
		return pk_unit;
	}
	public String getEu_sex() {
		return eu_sex;
	}
	public String getFlag_ped() {
		return flag_ped;
	}
	public String getDt_ordcate() {
		return dt_ordcate;
	}
	public String getOld_id() {
		return old_id;
	}
	public String getAge_min() {
		return age_min;
	}
	public String getAge_max() {
		return age_max;
	}
	public String getItem_id() {
		return item_id;
	}
	public String getDesc_ord() {
		return desc_ord;
	}
	public String getExcept_ord() {
		return except_ord;
	}
	public String getOld_type() {
		return old_type;
	}
	public String getFlag_noc() {
		return flag_noc;
	}
	public void setPk_ord(String pk_ord) {
		this.pk_ord = pk_ord;
	}
	public void setPk_ordtype(String pk_ordtype) {
		this.pk_ordtype = pk_ordtype;
	}
	public void setCode_ordtype(String code_ordtype) {
		this.code_ordtype = code_ordtype;
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
	public void setSpec(String spec) {
		this.spec = spec;
	}
	public void setSpcode(String spcode) {
		this.spcode = spcode;
	}
	public void setD_code(String d_code) {
		this.d_code = d_code;
	}
	public void setEu_exclude(String eu_exclude) {
		this.eu_exclude = eu_exclude;
	}
	public void setFlag_ns(String flag_ns) {
		this.flag_ns = flag_ns;
	}
	public void setFlag_dr(String flag_dr) {
		this.flag_dr = flag_dr;
	}
	public void setCode_freq(String code_freq) {
		this.code_freq = code_freq;
	}
	public void setQuan_def(String quan_def) {
		this.quan_def = quan_def;
	}
	public void setFlag_ip(String flag_ip) {
		this.flag_ip = flag_ip;
	}
	public void setFlag_op(String flag_op) {
		this.flag_op = flag_op;
	}
	public void setFlag_er(String flag_er) {
		this.flag_er = flag_er;
	}
	public void setFlag_hm(String flag_hm) {
		this.flag_hm = flag_hm;
	}
	public void setFlag_pe(String flag_pe) {
		this.flag_pe = flag_pe;
	}
	public void setFlag_emr(String flag_emr) {
		this.flag_emr = flag_emr;
	}
	public void setFlag_cg(String flag_cg) {
		this.flag_cg = flag_cg;
	}
	public void setFlag_pd(String flag_pd) {
		this.flag_pd = flag_pd;
	}
	public void setFlag_active(String flag_active) {
		this.flag_active = flag_active;
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
	public void setDel_flag(String del_flag) {
		this.del_flag = del_flag;
	}
	public void setTs(String ts) {
		this.ts = ts;
	}
	public void setFlag_unit(String flag_unit) {
		this.flag_unit = flag_unit;
	}
	public void setPk_unit(String pk_unit) {
		this.pk_unit = pk_unit;
	}
	public void setEu_sex(String eu_sex) {
		this.eu_sex = eu_sex;
	}
	public void setFlag_ped(String flag_ped) {
		this.flag_ped = flag_ped;
	}
	public void setDt_ordcate(String dt_ordcate) {
		this.dt_ordcate = dt_ordcate;
	}
	public void setOld_id(String old_id) {
		this.old_id = old_id;
	}
	public void setAge_min(String age_min) {
		this.age_min = age_min;
	}
	public void setAge_max(String age_max) {
		this.age_max = age_max;
	}
	public void setItem_id(String item_id) {
		this.item_id = item_id;
	}
	public void setDesc_ord(String desc_ord) {
		this.desc_ord = desc_ord;
	}
	public void setExcept_ord(String except_ord) {
		this.except_ord = except_ord;
	}
	public void setOld_type(String old_type) {
		this.old_type = old_type;
	}
	public void setFlag_noc(String flag_noc) {
		this.flag_noc = flag_noc;
	}

}
