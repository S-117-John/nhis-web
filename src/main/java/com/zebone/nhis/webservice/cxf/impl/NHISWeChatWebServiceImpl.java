package com.zebone.nhis.webservice.cxf.impl;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import javax.jws.soap.SOAPBinding.Style;

import com.alibaba.fastjson.JSON;
import com.zebone.nhis.common.module.ma.sms.SmsSend;
import com.zebone.nhis.common.module.ma.sms.SmsSendResult;

import com.zebone.platform.modules.exception.BusException;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.XML;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import com.foxinmy.weixin4j.util.StringUtil;
import com.zebone.nhis.common.module.base.ou.BdOuDept;
import com.zebone.nhis.common.module.bl.BlDeposit;
import com.zebone.nhis.common.module.bl.BlSettle;
import com.zebone.nhis.common.module.bl.opcg.BlOpDt;
import com.zebone.nhis.common.module.pi.PiAcc;
import com.zebone.nhis.common.module.pi.PiCate;
import com.zebone.nhis.common.module.pi.PiMaster;
import com.zebone.nhis.common.module.sch.plan.SchTicket;
import com.zebone.nhis.common.support.ApplicationUtils;
import com.zebone.nhis.common.support.BeanUtils;
import com.zebone.nhis.common.support.CommonUtils;
import com.zebone.nhis.common.support.DateUtils;
import com.zebone.nhis.common.support.EnumerateParameter;
import com.zebone.nhis.common.support.MathUtils;
import com.zebone.nhis.common.support.XmlUtil;
import com.zebone.nhis.webservice.cxf.INHISWeChatWebService;
import com.zebone.nhis.webservice.dao.BdPubForWsMapper;
import com.zebone.nhis.webservice.dao.BlPubForWsMapper;
import com.zebone.nhis.webservice.dao.CnPubForWsMapper;
import com.zebone.nhis.webservice.dao.SchPubForWsMapper;
import com.zebone.nhis.webservice.service.BdPubForWsService;
import com.zebone.nhis.webservice.service.BlPubForWsService;
import com.zebone.nhis.webservice.service.InvPubForWsService;
import com.zebone.nhis.webservice.service.LbPubForWsService;
import com.zebone.nhis.webservice.service.PvPubForWsService;
import com.zebone.nhis.webservice.service.SchPubForWsService;
import com.zebone.nhis.webservice.support.Constant;
import com.zebone.nhis.webservice.support.LbSelfUtil;
import com.zebone.nhis.webservice.support.RespJson;
import com.zebone.nhis.webservice.vo.BlPubSettleVo;
import com.zebone.nhis.webservice.vo.ItemPriceVo;
import com.zebone.nhis.webservice.vo.LbBlCgIpVo;
import com.zebone.nhis.webservice.vo.LbPiMasterRegVo;
import com.zebone.nhis.webservice.vo.LbRecipesVo;
import com.zebone.nhis.webservice.vo.LbSHRequestVo;
import com.zebone.nhis.webservice.vo.LbSHResponseVo;
import com.zebone.nhis.webservice.vo.OpCgTransforVo;
import com.zebone.nhis.webservice.vo.RequestXml;
import com.zebone.nhis.webservice.vo.appoinvo.AppointmentData;
import com.zebone.nhis.webservice.vo.appoinvo.AppointmentDataVo;
import com.zebone.nhis.webservice.vo.appoinvo.ResAppointmentVo;
import com.zebone.nhis.webservice.vo.appoinvo.ResponseAppointmentVo;
import com.zebone.nhis.webservice.vo.appschvo.ApptData;
import com.zebone.nhis.webservice.vo.appschvo.ApptDataVo;
import com.zebone.nhis.webservice.vo.appschvo.ResAppSchVo;
import com.zebone.nhis.webservice.vo.appschvo.ResponseAppSchVo;
import com.zebone.nhis.webservice.vo.apptvo.ResApptVo;
import com.zebone.nhis.webservice.vo.apptvo.ResponseApptVo;
import com.zebone.nhis.webservice.vo.chagsetvo.Req;
import com.zebone.nhis.webservice.vo.chagsetvo.ResOpSettleVo;
import com.zebone.nhis.webservice.vo.chagsetvo.ResponseOpSettleVo;
import com.zebone.nhis.webservice.vo.depositevo.DepositeData;
import com.zebone.nhis.webservice.vo.depositevo.DepositeDataVo;
import com.zebone.nhis.webservice.vo.depositevo.ResDepositeVo;
import com.zebone.nhis.webservice.vo.depositevo.ResponseDepositeVo;
import com.zebone.nhis.webservice.vo.deptvo.DeptData;
import com.zebone.nhis.webservice.vo.deptvo.DeptDataVo;
import com.zebone.nhis.webservice.vo.deptvo.DeptTypesVo;
import com.zebone.nhis.webservice.vo.deptvo.DeptTypesVos;
import com.zebone.nhis.webservice.vo.deptvo.ResDeptVo;
import com.zebone.nhis.webservice.vo.deptvo.ResponseDeptVo;
import com.zebone.nhis.webservice.vo.effpvo.EffpvData;
import com.zebone.nhis.webservice.vo.effpvo.EffpvDataVo;
import com.zebone.nhis.webservice.vo.effpvo.ResEffpVo;
import com.zebone.nhis.webservice.vo.effpvo.ResponseEffpVo;
import com.zebone.nhis.webservice.vo.employeevo.ResEmployeeVo;
import com.zebone.nhis.webservice.vo.employeevo.ResponseEmployeeVo;
import com.zebone.nhis.webservice.vo.empvo.DoctorData;
import com.zebone.nhis.webservice.vo.empvo.DoctorDataVo;
import com.zebone.nhis.webservice.vo.empvo.ResDoctorVo;
import com.zebone.nhis.webservice.vo.empvo.ResponseDoctorVo;
import com.zebone.nhis.webservice.vo.hospinfobedvo.HospInfoBedData;
import com.zebone.nhis.webservice.vo.hospinfobedvo.HospInfoBedDataVo;
import com.zebone.nhis.webservice.vo.hospinfobedvo.ResHospInfoBedVo;
import com.zebone.nhis.webservice.vo.hospinfobedvo.ResponseHospInfoBedVo;
import com.zebone.nhis.webservice.vo.hospinfovo.HospInfoData;
import com.zebone.nhis.webservice.vo.hospinfovo.HospInfoDataVo;
import com.zebone.nhis.webservice.vo.hospinfovo.ResHospInfoVo;
import com.zebone.nhis.webservice.vo.hospinfovo.ResponseHospInfoVo;
import com.zebone.nhis.webservice.vo.hpvo.HpData;
import com.zebone.nhis.webservice.vo.hpvo.HpDataVo;
import com.zebone.nhis.webservice.vo.hpvo.ResHpVo;
import com.zebone.nhis.webservice.vo.hpvo.ResponseHpVo;
import com.zebone.nhis.webservice.vo.ipcgdayvo.IpCgDayData;
import com.zebone.nhis.webservice.vo.ipcgdayvo.IpCgDayDataVo;
import com.zebone.nhis.webservice.vo.ipcgdayvo.ResIpCgDayVo;
import com.zebone.nhis.webservice.vo.ipcgdayvo.ResponseIpCgDayVo;
import com.zebone.nhis.webservice.vo.ipcgvo.IpCgData;
import com.zebone.nhis.webservice.vo.ipcgvo.IpCgDataVo;
import com.zebone.nhis.webservice.vo.ipcgvo.ResIpCgVo;
import com.zebone.nhis.webservice.vo.ipcgvo.ResponseIpCgVo;
import com.zebone.nhis.webservice.vo.itemvo.ItemCateData;
import com.zebone.nhis.webservice.vo.itemvo.ItemCateDataVo;
import com.zebone.nhis.webservice.vo.itemvo.ResItemCateVo;
import com.zebone.nhis.webservice.vo.itemvo.ResponseItemCateVo;
import com.zebone.nhis.webservice.vo.orgareavo.OrgAreaData;
import com.zebone.nhis.webservice.vo.orgareavo.OrgAreaDataVo;
import com.zebone.nhis.webservice.vo.orgareavo.ResOrgAreaVo;
import com.zebone.nhis.webservice.vo.orgareavo.ResponseOrgAreaVo;
import com.zebone.nhis.webservice.vo.orgvo.OrgData;
import com.zebone.nhis.webservice.vo.orgvo.OrgDataVo;
import com.zebone.nhis.webservice.vo.orgvo.ResOrgVo;
import com.zebone.nhis.webservice.vo.orgvo.ResponseOrgVo;
import com.zebone.nhis.webservice.vo.paidfeevo.PaidFeeData;
import com.zebone.nhis.webservice.vo.paidfeevo.PaidFeeDataVo;
import com.zebone.nhis.webservice.vo.paidfeevo.ResPaidFeeVo;
import com.zebone.nhis.webservice.vo.paidfeevo.ResponsePaidFeeVo;
import com.zebone.nhis.webservice.vo.paidvo.PaidData;
import com.zebone.nhis.webservice.vo.paidvo.PaidDataVo;
import com.zebone.nhis.webservice.vo.paidvo.ResPaidVo;
import com.zebone.nhis.webservice.vo.paidvo.ResponsePaidVo;
import com.zebone.nhis.webservice.vo.payordervo.DepositData;
import com.zebone.nhis.webservice.vo.payordervo.DepositDatas;
import com.zebone.nhis.webservice.vo.payordervo.InSettleData;
import com.zebone.nhis.webservice.vo.payordervo.InSettleDatas;
import com.zebone.nhis.webservice.vo.payordervo.PayInfoData;
import com.zebone.nhis.webservice.vo.payordervo.PayInfoOrderData;
import com.zebone.nhis.webservice.vo.payordervo.PayInfoRes;
import com.zebone.nhis.webservice.vo.payordervo.RegData;
import com.zebone.nhis.webservice.vo.payordervo.RegDatas;
import com.zebone.nhis.webservice.vo.payordervo.SettleData;
import com.zebone.nhis.webservice.vo.payordervo.SettleDatas;
import com.zebone.nhis.webservice.vo.picardvo.ResPiCardVo;
import com.zebone.nhis.webservice.vo.picardvo.ResponsePiCardVo;
import com.zebone.nhis.webservice.vo.picatevo.PicateData;
import com.zebone.nhis.webservice.vo.picatevo.PicateDataVo;
import com.zebone.nhis.webservice.vo.picatevo.ResPicateVo;
import com.zebone.nhis.webservice.vo.picatevo.ResponsePicateVo;
import com.zebone.nhis.webservice.vo.prechargevo.ResponsePreChargeVo;
import com.zebone.nhis.webservice.vo.preopsettle.BlDeposits;
import com.zebone.nhis.webservice.vo.preopsettle.BlOpDtVo;
import com.zebone.nhis.webservice.vo.preopsettle.BlOpDts;
import com.zebone.nhis.webservice.vo.preopsettle.ChagSett;
import com.zebone.nhis.webservice.vo.preopsettle.ChagSettList;
import com.zebone.nhis.webservice.vo.preopsettle.ChagSetts;
import com.zebone.nhis.webservice.vo.prepayvo.PrePayData;
import com.zebone.nhis.webservice.vo.prepayvo.PrePayDataVo;
import com.zebone.nhis.webservice.vo.prepayvo.ResPrePayVo;
import com.zebone.nhis.webservice.vo.prepayvo.ResponsePrePayVo;
import com.zebone.nhis.webservice.vo.pvchargevo.PvChargeData;
import com.zebone.nhis.webservice.vo.pvchargevo.PvChargeDataVo;
import com.zebone.nhis.webservice.vo.pvchargevo.ResPvChargeVo;
import com.zebone.nhis.webservice.vo.pvchargevo.ResponsePvChargeVo;
import com.zebone.nhis.webservice.vo.pvinfovo.PvInfoData;
import com.zebone.nhis.webservice.vo.pvinfovo.PvInfoDataVo;
import com.zebone.nhis.webservice.vo.pvinfovo.ResPvInfoVo;
import com.zebone.nhis.webservice.vo.pvinfovo.ResponsePvInfoVo;
import com.zebone.nhis.webservice.vo.registervo.RegisterData;
import com.zebone.nhis.webservice.vo.registervo.RegisterDataVo;
import com.zebone.nhis.webservice.vo.registervo.ResRegisterVo;
import com.zebone.nhis.webservice.vo.registervo.ResponseRegisterVo;
import com.zebone.nhis.webservice.vo.schInfovo.ResSchInfoVo;
import com.zebone.nhis.webservice.vo.schInfovo.ResponseSchInfoVo;
import com.zebone.nhis.webservice.vo.schInfovo.SchInfoData;
import com.zebone.nhis.webservice.vo.schInfovo.SchInfoDataVo;
import com.zebone.nhis.webservice.vo.ticketvo.Data;
import com.zebone.nhis.webservice.vo.ticketvo.Res;
import com.zebone.nhis.webservice.vo.ticketvo.SchTicketSec;
import com.zebone.nhis.webservice.vo.ticketvo.SchTicketSecs;
import com.zebone.nhis.webservice.vo.ticketvo.Sche;
import com.zebone.nhis.webservice.vo.ticketvo.ScheList;
import com.zebone.nhis.webservice.vo.ticketvo.Schsch;
import com.zebone.nhis.webservice.vo.ticketvo.Schschs;
import com.zebone.platform.common.support.User;
import com.zebone.platform.common.support.UserContext;
import com.zebone.platform.modules.dao.DataBaseHelper;
import com.zebone.platform.modules.utils.JsonUtil;
import com.zebone.platform.web.support.ResponseJson;

/**
 * ????????????webservice??????-json?????????????????????
 *
 * @author yangxue
 */
@WebService
@SOAPBinding(style = Style.RPC)



public class NHISWeChatWebServiceImpl implements INHISWeChatWebService {
	
	private Logger logger = LoggerFactory.getLogger("com.zebone");

    @Resource
    private BdPubForWsService bdPubForWsService;


    @Resource
    private BlPubForWsService blPubForWsService;

    @Resource
    private PvPubForWsService pvPubForWsService;

	@Resource
	private SchPubForWsService schPubForWsService;
	
	@Resource
	private BlPubForWsMapper blPubForWsMapper;
	
	@Autowired
	private CnPubForWsMapper cnPubForWsMapper;
	@Resource
	private InvPubForWsService invPubForWsService;
	@Resource
	private LbPubForWsService lbPubForWsService;
	@Resource
	private SchPubForWsMapper schPubForWsMapper;
	@Resource
	private BdPubForWsMapper bdPubForWsMapper;
	@Autowired
	private SchPubForWsMapper schMapper;
	
	ApplicationUtils apputil = new ApplicationUtils();

	/**
	* ?????????????????????????????????????????????
	* @author leiminjian
	* @param ?????????
	* @return 
	*/
	@Override
	public String getOrgInfo() {
		List<Map<String,Object>> orglist = bdPubForWsService.getOrgInfo(null);
		return CommonUtils.getString(new RespJson("0|??????|" + ApplicationUtils.objectToJson(orglist), true));
	}


	@Override
	public String getOrgList() {
		ResponseOrgVo orgVo = new ResponseOrgVo();
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("flagActive", "1");
		paramMap.put("delFlag", "0");
		List<Map<String, Object>> orgList = null;
		List<ResOrgVo> orgVoList = new ArrayList<ResOrgVo>();
		ResOrgVo resOrgVo = null;
		String result = null;
		try {
			orgList = bdPubForWsService.getOrgInfo(paramMap);
		} catch (Exception e) {
			orgVo.setStatus("-2");
			orgVo.setDesc("????????????");
			orgVo.setErrorMessage(e.getMessage());
			result = XmlUtil.beanToXml(orgVo, orgVo.getClass(), true);
			return result;
		}

		for (Map<String, Object> map : orgList) {
			resOrgVo = new ResOrgVo();
			try {
				resOrgVo = ApplicationUtils.mapToBean(map, ResOrgVo.class);
				orgVoList.add(resOrgVo);
			} catch (Exception e) {
				orgVo.setStatus("-2");
				orgVo.setDesc("????????????");
				orgVo.setErrorMessage(e.getMessage());
				result = XmlUtil.beanToXml(orgVo, orgVo.getClass(), true);
				return result;
			}
		}
		orgVo.setStatus(EnumerateParameter.ZERO);
		orgVo.setDesc("??????");
		orgVo.setErrorMessage("");
		OrgData orgData = new OrgData();
		OrgDataVo orgDataVo = new OrgDataVo();
		orgDataVo.setResOrgVo(orgVoList);
		orgData.setOrgDataVo(orgDataVo);
		orgVo.setOrgData(orgData);
		result = XmlUtil.beanToXml(orgVo, orgVo.getClass(), true);
		String classMathName = this.getClass().getName() + "/"
				+ Thread.currentThread().getStackTrace()[1].getMethodName();
		logger.info(classMathName + "/return/" + JsonUtil.writeValueAsString(result));
		return result;
	}
	
	
	/**
	* ????????????????????????
	* @author zhangtao
	* @param paramMap: pkOrg:?????????????????? 
	* @return
	*/
	@Override
	public String qryOrgAreaInfo(String param) {
		/*
		if (paramMap == null)
			return  CommonUtils.getString(new RespJson("-1|??????????????????", true));
		List<Map<String,Object>> deptlist = bdPubForWsService.getOrgAreaInfo(paramMap);
		return CommonUtils.getString(new RespJson("0|??????|" + ApplicationUtils.objectToJson(deptlist), true));*/
		String classMathName = this.getClass().getName() + "/"
				+ Thread.currentThread().getStackTrace()[1].getMethodName();
		logger.info(classMathName + "/param/" + JsonUtil.writeValueAsString(param));
		ResponseOrgAreaVo orgAreaVo = new ResponseOrgAreaVo();
		String result = null;
		RequestXml rx = null;
		try {
			rx = (RequestXml) XmlUtil.XmlToBean(RequestXml.class, param);
		} catch (Exception e) {
			orgAreaVo.setStatus("-1");
			orgAreaVo.setDesc("??????????????????");
			orgAreaVo.setErrorMessage("");
			result = XmlUtil.beanToXml(orgAreaVo, orgAreaVo.getClass(), true);
			return result;
		}
		
		Map<String, Object> paramMap = new HashMap<String, Object>();
		if (rx.getPkOrg() == null || "".equals(CommonUtils.getString(rx.getPkOrg()))) {
			orgAreaVo.setStatus("-1");
			orgAreaVo.setDesc("??????pkOrg????????????");
			orgAreaVo.setErrorMessage("");
			result = XmlUtil.beanToXml(orgAreaVo, orgAreaVo.getClass(), true);
			return result;
		}
		paramMap.put("pkOrg", rx.getPkOrg());
		
		List<Map<String, Object>> orgAreaList = null;
		List<ResOrgAreaVo> orgAreaVoList = new ArrayList<ResOrgAreaVo>();
		ResOrgAreaVo resOrgAreaVo = null;
		try {
			orgAreaList = bdPubForWsService.getOrgAreaInfo(paramMap);
		} catch (Exception e) {
			orgAreaVo.setStatus("-2");
			orgAreaVo.setDesc("????????????");
			orgAreaVo.setErrorMessage(e.getMessage());
			result = XmlUtil.beanToXml(orgAreaVo, orgAreaVo.getClass(), true);
			return result;
		}

		for (Map<String, Object> map : orgAreaList) {
			resOrgAreaVo = new ResOrgAreaVo();
			try {
				resOrgAreaVo = ApplicationUtils.mapToBean(map, ResOrgAreaVo.class);
				orgAreaVoList.add(resOrgAreaVo);
			} catch (Exception e) {
				orgAreaVo.setStatus("-2");
				orgAreaVo.setDesc("????????????");
				orgAreaVo.setErrorMessage(e.getMessage());
				result = XmlUtil.beanToXml(orgAreaVo, orgAreaVo.getClass(), true);
				return result;
			}
		}
		orgAreaVo.setStatus(EnumerateParameter.ZERO);
		orgAreaVo.setDesc("??????");
		orgAreaVo.setErrorMessage("");
		OrgAreaData orgAreaData = new OrgAreaData();
		OrgAreaDataVo orgAreaDataVo = new OrgAreaDataVo();
		orgAreaDataVo.setResOrgAreaVo(orgAreaVoList);
		orgAreaData.setOrgAreaDataVo(orgAreaDataVo);
		orgAreaVo.setOrgAreaData(orgAreaData);
		
		result = XmlUtil.beanToXml(orgAreaVo, orgAreaVo.getClass(), true);
		logger.info(classMathName + "/return/" + JsonUtil.writeValueAsString(result));
		return result;	
	}

