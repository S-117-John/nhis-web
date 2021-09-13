package com.zebone.nhis.common.module.ex.nis.ns;

import java.util.Date;
import java.math.BigDecimal;

import com.zebone.nhis.common.module.BaseModule;
import com.zebone.platform.modules.dao.build.au.Field;
import com.zebone.platform.modules.dao.build.au.PK;
import com.zebone.platform.modules.dao.build.au.Table;
import com.zebone.platform.modules.dao.build.au.Field.KeyId;


/**
 * Table: PV_OPER_TRANS 
 *
 * @since 2017-06-13 09:10:49
 */
@Table(value="PV_OPER_TRANS")
public class PvOperTrans extends BaseModule  {

	@PK
	@Field(value="PK_OPERTRANS",id=KeyId.UUID)
    private String pkOpertrans;

	@Field(value="PK_PV")
    private String pkPv;

	@Field(value="PK_ORDOP")
    private String pkOrdop;

	@Field(value="TEMPERATURE")
    private BigDecimal temperature;

	@Field(value="PULSE")
    private Integer pulse;

	@Field(value="BREATH")
    private Integer breath;

	@Field(value="BPMAX")
    private Integer bpmax;

	@Field(value="BPMIN")
    private Integer bpmin;

	@Field(value="AWARE")
    private String aware;

	@Field(value="FLAG_DAMAGE")
    private String flagDamage;

	@Field(value="PART_DAMAGE")
    private String partDamage;

	@Field(value="COOPERATION")
    private String cooperation;

	@Field(value="TRANSFUSION")
    private String transfusion;

	@Field(value="PART_TRANSFUSION")
    private String partTransfusion;

	@Field(value="FLAG_BEFOREDRUG")
    private String flagBeforedrug;

	@Field(value="FLAG_BEFOREBLOOD")
    private String flagBeforeblood;

	@Field(value="FLAG_BEFORETUBE")
    private String flagBeforetube;

	@Field(value="FLAG_BEFORECATHETER")
    private String flagBeforecatheter;

	@Field(value="PK_NURSE_BEFORE")
    private String pkNurseBefore;

	@Field(value="NAME_NURSE_BEFORE")
    private String nameNurseBefore;

	@Field(value="DATE_SIGN_BEFORE")
    private Date dateSignBefore;

	@Field(value="FLAG_IDEN")
    private String flagIden;

	@Field(value="NAME_OPER")
    private String nameOper;

	@Field(value="FLAG_OPERPART")
    private String flagOperpart;

	@Field(value="CONFIRM_SIGN")
    private String confirmSign;

	@Field(value="RIS_RESULT")
    private String risResult;

	@Field(value="FLAG_IMPLANT")
    private String flagImplant;

	@Field(value="NAME_IMPLANT")
    private String nameImplant;

	@Field(value="PART_IMPLANT")
    private String partImplant;

	@Field(value="FLAG_MS")
    private String flagMs;

	@Field(value="PRE_OP")
    private String preOp;

	@Field(value="REASON_NF")
    private String reasonNf;

	@Field(value="GOODS_IN")
    private String goodsIn;

	@Field(value="GOODS_IN_OTHERS")
    private String goodsInOthers;

	@Field(value="PK_NURSE_JB")
    private String pkNurseJb;

	@Field(value="NAME_NURSE_JB")
    private String nameNurseJb;

	@Field(value="PK_NURSE_JIB")
    private String pkNurseJib;

	@Field(value="NAME_NURSE_JIB")
    private String nameNurseJib;

	@Field(value="DATE_JB")
    private Date dateJb;

	@Field(value="PK_NURSE_JJ")
    private String pkNurseJj;

	@Field(value="NAME_NURSE_JJ")
    private String nameNurseJj;

	@Field(value="PK_NURSE_JJB")
    private String pkNurseJjb;

	@Field(value="NAME_NURSE_JJB")
    private String nameNurseJjb;

	@Field(value="DATE_JJ")
    private Date dateJj;

	@Field(value="DT_ANES")
    private String dtAnes;

	@Field(value="NAME_OP_AFTER")
    private String nameOpAfter;

