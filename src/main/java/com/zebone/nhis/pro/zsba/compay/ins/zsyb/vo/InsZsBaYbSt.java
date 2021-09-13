package com.zebone.nhis.pro.zsba.compay.ins.zsyb.vo;

import java.util.Date;
import java.math.BigDecimal;

import com.zebone.nhis.common.module.BaseModule;
import com.zebone.platform.modules.dao.build.au.Field;
import com.zebone.platform.modules.dao.build.au.PK;
import com.zebone.platform.modules.dao.build.au.Table;
import com.zebone.platform.modules.dao.build.au.Field.FieldType;
import com.zebone.platform.modules.dao.build.au.Field.KeyId;


/**
 * Table: ins_st - 外部医保-中山医保住院结算：（住院医保费用结算[2044]、住院取消结算[2035]、跨月退费信息查询 [4013]、跨 
 *
 * @since 2017-09-06 10:42:10
 */
@Table(value="INS_ST")
public class InsZsBaYbSt extends BaseModule  {

	@PK
	@Field(value="PK_INSST",id=KeyId.UUID)
    private String pkInsst;

	@Field(value="PK_INSPV")
    private String pkInspv;
	
    /** YYBH - 博爱为H003 */
	@Field(value="YYBH")
    private String yybh;

    /** PK_HP - 对应医保的就诊类别，21医疗住院、31外伤住院、 33工伤康复住院、41生育住院、 47计生手术住院、 2A	日间手术 */
	@Field(value="PK_HP")
    private String pkHp;

	@Field(value="PK_PI")
    private String pkPi;

	@Field(value="PK_PV")
    private String pkPv;

	@Field(value="PK_SETTLE")
    private String pkSettle;

    /** EU_PVTYPE - 1 门诊，2 急诊，3 住院，4 体检 */
	@Field(value="EU_PVTYPE")
    private String euPvtype;

	@Field(value="JZJLH")
    private String jzjlh;

	@Field(value="JSYWH")
    private String jsywh;

    /** XZLX - 56医疗 20工伤 30 生育 */
	@Field(value="XZLX")
    private String xzlx;

    /** JSBZ - 1正式结算2预结算 */
	@Field(value="JSBZ")
    private String jsbz;

	@Field(value="GRZFLJ")
    private BigDecimal grzflj;

	@Field(value="GWYTCXE")
    private BigDecimal gwytcxe;

    /** YBZHYE - NOT NULL */
	@Field(value="YBZHYE")
    private BigDecimal ybzhye;

    /** ND - 从医保卡上读取 */
	@Field(value="ND")
    private String nd;

    /** RYLB - 从医保卡上读取1 在职 2 退休  3 离休 */
	@Field(value="RYLB")
    private String rylb;

    /** KH - NOT NULL，读卡时必须保存！ */
	@Field(value="KH")
    private String kh;

    /** JYM - 从POS读取联系人，读卡时必须保存 */
	@Field(value="JYM")
    private String jym;

    /** PSAM - 读卡时必须保存 */
	@Field(value="PSAM")
    private String psam;

	@Field(value="YLFYZE")
    private BigDecimal ylfyze;

	@Field(value="GZZFUJE")
    private BigDecimal gzzfuje;

	@Field(value="GZZFEJE")
    private BigDecimal gzzfeje;

    /** TCZFJE - 当就诊类别为医疗时，返回的是基本医疗统筹基金支付金额;当就诊类别为工伤时，返回的是工伤基金支付金额，就诊类别为生育住院时返回的是生育基金支付 */
	@Field(value="TCZFJE")
    private BigDecimal tczfje;

	@Field(value="GWYTCZF")
    private BigDecimal gwytczf;

    /** XJZFUJE - 此处个人自付金额仍未减去民政低保统筹支付金额和优抚统筹支付金额 */
	@Field(value="XJZFUJE")
    private BigDecimal xjzfuje;

	@Field(value="XJZFEJE")
    private BigDecimal xjzfeje;

    /** DEJSBZ - 1计定额2不计定额 */
	@Field(value="DEJSBZ")
    private BigDecimal dejsbz;

    /** JSQZYTCED - 读卡时必须保存 */
	@Field(value="JSQZYTCED")
    private BigDecimal jsqzytced;

	@Field(value="JSRQ")
    private Date jsrq;

	@Field(value="BCYLTCZF")
    private BigDecimal bcyltczf;

    /** JBYLBZ - 1 可享受 0 不能享受 */
	@Field(value="JBYLBZ")
    private String jbylbz;

    /** BCXSBZ - 1 可享受 0 不能享受 */
	@Field(value="BCXSBZ")
    private String bcxsbz;

    /** GWYXSBZ - 1 可享受 0 不能享受 */
	@Field(value="GWYXSBZ")
    private String gwyxsbz;

	@Field(value="DBTCZF")
    private BigDecimal dbtczf;

    /** DBDXLB - 1.低保  2.低收入 */
	@Field(value="DBDXLB")
    private String dbdxlb;

	@Field(value="MZDBTCZF")
    private BigDecimal mzdbtczf;

