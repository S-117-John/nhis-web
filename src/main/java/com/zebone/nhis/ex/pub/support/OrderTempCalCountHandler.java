package com.zebone.nhis.ex.pub.support;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.zebone.nhis.ex.pub.vo.OrderAppExecVo;
import com.zebone.nhis.ex.pub.vo.OrderExecVo;

/**
 * 临时医嘱（执行一次的临时医嘱）计算数量
 * @author yangxue
 *
 */
public class OrderTempCalCountHandler {
	public OrderAppExecVo exCal(double quan,Date date_start,Date end_date){
		OrderAppExecVo exceVO = new OrderAppExecVo();
		List<OrderExecVo> execList = new ArrayList<OrderExecVo>();
		OrderExecVo vo = new OrderExecVo();
		vo.setExceTime(date_start);
		vo.setQuanCur(quan);
		execList.add(vo);

		exceVO.setQuanTotal(quan);
		exceVO.setExceList(execList);
		exceVO.setFreq(false);
		exceVO.setDateEnd(end_date);
		exceVO.setCount(1);
		return exceVO;
	}
}
