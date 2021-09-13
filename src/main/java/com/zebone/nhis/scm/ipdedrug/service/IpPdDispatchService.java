package com.zebone.nhis.scm.ipdedrug.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zebone.nhis.common.support.CommonUtils;
import com.zebone.nhis.common.support.DateUtils;
import com.zebone.nhis.scm.ipdedrug.dao.IpPdDispatchMapper;
import com.zebone.nhis.scm.ipdedrug.vo.AcceptVo;
import com.zebone.nhis.scm.ipdedrug.vo.DispatchVo;
import com.zebone.platform.common.support.User;
import com.zebone.platform.framework.support.IUser;
import com.zebone.platform.modules.exception.BusException;
import com.zebone.platform.modules.utils.JsonUtil;


@Service
public class IpPdDispatchService {
	@Autowired
	private IpPdDispatchMapper ipPdDispatchMapper;
	
	/**
	 * 查询物品发退表
	 * @param param
	 * @param user
	 */
	public List<DispatchVo> QueryDispatchList(String param,IUser user){
		Map<String,Object> map=JsonUtil.readValue(param, Map.class);
		String pk_store = ((User)user).getPkStore();
		if(CommonUtils.isEmptyString(pk_store))
			throw new BusException("请选择使用的仓库！");
		String dateE=(String)map.get("dateE");
		if(dateE!=null){
			 map.put("dateE", dateE.substring(0,8)+"235959");
		}
		String dateS=(String)map.get("dateS");
		if(dateS!=null){
			 map.put("dateS", dateS.substring(0,8)+"000000");
		}
		map.put("pkStoreDe",pk_store);
		List<DispatchVo> list=ipPdDispatchMapper.QueryDispatchList(map);
		return list;
	}
	/**
	 * 更新状态
	 * @param param
	 * @param user
	 */
	public void AcceptUpdate(String param,IUser user){
		AcceptVo accept=JsonUtil.readValue(param,AcceptVo.class );
		List<DispatchVo> list=accept.getList();
		if(list==null||list.size()<=0)
			throw new BusException("未获取到要配送的药品列表");
		List<String> alist=new ArrayList<String>();
		for (DispatchVo summaryVo : list) {
			alist.add(summaryVo.getPkPdde());
		}
		Map<String,Object> map=new HashMap<String, Object>();
		map.put("nameemp", ((User)user).getNameEmp());
		map.put("pkemp", ((User)user).getPkEmp());
		map.put("date",DateUtils.getDateTimeStr(new Date()));
		map.put("list",alist);
		ipPdDispatchMapper.UpdateState(map);
		return ;
	}
}
