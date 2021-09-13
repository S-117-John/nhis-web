package com.zebone.nhis.sch.pub.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.zebone.nhis.common.module.sch.plan.SchPlanEmp;
import com.zebone.nhis.common.module.sch.plan.SchPlanweekPvtype;
import com.zebone.nhis.common.module.sch.pub.SchResource;
import com.zebone.nhis.common.module.sch.pub.SchResourceDt;
import com.zebone.nhis.common.module.sch.pub.SchTicketrules;
import com.zebone.nhis.sch.pub.vo.SchPlanVo;
import com.zebone.nhis.sch.pub.vo.SchPlanWeekExt;
import com.zebone.nhis.sch.pub.vo.SchResourceVo;
import com.zebone.nhis.sch.pub.vo.SchSrvOrdSearchParam;
import com.zebone.platform.modules.mybatis.Mapper;

@Mapper
public interface SchBaseMapper {
	
	int SchSrvCheckExist(Map<String,Object> params);
	
	SchTicketrules getSchTicketrulesByPk(@Param("pkTicketrules") String pkTicketrules);
	
	List<SchSrvOrdSearchParam> searchSchSrvOrd(Map<String,String> params);
	
	List<Map<String, Object>> searchSchSrvOrdToMap(Map<String,String> params);

	SchPlanVo getSchPlanByPk(@Param("pkSchplan") String pkSchplan);
	
	List<SchPlanEmp> getSchPlanEmpsByPkSchplan(@Param("pkSchplan") String pkSchplan);
	
	List<SchPlanWeekExt> getSchPlanWeeksByPkSchplan(@Param("pkSchplan") String pkSchplan);
	
	List<SchPlanweekPvtype> getSchPlanWeekPvtypelistByPkPlanWeek(Map<String,Object> params);
	/**
	 * 查询诊疗排班日期分组明细
	 * @param params
	 * @return
	 */
	public List<SchResourceDt> getResouceDts(Map<String,String> params);
   /**
    * 根据日期分组类型编码，查询日期分组明细
    * @param params
    * @return
    */
	public List<SchResourceDt> getDateSlot(Map<String,String> params);
	/**
	 * 查询排班资源信息
	 * @param params
	 * @return
	 */
	public List<SchResourceVo> querySchResourceList(Map<String,String> params);
	
	String queryDtDateslottype(String pkSchres);

	/**
	 * 校验服务与医技项目为多对一的关系
	 * @param params
	 * @return
	 */
	List<Map<String, Object>> checkRepeatPk(Map<String, Object> params);

	/**
	 * 查询资源主信息（子节点）
	 * @param paramMap
	 * @return
	 */
	public List<SchResource> getSimpleSchResource(Map<String, Object> paramMap);

	/**
	 * /**
	 * 查询资源主信息(父节点)
	 * @param paramMap
	 * @return
	 */
	List<SchResource> getSimpleSchResourceFather(Map<String, Object> paramMap);

}
