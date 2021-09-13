package com.zebone.nhis.webservice.cxf.impl;

import com.zebone.nhis.common.support.ApplicationUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import javax.jws.soap.SOAPBinding.Style;

import com.zebone.nhis.common.support.ApplicationUtils;

import com.zebone.nhis.common.support.CommonUtils;
import com.zebone.nhis.webservice.cxf.INHISWXWebService;
import com.zebone.nhis.webservice.service.BdPubForWsService;
import com.zebone.nhis.webservice.service.BlPubForWsService;
import com.zebone.nhis.webservice.service.PvPubForWsService;
import com.zebone.nhis.webservice.service.SchPubForWsService;
import com.zebone.nhis.webservice.support.RespJson;
import com.zebone.nhis.webservice.vo.SchForExtVo;
import com.zebone.platform.modules.utils.JsonUtil;



import javax.annotation.Resource;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import javax.jws.soap.SOAPBinding.Style;

import java.util.Collection;
import java.util.List;
import java.util.Map;



import javax.annotation.Resource;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import javax.jws.soap.SOAPBinding.Style;

import java.util.List;
import java.util.Map;

/**
 * 微信对接webservice接口-json格式的出入参数
 *
 * @author yangxue
 */
@WebService
@SOAPBinding(style = Style.RPC)



public class NHISWXWebServiceImpl implements INHISWXWebService {
    @Resource
    private BdPubForWsService bdPubForWsService;


    @Resource
    private BlPubForWsService blPubForWsService;

    @Resource
    private PvPubForWsService pvPubForWsService;

	@Resource
	private SchPubForWsService schPubForWsService;

	/**
	* 查询系统所有启用状态的机构信息
	* @author leiminjian
	* @param 无入参
	* @return 
	*/
	@Override
	public String getOrgInfo() {
		List<Map<String,Object>> orglist = bdPubForWsService.getOrgInfo(null);
		return CommonUtils.getString(new RespJson("0|成功|" + ApplicationUtils.objectToJson(orglist), true));
	}



