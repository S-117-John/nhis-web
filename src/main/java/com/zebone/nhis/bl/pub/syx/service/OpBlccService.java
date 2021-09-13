package com.zebone.nhis.bl.pub.syx.service;

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

import org.apache.shiro.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zebone.nhis.bl.pub.syx.dao.OpBlccMapper;
import com.zebone.nhis.bl.pub.syx.vo.AccAmtVo;
import com.zebone.nhis.bl.pub.syx.vo.BlCcDt;
import com.zebone.nhis.bl.pub.syx.vo.CcDts;
import com.zebone.nhis.bl.pub.syx.vo.InvalidStInv;
import com.zebone.nhis.bl.pub.syx.vo.StInvInfo;
import com.zebone.nhis.common.module.bl.BlCc;
import com.zebone.nhis.common.module.bl.BlCcInv;
import com.zebone.nhis.common.module.bl.BlCcPay;
import com.zebone.nhis.common.module.bl.BlSettle;
import com.zebone.nhis.common.support.ApplicationUtils;
import com.zebone.nhis.common.support.CommonUtils;
import com.zebone.platform.Application;
import com.zebone.platform.common.support.NHISUUID;
import com.zebone.platform.common.support.UserContext;
import com.zebone.platform.framework.support.IUser;
import com.zebone.platform.modules.dao.DataBaseHelper;
import com.zebone.platform.modules.exception.BusException;
import com.zebone.platform.modules.utils.JsonUtil;

@Service
public class OpBlccService {
	
	@Autowired
	private OpBlccMapper opBlccMapper;
	
	/**
	 * 交易码：007002005001
	 * 查询未结账信息
	 * @param param
	 * @param user
	 */
	public BlCcDt qryUnSummaryInfo(String param,IUser user){
		//1.参数获取
		Map<String,Object> params = JsonUtil.readValue(param, Map.class);
		String dateEnd  = (String)params.get("dateEnd");
		if(CommonUtils.isEmptyString(dateEnd))
			throw new BusException("请选择结账截止日期！");
		
		return qryUnJzInfo(dateEnd);
	}
	
