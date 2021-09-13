package com.zebone.nhis.webservice.zhongshan.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.zebone.nhis.common.module.base.ou.BdOuEmployee;
import com.zebone.nhis.webservice.zhongshan.vo.*;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.collections.map.HashedMap;
import org.apache.commons.lang3.StringUtils;
import org.codehaus.jackson.type.TypeReference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.zebone.nhis.common.module.base.ou.BdOuDept;
import com.zebone.nhis.common.module.pv.PvEncounter;
import com.zebone.nhis.common.support.ApplicationUtils;
import com.zebone.nhis.common.support.BeanUtils;
import com.zebone.nhis.common.support.CommonUtils;
import com.zebone.nhis.common.support.DateUtils;
import com.zebone.nhis.webservice.support.Constant;
import com.zebone.nhis.webservice.support.RespJson;
import com.zebone.nhis.webservice.zhongshan.dao.ZsbaOutPatientOrderMapper;
import com.zebone.platform.common.support.NHISUUID;
import com.zebone.platform.common.support.User;
import com.zebone.platform.common.support.UserContext;
import com.zebone.platform.modules.dao.DataBaseHelper;
import com.zebone.platform.modules.exception.BusException;
import com.zebone.platform.modules.utils.JsonUtil;
import com.zebone.platform.web.support.ResponseJson;

/**
 * @Classname ZsbaOutpatientOrderService
 * @Description 医嘱相关接口
 * @Date 2021-06-08 19:09
 * @Created by wuqiang
 */
@Service
public class ZsbaOutpatientOrderService {
	private Logger logger = LoggerFactory.getLogger("nhis.zsbaWebSrv");
	@Autowired
	private ZsbaOutpatientOpChargeService opChService;
	@Autowired
	private ZsbaOutPatientOrderMapper ordMapper;
	
	ApplicationUtils apputil = new ApplicationUtils();

	/**
	 * 2.1.5.获取门诊和住院患者的检验医嘱
	 * @param inf
	 * @return
	 */
	public String getCnLabApply(String param) {
        String ret;
        ThiQueryPiInf thiQueryPiInf = JsonUtil.readValue(opChService.getIniParam(param), ThiQueryPiInf.class);
        //校验参数
        if (thiQueryPiInf == null) {
            ret = "-1|入参为空 " + " ";
            return new RespJson(ret, false).toString();
        }
        try {
            ret = opChService.vilaFiled(thiQueryPiInf);
            if (!StringUtils.isWhitespace(ret)) {
                return new RespJson("-1|" + ret, false).toString();
            }
        } catch (IllegalAccessException e) {
            e.printStackTrace();
            return new RespJson("-1|参数检验错误", false).toString();
        }
        List<Map<String, Object>> RespList = new ArrayList<>();
        Map<String, Object> paraMap = new HashedMap();
        if("2".equals(thiQueryPiInf.getRange())) {//range=2时为门诊否则为1为住院
        	if("1".equals(thiQueryPiInf.getType())) //type为1时是门诊号
        	{
        		paraMap.put("codeOp", thiQueryPiInf.getCode());
        	}
        	else if("3".equals(thiQueryPiInf.getType())){
        		paraMap.put("pkDeptOcc", thiQueryPiInf.getCode());
        	}
        	//查询条件判空
        	if(MapUtils.isNotEmpty(paraMap)) {
        		RespList = ordMapper.queryOpLabOrderApply(paraMap);
        	}
        }else {
        	switch (thiQueryPiInf.getType()){
            case "1":// 住院号
            	paraMap.put("codeIp", thiQueryPiInf.getCode());
            	break;
            case "2"://病区主键
            	paraMap.put("pkDeptNs", thiQueryPiInf.getCode());
                break;
            case "3"://科室主键
            	paraMap.put("pkDept", thiQueryPiInf.getCode());
                break;
            default:
                break;
            }
        	//查询条件判空
            if(MapUtils.isNotEmpty(paraMap)) {
            	RespList = ordMapper.queryIpLabOrderApply(paraMap);
        	}
        }
        return new RespJson("0|成功|" + JsonUtil.writeValueAsString(RespList), true).toString();
	}
	
