package com.zebone.nhis.pro.zsba.compay.ins.zsyb.vo;

import net.sf.json.JSONObject;

import com.zebone.nhis.pro.zsba.common.support.StringTools;


/**
 * 医保三大目录对照数据
 * @author zim
 *
 */
public class InsItemData {

	private String XMBH;//项目编号
	private String XMLB;//项目类别
	private String ZWMC;//	中文名称
	private String YWMC;//	英文名称
	private String FLDM;//	分类代码
	private String YPGG;//	规格
	private String YPJX;//	剂型
	private String ZJM;//	助记码
	private String YWMLDJ;//	药物目录等级
	private String JBZFBL;//	基本医疗自费比例
	private String SQZFBL;//	社区自费比例
	private String LXZFBL;//	离休自费比例
	private String GSZFBL;//	工伤自费比例
	private String SYZFBL;//	生育自费比例
	private String TMZFBL;//	特定门诊自费比例
	private String BCZFBL;//	补充医疗自费比例
	private String JZJG;//	基准价格
	private String XJJE;//	限价金额
	private String LXXJJE;//	离休限价金额
	private String JZKBZ;//	基准库标志
	private String XZKBZ;//	限制库标志
	private String TDKBZ;//	特定库标志
	private String CJKBZ;//	产检库标志
	private String BZDM;//	病种代码
	private String YJJYPBM;//	药监局药品编码
	private String YYCLZCZMC;//	医用材料的注册证产品名称
	private String YYCLSYJZCH;//	医用材料的食药监注册号
	private String TXBZ;//	特项标志
	private String KSSJ;//	开始时间
	private String ZSSJ;//	终止时间
	private String BZ;//		备注
	
	public InsItemData(){}
	
	public InsItemData(JSONObject obj){
		this.XMBH = obj.getString(StringTools.toLowerCaseFirstOne("XMBH"));//项目编号
		this.XMLB = obj.getString(StringTools.toLowerCaseFirstOne("XMLB"));//项目类别
		this.ZWMC = obj.getString(StringTools.toLowerCaseFirstOne("ZWMC"));//	中文名称
		this.YWMC = obj.getString(StringTools.toLowerCaseFirstOne("YWMC"));//	英文名称
		this.FLDM = obj.getString(StringTools.toLowerCaseFirstOne("FLDM"));//	分类代码
		this.YPGG = obj.getString(StringTools.toLowerCaseFirstOne("YPGG"));//	规格
		this.YPJX = obj.getString(StringTools.toLowerCaseFirstOne("YPJX"));//	剂型
		this.ZJM = obj.getString(StringTools.toLowerCaseFirstOne("ZJM"));//	助记码
		this.YWMLDJ = obj.getString(StringTools.toLowerCaseFirstOne("YWMLDJ"));//	药物目录等级
		this.JBZFBL = obj.getString(StringTools.toLowerCaseFirstOne("JBZFBL"));//	基本医疗自费比例
		this.SQZFBL = obj.getString(StringTools.toLowerCaseFirstOne("SQZFBL"));//	社区自费比例
		this.LXZFBL = obj.getString(StringTools.toLowerCaseFirstOne("LXZFBL"));//	离休自费比例
		this.GSZFBL = obj.getString(StringTools.toLowerCaseFirstOne("GSZFBL"));//	工伤自费比例
		this.SYZFBL = obj.getString(StringTools.toLowerCaseFirstOne("SYZFBL"));//	生育自费比例
		this.TMZFBL = obj.getString(StringTools.toLowerCaseFirstOne("TMZFBL"));//	特定门诊自费比例
		this.BCZFBL = obj.getString(StringTools.toLowerCaseFirstOne("BCZFBL"));//	补充医疗自费比例
		this.JZJG = obj.getString(StringTools.toLowerCaseFirstOne("JZJG"));//	基准价格
		this.XJJE = obj.getString(StringTools.toLowerCaseFirstOne("XJJE"));//	限价金额
		this.LXXJJE = obj.getString(StringTools.toLowerCaseFirstOne("LXXJJE"));//	离休限价金额
		this.JZKBZ = obj.getString(StringTools.toLowerCaseFirstOne("JZKBZ"));//	基准库标志
		this.XZKBZ = obj.getString(StringTools.toLowerCaseFirstOne("XZKBZ"));//	限制库标志
		this.TDKBZ = obj.getString(StringTools.toLowerCaseFirstOne("TDKBZ"));//	特定库标志
		this.CJKBZ = obj.getString(StringTools.toLowerCaseFirstOne("CJKBZ"));//产检库标志
		this.BZDM = obj.getString(StringTools.toLowerCaseFirstOne("BZDM"));//	病种代码
		this.YJJYPBM = obj.getString(StringTools.toLowerCaseFirstOne("YJJYPBM"));//	药监局药品编码
		this.YYCLZCZMC = obj.getString(StringTools.toLowerCaseFirstOne("YYCLZCZMC"));//	医用材料的注册证产品名称
		this.YYCLSYJZCH = obj.getString(StringTools.toLowerCaseFirstOne("YYCLSYJZCH"));//	医用材料的食药监注册号
		this.TXBZ = obj.getString(StringTools.toLowerCaseFirstOne("TXBZ"));//	特项标志
		this.KSSJ = obj.getString(StringTools.toLowerCaseFirstOne("KSSJ"));//	开始时间
		this.ZSSJ = obj.getString(StringTools.toLowerCaseFirstOne("ZSSJ"));//	终止时间
		this.BZ = obj.getString(StringTools.toLowerCaseFirstOne("BZ"));//		备注
	}
	