	/**
	 * 交易码：007002005002
	 * 保存结账信息
	 * @param param
	 * @param user
	 * @return
	 */
	public BlCcDt saveSummaryInfo(String param,IUser user){
		//1.参数获取
		Map<String,Object> params = JsonUtil.readValue(param, Map.class);
		String dateEnd  = (String)params.get("dateEnd");
		if(CommonUtils.isEmptyString(dateEnd))
			throw new BusException("请选择结账截止日期！");
		
		BlCcDt res = qryUnJzInfo(dateEnd);
		if(res!=null){
			//写表Bl_CC
			BlCc blCc = res.getBlCc();
			blCc.setPkCc(NHISUUID.getKeyId());
			ApplicationUtils.setDefaultValue(blCc, true);
			DataBaseHelper.insertBean(blCc);
			//写表bl_cc_pay；
			List<BlCcPay> payList = new ArrayList<>(res.getBlCcPayList());
			if(payList!=null && payList.size()>0){ //过滤DtPaymode为空  或者amt和amtback都为0的数据
				for(int i =payList.size() - 1; i >= 0; i--){
					if(CommonUtils.isEmptyString(payList.get(i).getDtPaymode()) || 
							(Double.compare(payList.get(i).getAmt(), 0)==0 && Double.compare(payList.get(i).getAmtBack(), 0)==0)){
						payList.remove(i);
					}else{
						payList.get(i).setPkCc(blCc.getPkCc());
						payList.get(i).setPkCcpay(NHISUUID.getKeyId());
						payList.get(i).setPkOrg(UserContext.getUser().getPkOrg());
						payList.get(i).setEuPaytype("0");
						ApplicationUtils.setDefaultValue(payList.get(i), true);
					}
				}
				DataBaseHelper.batchUpdate(DataBaseHelper.getInsertSql(BlCcPay.class), payList);
			}
			//写表bl_cc_inv
			List<BlCcInv> invList = new ArrayList<>();
			if(res.getInvalidList()!=null){
				for(InvalidStInv item :res.getInvalidList()){
					BlCcInv  vo = new BlCcInv();
					vo.setBeginNo(item.getCodeInv());
					vo.setEndNo(null);
					vo.setPkInvcate(item.getPkInvcate());
					vo.setFlagCanc("1");
					vo.setFlagWg("0");
					vo.setPkCc(blCc.getPkCc());
					vo.setPkOrg(UserContext.getUser().getPkOrg());
					vo.setPkCcinv(NHISUUID.getKeyId());
					ApplicationUtils.setDefaultValue(vo, true);
					invList.add(vo);
				}
			}
			if(res.getInvInfoList()!=null){
				for(StInvInfo item :res.getInvInfoList()){
					BlCcInv  vo = new BlCcInv();
					vo.setBeginNo(item.getBegincode());
					vo.setEndNo(item.getEndcode());
					vo.setPkInvcate(item.getPkInvcate());
					vo.setFlagCanc("0");
					vo.setFlagWg("0");
					vo.setPkCc(blCc.getPkCc());
					vo.setPkOrg(UserContext.getUser().getPkOrg());
					vo.setPkCcinv(NHISUUID.getKeyId());
					ApplicationUtils.setDefaultValue(vo, true);
					invList.add(vo);
				}		
			}
			
			if(invList!=null && invList.size()>0){
				DataBaseHelper.batchUpdate(DataBaseHelper.getInsertSql(BlCcInv.class), invList);
			}
			
			//更新业务表结账标志
			Map<String,Object> paramMap = new HashMap<>();
			paramMap.put("pkCc", blCc.getPkCc());
			paramMap.put("pkOrg", blCc.getPkOrg());
			paramMap.put("pkEmp",blCc.getPkEmpOpera());
			paramMap.put("dateEnd",dateEnd);
			paramMap.put("flagPv", "0");
			Set<String> pkList = opBlccMapper.qryUpPkSt(paramMap);
			if(pkList!=null && pkList.size()>0){
				//更新bl_settle表
				String sqlSt = "update bl_settle   set flag_cc = 1,pk_cc =  ? where  flag_cc = 0 and pk_settle in ("+CommonUtils.convertSetToSqlInPart(pkList, "pk_settle")+")";
				DataBaseHelper.execute(sqlSt.toString(), new Object[]{blCc.getPkCc()});
				//更新bl_deposit表
				String sqlDepo = " update bl_deposit set flag_cc = 1, pk_cc = ? where flag_cc = 0  and pk_settle in ("+CommonUtils.convertSetToSqlInPart(pkList, "pk_settle")+")";
				DataBaseHelper.execute(sqlDepo.toString(), new Object[]{blCc.getPkCc()});
				//更新BL_INVOICE表
				StringBuffer sqlUpd = new StringBuffer(" update BL_INVOICE inv set inv.flag_cc = '1',inv.pk_cc=? where exists ( ");
				sqlUpd.append(" select 1 from BL_ST_INV stinv where stinv.PK_INVOICE = inv.PK_INVOICE and ");
				sqlUpd.append(" stinv.PK_SETTLE in ( "+CommonUtils.convertSetToSqlInPart(pkList, "stinv.PK_SETTLE")+")) ");
				DataBaseHelper.execute(sqlUpd.toString(), new Object[]{blCc.getPkCc()});
				
				//查询取消结算信息所关联的正记录PK_SETTLE
				if(pkList!=null && pkList.size()>0){
					paramMap.put("pkList", pkList);
					List<String> canList = opBlccMapper.qryCanlStpkList(paramMap);
					pkList.addAll(canList);
				}
				
				StringBuffer sqlCancUpd = new StringBuffer(" update bl_invoice inv set FLAG_CC_CANCEL = '1',PK_CC_CANCEL=? ");
				sqlCancUpd.append("  where (FLAG_CC_CANCEL='0' or FLAG_CC_CANCEL is null) and pk_org=? and PK_EMP_CANCEL = ? and DATE_CANCEL <= ? ");
				sqlCancUpd.append(" and (exists(select 1 from BL_ST_INV stinv where stinv.PK_SETTLE in ("+CommonUtils.convertSetToSqlInPart(pkList, "stinv.PK_SETTLE")+") and stinv.PK_INVOICE = inv.PK_INVOICE) ");
				sqlCancUpd.append(" or inv.NOTE = '作废空发票') ");
				DataBaseHelper.execute(sqlCancUpd.toString(), new Object[]{blCc.getPkCc(),blCc.getPkOrg(),blCc.getPkEmpOpera(),dateTrans(dateEnd)});
			}
		}
		
		return res;
	}
	
