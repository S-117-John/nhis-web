package com.zebone.nhis.cn.ipdw.vo;

import java.util.List;

import com.zebone.nhis.common.module.cn.ipdw.BdCpTask;
import com.zebone.nhis.common.module.cn.ipdw.BdCpTaskDt;

public class PathOrderVo {

	private List<BdCpTask> list;
	
	private List<BdCpTaskDt> detailList;
	
	private BdCpTask bdCpTask;

	public BdCpTask getBdCpTask() {
		return bdCpTask;
	}

	public void setBdCpTask(BdCpTask bdCpTask) {
		this.bdCpTask = bdCpTask;
	}

	public List<BdCpTask> getList() {
		return list;
	}

	public void setList(List<BdCpTask> list) {
		this.list = list;
	}

	public List<BdCpTaskDt> getDetailList() {
		return detailList;
	}

	public void setDetailList(List<BdCpTaskDt> detailList) {
		this.detailList = detailList;
	}
}
