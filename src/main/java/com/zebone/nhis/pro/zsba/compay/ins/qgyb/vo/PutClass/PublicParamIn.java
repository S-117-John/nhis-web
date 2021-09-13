package com.zebone.nhis.pro.zsba.compay.ins.qgyb.vo.PutClass;

import java.util.Date;

import com.google.gson.annotations.SerializedName;
import com.zebone.nhis.common.support.DateUtils;
import com.zebone.nhis.pro.zsba.compay.ins.qgyb.utils.YbHttpClass;

public class PublicParamIn {
	
	private String	infno;//	Y	交易编号	交易编号详见接口列表
	private String	msgid;//	Y	发送方报文ID	定点医药机构编号(12)+时间(14)+ 顺序号(4)时间格式：yyyyMMddHHmmss
	private String	insuplc_admdvs;//		参保地医保区划 如果交易输入中含有人员编号，此项必填，可通过【1101】人员信息获取交易取得
	private String	mdtrtarea_admvs;//	Y	就医地医保区划
	private String	recer_sys_code;//	Y	接收方系统代码	用于多套系统接入，区分不同系统使用
	private String	dev_no;//		设备编号	
	private String	dev_safe_info;//		设备安全信息	
	private String	cainfo;//		数字签名信息	对input按签名类型进行签名
	private String	signtype;//		签名类型	建议使用SM2、SM3
	private String	infver;//	Y	接口版本号	例如：“V1.0”，版本号由医保下发通知。
	private String	opter_type;//	Y	经办人类别	1-经办人；2-自助终端；3-移动终端
	private String	opter;//	Y	经办人	传入经办人编号
	private String	opter_name;//	Y	经办人姓名	
	private String	inf_time;//	Y	交易时间	格式：yyyy-MM-dd HH:mm:ss
	private String	fixmedins_code;//	Y	定点医疗机构编号	
	private String	fixmedins_name;//	Y	定点医疗机构编号	
	private String	sign_no;//		签到流水号	通过签到【9001】交易获取
	//private PublicInput	input;//	Y	交易输入	
	
	public PublicParamIn(){
		
	}
	
	public PublicParamIn(String infno, String insuplc_admdvs, String infver,  String opter_type, String opter, String opter_name, String sign_no) {
		this.infno = infno;
		this.msgid = "H44020300006"+DateUtils.getDateTimeStr(new Date())+YbHttpClass.getRandomString(4);
		this.insuplc_admdvs = insuplc_admdvs;
		this.mdtrtarea_admvs = "441500";
		this.recer_sys_code = "01";
		this.infver = infver;
		this.opter_type = opter_type;
		this.opter = opter;
		this.opter_name = opter_name;
		this.inf_time = DateUtils.getDateTime();
		this.fixmedins_code = "GD_SW1097";
		this.fixmedins_name = "广东汕尾红海湾经济开发区东洲街道社区卫生服务中心";
		this.sign_no = sign_no;
		
	}

	public void Assignment(String infno, String insuplc_admdvs, String infver,  String opter_type, String opter, String opter_name, String sign_no) {
		this.infno = infno;
		this.msgid = "H44200100009"+DateUtils.getDateTimeStr(new Date())+YbHttpClass.getRandomString(4);
		this.insuplc_admdvs = insuplc_admdvs;
		this.mdtrtarea_admvs = "442000";
		this.recer_sys_code = "01";
		this.infver = infver;
		this.opter_type = opter_type;
		this.opter = opter;
		this.opter_name = opter_name;
		this.inf_time = DateUtils.getDateTime();
		this.fixmedins_code = "H44200100009";
		this.fixmedins_name = "中山市博爱医院";
		this.sign_no = sign_no;
	}

	public String getInfno() {
		return infno;
	}

	public void setInfno(String infno) {
		this.infno = infno;
	}

	public String getMsgid() {
		return msgid;
	}

	public void setMsgid(String msgid) {
		this.msgid = msgid;
	}

	public String getInsuplc_admdvs() {
		return insuplc_admdvs;
	}

	public void setInsuplc_admdvs(String insuplc_admdvs) {
		this.insuplc_admdvs = insuplc_admdvs;
	}

	public String getMdtrtarea_admvs() {
		return mdtrtarea_admvs;
	}

	public void setMdtrtarea_admvs(String mdtrtarea_admvs) {
		this.mdtrtarea_admvs = mdtrtarea_admvs;
	}

	public String getRecer_sys_code() {
		return recer_sys_code;
	}

	public void setRecer_sys_code(String recer_sys_code) {
		this.recer_sys_code = recer_sys_code;
	}

	public String getDev_no() {
		return dev_no;
	}

	public void setDev_no(String dev_no) {
		this.dev_no = dev_no;
	}

	public String getDev_safe_info() {
		return dev_safe_info;
	}

	public void setDev_safe_info(String dev_safe_info) {
		this.dev_safe_info = dev_safe_info;
	}

	public String getCainfo() {
		return cainfo;
	}

	public void setCainfo(String cainfo) {
		this.cainfo = cainfo;
	}

	public String getSigntype() {
		return signtype;
	}

	public void setSigntype(String signtype) {
		this.signtype = signtype;
	}

	public String getInfver() {
		return infver;
	}

	public void setInfver(String infver) {
		this.infver = infver;
	}

	public String getOpter_type() {
		return opter_type;
	}

	public void setOpter_type(String opter_type) {
		this.opter_type = opter_type;
	}

	public String getOpter() {
		return opter;
	}

	public void setOpter(String opter) {
		this.opter = opter;
	}

	public String getOpter_name() {
		return opter_name;
	}

	public void setOpter_name(String opter_name) {
		this.opter_name = opter_name;
	}

	public String getInf_time() {
		return inf_time;
	}

	public void setInf_time(String inf_time) {
		this.inf_time = inf_time;
	}

	public String getFixmedins_code() {
		return fixmedins_code;
	}

	public void setFixmedins_code(String fixmedins_code) {
		this.fixmedins_code = fixmedins_code;
	}

	public String getFixmedins_name() {
		return fixmedins_name;
	}

	public void setFixmedins_name(String fixmedins_name) {
		this.fixmedins_name = fixmedins_name;
	}

	public String getSign_no() {
		return sign_no;
	}

	public void setSign_no(String sign_no) {
		this.sign_no = sign_no;
	}
	
	
}
