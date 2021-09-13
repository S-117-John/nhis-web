package com.zebone.nhis.webservice.dao;
import com.zebone.nhis.webservice.vo.BdItemAttrVo;
import com.zebone.nhis.webservice.vo.BdSerialNoWs;
import com.zebone.nhis.webservice.vo.ItemPriceVo;
import com.zebone.nhis.webservice.vo.deptvo.DeptTypesVo;
import com.zebone.platform.modules.mybatis.Mapper;

import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;
@Mapper
public interface BdPubForWsMapper {
	/**
	 * 查询费用分类
	 * @return
	 */
	public List<Map<String,Object>> getItemCateInfo(Map<String,Object> paramMap);
	
	/**
	 * 查询系统所有启用状态的机构信息
	 * @return
	 */
	public List<Map<String,Object>> LbqueryOrgs();
	/**
	 * 查询系统所有机构信息
	 * @param map flagActive:启用状态  delFlag：删除标志  
	 * @return
	 */
	public List<Map<String,Object>> queryOrg(Map<String,Object> map);
	/**
	 * 查询机构院区信息
	 * @param map pkOrg:机构唯一标识 
	 * @return 
	 */
	public List<Map<String,Object>> queryOrgArea(Map<String,Object> map);
	/**
	 * 查询当前机构下所有科室信息
	 * @param map pkOrg:机构唯一标识  codeOrg：机构编码
	 * @return 
	 */
	public List<Map<String,Object>> queryDept(Map<String,Object> map);
	List<Map<String,Object>> queryAllDept(Map<String,Object> map);
	/**
	 * 查询所属科室下医生信息
	 * @param map pkDept：科室唯一标识   codeDept：科室编码(二选一)   flagActive :启用状态 0 停用 1启用   delFlag：删除标志 0 未删除 1删除
	 * @return
	 */
	public List<Map<String,Object>> queryEmployee(Map<String,Object> map);
	List<Map<String,Object>> queryAllDoctor(Map<String,Object> map);
	List<Map<String,Object>> queryAllEmployee(Map<String,Object> map);
	/**
	 * 查询内部医保信息
	 * @return
	 */
	public List<Map<String,Object>> getHpInfo(Map<String,Object> paramMap);
	
	public Double selectSn(@Param("tableName") String tableName, @Param("fieldName") String fieldName); 

	public int initSn(BdSerialNoWs initSn);
	public List<ItemPriceVo> LbgetBdItemInfo(String pkSchsrv);

	/**
	 * 查询科室类型属性
	 * @param
	 * @return
	 */
	public List<DeptTypesVo> getDeptTypes(List<String> list);
	/**
	 * 查询医生信息-lb便民平台
	 * @return
	 */
	public List<Map<String,Object>> LbgetOuEmployee(Map<String,Object> paramMap);
	/**获取收费项目扩展属性模板*/
	List<BdItemAttrVo> getBdItemAttrtemp(@Param("pkOrg") String pkOrg);
}
