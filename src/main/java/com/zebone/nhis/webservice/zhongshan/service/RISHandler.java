package com.zebone.nhis.webservice.zhongshan.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.zebone.nhis.common.support.ApplicationUtils;
import com.zebone.nhis.common.support.CommonUtils;
import com.zebone.nhis.webservice.support.RespJson;
import com.zebone.nhis.webservice.zhongshan.support.ErrorMsg;
import com.zebone.nhis.webservice.zhongshan.vo.CheckType;
import com.zebone.nhis.webservice.zhongshan.vo.PacsApplyRecordRisVo;
import com.zebone.platform.modules.dao.DataBaseHelper;
import com.zebone.platform.modules.dao.jdbc.DataSourceRoute;

@Service
public class RISHandler {

	@Resource
	private RISService risService;

	private Logger logger = LoggerFactory.getLogger("nhis.zsbaWebSrv");//日志
	
	/**
	 * 1、获取除心电外的全部申请单（RIS01）
	 * 提供方：NHIS系统
	 * 调用方：RIS系统
	 * 调用时机：在RIS系统接收申请的时候调用，
	 * 例如：在床旁根据患者的住院号或者是指引单上的患者ID号进行调用，
	 * 	 	 也可以根据时间段和检查类型进行批量获取。
	 * @param param
	 * @return
	 */
	public String getRisAppList(Map<String, Object> param){
		String rslt = "";
		List<PacsApplyRecordRisVo> rsList = new ArrayList<PacsApplyRecordRisVo>();// 查询的申请单列表结果集
		int cnt = 0;
		//1 住院-NHIS cn_ris_apply
		rsList = risService.queryRisAppListFromNHIS(param);
		cnt = null == rsList ? 0 : rsList.size();
		logger.debug("新住院系统数据获取：" + rsList.size() + " 条");
		
		//2 门诊-NHIS cn_ris_apply
		rsList = risService.queryOpRisAppListFromNHIS(param,rsList,"1");
		logger.debug("新门诊系统数据获取：" + (null == rsList ? 0 : rsList.size() - cnt) + " 条");
		
		logger.debug("新系统数据获取：" + (null == rsList ? 0 : rsList.size()) + " 条");
		
		//his系统:1 门诊 / 2 住院  pacs_apply_record【参数控制是否获取 旧his 系统数据】
		if ("1".equals(ApplicationUtils.getPropertyValue("ris01.sendHisListToPacs", ""))) {
			DataSourceRoute.putAppId("HIS_bayy");
			//现逻辑：对照关系直接存储到 医嘱分类 中 检查大类的 shortname，改由查询时直接匹配【2020-03-09】
			//原逻辑:切换库之后，先循环处理相关检查类型 check_type【从his系统获取对照关系，循环处理nhis数据】
			//transCheckTypeByHis(rsList);//循环匹配检验接口码
			cnt = rsList.size();
			rsList = risService.queryRisAppList(rsList, "1"); //门诊
			logger.debug("旧门诊系统数据获取：" + (null == rsList ? 0 : rsList.size() - cnt) + " 条");
			cnt = rsList.size();
			rsList = risService.queryRisAppList(rsList, "2"); //住院
			logger.debug("旧住院系统数据获取：" + (null == rsList ? 0 : rsList.size() - cnt) + " 条");
		}
		
		//3 体检：体检库 pacs_apply_record【参数控制是否获取 体检系统 数据】
		if ("1".equals(ApplicationUtils.getPropertyValue("ris01.sendTJListToPacs", ""))) {
			DataSourceRoute.putAppId("TJ_bayy");
			cnt = rsList.size();
			rsList = risService.queryRisAppList(rsList, "3");
			logger.debug("体检系统数据获取：" + (null == rsList ? 0 : rsList.size() - cnt) + " 条");
		}
		
		//4 妇幼  : 妇幼 pacs_apply_record【参数控制是否获取 妇幼系统 数据】
		if ("1".equals(ApplicationUtils.getPropertyValue("ris01.sendFYListToPacs", ""))) {
			DataSourceRoute.putAppId("FY_bayy");
			cnt = rsList.size();
			rsList = risService.queryRisAppList(rsList,"4");
			logger.debug("妇幼系统数据获取：" + (null == rsList ? 0 : rsList.size() - cnt) + " 条");
		}
		
		//5 检验 ：检验病理的申请单
		if ("1".equals(ApplicationUtils.getPropertyValue("ris01.sendJYBLListToPacs", ""))) {
			DataSourceRoute.putAppId("LIS_bayy");
			cnt = rsList.size();
			rsList = risService.queryRisFromLisAppList(rsList,"5");
			logger.debug("检验病理 - 数据获取：" + (null == rsList ? 0 : rsList.size() - cnt) + " 条");
		}

		//6 新体检  : 体检 pacs_apply_record【参数控制是否获取 妇幼系统 数据】
		if ("1".equals(ApplicationUtils.getPropertyValue("ris01.sendNewTJListToPacs", ""))) {
			DataSourceRoute.putAppId("TJ_New_bayy");
			cnt = rsList.size();
			rsList = risService.queryRisAppList(rsList,"5");
			logger.debug("新体检系统数据获取：" + (null == rsList ? 0 : rsList.size() - cnt) + " 条");
		}
		
		//组装查询结构
		if (rsList != null && rsList.size() > 0) {
			for (int i = 0; i < rsList.size(); i++) {
				rslt += rsList.get(i).toString();
			}
			rslt = "[" + rslt.substring(0, rslt.length() - 1) + "]";
		}
		
		DataSourceRoute.putAppId("default");
		return CommonUtils.getString(new RespJson("0|成功|" + rslt, true));
	}

