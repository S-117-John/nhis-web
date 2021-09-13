package com.zebone.nhis.pro.zsba.compay.ins.pub.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.zebone.nhis.bl.pub.vo.InvInfoVo;
import com.zebone.nhis.common.module.bl.BlAmtVo;
import com.zebone.nhis.common.module.bl.InvItemVo;
import com.zebone.platform.modules.mybatis.Mapper;

@Mapper
public interface InsPubIpSettleQryMapper {
	
	public BlAmtVo QryAmtAndPi(@Param(value = "pkPv") String pkPv,@Param(value = "dateBegin")String dateBegin, @Param(value = "dateEnd") String dateEnd,@Param(value = "pkSettle")String pkSettle);

	public List<InvItemVo> QryInvItemInfo(@Param(value = "pkPv") String pkPv,@Param(value = "pkOrg") String pkOrg,
			@Param(value = "dateBegin") String dateBegin,
			@Param(value = "dateEnd") String dateEnd,
			@Param(value = "pkDepts")List<InvInfoVo>  pkDepts,
			@Param(value = "pkSettle")String pkSettle);	
	
	/**
	 * 查询费用分类信息
	 * @param paraMap
	 * @return
	 */
	public List<Map<String,Object>> queryChargeClassify(Map<String, Object> paraMap);
	
	/**
	 * 根据人员主键查询科室医疗类型
	 * @param paraMap
	 * @return
	 */
	public List<Map<String,Object>> qryDtDeptType(Map<String, Object> paraMap);
}
