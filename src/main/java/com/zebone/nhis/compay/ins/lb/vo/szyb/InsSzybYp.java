package com.zebone.nhis.compay.ins.lb.vo.szyb;

import java.util.Date;

import com.zebone.nhis.common.module.BaseModule;
import com.zebone.platform.modules.dao.build.au.Field;
import com.zebone.platform.modules.dao.build.au.PK;
import com.zebone.platform.modules.dao.build.au.Table;
import com.zebone.platform.modules.dao.build.au.Field.KeyId;


/**
 * Table: INS_SZYB_YP 
 *
 * @since 2018-06-02 10:35:53
 */
@Table(value="INS_SZYB_YP")
public class InsSzybYp extends BaseModule  {

    /** PK_ID - 主键 */
	@PK
	@Field(value="PK_ID",id=KeyId.UUID)
    private String pkId;

    /** YPBM - YPBM-药品编码 */
	@Field(value="YPBM")
    private String ypbm;

    /** TYMC - TYMC-通用名称 */
	@Field(value="TYMC")
    private String tymc;

    /** YWMC - YWMC-英文名称 */
	@Field(value="YWMC")
    private String ywmc;

    /** SFLB - SFLB-收费类别 */
	@Field(value="SFLB")
    private String sflb;

    /** CFYBZ - CFYBZ-处方药标志 */
	@Field(value="CFYBZ")
    private String cfybz;

    /** SFXMDJ - SFXMDJ-收费项目等级 */
	@Field(value="SFXMDJ")
    private String sfxmdj;

    /** PYM - PYM-拼音码 */
	@Field(value="PYM")
    private String pym;

    /** YPJLDW - YPJLDW-药品剂量单位 */
	@Field(value="YPJLDW")
    private String ypjldw;

    /** ZGJG - ZGJG-最高价格 */
	@Field(value="ZGJG")
    private String zgjg;

    /** XFXJ - XFXJ-先付限价 */
	@Field(value="XFXJ")
    private String xfxj;

    /** LXEYZGXJ - LXEYZGXJ-离休二乙最高限价 */
	@Field(value="LXEYZGXJ")
    private String lxeyzgxj;

    /** PTMZZFBL - PTMZZFBL-普通门诊自付比例 */
	@Field(value="PTMZZFBL")
    private String ptmzzfbl;

    /** ZYZFBL - ZYZFBL-住院自付比例 */
	@Field(value="ZYZFBL")
    private String zyzfbl;

    /** LXZFBL - LXZFBL-离休自付比例 */
	@Field(value="LXZFBL")
    private String lxzfbl;

    /** GSZFBL - GSZFBL-工伤自付比例 */
	@Field(value="GSZFBL")
    private String gszfbl;

    /** SYZFBL - SYZFBL-生育自付比例 */
	@Field(value="SYZFBL")
    private String syzfbl;

    /** EYZFBL - EYZFBL-二乙自付比例 */
	@Field(value="EYZFBL")
    private String eyzfbl;

    /** JSZNXSZFBL - JSZNXSZFBL-家属子女学生自付比例 */
	@Field(value="JSZNXSZFBL")
    private String jsznxszfbl;

    /** YNZJBZ - YNZJBZ-院内制剂标志 */
	@Field(value="YNZJBZ")
    private String ynzjbz;

    /** DDYLJGBH - DDYLJGBH-定点医疗机构编号 */
	@Field(value="DDYLJGBH")
    private String ddyljgbh;

    /** SFXYSPBZ - SFXYSPBZ-是否需要审批标志 */
	@Field(value="SFXYSPBZ")
    private String sfxyspbz;

    /** ZXYYDJ - ZXYYDJ-最小医院等级 */
	@Field(value="ZXYYDJ")
    private String zxyydj;

    /** ZXYSDJ - ZXYSDJ-最小医师等级 */
	@Field(value="ZXYSDJ")
    private String zxysdj;

    /** ZLBFJRTCBZ - ZLBFJRTCBZ-自理部分进入统筹标志 */
	@Field(value="ZLBFJRTCBZ")
    private String zlbfjrtcbz;

    /** ZLBFJRJZYLBZ - ZLBFJRJZYLBZ-自理部分进入救助医疗标志 */
	@Field(value="ZLBFJRJZYLBZ")
    private String zlbfjrjzylbz;

    /** SFZBYP - SFZBYP-是否招标药品 */
	@Field(value="SFZBYP")
    private String sfzbyp;

    /** ZBJG - ZBJG-招标价格 */
	@Field(value="ZBJG")
    private String zbjg;

