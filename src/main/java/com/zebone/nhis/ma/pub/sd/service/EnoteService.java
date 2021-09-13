package com.zebone.nhis.ma.pub.sd.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.zebone.nhis.bl.pub.support.InvSettltService;
import com.zebone.nhis.bl.pub.vo.BillInfo;
import com.zebone.nhis.bl.pub.vo.InvInfoVo;
import com.zebone.nhis.common.module.base.bd.code.BdDefdoc;
import com.zebone.nhis.common.module.base.bd.price.BdInvcate;
import com.zebone.nhis.common.module.bl.*;
import com.zebone.nhis.common.module.mybatis.ReflectHelper;
import com.zebone.nhis.common.support.*;
import com.zebone.nhis.ma.pub.sd.dao.EnoteMapper;
import com.zebone.nhis.ma.pub.sd.vo.*;
import com.zebone.platform.common.support.NHISUUID;
import com.zebone.platform.common.support.User;
import com.zebone.platform.common.support.UserContext;
import com.zebone.platform.framework.support.IUser;
import com.zebone.platform.modules.dao.DataBaseHelper;
import com.zebone.platform.modules.exception.BusException;
import com.zebone.platform.modules.utils.JsonUtil;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.beanutils.ConvertUtils;
import org.apache.commons.beanutils.Converter;
import org.apache.commons.beanutils.converters.SqlDateConverter;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.codec.Base64;
import org.apache.shiro.crypto.hash.SimpleHash;
import org.codehaus.jackson.type.TypeReference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 电子票据接口服务
 * @author 李继鹏
 *
 */
@Service
public class EnoteService implements InitializingBean {
	
	private Logger billLog = LoggerFactory.getLogger("nhis.EbillLog");

	//url地址
	private String url;
	//应用id
	private String appId;
	//应用key
	private String key;
	/**纸质票据种类代码*/
	private String billCodePaper;	
	//深大门诊工号前面加M
	private String placeCode;
	//个性化配置
	private String perConfig;
	//机构编码
	private String hospCode;
	//医保归属地
	private String insuAddr;
	
	@Autowired
	private EnoteMapper enoteDao;
	
	//返回值状态 S0000：成功    其他：失败
	private String resultStr = "S0000";
	
	@Autowired
	private InvSettltService invSettltService;
	
	
	/**
	 * 住院电子票据开立
	 */
	private EnoteResDataVo ipEnotePrint(Object[] args){
		/**
		 * 入参
		 * pkSettle:结算主键
		 * pkPv:就诊主键
		 */
		Map<String,Object> paramMap =(Map<String,Object>)args[0];
//		Map<String, Object> paramMap = new HashMap<>();
//		paramMap.put("pkSettle", "784179468c874b05a4d2252199cdc787");
//		paramMap.put("pkPv", "6478bafa6d7747d28247fcab10ece1ba");
		
		//电子票据入参
		EnotePrintReqVo resVo = new EnotePrintReqVo();
		
		//获取票据信息
		List<Map<String,Object>> invList = enoteDao.qryENoteInvInfo(paramMap);
		Map<String,Object> invInfo = new HashMap<>(16);
		if(invList!=null && invList.size()>0){
			invInfo = invList.get(0);
			resVo.setBusNo("IP"+CommonUtils.getString(invInfo.get("busNo")));
			resVo.setBusType(CommonUtils.getString(invInfo.get("busType")));
			resVo.setPayer(CommonUtils.getString(invInfo.get("payer")));
			resVo.setBusDateTime(CommonUtils.getString(invInfo.get("busDateTime"))+"000");
			resVo.setPlaceCode(CommonUtils.getString(invInfo.get("placeCode")));
			resVo.setPayee(CommonUtils.getString(invInfo.get("payee")));
			resVo.setAuthor(CommonUtils.getString(invInfo.get("author")));
			resVo.setTotalAmt(CommonUtils.getDouble(invInfo.get("totalAmt")));
			resVo.setRemark(CommonUtils.getString(invInfo.get("remark")));
		}
		
		//缴款信息
		resVo.setAlipayCode(null);
		resVo.setWeChatOrderNo(null);
		resVo.setWeChatMedTransNo(null);
		resVo.setOpenID(null);
		
		//通知信息
		Map<String,Object> inform = enoteDao.qryInformInfo(paramMap);
		if(inform!=null && inform.size()>0){
			resVo.setTel(CommonUtils.getString(inform.get("tel")));
			resVo.setEmail(CommonUtils.getString(inform.get("email")));
			resVo.setPayerType(CommonUtils.getString(inform.get("payerType")));
			resVo.setIdCardNo(CommonUtils.getString(inform.get("idCardNo")));
			resVo.setCardType(CommonUtils.getString(inform.get("cardType")));
			resVo.setCardNo(CommonUtils.getString(inform.get("cardNo")));
		}
		
		//就诊信息
		paramMap.put("svType", EnumerateParameter.ONE);
		Map<String,Object> pvMap = enoteDao.qryENotePvInfo(paramMap);
		if(pvMap!=null && pvMap.size()>0){
			resVo.setMedicalInstitution(CommonUtils.getString(pvMap.get("medicalInstitution")));
			resVo.setMedCareInstitution(hospCode);
			resVo.setMedCareTypeCode(CommonUtils.getString(pvMap.get("medCareTypeCode")));
			resVo.setMedicalCareType(insuAddr+CommonUtils.getString(pvMap.get("medicalCareType")));
			resVo.setMedicalInsuranceID(CommonUtils.getString(pvMap.get("medicalInsuranceId")));
			resVo.setCategory(CommonUtils.getString(pvMap.get("category")));
			resVo.setCategoryCode(CommonUtils.getString(pvMap.get("categoryCode")));
			resVo.setLeaveCategory(CommonUtils.getString(pvMap.get("leaveCategory")));
			resVo.setLeaveCategoryCode(CommonUtils.getString(pvMap.get("leaveCategoryCode")));
			resVo.setHospitalNo(CommonUtils.getString(pvMap.get("hospitalNo")));
			resVo.setVisitNo(CommonUtils.getString(pvMap.get("visitNo")));
			resVo.setConsultationDate(CommonUtils.getString(pvMap.get("consultationDate")));
			resVo.setPatientId(CommonUtils.getString(pvMap.get("patientId")));
			resVo.setPatientNo(CommonUtils.getString(pvMap.get("patientNo")));
			resVo.setSex(CommonUtils.getString(pvMap.get("sex")));
			resVo.setAge(CommonUtils.getString(pvMap.get("age")));
			resVo.setHospitalArea(CommonUtils.getString(pvMap.get("hospitalArea")));
			resVo.setBedNo(CommonUtils.getString(pvMap.get("bedNo")));
			resVo.setCaseNumber(CommonUtils.getString(pvMap.get("caseNumber")));
			resVo.setInHospitalDate(CommonUtils.getString(pvMap.get("inHospitalDate")));
			resVo.setOutHospitalDate(CommonUtils.getString(pvMap.get("outHospitalDate")));
			resVo.setHospitalDays(null);
		}
		
		//查询预交金信息
		Double depoAmt = enoteDao.qryDepoAmtByPkPv(paramMap);
		Double amtPi = CommonUtils.getDouble(invInfo.get("amountPi"));
		if(depoAmt==null){
			depoAmt = 0D;
		}
		
		Double stAmt = MathUtils.sub(depoAmt, amtPi);

		if(!CommonUtils.isEmptyString(perConfig)
				&& paramMap.containsKey("pkSettle") && !CommonUtils.isEmptyString(CommonUtils.getString(paramMap.get("pkSettle")))){
			switch (perConfig){
				case "sugh":
				case "pskq":
					//查询医保支付金额
					Map<String,Object> insuMap = enoteDao.qryInsuAmtByPkSettle(paramMap);
					if(insuMap==null || insuMap.size()<=0){
						//查询患者是否为医保结算，只有医保需要走此逻辑
						String euHptype = enoteDao.qryHpTypeByPkSettle(paramMap);
						if(!CommonUtils.isEmptyString(euHptype) && !"0".equals(euHptype)){
							insuMap = enoteDao.qryInsuAmtByPkPv(paramMap);
						}
					}

					if(insuMap!=null && insuMap.size()>0){
						resVo.setAccountPay(MapUtils.getDouble(insuMap,"zhzf",0D));
						resVo.setFundPay(MapUtils.getDouble(insuMap,"jjzf",0D));
					}else{
						resVo.setAccountPay(0D);
						resVo.setFundPay(MapUtils.getDouble(invInfo,"amountInsu",0D));
					}
					break;
				default:
					break;
			}
		}

		//费用信息
		resVo.setPayMentVoucher(null);//预交金凭证消费扣款无数据
		resVo.setOtherfundPay(0D);
		resVo.setOwnPay(amtPi);
		resVo.setSelfConceitedAmt(0D);
		resVo.setSelfPayAmt(0D);
		resVo.setSelfCashPay(amtPi);
		resVo.setCashPay(depoAmt);
		resVo.setChequePay(0D);
		resVo.setTransferAccountPay(0D);
		
		//补缴、退费金额
		if(stAmt>=0){
			//退费
			resVo.setCashRefund(stAmt);
			resVo.setCashRecharge(0D);
		}else{
			//补缴
			resVo.setCashRefund(0D);
			resVo.setCashRecharge(MathUtils.mul(stAmt, -1D));
		}
		
		resVo.setChequeRecharge(0D);
		resVo.setTransferRecharge(0D);
		resVo.setChequeRefund(0D);
		resVo.setTransferRefund(0D);
		resVo.setOwnAcBalance(0D);
		resVo.setReimbursementAmt(0D);
		resVo.setBalancedNumber(CommonUtils.getString(invInfo.get("busNo")));
		resVo.setOtherInfo(null);
		resVo.setOtherMedicalList(null);	
		resVo.setPayChannelDetail(enoteDao.qryStDepoList(paramMap));//查询结算首付款信息
		
		//医保报销部分也要算入交费渠道列表里
		if(resVo.getAccountPay()!=0D){
			PayChannelDetailVo dtl = new PayChannelDetailVo();
			dtl.setPayChannelCode("YBK");
			dtl.setPayChannelValue(resVo.getAccountPay());
			if(resVo.getPayChannelDetail()==null){
				List<PayChannelDetailVo> dtlList = new ArrayList<>();
				resVo.setPayChannelDetail(dtlList);
			}
			resVo.getPayChannelDetail().add(dtl);
		}
		if(resVo.getFundPay()!=0D){
			PayChannelDetailVo dtl = new PayChannelDetailVo();
			dtl.setPayChannelCode("YBTCJJ");
			dtl.setPayChannelValue(resVo.getFundPay());
			if(resVo.getPayChannelDetail()==null){
				List<PayChannelDetailVo> dtlList = new ArrayList<>();
				resVo.setPayChannelDetail(dtlList);
			}
			resVo.getPayChannelDetail().add(dtl);
		}
		
		
		//其他信息
		resVo.seteBillRelateNo(null);
		resVo.setIsArrears("1");
		resVo.setArrearsReason(null);
		
		//项目信息
		if(!CommonUtils.isEmptyString(perConfig) && "zsrm".equals(perConfig)){//中山人民客户化
			zsrmListChargeDetail(paramMap,resVo);
			//resVo.setChargeDetail(enoteDao.qryStIpDtListZsrm(paramMap));//查询本次结算收费项目信息
			//resVo.setListDetail(enoteDao.qryStIpDtDtlListZsrm(paramMap));//查询本次结算清单信息
		}else{
			resVo.setChargeDetail(enoteDao.qryStIpDtList(paramMap));//查询本次结算收费项目信息
			resVo.setListDetail(enoteDao.qryStIpDtDtlList(paramMap));//查询本次结算清单信息
		}
		//校验项目信息的金额是否和总金额一致
		if(resVo.getListDetail()!=null && resVo.getListDetail().size()>0){
			List<ListDetailVo> dtls = new ArrayList<>();
			for(ListDetailVo cha : resVo.getListDetail()){
				Double amtChk = MathUtils.sub( cha.getAmtChk(), cha.getAmt());
				if(amtChk!=0D){
					cha.setAmt(MathUtils.add(cha.getAmt(), amtChk));
					
					Double amt = MathUtils.mul(amtChk, -1D);
					
					ListDetailVo dtl = new ListDetailVo();
					dtl.setListDetailNo("0");
					dtl.setChargeCode(cha.getChargeCode());
					dtl.setChargeName(cha.getChargeName());
					dtl.setCode(NHISUUID.getKeyId());
					dtl.setName("货币误差");
					dtl.setStd(amt);
					dtl.setNumber(1D);
					dtl.setAmt(amt);
					dtl.setSelfAmt(amt);
					dtl.setReceivableAmt(0D);
					dtl.setSortNo(0);
					
					dtls.add(dtl);
				}
			}
			
			if(dtls!=null && dtls.size()>0){
				resVo.getListDetail().addAll(dtls);
			}
		}
		
		//校验总金额和缴费渠道金额是否一致
		Double amtChk = 0D;
		for(PayChannelDetailVo pay : resVo.getPayChannelDetail()){
			//深大总医院个性化需求：如果支付方式里包含新冠、职业暴露、大肠癌筛查时统一修正支付方式为其他
			if(!CommonUtils.isEmptyString(perConfig) && "sugh".equals(perConfig)){
				//查询符合此需求的支付方式信息
				List<BdDefdoc> payList = enoteDao.qryPayInfoList();
				if(payList!=null && payList.size()>0){
					for(BdDefdoc docVo : payList)
					{
						if(pay.getPayChannelCode().equals(docVo.getCode())){
							pay.setPayChannelCode("99");
							break;
						}
					}
				}
			}
			//zsrm-处理支付类型为10、11、12、13
			if(!CommonUtils.isEmptyString(perConfig) && "zsrm".equals(perConfig)){//中山人民客户化
				pay.setPayChannelCode(zsrmPayChannelCodeTran(pay.getPayChannelCode()));
			}
			amtChk=MathUtils.add(amtChk, pay.getPayChannelValue());
		}
		if(amtChk.compareTo(resVo.getTotalAmt())!=0){
			Double amtCe = MathUtils.sub(resVo.getTotalAmt(), amtChk);
			
//			//差额算入到个人现金支付里
//			resVo.setOwnPay(MathUtils.add(resVo.getOwnPay(), amtCe));
//			resVo.setSelfCashPay(MathUtils.add(resVo.getSelfCashPay(), amtCe));
			
			PayChannelDetailVo dtl = new PayChannelDetailVo();
			dtl.setPayChannelCode("HBWC");
			dtl.setPayChannelValue(amtCe);
			if(resVo.getPayChannelDetail()==null){
				List<PayChannelDetailVo> dtlList = new ArrayList<>();
				resVo.setPayChannelDetail(dtlList);
			}
			resVo.getPayChannelDetail().add(dtl);
		}
		
		//将Data内容转换为json格式再base64编码,编码字符集UTF-8
		String dataJson = JsonUtil.writeValueAsString(resVo);
		//发送请求
		EnoteResDataVo dataVo = enoteRts(dataJson,"1.0","invEBillHospitalized");
		
		//解析data实体类
//		base64 = new Base64();
//		String msgJson = base64.decodeToString(dataVo.getMessage().getBytes());//做base64解码,编码字符集UTF-8
//		
//		//json转为实体类
//		EnotePrintResVo resMsgVo = JsonUtil.readValue(msgJson, EnotePrintResVo.class);
		
		return dataVo;
		
	}

