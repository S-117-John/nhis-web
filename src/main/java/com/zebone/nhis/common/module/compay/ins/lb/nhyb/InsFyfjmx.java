package com.zebone.nhis.common.module.compay.ins.lb.nhyb;

import java.util.Date;

import com.zebone.nhis.common.module.BaseModule;
import com.zebone.platform.modules.dao.build.au.Field;
import com.zebone.platform.modules.dao.build.au.PK;
import com.zebone.platform.modules.dao.build.au.Table;
import com.zebone.platform.modules.dao.build.au.Field.KeyId;


/**
 * Table: INS_FYFJMX 
 *
 * @since 2018-10-19 05:26:08
 */
@Table(value="INS_FYFJMX")
public class InsFyfjmx extends BaseModule  {

	@PK
	@Field(value="ID",id=KeyId.UUID)
    private String id;

    /** PK_PV - HIS就诊主键 */
	@Field(value="PK_PV")
    private String pkPv;

    /** PK_SETTLE - HIS结算主键 */
	@Field(value="PK_SETTLE")
    private String pkSettle;

    /** PK_INS_SETTLE - 医保结算主键 */
	@Field(value="PK_INS_SETTLE")
    private String pkInsSettle;

    /** PK_INS_REG - 医保登记主键 */
	@Field(value="PK_INS_REG")
    private String pkInsReg;

    /** PK_DETAIL - HIS明细主键 */
	@Field(value="PK_DETAIL")
    private String pkDetail;

    /** HIS_XMBM - HIS项目编码 */
	@Field(value="HIS_XMBM")
    private String hisXmbm;

    /** HIS_XMPK - HIS项目主键 */
	@Field(value="HIS_XMPK")
    private String hisXmpk;

    /** HIS_XMMC - HIS项目名称 */
	@Field(value="HIS_XMMC")
    private String hisXmmc;

    /** INS_XMBM - 医保的项目编码 */
	@Field(value="INS_XMBM")
    private String insXmbm;

    /** INS_XMMC - 医保项目名称 */
	@Field(value="INS_XMMC")
    private String insXmmc;

	@Field(value="HIS_ZJE")
    private Long hisZje;

	@Field(value="INS_ZJE")
    private Long insZje;

	@Field(value="INS_ZLJE")
    private Long insZlje;

	@Field(value="INS_ZFJE")
    private Long insZfje;

	@Field(value="INS_TCJE")
    private Long insTcje;

	@Field(value="INS_SFXMDJ")
    private Long insSfxmdj;

    /** MODIFY_TIME - 最后操作时间 */
	@Field(value="MODIFY_TIME")
    private Date modifyTime;


    public String getId(){
        return this.id;
    }
    public void setId(String id){
        this.id = id;
    }

    public String getPkPv(){
        return this.pkPv;
    }
    public void setPkPv(String pkPv){
        this.pkPv = pkPv;
    }

    public String getPkSettle(){
        return this.pkSettle;
    }
    public void setPkSettle(String pkSettle){
        this.pkSettle = pkSettle;
    }

    public String getPkInsSettle(){
        return this.pkInsSettle;
    }
    public void setPkInsSettle(String pkInsSettle){
        this.pkInsSettle = pkInsSettle;
    }

    public String getPkInsReg(){
        return this.pkInsReg;
    }
    public void setPkInsReg(String pkInsReg){
        this.pkInsReg = pkInsReg;
    }

    public String getPkDetail(){
        return this.pkDetail;
    }
    public void setPkDetail(String pkDetail){
        this.pkDetail = pkDetail;
    }

    public String getHisXmbm(){
        return this.hisXmbm;
    }
    public void setHisXmbm(String hisXmbm){
        this.hisXmbm = hisXmbm;
    }

    public String getHisXmpk(){
        return this.hisXmpk;
    }
    public void setHisXmpk(String hisXmpk){
        this.hisXmpk = hisXmpk;
    }

    public String getHisXmmc(){
        return this.hisXmmc;
    }
    public void setHisXmmc(String hisXmmc){
        this.hisXmmc = hisXmmc;
    }

    public String getInsXmbm(){
        return this.insXmbm;
    }
    public void setInsXmbm(String insXmbm){
        this.insXmbm = insXmbm;
    }

    public String getInsXmmc(){
        return this.insXmmc;
    }
    public void setInsXmmc(String insXmmc){
        this.insXmmc = insXmmc;
    }

    public Long getHisZje(){
        return this.hisZje;
    }
    public void setHisZje(Long hisZje){
        this.hisZje = hisZje;
    }

    public Long getInsZje(){
        return this.insZje;
    }
    public void setInsZje(Long insZje){
        this.insZje = insZje;
    }

    public Long getInsZlje(){
        return this.insZlje;
    }
    public void setInsZlje(Long insZlje){
        this.insZlje = insZlje;
    }

    public Long getInsZfje(){
        return this.insZfje;
    }
    public void setInsZfje(Long insZfje){
        this.insZfje = insZfje;
    }

    public Long getInsTcje(){
        return this.insTcje;
    }
    public void setInsTcje(Long insTcje){
        this.insTcje = insTcje;
    }

    public Long getInsSfxmdj(){
        return this.insSfxmdj;
    }
    public void setInsSfxmdj(Long insSfxmdj){
        this.insSfxmdj = insSfxmdj;
    }

    public Date getModifyTime(){
        return this.modifyTime;
    }
    public void setModifyTime(Date modifyTime){
        this.modifyTime = modifyTime;
    }
}