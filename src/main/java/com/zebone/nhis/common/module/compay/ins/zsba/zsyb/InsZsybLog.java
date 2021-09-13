package com.zebone.nhis.common.module.compay.ins.zsba.zsyb;

import java.util.Date;

import com.zebone.nhis.common.module.BaseModule;
import com.zebone.platform.modules.dao.build.au.Field;
import com.zebone.platform.modules.dao.build.au.PK;
import com.zebone.platform.modules.dao.build.au.Table;
import com.zebone.platform.modules.dao.build.au.Field.FieldType;
import com.zebone.platform.modules.dao.build.au.Field.KeyId;


/**
 * Table: ins_log - 外部医保-医保业务日志 
 *
 * @since 2017-09-06 10:42:10
 */
@Table(value="INS_LOG")
public class InsZsybLog extends BaseModule  {

	@PK
	@Field(value="PK_INSLOG",id=KeyId.UUID)
    private String pkInslog;

    /** EU_TYPE - 0 就诊登记，1 取消登记，2 中途结算，3 出院结算，4 取消结算 */
	@Field(value="EU_TYPE")
    private String euType;

	@Field(value="DATE_HAP")
    private Date dateHap;

	@Field(value="CODE_PI")
    private String codePi;

	@Field(value="NAME_PI")
    private String namePi;

	@Field(value="MEMO")
    private String memo;

	@Field(value="MODITY_TIME",date=FieldType.UPDATE)
    private Date modityTime;


    public String getPkInslog(){
        return this.pkInslog;
    }
    public void setPkInslog(String pkInslog){
        this.pkInslog = pkInslog;
    }

    public String getEuType(){
        return this.euType;
    }
    public void setEuType(String euType){
        this.euType = euType;
    }

    public Date getDateHap(){
        return this.dateHap;
    }
    public void setDateHap(Date dateHap){
        this.dateHap = dateHap;
    }

    public String getCodePi(){
        return this.codePi;
    }
    public void setCodePi(String codePi){
        this.codePi = codePi;
    }

    public String getNamePi(){
        return this.namePi;
    }
    public void setNamePi(String namePi){
        this.namePi = namePi;
    }

    public String getMemo(){
        return this.memo;
    }
    public void setMemo(String memo){
        this.memo = memo;
    }

    public Date getModityTime(){
        return this.modityTime;
    }
    public void setModityTime(Date modityTime){
        this.modityTime = modityTime;
    }
}