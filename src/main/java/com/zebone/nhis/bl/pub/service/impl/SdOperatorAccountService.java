package com.zebone.nhis.bl.pub.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.zebone.nhis.common.support.EnumerateParameter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.zebone.nhis.bl.pub.dao.BlIpCcMapper;
import com.zebone.nhis.bl.pub.dao.OpblccMapper;
import com.zebone.nhis.bl.pub.service.OperatorAccountService;
import com.zebone.nhis.bl.pub.vo.BlCcDt;
import com.zebone.nhis.bl.pub.vo.InvInfo;
import com.zebone.nhis.bl.pub.vo.InvalidStInv;
import com.zebone.nhis.bl.pub.vo.OpBlCcVo;
import com.zebone.nhis.bl.pub.vo.PayInfoSd;
import com.zebone.nhis.common.module.bl.BlCcPay;
import com.zebone.nhis.common.support.CommonUtils;
import com.zebone.nhis.common.support.DateUtils;
import com.zebone.nhis.common.support.MathUtils;
import com.zebone.platform.common.support.UserContext;
/**
 * 深大项目住院、门诊日结账个性化服务
 * @author Administrator
 *
 */
@Service("SdOperatorAccount")
public class SdOperatorAccountService implements OperatorAccountService {
	
	@Autowired
	private BlIpCcMapper blIpCcMapper;
	
	@Autowired
	private OpblccMapper opblccMapper;

	//是否开启全国医保，开启时查询全国医保结账数据
	@Value("#{applicationProperties['qgyb.enable']}")
	private String qgybEnable;

