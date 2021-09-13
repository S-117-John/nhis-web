package com.zebone.nhis.compay.ins.syx.vo.gzyb;

import java.math.BigDecimal;
import java.util.Date;

import com.zebone.platform.modules.dao.build.au.Field;
import com.zebone.platform.modules.dao.build.au.Table;

/**
 * Table: HIS_MZJS 医保接口中间表-门诊费用结算表
 * 
 * @since 
 */
@Table(value = "HIS_MZJS")
public class MiddleHisMzjs {
	@Field(value = "JYDJH")
	private String jYDJH;

	@Field(value = "FYPC")
	private Integer fYPC;

	@Field(value = "YYBH")
	private String yYBH;

	@Field(value = "GMSFHM")
	private String gMSFHM;

	@Field(value = "ZYH")
	private String zYH;

	@Field(value = "RYRQ")
	private Date rYRQ;

	@Field(value = "JSRQ")
	private Date jSRQ;

	@Field(value = "ZYZJE")
	private BigDecimal zYZJE;

	@Field(value = "SBZFJE")
	private BigDecimal sBZFJE;

	@Field(value = "ZHZFJE")
	private BigDecimal zHZFJE;

	@Field(value = "BFXMZFJE")
	private BigDecimal bFXMZFJE;

	@Field(value = "QFJE")
	private BigDecimal qFJE;

	@Field(value = "GRZFJE1")
	private BigDecimal gRZFJE1;

	@Field(value = "GRZFJE2")
	private BigDecimal gRZFJE2;

	@Field(value = "GRZFJE3")
	private BigDecimal gRZFJE3;

	@Field(value = "CXZFJE")
	private BigDecimal cXZFJE;

	@Field(value = "YYFDJE")
	private BigDecimal yYFDJE;

	@Field(value = "ZFYY")
	private String zFYY;

	@Field(value = "BZ1")
	private String bZ1;

	@Field(value = "BZ2")
	private String bZ2;

	@Field(value = "BZ3")
	private String bZ3;

	@Field(value = "DRBZ")
	private Integer dRBZ;

	public String getjYDJH() {
		return jYDJH;
	}

	public void setjYDJH(String jYDJH) {
		this.jYDJH = jYDJH;
	}

	public Integer getfYPC() {
		return fYPC;
	}

	public void setfYPC(Integer fYPC) {
		this.fYPC = fYPC;
	}

	public String getyYBH() {
		return yYBH;
	}

	public void setyYBH(String yYBH) {
		this.yYBH = yYBH;
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

	public Date getjSRQ() {
		return jSRQ;
	}

	public void setjSRQ(Date jSRQ) {
		this.jSRQ = jSRQ;
	}

	public BigDecimal getzYZJE() {
		return zYZJE;
	}

	public void setzYZJE(BigDecimal zYZJE) {
		this.zYZJE = zYZJE;
	}

	public BigDecimal getsBZFJE() {
		return sBZFJE;
	}

	public void setsBZFJE(BigDecimal sBZFJE) {
		this.sBZFJE = sBZFJE;
	}

	public BigDecimal getzHZFJE() {
		return zHZFJE;
	}

	public void setzHZFJE(BigDecimal zHZFJE) {
		this.zHZFJE = zHZFJE;
	}

	public BigDecimal getbFXMZFJE() {
		return bFXMZFJE;
	}

	public void setbFXMZFJE(BigDecimal bFXMZFJE) {
		this.bFXMZFJE = bFXMZFJE;
	}

	public BigDecimal getqFJE() {
		return qFJE;
	}

	public void setqFJE(BigDecimal qFJE) {
		this.qFJE = qFJE;
	}

	public BigDecimal getgRZFJE1() {
		return gRZFJE1;
	}

	public void setgRZFJE1(BigDecimal gRZFJE1) {
		this.gRZFJE1 = gRZFJE1;
	}

	public BigDecimal getgRZFJE2() {
		return gRZFJE2;
	}

	public void setgRZFJE2(BigDecimal gRZFJE2) {
		this.gRZFJE2 = gRZFJE2;
	}

	public BigDecimal getgRZFJE3() {
		return gRZFJE3;
	}

	public void setgRZFJE3(BigDecimal gRZFJE3) {
		this.gRZFJE3 = gRZFJE3;
	}

	public BigDecimal getcXZFJE() {
		return cXZFJE;
	}

	public void setcXZFJE(BigDecimal cXZFJE) {
		this.cXZFJE = cXZFJE;
	}

	public BigDecimal getyYFDJE() {
		return yYFDJE;
	}

	public void setyYFDJE(BigDecimal yYFDJE) {
		this.yYFDJE = yYFDJE;
	}

	public String getzFYY() {
		return zFYY;
	}

	public void setzFYY(String zFYY) {
		this.zFYY = zFYY;
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
}
