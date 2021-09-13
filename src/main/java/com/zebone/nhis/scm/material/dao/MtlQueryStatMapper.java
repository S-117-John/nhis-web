package com.zebone.nhis.scm.material.dao;

import java.util.List;
import java.util.Map;

import com.zebone.nhis.scm.material.vo.BdPdQuyBillDtlsVo;
import com.zebone.nhis.scm.material.vo.BdPdBillVo;
import com.zebone.nhis.scm.material.vo.BdPdPurVo;
import com.zebone.nhis.scm.material.vo.BdPdReceiveVo;
import com.zebone.nhis.scm.material.vo.BdPdStDetailsVo;
import com.zebone.nhis.scm.material.vo.BdPdStRecordVo;
import com.zebone.nhis.scm.material.vo.BdPdStVo;
import com.zebone.nhis.scm.material.vo.MtlDeptPdStVo;
import com.zebone.nhis.scm.material.vo.MtlLicensePdVo;
import com.zebone.nhis.scm.material.vo.MtlLicenseSupVo;
import com.zebone.nhis.scm.material.vo.MtlTransSumVo;
import com.zebone.nhis.scm.material.vo.PdDeptUsingVo;
import com.zebone.platform.modules.mybatis.Mapper;

/**
 * 物资-查询统计Mapper
 * @author c
 *
 */
@Mapper
public interface MtlQueryStatMapper {
	
	/**查询科室在用物品记录*/
	public List<PdDeptUsingVo> searchPdSDeptUsing(Map<String,Object> param);
	
	/**查询物品领用信息*/
	public List<BdPdReceiveVo> searchReceiveInfo(Map<String,Object> param);
	
	/**根据库单主键获取库单信息*/
	public BdPdStVo searchReceiveByPk(String pkPdst);
	
	/**根据入库单主键查询库单明细信息*/
	public List<BdPdStDetailsVo> searchStDtsByPkPdst(String pkPdst);
	
	/**查询物品采购信息*/
	public List<BdPdPurVo> searchBdPdPurInfo(Map<String,Object> param);
	
	/**查询当前仓库下的物品出入库记录*/
	public List<BdPdStRecordVo> searchPdStRecord(Map<String,Object> param);
	
	/**查询仓库物品台账零售金额信息*/
	public List<BdPdBillVo> searchBdPdBillAmt(Map<String,Object> param);
	
	/**查询仓库物品台账成本金额信息*/
	public List<BdPdBillVo> searchBdPdBillAmtCost(Map<String,Object> param);
	
	/**查询物品台账明细信息*/
	public List<BdPdQuyBillDtlsVo> searchBdPdBillDtls(Map<String,Object> param);
	
	/**
	 * 查询收发存汇总信息
	 * @param param
	 * @return
	 */
	public List<MtlTransSumVo> queryMtlTransSum(Map<String,Object> param);
	
	
	/**
	 * 三证查询-物品注册证效期
	 * @param param
	 * @return
	 */
	public List<MtlLicensePdVo> queryMtlLicensePd(Map<String,Object> param);
	
	/**
	 * 三证查询-供应商经营效期和许可效期
	 * @param param
	 * @return
	 */
	public List<MtlLicenseSupVo> queryMtlLicenseSup(Map<String,Object> param);
	
	
	/**
	 * 查询科室物品领退记录
	 * @param param
	 * @return
	 */
	public List<MtlDeptPdStVo> queryDeptPdSt(Map<String,Object> param);
	
}
