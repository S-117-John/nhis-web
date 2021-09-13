package com.zebone.nhis.bl.pub.pay.service;

import com.google.common.collect.Maps;
import com.zebone.nhis.bl.pub.service.CgQryMaintainService;
import com.zebone.nhis.bl.pub.service.OpCgPubService;
import com.zebone.nhis.bl.pub.vo.ClinicPayInputVo;
import com.zebone.nhis.bl.pub.vo.QrCodeVo;
import com.zebone.nhis.common.module.bl.BlSettleDetail;
import com.zebone.nhis.common.module.bl.TempBlOpDt;
import com.zebone.nhis.common.module.bl.TempBlSettle;
import com.zebone.nhis.common.module.bl.opcg.BlOpDt;
import com.zebone.nhis.common.module.pay.BlExtPay;
import com.zebone.nhis.common.module.pv.PvInsurance;
import com.zebone.nhis.common.pay.PayProxy;
import com.zebone.nhis.common.support.ApplicationUtils;
import com.zebone.nhis.common.support.CommonUtils;
import com.zebone.nhis.common.support.EnumerateParameter;
import com.zebone.nhis.common.support.MathUtils;
import com.zebone.platform.common.support.User;
import com.zebone.platform.framework.support.IUser;
import com.zebone.platform.modules.dao.DataBaseHelper;
import com.zebone.platform.modules.exception.BusException;
import com.zebone.platform.modules.utils.JsonUtil;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;

/**
 * 处理诊间支付业务
 * @author Administrator
 */
@Service
public class ClinicPayService {
	
	@Autowired
	private CgQryMaintainService cgQryMaintainService;
	
	@Autowired
	private OpCgPubService opCgPubService;

	/**
	 * 查询本次就诊待支付费用
	 * @param param
	 * @param user
	 * @return eu_drugtype --0西药，1成药，2草药，null检治项目
	 * 		           amt --对应金额
	 */
	public List<Map<String, Object>> queryWaitPayfees(String param, IUser user) {
		String pkPv = JsonUtil.readValue(param, String.class);
		if(StringUtils.isEmpty(pkPv)){
			throw new BusException("未传递本次就诊主键");
		}
		
		StringBuffer sBuffer = new StringBuffer();
		sBuffer.append("select pd.eu_drugtype, sum(dt.amount) amt from bl_op_dt dt ");
		sBuffer.append(" left outer join bd_pd pd on dt.pk_pd=pd.pk_pd ");
		sBuffer.append(" where dt.pk_pv=? and dt.flag_settle='0' and dt.flag_acc='0' ");
		sBuffer.append(" group by pd.eu_drugtype ");
		
		return DataBaseHelper.queryForList(sBuffer.toString(), pkPv);
	}
	
	
	/**
	 * 诊间支付生成微信和支付宝二维码
	 * @param param
	 * @param user
	 */
	public QrCodeVo clinicCreateQrcodePay(String param, IUser user){
		ClinicPayInputVo inputParam = JsonUtil.readValue(param, ClinicPayInputVo.class);
		QrCodeVo qrCodeVo = new QrCodeVo();
		String pkOrg = ((User) user).getPkOrg();
		// 0 选择要收费中没有生成临时结算的记录Temp_bl_op_dt和Temp_bl_settle和第三方支付订单表，已做非空判断
		List<BlOpDt> blOpDts = queryBlOpDtAndValidate(inputParam);
		
		// 1 判断前台传入的收费项目是否已经在临时表中存在，存在的话返回项目关联的临时表(list) --暂定用pkPI,pkPV,bd_item来查
		List<String> existPkExtpays = validateIsExistInTemp(blOpDts);
		if(!CollectionUtils.isEmpty(existPkExtpays)){
			qrCodeVo.setPkExtpays(existPkExtpays);
			return qrCodeVo;
		}
		
		/*
		 *根据医保计划和内部优惠处理收费项目
		 *    --A 累加项目金额，处理内部医保优惠和患者优惠的部分
		 *    --B 考虑内部医保计划,每次调用医保医保返回的分摊金额；
		 */
		Map<String, Double> reducedPrice = reducedPrice(blOpDts);
		reducedPrice = insuranceReduceAmout(inputParam, reducedPrice, pkOrg);
		
		// 2 生成Temp_bl_settle和Temp_bl_op_dt
		TempBlSettle tempBlSettle = createTempBlSettle(inputParam, pkOrg, reducedPrice);
		
		//createTempBlOpdts(blOpDts, tempBlSettle);		//和成哥确认，不写表temp_bl_op_dt
		
		// 3  生成第三方支付订单表
		createBlExtPay(inputParam, tempBlSettle);
		
		// 4 生成支付宝/微信二维码
		// 4.1 微信支付返回的二维码   --订单号    后台生成
		String weixinQrCode = createWeixinPayQrcode(tempBlSettle);
		if(StringUtils.isEmpty(weixinQrCode)){
			throw new BusException("微信二维码生成失败");
		}
		qrCodeVo.setWeixinQrCode(weixinQrCode);
		
		// 4.2 支付宝返回的二维码    --订单号  后台生成
		String aliPayQrCode = createAliPayQrcode(tempBlSettle);
		if(StringUtils.isEmpty(aliPayQrCode)){
			throw new BusException("支付宝二维码生成失败");
		}
		qrCodeVo.setAliPayQrCode(aliPayQrCode);
		
		return qrCodeVo;
	}
	
