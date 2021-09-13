package com.zebone.nhis.sch.hd.dao;

import java.util.List;
import java.util.Map;

import com.zebone.nhis.sch.hd.vo.SchBedVo;
import com.zebone.platform.modules.mybatis.Mapper;

/**
 * 透析排班
 * @author leiminjian
 *
 */
@Mapper
public interface HdSchMapper {

	/**
	 * 查询排班信息
	 * @param map
	 * @return
	 */
	public List<Map<String,Object>> queryDialysisSchHd(Map<String,Object> map);
	
	/**
	 * 查询能否取消排班
	 * @param map
	 * @return
	 */
	public List<Map<String,Object>> queryCancleSchHd(Map<String,String> map);
	
	/**
	 * 查询能否恢复排班
	 * @param map
	 * @return
	 */
	public List<Map<String,Object>> queryRecoverySchHd(Map<String,String> map);
	
	/**
	 * 查询患者排班信息
	 * @param map
	 * @return
	 */
	public List<SchBedVo> querySchHd(Map<String,String> map);
	
	/**
	 * 查询符合患者要求的可被排班床位信息-没有任何患者信息时
	 * @param map
	 * @return
	 */
	public List<SchBedVo> queryHdbedIsNull(Map<String,String> map);
	
	/**
	 * 查询符合要求的患者信息-没有任何排班信息时
	 * @param map
	 * @return
	 */
	public List<SchBedVo> queryPiHdIsNull(Map<String,String> map);
	
	//查询排班
	public List<Map<String,Object>> querySchHdBusiness(Map<String,Object> map);
}
