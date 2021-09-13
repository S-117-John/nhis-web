package com.zebone.nhis.ex.nis.pi.vo;

import com.zebone.nhis.common.module.pv.PvInfantGrade;

public class PvInfantGradeVo extends PvInfantGrade {
	private String name;//评分项目名称
	private String memo;//分数描述
	private String desc0;//评分描述1
	private String desc1;//评分描述2
	private String desc2;//评分描述3
	

	public String getDesc0() {
		return desc0;
	}

	public void setDesc0(String desc0) {
		this.desc0 = desc0;
	}

	public String getDesc1() {
		return desc1;
	}

	public void setDesc1(String desc1) {
		this.desc1 = desc1;
	}

	public String getDesc2() {
		return desc2;
	}

	public void setDesc2(String desc2) {
		this.desc2 = desc2;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getMemo() {
		return memo;
	}

	public void setMemo(String memo) {
		this.memo = memo;
	}

}