	@Override
	public BlCcDt getIpOperAccInfo(BlCcDt param) {
		
		Map<String,Object> paramMap = new HashMap<>(16);
		if(CommonUtils.isEmptyString(param.getBlCc().getPkCc())){
			paramMap.put("dateEnd", DateUtils.formatDate(param.getBlCc().getDateEnd(), "yyyyMMddHHmmss"));
			paramMap.put("pkEmp",  UserContext.getUser().getPkEmp());
			paramMap.put("pkOrg",  UserContext.getUser().getPkOrg());
			paramMap.put("pkCc", "");
		}else{
			paramMap.put("pkCc", param.getBlCc().getPkCc());
		}
		
		//收款集合信息
		List<PayInfoSd> payInfoList = new ArrayList<>();
		
		//查询参与结算的预交金
//		Map<String,Object> amtStPrepMap = blIpCcMapper.qryAmtStPrep(paramMap);
//		PayInfoSd paySd = new PayInfoSd();
//		paySd.setPayType("999");
//		paySd.setPayName("医疗预收款");
//		if(amtStPrepMap!=null && amtStPrepMap.size()>0){
//			paySd.setDebitAmt(CommonUtils.getDoubleObject(amtStPrepMap.get("amtStPrep")));
//		}else{
//			paySd.setDebitAmt(0D);
//		}
//		paySd.setCreditAmt(param.getBlCc().getAmtPrep());
//		payInfoList.add(paySd);
		
		//查询参与结算的预交金
		payInfoList.add(new PayInfoSd("999","医疗预收款",param.getBlCc().getAmtPrepRt(),param.getBlCc().getAmtPrep()));
		
		//医疗应收款信息
		payInfoList.add(new PayInfoSd(null,"医疗应收款",0D,param.getBlCc().getAmtSettle()));
		//支付方式集合信息
		List<PayInfoSd> payList = blIpCcMapper.qryPayListSd(paramMap);
		payInfoList.addAll(payList);
		//医保付款信息
		List<Map<String,Object>> insuPayList = blIpCcMapper.qryInsuPayInfoSd(paramMap);
		if(insuPayList!=null && insuPayList.size()>0){
			for(Map<String,Object> insuPayMap : insuPayList){
				if(insuPayMap!=null && insuPayMap.size()>0){
					switch (CommonUtils.getString(insuPayMap.get("code"))) {
					case "05":
						//异地医保个人账户
						payInfoList.add(new PayInfoSd(null,"异地医保个人账户",CommonUtils.getDouble(insuPayMap.get("grzf")),0D));
						//异地医保统筹支付
						payInfoList.add(new PayInfoSd(null,"异地医保统筹支付",CommonUtils.getDouble(insuPayMap.get("tczf")),0D));
						break;
					case "06":
						//全国医保个人账户
						payInfoList.add(new PayInfoSd(null,"全国医保个人账户",CommonUtils.getDouble(insuPayMap.get("grzf")),0D));
						//全国医保统筹支付
						payInfoList.add(new PayInfoSd(null,"全国医保统筹支付",CommonUtils.getDouble(insuPayMap.get("tczf")),0D));
						break;
					case "07":
						//深圳医保个人账户
						payInfoList.add(new PayInfoSd(null,"深圳医保个人账户",CommonUtils.getDouble(insuPayMap.get("grzf")),0D));
						//深圳医保统筹支付
						payInfoList.add(new PayInfoSd(null,"深圳医保统筹支付",CommonUtils.getDouble(insuPayMap.get("tczf")),0D));
						break;
					default:
						break;
					}
				}
			}
		}else{
			//异地医保个人账户
			payInfoList.add(new PayInfoSd(null,"异地医保个人账户",0D,0D));
			//异地医保统筹支付
			payInfoList.add(new PayInfoSd(null,"异地医保统筹支付",0D,0D));
			//全国医保个人账户
			payInfoList.add(new PayInfoSd(null,"全国医保个人账户",0D,0D));
			//全国医保统筹支付
			payInfoList.add(new PayInfoSd(null,"全国医保统筹支付",0D,0D));
			//深圳医保个人账户
			payInfoList.add(new PayInfoSd(null,"深圳医保个人账户",0D,0D));
			//深圳医保统筹支付
			payInfoList.add(new PayInfoSd(null,"深圳医保统筹支付",0D,0D));
		}

		//全国医保开启时查询全国医保结账数据
		if(!CommonUtils.isEmptyString(qgybEnable) && EnumerateParameter.ONE.equals(qgybEnable)){
			List<Map<String,Object>> qgPayList = blIpCcMapper.qryQgInsuPayInfoSd(paramMap);
			if(qgPayList!=null && qgPayList.size()>0){
				for(Map<String,Object> insuPayMap : qgPayList){
					if(insuPayMap!=null && insuPayMap.size()>0){
						//医保个人账户
						payInfoList.add(new PayInfoSd(null,"省医保个人账户",CommonUtils.getDouble(insuPayMap.get("grzf")),0D));
						//医保统筹支付
						payInfoList.add(new PayInfoSd(null,"省医保统筹支付",CommonUtils.getDouble(insuPayMap.get("tczf")),0D));
					}
				}
			}else{
				//医保个人账户
				payInfoList.add(new PayInfoSd(null,"省医保个人账户",0D,0D));
				//医保统筹支付
				payInfoList.add(new PayInfoSd(null,"省医保统筹支付",0D,0D));
			}
		}
		
		//查询票据分类金额信息
		List<Map<String,Object>> extListParam = blIpCcMapper.qryInvDtAmtList(paramMap);
		
		//扩展参数
		Map<String,Object> extParam = new HashMap<>(16);
		//结算作废笔数
		extParam.put("invCanlSt", blIpCcMapper.qryStCanlCnt(paramMap));
		//预交金退款笔数
		extParam.put("cntBackDepo", blIpCcMapper.qryPrepBackCnt(paramMap));
		//结算预交金退回数量
		extParam.put("stDepoCnt", blIpCcMapper.qryStDepoCnt(paramMap));
		//预交金有效票据张数
		extParam.put("depoCnt", blIpCcMapper.qryDepoCnt(paramMap));
		//查询现金短款金额
		Double amtRound = blIpCcMapper.qryCashShortAmt(paramMap);
		if(amtRound==null)
			amtRound = 0D;
		extParam.put("amtRound", amtRound);
		
		for(PayInfoSd pay : payInfoList){
			//上缴现金
			if("1".equals(pay.getPayType())){
				extParam.put("cashAmt", pay.getDebitAmt());
				pay.setDebitAmt(MathUtils.add(pay.getDebitAmt(), amtRound));
			}
			//支票支付金额
			if("2".equals(pay.getPayType())){
				extParam.put("cqAmt", pay.getDebitAmt());
			}
			//银行卡总额
			if("3".equals(pay.getPayType())){
				extParam.put("bankAmt", pay.getDebitAmt());
			}
			//结算退费回预交金金额
			if("999".equals(pay.getPayType())){
				extParam.put("amtStPrep", pay.getDebitAmt());
			}
		}
		
		//查詢电子发票号码使用号段
		List<InvInfo> voStInv = blIpCcMapper.qryStInvEBill(paramMap);
		if(voStInv!=null && voStInv.size()>0){
			StringBuffer invInfo = new StringBuffer(); 
        	BigDecimal cntInv = BigDecimal.ZERO;
        	for(int i = 0;i<voStInv.size();i++){
        		InvInfo vo = voStInv.get(i);
        		invInfo.append(vo.getBegincode());
        		invInfo.append("-");
        		invInfo.append(vo.getEndcode());
        		if(i+1==voStInv.size()){
        			invInfo.append(";");
        		}else{
        			invInfo.append(",");
        		}
        		
        		cntInv = cntInv.add(vo.getCnt());
        	}
        	extParam.put("eBillNo",invInfo.toString());
		}
		
		//电子发票作废号码
		List<InvalidStInv> InvalidList = blIpCcMapper.qryStInvEBillCanl(paramMap);
        if(InvalidList!=null && InvalidList.size()>0){
        	StringBuffer invInfoCanc = new StringBuffer();
        	for(int i = 0;i<InvalidList.size();i++){
        		InvalidStInv vo = InvalidList.get(i); 
        		invInfoCanc.append(vo.getCodeInv());
        		if(i+1==InvalidList.size()){
        			invInfoCanc.append(";");
        		}else{
        			invInfoCanc.append(",");
        		}    		
        	}
        	extParam.put("eBillCanlNo",invInfoCanc.toString());
        }
		
		param.setPayListSd(payInfoList);//支付金额列表
		param.setExtParam(extParam);	//特定参数查询
		param.setExtListParam(extListParam);//发票分类金额 集合
		
		return param;
	}

