package com.zebone.nhis.pro.zsba.compay.daysettle.pub.service;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.shiro.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zebone.nhis.pro.zsba.compay.daysettle.pub.dao.ZsBlIpCcMapper;
import com.zebone.nhis.pro.zsba.compay.daysettle.pub.vo.ZsAmtInsuVo;
import com.zebone.nhis.pro.zsba.compay.daysettle.pub.vo.ZsBaBlSumVo;
import com.zebone.nhis.pro.zsba.compay.daysettle.pub.vo.ZsBlCcDetails;
import com.zebone.nhis.pro.zsba.compay.daysettle.pub.vo.ZsBlCcDt;
import com.zebone.nhis.pro.zsba.compay.daysettle.pub.vo.ZsBlSumDepoVo;
import com.zebone.nhis.pro.zsba.compay.daysettle.pub.vo.ZsBlSumPayVo;
import com.zebone.nhis.pro.zsba.compay.daysettle.pub.vo.ZsBlSumRecords;
import com.zebone.nhis.pro.zsba.compay.daysettle.pub.vo.ZsBlSumVo;
import com.zebone.nhis.pro.zsba.compay.daysettle.pub.vo.ZsDepoRtnInfo;
import com.zebone.nhis.pro.zsba.compay.daysettle.pub.vo.ZsDepositInv;
import com.zebone.nhis.pro.zsba.compay.daysettle.pub.vo.ZsGroupDaySt;
import com.zebone.nhis.pro.zsba.compay.daysettle.pub.vo.ZsGroupDayStSum;
import com.zebone.nhis.pro.zsba.compay.daysettle.pub.vo.ZsInvInfo;
import com.zebone.nhis.pro.zsba.compay.daysettle.pub.vo.ZsInvalidStInv;
import com.zebone.nhis.pro.zsba.compay.daysettle.pub.vo.ZsPayerData;
import com.zebone.nhis.pro.zsba.compay.daysettle.pub.vo.ZsQryParam;
import com.zebone.nhis.pro.zsba.compay.daysettle.pub.vo.ZsbaBackDepoInfo;
import com.zebone.nhis.pro.zsba.compay.daysettle.pub.vo.ZsbaBackInvInfo;
import com.zebone.nhis.pro.zsba.compay.daysettle.pub.vo.ZsbaBlCc;
import com.zebone.nhis.pro.zsba.compay.daysettle.pub.vo.ZsbaBlCcDs;
import com.zebone.nhis.pro.zsba.compay.daysettle.pub.vo.ZsbaPayTypeAmount;
import com.zebone.nhis.common.module.bl.BlCc;
import com.zebone.nhis.common.module.bl.BlCcInv;
import com.zebone.nhis.common.module.bl.BlCcPay;
import com.zebone.nhis.common.module.bl.BlDeposit;
import com.zebone.nhis.common.module.bl.BlSettle;
import com.zebone.nhis.common.module.mybatis.ReflectHelper;
import com.zebone.nhis.common.support.ApplicationUtils;
import com.zebone.nhis.common.support.CommonUtils;
import com.zebone.nhis.common.support.DateUtils;
import com.zebone.nhis.common.support.EnumerateParameter;
import com.zebone.nhis.common.support.MathUtils;
import com.zebone.platform.Application;
import com.zebone.platform.common.support.NHISUUID;
import com.zebone.platform.common.support.User;
import com.zebone.platform.common.support.UserContext;
import com.zebone.platform.framework.support.IUser;
import com.zebone.platform.modules.dao.DataBaseHelper;
import com.zebone.platform.modules.exception.BusException;
import com.zebone.platform.modules.utils.JsonUtil;

@Service
public class ZsIpBlCcService {
	
	@Autowired
	private ZsBlIpCcMapper ipBlCcMapper;
	
	/**
	 * 查询已结账记录的明细
	 * @param param pkCc 结账主键
	 * @param user 
	 */
	@SuppressWarnings("unchecked")
	public ZsBlCcDetails QryClosedDetails(String param,IUser user){
		Map<String,Object> params = JsonUtil.readValue(param, Map.class);
		ZsBlCcDetails res = new ZsBlCcDetails();
		String pkCc = (String)params.get("pkCc");
		res.setDepo(ipBlCcMapper.getBlCcDepoDetail(pkCc));
		res.setDepoSt(ipBlCcMapper.getBlCcStDetail(pkCc));
		res.setDepoStGet(ipBlCcMapper.getBlCcStGetDetail(pkCc));
		
		return res;
	}
	
	/**
	 * 查询日结账记录选中明细的状态
	 * @param param pkCc 结账主键
	 * @param user 
	 */
	@SuppressWarnings("unchecked")
	public BlCc QueryFocus(String param,IUser user){
		Map<String,Object> params = JsonUtil.readValue(param, Map.class);
		if(CommonUtils.isNotNull(params.get("dateBegin"))){
			params.put("dateBegin", CommonUtils.getString(params.get("dateBegin")).substring(0, 8)+"000000");
		}
		if(CommonUtils.isNotNull(params.get("dateEnd"))){
			params.put("dateEnd", CommonUtils.getString(params.get("dateEnd")).substring(0, 8)+"235959");
		}
		BlCc bc = new BlCc();
		bc = ipBlCcMapper.getEuStatus(params);		
		return bc;
	}
	
	/**
	 * 查询未结账记录的明细
	 * @param param pkCc 结账主键
	 * @param user 
	 */
	@SuppressWarnings("unchecked")
	public ZsBlCcDetails QryUncloseDetails(String param,IUser user){
		Map<String,Object> params = JsonUtil.readValue(param, Map.class);
		ZsBlCcDetails res = new ZsBlCcDetails();
		if(params==null)return res;
		params.put("pkEmp", UserContext.getUser().getPkEmp());
		params.put("pkOrg", UserContext.getUser().getPkOrg());
		res.setDepo(ipBlCcMapper.getBlCcDepoDetail_Unclose(params));
		res.setDepoSt(ipBlCcMapper.getBlCcStDetail_Unclose(params));
		res.setDepoStGet(ipBlCcMapper.getBlCcStGetDetail_Unclose(params));
		return res;
	}
	
	
	
