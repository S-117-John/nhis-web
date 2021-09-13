package com.zebone.nhis.ex.nis.pd.service;

import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.zebone.nhis.common.support.CommonUtils;
import com.zebone.nhis.common.support.DateUtils;
import com.zebone.nhis.ex.nis.pd.dao.ReceiverMapper;
import com.zebone.nhis.ex.nis.pd.vo.AcceptVo;
import com.zebone.nhis.ex.nis.pd.vo.DetailedVo;
import com.zebone.nhis.ex.nis.pd.vo.EmpVo;
import com.zebone.nhis.ex.nis.pd.vo.SummaryVo;
import com.zebone.platform.framework.support.IUser;
import com.zebone.platform.modules.exception.BusException;
import com.zebone.platform.modules.utils.JsonUtil;

@Service
public class ReceiverService {
	
	@Resource
	ReceiverMapper receiverMapper;
	
	//查询配药汇总列表
	public List<SummaryVo> QueryDispenseList(String param,IUser user){
		Map<String,Object> map= JsonUtil.readValue(param, Map.class);
		List<SummaryVo> list=receiverMapper.QueryDispenseList(map);
		return list;
	}
	
	//查询配药明细列表
	public List<DetailedVo> QueryDetailedList(String param,IUser user){
		Map<String,Object> map = JsonUtil.readValue(param,Map.class);
		List<DetailedVo> list=receiverMapper.QueryDetailedList(map);
		return list;
	}
	
	//查询人物
	public EmpVo QueryEmp(String param,IUser user){
		Map<String,Object> map=JsonUtil.readValue(param, Map.class);
		EmpVo emp=receiverMapper.QueryEmp(map);
		return emp;
	}
	
	//更新签收状态
	public void AcceptUpdate(String param,IUser user){
		AcceptVo accept = JsonUtil.readValue(param,AcceptVo.class );
		if(accept == null)
			throw new BusException("未获取到待签收的记录！");
		if(accept.getPkPdDes() == null || accept.getPkPdDes().size() < 1)
			return;
		
		if(CommonUtils.isEmptyString(accept.getPkEmp()) || null == accept.getPkEmp())
			throw new BusException("未获取到签收人主键！");
		if(CommonUtils.isEmptyString(accept.getNameEmp()) || null == accept.getNameEmp())
			throw new BusException("未获取到签收人姓名！");
		String dt= DateUtils.getDateTimeStr(new Date());//设置当前签收时间【yyyyMMddHHmmss】
		accept.setDateSign(dt);
		receiverMapper.UpdateState(accept);
	}
}
