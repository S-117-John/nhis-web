package com.zebone.nhis.pro.zsba.mz.pub.service;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.shiro.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.druid.support.json.JSONUtils;
import com.google.common.collect.Maps;
import com.zebone.nhis.bl.pub.vo.InsuCountVo;
import com.zebone.nhis.bl.pub.vo.OpBlCcCradVo;
import com.zebone.nhis.bl.pub.vo.OpBlCcLeaderVo;
import com.zebone.nhis.bl.pub.vo.OpBlCcPayAndPiVo;
import com.zebone.nhis.common.module.base.ou.BdOuEmployee;
import com.zebone.nhis.common.module.bl.BlCc;
import com.zebone.nhis.common.module.bl.BlCcInv;
import com.zebone.nhis.common.module.bl.BlCcPay;
import com.zebone.nhis.common.module.bl.BlInvoice;
import com.zebone.nhis.common.module.bl.BlPi;
import com.zebone.nhis.common.module.bl.BlSettle;
import com.zebone.nhis.common.module.bl.BlSettleDetail;
import com.zebone.nhis.common.module.bl.support.BlConstant;
import com.zebone.nhis.common.support.CommonUtils;
import com.zebone.nhis.common.support.DateUtils;
import com.zebone.nhis.common.support.EnumerateParameter;
import com.zebone.nhis.pro.zsba.mz.datsett.vo.ZsbaOpBlCcVo;
import com.zebone.nhis.pro.zsba.mz.datsett.vo.ZsbaOpSettleBackInvDtl;
import com.zebone.nhis.pro.zsba.mz.pub.dao.ZsbaBlOtherPayMapper;
import com.zebone.nhis.pro.zsba.mz.pub.dao.ZsbaOpblccMapper;
import com.zebone.nhis.pro.zsba.mz.pub.vo.ZsBaBlCcPay;
import com.zebone.nhis.pro.zsba.mz.pub.vo.ZsbaBlCcDsMz;
import com.zebone.platform.common.support.NHISUUID;
import com.zebone.platform.common.support.User;
import com.zebone.platform.common.support.UserContext;
import com.zebone.platform.framework.support.IUser;
import com.zebone.platform.modules.dao.DataBaseHelper;
import com.zebone.platform.modules.exception.BusException;
import com.zebone.platform.modules.utils.JsonUtil;

/**
 * 门诊日结账、汇总结账服务类
 * 
 * @author wangpeng
 * @date 2016年10月21日
 *
 */
@Service
public class ZsbaOpblccService {
	
	@Resource
	private ZsbaOpblccMapper zsbaOpblccMapper;
	@Resource
	private ZsbaBlOtherPayService zsbaBlOtherPayService;
	
	/**
	 * 交易号：007002002002<br>
	 * 根据结账记录获取结账明细<br>
	 * <pre>
	 * 1、结算、账户收退款查询：结算查询 bl_cc_pay 表里eu_paytype=0 ； 账户查询bl_cc_pay 表里eu_paytype=2 ；
	 * </pre>
	 * 
	 * @param  param
	 * @param  user
	 * @return IpBlCcVo
	 * @throws 
	 *
	 * @author wangpeng
	 * @date 2016年10月21日
	 */
	public ZsbaOpBlCcVo getIpBlCcVo(String param, IUser user){
		BlCc blCc = JsonUtil.readValue(param, BlCc.class);
		String pkCc = blCc.getPkCc();
		ZsbaOpBlCcVo vo = new ZsbaOpBlCcVo();
		
		//日结账信息,包含发票以及作废信息
		blCc = zsbaOpblccMapper.getBlCcById(pkCc);
		vo.setBlCc(blCc);
		ZsbaBlCcDsMz zsbaBlCcDsZs = zsbaOpblccMapper.getBlCcDsMzBypkCc(pkCc);
		vo.setZsbaBlCcDsZs(zsbaBlCcDsZs);
		
		List<ZsbaOpSettleBackInvDtl> backDtls = zsbaOpblccMapper.qryInvoBackRj(pkCc);//退票明细 		
		vo.setSettleBackInvDtls(backDtls);
		
		vo.setAmountSt(blCc.getAmtSettle());//结算总金额
		
		//结算信息
		List<ZsBaBlCcPay> blCcPayList = zsbaOpblccMapper.getBlCcPayListByPkCc(pkCc);
		vo.setBlCcPayList(blCcPayList);
	
		//患者账户
		List<ZsBaBlCcPay> blCcPayPiList = zsbaOpblccMapper.getBlCcPayPiListByPkCc(pkCc);
		vo.setBlCcPayPiList(blCcPayPiList);
		
		//支付信息
		List<BlSettleDetail> blSettleDetailList = zsbaOpblccMapper.getBlSettleDetaiListByPkCc(pkCc);
		vo.setBlSettleDetailList(blSettleDetailList);
	
		//挂号信息
		List<InsuCountVo> insuCountVoList = zsbaOpblccMapper.getInsuCountVoListByPkCc(pkCc);
		if(CollectionUtils.isNotEmpty(insuCountVoList)){
			int cnt = 0;
			int cancekCnt = 0;
			for(InsuCountVo ic : insuCountVoList){
				if(ic.getCnt() != null){
					cnt += ic.getCnt();
				}
				if(ic.getCntCancel() != null){
					cancekCnt += ic.getCntCancel();
				}
			}
			vo.setCnt(cnt);
			vo.setCancekCnt(cancekCnt);
			vo.setInsuCountVoList(insuCountVoList);
		}
	
		/**日结账个性化信息查询，每个项目如果有特定的参数要查询，要在这个分支下写*/
  		/*Map<String,Object> rtn = CgProcessUtils.processOpOperatorAccount(vo);
  		if(rtn!=null&&"true".equals(rtn.get("enable"))&&rtn.get("result")!=null){
			return (OpBlCcVo)rtn.get("result");
		}*/
		
		return vo;
	}
	
