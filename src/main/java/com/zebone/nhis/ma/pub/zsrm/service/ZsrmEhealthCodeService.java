package com.zebone.nhis.ma.pub.zsrm.service;

import com.zebone.nhis.common.module.base.bd.code.BdDefdoc;
import com.zebone.nhis.common.module.base.ou.BdOuDept;
import com.zebone.nhis.common.module.pi.PiMaster;
import com.zebone.nhis.common.support.ApplicationUtils;
import com.zebone.nhis.common.support.DateUtils;
import com.zebone.nhis.common.support.HttpClientUtil;
import com.zebone.nhis.common.support.XmlUtil;
import com.zebone.nhis.ma.pub.zsrm.vo.*;
import com.zebone.nhis.pro.zsba.common.support.MD5Util;
import com.zebone.platform.common.support.NHISUUID;
import com.zebone.platform.framework.support.IUser;
import com.zebone.platform.modules.dao.DataBaseHelper;
import com.zebone.platform.modules.exception.BusException;
import com.zebone.platform.modules.utils.JsonUtil;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.codehaus.jackson.type.TypeReference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * 电子健康码接口调用服务
 *
 */
@Service
public class ZsrmEhealthCodeService {
	
	private Logger healthLog = LoggerFactory.getLogger("nhis.EHealthLog");

    // TODO: 2020-12-22 $PreComputer_IP 需要医院提供
	// http地址
	private static String url = ApplicationUtils.getPropertyValue("health.url", "");
	//数字证书
	private static String digitalCode = ApplicationUtils.getPropertyValue("health.digitalCode", "");
    //商户号
	private static  String merId= ApplicationUtils.getPropertyValue("health.merId", "");

	/**
	 * 【电子健康卡】注册接口   EHC01
	 * @param args
	 * @return
	 */
	public Map eHealthCodeEHC01(Object[] args) {
		Map<String,Object> paramMap = (Map<String,Object>)args[0];
		PiMaster piVo = (PiMaster) paramMap.get("piMaster");
		PiVo piRegVo = new PiVo();
		String time = System.currentTimeMillis()+"";
		//商户码
		piRegVo.setHead(new Head(merId,"A1001",time));
		PiRegRegisterVo pi = new PiRegRegisterVo();
		if("2".equals(piVo.getDtCountry())){//中国
			pi.setDtCountry("01");
		}else if("350".equals(piVo.getDtCountry())||"377".equals(piVo.getDtCountry())){
			pi.setDtCountry("02");//澳门//香港
		}else{//海外
			pi.setDtCountry("04");
		}
		switch (piVo.getDtIdtype()) {
			case "01"://身份证
			case "03":
				pi.setDtIdtype("01");
				break;
			case "02":
				pi.setDtIdtype("04");
				break;
			case "06"://香港护照
			case "07"://澳门护照
			case "10"://海外护照
				pi.setDtIdtype("03");
				break;
			case "08"://台湾居民来往大陆通行证
				pi.setDtIdtype("07");
				break;
			default:
				break;
		}
		pi.setIdNo(piVo.getIdNo());
		pi.setApplyType("3");//申请方式 9 其他
		pi.setNamePi(piVo.getNamePi());
		pi.setBirth(piVo.getBirthDate()!=null? DateUtils.formatDate(piVo.getBirthDate(), "yyyyMMdd"):null);
		pi.setSex("02".equals(piVo.getDtSex()) ?"1":"2");
		BdDefdoc bdDefdoc = DataBaseHelper.queryForBean("select ba_code from bd_defdoc where code_defdoclist ='000003' and code = '"+piVo.getDtNation()+"'",BdDefdoc.class);
		if(bdDefdoc != null){
			pi.setNation(bdDefdoc.getBaCode());
		}
		pi.setAddress2(piVo.getAddrCurDt());
		pi.setPhone(piVo.getMobile());
		pi.setAppliDate(DateUtils.formatDate(new Date(), "yyyy-MM-dd hh:mm:ss"));
		piRegVo.setPiRegRegisterVo(pi);
		String resXml= XmlUtil.beanToXml(piRegVo,PiVo.class);
		//数字证书码  需要卡管理提供
		// TODO: 2020-12-22
		String sign =digitalCode+"A1001"+time+resXml;
		//sign = ZsrmMsgUtils.encodeByMD5(sign);
		sign = MD5Util.MD5Encode(sign,"UTF-8");
		Map<String,String> reqParam = new HashMap<>();
		reqParam.put("xml",resXml);
		reqParam.put("sign",sign);
		String ret = HttpClientUtil.sendHttpPost(url,reqParam);
		PiMasterRegVo resvo=(PiMasterRegVo) XmlUtil.XmlToBean(ret, PiMasterRegVo.class);
		Map<String,Object> map = new HashMap<>();
		String res = "";
		String note = "";
		if(resvo != null && resvo.getPiRegVo() != null&&StringUtils.isNotBlank(resvo.getPiRegVo().getHealthCardId())){
			res = resvo.getPiRegVo().getHealthCardId();
			note = resvo.getPiRegVo().getReason();
			map.put("icCardId",resvo.getPiRegVo().getIcCardId());
			map.put("eleHealthCarId",resvo.getPiRegVo().getEleHealthCarId());
		}
		if(resvo != null && resvo.getPiRegVo() != null){
			note = resvo.getPiRegVo().getReason();
		}
		if(resvo != null && resvo.getHead() != null &&StringUtils.isNotBlank(resvo.getHead().getErrMsg())){
            note += resvo.getHead().getErrMsg();
        }

		map.put("hicNo",res);
		map.put("note",note);
		return map;
	}

