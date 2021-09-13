package com.zebone.nhis.ex.pub.support.freq;

import java.text.ParseException;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.zebone.nhis.common.module.base.bd.mk.BdTermFreq;
import com.zebone.nhis.common.module.base.bd.mk.BdTermFreqTime;
import com.zebone.nhis.common.support.CommonUtils;
import com.zebone.nhis.common.support.DateUtils;
import com.zebone.nhis.ex.pub.vo.OrderAppExecVo;
import com.zebone.nhis.ex.pub.vo.OrderExecVo;
import com.zebone.platform.modules.exception.BusException;

public abstract class CalFreqHandler {

	/**
	 * 计算执行次数和执行明细
	 * @param codeOrdtype
	 * @param beginDate
	 * @param endDate
	 * @param freqVO
	 * @param freqTimeVO
	 * @param quan
	 * @param isExce
	 * @return
	 * @throws BusException
	 */
	abstract public OrderAppExecVo getQuanFreqTime(String codeOrdtype, Date beginDate, Date endDate,
			BdTermFreq freqVO, List<BdTermFreqTime> freqTimeVO,double quan,boolean isExce) throws BusException ;
	
	protected int addEcxeVO(List<OrderExecVo> list, Date exceTime,double quan,int useCount) {
		OrderExecVo exceVO = new OrderExecVo();
		exceVO.setExceTime(exceTime);
		exceVO.setQuanCur(quan);
		list.add(exceVO);
		return useCount + 1;
	}
	
	/**
	 * 设置结束时间
	 * @param endDate
	 * @param exceVO
	 * @param list
	 * @param map
	 * @throws ParseException 
	 */
	protected void setDate_end(Date endDate, OrderAppExecVo exceVO,
			List<OrderExecVo> list,int unitct) throws ParseException {
		if(list == null || list.size() == 0){
			exceVO.setDateEnd(endDate);
			return;
		}
		Date lastDateTime = getLastExecTime(list);
		if(null == lastDateTime){
			exceVO.setDateEnd(endDate);
			return;
		}
		if(unitct > 1){
			exceVO.setDateEnd(DateUtils.getDefaultDateFormat().parse(DateUtils.getDefaultDateFormat().format(lastDateTime).substring(0, 8)+"235959"));
			return;
		}
		exceVO.setDateEnd(lastDateTime.after(endDate)?lastDateTime:endDate);
	}

	
	/**
	 * 获取最后执行时间
	 * @param list
	 * @return
	 */
	protected Date getLastExecTime(List<OrderExecVo> list){
		Date date = null;
		if(null == list || list.size() == 0){
			return date;
		}
		for(OrderExecVo vo : list){
			Date time = vo.getExceTime();
			if(null == date){
				date = vo.getExceTime();
			}else{
				date = date.after(time)?date:time;
			}
		}
		return date;
	}
	
	/**
	 * 获取固定分钟前的时间
	 * @param datetime
	 * @param min
	 * @return
	 * @throws ParseException 
	 */
	protected Date getUFDateTimeBeforeMi(Date datetime,Integer min) throws ParseException{
		if(null == min){
			return datetime;
		}
		Integer min_b = DateUtils.getMin(datetime);
		Integer h_b = DateUtils.getHour(datetime);
		if(min_b >= min){
			min_b = min_b - min;
		}else{
			min_b = min_b + 60 - min;
			if(h_b == 0){
				h_b = 23;
				datetime = DateUtils.getSpecifiedDay(datetime, -1);
			}else{
				h_b = h_b - 1;
			}
		}
		String time = (h_b>9?h_b.toString():"0"+h_b)+":"+(min_b>9?min_b.toString():"0"+min_b)+":"+DateUtils.getDefaultDateFormat().format(datetime).substring(12);
		return DateUtils.getDefaultDateFormat().parse(DateUtils.getDefaultDateFormat().format(datetime).substring(0,8)+time);
	}
	
	/**
	 * 获取固定分钟前的时间
	 * @param datetime
	 * @param min
	 * @return
	 * @throws ParseException 
	 */
	protected Date getUFDateTimeAfterMi(Date datetime,Integer min) throws ParseException{
		if(null == min){
			return datetime;
		}
		Integer min_be = DateUtils.getMin(datetime)+min;
		Integer h_be = DateUtils.getHour(datetime);
		if(min_be >= 60){
			min_be = min_be - 60;
			h_be = h_be + 1;
			if(h_be >= 24){
				h_be = h_be - 24;
				datetime = DateUtils.getSpecifiedDay(datetime, 1);
			}
		}
		String time = (h_be>9?h_be.toString():"0"+h_be)+":"+(min_be>9?min_be.toString():"0"+min_be)+":"+DateUtils.getDefaultDateFormat().format(datetime).substring(12);
		return DateUtils.getDefaultDateFormat().parse(DateUtils.getDefaultDateFormat().format(time).substring(0, 8)+time);
	}
	
	/**
	 * 获取时间，精确到分钟
	 * @param time
	 * @return
	 * @throws ParseException 
	 */
	protected Date getTimeWithoutS(Date time) throws ParseException{
		return DateUtils.getDefaultDateFormat().parse(DateUtils.getDefaultDateFormat().format(time).substring(0, 12) + "00");
	}
	
	
}
