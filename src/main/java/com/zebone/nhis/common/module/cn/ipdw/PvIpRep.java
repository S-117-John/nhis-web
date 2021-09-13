package com.zebone.nhis.common.module.cn.ipdw;

import java.util.Date;

import com.zebone.nhis.common.module.BaseModule;
import com.zebone.platform.modules.dao.build.au.Field;
import com.zebone.platform.modules.dao.build.au.PK;
import com.zebone.platform.modules.dao.build.au.Table;
import com.zebone.platform.modules.dao.build.au.Field.KeyId;


/**
 * Table: PV_IP_REP 
 *
 * @since 2017-10-31 04:28:44
 */
@Table(value="PV_IP_REP")
public class PvIpRep extends BaseModule  {

	@PK
	@Field(value="PK_IPREP",id=KeyId.UUID)
    private String pkIprep;

	@Field(value="PK_PV")
    private String pkPv;

	@Field(value="PK_DIAG")
    private String pkDiag;

	@Field(value="FLAG_HP")
    private String flagHp;

	@Field(value="FLAG_PAT")
    private String flagPat;

	@Field(value="FLAG_DISE")
    private String flagDise;

	@Field(value="FLAG_COMP_DISE")
    private String flagCompDise;

	@Field(value="FLAG_REJ")
    private String flagRej;

	@Field(value="FLAG_COMP_MED")
    private String flagCompMed;

	@Field(value="FLAG_INF")
    private String flagInf;

	@Field(value="FLAG_DELAY")
    private String flagDelay;

	@Field(value="FLAG_OVER")
    private String flagOver;

	@Field(value="FLAG_OTH")
    private String flagOth;

	@Field(value="DESC_OTH")
    private String descOth;

	@Field(value="DESC_SOC")
    private String descSoc;

	@Field(value="DESC_ADV")
    private String descAdv;

	@Field(value="PK_EMP_REP")
    private String pkEmpRep;

	@Field(value="NAME_EMP_REP")
    private String nameEmpRep;

	@Field(value="DATE_REP")
    private Date dateRep;

	@Field(value="PK_DEPT")
    private String pkDept;

	@Field(value="FLAG_CANC")
    private String flagCanc;

	@Field(value="DATE_CANC")
    private Date dateCanc;

	@Field(value="PK_EMP_CANC")
    private String pkEmpCanc;

	@Field(value="NAME_EMP_CANC")
    private String nameEmpCanc;

	@Field(value="MODIFIER")
    private String modifier;

	@Field(value="MODITY_TIME")
    private Date modityTime;


    public String getPkIprep(){
        return this.pkIprep;
    }
    public void setPkIprep(String pkIprep){
        this.pkIprep = pkIprep;
    }

    public String getPkPv(){
        return this.pkPv;
    }
    public void setPkPv(String pkPv){
        this.pkPv = pkPv;
    }

    public String getPkDiag(){
        return this.pkDiag;
    }
    public void setPkDiag(String pkDiag){
        this.pkDiag = pkDiag;
    }

    public String getFlagHp(){
        return this.flagHp;
    }
    public void setFlagHp(String flagHp){
        this.flagHp = flagHp;
    }

    public String getFlagPat(){
        return this.flagPat;
    }
    public void setFlagPat(String flagPat){
        this.flagPat = flagPat;
    }

    public String getFlagDise(){
        return this.flagDise;
    }
    public void setFlagDise(String flagDise){
        this.flagDise = flagDise;
    }

    public String getFlagCompDise(){
        return this.flagCompDise;
    }
    public void setFlagCompDise(String flagCompDise){
        this.flagCompDise = flagCompDise;
    }

    public String getFlagRej(){
        return this.flagRej;
    }
    public void setFlagRej(String flagRej){
        this.flagRej = flagRej;
    }

    public String getFlagCompMed(){
        return this.flagCompMed;
    }
    public void setFlagCompMed(String flagCompMed){
        this.flagCompMed = flagCompMed;
    }

    public String getFlagInf(){
        return this.flagInf;
    }
    public void setFlagInf(String flagInf){
        this.flagInf = flagInf;
    }

    public String getFlagDelay(){
        return this.flagDelay;
    }
    public void setFlagDelay(String flagDelay){
        this.flagDelay = flagDelay;
    }

    public String getFlagOver(){
        return this.flagOver;
    }
    public void setFlagOver(String flagOver){
        this.flagOver = flagOver;
    }

    public String getFlagOth(){
        return this.flagOth;
    }
    public void setFlagOth(String flagOth){
        this.flagOth = flagOth;
    }

    public String getDescOth(){
        return this.descOth;
    }
    public void setDescOth(String descOth){
        this.descOth = descOth;
    }

    public String getDescSoc(){
        return this.descSoc;
    }
    public void setDescSoc(String descSoc){
        this.descSoc = descSoc;
    }

    public String getDescAdv(){
        return this.descAdv;
    }
    public void setDescAdv(String descAdv){
        this.descAdv = descAdv;
    }

    public String getPkEmpRep(){
        return this.pkEmpRep;
    }
    public void setPkEmpRep(String pkEmpRep){
        this.pkEmpRep = pkEmpRep;
    }

    public String getNameEmpRep(){
        return this.nameEmpRep;
    }
    public void setNameEmpRep(String nameEmpRep){
        this.nameEmpRep = nameEmpRep;
    }

    public Date getDateRep(){
        return this.dateRep;
    }
    public void setDateRep(Date dateRep){
        this.dateRep = dateRep;
    }

    public String getPkDept(){
        return this.pkDept;
    }
    public void setPkDept(String pkDept){
        this.pkDept = pkDept;
    }

    public String getFlagCanc(){
        return this.flagCanc;
    }
    public void setFlagCanc(String flagCanc){
        this.flagCanc = flagCanc;
    }

    public Date getDateCanc(){
        return this.dateCanc;
    }
    public void setDateCanc(Date dateCanc){
        this.dateCanc = dateCanc;
    }

    public String getPkEmpCanc(){
        return this.pkEmpCanc;
    }
    public void setPkEmpCanc(String pkEmpCanc){
        this.pkEmpCanc = pkEmpCanc;
    }

    public String getNameEmpCanc(){
        return this.nameEmpCanc;
    }
    public void setNameEmpCanc(String nameEmpCanc){
        this.nameEmpCanc = nameEmpCanc;
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