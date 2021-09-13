package com.zebone.nhis.webservice.cxf.impl;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import com.zebone.nhis.webservice.service.BlPubForWsService;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.codehaus.jackson.type.TypeReference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.zebone.nhis.common.support.ApplicationUtils;
import com.zebone.nhis.common.support.CommonUtils;
import com.zebone.nhis.common.support.DateUtils;
import com.zebone.nhis.common.support.EnumerateParameter;
import com.zebone.nhis.common.support.XmlUtil;
import com.zebone.nhis.webservice.cxf.INHISWebService;
import com.zebone.nhis.webservice.service.CnPubForWsService;
import com.zebone.nhis.webservice.service.ExPubForWsService;
import com.zebone.nhis.webservice.service.PvPubForWsService;
import com.zebone.nhis.webservice.support.Constant;
import com.zebone.nhis.webservice.support.OtherRespJson;
import com.zebone.nhis.webservice.support.RespJson;
import com.zebone.nhis.webservice.vo.RequestXml;
import com.zebone.nhis.webservice.vo.hospinfovo.HospInfoData;
import com.zebone.nhis.webservice.vo.hospinfovo.HospInfoDataVo;
import com.zebone.nhis.webservice.vo.hospinfovo.ResHospInfoVo;
import com.zebone.nhis.webservice.vo.hospinfovo.ResponseHospInfoVo;
import com.zebone.platform.common.support.User;
import com.zebone.platform.common.support.UserContext;
import com.zebone.platform.modules.dao.DataBaseHelper;
import com.zebone.platform.modules.utils.JsonUtil;
import com.zebone.platform.web.support.ResponseJson;

/**
 * NHIS产品对外接口服务实现
 * @author yangxue
 *
 */
@WebService
@SOAPBinding(style = SOAPBinding.Style.RPC)
public class NHISWebServiceImpl implements INHISWebService {
	@Resource
	private PvPubForWsService pvPubForWsService;
	@Resource
	private ExPubForWsService exPubForWsService;
	@Resource
	private BlPubForWsService blPubForWsService;
	@Resource
	private CnPubForWsService cnPubForWsService;
	private Logger logger = LoggerFactory.getLogger("nhis.nhisWebServiceLog");
	/**
	 * 查询患者信息+就诊信息
	 * 
	 * @param param
	 * @return
	 */
	@Override
	@SuppressWarnings("unchecked")
	public String queryPiListInHosps(String param) {
		logger.info("患者信息就诊信息查询接口");
		OtherRespJson respJson = new OtherRespJson();
		try {
 			Map<String,Object> paramMap = JsonUtil.readValue(param, Map.class);
			List<Map<String,Object>> PiHospList= new ArrayList<Map<String,Object>>();
			if(MapUtils.isNotEmpty(paramMap)){
				PiHospList.addAll(pvPubForWsService.queryPiListInHosp(paramMap));
			}
			respJson.setData(JsonUtil.writeValueAsString(PiHospList,"yyyy-MM-dd HH:mm:ss"));
			respJson.setTotal(PiHospList.size());
			respJson.setStatus(Constant.RESFAIL);
			respJson.setDesc("成功");
		} catch (Exception e) {
			respJson.setStatus(Constant.UNUSUAL);
			respJson.setDesc("失败");
			respJson.setErrorMessage(e.getMessage());
		}
		return respJson.toString();
	}

