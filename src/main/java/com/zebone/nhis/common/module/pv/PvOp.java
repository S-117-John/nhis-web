package com.zebone.nhis.common.module.pv;

import com.zebone.nhis.common.module.BaseModule;
import com.zebone.platform.modules.dao.build.au.Field;
import com.zebone.platform.modules.dao.build.au.Field.KeyId;
import com.zebone.platform.modules.dao.build.au.Table;

import java.util.Date;


/**
 * Table: PV_OP  - 患者就诊-门诊属性
 *
 * @since 2016-09-19 10:09:35
 */
@Table(value = "PV_OP")
public class PvOp extends BaseModule {

    private static final long serialVersionUID = 1L;

    /**
     * PK_PVOP - 就诊主键
     */
    @Field(value = "PK_PVOP", id = KeyId.UUID)
    private String pkPvop;

    /**
     * PK_PV - 就诊
     */
    @Field(value = "PK_PV")
    private String pkPv;

    /**
     * OP_TIMES - 就诊次数
     */
    @Field(value = "OP_TIMES")
    private Long opTimes;

    /**
     * PK_SCHSRV - 排班服务:对应排班表的排班服务主键
     */
    @Field(value = "PK_SCHSRV")
    private String pkSchsrv;

    /**
     * PK_RES - 排班资源:对应排班表的排班资源主键
     */
    @Field(value = "PK_RES")
    private String pkRes;

    /**
     * PK_DATESLOT - 日期分组
     */
    @Field(value = "PK_DATESLOT")
    private String pkDateslot;

    /**
     * PK_DEPT_PV - 挂号科室:指挂号时的科室
     */
    @Field(value = "PK_DEPT_PV")
    private String pkDeptPv;

    /**
     * PK_EMP_PV - 挂号医生:挂号时对应医生，可以为空
     */
    @Field(value = "PK_EMP_PV")
    private String pkEmpPv;

    /**
     * NAME_EMP_PV - 挂号医生姓名
     */
    @Field(value = "NAME_EMP_PV")
    private String nameEmpPv;

    /**
     * DATE_BEGIN - 有效时间-开始
     */
    @Field(value = "DATE_BEGIN")
    private Date dateBegin;

    /**
     * DATE_END - 有效时间-终止
     */
    @Field(value = "DATE_END")
    private Date dateEnd;

    /**
     * TICKETNO - 排队序号
     */
    @Field(value = "TICKETNO")
    private Long ticketno;

    /**
     * PK_SCH - 对应排班:如果有排班功能，将从排班带过来，如果无排班时，可为空。
     */
    @Field(value = "PK_SCH")
    private String pkSch;

    /**
     * FLAG_FIRST - 初诊标志
     */
    @Field(value = "FLAG_FIRST")
    private String flagFirst;

    /**
     * PK_APPO - 对应预约:与就诊预约是1-1关系，所以在就诊预约表中有pk_pv，在本表中有pk_pvopappt.对应非预约的就诊，可为空。
     */
    @Field(value = "PK_APPO")
    private String pkAppo;
    /**
     * 挂号方式 0 现场挂号，1 预约挂号
     */
    @Field(value = "EU_REGTYPE")
    private String euRegtype;
    /**
     * 打印次数
     */
    @Field(value = "CNT_PRINT")
    private int cntPrint;
    /**
     * 最后打印时间
     */
    @Field(value = "DATE_PRINT")
    private Date datePrint;
    /**
     * 接诊资源
     */
    @Field(value = "PK_RES_CN")
    private String pkResCn;
    /**
     * 接诊服务
     */
    @Field(value = "PK_SCHSRV_CN")
    private String pkSchsrvCn;

    @Field(value = "PK_SCHAPPT")
    private String pkSchappt;//对应预约主键

    /**
     * 号源未释放标志
     */
    @Field(value = "FLAG_NORELEASE")
    private String flagNorelease;

    @Field(value = "DT_APPTYPE")
    private String dtApptype;
    
    @Field(value = "ORDERID_EXT")
    private String orderidExt;

    @Field(value = "DATE_SIGN")
    private Date dateSign;

    /**
     * OP_TIMES_REL - 真实就诊次数
     */
    @Field(value = "OP_TIMES_REL")
    private Long opTimesRel;

    /**
     * FLAG_ADD - 加诊标志
     */
    @Field(value = "FLAG_ADD")
    private String flagAdd;

    public String getFlagNorelease() {
        return flagNorelease;
    }

