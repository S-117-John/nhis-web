package com.zebone.nhis.common.module.scm.pub;

import java.util.Date;

import com.zebone.nhis.common.module.BaseModule;
import com.zebone.platform.modules.dao.build.au.Field;
import com.zebone.platform.modules.dao.build.au.PK;
import com.zebone.platform.modules.dao.build.au.Table;
import com.zebone.platform.modules.dao.build.au.Field.KeyId;


/**
 * Table: BD_HP_CGDIV_TMP 
 *
 * @since 2018-10-30 05:14:23
 */
@Table(value="BD_HP_CGDIV_TMP")
public class BdHpCgdivTmp extends BaseModule  {

	@PK
	@Field(value="PK_HPCGDIVTMP",id=KeyId.UUID)
    private String pkHpcgdivtmp;

	@Field(value="CODE_TMP")
    private String codeTmp;

	@Field(value="NAME_TMP")
    private String nameTmp;

	@Field(value="EU_TYPE")
    private String euType;

	@Field(value="PK_HPCGDIV")
    private String pkHpcgdiv;

	@Field(value="EU_DIVIDE")
    private String euDivide;

	@Field(value="RATE")
    private Double rate;

	@Field(value="MODIFY_TIME")
    private Date modifyTime;


    public String getPkHpcgdivtmp(){
        return this.pkHpcgdivtmp;
    }
    public void setPkHpcgdivtmp(String pkHpcgdivtmp){
        this.pkHpcgdivtmp = pkHpcgdivtmp;
    }

    public String getCodeTmp(){
        return this.codeTmp;
    }
    public void setCodeTmp(String codeTmp){
        this.codeTmp = codeTmp;
    }

    public String getNameTmp(){
        return this.nameTmp;
    }
    public void setNameTmp(String nameTmp){
        this.nameTmp = nameTmp;
    }

    public String getEuType(){
        return this.euType;
    }
    public void setEuType(String euType){
        this.euType = euType;
    }

    public String getPkHpcgdiv(){
        return this.pkHpcgdiv;
    }
    public void setPkHpcgdiv(String pkHpcgdiv){
        this.pkHpcgdiv = pkHpcgdiv;
    }

    public String getEuDivide(){
        return this.euDivide;
    }
    public void setEuDivide(String euDivide){
        this.euDivide = euDivide;
    }

    public Double getRate(){
        return this.rate;
    }
    public void setRate(Double rate){
        this.rate = rate;
    }

    public Date getModifyTime(){
        return this.modifyTime;
    }
    public void setModifyTime(Date modifyTime){
        this.modifyTime = modifyTime;
    }
}