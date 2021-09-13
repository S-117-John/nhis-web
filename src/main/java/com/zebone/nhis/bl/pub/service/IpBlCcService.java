package com.zebone.nhis.bl.pub.service;

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

import com.zebone.nhis.bl.pub.dao.BlIpCcMapper;
import com.zebone.nhis.bl.pub.support.CgProcessUtils;
import com.zebone.nhis.bl.pub.vo.AmtInsuVo;
import com.zebone.nhis.bl.pub.vo.BlCcDetails;
import com.zebone.nhis.bl.pub.vo.BlCcDt;
import com.zebone.nhis.bl.pub.vo.BlSumDepoVo;
import com.zebone.nhis.bl.pub.vo.BlSumPayVo;
import com.zebone.nhis.bl.pub.vo.BlSumRecords;
import com.zebone.nhis.bl.pub.vo.BlSumVo;
import com.zebone.nhis.bl.pub.vo.DepoRtnInfo;
import com.zebone.nhis.bl.pub.vo.DepositInv;
import com.zebone.nhis.bl.pub.vo.InvInfo;
import com.zebone.nhis.bl.pub.vo.InvalidStInv;
import com.zebone.nhis.bl.pub.vo.QryParam;
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
public class IpBlCcService {
	
	@Autowired
	private BlIpCcMapper ipBlCcMapper;
	
