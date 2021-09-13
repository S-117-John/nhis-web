package com.zebone.nhis.scm.st.dao;

import java.util.List;
import java.util.Map;

import com.zebone.nhis.scm.pub.vo.PuOrderDtVo;
import com.zebone.nhis.scm.pub.vo.PuOrderVo;
import com.zebone.nhis.scm.pub.vo.PdStDtVo;
import com.zebone.nhis.scm.pub.vo.PdStVo;
import com.zebone.platform.modules.mybatis.Mapper;

@Mapper
public interface PdInStoreMapper {
   /***
	 * 查询待入库单
	 * @param map
	 * @return
	 */
	public List<PdStVo> queryToInPdStList(Map<String,Object> map);
	/***
	 * 查询待入库明细
	 * @param map
	 * @return
	 */
	public List<PdStDtVo> queryToInPdStDtList(Map<String,Object> map);
	/***
	 * 查询入库单
	 * @param map
	 * @return
	 */
	public List<PdStVo> queryInPdStList(Map<String,Object> map);
	
}
