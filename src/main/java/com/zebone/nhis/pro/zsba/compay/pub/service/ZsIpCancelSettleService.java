package com.zebone.nhis.pro.zsba.compay.pub.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zebone.nhis.common.dao.BlIpPubMapper;
import com.zebone.nhis.common.module.bl.BlDeposit;
import com.zebone.nhis.common.module.bl.BlIpDt;
import com.zebone.nhis.common.module.bl.BlSettleAr;
import com.zebone.nhis.common.module.bl.BlSettleDetail;
import com.zebone.nhis.common.module.pi.PiAcc;
import com.zebone.nhis.common.module.pi.acc.PiAccDetail;
import com.zebone.nhis.common.module.pv.PvEncounter;
import com.zebone.nhis.common.service.BalAccoutService;
import com.zebone.nhis.common.support.ApplicationUtils;
import com.zebone.nhis.common.support.CommonUtils;
import com.zebone.nhis.common.support.EnumerateParameter;
import com.zebone.nhis.common.support.MathUtils;
import com.zebone.nhis.ma.pub.platform.PlatFormSendUtils;
import com.zebone.nhis.pro.zsba.compay.pub.dao.ZsbaBlIpPubMapper;
import com.zebone.nhis.pro.zsba.compay.pub.vo.PayPosTr;
import com.zebone.nhis.pro.zsba.compay.pub.vo.ZsbaBlDeposit;
import com.zebone.nhis.pro.zsba.compay.pub.vo.ZsbaBlSettle;
import com.zebone.nhis.pro.zsba.compay.pub.vo.ZsbaCancelSettleHuishen;
import com.zebone.nhis.pro.zsba.compay.pub.vo.ZsbaCancelSettleParam;
import com.zebone.platform.common.support.NHISUUID;
import com.zebone.platform.common.support.User;
import com.zebone.platform.common.support.UserContext;
import com.zebone.platform.framework.support.IUser;
import com.zebone.platform.modules.dao.DataBaseHelper;
import com.zebone.platform.modules.exception.BusException;
import com.zebone.platform.modules.utils.JsonUtil;


@Service
public class ZsIpCancelSettleService {
	
	@Autowired
	private BlIpPubMapper blIpPubMapper; 
	
	@Autowired
	private BalAccoutService balAccoutService;
	
	@Autowired
	private ZsIpSettleService settleService;
	
	@Autowired
	private ZsbaBlIpPubMapper zsbaBlipPubMapper;
	
