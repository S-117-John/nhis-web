package com.zebone.nhis.compay.ins.lb.vo.lxyb;

import java.util.Date;

import com.zebone.nhis.common.module.BaseModule;
import com.zebone.platform.modules.dao.build.au.Field;
import com.zebone.platform.modules.dao.build.au.PK;
import com.zebone.platform.modules.dao.build.au.Table;
import com.zebone.platform.modules.dao.build.au.Field.KeyId;


/**
 * Table: INS_SZLX_JS 
 *
 * @since 2018-06-21 12:02:06
 */
@Table(value="INS_SZLX_JS")
public class InsSzlxJs extends BaseModule  {

    /** ID - 主键 */
	@PK
	@Field(value="ID",id=KeyId.UUID)
    private String id;

    /** ICKH - ICKH-IC卡号 */
	@Field(value="ICKH")
    private String ickh;

    /** FYLB - FYLB-费用类别 */
	@Field(value="FYLB")
    private String fylb;

    /** YLFWJGBM - YLFWJGBM-医疗服务机构编码 */
	@Field(value="YLFWJGBM")
    private String ylfwjgbm;

    /** DJDM - DJDM-登记代码 */
	@Field(value="DJDM")
    private String djdm;

    /** JSCLLX - JSCLLX-结算处理类型 */
	@Field(value="JSCLLX")
    private String jscllx;

    /** YWDM - YWDM-业务代码 */
	@Field(value="YWDM")
    private String ywdm;

    /** SFSYGRZH - SFSYGRZH-是否使用个人帐户 */
	@Field(value="SFSYGRZH")
    private String sfsygrzh;

    /** CZY - CZY-操作员 */
	@Field(value="CZY")
    private String czy;

    /** YYDJZZFY - YYDJZZFY-医院端结帐总费用 */
	@Field(value="YYDJZZFY")
    private String yydjzzfy;

    /** BZ - BZ-备注 */
	@Field(value="BZ")
    private String bz;

    /** JSPJH - JSPJH-结算票据号 */
	@Field(value="JSPJH")
    private String jspjh;

    /** CWQJ - CWQJ-财务期间 */
	@Field(value="CWQJ")
    private String cwqj;

    /** BCQZHYE - BCQZHYE-本次前帐户余额 */
	@Field(value="BCQZHYE")
    private String bcqzhye;

    /** STZYCS - STZYCS-视同住院次数 */
	@Field(value="STZYCS")
    private String stzycs;

    /** ZYCS - ZYCS-住院次数 */
	@Field(value="ZYCS")
    private String zycs;

    /** ZFY - ZFY-总费用 */
	@Field(value="ZFY")
    private String zfy;

    /** FWNFY - FWNFY-范围内费用 */
	@Field(value="FWNFY")
    private String fwnfy;

    /** CBZFY - CBZFY-超标准费用 */
	@Field(value="CBZFY")
    private String cbzfy;

    /** ZFEIFY - ZFFY-自费费用 */
	@Field(value="ZFEIFY")
    private String zfeify;

    /** ZFFY - ZFFY-自付费用 */
	@Field(value="ZFFY")
    private String zffy;

    /** JLYZFY - JLYZFY-甲类药总费用 */
	@Field(value="JLYZFY")
    private String jlyzfy;

    /** YLYZFY - YLYZFY-乙类药总费用 */
	@Field(value="YLYZFY")
    private String ylyzfy;

    /** YLYZFFY - YLYZFFY-乙类药自付费用 */
	@Field(value="YLYZFFY")
    private String ylyzffy;

    /** ZFYZFY - ZFYZFY-自费药总费用 */
	@Field(value="ZFYZFY")
    private String zfyzfy;

    /** JLXMZFY - JLXMZFY-甲类项目总费用 */
	@Field(value="JLXMZFY")
    private String jlxmzfy;

    /** YLXMZFY - YLXMZFY-乙类项目总费用 */
	@Field(value="YLXMZFY")
    private String ylxmzfy;

    /** YLXMZFFY - YLXMZFFY-乙类项目自付费用 */
	@Field(value="YLXMZFFY")
    private String ylxmzffy;

    /** ZFXMZFY - ZFXMZFY-自费项目总费用 */
	@Field(value="ZFXMZFY")
    private String zfxmzfy;

    /** JBYLJJZF - JBYLJJZF-基本医疗基金支付 */
	@Field(value="JBYLJJZF")
    private String jbyljjzf;

