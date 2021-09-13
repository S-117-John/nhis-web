package com.zebone.nhis.ex.nis.pi.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.zebone.nhis.ex.pub.vo.PvInfantVo;
import com.zebone.platform.modules.mybatis.Mapper;

@Mapper
public interface PvInfantMapper {
    /**
     * 查询婴儿及就诊信息
     * @param paramMap{pkInfant,sortNo,pkPv}
     * @return
     */
	public Map<String,Object> queryInfantAndPV(Map<String,Object> paramMap);
	
	/**
	 * 根据婴儿主键或就诊主键、婴儿序号，分娩明细获取分娩信息及婴儿信息
	 * @param {pkInfant,pkPv,pkLaborrecdt,sortNo}
	 * @return
	 */
	public List<PvInfantVo> queryInfantListByRec(Map<String,Object> paramMap);
	/**
	 * 根据婴儿主键或就诊主键、婴儿序号，分娩明细获取婴儿信息及分娩信息
	 * @param {pkInfant,pkPv,pkLaborrecdt,sortNo}
	 * @return
	 */
	public List<PvInfantVo> queryInfantListByInfant(Map<String,Object> paramMap);
	/**
	 * 根据就诊主键获取分娩信息
	 * @param {pkPv}
	 * @return
	 */
	public List<PvInfantVo> queryRecList(@Param("pkPv")String pkPv);
	/**
	 * 根据就诊信息查询婴儿信息（婴儿转出院校验使用）
	 * @param paramMap
	 * @return
	 */
	public Map<String,Object> queryInfantByPv(Map<String,Object> paramMap);
	/**
	 * 查询未分配床位的婴儿列表
	 * @param paramMap
	 * @return
	 */
	public List<Map<String,Object>> getInfantNoBedList(Map<String,Object> paramMap);
	/**
	 * 根据母亲就诊信息及就诊病区查询婴儿信息
	 * @param 
	 * @return
	 */
	public List<Map<String,Object>> getInfantListByMother(@Param("pkPv")String pkPv,@Param("pkDeptNs")String pkDeptNs);
	
}
