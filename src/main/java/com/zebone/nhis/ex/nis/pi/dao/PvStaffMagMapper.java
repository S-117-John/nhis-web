package com.zebone.nhis.ex.nis.pi.dao;

import java.util.List;
import java.util.Map;

import com.zebone.nhis.ex.nis.pi.vo.PvClinicGroupVo;
import com.zebone.nhis.ex.nis.pi.vo.PvStaffVo;
import com.zebone.platform.modules.mybatis.Mapper;

@Mapper
public interface PvStaffMagMapper {
   
	/**
     * 查询患者医护人员信息
     * @param paramMap{pkPv}
     * @return
     */
	public List<PvStaffVo> queryPvStaffList(Map<String,Object> paramMap);
	
	/**
	 * 查询患者医疗组信息
	 * @param {pkInfant,pkPv,pkLaborrecdt,sortNo}
	 * @return
	 */
	public List<PvClinicGroupVo> queryPvWgList(Map<String,Object> paramMap);

	/**
	 * 查询医生医疗组信息
	 * @param {pkInfant,pkPv,pkLaborrecdt,sortNo}
	 * @return
	 */
	public List<PvClinicGroupVo> queryWgList(Map<String,Object> paramMap);
}