	/**
	 * 交易码：007002005003
	 * 查询门诊日结账信息
	 * @param param
	 * @param user
	 * @return
	 */
	public List<BlCc> qryBlCcInfo(String param,IUser user){
		Map<String,Object> paramMap = JsonUtil.readValue(param, Map.class);
		if(CommonUtils.isNotNull(paramMap.get("dateEnd"))){
			paramMap.put("dateEnd", CommonUtils.getString(paramMap.get("dateEnd")).substring(0, 8)+"235959");
		}
		
		return opBlccMapper.qryBlccInfo(paramMap);
	}
	
	/**
	 * 交易号：007002005004
	 * 查询已结账信息
	 * @param param
	 * @param user
	 */
	public BlCcDt qrySummaryInfo(String param,IUser user){
		Map<String,Object> params = JsonUtil.readValue(param, Map.class);
		String pkCc  = (String)params.get("pkCc");
		if(CommonUtils.isEmptyString(pkCc))
			throw new BusException("未传入结账主键，请检查！");
		
		BlCcDt res = new BlCcDt();
		Map<String,Object> paramMap = new HashMap<>();
		paramMap.put("pkCc", pkCc);
		//1查询已结账明细信息
		List<BlCcPay> payList = opBlccMapper.qryUnSummaryDtls(paramMap);
		//1.1查询门诊所有的支付类型
		List<Map<String,Object>> dtList = opBlccMapper.qryOpPayMode();
		//1.2为了固定排序，重新组装未结账明细信息列表
		List<BlCcPay> newPayList = new ArrayList<>();
		for(Map<String,Object> dt : dtList){
			if(payList!=null && payList.size()>0){
				for(BlCcPay pay : payList){
					if(pay.getDtPaymode().equals(CommonUtils.getString(dt.get("dtPaymode")))){
						newPayList.add(pay);
						break;
					}
				}
			}
		}
		//2查询已结账记账信息
		List<AccAmtVo> accList = opBlccMapper.qryAccAmtDtls(paramMap);
		//2.1重新组装已结账记账信息
		BlCcPay jzVo = new BlCcPay();//记账
		jzVo.setNamePaymode("记账");
		jzVo.setAmt(0D);
		jzVo.setCntTrade(0L);
		jzVo.setAmtBack(0D);
		jzVo.setCntTradeBack(0L);
		newPayList.add(jzVo);
		
		boolean gyAddFlag = false;
		boolean ybAddFlag = false;
		if(accList!=null && accList.size()>0){
			for(AccAmtVo vo : accList){
				if(vo.getEuHptype().equals("3")){
					BlCcPay gyjzVo = new BlCcPay();//公医记账
					gyjzVo.setNamePaymode("公医记账");
					gyjzVo.setAmt(vo.getAmt());
					gyjzVo.setCntTrade(vo.getCntTrade());
					gyjzVo.setAmtBack(vo.getAmtBack());
					gyjzVo.setCntTradeBack(vo.getCntTradeBack());
					newPayList.add(gyjzVo);
					gyAddFlag = true;
				}else if(vo.getEuHptype().equals("1")){
					BlCcPay ybjzVo = new BlCcPay();//医保记账
					ybjzVo.setNamePaymode("医保记账");
					ybjzVo.setAmt(vo.getAmt());
					ybjzVo.setCntTrade(vo.getCntTrade());
					ybjzVo.setAmtBack(vo.getAmtBack());
					ybjzVo.setCntTradeBack(vo.getCntTradeBack());
					newPayList.add(ybjzVo);
					ybAddFlag = true;
				}
			}
		}
		if(!gyAddFlag){
			BlCcPay gyjzVo = new BlCcPay();//公医记账
			gyjzVo.setNamePaymode("公医记账");
			gyjzVo.setAmt(0D);
			gyjzVo.setCntTrade(0L);
			gyjzVo.setAmtBack(0D);
			gyjzVo.setCntTradeBack(0L);
			newPayList.add(gyjzVo);
		}
		if(!ybAddFlag){
			BlCcPay ybjzVo = new BlCcPay();//医保记账
			ybjzVo.setNamePaymode("医保记账");
			ybjzVo.setAmt(0D);
			ybjzVo.setCntTrade(0L);
			ybjzVo.setAmtBack(0D);
			ybjzVo.setCntTradeBack(0L);
			newPayList.add(ybjzVo);
		}
		
		//2.2查询收据数和人数
		List<Map<String,Object>> cntList = opBlccMapper.qrySumyCntInfo(paramMap);
		StringBuffer stJzStr = new StringBuffer("");
		Long sjCnt = 0L;//收据数
		Long jzCnt = 0L;//记账人次
		if(cntList!=null && cntList.size()>0){
			for(Map<String,Object> map :cntList){
				if(map!=null && map.size()>0){
					if(map.get("peoCnt")!=null && CommonUtils.getInteger(map.get("peoCnt"))>0){
						jzCnt = CommonUtils.getLong(map.get("peoCnt"));
					}else if(map.get("invCnt")!=null && CommonUtils.getInteger(map.get("invCnt"))>0){
						sjCnt = CommonUtils.getLong(map.get("invCnt"));
					}
				}
			}
		}
		stJzStr.append("收据数："+sjCnt+"   记账人次："+jzCnt);
		
		//3统计补打发票支付方式和发票使用张数
		List<Map<String,Object>> bdInvList = opBlccMapper.qryBdInvInfo(paramMap);
		StringBuffer bdInvStr = new StringBuffer("补打发票统计：");
		if(bdInvList!=null && bdInvList.size()>0){
			for(int i=0; i<bdInvList.size(); i++){
				if(i!=bdInvList.size()-1)
					bdInvStr.append(CommonUtils.getString(bdInvList.get(i).get("name"))+"补打"+CommonUtils.getString(bdInvList.get(i).get("cnt"))+"张"+",");
				else
					bdInvStr.append(CommonUtils.getString(bdInvList.get(i).get("name"))+"补打"+CommonUtils.getString(bdInvList.get(i).get("cnt"))+"张");
			}
		}else{
			bdInvStr.append("无");
		}
		
		//查询Bl_CC信息
		BlCc blCc = DataBaseHelper.queryForBean("select * from bl_cc where pk_cc = ?", BlCc.class, new Object[]{pkCc});
		
		res.setBlCc(blCc);
		res.setBlCcPayList(newPayList);
		res.setBdInvStr(bdInvStr.toString());
		res.setStJzStr(stJzStr.toString());
		
		return res;
	}
	
