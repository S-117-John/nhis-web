package com.zebone.nhis.ex.nis.fee.dao;

import com.zebone.platform.modules.mybatis.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface BlCgIpQueryMapper {
   /**
    * 患者费用分类汇总
    * @param map{pkPvs,dateBegin,dateEnd,type,pkItemcate}
    * @return
    */
	public List<Map<String,Object>> queryBlCgIpSummer(Map<String,Object> map) ;
   /**
    * 查询患者费用明细
    * @param map{pkPvs,dateBegin,dateEnd,type,pkDept,pkDeptEx,pkItemcate,codeItemcate}
    * @return
    */
	public List<Map<String,Object>> queryBlCgIpDetails(Map<String,Object> map) ;
	
    /**
    * 查询产房增加的婴儿费用分类汇总信息
    * @param map{pkPvs,dateBegin,dateEnd,type,pkItemcate}
    * @return
    */
	public List<Map<String,Object>> queryInfantBlCgIpSummer(Map<String,Object> map) ;
   /**
    * 查询产房增加的婴儿费用明细信息
    * @param map{pkPvs,dateBegin,dateEnd,type,pkDept,pkDeptEx,pkItemcate}
    * @return
    */
	public List<Map<String,Object>> queryInfantBlCgIpDetails(Map<String,Object> map) ;
		
	
	/**
	 * 根据医嘱查询计费明细
	 * @param map{pkOrd}
	 * @return
	 */
	public List<Map<String,Object>> queryBlCgIpDetailsByOrd(Map<String,Object> map) ;
	/**
	 * 根据就诊患者查询计费明细列表
	 * @param map{pkPv,nameCg,flagPd}
	 * @return
	 */
	public List<Map<String,Object>> queryBlCgIpDetailsByPv(Map<String,Object> map);
	/**
	 * 根据就诊主键，就诊科室查询患者各类费用
	 * @param map{pkPv,pkDept}
	 * @return
	 */
	public List<Map<String,Object>> queryCgCateFeeByPv(Map<String,Object> map);
	/**
	 * 根据就诊主键，就诊科室等查询患者各项费用
	 * @param map{pkPv,pkDept,nameCg,pkItemcate}
	 * @return
	 */
	public List<Map<String,Object>> queryCgItemFeeByPv(Map<String,Object> map);
	
	/**
	 * 患者费用分类 （账单码）汇总
	 * @param map{pkPvs,dateBegin,dateEnd,type,pkItemcate}
	 * @return
	 */
	public List<Map<String,Object>> queryCgIpSummer(Map<String,Object> map) ;
	/**
	 * 查询患者费用明细 （项目）汇总
	 * @param map{pkPvs,dateBegin,dateEnd,type,pkDept,pkDeptEx,pkItemcate,codeItemcate}
	 * @return
	 */
	public List<Map<String,Object>> queryCgIpDetails(Map<String,Object> map) ;
	/**
	 * 查询患者费用分类 （账单码）汇总，其中药品按每日实际执行数量统计费用
	 * @param map{pkPvs,dateBegin,dateEnd,type,pkItemcate}
	 * @return
	 */
	public List<Map<String,Object>> queryExIpSummerByDatePlan(Map<String,Object> map) ;
	/**
	 * 查询患者费用明细 （项目），其中药品按每日实际执行数量统计费用
	 * @param map{pkPvs,dateBegin,dateEnd,type,pkDept,pkDeptEx,pkItemcate,codeItemcate}
	 * @return
	 */
	public List<Map<String,Object>> queryExIpDetailsByDatePlan(Map<String,Object> map) ;
	/**
	 * 查询患者费用分类 （账单码）汇总，实际记费
	 * @param map{pkPvs,dateBegin,dateEnd,type,pkItemcate}
	 * @return
	 */
	public List<Map<String,Object>> queryCgIpSummerByDateHap(Map<String,Object> map) ;
	/**
	 * 查询患者费用明细 （项目），实际记费
	 * @param map{pkPvs,dateBegin,dateEnd,type,pkDept,pkDeptEx,pkItemcate,codeItemcate}
	 * @return
	 */
	public List<Map<String,Object>> queryCgIpDetailsByDateHap(Map<String,Object> map) ;

    public List<Map<String, Object>> queryToalBlAmount(List<String> pkPvs);

	public List<Map<String, Object>> queryToalPreAmount(List<String> pkPvs);
	
	/**
	 * 查询患者费用核查信息
	 * @param map
	 * @return
	 */
	public List<Map<String, Object>> queryPatiDetails(Map<String, Object> map);
	/**
	 * 查询患者费用核查明细信息
	 * @param map
	 * @return
	 */
	public List<Map<String, Object>> queryCgDetaileds(Map<String, Object> map);
}
