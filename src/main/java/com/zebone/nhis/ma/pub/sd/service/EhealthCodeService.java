package com.zebone.nhis.ma.pub.sd.service;

import java.math.BigDecimal;
import java.text.ParseException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.zebone.nhis.common.module.pi.PiMaster;
import com.zebone.nhis.common.support.ApplicationUtils;
import com.zebone.nhis.common.support.CommonUtils;
import com.zebone.nhis.common.support.DateUtils;
import com.zebone.nhis.common.support.HttpClientUtil;
import com.zebone.nhis.ma.pub.sd.vo.health.EhealthCodeEHC01ReqVO;
import com.zebone.nhis.ma.pub.sd.vo.health.EhealthCodeEHC03ReqVO;
import com.zebone.nhis.ma.pub.sd.vo.health.EhealthCodeEHC05ReqVO;
import com.zebone.nhis.ma.pub.sd.vo.health.EhealthCodePM020ReqVO;
import com.zebone.nhis.ma.pub.sd.vo.health.EhealthCodePM020ResVO;
import com.zebone.nhis.ma.pub.sd.vo.health.EhealthCodePatiVO;
import com.zebone.nhis.ma.pub.sd.vo.health.EhealthCodeResVO;
import com.zebone.platform.common.support.User;
import com.zebone.platform.framework.support.IUser;
import com.zebone.platform.modules.dao.DataBaseHelper;
import com.zebone.platform.modules.exception.BusException;
import com.zebone.platform.modules.utils.JsonUtil;

/**
 * 电子健康码接口调用服务
 * @author zhangtao
 *
 */
@Service
public class EhealthCodeService {
	
	private Logger healthLog = LoggerFactory.getLogger("nhis.EHealthLog");
	
	// http地址
	private static String url = ApplicationUtils.getPropertyValue("health.url", "");
	

	/**
	 * 【电子健康卡】注册接口   EHC01
	 * @param args
	 * @return
	 */
	public Map eHealthCodeEHC01(Object[] args) {
		healthLog.info("--------------------------------电子健康卡接口开始--------------------------------");
		Map<String,Object> paramMap = (Map<String,Object>)args[0];
		//入参
		PiMaster piVo = (PiMaster) paramMap.get("piMaster");
		EhealthCodeEHC01ReqVO reqVo = new EhealthCodeEHC01ReqVO();
		reqVo.setIdNo(piVo.getIdNo());
		
		reqVo.setName(piVo.getNamePi());
		
		//获取证件类型对照信息
		String idType = getCollationInfo("noHis","IDTYPE",piVo.getDtIdtype());
        reqVo.setIdType(CommonUtils.isEmptyString(idType) ? "99":idType);
        
		//获取性别对照信息
		String dtSex = getCollationInfo("noHis","DTSEX",piVo.getDtSex());
        reqVo.setGender(CommonUtils.isEmptyString(dtSex) ? "0":dtSex);
        
        //获取民族对照信息
		String nation = getCollationInfo("noHis","NATION",piVo.getDtNation());
		reqVo.setNation(CommonUtils.isEmptyString(nation) ? "97":nation);
	
		reqVo.setBirthday(DateUtils.dateToStr("yyyyMMdd", piVo.getBirthDate()));
		reqVo.setMobile(piVo.getMobile());
		reqVo.setTelephone(piVo.getTelNo());
		reqVo.setAddress(piVo.getAddrCurDt());
		reqVo.setUnit(piVo.getUnitWork());		

		//将Data内容转换为json格式
		String dataJson = JsonUtil.writeValueAsString(reqVo);
		
		//发送请求
		EhealthCodeResVO resVo = healthRts(dataJson,"EHC01");

		if(!"0".equals(resVo.getCode())){//失败
			throw new BusException("电子健康码平台："+resVo.getMsg());
		}
		String reqData = JsonUtil.writeValueAsString(resVo.getData());
		//获取返回参数
		EhealthCodePatiVO dataVo = JsonUtil.readValue(reqData, EhealthCodePatiVO.class);

		Map<String,Object> map = new HashMap<>();
		map.put("hicNo",dataVo.getHealthCardQrcodeData());
		map.put("note","");
		//String res = dataVo.getHealthCardQrcodeData();
		
		healthLog.info("--------------------------------电子健康卡接口开始--------------------------------");
		return map;
	}
	
