package com.zebone.nhis.base.bd.vo;

//科室收费项目维护扩展类
public class DeptChargeItemMaintainVo {
	
	//主键
    private String pkItem;

    //项目类型
    private String euItemtype;

    //数量
    private int quan;
    
    //机构
    private String pkOrg;
    
    //科室
    private String pkDept;
    
    //计费套餐主键
    private String pkCgset;



	private Double dosage;


	private String unitDos;

	private String nameSupply;


	private String nameFreq;


	public Double getDosage() {
		return dosage;
	}

	public void setDosage(Double dosage) {
		this.dosage = dosage;
	}

	public String getUnitDos() {
		return unitDos;
	}

	public void setUnitDos(String unitDos) {
		this.unitDos = unitDos;
	}

	public String getNameSupply() {
		return nameSupply;
	}

	public void setNameSupply(String nameSupply) {
		this.nameSupply = nameSupply;
	}

	public String getNameFreq() {
		return nameFreq;
	}

	public void setNameFreq(String nameFreq) {
		this.nameFreq = nameFreq;
	}


	public String getPkCgset() {
		return pkCgset;
	}

	public void setPkCgset(String pkCgset) {
		this.pkCgset = pkCgset;
	}

	public String getPkItem() {
		return pkItem;
	}

	public void setPkItem(String pkItem) {
		this.pkItem = pkItem;
	}

	public String getEuItemtype() {
		return euItemtype;
	}

	public void setEuItemtype(String euItemtype) {
		this.euItemtype = euItemtype;
	}

	public int getQuan() {
		return quan;
	}

	public void setQuan(int quan) {
		this.quan = quan;
	}

	public String getPkOrg() {
		return pkOrg;
	}

	public void setPkOrg(String pkOrg) {
		this.pkOrg = pkOrg;
	}

	public String getPkDept() {
		return pkDept;
	}

	public void setPkDept(String pkDept) {
		this.pkDept = pkDept;
	}
    
}
