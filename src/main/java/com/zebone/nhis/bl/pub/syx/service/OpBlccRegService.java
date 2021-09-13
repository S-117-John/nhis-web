package com.zebone.nhis.bl.pub.syx.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;

import org.apache.commons.collections.MapUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zebone.nhis.bl.pub.syx.dao.OpBlccMapper;
import com.zebone.nhis.bl.pub.syx.dao.OpBlccRegMapper;
import com.zebone.nhis.bl.pub.syx.vo.BlCcDt;
import com.zebone.nhis.common.module.base.bd.price.BdInvcate;
import com.zebone.nhis.common.module.bl.BlCc;
import com.zebone.nhis.common.module.bl.BlCcInv;
import com.zebone.nhis.common.module.bl.BlCcPay;
import com.zebone.nhis.common.support.ApplicationUtils;
import com.zebone.nhis.common.support.CommonUtils;
import com.zebone.platform.Application;
import com.zebone.platform.common.support.NHISUUID;
import com.zebone.platform.common.support.User;
import com.zebone.platform.common.support.UserContext;
import com.zebone.platform.framework.support.IUser;
import com.zebone.platform.modules.dao.DataBaseHelper;
import com.zebone.platform.modules.exception.BusException;
import com.zebone.platform.modules.utils.JsonUtil;

/**
 * 门诊挂号日结账--中二
 * @author yangxue
 *
 */
@Service
public class OpBlccRegService {
	@Resource
	private OpBlccRegMapper opblccRegMapper;
	@Resource
	private OpBlccService opBlccService;
	@Autowired
	private OpBlccMapper opBlccMapper;
	/**
	 * 查询挂号日结记录
	 * @param param
	 * @param user
	 * @return
	 */
     public List<Map<String,Object>> queryBlccList(String param,IUser user){
    	 Map<String,Object> paramMap = JsonUtil.readValue(param, Map.class);
    	 if(paramMap==null||CommonUtils.isNull(paramMap.get("dateCcBegin"))||CommonUtils.isNull(paramMap.get("dateCcEnd")))
    		 throw new BusException("请传入结账类型、结账起止日期！");
    	 paramMap.put("euCctype", "1");
    	 paramMap.put("pkOrg", ((User)user).getPkOrg());
    	 return opblccRegMapper.queryBlccList(paramMap);
     }
     /**
      * 查询挂号结账明细
      * @param param
      * @param user
      * @return
      */
     public Map<String,Object> queryBlccDetail(String param,IUser user){
    	 Map<String,Object> paramMap = JsonUtil.readValue(param, Map.class);
    	 if(paramMap==null||CommonUtils.isNull(paramMap.get("pkCc"))||CommonUtils.isNull(paramMap.get("pkEmpReceipt"))){
    		 throw new BusException("请传入结账主键或开票人主键！");
    	 }
    	 //查询挂号类型及支付方式
    	 List<Map<String,Object>> paylist = opblccRegMapper.queryBlccByPaymode(paramMap);
    	 //查询发票号段
    	 BlCc blcc = opblccRegMapper.queryBlCcInfoByBlccPay(paramMap);
    	 //根据支付方式查询发票补打张数
    	 List<Map<String,Object>> receiptList = opblccRegMapper.queryReceiptByPaymode(paramMap);
    	 StringBuilder invInfo = new StringBuilder();
    	 if(blcc!=null && blcc.getCntInv()!=null && blcc.getCntInv().intValue() >0){
    		 invInfo.append(blcc.getInvInfo()).append(" 共  ").append(blcc.getCntInv()).append("张收据 ");
    	 }
    	 if(receiptList!=null&&receiptList.size()>0){
    		 for(Map<String,Object> rep:receiptList){
    			 if(rep!=null&&CommonUtils.isNotNull(rep.get("namePaymode")))
    				 invInfo.append(CommonUtils.getString(rep.get("namePaymode"))).append(" ").append(CommonUtils.getInteger(rep.get("cnt"))).append("张 "); 
    		 }
    	 }
    	//查询退号收据
    	 StringBuilder rtnInfo = new StringBuilder();
    	 List<Map<String,Object>> rtnReceiptList = DataBaseHelper.queryForList("select st.receipt_no, to_char(st.date_receipt,'YY-MM-DD') as date_receipt, emp.code_emp from bl_settle st inner join bd_ou_employee emp "
    	 		+ " on st.pk_emp_receipt=emp.pk_emp where st.pk_cc=? and st.dt_sttype='20' and st.flag_cc='1'", new Object[]{paramMap.get("pkCc")});
    	 constructRtnReceipt(rtnInfo, rtnReceiptList);
    	 
    	 Map<String,Object> result = new HashMap<String,Object>();
    	 result.put("dataList", paylist);//挂号信息列表
    	 result.put("receiptInfo", invInfo.toString());//收据凭证编号
    	 result.put("rtnReceiptInfo", rtnInfo.toString());//退号收据凭证编号
    	 return result;
    	 
     }
	private void constructRtnReceipt(StringBuilder rtnInfo, List<Map<String, Object>> rtnReceiptList) {
		if(rtnInfo!=null && rtnReceiptList!=null&&rtnReceiptList.size()>0){
			for(Map<String,Object> rep:rtnReceiptList){
				 if(rep!=null&&CommonUtils.isNotNull(rep.get("receiptNo")))
					 rtnInfo.append(CommonUtils.getString(rep.get("receiptNo"))).append("(").append(CommonUtils.getString(rep.get("dateReceipt")))
					 .append(" ").append(CommonUtils.getString(rep.get("codeEmp"))).append(") ")
					 .append(" ");
			 }
		}
	}
     
