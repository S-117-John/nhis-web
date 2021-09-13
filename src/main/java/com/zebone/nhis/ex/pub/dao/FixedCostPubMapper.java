package com.zebone.nhis.ex.pub.dao;

import java.util.List;
import java.util.Map;

import com.zebone.nhis.ex.pub.vo.PvBedVo;
import com.zebone.platform.modules.mybatis.Mapper;

@Mapper
public interface FixedCostPubMapper {
    /**
     * 查询固定费用
     * @param map{pkPvs}
     * @return
     */
	public List<Map<String,Object>> queryFixedCharge(Map<String,Object> map) ;
	 /**
     * 查询就诊床位及对应费用
     * @param map{pkPv,dateEnd}
     * @return
     */
	public List<Map<String,Object>> queryPvBedItem(Map<String,Object> map) ;
	
	/**
     * 查询就诊床位及对应费用--博爱使用费用子表维护床位费版本
     * @param map{pkPv,dateEnd}
     * @return
     */
	public List<Map<String,Object>> queryPvBedItems(Map<String,Object> map) ;
	 /**
     * 查询就诊虚床位记录
     * @param map{pkPv,dateEnd}
     * @return
     */
	public List<PvBedVo> queryPvVirtualBedItem(Map<String,Object> map) ;
	
	
}