	private void mapToBean(Map<String,Object> source,Object target,String pattern){
		/**
		 * 处理date类型转换出错的问题
		 */
		if(StringUtils.isNotBlank(pattern)) {
			Converter converter = new Converter() {
				@Override
				public Object convert(Class aClass, Object o) {
					SimpleDateFormat format = new SimpleDateFormat(pattern);
					Date parse = null;
					try {
						parse = format.parse(o.toString());
					} catch (ParseException e) {
						e.printStackTrace();
						throw new BusException("日期转换出错");
					}
					return parse;
				}
			};
			ConvertUtils.register(converter, Date.class);
		}
		try {
			BeanUtils.populate(target,source);
		} catch (Exception e) {
			e.printStackTrace();
			throw new BusException("map转实体类出错");
		}finally {
			ConvertUtils.deregister(Date.class);
			ConvertUtils.register(new SqlDateConverter(),java.util.Date.class);
		}
	}

	/**
	 * 门诊挂号票据开立
	 * @param args
	 * @return
	 */
	private EnoteResDataVo invEBillRegistration(Object[] args){
		/**
		 * 入参
		 * pkSettle:结算主键
		 * pkPv:就诊主键
		 */
		Map<String,Object> paramMap =(Map<String,Object>)args[0];
		
		//电子票据入参
		EnotePrintReqVo resVo = new EnotePrintReqVo();
		
		//获取票据信息
		List<Map<String,Object>> invList = enoteDao.qryENoteInvInfo(paramMap);
		Map<String,Object> invInfo = new HashMap<>(16);
		if(invList!=null && invList.size()>0){
			invInfo = invList.get(0);
			String placeCode = MapUtils.getString(invInfo,"placeCode");
			
			//if(!placeCode.startsWith(this.placeCode)){
			placeCode = this.placeCode+placeCode;
			//}
			 
			resVo.setBusNo("REG"+CommonUtils.getString(invInfo.get("busNo")));
			resVo.setBusType("06");
			resVo.setPayer(CommonUtils.getString(invInfo.get("payer")));
			resVo.setBusDateTime(CommonUtils.getString(invInfo.get("busDateTime"))+"000");
			resVo.setPlaceCode(placeCode);
			resVo.setPayee(CommonUtils.getString(invInfo.get("payee")));
			resVo.setAuthor(CommonUtils.getString(invInfo.get("author")));
			resVo.setTotalAmt(CommonUtils.getDouble(invInfo.get("totalAmt")));
			resVo.setRemark(CommonUtils.getString(invInfo.get("remark")));
		}
		
		//缴款信息
		resVo.setAlipayCode(null);
		resVo.setWeChatOrderNo(null);
		resVo.setWeChatMedTransNo(null);
		resVo.setOpenID(null);
		
		//通知信息
		Map<String,Object> inform = enoteDao.qryInformInfo(paramMap);
		if(inform!=null && inform.size()>0){
			resVo.setTel(CommonUtils.getString(inform.get("tel")));
			resVo.setEmail(CommonUtils.getString(inform.get("email")));
			resVo.setPayerType(CommonUtils.getString(inform.get("payerType")));
			resVo.setIdCardNo(CommonUtils.getString(inform.get("idCardNo")));
			resVo.setCardType(CommonUtils.getString(inform.get("cardType")));
			resVo.setCardNo(CommonUtils.getString(inform.get("cardNo")));
		}
		
		//就诊信息
		paramMap.put("svType", EnumerateParameter.ZERO);
		Map<String,Object> pvMap = enoteDao.qryENotePvInfo(paramMap);
		if(pvMap!=null && pvMap.size()>0){
			resVo.setMedicalInstitution(CommonUtils.getString(pvMap.get("medicalInstitution")));
			resVo.setMedCareInstitution(hospCode);
			resVo.setMedCareTypeCode(CommonUtils.getString(pvMap.get("medCareTypeCode")));
			resVo.setMedicalCareType(insuAddr+CommonUtils.getString(pvMap.get("medicalCareType")));
			resVo.setMedicalInsuranceID(CommonUtils.getString(pvMap.get("medicalInsuranceId")));
			resVo.setCategory(CommonUtils.getString(pvMap.get("category")));
			resVo.setPatientCategory(CommonUtils.getString(pvMap.get("categoryCode")));//就诊科室编码
			resVo.setPatientId(CommonUtils.getString(pvMap.get("patientId")));
			resVo.setPatientNo(CommonUtils.getString(pvMap.get("patientNo")));
			resVo.setConsultationDate(CommonUtils.getString(pvMap.get("inHospitalDate")));
			resVo.setSex(CommonUtils.getString(pvMap.get("sex")));
			resVo.setAge(CommonUtils.getString(pvMap.get("age")));
		}

		if(!CommonUtils.isEmptyString(perConfig)
				&& paramMap.containsKey("pkSettle") && !CommonUtils.isEmptyString(CommonUtils.getString(paramMap.get("pkSettle")))){
			switch (perConfig){
				case "sugh":
				case "pskq":
					//查询医保支付金额
					Map<String,Object> insuMap = enoteDao.qryInsuAmtByPkSettle(paramMap);
					if(insuMap==null || insuMap.size()<=0){
						//查询患者所挂号是否为医保号，只有医保需要走此逻辑
						String euHptype = enoteDao.qryHpTypeByPkSettle(paramMap);
						if(!CommonUtils.isEmptyString(euHptype) && !"0".equals(euHptype)){
							//因为挂号时写入的医保结算记录没有存入pk_pv、pk_settle等信息，所以在此用患者姓名和结算金额查询，根据时间倒叙取第一条数据
							paramMap.put("namePi",resVo.getPayer());
							paramMap.put("amountSt",resVo.getTotalAmt());
							insuMap = enoteDao.qryInsuAmtByNamePi(paramMap);
						}
					}

					if(insuMap!=null && insuMap.size()>0){
						resVo.setAccountPay(MapUtils.getDouble(insuMap,"zhzf",0D));
						resVo.setFundPay(MapUtils.getDouble(insuMap,"jjzf",0D));
					}else{
						resVo.setAccountPay(0D);
						resVo.setFundPay(MapUtils.getDouble(invInfo,"amountInsu",0D));
					}
					break;

				default:
					break;
			}
		}

		//费用信息
		resVo.setPayMentVoucher(null);//预交金凭证消费扣款无数据
		resVo.setOtherfundPay(0D);

		Double amtPi = CommonUtils.getDouble(invInfo.get("amountPi"));
		resVo.setOwnPay(amtPi);
		resVo.setSelfConceitedAmt(0D);
		resVo.setSelfPayAmt(0D);
		resVo.setSelfCashPay(amtPi);
		resVo.setCashPay(0D);
		resVo.setChequePay(0D);
		resVo.setTransferAccountPay(0D);
		resVo.setChequeRecharge(0D);
		resVo.setTransferRecharge(0D);
		resVo.setCashRefund(0D);
		resVo.setChequeRefund(0D);
		resVo.setTransferRefund(0D);
		resVo.setOwnAcBalance(0D);
		resVo.setReimbursementAmt(0D);
		resVo.setBalancedNumber(CommonUtils.getString(invInfo.get("busNo")));
		resVo.setOtherInfo(null);
		resVo.setOtherMedicalList(null);	
		resVo.setPayChannelDetail(enoteDao.qryStDepoList(paramMap));//查询结算首付款信息
		
		//医保报销部分也要算入交费渠道列表里
		if(resVo.getAccountPay()!=0D){
			PayChannelDetailVo dtl = new PayChannelDetailVo();
			dtl.setPayChannelCode("YBK");
			dtl.setPayChannelValue(resVo.getAccountPay());
			if(resVo.getPayChannelDetail()==null){
				List<PayChannelDetailVo> dtlList = new ArrayList<>();
				resVo.setPayChannelDetail(dtlList);
			}
			resVo.getPayChannelDetail().add(dtl);
		}
		if(resVo.getFundPay()!=0D){
			PayChannelDetailVo dtl = new PayChannelDetailVo();
			dtl.setPayChannelCode("YBTCJJ");
			dtl.setPayChannelValue(resVo.getFundPay());
			if(resVo.getPayChannelDetail()==null){
				List<PayChannelDetailVo> dtlList = new ArrayList<>();
				resVo.setPayChannelDetail(dtlList);
			}
			resVo.getPayChannelDetail().add(dtl);
		}
		
		//其他信息
		resVo.seteBillRelateNo(null);
		resVo.setIsArrears("1");
		resVo.setArrearsReason(null);

		//项目信息
		if(!CommonUtils.isEmptyString(perConfig) && "zsrm".equals(perConfig)){//中山人民客户化
			zsrmListChargeDetail(paramMap,resVo);
			//resVo.setChargeDetail(enoteDao.qryStIpDtListZsrm(paramMap));//查询本次结算收费项目信息
			//resVo.setListDetail(enoteDao.qryStIpDtDtlListZsrm(paramMap));//查询本次结算清单信息
		}else{
			resVo.setChargeDetail(enoteDao.qryStIpDtList(paramMap));//查询本次结算收费项目信息
			resVo.setListDetail(enoteDao.qryStIpDtDtlList(paramMap));//查询本次结算清单信息
		}

		//校验项目信息的金额是否和总金额一致
		if(resVo.getListDetail()!=null && resVo.getListDetail().size()>0){
			List<ListDetailVo> dtls = new ArrayList<>();
			for(ListDetailVo cha : resVo.getListDetail()){
				Double amtChk = MathUtils.sub( cha.getAmtChk(), cha.getAmt());
				if(amtChk!=0D){
					cha.setAmt(MathUtils.add(cha.getAmt(), amtChk));
					
					Double amt = MathUtils.mul(amtChk, -1D);
					
					ListDetailVo dtl = new ListDetailVo();
					dtl.setListDetailNo("0");
					dtl.setChargeCode(cha.getChargeCode());
					dtl.setChargeName(cha.getChargeName());
					dtl.setCode(NHISUUID.getKeyId());
					dtl.setName("货币误差");
					dtl.setStd(amt);
					dtl.setNumber(1D);
					dtl.setAmt(amt);
					dtl.setSelfAmt(amt);
					dtl.setReceivableAmt(0D);
					dtl.setSortNo(0);
					
					dtls.add(dtl);
				}
			}
			
			if(dtls!=null && dtls.size()>0){
				resVo.getListDetail().addAll(dtls);
			}
		}
		
		//校验总金额和缴费渠道金额是否一致
		Double amtChk = 0D;
		for(PayChannelDetailVo pay : resVo.getPayChannelDetail()){
			//深大总医院个性化需求：如果支付方式里包含新冠、职业暴露、大肠癌筛查时统一修正支付方式为其他
			if(!CommonUtils.isEmptyString(perConfig) && "sugh".equals(perConfig)){
				//查询符合此需求的支付方式信息
				List<BdDefdoc> payList = enoteDao.qryPayInfoList();
				if(payList!=null && payList.size()>0){
					for(BdDefdoc docVo : payList)
					{
						if(pay.getPayChannelCode().equals(docVo.getCode())){
							pay.setPayChannelCode("99");

							//需要把此部分金额算入到otherfundPay字段，同时ownPay和selfCashPay减去相应金额
							resVo.setOwnPay(MathUtils.sub(resVo.getOwnPay(),pay.getPayChannelValue()));
							resVo.setSelfCashPay(MathUtils.sub(resVo.getSelfCashPay(),pay.getPayChannelValue()));

							resVo.setOtherfundPay(MathUtils.add(resVo.getOtherfundPay(),pay.getPayChannelValue()));
							break;
						}
					}
				}
			}
            //zsrm-处理支付类型为10、11、12、13
			if(!CommonUtils.isEmptyString(perConfig) && "zsrm".equals(perConfig)){//中山人民客户化
				pay.setPayChannelCode(zsrmPayChannelCodeTran(pay.getPayChannelCode()));
			}
			amtChk=MathUtils.add(amtChk, pay.getPayChannelValue());
		}
		if(amtChk.compareTo(resVo.getTotalAmt())!=0){
			Double amtCe = MathUtils.sub(resVo.getTotalAmt(), amtChk);
			
			//差额算入到个人现金支付里
//			resVo.setOwnPay(MathUtils.add(resVo.getOwnPay(), amtCe));
//			resVo.setSelfCashPay(MathUtils.add(resVo.getSelfCashPay(), amtCe));
			
			PayChannelDetailVo dtl = new PayChannelDetailVo();
			dtl.setPayChannelCode("HBWC");
			dtl.setPayChannelValue(amtCe);
			if(resVo.getPayChannelDetail()==null){
				List<PayChannelDetailVo> dtlList = new ArrayList<>();
				resVo.setPayChannelDetail(dtlList);
			}
			resVo.getPayChannelDetail().add(dtl);
		}
		
		//将Data内容转换为json格式再base64编码,编码字符集UTF-8
		String dataJson = JsonUtil.writeValueAsString(resVo);
		//发送请求
		EnoteResDataVo dataVo = enoteRts(dataJson,"1.0","invEBillRegistration");
		
		return dataVo;
	}
	
