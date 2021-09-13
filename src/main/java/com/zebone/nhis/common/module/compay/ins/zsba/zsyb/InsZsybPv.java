package com.zebone.nhis.common.module.compay.ins.zsba.zsyb;

import java.util.Date;

import com.zebone.nhis.common.module.BaseModule;
import com.zebone.platform.modules.dao.build.au.Field;
import com.zebone.platform.modules.dao.build.au.PK;
import com.zebone.platform.modules.dao.build.au.Table;
import com.zebone.platform.modules.dao.build.au.Field.FieldType;
import com.zebone.platform.modules.dao.build.au.Field.KeyId;


/**
 * Table: ins_pv - 外部医保-中山住院医保登记：（普通住院登记[2037]、修改住院登记信息[2002]、取消住院登记[2003]、普通住院 
 *
 * @since 2017-09-06 10:42:10
 */
@Table(value="INS_PV")
public class InsZsybPv extends BaseModule  {

	@PK
	@Field(value="PK_INSPV",id=KeyId.UUID)
    private String pkInspv;

    /** YYBH - 博爱为H003 */
	@Field(value="YYBH")
    private String yybh;

    /** PK_HP - 对应医保的就诊类别，NOT NULL、21医疗住院、31外伤住院、33工伤康复住院、41生育住院、47计生手术住院、 2A日间手术 */
	@Field(value="PK_HP")
    private String pkHp;

	@Field(value="PK_PI")
    private String pkPi;

	@Field(value="PK_PV")
    private String pkPv;

    /** EU_PVTYPE - 1 门诊，2 急诊，3 住院，4 体检 */
	@Field(value="EU_PVTYPE")
    private String euPvtype;

	@Field(value="JZJLH")
    private String jzjlh;

	@Field(value="GMSFHM")
    private String gmsfhm;

    /** GRSXH - NOT NULL */
	@Field(value="GRSXH")
    private String grsxh;

    /** RYRQ - 住院不为空YYYYMMDD hhmmss */
	@Field(value="RYRQ")
    private Date ryrq;

    /** RYZD - 住院不为空 */
	@Field(value="RYZD")
    private String ryzd;

	@Field(value="RYZDGJDM")
    private String ryzdgjdm;

	@Field(value="RYZD2")
    private String ryzd2;
	
	@Field(value="RYZD3")
    private String ryzd3;

	@Field(value="RYZD4")
    private String ryzd4;

    /** ZZYSXM - 住院不为空 */
	@Field(value="ZZYSXM")
    private String zzysxm;

    /** BQDM - 住院不为空 */
	@Field(value="BQDM")
    private String bqdm;

	@Field(value="CWDH")
    private String cwdh;

    /** SHJG - 1、医院方通过 2、医院方不通过（默认为医院方通过） */
	@Field(value="SHJG")
    private String shjg;

    /** SFZY - 0否 1 是 */
	@Field(value="SFZY")
    private String sfzy;

    /** ZRYY - 字典项 */
	@Field(value="ZRYY")
    private String zryy;

    /** JSFFBZ - 工伤医保需要填写： 0 非旧伤复发、1 旧伤复发 */
	@Field(value="JSFFBZ")
    private String jsffbz;

    /** WSBZ - 工伤医保需要填写：1 疑似工伤、 0或空 非疑似工伤 */
	@Field(value="WSBZ")
    private String wsbz;

    /** SSRQ - 工伤医保不能为空，受伤日期不能晚于入院日期 YYYYMMDD hhmmss */
	@Field(value="SSRQ")
    private Date ssrq;

    /** SYLB - 生育医保必填 */
	@Field(value="SYLB")
    private String sylb;

	@Field(value="ZSZH")
    private String zszh;

    /** XZLX - 56医疗 20工伤 30 生育 */
	@Field(value="XZLX")
    private String xzlx;

    /** CYRQ - 住院不为空YYYYMMDD hhmmss */
	@Field(value="CYRQ")
    private Date cyrq;

    /** CYZD - 住院不为空 */
	@Field(value="CYZD")
    private String cyzd;

	@Field(value="CYZDGJDM")
    private String cyzdgjdm;

	@Field(value="CYZD2")
    private String cyzd2;

	@Field(value="CYZDGJDM2")
    private String cyzdgjdm2;

	@Field(value="CYZD3")
    private String cyzd3;

	@Field(value="CYZDGJDM3")
    private String cyzdgjdm3;

	@Field(value="CYZD4")
    private String cyzd4;

	@Field(value="CYZDGJDM4")
    private String cyzdgjdm4;