    /**
	* ??????????????????
	* @author leiminjian
	* @param paramMap: pkOrg:?????????????????? ; codeOrg???????????????
	* @return
	*/
	@SuppressWarnings("unused")
	@Override
	public String getDeptInfo(String param) {
		String classMathName = this.getClass().getName() + "/"
				+ Thread.currentThread().getStackTrace()[1].getMethodName();
		logger.info(classMathName + "/param/" + JsonUtil.writeValueAsString(param));
		ResponseDeptVo deptVo = new ResponseDeptVo();
		String result = null;
		RequestXml rx = null;
		try {
			rx = (RequestXml) XmlUtil.XmlToBean(RequestXml.class, param);
		} catch (Exception e) {
			deptVo.setStatus("-1");
			deptVo.setDesc("??????????????????");
			deptVo.setErrorMessage("");
			result = XmlUtil.beanToXml(deptVo, deptVo.getClass(), true);
			return result;
		}
		DeptTypesVos typeDeptVos = null;
		DeptTypesVo typeDeptVo = null;
		List<DeptTypesVo> typeDeptList = null;

		if (rx.getPkOrg() == null || "".equals(CommonUtils.getString(rx.getPkOrg()))) {
			deptVo.setStatus("-1");
			deptVo.setDesc("??????pkOrg????????????");
			deptVo.setErrorMessage("");
			result = XmlUtil.beanToXml(deptVo, deptVo.getClass(), true);
			return result;
		}
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("pkOrg", rx.getPkOrg());
		List<Map<String, Object>> deptList = null;
		List<ResDeptVo> deptVoList = new ArrayList<ResDeptVo>();
		ResDeptVo resDeptVo = null;

		try {
			deptList = bdPubForWsService.getDeptInfo(paramMap);
		} catch (Exception e) {
			deptVo.setStatus("-2");
			deptVo.setDesc("????????????");
			deptVo.setErrorMessage(e.getMessage());
			result = XmlUtil.beanToXml(deptVo, deptVo.getClass(), true);
			return result;
		}
		List<String> pkDepts = new ArrayList<String>();
		for (Map<String, Object> deptMap : deptList) {
			pkDepts.add(deptMap.get("pkDept").toString());
		}
		List<DeptTypesVo> deptTypesVoList = bdPubForWsService.getDeptTypes(pkDepts);
		for (Map<String, Object> map : deptList) {
			try {
				resDeptVo = ApplicationUtils.mapToBean(map, ResDeptVo.class);
				typeDeptList = new ArrayList<DeptTypesVo>();
				typeDeptVos = new DeptTypesVos();
				for (DeptTypesVo deptTypesVo : deptTypesVoList) {
					if (map.get("pkDept").toString().equals(deptTypesVo.getPkDept())) {
						typeDeptList.add(deptTypesVo);
					}
				}
				typeDeptVos.setDeptTypesVo(typeDeptList);
				resDeptVo.setDeptTypes(typeDeptVos);
				deptVoList.add(resDeptVo);
			} catch (Exception e) {
				deptVo.setStatus("-2");
				deptVo.setDesc("????????????");
				deptVo.setErrorMessage(e.getMessage());
				result = XmlUtil.beanToXml(deptVo, deptVo.getClass(), true);
				return result;
			}
		}
		deptVo.setStatus(EnumerateParameter.ZERO);
		deptVo.setDesc("??????");
		deptVo.setErrorMessage("");
		DeptDataVo deptDataVo = new DeptDataVo();
		Collections.sort(deptVoList, new Comparator<ResDeptVo>() {

			@Override
			public int compare(ResDeptVo o1, ResDeptVo o2) {
				return o1.getCodeDept().compareTo(o2.getCodeDept());
			}
		});
		deptDataVo.setResDeptVo(deptVoList);
		DeptData deptData = new DeptData();
		deptData.setDeptDataVo(deptDataVo);
		deptVo.setDatalist(deptData);

		result = XmlUtil.beanToXml(deptVo, deptVo.getClass(), true);
		logger.info(classMathName + "/return/" + JsonUtil.writeValueAsString(result));
		return result;
	}
	/**
	 * ?????????????????????????????? JSON ???????????? XML
	 * <p>Title: getEmpInfo</p>
	 * <p>Description: </p>
	 * @param param
	 * @return
	 * @see com.zebone.nhis.webservice.cxf.INHISSelfHelpWebService#getEmpInfo(java.lang.String)
	 */
	@Override
	public String getEmpInfo(String param) {
		String classMathName = this.getClass().getName() + "/"
				+ Thread.currentThread().getStackTrace()[1].getMethodName();
		logger.info(classMathName + "/param/" + JsonUtil.writeValueAsString(param));
		RequestXml rx = (RequestXml) XmlUtil.XmlToBean(RequestXml.class, param);
		ResponseDoctorVo doctorVo = new ResponseDoctorVo();
		String result = null;
		if (rx == null) {
			doctorVo.setStatus("-1");
			doctorVo.setDesc("??????????????????");
			doctorVo.setErrorMessage("");
			result = XmlUtil.beanToXml(doctorVo, doctorVo.getClass(), true);
			return result;
		}
		if (rx.getPkDept() == null || "".equals(CommonUtils.getString(rx.getPkDept()))) {
			doctorVo.setStatus("-1");
			doctorVo.setDesc("??????pkDept????????????");
			doctorVo.setErrorMessage("");
			result = XmlUtil.beanToXml(doctorVo, doctorVo.getClass(), true);
			return result;
		}
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("pkDept", rx.getPkDept());
		List<Map<String, Object>> doctorList = null;
		List<ResDoctorVo> doctorVoList = new ArrayList<ResDoctorVo>();
		ResDoctorVo resDoctorVo = null;
		try {
			doctorList = bdPubForWsService.getEmpInfo(paramMap);
		} catch (Exception e) {
			doctorVo.setStatus("-2");
			doctorVo.setDesc("????????????");
			doctorVo.setErrorMessage(e.getMessage());
			result = XmlUtil.beanToXml(doctorVo, doctorVo.getClass(), true);
			return result;
		}
		for (Map<String, Object> map : doctorList) {
			try {
				resDoctorVo = ApplicationUtils.mapToBean(map, ResDoctorVo.class);
				doctorVoList.add(resDoctorVo);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		doctorVo.setStatus(EnumerateParameter.ZERO);
		doctorVo.setDesc("??????");
		doctorVo.setErrorMessage("");
		DoctorData doctorData = new DoctorData();
		DoctorDataVo doctorDataVo = new DoctorDataVo();
		doctorDataVo.setResDoctorVo(doctorVoList);
		doctorData.setDoctorDataVo(doctorDataVo);
		doctorVo.setDoctorData(doctorData);

		result = XmlUtil.beanToXml(doctorVo, doctorVo.getClass(), true);
		logger.info(classMathName + "/return/" + JsonUtil.writeValueAsString(result));
		return result;
	}
	
	/**
	 * ??????????????????????????????????????????
	 * <p>Title: getPiMasterInfo</p>
	 * <p>Description: </p>
	 * @param param
	 * @return
	 * @see com.zebone.nhis.webservice.cxf.INHISSelfHelpWebService#getPiMasterInfo(java.lang.String)
	 */
	@Override
	public String getPiMasterInfo(String param) {
		String classMathName = this.getClass().getName() + "/"+ Thread.currentThread().getStackTrace()[1].getMethodName();
		System.out.println(classMathName + "/param/" + JsonUtil.writeValueAsString(param));
		RequestXml rx = (RequestXml) XmlUtil.XmlToBean(RequestXml.class, param);
		com.zebone.nhis.webservice.vo.ResponseMasterVo masterVo = new com.zebone.nhis.webservice.vo.ResponseMasterVo();
		String result = null;
		if (rx == null) {
			masterVo.setStatus("-1");
			masterVo.setDesc("??????????????????");
			masterVo.setErrorMessage("");
			result = XmlUtil.beanToXml(masterVo, masterVo.getClass(), true);
			return result;
		}

		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("pkPi", rx.getPkPi());
		paramMap.put("codePi", rx.getCodePi());
		paramMap.put("idno", rx.getIdno());
		paramMap.put("codeIp", rx.getCodeIp());
		paramMap.put("codeCard", rx.getCodeCard());
		paramMap.put("pkOrg", rx.getPkOrg());

		Map<String, Object> piMaster = null;
		com.zebone.nhis.webservice.vo.ResMasterVo resMasterVo = null;
		try {
			// ????????????????????????
			piMaster = pvPubForWsService.getPiMasterInfo(paramMap);
			piMaster.put("dt_Sex", piMaster.get("sex"));
			piMaster.put("birth_Date", piMaster.get("birthday"));
			piMaster.put("pk_Pi", piMaster.get("patientid"));
			piMaster.put("name_Pi", piMaster.get("name"));
			piMaster.put("codeOp", piMaster.get("codeOp"));
			if(null!=piMaster.get("dtcardNo")){
				piMaster.put("dtcardNo", piMaster.get("dtcardNo"));
			}
		} catch (Exception e) {
			masterVo.setStatus("-2");
			masterVo.setDesc("????????????");
			masterVo.setErrorMessage(e.getMessage());
			result = XmlUtil.beanToXml(masterVo, masterVo.getClass(), true);
			return result;
		}
		try {
			resMasterVo = ApplicationUtils.mapToBean(piMaster, com.zebone.nhis.webservice.vo.ResMasterVo.class);
		} catch (Exception e) {
			e.printStackTrace();
		}
		masterVo.setStatus(EnumerateParameter.ZERO);
		masterVo.setDesc("??????");
		masterVo.setErrorMessage("");
		masterVo.setResMasterVo(resMasterVo);
		result = XmlUtil.beanToXml(masterVo, masterVo.getClass(), true);
		System.out.println(classMathName + "/return/" + JsonUtil.writeValueAsString(result));
		return result;

	}
	/**
	 * ????????????????????????(?????????)
	 * @author kongxuedong
	 * @return java.lang.String
	 */
	@Override
	public String getPiCateInfo() {
		List<Map<String, Object>> picatelist = null;
		PicateData picateData = new PicateData();
		ResponsePicateVo pivo = new ResponsePicateVo();
		List<ResPicateVo> resPicateList = new ArrayList<ResPicateVo>();
		ResPicateVo resPicateVo = null;
		String result = null;
		try {
			picatelist = pvPubForWsService.getPiCateInfo();
			for (Map<String, Object> map : picatelist) {
				resPicateVo = ApplicationUtils.mapToBean(map, ResPicateVo.class);
				resPicateList.add(resPicateVo);
			}

		} catch (Exception e) {
			pivo.setStatus("-2");
			pivo.setDesc("????????????");
			pivo.setErrorMessage(e.getMessage());
			result = XmlUtil.beanToXml(pivo, pivo.getClass(), true);
			return result;
		}
		PicateDataVo piDataVo = new PicateDataVo();
		piDataVo.setResPicateVo(resPicateList);
		picateData.setPicateDataVo(piDataVo);
		pivo.setStatus("0");
		pivo.setDesc("??????");
		pivo.setErrorMessage("");
		pivo.setDatalist(picateData);
		result = XmlUtil.beanToXml(pivo, pivo.getClass(), true);
		String classMathName = this.getClass().getName() + "/"
				+ Thread.currentThread().getStackTrace()[1].getMethodName();
		logger.info(classMathName + "/return/" + JsonUtil.writeValueAsString(result));
		return result;
	}
	/**
     * ????????????
     *
     * @param param
     * @return
     */
	@SuppressWarnings("unchecked")
	@Override
	public String savePiMaster(String param) {
		String classMathName = this.getClass().getName() + "/"
				+ Thread.currentThread().getStackTrace()[1].getMethodName();
		System.out.println(classMathName + "/param/" + JsonUtil.writeValueAsString(param));
		// TODO Auto-generated method stub
		RequestXml rx = (RequestXml) XmlUtil.XmlToBean(RequestXml.class, param);
		com.zebone.nhis.webservice.vo.ResponsePmVo pmVo = new com.zebone.nhis.webservice.vo.ResponsePmVo();
		String result = null;
		if (rx == null) {
			pmVo.setStatus("-1");
			pmVo.setDesc("??????????????????");
			pmVo.setErrorMessage("");
			result = XmlUtil.beanToXml(pmVo, pmVo.getClass(), true);
			return result;
		}
		if (rx.getPkOrg() == null || "".equals(CommonUtils.getString(rx.getPkOrg()))) {
			pmVo.setStatus("-1");
			pmVo.setDesc("pkOrg????????????");
			pmVo.setErrorMessage("");
			result = XmlUtil.beanToXml(pmVo, pmVo.getClass(), true);
			return result;
		}
		if (rx.getDtIdtype() == null || "".equals(CommonUtils.getString(rx.getDtIdtype()))) {
			pmVo.setStatus("-1");
			pmVo.setDesc("dtIdtype????????????");
			pmVo.setErrorMessage("");
			result = XmlUtil.beanToXml(pmVo, pmVo.getClass(), true);
			return result;
		}
		if (rx.getDtSex() == null || "".equals(CommonUtils.getString(rx.getDtSex()))) {
			pmVo.setStatus("-1");
			pmVo.setDesc("dtSex????????????");
			pmVo.setErrorMessage("");
			result = XmlUtil.beanToXml(pmVo, pmVo.getClass(), true);
			return result;
		}
		if (rx.getIdNo() == null || "".equals(CommonUtils.getString(rx.getIdNo()))) {
			pmVo.setStatus("-1");
			pmVo.setDesc("idNo????????????");
			pmVo.setErrorMessage("");
			result = XmlUtil.beanToXml(pmVo, pmVo.getClass(), true);
			return result;
		}
		if (rx.getMobile() == null || "".equals(CommonUtils.getString(rx.getMobile()))) {
			pmVo.setStatus("-1");
			pmVo.setDesc("mobile????????????");
			pmVo.setErrorMessage("");
			result = XmlUtil.beanToXml(pmVo, pmVo.getClass(), true);
			return result;
		}
		if (rx.getNamePi() == null || "".equals(CommonUtils.getString(rx.getNamePi()))) {
			pmVo.setStatus("-1");
			pmVo.setDesc("namePi????????????");
			pmVo.setErrorMessage("");
			result = XmlUtil.beanToXml(pmVo, pmVo.getClass(), true);
			return result;
		}
		PiCate piCate = DataBaseHelper.queryForBean("SELECT * FROM pi_cate where flag_def='1'", PiCate.class);
		rx.setPkPicate(piCate.getPkPicate());
		Map<String, Object> paramMap = (Map<String, Object>) ApplicationUtils.beanToMap(rx);
		Map<String, Object> piResult = null;
		com.zebone.nhis.webservice.vo.ResPmVo resPmVo = new com.zebone.nhis.webservice.vo.ResPmVo();
		try {
			piResult = pvPubForWsService.savePiMaster(paramMap);
		} catch (Exception e) {
			pmVo.setStatus("-2");
			pmVo.setDesc("????????????");
			pmVo.setErrorMessage(e.getMessage());
			result = XmlUtil.beanToXml(pmVo, pmVo.getClass(), true);
			return result;
		}
		if ("false".equals(piResult.get("result"))) {
			resPmVo.setCodeOp((String) piResult.get("codeOp"));
			resPmVo.setCodePi((String) piResult.get("codePi"));
			resPmVo.setNamePi((String) piResult.get("namePi"));
			resPmVo.setPkPi((String) piResult.get("pkPi"));
			pmVo.setStatus("-1");
			pmVo.setDesc((String) piResult.get("desc"));
			pmVo.setErrorMessage("");
			pmVo.setData(resPmVo);
			result = XmlUtil.beanToXml(pmVo, pmVo.getClass(), true);
			return result;
		}
		resPmVo.setCodeOp((String) piResult.get("codeOp"));
		resPmVo.setCodePi((String) piResult.get("codePi"));
		resPmVo.setNamePi((String) piResult.get("namePi"));
		resPmVo.setPkPi((String) piResult.get("pkPi"));
		pmVo.setStatus("0");
		pmVo.setDesc((String) piResult.get("desc"));
		pmVo.setErrorMessage("");
		pmVo.setData(resPmVo);
		result = XmlUtil.beanToXml(pmVo, pmVo.getClass(), true);
		System.out.println(classMathName + "/return/" + JsonUtil.writeValueAsString(result));
		return result;
	}
	/**
     * ????????????
     *
     * @param param
     * @return
     */
	@Override
	@Transactional
	public String updatePiMaster(String param) {
		String classMathName = this.getClass().getName() + "/"
				+ Thread.currentThread().getStackTrace()[1].getMethodName();
		System.out.println(classMathName + "/param/" + JsonUtil.writeValueAsString(param));
		// TODO Auto-generated method stub
		RequestXml rx = (RequestXml) XmlUtil.XmlToBean(RequestXml.class, param);
		com.zebone.nhis.webservice.vo.ResponsePmVo pmVo = new com.zebone.nhis.webservice.vo.ResponsePmVo();
		String result = null;
		if (rx == null) {
			pmVo.setStatus("-1");
			pmVo.setDesc("??????????????????");
			pmVo.setErrorMessage("");
			result = XmlUtil.beanToXml(pmVo, pmVo.getClass(), true);
			return result;
		}
		
		if (rx.getPkPi()== null || "".equals(CommonUtils.getString(rx.getPkPi()))) {
			pmVo.setStatus("-1");
			pmVo.setDesc("pkpi????????????");
			pmVo.setErrorMessage("");
			result = XmlUtil.beanToXml(pmVo, pmVo.getClass(), true);
			return result;
		}
		if (rx.getHicNo()== null) {
			pmVo.setStatus("-1");
			pmVo.setDesc("hicNo????????????");
			pmVo.setErrorMessage("");
			result = XmlUtil.beanToXml(pmVo, pmVo.getClass(), true);
			return result;
		}
		PiMaster piMaster=new PiMaster();
		piMaster.setPkPi(rx.getPkPi());
		piMaster.setHicNo(rx.getHicNo());
		DataBaseHelper.updateBeanByPk(piMaster,false);
		pmVo.setStatus("0");
		pmVo.setDesc(null);
		pmVo.setErrorMessage("");
		pmVo.setData(null);
		result = XmlUtil.beanToXml(pmVo, pmVo.getClass(), true);
		System.out.println(classMathName + "/return/" + JsonUtil.writeValueAsString(result));
		return result;
	}
	/**
	 * ??????????????????(?????????)
	 * @author kongxuedong
	 * @return java.lang.String
	 */
	@Override
	public String getItemCateInfo() {
		ResponseItemCateVo itemCateVo = new ResponseItemCateVo();
		List<ResItemCateVo> list = new ArrayList<ResItemCateVo>();
		List<Map<String, Object>> itemcatelist = null;
		ResItemCateVo resVo = null;
		String result = null;
		try {
			itemcatelist = bdPubForWsService.getItemCateInfo(null);
			for (Map<String, Object> map : itemcatelist) {
				resVo = ApplicationUtils.mapToBean(map, ResItemCateVo.class);
				list.add(resVo);
			}
		} catch (Exception e) {
			itemCateVo.setStatus("-2");
			itemCateVo.setDesc("????????????");
			itemCateVo.setErrorMessage(e.getMessage());
			result = XmlUtil.beanToXml(itemCateVo, itemCateVo.getClass(), true);
			return result;
		}
		ItemCateData itemData = new ItemCateData();
		ItemCateDataVo itemDataVo = new ItemCateDataVo();
		itemDataVo.setResItemCateVos(list);
		itemData.setItemCateDataVo(itemDataVo);
		itemCateVo.setStatus("0");
		itemCateVo.setDesc("??????");
		itemCateVo.setErrorMessage("");
		itemCateVo.setDatalist(itemData);
		result = XmlUtil.beanToXml(itemCateVo, itemCateVo.getClass(), true);
		String classMathName = this.getClass().getName() + "/"
				+ Thread.currentThread().getStackTrace()[1].getMethodName();
		logger.info(classMathName + "/return/" + JsonUtil.writeValueAsString(result));
		return result;
	}

    /**
	 * ????????????????????????
	 * @param paramMap{pkOrg?????????}
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@Override
	public String getAppSchInfo(String param) {
		String classMathName = this.getClass().getName() + "/"
				+ Thread.currentThread().getStackTrace()[1].getMethodName();
		logger.info(classMathName + "/param/" + JsonUtil.writeValueAsString(param));
		long start = new Date().getTime();
		RequestXml rx = (RequestXml) XmlUtil.XmlToBean(RequestXml.class, param);
		ResponseAppSchVo appSchVo = new ResponseAppSchVo();
		String result = null;
		if (rx == null) {
			appSchVo.setStatus("-1");
			appSchVo.setDesc("??????????????????");
			appSchVo.setErrorMessage("");
			result = XmlUtil.beanToXml(appSchVo, appSchVo.getClass(), true);
			return result;
		}
		if (StringUtil.isEmpty(rx.getCodeOrg()) && StringUtil.isEmpty(rx.getPkOrg())) {
			appSchVo.setStatus("-1");
			appSchVo.setDesc("pkOrg???codeOrg??????????????????");
			appSchVo.setErrorMessage("");
			result = XmlUtil.beanToXml(appSchVo, appSchVo.getClass(), true);
			return result;
		} else if (StringUtil.isEmpty(rx.getPkOrg())) {
			String pkOrg = getPropValueStr(getOrgByCode(rx.getCodeOrg()), "pkOrg");
			rx.setPkOrg(pkOrg);
			if (StringUtil.isEmpty(pkOrg)) {
				appSchVo.setStatus("-1");
				appSchVo.setDesc("codeOrg?????????");
				appSchVo.setErrorMessage("");
				result = XmlUtil.beanToXml(appSchVo, appSchVo.getClass(), true);
				return result;
			}
			rx.setPkOrg(pkOrg);
		}
		User user = new User();
		user.setPkOrg(rx.getPkOrg());
		UserContext.setUser(user);
		System.out.println(new Date().getTime() - start);
		Map<String, Object> paramMap = (Map<String, Object>) ApplicationUtils.beanToMap(rx);
		List<Map<String, Object>> schMap = new ArrayList<Map<String, Object>>();
		List<ResAppSchVo> reslist = new ArrayList<ResAppSchVo>();
		try {
			schMap = schPubForWsService.getAppSchInfoForSelf(paramMap);
			System.out.println(new Date().getTime() - start);
			for (Map<String, Object> map : schMap) {
				ResAppSchVo resAppSchVo = ApplicationUtils.mapToBean(map, ResAppSchVo.class);
				reslist.add(resAppSchVo);
			}
		} catch (Exception e) {
			appSchVo.setStatus("-2");
			appSchVo.setDesc("????????????");
			appSchVo.setErrorMessage(e.getMessage());
			result = XmlUtil.beanToXml(appSchVo, appSchVo.getClass(), true);
			return result;
		}
		ApptData apptData = new ApptData();
		ApptDataVo apptDataVo = new ApptDataVo();
		apptDataVo.setApptSchVos(reslist);
		apptData.setApptDataVo(apptDataVo);
		appSchVo.setStatus("0");
		appSchVo.setDesc("??????");
		appSchVo.setErrorMessage("");
		appSchVo.setResAppSchVo(apptData);
		result = XmlUtil.beanToXml(appSchVo, appSchVo.getClass(), true);
		System.out.println(new Date().getTime() - start);
		logger.info(classMathName + "/return/" + JsonUtil.writeValueAsString(result));
		return result;
	}
	/**
	 * ????????????????????????
	 * @param param{pkSch?????????}
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@Override
	public String getTickets(String param) {
		logger.info("getTickets/param/" + JsonUtil.writeValueAsString(param));
		// TODO Auto-generated method stub
		RequestXml rx = (RequestXml) XmlUtil.XmlToBean(RequestXml.class, param);
		Res res = new Res();
		String result = null;
		Schschs schs = new Schschs();
		Schsch sch = null;
		SchTicketSec tiketSec = new SchTicketSec();
		SchTicketSecs tiketSecs = new SchTicketSecs();
		ScheList scheList = new ScheList();
		Data data = new Data();
		Sche sche = new Sche();
		if (rx == null) {
			res.setStatus("-1");
			res.setDesc("??????????????????");
			res.setErrorMessage("");
			result = XmlUtil.beanToXml(res, res.getClass(), true);
			return result;

		}
		if (StringUtil.isEmpty(rx.getPkSch())) {
			res.setStatus("-1");
			res.setDesc("??????????????????pkSch");
			res.setErrorMessage("");
			result = XmlUtil.beanToXml(res, res.getClass(), true);
			return result;
		}
		Map<String, Object> paramMap = (Map<String, Object>) ApplicationUtils.beanToMap(rx);
		List<Map<String, Object>> ticketlist;
		List<Schsch> schschListl = new ArrayList<Schsch>();
		List<SchTicketSec> ticketSecList = new ArrayList<SchTicketSec>();
		List<Sche> schListl = new ArrayList<Sche>();
		try {
			ticketlist = schPubForWsService.getTicketsForSelf(paramMap);
			String type = null;
			for (Map<String, Object> map : ticketlist) {
				type = (String) map.get("type");
				Map<String, Object> secMap = (Map<String, Object>) map.get("schTicketSecs");
				List<SchTicket> schList = (List<SchTicket>) secMap.get("schschs");
				Double price = (Double) secMap.get("price");
				for (SchTicket schMap : schList) {
					sch = new Schsch();
					ApplicationUtils.copyProperties(sch, schMap);
					sch.setPrice(price);
					schschListl.add(sch);
					schs.setSchsch(schschListl);
				}
				tiketSec.setSchschs(schs);
				ticketSecList.add(tiketSec);
				tiketSecs.setSchTicketSec(ticketSecList);
				sche.setSchTicketSecs(tiketSecs);
				sche.setType(type);
				schListl.add(sche);
				scheList.setSche(schListl);
				data.setScheList(scheList);
			}
		} catch (Exception e) {
			res.setStatus("-2");
			res.setDesc("????????????");
			res.setErrorMessage(e.getMessage());
			result = XmlUtil.beanToXml(res, res.getClass(), true);
			return result;
		}
		res.setData(data);
		res.setDesc("??????");
		res.setStatus("0");
		res.setErrorMessage("");
		result = XmlUtil.beanToXml(res, res.getClass(), true);
		logger.info("getTickets/return/" + JsonUtil.writeValueAsString(result));
		return result;
	}
	/**
	 * ??????????????????????????????????????????-??????
	 * @param param{pkSch?????????}
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@Override
	public String getTicketsGroupDate(String param) {
		logger.info("getTickets/param/" + JsonUtil.writeValueAsString(param));
		// TODO Auto-generated method stub
		RequestXml rx = (RequestXml) XmlUtil.XmlToBean(RequestXml.class, param);
		Res res = new Res();
		String result = null;
		Schschs schs = new Schschs();
		Schsch sch = null;
		SchTicketSec tiketSec = new SchTicketSec();
		SchTicketSecs tiketSecs = new SchTicketSecs();
		ScheList scheList = new ScheList();
		Data data = new Data();
		Sche sche = new Sche();
		if (rx == null) {
			res.setStatus("-1");
			res.setDesc("??????????????????");
			res.setErrorMessage("");
			result = XmlUtil.beanToXml(res, res.getClass(), true);
			return result;

		}
		if (StringUtil.isEmpty(rx.getPkSch())) {
			res.setStatus("-1");
			res.setDesc("??????????????????pkSch");
			res.setErrorMessage("");
			result = XmlUtil.beanToXml(res, res.getClass(), true);
			return result;
		}
		Map<String, Object> paramMap = (Map<String, Object>) ApplicationUtils.beanToMap(rx);
		List<Map<String, Object>> ticketlist;
		List<Schsch> schschListl = new ArrayList<Schsch>();
		List<SchTicketSec> ticketSecList = new ArrayList<SchTicketSec>();
		List<Sche> schListl = new ArrayList<Sche>();
		try {
			ticketlist = schPubForWsService.getTicketsGroupDate(paramMap);
			String type = null;
			for (Map<String, Object> map : ticketlist) {
				type = (String) map.get("type");
				Map<String, Object> secMap = (Map<String, Object>) map.get("schTicketSecs");
				List<Map<String, Object>> schList =(List<Map<String, Object>>) secMap.get("schschs");
				Double price = (Double) secMap.get("price");
				for (Map<String, Object> map1: schList) {
					sch = new Schsch();
					//ApplicationUtils.copyProperties(sch, schMap);
					sch.setBeginTimeStr((String)map1.get("beginTimeStr"));
					sch.setEndTimeStr((String)map1.get("endTimeStr"));
					sch.setCntAppt(String.valueOf(map1.get("cntAppt")));
					sch.setPkSch((String)map1.get("pkSch"));
					sch.setPrice(price);
					schschListl.add(sch);
					schs.setSchsch(schschListl);
				}
				tiketSec.setSchschs(schs);
				ticketSecList.add(tiketSec);
				tiketSecs.setSchTicketSec(ticketSecList);
				sche.setSchTicketSecs(tiketSecs);
				sche.setType(type);
				schListl.add(sche);
				scheList.setSche(schListl);
				data.setScheList(scheList);
			}
		} catch (Exception e) {
			res.setStatus("-2");
			res.setDesc("????????????");
			res.setErrorMessage(e.getMessage());
			result = XmlUtil.beanToXml(res, res.getClass(), true);
			return result;
		}
		res.setData(data);
		res.setDesc("??????");
		res.setStatus("0");
		res.setErrorMessage("");
		result = XmlUtil.beanToXml(res, res.getClass(), true);
		logger.info("getTickets/return/" + JsonUtil.writeValueAsString(result));
		return result;
	}
	/**
	 * ????????????????????????--??????????????????
	 * @param param{pkPi,pkSch,ticketNo}
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@Override
	public String saveAppointment(String param) {
		String classMathName = this.getClass().getName() + "/"
				+ Thread.currentThread().getStackTrace()[1].getMethodName();
		logger.info(classMathName + "/saveAppointment/param/" + JsonUtil.writeValueAsString(param));
		// TODO Auto-generated method stub
		RequestXml rx = (RequestXml) XmlUtil.XmlToBean(RequestXml.class, param);
		ResponseApptVo apptVo = new ResponseApptVo();
		ResApptVo resApptVo = new ResApptVo();
		String result = null;
		if (rx == null) {
			apptVo.setStatus("-1");
			apptVo.setDesc("??????????????????");
			apptVo.setErrorMessage("");
			result = XmlUtil.beanToXml(apptVo, apptVo.getClass(), true);
			return result;
		}
		if (rx.getPkSch() == null || "".equals(CommonUtils.getString(rx.getPkSch()))) {
			apptVo.setStatus("-1");
			apptVo.setDesc("??????????????????pkSch");
			apptVo.setErrorMessage("");
			result = XmlUtil.beanToXml(apptVo, apptVo.getClass(), true);
			return result;
		}
		if (rx.getPkPi() == null || "".equals(CommonUtils.getString(rx.getPkPi()))) {
			apptVo.setStatus("-1");
			apptVo.setDesc("??????????????????pkPi");
			apptVo.setErrorMessage("");
			result = XmlUtil.beanToXml(apptVo, apptVo.getClass(), true);
			return result;
		}
		if (rx.getTicketNo() == null || "".equals(CommonUtils.getString(rx.getTicketNo()))) {
			apptVo.setStatus("-1");
			apptVo.setDesc("??????????????????ticketNo");
			apptVo.setErrorMessage("");
			result = XmlUtil.beanToXml(apptVo, apptVo.getClass(), true);
			return result;
		}
		if (rx.getDtApptype()== null || "".equals(CommonUtils.getString(rx.getDtApptype()))) {
			apptVo.setStatus("-1");
			apptVo.setDesc("??????????????????dtApptype");
			apptVo.setErrorMessage("");
			result = XmlUtil.beanToXml(apptVo, apptVo.getClass(), true);
			return result;
		}
		

		Map<String, Object> paramMap = (Map<String, Object>) ApplicationUtils.beanToMap(rx);
		User u = new User();
		PiMaster piMaster = DataBaseHelper.queryForBean("SELECT * FROM pi_master where pk_pi=?", PiMaster.class,
				CommonUtils.getString(rx.getPkPi()));
		u.setPkOrg(piMaster.getPkOrg());
		UserContext.setUser(u);
		Map<String, Object> resultMap = null;
		try {
			resultMap = schPubForWsService.saveAppointment(paramMap);
		} catch (Exception e) {
			apptVo.setStatus("-2");
			apptVo.setDesc("????????????");
			apptVo.setErrorMessage(e.getMessage());
			result = XmlUtil.beanToXml(apptVo, apptVo.getClass(), true);
			return result;
		}
		if ("true".equals(resultMap.get("result"))) {
			resApptVo.setCode((String) resultMap.get("code"));
			resApptVo.setPkSchappt((String) resultMap.get("pkSchappt"));
			apptVo.setStatus("0");
			apptVo.setDesc((String) resultMap.get("message"));
			apptVo.setErrorMessage("");
			apptVo.setData(resApptVo);
		} else {
			apptVo.setStatus("-1");
			apptVo.setDesc((String) resultMap.get("message"));
			apptVo.setErrorMessage("");
		}
		result = XmlUtil.beanToXml(apptVo, apptVo.getClass(), true);
		logger.info(classMathName + "/return/" + JsonUtil.writeValueAsString(result));
		return result;
	}
	/**
	 * ??????????????????-?????????????????????????????????
	 * @param param{pkPi,pkSch,registerDate,registerTime}
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@Override
	public String saveAppointmentGroupDate(String param) {
		String classMathName = this.getClass().getName() + "/"
				+ Thread.currentThread().getStackTrace()[1].getMethodName();
		logger.info(classMathName + "/saveAppointment/param/" + JsonUtil.writeValueAsString(param));
		// TODO Auto-generated method stub
		RequestXml rx = (RequestXml) XmlUtil.XmlToBean(RequestXml.class, param);
		ResponseApptVo apptVo = new ResponseApptVo();
		ResApptVo resApptVo = new ResApptVo();
		String result = null;
		if (rx == null) {
			apptVo.setStatus("-1");
			apptVo.setDesc("??????????????????");
			apptVo.setErrorMessage("");
			result = XmlUtil.beanToXml(apptVo, apptVo.getClass(), true);
			return result;
		}
		if (rx.getPkSch() == null || "".equals(CommonUtils.getString(rx.getPkSch()))) {
			apptVo.setStatus("-1");
			apptVo.setDesc("??????????????????pkSch");
			apptVo.setErrorMessage("");
			result = XmlUtil.beanToXml(apptVo, apptVo.getClass(), true);
			return result;
		}
		if (rx.getPkPi() == null || "".equals(CommonUtils.getString(rx.getPkPi()))) {
			apptVo.setStatus("-1");
			apptVo.setDesc("??????????????????pkPi");
			apptVo.setErrorMessage("");
			result = XmlUtil.beanToXml(apptVo, apptVo.getClass(), true);
			return result;
		}
		if (rx.getRegisterDate() == null || "".equals(CommonUtils.getString(rx.getRegisterDate()))) {
			apptVo.setStatus("-1");
			apptVo.setDesc("??????????????????registerDate");
			apptVo.setErrorMessage("");
			result = XmlUtil.beanToXml(apptVo, apptVo.getClass(), true);
			return result;
		}
		if (rx.getRegisterTime() == null || "".equals(CommonUtils.getString(rx.getRegisterTime()))) {
			apptVo.setStatus("-1");
			apptVo.setDesc("??????????????????registerTime");
			apptVo.setErrorMessage("");
			result = XmlUtil.beanToXml(apptVo, apptVo.getClass(), true);
			return result;
		}
		if (rx.getDtApptype()== null || "".equals(CommonUtils.getString(rx.getDtApptype()))) {
			apptVo.setStatus("-1");
			apptVo.setDesc("??????????????????dtApptype");
			apptVo.setErrorMessage("");
			result = XmlUtil.beanToXml(apptVo, apptVo.getClass(), true);
			return result;
		}
		Map<String, Object> paramMap = (Map<String, Object>) ApplicationUtils.beanToMap(rx);
		User u = new User();
		PiMaster piMaster = DataBaseHelper.queryForBean("SELECT * FROM pi_master where pk_pi=?", PiMaster.class,
				CommonUtils.getString(rx.getPkPi()));
		u.setPkOrg(piMaster.getPkOrg());
		UserContext.setUser(u);
		BdOuDept codeDept = DataBaseHelper.queryForBean("select (select CODE_DEPT from BD_OU_DEPT where PK_DEPT=a.PK_DEPT) code_dept from SCH_SCH a where PK_SCH=?", BdOuDept.class,
				CommonUtils.getString(rx.getPkSch()));
		//??????????????????
		Map<String, Object> cardTypeMap = DataBaseHelper.queryForMap("select val from bd_sysparam where del_flag = '0' and  code='PV0019' and pk_org=?",u.getPkOrg());
		//????????????
		Map<String, Object> codeTypeMap = DataBaseHelper.queryForMap("select val from bd_sysparam where del_flag = '0' and  code='CN0001' and pk_org=?",u.getPkOrg());
		//LbPiMasterRegVo regvo = DataBaseHelper.queryForBean("SELECT * FROM pi_master pi LEFT JOIN PI_INSURANCE hp on hp.pk_pi = pi.pk_pi and hp.del_flag = '0' WHERE pi.del_flag = '0' and pi.pk_pi = ?", LbPiMasterRegVo.class, new Object[]{requ.getPatientId()});
		Map<String, Object> resultMap = null;
		try {
			if(null != codeTypeMap){
				String card[] = codeTypeMap.get("val").toString().split(",");
				for (int i = 0; i < card.length; i++) {
					  if((card[i]).equals(codeDept.getCodeDept())){
						int age = DateUtils.getAge(piMaster.getBirthDate());
						int Maximum = Integer.valueOf(cardTypeMap.get("val").toString());
						if(age-Maximum>0){
							logger.info("??????????????????????????????:"+Maximum+"?????????????????????"+age+"???????????????????????????"+param);
							return XmlUtil.beanToXml(LbSelfUtil.common(Constant.RESFAIL, "??????????????????????????????:"+Maximum+"?????????????????????"+age+"???"), LbSHResponseVo.class, false);
						}
					  }
	
					}
			}
		
			resultMap = schPubForWsService.saveAppointmentGroupDate(paramMap);
		} catch (Exception e) {
			apptVo.setStatus("-2");
			apptVo.setDesc("????????????");
			apptVo.setErrorMessage(e.getMessage());
			result = XmlUtil.beanToXml(apptVo, apptVo.getClass(), true);
			return result;
		}
		if ("true".equals(resultMap.get("result"))) {
			resApptVo.setCode((String) resultMap.get("code"));
			resApptVo.setPkSchappt((String) resultMap.get("pkSchappt"));
			apptVo.setStatus("0");
			apptVo.setDesc((String) resultMap.get("message"));
			apptVo.setErrorMessage("");
			apptVo.setData(resApptVo);
		} else {
			apptVo.setStatus("-1");
			apptVo.setDesc((String) resultMap.get("message"));
			apptVo.setErrorMessage("");
		}
		result = XmlUtil.beanToXml(apptVo, apptVo.getClass(), true);
		logger.info(classMathName + "/return/" + JsonUtil.writeValueAsString(result));
		return result;
	}
	/**
	 * ?????????????????????
	 * <p>Title: getPiCard</p>
	 * <p>Description: </p>
	 * @param param
	 * @return
	 * @see com.zebone.nhis.webservice.cxf.INHISSelfHelpWebService#getPiCard(java.lang.String)
	 */
	@Override
	public String getPiCard(String param) {
		String classMathName = this.getClass().getName() + "/"
				+ Thread.currentThread().getStackTrace()[1].getMethodName();
		logger.info(classMathName + "/param/" + JsonUtil.writeValueAsString(param));
		RequestXml rx = (RequestXml) XmlUtil.XmlToBean(RequestXml.class, param);
		ResponsePiCardVo piCardVo = new ResponsePiCardVo();
		String result = null;
		if (rx == null) {
			piCardVo.setStatus("-1");
			piCardVo.setDesc("??????????????????");
			piCardVo.setErrorMessage("");
			result = XmlUtil.beanToXml(piCardVo, piCardVo.getClass(), true);
			return result;
		}

		if ((rx.getPkPi() == null || "".equals(CommonUtils.getString(rx.getPkPi())))) {

			piCardVo.setStatus("-1");
			piCardVo.setDesc("pkPi????????????");
			piCardVo.setErrorMessage("");
			result = XmlUtil.beanToXml(piCardVo, piCardVo.getClass(), true);
			return result;

		}
		if ((rx.getDtCardtype() == null || "".equals(CommonUtils.getString(rx.getDtCardtype())))) {

			piCardVo.setStatus("-1");
			piCardVo.setDesc("dtCardtype????????????");
			piCardVo.setErrorMessage("");
			result = XmlUtil.beanToXml(piCardVo, piCardVo.getClass(), true);
			return result;

		}
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("pkPi", rx.getPkPi());
		paramMap.put("dtCardtype", rx.getDtCardtype());

		List<Map<String, Object>> piCardList = null;
		ResPiCardVo resPiCardVo = null;

		try {
			piCardList = pvPubForWsService.getPiCard(paramMap);
			Map<String, Object> piCard = piCardList.get(0);
			resPiCardVo = ApplicationUtils.mapToBean(piCard, ResPiCardVo.class);
		} catch (Exception e) {
			piCardVo.setStatus("-2");
			piCardVo.setDesc("????????????");
			piCardVo.setErrorMessage(e.getMessage());
			result = XmlUtil.beanToXml(piCardVo, piCardVo.getClass(), true);
			return result;
		}
		piCardVo.setStatus(EnumerateParameter.ZERO);
		piCardVo.setDesc("??????");
		piCardVo.setErrorMessage("");
		piCardVo.setResPiCardVo(resPiCardVo);

		result = XmlUtil.beanToXml(piCardVo, piCardVo.getClass(), true);
		logger.info(classMathName + "/return/" + JsonUtil.writeValueAsString(result));
		return result;
	}

	/**
	 * 6.2.??????????????????????????????
	 *??????????????????????????????????????????????????????
	 * <p>Title: getPvInfoByIp</p>
	 * <p>Description: </p>
	 * @param param
	 * @return
	 * @see com.zebone.nhis.webservice.cxf.INHISSelfHelpWebService#getPvInfoByIp(java.lang.String)
	 */
	@Override
	public String getPvInfoByIp(String param) {
		String classMathName = this.getClass().getName() + "/"
				+ Thread.currentThread().getStackTrace()[1].getMethodName();
		logger.info(classMathName + "/param/" + JsonUtil.writeValueAsString(param));
		RequestXml rx = (RequestXml) XmlUtil.XmlToBean(RequestXml.class, param);
		ResponseHospInfoVo hospInfoVo = new ResponseHospInfoVo();
		String result = null;
		if (rx == null) {
			hospInfoVo.setStatus("-1");
			hospInfoVo.setDesc("??????????????????");
			hospInfoVo.setErrorMessage("");
			result = XmlUtil.beanToXml(hospInfoVo, hospInfoVo.getClass(), true);
			return result;
		}
		if (rx.getPkPi() == null || "".equals(CommonUtils.getString(rx.getPkPi()))) {
			hospInfoVo.setStatus("-1");
			hospInfoVo.setDesc("??????pkPi????????????");
			hospInfoVo.setErrorMessage("");
			result = XmlUtil.beanToXml(hospInfoVo, hospInfoVo.getClass(), true);
			return result;
		}
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("pkPi", rx.getPkPi());
		if (rx.getCodeIp() != null && !"".equals(CommonUtils.getString(rx.getDateEnd()))) {
			paramMap.put("codeIp", rx.getCodeIp());
		}
		if (rx.getPkDept() != null && !"".equals(CommonUtils.getString(rx.getPkDept()))) {
			paramMap.put("pkDept", rx.getPkDept());
		}
		if (rx.getPkOrg() != null && !"".equals(CommonUtils.getString(rx.getPkOrg()))) {
			paramMap.put("pkOrg", rx.getPkDept());
		}
		if (rx.getEuStatus() != null && !"".equals(CommonUtils.getString(rx.getEuStatus()))) {
			paramMap.put("euStatus", rx.getPkDept());
		}
		List<Map<String, Object>> hospInfoList = null;
		List<ResHospInfoVo> hospInfoVoList = new ArrayList<ResHospInfoVo>();
		ResHospInfoVo resHospInfoVo = null;
		try {
			hospInfoList = pvPubForWsService.getPvInfoByIp(paramMap);
		} catch (Exception e) {
			hospInfoVo.setStatus("-2");
			hospInfoVo.setDesc("????????????");
			hospInfoVo.setErrorMessage(e.getMessage());
			result = XmlUtil.beanToXml(hospInfoVo, hospInfoVo.getClass(), true);
			return result;
		}
		for (Map<String, Object> map : hospInfoList) {
			try {
				resHospInfoVo = ApplicationUtils.mapToBean(map, ResHospInfoVo.class);
				hospInfoVoList.add(resHospInfoVo);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		hospInfoVo.setStatus(EnumerateParameter.ZERO);
		hospInfoVo.setDesc("??????");
		hospInfoVo.setErrorMessage("");
		HospInfoData hospInfoData = new HospInfoData();
		HospInfoDataVo hospInfoDataVo = new HospInfoDataVo();
		hospInfoDataVo.setResHospInfoVo(hospInfoVoList);
		hospInfoData.setHospInfoDataVo(hospInfoDataVo);
		hospInfoVo.setHospInfoData(hospInfoData);

		result = XmlUtil.beanToXml(hospInfoVo, hospInfoVo.getClass(), true);
		logger.info(classMathName + "/return/" + JsonUtil.writeValueAsString(result));
		return result;
	}
	
	/**
	 *@author GuoJing 
	 *???????????????????????????????????????????????????
	 *@param param{pkPi?????? codeOp:????????? pkDept:????????????}
	 *@return 
	 */
	@SuppressWarnings("unchecked")
	@Override
	public String getPvInfoByOp(String param) {
		String classMathName = this.getClass().getName() + "/"
				+ Thread.currentThread().getStackTrace()[1].getMethodName();
		logger.info(classMathName + "/param/" + JsonUtil.writeValueAsString(param));
		RequestXml rx = (RequestXml) XmlUtil.XmlToBean(RequestXml.class, param);
		ResponsePvInfoVo pvInfoVo = new ResponsePvInfoVo();
		String result = null;
		if (rx == null) {
			pvInfoVo.setStatus("-1");
			pvInfoVo.setDesc("??????????????????");
			pvInfoVo.setErrorMessage("");
			result = XmlUtil.beanToXml(pvInfoVo, pvInfoVo.getClass(), true);
			return result;
		}
		if (rx.getPkPi() == null || "".equals(CommonUtils.getString(rx.getPkPi()))) {
			pvInfoVo.setStatus("-1");
			pvInfoVo.setDesc("??????????????????pkPi");
			pvInfoVo.setErrorMessage("");
			result = XmlUtil.beanToXml(pvInfoVo, pvInfoVo.getClass(), true);
			return result;
		}
		Map<String, Object> paramMap = (Map<String, Object>) ApplicationUtils.beanToMap(rx);
		List<ResPvInfoVo> list = new ArrayList<ResPvInfoVo>();
		List<Map<String, Object>> pvInfoByOp = null;
		ResPvInfoVo resPvInfoVo = null;
		try {
			pvInfoByOp = pvPubForWsService.getPvInfoByOp(paramMap);
			for (Map<String, Object> map : pvInfoByOp) {
				resPvInfoVo = ApplicationUtils.mapToBean(map, ResPvInfoVo.class);
				list.add(resPvInfoVo);
			}
		} catch (Exception e) {
			pvInfoVo.setStatus("-2");
			pvInfoVo.setDesc("????????????");
			pvInfoVo.setErrorMessage(e.getMessage());
			result = XmlUtil.beanToXml(pvInfoVo, pvInfoVo.getClass(), true);
			return result;
		}
		PvInfoData pvInfoData = new PvInfoData();
		PvInfoDataVo pvInfoDataVo = new PvInfoDataVo();
		pvInfoDataVo.setResPvInfoVos(list);
		pvInfoData.setPvInfoDataVo(pvInfoDataVo);
		pvInfoVo.setDatalist(pvInfoData);
		pvInfoVo.setStatus("0");
		pvInfoVo.setDesc("??????");
		pvInfoVo.setErrorMessage("");
		result = XmlUtil.beanToXml(pvInfoVo, pvInfoVo.getClass(), true);
		logger.info(classMathName + "/return/" + JsonUtil.writeValueAsString(result));
		return result;
	}

    /**
     *????????????????????????
     * @param param
     * @return
     *
     */
	@SuppressWarnings("unchecked")
	@Override
	public String getUnpaidFeeByOp(String param) {
        Map<String, Object> paramMap = JsonUtil.readValue(param, Map.class);
        if (paramMap==null){
            return  CommonUtils.getString(new RespJson("-1|????????????????????????????????????", true));
        }
        if (paramMap.get("pkPi")==null || "".equals(paramMap.get("pkPi"))){
            return  CommonUtils.getString(new RespJson("-1|???????????????????????????!", true));
        }
        if (paramMap.get("pkPv")==null || "".equals(paramMap.get("pkPi"))){
            return  CommonUtils.getString(new RespJson("-1|???????????????????????????!", true));
        }
        if (paramMap.get("pkOrg")==null || "".equals(paramMap.get("pkPi"))){
            return  CommonUtils.getString(new RespJson("-1|???????????????????????????!", true));
        }
        List<Map<String, Object>> unpaidFeeByOp = blPubForWsService.getUnpaidFeeByOp(paramMap);
		return  CommonUtils.getString(new RespJson("0|??????|" +ApplicationUtils.objectToJson(unpaidFeeByOp),true));
	}

	/**
	 * 7.3.????????????????????????
	??????????????????????????????????????????????????????
	 * <p>Title: getPaidFeeByOp</p>
	 * <p>Description: </p>
	 * @param param
	 * @return
	 * @see com.zebone.nhis.webservice.cxf.INHISSelfHelpWebService#getPaidFeeByOp(java.lang.String)
	 */
	@Override
	public String getPaidFeeByOp(String param) {
		String classMathName = this.getClass().getName() + "/"
				+ Thread.currentThread().getStackTrace()[1].getMethodName();
		logger.info(classMathName + "/param/" + JsonUtil.writeValueAsString(param));
		RequestXml rx = (RequestXml) XmlUtil.XmlToBean(RequestXml.class, param);
		ResponsePaidFeeVo paidFeeVo = new ResponsePaidFeeVo();
		String result = null;
		if (rx == null) {
			paidFeeVo.setStatus("-1");
			paidFeeVo.setDesc("??????????????????");
			paidFeeVo.setErrorMessage("");
			result = XmlUtil.beanToXml(paidFeeVo, paidFeeVo.getClass(), true);
			return result;
		}
		if (rx.getPkPi() == null || "".equals(CommonUtils.getString(rx.getPkPi()))) {
			paidFeeVo.setStatus("-1");
			paidFeeVo.setDesc("??????pkPi????????????");
			paidFeeVo.setErrorMessage("");
			result = XmlUtil.beanToXml(paidFeeVo, paidFeeVo.getClass(), true);
			return result;
		}
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("pkPi", rx.getPkPi());
		paramMap.put("dateBegin", rx.getDateBegin());
		paramMap.put("dateEnd", rx.getDateEnd());
		List<Map<String, Object>> paidFeeList = null;
		List<ResPaidFeeVo> resPaidFeeVoList = new ArrayList<ResPaidFeeVo>();
		ResPaidFeeVo resPaidFeeVo = null;
		try {
			paidFeeList = blPubForWsService.getPaidFeeByOp(paramMap);
		} catch (Exception e) {
			paidFeeVo.setStatus("-2");
			paidFeeVo.setDesc("????????????");
			paidFeeVo.setErrorMessage(e.getMessage());
			result = XmlUtil.beanToXml(paidFeeVo, paidFeeVo.getClass(), true);
			return result;
		}
		for (Map<String, Object> map : paidFeeList) {
			try {
				resPaidFeeVo = ApplicationUtils.mapToBean(map, ResPaidFeeVo.class);
				resPaidFeeVoList.add(resPaidFeeVo);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		paidFeeVo.setStatus(EnumerateParameter.ZERO);
		paidFeeVo.setDesc("??????");
		paidFeeVo.setErrorMessage("");
		PaidFeeData paidFeeData = new PaidFeeData();
		PaidFeeDataVo paidFeeDataVo = new PaidFeeDataVo();
		paidFeeDataVo.setResPaidFeeVo(resPaidFeeVoList);
		paidFeeData.setPaidFeeDataVo(paidFeeDataVo);
		paidFeeVo.setPaidFeeData(paidFeeData);

		result = XmlUtil.beanToXml(paidFeeVo, paidFeeVo.getClass(), true);
		logger.info(classMathName + "/return/" + JsonUtil.writeValueAsString(result));
		return result;
	}


    /**
     * ????????????????????????
     * @param param
     * @return
     */
	@SuppressWarnings("unchecked")
	@Override
	public String getPayDetailByOp(String param) {
		String classMathName = this.getClass().getName() + "/"
				+ Thread.currentThread().getStackTrace()[1].getMethodName();
		logger.info(classMathName + "/param/" + JsonUtil.writeValueAsString(param));
		// TODO Auto-generated method stub
		RequestXml rx = (RequestXml) XmlUtil.XmlToBean(RequestXml.class, param);
		ResponsePaidVo paidVo = new ResponsePaidVo();
		String result = null;
		if (rx == null) {
			paidVo.setStatus("-1");
			paidVo.setDesc("??????????????????");
			paidVo.setErrorMessage("");
			result = XmlUtil.beanToXml(paidVo, paidVo.getClass(), true);
			return result;
		}
		if (rx.getPkSettle() == null || "".equals(CommonUtils.getString(rx.getPkSettle()))) {
			paidVo.setStatus("-1");
			paidVo.setDesc("??????????????????pkSettle");
			paidVo.setErrorMessage("");
			result = XmlUtil.beanToXml(paidVo, paidVo.getClass(), true);
			return result;
		}
		Map<String, Object> paramMap = (Map<String, Object>) ApplicationUtils.beanToMap(rx);
		List<ResPaidVo> listVo = new ArrayList<ResPaidVo>();
		List<Map<String, Object>> payDetailByOp = null;
		try {
			payDetailByOp = blPubForWsService.getPayDetailByOp(paramMap);

			ResPaidVo resPaidVo = null;
			for (Map<String, Object> map : payDetailByOp) {
				resPaidVo = ApplicationUtils.mapToBean(map, ResPaidVo.class);
				listVo.add(resPaidVo);
			}
		} catch (Exception e) {
			paidVo.setStatus("-2");
			paidVo.setDesc("????????????");
			paidVo.setErrorMessage(e.getMessage());
			result = XmlUtil.beanToXml(paidVo, paidVo.getClass(), true);
			return result;
		}
		PaidData paidData = new PaidData();
		PaidDataVo dataVo = new PaidDataVo();
		dataVo.setResPaidVos(listVo);
		paidData.setPaidDataVo(dataVo);
		paidVo.setDatalist(paidData);
		paidVo.setStatus("0");
		paidVo.setDesc("??????");
		paidVo.setErrorMessage("");
		result = XmlUtil.beanToXml(paidVo, paidVo.getClass(), true);
		logger.info(classMathName + "/return/" + JsonUtil.writeValueAsString(result));
		return result;
	}

	@Override
	public String cardRecharge(String param) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String prePayRecharge(String param) {
		String classMathName = this.getClass().getName() + "/"
				+ Thread.currentThread().getStackTrace()[1].getMethodName();
		logger.info(classMathName + "/param/" + JsonUtil.writeValueAsString(param));
		ResponsePreChargeVo chargeVo = new ResponsePreChargeVo();
		String result = null;
		/**************2019-11-21??????6?????????1135*************************************************************/
		chargeVo.setStatus("-1");
		chargeVo.setDesc("??????????????????????????????????????????????????????????????????");
		chargeVo.setErrorMessage("");
		result = XmlUtil.beanToXml(chargeVo, chargeVo.getClass(), true);
		return result;
	}

	/**
	 * 7.14.?????????????????????????????????
	 * <p>Title: getPrePayDetail</p>
	 * <p>Description: </p>
	 * @param param
	 * @return
	 * @see com.zebone.nhis.webservice.cxf.INHISSelfHelpWebService#getPrePayDetail(java.lang.String)
	 */
	@Override
	public String getPrePayDetail(String param) {
		String classMathName = this.getClass().getName() + "/"
				+ Thread.currentThread().getStackTrace()[1].getMethodName();
		logger.info(classMathName + "/param/" + JsonUtil.writeValueAsString(param));
		RequestXml rx = (RequestXml) XmlUtil.XmlToBean(RequestXml.class, param);
		ResponsePrePayVo prePayVo = new ResponsePrePayVo();
		String result = null;
		if (rx == null) {
			prePayVo.setStatus("-1");
			prePayVo.setDesc("??????????????????");
			prePayVo.setErrorMessage("");
			result = XmlUtil.beanToXml(prePayVo, prePayVo.getClass(), true);
			return result;
		}
		if (rx.getPkPi() == null || "".equals(CommonUtils.getString(rx.getPkPi()))) {
			prePayVo.setStatus("-1");
			prePayVo.setDesc("??????pkPi????????????");
			prePayVo.setErrorMessage("");
			result = XmlUtil.beanToXml(prePayVo, prePayVo.getClass(), true);
			return result;
		}
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("pkPi", rx.getPkPi());
		List<Map<String, Object>> prePayList = null;
		List<ResPrePayVo> resPrePayVoList = new ArrayList<ResPrePayVo>();
		ResPrePayVo resPrePayVo = null;
		try {
			prePayList = blPubForWsService.getPrePayDetail(paramMap);
		} catch (Exception e) {
			prePayVo.setStatus("-2");
			prePayVo.setDesc("????????????");
			prePayVo.setErrorMessage(e.getMessage());
			result = XmlUtil.beanToXml(prePayVo, prePayVo.getClass(), true);
			return result;
		}
		for (Map<String, Object> map : prePayList) {
			try {
				resPrePayVo = ApplicationUtils.mapToBean(map, ResPrePayVo.class);
				resPrePayVoList.add(resPrePayVo);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		prePayVo.setStatus(EnumerateParameter.ZERO);
		prePayVo.setDesc("??????");
		prePayVo.setErrorMessage("");
		PrePayData prePayData = new PrePayData();
		PrePayDataVo prePayDataVo = new PrePayDataVo();
		prePayDataVo.setResPrePayVo(resPrePayVoList);
		prePayData.setPrePayDataVo(prePayDataVo);
		prePayVo.setPrePayData(prePayData);

		result = XmlUtil.beanToXml(prePayVo, prePayVo.getClass(), true);
		logger.info(classMathName + "/return/" + JsonUtil.writeValueAsString(result));
		return result;
	}
    /**
     *???????????????????????????????????????
     * @author wuqiang
     * @date 2018/9/11
     * @param param pkPi:??????????????????
     * @return java.lang.String
     */
	@SuppressWarnings("unchecked")
	@Override
	public String getDepositInfo(String param) {
		String classMathName = this.getClass().getName() + "/"
				+ Thread.currentThread().getStackTrace()[1].getMethodName();
		logger.info(classMathName + "/param/" + JsonUtil.writeValueAsString(param));
		RequestXml rx = (RequestXml) XmlUtil.XmlToBean(RequestXml.class, param);
		ResponseDepositeVo depositeVo = new ResponseDepositeVo();
		String result = null;
		if (rx == null) {
			depositeVo.setStatus("-1");
			depositeVo.setDesc("??????????????????");
			depositeVo.setErrorMessage("");
			result = XmlUtil.beanToXml(depositeVo, depositeVo.getClass(), true);
			return result;
		}
		if (rx.getPkPi() == null || "".equals(CommonUtils.getString(rx.getPkPi()))) {
			depositeVo.setStatus("-1");
			depositeVo.setDesc("??????pkPi????????????");
			depositeVo.setErrorMessage("");
			result = XmlUtil.beanToXml(depositeVo, depositeVo.getClass(), true);
			return result;
		}
		Map<String, Object> paramMap = (Map<String, Object>) ApplicationUtils.beanToMap(rx);
		List<ResDepositeVo> depoList = new ArrayList<ResDepositeVo>();
		ResDepositeVo resDepositeVo = null;
		List<Map<String, Object>> list = null;
		try {
			list = blPubForWsService.getDepositInfo(paramMap);
			for (Map<String, Object> map : list) {
				resDepositeVo = ApplicationUtils.mapToBean(map, ResDepositeVo.class);
				depoList.add(resDepositeVo);
			}
		} catch (Exception e) {
			depositeVo.setStatus("-2");
			depositeVo.setDesc("????????????");
			depositeVo.setErrorMessage(e.getMessage());
			result = XmlUtil.beanToXml(depositeVo, depositeVo.getClass(), true);
			return result;
		}
		DepositeData depoData = new DepositeData();
		DepositeDataVo depoDataVo = new DepositeDataVo();
		depoDataVo.setResDepositeVo(depoList);
		depoData.setDepositeDateVo(depoDataVo);
		depositeVo.setDatalist(depoData);
		depositeVo.setStatus("0");
		depositeVo.setDesc("??????");
		depositeVo.setErrorMessage("");
		result = XmlUtil.beanToXml(depositeVo, depositeVo.getClass(), true);
		logger.info(classMathName + "/return/" + JsonUtil.writeValueAsString(result));
		return result;
	}
	/**
	 * ??????????????????
	 * @param {pkSchappt?????????}
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@Override
	public String cancelAppointment(String param) {
		String classMathName = this.getClass().getName() + "/"
				+ Thread.currentThread().getStackTrace()[1].getMethodName();
		logger.info(classMathName + "/cancelAppointment/param/" + JsonUtil.writeValueAsString(param));
		// TODO Auto-generated method stub
		RequestXml rx = (RequestXml) XmlUtil.XmlToBean(RequestXml.class, param);
		ResponseApptVo apptVo = new ResponseApptVo();
		ResApptVo resApptVo = new ResApptVo();
		String result = null;
		if (rx == null) {
			apptVo.setStatus("-1");
			apptVo.setErrorMessage("");
			apptVo.setDesc("??????????????????");
			result = XmlUtil.beanToXml(apptVo, apptVo.getClass(), true);
			return result;
		}
		if (rx.getPkSchappt() == null || "".equals(CommonUtils.getString(rx.getPkPi()))) {
			apptVo.setStatus("-1");
			apptVo.setErrorMessage("");
			apptVo.setDesc("??????????????????pkSchappt");
			result = XmlUtil.beanToXml(apptVo, apptVo.getClass(), true);
			return result;
		}
		Map<String, Object> paramMap = (Map<String, Object>) ApplicationUtils.beanToMap(rx);
		Map<String, Object> resultMap = null;
		try {
			resultMap = schPubForWsService.cancelAppointment(paramMap);
		} catch (Exception e) {
			apptVo.setStatus("-2");
			apptVo.setDesc("????????????");
			apptVo.setErrorMessage(e.getMessage());
			result = XmlUtil.beanToXml(apptVo, apptVo.getClass(), true);
			return result;
		}
		if ("true".equals(resultMap.get("result"))) {
			resApptVo.setPkSchappt((String) resultMap.get("pkSchappt"));
			apptVo.setData(resApptVo);
			apptVo.setStatus("0");
			apptVo.setDesc((String) resultMap.get("message"));
			apptVo.setErrorMessage("");
		} else {
			apptVo.setDesc((String) resultMap.get("message"));
			apptVo.setStatus("-1");
			apptVo.setErrorMessage("");
		}
		result = XmlUtil.beanToXml(apptVo, apptVo.getClass(), true);
		logger.info(classMathName + "/return/" + JsonUtil.writeValueAsString(result));
		return result;
	}
	/**
	 * 7.15.????????????????????????
	 * <p>Title: getIpCgDetail</p>
	 * <p>Description: </p>
	 * @param param
	 * @return
	 * @see com.zebone.nhis.webservice.cxf.INHISSelfHelpWebService#getIpCgDetail(java.lang.String)
	 */
	@Override
	public String getIpCgDetail(String param) {
		String classMathName = this.getClass().getName() + "/"
				+ Thread.currentThread().getStackTrace()[1].getMethodName();
		logger.info(classMathName + "/param/" + JsonUtil.writeValueAsString(param));
		RequestXml rx = (RequestXml) XmlUtil.XmlToBean(RequestXml.class, param);
		ResponseIpCgVo ipCgVo = new ResponseIpCgVo();
		String result = null;
		if (rx == null) {
			ipCgVo.setStatus("-1");
			ipCgVo.setDesc("??????????????????");
			ipCgVo.setErrorMessage("");
			result = XmlUtil.beanToXml(ipCgVo, ipCgVo.getClass(), true);
			return result;
		}
		if (rx.getPkPi() == null || "".equals(CommonUtils.getString(rx.getPkPi()))) {
			ipCgVo.setStatus("-1");
			ipCgVo.setDesc("??????pkPi????????????");
			ipCgVo.setErrorMessage("");
			result = XmlUtil.beanToXml(ipCgVo, ipCgVo.getClass(), true);
			return result;
		}
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("pkPi", rx.getPkPi());
		List<Map<String, Object>> ipCgList = null;
		List<ResIpCgVo> resIpCgVoList = new ArrayList<ResIpCgVo>();
		ResIpCgVo resIpCgVo = null;
		try {
			ipCgList = blPubForWsService.getIpCgDetail(paramMap);
		} catch (Exception e) {
			ipCgVo.setStatus("-2");
			ipCgVo.setDesc("????????????");
			ipCgVo.setErrorMessage(e.getMessage());
			result = XmlUtil.beanToXml(ipCgVo, ipCgVo.getClass(), true);
			return result;
		}
		for (Map<String, Object> map : ipCgList) {
			try {
				resIpCgVo = ApplicationUtils.mapToBean(map, ResIpCgVo.class);
				resIpCgVoList.add(resIpCgVo);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		ipCgVo.setStatus(EnumerateParameter.ZERO);
		ipCgVo.setDesc("??????");
		ipCgVo.setErrorMessage("");
		IpCgData ipCgData = new IpCgData();
		IpCgDataVo ipCgDataVo = new IpCgDataVo();
		ipCgDataVo.setResIpCgVo(resIpCgVoList);
		ipCgData.setIpCgDataVo(ipCgDataVo);
		ipCgVo.setIpCgData(ipCgData);

		result = XmlUtil.beanToXml(ipCgVo, ipCgVo.getClass(), true);
		logger.info(classMathName + "/return/" + JsonUtil.writeValueAsString(result));
		return result;
	}

	/**
	 * ????????????????????????(?????????)
	 * @author kongxuedong
	 * @return java.lang.String
	 */
	@SuppressWarnings("unchecked")
	@Override
	public String getHpInfo(String param) {
		String classMathName = this.getClass().getName() + "/"
				+ Thread.currentThread().getStackTrace()[1].getMethodName();
		logger.info(classMathName + "/param/" + JsonUtil.writeValueAsString(param));
		// TODO Auto-generated method stub
		RequestXml rx = (RequestXml) XmlUtil.XmlToBean(RequestXml.class, param);
		ResponseHpVo responseHpVo = new ResponseHpVo();
		HpData hpData = new HpData();
		String result = null;
		if (rx == null) {
			responseHpVo.setStatus("-1");
			responseHpVo.setDesc("??????????????????");
			responseHpVo.setErrorMessage("");
			result = XmlUtil.beanToXml(responseHpVo, responseHpVo.getClass(), true);
			return result;
		}
		if ((rx.getFlagOp() == null || "".equals(CommonUtils.getString(rx.getFlagOp())))) {
			responseHpVo.setStatus("-1");
			responseHpVo.setDesc("??????????????????flagOp");
			responseHpVo.setErrorMessage("");
			result = XmlUtil.beanToXml(responseHpVo, responseHpVo.getClass(), true);
			return result;
		}
		Map<String, Object> paramMap = (Map<String, Object>) ApplicationUtils.beanToMap(rx);
		List<String> hptype = new ArrayList<String>();
		hptype.add("0");
		hptype.add("4");
		hptype.add("9");
		paramMap.put("euHptypes", hptype);
		List<Map<String, Object>> hpinfolist = null;
		List<ResHpVo> resHpList = new ArrayList<ResHpVo>();
		ResHpVo resHpVo = null;
		try {
			hpinfolist = bdPubForWsService.getHpInfo(paramMap);
			for (Map<String, Object> map : hpinfolist) {
				resHpVo = ApplicationUtils.mapToBean(map, ResHpVo.class);
				resHpList.add(resHpVo);
			}
		} catch (Exception e) {
			responseHpVo.setStatus("-2");
			responseHpVo.setDesc("????????????");
			responseHpVo.setErrorMessage(e.getMessage());
			result = XmlUtil.beanToXml(responseHpVo, responseHpVo.getClass(), true);
			return result;
		}
		HpDataVo hpDataVo = new HpDataVo();
		hpDataVo.setResHpVo(resHpList);
		hpData.setHpDataVo(hpDataVo);
		responseHpVo.setDatalist(hpData);
		responseHpVo.setDesc("??????");
		responseHpVo.setErrorMessage("");
		responseHpVo.setStatus("0");
		result = XmlUtil.beanToXml(responseHpVo, responseHpVo.getClass(), true);
		logger.info(classMathName + "/return/" + JsonUtil.writeValueAsString(result));
		return result;
	}
	/**
	 * ????????????????????????
	 * @param  {pkOrg?????????}
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@Override
	public String getSchInfo(String param) {
		Map<String, Object> paramMap = JsonUtil.readValue(param,Map.class);
		if(paramMap == null)
			return  CommonUtils.getString(new RespJson("-1|???????????????????????????????????????", true));
		if(paramMap.get("pkOrg")==null||"".equals(CommonUtils.getString(paramMap.get("pkOrg"))))
		    return  CommonUtils.getString(new RespJson("-1|???????????????????????????????????????pkOrg", true));
		
		List<Map<String,Object>> result = schPubForWsService.getSchInfo(CommonUtils.getString(paramMap.get("pkOrg")));
		return CommonUtils.getString(new RespJson("0|??????|"+ApplicationUtils.objectToJson(result), true));
	}
	/**
	****??????????????????
		pkInsu????????????????????????
		pkPi?????????????????????
		pkPicate???????????????
		pkRes???????????????????????????
		pkSch?????????????????????
		pkSchsrv???????????????????????????
		pkOrg?????????????????????
		accountReceivable???????????????
		cardNo?????????????????????????????????????????????
	 */
	@SuppressWarnings("unchecked")
	@Override
	public String saveRegister(String param) {
		Map<String, Object> paramMap = JsonUtil.readValue(param,Map.class);
		if(paramMap == null)
			return  CommonUtils.getString(new RespJson("-1|?????????????????????????????????", true));
		if(paramMap.get("pkOrg")==null||"".equals(CommonUtils.getString(paramMap.get("pkOrg"))))
		    return  CommonUtils.getString(new RespJson("-1|?????????????????????????????????pkOrg", true));
		if(paramMap.get("pkInsu")==null||"".equals(CommonUtils.getString(paramMap.get("pkInsu"))))
		    return  CommonUtils.getString(new RespJson("-1|?????????????????????????????????pkInsu", true));
		if(paramMap.get("pkPi")==null||"".equals(CommonUtils.getString(paramMap.get("pkPi"))))
		    return  CommonUtils.getString(new RespJson("-1|?????????????????????????????????pkPi", true));
		if(paramMap.get("pkPicate")==null||"".equals(CommonUtils.getString(paramMap.get("pkPicate"))))
		    return  CommonUtils.getString(new RespJson("-1|?????????????????????????????????pkPicate", true));
		if(paramMap.get("pkRes")==null||"".equals(CommonUtils.getString(paramMap.get("pkRes"))))
		    return  CommonUtils.getString(new RespJson("-1|?????????????????????????????????pkRes", true));
		if(paramMap.get("pkSch")==null||"".equals(CommonUtils.getString(paramMap.get("pkSch"))))
		    return  CommonUtils.getString(new RespJson("-1|?????????????????????????????????pkSch", true));
		if(paramMap.get("pkSchsrv")==null||"".equals(CommonUtils.getString(paramMap.get("pkSchsrv"))))
		    return  CommonUtils.getString(new RespJson("-1|?????????????????????????????????pkSchsrv", true));
		if(paramMap.get("accountReceivable")==null||"".equals(CommonUtils.getString(paramMap.get("accountReceivable"))))
		    return  CommonUtils.getString(new RespJson("-1|?????????????????????????????????accountReceivable", true));
		List<Map<String,Object>> result = pvPubForWsService.saveRegister(paramMap);
		return CommonUtils.getString(new RespJson("0|??????|"+ApplicationUtils.objectToJson(result), true));
	}
	/**
	 * 7.16.????????????????????????
	 * <p>Title: getIpCgDayDetail</p>
	 * <p>Description: </p>
	 * @param param
	 * @return
	 * @see com.zebone.nhis.webservice.cxf.INHISSelfHelpWebService#getIpCgDayDetail(java.lang.String)
	 */
	@Override
	public String getIpCgDayDetail(String param) {
		String classMathName = this.getClass().getName() + "/"
				+ Thread.currentThread().getStackTrace()[1].getMethodName();
		logger.info(classMathName + "/param/" + JsonUtil.writeValueAsString(param));
		RequestXml rx = (RequestXml) XmlUtil.XmlToBean(RequestXml.class, param);
		ResponseIpCgDayVo ipCgDayVo = new ResponseIpCgDayVo();
		String result = null;
		if (rx == null) {
			ipCgDayVo.setStatus("-1");
			ipCgDayVo.setDesc("??????????????????");
			ipCgDayVo.setErrorMessage("");
			result = XmlUtil.beanToXml(ipCgDayVo, ipCgDayVo.getClass(), true);
			return result;
		}
		if (rx.getPkPi() == null || "".equals(CommonUtils.getString(rx.getPkPi()))) {
			ipCgDayVo.setStatus("-1");
			ipCgDayVo.setDesc("??????pkPi????????????");
			ipCgDayVo.setErrorMessage("");
			result = XmlUtil.beanToXml(ipCgDayVo, ipCgDayVo.getClass(), true);
			return result;
		}
		if (rx.getDateCg() == null || "".equals(CommonUtils.getString(rx.getDateCg()))) {
			ipCgDayVo.setStatus("-1");
			ipCgDayVo.setDesc("??????dateCg????????????");
			ipCgDayVo.setErrorMessage("");
			result = XmlUtil.beanToXml(ipCgDayVo, ipCgDayVo.getClass(), true);
			return result;
		}
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("pkPi", rx.getPkPi());
		paramMap.put("dateCg", rx.getDateCg());
		List<Map<String, Object>> ipCgDayList = null;
		List<ResIpCgDayVo> resIpCgDayVoList = new ArrayList<ResIpCgDayVo>();
		ResIpCgDayVo resIpCgDayVo = null;
		try {
			ipCgDayList = blPubForWsService.getIpCgDayDetail(paramMap);
		} catch (Exception e) {
			ipCgDayVo.setStatus("-2");
			ipCgDayVo.setDesc("????????????");
			ipCgDayVo.setErrorMessage(e.getMessage());
			result = XmlUtil.beanToXml(ipCgDayVo, ipCgDayVo.getClass(), true);
			return result;
		}
		for (Map<String, Object> map : ipCgDayList) {
			try {
				resIpCgDayVo = ApplicationUtils.mapToBean(map, ResIpCgDayVo.class);
				resIpCgDayVoList.add(resIpCgDayVo);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		ipCgDayVo.setStatus(EnumerateParameter.ZERO);
		ipCgDayVo.setDesc("??????");
		ipCgDayVo.setErrorMessage("");
		IpCgDayData ipCgDayData = new IpCgDayData();
		IpCgDayDataVo ipCgDayDataVo = new IpCgDayDataVo();
		ipCgDayDataVo.setResIpCgDayVo(resIpCgDayVoList);
		ipCgDayData.setIpCgDayDataVo(ipCgDayDataVo);
		ipCgDayVo.setIpCgCgDayData(ipCgDayData);

		result = XmlUtil.beanToXml(ipCgDayVo, ipCgDayVo.getClass(), true);
		logger.info(classMathName + "/return/" + JsonUtil.writeValueAsString(result));
		return result;
	}

	/**
	****?????????????????????
		pkInsu????????????????????????
		pkPi?????????????????????
		pkPicate???????????????
		pkRes???????????????????????????
		pkSch?????????????????????
		pkSchsrv???????????????????????????
		pkOrg?????????????????????
		cardNo?????????????????????????????????????????????
	 */
	@SuppressWarnings("unchecked")
	@Override
	public String preRegister(String param) {
		Map<String, Object> paramMap = JsonUtil.readValue(param,Map.class);
		if(paramMap == null)
			return  CommonUtils.getString(new RespJson("-1|?????????????????????????????????", true));
		if(paramMap.get("pkOrg")==null||"".equals(CommonUtils.getString(paramMap.get("pkOrg"))))
		    return  CommonUtils.getString(new RespJson("-1|?????????????????????????????????pkOrg", true));
		if(paramMap.get("pkInsu")==null||"".equals(CommonUtils.getString(paramMap.get("pkInsu"))))
		    return  CommonUtils.getString(new RespJson("-1|?????????????????????????????????pkInsu", true));
		if(paramMap.get("pkPi")==null||"".equals(CommonUtils.getString(paramMap.get("pkPi"))))
		    return  CommonUtils.getString(new RespJson("-1|?????????????????????????????????pkPi", true));
		if(paramMap.get("pkPicate")==null||"".equals(CommonUtils.getString(paramMap.get("pkPicate"))))
		    return  CommonUtils.getString(new RespJson("-1|?????????????????????????????????pkPicate", true));
		if(paramMap.get("pkRes")==null||"".equals(CommonUtils.getString(paramMap.get("pkRes"))))
		    return  CommonUtils.getString(new RespJson("-1|?????????????????????????????????pkRes", true));
		if(paramMap.get("pkSch")==null||"".equals(CommonUtils.getString(paramMap.get("pkSch"))))
		    return  CommonUtils.getString(new RespJson("-1|?????????????????????????????????pkSch", true));
		if(paramMap.get("pkSchsrv")==null||"".equals(CommonUtils.getString(paramMap.get("pkSchsrv"))))
		    return  CommonUtils.getString(new RespJson("-1|?????????????????????????????????pkSchsrv", true));
		Map<String,Object> result = pvPubForWsService.preRegister(paramMap);
		return CommonUtils.getString(new RespJson("0|??????|"+ApplicationUtils.objectToJson(result), true));
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
	
	/**
     * ??????????????????????????????????????????
     *
     * @param param pkDept ??????
     * @return java.lang.String
     * @author wuqiang
     * @date 2018/9/19
     */
    @Override
    public String getDeptSchInfo(String param) {
    	String classMathName = this.getClass().getName() + "/"
				+ Thread.currentThread().getStackTrace()[1].getMethodName();
		logger.info(classMathName + "/param/" + JsonUtil.writeValueAsString(param));
		RequestXml rx = (RequestXml) XmlUtil.XmlToBean(RequestXml.class, param);
		ResponseSchInfoVo schInfoVo = new ResponseSchInfoVo();
		String result = null;
		if (rx == null) {
			schInfoVo.setStatus("-1");
			schInfoVo.setDesc("??????????????????");
			schInfoVo.setErrorMessage("");
			result = XmlUtil.beanToXml(schInfoVo, schInfoVo.getClass(), true);
			return result;
		}
		if (StringUtils.isEmpty(rx.getPkDept()) && StringUtils.isEmpty(rx.getPkOrg())) {
			schInfoVo.setStatus("-1");
			schInfoVo.setDesc("pkDept???pkOrg??????????????????");
			schInfoVo.setErrorMessage("");
			result = XmlUtil.beanToXml(schInfoVo, schInfoVo.getClass(), true);
			return result;

		}
		Map<String, Object> paramMap = new HashMap<String, Object>();
		if (!StringUtils.isEmpty(rx.getPkDept())) {
			paramMap.put("pkDept", rx.getPkDept());
		}
		if (!StringUtils.isEmpty(rx.getPkOrg())) {
			paramMap.put("pkOrg", rx.getPkOrg());
		}
		if (!StringUtils.isEmpty(rx.getPkOrgarea())) {
			paramMap.put("pkOrgarea", rx.getPkOrgarea());
		}
		paramMap.put("euSrvtype", rx.getEuSrvtype());
		String dateBegin;
		if (rx.getDateBegin().length() > 9) {
			dateBegin = rx.getDateBegin().substring(0, 10);
		} else {
			dateBegin = rx.getDateBegin();
		}
		paramMap.put("dateBegin", dateBegin);
		String dateEnd;
		if (rx.getDateEnd().length() > 9) {
			dateEnd = rx.getDateEnd().substring(0, 10);
		} else {
			dateEnd = rx.getDateEnd();
		}
		paramMap.put("dateEnd", dateEnd);

		List<Map<String, Object>> schInfoList = null;
		List<ResSchInfoVo> schInfoVoList = new ArrayList<ResSchInfoVo>();
		ResSchInfoVo resSchInfoVo = null;

		try {
			schInfoList = blPubForWsService.getDeptSchInfo(paramMap);
		} catch (Exception e) {
			schInfoVo.setStatus("-2");
			schInfoVo.setDesc("????????????");
			schInfoVo.setErrorMessage(e.getMessage());
			result = XmlUtil.beanToXml(schInfoVo, schInfoVo.getClass(), true);
			return result;
		}
		for (Map<String, Object> map : schInfoList) {
			try {
				resSchInfoVo = ApplicationUtils.mapToBean(map, ResSchInfoVo.class);
				schInfoVoList.add(resSchInfoVo);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		schInfoVo.setStatus(EnumerateParameter.ZERO);
		schInfoVo.setDesc("??????");
		schInfoVo.setErrorMessage("");
		SchInfoData schInfoData = new SchInfoData();
		SchInfoDataVo schInfoDataVo = new SchInfoDataVo();
		schInfoDataVo.setResSchInfoVo(schInfoVoList);
		schInfoData.setSchInfoDataVo(schInfoDataVo);
		schInfoVo.setSchInfoData(schInfoData);

		result = XmlUtil.beanToXml(schInfoVo, schInfoVo.getClass(), true);
		logger.info(classMathName + "/return/" + JsonUtil.writeValueAsString(result));
		return result;
    }


    @Override
	public String getEffectPvnfo(String param) {
		String classMathName = this.getClass().getName() + "/"
				+ Thread.currentThread().getStackTrace()[1].getMethodName();
		logger.info(classMathName + "/param/" + JsonUtil.writeValueAsString(param));
		RequestXml rx = (RequestXml) XmlUtil.XmlToBean(RequestXml.class, param);
		ResponseEffpVo effpVo = new ResponseEffpVo();
		String result = null;
		if (rx == null) {
			effpVo.setStatus("-1");
			effpVo.setDesc("??????????????????");
			effpVo.setErrorMessage("");
			result = XmlUtil.beanToXml(effpVo, effpVo.getClass(), true);
			return result;

		}
		if (rx.getOnlyBlFlag() == null || "".equals(CommonUtils.getString(rx.getOnlyBlFlag()))) {
			effpVo.setStatus("-1");
			effpVo.setDesc("??????????????????onlyBlFlag");
			effpVo.setErrorMessage("");
			result = XmlUtil.beanToXml(effpVo, effpVo.getClass(), true);
			return result;
		}
		if (rx.getPkPi() == null || "".equals(CommonUtils.getString(rx.getPkPi()))) {
			effpVo.setStatus("-1");
			effpVo.setDesc("??????????????????pkPi");
			effpVo.setErrorMessage("");
			result = XmlUtil.beanToXml(effpVo, effpVo.getClass(), true);
			return result;
		}
		// Map<String, Object> paramMap = (Map<String, Object>)
		// ApplicationUtils.beanToMap(rx);
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("onlyBlFlag", rx.getOnlyBlFlag());
		paramMap.put("pkPi", rx.getPkPi());
		List<ResEffpVo> list = new ArrayList<ResEffpVo>();
		ResEffpVo resEffpVO = null;
		List<Map<String, Object>> effectList = null;
		try {
			effectList = blPubForWsService.getEffectPvnfo(paramMap);
			for (Map<String, Object> map : effectList) {
				resEffpVO = ApplicationUtils.mapToBean(map, ResEffpVo.class);
				list.add(resEffpVO);
			}
		} catch (Exception e) {
			effpVo.setStatus("-2");
			effpVo.setDesc("????????????");
			effpVo.setErrorMessage(e.getMessage());
			result = XmlUtil.beanToXml(effpVo, effpVo.getClass(), true);
			return result;
		}
		EffpvData effpvData = new EffpvData();
		EffpvDataVo effpvDataVo = new EffpvDataVo();
		effpvDataVo.setResEffpVos(list);
		effpvData.setEffpvDataVo(effpvDataVo);
		effpVo.setDatalist(effpvData);
		effpVo.setStatus("0");
		effpVo.setDesc("??????");
		effpVo.setErrorMessage("");
		result = XmlUtil.beanToXml(effpVo, effpVo.getClass(), true);
		logger.info(classMathName + "/return/" + JsonUtil.writeValueAsString(result));
		return result;
	}
    
    @Override
	public String getPvInfoByPv(String param) {
		String classMathName = this.getClass().getName() + "/"
				+ Thread.currentThread().getStackTrace()[1].getMethodName();
		logger.info(classMathName + "/param/" + JsonUtil.writeValueAsString(param));
		RequestXml rx = (RequestXml) XmlUtil.XmlToBean(RequestXml.class, param);
		ResponseHospInfoBedVo bedVo = new ResponseHospInfoBedVo();
		String result = null;
		if (rx == null) {
			bedVo.setStatus("-1");
			bedVo.setDesc("??????????????????");
			bedVo.setErrorMessage("");
			result = XmlUtil.beanToXml(bedVo, bedVo.getClass(), true);
			return result;
		}
		Map<String, Object> paramMap = new HashMap<String, Object>();
		if (rx.getPkPv() != null && !"".equals(CommonUtils.getString(rx.getPkPv()))) {
			paramMap.put("pkPv", rx.getPkPv());
		}
		if (rx.getCodePv() != null && !"".equals(CommonUtils.getString(rx.getCodePv()))) {
			paramMap.put("codePv", rx.getPkPv());
		}
		if (rx.getBedNum() != null && !"".equals(CommonUtils.getString(rx.getBedNum()))) {
			paramMap.put("bedNum", rx.getBedNum());
		}
		if (rx.getPkDept() != null && !"".equals(CommonUtils.getString(rx.getPkDept()))) {
			paramMap.put("pkDept", rx.getPkDept());

		}
		if (rx.getCodeDept() != null && !"".equals(CommonUtils.getString(rx.getCodeDept()))) {
			paramMap.put("codeDept", rx.getCodeDept());
		}
		List<Map<String, Object>> list = null;
		ResHospInfoBedVo resHospInfoBedVo = null;
		List<ResHospInfoBedVo> bedVos = new ArrayList<ResHospInfoBedVo>();
		try {
			list = pvPubForWsService.getPvInfoByPv(paramMap);
			for (Map<String, Object> map : list) {
				String pkPv = (String) map.get("pkPv");
				resHospInfoBedVo = ApplicationUtils.mapToBean(map, ResHospInfoBedVo.class);
				BigDecimal prePayment = DataBaseHelper
						.queryForScalar(
								"select sum(amount) as  prePayment  from bl_deposit where eu_dptype='9' and pk_pv=? and flag_settle='0'",
								BigDecimal.class, pkPv);

				BigDecimal patientInvest = DataBaseHelper
						.queryForScalar(
								"select sum(cg.amount) as patientInvest from bl_ip_dt cg  where cg.pk_pv=? and cg.flag_settle = '0'",
								BigDecimal.class, pkPv);

				BigDecimal creditMoney = DataBaseHelper.queryForScalar(
						" select sum(amt_credit) as creditMoney from pv_ip_acc  where flag_canc='0' and pk_pv = ?",
						BigDecimal.class, pkPv);

				if (prePayment != null) {
					resHospInfoBedVo.setPrePayment(prePayment.setScale(2, BigDecimal.ROUND_HALF_UP));
				}
				if (patientInvest != null) {
					resHospInfoBedVo.setPatientInvest(patientInvest.setScale(2, BigDecimal.ROUND_HALF_UP));
				}
				if (creditMoney != null) {
					resHospInfoBedVo.setCreditMoney(creditMoney.setScale(2, BigDecimal.ROUND_HALF_UP));
				}
				resHospInfoBedVo.setBalance(resHospInfoBedVo.getPrePayment()
						.subtract(resHospInfoBedVo.getPatientInvest()).setScale(2, BigDecimal.ROUND_HALF_UP));
				bedVos.add(resHospInfoBedVo);
			}
		} catch (Exception e) {
			bedVo.setStatus("-2");
			bedVo.setDesc("????????????");
			bedVo.setErrorMessage(e.getMessage());
			result = XmlUtil.beanToXml(bedVo, bedVo.getClass(), true);
			return result;
		}
		HospInfoBedData bedData = new HospInfoBedData();
		HospInfoBedDataVo bedDataVo = new HospInfoBedDataVo();
		bedDataVo.setResHospInfoBedVo(bedVos);
		bedData.setBedVo(bedDataVo);
		bedVo.setStatus("0");
		bedVo.setDesc("??????");
		bedVo.setErrorMessage("");
		bedVo.setHospInfoBedData(bedData);
		result = XmlUtil.beanToXml(bedVo, bedVo.getClass(), true);
		logger.info(classMathName + "/return/" + JsonUtil.writeValueAsString(result));
		return result;
	}
    
    /**
     * ????????????????????????
     *
     * @param param
     * @return
     */
    @SuppressWarnings("unchecked")
	@Override
	public String getRegistered(String param) {
		String classMathName = this.getClass().getName() + "/"
				+ Thread.currentThread().getStackTrace()[1].getMethodName();
		logger.info(classMathName + "/param/" + JsonUtil.writeValueAsString(param));
		// TODO Auto-generated method stub
		RequestXml rx = (RequestXml) XmlUtil.XmlToBean(RequestXml.class, param);
		ResponseRegisterVo registerVo = new ResponseRegisterVo();
		String result = null;
		if (rx == null) {
			registerVo.setStatus("-1");
			registerVo.setDesc("??????????????????");
			registerVo.setErrorMessage("");
			result = XmlUtil.beanToXml(registerVo, registerVo.getClass(), true);
			return result;
		}
		if (rx.getDateWork() == null || "".equals(CommonUtils.getString(rx.getDateWork()))) {
			registerVo.setStatus("-1");
			registerVo.setDesc("??????????????????dateWork");
			registerVo.setErrorMessage("");
			result = XmlUtil.beanToXml(registerVo, registerVo.getClass(), true);
			return result;
		}
		Map<String, Object> paramMap = (Map<String, Object>) ApplicationUtils.beanToMap(rx);
		List<ResRegisterVo> registerVos = new ArrayList<ResRegisterVo>();
		ResRegisterVo resRegisterVo = null;
		List<Map<String, Object>> list = null;
		try {
			list = schPubForWsService.getRegistered(paramMap);
			for (Map<String, Object> map : list) {
				resRegisterVo = ApplicationUtils.mapToBean(map, ResRegisterVo.class);
				registerVos.add(resRegisterVo);
			}
		} catch (Exception e) {
			registerVo.setStatus("-2");
			registerVo.setDesc("????????????");
			registerVo.setErrorMessage(e.getMessage());
			result = XmlUtil.beanToXml(registerVo, registerVo.getClass(), true);
			return result;
		}
		RegisterData registerData = new RegisterData();
		RegisterDataVo registerDataVo = new RegisterDataVo();
		registerDataVo.setResvos(registerVos);
		registerData.setRegisterDataVo(registerDataVo);
		registerVo.setStatus("0");
		registerVo.setDesc("??????");
		registerVo.setErrorMessage("");
		registerVo.setDatalist(registerData);
		result = XmlUtil.beanToXml(registerVo, registerVo.getClass(), true);
		logger.info(classMathName + "/return/" + JsonUtil.writeValueAsString(result));
		return result;
	}
    
    
    /**
     * ????????????????????????????????????
     * @param param
     * @return
     */
    @SuppressWarnings("unchecked")
	@Override
	public String queryOrderCenterInfo(String param) {
		// TODO Auto-generated method stub
		RequestXml rx = (RequestXml) XmlUtil.XmlToBean(RequestXml.class, param);
		PayInfoRes res = new PayInfoRes();
		String result = null;
		if (rx == null) {
			res.setStatus("-1");
			res.setErrorMessage("");
			res.setDesc("??????????????????");
			result = XmlUtil.beanToXml(res, res.getClass(), true);
			return result;
		}
		if (rx.getPayOderSn() == null || "".equals(CommonUtils.getString(rx.getPayOderSn()))) {
			res.setStatus("-1");
			res.setDesc("??????????????????payOderSn");
			res.setErrorMessage("");
			result = XmlUtil.beanToXml(res, res.getClass(), true);
			return result;
		}
		Map<String, Object> paramMap = (Map<String, Object>) ApplicationUtils.beanToMap(rx);
		List<Map<String, Object>> payInfoList = blPubForWsService.queryOrderCenterInfo(paramMap);
		PayInfoData payInfoData = new PayInfoData();
		PayInfoOrderData orderData = new PayInfoOrderData();
		RegDatas regDatas = new RegDatas();
		RegData regData = new RegData();
		List<RegData> regList = new ArrayList<RegData>();
		DepositDatas depositDatas = new DepositDatas();
		DepositData depositData = new DepositData();
		List<DepositData> depositList = new ArrayList<DepositData>();
		SettleDatas settleDatas = new SettleDatas();
		SettleData settleData = new SettleData();
		InSettleDatas inSettleDatas = new InSettleDatas();
		InSettleData inSettleData = new InSettleData();
		List<InSettleData> inSettleList = new ArrayList<InSettleData>();
		List<SettleData> settleList = new ArrayList<SettleData>();
		for (Map<String, Object> map : payInfoList) {
			payInfoData.setNameOrg(map.get("nameOrg").toString());
			payInfoData.setNamePi(map.get("namePi").toString());
			if (null != map.get("orderSummary")) {
				payInfoData.setOrderSummary(map.get("orderSummary").toString());
			}
			if (null != map.get("orderType")) {
				payInfoData.setOrderType(map.get("orderType").toString());
			}
			payInfoData.setPkOrg(map.get("pkOrg").toString());
			payInfoData.setPkPi(map.get("pkPi").toString());
			String pkPv = (String) map.get("pkPv");
			if (map.get("euDptype").toString().equals("0") && map.get("dtSttype").toString().equals("01")) {
				List<Map<String, Object>> list = blPubForWsService.queryReg(pkPv);
				for (Map<String, Object> mapReg : list) {
					try {
						regData = ApplicationUtils.mapToBean(mapReg, RegData.class);
						regList.add(regData);
					} catch (Exception e) {
						// TODO Auto-generated catch block
						res.setStatus("-2");
						res.setErrorMessage(e.getMessage());
						res.setDesc("????????????????????????");
						result = XmlUtil.beanToXml(res, res.getClass(), true);
						return result;
					}
				}
				regDatas.setRegData(regList);
				orderData.setRegDatas(regDatas);
			}
			if (map.get("euDptype").toString().equals("0") && map.get("dtSttype").toString().equals("00")) {
				List<Map<String, Object>> list = blPubForWsService.querySettle(pkPv);
				for (Map<String, Object> mapSettle : list) {
					try {
						settleData = ApplicationUtils.mapToBean(mapSettle, SettleData.class);
						settleList.add(settleData);
					} catch (Exception e) {
						// TODO Auto-generated catch block
						res.setStatus("-2");
						res.setErrorMessage(e.getMessage());
						res.setDesc("??????????????????????????????");
						result = XmlUtil.beanToXml(res, res.getClass(), true);
						return result;
					}
				}
				settleDatas.setSettleData(settleList);
				orderData.setSettleDatas(settleDatas);
			}
			if (map.get("euDptype").toString().equals("9")) {
				List<Map<String, Object>> list = blPubForWsService.queryIpFee(pkPv, rx.getPayOderSn());
				for (Map<String, Object> mapFee : list) {
					try {
						depositData = ApplicationUtils.mapToBean(mapFee, DepositData.class);
						depositList.add(depositData);
					} catch (Exception e) {
						// TODO Auto-generated catch block
						res.setStatus("-2");
						res.setErrorMessage(e.getMessage());
						res.setDesc("?????????????????????????????????");
						result = XmlUtil.beanToXml(res, res.getClass(), true);
						return result;
					}
				}
				depositDatas.setDepositData(depositList);
				orderData.setDepositDatas(depositDatas);

			}
			if (null != map.get("dtSttype") && map.get("dtSttype").toString().equals("10")) {
				List<Map<String, Object>> list = blPubForWsService.queryInSettle(pkPv, rx.getPayOderSn());
				for (Map<String, Object> mapInSettle : list) {
					try {
						inSettleData = ApplicationUtils.mapToBean(mapInSettle, InSettleData.class);
						inSettleList.add(inSettleData);
					} catch (Exception e) {
						// TODO Auto-generated catch block
						res.setStatus("-2");
						res.setErrorMessage(e.getMessage());
						res.setDesc("??????????????????????????????");
						result = XmlUtil.beanToXml(res, res.getClass(), true);
						return result;
					}

				}
				inSettleDatas.setInSettleData(inSettleList);
				orderData.setInSettleDatas(inSettleDatas);

			}

		}
		payInfoData.setOrderData(orderData);
		res.setData(payInfoData);
		res.setDesc("??????");
		res.setErrorMessage("");
		res.setStatus("0");
		result = XmlUtil.beanToXml(res, res.getClass(), true);
		return result;

	}
    
    /**
	 * 5.6.????????????????????????
	 * ????????????????????????????????????????????????
	 * <p>Title: getPiAppointment</p>
	 * <p>Description: </p>
	 * @param param
	 * @return
	 * @see com.zebone.nhis.webservice.cxf.INHISSelfHelpWebService#getPiAppointment(java.lang.String)
	 */
	@Override
	public String getPiAppointment(String param) {
		String classMathName = this.getClass().getName() + "/"
				+ Thread.currentThread().getStackTrace()[1].getMethodName();
		logger.info(classMathName + "/param/" + JsonUtil.writeValueAsString(param));
		// TODO Auto-generated method stub
		/*
		 * Map<String, Object> paramMap = JsonUtil.readValue(param, Map.class);
		 * if (paramMap == null) return CommonUtils.getString(new
		 * OtherRespJson("-1|??????????????????|null")); if (paramMap.get("pkPi") == null ||
		 * "".equals(CommonUtils.getString(paramMap.get("pkPi")))) { return
		 * CommonUtils.getString(new OtherRespJson("-1|????????????pkPi??????|null")); }
		 * List<Map<String, Object>> apptList = null; try { apptList =
		 * schPubForWsService.getPiAppointment(paramMap); } catch (Exception e)
		 * { return CommonUtils.getString(new OtherRespJson("-2|??????|" +
		 * e.getMessage()));
		 * 
		 * } return CommonUtils.getString(new OtherRespJson("0|??????|null|" +
		 * ApplicationUtils.beanToJson(apptList))); }
		 */

		RequestXml rx = (RequestXml) XmlUtil.XmlToBean(RequestXml.class, param);
		ResponseAppointmentVo appointmentVo = new ResponseAppointmentVo();
		String result = null;
		if (rx == null) {
			appointmentVo.setStatus("-1");
			appointmentVo.setDesc("??????????????????");
			appointmentVo.setErrorMessage("");
			result = XmlUtil.beanToXml(appointmentVo, appointmentVo.getClass(), true);
			return result;
		}
		if ((rx.getPkPi() == null || "".equals(CommonUtils.getString(rx.getPkPi())))) {

			appointmentVo.setStatus("-1");
			appointmentVo.setDesc("????????????pkPi");
			appointmentVo.setErrorMessage("");
			result = XmlUtil.beanToXml(appointmentVo, appointmentVo.getClass(), true);
			return result;

		}
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("pkPi", rx.getPkPi());
		paramMap.put("pkOrg", rx.getPkOrg());

		List<Map<String, Object>> apptList = null;
		List<ResAppointmentVo> appointmentVoList = new ArrayList<ResAppointmentVo>();
		ResAppointmentVo resAppointmentVo = null;

		try {
			apptList = schPubForWsService.getPiAppointment(paramMap);
			for (Map<String, Object> map : apptList) {
				resAppointmentVo = ApplicationUtils.mapToBean(map, ResAppointmentVo.class);
				appointmentVoList.add(resAppointmentVo);
			}
		} catch (Exception e) {
			appointmentVo.setStatus("-2");
			appointmentVo.setDesc("????????????");
			appointmentVo.setErrorMessage(e.getMessage());
			result = XmlUtil.beanToXml(appointmentVo, appointmentVo.getClass(), true);
			return result;
		}
		AppointmentData appointmentData = new AppointmentData();
		AppointmentDataVo appointmentDataVo = new AppointmentDataVo();
		appointmentDataVo.setResAppointmentVo(appointmentVoList);
		appointmentData.setAppointmentDataVo(appointmentDataVo);
		appointmentVo.setResAppointmentVo(appointmentData);
		appointmentVo.setStatus(EnumerateParameter.ZERO);
		appointmentVo.setDesc("??????");
		appointmentVo.setErrorMessage("");
		result = XmlUtil.beanToXml(appointmentVo, appointmentVo.getClass(), true);
		logger.info(classMathName + "/return/" + JsonUtil.writeValueAsString(result));
		return result;
	}
	/**
	 * ????????????????????????????????????
	 * @param param
	 * @return
	 */
	@Override
	public String getRegistRecord(String param) {
		String classMathName = this.getClass().getName() + "/"
				+ Thread.currentThread().getStackTrace()[1].getMethodName();
		logger.info(classMathName + "/param/" + JsonUtil.writeValueAsString(param));
		
		RequestXml rx = (RequestXml) XmlUtil.XmlToBean(RequestXml.class, param);
		ResponseAppointmentVo appointmentVo = new ResponseAppointmentVo();
		String result = null;
		if (rx == null) {
			appointmentVo.setStatus("-1");
			appointmentVo.setDesc("??????????????????");
			appointmentVo.setErrorMessage("");
			result = XmlUtil.beanToXml(appointmentVo, appointmentVo.getClass(), true);
			return result;
		}
		if ((rx.getPkPi() == null || "".equals(CommonUtils.getString(rx.getPkPi())))) {

			appointmentVo.setStatus("-1");
			appointmentVo.setDesc("????????????pkPi");
			appointmentVo.setErrorMessage("");
			result = XmlUtil.beanToXml(appointmentVo, appointmentVo.getClass(), true);
			return result;

		}
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("pkPi", rx.getPkPi());
		paramMap.put("pkOrg", rx.getPkOrg());

		List<Map<String, Object>> apptList = null;
		List<ResAppointmentVo> appointmentVoList = new ArrayList<ResAppointmentVo>();
		ResAppointmentVo resAppointmentVo = null;

		try {
			apptList = schPubForWsService.getRegistRecord(paramMap);
			for (Map<String, Object> map : apptList) {
				resAppointmentVo = ApplicationUtils.mapToBean(map, ResAppointmentVo.class);
				appointmentVoList.add(resAppointmentVo);
			}
		} catch (Exception e) {
			appointmentVo.setStatus("-2");
			appointmentVo.setDesc("????????????");
			appointmentVo.setErrorMessage(e.getMessage());
			result = XmlUtil.beanToXml(appointmentVo, appointmentVo.getClass(), true);
			return result;
		}
		AppointmentData appointmentData = new AppointmentData();
		AppointmentDataVo appointmentDataVo = new AppointmentDataVo();
		appointmentDataVo.setResAppointmentVo(appointmentVoList);
		appointmentData.setAppointmentDataVo(appointmentDataVo);
		appointmentVo.setResAppointmentVo(appointmentData);
		appointmentVo.setStatus(EnumerateParameter.ZERO);
		appointmentVo.setDesc("??????");
		appointmentVo.setErrorMessage("");
		result = XmlUtil.beanToXml(appointmentVo, appointmentVo.getClass(), true);
		logger.info(classMathName + "/return/" + JsonUtil.writeValueAsString(result));
		return result;
	}
	/**
     * ????????????????????????
     *
     * @param param
     * @return
     */
	@Override
	public String getPvToChargeInfo(String param) {
		String classMathName = this.getClass().getName() + "/"
				+ Thread.currentThread().getStackTrace()[1].getMethodName();
		logger.info(classMathName + "/param/" + JsonUtil.writeValueAsString(param));
		RequestXml rx = (RequestXml) XmlUtil.XmlToBean(RequestXml.class, param);
		ResponsePvChargeVo pvChargeVo = new ResponsePvChargeVo();
		String result = null;
		if (rx == null) {
			pvChargeVo.setStatus("-1");
			pvChargeVo.setDesc("??????????????????");
			pvChargeVo.setErrorMessage("");
			result = XmlUtil.beanToXml(pvChargeVo, pvChargeVo.getClass(), true);
			return result;
		}
		if (rx.getPkPv() == null || "".equals(CommonUtils.getString(rx.getPkPv()))) {
			pvChargeVo.setStatus("-1");
			pvChargeVo.setDesc("??????????????????pkPv");
			pvChargeVo.setErrorMessage("");
			result = XmlUtil.beanToXml(pvChargeVo, pvChargeVo.getClass(), true);
			return result;
		}
		/*String inssql="SELECT hp.* FROM PV_ENCOUNTER pi left join bd_hp hp on hp.pk_hp=pi.PK_INSU WHERE pi.PK_PV=?";
		Map<String, Object> hpmap = DataBaseHelper.queryForMap(inssql, rx.getPkPv());
		if(null!=hpmap){
			if(!"0".equals(hpmap.get("euHptype"))){
				pvChargeVo.setStatus("-1");
				pvChargeVo.setDesc("??????????????????????????????????????????");
				pvChargeVo.setErrorMessage("");
				return result = XmlUtil.beanToXml(pvChargeVo, pvChargeVo.getClass(), true);
			}
		}*/
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("pkPv", rx.getPkPv());
		paramMap.put("pkDeptExec", rx.getPkDeptExec());
		List<Map<String, Object>> chargeList = null;
		List<ResPvChargeVo> list = new ArrayList<ResPvChargeVo>();
		ResPvChargeVo resPvChargeVo = null;
		try {
			chargeList = blPubForWsService.getPvToChargeInfo(paramMap);
			for (Map<String, Object> map : chargeList) {
				Timestamp ts = (Timestamp) map.get("ts");
				// Timestamp tsp = DateUtils.pareTimestamp(ts);
				String strn = new SimpleDateFormat("yyyyMMddHHmmss").format(ts);
				map.put("ts", strn);
				resPvChargeVo = ApplicationUtils.mapToBean(map, ResPvChargeVo.class);
				list.add(resPvChargeVo);
			}
		} catch (Exception e) {
			pvChargeVo.setStatus("-2");
			pvChargeVo.setDesc("????????????");
			pvChargeVo.setErrorMessage(e.getMessage());
			result = XmlUtil.beanToXml(pvChargeVo, pvChargeVo.getClass(), true);
			return result;

		}
		PvChargeData pvChargeData = new PvChargeData();
		PvChargeDataVo pvChargeDataVo = new PvChargeDataVo();
		pvChargeDataVo.setResPvChargeVos(list);
		pvChargeData.setPvChargeDataVo(pvChargeDataVo);
		pvChargeVo.setDatalist(pvChargeData);
		pvChargeVo.setStatus("0");
		pvChargeVo.setDesc("??????");
		pvChargeVo.setErrorMessage("");
		result = XmlUtil.beanToXml(pvChargeVo, pvChargeVo.getClass(), true);
		logger.info(classMathName + "/return/" + JsonUtil.writeValueAsString(result));
		return result;
	}
    
    /**
     * ??????????????????
     *
     * @param param
     * @return
     */
	@Override
	public String chargeOpSettle(String param) {
		String classMathName = this.getClass().getName() + "/"
				+ Thread.currentThread().getStackTrace()[1].getMethodName();
		logger.info(classMathName + "/param/" + JsonUtil.writeValueAsString(param));
		Req rx = (Req) XmlUtil.XmlToBean(param, Req.class);
		ResponseOpSettleVo settleVo = new ResponseOpSettleVo();
		String result = null;
		if (rx == null) {
			settleVo.setStatus("-1");
			settleVo.setDesc("??????????????????");
			settleVo.setErrorMessage("");
			result = XmlUtil.beanToXml(settleVo, settleVo.getClass(), true);
			return result;
		}
		String pkPi = rx.getData().getChagSettList().getChagSett().getPkPi();
		String pkPv = rx.getData().getChagSettList().getChagSett().getPkPv();
		String pkOrgSt = rx.getData().getChagSettList().getChagSett().getPkOrgSt();
		BigDecimal patientsPay = rx.getData().getChagSettList().getChagSett().getPatientsPay();
		String note = rx.getData().getChagSettList().getChagSett().getNote();
		String codeEmpSt = rx.getData().getChagSettList().getChagSett().getCodeEmpSt();
		String pkDeptSt = rx.getData().getChagSettList().getChagSett().getPkDeptSt();
		BigDecimal aggregateAmount = rx.getData().getChagSettList().getChagSett().getAggregateAmount();
		OpCgTransforVo opcgVo = new OpCgTransforVo();
		opcgVo.setPkPi(pkPi);
		opcgVo.setPkPv(pkPv);
		opcgVo.setPkOrgSt(pkOrgSt);
		opcgVo.setCodeEmpSt(codeEmpSt);
		opcgVo.setPkDeptSt(pkDeptSt);
		opcgVo.setNote(note);
		opcgVo.setAggregateAmount(aggregateAmount);

		opcgVo.setPatientsPay(patientsPay);
		List<BlOpDt> blOpDts = new ArrayList<BlOpDt>();
		BlOpDt blOpDt = null;
		List<String> pkCgops = new ArrayList<String>();
		List<com.zebone.nhis.webservice.vo.chagsetvo.BlOpDt> blOpTrans = rx.getData().getChagSettList().getChagSett()
				.getBlOpDts().getBlOpDt();
		for (com.zebone.nhis.webservice.vo.chagsetvo.BlOpDt blOpTran : blOpTrans) {
			blOpDt = new BlOpDt();
			String timestr = blOpTran.getTs();
			Date d = DateUtils.strToDate(timestr);
			blOpDt.setAmount(blOpTran.getAmount());
			blOpDt.setAmountAdd(blOpTran.getAmountAdd());
			blOpDt.setAmountHppi(blOpTran.getAmountHppi());
			blOpDt.setAmountPi(blOpTran.getAmountPi());
			blOpDt.setSpec(blOpTran.getSpec());
			blOpDt.setRatioSelf(blOpTran.getRatioSelf());
			blOpDt.setRatioDisc(blOpTran.getRatioDisc());
			blOpDt.setQuan(blOpTran.getQuan());
			blOpDt.setPrice(blOpTran.getPrice());
			blOpDt.setPriceOrg(blOpTran.getPriceOrg());
			blOpDt.setPresNo(blOpTran.getPresNo());
			blOpDt.setPkCgop(blOpTran.getPkCgop());
			blOpDt.setPkCnord(blOpTran.getPkCnord());
			blOpDt.setPkDeptEx(blOpTran.getPkDeptEx());
			blOpDt.setPkDisc(blOpTran.getPkDisc());
			blOpDt.setPkEmpApp(blOpTran.getPkEmpOrd());
			blOpDt.setPkDeptApp(blOpTran.getPkDeptOrd());
			blOpDt.setFlagPv(blOpTran.getFlagPv());
			blOpDt.setNameEmpApp(blOpTran.getNameEmpOrd());
			blOpDt.setPkPi(blOpTran.getPkPi());
			blOpDt.setPkPv(blOpTran.getPkPv());
			blOpDt.setNameCg(blOpTran.getNameCg());
			blOpDt.setEuAdditem(blOpTran.getEuAdditem());
			blOpDt.setCodeOrdType(blOpTran.getCodeOrdType());
			blOpDt.setTs(d);

			blOpDts.add(blOpDt);
			pkCgops.add(blOpTran.getPkCgop());

		}
		opcgVo.setBlOpDts(blOpDts);
		List<BlDeposit> blDepos = new ArrayList<BlDeposit>();
		BlDeposit blDeposit = null;
		BigDecimal amountTran = null;
		List<com.zebone.nhis.webservice.vo.chagsetvo.BlDeposit> blDepoTrans = rx.getData().getChagSettList()
				.getChagSett().getBlDeposits().getBlDeposit();
		for (com.zebone.nhis.webservice.vo.chagsetvo.BlDeposit blDeTran : blDepoTrans) {
			if (StringUtils.isEmpty(blDeTran.getTradeNo())) {
				settleVo.setStatus("-1");
				settleVo.setDesc("tradeNo????????????");
				settleVo.setErrorMessage("");
				result = XmlUtil.beanToXml(settleVo, settleVo.getClass(), true);
				return result;
			}
			blDeposit = new BlDeposit();
			amountTran = blDeTran.getAmount();
			try {
				ApplicationUtils.copyProperties(blDeposit, blDeTran);
				blDepos.add(blDeposit);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		opcgVo.setBlDeposits(blDepos);
		BigDecimal amountBl = blPubForWsService.getBlOpDtAmountSum(pkCgops);
		if (amountBl != null && amountBl.compareTo(amountTran) != 0) {
			settleVo.setStatus("-1");
			settleVo.setDesc("????????????????????????????????????????????????");
			settleVo.setErrorMessage("");
			result = XmlUtil.beanToXml(settleVo, settleVo.getClass(), true);
			return result;
		}
		Map<String, Object> map = null;
		try {
			map = blPubForWsService.chargeOpSettle(opcgVo);
		} catch (Exception e) {
			settleVo.setStatus("-2");
			settleVo.setDesc("????????????");
			settleVo.setErrorMessage(e.getMessage());
			result = XmlUtil.beanToXml(settleVo, settleVo.getClass(), true);
			return result;
		}
		ResOpSettleVo resSettleVo = new ResOpSettleVo();
		String pkSettle = (String) map.get("pkSettle");
		resSettleVo.setPkSettle(pkSettle);
		BlSettle blSettle = DataBaseHelper.queryForBean("select * from bl_settle where pk_settle = ?", BlSettle.class,
				pkSettle);
		if (null != blSettle) {
			resSettleVo.setCodeSt(blSettle.getCodeSt());
		}
		settleVo.setDesc("??????");
		settleVo.setErrorMessage("");
		settleVo.setResOpSettleVo(resSettleVo);
		settleVo.setStatus("0");
		result = XmlUtil.beanToXml(settleVo, settleVo.getClass(), true);
		logger.info(classMathName + "/return/" + JsonUtil.writeValueAsString(result));
		return result;
	}
	
    /**
     * ???????????????
     * @param param
     * @return
     */
	@SuppressWarnings("unchecked")
	@Override
	public String getOpPreSettle(String param) {
		ResponseJson ret = new ResponseJson();
		JSONObject json;
		List<String> paramList;
		try {
			json = XML.toJSONObject(param);
			paramList = JsonUtil.readValue(json.getJSONObject("req").getJSONArray("pkCgops").toString(), List.class);
		} catch (Exception e) {
			try {
				json = XML.toJSONObject(param);
				paramList = Collections.singletonList(json.getJSONObject("req").get("pkCgops").toString());
			} catch (JSONException e1) {
				return String.format("<res>\r\n" + "  <errorMessage>%s</errorMessage>\r\n" + "  <desc>%s</desc>\r\n"
						+ "  <status>%s</status>\r\n" + "</res>", e1.getMessage(), "????????????xml??????", -1);
			}
		}
		if (CollectionUtils.isEmpty(paramList))
			return String.format("<res>\r\n" + "  <errorMessage>%s</errorMessage>\r\n" + "  <desc>%s</desc>\r\n"
					+ "  <status>%s</status>\r\n" + "</res>", null, "??????????????????", -1);
		List<BlOpDt> blOpDts = blPubForWsMapper.getBlOpDtList(paramList);
		if (CollectionUtils.isEmpty(blOpDts)) {
			return String.format("<res>\r\n" + "  <errorMessage>%s</errorMessage>\r\n" + "  <desc>%s</desc>\r\n"
					+ "  <status>%s</status>\r\n" + "</res>", null, "????????????????????????", -1);
		}
		String inssql="SELECT hp.* FROM PV_ENCOUNTER pi left join bd_hp hp on hp.pk_hp=pi.PK_INSU WHERE pi.PK_PV=?";
		Map<String, Object> hpmap = DataBaseHelper.queryForMap(inssql, blOpDts.get(0).getPkPv());
		if(null!=hpmap){
			if(!"0".equals(hpmap.get("euHptype"))){
				return String.format("<res>\r\n" + "  <errorMessage>%s</errorMessage>\r\n" + "  <desc>%s</desc>\r\n"
						+ "  <status>%s</status>\r\n" + "</res>", null, "??????????????????????????????????????????", -1);
			}
		}
		OpCgTransforVo opCgTransforVo = new OpCgTransforVo();
		opCgTransforVo.setPkPi(blOpDts.get(0).getPkPi());
		opCgTransforVo.setPkPv(blOpDts.get(0).getPkPv());
		opCgTransforVo.setPkOrgSt(blOpDts.get(0).getPkOrg());
		String codeEmpSt = getPropValueStr(DataBaseHelper.queryForMap(
				"select code_emp from bd_ou_employee where pk_emp= ?", blOpDts.get(0).getPkEmpApp()), "codeEmp");
		opCgTransforVo.setCodeEmpSt(codeEmpSt);
		opCgTransforVo.setPkDeptSt(blOpDts.get(0).getPkDeptEx());// pk_dept_ex
		opCgTransforVo.setCardNo("");//
		opCgTransforVo.setCurDate(new Date());
		String pkInsurance = getPropValueStr(
				DataBaseHelper.queryForMap("select pk_insu from pv_encounter where pk_pv= ?", blOpDts.get(0).getPkPv()),
				"pkInsu");
		for (BlOpDt blOpDt : blOpDts) {
			blOpDt.setPkInsu(pkInsurance);
		}
		opCgTransforVo.setPkInsurance(pkInsurance);
		opCgTransforVo.setBlOpDts(blOpDts);
		ApplicationUtils utils = new ApplicationUtils();
		User user = new User();
		UserContext.setUser(user);
		ret = utils.execService("bl", "OpCgSettlementService", "countOpcgAccountingSettlement", opCgTransforVo, user);
		logger.info("????????????????????????????????????" + JsonUtil.writeValueAsString(ret));
		try {
			// OpCgTransforVo vo = new OpCgTransforVo();
			// BeanUtils.copyProperties(ret.getData(), vo);
			// logger.info("????????????????????????????????????"+JsonUtil.writeValueAsString(vo));
			ChagSetts chagSetts = new ChagSetts();
			ChagSett chagSett = new ChagSett();
			String aggregateAmount = JsonUtil.getFieldValue(JsonUtil.writeValueAsString(ret), "data.aggregateAmount");
			if (!StringUtils.isEmpty(aggregateAmount)) {
				logger.info(aggregateAmount);
				chagSett.setAggregateAmount(new BigDecimal(aggregateAmount));
			}
			String medicarePayments = JsonUtil.getFieldValue(JsonUtil.writeValueAsString(ret), "data.medicarePayments");
			if (!StringUtils.isEmpty(medicarePayments)) {
				logger.info(medicarePayments);
				chagSett.setMedicarePayments(new BigDecimal(medicarePayments));
			}
			String patientsPay = JsonUtil.getFieldValue(JsonUtil.writeValueAsString(ret), "data.patientsPay");
			if (!StringUtils.isEmpty(patientsPay)) {
				logger.info(patientsPay);
				chagSett.setPatientsPay(new BigDecimal(patientsPay));
			}
			String pkPi = JsonUtil.getFieldValue(JsonUtil.writeValueAsString(ret), "data.pkPi");
			if (!StringUtils.isEmpty(pkPi)) {
				logger.info(pkPi);
				chagSett.setPkPi(pkPi);
			}
			BlOpDts blOpDtList = new BlOpDts();
			List<BlOpDtVo> blOpDtVoList = new ArrayList<BlOpDtVo>();
			try {
				List<Map<String, Object>> chargeList = blPubForWsService.queryBlOpDtsToPrePay(paramList);
				for (Map<String, Object> map : chargeList) {
					Timestamp ts = (Timestamp) map.get("TS");
					String strn = new SimpleDateFormat("yyyyMMddHHmmss").format(ts);
					map.put("ts", strn);
					BlOpDtVo blOpDtVo = ApplicationUtils.mapToBean(map, BlOpDtVo.class);
					blOpDtVoList.add(blOpDtVo);
				}
			} catch (Exception e) {
				ret.setStatus(-1);
				ret.setDesc("????????????xml??????");
				ret.setData(null);
				return "<res>" + CommonUtils.getString(XML.toString(new JSONObject(ret))) + "</res>";
			}
			blOpDtList.setBlOpDts(blOpDtVoList);
			chagSett.setBlOpDts(blOpDtList);
			BlDeposits blDepositList = new BlDeposits();
			blDepositList.setBlDeposit(Collections
					.singletonList(new com.zebone.nhis.webservice.vo.preopsettle.BlDeposit()));
			chagSett.setBlDeposits(blDepositList);
			ChagSettList chagSettList = new ChagSettList();
			chagSettList.setChagSett(chagSett);
			chagSetts.setChagSettList(chagSettList);
			ret.setData(chagSetts);
			return "<res>" + XML.toString(new JSONObject(ret)) + "</res>";
		} catch (Exception e) {
			ret.setStatus(-1);
			ret.setDesc("????????????xml??????");
			ret.setData(null);
			try {
				return "<res>" + CommonUtils.getString(XML.toString(new JSONObject(ret))) + "</res>";
			} catch (JSONException e1) {
				return String.format("<res>\r\n" + "  <errorMessage>%s</errorMessage>\r\n" + "  <desc>%s</desc>\r\n"
						+ "  <status>%s</status>\r\n" + "</res>", e1.getMessage(), "??????????????????", -1);
			}
		}
	}

	
    /**
	 * ????????????????????????????????????
	 * @param code
	 */
	public Map<String, Object> getOrgByCode(String code) {
		List<Map<String, Object>> orgDetils = DataBaseHelper
				.queryForList(
						"select pk_org,code_org,name_org,shortname from bd_ou_org where code_org = ?  and flag_active='1' and del_flag='0' ",
						code);
		if (orgDetils != null && orgDetils.size() > 0)
			return orgDetils.get(0);
		return null;
	}
	
	/**
	 * ???????????????
	 * @param map
	 * @return
	 */
	public static String getPropValueStr(Map<String, Object> map, String key) {
		if (map == null || map.isEmpty()) {
			return null;
		}
		String value = "";
		if (map.containsKey(key)) {
			Object obj = map.get(key);
			value = obj == null ? "" : obj.toString();
		}
		return value;
	}
	public String getEmployees(String param) {
		String classMathName = this.getClass().getName() + "/"
				+ Thread.currentThread().getStackTrace()[1].getMethodName();
		logger.info(classMathName + "/param/" + JsonUtil.writeValueAsString(param));
		// TODO Auto-generated method stub
		RequestXml rx = (RequestXml) XmlUtil.XmlToBean(RequestXml.class, param);
		ResponseEmployeeVo employeeVo = new ResponseEmployeeVo();
		String result = null;
		if (rx == null) {
			employeeVo.setStatus("-1");
			employeeVo.setDesc("??????????????????");
			employeeVo.setErrorMessage("");
			result = XmlUtil.beanToXml(employeeVo, employeeVo.getClass(), true);
			return result;
		}
		if (rx.getCodeEmp() == null) {
			employeeVo.setStatus("-1");
			employeeVo.setDesc("??????????????????");
			employeeVo.setErrorMessage("");
			result = XmlUtil.beanToXml(employeeVo, employeeVo.getClass(), true);
			return result;
		}
		ResEmployeeVo resEmployeeVo = new ResEmployeeVo();
		try {
			Map<String, Object> empMap= DataBaseHelper.queryForMap("SELECT * FROM bd_ou_employee where code_emp='"+rx.getCodeEmp()+"'");
			if(null!=empMap){
				resEmployeeVo = ApplicationUtils.mapToBean(empMap, ResEmployeeVo.class);
			}
		} catch (Exception e) {
			employeeVo.setStatus("-2");
			employeeVo.setDesc("????????????");
			employeeVo.setErrorMessage(e.getMessage());
			result = XmlUtil.beanToXml(employeeVo, employeeVo.getClass(), true);
			return result;
		}
		employeeVo.setResEmployeeVo(resEmployeeVo);
		employeeVo.setStatus("0");
		employeeVo.setDesc("??????");
		employeeVo.setErrorMessage("");
		result = XmlUtil.beanToXml(employeeVo, employeeVo.getClass(), true);
		logger.info(classMathName + "/return/" + JsonUtil.writeValueAsString(result));
		return result;
	}

	/**
	 * ??????????????????
	 * @param param
	 * @return
	 */
	@Override
	public String callBackSmsResult(String param) {
		Map map = JSON.parseObject(param, Map.class);
		String reportStatus = map.get("reportStatus").toString();
		String mobile = map.get("mobile").toString();
		String submitDate = map.get("submitDate").toString();
		String errorCode = map.get("errorCode").toString();
		String msgGroup = map.get("msgGroup").toString();
		SmsSendResult smsSendResult = DataBaseHelper.queryForBean("select * from sms_send_result where  msg_Group = ? and mobile = ?",SmsSendResult.class,new Object[]{msgGroup,mobile});
		if(smsSendResult == null){
			logger.info("??????????????????????????????"+param);
			return null;
		}
		boolean flag = "DELIVRD".equals(reportStatus);
		smsSendResult.setDateSend(DateUtils.strToDate(submitDate, "yyyy-MM-dd HH:mm:ss"));
		smsSendResult.setFlagSuccess(flag?EnumerateParameter.ZERO:EnumerateParameter.ONE);
		smsSendResult.setDescReturn(flag?"????????????":"????????????"+errorCode);
		DataBaseHelper.updateBeanByPk(smsSendResult);

		SmsSend smsSend  = DataBaseHelper.queryForBean("select * from sms_send where  pk_smssend = ? ",SmsSend.class,new Object[]{smsSendResult.getPkSmssend()});
		if(smsSend == null){
			logger.info("?????????????????????????????????????????????"+smsSendResult.getPkSmssend());
			return null;
		}
		smsSend.setCntSend((smsSend.getCntSend()==null?0:smsSend.getCntSend()+1));
		smsSend.setCntFailure(flag?smsSend.getCntFailure():(smsSend.getCntFailure()==null?0:smsSend.getCntFailure()+1));
		DataBaseHelper.updateBeanByPk(smsSend);
		return smsSendResult.getFlagSuccess();
	}
	/**
	 * ????????????
	 * @param param
	 * @return
	 */
	@SuppressWarnings("static-access")
	@Override
	public String accountRecharge(String param) {
		// TODO Auto-generated method stub
		logger.info("???????????????"+param);
		LbSHRequestVo requ = null;
		LbSHResponseVo responseVo = new LbSHResponseVo();
		if(StringUtils.isNotBlank(param)){
			requ = (LbSHRequestVo)XmlUtil.XmlToBean(param, LbSHRequestVo.class);
			if(CommonUtils.isEmptyString(requ.getDeviceid())){
				return XmlUtil.beanToXml(LbSelfUtil.common(Constant.RESFAIL, "??????????????????????????????"), LbSHResponseVo.class, false);
			}
			if(CommonUtils.isEmptyString(requ.getPatientId())){
				return XmlUtil.beanToXml(LbSelfUtil.common(Constant.RESFAIL, "????????????????????????"), LbSHResponseVo.class, false);
			}
			if(CommonUtils.isEmptyString(requ.getPayAmt())){
				return XmlUtil.beanToXml(LbSelfUtil.common(Constant.RESFAIL, "??????????????????????????????"), LbSHResponseVo.class, false);
			}
			if(CommonUtils.isEmptyString(requ.getPayType())){
				return XmlUtil.beanToXml(LbSelfUtil.common(Constant.RESFAIL, "????????????????????????"), LbSHResponseVo.class, false);
			}
		}else{
			return XmlUtil.beanToXml(LbSelfUtil.common(Constant.RESFAIL, "??????????????????"), LbSHResponseVo.class, false);
		}
		User user = LbSelfUtil.getDefaultUser(requ.getDeviceid());
		UserContext userContext = new UserContext();
		userContext.setUser(user);
		
		String sql = "select * from pi_acc where pk_pi = ? and (del_flag = '0' or del_flag is null)";
		PiAcc piAcc = DataBaseHelper.queryForBean(sql, PiAcc.class, requ.getPatientId());
		if(null ==piAcc){
			return XmlUtil.beanToXml(LbSelfUtil.common(Constant.RESFAIL, "?????????????????????????????????"), LbSHResponseVo.class, false);
		}
		try {
			//???????????????
			ResponseJson  NowSjh =  apputil.execService("PI", "CarddealService", "getNowSjh",null,user);
			Map<String, Object> tMap = new HashMap<>();
            tMap.put("euOptype", EnumerateParameter.ONE);
            tMap.put("dtPaymode",  requ.getPayType());
            tMap.put("euDirect", EnumerateParameter.ONE);
            tMap.put("pkPi", requ.getPatientId());
            tMap.put("amount", requ.getPayAmt());
            tMap.put("datePay", new Date());
            tMap.put("pkDept", ((User) user).getPkDept());
            tMap.put("pkEmpPay", ((User) user).getPkEmp());
            tMap.put("nameEmpPay", ((User) user).getNameEmp());
            tMap.put("reptNo", NowSjh.getData());
            if(!CommonUtils.isEmptyString(requ.getPayAmt()) && !("0.0").equals(requ.getPayAmt()) && !("0").equals(requ.getPayAmt())&& !("0.00").equals(requ.getPayAmt())){
				/**
    			 * ?????????????????????????????????????????????bl_ext_pay
    			 */
				if(("7").equals(requ.getPayType()) || ("8").equals(requ.getPayType())){
            	Map<String, Object> patMap = new HashMap<>();
				patMap.put("pkPi", requ.getPatientId());
				requ.getQrCodeInfoVo().get(0).setPaymethod("??????????????????"+requ.getPayAmt());
         		blPubForWsService.LbPayment(null,requ,patMap,user,null);
         		
         		tMap.put("payInfo", requ.getQrCodeInfoVo().get(0).getOrderno());
				}
            }
			ResponseJson  result =  apputil.execService("PI", "CarddealService", "saveMonOperation", tMap,user);
			if(result.getStatus()==0){
			   responseVo.setTranSerNo(NowSjh.getData().toString());//?????????
			}else{
				logger.info(result.getDesc()+"?????????????????????"+param);
				return XmlUtil.beanToXml(LbSelfUtil.common(Constant.RESFAIL, result.getDesc()), LbSHResponseVo.class, false);
			}
		} catch (Exception e) {
			// TODO: handle exception
			logger.info(e+"???????????????"+param);
		}
		responseVo.setCommonVo(LbSelfUtil.commonVo(Constant.RESSUC, "??????"));
		logger.info("????????????:"+XmlUtil.beanToXml(responseVo, LbSHResponseVo.class, false));
	    return XmlUtil.beanToXml(responseVo, LbSHResponseVo.class, false);
	}
	 //????????????
	@SuppressWarnings({ "static-access", "unchecked", "unused"})
	@Override
	public String payment(String param) {
		logger.info("?????????????????????:"+param);
		LbSHRequestVo requ = null;
		LbSHResponseVo responseVo = new LbSHResponseVo();
		if(StringUtils.isNotBlank(param)){
			requ = (LbSHRequestVo)XmlUtil.XmlToBean(param, LbSHRequestVo.class);
			if(CommonUtils.isEmptyString(requ.getDeviceid())){
				return XmlUtil.beanToXml(LbSelfUtil.common(Constant.RESFAIL, "????????????????????????"), LbSHResponseVo.class, false);
			}
			if (CommonUtils.isEmptyString(requ.getVisitId())) {
				return XmlUtil.beanToXml(LbSelfUtil.common(Constant.RESFAIL, "????????????????????????"),LbSHResponseVo.class, false);
			}
			if(CommonUtils.isEmptyString(requ.getRecipeIds())){
				return XmlUtil.beanToXml(LbSelfUtil.common(Constant.RESFAIL, "????????????????????????"), LbSHResponseVo.class, false);
			}
			if(CommonUtils.isEmptyString(requ.getPayAmt())){
				return XmlUtil.beanToXml(LbSelfUtil.common(Constant.RESFAIL, "??????????????????????????????"), LbSHResponseVo.class, false);
			}
			if(CommonUtils.isEmptyString(requ.getPayType())){
				return XmlUtil.beanToXml(LbSelfUtil.common(Constant.RESFAIL, "????????????????????????"), LbSHResponseVo.class, false);
			}
		}else{
			return XmlUtil.beanToXml(LbSelfUtil.common(Constant.RESFAIL, "??????????????????"), LbSHResponseVo.class, false);
		}
		//?????????????????????
		User user = LbSelfUtil.getDefaultUser(requ.getDeviceid());
		UserContext userContext = new UserContext();
		userContext.setUser(user);
		System.out.println(ApplicationUtils.getSysparam("BL0042", false));
		if(null==user){
			return XmlUtil.beanToXml(LbSelfUtil.common(Constant.RESFAIL, "????????????????????????????????????????????????"), LbSHResponseVo.class, false);
		}
		//????????????????????????
		String masSql="select ma.code_op,ous.code_emp,dept.name_dept,pv.* from pv_encounter pv INNER JOIN pi_master ma on ma.pk_pi=pv.pk_pi INNER JOIN BD_OU_DEPT dept on dept.pk_dept=pv.pk_dept INNER JOIN bd_ou_employee ous on ous.pk_emp=pv.pk_emp_phy where pk_pv= ?";
		Map<String, Object> PatMap = DataBaseHelper.queryForMap(masSql, requ.getVisitId());
		if(null==PatMap){
			logger.info("?????????????????????"+param);
			return XmlUtil.beanToXml(LbSelfUtil.common(Constant.RESFAIL, "?????????????????????????????????"), LbSHResponseVo.class, false);
		}
		try {
			List<BlOpDt> noSettleBlOpDts  = new ArrayList<BlOpDt>();//???????????????
			List<BlOpDt> noPkcnordBlOpDts  = new ArrayList<BlOpDt>();//???????????????
			Set<String> noSettlePkBlOpDt = new HashSet<String>();
			BigDecimal amtAcc = BigDecimal.ZERO;
			Set<String> pkBlOpDt = new HashSet<String>();
			Set<String> pkCnordS = new HashSet<String>();
				//?????????????????????????????????????????????????????????  ???--????????????1   ???--????????????0
				int beginIndex = requ.getRecipeIds().indexOf(",") == 0 ? 1 : 0;	
				//????????????????????????????????????????????????????????????  ???--?????????????????????   ???--??????????????????
				int endIndex = requ.getRecipeIds().lastIndexOf(",") + 1 == requ.getRecipeIds().length() ? requ.getRecipeIds().lastIndexOf(",") : requ.getRecipeIds().length();
				String source = requ.getRecipeIds().substring(beginIndex, endIndex);
				String recip[] = requ.getRecipeIds().split(",");
				for (String recipeid : recip) {
					if(pkBlOpDt.add(recipeid)){
					 String sql = "select * from bl_op_dt where pk_cgop='"+recipeid+"'";
					 List<BlOpDt> blOpDts = DataBaseHelper.queryForList(sql, BlOpDt.class, new Object[] {});
					 BigDecimal Payamt = BigDecimal.ZERO;
					 for (BlOpDt blOpDt : blOpDts) {
							if(Constant.RESFAIL.equals(blOpDt.getFlagSettle())&& null == blOpDt.getPkSettle()){
								if(null != blOpDt.getAmount()){
									BigDecimal amts=BigDecimal.valueOf(blOpDt.getAmount());
									BigDecimal amt=BigDecimal.valueOf(blOpDt.getAmount());
									amtAcc=amtAcc.add(amt);	
									Payamt=Payamt.add(amts);
								}
								if(blOpDt.getPkCnord()!=null){
									pkCnordS.add(blOpDt.getPkCnord());
								}else{
									noPkcnordBlOpDts.add(blOpDt);
								}
								noSettleBlOpDts.add(blOpDt);
								noSettlePkBlOpDt.add(blOpDt.getPkCgop());
								}
						}
					}
				}
				
			if(noSettleBlOpDts ==null || noSettleBlOpDts.size()<=0){
				return XmlUtil.beanToXml(LbSelfUtil.common(Constant.RESFAIL, "?????????????????????????????????????????????"), LbSHResponseVo.class, false);
			}else{
				if(!MathUtils.equ(amtAcc.doubleValue(), Double.valueOf(requ.getPayAmt()))){
					logger.info("?????????????????????"+amtAcc+"c:->"+requ.getPayAmt()+"????????????"+param);
					return XmlUtil.beanToXml(LbSelfUtil.common(Constant.RESFAIL, "??????????????????,????????????"), LbSHResponseVo.class, false);
				}
			}
			List<BlDeposit> BlDepositList = new ArrayList<>();//????????????
			BlDeposit blDeposit = new BlDeposit();
			OpCgTransforVo opCgTransforVo =new OpCgTransforVo();
			if(!("1").equals(requ.getPayType()) && !("4").equals(requ.getPayType())){
				if(requ.getInsurePayment()!=null && requ.getInsurePayment()>0){
					blDeposit.setAmount(new BigDecimal(requ.getSelfPayment()));//????????????
					opCgTransforVo.setAmtInsuThird(BigDecimal.valueOf(requ.getInsurePayment()));//??????????????????
				}else{
					blDeposit.setAmount(amtAcc);//????????????
					opCgTransforVo.setAmtInsuThird(BigDecimal.valueOf(0D));//??????????????????
				}
			    //?????????????????????????????????????????????bl_ext_pay  
				 blPubForWsService.LbPayment(null,requ,PatMap,user,null);
				 blDeposit.setPayInfo(requ.getQrCodeInfoVo().get(0).getOrderno());//?????????
				 //blDeposit.setAmount(new BigDecimal(requ.getQrCodeInfoVo().get(0).getPayamt()));//????????????
			}else if(("4").equals(requ.getPayType())){
				//??????????????????
				ResponseJson  blResult =  apputil.execService("bl", "PareAccoutService", "getPatiAccountAvailableBalance", PatMap,null);
				if(blResult.getStatus()== Constant.SUC){
				    Map<String, Object> pamMap = (Map<String, Object>)blResult.getData();
				    if(pamMap.isEmpty()){
				    	logger.info("??????????????????????????????,?????????????????????"+param);
						return XmlUtil.beanToXml(LbSelfUtil.common(Constant.RESFAIL, "????????????????????????"), LbSHResponseVo.class, false);
					   }
					//???????????????????????????
				    String sql=" select nvl(sum(DEPOSIT) ,0) as deposit from PI_CARD where FLAG_ACTIVE in ('0','1') and EU_STATUS in ('0','1','2') and PK_PI= ?";
					Map<String,Object > cardMap=DataBaseHelper.queryForMap(sql,PatMap.get("pkPi"));
					BigDecimal nAmout=BigDecimal.valueOf(Double.valueOf(String.valueOf(cardMap.get("deposit"))));
					BigDecimal BalanceAmout =BigDecimal.valueOf(Double.valueOf(LbSelfUtil.getPropValueStr(pamMap,"acc")));
					if(requ.getInsurePayment()!=null && requ.getInsurePayment()>0){
						if(requ.getSelfPayment().compareTo(BalanceAmout.subtract(nAmout).doubleValue()) == 1){
							logger.info("???????????????????????????"+amtAcc+"c:->"+BalanceAmout.subtract(nAmout)+"????????????"+param);
							return XmlUtil.beanToXml(LbSelfUtil.common(Constant.RESFAIL, "???????????????????????????????????????????????????"), LbSHResponseVo.class, false);
						}
						 blDeposit.setAmount(new BigDecimal(requ.getSelfPayment()));//????????????
						opCgTransforVo.setAmtInsuThird(BigDecimal.valueOf(requ.getInsurePayment()));//??????????????????
					}else{
						if(amtAcc.compareTo(BalanceAmout.subtract(nAmout)) == 1){
							logger.info("???????????????????????????"+amtAcc+"c:->"+BalanceAmout.subtract(nAmout)+"????????????"+param);
							return XmlUtil.beanToXml(LbSelfUtil.common(Constant.RESFAIL, "???????????????????????????????????????????????????"), LbSHResponseVo.class, false);
						}
						 blDeposit.setAmount(amtAcc);//????????????
						opCgTransforVo.setAmtInsuThird(BigDecimal.valueOf(0D));//??????????????????
					}
				}else{
					logger.info("??????????????????,???????????????????????????"+param);
					return XmlUtil.beanToXml(LbSelfUtil.common(Constant.RESFAIL, "??????????????????????????????"), LbSHResponseVo.class, false);
				}
				
				//???????????????
				ResponseJson  NowSjh =  apputil.execService("PI", "CarddealService", "getNowSjh",null,user);
				Map<String, Object> tMap = new HashMap<>();
	            tMap.put("euOptype", EnumerateParameter.NINE);//??????
	            tMap.put("dtPaymode",  requ.getPayType());//????????????
	            tMap.put("euDirect", EnumerateParameter.NEGA);
	            tMap.put("pkPi", LbSelfUtil.getPropValueStr(PatMap, "pkPi"));
	            tMap.put("amount", "-"+amtAcc.toString());
	            tMap.put("datePay", new Date());
	            tMap.put("pkDept", ((User) user).getPkDept());
	            tMap.put("pkEmpPay", ((User) user).getPkEmp());
	            tMap.put("nameEmpPay", ((User) user).getNameEmp());
	            tMap.put("reptNo", NowSjh.getData());
	            
				ResponseJson  blresult =  apputil.execService("PI", "CarddealService", "saveMonOperation", tMap,user);
				if(blresult.getStatus()!=0){
					logger.info("?????????????????????????????????"+param);
				    return XmlUtil.beanToXml(LbSelfUtil.common(Constant.RESFAIL,blresult.getDesc()), LbSHResponseVo.class, false);
				}
			}
			blDeposit.setDtPaymode(requ.getPayType());//????????????
			BlDepositList.add(blDeposit);
			BlPubSettleVo blPubSettleVo = new BlPubSettleVo();
			blPubSettleVo.setPkPi(LbSelfUtil.getPropValueStr(PatMap,"pkPi"));
			blPubSettleVo.setPkPv(LbSelfUtil.getPropValueStr(PatMap,"pkPv"));
			blPubSettleVo.setBlOpDts(noSettleBlOpDts);
			//blPubSettleVo.setInvoiceInfo(invoList); // ??????????????????
			blPubSettleVo.setDepositList(BlDepositList);
			blPubSettleVo.setPkBlOpDtInSql(CommonUtils.convertSetToSqlInPart(noSettlePkBlOpDt, "pk_cgop"));//??????????????????
			blPubSettleVo.setEuPvType(requ.getPayType());//????????????
			opCgTransforVo.setPkPi(LbSelfUtil.getPropValueStr(PatMap,"pkPi"));
			opCgTransforVo.setPkPv(LbSelfUtil.getPropValueStr(PatMap,"pkPv"));
			opCgTransforVo.setBlOpDts(noSettleBlOpDts);
			opCgTransforVo.setBlDeposits(BlDepositList);
			opCgTransforVo.setInvStatus("-2");
			
			ResponseJson  result =  apputil.execService("BL", "opCgSettlementService", "mergeOpcgAccountedSettlement", opCgTransforVo,user);
			if(result.getStatus()== Constant.SUC){
				//?????????????????????
			    List<LbRecipesVo>  recList = new ArrayList<>();
			    if(noPkcnordBlOpDts.size()>0){
			    	LbRecipesVo rec = new LbRecipesVo();
			    	BigDecimal amtBlOp = BigDecimal.ZERO;
			    	List<LbBlCgIpVo> blOpDtList = new ArrayList<>();
			    	 for(BlOpDt blOpDt:noPkcnordBlOpDts){
					    	if(blOpDt.getPkCnord()==null){
					    		LbBlCgIpVo lbVo = new LbBlCgIpVo();
					    		
		        				lbVo.setItemName(blOpDt.getNameCg());//????????????
		        				lbVo.setSpecs(blOpDt.getSpec());//??????
		        				if(blOpDt.getQuan() !=null){
		        					lbVo.setQty(blOpDt.getQuan().toString());//????????????
		        				}
		        				if(blOpDt.getPrice()!=null){
		        					lbVo.setPrice(blOpDt.getPrice().toString());//????????????
		        				}
		        				if(blOpDt.getAmount()!=null){
		        					lbVo.setItemFee(blOpDt.getAmount().toString());//???????????????
		        				}
		                		blOpDtList.add(lbVo);
		                		if(StringUtils.isNotBlank(lbVo.getItemFee())){
			               			 Double pric = Double.parseDouble(lbVo.getItemFee());
			                  		  BigDecimal amt=BigDecimal.valueOf(pric);
			                  		  amtBlOp=amtBlOp.add(amt);	
			               		}
					    	}
					    }
			    	    rec.setRecipeId("1");
			    	    rec.setRecipeName("??????");
			    	    rec.setRecipeTime(DateUtils.formatDate(DateUtils.parseDate(LbSelfUtil.getPropValueStr(PatMap,"dateBegin")),new Object[]{}));
		        		rec.setExecDept(LbSelfUtil.getPropValueStr(PatMap,"nameDept"));//????????????
	            		rec.setRecipeFee(amtBlOp.toString());
	            		rec.setLbBlCgIpVo(blOpDtList);
	            		
	            		recList.add(rec);
			    }
			    
			    for(String pkCnord :pkCnordS){
				    LbRecipesVo rec = new LbRecipesVo();
				    List<LbBlCgIpVo> blOpDtList = new ArrayList<>();
	        	    Map<String, Object>  cnordMap = new HashMap<String, Object>();
	        	    BigDecimal amtBlOp = BigDecimal.ZERO;
	        	    cnordMap.put("pkCnord",pkCnord);
	        	    List<Map<String, Object>> opCgDayDetail = cnPubForWsMapper.LbqueryCnOrderUnpaid(cnordMap);
	        	    rec = (LbRecipesVo)LbSelfUtil.mapToBean(opCgDayDetail.get(0),rec);
	        		rec.setRecipeTime(DateUtils.formatDate(DateUtils.parseDate(opCgDayDetail.get(0).get("recipeTime").toString()),new Object[]{}));
	        		
	        		String sql = "select itemcate.name as item_type,dt.* from bl_op_dt dt INNER JOIN bd_itemcate itemcate ON itemcate.pk_itemcate = dt.pk_itemcate where pk_cnord =?";
	        		List<Map<String, Object>> blOpList=DataBaseHelper.queryForList(sql, pkCnord);
	        		if(blOpList.size()>0){
	        			for(Map<String, Object> Opmap:blOpList){
	        				LbBlCgIpVo lbVo = new LbBlCgIpVo();
	        				lbVo.setItemType(LbSelfUtil.getPropValueStr(Opmap,"itemType"));//????????????
	        				lbVo.setItemName(LbSelfUtil.getPropValueStr(Opmap,"nameCg"));//????????????
	        				lbVo.setSpecs(LbSelfUtil.getPropValueStr(Opmap,"spec"));//??????
	        				lbVo.setQty(LbSelfUtil.getPropValueStr(Opmap,"quan"));//????????????
	        				lbVo.setPrice(LbSelfUtil.getPropValueStr(Opmap,"price"));//????????????
	        				lbVo.setItemFee(LbSelfUtil.getPropValueStr(Opmap,"amount"));//???????????????
	                		blOpDtList.add(lbVo);
	                		if(StringUtils.isNotBlank(lbVo.getItemFee())){
	               			 Double pric = Double.parseDouble(lbVo.getItemFee());
	                  		  BigDecimal amt=BigDecimal.valueOf(pric);
	                  		  amtBlOp=amtBlOp.add(amt);	
	               		     }
	            		} 
	        		}
	        		rec.setRecipeFee(amtBlOp.toString());//??????
	        		rec.setLbBlCgIpVo(blOpDtList);
	        		recList.add(rec);
	        	}
			    
        		responseVo.setRecipes(recList);
        		responseVo.setDoctName(LbSelfUtil.getPropValueStr(PatMap,"nameEmpPhy"));//????????????
	    		responseVo.setDoctCode(LbSelfUtil.getPropValueStr(PatMap,"codeEmp"));//????????????
	    		responseVo.setTotalFee(amtAcc.toString());//?????????
	    		responseVo.setCodeOp(LbSelfUtil.getPropValueStr(PatMap,"codeOp"));
	        	//responseVo.setInvoiceNo(queryForMap.get("curCodeInv").toString());//?????????
	    		Map<String, Object> piCardMap = DataBaseHelper.queryForMap("select CARD_NO from PI_CARD where FLAG_ACTIVE = '1' AND EU_STATUS ='0' and DT_CARDTYPE ='01' and pk_pi=? ORDER BY create_time DESC", new Object[]{LbSelfUtil.getPropValueStr(PatMap,"pkPi")});
	    		 if(piCardMap != null){
	    			responseVo.setDtcardNo(LbSelfUtil.getPropValueStr(piCardMap,"cardNo"));
	    		 }else{
	    			responseVo.setDtcardNo(LbSelfUtil.getPropValueStr(PatMap,"codeOp"));
	    		 }
	        	responseVo.setCommonVo(LbSelfUtil.commonVo(Constant.RESSUC, "??????"));
			}else{
				//??????bl_ext_pay???flag_pay???'0'
				blPubForWsService.LbUpdateBlExtPay(requ);
				logger.info("?????????????????????"+param);
			    return XmlUtil.beanToXml(LbSelfUtil.common(Constant.RESFAIL,result.getDesc()), LbSHResponseVo.class, false);
			}
	    } catch (Exception e) {
		// TODO: handle exception
		    logger.info("?????????????????????"+e);
		    return XmlUtil.beanToXml(LbSelfUtil.common(Constant.RESFAIL, "??????????????????!"), LbSHResponseVo.class, false);
	     }
		logger.info("????????????:"+XmlUtil.beanToXml(responseVo, LbSHResponseVo.class, false));
		    return XmlUtil.beanToXml(responseVo, LbSHResponseVo.class, false);
	}
	/**
	 * ???????????????
	 */
	public String RechargeInpatientDeposit(String param) {
		logger.info("?????????????????????????????????:"+param);
		LbSHRequestVo requ = null;
		LbSHResponseVo responseVo = new LbSHResponseVo();
		BlDeposit vo = new BlDeposit();
		if(StringUtils.isNotBlank(param)){
			requ = (LbSHRequestVo)XmlUtil.XmlToBean(param, LbSHRequestVo.class);
			if(CommonUtils.isEmptyString(requ.getDeviceid())){
				return XmlUtil.beanToXml(LbSelfUtil.common(Constant.RESFAIL, "????????????????????????"), LbSHResponseVo.class, false);
			}
			if(CommonUtils.isEmptyString(requ.getPayAmt())){
				return XmlUtil.beanToXml(LbSelfUtil.common(Constant.RESFAIL, "?????????????????????"), LbSHResponseVo.class, false);
			}
			if(CommonUtils.isEmptyString(requ.getPayType())){
				return XmlUtil.beanToXml(LbSelfUtil.common(Constant.RESFAIL, "????????????????????????"), LbSHResponseVo.class, false);
			}
		}else{
			return XmlUtil.beanToXml(LbSelfUtil.common("0", "??????????????????"), LbSHResponseVo.class, false);
		}
		
		//????????????????????????
		Map<String, Object> PatMap = DataBaseHelper.queryForMap("select * from pv_encounter pv left join pi_master pi on pv.pk_pi=pi.pk_pi and pi.del_flag='0' where pv.eu_pvtype= '3' and (pv.eu_status = '0' or pv.eu_status = '1') and pv.pk_pv= ?", requ.getInPatientNo());
		if(!com.zebone.nhis.common.support.BeanUtils.isNotNull(PatMap)){
			logger.info("????????????????????????????????????"+param);
			return XmlUtil.beanToXml(LbSelfUtil.common(Constant.RESFAIL, "?????????????????????????????????"), LbSHResponseVo.class, false);
		}
		User user = LbSelfUtil.getDefaultUser(requ.getDeviceid());
		if(!com.zebone.nhis.common.support.BeanUtils.isNotNull(user)){
			return XmlUtil.beanToXml(LbSelfUtil.common(Constant.RESFAIL, "??????????????????????????????????????????"), LbSHResponseVo.class, false);
		}
		
        try {
    			/**
    			 * ????????????????????????  
    			 */
    			//Map<String, Object> queryForMap = invPubForWsService.getCanUsedEmpinv("5", "??????"+requ.getDeviceid(), user.getPkEmp(), "0", user.getPkOrg());

    			String sql = "select inv.inv_prefix,inv.cur_no,cate.length,inv.cnt_use,inv.pk_empinv,inv.pk_invcate, cate.prefix from bl_emp_invoice inv "
    					+ "inner join bd_invcate cate on inv.pk_invcate=cate.pk_invcate where inv.del_flag = '0' and inv.pk_org = ? "
    					+ "and inv.flag_use ='1' and inv.flag_active ='1' and cate.eu_type = ? and inv.pk_emp_opera = ?";
    			Map<String, Object> queryForMap = new HashMap<String, Object>();

    			queryForMap = DataBaseHelper.queryForMap(sql, user.getPkOrg(), Constant.HOSPREPAY, user.getPkEmp());
    			
    			if(queryForMap!=null){
    			String curNo = null;
    				if(queryForMap.get("curNo") == null){
    					return XmlUtil.beanToXml(LbSelfUtil.common(Constant.RESFAIL, "???????????????????????????????????????????????????"), LbSHResponseVo.class, false);
    				}else{
    					curNo = queryForMap.get("curNo").toString();
    				}
    				//?????????=??????????????????+????????????+????????????
    				String prefix = queryForMap.get("prefix") == null?"":queryForMap.get("prefix").toString(); //??????????????????
    				String invPrefix = queryForMap.get("invPrefix") == null?"":queryForMap.get("invPrefix").toString();
    				if(queryForMap.get("length") != null){
    					long length = Long.parseLong(queryForMap.get("length").toString());
    					curNo = invPubForWsService.flushLeft("0", length, curNo);
    					String curCodeInv = prefix + invPrefix + curNo;
    					queryForMap.put("curCodeInv", curCodeInv);
    				}else{
    					String curCodeInv = prefix + invPrefix + curNo;
    					queryForMap.put("curCodeInv", curCodeInv);
    				}
    			}else{
    				return XmlUtil.beanToXml(LbSelfUtil.common(Constant.RESFAIL, "???????????????????????????????????????????????????????????????"), LbSHResponseVo.class, false);
    			}
    			
    			//????????????????????????{ "pkEmpinv":"????????????", "cnt":"????????????" }
    			//invPubForWsService.LbconfirmUseEmpInv(queryForMap.get("pkEmpinv").toString(), (long)1,user);
    			
    			Long cnt = (long)1;

    			Map<String, Object> empinv = DataBaseHelper.queryForMap("select cnt_use,pk_emp_opera from bl_emp_invoice where del_flag = '0' and pk_empinv = ?", queryForMap.get("pkEmpinv").toString());
    			Long cntUse = Long.parseLong(empinv.get("cntUse").toString());
    			String pkEmpOpera = empinv.get("pkEmpOpera").toString();
    			if (cntUse - cnt < 0) {
    				return XmlUtil.beanToXml(LbSelfUtil.common(Constant.RESFAIL, "?????????????????????????????????" + (cntUse - cnt) + "?????????0??????????????????"), LbSHResponseVo.class, false);
    			} else {
    				invPubForWsService.LbUpdateBlDmpInvoice(cntUse, cnt, user.getPkOrg(), queryForMap.get("pkEmpinv").toString(), pkEmpOpera);
    			}
    			
    			/**
        		 * ?????????????????????BL_DEPOSIT
        		 */
    			vo =blPubForWsService.LbInsertBlDeposit(Constant.OTHERINV,vo,queryForMap,requ,PatMap,user);
    			
    			if(("7").equals(requ.getPayType()) || ("8").equals(requ.getPayType())){
    				/**
        			 * ?????????????????????????????????????????????bl_ext_pay
        			 */
             		 blPubForWsService.LbPayment(vo,requ,PatMap,user,null);  
    			}
    			
         		 responseVo.setTranSerNo(vo.getCodeDepo());//????????????????????????
                 responseVo.setOperateTime(DateUtils.getDateTimeStr(vo.getCreateTime()));//????????????
                 List<Map<String, Object>> blDepoList = DataBaseHelper.queryForList("SELECT * FROM BL_DEPOSIT WHERE EU_DIRECT=1 and PK_PV=?", vo.getPkPv());
                 BigDecimal amtAcc = BigDecimal.ZERO;
                 for(Map<String, Object> map : blDepoList){
                 	Double pric = Double.parseDouble(map.get("amount").toString());
         			BigDecimal amt=BigDecimal.valueOf(pric);
         			amtAcc=amtAcc.add(amt);	
                 }
                 responseVo.setBalance(amtAcc.toString());
		} catch (Exception e) {
			// TODO: handle exception
			logger.info("????????????????????????????????????"+e);
			return XmlUtil.beanToXml(LbSelfUtil.common(Constant.RESFAIL, "?????????????????????????????????!????????????????????????"), LbSHResponseVo.class, false);
		}
        
        //??????????????????
        responseVo.setCommonVo(LbSelfUtil.commonVo(Constant.RESSUC, "????????????"));
        logger.info("?????????????????????????????????:"+XmlUtil.beanToXml(responseVo, LbSHResponseVo.class, false));
		return XmlUtil.beanToXml(responseVo, LbSHResponseVo.class, false);
	}
	/*
	 * ????????????
	 */
	@Override
	public String register(String param) {
		logger.info("????????????-?????????-"+param);
		LbSHRequestVo requ = null;
		//????????????
		LbSHResponseVo responseVo = new LbSHResponseVo();
		if(StringUtils.isNotBlank(param)){
			requ = (LbSHRequestVo)XmlUtil.XmlToBean(param, LbSHRequestVo.class);
			if(CommonUtils.isEmptyString(requ.getDeviceid())){
				return XmlUtil.beanToXml(LbSelfUtil.common(Constant.RESFAIL, "??????????????????????????????"), LbSHResponseVo.class, false);
			}
			if (StringUtils.isBlank(requ.getRegId())) {
				return XmlUtil.beanToXml(LbSelfUtil.common(Constant.RESFAIL, "????????????????????????"),LbSHResponseVo.class, false);
			}
			
			BdOuDept codeDept = DataBaseHelper.queryForBean("select (select CODE_DEPT from BD_OU_DEPT where PK_DEPT=a.PK_DEPT) code_dept from SCH_SCH a where PK_SCH=?", BdOuDept.class,
					CommonUtils.getString(requ.getRegId()));
			if (null==codeDept) {
				return XmlUtil.beanToXml(LbSelfUtil.common(Constant.RESFAIL, "??????????????????????????????"),LbSHResponseVo.class, false);
			}
			requ.setDeptCode(codeDept.getCodeDept());
			if (StringUtils.isBlank(requ.getPayAmt())) {
				return XmlUtil.beanToXml(LbSelfUtil.common(Constant.RESFAIL, "???????????????????????????"),LbSHResponseVo.class, false);
			}
			if (StringUtils.isBlank(requ.getPayType())) {
				return XmlUtil.beanToXml(LbSelfUtil.common(Constant.RESFAIL, "????????????????????????"),LbSHResponseVo.class, false);
			}
			if (StringUtils.isBlank(requ.getPatientId())) {
				return XmlUtil.beanToXml(LbSelfUtil.common(Constant.RESFAIL, "??????????????????????????????"),LbSHResponseVo.class, false);
			}
			if (StringUtils.isBlank(requ.getRegDate())) {
				return XmlUtil.beanToXml(LbSelfUtil.common(Constant.RESFAIL, "????????????????????????"),LbSHResponseVo.class, false);
			}
			if (StringUtils.isBlank(requ.getRegisterTime())) {
				return XmlUtil.beanToXml(LbSelfUtil.common(Constant.RESFAIL, "??????????????????????????????"),LbSHResponseVo.class, false);
			}
		}else{
			return XmlUtil.beanToXml(LbSelfUtil.common(Constant.RESFAIL, "??????????????????"), LbSHResponseVo.class, false);
		}

		User user = LbSelfUtil.getDefaultUser(requ.getDeviceid());
		UserContext.setUser(user);
		if(!BeanUtils.isNotNull(user)){
			return XmlUtil.beanToXml(LbSelfUtil.common(Constant.RESFAIL, "??????????????????????????????????????????"), LbSHResponseVo.class, false);
		}
		LbPiMasterRegVo regvo = DataBaseHelper.queryForBean("SELECT pi.*,hp.pk_hp,hp.pk_insurance FROM pi_master pi LEFT JOIN PI_INSURANCE hp on hp.pk_pi = pi.pk_pi and hp.del_flag = '0' WHERE pi.del_flag = '0' and pi.pk_pi = ?", LbPiMasterRegVo.class, new Object[]{requ.getPatientId()});
		//??????????????????????????????
		Map<String, Object> bdhpMap = DataBaseHelper.queryForMap("SELECT pk_hp FROM bd_hp WHERE eu_hptype=? and del_flag='0' ", "0");
		//if(StringUtils.isBlank(regvo.getPkHp())){
			regvo.setPkHp(LbSelfUtil.getPropValueStr(bdhpMap,"pkHp"));
		//}
		try{
		//??????????????????
		Map<String, Object> cardTypeMap = DataBaseHelper.queryForMap("select val from bd_sysparam where del_flag = '0' and  code='PV0019' and pk_org=?",user.getPkOrg());
		//????????????
		Map<String, Object> codeTypeMap = DataBaseHelper.queryForMap("select val from bd_sysparam where del_flag = '0' and  code='CN0001' and pk_org=?",user.getPkOrg());
		if(null != codeTypeMap){
			String card[] = codeTypeMap.get("val").toString().split(",");
			for (int i = 0; i < card.length; i++) {
				  if((card[i]).equals(requ.getDeptCode())){
					int age = DateUtils.getAge(regvo.getBirthDate());
					int Maximum = Integer.valueOf(cardTypeMap.get("val").toString());
					if(age-Maximum>0){
						logger.info("??????????????????????????????:"+Maximum+"?????????????????????"+age+"???????????????????????????"+param);
						return XmlUtil.beanToXml(LbSelfUtil.common(Constant.RESFAIL, "??????????????????????????????:"+Maximum+"?????????????????????"+age+"???"), LbSHResponseVo.class, false);
					}
				  }

				}
		}
		responseVo = lbPubForWsService.registerWechat(param,requ, user,regvo);
		} catch(BusException e){
			return XmlUtil.beanToXml(LbSelfUtil.common(Constant.RESFAIL, e.getMessage()), LbSHResponseVo.class, false);
		} catch(Exception e){
			 logger.info("?????????????????????{}",e.getMessage());
			return XmlUtil.beanToXml(LbSelfUtil.common(Constant.RESFAIL, "?????????????????????????????????"), LbSHResponseVo.class, false);
		}

    	logger.info("????????????"+XmlUtil.beanToXml(responseVo, LbSHResponseVo.class, false));
		return XmlUtil.beanToXml(responseVo, LbSHResponseVo.class, false);
	}
	/**
	 * ??????????????????
	 * @param param
	 * @return
	 */
	@Override
	public String appointmentRegister(String param) {
		// TODO Auto-generated method stub
		logger.info("??????????????????{}",param);
		LbSHRequestVo requ = null;
		String resXml=null;
		LbSHResponseVo responseVo = new LbSHResponseVo();
		if(StringUtils.isNotBlank(param)){
			requ = (LbSHRequestVo)XmlUtil.XmlToBean(param, LbSHRequestVo.class);
			if(CommonUtils.isEmptyString(requ.getDeviceid())){
				return XmlUtil.beanToXml(LbSelfUtil.common(Constant.RESFAIL, "???????????????????????????"), LbSHResponseVo.class, false);
			}
			if (StringUtils.isBlank(requ.getRegId())) {
				return XmlUtil.beanToXml(LbSelfUtil.common(Constant.RESFAIL, "????????????????????????"),LbSHResponseVo.class, false);
			}
			BdOuDept codeDept = DataBaseHelper.queryForBean("select (select CODE_DEPT from BD_OU_DEPT where PK_DEPT=a.PK_DEPT) code_dept from SCH_SCH a where PK_SCH=?", BdOuDept.class,
					CommonUtils.getString(requ.getRegId()));
			if (null==codeDept) {
				return XmlUtil.beanToXml(LbSelfUtil.common(Constant.RESFAIL, "??????????????????????????????"),LbSHResponseVo.class, false);
			}
			requ.setDeptCode(codeDept.getCodeDept());
			if (StringUtils.isBlank(requ.getPayAmt())) {
				return XmlUtil.beanToXml(LbSelfUtil.common(Constant.RESFAIL, "???????????????????????????"),LbSHResponseVo.class, false);
			}
			if (StringUtils.isBlank(requ.getPayType())) {
				return XmlUtil.beanToXml(LbSelfUtil.common(Constant.RESFAIL, "????????????????????????"),LbSHResponseVo.class, false);
			}
			if (StringUtils.isBlank(requ.getPatientId())) {
				return XmlUtil.beanToXml(LbSelfUtil.common(Constant.RESFAIL, "??????????????????????????????"),LbSHResponseVo.class, false);
			}
			if (StringUtils.isBlank(requ.getRegDate())) {
				return XmlUtil.beanToXml(LbSelfUtil.common(Constant.RESFAIL, "????????????????????????"),LbSHResponseVo.class, false);
			}
			if (StringUtils.isBlank(requ.getRegisterTime())) {
				return XmlUtil.beanToXml(LbSelfUtil.common(Constant.RESFAIL, "??????????????????????????????"),LbSHResponseVo.class, false);
			}
		}else{
			return XmlUtil.beanToXml(LbSelfUtil.common(Constant.RESFAIL, "??????????????????"), LbSHResponseVo.class, false);
		}
		/*Map<String, Object> queryMap = new HashMap<String, Object>();
		queryMap.put("pkSch", requ.getRegId());
		String registerDateStr=requ.getRegDate();
		String registerTimeStr=(String) requ.getRegisterTime();
		String [] timeArgs=registerTimeStr.split("-");
		queryMap.put("beginDate",registerDateStr+" "+timeArgs[0]);
		queryMap.put("endDate",registerDateStr+" "+timeArgs[1]);
		List<Map<String,Object>> list=schPubForWsMapper.queryTicketsBySchAndTimeList(queryMap);
		if(null == list || list.size() ==0 ){
			return XmlUtil.beanToXml(LbSelfUtil.common(Constant.RESFAIL, "??????????????????????????????????????????"),LbSHResponseVo.class, false);
		}
		requ.setRegId(list.get(0).get("pkSchticket").toString());*/
		
		User user = LbSelfUtil.getDefaultUser(requ.getDeviceid());
		UserContext userContext = new UserContext();
		userContext.setUser(user);
		
		LbPiMasterRegVo regvo = DataBaseHelper.queryForBean("SELECT * FROM pi_master pi LEFT JOIN PI_INSURANCE hp on hp.pk_pi = pi.pk_pi and hp.del_flag = '0' WHERE pi.del_flag = '0' and pi.pk_pi = ?", LbPiMasterRegVo.class, new Object[]{requ.getPatientId()});
		//??????????????????????????????
		Map<String, Object> bdhpMap = DataBaseHelper.queryForMap("SELECT * FROM bd_hp WHERE eu_hptype=? and del_flag='0' ", "0");
		//if(StringUtils.isBlank(regvo.getPkHp())){
			regvo.setPkHp(LbSelfUtil.getPropValueStr(bdhpMap,"pkHp"));
		//}
		//??????????????????
		Map<String, Object> cardTypeMap = DataBaseHelper.queryForMap("select val from bd_sysparam where del_flag = '0' and  code='PV0019' and pk_org=?",user.getPkOrg());
		//????????????
		Map<String, Object> codeTypeMap = DataBaseHelper.queryForMap("select val from bd_sysparam where del_flag = '0' and  code='CN0001' and pk_org=?",user.getPkOrg());

		try {
			if(null != codeTypeMap){
				   String card[] = codeTypeMap.get("val").toString().split(",");
				   for (int i = 0; i < card.length; i++) {
						if((card[i]).equals(requ.getDeptCode())){
							int age = DateUtils.getAge(regvo.getBirthDate());
							int Maximum = Integer.valueOf(cardTypeMap.get("val").toString());
							if(age-Maximum>0){
								logger.info("??????????????????????????????????????????:{},?????????????????????{},???????????????????????????{}",Maximum,age,param);
								return XmlUtil.beanToXml(LbSelfUtil.common(Constant.RESFAIL, "??????????????????????????????:"+Maximum+"?????????????????????"+age+"???"), LbSHResponseVo.class, false);
							}
						}
					}
			}
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			regvo.setDateAppt(sdf.parse(requ.getRegDate()));
			Map<String, Object> paramMap = new HashMap<String, Object>();
			paramMap.put("pkSch", requ.getRegId());
			//????????????????????????????????????
			List<Map<String, Object>> schPlanList = schMapper.LbgetSchPlanInfo(paramMap);
			if(schPlanList.size()<=0){
				logger.info("?????????????????????????????????{}",param);
				 return XmlUtil.beanToXml(LbSelfUtil.common(Constant.RESFAIL, "????????????????????????????????????????????????"), LbSHResponseVo.class, false);
			}
			if(schPlanList.get(0)==null){
				 logger.info("?????????????????????????????????{}",param);
				 return XmlUtil.beanToXml(LbSelfUtil.common(Constant.RESFAIL, "????????????????????????????????????"), LbSHResponseVo.class, false);
			}
			 regvo.setDateReg(new Date());//????????????--????????????
			 regvo.setPkDept(LbSelfUtil.getPropValueStr(schPlanList.get(0),"pkDept"));//????????????
			 regvo.setEuSchclass("0");//????????????
			 regvo.setPkDateslotsec(requ.getPhaseCode());//????????????
				if(LbSelfUtil.getPropValueStr(schPlanList.get(0),"euSrvtype").equals("9")){
				   regvo.setEuPvtype("2");//????????????
				}else{
				   regvo.setEuPvtype("1");//???????????? 
				}
			 regvo.setDtPaymode(requ.getPayType());//????????????
			 regvo.setPkSchres(LbSelfUtil.getPropValueStr(schPlanList.get(0),"pkSchres"));
			 regvo.setPkDateslot(LbSelfUtil.getPropValueStr(schPlanList.get(0),"pkDateslot"));//????????????
			 regvo.setTicketNo(LbSelfUtil.getPropValueStr(schPlanList.get(0),"ticketno"));//????????????
			 regvo.setPkSch(LbSelfUtil.getPropValueStr(schPlanList.get(0),"pkSch"));//????????????
			 
			 regvo.setPkSchsrv(LbSelfUtil.getPropValueStr(schPlanList.get(0),"pkSchsrv"));//??????????????????

			 BigDecimal amt=BigDecimal.ZERO;
			 if(!CommonUtils.isEmptyString(requ.getPayAmt()) && !("0.0").equals(requ.getPayAmt()) && !("0").equals(requ.getPayAmt())&& !("0.00").equals(requ.getPayAmt())){
				 BlDeposit dep =new BlDeposit();
				 Double pric = Double.parseDouble(requ.getPayAmt());
				 amt=BigDecimal.valueOf(pric);
				 
				 //?????????????????????????????? 
				 if(("4").equals(requ.getPayType())){
					 logger.info("??????????????????????????????????????????{}",param);
					 return XmlUtil.beanToXml(LbSelfUtil.common(Constant.RESFAIL, "??????????????????????????????"), LbSHResponseVo.class, false);
				 }else if(!("1").equals(requ.getPayType()) && !("4").equals(requ.getPayType())){
					 dep.setPayInfo(requ.getQrCodeInfoVo().get(0).getFlowno());
				 }
				 
				
				//?????????????????????
				List<ItemPriceVo>  itemList = bdPubForWsMapper.LbgetBdItemInfo(LbSelfUtil.getPropValueStr(schPlanList.get(0),"pkSchsrv"));
				for(ItemPriceVo vo:itemList){
					vo.setRatioDisc(1D);
					vo.setRatioSelf(1D);
					vo.setNameCg(vo.getName());
					vo.setPriceOrg(vo.getPrice());
				}
				regvo.setItemList(itemList);
				regvo.setInvStatus("-2");
				
				dep.setAmount(amt);
				dep.setDtPaymode(requ.getPayType());//????????????
				List<BlDeposit> depList =new ArrayList<>();
				depList.add(dep);
				regvo.setDepositList(depList);
			 }
			 
			 //????????????????????????
				String date = regvo.getDateAppt()!=null?DateUtils.formatDate(regvo.getDateAppt(), "yyyyMMdd"): (regvo.getDateReg()!=null?DateUtils.formatDate(regvo.getDateReg(), "yyyyMMdd"):null);
				if(StringUtils.isNotBlank(date)) {
					if(DataBaseHelper.queryForScalar("select count(*) from PV_ENCOUNTER t WHERE t.PK_PI=? AND to_char(t.DATE_BEGIN,'yyyyMMdd') =? AND t.PK_DEPT=? AND t.EU_PVTYPE in('1','2') AND t.EU_STATUS in('0','1')",
							Integer.class, new Object[]{regvo.getPkPi(),date,regvo.getPkDept()}) >0){
						logger.info("????????????????????????????????????????????????????????????????????????{}",param);
						return XmlUtil.beanToXml(LbSelfUtil.common(Constant.RESFAIL, "????????????????????????????????????????????????????????????"), LbSHResponseVo.class, false);
					}
				}
			 
				//????????????
				ResponseJson  NowSjh =  apputil.execService("PV", "RegSyxHandler", "saveApptPvRegInfo",regvo,user);
				if(NowSjh.getStatus()==Constant.SUC){
			    LbPiMasterRegVo regvos = JsonUtil.readValue(JsonUtil.writeValueAsString(NowSjh.getData()), LbPiMasterRegVo.class);
				responseVo.setCodeNo(regvos.getCodePv());//???????????????
				//????????????????????????
				//DataBaseHelper.queryForMap("select * from pv_encounter pv left join pi_master pi on pv.pk_pi=pi.pk_pi and pi.del_flag='0' where pv.eu_pvtype= '3' and (pv.eu_status = '0' or pv.eu_status = '1') and pi.pk_pi= ?", requ.getPatientId());
				Map<String, Object> PatMap = new HashMap<String, Object>();
				PatMap.put("pkPi", regvo.getPkPi());
				PatMap.put("pkPv", regvo.getPkPv());
				PatMap.put("pkEmp", regvo.getPkEmp());
				
				if(("7").equals(requ.getPayType()) || ("8").equals(requ.getPayType())){
					/**
					 * ?????????????????????????????????????????????bl_ext_pay
					 */
					BlDeposit vo =regvos.getDepositList().get(0);
				 	blPubForWsService.LbPayment(vo,requ,PatMap,user,null);
				}
				 Map<String, Object> piCardMap = DataBaseHelper.queryForMap("select CARD_NO from PI_CARD where FLAG_ACTIVE = '1' AND EU_STATUS ='0' and DT_CARDTYPE ='01' and pk_pi=? ORDER BY create_time DESC", new Object[]{requ.getPatientId()});
		 		 if(piCardMap != null){
		 			responseVo.setDtcardNo(LbSelfUtil.getPropValueStr(piCardMap,"cardNo"));
		 		 }else{
		 			responseVo.setDtcardNo(regvo.getCodeOp());
		 		 }
		 		 responseVo.setTotalFee(requ.getPayAmt());
				 responseVo.setDeptName(LbSelfUtil.getPropValueStr(schPlanList.get(0),"name"));//??????????????????
				 responseVo.setLocation(LbSelfUtil.getPropValueStr(schPlanList.get(0),"namePlace"));//????????????
				 responseVo.setWaitNo("???"+regvos.getTicketNo());
				 responseVo.setDoctName(LbSelfUtil.getPropValueStr(schPlanList.get(0),"nameEmp"));//????????????
				 responseVo.setTypeName(LbSelfUtil.getPropValueStr(schPlanList.get(0),"stvName"));
				 responseVo.setCodeOp(regvo.getCodeOp());
		    	 responseVo.setCommonVo(LbSelfUtil.commonVo(Constant.RESSUC, "???????????????"));
		    	
		    	}else{
					resXml=XmlUtil.beanToXml(LbSelfUtil.common(Constant.RESFAIL, NowSjh.getDesc()),LbSHResponseVo.class, false);   
					logger.info("????????????{}",resXml);
					   return resXml;
				}
		} catch(Exception e){
			 logger.info("????????????????????????{},{}",param,e.getMessage());
			return XmlUtil.beanToXml(LbSelfUtil.common(Constant.RESFAIL, "???????????????????????????????????????"), LbSHResponseVo.class, false);
		}
		resXml=XmlUtil.beanToXml(responseVo, LbSHResponseVo.class, false);
		logger.info("????????????{}",resXml);
		return resXml;
	}
	/**
	 * ??????????????????
	 */
	public String lockReg(String param) {
		logger.info("??????"+param);
		LbSHRequestVo requ = null;
		SchTicket ticket = null;
		LbSHResponseVo responseVo = new LbSHResponseVo();
		if (StringUtils.isNotBlank(param)) {
			requ = (LbSHRequestVo) XmlUtil.XmlToBean(param, LbSHRequestVo.class);
			if (StringUtils.isBlank(requ.getRegId())) {
				return XmlUtil.beanToXml(LbSelfUtil.common(Constant.RESFAIL, "????????????????????????"),LbSHResponseVo.class, false);
			}
			if(CommonUtils.isEmptyString(requ.getDeviceid())){
				return XmlUtil.beanToXml(LbSelfUtil.common(Constant.RESFAIL, "???????????????????????????"), LbSHResponseVo.class, false);
			}
			if(CommonUtils.isEmptyString(requ.getPatientId())){
				return XmlUtil.beanToXml(LbSelfUtil.common(Constant.RESFAIL, "????????????????????????"), LbSHResponseVo.class, false);
			}
			if(CommonUtils.isEmptyString(requ.getDeptCode())){
				return XmlUtil.beanToXml(LbSelfUtil.common(Constant.RESFAIL, "????????????????????????"), LbSHResponseVo.class, false);
			}
		} else {
			return XmlUtil.beanToXml(LbSelfUtil.common(Constant.RESFAIL, "??????????????????"),LbSHResponseVo.class, false);
		}
		User user = LbSelfUtil.getDefaultUser(requ.getDeviceid());
		@SuppressWarnings("unused")
		UserContext userContext = new UserContext();
		UserContext.setUser(user);
		if(!BeanUtils.isNotNull(user)){
			return XmlUtil.beanToXml(LbSelfUtil.common(Constant.RESFAIL, "?????????????????????????????????????????????"), LbSHResponseVo.class, false);
		}
		try {
			//??????????????????
			Map<String, Object> cardTypeMap = DataBaseHelper.queryForMap("select val from bd_sysparam where del_flag = '0' and  code='PV0019' and pk_org=?",user.getPkOrg());
			//????????????
			Map<String, Object> codeTypeMap = DataBaseHelper.queryForMap("select val from bd_sysparam where del_flag = '0' and  code='CN0001' and pk_org=?",user.getPkOrg());
			LbPiMasterRegVo regvo = DataBaseHelper.queryForBean("SELECT * FROM pi_master pi LEFT JOIN PI_INSURANCE hp on hp.pk_pi = pi.pk_pi and hp.del_flag = '0' WHERE pi.del_flag = '0' and pi.pk_pi = ?", LbPiMasterRegVo.class, new Object[]{requ.getPatientId()});
			if(null != codeTypeMap){
				String card[] = codeTypeMap.get("val").toString().split(",");
				for (int i = 0; i < card.length; i++) {
					  if((card[i]).equals(requ.getDeptCode())){
						int age = DateUtils.getAge(regvo.getBirthDate());
						int Maximum = Integer.valueOf(cardTypeMap.get("val").toString());
						if(age-Maximum>0){
							logger.info("??????????????????????????????:"+Maximum+"?????????????????????"+age+"???????????????????????????"+param);
							return XmlUtil.beanToXml(LbSelfUtil.common(Constant.RESFAIL, "??????????????????????????????:"+Maximum+"?????????????????????"+age+"???"), LbSHResponseVo.class, false);
						}
					  }

					}
			}
			responseVo = lbPubForWsService.lbLockReg(requ, ticket);
		} catch (Exception e) {
			logger.info("??????"+e);
			return XmlUtil.beanToXml(LbSelfUtil.common(Constant.RESFAIL, "????????????"),LbSHResponseVo.class, false);
		}
		logger.info("??????"+XmlUtil.beanToXml(responseVo, LbSHResponseVo.class, false));
		return XmlUtil.beanToXml(responseVo, LbSHResponseVo.class, false);
	}
}
