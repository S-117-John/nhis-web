package com.zebone.nhis.arch.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.zebone.platform.modules.dao.DataBaseHelper;
import com.zebone.platform.modules.dao.jdbc.DataSourceRoute;

@Service
public class LisPathService {
	
	private static final String SUFFIX = ".pdf";
	
	
	public List<String> getLisPaths(String sys,String codePi, String ipTimes) {
		 List<String> res = new ArrayList<String>();
		DataSourceRoute.putAppId("archOld");
		List<Map<String, Object>> data;
		StringBuilder sb = new StringBuilder(" select fil.* from emr_arc_file fil ");
		sb.append(" inner join emr_arc_catalog2  catalog on  catalog.sid = fil.arc_catalog_sid");
		sb.append(" inner join emr_arc_index inde on inde.sid = catalog.arc_index_sid");
		sb.append(" inner join view_enc enc on enc.pat_id = inde.pat_id");
		sb.append(" where enc.pat_id = ? and enc.times = ? and fil.file_type = ?");
		data = DataBaseHelper.queryForList(sb.toString(), codePi,ipTimes,sys);
		for(Map<String,Object> map : data){
			res.add((String)map.get("filePath")+(String)map.get("fileName")+SUFFIX);
		}
		return res;
	}

}