    /** JBYLGRZF - JBYLGRZF-基本医疗个人支付 */
	@Field(value="JBYLGRZF")
    private String jbylgrzf;

    /** YLJZJJZF - YLJZJJZF-医疗救助基金支付 */
	@Field(value="YLJZJJZF")
    private String yljzjjzf;

    /** YLJZJJGRZF - YLJZJJGRZF-医疗救助基金个人支付 */
	@Field(value="YLJZJJGRZF")
    private String yljzjjgrzf;

    /** QYBCBXJJZF - QYBCBXJJZF-企业补充保险基金支付 */
	@Field(value="QYBCBXJJZF")
    private String qybcbxjjzf;

    /** QYBCBXGRZF - QYBCBXGRZF-企业补充保险个人支付 */
	@Field(value="QYBCBXGRZF")
    private String qybcbxgrzf;

    /** GWYBCJJZF - GWYBCJJZF-公务员补充基金支付 */
	@Field(value="GWYBCJJZF")
    private String gwybcjjzf;

    /** GWYBCGRZF - GWYBCGRZF-公务员补充个人支付 */
	@Field(value="GWYBCGRZF")
    private String gwybcgrzf;

    /** GRZHZF - GRZHZF-个人帐户支付 */
	@Field(value="GRZHZF")
    private String grzhzf;

    /** XJZF - XJZF-现金支付 */
	@Field(value="XJZF")
    private String xjzf;

    /** TJTLXMZFY - TJTLXMZFY-特检特疗项目总费用 */
	@Field(value="TJTLXMZFY")
    private String tjtlxmzfy;

    /** TJTLXMZFFY - TJTLXMZFFY-特检特疗项目自付费用 */
	@Field(value="TJTLXMZFFY")
    private String tjtlxmzffy;

    /** BCQFBZ - BCQFBZ-本次起付标准 */
	@Field(value="BCQFBZ")
    private String bcqfbz;

    /** CGDBJZFDFY - CGDBJZFDFY-超过大病救助封顶费用 */
	@Field(value="CGDBJZFDFY")
    private String cgdbjzfdfy;

    /** DYDJJZF - DYDJJZF-第一段基金支付 */
	@Field(value="DYDJJZF")
    private String dydjjzf;

    /** DYDGRZF - DYDGRZF-第一段个人支付 */
	@Field(value="DYDGRZF")
    private String dydgrzf;

    /** DEDJJZF - DEDJJZF-第二段基金支付 */
	@Field(value="DEDJJZF")
    private String dedjjzf;

    /** DEDGRZF - DEDGRZF-第二段个人支付 */
	@Field(value="DEDGRZF")
    private String dedgrzf;

    /** DSDJJZF - DSDJJZF-第三段基金支付 */
	@Field(value="DSDJJZF")
    private String dsdjjzf;

    /** DSDGRZF - DSDGRZF-第三段个人支付 */
	@Field(value="DSDGRZF")
    private String dsdgrzf;

    /** DSIDJJZF - DSDJJZF-第四段基金支付 */
	@Field(value="DSIDJJZF")
    private String dsidjjzf;

    /** DSIDGRZF - DSDGRZF-第四段个人支付 */
	@Field(value="DSIDGRZF")
    private String dsidgrzf;

    /** DWDJJZF - DWDJJZF-第五段基金支付 */
	@Field(value="DWDJJZF")
    private String dwdjjzf;

    /** DWDGRZF - DWDGRZF-第五段个人支付 */
	@Field(value="DWDGRZF")
    private String dwdgrzf;

    /** DLDJJZF - DLDJJZF-第六段基金支付 */
	@Field(value="DLDJJZF")
    private String dldjjzf;

    /** DLDGRZF - DLDGRZF-第六段个人支付 */
	@Field(value="DLDGRZF")
    private String dldgrzf;

    /** DQDJJZF - DQDJJZF-第七段基金支付 */
	@Field(value="DQDJJZF")
    private String dqdjjzf;

    /** DQDGRZF - DQDGRZF-第七段个人支付 */
	@Field(value="DQDGRZF")
    private String dqdgrzf;

    /** DBDJJZF - DBDJJZF-第八段基金支付 */
	@Field(value="DBDJJZF")
    private String dbdjjzf;

    /** DBDGRZF - DBDGRZF-第八段个人支付 */
	@Field(value="DBDGRZF")
    private String dbdgrzf;