    /**
	* 查询科室信息
	* @author leiminjian
	* @param paramMap: pkOrg:机构唯一标识 ; codeOrg：机构编码
	* @return
	*/
	@Override
	public String getDeptInfo(String param) {
		Map<String,Object> paramMap = JsonUtil.readValue(param, Map.class);
		if (paramMap == null)
			return  CommonUtils.getString(new RespJson("-1|未获取到参数", true));
		if ((paramMap.get("pkOrg")==null||"".equals(CommonUtils.getString(paramMap.get("pkOrg"))))
				&& (paramMap.get("codeOrg")==null||"".equals(CommonUtils.getString(paramMap.get("codeOrg")))) )
			return CommonUtils.getString(new RespJson("-1|未获取到机构唯一标识或机构编码", true));
		List<Map<String,Object>> deptlist = bdPubForWsService.getDeptInfo(paramMap);
		return CommonUtils.getString(new RespJson("0|成功|" + ApplicationUtils.objectToJson(deptlist), true));
	}
	/**
	* 查询医生信息
	* @author leiminjian
	* @param paramMap: pkDept：科室唯一标识 ; codeDept：科室编码(二选一)
	* @return
	*/
	@Override
	public String getEmpInfo(String param) {
		Map<String,Object> paramMap = JsonUtil.readValue(param, Map.class);
		if (paramMap == null)
			return  CommonUtils.getString(new RespJson("-1|未获取到参数", true));
		if ((paramMap.get("pkDept")==null||"".equals(CommonUtils.getString(paramMap.get("pkDept"))))
				&& (paramMap.get("codeDept")==null||"".equals(CommonUtils.getString(paramMap.get("codeDept")))) )
			return CommonUtils.getString(new RespJson("-1|未获取到科室唯一标识或科室编码", true));
		List<Map<String,Object>> employeelist = bdPubForWsService.getEmpInfo(paramMap);
		return CommonUtils.getString(new RespJson("0|成功|" + ApplicationUtils.objectToJson(employeelist), true));
	}
	/**
	* 根据患者标识查询患者详细信息
	* @author leiminjian
	* @param paramMap: pkPi:患者唯一标识 ;  codePi:患者编码 ; idno:身份证号;  codeIp:住院号 ; codeCard:就诊卡号 
	* @return
	*/
	@Override
	public String getPiMasterInfo(String param) {
		Map<String,Object> paramMap = JsonUtil.readValue(param, Map.class);
		if ( paramMap == null)
			return CommonUtils.getString(new RespJson("-1|未获取到患者标识参数", true));
		if( (paramMap.get("pkPi")==null||"".equals(CommonUtils.getString(paramMap.get("pkPi")))) 
				&& (paramMap.get("codePi")==null||"".equals(CommonUtils.getString(paramMap.get("codePi")))) 
				&& (paramMap.get("idno")==null||"".equals(CommonUtils.getString(paramMap.get("idno")))) 
				&& (paramMap.get("codeIp")==null||"".equals(CommonUtils.getString(paramMap.get("codeIp"))))
				&& (paramMap.get("codeCard")==null||"".equals(CommonUtils.getString(paramMap.get("codeCard")))))
		    return  CommonUtils.getString(new RespJson("-1|未获取到有效的患者标识参数", true));
		Map<String,Object> piMaster=pvPubForWsService.getPiMasterInfo(paramMap);
		return  CommonUtils.getString(new RespJson("0|成功|" +  ApplicationUtils.objectToJson(piMaster), true));
	}
	/**
	 * 查询患者分类信息(无入参)
	 * @author kongxuedong
	 * @return java.lang.String
	 */
	@Override
	public String getPiCateInfo() {
		List<Map<String,Object>> picatelist = pvPubForWsService.getPiCateInfo();
		if(picatelist==null){
			return CommonUtils.getString(new RespJson("-1|未查询到患者分类信息|", true));
		}
		return CommonUtils.getString(new RespJson("0|成功|" + ApplicationUtils.objectToJson(picatelist), true));
	}
	/**
	 * 保存患者信息
	 * @param paramMap{namePi：必传，idNo：必传，mobile：必传，pkOrg：必传，dtSex}
	 * @return
	 */
	@Override
	public String savePiMaster(String param) {
		Map<String, Object> paramMap = JsonUtil.readValue(param,Map.class);
		if(paramMap == null)
			return  CommonUtils.getString(new RespJson("-1|未获取到保存患者信息的参数", true));
		if(paramMap.get("namePi")==null||"".equals(CommonUtils.getString(paramMap.get("namePi"))))
		    return  CommonUtils.getString(new RespJson("-1|未获取到保存患者信息的参数namePi", true));
		if(paramMap.get("idNo")==null||"".equals(CommonUtils.getString(paramMap.get("idNo"))))
			return  CommonUtils.getString(new RespJson("-1|未获取到保存患者信息的参数idNo", true));
		if(paramMap.get("mobile")==null||"".equals(CommonUtils.getString(paramMap.get("mobile"))))
			return  CommonUtils.getString(new RespJson("-1|未获取到保存患者信息的参数mobile", true));
		if(paramMap.get("pkPicate")==null||"".equals(CommonUtils.getString(paramMap.get("pkPicate"))))
			return  CommonUtils.getString(new RespJson("-1|未获取到保存患者信息的参数pkPicate", true));
		if(paramMap.get("pkOrg")==null||"".equals(CommonUtils.getString(paramMap.get("pkOrg"))))
			return  CommonUtils.getString(new RespJson("-1|未获取到保存患者信息的参数pkOrg", true));
		Map<String,Object> result = pvPubForWsService.savePiMaster(paramMap);
		if(result==null)
			return CommonUtils.getString(new RespJson("-1|保存患者信息失败", true));
		if("false".equals(result.get("result")))
		    return CommonUtils.getString(new RespJson("-1|"+result.get("message"), true));
		    
		return CommonUtils.getString(new RespJson("0|成功|"+ ApplicationUtils.objectToJson(result), true));
	}
	/**
	 * 查询费用分类(无入参)
	 * @author kongxuedong
	 * @return java.lang.String
	 */
	@Override
	public String getItemCateInfo() {
		List<Map<String,Object>> itemcatelist = bdPubForWsService.getItemCateInfo(null);
		if(itemcatelist==null){
			return CommonUtils.getString(new RespJson("-1|未查询到费用分类信息|", true));
		}
		return CommonUtils.getString(new RespJson("0|成功|" + ApplicationUtils.objectToJson(itemcatelist), true));
	}