    public void setFlagNorelease(String flagNorelease) {
        this.flagNorelease = flagNorelease;
    }

    public String getPkSchappt() {
        return pkSchappt;
    }

    public void setPkSchappt(String pkSchappt) {
        this.pkSchappt = pkSchappt;
    }

    public String getEuRegtype() {
        return euRegtype;
    }

    public void setEuRegtype(String euRegtype) {
        this.euRegtype = euRegtype;
    }

    public int getCntPrint() {
        return cntPrint;
    }

    public void setCntPrint(int cntPrint) {
        this.cntPrint = cntPrint;
    }

    public Date getDatePrint() {
        return datePrint;
    }

    public void setDatePrint(Date datePrint) {
        this.datePrint = datePrint;
    }

    public String getPkResCn() {
        return pkResCn;
    }

    public void setPkResCn(String pkResCn) {
        this.pkResCn = pkResCn;
    }

    public String getPkSchsrvCn() {
        return pkSchsrvCn;
    }

    public void setPkSchsrvCn(String pkSchsrvCn) {
        this.pkSchsrvCn = pkSchsrvCn;
    }

    public String getPkPvop() {
        return this.pkPvop;
    }

    public void setPkPvop(String pkPvop) {
        this.pkPvop = pkPvop;
    }

    public String getPkPv() {
        return this.pkPv;
    }

    public void setPkPv(String pkPv) {
        this.pkPv = pkPv;
    }

    public Long getOpTimes() {
        return this.opTimes;
    }

    public void setOpTimes(Long opTimes) {
        this.opTimes = opTimes;
    }

    public String getPkSchsrv() {
        return this.pkSchsrv;
    }

    public void setPkSchsrv(String pkSchsrv) {
        this.pkSchsrv = pkSchsrv;
    }

    public String getPkRes() {
        return this.pkRes;
    }

    public void setPkRes(String pkRes) {
        this.pkRes = pkRes;
    }

    public String getPkDateslot() {
        return this.pkDateslot;
    }

    public void setPkDateslot(String pkDateslot) {
        this.pkDateslot = pkDateslot;
    }

    public String getPkDeptPv() {
        return this.pkDeptPv;
    }

    public void setPkDeptPv(String pkDeptPv) {
        this.pkDeptPv = pkDeptPv;
    }

    public String getPkEmpPv() {
        return this.pkEmpPv;
    }

    public void setPkEmpPv(String pkEmpPv) {
        this.pkEmpPv = pkEmpPv;
    }

    public String getNameEmpPv() {
        return this.nameEmpPv;
    }

    public void setNameEmpPv(String nameEmpPv) {
        this.nameEmpPv = nameEmpPv;
    }

    public Date getDateBegin() {
        return this.dateBegin;
    }

    public void setDateBegin(Date dateBegin) {
        this.dateBegin = dateBegin;
    }

    public Date getDateEnd() {
        return this.dateEnd;
    }

    public void setDateEnd(Date dateEnd) {
        this.dateEnd = dateEnd;
    }

    public Long getTicketno() {
        return this.ticketno;
    }

    public void setTicketno(Long ticketno) {
        this.ticketno = ticketno;
    }

    public String getPkSch() {
        return this.pkSch;
    }

    public void setPkSch(String pkSch) {
        this.pkSch = pkSch;
    }

    public String getFlagFirst() {
        return this.flagFirst;
    }

    public void setFlagFirst(String flagFirst) {
        this.flagFirst = flagFirst;
    }

    public String getPkAppo() {
        return this.pkAppo;
    }

    public void setPkAppo(String pkAppo) {
        this.pkAppo = pkAppo;
    }

	public String getDtApptype() {
		return dtApptype;
	}

	public void setDtApptype(String dtApptype) {
		this.dtApptype = dtApptype;
	}

	public String getOrderidExt() {
		return orderidExt;
	}

	public void setOrderidExt(String orderidExt) {
		this.orderidExt = orderidExt;
	}

    public Date getDateSign() {
        return dateSign;
    }

    public void setDateSign(Date dateSign) {
        this.dateSign = dateSign;
    }

    public Long getOpTimesRel() {
        return opTimesRel;
    }

    public void setOpTimesRel(Long opTimesRel) {
        this.opTimesRel = opTimesRel;
    }

    public String getFlagAdd() {
        return flagAdd;
    }

    public void setFlagAdd(String flagAdd) {
        this.flagAdd = flagAdd;
    }
}