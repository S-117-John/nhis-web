package com.zebone.nhis.common.module.base.bd.price;

import java.util.Date;

import com.zebone.nhis.common.module.BaseModule;
import com.zebone.platform.modules.dao.build.au.Field;
import com.zebone.platform.modules.dao.build.au.PK;
import com.zebone.platform.modules.dao.build.au.Table;
import com.zebone.platform.modules.dao.build.au.Field.KeyId;


/**
 * Table: BD_HP_STDIV_DT 
 *
 * @since 2018-07-21 11:55:31
 */
@Table(value="BD_HP_STDIV_DT")
public class BdHpStdivDt extends BaseModule  {

	@PK
	@Field(value="PK_HPSTDIVDT",id=KeyId.UUID)
    private String pkHpstdivdt;

	@Field(value="PK_HPSTDIV")
    private String pkHpstdiv;

	@Field(value="AMT_MIN")
    private Double amtMin;

	@Field(value="AMT_MAX")
    private Double amtMax;

	@Field(value="EU_DIVIDE")
    private String euDivide;

	@Field(value="RATE")
    private Double rate;

	@Field(value="AMOUNT")
    private Double amount;

	@Field(value="DATE_BEGIN")
    private Date dateBegin;

	@Field(value="DATE_END")
    private Date dateEnd;
	
	//0新增行，1修改行
	private String rowStatus;

    public String getRowStatus() {
		return rowStatus;
	}
	public void setRowStatus(String rowStatus) {
		this.rowStatus = rowStatus;
	}
	public String getPkHpstdivdt(){
        return this.pkHpstdivdt;
    }
    public void setPkHpstdivdt(String pkHpstdivdt){
        this.pkHpstdivdt = pkHpstdivdt;
    }

    public String getPkHpstdiv(){
        return this.pkHpstdiv;
    }
    public void setPkHpstdiv(String pkHpstdiv){
        this.pkHpstdiv = pkHpstdiv;
    }

    public Double getAmtMin(){
        return this.amtMin;
    }
    public void setAmtMin(Double amtMin){
        this.amtMin = amtMin;
    }

    public Double getAmtMax(){
        return this.amtMax;
    }
    public void setAmtMax(Double amtMax){
        this.amtMax = amtMax;
    }

    public String getEuDivide(){
        return this.euDivide;
    }
    public void setEuDivide(String euDivide){
        this.euDivide = euDivide;
    }

    public Double getRate(){
        return this.rate;
    }
    public void setRate(Double rate){
        this.rate = rate;
    }

    public Double getAmount(){
        return this.amount;
    }
    public void setAmount(Double amount){
        this.amount = amount;
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

}