	/**
	 * 交易号：007002005005
	 * 取消结账
	 * @param param
	 * @param user
	 */
	public void summaryCanl(String param,IUser user){
		Map<String,Object> params = JsonUtil.readValue(param, Map.class);
		String pkCc = (String)params.get("pkCc");
		List<Map<String,Object>> cancelData = opBlccMapper.cancelData(pkCc);
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
		
		opBlccMapper.delBlCcPay(pkCc);
		opBlccMapper.delBlCcInv(pkCc);
		opBlccMapper.delBlCc(pkCc);
		opBlccMapper.updBlSt(pkCc);
		opBlccMapper.updBlDepo(pkCc);
		opBlccMapper.updBlInvoice(pkCc);
		opBlccMapper.updBlInvCanc(pkCc);
	}
	
	/**
	 * 交易号：007002005006
	 * 查询日结账记录选中明细的状态
	 * @param param pkCc 结账主键
	 * @param user 
	 */
	@SuppressWarnings("unchecked")
	public BlCc queryFocus(String param,IUser user){
		Map<String,Object> params = JsonUtil.readValue(param, Map.class);
		if(CommonUtils.isNotNull(params.get("dateBegin"))){
			params.put("dateBegin", CommonUtils.getString(params.get("dateBegin")).substring(0, 8)+"000000");
		}
		if(CommonUtils.isNotNull(params.get("dateEnd"))){
			params.put("dateEnd", CommonUtils.getString(params.get("dateEnd")).substring(0, 8)+"235959");
		}
		BlCc bc = new BlCc();
		bc = opBlccMapper.getEuStatus(params);		
		return bc;
	}
	
