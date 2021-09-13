package com.zebone.nhis.pro.zsba.task;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.zebone.nhis.common.module.pv.PvIpNotice;
import com.zebone.platform.modules.dao.DataBaseHelper;

@Service
public class ZsbaAdtSysMzToZyService {
		
	/**
	 * 获取待同步的入院通知单
	 * @return
	 */
	public List<PvIpNotice> getHaveAdmitList(){
		List<PvIpNotice> list = new ArrayList<PvIpNotice>();
		list = DataBaseHelper.queryForList("select top 500 * from pv_ip_notice "
				+ "where pk_pv_ip is not null and isnull(modifier,'') not like '%同步%'  order by date_admit desc", PvIpNotice.class,new Object[]{});
		return list;
	}
	
	/**
	 * 同步his - mz_to_zy 表的数据
	 * @param listId
	 * @return
	 */
	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public int upDateConfirmFlag(List<String> listId){
		int cnt = 0;
		Map<String,Object> paramMap = new HashMap<String,Object>();
		paramMap.put("ids", listId);
		cnt = DataBaseHelper.update("update mz_to_zy set confirm_flag = '1' "
				+ " where confirm_flag = '0' "
				+ "   and mz_patient_id + '-' + convert(varchar(10),visit_times) in (:ids) ", paramMap);
		return cnt;
	}
	
	/**
	 * 同步nhis - pv_ip_notice 表的 修改人
	 * @param listPk
	 * @return
	 */
	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public int updatePvIpNotice(List<String> listPk){
		int cnt = 0;
		Map<String,Object> paramMap = new HashMap<String,Object>();
		paramMap.put("pks", listPk);
		cnt = DataBaseHelper.update("update pv_ip_notice "
				+ "   set modifier = convert(varchar(10),getdate(),121) + '同步' "
				+ "     , ts = getdate() "
				+ " where isnull(modifier,'') not like '%同步%' "
				+ "   and pk_in_notice in (:pks) ", paramMap);
		return cnt;
	}
}
