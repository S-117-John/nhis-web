package com.zebone.nhis.pro.zsba.compay.ins.pub.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.zebone.nhis.common.module.pv.PvEncounter;
import com.zebone.nhis.common.support.DateUtils;
import com.zebone.nhis.pro.zsba.compay.ins.pub.vo.InsZsPubCertOut;
import com.zebone.platform.framework.support.IUser;
import com.zebone.platform.modules.dao.DataBaseHelper;
import com.zebone.platform.modules.exception.BusException;
import com.zebone.platform.modules.utils.JsonUtil;

@Service
public class InsPubCertOutService {
	
	/**
	 * 保存身份识别数据
	 * @param param 实体对象数据
	 * @param user  登录用户
	 */
	@Transactional
	public void saveInsCertOut(String param , IUser user) {
		InsZsPubCertOut insCertOut = JsonUtil.readValue(param, InsZsPubCertOut.class);
		PvEncounter pe =  DataBaseHelper.queryForBean("Select * from pv_encounter where pk_pv = ?", PvEncounter.class, insCertOut.getPkPv());
		if(!pe.getNamePi().equals(insCertOut.getAac003())){
			throw new BusException("持卡人姓名和住院病人姓名不一致！");
		}
		InsZsPubCertOut certOut =  DataBaseHelper.queryForBean("Select * from ins_cert_out where pk_pv = ?", InsZsPubCertOut.class, insCertOut.getPkPv());
		if(DateUtils.getYear(insCertOut.getAke007())==1){
			insCertOut.setAke007(null);
		}
		if(DateUtils.getYear(insCertOut.getAac006())==1){
			insCertOut.setAac006(null);
		}
		if(DateUtils.getYear(insCertOut.getAke014())==1){
			insCertOut.setAke014(null);
		}
		if(certOut==null){
			insCertOut.setPkPi(pe.getPkPi());
			insCertOut.setPkPv(pe.getPkPv());
			DataBaseHelper.insertBean(insCertOut);
		}else{
			insCertOut.setPkCertout(certOut.getPkCertout());
			insCertOut.setPkPi(certOut.getPkPi());
			DataBaseHelper.updateBeanByPk(insCertOut, false);
		}
	}
}