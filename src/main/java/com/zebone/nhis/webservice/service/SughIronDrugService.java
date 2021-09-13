package com.zebone.nhis.webservice.service;

import java.util.Date;

import org.springframework.stereotype.Service;

import com.zebone.platform.modules.dao.DataBaseHelper;

/***
 * 深大门诊发药机服务
 * @author jd_em
 *
 */
@Service
public class SughIronDrugService {
	
	/**
	 * 更新配药状态
	 * @param presNo
	 */
	public  void updatePresPrepStatus(String presNo){
		StringBuffer sql=new StringBuffer();
		sql.append("update  ex_pres_occ set flag_prep='1' ,pk_emp_prep='-----' ,name_emp_prep='IRON',date_prep=? ,eu_status='2'");
		sql.append(" where pres_no=? and (flag_conf='0' or flag_conf is null) and (flag_canc='0' or flag_canc is null) and eu_status not in ('3','5','9')");
		DataBaseHelper.execute(sql.toString(), new Object[]{new Date(),presNo});
	}
}
