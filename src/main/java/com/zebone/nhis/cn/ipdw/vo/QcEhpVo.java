package com.zebone.nhis.cn.ipdw.vo;

import java.util.List;
import java.util.Map;

import com.zebone.nhis.common.module.cn.ipdw.QcEhpDetail;
import com.zebone.nhis.common.module.cn.ipdw.QcEhpRec;

/**
 * 病案质控
 * @author dell
 *
 */
public class QcEhpVo {
	private List<Map<String,Object>> qcEhpList;
	//质控记录
	private List<Map<String,Object>> qcEhpRecList;
	//缺陷描述
	private List<Map<String,Object>> qcEhpDetail;
	private int totalCount;
	/** 每页行数 */
	private int pageSize;
	/** 页码 */
	private int pageIndex;
	public QcEhpRec qcEhpRecVo;
    public List<QcEhpDetail> listDetail;
	
	public List<Map<String, Object>> getQcEhpList() {
		return qcEhpList;
	}
	public void setQcEhpList(List<Map<String, Object>> qcEhpList) {
		this.qcEhpList = qcEhpList;
	}
	public int getTotalCount() {
		return totalCount;
	}
	public void setTotalCount(int totalCount) {
		this.totalCount = totalCount;
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
	public List<Map<String, Object>> getQcEhpRecList() {
		return qcEhpRecList;
	}
	public void setQcEhpRecList(List<Map<String, Object>> qcEhpRecList) {
		this.qcEhpRecList = qcEhpRecList;
	}
	public List<Map<String, Object>> getQcEhpDetail() {
		return qcEhpDetail;
	}
	public void setQcEhpDetail(List<Map<String, Object>> qcEhpDetail) {
		this.qcEhpDetail = qcEhpDetail;
	}
	public QcEhpRec getQcEhpRecVo() {
		return qcEhpRecVo;
	}
	public void setQcEhpRecVo(QcEhpRec qcEhpRecVo) {
		this.qcEhpRecVo = qcEhpRecVo;
	}
	public List<QcEhpDetail> getListDetail() {
		return listDetail;
	}
	public void setListDetail(List<QcEhpDetail> listDetail) {
		this.listDetail = listDetail;
	}
	
	
	
	
}
