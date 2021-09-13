package com.zebone.nhis.pro.zsba.compay.ins.qgyb.vo.BottomClass;

/**
 * 6.3.2.2【3202】医药机构费用结算对明细账
 * @author Administrator
 *
 */
public class Output3202Fileinfo {
	
	private String	file_qury_no;//	文件查询号
	private String	filename;//	文件名称
	private String	dld_endtime;//	下载截止时间
	public String getFile_qury_no() {
		return file_qury_no;
	}
	public void setFile_qury_no(String file_qury_no) {
		this.file_qury_no = file_qury_no;
	}
	public String getFilename() {
		return filename;
	}
	public void setFilename(String filename) {
		this.filename = filename;
	}
	public String getDld_endtime() {
		return dld_endtime;
	}
	public void setDld_endtime(String dld_endtime) {
		this.dld_endtime = dld_endtime;
	}
	

}
