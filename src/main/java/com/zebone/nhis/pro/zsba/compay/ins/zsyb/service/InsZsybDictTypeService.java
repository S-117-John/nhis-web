package com.zebone.nhis.pro.zsba.compay.ins.zsyb.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.zebone.nhis.pro.zsba.compay.ins.zsyb.vo.InsZsBaYbDicttype;
import com.zebone.platform.framework.support.IUser;
import com.zebone.platform.modules.dao.DataBaseHelper;
import com.zebone.platform.modules.exception.BusException;
import com.zebone.platform.modules.utils.JsonUtil;

@Service
public class InsZsybDictTypeService {

	/**
	 * 保存
	 * @param param 实体对象数据
	 * @param user  登录用户
	 * @return InsDictType 医保数据字典类别
	 */
	@Transactional
	public InsZsBaYbDicttype save(String param , IUser user){
		InsZsBaYbDicttype insDictType = JsonUtil.readValue(param, InsZsBaYbDicttype.class);
		if(insDictType!=null){
			if(insDictType.getPkInsdicttype()!=null){
				String sql = "select * from ins_dicttype where  pk_insdicttype = ? and del_flag = '0'";
				InsZsBaYbDicttype entity = DataBaseHelper.queryForBean(sql, InsZsBaYbDicttype.class, insDictType.getPkInsdicttype());
				if(!insDictType.getCodeType().equals(entity.getCodeType())){
				    sql = "select * from ins_dicttype where code_type = ? and del_flag = '0'";
					entity = DataBaseHelper.queryForBean(sql, InsZsBaYbDicttype.class, insDictType.getCodeType());
					if(entity!=null){
						throw new BusException("编码已存在");
					}
				}
				DataBaseHelper.updateBeanByPk(insDictType, false);
			}else{
				String sql = "select * from ins_dicttype where code_type = ? and del_flag = '0'";
				InsZsBaYbDicttype entity = DataBaseHelper.queryForBean(sql, InsZsBaYbDicttype.class, insDictType.getCodeType());
				if(entity!=null){
					throw new BusException("编码已存在");
				}
				DataBaseHelper.insertBean(insDictType);
			}
		}
		return insDictType;
	}
}