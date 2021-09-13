package com.zebone.nhis.scm.material.dao;

import java.util.List;
import java.util.Map;

import com.zebone.nhis.scm.material.vo.MtlPdStDtQryVo;
import com.zebone.nhis.scm.material.vo.MtlPdStDtVo;
import com.zebone.nhis.scm.material.vo.MtlPdStVo;
import com.zebone.platform.modules.mybatis.Mapper;

@Mapper
public interface MtlReceiptAcceptMapper {
	/***
	 * 查询待验收采购入库单
	 * @param map
	 * @return
	 */
	public List<MtlPdStVo> queryPdStToApt(Map<String,Object> map);
	
	/***
	 * 查询待验收发票明细
	 * @param map
	 * @return
	 */
	public List<MtlPdStDtVo> queryPdStToAptDetail(Map<String,Object> map);
	
	
	 /***
	 * 查询已验收发票信息
	 * @param map
	 * @return
	 */
	public List<MtlPdStDtQryVo> queryPdStApted(Map<String,Object> map);
	
   /***
	 * 查询已验收发票明细
	 * @param map
	 * @return
	 */
	public List<MtlPdStDtVo> queryPdStAptedDetail(Map<String,Object> map);	
	
}

	
	
