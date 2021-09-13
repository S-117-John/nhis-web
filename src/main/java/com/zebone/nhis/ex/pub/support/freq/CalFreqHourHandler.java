package com.zebone.nhis.ex.pub.support.freq;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.zebone.nhis.common.module.base.bd.mk.BdTermFreq;
import com.zebone.nhis.common.module.base.bd.mk.BdTermFreqTime;
import com.zebone.nhis.common.support.CommonUtils;
import com.zebone.nhis.common.support.DateUtils;
import com.zebone.nhis.ex.pub.vo.OrderAppExecVo;
import com.zebone.nhis.ex.pub.vo.OrderExecVo;
import com.zebone.platform.modules.exception.BusException;

public class CalFreqHourHandler extends CalFreqHandler {

	/**
	 * 计算频率次数-单位小时
	 * 
	 * @param beginDate  开始时间
	 * @param endDate   结算时间
	 * @param unitct  频率周期次数
	 * @param freqexVO 频率VO
	 * @return 执行次数
	 */
	@Override
	public OrderAppExecVo getQuanFreqTime(String codeOrdtype, Date beginDate,
			Date endDate, BdTermFreq freqVO, List<BdTermFreqTime> freqTimeVO,
			double quan, boolean isExce) throws BusException {
		int unitct = CommonUtils.getInteger(freqVO.getCntCycle());
		Integer useCount = 0;//执行总次数
		double result = 0;
		OrderAppExecVo exceVO = new OrderAppExecVo();
		List<OrderExecVo> list = new ArrayList<OrderExecVo>();
		if (endDate.after(beginDate)) {
			// 计算开始时间距结束时间一共有多少小时数
			int hourQuan = DateUtils.getHoursBetween(beginDate, endDate);
			// 小时数除以周期次数等于在此段时间内，当前执行时刻应该执行的次数
			int count = hourQuan / unitct;
			//如果是药品记录每次执行时间和数量
			try {
				calExecTime(beginDate, unitct, quan, useCount, list,count);
			} catch (ParseException e) {
				throw new BusException("按小时执行时计算每次执行时间和数量日期转换异常！");
			}
			exceVO.setDateEnd(list.get(list.size()-1).getExceTime());
			result = result + list.size();
			
		}
		exceVO.setExceList(list);
		exceVO.setCount(result);
		
		return exceVO;
	}

	private void calExecTime(Date beginDate, Integer unitct,
			Double quan, Integer useCount, List<OrderExecVo> list, int count) throws ParseException {
		Date nextTime = this.getNextExceTime(beginDate, unitct);
		if(useCount < count){//总执行次数小于两小时之间的周期次数
			addExecVO(list, nextTime, quan);
			calExecTime( nextTime,  unitct, quan, useCount+1,  list,  count);
		}else if(useCount == count){
			addExecVO(list, nextTime, quan);
		}
		return;
	}
	
	private Date getNextExceTime(Date dateTime,int unitct) throws ParseException{
		//取小时部分
		int h = DateUtils.getHour(dateTime);
		h += unitct;
		String time = DateUtils.getTimeStr(dateTime);
		if(h >= 24){
			dateTime = DateUtils.getSpecifiedDay(dateTime, 1);
			h -= 24;
		}
		String hour = h > 9 ? ""+h : "0" + h;
		return DateUtils.getDefaultDateFormat().parse(DateUtils.getDateStr(dateTime)+hour+time.substring(1));
	}
	
	private void addExecVO(List<OrderExecVo> list, Date exceTime,Double quan) {
		OrderExecVo exceVO = new OrderExecVo();
		exceVO.setExceTime(exceTime);
		exceVO.setQuanCur(quan);
		list.add(exceVO);
	}

	
}