    /** DEBZJ - DEBZJ-定额补助金 */
	@Field(value="DEBZJ")
    private String debzj;

    /** BCQFWNFYLJ - BCQFWNFYLJ-本次前范围内费用累计 */
	@Field(value="BCQFWNFYLJ")
    private String bcqfwnfylj;

    /** JBYLJJFWNLJ - JBYLJJFWNLJ(BHBC)-基本医疗基金范围内累计(不含本次) */
	@Field(value="JBYLJJFWNLJ")
    private String jbyljjfwnlj;

    /** DBJZJJFWNLJ - DBJZJJFWNLJ(BHBC)-大病救助基金范围内累计(不含本次) */
	@Field(value="DBJZJJFWNLJ")
    private String dbjzjjfwnlj;

    /** DQYWJJZFLJ - DQYWJJZFLJ(HBC)-当前业务基金支付累计(含本次) */
	@Field(value="DQYWJJZFLJ")
    private String dqywjjzflj;

    /** LXJJDBDYDZF - LXJJDBDYDZF(DWZF_-离休基金大病第一段支付(单位支付） */
	@Field(value="LXJJDBDYDZF")
    private String lxjjdbdydzf;

    /** LXJJBXGSDYDZF - LXJJBXGSDYDZF-离休基金保险公司第一段支付 */
	@Field(value="LXJJBXGSDYDZF")
    private String lxjjbxgsdydzf;

    /** LXJJDBDEDZF - LXJJDBDEDZF_DWZF_-离休基金大病第二段支付（单位支付） */
	@Field(value="LXJJDBDEDZF")
    private String lxjjdbdedzf;

    /** LXBXGSDEDZF - LXBXGSDEDZF-离休保险公司第二段支付 */
	@Field(value="LXBXGSDEDZF")
    private String lxbxgsdedzf;

    /** LXJJDBDSDZF - LXJJDBDSDZF_DWZF_-离休基金大病第三段支付（单位支付） */
	@Field(value="LXJJDBDSDZF")
    private String lxjjdbdsdzf;

    /** LXBXGSDSDZF - LXBXGSDSDZF-离休保险公司第三段支付 */
	@Field(value="LXBXGSDSDZF")
    private String lxbxgsdsdzf;

    /** LXJJDBDSIDZF - LXJJDBDSIDZF_DWZF_-离休基金大病第四段支付（单位支付） */
	@Field(value="LXJJDBDSIDZF")
    private String lxjjdbdsidzf;

    /** LXBXGSDSIDZF - LXBXGSDSDZF-离休保险公司第四段支付 */
	@Field(value="LXBXGSDSIDZF")
    private String lxbxgsdsidzf;

    /** BXGSZF - BXGSZF(HZ)-保险公司支付(汇总) */
	@Field(value="BXGSZF")
    private String bxgszf;

    /** LXDBDWZF - LXDBDWZF(HZ)-离休大病单位支付(汇总) */
	@Field(value="LXDBDWZF")
    private String lxdbdwzf;

    /** LXJBYLDWZF - LXJBYLDWZF(HZ)-离休基本医疗单位支付(汇总) */
	@Field(value="LXJBYLDWZF")
    private String lxjbyldwzf;

    /** MODIFY_TIME - 最后操作时间 */
	@Field(value="MODIFY_TIME")
    private Date modifyTime;

	@Field(value="YWLX")
    private String ywlx;

	@Field(value="PK_PV")
    private String pkPv;

	@Field(value="PK_SETTLE")
    private String pkSettle;


    public String getId(){
        return this.id;
    }
    public void setId(String id){
        this.id = id;
    }

    public String getIckh(){
        return this.ickh;
    }
    public void setIckh(String ickh){
        this.ickh = ickh;
    }

    public String getFylb(){
        return this.fylb;
    }
    public void setFylb(String fylb){
        this.fylb = fylb;
    }

    public String getYlfwjgbm(){
        return this.ylfwjgbm;
    }
    public void setYlfwjgbm(String ylfwjgbm){
        this.ylfwjgbm = ylfwjgbm;
    }

    public String getDjdm(){
        return this.djdm;
    }
    public void setDjdm(String djdm){
        this.djdm = djdm;
    }

    public String getJscllx(){
        return this.jscllx;
    }
    public void setJscllx(String jscllx){
        this.jscllx = jscllx;
    }

