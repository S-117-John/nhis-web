package com.zebone.nhis.sch.plan.vo;

import java.util.List;
import java.util.Set;

import com.alibaba.fastjson.JSON;
import com.zebone.nhis.common.module.base.bd.code.BdCodeDateslot;

public class SchInfoVo {
	
	private String pkSchres;
	
	private String pkSchsrv;

	private String schresName;
	
	private String schsrvName;

	/**医生姓名*/
	private String doctorName;

	private Set<String> pkDateslots;
	
	private List<BdCodeDateslot> bdCodeDateslots;
	
	private List<SchSchVo> schschs;
	
	public SchInfoVo(){
		super();
	}

	public SchInfoVo(String pkSchres, String pkSchsrv) {
		super();
		this.pkSchres = pkSchres;
		this.pkSchsrv = pkSchsrv;
	}
	public SchInfoVo(String pkSchres,String schresName, String pkSchsrv, String schsrvName) {
		super();
		this.pkSchres = pkSchres;
		this.pkSchsrv = pkSchsrv;
		this.schresName = schresName;
		this.schsrvName = schsrvName;
	}

	public List<BdCodeDateslot> getBdCodeDateslots() {
		return bdCodeDateslots;
	}

	public void setBdCodeDateslots(List<BdCodeDateslot> bdCodeDateslots) {
		this.bdCodeDateslots = bdCodeDateslots;
	}

	public String getPkSchres() {
		return pkSchres;
	}

	public void setPkSchres(String pkSchres) {
		this.pkSchres = pkSchres;
	}

	public String getPkSchsrv() {
		return pkSchsrv;
	}

	public void setPkSchsrv(String pkSchsrv) {
		this.pkSchsrv = pkSchsrv;
	}

	public String getSchresName() {
		return schresName;
	}

	public void setSchresName(String schresName) {
		this.schresName = schresName;
	}

	public String getSchsrvName() {
		return schsrvName;
	}

	public void setSchsrvName(String schsrvName) {
		this.schsrvName = schsrvName;
	}

	public Set<String> getPkDateslots() {
		return pkDateslots;
	}

	public void setPkDateslots(Set<String> pkDateslots) {
		this.pkDateslots = pkDateslots;
	}
	
	
	public List<SchSchVo> getSchschs() {
		return schschs;
	}

	public void setSchschs(List<SchSchVo> schschs) {
		this.schschs = schschs;
	}

	public String getDoctorName() {
		return doctorName;
	}

	public void setDoctorName(String doctorName) {
		this.doctorName = doctorName;
	}

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return JSON.toJSONString(this);
	}

	
	
}
