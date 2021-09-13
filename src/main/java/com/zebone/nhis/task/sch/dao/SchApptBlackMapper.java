package com.zebone.nhis.task.sch.dao;

import java.util.List;
import java.util.Map;

import com.zebone.platform.modules.mybatis.Mapper;

@Mapper
public interface SchApptBlackMapper {

	
	/**
	 * 获取生成黑名单爽约记录
	 * @param params
	 * @return
	 */
	List<Map<String, Object>> genApplyBlacklist(Map params);
	/**
	 * 获取生成黑名单迟到记录
	 * @param params
	 * @return
	 */
	List<Map<String, Object>> genApplyBlackLatelist(Map params);

	
	/**
     * 口腔医院查看预约记录（黑名单用）
     * @param params
     * @return
     */
    List<Map<String, Object>> genApplyBlackPskqlist(Map params);
    
    /**
     * 口腔医院查看锁定记录（黑名单解锁用）
     * @param params
     * @return
     */
    List<Map<String, Object>> genApplyUnBlackPskqlist(Map params);
    

}
