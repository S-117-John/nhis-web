package com.zebone.nhis.common.module.scm.st;

import java.util.Date;

import com.zebone.nhis.common.module.BaseModule;
import com.zebone.platform.modules.dao.build.au.Field;
import com.zebone.platform.modules.dao.build.au.PK;
import com.zebone.platform.modules.dao.build.au.Table;
import com.zebone.platform.modules.dao.build.au.Field.KeyId;


/**
 * Table: PD_CC  - pd_cc 
 *
 * @since 2016-12-05 03:05:46
 */
@Table(value="PD_CC")
public class PdCc extends BaseModule  {

	private static final long serialVersionUID = 1L;

	@PK
	@Field(value="PK_PDCC",id=KeyId.UUID)
    private String pkPdcc;

	@Field(value="PK_DEPT")
    private String pkDept;

	@Field(value="PK_STORE")
    private String pkStore;

	@Field(value="MONTH_FIN")
    private Integer monthFin;

	@Field(value="DATE_BEGIN")
    private Date dateBegin;

	@Field(value="DATE_END")
    private Date dateEnd;

	@Field(value="PK_EMP_CC")
    private String pkEmpCc;

	@Field(value="NAME_EMP_CC")
    private String nameEmpCc;

	@Field(value="DATE_CC")
    private Date dateCc;

	@Field(value="NOTE")
    private String note;

	@Field(value="MODIFIER")
    private String modifier;


    public String getPkPdcc(){
        return this.pkPdcc;
    }
    public void setPkPdcc(String pkPdcc){
        this.pkPdcc = pkPdcc;
    }

    public String getPkDept(){
        return this.pkDept;
    }
    public void setPkDept(String pkDept){
        this.pkDept = pkDept;
    }

    public String getPkStore(){
        return this.pkStore;
    }
    public void setPkStore(String pkStore){
        this.pkStore = pkStore;
    }

    public Integer getMonthFin(){
        return this.monthFin;
    }
    public void setMonthFin(Integer monthFin){
        this.monthFin = monthFin;
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

    public String getPkEmpCc(){
        return this.pkEmpCc;
    }
    public void setPkEmpCc(String pkEmpCc){
        this.pkEmpCc = pkEmpCc;
    }

    public String getNameEmpCc(){
        return this.nameEmpCc;
    }
    public void setNameEmpCc(String nameEmpCc){
        this.nameEmpCc = nameEmpCc;
    }

    public Date getDateCc(){
        return this.dateCc;
    }
    public void setDateCc(Date dateCc){
        this.dateCc = dateCc;
    }

    public String getNote(){
        return this.note;
    }
    public void setNote(String note){
        this.note = note;
    }

    public String getModifier(){
        return this.modifier;
    }
    public void setModifier(String modifier){
        this.modifier = modifier;
    }
}