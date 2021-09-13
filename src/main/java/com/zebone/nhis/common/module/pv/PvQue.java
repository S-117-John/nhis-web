package com.zebone.nhis.common.module.pv;

import java.util.Date;

import com.zebone.nhis.common.module.BaseModule;
import com.zebone.platform.modules.dao.build.au.Field;
import com.zebone.platform.modules.dao.build.au.Field.KeyId;
import com.zebone.platform.modules.dao.build.au.PK;
import com.zebone.platform.modules.dao.build.au.Table;


/**
 * Table: PV_QUE 
 *
 * @since 2019-03-28 08:14:54
 */
@Table(value="PV_QUE")
public class PvQue extends BaseModule  {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@PK
	@Field(value="PK_PVQUE",id=KeyId.UUID)
    private String pkPvque;

	@Field(value="PK_PV")
    private String pkPv;

	@Field(value="EU_PVTYPE")
    private String euPvtype;

	@Field(value="DATE_PV")
    private Date datePv;

	@Field(value="PK_DATESLOT")
    private String pkDateslot;

	@Field(value="SORTNO")
    private Integer sortno;

	@Field(value="SORTNO_ADJ")
    private Integer sortnoAdj;

	@Field(value="TICKETNO")
    private Integer ticketno;

	@Field(value="PK_QCQUE")
    private String pkQcque;

	@Field(value="PK_SCH")
    private String pkSch;

	@Field(value="PK_DEPT")
    private String pkDept;

	@Field(value="PK_SCHRES")
    private String pkSchres;

	@Field(value="PK_EMP_PHY")
    private String pkEmpPhy;

	@Field(value="NAME_EMP_PHY")
    private String nameEmpPhy;

	@Field(value="EU_STATUS")
    private String euStatus;

	@Field(value="EU_TYPE")
    private String euType;

	@Field(value="DATE_SIGN")
    private Date dateSign;

	@Field(value="PK_EMP_SIGN")
    private String pkEmpSign;

	@Field(value="NAME_EMP_SIGN")
    private String nameEmpSign;

	@Field(value="DATE_ARR")
    private Date dateArr;

	@Field(value="PK_EMP_ARR")
    private String pkEmpArr;

	@Field(value="NAME_EMP_ARR")
    private String nameEmpArr;

	@Field(value="DATE_OVER")
    private Date dateOver;

	@Field(value="DT_QCTYPE")
    private String dtQctype;

	@Field(value="PK_PATICATE")
    private String pkPaticate;

	@Field(value="LEVEL_DISE")
    private String levelDise;

	@Field(value="FLAG_CANCEL")
    private String flagCancel;

	@Field(value="EU_SOURCE")
    private String euSource;

	@Field(value="MODIFIER")
    private String modifier;

	@Field(value="MODITY_TIME")
    private Date modityTime;

	@Field(value="CNT_OVER")
    private Integer cntOver;


    public String getPkPvque(){
        return this.pkPvque;
    }
    public void setPkPvque(String pkPvque){
        this.pkPvque = pkPvque;
    }

    public String getPkPv(){
        return this.pkPv;
    }
    public void setPkPv(String pkPv){
        this.pkPv = pkPv;
    }

    public String getEuPvtype(){
        return this.euPvtype;
    }
    public void setEuPvtype(String euPvtype){
        this.euPvtype = euPvtype;
    }

    public Date getDatePv(){
        return this.datePv;
    }
    public void setDatePv(Date datePv){
        this.datePv = datePv;
    }

    public String getPkDateslot(){
        return this.pkDateslot;
    }
    public void setPkDateslot(String pkDateslot){
        this.pkDateslot = pkDateslot;
    }

    public Integer getSortno(){
        return this.sortno;
    }
    public void setSortno(Integer sortno){
        this.sortno = sortno;
    }

    public Integer getSortnoAdj(){
        return this.sortnoAdj;
    }
    public void setSortnoAdj(Integer sortnoAdj){
        this.sortnoAdj = sortnoAdj;
    }

    public Integer getTicketno(){
        return this.ticketno;
    }
    public void setTicketno(Integer ticketno){
        this.ticketno = ticketno;
    }

    public String getPkQcque(){
        return this.pkQcque;
    }
    public void setPkQcque(String pkQcque){
        this.pkQcque = pkQcque;
    }

    public String getPkSch(){
        return this.pkSch;
    }
    public void setPkSch(String pkSch){
        this.pkSch = pkSch;
    }

    public String getPkDept(){
        return this.pkDept;
    }
    public void setPkDept(String pkDept){
        this.pkDept = pkDept;
    }

    public String getPkSchres(){
        return this.pkSchres;
    }
    public void setPkSchres(String pkSchres){
        this.pkSchres = pkSchres;
    }

    public String getPkEmpPhy(){
        return this.pkEmpPhy;
    }
    public void setPkEmpPhy(String pkEmpPhy){
        this.pkEmpPhy = pkEmpPhy;
    }

    public String getNameEmpPhy(){
        return this.nameEmpPhy;
    }
    public void setNameEmpPhy(String nameEmpPhy){
        this.nameEmpPhy = nameEmpPhy;
    }

    public String getEuStatus(){
        return this.euStatus;
    }
    public void setEuStatus(String euStatus){
        this.euStatus = euStatus;
    }

    public String getEuType(){
        return this.euType;
    }
    public void setEuType(String euType){
        this.euType = euType;
    }

    public Date getDateSign(){
        return this.dateSign;
    }
    public void setDateSign(Date dateSign){
        this.dateSign = dateSign;
    }

    public String getPkEmpSign(){
        return this.pkEmpSign;
    }
    public void setPkEmpSign(String pkEmpSign){
        this.pkEmpSign = pkEmpSign;
    }

    public String getNameEmpSign(){
        return this.nameEmpSign;
    }
    public void setNameEmpSign(String nameEmpSign){
        this.nameEmpSign = nameEmpSign;
    }

    public Date getDateArr(){
        return this.dateArr;
    }
    public void setDateArr(Date dateArr){
        this.dateArr = dateArr;
    }

    public String getPkEmpArr(){
        return this.pkEmpArr;
    }
    public void setPkEmpArr(String pkEmpArr){
        this.pkEmpArr = pkEmpArr;
    }

    public String getNameEmpArr(){
        return this.nameEmpArr;
    }
    public void setNameEmpArr(String nameEmpArr){
        this.nameEmpArr = nameEmpArr;
    }

    public Date getDateOver(){
        return this.dateOver;
    }
    public void setDateOver(Date dateOver){
        this.dateOver = dateOver;
    }

    public String getDtQctype(){
        return this.dtQctype;
    }
    public void setDtQctype(String dtQctype){
        this.dtQctype = dtQctype;
    }

    public String getPkPaticate(){
        return this.pkPaticate;
    }
    public void setPkPaticate(String pkPaticate){
        this.pkPaticate = pkPaticate;
    }

    public String getLevelDise(){
        return this.levelDise;
    }
    public void setLevelDise(String levelDise){
        this.levelDise = levelDise;
    }

    public String getFlagCancel(){
        return this.flagCancel;
    }
    public void setFlagCancel(String flagCancel){
        this.flagCancel = flagCancel;
    }

    public String getEuSource(){
        return this.euSource;
    }
    public void setEuSource(String euSource){
        this.euSource = euSource;
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

    public Integer getCntOver(){
        return this.cntOver;
    }
    public void setCntOver(Integer cntOver){
        this.cntOver = cntOver;
    }
}