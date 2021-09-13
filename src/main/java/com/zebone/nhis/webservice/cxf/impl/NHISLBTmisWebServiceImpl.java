package com.zebone.nhis.webservice.cxf.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import javax.annotation.Resource;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;
import com.zebone.nhis.common.module.base.ou.BdOuOrg;
import com.zebone.nhis.common.module.cn.ipdw.CnOrder;
import com.zebone.nhis.common.module.cn.ipdw.CnTransApply;
import com.zebone.nhis.common.module.pv.PvEncounter;
import com.zebone.nhis.common.support.ApplicationUtils;
import com.zebone.nhis.common.support.BeanUtils;
import com.zebone.nhis.common.support.CommonUtils;
import com.zebone.nhis.common.support.XmlUtil;
import com.zebone.nhis.webservice.cxf.INHISLBTmisWebService;
import com.zebone.nhis.webservice.service.BlPubForWsService;
import com.zebone.nhis.webservice.service.PvPubForWsService;
import com.zebone.nhis.webservice.support.LbSelfUtil;
import com.zebone.nhis.webservice.syx.vo.bl.BlPubParamVo;
import com.zebone.nhis.webservice.vo.tmisvo.BldTypeDisInfoVo;
import com.zebone.nhis.webservice.vo.tmisvo.DoctorAdviceVo;
import com.zebone.nhis.webservice.vo.tmisvo.FeeInfoVo;
import com.zebone.nhis.webservice.vo.tmisvo.PatientInfoVo;
import com.zebone.nhis.webservice.vo.tmisvo.RequestDeptTmisVo;
import com.zebone.nhis.webservice.vo.tmisvo.RequestTmisVo;
import com.zebone.nhis.webservice.vo.tmisvo.ResDeptTmisVo;
import com.zebone.nhis.webservice.vo.tmisvo.ResponseDoctorAdviceVo;
import com.zebone.nhis.webservice.vo.tmisvo.ResponseHISFeeVo;
import com.zebone.nhis.webservice.vo.tmisvo.ResponseImisVo;
import com.zebone.nhis.webservice.vo.tmisvo.area.RespAreaImisVo;
import com.zebone.nhis.webservice.vo.tmisvo.area.ResponseSickRoomInfoVo;
import com.zebone.nhis.webservice.vo.tmisvo.area.ResponseSickRoomSInfoVo;
import com.zebone.nhis.webservice.vo.tmisvo.dept.RespDeptImisVo;
import com.zebone.nhis.webservice.vo.tmisvo.dept.ResponseDeptVo;
import com.zebone.nhis.webservice.vo.tmisvo.dept.ResponseDeptSVo;
import com.zebone.nhis.webservice.vo.tmisvo.employee.RespEmpImisVo;
import com.zebone.nhis.webservice.vo.tmisvo.employee.ResponseStaffInfoVo;
import com.zebone.nhis.webservice.vo.tmisvo.employee.ResponseStaffSInfoVo;
import com.zebone.nhis.webservice.vo.tmisvo.item.RespItemImisVo;
import com.zebone.nhis.webservice.vo.tmisvo.item.ResponseFeeItemInfoVo;
import com.zebone.nhis.webservice.vo.tmisvo.item.ResponseFeeItemSInfoVo;
import com.zebone.platform.common.support.User;
import com.zebone.platform.common.support.UserContext;
import com.zebone.platform.modules.dao.DataBaseHelper;
import com.zebone.platform.web.support.ResponseJson;

/**
 * @Description  灵璧项目-对外自助机接口 xml格式
 * @author ds
 */
public class NHISLBTmisWebServiceImpl implements INHISLBTmisWebService {
	
	private Logger logger = LoggerFactory.getLogger("nhis.lbWebServiceLog");
	
	@Resource
	private PvPubForWsService pvPubForWsService;
	@Resource
	private BlPubForWsService blPubForWsService;
	private static String prefixStr="tmis";
	private static String pleStr="SXYZ";
	static final Integer[] intArray = {1, 2, 3, 4, 5, 6, 7, 8, 9,0};

