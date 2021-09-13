package com.zebone.nhis.base.bd.dao;

import java.util.List;
import java.util.Map;

import com.zebone.nhis.common.module.cn.ipdw.BdUnit;
import com.zebone.platform.modules.mybatis.Mapper;

/**
 * 计量单位Mapper
 * @author lijipeng
 *
 */
@Mapper
public interface MseUnitMapper {
	/**查询所有计量单位信息**/
	List<BdUnit> queryAllUnits();
	
	/**查询计量单位(条件查询)**/
	List<BdUnit> queryUnitsByCondition(Map<String,Object> params);
	
	/**根据编码和名称查询计量单位是否存在**/
	int queryUnitCountByCondition(Map<String,Object> params);
	
	/**根据主键获取计量单位信息**/
	BdUnit queryUnitBypkUnit(String pkUnit);
	
	/**根据主键删除单位(必须是未作废的)**/
	int delUnitByPk(String pkUnit);
}
