package com.zebone.nhis.ma.pub.sd.service;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.zebone.nhis.ma.pub.sd.dao.MedicareDZMapper;
import com.zebone.platform.framework.support.IUser;
import com.zebone.platform.modules.utils.JsonUtil;

@Service
public class MedicareDZService {
	@Resource
	public MedicareDZMapper medicareDZMapper;
	/***
	 * 查询HIS医保结算数据
	 * 010005003009
	 * @param param
	 * @param user
	 * @return
	 */
	public List<Map<String,Object>> queryHisMedicalInsSt(String param,IUser user){
		List<Map<String, Object>> res = null;
		Map<String,Object> map = JsonUtil.readValue(param, Map.class);

		res=medicareDZMapper.queryHisMedicalInsSt(map);
		return res;
		
	}
}