	/**
	 * 取消结账
	 * @param param pkCc
	 * @param user
	 */
	@SuppressWarnings("unchecked")
	public void CancelCc(String param,IUser user){
		Map<String,Object> params = JsonUtil.readValue(param, Map.class);
		String pkCc = (String)params.get("pkCc");
		List<Map<String,Object>> cancelData = ipBlCcMapper.cancelData(pkCc);
		if(cancelData!=null && cancelData.size()>0){
			if(cancelData.get(0)!=null){
				int cnt = 0;
				if(Application.isSqlServer()){
					cnt = (Integer)cancelData.get(0).get("cnt");
				}else{
					BigDecimal cntB = (BigDecimal)cancelData.get(0).get("CNT");
					cnt = cntB.intValue();
				}
				if(cnt>0){
					throw new BusException("只能取消最大日期的结账!");
				}
			}
		}
		ipBlCcMapper.delBlCcPay(pkCc);
		ipBlCcMapper.delBlCcInv(pkCc);
		ipBlCcMapper.delBlCc(pkCc);
		ipBlCcMapper.delBlCcDs(pkCc);
		ipBlCcMapper.updBlSt(pkCc);
		ipBlCcMapper.updBlDepo(pkCc);
		ipBlCcMapper.updBlInvoice(pkCc);
		ipBlCcMapper.updBlInvCanc(pkCc);
		ipBlCcMapper.updBlStArCanc(pkCc);
		ipBlCcMapper.updBlDepoReplenish(pkCc);
	}
	
	
	/**
	 * 查询已结账信息
	 * @param param pkCC
	 * @param user
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public ZsBlCcDt QryBlCcInfo(String param, IUser user) {	
		Map<String,Object> params = JsonUtil.readValue(param, Map.class);
		String pkCc = (String)params.get("pkCc");
		ZsBlCcDt res = new ZsBlCcDt();
		ZsbaBlCc blcc = ipBlCcMapper.qryBlCcByPk(pkCc);
		List<BlCcPay> pays = ipBlCcMapper.qryBlCcPayByPk(pkCc);
		//2 实收合计
        Double amtTemp = MathUtils.add(blcc.getAmtSettle(), blcc.getAmtPrepRt());
        Double amtTemp1 = MathUtils.sub(amtTemp, blcc.getAmtPrepRt());
        //查询欠款总金额
        Double amtAr = ipBlCcMapper.qryStAmtAr(params);
        res.setAmtGet(MathUtils.sub(amtTemp1, blcc.getAmtInsu()));
		res.setBlCc(blcc);
		res.setBlCcPayList(pays);
		res.setAmtAr(amtAr==null?0.0:amtAr);
        //2.9付款方信息
        List<ZsPayerData> payerList = ipBlCcMapper.qryPayerDataRj(params);
        res.setPayerList(payerList);
        
        int invCancelCnt = 0;
        int invBackCnt = 0;
        if(blcc.getInvInfoCanc()!=null && blcc.getInvInfoCanc().length()>0){
        	String[] cancArr = blcc.getInvInfoCanc().substring(0, blcc.getInvInfoCanc().length()-1).split(",");
        	invCancelCnt = cancArr.length;//重打加退款
        	params.put("invInfoCanc", cancArr);
        	List<Map<String,Object>> invBackCntList = ipBlCcMapper.qryCcStInvRetreatCnt(params);//重打的
        	invBackCnt =  Integer.parseInt(invBackCntList.get(0).get("cnt").toString());
        }

        res.setCntRetreatFp(invBackCnt+"");
        res.setCntScrapFp((invCancelCnt-invBackCnt)+"");
        
        List<Map<String, Object>> cntRetreatInfo = ipBlCcMapper.qryCntRetreat(params);
        List<Map<String, Object>> cntScrapInfo = ipBlCcMapper.qryCntScrap(params);
        res.setCntRetreat(cntRetreatInfo.get(0).get("cntRetreat").toString());
        res.setCntScrap(cntScrapInfo.get(0).get("cntScrap").toString());
		return res;
	}
	
	/**
	 * 查询已结账信息
	 * @param param pkCC
	 * @param user
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public ZsBlCcDt QryBlCcInfoZsba(String param, IUser user) {	
		Map<String,Object> params = JsonUtil.readValue(param, Map.class);
		String pkCc = (String)params.get("pkCc");
		ZsBlCcDt res = new ZsBlCcDt();
		ZsbaBlCc blcc = ipBlCcMapper.qryBlCcByPk(pkCc);
		
		ZsbaBlCcDs blCcDs = ipBlCcMapper.qryBlCcDsByPk(pkCc);
		
		//收预交金、结算退款、已经出院病人欠费后交款的各支付方式的金额
        ZsbaPayTypeAmount zpta =  createPta(params);
        res.setPta(zpta);
        
        res.setBlCc(blcc);
        res.setBlCcDs(blCcDs);
        //退、费预交金票据信息
        List<ZsbaBackDepoInfo> backDepoInfoList = ipBlCcMapper.qryBackDepoInfo(params);
        res.setBackDepoInfoList(backDepoInfoList);
        //退票、废票信息
        params.put("pkEmp", blcc.getPkEmpOpera());
        List<ZsbaBackInvInfo> backInvInfoList = ipBlCcMapper.qryBackInvInfo(params);
        res.setBackInvInfoList(backInvInfoList);
        //内部转账
        List<ZsbaBackDepoInfo> nbzzDepoInfoList = ipBlCcMapper.qryNbzzList(params);
        res.setNbzzDepoInfoList(nbzzDepoInfoList);
		return res;
	}
	
	
	/**
	 * 产生结账信息
	 * @param param dateEnd结账截止时间
	 * @param user
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public ZsBlCcDt CreateCcInfo(String param, IUser user){
		
		//1.参数获取
		Map<String,Object> params = JsonUtil.readValue(param, Map.class);
		String dateEnd  = (String)params.get("dateEnd");
		
		ZsBlCcDt res = new ZsBlCcDt();
		if(CommonUtils.isEmptyString(dateEnd))return res;
        return creat(dateEnd);
	}

	/**
	 * 产生结账信息（财务科设计的报表）
	 * @param param dateEnd结账截止时间
	 * @param user
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public ZsBlCcDt CreateCcInfoZsba(String param, IUser user){
		
		//1.参数获取
		Map<String,Object> params = JsonUtil.readValue(param, Map.class);
		String dateEnd  = (String)params.get("dateEnd");
		String pkEmp  = (String)params.get("pkEmp");
		ZsBlCcDt res = new ZsBlCcDt();
		if(CommonUtils.isEmptyString(dateEnd))return res;
        return creatZsba(dateEnd, pkEmp);
	}
	
	/**
	 * 结账
	 * @param param dateEnd结账截止时间
	 * @param user
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public ZsBlCcDt CloseCounter(String param, IUser user){
		//1.参数获取
		Map<String,Object> params = JsonUtil.readValue(param, Map.class);
		String dateEnd  = (String)params.get("dateEnd");
		ZsBlCcDt res = creat(dateEnd);
		if(res !=null){
			Double amtGet = res.getAmtGet();
			Collection<BlCcPay> payVos = res.getBlCcPayList();
			Double amtCheck = 0.0;
			for(BlCcPay vo : payVos){
				amtCheck = MathUtils.add(amtCheck, vo.getAmt());
				Double temp = vo.getAmtBack()==null?0.00:vo.getAmtBack();
				amtCheck = MathUtils.add(amtCheck, temp);
			}
			//2.结账校验
			if(amtGet.compareTo(amtCheck)!=0){
				//throw new BusException("结账数据不平，请检查!");
			}
			
			//3.写结账表bl_cc；
			ZsbaBlCc blCc = res.getBlCc();
			List<Map<String, Object>>AmtCollectList = ipBlCcMapper.qryDepoAmtCollect(params);
	        if(AmtCollectList.get(0)==null){
	        	blCc.setAmtPi(0.00);
	        }else{
	        	blCc.setAmtPi(AmtCollectList.get(0).get("amount")==null?0.00:Double.parseDouble(AmtCollectList.get(0).get("amount").toString()));
	        }
			List<Map<String, Object>>AmtBackList = ipBlCcMapper.qryDepoAmtBack(params);
	        if(AmtBackList.get(0)==null){
	        	blCc.setAmtPiBack(0.00);
	        }else{
	        	blCc.setAmtPiBack(AmtBackList.get(0).get("amount")==null?0.00:Double.parseDouble(AmtBackList.get(0).get("amount").toString()));
	        }
			
			blCc.setEuCctype("8");
			//blCc.setDateEnd(dateTrans(dateEnd));
			//handleDateBegin(blCc);
			blCc.setDateCc(new Date());
			blCc.setFlagClear("0");
			blCc.setFlagLeader("0");
			blCc.setEuStatus("0");
			blCc.setPkDept(UserContext.getUser().getPkDept());//结账科室
			blCc.setPkCc(NHISUUID.getKeyId());
			setDefaultValue(blCc, true);
			DataBaseHelper.insertBean(blCc);

			//4 .写结账付款表bl_cc_pay；
			for(BlCcPay vo : payVos ){
				vo.setPkCc(blCc.getPkCc());
				vo.setPkOrg(UserContext.getUser().getPkOrg());
				vo.setPkCcpay(NHISUUID.getKeyId());
				setDefaultValue(vo, true);
				if(!EnumerateParameter.ONE.equals(vo.getEuDirect()) && 
						EnumerateParameter.ZERO.equals(vo.getEuPaytype())){
					vo.setAmtBack(vo.getAmt());
					vo.setCntTradeBack(vo.getCntTrade());
					
					vo.setAmt(0D);
					vo.setCntTrade(0L);
				}else{
					vo.setAmtBack(0D);
					vo.setCntTradeBack(0L);
				}
			}
			List pays = (payVos instanceof List)?(List)payVos:new ArrayList(payVos);			  
			DataBaseHelper.batchUpdate(DataBaseHelper.getInsertSql(BlCcPay.class), pays);
			
			//5.写结账发票表bl_cc_inv；
			List<BlCcInv> blCcInvs = handleInvCc(res,blCc.getPkCc());
			DataBaseHelper.batchUpdate(DataBaseHelper.getInsertSql(BlCcInv.class), blCcInvs);
			//6.更新业务表
			updBlInfo(blCc.getPkCc(),blCc.getPkEmpOpera(),DateUtils.strToDate(dateEnd),UserContext.getUser().getPkOrg());
			
			//写结账字段扩展表bl_cc_ds
			ZsbaBlCcDs ccDs = new ZsbaBlCcDs();
			ccDs.setPkCc(blCc.getPkCc());
			
			params.put("pkCc", blCc.getPkCc());
			//收预交金、结算退款、已经出院病人欠费后交款的各支付方式的金额
	        ZsbaPayTypeAmount zpta =  createPta(params);
	        res.setPta(zpta);
	        ccDs.setAmtSsXj(zpta.getPreXj()+zpta.getStBackXj()+zpta.getStCollXj());
	        ccDs.setAmtSs(blCc.getAmtSettle()+blCc.getAmtPrep()-blCc.getAmtPrepRt()-blCc.getAmtInsu()-blCc.getAmtDisc());
	        
	        blCc.setAmtSor(zpta.getStCollHj());
	        blCc.setAmtCa(zpta.getStBackHj());
	        DataBaseHelper.updateBeanByPk(blCc);
	        res.setBlCc(blCc);
	        
	        ccDs.setAmtGz(blCc.getAmtGz());
	        
	        //科研结算费用
	        List<Map<String, Object>> kyList = ipBlCcMapper.qryAmtKy(params);
	        if(kyList.get(0)==null){
	        	ccDs.setAmtOther(0.00);
	        }else{
	        	ccDs.setAmtOther(kyList.get(0).get("amtKy")==null?0.00:Double.parseDouble(kyList.get(0).get("amtKy").toString()));
	        }
	        //因为医保金额和科研金额存在了一个字段，所以amtInsu减去科研费用才是医保费用
	        ccDs.setAmtIns(blCc.getAmtInsu()-ccDs.getAmtOther());
	        ccDs.setAmtTc(ccDs.getAmtIns()-ccDs.getAmtGz());//医保金额-个账=统筹金额
	        
	        //自付费用
	        List<Map<String, Object>> piList = ipBlCcMapper.qryAmtPi(params);
	        if(piList.get(0)==null){
	        	ccDs.setAmtIp(0.00);
	        }else{
	        	ccDs.setAmtIp(piList.get(0).get("amtPi")==null?0.00:Double.parseDouble(piList.get(0).get("amtPi").toString()));
	        }
	        
	        //作废的发票（不包含退费的）
	        List<ZsInvalidStInv> InvVoidList = ipBlCcMapper.qryStInvVoid(params);
	        if(InvVoidList!=null && InvVoidList.size()>0){
	        	StringBuffer invInfoCanc = new StringBuffer();
	        	for(int i = 0;i<InvVoidList.size();i++){
	        		ZsInvalidStInv vo = InvVoidList.get(i); 
	        		invInfoCanc.append(vo.getCodeInv());
	        		if(i+1==InvVoidList.size()){
	        			invInfoCanc.append(";");
	        		}else{
	        			invInfoCanc.append(",");
	        		}    		
	        	}
	        	ccDs.setInvVoidCode(invInfoCanc.toString());
	        	ccDs.setInvVoidNum(InvVoidList.size());
	        }
	        
	        //退费的发票
	        List<ZsInvalidStInv> InvBackList = ipBlCcMapper.qryStInvBack(params);
	        if(InvBackList!=null && InvBackList.size()>0){
	        	StringBuffer invInfoCanc = new StringBuffer();
	        	for(int i = 0;i<InvBackList.size();i++){
	        		ZsInvalidStInv vo = InvBackList.get(i); 
	        		invInfoCanc.append(vo.getCodeInv());
	        		if(i+1==InvBackList.size()){
	        			invInfoCanc.append(";");
	        		}else{
	        			invInfoCanc.append(",");
	        		}    		
	        	}
	        	ccDs.setInvRefundCode(invInfoCanc.toString());
	        	ccDs.setInvRefundNum(InvBackList.size());
	        }
	        
			//作废收据号码
	        List<ZsDepoRtnInfo> voRtnInfoVoid =  ipBlCcMapper.qryDepoInvVoid(params);
	        if(voRtnInfoVoid!=null && voRtnInfoVoid.size()>0 ){
	        	StringBuffer depoInfoRtn =new StringBuffer();
	        	for(int i=0;i<voRtnInfoVoid.size();i++){
	        		ZsDepoRtnInfo vo = voRtnInfoVoid.get(i);
	        		if(vo==null)
	        			continue;
	        		depoInfoRtn.append(vo.getReptNo());
	        		if(i+1==voRtnInfoVoid.size()){
	        			depoInfoRtn.append(";");
	        		}else{
	        			depoInfoRtn.append(",");
	        		}   		
	        	}
	        	ccDs.setDepoVoidCode(depoInfoRtn.toString());
	        	ccDs.setDepoVoidNum(voRtnInfoVoid.size());
	        }
	        
			//退费收据号码
	        List<ZsDepoRtnInfo> voRtnInfoBack =  ipBlCcMapper.qryDepoInvBack(params);
	        if(voRtnInfoBack!=null && voRtnInfoBack.size()>0 ){
	        	StringBuffer depoInfoRtn =new StringBuffer();
	        	for(int i=0;i<voRtnInfoBack.size();i++){
	        		ZsDepoRtnInfo vo = voRtnInfoBack.get(i);
	        		if(vo==null)
	        			continue;
	        		depoInfoRtn.append(vo.getReptNo());
	        		if(i+1==voRtnInfoBack.size()){
	        			depoInfoRtn.append(";");
	        		}else{
	        			depoInfoRtn.append(",");
	        		}   		
	        	}
	        	ccDs.setDepoRefundCode(depoInfoRtn.toString());
	        	ccDs.setDepoRefundNum(voRtnInfoBack.size());
	        }
	        DataBaseHelper.insertBean(ccDs);
	        res.setBlCcDs(ccDs);
	        
	      //退票、废票信息
	        List<ZsbaBackInvInfo> backInvInfoList = ipBlCcMapper.qryBackInvInfo(params);
	        res.setBackInvInfoList(backInvInfoList);
	        //退、费预交金票据信息
	        List<ZsbaBackDepoInfo> backDepoInfoList = ipBlCcMapper.qryBackDepoInfo(params);
	        res.setBackDepoInfoList(backDepoInfoList);
	        
	        //内部转账
	        List<ZsbaBackDepoInfo> nbzzDepoInfoList = ipBlCcMapper.qryNbzzList(params);
	        res.setNbzzDepoInfoList(nbzzDepoInfoList);
		}
		return res;
	}

	private void updBlInfo(String pkCc, String pkEmp,Date date,String pkOrg) {
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("pkCc", pkCc);
		map.put("pkEmp", pkEmp);
		map.put("pkOrg", pkOrg);
		map.put("dateSt", new Date());
		Object[] args = new Object[] {pkCc,pkOrg,pkEmp,date}; 
		String sqlSt = "update bl_settle   set flag_cc = 1,pk_cc =  ? where eu_pvtype = 3 and flag_cc = 0 and pk_org =  ? and   pk_emp_st =  ? and date_st <? ";
		DataBaseHelper.execute(sqlSt.toString(), args);
		
		String sqlDepo = " update bl_deposit set flag_cc = 1, pk_cc = ? where flag_cc = 0 and (eu_pvtype = 3 or eu_pvtype = 11) and pk_org = ? and pk_emp_pay = ? and create_time < ? ";
		DataBaseHelper.execute(sqlDepo.toString(), args);

		//ipBlCcMapper.updBlSettle(map);
		//ipBlCcMapper.updBlDeposit(map);
		StringBuffer sqlUpd = new StringBuffer(" update bl_invoice  set flag_cc = 1,pk_cc = ?");
		sqlUpd.append(" where exists (select 1  from bl_settle st  inner join bl_st_inv si on st.pk_settle=si.pk_settle");
		sqlUpd.append(" where bl_invoice.pk_invoice=si.pk_invoice and  st.pk_org = ? and st.eu_pvtype = 3) and ");
		sqlUpd.append(" pk_emp_inv = ? and flag_cc = 0 and  date_inv <= ?  ");       
		DataBaseHelper.execute(sqlUpd.toString(), args);
		StringBuffer sqlCancUpd = new StringBuffer(" update bl_invoice  set flag_cc_cancel = 1, pk_cc_cancel = ?");
		sqlCancUpd.append("  where exists (select 1 from bl_settle st inner join bl_st_inv si on st.pk_settle=si.pk_settle");
		sqlCancUpd.append("  where bl_invoice.pk_invoice=si.pk_invoice and st.pk_org = ? and st.eu_pvtype = 3) and");
		sqlCancUpd.append("  pk_emp_cancel = ? and  flag_cc_cancel = 0 and date_cancel <= ?");
		DataBaseHelper.execute(sqlCancUpd.toString(), args);
		//修改BL_SETTLE_AR
		DataBaseHelper.execute(
				"update bl_settle_ar set flag_cc='1',pk_cc=? where pk_org=? and pk_emp_pay=? and flag_cc='0' and date_pay<=?",
				args);
		
//		ipBlCcMapper.updInv(map);
//		ipBlCcMapper.updInvCanc(map);
	}


	private List<BlCcInv> handleInvCc(ZsBlCcDt data,String pkCc) {
		List<BlCcInv> res = new ArrayList<BlCcInv>();
		String pkOrg = UserContext.getUser().getPkOrg();
		if(data.getInvalidList()!=null){
			for(ZsInvalidStInv item :data.getInvalidList()){
				BlCcInv  vo = new BlCcInv();
				vo.setBeginNo(item.getCodeInv());
				vo.setEndNo(null);
				vo.setPkInvcate(item.getPkInvcate());
				vo.setFlagCanc("1");
				vo.setFlagWg("0");
				vo.setPkCc(pkCc);
				vo.setPkOrg(pkOrg);
				vo.setPkCcinv(NHISUUID.getKeyId());
				res.add(vo);
			}
		}
		if(data.getInvInfo()!=null){
			for(ZsInvInfo item :data.getInvInfo()){
				BlCcInv  vo = new BlCcInv();
				vo.setBeginNo(item.getBegincode());
				vo.setEndNo(item.getEndcode());
				vo.setPkInvcate(item.getPkInvcate());
				vo.setFlagCanc("0");
				vo.setFlagWg("0");
				vo.setPkCc(pkCc);
				vo.setPkOrg(pkOrg);
				vo.setPkCcinv(NHISUUID.getKeyId());
				res.add(vo);
			}		
		}
		
		return res;
	}


	private void handleDateBegin(ZsbaBlCc cc) {
		Date dateBegin;
		Date dateEnd = cc.getDateEnd();
		BlCc blCcLast = null;
		if(Application.isSqlServer()){ //是否sqlserver
			 blCcLast = ipBlCcMapper.getLastBlCcByPkEmpForSql(cc.getPkEmpOpera());
		}else{
			 blCcLast = ipBlCcMapper.getLastCcByPkEmp(cc.getPkEmpOpera());
		}
		
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
			BlSettle blSettle = null;
			BlDeposit blDeposit = null;
			if(Application.isSqlServer()){
				blSettle = ipBlCcMapper.getLastBlSettleByPkEmpForSql(cc.getPkEmpOpera());
			}else{
				blSettle = ipBlCcMapper.getLastStByPkEmp(cc.getPkEmpOpera());	
			}
			if(Application.isSqlServer()){
				blDeposit = ipBlCcMapper.getLastPreByPkEmpForSql(cc.getPkEmpOpera());
			}else{
				blDeposit = ipBlCcMapper.getLastPreByPkEmp(cc.getPkEmpOpera());	
			}
			if(blDeposit != null){
				dateBegin = blDeposit.getDatePay();
				if(dateEnd.getTime() < dateBegin.getTime()){
					throw new BusException("截止时间不能早于最早的预交金收款时间！");
				}
			}
			else if(blSettle != null){
				dateBegin = blSettle.getDateSt();
				if(dateEnd.getTime() < dateBegin.getTime()){
					throw new BusException("截止时间不能早于最早的结算时间！");
				}
			}
			else{
				throw new BusException("该操作员不存在结算|预交金收款记录！");
			}
		}
		cc.setDateBegin(dateBegin);
	}
	
	/**
	 * 交易号：007003007014
	 * 查询医保金额(分为公医金额和普通医保金额)
	 * @param param
	 * @param user
	 * @return
	 */
	public List<ZsAmtInsuVo> qryAmtInsu(String param,IUser user){
		ZsQryParam qryParam = JsonUtil.readValue(param, ZsQryParam.class);
		Map<String,Object> paramMap = new HashMap<>();
		
		if(CommonUtils.isEmptyString(qryParam.getPkCc())){
			paramMap.put("dateEnd", qryParam.getDateEnd());
			paramMap.put("pkEmp",  UserContext.getUser().getPkEmp());
			paramMap.put("pkOrg",  UserContext.getUser().getPkOrg());
		}else{
			paramMap.put("pkCc", qryParam.getPkCc());
		}
		
		List<ZsAmtInsuVo> amtList = ipBlCcMapper.qryAmtInsu(paramMap);
		return amtList;
	}

