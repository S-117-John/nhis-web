package com.zebone.nhis.ex.oi.dao;


import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.zebone.nhis.ex.oi.vo.OinfuPlaceViewVO;
import com.zebone.platform.modules.mybatis.Mapper;

@Mapper
public interface OinPlaceViewMapper {
	/**
	 * 根据输液大厅，查询座位及患者信息
	 * @param paramMap
	 * @return
	 */
	public List<OinfuPlaceViewVO> searchPlaceIv(Map<String,Object> paramMap);
	/**
	 *根据座位查询执行记录的药品信息
	 * @param paramMap
	 * @return
	 */
	public List<OinfuPlaceViewVO> searchPdInfo(@Param("pkPlaceiv")String pkPlaceiv);
}
