package com.zebone.nhis.cn.ipdw.dao;

import java.util.List;
import java.util.Map;

import com.zebone.nhis.cn.ipdw.vo.CnIpPressVO;
import com.zebone.nhis.cn.ipdw.vo.CnOrderVO;
import com.zebone.platform.modules.mybatis.Mapper;

@Mapper
public interface CnIpPresMapper {
	/**
	 * 获取一个患者的处方
	 * @param pkPv
	 * @return
	 */
	public List<CnIpPressVO> qryCnIpPres(String pkPv);
	
	/**
	 * 查询一张处方下的明细
	 * @param paramMap
	 * @return
	 */
	public List<CnOrderVO> qryCnIpPresOrd(Map<String, Object> paramMap);
	
	/**
	 * 查询模板明细（只查药品细目）
	 * @param paramMap
	 * @return
	 */
	public List<Map<String,Object>> qryPresOrdSetDt(Map<String, Object> paramMap);
	
	/**
	 * 查询处方复制医嘱（只查药品）
	 * @param paramMap
	 * @return
	 */
	public List<Map<String,Object>> qryPresOrdCopy(Map<String, Object> paramMap);

	/**
	 * 查询常用处方（只查药品）
	 * @param paramMap
	 * @return
	 */
	public List<Map<String,Object>> qryPresOrdUse(Map<String, Object> paramMap);
	/***
	 * @Description 查询药品的仓库默认单位
	 * @auther wuqiang
	 * @Date 2020-07-23
	 * @Param [paramMap]
	 * @return java.util.List<java.util.Map<java.lang.String,java.lang.Object>>
	 */
    List<Map<String, Object>> getOrdStoreUnit(Map<String, Object> paramMap);
}