	/**
	 * 
	 * 交易号：007002002005<br>
	 * 查询已结账业务--挂号记录列表<br>
	 * 
	 * @param  param
	 * @param  user
	 * @return void
	 * @throws 
	 *
	 * @author wangpeng
	 * @date 2016年11月15日
	 */
	@SuppressWarnings("unchecked")
	public List<Map<String,Object>> getBlCcPvEncounterList(String param, IUser user){
		Map<String,Object> paramMap = JsonUtil.readValue(param, Map.class);
		//先查挂号基本信息，再处理挂号费问题
		List<Map<String,Object>> mapEnList = zsbaOpblccMapper.getBlCcPvEncounterList(paramMap);
		
//		StringBuffer sql = new StringBuffer("select pv.pk_pv as key_, sum(cg.amount) amt ");
//		sql.append("from pv_encounter pv ");
//		sql.append("inner join pv_op op on pv.pk_pv=op.pk_pv and op.del_flag = '0' ");
//		sql.append("inner join bl_settle st on st.pk_pv = pv.pk_pv and st.del_flag = '0' and st.eu_pvtype < 3 ");
//		sql.append("inner join bl_op_dt cg on cg.pk_settle=st.pk_settle and cg.flag_pv='1' and cg.del_flag = '0' ");
//		sql.append("inner join bl_cc cc on st.pk_cc=cc.pk_cc and cc.del_flag = '0' ");
//		sql.append("where cc.pk_cc = ? ");
//		Object[] paramSql = {paramMap.get("pkCc").toString()};
//		int i = 1;
//		if(paramMap.get("codePv") != null){
//			sql.append("and pv.code_pv = ? ");
//			paramSql = Arrays.copyOf(paramSql, 1+i);
//			paramSql[i] = paramMap.get("codePv").toString();
//			i++;
//		}
//		if(paramMap.get("namePi") != null){
//			sql.append("and pv.name_pi like ? ");
//			paramSql = Arrays.copyOf(paramSql, 1+i);
//			paramSql[i] = "%"+paramMap.get("namePi").toString()+"%";
//		}
//		sql.append("group by pv.pk_pv ");
//		Map<String, Map<String, Object>> sumAmtMap = DataBaseHelper.queryListToMap(sql.toString(), paramSql);
//		
//		for(Map<String,Object> mapEn : mapEnList){
//			if(sumAmtMap.get(mapEn.get("pkPv")) != null){
//				mapEn.putAll(sumAmtMap.get(mapEn.get("pkPv")));
//			}
//		}
		
		return mapEnList;		
	}	
	
	
	/**
	 * 
	 * 交易号：022003027093<br>
	 * 查询未结账详细信息<br>
	 * <pre>
	 * 1、获取起始日期：如果是第一次结账，从bl_settle表取当前用户最小date_st，否则，取上次结账截止时间+1秒；截止日期：参数截止日期；
	 * 2、获取结算金额，医保金额；
	 * 3、查询表bl_deposit获取结算收款和退款：
	 * 4、查询表bl_deposit_pi获取账户收款和退款；
	 * 5、查询表bl_settle、bl_settle_detail获取支付信息（收费结算）；
	 * 6、查询表pv_encounter获取挂号信息；
	 * 7、查询表bl_invoice获取发票以及作废发票信息
	 * </pre>
	 * 
	 * @param  param
	 * @param  user
	 * @return OpBlCcVo
	 * @throws 
	 *
	 * @author wangpeng
	 * @date 2016年10月25日
	 */
	public ZsbaOpBlCcVo getInfoForIpBlCc(String param, IUser user){
		User u = UserContext.getUser();
		BlCc blCc = JsonUtil.readValue(param, BlCc.class);
		Date dateEnd = blCc.getDateEnd();

		Map<String, Object> mapParam = new HashMap<String, Object>();
		mapParam.put("pkOrg", u.getPkOrg());
		mapParam.put("pkEmp", u.getPkEmp());
		mapParam.put("dateEndStr", DateUtils.dateToStr("yyyy-MM-dd HH:mm:ss", dateEnd));
		mapParam.put("euCctype", blCc.getEuCctype());// 结账类型: 0-门诊日结;1-门诊挂号日结;2-门诊收费日结

		ZsbaOpBlCcVo vo = new ZsbaOpBlCcVo();
		// 获取起始日期
		Date dateBegin;
		BlCc blCcLast = zsbaOpblccMapper.getLastBlCcByUserIdForSql(u.getPkEmp());
		if (blCcLast != null) {
			Date begin = blCcLast.getDateEnd();
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(begin);
			calendar.add(Calendar.SECOND, 1);
			dateBegin = calendar.getTime();
			if (dateEnd.getTime() < dateBegin.getTime()) {
				throw new BusException("截止时间不能早于上次结账截止时间！");
			}
		} else {
			BlSettle blSettle = zsbaOpblccMapper.getLastBlSettleByUserIdForSql(u.getPkEmp());
			if (blSettle != null) {
				dateBegin = blSettle.getDateSt();
				if (dateEnd.getTime() < dateBegin.getTime()) {
					throw new BusException("截止时间不能早于最早的结算时间！");
				}
			} else {
				throw new BusException("该操作员不存在结算记录！");
			}
		}
		blCc.setDateBegin(dateBegin);

		// 结算信息
		
		 /* String dtStypesql=""; if("1".equals(mapParam.get("euCctype")))
		 * dtStypesql="and (st.dt_sttype = '00' or st.dt_sttype = '20')"; else
		 * if("2".equals(mapParam.get("euCctype"))) dtStypesql=
		 * "and (st.dt_sttype = '01' or st.dt_sttype = '21')";*/
		 

		ZsbaBlCcDsMz zsbaBlCcDsMz = new ZsbaBlCcDsMz();
		/*List<Map<String, Object>> list = zsbaOpblccMapper.qryDepostitUnRj(mapParam);
		Double yhk =0.0;
		for (Map<String, Object> item : list) {
			String mode = item.get("DT_PAYMODE").toString();
			Double amount = new Double(item.get("AMOUNT").toString());
			switch	(mode)
			{
			case "1":	zsbaBlCcDsMz.setAmtXj(amount); break; // 现金 
			case "3":
			case "7":
			case "8":
			case "12":
			case "13":
			case "14":yhk+=amount; break;//银行卡
			case "92": zsbaBlCcDsMz.setAmtCwjs(amount);  break; // 财务结算 
			}			
		}
		zsbaBlCcDsMz.setAmtYhk(yhk);// 银行卡
		//个账
		Double gz = zsbaOpblccMapper.getPsnCashPay(mapParam);
		zsbaBlCcDsMz.setAmtGz(gz);	
		*/
		Double yyyh= zsbaOpblccMapper.qryYyyhPayerUnRj(mapParam);	
		zsbaBlCcDsMz.setAmtZ95(yyyh);	//医院优惠		

		Double amountStDw =0.0;
		/* List<Map<String, Object>> hpPayers = zsbaOpblccMapper.qryHpPayUnRj(mapParam);
			
		for (Map<String, Object> item : hpPayers) {
			String code = item.get("code").toString();
			Double amount = new Double(item.get("AMOUNT_ST").toString());
			amountStDw += amount;
		//'感染记账26','新冠疫情记账25' -->
			switch (code) {		
			case "26":
				zsbaBlCcDsMz.setAmtGr(amount);
				break;
			case "25":
				zsbaBlCcDsMz.setAmtXgyq(amount);
				break;		
			default:
				break;
			}

		}*/
		
		 List<Map<String, Object>> kyPayers = zsbaOpblccMapper.qryKyPayUnRj(mapParam);
		for (Map<String, Object> item : kyPayers) {
			String name = item.get("CODE").toString();
			Double amount = new Double(item.get("AMOUNT").toString());
			amountStDw += amount;
			switch (name) {			
			case "11"://产筛记账
				zsbaBlCcDsMz.setAmtCsjz(amount);
				break;
			case "03"://政府计划生育
				zsbaBlCcDsMz.setAmtYyjs(amount);
				break;		
			case "13"://东区产检记账
				zsbaBlCcDsMz.setAmtDqcj(amount);
				break;
			case "12"://五桂山产检记账
				zsbaBlCcDsMz.setAmtWgscj(amount);
				break;
			case "06"://GCP记账
				zsbaBlCcDsMz.setAmtGcp(amount);
				break;
			case "05"://科研记账
				zsbaBlCcDsMz.setAmtKyjz(amount);
				break;
			case "10"://政府记账
				zsbaBlCcDsMz.setAmtGr(amount);
				break;
			case "14"://重点保健记账
				zsbaBlCcDsMz.setAmtZ80(amount);
				break;
			case "15"://地贫产前记账
				zsbaBlCcDsMz.setAmtDpcqzd(amount);
				break;
			default:
				break;
			}

		}
		//医疗救助
		Double yljz = zsbaOpblccMapper.getMafPay(mapParam);
		zsbaBlCcDsMz.setAmtYljz(yljz);
		amountStDw+=yljz;
		// 医保统筹
		Double hp = zsbaOpblccMapper.qryHpUnRj(mapParam);
		Double hpgs = zsbaOpblccMapper.qryHpGsUnRj(mapParam);
		zsbaBlCcDsMz.setAmtTc(hp + hpgs - yljz );
	

		Map<String, Object>	invCc =zsbaOpblccMapper.qryInvUnRj(mapParam);//纸质票据
		zsbaBlCcDsMz.setInvInfo((String)invCc.get("MIN")+"-"+(String)invCc.get("MAX"));		
		zsbaBlCcDsMz.setInvCnt( new Integer(invCc.get("SUM").toString()));
		
		Map<String, Object>	invCcCancel =zsbaOpblccMapper.qryInvCancelUnRj(mapParam);//纸质票据退废
		zsbaBlCcDsMz.setInvScrap(new Integer(invCcCancel.get("CANCEL").toString()));	
		zsbaBlCcDsMz.setInvBack(new Integer(invCcCancel.get("BACK").toString()));		
		
		
		Map<String, Object>	invEbill =zsbaOpblccMapper.qryInvEbillUnRj(mapParam);//电子票据
		zsbaBlCcDsMz.setInvBillInfo(invEbill.get("MIN")+"-"+ invEbill.get("MAX"));	
		zsbaBlCcDsMz.setInvBillCnt(new Integer(invEbill.get("SUM").toString()));
		
		Map<String, Object>	invEbillBack =zsbaOpblccMapper.qryInvEbillBackUnRj(mapParam);//电子票据作废
		zsbaBlCcDsMz.setInvBillBack(new Integer(invEbillBack.get("BACK").toString()));
		vo.setZsbaBlCcDsZs(zsbaBlCcDsMz);
		
		List<ZsbaOpSettleBackInvDtl> backDtls = zsbaOpblccMapper.qryInvoBackUnRj(mapParam);//退票明细 
				
		vo.setSettleBackInvDtls(backDtls);
		
		blCc.setPkEmpOpera(u.getPkEmp());
		blCc.setNameEmpOpera(u.getNameEmp());
		//vo.setBlCc(blCc);	
	
		Double amountTs = zsbaOpblccMapper.getAmountTs(mapParam);
		vo.setAmountSt(amountTs);//结算总金额
		//blCc.setAmtInsu(new Double(mapParam.get("amtInsu").toString())); //医保金额		
		blCc.setAmtSettle(amountTs);
		
		vo.setBlCc(blCc);
		//结算收款和退款
		List<ZsBaBlCcPay> blCcPayList = zsbaOpblccMapper.getBlDepositNoCcList(mapParam);
		vo.setBlCcPayList(blCcPayList);
	
		//账户收款和退款
		List<ZsBaBlCcPay> blCcPayPiList = zsbaOpblccMapper.getBlDepositPiNoCcList(mapParam);
		vo.setBlCcPayPiList(blCcPayPiList);
		
		
		Double yhk =0.0; Double gz =0.0; Double xj =0.0; Double cwjz =0.0; Double cqjz=0.0;
		for (ZsBaBlCcPay item : vo.getBlCcPayList()) {			
			Double amount = item.getAmt()+item.getAmtBack();

			switch	(item.getDtPaymode() )
			{
			case "1":  xj+=amount;   	break; // 现金 
			case "3":
			case "7": 
			case "8": if(!item.getDtBank().contains("公众号/自助机")){yhk+=amount; }break;//银行卡
			case "12":
			case "13":
			case "14":  if(!item.getDtBank().contains("公众号/自助机")){gz+=amount; }break; //个账
			case "92": cwjz+=amount;  break; // 财务结算 
			case "93":cqjz+=amount; break;
			}			
		}

		 
		zsbaBlCcDsMz.setAmtYhk(yhk);// 银行卡
		zsbaBlCcDsMz.setAmtXj(xj); //现金
		zsbaBlCcDsMz.setAmtGz(gz); //个账
		zsbaBlCcDsMz.setAmtCwjz(cwjz); //财务记账		
		zsbaBlCcDsMz.setAmtQtdw(amountStDw-cqjz);// 其他单位（项目）负担部分
		zsbaBlCcDsMz.setAmtGr(zsbaBlCcDsMz.getAmtGr() -cqjz);
		
		Double amountblb = zsbaOpblccMapper.getAmountBl(mapParam);
		zsbaBlCcDsMz.setAmtZ85(amountblb);
	
		
		//支付信息
		List<BlSettleDetail> blSettleDetailList = zsbaOpblccMapper.getBlSettleDetailNoCcList(mapParam);
		vo.setBlSettleDetailList(blSettleDetailList);
		
		//挂号信息
		List<InsuCountVo> insuCountVoList = zsbaOpblccMapper.getInsuCountVoList(mapParam);
		if(CollectionUtils.isNotEmpty(insuCountVoList)){
			int cnt = 0;
			int cancekCnt = 0;
			for(InsuCountVo ic : insuCountVoList){
				if(ic.getCnt() != null){
					cnt += ic.getCnt();
				}
				if(ic.getCntCancel() != null){
					cancekCnt += ic.getCntCancel();
				}
			}
			vo.setCnt(cnt);
			vo.setCancekCnt(cancekCnt);
			vo.setInsuCountVoList(insuCountVoList);
		}
		
		//发票信息
		List<BlInvoice> blInvoiceList = zsbaOpblccMapper.getBlInvoiceGroup(mapParam);
		if(blInvoiceList != null){
			String invInfo = "";
			int cntInv = 0;
			for(BlInvoice bi : blInvoiceList){
				if(bi.getCnt() != null){
					cntInv += bi.getCnt();
				}
				if(!CommonUtils.isEmptyString(bi.getMinCodeInv()) && !CommonUtils.isEmptyString(bi.getMaxCodeInv())){
					invInfo += bi.getMinCodeInv() + "～" + bi.getMaxCodeInv() + " ";
				}
			}
			blCc.setInvInfo(invInfo);
			blCc.setCntInv(cntInv);
			vo.setBlInvoiceList(blInvoiceList);
		}		
		
		//发票作废信息
		List<BlInvoice> blInvoiceCancelList = zsbaOpblccMapper.getBlInvoiceCancelList(mapParam);
		if(CollectionUtils.isNotEmpty(blInvoiceCancelList)){
			String invInfoCanc = "";
			int cntInvCanc = blInvoiceCancelList.size();
			int i = 0;
			for(BlInvoice bi : blInvoiceCancelList){
				if(!CommonUtils.isEmptyString(bi.getCodeInv())){
					if(i == 0){
						invInfoCanc += bi.getCodeInv();
					}else{
						invInfoCanc += ", " + bi.getCodeInv();
					}
				}
				i++;
			}
			blCc.setCntInvCanc(cntInvCanc);
			blCc.setInvInfoCanc(invInfoCanc);
			vo.setBlInvoiceCancelList(blInvoiceCancelList);
		}


		/**日结账个性化信息查询，每个项目如果有特定的参数要查询，要在这个分支下写*/
  		/*Map<String,Object> rtn = ZsbaCgProcessUtils.processOpOperatorAccount(vo);
  		if(rtn!=null&&"true".equals(rtn.get("enable"))&&rtn.get("result")!=null){
			return (OpBlCcVo)rtn.get("result");
		}*/
		
		return vo;
	}
	
	
	