	@Field(value="RYZS")
    private String ryzs;

    /** CYQK - 住院不为空,出院转归 */
	@Field(value="CYQK")
    private String cyqk;

    /** RYQK - 住院不为空： 1危、 2急、3一般 */
	@Field(value="RYQK")
    private String ryqk;

	@Field(value="ZLFF")
    private String zlff;

    /** SSMC - 治疗方法名称 */
	@Field(value="SSMC")
    private String ssmc;

	@Field(value="ZLFF2")
    private String zlff2;

	@Field(value="ZLFFMC2")
    private String zlffmc2;

	@Field(value="ZLFF3")
    private String zlff3;

	@Field(value="ZLFFMC3")
    private String zlffmc3;

	@Field(value="ZLFF4")
    private String zlff4;

	@Field(value="ZLFFMC4")
    private String zlffmc4;

    /** SSRQ1 - 日间手术和分值修改需要：修改分值结算住院信息[2013]、日间手术信息维护[2150] */
	@Field(value="SSRQ1")
    private Date ssrq1;

    /** TZCYSJ - YYYYMMDD hhmmss */
	@Field(value="TZCYSJ")
    private Date tzcysj;

    /** ZWYY - 字典，CYQK为转院时必录：1 治愈、 2 好转、3 未愈、4 死亡、5 其他、 6 转院 */
	@Field(value="ZWYY")
    private String zwyy;

    /** JZLB - 11普通门诊、12特殊病种门诊、21普通住院、71住保特殊病种、72居民特殊病种、13居民门诊、 41生育住院（基本医疗）、 43产前检查 生育基金统筹 支付、 44计生手术门诊	生育基金统筹 支付、 */
	@Field(value="JZLB")
    private String jzlb;

	@Field(value="MODITY_TIME",date=FieldType.UPDATE)
    private Date modityTime;

    /** STATUS - 1入院登记成功，2入院登记失败；3资料维护成功，4资料维护失败；5出院登记成功，6出院登记失败；7取消出院登记成功， 8取消出院登记失败；9取消入院登记成功， 10取消入院登记失败；11结算成功， 1 */
	@Field(value="STATUS")
    private String status;

	@Field(value="FHZ")
    private String fhz;

	@Field(value="MSG")
    private String msg;


