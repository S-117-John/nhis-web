package com.zebone.nhis.pro.zsba.bl.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.zebone.platform.modules.exception.BusException;
import com.zebone.platform.modules.mybatis.Mapper;

/**
 * 医技执行接口服务(博爱版)
 * 
 * @author zhangtao
 * 
 */
@Mapper
public interface ZsbABlMedicalExeMapper {
	/**
	 * 住院医技申请单查询
	 * 
	 * @param paramMap
	 * @return
	 * @throws BusException
	 */
	public List<Map<String, Object>> qryIpMedAppInfo(Map<String, Object> paramMap) throws BusException;

	public List<Map<String, Object>> qryIpMedBlDtInfo(@Param("pkCnord")String pkCnord,@Param("pkExocc")String pkExocc);
	
	//查询负金额的医嘱信息
	public List<Map<String, Object>> qryIpNegativeAnountOrder(Map<String, Object> paramMap);
}
