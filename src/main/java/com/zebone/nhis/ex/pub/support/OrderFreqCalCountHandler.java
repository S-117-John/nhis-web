package com.zebone.nhis.ex.pub.support;

import com.zebone.nhis.common.module.base.bd.mk.BdTermFreq;
import com.zebone.nhis.common.module.base.bd.mk.BdTermFreqTime;
import com.zebone.nhis.ex.pub.support.freq.*;
import com.zebone.nhis.ex.pub.vo.OrderAppExecVo;
import com.zebone.platform.modules.dao.DataBaseHelper;
import com.zebone.platform.modules.exception.BusException;

import java.util.Date;
import java.util.List;
/**
 * 非变频医嘱根据时间区间计算执行数量
 * @author yangxue
 *
 */
public class OrderFreqCalCountHandler {
	private BdTermFreq freqVO;
	private CalFreqHandler freqHandler;
	/**
	 * 计算长期非变动医嘱的执行数量
	 * @param codeOrdtype
	 * @param code_freq
	 * @param begin_time
	 * @param end_time
	 * @param quan --单次执行数量
	 * @param isExce
	 * @return
	 * @throws BusException
	 */
	public OrderAppExecVo calCount(String codeOrdtype,String code_freq, Date begin_time,
			Date end_time, Double quan, boolean isExce) throws BusException {
		//根据医嘱频次编码获取频次信息
		if(code_freq == null || code_freq == ""){	//如果前台传递过来的参数为空，附一个默认值给频次
			code_freq = "always";
		}
		BdTermFreq freqVO = DataBaseHelper.queryForBean("select * from bd_term_freq where code = ? and del_flag='0'", BdTermFreq.class, code_freq);
		
		if (freqVO == null) {
			throw new BusException("没有得到对应的频次信息! 频次:"+code_freq);
		}
		//根据频次主键，获取频次执行时刻
		List<BdTermFreqTime> freqTimeVOList = DataBaseHelper.queryForList("select * from bd_term_freq_time where pk_freq = ? and del_flag='0'", BdTermFreqTime.class, freqVO.getPkFreq());
		return exceCal(codeOrdtype,begin_time, end_time,freqVO, freqTimeVOList,quan,isExce);
	}
	/**
	 * 获得频次执行次数
	 * 
	 * @param beginDate 	开始时间
	 * @param endDate		结束时间
	 * @param freqVO		频次主表
	 * @param freqTimeList	频次时刻表
	 * 
	 * @return 				执行次数明细
	 */
	public OrderAppExecVo exceCal(String codeOrdtype,Date beginDate, Date endDate,
			BdTermFreq freqVO, List<BdTermFreqTime> freqTimeList,double quan,boolean isExce)
			throws BusException {
		this.freqVO = freqVO;
		OrderAppExecVo vo =  getFreqHandler().getQuanFreqTime(codeOrdtype, beginDate, endDate, freqVO, freqTimeList, quan, isExce);
		vo.setEuCycle(freqVO.getEuCycle());
		return vo;
	}
	
	public CalFreqHandler getFreqHandler() {
		//频次vo为空，视为临时
		if(null == freqVO){
			freqHandler = new CalFreqTempHandler();
			return freqHandler;
		}
		
		//根据频次类型实例化具体操作
		//0按天执行 1按周执行  2按小时执行 3按分钟执行
		String eu_cycle = freqVO.getEuCycle();		//周期执行类型  
		if ("0".equals(eu_cycle)) {
			//周期为天计算方法
			freqHandler = new CalFreqDayHandler();
		}else if ("2".equals(eu_cycle)) {
			//周期为小时
			freqHandler = new CalFreqHourHandler();
		}else if ("1".equals(eu_cycle)) {
			//周期为周
			freqHandler = new CalFreqWeekHandler();
		}else if("1".equals(freqVO.getEuAlways())){
			//临时
			freqHandler = new CalFreqTempHandler();
		}
		return freqHandler;
	}

	public void setFreqHandler(CalFreqHandler freqHandler) {
		this.freqHandler = freqHandler;
	}
}
