package com.zebone.nhis.ex.pub.support;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.zebone.nhis.common.support.CommonUtils;
import com.zebone.nhis.common.support.DateUtils;
import com.zebone.nhis.ex.pub.vo.OrderAppExecVo;
import com.zebone.nhis.ex.pub.vo.OrderExecVo;
import com.zebone.platform.modules.dao.DataBaseHelper;
import com.zebone.platform.modules.exception.BusException;
/**
 * 变频医嘱根据时间区间计算执行数量
 * @author yangxue
 *
 */
public class OrderChCalCountHandler {

	
	@SuppressWarnings("unchecked")
	public OrderAppExecVo exceCal(Date begin_date, Date end_date,
			String pk_cnord) throws BusException, ParseException {
		
		//初始化执行VO
		OrderAppExecVo vo = new OrderAppExecVo();
		vo.setCount(0);
		vo.setQuanTotal(0);
		
		// 获取当前医嘱服务的执行信息
		String sql = "select pk_freqplan,time_ex,quan_occ " +
				" from cn_ord_freq_plan where pk_cnord = ?";
		List<Map<String, Object>> list = (List<Map<String, Object>>)DataBaseHelper.queryForMap(sql,new Object[]{pk_cnord});
		if (null == list || list.size() == 0)
			throw new BusException("没有定义相应的医嘱服务数量！");
		
		// 获取开始，结束的具体时间
		SimpleDateFormat sftime = new SimpleDateFormat("HHmmss");
        String begin_str = DateUtils.getDefaultDateFormat().format(begin_date);
        String begin_time_str = begin_str.substring(8,begin_str.length());//从第8个开始，
        Date begin_time = sftime.parse(begin_time_str);
		
		// 计算执行情况
			// 日循环
			for (Map<String, Object> quanMap : list) {
				String time_ex = quanMap.get("timeEx").toString();
				Date exce_time = sftime.parse(time_ex);
				Double quan = CommonUtils.getDouble(quanMap.get("quanOcc").toString());
				Date exceDate = null;
				//获取第一次执行时间
				if (exce_time.after(begin_time)) {//实际执行时刻晚于开始时间，使用开始时间的执行时刻
					exceDate = DateUtils.getDefaultDateFormat().parse(begin_str.substring(0, 8)+time_ex);
				} else {//实际执行时刻早于开始时间，使用明天的实际执行时刻
					//计算明天
					Date tomorrow = getDateTimeAfter(begin_date,1);
					exceDate =DateUtils.getDefaultDateFormat().parse(DateUtils.getDateStr(tomorrow)+time_ex);
				}
				//递归计算执行信息
				calExceIfno(vo, quan, exceDate, end_date);
			}
		return vo; 
	}
	
	/**
	 * 递归计算执行信息
	 * @param vo
	 * @param quan
	 * @param exceDate
	 * @param end_date
	 */
	private void calExceIfno(OrderAppExecVo vo, double quan, Date exceDate, Date end_date){
		//初始化exceVO
		OrderExecVo exceVO = new OrderExecVo();
		//递归体，计算数量
		if(exceDate.before(end_date)){
			exceVO.setExceTime(exceDate);
			exceVO.setQuanCur(quan);
			List<OrderExecVo> exceList = vo.getExceList();
			if(exceList == null){
				exceList = new ArrayList<OrderExecVo>();
				vo.setExceList(exceList);
			}
			vo.setCount(vo.getCount()+1);
			exceList.add(exceVO);
			vo.setQuanTotal(vo.getQuanTotal()+quan);
			calExceIfno(vo, quan,getDateTimeAfter(exceDate,1), end_date);
		}
	}
	/**
	 * 取几天后
	 * @param begin_date
	 * @param days
	 * @return
	 */
	private Date getDateTimeAfter(Date begin_date,int days){
		Date afterDate = new Date(begin_date.getTime()+86400000L*days);
		return afterDate;
	}
}
