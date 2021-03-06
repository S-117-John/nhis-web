package com.zebone.nhis.bl.pub.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;





import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zebone.nhis.bl.pub.dao.BlIpSettleQryMapper;
import com.zebone.nhis.bl.pub.service.IInvPrintService;
import com.zebone.nhis.bl.pub.service.InvMagService;
import com.zebone.nhis.bl.pub.support.InvSettltService;
import com.zebone.nhis.bl.pub.util.AmountConversionUtil;
import com.zebone.nhis.bl.pub.vo.BillInfo;
import com.zebone.nhis.bl.pub.vo.BlInvPrint;
import com.zebone.nhis.bl.pub.vo.InvoiceInfo;
import com.zebone.nhis.bl.pub.vo.OpdtGroupVo;
import com.zebone.nhis.common.module.base.ou.BdOuDept;
import com.zebone.nhis.common.module.bl.BlDeposit;
import com.zebone.nhis.common.module.bl.BlInvoice;
import com.zebone.nhis.common.module.bl.BlInvoiceDt;
import com.zebone.nhis.common.module.bl.BlSettle;
import com.zebone.nhis.common.module.bl.BlSettleDetail;
import com.zebone.nhis.common.module.bl.BlStInv;
import com.zebone.nhis.common.module.bl.opcg.BlOpDt;
import com.zebone.nhis.common.module.pv.PvEncounter;
import com.zebone.nhis.common.module.pv.PvIp;
import com.zebone.nhis.common.support.ApplicationUtils;
import com.zebone.nhis.common.support.CommonUtils;
import com.zebone.nhis.common.support.DateUtils;
import com.zebone.nhis.common.support.EnumerateParameter;
import com.zebone.nhis.common.support.MathUtils;
import com.zebone.platform.common.support.NHISUUID;
import com.zebone.platform.common.support.UserContext;
import com.zebone.platform.framework.support.IUser;
import com.zebone.platform.modules.dao.DataBaseHelper;
import com.zebone.platform.modules.exception.BusException;
import com.zebone.platform.modules.utils.JsonUtil;
@Service("lbInvPrintService")
public class LbInvPrintService implements IInvPrintService {

	@Autowired
	private InvSettltService invSettltService;
	@Autowired
	private InvMagService invMagService;
	
	@Autowired
	private BlIpSettleQryMapper blIpSettleQryMapper;
	