	/**
	 * 
	 * 交易号：022003027094<br>
	 * 保存结账信息<br>
	 * <pre>
	 * 1、获取未结账信息；
	 * 2、保存结账表bl_cc:结账类型eu_cctype=0,操作员pk_emp_opera=:pkEmp,操作员姓名name_emp_opera=nameEmp,操作员结账日期时间date_cc=system,财务交割标志flag_clare=0,组长日结标志flag_leader=0,结账状态eu_status=0;
	 * 3、保存结账付款表bl_cc_pay（结算收付款和患者账户收付款）：操作员结账主键pk_cc=blcc.pk_cc,收付款类型eu_paytype:0结算收付款，2账户收付款;
	 * 4、保存收费结算-操作员结账_票据明细信息表bl_cc_inv：操作员结账主键pk_cc=blcc.pk_cc;作废标志flag_canc，作废发票时为1，否则为0;核销标志flag_wg=0;
	 * 5、更新bl_settle、bl_deposit、bl_deposit_pi、bl_invoice设置结账标志为1,操作员结账主键为blcc.pk_cc;
	 * </pre>
	 * 
	 * @param  param
	 * @param  user
	 * @return OpBlCcVo
	 * @throws 
	 *
	 * @author wangpeng
	 * @date 2016年10月25日
	 */
	public ZsbaOpBlCcVo saveBlCc(String param, IUser user){
		User u = UserContext.getUser();
		BlCc blCc = JsonUtil.readValue(param, BlCc.class);
		Date dateEnd = blCc.getDateEnd();

		Map<String, Object> mapParam = new HashMap<String, Object>();
		mapParam.put("pkOrg", u.getPkOrg());
		mapParam.put("pkEmp", u.getPkEmp());
		mapParam.put("dateEndStr", DateUtils.dateToStr("yyyy-MM-dd HH:mm:ss", dateEnd));
		mapParam.put("euCctype", blCc.getEuCctype());// 结账类型: 0-门诊日结;1-门诊挂号日结;2-门诊收费日结

		
		//获取未结账信息
		ZsbaOpBlCcVo vo = getInfoForIpBlCc(param, user);
		
		
		// 结算收款和退款
		List<ZsBaBlCcPay> blCcPayList = vo.getBlCcPayList();

		// 账户收款和退款
		List<ZsBaBlCcPay> blCcPayPiList =vo.getBlCcPayPiList(); 

		// 支付信息
		List<BlSettleDetail> blSettleDetailList = zsbaOpblccMapper.getBlSettleDetailNoCcList(mapParam);
		vo.setBlSettleDetailList(blSettleDetailList);

		// 挂号信息
		List<InsuCountVo> insuCountVoList = zsbaOpblccMapper.getInsuCountVoList(mapParam);
		if (CollectionUtils.isNotEmpty(insuCountVoList)) {
			int cnt = 0;
			int cancekCnt = 0;
			for (InsuCountVo ic : insuCountVoList) {
				if (ic.getCnt() != null) {
					cnt += ic.getCnt();
				}
				if (ic.getCntCancel() != null) {
					cancekCnt += ic.getCntCancel();
				}
			}
			vo.setCnt(cnt);
			vo.setCancekCnt(cancekCnt);
			vo.setInsuCountVoList(insuCountVoList);
		}
		// 保存结账表bl_cc
		String pkCc = NHISUUID.getKeyId(); // 患者账户收退款先使用
		blCc = vo.getBlCc();
		if (blCc == null) {
			throw new BusException("该操作员不存在待结算记录！");
		}
		
		// 发票信息
		List<BlInvoice> blInvoiceList =vo.getBlInvoiceList();
		
		// 发票作废信息
		List<BlInvoice> blInvoiceCancelList = vo.getBlInvoiceCancelList();
	

		if (CommonUtils.isEmptyString(blCc.getEuCctype())) {
			blCc.setEuCctype(BlConstant.CC_EU_CCTYPE_0); // 结账类型:0 门诊结账
		}
		blCc.setPkCc(pkCc);

		blCc.setPkEmpOpera(u.getPkEmp());
		blCc.setNameEmpOpera(u.getNameEmp());
		//患者账户收退款
		BigDecimal amtPi = new BigDecimal("0");
		BigDecimal amtPiBack = new BigDecimal("0");
			
		List<ZsBaBlCcPay> payForInsert = new ArrayList<ZsBaBlCcPay>();
		if(CollectionUtils.isNotEmpty(blCcPayPiList)){
			for(ZsBaBlCcPay pay : blCcPayList){
				if(pay.getDtPaymode() != null){ //排除收付款方式为空的错误数据
					if(pay.getAmt() != null){
						amtPi = amtPi.add(new BigDecimal(pay.getAmt()));
					}					
					if(pay.getAmtBack() != null){
						amtPiBack = amtPiBack.add(new BigDecimal(pay.getAmtBack()));
					}					

					//操作员结账-付款方式
					pay.setPkCcpay(NHISUUID.getKeyId());
					pay.setPkOrg(u.getPkOrg());
					pay.setPkCc(pkCc);
					pay.setEuPaytype(BlConstant.CCPAY_EU_PAYTYPE_2); //2账户收付款
					pay.setCreator(u.getPkEmp());
					pay.setCreateTime(new Date());
					pay.setModifier(u.getPkEmp());
					pay.setDelFlag("0");
					pay.setTs(new Date());
					payForInsert.add(pay);
				}
			}
		}
		blCc.setAmtPi(amtPi.setScale(2,BigDecimal.ROUND_HALF_UP).doubleValue());
		blCc.setAmtPiBack(amtPiBack.setScale(2,BigDecimal.ROUND_HALF_UP).doubleValue());
		
			//查詢捨入金額
		if(vo!=null && vo.getBlSettleDetailList()!=null && vo.getBlSettleDetailList().size()>0){
			for(BlSettleDetail stDet : vo.getBlSettleDetailList()){
				if(!CommonUtils.isEmptyString(stDet.getPkInsurance()) 
						&& stDet.getPkInsurance().equals("2")){
					blCc.setAmtRound(stDet.getAmount());
				}
					
			}
		}
		
		
		blCc.setDateCc(new Date());
		blCc.setFlagClear("0");
		blCc.setFlagLeader("0");
		blCc.setEuStatus("0");	
		
		DataBaseHelper.insertBean(blCc);
		
		ZsbaBlCcDsMz blCcDszs = vo.getZsbaBlCcDsZs();
		String pKCcDs = NHISUUID.getKeyId();
		blCcDszs.setpKCcDs(pKCcDs);
		blCcDszs.setPkCc(blCc.getPkCc());
		blCcDszs.setDelFlag("0");;
		blCcDszs.setCreator(u.getPkEmp());
		blCcDszs.setCreateTime(new Date());
		blCcDszs.setTs(new Date());
		blCcDszs.setPkOrg(u.getPkOrg());
		
		DataBaseHelper.insertBean(blCcDszs);
		
		//保存结账付款表bl_cc_pay（结算收付款和患者账户收付款）
		//结算
		
		if(CollectionUtils.isNotEmpty(blCcPayList)){
			for(ZsBaBlCcPay pay : blCcPayList){
				if(pay.getDtPaymode() != null){ //排除收付款方式为空的错误数据
					//操作员结账-付款方式
					pay.setPkCcpay(NHISUUID.getKeyId());
					pay.setPkOrg(u.getPkOrg());
					pay.setPkCc(pkCc);
					pay.setEuPaytype(BlConstant.CCPAY_EU_PAYTYPE_0); //0结算收付款
					pay.setCreator(u.getPkEmp());
					pay.setCreateTime(new Date());
					pay.setModifier(u.getPkEmp());
					pay.setDelFlag("0");
					pay.setTs(new Date());
					payForInsert.add(pay);
				}
			}		
		}
		//统一保存，包含结算以及患者账户
		if(payForInsert.size() > 0){
			DataBaseHelper.batchUpdate(DataBaseHelper.getInsertSql(ZsBaBlCcPay.class), payForInsert);
		}		
		
		
		//保存结算票据表bl_cc_inv（使用发票和作废发票）

		List<BlCcInv> blCcInvlist = new ArrayList<BlCcInv>();
		if(CollectionUtils.isNotEmpty(blInvoiceList)){
			if(CollectionUtils.isNotEmpty(blInvoiceCancelList)){
				blInvoiceList.addAll(blInvoiceCancelList);
			}			
			for(BlInvoice blInv : blInvoiceList){
				BlCcInv inv = new BlCcInv();
				inv.setPkCcinv(NHISUUID.getKeyId());
				inv.setPkOrg(u.getPkOrg());
				inv.setPkCc(pkCc);
				inv.setPkInvcate(blInv.getPkInvcate());
				if("1".equals(blInv.getFlagCancel())){ //作废
					inv.setBeginNo(blInv.getCodeInv());
					inv.setEndNo(blInv.getCodeInv());
					inv.setFlagCanc("1");
				}else{
					inv.setBeginNo(blInv.getMinCodeInv());
					inv.setEndNo(blInv.getMaxCodeInv());
					inv.setFlagCanc("0");
				}				
				inv.setFlagWg("0");
				inv.setCreator(u.getPkEmp());
				inv.setCreateTime(new Date());
				inv.setModifier(u.getPkEmp());
				inv.setDelFlag("0");
				inv.setTs(new Date());				
				blCcInvlist.add(inv);
			}
			DataBaseHelper.batchUpdate(DataBaseHelper.getInsertSql(BlCcInv.class), blCcInvlist);
		}
		


		//更新业务表
		Map<String, Object> mapParams = new HashMap<String, Object>();
		mapParams.put("pkOrg", u.getPkOrg());
		mapParams.put("modifier", u.getPkEmp());
		mapParams.put("ts", new Date());
		mapParams.put("pkCc", pkCc);
		mapParams.put("dateEndStr", DateUtils.dateToStr("yyyy-MM-dd HH:mm:ss", blCc.getDateEnd()));
		mapParams.put("pkEmp", u.getPkEmp());
		mapParams.put("euCctype", blCc.getEuCctype());

		Set<String> pkList = zsbaOpblccMapper.qryAllPkSt(mapParams);
		if (pkList != null && pkList.size() > 0)
		{
			//更新收费结算-结算记录表bl_settle
			//更新bl_settle表
			String sqlSt = "update bl_settle set flag_cc = 1,pk_cc =  ? where  flag_cc = 0  and pk_settle in ("+CommonUtils.convertSetToSqlInPart(pkList, "pk_settle")+")";
			DataBaseHelper.execute(sqlSt.toString(), new Object[]{blCc.getPkCc()});
			//更新bl_deposit表
			String sqlDepo = " update bl_deposit set flag_cc = 1, pk_cc = ? where (flag_cc = 0 or flag_cc is null) and pk_settle in ("+CommonUtils.convertSetToSqlInPart(pkList, "pk_settle")+")";
			DataBaseHelper.execute(sqlDepo.toString(), new Object[]{blCc.getPkCc()});
		}

		//更新收费结算-交款记录表bl_deposit_pi
		DataBaseHelper.update("update bl_deposit_pi set flag_cc = '1', pk_cc =:pkCc, modifier =:modifier, ts =:ts where pk_org =:pkOrg and pk_emp_pay =:modifier and (flag_cc = '0' or flag_cc is null) and to_char(date_pay,'yyyy-MM-DD HH24:MI:SS') <=:dateEndStr ", mapParams);
		
		DataBaseHelper.update("update BL_DEPOSIT set FLAG_CC='1',PK_CC=:pkCc	where DATE_PAY <=:dateEndStr and PK_EMP_PAY=:modifier  and DEL_FLAG='0' and FLAG_CC='0' and NOTE in ('病历本','托儿本')", mapParams);
		
		
		
		// 更新收费结算-发票记录表bl_invoice
		Set<String> pkInvs = zsbaOpblccMapper.qryAllPkInv(mapParams);
		Set<String> pkInvsCancel = zsbaOpblccMapper.qryAllPkInvCancel(mapParams);

		DataBaseHelper.update(
				"update bl_invoice set flag_cc = '1', pk_cc =:pkCc, modifier =:modifier, ts =:ts "
						+ "where del_flag = '0' "
						 + " and (flag_cc = 0 or pk_cc is null) "
						+ "and pk_emp_inv = :pkEmp"
						+ " and to_char(date_inv,'yyyy-MM-DD HH24:MI:SS') <=:dateEndStr "
						+ " and pk_invoice in (" + CommonUtils.convertSetToSqlInPart(pkInvs, "pk_invoice") + ") ",
				mapParams);
		// 更新作废发票记录表bl_invoice
		StringBuffer sqlCancUpd = new StringBuffer(
				"update bl_invoice  set FLAG_CC_CANCEL = '1',PK_CC_CANCEL=:pkCc ");
		sqlCancUpd.append("  where  pk_org=:pkOrg " 
				+ "and PK_EMP_CANCEL =:pkEmp and to_char(date_cancel,'yyyy-MM-DD HH24:MI:SS') <=:dateEndStr ");

		if ("2".equals(mapParam.get("euCctype"))) {
			sqlCancUpd.append(" and ( pk_invoice in ("
					+ CommonUtils.convertSetToSqlInPart(pkInvsCancel, "pk_invoice") + ") or note='作废空发票' )");
		} else {
			sqlCancUpd.append(" and pk_invoice in (" + CommonUtils.convertSetToSqlInPart(pkInvsCancel, "pk_invoice") + ")");

		}
		DataBaseHelper.update(sqlCancUpd.toString(), mapParams);

		//更新电子票据信息
		DataBaseHelper.update(
				"update bl_invoice  set FLAG_CC_EBILL = '1', PK_CC_EBILL =:pkCc, modifier =:modifier, ts =:ts "
						+ "where del_flag = '0'  and (FLAG_CC_EBILL = 0 or PK_CC_EBILL is null) "
						+ "and PK_EMP_EBILL = :pkEmp and to_char(DATE_EBILL,'yyyy-MM-DD HH24:MI:SS') <=:dateEndStr "
						+ "and pk_invoice in (" + CommonUtils.convertSetToSqlInPart(pkInvs, "pk_invoice") + ") ",
				mapParams);

		StringBuffer sqlCancUpdEbill = new StringBuffer(
				"update bl_invoice set FLAG_CC_CANCEL_EBILL = '1',PK_CC_CANCEL_EBILL=:pkCc ");
		sqlCancUpdEbill.append("where (FLAG_CC_CANCEL_EBILL='0' or FLAG_CC_CANCEL_EBILL is null) and pk_org=:pkOrg "
				+ "and PK_EMP_CANCEL_EBILL =:pkEmp and to_char(DATE_EBILL_CANCEL,'yyyy-MM-DD HH24:MI:SS') <=:dateEndStr ");

		if ("2".equals(mapParam.get("euCctype"))) {
			sqlCancUpdEbill.append(" and ( pk_invoice in ("
					+ CommonUtils.convertSetToSqlInPart(pkInvsCancel, "pk_invoice") + ") or note='作废空发票' )");
		} else {
			sqlCancUpdEbill.append(" and pk_invoice in (" + CommonUtils.convertSetToSqlInPart(pkInvsCancel, "pk_invoice") + ")");

		}
		DataBaseHelper.update(sqlCancUpdEbill.toString(), mapParams);

		return vo;
	}
	
