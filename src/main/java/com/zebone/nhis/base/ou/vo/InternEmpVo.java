package com.zebone.nhis.base.ou.vo;

import java.util.Date;

public class InternEmpVo {
	
	private String pkEmp;			//人员主键
	private String pkOrg;			//机构
	private String codeEmp;			//人员编码
	private String nameEmp;			//人员名称
	private String dtSex;			//性别
	private String dtIdentype;		//证件类型
	private String idno;			//证件号码
	private String pyCode;			//拼音码
	private String dCode;			//自定义码
	private Date birthday;			//出生日期
	private Date workDate;			//参加工作日期
	private String addr;			//家庭地址
	private String homePhone;		//家庭电话
	private String workPhone;		//工作电话
	private String mobile;			//手机
	private String dtEmptype;		//人员职业类型
	private String dtEmpsrvtype;	//医疗项目权限
	private String flag_Pres;		//处方权
	private String flagAnes;		//麻醉权
	private String flagSpirOne;		//精一处方权
	private String flagSpirSec;		//精二处方权
	private String flagPoi;			//毒药处方权
	private String flagSpec;		//专家出诊标志
	private String flagActive;		//启用状态
	private String email;			//邮箱
	private String spec;			//专长
	private String photo;			//照片
	private String CAID;			//CA序列号
	private String CAID2;			//CA序列号2
	private String imgSign;			//手签图片
	private String creator;			//创建人
	private Date createTime;		//创建时间
	private String modifier;		//修改人
	private Date modityTime;		//修改时间
	private String delFlag;			//删除标志
	private String ts;				//时间戳
	private String dtInternsrc;		//来源
	private Date dateBegin;			//实习开始日期
	private Date dateEnd;			//实习结束日期
	private String euWorktype;		//实习工作方式
	private String cycle;			//轮转周期
	private String euTeachtype;		//带教方式
	private String pkEmpTeach;		//带教导师
	private String nameEmpTeach;	//导师姓名
	private String note;			//备注
	public String getPkEmp() {
		return pkEmp;
	}
	public void setPkEmp(String pkEmp) {
		this.pkEmp = pkEmp;
	}
	public String getPkOrg() {
		return pkOrg;
	}
	public void setPkOrg(String pkOrg) {
		this.pkOrg = pkOrg;
	}
	public String getCodeEmp() {
		return codeEmp;
	}
	public void setCodeEmp(String codeEmp) {
		this.codeEmp = codeEmp;
	}
	public String getNameEmp() {
		return nameEmp;
	}
	public void setNameEmp(String nameEmp) {
		this.nameEmp = nameEmp;
	}
	public String getDtSex() {
		return dtSex;
	}
	public void setDtSex(String dtSex) {
		this.dtSex = dtSex;
	}
	public String getDtIdentype() {
		return dtIdentype;
	}
	public void setDtIdentype(String dtIdentype) {
		this.dtIdentype = dtIdentype;
	}
	public String getIdno() {
		return idno;
	}
	public void setIdno(String idno) {
		this.idno = idno;
	}
	public String getPyCode() {
		return pyCode;
	}
	public void setPyCode(String pyCode) {
		this.pyCode = pyCode;
	}
	public String getdCode() {
		return dCode;
	}
	public void setdCode(String dCode) {
		this.dCode = dCode;
	}
	public Date getBirthday() {
		return birthday;
	}
	public void setBirthday(Date birthday) {
		this.birthday = birthday;
	}
	public Date getWorkDate() {
		return workDate;
	}
	public void setWorkDate(Date workDate) {
		this.workDate = workDate;
	}
	public String getAddr() {
		return addr;
	}
	public void setAddr(String addr) {
		this.addr = addr;
	}
	public String getHomePhone() {
		return homePhone;
	}
	public void setHomePhone(String homePhone) {
		this.homePhone = homePhone;
	}
	public String getWorkPhone() {
		return workPhone;
	}
	public void setWorkPhone(String workPhone) {
		this.workPhone = workPhone;
	}
	public String getMobile() {
		return mobile;
	}
	public void setMobile(String mobile) {
		this.mobile = mobile;
	}
	public String getDtEmptype() {
		return dtEmptype;
	}
	public void setDtEmptype(String dtEmptype) {
		this.dtEmptype = dtEmptype;
	}
	public String getDtEmpsrvtype() {
		return dtEmpsrvtype;
	}
	public void setDtEmpsrvtype(String dtEmpsrvtype) {
		this.dtEmpsrvtype = dtEmpsrvtype;
	}
	public String getFlag_Pres() {
		return flag_Pres;
	}
	public void setFlag_Pres(String flag_Pres) {
		this.flag_Pres = flag_Pres;
	}
	public String getFlagAnes() {
		return flagAnes;
	}
	public void setFlagAnes(String flagAnes) {
		this.flagAnes = flagAnes;
	}
	public String getFlagSpirOne() {
		return flagSpirOne;
	}
	public void setFlagSpirOne(String flagSpirOne) {
		this.flagSpirOne = flagSpirOne;
	}
	public String getFlagSpirSec() {
		return flagSpirSec;
	}
	public void setFlagSpirSec(String flagSpirSec) {
		this.flagSpirSec = flagSpirSec;
	}
	public String getFlagPoi() {
		return flagPoi;
	}
	public void setFlagPoi(String flagPoi) {
		this.flagPoi = flagPoi;
	}
	public String getFlagSpec() {
		return flagSpec;
	}
	public void setFlagSpec(String flagSpec) {
		this.flagSpec = flagSpec;
	}
	public String getFlagActive() {
		return flagActive;
	}
	public void setFlagActive(String flagActive) {
		this.flagActive = flagActive;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getSpec() {
		return spec;
	}
	public void setSpec(String spec) {
		this.spec = spec;
	}
	public String getPhoto() {
		return photo;
	}
	public void setPhoto(String photo) {
		this.photo = photo;
	}
	public String getCAID() {
		return CAID;
	}
	public void setCAID(String cAID) {
		CAID = cAID;
	}
	public String getCAID2() {
		return CAID2;
	}
	public void setCAID2(String cAID2) {
		CAID2 = cAID2;
	}
	public String getImgSign() {
		return imgSign;
	}
	public void setImgSign(String imgSign) {
		this.imgSign = imgSign;
	}
	public String getCreator() {
		return creator;
	}
	public void setCreator(String creator) {
		this.creator = creator;
	}
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	public String getModifier() {
		return modifier;
	}
	public void setModifier(String modifier) {
		this.modifier = modifier;
	}
	public Date getModityTime() {
		return modityTime;
	}
	public void setModityTime(Date modityTime) {
		this.modityTime = modityTime;
	}
	public String getDelFlag() {
		return delFlag;
	}
	public void setDelFlag(String delFlag) {
		this.delFlag = delFlag;
	}
	public String getTs() {
		return ts;
	}
	public void setTs(String ts) {
		this.ts = ts;
	}
	public String getDtInternsrc() {
		return dtInternsrc;
	}
	public void setDtInternsrc(String dtInternsrc) {
		this.dtInternsrc = dtInternsrc;
	}
	public Date getDateBegin() {
		return dateBegin;
	}
	public void setDateBegin(Date dateBegin) {
		this.dateBegin = dateBegin;
	}
	public Date getDateEnd() {
		return dateEnd;
	}
	public void setDateEnd(Date dateEnd) {
		this.dateEnd = dateEnd;
	}
	public String getEuWorktype() {
		return euWorktype;
	}
	public void setEuWorktype(String euWorktype) {
		this.euWorktype = euWorktype;
	}
	public String getCycle() {
		return cycle;
	}
	public void setCycle(String cycle) {
		this.cycle = cycle;
	}
	public String getEuTeachtype() {
		return euTeachtype;
	}
	public void setEuTeachtype(String euTeachtype) {
		this.euTeachtype = euTeachtype;
	}
	public String getPkEmpTeach() {
		return pkEmpTeach;
	}
	public void setPkEmpTeach(String pkEmpTeach) {
		this.pkEmpTeach = pkEmpTeach;
	}
	public String getNameEmpTeach() {
		return nameEmpTeach;
	}
	public void setNameEmpTeach(String nameEmpTeach) {
		this.nameEmpTeach = nameEmpTeach;
	}
	public String getNote() {
		return note;
	}
	public void setNote(String note) {
		this.note = note;
	}
	
	
	

}