	@Override
	public String test(String param) {
		// TODO Auto-generated method stub
		ResponseImisVo responseVo = new ResponseImisVo();
		responseVo.setResultCode("0");
		responseVo.setResultContent("test测试成功");
		return XmlUtil.beanToXml(responseVo, ResponseImisVo.class, false);
	}
	/**
	 * 查询科室列表信息
	 * @param param
	 * @return
	 */
	@Override
	public String queryDepts(String param) {
		// TODO Auto-generated method stub
		logger.info("queryDepts查询科室列表信息接口：{}",param);
		ResDeptTmisVo requ = null;
		RespDeptImisVo responseVo = new RespDeptImisVo();
		responseVo.setResultCode("1");
		if(StringUtils.isNotBlank(param)){
			requ = (ResDeptTmisVo) XmlUtil.XmlToBean(param, ResDeptTmisVo.class);
			if(CommonUtils.isEmptyString(requ.getOrgCode())){
				responseVo.setResultContent("未获取到机构编码");
				return XmlUtil.beanToXml(responseVo, ResponseImisVo.class);
			}
		}else{
			responseVo.setResultContent("未获取到信息");
			return XmlUtil.beanToXml(responseVo, ResponseImisVo.class);
		}
		requ = (ResDeptTmisVo) XmlUtil.XmlToBean(param, ResDeptTmisVo.class);
		Map<String,Object> map=new HashMap<String, Object>();
		map.put("caseNum", requ.getCaseNum());
		List<ResponseDeptVo> deptList = DataBaseHelper.queryForList(
						"select org.code_org hospHISCode,org.name_org Hosp_Name,bo.code_dept code,bo.name_dept name,bo.FLAG_OP Is_Out_Patient,bo.FLAG_IP Is_In_Patient from BD_OU_DEPT bo left join BD_OU_ORG org on org.PK_ORG=bo.PK_ORG  where org.code_org=? and bo.FLAG_ACTIVE='1' and bo.DEL_FLAG='0' ",
						ResponseDeptVo.class,requ.getOrgCode());
		if(deptList==null || deptList.size()==0){
			responseVo.setResultContent("未查询到科室信息");
			return XmlUtil.beanToXml(responseVo, ResponseImisVo.class);
		}
		ResponseDeptSVo responseDeptSVo=new ResponseDeptSVo();
		responseDeptSVo.setDeptInfo(deptList);
		responseVo.setDeptInfos(responseDeptSVo);
		responseVo.setResultCode("0");
		responseVo.setResultContent("成功");
		return  XmlUtil.beanToXml(responseVo, RespDeptImisVo.class, false);
	}
	/**
	 * 查询病区列表信息
	 * @param param
	 * @return
	 */
	@Override
	public String queryAreas(String param) {
		// TODO Auto-generated method stub
		logger.info("queryDepts查询科室列表信息接口：{}",param);
		ResDeptTmisVo requ = null;
		RespAreaImisVo responseVo = new RespAreaImisVo();
		responseVo.setResultCode("1");
		if(StringUtils.isNotBlank(param)){
			requ = (ResDeptTmisVo) XmlUtil.XmlToBean(param, ResDeptTmisVo.class);
			if(CommonUtils.isEmptyString(requ.getOrgCode())){
				responseVo.setResultContent("未获取到机构编码");
				return XmlUtil.beanToXml(responseVo, ResponseImisVo.class);
			}
		}else{
			responseVo.setResultContent("未获取到信息");
			return XmlUtil.beanToXml(responseVo, ResponseImisVo.class);
		}
		requ = (ResDeptTmisVo) XmlUtil.XmlToBean(param, ResDeptTmisVo.class);
		Map<String,Object> map=new HashMap<String, Object>();
		map.put("caseNum", requ.getCaseNum());
		List<ResponseSickRoomInfoVo> deptList = DataBaseHelper.queryForList(
						"select org.code_org HospHISCode,org.name_org Hosp_Name,bo.code_dept code,bo.name_dept name,bo.FLAG_OP Is_Out_Patient,bo.FLAG_IP Is_In_Patient from BD_OU_DEPT bo left join BD_OU_ORG org on org.PK_ORG=bo.PK_ORG  where org.code_org=? and bo.FLAG_ACTIVE='1' and bo.DEL_FLAG='0'  and bo.DT_DEPTTYPE='02'",
						ResponseSickRoomInfoVo.class,requ.getOrgCode());
		if(deptList==null || deptList.size()==0){
			responseVo.setResultContent("未查询到病区信息");
			return XmlUtil.beanToXml(responseVo, ResponseImisVo.class);
		}
		ResponseSickRoomSInfoVo responseSickRoomInfoVo=new ResponseSickRoomSInfoVo();
		responseSickRoomInfoVo.setSickRoomInfo(deptList);
		responseVo.setSickRoomInfos(responseSickRoomInfoVo);
		responseVo.setResultCode("0");
		responseVo.setResultContent("成功");
		return  XmlUtil.beanToXml(responseVo, responseVo.getClass(), false);
	}
	/**
	 * 查询职工列表信息
	 * @param param
	 * @return
	 */
	@Override
	public String queryEmployees(String param) {
		logger.info("queryEmployees查询职工列表信息接口：{}",param);
		ResDeptTmisVo requ = null;
		RespEmpImisVo responseVo = new RespEmpImisVo();
		responseVo.setResultCode("1");
		if(StringUtils.isNotBlank(param)){
			requ = (ResDeptTmisVo) XmlUtil.XmlToBean(param, ResDeptTmisVo.class);
			if(CommonUtils.isEmptyString(requ.getOrgCode())){
				responseVo.setResultContent("未获取到机构编码");
				return XmlUtil.beanToXml(responseVo, ResponseImisVo.class);
			}
		}else{
			responseVo.setResultContent("未获取到信息");
			return XmlUtil.beanToXml(responseVo, ResponseImisVo.class);
		}
		requ = (ResDeptTmisVo) XmlUtil.XmlToBean(param, ResDeptTmisVo.class);
		String sql="select org.code_org HospHISCode,org.name_org Hosp_Name,be.code_emp code,be.name_emp name,bd.code_dept dept_Code,be.dt_emptype doctorTypeID,"
				+"be.dt_sex sexID from BD_OU_EMPLOYEE be "
				+" left join BD_OU_EMPJOB bj on be.pk_emp=bj.pk_emp and bj.IS_MAIN='1' "
				+" left join BD_OU_DEPT bd on bd.PK_DEPT=bj.PK_DEPT "
				+" left join BD_OU_ORG org on org.pk_org=be.pk_org "
				+" where be.DEL_FLAG='0' and be.FLAG_ACTIVE='1' and  org.code_org=?";
		List<ResponseStaffInfoVo> deptList = DataBaseHelper.queryForList(sql,ResponseStaffInfoVo.class,requ.getOrgCode());
		if(deptList==null || deptList.size()==0){
			responseVo.setResultContent("未查询到职工信息");
			return XmlUtil.beanToXml(responseVo, ResponseImisVo.class);
		}
		ResponseStaffSInfoVo responseStaffSInfoVo=new ResponseStaffSInfoVo();
		responseStaffSInfoVo.setSickRoomInfo(deptList);
		responseVo.setSickRoomInfos(responseStaffSInfoVo);
		responseVo.setResultCode("0");
		responseVo.setResultContent("成功");
		return  XmlUtil.beanToXml(responseVo, responseVo.getClass(), false);
	}
	/**
	 * 查询收费项目信息
	 * @param param
	 * @return
	 */
	@Override
	public String queryBdItem(String param) {
		logger.info("queryBdItem查询收费项目信息接口："+param);
		ResDeptTmisVo requ = null;
		RespItemImisVo responseVo = new RespItemImisVo();
		responseVo.setResultCode("1");
		if(StringUtils.isNotBlank(param)){
			requ = (ResDeptTmisVo) XmlUtil.XmlToBean(param, ResDeptTmisVo.class);
			if(CommonUtils.isEmptyString(requ.getOrgCode())){
				responseVo.setResultContent("未获取到机构编码");
				return XmlUtil.beanToXml(responseVo, ResponseImisVo.class);
			}
		}else{
			responseVo.setResultContent("未获取到信息");
			return XmlUtil.beanToXml(responseVo, ResponseImisVo.class);
		}
		requ = (ResDeptTmisVo) XmlUtil.XmlToBean(param, ResDeptTmisVo.class);
		Map<String,Object> orgMap=getOrgByCode(requ.getOrgCode());
		List<ResponseFeeItemInfoVo> deptList = DataBaseHelper.queryForList("select bi.code,bi.name,bi.DT_ITEMTYPE Fee_Type_ID,unit.name unit,bi.price from bd_item  bi inner join bd_unit unit on bi.pk_unit = unit.pk_unit where bi.DT_ITEMTYPE='09'",ResponseFeeItemInfoVo.class);
		if(deptList==null || deptList.size()==0){
			responseVo.setResultContent("未查询到收费项目信息");
			return XmlUtil.beanToXml(responseVo, ResponseImisVo.class);
		}
		for (ResponseFeeItemInfoVo responseFeeItemInfoVo : deptList) {
			responseFeeItemInfoVo.setHospHISCode(orgMap.get("codeOrg").toString());
			responseFeeItemInfoVo.setHospName(orgMap.get("nameOrg").toString());
		}
		ResponseFeeItemSInfoVo responseFeeItemSInfoVo=new ResponseFeeItemSInfoVo();
		responseFeeItemSInfoVo.setSickRoomInfo(deptList);
		responseVo.setResponseFeeItemSInfoVo(responseFeeItemSInfoVo);
		responseVo.setResultCode("0");
		responseVo.setResultContent("成功");
		return  XmlUtil.beanToXml(responseVo, responseVo.getClass(), false);
	}
	/**
	 * 查询患者信息
	 * @param param
	 * @return
	 */
	@Override
	public String queryPiMaster(String param) {
		// TODO Auto-generated method stub
		logger.info("queryPiMaster查询患者信息接口：{}",param);
		ResDeptTmisVo requ = null;
		ResponseImisVo responseVo = new ResponseImisVo();
		responseVo.setResultCode("1");
		if(StringUtils.isNotBlank(param)){
			requ = (ResDeptTmisVo) XmlUtil.XmlToBean(param, ResDeptTmisVo.class);
			if(CommonUtils.isEmptyString(requ.getOrgCode())){
				responseVo.setResultContent("未获取到机构编码");
				return XmlUtil.beanToXml(responseVo, ResponseImisVo.class);
			}
			if(CommonUtils.isEmptyString(requ.getEuPvtype())){
				responseVo.setResultContent("未获取到就诊类型");
				return XmlUtil.beanToXml(responseVo, ResponseImisVo.class);
			}
			if(CommonUtils.isEmptyString(requ.getCaseNum())){
				responseVo.setResultContent("未获取到病案号");
				return XmlUtil.beanToXml(responseVo, ResponseImisVo.class);
			}	
		}else{
			responseVo.setResultContent("未获取到信息");
			return XmlUtil.beanToXml(responseVo, ResponseImisVo.class);
		}
		requ = (ResDeptTmisVo) XmlUtil.XmlToBean(param, ResDeptTmisVo.class);
		Map<String,Object> map=new HashMap<String, Object>();
		if("10".equals(requ.getEuPvtype())){
			map.put("codeOp", requ.getCaseNum());
		}else{
			map.put("codeIp", requ.getCaseNum());
		}
		List<Map<String,Object>> list=pvPubForWsService.queryPiMasterByTmis(map);
		if(list==null || list.size()==0){
			responseVo.setResultContent("未查询到患者信息");
			return XmlUtil.beanToXml(responseVo, ResponseImisVo.class);
		}
		Map<String,Object> mapPi=list.get(0);
		PatientInfoVo patientInfoVo=JSONObject.parseObject(JSONObject.toJSONString(mapPi), PatientInfoVo.class);
		responseVo.setPatientInfoVo(patientInfoVo);
		responseVo.setResultCode("0");
		responseVo.setResultContent("成功");
		return  XmlUtil.beanToXml(responseVo, responseVo.getClass(), false);
	}
	/**
	 * 查询患者血型分布
	 * @param param
	 * @return
	 */
	@Override
	public String queryMasterBlood(String param) {
		// TODO Auto-generated method stub
		logger.info("queryPiMaster查询患者信息接口："+param);
		ResDeptTmisVo requ = null;
		ResponseImisVo responseVo = new ResponseImisVo();
		responseVo.setResultCode("1");
		if(StringUtils.isNotBlank(param)){
			requ = (ResDeptTmisVo) XmlUtil.XmlToBean(param, ResDeptTmisVo.class);
			List<RequestDeptTmisVo> l=requ.getDeptIDs();
			if(null==l || l.size()==0){
				responseVo.setResultContent("未获取到科室id");
				return XmlUtil.beanToXml(responseVo, ResponseImisVo.class);
			}
		}else{
			responseVo.setResultContent("未获取到信息");
			return XmlUtil.beanToXml(responseVo, ResponseImisVo.class);
		}
		requ = (ResDeptTmisVo) XmlUtil.XmlToBean(param, ResDeptTmisVo.class);
		List<RequestDeptTmisVo> l=requ.getDeptIDs();
		List<String> pkdepts=new ArrayList<String>();
		for (RequestDeptTmisVo requestDeptTmisVo : l) {
			pkdepts.add(requestDeptTmisVo.getDeptID());
		}
		List<Map<String,Object>> list=pvPubForWsService.queryMasterBlood(pkdepts);
		if(list==null || list.size()==0){
			responseVo.setResultContent("未查询到科室血型分布信息");
			return XmlUtil.beanToXml(responseVo, ResponseImisVo.class);
		}
		//处理血型
		List<BldTypeDisInfoVo> listType=new ArrayList<BldTypeDisInfoVo>();
		int x=1;
		for (RequestDeptTmisVo requestDeptTmisVo : l) {
			for (Map<String, Object> map2 : list) {
				if(map2.get("pkDept").equals(requestDeptTmisVo.getDeptID())){
					BldTypeDisInfoVo bldType=new BldTypeDisInfoVo();
					bldType.setDeptHISCode(map2.get("codeDept").toString());
					bldType.setDeptName(map2.get("nameDept").toString());
					bldType.setDeptSort(String.valueOf(x));
					Integer count=Integer.parseInt(String.valueOf(map2.get("count")));
					Object aboCode=map2.get("aboCode");
					Object rhCode=map2.get("rhCode");
					if(null!=aboCode){
						//a型
						if("01".equals(aboCode) && "02".equals(rhCode)){
							bldType.setaPosCount(count);
						}
						if("01".equals(aboCode) && "01".equals(rhCode)){
							bldType.setaNegCount(count);
						}
						if(aboCode!=null && "01".equals(aboCode) && null==rhCode){
							bldType.setaXCount(count);
						}
						//b型
						if("02".equals(aboCode) && "02".equals(rhCode)){
							bldType.setbPosCount(count);
						}
						if("02".equals(aboCode) && "01".equals(rhCode)){
							bldType.setbNegCount(count);
						}
						if(aboCode!=null && "02".equals(aboCode) && null==rhCode){
							bldType.setbXCount(count);
						}
						//o型
						if("03".equals(aboCode) && "02".equals(rhCode)){
							bldType.setoPosCount(count);
						}
						if("03".equals(aboCode) && "01".equals(rhCode)){
							bldType.setoNegCount(count);
						}
						if(aboCode!=null && "03".equals(aboCode) && null==rhCode){
							bldType.setoXCount(count);
						}
						//ab型
						if("04".equals(aboCode) && "02".equals(rhCode)){
							bldType.setaBPosCount(count);
						}
						if("04".equals(aboCode) && "01".equals(rhCode)){
							bldType.setaBNegCount(count);
						}
						if(aboCode!=null && "04".equals(aboCode) && null==rhCode){
							bldType.setaBXCount(count);
						}
					}else{
						if(null==rhCode){
							bldType.setxCount(count);
						}
						if("02".equals(rhCode)){
							bldType.setxPosCount(count);
						}
						if("01".equals(rhCode)){
							bldType.setxNegCount(count);
						}
					}
					listType.add(bldType);
				}
			}
			x++;
		}
		//汇总血型数
		for (BldTypeDisInfoVo bldTypeDisInfoVo : listType) {
			bldTypeDisInfoVo.setaCount(bldTypeDisInfoVo.getaPosCount()+bldTypeDisInfoVo.getaNegCount()+bldTypeDisInfoVo.getaXCount());//a型总人数
			bldTypeDisInfoVo.setbCount(bldTypeDisInfoVo.getbPosCount()+bldTypeDisInfoVo.getbNegCount()+bldTypeDisInfoVo.getbXCount());//b型总人数
			bldTypeDisInfoVo.setoCount(bldTypeDisInfoVo.getoPosCount()+bldTypeDisInfoVo.getoNegCount()+bldTypeDisInfoVo.getoXCount());//o型总人数
			bldTypeDisInfoVo.setaBCount(bldTypeDisInfoVo.getaBPosCount()+bldTypeDisInfoVo.getaBNegCount()+bldTypeDisInfoVo.getaBXCount());//ab型总人数
			
			bldTypeDisInfoVo.setAllCount(bldTypeDisInfoVo.getaCount()+bldTypeDisInfoVo.getbCount()+bldTypeDisInfoVo.getoCount()+bldTypeDisInfoVo.getaBCount()+bldTypeDisInfoVo.getxCount());
		}
		responseVo.setBldTypeDisInfos(listType);
		responseVo.setResultCode("0");
		responseVo.setResultContent("成功");
		return  XmlUtil.beanToXml(responseVo, responseVo.getClass(), false);
	}
	/**
	 * 异体输血-医嘱回传(新增) 
	 * @param param
	 * @return
	 */
	@Override
	public String saveBlood(String param) {
		// TODO Auto-generated method stub
		logger.info("saveBlood异体输血-医嘱回传接口："+param);
		RequestTmisVo requ = null;
		ResponseImisVo responseVo = new ResponseImisVo();
		responseVo.setResultCode("1");
		if(StringUtils.isNotBlank(param)){
			requ = (RequestTmisVo) XmlUtil.XmlToBean(param, RequestTmisVo.class);
			if(null==requ.getDoctorAdvice() || requ.getDoctorAdvice().size()==0){
				if(CommonUtils.isEmptyString(requ.getCaseNum())){
					responseVo.setResultContent("未获取到回传的医嘱信息");
					return XmlUtil.beanToXml(responseVo, ResponseImisVo.class);
				}	
			}
		}else{
			responseVo.setResultContent("未获取到信息");
			return XmlUtil.beanToXml(responseVo, ResponseImisVo.class);
		}
		requ = (RequestTmisVo) XmlUtil.XmlToBean(param, RequestTmisVo.class);
		List<DoctorAdviceVo> listDoc=requ.getDoctorAdvice();
		List<ResponseDoctorAdviceVo> doctorAdvices=new ArrayList<ResponseDoctorAdviceVo>();
		for (DoctorAdviceVo doctorAdviceVo : listDoc) {
			String codeOrg=doctorAdviceVo.getHospHISCode();
			BdOuOrg bdOuOrg=DataBaseHelper.queryForBean("select * from bd_ou_org where code_org=?",
       			 BdOuOrg.class, new Object[]{codeOrg});
			String pkOrg=bdOuOrg.getPkOrg();
			PvEncounter pvEncounter=null;
			if(!CommonUtils.isEmptyString(doctorAdviceVo.getCaseNum())){
				pvEncounter=DataBaseHelper.queryForBean("select * from PV_ENCOUNTER where eu_status=1 and flag_in=1 and DEL_FLAG='0' and CODE_IP=?",
						PvEncounter.class, new Object[]{doctorAdviceVo.getCaseNum()});
			}
			if(null==pvEncounter || CommonUtils.isEmptyString(pvEncounter.getPkPv())){
				responseVo.setResultContent("患者不能为空");
				return XmlUtil.beanToXml(responseVo, ResponseImisVo.class);
			}
			String doctorAdviceMapInfoID=doctorAdviceVo.getDoctorAdviceMapInfoID();
			if (StringUtils.isEmpty(doctorAdviceMapInfoID)) {
				responseVo.setResultContent("医嘱唯一标识不能为空");
				return XmlUtil.beanToXml(responseVo, ResponseImisVo.class);
			}
			int codeLen=doctorAdviceMapInfoID.length();
			String number = numberGen(32-4-4-codeLen);
			//医嘱主键
			String pkCnord=prefixStr+doctorAdviceMapInfoID+pleStr+number;
			String pkPv =pvEncounter.getPkPv();
			String codeOrd=doctorAdviceVo.getDoctorAdviceHisCode();//医嘱编码
			String dosage = doctorAdviceVo.getBagCount();// 剂量
			String quan = doctorAdviceVo.getCount();// 用量 
			String pkOrgExec =pkOrg;// 执行机构
			String pkDeptExec =null; //doctorAdviceVo.get;// 执行科室
			if (StringUtils.isEmpty(pkDeptExec)) {
				responseVo.setResultContent("执行科室不能为空");
				return XmlUtil.beanToXml(responseVo, ResponseImisVo.class);
			}
			
			String pkUnit = doctorAdviceVo.getSpec();// 单位名称
			String pkDept = doctorAdviceVo.getDeptHISCode();// 开立科室
			String pkDeptNs =pvEncounter.getPkDeptNs();// 护理单元
			
			String pkEmpInput = doctorAdviceVo.getApplyDoctorHISCode();// 录入人
			String pkEmpOrd = doctorAdviceVo.getApplyDoctorHISCode();// 开立医生
			
			String codeApply=doctorAdviceVo.getApplyNum();//申请单号
			
			if (StringUtils.isEmpty(pkOrg) || StringUtils.isEmpty(pkPv) || StringUtils.isEmpty(codeOrd)
					|| StringUtils.isEmpty(pkDept) || StringUtils.isEmpty(dosage) || StringUtils.isEmpty(quan)
					|| StringUtils.isEmpty(pkOrgExec) || StringUtils.isEmpty(pkDeptExec) || StringUtils.isEmpty(pkUnit)
					|| StringUtils.isEmpty(pkDeptNs) || StringUtils.isEmpty(pkEmpInput) || StringUtils.isEmpty(pkEmpOrd)) {
				responseVo.setResultContent("必填项不能为空");
				return XmlUtil.beanToXml(responseVo, ResponseImisVo.class);
			}
			
			Map<String, Object> paramMap =new HashMap<String, Object>();
			paramMap.put("codeApply", codeApply);
			
			paramMap.put("pkCnord", pkCnord);
			ResponseDoctorAdviceVo responseDoctorAdviceVo=new ResponseDoctorAdviceVo();
			try {
				// 参数判断
				ApplicationUtils apputil = new ApplicationUtils();
				User u = new User();
				UserContext.setUser(u);
				u.setPkEmp("extwebservice");

				// 获取医嘱序号
				Map<String, Object> paramOrdsn = new HashMap<String, Object>();
				paramOrdsn.put("tableName", "CN_ORDER");
				paramOrdsn.put("fieldName", "ORDSN");
				paramOrdsn.put("count", 1);
				ResponseJson ordsn = apputil.execService("CN", "BdSnService", "getSerialNo", paramOrdsn, u);
				if (ordsn.getStatus() == 0) {
					paramMap.put("ordsn", Integer.parseInt(ordsn.getData().toString()));
					paramMap.put("ordsnParent", Integer.parseInt(ordsn.getData().toString()));
				} else {
					responseVo.setResultContent("获取医嘱号时执行失败");
					return XmlUtil.beanToXml(responseVo, ResponseImisVo.class);
				}

				// 获取医嘱主键
				Map<String, Object> ord = getOrdByCode(codeOrd, pkOrg, "12");// 输血类型为12
				if (ord != null) {
					paramMap.put("codeOrdtype", "12");
					paramMap.put("pkOrd", ord.get("pkOrd"));
					paramMap.put("nameOrd", ord.get("name"));
					paramMap.put("descOrd", ord.get("name"));
					paramMap.put("priceCg", ord.get("price"));

				} else {
					responseVo.setResultContent("根据医嘱编码未获取到对应的医嘱信息");
					return XmlUtil.beanToXml(responseVo, ResponseImisVo.class);
				}

				// 获取医嘱项目单位
				Map<String, Object> unit = getUnitByName(pkUnit);
				if (unit != null) {
					paramMap.put("pkUnit", unit.get("pkUnit"));
					paramMap.put("pkUnitDos", unit.get("pkUnit"));
				} else {
					
					responseVo.setResultContent("根据单位名称未获取单位信息");
					return XmlUtil.beanToXml(responseVo, ResponseImisVo.class);
				}

				// 获取机构信息
				Map<String, Object> org = getOrgByCode(pkOrg);
				if (org != null) {
					paramMap.put("pkOrg", org.get("pkOrg"));
					u.setPkOrg(org.get("pkOrg").toString());
				} else {
					responseVo.setResultContent("根据机构编码未获取到机构主键");
					return XmlUtil.beanToXml(responseVo, ResponseImisVo.class);
				}

				Map<String, Object> orgExec = getOrgByCode(pkOrgExec);
				if (orgExec != null) {
					paramMap.put("pkOrgExec", orgExec.get("pkOrg"));
				} else {
					responseVo.setResultContent("根据执行机构编码未获取到机构主键");
					return XmlUtil.beanToXml(responseVo, ResponseImisVo.class);
				}

				// 获取科室信息
				Map<String, Object> dept = getDeptByCode(pkDept, paramMap.get("pkOrg").toString());
				if (dept != null) {
					paramMap.put("pkDept", dept.get("pkDept"));
				} else {
					responseVo.setResultContent("根据开立科室编码未获取到科室主键");
					return XmlUtil.beanToXml(responseVo, ResponseImisVo.class);
				}
				Map<String, Object> deptExec = getDeptByCode(pkDeptExec, paramMap.get("pkOrg").toString());
				if (deptExec != null) {
					paramMap.put("pkDeptExec", deptExec.get("pkDept"));
				} else {
					responseVo.setResultContent("根据执行科室编码未获取到科室主键");
					return XmlUtil.beanToXml(responseVo, ResponseImisVo.class);
				}
				Map<String, Object> deptNs = getDeptByCode(pkDeptNs, paramMap.get("pkOrg").toString());
				if (deptNs != null) {
					paramMap.put("pkDeptNs", deptNs.get("pkDept"));
				} else {
					responseVo.setResultContent("根据开立病区编码未获取到科室主键");
					return XmlUtil.beanToXml(responseVo, ResponseImisVo.class);
				}

				// 获取患者信息
				Map<String, Object> pi = getPiByCode(pkPv);
				if (pi != null) {
					paramMap.put("pkPi", pi.get("pkPi"));
				} else {
					responseVo.setResultContent("根据患者就诊主键未获取到患者主键");
					return XmlUtil.beanToXml(responseVo, ResponseImisVo.class);
				}

				// 获取医生信息
				Map<String, Object> emp = getEmpByCode(pkEmpInput);
				if (emp != null) {
					paramMap.put("pkEmpInput", emp.get("pkUser"));
					paramMap.put("nameEmpInput", emp.get("nameUser"));
				} else {
					responseVo.setResultContent("根据录入人编码未获取到录入人信息");
					return XmlUtil.beanToXml(responseVo, ResponseImisVo.class);
				}
				Map<String, Object> emp1 = getEmpByCode(pkEmpOrd);
				if (emp1 != null) {
					paramMap.put("pkEmpOrd", emp1.get("pkEmp"));
					paramMap.put("nameEmpOrd", emp1.get("nameEmp"));
				} else {
					responseVo.setResultContent("根据开立人编码未获取到开立人信息");
					return XmlUtil.beanToXml(responseVo, ResponseImisVo.class);
				}
				
				//组装输血申请单
				Map<String, Object>  bloodPatientVO= new HashMap<String, Object>();
				bloodPatientVO.put("dtBttype", doctorAdviceVo.getApplyTypeName());//输血性质
				bloodPatientVO.put("dtBtAbo", doctorAdviceVo.getaBORhD());
				bloodPatientVO.put("pkUnitBt",unit);
				bloodPatientVO.put("quanBt",doctorAdviceVo.getBagCount()); 
				bloodPatientVO.put("datePlan",doctorAdviceVo.getPreTransDate());
				String applyNum=doctorAdviceVo.getApplyNum();
				if (StringUtils.isEmpty(applyNum)) {
					responseVo.setResultContent("申请单号不能为空");
					return XmlUtil.beanToXml(responseVo, ResponseImisVo.class);
				}
				int appLen=applyNum.length();
				String numberapp = numberGen(32-4-4-appLen);
				bloodPatientVO.put("PkOrdbt",prefixStr+doctorAdviceMapInfoID+pleStr+numberapp);
				paramMap.put("bloodPatientVO", bloodPatientVO);
				logger.info("saveBlood:调用医嘱回写方法");
				ResponseJson rs = apputil.execService("CN", "BloodService", "saveBlood", paramMap, u);
				logger.info("saveBlood:调用医嘱回写完成");
				if (rs.getStatus() == 0) {
					responseDoctorAdviceVo.setDoctorAdviceMapInfo(doctorAdviceMapInfoID);
					responseDoctorAdviceVo.setDoctorAdviceNumFee(ordsn.getData().toString());
					doctorAdvices.add(responseDoctorAdviceVo);
					/*return CommonUtils
							.getString(new OtherRespJson("0|成功|null|" + ApplicationUtils.beanToJson(rs.getData())));*/
				}
				String errMsg=rs.getErrorMessage();
				if (!StringUtils.isEmpty(rs.getDesc())) {
					errMsg=rs.getDesc();
				}
				responseVo.setResultContent("调用业务方法失败:"+errMsg);
				return XmlUtil.beanToXml(responseVo, ResponseImisVo.class);
			} catch (Exception e) {
				responseVo.setResultContent("调用业务方法失败:"+e.getMessage());
				return XmlUtil.beanToXml(responseVo, ResponseImisVo.class);
			}
		}
		responseVo.setDoctorAdvices(doctorAdvices);
		responseVo.setResultCode("0");
		responseVo.setResultContent("成功");
		return XmlUtil.beanToXml(responseVo, responseVo.getClass(), false);
	}
	/**
	 * 异体输血-医嘱回传(作废) 
	 * @param param
	 * @return
	 */
	@Override
	public String delOrder(String param) {
		// TODO Auto-generated method stub
		logger.info("delOrder作废接口："+param);
		RequestTmisVo requ = null;
		ResponseImisVo responseVo = new ResponseImisVo();
		responseVo.setResultCode("1");
		if(StringUtils.isNotBlank(param)){
			requ = (RequestTmisVo) XmlUtil.XmlToBean(param, RequestTmisVo.class);
			if(null==requ.getDoctorAdvice() || requ.getDoctorAdvice().size()==0){
				if(CommonUtils.isEmptyString(requ.getCaseNum())){
					responseVo.setResultContent("未获取到回传的医嘱信息");
					return XmlUtil.beanToXml(responseVo, ResponseImisVo.class);
				}	
			}
		}else{
			responseVo.setResultContent("未获取到信息");
			return XmlUtil.beanToXml(responseVo, ResponseImisVo.class);
		}
		requ = (RequestTmisVo) XmlUtil.XmlToBean(param, RequestTmisVo.class);
		List<DoctorAdviceVo> listDoc=requ.getDoctorAdvice();
		for (DoctorAdviceVo doctorAdviceVo : listDoc) {
			String doctorAdviceMapInfoID=doctorAdviceVo.getDoctorAdviceMapInfoID();
				if (StringUtils.isEmpty(doctorAdviceMapInfoID)) {
					responseVo.setResultContent("医嘱业务唯一标识不能为空");
					return XmlUtil.beanToXml(responseVo, ResponseImisVo.class);
				}
				String str=prefixStr+doctorAdviceMapInfoID;
				CnOrder cnorder=null;
				cnorder=DataBaseHelper.queryForBean("select * from cn_order where PK_CNORD like '"+str+"%'", CnOrder.class, doctorAdviceMapInfoID);
				if("3".equals(cnorder.getEuStatusOrd()) || "3".equals(cnorder.getEuStatusOrd()) || "9".equals(cnorder.getEuStatusOrd())){
					responseVo.setResultContent("当前医嘱状态不允许作废。");
					return XmlUtil.beanToXml(responseVo, ResponseImisVo.class);
				}
				CnTransApply cnt=null;
				cnt=DataBaseHelper.queryForBean("select * from CN_TRANS_APPLY where pk_cnord=? and DEL_FLAG='0'", CnTransApply.class, cnorder.getPkCnord());
				if(null==cnt || !"0".equals(cnt.getEuStatus())){
					responseVo.setResultContent("当前申请单状态不允许作废。");
					return XmlUtil.beanToXml(responseVo, ResponseImisVo.class);
				}
				Map<String,Object> map=DataBaseHelper.queryForMap("select nvl(sum(AMOUNT),0) amount from BL_IP_DT where PK_CNORD=? and DEL_FLAG='0'", cnorder.getPkCnord());
				Double amount=Double.valueOf(String.valueOf(map.get("amount")));
				if(amount>0){
					responseVo.setResultContent("当前医嘱已经计费，不允许作废。");
					return XmlUtil.beanToXml(responseVo, ResponseImisVo.class);
				}
				// 获取医生信息
				Map<String, Object> emp = getEmpByCode(doctorAdviceVo.getApplyDoctorHISCode());
				if (emp != null) {
					cnorder.setPkEmpErase(emp.get("pkUser").toString());
					cnorder.setNameEmpErase(emp.get("nameUser").toString());
				} else {
					responseVo.setResultContent("根据医生编码未获取到医生信息");
					return XmlUtil.beanToXml(responseVo, ResponseImisVo.class);
				}
				cnorder.setEuStatusOrd("9");
				cnorder.setDateErase(new Date());
				DataBaseHelper.updateBeanByPk(cnorder);
		}
		responseVo.setResultCode("0");
		responseVo.setResultContent("成功");
		return XmlUtil.beanToXml(responseVo, responseVo.getClass(), false);
	}
	/**
	 * 输血计费
	 * @param param
	 * @return
	 */
	@Override
	public String chargingBlood(String param) {
		// TODO Auto-generated method stub
		logger.info("输血计费接口{}",param);
		RequestTmisVo requ = null;
		ResponseImisVo responseVo = new ResponseImisVo();
		String respon = null;
		responseVo.setResultCode("1");
		if(StringUtils.isNotBlank(param)){
			requ = (RequestTmisVo) XmlUtil.XmlToBean(param, RequestTmisVo.class);
			if(requ.getFeeInfo().size()<=0){
				responseVo.setResultContent("未获取到回传的医嘱信息");
				respon=XmlUtil.beanToXml(responseVo, ResponseImisVo.class);
				logger.info("输血计费接口{}",respon);
				return respon;	
			}
		}else{
			responseVo.setResultContent("未获取到信息");
			respon=XmlUtil.beanToXml(responseVo, ResponseImisVo.class);
			logger.info("输血计费接口{}",respon);
			return respon;
		}
	
		List<FeeInfoVo> listDoc=requ.getFeeInfo();
		PvEncounter pvEncounter=null;
		BdOuOrg bdOuOrg=null;
		User user=null;
		String pkOrdexdt=null;//记费唯一标识判断
		List<BlPubParamVo> dtAllList = Lists.newArrayList();
	    
		for(FeeInfoVo feeInfoVo:listDoc){
			BlPubParamVo blPubParamVo=new BlPubParamVo();
			
			if(StringUtils.isNotBlank(feeInfoVo.getiD())){
				if(20 == feeInfoVo.getiD().length()){
					pkOrdexdt="2342002342000"+feeInfoVo.getiD();
					Map<String, Object> bdItemMaps = DataBaseHelper.queryForMap("select amount,pk_dept_ex,pk_cgip from bl_ip_dt where pk_ordexdt=? ",pkOrdexdt);
					 if(MapUtils.isNotEmpty(bdItemMaps)){
						 responseVo.setResultContent("项目已记费");
						 respon=XmlUtil.beanToXml(responseVo, ResponseImisVo.class);
						 logger.info("输血计费接口{}",respon);
						 return respon;
					 }
				}else{
					responseVo.setResultContent("获取到ID不是20位");
					respon=XmlUtil.beanToXml(responseVo, ResponseImisVo.class);
					logger.info("输血计费接口{}",respon);
					return respon;
				}
			}else{
				responseVo.setResultContent("未获取到ID");
				respon=XmlUtil.beanToXml(responseVo, ResponseImisVo.class);
				logger.info("输血计费接口{}",respon);
				return respon;
			}
			
			
			if(BeanUtils.isNotNull(pvEncounter)){
				if(StringUtils.isNotBlank(feeInfoVo.getVisitID())){
					if(!pvEncounter.getCodePv().equals(feeInfoVo.getVisitID())){
						pvEncounter=DataBaseHelper.queryForBean("select * from PV_ENCOUNTER where eu_status=1 and flag_in=1 and DEL_FLAG='0' and code_pv=?",PvEncounter.class, new Object[]{feeInfoVo.getVisitID()});
					    if(!BeanUtils.isNotNull(pvEncounter)){
					    	responseVo.setResultContent("未获取到有效患者信息");
							respon=XmlUtil.beanToXml(responseVo, ResponseImisVo.class);
							logger.info("输血计费接口{}",respon);
							return respon;	
					    }
					} 
				}else{
					responseVo.setResultContent("未获取到就诊流水号VisitID");
					respon=XmlUtil.beanToXml(responseVo, ResponseImisVo.class);
					logger.info("输血计费接口{}",respon);
					return respon;	
				}
			}else{
				if(StringUtils.isNotBlank(feeInfoVo.getVisitID())){
					pvEncounter=DataBaseHelper.queryForBean("select * from PV_ENCOUNTER where eu_status=1 and flag_in=1 and DEL_FLAG='0' and code_pv=?",PvEncounter.class, new Object[]{feeInfoVo.getVisitID()});
				    if(!BeanUtils.isNotNull(pvEncounter)){
				    	responseVo.setResultContent("未获取到有效患者信息");
						respon=XmlUtil.beanToXml(responseVo, ResponseImisVo.class);
						logger.info("输血计费接口{}",respon);
						return respon;	
				    }
				}else{
					responseVo.setResultContent("未获取到就诊流水号VisitID");
					respon=XmlUtil.beanToXml(responseVo, ResponseImisVo.class);
					logger.info("输血计费接口{}",respon);
					return respon;
				}
			}
			
			if(BeanUtils.isNotNull(user)){
				if(StringUtils.isNotBlank(feeInfoVo.getTollCollectorCode())){
					if(!user.getCodeEmp().equals(feeInfoVo.getTollCollectorCode())){
						user = LbSelfUtil.getDefaultUser(feeInfoVo.getTollCollectorCode());
					    if(!BeanUtils.isNotNull(user)){
					    	responseVo.setResultContent("未获取到操作员信息");
							respon=XmlUtil.beanToXml(responseVo, ResponseImisVo.class);
							logger.info("输血计费接口{}",respon);
							return respon;	
					    }
					} 
				}else{
					responseVo.setResultContent("未获取到操作员信息");
					respon=XmlUtil.beanToXml(responseVo, ResponseImisVo.class);
					logger.info("输血计费接口{}",respon);
					return respon;	
				}
			}else{
				if(StringUtils.isNotBlank(feeInfoVo.getTollCollectorCode())){
					user = LbSelfUtil.getDefaultUser(feeInfoVo.getTollCollectorCode());
				    if(!BeanUtils.isNotNull(user)){
				    	responseVo.setResultContent("未获取到操作员信息");
						respon=XmlUtil.beanToXml(responseVo, ResponseImisVo.class);
						logger.info("输血计费接口{}",respon);
						return respon;	
				    }
				}else{
					responseVo.setResultContent("未获取到操作员信息");
					respon=XmlUtil.beanToXml(responseVo, ResponseImisVo.class);
					logger.info("输血计费接口{}",respon);
					return respon;
				}
			}
			
			if(BeanUtils.isNotNull(bdOuOrg)){
				if(StringUtils.isNotBlank(feeInfoVo.getHospHISCode())){
					if(!bdOuOrg.getCodeOrg().equals(feeInfoVo.getHospHISCode())){
						bdOuOrg = DataBaseHelper.queryForBean("select * from bd_ou_org where code_org=? ", BdOuOrg.class, new Object[]{feeInfoVo.getHospHISCode()});
					    if(!BeanUtils.isNotNull(bdOuOrg)){
					    	responseVo.setResultContent("未获取到操作员信息");
							respon=XmlUtil.beanToXml(responseVo, ResponseImisVo.class);
							logger.info("输血计费接口{}",respon);
							return respon;	
					    }
					} 
				}else{
					responseVo.setResultContent("未获取到操作员信息");
					respon=XmlUtil.beanToXml(responseVo, ResponseImisVo.class);
					logger.info("输血计费接口{}",respon);
					return respon;	
				}
			}else{
				if(StringUtils.isNotBlank(feeInfoVo.getHospHISCode())){
					bdOuOrg = DataBaseHelper.queryForBean("select * from bd_ou_org where code_org=? ", BdOuOrg.class, new Object[]{feeInfoVo.getHospHISCode()});
				    if(!BeanUtils.isNotNull(bdOuOrg)){
				    	responseVo.setResultContent("未获取到操作员信息");
						respon=XmlUtil.beanToXml(responseVo, ResponseImisVo.class);
						logger.info("输血计费接口{}",respon);
						return respon;	
				    }
				}else{
					responseVo.setResultContent("未获取到操作员信息");
					respon=XmlUtil.beanToXml(responseVo, ResponseImisVo.class);
					logger.info("输血计费接口{}",respon);
					return respon;
				}
			}
			
			//查询判断费用编码是否有效
			if(StringUtils.isEmpty(feeInfoVo.getFeeItemHISCode())){
				responseVo.setResultContent("未获取到收费项目编码");
				respon=XmlUtil.beanToXml(responseVo, ResponseImisVo.class);
				logger.info("输血计费接口{}",respon);
				return respon;
			}
			List<Map<String, Object>> bdItemList=DataBaseHelper.queryForList("select pk_item from bd_item where flag_active='1' and code=?", feeInfoVo.getFeeItemHISCode());
			if(bdItemList.size()<=0){
				responseVo.setResultContent("未查询到有效的费用信息信息");
				respon=XmlUtil.beanToXml(responseVo, ResponseImisVo.class);
				logger.info("输血计费接口{}",respon);
				return respon;
			}else{
				if(MapUtils.isEmpty(bdItemList.get(0))){
					responseVo.setResultContent("未查询到有效的费用信息信息");
					respon=XmlUtil.beanToXml(responseVo, ResponseImisVo.class);
					logger.info("输血计费接口{}",respon);
					return respon;
				}
			}
			
			if(StringUtils.isEmpty(feeInfoVo.getTransDeptHISCode())){
				responseVo.setResultContent("执行科室不能为空");
				respon=XmlUtil.beanToXml(responseVo, ResponseImisVo.class);
				logger.info("输血计费接口{}",respon);
				return respon;
			}
			Map<String, Object> dept=getDeptByCode(feeInfoVo.getTransDeptHISCode(),bdOuOrg.getPkOrg());
			if(MapUtils.isEmpty(dept)){
				responseVo.setResultContent("未查询到有效的执行科室信息");
				respon=XmlUtil.beanToXml(responseVo, ResponseImisVo.class);
				logger.info("输血计费接口{}",respon);
				return respon;
			}
			
			if(StringUtils.isEmpty(feeInfoVo.getApplyDeptHISCode())){
				responseVo.setResultContent("申请科室不能为空");
				respon=XmlUtil.beanToXml(responseVo, ResponseImisVo.class);
				logger.info("输血计费接口{}",respon);
				return respon;
			}
			Map<String, Object> deptApp=getDeptByCode(feeInfoVo.getApplyDeptHISCode(),bdOuOrg.getPkOrg());
			if(MapUtils.isEmpty(deptApp)){
				responseVo.setResultContent("未查询到有效的申请科室信息");
				respon=XmlUtil.beanToXml(responseVo, ResponseImisVo.class);
				logger.info("输血计费接口{}",respon);
				return respon;
			}
			if(StringUtils.isEmpty(feeInfoVo.getApplyDocCode())){
				responseVo.setResultContent("申请人不能为空");
				respon=XmlUtil.beanToXml(responseVo, ResponseImisVo.class);
				logger.info("输血计费接口{}",respon);
				return respon;
			}
			Map<String, Object> empApp=getEmpInfoByCodeAndPkOrg(feeInfoVo.getApplyDocCode(),bdOuOrg.getPkOrg());
			if(MapUtils.isEmpty(empApp)){
				responseVo.setResultContent("未查询到有效的申请人信息");
				respon=XmlUtil.beanToXml(responseVo, ResponseImisVo.class);
				logger.info("输血计费接口{}",respon);
				return respon;
			}
			if(StringUtils.isEmpty(feeInfoVo.getChargerHISCode())){
				
				respon=XmlUtil.beanToXml(responseVo, ResponseImisVo.class);
				logger.info("输血计费接口{}",respon);
				return respon;
			}
			Map<String, Object> empChApp=getEmpInfoByCodeAndPkOrg(feeInfoVo.getChargerHISCode(),bdOuOrg.getPkOrg());
			if(MapUtils.isEmpty(empChApp)){
				responseVo.setResultContent("未查询到有效的计费人信息");
				respon=XmlUtil.beanToXml(responseVo, ResponseImisVo.class);
				logger.info("输血计费接口{}",respon);
				return respon;
			}
			
			//组装blPubParamVo实体
			blPubParamVo.setPkOrdexdt(pkOrdexdt);
        	blPubParamVo.setPkOrg(bdOuOrg.getPkOrg());
        	blPubParamVo.setEuPvType("3");
        	blPubParamVo.setPkPv(pvEncounter.getPkPv());
        	blPubParamVo.setPkPi(pvEncounter.getPkPi());
        	blPubParamVo.setPkItem(LbSelfUtil.getPropValueStr(bdItemList.get(0), "pkItem"));
        	blPubParamVo.setQuanCg(feeInfoVo.getAmount().doubleValue());
        	blPubParamVo.setPkOrgEx(bdOuOrg.getPkOrg());
        	blPubParamVo.setPkOrgApp(bdOuOrg.getPkOrg());
        	blPubParamVo.setPkDeptEx(LbSelfUtil.getPropValueStr(dept, "pkDept"));
        	blPubParamVo.setPkDeptApp(LbSelfUtil.getPropValueStr(deptApp, "pkDept"));
        	blPubParamVo.setPkEmpApp(LbSelfUtil.getPropValueStr(empApp, "pkEmp"));
        	blPubParamVo.setNameEmpApp(feeInfoVo.getApplyDocName());
        	blPubParamVo.setDateHap(new Date());
        	blPubParamVo.setPkDeptCg(LbSelfUtil.getPropValueStr(dept, "pkDept"));
        	blPubParamVo.setPkEmpCg(LbSelfUtil.getPropValueStr(empChApp, "pkEmp"));
        	blPubParamVo.setNameEmpCg(LbSelfUtil.getPropValueStr(empChApp, "nameEmp"));
        	blPubParamVo.setFlagPd("0");
        	dtAllList.add(blPubParamVo);
		}
		
		UserContext userContext = new UserContext();
		userContext.setUser(user);
		ApplicationUtils apputil = new ApplicationUtils();
		ResponseJson ret = apputil.execService("bl", "IpCgPubService", "savePatiCgInfo",dtAllList, user);
		if(0 == ret.getStatus()){
			responseVo.setResultCode("0");
			responseVo.setResultContent("成功");
			respon=XmlUtil.beanToXml(responseVo, ResponseImisVo.class);
			logger.info("输血计费接口{}",respon);
			return respon;
		}else{
			responseVo.setResultContent(ret.getDesc());
			respon=XmlUtil.beanToXml(responseVo, ResponseImisVo.class);
			logger.info("输血计费接口{}",respon);
			return respon;
		}
	}
	