	/**
	 * 【电子健康卡】查询接口  EHC03
	 * @param args
	 * @return
	 */
	public Map<String,Object> eHealthCodeEHC03(Object[] args) {
		healthLog.info("--------------------------------电子健康卡接口开始--------------------------------");
		Map<String,Object> paramMap = (Map<String,Object>)args[0];
		//入参
		EhealthCodeEHC03ReqVO reqVo = new EhealthCodeEHC03ReqVO();
		reqVo.setQueryType("1");
		reqVo.setIdNo(CommonUtils.getString(paramMap.get("idNo")));
		reqVo.setIdType(CommonUtils.getString(paramMap.get("dtIdtype")));
		//获取证件类型对照信息
		String idType = getCollationInfo("noHis","IDTYPE",CommonUtils.getString(paramMap.get("dtIdtype")));
		reqVo.setIdType(CommonUtils.isEmptyString(idType) ? "99":idType);
		
		//将Data内容转换为json格式
		String dataJson = JsonUtil.writeValueAsString(reqVo);
		
		//发送请求
		EhealthCodeResVO resVo = healthRts(dataJson,"EHC03");

		if(!"0".equals(resVo.getCode())){//失败
			throw new BusException("电子健康码平台："+resVo.getMsg());
		}
		String reqData = JsonUtil.writeValueAsString(resVo.getData());
		//获取返回参数
		EhealthCodePatiVO dataVo = JsonUtil.readValue(reqData, EhealthCodePatiVO.class);
		
		//组装患者信息
		PiMaster pi = new PiMaster();
		patiTransformation(pi,dataVo);
		
		Map<String,Object> res = new HashMap<>(16);
		res.put("piMster",pi);
		res.put("hicNo", dataVo.getHealthCardQrcodeData());
		
		healthLog.info("--------------------------------电子健康卡接口开始--------------------------------");
		return res;
	}
	
	/**
	 * 【电子健康卡】二维码验证查询接口   EHC05
	 * @param args
	 * @return
	 */
	public Map<String,Object> eHealthCodeEHC05(Object[] args){
		healthLog.info("--------------------------------电子健康卡接口开始--------------------------------");
		Map<String,Object> paramMap = (Map<String,Object>)args[0];
		String hicNo = CommonUtils.getString(paramMap.get("hicNo"));
		if(CommonUtils.isEmptyString(hicNo)){
			throw new BusException("电子健康码平台：hicNo健康码不能为空");
		}
		//入参
		EhealthCodeEHC05ReqVO reqVo = new EhealthCodeEHC05ReqVO();
		reqVo.setHealthCardQrcodeData(hicNo);
		reqVo.setLimitCodeType("1");
		//将Data内容转换为json格式
		String dataJson = JsonUtil.writeValueAsString(reqVo);
		
		//发送请求
		EhealthCodeResVO resVo = healthRts(dataJson,"EHC05");

		if(!"0".equals(resVo.getCode())){//失败
			throw new BusException("电子健康码平台："+resVo.getMsg());
		}
		String reqData = JsonUtil.writeValueAsString(resVo.getData());
		//获取返回参数
		//List<EhealthCodePatiVO> dataVo = JsonUtil.readValue(reqData, new TypeReference<List<EhealthCodePatiVO>>(){});
		EhealthCodePatiVO dataVo = JsonUtil.readValue(reqData, EhealthCodePatiVO.class);
		
		//组装患者实体信息
		PiMaster pi = new PiMaster();
		patiTransformation(pi,dataVo);		
	
		Map<String,Object> res = new HashMap<>(16);
		res.put("piMaster",pi);
		
		healthLog.info("--------------------------------电子健康卡接口开始--------------------------------");
		return res;
	}
	
	/**
	 * 【电子健康卡】发起退费通知接口   PM020
	 * @param args
	 * @return
	 */
	public Map<String,Object> eHealthCodePM020(Object[] args){		
		healthLog.info("--------------------------------电子健康卡接口开始--------------------------------");
		Map<String,Object> paramMap = (Map<String,Object>)args[0];
		IUser user =(IUser)args[1];
		User u = (User) user;
		EhealthCodePM020ReqVO reqVo = new EhealthCodePM020ReqVO();
		reqVo.setOrgUuid(ApplicationUtils.getPropertyValue("health.orgid", ""));
		reqVo.setPatientId(CommonUtils.getPropValueStr(paramMap, "codePi"));
		//reqVo.setReceiptId(CommonUtils.getPropValueStr(paramMap, "ebillno"));
		reqVo.setRefundId(CommonUtils.getPropValueStr(paramMap, "pkDepo"));
		reqVo.setPayOrderSerial(CommonUtils.getPropValueStr(paramMap, "serialNo"));
		reqVo.setPaySerial(CommonUtils.getPropValueStr(paramMap, "tradeNo"));
		BigDecimal bd = new BigDecimal("100");
		reqVo.setRefundCost(new BigDecimal(CommonUtils.getPropValueStr(paramMap, "amountSt") == "" ? 0 : Double.valueOf(CommonUtils.getPropValueStr(paramMap, "amountSt"))).multiply(bd).setScale(0, BigDecimal.ROUND_HALF_UP));
		reqVo.setCashAmount(new BigDecimal(CommonUtils.getPropValueStr(paramMap, "amountPi") == "" ? 0 : Double.valueOf(CommonUtils.getPropValueStr(paramMap, "amountPi"))).multiply(bd).setScale(0, BigDecimal.ROUND_HALF_UP));
		reqVo.setInsurAmount(new BigDecimal(CommonUtils.getPropValueStr(paramMap, "amountInsu") == "" ? 0 : Double.valueOf(CommonUtils.getPropValueStr(paramMap, "amountInsu"))).multiply(bd).setScale(0, BigDecimal.ROUND_HALF_UP));
		reqVo.setRefundTime(DateUtils.dateToStr("yyyy-MM-dd HH:mm:ss", new Date()));
		reqVo.setRefundType(CommonUtils.getPropValueStr(paramMap, "type"));//1 挂号退费,2 门诊退费,3住院押金退费
		
		//将Data内容转换为json格式
		String dataJson = JsonUtil.writeValueAsString(reqVo);
		
		//发送请求
		EhealthCodeResVO resVo = healthRts(dataJson,"PM020");
		
		if(!"0".equals(resVo.getCode())){//失败
			throw new BusException("电子健康码平台："+resVo.getMsg());
		}
		String reqData = JsonUtil.writeValueAsString(resVo.getData());
		//获取返回参数
		EhealthCodePM020ResVO dataVo = JsonUtil.readValue(reqData, EhealthCodePM020ResVO.class);
		
		Map<String,Object> res = new HashMap<>(16);
		res.put("refundNo",dataVo.getOrderSerial());//原始订单号
		res.put("serialNo",dataVo.getRefOrderSerial());//订单号

		healthLog.info("--------------------------------电子健康卡接口开始--------------------------------");
		return res;
	
	}