	/*
	 * 根据前台传入的明细主键查询明细记录。并进行数据校验码。
	 */
	private List<BlOpDt> queryBlOpDtAndValidate(ClinicPayInputVo inputParam) {
		List<BlOpDt> blOpDts = new ArrayList<>();
		
		List<String> pkPress = inputParam.getPkPress();   //处方主键
		List<String> pkCnords = inputParam.getPkCnords(); //医嘱主键
		
		// 1 根据处方关联医嘱查询项目明细
		Set<String> pkPresSet = null;
		List<BlOpDt> presBlOpDts = null;
		if(pkPress != null){
			pkPresSet = new HashSet<>(pkPress);
			// 根据处方主键关联医嘱表查询项目明细
			String  pkPresSql = "select dt.* from cn_order orders "
					+ " inner join bl_op_dt dt on dt.pk_cnord = orders.pk_cnord "
					+ " left join bl_settle settle on settle.pk_settle = dt.pk_settle and settle.flag_canc = '0' and amount_st > 0 "
					+ " where orders.PK_PRES in ( "
					+ CommonUtils.convertSetToSqlInPart(pkPresSet, "PK_PRES") + ") ";
			presBlOpDts = DataBaseHelper.queryForList(pkPresSql, BlOpDt.class);
		}
		if(presBlOpDts != null && presBlOpDts.size() > 0){
			blOpDts.addAll(presBlOpDts);
		}
		
		// 2 根据医嘱查询项目明细
		Set<String> pkCnordSet = null;
		List<BlOpDt> cnordBlOpDts = null;
		if(pkCnords != null){
			pkCnordSet = new HashSet<>(pkCnords);
			// 根据医嘱主键查项目明细
			String pkCnordSql = "select * from bl_op_dt where pk_cnord in ( "
					+ CommonUtils.convertSetToSqlInPart(pkCnordSet, "pk_cnord") + ") ";
			
			cnordBlOpDts = DataBaseHelper.queryForList(pkCnordSql, BlOpDt.class);
		}
		if(cnordBlOpDts != null && cnordBlOpDts.size() > 0){
			blOpDts.addAll(cnordBlOpDts);
		}
		
		// 3 处理项目明细
		if(CollectionUtils.isEmpty(blOpDts)){
			throw new BusException("收费项目为空，不能进行诊间支付!");
		}
		return blOpDts;
	}
	
