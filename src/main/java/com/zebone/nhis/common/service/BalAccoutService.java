package com.zebone.nhis.common.service;

import com.zebone.nhis.common.module.bl.BlDepositPi;
import com.zebone.nhis.common.module.pay.BlExtPay;
import com.zebone.nhis.common.module.pi.PiAcc;
import com.zebone.nhis.common.module.pi.acc.PiAccDetail;
import com.zebone.nhis.common.module.pi.acc.PiAccShare;
import com.zebone.nhis.common.module.pv.BlExtPayBankVo;
import com.zebone.nhis.common.support.EnumerateParameter;
import com.zebone.platform.common.support.User;
import com.zebone.platform.framework.support.IUser;
import com.zebone.platform.modules.dao.DataBaseHelper;
import com.zebone.platform.modules.exception.BusException;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * 公共服务-结算业务
 * @author zl
 *
 */
@Service
public class BalAccoutService {
	
	/**
	 * dtPaymode 为收付款方式 添加入表bl_deposit_pi
	 * 新增收退款(仅限充值和提现，其他不可用此)
	 * 冻结账户不可操作
	 * 往pi_acc,pi_acc_detail,bl_deposit_pi表里面插入数据
	 * 有操作pk_pv的费用记录需要将pk_pv传入，否则null
	 * euOptype 有费用操作类型的加入操作类型，该方法未加入校验
	 * EuOptype类型 1充值 2账户消费 3信用消费 9退费
		信用额度的增加和减少从pi_acc_credit中获取
		退费时金额不能超过账户余额，并且不能超过同类型累计充值总额      请在外部校验
		接口后来改过，为了不影响之前使用，在这边修改通用1239
	 * @param param
	 * @param user
	 */
	public BlDepositPi saveMonOperation(BlDepositPi dp, IUser user, String pkPv, String euOptype, String dtPaymode){
		
		/**
		 * 总体逻辑：充值和退款的话，用的是当前患者的账户；
		 * 		    消费的话，要看当前账户是否有授权账户，如果有授权账户，用授权账户。
		 */
		PiAcc pa = null;
		//账户充值或退款
		if(EnumerateParameter.ONE.equals(euOptype)||EnumerateParameter.NINE.equals(euOptype))
		{
			// 充值的话 用自己的账户；消费的话用的是授权账户
			pa = queryPiAccByPkPi(dp.getPkPi());
			if(pa!=null&&EnumerateParameter.ONE.equals(pa.getEuStatus())){
				// 通过前台传入的退费明细更新bl_deposit_pi
				dp.setDatePay(new Date());
				dp.setPkDept(((User)user).getPkDept());
				dp.setPkEmpPay(((User)user).getPkEmp());
				dp.setNameEmpPay(((User)user).getNameEmp());
				dp.setDtPaymode(dtPaymode);
				DataBaseHelper.insertBean(dp);
				//若充值方式为银行卡/微信/支付宝  还要写入bl_ext_pay表
				boolean cashFlag = dtPaymode.equals(EnumerateParameter.ONE);
				boolean accountFlag = dtPaymode.equals(EnumerateParameter.FOUR);
				if(!(cashFlag || accountFlag)&& "1".equals(dp.getEuDirect())){
					rechargeUpdateBlExtPay(dp, dtPaymode);
				}
				
				if(pa.getAmtAcc()==null||"".equals(pa.getAmtAcc())){
					pa.setAmtAcc(BigDecimal.ZERO);
				}
				// 根据收退款的标志确定收款还是退款 收1 退-1
				BigDecimal amtAcc=pa.getAmtAcc().add(dp.getAmount());
				if(EnumerateParameter.NEGA.equals(dp.getEuDirect())){
					// 如果是退款，进来处理
					if(amtAcc.compareTo(BigDecimal.ZERO) < 0){
						throw new BusException("退款金额不能大于账户余额！");
					}
				}
				pa.setAmtAcc(amtAcc);
				this.piAccDetailVal(pa,dp,pkPv,euOptype);
			}
			else{
				throw new BusException("该账户已冻结或已被删除，不可收退款");
			}
		}else{
			//消费的话走的是授权账户
			pa = queryParentOrPiAccByPkPi(dp.getPkPi());
			this.piAccDetailVal(pa,dp,pkPv,euOptype);
		}
		return dp;
	}

