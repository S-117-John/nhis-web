package com.zebone.nhis.common.module.ma.tpi.ems;

import java.math.BigDecimal;

import com.zebone.platform.modules.dao.build.au.Field;
import com.zebone.platform.modules.dao.build.au.PK;
import com.zebone.platform.modules.dao.build.au.Table;
import com.zebone.platform.modules.dao.build.au.Field.KeyId;


/**
 * Table: yj_sq02 
 *
 * @since 2018-09-13 05:30:20
 */
@Table(value="his.yj_sq02")
public class YjSq02   {

	@Field(value="SBXH")
    private Integer sbxh;

	@Field(value="YJXH")
    private Integer yjxh;

	@Field(value="MZZY")
    private Integer mzzy;

	@Field(value="NBMC")
    private String nbmc;

	@Field(value="DXMC")
    private String dxmc;

	@Field(value="DXQZ")
    private String dxqz;

	public Integer getSbxh() {
		return sbxh;
	}

	public void setSbxh(Integer sbxh) {
		this.sbxh = sbxh;
	}

	public Integer getYjxh() {
		return yjxh;
	}

	public void setYjxh(Integer yjxh) {
		this.yjxh = yjxh;
	}

	public Integer getMzzy() {
		return mzzy;
	}

	public void setMzzy(Integer mzzy) {
		this.mzzy = mzzy;
	}

	public String getNbmc() {
		return nbmc;
	}

	public void setNbmc(String nbmc) {
		this.nbmc = nbmc;
	}

	public String getDxmc() {
		return dxmc;
	}

	public void setDxmc(String dxmc) {
		this.dxmc = dxmc;
	}

	public String getDxqz() {
		return dxqz;
	}

	public void setDxqz(String dxqz) {
		this.dxqz = dxqz;
	}


}