package com.zebone.nhis.bl.pub.support;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.zebone.nhis.bl.pub.dao.BlIpSettleQryMapper;
import com.zebone.nhis.bl.pub.service.InvMagService;
import com.zebone.nhis.bl.pub.util.BlcgUtil;
import com.zebone.nhis.bl.pub.vo.BillInfo;
import com.zebone.nhis.bl.pub.vo.InvInfoVo;
import com.zebone.nhis.common.module.bl.BlAmtVo;
import com.zebone.nhis.common.module.bl.BlInvoice;
import com.zebone.nhis.common.module.bl.BlInvoiceDt;
import com.zebone.nhis.common.module.bl.InvItemVo;
import com.zebone.nhis.common.support.CommonUtils;
import com.zebone.nhis.common.support.DateUtils;
import com.zebone.nhis.common.support.EnumerateParameter;
import com.zebone.nhis.common.support.MathUtils;
import com.zebone.nhis.ma.pub.support.ExtSystemProcessUtils;
import com.zebone.platform.common.support.NHISUUID;
import com.zebone.platform.common.support.User;
import com.zebone.platform.common.support.UserContext;
import com.zebone.platform.framework.support.IUser;
import com.zebone.platform.modules.dao.DataBaseHelper;
import com.zebone.platform.modules.exception.BusException;

@Service
public class InvSettltService {

	@Autowired
	private BlIpSettleQryMapper blIpSettleQryMapper;

	@Autowired
	private InvMagService invMagService;

	public  Map<String, Object> invoData(String pkPv,String flagSpec,
										 List<InvInfoVo> invoInfos, IUser iuser,String dateBegin,String dateEnd,String pkSettle,String flagHbPrint) {

		User user = (User)iuser;
		String splitWay = null;
		if(invoInfos != null && invoInfos.size()>0){
			splitWay = invoInfos.get(0).getSplitWay();
		}
		if(EnumerateParameter.ONE.equals(splitWay)){
			return handleAmt(invoInfos,user,pkPv,flagSpec,dateBegin,dateEnd,pkSettle,flagHbPrint);
		}else if (EnumerateParameter.TWO.equals(splitWay)){
			return handleItem(invoInfos,user);
		}else if (EnumerateParameter.THREE.equals(splitWay)){
			return handleDept(invoInfos,user,dateBegin,dateEnd,pkPv);
		}else{
			return handleDate(invoInfos,user,pkPv,dateBegin,dateEnd);
		}

	}

	private Map<String, Object> handleDept(List<InvInfoVo> invoInfos, User user,String dateBegin,String dateEnd,String pkPv) {

		Map<Integer,List<InvInfoVo>> data = new HashMap<Integer,List<InvInfoVo>>();
		List<BlInvoice> invoices = new ArrayList<BlInvoice>();
		List<BlInvoiceDt> invoiceDts = new ArrayList<BlInvoiceDt>();
		Map<String,Object> res = new HashMap<String,Object>();
		for(InvInfoVo vo : invoInfos){
			int page = vo.getPage();
			List<InvInfoVo> invs = data.get(page) == null?new ArrayList<InvInfoVo>():data.get(page);
			invs.add(vo);
			data.put(vo.getPage(), invs);
		}
		for(int i = 1;i<=data.size();i++){
			List<InvInfoVo> vos = data.get(i);
			BlInvoice inv = new BlInvoice();
			inv.setPkInvoice(NHISUUID.getKeyId());
			inv.setPkOrg(user.getPkOrg());
			inv.setPkInvcate(vos.get(0).getInv().getPkInvcate());
			inv.setPkEmpinvoice(vos.get(0).getInv().getPkEmpinv());
			inv.setCodeInv(vos.get(0).getInv().getCurCodeInv());
			inv.setDateInv(new Date());
			inv.setNote("????????????"+vos.get(0).getNote());
			inv.setPkEmpInv(user.getPkEmp());
			inv.setNameEmpInv(user.getNameEmp());
			inv.setPrintTimes(1);
			inv.setFlagCancel("0");
			inv.setDateCancel(null);
			inv.setFlagCc("0");
			inv.setFlagCcCancel("0");
			Double amtTemp = 0.0;
			Double amtTempPi = 0.0;
			for(InvInfoVo item: vos){
				amtTemp = MathUtils.add(amtTemp, item.getAmount().doubleValue());
				amtTempPi = MathUtils.add(amtTempPi, item.getAmountPi().doubleValue());

			}
			inv.setAmountInv(amtTemp.doubleValue());
			//rate = MathUtils.div(amtTemp, amt.doubleValue());
			//inv.setAmountPi(MathUtils.mul(rate,amtPi.doubleValue()));
			inv.setAmountPi(amtTempPi);
			invoices.add(inv);
			List<InvItemVo> itemVos = blIpSettleQryMapper.QryInvItemInfo(pkPv,UserContext.getUser().getPkOrg(), dateBegin, dateEnd, vos,null);
			for(InvItemVo invItem : itemVos){
				BlInvoiceDt invDt = new BlInvoiceDt();
				invDt.setPkInvoicedt(NHISUUID.getKeyId());
				invDt.setPkInvoice(inv.getPkInvoice());
				invDt.setPkBill(invItem.getPkInvcateitem());
				invDt.setNameBill(invItem.getName());
				invDt.setCodeBill(invItem.getCode());
				invDt.setAmount(invItem.getAmount());
				invoiceDts.add(invDt);

			}

		}

		res.put("inv", invoices);
		res.put("invDt", invoiceDts);
		return res;
	}

