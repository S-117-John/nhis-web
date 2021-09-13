package com.zebone.nhis.pro.zsba.compay.pub.service;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONObject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zebone.nhis.bl.pub.util.AmountConversionUtil;
import com.zebone.nhis.common.module.base.bd.srv.BdItem;
import com.zebone.nhis.common.module.base.ou.BdOuDept;
import com.zebone.nhis.common.module.bl.BlDeposit;
import com.zebone.nhis.common.module.bl.BlDepositCancel;
import com.zebone.nhis.common.module.bl.BlDepositPi;
import com.zebone.nhis.common.module.bl.BlIpDt;
import com.zebone.nhis.common.module.pay.BlExtPay;
import com.zebone.nhis.common.service.BalAccoutService;
import com.zebone.nhis.common.service.CommonService;
import com.zebone.nhis.common.support.ApplicationUtils;
import com.zebone.nhis.common.support.CommonUtils;
import com.zebone.nhis.common.support.EnumerateParameter;
import com.zebone.nhis.pro.zsba.compay.ins.pub.dao.InsPubBlPrePayMapper;
import com.zebone.nhis.pro.zsba.compay.ins.zsyb.vo.MzDayDetailsData;
import com.zebone.nhis.pro.zsba.compay.ins.zsyb.vo.MzDayInvData;
import com.zebone.nhis.pro.zsba.compay.pub.vo.SaveMzDayOpCostParam;
import com.zebone.nhis.pro.zsba.compay.pub.vo.ZsbaBlDeposit;
import com.zebone.nhis.pro.zsba.compay.pub.vo.ZsbaBlUpPaymode;
import com.zebone.platform.common.support.User;
import com.zebone.platform.common.support.UserContext;
import com.zebone.platform.framework.support.IUser;
import com.zebone.platform.modules.dao.DataBaseHelper;
import com.zebone.platform.modules.exception.BusException;
import com.zebone.platform.modules.utils.JsonUtil;

/**
 * 收退款记录服务，供住院预交金管理，入院登记等模块公用
 * @author lipz
 *
 */
@Service//("zsBlPrePayService")
public class ZsBlPrePayService {

	@Autowired private CommonService commonService;
	@Autowired private BalAccoutService balAccoutService;
	@Autowired private InsPubBlPrePayMapper insPubBlPrePayMapper;
	
	
	/**
	 * 功能号[022003002001]
	 * 生成预交金，并更新支付记录关联预交金
	 * @param param		BlDeposit表数据
	 * @param user		当前登录用户
	 * @return			BlDeposit表数据
	 */
	public ZsbaBlDeposit saveDeposit(String param, IUser user) {
		ZsbaBlDeposit vo = JsonUtil.readValue(param, ZsbaBlDeposit.class);
		String deptSql = "select * from bd_ou_dept where pk_dept = ?";
		BdOuDept dept = DataBaseHelper.queryForBean(deptSql, BdOuDept.class, vo.getPkDept());
		//String s = null;
		//s = s.toString();
		// 校验预缴金票据号是否重复
		if ("1".equals(vo.getEuDirect())) {// 收款时
			
			//收费处才需要查询票据
			//if(dept.getDeptType().equals("08")){
				String countSql = "select count(1) from bl_deposit where rept_no=? and eu_dptype='9' and del_flag='0'";
				int count = DataBaseHelper.queryForScalar(countSql, Integer.class, vo.getReptNo());
				if (count > 0) {
					throw new BusException("当前票据号存在重复数据，请前往【票据管理】进行维护！");
				}
			//}
			
			// 收款时生成流水号0606
			if (CommonUtils.isEmptyString(vo.getCodeDepo())){
				vo.setCodeDepo(ApplicationUtils.getCode("0606"));
			}
		}
		return save(vo, dept, user);
	}