	/**
	 * 2.1.12.检验申请录入
	 * @param param
	 * @return
	 */
	public String saveLisApplyList(String param) {
		List<ZsbaCnLabApplyVo> saveLisList = JsonUtil.readValue(opChService.getIniParam(param), new TypeReference<List<ZsbaCnLabApplyVo>>(){});
		User user = new User();
		Map<String, Object> ord = new HashMap<>();
		List<String> appluList = new ArrayList<>();
		for (ZsbaCnLabApplyVo lab : saveLisList) {
			if(StringUtils.isEmpty(lab.getPkPv())) {
				return new RespJson("-1|未获取到就诊主键信息", false).toString();
			}else {
				PvEncounter pvEncounter = opChService.getPvconter(null, lab.getPkPv(), "3");
	            if (pvEncounter == null) {
	                return new RespJson("-1|未找到有效患者信息,请检查患者就诊编码", false).toString();
	            }
	            lab.setPkPi(pvEncounter.getPkPi());
			}
			if(StringUtils.isEmpty(lab.getPkOrd())) {
				return new RespJson("-1|未获取到医嘱信息", false).toString();
			}
			Map<String, Object> labOrd = new HashMap<>();
			if(ord.containsKey(lab.getPkOrd()) && null != ord.get(lab.getPkOrd())) {
				labOrd = (Map<String, Object>)ord.get(lab.getPkOrd());
			}else {
				labOrd.put("pkOrd", lab.getPkOrd());
				labOrd = ordMapper.queryBdOrdLabInfo(labOrd);
				ord.put(lab.getPkOrd(), labOrd);
			}
			lab.setNameOrd(MapUtils.getString(labOrd, "name"));
			lab.setCodeOrd(MapUtils.getString(labOrd, "code"));
			lab.setCodeOrdType(MapUtils.getString(labOrd, "codeOrdtype"));
			if(StringUtils.isEmpty(lab.getDtSamptype())) {//标本类型
				if(StringUtils.isEmpty(MapUtils.getString(labOrd, "dtSamptype"))) {
					return new RespJson("-1|标本类型不能为空", false).toString();
				}else {
					lab.setDtSamptype(MapUtils.getString(labOrd, "dtSamptype"));
				}
			}
			if(StringUtils.isEmpty(lab.getDtTubetype())) {//试管类型
				lab.setDtSamptype(MapUtils.getString(labOrd, "dtContype"));
			}
			if(StringUtils.isEmpty(lab.getDtColtype())) {//采集方法
				lab.setDtSamptype(MapUtils.getString(labOrd, "dtColltype"));
			}
			if(StringUtils.isEmpty(lab.getPkDeptExec())) {//执行科室主键
				return new RespJson("-1|未获取到执行科室主键信息", false).toString();
			}else {
				String sql = "select PK_DEPT,CODE_DEPT ,NAME_DEPT from BD_OU_DEPT BOD where  pk_dept=?";
		        BdOuDept bdOuDept = DataBaseHelper.queryForBean(sql, BdOuDept.class, new Object[]{lab.getPkDeptExec()});
		        if (bdOuDept == null) {
		            return new RespJson("-1|无效的开立科室", false).toString();
		        }
			}
			
			if(StringUtils.isEmpty(lab.getPkEmpOrd())) {//开立医生主键
				return new RespJson("-1|未获取到开立医生主键信息", false).toString();
			}else {
				user = getDefaultUser(null,lab.getPkEmpOrd());
		        if(!BeanUtils.isNotNull(user)){
		            return new RespJson("-1|未查询到操作人信息", false).toString();
				}
		        lab.setPkEmpOrd(lab.getPkEmpOrd());
			}
			
			if(StringUtils.isEmpty(lab.getPkDept())) {//开立科室主键
				return new RespJson("-1|未获取到开立科室主键信息", false).toString();
			}else {
				String sql = "select PK_DEPT,CODE_DEPT ,NAME_DEPT from BD_OU_DEPT BOD where  pk_dept=?";
		        BdOuDept bdOuDept = DataBaseHelper.queryForBean(sql, BdOuDept.class, new Object[]{lab.getPkDept()});
		        if (bdOuDept == null) {
		            return new RespJson("-1|无效的开立科室", false).toString();
		        }
			}
			if(StringUtils.isEmpty(lab.getPkDeptNs())) {//开立病区主键
				lab.setPkDept(opChService.getOpPkDeptNs(lab.getPkDept()));
			}
			String codeApply = ApplicationUtils.getCode("0401");//申请单号
			appluList.add(codeApply);
			lab.setCodeApply(codeApply);
			lab.setQuan(1.0D);
			lab.setRowStatus("N");
		}
		UserContext.setUser(user);
		ResponseJson  result =  apputil.execService("CN", "CnReqService", "saveLisApplyList", saveLisList,user);
		if(result.getStatus()== Constant.SUC){
			List<Map<String, Object>> datalist = ordMapper.queryPkCnordCodeInfo(appluList);
			return new RespJson("0|成功|" + JsonUtil.writeValueAsString(datalist), true).toString();
		}else {
			return new RespJson("-1|"+result.getDesc(), false).toString();
		}
	}
	
