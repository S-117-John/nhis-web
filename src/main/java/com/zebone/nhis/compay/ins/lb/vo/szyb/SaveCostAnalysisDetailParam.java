package com.zebone.nhis.compay.ins.lb.vo.szyb;
import java.util.List;

public class SaveCostAnalysisDetailParam {

	/// <summary>
    /// 处方明细数据
    /// </summary>
    public List<CostDetailUploadParam> costDetailUploadParam;

    /// <summary>
    /// 费用分解的数据
    /// </summary>
    public List<CostDetailUpload> costDetailUpload;

    /// <summary>
    /// 住院流水号(门诊流水号)
    /// </summary>
    public String ywlsh;

    /// <summary>
    /// 单次交易流水号
    /// </summary>
    public String jylsh;

	public List<CostDetailUploadParam> getCostDetailUploadParam() {
		return costDetailUploadParam;
	}

	public void setList(List<CostDetailUploadParam> costDetailUploadParam) {
		this.costDetailUploadParam = costDetailUploadParam;
	}

	public List<CostDetailUpload> getCostDetailUpload() {
		return costDetailUpload;
	}

	public void setCostDetailList(List<CostDetailUpload> costDetailUpload) {
		this.costDetailUpload = costDetailUpload;
	}

	public String getYwlsh() {
		return ywlsh;
	}

	public void setYwlsh(String ywlsh) {
		this.ywlsh = ywlsh;
	}

	public String getJylsh() {
		return jylsh;
	}

	public void setJylsh(String jylsh) {
		this.jylsh = jylsh;
	}
}
