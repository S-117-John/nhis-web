package com.zebone.nhis.pv.reg.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.zebone.nhis.common.module.base.bd.price.BdHp;
import com.zebone.nhis.common.module.base.bd.srv.BdItem;
import com.zebone.nhis.common.module.base.bd.srv.BdItemPv;
import com.zebone.nhis.common.module.bl.BlDeposit;
import com.zebone.nhis.common.module.bl.BlInvoiceDt;
import com.zebone.nhis.common.module.bl.opcg.BlOpDt;
import com.zebone.nhis.common.module.sch.appt.SchAppt;
import com.zebone.nhis.pv.pub.vo.PvOpAndSettleVo;
import com.zebone.nhis.pv.reg.vo.PvOpVo;
import com.zebone.platform.modules.mybatis.Mapper;

/**
 * 挂号相关dao接口
 * 
 * @author wangpeng
 * @date 2016年9月10日
 *
 */
@Mapper
public interface RegMapper {

	/** 获取医保计划列表 */
	List<BdHp> getBdPhList(BdHp bdhp);
	
	/** 获取收费项目列表 */
	List<BdItem> getBdItemList(BdItemPv bdItemPv);
	
	/** 根据患者主键获取患者预约记录 */
	List<SchAppt> getSchApptListByPkPi(@Param("pkPi")String pkPi,@Param("pkOrg")String pkOrg, @Param("sysTime")String sysTime);
	
	/** 根据患者主键获取患者历史就诊记录(最近五次) */
	List<PvOpVo> getPvOpVoHistoryList(@Param("pkPi")String pkPi,@Param("pkOrg")String pkOrg);
	
	/** 根据患者主键获取患者历史就诊记录(最近五次) Oracle写法*/
	List<PvOpVo> getPvOpVoHistoryListOracle(@Param("pkPi")String pkPi,@Param("pkOrg")String pkOrg);
	
	/** 根据患者主键获取患者当前挂号记录 */
	List<PvOpVo> getPvOpVoTodayList(@Param("pkPi")String pkPi,@Param("pkOrg")String pkOrg);
	
	/** 根据患者主键获取患者当前挂号记录  Oracle*/
	List<PvOpVo> getPvOpVoTodayListOracle(@Param("pkPi")String pkPi,@Param("pkOrg")String pkOrg);
	
	/** 根据就诊主键获取相关信息 */
	PvOpAndSettleVo getPvOpAndSettleVoByPkPv(String pkPv);
	
	/** 根据就诊主键查询收费项目分组列表(包含合计) */
	List<BlOpDt> getBlOpDtGroupListByPkPv(String pkPv);
	
	/** 根据就诊主键查询交款记录分组列表 */
	List<BlDeposit> getBlDepositGroupListByPkPv(String pkPv);
	
	/** 获取收费结算-发票记录列表 */
	List<BlInvoiceDt> getBlInvoiceDtList(String pkPv);
	
}
