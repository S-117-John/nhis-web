package com.zebone.nhis.bl.pub.pay.service;

import com.zebone.nhis.bl.pub.service.SettlePdOutService;
import com.zebone.nhis.common.module.bl.*;
import com.zebone.nhis.common.module.bl.opcg.BlOpDt;
import com.zebone.nhis.common.module.pay.BlExtPay;
import com.zebone.nhis.common.support.ApplicationUtils;
import com.zebone.nhis.common.support.EnumerateParameter;
import com.zebone.platform.framework.support.IUser;
import com.zebone.platform.modules.dao.DataBaseHelper;
import com.zebone.platform.modules.exception.BusException;
import com.zebone.platform.modules.utils.JsonUtil;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class NotifyPayService {
	
	@Autowired
	private SettlePdOutService settlePdOutService;
	
	/**
	 * 模拟测试  诊间支付扫描完二维码后的回调业务
	 */
	public void clinicNotifyBusinessTest(String param, IUser user){
		BlExtPay blExtPayOld = JsonUtil.readValue(param, BlExtPay.class);
		String serialNo = blExtPayOld.getSerialNo(); //临时收费主键
		BlExtPay blExtPay =  DataBaseHelper.queryForBean("select * from bl_ext_pay where pk_bus = ?",
                 BlExtPay.class, new Object[] {serialNo });

		blExtPay.setFlagPay("1");
		blExtPay.setSerialNo(serialNo);
		blExtPay.setDatePay(new Date());
		blExtPay.setEuPaytype(blExtPayOld.getEuPaytype());
		blExtPay.setOutTradeNo(blExtPayOld.getOutTradeNo());   // 和支付宝交易的订单号 "alipay1213214"
		
		// 1 根据临时结算表生成正式结算表
		BlSettle blSettle = createFormalBlSettle(serialNo);
		// 2 生成正式项目表记录
		List<BlOpDt> blOpDts = createFormalBlOpDts(serialNo, blSettle);
		// 3 生成付款方式记录
		createBlDeposit(blExtPay, blSettle);
		// 4 生成结算明细表
		createBlSettleDetail(blExtPay, blSettle);
		// 5 回写正式表结算主键到第三方表
		updateBlExtPay(blExtPay, blSettle);
		// 6 生成处方单；生成检治执行单,处理库存量。
		settlePdOutService.makeSdPresAssitRecords(blOpDts); 
		// 7 如果选择支付方式是微信的话，让支付宝的二维码失效；反之，让微信的二维码失效。 --- 重起一个事务
		cancleOrder(blExtPay);
	}
	
	/**
	 * 诊间支付扫描完二维码后的回调业务
	 */
	public void clinicNotifyBusiness(BlExtPay blExtPay){
		String serialNo = blExtPay.getSerialNo(); //临时收费主键
		// 1 根据临时结算表生成正式结算表
		BlSettle blSettle = createFormalBlSettle(serialNo);
		// 2 生成正式项目表记录
		List<BlOpDt> blOpDts = createFormalBlOpDts(serialNo, blSettle);
		// 3 生成付款方式记录
		createBlDeposit(blExtPay, blSettle);
		// 4 生成结算明细表
		createBlSettleDetail(blExtPay, blSettle);
		// 5 回写正式表结算主键到第三方表
		updateBlExtPay(blExtPay, blSettle);
		// 6 生成处方单；生成检治执行单,处理库存量。
		settlePdOutService.makeSdPresAssitRecords(blOpDts); 
		// 7 如果选择支付方式是微信的话，让支付宝的二维码失效；反之，让微信的二维码失效。 --- 重起一个事务
		cancleOrder(blExtPay);
	}
	
	/*
	 * 当微信支付成功后，撤销支付宝的订单。当支付宝支付成功后，撤销微信的订单。
	 */
	@Transactional(propagation= Propagation.NESTED)
	private void cancleOrder(BlExtPay blExtPay){
		String euPaytype = blExtPay.getEuPaytype(); //支付类型
		try{
			// 微信
			if(euPaytype.equals(EnumerateParameter.SEVEN)){
				// 撤销支付宝订单
				return;
			}
			// 支付宝
			else if(euPaytype.equals(EnumerateParameter.EIGHT)){
				// 撤销微信订单
				return;
			}
		}catch(Exception e){
			throw new BusException("支付宝/微信 撤销订单失败");
		}
	}

	/*
	 * 生成付款方式记录
	 */
	private void createBlDeposit(BlExtPay blExtPay, BlSettle blSettle) {
		BlDeposit blDeposit = new BlDeposit();
		blDeposit.setPkOrg(blSettle.getPkOrg());
		blDeposit.setEuDirect(EnumerateParameter.ONE);
		blDeposit.setPkPi(blSettle.getPkPi());
		blDeposit.setPkPv(blSettle.getPkPv());
		blDeposit.setAmount(blExtPay.getAmount());   // 支付金额
		blDeposit.setDtPaymode(blExtPay.getEuPaytype()); //支付方式
		blDeposit.setDatePay(new Date());
		blDeposit.setPkDept(blSettle.getPkDeptSt());
		blDeposit.setPkEmpPay(blSettle.getPkEmpSt());
		blDeposit.setNameEmpPay(blSettle.getNameEmpSt());
		blDeposit.setFlagSettle(EnumerateParameter.ONE);
		blDeposit.setPkSettle(blSettle.getPkSettle());
		
		ApplicationUtils.setDefaultValue(blDeposit, true);
		DataBaseHelper.insertBean(blDeposit);
	}
	
	/*
	 * 回写正式的结算主键和结算状态到第三方表中
	 * blExtPay在和微信/支付宝交互的时候，要反写serial_no和交易类型（7/8）
	 */
	private void updateBlExtPay(BlExtPay blExtPay, BlSettle blSettle) {
		// 修改支付状态
		blExtPay.setFlagPay(EnumerateParameter.ONE);
    	blExtPay.setDatePay(new Date());
		blExtPay.setPkBus(blSettle.getPkSettle());  // 回写正式结算主键
		ApplicationUtils.setDefaultValue(blExtPay, false);
		DataBaseHelper.updateBeanByPk(blExtPay, false);
	}

	/*
	 *  创建正式项目表记录
	 */
	private List<BlOpDt> createFormalBlOpDts(String serialNo, BlSettle newBlSettle) {
		String tempBodSql= "select * from temp_bl_op_dt where pk_settle=?";
		List<TempBlOpDt> tempBlOpDts = DataBaseHelper.queryForList(tempBodSql, TempBlOpDt.class, serialNo);
		
		if(CollectionUtils.isEmpty(tempBlOpDts)){
			throw new BusException("临时项目记录表未空");
		}
		
		List<BlOpDt> blOpDts = new ArrayList<>();
		for (TempBlOpDt tempBlOpDt : tempBlOpDts) {
			BlOpDt blOpDt = new BlOpDt();
			try {
				BeanUtils.copyProperties(blOpDt, tempBlOpDt);
			} catch (Exception exception) {
				throw new BusException("项目明细表复制临时项目明细表中属性出错，请检查字段。");
			}
			blOpDt.setPkCgop(tempBlOpDt.getPkTempCgop());   // 还原之前前台传递的项目主键
			blOpDt.setPkSettle(newBlSettle.getPkSettle());  // 关联新结算主键
			blOpDt.setFlagSettle(EnumerateParameter.ONE);   // 已结算标记
			blOpDts.add(blOpDt);
			// 删除临时表。
			DataBaseHelper.deleteBeanByPk(tempBlOpDt);
		}
		DataBaseHelper.batchUpdate(DataBaseHelper.getUpdateSql(BlOpDt.class), blOpDts);
		return blOpDts;
	}

	/*
	 * 根据临时结算表生成正式结算表
	 */
	private BlSettle createFormalBlSettle(String serialNo) {
		// 查询临时收费表记录
		String tempBsSql= "select * from temp_bl_settle where pk_temp_settle=?";
		TempBlSettle tempBlSettle = DataBaseHelper.queryForBean(tempBsSql, TempBlSettle.class, serialNo);
		
		if(tempBlSettle == null){
			throw new BusException("临时收费表未空");
		}
		
		BlSettle blSettle = new BlSettle();
		try {
			BeanUtils.copyProperties(blSettle, tempBlSettle);
		} catch (Exception exception) {
			throw new BusException("收费表复制临时收费表中属性出错，请检查字段。");
		}
		blSettle.setEuPvtype(EnumerateParameter.ONE);    //门诊
		blSettle.setEuStresult(EnumerateParameter.ZERO); //正常结算
		ApplicationUtils.setDefaultValue(blSettle, true);
		DataBaseHelper.insertBean(blSettle);
		
		// 删除临时表。
		tempBlSettle.setDelFlag(EnumerateParameter.ONE);
		tempBlSettle.setTs(new Date());
		DataBaseHelper.updateBeanByPk(tempBlSettle, false);
		
		return blSettle;
	}
	
	/*
	 * 写表bl_settle_detail；
	 * @param blSettle
	 */
	private void createBlSettleDetail(BlExtPay blExtPay, BlSettle blSettle) {
		BlSettleDetail blSettleDetail = new BlSettleDetail();
		blSettleDetail.setPkSettle(blSettle.getPkSettle());
		blSettleDetail.setAmount(blExtPay.getAmount().doubleValue());// 患者微信/支付宝支付的金额
		ApplicationUtils.setDefaultValue(blSettleDetail, true);// 设置默认字段
		DataBaseHelper.insertBean(blSettleDetail);
	}
}
