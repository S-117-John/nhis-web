package com.zebone.nhis.common.module.ex.nis.ns;

import java.util.Date;

import com.zebone.nhis.common.module.BaseModule;
import com.zebone.platform.modules.dao.build.au.Field;
import com.zebone.platform.modules.dao.build.au.Field.KeyId;
import com.zebone.platform.modules.dao.build.au.PK;
import com.zebone.platform.modules.dao.build.au.Table;


/**
 * Table: PV_OPER_TRANS_ITEM 
 *
 * @since 2017-06-13 09:10:57
 */
@Table(value="PV_OPER_TRANS_ITEM")
public class PvOperTransItem  extends BaseModule {

	@PK
	@Field(value="PK_TRANSITEM",id=KeyId.UUID)
    private String pkTransitem;

	@Field(value="PK_OPERTRANS")
    private String pkOpertrans;

	@Field(value="DT_ITEM")
    private String dtItem;

	@Field(value="ABOUT_ITEM")
    private String aboutItem;

	@Field(value="DATE_EXEC")
    private Date dateExec;

	@Field(value="PK_EMP_EXEC")
    private String pkEmpExec;

	@Field(value="NAME_EMP_EXEC")
    private String nameEmpExec;

	@Field(value="DATE_CONFIRM")
    private Date dateConfirm;

	@Field(value="PK_EMP_CONFIRM")
    private String pkEmpConfirm;

	@Field(value="NAME_EMP_CONFIRM")
    private String nameEmpConfirm;

	@Field(value="MODIFY_TIME")
    private Date modifyTime;

    public String getPkTransitem(){
        return this.pkTransitem;
    }
    public void setPkTransitem(String pkTransitem){
        this.pkTransitem = pkTransitem;
    }

    public String getPkOpertrans(){
        return this.pkOpertrans;
    }
    public void setPkOpertrans(String pkOpertrans){
        this.pkOpertrans = pkOpertrans;
    }

    public String getDtItem() {
		return dtItem;
	}
	public void setDtItem(String dtItem) {
		this.dtItem = dtItem;
	}
	public String getAboutItem(){
        return this.aboutItem;
    }
    public void setAboutItem(String aboutItem){
        this.aboutItem = aboutItem;
    }

    public Date getDateExec(){
        return this.dateExec;
    }
    public void setDateExec(Date dateExec){
        this.dateExec = dateExec;
    }

    public String getPkEmpExec(){
        return this.pkEmpExec;
    }
    public void setPkEmpExec(String pkEmpExec){
        this.pkEmpExec = pkEmpExec;
    }

    public String getNameEmpExec(){
        return this.nameEmpExec;
    }
    public void setNameEmpExec(String nameEmpExec){
        this.nameEmpExec = nameEmpExec;
    }

    public Date getDateConfirm(){
        return this.dateConfirm;
    }
    public void setDateConfirm(Date dateConfirm){
        this.dateConfirm = dateConfirm;
    }

    public String getPkEmpConfirm(){
        return this.pkEmpConfirm;
    }
    public void setPkEmpConfirm(String pkEmpConfirm){
        this.pkEmpConfirm = pkEmpConfirm;
    }

    public String getNameEmpConfirm(){
        return this.nameEmpConfirm;
    }
    public void setNameEmpConfirm(String nameEmpConfirm){
        this.nameEmpConfirm = nameEmpConfirm;
    }

    public Date getModifyTime(){
        return this.modifyTime;
    }
    public void setModifyTime(Date modifyTime){
        this.modifyTime = modifyTime;
    }
  
}