package com.zebone.nhis.common.module.compay.ins.xa.xayb;

import java.util.Date;

import com.zebone.nhis.common.module.BaseModule;
import com.zebone.platform.modules.dao.build.au.Field;
import com.zebone.platform.modules.dao.build.au.PK;
import com.zebone.platform.modules.dao.build.au.Table;
import com.zebone.platform.modules.dao.build.au.Field.FieldType;
import com.zebone.platform.modules.dao.build.au.Field.KeyId;


/**
 * Table: INS_REC_XAYBSUM 
 * 西安市医保清算汇总表（西安市医保、工商医保、职工生育医保）
 * @since 2017-12-14 09:40:15
 */
@Table(value="INS_REC_XAYBSUM")
public class InsRecXaybsum extends BaseModule {

    /**
	 * 序列号
	 */
	private static final long serialVersionUID = 5956996707795750934L;

	/** ID - 主键 */
	@PK
	@Field(value="ID",id=KeyId.UUID)
    private String id;

    /** QSQH - 清算期号 */
	@Field(value="QSQH")
    private String qsqh;

    /** ZXSHBXBF - 执行社会保险办法 */
	@Field(value="ZXSHBXBF")
    private String zxshbxbf;

    /** QSLB - 清算类别 */
	@Field(value="QSLB")
    private String qslb;

    /** QSFZX - 清算分中心 */
	@Field(value="QSFZX")
    private String qsfzx;

    /** FYZE - 费用总额 */
	@Field(value="FYZE")
    private Double fyze;

    /** JBYLTCZFJE - 基本医疗统筹支付金额 */
	@Field(value="JBYLTCZFJE")
    private Double jbyltczfje;

    /** DEYLZFJE - 大额医疗支付金额 */
	@Field(value="DEYLZFJE")
    private Double deylzfje;

    /** GWYTCZFJE - 公务员统筹支付金额 */
	@Field(value="GWYTCZFJE")
    private Double gwytczfje;

    /** DESBZFJE - 大额商保支付金额 */
	@Field(value="DESBZFJE")
    private Double desbzfje;

    /** GRZHZFJE - 个人帐户支付金额 */
	@Field(value="GRZHZFJE")
    private Double grzhzfje;

    /** QSSQR - 清算申请人 */
	@Field(value="QSSQR")
    private String qssqr;

    /** QSSQRSJ - 清算申请人时间 */
	@Field(value="QSSQRSJ")
    private Date qssqrsj;

    /** CHECKBZ - check标志 */
	@Field(value="CHECKBZ")
    private String checkbz;

    /** QSLX - 清算类型 */
	@Field(value="QSLX")
    private String qslx;

	@Field(value="MODITY_TIME",date=FieldType.UPDATE)
    private Date modityTime;

    /** INSCODE - 医保编号 */
	@Field(value="INSCODE")
    private String inscode;

    /** HOSPITALCODE - 医院编号 */
	@Field(value="HOSPITALCODE")
    private String hospitalcode;

    /** QZFJE - 全自费金额(工伤) */
	@Field(value="QZFJE")
    private Double qzfje;

    /** TCBXJE - 统筹报销金额(工伤) */
	@Field(value="TCBXJE")
    private Double tcbxje;

    /** SYDYBFZE - 生育待遇拨付总额(生育用) */
	@Field(value="SYDYBFZE")
    private Double sydybfze;

    /** SYYLBTZE - 生育医疗补贴总额(生育用) */
	@Field(value="SYYLBTZE")
    private Double syylbtze;

    /** JHSYSSFBTZE - 计划生育手术费补贴总额(生育) */
	@Field(value="JHSYSSFBTZE")
    private Double jhsyssfbtze;

    /** SSBFZBZFY - 生育并发症补助费用(生育) */
	@Field(value="SSBFZBZFY")
    private Double ssbfzbzfy;


    public String getId(){
        return this.id;
    }
    public void setId(String id){
        this.id = id;
    }

    public String getQsqh(){
        return this.qsqh;
    }
    public void setQsqh(String qsqh){
        this.qsqh = qsqh;
    }

    public String getZxshbxbf(){
        return this.zxshbxbf;
    }
    public void setZxshbxbf(String zxshbxbf){
        this.zxshbxbf = zxshbxbf;
    }

    public String getQslb(){
        return this.qslb;
    }
    public void setQslb(String qslb){
        this.qslb = qslb;
    }

    public String getQsfzx(){
        return this.qsfzx;
    }
    public void setQsfzx(String qsfzx){
        this.qsfzx = qsfzx;
    }

    public Double getFyze(){
        return this.fyze;
    }
    public void setFyze(Double fyze){
        this.fyze = fyze;
    }

    public Double getJbyltczfje(){
        return this.jbyltczfje;
    }
    public void setJbyltczfje(Double jbyltczfje){
        this.jbyltczfje = jbyltczfje;
    }

    public Double getDeylzfje(){
        return this.deylzfje;
    }
    public void setDeylzfje(Double deylzfje){
        this.deylzfje = deylzfje;
    }

    public Double getGwytczfje(){
        return this.gwytczfje;
    }
    public void setGwytczfje(Double gwytczfje){
        this.gwytczfje = gwytczfje;
    }

    public Double getDesbzfje(){
        return this.desbzfje;
    }
    public void setDesbzfje(Double desbzfje){
        this.desbzfje = desbzfje;
    }

    public Double getGrzhzfje(){
        return this.grzhzfje;
    }
    public void setGrzhzfje(Double grzhzfje){
        this.grzhzfje = grzhzfje;
    }

    public String getQssqr(){
        return this.qssqr;
    }
    public void setQssqr(String qssqr){
        this.qssqr = qssqr;
    }

    public Date getQssqrsj(){
        return this.qssqrsj;
    }
    public void setQssqrsj(Date qssqrsj){
        this.qssqrsj = qssqrsj;
    }

    public String getCheckbz(){
        return this.checkbz;
    }
    public void setCheckbz(String checkbz){
        this.checkbz = checkbz;
    }

    public String getQslx(){
        return this.qslx;
    }
    public void setQslx(String qslx){
        this.qslx = qslx;
    }

    public Date getModityTime(){
        return this.modityTime;
    }
    public void setModityTime(Date modityTime){
        this.modityTime = modityTime;
    }

    public String getInscode(){
        return this.inscode;
    }
    public void setInscode(String inscode){
        this.inscode = inscode;
    }

    public String getHospitalcode(){
        return this.hospitalcode;
    }
    public void setHospitalcode(String hospitalcode){
        this.hospitalcode = hospitalcode;
    }

    public Double getQzfje(){
        return this.qzfje;
    }
    public void setQzfje(Double qzfje){
        this.qzfje = qzfje;
    }

    public Double getTcbxje(){
        return this.tcbxje;
    }
    public void setTcbxje(Double tcbxje){
        this.tcbxje = tcbxje;
    }

    public Double getSydybfze(){
        return this.sydybfze;
    }
    public void setSydybfze(Double sydybfze){
        this.sydybfze = sydybfze;
    }

    public Double getSyylbtze(){
        return this.syylbtze;
    }
    public void setSyylbtze(Double syylbtze){
        this.syylbtze = syylbtze;
    }

    public Double getJhsyssfbtze(){
        return this.jhsyssfbtze;
    }
    public void setJhsyssfbtze(Double jhsyssfbtze){
        this.jhsyssfbtze = jhsyssfbtze;
    }

    public Double getSsbfzbzfy(){
        return this.ssbfzbzfy;
    }
    public void setSsbfzbzfy(Double ssbfzbzfy){
        this.ssbfzbzfy = ssbfzbzfy;
    }
}