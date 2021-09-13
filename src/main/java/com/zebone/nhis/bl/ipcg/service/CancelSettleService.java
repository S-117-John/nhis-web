package com.zebone.nhis.bl.ipcg.service;

import java.math.BigDecimal;








import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zebone.nhis.base.pub.service.DumpPubService;
import com.zebone.nhis.bl.pub.support.InvSettltService;
import com.zebone.nhis.common.dao.BlIpPubMapper;
import com.zebone.nhis.common.module.bl.BlDeposit;
import com.zebone.nhis.common.module.bl.BlDepositPi;
import com.zebone.nhis.common.module.bl.BlIpDt;
import com.zebone.nhis.common.module.bl.BlSettle;
import com.zebone.nhis.common.module.bl.BlSettleAr;
import com.zebone.nhis.common.module.bl.BlSettleDetail;
import com.zebone.nhis.common.module.pi.PiAcc;
import com.zebone.nhis.common.module.pi.acc.PiAccDetail;
import com.zebone.nhis.common.module.pv.PvEncounter;
import com.zebone.nhis.common.service.BalAccoutService;
import com.zebone.nhis.common.support.*;
import com.zebone.nhis.ma.pub.platform.PlatFormSendUtils;
import com.zebone.platform.common.support.NHISUUID;
import com.zebone.platform.common.support.User;
import com.zebone.platform.common.support.UserContext;
import com.zebone.platform.framework.support.IUser;
import com.zebone.platform.modules.dao.DataBaseHelper;
import com.zebone.platform.modules.exception.BusException;
import com.zebone.platform.modules.utils.JsonUtil;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;



@Service
public class CancelSettleService {
	
	@Autowired
	private BlIpPubMapper blIpPubMapper; 
	
	@Autowired
	private BalAccoutService balAccoutService;
	
	@Autowired
	private InvSettltService invSettltService;
	@Autowired
	private DumpPubService dumpPubService;