    /**
	 * 查询预约排班信息
	 * @param paramMap{pkOrg：必传}
	 * @return
	 */
	@Override
	public String getAppSchInfo(String param) {
		Map<String, Object> paramMap = JsonUtil.readValue(param,Map.class);
		if(paramMap == null||paramMap.get("pkOrg")==null||"".equals(CommonUtils.getString(paramMap.get("pkOrg"))))
		  return  CommonUtils.getString(new RespJson("-1|未获取到查询预约排班信息的参数", true));
		List<SchForExtVo> schlist = schPubForWsService.getAppSchInfo(paramMap);
		return CommonUtils.getString(new RespJson("0|成功|" + ApplicationUtils.objectToJson(schlist), true));
	}
	/**
	 * 查询预约号源信息
	 * @param param{pkSch：必传}
	 * @return
	 */
	@Override
	public String getTickets(String param) {
		Map<String, Object> paramMap = JsonUtil.readValue(param,Map.class);
		if(paramMap == null||paramMap.get("pkSch")==null||"".equals(CommonUtils.getString(paramMap.get("pkSch"))))
		  return  CommonUtils.getString(new RespJson("-1|未获取到查询预约号源的参数pkSch", true));
		Map<String,Object> ticket = schPubForWsService.getTickets(paramMap);
		return CommonUtils.getString(new RespJson("0|成功|" + ApplicationUtils.objectToJson(ticket), true));
	}
	/**
	 * 保存预约挂号信息--参数全部必传
	 * @param param{pkPi,pkSch,ticketNo}
	 * @return
	 */
	@Override
	public String saveAppointment(String param) {
		Map<String, Object> paramMap = JsonUtil.readValue(param,Map.class);
		if(paramMap == null)
			return  CommonUtils.getString(new RespJson("-1|未获取到保存预约挂号信息的参数", true));
		if(paramMap.get("pkPi")==null||"".equals(CommonUtils.getString(paramMap.get("pkPi"))))
		    return  CommonUtils.getString(new RespJson("-1|未获取到保存预约挂号信息的参数pkPi", true));
		if(paramMap.get("pkSch")==null||"".equals(CommonUtils.getString(paramMap.get("pkSch"))))
			return  CommonUtils.getString(new RespJson("-1|未获取到保存预约挂号信息的参数pkSch", true));
		if(paramMap.get("ticketNo")==null||"".equals(CommonUtils.getString(paramMap.get("ticketNo"))))
			return  CommonUtils.getString(new RespJson("-1|未获取到保存预约挂号信息的参数ticketNo", true));
		Map<String,Object> result = schPubForWsService.saveAppointment(paramMap);
		if(result==null)
			return CommonUtils.getString(new RespJson("-1|预约挂号失败", true));
		if("false".equals(result.get("result")))
		    return CommonUtils.getString(new RespJson("-1|"+result.get("message"), true));
		    
		return CommonUtils.getString(new RespJson("0|成功|"+ApplicationUtils.objectToJson(result), true));
	}
/**
 * 查询是否存在就诊卡
 * @param param{pkPi,dtCardtype}
 * @return
 * @author yuxiangyang
 */
	@Override
	public String getPiCard(String param) {
		Map<String,Object> piCard=JsonUtil.readValue(param,Map.class);
		if (piCard == null){
			return  CommonUtils.getString(new RespJson("-1|未获取到参数", true));
		}	
		if (piCard.get("pkPi")==null||"".equals(CommonUtils.getString(piCard.get("pkPi")))){
			return CommonUtils.getString(new RespJson("-1|未获取到患者唯一标识", true));
		}
		List<Map<String,Object>> picardlist = pvPubForWsService.getPiCard(piCard);
			return CommonUtils.getString(new RespJson("0|成功|" + ApplicationUtils.objectToJson(picardlist), true));
	}
/**
 *@author GuoJing
 *查询患者当前就诊状态的住院就诊信息
 *查询患者指定时间段住院记录
 *@param param{pkPi必传 euStatus:就诊状态  codeIp:住院号pkDept:就诊科室 dateBegin:开始日期 dateEnd:截止日期}
 *@return 
 */