	/**
	 * 账户余额可以为负，但是不能大于信用额度 ,针对消费的（即扣费）,其中费用值在BlDepositPi.Amount中，其中amount区别正负 
	 * 信用额度不变
	 * 1.校验：可扣款=账户余额+信用额度
	 * 2.授权扣费处理
	 * 注：消费时，信用额度不需要扣款，须校验；如果有授权账户的话，此处只对其被授权的账户的费用操作
	 * 若有多个被授权的，这里先取第一个
	 * 
	 * euOptype：2账户消费 3信用消费 
	 * @param pia
	 * @param jkjl
	 * @param pkPv
	 * @param euOptype
	 */
	public void piAccDetailVal(PiAcc pia ,BlDepositPi jkjl,String pkPv,String euOptype){
		if(pia == null){
			throw new BusException("该账户已冻结或已被删除，不可收退款");
		}
		PiAccDetail pad=new PiAccDetail();
		pad.setPkPiacc(pia.getPkPiacc());
		pad.setPkPi(pia.getPkPi());
		pad.setDateHap(new Date());
		//pad.setEuOptype(EnumerateParameter.ONE);
		if(pkPv!=null)
			pad.setPkPv(pkPv);
		if(euOptype!=null){
			if(EnumerateParameter.NINE.equals(euOptype)){
				if(pia.getAmtAcc().compareTo(jkjl.getAmount())<0){
					throw new BusException("退款金额不可超过账户余额。");
				}
				// 第三方退费时要更新外部表
				if(!jkjl.getDtPaymode().equals(EnumerateParameter.ONE)){
					//refundUpdateBlExtPay(jkjl);
				}
				euOptype = EnumerateParameter.ONE;//退款时,euOptype值写1
			}else if(EnumerateParameter.TWO.equals(euOptype)||EnumerateParameter.THREE.equals(euOptype)){//账户消费,信用消费
				if((pia.getAmtAcc().add(pia.getCreditAcc()).add(jkjl.getAmount())).compareTo(BigDecimal.ZERO)<0){//校验
					throw new BusException("账户余额不足。");
				}else{
					pia.setAmtAcc(pia.getAmtAcc().add(jkjl.getAmount()));//将账户余额去除消费金额(统一用账户费用，信用也是)
				}
			}
			pad.setEuOptype(euOptype);
		}
		pad.setEuDirect(jkjl.getEuDirect());
		pad.setAmount(jkjl.getAmount());
		pad.setPkDepopi(jkjl.getPkDepopi());
		pad.setAmtBalance(pia.getAmtAcc());
		pad.setPkEmpOpera(jkjl.getPkEmpPay());
		pad.setNameEmpOpera(jkjl.getNameEmpPay());
		DataBaseHelper.insertBean(pad);
		DataBaseHelper.updateBeanByPk(pia,false);
	}
	
	/*
	 * 充值方式为第三方支付方：银行卡/微信/支付宝。更新外部表
	 */
	private void rechargeUpdateBlExtPay(BlDepositPi dp, String dtPaymode) {
				
		if(dtPaymode.equals((EnumerateParameter.THREE))){
			List<BlExtPay> bankBlExtPays = DataBaseHelper.queryForList("select * from bl_ext_pay where SERIAL_NO=?",
																					BlExtPay.class, dp.getPayInfo());
			for (BlExtPay blExtPay : bankBlExtPays) {
				blExtPay.setPkDepopi(dp.getPkDepopi());
				blExtPay.setPkPi(dp.getPkPi());
				blExtPay.setSysname("银联");
			}
			
			DataBaseHelper.batchUpdate(DataBaseHelper.getUpdateSql(BlExtPay.class), bankBlExtPays);
		}else{
			BlExtPay weixinAliPay = DataBaseHelper.queryForBean("select * from bl_ext_pay where serial_no=?",
																					BlExtPay.class, dp.getPayInfo());

			weixinAliPay.setPkDepopi(dp.getPkDepopi());
			weixinAliPay.setPkPi(dp.getPkPi());
			if(dtPaymode.equals((EnumerateParameter.SEVEN))){
				weixinAliPay.setSysname("微信");
			}else if(dtPaymode.equals((EnumerateParameter.EIGHT))){
				weixinAliPay.setSysname("支付宝");
			}
			DataBaseHelper.updateBeanByPk(weixinAliPay, false);
		}
	}

