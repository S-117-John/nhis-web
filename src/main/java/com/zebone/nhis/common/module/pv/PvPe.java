package com.zebone.nhis.common.module.pv;

import java.util.Date;

import com.zebone.nhis.common.module.BaseModule;
import com.zebone.platform.modules.dao.build.au.Field;
import com.zebone.platform.modules.dao.build.au.Field.KeyId;
import com.zebone.platform.modules.dao.build.au.Table;


/**
 * Table: PV_PE  - 患者就诊-体检属性
 * @author zhangtao
 * @since 2021-01-27 10:09:35
 */
@Table(value = "PV_PE")
public class PvPe extends BaseModule {

	private static final long serialVersionUID = 1L;

    /**
     * PK_PVPE - 就诊主键
     */
    @Field(value = "PK_PVPE", id = KeyId.UUID)
    private String pkPvpe;

    /**
     * PK_PV - 就诊
     */
    @Field(value = "PK_PV")
    private String pkPv;

    /**
     * PK_PI - 患者主键
     */
    @Field(value = "PK_PI")
    private String pkPi;

    /**
     * DT_EXAM - 服务类型_体检
     */
    @Field(value = "DT_EXAM")
    private String dtExam;

    /**
     * PK_SRV - 服务_体检包
     */
    @Field(value = "PK_SRV")
    private String pkSrv;

    /**
     * srv_name - 服务名称_体检包
     */
    @Field(value = "srv_name")
    private String srvName;

    /**
     * PK_DATESLOT - 计划日期分组
     */
    @Field(value = "PK_DATESLOT")
    private String pkDateslot;

    /**
     * DATESLOT_NAME - 计划日期分组名称
     */
    @Field(value = "DATESLOT_NAME")
    private String dateslotName;

    /**
     * EFFECTIVE_B - 有效期限_开始
     */
    @Field(value = "EFFECTIVE_B")
    private Date effectiveB;

    /**
     * EFFECTIVE_E - 有效期限_结束
     */
    @Field(value = "EFFECTIVE_E")
    private Date effectiveE;


    /**
     * PK_SCH - 对应排班:如果有排班功能，将从排班带过来，如果无排班时，可为空。
     */
    @Field(value = "PK_SCH")
    private String pkSch;

    /**
     * TICKETNO - 就诊排队号
     */
    @Field(value = "TICKETNO")
    private Long ticketno;

    /**
     * PK_PVPECUST - 对应单位体检任务包
     */
    @Field(value = "PK_PVPECUST")
    private String pkPvpecust;
    /**
     * PK_CG - 对应记费
     */
    @Field(value = "PK_CG")
    private String pkCg;
    /**
     * ST_FLAG	- 已结算标志_挂号
     */
    @Field(value = "ST_FLAG")
    private String stFlag;
    /**
     * PK_ST - 对应结算
     */
    @Field(value = "PK_ST")
    private String pkSt;
    /**
     * PK_PVOPAPPT - 对应预约
     */
    @Field(value = "PK_PVOPAPPT")
    private String pkPvopappt;

	public String getPkPvpe() {
		return pkPvpe;
	}
	public String getPkPv() {
		return pkPv;
	}
	public String getPkPi() {
		return pkPi;
	}
	public String getDtExam() {
		return dtExam;
	}
	public String getPkSrv() {
		return pkSrv;
	}
	public String getSrvName() {
		return srvName;
	}
	public String getPkDateslot() {
		return pkDateslot;
	}
	public String getDateslotName() {
		return dateslotName;
	}
	public Date getEffectiveB() {
		return effectiveB;
	}
	public Date getEffectiveE() {
		return effectiveE;
	}
	public String getPkSch() {
		return pkSch;
	}
	public Long getTicketno() {
		return ticketno;
	}
	public String getPkPvpecust() {
		return pkPvpecust;
	}
	public String getPkCg() {
		return pkCg;
	}
	public String getStFlag() {
		return stFlag;
	}
	public String getPkSt() {
		return pkSt;
	}
	public String getPkPvopappt() {
		return pkPvopappt;
	}

	public void setPkPvpe(String pkPvpe) {
		this.pkPvpe = pkPvpe;
	}
	public void setPkPv(String pkPv) {
		this.pkPv = pkPv;
	}
	public void setPkPi(String pkPi) {
		this.pkPi = pkPi;
	}
	public void setDtExam(String dtExam) {
		this.dtExam = dtExam;
	}
	public void setPkSrv(String pkSrv) {
		this.pkSrv = pkSrv;
	}
	public void setSrvName(String srvName) {
		this.srvName = srvName;
	}
	public void setPkDateslot(String pkDateslot) {
		this.pkDateslot = pkDateslot;
	}
	public void setDateslotName(String dateslotName) {
		this.dateslotName = dateslotName;
	}
	public void setEffectiveB(Date effectiveB) {
		this.effectiveB = effectiveB;
	}
	public void setEffectiveE(Date effectiveE) {
		this.effectiveE = effectiveE;
	}
	public void setPkSch(String pkSch) {
		this.pkSch = pkSch;
	}
	public void setTicketno(Long ticketno) {
		this.ticketno = ticketno;
	}
	public void setPkPvpecust(String pkPvpecust) {
		this.pkPvpecust = pkPvpecust;
	}
	public void setPkCg(String pkCg) {
		this.pkCg = pkCg;
	}
	public void setStFlag(String stFlag) {
		this.stFlag = stFlag;
	}
	public void setPkSt(String pkSt) {
		this.pkSt = pkSt;
	}
	public void setPkPvopappt(String pkPvopappt) {
		this.pkPvopappt = pkPvopappt;
	}
	
}