	/**
	 * 
	 * 交易号：022003027096<br>
	 * 查询未结账业务--挂号记录列表<br>
	 * 
	 * @param  param
	 * @param  user
	 * @return void
	 * @throws 
	 *
	 * @author wangpeng
	 * @date 2016年11月15日
	 */
	@SuppressWarnings("unchecked")
	public List<Map<String,Object>> getNoBlCcPvEncounterList(String param, IUser user){
		Map<String,Object> paramMap = JsonUtil.readValue(param, Map.class);
		User u = UserContext.getUser();
		paramMap.put("pkOrg", u.getPkOrg());
		paramMap.put("pkEmp", u.getPkEmp());
		//先查挂号基本信息，再处理挂号费问题
		List<Map<String,Object>> resList = zsbaOpblccMapper.getNoBlCcPvEncounterList(paramMap);
		
		return resList;		
	}
	
	/**
	 * 
	 * 交易号：022003027097<br>
	 * 取消结账<br>
	 * <pre>
	 * 1、验证是否可以取消结账：只有最后的一次日结账才可以取消；
	 * 2、删除结账相关表：结账付款表bl_cc_pay、结算票据表bl_cc_inv、结账表bl_cc；
	 * 3、更新业务表：结算bl_settle、结算收付款bl_deposit、账户收付款bl_deposit_pi、结算发票（含作废）bl_invoice，把对应的结账标志置为0，结账（作废）主键置为null
	 * </pre>
	 * 
	 * @param  param
	 * @param  user
	 * @return OpBlCcVo
	 * @throws 
	 *
	 * @author wangpeng
	 * @date 2016年10月25日
	 */
	public void deleteBlCc(String param, IUser user){
		User u = UserContext.getUser();
		BlCc blCc = JsonUtil.readValue(param, BlCc.class);
		String pkCc = blCc.getPkCc();
		if(blCc.getEuCctype()==null)
			blCc.setEuCctype("0");
		
		//验证，最后一次才可以取消
		int count = DataBaseHelper.queryForScalar("select nvl(count(1),0) from bl_cc cc where cc.eu_cctype = ? and cc.del_flag = '0' "
				+ "and to_char(cc.date_cc,'yyyy-MM-DD HH24:MI:SS') > (select to_char(blcc.date_cc,'yyyy-MM-DD HH24:MI:SS') from bl_cc blcc where blcc.pk_cc = ? and blcc.pk_emp_opera=cc.pk_emp_opera and blcc.eu_cctype = '0')", 
				Integer.class, blCc.getEuCctype(),pkCc);
		if(count > 0){
			throw new BusException("只能取消最大日期的结账!");
		}
		//只有结账状态的可以取消
		int count_jz = DataBaseHelper.queryForScalar("select nvl(count(1),0) from bl_cc cc where cc.eu_cctype = ? and cc.del_flag = '0' "
				+ "and cc.eu_status = '0' and cc.pk_cc = ? ", Integer.class, blCc.getEuCctype(),pkCc);
		if(count_jz != 1){
			throw new BusException("只能取消状态为结账的记录!");
		}
		
		//删除结账付款表bl_cc_pay、结算票据表bl_cc_inv、结账表bl_cc
		DataBaseHelper.execute("delete from bl_cc_pay where pk_cc = ?", pkCc);
		DataBaseHelper.execute("delete from bl_cc_inv where pk_cc = ?", pkCc);
		DataBaseHelper.execute("delete from bl_cc where pk_cc = ?", pkCc);
		DataBaseHelper.execute("delete from BL_CC_DS_MZ where pk_cc = ?", pkCc);
		
		//更新业务表
		Map<String, Object> mapParam = new HashMap<String, Object>();
		mapParam.put("pkOrg", u.getPkOrg());
		mapParam.put("modifier", u.getPkEmp());
		mapParam.put("ts", new Date());
		mapParam.put("pkCc", pkCc);
		//结算bl_settle
		DataBaseHelper.update("update bl_settle set flag_cc = '0', pk_cc = null, modifier=:modifier, ts=:ts where pk_cc =:pkCc", mapParam);
		//结算收付款bl_deposit
		DataBaseHelper.update("update bl_deposit set flag_cc = '0', pk_cc = null, modifier=:modifier, ts=:ts where pk_cc =:pkCc", mapParam);
		//账户收付款bl_deposit_pi
		DataBaseHelper.update("update bl_deposit_pi set flag_cc = '0', pk_cc = null, modifier=:modifier, ts=:ts where pk_cc =:pkCc", mapParam);
		//结算发票bl_invoice
		DataBaseHelper.update("update bl_invoice set flag_cc = '0', pk_cc = null, modifier=:modifier, ts=:ts where pk_cc =:pkCc", mapParam);
		//结算作废发票bl_invoice
		DataBaseHelper.update("update bl_invoice set flag_cc_cancel = '0', pk_cc_cancel = null, modifier=:modifier, ts=:ts where pk_cc_cancel =:pkCc", mapParam);
		//结算电子发票bl_invoice
		DataBaseHelper.update("update bl_invoice set FLAG_CC_EBILL = '0', PK_CC_EBILL = null, modifier=:modifier, ts=:ts where PK_CC_EBILL =:pkCc ", mapParam);
		//结算电子作废发票bl_invoice
		DataBaseHelper.update("update bl_invoice set FLAG_CC_CANCEL_EBILL = '0', PK_CC_CANCEL_EBILL = null, modifier=:modifier, ts=:ts where PK_CC_CANCEL_EBILL =:pkCc", mapParam);
	}
	
