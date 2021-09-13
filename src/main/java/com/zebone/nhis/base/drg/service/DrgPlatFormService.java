package com.zebone.nhis.base.drg.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;

import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.codehaus.jackson.type.TypeReference;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.zebone.nhis.base.drg.dao.DrgPlatFormMapper;
import com.zebone.nhis.base.drg.vo.BdTermCcdtVo;
import com.zebone.nhis.base.drg.vo.DepartmentVo;
import com.zebone.nhis.base.drg.vo.DrgPlatFormPvVo;
import com.zebone.nhis.base.drg.vo.PvIpVo;
import com.zebone.nhis.common.module.base.bd.code.BdDefdoc;
import com.zebone.nhis.common.module.base.bd.drg.QcEhpIndrg;
import com.zebone.nhis.common.module.mybatis.MyBatisPage;
import com.zebone.nhis.common.support.ApplicationUtils;
import com.zebone.nhis.common.support.CommonUtils;
import com.zebone.nhis.ma.pub.support.ExtSystemProcessUtils;
import com.zebone.nhis.pro.zsba.common.support.Pinyin4jUtils;
import com.zebone.platform.common.support.User;
import com.zebone.platform.framework.support.IUser;
import com.zebone.platform.modules.dao.DataBaseHelper;
import com.zebone.platform.modules.dao.support.Page;
import com.zebone.platform.modules.exception.BusException;
import com.zebone.platform.modules.utils.JsonUtil;
/**
 * drg信息平台
 * @author dell
 *
 */
@Service
public class DrgPlatFormService {
	
