package com.zebone.nhis.bl.ipcg.service;

import com.zebone.nhis.bl.ipcg.support.YBProcessUtils;
import com.zebone.nhis.bl.ipcg.vo.SaveInvInfoVo;
import com.zebone.nhis.bl.pub.dao.BlIpSettleQryMapper;
import com.zebone.nhis.bl.pub.service.HpService;
import com.zebone.nhis.bl.pub.service.PareAccoutService;
import com.zebone.nhis.bl.pub.service.PriceStrategyService;
import com.zebone.nhis.bl.pub.support.Constant;
import com.zebone.nhis.bl.pub.support.InvPrintProcessUtils;
import com.zebone.nhis.bl.pub.support.InvSettltService;
import com.zebone.nhis.bl.pub.vo.*;
import com.zebone.nhis.common.dao.BlIpPubMapper;
import com.zebone.nhis.common.module.bl.*;
import com.zebone.nhis.common.module.compay.ins.syx.gzyb.InsGzgyBl;
import com.zebone.nhis.common.module.compay.ins.zsba.zsyb.InsZsybSt;
import com.zebone.nhis.common.module.mybatis.ReflectHelper;
import com.zebone.nhis.common.module.pay.BlExtPay;
import com.zebone.nhis.common.module.pi.PiAcc;
import com.zebone.nhis.common.module.pi.acc.PiAccDetail;
import com.zebone.nhis.common.module.pi.support.PiConstant;
import com.zebone.nhis.common.module.pv.PvEncounter;
import com.zebone.nhis.common.service.BalAccoutService;
import com.zebone.nhis.common.service.CommonService;
import com.zebone.nhis.common.support.*;
import com.zebone.nhis.ma.pub.platform.PlatFormSendUtils;
import com.zebone.platform.Application;
import com.zebone.platform.common.support.NHISUUID;
import com.zebone.platform.common.support.User;
import com.zebone.platform.common.support.UserContext;
import com.zebone.platform.framework.support.IUser;
import com.zebone.platform.modules.dao.DataBaseHelper;
import com.zebone.platform.modules.exception.BusException;
import com.zebone.platform.modules.utils.JsonUtil;
import org.apache.commons.collections.MapUtils;
import org.apache.shiro.util.StringUtils;
import org.codehaus.jackson.type.TypeReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import javax.annotation.Resource;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.Map.Entry;

/**
 * 出院结算服务接口
 * @author Roger
 *
 */