	private ZsBlCcDt creat(String dateEnd) {
		String jdbcType = ApplicationUtils.getPropertyValue("jdbc.dialect", "");	//获取数据库连接字符串
		if(CommonUtils.isEmptyString(dateEnd))return null;
		Map<String,Object> params = new HashMap<String,Object >();
		params.put("dateEnd", dateEnd);
		params.put("pkEmp",  UserContext.getUser().getPkEmp());
		params.put("pkOrg",  UserContext.getUser().getPkOrg());
		ZsBlCcDt res = new ZsBlCcDt();
		ZsbaBlCc blCc = new ZsbaBlCc();
		blCc.setPkOrg(UserContext.getUser().getPkOrg());
		blCc.setPkEmpOpera(UserContext.getUser().getPkEmp());
		blCc.setPkOrg(UserContext.getUser().getPkOrg());
		blCc.setNameEmpOpera(UserContext.getUser().getNameEmp());
		blCc.setDateEnd(dateTrans(dateEnd));
		handleDateBegin(blCc);
		
		//2 .产生结账信息
		//2.1结算收款
        ZsbaBlCc voSt = ipBlCcMapper.qryStAmtInfo(params);
        blCc.setAmtInsu(voSt==null?0.0:voSt.getAmtInsu());
        blCc.setAmtPrepRt(voSt==null?0.0:voSt.getAmtPrepRt());
        blCc.setAmtSettle(voSt==null?0.0:voSt.getAmtSettle());;
        blCc.setAmtDisc(voSt==null?0.0:voSt.getAmtDisc());;
        blCc.setAmtGz(voSt==null?0.0:voSt.getAmtGz()==null?0.0:voSt.getAmtGz());
        List<ZsInvInfo> voStInv = new ArrayList<>();
        //2.2发票信息 
        if("com.zebone.platform.modules.dao.dialect.spi.OracleDialect".equals(jdbcType))//Oracle
        	voStInv = ipBlCcMapper.qryStInvOracle(params);
        else //SqlServer
        	voStInv = ipBlCcMapper.qryStInvSqlServer(params);
    	   
        res.setInvInfo(voStInv);
        if(voStInv!=null && voStInv.size()>0){
        	StringBuffer invInfo = new StringBuffer(); 
        	BigDecimal cntInv = BigDecimal.ZERO;
        	for(int i = 0;i<voStInv.size();i++){
        		ZsInvInfo vo = voStInv.get(i);
        		invInfo.append(vo.getBegincode());
        		invInfo.append("-");
        		invInfo.append(vo.getEndcode());
        		if(i+1==voStInv.size()){
        			invInfo.append(";");
        		}else{
        			invInfo.append(",");
        		}
        		
        		cntInv = cntInv.add(vo.getCnt());
        	}
        	blCc.setInvInfo(invInfo.toString());
        	blCc.setCntInv(cntInv.intValue());
        }
        
		//2.3作废发票
        List<ZsInvalidStInv> InvalidList = ipBlCcMapper.qryStInv_Invalid(params);
        res.setInvalidList(InvalidList);
        if(InvalidList!=null && InvalidList.size()>0){
        	StringBuffer invInfoCanc = new StringBuffer();
        	for(int i = 0;i<InvalidList.size();i++){
        		ZsInvalidStInv vo = InvalidList.get(i); 
        		invInfoCanc.append(vo.getCodeInv());
        		if(i+1==InvalidList.size()){
        			invInfoCanc.append(";");
        		}else{
        			invInfoCanc.append(",");
        		}    		
        	}
        	blCc.setInvInfoCanc(invInfoCanc.toString());
        	blCc.setCntInvCanc(InvalidList.size());
        }
        
        //作废和退费的数量
        //params.put("flagCc", "0");
        List<Map<String,Object>> invCancelCntList = ipBlCcMapper.qryStInvScrapCnt(params);//作废加退款
        List<Map<String,Object>> invBackCntList = ipBlCcMapper.qryStInvRetreatCnt(params);//退款

//    	params.put("dateRept", dateEnd.substring(0, 8));
//		params.put("pkEmpRept",  UserContext.getUser().getPkEmp());        
//		List<BlDeposit> qryReceiptsData = ipBlCcMapper.qryReceiptsData(params);
//		int receiptscnt = 0;
//		StringBuffer depostr = new StringBuffer();depostr.append("  ");
//		if(qryReceiptsData!=null && qryReceiptsData.size()>0){
//			receiptscnt = (Integer)qryReceiptsData.size();
//			for(int i=0;i<qryReceiptsData.size();i++){
//				BlDeposit depositvo = qryReceiptsData.get(i);
//				if(depositvo!=null){
//					depostr.append(depositvo.getReptNo());
//					depostr.append(",");
//        		}
//			}
//		}
		//2.4预交金信息
        BlCc voPrePay = ipBlCcMapper.qryPrepPay(params);
        blCc.setAmtPrep(voPrePay==null?0.0:voPrePay.getAmtPrep());
        blCc.setCntDepo(voPrePay==null?0:voPrePay.getCntDepo());
		//2.5收款收据号串
        List<ZsDepositInv> voDepoInv = new ArrayList<>();
        if("com.zebone.platform.modules.dao.dialect.spi.OracleDialect".equals(jdbcType))//Oracle
        	voDepoInv = ipBlCcMapper.qryDepoInvOracle(params);  
        else	//SqlServer
        	voDepoInv = ipBlCcMapper.qryDepoInvSqlServer(params);  

        if(voDepoInv !=null && voDepoInv.size()>0 && voDepoInv.get(0)!=null){
        	StringBuffer depoInfo = new StringBuffer();
        	for(int i= 0;i<voDepoInv.size();i++){
        		ZsDepositInv vo = voDepoInv.get(i);
        		if(vo!=null){
        			depoInfo.append(vo.getBegincoe());
            		depoInfo.append("-");
            		depoInfo.append(vo.getEndcoe());
        		}
        		if(i+1==voDepoInv.size()){
        			depoInfo.append(";");
        		}else{
        			depoInfo.append(",");
        		}
        	}
        	blCc.setDepoInfo(depoInfo.toString());
        }
		//2.6退款收据号码
        List<ZsDepoRtnInfo> voRtnInfo =  ipBlCcMapper.qryDepoInv_Rtn(params);
        params.put("flagCc", "0");
        List<Map<String, Object>> cntRetreatInfo = ipBlCcMapper.qryCntRetreat(params);
        List<Map<String, Object>> cntScrapInfo = ipBlCcMapper.qryCntScrap(params);
        
        
        if(voRtnInfo!=null && voRtnInfo.size()>0 ){
        	StringBuffer depoInfoRtn =new StringBuffer();
        	for(int i=0;i<voRtnInfo.size();i++){
        		ZsDepoRtnInfo vo = voRtnInfo.get(i);
        		if(vo==null)
        			continue;
        		depoInfoRtn.append(vo.getReptNo());
        		if(i+1==voRtnInfo.size()){
        			depoInfoRtn.append(";");
        		}else{
        			depoInfoRtn.append(",");
        		}   		
        	}
        	blCc.setDepoInfoBack(depoInfoRtn.toString());
        	blCc.setCntDepoBack((long) voRtnInfo.size());
        }
        
		//2.7付款方式信息
        List<BlCcPay> DepoInfo   = ipBlCcMapper.qryDepoInfo(params);
       Map<String,List<BlCcPay>> Map_1 = new HashMap<String,List<BlCcPay>>();
       Map<String,List<BlCcPay>> Map_2 = new HashMap<String,List<BlCcPay>>();
        
        
        for(BlCcPay vo : DepoInfo){			
			if(EnumerateParameter.NINE.equals(vo.getEuPaytype())){
				vo.setEuPaytype("1");
 			}else if(EnumerateParameter.THREE.equals(vo.getEuPaytype())){
				vo.setEuPaytype("3");
			}else if("11".equals(vo.getEuPaytype())){
				vo.setEuPaytype("11");
			}else{
				vo.setEuPaytype("0");
			}
		}
        List<BlCcPay> vos = new ArrayList<BlCcPay>();
        for(Entry<String,List<BlCcPay>> e : Map_1.entrySet()){
        	 if(e.getValue().size()>1){
        		 BlCcPay vo0 = null; 
        		 BlCcPay vo1 = null; 
				 if(e.getValue().get(0).getEuDirect().equals("1")){
					 vo0 = e.getValue().get(0);
					 vo1 = e.getValue().get(1);
				 }else{
					 vo0 = e.getValue().get(1);
					 vo1 = e.getValue().get(0);
				 }
				  
        		 vo0.setAmt(MathUtils.add(vo0.getAmt(), vo1.getAmt()));
        		 vo0.setCntTradeBack(vo1.getCntTrade());
        		 vos.add(vo0);
        	 }else{
        		 vos.add(e.getValue().get(0));
        	 }
        		 
        }
        
        for(Entry<String,List<BlCcPay>> e : Map_2.entrySet()){
       	 if(e.getValue().size()>1){
       		 BlCcPay vo0 = null; 
       		 BlCcPay vo1 = null; 
				 if(e.getValue().get(0).getEuDirect().equals("1")){
					 vo0 = e.getValue().get(0);
					 vo1 = e.getValue().get(1);
				 }else{
					 vo0 = e.getValue().get(1);
					 vo1 = e.getValue().get(0);
				 }
				  
       		 vo0.setAmtBack(vo1.getAmt());;
       		 vo0.setCntTradeBack(vo1.getCntTrade());
       		 vos.add(vo0);
       	 }else{
       		 vos.add(e.getValue().get(0));
       	 }
       }
       /*
    	params.put("dateRept", dateEnd);
		params.put("pkEmpRept",  UserContext.getUser().getPkEmp());        
		List<Map<String,Object>> qryReceiptsData = ipBlCcMapper.qryReceiptsData(params);
		@SuppressWarnings("unused")
		int receiptscnt = 0;
		if(qryReceiptsData!=null && qryReceiptsData.size()>0){
			if(qryReceiptsData.get(0)!=null){
				
				if(Application.isSqlServer()){
					receiptscnt = (Integer)qryReceiptsData.get(0).get("cnt");
				}else{
					BigDecimal cntB = (BigDecimal)qryReceiptsData.get(0).get("CNT");
					receiptscnt = cntB.intValue();
				}
			}
		}*/
        //2.8 实收合计
        Double amtTemp = MathUtils.add(blCc.getAmtSettle(), blCc.getAmtPrep());
        Double amtTemp1 = MathUtils.sub(amtTemp, blCc.getAmtPrepRt());
        
        //查询欠款金额
        Double amtAr = ipBlCcMapper.qryStAmtAr(params);
        
        //查询结算欠款金额
        Double stAr = ipBlCcMapper.qrySettleAmtAr(params);
        blCc.setAmtAr(stAr);
        
        //查询结算补交金额
        Double stArfee = ipBlCcMapper.qrySettleAmtArFee(params);
        blCc.setAmtRepair(stArfee);
        
        //查询预结应收金额
        Double amtSor = ipBlCcMapper.qrySettleAmtSor(params);
        blCc.setAmtSor(amtSor);
        
        //查询预结冲销金额
        Double amtCa = ipBlCcMapper.qrySettleAmtCa(params);
        blCc.setAmtCa(amtCa);
        
        res.setAmtGet(MathUtils.sub(amtTemp1, blCc.getAmtInsu()));
        res.setBlCc(blCc);
        res.setAmtAr(amtAr==null?0.0:amtAr);
        res.setBlCcPayList(DepoInfo);
        double amt = 0.0;
        for(BlCcPay vo : vos){
        	amt = MathUtils.add(amt, vo.getAmt());
        }
        
        res.setCntRetreatFp(invBackCntList.get(0).get("cnt").toString());
        res.setCntScrapFp((Integer.parseInt(invCancelCntList.get(0).get("cnt").toString())-Integer.parseInt(invBackCntList.get(0).get("cnt").toString()))+"");
        res.setCntRetreat(cntRetreatInfo.get(0).get("cntRetreat").toString());
        res.setCntScrap(cntScrapInfo.get(0).get("cntScrap").toString());
        //2.9付款方信息
        List<ZsPayerData> payerList = ipBlCcMapper.qryPayerData(params);
        res.setPayerList(payerList);
	    return res;
	}
	
