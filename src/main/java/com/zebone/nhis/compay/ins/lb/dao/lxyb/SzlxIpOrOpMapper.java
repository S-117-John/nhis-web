package com.zebone.nhis.compay.ins.lb.dao.lxyb;

import java.util.List;
import java.util.Map;

import com.zebone.platform.modules.mybatis.Mapper;

@Mapper
public interface SzlxIpOrOpMapper {
	/**
	 *  根据就诊主键,去查询住院/门诊登记数据
	 * @param map
	 * @return
	 */
	public List<Map<String,Object>> getHpAdmit(Map<String,Object> map);
	
	/**
	 *  根据就诊主键,去查询住院/门诊登记数据
	 * @param map
	 * @return
	 */
	public List<Map<String,Object>> getHpAdmitByDjdm(Map<String,Object> map);
	
	/**
	 * 根据pkSettle 查询结算返回信息 INS_SZLX_JS
	 * @param map
	 * @return
	 */
	public Map<String,Object> getYbJsData(Map<String,Object> map);
	
	/**
	 * 查询宿州离休与nhis匹配项目{"pkHp":"医保主键","euMatch":"1(匹配)，2（未匹配）"}
	 * @param map
	 * @return
	 */
	public List<Map<String,Object>> qryLxItemWithInfo(Map<String,Object> map);
	
	/**
	 * 查询宿州离休与nhis匹配药品{"pkHp":"医保主键","euMatch":"1(匹配)，2（未匹配）"}
	 * @param map
	 * @return
	 */
	public List<Map<String,Object>> qryLxPdWithInfo(Map<String,Object> map);
	
	/**
	 * 根据pk_pv 查询离休费用明细：用于撤销部分上传的费用明细
	 * @param map
	 * @return
	 */
	public List<Map<String,Object>> qryLxFymx(Map<String,Object> map);
}
