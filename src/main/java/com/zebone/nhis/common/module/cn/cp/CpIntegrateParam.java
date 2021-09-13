package com.zebone.nhis.common.module.cn.cp;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.zebone.nhis.common.module.cn.ipdw.BdCpReason;

public class CpIntegrateParam {

	private Map<String,Object>  unListParamMap=new HashMap<String,Object> ();
	
	//临床路径--业务主表
	private CpRec cpRec=new CpRec();
	//临床路径--模板
	private CpTemp CpTempVo=new CpTemp();
	//临床路径--启用原因
	private List<BdCpReason>  cpReasonList= new ArrayList<BdCpReason>();
	//临床路径--阶段
	private List<CpRecPhase>  cpRecPhaseList= new ArrayList<CpRecPhase>();
	//临床路径--变异原因
	private List<CpRecExp>  cpRecExpList= new ArrayList<CpRecExp>();
	//医嘱列表
	private List<CpRecOrd>  cpOrdList = new ArrayList<CpRecOrd>();

	public List<CpRecOrd> getCpOrdList() {
		return cpOrdList;
	}

	public void setCpOrdList(List<CpRecOrd> cpOrdList) {
		this.cpOrdList = cpOrdList;
	}

	public List<CpRecPhase> getCpRecPhaseList() {
		return cpRecPhaseList;
	}

	public void setCpRecPhaseList(List<CpRecPhase> cpRecPhaseList) {
		this.cpRecPhaseList = cpRecPhaseList;
	}

	public List<CpRecExp> getCpRecExpList() {
		return cpRecExpList;
	}

	public void setCpRecExpList(List<CpRecExp> cpRecExpList) {
		this.cpRecExpList = cpRecExpList;
	}

	public Map<String, Object> getUnListParamMap() {
		return unListParamMap;
	}

	public void setUnListParamMap(Map<String, Object> unListParamMap) {
		this.unListParamMap = unListParamMap;
	}

	public CpRec getCpRec() {
		return cpRec;
	}

	public void setCpRec(CpRec cpRec) {
		this.cpRec = cpRec;
	}

	public CpTemp getCpTempVo() {
		return CpTempVo;
	}

	public void setCpTempVo(CpTemp cpTempVo) {
		CpTempVo = cpTempVo;
	}

	public List<BdCpReason> getCpReasonList() {
		return cpReasonList;
	}

	public void setCpReasonList(List<BdCpReason> cpReasonList) {
		this.cpReasonList = cpReasonList;
	}
	
	
	
}