	/**
	 * 在 his 系统获取 检查类医嘱的pacs接口码，将nhis系统数据循环转换
	 * @param rsList
	 */
	private void transCheckTypeByHis(List<PacsApplyRecordRisVo> rsList) {
		if(rsList != null && rsList.size() > 0){
			List<CheckType> chkTypes = risService.getCheckTypeFromHis();
			String ordcate = "";
			if(chkTypes != null && chkTypes.size() > 0){
				for (PacsApplyRecordRisVo ris : rsList) {
					ordcate = ris.getCheckType();
					ordcate = ordcate.length() == 2 ? ordcate : (ordcate.length() > 2 ? ordcate.substring(0, 2) : ordcate);
					if(CommonUtils.isEmptyString(ordcate)) 
						continue;
					for (CheckType checkType : chkTypes) {
						if(checkType.getCode().equals(ordcate)) 
						{
							ris.setCheckType(checkType.getInterfaceCode());
							break;
						}
					}
				}
			}
		}
	}
	
	/**
	 * 2、更新申请单状态（RIS02）
	 * 调用方：心电系统 (在心电系统接收、登记、报告审核、撤销的时候调用)
	 * @param func_id   	功能编号(非空) : 默认ECG02
	 * @param send_type  	就诊类型(非空) : 1门诊 2住院 3体检 4妇幼
	 * @param record_sn		申请单主键/申请单号(不能同时为空)
	 * @param uid 			申请单主键/申请单号(不能同时为空)
	 * @param opera			操作人编码(非空)
	 * @param opera_name	操作人姓名(非空)
	 * @param exec_dept 	操作科室编码(非空)
	 * @param dept_name 	操作科室名称(非空)
	 * @param ope_time		操作时间 (非空)：yyyy-mm-dd hh:nn:ss
	 * @param ope_type 		操作类型 : 1接收 2登记 3报告 4预约 5取消预约 6取消登记 7上机 9撤销
	 * @param report_info 	结论建议  : 报告审核时不能为空
	 * 
	 * @return ret_code		返回状态码(非空) : 0成功，其他失败
	 * @return ret_msg		错误提示信息 (非空)
	 */
	public String updateRisApp(Map<String, Object> param){
		
		String rstxt = "";
		// 1、数据校验
		String checkMsg = chkUpdateParam(param);
		if (checkMsg != null && "" != checkMsg) {
			DataSourceRoute.putAppId("default");
			return CommonUtils.getString(new RespJson(checkMsg, false));
		}

		// 2、更新申请单记录
		String sendType = CommonUtils.getString(param.get("send_type"), "");
		String opeType = CommonUtils.getString(param.get("ope_type"), "");
		int resultCount = 0;
		
		//若存在 【检验病理数据】 则都需要按如下数据更新接收状态
		if ("1".equals(opeType) && "1".equals(ApplicationUtils.getPropertyValue("ris01.sendJYBLListToPacs", ""))) {
			DataSourceRoute.putAppId("LIS_bayy");
			resultCount = risService.updateRisApp(param);
		}
		
		//若在检验病理库中不存在，则按照原流程处理更新
		if(resultCount < 1){
			if ("1".equals(sendType)) // 门诊
			{
				// 判断 NHIS / CIS 申请
				DataSourceRoute.putAppId("default");
				String chkSql = " select ord.pk_cnord from cn_order ord "
						+ " left join cn_ris_apply ris on ord.pk_cnord = ris.pk_cnord "
						+ " left join cn_lab_apply lab on ord.pk_cnord = lab.pk_cnord"
						+ " where ( ord.code_apply =:uid or ord.code_apply =:record_sn )";
				Map<String, Object> map = DataBaseHelper.queryForMap(chkSql, param);
				
				if (map != null && map.get("pkCnord") != null) // 1、NHIS 申请
				{
					param.put("pkCnord", map.get("pkCnord").toString());
					resultCount= risService.updateOpNhisRisApp(param);
				}else {
					DataSourceRoute.putAppId("HIS_bayy");
					resultCount = risService.updateRisApp(param);
				}
			} 
			else if ("2".equals(sendType)) // 住院 
			{ 
				// 判断 NHIS / CIS 申请			
				DataSourceRoute.putAppId("default");
				String chkSql = " select ord.pk_cnord from cn_order ord "
						+ " left join cn_ris_apply ris on ord.pk_cnord = ris.pk_cnord "
						+ " left join cn_lab_apply lab on ord.pk_cnord = lab.pk_cnord"
						+ " where ( ord.code_apply =:uid or ord.code_apply =:record_sn )";
				Map<String, Object> map = DataBaseHelper.queryForMap(chkSql, param);
				
				if (map != null && map.get("pkCnord") != null) // 1、NHIS 申请
				{
					param.put("pkCnord", map.get("pkCnord").toString());
					resultCount = risService.updateNhisRisApp(param);// 更新 cn_ris_apply (eu_status)
				} 
				else // 2、CIS 申请
				{
					DataSourceRoute.putAppId("HIS_bayy");
					resultCount = risService.updateRisApp(param);
				}
			}
			else if ("3".equals(sendType)) // 体检
			{ 
				DataSourceRoute.putAppId("TJ_bayy");
				resultCount = risService.updateRisApp(param);
			} 
			else if ("4".equals(sendType)) // 妇幼
			{ 
				DataSourceRoute.putAppId("FY_bayy");
				resultCount = risService.updateRisApp(param);
			}
			else if ("5".equals(sendType)) // 新体检系统
			{ 
				DataSourceRoute.putAppId("TJ_New_bayy");
				resultCount = risService.updateTjNewRisApp(param);
			}
		}
		
		if (resultCount > 0){
			if (resultCount == 1)
				rstxt =  "0|成功";
			else
				rstxt =  "24|申请单号或申请单主键不唯一！存在 "+ resultCount +"条类似数据,并且都已更新";
		}
		else
			rstxt = "23|更新失败";
		return CommonUtils.getString(new RespJson(rstxt, false));
	}
	
