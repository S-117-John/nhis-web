package com.zebone.nhis.pro.zsba.compay.ins.qgyb.vo.BottomClass;

/**
 * 医疗保障基金结算清单信息上传入参——重症监护信息
 * @author Administrator
 *
 */
public class Input4101Icuinfo {
	
	private String	scs_cutd_ward_type;//	Y	重症监护病房类型	
	private String	scs_cutd_inpool_time;//	Y	重症监护进入时间	
	private String	scs_cutd_exit_time;//	Y	重症监护退出时间	
	private String	scs_cutd_sum_dura;//	Y	重症监护合计时长	格式：天数/小时数/分钟数例：1/13/24
	public String getScs_cutd_ward_type() {
		return scs_cutd_ward_type;
	}
	public void setScs_cutd_ward_type(String scs_cutd_ward_type) {
		this.scs_cutd_ward_type = scs_cutd_ward_type;
	}
	public String getScs_cutd_inpool_time() {
		return scs_cutd_inpool_time;
	}
	public void setScs_cutd_inpool_time(String scs_cutd_inpool_time) {
		this.scs_cutd_inpool_time = scs_cutd_inpool_time;
	}
	public String getScs_cutd_exit_time() {
		return scs_cutd_exit_time;
	}
	public void setScs_cutd_exit_time(String scs_cutd_exit_time) {
		this.scs_cutd_exit_time = scs_cutd_exit_time;
	}
	public String getScs_cutd_sum_dura() {
		return scs_cutd_sum_dura;
	}
	public void setScs_cutd_sum_dura(String scs_cutd_sum_dura) {
		this.scs_cutd_sum_dura = scs_cutd_sum_dura;
	}

	
}
