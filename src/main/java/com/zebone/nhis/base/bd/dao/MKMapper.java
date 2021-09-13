package com.zebone.nhis.base.bd.dao;

import java.util.List;
import java.util.Map;

import com.zebone.nhis.common.module.base.bd.mk.BdSupply;
import com.zebone.nhis.common.module.base.bd.mk.BdSupplyItem;
import com.zebone.platform.modules.mybatis.Mapper;

/**
 * @Description: 医疗规范mapper
 * @author wangpeng
 * @date 2016年8月29日
 *
 */
@Mapper
public interface MKMapper {

	/** 根据主键获取 */
	BdSupply getBdSupplyById(String pkSupply);
	
	/** 根据医嘱用法主键获取医嘱用法附加费用列表 */
	List<BdSupplyItem> getBdSupplyItemListBySupplyId(String pkSupply);
	
	/**
	 * 获取当前常用诊断数量
	 * @param params
	 * @return
	 */
	public int getDiagQuantity(Map<String,String> params);
	
	//删除诊断别名
	public void delTermDiagAlias(Map map);
	
	public List<Map<String, Object>> qryTermDiag(Map map);
	
}
