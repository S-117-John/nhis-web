package com.zebone.nhis.common.module.ex.nis.ns;

import java.util.Date;

import com.zebone.nhis.common.module.BaseModule;
import com.zebone.platform.modules.dao.build.au.Field;
import com.zebone.platform.modules.dao.build.au.PK;
import com.zebone.platform.modules.dao.build.au.Table;
import com.zebone.platform.modules.dao.build.au.Field.KeyId;


/**
 * Table: EX_LAB_OCC_BACT_AL - ex_lab_occ_bact_al 
 *
 * @since 2016-10-28 10:59:01
 */
@Table(value="EX_LAB_OCC_BACT_AL")
public class ExLabOccBactAl extends BaseModule  {

	@PK
	@Field(value="PK_BACTAL",id=KeyId.UUID)
    private String pkBactal;

	@Field(value="PK_BACT")
    private String pkBact;

	@Field(value="SORT_NO")
    private Integer sortNo;

	@Field(value="CODE_PD")
    private String codePd;

	@Field(value="NAME_PD")
    private String namePd;

	@Field(value="MIC")
    private Double mic;

	@Field(value="VAL_LAB")
    private String valLab;

    /** EU_ALLEVEL - 0 敏感，1 中等，2 耐药 */
	@Field(value="EU_ALLEVEL")
    private String euAllevel;

	@Field(value="NOTE")
    private String note;

	@Field(value="MODITY_TIME")
    private Date modityTime;


    public String getPkBactal(){
        return this.pkBactal;
    }
    public void setPkBactal(String pkBactal){
        this.pkBactal = pkBactal;
    }

    public String getPkBact(){
        return this.pkBact;
    }
    public void setPkBact(String pkBact){
        this.pkBact = pkBact;
    }

    public Integer getSortNo(){
        return this.sortNo;
    }
    public void setSortNo(Integer sortNo){
        this.sortNo = sortNo;
    }

    public String getCodePd(){
        return this.codePd;
    }
    public void setCodePd(String codePd){
        this.codePd = codePd;
    }

    public String getNamePd(){
        return this.namePd;
    }
    public void setNamePd(String namePd){
        this.namePd = namePd;
    }

    public Double getMic(){
        return this.mic;
    }
    public void setMic(Double mic){
        this.mic = mic;
    }

    public String getValLab(){
        return this.valLab;
    }
    public void setValLab(String valLab){
        this.valLab = valLab;
    }

    public String getEuAllevel(){
        return this.euAllevel;
    }
    public void setEuAllevel(String euAllevel){
        this.euAllevel = euAllevel;
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
}