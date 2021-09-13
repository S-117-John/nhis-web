package com.zebone.nhis.pro.zsba.compay.ins.zsyb.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import net.sf.json.JSONObject;

import com.zebone.nhis.pro.zsba.compay.ins.zsyb.vo.MzDayDetailsData;
import com.zebone.nhis.pro.zsba.compay.ins.zsyb.vo.MzDayInvData;
import com.zebone.nhis.pro.zsba.compay.pub.vo.ZsbaBlDeposit;
import com.zebone.platform.framework.support.IUser;
import com.zebone.platform.modules.dao.DataBaseHelper;
import com.zebone.platform.modules.dao.jdbc.DataSourceRoute;

@Service
public class InsZsybHandler {
	@Resource
	private InsZsybService insZsybService;
	
	/**
	 * 获取旧系统门诊日间手术发票数据
	 * @param param
	 * @param user
	 * @return
	 */
	public List<MzDayInvData> getMzFp(String param , IUser user){
		JSONObject jo = JSONObject.fromObject(param);
		String pkPv = jo.getString("pkPv");
		List<MzDayInvData> mzDayInvData = new ArrayList<MzDayInvData>();
		String sql = "select pk_pv_op from pv_ip_notice where pk_pv_ip = ?";
		Map<String, Object> piMap = DataBaseHelper.queryForMap(sql, pkPv);
		if(piMap!=null && piMap.get("pkPvOp")!=null){
			if(piMap.get("pkPvOp").toString().contains("-")){
				//旧门诊系统    通知单的门诊就诊主键为门诊号加-加次数
				String[] opArr = piMap.get("pkPvOp").toString().trim().split("-");
				DataSourceRoute.putAppId("HIS_bayy");
				mzDayInvData = insZsybService.getMzDayIns(opArr[0], opArr[1]);
				DataSourceRoute.putAppId("default");
				for(MzDayInvData inv : mzDayInvData){
					sql = "select * from bl_deposit where mz_patient_id = ? and ledger_sn = ? and receipt_sn=?";
					ZsbaBlDeposit depo = DataBaseHelper.queryForBean(sql, ZsbaBlDeposit.class, inv.getMzId(), inv.getLedgerSn(), inv.getReceiptSn());
					if(depo!=null){
						inv.setIsImport("1");
					}
					else{
						inv.setIsImport("0");
					}
				}
			}else{
				//新门诊系统
				sql = " select a.ebillno as receiptNo, a.amount_inv as chargeTotal, c.date_st as cashDate, c.pk_settle as pkSettleOp, c.pk_settle as receipt_sn from BL_INVOICE a "
						+ "inner join BL_ST_INV b on a.pk_invoice = b.pk_invoice "
						+ " inner join BL_SETTLE c on b.pk_settle = c.pk_settle "
						+ " inner join bd_hp d on c.pk_insurance = d.pk_hp and d.code = '00011' "
						+ " where pk_pv = ?";
				mzDayInvData = DataBaseHelper.queryForList(sql, MzDayInvData.class, piMap.get("pkPvOp").toString());
				for(MzDayInvData inv : mzDayInvData){
					sql = "select * from bl_deposit where pk_settle_op = ? ";
					ZsbaBlDeposit depo = DataBaseHelper.queryForBean(sql, ZsbaBlDeposit.class, inv.getPkSettleOp());
					if(depo!=null){
						inv.setIsImport("1");
					}
					else{
						inv.setIsImport("0");
					}
				}
			}
			
		}
		return mzDayInvData;
	}

	/**
	 * 获取旧系统门诊日间手术费用明细数据
	 * @param param
	 * @param user
	 * @return
	 */
	public List<MzDayDetailsData> getMzDayopDetail(String param , IUser user){
		JSONObject jo = JSONObject.fromObject(param);
		String pkPv = jo.getString("pkPv");
		String receiptSn = jo.getString("receiptSn");
		List<MzDayDetailsData> mzDayDetailsData = new ArrayList<MzDayDetailsData>();
		String sql = "select pk_pv_op from pv_ip_notice where pk_pv_ip = ?";
		Map<String, Object> piMap = DataBaseHelper.queryForMap(sql, pkPv);
		if(piMap!=null && piMap.get("pkPvOp")!=null){
			if(piMap.get("pkPvOp").toString().contains("-")){
				//旧门诊系统    通知单的门诊就诊主键为门诊号加-加次数
				String[] opArr = piMap.get("pkPvOp").toString().trim().split("-");
				DataSourceRoute.putAppId("HIS_bayy");
				mzDayDetailsData = insZsybService.getMzDayopDetail(opArr[0], opArr[1], receiptSn);
			}else{
				//新门诊系统
				sql = "select a.name_cg as itemName, f.name as freqName, e.name as supplyName, c.dosage as dosage, d.name as dosageUnit,  "+
					" a.price as chargePrice, a.quan as chargeAmount, c.ords as caoyaoFu, a.amount as amount, a.name_emp_app as doctor, b.name_dept as execName, "+
					" a.pk_cgop as pkCgop"+
					" from BL_OP_DT a left join BD_OU_DEPT b on a.pk_dept_app = b.pk_dept "+
					" left join cn_order c on a.pk_cnord = c.pk_cnord  "+
					" left join bd_unit d on c.pk_unit_dos = d.pk_unit"+
					" left join bd_supply e on c.code_supply = e.code"+
					" left join BD_TERM_FREQ f on c.code_freq = f.code"+
					" where a.pk_settle = ? and a.del_flag = '0'";
				mzDayDetailsData = DataBaseHelper.queryForList(sql, MzDayDetailsData.class, receiptSn);
			}
		}
		return mzDayDetailsData;
	}
}