	/**
	 * 【电子健康卡】查询接口  EHC03
	 * @param args
	 * @return
	 */
	public Map eHealthCodeEHC03(Object[] args) {
		healthLog.info("--------------------------------电子健康卡接口开始--------------------------------");
		Map<String,Object> map = null;
		try {
			Map<String,Object> paramMap = (Map<String,Object>)args[0];
			PiMaster piVo = (PiMaster) paramMap.get("piMaster");
			PiMasterRegVo piMasterRegVo = new PiMasterRegVo();
			long time = System.currentTimeMillis();
			piMasterRegVo.setHead(new Head(merId,"A1005",time+""));
			PiRegVo piRegVo = new PiRegVo();
			if("2".equals(piVo.getDtCountry())){//中国
				piRegVo.setDtCountry("01");
			}else if("350".equals(piVo.getDtCountry())||"377".equals(piVo.getDtCountry())){
				piRegVo.setDtCountry("02");//澳门//香港
			}else{//海外
				piRegVo.setDtCountry("04");
			}

			switch (piVo.getDtIdtype()) {
				case "01"://身份证
				case "03":
					piRegVo.setDtIdtype("01");
					break;
				case "02":
					piRegVo.setDtIdtype("04");
					break;
				case "06"://香港护照
				case "07"://澳门护照
				case "10"://海外护照
					piRegVo.setDtIdtype("03");
					break;
				case "08"://台湾居民来往大陆通行证
					piRegVo.setDtIdtype("07");
					break;
				default:
					map = new HashMap<>();
					map.put("note","该证件类型不支持电子电子健康码注册！");
					break;
			}
			piRegVo.setIdNo(piVo.getIdNo());
			piMasterRegVo.setPiRegVo(piRegVo);
			String resXml= XmlUtil.beanToXml(piMasterRegVo,PiMasterRegVo.class);
			//数字证书码  需要卡管理提供
			// TODO: 2020-12-22
			String sign =digitalCode+"A1005"+time+""+resXml;
			sign = MD5Util.MD5Encode(sign,"UTF-8");
			Map<String,String> reqParam = new HashMap<>();
			reqParam.put("xml",resXml);
			reqParam.put("sign",sign);
			String ret = HttpClientUtil.sendHttpPost(url,reqParam);
			PiMasterRegVo resvo=(PiMasterRegVo) XmlUtil.XmlToBean(ret, PiMasterRegVo.class);
			//查询到健康码直接返回 要么去申请注册
			if(resvo != null && resvo.getPiRegVo() != null&& StringUtils.isNotBlank(resvo.getPiRegVo().getHealthCardId())){
				map = new HashMap<>();
				map.put("hicNo",resvo.getPiRegVo().getHealthCardId());
				map.put("note","");
				map.put("icCardId",resvo.getPiRegVo().getIcCardId());
				map.put("eleHealthCarId",resvo.getPiRegVo().getEleHealthCarId());
			}else{
				//注册
				map = eHealthCodeEHC01(args);
			}
		}catch (Exception e){
			healthLog.info("电子健康卡接口异常："+e.getMessage());
		}
		finally {
			healthLog.info("--------------------------------电子健康卡接口结束--------------------------------");
		}
		return map;
	}

