package com.zebone.nhis.cn.ipdw.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zebone.nhis.cn.ipdw.dao.PathOrderMapper;
import com.zebone.nhis.cn.ipdw.vo.PathOrderVo;
import com.zebone.nhis.common.module.cn.ipdw.BdCpTask;
import com.zebone.nhis.common.module.cn.ipdw.BdCpTaskDt;
import com.zebone.platform.framework.support.IUser;
import com.zebone.platform.modules.dao.DataBaseHelper;
import com.zebone.platform.modules.exception.BusException;
import com.zebone.platform.modules.utils.JsonUtil;

@Service
public class PathOrderService {
	
	@Autowired
	private PathOrderMapper pathOrderMapper;
	
	//查询路径医嘱列表：004004012004
	public List<Map<String, Object>> qryPathOrder(String param,IUser user){
		return pathOrderMapper.qryPathOrder();
	}
	
	//查询路径医嘱明细列表：004004012005
	public List<Map<String, Object>> qryPathOrderDetail(String param,IUser user){
		
		String pkCptask = JsonUtil.getFieldValue(param, "pkCptask");
		
		return pathOrderMapper.qryPathOrderDetail(pkCptask);
	}
	
	//保存路径医嘱：004004012015
	public void savePathOrder(String param,IUser user){
		
		PathOrderVo vo = JsonUtil.readValue(param, PathOrderVo.class);
				
		List<BdCpTask> list = vo.getList();
		if(list != null && list.size() > 0){
			for (BdCpTask bdCpTask : list) {
				
				if(StringUtils.isNotBlank(bdCpTask.getPkCptask())){					
					DataBaseHelper.updateBeanByPk(bdCpTask,false);
				}else{
					bdCpTask.setPkOrg("~                               ");
					bdCpTask.setEuType("0");
					bdCpTask.setFlagActive("1");
					DataBaseHelper.insertBean(bdCpTask);
				}
			}		
		}
		
		List<BdCpTaskDt> detailList = vo.getDetailList();
		
		BdCpTask bdCpTask = vo.getBdCpTask();
		if(StringUtils.isNotBlank(bdCpTask.getPkCptask())){
			DataBaseHelper.execute("UPDATE bd_cp_task_dt set del_flag = '1' where pk_cptask=?", bdCpTask.getPkCptask());			
		}
		
		if(detailList != null && detailList.size() > 0){
			
			for (BdCpTaskDt bdCpTaskDt : detailList) {
				
				if(StringUtils.isNotBlank(bdCpTaskDt.getPkCptaskdt())){
					bdCpTaskDt.setDelFlag("0");
					DataBaseHelper.updateBeanByPk(bdCpTaskDt,false);
				}else{
					bdCpTaskDt.setPkOrg("~                               ");
					DataBaseHelper.insertBean(bdCpTaskDt);
				}
			}			
		}
	}
	
	//删除路径医嘱：004004012016
	public void delPathOrder(String param,IUser user){
		
		String pkCptask = JsonUtil.getFieldValue(param, "pkCptask");
		String[] arr= pkCptask.split(",");	
		ArrayList<String> list = new ArrayList<>(Arrays.asList(arr));
		
		String sql = "select count(1) from cp_temp_cpord where pk_ord=? and eu_cpordtype='1'";
		for (String s : list) {
			Integer count = DataBaseHelper.queryForScalar(sql, Integer.class, s);
			if(count > 0){
				throw new BusException("该数据被模板引用，不能删除！");
			}
			DataBaseHelper.execute("delete from bd_cp_task where pk_cptask=?", s);
			DataBaseHelper.execute("delete from bd_cp_task_dt where pk_cptask=?", s);
		}
	}

}
