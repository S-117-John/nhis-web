package com.zebone.nhis.bl.pub.support;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.zebone.nhis.bl.pub.service.ICgService;
import com.zebone.nhis.bl.pub.service.OperatorAccountService;
import com.zebone.nhis.bl.pub.vo.BlCcDt;
import com.zebone.nhis.bl.pub.vo.BlPubParamVo;
import com.zebone.nhis.bl.pub.vo.BlPubReturnVo;
import com.zebone.nhis.bl.pub.vo.OpBlCcVo;
import com.zebone.nhis.common.module.bl.BlIpDt;
import com.zebone.nhis.common.module.bl.RefundVo;
import com.zebone.nhis.common.support.ApplicationUtils;
import com.zebone.nhis.ma.pub.support.ExtSystemProcessUtils;
import com.zebone.platform.modules.core.spring.ServiceLocator;

/**
 * 记费策略服务
 * @author yangxue
 *
 */
public class CgProcessUtils {
	/**
	 * 处理住院记费服务
	 * @param blCgPubParamVos
	 * @param isAllowQF 是否允许欠费
	 * @return enable:是否启用自定义记费服务标志，result:记费后返回的集合
	 */
	public static Map<String,Object> processIpCg(List<BlPubParamVo> blCgPubParamVos,boolean isAllowQF){
		
		//获取是否启用自定义记费服务标志 对应配置文件cg.properties配置文件
		Map<String,Object> result = new HashMap<String,Object>();
		if ("true".equals(ApplicationUtils.getPropertyValue("cg.ip.enable", ""))) {
			result.put("enable", "true");
			//获取自定义记费服务service，对应配置文件cg.properties配置文件
			String processClass = ApplicationUtils.getPropertyValue("cg.ip.processClass", "");
			Object bean = ServiceLocator.getInstance().getBean(processClass);
			if (bean != null) {
				ICgService handler = (ICgService) bean;
				BlPubReturnVo retvo = handler.chargeIpBatch(blCgPubParamVos,isAllowQF);
				result.put("result", retvo);
			}
		}else{
			result.put("enable", "false");
		}
		return result;
	}
	/**
	 * 处理住院记费服务
	 * @param params 退费明细集合
	 * @param dtlist bl_ip_dt表插入的退费明细
	 * @return enable:是否启用自定义记费服务标志，result:记费后返回的集合
	 */
	public static Map<String,Object> processIpRefund(List<RefundVo> params,List<BlIpDt> dtlist){
		//获取是否启用自定义退费服务标志 对应配置文件cg.properties配置文件
		Map<String,Object> result = new HashMap<String,Object>();
		if ("true".equals(ApplicationUtils.getPropertyValue("refund.ip.enable", ""))) {
			result.put("enable", "true");
			//获取自定义记费服务service，对应配置文件cg.properties配置文件
			String processClass = ApplicationUtils.getPropertyValue("refund.ip.processClass", "");
			Object bean = ServiceLocator.getInstance().getBean(processClass);
			if (bean != null) {
				ICgService handler = (ICgService) bean;
				BlPubReturnVo retvo = handler.refundInBatch(params,dtlist);
				if(retvo!=null){
					result.put("result", retvo);
				}
			}
		}else{
			result.put("enable", "false");
		}
		return result;
	}
	