	@Resource
	private DrgPlatFormMapper drgPlatFormMapper;
	/**
	 * 查询患者就诊信息
	 * @param param
	 * @param user
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public DrgPlatFormPvVo queryPvEncounterList(String param,IUser user){
		DrgPlatFormPvVo qryparam = JsonUtil.readValue(param,DrgPlatFormPvVo.class);
		if (qryparam == null )
			throw new BusException("查询参数有问题！");
		Map<String,Object> paramMap = JsonUtil.readValue(param, Map.class);
		int pageIndex = CommonUtils.getInteger(qryparam.getPageIndex());
		int pageSize = CommonUtils.getInteger(qryparam.getPageSize());
		if(paramMap.get("pkOrg")==null||"".equals(paramMap.get("pkOrg").toString()))
			paramMap.put("pkOrg", ((User)user).getPkOrg());
		MyBatisPage.startPage(pageIndex, pageSize);
		List<Map<String,Object>> list=drgPlatFormMapper.queryPvEncounterList(paramMap);
		Page<List<BdTermCcdtVo>> page = MyBatisPage.getPage();
		DrgPlatFormPvVo paramPage =new DrgPlatFormPvVo();
		paramPage.setPvEncounterList(list);
		paramPage.setTotalCount(page.getTotalCount());
		return paramPage;
	}
	/**
	 * 基础信息调用接口
	 * type:
	 * 1.医院信息
	 * 2.医院信息修改
	 * 3.标准科室查询
	 * 4.科室匹配
	 * 5.科室匹配修改
	 * 6.医生信息
	 * @param param
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public Map<String,Object> uploadBasicInfo(String param,IUser user){
		if(StringUtils.isEmpty(param)){
			throw new BusException("参数为空，请检查！");
		}
		Map<String,Object> paramMap = JsonUtil.readValue(param, Map.class);
		String type=(String) paramMap.get("type");
		String methodName=null;
		if(type.equals("1")){
			methodName="HospitalInfo";
		}else if(type.equals("2")){
			methodName="HospitalInfoUpd";
		}else if(type.equals("3")){
			methodName="DeptSearch";
		}else if(type.equals("4")){
			methodName="DeptMate";
		}else if(type.equals("5")){
			methodName="DeptMateUpd";
		}else if(type.equals("6")){
			methodName="DoctorInfo";
		}
		Object result=null;
		result=ExtSystemProcessUtils.processExtMethod("DrgBasicInfo", methodName, param);
		if(type.equals("3")){
			JSONObject json =JSONObject.parseObject(result.toString());
			Integer stuCode= (Integer)json.get("CODE");
			if(200==stuCode.intValue()){
				String row=JSONObject.parseObject(json.get("DATA").toString()).get("ROWS").toString();
				List<DepartmentVo> list = JSONObject.parseArray(row,  DepartmentVo.class);
				paramMap.put("rowData", list);
				for (DepartmentVo departmentVo : list) {
					String code=departmentVo.getS_DEPARTMENT_CODE();
					int count=DataBaseHelper.queryForScalar("select count(*) from bd_defdoc where CODE_DEFDOCLIST='010202' and code=? and DEL_FLAG='0'", Integer.class, code);
					if(count>0){
						throw new BusException("科室编码"+code+"重复，请检查！");
					}
					BdDefdoc bdDefdoc=new BdDefdoc();
					bdDefdoc.setPkOrg("~                               ");
					bdDefdoc.setCodeDefdoclist("010202");
					bdDefdoc.setCode(code);
					bdDefdoc.setName(departmentVo.getS_DEPARTMENT_NAME());
					bdDefdoc.setPyCode(Pinyin4jUtils.toFirstSpell(departmentVo.getS_DEPARTMENT_NAME()));
					bdDefdoc.setdCode(Pinyin4jUtils.toFirstSpell(departmentVo.getS_DEPARTMENT_NAME()));
					ApplicationUtils.setDefaultValue(bdDefdoc, true);
					DataBaseHelper.insertBean(bdDefdoc);
				}
			}
			 
		}
		paramMap.put("result", result);
		return paramMap;
	}
	/**
	 * 业务数据处理
	 * type:
	 * 1.最小数据集
	 * 2.医院信息修改
	 * 3.病人服务明细
	 * 4.病案质控反馈
	 * 5.分组结果查询
	 * 6.上传先上传最小数据集，再上传明细
	 * @param param
	 * @param user
	 * @return
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public Map<String,Object> uploadBusinessInfo(String param,IUser user){
		if(StringUtils.isEmpty(param)){
			throw new BusException("参数为空，请检查！");
		}
		Map<String,Object> paramMap = JsonUtil.readValue(param, Map.class);
		String type=(String) paramMap.get("type");
		String methodName=null;
		if(type.equals("1")){
			methodName="MinData";
		}else if(type.equals("2")){
			methodName="MinDataOff";
		}else if(type.equals("3")){
			methodName="ServiceDetail";
		}else if(type.equals("4")){
			methodName="Control";
		}else if(type.equals("5")){
			methodName="GroupQuery";
		}else if(type.equals("6")){
			methodName="MinData";
		}
		Object result=null;
		result=ExtSystemProcessUtils.processExtMethod("DrgBusinessInfo", methodName, new Object[]{MapUtils.getString(paramMap,"pkPvs",""),MapUtils.getString(paramMap,"beginDateDrg",""),MapUtils.getString(paramMap,"endDateDrg","")});
		Map<String,Object>  resultMap= JsonUtil.readValue(result.toString(), Map.class);
		JSONObject json =JSONObject.parseObject(result.toString());
		Integer stuCode= (Integer)json.get("CODE");
		if(type.equals("1") || type.equals("6")){
			//修改状态已上传
			if(200==stuCode.intValue()){
				updateDrgStatus(paramMap.get("pkPvs").toString(),"1");
				 if(type.equals("6")){
					methodName="ServiceDetail";
					result=ExtSystemProcessUtils.processExtMethod("DrgBusinessInfo", methodName, paramMap.get("pkPvs") );
					resultMap= JsonUtil.readValue(result.toString(), Map.class);
				 }
			}
		}else if (type.equals("2")){//冲销
			//修改状态未撤销
			if(200==stuCode.intValue()){
				updateDrgStatus(paramMap.get("pkPvs").toString(),"9");
			}
		}else if (type.equals("4") || type.equals("5")){
			if(200==stuCode.intValue()){
				List<Map> list =new ArrayList<Map>();;
				if(type.equals("4")){
					String row=null;
					if(null!=json.get("DATA")){
						row=JSONObject.parseObject(json.get("DATA").toString()).get("ROWS").toString();
						list = JSONObject.parseArray(row,  Map.class);
						for (Map map : list) {
							map.put("ZYCS",map.get("ZYCS").toString());
						}
					}
				}else{
					if(null!=json.get("DATA")){
						list =JSONObject.parseArray(json.get("DATA").toString(),  Map.class);
						for (Map map : list) {
							map.put("ZYCS",map.get("ZYCS").toString());
							map.put("IN_DRG",inDrgName(map.get("IN_DRG").toString()));
						}
					}
				}
				paramMap.put("rowData", list);
			}
			Map<String,Object>  resultM=new HashMap<String, Object>();
			resultM.put("CODE", resultMap.get("CODE"));
			resultM.put("MESSAGE", resultMap.get("MESSAGE"));
			resultMap.clear();
			resultMap=resultM;
		}
		paramMap.put("result", resultMap);
		return paramMap;
	}
	
	/**
	 * 保存分组结果
	 * @param param
	 * @param user
	 * @return
	 */
	public void saveDrgResponseInfo(String param,IUser user){
		if(StringUtils.isEmpty(param)){
			throw new BusException("参数为空，请检查！");
		}
		User u = (User)user;
		List<QcEhpIndrg> qcEhpIndrgList = JsonUtil.readValue(param, new TypeReference<List<QcEhpIndrg>>(){});
		Set<String> yblshs = new HashSet<String>();
		for (QcEhpIndrg qcEhpIndrg : qcEhpIndrgList) {
			yblshs.add(qcEhpIndrg.getYblsh());
			qcEhpIndrg.setInDrg(inDrgCode(qcEhpIndrg.getInDrg()));	
		}
		List<QcEhpIndrg> addEhpIndrgList = new ArrayList<QcEhpIndrg>();
		List<QcEhpIndrg> updateEhpIndrgList = new ArrayList<QcEhpIndrg>();
		//根据医保流水号查询是否存在
		String sql = "select * from qc_ehp_indrg where yblsh in (" + CommonUtils.convertSetToSqlInPart(yblshs, "yblsh") + ")";
		List<QcEhpIndrg> origQcEhpIndrgs = DataBaseHelper.queryForList(sql, QcEhpIndrg.class);
		for (QcEhpIndrg qcEhpIndrg : qcEhpIndrgList) {
			boolean isExist = false;
			if(origQcEhpIndrgs != null && origQcEhpIndrgs.size() > 0) {
				for (QcEhpIndrg origQcDrg : origQcEhpIndrgs) {
					if(qcEhpIndrg.getYblsh().equals(origQcDrg.getYblsh())) {
						qcEhpIndrg.setPkIndrg(origQcDrg.getPkIndrg());
						isExist = true;
						break;
					}
				}
			}
			//存在更新，不存在新增
			if(isExist) {
				ApplicationUtils.setDefaultValue(qcEhpIndrg, false);
				updateEhpIndrgList.add(qcEhpIndrg);
			}else {
				ApplicationUtils.setDefaultValue(qcEhpIndrg, true);
				addEhpIndrgList.add(qcEhpIndrg);
			}
		}
		if(addEhpIndrgList.size()>0){
			DataBaseHelper.batchUpdate(DataBaseHelper.getInsertSql(QcEhpIndrg.class), addEhpIndrgList);
		}
		if(updateEhpIndrgList.size()>0){
			DataBaseHelper.batchUpdate(DataBaseHelper.getUpdateSql(QcEhpIndrg.class), updateEhpIndrgList);
		}
	}
	