	/**
	 * 2.4.1.条码打印/取消条码打印接口
	 * @param param
	 * @return
	 */
	public String updaCnLabApplySampNo(String param){
		ZsbaOutLabVo LabVo= JsonUtil.readValue(opChService.getIniParam(param), ZsbaOutLabVo.class);
		//校验参数
		String ret;
        if (LabVo == null) {
            ret = "-1|入参为空 " + " ";
            return new RespJson(ret, false).toString();
        }
        try {
            ret = opChService.vilaFiled(LabVo);
            if (!StringUtils.isWhitespace(ret)) {
                return new RespJson("-1|入参为空" + ret, false).toString();
            }
        } catch (IllegalAccessException e) {
            e.printStackTrace();
            return new RespJson("-1|参数检验错误", false).toString();
        }
        
        if("0".equals(LabVo.getOpeType()) && CommonUtils.isEmptyString(LabVo.getCodeBar())) {
            return new RespJson("-1|操作类型为【"+LabVo.getOpeType()+"】时条码编号不能未空", false).toString();
        }
        if("8".equals(LabVo.getOpeType())){
        	LabVo.setCodeBar("");
        }
        if(null == LabVo.getOrdlist() || LabVo.getOrdlist().isEmpty()) {
            return new RespJson("-1|未获取到医嘱序号", false).toString();
        }
        User user = getDefaultUser(LabVo.getCodeEmp(),null);
        if(!BeanUtils.isNotNull(user)){
            return new RespJson("-1|未查询到操作人信息", false).toString();
		}
        Map<String, Object> pranaMap = new HashedMap();
        pranaMap.put("ordsns",LabVo.getOrdlist());
        List<Map<String, Object>> ordsList = ordMapper.queryOrdsnOrderInfo(pranaMap);
        if(null == ordsList || ordsList.isEmpty()) {
            return new RespJson("-1|未查询到有效执行单信息", false).toString();
        }
        try {
        	//更新cnLabApply条码
        	upCnLabApplySampNo(ordsList,LabVo.getCodeBar());

            pranaMap = ordsList.get(0);
            pranaMap.put("thiropetype", LabVo.getOpeType());//操作类型
            pranaMap.put("thircodebar", LabVo.getCodeBar());//条码号；
            pranaMap.put("thirtimeope", LabVo.getTimeOpe());//操作时间，yyyy-mm-dd hh24:mi:ss；
            //记录相关操作日志
            inserMtsOperRec(pranaMap,user);
            
		} catch (Exception e) {
			// TODO: handle exception
			return new RespJson("-1|" + e.getMessage(), false).toString();
		}
        return new RespJson("0|成功", false).toString();
	}