	/**
	 * 门诊电子票据开立
	 */
	private EnoteResDataVo invEBillOutpatient(Object[] args){
		/**
		 * 入参
		 * pkSettle:结算主键
		 * pkPv:就诊主键
		 */
		Map<String,Object> paramMap =(Map<String,Object>)args[0];
		IUser user =(IUser)args[1];
		User u = (User) user;
		paramMap.put("pkOrg", u.getPkOrg());
		
		//电子票据入参
		EnotePrintReqVo resVo = new EnotePrintReqVo();
		
		//获取票据信息
		List<Map<String,Object>> invList = enoteDao.qryENoteInvInfo(paramMap);
		Map<String,Object> invInfo = new HashMap<>(16);
		if(invList!=null && invList.size()>0){
			invInfo = invList.get(0);
			String placeCode = MapUtils.getString(invInfo,"placeCode");
			//if(!placeCode.startsWith(this.placeCode)){
			placeCode = this.placeCode+placeCode;
			//}
			resVo.setBusNo("OP"+CommonUtils.getString(invInfo.get("busNo")));
			resVo.setBusType(CommonUtils.getString(invInfo.get("busType")));
			resVo.setPayer(CommonUtils.getString(invInfo.get("payer")));
			resVo.setBusDateTime(CommonUtils.getString(invInfo.get("busDateTime"))+"000");
			resVo.setPlaceCode(placeCode);
			resVo.setPayee(CommonUtils.getString(invInfo.get("payee")));
			resVo.setAuthor(CommonUtils.getString(invInfo.get("author")));
			resVo.setTotalAmt(CommonUtils.getDouble(invInfo.get("totalAmt")));
			resVo.setRemark(CommonUtils.getString(invInfo.get("remark")));
		}
		
		//缴款信息
		resVo.setAlipayCode(null);
		resVo.setWeChatOrderNo(null);
		resVo.setWeChatMedTransNo(null);
		resVo.setOpenID(null);
		
		//通知信息
		Map<String,Object> inform = enoteDao.qryInformInfo(paramMap);
		if(inform!=null && inform.size()>0){
			resVo.setTel(CommonUtils.getString(inform.get("tel")));
			resVo.setEmail(CommonUtils.getString(inform.get("email")));
			resVo.setPayerType(CommonUtils.getString(inform.get("payerType")));
			resVo.setIdCardNo(CommonUtils.getString(inform.get("idCardNo")));
			resVo.setCardType(CommonUtils.getString(inform.get("cardType")));
			resVo.setCardNo(CommonUtils.getString(inform.get("cardNo")));
		}
		
		//就诊信息
		paramMap.put("svType", EnumerateParameter.ZERO);
		Map<String,Object> pvMap = null;
		if(!CommonUtils.isEmptyString(perConfig) && "zsrm".equals(perConfig)) {//中山人民客户化
			pvMap = enoteDao.qryENotePvInfoZsrm(paramMap);
			//医保类型
			String medCareTypeCode = CommonUtils.getPropValueStr(pvMap, "medCareTypeCode");
			//医保患者，显示医疗类别
			if(CommonUtils.isNotNull(medCareTypeCode)  && !"03".equals(medCareTypeCode)) {
				Map<String,Object> pvHpMap = enoteDao.qryENotePvMedTypeZsrm(paramMap);
				String code = CommonUtils.getPropValueStr(pvHpMap, "code");
				String name = CommonUtils.getPropValueStr(pvHpMap, "name");
				pvMap.put("med_care_type_code", CommonUtils.isNotNull(code) ? code : "11");
				pvMap.put("medical_care_type", CommonUtils.isNotNull(code) ? name : "普通门诊");
			}
		}else{
			pvMap = enoteDao.qryENotePvInfo(paramMap);
		}
		if(pvMap!=null && pvMap.size()>0){
			resVo.setMedicalInstitution(CommonUtils.getString(pvMap.get("medicalInstitution")));
			resVo.setMedCareInstitution(hospCode);
			resVo.setMedCareTypeCode(CommonUtils.getString(pvMap.get("medCareTypeCode")));
			resVo.setMedicalCareType(insuAddr+CommonUtils.getString(pvMap.get("medicalCareType")));
			resVo.setMedicalInsuranceID(CommonUtils.getString(pvMap.get("medicalInsuranceId")));
			resVo.setCategory(CommonUtils.getString(pvMap.get("category")));
			resVo.setPatientCategoryCode(CommonUtils.getString(pvMap.get("categoryCode")));
			resVo.setPatientId(CommonUtils.getString(pvMap.get("patientId")));
			resVo.setPatientNo(CommonUtils.getString(pvMap.get("patientNo")));
			resVo.setConsultationDate(CommonUtils.getString(pvMap.get("inHospitalDate")));
			resVo.setSex(CommonUtils.getString(pvMap.get("sex")));
			resVo.setAge(CommonUtils.getString(pvMap.get("age")));
			resVo.setHospitalArea(CommonUtils.getString(pvMap.get("hospitalArea")));
			resVo.setBedNo(CommonUtils.getString(pvMap.get("bedNo")));
			resVo.setCaseNumber(CommonUtils.getString(pvMap.get("caseNumber")));
			resVo.setSpecialDiseasesName(null);
		}

		if(!CommonUtils.isEmptyString(perConfig)
		&& paramMap.containsKey("pkSettle") && !CommonUtils.isEmptyString(CommonUtils.getString(paramMap.get("pkSettle")))){
			switch (perConfig){
				case "sugh":
				case "pskq":
					//查询医保支付金额
					Map<String,Object> insuMap = enoteDao.qryInsuAmtByPkSettle(paramMap);
					if(insuMap==null || insuMap.size()<=0){
						//查询患者是否为医保结算，只有医保需要走此逻辑
						String euHptype = enoteDao.qryHpTypeByPkSettle(paramMap);
						if(!CommonUtils.isEmptyString(euHptype) && !"0".equals(euHptype)){
							insuMap = enoteDao.qryInsuAmtByPkPv(paramMap);
						}
					}

					if(insuMap!=null && insuMap.size()>0){
						resVo.setAccountPay(MapUtils.getDouble(insuMap,"zhzf",0D));
						resVo.setFundPay(MapUtils.getDouble(insuMap,"jjzf",0D));
					}else{
						resVo.setAccountPay(0D);
						resVo.setFundPay(MapUtils.getDouble(invInfo,"amountInsu",0D));
					}
					break;
				case "zsrm":
					//个人账户支付
					Double accountPay = enoteDao.qryAumByPkSettleZsrm(paramMap);
					if(accountPay != null){
						resVo.setAccountPay(accountPay);
					}else{
						resVo.setAccountPay(0D);
					}

					if(invList!=null && invList.size()>0){
						//医保统筹基金支付
						resVo.setFundPay(MapUtils.getDouble(invList.get(0),"amountInsu",0D));
					}else{
						//医保统筹基金支付AMT_JJZF
						resVo.setFundPay(0D);
					}
					break;
				default:
					break;
			}

		}

		//费用信息
		resVo.setPayMentVoucher(null);//预交金凭证消费扣款无数据
		resVo.setOtherfundPay(0D);

		Double amtPi = CommonUtils.getDouble(invInfo.get("amountPi"));
		resVo.setOwnPay(amtPi);
		resVo.setSelfConceitedAmt(0D);
		resVo.setSelfPayAmt(0D);

		if(!CommonUtils.isEmptyString(perConfig) && "zsrm".equals(perConfig)){//中山人民客户化
			BigDecimal amtPiZsrm = new BigDecimal(Double.toString(amtPi));
			BigDecimal accPay = new BigDecimal(Double.toString(resVo.getAccountPay()));
			BigDecimal reckonPay = amtPiZsrm.subtract(accPay);
			if(reckonPay.compareTo(BigDecimal.ZERO) > 0){
				resVo.setSelfCashPay(reckonPay.doubleValue());
			}else{
				resVo.setSelfCashPay(0D);
			}
		}else{
			resVo.setSelfCashPay(amtPi);
		}

		resVo.setCashPay(0D);
		resVo.setChequePay(0D);
		resVo.setTransferAccountPay(0D);
		resVo.setCashRecharge(amtPi);
		resVo.setChequeRecharge(0D);
		resVo.setTransferRecharge(0D);
		resVo.setCashRefund(0D);
		resVo.setChequeRefund(0D);
		resVo.setTransferRefund(0D);
		resVo.setOwnAcBalance(0D);
		resVo.setReimbursementAmt(0D);
		resVo.setBalancedNumber(CommonUtils.getString(invInfo.get("busNo")));
		resVo.setOtherInfo(null);
		resVo.setOtherMedicalList(null);	
		resVo.setPayChannelDetail(enoteDao.qryStDepoList(paramMap));//查询结算首付款信息
		
		//医保报销部分也要算入交费渠道列表里
		if(resVo.getAccountPay() !=null && resVo.getAccountPay()!=0D){
			PayChannelDetailVo dtl = new PayChannelDetailVo();
			dtl.setPayChannelCode("YBK");
			dtl.setPayChannelValue(resVo.getAccountPay());
			if(resVo.getPayChannelDetail()==null){
				List<PayChannelDetailVo> dtlList = new ArrayList<>();
				resVo.setPayChannelDetail(dtlList);
			}
			resVo.getPayChannelDetail().add(dtl);
		}
		if(resVo.getFundPay() !=null && resVo.getFundPay()!=0D){
			PayChannelDetailVo dtl = new PayChannelDetailVo();
			dtl.setPayChannelCode("YBTCJJ");
			dtl.setPayChannelValue(resVo.getFundPay());
			if(resVo.getPayChannelDetail()==null){
				List<PayChannelDetailVo> dtlList = new ArrayList<>();
				resVo.setPayChannelDetail(dtlList);
			}
			resVo.getPayChannelDetail().add(dtl);
		}
		
		//其他信息
		resVo.seteBillRelateNo(null);
		resVo.setIsArrears(null);
		resVo.setArrearsReason(null);

		//项目信息
		if(!CommonUtils.isEmptyString(perConfig) && "zsrm".equals(perConfig)){//中山人民客户化
			zsrmRetSelefPayAmt(paramMap,resVo);
			zsrmListChargeDetail(paramMap,resVo);
			zsrmRemark(paramMap, resVo);
			zcjmAmt(paramMap, resVo);

			//resVo.setChargeDetail(enoteDao.qryStIpDtListZsrm(paramMap));//查询本次结算收费项目信息
			//resVo.setListDetail(enoteDao.qryStIpDtDtlListZsrm(paramMap));//查询本次结算清单信息
		}else{
			resVo.setChargeDetail(enoteDao.qryStIpDtList(paramMap));//查询本次结算收费项目信息
			resVo.setListDetail(enoteDao.qryStIpDtDtlList(paramMap));//查询本次结算清单信息
		}
		
		//校验项目信息的金额是否和总金额一致
		if(resVo.getListDetail()!=null && resVo.getListDetail().size()>0){
			List<ListDetailVo> dtls = new ArrayList<>();
			for(ListDetailVo cha : resVo.getListDetail()){
				Double amtChk = MathUtils.sub( cha.getAmtChk(), cha.getAmt());
				if(amtChk!=0D){
					cha.setAmt(MathUtils.add(cha.getAmt(), amtChk));
					
					Double amt = MathUtils.mul(amtChk, -1D);
					
					ListDetailVo dtl = new ListDetailVo();
					dtl.setListDetailNo("0");
					dtl.setChargeCode(cha.getChargeCode());
					dtl.setChargeName(cha.getChargeName());
					dtl.setCode(NHISUUID.getKeyId());
					dtl.setName("货币误差");
					dtl.setStd(amt);
					dtl.setNumber(1D);
					dtl.setAmt(amt);
					dtl.setSelfAmt(amt);
					dtl.setReceivableAmt(0D);
					dtl.setSortNo(0);
					
					dtls.add(dtl);
				}
			}
			
			if(dtls!=null && dtls.size()>0){
				resVo.getListDetail().addAll(dtls);
			}
		}
		
		//校验总金额和缴费渠道金额是否一致
		Double amtChk = 0D;
		for(PayChannelDetailVo pay : resVo.getPayChannelDetail()){
			//深大总医院个性化需求：如果支付方式里包含新冠、职业暴露、大肠癌筛查时统一修正支付方式为其他
			if(!CommonUtils.isEmptyString(perConfig) && "sugh".equals(perConfig)){
				//查询符合此需求的支付方式信息
				List<BdDefdoc> payList = enoteDao.qryPayInfoList();
				if(payList!=null && payList.size()>0){
					for(BdDefdoc docVo : payList)
					{
						if(pay.getPayChannelCode().equals(docVo.getCode())){
							pay.setPayChannelCode("99");

							//需要把此部分金额算入到otherfundPay字段，同时ownPay和selfCashPay减去相应金额
							resVo.setOwnPay(MathUtils.sub(resVo.getOwnPay(),pay.getPayChannelValue()));
							resVo.setSelfCashPay(MathUtils.sub(resVo.getSelfCashPay(),pay.getPayChannelValue()));

							resVo.setOtherfundPay(MathUtils.add(resVo.getOtherfundPay(),pay.getPayChannelValue()));
							break;
						}
					}
				}
			}
			//zsrm-处理支付类型为10、11、12、13
			if(!CommonUtils.isEmptyString(perConfig) && "zsrm".equals(perConfig)){//中山人民客户化
				
				pay.setPayChannelCode(zsrmPayChannelCodeTran(pay.getPayChannelCode()));
			}

			amtChk=MathUtils.add(amtChk, pay.getPayChannelValue());
		}
		System.out.println(JsonUtil.writeValueAsString(resVo));
		if(amtChk.compareTo(resVo.getTotalAmt())!=0){
			Double amtCe = MathUtils.sub(resVo.getTotalAmt(), amtChk);
			
			//差额算入到个人现金支付里
//			resVo.setOwnPay(MathUtils.add(resVo.getOwnPay(), amtCe));
//			resVo.setSelfCashPay(MathUtils.add(resVo.getSelfCashPay(), amtCe));
			
			PayChannelDetailVo dtl = new PayChannelDetailVo();
			dtl.setPayChannelCode("HBWC");
			dtl.setPayChannelValue(amtCe);
			if(resVo.getPayChannelDetail()==null){
				List<PayChannelDetailVo> dtlList = new ArrayList<>();
				resVo.setPayChannelDetail(dtlList);
			}
			resVo.getPayChannelDetail().add(dtl);
		}
		
		//将Data内容转换为json格式再base64编码,编码字符集UTF-8
		String dataJson = JsonUtil.writeValueAsString(resVo);
		//发送请求
		EnoteResDataVo dataVo = enoteRts(dataJson,"1.0","invoiceEBillOutpatient");
		
		return dataVo;
		
	}
	
	
	/**
	 * 获取当前纸质发票号
	 * @param args
	 * @return
	 */
	private EnoteResDataVo getPaperInvInfo(Object[] args){
		/**
		 * 入参 
		 * placeCode:操作员工号
		 * pBillBatchCode:发票前缀
		 */
		Map<String,Object> paramMap =(Map<String,Object>)args[0];
		IUser user =(IUser)args[1];
		User u = (User) user;

		//获取票据分类
		String euType = MapUtils.getString(paramMap,"euType");
		if(CommonUtils.isEmptyString(euType)){
			throw new BusException("获取纸质票据号时未传入票据分类，请联系管理员！");
		}
		String invPrefix = "";
        if(StringUtils.isNotEmpty(getPrefixByEuType(euType,u))){
			invPrefix=getPrefixByEuType(euType,u);
		}
		Map<String,Object> reqMap = new HashMap<>(16);
		reqMap.put("placeCode",  MapUtils.getString(paramMap,"placeCode"));
		reqMap.put("pBillBatchCode", billCodePaper+invPrefix);
		
		//将Data内容转换为json格式再base64编码,编码字符集UTF-8
		String dataJson = JsonUtil.writeValueAsString(reqMap);
		//发送请求
		EnoteResDataVo dataVo = enoteRts(dataJson,"1.0","getPaperBillNo");
		
		/**
		 * dataVo.message信息
		 * pBillBatchCode：纸质票据代码
		 * pBillNo：纸质票据号码
		 * */
		
		return dataVo;
	}

	
	/**
	 * 电子票据冲红
	 * @param args
	 * @return
	 */
	private EnoteResDataVo writeOffEbill(Object[] args,List<Map<String,Object>> ebillListMap){
		IUser user =(IUser)args[1];
		User u = (User) user;
		
		Map<String,Object> reqMap = new HashMap<>(16);
		//电子票据代码
		reqMap.put("billBatchCode", ebillListMap.get(0).get("ebillbatchcode"));
		//电子票据号码
		reqMap.put("billNo", ebillListMap.get(0).get("billno"));
		//冲红原因
		reqMap.put("reason", ebillListMap.get(0).get("reason"));
		//经办人
		reqMap.put("operator", u.getNameEmp());
		//业务发生时间
		reqMap.put("busDateTime", DateUtils.getDate("yyyyMMddHHmmssSSS"));
		//开票点编码
		reqMap.put("placeCode", getPlaceCode(MapUtils.getString(ebillListMap.get(0),"euType"),u));
		//将Data内容转换为json格式再base64编码,编码字符集UTF-8
		String dataJson = JsonUtil.writeValueAsString(reqMap);
		//发送请求
		EnoteResDataVo dataVo = enoteRts(dataJson,"1.0","writeOffEBill");
		
		/**
		 * dataVo.message信息
		 * eScarletBillBatchCode:电子红票票据代码
		 * eScarletBillNo:电子红票票据号码
		 * eScarletRandom:电子红票校验码
		 * createTime:电子红票生成时间  yyyyMMddHHmmssSSS
		 * billQRCode:电子票据二维码图片数据
		 * pictureUrl:电子票据H5页面URL
		 * pictureNetUrl:电子票据外网H5页面URL地址
		 * */
		
		return dataVo;
	}
	