	@Override
	public String getPvInfoByIp(String param) {
		// TODO Auto-generated method stub
		@SuppressWarnings("unchecked")
		Map<String, Object> paramMap = JsonUtil.readValue(param, Map.class);
		if(paramMap==null)
			return  CommonUtils.getString(new RespJson("-1|未能获取到患者当前就诊状态的住院就诊信息！", true));
		if(paramMap.get("pkPi")==null||"".equals(CommonUtils.getString(paramMap.get("pkPi"))))
			return  CommonUtils.getString(new RespJson("-1|未能获取到患者唯一标识pkPi!", true));		
		List<Map<String,Object>> pvInfoByIp = pvPubForWsService.getPvInfoByIp(paramMap);
		return CommonUtils.getString(new RespJson("0|成功|" +ApplicationUtils.objectToJson(pvInfoByIp),true));
	
	}

	/**
	 *@author GuoJing 
	 *查询当前患者就诊状态的门诊就诊信息
	 *@param param{pkPi必传 codeOp:门诊号 pkDept:就诊科室}
	 *@return 
	 */
		@Override
		public String getPvInfoByOp(String param) {
			// TODO Auto-generated method stub
			@SuppressWarnings("unchecked")
			Map<String, Object> paramMap = JsonUtil.readValue(param, Map.class);
			if(paramMap==null)
				return  CommonUtils.getString(new RespJson("-1|未能获取到前患者就诊状态的门诊就诊信息！", true));
			if(paramMap.get("pkPi")==null||"".equals(CommonUtils.getString(paramMap.get("pkPi"))))
				return  CommonUtils.getString(new RespJson("-1|未能获取到患者唯一标识pkPi!", true));	
			
			List<Map<String,Object>> pvInfoByOp = pvPubForWsService.getPvInfoByOp(paramMap);
			return CommonUtils.getString(new RespJson("0|成功|" +ApplicationUtils.objectToJson(pvInfoByOp),true));
			
		}


    /**
     *查询门诊待缴费用
     * @param param
     * @return
     *
     */
	@Override
	public String getUnpaidFeeByOp(String param) {
        Map<String, Object> paramMap = JsonUtil.readValue(param, Map.class);
        if (paramMap==null){
            return  CommonUtils.getString(new RespJson("-1|未能获取到当前患者信息！", true));
        }
        if (paramMap.get("pkPi")==null || "".equals(paramMap.get("pkPi"))){
            return  CommonUtils.getString(new RespJson("-1|未能获取到患者主键!", true));
        }
        if (paramMap.get("pkPv")==null || "".equals(paramMap.get("pkPi"))){
            return  CommonUtils.getString(new RespJson("-1|未能获取到患者主键!", true));
        }
        if (paramMap.get("pkOrg")==null || "".equals(paramMap.get("pkPi"))){
            return  CommonUtils.getString(new RespJson("-1|未能获取到患者主键!", true));
        }
        List<Map<String, Object>> unpaidFeeByOp = blPubForWsService.getUnpaidFeeByOp(paramMap);
		return  CommonUtils.getString(new RespJson("0|成功|" +ApplicationUtils.objectToJson(unpaidFeeByOp),true));
	}

    /**
     *查询门诊已缴费用
     *
     * @param param
     * @return
     */
	@Override
	public String getPaidFeeByOp(String param) {
		Map<String, Object> paramMap = JsonUtil.readValue(param, Map.class);
		if (paramMap==null){
			return  CommonUtils.getString(new RespJson("-1|未能获取到当前患者结算信息！", true));
		}
		if (paramMap.get("pkPi")==null || "".equals(paramMap.get("pkPi"))){
			return  CommonUtils.getString(new RespJson("-1|未能获取到患者主键!", true));
		}
		if(paramMap.get("pkOrg")==null||"".equals(CommonUtils.getString(paramMap.get("pkOrg")))){
			return  CommonUtils.getString(new RespJson("-1|未获取到保存患者信息的参数pkOrg", true));
		}
        List<Map<String,Object>> paidFeeByOp = blPubForWsService.getPaidFeeByOp(paramMap);
		return CommonUtils.getString(new RespJson("0|成功|" +paidFeeByOp,true));
	}