    /** TYBZ - TYBZ-特药标志 */
	@Field(value="TYBZ")
    private String tybz;

    /** SFECBX - SFECBX-是否二次报销 */
	@Field(value="SFECBX")
    private String sfecbx;

    /** JX - JX-剂型 */
	@Field(value="JX")
    private String jx;

    /** MCYL - MCYL-每次用量 */
	@Field(value="MCYL")
    private String mcyl;

    /** SYPC - SYPC-使用频次 */
	@Field(value="SYPC")
    private String sypc;

    /** YF - YF-用法 */
	@Field(value="YF")
    private String yf;

    /** WBZJM - WBZJM-五笔助记码 */
	@Field(value="WBZJM")
    private String wbzjm;

    /** DW - DW-单位 */
	@Field(value="DW")
    private String dw;

    /** GG - GG-规格 */
	@Field(value="GG")
    private String gg;

    /** XDTS - XDTS-限定天数 */
	@Field(value="XDTS")
    private String xdts;

    /** YPSPM - YPSPM-药品商品名 */
	@Field(value="YPSPM")
    private String ypspm;

    /** SPMJG - SPMJG-商品名价格 */
	@Field(value="SPMJG")
    private String spmjg;

    /** SPMPYM - SPMPYM-商品名拼音码 */
	@Field(value="SPMPYM")
    private String spmpym;

    /** SPMWBM - SPMWBM-商品名五笔码 */
	@Field(value="SPMWBM")
    private String spmwbm;

    /** YCMC - YCMC-药厂名称 */
	@Field(value="YCMC")
    private String ycmc;

    /** GYZZ - GYZZ-国药准字 */
	@Field(value="GYZZ")
    private String gyzz;

    /** JBR - JBR-经办人 */
	@Field(value="JBR")
    private String jbr;

    /** JBRQ - JBRQ-经办日期 */
	@Field(value="JBRQ")
    private String jbrq;

    /** KSSJ - KSSJ-开始时间 */
	@Field(value="KSSJ")
    private String kssj;

    /** ZZSJ - ZZSJ-终止时间 */
	@Field(value="ZZSJ")
    private String zzsj;

    /** BZ - BZ-备注 */
	@Field(value="BZ")
    private String bz;

    /** ZDYM - ZDYM-自定义码 */
	@Field(value="ZDYM")
    private String zdym;

    /** GJMLBM - GJMLBM-国家目录编码 */
	@Field(value="GJMLBM")
    private String gjmlbm;

    /** CKJG - CKJG-参考价格 */
	@Field(value="CKJG")
    private String ckjg;

    /** CKYY - CKYY-参考医院 */
	@Field(value="CKYY")
    private String ckyy;

    /** XZSYFW - XZSYFW-限制使用范围 */
	@Field(value="XZSYFW")
    private String xzsyfw;

    /** CD - CD-产地 */
	@Field(value="CD")
    private String cd;

    /** YXBZ - YXBZ-有效标志 */
	@Field(value="YXBZ")
    private String yxbz;

    /** GJJBYPMLJCWSYLJGYYBZ - GJJBYPMLJCWSYLJGYYBZ-国家基本药品目录基层卫生医疗机构用药标志 */
	@Field(value="GJJBYPMLJCWSYLJGYYBZ")
    private String gjjbypmljcwsyljgyybz;

    /** JMSYBZ - JMSYBZ-居民使用标志 */
	@Field(value="JMSYBZ")
    private String jmsybz;

    /** XBXDZFFW - XBXDZFFW-性别限定支付范围 */
	@Field(value="XBXDZFFW")
    private String xbxdzffw;

    /** JBXDZFFW - JBXDZFFW-疾病限定支付范围 */
	@Field(value="JBXDZFFW")
    private String jbxdzffw;

    /** MODIFY_TIME - 最后操作时间 */
	@Field(value="MODIFY_TIME")
    private Date modifyTime;


    public String getPkId(){
        return this.pkId;
    }
    public void setPkId(String pkId){
        this.pkId = pkId;
    }

    public String getYpbm(){
        return this.ypbm;
    }
    public void setYpbm(String ypbm){
        this.ypbm = ypbm;
    }

    public String getTymc(){
        return this.tymc;
    }
    public void setTymc(String tymc){
        this.tymc = tymc;
    }

    public String getYwmc(){
        return this.ywmc;
    }
    public void setYwmc(String ywmc){
        this.ywmc = ywmc;
    }

