package com.zebone.nhis.bl.pub.service;

import com.zebone.nhis.bl.pub.dao.BlPrePayMapper;
import com.zebone.nhis.bl.pub.util.AmountConversionUtil;
import com.zebone.nhis.bl.pub.vo.BillInfo;
import com.zebone.nhis.common.module.base.ou.BdOuDept;
import com.zebone.nhis.common.module.bl.BlDeposit;
import com.zebone.nhis.common.module.bl.BlDepositCancel;
import com.zebone.nhis.common.module.bl.BlDepositPi;
import com.zebone.nhis.common.module.mybatis.ReflectHelper;
import com.zebone.nhis.common.module.pay.BlExtPay;
import com.zebone.nhis.common.service.BalAccoutService;
import com.zebone.nhis.common.service.CommonService;
import com.zebone.nhis.common.support.ApplicationUtils;
import com.zebone.nhis.common.support.CommonUtils;
import com.zebone.nhis.common.support.EnumerateParameter;
import com.zebone.nhis.ma.pub.platform.PlatFormSendUtils;
import com.zebone.nhis.ma.pub.support.ExtSystemProcessUtils;
import com.zebone.platform.common.support.User;
import com.zebone.platform.common.support.UserContext;
import com.zebone.platform.framework.support.IUser;
import com.zebone.platform.modules.dao.DataBaseHelper;
import com.zebone.platform.modules.exception.BusException;
import com.zebone.platform.modules.utils.JsonUtil;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * 收退款记录服务，供住院预交金管理，入院登记等模块公用
 * 
 * @author Roger
 * 
 */
@Service
public class BlPrePayService {

	@Autowired
	private CommonService commonService;

	@Autowired
	private BalAccoutService balAccoutService;

	@Autowired
	private BlPrePayMapper blPrePayMapper;

	/**
	 * 
	 * @param param
	 * @param user
	 */
	public BlDeposit saveDeposit(String param, IUser user) {

		BlDeposit vo = JsonUtil.readValue(param, BlDeposit.class);
		
		// 校验预缴金票据号是否重复
		if ("1".equals(vo.getEuDirect())) {// 收款时
			String sql = "select count(1) from bl_deposit where rept_no=? and eu_dptype='9' and del_flag='0'";
			int count = DataBaseHelper.queryForScalar(sql, Integer.class,
					vo.getReptNo());
			if (count > 0) {
				throw new BusException("当前票据号存在重复数据，请前往【票据管理】进行维护！");
			}
			// 收款时生成流水号0606
			if (CommonUtils.isEmptyString(vo.getCodeDepo()))
				vo.setCodeDepo(ApplicationUtils.getCode("0606"));
		}
		BlDeposit save = save(vo, user);
//		增加预交金电子票据开立逻辑,判断系统参数BL0031
		User u = (User) user;
		Map<String, Object> codeMap = DataBaseHelper.queryForMap("select val from bd_sysparam  where del_flag='0' and pk_org = ? and code='BL0031'",u.getPkOrg());
		Object val = codeMap.get("val");
		if (val != null &&"1".equals(val.toString())) {
            HashMap<String, String> paramMap = new HashMap<>();
            paramMap.put("pkDepo", save.getPkDepo());
            paramMap.put("pkPv", save.getPkPv());
            paramMap.put("pkDepoBack", save.getPkDepoBack());
            if ("1".equals(save.getEuDirect())) {
//					预交金收款
                ExtSystemProcessUtils.processExtMethod("EBillInfo", "eBillAdvancePayment", new Object[]{paramMap, user});
            } else {
//					预交金退款
                ExtSystemProcessUtils.processExtMethod("EBillInfo", "eBillAdvanceOffPayment", new Object[]{paramMap, user});
            }
        }
		//(保存成功后)平台发送消息
		Map<String, Object> paramMap = JsonUtil.readValue(ApplicationUtils.beanToJson(save), Map.class);
		PlatFormSendUtils.sendDepositMsg(paramMap);
		return save;
	}

