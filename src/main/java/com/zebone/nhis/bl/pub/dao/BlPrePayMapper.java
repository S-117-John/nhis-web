package com.zebone.nhis.bl.pub.dao;

import java.util.Map;

import com.zebone.nhis.common.module.bl.BlDeposit;
import com.zebone.platform.modules.mybatis.Mapper;

@Mapper
public interface BlPrePayMapper {
	
	/**
	 * 预交金票据的数据
	 * @param 
	 * @return
	 */
	public Map<String,Object> getDepositFormData(Map<String,Object> paramMap);
	
	/**
	 * 重打预交金票据的数据
	 * @param 
	 * @return
	 */
	public Map<String,Object> getDepositFormDataWham(Map<String,Object> paramMap);
	
	/**
	 * 查询预交金可退金额
	 * @param paramMap
	 * @return
	 */
	public BlDeposit qryDepoBackAmt(Map<String,Object> paramMap);

}
