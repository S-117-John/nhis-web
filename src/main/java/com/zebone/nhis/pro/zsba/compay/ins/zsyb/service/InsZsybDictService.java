package com.zebone.nhis.pro.zsba.compay.ins.zsyb.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.zebone.nhis.pro.zsba.compay.ins.zsyb.vo.InsZsBaYbDict;
import com.zebone.platform.framework.support.IUser;
import com.zebone.platform.modules.dao.DataBaseHelper;
import com.zebone.platform.modules.exception.BusException;
import com.zebone.platform.modules.utils.JsonUtil;

@Service
public class InsZsybDictService {

	/**
	 * 保存
	 * @param param 实体对象数据
	 * @param user  登录用户
	 * @return InsDict 医保数据字典
	 */
	@Transactional
	public InsZsBaYbDict save(String param , IUser user){
		InsZsBaYbDict insDict = JsonUtil.readValue(param, InsZsBaYbDict.class);
		if(insDict!=null){
			if(insDict.getPkInsdict()!=null){
				String sql = "select * from ins_dict where  pk_insdict = ? and del_flag = '0'";
				InsZsBaYbDict entity = DataBaseHelper.queryForBean(sql, InsZsBaYbDict.class, insDict.getPkInsdict());
				if(!insDict.getCode().equals(entity.getCode())){
				    sql = "select * from ins_dict where  code = ? and pk_insdicttype = ? and del_flag = '0'";
					entity = DataBaseHelper.queryForBean(sql, InsZsBaYbDict.class, insDict.getCode(),insDict.getPkInsdicttype());
					if(entity!=null){
						throw new BusException("编码已存在");
					}
				}
				DataBaseHelper.updateBeanByPk(insDict, false);
			}else{
				String sql = "select * from ins_dict where code = ? and pk_insdicttype = ?  and del_flag = '0'";
				InsZsBaYbDict entity = DataBaseHelper.queryForBean(sql, InsZsBaYbDict.class, insDict.getCode(), insDict.getPkInsdicttype());
				if(entity!=null){
					throw new BusException("编码已存在");
				}
				DataBaseHelper.insertBean(insDict);
			}
		}
		return insDict;
	}
}