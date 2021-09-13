package com.zebone.nhis.common.module.pi.acc;

import java.util.Date;

import com.zebone.nhis.common.module.BaseModule;
import com.zebone.platform.modules.dao.build.au.Field;
import com.zebone.platform.modules.dao.build.au.Field.FieldType;
import com.zebone.platform.modules.dao.build.au.Field.KeyId;
import com.zebone.platform.modules.dao.build.au.PK;
import com.zebone.platform.modules.dao.build.au.Table;


/**
 * Table: PI_CARD_ISS  - pi_card_iss 
 *
 * @since 2016-09-20 02:11:28
 */
@Table(value="PI_CARD_ISS")
public class PiCardIss extends BaseModule  {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@PK
	@Field(value="PK_CARDISS",id=KeyId.UUID)
    private String pkCardiss;

	@Field(value="DATE_OPERA")
    private Date dateOpera;

	@Field(value="DT_CARDTYPE")
    private String dtCardtype;

	@Field(value="CNT_ISS")
    private Long cntIss;

    /** FLAG_USE - 表示在使用中 */
	@Field(value="FLAG_USE")
    private String flagUse;

	@Field(value="NAME_MACHINE")
    private String nameMachine;

	@Field(value="PK_EMP_ISS")
    private String pkEmpIss;

	@Field(value="PK_EMP_OPERA")
    private String pkEmpOpera;

	@Field(value="NAME_EMP_OPERA")
    private String nameEmpOpera;

	@Field(value="BEGIN_NO")
    private Long beginNo;

	@Field(value="END_NO")
    private Long endNo;

    /** CNT_USE - 使用时系统需要实时更新 */
	@Field(value="CNT_USE")
    private Long cntUse;

    /** CUR_NO - 操作员下次使用的号码 */
	@Field(value="CUR_NO")
    private Long curNo;

    /** FLAG_ACTIVE - 用完后标志置为false */
	@Field(value="FLAG_ACTIVE")
    private String flagActive;

    /** EU_STATUS - 0 初始，1 在用，2 停用，9 退回 */
	@Field(value="EU_STATUS")
    private String euStatus;

	@Field(userfield="pkEmp",userfieldscop=FieldType.ALL)
    private String modifier;

	@Field(value="MODITY_TIME")
    private Date modityTime;


    public String getPkCardiss(){
        return this.pkCardiss;
    }
    public void setPkCardiss(String pkCardiss){
        this.pkCardiss = pkCardiss;
    }

    public Date getDateOpera(){
        return this.dateOpera;
    }
    public void setDateOpera(Date dateOpera){
        this.dateOpera = dateOpera;
    }

    public String getDtCardtype(){
        return this.dtCardtype;
    }
    public void setDtCardtype(String dtCardtype){
        this.dtCardtype = dtCardtype;
    }

    public Long getCntIss(){
        return this.cntIss;
    }
    public void setCntIss(Long cntIss){
        this.cntIss = cntIss;
    }

    public String getFlagUse(){
        return this.flagUse;
    }
    public void setFlagUse(String flagUse){
        this.flagUse = flagUse;
    }

    public String getNameMachine(){
        return this.nameMachine;
    }
    public void setNameMachine(String nameMachine){
        this.nameMachine = nameMachine;
    }

    public String getPkEmpIss(){
        return this.pkEmpIss;
    }
    public void setPkEmpIss(String pkEmpIss){
        this.pkEmpIss = pkEmpIss;
    }

    public String getPkEmpOpera(){
        return this.pkEmpOpera;
    }
    public void setPkEmpOpera(String pkEmpOpera){
        this.pkEmpOpera = pkEmpOpera;
    }

    public String getNameEmpOpera(){
        return this.nameEmpOpera;
    }
    public void setNameEmpOpera(String nameEmpOpera){
        this.nameEmpOpera = nameEmpOpera;
    }

    public Long getBeginNo(){
        return this.beginNo;
    }
    public void setBeginNo(Long beginNo){
        this.beginNo = beginNo;
    }

    public Long getEndNo(){
        return this.endNo;
    }
    public void setEndNo(Long endNo){
        this.endNo = endNo;
    }

    public Long getCntUse(){
        return this.cntUse;
    }
    public void setCntUse(Long cntUse){
        this.cntUse = cntUse;
    }

    public Long getCurNo(){
        return this.curNo;
    }
    public void setCurNo(Long curNo){
        this.curNo = curNo;
    }

    public String getFlagActive(){
        return this.flagActive;
    }
    public void setFlagActive(String flagActive){
        this.flagActive = flagActive;
    }

    public String getEuStatus(){
        return this.euStatus;
    }
    public void setEuStatus(String euStatus){
        this.euStatus = euStatus;
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
}