	/**
	 * 
	 * @param vo收退款记录的VO
	 */
	public BlDeposit save(BlDeposit vo, IUser user) {

		BdOuDept dept = DataBaseHelper.queryForBean(
				"select * from bd_ou_dept where pk_dept = ?", BdOuDept.class,
				vo.getPkDept());
		// 收费处打印预交金票据，护士站打印小票

		if (dept.getDtDepttype().equals("08")) {
			String pkEmpInv = vo.getPkEmpinvoice();
			if (CommonUtils.isEmptyString(pkEmpInv)) {
				throw new BusException("票据领用主键为空，请检查票据！");
			}
			if (vo.getEuDirect().equals("1")) {
				commonService.confirmUseEmpInv(pkEmpInv, (long) 1);
			}
		}

		if (EnumerateParameter.FOUR.equals(vo.getDtPaymode())) {
			BlDepositPi dp = new BlDepositPi();
			dp.setPkPi(vo.getPkPi());
			dp.setEuDirect("-1".equals(vo.getEuDirect()) ? "1" : "-1");
			dp.setAmount(vo.getAmount().multiply(new BigDecimal(-1)));
			dp.setDtPaymode("4");
			dp.setPayInfo(vo.getPayInfo());

			balAccoutService.saveMonOperation(dp, user, null, "2",
					dp.getDtPaymode());
		}
		setDefaultValue(vo, true);
		DataBaseHelper.insertBean(vo);

		switch (ApplicationUtils.getPropertyValue("projectName", "")) {
		case "ZSBA":// 博爱医院业务
			saveBlExtPay(vo);
			break;
		default:
			break;
		}
		return vo;
	}

	/**
	 * 交易号：007003002009 重打收据信息
	 * 
	 * @param param
	 * @param user
	 * @return
	 */
	public BlDeposit letInvInfo(String param, IUser user) {
		// 作废收据信息
		BlDeposit canVo = JsonUtil.readValue(
				JsonUtil.getJsonNode(param, "canVo"), BlDeposit.class);

		// 重新缴纳预交金信息
		BlDeposit newVo = JsonUtil.readValue(
				JsonUtil.getJsonNode(param, "newVo"), BlDeposit.class);
		// 收款时生成流水号0606
		newVo.setCodeDepo(ApplicationUtils.getCode("0606"));

		String sql = "select count(1) from bl_deposit where rept_no=? and eu_dptype='9' and del_flag='0'";
		int count = DataBaseHelper.queryForScalar(sql, Integer.class,
				newVo.getReptNo());
		if (count > 0) {
			throw new BusException("当前票据号存在重复数据，请前往【票据管理】进行维护！");
		}

		// 退款
		save(canVo, user);
		BlDeposit blDeposit = save(newVo, user);
		// 重新缴款
		// 更新第三方订单表
		int num = DataBaseHelper.queryForScalar(
				"select count(1)from  BL_EXT_PAY where PK_DEPO=? and PK_PV=? ",
				Integer.class,
				new Object[] { canVo.getPkDepoBack(), canVo.getPkPv() });
		System.out.println(num);
		if (num > 0) {

			String sql1 = "update  BL_EXT_PAY set PK_DEPO =? where  PK_DEPO=? and PK_PV=?  ";
			DataBaseHelper.update(sql1, new Object[] { blDeposit.getPkDepo(),
					canVo.getPkDepoBack(), canVo.getPkPv() });
		}
		return blDeposit;
	}

	public static void setDefaultValue(Object obj, boolean flag) {

		User user = UserContext.getUser();

		Map<String, Object> default_v = new HashMap<String, Object>();
		if (flag) { // 如果新增
			default_v.put("pkOrg", user.getPkOrg());
			default_v.put("creator", user.getPkEmp());
			default_v.put("createTime", new Date());
			default_v.put("delFlag", "0");
		}

		default_v.put("ts", new Date());
		default_v.put("modifier", user.getPkEmp());

		Set<String> keys = default_v.keySet();

		for (String key : keys) {
			Field field = ReflectHelper.getTargetField(obj.getClass(), key);
			if (field != null) {
				ReflectHelper.setFieldValue(obj, key, default_v.get(key));
			}
		}

	}

	/**
	 * 保存统一支付数据(银联、支付、微信)
	 * 
	 * @param vo
	 */
	public void saveBlExtPay(BlDeposit vo) {
		if (vo.getEuDirect().equals(EnumerateParameter.ONE)) {
			if (vo.getDtPaymode().equals(EnumerateParameter.THREE)
					|| vo.getDtPaymode().equals(EnumerateParameter.SEVEN)
					|| vo.getDtPaymode().equals(EnumerateParameter.EIGHT)) {
				BlExtPay blExtPay = DataBaseHelper.queryForBean(
						"select * from bl_ext_pay where SERIAL_NO =?",
						BlExtPay.class, vo.getPayInfo());
				if (blExtPay != null) {
					blExtPay.setPkDepo(vo.getPkDepo());
					blExtPay.setPkPi(vo.getPkPi());
					blExtPay.setPkPv(vo.getPkPv());
					DataBaseHelper.updateBeanByPk(blExtPay);
				}
			}
		}
	}