	/**
	 * 产生日结
	 * @param dateEnd
	 * @return
	 */
	private ZsBlCcDt creatZsba(String dateEnd, String pkEmp) {
		String jdbcType = ApplicationUtils.getPropertyValue("jdbc.dialect", "");	//获取数据库连接字符串
		if(CommonUtils.isEmptyString(dateEnd))return null;
		Map<String,Object> params = new HashMap<String,Object >();
		params.put("dateEnd", dateEnd);
		params.put("pkEmp",  pkEmp);
		params.put("pkOrg",  UserContext.getUser().getPkOrg());
		ZsBlCcDt res = new ZsBlCcDt();
		ZsbaBlCc blCc = new ZsbaBlCc();
		blCc.setPkOrg(UserContext.getUser().getPkOrg());
		blCc.setPkEmpOpera(UserContext.getUser().getPkEmp());
		blCc.setPkOrg(UserContext.getUser().getPkOrg());
		blCc.setNameEmpOpera(UserContext.getUser().getNameEmp());
		blCc.setDateEnd(dateTrans(dateEnd));
		handleDateBegin(blCc);
		
		//BdOuUser user = DataBaseHelper.queryForBean("select * from bd_ou_user where pk_emp = ?", BdOuUser.class, objects)
		
		//2 .产生结账信息
		//2.1结算收款
        ZsbaBlCc voSt = ipBlCcMapper.qryStAmtInfo(params);
        blCc.setAmtInsu(voSt==null?0.0:voSt.getAmtInsu()==null?0.0:voSt.getAmtInsu());//医保加科研
        blCc.setAmtPrepRt(voSt==null?0.0:voSt.getAmtPrepRt()==null?0.0:voSt.getAmtPrepRt());//结算用到的预交金
        blCc.setAmtSettle(voSt==null?0.0:voSt.getAmtSettle()==null?0.0:voSt.getAmtSettle());//结算总金额
	    blCc.setAmtDisc(voSt==null?0.0:voSt.getAmtDisc()==null?0.0:voSt.getAmtDisc());//优惠金额
        ZsbaBlCcDs ccDs = new ZsbaBlCcDs();
        ccDs.setAmtIp(voSt==null?0.0:voSt.getAmtPi()==null?0.0:voSt.getAmtPi());//自费金额
        ccDs.setAmtGz(voSt==null?0.0:voSt.getAmtGz()==null?0.0:voSt.getAmtGz());//个账金额
        
        //收预交金、结算退款、已经出院病人欠费后交款的各支付方式的金额
        ZsbaPayTypeAmount zpta =  createPta(params);
        res.setPta(zpta);
        
        ccDs.setAmtSsXj(zpta.getPreXj()+zpta.getStBackXj()+zpta.getStCollXj());
        
        
        //其他单位负担部分(科研结算费用+计划生育+重点保健对象)
        List<Map<String, Object>> kyList = ipBlCcMapper.qryAmtKy(params);
        if(kyList.get(0)==null){
        	ccDs.setAmtOther(0.00);
        }else{
        	ccDs.setAmtOther(kyList.get(0).get("amtKy")==null?0.00:Double.parseDouble(kyList.get(0).get("amtKy").toString()));
        }
        //因为医保金额和科研金额存在了一个字段，所以amtInsu减去科研费用才是医保费用
        ccDs.setAmtIns(blCc.getAmtInsu()-ccDs.getAmtOther());
        ccDs.setAmtTc(ccDs.getAmtIns()-ccDs.getAmtGz());//医保金额-个账=统筹金额
        //2.2发票信息 
        List<ZsInvInfo> voStInv  = ipBlCcMapper.qryStInvSqlServer(params);
    	   
        res.setInvInfo(voStInv);
        if(voStInv!=null && voStInv.size()>0){
        	StringBuffer invInfo = new StringBuffer(); 
        	BigDecimal cntInv = BigDecimal.ZERO;
        	for(int i = 0;i<voStInv.size();i++){
        		ZsInvInfo vo = voStInv.get(i);
        		invInfo.append(vo.getBegincode());
        		invInfo.append("-");
        		invInfo.append(vo.getEndcode());
        		if(i+1==voStInv.size()){
        			invInfo.append(";");
        		}else{
        			invInfo.append(",");
        		}
        		
        		cntInv = cntInv.add(vo.getCnt());
        	}
        	blCc.setInvInfo(invInfo.toString());
        	blCc.setCntInv(cntInv.intValue());
        }
        
        //作废的发票（不包含退费的）
        List<ZsInvalidStInv> InvVoidList = ipBlCcMapper.qryStInvVoid(params);
        if(InvVoidList!=null && InvVoidList.size()>0){
        	StringBuffer invInfoCanc = new StringBuffer();
        	for(int i = 0;i<InvVoidList.size();i++){
        		ZsInvalidStInv vo = InvVoidList.get(i); 
        		invInfoCanc.append(vo.getCodeInv());
        		if(i+1==InvVoidList.size()){
        			invInfoCanc.append(";");
        		}else{
        			invInfoCanc.append(",");
        		}    		
        	}
        	ccDs.setInvVoidCode(invInfoCanc.toString());
        	ccDs.setInvVoidNum(InvVoidList.size());
        }
        
        //退费的发票
        List<ZsInvalidStInv> InvBackList = ipBlCcMapper.qryStInvBack(params);
        if(InvBackList!=null && InvBackList.size()>0){
        	StringBuffer invInfoCanc = new StringBuffer();
        	for(int i = 0;i<InvBackList.size();i++){
        		ZsInvalidStInv vo = InvBackList.get(i); 
        		invInfoCanc.append(vo.getCodeInv());
        		if(i+1==InvBackList.size()){
        			invInfoCanc.append(";");
        		}else{
        			invInfoCanc.append(",");
        		}    		
        	}
        	ccDs.setInvRefundCode(invInfoCanc.toString());
        	ccDs.setInvRefundNum(InvBackList.size());
        }
        
		//2.4预交金信息
        BlCc voPrePay = ipBlCcMapper.qryPrepPay(params);
        blCc.setAmtPrep(voPrePay==null?0.0:voPrePay.getAmtPrep());
        blCc.setCntDepo(voPrePay==null?0:voPrePay.getCntDepo());
        BigDecimal   amtSs   =   new   BigDecimal(blCc.getAmtSettle()+blCc.getAmtPrep()-blCc.getAmtPrepRt()-blCc.getAmtInsu() - blCc.getAmtDisc());  
        ccDs.setAmtSs(amtSs.setScale(2,   BigDecimal.ROUND_HALF_UP).doubleValue());
		//2.5收款收据号串
        List<ZsDepositInv>  voDepoInv = ipBlCcMapper.qryDepoInvSqlServer(params);  
        if(voDepoInv !=null && voDepoInv.size()>0 && voDepoInv.get(0)!=null){
        	StringBuffer depoInfo = new StringBuffer();
        	for(int i= 0;i<voDepoInv.size();i++){
        		ZsDepositInv vo = voDepoInv.get(i);
        		if(vo!=null){
        			depoInfo.append(vo.getBegincoe());
            		depoInfo.append("-");
            		depoInfo.append(vo.getEndcoe());
        		}
        		if(i+1==voDepoInv.size()){
        			depoInfo.append(";");
        		}else{
        			depoInfo.append(",");
        		}
        	}
        	blCc.setDepoInfo(depoInfo.toString());
        }
        
		//作废收据号码
        List<ZsDepoRtnInfo> voRtnInfoVoid =  ipBlCcMapper.qryDepoInvVoid(params);
        if(voRtnInfoVoid!=null && voRtnInfoVoid.size()>0 ){
        	StringBuffer depoInfoRtn =new StringBuffer();
        	for(int i=0;i<voRtnInfoVoid.size();i++){
        		ZsDepoRtnInfo vo = voRtnInfoVoid.get(i);
        		if(vo==null)
        			continue;
        		depoInfoRtn.append(vo.getReptNo());
        		if(i+1==voRtnInfoVoid.size()){
        			depoInfoRtn.append(";");
        		}else{
        			depoInfoRtn.append(",");
        		}   		
        	}
        	ccDs.setDepoVoidCode(depoInfoRtn.toString());
        	ccDs.setDepoVoidNum(voRtnInfoVoid.size());
        }
        
		//退费收据号码
        List<ZsDepoRtnInfo> voRtnInfoBack =  ipBlCcMapper.qryDepoInvBack(params);
        if(voRtnInfoBack!=null && voRtnInfoBack.size()>0 ){
        	StringBuffer depoInfoRtn =new StringBuffer();
        	for(int i=0;i<voRtnInfoBack.size();i++){
        		ZsDepoRtnInfo vo = voRtnInfoBack.get(i);
        		if(vo==null)
        			continue;
        		depoInfoRtn.append(vo.getReptNo());
        		if(i+1==voRtnInfoBack.size()){
        			depoInfoRtn.append(";");
        		}else{
        			depoInfoRtn.append(",");
        		}   		
        	}
        	ccDs.setDepoRefundCode(depoInfoRtn.toString());
        	ccDs.setDepoRefundNum(voRtnInfoBack.size());
        }
        
        //退票、废票信息
        List<ZsbaBackInvInfo> backInvInfoList = ipBlCcMapper.qryBackInvInfo(params);
        res.setBackInvInfoList(backInvInfoList);
        //退、费预交金票据信息
        List<ZsbaBackDepoInfo> backDepoInfoList = ipBlCcMapper.qryBackDepoInfo(params);
        res.setBackDepoInfoList(backDepoInfoList);
        //内部转账
        List<ZsbaBackDepoInfo> nbzzDepoInfoList = ipBlCcMapper.qryNbzzList(params);
        res.setNbzzDepoInfoList(nbzzDepoInfoList);
        //2.8 实收合计
//        Double amtTemp = MathUtils.add(blCc.getAmtSettle(), blCc.getAmtPrep());
//        Double amtTemp1 = MathUtils.sub(amtTemp, blCc.getAmtPrepRt());
        
        //查询欠款金额
//        Double amtAr = ipBlCcMapper.qryStAmtAr(params);
        
        //查询结算欠款金额
//        Double stAr = ipBlCcMapper.qrySettleAmtAr(params);
//        blCc.setAmtAr(stAr);
        
        //查询结算补交金额
//        Double stArfee = ipBlCcMapper.qrySettleAmtArFee(params);
//        blCc.setAmtRepair(stArfee);
        
        //查询预结应收金额
//        Double amtSor = ipBlCcMapper.qrySettleAmtSor(params);
//        blCc.setAmtSor(amtSor);
        
        //查询预结冲销金额
//        Double amtCa = ipBlCcMapper.qrySettleAmtCa(params);
//        blCc.setAmtCa(amtCa);
        
        //res.setAmtGet(MathUtils.sub(amtTemp1, blCc.getAmtInsu()));
        res.setBlCc(blCc);
        res.setBlCcDs(ccDs);
        //res.setAmtAr(amtAr==null?0.0:amtAr);
	    return res;
	}
	
