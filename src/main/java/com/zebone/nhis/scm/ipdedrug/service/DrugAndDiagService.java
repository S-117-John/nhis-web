package com.zebone.nhis.scm.ipdedrug.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;




import org.codehaus.jackson.type.TypeReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zebone.nhis.scm.ipdedrug.dao.DrugAndDiagMapper;
import com.zebone.platform.framework.support.IUser;
import com.zebone.platform.modules.exception.BusException;
import com.zebone.platform.modules.utils.JsonUtil;

/**
 * 药品关联诊断查询服务
 * @author jiangdong
 *
 */
@Service
public class DrugAndDiagService {

	@Autowired
	private DrugAndDiagMapper drugAndDiagMapper;
	
	public List<Map<String,Object>> qryDrugAndDiag(String param, IUser user){
		Map<String, Object> map = JsonUtil.readValue(param, new TypeReference<Map<String,Object>>() {
		});
		String type = (String) map.get("type");
		List<Map<String, Object>> result = new ArrayList<Map<String, Object>>();
		if(type.equals("outpatients")){
			result = drugAndDiagMapper.qryByPatient(map);
		}
		if(type.equals("inhospital")){
			result = drugAndDiagMapper.qryByHospital(map);
		}else{
			new BusException("请选择业务类型");
		}
		return result;
	}
}