	@Field(value="MZDBLJJE")
    private BigDecimal mzdbljje;

    /** YFDXLB - 1、三属人员；2、在乡复员军人；3、带病回乡退伍军人；4、一至四级残疾军人；5、五至六级残疾军人；6、七至十级残疾军人； 7、在乡“五老”人员；8、参战涉核退役人员；9、铀矿开采军队退役人员；10、（ */
	@Field(value="YFDXLB")
    private String yfdxlb;

	@Field(value="MZYFTCZF")
    private BigDecimal mzyftczf;

	@Field(value="MZYFLJJE")
    private BigDecimal mzyfljje;

    /** JZLB - 11	普通门诊； 12特殊病种门诊； 21普通住院；71住保特殊病种；72居民特殊病种； 13居民门诊；41	生育住院（基本医疗）；43产前检查 生育基金统筹 支付；44	计生手术门诊	生育基金统筹 */
	@Field(value="JZLB")
    private String jzlb;

	@Field(value="MODITY_TIME",date=FieldType.UPDATE)
    private Date modityTime;

    /** STATUS - 1入院登记成功，2入院登记失败；3资料维护成功，4资料维护失败； 5出院登记成功，6出院登记失败；7取消出院登记成功，  8取消出院登记失败；9取消入院登记成功，  10取消入院登记失败；11结算成功 */
	@Field(value="STATUS")
    private String status;

    /** TJLB - 报表类别=1 返回门诊费用月报表 =2返回医疗住院明细表＝4 返回离休报表 6 返回特定病种报表,，9 返回工伤门诊报表 ，10 返回工伤住院报表， 11 工伤康复门诊报表,12 返回工伤康复住院报表 */
	@Field(value="TJLB")
    private String tjlb;

	@Field(value="TJJZRQ")
    private String tjjzrq;

    /** JJYWH - 月结号，跨月退费时需要传该值 */
	@Field(value="JJYWH")
    private String jjywh;

	@Field(value="FHZ")
    private String fhz;

	@Field(value="MSG")
    private String msg;

	 /**POS_SN  - pos流水号 */
	@Field(value="POS_SN")
    private String posSn;
	 /**扩展属性，n代卡 */
	private String ndk;

    public String getPkInsst() {
		return pkInsst;
	}
	public void setPkInsst(String pkInsst) {
		this.pkInsst = pkInsst;
	}
	
	public String getPkInspv() {
		return pkInspv;
	}
	public void setPkInspv(String pkInspv) {
		this.pkInspv = pkInspv;
	}
	public String getYybh(){
        return this.yybh;
    }
    public void setYybh(String yybh){
        this.yybh = yybh;
    }

    public String getPkHp(){
        return this.pkHp;
    }
    public void setPkHp(String pkHp){
        this.pkHp = pkHp;
    }

