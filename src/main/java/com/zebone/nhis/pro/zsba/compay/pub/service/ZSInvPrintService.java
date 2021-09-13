package com.zebone.nhis.pro.zsba.compay.pub.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zebone.nhis.bl.pub.service.IInvPrintService;
import com.zebone.nhis.bl.pub.service.InvMagService;
import com.zebone.nhis.bl.pub.support.InvSettltService;
import com.zebone.nhis.bl.pub.util.AmountConversionUtil;
import com.zebone.nhis.bl.pub.vo.BillInfo;
import com.zebone.nhis.bl.pub.vo.BlInvPrint;
import com.zebone.nhis.bl.pub.vo.InvoiceInfo;
import com.zebone.nhis.common.module.base.bd.price.BdHp;
import com.zebone.nhis.common.module.base.ou.BdOuDept;
import com.zebone.nhis.common.module.bl.BlDeposit;
import com.zebone.nhis.common.module.bl.BlInvoice;
import com.zebone.nhis.common.module.bl.BlInvoiceDt;
import com.zebone.nhis.common.module.bl.BlSettle;
import com.zebone.nhis.common.module.bl.BlSettleDetail;
import com.zebone.nhis.common.module.bl.BlStInv;
import com.zebone.nhis.common.module.bl.opcg.BlOpDt;
import com.zebone.nhis.common.module.compay.ins.zsba.zsyb.InsZsybSt;
import com.zebone.nhis.common.module.pv.PvEncounter;
import com.zebone.nhis.common.module.pv.PvIp;
import com.zebone.nhis.common.support.ApplicationUtils;
import com.zebone.nhis.common.support.CommonUtils;
import com.zebone.nhis.common.support.DateUtils;
import com.zebone.nhis.common.support.EnumerateParameter;
import com.zebone.nhis.pro.zsba.compay.ins.ksyb.vo.InsZsKsybStKsyb;
import com.zebone.nhis.pro.zsba.compay.ins.qgyb.vo.Business.InsZsbaStQg;
import com.zebone.nhis.pro.zsba.compay.ins.ydyb.vo.InsZsYdybStSnyb;
import com.zebone.nhis.pro.zsba.compay.pub.vo.ZsBlInvPrint;
import com.zebone.nhis.pro.zsba.mz.ins.zsba.vo.InsQgybSt;
import com.zebone.nhis.pro.zsba.mz.ins.zsba.vo.InsSgsybSt;
import com.zebone.nhis.pro.zsba.mz.pub.dao.ZsbaBlIpSettleQryMapper;
import com.zebone.nhis.pro.zsba.mz.pub.support.BoaiInvSettltService;
import com.zebone.platform.Application;
import com.zebone.platform.common.support.NHISUUID;
import com.zebone.platform.common.support.UserContext;
import com.zebone.platform.framework.support.IUser;
import com.zebone.platform.modules.dao.DataBaseHelper;
import com.zebone.platform.modules.exception.BusException;
import com.zebone.platform.modules.utils.JsonUtil;

/**
 * 中山住院发票打印服务
 * @author yangxue
 *
 */
@Service("zsInvPrintService")
public class ZSInvPrintService implements IInvPrintService {
	
	@Autowired
	private InvSettltService invSettltService;
	@Autowired
	private InvMagService invMagService;
	@Autowired
	private ZsInvSettltService zsInvSettltService;
	@Autowired
	private ZsbaBlIpSettleQryMapper blIpSettleQryMapper;
	@Autowired
	private BoaiInvSettltService boaiInvSettltService;
	
