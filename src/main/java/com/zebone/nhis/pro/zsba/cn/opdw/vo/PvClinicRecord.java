package com.zebone.nhis.pro.zsba.cn.opdw.vo;
import java.util.Date;

import com.zebone.nhis.common.module.BaseModule;
import com.zebone.platform.modules.dao.build.au.Field;
import com.zebone.platform.modules.dao.build.au.PK;
import com.zebone.platform.modules.dao.build.au.Table;
import com.zebone.platform.modules.dao.build.au.Field.KeyId;


/**
 * Table: PV_CLINIC_RECORD
 *
 * @since 2021-04-16 06:45:53
 */
@Table(value="PV_CLINIC_RECORD")
public class PvClinicRecord extends BaseModule  {

    @PK
    @Field(value="PK_PVCLIREC",id=KeyId.UUID)
    private String pkPvclirec;

    @Field(value="PK_PV")
    private String pkPv;

    @Field(value="PK_PI")
    private String pkPi;

    @Field(value="FLAG_FIRST")
    private String flagFirst;

    @Field(value="FLAG_ADMIT")
    private String flagAdmit;

    @Field(value="FLAG_COMMU_TRANS")
    private String flagCommuTrans;

    @Field(value="FLAG_APPT")
    private String flagAppt;

    @Field(value="FLAG_OPERATION_REVIEW")
    private String flagOperationReview;

    @Field(value="FLAG_AREA")
    private String flagArea;

    @Field(value="EU_AGEGROUP")
    private String euAgegroup;

    @Field(value="MODITY_TIME")
    private Date modityTime;

    @Field(value="TEMPERATURE")
    private Double temperature;

    @Field(value="BP")
    private String bp;

    public String getPkPvclirec(){
        return this.pkPvclirec;
    }
    public void setPkPvclirec(String pkPvclirec){
        this.pkPvclirec = pkPvclirec;
    }

    public String getPkPv(){
        return this.pkPv;
    }
    public void setPkPv(String pkPv){
        this.pkPv = pkPv;
    }

    public String getPkPi(){
        return this.pkPi;
    }
    public void setPkPi(String pkPi){
        this.pkPi = pkPi;
    }

    public String getFlagFirst(){
        return this.flagFirst;
    }
    public void setFlagFirst(String flagFirst){
        this.flagFirst = flagFirst;
    }

    public String getFlagAdmit(){
        return this.flagAdmit;
    }
    public void setFlagAdmit(String flagAdmit){
        this.flagAdmit = flagAdmit;
    }

    public String getFlagCommuTrans(){
        return this.flagCommuTrans;
    }
    public void setFlagCommuTrans(String flagCommuTrans){
        this.flagCommuTrans = flagCommuTrans;
    }

    public String getFlagAppt(){
        return this.flagAppt;
    }
    public void setFlagAppt(String flagAppt){
        this.flagAppt = flagAppt;
    }

    public String getFlagOperationReview(){
        return this.flagOperationReview;
    }
    public void setFlagOperationReview(String flagOperationReview){
        this.flagOperationReview = flagOperationReview;
    }

    public String getFlagArea(){
        return this.flagArea;
    }
    public void setFlagArea(String flagArea){
        this.flagArea = flagArea;
    }

    public String getEuAgegroup(){
        return this.euAgegroup;
    }
    public void setEuAgegroup(String euAgegroup){
        this.euAgegroup = euAgegroup;
    }

    public Date getModityTime(){
        return this.modityTime;
    }
    public void setModityTime(Date modityTime){
        this.modityTime = modityTime;
    }

    public Double getTemperature() {
        return temperature;
    }

    public void setTemperature(Double temperature) {
        this.temperature = temperature;
    }

    public String getBp() {
        return bp;
    }

    public void setBp(String bp) {
        this.bp = bp;
    }
}