    /**
     * 查询门诊缴费明细
     * @param param
     * @return
     */
	@Override
	public String getPayDetailByOp(String param) {
        Map<String, Object> paramMap = JsonUtil.readValue(param, Map.class);
        if (paramMap==null){
            return  CommonUtils.getString(new RespJson("-1|未能获取到当前患者结算信息！", true));
        }
        if (paramMap.get("pkSettle")==null || "".equals(paramMap.get("pkSettle"))){
            return  CommonUtils.getString(new RespJson("-1|未能获取到患者结算主键!", true));
        }
        List<Map<String,Object>> payDetailByOp = blPubForWsService.getPayDetailByOp(paramMap);
		return CommonUtils.getString(new RespJson("0|成功|" +payDetailByOp,true));
	}

	@Override
	public String cardRecharge(String param) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String prePayRecharge(String param) {
		// TODO Auto-generated method stub
		return null;
	}
    /**
     *功能描述：查询住院预交金充值记录
     * @author wuqiang
     * @date 2018/9/11
     * @param param pkPi:患者唯一标识
     * @return java.lang.String
     */
	@Override
	public String getPrePayDetail(String param) {
		Map<String, Object> paramMap = JsonUtil.readValue(param, Map.class);
		String listToString = null;
		if (paramMap.size() > 0) {
			List<Map<String, Object>> list = blPubForWsService.getPrePayDetail(paramMap);
			listToString = CommonUtils.getString(new RespJson("0|成功|" + ApplicationUtils.objectToJson(list), true));
		} else {
			listToString = new RespJson("-1|失败|", true) + "请传入查询参数";
		}
		return listToString;
	}
    /**
     *功能描述：查询患者交易明细
     * @author wuqiang
     * @date 2018/9/11
     * @param param pkPi:患者唯一标识
     * @return java.lang.String
     */
	@Override
	public String getDepositInfo(String param) {
		Map<String, Object> paramMap = JsonUtil.readValue(param, Map.class);
		String listToString = null;
		if (paramMap.size() > 0) {
			List<Map<String, Object>> list = blPubForWsService.getDepositInfo(paramMap);
			listToString = CommonUtils.getString(new RespJson("0|成功|" + ApplicationUtils.objectToJson(list), true));

		} else {
			listToString = new RespJson("-1|失败|", true) + "请传入查询参数";
		}
		return listToString;
	}
	/**
	 * 取消预约挂号
	 * @param {pkSchappt：必传}
	 * @return
	 */
	@Override
	public String cancelAppointment(String param) {
		Map<String, Object> paramMap = JsonUtil.readValue(param,Map.class);
		if(paramMap == null)
			return  CommonUtils.getString(new RespJson("-1|未获取到取消预约挂号信息的参数", true));
		if(paramMap.get("pkSchappt")==null||"".equals(CommonUtils.getString(paramMap.get("pkSchappt"))))
		    return  CommonUtils.getString(new RespJson("-1|未获取到取消预约挂号信息的参数pkSchappt", true));
		
		Map<String,Object> result = schPubForWsService.cancelAppointment(paramMap);
		if(result==null)
			return CommonUtils.getString(new RespJson("-1|取消预约挂号失败", true));
		if("false".equals(result.get("result")))
		    return CommonUtils.getString(new RespJson("-1|"+result.get("message"), true));
		    
		return CommonUtils.getString(new RespJson("0|成功|"+ApplicationUtils.objectToJson(result), true));
		
	}
    /**
     *功能描述：查询患者当前住院信息费用清单
     * @author wuqiang
     * @date 2018/9/11
     * @param param pkPi:患者唯一标识
     * @return java.lang.String
     */
	@Override
	public String getIpCgDetail(String param) {
		Map<String, Object> paramMap = JsonUtil.readValue(param, Map.class);
		String listToString = null;
		if (paramMap.size() > 0) {
			List<Map<String, Object>> list = blPubForWsService.getIpCgDetail(paramMap);
			listToString = CommonUtils.getString(new RespJson("0|成功|" + ApplicationUtils.objectToJson(list), true));

		} else {
			listToString = new RespJson("-1|失败|", true) + "请传入查询参数";
		}
		return listToString;
	}

