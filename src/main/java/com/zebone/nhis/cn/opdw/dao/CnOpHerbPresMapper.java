package com.zebone.nhis.cn.opdw.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.zebone.nhis.cn.opdw.vo.CnPresDt;
import com.zebone.nhis.cn.opdw.vo.HerbOrdSetDt;
import com.zebone.nhis.cn.opdw.vo.PiPresInfo;
import com.zebone.nhis.cn.opdw.vo.PiPresInfoDt;
import com.zebone.nhis.common.module.cn.ipdw.BdOrdSet;
import com.zebone.nhis.common.module.cn.ipdw.CnPrescription;
import com.zebone.nhis.common.module.pv.PvDiag;
import com.zebone.platform.modules.mybatis.Mapper;

/**
 * 门诊医生站--草药处方DAO
 * @author Roger
 *
 */
@Mapper
public interface CnOpHerbPresMapper {

	//查询草药处方
	List<CnPrescription> getHerbPres(@Param("pkPv")String pkPv);
	
	//查询草药处方明细
	List<CnPresDt> getHerbPresDt(@Param("pkPres") String pkPres,@Param("pkPdList") List<String> pkPdList);

	//查询草药医嘱模板
	List<BdOrdSet> getOrdSet(Map<String, Object> paramMap);
	
	//查询草药医嘱模板明细
	List<HerbOrdSetDt> getOrdSetDt(Map<String, Object> paramMap);
	
	//根据就诊主键查询患者就诊_临床综合诊断信息列表 */
	List<PvDiag> getPvDiagListByPkPv(@Param("pkPv")String pkPv);

	List<PiPresInfo> getCopyPres(PiPresInfo para);
	List<PiPresInfo> getCopyPresOracle(PiPresInfo para);
	
	List<PiPresInfoDt> getCopyPresDt(@Param("pkPres") String pkPres,@Param("pkDept") String pkDept);
}