	/**
	 * 2.4.2.LIS状态回写接口
	 * @param param
	 * @return
	 */
	public String updateListEuStatus(String param) {
		ZsbaOutLabVo LabVo= JsonUtil.readValue(opChService.getIniParam(param), ZsbaOutLabVo.class);
		//校验参数
		String ret;
        if (LabVo == null) {
            ret = "-1|入参为空 " + " ";
            return new RespJson(ret, false).toString();
        }
        try {
            ret = opChService.vilaFiled(LabVo);
            if (!StringUtils.isWhitespace(ret)) {
                return new RespJson("-1|" + ret, false).toString();
            }
        } catch (IllegalAccessException e) {
            e.printStackTrace();
            return new RespJson("-1|参数检验错误", false).toString();
        }
        User user = getDefaultUser(LabVo.getCodeEmp(),null);
        if(!BeanUtils.isNotNull(user)){
            return new RespJson("-1|未查询到操作人信息", false).toString();
		}
        UserContext.setUser(user);
        if(CommonUtils.isEmptyString(LabVo.getCodeBar())) {
            return new RespJson("-1|条码编号不能未空", false).toString();
        }
        
        Map<String, Object> pranaMap = new HashedMap();
        pranaMap.put("codeBar",LabVo.getCodeBar());
        List<Map<String, Object>> ordsList = ordMapper.queryOrdsnOrderInfo(pranaMap);
        if(null == ordsList || ordsList.isEmpty()) {
            return new RespJson("-1|未查询到有效执行单信息", false).toString();
        }
        try {
        	//根据操作类型获取对应更改状态
            String euStatus = getEuStatus(LabVo.getOpeType());
            if(StringUtils.isNotEmpty(euStatus)) {
            	//批量更新cn_lab_apply表eu_status信息
            	upCnLabApplyEuStatus(ordsList,euStatus);
            }
            
            //根据执行类型进行操作
            if(StringUtils.isNotEmpty(LabVo.getExecType())) {
            	if("1".equals(LabVo.getExecType())) {//1，医技执行
            		excList(ordsList);
            		//判断住院就诊类型
            		if("3".equals(MapUtils.getString(ordsList.get(0), "euPvtype"))) {
            			//添加相关费用
            			zsBaThresIpCgPubService(LabVo,ordsList,user);
            		}
                }
            	else if("9".equals(LabVo.getExecType())) {//9，取消医技执行
            		cancleExocc(ordsList,"9");
                }else if("6".equals(LabVo.getExecType())) {//6，撤销执行
                	cancleExocc(ordsList,"0");
                }
            }

            pranaMap = ordsList.get(0);
            pranaMap.put("thiropetype", LabVo.getOpeType());//操作类型
            pranaMap.put("thircodebar", LabVo.getCodeBar());//条码号；
            pranaMap.put("thirtimeope", LabVo.getTimeOpe());//操作时间，yyyy-mm-dd hh24:mi:ss；
            //记录相关操作日志
            inserMtsOperRec(pranaMap,user);
        } catch (Exception e) {
			// TODO: handle exception
			return new RespJson("-1|" + e.getMessage(), false).toString();
		}
        return new RespJson("0|成功", false).toString();
	}
	
	//医技执行
	private void excList(List<Map<String, Object>> ordsList) {
		List<String> pkCnords = new ArrayList();
		ordsList.forEach(map ->{
			pkCnords.add(MapUtils.getString(map, "pkCnord"));
		});
		Map<String, Object> paramMap = new HashedMap();
		paramMap.put("pkCnords", pkCnords);
		paramMap.put("dateOcc", DateUtils.getDateTime());
		paramMap.put("pkEmpOcc", UserContext.getUser().getPkEmp());
		paramMap.put("nameEmpOcc", UserContext.getUser().getNameEmp());
		paramMap.put("pkOrgOcc",  UserContext.getUser().getPkOrg());
		paramMap.put("pkDeptOcc", UserContext.getUser().getPkDept());
		
		// 数据校验
        if ("3".equals(MapUtils.getString(ordsList.get(0), "euPvtype"))) {
        	int count = ordMapper.ipDataCheck(paramMap);
        	if (count > 0) {
                throw new BusException("有结算费用，不能执行!");
            }
        }else {
        	int count = ordMapper.opDataCheck(paramMap);
        	if (count > 0) {
                throw new BusException("有结算费用，不能执行!");
            }
        }
		// 医技执行
        try {
        	ordMapper.medExeOcc(paramMap);
        } catch (Exception e) {
            throw new BusException("执行失败！");
        }
	}	
	//撤销、取消医技执行
	private void cancleExocc(List<Map<String, Object>> ordsList,String euStatus) {
		Set<String> pkCnords = new HashSet<String>();
		ordsList.forEach(map ->{
			pkCnords.add(MapUtils.getString(map, "pkCnord"));
		});
		Map<String, Object> paramMap = new HashedMap();
		paramMap.put("euStatus", euStatus);
		paramMap.put("pkCnords", (List<String>)pkCnords);
		paramMap.put("pkEmpCanc", UserContext.getUser().getPkEmp());
		paramMap.put("nameEmpCanc", UserContext.getUser().getNameEmp());
		paramMap.put("dateCanc", DateUtils.dateToStr("yyyyMMddHHssmm", new Date()));
		ordMapper.medExeOcc(paramMap);
	}
	
