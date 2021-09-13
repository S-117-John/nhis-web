package com.zebone.nhis.base.pub.dao;

import java.util.List;
import java.util.Map;

import com.zebone.nhis.common.module.base.bd.res.BdResBed;
import com.zebone.platform.modules.mybatis.Mapper;

@Mapper
public interface BdResPubMapper {

	/**
	 * 根据病区主键、编码，确认是否存在该床位
	 * @param paramMap
	 * @return
	 */
	public List<BdResBed> qryInfBedByCode(Map<String,Object> paramMap);
	
	/**
	 * 根据病区主键，校验该科室是否允许添加婴儿床位
	 * @param paramMap
	 * @return
	 */
	public Map<String,Object> chkDeptAbleAddInf(Map<String,Object> paramMap);

	/**
	 * 获取母亲床位信息
	 * @param data
	 * @return
	 */
    BdResBed queMomBed(Map<String, Object> data);
}