	/**
	 * 获取住院发票打印信息
	 */
	public BlInvPrint getIpInvPrintDataByPkSettle(String pkSettle) {
		BlInvPrint res = new BlInvPrint();
		if(!CommonUtils.isEmptyString(pkSettle)){
			BlSettle st = DataBaseHelper.queryForBean("SELECT * FROM bl_settle where pk_settle = ?", BlSettle.class, pkSettle);
			BdHp hp = DataBaseHelper.queryForBean("SELECT * FROM bd_hp where pk_hp = ?", BdHp.class, st.getPkInsurance());
			InsZsybSt insSt  = null;
			if(hp.getCode().equals("00021") || hp.getCode().equals("00022") || hp.getCode().equals("00023") || hp.getCode().equals("00024")
					||hp.getCode().equals("00025")){
				 insSt = DataBaseHelper.queryForBean("SELECT * FROM ins_st WHERE pk_settle = ?", InsZsybSt.class, pkSettle);
				res.setInsSt(insSt);
				//res.set
			}else if(hp.getCode().equals("00031")){
				InsZsYdybStSnyb ydSt = DataBaseHelper.queryForBean("SELECT * FROM ins_st_snyb WHERE pk_settle = ?", InsZsYdybStSnyb.class, pkSettle);
/*				insSt = new InsZsybSt();
				insSt.setTczfje(ydSt.getAkb068());
				insSt.setGwytczf(new BigDecimal("0.00"));*/
				
			}
			else if(hp.getCode().equals("00031")){
				InsZsKsybStKsyb ydSt = DataBaseHelper.queryForBean("SELECT * FROM ins_st_ksyb WHERE pk_settle = ?", InsZsKsybStKsyb.class, pkSettle);
/*				insSt = new InsZsybSt();
				insSt.setTczfje(ydSt.getAkb068());
				insSt.setGwytczf(new BigDecimal("0.00"));*/
				
			}
			
			PvEncounter pe = DataBaseHelper.queryForBean("SELECT * FROM pv_encounter WHERE pk_pv = ?", PvEncounter.class, st.getPkPv());
			BdOuDept dept = DataBaseHelper.queryForBean("SELECT * FROM bd_ou_dept WHERE pk_dept = ?", BdOuDept.class, pe.getPkDept());
			PvIp pvIp = DataBaseHelper.queryForBean("SELECT * FROM pv_ip WHERE pk_pv = ?", PvIp.class, st.getPkPv());
			//BlSettleAr stAr = DataBaseHelper.queryForBean("SELECT * FROM bl_settle_ar WHERE pk_settle = ?", BlSettleAr.class, pkSettle);
			//BlDeposit depo =  DataBaseHelper.queryForBean("SELECT * FROM bl_deposit WHERE pk_settle = ? and (eu_dptype = '0' or eu_dptype = '1')", BlDeposit.class, pkSettle);
			List<BlSettleDetail> blSettleDetail = DataBaseHelper.queryForList(" SELECT * FROM Bl_Settle_Detail WHERE pk_settle = ?", BlSettleDetail.class, pkSettle);
			List<BlStInv> stInvs = DataBaseHelper.queryForList("SELECT * FROM bl_st_inv where pk_settle = ?", BlStInv.class, pkSettle);
			List<BlInvoice> inv = new ArrayList<BlInvoice>();
			for(BlStInv stInv : stInvs){
					BlInvoice invvo = DataBaseHelper.queryForBean(" SELECT * FROM bl_invoice WHERE flag_cancel= 0 and pk_invoice = ?", BlInvoice.class, stInv.getPkInvoice());
					//BlInvoice invvo = DataBaseHelper.queryForBean(" SELECT * FROM bl_invoice WHERE pk_invoice = ?", BlInvoice.class, stInv.getPkInvoice());
					if(invvo !=null && !CommonUtils.isEmptyString(invvo.getPkInvoice())){
						inv.add(invvo);
						List<BlInvoiceDt> invDt = DataBaseHelper.queryForList(" SELECT b.name as name_bill, case WHEN amount is null " +
								"THEN '0.00' ELSE amount END as amount FROM bl_invoice_dt a right join BD_INVCATE_ITEM b " +
								"on a.pk_bill = b.PK_INVCATEITEM and pk_invoice =  ?", BlInvoiceDt.class, invvo.getPkInvoice());
						invvo.setInvDt(invDt);	
					}
					
				}
			
			res.setBlInvoice(inv);
			res.setBlSettle(st);
			//res.setBlDeposit(depo);
			res.setCodeDept(dept.getCodeDept());
			res.setNameDept(dept.getNameDept());
			res.setBlSettleDetail(blSettleDetail);
			if(pvIp!=null){
				res.setIpTimes(pvIp.getIpTimes().toString());
			}
			res.setAmountCapital(AmountConversionUtil.change(st.getAmountSt().doubleValue()));
			res.setAmountCapitalInv(AmountConversionUtil.changeInv(st.getAmountSt().doubleValue()));
			//res.setAmtPay(stAr.getAmtPay().toString());
			res.setAmtPay("0.00");
			String sql = "select count(*) as jzcs from bl_settle where pk_settle not in (select t1.pk_settle from  bl_settle t1, bl_settle t2 " +
					"where t1.pk_settle = t2.pk_settle_canc and t1.pk_pi = ? ) " +
					"and pk_pi = ? and del_flag = '0' and flag_canc = '0'";
			Map<String, Object> map = DataBaseHelper.queryForMap(sql, st.getPkPi(), st.getPkPi());
			res.setJzcs(map.get("jzcs").toString());
			String dateBegin = null;
			String dateEnd = null;
			if(st.getDateBegin()!=null){
				dateBegin = DateUtils.dateToStr("yyyyMMdd", st.getDateBegin());
			}
			if(st.getDateEnd()!=null){
				dateEnd = DateUtils.dateToStr("yyyyMMdd", st.getDateEnd());
			}
			Map<String,Object> invMap = zsInvSettltService.getBillDetail(st.getPkPv(), pkSettle);	
			List<BlInvoiceDt> invoDt = (List<BlInvoiceDt>)invMap.get("invDt");
			res.setInvoDt(invoDt);
		}
		return res;
	}

