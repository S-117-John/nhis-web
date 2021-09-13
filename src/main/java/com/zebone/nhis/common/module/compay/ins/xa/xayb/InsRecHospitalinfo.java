package com.zebone.nhis.common.module.compay.ins.xa.xayb;

import java.util.Date;

import com.zebone.nhis.common.module.BaseModule;
import com.zebone.platform.modules.dao.build.au.Field;
import com.zebone.platform.modules.dao.build.au.PK;
import com.zebone.platform.modules.dao.build.au.Table;
import com.zebone.platform.modules.dao.build.au.Field.FieldType;
import com.zebone.platform.modules.dao.build.au.Field.KeyId;


/**
 * Table: INS_REC_HOSPITALINFO 
 *
 * @since 2017-12-14 09:39:25
 */
@Table(value="INS_REC_HOSPITALINFO")
public class InsRecHospitalinfo extends BaseModule  {

	/**
	 * 序列号
	 */
	private static final long serialVersionUID = -1485570763895078680L;

	@PK
	@Field(value="PK_HOSPITALINFO",id=KeyId.UUID)
    private String pkHospitalinfo;

	@Field(value="PK_MEDICALCODE")
    private String pkMedicalcode;

	@Field(value="HOSPITALNAME")
    private String hospitalname;

	@Field(value="NOTE")
    private String note;

	@Field(value="HOSPITALCONFIG")
    private String hospitalconfig;

	@Field(value="MODITY_TIME",date=FieldType.UPDATE)
    private Date modityTime;

	@Field(value="HOSPITALCODE")
    private String hospitalcode;

    public String getPkHospitalinfo(){
        return this.pkHospitalinfo;
    }
    public void setPkHospitalinfo(String pkHospitalinfo){
        this.pkHospitalinfo = pkHospitalinfo;
    }

    public String getPkMedicalcode(){
        return this.pkMedicalcode;
    }
    public void setPkMedicalcode(String pkMedicalcode){
        this.pkMedicalcode = pkMedicalcode;
    }

    public String getHospitalname(){
        return this.hospitalname;
    }
    public void setHospitalname(String hospitalname){
        this.hospitalname = hospitalname;
    }

    public String getNote(){
        return this.note;
    }
    public void setNote(String note){
        this.note = note;
    }

    public String getHospitalconfig(){
        return this.hospitalconfig;
    }
    public void setHospitalconfig(String hospitalconfig){
        this.hospitalconfig = hospitalconfig;
    }

    public Date getModityTime(){
        return this.modityTime;
    }
    public void setModityTime(Date modityTime){
        this.modityTime = modityTime;
    }

    public String getHospitalcode(){
        return this.hospitalcode;
    }
    public void setHospitalcode(String hospitalcode){
        this.hospitalcode = hospitalcode;
    }
}