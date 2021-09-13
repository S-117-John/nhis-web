package com.zebone.nhis.pro.zsba.compay.pub.dao;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.zebone.nhis.common.module.bl.BlIpDt;
import com.zebone.nhis.pro.zsba.compay.pub.vo.ZsbaBlDeposit;
import com.zebone.nhis.pro.zsba.compay.pub.vo.ZsbaBlSettle;
import com.zebone.platform.modules.mybatis.Mapper;

@Mapper
public interface ZsbaBlIpPubMapper {
	
	public List<ZsbaBlDeposit> QryBlDepositBySt(@Param(value = "pkSettle") String pkSettle);
	
	public ZsbaBlSettle QryBlSettleByPk(@Param(value = "pkSettle") String pkSettle);
	
	public List<BlIpDt> qryCgIps(Map<String,Object> params);

	/**
	 * 查询患者特诊加收金额
	 * @param pkCgips
	 * @param pkPv
	 * @param dateBegin
	 * @param dateEnd
	 * @return
	 */
	public Double qryAmountAddByPv(@Param("pkCgips") List<String> pkCgips,@Param("pkPv") String pkPv,@Param("dateBegin")Date dateBegin,@Param("dateEnd")Date dateEnd, @Param("pkDept") String pkDept);

	public List<BlIpDt> QryBlIpDtBySt(@Param(value = "pkSettle") String pkSettle);
}
