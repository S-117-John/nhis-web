package com.zebone.nhis.pro.zsba.compay.pub.service;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.annotation.Resource;

import net.sf.json.JSONObject;

import org.apache.shiro.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import com.zebone.nhis.bl.pub.service.HpService;
import com.zebone.nhis.bl.pub.service.PareAccoutService;
import com.zebone.nhis.bl.pub.service.PriceStrategyService;
import com.zebone.nhis.bl.pub.support.Constant;
import com.zebone.nhis.bl.pub.support.InvPrintProcessUtils;
import com.zebone.nhis.bl.pub.support.InvSettltService;
import com.zebone.nhis.bl.pub.vo.BlInvPrint;
import com.zebone.nhis.bl.pub.vo.InvInfoVo;
import com.zebone.nhis.common.dao.BlIpPubMapper;
import com.zebone.nhis.common.module.base.bd.price.BdHp;
import com.zebone.nhis.common.module.base.bd.price.BdPayer;
import com.zebone.nhis.common.module.bl.BlDeposit;
import com.zebone.nhis.common.module.bl.BlDepositPi;
import com.zebone.nhis.common.module.bl.BlInvoiceDt;
import com.zebone.nhis.common.module.bl.BlIpDt;
import com.zebone.nhis.common.module.bl.BlSettle;
import com.zebone.nhis.common.module.bl.BlSettleAr;
import com.zebone.nhis.common.module.bl.BlSettleDetail;
import com.zebone.nhis.common.module.bl.BlStInv;
import com.zebone.nhis.common.module.mybatis.ReflectHelper;
import com.zebone.nhis.common.module.pay.BlExtPay;
import com.zebone.nhis.common.module.pi.PiAcc;
import com.zebone.nhis.common.module.pi.PiMaster;
import com.zebone.nhis.common.module.pi.acc.PiAccDetail;
import com.zebone.nhis.common.module.pi.support.PiConstant;
import com.zebone.nhis.common.module.pv.PvEncounter;
import com.zebone.nhis.common.service.BalAccoutService;
import com.zebone.nhis.common.service.CommonService;
import com.zebone.nhis.common.support.ApplicationUtils;
import com.zebone.nhis.common.support.CommonUtils;
import com.zebone.nhis.common.support.EnumerateParameter;
import com.zebone.nhis.common.support.MathUtils;
import com.zebone.nhis.ma.pub.platform.PlatFormSendUtils;
import com.zebone.nhis.pro.zsba.compay.ins.pub.dao.InsPubIpSettleQryMapper;
import com.zebone.nhis.pro.zsba.compay.pub.dao.ZsBlIpSettleQryMapper;
import com.zebone.nhis.pro.zsba.compay.pub.dao.ZsbaBlIpPubMapper;
import com.zebone.nhis.pro.zsba.compay.pub.support.ZsbaInvSettltService;
import com.zebone.nhis.pro.zsba.compay.pub.vo.BlSettleDetailBa;
import com.zebone.nhis.pro.zsba.compay.pub.vo.ZsbaBlDeposit;
import com.zebone.nhis.pro.zsba.compay.pub.vo.ZsbaBlInvPrint;
import com.zebone.nhis.pro.zsba.compay.pub.vo.ZsbaBlInvoice;
import com.zebone.nhis.pro.zsba.compay.pub.vo.ZsbaBlSettle;
import com.zebone.nhis.pro.zsba.compay.pub.vo.ZsbaSettleInfo;
import com.zebone.nhis.pro.zsba.compay.up.vo.AmountBean;
import com.zebone.nhis.pro.zsba.compay.up.vo.PayAliRecord;
import com.zebone.nhis.pro.zsba.compay.up.vo.PayUnionpayRecord;
import com.zebone.nhis.pro.zsba.compay.up.vo.PayWechatRecord;
import com.zebone.nhis.pro.zsba.compay.up.vo.SettleRefundRecord;
import com.zebone.platform.common.support.NHISUUID;
import com.zebone.platform.common.support.User;
import com.zebone.platform.common.support.UserContext;
import com.zebone.platform.framework.support.IUser;
import com.zebone.platform.modules.dao.DataBaseHelper;
import com.zebone.platform.modules.exception.BusException;
import com.zebone.platform.modules.utils.JsonUtil;


/**
 * 出院结算服务接口
 * @author lipz
 * @date 2019-12-20
 */
@Service
public class ZsIpSettleService {

	@Autowired private BlIpPubMapper blIpPubMapper;
	@Autowired private ZsbaBlIpPubMapper zsbaBlIpPubMapper;
	@Autowired private HpService hpService;
	@Autowired private CommonService commonService;
	@Autowired private ZsbaInvSettltService zsbaInvSettltService;
	@Autowired private BalAccoutService balAccoutService;
	@Autowired private PriceStrategyService priceStrategyService;
	@Autowired private PareAccoutService pareAccoutService;
	@Autowired private InsPubIpSettleQryMapper insPubIpSettleQryMapper;
	@Autowired private ZSYBSettleService zSYBSettleService;
	@Autowired private InvSettltService invSettltService;
	@Autowired private ZsBlIpSettleQryMapper zsBlIpSettleQryMapper;
	
	// 用来控制手动事物
	@Resource(name = "transactionManager")
	private PlatformTransactionManager platformTransactionManager;
	
	/**
	 * 功能号[022003002016]
	 * 获取结算弹窗的初始化数据
	 * @param param DateBegin:'开始日期'DateEnd:'结束日期' PkPv:'就诊主键' 
	 * @param user
	 * @return 结算金额  预交金额? 医保支付 患者支付 账户支付 结算应收
	 */
	@SuppressWarnings("unused")
	public Map<String, Object>  GetAmtInfo(String param, IUser user) {
		//因为科研结算算中途结算，所以开始时间已经不准了要弃用
		Map<String,Object> res = new HashMap<String,Object>();
		User userinfo = (User)user;
		@SuppressWarnings("unchecked")
		Map<String,Object> paraMap = JsonUtil.readValue( param, Map.class); 

		String pkPv = (String)paraMap.get("pkPv");
		String dateBegin =(String)paraMap.get("dateBegin");
		Date begin = dateTrans(dateBegin);
		String dateEnd =(String)paraMap.get("dateEnd");	
		Date end = dateTrans(dateEnd);
		String euSttype = (String)paraMap.get("euSttype");//结算类型0：出院结算 1:中途结算 2：科研结算 
		String pkDept = (String)paraMap.get("pkDept");	//不为空则为分科结算
		String pkCgips = (String)paraMap.get("pkCgips");	//不为空则为明细结算
		Double amtInsuThird = Double.valueOf(paraMap.get("amtInsuThird")==null?"0":paraMap.get("amtInsuThird").toString());
		Double tcje = Double.valueOf(paraMap.get("tcje")==null?"0":paraMap.get("tcje").toString());
		Double gzje = Double.valueOf(paraMap.get("gzje")==null?"0":paraMap.get("gzje").toString());
		Double yfje = Double.valueOf(paraMap.get("yfje")==null?"0":paraMap.get("yfje").toString());
		Double dbje = Double.valueOf(paraMap.get("dbje")==null?"0":paraMap.get("dbje").toString());
		Double zdbjdxAmt = Double.valueOf(paraMap.get("zdbjdxAmt")==null?"0":paraMap.get("zdbjdxAmt").toString());
		res.put("Tcje", tcje);
		res.put("Gzje", gzje);
		res.put("Yfje", yfje);
		res.put("Dbje", dbje);
		//定义一些公共变量
		StringBuffer sqlSt;
		Map<String,Object> amtStMap;
		BigDecimal amtSt;//结算金额AmtSt
		String cgips = null;
		if(pkCgips != null){
			cgips = "";
			String[] cgipsArgs = pkCgips.split(",");
			int i = 0;
			for (String pkcgip : cgipsArgs) {
				if (i == 0) {
					cgips += "'" + pkcgip.trim() + "'";
				} else {
					cgips += ",'" + pkcgip.trim() + "'";
				}
				i++;
			}
			
			//结算金额AmtSt
			sqlSt = new StringBuffer(" select Sum(amount) AmtSt from bl_ip_dt blip where blip.flag_settle = '0' and blip.pk_cgip in (" + cgips + ")");
			sqlSt.append(" and blip.pk_pv = ?  and  blip.date_hap <= ? and blip.del_flag = '0'");
			amtStMap = DataBaseHelper.queryForMap(sqlSt.toString(), pkPv,end);
			amtSt = amtTrans(amtStMap.get("amtst"));
			res.put("AmtSt", amtSt);
			//冲账结算发票金额=新增收退金额+被冲账结算金额AmtStrikeInv
//			StringBuffer strikeInvSt = new StringBuffer(" select Sum(amount) AmtSt from bl_ip_dt blip where 1=1 and blip.pk_cgip in (" + pkCgips + ")");
//			strikeInvSt.append(" and blip.pk_pv = ?  and blip.date_cg >= ? and blip.date_cg <= ?");
//			Map<String,Object> strikeInvStMap = DataBaseHelper.queryForMap(strikeInvSt.toString(), pkPv,begin,end);
			StringBuffer strikeInvSt = new StringBuffer(" select Sum(amount) AmtSt from bl_ip_dt blip where 1=1 and blip.pk_cgip in (" + cgips + ")");
			strikeInvSt.append(" and blip.pk_pv = ?  and blip.date_hap <= ? and blip.del_flag = '0'");
			Map<String,Object> strikeInvStMap = DataBaseHelper.queryForMap(strikeInvSt.toString(), pkPv,end);
			BigDecimal strikeInvStAmt = amtTrans(strikeInvStMap.get("amtst"));
			res.put("AmtStrikeInv", strikeInvStAmt);
		}else{
			//结算金额AmtSt
			sqlSt = new StringBuffer(" select Sum(amount) AmtSt from bl_ip_dt blip where blip.flag_settle = '0' ");
			sqlSt.append(" and blip.pk_pv = ?  and blip.date_hap <= ? and blip.del_flag = '0'");
			if(pkDept!=null){
				sqlSt.append(" and blip.pk_dept_app = ? ");
				amtStMap = DataBaseHelper.queryForMap(sqlSt.toString(), pkPv,end, pkDept);
			}else{
				amtStMap = DataBaseHelper.queryForMap(sqlSt.toString(), pkPv,end);
			}
			
			amtSt = amtTrans(amtStMap.get("amtst"));
			res.put("AmtSt", amtSt);
			//冲账结算发票金额=新增收退金额+被冲账结算金额AmtStrikeInv
			
//			StringBuffer strikeInvSt = new StringBuffer(" select Sum(amount) AmtSt from bl_ip_dt blip where 1=1  ");
//			strikeInvSt.append(" and blip.pk_pv = ?  and blip.date_cg >= ? and blip.date_cg <= ?");
//			Map<String,Object> strikeInvStMap = DataBaseHelper.queryForMap(strikeInvSt.toString(), pkPv,begin,end);
			//这个逻辑好像没用到
			StringBuffer strikeInvSt = new StringBuffer(" select Sum(amount) AmtSt from bl_ip_dt blip where 1=1  ");
			strikeInvSt.append(" and blip.pk_pv = ?  and blip.date_hap <= ? and blip.del_flag = '0'");
			if(pkDept!=null){
				strikeInvSt.append(" and blip.pk_dept_app = ? ");
				Map<String,Object> strikeInvStMap = DataBaseHelper.queryForMap(strikeInvSt.toString(), pkPv,end, pkDept);
				BigDecimal strikeInvStAmt = amtTrans(strikeInvStMap.get("amtst"));
				res.put("AmtStrikeInv", strikeInvStAmt);
			}else{
				Map<String,Object> strikeInvStMap = DataBaseHelper.queryForMap(strikeInvSt.toString(), pkPv,end);
				BigDecimal strikeInvStAmt = amtTrans(strikeInvStMap.get("amtst"));
				res.put("AmtStrikeInv", strikeInvStAmt);
			}
			
		}
		//预缴金额AmtPrep		
		//StringBuffer sqlPrep = new StringBuffer("select sum(depo.amount)  AmtPrep from bl_deposit depo where depo.eu_dptype=9 and depo.flag_settle = '0' and  depo.pk_pv = ?  and depo.date_pay <= ? and depo.del_flag = '0'");
		StringBuffer sqlPrep = new StringBuffer("select sum(depo.amount)  AmtPrep from bl_deposit depo where depo.eu_dptype=9 and depo.flag_settle = '0' and  depo.pk_pv = ?   and depo.del_flag = '0'");
		
		Map<String,Object> amtPrepMap = DataBaseHelper.queryForMap(sqlPrep.toString(), pkPv);
		BigDecimal amtPrep = amtTrans(amtPrepMap.get("amtprep"));
		res.put("AmtPrep",amtPrep);	
		//医院优惠=第三方优惠
		//医保支付	
		ZsbaBlSettle tempSt = new ZsbaBlSettle();
		//这个地方如果直接传pkCgips，sql查询会很慢
		List<String> pkCgipList = null;
		if(pkCgips!=null){
			pkCgipList = new ArrayList<String>();
			String[] cgipArr = pkCgips.split(",");
			for(String cgip : cgipArr){
				pkCgipList.add(cgip);
			}
		}
		List<BlSettleDetailBa> details = handleSettleDetailC(null, pkPv,tempSt,userinfo.getPkOrg(),euSttype,dateBegin,dateEnd, amtInsuThird, 0d, 0d, 
				pkDept, cgips, zdbjdxAmt);
		Double amtTemp = 0.0;
		Double amtPiTemp = 0.0;
		Double amtAdd = 0.0;
		String payerSelf = qryBdPayerByEuType(userinfo.getPkOrg());
		String payerJs = qryBdPayerByCode("03");//计划生育付款方
		String payeret = qryBdPayerByCode("08");//儿童福利院付款方
		String payerBj = qryBdPayerByCode("14");//重点保健对象付款方
		String payerInv = qryBdPayerByEuType("02");
		boolean fyjyFlag = true; //自费、加收以外的费用校验，目前用于校验科研医嘱
		for(BlSettleDetailBa vo : details){
			if(!payerSelf.equals(vo.getPkPayer())){
				if(payerInv.equals(vo.getPkPayer())||payerJs.equals(vo.getPkPayer())||payeret.equals(vo.getPkPayer())||payerBj.equals(vo.getPkPayer())){
					amtTemp+=vo.getAmount();
					amtAdd+=vo.getAmountAdd();
				}else{
					//外部医保的单独算了，这个不用累计了
				}
			}else{
				//res.put("AmtPi", vo.getAmount()-amtInsuThird);
				amtPiTemp+=vo.getAmount();
				amtAdd+=vo.getAmountAdd();
			}
		}
		res.put("fyjyFlag", true);
		//amtTemp = amtTemp - amtAdd;
		res.put("amtAdd", amtAdd);
		BigDecimal amtPi = new BigDecimal(amtPiTemp);
		amtPi = amtPi.setScale(2, BigDecimal.ROUND_HALF_UP);
		res.put("AmtPi", amtPi.compareTo(BigDecimal.ZERO)<0?new BigDecimal(0):amtPi);
		
		BigDecimal amtInsu = new BigDecimal(amtTemp);
		amtInsu = amtInsu.setScale(2, BigDecimal.ROUND_HALF_UP);
		res.put("AmtInsu", amtInsu);
		res.put("AmtInsuThird", new BigDecimal(amtInsuThird.toString()));
		List<Map<String,Object>> hpPlans = getHpPlans(pkPv);
		for(Map<String,Object> map : hpPlans){
			String flagMaj = (String)map.get("flagMaj");
			if("1".equals(flagMaj)){
				res.put("PkInsurance", map.get("pkHp"));
			}
		}
		//账户余额,授权账户的余额也可以使用
		//Map<String,Object> amtAccMap = DataBaseHelper.queryForMap("select  Amt_acc,pk_piacc from pi_acc piacc  inner join pv_encounter pv on piacc.pk_pi = pv.pk_pi where  pv.pk_pv = ?", pkPv);
		PiAcc account = pareAccoutService.getPatiAccountAvailableBalanceByPv(pkPv);
		BigDecimal amtAcc = BigDecimal.ZERO;
		String pkAcc = null;
		if(account!=null&&!PiConstant.ACC_EU_STATUS_2.equals(account.getEuStatus())){//存在账户且未冻结的情况
			amtAcc = account.getAmtAcc()==null?BigDecimal.ZERO:account.getAmtAcc();		
			pkAcc = account.getPkPiacc();	
		}
		res.put("pkAcc", pkAcc);
		//患者支付	本次结算金额合计-预交金额-医保支付；AmtPi =AmtSt - AmtPrep -AmtInsu - AmtInsuThird
		BigDecimal amtGet = 	amtSt.subtract(amtPrep).subtract(amtInsu).subtract(new BigDecimal(amtInsuThird.toString()));
		amtGet = amtGet.setScale(2, BigDecimal.ROUND_HALF_UP);
		
		if(amtGet.compareTo(BigDecimal.ZERO)<0){//预交金够，患者不用支付，账户支付为0
			res.put("AmtAcc", BigDecimal.ZERO);
			res.put("AmtGet", amtGet);
			return set2Scal(res);
		}else{//预交金不够
			if(amtAcc.compareTo(BigDecimal.ZERO)>0){//如果患者账户有钱，先用
				amtGet = amtGet.subtract(amtAcc);
				if(amtGet.compareTo(BigDecimal.ZERO)<0){//账户有钱且足以支付应交费用，还需支付剩下的
					res.put("AmtAcc", amtSt.subtract(amtPrep).subtract(amtInsu));
					res.put("AmtGet", BigDecimal.ZERO);
					return set2Scal(res);
				}else{//账户有钱，但不足以支付应交费用，还需支付0
					res.put("AmtAcc",amtAcc);
					res.put("AmtGet", amtGet);
					return set2Scal(res);
				}
			}else{//如果患者账户没钱，账户支付为0
				res.put("AmtAcc", BigDecimal.ZERO);
				res.put("AmtGet", amtGet);
				return set2Scal(res);
			}
		} 	
	}
	
