package com.zebone.nhis.common.module.compay.ins.xa.xayb;

import java.util.Date;

import com.zebone.nhis.common.module.BaseModule;
import com.zebone.platform.modules.dao.build.au.Field;
import com.zebone.platform.modules.dao.build.au.PK;
import com.zebone.platform.modules.dao.build.au.Table;
import com.zebone.platform.modules.dao.build.au.Field.FieldType;
import com.zebone.platform.modules.dao.build.au.Field.KeyId;


/**
 * Table: INS_XAYB_PTMZJS 西安医保预结算结果
 * @since 2017-11-06 14:11:10
 */
@Table(value="INS_XAYB_PTMZJS")
public class InsXaybPtmzjs extends BaseModule{

	/**
	 * 序列号
	 */
	private static final long serialVersionUID = -3054084407041211493L;

	@PK
	@Field(value="PK_PTMZJS",id=KeyId.UUID)
    private String pkPtmzjs;

	@Field(value="JYLSH")
    private String jYLSH; //交易流水号

	@Field(value="JYYZM")
    private String jYYZM; //交易验证码

	@Field(value="YWLX")
    private String yWLX; // 业务类型

	@Field(value="PK_PV")
    private String pkPv; // HIS挂号主键

	@Field(value="PK_SETTLE")
    private String pkSettle; //HIS结算主键

	@Field(value="JZBH")
    private String jZBH; //就诊编号
	
	@Field(value="FZXBH")
	private String fZXBH; //分中心编号
	
	@Field(value="JSBH")
	private String jSBH; // 结算编号
	
	@Field(value="ZFLB")
	private String zFLB; //支付类别
	
	@Field(value="GRBH")
	private String gRBH; //个人编号
	
	@Field(value="GRZHZFBF")
	private double gRZHZFBF; // 个人帐户支付部分

	@Field(value="JBSJ")
	private String jBSJ; //经办时间

	@Field(value="FYZE")
	private double fYZE; // 费用总额
	
	@Field(value="QZFBF")
	private double qZFBF; //全自费部分
	
	@Field(value="XXZF")
	public String xXZF; //先行自付
	
	@Field(value="FHFW")
	private double fHFW; //符合范围
	
	@Field(value="TJTZXMFY")
	private double tJTZXMFY; // 特检特治项目费用
	
	@Field(value="TSHCFY3")
	private double tSHCFY3; //特殊耗材费用3:7
	
	@Field(value="TJTZXM5FY")
	private double tJTZXM5FY; // 特检特治项目5:5费用
	
	@Field(value="BCQFX")
	public String bCQFX; //本次起付线
	
	@Field(value="BCJBYLBXJE")
	public String bCJBYLBXJE; //本次基本医疗报销金额
	
	@Field(value="BCDBYLBXJE")
	public String bCDBYLBXJE; //本次大病医疗报销金额
	
	@Field(value="BCGWYBXJE")
	public String bCGWYBXJE; //本次公务员报销金额
	
	@Field(value="DESBZFJE")
	private double dESBZFJE; // 大额商保支付金额
	
	@Field(value="BCGRZHZFHZHYE")
	private double bCGRZHZFHZHYE; // 本次个人账户支付后帐户余额
	
	@Field(value="QSFZX")
	private String qSFZX; // 清算分中心
	
	@Field(value="QSLB")
	private String qSLB; // 清算类别
	
	@Field(value="QSFS")
	private String qSFS; // 清算方式
	
	@Field(value="QSQH")
	private String qSQH; // 清算期号
	
	@Field(value="CBRYSSSBJGBM")
	private String cBRYSSSBJGBM; // 参保人员所属社保机构编码
	
	@Field(value="XM")
	private String xM; // 姓名
	
	@Field(value="XB")
	private String xB; //性别
	
	@Field(value="SFHM")
	private String sFHM; // 身份号码
	
	@Field(value="CSRQ")
	private String cSRQ; // 出生日期
	
	@Field(value="SZNL")
	private double sZNL; // 实足年龄
	
	@Field(value="DWBH")
	private String dWBH; // 单位编号

	@Field(value="DWMC")
	private String dWMC; // 单位名称
	
	@Field(value="YLRYLB")
	private String yLRYLB; // 医疗人员类别
	
	@Field(value="YLZGRYLB")
	private String yLZGRYLB; // 医疗照顾人员类别
	
	@Field(value="JMYLRYLB")
	private String jMYLRYLB; // 居民医疗人员类别
	
	@Field(value="JMYLRYSF")
	private String jMYLRYSF; // 居民医疗人员身份
	