	/**
	 * 住院其他记账
	 * @param LabVo
	 * @param ordsList
	 * @param user
	 */
	private void zsBaThresIpCgPubService(ZsbaOutLabVo LabVo,List<Map<String, Object>> ordsList,User user) {
		if(StringUtils.isNotBlank(LabVo.getCodeItem()) && StringUtils.isNotBlank(LabVo.getQuan())) {
			List<ThiZsbaBlPubParamVo> blParaList = new ArrayList<>();
			Map<String, Object> bdItemMap = DataBaseHelper.queryForMap("select pk_item from bd_item where del_flag='0' and code=?",LabVo.getCodeItem());
		   	 if(MapUtils.isEmpty(bdItemMap)){
		   		 String sql = "select item.pk_item from bd_item item left join BD_ORD_ITEM orite on orite.pk_item=item.pk_item left join bd_ord ord on ord.pk_ord=orite.pk_ord  where ord.code=?";
		   		 List<Map<String, Object>> bdItemList = DataBaseHelper.queryForList(sql,LabVo.getCodeItem());
		       	 if(bdItemList.size()>0){
		       		 for(Map<String, Object> map:bdItemList){
		       			 map.put("pkOrg", UserContext.getUser().getPkOrg());//机构编码
		       			 map.put("pkPi", MapUtils.getString(ordsList.get(0),"pkPi"));//患者主键
		       			 map.put("pkPv", MapUtils.getString(ordsList.get(0),"pkPv"));//患者就诊主键
		       			 map.put("euPvtype", MapUtils.getString(ordsList.get(0),"euPvtype"));//就诊类型
		       			 map.put("pkDept", user.getPkDept());//开立科室
		       			 map.put("pkEmpInput", user.getPkEmp());//开立医生
		       			 map.put("nameEmpInput", user.getNameEmp());//医生姓名
		       			 map.put("pkEmpCg", user.getPkEmp());
		       			 map.put("nameEmpCg", user.getNameEmp());
		       			 map.put("pkOrgEcex", user.getPkOrg());//执行机构
		       			 map.put("pkDeptExec", user.getPkDept());//执行科室
		       			 map.put("quan", LabVo.getQuan());//数量
		       			 //map.put("nameItemset",ControlID);//获取消息id进行添加唯一标识判断查询使用
				         blParaList.add(getCgVo(map));  
		       		 }
		       	 }else{
		       		throw new BusException("未查询到相关收费项目");
		       	 }
		   	 }else{
		   		 bdItemMap.put("pkOrg", user.getPkOrg());//机构编码
		       	 bdItemMap.put("pkPi", MapUtils.getString(ordsList.get(0),"pkPi"));//患者主键
		       	 bdItemMap.put("pkPv", MapUtils.getString(ordsList.get(0),"pkPv"));//患者就诊主键
		       	 bdItemMap.put("euPvtype", MapUtils.getString(ordsList.get(0),"euPvtype"));//就诊类型
		       	 bdItemMap.put("pkDept", user.getPkDept());//开立科室
		       	 bdItemMap.put("pkEmpInput", user.getPkEmp());//开立医生
		       	 bdItemMap.put("nameEmpInput", UserContext.getUser().getNameEmp());//医生姓名
		       	 bdItemMap.put("pkEmpCg", user.getPkEmp());
		       	 bdItemMap.put("nameEmpCg", user.getNameEmp());
		       	 bdItemMap.put("pkOrgEcex", user.getPkOrg());//执行机构
		       	 bdItemMap.put("pkDeptExec", user.getPkDept());//执行科室
		       	 bdItemMap.put("quan", LabVo.getQuan());//数量
		       	 //bdItemMap.put("nameItemset",ControlID);//获取消息id进行添加唯一标识判断查询使用
		       	 blParaList.add(getCgVo(bdItemMap)); 
		   	 }
		   	 
		   //住院记费服务
		 	ResponseJson  NowSjh =  ApplicationUtils.execService("BL", "IpCgPubService", "savePatiCgInfo",blParaList,UserContext.getUser());
		 	if(NowSjh.getStatus()!=0){
		 		throw new BusException(NowSjh.getDesc());
		 	}
		}
		
	}
	
