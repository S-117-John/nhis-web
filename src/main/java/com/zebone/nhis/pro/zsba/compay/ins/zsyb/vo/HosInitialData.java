package com.zebone.nhis.pro.zsba.compay.ins.zsyb.vo;

import java.util.Date;

import com.zebone.platform.modules.dao.build.au.Field;

/**
 * 查询入院登记界面初始数据
 * @author zim
 *
 */
public class HosInitialData {

	private String pkPi; //患者主键
	private String pkPv; //就诊主键
	private String codeIp; //住院号
	private String ipTimes; //住院次数 
	private String namePi; //患者姓名
	private String dtSex; //性别编码
	private String birthDate; //出生日期
	//private String pkDept; //入院科室主键
	private String nameDept; //入院科室名称
	//private String pkDeptNs; //入院病区主键
	private String bqdm; //入院病区名称
	private String cwdh; //当前床号
	private String dtIdtype;//证件类型
	private String gmsfhm; //证件号码
	private String grsxh; //参保号
	private String pkHp; //医保主计划
	private Date ryrq; //入院时间
	private Date cyrq; //出院时间
    private String pkInsu;//nhis - 医保主计划主键
	private String pkInspv; //中山住院医保登记主键
	private String ryzd; //入院诊断
	private String ryzdgjdm; //入院ICD-10编码
	private String ryzd2; //入院诊断2
	private String ryzdgjdm2; //入院ICD-10编码2
	private String ryzd3; //入院诊断3
	private String ryzdgjdm3; //入院ICD-10编码3
	private String ryzd4; //入院诊断4
	private String ryzdgjdm4; //入院ICD-10编码4
	private String zzysxm; //主诊医师姓名
	private String jsffbz; //旧伤复发标志
	private String wsbz; //外伤标志
	private Date ssrq; //受伤日期
	private String sylb; //生育类别
	private String zszh; //准生证号
	private String jzjlh; //医保就诊登记号
	private String xzlx; //险种类型
	private String ryqk;//病情转归(入院情况)
	private String status; //状态标志
	private String ysbh;//医生编号
	
	public String getPkPi() {
		return pkPi;
	}
	public void setPkPi(String pkPi) {
		this.pkPi = pkPi;
	}
	public String getPkPv() {
		return pkPv;
	}
	public void setPkPv(String pkPv) {
		this.pkPv = pkPv;
	}
	public String getCodeIp() {
		return codeIp;
	}
	public void setCodeIp(String codeIp) {
		this.codeIp = codeIp;
	}
	public String getIpTimes() {
		return ipTimes;
	}
	public void setIpTimes(String ipTimes) {
		this.ipTimes = ipTimes;
	}
	public String getNamePi() {
		return namePi;
	}
	public void setNamePi(String namePi) {
		this.namePi = namePi;
	}
	public String getDtSex() {
		return dtSex;
	}
	public void setDtSex(String dtSex) {
		this.dtSex = dtSex;
	}
	public String getBirthDate() {
		return birthDate;
	}
	public void setBirthDate(String birthDate) {
		this.birthDate = birthDate;
	}
	public String getNameDept() {
		return nameDept;
	}
	public void setNameDept(String nameDept) {
		this.nameDept = nameDept;
	}
	public String getBqdm() {
		return bqdm;
	}
	public void setBqdm(String bqdm) {
		this.bqdm = bqdm;
	}
	public String getCwdh() {
		return cwdh;
	}
	public void setCwdh(String cwdh) {
		this.cwdh = cwdh;
	}
	public String getGmsfhm() {
		return gmsfhm;
	}
	public void setGmsfhm(String gmsfhm) {
		this.gmsfhm = gmsfhm;
	}
	public String getGrsxh() {
		return grsxh;
	}
	public void setGrsxh(String grsxh) {
		this.grsxh = grsxh;
	}
	public String getPkHp() {
		return pkHp;
	}
	public void setPkHp(String pkHp) {
		this.pkHp = pkHp;
	}
	public Date getRyrq() {
		return ryrq;
	}
	public void setRyrq(Date ryrq) {
		this.ryrq = ryrq;
	}
	public Date getCyrq() {
		return cyrq;
	}
	public void setCyrq(Date cyrq) {
		this.cyrq = cyrq;
	}
	public String getPkInsu() {
		return pkInsu;
	}
	public void setPkInsu(String pkInsu) {
		this.pkInsu = pkInsu;
	}
	public String getPkInspv() {
		return pkInspv;
	}
	public void setPkInspv(String pkInspv) {
		this.pkInspv = pkInspv;
	}
	public String getRyzd() {
		return ryzd;
	}
	public void setRyzd(String ryzd) {
		this.ryzd = ryzd;
	}
	public String getRyzdgjdm() {
		return ryzdgjdm;
	}
	public void setRyzdgjdm(String ryzdgjdm) {
		this.ryzdgjdm = ryzdgjdm;
	}
	public String getRyzd2() {
		return ryzd2;
	}
	public void setRyzd2(String ryzd2) {
		this.ryzd2 = ryzd2;
	}
	public String getRyzdgjdm2() {
		return ryzdgjdm2;
	}
	public void setRyzdgjdm2(String ryzdgjdm2) {
		this.ryzdgjdm2 = ryzdgjdm2;
	}
	public String getRyzd3() {
		return ryzd3;
	}
	public void setRyzd3(String ryzd3) {
		this.ryzd3 = ryzd3;
	}
	public String getRyzdgjdm3() {
		return ryzdgjdm3;
	}
	public void setRyzdgjdm3(String ryzdgjdm3) {
		this.ryzdgjdm3 = ryzdgjdm3;
	}
	public String getRyzd4() {
		return ryzd4;
	}
	public void setRyzd4(String ryzd4) {
		this.ryzd4 = ryzd4;
	}
	public String getRyzdgjdm4() {
		return ryzdgjdm4;
	}
	public void setRyzdgjdm4(String ryzdgjdm4) {
		this.ryzdgjdm4 = ryzdgjdm4;
	}
	public String getZzysxm() {
		return zzysxm;
	}
	public void setZzysxm(String zzysxm) {
		this.zzysxm = zzysxm;
	}
	public String getJsffbz() {
		return jsffbz;
	}
	public void setJsffbz(String jsffbz) {
		this.jsffbz = jsffbz;
	}
	public String getWsbz() {
		return wsbz;
	}
	public void setWsbz(String wsbz) {
		this.wsbz = wsbz;
	}
	public Date getSsrq() {
		return ssrq;
	}
	public void setSsrq(Date ssrq) {
		this.ssrq = ssrq;
	}
	public String getSylb() {
		return sylb;
	}
	public void setSylb(String sylb) {
		this.sylb = sylb;
	}
	public String getZszh() {
		return zszh;
	}
	public void setZszh(String zszh) {
		this.zszh = zszh;
	}
	public String getJzjlh() {
		return jzjlh;
	}
	public void setJzjlh(String jzjlh) {
		this.jzjlh = jzjlh;
	}
	public String getXzlx() {
		return xzlx;
	}
	public void setXzlx(String xzlx) {
		this.xzlx = xzlx;
	}
	public String getRyqk() {
		return ryqk;
	}
	public void setRyqk(String ryqk) {
		this.ryqk = ryqk;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getYsbh() {
		return ysbh;
	}
	public void setYsbh(String ysbh) {
		this.ysbh = ysbh;
	}
	public String getDtIdtype() {
		return dtIdtype;
	}
	public void setDtIdtype(String dtIdtype) {
		this.dtIdtype = dtIdtype;
	}
	
}