	/*
	 * 检查传入的要诊间结算的项目中是否存在已经生成二维码的项目
	 */
	private List<String> validateIsExistInTemp(List<BlOpDt> blOpDts) {
		// 医嘱集合
		Set<String> pkCnordSet = new HashSet<>();
		for (BlOpDt blOpDt : blOpDts) {
			pkCnordSet.add(blOpDt.getPkCnord());
		}
		
		// 根据医嘱主键查临时项目明细
		String querySql = "select * from temp_bl_op_dt where del_flag='0' and pk_cnord in ("
											+ CommonUtils.convertSetToSqlInPart(pkCnordSet, "pk_cnord") + ")";
		
		List<TempBlOpDt> tempBlOpDts = DataBaseHelper.queryForList(querySql, TempBlOpDt.class);
		
		if(tempBlOpDts == null || tempBlOpDts.size() == 0){
			return null;
		}
		
		// 根据pkItem来遍历收费项目是否存在
		Set<String> pkItemSet = new HashSet<>();
		for (TempBlOpDt tempBlOpDt : tempBlOpDts) {
			pkItemSet.add(tempBlOpDt.getPkItem());
		}
		// 根据已结算的收费项目来查询关联的第三方表
		Set<String> isExistSet = new HashSet<>();
		for (BlOpDt bod : blOpDts) {
			if(pkItemSet.contains(bod.getPkItem())){
				isExistSet.add(bod.getPkItem());
			}
		}
		String extSql = "select * from bl_ext_pay ext inner join TEMP_BL_OP_DT dt on ext.serial_no=dt.pk_settle "
							+ " where dt.pk_item in (" + CommonUtils.convertSetToSqlInPart(isExistSet, "pk_item") + ")";
		List<BlExtPay> blExtPays = DataBaseHelper.queryForList(extSql, BlExtPay.class);
		
		// 取第三方表的主键返回
		List<String> pkExtpays = new ArrayList<>();
		if(blExtPays != null){
			for (BlExtPay blExtPay : blExtPays) {
				pkExtpays.add(blExtPay.getPkExtpay());
			}
		}
		
		return pkExtpays;
	}


	/**
	 * 生成微信支付二维码
	 * @param param
	 * @param user
	 * @return
	 */
	public String createWeixinPayQrcode(TempBlSettle tempBlSettle){
		//业务主键  --类似于商品id或者订单号
//		String pkBus = "weixin" + System.currentTimeMillis() + (long) (Math.random() * 10000000L);
		String pkBus = tempBlSettle.getPkTempSettle();
		//商品描述
		String body = "诊间支付项目";
		//总金额
		Double totalFee = tempBlSettle.getAmountPi();    // 微信支付的时候传入的是分 
		//回调url
		String notifyUrl = PayProxy.notifyDomain + "/src/main/static/clinicPay/weixinPay/notify";
		//额外参数
		String attach = "";
		//交易流水号
//		String serialNum = NHISUUID.getKeyId();
		String weixinQrCode = PayProxy.getWeixinPayQrcode("weixin" + pkBus, body, pkBus, totalFee, notifyUrl, "127.0.0.1", attach);
		return weixinQrCode;
	}
	
	/**
	 * 生成支付宝支付二维码
	 * @param param
	 * @param user
	 * @return
	 */
	public String createAliPayQrcode(TempBlSettle tempBlSettle){
		//业务主键  --类似于商品id或者订单号
//		String orderNo = "alipay" + System.currentTimeMillis() + (long) (Math.random() * 10000000L);
		String orderNo = "alipay" + tempBlSettle.getPkTempSettle();
		//商品描述
		String subject = "诊间支付项目";
		//商品body
		String body = "医嘱项目";
		//总金额
		Double totalFee = tempBlSettle.getAmountPi();
		//回调url
		String notifyUrl = PayProxy.notifyDomain + "/src/main/static/clinicPay/weixinPay/notify";
		String aliPayQrCode = PayProxy.getAlipayQrcode(orderNo, subject, body, "" + totalFee, notifyUrl);
		return aliPayQrCode;
	}
	
