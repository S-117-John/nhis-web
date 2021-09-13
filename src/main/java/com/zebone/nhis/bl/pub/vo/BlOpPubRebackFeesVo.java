package com.zebone.nhis.bl.pub.vo;

import java.util.List;

import com.zebone.nhis.common.module.bl.opcg.BlOpDt;

/**
 * 退费时数据传输层
 * @author gongxy
 * 
 */
public class BlOpPubRebackFeesVo {

	/**
	 * 计费主键
	 */
	private String pkOpcg;

	/**
	 * 退费数量
	 */
	private Double quanReBack;

	/**
	 * 可退数量
	 */
	private Double canBack;

	/**
	 * 产生的退费记录
	 */
	private List<BlOpDt> blOpDtOlds;

	/**
	 * 如果是部分退费则是产生的新的计费明细
	 */
	private List<BlOpDt> blOpDtNews;

	public String getPkOpcg() {

		return pkOpcg;
	}

	public void setPkOpcg(String pkOpcg) {

		this.pkOpcg = pkOpcg;
	}

	public List<BlOpDt> getBlOpDtOlds() {

		return blOpDtOlds;
	}

	public void setBlOpDtOlds(List<BlOpDt> blOpDtOlds) {

		this.blOpDtOlds = blOpDtOlds;
	}

	public List<BlOpDt> getBlOpDtNews() {

		return blOpDtNews;
	}

	public void setBlOpDtNews(List<BlOpDt> blOpDtNews) {

		this.blOpDtNews = blOpDtNews;
	}

	public Double getQuanReBack() {

		return quanReBack;
	}

	public void setQuanReBack(Double quanReBack) {

		this.quanReBack = quanReBack;
	}

	public Double getCanBack() {

		return canBack;
	}

	public void setCanBack(Double canBack) {

		this.canBack = canBack;
	}

}
