package com.zebone.nhis.pro.lb.arch.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.zebone.nhis.pro.lb.arch.vo.ArchDocVo;
import com.zebone.nhis.pro.lb.arch.vo.PatientArchVo;
import com.zebone.platform.modules.mybatis.Mapper;

@Mapper
public interface ArchiveLbMapper {

	/**
	 * 结合病历归档信息，查询出院患者信息
	 * @param param
	 * @return
	 */
	List<PatientArchVo> queryPatientOfArch(Map<String,Object> param);
	
	/**
	 * 查询患者病历归档和打印信息，不包含内容
	 * @param pkPv
	 * @return
	 */
	List<ArchDocVo> queryArchDocAndPrint(@Param("pkPv") String pkPv);
	
	List<ArchDocVo> queryArchDoc(@Param("pkPv") String pkPv);
}
