package com.zebone.nhis.common.support;

/**
 * 基础码表常用值 接口 修改后请同步更新bd_defdoc表对应的内容
 * @author
 * 
 */
public interface IDictCodeConst {

	/**
	 * 基础码表类型---性别
	 */
	public static final String DT_SEX_UNKNOWN = "01"; // 未知

	public static final String DT_SEX_BOY = "02"; // 男

	public static final String DT_SEX_GIRL = "03"; // 女

	public static final String DT_SEX_UNDESC = "04"; // 未说明

	public static final String DT_SEX_UNION = "034"; // 男中性

	/**
	 * 基础码表类型---部门类型010200
	 */
	public static final String DT_DEPTTYPE_CLINICAL = "01"; // 临床科室

	public static final String DT_DEPTTYPE_NURSINGUNIT = "02";// 护理单元
	
	public static final String DT_DEPTTYPE_CF = "0310";

	/**
	 * 基础编码类型---医护人员角色
	 */
	public static final String DT_EMPROLE_MAINDOCT = "0001";// 主管医生

	public static final String DT_EMPROLE_HEADDOCT = "0000";// 主任医生

	public static final String DT_EMPROLE_IPDOCT = "0002";// 住院医师

	public static final String DT_EMPROLE_INTERDOCT = "0003";// 进修医师

	public static final String DT_EMPROLE_REFDOCT = "0004";// 实习医生

	public static final String DT_EMPROLE_QADOCT = "0005";// 质控医生

	public static final String DT_EMPROLE_UPPERDOCT = "0006";// 上级医生

	public static final String DT_EMPROLE_MAINNS = "0100";// 主管护士

	public static final String DT_EMPROLE_HEADNS = "0101";// 上级医生

	/**
	 * 基础编码类型---卡类型
	 */
	public static final String DT_CARDTYPE_HOS = "01";// 院内就诊卡

	public static final String DT_CARDTYPE_YBK = "02";// 医保卡

	public static final String DT_CARDTYPE_CITY = "03";// 城市一卡通

	public static final String DT_CARDTYPE_IDC = "04";// 身份证

	/**
	 * 基础编码类型---出入库业务类型
	 */
	/** 0101 采购入库  */
	public static final String DT_STTYPE_BUY = "0101";
	/** 0104 医嘱入库  */
	public static final String DT_STTYPE_ORDIN = "0104";
	/** 0106 科室退回  */
	public static final String DT_STTYPE_DEPTRTN = "0106";
	/** 0107 盘盈入库 */
	public static final String DT_STTYPE_INVLOSSIN = "0107";
	/** 0201 调拨出库  */
	public static final String DT_STTYPE_ALLOOUT = "0201";
	/** 0202 科室出库【科室领用】  */
	public static final String DT_STTYPE_DETPOUT = "0202";
	/** 0203 采购退回  */
	public static final String DT_STTYPE_RTN = "0203";
	/** 0204 入库退回  */
	public static final String DT_STTYPE_INRTN = "0204";
	/** 0205 医嘱出库  */
	public static final String DT_STTYPE_ORDOUT = "0205";
	/** 0206 盘亏出库  */
	public static final String DT_STTYPE_INVLOSSOUT = "0206";
	/** 0207 报废出库  */
	public static final String DT_STTYPE_DISOUT = "0207";
	/** 0208 其他出库  */
	public static final String DT_STTYPE_OTOUT = "0208";

	/**
	 * 基础编码类型---采购计划类型
	 */
	public static final String DT_PLANTYPE_INSTORE = "01";// 入库计划

	public static final String DT_PLANTYPE_BUY = "0101";// 采购计划

	public static final String DT_PLANTYPE_OUTSTORE = "02";// 出库计划

	public static final String DT_PLANTYPE_DEPTAPP = "0201";// 部门请领

	public static final String DT_PLANTYPE_ALLOCATION = "03";// 调拨计划

	public static final String DT_PLANTYPE_ALLOAPP = "0301";// 调拨请领

	/**
	 * 基础编码类型---病情等级
	 */

	public static final String DT_BQDJ_BW = "03";// 病危

	public static final String DT_BQDJ_BZ = "02";// 病重

	public static final String DT_BQDJ_JZ = "01";// 加重

	public static final String DT_BQDJ_WD = "00";// 稳定

	/**
	 * 支付方式---门诊收费计算选择支付方式
	 */

	public static final String CASH = "1";// 现金

	public static final String CHEQUE = "2";// 支票

	public static final String BANKCARD = "3";// 银行卡

	public static final String PATIACCOUNT = "4";// 患者账户

	public static final String INNERTRANS = "5";// 内部转账

	public static final String UNITACCOUNT = "6";// 单位记账

	public static final String WECHAT = "7";// 微信支付

	public static final String ALI = "8";// 支付宝

	public static final String Other = "99";// 其他
	
	/**
	 * 就诊类型
	 */
	public static final String EUPVTYPE_IP = "3";//住院
	
	public static final String EUPVTYPE_ER = "2";//急诊
	
	public static final String EUPVTYPE_OP = "1";//门诊
	
	public static final String EUPVTYPE_PE = "4";//体检
	
	/**
	 * 就诊模式
	 */
	public static final String EUPVMODE_NOR = "0";//普通
	
	public static final String EUPVMODE_CP = "1";//临床路径
	
	public static final String EUPVMODE_DAN = "2";//单病种模式
	
	public static final String EUPVMODE_SP = "11";//特诊
	
	public static final String EUPVMODE_EM = "12";//急诊留观
	
	public static final String EUPVMODE_HE = "21";//血液透析
	
	
	
}
