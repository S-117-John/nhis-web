package com.zebone.nhis.compay.ins.syx.vo.gzyb;

import java.util.Date;

import com.zebone.platform.modules.dao.build.au.Field;
import com.zebone.platform.modules.dao.build.au.Table;

/**
 * Table: HIS_MZXM 医保接口中间表-MZ费用明细表
 * 
 * @since 
 */
@Table(value = "HIS_MZXM")
public class MiddleHisMzxm {
	@Field(value = "JYDJH")
	private String jYDJH;

	@Field(value = "YYBH")
	private String yYBH;

	@Field(value = "XMXH")
	private Integer xMXH;

	@Field(value = "GMSFHM")
	private String gMSFHM;

	@Field(value = "ZYH")
	private String zYH;

	@Field(value = "RYRQ")
	private Date rYRQ;

	@Field(value = "FYRQ")
	private Date fYRQ;

	@Field(value = "XMBH")
	private String xMBH;

	@Field(value = "XMMC")
	private String xMMC;

	@Field(value = "FLDM")
	private Integer fLDM;

	@Field(value = "YPGG")
	private String yPGG;

	@Field(value = "YPJX")
	private String yPJX;

	@Field(value = "JG")
	private Double jG;

	@Field(value = "MCYL")
	private Double mCYL;

	@Field(value = "JE")
	private Double jE;

	@Field(value = "BZ1")
	private String bZ1;

	@Field(value = "BZ2")
	private String bZ2;

	@Field(value = "BZ3")
	private String bZ3;

	@Field(value = "DRBZ")
	private Integer dRBZ;

	@Field(value = "YPLY")
	private String yPLY;

	public String getjYDJH() {
		return jYDJH;
	}

	public void setjYDJH(String jYDJH) {
		this.jYDJH = jYDJH;
	}

	public String getyYBH() {
		return yYBH;
	}

	public void setyYBH(String yYBH) {
		this.yYBH = yYBH;
	}

	public Integer getxMXH() {
		return xMXH;
	}

	public void setxMXH(Integer xMXH) {
		this.xMXH = xMXH;
	}

	public String getgMSFHM() {
		return gMSFHM;
	}

	public void setgMSFHM(String gMSFHM) {
		this.gMSFHM = gMSFHM;
	}

	public String getzYH() {
		return zYH;
	}

	public void setzYH(String zYH) {
		this.zYH = zYH;
	}

	public Date getrYRQ() {
		return rYRQ;
	}

	public void setrYRQ(Date rYRQ) {
		this.rYRQ = rYRQ;
	}

	public Date getfYRQ() {
		return fYRQ;
	}

	public void setfYRQ(Date fYRQ) {
		this.fYRQ = fYRQ;
	}

	public String getxMBH() {
		return xMBH;
	}

	public void setxMBH(String xMBH) {
		this.xMBH = xMBH;
	}

	public String getxMMC() {
		return xMMC;
	}

	public void setxMMC(String xMMC) {
		this.xMMC = xMMC;
	}

	public Integer getfLDM() {
		return fLDM;
	}

	public void setfLDM(Integer fLDM) {
		this.fLDM = fLDM;
	}

	public String getyPGG() {
		return yPGG;
	}

	public void setyPGG(String yPGG) {
		this.yPGG = yPGG;
	}

	public String getyPJX() {
		return yPJX;
	}

	public void setyPJX(String yPJX) {
		this.yPJX = yPJX;
	}

	public Double getjG() {
		return jG;
	}

	public void setjG(Double jG) {
		this.jG = jG;
	}

	public Double getmCYL() {
		return mCYL;
	}

	public void setmCYL(Double mCYL) {
		this.mCYL = mCYL;
	}

	public Double getjE() {
		return jE;
	}

	public void setjE(Double jE) {
		this.jE = jE;
	}

	public String getbZ1() {
		return bZ1;
	}

	public void setbZ1(String bZ1) {
		this.bZ1 = bZ1;
	}

	public String getbZ2() {
		return bZ2;
	}

	public void setbZ2(String bZ2) {
		this.bZ2 = bZ2;
	}

	public String getbZ3() {
		return bZ3;
	}

	public void setbZ3(String bZ3) {
		this.bZ3 = bZ3;
	}

	public Integer getdRBZ() {
		return dRBZ;
	}

	public void setdRBZ(Integer dRBZ) {
		this.dRBZ = dRBZ;
	}

	public String getyPLY() {
		return yPLY;
	}

	public void setyPLY(String yPLY) {
		this.yPLY = yPLY;
	}

	
}
