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
 * Table: ins_item - 外部医保-医保目录：由医保管理员不定期调用[2020]，从社保局下载或者更新三大目录。在返回的数据集中，与本表进行对比。 
 *
 * @since 2017-09-06 10:42:10
 */
@Table(value="INS_ITEM")
public class InsZsBaYbItem extends BaseModule  {

	@PK
	@Field(value="PK_INSITEM",id=KeyId.UUID)
    private String pkInsitem;

	@Field(value="BGSJ")
    private Date bgsj;

	@Field(value="PK_HP")
    private String pkHp;

	@Field(value="XMBH")
    private String xmbh;

    /** XMLB - 1 药品，2 材料，3 诊疗 */
	@Field(value="XMLB")
    private String xmlb;

	@Field(value="ZWMC")
    private String zwmc;

	@Field(value="YWMC")
    private String ywmc;

	@Field(value="SPCODE")
    private String spcode;

	@Field(value="D_CODE")
    private String dCode;

	@Field(value="FLDM")
    private String fldm;

	@Field(value="YPGG")
    private String ypgg;

	@Field(value="YPJX")
    private String ypjx;

	@Field(value="ZJM")
    private String zjm;

    /** YWMLDJ - 1甲类2乙类3自费 */
	@Field(value="YWMLDJ")
    private String ywmldj;

	@Field(value="JBZFBL")
    private BigDecimal jbzfbl;

	@Field(value="SQZFBL")
    private BigDecimal sqzfbl;

	@Field(value="LXZFBL")
    private BigDecimal lxzfbl;

	@Field(value="GSZFBL")
    private BigDecimal gszfbl;

	@Field(value="SYZFBL")
    private BigDecimal syzfbl;

	@Field(value="TMZFBL")
    private BigDecimal tmzfbl;

	@Field(value="BCZFBL")
    private BigDecimal bczfbl;

	@Field(value="JZJG")
    private BigDecimal jzjg;
	
	@Field(value="XJJE")
    private BigDecimal xjje;
	
	@Field(value="LXXJJE")
    private BigDecimal lxxjje;
	
	@Field(value="JZKBZ")
    private BigDecimal jzkbz;
	
	@Field(value="XZKBZ")
    private BigDecimal xzkbz;
	
	@Field(value="TDKBZ")
    private BigDecimal tdkbz;
	
	@Field(value="CJKBZ")
    private BigDecimal cjlbz;
	
	@Field(value="BZDM")
    private BigDecimal bzdm;

	@Field(value="YJJYPBM")
    private String yjjypbm;

	@Field(value="YYCLZCZMC")
    private String yyclzczmc;

	@Field(value="YYCLSYJZCH")
    private String yyclsyjzch;

	@Field(value="TXBZ")
    private String txbz;

	@Field(value="KSSJ")
    private String kssj;

	@Field(value="ZSSJ")
    private String zssj;

	@Field(value="BZ")
    private String bz;
	
	@Field(value="SCDW")
    private String scdw;
	
	@Field(value="CWLR")
    private String cwlr;
	
	@Field(value="SM")
    private String sm;
	
	@Field(value="XMNH")
    private String xmnh;
	
	@Field(value="NATION_CODE")
    private String nationCode;

	@Field(value="YBZFBZ")
    private String ybzfbz;//医保支付标准（耗材映射库）
	@Field(value="YJFL")
    private String yjfl;//一级分类（耗材映射库）
	@Field(value="EJFL")
    private String ejfl;//二级分类（耗材映射库）
	@Field(value="SJFL")
    private String sjfl;//三级分类（耗材映射库）
	@Field(value="CZ")
    private String cz;//材质（耗材映射库）
	@Field(value="TZ")
    private String tz;//特征（耗材映射库）
	@Field(value="DJCPMC")
    private String djcpmc;//单件产品名称（耗材映射库）
	@Field(value="GGXHS")
    private String ggxhs;//规格型号（耗材映射库）
	@Field(value="BZCZ")
	private String bzcz;//包装材质（西药中成药代码库）
	@Field(value="ZXBZSL")
	private String zxbzsl;//最小包装数量（西药中成药代码库）
	@Field(value="ZXBZDW")
	private String zxbzdw;//最小包装单位（西药中成药代码库）
	@Field(value="ZXZJDW")
	private String zxzjdw;//最小制剂单位（西药中成药代码库）
	