	/**
	 * 获取住院发票打印信息（中山博爱）
	 */
	@SuppressWarnings("unchecked")
	public ZsBlInvPrint getZsBaIpInvPrintDataByPkSettle(String param, IUser user) {
		Map<String,Object> paraMap = JsonUtil.readValue( param, Map.class); 

		String pkSettle = (String)paraMap.get("pkSettle");
		ZsBlInvPrint res = new ZsBlInvPrint();
		if(!CommonUtils.isEmptyString(pkSettle)){
			BlSettle st = DataBaseHelper.queryForBean("SELECT * FROM bl_settle where pk_settle = ?", BlSettle.class, pkSettle);
			res.setDsfzf(st.getAmountInsu().toString());
			BdHp hp = DataBaseHelper.queryForBean("SELECT * FROM bd_hp where pk_hp = ?", BdHp.class, st.getPkInsurance());
			
			//查询科研结算付款方，查到了就是科研结算，一个付款方做一次结算，所以查出来的付款方只有一个
			StringBuffer sbSql = new StringBuffer();	
			sbSql.append(" select b.PK_PAYER,b.NAME from (select * from BL_IP_DT union all select * from BL_IP_DT_b) a inner join BD_PAYER b on a.pk_payer = b.pk_payer  ");
			sbSql.append(" where a.pk_settle=? and b.name!='个人' and quan>0");
			sbSql.append(" and pk_cgip not in (select pk_cgip_back from (select * from BL_IP_DT union all select * from BL_IP_DT_b) a inner join BD_PAYER b on a.pk_payer = b.pk_payer ");
			sbSql.append(" where a.pk_settle=? and b.name!='个人' and pk_cgip_back is not null and pk_cgip_back!='') ");
			sbSql.append("  group by b.PK_PAYER,b.NAME");
			List<Map<String, Object>> kyPayerList = DataBaseHelper.queryForList(sbSql.toString(), pkSettle, pkSettle);
			if(kyPayerList.size()>0){
				res.setJsfs(kyPayerList.get(0).get("name").toString());
			}else{
				InsZsybSt insSt  = null;
				res.setJsfs(hp.getName());
				if(hp.getCode().equals("00021") || hp.getCode().equals("00022") || hp.getCode().equals("00023") || hp.getCode().equals("00024")
						||hp.getCode().equals("00025")){
					 insSt = DataBaseHelper.queryForBean("SELECT * FROM ins_st WHERE pk_settle = ?", InsZsybSt.class, pkSettle);
					res.setInsSt(insSt);
					if(insSt==null){
						res.setJsfs("全自费");
					}else{
						BigDecimal xj = (insSt.getXjzfeje().add(insSt.getXjzfuje())).subtract(insSt.getMzdbtczf()).subtract(insSt.getMzyftczf());
						res.setYbzf(insSt.getYlfyze().subtract(xj).toString());
						res.setDsfzf((st.getAmountInsu().subtract(insSt.getYlfyze().subtract(xj))).toString());
					}
				}else if(hp.getCode().equals("00031")){
					InsZsYdybStSnyb ydSt = DataBaseHelper.queryForBean("SELECT * FROM ins_st_snyb WHERE pk_settle = ?", InsZsYdybStSnyb.class, pkSettle);
	/*				insSt = new InsZsybSt();
					insSt.setTczfje(ydSt.getAkb068());
					insSt.setGwytczf(new BigDecimal("0.00"));*/
					if(ydSt==null){
						res.setJsfs("全自费");
					}else{
						res.setYbzf(ydSt.getAkb068().toString());
						res.setDsfzf((st.getAmountInsu().subtract(ydSt.getAkb068())).toString());
					}
				}
				else if(hp.getCode().equals("0004")){
					InsZsKsybStKsyb ydSt = DataBaseHelper.queryForBean("SELECT * FROM ins_st_ksyb WHERE pk_settle = ?", InsZsKsybStKsyb.class, pkSettle);
	/*				insSt = new InsZsybSt();
					insSt.setTczfje(ydSt.getAkb068());
					insSt.setGwytczf(new BigDecimal("0.00"));*/
					if(ydSt==null){
						res.setJsfs("全自费");
					}else{
						res.setYbzf(ydSt.getAke149().toString());
						res.setDsfzf((st.getAmountInsu().subtract(ydSt.getAke149())).toString());
					}
				}
				else if(hp.getCode().substring(0, 4).equals("0005")){
					InsZsbaStQg ydSt = DataBaseHelper.queryForBean("SELECT * FROM ins_st_qg WHERE pk_settle = ? and del_flag = '0'", InsZsbaStQg.class, pkSettle);
					res.setInsStQg(ydSt);
					if(ydSt==null){
						res.setJsfs("全自费");
					}else{
						res.setYbzf((ydSt.getMedfeeSumamt().subtract(ydSt.getPsnPartAmt())).toString());
						res.setDsfzf((st.getAmountInsu().subtract(ydSt.getMedfeeSumamt().subtract(ydSt.getPsnPartAmt()))).toString());
					}
				}
			}
			PvEncounter pe = DataBaseHelper.queryForBean("SELECT * FROM pv_encounter WHERE pk_pv = ?", PvEncounter.class, st.getPkPv());
			BdOuDept dept = DataBaseHelper.queryForBean("SELECT * FROM bd_ou_dept WHERE pk_dept = ?", BdOuDept.class, pe.getPkDept());
			PvIp pvIp = DataBaseHelper.queryForBean("SELECT * FROM pv_ip WHERE pk_pv = ?", PvIp.class, st.getPkPv());
			//BlSettleAr stAr = DataBaseHelper.queryForBean("SELECT * FROM bl_settle_ar WHERE pk_settle = ?", BlSettleAr.class, pkSettle);
			BlDeposit depo =  DataBaseHelper.queryForBean("SELECT * FROM bl_deposit WHERE pk_settle = ? and (eu_dptype = '0' or eu_dptype = '1')", BlDeposit.class, pkSettle);
			List<BlSettleDetail> blSettleDetail = DataBaseHelper.queryForList(" SELECT * FROM Bl_Settle_Detail WHERE pk_settle = ?", BlSettleDetail.class, pkSettle);
			List<BlStInv> stInvs = DataBaseHelper.queryForList("SELECT * FROM bl_st_inv where pk_settle = ?", BlStInv.class, pkSettle);
			List<BlInvoice> inv = new ArrayList<BlInvoice>();
			for(BlStInv stInv : stInvs){
					BlInvoice invvo = DataBaseHelper.queryForBean(" SELECT * FROM bl_invoice WHERE flag_cancel= 0 and pk_invoice = ?", BlInvoice.class, stInv.getPkInvoice());
					//BlInvoice invvo = DataBaseHelper.queryForBean(" SELECT * FROM bl_invoice WHERE pk_invoice = ?", BlInvoice.class, stInv.getPkInvoice());
					if(invvo !=null && !CommonUtils.isEmptyString(invvo.getPkInvoice())){
						inv.add(invvo);
						List<BlInvoiceDt> invDt = DataBaseHelper.queryForList(" SELECT b.name as name_bill, case WHEN amount is null " +
								"THEN '0.00' ELSE amount END as amount FROM bl_invoice_dt a right join BD_INVCATE_ITEM b " +
								"on a.pk_bill = b.PK_INVCATEITEM and pk_invoice =  ? where b.sort_no is not null", BlInvoiceDt.class, invvo.getPkInvoice());
						invvo.setInvDt(invDt);	
					}
					
				}
			
			res.setBlInvoice(inv);
			res.setBlSettle(st);
			res.setBlDeposit(depo);
			res.setCodeDept(dept.getCodeDept());
			res.setNameDept(dept.getNameDept());
			res.setBlSettleDetail(blSettleDetail);
			if(pvIp!=null){
				res.setIpTimes(pvIp.getIpTimes().toString());
			}
			res.setAmountCapital(AmountConversionUtil.change(st.getAmountSt().doubleValue()));
			res.setAmountCapitalInv(AmountConversionUtil.changeInv(st.getAmountSt().doubleValue()));
			//res.setAmtPay(stAr.getAmtPay().toString());
			res.setAmtPay("0.00");
			String sql = "select count(*) as jzcs from bl_settle where pk_settle not in (select t1.pk_settle from  bl_settle t1, bl_settle t2 " +
					"where t1.pk_settle = t2.pk_settle_canc and t1.pk_pi = ? ) " +
					"and pk_pi = ? and del_flag = '0' and flag_canc = '0'";
			Map<String, Object> map = DataBaseHelper.queryForMap(sql, st.getPkPi(), st.getPkPi());
			res.setJzcs(map.get("jzcs").toString());
			String dateBegin = null;
			String dateEnd = null;
			if(st.getDateBegin()!=null){
				dateBegin = DateUtils.dateToStr("yyyyMMdd", st.getDateBegin());
			}
			if(st.getDateEnd()!=null){
				dateEnd = DateUtils.dateToStr("yyyyMMdd", st.getDateEnd());
			}
			Map<String,Object> invMap = zsInvSettltService.getBillDetail(st.getPkPv(), pkSettle);	
			List<BlInvoiceDt> invoDt = (List<BlInvoiceDt>)invMap.get("invDt");
			res.setInvoDt(invoDt);
		}
		return res;
	}
	
