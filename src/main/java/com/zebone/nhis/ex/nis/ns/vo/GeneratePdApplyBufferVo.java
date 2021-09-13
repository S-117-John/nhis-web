package com.zebone.nhis.ex.nis.ns.vo;

import java.util.Date;
import java.util.List;

import com.zebone.nhis.common.module.ex.nis.ns.ExPdApply;
import com.zebone.nhis.common.module.ex.nis.ns.ExPdApplyDetail;
import com.zebone.nhis.ex.nis.ns.support.pdap.SynExListInfoHandler;
/**
 * 生成请领单缓存
 * @author yangxue
 *
 */
public class GeneratePdApplyBufferVo {

	private Date exceTime;					//生成请领操作执行时间
	private String pkOrg;							//机构
	private boolean isFinish;						//执行完成标志
	private List<ExPdApply> apList;				//请领单缓存
	private List<ExPdApplyDetail> dtList;			//请领明细缓存
	private List<SynExListInfoHandler> updateList;				//执行单更新数据缓存
	private List<PdApplyVo> showList;		//界面显示列表缓存
	private String err;								//生成过程中错误信息
	
	
	
	public boolean isFinish() {
		return isFinish;
	}
	public void setFinish(boolean isFinish) {
		this.isFinish = isFinish;
	}

	public String getErr() {
		return err;
	}
	public void setErr(String err) {
		this.err = err;
	}
	public Date getExceTime() {
		return exceTime;
	}
	public void setExceTime(Date exceTime) {
		this.exceTime = exceTime;
	}
	public String getPkOrg() {
		return pkOrg;
	}
	public void setPkOrg(String pkOrg) {
		this.pkOrg = pkOrg;
	}
	public List<ExPdApply> getApList() {
		return apList;
	}
	public void setApList(List<ExPdApply> apList) {
		this.apList = apList;
	}
	public List<ExPdApplyDetail> getDtList() {
		return dtList;
	}
	public void setDtList(List<ExPdApplyDetail> dtList) {
		this.dtList = dtList;
	}
	public List<SynExListInfoHandler> getUpdateList() {
		return updateList;
	}
	public void setUpdateList(List<SynExListInfoHandler> updateList) {
		this.updateList = updateList;
	}
	public List<PdApplyVo> getShowList() {
		return showList;
	}
	public void setShowList(List<PdApplyVo> showList) {
		this.showList = showList;
	}
	
	
}
