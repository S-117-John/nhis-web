package com.zebone.nhis.ma.pub.sd.vo.cmrInsu;

public class CiIcdVo {

    /**
     * 诊断编码
     */
    private String diagnosisCode;

    /**
     * 诊断名称
     */
    private String diagnosisName;
    
    /**
     * 诊断类型
     */
    private String dtDiagtype;    

	/**
     * 诊断排序
     * 数据字典：
     * 0-主要诊断
     * 1-次要诊断
     */
    private String diagSort;

    public String getDiagnosisCode() {
        return diagnosisCode;
    }

    public void setDiagnosisCode(String diagnosisCode) {
        this.diagnosisCode = diagnosisCode;
    }

    public String getDiagnosisName() {
        return diagnosisName;
    }

    public void setDiagnosisName(String diagnosisName) {
        this.diagnosisName = diagnosisName;
    }

    public String getDiagSort() {
        return diagSort;
    }

    public void setDiagSort(String diagSort) {
        this.diagSort = diagSort;
    }
    
    public String getDtDiagtype() {
		return dtDiagtype;
	}

	public void setDtDiagtype(String dtDiagtype) {
		this.dtDiagtype = dtDiagtype;
	}
}
