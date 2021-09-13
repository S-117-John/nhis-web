package com.zebone.nhis.pro.zsba.compay.up.dao;

import java.util.List;
import java.util.Map;

import com.zebone.platform.modules.mybatis.Mapper;

@Mapper
public interface ZsBlCgIpQueryMapper {
	
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

}