	/**
	 * 生成预交金，并更新支付记录关联预交金
	 * @param vo	收退款记录的VO
	 * @param user
	 * @return
	 */
	public ZsbaBlDeposit save(ZsbaBlDeposit vo, BdOuDept dept, IUser user) {
		// 收费处打印预交金票据，护士站打印小票
		//if (dept.getDtDepttype().equals("08")) {
		if(!vo.getEuPvtype().equals("11")){//11：门禁卡    门禁卡打印小票  不需要领用票据
			String pkEmpInv = vo.getPkEmpinvoice();
			if (CommonUtils.isEmptyString(pkEmpInv)) {
				throw new BusException("票据领用主键为空，请检查票据！");
			}
			if (vo.getEuDirect().equals("1")) {
				commonService.confirmUseEmpInv(pkEmpInv, (long) 1);
			}
		}
		//}

		if (EnumerateParameter.FOUR.equals(vo.getDtPaymode())) {
			BlDepositPi dp = new BlDepositPi();
			dp.setPkPi(vo.getPkPi());
			dp.setEuDirect("-1".equals(vo.getEuDirect()) ? "1" : "-1");
			dp.setAmount(vo.getAmount().multiply(new BigDecimal(-1)));
			dp.setDtPaymode("4");
			dp.setPayInfo(vo.getPayInfo());

			balAccoutService.saveMonOperation(dp, user, null, "2", dp.getDtPaymode());
		}
		if("-1".equals(vo.getEuDirect())){
			//退款时，新的那条记录必须清空日结主键和标志，不然如果原来那条被日结了，日结会统计不到这条退款的
			vo.setPkCc(null);
			vo.setFlagCc("0");
			vo.setVoidType("1");
			vo.setVoidEMP(UserContext.getUser().getPkEmp());
			vo.setVoidTIME(new Date());
			vo.setTs(null);
			vo.setCreateTime(null);
			vo.setCreator(null);
			//原来那条的作废标志改为4
			ZsbaBlDeposit oriVo = DataBaseHelper.queryForBean("select * from bl_deposit where pk_depo = ?", ZsbaBlDeposit.class, vo.getPkDepoBack());
			oriVo.setVoidType("4");
			oriVo.setVoidEMP(UserContext.getUser().getPkEmp());
			oriVo.setVoidTIME(new Date());
			DataBaseHelper.updateBeanByPk(oriVo, false);
		}else{
			vo.setVoidType("0");
		}
		vo.setDatePay(new Date());
		ApplicationUtils.setDefaultValue(vo, true);
		DataBaseHelper.insertBean(vo);
		saveBlExtPay(vo);
		return vo;
	}
	