   /**
    * @Description 获取患者电子健康二维码
    * @auther wuqiang
    * @Date 2021-03-27
    * @Param [args]
    * @return java.lang.Object
    */
	public Object eHealthCodeEHC1026(Object[] args) {
		healthLog.info("--------------------------------电子健康卡接口开始--------------------------------");
		Map<String,Object> map = null;
		try {
			Map<String,Object> paramMap = (Map<String,Object>)args[0];
			String ehealthCardId= MapUtils.getString(paramMap,"ehealthCardId");
			String codeType=MapUtils.getString(paramMap,"codeType","1");
			long time = System.currentTimeMillis();
			EcgHealthGetQrIn ecgHealthGetQrIn=new EcgHealthGetQrIn();
			ecgHealthGetQrIn.setHead(new Head(merId,"A1026",time+""));
			EcgHealthGetQrInVo ecgHealthGetQrInVo=new EcgHealthGetQrInVo();
			ecgHealthGetQrInVo.setCodeType(codeType);
			ecgHealthGetQrInVo.setEhealthCardId(ehealthCardId);
			ecgHealthGetQrIn.setEcgHealthGetQrInVo(ecgHealthGetQrInVo);
			String resXml= XmlUtil.beanToXml(ecgHealthGetQrIn,EcgHealthGetQrIn.class);
			String sign =digitalCode+"A1026"+time+""+resXml;
			sign = MD5Util.getMD5Code(sign);
			Map<String,String> reqParam = new HashMap<>();
			reqParam.put("xml",resXml);
			reqParam.put("sign",sign);
			String ret = HttpClientUtil.sendHttpPost(url,reqParam);
			EcgHealthGetQr	resvo=(EcgHealthGetQr) XmlUtil.XmlToBean(ret, EcgHealthGetQr.class);
            if (resvo!=null&& resvo.getEcgHealthGetQrVo()!=null){
				map = new HashMap<>();
				map.put("ehealthCode",resvo.getEcgHealthGetQrVo().getEhealthCode());
			}
		}catch (Exception e){
			healthLog.info("电子健康卡接口-获取电子二维码异常："+e.getMessage());
		}
		finally {
			healthLog.info("--------------------------------电子健康卡接口结束--------------------------------");
		}
		return map;
	}

	/**
	 * 【电子健康卡】验证电子健康卡二维码   A1027
	 * @param ehealthCode
	 * @return
	 */
	public Map eHealthCodeEHCA1027(String ehealthCode) {
		PiEhealthVo pi = new PiEhealthVo();
		String time = System.currentTimeMillis()+"";
		//商户码
		pi.setHead(new Head(merId,"A1027",time));
		PiParamVo piParam = new PiParamVo();
		piParam.setEhealthCode(ehealthCode);
		pi.setPiParamVo(piParam);
		String resXml= XmlUtil.beanToXml(pi,PiEhealthVo.class);
		//数字证书码  需要卡管理提供
		// TODO: 2020-12-22
		String sign =digitalCode+"A1027"+time+resXml;
		//sign = ZsrmMsgUtils.encodeByMD5(sign);
		sign = MD5Util.MD5Encode(sign,"UTF-8");
		Map<String,String> reqParam = new HashMap<>();
		reqParam.put("xml",resXml);
		reqParam.put("sign",sign);
		String ret = HttpClientUtil.sendHttpPost(url,reqParam);
		PiA1027Vo resvo=(PiA1027Vo) XmlUtil.XmlToBean(ret, PiA1027Vo.class);
		Map<String,Object> map = new HashMap<>();
		if(resvo != null && resvo.getPiA1027RegVo() != null&&StringUtils.isNotBlank(resvo.getPiA1027RegVo().getEhealthCardId())){
			map.put("icCardId",resvo.getPiA1027RegVo().getEhealthCardId());
		}
		return map;
	}

