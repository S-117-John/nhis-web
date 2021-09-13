package com.zebone.nhis.ma.pub.platform.send.impl.syx.dao;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import com.zebone.platform.modules.mybatis.Mapper;

@Mapper
public interface SyxPlatFormSaveTabMapper {
	/**
	 * 查询医嘱信息发送者IP
	 * @return
	 */
	@Select("select t.attr_desc from bd_defdoc t where code_defdoclist ='800004'and code = #{action}")
	public String qryIpSend(@Param(value = "action")String action);
}