	/**
	 * 查询内部医保信息(无入参)
	 * @author kongxuedong
	 * @return java.lang.String
	 */
	@Override
	public String getHpInfo() {
		Map<String,Object> paramMap = new HashMap<String,Object>();
		paramMap.put("flagOp", "1");
		List<String> hptype = new ArrayList<String>();
		hptype.add("0");
		hptype.add("4");
		hptype.add("9");
		paramMap.put("euHptypes", hptype);
		List<Map<String,Object>> hpinfolist = bdPubForWsService.getHpInfo(paramMap);
		if(hpinfolist==null){
			return CommonUtils.getString(new RespJson("-1|未查询到内部医保信息|", true));
		}
		return CommonUtils.getString(new RespJson("0|成功|" + ApplicationUtils.objectToJson(hpinfolist), true));
	}
	/**
	 * 查询当日排班信息
	 * @param  {pkOrg：必传}
	 * @return
	 */
	@Override
	public String getSchInfo(String param) {
		Map<String, Object> paramMap = JsonUtil.readValue(param,Map.class);
		if(paramMap == null)
			return  CommonUtils.getString(new RespJson("-1|未获取到查询挂号信息的参数", true));
		if(paramMap.get("pkOrg")==null||"".equals(CommonUtils.getString(paramMap.get("pkOrg"))))
		    return  CommonUtils.getString(new RespJson("-1|未获取到查询挂号信息的参数pkOrg", true));
		
		List<Map<String,Object>> result = schPubForWsService.getSchInfo(CommonUtils.getString(paramMap.get("pkOrg")));
		return CommonUtils.getString(new RespJson("0|成功|"+ApplicationUtils.objectToJson(result), true));
	}
	/**
	****门诊挂号结算
		pkInsu：主医保唯一标识
		pkPi：患者唯一标识
		pkPicate：患者分类
		pkRes：挂号资源唯一标识
		pkSch：排班唯一标识
		pkSchsrv：排班服务唯一标识
		pkOrg：机构唯一标识
		accountReceivable：应收金额
		cardNo：就诊卡号（非必填，其他必填）
	 */
	@Override
	public String saveRegister(String param) {
		Map<String, Object> paramMap = JsonUtil.readValue(param,Map.class);
		if(paramMap == null)
			return  CommonUtils.getString(new RespJson("-1|未获取到挂号所需的参数", true));
		if(paramMap.get("pkOrg")==null||"".equals(CommonUtils.getString(paramMap.get("pkOrg"))))
		    return  CommonUtils.getString(new RespJson("-1|未获取到挂号所需的参数pkOrg", true));
		if(paramMap.get("pkInsu")==null||"".equals(CommonUtils.getString(paramMap.get("pkInsu"))))
		    return  CommonUtils.getString(new RespJson("-1|未获取到挂号所需的参数pkInsu", true));
		if(paramMap.get("pkPi")==null||"".equals(CommonUtils.getString(paramMap.get("pkPi"))))
		    return  CommonUtils.getString(new RespJson("-1|未获取到挂号所需的参数pkPi", true));
		if(paramMap.get("pkPicate")==null||"".equals(CommonUtils.getString(paramMap.get("pkPicate"))))
		    return  CommonUtils.getString(new RespJson("-1|未获取到挂号所需的参数pkPicate", true));
		if(paramMap.get("pkRes")==null||"".equals(CommonUtils.getString(paramMap.get("pkRes"))))
		    return  CommonUtils.getString(new RespJson("-1|未获取到挂号所需的参数pkRes", true));
		if(paramMap.get("pkSch")==null||"".equals(CommonUtils.getString(paramMap.get("pkSch"))))
		    return  CommonUtils.getString(new RespJson("-1|未获取到挂号所需的参数pkSch", true));
		if(paramMap.get("pkSchsrv")==null||"".equals(CommonUtils.getString(paramMap.get("pkSchsrv"))))
		    return  CommonUtils.getString(new RespJson("-1|未获取到挂号所需的参数pkSchsrv", true));
		if(paramMap.get("accountReceivable")==null||"".equals(CommonUtils.getString(paramMap.get("accountReceivable"))))
		    return  CommonUtils.getString(new RespJson("-1|未获取到挂号所需的参数accountReceivable", true));
		List<Map<String,Object>> result = pvPubForWsService.saveRegister(paramMap);
		return CommonUtils.getString(new RespJson("0|成功|"+ApplicationUtils.objectToJson(result), true));
	}
    /**
     *功能描述：查询患者住院一日费用清单
     * @author wuqiang
     * @date 2018/9/11
     * @param param  pkPi:患者唯一标识，dateCg:费用日期
     * @return java.lang.String
     */
    @Override
    public String getIpCgDayDetail(String param) {
        Map<String, Object> paramMap = JsonUtil.readValue(param, Map.class);
        String listToString = null;
		if (paramMap.get("pkPi")!=null && paramMap.get("dateCg")!=null) {
			List<Map<String, Object>> list = blPubForWsService.getIpCgDayDetail(paramMap);
				listToString = CommonUtils.getString(new RespJson("0|成功|" + ApplicationUtils.objectToJson(list), true));

        } else {
            listToString = new RespJson("-1|失败|", true) + "请传入查询参数";
        }
        return listToString;
    }

