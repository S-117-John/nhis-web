package com.zebone.nhis.common.module.base.bd.res;


import com.zebone.nhis.common.module.BaseModule;
import com.zebone.platform.modules.dao.build.au.Field;
import com.zebone.platform.modules.dao.build.au.PK;
import com.zebone.platform.modules.dao.build.au.Table;
import com.zebone.platform.modules.dao.build.au.Field.KeyId;


/**
 * 手术台与设备关系
 * Table: BD_RES_OPT_FA  - bd_res_opt_fa 
 *
 * @since 2016-08-23 10:39:12
 */
@Table(value="BD_RES_OPT_FA")
public class BdResOptFa extends BaseModule  {
	@PK
	@Field(value="PK_OPTFA",id=KeyId.UUID)
    private String pkOptfa;

	@Field(value="PK_OPT")
    private String pkOpt;

	@Field(value="CODE_FA")
    private String codeFa;

    /** DT_FAROLE - 来自码表 */
	@Field(value="DT_FAROLE")
    private String dtFarole;

	@Field(value="NOTE")
    private String note;


    public String getPkOptfa(){
        return this.pkOptfa;
    }
    public void setPkOptfa(String pkOptfa){
        this.pkOptfa = pkOptfa;
    }

    public String getPkOpt(){
        return this.pkOpt;
    }
    public void setPkOpt(String pkOpt){
        this.pkOpt = pkOpt;
    }

    public String getCodeFa(){
        return this.codeFa;
    }
    public void setCodeFa(String codeFa){
        this.codeFa = codeFa;
    }

    public String getDtFarole(){
        return this.dtFarole;
    }
    public void setDtFarole(String dtFarole){
        this.dtFarole = dtFarole;
    }

    public String getNote(){
        return this.note;
    }
    public void setNote(String note){
        this.note = note;
    }
}