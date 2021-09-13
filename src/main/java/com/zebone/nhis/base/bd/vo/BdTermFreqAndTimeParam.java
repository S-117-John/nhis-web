package com.zebone.nhis.base.bd.vo;

import java.util.ArrayList;
import java.util.List;

import com.zebone.nhis.common.module.base.bd.mk.BdTermFreq;
import com.zebone.nhis.common.module.base.bd.mk.BdTermFreqTime;

/**
 * 
 * 医嘱频次以及医嘱频次时刻
 * @author wangpeng
 * @date 2016年8月29日
 *
 */
public class BdTermFreqAndTimeParam {
	
	/** 医嘱频次 */
	private BdTermFreq freq = new BdTermFreq(); 
	
	/** 医嘱频次 */
	private List<BdTermFreqTime>  timeList = new ArrayList<BdTermFreqTime>();

	public BdTermFreq getFreq() {
		return freq;
	}

	public void setFreq(BdTermFreq freq) {
		this.freq = freq;
	}

	public List<BdTermFreqTime> getTimeList() {
		return timeList;
	}

	public void setTimeList(List<BdTermFreqTime> timeList) {
		this.timeList = timeList;
	}

}
