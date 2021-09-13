package com.zebone.nhis.base.ou.service;



import org.springframework.stereotype.Service;

import com.zebone.nhis.common.module.base.ou.BdOuOper;
import com.zebone.platform.framework.support.IUser;
import com.zebone.platform.modules.dao.DataBaseHelper;
import com.zebone.platform.modules.exception.BusException;
import com.zebone.platform.modules.utils.JsonUtil;

/**
 * 功能服务
 * @author lingjun
 *
 */
@Service
public class OperService {
	
	
	/**
	 * 功能注册
	 * @param param
	 * @param user
	 * @return
	 */
	public BdOuOper saveOper(String param , IUser user){
		 
		BdOuOper oper = JsonUtil.readValue(param,BdOuOper.class);
		
		if(oper.getPkOper() == null){
			int count_oper = DataBaseHelper.queryForScalar("select count(1) from bd_ou_oper "
					+ "where code_oper= ?",Integer.class, oper.getCodeOper()); 
			
			if(count_oper == 0){
				DataBaseHelper.insertBean(oper);
			}else{
				throw new BusException("功能编码重复！");
			}
		}else{
			int count_oper = DataBaseHelper.queryForScalar("select count(1) from bd_ou_oper "
					+ "where code_oper= ? and PK_OPER != ?",Integer.class, oper.getCodeOper(),oper.getPkOper());
			
			if(count_oper == 0){
				DataBaseHelper.updateBeanByPk(oper);
			}else{
				throw new BusException("功能编码重复！");
			}
		}
		
		return oper;
	}

}
