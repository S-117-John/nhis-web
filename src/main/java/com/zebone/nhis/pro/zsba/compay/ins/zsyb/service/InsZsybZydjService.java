package com.zebone.nhis.pro.zsba.compay.ins.zsyb.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.zebone.nhis.pro.zsba.compay.ins.zsyb.vo.InsZsBaYbZydj;
import com.zebone.platform.framework.support.IUser;
import com.zebone.platform.modules.dao.DataBaseHelper;
import com.zebone.platform.modules.utils.JsonUtil;

@Service
public class InsZsybZydjService {
	
//	@Autowired private InsZsybZydjMapper insZsybZydjMapper;

	/**
	 * 保存
	 * @param param 实体对象数据
	 * @param user  登录用户
	 * @return InsZsybZydj 中山医保转院登记
	 */
	@Transactional
	public InsZsBaYbZydj save(String param , IUser user){
		InsZsBaYbZydj zydj = JsonUtil.readValue(param, InsZsBaYbZydj.class);
		if(zydj!=null){
			if(zydj.getPkInszsybzydj()!=null){
				DataBaseHelper.updateBeanByPk(zydj, false);
			}else{
				DataBaseHelper.insertBean(zydj);
			}
		}
		return zydj;
	}
}