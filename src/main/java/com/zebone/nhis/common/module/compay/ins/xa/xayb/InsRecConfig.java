package com.zebone.nhis.common.module.compay.ins.xa.xayb;

import java.util.Date;

import com.zebone.nhis.common.module.BaseModule;
import com.zebone.platform.modules.dao.build.au.Field;
import com.zebone.platform.modules.dao.build.au.PK;
import com.zebone.platform.modules.dao.build.au.Table;
import com.zebone.platform.modules.dao.build.au.Field.FieldType;
import com.zebone.platform.modules.dao.build.au.Field.KeyId;


/**
 * Table: INS_REC_CONFIG 
 *
 * @since 2017-12-14 09:38:43
 */
@Table(value="INS_REC_CONFIG")
public class InsRecConfig extends BaseModule  {

	
	/**
	 * 序列号
	 */
	private static final long serialVersionUID = -6979447012758851030L;

	@PK
	@Field(value="PK_INSRECCONFIG",id=KeyId.UUID)
    private String pkInsrecconfig;

	@Field(value="INSCODE")
    private String inscode;

	@Field(value="INSNAME")
    private String insname;

	@Field(value="INSDLL")
    private String insdll;

	@Field(value="INSNOTE")
    private String insnote;

	@Field(value="MODITY_TIME",date=FieldType.UPDATE)
    private Date modityTime;

    public String getPkInsrecconfig(){
        return this.pkInsrecconfig;
    }
    public void setPkInsrecconfig(String pkInsrecconfig){
        this.pkInsrecconfig = pkInsrecconfig;
    }

    public String getInscode(){
        return this.inscode;
    }
    public void setInscode(String inscode){
        this.inscode = inscode;
    }

    public String getInsname(){
        return this.insname;
    }
    public void setInsname(String insname){
        this.insname = insname;
    }

    public String getInsdll(){
        return this.insdll;
    }
    public void setInsdll(String insdll){
        this.insdll = insdll;
    }

    public String getInsnote(){
        return this.insnote;
    }
    
    public void setInsnote(String insnote){
        this.insnote = insnote;
    }
    
    public Date getModityTime(){
        return this.modityTime;
    }
    
    public void setModityTime(Date modityTime){
        this.modityTime = modityTime;
    }
}