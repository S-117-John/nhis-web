package com.zebone.nhis.bl.pub.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.zebone.nhis.bl.pub.vo.DoctorMaintainVo;
import com.zebone.platform.modules.mybatis.Mapper;
@Mapper
public interface DoctorMaintainMapper {
	/**
	 * 查询医技医嘱
	 * @param pkdept
	 * @return
	 */
	public List<DoctorMaintainVo> QueryList(@Param("pkdept")String pkdept);
	/**
	 * 根据拼音码查询医技医嘱
	 * @param map
	 * @return
	 */
	public List<DoctorMaintainVo> QueryListByCode(Map<String,Object> map);
}
