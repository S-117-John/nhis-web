package com.zebone.nhis.common.support;

/***
 * 系统常量，包含：编码规则、系统参数
 * 
 */
public class SysConstant {

	/** 编码规则-患者编码-旧-001 新-0201*/
	public static final String ENCODERULE_CODE_HZ = "0201";

	/** 编码规则-门诊流水号-旧-002 新-0301*/
	public static final String ENCODERULE_CODE_MZ = "0301";

	/** 编码规则-住院流水号-旧-003 新-0302*/
	public static final String ENCODERULE_CODE_ZY = "0302";
	
	/** 编码规则-急诊流水号-0303 */
	public static final String ENCODERULE_CODE_JZ = "0303";
	
	/** 编码规则-门诊医保流水-0304 */
	public static final String ENCODERULE_CODE_YB = "0304";

	/** 编码规则-收据号-旧-004 新-0603*/
	public static final String ENCODERULE_CODE_SJ = "0603";

	/** 编码规则-门诊记费流水号-旧-005 新-0601*/
	public static final String ENCODERULE_CODE_MZJF = "0601";

	/** 编码规则-住院记费费流水号-旧-006 新-0602 */
	public static final String ENCODERULE_CODE_ZYJF = "0602";

	/** 编码规则-手术申请单号-旧-007 新-0403*/
	public static final String ENCODERULE_CODE_SSSQD = "0403";

	/** 编码规则-输血申请单号-旧-008 新-0404 */
	public static final String ENCODERULE_CODE_SXSQD = "0404";

	/** 编码规则-会诊申请单号-旧-009 新-0405*/
	public static final String ENCODERULE_CODE_HZSQD = "0405";

	/** 编码规则-检查申请单号-旧-010 新-0402 */
	public static final String ENCODERULE_CODE_JCSQD = "0402";

	/** 编码规则-检验申请单号-旧-011 新-0401*/
	public static final String ENCODERULE_CODE_JYSQD = "0401";

	/** 编码规则-出库-0708*/
	public static final String ENCODERULE_CODE_CKCLD = "0708";

	/** 编码规则-入库单0702*/
	public static final String ENCODERULE_CODE_RKD = "0702";

	/** 编码规则-采购计划单号-旧-012 新-0709*/
	public static final String ENCODERULE_CODE_CGJHD = "0709";

	/** 编码规则-处方单号-旧-013 新-0406*/
	public static final String ENCODERULE_CODE_CFD = "0406";

	/** 编码规则-草药处方号-旧-014 新-0407*/
	public static final String ENCODERULE_CODE_CYCF = "0407";

	/** 编码规则-门诊病历号-旧-015 新-0202*/
	public static final String ENCODERULE_CODE_MZBL = "0202";

	/** 编码规则-住院病历号-旧-016 新-0203 */
	public static final String ENCODERULE_CODE_ZYBL = "0203";

	/** 系统参数-入院登记时是否收取预交金-PV0002 */
	public static final String SYS_PARAM_PV0002 = "PV0002";

	/** 系统参数-门诊有效时间系统参数配置-PV0003 */
	public static final String SYS_PARAM_PV0003 = "PV0003";

	/** 系统参数-急诊有效时间系统参数配置-PV0004 */
	public static final String SYS_PARAM_PV0004 = "PV0004";

	/** 住院发药单号 */
	public static final String ENCODERULE_CODE_IPDS = "0502";

	/** 门诊医技执行单条码 旧-998 新-0503*/
	public static final String ENCODERULE_CODE_OPASEX = "0503";

}
