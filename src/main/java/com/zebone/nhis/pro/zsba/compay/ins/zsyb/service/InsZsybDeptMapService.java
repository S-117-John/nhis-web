package com.zebone.nhis.pro.zsba.compay.ins.zsyb.service;

import java.util.List;

import org.codehaus.jackson.type.TypeReference;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.zebone.nhis.common.module.base.bd.price.BdHp;
import com.zebone.nhis.pro.zsba.compay.ins.zsyb.vo.InsZsBaYbDeptMap;
import com.zebone.platform.common.support.User;
import com.zebone.platform.framework.support.IUser;
import com.zebone.platform.modules.dao.DataBaseHelper;
import com.zebone.platform.modules.exception.BusException;
import com.zebone.platform.modules.utils.JsonUtil;


/**
 * 医保科室对照 -- 业务处理类
 * @author zim
 *
 */
@Service
public class InsZsybDeptMapService {

	/**
	 * 保存医保科室对照
	 * @param param 实体对象数据
	 * @param user  登录用户
	 */
	@Transactional
	public void saveAndMap(String param , IUser user) {
		param = param.substring(param.indexOf("["), param.indexOf("]")+1);
		List<InsZsBaYbDeptMap> data = JsonUtil.readValue(param, new TypeReference<List<InsZsBaYbDeptMap>>(){});
		for(InsZsBaYbDeptMap dm : data){
			if(dm.getPkInsdeptmap()!=null){
				DataBaseHelper.updateBeanByPk(dm, false);
			}else{
				String pkOrg = ((User)user).getPkOrg();
			    String sql = "select pk_hp from bd_hp where del_flag='0' and code='00031'"; //省内普通医保
				List<BdHp> type = DataBaseHelper.queryForList(sql, BdHp.class);
				if(type!=null && type.size()>0){
					String pkHp = type.get(0).getPkHp(); 
					dm.setPkOrg(pkOrg);
					dm.setPkHp(pkHp);
					DataBaseHelper.insertBean(dm);
				}else{
					throw new BusException("保存出错，查不到省内医保");
				}
			}
		}
	}
	
	
	
}
