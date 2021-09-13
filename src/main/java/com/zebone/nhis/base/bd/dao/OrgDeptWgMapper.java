package com.zebone.nhis.base.bd.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;
import com.zebone.nhis.base.bd.vo.BdFlowBpExt;
import com.zebone.nhis.common.module.base.bd.res.OrgDeptWg;
import com.zebone.platform.modules.mybatis.Mapper;

@Mapper
public interface OrgDeptWgMapper {

	//获取医疗管理组的审批流配置
	List<BdFlowBpExt> getFlowConfig();
	
	//获取医疗管理组的审批流配置 nextStep:true 下一节点  false:上一节点
	List<BdFlowBpExt> findNextOrPreStep(@Param(value = "pkFlowStep") String pkFlowStep,@Param(value = "nextStep") Boolean nextStep);

	
	//医疗组审核查询待审的医疗组列表
	List<OrgDeptWg> queryApproveWg(Map<String,String> paramMap);
	
	
}