	/**
	 * 组织主参数，调用http服务
	 * @param dataJson  数据参数
	 * @param version	服务版本
	 * @param serviceName	服务名称	
	 */
	private EhealthCodeResVO healthRts(String dataJson,String serviceName){
		healthLog.info("--------------------------------调用HTTP服务开始--------------------------------");
		/**调用服务接口*/
		StringBuffer urlStr = new StringBuffer(String.format("%s%s", url,serviceName));
		healthLog.info("调用服务地址{}",urlStr.toString());
		healthLog.info("入参{}",dataJson);
		String resJson = HttpClientUtil.sendHttpPostJson(urlStr.toString(), dataJson);
		healthLog.info("出参结果：{}",resJson);
		//解析响应参数
		EhealthCodeResVO healthResVo = JsonUtil.readValue(resJson, EhealthCodeResVO.class);
		
		healthLog.info("出参转换出的Message：{}",resJson);
		healthLog.info("--------------------------------调用HTTP服务结束--------------------------------");
		return healthResVo;
	}
	
	/**
	 * 转换为his患者对象信息
	 * @return
	 */
	private PiMaster patiTransformation(PiMaster pi,EhealthCodePatiVO dataVo){
		pi.setMobile(dataVo.getMobile());// 手机号
		pi.setAddrCurDt(dataVo.getAddress());// 地址
		pi.setNamePi(dataVo.getName());// 患者名称
		pi.setTelNo(dataVo.getTelephone());//电话号码
		pi.setUnitWork(dataVo.getUnit());//工作地址
		pi.setHicNo(dataVo.getHealthCardQrcodeData());//健康卡二维码内容
		pi.setIdNo(dataVo.getIdNo());//证件号
		
		//获取证件类型对照信息
		String idType = getCollationInfo("his","IDTYPE",dataVo.getIdType());
		pi.setDtIdtype(CommonUtils.isEmptyString(idType) ? "99":idType);
		        
		//获取性别对照信息
		String dtSex = getCollationInfo("his","DTSEX",dataVo.getGender());
		pi.setDtSex(CommonUtils.isEmptyString(dtSex) ? "04":dtSex);
		        
        //获取民族对照信息
		String nation = getCollationInfo("his","NATION",dataVo.getNation());
		pi.setDtNation(CommonUtils.isEmptyString(nation) ? "-1":nation);

		try {
			if(!CommonUtils.isEmptyString(dataVo.getBirthday()))
			{
				pi.setBirthDate(DateUtils.parseDate(dataVo.getBirthday(),"yyyyMMdd"));// 出生日期
			}
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return pi;
	}	
	
	/**
	 * 获取对照信息
	 * @param factory  要获取的哪一方的编码
	 * @param codeType 对照的类型 
	 * @param code 编码
	 * @return
	 */
	private String getCollationInfo(String factory,String codeType,String code){
		String resCode = ""; 
		if(CommonUtils.isEmptyString(code)){
			return resCode;
		}
		//获取his方code
		if("his".equals(factory)){
			
			Map<String, Object> queryForMap = DataBaseHelper.queryForMap(
					"select code_his from INS_SZYB_DICTMAP where EU_HPDICTTYPE='03' and code_type = ?  "
					+ " and code_insur=? and flag_chd='1' and del_flag='0'",new Object[]{codeType,code});
			resCode = CommonUtils.getPropValueStr(queryForMap, "codeHis");
		}else{
			//获取第三方code
			Map<String, Object> queryForMap = DataBaseHelper.queryForMap("select code_insur from INS_SZYB_DICTMAP where EU_HPDICTTYPE='03' and code_type = ?  "
					+ " and code_his=? and flag_chd='1' and del_flag='0'",new Object[]{codeType,code});
			resCode = CommonUtils.getPropValueStr(queryForMap, "codeInsur");
		}
		return resCode;
	}
	
}