     /**
      * 查询挂号未结账明细
      * @param param
      * @param user
      * @return
      */
     public Map<String,Object> queryUnBlccDetail(String param,IUser user){
    	 Map<String,Object> paramMap = JsonUtil.readValue(param, Map.class);
    	 if(paramMap==null||CommonUtils.isNull(paramMap.get("pkEmp"))||CommonUtils.isNull(paramMap.get("dateEnd"))){
    		 throw new BusException("请传入操作员主键或截止日期！");
    	 }
    	 //查询未结账挂号类型及支付方式
    	 List<Map<String,Object>> paylist = opblccRegMapper.queryUnBlccByPaymode(paramMap);
    	 
    	 //查询发票张数及起始号
    	 Map<String,Object> receipt = opblccRegMapper.queryUnBlccReceipt(paramMap);
    	 //查询退号收据
    	 List<Map<String,Object>> rtnReceiptList = opblccRegMapper.queryUnBlccRtnReceipt(paramMap);
    	 StringBuilder invInfo = new StringBuilder();
    	 StringBuilder rtnInfo = new StringBuilder();
    	 if(receipt!=null && MapUtils.getIntValue(receipt, "cnt",0) >0){
    		 invInfo.append(CommonUtils.getString(receipt.get("beginInv"))).append("-").append(CommonUtils.getString(receipt.get("endInv"))).append(" 共 ")
    		 .append(CommonUtils.getInteger(receipt.get("cnt"))).append("张收据 ");
    	 }
    	 constructRtnReceipt(rtnInfo, rtnReceiptList);
    	 Map<String,Object> result = new HashMap<String,Object>();
    	 result.put("dataList", paylist);//挂号信息列表
    	 result.put("receiptInfo", invInfo.toString());//收据凭证编号
    	 result.put("rtnReceiptInfo", rtnInfo.toString());//退号收据凭证编号
    	 return result;
    	 
     }
     
