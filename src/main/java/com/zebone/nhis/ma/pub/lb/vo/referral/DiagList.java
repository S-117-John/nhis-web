
package com.zebone.nhis.ma.pub.lb.vo.referral;


public class DiagList {

    private String diagnosticCode;
    private String diagnosticName;
    private String diagnosticMainSign;
    private String diagTypeCode;
    public void setDiagnosticCode(String diagnosticCode) {
         this.diagnosticCode = diagnosticCode;
     }
     public String getDiagnosticCode() {
         return diagnosticCode;
     }

    public void setDiagnosticName(String diagnosticName) {
         this.diagnosticName = diagnosticName;
     }
     public String getDiagnosticName() {
         return diagnosticName;
     }

    public void setDiagnosticMainSign(String diagnosticMainSign) {
         this.diagnosticMainSign = diagnosticMainSign;
     }
     public String getDiagnosticMainSign() {
         return diagnosticMainSign;
     }

    public void setDiagTypeCode(String diagTypeCode) {
         this.diagTypeCode = diagTypeCode;
     }
     public String getDiagTypeCode() {
         return diagTypeCode;
     }

}