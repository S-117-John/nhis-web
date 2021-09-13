package com.zebone.nhis.cn.opdw.vo;

import java.util.Date;

import com.zebone.nhis.common.module.BaseModule;

public class bdOpEmrVo extends BaseModule  {

	    private String pkTemp;

		
	    private String pkTempcate;

		
	    private String code;

		
	    private String name;

		
	    private String spcode;

		
	    private String dCode;

		
	    private String pkDiag;

		
	    private String problem;

		
	    private String present;

		
	    private String history;

		
	    private String allergy;

		
	    private String examPhy;

		
	    private String examAux;

		
	    private String flagActive;

		
	    private String modifier;

		
	    private Date modityTime;
	    
	     private String diagName;

	      private  String cateName;

	    public String getDiagName() {
			return diagName;
		}
		public void setDiagName(String diagName) {
			this.diagName = diagName;
		}
		public String getPkTemp(){
	        return this.pkTemp;
	    }
	    public void setPkTemp(String pkTemp){
	        this.pkTemp = pkTemp;
	    }

	    public String getCateName(){ return cateName;}
	    public void setCateName(String cateName)
		{
			this.cateName=cateName;
		}
	    public String getPkTempcate(){
	        return this.pkTempcate;
	    }
	    public void setPkTempcate(String pkTempcate){
	        this.pkTempcate = pkTempcate;
	    }

	    public String getCode(){
	        return this.code;
	    }
	    public void setCode(String code){
	        this.code = code;
	    }

	    public String getName(){
	        return this.name;
	    }
	    public void setName(String name){
	        this.name = name;
	    }

	    public String getSpcode(){
	        return this.spcode;
	    }
	    public void setSpcode(String spcode){
	        this.spcode = spcode;
	    }

	    public String getdCode(){
	        return this.dCode;
	    }
	    public void setdCode(String dCode){
	        this.dCode = dCode;
	    }

	    public String getPkDiag(){
	        return this.pkDiag;
	    }
	    public void setPkDiag(String pkDiag){
	        this.pkDiag = pkDiag;
	    }

	    public String getProblem(){
	        return this.problem;
	    }
	    public void setProblem(String problem){
	        this.problem = problem;
	    }

	    public String getPresent(){
	        return this.present;
	    }
	    public void setPresent(String present){
	        this.present = present;
	    }

	    public String getHistory(){
	        return this.history;
	    }
	    public void setHistory(String history){
	        this.history = history;
	    }

	    public String getAllergy(){
	        return this.allergy;
	    }
	    public void setAllergy(String allergy){
	        this.allergy = allergy;
	    }

	    public String getExamPhy(){
	        return this.examPhy;
	    }
	    public void setExamPhy(String examPhy){
	        this.examPhy = examPhy;
	    }

	    public String getExamAux(){
	        return this.examAux;
	    }
	    public void setExamAux(String examAux){
	        this.examAux = examAux;
	    }

	    public String getFlagActive(){
	        return this.flagActive;
	    }
	    public void setFlagActive(String flagActive){
	        this.flagActive = flagActive;
	    }

	    public String getModifier(){
	        return this.modifier;
	    }
	    public void setModifier(String modifier){
	        this.modifier = modifier;
	    }

	    public Date getModityTime(){
	        return this.modityTime;
	    }
	    public void setModityTime(Date modityTime){
	        this.modityTime = modityTime;
	    }
	
}
