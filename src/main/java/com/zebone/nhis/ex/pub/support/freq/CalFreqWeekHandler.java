package com.zebone.nhis.ex.pub.support.freq;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.zebone.nhis.common.module.base.bd.mk.BdTermFreq;
import com.zebone.nhis.common.module.base.bd.mk.BdTermFreqTime;
import com.zebone.nhis.common.support.CommonUtils;
import com.zebone.nhis.common.support.DateUtils;
import com.zebone.nhis.ex.pub.vo.OrderAppExecVo;
import com.zebone.nhis.ex.pub.vo.OrderExecVo;
import com.zebone.platform.modules.exception.BusException;

/**
 * 频次周期单位为周的算法
 * @author yangxue
 *
 */
public class CalFreqWeekHandler extends CalFreqHandler{
	@Override
	public OrderAppExecVo getQuanFreqTime(String codeOrdtype, Date beginDate,
			Date endDate, BdTermFreq freqVO, List<BdTermFreqTime> freqTimeVOList,
			double quan, boolean isExce) throws BusException {
		Integer useCount = 0;
		OrderAppExecVo exceVO = new OrderAppExecVo();
		int unitct = CommonUtils.getInteger(freqVO.getCntCycle());//频次周期数
		List<OrderExecVo> list = new ArrayList<OrderExecVo>();
		// 开始时间是周几，称为周号
		int weekNo_B = DateUtils.getDayNumOfWeek(beginDate);
		if (endDate.after(beginDate)) {
			for(BdTermFreqTime vo:freqTimeVOList){
				String time_exce = vo.getTimeOcc();
				Integer weekNo = CommonUtils.getInteger(vo.getWeekNo());
				if(null == weekNo){
					throw new BusException("周期频次缺少必要条件：周模型，缺少星期日期定义！");
				}
				Date firstTime = null;
				try {
					firstTime = getFristExceTime_W(beginDate, weekNo_B,
							weekNo, time_exce);
				} catch (ParseException e) {
					throw new BusException("计算按周执行开始时间异常！");
				}
				useCount = setExceVO_W(list, firstTime, quan, useCount, endDate, unitct);
			}
		}
		try {
			setDate_end(endDate, exceVO, list, unitct);
		} catch (ParseException e) {
			throw new BusException("计算按周执行结束时间异常！");
		}
		exceVO.setExceList(list);
		exceVO.setCount(new Double(useCount));
		return exceVO;
	}
	
	/**
	 * 获取第一次执行时间
	 * @param beginDate		开始时间
	 * @param weekNo_B		开始日期的星期数
	 * @param weekno		执行的星期数
	 * @param time_exce		执行时刻
	 * @return
	 * @throws ParseException 
	 */
	private Date getFristExceTime_W(Date beginDate, int weekNo_B, int weekno, String time_exce) throws ParseException{
		Date dateTime = new Date();
		String time = time_exce.replaceAll(":", "");
		Date exec_time = DateUtils.getDefaultDateFormat().parse(DateUtils.getDateStr(dateTime)+time);
		Date begin_time = DateUtils.getDefaultDateFormat().parse(DateUtils.getDateStr(dateTime)+DateUtils.getTimeStr(beginDate));
		int days = 0;
		if (weekno > weekNo_B) {
			days = weekno - weekNo_B;
		}else if(weekno < weekNo_B){
			days = 7 - weekNo_B + weekno;
		}else if(!exec_time.before(begin_time)){
			days = 0;
		}else{
			days = 7;
		}
		dateTime = DateUtils.getDefaultDateFormat().parse(DateUtils.getSpecifiedDateStr(beginDate, days)+time);
		return dateTime;
	}
	
	/**
	 * 累加执行次数
	 * @param list			执行明细列表
	 * @param exceTime		执行时间
	 * @param quan			执行数量
	 * @param useCount		当前次数
	 * @param endDate		结束时间
	 * @param unitct		周期间隔数
	 * @return
	 */
	private int setExceVO_W (List<OrderExecVo> list, Date exceTime,double quan,int useCount,Date endDate,int unitct){
		if(!exceTime.after(endDate)){
			useCount = addEcxeVO( list,  exceTime, quan, useCount);
			useCount =setExceVO_W(list, DateUtils.getSpecifiedDay(exceTime, unitct*7), quan, useCount,endDate,unitct);
		}
		return useCount;
	}

	
}
