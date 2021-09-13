package com.zebone.nhis.common.module.compay.ins.xa.xayb;

import java.util.Date;

import com.zebone.nhis.common.module.BaseModule;
import com.zebone.platform.modules.dao.build.au.Field;
import com.zebone.platform.modules.dao.build.au.PK;
import com.zebone.platform.modules.dao.build.au.Table;
import com.zebone.platform.modules.dao.build.au.Field.FieldType;
import com.zebone.platform.modules.dao.build.au.Field.KeyId;


/**
 * Table: INS_REC_MEDICALCODE 
 *
 * @since 2017-12-14 09:40:43
 */
@Table(value="INS_REC_MEDICALCODE")
public class InsRecMedicalcode extends BaseModule {

	/**
	 * 序列号
	 */
	private static final long serialVersionUID = 7263089973542199063L;

	@PK
	@Field(value="PK_MEDICALCODE",id=KeyId.UUID)
	private String pkMedicalcode;

	@Field(value="PK_INSRECCONFIG")
    private String pkInsrecconfig;

	@Field(value="MEDICALCODE")
    private String medicalcode;

	@Field(value="NOTE")
    private String note;

	@Field(value="MODITY_TIME",date=FieldType.UPDATE)
    private Date modityTime;

    public String getPkMedicalcode(){
        return this.pkMedicalcode;
    }
    public void setPkMedicalcode(String pkMedicalcode){
        this.pkMedicalcode = pkMedicalcode;
    }

    public String getPkInsrecconfig(){
        return this.pkInsrecconfig;
    }
    public void setPkInsrecconfig(String pkInsrecconfig){
        this.pkInsrecconfig = pkInsrecconfig;
    }

    public String getMedicalcode(){
        return this.medicalcode;
    }
    public void setMedicalcode(String medicalcode){
        this.medicalcode = medicalcode;
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