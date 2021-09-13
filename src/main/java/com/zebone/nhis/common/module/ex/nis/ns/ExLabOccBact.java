package com.zebone.nhis.common.module.ex.nis.ns;

import java.util.Date;

import com.zebone.nhis.common.module.BaseModule;
import com.zebone.platform.modules.dao.build.au.Field;
import com.zebone.platform.modules.dao.build.au.PK;
import com.zebone.platform.modules.dao.build.au.Table;
import com.zebone.platform.modules.dao.build.au.Field.KeyId;


/**
 * Table: EX_LAB_OCC_BACT - ex_lab_occ_bact 
 *
 * @since 2016-10-28 10:58:27
 */
@Table(value="EX_LAB_OCC_BACT")
public class ExLabOccBact extends BaseModule  {

	@PK
	@Field(value="PK_BACT",id=KeyId.UUID)
    private String pkBact;

	@Field(value="PK_LABOCC")
    private String pkLabocc;

	@Field(value="SORT_NO")
    private Integer sortNo;

	@Field(value="BACTTYPE")
    private String bacttype;

	@Field(value="CODE_BACT")
    private String codeBact;

	@Field(value="NAME_BACT")
    private String nameBact;

	@Field(value="BACTCOL")
    private Double bactcol;

	@Field(value="NOTE")
    private String note;

	@Field(value="MODITY_TIME")
    private Date modityTime;
	
	@Field(value="NAME_PD")
    private String namePd;

	@Field(value="MIC")
    private int mic;
	
	@Field(value="EU_ALLEVEL")
    private String euAllevel;
	
	@Field(value="VAL_LAB")
    private String valLab;
	
    public String getValLab() {
		return valLab;
	}
	public void setValLab(String valLab) {
		this.valLab = valLab;
	}
	public String getNamePd() {
		return namePd;
	}
	public void setNamePd(String namePd) {
		this.namePd = namePd;
	}
	public int getMic() {
		return mic;
	}
	public void setMic(int mic) {
		this.mic = mic;
	}
	public String getEuAllevel() {
		return euAllevel;
	}
	public void setEuAllevel(String euAllevel) {
		this.euAllevel = euAllevel;
	}
	public String getPkBact(){
        return this.pkBact;
    }
    public void setPkBact(String pkBact){
        this.pkBact = pkBact;
    }

    public String getPkLabocc(){
        return this.pkLabocc;
    }
    public void setPkLabocc(String pkLabocc){
        this.pkLabocc = pkLabocc;
    }

    public Integer getSortNo(){
        return this.sortNo;
    }
    public void setSortNo(Integer sortNo){
        this.sortNo = sortNo;
    }

    public String getBacttype(){
        return this.bacttype;
    }
    public void setBacttype(String bacttype){
        this.bacttype = bacttype;
    }

    public String getCodeBact(){
        return this.codeBact;
    }
    public void setCodeBact(String codeBact){
        this.codeBact = codeBact;
    }

    public String getNameBact(){
        return this.nameBact;
    }
    public void setNameBact(String nameBact){
        this.nameBact = nameBact;
    }

    public Double getBactcol(){
        return this.bactcol;
    }
    public void setBactcol(Double bactcol){
        this.bactcol = bactcol;
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