    public String getYwdm(){
        return this.ywdm;
    }
    public void setYwdm(String ywdm){
        this.ywdm = ywdm;
    }

    public String getSfsygrzh(){
        return this.sfsygrzh;
    }
    public void setSfsygrzh(String sfsygrzh){
        this.sfsygrzh = sfsygrzh;
    }

    public String getCzy(){
        return this.czy;
    }
    public void setCzy(String czy){
        this.czy = czy;
    }

    public String getYydjzzfy(){
        return this.yydjzzfy;
    }
    public void setYydjzzfy(String yydjzzfy){
        this.yydjzzfy = yydjzzfy;
    }

    public String getBz(){
        return this.bz;
    }
    public void setBz(String bz){
        this.bz = bz;
    }

    public String getJspjh(){
        return this.jspjh;
    }
    public void setJspjh(String jspjh){
        this.jspjh = jspjh;
    }

    public String getCwqj(){
        return this.cwqj;
    }
    public void setCwqj(String cwqj){
        this.cwqj = cwqj;
    }

    public String getBcqzhye(){
        return this.bcqzhye;
    }
    public void setBcqzhye(String bcqzhye){
        this.bcqzhye = bcqzhye;
    }

    public String getStzycs(){
        return this.stzycs;
    }
    public void setStzycs(String stzycs){
        this.stzycs = stzycs;
    }

    public String getZycs(){
        return this.zycs;
    }
    public void setZycs(String zycs){
        this.zycs = zycs;
    }

    public String getZfy(){
        return this.zfy;
    }
    public void setZfy(String zfy){
        this.zfy = zfy;
    }

    public String getFwnfy(){
        return this.fwnfy;
    }
    public void setFwnfy(String fwnfy){
        this.fwnfy = fwnfy;
    }

    public String getCbzfy(){
        return this.cbzfy;
    }
    public void setCbzfy(String cbzfy){
        this.cbzfy = cbzfy;
    }

    public String getZfeify(){
        return this.zfeify;
    }
    public void setZfeify(String zfeify){
        this.zfeify = zfeify;
    }

    public String getZffy(){
        return this.zffy;
    }
    public void setZffy(String zffy){
        this.zffy = zffy;
    }

    public String getJlyzfy(){
        return this.jlyzfy;
    }
    public void setJlyzfy(String jlyzfy){
        this.jlyzfy = jlyzfy;
    }

    public String getYlyzfy(){
        return this.ylyzfy;
    }
    public void setYlyzfy(String ylyzfy){
        this.ylyzfy = ylyzfy;
    }

    public String getYlyzffy(){
        return this.ylyzffy;
    }
    public void setYlyzffy(String ylyzffy){
        this.ylyzffy = ylyzffy;
    }

    public String getZfyzfy(){
        return this.zfyzfy;
    }
    public void setZfyzfy(String zfyzfy){
        this.zfyzfy = zfyzfy;
    }

    public String getJlxmzfy(){
        return this.jlxmzfy;
    }
    public void setJlxmzfy(String jlxmzfy){
        this.jlxmzfy = jlxmzfy;
    }

    public String getYlxmzfy(){
        return this.ylxmzfy;
    }
    public void setYlxmzfy(String ylxmzfy){
        this.ylxmzfy = ylxmzfy;
    }

    public String getYlxmzffy(){
        return this.ylxmzffy;
    }
    public void setYlxmzffy(String ylxmzffy){
        this.ylxmzffy = ylxmzffy;
    }

    public String getZfxmzfy(){
        return this.zfxmzfy;
    }
    public void setZfxmzfy(String zfxmzfy){
        this.zfxmzfy = zfxmzfy;
    }

    public String getJbyljjzf(){
        return this.jbyljjzf;
    }
    public void setJbyljjzf(String jbyljjzf){
        this.jbyljjzf = jbyljjzf;
    }

    public String getJbylgrzf(){
        return this.jbylgrzf;
    }
    public void setJbylgrzf(String jbylgrzf){
        this.jbylgrzf = jbylgrzf;
    }

    public String getYljzjjzf(){
        return this.yljzjjzf;
    }
    public void setYljzjjzf(String yljzjjzf){
        this.yljzjjzf = yljzjjzf;
    }

    public String getYljzjjgrzf(){
        return this.yljzjjgrzf;
    }
    public void setYljzjjgrzf(String yljzjjgrzf){
        this.yljzjjgrzf = yljzjjgrzf;
    }

