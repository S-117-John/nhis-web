package com.zebone.nhis.common.module.cn.ipdw;


import com.zebone.platform.modules.dao.build.au.Field;
import com.zebone.platform.modules.dao.build.au.Field.KeyId;
import com.zebone.platform.modules.dao.build.au.PK;
import com.zebone.platform.modules.dao.build.au.Table;


/**
 * Table: CN_PRES_DIAG 
 *
 * @since 2016-09-12 10:30:28
 */
@Table(value="CN_PRES_DIAG")
public class CnPresDiag   {

	@PK
	@Field(value="PK_PRESDIAG",id=KeyId.UUID)
    private String pkPresdiag;

	@Field(value="PK_PRES")
    private String pkPres;

	@Field(value="PK_DIAG")
    private String pkDiag;

	@Field(value="DESC_DIAG")
    private String descDiag;

	@Field(value="NOTE")
    private String note;


    public String getPkPresdiag(){
        return this.pkPresdiag;
    }
    public void setPkPresdiag(String pkPresdiag){
        this.pkPresdiag = pkPresdiag;
    }

    public String getPkPres(){
        return this.pkPres;
    }
    public void setPkPres(String pkPres){
        this.pkPres = pkPres;
    }

    public String getPkDiag(){
        return this.pkDiag;
    }
    public void setPkDiag(String pkDiag){
        this.pkDiag = pkDiag;
    }

    public String getDescDiag(){
        return this.descDiag;
    }
    public void setDescDiag(String descDiag){
        this.descDiag = descDiag;
    }

    public String getNote(){
        return this.note;
    }
    public void setNote(String note){
        this.note = note;
    }
}