	/**
	 * 取消结算
	 * @param param 结算主键
	 * @param user
	 * @throws Exception 
	 */
	@SuppressWarnings("unchecked")
	public ZsbaCancelSettleHuishen cancelSettle(String param, IUser user) throws Exception {
		ZsbaCancelSettleParam canSt = JsonUtil.readValue(param, ZsbaCancelSettleParam.class);
		
		ZsbaBlSettle stVo = zsbaBlipPubMapper.QryBlSettleByPk(canSt.getPkSettle());
		if(stVo.getFlagCanc().equals("1")){
			throw new BusException("此次結算已取消，不可重复取消！");
		}
		String pkPv = stVo.getPkPv();
		PvEncounter pvvo = DataBaseHelper.queryForBean("Select * from pv_encounter where pk_pv = ?",PvEncounter.class, pkPv);
		User userInfo = (User) user;
		
		// 1.写入被取消结算的反交易记录，写表bl_settle；返回值为反结算Vo
		ZsbaBlSettle canStVo = stInfo(stVo, userInfo);
		
		// 2.查询被取消结算对应的记费明细，并基于此生成相反的记费记录，关联反结算记录，写bl_ip_st表，除以下字段外，其余字段值同原记费记录值；
		List<BlIpDt> cgVos = zsbaBlipPubMapper.QryBlIpDtBySt(canSt.getPkSettle());
		List<BlIpDt> cgVosC = new ArrayList<BlIpDt>();
		for (BlIpDt vo : cgVos) {
			BlIpDt voc = (BlIpDt) vo.clone();
			voc.setQuan(vo.getQuan() * -1);
			voc.setAmount(vo.getAmount() * -1);
			voc.setAmountPi(vo.getAmountPi() * -1);
			if(vo.getAmountHppi()!=null){
				voc.setAmountHppi(vo.getAmountHppi() * -1);
			}
			if (vo.getAmountAdd() != null){
				voc.setAmountAdd(vo.getAmountAdd() * -1);
			}
			voc.setPkSettle(canStVo.getPkSettle());
			voc.setPkCgip(NHISUUID.getKeyId());
			voc.setDateCg(new Date());
			voc.setCreateTime(new Date());
			voc.setTs(new Date());
			cgVosC.add(voc);
		}
		cgVosC = clCgipBack(cgVos, cgVosC);
		DataBaseHelper.batchUpdate(DataBaseHelper.getInsertSql(BlIpDt.class), cgVosC);
		
		// 3 基于被取消结算对应的记费明细生成未结算的记费记录，写bl_ip_st表，除以下字段外，其余字段值同原记费记录值；
		List<BlIpDt> cgVosN = new ArrayList<BlIpDt>();
		for (BlIpDt vo : cgVos) {
			BlIpDt voc = (BlIpDt) vo.clone();
			voc.setPkSettle(null);
			voc.setFlagSettle("0");
			voc.setFlagInsu("0");
			voc.setPkCgip(NHISUUID.getKeyId());
			voc.setDateCg(new Date());
			voc.setCreateTime(new Date());
			voc.setTs(new Date());
			cgVosN.add(voc);
		}
		cgVosN = clCgipBack(cgVos, cgVosN);
		DataBaseHelper.batchUpdate(DataBaseHelper.getInsertSql(BlIpDt.class), cgVosN);
		// 4 取消收付款表的住院预交金和被取消结算的关联；
		DataBaseHelper.execute("update bl_deposit set pk_settle=null, flag_settle='0' where pk_settle=? and eu_dptype='9'", canSt.getPkSettle());
		
		// 5 写入基于收付款表的结算类型记录的反交易记录，写表bl_deposit，除以下字段外，其余字段同原结算收付款记录；
		List<ZsbaBlDeposit> depos = zsbaBlipPubMapper.QryBlDepositBySt(canSt.getPkSettle());
		if (depos.size() > 0) {
			//addPaymentRefundRecord(depos, userInfo);// 写入预交金退款负记录
		}
		
		int amtIf = new BigDecimal(canSt.getSsAmt()).compareTo(BigDecimal.ZERO); 
		if(amtIf==1){
			//收的话将所有金额和为一条
				for (int i = 0; i < depos.size(); ++i) {
					if(i==0){
						ZsbaBlDeposit depo = depos.get(i);
						if (EnumerateParameter.ONE.equals(depo.getEuDirect())
								&& EnumerateParameter.FOUR.equals(depo.getDtPaymode())) {
							if (EnumerateParameter.ONE.equals(depo.getEuDptype())
									|| EnumerateParameter.ZERO.equals(depo.getEuDptype())) {
								dealAcc(stVo, pvvo, depo.getAmount().multiply(new BigDecimal(-1)));
							}
						}
						depo.setEuDptype("4");//4 取消结算
						int direct = Integer.parseInt(depo.getEuDirect());
						depo.setEuDirect((-1 * direct) + "");
						depo.setAmount(new BigDecimal(canSt.getSsAmt()));
						depo.setDatePay(new Date());
						depo.setPkDept(userInfo.getPkDept());
						depo.setPkEmpPay(userInfo.getPkEmp());
						depo.setNameEmpPay(userInfo.getNameEmp());
						depo.setFlagSettle("1");
						depo.setPkSettle(canStVo.getPkSettle());
						depo.setFlagCc("0");
						depo.setPkCc(null);
						depo.setPkStMid(null);
						depo.setPkDepo(NHISUUID.getKeyId());
						depo.setDtPaymode(canSt.getPayMode());
						depo.setTs(new Date());
						depo.setCreateTime(new Date());
						depo.setPkDepoBack(null);
						depo.setVoidType("0");
						if(canSt.getPayMode().equals("1")){
							depo.setNote("取消结算：收现金");
						}else if (canSt.getPayMode().equals("3")){
							depo.setNote("取消结算：收银行卡");
						}else if (canSt.getPayMode().equals("7")){
							depo.setNote("取消结算：收微信");
						}else if (canSt.getPayMode().equals("8")){
							depo.setNote("取消结算：收支付宝");
						}else if (canSt.getPayMode().equals("92")){
							depo.setNote("取消结算：收财务记账");
						}
						if(canSt.getBlExtPay()!=null && canSt.getBlExtPay().getPkExtpay()!=null){
							depo.setDtBank("1");
							depo.setPayInfo(canSt.getBlExtPay().getPkExtpay());
							
						}
					}
				}
				List<ZsbaBlDeposit> depos2 = new ArrayList<ZsbaBlDeposit>();
				depos2.add(depos.get(0));
				DataBaseHelper.batchUpdate(DataBaseHelper.getInsertSql(ZsbaBlDeposit.class), depos2);
				if(canSt.getBlExtPay()!=null && canSt.getBlExtPay().getPkExtpay()!=null){
					DataBaseHelper.execute("update bl_ext_pay set pk_depo=? where pk_extpay=? ", depos2.get(0).getPkDepo(), canSt.getBlExtPay().getPkExtpay());
				}
		}else if(amtIf==-1){
			//如果是退钱并且现金，将所以金额合为一条，
			if(canSt.getPayMode().equals("1")||canSt.getPayMode().equals("92")){
				for (int i = 0; i < depos.size(); ++i) {
					if(i==0){
						ZsbaBlDeposit depo = depos.get(i);
						if (EnumerateParameter.ONE.equals(depo.getEuDirect())
								&& EnumerateParameter.FOUR.equals(depo.getDtPaymode())) {
							if (EnumerateParameter.ONE.equals(depo.getEuDptype())
									|| EnumerateParameter.ZERO.equals(depo.getEuDptype())) {
								dealAcc(stVo, pvvo, depo.getAmount().multiply(new BigDecimal(-1)));
							}
						}
						depo.setEuDptype("4");//4 取消结算
						int direct = Integer.parseInt(depo.getEuDirect());
						depo.setEuDirect((-1 * direct) + "");
						depo.setAmount(new BigDecimal(canSt.getSsAmt()));
						depo.setDatePay(new Date());
						depo.setPkDept(userInfo.getPkDept());
						depo.setPkEmpPay(userInfo.getPkEmp());
						depo.setNameEmpPay(userInfo.getNameEmp());
						depo.setFlagSettle("1");
						depo.setPkSettle(canStVo.getPkSettle());
						depo.setFlagCc("0");
						depo.setPkCc(null);
						depo.setPkStMid(null);
						depo.setPkDepo(NHISUUID.getKeyId());
						depo.setDtPaymode(canSt.getPayMode());
						//depo.setNote("取消结算：退现金");
						depo.setTs(new Date());
						depo.setCreateTime(new Date());
						depo.setPkDepoBack(null);
						depo.setVoidType("0");
						if(canSt.getPayMode().equals("1")){
							depo.setNote("取消结算：退现金");
						}else if (canSt.getPayMode().equals("3")){
							depo.setNote("取消结算：退银行卡");
						}else if (canSt.getPayMode().equals("7")){
							depo.setNote("取消结算：退微信");
						}else if (canSt.getPayMode().equals("8")){
							depo.setNote("取消结算：退支付宝");
						}else if (canSt.getPayMode().equals("92")){
							depo.setNote("取消结算：退财务记账");
						}
					}
				}
				List<ZsbaBlDeposit> depos2 = new ArrayList<ZsbaBlDeposit>();
				depos2.add(depos.get(0));
				DataBaseHelper.batchUpdate(DataBaseHelper.getInsertSql(ZsbaBlDeposit.class), depos2);
			}else{
				//其他支付方式处理
				String dateFmt = "yyyy-MM-dd HH:mm:ss";
				String dateEnd = settleService.dateTrans(stVo.getDateSt(), dateFmt);
				settleService.saveRefundData(canStVo, dateEnd);
			}
		}

		if(canSt.getBlExtPay()!=null && canSt.getBlExtPay().getPkExtpay()!=null){
			//	3.6.处理第三方支付交易数据	
			canSt.getBlExtPay().setPkSettle(canStVo.getPkSettle());
			settleService.saveBlExtPay(depos.get(0).getPkDepo(),canSt.getBlExtPay());
		}
		//处理医保数据,目前只有省工伤医保用到
		if(canSt.getInsType()!=null){
			if(canSt.getInsType().equals("3")){
				DataBaseHelper.execute("update ins_st_wi set pk_settle=? where pk_insstwi=? ", canStVo.getPkSettle(), canSt.getPkInsSt());
			}
		}
		
		// 6 如果被取消的结算为中途结算，冲销pk_st_mid为中途结算主键的记录；
		// 0X: 门诊流程： 00 门诊收费结算，01 门诊挂号结算 ；
		// 1X 住院流程：10 出院结算，11中途结算；
		// 2X 反流程结算：20 冲账，21 取消结算
		if ("11".equals(stVo.getDtSttype())) {
			List<BlDeposit> deposRet = blIpPubMapper.QryBlDepositByMidSt(canSt.getPkSettle());
			if (deposRet != null && deposRet.size() > 0) {
				for (BlDeposit depo : deposRet) {
					depo.setEuDptype("9");
					depo.setEuDirect("-1");
					depo.setAmount(depo.getAmount().multiply(new BigDecimal(-1)));
					depo.setDatePay(new Date());
					depo.setPkDept(userInfo.getPkDept());
					depo.setPkEmpPay(userInfo.getPkEmp());
					depo.setNameEmpPay(userInfo.getNameEmp());
					depo.setFlagSettle("0");
					depo.setPkSettle(null);
					depo.setPkCc(null);
					depo.setFlagCc("0");
					depo.setPkDepoBack(depo.getPkDepo());
					depo.setPkDepo(NHISUUID.getKeyId());
					depo.setPkStMid(stVo.getPkSettle());
				}
			}
			DataBaseHelper.batchUpdate(DataBaseHelper.getInsertSql(BlDeposit.class), deposRet);
		}

/*		if ("10".equals(stVo.getDtSttype())
				&& EnumerateParameter.TWO.equals(stVo.getEuStresult())) {
			
			 *  如果被取消的结算为出院结算，结算结果为存款结算（eu_stresult=2），
			 *  要将存入患者账户的存款冲销回原状，冲销前要调用查询患者账户服务，确认患者账户金额是否足以冲销，如果患者账户金额不足，将不允许取消结算。
			 
			List<BlDepositPi> deposRe = blIpPubMapper.QryBlDepositPiBySt(pkSettle);
			BigDecimal amtAcc = BigDecimal.ZERO;
			for (BlDepositPi depo : deposRe) {
				amtAcc = amtAcc.add(depo.getAmount());
			}
/*			Map<String, Object> amtAccMap = DataBaseHelper.queryForMap(
							"SELECT Amt_acc FROM pi_acc piacc INNER JOIN pv_encounter pv on piacc.pk_pi=pv.pk_pi WHERE pv.pk_pv=?",
							stVo.getPkPv());
			BigDecimal amtAccNow = new BigDecimal("0");
			if(amtAccMap!=null && amtAccMap.get("amtAcc")!=null){
				amtAccNow =(BigDecimal) amtAccMap.get("amtAcc");
			}
			if (amtAccNow.compareTo(amtAcc) >= 0) {
				BlDepositPi blDepoPi = new BlDepositPi();
				blDepoPi.setPkDepopi(NHISUUID.getKeyId());
				blDepoPi.setPkPi(stVo.getPkPi());
				blDepoPi.setEuDirect("1");
				blDepoPi.setAmount(amtAcc.multiply(new BigDecimal(-1)));// 存款金额
				blDepoPi.setDtPaymode("5");// （内部转账）
				blDepoPi.setDatePay(new Date());// 当前日期
				blDepoPi.setPkDept(UserContext.getUser().getPkDept());// 当前部门
				blDepoPi.setPkEmpPay(UserContext.getUser().getPkEmp());// 当前用户
				blDepoPi.setNameEmpPay(UserContext.getUser().getNameEmp());// 当前用户姓名
				blDepoPi.setReptNo("");
				balAccoutService.saveMonOperation(blDepoPi, user, null, null,blDepoPi.getDtPaymode());
			} else {
				throw new BusException("账户余额:" + amtAccNow.doubleValue()+ "不足，小于冲销所需:" + amtAcc + ",取消结算失败!");
			}
		}*/
		// 7 作废被取消结算关联的发票；
		StringBuffer sql = new StringBuffer("update bl_invoice");
		sql.append(" set flag_cancel='1',");
		sql.append(" date_cancel=?,");
		sql.append(" pk_emp_cancel=?,");
		sql.append(" name_emp_cancel=?,");
		sql.append(" flag_back='1'");
		sql.append(" where exists (select 1 from bl_st_inv stinv");
		sql.append(" inner join bl_settle st on stinv.pk_settle=st.pk_settle");
		sql.append(" where stinv.pk_invoice=bl_invoice.pk_invoice and st.pk_settle=?)");
		sql.append(" and flag_cancel='0'");
		DataBaseHelper.execute(sql.toString(), new Date(), userInfo.getPkEmp(), userInfo.getNameEmp(), canSt.getPkSettle());
		
		// 设置pvEncounter
		if (!"11".equals(stVo.getDtSttype())){
			DataBaseHelper.execute("update pv_encounter set flag_settle='0',eu_status='2' where pk_pv=?", pkPv); // ,flag_in='1' TODO：取消出院结算时，不需要更新在院标志
		}
		// 修改原结算记录的flag_canl为“1”
		DataBaseHelper.execute("update bl_settle set flag_canc='1' where pk_settle=?", canSt.getPkSettle());

		Map<String, Object> paraMap = new HashMap<String, Object>();
		paraMap.put("pkSettle", canSt.getPkSettle());
		paraMap.put("codeEmp", userInfo.getCodeEmp());
		paraMap.put("nameEmp", userInfo.getNameEmp());
		paraMap.put("pkPv", stVo.getPkPv());
		PlatFormSendUtils.sendBlCancelSettleMsg(paraMap);
		paraMap = null;
		ZsbaCancelSettleHuishen huishen = new ZsbaCancelSettleHuishen();
		huishen.setBlDepositList(depos);
		huishen.setBlSettle(canStVo);
		
		//三代卡个账交易记录插入结算主键和医保结算主键
		if(canSt.getPkPosTr()!=null){
			PayPosTr posTr = new PayPosTr();
			posTr.setPkPosTr(canSt.getPkPosTr());
			posTr.setPkSettle(canStVo.getPkSettle());
			Map<String, Object> insStMap = DataBaseHelper.queryForMap("select top 1 * from ins_st where pk_settle=? order by create_time desc", canStVo.getPkSettleCanc());
			if(insStMap!=null && insStMap.get("pkInsst")!=null){
				posTr.setPkInsst(insStMap.get("pkInsst").toString());
			}
			DataBaseHelper.updateBeanByPk(posTr,false);
		}
		return huishen;
	}

