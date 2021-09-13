package com.zebone.nhis.common.module.scm.pub;


import com.zebone.nhis.common.module.BaseModule;
import com.zebone.platform.modules.dao.build.au.Field;
import com.zebone.platform.modules.dao.build.au.PK;
import com.zebone.platform.modules.dao.build.au.Table;
import com.zebone.platform.modules.dao.build.au.Field.KeyId;


/**
 * Table: BD_PD_ATT - bd_pd_att 
 *
 * @since 2016-10-29 09:38:45
 */
@Table(value="BD_PD_ATT")
public class BdPdAtt extends BaseModule  {

	@PK
	@Field(value="PK_PDATT",id=KeyId.UUID)
    private String pkPdatt;

	@Field(value="PK_PD")
    private String pkPd;

	@Field(value="PK_PDATTDEF")
    private String pkPdattdef;

	@Field(value="VAL_ATT")
    private String valAtt;


    public String getPkPdatt(){
        return this.pkPdatt;
    }
    public void setPkPdatt(String pkPdatt){
        this.pkPdatt = pkPdatt;
    }

    public String getPkPd(){
        return this.pkPd;
    }
    public void setPkPd(String pkPd){
        this.pkPd = pkPd;
    }

    public String getPkPdattdef(){
        return this.pkPdattdef;
    }
    public void setPkPdattdef(String pkPdattdef){
        this.pkPdattdef = pkPdattdef;
    }

    public String getValAtt(){
        return this.valAtt;
    }
    public void setValAtt(String valAtt){
        this.valAtt = valAtt;
    }
}