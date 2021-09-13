package com.zebone.nhis.common.module.ex.nis.ns;

import java.util.Date;

import com.zebone.nhis.common.module.BaseModule;
import com.zebone.platform.modules.dao.build.au.Field;
import com.zebone.platform.modules.dao.build.au.PK;
import com.zebone.platform.modules.dao.build.au.Table;
import com.zebone.platform.modules.dao.build.au.Field.KeyId;


/**
 * Table: EX_PD_PIVAS 
 *
 * @since 2016-12-16 03:22:43
 */
@Table(value="EX_PD_PIVAS")
public class ExPdPivas extends BaseModule  {

	private static final long serialVersionUID = 1L;

	@PK
	@Field(value="PK_PDPIVAS",id=KeyId.UUID)
    private String pkPdpivas;

	@Field(value="PK_PV")
    private String pkPv;

	@Field(value="PK_PI")
    private String pkPi;

	@Field(value="PK_PDAPDT")
    private String pkPdapdt;

	@Field(value="PK_EXOCC")
    private String pkExocc;

	@Field(value="PK_CNORD")
    private String pkCnord;

	@Field(value="EU_ALWAYS")
    private String euAlways;

	@Field(value="NAME_ORD")
    private String nameOrd;

	@Field(value="SPEC")
    private String spec;

	@Field(value="ORDSN")
    private Integer ordsn;

	@Field(value="ORDSN_PARENT")
    private Integer ordsnParent;

	@Field(value="CODE_SUPPLY")
    private String codeSupply;

	@Field(value="CODE_FREQ")
    private String codeFreq;

	@Field(value="DOSAGE")
    private Double dosage;

	@Field(value="PK_UNIT_DOS")
    private String pkUnitDos;

	@Field(value="DRIP_SPEED")
    private Integer dripSpeed;

	@Field(value="PK_PD")
    private String pkPd;

	@Field(value="PK_UNIT")
    private String pkUnit;

	@Field(value="PACK_SIZE")
    private Double packSize;

	@Field(value="QUAN_PACK")
    private Double quanPack;

	@Field(value="QUAN_MIN")
    private Double quanMin;

	@Field(value="PK_PIVASCATE")
    private String pkPivascate;

	@Field(value="PK_DEPT_APPLY")
    private String pkDeptApply;

	@Field(value="PK_DEPT_ADMIX")
    private String pkDeptAdmix;

	@Field(value="CODE_BATCH")
    private String codeBatch;

	@Field(value="BARCODE")
    private String barcode;

	@Field(value="TIME_PLAN")
    private Date timePlan;

	@Field(value="TIME_OCC")
    private Date timeOcc;

	@Field(value="EU_STATUS")
    private String euStatus;

	@Field(value="FLAG_PRT")
    private String flagPrt;

	@Field(value="DATE_PRT")
    private Date datePrt;

	@Field(value="PK_EMP_PRT")
    private String pkEmpPrt;

	@Field(value="NAME_EMP_PRT")
    private String nameEmpPrt;

	@Field(value="FLAG_STOP")
    private String flagStop;

	@Field(value="FLAG_PACK")
    private String flagPack;

	@Field(value="DATE_PACK")
    private Date datePack;

	@Field(value="PK_EMP_PACK")
    private String pkEmpPack;

	@Field(value="NAME_EMP_PACK")
    private String nameEmpPack;

	@Field(value="FLAG_IN")
    private String flagIn;

	@Field(value="DATE_IN")
    private Date dateIn;

	@Field(value="PK_EMP_IN")
    private String pkEmpIn;

	@Field(value="NAME_EMP_IN")
    private String nameEmpIn;

	@Field(value="FLAG_ADMIX")
    private String flagAdmix;

	@Field(value="DATE_ADMIX")
    private Date dateAdmix;

	@Field(value="PK_EMP_ADMIX")
    private String pkEmpAdmix;

	@Field(value="NAME_EMP_ADMIX")
    private String nameEmpAdmix;

	@Field(value="FLAG_OUT")
    private String flagOut;

	@Field(value="DATE_OUT")
    private Date dateOut;

	@Field(value="PK_EMP_OUT")
    private String pkEmpOut;

	@Field(value="NAME_EMP_OUT")
    private String nameEmpOut;

	@Field(value="FLAG_CHK")
    private String flagChk;

	@Field(value="DATE_CHK")
    private Date dateChk;

	@Field(value="PK_EMP_CHK")
    private String pkEmpChk;

	@Field(value="NAME_EMP_CHK")
    private String nameEmpChk;

	@Field(value="FLAG_SIGN")
    private String flagSign;

	@Field(value="DATE_SIGN")
    private Date dateSign;

	@Field(value="PK_EMP_SIGN")
    private String pkEmpSign;

	@Field(value="NAME_EMP_SIGN")
    private String nameEmpSign;