	/**
	 * 作废换开的纸质票据
	 * @param args
	 * @return
	 */
	private EnoteResDataVo invalidPaper(Object[] args,List<Map<String,Object>> ebillListMap){
		IUser user =(IUser)args[1];
		User u = (User) user;
		
		Map<String,Object> reqMap = new HashMap<>(16);
		//纸质票据代码
		if(!CommonUtils.isEmptyString(perConfig) && "zsrm".equals(perConfig)){//中山人民客户化
			String invPrefix = "";
			if(StringUtils.isNotEmpty(getPrefixByEuType(MapUtils.getString(ebillListMap.get(0),"euType"),u))){
				invPrefix=getPrefixByEuType(MapUtils.getString(ebillListMap.get(0),"euType"),u);
			}
			reqMap.put("pBillBatchCode", ebillListMap.get(0).get("billbatchcode")+invPrefix);
		}else{
			reqMap.put("pBillBatchCode", ebillListMap.get(0).get("billbatchcode"));
		}
		//纸质票据号
		reqMap.put("pBillNo", ebillListMap.get(0).get("codeinv"));
		//开票点编码
		reqMap.put("placeCode", getPlaceCode(MapUtils.getString(ebillListMap.get(0),"euType"),u));
		//作废人
		reqMap.put("author", u.getNameEmp());
		//作废原因
		reqMap.put("reason", ebillListMap.get(0).get("reason"));
		//业务发生时间
		reqMap.put("busDateTime", DateUtils.getDate("yyyyMMddHHmmssSSS"));
		
		//将Data内容转换为json格式再base64编码,编码字符集UTF-8
		String dataJson = JsonUtil.writeValueAsString(reqMap);
		//发送请求
		EnoteResDataVo dataVo = enoteRts(dataJson,"1.0","invalidPaper");
		
		/**
		 * dataVo.message信息
		 * eScarletBillBatchCode:电子红票票据代码
		 * eScarletBillNo:电子红票票据号码
		 * eScarletRandom:电子红票校验码
		 * createTime:电子红票生成时间  yyyyMMddHHmmssSSS
		 * billQRCode:电子票据二维码图片数据
		 * pictureUrl:电子票据H5页面URL
		 * pictureNetUrl:电子票据外网H5页面URL地址
		 * */
		
		return dataVo;
	}
	
	/**
	 * 作废空白纸质票据
	 * @param args
	 * @return
	 */
	private EnoteResDataVo invalidBlankBillNo(Object[] args){
		/**
		 * 入参 
		 * pbillbatchcode:纸质票据代码
		 * pbillnostart:起始纸质票据号
		 * pbillnoend:终止纸质票据号
		 */
		Map<String,Object> paramMap =(Map<String,Object>)args[0];
		IUser user =(IUser)args[1];
		User u = (User) user;

		/**获取电子票据平台最新纸质票据号*/
		String start = MapUtils.getString(paramMap,"pBillNoStart");
		String end = MapUtils.getString(paramMap,"pBillNoEnd");
		if(StringUtils.isBlank(start)
				&& StringUtils.isBlank(end)){
			EnoteResDataVo invResVo = getPaperInvInfo(args);
			if(!"S0000".equals(invResVo.getResult())){//失败
				throw new BusException("电子票据平台："+invResVo.getMessage());
			}
			Map<String,Object> eBillMap = JsonUtil.readValue(invResVo.getMessage(), Map.class);
			start = end = MapUtils.getString(eBillMap,"pBillNo");
		}

		//获取票据分类
		String euType = MapUtils.getString(paramMap,"euType");
		if(CommonUtils.isEmptyString(euType)){
			throw new BusException("获取纸质票据号时未传入票据分类，请联系管理员！");
		}

		String invPrefix = getPrefixByEuType(euType,u);
		
		Map<String,Object> reqMap = new HashMap<>(16);
		//纸质票据代码
		//起始纸质票据号
		reqMap.put("pBillBatchCode", billCodePaper+invPrefix);
		reqMap.put("pBillNoStart", start);
		//终止纸质票据号
		reqMap.put("pBillNoEnd", end);
		//开票点编码
		reqMap.put("placeCode", paramMap.get("placeCode"));
		//作废人
		reqMap.put("author", u.getNameEmp());
		//作废原因
		reqMap.put("reason", MapUtils.getString(paramMap,"reason","其他"));
		//业务发生时间
		reqMap.put("busDateTime", DateUtils.getDate("yyyyMMddHHmmssSSS"));
		
		//将Data内容转换为json格式再base64编码,编码字符集UTF-8
		String dataJson = JsonUtil.writeValueAsString(reqMap);
		//发送请求
		EnoteResDataVo dataVo = enoteRts(dataJson,"1.0","invalidBlankBillNo");
		
		/**
		 * dataVo.message信息
		 * "成功"
		 * */
		
		return dataVo;
	}
	
	/**
	 * 换开纸质票据
	 * @param args
	 * @return
	 */
	private EnoteResDataVo turnPaper(Object[] args){
		/**
		 * 入参 
		 * billBatchCode：电子票据代码
		 * billNo：电子票据号
		 * pBillBatchCode:纸质票据代码
		 * pBillNo:纸质票据号
		 * pkSettle:结算主键
		 * 
		 */
		Map<String,Object> paramMap =(Map<String,Object>)args[0];
		
		IUser user =(IUser)args[1];
		User u = (User) user;
		
		//List<Map<String,Object>> ebillListMap =  enoteDao.qryEbillInfo(paramMap);

		//获取票据分类
		String euType = MapUtils.getString(paramMap,"euType");
		if(CommonUtils.isEmptyString(euType)){
			throw new BusException("获取纸质票据号时未传入票据分类，请联系管理员！");
		}

		String invPrefix = getPrefixByEuType(euType,u);
		
		//组织入参
		Map<String,Object> reqMap = new HashMap<>(16);
		
		//电子票据代码
		reqMap.put("billBatchCode", paramMap.get("billBatchCode"));
		//电子票据号
		reqMap.put("billNo", paramMap.get("billNo"));
		//纸质票据代码
		reqMap.put("pBillBatchCode", billCodePaper+invPrefix);
		//纸质票据号
		reqMap.put("pBillNo", paramMap.get("pBillNo"));
		//业务发生时间
		reqMap.put("busDateTime", DateUtils.getDate("yyyyMMddHHmmssSSS"));
		//开票点编码
		reqMap.put("placeCode", paramMap.get("placeCode"));
		//经办人
		reqMap.put("operator", u.getNameEmp());
		
		//将Data内容转换为json格式再base64编码,编码字符集UTF-8
		String dataJson = JsonUtil.writeValueAsString(reqMap);
		//发送请求
		EnoteResDataVo dataVo = enoteRts(dataJson,"1.0","turnPaper");
		
		/**
		 * dataVo.message信息
		 * "成功"
		 * */
		
		return dataVo;
	}
	
	/**
	 * 重新换开纸质票据
	 * @param args
	 * @return
	 */
	private EnoteResDataVo reTurnPaper(Object[] args){
		/**
		 * 入参 
		 * List<BlInvoice> invList
		 */
		Map<String,Object> paramMap = (Map<String,Object>)args[0];
		BlInvoice inv =(BlInvoice)paramMap.get("invList");
		Map<String,Object> eBillMap = (Map<String,Object>)paramMap.get("eBillMap");
		
		IUser user =(IUser)args[1];
		User u = (User) user;

		//获取票据分类
		String euType = MapUtils.getString(paramMap,"euType");
		if(CommonUtils.isEmptyString(euType)){
			throw new BusException("获取纸质票据号时未传入票据分类，请联系管理员！");
		}

		String invPrefix = getPrefixByEuType(euType,u);

		//组织入参
		Map<String,Object> reqMap = new HashMap<>(16);

		//电子票据代码
		reqMap.put("billBatchCode", inv.getEbillbatchcode());
		//电子票据号
		reqMap.put("billNo", inv.getEbillno());
		//新纸质票据代码
		reqMap.put("pBillBatchCode", billCodePaper+invPrefix);
		//新纸质票据号
		reqMap.put("pBillNo", CommonUtils.getString(eBillMap.get("pBillNo")));
		//业务发生时间
		reqMap.put("busDateTime", DateUtils.getDate("yyyyMMddHHmmssSSS"));
		//开票点编码
		reqMap.put("placeCode", paramMap.get("placeCode"));
		//经办人
		reqMap.put("operator", u.getNameEmp());
		
		//将Data内容转换为json格式再base64编码,编码字符集UTF-8
		String dataJson = JsonUtil.writeValueAsString(reqMap);
		//发送请求
		EnoteResDataVo dataVo = enoteRts(dataJson,"1.0","reTurnPaper");
		
		/**
		 * dataVo.message信息
		 * "成功"
		 * */
		
		return dataVo;
	}
	
