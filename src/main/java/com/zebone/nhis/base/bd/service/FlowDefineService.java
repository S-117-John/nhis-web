package com.zebone.nhis.base.bd.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zebone.nhis.base.bd.dao.FlowDefineMapper;
import com.zebone.nhis.base.bd.vo.BdFlowVO;
import com.zebone.nhis.common.module.base.bd.wf.BdFlow;
import com.zebone.nhis.common.module.base.bd.wf.BdFlowStep;
import com.zebone.platform.common.support.User;
import com.zebone.platform.framework.support.IUser;
import com.zebone.platform.modules.dao.DataBaseHelper;
import com.zebone.platform.modules.exception.BusException;
import com.zebone.platform.modules.utils.JsonUtil;

/*
 * 基础数据-流程配置-审批流定义
 */
@Service
public class FlowDefineService {

	@Autowired
	private FlowDefineMapper flowDefineMapper;
	
	//审批流业务列表查询：001002006026
	public List<Map<String, Object>> qryFlow(String param, IUser user){
		
		return flowDefineMapper.qryFlow();
	}
	
	//业务步骤查询：001002006027
	public List<Map<String, Object>> qryFlowStep(String param, IUser user){
		
		User u = (User)user;
		String pkFlow = JsonUtil.getFieldValue(param, "pkFlow");
		
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("pkFlow", pkFlow);
		map.put("pkOrg", u.getPkOrg());
		
		return flowDefineMapper.qryFlowStep(map);
	}
	//审批流保存：001002006028
	public void saveFlow(String param, IUser user){
		
		BdFlowVO vo = JsonUtil.readValue(param, BdFlowVO.class);
		BdFlow flow = vo.getFlow();
		
		//保存定义
		if(StringUtils.isNotBlank(flow.getPkFlow())){
			DataBaseHelper.updateBeanByPk(flow,false);
			//先把步骤列表软删除，再恢复
			DataBaseHelper.execute("update bd_flow_step set del_flag = '1' where pk_flow = ?", flow.getPkFlow());
		}else{
			flow.setPkOrg("~                               ");
			DataBaseHelper.insertBean(flow);
		}
		
		//保存步骤列表
		List<BdFlowStep> list = vo.getFlowStepList();
		if(list != null && list.size() > 0){
			for (BdFlowStep bdFlowStep : list) {
				if(StringUtils.isNotBlank(bdFlowStep.getPkFlowstep())){
					bdFlowStep.setDelFlag("0");
					DataBaseHelper.updateBeanByPk(bdFlowStep,false);
				}else{
					bdFlowStep.setPkFlow(flow.getPkFlow());
					DataBaseHelper.insertBean(bdFlowStep);
				}
			}
		}
	}
	
	//审批流保存：001002006029
	public void delFlow(String param, IUser user){
		
		String pkFlow = JsonUtil.getFieldValue(param, "pkFlow");
		
		Integer count = DataBaseHelper.queryForScalar("select count(1) from bd_flow_bp bp where bp.pk_flow = ?", Integer.class, pkFlow);
		if(count > 0){
			throw new BusException("禁止删除！");
		}
		
		DataBaseHelper.execute("delete from bd_flow_step where pk_flow = ?", pkFlow);
		DataBaseHelper.execute("delete from bd_flow where pk_flow=?", pkFlow);
	}
}
