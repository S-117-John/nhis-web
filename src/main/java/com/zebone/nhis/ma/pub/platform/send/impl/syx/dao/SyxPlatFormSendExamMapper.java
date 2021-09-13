package com.zebone.nhis.ma.pub.platform.send.impl.syx.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import com.zebone.nhis.common.module.cn.ipdw.CnOrder;
import com.zebone.platform.modules.mybatis.Mapper;

@Mapper
public interface SyxPlatFormSendExamMapper {
	public List<Map<String, Object>> qryExamAndCheckAll(@Param("pkCnords") List pkCnords);

	public Map<String, Object> qryBloodInfo(String pkCnord);

	public List<Map<String, Object>> qryPathologyAll(@Param("pkCnords") List pkCnords);

	public String getOrdType(String propValueStr);
	
	public List<String> qryPathologyDept(@Param("pkCnords")List<CnOrder> pkCnords);
	
	/**
	 * 查询医嘱信息发送者IP
	 * @return
	 */
	@Select("select t.attr_desc from bd_defdoc t where code_defdoclist ='800004'and code = #{action}")
	public String qryIpSend(@Param(value = "action")String action);
}