	/**
	 * 组织主参数，调用http服务
	 * @param dataJson  数据参数
	 * @param version	服务版本
	 * @param serviceName	服务名称	
	 */
	private EnoteResDataVo enoteRts(String dataJson,String version,String serviceName){
		billLog.info("--------------------------------调用HTTP服务开始--------------------------------");
		String dataBs64Str = "";
		try {
			dataBs64Str= Base64.encodeToString(dataJson.getBytes("UTF-8"));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		
		//组织sign参数
		StringBuffer signSbf = new StringBuffer("");
		String noiseStr = NHISUUID.getKeyId();
		
		signSbf.append(String.format("appid=%s", appId));//appid
		signSbf.append(String.format("&data=%s", dataBs64Str));//data
		signSbf.append(String.format("&noise=%s", noiseStr));//noise UUID
		signSbf.append(String.format("&key=%s", key));//key
		signSbf.append(String.format("&version=%s", version));//version
		
		String signMd5 = new SimpleHash("md5",signSbf.toString()).toHex().toUpperCase();
		
		//组织主参数
		EnoteReqVo reqVo = new EnoteReqVo();
		reqVo.setAppid(appId);
		reqVo.setData(dataBs64Str);
		reqVo.setNoise(noiseStr);
		reqVo.setVersion(version);
		reqVo.setSign(signMd5);
		
		//请求参数转为json格式
		String reqJson = JsonUtil.writeValueAsString(reqVo);
		/**调用服务接口*/
		StringBuffer urlStr = new StringBuffer(String.format("%s%s", url,serviceName));
		billLog.info("调用服务地址{}",urlStr.toString());
		billLog.info("入参{}",dataJson);
		String resJson = HttpClientUtil.sendHttpPostJson(urlStr.toString(), reqJson);
		billLog.info("出参结果：{}",resJson);
		//解析响应参数
		EnoteReqVo enoteResVo = JsonUtil.readValue(resJson, EnoteReqVo.class);

		//对返回参数data做base64解码处理
		String datajson = Base64.decodeToString(enoteResVo.getData());
		//转换为data实体类
		EnoteResDataVo dataVo = JsonUtil.readValue(datajson, EnoteResDataVo.class);
		
		String jsonStr = base64ToStr(dataVo.getMessage());
		dataVo.setMessage(jsonStr);
		
		billLog.info("出参转换出的Message：{}",jsonStr);
		billLog.info("--------------------------------调用HTTP服务结束--------------------------------");
		return dataVo;
	}
	
	/**
	 * 作废当前票据号码
	 * @param args
	 */
	public Map<String,Object> invCanl(Object[] args){
		Map<String,Object> paramMap =(Map<String,Object>)args[0];
		paramMap.put("placeCode",getPlaceCode(MapUtils.getString(paramMap,"euType"),(User) args[1]));
		EnoteResDataVo invResVo = invalidBlankBillNo(args);
		if(!"S0000".equals(invResVo.getResult())){//失败
			throw new BusException("电子票据平台："+invResVo.getMessage());
		}
		
		return null;
	}
	
	/**
	 * 开立票据对外接口
	 * @param args
	 * @return
	 */
	public Map<String,Object> eBillOutpatient(Object[] args){
		billLog.info("--------------------------------开立电子票据对外接口开始--------------------------------");
		Map<String,Object> paramMap = (Map<String,Object>)args[0];
		String flagPrint = CommonUtils.getString(paramMap.get("flagPrint"));
		String pkPv = CommonUtils.getString(paramMap.get("pkPv"));
		String pkSettle = CommonUtils.getString(paramMap.get("pkSettle"));
		List<BillInfo> billList =(ArrayList<BillInfo>)paramMap.get("billList");
		List<InvInfoVo> invoInfos = (ArrayList<InvInfoVo>)paramMap.get("invoInfos");
		String dateBegin = CommonUtils.getString(paramMap.get("dateBegin"));
		String dateEnd = CommonUtils.getString(paramMap.get("dateEnd"));
		
		IUser user =(IUser)args[1];
		User u = (User) user;
		if(paramMap!=null){
			paramMap.put("placeCode",getPlaceCode(EnumerateParameter.ONE,u));
		}
		//平台当前票据号信息
		Map<String,Object> eBillMap = new HashMap<>(16);
		
		//是否打印纸质票据
		if(!CommonUtils.isEmptyString(flagPrint) && "1".equals(flagPrint)){
			paramMap.put("euType","1");
			eBillMap = getInvPrivate(paramMap,u,billList);
		}
		
		
		/**开立电子票据*/
		EnoteResDataVo resVo = ipEnotePrint(args);
		if(!"S0000".equals(resVo.getResult())){//失败
			throw new BusException("电子票据平台："+resVo.getMessage());
		}
		
		//调用组织纸质票据数据接口 ；补打电子票据不需要传入此参数，所以在这里加入非空校验
		List<BlInvoice> invo = null;
		if(invoInfos!=null && invoInfos.size()>0){
			Map<String,Object> invMap = invSettltService.invoData(pkPv, "0", invoInfos, user, dateBegin, dateEnd, pkSettle, "1");
			invo = (List<BlInvoice>) invMap.get("inv");
		}
		
		//获取返回参数
		EnotePrintResVo dataVo = JsonUtil.readValue(resVo.getMessage(), EnotePrintResVo.class);
		
		BlInvoice invInfo = new BlInvoice();//票据主表
		List<BlInvoiceDt> invDtList = new ArrayList<>();//票据明细从表
		
		//组织票据主表信息
		invInfo.setPkInvoice(NHISUUID.getKeyId());
		invInfo.setPkOrg(u.getPkOrg());
		invInfo.setEbillbatchcode(dataVo.getBillBatchCode());
		invInfo.setEbillno(dataVo.getBillNo());
		invInfo.setCheckcode(dataVo.getRandom());
		invInfo.setDateEbill(DateUtils.strToDate(dataVo.getCreateTime(),"yyyyMMddHHmmssSSS"));
		invInfo.setDateInv(DateUtils.strToDate(dataVo.getCreateTime(),"yyyyMMddHHmmssSSS"));
		//需要BASE64解码
		invInfo.setQrcodeEbill(Base64.decode(dataVo.getBillQRCode()));
		//invInfo.setQrcodeEbill(base64ToStr(dataVo.getBillQRCode()).getBytes("UTF-8"));
		invInfo.setUrlEbill(dataVo.getPictureUrl());
		invInfo.setUrlNetebill(dataVo.getPictureNetUrl());
		invInfo.setPkEmpEbill(u.getPkEmp());
		invInfo.setNameEmpEbill(u.getNameEmp());
		invInfo.setFlagCcEbill("0");
		invInfo.setFlagCcCancelEbill("0");
		invInfo.setFlagCancelEbill("0");
		invInfo.setFlagCancel("0");
		invInfo.setFlagCc("0");
		invInfo.setFlagCcCancel("0");
		invInfo.setPkEmpInv(u.getPkEmp());
		invInfo.setNameEmpInv(u.getNameEmp());
		invInfo.setPrintTimes(1);
		
		if(invo!=null && invo.size()>0){
			invInfo.setAmountInv(invo.get(0).getAmountInv());
			invInfo.setAmountPi(invo.get(0).getAmountPi());
			invInfo.setNote(invo.get(0).getNote());
			invInfo.setPkInvcate(invo.get(0).getPkInvcate());
		}
		
		//组织票据明细信息
		Map<String,Object> mapQry = new HashMap<>();
		mapQry.put("svType",EnumerateParameter.ONE);
		mapQry.put("pkOrg",u.getPkOrg());
		mapQry.put("pkPv",pkPv);
		mapQry.put("pkSettle",pkSettle);
		List<InvItemVo> itemVos = enoteDao.qryInvItemInfo(mapQry);
		if(itemVos!=null && itemVos.size()>0){
			for(InvItemVo invItem : itemVos){
				BlInvoiceDt invDt = new BlInvoiceDt();
				invDt.setPkInvoicedt(NHISUUID.getKeyId());
				invDt.setPkInvoice(invInfo.getPkInvoice());
				invDt.setPkBill(invItem.getPkInvcateitem());
				invDt.setNameBill(invItem.getName());
				invDt.setCodeBill(invItem.getCode());
				//invDt.setAmount(MathUtils.mul(rate, invItem.getAmount()));		
				invDt.setAmount(invItem.getAmount());
				invDtList.add(invDt);
 			}
		}
		
		//是否打印纸质票据
		if(!CommonUtils.isEmptyString(flagPrint) && "1".equals(flagPrint)){
			/**换开纸质票据*/
			paramMap.put("billBatchCode", invInfo.getEbillbatchcode());
			paramMap.put("billNo", invInfo.getEbillno());
			paramMap.put("pBillNo", eBillMap.get("pBillNo"));
			paramMap.put("euType","1");//住院发票
			
			EnoteResDataVo turnResVo = turnPaper(args);
			if(!"S0000".equals(turnResVo.getResult())){//失败
				throw new BusException("电子票据平台："+turnResVo.getMessage());
			}
			
//			//调用组织纸质票据数据接口
//			Map<String,Object> invMap = invSettltService.invoData(pkPv, "0", invoInfos, user, dateBegin, dateEnd, pkSettle, "1");
//			List<BlInvoice> invo = (List<BlInvoice>) invMap.get("inv");
			
			invInfo.setPkOrg(invo.get(0).getPkOrg());
			invInfo.setPkInvcate(invo.get(0).getPkInvcate());
			invInfo.setPkEmpinvoice(invo.get(0).getPkEmpinvoice());
			invInfo.setCodeInv(invo.get(0).getCodeInv());
			invInfo.setDateInv(new Date());
			
			invInfo.setPkEmpInv(invo.get(0).getPkEmpInv());
			invInfo.setNameEmpInv(invo.get(0).getNameEmpInv());
			invInfo.setPrintTimes(1);
			invInfo.setFlagCancel("0");
			invInfo.setDateCancel(null);
			invInfo.setFlagCc("0");
			invInfo.setFlagCcCancel("0");
			invInfo.setBillbatchcode(CommonUtils.getString(eBillMap.get("pBillBatchCode")));
			invInfo.setCodeSn(CommonUtils.getString(eBillMap.get("pBillNo")));
		}
		
		Map<String,Object> res = new HashMap<>(16);
		List<BlInvoice> invs = new ArrayList<>();
		invs.add(invInfo);
		res.put("inv", invs);
		res.put("invDt", invDtList);
		
		billLog.info("--------------------------------开立电子票据对外接口结束--------------------------------");
		return res;
	}
	
	/**
	 * 票据作废
	 * @param args
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public Map<String,Object> billCancel(Object[] args){
		billLog.info("--------------------------------作废票据对外接口开始--------------------------------");
		/**
		 * 入参 
		 * pkSettle:结算主键
		 */
		Map<String,Object> paramMap = (Map<String,Object>)args[0];
		
		IUser user =(IUser)args[1];
		User u = (User) user;
		
		//获取票据信息判断code_inv是否为空，空调用电子票据冲红，否则调用作废纸质票据
		List<Map<String,Object>> ebillListMap =  enoteDao.qryEbillInfo(paramMap);
		
		EnoteResDataVo resVo = new EnoteResDataVo();
		if(ebillListMap!=null && ebillListMap.size()>0){
			if(ebillListMap.get(0).containsKey("codeinv") && ebillListMap.get(0).get("codeinv")!=null && 
					!CommonUtils.isEmptyString(CommonUtils.getString(ebillListMap.get(0).get("codeinv"))) && 
					ebillListMap.get(0).containsKey("billbatchcode") && ebillListMap.get(0).get("billbatchcode")!=null && 
					!CommonUtils.isEmptyString(CommonUtils.getString(ebillListMap.get(0).get("billbatchcode")))){
				//已经作废的，再次作废接口会返回失败，所以先查询状态,state=1未作废时，再请求作废
				//Map<String, Object> billState = getEBillStates(new Object[]{ebillListMap.get(0)});
				//if(EnumerateParameter.ONE.equals(MapUtils.getString(billState,"state"))) {
				//作废纸质票据
				resVo = invalidPaper(args, ebillListMap);
				//}
			}else if(ebillListMap.get(0).containsKey("ebillbatchcode") && ebillListMap.get(0).get("ebillbatchcode")!=null && 
					!CommonUtils.isEmptyString(CommonUtils.getString(ebillListMap.get(0).get("ebillbatchcode")))){
				//电子票据冲红 ~~这里已经冲销的发票，调用getEBillStates直接返回500，所以，下面使用message来判断了
				resVo = writeOffEbill(args, ebillListMap);
			}
		}
		
		/**
		 * resVo.message信息
		 * eScarletBillBatchCode:电子红票票据代码
		 * eScarletBillNo:电子红票票据号码
		 * eScarletRandom:电子红票校验码
		 * createTime:电子红票生成时间  yyyyMMddHHmmssSSS
		 * billQRCode:电子票据二维码图片数据
		 * pictureUrl:电子票据H5页面URL
		 * pictureNetUrl:电子票据外网H5页面URL地址
		 * */
		if(resVo!=null && !CommonUtils.isEmptyString(resVo.getResult())){
			if(!"S0000".equals(resVo.getResult())){//失败

				if(StringUtils.contains(resVo.getMessage(),"已冲销，不能冲销开票")){
					billLog.info("电子票据平台：{}",resVo.getMessage());
					billLog.info("--------------------------------作废票据对外接口结束--------------------------------");
					return null;
				}
				throw new BusException("电子票据平台："+resVo.getMessage());
			}
			Map<String,Object> dataVo = JsonUtil.readValue(resVo.getMessage(), Map.class);
			
			if(dataVo!=null && dataVo.size()>0){
				//查询结算票据完整信息
				List<BlInvoice> invList = enoteDao.qryInvInfoByPkSettle(paramMap);
				
				invList.get(0).setEbillbatchcodeCancel(CommonUtils.getString(dataVo.get("eScarletBillBatchCode")));
				invList.get(0).setEbillnoCancel(CommonUtils.getString(dataVo.get("eScarletBillNo")));
				invList.get(0).setCheckcodeCancel(CommonUtils.getString(dataVo.get("eScarletRandom")));
				invList.get(0).setDateEbillCancel(DateUtils.strToDate(CommonUtils.getString(dataVo.get("createTime")),"yyyyMMddHHmmssSSS"));
//				try {
//					invList.get(0).setQrcodeEbillCancel(base64ToStr(CommonUtils.getString(dataVo.get("billQRCode"))).getBytes("UTF-8"));
//				} catch (UnsupportedEncodingException e) {
//					e.printStackTrace();
//				}
				invList.get(0).setQrcodeEbillCancel(Base64.decode(CommonUtils.getString(dataVo.get("billQRCode"))));
				invList.get(0).setUrlEbillCancel(CommonUtils.getString(dataVo.get("pictureUrl")));
				invList.get(0).setUrlNetebillCancel(CommonUtils.getString(dataVo.get("pictureNetUrl")));
				invList.get(0).setFlagCancelEbill("1");
				invList.get(0).setPkEmpCancelEbill(u.getPkEmp());
				invList.get(0).setNameEmpCancelEbill(u.getNameEmp());
				invList.get(0).setFlagCcCancelEbill("0");
				invList.get(0).setDateEbillCancel(new Date());
				
				//修改票据信息
				DataBaseHelper.batchUpdate(DataBaseHelper.getUpdateSql(BlInvoice.class), invList);
			}
			
		}
		billLog.info("--------------------------------作废票据对外接口结束--------------------------------");
		return null;
	}
	
