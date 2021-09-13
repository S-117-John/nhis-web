package com.zebone.nhis.common.service;

import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import com.zebone.nhis.common.module.base.message.SysMessage;
import com.zebone.nhis.common.module.base.message.SysMessageSend;
import com.zebone.nhis.common.support.CommonUtils;
import com.zebone.nhis.common.support.RedisUtils;
import com.zebone.platform.common.support.UserContext;
import com.zebone.platform.modules.core.spring.ServiceLocator;
import com.zebone.platform.modules.dao.DataBaseHelper;
import com.zebone.platform.modules.exception.BusException;

/**
 * 系统级公用服务
 * @author think
 * 
 */
@Service
public class ApplicationService {

	
	@Autowired
	public ServiceLocator serviceLocator;

	@PostConstruct
	public void initWebCach() {

		/**
		 * Web应用重启时清除缓存
		 */
		RedisUtils.delCache("sys:sysparam");
	}


	/**
	 * 查找系统参数code获取 系统参数值
	 * @param code
	 * @param isPub
	 *            是否全部参数
	 * @return
	 */
	public String getSysparam(String code, boolean isPub) {

		String sql = "select val from bd_sysparam where code = ? and del_flag = '0' and pk_org=?";
		String org = UserContext.getUser().getPkOrg();
		String name = "sys:sysparam:";

		if (isPub) {
			org = "~                               "; // 全局机构主键
			name += "public";
		} else {
			name += org;
		}

		String val_o = RedisUtils.getCacheHashObj(name, code, String.class);

		if (val_o == null) {
			List<Map<String, Object>> datas = DataBaseHelper.queryForList(sql, code, org);
			if (datas.size() > 0) {
				String val = CommonUtils.getString(datas.get(0).get("val"));
				if (val != null) {
					RedisUtils.setCacheHashObj(name, code, val);
				}
				return val;
			}

		} else {
			return val_o;
		}
		return null;
	}
	/**
	 * 查找pkDept配置的科室系统参数code
	 * @param code
	 * @param pkDept
	 * @return
	 */
	public String getDeptSysparam(String code,String pkDept) {

		String sql = "select arguval from bd_res_pc_argu where code_argu = ? and pk_dept = ? and flag_stop = '0'";
		List<Map<String, Object>> datas = DataBaseHelper.queryForList(sql,code,pkDept);
		if (datas.size() > 0) {
			String val = CommonUtils.getString(datas.get(0).get("arguval"));
			return val;
		} 
		return null;
	}
	/**
	 * 发送消息
	 * @param message
	 *            具体消息
	 * @param sends
	 *            发送给具体客户端
	 */
	public void saveMessage(SysMessage message, List<SysMessageSend> sends) {

		if (message == null || sends == null) {
			throw new BusException("消息推送失败：缺少消息或接受人！");
		}

		int len = sends.size();

		if (len == 0) {
			throw new BusException("消息推送失败：缺少接受人！");
		}

		//message.setType("0"); // 默认我新消息
		message.setEuHandleStatus("0"); //默认新消息
		DataBaseHelper.insertBean(message);

		for (int i = 0; i < len; i++) {
			sends.get(i).setPkMessage(message.getPkMessage());
		}

		DataBaseHelper.batchUpdate(DataBaseHelper.getInsertSql(SysMessageSend.class), sends);

	}

}
