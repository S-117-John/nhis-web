package com.zebone.nhis.ex.pub.support.freq;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.zebone.nhis.common.module.base.bd.mk.BdTermFreq;
import com.zebone.nhis.common.module.base.bd.mk.BdTermFreqTime;
import com.zebone.nhis.ex.pub.vo.OrderAppExecVo;
import com.zebone.nhis.ex.pub.vo.OrderExecVo;
import com.zebone.platform.modules.exception.BusException;

public class CalFreqTempHandler extends CalFreqHandler{

	@Override
	public OrderAppExecVo getQuanFreqTime(String codeOrdtype, Date beginDate,
			Date endDate, BdTermFreq freqVO, List<BdTermFreqTime> freqTimeVO,
			double quan, boolean isExce) throws BusException {
		OrderAppExecVo exceVO = new OrderAppExecVo();
		List<OrderExecVo> execList = new ArrayList<OrderExecVo>();
		OrderExecVo vo = new OrderExecVo();
		vo.setExceTime(new Date());
		vo.setQuanCur(quan);
		execList.add(vo);

		//exceVO.setQuan_med(quan);
		exceVO.setExceList(execList);
		exceVO.setFreq(false);
		exceVO.setDateEnd(endDate);
		exceVO.setCount(1);
		return exceVO;
	}

	

}
