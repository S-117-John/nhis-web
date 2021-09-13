package com.zebone.nhis.cn.ipdw.service;

import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.Map;

import org.apache.commons.beanutils.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zebone.nhis.cn.ipdw.dao.CnIpPressAuthMapper;
import com.zebone.nhis.cn.ipdw.vo.CnIpPressAuth;
import com.zebone.platform.common.support.User;
import com.zebone.platform.framework.support.IUser;

@Service
public class CnIpPressAuthService {
	@Autowired 
	private CnIpPressAuthMapper cnIpPressAuthMapper;
	
	public CnIpPressAuth getDoctorAuth(String param,IUser user) throws IllegalAccessException, InvocationTargetException{
		return getDoctorAuth(user);
	}
    public CnIpPressAuth getDoctorAuth(IUser user) throws IllegalAccessException, InvocationTargetException{
    	List<Map<String,Object>> ret = cnIpPressAuthMapper.qryPressAuth(user.getId());
		if(null == ret || ret.size() <= 0 || ret.get(0) == null) return null;
		CnIpPressAuth auth = new CnIpPressAuth();
		BeanUtils.copyProperties(auth, ret.get(0));	
		auth.setAntiCode(ret.get(0).get("dtAnti")==null?"":ret.get(0).get("dtAnti").toString());
		return auth;		
   }
}
