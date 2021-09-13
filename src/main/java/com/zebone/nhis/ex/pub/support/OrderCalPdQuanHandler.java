package com.zebone.nhis.ex.pub.support;

import java.text.ParseException;
import java.util.Date;
import java.util.List;

import com.zebone.nhis.common.module.base.bd.mk.BdTermFreq;
import com.zebone.nhis.common.module.base.bd.mk.BdTermFreqTime;
import com.zebone.nhis.common.support.CommonUtils;
import com.zebone.nhis.common.support.IOrdTypeCodeConst;
import com.zebone.nhis.ex.pub.vo.GenerateExLisOrdVo;
import com.zebone.nhis.ex.pub.vo.OrderAppExecVo;
import com.zebone.nhis.ex.pub.vo.OrderExecVo;
import com.zebone.platform.modules.dao.DataBaseHelper;
import com.zebone.platform.modules.exception.BusException;
/**
 * 根据时间区间计算执行数量
 * @author yangxue
 *
 */
public class OrderCalPdQuanHandler {
    /**
     * 根据医嘱生成执行单时间区间，医嘱类型计算医嘱执行数量
     * @param begin_date：执行开始时间（YYYYMMDDHHMMSS）
     * @param end_date执行结束时间（YYYYMMDDHHMMSS）
     * @param ordVO 医嘱对象
     * @return
     * @throws BusException
     */
	public OrderAppExecVo calOrdQuan(Date begin_date,Date end_date, GenerateExLisOrdVo ordVO) throws BusException{
		
		OrderAppExecVo exceVO = new OrderAppExecVo();
		exceVO.setCodeOrdtype(ordVO.getCodeOrdtype());
		String eu_cycle = ordVO.getEuCycle();//频次周期单位
		boolean isExec = ordVO.getDateLastEx()==null?false:true;//是否执行过
		Double quan = getQuanCur(ordVO);//单次执行数量
		exceVO.setEuCycle(eu_cycle);//频次周期单位
		String freqtype = this.getFreqType(ordVO);
		//计算执行次数及明细
		if("04".equals(freqtype)){//处方
			exceVO = new OrderTempCalCountHandler().exCal(quan,ordVO.getDateStart(), end_date);
		}else if("06".equals(freqtype)){//变动医嘱
			//计算变动医嘱
			try {
				exceVO = new OrderChCalCountHandler().exceCal(begin_date, end_date,
						ordVO.getPkCnord());
			} catch (ParseException e) {
				throw new BusException("计算变动医嘱数量时，日期类型转换错误！");
			}
			exceVO.setFreq(false);
			exceVO.setDateEnd(end_date);
		}else if("05".equals(freqtype)){//草药医嘱
			// 当做执行一次，取单次数量。草药当作临时医嘱处理。
			exceVO = new OrderTempCalCountHandler().exCal(quan, ordVO.getDateStart(),end_date);
		}else if("07".equals(freqtype)){//临时医嘱-针对博爱医院这种每天三次也算临时医嘱的情况
			exceVO = new OrderOnceCalCountHandler().calCount(ordVO.getCodeOrdtype(),ordVO.getCodeFreq(),ordVO.getDateStart(),begin_date,end_date,quan,isExec,exceVO);
			exceVO.setQuanTotal(quan*exceVO.getCount());
			exceVO.setFreq(true);
		}else{
			//计算非变动医嘱
			exceVO = new OrderFreqCalCountHandler().calCount(ordVO.getCodeOrdtype(),ordVO.getCodeFreq(),begin_date,end_date,quan,isExec);
			exceVO.setQuanTotal(quan*exceVO.getCount());
			exceVO.setFreq(true);
		}
		
		addFristExec(ordVO, exceVO, isExec);
		return exceVO;
	}

	private double getQuanCur(GenerateExLisOrdVo ordVO) {
		return ordVO.getQuanOcc() == null ? 0 : ordVO.getQuanOcc();
	}
    /**
     * 获取频次类型
     * @return
     */
	private String getFreqType(GenerateExLisOrdVo ordVO){
		String freqtype = "";
		//处方的情况
		if(!CommonUtils.isEmptyString(ordVO.getPkPres())){
			if(!CommonUtils.isEmptyString(ordVO.getCodeOrdtype())&&ordVO.getCodeOrdtype().equals(IOrdTypeCodeConst.DT_ORDTYPE_DRUG_HERB)){
				freqtype="05";//草药按临时医嘱处理
			}else{
				freqtype="04";//处方
			}
		}else{//非处方的情况
			
			if("1".equals(ordVO.getFlagPlan())){
				freqtype = "06";//变频医嘱
			}else{
				if("1".equals(ordVO.getEuAlways())){
					freqtype="07";//临时医嘱
				}
			}
		}
		return freqtype;
	}
	
	/**
	 * 增加首次执行
	 * @param ordVO
	 * @param exceVO
	 * @param isExec
	 */
	private void addFristExec(GenerateExLisOrdVo ordVO, OrderAppExecVo exceVO,
			boolean isExec) {
		if(null != ordVO.getFlagFirst() && ordVO.getFlagFirst().equals("1") && !isExec){
			List<OrderExecVo> execList = exceVO.getExceList();
			if(null != execList && execList.size() > 0){
				OrderExecVo vo = new OrderExecVo();
				vo.setExceTime(ordVO.getDateStart());
				vo.setQuanCur(this.getQuanCur(ordVO));
				execList.add(vo);
			}
		}
	}
	/**
	 * 根据频次计算时间段内执行情况,增加是否执行参数,增加隔天频次计算方法
	 * @param codeOrdtype
	 * @param code_freq
	 * @param begin_time
	 * @param end_time
	 * @param quan
	 * @param isExce
	 * @return
	 * @throws BusinessException
	 */
	public OrderAppExecVo calCount(String codeOrdtype,String code_freq, Date begin_time,
			Date end_time, double quan, boolean isExce) throws BusException {
		
		BdTermFreq freqVO = DataBaseHelper.queryForBean("select * from bd_term_freq where code = ? and del_flag='0' ", BdTermFreq.class, code_freq);
		if(freqVO == null ){
			throw new BusException("没有查找到对应的频次信息");
		}
		List<BdTermFreqTime> freqTimeList = DataBaseHelper.queryForList("select * from bd_term_freq_time where pk_freq = ? and del_flag='0' ", BdTermFreqTime.class, freqVO.getPkFreq());
		return new OrderFreqCalCountHandler().exceCal(codeOrdtype,begin_time, end_time,
				freqVO, freqTimeList,quan,isExce);
	}
}