	/**
	 * 2.1 校验 RIS02 更新申请单的入参
	 * @param func_id 		功能编号(非空) : 默认RIS02
	 * @param send_type 	就诊类型(非空) : 1门诊 2住院 3体检 4妇幼
	 * @param record_sn 	申请单主键(申请单主键/申请单号不能同时为空)
	 * @param uid 			申请单号(申请单主键/申请单号不能同时为空)
	 * @param opera 		操作人编码(非空)
	 * @param opera_name 	操作人姓名(非空)
	 * @param exec_dept 	操作科室编码(非空)
	 * @param dept_name 	操作科室名称(非空)
	 * @param ope_time 		操作时间 (非空)：yyyy-mm-dd hh:nn:ss
	 * @param ope_type 		操作类型 : 1接收 2登记 3报告 4预约 5取消预约 6取消登记 7上机 9撤销
	 * @param report_info 	结论建议 : 报告审核时不能为空
	 * @param date_exam 	预约检查时间 : 操作类型为预约时不能为空
	 * 
	 * @return errorMsg 0成功，其他失败 ,错误提示信息 (非空)
	 */
	public String chkUpdateParam(Map<String, Object> paramMap) {

		/* func_id 功能编号(非空) : 默认RIS02 */
		if (paramMap.get("func_id") == null
				|| "".equals(paramMap.get("func_id").toString())) {
			return ErrorMsg.func_id_Null;
		} else {
			String func_id = paramMap.get("func_id").toString();
			if (!"RIS02".equals(func_id))
				return ErrorMsg.func_id_Error;
		}

		/* send_type 就诊类型(非空) : 1门诊 2住院 3体检 4妇幼 */
		String send_type = "";
		/* 就诊类型(非空) : 1门诊 2住院 3体检 4妇幼 */
		if (paramMap.get("send_type") == null
				|| "".equals(paramMap.get("send_type").toString())) {
			return ErrorMsg.send_type_Null;
		} else {
			send_type = paramMap.get("send_type").toString();
			if (!"1".equals(send_type) && !"2".equals(send_type)
					&& !"3".equals(send_type) && !"4".equals(send_type))
				return ErrorMsg.send_type_Error;
		}

		/* record_sn 申请单主键/ uid申请单号(不能同时为空) */
		if ((paramMap.get("record_sn") == null || "".equals(paramMap.get("record_sn").toString()))
			&& (paramMap.get("uid") == null || "".equals(paramMap.get("uid").toString()))) {
			return ErrorMsg.record_snOruid_Null;
		}

		/* opera 操作人编码(非空) */
		if (paramMap.get("opera") == null
				|| "".equals(paramMap.get("opera").toString())) {
			return ErrorMsg.opera_Null;
		}

		/* opera_name 操作人姓名(非空) */
		if (paramMap.get("opera_name") == null
				|| "".equals(paramMap.get("opera_name").toString())) {
			return ErrorMsg.opera_name_Null;
		}

		/* exec_dept 操作科室编码(非空) */
		if (paramMap.get("exec_dept") == null
				|| "".equals(paramMap.get("exec_dept").toString())) {
			return ErrorMsg.exec_dept_Null;
		}

		/* dept_name 操作科室名称(非空) */
		if (paramMap.get("dept_name") == null
				|| "".equals(paramMap.get("dept_name").toString()))
			return ErrorMsg.dept_name_Null;

		/* ope_time 操作时间 (非空)：yyyy-MM-dd hh:mm:ss */
		if (paramMap.get("ope_time") == null
				|| "".equals(paramMap.get("ope_time").toString()))
			return ErrorMsg.ope_time_Null;
		else {
			String ope_time = paramMap.get("ope_time").toString();
			if (CommonUtils.rightDate(ope_time, "yyyy-MM-dd HH:mm:ss"))
				return ErrorMsg.ope_time_Error;
		}

		String ope_type = "";
		/* ope_type 操作类型(非空) : 1接收 2登记 3报告 4预约 9撤销 */
		if (paramMap.get("ope_type") == null
				|| "".equals(paramMap.get("ope_type").toString()))
			return ErrorMsg.ope_type_Null;
		else {
			ope_type = paramMap.get("ope_type").toString();
			if (!"1".equals(ope_type) && !"2".equals(ope_type) 
			 && !"3".equals(ope_type) && !"4".equals(ope_type) 
			 && !"5".equals(ope_type) && !"6".equals(ope_type)
			 && !"7".equals(ope_type) && !"9".equals(ope_type))
				return ErrorMsg.ris_ope_type_Error;
		}
		
		/* date_exam 预约检查时间 : 预约时不能为空 */
		if ("4".equals(ope_type)
				&& (paramMap.get("date_exam") == null 
				|| "".equals(paramMap .get("date_exam").toString()))) {
			return ErrorMsg.date_exam_Error;
		}

		/* report_info 结论建议 : 报告审核时不能为空 */
		if ("3".equals(ope_type)
				&& (paramMap.get("report_info") == null 
				|| "".equals(paramMap.get("report_info").toString()))) {
			return ErrorMsg.report_info_Null;
		}
		return null;
	}

}
