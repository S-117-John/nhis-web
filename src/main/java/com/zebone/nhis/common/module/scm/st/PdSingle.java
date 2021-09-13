package com.zebone.nhis.common.module.scm.st;
import java.util.Date;

import com.zebone.nhis.common.module.BaseModule;
import com.zebone.platform.modules.dao.build.au.Field;
import com.zebone.platform.modules.dao.build.au.PK;
import com.zebone.platform.modules.dao.build.au.Table;
import com.zebone.platform.modules.dao.build.au.Field.KeyId;


/**
 * Table: PD_SINGLE 
 *
 * @since 2018-08-15 07:28:16
 */
@Table(value="PD_SINGLE")
public class PdSingle extends BaseModule  {

	@PK
	@Field(value="PK_SINGLE",id=KeyId.UUID)
    private String pkSingle;

	@Field(value="PK_DEPT")
    private String pkDept;

	@Field(value="PK_STORE")
    private String pkStore;

	@Field(value="PK_PD")
    private String pkPd;

	@Field(value="BARCODE")
    private String barcode;

	@Field(value="FLAG_IN")
    private String flagIn;

	@Field(value="FLAG_OUT")
    private String flagOut;

	@Field(value="EU_STATUS")
    private String euStatus;

	@Field(value="PK_PDSTDT_IN")
    private String pkPdstdtIn;

	@Field(value="PK_PDSTDT_OUT")
    private String pkPdstdtOut;

	@Field(value="NOTE")
    private String note;

	@Field(value="MODITY_TIME")
    private Date modityTime;


    public String getPkSingle(){
        return this.pkSingle;
    }
    public void setPkSingle(String pkSingle){
        this.pkSingle = pkSingle;
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

    public String getPkPd(){
        return this.pkPd;
    }
    public void setPkPd(String pkPd){
        this.pkPd = pkPd;
    }

    public String getBarcode(){
        return this.barcode;
    }
    public void setBarcode(String barcode){
        this.barcode = barcode;
    }

    public String getFlagIn(){
        return this.flagIn;
    }
    public void setFlagIn(String flagIn){
        this.flagIn = flagIn;
    }

    public String getFlagOut(){
        return this.flagOut;
    }
    public void setFlagOut(String flagOut){
        this.flagOut = flagOut;
    }

    public String getEuStatus(){
        return this.euStatus;
    }
    public void setEuStatus(String euStatus){
        this.euStatus = euStatus;
    }

    public String getPkPdstdtIn(){
        return this.pkPdstdtIn;
    }
    public void setPkPdstdtIn(String pkPdstdtIn){
        this.pkPdstdtIn = pkPdstdtIn;
    }

    public String getPkPdstdtOut(){
        return this.pkPdstdtOut;
    }
    public void setPkPdstdtOut(String pkPdstdtOut){
        this.pkPdstdtOut = pkPdstdtOut;
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