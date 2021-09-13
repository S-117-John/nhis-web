package com.zebone.nhis.common.module.cn.cp;

import java.util.Date;
import java.util.List;

import com.zebone.nhis.common.module.BaseModule;
import com.zebone.platform.modules.dao.build.au.Field;
import com.zebone.platform.modules.dao.build.au.Field.KeyId;
import com.zebone.platform.modules.dao.build.au.PK;
import com.zebone.platform.modules.dao.build.au.Table;


/**
 * Table: CP_TEMP_PHASE 
 *
 * @since 2016-12-12 02:43:26
 */
@Table(value="CP_TEMP_PHASE")
public class CpTempPhase extends BaseModule  {

	@PK
	@Field(value="PK_CPPHASE",id=KeyId.UUID)
    private String pkCpphase;

	@Field(value="PK_CPTEMP")
    private String pkCptemp;

	@Field(value="SORTNO")
    private Integer sortno;

	@Field(value="CODE_PHASE")
    private String codePhase;

	@Field(value="NAME_PHASE")
    private String namePhase;

	@Field(value="DESC_PHASE")
    private String descPhase;

	@Field(value="DAYS_MIN")
    private Integer daysMin;

	@Field(value="DAYS_MAX")
    private Integer daysMax;

	@Field(value="PK_CPPHASE_PRE")
    private String pkCpphasePre;

	@Field(value="MODIFIER")
    private String modifier;

	@Field(value="MODITY_TIME")
    private Date modityTime;
	
    public String getPkCpphase(){
        return this.pkCpphase;
    }
    public void setPkCpphase(String pkCpphase){
        this.pkCpphase = pkCpphase;
    }

    public String getPkCptemp(){
        return this.pkCptemp;
    }
    public void setPkCptemp(String pkCptemp){
        this.pkCptemp = pkCptemp;
    }

    public Integer getSortno(){
        return this.sortno;
    }
    public void setSortno(Integer sortno){
        this.sortno = sortno;
    }

    public String getCodePhase(){
        return this.codePhase;
    }
    public void setCodePhase(String codePhase){
        this.codePhase = codePhase;
    }

    public String getNamePhase(){
        return this.namePhase;
    }
    public void setNamePhase(String namePhase){
        this.namePhase = namePhase;
    }

    public String getDescPhase(){
        return this.descPhase;
    }
    public void setDescPhase(String descPhase){
        this.descPhase = descPhase;
    }

    public Integer getDaysMin(){
        return this.daysMin;
    }
    public void setDaysMin(Integer daysMin){
        this.daysMin = daysMin;
    }

    public Integer getDaysMax(){
        return this.daysMax;
    }
    public void setDaysMax(Integer daysMax){
        this.daysMax = daysMax;
    }

    public String getPkCpphasePre(){
        return this.pkCpphasePre;
    }
    public void setPkCpphasePre(String pkCpphasePre){
        this.pkCpphasePre = pkCpphasePre;
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

    
	private List<CpTempOrd> orderList;
	private List<CpTempWork> workList;
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
	public List<CpTempOrd> getOrderList() {
		return orderList;
	}
	public void setOrderList(List<CpTempOrd> orderList) {
		this.orderList = orderList;
	}
	public List<CpTempWork> getWorkList() {
		return workList;
	}
	public void setWorkList(List<CpTempWork> workList) {
		this.workList = workList;
	}

}