	@Field(value="AWARE_AFTER")
    private String awareAfter;

	@Field(value="PULSE_AFTER")
    private Integer pulseAfter;

	@Field(value="BP_AFTER_MAX")
    private Integer bpAfterMax;
	
	@Field(value="BP_AFTER_MIN")
    private Integer bpAfterMin;

	@Field(value="BREATH_AFTER")
    private Integer breathAfter;

	@Field(value="FLAG_DAMAGE_AFTER")
    private String flagDamageAfter;

	@Field(value="PART_DAMAGE_AFTER")
    private String partDamageAfter;

	@Field(value="FLAG_TUBE")
    private String flagTube;

	@Field(value="FLAG_TUBEFLAG")
    private String flagTubeflag;

	@Field(value="LIQUID")
    private String liquid;

	@Field(value="PK_NURSE_BED")
    private String pkNurseBed;

	@Field(value="NAME_NURSE_BED")
    private String nameNurseBed;

	@Field(value="PK_NURSE_BEDJB")
    private String pkNurseBedjb;

	@Field(value="NAME_NURSE_BEDJB")
    private String nameNurseBedjb;

	@Field(value="DATE_BED_JB")
    private Date dateBedJb;

	@Field(value="EU_STATUS")
    private String euStatus;

	@Field(value="MODIFY_TIME")
    private Date modifyTime;


    public String getPkOpertrans(){
        return this.pkOpertrans;
    }
    public void setPkOpertrans(String pkOpertrans){
        this.pkOpertrans = pkOpertrans;
    }

    public String getPkPv(){
        return this.pkPv;
    }
    public void setPkPv(String pkPv){
        this.pkPv = pkPv;
    }

    public String getPkOrdop(){
        return this.pkOrdop;
    }
    public void setPkOrdop(String pkOrdop){
        this.pkOrdop = pkOrdop;
    }

    public BigDecimal getTemperature(){
        return this.temperature;
    }
    public void setTemperature(BigDecimal temperature){
        this.temperature = temperature;
    }

    public Integer getPulse(){
        return this.pulse;
    }
    public void setPulse(Integer pulse){
        this.pulse = pulse;
    }

    public Integer getBreath(){
        return this.breath;
    }
    public void setBreath(Integer breath){
        this.breath = breath;
    }

    public Integer getBpmax(){
        return this.bpmax;
    }
    public void setBpmax(Integer bpmax){
        this.bpmax = bpmax;
    }

    public Integer getBpmin(){
        return this.bpmin;
    }
    public void setBpmin(Integer bpmin){
        this.bpmin = bpmin;
    }

    public String getAware(){
        return this.aware;
    }
    public void setAware(String aware){
        this.aware = aware;
    }

    public String getPartDamage(){
        return this.partDamage;
    }
    public void setPartDamage(String partDamage){
        this.partDamage = partDamage;
    }

    public String getCooperation(){
        return this.cooperation;
    }
    public void setCooperation(String cooperation){
        this.cooperation = cooperation;
    }

    public String getTransfusion(){
        return this.transfusion;
    }
    public void setTransfusion(String transfusion){
        this.transfusion = transfusion;
    }

    public String getPartTransfusion(){
        return this.partTransfusion;
    }
    public void setPartTransfusion(String partTransfusion){
        this.partTransfusion = partTransfusion;
    }


    public String getPkNurseBefore(){
        return this.pkNurseBefore;
    }
    public void setPkNurseBefore(String pkNurseBefore){
        this.pkNurseBefore = pkNurseBefore;
    }

    public String getNameNurseBefore(){
        return this.nameNurseBefore;
    }
    public void setNameNurseBefore(String nameNurseBefore){
        this.nameNurseBefore = nameNurseBefore;
    }

    public Date getDateSignBefore(){
        return this.dateSignBefore;
    }
    public void setDateSignBefore(Date dateSignBefore){
        this.dateSignBefore = dateSignBefore;
    }


    public String getNameOper(){
        return this.nameOper;
    }
    public void setNameOper(String nameOper){
        this.nameOper = nameOper;
    }


