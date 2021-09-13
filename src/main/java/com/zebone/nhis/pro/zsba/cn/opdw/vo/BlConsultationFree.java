package com.zebone.nhis.pro.zsba.cn.opdw.vo;

import com.zebone.nhis.common.module.BaseModule;
import com.zebone.platform.modules.dao.build.au.Field;
import com.zebone.platform.modules.dao.build.au.Field.FieldType;
import com.zebone.platform.modules.dao.build.au.PK;
import com.zebone.platform.modules.dao.build.au.Table;

import java.util.Date;

/**
 * Table: BL_CONSULTATION_FREE
 *
 * @since 2021-03-18 08:44:50
 */
@Table(value="BL_CONSULTATION_FREE")
public class BlConsultationFree extends BaseModule {

    @PK
    @Field(value="PK_CONFREE",id= Field.KeyId.UUID)
    private String pkConfree;

    @Field(value="NAME")
    private String name;

    @Field(value="EU_TYPE")
    private String euType;

    @Field(value="EU_REL")
    private String euRel;

    @Field(value="PK_ORD")
    private String pkOrd;

    @Field(value="PK_DIAG")
    private String pkDiag;
    @Field(value="code")
    private String code;
    @Field(value="FLAG_STOP")
    private String flagStop;

    @Field(value="MODIFY_TIME",date=FieldType.ALL)
    private Date modifyTime;


    public String getPkConfree(){
        return this.pkConfree;
    }
    public void setPkConfree(String pkConfree){
        this.pkConfree = pkConfree;
    }

	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getEuType(){
        return this.euType;
    }
    public void setEuType(String euType){
        this.euType = euType;
    }

    public String getEuRel(){
        return this.euRel;
    }
    public void setEuRel(String euRel){
        this.euRel = euRel;
    }

    public String getPkOrd(){
        return this.pkOrd;
    }
    public void setPkOrd(String pkOrd){
        this.pkOrd = pkOrd;
    }

    public String getPkDiag(){
        return this.pkDiag;
    }
    public void setPkDiag(String pkDiag){
        this.pkDiag = pkDiag;
    }

    public String getFlagStop(){
        return this.flagStop;
    }
    public void setFlagStop(String flagStop){
        this.flagStop = flagStop;
    }

    public Date getModifyTime(){
        return this.modifyTime;
    }
    public void setModifyTime(Date modifyTime){
        this.modifyTime = modifyTime;
    }

    @Override
    public String toString() {
        return "BlConsultationFree{" +
                "pkConfree='" + pkConfree + '\'' +
                ", euType='" + euType + '\'' +
                ", euRel='" + euRel + '\'' +
                ", pkOrd='" + pkOrd + '\'' +
                ", pkDiag='" + pkDiag + '\'' +
                ", flagStop='" + flagStop + '\'' +
                ", modifyTime=" + modifyTime +
                '}';
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
