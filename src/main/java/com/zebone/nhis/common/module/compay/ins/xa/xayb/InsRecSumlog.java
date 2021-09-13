package com.zebone.nhis.common.module.compay.ins.xa.xayb;

import java.util.Date;

import com.zebone.nhis.common.module.BaseModule;
import com.zebone.platform.modules.dao.build.au.Field;
import com.zebone.platform.modules.dao.build.au.PK;
import com.zebone.platform.modules.dao.build.au.Table;
import com.zebone.platform.modules.dao.build.au.Field.FieldType;
import com.zebone.platform.modules.dao.build.au.Field.KeyId;


/**
 * Table: INS_REC_SUMLOG  - 清算申请日志 
 *
 * @since 2017-12-14 09:40:03
 */
@Table(value="INS_REC_SUMLOG")
public class InsRecSumlog extends BaseModule {

	/**
	 * 序列号
	 */
	private static final long serialVersionUID = -5642552360980026517L;

	@PK
	@Field(value="PK_SUMLOG",id=KeyId.UUID)
    private String pkSumlog;

    /** HOSPITALCODE - 医院编号 */
	@Field(value="HOSPITALCODE")
    private String hospitalcode;

    /** QSQH - 申请周期号 */
	@Field(value="QSQH")
    private String qsqh;

    /** INSCODE - 医保编号 */
	@Field(value="INSCODE")
    private String inscode;

    /** MEDICALCODE - 医保编号 */
	@Field(value="MEDICALCODE")
    private String medicalcode;

    /** NOTE - 备注 */
	@Field(value="NOTE")
    private String note;

    /** SQLX - 1.HIS申请
		2.平台医保申请清算
		3.HIS清算撤销
		4.平台医保申请撤销 */
	@Field(value="SQLX")
    private String sqlx;

    /** SQDATE - 申请日期 */
	@Field(value="SQDATE")
    private Date sqdate;

	@Field(value="MODITY_TIME",date=FieldType.UPDATE)
    private Date modityTime;

    public String getPkSumlog(){
        return this.pkSumlog;
    }
    public void setPkSumlog(String pkSumlog){
        this.pkSumlog = pkSumlog;
    }

    public String getHospitalcode(){
        return this.hospitalcode;
    }
    public void setHospitalcode(String hospitalcode){
        this.hospitalcode = hospitalcode;
    }

    public String getQsqh(){
        return this.qsqh;
    }
    public void setQsqh(String qsqh){
        this.qsqh = qsqh;
    }

    public String getInscode(){
        return this.inscode;
    }
    public void setInscode(String inscode){
        this.inscode = inscode;
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

    public String getSqlx(){
        return this.sqlx;
    }
    public void setSqlx(String sqlx){
        this.sqlx = sqlx;
    }

    public Date getSqdate(){
        return this.sqdate;
    }
    public void setSqdate(Date sqdate){
        this.sqdate = sqdate;
    }

    public Date getModityTime(){
        return this.modityTime;
    }
    public void setModityTime(Date modityTime){
        this.modityTime = modityTime;
    }
}