package com.zebone.nhis.webservice.zhongshan.service;

import com.zebone.nhis.common.support.CommonUtils;
import com.zebone.nhis.webservice.support.RespJson;
import com.zebone.nhis.webservice.zhongshan.support.ErrorMsg;
import com.zebone.nhis.webservice.zhongshan.vo.PacsApplyRecord;
import com.zebone.platform.modules.dao.DataBaseHelper;
import com.zebone.platform.modules.dao.jdbc.DataSourceRoute;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class ECGHandler {

    @Resource
    private ECGService service;

    /**
     * 1. 根据传入参数 查询 患者心电检查申请单（ECG01）
     * 调用方：心电系统  (在心电系统接收申请的时候调用)
     *
     * @param func_id      功能编号(非空) : 默认ECG01
     * @param send_type    就诊类型(非空) : 1门诊 2住院 3体检 4妇幼
     * @param check_type   检查类型(非空) : 心电检查类型为7
     * @param uid          申请单号(非空)
     * @param patient_id   患者id : 就诊类型非住院时不能为空。门诊对应患者id，体检对应体检号，妇幼对应保健号
     * @param inpatient_no 住院号 : 就诊类型为住院时，不能为空。仅对住院有效
     * @param start_time   开始时间 : YYYY-MM-DD
     * @param end_time     结束时间  : YYYY-MM-DD
     * @return datalist        申请单列表
     */
    public String getXDAppList(Map<String, Object> param) {
        // 1 、数据校验
        String checkMsg = chkQryParam(param);
        if (checkMsg != null && "" != checkMsg)
            return CommonUtils.getString(new RespJson(checkMsg, true));
        
        // 2、查询申请单列表
        String start_time = param.get("start_time").toString();
        String end_time = param.get("end_time").toString();
        //设置查询时间范围
        if (!"".equals(start_time)) {
        	String[] strBegin = start_time.split("-");
        	param.put("startTime", strBegin[0] + strBegin[1] + strBegin[2]
        			+ "000000");
        }
        if (!"".equals(end_time)) {
        	String[] strEnd = end_time.split("-");
        	param.put("endTime", strEnd[0] + strEnd[1] + strEnd[2] + "235959");
        }
        
        String rslt = "";
        List<PacsApplyRecord> list = new ArrayList<PacsApplyRecord>();// 查询的申请单列表结果集
        String send_type = param.get("send_type").toString();
        
        if ("1".equals(send_type)) // 门诊 ： HIS pacs_apply_record + NHIS cn_ris_apply
        {
        	List<String> pkPvs = service.getPkPvByOp(param);//判断是否为NHIS门诊患者
        	if (!CollectionUtils.isEmpty(pkPvs)) 
        	{
        		param.put("pkPv", pkPvs);
        		list.addAll(service.queryOpEcgAppListFromNHIS(param));// NHIS cn_ris_apply 
        	} 
        	DataSourceRoute.putAppId("HIS_bayy");
        	list.addAll(service.queryAppList(param)); // HIS pacs_apply_record
        } 
        else if("2".equals(send_type)) // 住院 ： HIS pacs_apply_record ||  NHIS cn_ris_apply
        {
        	List<String> pkPvs = service.getPkPvByIp(param);//判断是否为NHIS住院患者
        	if (!CollectionUtils.isEmpty(pkPvs))  
        	{
        		param.put("pkPv", pkPvs);
        		list = service.queryIpEcgAppListFromNHIS(param);// NHIS cn_ris_apply 
        	} else {
        		DataSourceRoute.putAppId("HIS_bayy");
        		list = service.queryAppList(param);// HIS pacs_apply_record
        	}
        }
        else if ("3".equals(send_type)) // 体检：体检库 pacs_apply_record
        {
        	DataSourceRoute.putAppId("TJ_bayy");
        	list.addAll(service.queryAppList(param)); //执行查询SQL
        } 
        else if ("4".equals(send_type))//妇幼  : 妇幼 pacs_apply_record
        {
        	DataSourceRoute.putAppId("FY_bayy");
        	list.addAll(service.queryAppList(param)); //执行查询SQL
        }
        
        if (list != null && list.size() > 0) {
        	for (int i = 0; i < list.size(); i++) {
        		rslt += list.get(i).toString();
        	}
        	rslt = "[" + rslt.substring(0, rslt.length() - 1) + "]";
        }
        return CommonUtils.getString(new RespJson("0|成功|" + rslt, true));
    }

    /**
     * 1.1 校验 ECG01 获取申请单的入参
     *
     * @param func_id      功能编号(非空) : 默认ECG01
     * @param send_type    就诊类型(非空) : 1门诊 2住院 3体检 4妇幼
     * @param check_type   检查类型(非空) : 心电检查类型为7
     * @param uid          申请单号(非空)
     * @param patient_id   患者id : 就诊类型非住院时不能为空。门诊对应患者id，体检对应体检号，妇幼对应保健号
     * @param inpatient_no 住院号 : 就诊类型为住院时，不能为空。仅对住院有效
     * @param start_time   开始时间 : YYYY-MM-DD
     * @param end_time     结束时间 : YYYY-MM-DD
     * @return errorMsg
     */
    public String chkQryParam(Map<String, Object> paramMap) {

        /* 功能编号(非空) : 默认ECG01 */
        if (paramMap.get("func_id") == null
                || "".equals(paramMap.get("func_id").toString())) {
            return ErrorMsg.func_id_Null;
        } else {
            String func_id = paramMap.get("func_id").toString();
            if (!"ECG01".equals(func_id))
                return ErrorMsg.func_id_Error;
        }
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

        /* 检查类型(非空) : 心电检查类型为7 */
        if (paramMap.get("check_type") == null
                || "".equals(paramMap.get("check_type").toString())) {
            return ErrorMsg.check_type_Null;
        } else {
            String check_type = paramMap.get("check_type").toString();
            if (!"7".equals(check_type)) {
                return ErrorMsg.check_type_Error;
            }
        }

        /* 申请单号(非空) */
        // if (paramMap.get("uid") == null
        // || "".equals(paramMap.get("uid").toString())) {
        // return ErrorMsg.uid_Null;
        // }
        if (!"2".equals(send_type)) {
            /* 患者id : 就诊类型非住院时不能为空 */
            if (paramMap.get("patient_id") == null
                    || "".equals(paramMap.get("patient_id").toString()))
                return ErrorMsg.patient_id_Null;
        } else {
            /* 住院号 : 就诊类型为住院时，不能为空。仅对住院有效 */
            if (paramMap.get("inpatient_no") == null
                    || "".equals(paramMap.get("inpatient_no").toString()))
                return ErrorMsg.inpatient_no_Null;
        }

        /* 开始时间 : YYYY-MM-DD */
        if (paramMap.get("start_time") == null
                || "".equals(paramMap.get("start_time").toString())) {
            return ErrorMsg.start_time_Error;
        } else {
            String start_time = paramMap.get("start_time").toString();
            if (!CommonUtils.rightDate(start_time, "yyyy-MM-dd") || start_time.length() != 10)
                return ErrorMsg.start_time_Error;
        }

        /* 结束时间 : YYYY-MM-DD */
        if (paramMap.get("end_time") == null
                || "".equals(paramMap.get("end_time").toString())) {
            return ErrorMsg.end_time_Error;
        } else {
            String end_time = paramMap.get("end_time").toString();
            if (!CommonUtils.rightDate(end_time, "yyyy-MM-dd") || end_time.length() != 10)
                return ErrorMsg.end_time_Error;
        }
        return null;
    }

    /**
     * 2、更新申请单状态（ECG02）
     * 调用方：心电系统 (在心电系统接收、登记、报告审核、撤销的时候调用)
     *
     * @param func_id     功能编号(非空) : 默认ECG02
     * @param send_type   就诊类型(非空) : 1门诊 2住院 3体检 4妇幼
     * @param record_sn   申请单主键/申请单号(不能同时为空)
     * @param uid         申请单主键/申请单号(不能同时为空)
     * @param opera       操作人编码(非空)
     * @param opera_name  操作人姓名(非空)
     * @param exec_dept   操作科室编码(非空)
     * @param dept_name   操作科室名称(非空)
     * @param ope_time    操作时间 (非空)：yyyy-mm-dd hh:nn:ss
     * @param ope_type    操作类型 : 1接收 2登记 3报告 9撤销
     * @param report_info 结论建议  : 报告审核时不能为空
     * @return ret_msg        错误提示信息 (非空)
     */
    public String updateXDAppStatus(Map<String, Object> param) {
        // 1、数据校验
        String checkMsg = chkUpdateParam(param);
        if (checkMsg != null && "" != checkMsg) {
            DataSourceRoute.putAppId("default");
            return CommonUtils.getString(new RespJson(checkMsg, false));
        }
        // 2、更新申请单记录
        String sendType = param.get("send_type").toString();
        int resultCount = 0;
        if ("1".equals(sendType)) // 门诊
        {
        	// 判断 NHIS / CIS 申请
        	String chkSql = " select ord.pk_cnord from cn_order ord , cn_ris_apply ris"
        			+ " where ord.pk_cnord = ris.pk_cnord  "
        			+ " and ( ord.code_apply =:uid or code_apply =:record_sn )";
        	Map<String, Object> map = DataBaseHelper.queryForMap(chkSql, param);
        	if (map != null && map.get("pkCnord") != null) // 1、NHIS 申请
        	{
        		param.put("pkCnord", map.get("pkCnord").toString());
        		resultCount = service.updateNhisOpApp(param);// 更新 cn_ris_apply (eu_status)
        	}else{
        		DataSourceRoute.putAppId("HIS_bayy");
        		resultCount = service.updatePacsApp(param);
        	}
        } 
        else if ("2".equals(sendType)) { // 住院// pacs_apply_record
        	// 判断 NHIS / CIS 申请
        	String chkSql = " select ord.pk_cnord from cn_order ord , cn_ris_apply ris"
        			+ " where ord.pk_cnord = ris.pk_cnord  "
        			+ " and ( ord.code_apply =:uid or code_apply =:record_sn )";
        	Map<String, Object> map = DataBaseHelper.queryForMap(chkSql, param);
        	if (map != null && map.get("pkCnord") != null) // 1、NHIS 申请
        	{
        		param.put("pkCnord", map.get("pkCnord").toString());
        		resultCount = service.updateNhisIpApp(param);// 更新 cn_ris_apply (eu_status)
        	} else // 2、CIS 申请
        	{
        		DataSourceRoute.putAppId("HIS_bayy");
        		resultCount = service.updatePacsApp(param);
        	}
        }
        else if ("3".equals(sendType)) // 体检
        {
        	DataSourceRoute.putAppId("TJ_bayy");
        	resultCount = service.updatePacsApp(param);
        } else if ("4".equals(sendType)) // 妇幼
        {
        	DataSourceRoute.putAppId("FY_bayy");
        	resultCount = service.updatePacsApp(param);
        }
        String rstxt = "";
        if (resultCount > 0) {
            if (resultCount == 1)
                rstxt = "0|成功";
            else
                rstxt = "24|申请单号或申请单主键不唯一！存在 " + resultCount + "条类似数据,并且都已更新";
        } else
            rstxt = "23|未获取到需要写入的数据！";
        return CommonUtils.getString(new RespJson(rstxt, false));
    }

    /**
     * 2.1 校验 ECG02 更新申请单的入参
     *
     * @param func_id     功能编号(非空) : 默认ECG02
     * @param send_type   就诊类型(非空) : 1门诊 2住院 3体检 4妇幼
     * @param record_sn   申请单主键/申请单号(不能同时为空)
     * @param uid         申请单主键/申请单号(不能同时为空)
     * @param opera       操作人编码(非空，1.2.3.4.9)
     * @param opera_name  操作人姓名(非空)
     * @param exec_dept   操作科室编码(非空)
     * @param dept_name   操作科室名称(非空)
     * @param ope_time    操作时间 (非空)：yyyy-mm-dd hh:nn:ss
     * @param ope_type    操作类型 : 1接收 2登记 3报告 9撤销
     * @param report_info 结论建议 : 报告审核时不能为空
     * @return errorMsg 0成功，其他失败 ,错误提示信息 (非空)
     */
    public String chkUpdateParam(Map<String, Object> paramMap) {

        /* func_id 功能编号(非空) : 默认ECG02 */
        if (paramMap.get("func_id") == null
                || "".equals(paramMap.get("func_id").toString())) {
            return ErrorMsg.func_id_Null;
        } else {
            String func_id = paramMap.get("func_id").toString();
            if (!"ECG02".equals(func_id))
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
//		if (paramMap.get("opera") == null
//				|| "".equals(paramMap.get("opera").toString())) {
//			return ErrorMsg.opera_Null;
//		}

        /* opera_name 操作人姓名(非空) */
        if (paramMap.get("opera_name") == null
                || "".equals(paramMap.get("opera_name").toString())) {
            return ErrorMsg.opera_name_Null;
        }

        /* exec_dept 操作科室编码(非空) */
//		if (paramMap.get("exec_dept") == null
//				|| "".equals(paramMap.get("exec_dept").toString())) {
//			return ErrorMsg.exec_dept_Null;
//		}

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
        /* ope_type 操作类型(非空) : 1接收 2登记 3报告 9撤销 */
        if (paramMap.get("ope_type") == null
                || "".equals(paramMap.get("ope_type").toString()))
            return ErrorMsg.ope_type_Null;
        else {
            ope_type = paramMap.get("ope_type").toString();
            if (!"1".equals(ope_type) && !"2".equals(ope_type)
                    && !"3".equals(ope_type) && !"4".equals(ope_type)
                    && !"9".equals(ope_type))
                return ErrorMsg.ecg_ope_type_Error;
        }

        /* report_info 结论建议 : 报告审核时不能为空 */
        if ("3".equals(ope_type)
                && (paramMap.get("report_info") == null || "".equals(paramMap
                .get("report_info").toString()))) {
            return ErrorMsg.report_info_Null;
        }
        return null;
    }
}
