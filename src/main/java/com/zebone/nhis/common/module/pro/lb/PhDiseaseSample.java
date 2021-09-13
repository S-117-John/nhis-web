package com.zebone.nhis.common.module.pro.lb;

import com.zebone.nhis.common.module.BaseModule;
import com.zebone.platform.modules.dao.build.au.Field;
import com.zebone.platform.modules.dao.build.au.PK;
import com.zebone.platform.modules.dao.build.au.Table;

import java.math.BigDecimal;
import java.util.Date;

@Table(value="PH_DISEASE_SAMPLE")
public class PhDiseaseSample extends BaseModule {
    @PK
    @Field(value="PK_SAMPLE")
    private String pkSample;

    @Field(value="SORTNO")
    private Integer sortno;

    @Field(value="PK_FOODBORNE")
    private String pkFoodborne;

    @Field(value="CODE_SAMPLE")
    private String codeSample;

    @Field(value="EU_TYPE")
    private String euType;

    @Field(value="QUAN")
    private BigDecimal quan;

    @Field(value="DATE_SAMPLE")
    private Date dateSample;

    @Field(value="NOTE")
    private String note;

    public String getPkSample() {
        return pkSample;
    }

    public void setPkSample(String pkSample) {
        this.pkSample = pkSample;
    }

    public Integer getSortno() {
        return sortno;
    }

    public void setSortno(Integer sortno) {
        this.sortno = sortno;
    }

    public String getPkFoodborne() {
        return pkFoodborne;
    }

    public void setPkFoodborne(String pkFoodborne) {
        this.pkFoodborne = pkFoodborne;
    }

    public String getCodeSample() {
        return codeSample;
    }

    public void setCodeSample(String codeSample) {
        this.codeSample = codeSample;
    }

    public String getEuType() {
        return euType;
    }

    public void setEuType(String euType) {
        this.euType = euType;
    }

    public BigDecimal getQuan() {
        return quan;
    }

    public void setQuan(BigDecimal quan) {
        this.quan = quan;
    }

    public Date getDateSample() {
        return dateSample;
    }

    public void setDateSample(Date dateSample) {
        this.dateSample = dateSample;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }
}