package com.zebone.nhis.ex.nis.ns.dao;

import java.util.List;
import java.util.Map;

import com.zebone.platform.modules.mybatis.Mapper;
import com.zebone.nhis.ex.nis.ns.vo.ExPdApplyDetailVo;
import com.zebone.nhis.ex.nis.ns.vo.MedicineVo;
import com.zebone.nhis.ex.nis.ns.vo.OrderOccVo;

@Mapper
public interface DrugRetreatMapper {
	
	/**
	 * 查询待生成退药请领的执行单
	 * @param map
	 * @return
	 */
	public List<Map<String,Object>> queryOccList(Map<String,Object> map);
	
	public List<MedicineVo> queryMedicineList(Map<String,Object> map);
	
	/**
	 * 更新执行单 - 请退主键
	 * @param exPdApplyDetailVo
	 */
	public void updateExOrderOcc(ExPdApplyDetailVo exPdApplyDetailVo);
	
	/**
	 * 根据参数更新执行单状态
	 * @param paramMap
	 */
	public void updateExList(Map<String,Object> paramMap);
	
	/**
	 * 查询待生成退药请领的执行单--[中山二院需求]
	 * @param map
	 * @return
	 */
	public List<Map<String,Object>> queryOccListBySyx(Map<String,Object> map);
	
	public List<MedicineVo> queryMedicineListBySyx(Map<String,Object> map);
	/**
	 * 查询退药申请单
	 * @param map
	 * @return
	 */
	public List<Map<String,Object>> queryRtnApply(Map<String,Object> map);
	
}
