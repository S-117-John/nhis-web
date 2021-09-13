package com.zebone.nhis.common.module.bl.support;

import java.util.Date;
import java.math.BigDecimal;

import com.zebone.nhis.common.module.BaseModule;
import com.zebone.platform.modules.dao.build.au.Field;
import com.zebone.platform.modules.dao.build.au.PK;
import com.zebone.platform.modules.dao.build.au.Table;
import com.zebone.platform.modules.dao.build.au.Field.KeyId;


/**
 * Table: BD_ORD_MT 
 *
 * @since 2017-10-18 17:38:04
 */
@Table(value="BD_ORD_MT")
public class BdOrdMt extends BaseModule  {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@PK
	@Field(value="PK_ORDMT",id=KeyId.UUID)
    private String pkOrdmt;

	@Field(value="PK_DEPT")
    private String pkDept;

	@Field(value="SORTNO")
    private String sortno;

	@Field(value="FLAG_PD")
    private String flagPd;

	@Field(value="PK_ORD")
    private String pkOrd;

	@Field(value="DOSAGE")
    private String dosage;

	@Field(value="PK_UNIT_DOS")
    private String pkUnitDos;

	@Field(value="CODE_SUPPLY")
    private String codeSupply;

	@Field(value="CODE_FREQ")
    private String codeFreq;

	@Field(value="QUAN")
    private String quan;

	@Field(value="PK_UNIT")
    private String pkUnit;

	@Field(value="NOTE")
    private String note;

	@Field(value="FLAG_STOP")
    private String flagStop;

	@Field(value="MODITY_TIME")
    private Date modityTime;

	@Field(value="price")
    private String price;


    public String getPrice() {
		return price;
	}
	public void setPrice(String price) {
		this.price = price;
	}
	public String getPkOrdmt(){
        return this.pkOrdmt;
    }
    public void setPkOrdmt(String pkOrdmt){
        this.pkOrdmt = pkOrdmt;
    }

    public String getPkDept(){
        return this.pkDept;
    }
    public void setPkDept(String pkDept){
        this.pkDept = pkDept;
    }

    public String getSortno(){
        return this.sortno;
    }
    public void setSortno(String sortno){
        this.sortno = sortno;
    }

    public String getFlagPd(){
        return this.flagPd;
    }
    public void setFlagPd(String flagPd){
        this.flagPd = flagPd;
    }

    public String getPkOrd(){
        return this.pkOrd;
    }
    public void setPkOrd(String pkOrd){
        this.pkOrd = pkOrd;
    }


    public String getDosage() {
		return dosage;
	}
	public void setDosage(String dosage) {
		this.dosage = dosage;
	}
	public String getPkUnitDos(){
        return this.pkUnitDos;
    }
    public void setPkUnitDos(String pkUnitDos){
        this.pkUnitDos = pkUnitDos;
    }

    public String getCodeSupply(){
        return this.codeSupply;
    }
    public void setCodeSupply(String codeSupply){
        this.codeSupply = codeSupply;
    }

    public String getCodeFreq(){
        return this.codeFreq;
    }
    public void setCodeFreq(String codeFreq){
        this.codeFreq = codeFreq;
    }


    public String getQuan() {
		return quan;
	}
	public void setQuan(String quan) {
		this.quan = quan;
	}
	public String getPkUnit(){
        return this.pkUnit;
    }
    public void setPkUnit(String pkUnit){
        this.pkUnit = pkUnit;
    }

    public String getNote(){
        return this.note;
    }
    public void setNote(String note){
        this.note = note;
    }

    public String getFlagStop(){
        return this.flagStop;
    }
    public void setFlagStop(String flagStop){
        this.flagStop = flagStop;
    }

    public Date getModityTime(){
        return this.modityTime;
    }
    public void setModityTime(Date modityTime){
        this.modityTime = modityTime;
    }
}