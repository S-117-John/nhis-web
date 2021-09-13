package com.zebone.nhis.common.module.base.bd.res;


import com.zebone.nhis.common.module.BaseModule;
import com.zebone.platform.modules.dao.build.au.Field;
import com.zebone.platform.modules.dao.build.au.PK;
import com.zebone.platform.modules.dao.build.au.Table;
import com.zebone.platform.modules.dao.build.au.Field.KeyId;


/**
 * 医疗资源与设备关系
 * Table: BD_RES_MSP_FA  - bd_res_msp_fa 
 *
 * @since 2016-08-23 10:39:05
 */
@Table(value="BD_RES_MSP_FA")
public class BdResMspFa extends BaseModule  {

	@PK
	@Field(value="PK_MSPFA",id=KeyId.UUID)
    private String pkMspfa;

	@Field(value="PK_MSP")
    private String pkMsp;

	@Field(value="CODE_FA")
    private String codeFa;

    /** DT_FAROLE - 来自码表 */
	@Field(value="DT_FAROLE")
    private String dtFarole;

	@Field(value="NOTE")
    private String note;


    public String getPkMspfa(){
        return this.pkMspfa;
    }
    public void setPkMspfa(String pkMspfa){
        this.pkMspfa = pkMspfa;
    }

    public String getPkMsp(){
        return this.pkMsp;
    }
    public void setPkMsp(String pkMsp){
        this.pkMsp = pkMsp;
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