	private ZsbaBlSettle  stInfo(ZsbaBlSettle stVoTemp,User userInfo){
		ZsbaBlSettle stVo = (ZsbaBlSettle)stVoTemp.clone();
		stVo.setPkOrg(userInfo.getPkOrg());
		stVo.setEuPvtype(EnumerateParameter.THREE);
		stVo.setDtSttype("21");
		PvEncounter pvvo = DataBaseHelper.queryForBean(
				"select * from pv_encounter where pk_pv = ?",
				PvEncounter.class, stVo.getPkPv());	
		stVo.setPkInsurance(pvvo.getPkInsu());//主医保；
		stVo.setAmountPrep(new BigDecimal(-1).multiply(stVo.getAmountPrep()));
		stVo.setAmountSt(new BigDecimal(-1).multiply(stVo.getAmountSt()));
		stVo.setAmountPi(new BigDecimal(-1).multiply(stVo.getAmountPi()));
		stVo.setAmountInsu(new BigDecimal(-1).multiply(stVo.getAmountInsu()));
		if(stVo.getAmountDisc()!=null){
			stVo.setAmountDisc(stVo.getAmountDisc()*-1);
		}
		stVo.setDateSt(new Date());
		stVo.setPkOrgSt(userInfo.getPkOrg());
		stVo.setPkDeptSt(userInfo.getPkDept());
		stVo.setPkEmpSt(userInfo.getPkEmp());
		stVo.setNameEmpSt(userInfo.getNameEmp());
		stVo.setFlagCc("0");
		stVo.setFlagCanc("1");
		stVo.setPkSettleCanc(stVo.getPkSettle());
		stVo.setReasonCanc("取消结算"+stVo.getCodeSt());
		//结算编码
		stVo.setCodeSt(ApplicationUtils.getCode("0605"));
		stVo.setFlagArclare("0");
		stVo.setPkSettle(NHISUUID.getKeyId());
		DataBaseHelper.insertBean(stVo);
		
		//写bl_settle_detail反结算记录
		List<BlSettleDetail> stDts = DataBaseHelper.queryForList(
				"select * from BL_SETTLE_DETAIL where PK_SETTLE = ?", 
				BlSettleDetail.class, stVoTemp.getPkSettle());
		if(stDts!=null && stDts.size()>0){
			for(BlSettleDetail dt : stDts){
				dt.setPkStdt(NHISUUID.getKeyId());
				dt.setPkSettle(stVo.getPkSettle());
				dt.setAmount(MathUtils.mul(new Double(-1), dt.getAmount()));
			}
			DataBaseHelper.batchUpdate(DataBaseHelper.getInsertSql(BlSettleDetail.class),stDts);
		}
		
		//写bl_settle_ar反结算记录
		List<BlSettleAr> stArList = DataBaseHelper.queryForList(
				"select * from BL_SETTLE_AR where PK_SETTLE = ?", 
				BlSettleAr.class, stVoTemp.getPkSettle());
		if(stArList!=null && stArList.size()>0){
			for(BlSettleAr stAr : stArList){
				stAr.setPkSettlear(NHISUUID.getKeyId());
				stAr.setPkSettle(stVo.getPkSettle());
				stAr.setAmtAr(stAr.getAmtAr()==null?0D:MathUtils.mul(stAr.getAmtAr(), -1D));
				stAr.setAmtPay(stAr.getAmtPay()==null?0D:MathUtils.mul(stAr.getAmtPay(), -1D));
				stAr.setDatePay(new Date());
				ApplicationUtils.setDefaultValue(stAr, true);
			}
			DataBaseHelper.batchUpdate(DataBaseHelper.getInsertSql(BlSettleAr.class),stArList);
		}
		
		return stVo;
	}

