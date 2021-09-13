package com.zebone.nhis.common.module.compay.ins.shenzhen;




public class PageSzYbQryParam {

	/** 每页行数 */
	private int pageSize;
	/** 页码 */
	private int pageIndex;
	/** 查询项目入参 */
	private String qryItemParam;

	/**查询医保项目名称入参*/
	private String qryInsuNameParam;

	 /** 查询批文号入参 */
	private String qryApprovalNumParam;

    /** 查询生产厂家入参 */
	private String qryFactoryNameParam;
	
	//是否查询未对照标示
	private String flagNoMap;
	//是否启用
	private String flagActive;
    // 医保分类
	private String eunmMedicalInsurance;

    //项目类别
	private String projectCatalogue;

	//项目类别2
	private String projectCatalogue2;
	
	//疾病信息查询条件
	private String searchParam;

	public String getQryInsuNameParam() {
		return qryInsuNameParam;
	}

	public void setQryInsuNameParam(String qryInsuNameParam) {
		this.qryInsuNameParam = qryInsuNameParam;
	}

	public String getFlagNoMap() {
		return flagNoMap;
	}
	public void setFlagNoMap(String flagNoMap) {
		this.flagNoMap = flagNoMap;
	}

	public String getFlagActive() {
		return flagActive;
	}
	public void setFlagActive(String flagActive) {
		this.flagActive = flagActive;
	}

	public String getSearchParam() {
		return searchParam;
	}
	public void setSearchParam(String searchParam) {
		this.searchParam = searchParam;
	}
	public int getPageSize() {
		return pageSize;
	}
	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}
	public int getPageIndex() {
		return pageIndex;
	}
	public void setPageIndex(int pageIndex) {
		this.pageIndex = pageIndex;
	}
	public String getQryItemParam() {
		return qryItemParam;
	}
	public void setQryItemParam(String qryItemParam) {
		this.qryItemParam = qryItemParam;
	}
	public String getEunmMedicalInsurance() {
		return eunmMedicalInsurance;
	}
	public void setEunmMedicalInsurance(String eunmMedicalInsurance) {
		this.eunmMedicalInsurance = eunmMedicalInsurance;
	}
	public String getProjectCatalogue() {
		return projectCatalogue;
	}
	public void setProjectCatalogue(String projectCatalogue) {
		this.projectCatalogue = projectCatalogue;
	}
	public String getProjectCatalogue2() {
		return projectCatalogue2;
	}
	public void setProjectCatalogue2(String projectCatalogue2) {
		this.projectCatalogue2 = projectCatalogue2;
	}
	public String getQryApprovalNumParam() {
		return qryApprovalNumParam;
	}
	public void setQryApprovalNumParam(String qryApprovalNumParam) {
		this.qryApprovalNumParam = qryApprovalNumParam;
	}
	public String getQryFactoryNameParam() {
		return qryFactoryNameParam;
	}
	public void setQryFactoryNameParam(String qryFactoryNameParam) {
		this.qryFactoryNameParam = qryFactoryNameParam;
	}



}
