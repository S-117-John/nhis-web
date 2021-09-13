
package com.zebone.nhis.ma.pub.lb.vo.referral;


import com.zebone.platform.modules.dao.build.au.Field;
import com.zebone.platform.modules.dao.build.au.Table;

@Table(value = "cn_emr_op")
public class TreatmentPlanning{
    @Field(value = "principle")
    private String treatment;
    public void setTreatment(String treatment) {
         this.treatment = treatment;
     }
     public String getTreatment() {
         return treatment;
     }
}