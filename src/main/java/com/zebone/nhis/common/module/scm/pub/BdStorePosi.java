package com.zebone.nhis.common.module.scm.pub;

import java.util.Date;

import com.zebone.nhis.common.module.BaseModule;
import com.zebone.platform.modules.dao.build.au.Field;
import com.zebone.platform.modules.dao.build.au.PK;
import com.zebone.platform.modules.dao.build.au.Table;
import com.zebone.platform.modules.dao.build.au.Field.KeyId;


/**
 * Table: BD_STORE_POSI 
 *
 * @since 2018-10-29 11:05:21
 */
@Table(value="BD_STORE_POSI")
public class BdStorePosi extends BaseModule  {

	@PK
	@Field(value="PK_POSI",id=KeyId.UUID)
    private String pkPosi;

	@Field(value="PK_STORE")
    private String pkStore;

	@Field(value="CODE_POS")
    private String codePos;

	@Field(value="SORTNO")
    private Integer sortno;

	@Field(value="NOTE")
    private String note;

	@Field(value="MODITY_TIME")
    private Date modityTime;


    public String getPkPosi(){
        return this.pkPosi;
    }
    public void setPkPosi(String pkPosi){
        this.pkPosi = pkPosi;
    }

    public String getPkStore(){
        return this.pkStore;
    }
    public void setPkStore(String pkStore){
        this.pkStore = pkStore;
    }

    public String getCodePos(){
        return this.codePos;
    }
    public void setCodePos(String codePos){
        this.codePos = codePos;
    }

    public Integer getSortno(){
        return this.sortno;
    }
    public void setSortno(Integer sortno){
        this.sortno = sortno;
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