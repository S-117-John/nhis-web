package com.zebone.nhis.common.module.labor.nis;

import com.zebone.nhis.common.module.BaseModule;
import com.zebone.platform.modules.dao.build.au.Field;
import com.zebone.platform.modules.dao.build.au.Field.KeyId;
import com.zebone.platform.modules.dao.build.au.PK;
import com.zebone.platform.modules.dao.build.au.Table;

import java.math.BigDecimal;
import java.util.Date;


/**
 * Table: PV_LABOR_INSTRU_DT - 器械清点明细 
 *
 * @since 2017-05-24 03:08:03
 */
@Table(value="PV_LABOR_INSTRU_DT")
public class PvLaborInstruDt  extends BaseModule {

	@PK
	@Field(value="PK_INSTRU_DT",id=KeyId.UUID)
    private String pkInstruDt;

	@Field(value="PK_INSTRU")
    private String pkInstru;

	@Field(value="DT_INSTRUMENT")
    private String dtInstrument;

	@Field(value="CNT_PER")
    private BigDecimal cntPer;

    @Field(value="CNT_IN")
    private BigDecimal cntIn;

	@Field(value="CNT_AFTER")
    private BigDecimal cntAfter;

	@Field(value="MODIFY_TIME")
    private Date modifyTime;

    public String getPkInstruDt(){
        return this.pkInstruDt;
    }
    public void setPkInstruDt(String pkInstruDt){
        this.pkInstruDt = pkInstruDt;
    }

    public String getPkInstru(){
        return this.pkInstru;
    }
    public void setPkInstru(String pkInstru){
        this.pkInstru = pkInstru;
    }

    public String getDtInstrument(){
        return this.dtInstrument;
    }
    public void setDtInstrument(String dtInstrument){
        this.dtInstrument = dtInstrument;
    }

    public BigDecimal getCntPer(){
        return this.cntPer;
    }
    public void setCntPer(BigDecimal cntPer){
        this.cntPer = cntPer;
    }

    public BigDecimal getCntAfter(){
        return this.cntAfter;
    }
    public void setCntAfter(BigDecimal cntAfter){
        this.cntAfter = cntAfter;
    }

    public Date getModifyTime(){
        return this.modifyTime;
    }
    public void setModifyTime(Date modifyTime){
        this.modifyTime = modifyTime;
    }

    public BigDecimal getCntIn() {return cntIn;}
    public void setCntIn(BigDecimal cntIn) {this.cntIn = cntIn;}
}