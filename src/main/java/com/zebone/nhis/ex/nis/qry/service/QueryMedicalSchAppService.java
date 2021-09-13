package com.zebone.nhis.ex.nis.qry.service;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.zebone.nhis.common.support.CommonUtils;
import com.zebone.nhis.ex.nis.qry.dao.MedSchAppQueryMapper;
import com.zebone.platform.framework.support.IUser;
import com.zebone.platform.modules.utils.JsonUtil;

@Service
public class QueryMedicalSchAppService {
    @Resource
	private MedSchAppQueryMapper medschAppQueryMapper;
	
    /**
     * 医技预约查询
     * @param param{pkDeptNs,pkDept,pkDeptExec,dateEnd,dateBegin,codePv,bedNo,namePi}
     * @param user
     * @return
     */
    public List<Map<String,Object>> queryMedSchApp(String param,IUser user){
    	Map<String,Object> map = JsonUtil.readValue(param, Map.class);
		String dateEnd = CommonUtils.getString(map.get("dateEnd"));
		if(dateEnd!=null&&!dateEnd.equals("")){
			map.put("dateEnd", dateEnd.substring(0, 8)+"235959");
		}
    	return medschAppQueryMapper.queryMedSchApp(map);
    }
}
