package com.zebone.nhis.common.module.pv;

import java.util.Date;
import java.math.BigDecimal;

import com.zebone.nhis.common.module.BaseModule;
import com.zebone.platform.modules.dao.build.au.Field;
import com.zebone.platform.modules.dao.build.au.PK;
import com.zebone.platform.modules.dao.build.au.Table;
import com.zebone.platform.modules.dao.build.au.Field.KeyId;


/**
 * Table: PV_ER 
 *
 * @since 2018-03-06 02:34:08
 */
@Table(value="PV_ER")
public class PvEr extends BaseModule  {

	@PK
	@Field(value="PK_PVER",id=KeyId.UUID)
    private String pkPver;

	@Field(value="PK_PV")
    private String pkPv;

	@Field(value="PK_SCHSRV")
    private String pkSchsrv;

	@Field(value="PK_RES")
    private String pkRes;

	@Field(value="PK_DATESLOT")
    private String pkDateslot;

	@Field(value="PK_DEPT_PV")
    private String pkDeptPv;

	@Field(value="PK_EMP_PV")
    private String pkEmpPv;

	@Field(value="NAME_EMP_PV")
    private String nameEmpPv;

	@Field(value="DATE_BEGIN")
    private Date dateBegin;

	@Field(value="DATE_END")
    private Date dateEnd;

	@Field(value="TICKETNO")
    private Long ticketno;

	@Field(value="PK_SCH")
    private String pkSch;

	@Field(value="DATE_ARV")
    private Date dateArv;

	@Field(value="DT_ESCORTTYPE")
    private String dtEscorttype;

	@Field(value="EU_PVSRC")
    private String euPvsrc;

	@Field(value="DT_ARVTYPE")
    private String dtArvtype;

	@Field(value="MODIFIER")
    private String modifier;

	@Field(value="MODITY_TIME")
    private Date modityTime;

	@Field(value="DT_EMDISTYPE")
    private String dtEmdistype;

	@Field(value="CODE_VEH")
    private String codeVeh;

	@Field(value="DRIVER")
    private String driver;

	@Field(value="NOTE")
    private String note;

	@Field(value="EU_STATUS_ER")
    private String euStatusEr;

	@Field(value="EU_AREA_PV")
    private String euAreaPv;

	@Field(value="DATE_DISC")
    private Date dateDisc;

	@Field(value="DT_OBTYPE")
    private String dtObtype;

	@Field(value="PK_DEPT_IP")
    private String pkDeptIp;
	/** 打印次数*/
	@Field(value="CNT_PRINT")
	private  int  cntPrint;
	/** 最后打印时间*/
	@Field(value="DATE_PRINT")
	private Date  datePrint;
	/** 接诊资源*/
	@Field(value="PK_RES_CN")
	private  String pkResCn;
	/** 接诊服务*/
	@Field(value="PK_SCHSRV_CN")
    private String pkSchsrvCn;
	

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
	public String getPkPver(){
        return this.pkPver;
    }
    public void setPkPver(String pkPver){
        this.pkPver = pkPver;
    }

    public String getPkPv(){
        return this.pkPv;
    }
    public void setPkPv(String pkPv){
        this.pkPv = pkPv;
    }

    public String getPkSchsrv(){
        return this.pkSchsrv;
    }
    public void setPkSchsrv(String pkSchsrv){
        this.pkSchsrv = pkSchsrv;
    }

    public String getPkRes(){
        return this.pkRes;
    }
    public void setPkRes(String pkRes){
        this.pkRes = pkRes;
    }

    public String getPkDateslot(){
        return this.pkDateslot;
    }
    public void setPkDateslot(String pkDateslot){
        this.pkDateslot = pkDateslot;
    }

    public String getPkDeptPv(){
        return this.pkDeptPv;
    }
    public void setPkDeptPv(String pkDeptPv){
        this.pkDeptPv = pkDeptPv;
    }

    public String getPkEmpPv(){
        return this.pkEmpPv;
    }
    public void setPkEmpPv(String pkEmpPv){
        this.pkEmpPv = pkEmpPv;
    }

    public String getNameEmpPv(){
        return this.nameEmpPv;
    }
    public void setNameEmpPv(String nameEmpPv){
        this.nameEmpPv = nameEmpPv;
    }

    public Date getDateBegin(){
        return this.dateBegin;
    }
    public void setDateBegin(Date dateBegin){
        this.dateBegin = dateBegin;
    }

    public Date getDateEnd(){
        return this.dateEnd;
    }
    public void setDateEnd(Date dateEnd){
        this.dateEnd = dateEnd;
    }

    public Long getTicketno(){
        return this.ticketno;
    }
    public void setTicketno(Long ticketno){
        this.ticketno = ticketno;
    }

    public String getPkSch(){
        return this.pkSch;
    }
    public void setPkSch(String pkSch){
        this.pkSch = pkSch;
    }

    public Date getDateArv(){
        return this.dateArv;
    }
    public void setDateArv(Date dateArv){
        this.dateArv = dateArv;
    }

    public String getDtEscorttype(){
        return this.dtEscorttype;
    }
    public void setDtEscorttype(String dtEscorttype){
        this.dtEscorttype = dtEscorttype;
    }

    public String getEuPvsrc(){
        return this.euPvsrc;
    }
    public void setEuPvsrc(String euPvsrc){
        this.euPvsrc = euPvsrc;
    }

    public String getDtArvtype(){
        return this.dtArvtype;
    }
    public void setDtArvtype(String dtArvtype){
        this.dtArvtype = dtArvtype;
    }

    public String getModifier(){
        return this.modifier;
    }
    public void setModifier(String modifier){
        this.modifier = modifier;
    }

    public Date getModityTime(){
        return this.modityTime;
    }
    public void setModityTime(Date modityTime){
        this.modityTime = modityTime;
    }

    public String getDtEmdistype(){
        return this.dtEmdistype;
    }
    public void setDtEmdistype(String dtEmdistype){
        this.dtEmdistype = dtEmdistype;
    }

    public String getCodeVeh(){
        return this.codeVeh;
    }
    public void setCodeVeh(String codeVeh){
        this.codeVeh = codeVeh;
    }

    public String getDriver(){
        return this.driver;
    }
    public void setDriver(String driver){
        this.driver = driver;
    }

    public String getNote(){
        return this.note;
    }
    public void setNote(String note){
        this.note = note;
    }

    public String getEuStatusEr(){
        return this.euStatusEr;
    }
    public void setEuStatusEr(String euStatusEr){
        this.euStatusEr = euStatusEr;
    }

    public String getEuAreaPv(){
        return this.euAreaPv;
    }
    public void setEuAreaPv(String euAreaPv){
        this.euAreaPv = euAreaPv;
    }

    public Date getDateDisc(){
        return this.dateDisc;
    }
    public void setDateDisc(Date dateDisc){
        this.dateDisc = dateDisc;
    }

    public String getDtObtype(){
        return this.dtObtype;
    }
    public void setDtObtype(String dtObtype){
        this.dtObtype = dtObtype;
    }

    public String getPkDeptIp(){
        return this.pkDeptIp;
    }
    public void setPkDeptIp(String pkDeptIp){
        this.pkDeptIp = pkDeptIp;
    }
}