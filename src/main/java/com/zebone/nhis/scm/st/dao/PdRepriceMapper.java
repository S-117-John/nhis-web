package com.zebone.nhis.scm.st.dao;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.zebone.nhis.scm.pub.vo.PdRepriceDtVo;
import com.zebone.nhis.scm.pub.vo.PdRepriceHistVo;
import com.zebone.nhis.scm.pub.vo.PdRepriceVo;
import com.zebone.platform.modules.mybatis.Mapper;

@Mapper
public interface PdRepriceMapper {
	/**
	 * 查询调价单
	 * @param map{pkOrg,dtReptype,codeRep,dateBeginE,dateEndE,dateBeginC,dateEndC,euStatus}
	 * @return
	 */
	public List<PdRepriceVo> queryRepriceList(Map<String,Object> map);
	/**
	 * 查询调价单对应的明细
	 * @param pkPdrep
	 * @return
	 */
	public List<PdRepriceDtVo> queryRepriceDtList(@Param("pkPdrep") String pkPdrep);
	
	/***
	 * 查询调价历史
	 * @param
	 * @return
	 */
	public List<PdRepriceHistVo> queryRepriceHistList(Map<String,Object> map);
	
	/**
	 * 生成调价历史查询---oracle
	 * @param pkPdrep
	 * @return
	 */
	public List<PdRepriceHistVo> generateHistOracle(@Param("pkPdrep") String pkPdrep);
	/**
	 * 生成调价历史查询---sqlserver
	 * @param pkPdrep
	 * @return
	 */
	public List<PdRepriceHistVo> generateHistSqlServer(@Param("pkPdrep") String pkPdrep);


	/**
	 * 根据单个药品生成调价历史查询
	 * @param priceDt
	 * @return
	 */
	public List<PdRepriceHistVo> generateHistByPd(Map<String,Object> priceDt);

}