	@Override
	public OpBlCcVo getOpOperAccInfo(OpBlCcVo param) {
		Map<String,Object> paramMap = new HashMap<>(16);
		//扩展参数
		Map<String,Object> extParam = new HashMap<>(16);
		if(CommonUtils.isEmptyString(param.getBlCc().getPkCc())){
			paramMap.put("dateEnd", DateUtils.formatDate(param.getBlCc().getDateEnd(), "yyyyMMddHHmmss"));
			paramMap.put("pkEmp",  UserContext.getUser().getPkEmp());
			paramMap.put("pkOrg",  UserContext.getUser().getPkOrg());
			paramMap.put("pkCc", "");
			paramMap.put("euCctype", param.getBlCc().getEuCctype());

		}else{
			paramMap.put("pkCc", param.getBlCc().getPkCc());
		}
		/** 查询现金舍入金额*/
		Double amtRound = opblccMapper.qryOpAmtAround(paramMap);
		if (amtRound != null) {
			extParam.put("amtRound", amtRound);
		}

		//查詢电子发票号码使用号段
		List<InvInfo> voStInv = opblccMapper.qryOpStInvEBill(paramMap);
		if(voStInv!=null && voStInv.size()>0){
			StringBuffer invInfo = new StringBuffer();
			BigDecimal cntInv = BigDecimal.ZERO;
			for(int i = 0;i<voStInv.size();i++){
				InvInfo vo = voStInv.get(i);
				invInfo.append(vo.getBegincode());
				invInfo.append("-");
				invInfo.append(vo.getEndcode());
				if(i+1==voStInv.size()){
					invInfo.append(";");
				}else{
					invInfo.append(",");
				}

				cntInv = cntInv.add(vo.getCnt());
			}
			extParam.put("eBillNo",invInfo.toString());
		}

		//电子发票作废号码
		List<InvalidStInv> InvalidList = opblccMapper.qryOpStInvEBillCanl(paramMap);
		if(InvalidList!=null && InvalidList.size()>0){
			StringBuffer invInfoCanc = new StringBuffer();
			int cntInvCanc = InvalidList.size();
			for(int i = 0;i<InvalidList.size();i++){
				InvalidStInv vo = InvalidList.get(i);
				invInfoCanc.append(vo.getCodeInv());
				if(i+1==InvalidList.size()){
					invInfoCanc.append(";");
				}else{
					invInfoCanc.append(",");
				}
			}
			extParam.put("eBillCanlNo",invInfoCanc.toString());
			param.getBlCc().setCntInvCanc(cntInvCanc);
		}



		/**查询票据分类金额信息*/
		List<Map<String,Object>> extListParam = opblccMapper.qryOpInvDtAmtList(paramMap);
		
		/**查询付款方式信息*/
		List<BlCcPay> payList = opblccMapper.qryPayModeList();
		
		if(payList!=null && payList.size()>0){
			if(param.getBlCcPayList()!=null && param.getBlCcPayList().size()>0){
				for(BlCcPay payMode : payList){
					for(BlCcPay ccPay : param.getBlCcPayList()){
						if(payMode.getDtPaymode().equals(ccPay.getDtPaymode())){
							payMode.setAmt(ccPay.getAmt());
							payMode.setAmtBack(ccPay.getAmtBack());
							payMode.setCntTrade(ccPay.getCntTrade());
							payMode.setCntTradeBack(ccPay.getCntTradeBack());
							break;
						}
					}
				}
			}
			
			param.setBlCcPayListSd(payList);
		}
		
		//医保付款信息
		List<Map<String,Object>> insuPayList = opblccMapper.qryOpInsuPayInfoSd(paramMap);
		
		if(insuPayList!=null && insuPayList.size()>0){
			for(Map<String,Object> insuPayMap : insuPayList){
				if(insuPayMap!=null && insuPayMap.size()>0){
					switch (CommonUtils.getString(insuPayMap.get("code"))) {
					case "07":
						if(CommonUtils.getDouble(insuPayMap.get("grzf"))<0){
							//深圳医保个人账户
							BlCcPay insuPay = new BlCcPay();
							insuPay.setNamePaymode("账户支付");
							insuPay.setAmtBack(MathUtils.add(CommonUtils.getDouble(insuPay.getAmtBack()), CommonUtils.getDouble(insuPayMap.get("grzf"))));
							param.getBlCcPayListSd().add(insuPay);
							extParam.put("grzf", insuPay.getAmtBack().toString());
						}else{
							//深圳医保个人账户
							BlCcPay insuPay = new BlCcPay();
							insuPay.setNamePaymode("账户支付");
							insuPay.setAmt(MathUtils.add(CommonUtils.getDouble(insuPay.getAmt()), CommonUtils.getDouble(insuPayMap.get("grzf"))));
							param.getBlCcPayListSd().add(insuPay);
							extParam.put("grzf", insuPay.getAmt().toString());
						}

						if(CommonUtils.getDouble(insuPayMap.get("tczf"))<0){
							//深圳医保统筹支付
							BlCcPay insuPaytc = new BlCcPay();
							insuPaytc.setNamePaymode("医保统筹");
							insuPaytc.setAmtBack(MathUtils.add(CommonUtils.getDouble(insuPaytc.getAmtBack()), CommonUtils.getDouble(insuPayMap.get("tczf"))));
							param.getBlCcPayListSd().add(insuPaytc);
							extParam.put("tczf", insuPaytc.getAmtBack().toString());
						}else{
							//深圳医保统筹支付
							BlCcPay insuPaytc = new BlCcPay();
							insuPaytc.setNamePaymode("医保统筹");
							insuPaytc.setAmt(MathUtils.add(CommonUtils.getDouble(insuPaytc.getAmt()), CommonUtils.getDouble(insuPayMap.get("tczf"))));
							param.getBlCcPayListSd().add(insuPaytc);
							extParam.put("tczf", insuPaytc.getAmt().toString());
						}
						break;
					default:
						break;
					}
				}
			}
		}else{
			BlCcPay insuPay = new BlCcPay();
			insuPay.setNamePaymode("医保统筹");
			insuPay.setAmt(0D);
			insuPay.setAmtBack(0D);
			param.getBlCcPayListSd().add(insuPay);
			
			BlCcPay insuPaytc = new BlCcPay();
			insuPaytc.setNamePaymode("账户支付");
			insuPaytc.setAmt(0D);
			insuPaytc.setAmtBack(0D);
			param.getBlCcPayListSd().add(insuPaytc);
		}

		//全国医保开启时查询全国医保结账数据
		if(!CommonUtils.isEmptyString(qgybEnable) && EnumerateParameter.ONE.equals(qgybEnable)){
			List<Map<String,Object>> qgPayList = blIpCcMapper.qryQgOpInsuPayInfoSd(paramMap);
			if(qgPayList!=null && qgPayList.size()>0 && qgPayList.get(0)!=null){
				for(Map<String,Object> insuPayMap : qgPayList){
					if(CommonUtils.getDouble(insuPayMap.get("grzf"))<0){
						//深圳医保个人账户
						BlCcPay insuPay = new BlCcPay();
						insuPay.setNamePaymode("省医保账户支付");
						insuPay.setAmtBack(MathUtils.add(CommonUtils.getDouble(insuPay.getAmtBack()), CommonUtils.getDouble(insuPayMap.get("grzf"))));
						param.getBlCcPayListSd().add(insuPay);
						//extParam.put("sybgrzf", insuPay.getAmtBack().toString());
					}else{
						//深圳医保个人账户
						BlCcPay insuPay = new BlCcPay();
						insuPay.setNamePaymode("省医保账户支付");
						insuPay.setAmt(MathUtils.add(CommonUtils.getDouble(insuPay.getAmt()), CommonUtils.getDouble(insuPayMap.get("grzf"))));
						param.getBlCcPayListSd().add(insuPay);
						//extParam.put("sybgrzf", insuPay.getAmt().toString());
					}

					if(CommonUtils.getDouble(insuPayMap.get("tczf"))<0){
						//深圳医保统筹支付
						BlCcPay insuPaytc = new BlCcPay();
						insuPaytc.setNamePaymode("省医保统筹");
						insuPaytc.setAmtBack(MathUtils.add(CommonUtils.getDouble(insuPaytc.getAmtBack()), CommonUtils.getDouble(insuPayMap.get("tczf"))));
						param.getBlCcPayListSd().add(insuPaytc);
						//extParam.put("sybtczf", insuPaytc.getAmtBack().toString());
					}else{
						//深圳医保统筹支付
						BlCcPay insuPaytc = new BlCcPay();
						insuPaytc.setNamePaymode("省医保统筹");
						insuPaytc.setAmt(MathUtils.add(CommonUtils.getDouble(insuPaytc.getAmt()), CommonUtils.getDouble(insuPayMap.get("tczf"))));
						param.getBlCcPayListSd().add(insuPaytc);
						//extParam.put("sybtczf", insuPaytc.getAmt().toString());
					}
				}
			}else{
				BlCcPay insuPay = new BlCcPay();
				insuPay.setNamePaymode("省医保统筹");
				insuPay.setAmt(0D);
				insuPay.setAmtBack(0D);
				param.getBlCcPayListSd().add(insuPay);

				BlCcPay insuPaytc = new BlCcPay();
				insuPaytc.setNamePaymode("省医保账户支付");
				insuPaytc.setAmt(0D);
				insuPaytc.setAmtBack(0D);
				param.getBlCcPayListSd().add(insuPaytc);
			}
		}
		
		
		for(BlCcPay pay : param.getBlCcPayList()){
			//上缴现金
			if("1".equals(pay.getDtPaymode())){
				extParam.put("cashAmt", MathUtils.add(pay.getAmt()==null?0D:pay.getAmt(), pay.getAmtBack()==null?0D:pay.getAmtBack()));
			}
		}
		
		param.setExtListParam(extListParam);
		param.setExtParam(extParam);
		
		return param;
	}

}