	private ZsbaPayTypeAmount createPta(Map<String,Object> params){
        ZsbaPayTypeAmount pta = new ZsbaPayTypeAmount();
        pta.setPreHj(0.00);
        pta.setStBackHj(0.00);
        pta.setStCollHj(0.00);
        pta.setMjkHj(0.00);
        pta.setMjkBackHj(0.00);
        
        //收预交款不包含微信支付宝
        List<Map<String, Object>> syjjList = ipBlCcMapper.qryYjkAmt(params);
        boolean flag = false;
        for (Map<String, Object> map : syjjList) {
			if(map.get("dt_paymode").equals("1")){
				pta.setPreXj(Double.parseDouble(map.get("amount")==null?"0.00":map.get("amount").toString()));
				flag = true;
				break;
			}
		}
        if(!flag){
        	pta.setPreXj(0.00);
        }
        
        flag = false;
        for (Map<String, Object> map : syjjList) {
			if(map.get("dt_paymode").equals("3")){
				pta.setPreYh(Double.parseDouble(map.get("amount")==null?"0.00":map.get("amount").toString()));
				flag = true;
				break;
			}
		}
        if(!flag){
        	pta.setPreYh(0.00);
        }
        
        flag = false;
        for (Map<String, Object> map : syjjList) {
			if(map.get("dt_paymode").equals("92")){
				pta.setPreCw(Double.parseDouble(map.get("amount")==null?"0.00":map.get("amount").toString()));
				flag = true;
				break;
			}
		}
        if(!flag){
        	pta.setPreCw(0.00);
        }
        
        //收预交款 衫德的微信支付宝
        List<Map<String, Object>> syjjSandList = ipBlCcMapper.qryYjkAmtSand(params);
        flag = false;
        for (Map<String, Object> map : syjjSandList) {
			if(map.get("dt_paymode").equals("7")){
				pta.setPreWx(Double.parseDouble(map.get("amount")==null?"0.00":map.get("amount").toString()));
				flag = true;
				break;
			}
		}
        if(!flag){
        	pta.setPreWx(0.00);
        }
        
        flag = false;
        for (Map<String, Object> map : syjjSandList) {
			if(map.get("dt_paymode").equals("8")){
				pta.setPreZfb(Double.parseDouble(map.get("amount")==null?"0.00":map.get("amount").toString()));
				flag = true;
				break;
			}
		}
        if(!flag){
        	pta.setPreZfb(0.00);
        }
        
      //收预交款 自助机公众号的微信支付宝
        List<Map<String, Object>> syjjGzhList = ipBlCcMapper.qryYjkAmtGzh(params);
        flag = false;
        for (Map<String, Object> map : syjjGzhList) {
			if(map.get("dt_paymode").equals("7")){
				pta.setPreGzhWx(Double.parseDouble(map.get("amount")==null?"0.00":map.get("amount").toString()));
				flag = true;
				break;
			}
		}
        if(!flag){
        	pta.setPreGzhWx(0.00);
        }
        
        //门禁卡预交金收款
        params.put("euDirect", "1");
        List<Map<String, Object>> mjkList = ipBlCcMapper.qryMjkAmt(params);
        flag = false;
        for (Map<String, Object> map : mjkList) {
			if(map.get("dt_paymode").equals("1")){
				pta.setMjkXj(Double.parseDouble(map.get("amount")==null?"0.00":map.get("amount").toString()));
				flag = true;
				break;
			}
		}
        if(!flag){
        	pta.setMjkXj(0.00);
        }
        
        flag = false;
        for (Map<String, Object> map : mjkList) {
			if(map.get("dt_paymode").equals("3")){
				pta.setMjkYh(Double.parseDouble(map.get("amount")==null?"0.00":map.get("amount").toString()));
				flag = true;
				break;
			}
		}
        if(!flag){
        	pta.setMjkYh(0.00);
        }
        
        flag = false;
        for (Map<String, Object> map : mjkList) {
			if(map.get("dt_paymode").equals("7")){
				pta.setMjkWx(Double.parseDouble(map.get("amount")==null?"0.00":map.get("amount").toString()));
				flag = true;
				break;
			}
		}
        if(!flag){
        	pta.setMjkWx(0.00);
        }
        
        flag = false;
        for (Map<String, Object> map : mjkList) {
			if(map.get("dt_paymode").equals("8")){
				pta.setMjkZfb(Double.parseDouble(map.get("amount")==null?"0.00":map.get("amount").toString()));
				flag = true;
				break;
			}
		}
        if(!flag){
        	pta.setMjkZfb(0.00);
        }
        
        flag = false;
        for (Map<String, Object> map : mjkList) {
			if(map.get("dt_paymode").equals("92")){
				pta.setMjkCw(Double.parseDouble(map.get("amount")==null?"0.00":map.get("amount").toString()));
				flag = true;
				break;
			}
		}
        if(!flag){
        	pta.setMjkCw(0.00);
        }
        
        //门禁卡不会有自助机和公众号的交款和退款，所以无需改变
        pta.setMjkGzhWx(0.00);
        
        //门禁卡预交金退款
        params.put("euDirect", "-1");
        List<Map<String, Object>> mjkBackList = ipBlCcMapper.qryMjkAmt(params);
        flag = false;
        for (Map<String, Object> map : mjkBackList) {
			if(map.get("dt_paymode").equals("1")){
				pta.setMjkBackXj(Double.parseDouble(map.get("amount")==null?"0.00":map.get("amount").toString()));
				flag = true;
				break;
			}
		}
        if(!flag){
        	pta.setMjkBackXj(0.00);
        }
        
        flag = false;
        for (Map<String, Object> map : mjkBackList) {
			if(map.get("dt_paymode").equals("3")){
				pta.setMjkBackYh(Double.parseDouble(map.get("amount")==null?"0.00":map.get("amount").toString()));
				flag = true;
				break;
			}
		}
        if(!flag){
        	pta.setMjkBackYh(0.00);
        }
        
        flag = false;
        for (Map<String, Object> map : mjkBackList) {
			if(map.get("dt_paymode").equals("7")){
				pta.setMjkBackWx(Double.parseDouble(map.get("amount")==null?"0.00":map.get("amount").toString()));
				flag = true;
				break;
			}
		}
        if(!flag){
        	pta.setMjkBackWx(0.00);
        }
        
        flag = false;
        for (Map<String, Object> map : mjkBackList) {
			if(map.get("dt_paymode").equals("8")){
				pta.setMjkBackZfb(Double.parseDouble(map.get("amount")==null?"0.00":map.get("amount").toString()));
				flag = true;
				break;
			}
		}
        if(!flag){
        	pta.setMjkBackZfb(0.00);
        }
        
        flag = false;
        for (Map<String, Object> map : mjkBackList) {
			if(map.get("dt_paymode").equals("92")){
				pta.setMjkBackCw(Double.parseDouble(map.get("amount")==null?"0.00":map.get("amount").toString()));
				flag = true;
				break;
			}
		}
        if(!flag){
        	pta.setMjkBackCw(0.00);
        }
        
      //门禁卡不会有自助机和公众号的交款和退款，所以无需改变
        pta.setMjkBackGzhWx(0.00);
        
        //结算退款 不包含微信支付宝
        List<Map<String, Object>> jstkList = ipBlCcMapper.qryJstkAmt(params);
        flag = false;
        for (Map<String, Object> map : jstkList) {
			if(map.get("dt_paymode").equals("1")){
				pta.setStBackXj(Double.parseDouble(map.get("amount")==null?"0.00":map.get("amount").toString()));
				flag = true;
				break;
			}
		}
        if(!flag){
        	pta.setStBackXj(0.00);
        }
        
        flag = false;
        for (Map<String, Object> map : jstkList) {
			if(map.get("dt_paymode").equals("3")){
				pta.setStBackYh(Double.parseDouble(map.get("amount")==null?"0.00":map.get("amount").toString()));
				flag = true;
				break;
			}
		}
        if(!flag){
        	pta.setStBackYh(0.00);
        }
        

        
        flag = false;
        for (Map<String, Object> map : jstkList) {
			if(map.get("dt_paymode").equals("92")){
				pta.setStBackCw(Double.parseDouble(map.get("amount")==null?"0.00":map.get("amount").toString()));
				flag = true;
				break;
			}
		}
        if(!flag){
        	pta.setStBackCw(0.00);
        }
        
      //结算退款 衫德微信、支付宝
        List<Map<String, Object>> jstkSandList = ipBlCcMapper.qryJstkAmtSand(params);
        flag = false;
        for (Map<String, Object> map : jstkSandList) {
			if(map.get("dt_paymode").equals("7")){
				pta.setStBackWx(Double.parseDouble(map.get("amount")==null?"0.00":map.get("amount").toString()));
				flag = true;
				break;
			}
		}
        if(!flag){
        	pta.setStBackWx(0.00);
        }
        
        flag = false;
        for (Map<String, Object> map : jstkSandList) {
			if(map.get("dt_paymode").equals("8")){
				pta.setStBackZfb(Double.parseDouble(map.get("amount")==null?"0.00":map.get("amount").toString()));
				flag = true;
				break;
			}
		}
        if(!flag){
        	pta.setStBackZfb(0.00);
        }
        
        //结算退款 自助机公众号微信
        List<Map<String, Object>> jstkGzhList = ipBlCcMapper.qryJstkAmtGzh(params);
        flag = false;
        for (Map<String, Object> map : jstkGzhList) {
			if(map.get("dt_paymode").equals("7")){
				pta.setStBackGzhWx(Double.parseDouble(map.get("amount")==null?"0.00":map.get("amount").toString()));
				flag = true;
				break;
			}
		}
        if(!flag){
        	pta.setStBackGzhWx(0.00);
        }
        
        //已经出院病人欠费后交款
        List<Map<String, Object>> qfjkList = ipBlCcMapper.qryJsskAmt(params);
        flag = false;
        for (Map<String, Object> map : qfjkList) {
			if(map.get("dt_paymode").equals("1")){
				pta.setStCollXj(Double.parseDouble(map.get("amount")==null?"0.00":map.get("amount").toString()));
				flag = true;
				break;
			}
		}
        if(!flag){
        	pta.setStCollXj(0.00);
        }
        
        flag = false;
        for (Map<String, Object> map : qfjkList) {
			if(map.get("dt_paymode").equals("3")){
				pta.setStCollYh(Double.parseDouble(map.get("amount")==null?"0.00":map.get("amount").toString()));
				flag = true;
				break;
			}
		}
        if(!flag){
        	pta.setStCollYh(0.00);
        }
        
        
        
        flag = false;
        for (Map<String, Object> map : qfjkList) {
			if(map.get("dt_paymode").equals("92")){
				pta.setStCollCw(Double.parseDouble(map.get("amount")==null?"0.00":map.get("amount").toString()));
				flag = true;
				break;
			}
		}
        if(!flag){
        	pta.setStCollCw(0.00);
        }
        
        //已经出院病人欠费后交款 衫德支付宝微信
        List<Map<String, Object>> qfjkSandList = ipBlCcMapper.qryJsskAmtSand(params);
        flag = false;
        for (Map<String, Object> map : qfjkSandList) {
			if(map.get("dt_paymode").equals("7")){
				pta.setStCollWx(Double.parseDouble(map.get("amount")==null?"0.00":map.get("amount").toString()));
				flag = true;
				break;
			}
		}
        if(!flag){
        	pta.setStCollWx(0.00);
        }
        
        flag = false;
        for (Map<String, Object> map : qfjkSandList) {
			if(map.get("dt_paymode").equals("8")){
				pta.setStCollZfb(Double.parseDouble(map.get("amount")==null?"0.00":map.get("amount").toString()));
				flag = true;
				break;
			}
		}
        if(!flag){
        	pta.setStCollZfb(0.00);
        }
        
        //已经出院病人欠费后交款 自助机公众号微信       住院結算收款不會用到公众号和微信
        pta.setStCollGzhWx(0.00);
        
        //结算退预交金 不包含微信支付宝
        List<Map<String, Object>> tyjjList = ipBlCcMapper.qryJstyjjAmt(params);
        flag = false;
        for (Map<String, Object> map : tyjjList) {
			if(map.get("dt_paymode").equals("1")){
				pta.setJstyjjXj(Double.parseDouble(map.get("amount")==null?"0.00":map.get("amount").toString()));
				flag = true;
				break;
			}
		}
        if(!flag){
        	pta.setJstyjjXj(0.00);
        }
        
        flag = false;
        for (Map<String, Object> map : tyjjList) {
			if(map.get("dt_paymode").equals("3")){
				pta.setJstyjjYh(Double.parseDouble(map.get("amount")==null?"0.00":map.get("amount").toString()));
				flag = true;
				break;
			}
		}
        if(!flag){
        	pta.setJstyjjYh(0.00);
        }
        
        
        
        flag = false;
        for (Map<String, Object> map : tyjjList) {
			if(map.get("dt_paymode").equals("92")){
				pta.setJstyjjCw(Double.parseDouble(map.get("amount")==null?"0.00":map.get("amount").toString()));
				flag = true;
				break;
			}
		}
        if(!flag){
        	pta.setJstyjjCw(0.00);
        }
        
      //结算退预交金 不包含微信支付宝
        List<Map<String, Object>> tyjjSandList = ipBlCcMapper.qryJstyjjAmtSand(params);
        flag = false;
        for (Map<String, Object> map : tyjjSandList) {
			if(map.get("dt_paymode").equals("7")){
				pta.setJstyjjWx(Double.parseDouble(map.get("amount")==null?"0.00":map.get("amount").toString()));
				flag = true;
				break;
			}
		}
        if(!flag){
        	pta.setJstyjjWx(0.00);
        }
        
        flag = false;
        for (Map<String, Object> map : tyjjList) {
			if(map.get("dt_paymode").equals("8")){
				pta.setJstyjjZfb(Double.parseDouble(map.get("amount")==null?"0.00":map.get("amount").toString()));
				flag = true;
				break;
			}
		}
        if(!flag){
        	pta.setJstyjjZfb(0.00);
        }
        
      //结算退预交金 自助机公众号微信
        List<Map<String, Object>> tyjjGzhList = ipBlCcMapper.qryJstyjjAmtGzh(params);
        flag = false;
        for (Map<String, Object> map : tyjjGzhList) {
			if(map.get("dt_paymode").equals("7")){
				pta.setJstyjjGzhWx(Double.parseDouble(map.get("amount")==null?"0.00":map.get("amount").toString()));
				flag = true;
				break;
			}
		}
        if(!flag){
        	pta.setJstyjjGzhWx(0.00);
        }
        
        pta.setPreHj(pta.getPreXj()+pta.getPreYh()+pta.getPreWx()+pta.getPreZfb() + pta.getPreCw() + pta.getPreGzhWx());
        pta.setStBackHj(pta.getStBackXj()+pta.getStBackYh()+pta.getStBackWx()+pta.getStBackZfb() + pta.getStBackCw() + pta.getStBackGzhWx());
        pta.setStCollHj(pta.getStCollXj()+pta.getStCollYh()+pta.getStCollWx()+pta.getStCollZfb() + pta.getStCollCw() + pta.getStCollGzhWx());
        pta.setJstyjjHj(pta.getJstyjjXj()+pta.getJstyjjYh()+pta.getJstyjjWx()+pta.getJstyjjZfb() + pta.getJstyjjCw() + pta.getJstyjjGzhWx());
        pta.setMjkHj(pta.getMjkXj()+pta.getMjkYh()+pta.getMjkWx()+pta.getMjkZfb() + pta.getMjkCw() + pta.getMjkGzhWx());
        pta.setMjkBackHj(pta.getMjkBackXj()+pta.getMjkBackYh()+pta.getMjkBackWx()+pta.getMjkBackZfb() + pta.getMjkBackCw()+ pta.getMjkBackGzhWx());
        
        pta.setXjHj(pta.getPreXj()+pta.getStBackXj()+pta.getStCollXj()+pta.getMjkXj()+pta.getMjkBackXj());
        pta.setYhHj(pta.getPreYh()+pta.getStBackYh()+pta.getStCollYh()+pta.getMjkYh()+pta.getMjkBackYh());
        pta.setWxHj(pta.getPreWx()+pta.getStBackWx()+pta.getStCollWx()+pta.getMjkWx()+pta.getMjkBackWx());
        pta.setZfbHj(pta.getPreZfb()+pta.getStBackZfb()+pta.getStCollZfb()+pta.getMjkZfb()+pta.getMjkBackZfb());
        pta.setGzhWxHj(pta.getPreGzhWx()+pta.getStBackGzhWx()+pta.getStCollGzhWx()+pta.getMjkGzhWx()+pta.getMjkBackGzhWx());
        pta.setCwHj(pta.getPreCw()+pta.getStBackCw()+pta.getStCollCw()+pta.getMjkCw()+pta.getMjkBackCw());
        pta.setHj(pta.getXjHj()+pta.getYhHj()+pta.getWxHj()+pta.getZfbHj()+pta.getCwHj() + pta.getGzhWxHj());
        
        pta.setMjkSs(pta.getMjkHj()+pta.getMjkBackHj());
        return pta;
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
	
	/*******************以下未住院结账汇总功能服务******************/
	/**
	 * 查询住院汇总记录
	 * @param param
	 * @param user
	 */
	@SuppressWarnings("unchecked")
	public List<ZsBlSumRecords> QrySumRecords(String param,IUser user){
		Map<String,Object> params = JsonUtil.readValue(param, Map.class);
    	params.put("pkOrg", UserContext.getUser().getPkOrg());
    	params.put("pkEmp", UserContext.getUser().getPkEmp());
    	if(CommonUtils.isNotNull(params.get("dateBegin"))){
			params.put("dateBegin", CommonUtils.getString(params.get("dateBegin")).substring(0, 8)+"000000");
		}
		if(CommonUtils.isNotNull(params.get("dateEnd"))){
			params.put("dateEnd", CommonUtils.getString(params.get("dateEnd")).substring(0, 8)+"235959");
		}
    	return ipBlCcMapper.QrySumRecords(params);

	}
	
	/**
	 * 根据住院汇总记录查询住院汇总明细
	 * @param param
	 * @param user
	 */
    @SuppressWarnings("unchecked")
	public ZsBlSumVo QrySumDetails(String param,IUser user){
    	Map<String,Object> params = JsonUtil.readValue(param, Map.class);
    	params.put("pkEmp",  UserContext.getUser().getPkEmp());
    	params.put("nameEmp", UserContext.getUser().getNameEmp());
    	params.put("pkOrg", UserContext.getUser().getPkOrg());
    	ZsBlSumVo res = new ZsBlSumVo();   
    	
    	List<ZsBlSumDepoVo> depos =ipBlCcMapper.QrySumDepo(params); 	
    	List<ZsBlSumPayVo> pays =ipBlCcMapper.QrySumPay(params);
    	
    	ZsBlSumRecords vo = ipBlCcMapper.QrySumInfo(params);
    	if(vo!=null){
    		vo.setDateEnd(dateTrans((String)params.get("dateLeader")));
    	}
    	
    	res.setSumInfo(vo);
    	res.setDepos(depos);
    	res.setPayos(MakeClear(pays));
    	
    	//付款方信息
    	List<ZsPayerData> payerList =ipBlCcMapper.QrySumPayerData(params);
    	res.setPayerList(payerList);
    	//汇总人和日期
    	List<Map<String,Object>> empList =ipBlCcMapper.QrySumEmp(params);
    	res.setPkEmp(empList.get(0).get("pk_emp_leader").toString());
    	res.setNameEmp(empList.get(0).get("name_emp_leader").toString());
    	Date date = dateTrans((String)params.get("dateLeader"));
    	String dateStr = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(date);
    	res.setDateLeader(dateStr);
    	return res;
	}
    
	/**
	 * 根据住院汇总记录查询住院汇总明细（中山博爱）
	 * @param param
	 * @param user
	 */
    @SuppressWarnings("unchecked")
	public ZsBlSumVo QrySumDetailsZsba(String param,IUser user){
    	Map<String,Object> params = JsonUtil.readValue(param, Map.class);
    	params.put("pkEmp",  UserContext.getUser().getPkEmp());
    	params.put("nameEmp", UserContext.getUser().getNameEmp());
    	params.put("pkOrg", UserContext.getUser().getPkOrg());
    	ZsBlSumVo res = new ZsBlSumVo();   

    	ZsBlSumRecords vo = ipBlCcMapper.QrySumInfo(params);
    	if(vo!=null){
    		vo.setDateEnd(dateTrans((String)params.get("dateLeader")));
    	}
    	
    	res.setSumInfo(vo);
    	
    	List<ZsBaBlSumVo>  blSumVoList = ipBlCcMapper.qryNotSummarizedInfo(params);
    	res.setBlSumVoList(blSumVoList);
    	
    	//汇总人和日期
    	List<Map<String,Object>> empList =ipBlCcMapper.QrySumEmp(params);
    	res.setPkEmp(empList.get(0).get("pk_emp_leader").toString());
    	res.setNameEmp(empList.get(0).get("name_emp_leader").toString());
    	Date date = dateTrans((String)params.get("dateLeader"));
    	String dateStr = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(date);
    	res.setDateLeader(dateStr);
    	return res;
	}
    
    
    /**
	 * 未结账住院汇总明细
	 * @param param
	 * @param user
	 */
    @SuppressWarnings("unchecked")
	public ZsBlSumVo CreateSumCc(String param,IUser user){
    	Map<String,Object> params = JsonUtil.readValue(param, Map.class);
    	params.put("pkOrg", UserContext.getUser().getPkOrg());
    	ZsBlSumVo res = new ZsBlSumVo();   
    	
    	List<ZsBlSumDepoVo> depos =ipBlCcMapper.QryUnSumDepo(params); 	
    	List<ZsBlSumPayVo> pays =ipBlCcMapper.QryUnSumPay(params);
    	
    	ZsBlSumRecords vo = ipBlCcMapper.QryUnSumInfo(params);
    	if(vo !=null){
    		Date DateEnd = dateTrans((String)params.get("dateEnd")); 
    		vo.setDateEnd(DateEnd);
    	}

    	res.setSumInfo(vo);
    	res.setDepos(depos);
    	res.setPayos(MakeClear(pays));
    	
    	//付款方信息
    	List<ZsPayerData> payerList = ipBlCcMapper.QryUnSumPayerData(params);
    	res.setPayerList(payerList);
    	res.setPkEmp(UserContext.getUser().getPkEmp());
    	res.setNameEmp(UserContext.getUser().getNameEmp());
    	Date date = dateTrans((String)params.get("dateEnd"));
    	String dateStr = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(date);
    	res.setDateLeader(dateStr);
    	
    	return res;
	}

    /**
	 * 未结账住院汇总明细中山博爱
	 * @param param
	 * @param user
	 */
    @SuppressWarnings("unchecked")
	public ZsBlSumVo CreateSumCcZsba(String param,IUser user){
    	Map<String,Object> params = JsonUtil.readValue(param, Map.class);
    	params.put("pkOrg", UserContext.getUser().getPkOrg());
    	ZsBlSumVo res = new ZsBlSumVo();   
    	
    	ZsBlSumRecords vo = new ZsBlSumRecords();
    	Date DateBegin = dateTrans((String)params.get("dateBegin")); 
		Date DateEnd = dateTrans((String)params.get("dateEnd")); 
    	vo.setDateBegin(DateBegin);
		vo.setDateEnd(DateEnd);
    	res.setSumInfo(vo);
    	List<ZsBaBlSumVo>  blSumVoList = ipBlCcMapper.qryNotSummarizedInfo(params);
    	res.setBlSumVoList(blSumVoList);
    	
    	return res;
	}

    
    private List<ZsBlSumPayVo> MakeClear(List<ZsBlSumPayVo> pays) {
		List<ZsBlSumPayVo> res = new ArrayList<ZsBlSumPayVo>();
		Set<String> payways = new HashSet<String>();
		Map<String,ZsBlSumPayVo> prep = new HashMap<String,ZsBlSumPayVo>();
		Map<String,ZsBlSumPayVo> st = new HashMap<String,ZsBlSumPayVo>();
		Map<String,ZsBlSumPayVo> stAr = new HashMap<String,ZsBlSumPayVo>();
		for(ZsBlSumPayVo vo : pays){
			if("1".equals(vo.getEuPaytype())){
				prep.put(vo.getDtPaymode(), vo);
			}
			if("0".equals(vo.getEuPaytype())){
				st.put(vo.getDtPaymode(), vo);
			}
			if("3".equals(vo.getEuPaytype())){
				stAr.put(vo.getDtPaymode(), vo);
			}
		}
		for(Entry<String,ZsBlSumPayVo> e: st.entrySet()){
			ZsBlSumPayVo prepAmt = prep.get(e.getKey());
			if(prepAmt==null)continue;
			payways.add(e.getKey());
			ZsBlSumPayVo temp = e.getValue();
			temp.setAmtPrep(prepAmt.getAmt());
			st.put(e.getKey(), temp);
		}
		for(Entry<String,ZsBlSumPayVo> e : prep.entrySet()){
			if(!payways.contains(e.getKey())){
				ZsBlSumPayVo temp = e.getValue();
				temp.setAmtPrep(temp.getAmt());
				temp.setAmt(BigDecimal.ZERO);
				res.add(e.getValue());
			}
		}
		res.addAll(st.values());
		for(Entry<String,ZsBlSumPayVo> e : stAr.entrySet()){
			boolean flag = false;
			ZsBlSumPayVo temp = e.getValue();
			for(ZsBlSumPayVo payVo : res){
				if(payVo.getDtPaymode().equals(e.getKey())){
					flag = true;
					payVo.setAmtRepair(temp.getAmt());
					break;
				}
			}
			if(!flag){
				temp.setAmtRepair(temp.getAmt());
				temp.setAmt(BigDecimal.ZERO);
				res.add(temp);
			}
		}
		return  res;
	}

	/**
     * 住院汇总结账
     * @param param
     * @param user
     */
    @SuppressWarnings("unchecked")
	public void SummaryClose(String param,IUser user){
    	Map<String,Object> params = JsonUtil.readValue(param, Map.class);
    	String dateLeader = (String)params.get("dateLeader");//汇总截止日期
    	params.put("pkEmp",  UserContext.getUser().getPkEmp());
    	params.put("nameEmp", UserContext.getUser().getNameEmp());
    	params.put("pkOrg", UserContext.getUser().getPkOrg());
    	SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    	String time = df.format(dateTrans(dateLeader));
    	Timestamp ts = Timestamp.valueOf(time);
    	params.put("dateCc", ts);
    	if(Application.isSqlServer()){
    		String sql = "update bl_cc  set flag_leader = 1, pk_emp_leader = ?,"
    	    		+ " name_emp_leader = ?, date_leader =  ?,eu_status = 1"
    	    		+ " where pk_org = ? and  eu_cctype = 8 and flag_leader = 0 and date_end <= ?";
    		DataBaseHelper.execute(sql, UserContext.getUser().getPkEmp(),UserContext.getUser().getNameEmp(),
    				ts,UserContext.getUser().getPkOrg(),dateTrans(dateLeader));
    		return;
    	}
    	ipBlCcMapper.updSumCc(params);
    }
    
    /**
     * 取消住院汇总结账
     * @param param
     * @param user
     */
    @SuppressWarnings("unchecked")
	public void CancSumClose(String param,IUser user){
    	Map<String,Object> params = JsonUtil.readValue(param, Map.class);
    	params.put("pkOrg", UserContext.getUser().getPkOrg());
    	params.put("pkEmp", UserContext.getUser().getPkEmp());
    	String dateLeader = (String)params.get("dateLeader");//汇总截止日期
    	List<Map<String,Object>> cancelData = ipBlCcMapper.cancelSumData(params);
		if(cancelData!=null && cancelData.size()>0){
			if(cancelData.get(0)!=null){
				
				int cnt = 0;
				if(Application.isSqlServer()){
					cnt = (Integer)cancelData.get(0).get("cnt");
				}else{
					BigDecimal cntB = (BigDecimal)cancelData.get(0).get("CNT");
					cnt = cntB.intValue();
				}
				if(cnt>1){
					throw new BusException("只能取消最大日期的结账!");
				}
			}
		}
		if(Application.isSqlServer()){
			String sql = "update bl_cc  set flag_leader = 0,pk_emp_leader = null,name_emp_leader = null, date_leader = null,eu_status = 0"
					+ " where pk_org = ? and pk_emp_leader = ? and date_leader = ? and eu_cctype = 8 and   flag_leader = 1";
			DataBaseHelper.execute(sql, UserContext.getUser().getPkOrg(),UserContext.getUser().getPkEmp(),dateTrans(dateLeader))	;	
    	return;
		}
		ipBlCcMapper.updCancelSumCc(params);
    	
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
   
   @SuppressWarnings("unchecked")
   public ZsGroupDayStSum GroupDayStSum(String param,IUser user){
	   Map<String,Object> params = JsonUtil.readValue(param, Map.class);
	   String pkDept = params.get("pkDept").toString();
	   String beginDate = params.get("beginDate").toString()+"000000";
	   String endDate = params.get("endDate").toString()+"235959";
	   StringBuffer sb = new StringBuffer();
	   sb.append("select a.name_emp_opera, a.date_end, a.amt_settle, a.amt_prep,  case when b.amt_ss is null then '0.00' else b.amt_ss end as amt_ss from ( ");
	   sb.append("select cc.name_emp_opera, cc.date_end, sum(amt_settle) as amt_settle, sum(amt_prep) as amt_prep, cc.pk_cc ");
	   sb.append(" from bl_cc cc inner join BD_OU_EMPLOYEE emp on cc.pk_emp_opera = emp.PK_EMP");
	   sb.append(" left join bd_ou_empjob empjob on emp.pk_emp = empjob.pk_emp");
	   sb.append(" left join bd_ou_dept dept on dept.pk_dept = empjob.pk_dept");
	   sb.append(" where emp.flag_active = '1'");
	   sb.append(" and cc.eu_cctype = '8'");
	   sb.append(" and emp.del_flag = '0'");
	   sb.append(" and empjob.pk_dept = ?");
	   sb.append(" and date_end >= to_date(?,'yyyymmddhh24miss')");
	   sb.append(" and date_end <= to_date(?,'yyyymmddhh24miss')");
	   sb.append(" group by cc.name_emp_opera, cc.date_end, cc.pk_cc ");
	   sb.append(" ) a left join ( ");
	   sb.append(" select cc.pk_cc, case when sum(depo.AMOUNT) is null then '0.00' else sum(depo.AMOUNT) end as amt_ss ");
	   sb.append(" from bl_cc cc inner join BL_DEPOSIT depo on depo.pk_cc = cc.pk_cc and depo.DT_PAYMODE = '1'");
	   sb.append(" AND cc.eu_cctype = '8'");
	   sb.append(" and date_end >= to_date(?,'yyyymmddhh24miss')");
	   sb.append(" and date_end <= to_date(?,'yyyymmddhh24miss')");
	   sb.append(" group by cc.name_emp_opera, cc.date_end, cc.pk_cc ");
	   sb.append(" ) b on a.pk_cc = b.pk_cc  ");
	   sb.append(" order by a.date_end ASC ");
	   List<ZsGroupDaySt> list = DataBaseHelper.queryForList(sb.toString(), ZsGroupDaySt.class, pkDept, beginDate, endDate, beginDate, endDate);
	   ZsGroupDayStSum zsGroupDayStSum = new ZsGroupDayStSum();
	   zsGroupDayStSum.setZsGroupDayStList(list);
	   return zsGroupDayStSum;
   }
}