	/*
	 * 生成第三方订单表。根据pkPi和pkPv关联生成的临时项目明细表和临时结算表
	 */
	private BlExtPay createBlExtPay(ClinicPayInputVo inputParam, TempBlSettle tempBlSettle) {
		BlExtPay blExtPay = new BlExtPay();
		blExtPay.setAmount(new BigDecimal(tempBlSettle.getAmountPi()));
		blExtPay.setCreateTime(new Date());
		blExtPay.setFlagPay(EnumerateParameter.ZERO); //未支付状态
		blExtPay.setPkPi(inputParam.getPkPi());
		blExtPay.setPkPv(inputParam.getPkPv());
		blExtPay.setPkBus(tempBlSettle.getPkTempSettle());    //临时缴款主键--业务主键
		blExtPay.setSerialNo(tempBlSettle.getPkTempSettle()); //临时缴款主键当成订单号
	    ApplicationUtils.setDefaultValue(blExtPay, true);
	    DataBaseHelper.insertBean(blExtPay);
	    return blExtPay;
	}
	
	/**
	 * 调用价格策略服务接口，根据患者主医保、辅医保、患者分类和费用明细计算结算信息
	 * @param inputParam
	 * @param reducedPrice
	 * @param pkOrg
	 */
	public Map<String, Double> insuranceReduceAmout(ClinicPayInputVo inputParam, Map<String, Double> reducedPrice, String pkOrg)
	{
		double amountPi = reducedPrice.get("amountPi");
		double amountInsu = reducedPrice.get("amountInsu");
		
		Map<String, Object> mapParam = new HashMap<>();
		mapParam.put("pkPv", inputParam.getPkPv());
		mapParam.put("pkOrg", pkOrg);
		List<PvInsurance> pvInsurances = cgQryMaintainService.qryPkHpByPkPv(mapParam);
		List<BlSettleDetail> details = opCgPubService.countMedicare(
															pvInsurances, new BigDecimal(amountPi), inputParam.getEuPvtype(), null);
		
		// 医保分摊金额
		double shareAmount = 0d;
		for (BlSettleDetail blSettleDetail : details) {
			shareAmount += blSettleDetail.getAmount();
		}
		// 医保分摊金额大于0，修改患者自费和医保支付的金额
		if (((Double) shareAmount).compareTo(0d) == 1) {
			amountPi =  amountPi - shareAmount;// 患者自费
			amountInsu = amountInsu + shareAmount;// 医保支付
		}
		reducedPrice.remove("amountPi");
		reducedPrice.remove("amountInsu");
		reducedPrice.put("amountPi",amountPi);
		reducedPrice.put("amountInsu",amountInsu);
		
		return reducedPrice;
	}

	/*
	 * 累加项目金额，处理内部医保优惠和患者优惠的部分
	 * @param blOpDts
	 * @return Map<String, Double>
	 */
	public Map<String, Double> reducedPrice(List<BlOpDt> blOpDts) {
		Map<String, Double> amountMap = new HashMap<>();
		
		double amountSt = 0d;// 结算金额
		double amountPi = 0d;// 患者自付金额
		double amountInsu = 0d;// 医保支付金额
		double discAmount = 0d;// 患者优惠金额

		for (BlOpDt bpt : blOpDts) {
			amountSt += bpt.getAmount();
			amountPi += bpt.getAmountPi();
			//内部医保优惠计费部分
			amountInsu += ((bpt.getPriceOrg() - bpt.getPrice()) + (bpt.getPriceOrg() * (1 - bpt.getRatioSelf()))) * bpt.getQuan();
			if (bpt.getPkDisc() != null) {
				//患者优惠
				discAmount += MathUtils.mul(MathUtils.mul(bpt.getPriceOrg(), 1 - bpt.getRatioDisc()), bpt.getQuan());
			}
		}
		
		amountMap.put("amountSt", amountSt); // 结算金额
		amountMap.put("amountPi", amountPi); // 患者自付金额
		amountMap.put("amountInsu", amountInsu); // 医保支付金额
		amountMap.put("discAmount", discAmount); // 患者
		
		return amountMap;
	}