	/**
	 * ba-检验专用
	 * 更新cn_lab_apply标本编号
	 * @param ordsList 需要修改的cn_Lab_apply数据集
	 * @param codeBar 检验条码可以为空
	 * 
	 */
	private void upCnLabApplySampNo(List<Map<String, Object>> ordsList,String codeBar) {
		Map<String, Object> paramMap = new HashedMap();
		paramMap.put("sampNo", codeBar);
		List<String> pkCnords = new ArrayList<>();
		ordsList.forEach(map-> {
			if(StringUtils.isNotEmpty(codeBar)) {
	        	pkCnords.add(MapUtils.getString(map, "pkCnord"));
			}else if("1".equals(MapUtils.getString(map, "euStatus"))) {
	        	pkCnords.add(MapUtils.getString(map, "pkCnord"));
			}
        });
		
		if(!pkCnords.isEmpty()) {
			paramMap.put("pkCnords", pkCnords);
			ordMapper.updateCnLabApply(paramMap);
		}
	}
	

	/**
	 * ba-检验专用
	 * 更新cn_lab_apply状态
	 * @param ordsList 需要修改的cn_Lab_apply数据集
	 * @param euStatus 状态
	 * 
	 */
	private void upCnLabApplyEuStatus(List<Map<String, Object>> ordsList,String euStatus) {
		Map<String, Object> paramMap = new HashedMap();
		paramMap.put("euStatus", euStatus);
		List<String> pkCnords = new ArrayList<>();
		ordsList.forEach(map->{
			pkCnords.add(MapUtils.getString(map, "pkCnord"));
        });
		if(!pkCnords.isEmpty()) {
			paramMap.put("pkCnords", pkCnords);
			ordMapper.updateCnLabApply(paramMap);
		}
	}
	
	
	/**
	 * ba检验接口专用
	 * 插入MtsOperRec操作记录
	 * @param paraMap
	 */
	private void inserMtsOperRec(Map<String, Object> paraMap,User user) {
		MtsOperRec operRec = new MtsOperRec();
        operRec.setPkRec(NHISUUID.getKeyId());//主键；
        operRec.setPkOrg(MapUtils.getString(paraMap, "pkOrg"));//机构主键；
        operRec.setPkPv(MapUtils.getString(paraMap, "pkPv"));//就诊主键；
        operRec.setCodePi(MapUtils.getString(paraMap, "codePi"));//患者编码；
        operRec.setCode("3".equals(MapUtils.getString(paraMap, "euPvtype")) ? MapUtils.getString(paraMap, "codeIp") : MapUtils.getString(paraMap, "codeOp"));//门诊，存门诊号code_op；住院，存住院号code_ip。
        Integer times = Integer.valueOf("3".equals(MapUtils.getString(paraMap, "euPvtype")) ? MapUtils.getString(paraMap, "ipTimes","") : MapUtils.getString(paraMap, "opTimes",""));
        operRec.setTimes(times);//就诊次数；
        operRec.setPvType(MapUtils.getString(paraMap, "euPvtype"));//就诊类型，1门诊、3住院；
        operRec.setPvName("3".equals(MapUtils.getString(paraMap, "euPvtype"))?"住院":"门诊");//就诊类型名称；
        operRec.setMtsType("LIS");//业务类型，。
        operRec.setMtsName(MapUtils.getString(paraMap, "nameOrd"));//医嘱名称;
        operRec.setOperType(String.format("L%s",MapUtils.getString(paraMap, "thiropetype")));
        operRec.setOperName(getOpeType(MapUtils.getString(paraMap, "thiropetype")));//操作类型名称；
        operRec.setReqNo(MapUtils.getString(paraMap, "codeApply"));//申请单号；
        operRec.setRecNo(MapUtils.getString(paraMap, "thircodebar",""));//条码号；
        operRec.setOperTime(DateUtils.strToDate(MapUtils.getString(paraMap, "thirtimeope"),"yyyy-MM-dd HH:mm:ss"));//操作时间，yyyy-mm-dd hh24:mi:ss；
        operRec.setEmpCode(user.getCodeEmp());//操作员编码；
        operRec.setEmpName(user.getNameEmp());//操作员姓名；
        //operRec.setRemark(null);//备注；
        operRec.setDelFlag("0");//删除标记；
        DataBaseHelper.insertBean(operRec);
	}
	
	
	/**
     * 转换操作类型
     * @param opeType
     * @return
     */
	private static String getOpeType(String opeType){
		//操作类型，枚举：L0-条码打印、L8-取消条码打印、L1-标本采集；L6-取消采集；
        //L2-标本送出；L5-取消标本送出；L3-标本接收；L7-标本退回；L4-报告；L10-报告召回、L9上机。
		String typeName;
    	switch (opeType) {
		case "0":
			typeName="条码打印";
			break;
		case "1":
			typeName="标本采集";
			break;
		case "2":
			typeName="标本送出";
			break;
		case "3":
			typeName="标本接收";
			break;
		case "4":
			typeName="报告";
			break;
		case "5":
			typeName="取消标本送出";
			break;
		case "6":
			typeName="取消采集";
			break;
		case "7":
			typeName="标本退回";
			break;
		case "8":
			typeName="取消条码打印";
			break;
		case "9":
			typeName="上机";
			break;
		case "10":
			typeName="报告召回";
			break;
		default:
			typeName="";
			break;
		}
    	return typeName;
	}
	
