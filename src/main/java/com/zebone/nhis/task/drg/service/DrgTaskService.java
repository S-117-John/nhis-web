package com.zebone.nhis.task.drg.service;



import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.zebone.nhis.base.drg.vo.PvIpVo;
import com.zebone.nhis.ma.pub.support.ExtSystemProcessUtils;
import com.zebone.nhis.task.drg.dao.DrgMapper;
import com.zebone.platform.modules.dao.DataBaseHelper;
import com.zebone.platform.modules.exception.BusException;
import com.zebone.platform.modules.utils.JsonUtil;
import com.zebone.platform.quartz.modle.QrtzJobCfg;

/**
 * drg定时任务
 * @author ds
 *
 */
@Service
public class DrgTaskService {
	private static Logger log = LoggerFactory.getLogger("nhis.SdDrgPlatFormLog");
	@Resource
	private	DrgMapper drgMapper;
	
	/**
	 * 定时上传DRG数据
	 * @param cfg
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public Map executeTask(QrtzJobCfg cfg){
		log.info("****************定时任务开始执行DRG数据上传*****************");
		//查询需要上传的数据列表
		List<Map<String,Object>> list=drgMapper.queryPvEncounterList(null);
		for (Map<String, Object> map : list) {
			String pkPv=(String) map.get("pkPv");
			//先调用最小数据集
			Object result=ExtSystemProcessUtils.processExtMethod("DrgBusinessInfo", "MinData", pkPv);
			Map<String,Object>  resultMap= JsonUtil.readValue(result.toString(), Map.class);
			if(resultMap.get("CODE").equals("200")){
				//调用最病人服务明细
				result=ExtSystemProcessUtils.processExtMethod("DrgBusinessInfo", "ServiceDetail", "'"+pkPv+"'");
				Map<String,Object>  min= JsonUtil.readValue(result.toString(), Map.class);
				if(min.get("CODE").equals("200")){
					updateDrgStatus(pkPv,"1");
				}
			}
		}
		log.info("****************定时任务开始执行DRG数据上传结束*****************");
		return null;
		
	}
	public void updateDrgStatus(String pkpv,String euStatusDrg){
		PvIpVo pvip=new PvIpVo();
		pvip.setEuStatusDrg(euStatusDrg);
		pvip.setDateDrg(new Date());
		int count=DataBaseHelper.updateBeanByWhere(pvip, "PK_PV="+pkpv,false);
		if(count==0){
			throw new BusException("Drg状态修改失败，请检查！");
		}
	}
}