	/*
	 * 生成临时结算表temp_bl_settle并插入表中
	 */
	private TempBlSettle createTempBlSettle(ClinicPayInputVo inputParam, String pkOrg, Map<String, Double> reducedPrice) {
		double amountSt = reducedPrice.get("amountSt");// 结算金额
		double amountPi = reducedPrice.get("amountPi");// 患者自付金额
		double amountInsu = reducedPrice.get("amountInsu");// 医保支付金额
		double discAmount = reducedPrice.get("discAmount");// 患者优惠金额
		
		TempBlSettle tempBlSettle = new TempBlSettle();
		tempBlSettle.setAmountSt(amountSt);  // 结算金额
		tempBlSettle.setAmountPi(amountPi);
		tempBlSettle.setAmountInsu(amountInsu + discAmount); // 经过医保计划处理后的医保分摊
		tempBlSettle.setPkOrg(pkOrg);
		tempBlSettle.setPkPi(inputParam.getPkPi());
		tempBlSettle.setPkPv(inputParam.getPkPv());
		tempBlSettle.setDtSttype("0X");  // 结算类型 --诊间支付：挂号和结算合在一起
		tempBlSettle.setDateSt(new Date());
		tempBlSettle.setPkOrgSt(inputParam.getPkOrgSt());
		tempBlSettle.setPkDeptSt(inputParam.getPkDeptSt());
		tempBlSettle.setPkEmpSt(inputParam.getPkEmp());
		tempBlSettle.setNameEmpSt(inputParam.getNameEmp());
		ApplicationUtils.setDefaultValue(tempBlSettle, true);
		DataBaseHelper.insertBean(tempBlSettle);
		return tempBlSettle;
	}

	/*
	 * 生成临时项目收费表并插入数据库
	 */
	private List<TempBlOpDt> createTempBlOpdts(List<BlOpDt> blOpDts, TempBlSettle tempBlSettle) {
		List<TempBlOpDt> tempBlOpDts = new ArrayList<>();
		for (BlOpDt blOpDt : blOpDts) {
			TempBlOpDt tempBlOpDt = new TempBlOpDt();
			try {
				BeanUtils.copyProperties(tempBlOpDt, blOpDt);
			} catch (Exception exception) {
				throw new BusException("临时表项目表复制项目表中属性出错，请检查字段。");
			}
			tempBlOpDt.setPkSettle(tempBlSettle.getPkTempSettle()); // 把临时的支付主键传递进来
			
			tempBlOpDt.setPkTempCgop(blOpDt.getPkCgop());  //完全复制前台传递的项目主键
			tempBlOpDt.setCreateTime(new Date());
			tempBlOpDt.setDelFlag(EnumerateParameter.ZERO);
			ApplicationUtils.setDefaultValue(tempBlOpDt, false);
			
			tempBlOpDts.add(tempBlOpDt);
		}
		DataBaseHelper.batchUpdate(DataBaseHelper.getInsertSql(TempBlOpDt.class), tempBlOpDts);
		return tempBlOpDts;
	}
	
	public static void main1(String[] args) {
		//业务主键  --类似于商品id或者订单号
		String pkBus = "weixin" + System.currentTimeMillis();
		System.out.println(pkBus);
		String body = "诊间支付项目test";
		Double totalFee = 0.02d;
//		String notifyUrl = PayProxy.notifyDomain + "/static/pay/weixinPay/notify";
		String notifyUrl = "http:192.168.2.209:8080/nhis/static/pay/weixinPay/notify";
		String attach = "";
//		String serialNum = NHISUUID.getKeyId();
		Map<String,String> resultMap = Maps.newHashMap();
		resultMap.put("weixinQrCode", PayProxy.getWeixinPayQrcode(pkBus, body, pkBus, totalFee, notifyUrl, "127.0.0.1", attach));
		System.out.println(resultMap);
	}
	public static void main(String[] args) {
		String pkBus = "alipay" + System.currentTimeMillis();
		System.out.println(pkBus);
		String body = "诊间支付项目";
		Double totalFee = 0.01d;
//		String notifyUrl = PayProxy.notifyDomain + "/static/pay/weixinPay/notify";
		String notifyUrl = "http:192.168.2.209:8080/nhis/static/pay/weixinPay/notify";
		Map<String,String> resultMap = Maps.newHashMap();
		resultMap.put("qrCode",PayProxy.getAlipayQrcode(pkBus, "title1233", body, totalFee+"", notifyUrl));
		System.out.println(resultMap);
	}
}
