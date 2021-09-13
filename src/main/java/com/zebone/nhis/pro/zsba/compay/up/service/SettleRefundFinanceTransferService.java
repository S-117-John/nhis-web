package com.zebone.nhis.pro.zsba.compay.up.service;

import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.codehaus.jackson.type.TypeReference;
import org.springframework.stereotype.Service;

import com.google.common.collect.Lists;
import com.zebone.nhis.pro.zsba.compay.up.vo.SettleRefundFinanceTransfer;
import com.zebone.nhis.pro.zsba.compay.up.vo.SettleRefundTurnFinance;
import com.zebone.platform.common.support.UserContext;
import com.zebone.platform.framework.support.IUser;
import com.zebone.platform.modules.dao.DataBaseHelper;
import com.zebone.platform.modules.exception.BusException;
import com.zebone.platform.modules.utils.JsonUtil;


/**
 * 
 * 结算多余预交金退费：财务转账提交与查询
 * 
 * @author lipz
 * 
 */
@Service
public class SettleRefundFinanceTransferService {
	
	
	/**
	 * 查询财务转账记录
	 * @param param
	 * @param user
	 * @return
	 */
	public List<SettleRefundFinanceTransfer> queryTransfer(String param, IUser user) {
		List<SettleRefundFinanceTransfer> srrList = Lists.newArrayList();

		SettleRefundFinanceTransfer ft = JsonUtil.readValue(param, SettleRefundFinanceTransfer.class);
		if (ft != null
				&& (StringUtils.isNotEmpty(ft.getPkSettle()) 
						|| StringUtils.isNotEmpty(ft.getPkSettleRefundTf())
						|| StringUtils.isNotEmpty(ft.getPkPv()))) {

			Object[] params = null;
			StringBuffer sql = new StringBuffer(
					" SELECT * FROM PAY_SETTLE_REFUND_FINANCE_TRANSFER WHERE DEL_FLAG='0' ");
			if (StringUtils.isNotEmpty(ft.getPkSettle())) {
				sql.append(" and PK_SETTLE=?");
				params = new Object[] { ft.getPkSettle() };
				
			} else if(StringUtils.isNotEmpty(ft.getPkSettleRefundTf())){
				sql.append(" and PK_SETTLE_REFUND_TF=?");
				params = new Object[] { ft.getPkSettleRefundTf() };
				
			}else if (StringUtils.isNotEmpty(ft.getPkPv())) {
				sql.append(" and PK_PV=?");
				params = new Object[] {ft.getPkPv() };
			}
			if (params != null) {
				srrList = DataBaseHelper.queryForList(sql.toString(), SettleRefundFinanceTransfer.class, params);
			}
		} else {
			throw new BusException("参数错误，pkSettle不能为空 或者 pkSettleRefundTf 或者 pkPv不能为空！");
		}
		return srrList;
	}
	
	/**
	 * 保存财务转账退费数据
	 * 022003002010
	 * @param param
	 * @param user
	 * @return
	 */
	public String insertTransfer(String param, IUser user){
		
		List<SettleRefundFinanceTransfer> data = JsonUtil.readValue(param, new TypeReference<List<SettleRefundFinanceTransfer>>(){});
		
		SettleRefundTurnFinance tf = null;
		String tfSql = "SELECT * FROM PAY_SETTLE_REFUND_TURN_FINANCE where PK_SETTLE_REFUND_TF=?";
				
		for(SettleRefundFinanceTransfer ft : data){
			if(StringUtils.isNotEmpty(ft.getPkSettleRefundTf()) && ft.getOutAmount()!=null 
					&& StringUtils.isNotEmpty(ft.getOutBankCardNo()) && StringUtils.isNotEmpty(ft.getOutBankName())){
				if(tf==null){
					tf = DataBaseHelper.queryForBean(tfSql, SettleRefundTurnFinance.class, new Object[]{ft.getPkSettleRefundTf()});
				}
				if(tf!=null){
					ft.setPkSettle(tf.getPkSettle());
					ft.setPkPi(tf.getPkPi());
					ft.setPkPv(tf.getPkPv());
					ft.setPkDepo(tf.getPkDepo());
					
					ft.setPkOrg(UserContext.getUser().getPkOrg());//当前操作人关联机构
					ft.setCreator(UserContext.getUser().getCodeEmp());//当前操作人关联编码
					ft.setCreateTime(new Date());
					ft.setModifier(ft.getCreator());
					ft.setModityTime(ft.getCreateTime());
					DataBaseHelper.insertBean(ft);
				}else{
					throw new BusException("数据不存在，根据主键["+ft.getPkSettleRefundTf()+"]未查询到转财务退费数据！");
				}
			}else{
				throw new BusException("参数错误，pkSettleRefundTf、outAmount、outBankCardNo、outBankName不能为空！");
			}
		}
		if(tf!=null){
			tf.setRefundStatus("1");//标记为已退
			DataBaseHelper.updateBeanByPk(tf);
			
			//更新待退款记录的退款标志为 已退款
			String sql = "UPDATE PAY_SETTLE_REFUND_RECORD SET REFUND_STATUS='1'  WHERE DEL_FLAG='0' and REFUND_STATUS='2' and PK_SETTLE=?";
			DataBaseHelper.execute(sql, tf.getPkSettle());
		}
		return "true";
	}

}