	/**
	 * 查询已结账记录的明细
	 * @param param pkCc 结账主键
	 * @param user 
	 */
	@SuppressWarnings("unchecked")
	public BlCcDetails QryClosedDetails(String param,IUser user){
		Map<String,Object> params = JsonUtil.readValue(param, Map.class);
		BlCcDetails res = new BlCcDetails();
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
	public BlCcDetails QryUncloseDetails(String param,IUser user){
		Map<String,Object> params = JsonUtil.readValue(param, Map.class);
		BlCcDetails res = new BlCcDetails();
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
		ipBlCcMapper.updBlSt(pkCc);
		ipBlCcMapper.updBlDepo(pkCc);
		ipBlCcMapper.updBlInvoice(pkCc);
		ipBlCcMapper.updBlInvCanc(pkCc);
		ipBlCcMapper.updBlStArCanc(pkCc);
		ipBlCcMapper.updBlDepoReplenish(pkCc);
		//电子票据更新
		ipBlCcMapper.updBlInvEbill(pkCc);
		ipBlCcMapper.updBlInvEbillCanl(pkCc);
	}
	
	
	/**
	 * 查询已结账信息
	 * @param param pkCC
	 * @param user
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public BlCcDt QryBlCcInfo(String param, IUser user) {	
		Map<String,Object> params = JsonUtil.readValue(param, Map.class);
		String pkCc = (String)params.get("pkCc");
		BlCcDt res = new BlCcDt();
		BlCc blcc = ipBlCcMapper.qryBlCcByPk(pkCc);
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
		
		/**日结账个性化信息查询，每个项目如果有特定的参数要查询，要在这个分支下写*/
  		Map<String,Object> rtn = CgProcessUtils.processIpOperatorAccount(res);
  		if(rtn!=null&&"true".equals(rtn.get("enable"))&&rtn.get("result")!=null){
			return (BlCcDt)rtn.get("result");
		}
		
		return res;
	}
	
	
	/**
	 * 产生结账信息
	 * @param param dateEnd结账截止时间
	 * @param user
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public BlCcDt CreateCcInfo(String param, IUser user){
		
		//1.参数获取
		Map<String,Object> params = JsonUtil.readValue(param, Map.class);
		String dateEnd  = (String)params.get("dateEnd");
		BlCcDt res = new BlCcDt();
		if(CommonUtils.isEmptyString(dateEnd))return res;
        return creat(dateEnd);
	}

	/**
	 * 结账
	 * @param param dateEnd结账截止时间
	 * @param user
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public BlCcDt CloseCounter(String param, IUser user){
		//1.参数获取
		Map<String,Object> params = JsonUtil.readValue(param, Map.class);
		String dateEnd  = (String)params.get("dateEnd");
		BlCcDt res = creat(dateEnd);
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
			BlCc blCc = res.getBlCc();
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
		
		String sqlDepo = " update bl_deposit set flag_cc = 1, pk_cc = ? where flag_cc = 0 and eu_pvtype = 3 and pk_org = ? and pk_emp_pay = ? and date_pay < ? ";
		DataBaseHelper.execute(sqlDepo.toString(), args);

		//ipBlCcMapper.updBlSettle(map);
		//ipBlCcMapper.updBlDeposit(map);
		StringBuffer sqlUpd = new StringBuffer(" update bl_invoice  set flag_cc = 1,pk_cc = ?");
		sqlUpd.append(" where exists (select 1  from bl_settle st  inner join bl_st_inv si on st.pk_settle=si.pk_settle");
		sqlUpd.append(" where bl_invoice.pk_invoice=si.pk_invoice and  st.pk_org = ? and st.eu_pvtype = 3) and ");
		sqlUpd.append(" pk_emp_inv = ? and flag_cc = 0 and  date_inv <= ?  ");       
		DataBaseHelper.execute(sqlUpd.toString(), args);
		StringBuffer sqlCancUpd = new StringBuffer(" update bl_invoice  set flag_cc_cancel = 1, pk_cc_cancel = ?");
		sqlCancUpd.append("  where ");
		sqlCancUpd.append("  pk_emp_cancel = ? and  flag_cc_cancel = 0 and date_cancel <= ?");
		DataBaseHelper.execute(sqlCancUpd.toString(), new Object[]{pkCc,pkEmp,date});
		//修改BL_SETTLE_AR
		DataBaseHelper.execute(
				"update bl_settle_ar set flag_cc='1',pk_cc=? where pk_org=? and pk_emp_pay=? and flag_cc='0' and date_pay<=?",
				args);
		
		//更新电子票据信息
		StringBuffer sqlEbillUpd = new StringBuffer(" update bl_invoice  set flag_cc_ebill = 1,pk_cc_ebill = ?");
		sqlEbillUpd.append(" where exists (select 1  from bl_settle st  inner join bl_st_inv si on st.pk_settle=si.pk_settle");
		sqlEbillUpd.append(" where bl_invoice.pk_invoice=si.pk_invoice and  st.pk_org = ? and st.eu_pvtype = 3) and ");
		sqlEbillUpd.append(" pk_emp_ebill = ? and FLAG_CC_EBILL = 0 and  date_ebill <= ?  ");       
		DataBaseHelper.execute(sqlEbillUpd.toString(), args);
		
		StringBuffer sqlEbillCancUpd = new StringBuffer(" update bl_invoice  set flag_cc_cancel_ebill = 1, pk_cc_cancel_ebill = ?");
		sqlEbillCancUpd.append("  where exists (select 1 from bl_settle st inner join bl_st_inv si on st.pk_settle=si.pk_settle");
		sqlEbillCancUpd.append("  where bl_invoice.pk_invoice=si.pk_invoice and st.pk_org = ? and st.eu_pvtype = 3) and");
		sqlEbillCancUpd.append("  pk_emp_cancel_ebill = ? and  flag_cc_cancel_ebill = 0 and date_ebill_cancel <= ?");
		DataBaseHelper.execute(sqlEbillCancUpd.toString(), args);
		
//		ipBlCcMapper.updInv(map);
//		ipBlCcMapper.updInvCanc(map);
	}


	private List<BlCcInv> handleInvCc(BlCcDt data,String pkCc) {
		List<BlCcInv> res = new ArrayList<BlCcInv>();
		String pkOrg = UserContext.getUser().getPkOrg();
		if(data.getInvalidList()!=null){
			for(InvalidStInv item :data.getInvalidList()){
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
			for(InvInfo item :data.getInvInfo()){
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


	private void handleDateBegin(BlCc cc) {
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
	public List<AmtInsuVo> qryAmtInsu(String param,IUser user){
		QryParam qryParam = JsonUtil.readValue(param, QryParam.class);
		Map<String,Object> paramMap = new HashMap<>();
		
		if(CommonUtils.isEmptyString(qryParam.getPkCc())){
			paramMap.put("dateEnd", qryParam.getDateEnd());
			paramMap.put("pkEmp",  UserContext.getUser().getPkEmp());
			paramMap.put("pkOrg",  UserContext.getUser().getPkOrg());
		}else{
			paramMap.put("pkCc", qryParam.getPkCc());
		}
		
		List<AmtInsuVo> amtList = ipBlCcMapper.qryAmtInsu(paramMap);
		return amtList;
	}

	private BlCcDt creat(String dateEnd) {
		String jdbcType = ApplicationUtils.getPropertyValue("jdbc.dialect", "");	//获取数据库连接字符串
		if(CommonUtils.isEmptyString(dateEnd))return null;
		Map<String,Object> params = new HashMap<String,Object >();
		params.put("dateEnd", dateEnd);
		params.put("pkEmp",  UserContext.getUser().getPkEmp());
		params.put("pkOrg",  UserContext.getUser().getPkOrg());
		BlCcDt res = new BlCcDt();
		BlCc blCc = new BlCc();
		blCc.setPkOrg(UserContext.getUser().getPkOrg());
		blCc.setPkEmpOpera(UserContext.getUser().getPkEmp());
		blCc.setPkOrg(UserContext.getUser().getPkOrg());
		blCc.setNameEmpOpera(UserContext.getUser().getNameEmp());
		blCc.setDateEnd(dateTrans(dateEnd));
		handleDateBegin(blCc);
		//2 .产生结账信息
		//2.1结算收款
        BlCc voSt = ipBlCcMapper.qryStAmtInfo(params);
        blCc.setAmtInsu(voSt==null?0.0:voSt.getAmtInsu());
        blCc.setAmtPrepRt(voSt==null?0.0:voSt.getAmtPrepRt());
        blCc.setAmtSettle(voSt==null?0.0:voSt.getAmtSettle());;
	       
        List<InvInfo> voStInv = new ArrayList<>();
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
        		InvInfo vo = voStInv.get(i);
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
        List<InvalidStInv> InvalidList = ipBlCcMapper.qryStInv_Invalid(params);
        res.setInvalidList(InvalidList);
        if(InvalidList!=null && InvalidList.size()>0){
        	StringBuffer invInfoCanc = new StringBuffer();
        	for(int i = 0;i<InvalidList.size();i++){
        		InvalidStInv vo = InvalidList.get(i); 
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
        List<DepositInv> voDepoInv = new ArrayList<>();
        if("com.zebone.platform.modules.dao.dialect.spi.OracleDialect".equals(jdbcType))//Oracle
        	voDepoInv = ipBlCcMapper.qryDepoInvOracle(params);  
        else	//SqlServer
        	voDepoInv = ipBlCcMapper.qryDepoInvSqlServer(params);  

        if(voDepoInv !=null && voDepoInv.size()>0 && voDepoInv.get(0)!=null){
        	StringBuffer depoInfo = new StringBuffer();
        	for(int i= 0;i<voDepoInv.size();i++){
        		DepositInv vo = voDepoInv.get(i);
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
        List<DepoRtnInfo> voRtnInfo =  ipBlCcMapper.qryDepoInv_Rtn(params);
      
        if(voRtnInfo!=null && voRtnInfo.size()>0 ){
        	StringBuffer depoInfoRtn =new StringBuffer();
        	for(int i=0;i<voRtnInfo.size();i++){
        		DepoRtnInfo vo = voRtnInfo.get(i);
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
        
        /**日结账个性化信息查询，每个项目如果有特定的参数要查询，要在这个分支下写*/
  		Map<String,Object> rtn = CgProcessUtils.processIpOperatorAccount(res);
  		if(rtn!=null&&"true".equals(rtn.get("enable"))&&rtn.get("result")!=null){
			return (BlCcDt)rtn.get("result");
		}
        