	//未联调测试
	@Override
	public String getPvInfoByIp(String param) {
		String classMathName = this.getClass().getName() + "/"+ Thread.currentThread().getStackTrace()[1].getMethodName();
		//logger.info(classMathName + "/param/"+ JsonUtil.writeValueAsString(param));
		RequestXml rx = (RequestXml) XmlUtil.XmlToBean(RequestXml.class, param);
		ResponseHospInfoVo hospInfoVo = new ResponseHospInfoVo();
		String result = null;
		/*if (rx == null) {
			hospInfoVo.setStatus("-1");
			hospInfoVo.setDesc("未获取到参数");
			hospInfoVo.setErrorMessage("");
			//result = XmlUtil.beanToXml(hospInfoVo, hospInfoVo.getClass(), true);
			return hospInfoVo;
		}
		if (rx.getPkPi() == null
				|| "".equals(CommonUtils.getString(rx.getPkPi()))) {
			hospInfoVo.setStatus("-1");
			hospInfoVo.setDesc("参数pkPi不能为空");
			hospInfoVo.setErrorMessage("");
			//result = XmlUtil.beanToXml(hospInfoVo, hospInfoVo.getClass(), true);
			return hospInfoVo;
		}*/
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("pkPi", "66e0a2bdc642491b9937c815dca8fb77");
		
		/*if (rx.getCodeIp() != null && !"".equals(CommonUtils.getString(rx.getDateEnd()))) {
			paramMap.put("codeIp", rx.getCodeIp());
		}
		if (rx.getPkDept() != null
				&& !"".equals(CommonUtils.getString(rx.getPkDept()))) {
			paramMap.put("pkDept", rx.getPkDept());
		}
		if (rx.getPkOrg() != null
				&& !"".equals(CommonUtils.getString(rx.getPkOrg()))) {
			paramMap.put("pkOrg", rx.getPkOrg());
		}
		if (rx.getEuStatus() != null
				&& !"".equals(CommonUtils.getString(rx.getEuStatus()))) {
			paramMap.put("euStatus", rx.getEuStatus());
		}
		if (rx.getCodeOrg() != null
				&& !"".equals(CommonUtils.getString(rx.getCodeOrg()))) {
			paramMap.put("codeOrg", rx.getCodeOrg());
		}*/
		List<Map<String, Object>> hospInfoList = null;
		List<ResHospInfoVo> hospInfoVoList = new ArrayList<ResHospInfoVo>();
		ResHospInfoVo resHospInfoVo = null;
		try {
			hospInfoList = pvPubForWsService.getPvInfoByIp(paramMap);
		} catch (Exception e) {
			hospInfoVo.setStatus("-2");
			hospInfoVo.setDesc("程序错误");
			hospInfoVo.setErrorMessage(e.getMessage());
			//result = XmlUtil.beanToXml(hospInfoVo, hospInfoVo.getClass(), true);
			//return hospInfoVo;
		}
		for (Map<String, Object> map : hospInfoList) {
			try {
				resHospInfoVo = ApplicationUtils.mapToBean(map,
						ResHospInfoVo.class);
				hospInfoVoList.add(resHospInfoVo);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		hospInfoVo.setStatus(EnumerateParameter.ZERO);
		hospInfoVo.setDesc("成功");
		hospInfoVo.setErrorMessage("");
		HospInfoData hospInfoData = new HospInfoData();
		HospInfoDataVo hospInfoDataVo = new HospInfoDataVo();
		hospInfoDataVo.setResHospInfoVo(hospInfoVoList);
		hospInfoData.setHospInfoDataVo(hospInfoDataVo);
		hospInfoVo.setHospInfoData(hospInfoData);

		result = XmlUtil.beanToXml(hospInfoVo, hospInfoVo.getClass(), true);
		//logger.info(classMathName + "/return/"+ JsonUtil.writeValueAsString(result));
		
		return ApplicationUtils.objectToJson(hospInfoVo);
	}
	/**
	 * 查询医嘱信息
	 * @param param
	 * @return
	 */
	@Override
	public String queryOrdlist(String param) {
		logger.info("查询医嘱信息{}",param);
		OtherRespJson respJson = new OtherRespJson();
		try {
			@SuppressWarnings("unchecked")
			Map<String,Object> paramMap = JsonUtil.readValue(param, Map.class);
			List<Map<String,Object>> PiHospList= new ArrayList<Map<String,Object>>();
			if(MapUtils.isNotEmpty(paramMap)){
				PiHospList.addAll(cnPubForWsService.queryCnOrderWeb(paramMap));
			}
			respJson.setData(JsonUtil.writeValueAsString(PiHospList,"yyyy-MM-dd HH:mm:ss"));
			respJson.setTotal(PiHospList.size());
			respJson.setStatus(Constant.RESFAIL);
			respJson.setDesc("成功");
		} catch (Exception e) {
			respJson.setStatus(Constant.UNUSUAL);
			respJson.setDesc("失败");
			respJson.setErrorMessage(e.getMessage());
		}
		String json = respJson.toString();
		logger.info("查询医嘱信息{}",json);
		return json;
	}
	/**
	 * 查询查询通用执行单信息
	 * @param param
	 * @return
	 */
	@Override
	public String queryExlist(String param) {
		logger.info("查询查询通用执行单信息{}",param);
		OtherRespJson respJson = new OtherRespJson();
		try {
			@SuppressWarnings("unchecked")
			Map<String,Object> paramMap = JsonUtil.readValue(param, Map.class);
			List<Map<String,Object>> PiHospList= new ArrayList<Map<String,Object>>();
			if(MapUtils.isNotEmpty(paramMap)){
				PiHospList.addAll(exPubForWsService.queryExecListByCon(paramMap));
			}
			respJson.setData(JsonUtil.writeValueAsString(PiHospList,"yyyy-MM-dd HH:mm:ss"));
			respJson.setTotal(PiHospList.size());
			respJson.setStatus(Constant.RESFAIL);
			respJson.setDesc("成功");
		} catch (Exception e) {
			respJson.setStatus(Constant.UNUSUAL);
			respJson.setDesc("失败");
			respJson.setErrorMessage(e.getMessage());
		}
		String json = respJson.toString();
		logger.info("查询查询通用执行单信息{}",json);
		return json;
	}
	
	@Override
	public String confirmExlist(String param) {
		logger.info("通用执行{}",param);
		OtherRespJson respJson = new OtherRespJson();
		try {
			String classMathName = this.getClass().getName() + "/" + Thread.currentThread().getStackTrace()[1].getMethodName();
			//System.out.println(classMathName + "/param/" + JsonUtil.writeValueAsString(param));
			//zebonelogger.info("/param/" + JsonUtil.writeValueAsString(param));
			@SuppressWarnings("unchecked")
			Map<String, Object> map = JsonUtil.readValue(param, Map.class);
			if (map == null || map.get("codeIp") == null || CommonUtils.isEmptyString(map.get("codeIp").toString()) ||
					map.get("pkExocc") == null || CommonUtils.isEmptyString(map.get("pkExocc").toString())) {
				respJson.setStatus(Constant.UNUSUAL);
				respJson.setDesc("失败");
				respJson.setErrorMessage("失败，请检查参数");
				String resp = respJson.toString();
				logger.info("通用执行{}",resp);
				return resp;
			}
			String pkEmpOcc =  CommonUtils.getString(DataBaseHelper.queryForMap("select pk_emp from bd_ou_employee where pk_org = ? and code_emp = ?",CommonUtils.getString(map.get("pkOrg")), CommonUtils.getString(map.get("codeEmpOcc"))).get("pkEmp"));
			map.put("pkEmpOcc", pkEmpOcc);
			
			if (checkOrdEuStatue(CommonUtils.getString(map.get("pkExocc")))){
				respJson.setStatus(Constant.UNUSUAL);
				respJson.setDesc("失败");
				respJson.setErrorMessage("执行单无效");
				String resp = respJson.toString();
				logger.info("通用执行{}",resp);
				return resp;
			}
			ApplicationUtils apputil = new ApplicationUtils();
			User u = new User();
			u.setPkOrg(CommonUtils.getString(map.get("pkOrg")));
			u.setPkEmp(CommonUtils.getString(map.get("pkEmpOcc")));
			u.setNameEmp(CommonUtils.getString(map.get("nameEmpOcc")));
			u.setPkDept(CommonUtils.getString(map.get("pkDeptOcc")));
			UserContext.setUser(u);
			String dateOcc = CommonUtils.getString(map.get("dateOcc"));
			//System.out.println("-------NHISMOBNURWebServiceImpl/medicalOrderExe,date_occ begin:"+dateOcc);
			//zebonelogger.info("-------NHISMOBNURWebServiceImpl/medicalOrderExe,date_occ begin:"+dateOcc);
			if (!StringUtils.isEmpty(dateOcc)) {
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				map.put("dateOcc", DateUtils.getDateTimeStr(sdf.parse(dateOcc)));
			}
			//System.out.println("-------NHISMOBNURWebServiceImpl/medicalOrderExe,date_occ.toDate:"+map.get("dateOcc"));
			//zebonelogger.info("-------NHISMOBNURWebServiceImpl/medicalOrderExe,date_occ.toDate:"+map.get("dateOcc"));
			
			List<Map<String, Object>> exlist =exPubForWsService.queryExecListByCon(map);
			map.putAll(exlist.get(0));
			
			//调用护士站-执行确认接口
			ResponseJson rs = apputil.execService("EX", "OrderExecListConfirmService", "confirmEx", Collections.singletonList(map), u);
			List<Map<String, Object>> resList = (List<Map<String, Object>>) rs.getData();
			if (rs.getStatus() == 0) {
				//接口返回参数是空，对数据不做处理
				//List<Map<String, Object>> list = (List<Map<String, Object>>) rs.getData();
				//System.out.println(classMathName + "/return/" + JsonUtil.writeValueAsString(list));
				//zebonelogger.info("/return/" + JsonUtil.writeValueAsString(list));
				
				//respJson.setData(JsonUtil.writeValueAsString(list,"yyyy-MM-dd HH:mm:ss"));
				//respJson.setTotal(list.size());
				
				respJson.setStatus(Constant.RESFAIL);
				respJson.setDesc("成功");
				
				String resp = respJson.toString();
				logger.info("通用执行{}",resp);
				return resp;
			}
			//zebonelogger.info("医嘱执行medicalOrderExe失败"+JsonUtil.writeValueAsString(rs));
			//System.out.println("医嘱执行medicalOrderExe失败"+JsonUtil.writeValueAsString(rs));
			String errMsg=rs.getErrorMessage();
			if (!StringUtils.isEmpty(rs.getDesc())) {
				errMsg=rs.getDesc();
			}
			respJson.setStatus(Constant.UNUSUAL);
			respJson.setDesc("失败");
			respJson.setErrorMessage(errMsg);
			String resp = respJson.toString();
			logger.info("通用执行{}",resp);
			return resp;
		} catch (Exception e) {
			respJson.setStatus(Constant.UNUSUAL);
			respJson.setDesc("失败");
			respJson.setErrorMessage(e.getMessage());
			String resp = respJson.toString();
			logger.info("通用执行{}",resp);
			return resp;
		}
	}
	/**
	 * @Description 校验医技执行单是否已经被执行
	 * @auther wuqiang
	 * @Date 2019/6/17
	 * @Param [pkExocc]
	 * @return boolean
	 */
	private boolean checkOrdEuStatue(String pkExocc) {
		String sql = "select  eu_status as eu_status from ex_order_occ where pk_exocc=?";
		Map<String, Object> map1 = DataBaseHelper.queryForMap(sql, pkExocc);
		if (map1 == null || !"0".equals(CommonUtils.getString(map1.get("euStatus")))) {
			return true;
		}
		return false;
	}

	@Override
	public String confirmBsEx(String param) {
		logger.info(param);
		try {
			User u = new User();
			String classMathName = this.getClass().getName() + "/" + Thread.currentThread().getStackTrace()[1].getMethodName();
			//zebonelogger.info("/param/" + JsonUtil.writeValueAsString(param));
			//System.out.println(classMathName + "/param/" + JsonUtil.writeValueAsString(param));
			Map<String, Object> map = JsonUtil.readValue(param, Map.class);
			if (map.isEmpty()) {
				return CommonUtils.getString(new RespJson("-1|参数不能为空，请检查|", false));
			}
			if (checkOrdEuStatue(CommonUtils.getString(map.get("pkExocc")))) return CommonUtils.getString(new RespJson("-1|失败，执行单无效|", false));
			String pkEmpOcc =  CommonUtils.getString(DataBaseHelper.queryForMap("select pk_emp from bd_ou_employee where pk_org = ? and code_emp = ?",CommonUtils.getString(map.get("pkOrg")), CommonUtils.getString(map.get("codeEmpOcc"))).get("pkEmp"));
			map.put("pkEmpOcc", pkEmpOcc);
			ApplicationUtils apputil = new ApplicationUtils();
			u.setPkEmp("extwebservice");
			u.setPkOrg(CommonUtils.getString(map.get("pkOrg")));
			u.setPkEmp(CommonUtils.getString(map.get("pkEmpOcc")));
			u.setNameEmp(CommonUtils.getString(map.get("nameEmpOcc")));
			UserContext.setUser(u);
			ResponseJson rs = apputil.execService("EX", "OrderExecListConfirmService", "updateExOrderOcc", map, u);
			if (rs.getStatus() == 0) {
				List<Map<String, Object>> list = (List<Map<String, Object>>) rs.getData();
				//System.out.println(classMathName + "/return/" + JsonUtil.writeValueAsString(list));
				//zebonelogger.info("/return/" + JsonUtil.writeValueAsString(list));
				return CommonUtils.getString(new RespJson("0|成功|" + ApplicationUtils.objectToJsonWithTimeFormate(list, "yyyyMMddHHmmss"), true));
			}
			String errMsg=rs.getErrorMessage();
			if (!StringUtils.isEmpty(rs.getDesc())) {
				errMsg=rs.getDesc();
			}
			return CommonUtils.getString(new RespJson("-1|失败,"+errMsg+"|", false));
		} catch (Exception e) {
			return CommonUtils.getString(new RespJson("-1|失败,"+e.getMessage()+"|", false));
		}
	}

	@Override
	public String confirmStEx(String param) {
		try {
			User u = new User();
			String classMathName = this.getClass().getName() + "/" + Thread.currentThread().getStackTrace()[1].getMethodName();
			//System.out.println(classMathName + "/param/" + JsonUtil.writeValueAsString(param));
			//zebonelogger.info("/param/" + JsonUtil.writeValueAsString(param));
			Map<String, Object> map = JsonUtil.readValue(param, Map.class);
			if (map.isEmpty()) {
				return CommonUtils.getString(new RespJson("-1|参数不能为空，请检查|", false));
			}
			//添加批号不为空校验
			String batchNo = CommonUtils.getString(map.get("batchNo"));
	        if(StringUtils.isEmpty(batchNo)){
	        	return CommonUtils.getString(new RespJson("-1|药品批号不能为空|",false));
	        }		
			if (checkOrdEuStatue(CommonUtils.getString(map.get("pkExocc")))) return CommonUtils.getString(new RespJson("-1|失败，执行单无效|", false));
			String pkEmpOcc =  CommonUtils.getString(DataBaseHelper.queryForMap("select pk_emp from bd_ou_employee where pk_org = ? and code_emp = ?",CommonUtils.getString(map.get("pkOrgOcc")), CommonUtils.getString(map.get("codeEmpOcc"))).get("pkEmp"));
			map.put("pkEmpOcc", pkEmpOcc);
			ApplicationUtils apputil = new ApplicationUtils();
			u.setPkEmp(CommonUtils.getString(map.get("pkEmpOcc")));
			u.setNameEmp(CommonUtils.getString(map.get("nameEmpOcc")));
			u.setPkOrg(CommonUtils.getString(map.get("pkOrgOcc")));
			UserContext.setUser(u);
			
			String dateOcc = CommonUtils.getString(map.get("dateOcc"));
			if (!StringUtils.isEmpty(dateOcc)) {
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				map.put("dateOcc", DateUtils.getDateTimeStr(sdf.parse(dateOcc)));
			}
			String dateBeginSt = CommonUtils.getString(map.get("dateBeginSt"));
			if (!StringUtils.isEmpty(dateBeginSt)) {
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				map.put("dateBeginSt", DateUtils.getDateTimeStr(sdf.parse(dateBeginSt)));
			}
			ResponseJson rs = apputil.execService("EX", "OrderExecListConfirmService", "saveStResult", map, u);
			if (rs.getStatus() == 0) {
				List<Map<String, Object>> list = (List<Map<String, Object>>) rs.getData();
				//System.out.println(classMathName + "/return/" + JsonUtil.writeValueAsString(list));
				//zebonelogger.info("/return/" + JsonUtil.writeValueAsString(list));
				return CommonUtils.getString(new RespJson("0|成功|" + ApplicationUtils.objectToJsonWithTimeFormate(list, "yyyyMMddHHmmss"), true));
			}
			String errMsg=rs.getErrorMessage();
			if (!StringUtils.isEmpty(rs.getDesc())) {
				errMsg=rs.getDesc();
			}
			return CommonUtils.getString(new RespJson("-1|失败,"+errMsg+"|", false));
		} catch (Exception e) {
			return CommonUtils.getString(new RespJson("-1|失败,"+e.getMessage()+"|", false));
		}
	}
    
	/**
	 * 住院费用录入接口
	 */
	@Override
	public String savePatiCgInfoByThirdParty(String param) {
		logger.info("住院费用录入接口{}",param);
		List<Map<String, Object>> requestParam;
		try {
			requestParam = JsonUtil.readValue(param,
					new TypeReference<List<Map<String, Object>>>() {
					});
		} catch (Exception e1) {
			return CommonUtils.getString(new RespJson("-1|" + e1.getMessage(),
					false));
		}
		String respJson = blPubForWsService.savePatiCgInfo(requestParam);
		logger.info("住院费用录入接口响应{}",respJson);
		return respJson;
	}

}
