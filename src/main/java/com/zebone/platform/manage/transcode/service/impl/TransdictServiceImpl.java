package com.zebone.platform.manage.transcode.service.impl;

import com.zebone.platform.manage.transcode.service.TransdictService;
import com.zebone.platform.modules.dao.DataBaseHelper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service("transdictService")
public class TransdictServiceImpl implements TransdictService {

	@Override
	public List<Map<String, Object>> getSyscodeTypes(String typegroup) {
		String sql = "";
		if("syscode".equals(typegroup)){
			sql+="select id,typename as systypename,typegroup,typedesc";
		}else if("proxytime".equals(typegroup)){
			sql+="select id,typename as pxytypename,typegroup,typedesc";
		}else if("zt".equals(typegroup)){
			sql+="select id,typename as zttypename,typegroup,typedesc";
		}else if("isvisable".equals(typegroup)){
			sql+="select id,typename as vistypename,typegroup,typedesc";
		}else if("cronexp".equals(typegroup)){
			sql+="select id,typename as crontypename,typegroup,typedesc";
		}else if("jobtype".equals(typegroup)){
			sql+="select id,typename as jobtypename,typegroup,typedesc";
		}else if("syslog".equals(typegroup)){
			sql+="select id,typename as syslogtypename,typegroup,typedesc";
		}
		sql+=" from sys_service_trans_dict where typegroup = '"+typegroup+"'";
		List<Map<String, Object>> typelist = DataBaseHelper.queryForList(sql);
		return typelist;
	}

}
