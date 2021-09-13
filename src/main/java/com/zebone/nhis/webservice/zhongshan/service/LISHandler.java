package com.zebone.nhis.webservice.zhongshan.service;

import com.zebone.nhis.webservice.support.RespJson;
import com.zebone.nhis.webservice.zhongshan.vo.CheckInParam;
import com.zebone.platform.modules.utils.JsonUtil;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class LISHandler {

	@Resource
	private LISService lisService;
	
	/**
	 * 1、条码打印与试管费用加收（LIS01）
	 *  调用方：LIS系统(在LIS系统打印标本条码时，调用该服务，告知NHIS系统记录申请单所对应的试管条码)
	 * @param func_id 		功能编号 LIS01 非空
	 * @param print_time 	打印时间yyyy-MM-dd HH:mm:ss 非空
	 * @param ope_code 		 操作人工号 非空
	 * @param bar_code 		条形编号 非空
	 * @param patient_id 	患者ID-code_pi 非空
	 * @param admiss_time 	就诊次数 非空
	 * @param charge_code 	试管收费编码 如果没有试管收费，则传空串
	 * @param ord_list 		医嘱号列表
	 * {"func_id":"LIS01",print_time":"2017-12-16","ope_code":"liliang","bar_code":"barcode2","patient_id":"PI000063","admiss_times":"1","charge_code":"003257","order_sn":"5232,5233"}
	 * @return
	 */
	public String updateLabApplySampNo(String input_info) {
		CheckInParam chkParam = JsonUtil.readValue(input_info,CheckInParam.class);
		String mes = lisService.updateLabApplySampNoService(chkParam);
		if(mes!=null) return new RespJson(mes, false).toString();
		return new RespJson("0|成功", false).toString();
	}

	/**
	 * 2、更新标本状态(LIS02) 
	 * 调用方：LIS系统(在病区护士打印完条码后，发送标本、检验科接收标本，标本上机、报告审核等环节调用)
	 * @param func_id 		功能编号 LIS02 非空
	 * @param ope_time 		操作时间yyyy-MM-dd HH:mm:ss 非空
	 * @param ope_code 		操作人工号 非空
	 * @param bar_code 		条形编号 非空
	 * @param patient_id 	患者ID-code_pi 非空
	 * @param admiss_times 	就诊次数 非空
	 * @param ope_type 		操作类型 非空 2标本发送、3标本接收、4标本拒收、5上机、6报告
	 * @param report_url	报告存放位置 如果报告以URL方式或PDF方式存储，则写明具体调用地址或文件名，否则传空
	 * {"func_id":"LIS02","ope_time":"2017-12-16","ope_code":"liliang","bar_code":"barcode2","patient_id":"PI000063","admiss_times":"1","ope_type":"3","report_url":""}
	 * @return
	 */
	public String updateLabApplyStatus(String input_info) {
		CheckInParam chkParam = JsonUtil.readValue(input_info,CheckInParam.class);
		String mes = lisService.updateLabApplyStatusService(chkParam);
	    if(mes!=null) return  mes;
	    return new RespJson("0|成功", false).toString();
	}

//	/**
//	 * 3.获取检验申请单 调用方：LIS系统(在LIS系统接收检验申请的时候调用)
//	 * @param start_date
//	 * @param end_date
//	 * @param inpatient_no
//	 * @param apply_unit
//	 * @return
//	 */
//	public String qryLabApplyInfo(String input_info) {
//		CheckInParam chkParam = JsonUtil.readValue(input_info,CheckInParam.class);
//		// 校验参数
//		String msg = chkParam.toString();
//		if (msg != null)
//			return new RespJson(msg, false).toString();
//		
//		// 获取检验申请单
//		Map<String, Object> paramMap = new HashMap<String, Object>();
//		paramMap.put("beginDate",chkParam.getStart_date());
//		paramMap.put("endDate", chkParam.getEnd_date());
//		paramMap.put("codeIp",chkParam.getInpatient_no());
//		paramMap.put("pkDept", chkParam.getApply_unit());
//		List<Map<String, Object>> labList = lisService .qryLabApplyInfo(paramMap);
//		return new RespJson(lisService.tranJson(labList), true).toString();
//	}
	
}
