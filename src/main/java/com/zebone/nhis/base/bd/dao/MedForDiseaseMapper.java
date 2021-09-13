package com.zebone.nhis.base.bd.dao;

import java.util.List;
import java.util.Map;

import com.zebone.platform.modules.mybatis.Mapper;

@Mapper
public interface MedForDiseaseMapper {
	
	/**
	 * 查询药品信息
	 * @param paramMap
	 * @return
	 */
	public List<Map<String,Object>> qryPdInfo(Map<String, Object> paramMap);
	
	/**
	 * 查询药品关联的病种信息
	 * @param pkPd
	 * @return
	 */
	public List<Map<String,Object>> qryBdPdRefDisease(String pkPd);
	
	/**
	 * 按病种：查询病种信息
	 * @return
	 */
	public List<Map<String,Object>> qryDiseaseInfo();
	
	/**
	 * 按病种：根据病种查询关联药品信息
	 * @param pkGzgydisease
	 * @return
	 */
	public List<Map<String,Object>> qryDisRefPdInfo(String pkGzgydisease);
	
	/**
	 *  删除病种关联药品信息
	 * @param pkDiseaseords
	 */
	public void delDisRefPd(List<String> pkDiseaseords);
}