	/**
	 * 获取押金单数据
	 * 
	 * @param vo
	 */
	public Map<String, Object> getDepositFormData(String param, IUser user) {
		JSONObject jo = JSONObject.fromObject(param);
		String pkDepo = jo.getString("pkDepo");

		Map<String, Object> queryMap = new HashMap<String, Object>();
		queryMap.put("pkDepo", pkDepo);

		Map<String, Object> map = blPrePayMapper.getDepositFormData(queryMap);
		if (map == null) {
			throw new BusException("查不到押金单记录");
		}
		map.put("capitalAmount",
				AmountConversionUtil.changePrePay(Double.parseDouble(map.get(
						"amount").toString())));
		map.put("capitalAmountTwo", AmountConversionUtil.change(Double
				.parseDouble(map.get("amount").toString())));
		return map;
	}

	/**
	 * 重打预交金票据
	 * 
	 * @param param
	 * @param user
	 * @return
	 */
	public Map<String, Object> getDepositFormDataWham(String param, IUser user) {
		JSONObject jo = JSONObject.fromObject(param);
		String pkDepo = jo.getString("pkDepo");
		String dtDepttype = jo.getString("dtDepttype");
		String pkEmpinvoice = jo.getString("pkEmpinvoice");
		String reptNo = jo.getString("reptNo");
		BlDeposit depo = DataBaseHelper.queryForBean(
				"select * from bl_deposit where pk_depo = ?", BlDeposit.class,
				pkDepo);
		BdOuDept dept = DataBaseHelper.queryForBean(
				"select * from bd_ou_dept where pk_dept = ?", BdOuDept.class,
				depo.getPkDept());

		Map<String, Object> queryMap = new HashMap<String, Object>();
		queryMap.put("pkDepo", pkDepo);

		Map<String, Object> map = blPrePayMapper
				.getDepositFormDataWham(queryMap);
		if (map == null) {
			throw new BusException("查不到押金单记录");
		}
		if (dtDepttype.equals("08")) {
			// 收费处 收费处只能重打收费处交的预交金票据
			if (dept.getDeptType().equals("08")) {
				commonService.confirmUseEmpInv(pkEmpinvoice, (long) 1);
				String capitalAmount = AmountConversionUtil.changePrePay(Double
						.parseDouble(map.get("amount").toString()));
				map.put("capitalAmount", capitalAmount);
				map.put("reptNo", reptNo);
				// 将旧的票据信息添加到作废预交金票据记录表
				BlDepositCancel dc = new BlDepositCancel();
				dc.setPkDepo(depo.getPkDepo());
				// depo实体类的PkEmpinvoice属性的set方法不一样，导致值是空的，不知道哪个地方用的，这个单独赋值
				dc.setPkEmpinvoice(map.get("pkEmpinvoice").toString());
				dc.setCodeBill(depo.getReptNo());
				dc.setDateBill(depo.getDatePay());
				dc.setAmountBill(depo.getAmount().doubleValue());
				dc.setPkEmpBill(depo.getPkEmpBack());
				dc.setNameEmpBill(depo.getNameEmpPay());
				dc.setDateCancel(new Date());
				dc.setPkEmpCancel(user.getId());
				dc.setNameEmpCancel(user.getUserName());
				DataBaseHelper.insertBean(dc);
				// 交款记录的票据信息更新为当前票据
				depo.setPkEmpinv(pkEmpinvoice);
				depo.setReptNo(reptNo);
				DataBaseHelper.updateBeanByPk(depo, false);
			} else {
				throw new BusException("该押金单记录是在护士站生成的，请去护士站打印");
			}
		} else {
			// 护士站 护士站只能重打护士站交的预交金票据
			if (dept.getDeptType().equals("08")) {
				throw new BusException("该押金单记录是在收费站生成的，请去收费处打印");
			}
		}
		return map;
	}

	/**
	 * 交易号：007003002008 住院预交金作废当前使用票据
	 * 
	 * @param param
	 * @param user
	 */
	public void canelDepo(String param, IUser user) {
		BillInfo bill = JsonUtil.readValue(param, BillInfo.class);

		// 作废发票，写表bl_deposit
		BlDeposit inv = new BlDeposit();

		inv.setEuDptype("9");
		inv.setEuDirect("-1");
		inv.setEuPvtype("3");
		inv.setAmount(new BigDecimal(0));
		inv.setDtPaymode("1");
		inv.setDatePay(new Date());
		inv.setPkDept(UserContext.getUser().getPkDept());
		inv.setPkEmpPay(UserContext.getUser().getPkEmp());
		inv.setNameEmpPay(UserContext.getUser().getNameEmp());
		inv.setFlagAcc("0");
		inv.setFlagSettle("0");
		inv.setPkEmpinv(bill.getPkEmpinv());
		inv.setReptNo(bill.getCurCodeInv());
		inv.setFlagReptBack("0");
		inv.setFlagCc("0");
		inv.setNote("作废收据");

		DataBaseHelper.insertBean(inv);

		// 7.调用发票使用确认服务，完成发票更新。
		commonService.confirmUseEmpInv(bill.getPkEmpinv(), new Long(1));
	}

