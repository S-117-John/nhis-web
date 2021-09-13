package com.zebone.nhis.pv.pub.service.impl;

import java.util.Map;

import org.springframework.stereotype.Service;

import com.zebone.nhis.common.module.pi.PiMaster;
import com.zebone.nhis.common.module.pv.PvEncounter;
import com.zebone.nhis.common.support.CommonUtils;
import com.zebone.nhis.pv.pub.service.IAdtPsadService;
import com.zebone.platform.modules.dao.DataBaseHelper;
import com.zebone.platform.modules.exception.BusException;

/**
 * 灵璧处理ADT个性化业务接口
 * @author 32916
 *
 */
@Service("LbAdtPsadService")
public class LbAdtPsadService implements IAdtPsadService {

	@Override
	public Map<String,Object> dealAdtPsadMethod(PiMaster pi, PvEncounter pv) 
			throws BusException{
		
		if(pi!=null && !CommonUtils.isEmptyString(pi.getIdNo())){
			//查询视图数据校验该患者是否有欠费结算数据
			Integer cnt = DataBaseHelper.queryForScalar(
					"select count(1) from view_pat_unliquidated where id_no = ?",
					Integer.class, new Object[]{pi.getIdNo()});
			if(cnt!=null && cnt.compareTo(0)>0){
				throw new BusException("该患者在【旧系统】存在未结算或欠费的住院记录，不能再次入院！");
			}
			
		}
		return null;
	}

}