	/**
	 * 
	 * 交易号：007002002012<br>
	 * 查询未汇总结账信息<br>
	 * <pre>
	 * 1、查询时按照 bl_cc.date_end <= 参数截止时间查询
	 * </pre>
	 *  
	 * @param  param
	 * @param  user
	 * @return OpBlCcLeaderVo
	 * @throws 
	 *
	 * @author wangpeng
	 * @date 2016年10月27日
	 */
	public OpBlCcLeaderVo getNoOpBlCcLeaderVo(String param, IUser user){
		User u = UserContext.getUser();
		BlCc blCc = JsonUtil.readValue(param, BlCc.class);
		//OpBlCcLeaderVo vo = new OpBlCcLeaderVo();
		
		Map<String, Object> mapParam = new HashMap<String, Object>();
		mapParam.put("pkOrg", u.getPkOrg());
		mapParam.put("dateEndStr", DateUtils.dateToStr("yyyy-MM-dd HH:mm:ss", blCc.getDateEnd()));
		
		//获取开始时间、截止时间、汇总人
		OpBlCcLeaderVo vo = DataBaseHelper.queryForBean("select min(date_begin) date_begin, max(date_end) date_end from bl_cc "
				+ "where del_flag = '0' and eu_cctype = '0' and eu_status = '0' and (flag_leader = '0' or flag_leader is null) "
				+ "and pk_org = ? "
				+ "and to_char(date_end,'yyyy-MM-DD HH24:MI:SS') <= ? ", OpBlCcLeaderVo.class, new Object[]{u.getPkOrg(), DateUtils.dateToStr("yyyy-MM-dd HH:mm:ss", blCc.getDateEnd())});
		if(vo.getDateBegin() == null){
			throw new BusException("不存在未汇总结账信息！");
		}
		//获取上此结账信息
		OpBlCcLeaderVo dateBeginVo = DataBaseHelper.queryForBean("select max(date_leader) date_begin from bl_cc "
				+ "where del_flag = '0' and eu_cctype = '0' and eu_status = '1' and flag_leader = '1' "
				+ "and pk_org = ? "
				+ "and to_char(date_end,'yyyy-MM-DD HH24:MI:SS') <= ? ", OpBlCcLeaderVo.class, new Object[]{u.getPkOrg(), DateUtils.dateToStr("yyyy-MM-dd HH:mm:ss", blCc.getDateEnd())});
	    if(dateBeginVo.getDateBegin() != null){
	    	long time = dateBeginVo.getDateBegin().getTime();
	    	dateBeginVo.getDateBegin().setTime(time + 1000); 
	    	vo.setDateBegin(dateBeginVo.getDateBegin());
	    }
		
		vo.setDateEnd(blCc.getDateEnd());
		vo.setNameEmpLeader(u.getNameEmp());
		
		//按操作员分组
		List<OpBlCcPayAndPiVo> operList = zsbaOpblccMapper.getNoBlCcPayGroupByOperaList(mapParam);
		vo.setOperList(operList);
		
		//按付款方式分组
		List<OpBlCcPayAndPiVo> paymodeList = zsbaOpblccMapper.getNoBlCcPayGroupByPaymodeList(mapParam);
		vo.setPaymodeList(paymodeList);		
		
		return vo;
	}
	