	private Map<String, Object> handleItem(List<InvInfoVo> invoInfos, User user) {

		Map<Integer,List<InvInfoVo>> data = new HashMap<Integer,List<InvInfoVo>>();
		List<BlInvoice> invoices = new ArrayList<BlInvoice>();
		List<BlInvoiceDt> invoiceDts = new ArrayList<BlInvoiceDt>();
		Map<String,Object> res = new HashMap<String,Object>();

		for(InvInfoVo vo : invoInfos){
			int page = vo.getPage();
			List<InvInfoVo> list = data.get(page) == null?new ArrayList<InvInfoVo>():data.get(page);
			list.add(vo);
			data.put(vo.getPage(), list);
		}
		for(int i = 1;i<= data.size();i++){
			List<InvInfoVo> vos = data.get(i);
			BigDecimal amount = BigDecimal.ZERO;
			BigDecimal amountPi = BigDecimal.ZERO;
			BlInvoice inv = new BlInvoice();
			inv.setPkInvoice(NHISUUID.getKeyId());
			inv.setPkOrg(user.getPkOrg());
			inv.setPkInvcate(vos.get(0).getInv().getPkInvcate());
			inv.setPkEmpinvoice(vos.get(0).getInv().getPkEmpinv());
			inv.setCodeInv(vos.get(0).getInv().getCurCodeInv());
			inv.setDateInv(new Date());
			inv.setNote("????????????"+vos.get(0).getNote());
			inv.setPkEmpInv(user.getPkEmp());
			inv.setNameEmpInv(user.getNameEmp());
			inv.setPrintTimes(1);
			inv.setFlagCancel("0");
			inv.setDateCancel(null);
			inv.setFlagCc("0");
			inv.setFlagCcCancel("0");
			for(InvInfoVo temp :vos){
				amount = amount.add(temp.getAmount());
				amountPi = amountPi.add(temp.getAmountPi());
				BlInvoiceDt tempDt = new BlInvoiceDt();
				tempDt.setPkInvoicedt(NHISUUID.getKeyId());
				tempDt.setPkInvoice(inv.getPkInvoice());
				tempDt.setPkBill(temp.getPkInvcateitem());
				tempDt.setNameBill(temp.getName());
				tempDt.setCodeBill(temp.getCode());
				tempDt.setAmount(temp.getAmount().doubleValue());
				invoiceDts.add(tempDt);
			}

			inv.setAmountInv(amount.doubleValue());
			inv.setAmountPi(amountPi.doubleValue());
			invoices.add(inv);
		}

		res.put("inv", invoices);
		res.put("invDt", invoiceDts);
		return res;
	}

