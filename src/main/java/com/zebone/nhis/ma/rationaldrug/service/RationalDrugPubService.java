package com.zebone.nhis.ma.rationaldrug.service;

import com.zebone.nhis.common.module.cn.ipdw.CnOrder;
import com.zebone.nhis.common.module.cn.ipdw.CnPrescription;
import com.zebone.nhis.common.module.pi.PiAddress;
import com.zebone.nhis.common.module.pi.PiAllergic;
import com.zebone.nhis.common.module.pi.PiMaster;
import com.zebone.nhis.common.module.pv.PvDiag;
import com.zebone.nhis.common.support.ApplicationUtils;
import com.zebone.nhis.common.support.CommonUtils;
import com.zebone.nhis.ma.rationaldrug.dao.RationalDrugPubMapper;
import com.zebone.platform.framework.support.IUser;
import com.zebone.platform.modules.exception.BusException;
import com.zebone.platform.modules.utils.JsonUtil;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
/**
 * 合理用药公共服务（各个厂商个性化需求内容请单独创建service）
 * @author yangxue
 *
 */
@Service
public class RationalDrugPubService {
	@Resource
	private RationalDrugPubMapper rationalDrugPubMapper;
	
   /**
    * 查询患者信息
    * @param param{pkPi:患者主键}
    * @param user
    * @return{piInfo:患者信息,addressInfos:地址信息,allergicInfos:过敏史}
    */
   public Map<String,Object> getPiInfo(String param,IUser user){
	   Map<String,Object> paramMap = JsonUtil.readValue(param, Map.class);
	   if(paramMap == null||CommonUtils.isNull(paramMap.get("pkPi")))
		   throw new BusException("未获取到患者主键！");
	   PiMaster pi = rationalDrugPubMapper.queryPiMaster(paramMap);
	   List<PiAddress> addressList = rationalDrugPubMapper.queryPiAddress(paramMap);
	   List<PiAllergic>  allergicList = rationalDrugPubMapper.queryPiAllergic(paramMap);
	   paramMap.clear();
	   paramMap.put("piInfo", pi);
	   paramMap.put("addressInfos", addressList);
	   paramMap.put("allergicInfos", allergicList);
	   return paramMap;
   }
   /**
    * 查询住院患者信息
    * @param param{pkPv:患者主键}
    * @param user
    * @return
    */
   public Map<String,Object> getPvIpInfo(String param,IUser user){
	   Map<String,Object> paramMap = JsonUtil.readValue(param, Map.class);
	   if(paramMap == null||CommonUtils.isNull(paramMap.get("pkPv")))
		   throw new BusException("未获取到患者主键！");
	   getPiOpIpinfo(paramMap,"ip" );
	   return paramMap;
	   
   }
/**
 * @Description 查询患者门诊就诊信息
 * 010007001006
 * @auther wuqiang
 * @Date 2019/7/30
 * @Param [param, user]
 * @return java.util.Map<java.lang.String,java.lang.Object>
 */
   public Map<String,Object> getPiOpInfo(String param,IUser user){
	   Map<String,Object> paramMap = JsonUtil.readValue(param, Map.class);
	   getPiOpIpinfo(paramMap,"op" );
	   return paramMap;

   }
	/**
	 * @Description 查询患者住院，门诊就诊信息
	 * @auther wuqiang
	 * @Date 2019/7/30
	 * @Param [paramMap, op：为op时查询患者门诊信息]
	 * @return void
	 */
	private void getPiOpIpinfo(Map<String, Object> paramMap,String op) {

		if(paramMap == null|| CommonUtils.isNull(paramMap.get("pkPv"))){
			throw new BusException("未获取到患者主键！");
		}
		Map<String,Object> pvIp;
		if ("op".equals(op)){
			pvIp = rationalDrugPubMapper.queryPvOpInfo(paramMap);
		}else {
			pvIp = rationalDrugPubMapper.queryPvIpInfo(paramMap);
		}
		List<PvDiag> pvDiagList = rationalDrugPubMapper.queryPvDiags(paramMap);
		BigDecimal amt = rationalDrugPubMapper.queryIpPreAmt(paramMap);
		paramMap.clear();
		paramMap.put("pvInfo", pvIp);
		paramMap.put("pvDiags", pvDiagList);
		paramMap.put("pvAcc", amt);
		paramMap.put("isIns", pvIp.get("dtExthp")==null?"0":"1");
	}

	/**
    * 查询医嘱信息
    * @param param{pkCnords}
    * @param user
    * @return
    */
   public List<Map<String,Object>> getOrdInfos(String param,IUser user) {
	   Map<String, Object> paramMap = JsonUtil.readValue(param, Map.class);
	   if (paramMap == null || (CommonUtils.isNull(paramMap.get("pkCnords")) && CommonUtils.isNull(paramMap.get("ordSns")))) {
		   throw new BusException("未获取到查询医嘱主键/序号！");
	   }
	   return rationalDrugPubMapper.queryOrderInfo(paramMap);
   }
   /**
    * 查询处方信息
    * @param param{pkPres:处方主键}
    * @param user
    * @return
    */
   public Map<String,Object> getPresInfo(String param,IUser user){
	   Map<String,Object> paramMap = JsonUtil.readValue(param, Map.class);
	   if(paramMap == null||CommonUtils.isNull(paramMap.get("pkPres")))
		   throw new BusException("未获取到处方主键！");
	   CnPrescription pres = rationalDrugPubMapper.queryPresInfo(paramMap);
	   List<Map<String,Object>> orders = rationalDrugPubMapper.queryOrderInfo(paramMap);
	   paramMap.clear();
	   paramMap.put("presInfo", pres);
	   paramMap.put("orders",orders);
	   return paramMap;
   }
   /**
    * 查询草药医嘱信息
    * @param param
    * @param user
    * @return
    */
   public Map<String,Object> getHerbOrds(String param,IUser user) throws Exception {
	   Map<String,Object> paramMap = JsonUtil.readValue(param, Map.class);
          if(paramMap == null||CommonUtils.isNull(paramMap.get("pkPres")))
		   throw new BusException("未获取到处方主键！");
	   CnPrescription pres = rationalDrugPubMapper.queryPresInfo(paramMap);
	   List<Map<String,Object>> orders = rationalDrugPubMapper.queryOrderInfo(paramMap);
	   List<Map<String,Object>> herbs = rationalDrugPubMapper.queryHerbOrdInfo(paramMap);
	   CnOrder cnOrder=ApplicationUtils.mapToBean(orders.get(0),CnOrder.class);
	   paramMap.clear();
	   paramMap.put("presInfo", pres);
	   paramMap.put("orderInfo",cnOrder);
	   paramMap.put("herbInfos", herbs);
	   return paramMap;
   }
   
}
