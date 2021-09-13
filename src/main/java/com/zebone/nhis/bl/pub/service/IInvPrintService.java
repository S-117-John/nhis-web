package com.zebone.nhis.bl.pub.service;


import java.util.List;
import java.util.Map;

import com.zebone.nhis.bl.pub.vo.BlInvPrint;
import com.zebone.nhis.bl.pub.vo.InvoiceInfo;
import com.zebone.nhis.common.module.bl.BlSettle;

/**
 * 住院发票打印公共接口服务
 * @author yangxue
 *
 */
public interface IInvPrintService {
	   /**
	    * 根据结算信息获取需要打印的住院发票项目
	    * @param pkSettle
	    * @return
	    */
		public BlInvPrint getIpInvPrintDataByPkSettle(String pkSettle);
		 /**
		    * 根据结算信息获取需要打印的门诊发票项目
		    * @param pkSettle
		    * @return
		    */
		public BlInvPrint getOpInvPrintDataByPkSettle(String pkSettle);
		
		/**
		 * 处理保存门诊发票服务
		 * @param blSettle
		 * @param invoiceInfos
		 * @return
		 */
		public Map<String,Object> saveOpInvInfo(BlSettle blSettle,List<InvoiceInfo> invoiceInfos);
		
		/**
		 * 重打发票个性化服务
		 * @param param
		 * @return
		 */
		public Map<String,Object> invRePrint(Map<String,Object> param);
		
}
