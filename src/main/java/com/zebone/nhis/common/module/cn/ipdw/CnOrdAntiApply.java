package com.zebone.nhis.common.module.cn.ipdw;

import java.util.Date;

import com.zebone.nhis.common.module.BaseModule;
import com.zebone.platform.modules.dao.build.au.Field;
import com.zebone.platform.modules.dao.build.au.PK;
import com.zebone.platform.modules.dao.build.au.Table;
import com.zebone.platform.modules.dao.build.au.Field.KeyId;


/**
 * Table: CN_ORD_ANTI_APPLY 
 *
 * @since 2019-01-23 03:11:24
 */
@Table(value="CN_ORD_ANTI_APPLY")
public class CnOrdAntiApply extends BaseModule  {

	@PK
	@Field(value="PK_ORDANTIAPPLY",id=KeyId.UUID)
    private String pkOrdantiapply;

	@Field(value="PK_CNORD")
    private String pkCnord;

	@Field(value="HISTORY")
    private String history;

	@Field(value="TEMPERATURE")
    private Double temperature;

	@Field(value="EU_ALGOR")
    private String euAlgor;

	@Field(value="WBC")
    private Double wbc;

	@Field(value="NEUTROPHIL")
    private Double neutrophil;

	@Field(value="EU_NP")
    private String euNp;

	@Field(value="EU_IDVC")
    private String euIdvc;

	@Field(value="EU_IDVC48")
    private String euIdvc48;

	@Field(value="EU_PADE")
    private String euPade;

	@Field(value="PADE_BACT")
    private String padeBact;

	@Field(value="FLAG_DETE")
    private String flagDete;

	@Field(value="EU_DST")
    private String euDst;

	@Field(value="EU_AGED")
    private String euAged;

	@Field(value="EU_USEANTI")
    private String euUseanti;

	@Field(value="ANTI")
    private String anti;

	@Field(value="DIAGNAME")
    private String diagname;

	@Field(value="ITEM_INFECTION")
    private String itemInfection;

	@Field(value="ITEM_LOWIMM")
    private String itemLowimm;

	@Field(value="ITEM_CANCURE")
    private String itemCancure;

	@Field(value="CONSDEPT")
    private String consdept;

	@Field(value="CONSDR")
    private String consdr;

	@Field(value="DATE_CONS")
    private Date dateCons;

	@Field(value="EU_OPINION")
    private String euOpinion;

	@Field(value="NOTE")
    private String note;

	@Field(value="FLAG_PRINT")
    private String flagPrint;

	@Field(value="DATE_PRINT")
    private Date datePrint;

	@Field(value="PK_EMP_PRINT")
    private String pkEmpPrint;

	@Field(value="NAME_EMP_PRINT")
    private String nameEmpPrint;

	@Field(value="MODIFIER")
    private String modifier;

	@Field(value="MODITY_TIME")
    private Date modityTime;


    public String getPkOrdantiapply(){
        return this.pkOrdantiapply;
    }
    public void setPkOrdantiapply(String pkOrdantiapply){
        this.pkOrdantiapply = pkOrdantiapply;
    }

    public String getPkCnord(){
        return this.pkCnord;
    }
    public void setPkCnord(String pkCnord){
        this.pkCnord = pkCnord;
    }

    public String getHistory(){
        return this.history;
    }
    public void setHistory(String history){
        this.history = history;
    }

    public Double getTemperature(){
        return this.temperature;
    }
    public void setTemperature(Double temperature){
        this.temperature = temperature;
    }

    public String getEuAlgor(){
        return this.euAlgor;
    }
    public void setEuAlgor(String euAlgor){
        this.euAlgor = euAlgor;
    }

    public Double getWbc(){
        return this.wbc;
    }
    public void setWbc(Double wbc){
        this.wbc = wbc;
    }

    public Double getNeutrophil(){
        return this.neutrophil;
    }
    public void setNeutrophil(Double neutrophil){
        this.neutrophil = neutrophil;
    }

    public String getEuNp(){
        return this.euNp;
    }
    public void setEuNp(String euNp){
        this.euNp = euNp;
    }

    public String getEuIdvc(){
        return this.euIdvc;
    }
    public void setEuIdvc(String euIdvc){
        this.euIdvc = euIdvc;
    }

    public String getEuIdvc48(){
        return this.euIdvc48;
    }
    public void setEuIdvc48(String euIdvc48){
        this.euIdvc48 = euIdvc48;
    }

    public String getEuPade(){
        return this.euPade;
    }
    public void setEuPade(String euPade){
        this.euPade = euPade;
    }

    public String getPadeBact(){
        return this.padeBact;
    }
    public void setPadeBact(String padeBact){
        this.padeBact = padeBact;
    }

    public String getFlagDete(){
        return this.flagDete;
    }
    public void setFlagDete(String flagDete){
        this.flagDete = flagDete;
    }

    public String getEuDst(){
        return this.euDst;
    }
    public void setEuDst(String euDst){
        this.euDst = euDst;
    }

    public String getEuAged(){
        return this.euAged;
    }
    public void setEuAged(String euAged){
        this.euAged = euAged;
    }

    public String getEuUseanti(){
        return this.euUseanti;
    }
    public void setEuUseanti(String euUseanti){
        this.euUseanti = euUseanti;
    }

    public String getAnti(){
        return this.anti;
    }
    public void setAnti(String anti){
        this.anti = anti;
    }

    public String getDiagname(){
        return this.diagname;
    }
    public void setDiagname(String diagname){
        this.diagname = diagname;
    }

    public String getItemInfection(){
        return this.itemInfection;
    }
    public void setItemInfection(String itemInfection){
        this.itemInfection = itemInfection;
    }

    public String getItemLowimm(){
        return this.itemLowimm;
    }
    public void setItemLowimm(String itemLowimm){
        this.itemLowimm = itemLowimm;
    }

    public String getItemCancure(){
        return this.itemCancure;
    }
    public void setItemCancure(String itemCancure){
        this.itemCancure = itemCancure;
    }

    public String getConsdept(){
        return this.consdept;
    }
    public void setConsdept(String consdept){
        this.consdept = consdept;
    }

    public String getConsdr(){
        return this.consdr;
    }
    public void setConsdr(String consdr){
        this.consdr = consdr;
    }

    public Date getDateCons(){
        return this.dateCons;
    }
    public void setDateCons(Date dateCons){
        this.dateCons = dateCons;
    }

    public String getEuOpinion(){
        return this.euOpinion;
    }
    public void setEuOpinion(String euOpinion){
        this.euOpinion = euOpinion;
    }

    public String getNote(){
        return this.note;
    }
    public void setNote(String note){
        this.note = note;
    }

    public String getFlagPrint(){
        return this.flagPrint;
    }
    public void setFlagPrint(String flagPrint){
        this.flagPrint = flagPrint;
    }

    public Date getDatePrint(){
        return this.datePrint;
    }
    public void setDatePrint(Date datePrint){
        this.datePrint = datePrint;
    }

    public String getPkEmpPrint(){
        return this.pkEmpPrint;
    }
    public void setPkEmpPrint(String pkEmpPrint){
        this.pkEmpPrint = pkEmpPrint;
    }

    public String getNameEmpPrint(){
        return this.nameEmpPrint;
    }
    public void setNameEmpPrint(String nameEmpPrint){
        this.nameEmpPrint = nameEmpPrint;
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