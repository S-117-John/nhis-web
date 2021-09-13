package com.zebone.nhis.ma.pub.sd.vo.health;

/**
 * 电子健康码 监护人实体类
 * @author zhangtao
 *
 */
public class EhealthCodeGuardianResVO {

	/**
	 * 监护人姓名
	 */
	private String guardianName;
	
	/**
	 * 监护人证件类型
	 */
	private String guardianIdType;
	
	/**
	 * 监护人证件号码
	 */
	private String guardianIdNo;
	
	/**
	 * 监护人与本人关系
	 */
	private String guardianRelation;

	public String getGuardianName() {
		return guardianName;
	}

	public void setGuardianName(String guardianName) {
		this.guardianName = guardianName;
	}

	public String getGuardianIdType() {
		return guardianIdType;
	}

	public void setGuardianIdType(String guardianIdType) {
		this.guardianIdType = guardianIdType;
	}

	public String getGuardianIdNo() {
		return guardianIdNo;
	}

	public void setGuardianIdNo(String guardianIdNo) {
		this.guardianIdNo = guardianIdNo;
	}

	public String getGuardianRelation() {
		return guardianRelation;
	}

	public void setGuardianRelation(String guardianRelation) {
		this.guardianRelation = guardianRelation;
	}
	
}