    public String getQybcbxjjzf(){
        return this.qybcbxjjzf;
    }
    public void setQybcbxjjzf(String qybcbxjjzf){
        this.qybcbxjjzf = qybcbxjjzf;
    }

    public String getQybcbxgrzf(){
        return this.qybcbxgrzf;
    }
    public void setQybcbxgrzf(String qybcbxgrzf){
        this.qybcbxgrzf = qybcbxgrzf;
    }

    public String getGwybcjjzf(){
        return this.gwybcjjzf;
    }
    public void setGwybcjjzf(String gwybcjjzf){
        this.gwybcjjzf = gwybcjjzf;
    }

    public String getGwybcgrzf(){
        return this.gwybcgrzf;
    }
    public void setGwybcgrzf(String gwybcgrzf){
        this.gwybcgrzf = gwybcgrzf;
    }

    public String getGrzhzf(){
        return this.grzhzf;
    }
    public void setGrzhzf(String grzhzf){
        this.grzhzf = grzhzf;
    }

    public String getXjzf(){
        return this.xjzf;
    }
    public void setXjzf(String xjzf){
        this.xjzf = xjzf;
    }

    public String getTjtlxmzfy(){
        return this.tjtlxmzfy;
    }
    public void setTjtlxmzfy(String tjtlxmzfy){
        this.tjtlxmzfy = tjtlxmzfy;
    }

    public String getTjtlxmzffy(){
        return this.tjtlxmzffy;
    }
    public void setTjtlxmzffy(String tjtlxmzffy){
        this.tjtlxmzffy = tjtlxmzffy;
    }

    public String getBcqfbz(){
        return this.bcqfbz;
    }
    public void setBcqfbz(String bcqfbz){
        this.bcqfbz = bcqfbz;
    }

    public String getCgdbjzfdfy(){
        return this.cgdbjzfdfy;
    }
    public void setCgdbjzfdfy(String cgdbjzfdfy){
        this.cgdbjzfdfy = cgdbjzfdfy;
    }

    public String getDydjjzf(){
        return this.dydjjzf;
    }
    public void setDydjjzf(String dydjjzf){
        this.dydjjzf = dydjjzf;
    }

    public String getDydgrzf(){
        return this.dydgrzf;
    }
    public void setDydgrzf(String dydgrzf){
        this.dydgrzf = dydgrzf;
    }

    public String getDedjjzf(){
        return this.dedjjzf;
    }
    public void setDedjjzf(String dedjjzf){
        this.dedjjzf = dedjjzf;
    }

    public String getDedgrzf(){
        return this.dedgrzf;
    }
    public void setDedgrzf(String dedgrzf){
        this.dedgrzf = dedgrzf;
    }

    public String getDsdjjzf(){
        return this.dsdjjzf;
    }
    public void setDsdjjzf(String dsdjjzf){
        this.dsdjjzf = dsdjjzf;
    }

    public String getDsdgrzf(){
        return this.dsdgrzf;
    }
    public void setDsdgrzf(String dsdgrzf){
        this.dsdgrzf = dsdgrzf;
    }

    public String getDsidjjzf(){
        return this.dsidjjzf;
    }
    public void setDsidjjzf(String dsidjjzf){
        this.dsidjjzf = dsidjjzf;
    }

    public String getDsidgrzf(){
        return this.dsidgrzf;
    }
    public void setDsidgrzf(String dsidgrzf){
        this.dsidgrzf = dsidgrzf;
    }

    public String getDwdjjzf(){
        return this.dwdjjzf;
    }
    public void setDwdjjzf(String dwdjjzf){
        this.dwdjjzf = dwdjjzf;
    }

    public String getDwdgrzf(){
        return this.dwdgrzf;
    }
    public void setDwdgrzf(String dwdgrzf){
        this.dwdgrzf = dwdgrzf;
    }

    public String getDldjjzf(){
        return this.dldjjzf;
    }
    public void setDldjjzf(String dldjjzf){
        this.dldjjzf = dldjjzf;
    }

    public String getDldgrzf(){
        return this.dldgrzf;
    }
    public void setDldgrzf(String dldgrzf){
        this.dldgrzf = dldgrzf;
    }

    public String getDqdjjzf(){
        return this.dqdjjzf;
    }
    public void setDqdjjzf(String dqdjjzf){
        this.dqdjjzf = dqdjjzf;
    }