	public String getXMBH() {
		return XMBH;
	}
	public void setXMBH(String xMBH) {
		XMBH = xMBH;
	}
	public String getXMLB() {
		return XMLB;
	}
	public void setXMLB(String xMLB) {
		XMLB = xMLB;
	}
	public String getZWMC() {
		return ZWMC;
	}
	public void setZWMC(String zWMC) {
		ZWMC = zWMC;
	}
	public String getYWMC() {
		return YWMC;
	}
	public void setYWMC(String yWMC) {
		YWMC = yWMC;
	}
	public String getFLDM() {
		return FLDM;
	}
	public void setFLDM(String fLDM) {
		FLDM = fLDM;
	}
	public String getYPGG() {
		return YPGG;
	}
	public void setYPGG(String yPGG) {
		YPGG = yPGG;
	}
	public String getYPJX() {
		return YPJX;
	}
	public void setYPJX(String yPJX) {
		YPJX = yPJX;
	}
	public String getZJM() {
		return ZJM;
	}
	public void setZJM(String zJM) {
		ZJM = zJM;
	}
	public String getYWMLDJ() {
		return YWMLDJ;
	}
	public void setYWMLDJ(String yWMLDJ) {
		YWMLDJ = yWMLDJ;
	}
	public String getJBZFBL() {
		return JBZFBL;
	}
	public void setJBZFBL(String jBZFBL) {
		JBZFBL = jBZFBL;
	}
	public String getSQZFBL() {
		return SQZFBL;
	}
	public void setSQZFBL(String sQZFBL) {
		SQZFBL = sQZFBL;
	}
	public String getLXZFBL() {
		return LXZFBL;
	}
	public void setLXZFBL(String lXZFBL) {
		LXZFBL = lXZFBL;
	}
	public String getGSZFBL() {
		return GSZFBL;
	}
	public void setGSZFBL(String gSZFBL) {
		GSZFBL = gSZFBL;
	}
	public String getSYZFBL() {
		return SYZFBL;
	}
	public void setSYZFBL(String sYZFBL) {
		SYZFBL = sYZFBL;
	}
	public String getTMZFBL() {
		return TMZFBL;
	}
	public void setTMZFBL(String tMZFBL) {
		TMZFBL = tMZFBL;
	}
	public String getBCZFBL() {
		return BCZFBL;
	}
	public void setBCZFBL(String bCZFBL) {
		BCZFBL = bCZFBL;
	}
	public String getJZJG() {
		return JZJG;
	}
	public void setJZJG(String jZJG) {
		JZJG = jZJG;
	}
	public String getYJJYPBM() {
		return YJJYPBM;
	}
	public void setYJJYPBM(String yJJYPBM) {
		YJJYPBM = yJJYPBM;
	}
	public String getYYCLZCZMC() {
		return YYCLZCZMC;
	}
	public void setYYCLZCZMC(String yYCLZCZMC) {
		YYCLZCZMC = yYCLZCZMC;
	}
	public String getYYCLSYJZCH() {
		return YYCLSYJZCH;
	}
	public void setYYCLSYJZCH(String yYCLSYJZCH) {
		YYCLSYJZCH = yYCLSYJZCH;
	}
	public String getTXBZ() {
		return TXBZ;
	}
	public void setTXBZ(String tXBZ) {
		TXBZ = tXBZ;
	}
	public String getKSSJ() {
		return KSSJ;
	}
	public void setKSSJ(String kSSJ) {
		KSSJ = kSSJ;
	}
	public String getZSSJ() {
		return ZSSJ;
	}
	public void setZSSJ(String zSSJ) {
		ZSSJ = zSSJ;
	}
	public String getBZ() {
		return BZ;
	}
	public void setBZ(String bZ) {
		BZ = bZ;
	}
}