	/**
	 * 功能号[022003002017]
	 * 获取结算弹窗的初始化数据(科研)
	 * @param param DateBegin:'开始日期'DateEnd:'结束日期' PkPv:'就诊主键' 
	 * @param user
	 * @return 结算金额  预交金额? 医保支付 患者支付 账户支付 结算应收
	 */
	@SuppressWarnings("unused")
	public Map<String, Object>  GetAmtInfoKy(String param, IUser user) {
		
		Map<String,Object> res = new HashMap<String,Object>();
		User userinfo = (User)user;
		@SuppressWarnings("unchecked")
		Map<String,Object> paraMap = JsonUtil.readValue( param, Map.class); 
		
		String pkPayer =  (String)paraMap.get("pkPayer");
		String pkPv = (String)paraMap.get("pkPv");
		String dateBegin =(String)paraMap.get("dateBegin");
		Date begin = dateTrans(dateBegin);
		String dateEnd =(String)paraMap.get("dateEnd");	
		Date end = dateTrans(dateEnd);
		String euSttype = (String)paraMap.get("euSttype");//结算类型0：出院结算 1:中途结算 2：科研结算
		Double amtInsuThird = Double.valueOf(paraMap.get("amtInsuThird")==null?"0":paraMap.get("amtInsuThird").toString());// xr+  第三方医保有前端传过来
		//定义一些公共变量
		StringBuffer sqlSt;
		Map<String,Object> amtStMap;
		BigDecimal amtSt;//结算金额AmtSt
		BigDecimal amtInsu;//记账金额
		BigDecimal amtPi;//自费金额
		//结算金额AmtSt
		sqlSt = new StringBuffer(" Select sum(dt.AMOUNT) amtst, sum(dt.AMOUNT - dt.AMOUNT_PI)  amtinsu, sum(dt.AMOUNT_PI)  amtpi from BL_IP_DT dt ");
		sqlSt.append(" where dt.FLAG_SETTLE='0' and dt.pk_pv =? and dt.pk_payer = ? ");
		amtStMap = DataBaseHelper.queryForMap(sqlSt.toString(), pkPv, pkPayer);
		amtSt = amtTrans(amtStMap.get("amtst")==null?"0.00":amtStMap.get("amtst"));
		amtInsu = amtTrans(amtStMap.get("amtinsu")==null?"0.00":amtStMap.get("amtinsu"));
		amtPi = amtTrans(amtStMap.get("amtpi")==null?"0.00":amtStMap.get("amtpi"));
		res.put("AmtSt", amtSt);
		res.put("AmtInsu", amtInsu);
		res.put("amtAdd", BigDecimal.ZERO);
		res.put("AmtPi", amtPi);
		
		if(amtSt.compareTo(amtPi)==0){
			//目前医嘱会出现科研结算全自费的情况，这种情况是不对的，需要再这里加个验证
			throw new BusException("科研医嘱费用不对，请联系信息科处理！");
		}
		
		//科研結算時，如果自费为0，就不要使用预交金了
		BigDecimal amtPrep = BigDecimal.ZERO;
		if(Double.parseDouble(amtPi.toString())>0){
			//预缴金额AmtPrep		
			StringBuffer sqlPrep = new StringBuffer("select sum(depo.amount)  AmtPrep from bl_deposit depo where depo.eu_dptype=9 and depo.flag_settle = '0' and  depo.pk_pv = ? and depo.date_pay <= ? and depo.del_flag = '0'");
			Map<String,Object> amtPrepMap = DataBaseHelper.queryForMap(sqlPrep.toString(), pkPv,end);
			amtPrep = amtTrans(amtPrepMap.get("amtprep"));
		}
		res.put("AmtPrep",amtPrep);	
		//账户余额,授权账户的余额也可以使用
		//Map<String,Object> amtAccMap = DataBaseHelper.queryForMap("select  Amt_acc,pk_piacc from pi_acc piacc  inner join pv_encounter pv on piacc.pk_pi = pv.pk_pi where  pv.pk_pv = ?", pkPv);
		PiAcc account = pareAccoutService.getPatiAccountAvailableBalanceByPv(pkPv);
		BigDecimal amtAcc = BigDecimal.ZERO;
		String pkAcc = null;
		if(account!=null&&!PiConstant.ACC_EU_STATUS_2.equals(account.getEuStatus())){//存在账户且未冻结的情况
			amtAcc = account.getAmtAcc()==null?BigDecimal.ZERO:account.getAmtAcc();		
			pkAcc = account.getPkPiacc();	
		}
		res.put("pkAcc", pkAcc);
		//患者支付	本次结算金额合计-预交金额-医保支付；AmtPi =AmtSt - AmtPrep -AmtInsu
		BigDecimal amtGet = 	amtSt.subtract(amtPrep).subtract(amtInsu);
		amtGet = amtGet.setScale(2, BigDecimal.ROUND_HALF_UP);
		
		if(amtGet.compareTo(BigDecimal.ZERO)<0){//预交金够，患者不用支付，账户支付为0
			res.put("AmtAcc", BigDecimal.ZERO);
			res.put("AmtGet", amtGet);
			return set2Scal(res);
		}else{//预交金不够
			if(amtAcc.compareTo(BigDecimal.ZERO)>0){//如果患者账户有钱，先用
				amtGet = amtGet.subtract(amtAcc);
				if(amtGet.compareTo(BigDecimal.ZERO)<0){//账户有钱且足以支付应交费用，还需支付剩下的
					res.put("AmtAcc", amtSt.subtract(amtPrep).subtract(amtInsu));
					res.put("AmtGet", BigDecimal.ZERO);
					return set2Scal(res);
				}else{//账户有钱，但不足以支付应交费用，还需支付0
					res.put("AmtAcc",amtAcc);
					res.put("AmtGet", amtGet);
					return set2Scal(res);
				}
			}else{//如果患者账户没钱，账户支付为0
				res.put("AmtAcc", BigDecimal.ZERO);
				res.put("AmtGet", amtGet);
				return set2Scal(res);
			}
		} 	
	}
	
	
	private Map<String,Object> set2Scal(Map<String,Object> map){
		if(map!=null){
			for(Entry<String,Object> e : map.entrySet()){
				String key = e.getKey();
				if(e.getValue()!=null){
					if(isNumber(e.getValue().toString())){
						BigDecimal val  = new BigDecimal(e.getValue().toString());
						map.put(key, val.setScale(2, BigDecimal.ROUND_HALF_UP));
					}else{
						map.put(key, e.getValue());
					}
				}else{
					map.put(key, e.getValue());
				}
			}
		}
		return map;
	}
	
	public static boolean isNumber(String str){  
        String reg = "^[0-9]+(.[0-9]+)?$";  
        return str.matches(reg);  
    }  
	
	/**
	 * 功能号[022003002002]
	 * 结算数据保存
	 * @param param
	 * @param user
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public String dealSettleData(String param, IUser user) {
		/*
		 * 1.参数接收		
		 */
		ZsbaSettleInfo allData = JsonUtil.readValue(param, ZsbaSettleInfo.class);	
		String pkPv = allData.getPkPv();					//就诊主键
		String euSttype = allData.getEuSttype();			//结算类型
		String euStresult = allData.getEuStresult();		//结算结果类型
		String dateEnd = allData.getDateEnd();				//结算截止时间
		String flagHbPrint = allData.getFlagHbPrint();		//是否合并特诊发票
		ZsbaBlDeposit fromSettle = allData.getDeposit();		//收退找零金额
		ZsbaBlDeposit depositAcc = allData.getDepositAcc();		//账户支付金额
		BlExtPay blExtPay = allData.getBlExtPay();			//第三方支付交易记录 xr+
		List<InvInfoVo> invoInfos = allData.getInvos();		//发票信息
		String dtDeptType = allData.getDtDeptType();		//操作人员所属部门类型, 08收费处、01护士站
		String pkDept = allData.getPkDept(); //科室主键，分科结算时传入此值
		String pkCgips = allData.getPkCgipsStr(); //明细主键，明细结算时传入此值
		BigDecimal zdbjdxAmt = allData.getZdbjdxAmt();//重点保健对象金额
		
		String cgips = null;
		if(pkCgips!=null){
			String[] cgipArr = pkCgips.split(",");
			List<String> cgipList = new ArrayList<String>();
			for(String cgip : cgipArr){
				cgipList.add(cgip);
			}
			allData.setPkCgips(cgipList);
			
			cgips = "";
			String[] cgipsArgs = pkCgips.split(",");
			int i = 0;
			for (String pkcgip : cgipsArgs) {
				if (i == 0) {
					cgips += "'" + pkcgip.trim() + "'";
				} else {
					cgips += ",'" + pkcgip.trim() + "'";
				}
				i++;
			}
		}
		
		/*
		 * 1.1.从[患者就诊-就诊记录 ]+[中途结算记录]中获取开始日期
		 */
		String dateBegin = "";
		String pvSql = "select * from pv_encounter where pk_pv=?";
		PvEncounter pvvo = DataBaseHelper.queryForBean(pvSql, PvEncounter.class, pkPv);
		Date midSettle = checkMidSettle(pkPv);
		if(midSettle == null){
			dateBegin = dateTrans(pvvo.getDateBegin());
		}else{
			dateBegin = dateTrans(midSettle);
		}
		
