package com.zebone.nhis.ex.nis.pub.dao;

import java.util.List;
import java.util.Map;

import com.zebone.nhis.common.module.base.ou.BdOuEmployee;
import com.zebone.nhis.ex.pub.vo.PatiCardVo;
import com.zebone.platform.modules.mybatis.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface PatiBedMapper {
	/**
	 * 查询患者就诊及费用信息
	 * @param paramMap
	 * @return
	 */
	public List<PatiCardVo> getBedInfo(Map<String,Object> paramMap);

	/**
	 * getBedInfo方法拆分的子查询
	 *
	 * @return List<Map < String, Object>>
	 */
	List<Map<String, Object>> getBedInfoChildSearch(List<String> listPkPv);

	 /**
     * 查询患者在途费用明细列表
     * @param paramMap
     * @return
     */
	public List<Map<String,Object>> getZtFeeDetailByPv(Map<String,Object> paramMap);
	 /**
     * 查询患者固定费用明细列表
     * @param paramMap
     * @return
     */
	public List<Map<String,Object>> getGdFeeDetailByPv(Map<String,Object> paramMap);
	/**
	 * 查询包床退包床床位
	 * @param paramMap
	 * @return
	 */
	public List<Map<String,Object>> queryPvPackBed(Map<String,Object> paramMap);

	/**
	 * 查询患者医护人员信息
	 * @param paramMap
	 * @return
	 */
	public List<PatiCardVo> getPiStafInfoByBed(String pkPv);

	/**
	 * 查询床位责任护士
	 * @param paramMap
	 * @return
	 */
	public List<Map<String,Object>> getEmpNsByBed(Map<String,Object> paramMap);
	/**
	 * 查询包含附加收费项目的
	 * @param paramMap
	 * @return
	 */
	public List<Map<String,Object>> queryBedContainsAdditem(Map<String,Object> paramMap);

	/**
	 * 查询包含包床费用的子表
	 * @param paramMap
	 * @return
	 */
	public List<Map<String, Object>> queryItemBedContainsAdditem(Map<String, Object> paramMap);

	/**
	 * 查询类型为0303的打印模板
	 * @return
	 */
	public List<Map<String, Object>> getPrintList();
}
