package com.zebone.nhis.ex.nis.pub.dao;

import java.util.List;
import java.util.Map;

import com.zebone.platform.modules.mybatis.Mapper;

@Mapper
public interface PatiListMapper {
    /**
     * 根据病区获取在院患者列表
     * @param map
     * @return
     */
	public List<Map<String,Object>> queryPatiListByDept(Map<String,Object> map) ;
	
	 /**
     * 根据病区获取转科患者列表
     * @param map
     * @return
     */
	public List<Map<String,Object>> queryTransPiListByDeptNs(Map<String,Object> map) ;
}