	/*
	 * 退款时第三方支付方式---银行卡、微信、支付宝，更新外部表
	 */
	private void refundUpdateBlExtPay(BlDepositPi jkjl) {
		List<BlExtPayBankVo> blPayBankVos = jkjl.getBlPayTrdVO();
		
		if(CollectionUtils.isEmpty(blPayBankVos)){
			throw new BusException("第三方支付方式缺少新旧交易码");
		}
		
		for (BlExtPayBankVo blPayBankVo : blPayBankVos) {
			if(jkjl.getDtPaymode().equals(EnumerateParameter.THREE)){
				//银行卡
				BlExtPay bankBlExtPay = DataBaseHelper.queryForBean("select * from bl_ext_pay where trade_no = ?",
						BlExtPay.class, blPayBankVo.getNewBankCode());
				bankBlExtPay.setPkDepopi(jkjl.getPkDepopi());
				bankBlExtPay.setPkPi(jkjl.getPkPi());
				bankBlExtPay.setSysname("银联");
				bankBlExtPay.setAmount(new BigDecimal(-1).multiply(blPayBankVo.getAmount().abs()));
				DataBaseHelper.updateBeanByPk(bankBlExtPay,false);
			}
			else{
				//支付宝、微信
				BlExtPay weixinAliblExtPay = DataBaseHelper.queryForBean("select * from bl_ext_pay where serial_no = ?", 
						 BlExtPay.class, blPayBankVo.getNewBankCode());
				//取新生成的主键
				weixinAliblExtPay.setPkPi(jkjl.getPkPi()); //患者主键
				weixinAliblExtPay.setPkDepopi(jkjl.getPkDepopi()); 
				if(jkjl.getDtPaymode().equals(EnumerateParameter.SEVEN)){
					//微信
					weixinAliblExtPay.setAmount(new BigDecimal(-1.0).multiply(blPayBankVo.getAmount().divide(new BigDecimal(100)).abs())); //金额
					weixinAliblExtPay.setSysname("微信");
				}else{
					weixinAliblExtPay.setSysname("支付宝");
					weixinAliblExtPay.setAmount(new BigDecimal(-1.0).multiply(blPayBankVo.getAmount().abs())); //金额
				}
				DataBaseHelper.updateBeanByPk(weixinAliblExtPay, false);
			}
		}
	}
	
	/**
	 * 根据pkpi来获取患者账户（有授权用户的取授权账户，没有授权账户的取患者账户，没有患者账户取null）
	 * 设计假设：取账户是用来支付，考虑账户冻结情况。
	 * @param pkPi
	 * @return
	 */
	public PiAcc queryParentOrPiAccByPkPi(String pkPi)
	{
		PiAcc piAcc = null;

		// 查询患者存在的账户共享关系 --增加 and del_flag='0'查询表中存在的授权关系
		String psql = "select * from pi_acc_share where pk_pi_use=? and del_flag='0'";
		PiAccShare piAccShare = DataBaseHelper.queryForBean(psql, PiAccShare.class, pkPi);
		
		// 存在授权账户，用授权账户 
		if (piAccShare != null) {
			String preSql = "select * from pi_acc where pk_piacc=?  and nvl(del_flag,'0')='0'";
			piAcc = DataBaseHelper.queryForBean(preSql, PiAcc.class, piAccShare.getPkPiacc());
			// eu_code = '2' 当前账户被冻结
			if (EnumerateParameter.TWO.equals(piAcc.getEuStatus())){
				throw new BusException("父账户被冻结，不能使用账户支付，请使用其他付款方式。");
			}
		}else{
			// 查询患者自己账户信息
			String sql = "select * from pi_acc where pk_pi=?  and nvl(del_flag,'0')='0'";
			piAcc = DataBaseHelper.queryForBean(sql, PiAcc.class, pkPi);
			// 不存在患者账户
			if(piAcc == null){
				return piAcc;
			}
			// eu_code = '2' 当前账户被冻结
			if(EnumerateParameter.TWO.equals(piAcc.getEuStatus())){
				throw new BusException("当前账户被冻结，不能使用账户支付，请使用其他付款方式。");
			}
		}
		return piAcc;
	}
	
	/**
	 * 根据pkpi来获取患者自己的账户
	 * 设计假设：账户充值和账户退款走的都是患者自己账户，不考虑授权账户
	 * @param pkPi
	 * @return
	 */
	public PiAcc queryPiAccByPkPi(String pkPi)
	{
		PiAcc piAcc = null;
		// 查询患者自己账户信息
		String sql = "select * from pi_acc where pk_pi=?  and nvl(del_flag,'0')='0'";
		piAcc = DataBaseHelper.queryForBean(sql, PiAcc.class, pkPi);
		// 不存在患者账户
		if(piAcc == null){
			return piAcc;
		}
		// eu_code = '2' 当前账户被冻结
		if(EnumerateParameter.TWO.equals(piAcc.getEuStatus())){
			throw new BusException("当前账户被冻结，不能充值或退款。");
		}
		return piAcc;
	}
}