    public String getPkInspv(){
        return this.pkInspv;
    }
    public void setPkInspv(String pkInspv){
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

    public String getGmsfhm(){
        return this.gmsfhm;
    }
    public void setGmsfhm(String gmsfhm){
        this.gmsfhm = gmsfhm;
    }

    public String getGrsxh(){
        return this.grsxh;
    }
    public void setGrsxh(String grsxh){
        this.grsxh = grsxh;
    }

    public Date getRyrq(){
        return this.ryrq;
    }
    public void setRyrq(Date ryrq){
        this.ryrq = ryrq;
    }

    public String getRyzd(){
        return this.ryzd;
    }
    public void setRyzd(String ryzd){
        this.ryzd = ryzd;
    }

    public String getRyzdgjdm(){
        return this.ryzdgjdm;
    }
    public void setRyzdgjdm(String ryzdgjdm){
        this.ryzdgjdm = ryzdgjdm;
    }

    public String getRyzd2(){
        return this.ryzd2;
    }
    public void setRyzd2(String ryzd2){
        this.ryzd2 = ryzd2;
    }

    public String getRyzd3(){
        return this.ryzd3;
    }
    public void setRyzd3(String ryzd3){
        this.ryzd3 = ryzd3;
    }

    public String getRyzd4(){
        return this.ryzd4;
    }
    public void setRyzd4(String ryzd4){
        this.ryzd4 = ryzd4;
    }

    public String getZzysxm(){
        return this.zzysxm;
    }
    public void setZzysxm(String zzysxm){
        this.zzysxm = zzysxm;
    }

    public String getBqdm(){
        return this.bqdm;
    }
    public void setBqdm(String bqdm){
        this.bqdm = bqdm;
    }

    public String getCwdh(){
        return this.cwdh;
    }
    public void setCwdh(String cwdh){
        this.cwdh = cwdh;
    }

    public String getShjg(){
        return this.shjg;
    }
    public void setShjg(String shjg){
        this.shjg = shjg;
    }

    public String getSfzy(){
        return this.sfzy;
    }
    public void setSfzy(String sfzy){
        this.sfzy = sfzy;
    }

    public String getZryy(){
        return this.zryy;
    }
    public void setZryy(String zryy){
        this.zryy = zryy;
    }

    public String getJsffbz(){
        return this.jsffbz;
    }
    public void setJsffbz(String jsffbz){
        this.jsffbz = jsffbz;
    }

    public String getWsbz(){
        return this.wsbz;
    }
    public void setWsbz(String wsbz){
        this.wsbz = wsbz;
    }

    public Date getSsrq(){
        return this.ssrq;
    }
    public void setSsrq(Date ssrq){
        this.ssrq = ssrq;
    }

    public String getSylb(){
        return this.sylb;
    }
    public void setSylb(String sylb){
        this.sylb = sylb;
    }

    public String getZszh(){
        return this.zszh;
    }
    public void setZszh(String zszh){
        this.zszh = zszh;
    }

    public String getXzlx(){
        return this.xzlx;
    }
    public void setXzlx(String xzlx){
        this.xzlx = xzlx;
    }

    public Date getCyrq(){
        return this.cyrq;
    }
    public void setCyrq(Date cyrq){
        this.cyrq = cyrq;
    }

    public String getCyzd(){
        return this.cyzd;
    }
    public void setCyzd(String cyzd){
        this.cyzd = cyzd;
    }

    public String getCyzdgjdm(){
        return this.cyzdgjdm;
    }
    public void setCyzdgjdm(String cyzdgjdm){
        this.cyzdgjdm = cyzdgjdm;
    }

    public String getCyzd2(){
        return this.cyzd2;
    }
    public void setCyzd2(String cyzd2){
        this.cyzd2 = cyzd2;
    }

    public String getCyzdgjdm2(){
        return this.cyzdgjdm2;
    }
    public void setCyzdgjdm2(String cyzdgjdm2){
        this.cyzdgjdm2 = cyzdgjdm2;
    }

    public String getCyzd3(){
        return this.cyzd3;
    }
    public void setCyzd3(String cyzd3){
        this.cyzd3 = cyzd3;
    }

    public String getCyzdgjdm3(){
        return this.cyzdgjdm3;
    }
    public void setCyzdgjdm3(String cyzdgjdm3){
        this.cyzdgjdm3 = cyzdgjdm3;
    }

    public String getCyzd4(){
        return this.cyzd4;
    }
    public void setCyzd4(String cyzd4){
        this.cyzd4 = cyzd4;
    }

    public String getCyzdgjdm4(){
        return this.cyzdgjdm4;
    }
    public void setCyzdgjdm4(String cyzdgjdm4){
        this.cyzdgjdm4 = cyzdgjdm4;
    }

    public String getRyzs(){
        return this.ryzs;
    }
    public void setRyzs(String ryzs){
        this.ryzs = ryzs;
    }

    public String getCyqk(){
        return this.cyqk;
    }
    public void setCyqk(String cyqk){
        this.cyqk = cyqk;
    }

    public String getRyqk(){
        return this.ryqk;
    }
    public void setRyqk(String ryqk){
        this.ryqk = ryqk;
    }

    public String getZlff(){
        return this.zlff;
    }
    public void setZlff(String zlff){
        this.zlff = zlff;
    }

    public String getSsmc(){
        return this.ssmc;
    }
    public void setSsmc(String ssmc){
        this.ssmc = ssmc;
    }

    public String getZlff2(){
        return this.zlff2;
    }
    public void setZlff2(String zlff2){
        this.zlff2 = zlff2;
    }

    public String getZlffmc2(){
        return this.zlffmc2;
    }
    public void setZlffmc2(String zlffmc2){
        this.zlffmc2 = zlffmc2;
    }

    public String getZlff3(){
        return this.zlff3;
    }
    public void setZlff3(String zlff3){
        this.zlff3 = zlff3;
    }

    public String getZlffmc3(){
        return this.zlffmc3;
    }
    public void setZlffmc3(String zlffmc3){
        this.zlffmc3 = zlffmc3;
    }

    public String getZlff4(){
        return this.zlff4;
    }
    public void setZlff4(String zlff4){
        this.zlff4 = zlff4;
    }

    public String getZlffmc4(){
        return this.zlffmc4;
    }
    public void setZlffmc4(String zlffmc4){
        this.zlffmc4 = zlffmc4;
    }

    public Date getSsrq1(){
        return this.ssrq1;
    }
    public void setSsrq1(Date ssrq1){
        this.ssrq1 = ssrq1;
    }

    public Date getTzcysj(){
        return this.tzcysj;
    }
    public void setTzcysj(Date tzcysj){
        this.tzcysj = tzcysj;
    }

    public String getZwyy(){
        return this.zwyy;
    }
    public void setZwyy(String zwyy){
        this.zwyy = zwyy;
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
}