	/**
	 * 获取输血费用列表（）
	 * @param param
	 * @return
	 */
	@Override
	public String queryBloodCost(String param) {
		// TODO Auto-generated method stub
		logger.info("queryBloodCost查询输血费用接口："+param);
		ResDeptTmisVo requ = null;
		ResponseImisVo responseVo = new ResponseImisVo();
		responseVo.setResultCode("1");
		if(StringUtils.isNotBlank(param)){
			requ = (ResDeptTmisVo) XmlUtil.XmlToBean(param, ResDeptTmisVo.class);
			if(CommonUtils.isEmptyString(requ.getOrgCode())){
				responseVo.setResultContent("未获取到机构编码");
				return XmlUtil.beanToXml(responseVo, ResponseImisVo.class);
			}
			if(CommonUtils.isEmptyString(requ.getBusinessEndDate())){
				responseVo.setResultContent("未获取到结束日期");
				return XmlUtil.beanToXml(responseVo, ResponseImisVo.class);
			}
			if(CommonUtils.isEmptyString(requ.getBusinessStartDate())){
				responseVo.setResultContent("未获取到开始日期");
				return XmlUtil.beanToXml(responseVo, ResponseImisVo.class);
			}
		}else{
			responseVo.setResultContent("未获取到信息");
			return XmlUtil.beanToXml(responseVo, ResponseImisVo.class);
		}
		requ = (ResDeptTmisVo) XmlUtil.XmlToBean(param, ResDeptTmisVo.class);
		Map<String, Object> paramMap =new HashMap<String, Object>();
		paramMap.put("businessEndDate", requ.getBusinessEndDate());
		paramMap.put("businessStartDate", requ.getBusinessStartDate());
		paramMap.put("caseNum", requ.getCaseNum());
		paramMap.put("patientName", requ.getPatientName());
		if(StringUtils.isNotBlank(requ.getFeeItemHISCode())){
			paramMap.put("feeItemHISCode",Arrays.asList(requ.getFeeItemHISCode().split(",")));
		}
		List<ResponseHISFeeVo> list=blPubForWsService.queryBloodCost(paramMap);
		responseVo.sethISFeeLists(list);
		responseVo.setResultCode("0");
		responseVo.setResultContent("成功");
		return  XmlUtil.beanToXml(responseVo, responseVo.getClass(), false);
	}
	
	
	
	
	
	
	
	
	
	
	
	
	/**
	 * 根据单位名称获取单位信息
	 * @param name
	 */
	public Map<String, Object> getUnitByName(String name) {
		List<Map<String, Object>> unitDetils = DataBaseHelper.queryForList(
				"select pk_unit,code,name from bd_unit where name=? and del_flag='0'", name);
		if (unitDetils != null && unitDetils.size() > 0)
			return unitDetils.get(0);
		return null;
	}
	/**
	 * 根据机构编码获取机构信息
	 * @param code
	 */
	public Map<String, Object> getOrgByCode(String code) {
		List<Map<String, Object>> orgDetils = DataBaseHelper.queryForList("select pk_org,code_org,name_org,shortname from bd_ou_org where code_org = ?  and flag_active='1' and del_flag='0' ",code);
		if (orgDetils != null && orgDetils.size() > 0)
			return orgDetils.get(0);
		return null;
	}
	/**
	 * 根据科室编码获取科信息
	 * @param code
	 */
	public Map<String, Object> getDeptByCode(String code, String pkOrg) {
		List<Map<String, Object>> deptDetils = DataBaseHelper
				.queryForList(
						"select pk_dept,code_dept,name_dept,dept_type,dt_depttype from bd_ou_dept where code_dept = ? and pk_org = ? and  flag_active='1' and del_flag='0' ",
						code, pkOrg);
		if (deptDetils != null && deptDetils.size() > 0)
			return deptDetils.get(0);
		return null;
	}
	/**
	 * 根据医嘱号获取医嘱信息及收费项目
	 * @param code
	 * @return
	 */
	public Map<String, Object> getOrdByCode(String code, String pkOrg, String ordtype) {
		// 拼写sql
		StringBuffer sql = new StringBuffer();
		sql.append(" select ord.pk_ord,pk_ordtype,ord.code,ord.name,ord.code_ordtype,ord.flag_cg,ITEM.price from bd_ord ord ");
		sql.append(" left outer join (select busi.pk_ord,sum((case when busi.quan=null then 0 else busi.quan end ) *(case when item.price=null then 0 else item.price end )) as price ");
		sql.append(" from bd_ord_item busi, bd_item item where busi.pk_item = item.pk_item and busi.del_flag = '0' and item.del_flag = '0' ");
		sql.append(" group by busi.pk_ord) item on ord.pk_ord = item.pk_ord ");
		sql.append(" where ord.flag_active = '1' and ord.del_flag='0' ");
		sql.append(" and code_ordtype='" + ordtype + "'");
		sql.append(" and ord.code='" + code + "'");
		if ("0".equals(ApplicationUtils.getSysparam("YJ0009", false))) {
			sql.append(" ord.pk_org_belong ='" + pkOrg + "'");
		}
		// 查询sql
		List<Map<String, Object>> ps = DataBaseHelper.queryForList(sql.toString());
		if (ps != null && ps.size() > 0)
			return ps.get(0);
		return null;
	}
	/**
	 * 根据pkPv获取患者信息
	 * @param
	 */
	public Map<String, Object> getPiByCode(String code) {
		// 拼写sql
		StringBuffer sql = new StringBuffer();
		sql.append(" select pv.pk_pv,pv.pk_pi,pv.code_pv,pv.name_pi,pv.bed_no,pi.code_pi,pi.code_op,pi.code_ip,pv.eu_status ");
		sql.append(" FROM pv_encounter pv inner join pi_master pi on pi.pk_pi=pv.pk_pi ");
		sql.append(" where pv.del_flag='0' and pv.pk_pv='" + code + "'");
		// 查询sql
		List<Map<String, Object>> ps = DataBaseHelper.queryForList(sql.toString());
		if (ps != null && ps.size() > 0)
			return ps.get(0);
		return null;
	}
	/**
	 * 根据人员编码获取人员信息
	 * @param code
	 */
	public Map<String, Object> getEmpByCode(String code) {
		// 拼写sql
		StringBuffer sql = new StringBuffer();
		sql.append(" select emp.pk_org,emp.pk_user,emp.name_user,emp.pk_emp,emp.name_emp,ou.code_user FROM  bd_ou_user ou ");
		sql.append(" inner join bd_emp_argu emp on ou.pk_emp=emp.pk_emp and emp.del_flag='0' ");
		sql.append(" where ou.flag_active='1' and ou.code_user='" + code + "'");
		// 查询sql
		List<Map<String, Object>> ps = DataBaseHelper.queryForList(sql.toString());
		if (ps != null && ps.size() > 0)
			return ps.get(0);
		return null;
	}
	/**
     * @return java.lang.String
     * @Description 纯数字生成
     * @Author wanglei
     * @Date 2019/4/15
     * @Param [length]
     **/
    public static String numberGen(int length) {
        StringBuffer sb = new StringBuffer();
        Random random = new Random();
        for (int i = 0; i < length; i++) {
            sb.append(intArray[random.nextInt(intArray.length)]);
        }
        return sb.toString();
    }
    public Map<String, Object> getEmpInfoByCodeAndPkOrg(String codeEmp, String pkOrg) {
		StringBuffer sql = new StringBuffer();
		sql.append(" select emp.* from  bd_ou_employee emp ");
		sql.append(" where emp.pk_org = '" + pkOrg + "' and emp.code_emp='" + codeEmp + "'");
		// 查询sql
		List<Map<String, Object>> ps = DataBaseHelper.queryForList(sql.toString());
		if (ps != null && ps.size() > 0)
			return ps.get(0);
		return null;
	}
}
