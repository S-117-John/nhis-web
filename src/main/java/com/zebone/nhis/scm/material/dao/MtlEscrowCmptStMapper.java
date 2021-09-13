package com.zebone.nhis.scm.material.dao;

import java.util.List;
import java.util.Map;

import com.zebone.nhis.scm.material.vo.MtlPdStDtQryVo;
import com.zebone.nhis.scm.material.vo.MtlPdStDtVo;
import com.zebone.nhis.scm.material.vo.MtlPdStVo;
import com.zebone.platform.modules.mybatis.Mapper;

@Mapper
public interface MtlEscrowCmptStMapper {
	/***
	 * 查询待结算记录
	 * @param map
	 * @return
	 */
	public List<MtlPdStVo> queryPdStTdList(Map<String,Object> map);
	
	/***
	 * 已结算记录信息
	 * @param map
	 * @return
	 */
	public List<MtlPdStVo> queryPdStDnList(Map<String,Object> map);
	
	
	/***
	 * 查询待结算明细记录
	 * @param map
	 * @return
	 */
	public List<MtlPdStDtVo> queryPdStTdDetail(Map<String,Object> map);

	
   /***
	 * 查询已结算明细记录
	 * @param map
	 * @return
	 */
	public List<MtlPdStDtVo> queryPdStDnDetail(Map<String,Object> map);	
	
}

	
	
