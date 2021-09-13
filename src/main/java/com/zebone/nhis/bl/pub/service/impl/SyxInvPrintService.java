package com.zebone.nhis.bl.pub.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zebone.nhis.bl.pub.service.CgQryMaintainService;
import com.zebone.nhis.bl.pub.service.IInvPrintService;
import com.zebone.nhis.bl.pub.support.Constant;
import com.zebone.nhis.bl.pub.support.InvSettltService;
import com.zebone.nhis.bl.pub.syx.dao.IpSettleGzgyMapper;
import com.zebone.nhis.bl.pub.syx.dao.OgCgStrategyPubMapper;
import com.zebone.nhis.bl.pub.vo.BlInvPrint;
import com.zebone.nhis.bl.pub.vo.InvoiceInfo;
import com.zebone.nhis.common.module.bl.BlDeposit;
import com.zebone.nhis.common.module.bl.BlInvoice;
import com.zebone.nhis.common.module.bl.BlInvoiceDt;
import com.zebone.nhis.common.module.bl.BlSettle;
import com.zebone.nhis.common.module.bl.BlSettleAr;
import com.zebone.nhis.common.module.bl.BlSettleDetail;
import com.zebone.nhis.common.module.bl.BlStInv;
import com.zebone.nhis.common.module.bl.opcg.BlOpDt;
import com.zebone.nhis.common.module.pv.PvEncounter;
import com.zebone.nhis.common.support.ApplicationUtils;
import com.zebone.nhis.common.support.CommonUtils;
import com.zebone.nhis.common.support.DateUtils;
import com.zebone.nhis.common.support.EnumerateParameter;
import com.zebone.nhis.common.support.MathUtils;
import com.zebone.platform.common.support.NHISUUID;
import com.zebone.platform.common.support.UserContext;
import com.zebone.platform.modules.dao.DataBaseHelper;
import com.zebone.platform.modules.exception.BusException;

/**
 * 中二住院发票打印服务
 * @author lijipeng
 *
 */
@Service("syxInvPrintService")
public class SyxInvPrintService implements IInvPrintService  {
	
	@Autowired
	private InvSettltService invSettltService;
	
	@Autowired
	private IpSettleGzgyMapper ipSettleGzgyMapper;
	
	@Resource
	private CgQryMaintainService cgQryMaintainService;
	
	@Autowired
	private OgCgStrategyPubMapper ogCgStrategyPubMapper;
	
