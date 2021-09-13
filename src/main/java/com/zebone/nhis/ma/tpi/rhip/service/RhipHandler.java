package com.zebone.nhis.ma.tpi.rhip.service;


import com.zebone.nhis.common.module.emr.mgr.EmrOperateLogs;
import com.zebone.nhis.common.support.CommonUtils;
import com.zebone.nhis.ma.tpi.rhip.dao.RhipMapper;
import com.zebone.nhis.ma.tpi.rhip.vo.OutPvPatiVo;
import com.zebone.nhis.ma.tpi.rhip.vo.PatListVo;
import com.zebone.platform.common.support.User;
import com.zebone.platform.framework.support.IUser;
import com.zebone.platform.modules.dao.DataBaseHelper;
import com.zebone.platform.modules.dao.jdbc.DataSourceRoute;
import com.zebone.platform.modules.exception.BusException;
import com.zebone.platform.modules.utils.JsonUtil;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;


/**
 * 第三方接口-区域卫生平台接口服务
 * @author chengjia
 *
 */
@Service
public class RhipHandler {
	
	@Resource
	private	RhipService rhipService;

	@Resource
	private	RhipPacsService pacsService;
	
	@Resource
	private	RhipLisService lisService;

	@Resource
	private RhipMapper rhipMapper;

	@Resource
	private RhipOperationService operationService;
    /**
     * 区域平台数据上传
     * @param param
     * @param user
     * @return
     * @throws Exception 
     */
    @SuppressWarnings({ "rawtypes", "unchecked", "unused" })
	public void rpDataTrans(String param , IUser user) throws Exception{
    	Map map = JsonUtil.readValue(param,Map.class);
		if( map.get("pkPv") == null || CommonUtils.isEmptyString(map.get("pkPv").toString()))
			throw new BusException("未获取到需要上传的患者就诊主键！");
    	String pkPv = (String) map.get("pkPv");
		String[] pkPvs = pkPv.split(",");
		if(pkPvs.length < 1) return;
		for (String pkpv : pkPvs) {
			try {
				pkPv = pkpv;
				pkpv = "{\"pkPv\":\""+pkpv+"\"}";
				PatListVo pat = null;
				//pat = getPatListVo(pat,map,pkPv);
				//NHIS相关
				List<Object> rtnList =rhipService.rpDataTrans(pkpv, user,pat);
				if(rtnList==null||rtnList.size()<2){
					throw new BusException("上传失败!");
				}
				String rtn = rtnList.get(0).toString();
				pat=(PatListVo)rtnList.get(1);
				if(!CommonUtils.isEmptyString(rtn)){
					throw new BusException(rtn);
				}
				if(pat==null){
					throw new BusException("获取病人就诊信息失败!");
				}
				
				//3其他数据集
				/*//1/检查报告(Pt_ExamReport)@todo
				DataSourceRoute.putAppId("bbaPacsaPacs");//切换数据库连接资源
				pacsService.rpDataTrans(pkpv, user, pat);*/

				//心电
				DataSourceRoute.putAppId("banl");//
				pacsService.rpDataTrans(pkpv, user, pat);
				//2/检验报告(Pt_LabReport)
				DataSourceRoute.putAppId("LIS_bayy");
				lisService.rpDataTrans(pkpv, user, pat);
				//
				DataSourceRoute.putAppId("pt_op");
				operationService.rpDataTrans(pkpv, user, pat);
			}finally{
				DataSourceRoute.putAppId("default");
			}
			User u = (User) user;
			rhipService.updateRhipStatusFromEmr(pkPv,u.getPkEmp());
		}
    }
    
