package com.zebone.nhis.cn.ipdw.service;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zebone.nhis.cn.ipdw.dao.InsOptMapper;
import com.zebone.nhis.cn.ipdw.vo.InsOptParam;
import com.zebone.nhis.common.module.cn.ipdw.InsBear;
import com.zebone.nhis.common.module.cn.ipdw.InsOptDay;
import com.zebone.nhis.common.module.cn.ipdw.InsOptPb;
import com.zebone.platform.framework.support.IUser;
import com.zebone.platform.modules.dao.DataBaseHelper;
import com.zebone.platform.modules.utils.JsonUtil;

@Service
public class InsOptService {
    @Autowired
	private InsOptMapper insDao;
    
	public InsOptParam qryInsOpt(String param,IUser user){
		InsOptParam insParam = JsonUtil.readValue(param, InsOptParam.class);
		return qryInsOpt(insParam);
	}
	public void saveInsOpt(String param,IUser user){
		InsOptParam insParam = JsonUtil.readValue(param, InsOptParam.class);
	    saveInsOpt(insParam);		
	}
	private InsOptParam qryInsOpt(InsOptParam insParam) {
        InsOptParam qryRtn = new InsOptParam();
    
		String instype = insParam.getInsType();
		String pkpv = insParam.getPkPv();
		if("optpb".equals(instype)){
			qryRtn.setInsOptPb(insDao.qryInsOptPb(pkpv));
		}else if("bear".equals(instype)){
			qryRtn.setInsBear(insDao.qryInsBear(pkpv));
		}else if("optday".equals(instype)){
			qryRtn.setInsOptDay(insDao.qryInsOptDay(pkpv));
		}
		return qryRtn;
	}
	private void saveInsOpt(InsOptParam insParam) {
		InsOptPb insOptPb = insParam.getInsOptPb();
		if(insOptPb!=null) {
			saveInsOptPub(insOptPb,insOptPb.getPkOptpb());
			return;
		}
		InsBear insBear = insParam.getInsBear();
		if(insBear!=null) {
			saveInsOptPub(insBear,insBear.getPkInsbear());
			return;
		}
		InsOptDay insOptDay = insParam.getInsOptDay();
		if(insOptDay!=null) {
			saveInsOptPub(insOptDay,insOptDay.getPkOptday());
			return;
		}
	}
	private <T> void saveInsOptPub(T insOptPb,String pkP) {
		 if(insOptPb==null) return;
		 if(StringUtils.isBlank(pkP)){
			 DataBaseHelper.insertBean(insOptPb);
		 }else{
			 DataBaseHelper.updateBeanByPk(insOptPb,false);
		 }
	}
}
