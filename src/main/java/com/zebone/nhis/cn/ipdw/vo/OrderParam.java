package com.zebone.nhis.cn.ipdw.vo;

import java.util.ArrayList;
import java.util.List;

import com.zebone.nhis.common.module.cn.ipdw.CnOrdAntiApply;
import com.zebone.nhis.common.module.cn.ipdw.CnOrder;

public class OrderParam {
	private List<CnOrder>  changeOrdList = new ArrayList<CnOrder>();
    private List<CnOrder> newOrdList = new ArrayList<CnOrder>();
    private List<CnOrder> updateOrdList = new ArrayList<CnOrder>();
    private List<CnOrder> delOrdList = new ArrayList<CnOrder>();
    private List<CnOrder> cpOrdList = new ArrayList<CnOrder>();
    private List<CnOrder> fitOrdList = new ArrayList<CnOrder>();
    private CnOrdAntiApply antiItemApply;
    private String flagCancleStop;
    private String flagOpManager;
    private String isLabor;//是否产房
	private String ifry;//是否人医客户化功能

	public List<CnOrder> getChangeOrdList() {
		return changeOrdList;
	}
	public void setChangeOrdList(List<CnOrder> changeOrdList) {
		this.changeOrdList = changeOrdList;
	}
	public List<CnOrder> getNewOrdList() {
		return newOrdList;
	}
	public void setNewOrdList(List<CnOrder> newOrdList) {
		this.newOrdList = newOrdList;
	}
	public List<CnOrder> getUpdateOrdList() {
		return updateOrdList;
	}
	public void setUpdateOrdList(List<CnOrder> updateOrdList) {
		this.updateOrdList = updateOrdList;
	}
	public List<CnOrder> getDelOrdList() {
		return delOrdList;
	}
	public void setDelOrdList(List<CnOrder> delOrdList) {
		this.delOrdList = delOrdList;
	}
	public List<CnOrder> getCpOrdList() {
		return cpOrdList;
	}
	public void setCpOrdList(List<CnOrder> cpOrdList) {
		this.cpOrdList = cpOrdList;
	}
	public String getFlagCancleStop() {
		return flagCancleStop;
	}
	public void setFlagCancleStop(String flagCancleStop) {
		this.flagCancleStop = flagCancleStop;
	}
	public String getFlagOpManager() {
		return flagOpManager;
	}
	public void setFlagOpManager(String flagOpManager) {
		this.flagOpManager = flagOpManager;
	}
	public List<CnOrder> getFitOrdList() {
		return fitOrdList;
	}
	public void setFitOrdList(List<CnOrder> fitOrdList) {
		this.fitOrdList = fitOrdList;
	}
	public CnOrdAntiApply getAntiItemApply() {
		return antiItemApply;
	}
	public void setAntiItemApply(CnOrdAntiApply antiItemApply) {
		this.antiItemApply = antiItemApply;
	}

	public String getIsLabor() {
		return isLabor;
	}

	public void setIsLabor(String isLabor) {
		this.isLabor = isLabor;
	}

	public String getIfry() {
		return ifry;
	}

	public void setIfry(String ifry) {
		this.ifry = ifry;
	}
}