	@Override
	public BlInvPrint getIpInvPrintDataByPkSettle(String pkSettle) {
		BlInvPrint res = new BlInvPrint();
		if(!CommonUtils.isEmptyString(pkSettle)){
			PvEncounter pv = DataBaseHelper.queryForBean("select pv.* from PV_ENCOUNTER pv inner join BL_SETTLE st on st.pk_pv = pv.pk_pv where st.PK_SETTLE = ?", PvEncounter.class, pkSettle);
			BlSettle st = DataBaseHelper.queryForBean("SELECT * FROM bl_settle where pk_settle = ?", BlSettle.class, pkSettle);
			List<BlDeposit> depoList =  DataBaseHelper.queryForList("SELECT * FROM bl_deposit WHERE pk_settle = ? and (eu_dptype = '0' or eu_dptype = '1')", BlDeposit.class, pkSettle);
			List<BlSettleDetail> blSettleDetail = DataBaseHelper.queryForList(" SELECT * FROM Bl_Settle_Detail WHERE pk_settle = ?", BlSettleDetail.class, pkSettle);
			List<BlStInv> stInvs = DataBaseHelper.queryForList("SELECT * FROM bl_st_inv where pk_settle = ?", BlStInv.class, pkSettle);
			List<BlSettleAr> blArList = DataBaseHelper.queryForList("select * from bl_settle_ar where PK_SETTLE = ?", BlSettleAr.class ,pkSettle);
			List<BlInvoice> inv = new ArrayList<BlInvoice>();
			for(int i=0; i<stInvs.size(); i++){
				BlInvoice invvo = DataBaseHelper.queryForBean(" SELECT * FROM bl_invoice WHERE pk_invoice = ?", BlInvoice.class, stInvs.get(i).getPkInvoice());
				if(invvo !=null && !CommonUtils.isEmptyString(invvo.getPkInvoice())){
					inv.add(invvo);
					List<BlInvoiceDt> invDt = DataBaseHelper.queryForList(" SELECT * FROM bl_invoice_dt WHERE pk_invoice = ?", BlInvoiceDt.class, invvo.getPkInvoice());
					/**校验bl0041参数是否为1，获取挂号费用在发票的诊查费和预交金展示*/
					String val = ApplicationUtils.getSysparam("BL0041", false);
					if(Integer.valueOf(pv.getEuPvtype()).compareTo(3)<0 &&
							i==0 &&
							!CommonUtils.isEmptyString(val) && "1".equals(val)){
						//查询该患者挂号费用
						BlSettle pvFee = DataBaseHelper.queryForBean("select * from bl_settle where dt_sttype = '00' and pk_pv = ?", BlSettle.class, pv.getPkPv());
						//查询患者有无结算过(结算过不再处理挂号费)
						Map<String,Object> paramMap = new HashMap<>();
						paramMap.put("pkPv", pv.getPkPv());
						Integer stCnt = ogCgStrategyPubMapper.qryStInfoByPv(paramMap);
						if(pvFee!=null && (stCnt<=0 || stCnt==1)){
							//查询结算关联的费用明细
							List<BlOpDt> dtList = DataBaseHelper.queryForList(
									"select * from bl_op_dt where PK_SETTLE = ?",
									BlOpDt.class,new Object[]{pvFee.getPkSettle()});
							Map<String, Object> param = new HashMap<String, Object>();
							param.put("pkItem", dtList.get(0).getPkItem());
							param.put("pkOrg", dtList.get(0).getPkOrg());
							param.put("euType", Constant.OPINV);
							param.put("flagPd", dtList.get(0).getFlagPd());
							//查询挂号费对应的发票分类编码
							Map<String, Object> resBill =  cgQryMaintainService
									.qryBillCodeByPkItem(param);
							String codeBill = "";
							if(resBill!=null && resBill.get("code")!=null){
								codeBill = resBill.get("code")==null?"":(String)resBill.get("code");
							}
							if(!CommonUtils.isEmptyString(codeBill)){
								boolean flagIns = false;//标志是否匹配到费用分类
								for(BlInvoiceDt dt : invDt){
									if(dt.getCodeBill().equals(codeBill)){
										dt.setAmount(pvFee.getAmountSt().doubleValue());
										flagIns=true;
										break;
									}
								}
								if(!flagIns){
									BlInvoiceDt dt = new BlInvoiceDt();
									dt.setCodeBill(codeBill);
									dt.setNameBill("诊查费");
									dt.setAmount(pvFee.getAmountSt().doubleValue());
									invDt.add(dt);
								}
								//发票表总金额累加挂号费用
								invvo.setAmountInv(pvFee.getAmountSt().add(BigDecimal.valueOf(invvo.getAmountInv())).doubleValue());
								//预交金为挂号费用
								st.setAmountPrep(pvFee.getAmountSt());
							}
						}
					}
					
					invvo.setInvDt(invDt);	
				}
			}
			
			//查询患者医保是否是广州公医
			String valSql = "select attr.val_attr,hp.eu_hptype from bd_hp hp "+
							" inner join bd_dictattr attr on hp.pk_hp=attr.pk_dict "+
							" inner join bd_dictattr_temp tmp on attr.pk_dictattrtemp=tmp.pk_dictattrtemp and tmp.dt_dicttype='03' "+
							" inner join PV_ENCOUNTER pv on hp.PK_HP = pv.PK_INSU "+
							" where pv.pk_pv = ? and tmp.code_attr='0301' ";
			Map<String,Object> retMap = DataBaseHelper.queryForMap(valSql,st.getPkPv());
			
			String varAttr = null;
			String euHptype = null;
			Double amtPi = 0D;//超额自费药
			if(retMap!=null && retMap.size()>0 && retMap.get("valAttr")!=null){
				varAttr = CommonUtils.getString(retMap.get("valAttr"));
				euHptype = CommonUtils.getString(retMap.get("euHptype"));
			}
			
			if((!CommonUtils.isEmptyString(varAttr) && varAttr.equals("1"))
					|| (!CommonUtils.isEmptyString(euHptype) && euHptype.equals("4"))){
				//查询患者药费限额
				Map<String,Object> drugquota = DataBaseHelper.queryForMap(
						"select DRUGQUOTA from ins_gzgy_pv where pk_pv = ?"
						, new Object[]{st.getPkPv()});
				//没有药费限额，如果没有药费限额，查询患者全自费药品
				if(drugquota==null || 
						drugquota.get("drugquota")==null || 
						CommonUtils.getDouble(drugquota.get("drugquota"))==0D){
					//药费限额为0查询该患者有无自费100%药品
					Double drugAmount = DataBaseHelper.queryForScalar("SELECT sum(amount) FROM bl_ip_dt WHERE flag_pd = '1'  and ratio_self = 1 and pk_pv = ? AND pk_settle = ?"
							, Double.class, new Object[]{st.getPkPv(),st.getPkSettle()});
					if(drugAmount!=null && drugAmount>0){
						amtPi = drugAmount;
					} else {
						amtPi = 0D;
					}
				}else{
					Double amount = 0D;//未结算药费
					Double amountDrug = 0D;//未结算甲乙类药费
					Double stDrugAmt = 0D;//已结算药费
					
					//查询公医患者费药合计
					Map<String,Object> amtMap = DataBaseHelper.queryForMap(
							"SELECT sum(dt.amount) amount, "+
							"  sum(case when dt.amount>dt.amount_hppi then dt.amount "+
							"           else 0 end) amount_durg "+
							"FROM bl_ip_dt dt "+
							"WHERE FLAG_PD = '1' AND PK_SETTLE = ?",
							pkSettle);
					//查询本次就诊已结算的药费
					Map<String,Object> paramMap = new HashMap<>();
					paramMap.put("pkPv", st.getPkPv());
					paramMap.put("pkStCanl", pkSettle);//过滤本次结算的药费
					
					Map<String,Object> drugAmtMap = ipSettleGzgyMapper.qryStDrugAmt(paramMap);
					
					if(amtMap!=null && amtMap.size()>0){
						if(amtMap.get("amount")!=null)
							amount = CommonUtils.getDoubleObject(amtMap.get("amount"));
						if(amtMap.get("amountDurg")!=null)
							amountDrug = CommonUtils.getDoubleObject(amtMap.get("amountDurg"));
					}
					
					if(drugAmtMap!=null && drugAmtMap.size()>0 && drugAmtMap.get("stamtdrug")!=null)
						stDrugAmt = CommonUtils.getDouble(drugAmtMap.get("stamtdrug"));
					
					if(stDrugAmt>=CommonUtils.getDouble(drugquota.get("drugquota"))){
						amtPi=amount;
					}else if(MathUtils.add(stDrugAmt,amountDrug)>=CommonUtils.getDouble(drugquota.get("drugquota"))){
						amtPi = MathUtils.add(
								MathUtils.sub(
										MathUtils.add(stDrugAmt, amountDrug)
										, CommonUtils.getDouble(drugquota.get("drugquota")))
								,
								MathUtils.sub(amount, amountDrug));
					} else if(MathUtils.add(stDrugAmt,amountDrug)<CommonUtils.getDouble(drugquota.get("drugquota"))){
						amtPi = MathUtils.sub(amount, amountDrug);
					}
					
				}
			}
			
			res.setCezf(amtPi);
			res.setFlagGy(varAttr);//广州公医标志(1为公医)
			res.setBlInvoice(inv);
			res.setBlSettle(st);
			res.setBlDepositList(depoList);
			res.setBlSettleDetail(blSettleDetail);
			res.setBlArList(blArList);
			
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
		
		Map<String,Object> ret = new HashMap<String, Object>();
		
		for (InvoiceInfo invoiceInfo : invoiceInfos) {
			if(invoiceInfo.getBlInDts()==null||invoiceInfo.getBlInDts().size()<=0){
				throw new BusException("收费结算时，未传入发票号["+invoiceInfo.getCodeInv()+"]对应的发票明细！");
			}
			/**
			 * 插入发票
			 */
			BlInvoice bi = new BlInvoice();
			bi.setPkOrg(pkOrg);
			bi.setPkInvcate(invoiceInfo.getPkInvcate());// 票据分类主键
			bi.setCodeInv(invoiceInfo.getCodeInv());// 发票号码
			bi.setPkEmpinvoice(invoiceInfo.getPkEmpinvoice());// 票据领用主键
			bi.setDateInv(new Date());// 发票日期
			bi.setNote(DateUtils.dateToStr("yyyy-MM-dd HH:ss:mm", CommonUtils.isEmptyString(invoiceInfo.getDateInv())?new Date():DateUtils.strToDate(invoiceInfo.getDateInv())));//存放分担日期
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
			
			/**
			 * 插入发票明细
			 */
			for(BlInvoiceDt blInvDt : invoiceInfo.getBlInDts()){
				BlInvoiceDt blInvoiceDt = new BlInvoiceDt();
				blInvoiceDt.setPkInvoice(bi.getPkInvoice()); // 关联发票主键
				blInvoiceDt.setCodeBill(blInvDt.getCodeBill()); // 发票编码
				blInvoiceDt.setNameBill(blInvDt.getNameBill()); // 发票编码名称
				blInvoiceDt.setAmount(blInvDt.getAmount()); // 金额
				blInvoiceDt.setPkBill(blInvDt.getPkBill());// 
				ApplicationUtils.setDefaultValue(blInvoiceDt, true);
				blInvoiceDts.add(blInvoiceDt); 
				//amtInv = MathUtils.add(amtInv,blInvDt.getAmount());
			}
			
			blInvoices.add(bi);
			
			/**
			 * 写发票与结算的关系
			 */
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
		
		return ret;
	}

	@Override
	public Map<String, Object> invRePrint(Map<String, Object> param) {
		// TODO Auto-generated method stub
		return null;
	}
	
}