	/**
	 * 获取门诊发票打印信息（中山博爱）
	 */
	@SuppressWarnings("unchecked")
	public ZsBlInvPrint getZsBaOpInvPrintDataByPkSettle(String param, IUser user) {
		Map<String,Object> paraMap = JsonUtil.readValue( param, Map.class); 

		String pkSettle = (String)paraMap.get("pkSettle");
		ZsBlInvPrint res = new ZsBlInvPrint();
		if(!CommonUtils.isEmptyString(pkSettle)){
			BlSettle st = DataBaseHelper.queryForBean("SELECT * FROM bl_settle where pk_settle = ?", BlSettle.class, pkSettle);
			res.setDsfzf(st.getAmountDisc().toString());
			//结算主医保计划
			BdHp hp = DataBaseHelper.queryForBean("SELECT * FROM bd_hp where pk_hp = ?", BdHp.class, st.getPkInsurance());
			res.setJsfs(hp.getName());
			if(hp.getCode().equals("310") || hp.getCode().equals("390")){//全国医保
				InsQgybSt ydSt = DataBaseHelper.queryForBean("SELECT * FROM INS_QGYB_ST WHERE pk_settle = ?", InsQgybSt.class, pkSettle);
				//res.setInsQgybSt(ydSt);
				if(ydSt==null){
					res.setJsfs("全自费");
				}else{
					res.setYbzf((new BigDecimal(ydSt.getMedfeeSumamt()).subtract(new BigDecimal(ydSt.getPsnPartAmt()))).toString());
					//res.setDsfzf((st.getAmountInsu().subtract(new BigDecimal(ydSt.getMedfeeSumamt()).subtract(new BigDecimal(ydSt.getPsnPartAmt())))).toString());
					res.setDsfzf(st.getAmountDisc().toString());
				}
			}else if(hp.getCode().equals("00033")){//省工伤医保
				InsSgsybSt ydSt = DataBaseHelper.queryForBean("SELECT * FROM INS_SGSYB_ST WHERE pk_settle = ?", InsSgsybSt.class, pkSettle);
				//res.setInsSgsybSt(ydSt);
				if(ydSt==null){
					res.setJsfs("全自费");
				}else{
					res.setYbzf(ydSt.getBka832().toString());
					//res.setDsfzf((st.getAmountInsu().subtract(new BigDecimal(ydSt.getBka832()))).toString());
					res.setDsfzf(st.getAmountDisc().toString());
				}
			}
			
			PvEncounter pe = DataBaseHelper.queryForBean("SELECT * FROM pv_encounter WHERE pk_pv = ?", PvEncounter.class, st.getPkPv());
			BdOuDept dept = DataBaseHelper.queryForBean("SELECT * FROM bd_ou_dept WHERE pk_dept = ?", BdOuDept.class, pe.getPkDept());
			PvIp pvIp = DataBaseHelper.queryForBean("SELECT * FROM pv_ip WHERE pk_pv = ?", PvIp.class, st.getPkPv());
			BlDeposit depo =  DataBaseHelper.queryForBean("SELECT * FROM bl_deposit WHERE pk_settle = ? and eu_dptype = '0'", BlDeposit.class, pkSettle);
			List<BlSettleDetail> blSettleDetail = DataBaseHelper.queryForList(" SELECT * FROM Bl_Settle_Detail WHERE pk_settle = ?", BlSettleDetail.class, pkSettle);
			List<BlStInv> stInvs = DataBaseHelper.queryForList("SELECT * FROM bl_st_inv where pk_settle = ?", BlStInv.class, pkSettle);
			List<BlInvoice> inv = new ArrayList<BlInvoice>();
			for(BlStInv stInv : stInvs){
				BlInvoice invvo = DataBaseHelper.queryForBean(" SELECT * FROM bl_invoice WHERE flag_cancel= 0 and pk_invoice = ?", BlInvoice.class, stInv.getPkInvoice());
				if(invvo !=null && !CommonUtils.isEmptyString(invvo.getPkInvoice())){
					inv.add(invvo);
					List<BlInvoiceDt> invDt = DataBaseHelper.queryForList("SELECT b.name as name_bill, "+
						"CASE WHEN a.amount is null THEN '0.00' ELSE a.amount END as amount "+
						"FROM bl_invoice_dt a "+
						"RIGHT JOIN BD_INVCATE_ITEM b "+
						"on a.pk_bill = b.PK_INVCATEITEM and a.pk_invoice = ? "+
						"WHERE b.PK_INVCATE=(select PK_INVCATE from BD_INVCATE where CODE='01')", BlInvoiceDt.class, invvo.getPkInvoice());
					invvo.setInvDt(invDt);
				}
			}
			
			res.setBlInvoice(inv);
			res.setBlSettle(st);
			res.setBlDeposit(depo);
			res.setCodeDept(dept.getCodeDept());
			res.setNameDept(dept.getNameDept());
			res.setBlSettleDetail(blSettleDetail);
			if(pvIp!=null){
				res.setIpTimes(pvIp.getIpTimes().toString());
			}
			res.setAmountCapital(AmountConversionUtil.change(st.getAmountSt().doubleValue()));
			res.setAmountCapitalInv(AmountConversionUtil.changeInv(st.getAmountSt().doubleValue()));
			res.setAmtPay("0.00");
			String sql = "select count(*) as jzcs from bl_settle where pk_settle not in (select t1.pk_settle from  bl_settle t1, bl_settle t2 " +
					"where t1.pk_settle = t2.pk_settle_canc and t1.pk_pi = ? ) " +
					"and pk_pi = ? and del_flag = '0' and flag_canc = '0'";
			Map<String, Object> map = DataBaseHelper.queryForMap(sql, st.getPkPi(), st.getPkPi());
			res.setJzcs(map.get("jzcs").toString());
			Map<String,Object> invMap = zsInvSettltService.getBillDetail(st.getPkPv(), pkSettle);	
			List<BlInvoiceDt> invoDt = (List<BlInvoiceDt>)invMap.get("invDt");
			res.setInvoDt(invoDt);
		}
		return res;
	}
	
