package com.zebone.nhis.pv.adt.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.zebone.nhis.common.support.CommonUtils;
import com.zebone.nhis.pv.adt.vo.ZsHisAdtPv;
import com.zebone.platform.framework.support.IUser;
import com.zebone.platform.modules.dao.DataBaseHelper;
import com.zebone.platform.modules.dao.jdbc.DataSourceRoute;
import com.zebone.platform.modules.exception.BusException;
import com.zebone.platform.modules.utils.JsonUtil;

@Service
public class ZsAdtHandler {
	
	@Resource
	private ZsAdtService zsAdtService;

	/**
	 * 1.获取新住院号（code_ip）、新患者编码（code_pi）
	 * @param param
	 * @param user
	 * @return
	 */
	public String getCodeFromHis(String param, IUser user) {
		DataSourceRoute.putAppId("HIS_bayy");//切换到 HIS
		String codeIp,codePi = "";
		codeIp = zsAdtService.getCodeIpFromHis();//获取住院号
		codePi = zsAdtService.getCodePiFromHis();//获取患者编码
		DataSourceRoute.putAppId("defult");//切换到 NHIS
		return codeIp+","+codePi;
	}
	
	/**
	 * 2.获取相似患者登记记录
	 * @param param
	 * @param user
	 * @return
	 */
	public List<ZsHisAdtPv> queryPvsFromHis(String param, IUser user){
		Map<String,Object> paramMap = JsonUtil.readValue(param, Map.class);
		if(null == paramMap.get("namePi") || CommonUtils.isEmptyString(paramMap.get("namePi").toString())) 
			throw new BusException("未获取到患者姓名！");
		
		//切换到 HIS 获取code_ip
		DataSourceRoute.putAppId("HIS_bayy");
		String namePi = paramMap.get("namePi").toString();
		List<ZsHisAdtPv> list = new ArrayList<ZsHisAdtPv>();
		list = DataBaseHelper.queryForList("select inpatient_no,name,social_no,home_street,employer_name, employer_street,relation_name ,birth_date"
				+ ",patient_id,lastadmiss_times from a_patient_mi where  name like '%"+namePi+"%' and inpatient_no not like '%B%'", ZsHisAdtPv.class);
		//切换到 NHIS
		DataSourceRoute.putAppId("defult");
		return list;
	}
}
