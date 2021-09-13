package com.zebone.nhis.pv.reg.dao;

import com.zebone.nhis.bl.pub.vo.ItemPriceVo;
import com.zebone.nhis.common.module.bl.BlDeposit;
import com.zebone.nhis.common.module.pay.BlExtPay;
import com.zebone.nhis.common.module.pv.PvEncounter;
import com.zebone.nhis.pv.reg.vo.PiMasterRegVo;
import com.zebone.platform.modules.mybatis.Mapper;

import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * 中山二院版本 门诊挂号服务
 * @author yangxue
 *
 */
@Mapper
public interface RegSyxMapper {

	/**
	 * 查询排班服务及排班服务明细信息
	 * @param paramMap
	 * @return
	 */
	public List<Map<String,Object>> querySchSrvAndDtInfo(Map<String,Object> paramMap);
	
	/**
	 * 查询患者相关信息
	 * @param pkPi
	 * @return
	 */
	public List<Map<String, Object>> queryPiMaster(Map<String,Object> paramMap);
	/**
	 * 查询患者挂号记录
	 * @param pkPi
	 * @param paramMap
	 * @return
	 */
	public List<Map<String, Object>> queryPvEncounter(Map<String, Object> paramMap);
	
	/**
	 * 查询患者挂号支付明细
	 * @param pkPv
	 * @return
	 */
	List<BlDeposit> queryRegPayItems(@Param("pkPv") String pkPv,@Param("euStatus") String euStatus);

	/**
	 * 查询患者预约待确认记录
	 * @param pkPi
	 * @return
	 */
	public List<Map<String, Object>> queryConfirmReservations(@Param("pkPi") String pkPi);

	/**
	 * 查询患者挂号费明细
	 * @param pkPv
	 * @return
	 */
	public List<ItemPriceVo> queryRegFeeItems(@Param("pkPv") String pkPv);
	
	/**
	 * 根据挂号信息更新患者基本信息,只更新界面录入的证件信息
	 * @param regvo
	 */
	public void updatePiMasterByReg(PiMasterRegVo regvo);
	/**
	 * 查询排班资源信息
	 * @param paramMap{pkSchres}
	 * @return
	 */
	public Map<String,Object> querySchResInfo(Map<String,Object> paramMap);
	/**
	 * 查询患者在院信息--用来校验患者是否在指定医保下处于在院状态
	 * @param paramMap{pkPi,pkHp}
	 * @return
	 */
	public List<PvEncounter> queryPatiPvIpInfoByHp(Map<String,Object> paramMap);
	
	/**
	 * 根据不同条件查询挂号记录
	 * @param paramMap{codePv}
	 * @return
	 */
	public List<PiMasterRegVo> queryPvEncounterByCon(Map<String,Object> paramMap);
	
	/**
	 * 更新就诊信息
	 * @param paramMap
	 */
	public void updatePvEncounter(Map<String,Object> paramMap);
	
	/**
	 * 更新预约信息
	 * @param paramMap
	 */
	public void updateSchAppt(Map<String,Object> paramMap);
	
	/**
	 * 查询医保扩展属性（本院职工）对应的配置值
	 * @param pkHp
	 * @return
	 */
	public Map<String,Object> queryPhAttrVal(String pkHp);

    public  List<BlExtPay> queryBlextPayItems(String pkDepo);
    
	/**查询医保扩展属性*/
	public String qryHpAttrCodeVal(Map<String,Object> paramMap);
}
