package com.zebone.nhis.compay.ins.syx.dao.gzyb;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.zebone.nhis.common.module.compay.ins.syx.GzybDataSource;
import com.zebone.nhis.common.module.compay.ins.syx.gzyb.GzybVisit;
import com.zebone.nhis.common.module.compay.ins.syx.gzyb.InsGzybStInjuryVo;
import com.zebone.nhis.compay.ins.syx.vo.gzyb.GzDiagVo;
import com.zebone.nhis.compay.ins.syx.vo.gzyb.BlipdtReUploadVo;
import com.zebone.nhis.compay.ins.syx.vo.gzyb.BlipdtVo;
import com.zebone.nhis.compay.ins.syx.vo.gzyb.BlopdtVo;
import com.zebone.nhis.compay.ins.syx.vo.gzyb.MiddleHisFyjs;
import com.zebone.nhis.compay.ins.syx.vo.gzyb.MiddleHisMzdj;
import com.zebone.nhis.compay.ins.syx.vo.gzyb.MiddleHisMzjs;
import com.zebone.nhis.compay.ins.syx.vo.gzyb.MiddleHisZydj;
import com.zebone.nhis.compay.ins.syx.vo.gzyb.SettlementInfo;
import com.zebone.nhis.compay.ins.syx.vo.gzyb.VisitInfo;
import com.zebone.nhis.compay.ins.syx.vo.gzyb.DeptVo;
import com.zebone.platform.modules.mybatis.Mapper;

@Mapper
public interface GzybMapper {

	/** 015001007004 通过医保计划、所属机构、所属科室查询对应数据源信息 */
	public GzybDataSource queryDsInfo(@Param(value = "pkhp") String pkhp,
			@Param(value = "pkOrg") String pkOrg,
			@Param(value = "pkdept") String pkdept);

	/** 015001007008 通过通过中间库、身份证号码查询医保中间表-住院登记表信息 */
	public List<MiddleHisZydj> queryInfoList(
			@Param(value = "gmsfhm") String gmsfhm,
			@Param(value = "zyh") String zyh, @Param(value = "xm") String xm,
			@Param(value = "xb") String xb);

	/** 015001007011通过就诊主键查询未结算的登记信息 */
	public GzybVisit getGzybVisit(@Param(value = "pkpv") String pkpv);

	/** 015001007016根据就诊主键pk_pv查询住院收费明细bl_ip_dt的医保上传标志flag_insu为0的记录 */
	public List<BlipdtVo> qryBdItemAndOrderByPkPv(
			@Param(value = "pkpv") String pkpv,
			@Param(value = "pkdept") String pkdept,
			@Param("pkCgips") List<String> pkCgips,
			@Param(value = "euhpdicttype") String euhpdicttype,
			@Param(value = "dateEnd") String dateEnd,
			@Param(value = "dateBegin") String dateBegin);

	/** 015001007021通过就医登记号获取中间库-费用结算表HIS_FYJS的数据 */
	public MiddleHisFyjs queryCostInfo(@Param(value = "jydjh") String jydjh);

	/** 015001007024取消住院结算时根据就诊主键pk_pv查询已结算的登记信息 */
	public GzybVisit getSettledVisit(@Param(value = "pkpv") String pkpv);

	/**
	 * 根据住院收费主键跟新bl_ip_dt中的flag_ins
	 * 
	 * @param pkCgips
	 */
	public void updateFlagInsuByPk(List<String> pkCgips);

	/** 015001007048通过结算主键获取已上传的费用明细数据 */
	public List<BlipdtReUploadVo> qryBdItemAndOrderByPkSettle(
			@Param(value = "pkSettle") String pkSettle,
			@Param(value = "euhpdicttype") String euhpdicttype);

	/** 门诊 */
	/** 通过通过中间库、身份证号码查询医保中间表-门诊登记表信息 */
	public List<MiddleHisMzdj> queryMZInfoList(
			@Param(value = "gmsfhm") String gmsfhm,
			@Param(value = "zyh") String zyh, @Param(value = "xm") String xm,
			@Param(value = "xb") String xb);

	/** 通过就医登记号获取中间库-门诊费用结算表HIS_MZJS的数据 */
	public MiddleHisMzjs queryMZCostInfo(@Param(value = "jydjh") String jydjh);

	public List<BlopdtVo> queryNoSettleInfoForCgByPkPv(
			@Param(value = "pkpv") String pkpv,
			@Param(value = "pkCgips") List<String> pkCgips);

	public VisitInfo qrycityPiInfoByPkPv(
			@Param(value = "pkpv") String pkpv);

	public List<BlopdtVo> querySettleInfoByPkPvAndPkSettle(
			@Param(value = "pkpv") String pkpv,
			@Param(value = "pkSettle") String pkSettle,
			@Param(value = "euhpdicttype") String euhpdicttype);
	
	/**
	 * 
	 * @param pkpv
	 * @return
	 */
	public List<GzDiagVo> qryDiagVo(@Param(value = "pkPv") String pkpv);
	
	public VisitInfo qrycityPiInfoByPkVisit(
			@Param(value = "pkVisit") String pkVisit);
	
	public SettlementInfo qrycitySettlementInfoByPkVisit(
			@Param(value = "pkVisit") String pkVisit);
	
	
	public InsGzybStInjuryVo qryInsGzybStInjuryVoByPkInsst(
			@Param(value = "pkInsst") String pkInsst);
	
	public DeptVo qryDeptVo(@Param(value = "pkdept") String pkdept);
}
