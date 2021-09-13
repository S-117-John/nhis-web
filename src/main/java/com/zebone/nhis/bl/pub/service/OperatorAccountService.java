package com.zebone.nhis.bl.pub.service;

import com.zebone.nhis.bl.pub.vo.BlCcDt;
import com.zebone.nhis.bl.pub.vo.OpBlCcVo;

/**
 * 住院日结账个性化服务
 * @author Administrator
 *
 */
public interface OperatorAccountService {
	
	/**
    * 获取住院日结账报表显示信息
    * @param pkSettle
    * @return
    */
	public BlCcDt getIpOperAccInfo(BlCcDt param);
	
	/**
    * 获取门诊日结账报表显示信息
    * @param pkSettle
    * @return
    */
	public OpBlCcVo getOpOperAccInfo(OpBlCcVo param);
	
	
}
