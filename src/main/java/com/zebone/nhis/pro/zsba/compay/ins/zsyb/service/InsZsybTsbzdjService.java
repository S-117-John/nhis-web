package com.zebone.nhis.pro.zsba.compay.ins.zsyb.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.zebone.nhis.pro.zsba.compay.ins.zsyb.vo.InsZsBaYbTsbzdj;
import com.zebone.platform.framework.support.IUser;
import com.zebone.platform.modules.dao.DataBaseHelper;
import com.zebone.platform.modules.utils.JsonUtil;

@Service
public class InsZsybTsbzdjService {
	
//	@Autowired private InsZsybTsbzdjMapper insZsybTsbzdjMapper;

	/**
	 * 保存
	 * @param param 实体对象数据
	 * @param user  登录用户
	 * @return InsZsybTsbzdj 中山医保特殊病种登记
	 */
	@Transactional
	public InsZsBaYbTsbzdj save(String param , IUser user){
		InsZsBaYbTsbzdj tsbzdj = JsonUtil.readValue(param, InsZsBaYbTsbzdj.class);
		if(tsbzdj!=null){
			if(tsbzdj.getPkInszsybtsbzdj()!=null){
				DataBaseHelper.updateBeanByPk(tsbzdj, false);
			}else{
				DataBaseHelper.insertBean(tsbzdj);
			}
		}
		return tsbzdj;
	}

}