package com.zebone.nhis.ma.lb.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.zebone.nhis.common.module.pi.PiAllergic;
import com.zebone.nhis.common.module.pi.PiCard;
import com.zebone.nhis.common.module.pi.PiMaster;
import com.zebone.nhis.common.module.pi.acc.PiCardIss;
import com.zebone.nhis.common.module.pv.PvDiag;
import com.zebone.nhis.common.module.pv.PvEncounter;
import com.zebone.nhis.ma.lb.vo.PvOpVo;
import com.zebone.nhis.pi.pub.vo.PibaseVo;
import com.zebone.platform.modules.mybatis.Mapper;

/***
 * 患者相关mapper
 *
 */
@Mapper
public interface SelfMsgMapper {
	
	/** 获取患者基本信息列表(不含照片) */
	List<Map<String,Object>> getPiMasterListNoPhoto(PiMaster piMaster);
	
	/**
	 * 自助机排班
	 * @param paramMap
	 * @return
	 */
	List<Map<String, Object>> qrySchedulInfo(Map<String, Object> paramMap);

	/**
	 * 根据排班主键得到相关信息
	 * @param pkSch
	 * @return
	 */
	Map<String, Object> getPlanInfo(@Param("key")String key);

	/**
	 * 门诊查询患者
	 * @param pvEncounter
	 * @return
	 */
	List<PibaseVo> getOpPiMaster(PvEncounter pvEncounter);
	
	/** 根据患者主键获取患者当前挂号记录  Oracle*/
	List<PvOpVo> getPvOpVoTodayListOracle(@Param("pkPi")String pkPi,@Param("pkOrg")String pkOrg);
}