	/**
	 * 交易号：007003002010 查询患者就诊状态
	 * 
	 * @param param
	 * @param user
	 * @return
	 */
	public String qryPvEuStatus(String param, IUser user) {
		String pkPv = JsonUtil.getFieldValue(param, "pkPv");
		;
		Map<String, Object> retMap = DataBaseHelper.queryForMap(
				"select eu_status from PV_ENCOUNTER where pk_pv = ?", pkPv);

		String euStatus = "";
		if (retMap != null && retMap.size() > 0
				&& retMap.get("euStatus") != null)
			euStatus = CommonUtils.getString(retMap.get("euStatus"));

		return euStatus;
	}

	/**
	 * 
	 * @param param
	 * @param user
	 */
	public void updateBlDeposit(String param, IUser user) {
		String pkDepo = JsonUtil.getFieldValue(param, "pkDepo");
		String reptNo = JsonUtil.getFieldValue(param, "reptNo");
		String pkEmpinv = JsonUtil.getFieldValue(param, "pkEmpinv");
		String codeDepo = JsonUtil.getFieldValue(param, "codeDepo");
		if (pkDepo != null && reptNo != null && pkEmpinv != null
				&& codeDepo != null) {
			DataBaseHelper
					.execute(
							"update BL_DEPOSIT set REPT_NO=?,PK_EMP_REPT=?,NAME_EMP_REPT=?,DATE_REPT=?,PK_EMPINVOICE=?,CODE_DEPO=? where PK_DEPO=?",
							new Object[] { reptNo,
									UserContext.getUser().getPkEmp(),
									UserContext.getUser().getNameEmp(),
									new Date(),
									pkEmpinv, codeDepo, pkDepo });
		} else {
			throw new BusException("传入参数有空值");
		}
	}
	
	/**
	 * 交易号：007003002012
	 * 校验预交金部分退费的金额是否正确
	 * @param param
	 * @param user
	 */
	public void chkDepoAmtBack(String param,IUser user){
		BlDeposit depo = JsonUtil.readValue(param, BlDeposit.class);
		
		//查询该预交金可退金额
		Map<String,Object> paramMap = new HashMap<>(16);
		paramMap.put("pkDepoBack", depo.getPkDepoBack());
		BlDeposit chkVo = blPrePayMapper.qryDepoBackAmt(paramMap);
		
		depo.setAmount(depo.getAmount().multiply(BigDecimal.valueOf(-1d)));
		
		//校验前台传入的退费金额和数据库查出的可退费金额是否一致
		if(chkVo!=null 
				&& depo.getPkDepoBack().equals(chkVo.getPkDepo()) 
				&& depo.getAmount().compareTo(chkVo.getAmount())!=0){
			throw new BusException(String.format("传入的退费金额%s元与实际可退费金额%s不一致，请联系管理员！",depo.getAmount(),chkVo.getAmount()));
		}
	}
	/**
	 * 交易号：
	 * 更新交款记录表中的付款方式
	 * @param param
	 * @param user
	 */
	public void updateBlDepositPayMode(String param, IUser user) {
		BlDeposit depoVo=JsonUtil.readValue(param, BlDeposit.class);
					
		if (depoVo!=null && depoVo.getPkDepo() != null) {
			BlDeposit depo=DataBaseHelper.queryForBean("select * from bl_deposit where pk_depo = ?",BlDeposit.class,depoVo.getPkDepo());	
			if(depo!=null && !depo.getFlagCc().equals("1"))
			{							
				DataBaseHelper.execute(
							"update BL_DEPOSIT set DT_PAYMODE=?,modifier=?, ts=? where PK_DEPO=?",
							new Object[] { depoVo.getDtPaymode(),
									UserContext.getUser().getPkEmp(),
									new Date(),
									depoVo.getPkDepo() });
			} else {
				throw new BusException("交款记录不存在或已日结");
			}
			
		} else {
			throw new BusException("传入参数有空值");
		}
	}
}