	/**
	 * 更新支付记录关联预交金
	 * @param vo
	 */
	public void saveBlExtPay(ZsbaBlDeposit vo) {
		if (vo.getEuDirect().equals(EnumerateParameter.ONE)) {
			if (vo.getDtPaymode().equals(EnumerateParameter.THREE)
					|| vo.getDtPaymode().equals(EnumerateParameter.SEVEN)
					|| vo.getDtPaymode().equals(EnumerateParameter.EIGHT)) {
				
				//String blExtPaySql = "select * from bl_ext_pay where SERIAL_NO=?";//调湘润的统一支付
				String blExtPaySql = "select * from bl_ext_pay where pk_extpay=?";//调衫德的统一支付
				BlExtPay blExtPay = DataBaseHelper.queryForBean(blExtPaySql, BlExtPay.class, vo.getPayInfo());
				if (blExtPay != null) {
					blExtPay.setPkDepo(vo.getPkDepo());
					//调衫德的一下两行注释
					//blExtPay.setPkPi(vo.getPkPi());
					//blExtPay.setPkPv(vo.getPkPv());
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

		Map<String, Object> map = insPubBlPrePayMapper.getDepositFormData(queryMap);
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

		Map<String, Object> map = insPubBlPrePayMapper
				.getDepositFormDataWham(queryMap);
		if (map == null) {
			throw new BusException("查不到押金单记录");
		}
		if (dtDepttype.equals("08")) {
			// 收费处 收费处只能重打收费处交的预交金票据
			if (dept.getDtDepttype().equals("08")) {
				commonService.confirmUseEmpInv(pkEmpinvoice, (long) 1);
				String capitalAmount = AmountConversionUtil.changePrePay(Double
						.parseDouble(map.get("amount").toString()));
				map.put("capitalAmount", capitalAmount);
				map.put("reptNo", reptNo);
				
				// 退款
				
				
				
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
	 * 交易号：007003002009 重打收据信息
	 * 
	 * @param param
	 * @param user
	 * @return
	 */
	public Map<String, Object> letInvInfo(String param, IUser user) {
		// 作废收据信息
		ZsbaBlDeposit canVo = JsonUtil.readValue(
				JsonUtil.getJsonNode(param, "canVo"), ZsbaBlDeposit.class);

		// 重新缴纳预交金信息
		ZsbaBlDeposit newVo = JsonUtil.readValue(
				JsonUtil.getJsonNode(param, "newVo"), ZsbaBlDeposit.class);
		
		JSONObject jo = JSONObject.fromObject(param);
		String pkDepo = jo.getString("pkDepo");
		String dtDepttype = jo.getString("dtDepttype");
		//String pkEmpinvoice = jo.getString("pkEmpinvoice");
		//String reptNo = jo.getString("reptNo");
		BlDeposit depo = DataBaseHelper.queryForBean(
				"select * from bl_deposit where pk_depo = ?", BlDeposit.class,
				pkDepo);
		BdOuDept dept = DataBaseHelper.queryForBean(
				"select * from bd_ou_dept where pk_dept = ?", BdOuDept.class,
				depo.getPkDept());
		
		String sql = "select count(1) from bl_deposit where rept_no=? and eu_dptype='9' and del_flag='0'";
		int count = DataBaseHelper.queryForScalar(sql, Integer.class,
				newVo.getReptNo());
		if (count > 0) {
			throw new BusException("当前票据号存在重复数据，请前往【票据管理】进行维护！");
		}
		
		Map<String, Object> queryMap = new HashMap<String, Object>();
		queryMap.put("pkDepo", pkDepo);

		Map<String, Object> map = insPubBlPrePayMapper
				.getDepositFormDataWham(queryMap);
		if (map == null) {
			throw new BusException("查不到押金单记录");
		}
		
		if (dtDepttype.equals("08")) {
			//commonService.confirmUseEmpInv(pkEmpinvoice, (long) 1);
			String capitalAmount = AmountConversionUtil.changePrePay(Double
					.parseDouble(map.get("amount").toString()));
			map.put("capitalAmount", capitalAmount);
			//map.put("reptNo", reptNo);
			
			ZsbaBlDeposit oriVo = DataBaseHelper.queryForBean("select * from bl_deposit where pk_depo = ?", ZsbaBlDeposit.class, canVo.getPkDepoBack());
			oriVo.setVoidType("4");
			oriVo.setVoidEMP(UserContext.getUser().getPkEmp());
			oriVo.setVoidTIME(new Date());
			DataBaseHelper.updateBeanByPk(oriVo, false);
			
			// 收款时生成流水号0606
			newVo.setCodeDepo(ApplicationUtils.getCode("0606"));
			// 退款
			save(canVo, user);
			ZsbaBlDeposit blDeposit = save(newVo, user);
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
		} else {
			//目前护士站前端没开发重打的功能
			// 护士站 护士站只能重打护士站交的预交金票据
			if (dept.getDeptType().equals("08")) {
				throw new BusException("该押金单记录是在收费站生成的，请去收费处打印");
			}else{
				//commonService.confirmUseEmpInv(pkEmpinvoice, (long) 1);
				String capitalAmount = AmountConversionUtil.changePrePay(Double
						.parseDouble(map.get("amount").toString()));
				map.put("capitalAmount", capitalAmount);
				//map.put("reptNo", reptNo);
				
				// 收款时生成流水号0606
				newVo.setCodeDepo(ApplicationUtils.getCode("0606"));

				// 退款
				save(canVo, user);
				ZsbaBlDeposit blDeposit = save(newVo, user);
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
			}
		}
		map.put("reptNo", newVo.getReptNo());
		map.put("capitalAmountTwo", AmountConversionUtil.change(Double
				.parseDouble(map.get("amount").toString())));
		map.put("nameEmpPya", newVo.getNameEmpPay());
		return map;
	}
	
	/**
	 * 
	 * @param vo收退款记录的VO
	 */
	public ZsbaBlDeposit save(ZsbaBlDeposit vo, IUser user) {

		BdOuDept dept = DataBaseHelper.queryForBean(
				"select * from bd_ou_dept where pk_dept = ?", BdOuDept.class,
				vo.getPkDept());
		// 收费处打印预交金票据，护士站打印小票

		//if (dept.getDtDepttype().equals("08")) {
			String pkEmpInv = vo.getPkEmpinvoice();
			if (CommonUtils.isEmptyString(pkEmpInv)) {
				throw new BusException("票据领用主键为空，请检查票据！");
			}
			if (vo.getEuDirect().equals("1")) {
				commonService.confirmUseEmpInv(pkEmpInv, (long) 1);
			}
		//}

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
		ApplicationUtils.setDefaultValue(vo, true);
		if("-1".equals(vo.getEuDirect())){
			vo.setVoidType("2");
			vo.setVoidEMP(UserContext.getUser().getPkEmp());
			vo.setVoidTIME(new Date());
		}else{
			vo.setVoidType("0");
		}
		DataBaseHelper.insertBean(vo);

		saveBlExtPay(vo);
		return vo;
	}
	
	/**
	 * 将门诊日间手术费用导入住院明细，并生成押金单
	 * @param vo
	 * @param user
	 */
	public void saveMzDayopCost(String param, IUser user){
		User u = (User)user;
		SaveMzDayOpCostParam vo = JsonUtil.readValue(param, SaveMzDayOpCostParam.class);
		//插入押金单
		for(MzDayInvData inv : vo.getMzDayInvDataList()){
			ZsbaBlDeposit depo = new ZsbaBlDeposit();
			depo.setEuDptype("9");
			depo.setEuDirect("1");
			depo.setEuPvtype("3");
			depo.setPkPv(vo.getPkPv());
			depo.setPkPi(vo.getPkPi());
			depo.setAmount(new BigDecimal(inv.getChargeTotal()));
			depo.setDtPaymode("91");
			depo.setDatePay(new Date());
			depo.setPkDept(UserContext.getUser().getPkDept());
			depo.setPkEmpPay(UserContext.getUser().getPkEmp());
			depo.setNameEmpPay(UserContext.getUser().getNameEmp());
			depo.setFlagAcc("0");
			depo.setFlagSettle("0");
			depo.setFlagCc("0");
			depo.setFlagReptBack("0");
			depo.setFlagCcRept("0");
			depo.setVoidType("0");
			if(inv.getReceiptSn().length()>8){//新门诊存的是门诊发票主键 32位
				depo.setPkSettleOp(inv.getReceiptSn());
				depo.setNote("日间手术新门诊发票内部转账");
			}else{
				depo.setNote("日间手术门诊发票内部转账");
				depo.setMzPatientId(inv.getMzId());
				depo.setLedgerSn(inv.getLedgerSn());
				depo.setReceiptSn(inv.getReceiptSn());
			}
			DataBaseHelper.insertBean(depo);
		}
		
		String bqSql = "select * from BD_OU_DEPT where code_dept = ? and del_flag = '0'";
		BdOuDept dept = DataBaseHelper.queryForBean(bqSql, BdOuDept.class, vo.getPkDeptSn());
		
		//插入费用明细
		if(vo.getMzDayDetailsDataList().get(0).getPkCgop()==null){
			//旧系统
			for(MzDayDetailsData mx :  vo.getMzDayDetailsDataList()){
				
				StringBuffer sbSql = new StringBuffer("select item.flag_pd,  item.pk_Itemcate, item.pk_item, item.name, item.pk_unit,  item.spec, au.code as code_audit, invitem.code as code_bill ");
				sbSql.append(" from bd_item item left join bd_audit au on item.pk_audit = au.pk_audit");
				sbSql.append(" inner join BD_INVCATE_ITEMCATE cate on cate.pk_itemcate = item.pk_itemcate");
				sbSql.append(" inner join BD_INVCATE_ITEM invitem on invitem.pk_invcateitem = cate.pk_invcateitem");
				sbSql.append(" where item.code_ext = ?");
				sbSql.append(" union all ");
				sbSql.append(" select DISTINCT '1' as flag_pd,  item.pk_Itemcate, item.pk_pd as pk_item, item.name, item.pk_unit_wt as pk_unit,  item.spec, au.code as code_audit, invitem.code as codeBill from bd_invcate_item invitem");
				sbSql.append(" inner join BD_INVCATE_ITEMCATE cate on invitem.pk_invcateitem = cate.PK_INVCATEITEM");
				sbSql.append(" inner join BD_INVCATE inv on invitem.pk_invcate=inv.PK_INVCATE");
				sbSql.append(" inner join bd_pd item on cate.PK_ITEMCATE = item.pk_itemcate");
				sbSql.append(" left join bd_audit au on item.pk_audit = au.pk_audit");
				sbSql.append(" where item.code_ext = ?");
				Map<String, Object>  map = new HashMap<String, Object>();
				map = DataBaseHelper.queryForMap(sbSql.toString(), (mx.getChargeCode()+mx.getSerialNo()).trim(), mx.getChargeCode());
				if(map==null){
					//抛出异常
					throw new BusException(mx.getItemName()+"（"+mx.getChargeCode()+")新系统找不到对应的账单码、核算码！");
				}
				BlIpDt dt = new BlIpDt();
				dt.setPkPi(vo.getPkPi());
				dt.setPkPv(vo.getPkPv());
				dt.setFlagPd(map.get("flagPd").toString());
				dt.setCodeBill(map.get("codeBill").toString());
				dt.setCodeAudit(map.get("codeAudit").toString());
				dt.setPkItemcate(map.get("pkItemcate").toString());
				dt.setPkItem(map.get("pkItem").toString());
				dt.setNameCg(map.get("name").toString());
				if(map.get("pkUnit")!=null)
					dt.setPkUnit(map.get("pkUnit").toString());
				if(map.get("spec")!=null)
					dt.setSpec(map.get("spec").toString());
				dt.setPriceOrg(Double.parseDouble(mx.getOrigPrice().toString()));
				dt.setPrice(Double.parseDouble(mx.getChargePrice()));
				dt.setQuan(Double.parseDouble(mx.getChargeAmount().toString()));
				DecimalFormat df = new DecimalFormat("#.##"); 
				dt.setAmount(Double.parseDouble(df.format(dt.getPrice()*dt.getQuan())));
				dt.setRatioSelf(1.0);
				dt.setAmountHppi(0d);
				dt.setRatioDisc(1.0);
				dt.setRatioAdd(0d);
				dt.setAmountAdd(0d);
				dt.setAmountPi(dt.getAmount());
				dt.setPkOrgApp(UserContext.getUser().getPkOrg());
				dt.setPkDeptApp(UserContext.getUser().getPkDept());
				dt.setPkDeptNsApp(dept.getPkDept());
				dt.setPkEmpApp(u.getPkEmp());
				dt.setNameEmpApp(u.getNameEmp());
				dt.setPkOrgEx(u.getPkOrg());
				dt.setPkDeptEx(u.getPkDept());
				dt.setPkEmpEx(u.getPkEmp());
				dt.setNameEmpEx(u.getNameEmp());
				dt.setEuBltype("99");
				dt.setDateHap(new Date());
				dt.setFlagSettle("0");
				dt.setFlagInsu("0");
				dt.setDosage(Double.parseDouble(mx.getDosage()!=null?mx.getDosage():"0"));
				dt.setUnitDos(mx.getDosageUnit());
				dt.setNameSupply(mx.getSupplyName());//用法
				dt.setNameFreq(mx.getFreqName());//频次
				dt.setDateEntry(new Date());
				dt.setCodeCg(ApplicationUtils.getCode("0601"));
				dt.setDateCg(new Date());
				dt.setPkDeptCg(u.getPkDept());
				dt.setPkEmpCg(u.getPkEmp());
				dt.setNameEmpCg(u.getNameEmp());
				if(map.get("flagPd").toString().equals("1")){
					dt.setPkPd(map.get("pkItem").toString());
				}
				DataBaseHelper.insertBean(dt);
			}
		}else{
			//新系统
			for(MzDayInvData inv : vo.getMzDayInvDataList()){
				String sql = "select a.*, f.name as name_freq, c.code_supply, c.dosage, c.pk_unit_dos,  c.spec, d.name, "+
						" c.ords, a.amount, b.name_dept,  e.name as name_supply  "+
						" from BL_OP_DT a left join BD_OU_DEPT b on a.pk_dept_app = b.pk_dept "+
						" left join cn_order c on a.pk_cnord = c.pk_cnord  "+
						" left join bd_unit d on c.pk_unit = d.pk_unit"+
						" left join bd_supply e on c.code_supply = e.code"+
						" left join BD_TERM_FREQ f on c.code_freq = f.code "+
						" where a.pk_settle = ? and a.del_flag = '0'";
				List<Map<String, Object>> mapList = DataBaseHelper.queryForList(sql, inv.getReceiptSn());
				for (Map<String, Object> map : mapList) {
					BlIpDt dt = new BlIpDt();
					dt.setPkPi(vo.getPkPi());
					dt.setPkPv(vo.getPkPv());
					dt.setFlagPd(map.get("flagPd").toString());
					dt.setCodeBill(map.get("codeBill").toString());
					dt.setCodeAudit(map.get("codeAudit").toString());
					dt.setPkItemcate(map.get("pkItemcate").toString());
					dt.setPkItem(map.get("pkItem").toString());
					dt.setNameCg(map.get("nameCg").toString());
					if(map.get("pkUnit")!=null)
						dt.setPkUnit(map.get("pkUnit").toString());
					if(map.get("spec")!=null)
						dt.setSpec(map.get("spec").toString());
					dt.setPriceOrg(Double.parseDouble(map.get("priceOrg").toString()));
					dt.setPrice(Double.parseDouble(map.get("price").toString()));
					dt.setQuan(Double.parseDouble(map.get("quan").toString()));
					dt.setAmount(Double.parseDouble(map.get("amount").toString()));
					dt.setRatioSelf(Double.parseDouble(map.get("ratioSelf").toString()));
					dt.setAmountHppi(Double.parseDouble(map.get("amountHppi").toString()));
					dt.setRatioDisc(Double.parseDouble(map.get("ratioDisc").toString()));
					dt.setRatioAdd(Double.parseDouble(map.get("ratioAdd").toString()));
					dt.setAmountAdd(Double.parseDouble(map.get("amountAdd").toString()));
					dt.setAmountPi(Double.parseDouble(map.get("amountPi").toString()));
					dt.setPkOrgApp(map.get("pkOrgApp").toString());
					dt.setPkDeptApp(map.get("pkDeptApp").toString());
					dt.setPkDeptNsApp(dept.getPkDept());
					dt.setPkEmpApp(map.get("pkEmpApp").toString());
					dt.setNameEmpApp(map.get("nameEmpApp").toString());
					dt.setPkOrgEx(map.get("pkOrgEx").toString());
					dt.setPkDeptEx(map.get("pkDeptEx").toString());
					dt.setPkEmpEx(map.get("pkEmpEx")==null?null:map.get("pkEmpEx").toString());
					dt.setNameEmpEx(map.get("nameEmpEx")==null?null:map.get("nameEmpEx").toString());
					dt.setEuBltype("99");
					dt.setDateHap(new Date());
					dt.setFlagSettle("0");
					dt.setFlagInsu("0");
					dt.setDosage(Double.parseDouble(map.get("dosage")!=null?map.get("dosage").toString():"0"));
					if(map.get("name")!=null){
						dt.setUnitDos(map.get("name").toString()); //用量单位
					}
					if(map.get("nameSupply")!=null){
						dt.setNameSupply(map.get("nameSupply").toString());//用法
					}
					if(map.get("nameFreq")!=null){
						dt.setNameFreq(map.get("nameFreq").toString());//频次
					}
					
					dt.setDateEntry(new Date());
					dt.setCodeCg(ApplicationUtils.getCode("0601"));
					dt.setDateCg(new Date());
					dt.setPkDeptCg(map.get("pkDeptCg").toString());
					dt.setPkEmpCg(map.get("pkEmpCg").toString());
					dt.setNameEmpCg(map.get("nameEmpCg").toString());
					dt.setPkPd(map.get("pkPd")==null?null:map.get("pkPd").toString());
					if(map.get("packSize")!=null){
						dt.setPackSize(Double.parseDouble(map.get("packSize").toString()));
					}
					DataBaseHelper.insertBean(dt);
				}

			}
		}

	}

	/**
	 * 补打押金单，将新的票据号填到押金记录上(目前用于补打自助机、微信)
	 * @param param
	 * @param user
	 */
	public Map<String, Object> saveBlDepositBD(String param, IUser user){
		JSONObject jo = JSONObject.fromObject(param);
		String pkDepo = jo.getString("pkDepo");
		String reptNo = jo.getString("reptNo");
		String pkEmpinvoice = jo.getString("pkEmpinvoice");
		
		String sql = "select count(1) from bl_deposit where rept_no=? and eu_dptype='9' and del_flag='0'";
		int count = DataBaseHelper.queryForScalar(sql, Integer.class,
				reptNo);
		if (count > 0) {
			throw new BusException("当前票据号存在重复数据，请前往【票据管理】进行维护！");
		}
		
		BlDeposit depo = DataBaseHelper.queryForBean(
				"select * from bl_deposit where pk_depo = ?", BlDeposit.class,
				pkDepo);
		depo.setReptNo(reptNo);
		depo.setPkEmpinvoice(pkEmpinvoice);
		
		String pkEmpInv = pkEmpinvoice;
		if (CommonUtils.isEmptyString(pkEmpInv)) {
			throw new BusException("票据领用主键为空，请检查票据！");
		}
		commonService.confirmUseEmpInv(pkEmpInv, (long) 1);
		
		DataBaseHelper.updateBeanByPk(depo);
		
		Map<String, Object> queryMap = new HashMap<String, Object>();
		queryMap.put("pkDepo", pkDepo);

		Map<String, Object> map = insPubBlPrePayMapper
				.getDepositFormDataWham(queryMap);
		if (map == null) {
			throw new BusException("查不到押金单记录");
		}
		String capitalAmount = AmountConversionUtil.changePrePay(Double
				.parseDouble(map.get("amount").toString()));
		map.put("capitalAmount", capitalAmount);
		map.put("reptNo", reptNo);
		map.put("capitalAmountTwo", AmountConversionUtil.change(Double
				.parseDouble(map.get("amount").toString())));
		//map.put("nameEmpPya", vo.getNameEmpPay());
		return map;
	}
	
	/**
	 * 修改付款方式
	 * 功能号：022003002041
	 * @param param
	 * @param user
	 */
	public void savePayMode(String param, IUser user){
		ZsbaBlDeposit vo = JsonUtil.readValue(param, ZsbaBlDeposit.class);
		if(vo!=null && vo.getPkDepo()!=null && vo.getDtPaymode()!=null){
			ZsbaBlDeposit depo = DataBaseHelper.queryForBean("select * from bl_deposit where pk_depo = ?", ZsbaBlDeposit.class, vo.getPkDepo());
			if(depo!=null){
				if(depo.getFlagCc().equals("0")){
					ZsbaBlUpPaymode upPay = new ZsbaBlUpPaymode();
					upPay.setPkDepo(depo.getPkDepo());
					upPay.setOldPaymode(depo.getDtPaymode());
					upPay.setNewPaymode(vo.getDtPaymode());
					upPay.setNameEmp(UserContext.getUser().getNameEmp());
					DataBaseHelper.updateBeanByPk(vo, false);
					DataBaseHelper.insertBean(upPay);
					
				}
			}
		}
	}
	
    /**
     * @return java.util.Map<java.lang.String, java.lang.Object>
     * @Description 根据BLdepost 表收款主键查询出 Bl的post收款记录，和第三方收款数据
     * @auther wuqiang
     * @Date 2019-10-26
     * @Param [param, user]
     */
    public  Map<String, Object> queryRefundParam(String param, IUser user) {
        String pkDepo=JsonUtil.getFieldValue(param,"pkDepo") ;
        String  sql="select * from BL_DEPOSIT where PK_DEPO=?";
        BlDeposit blDeposit=DataBaseHelper.queryForBean(sql,BlDeposit.class,pkDepo);
        Map<String,Object> map=new HashMap<>(2);
        map.put("blDeposit",blDeposit);
        sql="select *from BL_EXT_PAY where FLAG_PAY='1' and REFUND_NO is null and (PK_DEPO=? or SERIAL_NO=?) ";
        BlExtPay blExtPay=DataBaseHelper.queryForBean(sql,BlExtPay.class,pkDepo,blDeposit.getPayInfo());
        map.put("blExtPay",blExtPay);
        return map;
    }
}