    public String getSflb(){
        return this.sflb;
    }
    public void setSflb(String sflb){
        this.sflb = sflb;
    }

    public String getCfybz(){
        return this.cfybz;
    }
    public void setCfybz(String cfybz){
        this.cfybz = cfybz;
    }

    public String getSfxmdj(){
        return this.sfxmdj;
    }
    public void setSfxmdj(String sfxmdj){
        this.sfxmdj = sfxmdj;
    }

    public String getPym(){
        return this.pym;
    }
    public void setPym(String pym){
        this.pym = pym;
    }

    public String getYpjldw(){
        return this.ypjldw;
    }
    public void setYpjldw(String ypjldw){
        this.ypjldw = ypjldw;
    }

    public String getZgjg(){
        return this.zgjg;
    }
    public void setZgjg(String zgjg){
        this.zgjg = zgjg;
    }

    public String getXfxj(){
        return this.xfxj;
    }
    public void setXfxj(String xfxj){
        this.xfxj = xfxj;
    }

    public String getLxeyzgxj(){
        return this.lxeyzgxj;
    }
    public void setLxeyzgxj(String lxeyzgxj){
        this.lxeyzgxj = lxeyzgxj;
    }

    public String getPtmzzfbl(){
        return this.ptmzzfbl;
    }
    public void setPtmzzfbl(String ptmzzfbl){
        this.ptmzzfbl = ptmzzfbl;
    }

    public String getZyzfbl(){
        return this.zyzfbl;
    }
    public void setZyzfbl(String zyzfbl){
        this.zyzfbl = zyzfbl;
    }

    public String getLxzfbl(){
        return this.lxzfbl;
    }
    public void setLxzfbl(String lxzfbl){
        this.lxzfbl = lxzfbl;
    }

    public String getGszfbl(){
        return this.gszfbl;
    }
    public void setGszfbl(String gszfbl){
        this.gszfbl = gszfbl;
    }

    public String getSyzfbl(){
        return this.syzfbl;
    }
    public void setSyzfbl(String syzfbl){
        this.syzfbl = syzfbl;
    }

    public String getEyzfbl(){
        return this.eyzfbl;
    }
    public void setEyzfbl(String eyzfbl){
        this.eyzfbl = eyzfbl;
    }

    public String getJsznxszfbl(){
        return this.jsznxszfbl;
    }
    public void setJsznxszfbl(String jsznxszfbl){
        this.jsznxszfbl = jsznxszfbl;
    }

    public String getYnzjbz(){
        return this.ynzjbz;
    }
    public void setYnzjbz(String ynzjbz){
        this.ynzjbz = ynzjbz;
    }

    public String getDdyljgbh(){
        return this.ddyljgbh;
    }
    public void setDdyljgbh(String ddyljgbh){
        this.ddyljgbh = ddyljgbh;
    }

    public String getSfxyspbz(){
        return this.sfxyspbz;
    }
    public void setSfxyspbz(String sfxyspbz){
        this.sfxyspbz = sfxyspbz;
    }

    public String getZxyydj(){
        return this.zxyydj;
    }
    public void setZxyydj(String zxyydj){
        this.zxyydj = zxyydj;
    }

    public String getZxysdj(){
        return this.zxysdj;
    }
    public void setZxysdj(String zxysdj){
        this.zxysdj = zxysdj;
    }

    public String getZlbfjrtcbz(){
        return this.zlbfjrtcbz;
    }
    public void setZlbfjrtcbz(String zlbfjrtcbz){
        this.zlbfjrtcbz = zlbfjrtcbz;
    }

    public String getZlbfjrjzylbz(){
        return this.zlbfjrjzylbz;
    }
    public void setZlbfjrjzylbz(String zlbfjrjzylbz){
        this.zlbfjrjzylbz = zlbfjrjzylbz;
    }

    public String getSfzbyp(){
        return this.sfzbyp;
    }
    public void setSfzbyp(String sfzbyp){
        this.sfzbyp = sfzbyp;
    }

    public String getZbjg(){
        return this.zbjg;
    }
    public void setZbjg(String zbjg){
        this.zbjg = zbjg;
    }

    public String getTybz(){
        return this.tybz;
    }
    public void setTybz(String tybz){
        this.tybz = tybz;
    }

    public String getSfecbx(){
        return this.sfecbx;
    }
    public void setSfecbx(String sfecbx){
        this.sfecbx = sfecbx;
    }

    public String getJx(){
        return this.jx;
    }
    public void setJx(String jx){
        this.jx = jx;
    }

