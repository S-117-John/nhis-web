package com.zebone.nhis.ex.pub.support;

import com.zebone.nhis.common.module.base.bd.mk.BdTermFreq;
import com.zebone.nhis.common.support.DateUtils;
import com.zebone.nhis.ex.pub.vo.OrderAppExecVo;
import com.zebone.platform.modules.dao.DataBaseHelper;
import com.zebone.platform.modules.exception.BusException;

import java.text.ParseException;
import java.util.Date;

/**
 * 执行多次，但执行单只生成一次的临时医嘱
 * @author yangxue
 *
 */
public class OrderOnceCalCountHandler {
	private OrderFreqCalCountHandler freqCalHandler;
	/**
	 * 调用按频次生成的接口，但截至时间生成到开始时间的24小时内
	 * @param codeOrdtype
	 * @param code_freq
	 * @param begin_time
	 * @param end_time
	 * @param quan
	 * @param isExce
	 * @param exceVO
	 * @return
	 * @throws BusException
	 */
	public OrderAppExecVo calCount(String codeOrdtype,String code_freq, Date date_start,Date begin_time,
			Date end_time, Double quan, boolean isExce,OrderAppExecVo exceVO) throws BusException {
		//根据医嘱频次编码获取频次信息
		BdTermFreq freqVO = DataBaseHelper.queryForBean("select eu_always,pk_freq from bd_term_freq where code = ? and del_flag='0'", BdTermFreq.class, code_freq);
		if (freqVO == null) {
			throw new BusException("没有得到对应的频次信息! 频次："+code_freq);
		}
		if("1".equals(freqVO.getEuAlways())){//临时的频次，只生成一次
			exceVO = new OrderTempCalCountHandler().exCal(quan,date_start,end_time);
			return exceVO;
		}else{//按长期的频次生成，其中开始时间为开立当天的0点，结束时间为开立当天的23点
			String start_date = DateUtils.getDateStr(date_start);//
			//查询执行时刻
			//杨雪注释，博爱需求与中山二院的首日次数需求发送冲突，因此注释
//			List<BdTermFreqTime> freqTimeVOList = DataBaseHelper.queryForList("select * from bd_term_freq_time where pk_freq = ? and del_flag='0' order by sort_no ", BdTermFreqTime.class, freqVO.getPkFreq());
//			boolean flagAdd = false;
//			OrderAppExecVo exceVO_temp = null;
//			if(freqTimeVOList == null ||freqTimeVOList.size()<=0){
//				flagAdd = true;
//			}else{
//				String timeOcc = freqTimeVOList.get(0).getTimeOcc();
//				try {
//					Date firstTime = DateUtils.getDefaultDateFormat().parse(begin_date+timeOcc.replaceAll(":", ""));
//					if(begin_time.after(firstTime)){
//						flagAdd = true;
//					}else{
//						flagAdd = false;
//					}
//				} catch (ParseException e) {
//					e.printStackTrace();
//				}
//				
//			}
			//默认添加一次执行记录
//			if(flagAdd)	
//				exceVO_temp = new OrderTempCalCountHandler().exCal(quan, date_start,end_time);
			
			//剩余执行单按长期医嘱频次继续添加
			freqCalHandler = new OrderFreqCalCountHandler();
			//重新设置结束时间为当天235959
			try {
				Date begin_time_long = DateUtils.getDefaultDateFormat().parse(start_date+"000000");
				Date end_time_long = DateUtils.getDefaultDateFormat().parse(start_date+"235959");
				exceVO = freqCalHandler.calCount(codeOrdtype,code_freq,begin_time_long,end_time_long,quan,isExce);
			} catch (ParseException e) {
				e.printStackTrace();
			}
			
//			if(exceVO!=null&&exceVO.getExceList()!=null){
//				if(exceVO_temp!=null&&exceVO_temp.getExceList()!=null){
//				 exceVO.getExceList().addAll(exceVO_temp.getExceList());
//				 exceVO.setCount(exceVO.getCount()+exceVO_temp.getCount());
//				 exceVO.setQuanTotal(exceVO.getQuanTotal()+exceVO_temp.getQuanTotal());
//				}
//			}else{
//				if(exceVO_temp!=null){
//					exceVO = exceVO_temp;
//				}
//			}
			return exceVO;
		}
		
	}
}
