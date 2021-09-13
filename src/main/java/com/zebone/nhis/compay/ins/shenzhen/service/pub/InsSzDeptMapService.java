package com.zebone.nhis.compay.ins.shenzhen.service.pub;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zebone.nhis.common.module.compay.ins.shenzhen.InsDeptMap;
import com.zebone.nhis.compay.ins.shenzhen.dao.pub.InsSzDeptMapper;
import com.zebone.nhis.compay.ins.shenzhen.vo.szxnh.InsDeptMapDataVo;
import com.zebone.platform.common.support.User;
import com.zebone.platform.framework.support.IUser;
import com.zebone.platform.modules.dao.DataBaseHelper;
import com.zebone.platform.modules.exception.BusException;
import com.zebone.platform.modules.utils.JsonUtil;
/**
 * 科室对照
 * @author shihongxin
 *
 */
@Service
public class InsSzDeptMapService {


	@Autowired
	private	InsSzDeptMapper insDeptMapper;
	/**
	 * 保存科室对照信息
	 * 
	 * @param param
	 * @param user
	 */
	public void saveInsDeptMap(String param, IUser user) {
		InsDeptMap info = JsonUtil.readValue(param, InsDeptMap.class);
		User u = (User) user;
		if (info != null) {
			info.setPkOrg(u.getPkOrg());
			info.setDelFlag("0");
			if (info.getPkInsdeptmap() != null) {
				info.setModifier(u.getUserName());
				info.setModityTime(new Date());
				DataBaseHelper.updateBeanByPk(info, false);
			} else {
				info.setCreateTime(new Date());
				info.setCreator(u.getUserName());
				DataBaseHelper.insertBean(info);
			}
		}
	}

	/**
	 * 刪除科室对照关系
	 * 
	 * @param param
	 * @param user
	 */
	public void delete(String param, IUser user) {
		String pkInsdeptmap = JsonUtil.getFieldValue(param, "pkInsdeptmap");
		if (pkInsdeptmap == null || "".equals(pkInsdeptmap))
			throw new BusException("未获得科室对照信息！");
		String sql = "UPDATE INS_DEPT_MAP SET DEL_FLAG='1' WHERE PK_INSDEPTMAP=?";
		DataBaseHelper.update(sql, new Object[] { pkInsdeptmap });
	}

	/**
	 * 获取科室对照列表
	 * 
	 * @param param
	 * @param user
	 * @return
	 */
	public List<InsDeptMapDataVo> getList(String param, IUser user) {
		List<InsDeptMapDataVo> list = insDeptMapper.getList();
		return list;
	}
}