	/**
	 * 处理关联记费主键
	 * @param oldCgs
	 * @param newCgs
	 * @return
	 */
	private List<BlIpDt> clCgipBack(List<BlIpDt> oldCgs,List<BlIpDt> newCgs){
		/**处理退费关联计费主键*/
		for(int i = 0; i<oldCgs.size(); i++){
			if(!CommonUtils.isEmptyString(oldCgs.get(i).getPkCgipBack())){
				for(int j=0; j<oldCgs.size(); j++){
					if(oldCgs.get(j).getPkCgip().equals(oldCgs.get(i).getPkCgipBack())){
						newCgs.get(i).setPkCgipBack(newCgs.get(j).getPkCgip());
						break;
					}
				}
			}
		}
		return newCgs;
	}

	
	/**
	 * @Description  添加预交金负记录
	 * @auther wuqiang
	 * @Date 2019/8/17
	 * @Param [depos, userInfo]
	 * @return void
	 */
   private  void  addPaymentRefundRecord (List<ZsbaBlDeposit> depos,User userInfo){

	   if ("1".equals(ApplicationUtils.getSysparam("BL0042", false))){
		   for (ZsbaBlDeposit blDeposit:depos){
			   if ("-1".equals(blDeposit.getEuDirect())){
				   BlDeposit blDeposit2=new BlDeposit();
				   ApplicationUtils.copyProperties(blDeposit2,blDeposit);
				   blDeposit2.setEuDptype("9");
				   blDeposit2.setAmount(blDeposit.getAmount());
				   blDeposit2.setDtPaymode(blDeposit.getDtPaymode());
				   blDeposit2.setEuDirect("-1");
				   blDeposit2.setDatePay(new Date());
				   blDeposit2.setPkDept(userInfo.getPkDept());
				   blDeposit2.setPkEmpPay(userInfo.getPkEmp());
				   blDeposit2.setNameEmpPay(userInfo.getNameEmp());
				   blDeposit2.setFlagSettle("0");
				   blDeposit2.setFlagCc("0");
				   blDeposit2.setPkCc(null);
				   blDeposit2.setPkStMid(null);
				   blDeposit2.setNote("取消结算-预交金退款冲负");
				   ApplicationUtils.setDefaultValue(blDeposit2,true);
				   DataBaseHelper.insertBean(blDeposit2);
			   }
		   }
	   }
   }

