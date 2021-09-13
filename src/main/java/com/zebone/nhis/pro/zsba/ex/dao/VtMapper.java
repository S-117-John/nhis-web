package com.zebone.nhis.pro.zsba.ex.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.zebone.nhis.pro.zsba.ex.vo.ExVtsOccByPv;
import com.zebone.nhis.pro.zsba.ex.vo.ExVtsOccVo;
import com.zebone.nhis.pro.zsba.ex.vo.ExVtsoccDtVo;
import com.zebone.nhis.pro.zsba.ex.vo.SkinTestVo;
import com.zebone.platform.modules.mybatis.Mapper;

@Mapper
public interface VtMapper {
	/**
	 * 查询患者生命体征信息
	 * @param map
	 * @return
	 */
	public List<ExVtsOccVo> queryVtsByDate(Map<String,Object> map);
	
	/**
    * 根据主键查询生命体征明细
    * @param map{pkVtsocc,euDateslot,hourVts}
    * @return
    */
	public List<ExVtsoccDtVo> queryVtsDetailsByPk(Map<String,Object> map) ;
    
	/**
	 * 查询皮试结果
	 * @param paramMap
	 * @return
	 */
	public List<SkinTestVo> querySkinTestByPkPv(@Param(value = "pkPv") String paramMap);
	/**
	 * 查询单个患者生命体征信息
	 * @param paramMap
	 * @return
	 */
	public List<ExVtsOccByPv> queryVtsByPvAndDateByOne(Map<String, Object> paramMap);
	/**
	 * 查询单个患者生命体征明细信息
	 * @param paramMap
	 * @return
	 */
	public List<ExVtsoccDtVo> queryVtsDetailsByPV(Map<String, Object> paramMap);
	
	/**
	 * 根据日期查询产房所有患者生命体征
	 * @param map{pkDeptNs,dateCur,euDateslot,hourVts}
	 * @return
	 */
	public List<ExVtsOccVo> queryLaborVtsByDate(Map<String,Object> map);

}
