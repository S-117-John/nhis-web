package com.zebone.nhis.pv.pub.dao;

import com.zebone.nhis.common.module.pi.PiMaster;
import com.zebone.nhis.common.module.pv.PvEncounter;
import com.zebone.nhis.common.module.sch.plan.SchTicket;
import com.zebone.platform.modules.mybatis.Mapper;

/**
 * 挂号相关dao接口
 * 
 * @author wangpeng
 * @date 2016年9月10日
 *
 */
@Mapper
public interface RegPubMapper {
	
	/** 根据主键获取就诊记录 */
	public PvEncounter getPvEncounterByPkPv(String pkPv);
	
	/** 根据患者主键查询患者信息（不包含照片） */
	public PiMaster getPiMasterNoPhoto(String pkPi);
	
	/** 根据患者主键获取最大的就诊次数(门诊) */
	public Integer getMaxOpTimes(String pkPi);
	
	/** 根据排班主键获取可使用的最小的票号 */
	public SchTicket getMinNoTicketnoByPkSch(String pkSch,String pkOrg);
	
	/** 根据排班主键获取可使用的最小的票号（sqlServer） */
	public SchTicket getMinNoTicketnoByPkSchForSql(String pkSch,String pkOrg);
	
	/** 根据患者主键查询未结算或欠费患者 2018-11-26*/
	public Integer getArrearsPi(String pkPi);
	
	/** 根据患者主键获取最大的就诊次数(住院) */
	public Integer getMaxIpTimes(String pkPi);

	Integer  getMaxOpTimesFromPiMaster(String pkPi);
}
