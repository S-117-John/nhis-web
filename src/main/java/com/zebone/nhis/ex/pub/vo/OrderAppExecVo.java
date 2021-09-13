package com.zebone.nhis.ex.pub.vo;

import java.util.Date;
import java.util.List;

import com.zebone.nhis.ex.nis.ns.vo.OrderExecPdVo;
/**
 * 单条医嘱，根据执行时间区间对应的执行信息集合
 * @author yangxue
 *
 */
public class OrderAppExecVo {
	private double count;					//执行次数
	private List<OrderExecVo> exceList;	//执行单次信息
	private String codeOrdtype;				//医嘱字典类型编码
	private boolean isFreq;					//是否是非变动医嘱
	private double quanTotal;			//总用量当前单位
	private String message;					//提示信息，  扩展字段现在没有用
	private OrderExecPdVo pdVO;				//使用的物品信息
	private Date dateEnd;			//请领结束日期时间，小时频次记录最后执行时间
	private String euCycle;				//频次周期类型
	public double getCount() {
		return count;
	}
	public void setCount(double count) {
		this.count = count;
	}
	public List<OrderExecVo> getExceList() {
		return exceList;
	}
	public void setExceList(List<OrderExecVo> exceList) {
		this.exceList = exceList;
	}
	public String getCodeOrdtype() {
		return codeOrdtype;
	}
	public void setCodeOrdtype(String codeOrdtype) {
		this.codeOrdtype = codeOrdtype;
	}
	public boolean isFreq() {
		return isFreq;
	}
	public void setFreq(boolean isFreq) {
		this.isFreq = isFreq;
	}
	public double getQuanTotal() {
		return quanTotal;
	}
	public void setQuanTotal(double quanTotal) {
		this.quanTotal = quanTotal;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public OrderExecPdVo getPdVO() {
		return pdVO;
	}
	public void setPdVO(OrderExecPdVo pdVO) {
		this.pdVO = pdVO;
	}
	public Date getDateEnd() {
		return dateEnd;
	}
	public void setDateEnd(Date dateEnd) {
		this.dateEnd = dateEnd;
	}
	public String getEuCycle() {
		return euCycle;
	}
	public void setEuCycle(String euCycle) {
		this.euCycle = euCycle;
	}

}
