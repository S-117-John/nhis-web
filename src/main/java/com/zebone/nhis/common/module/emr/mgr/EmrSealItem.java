package com.zebone.nhis.common.module.emr.mgr;

import java.util.Date;

import com.zebone.nhis.common.module.BaseModule;
import com.zebone.platform.modules.dao.build.au.Field;
import com.zebone.platform.modules.dao.build.au.PK;
import com.zebone.platform.modules.dao.build.au.Table;
import com.zebone.platform.modules.dao.build.au.Field.KeyId;


/**
 * Table: EMR_SEAL_ITEM 
 *
 * @since 2017-12-21 09:34:10
 */
@Table(value="EMR_SEAL_ITEM")
public class EmrSealItem extends BaseModule  {

	@PK
	@Field(value="PK_SEALITEM",id=KeyId.UUID)
    private String pkSealitem;

	@Field(value="PK_SEALREC")
    private String pkSealrec;

	@Field(value="DATE_APPLY")
    private Date dateApply;

	@Field(value="DATE_DEAL")
    private String dateDeal;

	@Field(value="DATE_ARCHIVE")
    private Date dateArchive;

	@Field(value="PK_REC")
    private String pkRec;

    /** EU_STATUS - 1-未处理，2-在处理，3-已处理，4-已完成 */
	@Field(value="EU_STATUS")
    private String euStatus;

	@Field(value="PK_EMP_ARCHIVE")
    private String pkEmpArchive;

	@Field(value="REMARK")
    private String remark;


    public String getPkSealitem(){
        return this.pkSealitem;
    }
    public void setPkSealitem(String pkSealitem){
        this.pkSealitem = pkSealitem;
    }

    public String getPkSealrec(){
        return this.pkSealrec;
    }
    public void setPkSealrec(String pkSealrec){
        this.pkSealrec = pkSealrec;
    }

    public Date getDateApply(){
        return this.dateApply;
    }
    public void setDateApply(Date dateApply){
        this.dateApply = dateApply;
    }

    public String getDateDeal(){
        return this.dateDeal;
    }
    public void setDateDeal(String dateDeal){
        this.dateDeal = dateDeal;
    }

    public Date getDateArchive(){
        return this.dateArchive;
    }
    public void setDateArchive(Date dateArchive){
        this.dateArchive = dateArchive;
    }

    public String getPkRec(){
        return this.pkRec;
    }
    public void setPkRec(String pkRec){
        this.pkRec = pkRec;
    }

    public String getEuStatus(){
        return this.euStatus;
    }
    public void setEuStatus(String euStatus){
        this.euStatus = euStatus;
    }

    public String getPkEmpArchive(){
        return this.pkEmpArchive;
    }
    public void setPkEmpArchive(String pkEmpArchive){
        this.pkEmpArchive = pkEmpArchive;
    }

    public String getRemark(){
        return this.remark;
    }
    public void setRemark(String remark){
        this.remark = remark;
    }
}