	/**
     * 转换操作类型
     * @param opeType
     * @return euStatus
     */
	private static String getEuStatus(String opeType){
		//操作类型，枚举：L0-条码打印、L8-取消条码打印、L1-标本采集；L6-取消采集；
        //L2-标本送出；L5-取消标本送出；L3-标本接收；L7-标本退回；L4-报告；L10-报告召回、L9上机。
		String euStatus;
    	switch (opeType) {
		case "1":
			euStatus="2";
			break;
		case "3":
			euStatus="3";
			break;
		case "4":
			euStatus="4";
			break;
		case "6":
			euStatus="1";
			break;
		case "7":
			euStatus="2";
			break;
		case "9":
			euStatus="3";
			break;
		case "10":
			euStatus="3";
			break;
		default:
			euStatus="";
			break;
		}
    	return euStatus;
	}
	
	/**
	 * 获取人员信息以及所属科室
	 * @param codeEmp
	 * @return
	 */
	private static User getDefaultUser(String codeEmp,String pkEmp) {
		User user=null;
		String sql = "";
		if(StringUtils.isNotEmpty(codeEmp)) {
			sql = "SELECT * FROM bd_ou_employee emp JOIN bd_ou_empjob empjob  ON empjob.pk_emp = emp.pk_emp WHERE empjob.del_flag = '0' and emp.del_flag = '0' and emp.CODE_EMP = ?";
		}
        if(StringUtils.isNotEmpty(pkEmp)) {
			sql = "SELECT * FROM bd_ou_employee emp JOIN bd_ou_empjob empjob  ON empjob.pk_emp = emp.pk_emp WHERE empjob.del_flag = '0' and emp.del_flag = '0' and emp.pk_emp = ?";
			codeEmp = pkEmp;
		}
	    Map<String, Object> bdOuMap = DataBaseHelper.queryForMap(sql ,codeEmp);
		if (bdOuMap != null) {
			user = new User();
			user.setPkOrg(MapUtils.getString(bdOuMap, "pkOrg"));
			user.setNameEmp(MapUtils.getString(bdOuMap, "nameEmp"));
			user.setPkOrg(MapUtils.getString(bdOuMap, "pkOrg"));
			user.setNameEmp(MapUtils.getString(bdOuMap, "nameEmp"));
			user.setPkEmp(MapUtils.getString(bdOuMap, "pkEmp"));
			user.setPkDept(MapUtils.getString(bdOuMap, "pkDept"));
			user.setCodeEmp(MapUtils.getString(bdOuMap, "codeEmp"));
		}
		return user;
	}
	