    /**
      * 区域平台数据上传--门诊
     * @param param
     * @param user
     * @return
     * @throws Exception 
     */
    @SuppressWarnings({ "rawtypes", "unchecked", "unused" })
	public void rpDataTransOp(String param , IUser user) throws Exception{
    	//查询所有未上传的患者，过滤已上传，过滤上传失败
    	String sql="select top 3 pk_pv from view_pat_list_op_tpi where eu_status_pv!='9' and name not like '%测试%' "+
    	"and not exists(select 1 from rp_data_upload u where u.pk_pv= view_pat_list_op_tpi.pk_pv and u.del_flag='0') order by DATE_CLINIC";
    	String pkPv = "";
    	List<String> pkPvs=DataBaseHelper.queryForList(sql, String.class);//rhipService.queryPvList();
		for (String pkpv : pkPvs) {
			try {
				pkPv = pkpv;
				PatListVo pat = null;
				//NHIS相关
				List<Object> rtnList =rhipService.rpDataTransOp(pkpv, user,pat);
				if(rtnList==null||rtnList.size()<2){
					insertErrLogsOp(pkPv, "上传失败!");
				}
				String rtn = rtnList.get(0).toString();
				pat=(PatListVo)rtnList.get(1);
				if(!CommonUtils.isEmptyString(rtn)){
					insertErrLogsOp(pkPv, rtn);
				}
			}catch (Exception e) {
				insertErrLogsOp(pkPv, e.getMessage());
			}finally{
				//DataSourceRoute.putAppId("default");
			}
			User u = (User) user;
			rhipService.insertRhipStatusFromEmr(pkPv,u.getPkEmp(),null);
		}
		//药房，药库
		/*try {
			List<Object> rtnList =rhipService.rpDataTransOpPMC(param, user);
			if(rtnList==null||rtnList.size()<1){
				throw new BusException("上传失败!");
			}
			String rtn = rtnList.get(0).toString();
			if(!CommonUtils.isEmptyString(rtn)){
				throw new BusException(rtn);
			}
		}finally{
			//DataSourceRoute.putAppId("default");
		}*/
		/*User u = (User) user;
		rhipService.insertRhipStatusFromEmr(pkPv,u.getPkEmp(),null);*/
    }
    
    /**
     * 自动病历上传，报错则插入一条错误日志
     * @param param
     * @param user
     */
    public void rpDataTransAuto(String param , IUser user) {
    	Map map = JsonUtil.readValue(param,Map.class);
    	if( map.get("pkPv") == null || CommonUtils.isEmptyString(map.get("pkPv").toString()))
			throw new BusException("未获取到需要上传的患者就诊主键！");
    	String pkPv = (String) map.get("pkPv");
		String[] pkPvs = pkPv.split(",");
		if(pkPvs.length < 1) return;
		for (String pkpv : pkPvs) {
			try {
				pkPv = pkpv;
				pkpv = "{\"pkPv\":\""+pkpv+"\"}";
				//NHIS相关
				PatListVo pat=null;
				List<Object> rtnList =rhipService.rpDataTrans(pkpv, user,pat);
				if(rtnList==null||rtnList.size()<2){
					DataSourceRoute.putAppId("default");
					insertErrLogs(pkPv, "上传失败!");
					return;
				}
				String rtn = rtnList.get(0).toString();
				
				if(!CommonUtils.isEmptyString(rtn)){
					DataSourceRoute.putAppId("default");
					insertErrLogs(pkPv, rtn);
					return;
				}
				pat=(PatListVo)rtnList.get(1);
				//3其他数据集
				
				//1/检查报告(Pt_ExamReport)@todo
				DataSourceRoute.putAppId("baPacs");
				rtn = pacsService.rpDataTrans(pkpv, user, pat);
				if(!CommonUtils.isEmptyString(rtn)){
					DataSourceRoute.putAppId("default");
					insertErrLogs(pkPv, rtn);
					return;
				}
				

				DataSourceRoute.putAppId("LIS_bayy");
				//2/检验报告(Pt_LabReport)
				rtn = lisService.rpDataTrans(pkpv, user, pat);
				if(!CommonUtils.isEmptyString(rtn)){
					DataSourceRoute.putAppId("default");
					insertErrLogs(pkPv, rtn);
					return;
				}
				
			}
			catch (Exception e) {
				insertErrLogs(pkPv, e.getMessage());
			}finally{
				DataSourceRoute.putAppId("default");
			}
			User u = (User) user;
			rhipService.updateRhipStatusFromEmr(pkPv,u.getPkEmp());
		} 
    }

