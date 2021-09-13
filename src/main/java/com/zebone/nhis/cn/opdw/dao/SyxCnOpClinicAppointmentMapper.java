package com.zebone.nhis.cn.opdw.dao;

import java.util.List;
import java.util.Map;

import com.zebone.platform.modules.mybatis.Mapper;

@Mapper
public interface SyxCnOpClinicAppointmentMapper {

	/**
	 * 查询当前医生排班日历
	 * @param paramMap
	 * @return
	 */
	List<Map<String, Object>> qryClinicSch(Map<String, Object> paramMap);

	/**
	 * 查询当前医生排班日历
	 * @param paramMap
	 * @return
	 */
	List<Map<String, Object>> qryClinicSchTic(Map<String, Object> paramMap);

	/**
	 * 查询医生出诊信息
	 * @param paramMap
	 * @return
	 */
	List<Map<String, Object>> qryClinicSchList(Map<String, Object> paramMap);
	/**
	 * 查询医生出诊信息
	 * @param paramMap
	 * @return
	 */
	List<Map<String, Object>> qryClinicSchListTic(Map<String, Object> paramMap);

}