	private Map<String, Object> handleAmt(List<InvInfoVo> invoInfos, User user, String pkPv, String flagSpec , String dateBegin, String dateEnd,String pkSettle,String flagHbPrint) {
		if(BlcgUtil.converToTrueOrFalse(flagSpec) && !BlcgUtil.converToTrueOrFalse(flagHbPrint))	//??????????????????????????????
			return handleAmtBySpec(invoInfos,user,pkPv,flagSpec,dateBegin,dateEnd,pkSettle);
		//????????????????????????pk_settle?????????
		List<InvItemVo> itemVos =blIpSettleQryMapper.QryInvItemInfo(pkPv,UserContext.getUser().getPkOrg(), null, null, null,pkSettle);

		List<BlInvoice> invs = new ArrayList<BlInvoice>();
		List<BlInvoiceDt> invDts = new ArrayList<BlInvoiceDt>();
		Map<String,Object> res = new HashMap<String,Object>(16);
		BlAmtVo amtRe = blIpSettleQryMapper.QryAmtAndPi(pkPv, null, null,pkSettle);
		double amt = 0.0;
		double amtPi = 0.0;
		if(amtRe !=null ){
			amt  = amtRe.getAmtSum();
			amtPi  = amtRe.getAmtPi();
		}

		Double rate = 1.0;
		if(amt<=0){
			res.put("inv", invs);
			res.put("invDt", invDts);
			return res;
		}
		//???????????????????????????
		List<BillInfo> billList = getInvInfo(invoInfos,EnumerateParameter.ONE);
		int count = 0;
		for(InvInfoVo vo : invoInfos){
			//????????????
			vo.setInv(billList.get(count));
			count++;

			rate  = MathUtils.div(vo.getAmount().doubleValue(), amt);
			BlInvoice inv = new BlInvoice();
			inv.setPkInvoice(NHISUUID.getKeyId());
			inv.setPkOrg(user.getPkOrg());
			inv.setPkInvcate(vo.getInv().getPkInvcate());
			inv.setPkEmpinvoice(vo.getInv().getPkEmpinv());
			inv.setCodeInv(vo.getInv().getCurCodeInv());
			inv.setDateInv(new Date());

			inv.setAmountInv(vo.getAmount().doubleValue());
			double amt_pi = MathUtils.mul(rate, amtPi);
			inv.setAmountPi(amt_pi);
			inv.setNote("????????????"+vo.getNote());
			inv.setPkEmpInv(user.getPkEmp());
			inv.setNameEmpInv(user.getNameEmp());
			inv.setPrintTimes(1);
			inv.setFlagCancel("0");
			inv.setDateCancel(null);
			inv.setFlagCc("0");
			inv.setFlagCcCancel("0");
			invs.add(inv);
			for(InvItemVo invItem : itemVos){
				BlInvoiceDt invDt = new BlInvoiceDt();
				invDt.setPkInvoicedt(NHISUUID.getKeyId());
				invDt.setPkInvoice(inv.getPkInvoice());
				invDt.setPkBill(invItem.getPkInvcateitem());
				invDt.setNameBill(invItem.getName());
				invDt.setCodeBill(invItem.getCode());
				//invDt.setAmount(MathUtils.mul(rate, invItem.getAmount()));
				invDt.setAmount(invItem.getAmount());
				invDts.add(invDt);
			}

		}
		res.put("inv", invs);
		res.put("invDt", invDts);
		return res;
	}

