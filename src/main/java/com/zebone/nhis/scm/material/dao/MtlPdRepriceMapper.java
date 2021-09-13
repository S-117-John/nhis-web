package com.zebone.nhis.scm.material.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.zebone.nhis.scm.pub.vo.MtlPdRepriceDtVo;
import com.zebone.nhis.scm.pub.vo.MtlPdRepriceHistVo;
import com.zebone.nhis.scm.pub.vo.MtlPdRepriceVo;
import com.zebone.platform.modules.mybatis.Mapper;

@Mapper
public interface MtlPdRepriceMapper {
	/**
	 * 查询调价单
	 * @param map{pkOrg,dtReptype,codeRep,dateBeginE,dateEndE,dateBeginC,dateEndC,euStatus}
	 * @return
	 */
	public List<MtlPdRepriceVo> queryRepriceList(Map<String,Object> map);
	/**
	 * 查询调价单对应的明细
	 * @param pkPdrep
	 * @return
	 */
	public List<MtlPdRepriceDtVo> queryRepriceDtList(@Param("pkPdrep") String pkPdrep);
	
	/***
	 * 查询调价历史
	 * @param codeRep
	 * @return
	 */
	public List<MtlPdRepriceHistVo> queryRepriceHistList(Map<String,Object> map);
	
	/**
	 * 生成调价历史查询---oracle
	 * @param pkPdrep
	 * @return
	 */
	public List<MtlPdRepriceHistVo> generateHistOracle(@Param("pkPdrep") String pkPdrep);
	/**
	 * 生成调价历史查询---sqlserver
	 * @param pkPdrep
	 * @return
	 */
	public List<MtlPdRepriceHistVo> generateHistSqlServer(@Param("pkPdrep") String pkPdrep);
	
}
