package com.zebone.nhis.bl.opcg.service;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.zebone.nhis.bl.pub.vo.BlSettleInvoiceVo;
import com.zebone.nhis.bl.pub.vo.InvoiceInfo;
import com.zebone.nhis.bl.pub.vo.OpCgTransforVo;
import com.zebone.nhis.common.module.bl.BlInvoice;
import com.zebone.nhis.common.module.bl.BlInvoiceDt;
import com.zebone.nhis.common.module.bl.BlStInv;
import com.zebone.nhis.common.support.ApplicationUtils;
import com.zebone.nhis.common.support.CommonUtils;
import com.zebone.nhis.common.support.EnumerateParameter;
import com.zebone.platform.common.support.User;
import com.zebone.platform.common.support.UserContext;
import com.zebone.platform.framework.support.IUser;
import com.zebone.platform.modules.dao.DataBaseHelper;
import com.zebone.platform.modules.exception.BusException;
import com.zebone.platform.modules.utils.JsonUtil;

/***
 * 门诊发票打印服务
 * 
 * @author Administrator
 *
 */
@Service
public class InvoiceService {

	/**
	 * 查询患者未打印发票的结算数据
	 * 
	 * @param param
	 * @param user
	 * @return
	 * @throws ParseException
	 */
	@SuppressWarnings({ "unchecked" })
	public List<BlSettleInvoiceVo> getInvoiceSettle(String param, IUser user) throws ParseException {

		Map<String, Object> map = JsonUtil.readValue(param, Map.class);

		String pkPi = CommonUtils.getString(map.get("pkPi"));
		String endDate = CommonUtils.getString(map.get("dateend"));
		String beginDate = CommonUtils.getString(map.get("dateSt"));
		String pkOrg = ((User) user).getPkOrg();
		
		// 查询结算信息
		String sql = "select st.pk_settle as pkSettle,st.date_st as dateCg,st.amount_st as amountSt,st.amount_pi as amountPi,st.amount_insu as amountInsu,"
				+ " decode(dt_sttype,'01','门诊挂号','00','门诊结算')as dtSttype from bl_settle st where st.pk_pi=? and st.date_st>=to_date(?,'yyyy-MM-dd HH24:mi:ss') and st.date_st<=to_date(?,'yyyy-MM-dd HH24:mi:ss') and "
				+ "st.pk_org=? and st.dt_sttype<'10' and not exists (select 1 from bl_st_inv si where st.pk_settle=si.pk_settle) order by st.date_st desc";
		List<BlSettleInvoiceVo> list = DataBaseHelper.queryForList(sql, BlSettleInvoiceVo.class,
				new Object[] { pkPi, beginDate, endDate,pkOrg });

		return list;
	}

	/***
	 * 门诊发票保存打印信息
	 * 
	 * @param param
	 * @param user
	 * @return
	 */
	public OpCgTransforVo printSaveInvoice(String param, IUser user) {

		/** 获取前台参数 */
		OpCgTransforVo opCgTransforVo = JsonUtil.readValue(param, OpCgTransforVo.class);
		List<InvoiceInfo> invoiceInfos = opCgTransforVo.getInvoiceInfo();
		String pkOrg = ((User) user).getPkOrg();

		// 写发票
		List<BlInvoice> blInvoices = new ArrayList<>(); // 发票
		List<BlInvoiceDt> blInvoiceDts = new ArrayList<BlInvoiceDt>(); // 发票明细
		List<BlStInv> blStInvs = new ArrayList<BlStInv>(); // 写发票与结算的关系
		if (invoiceInfos == null) {
			throw new BusException("发票票据号和发票明细为空，请检查传递的参数。");
		}
		for (InvoiceInfo invoiceInfo : invoiceInfos) {
			BlInvoiceDt blInDt = invoiceInfo.getBlInDts().get(0); // 目前只考虑一张发票对应一个发票明细
			if (blInDt == null) {
				throw new BusException("前台未传发票明细参数，请重新核对");
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
			bi.setAmountInv(blInDt.getAmount());// 结算金额

			bi.setAmountPi(blInDt.getAmount());// 患者自付
			bi.setPkEmpInv(UserContext.getUser().getPkEmp());// 发票开立人员
			bi.setNameEmpInv(UserContext.getUser().getNameEmp());
			bi.setPrintTimes(0);
			bi.setFlagCancel(EnumerateParameter.ZERO);
			bi.setFlagCc(EnumerateParameter.ZERO);
			bi.setFlagCcCancel(EnumerateParameter.ZERO);
			ApplicationUtils.setDefaultValue(bi, true);
			blInvoices.add(bi);

			/**
			 * 插入发票明细
			 */
			BlInvoiceDt blInvoiceDt = new BlInvoiceDt();
			blInvoiceDt.setPkInvoice(bi.getPkInvoice()); // 关联发票主键
			blInvoiceDt.setCodeBill(blInDt.getCodeBill()); // 发票编码
			blInvoiceDt.setNameBill(blInDt.getNameBill()); // 发票编码名称
			blInvoiceDt.setAmount(blInDt.getAmount()); // 金额
			blInvoiceDt.setPkBill(blInDt.getPkBill());//
			ApplicationUtils.setDefaultValue(blInvoiceDt, true);
			blInvoiceDts.add(blInvoiceDt);

			/**
			 * 写发票与结算的关系
			 */
			// 一张发票对应多个结算(需要根据前台数据重新处理)
			// for (int i = 0; i < array.length; i++) {
			BlStInv bsi = new BlStInv();
			bsi.setPkOrg(pkOrg);
			bsi.setPkSettle(opCgTransforVo.getPkSettle());
			bsi.setPkInvoice(bi.getPkInvoice());
			ApplicationUtils.setDefaultValue(bsi, true);
			blStInvs.add(bsi);
			// }

		}
		if (blInvoices.size() > 0) {
			DataBaseHelper.batchUpdate(DataBaseHelper.getInsertSql(BlInvoice.class), blInvoices); // 批量插入发票
		}
		if (blInvoiceDts.size() > 0) {
			DataBaseHelper.batchUpdate(DataBaseHelper.getInsertSql(BlInvoiceDt.class), blInvoiceDts); // 批量插入发票明细
		}
		if (blStInvs.size() > 0) {
			DataBaseHelper.batchUpdate(DataBaseHelper.getInsertSql(BlStInv.class), blStInvs); // 批量写发票与结算的关系
		}

		// 返回发票明细
		opCgTransforVo.setBlInvoiceDts(blInvoiceDts);

		List<InvoiceInfo> invoiceList = opCgTransforVo.getInvoiceInfo();
		for (InvoiceInfo invoiceInfo : invoiceList) {
			BlInvoiceDt blInDt = invoiceInfo.getBlInDts().get(0);
			for (BlInvoiceDt blInvoiceDt : blInvoiceDts) {
				if (blInDt.getCodeBill().equals(blInvoiceDt.getCodeBill())) {
					blInDt.setPkInvoice(blInvoiceDt.getPkInvoice());
					break;
				}
			}
		}

		// 把发票主键放回发票明细中 返回前台
		opCgTransforVo.setInvoiceInfo(invoiceList);
		return opCgTransforVo;
	}
}
