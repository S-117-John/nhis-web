package com.zebone.nhis.common.module.cn.opdw;

import java.util.Date;

import com.zebone.nhis.common.module.BaseModule;
import com.zebone.platform.modules.dao.build.au.Field;
import com.zebone.platform.modules.dao.build.au.PK;
import com.zebone.platform.modules.dao.build.au.Table;
import com.zebone.platform.modules.dao.build.au.Field.KeyId;

/**
 * Table: PI_ORD_TEMP - pi_ord_temp 
 *
 * @since 2017-03-13 10:15:40
 */
@Table(value="PI_ORD_TEMP")
public class PiOrdTemp extends BaseModule  {

	@PK
	@Field(value="PK_ORDTEMP",id=KeyId.UUID)
    private String pkOrdtemp;

	@Field(value="PK_PI")
    private String pkPi;

	@Field(value="SORT_NO")
    private Integer sortNo;

	@Field(value="PK_ORD")
    private String pkOrd;

	@Field(value="DOSAGE")
    private Double dosage;

	@Field(value="PK_UNIT_DOS")
    private String pkUnitDos;

	@Field(value="QUAN")
    private Double quan;

	@Field(value="PK_UNIT")
    private String pkUnit;

	@Field(value="CODE_FREQ")
    private String codeFreq;

	@Field(value="CODE_SUPPLY")
    private String codeSupply;

	@Field(value="NOTE_SUPPLY")
    private String noteSupply;

	@Field(value="DAYS")
    private Integer days;

	@Field(value="NOTE")
    private String note;

	@Field(value="MODITY_TIME")
    private Date modityTime;


    public String getPkOrdtemp(){
        return this.pkOrdtemp;
    }
    public void setPkOrdtemp(String pkOrdtemp){
        this.pkOrdtemp = pkOrdtemp;
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

    public String getPkOrd(){
        return this.pkOrd;
    }
    public void setPkOrd(String pkOrd){
        this.pkOrd = pkOrd;
    }

    public Double getDosage(){
        return this.dosage;
    }
    public void setDosage(Double dosage){
        this.dosage = dosage;
    }

    public String getPkUnitDos(){
        return this.pkUnitDos;
    }
    public void setPkUnitDos(String pkUnitDos){
        this.pkUnitDos = pkUnitDos;
    }

    public Double getQuan(){
        return this.quan;
    }
    public void setQuan(Double quan){
        this.quan = quan;
    }

    public String getPkUnit(){
        return this.pkUnit;
    }
    public void setPkUnit(String pkUnit){
        this.pkUnit = pkUnit;
    }

    public String getCodeFreq(){
        return this.codeFreq;
    }
    public void setCodeFreq(String codeFreq){
        this.codeFreq = codeFreq;
    }

    public String getCodeSupply(){
        return this.codeSupply;
    }
    public void setCodeSupply(String codeSupply){
        this.codeSupply = codeSupply;
    }

    public String getNoteSupply(){
        return this.noteSupply;
    }
    public void setNoteSupply(String noteSupply){
        this.noteSupply = noteSupply;
    }

    public Integer getDays(){
        return this.days;
    }
    public void setDays(Integer days){
        this.days = days;
    }

    public String getNote(){
        return this.note;
    }
    public void setNote(String note){
        this.note = note;
    }

    public Date getModityTime(){
        return this.modityTime;
    }
    public void setModityTime(Date modityTime){
        this.modityTime = modityTime;
    }
    /**
	 * 数据更新状态
	 */
	private String rowStatus;
	
	public String getRowStatus() {
		return rowStatus;
	}
	public void setRowStatus(String rowStatus) {
		this.rowStatus = rowStatus;
	}    
}