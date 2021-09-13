package com.zebone.nhis.pv.adt.service;

import org.springframework.stereotype.Service;

import com.zebone.nhis.common.support.CommonUtils;
import com.zebone.platform.modules.dao.DataBaseHelper;
import com.zebone.platform.modules.exception.BusException;

@Service
public class ZsAdtService {
	
	/**
	 * 从HIS中获取患者编码
	 * @return
	 */
	public String getCodePiFromHis() {
		String codePi;
		DataBaseHelper.update("update gh_config set patient_sn = patient_sn + 1");
		codePi = DataBaseHelper.queryForScalar("select patient_sn from gh_config", String.class);
		if(CommonUtils.isEmptyString(codePi))
			throw new BusException("未获取到新患者编码！");
		
		codePi += "00";
		int length = 12 - codePi.length();
		if( length > 0){
			for(int i = 0 ; i < length ; i ++ ){
				codePi = "0" + codePi;
			}
		}
		return codePi;
	}

	/**
	 * 从HIS中获取住院号
	 * @return
	 */
	public String getCodeIpFromHis() {
		String codeIp;
		DataBaseHelper.update("update zy_configure set inpatient_no =inpatient_no + 1");
		codeIp = DataBaseHelper.queryForScalar("select inpatient_no from zy_configure", String.class);
		
		if(CommonUtils.isEmptyString(codeIp))
			throw new BusException("未获取到新住院号！");
		return codeIp;
	}

}
