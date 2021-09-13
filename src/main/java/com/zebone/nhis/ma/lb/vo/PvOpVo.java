package com.zebone.nhis.ma.lb.vo;

import com.zebone.nhis.common.module.pv.PvEncounter;

/**
 * 就诊门诊属性
 *  
 * @author wangpeng
 * @date 2016年9月19日
 *
 */
public class PvOpVo extends PvEncounter {

	private static final long serialVersionUID = 1L;
	
	/** PK_PVOP - 就诊主键 */
    private String pkPvop;
	
	/** OP_TIMES - 就诊次数 */
    private Long opTimes;

    /** PK_SCHSRV - 排班服务:对应排班表的排班服务主键 */
    private String pkSchsrv;

    /** PK_RES - 排班资源:对应排班表的排班资源主键 */
    private String pkRes;

    /** PK_DATESLOT - 日期分组 */
    private String pkDateslot;

    /** PK_DEPT_PV - 挂号科室:指挂号时的科室 */
    private String pkDeptPv;

    /** PK_EMP_PV - 挂号医生:挂号时对应医生，可以为空 */
    private String pkEmpPv;

    /** NAME_EMP_PV - 挂号医生姓名 */
    private String nameEmpPv;

    /** TICKETNO - 排队序号 */
    private Long ticketno;

    /** PK_SCH - 对应排班:如果有排班功能，将从排班带过来，如果无排班时，可为空。 */
    private String pkSch;

    /** FLAG_FIRST - 初诊标志 */
    private String flagFirst;

    /** PK_APPO - 对应预约:与就诊预约是1-1关系，所以在就诊预约表中有pk_pv，在本表中有pk_pvopappt.对应非预约的就诊，可为空。 */
    private String pkAppo;

    /** 资源名称 */
	private String resourceName;
	
	/** 午别名称 */
	private String dateslotName;
	
	/** 结算主键 */
	private String pkSettle;
	
	public String getPkPvop() {
		return pkPvop;
	}

	public void setPkPvop(String pkPvop) {
		this.pkPvop = pkPvop;
	}

	public Long getOpTimes() {
		return opTimes;
	}

	public void setOpTimes(Long opTimes) {
		this.opTimes = opTimes;
	}

	public String getPkSchsrv() {
		return pkSchsrv;
	}

	public void setPkSchsrv(String pkSchsrv) {
		this.pkSchsrv = pkSchsrv;
	}

	public String getPkRes() {
		return pkRes;
	}

	public void setPkRes(String pkRes) {
		this.pkRes = pkRes;
	}

	public String getPkDateslot() {
		return pkDateslot;
	}

	public void setPkDateslot(String pkDateslot) {
		this.pkDateslot = pkDateslot;
	}

	public String getPkDeptPv() {
		return pkDeptPv;
	}

	public void setPkDeptPv(String pkDeptPv) {
		this.pkDeptPv = pkDeptPv;
	}

	public String getPkEmpPv() {
		return pkEmpPv;
	}

	public void setPkEmpPv(String pkEmpPv) {
		this.pkEmpPv = pkEmpPv;
	}

	public String getNameEmpPv() {
		return nameEmpPv;
	}

	public void setNameEmpPv(String nameEmpPv) {
		this.nameEmpPv = nameEmpPv;
	}

	public Long getTicketno() {
		return ticketno;
	}

	public void setTicketno(Long ticketno) {
		this.ticketno = ticketno;
	}

	public String getPkSch() {
		return pkSch;
	}

	public void setPkSch(String pkSch) {
		this.pkSch = pkSch;
	}

	public String getFlagFirst() {
		return flagFirst;
	}

	public void setFlagFirst(String flagFirst) {
		this.flagFirst = flagFirst;
	}

	public String getPkAppo() {
		return pkAppo;
	}

	public void setPkAppo(String pkAppo) {
		this.pkAppo = pkAppo;
	}

	public String getResourceName() {
		return resourceName;
	}

	public void setResourceName(String resourceName) {
		this.resourceName = resourceName;
	}

	public String getDateslotName() {
		return dateslotName;
	}

	public void setDateslotName(String dateslotName) {
		this.dateslotName = dateslotName;
	}

	public String getPkSettle() {
		return pkSettle;
	}

	public void setPkSettle(String pkSettle) {
		this.pkSettle = pkSettle;
	}

}
