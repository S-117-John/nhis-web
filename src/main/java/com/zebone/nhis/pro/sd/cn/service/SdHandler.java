package com.zebone.nhis.pro.sd.cn.service;

import com.zebone.nhis.pro.sd.cn.dao.RisLabMapper;
import com.zebone.platform.framework.support.IUser;
import com.zebone.platform.modules.dao.DataBaseHelper;
import com.zebone.platform.modules.dao.jdbc.DataSourceRoute;
import com.zebone.platform.modules.exception.BusException;
import com.zebone.platform.modules.utils.JsonUtil;

import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 深大，检验检查客户化逻辑
 * 
 * @author leiminjian
 */
@Service
public class SdHandler {
    @Resource
    private RisLabMapper risLisDao;

	public Map<String, Object> qryPaitBs(String param, IUser user) {
        Map<String,Object> paramMap = JsonUtil.readValue(param, Map.class);
        if(paramMap == null){
			throw new BusException("未获取查询条件！");
        }
		// 校验其他条件
        if(paramMap.get("codeOp")==null){
			throw new BusException("未获取到查患者的门诊号");
        }

        try {
            DataSourceRoute.putAppId("EMER_SD");
			List<Map<String, Object>> list = new ArrayList<>();
			list = DataBaseHelper.queryForList(
					"select chief_Complaint ,hpi ,elapsemhis ,elapsemhis,bodycheck,specialcheck from yibang.view_tohis_OUTPATIENT where pid=:codeOp and register_SN=:codePv ",
					new Object[] { paramMap.get("codeOp").toString(), paramMap.get("codePv").toString() });
			if (list.size() > 0) {
                list.get(0).put("codeop",paramMap.get("codeOp"));
				return list.get(0);
			}
			return null;
        } catch (Exception e) {
			throw new BusException("查询病历信息出错！！！");
        } finally {
            DataSourceRoute.putAppId("default");
        }


    }
}