	/**
	 * 取消结算
	 * @param param 结算主键
	 * @param user
	 * @throws Exception 
	 */
	@SuppressWarnings("unchecked")
	public List<BlDeposit > cancelSettle(String param, IUser user) throws Exception {
		Map<String,Object> paraMap = JsonUtil.readValue(param, Map.class);
		String pkSettle = (String)paraMap.get("pkSettle");
		BlSettle stVo = blIpPubMapper.QryBlSettleByPk(pkSettle);
		String pk_pv = stVo.getPkPv();
		PvEncounter pvvo =  DataBaseHelper.queryForBean("Select * from pv_encounter where pk_pv = ?", PvEncounter.class, pk_pv);
		User userInfo = (User)user;
		//1.写入被取消结算的反交易记录，写表bl_settle；返回值为反结算Vo
		Date current = new Date();
		stVo.setDateSt(current);
		BlSettle canStVo = stInfo(stVo, userInfo);
//		2	查询被取消结算对应的记费明细，并基于此生成相反的记费记录，关联反结算记录，写bl_ip_st表，除以下字段外，其余字段值同原记费记录值；
		List<BlIpDt> cgVos = blIpPubMapper.QryBlIpDtBySt(pkSettle);
		List<BlIpDt> cgVosC = new ArrayList<BlIpDt>();
		for(BlIpDt vo : cgVos){
			BlIpDt voc = (BlIpDt) vo.clone();
			voc.setQuan(vo.getQuan()*-1);
			voc.setAmount(vo.getAmount()*-1); 
			voc.setAmountPi(vo.getAmountPi()*-1);
			voc.setAmountHppi(vo.getAmountHppi()*-1);
			if(vo.getAmountAdd()!=null)
				voc.setAmountAdd(vo.getAmountAdd()*-1);
			voc.setPkSettle(canStVo.getPkSettle());
			voc.setPkCgip(NHISUUID.getKeyId());
			voc.setDateCg(current);
			voc.setCreateTime(new Date());
			cgVosC.add(voc);
		}
		
		cgVosC = clCgipBack(cgVos,cgVosC);
		DataBaseHelper.batchUpdate(DataBaseHelper.getInsertSql(BlIpDt.class), cgVosC);
//		3	基于被取消结算对应的记费明细生成未结算的记费记录，写bl_ip_st表，除以下字段外，其余字段值同原记费记录值；
		List<BlIpDt> cgVosN = new ArrayList<BlIpDt>();
		for( BlIpDt vo :cgVos){
			BlIpDt voc = (BlIpDt) vo.clone();
			voc.setPkSettle(null);
			voc.setFlagSettle("0");
			voc.setFlagInsu("0");
			voc.setPkCgip(NHISUUID.getKeyId());
			voc.setDateCg(current);
			voc.setCreateTime(new Date());
			cgVosN.add(voc);
		}
		
		cgVosN = clCgipBack(cgVos,cgVosN);
		
		DataBaseHelper.batchUpdate(DataBaseHelper.getInsertSql(BlIpDt.class), cgVosN);
//		4	取消收付款表的住院预交金和被取消结算的关联；
		DataBaseHelper.execute("update bl_deposit   set pk_settle = null,flag_settle = '0' where pk_settle = ? and eu_dptype = '9'", pkSettle);
//		5	写入基于收付款表的结算类型记录的反交易记录，写表bl_deposit，除以下字段外，其余字段同原结算收付款记录；
		List<BlDeposit> depos = blIpPubMapper.QryBlDepositBySt(pkSettle);
		//写入预交金退款负记录
		if (depos.size()>0){ addPaymentRefundRecord(current,depos,userInfo); }
		for( int i = 0;i<depos.size();++i){
			BlDeposit depo = depos.get(i);
			if(EnumerateParameter.ONE.equals(depo.getEuDirect()) && EnumerateParameter.FOUR.equals(depo.getDtPaymode())){
				if(EnumerateParameter.ONE.equals(depo.getEuDptype()) || EnumerateParameter.ZERO.equals(depo.getEuDptype())){
					dealAcc(stVo,pvvo,depo.getAmount().multiply(new BigDecimal(-1)));
				}
			}
			depo.setEuDptype("4");
			int direct = Integer.parseInt(depo.getEuDirect());
			depo.setEuDirect((-1*direct)+"");
			depo.setAmount(depo.getAmount().multiply(new BigDecimal(-1)));
			depo.setDatePay(current);
			depo.setPkDept(userInfo.getPkDept());
			depo.setPkEmpPay(userInfo.getPkEmp());
			depo.setNameEmpPay(userInfo.getNameEmp());
			depo.setFlagSettle("1");
			depo.setPkSettle(canStVo.getPkSettle());
			depo.setFlagCc("0");
			depo.setPkCc(null);
			depo.setPkStMid(null);
			depo.setNote("取消结算");
			depo.setCodeDepo(ApplicationUtils.getCode("0606"));

			if(EnumerateParameter.THREE.equals(depo.getDtPaymode())&&!DateUtils.dateToStr("yyyyMMdd", depo.getDatePay()).equals(DateUtils.dateToStr("yyyyMMdd", new Date()))){
				//银行卡缴费只能退当天的，不是当天的只能退现金
				depo.setDtPaymode(EnumerateParameter.ONE);
			}
			depo.setPkDepo(NHISUUID.getKeyId());
		}
		DataBaseHelper.batchUpdate(DataBaseHelper.getInsertSql(BlDeposit.class), depos);
		
		
		
		
//		6	如果被取消的结算为中途结算，冲销pk_st_mid为中途结算主键的记录；
		//0X: 门诊流程： 00 门诊收费结算，01 门诊挂号结算 ；
		//1X 住院流程：10 出院结算，11中途结算；
		//2X 反流程结算：20 冲账，21 取消结算

		if("11".equals(stVo.getDtSttype())){
			List<BlDeposit> deposRet = blIpPubMapper.QryBlDepositByMidSt(pkSettle);
				if(deposRet!=null && deposRet.size()>0){
					for(BlDeposit depo : deposRet){
						depo.setEuDptype("9");
						depo.setEuDirect("-1");
						depo.setAmount(depo.getAmount().multiply(new BigDecimal(-1)));
						depo.setDatePay(current);
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
						depo.setCodeDepo(ApplicationUtils.getCode("0606"));
					}
				}
			DataBaseHelper.batchUpdate(DataBaseHelper.getInsertSql(BlDeposit.class), deposRet);
		}
		
     if("10".equals(stVo.getDtSttype()) && EnumerateParameter.TWO.equals(stVo.getEuStresult())){
    	 //如果被取消的结算为出院结算，结算结果为存款结算（eu_stresult=2），要将存入患者账户的存款冲销回原状，冲销前要调用查询患者账户服务，确认患者账户金额是否足以冲销，如果患者账户金额不足，将不允许取消结算。
    	 List<BlDepositPi> deposRe = blIpPubMapper.QryBlDepositPiBySt(pkSettle);
    	 BigDecimal amtAcc = BigDecimal.ZERO;
    	 for(BlDepositPi depo : deposRe){				
				amtAcc= amtAcc.add(depo.getAmount());
		}
    	Map<String,Object> amtAccMap = DataBaseHelper.queryForMap("select  Amt_acc from pi_acc piacc  inner join pv_encounter pv on piacc.pk_pi = pv.pk_pi where  pv.pk_pv = ?", stVo.getPkPv());
  		BigDecimal amtAccNow = (BigDecimal)amtAccMap.get("amtAcc");
  		if(amtAccNow.compareTo(amtAcc)>=0){
  			BlDepositPi blDepoPi = new BlDepositPi();
  	 		blDepoPi.setPkDepopi(NHISUUID.getKeyId());
  	 		blDepoPi.setPkPi(stVo.getPkPi());
  	 		blDepoPi.setEuDirect("1");
  	 		blDepoPi.setAmount(amtAcc.multiply(new BigDecimal(-1)));//存款金额
  	 		blDepoPi.setDtPaymode("5");//（内部转账）
  	 		blDepoPi.setDatePay(current);//当前日期
  	 		blDepoPi.setPkDept(UserContext.getUser().getPkDept());//当前部门
  	 		blDepoPi.setPkEmpPay(UserContext.getUser().getPkEmp());//当前用户
  	 		blDepoPi.setNameEmpPay(UserContext.getUser().getNameEmp());//当前用户姓名
  	 		blDepoPi.setReptNo("");
  	 		balAccoutService.saveMonOperation(blDepoPi,user,null,null,blDepoPi.getDtPaymode());
  		}else{
  			throw new  BusException("账户余额:"+amtAccNow.doubleValue()+"不足，小于冲销所需:"+amtAcc+",取消结算失败!");
  		}    	 
     }
     
     //获取BL0031（收费结算启用电子票据），参数值为1时调用冲红电子票据接口
     //String eBillFlag = ApplicationUtils.getSysparam("BL0031", false);
     String eBillFlag = invSettltService.getBL0031ByNameMachine(paraMap.containsKey("nameMachine")?CommonUtils.getString(paraMap.get("nameMachine")):null);
	 if(!CommonUtils.isEmptyString(eBillFlag) && "1".equals(eBillFlag)){
		 try{
			 invSettltService.billCancelNoNewTrans(pkSettle, user);
		 }catch (Exception e) {
				throw new BusException("取消结算失败：" + e.getMessage());
		}
	 }
     
//		7	作废被取消结算关联的发票；
     StringBuffer sql = new StringBuffer("update bl_invoice");
     sql.append(" set flag_cancel = '1',");
     sql.append(" date_cancel =?,");
     sql.append(" pk_emp_cancel =?,");
     sql.append(" name_emp_cancel =?");
     sql.append(" where exists (select 1 from bl_st_inv stinv");
     sql.append(" inner join bl_settle st on stinv.pk_settle=st.pk_settle");
     sql.append(" where stinv.pk_invoice=bl_invoice.pk_invoice and st.pk_settle = ?)");
     DataBaseHelper.execute(sql.toString(),current,userInfo.getPkEmp(),userInfo.getNameEmp(),pkSettle);
     //设置pvEncounter
     if(!"11".equals(stVo.getDtSttype()))
		DataBaseHelper.execute("update pv_encounter set flag_settle = '0',eu_status = '2' where pk_pv = ? ", pk_pv);
     
     //修改原结算记录的flag_canl为“1”
     DataBaseHelper.execute("update bl_settle set flag_canc='1' where pk_settle = ?", pkSettle);
     
     paraMap.put("codeEmp", userInfo.getCodeEmp());
     paraMap.put("nameEmp", userInfo.getNameEmp());
     paraMap.put("pkPv", stVo.getPkPv());
     PlatFormSendUtils.sendBlCancelSettleMsg(paraMap);
     paraMap = null;
     //恢复数据
     List<String> list=new ArrayList<String>();
     list.add(pk_pv);
     dumpPubService.recoveryDataByPkpv(list, "3");
     
     return  depos;
	}
/**
 * @Description  添加预交金负记录
 * @auther wuqiang
 * @Date 2019/8/17
 * @Param [depos, userInfo]
 * @return void
 */
   private  void  addPaymentRefundRecord (Date current,List<BlDeposit> depos,User userInfo){

	   if ("1".equals(ApplicationUtils.getSysparam("BL0042", false))){
		   for (BlDeposit blDeposit:depos){
			   if ("-1".equals(blDeposit.getEuDirect())){
				   BlDeposit blDeposit2=new BlDeposit();
				   ApplicationUtils.copyProperties(blDeposit2,blDeposit);
				   blDeposit2.setEuDptype("9");
				   blDeposit2.setAmount(blDeposit.getAmount());
				   blDeposit2.setDtPaymode(blDeposit.getDtPaymode());
				   blDeposit2.setEuDirect("-1");
				   blDeposit2.setDatePay(current);
				   blDeposit2.setPkDept(userInfo.getPkDept());
				   blDeposit2.setPkEmpPay(userInfo.getPkEmp());
				   blDeposit2.setNameEmpPay(userInfo.getNameEmp());
				   blDeposit2.setFlagSettle("0");
				   blDeposit2.setFlagCc("0");
				   blDeposit2.setPkCc(null);
				   blDeposit2.setPkStMid(null);
				   blDeposit2.setNote("取消结算-预交金退款冲负");
				   blDeposit2.setCodeDepo(ApplicationUtils.getCode("0606"));
				   ApplicationUtils.setDefaultValue(blDeposit2,true);
				   DataBaseHelper.insertBean(blDeposit2);
			   }
		   }

	   }


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


	private void dealAcc(BlSettle stVo, PvEncounter pvvo, BigDecimal amtAcc) {
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
				pad.setDateHap(stVo.getDateSt());
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


		private BlSettle  stInfo(BlSettle stVoTemp,User userInfo){
	     BlSettle stVo = (BlSettle)stVoTemp.clone();
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
		stVo.setAmountRound(stVo.getAmountRound()!=null?new BigDecimal(-1).multiply(stVo.getAmountRound()):BigDecimal.valueOf(0D));
		stVo.setPkOrgSt(userInfo.getPkOrg());
		stVo.setPkDeptSt(userInfo.getPkDept());
		stVo.setPkEmpSt(userInfo.getPkEmp());
		stVo.setNameEmpSt(userInfo.getNameEmp());
		stVo.setFlagCc("0");
		stVo.setPkCc(null);
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
				stAr.setDatePay(stVo.getDateSt());
				ApplicationUtils.setDefaultValue(stAr, true);
			}
			DataBaseHelper.batchUpdate(DataBaseHelper.getInsertSql(BlSettleAr.class),stArList);
		}
		
		return stVo;
	}

}
