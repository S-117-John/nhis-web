package com.zebone.nhis.scm.pub.vo;


/**
 * 物品出库请求参数
 * @author yangxue
 *
 */
public class PdOutParamVo implements Cloneable {
	private String pkPv;
	private String pkCnOrd;
	private String pkPdapdt;//请领明细主键
	private String pkPd;
	private Integer packSize;//包装量
	private String pkUnitPack;//仓库物品中对应的包装单位
	private Double quanPack;//包装单位下需要出库的数量
	private Double quanMin;//基本单位下需要出库的数量
	//private String batchNo;
	public PdOutParamVo clone() {  
		PdOutParamVo o = null;  
        try {  
            o = (PdOutParamVo) super.clone();  
        } catch (CloneNotSupportedException e) {  
            e.printStackTrace();  
        }  
        return o;  
    }  
	
	public String getPkPd() {
		return pkPd;
	}
	public String getPkPv() {
		return pkPv;
	}
	public void setPkPv(String pkPv) {
		this.pkPv = pkPv;
	}
	public String getPkCnOrd() {
		return pkCnOrd;
	}
	public void setPkCnOrd(String pkCnOrd) {
		this.pkCnOrd = pkCnOrd;
	}
	public String getPkPdapdt() {
		return pkPdapdt;
	}
	public void setPkPdapdt(String pkPdapdt) {
		this.pkPdapdt = pkPdapdt;
	}
	public void setPkPd(String pkPd) {
		this.pkPd = pkPd;
	}
	public Integer getPackSize() {
		return packSize;
	}
	public void setPackSize(Integer packSize) {
		this.packSize = packSize;
	}
	public String getPkUnitPack() {
		return pkUnitPack;
	}
	public void setPkUnitPack(String pkUnitPack) {
		this.pkUnitPack = pkUnitPack;
	}
	public Double getQuanPack() {
		return quanPack;
	}
	public void setQuanPack(Double quanPack) {
		this.quanPack = quanPack;
	}
	public Double getQuanMin() {
		return quanMin;
	}
	public void setQuanMin(Double quanMin) {
		this.quanMin = quanMin;
	}
	
}