	/**
	****门诊挂号预结算
		pkInsu：主医保唯一标识
		pkPi：患者唯一标识
		pkPicate：患者分类
		pkRes：挂号资源唯一标识
		pkSch：排班唯一标识
		pkSchsrv：排班服务唯一标识
		pkOrg：机构唯一标识
		cardNo：就诊卡号（非必填，其他必填）
	 */
	@Override
	public String preRegister(String param) {
		Map<String, Object> paramMap = JsonUtil.readValue(param,Map.class);
		if(paramMap == null)
			return  CommonUtils.getString(new RespJson("-1|未获取到挂号所需的参数", true));
		if(paramMap.get("pkOrg")==null||"".equals(CommonUtils.getString(paramMap.get("pkOrg"))))
		    return  CommonUtils.getString(new RespJson("-1|未获取到挂号所需的参数pkOrg", true));
		if(paramMap.get("pkInsu")==null||"".equals(CommonUtils.getString(paramMap.get("pkInsu"))))
		    return  CommonUtils.getString(new RespJson("-1|未获取到挂号所需的参数pkInsu", true));
		if(paramMap.get("pkPi")==null||"".equals(CommonUtils.getString(paramMap.get("pkPi"))))
		    return  CommonUtils.getString(new RespJson("-1|未获取到挂号所需的参数pkPi", true));
		if(paramMap.get("pkPicate")==null||"".equals(CommonUtils.getString(paramMap.get("pkPicate"))))
		    return  CommonUtils.getString(new RespJson("-1|未获取到挂号所需的参数pkPicate", true));
		if(paramMap.get("pkRes")==null||"".equals(CommonUtils.getString(paramMap.get("pkRes"))))
		    return  CommonUtils.getString(new RespJson("-1|未获取到挂号所需的参数pkRes", true));
		if(paramMap.get("pkSch")==null||"".equals(CommonUtils.getString(paramMap.get("pkSch"))))
		    return  CommonUtils.getString(new RespJson("-1|未获取到挂号所需的参数pkSch", true));
		if(paramMap.get("pkSchsrv")==null||"".equals(CommonUtils.getString(paramMap.get("pkSchsrv"))))
		    return  CommonUtils.getString(new RespJson("-1|未获取到挂号所需的参数pkSchsrv", true));
		Map<String,Object> result = pvPubForWsService.preRegister(paramMap);
		return CommonUtils.getString(new RespJson("0|成功|"+ApplicationUtils.objectToJson(result), true));
	}



	@Override
	public String preRtnRegister(String param) {
		// TODO Auto-generated method stub
		return null;
	}



	@Override
	public String saveRtnRegister(String param) {
		// TODO Auto-generated method stub
		return null;
	}


}
