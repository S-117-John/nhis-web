package com.zebone.nhis.base.bd.dao;

import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.ibatis.annotations.Param;

import com.zebone.nhis.common.module.base.bd.code.BdCodeDateslot;
import com.zebone.nhis.common.module.base.bd.code.BdReport;
import com.zebone.nhis.common.module.base.bd.code.BdTempPrt;
import com.zebone.platform.modules.mybatis.Mapper;

@Mapper
public interface CodeMapper {
	
	List<BdCodeDateslot> getBdCodeDateslotByPkForType(@Param("pks")Set<String> pks);
	
	int BdWorkcalendarCheckExist(Map<String,String> params);

	int BdWorkcalendruleCheckExist(Map<String, String> params);
	
	int BdCodeDateslotCheckExist(Map<String, String> params);
	
	int BdAdminDivisionCheckExist(Map<String, String> params);

	int BdDefdoclistCheckExist(Map<String, String> params);
	
	BdCodeDateslot selectBdCodeDateslotByPk(String pk);
	

	List<BdCodeDateslot> getBdCodeDateslotForSortNo(String dtDateslottype);

	int batchUpdateBdCodeDateslotSortNo(@Param("bdCodeDateslotList")List<BdCodeDateslot> bdcodelist);
	
	//根据机构和打印模板编码数组删除打印模板
	int deleteBdTempPrtByCodes(@Param("pkOrg")String pkOrg, @Param("codes")String[] codes);
	
	//根据机构和打印模板编码数组查询打印模板列表
	List<BdTempPrt> getBdTempPrtListByCodes(@Param("pkOrg")String pkOrg, @Param("codes")String[] codes);
	/**
	 * 查询用户对应报表列表
	 * @param paramMap(pkUser)
	 * @return
	 */
	public List<String> getReportByUser(Map<String,Object> paramMap);
	
	void delDefdocsByList(List<String> list);
	//日历查询
	List<Map<String, Object>> searchWorkcalendars(@Param("pkOrg") String pkOrg);
	//获取日历规则
	List<Map<String, Object>> searchWorkRules(@Param("pkOrg") String pkOrg,@Param("pkWorkcalendrule") String pkWorkcalendrule);
	//根据日期分组获取时间分段
	List<Map<String, Object>> getDatesSecByDates(@Param("pkOrg") String pkOrg,@Param("pkDateslot") String pkDateslot);
	//根据日期分组获取工作时间
	List<Map<String, Object>> getDatesTimeByDates(@Param("pkOrg") String pkOrg,
			@Param("pkDateslot") String pkDateslot);

	List<Map<String, Object>> getTempPrtList(@Param("params")Map<String,Object> paramMap);

	/**
	 * 查询参数模板下的机构参数清单
	 * @param pkParamTemp
	 * @return
	 */
	List<Map<String, Object>> getOrgListByPkParamTemp(String pkParamTemp);

	/**
	 * 删除机构
	 * @param pkParamTemp
	 */
	void deleteOrgList(String pkParamTemp);

	//查询字典分类是否为系统定义
	String searchDefdoclist(@Param("pkDefdoclist") String pkDefdoclist);
	//查询字典明细是否为系统定义
	String searchDefdoc(@Param("pkDefdoc") String pkDefdoc);

	/**
	 * 查询参数模板下的机构参数清单
	 * @param code
	 * @return
	 */
	List<Map<String, Object>> getOrgListByCodeTemp(@Param("code")String code);
}
 