	public void updateDrgStatus(String pkpv,String euStatusDrg){
		PvIpVo pvip=new PvIpVo();
		pvip.setEuStatusDrg(euStatusDrg);
		pvip.setDateDrg(new Date());
		int count=DataBaseHelper.updateBeanByWhere(pvip, "PK_PV in ("+pkpv+")",false);
		if(count==0){
			throw new BusException("Drg状态修改失败，请检查！");
		}
	}
	private String inDrgName(String inDrg){
		String name="";
		switch(inDrg){
		    case "1" :
		       name="入组成功，按 DRG 结算";
		       break; 
		    case "2" :
		       name="人工审核无法入组，按项目结算";
		       break; 
		    case "3" :
		       name="地方术语之外病例，按项目结算";
		       break; 
		    case "4" :
		       name="住院天数极端值，按项目结算";
		       break; 
		    case "5" :
		       name="非医嘱离院，按项目结算";
		       break; 
		    case "6" :
		       name="转诊、死亡，按项目结算";
		       break; 
		    case "7" :
		       name="中医中治，按项目结算";
		       break; 
		    case "8" :
		       name="康复治疗，按床日结算";
		       break; 
		    case "9" :
		       name="精神专科，按项目结算";
		       break; 
		    case "10" :
		       name="住院费用极端值，按项目结算";
		       break; 
		    default : 
		    	name="未知";
		}
		return name;
	}

	private String inDrgCode(String inDrg){
		String code="";
		switch(inDrg){
		    case "入组成功，按 DRG 结算" :
		    	code="1";
		    	break; 
		    case "人工审核无法入组，按项目结算" :
		    	code="2";
		    	break; 
		    case "地方术语之外病例，按项目结算" :
		    	code="3";
		    	break; 
		    case "住院天数极端值，按项目结算" :
		    	code="4";
		    	break; 
		    case "非医嘱离院，按项目结算" :
		    	code="5";
		    	break; 
		    case "转诊、死亡，按项目结算" :
		    	code="6";
		    	break; 
		    case "中医中治，按项目结算" :
		    	code="7";
		    	break; 
		    case "康复治疗，按床日结算" :
		    	code="8";
		    	break; 
		    case "精神专科，按项目结算" :
		    	code="9";
		    	break; 
		    case "住院费用极端值，按项目结算" :
		    	code="10";
		    	break; 
		    default : 
		    	code="99";
		}
		return code;
	}
}
