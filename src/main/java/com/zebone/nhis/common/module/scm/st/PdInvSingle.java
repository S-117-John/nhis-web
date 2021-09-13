package com.zebone.nhis.common.module.scm.st;

import java.util.Date;

import com.zebone.nhis.common.module.BaseModule;
import com.zebone.platform.modules.dao.build.au.Field;
import com.zebone.platform.modules.dao.build.au.PK;
import com.zebone.platform.modules.dao.build.au.Table;
import com.zebone.platform.modules.dao.build.au.Field.KeyId;


/**
 * Table: PD_INV_SINGLE 
 *
 * @since 2018-08-13 07:28:48
 */
@SuppressWarnings("serial")
@Table(value="PD_INV_SINGLE")
public class PdInvSingle extends BaseModule  {

	@PK
	@Field(value="PK_INVSINGLE",id=KeyId.UUID)
    private String pkInvsingle;

	@Field(value="PK_PDINVDT")
    private String pkPdinvdt;

	@Field(value="PK_PD")
    private String pkPd;

	@Field(value="BARCODE")
    private String barcode;

	@Field(value="EU_STATUS")
    private String euStatus;

	@Field(value="NOTE")
    private String note;

	@Field(value="MODITY_TIME")
    private Date modityTime;


    public String getPkInvsingle(){
        return this.pkInvsingle;
    }
    public void setPkInvsingle(String pkInvsingle){
        this.pkInvsingle = pkInvsingle;
    }

    public String getPkPdinvdt(){
        return this.pkPdinvdt;
    }
    public void setPkPdinvdt(String pkPdinvdt){
        this.pkPdinvdt = pkPdinvdt;
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

    public String getEuStatus(){
        return this.euStatus;
    }
    public void setEuStatus(String euStatus){
        this.euStatus = euStatus;
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