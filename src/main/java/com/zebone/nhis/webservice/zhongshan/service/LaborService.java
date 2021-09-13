package com.zebone.nhis.webservice.zhongshan.service;

import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

import com.zebone.nhis.common.module.labor.nis.PiLabor;
import com.zebone.nhis.common.support.CommonUtils;
import com.zebone.nhis.webservice.support.RespJson;
import com.zebone.platform.modules.dao.DataBaseHelper;
/**
 * 妇幼与产房数据同步webservice接口
 * @author yangxue
 */
@Service
public class LaborService {
	private  Logger logger = LogManager.getLogger(LaborService.class.getName());
    /**
     * 根据妇幼保健号，更新孕妇档案信息,对应功能号LABOR01
     * @param paramMap{fun_id,pno,......}
     */
	public String updatePiLaborInfo(Map<String, Object> paramMap){
		String pno = CommonUtils.getString(paramMap.get("pno"));
		if(CommonUtils.isEmptyString(pno))
			return CommonUtils.getString(new RespJson("-1|未获取到妇幼保健号！|", true));
		PiLabor pi = DataBaseHelper.queryForBean("select * from pi_labor where pno = ?", PiLabor.class, new Object[]{pno});
		if(pi == null||CommonUtils.isEmptyString(pi.getPkPilabor())){//当前患者不存在孕妇档案，插入新数据
			
		}else{//更新
			
		}
		return  null;
		//DataBaseHelper.insertBean(paramBean);
	}
	
	
}