		/*
		 *  关闭事务自动提交, 修改为手动事物
		 */
		DefaultTransactionDefinition def = new DefaultTransactionDefinition();
		def.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRES_NEW);
		TransactionStatus status = platformTransactionManager.getTransaction(def);
		ZsbaBlSettle stVo = null;
		try{
			//String sss = null;
			//sss = sss.toString();
			/*
			 * 2.结算数据准备、结算明细数据准备		
			 */
			//	2.1.结算数据准备，处理了账户支付后，患者账户相关pi_acc,pi_acc_detial表
			Map<String,Object> stDataMap = settleData(allData,pkPv,euSttype,dateBegin,dateEnd,user,euStresult, pkDept, Double.parseDouble(zdbjdxAmt==null?"0":zdbjdxAmt.toString()));
			List<BlSettleDetail> stDtVos = (List<BlSettleDetail>) stDataMap.get("detail");
			stVo = (ZsbaBlSettle)stDataMap.get("settle");
			for(BlSettleDetail vo : stDtVos){
				setDefaultValue(vo, true);
				vo.setPkStdt(NHISUUID.getKeyId());
			}
			setDefaultValue(stVo, true);
			//	2.2.结算数据写入
			//是医保身份但不是医保结算的时候，这条结算记录的主医保要改成全自费
			BdHp bdHp = DataBaseHelper.queryForBean("select * from bd_hp where pk_hp=?", BdHp.class, stVo.getPkInsurance());
			if(bdHp.getCode().equals("00021") || bdHp.getCode().equals("00022") || bdHp.getCode().equals("00023")
					|| bdHp.getCode().equals("00024") || bdHp.getCode().equals("00025") || bdHp.getCode().equals("00031")
					|| bdHp.getCode().equals("00032") || bdHp.getCode().equals("0004") || bdHp.getCode().equals("00051")
					|| bdHp.getCode().equals("00052")|| bdHp.getCode().equals("00053")|| bdHp.getCode().equals("00054")
					|| bdHp.getCode().equals("00058")|| bdHp.getCode().equals("00059"))
			{
				if(!zSYBSettleService.checkYbSt(allData, stVo)){
					BdHp bdHpZf = DataBaseHelper.queryForBean("select * from bd_hp where code = ?", BdHp.class, "0001");
					stVo.setPkInsurance(bdHpZf.getPkHp());
				}
			}
			stVo.setNote(allData.getNote());
			stVo.setDtFayplgitem(allData.getDtFayplgitem());
			stVo.setAddrCurTown(allData.getAddrCurTown());
			DataBaseHelper.insertBean(stVo);
			DataBaseHelper.batchUpdate(DataBaseHelper.getInsertSql(BlSettleDetail.class), stDtVos);
	
			/*
			 * 3.结算结果类型判断，更新付款表-bl_deposit
			 */
			//	3.1.更新收款表和外部接口支付表
			
			if("2".equals(euSttype)&&Double.parseDouble(stVo.getAmountPi().toString())==0){
				//科研结算并且自付等于0的时候，不使用预交金
			}else{
				updDepositInfo(pkPv,stVo.getPkSettle(),dateTrans(dateEnd));
			}
		    //	3.2.写入结算时账户支付的收付款记录（结算信息标志已加入）
		    if(depositAcc!=null){
				setDepoInfo(depositAcc, stVo);
				depositAcc.setEuDptype(euSttype);
				setDefaultValue(depositAcc, true);
				DataBaseHelper.insertBean(depositAcc);
		    }
		    //	3.3.写入结算时，多退少补的收付款记录（结算信息标志已加入）
			if(fromSettle!=null){
				/** 出院结算 */
				//if("0".equals(euSttype)){
					setDepoInfo(fromSettle, stVo);
					String euSttypeName = "";
					if("0".equals(euSttype)){
						euSttypeName = "出院结算";
						fromSettle.setEuDptype("0");
					}else if("1".equals(euSttype)){
						euSttypeName = "中途结算";
						fromSettle.setEuDptype("1");
					}else if("2".equals(euSttype)){
						euSttypeName = "科研结算";
						fromSettle.setEuDptype("1");
					}
					if(EnumerateParameter.TWO.equals(euStresult)){
						/*	3.3.1.存款结算 */
						//settleForDeposit(stVo,user,pkPv);//账户充值过程
						
						
						
						/*
						 *  TODO 
						 *  当不使用账户并且没有选中现金退费，需生成可退款押金记录;
						 */
						if(!EnumerateParameter.ONE.equals(fromSettle.getDtPaymode())
								//&&!EnumerateParameter.THREE.equals(fromSettle.getDtPaymode())
								&&!fromSettle.getDtPaymode().equals("92")){
							/*
							 * 1.生成可退款记录同时标记未退款。
							 * 2.弹出框给收费员 选择退款的押金记录 或 选择转财务退。
							 * 3.在执行退款 或 转财务退款过程中，生成退款预交金记录 并 标记为已退款和关联预交金主键。
							 * 4.财务在完成转账退款时更新退款标记为已退款。
							 */
							String dateFmt = "yyyy-MM-dd HH:mm:ss";
							saveRefundData(stVo, dateTrans(dateTrans(dateEnd), dateFmt));
						}else {//	生成退款预交金的记录
							if(EnumerateParameter.ONE.equals(fromSettle.getDtPaymode())){
								fromSettle.setNote(euSttypeName+"：退现金");
							}else if(EnumerateParameter.THREE.equals(fromSettle.getDtPaymode())){
								//新邏輯這裏不會進來了
								fromSettle.setDtBank("1");
								fromSettle.setPayInfo(blExtPay.getPkExtpay());
								fromSettle.setNote(euSttypeName+"：退银行卡");
							}else if(fromSettle.getDtPaymode().equals("92")){
								//fromSettle.setDtBank("1");
								//fromSettle.setPayInfo(blExtPay.getPkExtpay());
								fromSettle.setNote(euSttypeName+"：退财务记账");
							}
							
							setDefaultValue(fromSettle, true);
							fromSettle.setDateReptBack(null);
							DataBaseHelper.insertBean(fromSettle);
						}
						
					}else if(EnumerateParameter.ONE.equals(euStresult)){
						/*	3.3.2.欠款结算 */		  
						settleForArrears(stVo);//欠费记录过程
					}else{
						/*	3.3.3.正常结算 */
						fromSettle.setEuDptype("0");
						setDefaultValue(fromSettle, true);
						fromSettle.setDateReptBack(null);
						fromSettle.setVoidType("0");
						if(fromSettle.getDtPaymode().equals("3")){
							fromSettle.setDtBank("1");
							fromSettle.setPayInfo(blExtPay.getPkExtpay());
							fromSettle.setNote("出院结算：收银行卡");
						}else if(fromSettle.getDtPaymode().equals("7")){
							fromSettle.setDtBank("1");
							fromSettle.setPayInfo(blExtPay.getPkExtpay());
							fromSettle.setNote("出院结算：收微信");
						}else if(fromSettle.getDtPaymode().equals("8")){
							fromSettle.setDtBank("1");
							fromSettle.setPayInfo(blExtPay.getPkExtpay());
							fromSettle.setNote("出院结算：收支付宝");
						}else if(fromSettle.getDtPaymode().equals("1")){
							fromSettle.setNote("出院结算：收现金");
						}
						DataBaseHelper.insertBean(fromSettle);
					}
/*				}else if("1".equals(euSttype)|| "2".equals(euSttype)){*//** 中途结算，少收多不退，但是存预交金 *//*
					
					 * 3.4.结算类型为中途结算或科研结算时，如果预交金额合计>结算费用合计，
					 * 		将其差额生成新的预交金记录（eu_dptype=9），
					 * 		写bl_deposit表，
					 * 		并在pk_st_mid字段记录中途结算主键；
					 * 	如果预交金合计<结算费用合计，处理方式同出院结算；
					 
					if("-1".equals(fromSettle.getEuDirect())){//预交金多了，先退预交金，后转存预交金，要写两条记录
						//	3.4.1 本次结算退多余预交金的记录
						ZsbaBlDeposit rtn = (ZsbaBlDeposit)fromSettle.clone();
						setDepoInfo(rtn, stVo);
						rtn.setEuDptype("1");
						rtn.setReptNo(null);
						rtn.setPkEmpinv(null);
						setDefaultValue(rtn, true);
						DataBaseHelper.insertBean(rtn);
						//	3.4.2 存一笔预交金的收款记录
						fromSettle.setEuDptype("9");
						fromSettle.setEuDirect("1");
						fromSettle.setBankNo(null);
						fromSettle.setPayInfo(null);
						fromSettle.setPkStMid(stVo.getPkSettle());
						BigDecimal amt = fromSettle.getAmount();
						fromSettle.setAmount(amt.multiply(new BigDecimal(-1)));
						fromSettle.setNote("中途结算转存");
						fromSettle.setFlagSettle("0");
						fromSettle.setPkSettle(null);
						fromSettle.setDateReptBack(null);
						fromSettle.setVoidType("0");
					}else{
							3.5.预交金少了，收款 
						fromSettle.setEuDptype("1");
						fromSettle.setEuDirect("1");
						setDepoInfo(fromSettle, stVo);
					}
					setDefaultValue(fromSettle, true);
					fromSettle.setDateReptBack(null);
					DataBaseHelper.insertBean(fromSettle);
				}*/
				if(blExtPay!=null){
					//	3.6.处理第三方支付交易数据	
					blExtPay.setPkSettle(fromSettle.getPkSettle());
					saveBlExtPay(fromSettle.getPkDepo(),blExtPay);
				}
				
			}
			//	3.7.处理医保结算业务,根据医保配置文件yb.properties配置医保处理类进行处理相应医保业务
			//博爱的实际调用保存的方法就行了，不用根据配置文件去调了
			zSYBSettleService.dealYBSettleMethod(allData, stVo, allData.getPkPosTr());
			
			/*
			 * 4.更新住院记费表		
			 */
			updChargeInfo(pkPv,stVo.getPkSettle(),dateTrans(dateEnd), euSttype, allData.getPkPayer(), pkDept, cgips);
			if("0".equals(euSttype)){
				if(EnumerateParameter.ONE.equals(euStresult)){
					DataBaseHelper.execute("update pv_encounter set flag_settle='1', eu_status='3' where pk_pv=? ", pvvo.getPkPv());
					return stVo.getPkSettle();//欠款结算到此结束不做发票处理
				}
			}
			
			/*
			 * TODO: 2020-05-13 lipz，操作人员所属部门类型：从前端传入
			Map<String, Object> paraMap = new HashMap<String, Object>();
			paraMap.put("pkEmp", UserContext.getUser().getPkEmp());
			List<Map<String,Object>> deptTypeList = insPubIpSettleQryMapper.qryDtDeptType(paraMap);
			String dtDeptType = deptTypeList.get(0).get("dt_depttype").toString();
			*/
			
			/*
			 * 5.处理发票数据
			 */
			if(invoInfos!=null&&invoInfos.size()>0){  
				BigDecimal ybAmt = new BigDecimal("0");
				if(!"2".equals(euSttype)){ //科研结算传0
					ybAmt = stVo.getAmountInsu();
				}
				//	5.1.发票数据准备
				Map<String,Object> invMap = zsbaInvSettltService.invoData(pkPv,pvvo.getFlagSpec(),invoInfos,user,
																dateBegin,dateEnd,stVo.getPkSettle(),flagHbPrint, dtDeptType, ybAmt, allData.getAmountDisc());	
				List<ZsbaBlInvoice> invo = (List<ZsbaBlInvoice>) invMap.get("inv");
				List<BlInvoiceDt> invoDt = (List<BlInvoiceDt>)invMap.get("invDt");
				//	5.2写发票表bl_invoice和发票明细表bl_invoice_dt
				insertInvo(invo,invoDt);
				//	5.3.写发票和结算关系表bl_st_inv；
				insertInvoSt(invo,stVo);
				//护士站不需要保存发票号，所以这个是空的
				if(dtDeptType.equals("08")){
					// 5.4.调用发票使用确认服务，完成发票更新。
					commonService.confirmUseEmpInv(invoInfos.get(0).getInv().getPkEmpinv(), new Long(invo.size()));
				}
			}
			
			/*
			 * 6.更新pv表的结算字段；
			 */
			if("0".equals(euSttype)){
				DataBaseHelper.execute("update pv_encounter set flag_settle='1', eu_status='3' where pk_pv=? ", pvvo.getPkPv());
			}
			platformTransactionManager.commit(status); // 提交事务
		} catch (Exception e) {
			platformTransactionManager.rollback(status); // 添加失败 回滚事务；
		    throw new BusException("结算失败：" + getExceptionAllInfo(e));
		}
		
		/*
		 * 7.数据组装-HL7消息发送
		 */
		String amtKind = euSttype.equals("0") ? "3" : "";
		amtKind = euSttype.equals("1") ? "2" : "1";
		List<Map<String, Object>> hpMap = getHpPlans(allData.getPkPv());
		String amtType="";
		for (Map<String, Object> map : hpMap) {
			if(map.get("euHptype").equals("0")){
				amtType="01";
			}else if(map.get("euHptype").equals("3")){
				amtType="03";
			}else{
				amtType="02"; 
			}
		}
		User userinfo = (User)user;
		Map<String,Object> paramMap = JsonUtil.readValue(param, Map.class);	
		paramMap.put("doCode", userinfo.getCodeEmp());
		paramMap.put("doName", userinfo.getNameEmp());
		if(invoInfos!=null&& invoInfos.get(0)!=null &&invoInfos.get(0).getInv()!=null){
			paramMap.put("settleNo", invoInfos.get(0).getInv().getCurCodeInv());
		}
		paramMap.put("totalAmount", allData.getAmountSt());	//总费用
		paramMap.put("selfAmount", allData.getAmountPi());	//自费
		paramMap.put("amtInsu", allData.getAmountInsu());	//医保费用
		paramMap.put("amtType", amtType);					//结算类别
		paramMap.put("amtKind", amtKind);					//结算方式
		paramMap.put("pkPv", allData.getPkPv());			//就诊主键
		PlatFormSendUtils.sendBlSettleMsg(paramMap);
		paramMap = null;
		
		return stVo==null?"":stVo.getPkSettle();//返回结算主键
	}

	/**
	 * 获取 Exception异常详细信息
	 * @param ex
	 * @return
	 */
	public static String getExceptionAllInfo(Exception ex) {
	        ByteArrayOutputStream out = null;
	        PrintStream pout = null;
	        String ret = "";
	        try {
	        	out = new ByteArrayOutputStream();
	        	pout = new PrintStream(out);
	        	ex.printStackTrace(pout);
		        ret = new String(out.toByteArray());
		        out.close();
	        }catch(Exception e){
	        	return ex.getMessage();
	        }finally{
	        	if(pout!=null){
	        		pout.close();
	        	}
	        }
	        return ret;
	}
	
	/**
	 * 
	 * @param blSettle
	 * @param dateEnd
	 */
	public void saveRefundData(ZsbaBlSettle blSettle, String dateEnd){
		
		String dateBegin = "";
		String pvSql = "select * from pv_encounter where pk_pv=?";
		PvEncounter pvvo = DataBaseHelper.queryForBean(pvSql, PvEncounter.class, blSettle.getPkPv());
		dateBegin = dateTrans(pvvo.getDateReg());
		
		//因为结算前有科研结算，如果取中途结算是开始时间，当有科研结算的时候，是查不到记录的
/*		Date midSettle = checkMidSettle(blSettle.getPkPv());
		if(midSettle == null){
			dateBegin = dateTrans(pvvo.getDateReg());
		}else{
			dateBegin = dateTrans(midSettle);
		}*/
		
		String dateFmt = "yyyy-MM-dd HH:mm:ss";
		dateBegin = dateTrans(dateTrans(dateBegin), dateFmt);
		
		// 计算 可退款金额  = 患者预交金  - 患者自付金额；
		BigDecimal amountPi = blSettle.getAmountPi()==null?BigDecimal.ZERO:blSettle.getAmountPi();
		BigDecimal amountPrep = blSettle.getAmountPrep()==null?BigDecimal.ZERO:blSettle.getAmountPrep();
		BigDecimal deposit = amountPrep.subtract(amountPi);
		
		Object[] params = new Object[]{dateBegin, dateEnd, blSettle.getPkPi(), blSettle.getPkPv()};
/*		if(blSettle.getDtSttype().equals("21")){
			//取消结算的时候，deposit是负数的，需要改为正数
			deposit = deposit.subtract(deposit).subtract(deposit);
		}*/
		if(deposit.compareTo(BigDecimal.ZERO)>0){
			// 获取微信、支付宝、银联的交易记录收款总额
			
			//直接微信缴费，不经过第三方
			String wxSql = "select CASE WHEN sum(a.TOTAL_AMOUNT) is NULL THEN 0.0 ELSE sum(a.TOTAL_AMOUNT) END as AMOUNT " +
					"from PAY_WECHAT_RECORD a inner join bl_ext_pay b on a.PK_EXTPAY = b.pk_extpay " +
					"where a.ORDER_TYPE='pay' and a.TRADE_STATE='SUCCESS' and a.REFUND_TRADE_NO is NULL " +
					"and a.CREATE_TIME>=? and a.CREATE_TIME<=? and a.PK_PI=? and a.PK_PV=?";
					//"and b.pk_settle=?";
			AmountBean wxTotal = DataBaseHelper.queryForBean(wxSql, AmountBean.class, params);
			BigDecimal wxAmount = wxTotal.getAmount();
			
			//直接支付宝缴费，不经过第三方
			String aliSql = "select CASE WHEN sum(TOTAL_AMOUNT) is NULL THEN 0.0 ELSE sum(TOTAL_AMOUNT) END as AMOUNT " +
					"from PAY_ALI_RECORD where ORDER_TYPE='pay' and TRADE_STATE='SUCCESS' and REFUND_TRADE_NO is NULL " +
					"and CREATE_TIME>=? and CREATE_TIME<=? and PK_PI=? and PK_PV=?";
/*			String aliSql = "select CASE WHEN sum(TOTAL_AMOUNT) is NULL THEN 0.0 ELSE sum(TOTAL_AMOUNT) END as AMOUNT " +
					"from PAY_ALI_RECORD a inner join bl_ext_pay b on a.PK_EXTPAY = b.pk_extpay " +
					"where ORDER_TYPE='pay' and TRADE_STATE='SUCCESS' and REFUND_TRADE_NO is NULL " +
					"and b.pk_settle=?";*/
			AmountBean aliTotal = DataBaseHelper.queryForBean(aliSql, AmountBean.class, params);
			BigDecimal aliAmount = aliTotal.getAmount();
			
			//旧的银行卡缴费
			String unionSql = "select CASE WHEN sum(AMOUNT) is NULL THEN 0.0 ELSE sum(AMOUNT) END as AMOUNT " +
					"from PAY_UNIONPAY_RECORD where TRANSTYPE='31' and (LTRIM(RTRIM(ORIGINTRACENO))='' and ORIGIN_SYSREFNO is null ) " +
					"and CREATE_TIME>=? and CREATE_TIME<=? and PK_PI=? and PK_PV=?";
			/*String unionSql = "select CASE WHEN sum(a.AMOUNT) is NULL THEN 0.0 ELSE sum(a.AMOUNT) END as AMOUNT " +
					"from PAY_UNIONPAY_RECORD a inner join bl_ext_pay b on a.PK_EXTPAY = b.pk_extpay " +
					" where a.TRANSTYPE='31' " +
					"and (LTRIM(RTRIM(a.ORIGINTRACENO))='' and a.ORIGIN_SYSREFNO is null ) " +
					"and b.pk_settle = ?";*/
			AmountBean unionTotal = DataBaseHelper.queryForBean(unionSql, AmountBean.class, params);
			BigDecimal unionAmount = unionTotal.getAmount();
			
			//新的微信支付宝银行卡缴费  用衫德pos机缴的
			Object[] SandParams = new Object[]{blSettle.getPkPv(), blSettle.getPkPv()};
			String SandSql = "select CASE WHEN sum(a.AMOUNT) is NULL THEN 0.0 ELSE sum(a.AMOUNT) END as AMOUNT  "
					+ " from bl_ext_pay a inner join bl_deposit b on a.pk_depo = b.pk_depo where SERIAL_NO not in (  "
					+" select refund_no from bl_ext_pay where pk_pv = ? and flag_pay = '1' and refund_no is not null "
					+" ) and a.pk_pv = ? and a.flag_pay = '1' and refund_no is null and b.dt_bank = '1'";
			AmountBean SandTotal = DataBaseHelper.queryForBean(SandSql, AmountBean.class, SandParams);
			BigDecimal SandAmount = SandTotal.getAmount();
			
			if (wxAmount.compareTo(BigDecimal.ZERO)>0 && deposit.compareTo(BigDecimal.ZERO)>0) {
				// 多余应退款金额的直接使用-微信-退款
				deposit = insertWx(blSettle, deposit, params);
			}
			if (aliAmount.compareTo(BigDecimal.ZERO)>0 && deposit.compareTo(BigDecimal.ZERO)>0) {
				// 多余应退款金额的直接使用-支付宝-退款
				deposit = insertAli(blSettle, deposit, params);
			}
			if (unionAmount.compareTo(BigDecimal.ZERO)>0 && deposit.compareTo(BigDecimal.ZERO)>0) {
				// 多余应退款金额的直接使用-银联-退款
				BigDecimal ytAmount = deposit;
				if(unionAmount.compareTo(deposit)>0){
					deposit = BigDecimal.ZERO;
				}else{
					ytAmount = unionAmount;
					deposit = deposit.subtract(unionAmount);
				}
				insertUnion(blSettle, ytAmount, params);
			}
			if (SandAmount.compareTo(BigDecimal.ZERO)>0 && deposit.compareTo(BigDecimal.ZERO)>0) {
				// 多余应退款金额的直接使用-衫德pos机-退款
				BigDecimal ytAmount = deposit;
				if(SandAmount.compareTo(deposit)>0){
					deposit = BigDecimal.ZERO;
				}else{
					ytAmount = SandAmount;
					deposit = deposit.subtract(SandAmount);
				}
				insertSand(blSettle, ytAmount, SandParams);
			}
			if(deposit.compareTo(BigDecimal.ZERO)>0){ //这里是减去线上可退的，剩下的才转现金，我要剩余可退的都可转现金，所以这个逻辑不需要了  或者把这个现金当成剩余的，这样方便对数
				// 如果线上都没有可以退款的金额，直接记录退现金    （这个目前当做剩余金额 用于凑数）
				insertXj(blSettle, deposit);
			}
			
		}
	}

	/**
	 * 
	 * @param blSettle
	 * @param deposit
	 * @param params
	 * @param pkSettle取消结算的结算主键
	 * @return
	 */
	private BigDecimal insertWx(ZsbaBlSettle blSettle, BigDecimal deposit, Object[] params){
		String sql = "select a.* " +
				"from PAY_WECHAT_RECORD a inner join bl_ext_pay b on a.PK_EXTPAY = b.pk_extpay " +
				"where a.ORDER_TYPE='pay' and a.TRADE_STATE='SUCCESS' and a.REFUND_TRADE_NO is NULL " +
				"and a.CREATE_TIME>=? and a.CREATE_TIME<=? and a.PK_PI=? and a.PK_PV=?" +
				//"and b.pk_settle=? " +
				" order by a.TOTAL_AMOUNT desc";
		List<PayWechatRecord> wrList = DataBaseHelper.queryForList(sql, PayWechatRecord.class, params);
		BigDecimal refundAmount = new BigDecimal(0);
		for(PayWechatRecord wr : wrList){
			if(deposit.compareTo(new BigDecimal(0)) > 0){
				if(deposit.compareTo(wr.getTotalAmount()) >= 0){
					refundAmount = wr.getTotalAmount();
				}else{
					refundAmount = deposit;
				}
				deposit = deposit.subtract(refundAmount);
				
				SettleRefundRecord srr = new SettleRefundRecord();
//				if(blSettle.getPkSettleCanc()!=null){
//					srr.setPkSettle(blSettle.getPkSettleCanc());
//				}else{
//					srr.setPkSettle(blSettle.getPkSettle());
//				}
				srr.setPkSettle(blSettle.getPkSettle());
				srr.setPkPi(blSettle.getPkPi());
				srr.setPkPv(blSettle.getPkPv());
				srr.setOrderType("0");
				srr.setPkPayRecord(wr.getPkPayWechatRecord());
				srr.setOriginalCardNo(wr.getOpenid());
				srr.setOriginalTradeNo(wr.getOutTradeNo());
				srr.setOriginalAmount(wr.getTotalAmount());
				srr.setRefundAmount(refundAmount);
				srr.setRefundStatus("0");
				srr.setDelFlag("0");
				
				srr.setPkOrg(UserContext.getUser().getPkOrg());//当前操作人关联机构
				srr.setCreator(UserContext.getUser().getCodeEmp());//当前操作人关联编码
				srr.setCreateTime(new Date());
				srr.setModifier(srr.getCreator());
				srr.setModityTime(srr.getCreateTime());
				DataBaseHelper.insertBean(srr);
			}
		}
		return deposit;
	}
	
	private BigDecimal insertAli(ZsbaBlSettle blSettle, BigDecimal deposit, Object[] params){
		String sql = "select * " +
				"from PAY_ALI_RECORD " +
				"where ORDER_TYPE='pay' and TRADE_STATE='SUCCESS' and REFUND_TRADE_NO is NULL " +
				"and CREATE_TIME>=? and CREATE_TIME<=? and PK_PI=? and PK_PV=? " +
				"order by TOTAL_AMOUNT desc";
/*		String sql = "select * " +
				"from PAY_ALI_RECORD a inner join bl_ext_pay b on a.PK_EXTPAY = b.pk_extpay " +
				"where ORDER_TYPE='pay' and TRADE_STATE='SUCCESS' and REFUND_TRADE_NO is NULL " +
				"and b.pk_settle=? "+
				"order by a.TOTAL_AMOUNT desc";*/
		List<PayAliRecord> arList = DataBaseHelper.queryForList(sql, PayAliRecord.class, params);
		BigDecimal refundAmount = new BigDecimal(0);
		for(PayAliRecord ar : arList){
			if(deposit.compareTo(new BigDecimal(0)) > 0){
				if(deposit.compareTo(ar.getTotalAmount()) >= 0){
					refundAmount = ar.getTotalAmount();
				}else{
					refundAmount = deposit;
				}
				deposit = deposit.subtract(refundAmount);
				
				SettleRefundRecord srr = new SettleRefundRecord();
//				if(blSettle.getPkSettleCanc()!=null){
//					srr.setPkSettle(blSettle.getPkSettleCanc());
//				}else{
//					srr.setPkSettle(blSettle.getPkSettle());
//				}
				srr.setPkSettle(blSettle.getPkSettle());
				srr.setPkPi(blSettle.getPkPi());
				srr.setPkPv(blSettle.getPkPv());
				srr.setOrderType("1");
				srr.setPkPayRecord(ar.getPkPayAliRecord());
				srr.setOriginalCardNo(ar.getBuyerId());
				srr.setOriginalTradeNo(ar.getOutTradeNo());
				srr.setOriginalAmount(ar.getTotalAmount());
				srr.setRefundAmount(refundAmount);
				srr.setRefundStatus("0");
				srr.setDelFlag("0");

				srr.setPkOrg(UserContext.getUser().getPkOrg());//当前操作人关联机构
				srr.setCreator(UserContext.getUser().getCodeEmp());//当前操作人关联编码
				srr.setCreateTime(new Date());
				srr.setModifier(srr.getCreator());
				srr.setModityTime(srr.getCreateTime());
				DataBaseHelper.insertBean(srr);
			}
		}
		return deposit;
	}
	
	private BigDecimal insertUnion(ZsbaBlSettle blSettle, BigDecimal deposit, Object[] params){
		String sql = "select * " +
				"from PAY_UNIONPAY_RECORD " +
				"where TRANSTYPE='31' and (LTRIM(RTRIM(ORIGINTRACENO))='' and ORIGIN_SYSREFNO is null) " +
				"and CREATE_TIME>=? and CREATE_TIME<=? and PK_PI=? and PK_PV=? " +
				"order by AMOUNT desc";
		/*String sql = "select * " +
				"from PAY_UNIONPAY_RECORD a inner join bl_ext_pay b on a.PK_EXTPAY = b.pk_extpay " +
				" where a.TRANSTYPE='31' " +
				"and (LTRIM(RTRIM(a.ORIGINTRACENO))='' and a.ORIGIN_SYSREFNO is null ) " +
				"and b.pk_settle = ? "+
				"order by a.AMOUNT desc";*/
		List<PayUnionpayRecord> unionrList = DataBaseHelper.queryForList(sql, PayUnionpayRecord.class, params);
		BigDecimal ytDeposit = deposit;
		BigDecimal refundAmount = new BigDecimal(0);
		for(PayUnionpayRecord ur : unionrList){
			if(deposit.compareTo(new BigDecimal(0)) > 0){
				if(deposit.compareTo(ur.getAmount()) >= 0){
					refundAmount = ur.getAmount();
				}else{
					refundAmount = deposit;
				}
				deposit = deposit.subtract(refundAmount);
			}
			
			SettleRefundRecord srr = new SettleRefundRecord();
//			if(blSettle.getPkSettleCanc()!=null){
//				srr.setPkSettle(blSettle.getPkSettleCanc());
//			}else{
//				srr.setPkSettle(blSettle.getPkSettle());
//			}
			srr.setPkSettle(blSettle.getPkSettle());
			srr.setPkPi(blSettle.getPkPi());
			srr.setPkPv(blSettle.getPkPv());
			srr.setOrderType("2");
			srr.setPkPayRecord(ur.getPkUnionpayRecord());
			srr.setOriginalCardNo(ur.getCardno());
			srr.setOriginalTradeNo(ur.getSysrefno()+ur.getTransdate().substring(4,8));
			srr.setOriginalAmount(ur.getAmount());
			srr.setRefundAmount(new BigDecimal(0));//refundAmount //需要根据前端选择退费记录后才能只等退了哪条和具体退费金额
			srr.setYtAmount(ytDeposit);
			srr.setRefundStatus("0");
			srr.setDelFlag("0");

			srr.setPkOrg(UserContext.getUser().getPkOrg());//当前操作人关联机构
			srr.setCreator(UserContext.getUser().getCodeEmp());//当前操作人关联编码
			srr.setCreateTime(new Date());
			srr.setModifier(srr.getCreator());
			srr.setModityTime(srr.getCreateTime());
			DataBaseHelper.insertBean(srr);

		}
		return deposit;
	}

	private BigDecimal insertSand(ZsbaBlSettle blSettle, BigDecimal deposit, Object[] params){
		String sql = "select a.* "
				+ " from bl_ext_pay a inner join bl_deposit b on a.pk_depo = b.pk_depo where SERIAL_NO not in (  "
				+" select refund_no from bl_ext_pay where pk_pv = ? and flag_pay = '1' and refund_no is not null "
				+" ) and a.pk_pv = ? and a.flag_pay = '1' and refund_no is null and b.dt_bank = '1'";
		List<BlExtPay> unionrList = DataBaseHelper.queryForList(sql, BlExtPay.class, params);
		BigDecimal ytDeposit = deposit;
		BigDecimal refundAmount = new BigDecimal(0);
		for(BlExtPay ur : unionrList){
			if(deposit.compareTo(new BigDecimal(0)) > 0){
				if(deposit.compareTo(ur.getAmount()) >= 0){
					refundAmount = ur.getAmount();
				}else{
					refundAmount = deposit;
				}
				deposit = deposit.subtract(refundAmount);
			}
			
			SettleRefundRecord srr = new SettleRefundRecord();
//			if(blSettle.getPkSettleCanc()!=null){
//				srr.setPkSettle(blSettle.getPkSettleCanc());
//			}else{
//				srr.setPkSettle(blSettle.getPkSettle());
//			}
			srr.setPkSettle(blSettle.getPkSettle());
			srr.setPkPi(blSettle.getPkPi());
			srr.setPkPv(blSettle.getPkPv());
			if(ur.getDtBank() == null){//银行卡这个值会等于null
				srr.setOrderType("6");
			}
			else if(ur.getDtBank().equals("Wechat")){
				srr.setOrderType("4");
			}else if(ur.getDtBank().equals("Alipay")){
				srr.setOrderType("5");
			}else{
				srr.setOrderType("6");
			}
			
			srr.setPkPayRecord(ur.getPkExtpay());
			srr.setOriginalCardNo(ur.getCardNo());
			srr.setOriginalTradeNo(ur.getSerialNo());
			srr.setOriginalAmount(ur.getAmount());
			srr.setRefundAmount(new BigDecimal(0));//refundAmount //需要根据前端选择退费记录后才能只等退了哪条和具体退费金额
			srr.setYtAmount(ytDeposit);
			srr.setRefundStatus("0");
			srr.setDelFlag("0");

			srr.setPkOrg(UserContext.getUser().getPkOrg());//当前操作人关联机构
			srr.setCreator(UserContext.getUser().getCodeEmp());//当前操作人关联编码
			srr.setCreateTime(new Date());
			srr.setModifier(srr.getCreator());
			srr.setModityTime(srr.getCreateTime());
			DataBaseHelper.insertBean(srr);

		}
		return deposit;
	}

	
	private void insertXj(ZsbaBlSettle blSettle, BigDecimal deposit){
		SettleRefundRecord srr = new SettleRefundRecord();
/*		if(blSettle.getPkSettleCanc()!=null){
			srr.setPkSettle(blSettle.getPkSettleCanc());
		}else{
			srr.setPkSettle(blSettle.getPkSettle());
		}*/
		srr.setPkSettle(blSettle.getPkSettle());
		srr.setPkPi(blSettle.getPkPi());
		srr.setPkPv(blSettle.getPkPv());
		srr.setOrderType("3");
		srr.setPkPayRecord(null);
		srr.setRefundAmount(deposit);
		srr.setRefundStatus("0");
		srr.setDelFlag("0");
		
		String sql = " select sum(amount) amount from BL_DEPOSIT where del_flag='0' and eu_dptype='9' and eu_direct='1' and eu_pvtype='3' and dt_paymode='1' and void_type='0' and pk_pv=?";
		ZsbaBlDeposit depo = DataBaseHelper.queryForBean(sql, ZsbaBlDeposit.class, blSettle.getPkPv());
		
		srr.setOriginalAmount(depo.getAmount());
		srr.setPkOrg(UserContext.getUser().getPkOrg());//当前操作人关联机构
		srr.setCreator(UserContext.getUser().getCodeEmp());//当前操作人关联编码
		srr.setCreateTime(new Date());
		srr.setModifier(srr.getCreator());
		srr.setModityTime(srr.getCreateTime());
		DataBaseHelper.insertBean(srr);
	}
	
	
 	public void insertInvoSt(List<ZsbaBlInvoice> invos, ZsbaBlSettle stVo) {
		for(ZsbaBlInvoice invo :invos ){
			BlStInv vo = new BlStInv();
			vo.setPkInvoice(invo.getPkInvoice());
			vo.setPkOrg(invo.getPkOrg());
			vo.setPkSettle(stVo.getPkSettle());
			vo.setPkStinv(NHISUUID.getKeyId());
			setDefaultValue(vo, true);
			DataBaseHelper.insertBean(vo);
		}
	}

	public void insertInvo(List<ZsbaBlInvoice> invo, List<BlInvoiceDt> invoDt) {
		for(ZsbaBlInvoice vo:invo){
			setDefaultValue(vo, true);
		}
		for(BlInvoiceDt vo:invoDt){
			setDefaultValue(vo, true);
		}
		DataBaseHelper.batchUpdate(DataBaseHelper.getInsertSql(ZsbaBlInvoice.class), invo);
		DataBaseHelper.batchUpdate(DataBaseHelper.getInsertSql(BlInvoiceDt.class), invoDt);
	}
	
	private void setDepoInfo(ZsbaBlDeposit fromSettle, ZsbaBlSettle stVo) {
		fromSettle.setPkDepo(NHISUUID.getKeyId());
		fromSettle.setFlagSettle("1");
		fromSettle.setEuPvtype("3");
		fromSettle.setPkSettle(stVo.getPkSettle());
	}
	
	private void updDepositInfo(String pkPv,String pkSettle,Date end) {
		String sql = "update b set  b.pk_settle = ? from bl_deposit a, bl_ext_pay b where a.pk_depo = b.pk_depo and a.eu_dptype = '9' and a.flag_settle <> 1 and a.pk_pv = ? ";
		DataBaseHelper.execute(sql,pkSettle,pkPv);		
		
		sql = "update bl_deposit set flag_settle = '1', pk_settle = ? where eu_dptype = '9' and flag_settle <> 1 and pk_pv = ? ";
		DataBaseHelper.execute(sql,pkSettle,pkPv);		
	}
	
	private void updChargeInfo(String pkPv, String pkSettle, Date end, String euSttype, String pkPayer, String pkDept, String pkCgips) {
		if(!euSttype.equals("2")){
			String sql = null;
			if(pkCgips==null){
				if(pkDept==null || pkDept.trim().length()==0){
					sql = "update bl_ip_dt set flag_settle = 1, pk_settle = ? where  flag_settle <> 1 and pk_pv = ? and date_hap <= ? and del_flag = '0' and flag_settle = '0' ";
					DataBaseHelper.execute(sql,pkSettle,pkPv, end);	
				}else{
					sql = "update bl_ip_dt set flag_settle = 1, pk_settle = ? where  flag_settle <> 1 and pk_pv = ? and date_hap <= ? and del_flag = '0' and pk_dept_app = ? and flag_settle = '0' ";
					DataBaseHelper.execute(sql,pkSettle,pkPv, end, pkDept);	
				}
			}else{
				sql = "update bl_ip_dt set flag_settle = 1, pk_settle = ? where  flag_settle <> 1 and pk_pv = ? and pk_cgip in ("+pkCgips+") and del_flag = '0' and flag_settle = '0'";
				DataBaseHelper.execute(sql,pkSettle,pkPv);	
			}
		}else{
			StringBuffer sqlSb = new StringBuffer("update BL_IP_DT set flag_settle = 1, pk_settle = ? ");
			sqlSb.append(" where pk_pv = ? and flag_settle = 0 and date_hap <= ?");
			sqlSb.append(" and pk_payer=? and del_flag = '0' and flag_settle = '0' ");
			DataBaseHelper.execute(sqlSb.toString(),pkSettle,pkPv, end, pkPayer);	
		}		
	}
	
	
	
	private void settleForDeposit(ZsbaBlSettle blSettle,IUser user,String pkPv) {
		//  //4.1.1）计算可存款金额 = 患者预交金 - 患者自付金额；
		BigDecimal amountPi = blSettle.getAmountPi()==null?BigDecimal.ZERO:blSettle.getAmountPi();
		BigDecimal amountPrep = blSettle.getAmountPrep()==null?BigDecimal.ZERO:blSettle.getAmountPrep();
		BigDecimal deposit = amountPrep.subtract(amountPi);
		 //4.1.2）生成支付数据：
		BlDeposit blDepo = new BlDeposit();
		blDepo.setPkDepo(NHISUUID.getKeyId());
		blDepo.setPkPi(blSettle.getPkPi());
		blDepo.setPkPv(blSettle.getPkPv());
		blDepo.setPkDept(blSettle.getPkDeptSt());
		blDepo.setPkEmpPay(blSettle.getPkEmpSt());
		blDepo.setNameEmpPay(blSettle.getNameEmpSt());
		blDepo.setNote("余额存入患者账户");
		blDepo.setAmount(deposit);
        blDepo.setEuPvtype("3");
		blDepo.setEuDirect("-1");
		blDepo.setEuDptype("0");
		blDepo.setDtPaymode("5");
		blDepo.setFlagSettle("1");
		blDepo.setFlagAcc("0");
        blDepo.setFlagReptBack("0");
		blDepo.setDatePay(new Date());
        DataBaseHelper.insertBean(blDepo);
		//4.1.3）生成存款数据：
		BlDepositPi blDepoPi = new BlDepositPi();
		blDepoPi.setPkDepopi(NHISUUID.getKeyId());
		blDepoPi.setPkPi(blSettle.getPkPi());
		blDepoPi.setEuDirect("1");
		blDepoPi.setAmount(deposit);//存款金额
		blDepoPi.setDtPaymode("5");//（内部转账）
		blDepoPi.setDatePay(new Date());//当前日期
		blDepoPi.setPkDept(blSettle.getPkDeptSt());//当前部门
		blDepoPi.setPkEmpPay(blSettle.getPkEmpSt());//当前用户
		blDepoPi.setNameEmpPay(blSettle.getNameEmpSt());//当前用户姓名
		blDepoPi.setReptNo("");
		blDepoPi.setPkSettle(blSettle.getPkSettle());
		blDepoPi.setNote("存款结算转入");
		balAccoutService.saveMonOperation(blDepoPi,user,pkPv,null,blDepoPi.getDtPaymode());
	}

	private void settleForArrears(ZsbaBlSettle blSettle) {
		// //4.2.1计算欠款金额 = 患者自付金额 – 患者预交金。
		BigDecimal amountPi = blSettle.getAmountPi()==null?BigDecimal.ZERO:blSettle.getAmountPi();
		BigDecimal amountPrep = blSettle.getAmountPrep()==null?BigDecimal.ZERO:blSettle.getAmountPrep();
		BigDecimal deposit = amountPi.subtract(amountPrep);
	    //4.2.2生成欠费数据：
		BlSettleAr vo = new BlSettleAr();
		vo.setAmtAr(deposit.doubleValue());
		vo.setAmtPay(0.0);
		vo.setFlagCl("0");
		vo.setPkOrg(blSettle.getPkOrg());
		vo.setPkSettle(blSettle.getPkSettle());
		vo.setPkSettlear(NHISUUID.getKeyId());
		vo.setDatePay(new Date());
		vo.setPkEmpPay(blSettle.getPkEmpSt());
		vo.setNameEmpPay(blSettle.getNameEmpSt());
		DataBaseHelper.insertBean(vo);
	}
	
	
	/**
	 * 处理外部接口支付数据
	 * @param pkDepo
	 * @param blExtPay
	 */
	public void saveBlExtPay(String pkDepo, BlExtPay blExtPay){
		String sql="update BL_EXT_PAY set PK_DEPO=?,  pk_settle = ? where PK_EXTPAY=? ";
		DataBaseHelper.update( sql, new Object[]{pkDepo, blExtPay.getPkSettle(), blExtPay.getPkExtpay()});
	}
	
	/**
	 * 
	 * @param pkPv 患者就诊
	 * @param euSttype 结算类型  0出院结算，1中途结算
	 * @param dateEndTime 中途结算时间
	 * @return
	 */
	public Map<String, Object> settleData(ZsbaSettleInfo settleInfo,String pkPv, String euSttype,
			String dateBegin,String dateEnd, IUser user, String euStresult, String pkDept, Double zdbjdxAmt) {
		// 1.1查询患者就诊信息
		PvEncounter pvvo = DataBaseHelper.queryForBean("select * from pv_encounter where pk_pv = ?", PvEncounter.class, pkPv);
		Date dateEndTime = dateTrans(dateEnd);
		User userInfo = (User) user;
		ZsbaBlSettle blSettle = new ZsbaBlSettle();
		blSettle.setDateBegin(dateTrans(dateBegin));
		blSettle.setPkPv(pkPv);// 当前患者就诊主键
		// 预缴金额AmtPrep
		//String amtPrepMapSql = "select sum(depo.amount)  AmtPrep from bl_deposit depo where depo.eu_dptype=9 and depo.flag_settle = '0' and depo.pk_pv = ?  and depo.date_pay <= ?";
		String amtPrepMapSql = "select sum(depo.amount)  AmtPrep from bl_deposit depo where depo.eu_dptype=9 and depo.flag_settle = '0' and depo.pk_pv = ? ";
		Map<String, Object> amtPrepMap = DataBaseHelper.queryForMap(amtPrepMapSql, pkPv);
		BigDecimal amtPrep = amtTrans(amtPrepMap.get("amtprep"));
		blSettle.setAmountPrep(amtPrep);
		// 1.生成结算信息
		blSettle.setPkSettle(NHISUUID.getKeyId());
		//结算编码
		blSettle.setCodeSt(ApplicationUtils.getCode("0605"));
		blSettle.setPkOrg(userInfo.getPkOrg());// 当前机构
		blSettle.setPkPi(pvvo.getPkPi());// 当前患者主键
		blSettle.setEuPvtype("3");// 就诊类型
		blSettle.setPkInsurance(pvvo.getPkInsu());// 患者主医保
		// 结算类型
		if ("0".equals(euSttype)) {
			blSettle.setDtSttype("10"); // 结算类型--出院结算
			blSettle.setDateEnd(pvvo.getDateEnd());
		} else if("1".equals(euSttype)||"2".equals(euSttype)) {
			blSettle.setDtSttype("11");// 结算类型--中途结算
			blSettle.setDateEnd(dateEndTime);
		}else if("20".equals(euSttype)){
			blSettle.setDtSttype("20");// 结算冲账
			blSettle.setDateEnd(dateEndTime);
		}
		blSettle.setEuStresult(euStresult);

		blSettle.setDateSt(new Date());// 当前日期
		blSettle.setPkOrgSt(userInfo.getPkOrg());// 当前机构
		blSettle.setPkDeptSt(userInfo.getPkDept());// 当前部门
		blSettle.setPkEmpSt(userInfo.getPkEmp());// 当前用户
		blSettle.setNameEmpSt(userInfo.getNameEmp());// 当前用户姓名

		blSettle.setAmountPrep(settleInfo.getAmountPrep()); //患者预交金合计
		blSettle.setAmountSt(settleInfo.getAmountSt());//患者费用合计
		blSettle.setAmountPi(settleInfo.getAmountPi());//患者自付合计
		//blSettle.setAmountInsu(settleInfo.getAmountInsuThird()==null?BigDecimal.ZERO:settleInfo.getAmountInsuThird());//第三方医保接口返回的医保支付
		blSettle.setAmountInsu(BigDecimal.valueOf(MathUtils.sub(MathUtils.sub(settleInfo.getAmountSt().doubleValue(), settleInfo.getAmountPi().doubleValue()), settleInfo.getAmountDisc().doubleValue())));
		blSettle.setAmountDisc(Double.parseDouble(settleInfo.getAmountDisc().toString()));
		BigDecimal amtAcc = settleInfo.getAmountAcc();
		
		if(amtAcc!=null && amtAcc.compareTo(BigDecimal.ZERO)>0){
//			patiAccountChange(amtAcc,pvvo);//调用患者账户服务
		}

		if (!"2".equals(euSttype)) {
			//计算特诊总费用
			Double amountAdd = zsbaBlIpPubMapper.qryAmountAddByPv(settleInfo.getPkCgips(), pkPv, dateTrans(dateBegin), dateEndTime, pkDept);
			blSettle.setAmountAdd(amountAdd);
		}else{
			//科研不计算特诊总费用
			blSettle.setAmountAdd(0d);
		}
		blSettle.setFlagCanc("0");
		blSettle.setReasonCanc(null);
		blSettle.setPkSettleCanc(null);
		blSettle.setFlagArclare("0");
		blSettle.setFlagCc("0");
		blSettle.setPkCc(null);
		
		Map<String, Object> resMap = new HashMap<String, Object>();
		
		// 2.生成结算明细 并处理相关金额赋值
		if (!"2".equals(euSttype)) {
			Double ybzf = settleInfo.getAmountInsuThird().doubleValue();
			Double gz = 0.0;
			if(settleInfo.getInsStQgyb()!=null){
				gz = settleInfo.getInsStQgyb().getAcctPay().doubleValue();
				ybzf = ybzf-gz;
			}
			String cgips = null;
			if(settleInfo.getPkCgipsStr()!=null){
				cgips = "";
				String[] cgipsArgs = settleInfo.getPkCgipsStr().split(",");
				int i = 0;
				for (String pkcgip : cgipsArgs) {
					if (i == 0) {
						cgips += "'" + pkcgip.trim() + "'";
					} else {
						cgips += ",'" + pkcgip.trim() + "'";
					}
					i++;
				}
			}
			
			
			List<BlSettleDetailBa> detailVos = handleSettleDetailC(settleInfo, pkPv, blSettle, userInfo.getPkOrg(),euSttype,dateBegin,dateEnd, ybzf, 
					Double.parseDouble(settleInfo.getAmountDisc().toString()), gz, pkDept, cgips, zdbjdxAmt);
			resMap.put("detail", detailVos);
		}else{
			List<BlSettleDetail> detailVos = handleSettleDetailCKy(pkPv, blSettle, settleInfo.getPkPayer());
			resMap.put("detail", detailVos);
		}
		resMap.put("settle", blSettle);
		
		return resMap;
	}
	
	/**
	 * 
	 * @param settleInfo 客户端传过来的参数   预结算这个传的是null
	 * @param PkPv
	 * @param blSettle
	 * @param pkOrg
	 * @param euSttype
	 * @param begin
	 * @param end
	 * @param insAmount 医保支付   旧医保系统这个是统筹+个账   新医保系统这个只是统筹
	 * @param amountDisc
	 * @param gzAmount  个账 新医保系统用的，旧医保系统这个固定为0
	 * @param pkDept  科室主键，分科结算用的
	 * @param pkCgips 明细主键,以逗号分隔
	 * @return
	 */
	private List<BlSettleDetailBa> handleSettleDetailC(ZsbaSettleInfo settleInfo, String PkPv, ZsbaBlSettle blSettle, String pkOrg,
			String euSttype, String begin, String end, Double insAmount, Double amountDisc, Double gzAmount, String pkDept, String pkCgips,
			Double zdbjdxAmt) {
		Map<String, BlSettleDetailBa> res = new HashMap<String, BlSettleDetailBa>();
		// 1.查询记费表的数据，组织记费阶段的数据
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("pkPv", PkPv);
		params.put("euSttype", euSttype);
		params.put("pkDept", pkDept);
		params.put("dateBegin", begin);
		params.put("dateEnd", end);
		
		if(pkCgips!=null){
			params.put("pkCgips", pkCgips);
		}
		
		StringBuffer sql = new StringBuffer("select * from bl_ip_dt  where pk_pv = ?");
		if(begin!=null){
			sql.append(" and date_hap >=  to_date(substr("+begin+" ,1,8)+'000000', 'yyyymmddhh24miss')");
		}
		if(end!=null){
			sql.append(" and date_hap <=  to_date(substr("+end+", 1, 8)+'235959', 'yyyymmddhh24miss')");
		}
		if(euSttype!=null && !euSttype.equals("23")){
			sql.append(" and flag_settle=0 ");
		}
		if(pkDept!=null){
			sql.append(" and pk_dept_app="+pkDept+" ");
		}
		if(pkCgips!=null){
			sql.append(" and pk_cgip in ("+pkCgips+") ");
		}
		List<BlIpDt> cgVos = DataBaseHelper.queryForList(sql.toString(), BlIpDt.class, PkPv);
		
		//使用下面这个语句，如果有in语法，会很慢
		//List<BlIpDt> cgVos = zsbaBlIpPubMapper.qryCgIps(params);
		
		Double amtDisc = 0.0;
		Double amtHp = 0.0;
		Double patiSelf = 0.0;
		Double amtAdd = 0.0;
		for (BlIpDt vo : cgVos) {
			Double amtHpTemp = 0.0;
			//加收金额的自费比例是1.0   不是加收金额的加收比例为0.0
			if (1.0 == vo.getRatioSelf()) { 
				Double price = MathUtils.sub(vo.getPriceOrg(), vo.getPrice());
				amtHpTemp = MathUtils.mul(price, vo.getQuan());
				//amtHpTemp = MathUtils.mul(vo.getPrice(), vo.getQuan());
			} else {
				amtHpTemp = MathUtils.mul(MathUtils.mul(vo.getQuan(), vo.getPrice()), vo.getRatioSelf());
			}
			amtHp = MathUtils.add(amtHp.doubleValue(), amtHpTemp);
			//优惠金额要减去加收的  
			amtDisc = MathUtils.add(amtDisc, MathUtils.sub(MathUtils.sub(MathUtils.sub(vo.getAmount(), vo.getAmountPi()), amtHpTemp), vo.getAmountAdd()==null?0f:vo.getAmountAdd()));
			patiSelf = MathUtils.add(patiSelf, vo.getAmountPi());
			amtAdd = MathUtils.add(amtAdd, vo.getAmountAdd()==null?0f:vo.getAmountAdd());
		}

		PvEncounter pvvo = DataBaseHelper.queryForBean("Select * from pv_encounter where pk_pv = ?", PvEncounter.class, PkPv);
		List<BlSettleDetailBa> resvo = new ArrayList<BlSettleDetailBa>();

		// 2.1处理患者优惠的记费明细数据
		if (amtDisc > 0) {
			BlSettleDetailBa voDisc = new BlSettleDetailBa();
			voDisc.setAmount(amtDisc.doubleValue());
			StringBuffer sqlCate = new StringBuffer(" select picate.pk_hp, bh.pk_payer, bh.eu_hptype from pi_cate picate");
			sqlCate.append(" inner join bd_hp bh on bh.pk_hp = picate.pk_hp ");
			sqlCate.append(" where picate.pk_picate = ?");
			List<Map<String, Object>> discHpMap = DataBaseHelper.queryForList(sqlCate.toString(), pvvo.getPkPicate());
			String pkPayer = "";
			String pkHp = "";
			if (discHpMap != null && discHpMap.size() > 0) {
				Map<String, Object> tempDisc = discHpMap.get(0);
				if (tempDisc.get("pkPayer") != null) {
					pkPayer = (String) tempDisc.get("pkPayer");
				}
				if (tempDisc.get("pkHp") != null) {
					pkHp = (String) tempDisc.get("pkHp");
				}
			}
			voDisc.setPkSettle(blSettle.getPkSettle());
			voDisc.setPkPayer(pkPayer);
			voDisc.setPkInsurance(pkHp);
			res.put(pkHp, voDisc);
		}
		List<Map<String, Object>> hpPlans = getHpPlans(PkPv);// 1.获取患者的医保列表
		if (hpPlans == null || hpPlans.size() <= 0) {
			resvo.addAll(res.values());
			return resvo;
		}
		Map<String, Object> majorHp = null;
		for (Map<String, Object> map : hpPlans) {
			String flagMaj = "";
			if (map.get("flagMaj") != null) {
				flagMaj = (String) map.get("flagMaj");
			}
			if ("1".equals(flagMaj)) {
				majorHp = map;
				break;
			}
		}
		
		// 2.2处理内部医保的记费明细数据
		if (amtHp > 0 && majorHp != null) {
			BlSettleDetailBa voHp = new BlSettleDetailBa();
			voHp.setAmount(amtHp.doubleValue());
			voHp.setPkSettle(blSettle.getPkSettle());
			String pkPayer = (majorHp != null && majorHp.get("pkPayer") != null) ? (String) majorHp.get("pkPayer") : "";
			String pkHp = (majorHp != null && majorHp.get("pkHp") != null) ? (String) majorHp.get("pkHp") : "";
			voHp.setPkPayer(pkPayer);
			voHp.setPkInsurance(pkHp);
			res.put(pkHp, voHp);
		}

		// 3.循环处理内部医保的结算策略和<=3的各类型医保
		for (Map<String, Object> map : hpPlans) {
			// 3.1数据准备
			String pkPayer = (map != null && map.get("pkPayer") != null) ? (String) map.get("pkPayer") : "";
			String pkHp = (map != null && map.get("pkHp") != null) ? (String) map.get("pkHp") : "";

			int EuHptype = -1;
			if (map.get("euHptype") != null) {
				EuHptype = Integer.parseInt((String) map.get("euHptype"));
			}

			// 3.2处理结算策略的各类数据
			if (EuHptype > 3) {// 该情况下可出现4,9
				BigDecimal amountInnerD = priceStrategyService.qryPatiSettlementAmountAllocationInfo(new BigDecimal(patiSelf), pvvo.getPkOrg(), Constant.IP, pkHp);
				if (patiSelf > amountInnerD.doubleValue()) {
					// 返回的自付金额要是比当前的小，则是有分摊的，故写入一些明细记录
					BlSettleDetailBa voInner = null;
					Double hpTemp = MathUtils.sub(patiSelf, amountInnerD.doubleValue());
					if (res.get(pkHp) != null) {
						voInner = res.get(pkHp);
						voInner.setAmount(MathUtils.add(voInner.getAmount(), hpTemp));
					} else {
						voInner = new BlSettleDetailBa();
						voInner.setAmount(hpTemp);
						voInner.setPkSettle(blSettle.getPkSettle());
						voInner.setPkInsurance(pkHp);
						voInner.setPkPayer(pkPayer);
					}
					res.put(pkHp, voInner);
					patiSelf = amountInnerD.doubleValue();
				}
			} else {
				Map<String, Object> hpMap = hpService.getHpInfo(patiSelf, map);
				BigDecimal amtHp3 = hpMap.get("amtHps") == null ? BigDecimal.ZERO : (BigDecimal) hpMap.get("amtHps");
				if (amtHp3.compareTo(BigDecimal.ZERO) > 0) {
					BlSettleDetailBa voHp3 = null;
					if (res.get(pkHp) != null) {
						voHp3 = res.get(pkHp);
						voHp3.setAmount(MathUtils.add(amtHp3.doubleValue(), voHp3.getAmount()));
						// 给主表赋值
						blSettle.setAmountInsu(amtHp3.add(blSettle.getAmountInsu()));
					} else {
						voHp3 = new BlSettleDetailBa();
						voHp3.setAmount(amtHp3.doubleValue());
						voHp3.setPkPayer(pkPayer);
						voHp3.setPkSettle(blSettle.getPkSettle());
						voHp3.setPkInsurance(pkHp);
						// 给主表赋值
						blSettle.setAmountInsu(amtHp3);
					}
					res.put(pkHp, voHp3);
					patiSelf = MathUtils.sub(patiSelf, amtHp3.doubleValue());
				}
			}
		}

		
		//外部医保
		if(insAmount>0){
			BlSettleDetailBa voIns = new BlSettleDetailBa();
			voIns.setAmount(insAmount);
			voIns.setPkSettle(blSettle.getPkSettle());
			Map<String,Object> map = DataBaseHelper.queryForMap("select b.pk_payer from bd_hp a inner join bd_payer b on a.pk_payer = b.pk_payer where pk_hp = ? ", pvvo.getPkInsu());
			voIns.setPkPayer(map.get("pkPayer").toString());
			voIns.setPkInsurance(pvvo.getPkInsu());
			resvo.add(voIns);
		}
		//个账 全国医保才需要加这个
		if(gzAmount>0){
			BlSettleDetailBa voIns = new BlSettleDetailBa();
			voIns.setAmount(gzAmount);
			voIns.setPkSettle(blSettle.getPkSettle());
			Map<String,Object> map = DataBaseHelper.queryForMap("select pk_payer from bd_payer where code = ? ", "09"); //09：个账
			voIns.setPkPayer(map.get("pkPayer").toString());
			//voIns.setPkInsurance(pvvo.getPkInsu());
			resvo.add(voIns);
		}
		//结算弹窗中手动输入的优惠金额(有时护士不小心弄坏的病人的药，结算的时候，会通知收费处手动输入对应的金额，作为医院优惠，免去弄坏的药品的费用)
		if(amountDisc>0){
			BlSettleDetailBa voDisc = new BlSettleDetailBa();
			voDisc.setAmount(amountDisc);
			voDisc.setPkSettle(blSettle.getPkSettle());
			Map<String,Object> map = DataBaseHelper.queryForMap("select pk_payer from bd_payer where code = ? ", "04"); //04：医院优惠
			voDisc.setPkPayer(map.get("pkPayer").toString());
			//voDisc.setPkInsurance(pvvo.getPkInsu());
			resvo.add(voDisc);
		}
		
		Double jsAmt = 0d;
		//计划生育
		if(hpPlans.get(0).get("code").equals("1000")){
			//旧的计划生育结算逻辑
/*			String jsSql = "select sum(amount) as amount from bl_ip_dt a inner join BD_INVCATE_ITEM b on a.code_bill = b.code where pk_pv = ? and b.code not in ('J1', 'J2', 'J3')";
			Map<String,Object> jsMap = DataBaseHelper.queryForMap(jsSql, PkPv); //计划生育需要记账的金额
			BlSettleDetailBa voIns = new BlSettleDetailBa();
			jsAmt = Double.parseDouble(jsMap.get("amount").toString());
			voIns.setAmount(jsAmt);
			voIns.setPkSettle(blSettle.getPkSettle());
			voIns.setPkPayer(hpPlans.get(0).get("pkPayer").toString());
			voIns.setPkInsurance(pvvo.getPkInsu());
			resvo.add(voIns);*/
			
			//新的计划生育结算逻辑 2021-02-28
			if(settleInfo!=null && settleInfo.getDtFayplgitem()!=null){
				String jsSql = "select * from bd_defdoc where code_defdoclist = 'BA004' and code = ?";
				Map<String,Object> jsMap = DataBaseHelper.queryForMap(jsSql, settleInfo.getDtFayplgitem()); //计划生育需要记账的金额
				BlSettleDetailBa voIns = new BlSettleDetailBa();
				jsAmt = Double.parseDouble(jsMap.get("valAttr").toString());
				voIns.setAmount(jsAmt);
				voIns.setPkSettle(blSettle.getPkSettle());
				voIns.setPkPayer(hpPlans.get(0).get("pkPayer").toString());
				voIns.setPkInsurance(pvvo.getPkInsu());
				resvo.add(voIns);
			}
		}else if(hpPlans.get(0).get("code").equals("2000")){
			//儿童福利院(全免)
			BlSettleDetailBa voIns = new BlSettleDetailBa();
			jsAmt = patiSelf.doubleValue();
			voIns.setAmount(jsAmt);
			voIns.setPkSettle(blSettle.getPkSettle());
			voIns.setPkPayer(hpPlans.get(0).get("pkPayer").toString());
			voIns.setPkInsurance(pvvo.getPkInsu());
			resvo.add(voIns);
		}
		
		
		//重点保健对象
		if(zdbjdxAmt>0){
			BdPayer bjPayer = DataBaseHelper.queryForBean("select * from bd_payer where code = '14' and del_flag = '0'", BdPayer.class);
			BlSettleDetailBa voIns = new BlSettleDetailBa();
			voIns.setAmount(zdbjdxAmt);
			voIns.setPkSettle(blSettle.getPkSettle());
			voIns.setPkPayer(bjPayer.getPkPayer());
			voIns.setPkInsurance(pvvo.getPkInsu());
			resvo.add(voIns);
		}
		
		// 4.结算时患者自付金额。
		BlSettleDetailBa voSelf = new BlSettleDetailBa();
		voSelf.setAmount(patiSelf.doubleValue()-insAmount-amountDisc - jsAmt - gzAmount - zdbjdxAmt);
		voSelf.setAmountAdd(amtAdd.doubleValue());
		voSelf.setPkSettle(blSettle.getPkSettle());
		voSelf.setPkInsurance(null);
		// 当保险计划为空时，付款方应该也为空
		voSelf.setPkPayer(qryBdPayerByEuType(pkOrg));
		res.put("pkSelf", voSelf);

		// 5.给主表赋值
		// blSettle.setAmountPi(new BigDecimal(patiSelf).setScale(2,
		// BigDecimal.ROUND_HALF_UP));//不保留两位小数，保存的时候会报错
		resvo.addAll(res.values());

		
		
		return resvo;
	}
	
	/**
	 * 生成结算明细（科研）
	 * @param PkPv
	 * @param blSettle
	 * @param pkOrg
	 * @param euSttype
	 * @param begin
	 * @param end
	 * @return
	 */
	private List<BlSettleDetail> handleSettleDetailCKy(String PkPv, ZsbaBlSettle blSettle, String pkPayer){
		List<BlSettleDetail> resvo = new ArrayList<BlSettleDetail>();
		//优惠付款方和金额查询
		StringBuffer sqlSb = new StringBuffer(" Select dt.PK_PAYER, sum(dt.AMOUNT) amtst, sum(dt.AMOUNT - dt.AMOUNT_PI)  amtinsu, sum(dt.AMOUNT_PI)  amtpi ");
		sqlSb.append(" from BL_IP_DT dt ");
		sqlSb.append(" where dt.FLAG_SETTLE='0' and dt.pk_pv =? and dt.pk_payer = ? ");
		sqlSb.append(" group by dt.PK_PAYER ");
		List<Map<String, Object>> yhMap = DataBaseHelper.queryForList(sqlSb.toString(), PkPv, pkPayer);
		Map<String, Object> map = yhMap.get(0);
		BlSettleDetail bdYh = new BlSettleDetail();
		bdYh.setPkSettle(blSettle.getPkSettle());
		bdYh.setPkPayer(map.get("pkPayer").toString());
		bdYh.setAmount(Double.parseDouble(map.get("amtinsu").toString()));
		bdYh.setPkInsurance(null);
		resvo.add(bdYh);
		
		//个人自付付款方和金额查询
		sqlSb = new StringBuffer("select * from BD_PAYER where EU_TYPE =0 and DEL_FLAG ='0' and code = '01'");
		BdPayer bp = DataBaseHelper.queryForBean(sqlSb.toString(), BdPayer.class, null);

		BlSettleDetail bd = new BlSettleDetail();
		bd.setPkSettle(blSettle.getPkSettle());
		bd.setPkPayer(bp.getPkPayer());
		bd.setAmount(Double.parseDouble(map.get("amtpi").toString()));
		bd.setPkInsurance(null);
		resvo.add(bd);
		return resvo;
	}
	
	private void patiAccountChange(BigDecimal amtAcc, PvEncounter pvvo) {
		BlDeposit dp = new BlDeposit();
		dp.setPkPi(pvvo.getPkPi());
		dp.setEuDirect("-1");
		dp.setAmount(amtAcc.multiply(new BigDecimal(-1)));
		dp.setDtPaymode("4");
		dp.setPkEmpPay(UserContext.getUser().getPkEmp());
		dp.setNameEmpPay(UserContext.getUser().getNameEmp());
		piAccDetailVal(dp);
	}

	public static void piAccDetailVal(BlDeposit dp) {
		String getPiA = "Select * from pi_acc where pk_pi=? and (del_flag='0' or del_flag is null)";
		PiAcc pa = DataBaseHelper.queryForBean(getPiA, PiAcc.class, dp.getPkPi());
		if (pa != null && EnumerateParameter.ONE.equals(pa.getEuStatus())) {
			if (pa.getAmtAcc() == null || "".equals(pa.getAmtAcc())) {
				pa.setAmtAcc(BigDecimal.ZERO);
			}
			BigDecimal amtAcc = pa.getAmtAcc().add(dp.getAmount());
			if (amtAcc.compareTo(BigDecimal.ZERO) < 0) {
				throw new BusException("信用额度小于0！");
			} else {
				pa.setAmtAcc(amtAcc);
			}
			DataBaseHelper.updateBeanByPk(pa, false);
			PiAccDetail pad = new PiAccDetail();
			pad.setPkPiacc(pa.getPkPiacc());
			pad.setPkPi(pa.getPkPi());
			pad.setDateHap(new Date());
			pad.setEuOptype(EnumerateParameter.ONE);
			pad.setEuDirect(dp.getEuDirect());
			pad.setAmount(dp.getAmount());
			pad.setPkDepopi(null);
			pad.setAmtBalance(pa.getAmtAcc());
			pad.setPkEmpOpera(dp.getPkEmpPay());
			pad.setNameEmpOpera(dp.getNameEmpPay());
			DataBaseHelper.insertBean(pad);
		} else {
			throw new BusException("该账户已冻结或已被删除，不可收退款");
		}

	}
	
	private String qryBdPayerByEuType(String pkOrg) {
		String pkPayer = "";
		Map<String,Object> map = DataBaseHelper.queryForMap("select pk_payer from bd_payer where eu_type='0' and code = '01'");
		if(map != null){
			pkPayer = (String)map.get("pkPayer");
		}
		return pkPayer;
	}
	
	private String qryBdPayerByCode(String code) {
		String pkPayer = "";
		Map<String,Object> map = DataBaseHelper.queryForMap("select pk_payer from bd_payer where code=? ", code);
		if(map != null){
			pkPayer = (String)map.get("pkPayer");
		}
		return pkPayer;
	}
	
	private List<Map<String,Object>> getHpPlans(String PkPv){
		 List<Map<String,Object>> res = new ArrayList<Map<String,Object>>();
		//1.pv_insurance表里的医保
		StringBuffer sql = new StringBuffer("select bh.pk_payer,insu.sort_no,bh.eu_hptype,insu.pk_hp,insu.flag_maj, bh.code ");
		sql.append(" from pv_insurance insu");
		sql.append(" inner join  bd_hp bh");
		sql.append(" on insu.pk_hp = bh.pk_hp ");
		sql.append("  where insu.pk_pv = ?");
		sql.append(" order by insu.sort_no asc,insu.flag_maj desc");
		res.addAll(DataBaseHelper.queryForList(sql.toString(), PkPv));
       return res;
	}

	/**
	 * 判断患者是否有过中途结算，如果有，返回上次的结算日期
	 * @param PkPv
	 * @return
	 */
	private Date checkMidSettle(String PkPv){
		String mapSql = "select count(1) amt from bl_settle where dt_sttype = 11 and flag_canc = 0 and pk_pv = ? and pk_settle not in (select pk_settle_canc from bl_settle where pk_pv = ?)";
		Map<String,Object> map = DataBaseHelper.queryForMap(mapSql, PkPv,PkPv);
		int cnt = 0;
		if(map.get("amt") instanceof BigDecimal){
			BigDecimal amt = amtTrans(map.get("amt"));
			cnt = amt.intValue();
		}else{
			 cnt = map.get("amt")==null?0:(Integer)map.get("amt");
		}
	
		if(cnt> 0){
			String dateSql = "select date_end dateEnd from bl_settle where  dt_sttype = 11 and flag_canc = 0 and  pk_pv = ? order by  date_end desc";
			Map<String,Object> dateInfo = DataBaseHelper.queryForMap(dateSql, PkPv);
			return (Date)dateInfo.get("dateend");
		}
		return null;
	}
	private BigDecimal amtTrans(Object amt) {
		if(amt == null){
			return BigDecimal.ZERO;
		}else{
			return (BigDecimal)amt;
		}
	}

	public String dateTrans(Date date) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
		String temp = null;
		if(date!=null){
			temp = sdf.format(date);
		}
		return temp;
	}
	public Date dateTrans(String date) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
		Date temp = null;
		if(StringUtils.hasText(date)){
			try {
				temp = sdf.parse(date);
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}
		return temp;
	}
	public String dateTrans(Date date, String dateFmt) {
		SimpleDateFormat sdf = new SimpleDateFormat(dateFmt);
		return sdf.format(date);
	}

	public static void setDefaultValue(Object obj, boolean flag) {
		User user = UserContext.getUser();
		Map<String, Object> default_v = new HashMap<String, Object>();
		if (flag) { // 如果新增
			default_v.put("pkOrg", user.getPkOrg());
			default_v.put("creator", user.getPkEmp());
			default_v.put("createTime", new Date());
			default_v.put("delFlag", "0");
		}
		default_v.put("ts", new Date());
		default_v.put("modifier", user.getPkEmp());
		
		Set<String> keys = default_v.keySet();
		for (String key : keys) {
			Field field = ReflectHelper.getTargetField(obj.getClass(), key);
			if (field != null) {
				ReflectHelper.setFieldValue(obj, key, default_v.get(key));
			}
		}
	}

	/**
	 * 获取患者列表 PkOrg EuSttype
	 * @param param
	 * @param user
	 */
	public List<Map<String, Object>> QryPatiList(String param, IUser user) {

		@SuppressWarnings("unchecked")
		Map<String, Object> paraMap = JsonUtil.readValue(param, Map.class);
		String euSttype = (String) paraMap.get("euSttype");
		String pkOrg = (String) paraMap.get("pkOrg");
		String codeDept = (String) paraMap.get("codeDept");
		String typeDept = (String) paraMap.get("typeDept");

		StringBuffer sql = new StringBuffer(
				"select pv.pk_pv, pi.pk_pi,pv.code_pv,pi.code_ip, pi.name_pi,dept.name_dept,ip.flag_frozen,ip.dt_stway,ip.dt_sttype_ins,pv.eu_status");
		sql.append(" from pv_encounter pv");
		sql.append(" inner join pv_ip ip on pv.pk_pv=ip.pk_pv");
		sql.append(" inner join pi_master pi on pv.pk_pi=pi.pk_pi");
		sql.append(" inner join bd_ou_dept dept on pv.pk_dept=dept.pk_dept");
		sql.append(" where dept.pk_org = ? ");
		//护士站增加护士站查询条件
		if("02".equals(typeDept)){
			sql.append(" and pv.pk_dept_ns=?");
		}
		
		if ("0".equals(euSttype)) {// 出院结算
			sql.append(" and pv.eu_status = 2 ");
			// 读取系统参数BL0009，如果参数值=1，列表显示就诊为“结束”状态，且已经预结算的患者列表；如果参数值=0，列表显示就诊为“结束”的患者列表；
			String type = ApplicationUtils.getSysparam("BL0009", false);
			if ("1".equals(type)) {
				sql.append(" and ip.flag_prest = 1");
			}
		} else{// 中途结算 当前患者列表为“就诊”状态；
			sql.append(" and pv.eu_status in(1,2) ");
			sql.append(" and exists (select 1 from bl_ip_dt dt where dt.pk_pv = pv.pk_pv and flag_settle = '0') ");
		}
//		}else{//科研结算
//			sql.append(" and pv.eu_status in(1,2) and (");
//			String type = ApplicationUtils.getSysparam("BL0009", false);
//			if ("1".equals(type)) {
//				sql.append(" ip.flag_prest = 1 or");
//			}
//			sql.append(" exists (select 1 from bl_ip_dt dt where dt.pk_pv = pv.pk_pv and flag_settle = '0')) ");
//		}
		//新增线上结算查询条件
		if(paraMap.get("isAtlSt")!=null && !CommonUtils.isEmptyString(CommonUtils.getString(paraMap.get("isAtlSt")))){
			if(CommonUtils.getString(paraMap.get("isAtlSt")).equals("0")){
				sql.append(" and (ip.dt_stway<>1  or ip.dt_stway is null or ip.dt_stway='') ");
			}else if(CommonUtils.getString(paraMap.get("isAtlSt")).equals("1")){
				sql.append(" and (ip.dt_stway<>0 or ip.dt_stway is null or ip.dt_stway='') ");
			}
		}
		
		List<Map<String, Object>> res = new ArrayList<Map<String,Object>>();
		if("02".equals(typeDept)){
			res = DataBaseHelper.queryForList(sql.toString(), pkOrg, UserContext.getUser().getPkDept());
		}else if(typeDept!=null&&"08".equals(typeDept.substring(0, 2))){
			res = DataBaseHelper.queryForList(sql.toString(), pkOrg);
		}
		return res;
	}
	
	/**
	 * 发票补打
	 * @param param
	 * @param user
	 */
	@SuppressWarnings("unchecked")
	public ZsbaBlInvPrint stInvMakeUpPrint(String param, IUser user) {
		BlInvPrint res = new BlInvPrint();
		Map<String,Object> paraMap = JsonUtil.readValue( param, Map.class); 
		String pkSettle = (String)paraMap.get("pkSettle");
		List<String> codeInv = (List<String>) paraMap.get("codeInv");
		String pkEmpinv = (String)paraMap.get("pkEmpinv");
		String pkInvcate = (String)paraMap.get("pkInvcate");
		ZsbaBlInvPrint old = getPrintDataByPkSettle(pkSettle);
		
		//发送重打发票消息
//		Map<String,Object> paramMap = JsonUtil.readValue( param, Map.class); 
//		paramMap.put("fpbz", "2");//重打发票
//		List<String> codeInvList = new ArrayList();
//		for(BlInvoice inv : old.getBlInvoice()){
//			codeInvList.add(inv.getCodeInv());
//		}
//		paramMap.put("codeInvList", codeInvList);
//		PlatFormSendUtils.sendReceiptMsg(paramMap);	
		for(int i=0; i<old.getBlInvoice().size(); i++){
			ZsbaBlInvoice inv = old.getBlInvoice().get(i);
			//将发票号写入发票表
			DataBaseHelper.execute("update bl_invoice  set pk_invcate = ?,pk_empinvoice = ?,"
					+ "  code_inv = ?"
					+ " where pk_invoice = ?", pkInvcate,pkEmpinv, codeInv.get(i), inv.getPkInvoice());
		}
		
//		调用发票使用确认服务，完成发票更新。
		try{
			commonService.confirmUseEmpInv(pkEmpinv, new Long(old.getBlInvoice().size()));
		}catch(Exception e){
			throw new BusException("发票更新失败，请确认票据是否充足！");
		}
		return getPrintDataByPkSettle(pkSettle);
	}

	private ZsbaBlInvPrint getPrintDataByPkSettle(String pkSettle) {
		ZsbaBlInvPrint res = new ZsbaBlInvPrint();
		if(!CommonUtils.isEmptyString(pkSettle)){
			ZsbaBlSettle st = DataBaseHelper.queryForBean("SELECT * FROM bl_settle where pk_settle = ?", ZsbaBlSettle.class, pkSettle);
			List<BlSettleDetail> blSettleDetail = DataBaseHelper.queryForList(" SELECT * FROM Bl_Settle_Detail WHERE pk_settle = ?", BlSettleDetail.class, pkSettle);
			List<BlStInv> stInvs = DataBaseHelper.queryForList("SELECT * FROM bl_st_inv where pk_settle = ?", BlStInv.class, pkSettle);
			List<BlDeposit> blDepositList =  DataBaseHelper.queryForList("SELECT * FROM bl_deposit WHERE pk_settle = ? and (eu_dptype = '0' or eu_dptype = '1')", BlDeposit.class, pkSettle);
			List<ZsbaBlInvoice> inv = new ArrayList<ZsbaBlInvoice>();
			for(BlStInv stInv : stInvs){
					ZsbaBlInvoice invvo = DataBaseHelper.queryForBean(" SELECT * FROM bl_invoice WHERE flag_cancel= 0 and pk_invoice = ?", ZsbaBlInvoice.class, stInv.getPkInvoice());
					if(invvo !=null && !CommonUtils.isEmptyString(invvo.getPkInvoice())){
						inv.add(invvo);
						List<BlInvoiceDt> invDt = DataBaseHelper.queryForList(" SELECT * FROM bl_invoice_dt WHERE pk_invoice = ?", BlInvoiceDt.class, invvo.getPkInvoice());
						invvo.setInvDt(invDt);	
					}
				}

			res.setBlInvoice(inv);
			res.setBlSettle(st);
			res.setBlDepositList(blDepositList);
			res.setBlSettleDetail(blSettleDetail);
		}
		return res;
	}
	
	/**
	 * 住院收费--住院结算 重打
	 * @param param
	 * @param user
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public ZsbaBlInvPrint stInvReprint(String param,IUser user){
		
		ZsbaBlInvPrint res = new ZsbaBlInvPrint();
		Map<String,Object> paraMap = JsonUtil.readValue( param, Map.class); 
		String pkSettle = (String)paraMap.get("pkSettle");
		List<String> codeInv = (List<String>) paraMap.get("codeInv");
		String pkEmpinv = (String)paraMap.get("pkEmpinv");
		ZsbaBlInvPrint old = getPrintDataByPkSettle(pkSettle);
		
		Map<String,Object> rePrintMap = new HashMap<>();//重打发票个性化服务入参
//		调用发票查询服务，判断是否有足够的发票张数，如果不够，提示用户“发票剩余数量不足，请设置!”；
//		作废原发票号码；
		for(ZsbaBlInvoice inv : old.getBlInvoice()){
			DataBaseHelper.execute("update bl_invoice  set flag_cancel = 1,date_cancel = ?,"
					+ "  pk_emp_cancel = ?,name_emp_cancel = ?"
					+ " where code_inv = ?", new Date(),UserContext.getUser().getPkEmp(),
					UserContext.getUser().getNameEmp(),inv.getCodeInv());
		}

		List<ZsbaBlInvoice> newInvs = old.getBlInvoice();
		List<BlInvoiceDt> newInvDts = new ArrayList<BlInvoiceDt>();
		List<BlStInv> stInvs =  new ArrayList<BlStInv>();
		for(int i = 0;i<newInvs.size();i++){
			ZsbaBlInvoice newVo = newInvs.get(i);
			String oldPk = newVo.getPkInvoice();
			newVo.setPkEmpInv(UserContext.getUser().getPkEmp());
			newVo.setPkEmpinvoice(pkEmpinv);
			newVo.setNameEmpInv(UserContext.getUser().getNameEmp());
			if(codeInv!=null){
				newVo.setCodeInv(codeInv.get(i));
			}
			newVo.setDateInv(new Date());
			newVo.setPrintTimes(0);
			newVo.setFlagCancel("0");
			newVo.setFlagCc("0");
			newVo.setFlagReprint("1");
			ApplicationUtils.setDefaultValue(newVo, true);
			rePrintMap.put(oldPk, newVo);//灵璧个性化重打发票服务入参
			
			for(BlInvoiceDt dt : newVo.getInvDt()){
				dt.setPkInvoice(newVo.getPkInvoice());
				ApplicationUtils.setDefaultValue(dt, true);
				newInvDts.add(dt);
			}
			
			BlStInv stInv = new BlStInv();
			stInv.setPkInvoice(newVo.getPkInvoice());
			stInv.setPkSettle(pkSettle);
			stInv.setPkOrg(UserContext.getUser().getPkOrg());
			ApplicationUtils.setDefaultValue(stInv, true);
			stInvs.add(stInv);
		}
//		写发票表bl_invoice；
		DataBaseHelper.batchUpdate(DataBaseHelper.getInsertSql(ZsbaBlInvoice.class), newInvs);
//		写发票明细表bl_invoice_dt；
		DataBaseHelper.batchUpdate(DataBaseHelper.getInsertSql(BlInvoiceDt.class), newInvDts);
//		写发票结算关联表bl_st_inv；
		DataBaseHelper.batchUpdate(DataBaseHelper.getInsertSql(BlStInv.class), stInvs);
		
		//重打发票个性化服务
		InvPrintProcessUtils.invRePrint(rePrintMap);
		
//		调用发票使用确认服务，完成发票更新。
		try{
			commonService.confirmUseEmpInv(pkEmpinv, new Long(newInvs.size()));
		}catch(Exception e){
			throw new BusException("发票更新失败，请确认票据是否充足！");
		}
		
		//重打发票发送平台消息
		PlatFormSendUtils.sendReceiptMsg(paraMap);
		
		return getPrintDataByPkSettle(pkSettle);
	}

	/**
	 * 结算前 检查是否有婴儿费用
	 * @param param
	 * @param user
	 */
	@SuppressWarnings("unchecked")
	public Map<String, Object> checkBabyCosts(String param,IUser user){
		JSONObject jo = JSONObject.fromObject(param);
		String codeIp = jo.getString("codeIp");
		Map<String, Object> returnMap = new HashMap<String, Object>();
		
		String sql = "select * from PV_ENCOUNTER a inner join PI_MASTER b on a.pk_pi = b.pk_pi  where a.pk_pi in (select pk_pi from PI_MASTER where code_ip like '%"+codeIp+"B%')  and a.eu_status<3 and a.eu_pvtype = '3' ";
		List<Map<String, Object>> list = DataBaseHelper.queryForList(sql);
		if(list.size()>0){
			List<Map<String, Object>> yeList = new ArrayList<Map<String,Object>>();
			//有未结算的婴儿
			for (Map<String, Object> map : list) {
				sql = "select sum(amount) amount  from bl_ip_dt where pk_pv = ? and del_flag = '0'";
				Map<String, Object> amount = DataBaseHelper.queryForMap(sql, map.get("pkPv"));
				Map<String, Object> yeMap = new HashMap<String, Object>();
				yeMap.put("codeIp", map.get("codeIp"));
				yeMap.put("namePi", map.get("namePi"));
				if(map.get("euStatus").toString().equals("2")){
					yeMap.put("status", "出院");
				}else{
					yeMap.put("status", "在院");
				}
				yeMap.put("amount", amount.get("amount")==null?"0":String.format("%.2f", amount.get("amount")));
				yeList.add(yeMap);
			}
			returnMap.put("list", yeList);
			returnMap.put("code", "0");
			returnMap.put("msg", "有待结算的婴儿");
		}else{
			returnMap.put("code", "-1");
			returnMap.put("msg", "没有待结算的婴儿");
		}
		return returnMap;
	}
	
	/**
	 * 结算前检查婴儿是否有妈咪费用
	 * @param param
	 * @param user
	 */
	@SuppressWarnings("unchecked")
	public Map<String, Object> checkMommyCosts(String param,IUser user){
		JSONObject jo = JSONObject.fromObject(param);
		String codeIp = jo.getString("codeIp");
		Map<String, Object> returnMap = new HashMap<String, Object>();
		if(codeIp.indexOf("B")!=-1){
			String codeIpMi = codeIp.substring(0, codeIp.indexOf("B"));
			String sql = "select * from PV_ENCOUNTER a inner join PI_MASTER b on a.pk_pi = b.pk_pi  where a.pk_pi in (select pk_pi from PI_MASTER where code_ip = '"+codeIpMi+"')  and a.eu_status<3 and a.eu_pvtype = '3'";
			List<Map<String, Object>> list = DataBaseHelper.queryForList(sql);
			if(list.size()>0){
				List<Map<String, Object>> yeList = new ArrayList<Map<String,Object>>();
				//有未结算的产妇
				for (Map<String, Object> map : list) {
					sql = "select sum(amount) amount  from bl_ip_dt where pk_pv = ? and del_flag = '0'";
					Map<String, Object> amount = DataBaseHelper.queryForMap(sql, map.get("pkPv"));
					Map<String, Object> yeMap = new HashMap<String, Object>();
					yeMap.put("codeIp", map.get("codeIp"));
					yeMap.put("namePi", map.get("namePi"));
					if(map.get("euStatus").toString().equals("2")){
						yeMap.put("status", "出院");
					}else{
						yeMap.put("status", "在院");
					}
					yeMap.put("amount", amount.get("amount")==null?"0":String.format("%.2f", amount.get("amount")));
					yeList.add(yeMap);
				}
				returnMap.put("list", yeList);
				returnMap.put("code", "0");
				returnMap.put("msg", "有待结算的产妇");
			}else{
				returnMap.put("code", "-1");
				returnMap.put("msg", "没有待结算的产妇");
			}
		}else{
			returnMap.put("code", "-1");
			returnMap.put("msg", "该住院号:"+codeIp+"不是婴儿住院号！");
		}
		return returnMap;
	}
	
	/**
	 * 结算前检查是否有历史欠费信息(住院)
	 * @param param
	 * @param user
	 */
	@SuppressWarnings("unchecked")
	public Map<String, Object> checkHistoryCosts(String param,IUser user){
		JSONObject jo = JSONObject.fromObject(param);
		String codeIp = jo.getString("codeIp");
		String ipTimes = jo.getString("ipTimes");
		Map<String, Object> returnMap = new HashMap<String, Object>();
		String sql = "select * from PV_ENCOUNTER a inner join PI_MASTER b on a.pk_pi = b.pk_pi "+
		" inner join pv_ip c on a.pk_pv = c.pk_pv "+
		" where a.pk_pi in (select pk_pi from PI_MASTER where code_ip = '"+codeIp+"')  and a.eu_status<3 and a.eu_pvtype = '3' and c.ip_times != '"+ipTimes+"'";
		List<Map<String, Object>> list = DataBaseHelper.queryForList(sql);
		if(list.size()>0){
			List<Map<String, Object>> yeList = new ArrayList<Map<String,Object>>();
			//有未结算的住院信息
			for (Map<String, Object> map : list) {
				sql = "select sum(amount) amount  from bl_ip_dt where pk_pv = ? and del_flag = '0'";
				Map<String, Object> amount = DataBaseHelper.queryForMap(sql, map.get("pkPv"));
				Map<String, Object> yeMap = new HashMap<String, Object>();
				yeMap.put("codeIp", map.get("codeIp"));
				yeMap.put("namePi", map.get("namePi"));
				if(map.get("euStatus").toString().equals("2")){
					yeMap.put("status", "出院");
				}else{
					yeMap.put("status", "在院");
				}
				yeMap.put("amount", amount.get("amount")==null?"0":String.format("%.2f", amount.get("amount")));
				yeList.add(yeMap);
			}
			returnMap.put("list", yeList);
			returnMap.put("code", "0");
			returnMap.put("msg", "有待结算的历史住院信息");
		}else{
			returnMap.put("code", "-1");
			returnMap.put("msg", "没有待结算的历史住院信息");
		}
		return returnMap;
	}
	
	/**
	 * 查询患者门诊欠费数据
	 * @param codeOp
	 * @return
	 */
	public List<Map<String, Object>> getWhetherArrears(String param,IUser user){
		StringBuffer sql = new StringBuffer(" select * from ( ");
		JSONObject jo = JSONObject.fromObject(param);
		String codeIp = jo.getString("codeIp");
		PiMaster pi = DataBaseHelper.queryForBean("select * from pi_master where code_ip = ?", PiMaster.class, codeIp);
		sql.append(" SELECT ");
			sql.append(" pv.PK_PI, pv.PK_PV, pi.CODE_OP, CONVERT(VARCHAR(16), pv.DATE_CLINIC, 120) AS DATE_CLINIC, op.OP_TIMES ");
			sql.append(" , dept.CODE_DEPT, dept.NAME_DEPT, emp.NAME_EMP, pi.NAME_PI ");
			sql.append(" , SUM(dt.AMOUNT) AMOUNT, hp.NAME NAME_HP, 0 AMT_DISC, dt.FLAG_SETTLE, dt.PK_SETTLE,hp.DT_EXTHP, 0 VAL_ATTR ");
		sql.append(" FROM ");
			sql.append(" BL_OP_DT dt ");
			sql.append(" INNER JOIN PV_ENCOUNTER pv on pv.PK_PV = dt.PK_PV and pv.eu_pvtype<>'3' ");//3:住院
			sql.append(" INNER JOIN PI_MASTER pi on pi.PK_PI = pv.PK_PI ");
			sql.append(" LEFT JOIN PV_OP op on op.PK_PV = pv.PK_PV ");
			sql.append(" LEFT JOIN BD_OU_DEPT dept on dept.PK_DEPT = pv.PK_DEPT ");
			sql.append(" LEFT JOIN BD_OU_EMPLOYEE emp on emp.PK_EMP = pv.PK_EMP_PHY ");
			sql.append(" LEFT JOIN BD_HP hp on hp.pk_hp = pv.PK_INSU ");
		sql.append(" WHERE ");
			sql.append(" dt.DEL_FLAG='0' AND dt.FLAG_ACC='0' AND dt.FLAG_SETTLE='0' AND dt.PK_CGOP_BACK is null ");
			sql.append(" AND pv.eu_status not in ('0','1','9') ");
			sql.append(" AND dt.FLAG_PV='1' ");
			sql.append(" AND pi.CODE_OP= ? ");
		sql.append(" GROUP BY ");
			sql.append(" pv.PK_PI, pv.PK_PV, pi.CODE_OP, pv.DATE_CLINIC, op.OP_TIMES, dept.CODE_DEPT, ");
			sql.append(" dept.NAME_DEPT, emp.NAME_EMP, pi.NAME_PI, hp.NAME, dt.FLAG_SETTLE, dt.PK_SETTLE,hp.DT_EXTHP ");
		
		sql.append(" UNION ALL ");	
		
		sql.append(" SELECT * FROM ( ");
			sql.append(" SELECT ");
				sql.append(" pv.PK_PI, pv.PK_PV, pi.CODE_OP, CONVERT(VARCHAR(16), pv.DATE_CLINIC, 120) AS DATE_CLINIC, op.OP_TIMES ");
				sql.append(" , dept.CODE_DEPT, dept.NAME_DEPT, emp.NAME_EMP, pi.NAME_PI ");
				sql.append(" , SUM(dt.AMOUNT) AMOUNT, hp.NAME NAME_HP, 0 AMT_DISC, dt.FLAG_SETTLE, dt.PK_SETTLE,hp.DT_EXTHP, ISNULL(attr.VAL_ATTR, 0) VAL_ATTR ");
			sql.append(" FROM ");
				sql.append(" BL_OP_DT dt ");
				sql.append(" INNER JOIN PV_ENCOUNTER pv on pv.PK_PV = dt.PK_PV and pv.eu_pvtype<>'3' ");//3:住院
				sql.append(" INNER JOIN PI_MASTER pi on pi.PK_PI = pv.PK_PI ");
				sql.append(" LEFT JOIN PV_OP op on op.PK_PV = pv.PK_PV ");
				sql.append(" LEFT JOIN BD_OU_DEPT dept on dept.PK_DEPT = pv.PK_DEPT ");
				sql.append(" LEFT JOIN BD_OU_EMPLOYEE emp on emp.PK_EMP = pv.PK_EMP_PHY ");
				sql.append(" LEFT JOIN BD_HP hp on hp.pk_hp = pv.PK_INSU ");
				sql.append(" LEFT JOIN CN_ORDER ord on ord.PK_CNORD = dt.PK_CNORD ");
				sql.append(" LEFT JOIN BD_DICTATTR attr on ord.pk_ord = attr.PK_DICT and attr.CODE_ATTR= 'BA004' ");
				sql.append(" LEFT JOIN BD_DICTATTR_TEMP attrt on attrt.PK_DICTATTRTEMP = attr.PK_DICTATTRTEMP and attrt.DT_DICTTYPE='02' ");
			sql.append(" WHERE ");
				sql.append(" dt.DEL_FLAG='0' AND dt.FLAG_ACC='0' AND dt.FLAG_SETTLE='0' AND dt.PK_CGOP_BACK is null ");
				sql.append(" AND pv.eu_status not in ('0','1','9') ");
				sql.append(" AND pi.CODE_OP= ? ");
			sql.append(" GROUP BY ");
				sql.append(" pv.PK_PI, pv.PK_PV, pi.CODE_OP, pv.DATE_CLINIC, op.OP_TIMES, dept.CODE_DEPT, ");
				sql.append(" dept.NAME_DEPT, emp.NAME_EMP, pi.NAME_PI, hp.NAME, dt.FLAG_SETTLE, dt.PK_SETTLE,hp.DT_EXTHP,attr.VAL_ATTR ");
		sql.append(" ) t WHERE t.VAL_ATTR=1 ");	
			
		sql.append(" ) a ORDER BY a.OP_TIMES asc ");
		List<Map<String, Object>> list = DataBaseHelper.queryForList(sql.toString(), pi.getCodeOp(), pi.getCodeOp());
		return list;
	}
	
	
	/**
	 * 查询患者预缴金
	 * @param param
	 */
	public List<Map<String,Object>> queryChargePrePay(String param,IUser user){
		Map<String,Object> paraMap = JsonUtil.readValue( param, Map.class); 
		if(paraMap == null){
			paraMap = new HashMap<String,Object>();
    	}
		//使用date_begin作为条件可能会查不出患者的预交金信息，故把dateBegin条件删除。
    	if(CommonUtils.isNotNull(paraMap.get("dateBegin"))){
    		//paraMap.put("dateBegin", CommonUtils.getString(paraMap.get("dateBegin")).substring(0, 8)+"000000");
    		paraMap.remove("dateBegin");
    	}
    	//结算查询患者预交金信息无需根据时间过滤，需要全部显示出患者预交金记录，故把date_end条件删除
		if(CommonUtils.isNotNull(paraMap.get("dateEnd"))){
			//paraMap.put("dateEnd", CommonUtils.getString(paraMap.get("dateEnd")).substring(0, 8)+"235959");
			paraMap.remove("dateEnd");
		}
		return zsBlIpSettleQryMapper.queryChargePrePay(paraMap);
	}
}
