package com.zebone.nhis.bl.pub.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.zebone.nhis.bl.pub.support.InvPrintProcessUtils;
import com.zebone.nhis.bl.pub.vo.InvoiceInfo;
import com.zebone.nhis.bl.pub.vo.OpCgTransforVo;
import com.zebone.nhis.common.module.bl.BlInvoice;
import com.zebone.nhis.common.module.bl.BlInvoiceDt;
import com.zebone.nhis.common.module.bl.BlSettle;
import com.zebone.nhis.common.module.bl.BlStInv;
import com.zebone.nhis.common.support.CommonUtils;
import com.zebone.platform.framework.support.IUser;
import com.zebone.platform.modules.dao.DataBaseHelper;
import com.zebone.platform.modules.exception.BusException;
import com.zebone.platform.modules.utils.JsonUtil;

/**
 * 电子票据开立接口（交易号）
 * @author zhangtao
 *
 */
@Service
public class BlInvEBillService {

	/**
	 * 交易号：007002003058
	 * 开立门诊结算电子票据
	 * @param param
	 * @param user
	 */
	public OpCgTransforVo eBillMzOutpatient(String param,IUser user){
		OpCgTransforVo opCgTransforVo = JsonUtil.readValue(param, OpCgTransforVo.class);
		if(opCgTransforVo == null) {
			throw new BusException("未传入参数信息，请检查！");
		}
		if(CommonUtils.isNull(opCgTransforVo.getPkSettle())) {
			throw new BusException("未传入结算主键pkSettle，请检查！");
		}
		if(CommonUtils.isNull(opCgTransforVo.getPkPv())) {
			throw new BusException("未传入患者就诊主键pkPv，请检查！");
		}
		if(CommonUtils.isNull(opCgTransforVo.getPkPi())) {
			throw new BusException("未传入患者主键pkPi，请检查！");
		}
		if(CommonUtils.isNull(opCgTransforVo.getMachineName())) {
			throw new BusException("未传入本地计算机名称machineName，请检查！");
		}
		List<InvoiceInfo> invoiceInfos = opCgTransforVo.getInvoiceInfo();
		if(invoiceInfos == null || invoiceInfos.size() == 0 && "1".equals(opCgTransforVo.getFlagPrint())) {
			throw new BusException("未传入发票信息invoiceInfo，请检查！");
		}
		if(CommonUtils.isNull(opCgTransforVo.getFlagPrint())) {
			opCgTransforVo.setFlagPrint("0");
		}
		BlSettle bs = new BlSettle();
		bs.setPkSettle(opCgTransforVo.getPkSettle());
		bs.setPkPv(opCgTransforVo.getPkPv());
		bs.setPkPi(opCgTransforVo.getPkPi());
		bs.setDtSttype("01");
		if(invoiceInfos == null || invoiceInfos.size() == 0){
			invoiceInfos = new ArrayList<>();
			InvoiceInfo invInfo = new InvoiceInfo();
			invInfo.setMachineName(opCgTransforVo.getMachineName());
			invInfo.setFlagPrint(opCgTransforVo.getFlagPrint());
			invoiceInfos.add(invInfo);
		}else{
			invoiceInfos.get(0).setMachineName(opCgTransforVo.getMachineName());
			invoiceInfos.get(0).setFlagPrint(opCgTransforVo.getFlagPrint());
		}

		Map<String,Object> invMap = InvPrintProcessUtils.saveOpInvInfo(bs, invoiceInfos);
		if(invMap==null || invMap.size()<=0){
			throw new BusException("收费结算时，发票信息组织失败，请检查！");
		}
		List<BlInvoice> blInvoices = (List<BlInvoice>)invMap.get("inv");
		List<BlInvoiceDt> blInvoiceDts = (List<BlInvoiceDt>)invMap.get("invDt");
		List<BlStInv> blStInvs = (List<BlStInv>)invMap.get("stInv");
		
		if(blInvoices!=null && blInvoices.size() > 0){
			DataBaseHelper.batchUpdate(DataBaseHelper.getInsertSql(BlInvoice.class), blInvoices); // 批量插入发票
		}
		if(blInvoiceDts!=null && blInvoiceDts.size() > 0){
			DataBaseHelper.batchUpdate(DataBaseHelper.getInsertSql(BlInvoiceDt.class), blInvoiceDts); // 批量插入发票明细
		}
		if(blStInvs!=null && blStInvs.size() > 0){
			DataBaseHelper.batchUpdate(DataBaseHelper.getInsertSql(BlStInv.class), blStInvs); // 批量写发票与结算的关系
		}
		opCgTransforVo.setBlInvoiceDts(blInvoiceDts);
		opCgTransforVo.setBlInvoices(blInvoices);
		return opCgTransforVo;
	}
	
}
