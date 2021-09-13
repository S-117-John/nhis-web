package com.zebone.nhis.ma.tpi.rhip.vo;

import com.zebone.nhis.common.module.pi.PiMaster;
import com.zebone.nhis.common.module.pv.PvEncounter;
import com.zebone.nhis.common.module.pv.PvIp;
import com.zebone.nhis.common.module.pv.PvOp;

/**
 * 就诊记录vo（包括pv、pi、pv_ip、pv_op）
 * @author chengjia
 *
 */
public class EncVo {
	private PiMaster pi;
	private PvEncounter pv;
	private PvIp pvIp;
	private PvOp pvOp;
	private String apptCode;
	private String hpCode;
	private String euHptype;
	private String deptCodeOrig;
	private String codeEmpPv;
	private String deptNameGh;
	
	public String getHpCode() {
		return hpCode;
	}
	public void setHpCode(String hpCode) {
		this.hpCode = hpCode;
	}
	public String getEuHptype() {
		return euHptype;
	}
	public void setEuHptype(String euHptype) {
		this.euHptype = euHptype;
	}
	public String getApptCode() {
		return apptCode;
	}
	public void setApptCode(String apptCode) {
		this.apptCode = apptCode;
	}
	public PiMaster getPi() {
		return pi;
	}
	public void setPi(PiMaster pi) {
		this.pi = pi;
	}
	public PvEncounter getPv() {
		return pv;
	}
	public void setPv(PvEncounter pv) {
		this.pv = pv;
	}
	public PvIp getPvIp() {
		return pvIp;
	}
	public void setPvIp(PvIp pvIp) {
		this.pvIp = pvIp;
	}
	public PvOp getPvOp() {
		return pvOp;
	}
	public void setPvOp(PvOp pvOp) {
		this.pvOp = pvOp;
	}
	public String getDeptCodeOrig() {
		return deptCodeOrig;
	}
	public void setDeptCodeOrig(String deptCodeOrig) {
		this.deptCodeOrig = deptCodeOrig;
	}
	public String getDeptNameGh() {
		return deptNameGh;
	}
	public void setDeptNameGh(String deptNameGh) {
		this.deptNameGh = deptNameGh;
	}
	public String getCodeEmpPv() {
		return codeEmpPv;
	}
	public void setCodeEmpPv(String codeEmpPv) {
		this.codeEmpPv = codeEmpPv;
	}
	
}