    public String getPkPi(){
        return this.pkPi;
    }
    public void setPkPi(String pkPi){
        this.pkPi = pkPi;
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

    public String getEuPvtype(){
        return this.euPvtype;
    }
    public void setEuPvtype(String euPvtype){
        this.euPvtype = euPvtype;
    }

    public String getJzjlh(){
        return this.jzjlh;
    }
    public void setJzjlh(String jzjlh){
        this.jzjlh = jzjlh;
    }

    public String getJsywh(){
        return this.jsywh;
    }
    public void setJsywh(String jsywh){
        this.jsywh = jsywh;
    }

    public String getXzlx(){
        return this.xzlx;
    }
    public void setXzlx(String xzlx){
        this.xzlx = xzlx;
    }

    public String getJsbz(){
        return this.jsbz;
    }
    public void setJsbz(String jsbz){
        this.jsbz = jsbz;
    }

    public BigDecimal getGrzflj(){
        return this.grzflj;
    }
    public void setGrzflj(BigDecimal grzflj){
        this.grzflj = grzflj;
    }

    public BigDecimal getGwytcxe(){
        return this.gwytcxe;
    }
    public void setGwytcxe(BigDecimal gwytcxe){
        this.gwytcxe = gwytcxe;
    }

    public BigDecimal getYbzhye(){
        return this.ybzhye;
    }
    public void setYbzhye(BigDecimal ybzhye){
        this.ybzhye = ybzhye;
    }

    public String getNd(){
        return this.nd;
    }
    public void setNd(String nd){
        this.nd = nd;
    }

    public String getRylb(){
        return this.rylb;
    }
    public void setRylb(String rylb){
        this.rylb = rylb;
    }

    public String getKh(){
        return this.kh;
    }
    public void setKh(String kh){
        this.kh = kh;
    }

    public String getJym(){
        return this.jym;
    }
    public void setJym(String jym){
        this.jym = jym;
    }

    public String getPsam(){
        return this.psam;
    }
    public void setPsam(String psam){
        this.psam = psam;
    }

    public BigDecimal getYlfyze(){
        return this.ylfyze;
    }
    public void setYlfyze(BigDecimal ylfyze){
        this.ylfyze = ylfyze;
    }

    public BigDecimal getGzzfuje(){
        return this.gzzfuje;
    }
    public void setGzzfuje(BigDecimal gzzfuje){
        this.gzzfuje = gzzfuje;
    }

    public BigDecimal getGzzfeje(){
        return this.gzzfeje;
    }
    public void setGzzfeje(BigDecimal gzzfeje){
        this.gzzfeje = gzzfeje;
    }

    public BigDecimal getTczfje(){
        return this.tczfje;
    }
    public void setTczfje(BigDecimal tczfje){
        this.tczfje = tczfje;
    }

    public BigDecimal getGwytczf(){
        return this.gwytczf;
    }
    public void setGwytczf(BigDecimal gwytczf){
        this.gwytczf = gwytczf;
    }

    public BigDecimal getXjzfuje(){
        return this.xjzfuje;
    }
    public void setXjzfuje(BigDecimal xjzfuje){
        this.xjzfuje = xjzfuje;
    }

    public BigDecimal getXjzfeje(){
        return this.xjzfeje;
    }
    public void setXjzfeje(BigDecimal xjzfeje){
        this.xjzfeje = xjzfeje;
    }

    public BigDecimal getDejsbz(){
        return this.dejsbz;
    }
    public void setDejsbz(BigDecimal dejsbz){
        this.dejsbz = dejsbz;
    }

    public BigDecimal getJsqzytced(){
        return this.jsqzytced;
    }
    public void setJsqzytced(BigDecimal jsqzytced){
        this.jsqzytced = jsqzytced;
    }

    public Date getJsrq(){
        return this.jsrq;
    }
    public void setJsrq(Date jsrq){
        this.jsrq = jsrq;
    }

    public BigDecimal getBcyltczf(){
        return this.bcyltczf;
    }
    public void setBcyltczf(BigDecimal bcyltczf){
        this.bcyltczf = bcyltczf;
    }

    public String getJbylbz(){
        return this.jbylbz;
    }
    public void setJbylbz(String jbylbz){
        this.jbylbz = jbylbz;
    }

    public String getBcxsbz(){
        return this.bcxsbz;
    }
    public void setBcxsbz(String bcxsbz){
        this.bcxsbz = bcxsbz;
    }

    public String getGwyxsbz(){
        return this.gwyxsbz;
    }
    public void setGwyxsbz(String gwyxsbz){
        this.gwyxsbz = gwyxsbz;
    }

    public BigDecimal getDbtczf(){
        return this.dbtczf;
    }
    public void setDbtczf(BigDecimal dbtczf){
        this.dbtczf = dbtczf;
    }

    public String getDbdxlb(){
        return this.dbdxlb;
    }
    public void setDbdxlb(String dbdxlb){
        this.dbdxlb = dbdxlb;
    }

    public BigDecimal getMzdbtczf(){
        return this.mzdbtczf;
    }
    public void setMzdbtczf(BigDecimal mzdbtczf){
        this.mzdbtczf = mzdbtczf;
    }

    public BigDecimal getMzdbljje(){
        return this.mzdbljje;
    }
    public void setMzdbljje(BigDecimal mzdbljje){
        this.mzdbljje = mzdbljje;
    }

    public String getYfdxlb(){
        return this.yfdxlb;
    }
    public void setYfdxlb(String yfdxlb){
        this.yfdxlb = yfdxlb;
    }

    public BigDecimal getMzyftczf(){
        return this.mzyftczf;
    }
    public void setMzyftczf(BigDecimal mzyftczf){
        this.mzyftczf = mzyftczf;
    }

    public BigDecimal getMzyfljje(){
        return this.mzyfljje;
    }
    public void setMzyfljje(BigDecimal mzyfljje){
        this.mzyfljje = mzyfljje;
    }

    public String getJzlb(){
        return this.jzlb;
    }
    public void setJzlb(String jzlb){
        this.jzlb = jzlb;
    }

    public Date getModityTime(){
        return this.modityTime;
    }
    public void setModityTime(Date modityTime){
        this.modityTime = modityTime;
    }

    public String getStatus(){
        return this.status;
    }
    public void setStatus(String status){
        this.status = status;
    }

    public String getTjlb(){
        return this.tjlb;
    }
    public void setTjlb(String tjlb){
        this.tjlb = tjlb;
    }

    public String getTjjzrq(){
        return this.tjjzrq;
    }
    public void setTjjzrq(String tjjzrq){
        this.tjjzrq = tjjzrq;
    }

    public String getJjywh(){
        return this.jjywh;
    }
    public void setJjywh(String jjywh){
        this.jjywh = jjywh;
    }

    public String getFhz(){
        return this.fhz;
    }
    public void setFhz(String fhz){
        this.fhz = fhz;
    }

    public String getMsg(){
        return this.msg;
    }
    public void setMsg(String msg){
        this.msg = msg;
    }
	public String getPosSn() {
		return posSn;
	}
	public void setPosSn(String posSn) {
		this.posSn = posSn;
	}
	public String getNdk() {
		return ndk;
	}
	public void setNdk(String ndk) {
		this.ndk = ndk;
	}
    
}