	/**
	 * 
	 * 交易号：007002002013<br>
	 * 保存门诊汇总结账<br>
	 * 
	 * @param  param
	 * @param  user
	 * @return OpBlCcLeaderVo
	 * @throws 
	 *
	 * @author wangpeng
	 * @date 2016年10月27日
	 */
	public Map<String, Object> saveBlCcLeader(String param, IUser user){
		User u = UserContext.getUser();
		BlCc blCc = JsonUtil.readValue(param, BlCc.class);
		Date ts = new Date();
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("dateLeader", blCc.getDateEnd());
		
		Map<String, Object> mapParam = new HashMap<String, Object>();
		mapParam.put("pkOrg", u.getPkOrg());
		mapParam.put("modifier", u.getPkEmp());
		mapParam.put("modifierName", u.getNameEmp());
		mapParam.put("dateLeader", blCc.getDateEnd());
		mapParam.put("ts", ts);
		mapParam.put("dateEndStr", DateUtils.dateToStr("yyyy-MM-dd HH:mm:ss", blCc.getDateEnd()));
		
		//更新操纵员结账表:bl_cc.date_leader=sysdate
		DataBaseHelper.update("update bl_cc set flag_leader = '1', pk_emp_leader =:modifier, name_emp_leader =:modifierName, date_leader =:dateLeader, eu_status = '1', modifier=:modifier, ts=:ts "
				+ "where pk_org =:pkOrg and eu_cctype = '0' and flag_leader = '0' and to_char(date_end,'yyyy-MM-DD HH24:MI:SS') <=:dateEndStr",mapParam);
		
		return map;
	}
	
	/**
	 * 
	 * 交易号：007002002015<br>
	 * 查询已汇总的门诊汇总结账详情<br>
	 * <pre>
	 * 1、查询时按照 bl_cc.date_leader = 参数门诊汇总结账时间查询
	 * </pre>
	 *  
	 * @param  param
	 * @param  user
	 * @return OpBlCcLeaderVo
	 * @throws 
	 *
	 * @author wangpeng
	 * @date 2016年10月27日
	 */
	public OpBlCcLeaderVo getOpBlCcLeaderVo(String param, IUser user){
		User u = UserContext.getUser();
		BlCc blCc = JsonUtil.readValue(param, BlCc.class);
		//OpBlCcLeaderVo vo = new OpBlCcLeaderVo();
		
		Map<String, Object> mapParam = new HashMap<String, Object>();
		mapParam.put("pkOrg", u.getPkOrg());
		mapParam.put("pkEmpLeader", u.getPkEmp());
		mapParam.put("dateLeaderStr", DateUtils.dateToStr("yyyy-MM-dd HH:mm:ss", blCc.getDateLeader()));
		
		//获取开始时间、截止时间、汇总人
		OpBlCcLeaderVo vo = DataBaseHelper.queryForBean("select min(date_begin) date_begin, max(date_end) date_end from bl_cc "
				+ "where del_flag = '0' and flag_leader = '1' and eu_cctype = '0' "
				+ "and pk_org = ? "
				+ "and pk_emp_leader = ? "
				+ "and to_char(date_leader,'yyyy-MM-DD HH24:MI:SS') = ? ", OpBlCcLeaderVo.class, new Object[]{u.getPkOrg(), u.getPkEmp(), DateUtils.dateToStr("yyyy-MM-dd HH:mm:ss", blCc.getDateLeader())});
		if(vo.getDateBegin() == null){
			throw new BusException("不存在汇总结账信息！");
		}
		//获取上此结账信息
		OpBlCcLeaderVo dateBeginVo = DataBaseHelper.queryForBean("select max(date_leader) date_begin from bl_cc "
				+ "where del_flag = '0' and flag_leader = '1' and eu_cctype = '0' "
				+ "and pk_org = ? "
				+ "and pk_emp_leader = ? "
				+ "and to_char(date_leader,'yyyy-MM-DD HH24:MI:SS') < ? ", OpBlCcLeaderVo.class, new Object[]{u.getPkOrg(), u.getPkEmp(), DateUtils.dateToStr("yyyy-MM-dd HH:mm:ss", blCc.getDateLeader())});
		if(dateBeginVo.getDateBegin() != null){
			long time = dateBeginVo.getDateBegin().getTime();
	    	dateBeginVo.getDateBegin().setTime(time + 1000); 
	    	vo.setDateBegin(dateBeginVo.getDateBegin());
	    }
		//制表人
		vo.setNameEmpLeader(u.getNameEmp());
		vo.setDateEnd(blCc.getDateLeader());
		//按操作员分组
		List<OpBlCcPayAndPiVo> operList = zsbaOpblccMapper.getBlCcPayGroupByOperaList(mapParam);
		vo.setOperList(operList);
		
		//按付款方式分组
		List<OpBlCcPayAndPiVo> paymodeList = zsbaOpblccMapper.getBlCcPayGroupByPaymodeList(mapParam);
		vo.setPaymodeList(paymodeList);		
		
		return vo;
	}
	