	@Field(value="QZLXJLBYJZFJE")
	private double qZLXJLBYJZFJE; // 其中离休奖励备用金支付金额
	
	@Field(value="QZLXJLBYJZFHDYE")
	private double qZLXJLBYJZFHDYE; // 其中离休奖励备用金支付后的余额
	
	@Field(value="ZXDSBZC")
	private String zXDSBZC; //执行的社保政策
	
	@Field(value="PK_INS")
	private String pkIns; //医保计划主键
	
	@Field(value="EU_STATUS")
	private String euStatus; //0-预预结算，1-医保结算，2-HIS结算 
	
	@Field(value="MODITY_TIME",date=FieldType.UPDATE)
	private Date modityTime; // 最后操作时间

	public String getPkPtmzjs() {
		return pkPtmzjs;
	}

	public void setPkPtmzjs(String pkPtmzjs) {
		this.pkPtmzjs = pkPtmzjs;
	}

	public String getjYLSH() {
		return jYLSH;
	}

	public void setjYLSH(String jYLSH) {
		this.jYLSH = jYLSH;
	}

	public String getjYYZM() {
		return jYYZM;
	}

	public void setjYYZM(String jYYZM) {
		this.jYYZM = jYYZM;
	}

	public String getyWLX() {
		return yWLX;
	}

	public void setyWLX(String yWLX) {
		this.yWLX = yWLX;
	}

	public String getPkPv() {
		return pkPv;
	}

	public void setPkPv(String pkPv) {
		this.pkPv = pkPv;
	}

	public String getPkSettle() {
		return pkSettle;
	}

	public void setPkSettle(String pkSettle) {
		this.pkSettle = pkSettle;
	}

	public String getjZBH() {
		return jZBH;
	}

	public void setjZBH(String jZBH) {
		this.jZBH = jZBH;
	}

	public String getfZXBH() {
		return fZXBH;
	}

	public void setfZXBH(String fZXBH) {
		this.fZXBH = fZXBH;
	}

	public String getjSBH() {
		return jSBH;
	}

	public void setjSBH(String jSBH) {
		this.jSBH = jSBH;
	}

	public String getzFLB() {
		return zFLB;
	}

	public void setzFLB(String zFLB) {
		this.zFLB = zFLB;
	}

	public String getgRBH() {
		return gRBH;
	}

	public void setgRBH(String gRBH) {
		this.gRBH = gRBH;
	}

	public double getgRZHZFBF() {
		return gRZHZFBF;
	}

	public void setgRZHZFBF(double gRZHZFBF) {
		this.gRZHZFBF = gRZHZFBF;
	}

	public String getjBSJ() {
		return jBSJ;
	}

	public void setjBSJ(String jBSJ) {
		this.jBSJ = jBSJ;
	}

	public double getfYZE() {
		return fYZE;
	}

	public String getEuStatus() {
		return euStatus;
	}

	public void setEuStatus(String euStatus) {
		this.euStatus = euStatus;
	}

	public void setfYZE(double fYZE) {
		this.fYZE = fYZE;
	}

	public double getqZFBF() {
		return qZFBF;
	}

	public void setqZFBF(double qZFBF) {
		this.qZFBF = qZFBF;
	}

	public String getxXZF() {
		return xXZF;
	}

	public void setxXZF(String xXZF) {
		this.xXZF = xXZF;
	}

	public double getfHFW() {
		return fHFW;
	}

	public void setfHFW(double fHFW) {
		this.fHFW = fHFW;
	}

	public double gettJTZXMFY() {
		return tJTZXMFY;
	}

	public void settJTZXMFY(double tJTZXMFY) {
		this.tJTZXMFY = tJTZXMFY;
	}

	public double gettSHCFY3() {
		return tSHCFY3;
	}

	public void settSHCFY3(double tSHCFY3) {
		this.tSHCFY3 = tSHCFY3;
	}

	public double gettJTZXM5FY() {
		return tJTZXM5FY;
	}

	public void settJTZXM5FY(double tJTZXM5FY) {
		this.tJTZXM5FY = tJTZXM5FY;
	}

	public String getbCQFX() {
		return bCQFX;
	}

	public void setbCQFX(String bCQFX) {
		this.bCQFX = bCQFX;
	}

	public String getbCJBYLBXJE() {
		return bCJBYLBXJE;
	}

	public void setbCJBYLBXJE(String bCJBYLBXJE) {
		this.bCJBYLBXJE = bCJBYLBXJE;
	}

