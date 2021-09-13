package com.zebone.nhis.pi.pub.dao;

import com.zebone.nhis.common.module.bl.BlSettle;
import com.zebone.nhis.common.module.pi.PiAllergic;
import com.zebone.nhis.common.module.pi.PiCard;
import com.zebone.nhis.common.module.pi.PiMaster;
import com.zebone.nhis.common.module.pi.acc.PiCardIss;
import com.zebone.nhis.common.module.pv.PvEncounter;
import com.zebone.nhis.pi.pub.vo.PvDiagVo;
import com.zebone.platform.modules.mybatis.Mapper;

import java.util.List;
import java.util.Map;

/***
 * 患者相关mapper
 *
 */
@Mapper
public interface PiPubMapper {
	
	/** 根据就诊主键获取预交金总额 */
	Double getTotalPrepayAmountByPkPv(String pkPv);
	
	/** 根据就诊主键获取费用总额 */
	Double getTotalAmountByPkPv(String pkPv);
	
	/** 根据就诊主键获取自费总额*/
	Double getTotalAmountPiByPkPv(String pkPv);
	
	/** 根据就诊主键查询患者就诊_临床综合诊断信息列表 */
	List<PvDiagVo> getPvDiagListByPkPv(String pkPv);
	
	/** 根据患者主键查询过敏史列表 */
	List<PiAllergic> getPiAllergicListByPkPi(String pkPi);
	
	/** 根据卡编码查询患者卡信息 */
	PiCard getPiCardByCardNo(String cardNo);
	
	/** 获取患者就诊信息列表 */
	List<PvEncounter> getPvEncounterList(PvEncounter pvEncounter);
	
	/** 获取患者基本信息 */
	List<PiMaster> getPiMaster(PiMaster piMaster);
	
	/**获取无名氏最大编号*/
	Integer getAnonymousMaxNumberSqlServer();
	Integer getAnonymousMaxNumberOracle();
	
	List<PiCardIss> getPiCardIss(PiCardIss piCardIss);	
	
	List<Map<String, Object>> getPkAndPvtype(String pkPi);
	
	BlSettle qryBlSettleByCodeSt(String codeSt);
	
	public List<Map<String,Object>> qryPvCgInfo(Map<String,Object> paramMap);

    List<Map<String, Object>>  qryPvCgInfoByMssql(Map<String, Object> paramMap);

	public List<Map<String,Object>> qryPvCgInfos(Map<String,Object> paramMap);

}