	/***
	 * 交易号：007002002016<br>
	 * 取消门诊汇总结账<br>
	 * 
	 * @param  param
	 * @param  user
	 * @return void
	 * @throws 
	 *
	 * @author wangpeng
	 * @date 2016年10月29日
	 */
	public void deleteBlCcLeader(String param, IUser user){
		User u = UserContext.getUser();
		BlCc blCc = JsonUtil.readValue(param, BlCc.class);
		
		//校验能否取消汇总结账
		int count = DataBaseHelper.queryForScalar("select count(1) from bl_cc cc "
				+ "where cc.del_flag = '0' and  cc.pk_org = ? and cc.eu_cctype = '0' and cc.flag_leader = '1' and cc.pk_emp_leader = ? and to_char(cc.date_leader,'yyyy-MM-DD HH24:MI:SS') > ?", Integer.class, new Object[]{u.getPkOrg(), u.getPkEmp(), DateUtils.dateToStr("yyyy-MM-dd HH:mm:ss", blCc.getDateLeader())});
		if(count > 0){
			throw new BusException("只能取消最大日期的汇总结账!");
		}
		
		Map<String, Object> mapParam = new HashMap<String, Object>();
		mapParam.put("pkOrg", u.getPkOrg());
		mapParam.put("pkEmpLeader", u.getPkEmp());
		mapParam.put("ts", new Date());
		mapParam.put("dateLeaderStr", DateUtils.dateToStr("yyyy-MM-dd HH:mm:ss", blCc.getDateLeader()));
		
		DataBaseHelper.update("update bl_cc "
				+ "set flag_leader = '0', pk_emp_leader = null, name_emp_leader = null, date_leader = null, eu_status = '0', modifier =:pkEmpLeader, ts =:ts "
				+ "where eu_cctype = '0' and flag_leader = '1' and pk_org =:pkOrg and pk_emp_leader =:pkEmpLeader and to_char(date_leader,'yyyy-MM-DD HH24:MI:SS') =:dateLeaderStr ", mapParam);
	
	}
	/**
	 * 非就诊业务结账处理接口
	 * @param param
	 * @param user
	 */
	public OpBlCcCradVo saveNonTreatment(String param, IUser user){
		 BlPi bPi = JsonUtil.readValue(param, BlPi.class);
		 User u = UserContext.getUser();		
		 OpBlCcCradVo oCradVo = getBlCcCrad(param, user);
		 String pkCc = NHISUUID.getKeyId(); //患者账户收退款先使用
		 //2.写结账表bl_cc
		 BlCc blCc = oCradVo.getBlCc();
		    blCc.setPkCc(pkCc);			
			blCc.setEuCctype("51");
			blCc.setDateEnd(bPi.getDateBl());
			blCc.setDateCc(new Date());
			blCc.setFlagClear(EnumerateParameter.ZERO);
			blCc.setFlagLeader(EnumerateParameter.ZERO);
			blCc.setEuStatus(EnumerateParameter.ZERO);
			DataBaseHelper.insertBean(blCc);
			List<BlCcPay> blCcPayPiList = oCradVo.getBlCcPay();		
			List<BlCcPay> payForInsert = new ArrayList<BlCcPay>();
			if(CollectionUtils.isNotEmpty(blCcPayPiList)){
				for(BlCcPay pay : blCcPayPiList){					
						//操作员结账-付款方式
						pay.setPkCcpay(NHISUUID.getKeyId());
						pay.setPkOrg(u.getPkOrg());
						pay.setPkCc(pkCc);
						pay.setEuPaytype("51"); //非就诊结账（结算）
						pay.setCreator(u.getPkEmp());
						pay.setCreateTime(new Date());
						pay.setModifier(u.getPkEmp());
						pay.setDelFlag("0");
						pay.setTs(new Date());
						payForInsert.add(pay);
					}
				}
			//统一保存，包含结算以及患者账户
			if(payForInsert.size() > 0){
				DataBaseHelper.batchUpdate(DataBaseHelper.getInsertSql(BlCcPay.class), payForInsert);
			}
			//更新业务表
			Map<String, Object> mapParam = new HashMap<String, Object>();
			mapParam.put("pkOrg", u.getPkOrg());
			mapParam.put("modifier", u.getPkEmp());
			mapParam.put("ts", new Date());
			mapParam.put("pkCc", pkCc);
			mapParam.put("dateEndStr", DateUtils.dateToStr("yyyy-MM-dd HH:mm:ss", blCc.getDateEnd()));									
			//更新收费结算-交款记录表bl_deposit_pi
			DataBaseHelper.update("update bl_deposit_pi set flag_cc = '1', pk_cc =:pkCc, modifier =:modifier, ts =:ts where pk_org =:pkOrg and pk_emp_pay =:modifier and (flag_cc = '0' or flag_cc is null) and to_char(date_pay,'yyyy-MM-DD HH24:MI:SS') <=:dateEndStr ", mapParam);
			DataBaseHelper.update("update bl_pi set flag_cc = '1', pk_cc =:pkCc where pk_org =:pkOrg and pk_emp_bl =:modifier and flag_cc = 0 and to_char(date_bl,'yyyy-MM-DD HH24:MI:SS') <=:dateEndStr ", mapParam);
			return oCradVo;		
	}
				
