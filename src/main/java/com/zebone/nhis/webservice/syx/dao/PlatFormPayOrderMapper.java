package com.zebone.nhis.webservice.syx.dao;

import com.zebone.nhis.common.module.base.bd.code.BdDefdoc;
import com.zebone.nhis.common.module.base.bd.price.BdHp;
import com.zebone.nhis.common.module.pi.PiInsurance;
import com.zebone.nhis.webservice.syx.vo.platForm.SchApptVo;
import com.zebone.platform.modules.mybatis.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface PlatFormPayOrderMapper {
	
	/**查询预约信息*/
	public SchApptVo qrySchApptInfo(Map<String,Object> paramMap);
	
	/**查询预约来源码表信息*/
	public List<BdDefdoc> qryApptypeDocList(Map<String,Object> paramMap);
	
	/**查询患者主医保*/
	public List<PiInsurance> qryPiInsu(String pkPi);
	
	/** 根据患者主键获取最大的就诊次数(门诊) */
	public Integer getMaxOpTimes(String pkPi);
	
	/**查询医保信息*/
	public BdHp qryHpInfo(String pkHp);
	
	/**查询预约就诊记录*/
	public SchApptVo qrySchApptPvInfo(Map<String,Object> paramMap);
	
	/**查询患者医保拓展属性*/
	public String qryHpValAttr(Map<String,Object> paramMap);
	
	/**查询本次就诊草药数量个数*/
	public Integer qryCyCntByPv(Map<String,Object> paramMap);

	/**
	 * 申请挂号退费信息
	 * @param paramMap
	 * @return
	 */
	public List<Map<String, Object>> getReturnPayInfo(Map<String,Object> paramMap);

	/**
	 * 查询标本采集地点
	 * @param codeSt
	 * @return
	 */
	public Map<String, Object> qryCodeAndDesc(String codeSt);
}
  
