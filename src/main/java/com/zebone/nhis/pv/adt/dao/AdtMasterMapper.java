package com.zebone.nhis.pv.adt.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.zebone.nhis.common.module.bl.BlIpDt;
import com.zebone.nhis.pi.pub.vo.PiMasterAndAddr;
import com.zebone.platform.modules.mybatis.Mapper;

/**
 * 入院患者管理mapper
 * 
 * @author wangpeng
 * @date 2016年12月12日
 *
 */
@Mapper
public interface AdtMasterMapper {
	
	/** 获取住院费用 */
	List<BlIpDt> getBlIpDtListByPkPv(String pkPv);
	
	
	List<PiMasterAndAddr> getRepeatPatientList(Map<String, String> params);
}