	/**
	 * 交易号：007002005007
	 * 查询结账明细信息
	 * @param param
	 * @param user
	 */
	public CcDts qrySummaryDts(String param,IUser user){
		Map<String,Object> paramMap = JsonUtil.readValue(param, Map.class);
		Map<String,Object> resMap = new HashMap<>();
		
		//查询付款方式信息
		List<Map<String,Object>> payInfo =  opBlccMapper.qrySumyPayInfo(paramMap);
		//查询已结账作废发票信息
		List<Map<String,Object>> canlInvInfo = opBlccMapper.qrySumyCanlInv(paramMap);
		//查询已结账收费信息
		List<Map<String,Object>> cgInfo = opBlccMapper.qrySumyCgInfo(paramMap);
		//查询已结账医保收费信息
		List<Map<String,Object>> hpInfo = opBlccMapper.qrySumyHpInfo(paramMap);
		//查询已结账信息
		BlCc blCc = DataBaseHelper.queryForBean("select * from bl_cc where pk_cc = ?",
				BlCc.class, new Object[]{CommonUtils.getString(paramMap.get("pkCc"))});
		
		CcDts ccDt = new CcDts();
		ccDt.setBlCc(blCc);
		ccDt.setCanlInvInfoList(canlInvInfo);
		ccDt.setPayInfoList(payInfo);
		ccDt.setCgInfoList(cgInfo);
		ccDt.setHpInfoList(hpInfo);
		
		return ccDt;
	}
	
	/**
	 * 交易号：007002005008
	 * 根据结账时间查询结账信息
	 * @param param
	 * @param user
	 * @return
	 */
	public BlCc qryBlccByDate(String param,IUser user){
		String dateCc = JsonUtil.getFieldValue(param, "dateEnd");
		if(CommonUtils.isEmptyString(dateCc))
			throw new BusException("请传入结账时间！");
		
		BlCc blCc = DataBaseHelper.queryForBean("select * from bl_cc where date_end = to_date(?,'yyyymmddhh24miss')",
				BlCc.class, new Object[]{dateCc});
		return blCc;
	}
	