	@Override
	public BlInvPrint getOpInvPrintDataByPkSettle(String pkSettle) {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public Map<String, Object> saveOpInvInfo(BlSettle blSettle,
			List<InvoiceInfo> invoiceInfos) {
		String pkOrg = UserContext.getUser().getPkOrg();
		String pkOpDoctor = UserContext.getUser().getPkEmp();
		String nameUser = UserContext.getUser().getNameEmp();

		List<BlInvoice> blInvoices = new ArrayList<>(); // 发票
		List<BlInvoiceDt> blInvoiceDts = new ArrayList<BlInvoiceDt>(); // 发票明细
		List<BlStInv> blStInvs = new ArrayList<BlStInv>(); // 写发票与结算的关系

		// 查询本次结算所有项目明细
		List<BlOpDt> opDtList = DataBaseHelper.queryForList(
				"select * from bl_op_dt where del_flag='0' and pk_settle = ?", BlOpDt.class,
				new Object[] { blSettle.getPkSettle() });
		if (opDtList == null) {
			throw new BusException("未获取到收费项目信息，无法打印发票！");
		}
		if (Application.isSqlServer()) {
			String stInvCnt = DataBaseHelper.queryForBean("select count(*) from bl_st_inv stinv "
					+ " left join bl_invoice inv on inv.pk_invoice =stinv.pk_invoice and inv.del_flag='0' and inv.flag_cancel='0'"
					+ " where stinv.pk_settle= ?", String.class, blSettle.getPkSettle());
			if (!"".equals(stInvCnt)) {
				throw new BusException("已有发票信息，请刷新");
			}
		}else{
			int stInvCnt = DataBaseHelper.execute("select stinv.* from bl_st_inv stinv "
					+ "left join bl_invoice inv on inv.pk_invoice =stinv.pk_invoice and inv.del_flag='0' and inv.flag_cancel='0'"
					+ "where stinv.pk_settle= ?", blSettle.getPkSettle());
			if (stInvCnt > 0) {
				throw new BusException("已有发票信息，请刷新");
			}
		}
		List<Map<String, Object>> cateInfos = blIpSettleQryMapper.qyrOpdtItemcate(blSettle.getPkSettle());
		for (Map<String, Object> cateInfo : cateInfos) {
			BlInvoiceDt blInvDt = new BlInvoiceDt();
			blInvDt.setCodeBill(cateInfo.get("codeBill").toString()); // 发票编码
			blInvDt.setNameBill(cateInfo.get("nameBill").toString()); // 发票编码名称
			blInvDt.setAmount(CommonUtils.getDouble(cateInfo.get("amount"))); // 金额
			blInvDt.setPkBill(cateInfo.get("pkBill").toString());//
			
			blInvoiceDts.add(blInvDt);
		}
		invoiceInfos.get(0).setBlInDts(blInvoiceDts);

		Map<String, Object> ret = new HashMap<String, Object>();
		// 获取BL0031（收费结算启用电子票据），参数值为1时打印电子票据
		String eBillFlag = ApplicationUtils.getSysparam("BL0031", false, "1");
		//String eBillFlag = boaiInvSettltService.getBL0031ByNameMachine(invoiceInfos.get(0).getMachineName());
		if ("1".equals(eBillFlag)) {
	
//			ret = boaiInvSettltService.eBillMzOutpatient(blSettle.getPkPv(), UserContext.getUser(), blSettle.getPkSettle(),
//					invoiceInfos.get(0).getFlagPrint(), invoiceInfos.get(0).getMachineName());

			//Map<String, Object> invRet = new HashMap<>(16);
			
			if(blSettle!=null && !CommonUtils.isEmptyString(blSettle.getDtSttype())){
				if("00".equals(blSettle.getDtSttype())) {
					ret = boaiInvSettltService.repEBillRegistration(blSettle.getPkPv(), UserContext.getUser(), blSettle.getPkSettle());
				}else {
					ret = boaiInvSettltService.eBillMzOutpatient(blSettle.getPkPv(), UserContext.getUser(), blSettle.getPkSettle(),
							invoiceInfos.get(0).getFlagPrint(), invoiceInfos.get(0).getMachineName());
				}
			}else{
				ret = boaiInvSettltService.eBillMzOutpatient(blSettle.getPkPv(), UserContext.getUser(), blSettle.getPkSettle(),
						invoiceInfos.get(0).getFlagPrint(), invoiceInfos.get(0).getMachineName());
			}
			
		} else {
			for (InvoiceInfo invoiceInfo : invoiceInfos) {
				if (invoiceInfo.getBlInDts() == null || invoiceInfo.getBlInDts().size() <= 0) {
					throw new BusException("收费结算时，未传入发票号[" + invoiceInfo.getCodeInv() + "]对应的发票明细！");
				}
				
				//获取门诊发票信息
				List<BillInfo> billList = getInvInfo(invoiceInfos.get(0).getMachineName(),1,UserContext.getUser());
				if(billList==null || billList.size()<=0)
					throw new BusException("未获取到发票信息，请检查！");
				
				/**校验新获取的发票号是否重复，重复则抛出异常。*/
//				int invCount = DataBaseHelper.queryForScalar(
//						"select count(1) from bl_invoice where code_inv = ?"
//						, Integer.class, invoiceInfo.getCodeInv());
//				
//				if(invCount>0)
//					throw new BusException("发票号码["+invoiceInfo.getCodeInv()+"]已经被使用，请到票据管理进行修改！");
				
				
				// 插入发票
				BlInvoice bi = new BlInvoice();
				bi.setPkOrg(pkOrg);
				bi.setPkInvcate(billList.get(0).getPkInvcate());// 票据分类主键
				bi.setCodeInv(billList.get(0).getCurCodeInv());// 发票号码
				bi.setPkEmpinvoice(billList.get(0).getPkEmpinv());// 票据领用主键
				bi.setDateInv(new Date());// 发票日期
				bi.setNote(
						DateUtils.dateToStr("yyyy-MM-dd HH:ss:mm", CommonUtils.isEmptyString(invoiceInfo.getDateInv())
								? new Date() : DateUtils.strToDate(invoiceInfo.getDateInv())));// 存放分担日期
				bi.setPkEmpInv(pkOpDoctor);// 发票开立人员
				bi.setNameEmpInv(nameUser);
				bi.setPrintTimes(0);
				bi.setFlagCancel(EnumerateParameter.ZERO);
				bi.setFlagCc(EnumerateParameter.ZERO);
				bi.setFlagCcCancel(EnumerateParameter.ZERO);
				bi.setPkInvoice(NHISUUID.getKeyId());
				bi.setAmountInv(blSettle.getAmountSt().doubleValue());// 发票总金额
				bi.setAmountPi(blSettle.getAmountPi().doubleValue());
				ApplicationUtils.setDefaultValue(bi, true);

				// 插入发票明细
				for (BlInvoiceDt blInvDt : invoiceInfo.getBlInDts()) {
					blInvDt.setPkInvoice(bi.getPkInvoice()); // 关联发票主键
					ApplicationUtils.setDefaultValue(blInvDt, true);

					// BlInvoiceDt blInvoiceDt = new BlInvoiceDt();
					// blInvoiceDt.setCodeBill(blInvDt.getCodeBill()); // 发票编码
					// blInvoiceDt.setNameBill(blInvDt.getNameBill()); // 发票编码名称
					// blInvoiceDt.setAmount(blInvDt.getAmount()); // 金额
					// blInvoiceDt.setPkBill(blInvDt.getPkBill());//
					//blInvoiceDts.add(blInvoiceDt);
					// amtInv = MathUtils.add(amtInv,blInvDt.getAmount());
				}

				blInvoices.add(bi);

				// 写发票与结算的关系
				BlStInv bsi = new BlStInv();
				bsi.setPkOrg(pkOrg);
				bsi.setPkSettle(blSettle.getPkSettle());
				bsi.setPkInvoice(bi.getPkInvoice());
				ApplicationUtils.setDefaultValue(bsi, true);
				blStInvs.add(bsi);
			}

			ret.put("inv", blInvoices);
			ret.put("invDt", blInvoiceDts);
			ret.put("stInv", blStInvs);
		}
		
		return ret;
	}

	/**
	 * 获取门诊发票
	 * @param count	需要打印几张发票
	 * @return
	 */
	private List<BillInfo> getInvInfo(String nameMachine,int invCnt,IUser user){
		Map<String,Object> paramMap = new HashMap<>();
		paramMap.put("nameMachine", nameMachine); 	//本地计算机名称
		paramMap.put("euType", EnumerateParameter.ZERO);	//获取门诊发票
		
		String strJson = JsonUtil.writeValueAsString(paramMap);
		
		List<BillInfo> billList = new ArrayList<>();
		Map<String,Object> resMap = invMagService.searchCanUsedEmpInvoices(strJson,user);
		
		//获取票据张数
		int count = invCnt;
		for(int k=0; k<count; k++){
			BillInfo bill = new BillInfo();
			
			bill.setCurNo(CommonUtils.getInt(resMap.get("curNo")));
			bill.setInvPrefix(resMap.get("invPrefix")!=null?resMap.get("invPrefix").toString():"");
			bill.setPrefix(resMap.get("prefix")!=null?resMap.get("prefix").toString():"");
//			bill.setCntUse(CommonUtils.getInt(resMap.get("cntUse")));
			bill.setPkEmpinv(CommonUtils.getString(resMap.get("pkEmpinv")));
			bill.setPkInvcate(CommonUtils.getString(resMap.get("pkInvcate")));
			
			String curNo = CommonUtils.getString(bill.getCurNo()+k);
			
			if(resMap.get("length")!=null){
				bill.setLength(CommonUtils.getInteger(resMap.get("length")));
				StringBuffer sbf = new StringBuffer(curNo);
				for(int i=0; i<bill.getLength()-curNo.length(); i++){
					sbf.insert(0, "0");
				}
				curNo = sbf.toString();
			}
			
			bill.setCurCodeInv(bill.getPrefix()
					+bill.getInvPrefix()
					+curNo);
			
			/**校验新获取的发票号是否重复，重复则抛出异常。*/
			int invCount = DataBaseHelper.queryForScalar(
					"select count(1) from bl_invoice where code_inv = ?"
					, Integer.class, bill.getCurCodeInv());
			
			if(invCount>0)
				throw new BusException("发票号码["+bill.getCurCodeInv()+"]已经被使用，请到票据管理进行修改！");
			
			billList.add(bill); 
		}
		
		
		return billList;
	}

	@Override
	public Map<String, Object> invRePrint(Map<String, Object> param) {
		// TODO Auto-generated method stub
		return null;
	}
	

}