	/**
	 * 重打纸质票据
	 * @param args
	 * @return
	 */
	public Map<String,Object> rePaperInv(Object[] args){
		billLog.info("--------------------------------重打电子票据对外接口开始--------------------------------");
		Map<String,Object> paramMap = (Map<String,Object>)args[0];
		String pkSettle = CommonUtils.getString(paramMap.get("pkSettle"));
		List<BillInfo> billList =(ArrayList<BillInfo>)paramMap.get("billList");
		
		IUser user =(IUser)args[1];
		User u = (User) user;
		
		//平台当前票据号信息
		Map<String,Object> eBillMap = new HashMap<>(16);
		
		//查询本次结算有效票据信息
		List<BlInvoice> invList = enoteDao.qryInvInfoByPkSettle(paramMap);
		//是否重新打印纸质票据
		if(invList!=null && invList.size()>0){
			for(BlInvoice inv : invList){
				if(!CommonUtils.isEmptyString(inv.getCodeInv()) &&
						!CommonUtils.isEmptyString(inv.getEbillbatchcode()) &&
						!CommonUtils.isEmptyString(inv.getEbillno())){
					String euType = MapUtils.getString(DataBaseHelper.queryForMap("select EU_TYPE from BD_INVCATE where PK_INVCATE=?",new Object[]{CommonUtils.isEmptyString(inv.getPkInvcate())?billList.get(0).getPkInvcate():inv.getPkInvcate()}),"euType");
					paramMap.put("placeCode",getPlaceCode(euType,u));
					paramMap.put("euType", euType);
					eBillMap = getInvPrivate(paramMap,u,billList);

					/**调用重打票据接口*/
					paramMap.put("invList", inv);//his票据主表信息
					paramMap.put("eBillMap", eBillMap);//电子票据平台返回票据号信息
					EnoteResDataVo invReVo = reTurnPaper(new Object[]{paramMap,args[1]});
					if(!"S0000".equals(invReVo.getResult())){//失败
						/**调用打印纸质票据接口*/
						paramMap.put("billBatchCode", invList.get(0).getEbillbatchcode());
						paramMap.put("billNo", invList.get(0).getEbillno());
						paramMap.put("pBillBatchCode", eBillMap.get("pBillBatchCode"));
						paramMap.put("pBillNo", eBillMap.get("pBillNo"));

						EnoteResDataVo turnResVo = turnPaper(args);
						if(!"S0000".equals(turnResVo.getResult())){//失败
							throw new BusException("第一次失败电子票据平台："+invReVo.getMessage()+"第二次失败电子票据平台："+turnResVo.getMessage());
						}
					}

					/**更新票据信息*/
					//更新已作废票据的电子票据信息
					DataBaseHelper.execute(
							"update BL_INVOICE set EBILLBATCHCODE = null,ebillno=null,checkcode=null,QRCODE_EBILL=null,URL_EBILL=null,URL_NETEBILL=null  where FLAG_CANCEL = '1' and PK_INVOICE in(select BL_ST_INV.PK_INVOICE from BL_ST_INV where PK_SETTLE = ?)",
							new Object[]{pkSettle});
					//更新新生成票据信息的纸质票据代码
					inv.setBillbatchcode(CommonUtils.getString(eBillMap.get("pBillBatchCode")));
					inv.setCodeSn(CommonUtils.getString(eBillMap.get("pBillNo")));
					if(CommonUtils.isEmptyString(inv.getPkInvcate())){
						inv.setPkInvcate(billList.get(0).getPkInvcate());
					}
					
					DataBaseHelper.update(DataBaseHelper.getUpdateSql(BlInvoice.class), inv);
					break;
				}
			}
		}
		
		billLog.info("--------------------------------重打电子票据对外接口结束--------------------------------");
		
		return null;
	}
	
	/**
	 * 门诊挂号开立票据对外接口
	 * @param args
	 * @return
	 */
	public Map<String,Object> eBillRegistration(Object[] args){
		billLog.info("--------------------------------门诊挂号开立电子票据对外接口开始--------------------------------");
		Map<String,Object> paramMap = (Map<String,Object>)args[0];
		String flagPrint = CommonUtils.getString(paramMap.get("flagPrint"));
		String pkPv = CommonUtils.getString(paramMap.get("pkPv"));
		String pkSettle = CommonUtils.getString(paramMap.get("pkSettle"));
		List<BillInfo> billList =(ArrayList<BillInfo>)paramMap.get("billList");
		
		IUser user =(IUser)args[1];
		User u = (User) user;
		if(paramMap!=null){
			paramMap.put("placeCode",getPlaceCode(EnumerateParameter.ZERO,u));
		}
		//平台当前票据号信息
		Map<String,Object> eBillMap = new HashMap<>(16);
		
		//是否打印纸质票据
		if(!CommonUtils.isEmptyString(flagPrint) && "1".equals(flagPrint)){
			//获取可用发票
			paramMap.put("euType","0");
			eBillMap = getInvPrivate(paramMap,u,billList);
		}
		
		
		/**开立电子票据*/
		EnoteResDataVo resVo = invEBillRegistration(args);
		if(!"S0000".equals(resVo.getResult())){//失败
			throw new BusException("电子票据平台："+resVo.getMessage());
		}
		
		BlSettle stInfo = DataBaseHelper.queryForBean(
				"select * from bl_settle where pk_settle = ?",
				BlSettle.class, new Object[]{pkSettle});
		
		//获取返回参数
		EnotePrintResVo dataVo = JsonUtil.readValue(resVo.getMessage(), EnotePrintResVo.class);
		
		BlInvoice invInfo = new BlInvoice();//票据主表
		List<BlInvoiceDt> invDtList = new ArrayList<>();//票据明细从表
		
		//组织票据主表信息
		invInfo.setPkInvoice(NHISUUID.getKeyId());
		invInfo.setPkOrg(u.getPkOrg());
		invInfo.setEbillbatchcode(dataVo.getBillBatchCode());
		invInfo.setEbillno(dataVo.getBillNo());
		invInfo.setCheckcode(dataVo.getRandom());
		invInfo.setDateEbill(DateUtils.strToDate(dataVo.getCreateTime(),"yyyyMMddHHmmssSSS"));
		invInfo.setDateInv(DateUtils.strToDate(dataVo.getCreateTime(),"yyyyMMddHHmmssSSS"));
		invInfo.setQrcodeEbill(Base64.decode(dataVo.getBillQRCode()));
		invInfo.setUrlEbill(dataVo.getPictureUrl());
		invInfo.setUrlNetebill(dataVo.getPictureNetUrl());
		invInfo.setPkEmpEbill(u.getPkEmp());
		invInfo.setNameEmpEbill(u.getNameEmp());
		invInfo.setFlagCcEbill("0");
		invInfo.setFlagCcCancelEbill("0");
		invInfo.setFlagCancelEbill("0");
		invInfo.setFlagCancel("0");
		invInfo.setFlagCc("0");
		invInfo.setFlagCcCancel("0");
		invInfo.setPkEmpInv(u.getPkEmp());
		invInfo.setNameEmpInv(u.getNameEmp());
		invInfo.setPkEmpEbill(u.getPkEmp());
		invInfo.setNameEmpEbill(u.getNameEmp());
		invInfo.setDateEbill(new Date());
		invInfo.setAmountInv(stInfo.getAmountSt().doubleValue());
		invInfo.setAmountPi(stInfo.getAmountPi().doubleValue());
		invInfo.setPrintTimes(0);
		if(billList!=null && billList.size()>0){
			invInfo.setPkInvcate(billList.get(0).getPkInvcate());
		}else {
			String sql = "select * from BD_INVCATE WHERE EU_TYPE='0' AND DEL_FLAG ='0' ";
			List<BdInvcate> bdInvcate = DataBaseHelper.queryForList(sql,BdInvcate.class);
			if(bdInvcate != null && bdInvcate.size()>0) {
				invInfo.setPkInvcate(bdInvcate.get(0).getPkInvcate());
			}
		}
		
		//组织票据明细信息
		Map<String,Object> mapQry = new HashMap<>();
		mapQry.put("svType",EnumerateParameter.ZERO);
		mapQry.put("pkOrg",u.getPkOrg());
		mapQry.put("pkPv",pkPv);
		mapQry.put("pkSettle",pkSettle);
		List<InvItemVo> itemVos = enoteDao.qryInvItemInfo(mapQry);
		if(itemVos!=null && itemVos.size()>0){
			for(InvItemVo invItem : itemVos){
				BlInvoiceDt invDt = new BlInvoiceDt();
				invDt.setPkInvoicedt(NHISUUID.getKeyId());
				invDt.setPkInvoice(invInfo.getPkInvoice());
				invDt.setPkBill(invItem.getPkInvcateitem());
				invDt.setNameBill(invItem.getName());
				invDt.setCodeBill(invItem.getCode());
				invDt.setAmount(invItem.getAmount());
				invDtList.add(invDt);
 			}
		}
		
		//是否打印纸质票据
		if(!CommonUtils.isEmptyString(flagPrint) && "1".equals(flagPrint)){
			/**换开纸质票据*/
			paramMap.put("billBatchCode", invInfo.getEbillbatchcode());
			paramMap.put("billNo", invInfo.getEbillno());
			paramMap.put("pBillBatchCode", billCodePaper);
			paramMap.put("pBillNo", eBillMap.get("pBillNo"));
			paramMap.put("euType", "0");
			
			EnoteResDataVo turnResVo = turnPaper(args);
			if(!"S0000".equals(turnResVo.getResult())){//失败
				throw new BusException("电子票据平台："+turnResVo.getMessage());
			}
			
			invInfo.setPkOrg(u.getPkOrg());
			invInfo.setPkInvcate(billList.get(0).getPkInvcate());
			invInfo.setPkEmpinvoice(billList.get(0).getPkEmpinv());
			invInfo.setCodeInv(billList.get(0).getCurCodeInv());
			invInfo.setDateInv(new Date());
			
			invInfo.setPkEmpInv(u.getPkEmp());
			invInfo.setNameEmpInv(u.getNameEmp());
			invInfo.setPrintTimes(1);
			invInfo.setFlagCancel("0");
			invInfo.setDateCancel(null);
			invInfo.setFlagCc("0");
			invInfo.setFlagCcCancel("0");
			invInfo.setBillbatchcode(billCodePaper);
			invInfo.setCodeSn(CommonUtils.getString(eBillMap.get("pBillNo")));
		}
		
		Map<String,Object> res = new HashMap<>(16);
		List<BlInvoice> invs = new ArrayList<>();
		invs.add(invInfo);
		
		for(BlInvoice vo:invs){
			setDefaultValue(vo, true);
		}
		for(BlInvoiceDt vo:invDtList){
			setDefaultValue(vo, true);
		}
		
		//批量补打不插入
		List<BlStInv> stInvList = new ArrayList<>();
		for(BlInvoice invo :invs){
			BlStInv vo = new BlStInv();
			vo.setPkInvoice(invo.getPkInvoice());
			vo.setPkOrg(invo.getPkOrg());
			vo.setPkSettle(pkSettle);
			vo.setPkStinv(NHISUUID.getKeyId());
			setDefaultValue(vo, true);
			stInvList.add(vo);
		}

		if(CommonUtils.isEmptyString(CommonUtils.getPropValueStr(paramMap, "flagUpdate"))  || "0".equals(CommonUtils.getPropValueStr(paramMap, "flagUpdate"))) {
			DataBaseHelper.batchUpdate(DataBaseHelper.getInsertSql(BlInvoice.class), invs);
			DataBaseHelper.batchUpdate(DataBaseHelper.getInsertSql(BlInvoiceDt.class), invDtList);
			DataBaseHelper.batchUpdate(DataBaseHelper.getInsertSql(BlStInv.class), stInvList);
		}
		res.put("inv", invs);
		res.put("invDt", invDtList);
		res.put("stInv", stInvList);
		res.put("invs", invs);
		billLog.info("--------------------------------门诊挂号开立电子票据对外接口结束--------------------------------");
		return res;
	}

	private Map<String,Object> getPaperInvInfo(Map<String,Object> paramMap, User u){
		EnoteResDataVo invResVo = getPaperInvInfo(new Object[]{paramMap,u});
		if(!"S0000".equals(invResVo.getResult())){//失败
			throw new BusException("电子票据平台："+invResVo.getMessage());
		}
		//校验平台返回的号码是否和his系统的号码一致
		Map<String,Object> eBillMap = JsonUtil.readValue(invResVo.getMessage(), Map.class);
		if(eBillMap==null || eBillMap.size()<=0 ||
				!eBillMap.containsKey("pBillNo") || eBillMap.get("pBillNo")==null){
			throw new BusException("电子票据平台："+"平台返回发票号码为空,请联系管理员！");
		}
		return eBillMap;
	}

