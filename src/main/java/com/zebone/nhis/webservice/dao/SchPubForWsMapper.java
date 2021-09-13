package com.zebone.nhis.webservice.dao;

import java.util.List;
import java.util.Map;

import com.zebone.nhis.webservice.pskq.model.ExOpSchVo;
import com.zebone.nhis.webservice.vo.LbSHRequestVo;
import com.zebone.nhis.webservice.vo.PskqSchApptVo;
import com.zebone.platform.modules.mybatis.Mapper;

@Mapper
public interface SchPubForWsMapper {
	/**
	 * 获取当前可用的排班资源
	 * @return
	 */
	List<Map<String, Object>> LbTodaySchInfosByDate(Map<String,Object> paramMap);
	
	/**
	 * 获取可用的预约排班
	 * @param paramMap
	 * @return
	 */
	List<Map<String, Object>> PskqTodaySchInfos(Map<String,Object> paramMap);
	
	//查询未使用号源数
	int getSchTicketNotUsedSum(Map<String,Object> paramMap);
		
	public List<Map<String,Object>> querySchInfo(Map<String,Object> map);
	
	/**
	 * 查询排班资源信息
	 * @param paramMap{pkSchres}
	 * @return
	 */
	public Map<String,Object> querySchResInfo(Map<String,Object> paramMap);
	/**
	 * 查询预约号源
	 * @param
	 * @return
	 */
	public List<Map<String,Object>>  QueryRegisteredRecords(Map<String,Object> paramMap);
		
	public List<Map<String, Object>> LbgetSchPlanInfo(Map<String,Object> paramMap);
	
	public List<Map<String,Object>> getRegistered(Map<String,Object> paramMap);
	//lb查询预约记录-编民平台
	public List<Map<String,Object>> LbgetSchAppt(Map<String,Object> paramMap);
	//lb-便民平台-排班时间分段查询
	public List<Map<String,Object>> querySchAppSec(Map<String,Object> paramMap);
	//查询预约号源信息
	public List<Map<String,Object>> querySchAppTicket(Map<String,Object> paramMap);
	
	
	/***
	 * 查询预约号源信息按时间段分组-灵璧
	 * @param pkSch
	 * @return
	 */
	public List<Map<String,Object>> getTicketsGroupDate(String pkSch);
	
	/**
	 * 微信公众号查询号源信息 -灵璧
	 * @param paramMap
	 * @return
	 */
	public List<Map<String,Object>> queryTicketsBySchAndTimeList(Map<String,Object> paramMap);
	//更新号表状态，根据号表主键
	public int updateSchTicketFlagUsed(Map<String,Object> paramMap);
	//更新号表状态 ，预约挂号时根据pksch、ticketno条件进行更新
	public int updateSchTicketAppt(Map<String,Object> paramMap);
	//更新号表
	public int updateSchSchTicketNo(String pkSch);
	//根据pkpi,pkSch更新预约状态
	public int updateSchApptEuStatus(Map<String,Object> paramMap);
	//根据PkSchappt更新状态
	public int updateSchApptPkSchappt(Map<String,Object> paramMap);
	//有号表方式，更新排班已使用号数
	public int updateSchCntUsed(String pkSch);
	
	 /**
     * 查询患者挂号记录已缴费的
     * @param params
     * @return
     */
    public List<Map<String,Object>> getRegistRecord(Map<String,Object> params);

	public ExOpSchVo querOpSchHedule(String ordsn);
	
	/**
	 * 获取排班科室信息
	 * @return
	 */
	List<Map<String, Object>> getSchDeptInfo(Map<String,Object> paramMap);
	
	/**
	 * 查询预约记录信息
	 * @param paramMap
	 * @return
	 */
	public List<Map<String,Object>> getApptRegRecords(Map<String,Object> paramMap);
	/**
	 * 根据医生工号查询预约记录
	 * @return
	 */
	List<PskqSchApptVo> searchSchAppt(LbSHRequestVo requ);

	//lbzy-查询排班服务分类和费用
	List<Map<String,Object>> getLbzySchSrv();
}