    public String getDqdgrzf(){
        return this.dqdgrzf;
    }
    public void setDqdgrzf(String dqdgrzf){
        this.dqdgrzf = dqdgrzf;
    }

    public String getDbdjjzf(){
        return this.dbdjjzf;
    }
    public void setDbdjjzf(String dbdjjzf){
        this.dbdjjzf = dbdjjzf;
    }

    public String getDbdgrzf(){
        return this.dbdgrzf;
    }
    public void setDbdgrzf(String dbdgrzf){
        this.dbdgrzf = dbdgrzf;
    }

    public String getDebzj(){
        return this.debzj;
    }
    public void setDebzj(String debzj){
        this.debzj = debzj;
    }

    public String getBcqfwnfylj(){
        return this.bcqfwnfylj;
    }
    public void setBcqfwnfylj(String bcqfwnfylj){
        this.bcqfwnfylj = bcqfwnfylj;
    }

    public String getJbyljjfwnlj(){
        return this.jbyljjfwnlj;
    }
    public void setJbyljjfwnlj(String jbyljjfwnlj){
        this.jbyljjfwnlj = jbyljjfwnlj;
    }

    public String getDbjzjjfwnlj(){
        return this.dbjzjjfwnlj;
    }
    public void setDbjzjjfwnlj(String dbjzjjfwnlj){
        this.dbjzjjfwnlj = dbjzjjfwnlj;
    }

    public String getDqywjjzflj(){
        return this.dqywjjzflj;
    }
    public void setDqywjjzflj(String dqywjjzflj){
        this.dqywjjzflj = dqywjjzflj;
    }

    public String getLxjjdbdydzf(){
        return this.lxjjdbdydzf;
    }
    public void setLxjjdbdydzf(String lxjjdbdydzf){
        this.lxjjdbdydzf = lxjjdbdydzf;
    }

    public String getLxjjbxgsdydzf(){
        return this.lxjjbxgsdydzf;
    }
    public void setLxjjbxgsdydzf(String lxjjbxgsdydzf){
        this.lxjjbxgsdydzf = lxjjbxgsdydzf;
    }

    public String getLxjjdbdedzf(){
        return this.lxjjdbdedzf;
    }
    public void setLxjjdbdedzf(String lxjjdbdedzf){
        this.lxjjdbdedzf = lxjjdbdedzf;
    }

    public String getLxbxgsdedzf(){
        return this.lxbxgsdedzf;
    }
    public void setLxbxgsdedzf(String lxbxgsdedzf){
        this.lxbxgsdedzf = lxbxgsdedzf;
    }

    public String getLxjjdbdsdzf(){
        return this.lxjjdbdsdzf;
    }
    public void setLxjjdbdsdzf(String lxjjdbdsdzf){
        this.lxjjdbdsdzf = lxjjdbdsdzf;
    }

    public String getLxbxgsdsdzf(){
        return this.lxbxgsdsdzf;
    }
    public void setLxbxgsdsdzf(String lxbxgsdsdzf){
        this.lxbxgsdsdzf = lxbxgsdsdzf;
    }

    public String getLxjjdbdsidzf(){
        return this.lxjjdbdsidzf;
    }
    public void setLxjjdbdsidzf(String lxjjdbdsidzf){
        this.lxjjdbdsidzf = lxjjdbdsidzf;
    }

    public String getLxbxgsdsidzf(){
        return this.lxbxgsdsidzf;
    }
    public void setLxbxgsdsidzf(String lxbxgsdsidzf){
        this.lxbxgsdsidzf = lxbxgsdsidzf;
    }

    public String getBxgszf(){
        return this.bxgszf;
    }
    public void setBxgszf(String bxgszf){
        this.bxgszf = bxgszf;
    }

    public String getLxdbdwzf(){
        return this.lxdbdwzf;
    }
    public void setLxdbdwzf(String lxdbdwzf){
        this.lxdbdwzf = lxdbdwzf;
    }

    public String getLxjbyldwzf(){
        return this.lxjbyldwzf;
    }
    public void setLxjbyldwzf(String lxjbyldwzf){
        this.lxjbyldwzf = lxjbyldwzf;
    }

    public Date getModifyTime(){
        return this.modifyTime;
    }
    public void setModifyTime(Date modifyTime){
        this.modifyTime = modifyTime;
    }

    public String getYwlx(){
        return this.ywlx;
    }
    public void setYwlx(String ywlx){
        this.ywlx = ywlx;
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
}