package com.zebone.nhis.emr.rec.dict.vo;

import java.util.ArrayList;
import java.util.List;

import com.zebone.nhis.common.module.emr.rec.dict.EmrDictRange;
import com.zebone.nhis.common.module.emr.rec.dict.EmrDictRangeCode;

/**
 * 保存病历值域vo
 * @author chengjia
 *
 */
public class DictRangePrarm {
	
	private EmrDictRange range=new EmrDictRange();
	
	private List<EmrDictRangeCode> codeList=new ArrayList<EmrDictRangeCode>();

	public EmrDictRange getRange() {
		return range;
	}

	public void setRange(EmrDictRange range) {
		this.range = range;
	}

	public List<EmrDictRangeCode> getCodeList() {
		return codeList;
	}

	public void setCodeList(List<EmrDictRangeCode> codeList) {
		this.codeList = codeList;
	}
	
	
}