	@Field(value="FHZ")
    private Integer fhz;

	@Field(value="MSG")
    private String msg;

	@Field(value="MODITY_TIME",date=FieldType.UPDATE)
    private Date modityTime;

    public String getPkInsitem(){
        return this.pkInsitem;
    }
    public void setPkInsitem(String pkInsitem){
        this.pkInsitem = pkInsitem;
    }

    public Date getBgsj(){
        return this.bgsj;
    }
    public void setBgsj(Date bgsj){
        this.bgsj = bgsj;
    }

    public String getPkHp(){
        return this.pkHp;
    }
    public void setPkHp(String pkHp){
        this.pkHp = pkHp;
    }

    public String getXmbh(){
        return this.xmbh;
    }
    public void setXmbh(String xmbh){
        this.xmbh = xmbh;
    }

    public String getXmlb(){
        return this.xmlb;
    }
    public void setXmlb(String xmlb){
        this.xmlb = xmlb;
    }

    public String getZwmc(){
        return this.zwmc;
    }
    public void setZwmc(String zwmc){
        this.zwmc = zwmc;
    }

    public String getYwmc(){
        return this.ywmc;
    }
    public void setYwmc(String ywmc){
        this.ywmc = ywmc;
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

    public String getFldm(){
        return this.fldm;
    }
    public void setFldm(String fldm){
        this.fldm = fldm;
    }

    public String getYpgg(){
        return this.ypgg;
    }
    public void setYpgg(String ypgg){
        this.ypgg = ypgg;
    }

    public String getYpjx(){
        return this.ypjx;
    }
    public void setYpjx(String ypjx){
        this.ypjx = ypjx;
    }

    public String getZjm(){
        return this.zjm;
    }
    public void setZjm(String zjm){
        this.zjm = zjm;
    }

    public String getYwmldj(){
        return this.ywmldj;
    }
    public void setYwmldj(String ywmldj){
        this.ywmldj = ywmldj;
    }

    public BigDecimal getJbzfbl(){
        return this.jbzfbl;
    }
    public void setJbzfbl(BigDecimal jbzfbl){
        this.jbzfbl = jbzfbl;
    }

    public BigDecimal getSqzfbl(){
        return this.sqzfbl;
    }
    public void setSqzfbl(BigDecimal sqzfbl){
        this.sqzfbl = sqzfbl;
    }

    public BigDecimal getLxzfbl(){
        return this.lxzfbl;
    }
    public void setLxzfbl(BigDecimal lxzfbl){
        this.lxzfbl = lxzfbl;
    }

    public BigDecimal getGszfbl(){
        return this.gszfbl;
    }
    public void setGszfbl(BigDecimal gszfbl){
        this.gszfbl = gszfbl;
    }

    public BigDecimal getSyzfbl(){
        return this.syzfbl;
    }
    public void setSyzfbl(BigDecimal syzfbl){
        this.syzfbl = syzfbl;
    }

    public BigDecimal getTmzfbl(){
        return this.tmzfbl;
    }
    public void setTmzfbl(BigDecimal tmzfbl){
        this.tmzfbl = tmzfbl;
    }

    public BigDecimal getBczfbl(){
        return this.bczfbl;
    }
    public void setBczfbl(BigDecimal bczfbl){
        this.bczfbl = bczfbl;
    }

    public BigDecimal getJzjg(){
        return this.jzjg;
    }
    public void setJzjg(BigDecimal jzjg){
        this.jzjg = jzjg;
    }

    public String getYjjypbm(){
        return this.yjjypbm;
    }
    public void setYjjypbm(String yjjypbm){
        this.yjjypbm = yjjypbm;
    }

    public String getYyclzczmc(){
        return this.yyclzczmc;
    }
    public void setYyclzczmc(String yyclzczmc){
        this.yyclzczmc = yyclzczmc;
    }

    public String getYyclsyjzch(){
        return this.yyclsyjzch;
    }
    public void setYyclsyjzch(String yyclsyjzch){
        this.yyclsyjzch = yyclsyjzch;
    }

    public String getTxbz(){
        return this.txbz;
    }
    public void setTxbz(String txbz){
        this.txbz = txbz;
    }

    public String getKssj(){
        return this.kssj;
    }
    public void setKssj(String kssj){
        this.kssj = kssj;
    }

    public String getZssj(){
        return this.zssj;
    }
    public void setZssj(String zssj){
        this.zssj = zssj;
    }

    public String getBz(){
        return this.bz;
    }
    public void setBz(String bz){
        this.bz = bz;
    }

    public Integer getFhz(){
        return this.fhz;
    }
    public void setFhz(Integer fhz){
        this.fhz = fhz;
    }

    public String getMsg(){
        return this.msg;
    }
    public void setMsg(String msg){
        this.msg = msg;
    }

    public Date getModityTime(){
        return this.modityTime;
    }
    public void setModityTime(Date modityTime){
        this.modityTime = modityTime;
    }
	public BigDecimal getXjje() {
		return xjje;
	}
	public void setXjje(BigDecimal xjje) {
		this.xjje = xjje;
	}
	public BigDecimal getLxxjje() {
		return lxxjje;
	}
	public void setLxxjje(BigDecimal lxxjje) {
		this.lxxjje = lxxjje;
	}
	public BigDecimal getJzkbz() {
		return jzkbz;
	}
	public void setJzkbz(BigDecimal jzkbz) {
		this.jzkbz = jzkbz;
	}
	public BigDecimal getXzkbz() {
		return xzkbz;
	}
	public void setXzkbz(BigDecimal xzkbz) {
		this.xzkbz = xzkbz;
	}
	public BigDecimal getTdkbz() {
		return tdkbz;
	}
	public void setTdkbz(BigDecimal tdkbz) {
		this.tdkbz = tdkbz;
	}
	public BigDecimal getCjlbz() {
		return cjlbz;
	}
	public void setCjlbz(BigDecimal cjlbz) {
		this.cjlbz = cjlbz;
	}
	public BigDecimal getBzdm() {
		return bzdm;
	}
	public void setBzdm(BigDecimal bzdm) {
		this.bzdm = bzdm;
	}
	public String getScdw() {
		return scdw;
	}
	public void setScdw(String scdw) {
		this.scdw = scdw;
	}
	public String getCwlr() {
		return cwlr;
	}
	public void setCwlr(String cwlr) {
		this.cwlr = cwlr;
	}
	public String getSm() {
		return sm;
	}
	public void setSm(String sm) {
		this.sm = sm;
	}
	public String getXmnh() {
		return xmnh;
	}
	public void setXmnh(String xmnh) {
		this.xmnh = xmnh;
	}
	public String getNationCode() {
		return nationCode;
	}
	public void setNationCode(String nationCode) {
		this.nationCode = nationCode;
	}
	public String getYbzfbz() {
		return ybzfbz;
	}
	public void setYbzfbz(String ybzfbz) {
		this.ybzfbz = ybzfbz;
	}
	public String getYjfl() {
		return yjfl;
	}
	public void setYjfl(String yjfl) {
		this.yjfl = yjfl;
	}
	public String getEjfl() {
		return ejfl;
	}
	public void setEjfl(String ejfl) {
		this.ejfl = ejfl;
	}
	public String getSjfl() {
		return sjfl;
	}
	public void setSjfl(String sjfl) {
		this.sjfl = sjfl;
	}
	public String getCz() {
		return cz;
	}
	public void setCz(String cz) {
		this.cz = cz;
	}
	public String getTz() {
		return tz;
	}
	public void setTz(String tz) {
		this.tz = tz;
	}
	public String getDjcpmc() {
		return djcpmc;
	}
	public void setDjcpmc(String djcpmc) {
		this.djcpmc = djcpmc;
	}
	public String getGgxhs() {
		return ggxhs;
	}
	public void setGgxhs(String ggxhs) {
		this.ggxhs = ggxhs;
	}
	public String getBzcz() {
		return bzcz;
	}
	public void setBzcz(String bzcz) {
		this.bzcz = bzcz;
	}
	public String getZxbzsl() {
		return zxbzsl;
	}
	public void setZxbzsl(String zxbzsl) {
		this.zxbzsl = zxbzsl;
	}
	public String getZxbzdw() {
		return zxbzdw;
	}
	public void setZxbzdw(String zxbzdw) {
		this.zxbzdw = zxbzdw;
	}
	public String getZxzjdw() {
		return zxzjdw;
	}
	public void setZxzjdw(String zxzjdw) {
		this.zxzjdw = zxzjdw;
	}
    
}