    private ThiZsbaBlPubParamVo getCgVo(Map<String, Object> orderMap){
	    ThiZsbaBlPubParamVo cgParam = new ThiZsbaBlPubParamVo();
		cgParam.setPkOrdexdt(MapUtils.getString(orderMap,"nameItemset"));//获取消息id进行添加唯一标识判断查询使用
		cgParam.setPkOrg(MapUtils.getString(orderMap,"pkOrg"));
		cgParam.setPkItem(MapUtils.getString(orderMap,"pkItem"));//记费项目
		cgParam.setPkCnord(MapUtils.getString(orderMap,"pkCnord"));//医嘱主键
		cgParam.setPkPi(MapUtils.getString(orderMap,"pkPi"));//患者主键
		cgParam.setPkPv(MapUtils.getString(orderMap,"pkPv"));//患者就诊主键
		cgParam.setEuPvType(MapUtils.getString(orderMap,"euPvtype"));//就诊类型
		cgParam.setFlagPd("0");
		cgParam.setFlagPv("0");
		cgParam.setCodeOrdtype(MapUtils.getString(orderMap,"codeOrdtype"));
		cgParam.setDateStart((Date)orderMap.get("dateStart"));
		cgParam.setInfantNo(MapUtils.getString(orderMap,"infantNo"));//婴儿标志
		cgParam.setDateHap(new Date());
		cgParam.setPkOrgApp(cgParam.getPkOrg());
		cgParam.setPkDeptApp(MapUtils.getString(orderMap,"pkDept"));//开立科室
		cgParam.setPkDeptNsApp(MapUtils.getString(orderMap,"pkDeptNs"));
		cgParam.setPkEmpApp(MapUtils.getString(orderMap,"pkEmpInput"));
		cgParam.setNameEmpApp(MapUtils.getString(orderMap,"nameEmpInput"));
		cgParam.setPkOrgEx(MapUtils.getString(orderMap,"pkOrgEcex"));
		cgParam.setPkDeptEx(MapUtils.getString(orderMap,"pkDeptExec"));
		cgParam.setFlagFit(MapUtils.getString(orderMap,"flagFit"));
	    cgParam.setPkEmpCg(MapUtils.getString(orderMap,"pkEmpCg")); 
		cgParam.setNameEmpCg(MapUtils.getString(orderMap,"nameEmpCg"));
		cgParam.setPkDeptCg(cgParam.getPkDeptEx());
		String price = "0.00" ;  //药品才传
		cgParam.setPrice(Double.parseDouble(price));
		Double quan = 1.00;
        Double quanCg = 1.00; 
		String quanStr  = MapUtils.getString(orderMap,"quan");
		if(StringUtils.isNotBlank(quanStr)) quan = Double.valueOf(quanStr);
		String quanCgStr  = MapUtils.getString(orderMap,"quanCg");
		if(StringUtils.isNotBlank(quanCgStr)) quanCg = Double.valueOf(quanCgStr);
		cgParam.setQuanCg("3".equals(cgParam.getEuPvType())?quan:quanCg); 
		return cgParam;
	}
}
