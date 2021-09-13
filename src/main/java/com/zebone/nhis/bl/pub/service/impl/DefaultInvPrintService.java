package com.zebone.nhis.bl.pub.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.zebone.nhis.bl.pub.service.IInvPrintService;
import com.zebone.nhis.bl.pub.vo.BlInvPrint;
import com.zebone.nhis.bl.pub.vo.InvoiceInfo;
import com.zebone.nhis.common.module.bl.BlInvoice;
import com.zebone.nhis.common.module.bl.BlInvoiceDt;
import com.zebone.nhis.common.module.bl.BlSettle;
import com.zebone.nhis.common.module.bl.BlStInv;
import com.zebone.nhis.common.support.ApplicationUtils;
import com.zebone.nhis.common.support.CommonUtils;
import com.zebone.nhis.common.support.DateUtils;
import com.zebone.nhis.common.support.EnumerateParameter;
import com.zebone.platform.common.support.NHISUUID;
import com.zebone.platform.common.support.UserContext;
import com.zebone.platform.modules.exception.BusException;

/**
 * 默认住院发票打印服务
 * @author yangxue
 *
 */
public class DefaultInvPrintService implements IInvPrintService{

	@Override
	public BlInvPrint getIpInvPrintDataByPkSettle(String pkSettle) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public BlInvPrint getOpInvPrintDataByPkSettle(String pkSettle) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Map<String, Object> saveOpInvInfo(BlSettle blSettle,
			List<InvoiceInfo> invoiceInfos) {
		String pkOrg = UserContext.getUser().getPkOrg();
		String pkOpDoctor = UserContext.getUser().getPkEmp();
		String nameUser = UserContext.getUser().getNameEmp();
		
		List<BlInvoice> blInvoices = new ArrayList<>(); // 发票
		List<BlInvoiceDt> blInvoiceDts = new ArrayList<BlInvoiceDt>(); // 发票明细
		List<BlStInv> blStInvs = new ArrayList<BlStInv>(); // 写发票与结算的关系
		
		Map<String,Object> ret = new HashMap<String, Object>();
		
		for (InvoiceInfo invoiceInfo : invoiceInfos) {
			if(invoiceInfo.getBlInDts()==null||invoiceInfo.getBlInDts().size()<=0){
				throw new BusException("收费结算时，未传入发票号["+invoiceInfo.getCodeInv()+"]对应的发票明细！");
			}
			/**
			 * 插入发票
			 */
			BlInvoice bi = new BlInvoice();
			bi.setPkOrg(pkOrg);
			bi.setPkInvcate(invoiceInfo.getPkInvcate());// 票据分类主键
			bi.setCodeInv(invoiceInfo.getCodeInv());// 发票号码
			bi.setPkEmpinvoice(invoiceInfo.getPkEmpinvoice());// 票据领用主键
			bi.setDateInv(new Date());// 发票日期
			bi.setNote(DateUtils.dateToStr("yyyy-MM-dd HH:ss:mm", CommonUtils.isEmptyString(invoiceInfo.getDateInv())?new Date():DateUtils.strToDate(invoiceInfo.getDateInv())));//存放分担日期
			bi.setPkEmpInv(pkOpDoctor);// 发票开立人员
			bi.setNameEmpInv(nameUser);
			bi.setPrintTimes(0);
			bi.setFlagCancel(EnumerateParameter.ZERO);
			bi.setFlagCc(EnumerateParameter.ZERO);
			bi.setFlagCcCancel(EnumerateParameter.ZERO);
			bi.setPkInvoice(NHISUUID.getKeyId());
			bi.setAmountInv(blSettle.getAmountSt().doubleValue());// 发票总金额
			bi.setAmountPi(blSettle.getAmountPi().doubleValue());
			ApplicationUtils.setDefaultValue(bi, true);
			
			/**
			 * 插入发票明细
			 */
			for(BlInvoiceDt blInvDt : invoiceInfo.getBlInDts()){
				BlInvoiceDt blInvoiceDt = new BlInvoiceDt();
				blInvoiceDt.setPkInvoice(bi.getPkInvoice()); // 关联发票主键
				blInvoiceDt.setCodeBill(blInvDt.getCodeBill()); // 发票编码
				blInvoiceDt.setNameBill(blInvDt.getNameBill()); // 发票编码名称
				blInvoiceDt.setAmount(blInvDt.getAmount()); // 金额
				blInvoiceDt.setPkBill(blInvDt.getPkBill());// 
				ApplicationUtils.setDefaultValue(blInvoiceDt, true);
				blInvoiceDts.add(blInvoiceDt); 
				//amtInv = MathUtils.add(amtInv,blInvDt.getAmount());
			}
			
			blInvoices.add(bi);
			
			/**
			 * 写发票与结算的关系
			 */
			BlStInv bsi = new BlStInv();
			bsi.setPkOrg(pkOrg);
			bsi.setPkSettle(blSettle.getPkSettle());
			bsi.setPkInvoice(bi.getPkInvoice());
			ApplicationUtils.setDefaultValue(bsi, true);
			blStInvs.add(bsi);
		}
		
		ret.put("inv", blInvoices);
		ret.put("invDt", blInvoiceDts);
		ret.put("stInv", blStInvs);
		
		return ret;
	}

	@Override
	public Map<String, Object> invRePrint(Map<String, Object> param) {
		// TODO Auto-generated method stub
		return null;
	}

}
