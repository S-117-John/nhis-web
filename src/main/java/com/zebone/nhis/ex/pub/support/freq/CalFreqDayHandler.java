package com.zebone.nhis.ex.pub.support.freq;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.zebone.nhis.pro.zsba.common.support.DateUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.zebone.nhis.common.module.base.bd.mk.BdTermFreq;
import com.zebone.nhis.common.module.base.bd.mk.BdTermFreqTime;
import com.zebone.nhis.common.support.CommonUtils;
import com.zebone.nhis.common.support.DateUtils;
import com.zebone.nhis.ex.nis.ns.support.OrderTypeUtil;
import com.zebone.nhis.ex.pub.vo.OrderAppExecVo;
import com.zebone.nhis.ex.pub.vo.OrderExecVo;
import com.zebone.platform.framework.security.ShiroDbRealm;
import com.zebone.platform.modules.exception.BusException;


public class CalFreqDayHandler extends CalFreqHandler{
	
	static Logger logger = LogManager.getLogger(ShiroDbRealm.class.getName());


	/**
	 * 计算频率次数-单位天
	 * @param beginDate 	开始时间
	 * @param endDate  		结算时间
	 * @param freqTimeList		频率周期次数
	 * @param freqVO 		频率VO
	 * @return 执行次数
	 *
	 */
	@Override
	public OrderAppExecVo getQuanFreqTime(String codeOrdtype, Date beginDate,
			Date endDate, BdTermFreq freqVO, List<BdTermFreqTime> freqTimeList,
			double quan, boolean isExce) throws BusException {
		//获取频次周期数
		int unitct = CommonUtils.getInteger(freqVO.getCntCycle());
		Integer useCount = 0;//执行次数
		OrderAppExecVo exceVO = new OrderAppExecVo();
		List<OrderExecVo> list = new ArrayList<OrderExecVo>();
		//开始时间小于结束时间
		if (endDate.after(beginDate)) {
			//计算数量
			//根据时间间隔计算数量
			if("3".equals(freqVO.getEuExtime())){
				List<BdTermFreqTime> newfreqTimeList = getFreqTimeByInterval(beginDate,endDate,freqVO,isExce,unitct);
				for(BdTermFreqTime vo : newfreqTimeList){
					String timeOcc = vo.getTimeOcc();
					Date exceTime = DateUtils.strToDate(timeOcc);
					if(exceTime.before(endDate)){
						useCount = addEcxeVO(list,exceTime,quan,useCount);
					}
				}
			}else{
				//根据子表执行时间计算数量
				for(BdTermFreqTime vo:freqTimeList){
					String time_exce = vo.getTimeOcc();
					//logger.info("频次执行时刻："+time_exce);
					//logger.info("医嘱开始时间："+DateUtils.getDefaultDateFormat().format(beginDate));
					Date exceTime = null;
					try {
						exceTime = getFirstExceTime(beginDate, time_exce,isExce,unitct);
					} catch (ParseException e) {
						throw new BusException("按天执行计算首次执行时间日期转换错误!");
					}
					useCount = setExceVO_D(list, exceTime, quan, useCount, endDate, unitct);
			}

		}


		}
		try {
			//按时间间隔执行，最后执行时刻写最后一次医嘱频次时刻时间
			if("3".equals(freqVO.getEuExtime())){
				if(list == null || list.size() == 0){
					exceVO.setDateEnd(endDate);
				}
				Date lastDateTime = getLastExecTime(list);
				if(null == lastDateTime){
					exceVO.setDateEnd(endDate);
				}
				exceVO.setDateEnd(lastDateTime);
			}else{
				setDate_end(endDate, exceVO, list, unitct);
			}

		} catch (ParseException e) {
			throw new BusException("按天执行计算执行结束时间日期转换错误!");
		}
		exceVO.setExceList(list);
		exceVO.setCount(new Double(useCount));
		return exceVO;
	}
	
