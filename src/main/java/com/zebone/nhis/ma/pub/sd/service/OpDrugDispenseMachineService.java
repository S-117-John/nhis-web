package com.zebone.nhis.ma.pub.sd.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.zebone.nhis.common.support.CommonUtils;
import com.zebone.platform.modules.dao.DataBaseHelper;

/**
 * 深大门诊发药机服务接口：此类根据接口调用返回处理HIS业务逻辑
 * @author jd_em
 *
 */
@Service
public class OpDrugDispenseMachineService {

	/**
	 * 更新窗口号
	 * @param pkPresocces
	 */
	public void updatePresWinno(List<String> presNos,String winno){
		String sql="update EX_PRES_OCC set WINNO_CONF=?,note='iron' where PRES_NO in ("+CommonUtils.convertListToSqlInPart(presNos)+")";
		DataBaseHelper.execute(sql, new Object[]{winno});
	}
}
