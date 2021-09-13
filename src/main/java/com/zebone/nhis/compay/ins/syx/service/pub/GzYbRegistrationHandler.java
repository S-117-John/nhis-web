package com.zebone.nhis.compay.ins.syx.service.pub;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zebone.nhis.common.module.compay.ins.syx.GzybDataSource;
import com.zebone.nhis.common.module.compay.ins.syx.gzyb.GzybVisit;
import com.zebone.nhis.compay.ins.syx.dao.gzyb.GzybMapper;
import com.zebone.platform.common.support.User;
import com.zebone.platform.framework.support.IUser;
import com.zebone.platform.modules.dao.DataBaseHelper;
import com.zebone.platform.modules.dao.jdbc.DataSourceRoute;
import com.zebone.platform.modules.exception.BusException;
import com.zebone.platform.modules.utils.JsonUtil;

@Service
public class GzYbRegistrationHandler {
	@Autowired
	private GzybMapper gzybMapper;

	public Map<String, Object> queryZydjInfo(String param, IUser user) {
		User u = (User) user;
		String pkpv = JsonUtil.getFieldValue(param, "pkpv");
		if (pkpv != null) {
			GzybVisit visitInfo = gzybMapper.getGzybVisit(pkpv);
			if (visitInfo != null) {
				if (visitInfo.getPkhp() != null) {
					String pkOrg = u.getPkOrg();
					String pkdept = u.getPkDept();
					GzybDataSource dataInfo = gzybMapper.queryDsInfo(
							visitInfo.getPkhp(), pkOrg, pkdept);
					if (dataInfo != null) {
						if (dataInfo.getDescConn() != null) {
							DataSourceRoute.putAppId(dataInfo.getDescConn());
							String qrySql = "select * from his_zydj where jydjh=? ";
							Map<String, Object> insstMap = DataBaseHelper
									.queryForMapFj(qrySql,
											new Object[] { visitInfo
													.getPvcodeIns() });
							DataSourceRoute.putAppId("default");
							return insstMap;

						} else {
							throw new BusException(
									"数据源没有配置，请到医保管理>广州医保>医保数据源配置处进行配置！");
						}
					} else {
						throw new BusException(
								"科室跟医保计划没有配置，请到医保管理>广州医保>医保数据源配置处进行配置！");
					}

				} else {
					throw new BusException("未查询到当前患者的医保登记信息，无法获取数据！");
				}
			} else {
				throw new BusException("未查询到当前患者的医保登记信息，无法获取数据！");
			}
		} else {
			throw new BusException("传入参数有空值");
		}
	}
}