	@Field(value="PK_PDDE")
    private String pkPdde;

	@Field(value="MODIFIER")
    private String modifier;

	@Field(value="MODITY_TIME")
    private Date modityTime;


    public String getPkPdpivas(){
        return this.pkPdpivas;
    }
    public void setPkPdpivas(String pkPdpivas){
        this.pkPdpivas = pkPdpivas;
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

    public String getPkPdapdt(){
        return this.pkPdapdt;
    }
    public void setPkPdapdt(String pkPdapdt){
        this.pkPdapdt = pkPdapdt;
    }

    public String getPkExocc(){
        return this.pkExocc;
    }
    public void setPkExocc(String pkExocc){
        this.pkExocc = pkExocc;
    }

    public String getPkCnord(){
        return this.pkCnord;
    }
    public void setPkCnord(String pkCnord){
        this.pkCnord = pkCnord;
    }

    public String getEuAlways(){
        return this.euAlways;
    }
    public void setEuAlways(String euAlways){
        this.euAlways = euAlways;
    }

    public String getNameOrd(){
        return this.nameOrd;
    }
    public void setNameOrd(String nameOrd){
        this.nameOrd = nameOrd;
    }

    public String getSpec(){
        return this.spec;
    }
    public void setSpec(String spec){
        this.spec = spec;
    }

    public Integer getOrdsn(){
        return this.ordsn;
    }
    public void setOrdsn(Integer ordsn){
        this.ordsn = ordsn;
    }

    public Integer getOrdsnParent(){
        return this.ordsnParent;
    }
    public void setOrdsnParent(Integer ordsnParent){
        this.ordsnParent = ordsnParent;
    }

    public String getCodeSupply(){
        return this.codeSupply;
    }
    public void setCodeSupply(String codeSupply){
        this.codeSupply = codeSupply;
    }

    public String getCodeFreq(){
        return this.codeFreq;
    }
    public void setCodeFreq(String codeFreq){
        this.codeFreq = codeFreq;
    }

    public Double getDosage(){
        return this.dosage;
    }
    public void setDosage(Double dosage){
        this.dosage = dosage;
    }

    public String getPkUnitDos(){
        return this.pkUnitDos;
    }
    public void setPkUnitDos(String pkUnitDos){
        this.pkUnitDos = pkUnitDos;
    }

    public Integer getDripSpeed(){
        return this.dripSpeed;
    }
    public void setDripSpeed(Integer dripSpeed){
        this.dripSpeed = dripSpeed;
    }

    public String getPkPd(){
        return this.pkPd;
    }
    public void setPkPd(String pkPd){
        this.pkPd = pkPd;
    }

    public String getPkUnit(){
        return this.pkUnit;
    }
    public void setPkUnit(String pkUnit){
        this.pkUnit = pkUnit;
    }

    public Double getPackSize(){
        return this.packSize;
    }
    public void setPackSize(Double packSize){
        this.packSize = packSize;
    }

    public Double getQuanPack(){
        return this.quanPack;
    }
    public void setQuanPack(Double quanPack){
        this.quanPack = quanPack;
    }

    public Double getQuanMin(){
        return this.quanMin;
    }
    public void setQuanMin(Double quanMin){
        this.quanMin = quanMin;
    }

    public String getPkPivascate(){
        return this.pkPivascate;
    }
    public void setPkPivascate(String pkPivascate){
        this.pkPivascate = pkPivascate;
    }

    public String getPkDeptApply(){
        return this.pkDeptApply;
    }
    public void setPkDeptApply(String pkDeptApply){
        this.pkDeptApply = pkDeptApply;
    }

    public String getPkDeptAdmix(){
        return this.pkDeptAdmix;
    }
    public void setPkDeptAdmix(String pkDeptAdmix){
        this.pkDeptAdmix = pkDeptAdmix;
    }

    public String getCodeBatch(){
        return this.codeBatch;
    }
    public void setCodeBatch(String codeBatch){
        this.codeBatch = codeBatch;
    }

    public String getBarcode(){
        return this.barcode;
    }
    public void setBarcode(String barcode){
        this.barcode = barcode;
    }

    public Date getTimePlan(){
        return this.timePlan;
    }
    public void setTimePlan(Date timePlan){
        this.timePlan = timePlan;
    }

    public Date getTimeOcc(){
        return this.timeOcc;
    }
    public void setTimeOcc(Date timeOcc){
        this.timeOcc = timeOcc;
    }

    public String getEuStatus(){
        return this.euStatus;
    }
    public void setEuStatus(String euStatus){
        this.euStatus = euStatus;
    }

    public String getFlagPrt(){
        return this.flagPrt;
    }
    public void setFlagPrt(String flagPrt){
        this.flagPrt = flagPrt;
    }

    public Date getDatePrt(){
        return this.datePrt;
    }
    public void setDatePrt(Date datePrt){
        this.datePrt = datePrt;
    }

    public String getPkEmpPrt(){
        return this.pkEmpPrt;
    }
    public void setPkEmpPrt(String pkEmpPrt){
        this.pkEmpPrt = pkEmpPrt;
    }

    public String getNameEmpPrt(){
        return this.nameEmpPrt;
    }
    public void setNameEmpPrt(String nameEmpPrt){
        this.nameEmpPrt = nameEmpPrt;
    }

    public String getFlagStop(){
        return this.flagStop;
    }
    public void setFlagStop(String flagStop){
        this.flagStop = flagStop;
    }

    public String getFlagPack(){
        return this.flagPack;
    }
    public void setFlagPack(String flagPack){
        this.flagPack = flagPack;
    }

    public Date getDatePack(){
        return this.datePack;
    }
    public void setDatePack(Date datePack){
        this.datePack = datePack;
    }

    public String getPkEmpPack(){
        return this.pkEmpPack;
    }
    public void setPkEmpPack(String pkEmpPack){
        this.pkEmpPack = pkEmpPack;
    }

    public String getNameEmpPack(){
        return this.nameEmpPack;
    }
    public void setNameEmpPack(String nameEmpPack){
        this.nameEmpPack = nameEmpPack;
    }

    public String getFlagIn(){
        return this.flagIn;
    }
    public void setFlagIn(String flagIn){
        this.flagIn = flagIn;
    }

    public Date getDateIn(){
        return this.dateIn;
    }
    public void setDateIn(Date dateIn){
        this.dateIn = dateIn;
    }

    public String getPkEmpIn(){
        return this.pkEmpIn;
    }
    public void setPkEmpIn(String pkEmpIn){
        this.pkEmpIn = pkEmpIn;
    }

    public String getNameEmpIn(){
        return this.nameEmpIn;
    }
    public void setNameEmpIn(String nameEmpIn){
        this.nameEmpIn = nameEmpIn;
    }

    public String getFlagAdmix(){
        return this.flagAdmix;
    }
    public void setFlagAdmix(String flagAdmix){
        this.flagAdmix = flagAdmix;
    }

    public Date getDateAdmix(){
        return this.dateAdmix;
    }
    public void setDateAdmix(Date dateAdmix){
        this.dateAdmix = dateAdmix;
    }

    public String getPkEmpAdmix(){
        return this.pkEmpAdmix;
    }
    public void setPkEmpAdmix(String pkEmpAdmix){
        this.pkEmpAdmix = pkEmpAdmix;
    }

    public String getNameEmpAdmix(){
        return this.nameEmpAdmix;
    }
    public void setNameEmpAdmix(String nameEmpAdmix){
        this.nameEmpAdmix = nameEmpAdmix;
    }

    public String getFlagOut(){
        return this.flagOut;
    }
    public void setFlagOut(String flagOut){
        this.flagOut = flagOut;
    }

    public Date getDateOut(){
        return this.dateOut;
    }
    public void setDateOut(Date dateOut){
        this.dateOut = dateOut;
    }

    public String getPkEmpOut(){
        return this.pkEmpOut;
    }
    public void setPkEmpOut(String pkEmpOut){
        this.pkEmpOut = pkEmpOut;
    }

    public String getNameEmpOut(){
        return this.nameEmpOut;
    }
    public void setNameEmpOut(String nameEmpOut){
        this.nameEmpOut = nameEmpOut;
    }

    public String getFlagChk(){
        return this.flagChk;
    }
    public void setFlagChk(String flagChk){
        this.flagChk = flagChk;
    }

    public Date getDateChk(){
        return this.dateChk;
    }
    public void setDateChk(Date dateChk){
        this.dateChk = dateChk;
    }

    public String getPkEmpChk(){
        return this.pkEmpChk;
    }
    public void setPkEmpChk(String pkEmpChk){
        this.pkEmpChk = pkEmpChk;
    }

    public String getNameEmpChk(){
        return this.nameEmpChk;
    }
    public void setNameEmpChk(String nameEmpChk){
        this.nameEmpChk = nameEmpChk;
    }

    public String getFlagSign(){
        return this.flagSign;
    }
    public void setFlagSign(String flagSign){
        this.flagSign = flagSign;
    }

    public Date getDateSign(){
        return this.dateSign;
    }
    public void setDateSign(Date dateSign){
        this.dateSign = dateSign;
    }

    public String getPkEmpSign(){
        return this.pkEmpSign;
    }
    public void setPkEmpSign(String pkEmpSign){
        this.pkEmpSign = pkEmpSign;
    }

    public String getNameEmpSign(){
        return this.nameEmpSign;
    }
    public void setNameEmpSign(String nameEmpSign){
        this.nameEmpSign = nameEmpSign;
    }

    public String getPkPdde(){
        return this.pkPdde;
    }
    public void setPkPdde(String pkPdde){
        this.pkPdde = pkPdde;
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