	public String getbCDBYLBXJE() {
		return bCDBYLBXJE;
	}

	public void setbCDBYLBXJE(String bCDBYLBXJE) {
		this.bCDBYLBXJE = bCDBYLBXJE;
	}

	public String getbCGWYBXJE() {
		return bCGWYBXJE;
	}

	public void setbCGWYBXJE(String bCGWYBXJE) {
		this.bCGWYBXJE = bCGWYBXJE;
	}

	public double getdESBZFJE() {
		return dESBZFJE;
	}

	public void setdESBZFJE(double dESBZFJE) {
		this.dESBZFJE = dESBZFJE;
	}

	public double getbCGRZHZFHZHYE() {
		return bCGRZHZFHZHYE;
	}

	public void setbCGRZHZFHZHYE(double bCGRZHZFHZHYE) {
		this.bCGRZHZFHZHYE = bCGRZHZFHZHYE;
	}

	public String getqSFZX() {
		return qSFZX;
	}

	public void setqSFZX(String qSFZX) {
		this.qSFZX = qSFZX;
	}

	public String getqSLB() {
		return qSLB;
	}

	public void setqSLB(String qSLB) {
		this.qSLB = qSLB;
	}

	public String getqSFS() {
		return qSFS;
	}

	public void setqSFS(String qSFS) {
		this.qSFS = qSFS;
	}

	public String getqSQH() {
		return qSQH;
	}

	public void setqSQH(String qSQH) {
		this.qSQH = qSQH;
	}

	public String getcBRYSSSBJGBM() {
		return cBRYSSSBJGBM;
	}

	public void setcBRYSSSBJGBM(String cBRYSSSBJGBM) {
		this.cBRYSSSBJGBM = cBRYSSSBJGBM;
	}

	public String getxM() {
		return xM;
	}

	public void setxM(String xM) {
		this.xM = xM;
	}

	public String getxB() {
		return xB;
	}

	public void setxB(String xB) {
		this.xB = xB;
	}

	public String getsFHM() {
		return sFHM;
	}

	public void setsFHM(String sFHM) {
		this.sFHM = sFHM;
	}

	public String getcSRQ() {
		return cSRQ;
	}

	public void setcSRQ(String cSRQ) {
		this.cSRQ = cSRQ;
	}

	public double getsZNL() {
		return sZNL;
	}

	public void setsZNL(double sZNL) {
		this.sZNL = sZNL;
	}

	public String getdWBH() {
		return dWBH;
	}

	public void setdWBH(String dWBH) {
		this.dWBH = dWBH;
	}

	public String getdWMC() {
		return dWMC;
	}

	public void setdWMC(String dWMC) {
		this.dWMC = dWMC;
	}

	public String getyLRYLB() {
		return yLRYLB;
	}

	public void setyLRYLB(String yLRYLB) {
		this.yLRYLB = yLRYLB;
	}

	public String getyLZGRYLB() {
		return yLZGRYLB;
	}

	public void setyLZGRYLB(String yLZGRYLB) {
		this.yLZGRYLB = yLZGRYLB;
	}

	public String getjMYLRYLB() {
		return jMYLRYLB;
	}

	public void setjMYLRYLB(String jMYLRYLB) {
		this.jMYLRYLB = jMYLRYLB;
	}

	public String getjMYLRYSF() {
		return jMYLRYSF;
	}

	public void setjMYLRYSF(String jMYLRYSF) {
		this.jMYLRYSF = jMYLRYSF;
	}

	public double getqZLXJLBYJZFJE() {
		return qZLXJLBYJZFJE;
	}

	public void setqZLXJLBYJZFJE(double qZLXJLBYJZFJE) {
		this.qZLXJLBYJZFJE = qZLXJLBYJZFJE;
	}

	public double getqZLXJLBYJZFHDYE() {
		return qZLXJLBYJZFHDYE;
	}

	public void setqZLXJLBYJZFHDYE(double qZLXJLBYJZFHDYE) {
		this.qZLXJLBYJZFHDYE = qZLXJLBYJZFHDYE;
	}

	public String getzXDSBZC() {
		return zXDSBZC;
	}

	public void setzXDSBZC(String zXDSBZC) {
		this.zXDSBZC = zXDSBZC;
	}

	public String getPkIns() {
		return pkIns;
	}

	public void setPkIns(String pkIns) {
		this.pkIns = pkIns;
	}

	public Date getModityTime() {
		return modityTime;
	}

	public void setModityTime(Date modityTime) {
		this.modityTime = modityTime;
	}

}