    public String getMcyl(){
        return this.mcyl;
    }
    public void setMcyl(String mcyl){
        this.mcyl = mcyl;
    }

    public String getSypc(){
        return this.sypc;
    }
    public void setSypc(String sypc){
        this.sypc = sypc;
    }

    public String getYf(){
        return this.yf;
    }
    public void setYf(String yf){
        this.yf = yf;
    }

    public String getWbzjm(){
        return this.wbzjm;
    }
    public void setWbzjm(String wbzjm){
        this.wbzjm = wbzjm;
    }

    public String getDw(){
        return this.dw;
    }
    public void setDw(String dw){
        this.dw = dw;
    }

    public String getGg(){
        return this.gg;
    }
    public void setGg(String gg){
        this.gg = gg;
    }

    public String getXdts(){
        return this.xdts;
    }
    public void setXdts(String xdts){
        this.xdts = xdts;
    }

    public String getYpspm(){
        return this.ypspm;
    }
    public void setYpspm(String ypspm){
        this.ypspm = ypspm;
    }

    public String getSpmjg(){
        return this.spmjg;
    }
    public void setSpmjg(String spmjg){
        this.spmjg = spmjg;
    }

    public String getSpmpym(){
        return this.spmpym;
    }
    public void setSpmpym(String spmpym){
        this.spmpym = spmpym;
    }

    public String getSpmwbm(){
        return this.spmwbm;
    }
    public void setSpmwbm(String spmwbm){
        this.spmwbm = spmwbm;
    }

    public String getYcmc(){
        return this.ycmc;
    }
    public void setYcmc(String ycmc){
        this.ycmc = ycmc;
    }

    public String getGyzz(){
        return this.gyzz;
    }
    public void setGyzz(String gyzz){
        this.gyzz = gyzz;
    }

    public String getJbr(){
        return this.jbr;
    }
    public void setJbr(String jbr){
        this.jbr = jbr;
    }

    public String getJbrq(){
        return this.jbrq;
    }
    public void setJbrq(String jbrq){
        this.jbrq = jbrq;
    }

    public String getKssj(){
        return this.kssj;
    }
    public void setKssj(String kssj){
        this.kssj = kssj;
    }

    public String getZzsj(){
        return this.zzsj;
    }
    public void setZzsj(String zzsj){
        this.zzsj = zzsj;
    }

    public String getBz(){
        return this.bz;
    }
    public void setBz(String bz){
        this.bz = bz;
    }

    public String getZdym(){
        return this.zdym;
    }
    public void setZdym(String zdym){
        this.zdym = zdym;
    }

    public String getGjmlbm(){
        return this.gjmlbm;
    }
    public void setGjmlbm(String gjmlbm){
        this.gjmlbm = gjmlbm;
    }

    public String getCkjg(){
        return this.ckjg;
    }
    public void setCkjg(String ckjg){
        this.ckjg = ckjg;
    }

    public String getCkyy(){
        return this.ckyy;
    }
    public void setCkyy(String ckyy){
        this.ckyy = ckyy;
    }

    public String getXzsyfw(){
        return this.xzsyfw;
    }
    public void setXzsyfw(String xzsyfw){
        this.xzsyfw = xzsyfw;
    }

    public String getCd(){
        return this.cd;
    }
    public void setCd(String cd){
        this.cd = cd;
    }

    public String getYxbz(){
        return this.yxbz;
    }
    public void setYxbz(String yxbz){
        this.yxbz = yxbz;
    }

    public String getGjjbypmljcwsyljgyybz(){
        return this.gjjbypmljcwsyljgyybz;
    }
    public void setGjjbypmljcwsyljgyybz(String gjjbypmljcwsyljgyybz){
        this.gjjbypmljcwsyljgyybz = gjjbypmljcwsyljgyybz;
    }

    public String getJmsybz(){
        return this.jmsybz;
    }
    public void setJmsybz(String jmsybz){
        this.jmsybz = jmsybz;
    }

    public String getXbxdzffw(){
        return this.xbxdzffw;
    }
    public void setXbxdzffw(String xbxdzffw){
        this.xbxdzffw = xbxdzffw;
    }

    public String getJbxdzffw(){
        return this.jbxdzffw;
    }
    public void setJbxdzffw(String jbxdzffw){
        this.jbxdzffw = jbxdzffw;
    }

    public Date getModifyTime(){
        return this.modifyTime;
    }
    public void setModifyTime(Date modifyTime){
        this.modifyTime = modifyTime;
    }
}