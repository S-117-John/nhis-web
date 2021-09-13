package com.zebone.nhis.ma.pub.platform.send.impl.zsrm.vo.sch;

public class SchAppChanged {

   private String id;

   private String implicitRules;

   private String resourceType;

   private String codeOp;

   private String pkSchApp;

   private String codeOpChanged;

   public String getId() {
      return id;
   }

   public void setId(String id) {
      this.id = id;
   }

   public String getImplicitRules() {
      return implicitRules;
   }

   public void setImplicitRules(String implicitRules) {
      this.implicitRules = implicitRules;
   }

   public String getResourceType() {
      return resourceType;
   }

   public void setResourceType(String resourceType) {
      this.resourceType = resourceType;
   }

   public String getCodeOp() {
      return codeOp;
   }

   public void setCodeOp(String codeOp) {
      this.codeOp = codeOp;
   }

   public String getPkSchApp() {
      return pkSchApp;
   }

   public void setPkSchApp(String pkSchApp) {
      this.pkSchApp = pkSchApp;
   }

   public String getCodeOpChanged() {
      return codeOpChanged;
   }

   public void setCodeOpChanged(String codeOpChanged) {
      this.codeOpChanged = codeOpChanged;
   }
}
