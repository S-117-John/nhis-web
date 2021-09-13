package com.zebone.nhis.task.scm.service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.zebone.nhis.common.support.CommonUtils;
import com.zebone.nhis.scm.pub.service.PdRepricePubService;
import com.zebone.platform.modules.exception.BusException;
import com.zebone.platform.quartz.modle.QrtzJobCfg;
/**
 * 供应链定时任务
 * @author yangxue
 *
 */
@Service
public class ScmTaskService {
	@Resource
	private PdRepricePubService  pdRepricePubService;
	/**
	 * 定时调价任务
	 * @param cfg
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public Map timeRepriceTask(QrtzJobCfg cfg){
		String pkOrg = cfg.getJgs();
		if(CommonUtils.isEmptyString(pkOrg))
			throw new BusException("未获取到机构信息！");
		Date begin = new Date();
		String[] orgarr = pkOrg.split(",");
		if(orgarr!=null&&orgarr.length>0){
			for(int i = 0; i<orgarr.length;i++){
				pdRepricePubService.execFixedReprice(orgarr[i]);
			}
		}
		return null;
	}
	
}