	/**
	 * 电子健康卡用卡数据上传
	 * @param param
	 * @param user
	 * @return
	 */
	public Map serviceLogA1028(String param,IUser user) {
		PiA1028Vo pi = new PiA1028Vo();
		String time = System.currentTimeMillis()+"";
		//商户码
		pi.setHead(new Head(merId,"A1028",time));
		List<UseCardDataVo> useCardDataList = new ArrayList<>();
		List<Map<String, Object>> paramMapList = JsonUtil.readValue(param, new TypeReference<List<Map<String, Object>>>() {
		});
		if(CollectionUtils.isEmpty(paramMapList)){
			throw new BusException("未传入上传数据！");
		}
		//一般会是一条，循环不影响
		for(Map<String, Object> map:paramMapList){
			PiA1028RegVo vo = new PiA1028RegVo();
			vo.setSeqNo(NHISUUID.getKeyId());
			if(StringUtils.isNotBlank(MapUtils.getString(map,"departmentType"))){
				BdOuDept dept = DataBaseHelper.queryForBean(
						"select dt_stdepttype from bd_ou_dept where code_dept = ?", BdOuDept.class,
						MapUtils.getString(map,"departmentType"));
				vo.setDepartmentType(dept!=null?dept.getDtStdepttype():"");
			}
			vo.setEhealthCardId(MapUtils.getString(map,"ehealthCardId"));
			vo.setTime(MapUtils.getString(map,"time"));
			vo.setPname(MapUtils.getString(map,"pname"));
			String idType = MapUtils.getString(map,"idType");
			switch (idType) {
				case "01"://身份证
				case "03":
					vo.setIdType("01");
					break;
				case "02":
					vo.setIdType("04");
					break;
				case "06"://香港护照
				case "07"://澳门护照
				case "10"://海外护照
					vo.setIdType("03");

					break;
				case "08"://台湾居民来往大陆通行证
					vo.setIdType("07");
					break;
				default:
					break;
			}
			vo.setIdcard(MapUtils.getString(map,"idcard"));
			vo.setPhone(MapUtils.getString(map,"phone"));
			vo.setDomicile(MapUtils.getString(map,"domicile"));
			vo.setChannelCode(MapUtils.getString(map,"channelCode"));
			vo.setChannelName(MapUtils.getString(map,"channelName"));
			vo.setMedStepCode(MapUtils.getString(map,"medStepCode"));
			vo.setMedStepName(MapUtils.getString(map,"medStepName"));
			vo.setChannelSn(MapUtils.getString(map,"channelSn"));
			vo.setPayAmount(MapUtils.getString(map,"payAmount"));
			vo.setPayChannel(MapUtils.getString(map,"payChannel"));
			vo.setOnlineOrOffline(MapUtils.getString(map,"onlineOrOffline"));
			useCardDataList.add(new UseCardDataVo(vo));
		}
		pi.setUseCardDataList(useCardDataList);
		String resXml= XmlUtil.beanToXml(pi,PiA1028Vo.class);
		//数字证书码  需要卡管理提供
		// TODO: 2020-12-22
		String sign =digitalCode+"A1028"+time+resXml;
		sign = MD5Util.MD5Encode(sign,"UTF-8");
		Map<String,String> reqParam = new HashMap<>();
		reqParam.put("xml",resXml);
		reqParam.put("sign",sign);
		String ret = HttpClientUtil.sendHttpPost(url,reqParam);
		PiMasterRegVo resvo=(PiMasterRegVo) XmlUtil.XmlToBean(ret, PiMasterRegVo.class);
		Map<String,Object> map = new HashMap<>();
		String res = "";
		String note = "";
		if(resvo != null && resvo.getPiRegVo() != null&&StringUtils.isNotBlank(resvo.getPiRegVo().getCount())){
			res = resvo.getPiRegVo().getCount();
		}
		if(resvo != null && resvo.getHead() != null &&StringUtils.isNotBlank(resvo.getHead().getErrMsg())){
			note = resvo.getHead().getErrMsg();
		}

		map.put("count",res);
		map.put("errMsg",note);
		return map;
	}
	
