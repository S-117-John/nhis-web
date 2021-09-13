package com.zebone.nhis.sch.appt.service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zebone.nhis.sch.appt.dao.SchQuitDiagnosisQryMapper;
import com.zebone.platform.framework.support.IUser;
import com.zebone.platform.modules.utils.JsonUtil;


@Service
public class SchQuitDiagnosisQryService {
	

	@Autowired
	private SchQuitDiagnosisQryMapper schQuitDiagnosisQryMapper;
	
	public List<Map<String, Object>> getSchApptList(String param, IUser user){
		Map<String, Object> params = JsonUtil.readValue(param, Map.class);	
		SimpleDateFormat df = new SimpleDateFormat("yyyyMMddhhmmss");
		try {
			if(params != null){
				if(params.get("dateBegin") != null){
					Date dateBegin = df.parse((String) params.get("dateBegin"));
					params.put("dateBegin", dateBegin);
				}
				if(params.get("dateEnd")!= null){
					Date dateEnd = df.parse((String) params.get("dateEnd"));					
					params.put("dateEnd", dateEnd);		
				}										
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}						
		return schQuitDiagnosisQryMapper.getSchAppt(params);
	}

}
