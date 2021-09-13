package com.zebone.nhis.common.module.pv;

import com.zebone.nhis.common.module.BaseModule;
import com.zebone.platform.modules.dao.build.au.Field;
import com.zebone.platform.modules.dao.build.au.Field.KeyId;
import com.zebone.platform.modules.dao.build.au.PK;
import com.zebone.platform.modules.dao.build.au.Table;

/**
 * Table: PV_INSURANCE - 患者就诊-保险计划
 * 
 * @since 2016-09-21 03:42:27
 */
@Table(value = "PV_INSURANCE")
public class PvInsurance extends BaseModule {

	private static final long serialVersionUID = 1L;

	/** PK_PVHP - 患者就诊医保计划主键 */
	@PK
	@Field(value = "PK_PVHP", id = KeyId.UUID)
	private String pkPvhp;

	/** PK_PV - 就诊主键 */
	@Field(value = "PK_PV")
	private String pkPv;

	/** SORT_NO - 序号 */
	@Field(value = "SORT_NO")
	private Integer sortNo;

	/** PK_HP - 保险计划_本次就诊 */
	@Field(value = "PK_HP")
	private String pkHp;

	/** FLAG_MAJ - 主医保计划标识 */
	@Field(value = "FLAG_MAJ")
	private String flagMaj;

	public String getPkPvhp() {

		return this.pkPvhp;
	}

	public void setPkPvhp(String pkPvhp) {

		this.pkPvhp = pkPvhp;
	}

	public String getPkPv() {

		return this.pkPv;
	}

	public void setPkPv(String pkPv) {

		this.pkPv = pkPv;
	}

	public Integer getSortNo() {

		return this.sortNo;
	}

	public void setSortNo(Integer sortNo) {

		this.sortNo = sortNo;
	}

	public String getPkHp() {

		return this.pkHp;
	}

	public void setPkHp(String pkHp) {

		this.pkHp = pkHp;
	}

	public String getFlagMaj() {

		return this.flagMaj;
	}

	public void setFlagMaj(String flagMaj) {

		this.flagMaj = flagMaj;
	}

	/**
	 * 由于需要用到此对象的业务意义上相等，故重写equals方法，
	 * hashcode方法重写是当此对象放在hash类型的容器中时会将hashcode作为K所以重写
	 * 
	 * gongxy 2017-6-2 16:47:35
	 */
	@Override
	public boolean equals(Object obj) {

		if (this == obj) {
			return true;
		}
		if (null == obj) {
			return false;
		}
		if (obj instanceof PvInsurance) {
			PvInsurance pvInsurance = (PvInsurance) obj;
			return (pvInsurance.getPkPvhp().equals(pkPvhp));
		}
		return false;
	}

	@Override
	public int hashCode() {

		return pkPvhp.hashCode() * 7;
	}

}