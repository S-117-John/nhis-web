package com.zebone.nhis.ex.pub.support;

import java.util.Date;

import org.springframework.stereotype.Service;

import com.zebone.nhis.ex.pub.vo.GenerateExLisOrdVo;
import com.zebone.nhis.ex.pub.vo.OrderAppExecVo;
import com.zebone.platform.modules.exception.BusException;
/**
 * 
 * @author yangxue
 *
 */
@Service
public class CalOrderPdQuanService {
	/**
     * 根据医嘱生成执行单时间区间，医嘱类型计算医嘱执行数量
     * @param begin_date：执行开始时间（YYYYMMDDHHMMSS）//处方和临时医嘱可以不传
     * @param end_date执行结束时间（YYYYMMDDHHMMSS）//处方和临时医嘱传当前时间
     * @param ordVO 医嘱对象
     * @return
     * @throws BusException
     */
	public OrderAppExecVo calOrdQuan(Date begin_date,Date end_date, GenerateExLisOrdVo ordVO) throws BusException{
	    if(ordVO == null) throw new BusException("未获取到需要计算执行数量的医嘱");
	    if(end_date == null) throw new BusException("未获取到执行结束时间！");
		return new OrderCalPdQuanHandler().calOrdQuan(begin_date,end_date, ordVO);
	}
}