	//未结账业务记录
	public OpBlCcCradVo getBlCcCrad(String param, IUser user){
		User u = UserContext.getUser();
		OpBlCcCradVo oCradVo = new OpBlCcCradVo();
		BlPi bPi = JsonUtil.readValue(param, BlPi.class);
		String blPiSql = "select sum(bp.amount) amt, sum(bp.quan) cnt_trade,bp.dt_paymode, bp.eu_butype  from bl_pi bp "
			 		+ " where bp.pk_org = ? and  bp.pk_emp_bl = ? and bp.flag_cc = '0' and bp.date_bl <= ? group by bp.dt_paymode,  bp.eu_butype ";				 
		List<BlCcPay> blccpay = DataBaseHelper.queryForList(blPiSql,BlCcPay.class, u.getPkOrg(),u.getPkEmp(),bPi.getDateBl());
		BlCc blCc = new BlCc();
		Date dateEnd = bPi.getDateBl();
		 Date dateBegin = null;
			BlCc blCcLast = DataBaseHelper.queryForBean("select top 1 * from bl_cc "
					+ " where del_flag = '0' and eu_cctype = '51' "
					+ " and pk_emp_opera = ? order by date_end desc ", BlCc.class, u.getPkEmp());
			if(blCcLast != null){
				Date begin = blCcLast.getDateEnd();
				Calendar calendar = Calendar.getInstance();
				calendar.setTime(begin);
				calendar.add(Calendar.SECOND, 1);
				dateBegin = calendar.getTime();
				if(dateEnd.getTime() < dateBegin.getTime()){
					throw new BusException("截止时间不能早于上次结账截止时间！");
				}
			}else{
				BlPi blP = DataBaseHelper.queryForBean(" select top 1 *  from bl_pi where pk_emp_bl = ? and del_flag='0'  order by date_bl  ", BlPi.class, u.getPkEmp());
				if (blP !=null ) {
					dateBegin = blP.getDateBl();
					if (dateEnd.getTime() < dateBegin.getTime()) {
						throw new BusException("截止时间不能早于最早的结算时间！");
					}
				}else {
					throw new BusException("该操作员不存在收费记录！");
				}				
			}
			blCc.setDateBegin(dateBegin);
			blCc.setDateEnd(bPi.getDateBl());
			blCc.setPkEmpOpera(u.getPkEmp());
			blCc.setNameEmpOpera(u.getNameEmp());
			blCc.setPkOrg(u.getPkOrg());
			oCradVo.setBlCc(blCc);
			oCradVo.setBlCcPay(blccpay);
		return oCradVo;		
	}
	/**
	 * 非就诊业务取消结账
	 * @param param
	 * @param user
	 */
	public void deleteNonBlCc(String param, IUser user){
		User u = UserContext.getUser();
		BlCc blCc = JsonUtil.readValue(param, BlCc.class);
		String pkCc = blCc.getPkCc();		
		//验证，最后一次才可以取消
		int count = DataBaseHelper.queryForScalar("select nvl(count(1),0) from bl_cc cc where cc.eu_cctype = '51' and cc.del_flag = '0' "
				+ "and cc.date_cc > (select blcc.date_cc from bl_cc blcc where blcc.pk_cc = ? and blcc.pk_emp_opera=cc.pk_emp_opera and blcc.eu_cctype = '51')", Integer.class, pkCc);
		if(count > 0){
			throw new BusException("只能取消最大日期的结账!");
		}
		//只有结账状态的可以取消
		int count_jz = DataBaseHelper.queryForScalar("select nvl(count(1),0) from bl_cc cc where cc.eu_cctype = '51' and cc.del_flag = '0' "
				+ "and cc.eu_status = '0' and cc.pk_cc = ? ", Integer.class, pkCc);
		if(count_jz != 1){
			throw new BusException("只能取消状态为结账的记录!");
		}
		//删除结账付款表bl_cc_pay、结算票据表bl_cc_inv、结账表bl_cc
		DataBaseHelper.execute("delete from bl_cc_pay where pk_cc = ?", pkCc);
		DataBaseHelper.execute("delete from bl_cc where pk_cc = ?", pkCc);
		//更新业务表
				Map<String, Object> mapParam = new HashMap<String, Object>();
				mapParam.put("pkOrg", u.getPkOrg());
				mapParam.put("modifier", u.getPkEmp());
				mapParam.put("ts", new Date());
				mapParam.put("pkCc", pkCc);			
				//账户收付款bl_deposit_pi
				DataBaseHelper.update("update bl_deposit_pi set flag_cc = '0', pk_cc = null, modifier=:modifier, ts=:ts where pk_cc =:pkCc", mapParam);
	            //非就诊业务记录bl_pi
				DataBaseHelper.update("update bl_pi set flag_cc = '0', pk_cc = null, modifier=:modifier, ts=:ts where pk_cc =:pkCc", mapParam);    
	}
	/**
	 * 根据结账记录显示结账信息
	 * @param param
	 * @param user
	 * @return
	 */
	public OpBlCcCradVo getBlccList(String param, IUser user){
		BlCc blCc = JsonUtil.readValue(param, BlCc.class);
		OpBlCcCradVo oCradVo = new OpBlCcCradVo();
		String pkCc = blCc.getPkCc();
		blCc = DataBaseHelper.queryForBean("select * from bl_cc where pk_cc = ? and eu_cctype = '51' ", BlCc.class, pkCc);
		List<BlCcPay> blccpay = DataBaseHelper.queryForList("select * from BL_CC_PAY where pk_cc = ? and eu_paytype = '51'", BlCcPay.class, pkCc);
		oCradVo.setBlCc(blCc);
		oCradVo.setBlCcPay(blccpay);
	    return oCradVo;			
	}
	/**
	 * 查询已日结结算信息
	 * @param param
	 * @param user
	 * @return
	 */
	public List<Map<String,Object>> queryBlSettleInfoByCC(String param,IUser user){ 
		Map<String,Object> paramMap = JsonUtil.readValue(param, Map.class);
		return zsbaOpblccMapper.getBlCcSettleInfo(paramMap);
	}
	/**
	 * 查询未日结结算信息
	 * @param param
	 * @param user
	 * @return
	 */
	public List<Map<String,Object>> queryBlSettleInfoByUnCC(String param,IUser user){ 
		Map<String,Object> paramMap = JsonUtil.readValue(param, Map.class); 
		paramMap.put("pkOrg", ((User)user).getPkOrg());
		paramMap.put("pkEmp", ((User)user).getPkEmp());
		return zsbaOpblccMapper.getNoBlCcSettleInfo(paramMap);
	}
	
	/**
	 * 交易号：022003027095
	 * 未日结中查询收费收退款信息
	 * @param param
	 * @param user
	 * @return
	 */
	public List<Map<String,Object>> searchBlCcInfoUnSum(String param,IUser user){
		Map<String,Object> paramMap = JsonUtil.readValue(param, Map.class); 
		paramMap.put("pkOrg", ((User)user).getPkOrg());
		paramMap.put("pkEmp", ((User)user).getPkEmp());
		List<Map<String,Object>> resList = zsbaOpblccMapper.searchBlCcUnSum(paramMap);
		return resList;
	}
	
	/**
	 * 交易号：022003027091
	 * 查询已结账业务结算记录列表
	 * @param param
	 * @param user
	 * @return
	 */
	public List<Map<String,Object>> searchBlCcInfoByCc(String param,IUser user){
		Map<String,Object> paramMap = JsonUtil.readValue(param, Map.class); 
		paramMap.put("pkOrg", ((User)user).getPkOrg());
		paramMap.put("pkEmp", ((User)user).getPkEmp());
		List<Map<String,Object>> resList = zsbaOpblccMapper.searchBlCcByCc(paramMap);
		return resList;
	}

	public Date dateTrans(String date) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
		Date temp = null;
		if (StringUtils.hasText(date)) {
			try {
				temp = sdf.parse(date);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		return temp;
	}
	
	
	public ZsbaOpBlCcVo SaveZzjRj(String param,IUser user )
	{
		Map<String,Object> paramMap = JsonUtil.readValue(param, Map.class);				
		String empCode =paramMap.get("empCode").toString();	
		Map<String,Object> params = Maps.newHashMap();
		try {
			String dateStr = paramMap.get("dateEnd").toString() +" 23:59:59";
			SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			Date dateEnd = simpleDateFormat.parse(dateStr);
			params.put("dateEnd",dateEnd );
			BdOuEmployee	employee  =DataBaseHelper.queryForBean("	select * from bd_ou_employee  WHERE code_emp =? ", BdOuEmployee.class, new Object[]{empCode});
			user= 	inittUser(employee); 
		} catch (ParseException e) {
			
			e.printStackTrace();
		}
		return saveBlCc(JsonUtil.writeValueAsString(params),user);
	}
	
	private IUser inittUser(BdOuEmployee employee){
		User user = new User();
		user.setPkEmp(employee.getPkEmp());
		user.setNameEmp(employee.getNameEmp());
		user.setCodeEmp(employee.getCodeEmp());
		user.setPkOrg(employee.getPkOrg());
		user.setPkDept(MapUtils.getString(DataBaseHelper.queryForMap("select pk_dept from BD_OU_EMPJOB where pk_emp=? and del_flag='0'",
				new Object[]{employee.getPkEmp()}), "pkDept"));
		UserContext.setUser(user);
		return user;
	}
	
	public void DeleteZzjRj(String param,IUser user )
	{
		Map<String,Object> paramMap = JsonUtil.readValue(param, Map.class);				
		String pkCc =paramMap.get("pkCc").toString();	
		
		Map<String,Object> params = Maps.newHashMap();
		params.put("pkCc",pkCc );
		BlCc	blcc  =DataBaseHelper.queryForBean("	select * from bl_cc  WHERE pk_cc =? ", BlCc.class, new Object[]{pkCc});
		BdOuEmployee	employee  =DataBaseHelper.queryForBean("	select * from bd_ou_employee  WHERE pk_emp =? ", BdOuEmployee.class, new Object[]{blcc.getPkEmpOpera()});
		user= 	inittUser(employee); 
		 deleteBlCc(JsonUtil.writeValueAsString(params),user);
	}
	
}