    public String getConfirmSign(){
        return this.confirmSign;
    }
    public void setConfirmSign(String confirmSign){
        this.confirmSign = confirmSign;
    }

    public String getRisResult(){
        return this.risResult;
    }
    public void setRisResult(String risResult){
        this.risResult = risResult;
    }


    public String getNameImplant(){
        return this.nameImplant;
    }
    public void setNameImplant(String nameImplant){
        this.nameImplant = nameImplant;
    }

    public String getPartImplant(){
        return this.partImplant;
    }
    public void setPartImplant(String partImplant){
        this.partImplant = partImplant;
    }


    public String getPreOp(){
        return this.preOp;
    }
    public void setPreOp(String preOp){
        this.preOp = preOp;
    }

    public String getReasonNf(){
        return this.reasonNf;
    }
    public void setReasonNf(String reasonNf){
        this.reasonNf = reasonNf;
    }

    public String getFlagDamage() {
		return flagDamage;
	}
	public void setFlagDamage(String flagDamage) {
		this.flagDamage = flagDamage;
	}
	public String getFlagBeforedrug() {
		return flagBeforedrug;
	}
	public void setFlagBeforedrug(String flagBeforedrug) {
		this.flagBeforedrug = flagBeforedrug;
	}
	public String getFlagBeforeblood() {
		return flagBeforeblood;
	}
	public void setFlagBeforeblood(String flagBeforeblood) {
		this.flagBeforeblood = flagBeforeblood;
	}
	public String getFlagBeforetube() {
		return flagBeforetube;
	}
	public void setFlagBeforetube(String flagBeforetube) {
		this.flagBeforetube = flagBeforetube;
	}
	public String getFlagBeforecatheter() {
		return flagBeforecatheter;
	}
	public void setFlagBeforecatheter(String flagBeforecatheter) {
		this.flagBeforecatheter = flagBeforecatheter;
	}
	public String getFlagIden() {
		return flagIden;
	}
	public void setFlagIden(String flagIden) {
		this.flagIden = flagIden;
	}
	public String getFlagOperpart() {
		return flagOperpart;
	}
	public void setFlagOperpart(String flagOperpart) {
		this.flagOperpart = flagOperpart;
	}
	public String getFlagImplant() {
		return flagImplant;
	}
	public void setFlagImplant(String flagImplant) {
		this.flagImplant = flagImplant;
	}
	public String getFlagMs() {
		return flagMs;
	}
	public void setFlagMs(String flagMs) {
		this.flagMs = flagMs;
	}
	public String getFlagDamageAfter() {
		return flagDamageAfter;
	}
	public void setFlagDamageAfter(String flagDamageAfter) {
		this.flagDamageAfter = flagDamageAfter;
	}
	public String getFlagTube() {
		return flagTube;
	}
	public void setFlagTube(String flagTube) {
		this.flagTube = flagTube;
	}
	public String getFlagTubeflag() {
		return flagTubeflag;
	}
	public void setFlagTubeflag(String flagTubeflag) {
		this.flagTubeflag = flagTubeflag;
	}
	public String getGoodsIn(){
        return this.goodsIn;
    }
    public void setGoodsIn(String goodsIn){
        this.goodsIn = goodsIn;
    }

    public String getGoodsInOthers(){
        return this.goodsInOthers;
    }
    public void setGoodsInOthers(String goodsInOthers){
        this.goodsInOthers = goodsInOthers;
    }

    public String getPkNurseJb(){
        return this.pkNurseJb;
    }
    public void setPkNurseJb(String pkNurseJb){
        this.pkNurseJb = pkNurseJb;
    }

    public String getNameNurseJb(){
        return this.nameNurseJb;
    }
    public void setNameNurseJb(String nameNurseJb){
        this.nameNurseJb = nameNurseJb;
    }

    public String getPkNurseJib(){
        return this.pkNurseJib;
    }
    public void setPkNurseJib(String pkNurseJib){
        this.pkNurseJib = pkNurseJib;
    }