	private BlCcDt qryUnJzInfo(String dateEnd){
		BlCcDt res = new BlCcDt();
		
		Map<String,Object> paramMap = new HashMap<>();
		paramMap.put("dateEnd", dateEnd);
		paramMap.put("pkEmp",  UserContext.getUser().getPkEmp());
		paramMap.put("pkOrg",  UserContext.getUser().getPkOrg());
		
		//2查询未结账明细信息
		List<BlCcPay> payList = opBlccMapper.qryUnSummaryDtls(paramMap);
		//2.1查询门诊所有的支付类型
		List<Map<String,Object>> dtList = opBlccMapper.qryOpPayMode();
		//2.2为了固定排序，重新组装未结账明细信息列表
		List<BlCcPay> newPayList = new ArrayList<>();
		for(Map<String,Object> dt : dtList){
			if(payList!=null && payList.size()>0){
				for(BlCcPay pay : payList){
					if(pay.getDtPaymode().equals(CommonUtils.getString(dt.get("dtPaymode")))){
						newPayList.add(pay);
						break;
					}
				}
			}
		}
		
		//3查询未结账记账信息
		List<AccAmtVo> accList = opBlccMapper.qryAccAmtDtls(paramMap);
		//3.1重新组装未结账记账信息
		BlCcPay jzVo = new BlCcPay();//记账
		jzVo.setNamePaymode("记账");
		jzVo.setAmt(0D);
		jzVo.setCntTrade(0L);
		jzVo.setAmtBack(0D);
		jzVo.setCntTradeBack(0L);
		newPayList.add(jzVo);
		
		boolean gyAddFlag = false;
		boolean ybAddFlag = false;
		if(accList!=null && accList.size()>0){
			for(AccAmtVo vo : accList){
				if(vo.getEuHptype().equals("3")){
					BlCcPay gyjzVo = new BlCcPay();//公医记账
					gyjzVo.setNamePaymode("公医记账");
					gyjzVo.setAmt(vo.getAmt());
					gyjzVo.setCntTrade(vo.getCntTrade());
					gyjzVo.setAmtBack(vo.getAmtBack());
					gyjzVo.setCntTradeBack(vo.getCntTradeBack());
					newPayList.add(gyjzVo);
					gyAddFlag = true;
				}else if(vo.getEuHptype().equals("1")){
					BlCcPay ybjzVo = new BlCcPay();//医保记账
					ybjzVo.setNamePaymode("医保记账");
					ybjzVo.setAmt(vo.getAmt());
					ybjzVo.setCntTrade(vo.getCntTrade());
					ybjzVo.setAmtBack(vo.getAmtBack());
					ybjzVo.setCntTradeBack(vo.getCntTradeBack());
					newPayList.add(ybjzVo);
					ybAddFlag = true;
				}
			}
		}
		if(!gyAddFlag){
			BlCcPay gyjzVo = new BlCcPay();//公医记账
			gyjzVo.setNamePaymode("公医记账");
			gyjzVo.setAmt(0D);
			gyjzVo.setCntTrade(0L);
			gyjzVo.setAmtBack(0D);
			gyjzVo.setCntTradeBack(0L);
			newPayList.add(gyjzVo);
		}
		if(!ybAddFlag){
			BlCcPay ybjzVo = new BlCcPay();//医保记账
			ybjzVo.setNamePaymode("医保记账");
			ybjzVo.setAmt(0D);
			ybjzVo.setCntTrade(0L);
			ybjzVo.setAmtBack(0D);
			ybjzVo.setCntTradeBack(0L);
			newPayList.add(ybjzVo);
		}
			
		//4查询发票号段
		List<StInvInfo> invInfo = opBlccMapper.qryStInvInfo(paramMap);
		//5查询作废发票
		List<InvalidStInv> InvalidList = opBlccMapper.qryStInv_Invalid(paramMap);
		//6统计补打发票支付方式和发票使用张数
		List<Map<String,Object>> bdInvList = opBlccMapper.qryBdInvInfo(paramMap);
		StringBuffer bdInvStr = new StringBuffer("补打发票统计：");
		if(bdInvList!=null && bdInvList.size()>0){
			for(int i=0; i<bdInvList.size(); i++){
				if(i!=bdInvList.size()-1)
					bdInvStr.append(CommonUtils.getString(bdInvList.get(i).get("name"))+"补打"+CommonUtils.getString(bdInvList.get(i).get("cnt"))+"张"+",");
				else
					bdInvStr.append(CommonUtils.getString(bdInvList.get(i).get("name"))+"补打"+CommonUtils.getString(bdInvList.get(i).get("cnt"))+"张");
			}
		}else{
			bdInvStr.append("无");
		}
		
		//查询收据数和人数
		List<Map<String,Object>> cntList = opBlccMapper.qrySumyCntInfo(paramMap);
		StringBuffer stJzStr = new StringBuffer("");
		Long sjCnt = 0L;//收据数
		Long jzCnt = 0L;//记账人次
		if(cntList!=null && cntList.size()>0){
			for(Map<String,Object> map :cntList){
				if(map!=null && map.size()>0){
					if(map.get("peoCnt")!=null && CommonUtils.getInteger(map.get("peoCnt"))>0){
						jzCnt = CommonUtils.getLong(map.get("peoCnt"));
					}else if(map.get("invCnt")!=null && CommonUtils.getInteger(map.get("invCnt"))>0){
						sjCnt = CommonUtils.getLong(map.get("invCnt"));
					}
				}
			}
		}
		stJzStr.append("收据数："+sjCnt+"   记账人次："+jzCnt);
		
		BlCc blCc = new BlCc();
		blCc.setPkOrg(UserContext.getUser().getPkOrg());
		blCc.setPkEmpOpera(UserContext.getUser().getPkEmp());
		blCc.setPkOrg(UserContext.getUser().getPkOrg());
		blCc.setNameEmpOpera(UserContext.getUser().getNameEmp());
		blCc.setPkDept(UserContext.getUser().getPkDept());
		blCc.setDateEnd(dateTrans(dateEnd));
		blCc.setDateBegin(null);//暂时写null
		blCc.setFlagClear("0");
		blCc.setFlagLeader("0");
		blCc.setEuStatus("0");
		blCc.setDateCc(new Date());
		blCc.setEuCctype("2");
		blCc.setAmtPi(0D);
		blCc.setAmtPiBack(0D);
		handleDateBegin(blCc,new String[]{"01"},"2");//门诊收费
		
		//结算收款
		paramMap.put("flagPv", "0");
        BlCc voSt = opBlccMapper.qryStAmtInfo(paramMap);
        blCc.setAmtInsu(voSt==null?0.0:voSt.getAmtInsu());
        blCc.setAmtSettle(voSt==null?0.0:voSt.getAmtSettle());
        //发票信息
        if(invInfo!=null && invInfo.size()>0){
        	StringBuffer invInfoStr = new StringBuffer(); 
        	BigDecimal cntInv = BigDecimal.ZERO;
        	for(int i = 0;i<invInfo.size();i++){
        		StInvInfo vo = invInfo.get(i);
        		invInfoStr.append(vo.getBegincode());
        		invInfoStr.append("-");
        		invInfoStr.append(vo.getEndcode());
        		if(i+1==invInfo.size()){
        			invInfoStr.append(";");
        		}else{
        			invInfoStr.append(",");
        		}
        		
        		cntInv = cntInv.add(vo.getCnt());
        	}
        	blCc.setInvInfo(invInfoStr.toString());
        	blCc.setCntInv(cntInv.intValue());
        }
        //作废发票信息
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
        
		res.setBlCc(blCc);
		res.setBlCcPayList(newPayList);
		res.setBdInvStr(bdInvStr.toString());
		res.setStJzStr(stJzStr.toString());
		res.setInvInfoList(invInfo);
		res.setInvalidList(InvalidList);
		
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
	/**
	 * 获取结账开始时间
	 * @param cc
	 * @param  dtSttypes
	 * @param euCctype
	 */
	public void handleDateBegin(BlCc cc,String []dtSttypes,String euCctype) {
		Date dateBegin;
		Date dateEnd = cc.getDateEnd();
		BlCc blCcLast = null;
		Map<String,Object> paramMap = new HashMap<String,Object>();
		paramMap.put("pkEmp", cc.getPkEmpOpera());
		paramMap.put("euCctype", euCctype);
		if(Application.isSqlServer()){ //是否sqlserver
			 blCcLast = opBlccMapper.getLastBlCcByPkEmpForSql(paramMap);
		}else{
			 blCcLast = opBlccMapper.getLastCcByPkEmp(paramMap);
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
			paramMap.put("dtSttype", dtSttypes);
			if(Application.isSqlServer()){
				blSettle = opBlccMapper.getLastBlSettleByPkEmpForSql(paramMap);
			}else{
				blSettle = opBlccMapper.getLastStByPkEmp(paramMap);
			}
			if(blSettle != null){
				dateBegin = blSettle.getDateSt();
				if(dateEnd.getTime() < dateBegin.getTime()){
					throw new BusException("截止时间不能早于最早的结算时间！");
				}
			}else{
				throw new BusException("该操作员不存在结算记录！");
			}
		}
		cc.setDateBegin(dateBegin);
	}
	
}
