
package com.zebone.nhis.ma.pub.lb.vo.referral;

public class MedicalAdvice {

    private String healthTemplate;
    private String healthRx;
    private String fourDiagnostic;
    public void setHealthTemplate(String healthTemplate) {
         this.healthTemplate = healthTemplate;
     }
     public String getHealthTemplate() {
         return healthTemplate;
     }

    public void setHealthRx(String healthRx) {
         this.healthRx = healthRx;
     }
     public String getHealthRx() {
         return healthRx;
     }

    public void setFourDiagnostic(String fourDiagnostic) {
         this.fourDiagnostic = fourDiagnostic;
     }
     public String getFourDiagnostic() {
         return fourDiagnostic;
     }

}