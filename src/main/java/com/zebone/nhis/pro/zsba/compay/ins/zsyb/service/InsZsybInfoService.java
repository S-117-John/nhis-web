package com.zebone.nhis.pro.zsba.compay.ins.zsyb.service;

import net.sf.json.JSONObject;

import org.springframework.stereotype.Service;

import com.zebone.nhis.pro.zsba.compay.ins.zsyb.vo.InsZsBaYbInfo;
import com.zebone.platform.framework.support.IUser;
import com.zebone.platform.modules.dao.DataBaseHelper;
import com.zebone.platform.modules.utils.JsonUtil;

@Service
public class InsZsybInfoService {
	

	/**
	 * 保存中山医保个人资料
	 * @param param
	 * @param user
	 */
	public void saveInsInfo(String param , IUser user){
		InsZsBaYbInfo insInfo = JsonUtil.readValue(param, InsZsBaYbInfo.class);
		if(insInfo.getPkInsinfo()==null){
			//判断本次就诊的个人资料是否存在，存在则修改
			InsZsBaYbInfo insInfoTwo = DataBaseHelper.queryForBean("select * from ins_info where del_flag = '0' and pk_pv = ?", InsZsBaYbInfo.class, insInfo.getPkPv());
			if(insInfoTwo==null){
				DataBaseHelper.insertBean(insInfo);
			}else{
				insInfo.setPkInsinfo(insInfoTwo.getPkInsinfo());
				DataBaseHelper.updateBeanByPk(insInfo, false);
			}
			
		}else{
			DataBaseHelper.updateBeanByPk(insInfo, false);
		}
	}
	
	/**
	 * 获取中山医保个人资料
	 * @param param
	 * @param user
	 */
	public InsZsBaYbInfo getInsInfo(String param , IUser user){
		JSONObject jo = JSONObject.fromObject(param);
		String pkInspv = jo.getString("pkInspv");
		InsZsBaYbInfo insInfo = DataBaseHelper.queryForBean("select * from ins_info where del_flag = '0' and pk_inspv = ?", InsZsBaYbInfo.class, pkInspv);
		return insInfo;
	}
}