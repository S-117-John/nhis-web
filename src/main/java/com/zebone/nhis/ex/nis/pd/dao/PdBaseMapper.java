package com.zebone.nhis.ex.nis.pd.dao;

import java.util.List;
import java.util.Map;

import com.zebone.nhis.ex.nis.pd.vo.PdBaseVo;
import com.zebone.platform.modules.mybatis.Mapper;

/**
 * 基数药维护
 * @author yangxue
 *
 */
@Mapper
public interface PdBaseMapper {
/**
 * 查询基数药品
 * @param map{pkDeptNs,pdname}
 * @return
 */
	public List<PdBaseVo> queryPdBaseByCon(Map<String,Object> map);
	
}