@Service
public class IpSettleService {
	
	
	@Autowired
	private PriceStrategyService priceStrategyService;
	@Autowired
	private HpService hpService;
	@Autowired
	private InvSettltService invSettltService;
	@Autowired
	private CommonService commonService;
	@Autowired
	private BalAccoutService balAccoutService;
	@Autowired
	private BlIpSettleQryMapper blIpSettleQryMapper;
	@Autowired
	private BlIpPubMapper blIpPubMapper;
	@Autowired
	private PareAccoutService pareAccoutService;
	// 用来控制手动事物
	@Resource(name = "transactionManager")
	private PlatformTransactionManager platformTransactionManager;
		
	
	/**
	 * 出院预结
	 * @param param
	 * @param user
	 */
	public void Discharge(String param,IUser user){
		Map<String, String> paraMap = JsonUtil.readValue(param, Map.class);
		String sql = "update pv_ip set flag_prest = 1 , date_prest =? ,"
				+ " pk_emp_prest = ?, name_emp_prest = ? where pk_pv = ?";
		DataBaseHelper.execute(sql, new Date(),UserContext.getUser().getPkEmp(),UserContext.getUser().getNameEmp(),paraMap.get("pkPv"));

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
				"select pv.pk_pv, pi.pk_pi,pv.code_pv,pi.code_ip, pi.name_pi,dept.name_dept,ip.flag_frozen,ip.dt_stway,ip.dt_sttype_ins");
		if ("1".equals(paraMap.get("flagQryInfantCharge"))) {
			sql.append(",inf.amount infAmount");
		}
		sql.append(" from pv_encounter pv");
		sql.append(" inner join pv_ip ip on pv.pk_pv=ip.pk_pv");
		sql.append(" inner join pi_master pi on pv.pk_pi=pi.pk_pi");
		sql.append(" inner join bd_ou_dept dept on pv.pk_dept=dept.pk_dept");
		if ("1".equals(paraMap.get("flagQryInfantCharge"))) {
			sql.append(" left join (select sum(ipdt.AMOUNT) as amount, pvinf.PK_PV from pv_infant pvinf"
					+ " inner join bl_ip_dt ipdt on ipdt.pk_pv = pvinf.PK_PV_INFANT"
					+ " where ipdt.FLAG_SETTLE = '0' group by pvinf.PK_PV) inf on inf.pk_pv = pv.pk_pv");
		}

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
		} else {// 中途结算 当前患者列表为“就诊”状态；
			sql.append(" and pv.eu_status in(1,2) ");
			sql.append(" and exists (select 1 from bl_ip_dt dt where dt.pk_pv = pv.pk_pv and flag_settle = '0') ");
		}
		//新增线上结算查询条件
		if(paraMap.get("isAtlSt")!=null && !CommonUtils.isEmptyString(CommonUtils.getString(paraMap.get("isAtlSt")))){
			if(CommonUtils.getString(paraMap.get("isAtlSt")).equals("0")){
				sql.append(" and (ip.dt_stway<>1  or ip.dt_stway is null or ip.dt_stway='') ");
			}else if(CommonUtils.getString(paraMap.get("isAtlSt")).equals("1")){
				sql.append(" and (ip.dt_stway<>0 or ip.dt_stway is null or ip.dt_stway='') ");
			}
		}
		List<Map<String, Object>> res = new ArrayList<Map<String, Object>>();

		if("02".equals(typeDept)){
			res = DataBaseHelper.queryForList(sql.toString(), pkOrg, UserContext.getUser().getPkDept());
		}else if(typeDept!=null&&"08".equals(typeDept.substring(0, 2))){
			res = DataBaseHelper.queryForList(sql.toString(), pkOrg);
		}
		return res;
	}


	private void settleForArrears(BlSettle blSettle) {
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

	private void settleForDeposit(BlSettle blSettle,IUser user,String pkPv) {
		//  //4.1.1）计算可存款金额 = 患者预交金 - 患者自付金额；
		BigDecimal amountPi = blSettle.getAmountPi()==null?BigDecimal.ZERO:blSettle.getAmountPi();
		BigDecimal amountPrep = blSettle.getAmountPrep()==null?BigDecimal.ZERO:blSettle.getAmountPrep();
		BigDecimal deposit = amountPrep.subtract(amountPi);
		 //4.1.2）生成支付数据：
		BlDeposit blDepo = new BlDeposit();
		blDepo.setEuDirect("-1");
		blDepo.setEuDptype("0");
		blDepo.setPkPi(blSettle.getPkPi());
		blDepo.setPkPv(blSettle.getPkPv());
		blDepo.setAmount(deposit);
		blDepo.setDtPaymode("5");
		blDepo.setDatePay(new Date());
		blDepo.setPkDept(blSettle.getPkDeptSt());
		blDepo.setPkEmpPay(blSettle.getPkEmpSt());
		blDepo.setNameEmpPay(blSettle.getNameEmpSt());
		blDepo.setFlagAcc("0");
		blDepo.setFlagSettle("1");
		blDepo.setNote("余额存入患者账户");
		blDepo.setPkDepo(NHISUUID.getKeyId());
        blDepo.setFlagReptBack("0");
        blDepo.setEuPvtype("3");
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

	/**
	 * 判断患者是否有过中途结算，如果有，返回上次的结算日期
	 * @param PkPv
	 * @return
	 */
	private Date checkMidSettle(String PkPv){
		
	Map<String,Object> map = DataBaseHelper.queryForMap("select count(1) amt from bl_settle where dt_sttype = 11 and flag_canc = 0 and pk_pv = ? and pk_settle not in (select pk_settle_canc from bl_settle where pk_pv = ?)", PkPv,PkPv);
	int cnt = 0;
	if(map.get("amt") instanceof BigDecimal){
		BigDecimal amt = amtTrans(map.get("amt"));
		cnt = amt.intValue();
	}else{
		 cnt = map.get("amt")==null?0:(Integer)map.get("amt");
	}
	
		if(cnt> 0){
			Map<String,Object> dateInfo = DataBaseHelper.queryForMap("select date_end dateEnd from bl_settle where  dt_sttype = 11 and flag_canc = 0 and  pk_pv = ? order by  date_end desc", PkPv);
			return (Date)dateInfo.get("dateend");
		}
		return null;
	}
	
	@SuppressWarnings("unchecked")
	public Date getMidStBeginDate(String param,IUser user){
		
		Map<String,Object> para = JsonUtil.readValue(param, Map.class);	
		String pkPv  = (String)para.get("pkPv");
		PvEncounter pvvo = DataBaseHelper.queryForBean(
				"select date_begin from pv_encounter where pk_pv = ?",
				PvEncounter.class, pkPv);
		Date begin  = checkMidSettle(pkPv);
		return begin==null?pvvo.getDateBegin():begin;
	}
	private List<Map<String,Object>> getHpPlans(String PkPv){
		 List<Map<String,Object>> res = new ArrayList<Map<String,Object>>();
		//1.pv_insurance表里的医保
		StringBuffer sql = new StringBuffer("select bh.pk_payer,insu.sort_no,bh.eu_hptype,insu.pk_hp,insu.flag_maj");
		sql.append(" from pv_insurance insu");
		sql.append(" inner join  bd_hp bh");
		sql.append(" on insu.pk_hp = bh.pk_hp ");
		sql.append("  where insu.pk_pv = ?");
		sql.append(" order by insu.sort_no asc,insu.flag_maj desc");
		res.addAll(DataBaseHelper.queryForList(sql.toString(), PkPv));
        return res;
	}
	
	/**
	 * @param param DateBegin:'开始日期'DateEnd:'结束日期' PkPv:'就诊主键' 
	 * @param user
	 * @return 结算金额  预交金额? 医保支付 患者支付 账户支付 结算应收
	 */
	@SuppressWarnings("unused")
	public Map<String, Object>  GetAmtInfo(String param, IUser user) {
		
		Map<String,Object> res = new HashMap<String,Object>();
		User userinfo = (User)user;
		@SuppressWarnings("unchecked")
		Map<String,Object> paraMap = JsonUtil.readValue( param, Map.class); 

		String pkPv = (String)paraMap.get("pkPv");
		//String dateBegin =(String)paraMap.get("dateBegin");
		String dateBegin = CommonUtils.getString(paraMap.get("dateBegin"))
				.substring(0, 8) + "000000";
		Date begin = dateTrans(dateBegin);
		String dateEnd =(String)paraMap.get("dateEnd");	
		Date end = dateTrans(dateEnd);
		String euSttype = (String)paraMap.get("euSttype");
		Double amtInsuThird = Double.valueOf(paraMap.get("amtInsuThird")==null?"0":paraMap.get("amtInsuThird").toString());// xr+  第三方医保有前端传过来
		//定义一些公共变量
		StringBuffer sqlSt;
		Map<String,Object> amtStMap;
		BigDecimal amtSt;//结算金额AmtSt
		//String cgips = paraMap.get("pkCgips").toString().substring(paraMap.get("pkCgips").toString().indexOf("[")+1,paraMap.get("pkCgips").toString().lastIndexOf("]"));
		List<String> cgipsArgs = (List<String>)paraMap.get("pkCgips");
		// 本次結算的預交金主鍵
		List<String> pkDepoList = (List<String>) paraMap.get("pkDepoList");
		
		// 校验是否是出院结算，如果是出院结算则把end赋值为20991231235959
		if (!CommonUtils.isEmptyString(euSttype) && "10".equals(euSttype)) {
			end = dateTrans("20991231235959");
		}
		
		//String[] cgipsArgs = cgips.split(",");
		if(cgipsArgs != null){
			String pkCgips = "";
			int i = 0;
			for (String pkcgip : cgipsArgs) {
				if (i == 0) {
					pkCgips += "'" + pkcgip.trim() + "'";
				} else {
					pkCgips += ",'" + pkcgip.trim() + "'";
				}
				i++;
			}
			//结算金额AmtSt
			sqlSt = new StringBuffer(" select Sum(amount) AmtSt from bl_ip_dt blip where blip.flag_settle = '0' and blip.pk_cgip in (" + pkCgips + ")");
			sqlSt.append(" and blip.pk_pv = ?  and blip.date_hap >= ? and blip.date_hap <= ?");
			amtStMap = DataBaseHelper.queryForMap(sqlSt.toString(), pkPv,begin,end);
			amtSt = amtTrans(amtStMap.get("amtst"));
			res.put("AmtSt", amtSt);
			//冲账结算发票金额=新增收退金额+被冲账结算金额AmtStrikeInv
			
			StringBuffer strikeInvSt = new StringBuffer(" select Sum(amount) AmtSt from bl_ip_dt blip where 1=1 and blip.pk_cgip in (" + pkCgips + ")");
			strikeInvSt.append(" and blip.pk_pv = ?  and blip.date_hap >= ? and blip.date_hap <= ?");
			Map<String,Object> strikeInvStMap = DataBaseHelper.queryForMap(strikeInvSt.toString(), pkPv,begin,end);
			BigDecimal strikeInvStAmt = amtTrans(strikeInvStMap.get("amtst"));
			res.put("AmtStrikeInv", strikeInvStAmt);
		}else{
			//结算金额AmtSt
			sqlSt = new StringBuffer(" select Sum(amount) AmtSt from bl_ip_dt blip where blip.flag_settle = '0' ");
			sqlSt.append(" and blip.pk_pv = ?  and blip.date_hap >= ? and blip.date_hap <= ?");
			amtStMap = DataBaseHelper.queryForMap(sqlSt.toString(), pkPv,begin,end);
			amtSt = amtTrans(amtStMap.get("amtst"));
			res.put("AmtSt", amtSt);
			//冲账结算发票金额=新增收退金额+被冲账结算金额AmtStrikeInv
			
			StringBuffer strikeInvSt = new StringBuffer(" select Sum(amount) AmtSt from bl_ip_dt blip where 1=1  ");
			strikeInvSt.append(" and blip.pk_pv = ?  and blip.date_hap >= ? and blip.date_hap <= ?");
			Map<String,Object> strikeInvStMap = DataBaseHelper.queryForMap(strikeInvSt.toString(), pkPv,begin,end);
			BigDecimal strikeInvStAmt = amtTrans(strikeInvStMap.get("amtst"));
			res.put("AmtStrikeInv", strikeInvStAmt);
		}
		// 预缴金额AmtPrep
		// 成伟要求：操作bl_deposit表只使用pk_pv字段查询。
		// StringBuffer sqlPrep = new
		// StringBuffer("select sum(depo.amount)  AmtPrep from bl_deposit depo where depo.eu_dptype=9 and depo.flag_settle = '0' and  depo.pk_pv = ? and depo.date_pay >= ? and depo.date_pay <= ?");
		StringBuffer sqlPrep = new StringBuffer(
				"select sum(depo.amount)  AmtPrep from bl_deposit depo where depo.eu_dptype=9 and depo.flag_settle = '0' and  depo.pk_pv = ? ");
		String isStPre = ApplicationUtils.getSysparam("BL0040", false);
		if (!CommonUtils.isEmptyString(isStPre) && isStPre.equals("0")
				&& !CommonUtils.isEmptyString(euSttype)
				&& euSttype.equals("11")) {
			if (pkDepoList != null && pkDepoList.size() > 0) {
				res.put("pkDepoList", pkDepoList);
				sqlPrep.append(" and depo.pk_depo in (");
				for (int i = 0; i < pkDepoList.size(); i++) {
					if (i == 0) {
						sqlPrep.append("'" + pkDepoList.get(i).trim() + "'");
					} else {
						sqlPrep.append(",'" + pkDepoList.get(i).trim() + "'");
					}
				}
				sqlPrep.append(" ) ");
			} else {
				sqlPrep.append(" and 1=0 ");
			}
		}
		
		Map<String,Object> amtPrepMap = DataBaseHelper.queryForMap(sqlPrep.toString(), pkPv);
		BigDecimal amtPrep = amtTrans(amtPrepMap.get("amtprep"));
		res.put("AmtPrep",amtPrep);	
		//医保支付	第三方医保报销金额 + 内部医保报销金额；
		//第三方医保报销金额由第三方医保接口返回；AmtInsu		
		BlSettle tempSt = new BlSettle();
		List<BlSettleDetail> details = handleSettleDetailC(pkPv,tempSt,userinfo.getPkOrg(),euSttype,dateBegin,dateEnd);
		Double amtTemp = 0.0;
		Double amtPiTemp = 0.0;
		String payerSelf = qryBdPayerByEuType(userinfo.getPkOrg());
		for(BlSettleDetail vo : details){
			if(!payerSelf.equals(vo.getPkPayer())){
				amtTemp+=vo.getAmount();
			}else{
				//res.put("AmtPi", vo.getAmount()-amtInsuThird);
				amtPiTemp+=vo.getAmount();
			}
		}
		BigDecimal amtPi = new BigDecimal(amtPiTemp-amtInsuThird);
		amtPi = amtPi.setScale(2, BigDecimal.ROUND_HALF_UP);
		res.put("AmtPi", amtPi.compareTo(BigDecimal.ZERO)<0?new BigDecimal(0):amtPi);
		
		BigDecimal amtInsu = new BigDecimal(amtTemp+amtInsuThird);
		amtInsu = amtInsu.setScale(2, BigDecimal.ROUND_HALF_UP);
		res.put("AmtInsu", amtInsu);
		if(tempSt.getAmountInsu()!=null && tempSt.getAmountInsu().compareTo(BigDecimal.ZERO) !=0){
			res.put("AmtInsuThird", tempSt.getAmountInsu());
		}else{
			res.put("AmtInsuThird", BigDecimal.ZERO);
		}
//		List<Map<String,Object>> hpPlans = getHpPlans(pkPv);
//		for(Map<String,Object> map : hpPlans){
//			String flagMaj = (String)map.get("flagMaj");
//			if("1".equals(flagMaj)){
//				res.put("PkInsurance", map.get("pkHp"));
//			}
//		}
		
		//获取患者主医保(注释上面代码，目前存在PV_INSURANCE表和PV_ENCOUNTER表医保不一致问题，故直接获取PV_ENCOUNTER的pk_insu)
		String pkInsu = DataBaseHelper.queryForScalar(
				"select pk_insu from PV_ENCOUNTER where pk_pv = ?",
				String.class, new Object[]{pkPv});
		res.put("PkInsurance", pkInsu);
		
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
	
	private String qryBdPayerByEuType(String pkOrg) {
		String pkPayer = "";
		Map<String,Object> map = DataBaseHelper.queryForMap("select pk_payer from bd_payer where eu_type='0' ");
		if(map != null){
			pkPayer = (String)map.get("pkPayer");
		}
		return pkPayer;
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
	public String dateTrans(Date date) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
		String temp = null;
		if(date!=null){
			temp = sdf.format(date);
		}
		
		return temp;
	}
	
	private BigDecimal amtTrans(Object amt) {
		if(amt == null){
			return BigDecimal.ZERO;
		}else{
			return (BigDecimal)amt;
		}
	}
	
	public List<InvInfoVo> getInvAmountByDate(String param , IUser user)  throws Exception {
		SettleInfo allData = JsonUtil.readValue(param, SettleInfo.class);
		if(null == allData)
			throw new BusException("参数错误！");
		String pkPv = allData.getPkPv();//就诊主键
		List<InvInfoVo> invoInfos = allData.getInvos();//部分发票信息（一条或两条数据）
		
		for(InvInfoVo inv : invoInfos){
			if(null != inv.getDateSplitBegin() && null != inv.getDateSplitEnd() ){
				Map<String, Object> amtPrepMap = DataBaseHelper
						.queryForMap(
								"select sum(depo.amount)  AmtPrep from bl_ip_dt  depo where depo.pk_pv = ? and depo.date_cg  >= ? and depo.date_cg  <= to_date(?, 'YYYYMMDDHH24MISS')   and del_flag = ?",
								pkPv, inv.getDateSplitBegin(), DateUtils.getSpecifiedDateStr(inv.getDateSplitEnd(), 0)+"235959"  ,0);
				inv.setAmount(amtTrans(amtPrepMap.get("amtprep")));
			}
		}
		
		return invoInfos;
	}
	
	
	@SuppressWarnings("unchecked")
	public String dealSettleData(String param, IUser user) {
		//1.参数接收		
		SettleInfo allData = JsonUtil.readValue(param, SettleInfo.class);		
		BlDeposit fromSettle = allData.getDeposit();//收退找零金额
		BlDeposit depositAcc = allData.getDepositAcc();//账户支付金额
		String pkPv = allData.getPkPv();//就诊主键
		String euSttype = allData.getEuSttype();//结算类型
		String euStresult = allData.getEuStresult();//结算结果类型
		String dateEnd = allData.getDateEnd();//结算截止时间
		String flagHbPrint = allData.getFlagHbPrint();//是否合并特诊发票
		String dateBegin = "";//开始时间
		List<InvInfoVo> invoInfos = allData.getInvos();//发票信息
		BlExtPay blExtPay = allData.getBlExtPay();//第三方支付交易记录 xr+
		
		PvEncounter pvvo = DataBaseHelper.queryForBean(
				"select * from pv_encounter where pk_pv = ?",
				PvEncounter.class, pkPv);
		if(EnumerateParameter.ZERO.equals(euSttype)&&EnumerateParameter.ONE.equals(pvvo.getEuStatus())){
			throw new BusException("该患者已经取消出院，请刷新后重试！");
		}
		Date midSettle = checkMidSettle(pkPv);
		if(midSettle == null){
			dateBegin = dateTrans(pvvo.getDateBegin());
		}else{
			dateBegin = dateTrans(midSettle);
		}
		//yangxue 修改为手动事物
		// 关闭事务自动提交
		DefaultTransactionDefinition def = new DefaultTransactionDefinition();
		def.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRES_NEW);
		TransactionStatus status = platformTransactionManager.getTransaction(def);
		BlSettle stVo = null;
		
		try{
		
		//2.结算数据准备  结算明细数据准备		
			//2.1结算数据准备，处理了账户支付后，患者账户相关pi_acc,pi_acc_detial表
		Map<String,Object> stDataMap = settleData(allData,pkPv,euSttype,dateBegin,dateEnd,user,euStresult,null);
		List<BlSettleDetail> stDtVos = (List<BlSettleDetail>) stDataMap.get("detail");
		stVo = (BlSettle)stDataMap.get("settle");
		for(BlSettleDetail vo : stDtVos){
			setDefaultValue(vo, true);
			vo.setPkStdt(NHISUUID.getKeyId());
		}
		setDefaultValue(stVo, true);
			//2.2结算数据写入
		DataBaseHelper.insertBean(stVo);
		DataBaseHelper.batchUpdate(DataBaseHelper.getInsertSql(BlSettleDetail.class), stDtVos);
		

		//3.结算结果类型判断，更新付款表-bl_deposit
			//3.1更新收款表
	    updDepositInfo(pkPv,stVo.getPkSettle(),dateTrans(dateBegin),dateTrans(dateEnd));
	    	//3.2写入结算时账户支付的收付款记录（结算信息标志已加入）
	    if(depositAcc!=null){
				setDepoInfo(depositAcc, stVo);
				depositAcc.setEuDptype(euSttype);
				setDefaultValue(depositAcc, true);
				DataBaseHelper.insertBean(depositAcc);
	    }
	    	//3.3写入结算时，多退少补的收付款记录（结算信息标志已加入）
		if(fromSettle!=null){	
			if(EnumerateParameter.ZERO.equals(euSttype)){//出院结算
				setDepoInfo(fromSettle, stVo);
				fromSettle.setEuDptype("0");
				if(EnumerateParameter.TWO.equals(euStresult)){
					//3.3.1存款结算
					settleForDeposit(stVo,user,pkPv);//账户充值过程
					setDefaultValue(fromSettle, true);
					fromSettle.setDateReptBack(null);
					DataBaseHelper.insertBean(fromSettle);
				}else if(EnumerateParameter.ONE.equals(euStresult)){
					//3.3.2欠款结算			  
					settleForArrears(stVo);//欠费记录过程
				}else{//3.3.3正常结算
					setDefaultValue(fromSettle, true);
					fromSettle.setDateReptBack(null);
					DataBaseHelper.insertBean(fromSettle);
				}
								
			}else if(EnumerateParameter.ONE.equals(euSttype)){//中途结算，少收多不退，但是存预交金
				  
				//3.3结算类型为中途结算时，如果预交金额合计>结算费用合计，将其差额生成新的预交金记录（eu_dptype=9），写bl_deposit表，并在pk_st_mid字段记录中途结算主键；如果预交金合计<结算费用合计，处理方式同出院结算；
				if(EnumerateParameter.NEGA.equals(fromSettle.getEuDirect())){//预交金多了，先退预交金，后转存预交金，要写两条记录
					//3.3.1 本次结算退多余预交金的记录
					BlDeposit rtn = (BlDeposit)fromSettle.clone();
					setDepoInfo(rtn, stVo);
					rtn.setEuDptype("1");
					rtn.setReptNo(null);
					rtn.setPkEmpinv(null);
					setDefaultValue(rtn, true);
					DataBaseHelper.insertBean(rtn);
					//3.3.2 存一笔预交金的收款记录
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
				}else{//预交金少了，收款
					fromSettle.setEuDptype("1");
					 fromSettle.setEuDirect("1");
					setDepoInfo(fromSettle, stVo);
				}
				setDefaultValue(fromSettle, true);
				fromSettle.setDateReptBack(null);
				DataBaseHelper.insertBean(fromSettle);
			}		
			if(blExtPay!=null){
				//处理第三方支付交易数据	
				saveBlExtPay(fromSettle.getPkDepo(),blExtPay);
			}
			
		}
		//处理医保结算业务,根据医保配置文件yb.properties配置医保处理类进行处理相应医保业务
		YBProcessUtils.dealYBSettleMethod(allData, stVo);
	
		//4.更新住院记费表		
		updChargeInfo(pkPv,stVo.getPkSettle(),dateTrans(dateBegin),dateTrans(dateEnd));
		if(EnumerateParameter.ZERO.equals(euSttype)){//欠款结算到此结束不做发票处理
				 if(EnumerateParameter.ONE.equals(euStresult)){
					DataBaseHelper.execute("update pv_encounter set flag_settle = '1',eu_status = '3' where pk_pv = ? ", pvvo.getPkPv());
					return stVo.getPkSettle();
				}
		}	
		if(invoInfos!=null&&invoInfos.size()>0){  
			//5发票数据准备
			Map<String,Object> invMap = invSettltService.invoData(pkPv,pvvo.getFlagSpec(),invoInfos,user,dateBegin,dateEnd,stVo.getPkSettle(),flagHbPrint);	
			List<BlInvoice> invo = (List<BlInvoice>) invMap.get("inv");;
			List<BlInvoiceDt> invoDt = (List<BlInvoiceDt>)invMap.get("invDt");
				//5.1写发票表bl_invoice和发票明细表bl_invoice_dt
				insertInvo(invo,invoDt);
			//6.写发票和结算关系表bl_st_inv
			insertInvoSt(invo,stVo);
			//7.调用发票使用确认服务，完成发票更新。
			commonService.confirmUseEmpInv(invoInfos.get(0).getInv().getPkEmpinv(), new Long(invo.size()));
		}

		//8.结算结果类型判断，存款结算  调用账户充值服务，将预交金合计-结算金额合计的差额记入患者账户,已处理
		
		//9.更新pv表的结算字段；
		if(!EnumerateParameter.ONE.equals(euSttype)){
			DataBaseHelper.execute("update pv_encounter set flag_settle = '1',eu_status = '3' where pk_pv = ? ", pvvo.getPkPv());
		}
		
		 platformTransactionManager.commit(status); // 提交事务
		} catch (Exception e) {
	       platformTransactionManager.rollback(status); // 添加失败 回滚事务；
		   throw new BusException("结算失败：" + e.getMessage());
		}
		/* HL7消息发送-数据组装*/
		String amtKind=EnumerateParameter.ZERO.equals(euSttype)?"3":""; 
		amtKind=EnumerateParameter.ONE.equals(euSttype)?"2":"1";
		List<Map<String, Object>> hpMap=getHpPlans(allData.getPkPv());
		String amtType="";
		for (Map<String, Object> map : hpMap) {
			if(EnumerateParameter.ZERO.equals(map.get("euHptype"))){
				amtType="01";
			}else if(EnumerateParameter.THREE.equals(map.get("euHptype"))){
				amtType="03";
			}else{
				amtType="02"; 
			}
		}
		User userinfo=(User)user;
		Map<String,Object> paramMap = JsonUtil.readValue(param, Map.class);	
		paramMap.put("doCode", userinfo.getCodeEmp());
		paramMap.put("doName", userinfo.getNameEmp());
		if(invoInfos!=null&& invoInfos.get(0)!=null &&invoInfos.get(0).getInv()!=null){
			paramMap.put("settleNo", invoInfos.get(0).getInv().getCurCodeInv());
		}
		paramMap.put("totalAmount", allData.getAmountSt());//总费用
		paramMap.put("selfAmount", allData.getAmountPi());//自费
		paramMap.put("amtInsu", allData.getAmountInsu());//医保费用
		paramMap.put("amtType", amtType);//结算类别
		paramMap.put("amtKind", amtKind);//结算方式
		paramMap.put("pkPv", allData.getPkPv());//就诊主键
		
		PlatFormSendUtils.sendBlSettleMsg(paramMap);
		paramMap = null;
		return stVo==null?"":stVo.getPkSettle();

	}
	
	/**add
	 * 结算服务-暂为灵璧项目增加，后期修改
	 * @param param
	 * @param user
	 * @return
	 */
	public  Map<String,Object> dealSettleDataLb(String param, IUser user) {

		//1.参数接收
		SettleInfo allData = JsonUtil.readValue(param, SettleInfo.class);
		//BlDeposit fromSettle = allData.getDeposit();//收退找零金额
		BlDeposit depositAcc = allData.getDepositAcc();//账户支付金额
		String pkPv = allData.getPkPv();//就诊主键
		String euSttype = allData.getEuSttype();//结算类型
		String euStresult = allData.getEuStresult();//结算结果类型
		String dateEnd = allData.getDateEnd();//结算截止时间
		String flagHbPrint = allData.getFlagHbPrint();//是否合并特诊发票
		String dateBegin = "";//开始时间
		List<InvInfoVo> invoInfos = allData.getInvos();//发票信息
		List<BlExtPay> blExtPayList = allData.getBlExtPayList();//第三方支付交易记录 xr+
		List<BlDeposit> depoList=allData.getDepoList();//收款退款记录-因灵璧项目含笔退款记录，故使用此字段
		List<String> pkDepoList = allData.getPkDepoList();
		String flagPrint = allData.getFlagPrint();//是否打印纸质票据
		
		Map<String,Object> mapReturn=new HashMap<>(16);

		PvEncounter pvvo = DataBaseHelper.queryForBean(
				"select * from pv_encounter where pk_pv = ?",
				PvEncounter.class, pkPv);
		
		/** 校验患者就诊状态是否是结束 */
		if (EnumerateParameter.ZERO.equals(euSttype) && !EnumerateParameter.TWO.equals(pvvo.getEuStatus()))
			throw new BusException("患者就诊状态发生变化，请刷新页面重新结算！");

		
		if (CommonUtils.isEmptyString(dateBegin)){
			dateBegin = dateTrans(pvvo.getDateBegin());
		}
		
		//yangxue 修改为手动事物
		// 关闭事务自动提交
		DefaultTransactionDefinition def = new DefaultTransactionDefinition();
		def.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRES_NEW);
		TransactionStatus status = platformTransactionManager.getTransaction(def);
		BlSettle stVo = null;

		try{

			//2.结算数据准备  结算明细数据准备
			//2.1结算数据准备，处理了账户支付后，患者账户相关pi_acc,pi_acc_detial表
			Map<String,Object> stDataMap = settleData(allData,pkPv,euSttype,dateBegin,dateEnd,user,euStresult,pkDepoList);
			List<BlSettleDetail> stDtVos = (List<BlSettleDetail>) stDataMap.get("detail");
			stVo = (BlSettle)stDataMap.get("settle");
			for(BlSettleDetail vo : stDtVos){
				setDefaultValue(vo, true);
				vo.setPkStdt(NHISUUID.getKeyId());
			}
			setDefaultValue(stVo, true);
			//2.2结算数据写入
			DataBaseHelper.insertBean(stVo);
			DataBaseHelper.batchUpdate(DataBaseHelper.getInsertSql(BlSettleDetail.class), stDtVos);

			// 校验系统参数BL0040(0:不结转预交金 1:结转预交金)
			String isStPre = ApplicationUtils.getSysparam("BL0040", false);
			
			//3.结算结果类型判断，更新付款表-bl_deposit
			//3.1更新收款表
			//updDepositInfo(pkPv,stVo.getPkSettle(),dateTrans(dateBegin),dateTrans(dateEnd));
			updDepositInfo(pkPv, stVo.getPkSettle(), dateTrans(dateBegin),
					dateTrans(dateEnd), pkDepoList, isStPre, euSttype);
			//3.2写入结算时账户支付的收付款记录（结算信息标志已加入）
			if(depositAcc!=null){
				setDepoInfo(depositAcc, stVo);
				depositAcc.setEuDptype(euSttype);
				setDefaultValue(depositAcc, true);
				DataBaseHelper.insertBean(depositAcc);
			}
//			if (blDepositList!=null){
//				for (BlDeposit blDeposit:blDepositList){
//					if ("0".equals(euSttype) && !EnumerateParameter.ONE.equals(euStresult)){//欠款结算不写bl_deposit
//						setDepoInfo(blDeposit,stVo);
//						blDeposit.setEuDptype("0");
//						setDefaultValue(blDeposit, true);
//						DataBaseHelper.insertBean(blDeposit);
//
//						if (blExtPay!=null&&"1".equals(blDeposit.getEuDirect())){
//							saveBlExtPay(blDeposit.getPkDepo(),blExtPay);
//						}
//					}
//				}
//				mapReturn.put("blDepositList",blDepositList);
//			}
			
			// 3.3写入结算时，多退少补的收付款记录（结算信息标志已加入）
			if ((depoList != null && depoList.size() > 0)
					|| stVo.getEuStresult().equals(EnumerateParameter.ONE)) {
				if (EnumerateParameter.ZERO.equals(euSttype)) {// 出院结算

					if (EnumerateParameter.TWO.equals(euStresult)) {
						// 3.3.1存款结算
						settleForDeposit(stVo, user, pkPv);// 账户充值过程

						for (BlDeposit fromSettle : depoList) {
							setDepoInfo(fromSettle, stVo) ;
							fromSettle.setEuDptype(EnumerateParameter.ZERO);
							setDefaultValue(fromSettle, true);
							fromSettle.setDateReptBack(null);
							fromSettle.setPkDepo(NHISUUID.getKeyId());
							fromSettle.setDatePay(stVo.getDateSt());
						}

						DataBaseHelper.batchUpdate(
								DataBaseHelper.getInsertSql(BlDeposit.class),
								depoList);
						// DataBaseHelper.insertBean(fromSettle);
					} else if (EnumerateParameter.ONE.equals(euStresult)) {
						// 3.3.2欠款结算
						settleForArrears(stVo);// 欠费记录过程
					} else {// 3.3.3正常结算
						for (BlDeposit fromSettle : depoList) {
							setDepoInfo(fromSettle, stVo);
							// 结算类型
							fromSettle.setEuDptype(EnumerateParameter.ZERO);
							setDefaultValue(fromSettle, true);
							fromSettle.setDateReptBack(null);
							fromSettle.setPkDepo(NHISUUID.getKeyId());
							fromSettle.setDatePay(stVo.getDateSt());
						}
						DataBaseHelper.batchUpdate(
								DataBaseHelper.getInsertSql(BlDeposit.class),
								depoList);
						// DataBaseHelper.insertBean(fromSettle);
					}
					
					List<BlDeposit> dList = new ArrayList<>();
					dList.addAll(depoList);
					
					mapReturn.put("blDepositList",dList);
				} else if (EnumerateParameter.ONE.equals(euSttype)) {// 中途结算，少收多不退，但是存预交金
					// 参数值为1时，可以将退款金额结转为预交金，故更新票据使用号
					if (!CommonUtils.isEmptyString(isStPre) && isStPre.equals(EnumerateParameter.ONE)) {
						/** 校验是否需要更新发票数据 */
						for (BlDeposit vo : depoList) {
							if (EnumerateParameter.NEGA.equals(vo.getEuDirect())) {
								// 调用发票使用确认服务，完成发票更新。
								commonService.confirmUseEmpInv(depoList.get(0)
										.getPkEmpinvoice(), new Long(1));
								break;
							}
						}
					}

					for (BlDeposit fromSettle : depoList) {
						// 3.3结算类型为中途结算时，如果预交金额合计>结算费用合计，将其差额生成新的预交金记录（eu_dptype=9），写bl_deposit表，并在pk_st_mid字段记录中途结算主键；如果预交金合计<结算费用合计，处理方式同出院结算；
						if (EnumerateParameter.NEGA.equals(fromSettle.getEuDirect())) {// 预交金多了，先退预交金，后转存预交金，要写两条记录
							// 3.3.1 本次结算退多余预交金的记录
							BlDeposit rtn = (BlDeposit) fromSettle.clone();
							setDepoInfo(rtn, stVo);
							rtn.setEuDptype(EnumerateParameter.ONE);
							rtn.setReptNo(null);
							rtn.setPkEmpinv(null);
							setDefaultValue(rtn, true);
							DataBaseHelper.insertBean(rtn);
							// 参数值为1时，退款信息结转为预交金
							if (!CommonUtils.isEmptyString(isStPre)
									&& isStPre.equals(EnumerateParameter.ONE)) {
								// 3.3.2 存一笔预交金的收款记录
								fromSettle.setEuDptype(EnumerateParameter.NINE);
								fromSettle.setEuDirect(EnumerateParameter.ONE);
								fromSettle.setBankNo(null);
								fromSettle.setPayInfo(null);
								fromSettle.setPkStMid(stVo.getPkSettle());
								BigDecimal amt = fromSettle.getAmount();
								fromSettle.setAmount(amt
										.multiply(new BigDecimal(-1)));
								fromSettle.setNote("中途结算转存");
								fromSettle.setFlagSettle(EnumerateParameter.ZERO);
								fromSettle.setPkSettle(null);
								fromSettle.setDateReptBack(null);
								// 收款时生成流水号0606
								if (CommonUtils.isEmptyString(fromSettle
										.getCodeDepo()))
									fromSettle.setCodeDepo(ApplicationUtils
											.getCode("0606"));
							}
						} else {// 预交金少了，收款
							fromSettle.setEuDptype(EnumerateParameter.ONE);
							fromSettle.setEuDirect(EnumerateParameter.ONE);
							setDepoInfo(fromSettle, stVo);
						}
						setDefaultValue(fromSettle, true);
						fromSettle.setDateReptBack(null);
						fromSettle.setPkDepo(NHISUUID.getKeyId());
						fromSettle.setDatePay(stVo.getDateSt());
					}
					
					List<BlDeposit> dList = new ArrayList<>();
					dList.addAll(depoList);
					
					mapReturn.put("blDepositList",dList);
					
					// 参数值为0时，因为退款信息不结转预交金，故过滤掉退款信息
					if (!CommonUtils.isEmptyString(isStPre) && isStPre.equals(EnumerateParameter.ZERO)) {
						for (int i = depoList.size() - 1; i >= 0; i--) {
							if (depoList.get(i).getEuDirect().equals(EnumerateParameter.NEGA))
								depoList.remove(depoList.get(i));
						}
					}
					if (depoList != null && depoList.size() > 0)
						DataBaseHelper.batchUpdate(
								DataBaseHelper.getInsertSql(BlDeposit.class),
								depoList);

					// DataBaseHelper.insertBean(fromSettle);
				}
				
				if (blExtPayList!=null && blExtPayList.size()>0){
					for(BlExtPay blExtPay : blExtPayList){
						for (BlDeposit fromSettle : depoList) {
							if("1".equals(fromSettle.getEuDirect()) && fromSettle.getDtPaymode().equals(blExtPay.getEuPaytype())){
								// 处理第三方支付交易数据
								saveBlExtPay(fromSettle.getPkDepo(), blExtPay);
								break;
							}
						}
					}
					
				}
			
				
				
			}
			

//			//3.3写入结算时，多退少补的收付款记录（结算信息标志已加入）
//			if(fromSettle!=null){
//				if(EnumerateParameter.ZERO.equals(euSttype)){//出院结算
//					setDepoInfo(fromSettle, stVo);
//					fromSettle.setEuDptype("0");
//					if(EnumerateParameter.TWO.equals(euStresult)){
//						//3.3.1存款结算
//						settleForDeposit(stVo,user,pkPv);//账户充值过程
//						setDefaultValue(fromSettle, true);
//						fromSettle.setDateReptBack(null);
//						DataBaseHelper.insertBean(fromSettle);
//					}else if(EnumerateParameter.ONE.equals(euStresult)){
//						//3.3.2欠款结算
//						settleForArrears(stVo);//欠费记录过程
//					}else{//3.3.3正常结算
//						setDefaultValue(fromSettle, true);
//						fromSettle.setDateReptBack(null);
//						DataBaseHelper.insertBean(fromSettle);
//					}
//
//				}else if(EnumerateParameter.ONE.equals(euSttype)){//中途结算，少收多不退，但是存预交金
//
//					//3.3结算类型为中途结算时，如果预交金额合计>结算费用合计，将其差额生成新的预交金记录（eu_dptype=9），写bl_deposit表，并在pk_st_mid字段记录中途结算主键；如果预交金合计<结算费用合计，处理方式同出院结算；
//					if(EnumerateParameter.NEGA.equals(fromSettle.getEuDirect())){//预交金多了，先退预交金，后转存预交金，要写两条记录
//						//3.3.1 本次结算退多余预交金的记录
//						BlDeposit rtn = (BlDeposit)fromSettle.clone();
//						setDepoInfo(rtn, stVo);
//						rtn.setEuDptype("1");
//						rtn.setReptNo(null);
//						rtn.setPkEmpinv(null);
//						setDefaultValue(rtn, true);
//						DataBaseHelper.insertBean(rtn);
//						//3.3.2 存一笔预交金的收款记录
//						fromSettle.setEuDptype("9");
//						fromSettle.setEuDirect("1");
//						fromSettle.setBankNo(null);
//						fromSettle.setPayInfo(null);
//						fromSettle.setPkStMid(stVo.getPkSettle());
//						BigDecimal amt = fromSettle.getAmount();
//						fromSettle.setAmount(amt.multiply(new BigDecimal(-1)));
//						fromSettle.setNote("中途结算转存");
//						fromSettle.setFlagSettle("0");
//						fromSettle.setPkSettle(null);
//						fromSettle.setDateReptBack(null);
//					}else{//预交金少了，收款
//						fromSettle.setEuDptype("1");
//						fromSettle.setEuDirect("1");
//						setDepoInfo(fromSettle, stVo);
//					}
//					setDefaultValue(fromSettle, true);
//					fromSettle.setDateReptBack(null);
//					DataBaseHelper.insertBean(fromSettle);
//				}
//				if(blExtPay!=null&&fromSettle!=null){
//					//处理第三方支付交易数据
//					saveBlExtPay(fromSettle.getPkDepo(),blExtPay);
//				}
//
//			} else if(EnumerateParameter.ZERO.equals(euSttype) && EnumerateParameter.ONE.equals(euStresult)){//欠款结算
//				//3.3.2欠款结算
//				settleForArrears(stVo);//欠费记录过程
//			}

			//处理医保结算业务,根据医保配置文件yb.properties配置医保处理类进行处理相应医保业务
			YBProcessUtils.dealYBSettleMethod(allData, stVo);

			//4.更新住院记费表
			updChargeInfo(pkPv,stVo.getPkSettle(),dateTrans(dateBegin),dateTrans(EnumerateParameter.ZERO.equals(euSttype) ? "20991231235959" : dateEnd));
			if(EnumerateParameter.ZERO.equals(euSttype)){//欠款结算到此结束不做发票处理
				if(EnumerateParameter.ONE.equals(euStresult)){
					DataBaseHelper.execute("update pv_encounter set flag_settle = '1',eu_status = '3' where pk_pv = ? ", pvvo.getPkPv());
					mapReturn.put("pkSettle",stVo.getPkSettle());
					platformTransactionManager.commit(status); // 提交事务
					return mapReturn;
				}
			}
			if(invoInfos!=null&&invoInfos.size()>0){
				//5发票数据准备
				Map<String,Object> invMap = new HashMap<String, Object>(16);
				
				//获取BL0031（收费结算启用电子票据），参数值为1时打印电子票据
				//String eBillFlag = ApplicationUtils.getSysparam("BL0031", false);
				String eBillFlag = invSettltService.getBL0031ByNameMachine(invoInfos.get(0).getNameMachine());
				if(!CommonUtils.isEmptyString(eBillFlag) && "1".equals(eBillFlag)){
					invMap = invSettltService.eBillOutpatient(pkPv, invoInfos, user, dateBegin, dateEnd, stVo.getPkSettle(), flagPrint);
				}else{
					invMap = invSettltService.invoData(pkPv,pvvo.getFlagSpec(),invoInfos,user,dateBegin,dateEnd,stVo.getPkSettle(),flagHbPrint);
				}
				
				List<BlInvoice> invo = (List<BlInvoice>) invMap.get("inv");
				List<BlInvoiceDt> invoDt = (List<BlInvoiceDt>)invMap.get("invDt");
				if(invo!=null && invo.size()>0){
					Date st = stVo.getDateSt();
					invo.forEach(vo ->vo.setDateInv(st));
				}
				//5.1写发票表bl_invoice和发票明细表bl_invoice_dt
				insertInvo(invo,invoDt);
				//6.写发票和结算关系表bl_st_inv；
				insertInvoSt(invo,stVo);
				//7.调用发票使用确认服务，完成发票更新。
				//新增codeInv(纸质票据号)是否为空，空则不更新票据号
				if(invo!=null && invo.size()>0){
					for(BlInvoice invInfo :invo){
						if(!CommonUtils.isEmptyString(invInfo.getCodeInv())){
							commonService.confirmUseEmpInv(invoInfos.get(0).getInv().getPkEmpinv(), new Long(1));
						}
					}
				}
			}

			//8.结算结果类型判断，存款结算  调用账户充值服务，将预交金合计-结算金额合计的差额记入患者账户,已处理

			//9.更新pv表的结算字段；
			if(!EnumerateParameter.ONE.equals(euSttype)){
				DataBaseHelper.execute("update pv_encounter set flag_settle = '1',eu_status = '3' where pk_pv = ? ", pvvo.getPkPv());
			}

			platformTransactionManager.commit(status); // 提交事务
		} catch (Exception e) {
			platformTransactionManager.rollback(status); // 添加失败 回滚事务；
			throw new BusException("结算失败：" + e.getMessage());
		}
		/* HL7消息发送-数据组装*/
		String amtKind=euSttype.equals(EnumerateParameter.ZERO)?"3":"";
		amtKind=euSttype.equals(EnumerateParameter.ONE)?"2":"1";
		List<Map<String, Object>> hpMap=getHpPlans(allData.getPkPv());
		String amtType="";
		for (Map<String, Object> map : hpMap) {
			if(map.get("euHptype").equals(EnumerateParameter.ZERO)){
				amtType="01";
			}else if(map.get("euHptype").equals(EnumerateParameter.THREE)){
				amtType="03";
			}else{
				amtType="02";
			}
		}
		User userinfo=(User)user;
		Map<String,Object> paramMap = JsonUtil.readValue(param, Map.class);
		paramMap.put("doCode", userinfo.getCodeEmp());
		paramMap.put("doName", userinfo.getNameEmp());
		if(invoInfos!=null&& invoInfos.get(0)!=null &&invoInfos.get(0).getInv()!=null){
			paramMap.put("settleNo", invoInfos.get(0).getInv().getCurCodeInv());
		}
		paramMap.put("totalAmount", allData.getAmountSt());//总费用
		paramMap.put("selfAmount", allData.getAmountPi());//自费
		paramMap.put("amtInsu", allData.getAmountInsu());//医保费用
		paramMap.put("amtType", amtType);//结算类别
		paramMap.put("amtKind", amtKind);//结算方式
		paramMap.put("pkPv", allData.getPkPv());//就诊主键
		paramMap.put("invoInfos", invoInfos);//发票信息；深大使用
		PlatFormSendUtils.sendBlSettleMsg(paramMap);


		paramMap = null;
		mapReturn.put("pkSettle",stVo.getPkSettle());
		return mapReturn;

	}

	public void insertInvoSt(List<BlInvoice> invos, BlSettle stVo) {
		for(BlInvoice invo :invos ){
			BlStInv vo = new BlStInv();
			vo.setPkInvoice(invo.getPkInvoice());
			vo.setPkOrg(invo.getPkOrg());
			vo.setPkSettle(stVo.getPkSettle());
			vo.setPkStinv(NHISUUID.getKeyId());
			setDefaultValue(vo, true);
			DataBaseHelper.insertBean(vo);
		}
		
	}

	public void insertInvo(List<BlInvoice> invo, List<BlInvoiceDt> invoDt) {
		for(BlInvoice vo:invo){
			setDefaultValue(vo, true);
		}
		for(BlInvoiceDt vo:invoDt){
			setDefaultValue(vo, true);
		}
		DataBaseHelper.batchUpdate(DataBaseHelper.getInsertSql(BlInvoice.class), invo);
		DataBaseHelper.batchUpdate(DataBaseHelper.getInsertSql(BlInvoiceDt.class), invoDt);
	}

	private void updChargeInfo(String pkPv, String pkSettle,Date begin,Date end) {
		String sql = "update bl_ip_dt set flag_settle = 1, pk_settle = ? where  flag_settle <> 1 and pk_pv = ? and  date_hap >= ? and date_hap <= ?";
		
		// 格式化开始时间
		String date = dateTrans(begin).substring(0, 8) + "000000";
		Date dateBegin = dateTrans(date);
		
		DataBaseHelper.execute(sql,pkSettle,pkPv,dateTrans(date),end);	
		
	}

	private void setDepoInfo(BlDeposit fromSettle, BlSettle stVo) {
		fromSettle.setPkDepo(NHISUUID.getKeyId());
		fromSettle.setFlagSettle("1");
		fromSettle.setEuPvtype("3");
		fromSettle.setPkSettle(stVo.getPkSettle());
		fromSettle.setCodeDepo(ApplicationUtils.getCode("0606"));
		fromSettle.setDatePay(stVo.getDateSt());
	}

	private void updDepositInfo(String pkPv,String pkSettle,Date begin,Date end) {
		String sql = "update bl_deposit set flag_settle = '1', pk_settle = ? where eu_dptype = '9' and flag_settle <> 1 and pk_pv = ? and date_pay >= ? and date_pay <= ?";
		DataBaseHelper.execute(sql,pkSettle,pkPv,begin,end);		
	}
	
	private void updDepositInfo(String pkPv, String pkSettle, Date begin,
			Date end, List<String> pkDepoList, String isStPre, String euSttype) {
		// 成伟要求：操作bl_deposit表只使用pk_pv字段查询。
		StringBuffer sql = new StringBuffer(
				"update bl_deposit set flag_settle = '1', pk_settle = ? where eu_dptype = '9' and flag_settle <> 1 and pk_pv = ? ");
		// 系统参数BL0040值为0时，判断有无选择指定预交金信息参与结算，如果有，只更新选择的预交金信息
		if (!CommonUtils.isEmptyString(isStPre) && isStPre.equals("0")
				&& !CommonUtils.isEmptyString(euSttype) && euSttype.equals("1")) {
			if (pkDepoList != null && pkDepoList.size() > 0) {
				sql.append(" and pk_depo in (");
				for (int i = 0; i < pkDepoList.size(); i++) {
					if (i == 0) {
						sql.append("'" + pkDepoList.get(i).trim() + "'");
					} else {
						sql.append(",'" + pkDepoList.get(i).trim() + "'");
					}
				}
				sql.append(" ) ");
			} else {
				sql.append(" and 1=0 ");
			}
		}
		DataBaseHelper.execute(sql.toString(), pkSettle, pkPv);
	}

	/**
	 * 
	 * @param pkPv 患者就诊
	 * @param euSttype 结算类型  0出院结算，1中途结算
	 * @param
	 * @return
	 */
	public Map<String, Object> settleData(SettleInfo settleInfo,String pkPv, String euSttype,String dateBegin,
 String dateEnd, IUser user, String euStresult,List<String> pkDepoList) {
		// 1.1查询患者就诊信息
		PvEncounter pvvo = DataBaseHelper.queryForBean(
				"select * from pv_encounter where pk_pv = ?",
				PvEncounter.class, pkPv);
		Date dateEndTime = dateTrans(dateEnd);
		User userInfo = (User) user;
		BlSettle blSettle = new BlSettle();
		blSettle.setDateBegin(dateTrans(dateBegin));
		blSettle.setPkPv(pkPv);// 当前患者就诊主键
		// 预缴金额AmtPrep
		// 校验系统参数BL0040(0:不结转预交金 1:结转预交金)
		String isStPre = ApplicationUtils.getSysparam("BL0040", false);
		StringBuffer sql = new StringBuffer("select sum(depo.amount)  AmtPrep from bl_deposit depo where depo.eu_dptype=9 and depo.flag_settle='0' and depo.pk_pv = ? ");
		
		// 系统参数BL0040值为0时，判断有无选择指定预交金信息参与结算，如果有，只更新选择的预交金信息
		if (!CommonUtils.isEmptyString(isStPre) && isStPre.equals("0") && 
				!CommonUtils.isEmptyString(euSttype) && euSttype.equals("1")) {
			if (pkDepoList != null && pkDepoList.size() > 0) {
				sql.append(" and pk_depo in (");
				for (int i = 0; i < pkDepoList.size(); i++) {
					if (i == 0) {
						sql.append("'" + pkDepoList.get(i).trim() + "'");
					} else {
						sql.append(",'" + pkDepoList.get(i).trim() + "'");
					}
				}
				sql.append(" ) ");
			} else {
				sql.append(" and 1=0 ");
			}
		}
		Map<String, Object> amtPrepMap = DataBaseHelper
				.queryForMap(
						sql.toString(),
						pkPv);
		BigDecimal amtPrep = amtTrans(amtPrepMap.get("amtprep"));
		blSettle.setAmountPrep(amtPrep);
		// 1.生成结算信息

		blSettle.setPkSettle(NHISUUID.getKeyId());
		//结算编码
		if(CommonUtils.isEmptyString(blSettle.getCodeSt())){
			blSettle.setCodeSt(ApplicationUtils.getCode("0605"));
		}
		blSettle.setPkOrg(userInfo.getPkOrg());// 当前机构
		blSettle.setPkPi(pvvo.getPkPi());// 当前患者主键
		blSettle.setEuPvtype("3");// 就诊类型
		blSettle.setPkInsurance(pvvo.getPkInsu());// 患者主医保

		// 结算类型
		if ("0".equals(euSttype)) {
			blSettle.setDtSttype("10"); // 结算类型--出院结算
			blSettle.setDateEnd(pvvo.getDateEnd());
		} else if("1".equals(euSttype)) {
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

		blSettle.setAmountRound(settleInfo.getAmtRound()!=null?settleInfo.getAmtRound():new BigDecimal(0D));
		blSettle.setAmountPrep(settleInfo.getAmountPrep()); //患者预交金合计
		blSettle.setAmountSt(settleInfo.getAmountSt());//患者费用合计
		blSettle.setAmountPi(settleInfo.getAmountPi());//患者自付合计
		//blSettle.setAmountInsu(settleInfo.getAmountInsuThird()==null?BigDecimal.ZERO:settleInfo.getAmountInsuThird());//第三方医保接口返回的医保支付
		blSettle.setAmountInsu(BigDecimal.valueOf(MathUtils.sub(settleInfo.getAmountSt().doubleValue(), settleInfo.getAmountPi().doubleValue())));
		BigDecimal amtAcc = settleInfo.getAmountAcc();
		
		if(amtAcc!=null && amtAcc.compareTo(BigDecimal.ZERO)>0){
			//调用患者账户服务
			patiAccountChange(amtAcc,pvvo);
		}

		//计算特诊总费用
		// 计算特诊总费用
		String begin = dateBegin.substring(0, 8) + "000000";// 格式化开始时间
		Double amountAdd = blIpPubMapper.qryAmountAddByPv(null, pkPv, dateTrans(begin), 
				"0".equals(euSttype) ? dateTrans("20991231235959") : dateEndTime);
		blSettle.setAmountAdd(amountAdd);
		
		blSettle.setFlagCanc("0");
		blSettle.setReasonCanc(null);
		blSettle.setPkSettleCanc(null);
		blSettle.setFlagArclare("0");
		blSettle.setFlagCc("0");
		blSettle.setPkCc(null);
		// 2.生成结算明细 并处理相关金额赋值
		List<BlSettleDetail> detailVos = handleSettleDetailC(pkPv, blSettle,
				userInfo.getPkOrg(),euSttype,dateBegin,dateEnd);
		Map<String, Object> resMap = new HashMap<String, Object>();
		resMap.put("settle", blSettle);
		resMap.put("detail", detailVos);
		return resMap;

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
	
	

private List<BlSettleDetail> handleSettleDetailC(String PkPv, BlSettle blSettle,String pkOrg,String euSttype,String begin,String end){
		Map<String,BlSettleDetail> res = new HashMap<String,BlSettleDetail>();
	//1.查询记费表的数据，组织记费阶段的数据	
	Map<String,Object> params = new HashMap<String,Object>();
	params.put("pkPv", PkPv);
	params.put("dateBegin", begin);
	params.put("dateEnd", end);
	params.put("euSttype", euSttype);
	List<BlIpDt> cgVos = blIpPubMapper.qryCgIps(params);	
	Double amtDisc = 0.0;
	Double amtHp = 0.0;
	Double  patiSelf = 0.0;
	for(BlIpDt vo : cgVos){
		Double amtHpTemp = 0.0; 
		if(1.0 != vo.getRatioSelf()){ //计算内部优惠金额
//			Double price = MathUtils.sub(vo.getPriceOrg(), vo.getPrice());  
//			amtHpTemp= MathUtils.mul(price, vo.getQuan());			
			
			amtHpTemp = MathUtils.sub(vo.getAmount(), vo.getAmountPi());  
			amtHp = MathUtils.add(amtHp.doubleValue(), amtHpTemp);
		} else if(1.0 != vo.getRatioDisc()) {//计算内部医保报销金额
			//amtHpTemp = MathUtils.mul(MathUtils.mul(vo.getQuan(), vo.getPrice()), MathUtils.sub(1.0, vo.getRatioSelf()));  
			
			amtHpTemp = MathUtils.sub(vo.getAmount(), vo.getAmountPi());
			amtDisc =MathUtils.add(amtDisc,amtHpTemp);
		}
//		amtHp = MathUtils.add(amtHp.doubleValue(), amtHpTemp);
//		amtDisc =MathUtils.add(amtDisc, MathUtils.sub(MathUtils.sub(vo.getAmount(),vo.getAmountPi()),amtHpTemp));
		patiSelf = MathUtils.add(patiSelf, vo.getAmountPi()); 
	}
	
	 PvEncounter pvvo =  DataBaseHelper.queryForBean("Select * from pv_encounter where pk_pv = ?", PvEncounter.class, PkPv);
	 List<BlSettleDetail> resvo = new ArrayList<BlSettleDetail>();
    
    
	//2.1处理患者优惠的记费明细数据
		if(amtDisc>0){
			BlSettleDetail voDisc = new BlSettleDetail();
			voDisc.setAmount(amtDisc.doubleValue());			
			StringBuffer sqlCate  =new StringBuffer(" select picate.pk_hp, bh.pk_payer, bh.eu_hptype from pi_cate picate");
			sqlCate.append(" inner join bd_hp bh on bh.pk_hp = picate.pk_hp ");
			sqlCate.append(" where picate.pk_picate = ?"); 
			List<Map<String,Object>> discHpMap = DataBaseHelper.queryForList(sqlCate.toString(), pvvo.getPkPicate());
			String pkPayer = "";
			String pkHp = "";
			if(discHpMap!=null && discHpMap.size()>0){
				Map<String,Object> tempDisc = discHpMap.get(0);
				if(tempDisc.get("pkPayer")!=null){
					pkPayer = (String)tempDisc.get("pkPayer");
				}
				if(tempDisc.get("pkHp")!=null){
					pkHp = (String)tempDisc.get("pkHp");
				}
			}
			voDisc.setPkSettle(blSettle.getPkSettle());
			voDisc.setPkPayer(pkPayer);
			voDisc.setPkInsurance(pkHp);	
			res.put(pkHp, voDisc);
		}
	 List<Map<String,Object>> hpPlans = getHpPlans(PkPv);//1.获取患者的医保列表
	 if(hpPlans==null || hpPlans.size()<=0){  
		
		 resvo.addAll(res.values());
		 return resvo;
	 }
	 Map<String,Object> majorHp = null;
	 for(Map<String,Object> map : hpPlans){
		 String flagMaj = "";
		 if(map.get("flagMaj")!=null){
			 flagMaj = (String)map.get("flagMaj");
		 }
		 if("1".equals(flagMaj)){
			 majorHp = map;
			 break;
		 }
	 }
	 
	//2.2处理内部医保的记费明细数据
	if(amtHp>0 && majorHp !=null){
		BlSettleDetail voHp = new BlSettleDetail();
		voHp.setAmount(amtHp.doubleValue());
		voHp.setPkSettle(blSettle.getPkSettle());
		String pkPayer = (majorHp!=null && majorHp.get("pkPayer")!=null)?(String)majorHp.get("pkPayer"):"";
		String pkHp   =(majorHp!=null && majorHp.get("pkHp")!=null)?(String)majorHp.get("pkHp"):"";
		voHp.setPkPayer(pkPayer);
		voHp.setPkInsurance(pkHp);
		res.put(pkHp, voHp);
	}
	
	//3.循环处理内部医保的结算策略和<=3的各类型医保
	 for(Map<String,Object> map : hpPlans){
		 //3.1数据准备			
		String pkPayer = (map!=null && map.get("pkPayer")!=null)?(String)map.get("pkPayer"):"";
		String pkHp   =(map!=null && map.get("pkHp")!=null)?(String)map.get("pkHp"):"";
			
		
	     int EuHptype = -1;	 
		 if(map.get("euHptype")!=null){
			 EuHptype = Integer.parseInt((String)map.get("euHptype"));
		 }
		 
		 //3.2处理结算策略的各类数据
		 if(EuHptype > 3){//该情况下可出现4,9
			BigDecimal amountInnerD =priceStrategyService.qryPatiSettlementAmountAllocationInfo(new BigDecimal(patiSelf),pvvo.getPkOrg(),Constant.IP,pkHp);		
			if(patiSelf > amountInnerD.doubleValue()){
				//返回的自付金额要是比当前的小，则是有分摊的，故写入一些明细记录
				BlSettleDetail voInner = null;
				Double hpTemp = MathUtils.sub(patiSelf, amountInnerD.doubleValue());
				if(res.get(pkHp)!=null){
					voInner = res.get(pkHp);
					voInner.setAmount(MathUtils.add(voInner.getAmount(), hpTemp));
				}else{
					voInner = new BlSettleDetail();
					voInner.setAmount(hpTemp);
					voInner.setPkSettle(blSettle.getPkSettle());
					voInner.setPkInsurance(pkHp);
					voInner.setPkPayer(pkPayer);
				}
				res.put(pkHp, voInner);
				patiSelf = amountInnerD.doubleValue();
			}	
		 }else{
			    Map<String, Object> hpMap = hpService.getHpInfo(patiSelf,map);
				BigDecimal amtHp3 =hpMap.get("amtHps") == null?BigDecimal.ZERO:(BigDecimal)hpMap.get("amtHps");				
				if(amtHp3.compareTo(BigDecimal.ZERO)>0){
					BlSettleDetail voHp3 =  null;
					if(res.get(pkHp)!=null){
						voHp3 = res.get(pkHp);
						voHp3.setAmount(MathUtils.add(amtHp3.doubleValue(), voHp3.getAmount()));
						//给主表赋值
						blSettle.setAmountInsu(amtHp3.add(blSettle.getAmountInsu()));
					}else{
						voHp3 =new BlSettleDetail();
						voHp3.setAmount(amtHp3.doubleValue());
						voHp3.setPkPayer(pkPayer);
						voHp3.setPkSettle(blSettle.getPkSettle());
						voHp3.setPkInsurance(pkHp);
						//给主表赋值
						blSettle.setAmountInsu(amtHp3);	
					}
					res.put(pkHp, voHp3);	
					patiSelf =MathUtils.sub(patiSelf, amtHp3.doubleValue());
				}
		 }
		 
	 }
		
		
	//4.结算时患者自付金额。
	BlSettleDetail voSelf = new BlSettleDetail();
	voSelf.setAmount(patiSelf.doubleValue());
	voSelf.setPkSettle(blSettle.getPkSettle());
	voSelf.setPkInsurance(null);
	//当保险计划为空时，付款方应该也为空
	voSelf.setPkPayer(qryBdPayerByEuType(pkOrg));
	res.put("pkSelf",voSelf);
		
	//5.给主表赋值
	//blSettle.setAmountPi(new BigDecimal(patiSelf).setScale(2, BigDecimal.ROUND_HALF_UP));//不保留两位小数，保存的时候会报错
	resvo.addAll(res.values());
	
	return resvo;
	}

	public static void setDefaultValue(Object obj, boolean flag) {
		
		User user = UserContext.getUser();
	
		Map<String,Object> default_v = new HashMap<String,Object>();
		if(flag){  // 如果新增
			default_v.put("pkOrg", user.getPkOrg());
			default_v.put("creator", user.getPkEmp());
			default_v.put("createTime",new Date());
			default_v.put("delFlag", "0");
		}
		
		default_v.put("ts", new Date());
		default_v.put("modifier",  user.getPkEmp());
		
		Set<String> keys = default_v.keySet();
		
		for(String key : keys){
			Field field = ReflectHelper.getTargetField(obj.getClass(), key);
			if (field != null) {
				ReflectHelper.setFieldValue(obj, key, default_v.get(key));
			}
		}
	
	}
	
	
	public static void piAccDetailVal(BlDeposit dp){
		
		String getPiA="Select * from pi_acc where pk_pi=? and (del_flag='0' or del_flag is null)";
		PiAcc pa=DataBaseHelper.queryForBean(getPiA, PiAcc.class, dp.getPkPi());
		if(pa!=null&&EnumerateParameter.ONE.equals(pa.getEuStatus())){
			if(pa.getAmtAcc()==null||"".equals(pa.getAmtAcc())){
				pa.setAmtAcc(BigDecimal.ZERO);
			}
			BigDecimal amtAcc=pa.getAmtAcc().add(dp.getAmount());
			if(amtAcc.compareTo(BigDecimal.ZERO)<0){
				throw new BusException("信用额度小于0！");
			}else{
				pa.setAmtAcc(amtAcc);
			}
			DataBaseHelper.updateBeanByPk(pa,false);
			PiAccDetail pad=new PiAccDetail();
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
		}else{
			throw new BusException("该账户已冻结或已被删除，不可收退款");
		}
		
		}
	/**
	 * 查询发票信息
	 * @param param
	 * @param user
	 * @return
	 */
	public BlInvPrint qryInvPrint(String param,IUser user){
		Map<String,String> paraMap = JsonUtil.readValue( param, Map.class); 
		String pkSettle = paraMap.get("pkSettle");
		BlInvPrint  bllnvPrint = new BlInvPrint();
		//查询住院发票信息
		bllnvPrint = InvPrintProcessUtils.getIpInvPrintDataByPkSettle(pkSettle);
		return  bllnvPrint;
	}


	private BlInvPrint getPrintDataByPkSettle(String pkSettle) {
		BlInvPrint res = new BlInvPrint();
		if(!CommonUtils.isEmptyString(pkSettle)){
			BlSettle st = DataBaseHelper.queryForBean("SELECT * FROM bl_settle where pk_settle = ?", BlSettle.class, pkSettle);
			List<BlSettleDetail> blSettleDetail = DataBaseHelper.queryForList(" SELECT * FROM Bl_Settle_Detail WHERE pk_settle = ?", BlSettleDetail.class, pkSettle);
			List<BlStInv> stInvs = DataBaseHelper.queryForList("SELECT * FROM bl_st_inv where pk_settle = ?", BlStInv.class, pkSettle);
			List<BlDeposit> blDepositList =  DataBaseHelper.queryForList("SELECT * FROM bl_deposit WHERE pk_settle = ? and (eu_dptype = '0' or eu_dptype = '1')", BlDeposit.class, pkSettle);
			List<BlInvoice> inv = new ArrayList<BlInvoice>();
			for(BlStInv stInv : stInvs){
					BlInvoice invvo = DataBaseHelper.queryForBean(" SELECT * FROM bl_invoice WHERE flag_cancel= 0 and pk_invoice = ?", BlInvoice.class, stInv.getPkInvoice());
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
	public BlInvPrint stInvReprint(String param,IUser user){
		
		BlInvPrint res = new BlInvPrint();
		Map<String,Object> paraMap = JsonUtil.readValue( param, Map.class); 
		String pkSettle = (String)paraMap.get("pkSettle");
		List<String> codeInv = (List<String>) paraMap.get("codeInv");
		String pkEmpinv = (String)paraMap.get("pkEmpinv");
		BlInvPrint old = getPrintDataByPkSettle(pkSettle);
		
		Map<String,Object> rePrintMap = new HashMap<>();//重打发票个性化服务入参
//		调用发票查询服务，判断是否有足够的发票张数，如果不够，提示用户“发票剩余数量不足，请设置!”；
//		作废原发票号码；
		for(BlInvoice inv : old.getBlInvoice()){
			DataBaseHelper.execute("update bl_invoice  set flag_cancel = 1,date_cancel = ?,"
					+ "  pk_emp_cancel = ?,name_emp_cancel = ?"
					+ " where code_inv = ?", new Date(),UserContext.getUser().getPkEmp(),
					UserContext.getUser().getNameEmp(),inv.getCodeInv());
		}

		List<BlInvoice> newInvs = old.getBlInvoice();
		List<BlInvoiceDt> newInvDts = new ArrayList<BlInvoiceDt>();
		List<BlStInv> stInvs =  new ArrayList<BlStInv>();
		for(int i = 0;i<newInvs.size();i++){
			BlInvoice newVo = newInvs.get(i);
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
		DataBaseHelper.batchUpdate(DataBaseHelper.getInsertSql(BlInvoice.class), newInvs);
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
		 * 处理外部接口支付数据
		 * @param pkDepo
		 * @param blExtPay
		 */
		private void saveBlExtPay(String pkDepo, BlExtPay blExtPay){
			String sql="update BL_EXT_PAY set PK_DEPO= ?  where PK_EXTPAY = ?  ";

			DataBaseHelper.update( sql, new Object[]{pkDepo,blExtPay.getPkExtpay()});
		}
		
		/**
		 * 结算成功后，医保结算记录添加nhis结算主键
		 * @param pkSettle
		 * @param
		 */
		private void updateInsSt(String pkSettle, String pkInsSt){
			//医保结算数据添加nhis结算主键
			InsZsybSt insSt = DataBaseHelper.queryForBean(
					"select * from ins_st where pk_insst = ?",
					InsZsybSt.class, pkInsSt);
			if(insSt!=null){
				insSt.setPkSettle(pkSettle);
				DataBaseHelper.updateBeanByPk(insSt, false);
				//医保结算记录对应的项目数据关联nhis结算主键
				String sql = "update ins_st_itemcate set pk_settle = ? where pk_insst = ?";
				DataBaseHelper.execute(sql,pkSettle,insSt.getPkInsst());	
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
		 * 查询费用分类信息
		 * @param param
		 */
		public List<Map<String,Object>> queryChargeClassify(String param,IUser user){
			Map<String,Object> paraMap = JsonUtil.readValue( param, Map.class); 
			if(paraMap == null){
				paraMap = new HashMap<String,Object>();
	    	}
	    	if(CommonUtils.isNotNull(paraMap.get("dateBegin"))){
	    		paraMap.put("dateBegin", CommonUtils.getString(paraMap.get("dateBegin")).substring(0, 8)+"000000");
			}
			if(CommonUtils.isNotNull(paraMap.get("dateEnd"))){
				paraMap.put("dateEnd", CommonUtils.getString(paraMap.get("dateEnd")).substring(0, 8)+"235959");
			}
			return blIpSettleQryMapper.queryChargeClassify(paraMap);
		}
		
		/**
		 * 查询费用项目信息
		 * @param param
		 */
		public List<Map<String,Object>> queryChargeItem(String param,IUser user){
			Map<String,Object> paraMap = JsonUtil.readValue( param, Map.class); 
			if(paraMap == null){
				paraMap = new HashMap<String,Object>();
	    	}
	    	if(CommonUtils.isNotNull(paraMap.get("dateBegin"))){
	    		paraMap.put("dateBegin", CommonUtils.getString(paraMap.get("dateBegin")).substring(0, 8)+"000000");
			}
			if(CommonUtils.isNotNull(paraMap.get("dateEnd"))){
				paraMap.put("dateEnd", CommonUtils.getString(paraMap.get("dateEnd")).substring(0, 8)+"235959");
			}
			return blIpSettleQryMapper.queryChargeItem(paraMap);
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
			return blIpSettleQryMapper.queryChargePrePay(paraMap);
		}
		
		/**
		 * 查询患者未结算项目
		 * @param param
		 */
		public List<Map<String,Object>> getPatientChargeItem(String param,IUser user){
			Map<String,Object> paraMap = JsonUtil.readValue( param, Map.class); 
			if(paraMap == null){
				paraMap = new HashMap<String,Object>();
	    	}
	    	if(CommonUtils.isNotNull(paraMap.get("dateEnd"))){
	    		paraMap.put("dateEnd", CommonUtils.getString(paraMap.get("dateEnd")).substring(0, 8)+"235959");
			}
			if(CommonUtils.isNotNull(paraMap.get("pkPv"))){
				paraMap.put("pkPv", paraMap.get("pkPv").toString());
			}
			return blIpSettleQryMapper.getPatientChargeItem(paraMap);
		}
		/**
		 * 交易号：007003003026
		 * 根据患者就诊主键查询待结算项目
		 * @param param
		 * @param user
		 * @return
		 */
		public List<Map<String,Object>> qryWaitChargeItem(String param,IUser user){
			Map<String,Object> paraMap = JsonUtil.readValue( param, Map.class); 
			
			return blIpSettleQryMapper.qryWaitChargeItem(paraMap);
		}
		
		/**
		 * 交易号：007003003032
		 * 查询费用分类信息
		 * @param param
		 * @param user
		 * @return
		 */
		public List<Map<String,Object>> qryAmtCate(String param,IUser user){
			@SuppressWarnings("unchecked")
			Map<String,Object> paraMap = JsonUtil.readValue( param, Map.class); 
			String euSttype = (String)paraMap.get("euSttype");

			//查询中途结算费用明细pk
			if(euSttype.equals("11")){
				if(CommonUtils.isNotNull(paraMap.get("dateBegin"))){
		    		paraMap.put("dateBegin", CommonUtils.getString(paraMap.get("dateBegin")).substring(0, 8)+"000000");
				}
				if(CommonUtils.isNotNull(paraMap.get("dateEnd"))){
					paraMap.put("dateEnd", CommonUtils.getString(paraMap.get("dateEnd")).substring(0, 8)+"235959");
				}
			}
			
			//查询费用分类信息
			List<Map<String,Object>> resMap = blIpSettleQryMapper.qryAmtCate(paraMap);
			return resMap;
		}
		
		/**
		 * 交易号：007003003033
		 * 查询费用分类明细信息
		 * @param param
		 * @param user
		 * @return
		 */
		public List<Map<String,Object>> qryCateDtlsInfo(String param,IUser user){
			@SuppressWarnings("unchecked")
			Map<String,Object> paraMap = JsonUtil.readValue( param, Map.class); 
			
			String euSttype = (String)paraMap.get("euSttype");

			//查询中途结算费用明细pk
			if(euSttype.equals("11")){
				if(CommonUtils.isNotNull(paraMap.get("dateBegin"))){
		    		paraMap.put("dateBegin", CommonUtils.getString(paraMap.get("dateBegin")).substring(0, 8)+"000000");
				}
				if(CommonUtils.isNotNull(paraMap.get("dateEnd"))){
					paraMap.put("dateEnd", CommonUtils.getString(paraMap.get("dateEnd")).substring(0, 8)+"235959");
				}
			}
			String sysParam = ApplicationUtils.getSysparam("BL0034", false);
			
			//查询费用分类明细信息
			List<Map<String,Object>> resMap = new ArrayList<>();
			//获取系统参数[BL0034]是否可以修改费用明细的自付比例
			if(paraMap.containsKey("flagSum") && CommonUtils.isNotNull(paraMap.get("flagSum"))
					&& "0".equals(paraMap.get("flagSum").toString())){
				//查询费用分类明细信息(汇总查询)
				resMap = blIpSettleQryMapper.qrySumCateDtlsInfo(paraMap);
			}else{
				//查询费用分类明细信息
				resMap = blIpSettleQryMapper.qryCateDtlsInfo(paraMap);
			}
			
			return resMap;
		}
		
		/**
		 * 交易号：007003003035
		 * 获取患者特诊标志
		 * @param param
		 * @param user
		 * @return
		 */
		public String qryPvFlagSpec(String param,IUser user){
			String pkPv = JsonUtil.getFieldValue(param, "pkPv");
			Map<String,Object> retMap = DataBaseHelper.queryForMap(
					"select flag_spec from PV_ENCOUNTER where pk_pv=?"
					, pkPv);
			String flagSpec = "";
			if(retMap!=null && retMap.size()>0 && retMap.get("flagSpec")!=null)
				flagSpec = CommonUtils.getString(retMap.get("flagSpec"));
			return flagSpec;
		}
		
		/**
		 * 交易号：007003003043
		 * 根据住院自付比例修改住院收费明细信息
		 * @param param
		 * @param user
		 */
		public void updateIpDtInfo(String param,IUser user){
			List<BlIpDt> dtList = JsonUtil.readValue(param, new TypeReference<List<BlIpDt>>() {
			});
			
			//修改计费明细信息
			DataBaseHelper.batchUpdate(DataBaseHelper.getUpdateSql(BlIpDt.class), dtList);
			Set<String> pkList = new HashSet<String>();
			
			//如果为公医患者则同步修改ins_gzgy_bl
			for(BlIpDt vo : dtList){
				pkList.add(vo.getPkCgip());
			}
			
			String sql = "select * from ins_gzgy_bl where pk_cg in ("+CommonUtils.convertSetToSqlInPart(pkList, "pk_cg")+")";
			
			List<InsGzgyBl> blList= DataBaseHelper.queryForList(sql, InsGzgyBl.class, new Object[]{});
			if(blList!=null && blList.size()>0){
				for(InsGzgyBl gzgyBl : blList){
					for(BlIpDt dt : dtList){
						if(gzgyBl.getPkCg().equals(dt.getPkCgip())){
							gzgyBl.setRatio(dt.getRatioSelf());
							gzgyBl.setAmountPi(dt.getAmountHppi());
							gzgyBl.setAmountHp(dt.getAmountPi());
						}
					}
				}
				//更新ins_gzgy_bl
				DataBaseHelper.batchUpdate(DataBaseHelper.getUpdateSql(InsGzgyBl.class), blList);
			}
		}
		
		/**
		 * 查询患者结算记录
		 * @param param
		 * @param user
		 * @return
		 */
		public Map<String, Object> qryBlSettles(String param,IUser user) {
			Map<String,Object> map = JsonUtil.readValue(param, Map.class);
			if (map==null) 
				return null;
			String pkPv = (String)map.get("pkPv");
			if(!org.apache.commons.lang3.StringUtils.isNotBlank(pkPv))
				return null;
			Map<String, Object> PiChanged = new HashMap<String,Object>();
			PiChanged.put("pvDiags", blIpSettleQryMapper.qryPvDiags(pkPv));
			List<BlSettleVo> blSettles = blIpSettleQryMapper.qryBlSettles(pkPv);
			
			Map<String, String> qryPkOrg = blIpSettleQryMapper.qryPkOrg((String)map.get("pkPi"));
			map.put("pkOrg",qryPkOrg.get("pkOrg"));
			PiChanged.put("unitWork", qryPkOrg.get("unitWork"));
			if (blSettles==null||blSettles.size()<1 || 
					(map.containsKey("flagIn") && !CommonUtils.isEmptyString(CommonUtils.getString(map.get("flagIn"))) && "1".equals(CommonUtils.getString(map.get("flagIn"))) )) {
				map.put("num", 0);
				if (map.get("pkSettle")==null) 
					map.put("pkSettle", "");
				List<BlIpDtsVo> BlIpDts = blIpSettleQryMapper.qryBlIpDtCount(map);
				//通过pkpv查出该患者对应的所有费用明显
				List<BlIpDtsVo> blIpDtList =  blIpSettleQryMapper.qryBlIpDtInfo(map);
				if (BlIpDts!=null&&BlIpDts.size()>0) {
					for (BlIpDtsVo BlIpDtmap : BlIpDts) {
						if(blIpDtList!=null&&blIpDtList.size()>0){
							List<BlIpDtsVo> blList = new ArrayList<>();
							for(BlIpDtsVo vo :blIpDtList){
								BigDecimal data1 = new BigDecimal(vo.getRatioSelf());
								BigDecimal data2 = new BigDecimal(BlIpDtmap.getRatioSelf());
								if(org.apache.commons.lang3.StringUtils.equals(vo.getPkItemcate(),BlIpDtmap.getPkItemcate())
										&&data1.compareTo(data2)==0){
                                    //如果没有pkSettle，就放的是未结算的费用明细
									if(org.apache.commons.lang3.StringUtils.isBlank(BlIpDtmap.getPkSettle())){
										if("0".equals(vo.getFlagSettle())){
											blList.add(vo);
										}
									}else{
										if(org.apache.commons.lang3.StringUtils.equals(vo.getPkSettle(),BlIpDtmap.getPkSettle())){
											blList.add(vo);
										}
									}
								}

							}
							BlIpDtmap.setBlIpDtInfo(blList);
						}
					}
				}
				PiChanged.put("blDeposits",blIpSettleQryMapper.qryBlDeposits(map));
				PiChanged.put("blIpDts",BlIpDts);
			}else{
				PiChanged.put("blSettles",blSettles);
				map.put("num", 1);
				map.put("pkSettle", blSettles.get(0).getPkSettle());
				List<BlIpDtsVo> BlIpDts = blIpSettleQryMapper.qryBlIpDtCount(map);
				//通过pkpv查出该患者对应的所有费用明显
				List<BlIpDtsVo> blIpDtList =  blIpSettleQryMapper.qryBlIpDtInfo(map);
				if (BlIpDts!=null&&BlIpDts.size()>0) {
					for (BlIpDtsVo BlIpDtmap : BlIpDts) {
						if(blIpDtList!=null&&blIpDtList.size()>0){
							List<BlIpDtsVo> blList = new ArrayList<>();
							for(BlIpDtsVo vo :blIpDtList){
								BigDecimal data1 = new BigDecimal(vo.getRatioSelf());
								BigDecimal data2 = new BigDecimal(BlIpDtmap.getRatioSelf());
								if(org.apache.commons.lang3.StringUtils.equals(vo.getPkItemcate(),BlIpDtmap.getPkItemcate())
										&&data1.compareTo(data2)==0){
									//如果没有pkSettle，就放的是未结算的费用明细
									if(org.apache.commons.lang3.StringUtils.isBlank(BlIpDtmap.getPkSettle())){
										if("0".equals(vo.getFlagSettle())){
											blList.add(vo);
										}
									}else{
										if(org.apache.commons.lang3.StringUtils.equals(vo.getPkSettle(),BlIpDtmap.getPkSettle())){
											blList.add(vo);
										}
									}
								}

							}
							BlIpDtmap.setBlIpDtInfo(blList);
						}
					}
				}
				
				PiChanged.put("blIpDts",BlIpDts);
				PiChanged.put("blStInvs",blIpSettleQryMapper.qryBlStInv((String)map.get("pkSettle")));
				PiChanged.put("blDeposits",blIpSettleQryMapper.qryBlDeposits(map));
				PiChanged.put("blDepositInfos",blIpSettleQryMapper.qryBlDepositInfo((String)map.get("pkSettle")));
				PiChanged.put("blSettleDetails", blIpSettleQryMapper.qryBlSettleDetail((String)map.get("pkSettle")));
				
			}
			
			return PiChanged;
		}
		
		
		/**
		 * 查询结算各类明细
		 * @param param
		 * @param user
		 * @return
		 */
		public Map<String, Object> qryPiChanged(String param,IUser user) {
			Map map = JsonUtil.readValue(param, Map.class);
			if (map==null) 
				return null;
			String pkPv = (String)map.get("pkPv");
			map.put("num", "1");
			map.put("pkOrg",blIpSettleQryMapper.qryPkOrgByPkPv(pkPv));
			Map<String, Object> PiChanged = new HashMap<String,Object>();
			ArrayList<String> list = new ArrayList<String>();
			List<BlIpDtsVo> BlIpDts = blIpSettleQryMapper.qryBlIpDts(map);
			//通过pkpv查出该患者对应的所有费用明显
			List<BlIpDtsVo> blIpDtList =  blIpSettleQryMapper.qryBlIpDtInfo(map);
			if (BlIpDts!=null&&BlIpDts.size()>0) {
				for (BlIpDtsVo BlIpDtmap : BlIpDts) {
					if(blIpDtList!=null&&blIpDtList.size()>0){
						List<BlIpDtsVo> blList = new ArrayList<>();
						for(BlIpDtsVo vo :blIpDtList){
							BigDecimal data1 = new BigDecimal(vo.getRatioSelf());
							BigDecimal data2 = new BigDecimal(BlIpDtmap.getRatioSelf());
							if(org.apache.commons.lang3.StringUtils.equals(vo.getPkItemcate(),BlIpDtmap.getPkItemcate())
									&&data1.compareTo(data2)==0){
								//如果没有pkSettle，就放的是未结算的费用明细
								if(org.apache.commons.lang3.StringUtils.isBlank(BlIpDtmap.getPkSettle())){
									if("0".equals(vo.getFlagSettle())){
										blList.add(vo);
									}
								}else{
									if(org.apache.commons.lang3.StringUtils.equals(vo.getPkSettle(),BlIpDtmap.getPkSettle())){
										blList.add(vo);
									}
								}
							}

						}
						BlIpDtmap.setBlIpDtInfo(blList);
					}
				}
			}
			
			PiChanged.put("blIpDts",BlIpDts);
			PiChanged.put("blStInvs",blIpSettleQryMapper.qryBlStInv((String)map.get("pkSettle")));
			PiChanged.put("blDeposits",blIpSettleQryMapper.qryBlDeposits(map));
			PiChanged.put("blDepositInfos",blIpSettleQryMapper.qryBlDepositInfo((String)map.get("pkSettle")));
			PiChanged.put("blSettleDetails", blIpSettleQryMapper.qryBlSettleDetail((String)map.get("pkSettle")));
			return PiChanged;
		}
		
		/**
		 * 查询所有的就诊记录
		 * @param param
		 * @param user
		 * @return
		 */
		public List<PvEncounterVo> qryPvEncounters(String param,IUser user) {
			Map map = JsonUtil.readValue(param, Map.class);
			if (map==null) 
				return null;
			List<PvEncounterVo> pvList = new ArrayList<>();
			if(Application.isSqlServer()){
				pvList = blIpSettleQryMapper.qryPvInfoSqlServer(map);
			}else{
				pvList = blIpSettleQryMapper.qryPvEncounters(map);
			}
			return pvList;
		}
		
		/**
		 * 查询发票信息
		 * @return
		 */
		public List<List<Map<String, Object>>> qryInvoice(String param,IUser user){
			Map map = JsonUtil.readValue(param, Map.class);
			if (map==null) 
				return null;
			if(map.get("pkSettle") == null)
				return null;
			String pkSettle = (String)map.get("pkSettle");
			if(!org.apache.commons.lang3.StringUtils.isNotBlank(pkSettle))return null;
			
			List<String> list = blIpSettleQryMapper.qryPkInvoices(pkSettle);
			if(list!=null&&list.size()>0){
				List<List<Map<String, Object>>>  qryInvoices = new ArrayList<>();
				for (String string : list) {
					List<Map<String,Object>> qryInvoice = blIpSettleQryMapper.qryInvoice(string);
					qryInvoices.add(qryInvoice);
				}
				 return qryInvoices;
			}
			return null;
		}
		
		public Map<String, Object> qrySelfFundedMedicine(String param,IUser user){
			Map map = JsonUtil.readValue(param, Map.class);
			if (map==null) 
				return null;
			if(map.get("pkSettle") == null||map.get("pkPv")==null)
				return null;
			HashMap<String, Object> res = new HashMap<>();
			String pkSettle = (String)map.get("pkSettle");
			String pkPv = (String)map.get("pkPv");
			//查询患者医保是否是广州公医
			String valSql = "select attr.val_attr from bd_hp hp "+
							" inner join bd_dictattr attr on hp.pk_hp=attr.pk_dict "+
							" inner join bd_dictattr_temp tmp on attr.pk_dictattrtemp=tmp.pk_dictattrtemp and tmp.dt_dicttype='03' "+
							" inner join PV_ENCOUNTER pv on hp.PK_HP = pv.PK_INSU "+
							" where pv.pk_pv = ? and tmp.code_attr='0301' ";
			Map<String,Object> retMap = DataBaseHelper.queryForMap(valSql,pkPv);
			
			String varAttr = null;
			if(retMap!=null && retMap.size()>0 && retMap.get("valAttr")!=null)
				varAttr = CommonUtils.getString(retMap.get("valAttr"));
			
			res.put("attr", varAttr);
			if(!CommonUtils.isEmptyString(varAttr) && varAttr.equals("1")){
				
				//查询公医患者费药合计
				Map<String,Object> amtMap = DataBaseHelper.queryForMap(
						"select CASE when sum(amount) is not NULL then sum(amount) ELSE 0 END limit_amt from bl_ip_dt where FLAG_PD = '1' and PK_SETTLE = ?",
						pkSettle);
			
				//查询公医患者药费报销合计
				Map<String,Object> amtDrugMap = DataBaseHelper.queryForMap(
						"select amount_ins_drug as amt_drug from ins_gzgy_st where pk_settle = ?"
						,pkSettle);
				 
				if((amtMap!=null && amtMap.size()>0)
						&& (amtDrugMap!=null && amtDrugMap.size()>0)&&amtMap.get("limitAmt")!=null)
					res.put("limitamt",MathUtils.sub(CommonUtils.getDoubleObject(amtMap.get("limitAmt")), CommonUtils.getDoubleObject(amtDrugMap.get("amtDrug"))));
				else
					res.put("limitamt",0D);
			}

			return res;
		}
		
		/**
		 * 交易号：007003009001
		 * 查询待欠款结算信息
		 * @param param
		 * @param user
		 * @return
		 */
		public List<Map<String,Object>> qryDebtStInfo(String param,IUser user){
			Map<String,Object> paramMap = JsonUtil.readValue(param, Map.class);
			
			List<Map<String,Object>> ret = blIpSettleQryMapper.qryDebtStInfo(paramMap);
			
			return ret;
		}
		
		/**
		 * 交易号：007003009002
		 * 查询患者欠款结算信息
		 * @param param
		 * @param user
		 * @return
		 */
		public PvDebtStVo qryDebtStVo(String param,IUser user){
			Map<String,Object> paramMap = JsonUtil.readValue(param, Map.class);
			
			if(CommonUtils.isEmptyString(CommonUtils.getString(paramMap.get("pkPv")))){
				throw new BusException("请传入患者就诊主键pkPv!");
			}
			
			if(CommonUtils.isEmptyString(CommonUtils.getString(paramMap.get("pkSettle")))){
				throw new BusException("请传入结算主键pkSettle!");
			}
			
			paramMap.put("pkOrg", UserContext.getUser().getPkOrg());
			
			PvDebtStVo debtStVo = new PvDebtStVo();
			//查询欠款结算患者信息
			debtStVo.setPatiVo(blIpSettleQryMapper.qryDebtPatiInfo(paramMap));
			//查询欠款结算费用分类信息
			debtStVo.setCateVo(blIpSettleQryMapper.qryDebtCateAmtInfo(paramMap));
			//查询欠款结算预交金信息
			debtStVo.setPerpVo(blIpSettleQryMapper.qryDebtPerpAmtInfo(paramMap));
			
			return debtStVo;
		}
		
		/**
		 * 交易号：007003009003
		 * 保存欠费补交信息
		 * @param param
		 * @param user
		 */
		public void saveDebtSt(String param,IUser user){
			DebtSaveVo saveInfo = JsonUtil.readValue(param,DebtSaveVo.class);
			
			BlDeposit depo = saveInfo.getDepo();
			List<InvInfoVo> invoInfos = saveInfo.getInvList();
			
			if(depo==null || depo.getAmount().compareTo(BigDecimal.valueOf(0D))==0){
				throw new BusException("保存时未传入补交金额，请检查！");
			}
			//生成交款记录，写表bl_deposit
			ApplicationUtils.setDefaultValue(depo, true);
			DataBaseHelper.insertBean(depo);
			
			//写结算应收记录，写表bl_settle_ar
			BlSettleAr stAr = new BlSettleAr();
			stAr.setPkSettle(depo.getPkSettle());
			stAr.setAmtPay(CommonUtils.getDoubleObject(depo.getAmount()));
			stAr.setFlagCl("1");
			stAr.setDatePay(depo.getDatePay());
			stAr.setPkEmpPay(depo.getPkEmpPay());
			stAr.setNameEmpPay(depo.getNameEmpPay());
			stAr.setFlagCc("0");
			ApplicationUtils.setDefaultValue(stAr, true);
			DataBaseHelper.insertBean(stAr);
			
			//更新结算记录，写表bl_settle
			DataBaseHelper.update(
					"update bl_settle set flag_arclare = '1' where pk_settle = ?",
					new Object[]{depo.getPkSettle()});
			
			//查询结算信息
			BlSettle st = DataBaseHelper.queryForBean(
					"select * from bl_settle where pk_settle = ?"
					, BlSettle.class, new Object[]{depo.getPkSettle()});
			
			//保存发票信息
			if(invoInfos!=null&&invoInfos.size()>0){
				//5发票数据准备
				Map<String,Object> invMap = invSettltService.invoData(depo.getPkPv(),"0",invoInfos,user,dateTrans(st.getDateBegin()),dateTrans(st.getDateEnd()),st.getPkSettle(),"0");
				List<BlInvoice> invo = (List<BlInvoice>) invMap.get("inv");;
				List<BlInvoiceDt> invoDt = (List<BlInvoiceDt>)invMap.get("invDt");
				//5.1写发票表bl_invoice和发票明细表bl_invoice_dt
				insertInvo(invo,invoDt);
				//6.写发票和结算关系表bl_st_inv；
				insertInvoSt(invo,st);
				//7.调用发票使用确认服务，完成发票更新。
				commonService.confirmUseEmpInv(invoInfos.get(0).getInv().getPkEmpinv(), new Long(invo.size()));
			}
		}
		
		/**
		 * 交易号：007003003056
		 * 预结算完后处理BL_SETTLE表eu_prest字段
		 * @param param
		 * @param user
		 */
		public void updPerStInfo(String param,IUser user){
			String pkSettle = JsonUtil.getFieldValue(param, "pkSettle");
			
			if(CommonUtils.isEmptyString(pkSettle))
				throw new BusException("预结算时未传入pkSettle,请检查！");
			
			DataBaseHelper.execute("update bl_settle set eu_prest = '1' where pk_settle = ?", new Object[]{pkSettle});
		}
		
		/**
		 * 交易号：007003003057
		 * 查询预结再结患者
		 * @param param
		 * @param user
		 */
		public List<Map<String,Object>> qryPerStPv(String param,IUser user){
			@SuppressWarnings("unchecked")
			Map<String, Object> paraMap = JsonUtil.readValue(param, Map.class);
			String pkOrg = (String) paraMap.get("pkOrg");
			String typeDept = (String) paraMap.get("typeDept");

			StringBuffer sql = new StringBuffer(
					"select st.pk_settle,pv.pk_pv, pi.pk_pi,pv.code_pv,pi.code_ip, pi.name_pi,dept.name_dept,ip.flag_frozen,ip.dt_stway,ip.dt_sttype_ins");
			sql.append(" from pv_encounter pv");
			sql.append(" inner join pv_ip ip on pv.pk_pv=ip.pk_pv");
			sql.append(" inner join pi_master pi on pv.pk_pi=pi.pk_pi");
			sql.append(" inner join bd_ou_dept dept on pv.pk_dept=dept.pk_dept");
			sql.append(" inner join bl_settle st on st.pk_pv = pv.pk_pv ");
			sql.append(" where st.eu_stresult='0' and st.eu_prest='1' and dept.pk_org = ? and st.flag_canc!='1' ");
			//护士站增加护士站查询条件
			if("02".equals(typeDept)){
				sql.append(" and pv.pk_dept_ns=?");
			}
			
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
		 * 交易号：007003003058
		 * 查询预结再结费用分类信息
		 * @param param
		 * @param user
		 * @return
		 */
		public List<Map<String,Object>> qryPreStChargeInfo(String param,IUser user){
			Map<String,Object> paraMap = JsonUtil.readValue( param, Map.class); 
			if(paraMap == null){
				paraMap = new HashMap<String,Object>();
	    	}
	    	
			return blIpSettleQryMapper.queryChargeClassify(paraMap);
		}
		
		/**
		 * 交易号：007003003059
		 * 查询预结再结患者预缴金
		 * @param param
		 */
		public List<Map<String,Object>> qryPreStPay(String param,IUser user){
			Map<String,Object> paraMap = JsonUtil.readValue( param, Map.class); 
			if(paraMap == null){
				paraMap = new HashMap<String,Object>();
	    	}
			
			return blIpSettleQryMapper.queryChargePrePay(paraMap);
		}
		
		/**
		 * 交易号：007003003060
		 * 取消预结更新BL_SETTLE
		 * @param param
		 * @param user
		 */
		public void preStCanl(String param,IUser user){
			String pkSettle = JsonUtil.getFieldValue(param, "pkSettle");
			
			if(CommonUtils.isEmptyString(pkSettle))
				throw new BusException("取消预结时未传入pkSettle,请检查！");
			
			BlSettle stInfo = DataBaseHelper.queryForBean(
					"select * from bl_settle where PK_SETTLE_CANC = ?",
					BlSettle.class, new Object[]{pkSettle});
			
			if(stInfo!=null && 
					!CommonUtils.isEmptyString(stInfo.getPkSettleCanc())){
				String upSql = "update bl_settle set eu_prest='9',pk_emp_finish=?,name_emp_finish=?,date_finish=?  ";
				upSql += " where pk_settle in ('"+stInfo.getPkSettle()+"','"+stInfo.getPkSettleCanc()+"'"+")";
				
				DataBaseHelper.execute(
						upSql,
						new Object[]{UserContext.getUser().getPkEmp(),UserContext.getUser().getNameEmp(),new Date()});
			}
		}
		
		/**
		 * 交易号：007003003061
		 * 查询预结算信息
		 * @param param
		 * @param user
		 * @return
		 */
		public Map<String,Object> qryPreStInfo(String param,IUser user){
			String pkSettle = JsonUtil.getFieldValue(param, "pkSettle");
			
			if(CommonUtils.isEmptyString(pkSettle))
				throw new BusException("预结再结传入pkSettle,请检查！");
			
			Map<String,Object> paramMap = new HashMap<>();
			paramMap.put("pkSettle",pkSettle);
			
			return blIpSettleQryMapper.qryPreStInfo(paramMap);
		}
		
		/**
		 * 交易号:007003003062
		 * 保存预结再结收退款信息
		 * @param param
		 * @param user
		 */
		public Map<String,Object> savePreStInfo(String param, IUser user){
			SettleInfo allData = JsonUtil.readValue(param, SettleInfo.class);
			
			String pkSettle = allData.getPkSettle();
			BlExtPay blExtPay = allData.getBlExtPay();//第三方支付交易记录 xr+
			List<BlDeposit> blDepositList=allData.getDepoList();//收款退款记录-因灵璧项目含笔退款记录，故使用此字段
			
			Map<String,Object> mapReturn=new HashMap<>();
			
			BlSettle stInfo = DataBaseHelper.queryForBean(
					"select * from bl_settle where pk_settle = ?",
					BlSettle.class, new Object[]{pkSettle});
			
			if (blDepositList!=null){
				for (BlDeposit blDeposit:blDepositList){
					setDepoInfo(blDeposit,stInfo);
					blDeposit.setEuDptype("0");
					setDefaultValue(blDeposit, true);
					DataBaseHelper.insertBean(blDeposit);

					if (blExtPay!=null&&"1".equals(blDeposit.getEuDirect())){
						saveBlExtPay(blDeposit.getPkDepo(),blExtPay);
					}
				}
				mapReturn.put("blDepositList",blDepositList);
			}
			
			//更新bl_settle表
			stInfo.setEuPrest("2");
			stInfo.setPkEmpFinish(UserContext.getUser().getPkEmp());
			stInfo.setNameEmpFinish(UserContext.getUser().getNameEmp());
			stInfo.setDateFinish(new Date());
			
			DataBaseHelper.update(DataBaseHelper.getUpdateSql(BlSettle.class), stInfo);
			
			return mapReturn;
		}
		
		/**
		 * 住院结算查询--重打发票
		 * 交易号：007003008014
		 * @param param
		 * @param user
		 * @return
		 */
		@SuppressWarnings("unchecked")
		public BlInvPrint stInvReprintByInv(String param,IUser user){
			BlInvPrint res = new BlInvPrint();
			Map<String,Object> paraMap = JsonUtil.readValue( param, Map.class); 
			String pkSettle = (String)paraMap.get("pkSettle");
			List<String> codeInv = (List<String>) paraMap.get("codeInv");
			String pkEmpinv = (String)paraMap.get("pkEmpinv");
			List<String> pkInvList = JsonUtil.readValue(
					JsonUtil.getJsonNode(param, "pkInvList"),
					new TypeReference<List<String>>() {
					});
			
			if(pkInvList==null || pkInvList.size()<=0)
				throw new BusException("请勾选需要重打的发票！");
			
			//重新获取发票信息
			List<InvInfoVo> invoInfos = new ArrayList<>();
			List<BillInfo> billList = new ArrayList<>();
    		if(codeInv!=null && codeInv.size()>0){
    			for(String invNo : codeInv){
    				InvInfoVo invo = new InvInfoVo();
    				if(paraMap.containsKey("nameMachine")){
    					invo.setNameMachine(CommonUtils.getString(paraMap.get("nameMachine")));
    				}
    				invoInfos.add(invo);
    			}
    			String euType = MapUtils.getString(DataBaseHelper.queryForMap("select EU_PVTYPE from bl_settle where PK_SETTLE=?",new Object[]{pkSettle}),"euPvtype");
    			billList = invSettltService.getInvInfo(invoInfos, EnumerateParameter.THREE.equals(euType)?EnumerateParameter.ONE:EnumerateParameter.ZERO);
    		}
			
			BlInvPrint old = getPrintDataByPkSettle(pkSettle);
			
			Map<String,Object> rePrintMap = new HashMap<>();//重打发票个性化服务入参
//			调用发票查询服务，判断是否有足够的发票张数，如果不够，提示用户“发票剩余数量不足，请设置!”；
//			作废原发票号码；
			List<BlInvoice> newInvs = new ArrayList<>();
			List<BlInvoice> upInvs = new ArrayList<>();
			for(BlInvoice inv : old.getBlInvoice()){
				boolean flag = false;
				for(String pkInv : pkInvList){
					if(inv.getPkInvoice().equals(pkInv)){
						if(!"1".equals(inv.getFlagCancel())){
							flag = true;
							newInvs.add(inv);
							DataBaseHelper.execute("update bl_invoice  set print_times=1,flag_cancel = 1,date_cancel = ?,"
									+ "  pk_emp_cancel = ?,name_emp_cancel = ?"
									+ " where PK_INVOICE = ?", new Date(),UserContext.getUser().getPkEmp(),
									UserContext.getUser().getNameEmp(),inv.getPkInvoice());
							break;
						}else{
							throw new BusException("票据号["+inv.getCodeInv()+"]已经作废，请刷新后重新勾选！");
						}
					}
				}
				if(!flag){//不需要重打的票据更新其print_times=1
					inv.setPrintTimes(1);
					upInvs.add(inv);
				}
			}

			if(upInvs!=null && upInvs.size()>0)
				DataBaseHelper.batchUpdate(DataBaseHelper.getUpdateSql(BlInvoice.class), upInvs);
			
			List<BlInvoiceDt> newInvDts = new ArrayList<BlInvoiceDt>();
			List<BlStInv> stInvs =  new ArrayList<BlStInv>();
			for(int i = 0;i<newInvs.size();i++){
				BlInvoice newVo = newInvs.get(i);
				String oldPk = newVo.getPkInvoice();
				newVo.setPkEmpInv(UserContext.getUser().getPkEmp());
				newVo.setPkEmpinvoice(pkEmpinv);
				newVo.setNameEmpInv(UserContext.getUser().getNameEmp());
				if(codeInv!=null){
					newVo.setCodeInv(billList.get(i).getCurCodeInv());
				}
				newVo.setDateInv(new Date());
				newVo.setPrintTimes(0);
				newVo.setFlagCancel("0");
				newVo.setFlagCc("0");
				newVo.setBillbatchcode(null);
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
//			写发票表bl_invoice；
			DataBaseHelper.batchUpdate(DataBaseHelper.getInsertSql(BlInvoice.class), newInvs);
//			写发票明细表bl_invoice_dt；
			DataBaseHelper.batchUpdate(DataBaseHelper.getInsertSql(BlInvoiceDt.class), newInvDts);
//			写发票结算关联表bl_st_inv；
			DataBaseHelper.batchUpdate(DataBaseHelper.getInsertSql(BlStInv.class), stInvs);
			
			//重打发票个性化服务
			InvPrintProcessUtils.invRePrint(rePrintMap);
			
			//获取BL0031（收费结算启用电子票据），参数值为1时调用重打电子票据接口
		    //String eBillFlag = ApplicationUtils.getSysparam("BL0031", false);
			String eBillFlag = invSettltService.getBL0031ByNameMachine(invoInfos.get(0).getNameMachine());
		    if(!CommonUtils.isEmptyString(eBillFlag) && "1".equals(eBillFlag)){
		    	try{
		    		invSettltService.rePaperInv(billList,pkSettle, user);
				}catch (Exception e) {
					throw new BusException("重打发票失败：" + e.getMessage());
				}
		    }
		    
//			调用发票使用确认服务，完成发票更新。
			try{
				commonService.confirmUseEmpInv(pkEmpinv, new Long(newInvs.size()));
			}catch(Exception e){
				throw new BusException("发票更新失败，请确认票据是否充足！");
			}
			
			//发送发票消息（重打发票）
			PlatFormSendUtils.sendReceiptMsg(paraMap);
			
			return getPrintDataByPkSettle(pkSettle);
		}
		
		/**
		 * 交易码：007003008015
		 * 发票打印
         * 仅适用于结算没有生成发票信息使用
         * 调用此事件会生成新的发票信息并关联结算，已生成过发票信息的不可用此事件
		 * @param param
		 * @param user
		 */
		public void saveStInvInfo(String param,IUser user){
			SaveInvInfoVo invVo = JsonUtil.readValue(param, SaveInvInfoVo.class);
			if(invVo!=null && !CommonUtils.isEmptyString(invVo.getPkSettle())){
				//获取结算主键校验是否是有效结算信息
				String pkSettle = invVo.getPkSettle();
				BlSettle stInfo = DataBaseHelper.queryForBean(
						"select * from bl_settle where pk_settle = ?",
						BlSettle.class, new Object[]{pkSettle});
				if(stInfo!=null){
					//校验是否作废
					if("1".equals(stInfo.getFlagCanc()) || !CommonUtils.isEmptyString(stInfo.getPkSettleCanc())){
						throw new BusException("已作废的结算信息不可打印发票！");
					}
					//校验是否已经打印过发票
					Integer cnt = DataBaseHelper.queryForScalar(
							"select count(1) from BL_ST_INV where PK_SETTLE = ?",
							Integer.class, new Object[]{pkSettle});
					if(cnt!=null && cnt>=1){
						throw new BusException("该结算信息已经打印过票据！");
					}
					
					//组织发票信息
					List<InvInfoVo> invoInfos = new ArrayList<>();
					InvInfoVo inv = new InvInfoVo();
					inv.setAmount(invVo.getAmount());
					inv.setAmountPi(invVo.getAmountPi());
					inv.setInv(invVo.getInv());
					inv.setPage(invVo.getPage());
					inv.setSplitWay(invVo.getSplitWay());
					inv.setNameMachine(invVo.getNameMachine());
					invoInfos.add(inv);
					
					//发票数据准备
					Map<String,Object> invMap = invSettltService.invoData(stInfo.getPkPv(),"0",invoInfos,user,dateTrans(stInfo.getDateBegin()),dateTrans(stInfo.getDateEnd()),stInfo.getPkSettle(),"0");
					List<BlInvoice> invo = (List<BlInvoice>) invMap.get("inv");;
					List<BlInvoiceDt> invoDt = (List<BlInvoiceDt>)invMap.get("invDt");
					//写发票表bl_invoice和发票明细表bl_invoice_dt
					insertInvo(invo,invoDt);
					//写发票和结算关系表bl_st_inv；
					insertInvoSt(invo,stInfo);
					//调用发票使用确认服务，完成发票更新。
					commonService.confirmUseEmpInv(invoInfos.get(0).getInv().getPkEmpinv(), new Long(invo.size()));
					
				}else{
					throw new BusException("未查询到结算信息，请联系管理员！");
				}
			}else{
				throw new BusException("未传入发票信息，请联系管理员！");
			}
			
		}
		
		
		/**
		 * 交易号：007003003066
		 * 现金退款处理
		 * 现金退款时需要按照预交金的缴款笔数分开退，不能退一笔总数
		 * @param param
		 * @param user
		 */
		public List<BlDeposit> calCashRefund(String param,IUser user){
			// 入参是pkpv，患者退款金额
	        Map<String, Object> objectMap = JsonUtil.readValue(param, Map.class);
			
	        BigDecimal refund = BigDecimal.valueOf(CommonUtils.getDoubleObject(objectMap.get("refund")));
	        refund = refund.abs();
	        BigDecimal remainingRefund = BigDecimal.ZERO;
	        //返回结果集集合
	        List<BlDeposit> depoList = new ArrayList<>();
	        
	        //现金预交金集合
	        List<BlDeposit> cashList = blIpSettleQryMapper.qryCashDepoInfo(objectMap);
	        
	        if(cashList!=null && cashList.size()>0){
	        	boolean isCancle = true;//是否运算完毕
	        	for(BlDeposit cashVo : cashList){
	        		if(cashVo.getAmount().compareTo(BigDecimal.ZERO)<=0 ||
	        				refund.compareTo(BigDecimal.ZERO)<=0){
	        			continue;
	        		}
	        		
	        		if (isCancle) {
		        		remainingRefund = cashVo.getAmount().subtract(refund);
		                int i = remainingRefund.compareTo(BigDecimal.ZERO);
		                
		                cashVo.setPkEmpPay(UserContext.getUser().getPkEmp());
		                cashVo.setNameEmpPay(UserContext.getUser().getNameEmp());
		                cashVo.setFlagCc("0");
		                cashVo.setDatePay(new Date());
		                
		                //订单满足付款
		                if (i == 1 || i == 0) {
		                	cashVo.setAmount(refund.negate());
		                	depoList.add(cashVo);
		                    refund = BigDecimal.ZERO;
		                    isCancle = false;
		                } else {
		                	cashVo.setAmount(cashVo.getAmount().multiply(BigDecimal.valueOf(-1d)));
		                    depoList.add(cashVo);
		                    refund = remainingRefund.abs();
		                }
	        		}
	        	}
	        }
			
	        return depoList;
		}
		
	/**
	 * 交易码：007003003067
	 * 结算退款时查询可退的预交金信息
	 * @param param
	 * @param user
	 */
	public List<Map<String,Object>> qryStBackDepo(String param,IUser user){
		// 入参是pkpv
        Map<String, Object> paramMap = JsonUtil.readValue(param, Map.class);
        
        List<Map<String,Object>> retList = new ArrayList<>();
        
        if(paramMap!=null && paramMap.size()>0 && 
        		paramMap.containsKey("pkPv") && !CommonUtils.isEmptyString(CommonUtils.getString(paramMap.get("pkPv")))){
        	//查询患者可退预交金集合
        	retList = blIpSettleQryMapper.qryStBackDepo(paramMap);
        }
        
        return retList;
	}

	/**
	 * 交易号：007003003068
	 * 查询患者辅医保信息
	 * @param param
	 * @param user
	 * @return
	 */
	public List<Map<String,Object>> qryMirHpInfoByPkPv(String param,IUser user){
		// 入参是pkpv
		Map<String, Object> paramMap = JsonUtil.readValue(param, Map.class);

		List<Map<String,Object>> retList = new ArrayList<>();

		if(paramMap!=null && paramMap.size()>0 &&
				paramMap.containsKey("pkPv") && !CommonUtils.isEmptyString(CommonUtils.getString(paramMap.get("pkPv")))){
			//查询患者辅医保信息
			retList = blIpSettleQryMapper.qryMirHpInfoByPkPv(paramMap);
		}

		return retList;
	}
		
}
	

