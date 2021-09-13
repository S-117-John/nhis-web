package com.zebone.nhis.scm.st.dao;

import java.util.List;
import java.util.Map;

import com.zebone.nhis.scm.pub.vo.PuOrderDtVo;
import com.zebone.nhis.scm.pub.vo.PuOrderVo;
import com.zebone.nhis.scm.pub.vo.PdStDtVo;
import com.zebone.nhis.scm.pub.vo.PdStVo;
import com.zebone.platform.modules.mybatis.Mapper;

@Mapper
public interface InvoiceCheckMapper {
   /***
	 * 查询采购入库单
	 * @param map
	 * @return
	 */
	public List<PdStVo> queryPdPuStList(Map<String,Object> map);
	
	/***
	* 查询采购入库单明细
	* @param map
	* @return
	*/
	public List<PdStDtVo> queryPdPuStDtList(Map<String,Object> map);
	/***
	* 查询已验收列表
	* @param map
	* @return
	*/
	public List<Map<String,Object>> queryPdInvoiceList(Map<String,Object> map);
	/***
	* 查询已验收明细列表
	* @param map
	* @return
	*/
	public List<PdStDtVo> queryPdInvoiceDtList(Map<String,Object> map);
}