	/**
	 * 处理门诊记费服务
	 * @param blOpCgPubParamVos
	 * @return enable:是否启用自定义记费服务标志，result:记费后返回的集合
	 */
	public static Map<String,Object> processOpCg(List<BlPubParamVo> blOpCgPubParamVos){
		//获取是否启用自定义记费服务标志 对应配置文件cg.properties配置文件
		Map<String,Object> result = new HashMap<String,Object>();
		if ("true".equals(ApplicationUtils.getPropertyValue("cg.op.enable", ""))) {
			result.put("enable", "true");
			//获取自定义记费服务service，对应配置文件cg.properties配置文件
			String processClass = ApplicationUtils.getPropertyValue("cg.op.processClass", "");
			Object bean = ServiceLocator.getInstance().getBean(processClass);
			if (bean != null) {
				ICgService handler = (ICgService) bean;
				BlPubReturnVo retvo = handler.chargeOpBatch(blOpCgPubParamVos);
				result.put("result", retvo);

				ExtSystemProcessUtils.processExtMethod("CONSUMABLE", "saveOpConsumable", blOpCgPubParamVos,retvo);
			}
		}else{
			result.put("enable", "false");
		}
		return result;
	}
	/**
	 * 更新住院记费服务
	 * @param blCgPubParamVos
	 * @return enable:是否启用自定义记费服务标志，result:记费后返回的集合
	 */
	public static Map<String,Object>  updateIpCg(String pkPv,String pkHp,String pkPicate,String oldPkInsu,String oldPkPicate){
		Map<String,Object> result = new HashMap<String,Object>();
		//获取是否启用自定义记费服务标志 对应配置文件cg.properties配置文件
		if ("true".equals(ApplicationUtils.getPropertyValue("cg.ip.enable", ""))) {
			result.put("enable", "true");
			//获取自定义记费服务service，对应配置文件cg.properties配置文件
			String processClass = ApplicationUtils.getPropertyValue("cg.ip.processClass", "");
			Object bean = ServiceLocator.getInstance().getBean(processClass);
			if (bean != null) {
				ICgService handler = (ICgService) bean;
				handler.updateBlIpDt(pkPv, pkHp, pkPicate,oldPkInsu,oldPkPicate);
			}
		}else{
			result.put("enable", "false");
		}
		return result;
	}
	/**
	 * 更新门诊记费服务
	 * @param blOpCgPubParamVos
	 * @return enable:是否启用自定义记费服务标志，result:记费后返回的集合
	 */
	public static Map<String,Object>  updateOpCg(String pkPv,String pkHp,String pkPicate,String oldPkInsu,String oldPkPicate){
		Map<String,Object> result = new HashMap<String,Object>();
		//获取是否启用自定义记费服务标志 对应配置文件cg.properties配置文件
		if ("true".equals(ApplicationUtils.getPropertyValue("cg.op.enable", ""))) {
			result.put("enable", "true");
			//获取自定义记费服务service，对应配置文件cg.properties配置文件
			String processClass = ApplicationUtils.getPropertyValue("cg.op.processClass", "");
			Object bean = ServiceLocator.getInstance().getBean(processClass);
			if (bean != null) {
				ICgService handler = (ICgService) bean;
				handler.updateBlOpDt(pkPv, pkHp, pkPicate,oldPkInsu,oldPkPicate);
			}
		}else{
			result.put("enable", "false");
		}
		return result;
	}
	
	/**
	 * 处理住院日结账个性化服务
	 * param:日结账已查询出的参数
	 * @return enable:是否启用自定义记费服务标志，result:查询日结信息后的VO
	 */
	public static Map<String,Object> processIpOperatorAccount(BlCcDt param){
		//获取是否启用自定义退费服务标志 对应配置文件cg.properties配置文件
		Map<String,Object> result = new HashMap<String,Object>();
		if ("true".equals(ApplicationUtils.getPropertyValue("ip.operatorAccount.enable", ""))) {
			result.put("enable", "true");
			//获取自定义记费服务service，对应配置文件cg.properties配置文件
			String processClass = ApplicationUtils.getPropertyValue("ip.operatorAccount.processClass", "");
			Object bean = ServiceLocator.getInstance().getBean(processClass);
			if (bean != null) {
				OperatorAccountService handler = (OperatorAccountService) bean;
				BlCcDt retvo = handler.getIpOperAccInfo(param);
				if(retvo!=null){
					result.put("result", retvo);
				}
			}
		}else{
			result.put("enable", "false");
		}
		return result;
	}
	
	/**
	 * 处理住院日结账个性化服务
	 * param:日结账已查询出的参数
	 * @return enable:是否启用自定义记费服务标志，result:查询日结信息后的VO
	 */
	public static Map<String,Object> processOpOperatorAccount(OpBlCcVo param){
		//获取是否启用自定义退费服务标志 对应配置文件cg.properties配置文件
		Map<String,Object> result = new HashMap<String,Object>();
		if ("true".equals(ApplicationUtils.getPropertyValue("op.operatorAccount.enable", ""))) {
			result.put("enable", "true");
			//获取自定义记费服务service，对应配置文件cg.properties配置文件
			String processClass = ApplicationUtils.getPropertyValue("op.operatorAccount.processClass", "");
			Object bean = ServiceLocator.getInstance().getBean(processClass);
			if (bean != null) {
				OperatorAccountService handler = (OperatorAccountService) bean;
				OpBlCcVo retvo = handler.getOpOperAccInfo(param);
				if(retvo!=null){
					result.put("result", retvo);
				}
			}
		}else{
			result.put("enable", "false");
		}
		return result;
	}
}
