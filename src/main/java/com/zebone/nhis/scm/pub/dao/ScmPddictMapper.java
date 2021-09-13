package com.zebone.nhis.scm.pub.dao;

import java.util.List;
import java.util.Map;

import com.zebone.platform.modules.mybatis.Mapper;

/**
 * 供应链字典Mapper
 * @author lijipeng
 *
 */
@Mapper
public interface ScmPddictMapper {
	
	/**查询药品信息*/
	public List<Map<String,Object>> searchBdPds(Map<String,Object> paramMap);
	
	/**获取药品限制使用信息*/
	public List<Map<String,Object>> searchPdRestByPkPd(Map<String,Object> paramMap);
	
	/**批量删除药品限制使用信息*/
	public int batchDelBdPdRest(List<String> pkPdRests);
	
	/**
	 * 获取包装信息（药品计量单位）
	 * @param pkPd
	 * @return
	 */
	public List<Map<String,Object>> qryPdStorePack(String pkPd);
	
	/**
	 * 查询当前仓库下的物品出入库记录 
	 */
	public List<Map<String,Object>> searchPdStRecord(Map<String,Object> param);

	/**
	 * 根据药品主键查询药品的仓库信息
	 * @param pkPd
	 * @return
	 */
	public List<Map<String,Object>> qryPdStorePackInfo(String pkPd);

	/**
	 * 查询药品字典的停用恢复信息
	 * @param pkPd
	 * @return
	 */
	public List<Map<String,Object>> qryPdStopLogInfo(String pkPd);
}