	/**
	 * 获取第一次执行时间   周期天
	 * @param beginDate
	 * @param time_exce
	 * @return
	 * @throws ParseException 
	 */
	private Date getFirstExceTime(Date beginDate, String time_exce,boolean isExce,int unitct) throws ParseException{
		Date dateTime = null;
		if(!CommonUtils.isEmptyString(time_exce)){
			time_exce = time_exce.replaceAll(":", "");
		}
		//隔日执行
		if(unitct > 1){
			if(isExce){
				//已经执行了，取周期数后的时间
				return DateUtils.getDefaultDateFormat().parse(DateUtils.getSpecifiedDateStr(beginDate,unitct)+time_exce);
			}else{
				//未执行，取开始时间的执行时刻时间
				String date_str_t = DateUtils.getDateStr(new Date());//获取当前日期的日期部分，用来拼成符合yyyymmddhhmmss的时间进行时间的比较
				Date exec_time_t = DateUtils.getDefaultDateFormat().parse(date_str_t+time_exce);
				Date begin_time_t = DateUtils.getDefaultDateFormat().parse(date_str_t+DateUtils.getTimeStr(beginDate));
				if(exec_time_t.after(begin_time_t)||exec_time_t.equals(begin_time_t)){//执行时间在开始时间之后
					return DateUtils.getDefaultDateFormat().parse(DateUtils.getDateStr(beginDate)+time_exce.toString());
				}else{
					return DateUtils.getDefaultDateFormat().parse(DateUtils.getSpecifiedDateStr(beginDate,unitct)+time_exce);
				}
			}
		}
		String date_str = DateUtils.getDateStr(new Date());//获取当前日期的日期部分，用来拼成符合yyyymmddhhmmss的时间进行时间的比较
		Date exec_time = DateUtils.getDefaultDateFormat().parse(date_str+time_exce);
		Date begin_time = DateUtils.getDefaultDateFormat().parse(date_str+DateUtils.getTimeStr(beginDate));
		//非隔日执行
		if (exec_time.after(begin_time)) 
			// 执行时间在开始时间之后
			dateTime = DateUtils.getDefaultDateFormat().parse(DateUtils.getDateStr(beginDate) + time_exce);
		else if(exec_time.equals(begin_time))
			// 执行时间与开始时间相同
			dateTime = beginDate;
		else
			// 执行时间在开始时间之前
			dateTime = DateUtils.getDefaultDateFormat().parse(DateUtils.getSpecifiedDateStr(beginDate,1)+time_exce);
		return dateTime;
	}
	
	/**
	 * 周期为日，根据执行时刻计算递归算法
	 * @param list
	 * @param exceTime
	 * @param quan
	 * @param useCount
	 * @param endDate
	 * @param unitct
	 * @return
	 */
	private int setExceVO_D (List<OrderExecVo> list, Date exceTime,Double quan,int useCount,Date endDate,int unitct){
		if(exceTime.before(endDate)){
			useCount = addEcxeVO(list,exceTime,quan,useCount);
			useCount = setExceVO_D(list,DateUtils.getSpecifiedDay(exceTime,unitct),quan,useCount,endDate,unitct);
		}
		return useCount;
	}

	/**
	 * 根据设定的时间间隔，计算频次时刻
	 * @return
	 */
	private  List<BdTermFreqTime> getFreqTimeByInterval(Date beginDate, Date endDate,BdTermFreq freqVO,boolean isExce,int unitct){
            if(freqVO==null||freqVO.getCntInterval()==null||freqVO.getCntInterval().intValue()<=0)
            	throw new BusException("按时间间隔计算执行时刻时，医嘱频次维护数据不正确，请校对后重新生成！");
		List<BdTermFreqTime> timelist = new ArrayList<BdTermFreqTime>();
		//计算开始时间与结束时间相差小时数
		Date beginTime = null;
		try {
			beginTime = getFirstExceTime(beginDate,DateUtils.getTimeStr(beginDate),isExce,unitct);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		int hours = DateUtils.getHoursBetween(beginTime,endDate);
		//剩余最大执行次数
		int cnt = hours/freqVO.getCntInterval().intValue();
		for(int i=0;i<=cnt;i++){
			//执行过的，跳过首次，避免与上一次生成重复
			if(isExce&&i==0)
				continue;
			BdTermFreqTime freqTime = new BdTermFreqTime();
			freqTime.setSortNo(i+1L);
			String timeOcc = DateUtils.addDate(beginDate,i*freqVO.getCntInterval().intValue(),4,"yyyyMMddHHmmss");
			freqTime.setTimeOcc(timeOcc);
			freqTime.setWeekNo(1L);
			timelist.add(freqTime);
		}
		return timelist;
	}
	
}