	/**
	 * 获取当前发票，如果比对方的大，会自动调用空票作废接口，然后再次重新获取
	 * @param paramMap
	 * @param u
	 * @param billList
	 * @return
	 */
	private Map<String,Object> getInvPrivate(Map<String,Object> paramMap, User u,List<BillInfo> billList){
		Map<String,Object> eBillMap = getPaperInvInfo(paramMap,u);
		String curNo;
		if(!CommonUtils.isEmptyString(perConfig) && "zsrm".equals(perConfig)){//中山人民客户化
			//中山人民票号前不足8位补0
			curNo =String.format("%8d", Long.parseLong(CommonUtils.getString(billList.get(0).getCurNo()))).replace(" ", "0");
		}else{
			curNo = CommonUtils.getString(billList.get(0).getCurNo());
		}
		String pBillNo = MapUtils.getString(eBillMap,"pBillNo");
		if(!curNo.equals(pBillNo)){
			//如果是我们的比对方的号大，证明我们有可能有段时间未其使用点在票据并未对接，开--关--开--关---开。。调用作废空票据
//			if(curNo.compareTo(pBillNo) >0){
//				paramMap.put("pBillBatchCode", billCodePaper);
//				paramMap.put("pBillNoStart",pBillNo);
//				paramMap.put("pBillNoEnd",Long.valueOf(curNo)-1);
//				paramMap.put("placeCode", MapUtils.getString(paramMap,"placeCode"));
//				paramMap.put("author", u.getNameEmp());
//				paramMap.put("reason", "空白纸质票据作废:"+pBillNo+"~"+paramMap.get("pBillNoEnd"));
//				EnoteResDataVo resDataVo = invalidBlankBillNo(new Object[]{paramMap,u});
//				if(!"S0000".equals(resDataVo.getResult())){//失败
//					throw new BusException("电子票据平台："+resDataVo.getMessage());
//				}
//				//重新获取，否则对方会报“无可用票据”,,这里只获取一次
//				eBillMap = getPaperInvInfo(paramMap,u);
//			} else{
				throw new BusException("电子票据平台："+"系统发票号码【"+billList.get(0).getCurNo()+"】和平台发票号码【"+CommonUtils.getString(eBillMap.get("pBillNo"))+"】不一致,请作废空票！");
			//}

		}
		return eBillMap;
	}
	/**
	 * 门诊结算开立票据对外接口
	 * @param args
	 * @return
	 */
	public Map<String,Object> eBillMzOutpatient(Object[] args){
		billLog.info("--------------------------------门诊结算开立电子票据对外接口开始--------------------------------");
		Map<String,Object> paramMap = (Map<String,Object>)args[0];
		String flagPrint = MapUtils.getString(paramMap,"flagPrint","0");
		String pkPv = CommonUtils.getString(paramMap.get("pkPv"));
		String pkSettle = CommonUtils.getString(paramMap.get("pkSettle"));
		List<BillInfo> billList =(ArrayList<BillInfo>)paramMap.get("billList");

		IUser user =(IUser)args[1];
		User u = (User) user;
		if(paramMap!=null){
			paramMap.put("placeCode",getPlaceCode(EnumerateParameter.ZERO,u));
		}
		//平台当前票据号信息
		Map<String,Object> eBillMap = new HashMap<>(16);
		
		//是否打印纸质票据
		if("1".equals(flagPrint)){
			paramMap.put("euType","0");
			eBillMap = getInvPrivate(paramMap,u,billList);
		}
		
		
		/**开立电子票据*/
		EnoteResDataVo resVo = invEBillOutpatient(args);
		if(!"S0000".equals(resVo.getResult())){//失败
			throw new BusException("电子票据平台："+resVo.getMessage());
		}
		
		BlSettle stInfo = DataBaseHelper.queryForBean(
				"select * from bl_settle where pk_settle = ?",
				BlSettle.class, new Object[]{pkSettle});
		
		//获取返回参数
		EnotePrintResVo dataVo = JsonUtil.readValue(resVo.getMessage(), EnotePrintResVo.class);
		
		BlInvoice invInfo = new BlInvoice();//票据主表
		List<BlInvoiceDt> invDtList = new ArrayList<>();//票据明细从表
		
		//组织票据主表信息
		invInfo.setPkInvoice(NHISUUID.getKeyId());
		invInfo.setPkOrg(u.getPkOrg());
		invInfo.setEbillbatchcode(dataVo.getBillBatchCode());
		invInfo.setEbillno(dataVo.getBillNo());
		invInfo.setCheckcode(dataVo.getRandom());
		invInfo.setDateEbill(DateUtils.strToDate(dataVo.getCreateTime(),"yyyyMMddHHmmssSSS"));
		invInfo.setDateInv(DateUtils.strToDate(dataVo.getCreateTime(),"yyyyMMddHHmmssSSS"));
		invInfo.setQrcodeEbill(Base64.decode(dataVo.getBillQRCode()));
		invInfo.setUrlEbill(dataVo.getPictureUrl());
		invInfo.setUrlNetebill(dataVo.getPictureNetUrl());
		invInfo.setPkEmpEbill(u.getPkEmp());
		invInfo.setNameEmpEbill(u.getNameEmp());
		invInfo.setFlagCcEbill("0");
		invInfo.setFlagCcCancelEbill("0");
		invInfo.setFlagCancelEbill("0");
		invInfo.setFlagCancel("0");
		invInfo.setFlagCc("0");
		invInfo.setFlagCcCancel("0");
		invInfo.setPkEmpInv(u.getPkEmp());
		invInfo.setNameEmpInv(u.getNameEmp());
		invInfo.setPkEmpEbill(u.getPkEmp());
		invInfo.setNameEmpEbill(u.getNameEmp());
		invInfo.setDateEbill(new Date());
		invInfo.setAmountInv(stInfo.getAmountSt().doubleValue());
		invInfo.setAmountPi(stInfo.getAmountPi().doubleValue());
		invInfo.setPrintTimes(0);
		if(billList!=null && billList.size()>0){
			invInfo.setPkInvcate(billList.get(0).getPkInvcate());
		}else {
			String sql = "select * from BD_INVCATE WHERE EU_TYPE='0' AND DEL_FLAG ='0' ";
			List<BdInvcate> bdInvcate = DataBaseHelper.queryForList(sql,BdInvcate.class);
			if(bdInvcate != null && bdInvcate.size()>0) {
				invInfo.setPkInvcate(bdInvcate.get(0).getPkInvcate());
			}
		}
		
		//组织票据明细信息
		Map<String,Object> mapQry = new HashMap<>();
		mapQry.put("svType",EnumerateParameter.ZERO);
		mapQry.put("pkOrg",u.getPkOrg());
		mapQry.put("pkPv",pkPv);
		mapQry.put("pkSettle",pkSettle);
		List<InvItemVo> itemVos = enoteDao.qryInvItemInfo(mapQry);
		if(itemVos!=null && itemVos.size()>0){
			for(InvItemVo invItem : itemVos){
				BlInvoiceDt invDt = new BlInvoiceDt();
				invDt.setPkInvoicedt(NHISUUID.getKeyId());
				invDt.setPkInvoice(invInfo.getPkInvoice());
				invDt.setPkBill(invItem.getPkInvcateitem());
				invDt.setNameBill(invItem.getName());
				invDt.setCodeBill(invItem.getCode());
				invDt.setAmount(invItem.getAmount());
				invDtList.add(invDt);
 			}
		}
		
		//是否打印纸质票据
		if("1".equals(flagPrint)){
			/**换开纸质票据*/
			paramMap.put("billBatchCode", invInfo.getEbillbatchcode());
			paramMap.put("billNo", invInfo.getEbillno());
			paramMap.put("pBillBatchCode", billCodePaper);
			paramMap.put("pBillNo", eBillMap.get("pBillNo"));
			paramMap.put("euType", "0");
			
			EnoteResDataVo turnResVo = turnPaper(args);
			if(!"S0000".equals(turnResVo.getResult())){//失败
				throw new BusException("电子票据平台："+turnResVo.getMessage());
			}
			
			invInfo.setPkOrg(u.getPkOrg());
			invInfo.setPkInvcate(billList.get(0).getPkInvcate());
			invInfo.setPkEmpinvoice(billList.get(0).getPkEmpinv());
			invInfo.setCodeInv(billList.get(0).getCurCodeInv());
			invInfo.setDateInv(new Date());
			
			invInfo.setPkEmpInv(u.getPkEmp());
			invInfo.setNameEmpInv(u.getNameEmp());
			invInfo.setPrintTimes(1);
			invInfo.setFlagCancel("0");
			invInfo.setDateCancel(null);
			invInfo.setFlagCc("0");
			invInfo.setFlagCcCancel("0");
			invInfo.setBillbatchcode(billCodePaper);
			invInfo.setCodeSn(CommonUtils.getString(eBillMap.get("pBillNo")));
		}
		
		Map<String,Object> res = new HashMap<>(16);
		List<BlInvoice> invs = new ArrayList<>();
		List<BlStInv> stInvList = new ArrayList<>();
		
		invs.add(invInfo);
		
		for(BlInvoice vo:invs){
			setDefaultValue(vo, true);
		}
		for(BlInvoiceDt vo:invDtList){
			setDefaultValue(vo, true);
		}
		
		
		for(BlInvoice invo :invs){
			BlStInv vo = new BlStInv();
			vo.setPkInvoice(invo.getPkInvoice());
			vo.setPkOrg(invo.getPkOrg());
			vo.setPkSettle(pkSettle);
			vo.setPkStinv(NHISUUID.getKeyId());
			setDefaultValue(vo, true);
			stInvList.add(vo);
		}

		res.put("inv", invs);
		res.put("invDt", invDtList);
		res.put("stInv", stInvList);
		
		billLog.info("--------------------------------门诊结算开立电子票据对外接口结束--------------------------------");
		return res;
	}

	public Map<String,Object> getEBillStates(Object[] args){
		billLog.info("--------------------------------4.1.7.根据电子票信息获取电子票据状态接口开始--------------------------------");
		Map<String,Object> paramMap = (Map<String,Object>)args[0];
		//入参billBatchCode	电子票据代码、billNo	电子票据号码
		Map<String,Object> eBillMap = new HashMap<>(16);
		eBillMap.put("billBatchCode", MapUtils.getString(paramMap,"billbatchcode"));
		eBillMap.put("billNo", MapUtils.getString(paramMap,"codeinv"));
		String dataJson = JsonUtil.writeValueAsString(eBillMap);
		//发送请求
		EnoteResDataVo dataVo = enoteRts(dataJson,"1.0","getEBillStatesByBillInfo");
		if(!"S0000".equals(dataVo.getResult())){//失败
			throw new BusException("调用获取电子票据状态返回失败："+dataVo.getMessage());
		}
		eBillMap.clear();
		eBillMap.putAll(JsonUtil.readValue(dataVo.getMessage(), new TypeReference<Map<String,Object>>() {}));
		//返回字段说明~目前其实就用到state--状态：1正常，2作废	isScarlet：0未开红票，1已开红票
		billLog.info("--------------------------------4.1.7.根据电子票信息获取电子票据状态接口结束--------------------------------");
		return eBillMap;
	}

	public static void setDefaultValue(Object obj, boolean flag) {
		
		User user = UserContext.getUser();
	
		Map<String,Object> default_v = new HashMap<String,Object>();
		if(flag){  // 如果新增
			default_v.put("pkOrg", user.getPkOrg());
			default_v.put("creator", user.getPkEmp());
			default_v.put("createTime",new Date());
			default_v.put("delFlag", "0");
		}
		
		default_v.put("ts", new Date());
		default_v.put("modifier",  user.getPkEmp());
		
		Set<String> keys = default_v.keySet();
		
		for(String key : keys){
			Field field = ReflectHelper.getTargetField(obj.getClass(), key);
			if (field != null) {
				ReflectHelper.setFieldValue(obj, key, default_v.get(key));
			}
		}
	
	}
	
	private String base64ToStr(String base64Str){
		try {
			return Base64.decodeToString(base64Str.getBytes("UTF-8"));
		} catch (UnsupportedEncodingException e) {
			billLog.error("编码异常",e);
			throw new BusException("编码异常");
		}
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		url=ApplicationUtils.getPropertyValue("eNote.url", "");
		appId=ApplicationUtils.getPropertyValue("eNote.appId", "");
		key=ApplicationUtils.getPropertyValue("eNote.key", "");
		billCodePaper=ApplicationUtils.getPropertyValue("eNote.billCodePaper", "");
		placeCode=ApplicationUtils.getPropertyValue("eNote.placeCode", "");
		perConfig=ApplicationUtils.getPropertyValue("eNote.perConfig", "");
		hospCode = ApplicationUtils.getPropertyValue("eNote.hospCode", "");
		insuAddr = ApplicationUtils.getPropertyValue("eNote.insuAddr", "");
		if(!CommonUtils.isEmptyString(insuAddr)){
			insuAddr = new String (insuAddr.getBytes("ISO8859-1"),"UTF-8");
			insuAddr = insuAddr+"-";
		}
	}

	private String getPlaceCode(String euType,User u){
		if(StringUtils.isBlank(euType) || u == null){
			return u.getCodeEmp();
		}
		if(EnumerateParameter.ONE.equals(euType)){
			//住院
			return u.getCodeEmp();
		} else if(EnumerateParameter.ZERO.equals(euType)){
			//门诊
			return placeCode+u.getCodeEmp();
		}
		return null;
	}

