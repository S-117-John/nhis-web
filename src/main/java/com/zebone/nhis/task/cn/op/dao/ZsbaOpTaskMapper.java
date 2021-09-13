package com.zebone.nhis.task.cn.op.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.zebone.platform.modules.mybatis.Mapper;

@Mapper
public interface ZsbaOpTaskMapper {

	//博爱专用，查询门诊三天以上未交费门诊医嘱-需求8245
	List<String> queryPkCnordTaskBa();
	//根据pkCnord查询PkPres
	List<String> queryPkPresInfo(@Param("pkCnords") List<String> pkCnords);
	int updateCnOrderDelFlag(@Param("pkCnords") List<String> pkCnords);
	int updateExAssistOccDelFlag(@Param("pkCnords") List<String> pkCnords);
	int updateBlOpDtDelFlag(@Param("pkCnords") List<String> pkCnords);
	int updateCnLabApplyDelFlag(@Param("pkCnords") List<String> pkCnords);
	int updateCnRisApplyDelFlag(@Param("pkCnords") List<String> pkCnords);
	int updateCnPrescriptionDelFlag(@Param("pkPress") List<String> pkPress);
}
