package com.zebone.nhis.bl.pub.syx.vo;
/**
 * 药品和收费项目参照
 * @author 
 *
 */
public class OtherReferencePdAndItemVo {
	private String pk_pd;
	private String name;
	private String spec;
	private String pk_unit_min;
	private Double price;
	private Double dosage_def;
	private String pk_unit_def;
	private String code_supply;
	private String code_freq;

	public String getPk_pd() {
		return pk_pd;
	}

	public void setPk_pd(String pk_pd) {
		this.pk_pd = pk_pd;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getSpec() {
		return spec;
	}

	public void setSpec(String spec) {
		this.spec = spec;
	}

	public String getPk_unit_min() {
		return pk_unit_min;
	}

	public void setPk_unit_min(String pk_unit_min) {
		this.pk_unit_min = pk_unit_min;
	}

	public Double getPrice() {
		return price;
	}

	public void setPrice(Double price) {
		this.price = price;
	}

	public Double getDosage_def() {
		return dosage_def;
	}

	public void setDosage_def(Double dosage_def) {
		this.dosage_def = dosage_def;
	}

	public String getPk_unit_def() {
		return pk_unit_def;
	}

	public void setPk_unit_def(String pk_unit_def) {
		this.pk_unit_def = pk_unit_def;
	}

	public String getCode_supply() {
		return code_supply;
	}

	public void setCode_supply(String code_supply) {
		this.code_supply = code_supply;
	}

	public String getCode_freq() {
		return code_freq;
	}

	public void setCode_freq(String code_freq) {
		this.code_freq = code_freq;
	}

}