	    return res;
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
	public List<BlSumRecords> QrySumRecords(String param,IUser user){
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
	public BlSumVo QrySumDetails(String param,IUser user){
    	Map<String,Object> params = JsonUtil.readValue(param, Map.class);
    	params.put("pkEmp",  UserContext.getUser().getPkEmp());
    	params.put("nameEmp", UserContext.getUser().getNameEmp());
    	params.put("pkOrg", UserContext.getUser().getPkOrg());
    	BlSumVo res = new BlSumVo();   
    	
    	List<BlSumDepoVo> depos =ipBlCcMapper.QrySumDepo(params); 	
    	List<BlSumPayVo> pays =ipBlCcMapper.QrySumPay(params);
    	
    	BlSumRecords vo = ipBlCcMapper.QrySumInfo(params);
    	if(vo!=null){
    		vo.setDateEnd(dateTrans((String)params.get("dateLeader")));
    	}
    	
    	res.setSumInfo(vo);
    	res.setDepos(depos);
    	res.setPayos(MakeClear(pays));
    	return res;
	}
    
    /**
	 * 未结账住院汇总明细
	 * @param param
	 * @param user
	 */
    @SuppressWarnings("unchecked")
	public BlSumVo CreateSumCc(String param,IUser user){
    	Map<String,Object> params = JsonUtil.readValue(param, Map.class);
    	params.put("pkOrg", UserContext.getUser().getPkOrg());
    	BlSumVo res = new BlSumVo();   
    	
    	List<BlSumDepoVo> depos =ipBlCcMapper.QryUnSumDepo(params); 	
    	List<BlSumPayVo> pays =ipBlCcMapper.QryUnSumPay(params);
    	
    	BlSumRecords vo = ipBlCcMapper.QryUnSumInfo(params);
    	if(vo !=null){
    		Date DateEnd = dateTrans((String)params.get("dateEnd")); 
    		vo.setDateEnd(DateEnd);
    	}

    	res.setSumInfo(vo);
    	res.setDepos(depos);
    	res.setPayos(MakeClear(pays));
    	return res;
	}
    
    
    
    
    
    private List<BlSumPayVo> MakeClear(List<BlSumPayVo> pays) {
		List<BlSumPayVo> res = new ArrayList<BlSumPayVo>();
		Set<String> payways = new HashSet<String>();
		Map<String,BlSumPayVo> prep = new HashMap<String,BlSumPayVo>();
		Map<String,BlSumPayVo> st = new HashMap<String,BlSumPayVo>();
		Map<String,BlSumPayVo> stAr = new HashMap<String,BlSumPayVo>();
		for(BlSumPayVo vo : pays){
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
		for(Entry<String,BlSumPayVo> e: st.entrySet()){
			BlSumPayVo prepAmt = prep.get(e.getKey());
			if(prepAmt==null)continue;
			payways.add(e.getKey());
			BlSumPayVo temp = e.getValue();
			temp.setAmtPrep(prepAmt.getAmt());
			st.put(e.getKey(), temp);
		}
		for(Entry<String,BlSumPayVo> e : prep.entrySet()){
			if(!payways.contains(e.getKey())){
				BlSumPayVo temp = e.getValue();
				temp.setAmtPrep(temp.getAmt());
				temp.setAmt(BigDecimal.ZERO);
				res.add(e.getValue());
			}
		}
		res.addAll(st.values());
		for(Entry<String,BlSumPayVo> e : stAr.entrySet()){
			boolean flag = false;
			BlSumPayVo temp = e.getValue();
			for(BlSumPayVo payVo : res){
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
}
