package com.zebone.nhis.common.module.compay.ins.xa.xayb;

import java.util.Date;

import com.zebone.nhis.common.module.BaseModule;
import com.zebone.platform.modules.dao.build.au.Field;
import com.zebone.platform.modules.dao.build.au.PK;
import com.zebone.platform.modules.dao.build.au.Table;
import com.zebone.platform.modules.dao.build.au.Field.KeyId;

/**
 * Table: INS_XAYB_ITEM 西安- 外部医保-医保目录
 * @since 2017-10-19 14:11:10
 */
@Table(value="INS_XAYB_ITEM")
public class InsXaybItem extends BaseModule{
	
	/**
	 * 序列号
	 */
	private static final long serialVersionUID = -8683705698189422229L;

	@PK
	@Field(value="PK_ITEM",id=KeyId.UUID)
    private String pkItem;

	@Field(value="YBTYXMBM")
    private String ybtyxmbm; //医保通用项目编码

	@Field(value="YBTYXMMC")
    private String ybtyxmmc; //医保通用项目名称

	@Field(value="YPZCMC")
	private String ypzcmc; //药品注册名称
	
	@Field(value="SPM")
	private String spm; //商品名

	@Field(value="XYYPDM")
	private String xyypdm; //西药药品代码
	
	@Field(value="YJJYPBM")
	private String yjjypbm; //药监局药品编码
	
	@Field(value="ZCXXXLH")
	private String zcxxxlh; //注册信息序列号

	@Field(value="BGSJ")
    private String bgsj; // 变更时间

	@Field(value="DLBM")
    private String dlbm; // 大类编码

	@Field(value="TJLX")
    private String tjlx; //统计类型

	@Field(value="PYZJM")
    private String pyzjm; //拼音助记码
	
	@Field(value="WBZJM")
	private String wbzjm; //五笔助记码
	
	@Field(value="ZXJJDW")
	private String zxjjdw; //最小计价单位
	
	@Field(value="ZCGG")
	private String zcgg; //注册规格

	@Field(value="JX")
	private String jx; // 剂型
	
	@Field(value="ZCJX")
	private String zcjx; //注册剂型
	
	@Field(value="SCDW")
	private String scdw; //生产单位

	@Field(value="PZWH")
	private String pzwh; //批准文号
	
	@Field(value="PZWHBZ")
	private String pzwhbz; //批准文号备注
	
	@Field(value="TYXMYWMC")
	private String tyxmywmc; //通用项目英文名称
	
	@Field(value="ZFBL")
	private double zfbl; //自付比例
	
	@Field(value="XJ")
	private double xj; // 限价
	
	@Field(value="MLTXSYBZ")
	private String mltxsybz; // 目录特项使用标志
	
	@Field(value="XBMLBZ")
	private String xbmlbz; // 新版目录备注
	
	@Field(value="SG")
	private String sg; // 酸根
	
	@Field(value="YJ")
	private String yj; // 盐基
	
	@Field(value="BZ")
	private String bz; // 备注
	
	@Field(value="MODITY_TIME")
	private Date modifyTime; // 最后操作时间

	public String getPkItem() {
		return pkItem;
	}

	public void setPkItem(String pkItem) {
		this.pkItem = pkItem;
	}

	public String getYbtyxmbm() {
		return ybtyxmbm;
	}

	public void setYbtyxmbm(String ybtyxmbm) {
		this.ybtyxmbm = ybtyxmbm;
	}

	public String getYbtyxmmc() {
		return ybtyxmmc;
	}

	public void setYbtyxmmc(String ybtyxmmc) {
		this.ybtyxmmc = ybtyxmmc;
	}

	public String getYpzcmc() {
		return ypzcmc;
	}

	public void setYpzcmc(String ypzcmc) {
		this.ypzcmc = ypzcmc;
	}

	public String getSpm() {
		return spm;
	}

	public void setSpm(String spm) {
		this.spm = spm;
	}

	public String getXyypdm() {
		return xyypdm;
	}

	public void setXyypdm(String xyypdm) {
		this.xyypdm = xyypdm;
	}

	public String getYjjypbm() {
		return yjjypbm;
	}

	public void setYjjypbm(String yjjypbm) {
		this.yjjypbm = yjjypbm;
	}

	public String getZcxxxlh() {
		return zcxxxlh;
	}

	public void setZcxxxlh(String zcxxxlh) {
		this.zcxxxlh = zcxxxlh;
	}

	public String getBgsj() {
		return bgsj;
	}

	public void setBgsj(String bgsj) {
		this.bgsj = bgsj;
	}

	public String getDlbm() {
		return dlbm;
	}

	public void setDlbm(String dlbm) {
		this.dlbm = dlbm;
	}

	public String getTjlx() {
		return tjlx;
	}

	public void setTjlx(String tjlx) {
		this.tjlx = tjlx;
	}

	public String getPyzjm() {
		return pyzjm;
	}

	public void setPyzjm(String pyzjm) {
		this.pyzjm = pyzjm;
	}

	public String getWbzjm() {
		return wbzjm;
	}

	public void setWbzjm(String wbzjm) {
		this.wbzjm = wbzjm;
	}

	public String getZxjjdw() {
		return zxjjdw;
	}

	public void setZxjjdw(String zxjjdw) {
		this.zxjjdw = zxjjdw;
	}

	public String getZcgg() {
		return zcgg;
	}

	public void setZcgg(String zcgg) {
		this.zcgg = zcgg;
	}

	public String getJx() {
		return jx;
	}

	public void setJx(String jx) {
		this.jx = jx;
	}

	public String getZcjx() {
		return zcjx;
	}

	public void setZcjx(String zcjx) {
		this.zcjx = zcjx;
	}

	public String getScdw() {
		return scdw;
	}

	public void setScdw(String scdw) {
		this.scdw = scdw;
	}

	public String getPzwh() {
		return pzwh;
	}

	public void setPzwh(String pzwh) {
		this.pzwh = pzwh;
	}

	public String getPzwhbz() {
		return pzwhbz;
	}

	public void setPzwhbz(String pzwhbz) {
		this.pzwhbz = pzwhbz;
	}

	public String getTyxmywmc() {
		return tyxmywmc;
	}

	public void setTyxmywmc(String tyxmywmc) {
		this.tyxmywmc = tyxmywmc;
	}

	public double getZfbl() {
		return zfbl;
	}

	public void setZfbl(double zfbl) {
		this.zfbl = zfbl;
	}

	public double getXj() {
		return xj;
	}

	public void setXj(double xj) {
		this.xj = xj;
	}

	public String getMltxsybz() {
		return mltxsybz;
	}

	public void setMltxsybz(String mltxsybz) {
		this.mltxsybz = mltxsybz;
	}

	public String getXbmlbz() {
		return xbmlbz;
	}

	public void setXbmlbz(String xbmlbz) {
		this.xbmlbz = xbmlbz;
	}

	public String getSg() {
		return sg;
	}

	public void setSg(String sg) {
		this.sg = sg;
	}

	public String getYj() {
		return yj;
	}

	public void setYj(String yj) {
		this.yj = yj;
	}

	public String getBz() {
		return bz;
	}

	public void setBz(String bz) {
		this.bz = bz;
	}

	public Date getModifyTime() {
		return modifyTime;
	}

	public void setModifyTime(Date modifyTime) {
		this.modifyTime = modifyTime;
	}

}
