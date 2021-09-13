package com.zebone.nhis.pro.zsba.compay.ins.zsyb.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.zebone.nhis.pro.zsba.compay.ins.zsyb.vo.MzDayDetailsData;
import com.zebone.nhis.pro.zsba.compay.ins.zsyb.vo.MzDayInvData;
import com.zebone.platform.modules.dao.DataBaseHelper;

@Service
public class InsZsybService {

	/**
	 * 获取旧系统门诊日间手术发票数据
	 * @param mzId
	 * @param visitTimes
	 * @return
	 */
	public List<MzDayInvData> getMzDayIns(String mzId, String visitTimes){
		StringBuffer sqlSb = new StringBuffer("select * from view_dayop_receipt ");
		sqlSb.append(" WHERE  mz_id = ? and mz_times = ?");
	
		List<MzDayInvData> mzDayInvData = DataBaseHelper.queryForList(sqlSb.toString(), MzDayInvData.class, mzId, visitTimes);
		return mzDayInvData;
	}
	
	/**
	 * 获取旧系统门诊日间手术费用明细数据
	 * @param mzId
	 * @param visitTimes
	 * @param receiptSn
	 * @return
	 */
	public List<MzDayDetailsData> getMzDayopDetail(String mzId, String visitTimes, String receiptSn){
		StringBuffer sqlSb = new StringBuffer("select *, (charge_price*charge_amount) as amount from view_dayop_detail ");
		sqlSb.append(" WHERE  mz_patient_id = ? and mz_times = ? and receipt_sn in (?)");
	
		List<MzDayDetailsData> mzDayopDetailData = DataBaseHelper.queryForList(sqlSb.toString(), MzDayDetailsData.class, mzId, visitTimes, receiptSn );
		return mzDayopDetailData;
	}
}
