package com.zebone.nhis.common.module.pv;

import java.util.Date;

import com.zebone.nhis.common.module.BaseModule;
import com.zebone.platform.modules.dao.build.au.Field;
import com.zebone.platform.modules.dao.build.au.PK;
import com.zebone.platform.modules.dao.build.au.Table;
import com.zebone.platform.modules.dao.build.au.Field.KeyId;


/**
 * Table: PV_INFANT_GRADE - PV_INFANT_GRADE 
 *
 * @since 2017-05-17 05:24:40
 */
@Table(value="PV_INFANT_GRADE")
public class PvInfantGrade   extends BaseModule {

	@PK
	@Field(value="PK_INFANTGRADE",id=KeyId.UUID)
    private String pkInfantgrade;

	@Field(value="PK_INFANT")
    private String pkInfant;

	@Field(value="PK_PV")
    private String pkPv;

	@Field(value="SCORE_ONE")
    private Integer scoreOne;

	@Field(value="SCORE_FIVE")
    private Integer scoreFive;

	@Field(value="SCORE_TEN")
    private Integer scoreTen;

	@Field(value="PK_ITEM")
    private String pkItem;

	

	@Field(value="MODIFY_TIME")
    private Date modifyTime;

	


    public String getPkInfantgrade(){
        return this.pkInfantgrade;
    }
    public void setPkInfantgrade(String pkInfantgrade){
        this.pkInfantgrade = pkInfantgrade;
    }

    public String getPkInfant(){
        return this.pkInfant;
    }
    public void setPkInfant(String pkInfant){
        this.pkInfant = pkInfant;
    }

    public String getPkPv(){
        return this.pkPv;
    }
    public void setPkPv(String pkPv){
        this.pkPv = pkPv;
    }

    public Integer getScoreOne(){
        return this.scoreOne;
    }
    public void setScoreOne(Integer scoreOne){
        this.scoreOne = scoreOne;
    }

    public Integer getScoreFive(){
        return this.scoreFive;
    }
    public void setScoreFive(Integer scoreFive){
        this.scoreFive = scoreFive;
    }

    public Integer getScoreTen(){
        return this.scoreTen;
    }
    public void setScoreTen(Integer scoreTen){
        this.scoreTen = scoreTen;
    }

    public String getPkItem(){
        return this.pkItem;
    }
    public void setPkItem(String pkItem){
        this.pkItem = pkItem;
    }
    public Date getModifyTime(){
        return this.modifyTime;
    }
    public void setModifyTime(Date modifyTime){
        this.modifyTime = modifyTime;
    }

}