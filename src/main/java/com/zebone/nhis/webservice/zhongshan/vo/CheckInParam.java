package com.zebone.nhis.webservice.zhongshan.vo;

import com.zebone.nhis.webservice.zhongshan.support.ErrorMsg;
import org.apache.commons.lang3.StringUtils;

import java.util.List;

public class CheckInParam {
	//功能编号
	private String func_id;
	private String print_time;//打印时间
	private String ope_code;//操作人工号
	private String bar_code;//条码编号
	private String patient_id;//患者ID
	private String admiss_times;//就诊次数
	private String charge_code;//试管收费编码
	private List<String> ord_list;//指检验申请项目合并（合管）操作时对应医嘱号，可能包含多个
	private String ope_time;//操作时间
	private String ope_type; //操作类型 2标本发送、3标本接收、4标本拒收、5上机、6报告
	private String report_url;//报告存放位置	
	private String rpt_id;//报告单号
    
	@Override
	public String toString() {
		if(StringUtils.isEmpty(func_id)) return ErrorMsg.func_id_Null;
		if(StringUtils.isEmpty(ope_code)) return "1|操作人工号ope_code为空!";
		if(StringUtils.isEmpty(bar_code)) return "1|条码编号bar_code为空!";		
		if(StringUtils.isEmpty(patient_id)) return ErrorMsg.patient_id_Null;
		if("LIS01".equals(func_id)){
			if(StringUtils.isEmpty(print_time)) return "1|打印时间print_time为空";
			if(ord_list == null || ord_list.size() == 0) return "1|医嘱号列表ord_list为空!";
		}else if("LIS02".equals(func_id)){
			if(StringUtils.isEmpty(ope_time)) return ErrorMsg.ope_time_Null;
			if(StringUtils.isEmpty(ope_type)) return ErrorMsg.ope_type_Null;
		}
		if (!("LIS01".equals(func_id)||"LIS02".equals(func_id))){
			if(StringUtils.isEmpty(admiss_times))
			{return "1|就诊次数admiss_times为空!";}
		}
		return null;
	}

	public List<String> getOrd_list() {
		return ord_list;
	}

	public void setOrd_list(List<String> ord_list) {
		this.ord_list = ord_list;
	}

	public String getFunc_id() {
		return func_id;
	}

	public void setFunc_id(String func_id) {
		this.func_id = func_id;
	}

	public String getPrint_time() {
		return print_time;
	}

	public void setPrint_time(String print_time) {
		this.print_time = print_time;
	}

	public String getOpe_time() {
		return ope_time;
	}

	public void setOpe_time(String ope_time) {
		this.ope_time = ope_time;
	}

	public String getOpe_code() {
		return ope_code;
	}

	public void setOpe_code(String ope_code) {
		this.ope_code = ope_code;
	}

	public String getBar_code() {
		return bar_code;
	}

	public void setBar_code(String bar_code) {
		this.bar_code = bar_code;
	}

	public String getPatient_id() {
		return patient_id;
	}

	public void setPatient_id(String patient_id) {
		this.patient_id = patient_id;
	}

	public String getAdmiss_times() {
		return admiss_times;
	}

	public void setAdmiss_times(String admiss_times) {
		this.admiss_times = admiss_times;
	}

	public String getCharge_code() {
		return charge_code;
	}

	public void setCharge_code(String charge_code) {
		this.charge_code = charge_code;
	}

	public String getOpe_type() {
		return ope_type;
	}

	public void setOpe_type(String ope_type) {
		this.ope_type = ope_type;
	}

	public String getReport_url() {
		return report_url;
	}

	public void setReport_url(String report_url) {
		this.report_url = report_url;
	}


	public String getRpt_id() {
		return rpt_id;
	}

	public void setRpt_id(String rpt_id) {
		this.rpt_id = rpt_id;
	}
	
}
