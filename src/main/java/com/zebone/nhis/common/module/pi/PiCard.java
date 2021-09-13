package com.zebone.nhis.common.module.pi;

import java.math.BigDecimal;
import java.util.Date;

import com.zebone.nhis.common.module.BaseModule;
import com.zebone.platform.modules.dao.build.au.Field;
import com.zebone.platform.modules.dao.build.au.Field.KeyId;
import com.zebone.platform.modules.dao.build.au.PK;
import com.zebone.platform.modules.dao.build.au.Table;


/**
 * Table: PI_CARD  - 患者信息-卡信息 
 *
 * @since 2016-09-12 01:18:54
 */
@Table(value="PI_CARD")
public class PiCard extends BaseModule  {

	private static final long serialVersionUID = 1L;

	/** PK_PICARD - 患者卡主键 */
	@PK
	@Field(value="PK_PICARD",id=KeyId.UUID)
    private String pkPicard;

    /** PK_PI - 患者主键 */
	@Field(value="PK_PI")
    private String pkPi;

    /** SORT_NO - 序号 */
	@Field(value="SORT_NO")
    private Integer sortNo;

    /** DT_CARDTYPE - 卡类型 */
	@Field(value="DT_CARDTYPE")
    private String dtCardtype;

    /** INNER_NO - 内部卡号 正常情况下与卡编码一致 */
	@Field(value="INNER_NO")
    private String innerNo;

    /** CARD_NO - 卡编码 */
	@Field(value="CARD_NO")
    private String cardNo;

	/** DEPOSIT - 卡押金 */
	@Field(value="DEPOSIT")
    private BigDecimal deposit;
	
	/** DATE_BEGIN - 有效开始时间 */
	@Field(value="DATE_BEGIN")
    private Date dateBegin;

    /** DATE_END - 有效结束时间 */
	@Field(value="DATE_END")
    private Date dateEnd;

    /** FLAG_ACTIVE - 启用标志 */
	@Field(value="FLAG_ACTIVE")
    private String flagActive;

    /** EU_STATUS - 卡状态 0 使用,1挂失, 2到期 9 作废 */
	@Field(value="EU_STATUS")
    private String euStatus;

    public String getPkPicard(){
        return this.pkPicard;
    }
    public void setPkPicard(String pkPicard){
        this.pkPicard = pkPicard;
    }

    public String getPkPi(){
        return this.pkPi;
    }
    public void setPkPi(String pkPi){
        this.pkPi = pkPi;
    }

    public Integer getSortNo(){
        return this.sortNo;
    }
    public void setSortNo(Integer sortNo){
        this.sortNo = sortNo;
    }

    public String getDtCardtype(){
        return this.dtCardtype;
    }
    public void setDtCardtype(String dtCardtype){
        this.dtCardtype = dtCardtype;
    }

    public String getInnerNo(){
        return this.innerNo;
    }
    public void setInnerNo(String innerNo){
        this.innerNo = innerNo;
    }

    public String getCardNo(){
        return this.cardNo;
    }
    public void setCardNo(String cardNo){
        this.cardNo = cardNo;
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
    public BigDecimal getDeposit() {
		return deposit;
	}
	public void setDeposit(BigDecimal deposit) {
		this.deposit = deposit;
	}

}