     /**
      * 保存结账信息
      * @param param
      * @param user
      * @return
      */
     public BlCc saveBlcc(String param,IUser user){
    	 Map<String,Object> paramMap = JsonUtil.readValue(param, Map.class);
    	 if(paramMap==null||CommonUtils.isNull(paramMap.get("pkEmp"))||CommonUtils.isNull(paramMap.get("dateEnd"))){
    		 throw new BusException("请传入操作员主键或截止日期！");
    	 }
    	 BlCc blCc = null;
    	 BlCcDt ccDt = constructCcInfo(paramMap);
    	 if(ccDt!=null){
 			//写表Bl_CC
 			blCc = ccDt.getBlCc();
 			blCc.setPkCc(NHISUUID.getKeyId());
 			ApplicationUtils.setDefaultValue(blCc, true);
 			DataBaseHelper.insertBean(blCc);
 			//写表bl_cc_pay；
 			List<BlCcPay> payList = new ArrayList<>(ccDt.getBlCcPayList());
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
 			if(ccDt.getBlCcInvList()!=null){
 				for(BlCcInv vo :ccDt.getBlCcInvList()){
 					vo.setFlagCanc("0");
 					vo.setFlagWg("0");
 					vo.setPkCc(blCc.getPkCc());
 					vo.setPkOrg(UserContext.getUser().getPkOrg());
 					vo.setPkCcinv(NHISUUID.getKeyId());
 					ApplicationUtils.setDefaultValue(vo, true);
 				}
 				DataBaseHelper.batchUpdate(DataBaseHelper.getInsertSql(BlCcInv.class), ccDt.getBlCcInvList());
 			}
 			//更新业务表结账标志
 			Map<String,Object> queryParam = new HashMap<>();
 			queryParam.put("pkCc", blCc.getPkCc());
 			queryParam.put("pkOrg", blCc.getPkOrg());
 			queryParam.put("pkEmp",blCc.getPkEmpOpera());
 			queryParam.put("dateEnd",paramMap.get("dateEnd"));
 			queryParam.put("flagPv", "1");
 			Set<String> pkList = opBlccMapper.qryUpPkSt(paramMap);
 			if(pkList!=null && pkList.size()>0){
 				//更新bl_settle表
 				String sqlSt = "update bl_settle   set flag_cc = 1,pk_cc =  ? where  flag_cc = 0 and pk_settle in ("+CommonUtils.convertSetToSqlInPart(pkList, "pk_settle")+")";
 				DataBaseHelper.execute(sqlSt.toString(), new Object[]{blCc.getPkCc()});
 				//更新bl_deposit表
 				String sqlDepo = " update bl_deposit set flag_cc = 1, pk_cc = ? where flag_cc = 0  and pk_settle in ("+CommonUtils.convertSetToSqlInPart(pkList, "pk_settle")+")";
 				DataBaseHelper.execute(sqlDepo.toString(), new Object[]{blCc.getPkCc()});
 			}
 		}
    	 return blCc;
     }
     /**
      * 构建结账信息
      * @param dateEnd
      * @return
      */
     private BlCcDt constructCcInfo(Map<String,Object> paramMap){
 		BlCcDt res = new BlCcDt();
 		//查询未结账支付明细信息
 		List<BlCcPay> payList = opblccRegMapper.qryUnBlccPayDetail(paramMap);
 		//查询票据号段
 		Map<String,Object> receipt = opblccRegMapper.queryUnBlccReceipt(paramMap);
 		
 		BlCc blCc = new BlCc();
 		blCc.setPkOrg(UserContext.getUser().getPkOrg());
 		blCc.setPkEmpOpera(UserContext.getUser().getPkEmp());
 		blCc.setPkOrg(UserContext.getUser().getPkOrg());
 		blCc.setNameEmpOpera(UserContext.getUser().getNameEmp());
 		blCc.setPkDept(UserContext.getUser().getPkDept());
 		blCc.setDateBegin(null);//暂时写null
 		blCc.setFlagClear("0");
 		blCc.setFlagLeader("0");
 		blCc.setEuStatus("0");
 		blCc.setDateCc(new Date());
 		blCc.setEuCctype("1");
 		blCc.setAmtPi(0D);
 		blCc.setAmtPiBack(0D);
 		blCc.setDateEnd(opBlccService.dateTrans(CommonUtils.getString(paramMap.get("dateEnd"))));
 		opBlccService.handleDateBegin(blCc,new String[]{"00","20"},"1");
 		 //结算收款
 		 paramMap.put("flagPv", "1");
         BlCc voSt = opBlccMapper.qryStAmtInfo(paramMap);
         blCc.setAmtInsu(voSt==null?0.0:voSt.getAmtInsu());
         blCc.setAmtSettle(voSt==null?0.0:voSt.getAmtSettle());
         
         BlCcInv inv = new BlCcInv();
         //发票信息
         if(receipt!=null){
        	paramMap.put("euType", "3");
        	BdInvcate invcate = opblccRegMapper.queryInvcateByCate(paramMap);
        	if(invcate!=null)
        		inv.setPkInvcate(invcate.getPkInvcate());
         	StringBuffer invInfoStr = new StringBuffer(); 
         	inv.setBeginNo(CommonUtils.getString(receipt.get("beginInv")));
         	inv.setEndNo(CommonUtils.getString(receipt.get("endInv")));
         	invInfoStr.append(inv.getBeginNo());
         	invInfoStr.append("-");
         	invInfoStr.append(inv.getEndNo());
         	invInfoStr.append(";");
         	blCc.setInvInfo(invInfoStr.toString());
         	blCc.setCntInv(CommonUtils.getInteger(receipt.get("cnt")));
         }
        
        List<BlCcInv> invList = new ArrayList<BlCcInv>();
        invList.add(inv);
 		res.setBlCc(blCc);
 		res.setBlCcInvList(invList);
 		res.setBlCcPayList(payList);
 		return res;
 	}
     /**
 	 * 
 	 * 取消结账
 	 * @param param
 	 * @param user
 	 */
 	public void cancelCc(String param,IUser user){
 		Map<String,Object> params = JsonUtil.readValue(param, Map.class);
 		String pkCc = (String)params.get("pkCc");
 		List<Map<String,Object>> cancelData = opblccRegMapper.queryCanCancelData(pkCc);
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
 	}
 	
} 
