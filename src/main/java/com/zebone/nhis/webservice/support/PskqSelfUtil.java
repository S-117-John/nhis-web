package com.zebone.nhis.webservice.support;

import java.util.Map;

import com.zebone.nhis.common.support.CommonUtils;
import com.zebone.platform.common.support.User;
import com.zebone.platform.modules.dao.DataBaseHelper;
import com.zebone.platform.modules.utils.JsonUtil;

public class PskqSelfUtil {

	public static User getDefaultUser(String deviceId) {
		User user = new User();
		Map<String, Object> bdOuMap = DataBaseHelper
				.queryForMap(
						"SELECT * FROM bd_ou_employee emp JOIN bd_ou_empjob empjob  ON empjob.pk_emp = emp.pk_emp WHERE empjob.del_flag = '0' and emp.del_flag = '0' and emp.CODE_EMP = ?",
						deviceId);
		if (bdOuMap != null) {
			user.setPkOrg(CommonUtils.getPropValueStr(bdOuMap, "pkOrg"));
			user.setNameEmp(CommonUtils.getPropValueStr(bdOuMap, "nameEmp"));
			user.setPkOrg(CommonUtils.getPropValueStr(bdOuMap, "pkOrg"));
			user.setNameEmp(CommonUtils.getPropValueStr(bdOuMap, "nameEmp"));
			user.setPkEmp(CommonUtils.getPropValueStr(bdOuMap, "pkEmp"));
			user.setPkDept(CommonUtils.getPropValueStr(bdOuMap, "pkDept"));
			user.setCodeEmp(CommonUtils.getPropValueStr(bdOuMap, "codeEmp"));
		}else {
			user = null;
		}
		return user;
	}

	public static Object beanToMap1(Object obj) {
		String stringBean = JsonUtil.writeValueAsString(obj);
		return JsonUtil.readValue(stringBean, Map.class);
	}
	
}