   private void dealAcc(ZsbaBlSettle stVo, PvEncounter pvvo, BigDecimal amtAcc) {
		Map<String,Object> amtAccMap = DataBaseHelper.queryForMap("select  Amt_acc from pi_acc piacc  inner join pv_encounter pv on piacc.pk_pi = pv.pk_pi where  pv.pk_pv = ?", stVo.getPkPv());
		BigDecimal amtAccNow = (BigDecimal)amtAccMap.get("amtAcc");
		if(amtAccNow.compareTo(amtAcc)>=0){
			String getPiA="Select * from pi_acc where pk_pi=? and (del_flag='0' or del_flag is null)";
			PiAcc pa=DataBaseHelper.queryForBean(getPiA, PiAcc.class, pvvo.getPkPi());
			pa.setAmtAcc(amtAccNow.subtract(amtAcc));
				DataBaseHelper.updateBeanByPk(pa,false);
				PiAccDetail pad=new PiAccDetail();
				pad.setPkPiacc(pa.getPkPiacc());
				pad.setPkPi(pa.getPkPi());
				pad.setDateHap(new Date());
				pad.setEuOptype(EnumerateParameter.ONE);
				pad.setEuDirect(amtAcc.compareTo(BigDecimal.ZERO)>0?"-1":"1");
				pad.setAmount(new BigDecimal(-1).multiply(amtAcc));
				pad.setPkDepopi(null);
				pad.setPkEmpOpera(UserContext.getUser().getPkEmp());
				pad.setNameEmpOpera(UserContext.getUser().getNameEmp());
				DataBaseHelper.insertBean(pad);
		}else{
			throw new  BusException("账户余额:"+amtAccNow.doubleValue()+"不足，小于冲销所需:"+amtAcc+",取消结算失败!");
		}
	}

}
