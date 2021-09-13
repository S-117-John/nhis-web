package com.zebone.nhis.common.module.compay.ins.shenzhen;

import java.util.List;


public class SzYbDrugInfo {

    /// 页码
    private String pageno ;

    /// 尾页标志
    private String endpage ;


    /// 本页返回条数
    private String currentpagesize ;


    /// 总记录数
    private String totalsize ;


    /// 总页数
    private String totalpagecount ;

    /// 数据集
    private List<SzYbDrugInfoList> outputlist ;


	public String getPageno() {
		return pageno;
	}


	public void setPageno(String pageno) {
		this.pageno = pageno;
	}


	public String getEndpage() {
		return endpage;
	}


	public void setEndpage(String endpage) {
		this.endpage = endpage;
	}


	public String getCurrentpagesize() {
		return currentpagesize;
	}


	public void setCurrentpagesize(String currentpagesize) {
		this.currentpagesize = currentpagesize;
	}


	public String getTotalsize() {
		return totalsize;
	}


	public void setTotalsize(String totalsize) {
		this.totalsize = totalsize;
	}


	public String getTotalpagecount() {
		return totalpagecount;
	}


	public void setTotalpagecount(String totalpagecount) {
		this.totalpagecount = totalpagecount;
	}


	public List<SzYbDrugInfoList> getOutputlist() {
		return outputlist;
	}


	public void setOutputlist(List<SzYbDrugInfoList> outputlist) {
		this.outputlist = outputlist;
	}


}
