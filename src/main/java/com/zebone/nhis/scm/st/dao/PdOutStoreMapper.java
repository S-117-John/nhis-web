package com.zebone.nhis.scm.st.dao;

import java.util.List;
import java.util.Map;

import com.zebone.nhis.scm.pub.vo.PdPlanDtVo;
import com.zebone.nhis.scm.pub.vo.PdPlanVo;
import com.zebone.nhis.scm.pub.vo.PdStDtVo;
import com.zebone.nhis.scm.pub.vo.PdStVo;
import com.zebone.platform.modules.mybatis.Mapper;

@Mapper
public interface PdOutStoreMapper {
   /***
	 * 查询待出库库单
	 * @param map
	 * @return
	 */
	public List<PdPlanVo> queryToOutPdStList(Map<String,Object> map);
	/**
	 * 查询待出库明细
	 * @param map
	 * @return
	 */
	public List<PdPlanDtVo> queryToOutPdStDtList(Map<String,Object> map);
	
	/**
	 * 查询出库单
	 * @param map
	 * @return
	 */
	public List<PdStVo> queryPdStOutList(Map<String,Object> map);
	/**
	 * 查询出库明细
	 * @param map
	 * @return
	 */
	public List<PdStDtVo> queryPdStDtOutList(Map<String,Object> map);
	
}
