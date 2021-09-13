package com.zebone.nhis.compay.ins.syx.service.pub;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zebone.nhis.common.module.compay.ins.syx.GzybDataSource;
import com.zebone.nhis.compay.ins.syx.dao.pub.GzybDataSourceMapper;
import com.zebone.platform.common.support.User;
import com.zebone.platform.framework.support.IUser;
import com.zebone.platform.modules.dao.DataBaseHelper;
import com.zebone.platform.modules.exception.BusException;
import com.zebone.platform.modules.utils.JsonUtil;
@Service
public class GzybDataSourceService {

	@Autowired
	private GzybDataSourceMapper gzybDataSourceMapper;

	/**
	 * 015001007001 查询数据源配置列表
	 * 
	 * @param param
	 * @param user
	 * @return
	 */
	public List<GzybDataSource> getList(String param, IUser user) {	
		List<GzybDataSource> list = gzybDataSourceMapper.getList();
		return list;
	}

	/**
	 * 015001007002 保存医保数据源配置信息
	 * 
	 * @param param
	 * @param user
	 * @return
	 */
	public void save(String param, IUser user) {
		GzybDataSource info = JsonUtil.readValue(param, GzybDataSource.class);
		User u = (User) user;
		if (info != null) {
			info.setPkOrg(u.getPkOrg());
			if (info.getPkGzybds() != null) {
				DataBaseHelper.updateBeanByPk(info, false);
			} else {
				info.setCreateTime(new Date());
				info.setCreator(u.getUserName());
				DataBaseHelper.insertBean(info);
			}
		}
	}

	/**
	 * 015001007003 根据数据源配置主键删除数据源配置信息
	 * 
	 * @param param
	 * @param user
	 * @return
	 */
	public void delete(String param, IUser user) {
		String pkds = JsonUtil.getFieldValue(param, "pkds");
		if (pkds == null || "".equals(pkds)) {
			throw new BusException("数据源主键为空！");
		}
		String sql = "delete from ins_gzyb_ds where pk_gzybds=? ";
		DataBaseHelper.execute(sql, new Object[] { pkds });
	}
}
