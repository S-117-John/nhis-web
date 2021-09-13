package com.zebone.nhis.ex.nis.ns.vo;

import com.zebone.nhis.common.module.ex.nis.ns.ExPdApplyDetail;
import com.zebone.nhis.ex.nis.ns.support.pdap.SynExListInfoHandler;

/**
 * 生成请领单明细模型
 * @author yangxue
 *
 */
public class PdApplyDtVo {
	private ExPdApplyDetail dtvo;
	private PdApplyVo showVO;
	private SynExListInfoHandler info;
	private String errMsg;//错误信息
	
	public String getErrMsg() {
		return errMsg;
	}
	public void setErrMsg(String errMsg) {
		this.errMsg = errMsg;
	}
	public ExPdApplyDetail getDtvo() {
		return dtvo;
	}
	public void setDtvo(ExPdApplyDetail dtvo) {
		this.dtvo = dtvo;
	}
	public PdApplyVo getShowVO() {
		return showVO;
	}
	public void setShowVO(PdApplyVo showVO) {
		this.showVO = showVO;
	}
	public SynExListInfoHandler getInfo() {
		return info;
	}
	public void setInfo(SynExListInfoHandler info) {
		this.info = info;
	}
	
}