	private String getPrefixByEuType(String euType,User u){
		String invPrefix = null;

		if(CommonUtils.isEmptyString(euType)){
			throw new BusException("获取当前使用票据信息未传入票据分类，请联系管理员！");
		}

		Map<String,Object> qryParam = new HashMap<>(16);
		qryParam.put("euType",euType);
		qryParam.put("pkEmp",u.getPkEmp());

		List<BlEmpInvoice> invList = enoteDao.qryPrefixByEuType(qryParam);

		if(invList!=null && invList.size()>0){
			invPrefix = invList.get(0).getInvPrefix();
		}

		return invPrefix;
	}

	/**
	 * zsrm-项目化-处理特诊加收、特需费用显示一条费用明细
	 * zsrm_任务[4830]
	 * @param paramMap
	 * @param resVo
	 */
   public void zsrmListChargeDetail(Map<String,Object> paramMap,EnotePrintReqVo resVo){
	   List<ChargeDetailVo> ChargeDetail =  enoteDao.qryStIpDtListZsrm(paramMap);//查询本次结算收费项目信息
	   List<ListDetailVo> detaiList = enoteDao.qryStIpDtDtlListZsrm(paramMap);//查询本次结算清单信息

	   BigDecimal Payamt = BigDecimal.ZERO;
	   Double amount = enoteDao.qryDtSumAmountZsrm(paramMap);
	   if(amount != null){
		   Payamt = Payamt.add(BigDecimal.valueOf(amount));
	   }
	   Double amountAdd = enoteDao.qryDtSumAmountAddZsrm(paramMap);
	   if(amountAdd != null){
		   Payamt = Payamt.add(BigDecimal.valueOf(amountAdd));
	   }

	   if(Payamt.compareTo(BigDecimal.ZERO) != 0){
		   ChargeDetailVo chargeDetail = new ChargeDetailVo();
		   chargeDetail.setChargeCode("020");
		   chargeDetail.setChargeName("特需费");
		   chargeDetail.setStd(Payamt.doubleValue());
		   chargeDetail.setNumber(1D);
		   chargeDetail.setAmt(Payamt.doubleValue());
		   chargeDetail.setSelfAmt(Payamt.doubleValue());
		   ChargeDetail.add(chargeDetail);

		   ListDetailVo Detail = new ListDetailVo();
		   Detail.setListDetailNo("0");
		   Detail.setChargeCode("020");
		   Detail.setChargeName("特需费");
		   Detail.setName("特需服务费");
		   Detail.setStd(Payamt.doubleValue());
		   Detail.setNumber(1D);
		   Detail.setAmtChk(Payamt.doubleValue());
		   Detail.setSelfAmtChk(Payamt.doubleValue());
		   Detail.setAmt(Payamt.doubleValue());
		   Detail.setSelfAmt(Payamt.doubleValue());
		   Detail.setReceivableAmt(0D);
		   Detail.setSortNo(0);
		   detaiList.add(Detail);
	   }

	   resVo.setChargeDetail(ChargeDetail);//添加本次结算收费项目信息
	   resVo.setListDetail(detaiList);//添加本次结算清单信息
	   //添加微信扫码付款订单号
	   List<String> serialList = enoteDao.qrySerialNoByPkSettleZsrm(paramMap);
	   if(serialList.size()>0) {
		   resVo.setWeChatOrderNo(serialList.get(0));
	   }
   }
   /**
	 * zsrm-项目化-重新计算自付金额,个人自费金额
	 * 个人自付+个人自费=个人账户+个人现金
	 * @param paramMap
	 * @param resVo
	 */
  public void zsrmRetSelefPayAmt(Map<String,Object> paramMap,EnotePrintReqVo resVo){
	  Double selfConceitedAmt = enoteDao.qrySelfConceitedAmtPkSettleZsrm(paramMap);
	  if(selfConceitedAmt==null){
		  selfConceitedAmt = 0D;
		}
	  Double ownPay=MathUtils.sub(resVo.getOwnPay(),selfConceitedAmt);
	  if(selfConceitedAmt>=0 &&ownPay>=0 ){
		  resVo.setSelfPayAmt(selfConceitedAmt);
		  resVo.setOwnPay(ownPay);
	  }else{
		  billLog.error("医保个人自付金额小于0,"+ownPay+"|"+selfConceitedAmt+"请核查 pkSettle:"+CommonUtils.getString(paramMap.get("pkSettle")));
	  }
  }
  /**
	 * zsrm-项目化-个性化备注信息
	 * @param paramMap
	 * @param resVo
	 */
  public void zsrmRemark(Map<String,Object> paramMap,EnotePrintReqVo resVo){
	  
	  for (PayChannelDetailVo depo : resVo.getPayChannelDetail()) {
		if("26".equals(depo.getPayChannelCode())){
			resVo.setRemark("个人现金支付含重点保健对象补助金额"+depo.getPayChannelValue()+"元");
		}else if("11".equals(depo.getPayChannelCode())){
			resVo.setRemark("个人现金支付含慈爱基金金额"+depo.getPayChannelValue()+"元");
		}	
	}
	  
	}
  	
	/**政策减免金额
	 * @param paramMap
	 * @param resVo
	 */
	public void zcjmAmt(Map<String, Object> paramMap, EnotePrintReqVo resVo) {
		BlSettle stInfo = DataBaseHelper.queryForBean(
				"select * from bl_settle where pk_settle = ?",
				BlSettle.class, new Object[]{paramMap.get("pkSettle")});
		List<Map<String,Object>> attributes= enoteDao.ExtendedAttributes(stInfo.getPkInsurance());
		for(Map<String,Object> attr:attributes){
			if("0331".equals(attr.get("codeAttr"))){
				if("1".equals(attr.get("valAttr"))){
					List<OtherInfoVo> otherInfos =new ArrayList<OtherInfoVo>();
					OtherInfoVo otherInfoVo=new OtherInfoVo();
					otherInfoVo.setInfoName(stInfo.getAmountDisc().toString());//此处为政策减免显示内容
					otherInfoVo.setInfoValue(stInfo.getAmountDisc().toString());
					otherInfoVo.setInfoNo(0);
					otherInfos.add(otherInfoVo);
					resVo.setOtherInfo(otherInfos);
				}
			}
		}
	}

	/**
	 * zsrm-项目化处理支付类型编码转移
	 * @param resVo
	 * @return
	 */
   public String zsrmPayChannelCodeTran(String resVo){
	   if(StringUtils.isBlank(resVo)){
		   return "99";
	   }
	   switch (resVo){
		   case "3":
			   return "4";
		   case "4":
			   return "04";
		   case "7":
			   return "6";
		   case "8":
			   return "7";
		   case "9":
			   return "03";
		   case "10":
			   return "010";
		   case "11":
			   return "011";
		   case "12":
			   return "012";
		   case "13":
			   return "013";
		   default:
			   return resVo;
	   }
   }

	/**
	 * 预交金电子票据开立对外接口
	 * @param args
	 * @return
	 */
	public Object eBillAdvancePayment(Object[] args) {
		Map<String,Object> paramMap = (Map<String,Object>)args[0];
//		开具电子票
		EnoteResDataVo enoteResDataVo = invoicePayMentVoucher(args);
		if(!"S0000".equals(enoteResDataVo.getResult())){
			throw new BusException("预交金电子票据开立失败:"+enoteResDataVo.getMessage());
		}
//		在bl_deposit中保存电子凭证信息
		User user = (User) args[1];
		Map map = JsonUtil.readValue(enoteResDataVo.getMessage(), Map.class);
		String enCodeBillQRCode = map.get("billQRCode").toString();
//		base64进行解码
		byte[] billQRCode = Base64.decode(enCodeBillQRCode);
		map.put("billQRCode",billQRCode);
		Object createTime = map.get("createTime");
		map.remove("createTime");
		map.put("billCreateTime",createTime);
		map.put("pkEmpEbill",user.getPkEmp());
		map.put("nameEmpEbill",user.getNameEmp());
		map.put("pkDepo",paramMap.get("pkDepo"));
		BlDeposit blDeposit = new BlDeposit();
		mapToBean(map,blDeposit,"yyyyMMddHHmmssSSS");
		blDeposit.setFlagCcEbill("0");
		enoteDao.updateBlDeposit(blDeposit);
//		DataBaseHelper.update(DataBaseHelper.getUpdateSql(BlDeposit.class),blDeposit);
		return blDeposit;
	}

	/**
	 * 预交金退款电子凭证开具接口
	 * @param args
	 * @return
	 */
	public Object eBillAdvanceOffPayment(Object[] args) {
		Map<String,Object> paramMap = (Map<String,Object>)args[0];
//		开具退款电子票据
		EnoteResDataVo enoteResDataVo = writeOffPayMentVoucher(args);
		if(!"S0000".equals(enoteResDataVo.getResult())){
			throw new BusException("预交金电子退款凭证开立失败:"+enoteResDataVo.getMessage());
		}
		//		在bl_deposit中保存电子凭证信息
		User user = (User) args[1];
		Map map = JsonUtil.readValue(enoteResDataVo.getMessage(), Map.class);
		String enCodeBillQRCode = map.get("billQRCode").toString();
//		base64进行解码
		byte[] billQRCode = Base64.decode(enCodeBillQRCode);
		map.put("billQRCode",billQRCode);
		Object createTime = map.get("createTime");
		map.remove("createTime");
		map.put("billCreateTime",createTime);
		map.put("pkEmpEbill",user.getPkEmp());
		map.put("nameEmpEbill",user.getNameEmp());
		map.put("pkDepo",paramMap.get("pkDepo"));
		map.put("billBatchCode",map.get("eScarletBillBatchCode"));
		map.put("billNo",map.get("eScarletBillNo"));
		map.put("random",map.get("eScarletRandom"));
		BlDeposit blDeposit = new BlDeposit();
		mapToBean(map,blDeposit,"yyyyMMddHHmmssSSS");
		blDeposit.setFlagCcEbill("0");
		enoteDao.updateBlDeposit(blDeposit);
		return blDeposit;
	}
	/**
	 * 住院预交金开立
	 * @param args
	 * @return
	 */
	private EnoteResDataVo invoicePayMentVoucher(Object[] args) {
		/**
		 * 入参
		 * pkDepo:预交金缴款主键
		 * pkPv:就诊主键
		 */
		Map<String, Object> paramMap = (Map<String, Object>) args[0];
//		查询预交金信息
		List<Map<String, Object>> list = enoteDao.qryENoteAdvancePayInfo(paramMap);
		if(list.isEmpty()){
			throw new BusException("无法查询到预交金交款记录");
		}
		ArrayList<PayChannelDetailVo> channelList = new ArrayList<>();
		Map<String, Object> map = list.get(0);
		EnoteDepoReqVo enoteDepoReqVo = new EnoteDepoReqVo();
		this.mapToBean(map, enoteDepoReqVo, "yyyy-MM-dd HH:mm:ss");
		enoteDepoReqVo.setBusType("07");
//			处理支付渠道信息
		for (Map<String, Object> temp : list) {
			PayChannelDetailVo detailVo = new PayChannelDetailVo();
			detailVo.setPayChannelCode((String) temp.get("payChannelCode"));
			String payChannelValue = "0.00";
			if(temp.get("payChannelValue")!=null) {
				payChannelValue = temp.get("payChannelValue").toString();
			}
			detailVo.setPayChannelValue(new BigDecimal(payChannelValue).setScale(2).doubleValue());
			channelList.add(detailVo);
		}
		enoteDepoReqVo.setPayChannelDetail(channelList);
//			计算预交金余额
		Double amt = enoteDao.qryDepoAmtByPkPv(paramMap);
		enoteDepoReqVo.setOwnAcBalance(amt-enoteDepoReqVo.getAmt());
		User user = (User) args[1];
		enoteDepoReqVo.setPlaceCode(user.getCodeEmp());
//		测试时placeCode=6915
//		enoteDepoReqVo.setPlaceCode("6915");
//			序列化入参
		String jsonString = JSON.toJSONString(enoteDepoReqVo, SerializerFeature.WriteMapNullValue, SerializerFeature.WriteNullStringAsEmpty, SerializerFeature.WriteNullNumberAsZero);
		return enoteRts(jsonString, "1.0", "invoicePayMentVoucher");
	}

	/**
	 * 住院预交金退款电子凭证开具接口
	 */
	private EnoteResDataVo writeOffPayMentVoucher(Object[] args){
		Map<String,Object> paramMap =(Map<String,Object>)args[0];
		User user = (User) args[1];
//		查询预交金信息
		Map<String,Object> map = enoteDao.qryDepoInfo(paramMap);
		BigDecimal temp = (BigDecimal) map.get("amt");
		double amt = Math.abs(temp.doubleValue());
		HashMap<String, String> reqMap = new HashMap<>();
		reqMap.put("busType","07");
		reqMap.put("billBatchCode",(String) map.get("billBatchCode"));
		reqMap.put("billNo",(String) map.get("billNo"));
		reqMap.put("reason",(String) map.get("reason"));
		reqMap.put("operator",user.getNameEmp());
		reqMap.put("busDateTime",map.get("busDateTime")+"000");
//		测试时,开票点先写死
//		reqMap.put("placeCode",user.getCodeEmp());
		reqMap.put("placeCode","6915");
		reqMap.put("voucherBatchCode",(String) map.get("voucherBatchCode"));
		reqMap.put("voucherNo",(String) map.get("voucherNo"));
		reqMap.put("amt",String.valueOf(amt));
		Double sum = enoteDao.qryDepoAmtByPkPv(paramMap);
		reqMap.put("ownAcBalance",String.valueOf(sum+amt));
		reqMap.put("remark","");
		//将Data内容转换为json格式再base64编码,编码字符集UTF-8
		String dataJson = JsonUtil.writeValueAsString(reqMap);
		//发送请求
		EnoteResDataVo dataVo = enoteRts(dataJson,"1.0","writeOffPayMentVoucher");
		return dataVo;
	}
}