	/**
	 * 3.4 查询健康卡信息 A1006
	 * 查询持卡人档案信息
	 * @param args
	 * @return
	 */
	public Map eHealthCodeEHCA1006(Object[] args) {
		healthLog.info("--------------------------------电子健康卡接口开始--------------------------------");
		Map<String,Object> map = new HashMap<>();
		try {
			Map<String,Object> paramMap = (Map<String,Object>)args[0];
			PiMaster piVo = (PiMaster) paramMap.get("piMaster");
			if(StringUtils.isNotEmpty(piVo.getPkPi())) {
				String sql = "select * from pi_master where pk_pi = ?";
				piVo =  DataBaseHelper.queryForBean(sql,PiMaster.class, piVo.getPkPi());
			}
			PiMasterRegVo piMasterRegVo = new PiMasterRegVo();
			long time = System.currentTimeMillis();
			piMasterRegVo.setHead(new Head(merId,"A1019",time+""));
			PiRegVo piRegVo = new PiRegVo();
			if("2".equals(piVo.getDtCountry())){//中国
				piRegVo.setDtCountry("01");
			}else if("350".equals(piVo.getDtCountry())||"377".equals(piVo.getDtCountry())){
				piRegVo.setDtCountry("02");//澳门//香港
			}else{//海外
				piRegVo.setDtCountry("04");
			}

			switch (piVo.getDtIdtype()) {
				case "01"://身份证
				case "03":
					piRegVo.setDtIdtype("01");
					break;
				case "02":
					piRegVo.setDtIdtype("04");
					break;
				case "06"://香港护照
				case "07"://澳门护照
				case "10"://海外护照
					piRegVo.setDtIdtype("03");
					break;
				case "08"://台湾居民来往大陆通行证
					piRegVo.setDtIdtype("07");
					break;
				default:
					map = new HashMap<>();
					map.put("note","该证件类型不支持电子电子健康码注册！");
					break;
			}
			piRegVo.setIdNo(piVo.getIdNo());
			piMasterRegVo.setPiRegVo(piRegVo);
			String resXml= XmlUtil.beanToXml(piMasterRegVo,PiMasterRegVo.class);
			//数字证书码  需要卡管理提供
			// TODO: 2020-12-22
			String sign =digitalCode+"A1019"+time+""+resXml;
			sign = MD5Util.MD5Encode(sign,"UTF-8");
			Map<String,String> reqParam = new HashMap<>();
			reqParam.put("xml",resXml);
			reqParam.put("sign",sign);
			String ret = HttpClientUtil.sendHttpPost(url,reqParam);
			PiMasterRegVo resvo=(PiMasterRegVo) XmlUtil.XmlToBean(ret, PiMasterRegVo.class);
			if(resvo != null && resvo.getPiRegVo() != null && StringUtils.isNotEmpty(resvo.getPiRegVo().getCadreLevel())) {
				map.put("cadrelevel",resvo.getPiRegVo().getCadreLevel());//干部等级
			}
		}catch (Exception e){
			healthLog.info("电子健康卡接口异常："+e.getMessage());
		}
		finally {
			healthLog.info("--------------------------------电子健康卡接口结束--------------------------------");
		}
		return map;
	}

}