	@Override
	public BlInvPrint getIpInvPrintDataByPkSettle(String pkSettle) {
		BlInvPrint res = new BlInvPrint();
		if(!CommonUtils.isEmptyString(pkSettle)){
			List<BlStInv> stInvs = DataBaseHelper.queryForList("SELECT * FROM bl_st_inv where pk_settle = ?", BlStInv.class, pkSettle);
			if(stInvs==null || stInvs.size()==0)return null;//?????????????????????????????????????????????
			BlSettle st = DataBaseHelper.queryForBean("SELECT * FROM bl_settle where pk_settle = ?", BlSettle.class, pkSettle);
			PvEncounter pe = DataBaseHelper.queryForBean("SELECT * FROM pv_encounter WHERE pk_pv = ?", PvEncounter.class, st.getPkPv());
			BdOuDept dept = DataBaseHelper.queryForBean("SELECT * FROM bd_ou_dept WHERE pk_dept = ?", BdOuDept.class, pe.getPkDept());
			updateInviocedt(pe.getPkPv(),st.getPkSettle());
			PvIp pvIp = DataBaseHelper.queryForBean("SELECT * FROM pv_ip WHERE pk_pv = ?", PvIp.class, st.getPkPv());
			BlDeposit depo =  DataBaseHelper.queryForBean("SELECT * FROM bl_deposit WHERE pk_settle = ? and (eu_dptype = '0' or eu_dptype = '1')", BlDeposit.class, pkSettle);
			List<BlSettleDetail> blSettleDetail = DataBaseHelper.queryForList(" SELECT * FROM Bl_Settle_Detail WHERE pk_settle = ?", BlSettleDetail.class, pkSettle);
			List<BlInvoice> inv = new ArrayList<BlInvoice>();
			for(BlStInv stInv : stInvs){
					BlInvoice invvo = DataBaseHelper.queryForBean(" SELECT * FROM bl_invoice WHERE flag_cancel= 0 and pk_invoice = ?", BlInvoice.class, stInv.getPkInvoice());
					if(invvo !=null && !CommonUtils.isEmptyString(invvo.getPkInvoice())){
						List<BlInvoiceDt> invDt = DataBaseHelper.queryForList(" SELECT * FROM bl_invoice_dt WHERE pk_invoice = ?", BlInvoiceDt.class, invvo.getPkInvoice());
						invvo.setInvDt(invDt);	
						inv.add(invvo);
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
			//res.setAmtPay(stAr.getAmtPay().toString());
			res.setAmtPay("0.00");
			String sql = "select count(*) as jzcs from bl_settle where pk_settle not in (select t1.pk_settle from  bl_settle t1, bl_settle t2 " +
					"where t1.pk_settle = t2.pk_settle_canc and t1.pk_pi = ? ) " +
					"and pk_pi = ? and del_flag = '0' and flag_canc = '0'";
			Map<String, Object> map = DataBaseHelper.queryForMap(sql, st.getPkPi(), st.getPkPi());
			res.setJzcs(map.get("jzcs").toString());
		}
		return res;
	}

	@Override
	public BlInvPrint getOpInvPrintDataByPkSettle(String pkSettle) {
		return null;
	}
	
	/**
	 * ????????????????????????????????????????????????????????????????????????????????????????????????
	 * @param pkPv
	 * @param pkSettle
	 */
	public void updateInviocedt(String pkPv,String pkSettle){
		String sql="select code_bill from bl_ip_dt where del_flag='0' and pk_settle=? group by code_bill";
		List<Map<String,Object>> billMap=DataBaseHelper.queryForList(sql, pkSettle);
		List<String> codeBills=new ArrayList<>();
		for (Map<String,Object> bills : billMap) {
			String codebill=bills.get("codeBill")==null?"":bills.get("codeBill").toString();
			codeBills.add(codebill);
		}
		Map<String,Object> map=new HashMap<String, Object>();
		map.put("pkPv", pkPv);
		map.put("codeBills", codeBills);
		//?????????????????????????????????
		List<Map<String,Object>> resList=new ArrayList<>();//lbYbPubService.qryLbZfSumAmount(map);//"select pk_invoice  from bl_st_inv where del_flag='0' and pk_settle=? "
		
		List<Map<String,Object>> pkInvoices=DataBaseHelper.queryForList("select pk_invoice  from bl_st_inv where del_flag='0' and pk_settle=? ", new Object[]{pkSettle});
		for (Map<String,Object> pkInvoice : pkInvoices) {
			for (Map<String,Object>  resMap : resList) {
				String res="update bl_invoice_dt set note=? where del_flag='0' and pk_invoice=? and code_bill=?";
				DataBaseHelper.update(res, new Object[]{resMap.get("amountPi"),pkInvoice.get("pkInvoice"),resMap.get("codeBill")});
			}
		}
	}
	
	
	@Override
	public Map<String, Object> saveOpInvInfo(BlSettle blSettle,
			List<InvoiceInfo> invoiceInfos) {
		
		List<BlInvoice> blInvoices = new ArrayList<>(); // ??????
		List<BlStInv> blStInvs = new ArrayList<BlStInv>(); // ???????????????????????????
		
		String pkOrg = UserContext.getUser().getPkOrg();
		String pkOpDoctor = UserContext.getUser().getPkEmp();
		String nameUser = UserContext.getUser().getNameEmp();
		
		Map<String,Object> paramMap = new HashMap<>();
		paramMap.put("pkSettle", blSettle.getPkSettle());
		
		/**
		 * ???????????????????????????
		 * 1.??????????????????????????????????????????36????????????????????????????????????(bl_invoice),??????36?????????????????????
		 * 2.?????????????????????(bl_invoice)????????????????????????????????????????????????
		 * 3.????????????????????????(bl_invoice_dt)
		 * */
//		//???????????????????????????
//		Double opCnt = blIpSettleQryMapper.qryOpCntByPkSt(paramMap);
//		
//		//??????????????????????????????
//		List<BlOpDt> opList = DataBaseHelper.queryForList(
//				"select * from bl_op_dt where pk_settle = ?",
//				BlOpDt.class,new Object[]{blSettle.getPkSettle()});
//		
//		//???????????????????????????
//		List<BlOpDt> upDtList = new ArrayList<>();
//		
//		//????????????????????????????????????
//		int invCnt = MathUtils.upRound(MathUtils.div(opCnt, 36D)).intValue();
//		/**??????????????????????????????????????????*/
//		for(int i=1; i<=invCnt; i++){
//			//????????????????????????
//			List<BillInfo> billList = getInvInfo(invoiceInfos.get(0).getMachineName(),invCnt,UserContext.getUser());
//			if(billList==null || billList.size()<=0)
//				throw new BusException("???????????????????????????????????????");
//			
//			BlInvoice bi = new BlInvoice();
//			bi.setPkOrg(pkOrg);
//			bi.setPkInvcate(billList.get(i-1).getPkInvcate());// ??????????????????
//			bi.setCodeInv(billList.get(i-1).getCurCodeInv());// ????????????
//			bi.setPkEmpinvoice(billList.get(i-1).getPkEmpinv());// ??????????????????
//			bi.setDateInv(new Date());// ????????????
//			bi.setNote(DateUtils.dateToStr("yyyy-MM-dd HH:ss:mm", new Date()));//??????????????????
//			bi.setPkEmpInv(pkOpDoctor);// ??????????????????
//			bi.setNameEmpInv(nameUser);
//			bi.setPrintTimes(0);
//			bi.setFlagCancel(EnumerateParameter.ZERO);
//			bi.setFlagCc(EnumerateParameter.ZERO);
//			bi.setFlagCcCancel(EnumerateParameter.ZERO);
//			bi.setPkInvoice(NHISUUID.getKeyId());
//			if(i==1){
//				bi.setAmountInv(blSettle.getAmountSt().doubleValue());// ???????????????
//				bi.setAmountPi(blSettle.getAmountPi().doubleValue());
//			}else{
//				bi.setAmountInv(0D);// ???????????????
//				bi.setAmountPi(0D);
//			}
//			ApplicationUtils.setDefaultValue(bi, true);
//			
//			blInvoices.add(bi);
//			
//			//????????????????????????
//			BlStInv bsi = new BlStInv();
//			bsi.setPkOrg(pkOrg);
//			bsi.setPkSettle(blSettle.getPkSettle());
//			bsi.setPkInvoice(bi.getPkInvoice());
//			ApplicationUtils.setDefaultValue(bsi, true);
//			
//			blStInvs.add(bsi);
//			
//			//???38?????????????????????????????????
//			if(opList!=null && opList.size()>0){
//				int cnt = 0;
//				for(int j =opList.size() - 1; j >= 0; j--){
//					if(cnt<36){
//						opList.get(j).setPkInvoice(bi.getPkInvoice());
//						upDtList.add(opList.get(j));
//						opList.remove(j);
//						cnt++;
//					}
//				}
//			}
//		}
//		
//		if(upDtList!=null && upDtList.size()>0)
//			DataBaseHelper.batchUpdate(DataBaseHelper.getUpdateSql(BlOpDt.class), upDtList);
//		
		/**
		 * ???????????????????????????
		 * 1.?????????????????????????????????????????????????????????????????????
		 * 2.????????????????????????(bl_invoice_dt)
		 */
		//????????????????????????????????????
		List<BlOpDt> opDtList = DataBaseHelper.queryForList("select * from bl_op_dt where pk_settle = ?",
				BlOpDt.class,new Object[]{blSettle.getPkSettle()});
		if(opDtList!=null && opDtList.size()>0){
			for(BlOpDt dt : opDtList){
				//????????????????????????pk_invoice????????????????????????
				if(!CommonUtils.isEmptyString(dt.getPkInvoice())){
					BlSettle st = DataBaseHelper.queryForBean("select pk_settle from bl_settle where pk_settle = (select pk_settle from bl_st_inv where pk_invoice = ?)",
							BlSettle.class, new Object[]{dt.getPkInvoice()});
					//???????????????bl_deposit??????
					BlDeposit depoOld = DataBaseHelper.queryForBean("select * from bl_deposit where pk_settle = ?",
							BlDeposit.class, new Object[]{st.getPkSettle()});
					if(depoOld!=null && !CommonUtils.isEmptyString(depoOld.getPkDepo()))
						DataBaseHelper.execute("update bl_deposit set DT_PAYMODE = ? where pk_settle = ?", new Object[]{depoOld.getDtPaymode(),blSettle.getPkSettle()});
					
					if(st!=null && !CommonUtils.isEmptyString(st.getPkSettle())){
						invoiceInfos.get(0).setPkSettleOld(st.getPkSettle());
						break;
					}
				}
			}
		}
		
		List<OpdtGroupVo> opList = blIpSettleQryMapper.qryDtListByDeptEx(paramMap);
		
		Map<String,List<OpdtGroupVo>> exListMap = new HashMap<>();//??????????????????????????????????????????????????????????????????-
		if(opList!=null && opList.size()>0){
			for(OpdtGroupVo dt : opList){
				List<OpdtGroupVo> list = new ArrayList<>();
				String dtLisgroup = CommonUtils.getString(dt.getDtLisgroup())==null?"":CommonUtils.getString(dt.getDtLisgroup());
				if(exListMap.containsKey(dt.getPkDeptEx()+dtLisgroup)){
					list = exListMap.get(dt.getPkDeptEx()+dtLisgroup);
				}
				list.add(dt);
				exListMap.put(dt.getPkDeptEx()+dtLisgroup, list);
			}
			
			//??????exListMap??????????????????????????????????????????????????????(???????????????????????????????????????)
			if(exListMap!=null && exListMap.size()>0){
				List<BlOpDt> upDtList = new ArrayList<>();//????????????????????????
				
				//????????????????????????
				List<BillInfo> billList = getInvInfo(invoiceInfos.get(0).getMachineName(),exListMap.size(),UserContext.getUser());
				if(billList==null || billList.size()<=0)
					throw new BusException("???????????????????????????????????????");
				else if(billList.size()!=exListMap.size())
					throw new BusException("?????????????????????????????????????????????????????????");
				
				int i = 0;
				for(String key : exListMap.keySet()){
					i++;
					List<OpdtGroupVo> dtList = exListMap.get(key);
					//????????????????????????
					Set<String> pkList = new HashSet<>();
					for(OpdtGroupVo groupVo : dtList){
						pkList.add(groupVo.getPkCgop());
					}
					List<BlOpDt> opCgList = DataBaseHelper.queryForList(
							"select * from bl_op_dt where pk_cgop in ("+CommonUtils.convertSetToSqlInPart(pkList, "pk_cgop")+")",
							BlOpDt.class,new Object[]{});
					//???????????????????????????
					Map<String,Object> amtMap = DataBaseHelper.queryForMap(
							"select sum(amount) amt,sum(amount_pi) amt_pi from bl_op_dt where pk_cgop in ("+CommonUtils.convertSetToSqlInPart(pkList, "pk_cgop")+")",
							new Object[]{});
					if(dtList!=null && dtList.size()>0){ 
						BlInvoice bi = new BlInvoice();
						bi.setPkOrg(pkOrg);
						bi.setPkInvcate(billList.get(0).getPkInvcate());// ??????????????????
						bi.setCodeInv(billList.get(i-1).getCurCodeInv());// ????????????
						bi.setPkEmpinvoice(billList.get(i-1).getPkEmpinv());// ??????????????????
						bi.setDateInv(new Date());// ????????????
						bi.setNote(DateUtils.dateToStr("yyyy-MM-dd HH:ss:mm", new Date()));//??????????????????
						bi.setPkEmpInv(pkOpDoctor);// ??????????????????
						bi.setNameEmpInv(nameUser);
						bi.setPrintTimes(0);
						bi.setFlagCancel(EnumerateParameter.ZERO);
						bi.setFlagCc(EnumerateParameter.ZERO);
						bi.setFlagCcCancel(EnumerateParameter.ZERO);
						bi.setPkInvoice(NHISUUID.getKeyId());
						bi.setAmountInv(amtMap.get("amt")!=null?CommonUtils.getDouble(amtMap.get("amt")):0D);
						bi.setAmountPi(amtMap.get("amtPi")!=null?CommonUtils.getDouble(amtMap.get("amtPi")):0D);
						ApplicationUtils.setDefaultValue(bi, true);
						
						blInvoices.add(bi);
						
						//????????????????????????
						BlStInv bsi = new BlStInv();
						bsi.setPkOrg(pkOrg);
						bsi.setPkSettle(blSettle.getPkSettle());
						bsi.setPkInvoice(bi.getPkInvoice());
						ApplicationUtils.setDefaultValue(bsi, true);
						
						blStInvs.add(bsi);
						
						//??????????????????????????????
						for(BlOpDt upDt : opCgList){
							upDt.setPkInvoice(bi.getPkInvoice());
						}
						upDtList.addAll(opCgList);
					}
				}
				
				//???????????????????????????????????????????????????????????????????????????
				if(!CommonUtils.isEmptyString(invoiceInfos.get(0).getPkSettleOld())){
					this.upSettleInv(invoiceInfos.get(0).getPkSettleOld(), blSettle.getPkSettle(), blInvoices, blStInvs,upDtList);
					
					//?????????????????????
					if(blInvoices!=null && blInvoices.size()>0){
						//????????????????????????
						List<BillInfo> billNewList = getInvInfo(invoiceInfos.get(0).getMachineName(),blInvoices.size(),UserContext.getUser());
						for(int k=0; k<blInvoices.size(); k++){
							blInvoices.get(k).setPkInvcate(billNewList.get(k).getPkInvcate());// ??????????????????
							blInvoices.get(k).setCodeInv(billNewList.get(k).getCurCodeInv());// ????????????
							blInvoices.get(k).setPkEmpinvoice(billNewList.get(k).getPkEmpinv());// ??????????????????
						}
					}
				}
				
				if(upDtList!=null && upDtList.size()>0)
					DataBaseHelper.batchUpdate(DataBaseHelper.getUpdateSql(BlOpDt.class), upDtList);
			}
		}
		
		Map<String,Object> ret = new HashMap<>();
		ret.put("inv", blInvoices);
		ret.put("stInv", blStInvs);
		
		return ret;
	}
	
	/**
	 * ??????????????????
	 * @param count	????????????????????????
	 * @return
	 */
	private List<BillInfo> getInvInfo(String nameMachine,int invCnt,IUser user){
		Map<String,Object> paramMap = new HashMap<>();
		paramMap.put("nameMachine", nameMachine); 	//?????????????????????
		paramMap.put("euType", EnumerateParameter.ZERO);	//??????????????????
		
		String strJson = JsonUtil.writeValueAsString(paramMap);
		
		List<BillInfo> billList = new ArrayList<>();
		Map<String,Object> resMap = invMagService.searchCanUsedEmpInvoices(strJson,user);
		
		//??????????????????
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
			
			/**??????????????????????????????????????????????????????????????????*/
			int invCount = DataBaseHelper.queryForScalar(
					"select count(1) from bl_invoice where code_inv = ?"
					, Integer.class, bill.getCurCodeInv());
			
			if(invCount>0)
				throw new BusException("????????????["+bill.getCurCodeInv()+"]???????????????????????????????????????????????????");
			
			billList.add(bill); 
		}
		
		
		return billList;
	}

	@Override
	public Map<String, Object> invRePrint(Map<String, Object> param) {
		
		if(param!=null && param.size()>0){
			String pkInvoice = null;
			for(String key : param.keySet()){
				if(!CommonUtils.isEmptyString(key)){
					pkInvoice = key;
					break;
				}
			}
			
			//??????????????????
			Map<String,Object> paramMap = new HashMap<>();
			paramMap.put("pkInvoice", pkInvoice);
			
			String euType = blIpSettleQryMapper.qryInvCateType(paramMap);
			if(!CommonUtils.isEmptyString(euType)){
				//???????????????????????????????????????
				if(EnumerateParameter.ZERO.equals(euType)){
					List<BlOpDt> opList = blIpSettleQryMapper.qryOpListByPkinv(paramMap);
					//???????????????????????????????????????
					if(opList!=null && opList.size()>0){
						for(BlOpDt op : opList){
							if(!CommonUtils.isEmptyString(op.getPkInvoice())){
								BlInvoice inv = (BlInvoice)param.get(op.getPkInvoice());
								if(inv!=null){
									op.setPkInvoice(inv.getPkInvoice());
								}
							}
						}
						
						DataBaseHelper.batchUpdate(DataBaseHelper.getUpdateSql(BlOpDt.class), opList);
					}
				}
			}
		}
		
		return null;
	}

	private void upSettleInv(String pkSettleOld,String pkSettleNew,List<BlInvoice> invNewList,List<BlStInv> StInvNewList,List<BlOpDt> cgopNewList) {
		/**
		 * ?????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????
		 * ????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????
		 * */
		if(!CommonUtils.isEmptyString(pkSettleOld) && !CommonUtils.isEmptyString(pkSettleNew)){
			
			Map<String,Object> paramMap = new HashMap<>();
			paramMap.put("pkSettle", pkSettleOld);
			//???????????????????????????????????????
			List<BlInvoice> invOldList = blIpSettleQryMapper.qryStInvInfo(paramMap);
			//????????????????????????????????????
			//paramMap.put("pkSettle", pkSettleNew);
			//List<BlInvoice> invNewList = blIpSettleQryMapper.qryStInvInfo(paramMap);
			
			if((invOldList!=null && invOldList.size()>0)
					 && (invNewList!=null && invNewList.size()>0)){
				//?????????????????????????????????????????????
				List<BlOpDt> cgopOldList = DataBaseHelper.queryForList(
						"select * from bl_op_dt where pk_settle = ?",
						BlOpDt.class,new Object[]{pkSettleOld});
				//??????????????????????????????????????????
				//List<BlOpDt> cgopNewList = DataBaseHelper.queryForList(
						//"select * from bl_op_dt where pk_settle = ?",
						//BlOpDt.class,new Object[]{pkSettleNew});
				
				//????????????????????????
				Set<String> upPkList = new HashSet<>();
				//????????????????????????
				Set<String> delPkList = new HashSet<>();
				
				//??????????????????????????????????????????
				for(BlInvoice invNew : invNewList){
					for(BlInvoice invOld : invOldList){
						if(invNew.getAmountInv().compareTo(invOld.getAmountInv())==0){//????????????
							//?????????????????????????????????????????????????????????
							if(checkInvIden(cgopOldList, cgopNewList, invOld.getPkInvoice(), invNew.getPkInvoice())){
								//??????????????????????????????????????????
								upPkList.add(invOld.getPkInvoice());
								//?????????????????????
								delPkList.add(invNew.getPkInvoice());
								//??????????????????????????????????????????????????????
								for(BlOpDt op : cgopNewList){
									if(invNew.getPkInvoice().equals(op.getPkInvoice())){
										op.setPkInvoice(invOld.getPkInvoice());
									}
								}
							}
						}
					}
				}
				
				/**????????????????????????????????????*/
				for(String pkInv : delPkList){
					for(int i =invNewList.size() - 1; i >= 0; i--){
						if(pkInv.equals(invNewList.get(i).getPkInvoice())){
							invNewList.remove(i);
							break;
						}
					}
				}
				
				//?????????????????????
				for(String pkInv : delPkList){
					for(int i =StInvNewList.size() - 1; i >= 0; i--){
						if(pkInv.equals(StInvNewList.get(i).getPkInvoice())){
							StInvNewList.remove(i);
							break;
						}
					}
				}
				/**????????????????????????????????????*/
				DataBaseHelper.execute(
						"update BL_INVOICE set flag_cancel = '0',print_times=1,date_cancel=null,pk_emp_cancel=null,name_emp_cancel=null where PK_INVOICE in ("+CommonUtils.convertSetToSqlInPart(upPkList, "PK_INVOICE")+")",
						new Object[]{});
				//???????????????????????????????????????
				DataBaseHelper.execute(
						"update bl_st_inv set pk_settle = ? where PK_INVOICE in ("+CommonUtils.convertSetToSqlInPart(upPkList, "PK_INVOICE")+")",
						new Object[]{pkSettleNew});
				
			}
			
		}
	}
	
	/**
	 * ????????????????????????????????????????????????????????????
	 * @param cgopOldList
	 * @param cgopNewList
	 * @param pkInvOld
	 * @param pkInvNew
	 * @return
	 */
	private boolean checkInvIden(List<BlOpDt> cgopOldList,List<BlOpDt> cgopNewList,String pkInvOld,String pkInvNew){
		boolean flag = false;
		if((cgopOldList!=null && cgopOldList.size()>0) && 
				(cgopNewList!=null && cgopNewList.size()>0)){
			for(BlOpDt dtNew : cgopNewList){
				if(pkInvNew.equals(dtNew.getPkInvoice())){//????????????????????????
					for(BlOpDt dtOld : cgopOldList){
						if(pkInvOld.equals(dtOld.getPkInvoice())//????????????????????????
								&& dtNew.getPkDeptEx().equals(dtOld.getPkDeptEx())){//??????????????????
							flag = true;
							break;
						}
					}
				}
				if(flag)
					break;
			}
		}
		return flag;
	}
	
}
