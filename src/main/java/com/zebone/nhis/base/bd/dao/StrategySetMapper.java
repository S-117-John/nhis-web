package com.zebone.nhis.base.bd.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.zebone.nhis.common.module.base.bd.code.InsGzgyHpDiv;
import com.zebone.nhis.common.module.base.bd.price.InsGzgyDivBed;
import com.zebone.nhis.common.module.base.bd.price.InsGzgyDivHvitem;
import com.zebone.nhis.common.module.base.bd.price.InsGzgyDivSpitem;
import com.zebone.platform.modules.mybatis.Mapper;

@Mapper
public interface StrategySetMapper {
	
	/**查询床位费策略*/
	List<InsGzgyDivBed> qryDivBed(Map<String,Object> paramMap);
	
	/**查询高值耗材策略*/
	List<InsGzgyDivHvitem> qryDivHvitem(Map<String,Object> paramMap);
	
	/**查询特殊项目策略*/
	List<InsGzgyDivSpitem> qryDivSpitem(Map<String,Object> paramMap);
	
	/**根据策略主键查询关联的医保信息*/
	List<InsGzgyHpDiv> qryDivHpByPkdiv(@Param("euDivtype")String euDivtype,@Param("pkDiv") String pkDiv);
	
	/**查询待选公费医保信息*/
	List<InsGzgyHpDiv> qryWaitChooseHp(Map<String,Object> paramMap);
	
	/**查询已选公费医保信息*/
	List<InsGzgyHpDiv> qrySelectedHp(Map<String,Object> paramMap);
	
	/**批量删除床位费策略*/
	int batchDelDivBed(List<String> delPks);
	
	/**批量删除特殊项目策略*/
	int batchDelDivSpitem(List<String> delPks);
	
	/**批量删除高值策略*/
	int batchDelDivHvitem(List<String> delPks);
	
	/**批量删除关联医保信息*/
	int batchDelDivHp(List<String> delPks);
}