	private void insertErrLogs(String pkPv, String errTxt) {
//		EmrOperateLogs emrOpeLogs = new EmrOperateLogs();
//		emrOpeLogs.setCode("rhip_upload");
//		emrOpeLogs.setDelFlag("0");
//		emrOpeLogs.setEuStatus("0");
//		emrOpeLogs.setPkOrg(UserContext.getUser().getPkOrg());
//		emrOpeLogs.setPkPv(pkPv);
//		emrOpeLogs.setOperateTxt(errTxt);
//		emrOpeLogs.setPkLog(UUID.randomUUID().toString().replace("-",""));
//		DataBaseHelper.insertBean(emrOpeLogs);
		rhipService.insertErrLogs(pkPv, errTxt);
	}
	private void insertErrLogsOp(String pkPv, String errTxt) {
		EmrOperateLogs emrOpeLogs = new EmrOperateLogs();
		emrOpeLogs.setCode("rhip_upload_op");
		emrOpeLogs.setDelFlag("0");
		emrOpeLogs.setEuStatus("0");
		emrOpeLogs.setPkOrg("~");
		emrOpeLogs.setPkPv(pkPv);
		emrOpeLogs.setOperateTxt(errTxt);
		emrOpeLogs.setPkLog(UUID.randomUUID().toString().replace("-",""));
		DataBaseHelper.insertBean(emrOpeLogs);
	}
    /**
	 * 查询出院病人列表
	 * @param param{codeIp,namePi,dateInBegin,dateInEnd,dateOutBegin,dateOutEnd,pkDept}
	 * @param user
	 * @return
     * @throws Exception 
	 */
	public List<OutPvPatiVo> qryOutPatiList(String param,IUser user) throws Exception{
		Map<String,Object> map = JsonUtil.readValue(param, Map.class);
		return rhipService.getOutPatiList(map);
	}

	/**
	 * 获取患者信息
	 * @param pat
	 * @param map
	 * @param pkPv
	 * @return
	 */
	public PatListVo getPatListVo(PatListVo pat ,Map map , String pkPv){
		List<Object> rtnList=new ArrayList<>();
		rtnList.add("");
		rtnList.add(pat);

		List<PatListVo> list=rhipMapper.queryPatList(map);
		//根据pkpv查询诊断编码和诊断名称
		List<Map<String,String>> diagInfo=rhipMapper.queryDiagInfo(pkPv);
		String diagCode = "";
		String diagName = "";
		String adCode = "";
		String adName = "";
		for (Map<String,String> diagInfoMap: diagInfo) {
			if ("1".equals(diagInfoMap.get("flag")) ) {// 获取主要诊断
				diagCode = diagInfoMap.get("diagcode");
				diagName = diagInfoMap.get("diagname");

			}
			if ("0100".equals(diagInfoMap.get("diagtype")) && "".equals(adCode)) {//获取入院诊断
				adCode = diagInfoMap.get("diagcode");
				adName = diagInfoMap.get("diagname");
			}
			if (StringUtils.isNotBlank(diagCode)&& StringUtils.isNotBlank(adCode)) {
				break;
			}
		}
		if(list==null||list.size()==0){
			rtnList.set(0, "未能找到患者信息");

		}
		String xml="";
		String rtnStr="";
		pat=list.get(0);
		pat.setDiagCode(diagCode);
		pat.setDiagName(diagName);
		pat.setAdmissionDiagnosisCode(adCode);
		pat.setAdmissionDiagnosisName(adName);
		return pat;
	}
}
