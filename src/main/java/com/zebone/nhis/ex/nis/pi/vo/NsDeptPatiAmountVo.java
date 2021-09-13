package com.zebone.nhis.ex.nis.pi.vo;

/**
 * 护士站床位卡人数统计信息
 * @author yangxue
 *
 */
public class NsDeptPatiAmountVo {
	private Integer bw;//病危
	private Integer bz;//病重
	private Integer total;//总人数 （不含新生儿）
	private Integer yr;//婴儿
	private Integer men;
	private Integer women;
	private Integer newborn;
	private Integer others;
	private Integer hospin;//入院
	private Integer hospout;//出院
	private Integer deptin;//转入
	private Integer deptout;//转出
	private Integer opNum;//手术
	private Integer dieNum;//死亡
	private Integer OutStNum;//出院未结算
	private Integer bwbz;
	//一级护理人数
	private Integer oneLevelNum;
	/**医保患者人数*/
	private Integer medicalInsurance;
	/**自费患者患者人数*/
	private Integer ownExpense;


	public Integer getMedicalInsurance() {
		return medicalInsurance;
	}

	public void setMedicalInsurance(Integer medicalInsurance) {
		this.medicalInsurance = medicalInsurance;
	}

	public Integer getOwnExpense() {
		return ownExpense;
	}

	public void setOwnExpense(Integer ownExpense) {
		this.ownExpense = ownExpense;
	}

	public Integer getBwbz() {
		return bwbz;
	}

	public void setBwbz(Integer bwbz) {
		this.bwbz = bwbz;
	}

	public Integer getHospin() {
		return hospin;
	}

	public void setHospin(Integer hospin) {
		this.hospin = hospin;
	}

	public Integer getHospout() {
		return hospout;
	}

	public void setHospout(Integer hospout) {
		this.hospout = hospout;
	}

	public Integer getDeptin() {
		return deptin;
	}

	public void setDeptin(Integer deptin) {
		this.deptin = deptin;
	}

	public Integer getDeptout() {
		return deptout;
	}

	public void setDeptout(Integer deptout) {
		this.deptout = deptout;
	}

	public Integer getBw() {
		return bw;
	}

	public void setBw(Integer bw) {
		this.bw = bw;
	}

	public Integer getBz() {
		return bz;
	}

	public void setBz(Integer bz) {
		this.bz = bz;
	}

	public Integer getTotal() {
		return total;
	}

	public void setTotal(Integer total) {
		this.total = total;
	}

	public Integer getMen() {
		return men;
	}

	public void setMen(Integer men) {
		this.men = men;
	}

	public Integer getWomen() {
		return women;
	}

	public void setWomen(Integer women) {
		this.women = women;
	}

	public Integer getNewborn() {
		return newborn;
	}

	public void setNewborn(Integer newborn) {
		this.newborn = newborn;
	}

	public Integer getOthers() {
		return others;
	}

	public void setOthers(Integer others) {
		this.others = others;
	}

	public Integer getOpNum() {
		return opNum;
	}

	public void setOpNum(Integer opNum) {
		this.opNum = opNum;
	}

	public Integer getDieNum() {
		return dieNum;
	}

	public void setDieNum(Integer dieNum) {
		this.dieNum = dieNum;
	}

	public Integer getOutStNum() {
		return OutStNum;
	}

	public void setOutStNum(Integer outStNum) {
		OutStNum = outStNum;
	}

	public Integer getYr() {
		return yr;
	}

	public void setYr(Integer yr) {
		this.yr = yr;
	}

	public Integer getOneLevelNum() {
		return oneLevelNum;
	}

	public void setOneLevelNum(Integer oneLevelNum) {
		this.oneLevelNum = oneLevelNum;
	}

}