    public String getNameNurseJib(){
        return this.nameNurseJib;
    }
    public void setNameNurseJib(String nameNurseJib){
        this.nameNurseJib = nameNurseJib;
    }

    public Date getDateJb(){
        return this.dateJb;
    }
    public void setDateJb(Date dateJb){
        this.dateJb = dateJb;
    }

    public String getPkNurseJj(){
        return this.pkNurseJj;
    }
    public void setPkNurseJj(String pkNurseJj){
        this.pkNurseJj = pkNurseJj;
    }

    public String getNameNurseJj(){
        return this.nameNurseJj;
    }
    public void setNameNurseJj(String nameNurseJj){
        this.nameNurseJj = nameNurseJj;
    }

    public String getPkNurseJjb(){
        return this.pkNurseJjb;
    }
    public void setPkNurseJjb(String pkNurseJjb){
        this.pkNurseJjb = pkNurseJjb;
    }

    public String getNameNurseJjb(){
        return this.nameNurseJjb;
    }
    public void setNameNurseJjb(String nameNurseJjb){
        this.nameNurseJjb = nameNurseJjb;
    }

    public Date getDateJj(){
        return this.dateJj;
    }
    public void setDateJj(Date dateJj){
        this.dateJj = dateJj;
    }

    public String getDtAnes(){
        return this.dtAnes;
    }
    public void setDtAnes(String dtAnes){
        this.dtAnes = dtAnes;
    }

    public String getNameOpAfter(){
        return this.nameOpAfter;
    }
    public void setNameOpAfter(String nameOpAfter){
        this.nameOpAfter = nameOpAfter;
    }

    public String getAwareAfter(){
        return this.awareAfter;
    }
    public void setAwareAfter(String awareAfter){
        this.awareAfter = awareAfter;
    }

    public Integer getPulseAfter(){
        return this.pulseAfter;
    }
    public void setPulseAfter(Integer pulseAfter){
        this.pulseAfter = pulseAfter;
    }

    public Integer getBpAfterMax() {
		return bpAfterMax;
	}
	public void setBpAfterMax(Integer bpAfterMax) {
		this.bpAfterMax = bpAfterMax;
	}
	public Integer getBpAfterMin() {
		return bpAfterMin;
	}
	public void setBpAfterMin(Integer bpAfterMin) {
		this.bpAfterMin = bpAfterMin;
	}
	public Integer getBreathAfter(){
        return this.breathAfter;
    }
    public void setBreathAfter(Integer breathAfter){
        this.breathAfter = breathAfter;
    }

    public String getPartDamageAfter(){
        return this.partDamageAfter;
    }
    public void setPartDamageAfter(String partDamageAfter){
        this.partDamageAfter = partDamageAfter;
    }

    public String getLiquid(){
        return this.liquid;
    }
    public void setLiquid(String liquid){
        this.liquid = liquid;
    }

    public String getPkNurseBed(){
        return this.pkNurseBed;
    }
    public void setPkNurseBed(String pkNurseBed){
        this.pkNurseBed = pkNurseBed;
    }

    public String getNameNurseBed(){
        return this.nameNurseBed;
    }
    public void setNameNurseBed(String nameNurseBed){
        this.nameNurseBed = nameNurseBed;
    }

    public String getPkNurseBedjb(){
        return this.pkNurseBedjb;
    }
    public void setPkNurseBedjb(String pkNurseBedjb){
        this.pkNurseBedjb = pkNurseBedjb;
    }

    public String getNameNurseBedjb(){
        return this.nameNurseBedjb;
    }
    public void setNameNurseBedjb(String nameNurseBedjb){
        this.nameNurseBedjb = nameNurseBedjb;
    }

    public Date getDateBedJb(){
        return this.dateBedJb;
    }
    public void setDateBedJb(Date dateBedJb){
        this.dateBedJb = dateBedJb;
    }

    public String getEuStatus(){
        return this.euStatus;
    }
    public void setEuStatus(String euStatus){
        this.euStatus = euStatus;
    }

    public Date getModifyTime(){
        return this.modifyTime;
    }
    public void setModifyTime(Date modifyTime){
        this.modifyTime = modifyTime;
    }
}