	/**
	 * ????????????????????????
	 * @param count	????????????????????????
	 * @return
	 */
	public List<BillInfo> getInvInfo(List<InvInfoVo> invoInfos,String euType){
		Map<String,Object> paramMap = new HashMap<>();
		paramMap.put("nameMachine", invoInfos.get(0).getNameMachine()); 	//?????????????????????
		paramMap.put("euType", euType);

		List<BillInfo> billList = new ArrayList<>();
		Map<String,Object> resMap = invMagService.searchCanUsedEmpInvoices(paramMap);

		//??????????????????
		int count = invoInfos.size();
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

	/**
	 * ??????????????????????????????
	 * @param invoInfos
	 * @param user
	 * @param pkPv
	 * @param flagSpec
	 * @param dateBegin
	 * @param dateEnd
	 */
	private Map<String, Object> handleAmtBySpec(List<InvInfoVo> invoInfos, User user, String pkPv, String flagSpec , String dateBegin, String dateEnd,String pkSettle){
		//?????????????????????
		List<InvItemVo> itemVos = blIpSettleQryMapper.qryNotSpecInvItem(pkSettle);
		//??????????????????
		List<InvItemVo> itemVosT = blIpSettleQryMapper.qrySpecInvItem(pkSettle);
		//??????????????????
		BlAmtVo amtRe = blIpSettleQryMapper.QryAmtAndPiFT(pkSettle);
		//???????????????
		BlAmtVo amtReT = blIpSettleQryMapper.QryAmtAndPiT(pkSettle);

		List<BlInvoice> invs = new ArrayList<BlInvoice>();
		List<BlInvoiceDt> invDts = new ArrayList<BlInvoiceDt>();
		Map<String,Object> res = new HashMap<String,Object>();

		//???????????????????????????
		List<BillInfo> billList = getInvInfo(invoInfos,EnumerateParameter.ONE);

		/**?????????????????????????????????????????????????????????????????????????????????????????????*/
		for(int i=0; i<invoInfos.size(); i++){
			//??????????????????????????????
			invoInfos.get(i).setInv(billList.get(i));

			BlInvoice inv = new BlInvoice();
			inv.setPkInvoice(NHISUUID.getKeyId());
			inv.setPkOrg(user.getPkOrg());
			inv.setPkInvcate(invoInfos.get(i).getInv().getPkInvcate());
			inv.setPkEmpinvoice(invoInfos.get(i).getInv().getPkEmpinv());
			inv.setCodeInv(invoInfos.get(i).getInv().getCurCodeInv());
			inv.setDateInv(new Date());

			inv.setPkEmpInv(user.getPkEmp());
			inv.setNameEmpInv(user.getNameEmp());
			inv.setPrintTimes(1);
			inv.setFlagCancel("0");
			inv.setDateCancel(null);
			inv.setFlagCc("0");
			inv.setFlagCcCancel("0");

			if(i==0){
				inv.setAmountInv(amtRe.getAmtSum());
				inv.setAmountPi(amtRe.getAmtPi());
				inv.setNote("");
				invs.add(inv);
				for(InvItemVo invItem : itemVos){
					BlInvoiceDt invDt = new BlInvoiceDt();
					invDt.setPkInvoicedt(NHISUUID.getKeyId());
					invDt.setPkInvoice(inv.getPkInvoice());
					invDt.setPkBill(invItem.getPkInvcateitem());
					invDt.setNameBill(invItem.getName());
					invDt.setCodeBill(invItem.getCode());
					invDt.setAmount(invItem.getAmount());
					invDts.add(invDt);
				}
			} else {
				inv.setAmountInv(amtReT.getAmtSum());
				inv.setAmountPi(amtReT.getAmtPi());
				inv.setNote("(???)");
				invs.add(inv);
				for(InvItemVo invItem : itemVosT){
					BlInvoiceDt invDt = new BlInvoiceDt();
					invDt.setPkInvoicedt(NHISUUID.getKeyId());
					invDt.setPkInvoice(inv.getPkInvoice());
					invDt.setPkBill(invItem.getPkInvcateitem());
					invDt.setNameBill(invItem.getName());
					invDt.setCodeBill(invItem.getCode());
					invDt.setAmount(invItem.getAmount());
					invDts.add(invDt);
				}
			}
		}
		res.put("inv", invs);
		res.put("invDt", invDts);
		return res;
	}

	private Map<String, Object> handleDate(List<InvInfoVo> invoInfos, User user, String pkPv,String dateBegin, String dateEnd) {

		List<BlInvoice> invs = new ArrayList<BlInvoice>();
		List<BlInvoiceDt> invDts = new ArrayList<BlInvoiceDt>();
		Map<String,Object> res = new HashMap<String,Object>();
		BlAmtVo amtRe = blIpSettleQryMapper.QryAmtAndPi(pkPv, dateBegin, dateEnd,null);
		double amt = 0.0;
		double amtPi = 0.0;
		if(amtRe !=null ){
			amt  = amtRe.getAmtSum();
			amtPi  = amtRe.getAmtPi();
		}

		Double rate = 1.0;
		if(amt<=0){
			res.put("inv", invs);
			res.put("invDt", invDts);
			return res;
		}
		for(InvInfoVo vo : invoInfos){
			rate  = MathUtils.div(vo.getAmount().doubleValue(), amt);
			BlInvoice inv = new BlInvoice();
			inv.setPkInvoice(NHISUUID.getKeyId());
			inv.setPkOrg(user.getPkOrg());
			inv.setPkInvcate(vo.getInv().getPkInvcate());
			inv.setPkEmpinvoice(vo.getInv().getPkEmpinv());
			inv.setCodeInv(vo.getInv().getCurCodeInv());
			inv.setDateInv(new Date());

			inv.setAmountInv(vo.getAmount().doubleValue());
			double amt_pi = MathUtils.mul(rate, amtPi);
			inv.setAmountPi(amt_pi);
			inv.setNote("????????????"+vo.getNote());
			inv.setPkEmpInv(user.getPkEmp());
			inv.setNameEmpInv(user.getNameEmp());
			inv.setPrintTimes(1);
			inv.setFlagCancel("0");
			inv.setDateCancel(null);
			inv.setFlagCc("0");
			inv.setFlagCcCancel("0");
			invs.add(inv);

			List<InvItemVo> itemVos = blIpSettleQryMapper.QryInvItemInfo(pkPv,UserContext.getUser().getPkOrg(),
					DateUtils.dateToStr("yyyyMMddHHmmss", vo.getDateSplitBegin()),DateUtils.dateToStr("yyyyMMddHHmmss", vo.getDateSplitEnd()), null,null);

			for(InvItemVo invItem : itemVos){
				BlInvoiceDt invDt = new BlInvoiceDt();
				invDt.setPkInvoicedt(NHISUUID.getKeyId());
				invDt.setPkInvoice(inv.getPkInvoice());
				invDt.setPkBill(invItem.getPkInvcateitem());
				invDt.setNameBill(invItem.getName());
				invDt.setCodeBill(invItem.getCode());
				invDt.setAmount(invItem.getAmount());
				invDts.add(invDt);
			}

		}
		res.put("inv", invs);
		res.put("invDt", invDts);
		return res;
	}

	public Map<String, Object> getBillDetail(String pkPv, String dateBegin, String dateEnd) {
		List<InvItemVo> itemVos =blIpSettleQryMapper.QryInvItemInfo(pkPv,UserContext.getUser().getPkOrg(), dateBegin, dateEnd, null,null);
		Map<String,Object> res = new HashMap<String,Object>();
		List<BlInvoiceDt> invDts = new ArrayList<BlInvoiceDt>();

		for(InvItemVo invItem : itemVos){
			BlInvoiceDt invDt = new BlInvoiceDt();
			invDt.setNameBill(invItem.getName());
			invDt.setAmount(invItem.getAmount());
			invDts.add(invDt);
		}
		res.put("invDt", invDts);
		return res;
	}

	/**
	 * ????????????????????????
	 * @param pkPv
	 * @param invoInfos
	 * @param iuser
	 * @param dateBegin
	 * @param dateEnd
	 * @param pkSettle
	 * @param flagPrint
	 * @return
	 */
	public Map<String, Object> eBillOutpatient(String pkPv,
											   List<InvInfoVo> invoInfos, IUser iuser,String dateBegin,String dateEnd,String pkSettle,String flagPrint){
		Map<String, Object> retInv = new HashMap<>(16);

		Map<String,Object> paramMap = new HashMap<>(16);
		paramMap.put("pkPv", pkPv);
		paramMap.put("pkSettle", pkSettle);
		paramMap.put("flagPrint", flagPrint);//????????????????????????
		paramMap.put("invoInfos", invoInfos);
		paramMap.put("dateBegin", dateBegin);
		paramMap.put("dateEnd", dateEnd);

		//???????????????????????????
		List<BillInfo> billList = getInvInfo(invoInfos,EnumerateParameter.ONE);
		paramMap.put("billList", billList);

		//??????????????????????????????????????????
		retInv = (Map<String, Object>) ExtSystemProcessUtils.processExtMethod("EBillInfo", "eBillOutpatient", new Object[]{paramMap,iuser});

		return retInv;
	}

	/**
	 * ????????????????????????
	 * @param pkSettle
	 * @param user
	 */
	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public void billCancel(String pkSettle,IUser user){
		Map<String,Object> paramMap = new HashMap<>(16);
		paramMap.put("pkSettle", pkSettle);

		//??????????????????????????????????????????
		ExtSystemProcessUtils.processExtMethod("EBillInfo", "billCancel", new Object[]{paramMap,user});
	}

	/**
	 *??????????????????????????????????????????
	 * @param pkSettle
	 * @param user
	 */
	public void billCancelNoNewTrans(String pkSettle,IUser user){
		Map<String,Object> paramMap = new HashMap<>(16);
		paramMap.put("pkSettle", pkSettle);

		//??????????????????????????????????????????
		ExtSystemProcessUtils.processExtMethod("EBillInfo", "billCancel", new Object[]{paramMap,user});
	}

	/**
	 * ????????????????????????
	 * @param pkSettle
	 * @param user
	 */
	public void rePaperInv(List<BillInfo> billList,String pkSettle,IUser user){
		Map<String,Object> paramMap = new HashMap<>(16);
		paramMap.put("pkSettle", pkSettle);
		paramMap.put("billList", billList);

		//??????????????????????????????????????????
		ExtSystemProcessUtils.processExtMethod("EBillInfo", "rePaperInv", new Object[]{paramMap,user});
	}
	
	/**
	 * ????????????????????????(?????????)
	 * @param pkSettle
	 * @param user
	 */
	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public void drawRePaperBill(List<BillInfo> billList,String pkSettle,IUser user){
		Map<String,Object> paramMap = new HashMap<>(16);
		paramMap.put("pkSettle", pkSettle);
		paramMap.put("billList", billList);

		//??????????????????????????????????????????
		ExtSystemProcessUtils.processExtMethod("EBillInfo", "rePaperInv", new Object[]{paramMap,user});
	}

	/**
	 * ??????????????????
	 * @param billInfo
	 * @param user
	 */
	public void invCanl(BillInfo billInfo,IUser user){
		Map<String,Object> paramMap = new HashMap<>(16);
		if(billInfo!= null){
			paramMap.putAll(DataBaseHelper.queryForMap("select EU_TYPE from BD_INVCATE where PK_INVCATE=?",new Object[]{billInfo.getPkInvcate()}));
		}

		//??????????????????????????????????????????
		ExtSystemProcessUtils.processExtMethod("EBillInfo", "invCanl", new Object[]{paramMap,user});
	}

	/**
	 * ??????????????????????????????
	 * @param pkPv
	 * @param iuser
	 * @param pkSettle
	 * @param flagPrint
	 * @return
	 */
	public Map<String, Object> eBillRegistration(String pkPv,IUser iuser,String pkSettle,String flagPrint,String nameMachine){
		Map<String, Object> retInv = new HashMap<>(16);

		Map<String,Object> paramMap = new HashMap<>(16);
		paramMap.put("pkPv", pkPv);
		paramMap.put("pkSettle", pkSettle);
		paramMap.put("flagPrint", flagPrint);//????????????????????????

		//???????????????????????????
		List<InvInfoVo> invoInfos = new ArrayList<>();
		InvInfoVo invInfo = new InvInfoVo();
		invInfo.setNameMachine(nameMachine);
		invoInfos.add(invInfo);
		List<BillInfo> billList = getInvInfo(invoInfos,EnumerateParameter.ZERO);
		paramMap.put("billList", billList);

		//??????????????????????????????????????????
		retInv = (Map<String, Object>) ExtSystemProcessUtils.processExtMethod("EBillInfo", "eBillRegistration", new Object[]{paramMap,iuser});

		return retInv;
	}

	/**
	 * ????????????????????????????????????
	 * @param pkPv
	 * @param iuser
	 * @param pkSettle
	 * @param flagPrint
	 * @param nameMachine
	 * @return
	 */
	public Map<String, Object> eBillMzOutpatient(String pkPv,IUser iuser,String pkSettle,String flagPrint,String nameMachine){
		Map<String, Object> retInv = new HashMap<>(16);

		Map<String,Object> paramMap = new HashMap<>(16);
		paramMap.put("pkPv", pkPv);
		paramMap.put("pkSettle", pkSettle);
		paramMap.put("flagPrint", flagPrint);//????????????????????????

		List<InvInfoVo> invoInfos = new ArrayList<>();
		InvInfoVo invInfo = new InvInfoVo();
		invInfo.setNameMachine(nameMachine);
		invoInfos.add(invInfo);
		List<BillInfo> billList = new ArrayList<BillInfo>();
		if("1" .equals(flagPrint)) {
			billList = getInvInfo(invoInfos,EnumerateParameter.ZERO);
		} 
		paramMap.put("billList", billList);

		//??????????????????????????????????????????
		retInv = (Map<String, Object>) ExtSystemProcessUtils.processExtMethod("EBillInfo", "eBillMzOutpatient", new Object[]{paramMap,iuser});

		return retInv;
	}
	
	/**
	 * ??????????????????????????????BL0031???
	 * ??????????????????????????????(?????????????????????)
	 * @param nameMachine
	 * @return
	 */
	public String getBL0031ByNameMachine(String nameMachine){
		String sql = "select argu.arguval from bd_res_pc pc "
				+" left join bd_res_pc_argu argu on pc.PK_PC = argu.pk_pc and argu.flag_stop = '0' and argu.DEL_FLAG = '0'"
				+" where pc.flag_active = '1' and pc.del_flag = '0' and pc.eu_addrtype = '0' and argu.code_argu = 'BL0031' and pc.addr = ?";
	
		Map<String,Object> qryMap = DataBaseHelper.queryForMap(sql, nameMachine);
		
		String rtnName = null;
		
		if(qryMap!=null && qryMap.size()>0){
			rtnName = CommonUtils.getString(qryMap.get("arguval"));
		}
			
		if(CommonUtils.isEmptyString(rtnName)){
			rtnName = "0";
		}
		
		return rtnName;
	}
	
	/**
	 * ????????????????????????(??????)
	 * @param pkPv
	 * @param iuser
	 * @param pkSettle
	 * @return
	 */
	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public Map<String, Object> repEBillMzOutpatient(String pkPv,
			   IUser iuser,String pkSettle){
		Map<String, Object> retInv = new HashMap<>(16);
		
		Map<String,Object> paramMap = new HashMap<>(16);
		paramMap.put("pkPv", pkPv);
		paramMap.put("pkSettle", pkSettle);
		paramMap.put("flagPrint", "0");//????????????????????????
		paramMap.put("invoInfos", null);
		paramMap.put("dateBegin", null);
		paramMap.put("dateEnd", null);
		paramMap.put("billList", null);
		
		//??????????????????????????????????????????
		retInv = (Map<String, Object>) ExtSystemProcessUtils.processExtMethod("EBillInfo", "eBillMzOutpatient", new Object[]{paramMap,iuser});
		
		return retInv;
	}
	
	/**
	 * ????????????????????????(????????????)
	 * @param pkPv
	 * @param iuser
	 * @param pkSettle
	 * @return
	 */
	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public Map<String, Object> repEBillOutpatient(String pkPv,
			   IUser iuser,String pkSettle){
		Map<String, Object> retInv = new HashMap<>(16);
		
		Map<String,Object> paramMap = new HashMap<>(16);
		paramMap.put("pkPv", pkPv);
		paramMap.put("pkSettle", pkSettle);
		paramMap.put("flagPrint", "0");//????????????????????????
		paramMap.put("invoInfos", null);
		paramMap.put("dateBegin", null);
		paramMap.put("dateEnd", null);
		paramMap.put("billList", null);
		
		//??????????????????????????????????????????
		retInv = (Map<String, Object>) ExtSystemProcessUtils.processExtMethod("EBillInfo", "eBillOutpatient", new Object[]{paramMap,iuser});
		
		return retInv;
	}
	/**
	 * ????????????????????????(????????????)
	 * @param pkPv
	 * @param iuser
	 * @param pkSettle
	 * @return
	 */
	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public Map<String, Object> repEBillRegistration(String pkPv,
			   IUser iuser,String pkSettle){
		Map<String, Object> retInv = new HashMap<>(16);
		
		Map<String,Object> paramMap = new HashMap<>(16);
		paramMap.put("pkPv", pkPv);
		paramMap.put("pkSettle", pkSettle);
		paramMap.put("flagPrint", "0");//????????????????????????
		paramMap.put("invoInfos", null);
		paramMap.put("dateBegin", null);
		paramMap.put("dateEnd", null);
		paramMap.put("billList", null);
		paramMap.put("flagUpdate", "1");
		
		//??????????????????????????????????????????
		retInv = (Map<String, Object>) ExtSystemProcessUtils.processExtMethod("EBillInfo", "eBillRegistration", new Object[]{paramMap,iuser});
		
		return retInv;
	}
	
	/**  
	* <p>Desc:BL_IP_DT?????????????????????????????????????????????</p>  
	* @author : wangpengyong  
	* @date   : 2021???6???29???  
	*/  
	public String  getPkDept(){
		//bug 35166 ??????????????????6439?????????????????????????????????????????????????????????
        User loginUser = UserContext.getUser();
    	String deptSql = "Select pk_dept From bd_ou_empjob Where pk_emp=? And is_main='1'";
		Map<String,Object> deptMap = DataBaseHelper.queryForMap(deptSql, loginUser.getPkEmp());
		String pkDept = "";
		if(deptMap != null)
		{
			pkDept = deptMap.get("pkDept") == null?null:deptMap.get("pkDept").toString();
		}
		if(StringUtils.isBlank(pkDept)) {
			pkDept = "";
		}
		return pkDept;
	}
	
}
