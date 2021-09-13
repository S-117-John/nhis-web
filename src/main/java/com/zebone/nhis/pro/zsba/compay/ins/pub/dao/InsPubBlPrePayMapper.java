package com.zebone.nhis.pro.zsba.compay.ins.pub.dao;

import java.util.Map;

import com.zebone.platform.modules.mybatis.Mapper;

@Mapper
public interface InsPubBlPrePayMapper {
	
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

}
