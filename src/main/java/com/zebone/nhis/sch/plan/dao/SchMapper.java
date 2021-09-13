package com.zebone.nhis.sch.plan.dao;

import java.util.Date;
import java.util.List;
import java.util.Map;

import com.zebone.nhis.webservice.vo.ticketvo.Data;
import org.apache.ibatis.annotations.Param;

import com.zebone.nhis.common.module.base.bd.code.BdWorkcalendardate;
import com.zebone.nhis.common.module.sch.plan.SchPlanEmp;
import com.zebone.nhis.common.module.sch.plan.SchPlanweekPvtype;
import com.zebone.nhis.common.module.sch.plan.SchSch;
import com.zebone.nhis.sch.plan.vo.SchExclude;
import com.zebone.nhis.sch.plan.vo.SchPlanWithWeek;
import com.zebone.nhis.sch.plan.vo.SchSchVo;
import com.zebone.platform.modules.mybatis.Mapper;

@Mapper
public interface SchMapper {
	
	/**
	 * 获取当天一天的所有排班
	 * @param rescode
	 * @param pkOrg
	 * @param nowdate
	 * @return
	 */
	List<Map<String, Object>> getTodaySchInfos(@Param("rescode") String rescode, @Param("pkOrg") String pkOrg, @Param("nowdate") String nowdate,@Param("pkSchres") String pkSchres);
	
	/**
	 * 获取当前可用的排班资源
	 * @param rescode
	 * @param pkOrg
	 * @param nowdate
	 * @return
	 */
	List<Map<String, Object>> getTodaySchInfosByDate(@Param("rescode") String rescode, @Param("pkOrg") String pkOrg,
			@Param("nowDate") String nowdate,@Param("nowDay") String nowDay,
			@Param("nowTime") String nowTime,@Param("pkSchres") String pkSchres);
	
/*	List<Map<String, Object>> getTodaySchInfosOracle(@Param("rescode") String rescode, @Param("pkOrg") String pkOrg, @Param("nowdate") String nowdate);
*/
	List<SchSchVo> getSchInfo(Map<String,String> params);

	List<SchSchVo> getSchInfoZs(Map<String,String> params);
	
	int deleteSchSch(@Param("pkSchs")List<String> pkSchs,@Param("ts")Date ts,@Param("pkEmp")String pkEmp,@Param("modityTime")Date modityTime);
	
	int deleteSchEmp(@Param("pkSchs")List<String> pkSchs,@Param("ts")Date ts,@Param("pkEmp")String pkEmp,@Param("modityTime")Date modityTime);
	
	int deleteSchPvtype(@Param("pkSchs")List<String> pkSchs,@Param("ts")Date ts,@Param("pkEmp")String pkEmp,@Param("modityTime")Date modityTime);
	
	int deleteSchTicket(@Param("pkSchs")List<String> pkSchs,@Param("ts")Date ts,@Param("pkEmp")String pkEmp,@Param("modityTime")Date modityTime);

	int deleteSchEmpByPkSch(@Param("pkSch")String pkSch);
	
	int deleteSchPvtypeByPkSch(@Param("pkSch")String pkSch);

	int updateSchTicketnoType(@Param("pkSch")String pkSch, @Param("euPvtype")String euPvtype, @Param("ticketnos")List<String> ticketnos);

	List<SchPlanWithWeek> getSchplanWithWeek(Map<String,String> params);

	int deleteSchSchByPlanWeeks(Map<String, Object> prms);

	/*int deleteSchSchByPlanWeeksOracle(Map<String, Object> prms);*/
	
	List<Date> getSchSchDateworks(Map<String, Object> params);

	List<SchPlanEmp> getSchPlanEmpByPkSchplan(String pkSchplan);

	List<SchPlanweekPvtype> getSchPlanweekPvtypeByPkSchplanweek(String pkPlanweek);

    List<SchSch> listAll();
	
    List<BdWorkcalendardate> getWordcalendardate(Map<String, Object> params);
    
    /**
     * 依据排班主键，修改排班审核状态
     * @param sch 要修改的目标状态以及其他信息
     * @param originalStatus 原始的状态
     * @param listPkSch 主键列表
     * @return
     */
    int updateEuStatus(@Param("sch")  SchSch sch,@Param("originalStatus") String originalStatus,@Param("pkSchs") List<String>  listPkSch);
    /**
     * 
     * @param pkSch
     * @return
     */
    int getSchTicketSum(@Param("pkSch")String pkSch);

    /**根据资源主键查询是否有排班记录*/
	List<SchSchVo> qrySchByPkSchSrv(Map<String,String> params);

	List<String> getSchOfApptGroup(Map<String,Object> params);

	List<SchSchVo> getSchOfApptInfo(Map<String,Object> params);
	
	List<Map<String,Object>> getSchstopworks(@Param("pkEmp")String pkEmp);
	
	//通过排班计划获取午别标识
	String getEuNoonByPlan(Map<String, String> params);
	
	//通过排班资源获取午别标识
	String getEuNoonByResource(Map<String, String> params);

}
