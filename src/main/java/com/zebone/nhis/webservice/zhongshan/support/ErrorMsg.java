package com.zebone.nhis.webservice.zhongshan.support;

public class ErrorMsg {
	
	public static String func_id_Null  = "1|功能编码func_id 为空!|";
	
	public static String func_id_Error = "2|功能编码 func_id 错误!|";
	
	public static String send_type_Null = "3|就诊类型send_type 为空!|";
	
	public static String send_type_Error = "4|就诊类型send_type 错误!(1门诊 2住院 3体检 4妇幼)|";
	
	public static String check_type_Null = "5|检查类型check_type 为空!|";
	
	public static String check_type_Error = "6|检查类型check_type 错误!(心电检查类型为7)|";
	
	public static String uid_Null = "7|申请单号 uid 为空!|";
	
	public static String patient_id_Null = "8|就诊类型非住院时患者id patient_id 不能为空!|";
	
	public static String inpatient_no_Null = "9|就诊类型为住院时住院号 inpatient_no 不能为空!|";
	
	public static String start_time_Error = "10|开始时间 start_time 格式不对!(YYYY-MM-DD)|";
	
	public static String end_time_Error = "11|结束时间格end_time 式不对!(YYYY-MM-DD)|";
	
	public static String record_snOruid_Null = "12|申请单主键 record_sn  和申请单号uid 不能同时为空!|";
	
	public static String opera_Null = "13|操作人编码opera 为空!|";
	
	public static String opera_name_Null = "14|操作人姓名opera_name 为空!|";
	
	public static String exec_dept_Null = "15|操作科室编码exec_dept 为空!|";
	
	public static String dept_name_Null = "16|操作科室名称dept_name 为空!|";
	
	public static String ope_time_Null = "17|操作时间 ope_time 为空!|";
	
	public static String ope_time_Error = "18|操作时间 ope_time 格式不对!(yyyy-mm-dd hh:mm:ss)|";
	
	public static String ope_type_Null = "19|操作类型 ope_type 为空!|";
	
	public static String ecg_ope_type_Error = "20|操作类型 ope_type 错误!(1接收 2登记 3报告 4取消报告 9撤销登记)|";
	
	public static String report_info_Null = "21|结论建议report_info : 报告审核时不能为空！|";
	
	public static String ope_type_Notip_Error = "22|非住院或住院不在NHIS系统时，只处理1接收/3报告的操作！|";
	
	public static String ope_type_ip_Error = "23|住院时，在NHIS系统中只处理 2接收/3报告的操作！|";
	
	public static String date_exam_Error = "24|操作类型 ope_type=4[预约]时不能为空！|";
	
	public static String ris_ope_type_Error = "25|操作类型 ope_type 错误!(1接收 2登记 3报告 4预约 5取消预约 6取消登记 7上机 9撤销)|";
	
}
