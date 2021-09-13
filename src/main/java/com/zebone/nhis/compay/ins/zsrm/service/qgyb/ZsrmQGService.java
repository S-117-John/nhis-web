package com.zebone.nhis.compay.ins.zsrm.service.qgyb;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.esotericsoftware.minlog.Log;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import com.zebone.nhis.common.module.base.bd.price.BdHp;
import com.zebone.nhis.common.module.bl.opcg.BlOpDt;
import com.zebone.nhis.common.module.compay.ins.qgyb.*;
import com.zebone.nhis.common.module.mybatis.MyBatisPage;
import com.zebone.nhis.common.module.pi.PiMaster;
import com.zebone.nhis.common.module.pv.PvEncounter;
import com.zebone.nhis.common.support.*;
import com.zebone.nhis.compay.ins.zsrm.dao.ZsrmQGMapper;
import com.zebone.nhis.compay.ins.zsrm.vo.*;
import com.zebone.nhis.ma.pub.platform.pskq.utils.MapUtils;
import com.zebone.platform.common.support.NHISUUID;
import com.zebone.platform.common.support.User;
import com.zebone.platform.framework.support.IUser;
import com.zebone.platform.modules.dao.DataBaseHelper;
import com.zebone.platform.modules.dao.support.Page;
import com.zebone.platform.modules.exception.BusException;
import com.zebone.platform.modules.utils.JsonUtil;
import net.sf.json.JSONObject;
import org.apache.commons.lang.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.entity.ContentType;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.type.TypeReference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.*;
import java.math.BigDecimal;
import java.nio.charset.Charset;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

@Service
public class ZsrmQGService {
	private Logger logger = LoggerFactory.getLogger("nhis.ZsrmQGLog");
	@Value("#{applicationProperties['qgyb.url']}")
	private String HSA_URL;
	@Value("#{applicationProperties['qgyb.accountCode']}")
	private String HSA_ACCOUNT_CODE;
	@Value("#{applicationProperties['qgyb.paasid']}")
	private String HSA_PAASID;
	@Value("#{applicationProperties['qgyb.secretKey']}")
	private String SECRETKEY;
	@Value("#{applicationProperties['qgyb.fixmedins_name']}")
	private String FIXMEDINS_NAME;
	@Value("#{applicationProperties['qgyb.fixmedins_code']}")
	private String FIXMEDINS_CODE;
	@Value("#{applicationProperties['qgyb.version']}")
	private String VERSION;
	@Value("#{applicationProperties['qgyb.uploadFile.path']}")
	private String UploadFilePath;

	@Resource
	private ZsrmQGMapper zsrmQGMapper;

	// 用来控制手动事物
	@Resource(name = "transactionManager")
	private PlatformTransactionManager platformTransactionManager;

	/**
	 * 获取患者基本信息
	 */
	public InsPiInfoVo getPersonInfo(String params, IUser user) {
		Map<String, Object> paramMap = JsonUtil.readValue(params, Map.class);
		paramMap = ZsrmQGUtils.humpToMap(paramMap);
		Map<String, Object> retMap = new HashMap<>();
		String ret = "";
		paramMap.put("port", "1101");
		String pkPi = CommonUtils.getPropValueStr(paramMap, "pk_pi");
		String pkPv = CommonUtils.getPropValueStr(paramMap, "pk_pv");
		PiMaster pi = null;
		if (CommonUtils.isNotNull(pkPi)) {
			pi = DataBaseHelper.queryForBean("select * from pi_master where pk_pi=?", PiMaster.class,
					pkPi);
		}
 
		// 证件号处理；=15位转为18位，<15位 港澳台
		if (paramMap.containsKey("mdtrt_cert_no") && paramMap.get("mdtrt_cert_no")!=null) {
			String cernNo = CommonUtils.getString(paramMap.get("mdtrt_cert_no"));
			if (cernNo.length() == 15) {
				// 15位身份证号转为18位
				paramMap.put("mdtrt_cert_no", ZsrmQGUtils.getEighteenIDCard(cernNo));
			} else {
				if (pi != null && !"01".equals(pi.getDtIdtype())) {
					//因医保数据存在垃圾数据(1,2)，暂时过滤证件号长度小于3位的
					if(pi.getIdNo().length()<=3){
						throw new BusException("功能号1101，无效的证件号码: "+pi.getIdNo());
					}
			
					// 港澳台；外籍护照
					if ("02".equals(pi.getDtIdtype()) || "04".equals(pi.getDtIdtype()) || "05".equals(pi.getDtIdtype())) {
						paramMap.put("mdtrt_cert_type", "99");
						paramMap.put("certno", pi.getIdNo());
					} else {
						paramMap.put("mdtrt_cert_type", "03");
						// throw new BusException("该证件类型暂不支持： "+pi.getDtIdtype());
					}
				}
			}
		}
		ret = getHealthInsuranceEntry(paramMap, user);

		retMap = JsonUtil.readValue(ret, Map.class);
		if ("0".equals(CommonUtils.getString(retMap.get("infcode")))) {
			retMap = (Map<String, Object>) retMap.get("output");
			retMap=ZsrmQGUtils.underlineToCamel(retMap);
		
			InsPiInfoVo inspi = JsonUtil.readValue(ApplicationUtils.objectToJson(retMap), InsPiInfoVo.class);

			//paramMap=ZsrmQGUtils.underlineToCamel(paramMap);
			if (CommonUtils.isNotNull(pkPv)) {

				String sql = "select * from ins_qgyb_master where psn_cert_type='01' and certno=? ";
				InsQgybMaster insMaster = DataBaseHelper.queryForBean(sql, InsQgybMaster.class,
						inspi.getBaseinfo().getCertno());
				if (insMaster != null) {
					DataBaseHelper.deleteBeanByPk(insMaster);
					DataBaseHelper.execute("delete ins_qgyb_insutype where pk_insupi=?", insMaster.getPkInsupi());
					DataBaseHelper.execute("delete ins_qgyb_idet where pk_insupi=?", insMaster.getPkInsupi());
				}
				insMaster = inspi.getBaseinfo();
				ApplicationUtils.setDefaultValue(insMaster, true);
				insMaster.setPkPi(pkPi);
				String insu390 = "";
				String insu310 = "";
				List<InsQgybInsutype> insutypeList = new ArrayList<InsQgybInsutype>();
				for (InsQgybInsutype insu : inspi.getInsuinfo()) {
					//只保存险种正常的信息
					if(EnumerateParameter.ONE.equals(insu.getPsnInsuStas())) {
						if("390".equals(insu.getInsutype())){
							insu390 = insu.getInsutype();
						}
						if("310".equals(insu.getInsutype())){
							insu310 = insu.getInsutype();
						}
						insu.setPkInsupi(insMaster.getPkInsupi());
						ApplicationUtils.setDefaultValue(insu, true);
						insutypeList.add(insu);
					}
				}
				inspi.setInsuinfo(insutypeList);
				for (InsQgybIdet idet : inspi.getIdetinfo()) {
					idet.setPkInsupi(insMaster.getPkInsupi());
					ApplicationUtils.setDefaultValue(idet, true);
				}
				
				DataBaseHelper.insertBean(insMaster);
				if(insutypeList != null && insutypeList.size() > 0) {
					DataBaseHelper.batchUpdate(DataBaseHelper.getInsertSql(InsQgybInsutype.class), insutypeList);
				}	
				DataBaseHelper.batchUpdate(DataBaseHelper.getInsertSql(InsQgybIdet.class), inspi.getIdetinfo());

				//险种获取方式：1、保存患者所有险种  2、先取310-职工，没有。取390-居民
				if(CommonUtils.isNotNull(insu390) || CommonUtils.isNotNull(insu310)) {
					paramMap.put("insutype", CommonUtils.isNotNull(insu310) ? insu310 : insu390);
					paramMap.put("pkPv", pkPv);
					zsrmQGMapper.updatePv(paramMap);
				}
			}
			return inspi;
		
		}
		//部分特殊患者处理方式
		else if("99".equals(paramMap.get("mdtrt_cert_type")) && CommonUtils.getString(retMap.get("err_msg")).contains("该参保人的参保信息为空")){
			paramMap.put("psn_cert_type","04");
			paramMap.put("port", "1101");
			ret = getHealthInsuranceEntry(paramMap, user);

			retMap = JsonUtil.readValue(ret, Map.class);
			if ("0".equals(CommonUtils.getString(retMap.get("infcode")))) {
				retMap = (Map<String, Object>) retMap.get("output");
				retMap=ZsrmQGUtils.underlineToCamel(retMap);
			
				InsPiInfoVo inspi = JsonUtil.readValue(ApplicationUtils.objectToJson(retMap), InsPiInfoVo.class);
				return inspi;
			}else {
				throw new BusException("功能号1101，医保获取人员信息失败  " + CommonUtils.getString(retMap.get("err_msg")));
			}
		}
		else{
			throw new BusException("功能号1101，医保获取人员信息失败  " + CommonUtils.getString(retMap.get("err_msg")));
		}
		
	}
	/**
	 * 待遇检查
	 */
	public String insCheck(String params, IUser user){
		Map<String, Object> paramMap = JsonUtil.readValue(params, Map.class);
		paramMap = ZsrmQGUtils.humpToMap(paramMap);
		String psnNo = CommonUtils.getPropValueStr(paramMap, "psn_no");
		//获取人员信息
		if (CommonUtils.isNull(psnNo)) {
			InsPiInfoVo insPi = getPersonInfo(params,user);
			paramMap.put("psn_no", insPi.getBaseinfo().getPsnNo());
		}
		
		//待遇检查
		paramMap.put("fixmedins_code",FIXMEDINS_CODE);
		Map<String, Object> retMap = new HashMap<>();
		paramMap.put("port", "2001");
		String ret = getHealthInsuranceEntry(paramMap, user);
		retMap = JsonUtil.readValue(ret, Map.class);
		Object object = retMap.get("infcode");
		if ("0".equals(CommonUtils.getString(object))) {
			retMap = (Map<String, Object>) retMap.get("output");
			List<InsQgybTrtinfo> trtinfo = JsonUtil.readValue(JsonUtil.writeValueAsString(retMap.get("trtinfo")),new TypeReference<List<InsQgybTrtinfo>>() {
			});
			for(InsQgybTrtinfo trt : trtinfo){
				if("510100".equals(trt.getFund_pay_type())){
					if("1".equals(trt.getTrt_enjymnt_flag())){
						throw new BusException("生育基金对比成功");
					}else{
						throw new BusException("生育基金对比失败");
					}
				}
			}
			throw new BusException("未查询到生育基金信息");
		}else{
			throw new BusException("功能号2001，待遇检查调用失败 " + CommonUtils.getString(retMap.get("err_msg")));
		}

	}

	/**
	 * 门诊挂号
	 */
	public Map<String, Object> OutPatientRegister(String params, IUser user) {
		Map<String, Object> paramMap = JsonUtil.readValue(params, Map.class);
		Map<String, Object> retMap = new HashMap<>();
		
		paramMap.putAll(zsrmQGMapper.qryInsRegPre(paramMap.get("pkPv").toString()));
		paramMap.put("port", "2201");
		Map<String, Object> ret = ZsrmQGUtils.humpToMap(paramMap);
		
		String strRet = getHealthInsuranceEntry(ret, user);

		retMap = JsonUtil.readValue(strRet, Map.class);
		if ("0".equals(CommonUtils.getString(retMap.get("infcode")))) {
			retMap = (Map<String, Object>) retMap.get("output");
			if (retMap.get("data") != null) {
				retMap = (Map<String, Object>) retMap.get("data");
				return retMap;
			}
		}else{
			throw new BusException("功能号2201,医保登记失败  " + CommonUtils.getString(retMap.get("err_msg")));
		}

		return retMap;
	}

	/**
	 * @param params
	 *            015001013002 门诊收费预结算
	 */
	public Map<String,Object> mzHpHuaJia(String params, IUser user) throws Exception {
		Map<String, Object> paramMap = JsonUtil.readValue(params, Map.class);

		// 校验入参
		String errMsg = validaParam(paramMap);
		if (CommonUtils.isNotNull(errMsg)) {
			throw new BusException(errMsg);
		}

		// 公共参数
		Map<String, Object> reqMap = new HashMap<>();
		Map<String, Object> retMap = new HashMap<>();
		String retStr = "";
		String pkPv = paramMap.get("pkPv").toString();
		User userInfo = (User) user;
		
		//签到
		signOper(paramMap,userInfo);

		//获取挂号登记信息
		InsQgybVisit visit = getPvVist(paramMap, user);
		if (visit == null || CommonUtils.isNull(visit.getMedType())) {
			throw new BusException("获取医保登记信息信息失败");
		}
		paramMap.put("YBRegInfo", visit);

		InsQgybVisit insMedtype = qryVisitInfo(pkPv);
		if (insMedtype == null || insMedtype.getMedType() == null) {
			throw new BusException("未获取本次就诊的到医疗类别");
		}
		
		
		// 上传明细信息
		String chrgBchno = ApplicationUtils.getCode("0608");// 收费批次号
		String strSn=chrgBchno;//取批次号后四位
		if (strSn != null && strSn.length() >= 4) {
			strSn =strSn.substring(strSn.length() - 4,strSn.length());
		}
		
							
		paramMap.put("chrgBchno", chrgBchno);
		paramMap.put("strSn", strSn);
		paramMap.put("insutype", visit.getInsutype());
		List<Map<String, Object>> dts = zsrmQGMapper.qryChargeDetailNoUpload(paramMap);
		
		//待上传明细为空，直接反回，可能只有特需费用
		if(dts==null || dts.size()==0){
			InsQgybPreSt preSt = new InsQgybPreSt();
			Double dtAmt=0d;
			
			//检索非特需费用
			List<BlOpDt> dtRet=zsrmQGMapper.qryChargeDetailNoUploadSpec(paramMap);
			for(BlOpDt dt :dtRet){
				dtAmt=dtAmt+dt.getAmount();
			}
			
			preSt.setAggregateAmount(dtAmt);
			preSt.setMedicarePayments(0d);
			preSt.setPatientsPay(dtAmt);
			return (Map<String, Object>) ApplicationUtils.beanToMap(preSt);
		}

		// 查询出待上传明细时校验是否已经有对照信息，没有对照信息时提示用户无法结算
		if (dts != null && dts.size() > 0) {
			StringBuffer errorMsg = new StringBuffer("");
			dts.stream()
					.filter(m -> CommonUtils.isEmptyString(CommonUtils.getPropValueStr(m, "medListCodg"))
							|| CommonUtils.isEmptyString(CommonUtils.getPropValueStr(m, "medinsListCodg")))
					.forEach(map -> {
						errorMsg.append(CommonUtils.getPropValueStr(map, "nameCg"));
						errorMsg.append(",");
						errorMsg.append("\r\n");
					});
			// 有未对照的信息给出提示
			if (errorMsg != null && !CommonUtils.isEmptyString(errorMsg.toString())) {
				throw new BusException("HIS系统提示，以下收费项目未对照：\r\n" + errorMsg.toString());
			}
		}		
		dts = ZsrmQGUtils.humpToLineListMap(dts);
		retMap.clear();
		reqMap.put("pkPv", pkPv);
		reqMap.put("insutype", visit.getInsutype());
		//撤销费用上传
		mzBillDel(JsonUtil.writeValueAsString(reqMap), user);
		
		//上传本次费用
		reqMap.put("port", "2204");
		reqMap.put("params", dts);
		reqMap.put("pk_pv", pkPv);
		//Map<String,Object> uplod = ZsrmQGUtils.humpToMap(reqMap);
		Map<String,Object> uplod = reqMap;

		retStr = getHealthInsuranceEntry(uplod, user);
		retMap = JsonUtil.readValue(retStr, Map.class);
		if (!"0".equals(CommonUtils.getString(retMap.get("infcode")))) {
			throw new BusException("功能号2204，上传费用明细失败，失败原因：" + CommonUtils.getString(retMap.get("err_msg")));
		}
		
		retMap = (Map<String, Object>) retMap.get("output");
		retStr = JsonUtil.writeValueAsString(retMap.get("result"));
		List<InsQgybCg> cgs = JsonUtil.readValue(retStr, new TypeReference<List<InsQgybCg>>() {
		});
		BigDecimal medfeeSumamt = BigDecimal.ZERO;
		
		for (InsQgybCg insQgybCg : cgs) {
			medfeeSumamt = medfeeSumamt.add(new BigDecimal(insQgybCg.getDetItemFeeSumamt()));
			ApplicationUtils.setDefaultValue(insQgybCg, true);
			insQgybCg.setPkPv(pkPv);
			for(Map<String,Object> dt :dts){
				if(insQgybCg.getFeedetlSn().equals(dt.get("feedetl_sn"))){
					insQgybCg.setPkCgop(dt.get("pk_cgop").toString());
					break;
				}
			}
		}
		// 关闭事务自动提交
		DefaultTransactionDefinition def = new DefaultTransactionDefinition();
		def.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRES_NEW);
		TransactionStatus status = platformTransactionManager.getTransaction(def);

		try {
			DataBaseHelper.batchUpdate(DataBaseHelper.getInsertSql(InsQgybCg.class), cgs);

			platformTransactionManager.commit(status); // 提交事务
		} catch (Exception e) {
			platformTransactionManager.rollback(status); // 添加失败 回滚事务；
			e.printStackTrace();
			throw new BusException("上传费用信息成功，保存失败：" + e);
		}

		// 调用预结算
		retMap.clear();
		reqMap.clear();
		retStr = "";
		reqMap.put("port", "2206");
		reqMap.put("psn_no", visit.getPsnNo());
		reqMap.put("mdtrt_cert_type", visit.getMdtrtCertType());
		reqMap.put("mdtrt_cert_no", visit.getMdtrtCertNo());
		reqMap.put("med_type", visit.getMedType());
		reqMap.put("medfee_sumamt", medfeeSumamt);
		reqMap.put("psn_setlway", "01");
		reqMap.put("mdtrt_id", visit.getMdtrtId());
		reqMap.put("chrg_bchno", chrgBchno);
		reqMap.put("acct_used_flag", "0");
		reqMap.put("insutype", visit.getInsutype());
		reqMap.put("pk_pv", pkPv);
		retStr = getHealthInsuranceEntry(reqMap, user);

		retMap = JsonUtil.readValue(retStr, Map.class);
		if ("0".equals(CommonUtils.getString(retMap.get("infcode")))) {

			Map<String, Object> zretMap = (Map<String, Object>) ((Map<String, Object>) retMap.get("output")).get("setlinfo");

			InsQgybPreSt preSt = new InsQgybPreSt();
			OutParamHuaJia outParamHuaJia = JsonUtil.readValue(JsonUtil.writeValueAsString(zretMap),
					OutParamHuaJia.class);
			InsQgybSt insQgybSt = new InsQgybSt();
			ApplicationUtils.copyProperties(insQgybSt, outParamHuaJia);
			insQgybSt.setPkPv(pkPv);
			insQgybSt.setPkPi(visit.getPkPi());
			insQgybSt.setPkHp(visit.getPkHp());
			insQgybSt.setPkVisit(visit.getPkVisit());

			outParamHuaJia.setAmount(outParamHuaJia.getMedfeeSumamt());// 总金额
			//outParamHuaJia.setAmtGrzhzf(outParamHuaJia.getAcctPay());// 个人账户
			outParamHuaJia.setAmtGrzf(outParamHuaJia.getPsnCashPay());// 现金
			outParamHuaJia.setAmtJjzf(outParamHuaJia.getFundPaySumamt());// 基金

			preSt.setYbPreSettleInfo(outParamHuaJia);
			preSt.setYbPreSettleParam(reqMap);
			return (Map<String, Object>) ApplicationUtils.beanToMap(preSt);

		} else {
			logger.error(retStr);
			// 预结算失败，撤销明细上传
			reqMap.clear();
			reqMap.put("pkPv", pkPv);
			reqMap.put("insutype", visit.getInsutype());
			mzBillDel(JsonUtil.writeValueAsString(reqMap), user);
			throw new BusException("功能号2206，医保预结算失败：" + CommonUtils.getString(retMap.get("err_msg")));
		}
	}
	// 签到 qgybSignIn+codeEmp
	private void signOper(Map<String, Object> paramMap, User userInfo) {	

		if ("true".equals(ApplicationUtils.getPropertyValue("qgyb.signInFlag", ""))) {

			Object obj = RedisUtils.getCacheObj("qgybSignIn" + userInfo.getCodeEmp());
			if (obj == null) {
				Map<String, Object> reqMap = new HashMap();
				reqMap.put("opter_no", paramMap.get("opterNo"));
				reqMap.put("mac", paramMap.get("mac"));
				reqMap.put("ip", paramMap.get("ip"));
				reqMap.put("port", "9001");
				String retStr = getHealthInsuranceEntry(reqMap, userInfo);
				Map<String, Object> retMap = JsonUtil.readValue(retStr, Map.class);
				if ("0".equals(CommonUtils.getString(retMap.get("infcode")))) {
					if (CommonUtils.isNotNull(retMap.get("output"))) {
						retMap = (Map<String, Object>) retMap.get("output");
					}
					reqMap.putAll(retMap);
					RedisUtils.setCacheObj("qgybSignIn" + userInfo.getCodeEmp(), reqMap, 60*60*24);
				}
			}
		}
	}


	private InsQgybVisit qryVisitInfo(String pkPv){
		InsQgybVisit insMedtype = DataBaseHelper.queryForBean(
				"select * from (select * from ins_qgyb_visit where pk_pv=? order by create_time desc) where ROWNUM=1", InsQgybVisit.class,
				pkPv);

		return insMedtype;
	}
	private InsQgybVisit qryVisitInfo(String pkPv,String insutype){
		InsQgybVisit insMedtype = DataBaseHelper.queryForBean(
				"select * from (select * from ins_qgyb_visit where pk_pv=? and insutype=?  order by create_time desc) where ROWNUM=1", InsQgybVisit.class,
				pkPv,insutype);

		return insMedtype;
	}
	private InsQgybSt qryInsSt(String pkPv){
		InsQgybSt insSt = DataBaseHelper.queryForBean(
				"select * from (select * from ins_qgyb_st where pk_pv=?  order by create_time desc) where ROWNUM=1", InsQgybSt.class,
				pkPv);
	
		return insSt;
	}
	
	/**
	 * 校验医疗类别是否与上次结算一致
	 */
	private void validaMedType(String pkPv) {

		InsQgybPv insQgybPv = DataBaseHelper.queryForBean("select * from ins_qgyb_pv where pk_pv=? and del_flag='0' ",
				InsQgybPv.class, pkPv);
		if (insQgybPv == null) {
			throw new BusException("校验医疗类别时未获取到医保登记信息 ");
		}
		
		String errMsg = null;
		InsQgybSt insStLast = qryInsSt(insQgybPv.getPkPv());
		if (insStLast != null) {
			// 如果为空，默认为普通门诊
			if (insQgybPv.getMedType() == null && "11".equals(insStLast.getMedType())) {
				return;
			} else if (insQgybPv.getMedType().equals(insStLast.getMedType())) {
				return;		
			}

			errMsg = "上次结算使用医疗类别（" + getQgybDict("med_type", insStLast.getMedType()).getName() + "）与本次（"
					+ getQgybDict("med_type", insQgybPv.getMedType()).getName() + "）不一致，请核对本次就诊信息";
		}
		
		if(errMsg!=null){
			throw new BusException(errMsg);
		}
	}

	/**
	 * @param codeType 字典类型
	 * @param code 编码
	 * @return
	 */
	private InsQgybDict getQgybDict(String codeType,String code){
		InsQgybDict dict=DataBaseHelper.queryForBean("select * from (select * from ins_qgyb_dict where code_type= ? and code= ? and del_flag='0' ) where ROWNUM=1",
				InsQgybDict.class, codeType,code);
		
		return dict;
	}
				
	/**
	 * @param params
	 *            015001013003 撤销上传
	 */
	public void mzBillDel(String params, IUser user) {
		Map<String, Object> paramMap = JsonUtil.readValue(params, Map.class);

		InsQgybVisit visit=null;
		if(paramMap.get("insutype")!=null){
			visit = qryVisitInfo(CommonUtils.getString(paramMap.get("pkPv")),CommonUtils.getString(paramMap.get("insutype")));
		}else{
			visit = qryVisitInfo(CommonUtils.getString(paramMap.get("pkPv")));
		}
		 
		if (visit != null) {
			paramMap.clear();
			paramMap.put("port", "2205");
			paramMap.put("mdtrt_id", visit.getMdtrtId());
			paramMap.put("psn_no", visit.getPsnNo());
			paramMap.put("chrg_bchno", "0000");
			String retStr = getHealthInsuranceEntry(paramMap, user);
			paramMap = JsonUtil.readValue(retStr, Map.class);
			if (!"0".equals(CommonUtils.getString(paramMap.get("infcode")))) {
				if(CommonUtils.getString(paramMap.get("err_msg")).contains("未查询到任何有效相关的记录")
						|| CommonUtils.getString(paramMap.get("err_msg")).contains("费用明细List不能为空")){
					return;
				}
				Log.error(CommonUtils.getString(paramMap.get("err_msg")));
				throw new BusException("功能号2205，门诊费用明细信息撤销失败 " + CommonUtils.getString(paramMap.get("err_msg")));
			}
		}
	}

	/**
	 * 校验就诊主键、费用明细主键
	 */
	private String validaParam(Map<String, Object> paramMap) {
		String errMsg = "";

		if (paramMap == null) {
			errMsg = "未获取到参数信息";
			return errMsg;
		}

		if (CommonUtils.isNull(paramMap.get("pkPv"))) {
			errMsg = "未传入就诊信息主键pkPv";
			return errMsg;
		}

		if (CommonUtils.isNull(paramMap.get("pkCgops"))) {
			errMsg = "未传入费用信息(pkCgops)";
			return errMsg;
		}
		Set<String> pkCgops = new HashSet<String>((List<String>) paramMap.get("pkCgops"));

		int cnt =  DataBaseHelper.queryForScalar("select count(1) from bl_op_dt where flag_settle='1' and pk_cgop in (" 
					+ CommonUtils.convertSetToSqlInPart(pkCgops, "pk_cgop") + ") and pk_pv = ? ", Integer.class,paramMap.get("pkPv"));
		if (cnt > 0) {
			errMsg = "数据已变更，请刷新重试";
			return errMsg;
		}
		return errMsg;
	}

	/**
	 * 获取医保登记信息
	 */
	private InsQgybVisit getPvVist(Map<String, Object> paramMap, IUser user) {
		//Map<String, Object> paramMap = JsonUtil.readValue(params, Map.class);
		String pkPv = CommonUtils.getPropValueStr(paramMap, "pkPv");
		if (CommonUtils.isNull(pkPv)) {
			throw new BusException("未传入就诊信息主键pkPv");
		}
		String medType = "11";


		//获取险种信息，默认取医保计划编码
		PvEncounter pvencounter = DataBaseHelper.queryForBean("select * from pv_encounter where pk_pv=? and eu_pvtype in (1,2,4) and del_flag='0' ",
				PvEncounter.class, pkPv);
		BdHp hp = DataBaseHelper.queryForBean("select * from bd_hp where pk_hp=? and del_flag='0' ",
				BdHp.class, pvencounter.getPkInsu());
		
		// 查询医保登记信息
		Map<String, Object> piVisit = zsrmQGMapper.qryInsVisit(pkPv,hp.getCode());
		
		//有就诊登记信息时则校验上次挂号的险种类型和当前就诊的险种类型是否一致，不一致时重新调用挂号接口
		boolean flagReg = false;
		if(piVisit!=null && piVisit.size()>0){
	    	if(piVisit.containsKey("insutype")
					&& !hp.getCode().equals(CommonUtils.getPropValueStr(piVisit,"insutype"))){
				flagReg = true;
			}
		}
		
		InsQgybPv insQgybPv = DataBaseHelper.queryForBean("select * from ins_qgyb_pv where pk_pv=? and del_flag='0' ",
				InsQgybPv.class, pkPv);
		if (insQgybPv != null && insQgybPv.getMedType() != null) {
			medType = insQgybPv.getMedType();
		}
		
		if ((piVisit == null || piVisit.size() == 0) || flagReg)// 医保挂号
		{
			// 获取患者基本信息
			Map<String, Object> paraInput = new HashMap<String, Object>();
			paraInput = zsrmQGMapper.qryInsBasePre(pkPv);
			InsPiInfoVo insPi =getPersonInfo(ApplicationUtils.beanToJson(paraInput), user);

			if (insPi != null) {
				/*
				if(!pvencounter.getNamePi().equals(insPi.getBaseinfo().getPsnName())){
					throw new BusException("就诊姓名(" + pvencounter.getNamePi()+") 医保平台返回姓名("+insPi.getBaseinfo().getPsnName()+") 不符！");
				}
				*/
				
				InsRegParam insRegParam = new InsRegParam();
				insRegParam.setPsnNo(insPi.getBaseinfo().getPsnNo());
				insRegParam.setInsutype(hp.getCode());
				insRegParam.setMdtrtCertType("02");
				insRegParam.setMdtrtCertNo(insPi.getBaseinfo().getCertno());
				insRegParam.setPkPv(pkPv);

				// 挂号
				Map<String, Object> retRegMap = OutPatientRegister(ApplicationUtils.beanToJson(insRegParam), user);
				// 保存挂号信息
				InsQgybVisit visit = new InsQgybVisit();
				ApplicationUtils.setDefaultValue(visit, true);
				visit.setPkPv(pkPv);
				visit.setMdtrtId(retRegMap.get("mdtrt_id").toString());
				visit.setPsnNo(retRegMap.get("psn_no").toString());
				visit.setInsutype(insRegParam.getInsutype());
				visit.setMdtrtCertType("02");
				visit.setMdtrtCertNo(insRegParam.getMdtrtCertNo());
				visit.setPkPi(paraInput.get("pkPi").toString());
				visit.setMedType(medType);
				visit.setBegntime(new Date());
				visit.setNamePi(insPi.getBaseinfo().getPsnName());
				visit.setPkHp(pvencounter.getPkInsu());
				
				// 关闭事务自动提交
				DefaultTransactionDefinition def = new DefaultTransactionDefinition();
				def.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRES_NEW);
				TransactionStatus status = platformTransactionManager.getTransaction(def);
				
				try {
					DataBaseHelper.insertBean(visit);
					platformTransactionManager.commit(status); // 提交事务
				} catch (Exception e) {
					platformTransactionManager.rollback(status); // 添加失败 回滚事务；
					e.printStackTrace();
					throw new BusException("保存挂号信息失败，请重试：" + e);
				}
			} else {
				throw new BusException("医保登记失败");
			}
		}
		
		// 先上传就诊信息
		InsQgybVisit visit = qryVisitInfo(pkPv,hp.getCode());
		//校验医疗类别是否与上次结算一致
		//validaMedType(visit.getPkPv());
		
		Map<String, Object> upPv = new HashMap<>();
		List<Map<String, Object>> pvDiags = zsrmQGMapper.qryPvDiag(pkPv);// 查询诊断信息

		// piVisit = zsrmQGMapper.qryInsVisit(pkPv) ;//若本次预结算挂号，此方式无法查出数据，暂且注释
		if (piVisit == null) 
		{
			piVisit = DataBaseHelper.queryForMap("select * from ( "
					+ "select visit.mdtrt_id, visit.psn_no, visit.flag_insu, to_char(visit.begntime, 'yyyy-MM-dd HH:mm:ss') as begntime "
					+ ",case when inspv.med_type is null then '11' else inspv.med_type end as med_type"
					+ ",inspv.main_cond_dscr,inspv.dise_codg,inspv.dise_name,inspv.birctrl_type,"
					+ "to_char(inspv.birctrl_matn_date, 'yyyy-MM-dd') as birctrl_matn_date "
					+ " from ins_qgyb_visit visit "
					+ " left join ins_qgyb_pv inspv on inspv.PK_PV=visit.PK_PV and inspv.del_flag='0'"
					+ " where visit.pk_pv=? and visit.insutype=? order by visit.create_time desc " + " ) where ROWNUM=1", pkPv,hp.getCode());
		}

		pvDiags = ZsrmQGUtils.humpToLineListMap(pvDiags);
		String mdtrtinfo = ZsrmQGUtils.humpToLine(piVisit);
		String diseinfo = ZsrmQGUtils.humpToLineListStr(pvDiags);
		Map<String, Object> pvParam = new HashMap<String, Object>();
		pvParam.put("port", "2203A");
		pvParam.put("mdtrtinfo", mdtrtinfo);
		pvParam.put("diseinfo", diseinfo);
		pvParam.put("pk_pv", pkPv);
		upPv.clear();
		String strRet = getHealthInsuranceEntry(pvParam, user);

		upPv = JsonUtil.readValue(strRet, Map.class);
		if (!"0".equals(CommonUtils.getString(upPv.get("infcode")))) {
			throw new BusException("功能号2203A，上传就诊信息失败：" + CommonUtils.getString(upPv.get("err_msg")));
		}
		// 更新上传标志，保存就诊信息
		//visit.setFlagInsu("1");
		//DataBaseHelper.updateBeanByPk(visit);

		return visit;
	}

	/**
	 * @param 015001013004
	 *            门诊缴费
	 */
	public InsQgybSt mzHpJiaokuan(String params, IUser user) {
		Map<String, Object> paramMap = JsonUtil.readValue(params, Map.class);

		// 公共参数
		Map<String, Object> reqMap = new HashMap<>();
		Map<String, Object> retMap = new HashMap<>();
		String reqStr = "";
		String retStr = "";
		String pkPv = paramMap.get("pkPv").toString();		

		reqMap = (Map<String, Object>) paramMap.get("ybPreSettlParam");
		reqMap.put("port", "2207");
		
		retStr = getHealthInsuranceEntry(reqMap, user);

		retMap = JsonUtil.readValue(retStr, Map.class);
		if ("0".equals(CommonUtils.getString(retMap.get("infcode")))) {

			retMap = ((Map<String, Object>) retMap.get("output"));
			Map<String, Object> setlinfo = (Map<String, Object>) retMap.get("setlinfo");
			OutParamHuaJia outParamHuaJia = JsonUtil.readValue(JsonUtil.writeValueAsString(setlinfo),
					OutParamHuaJia.class);

			if (CommonUtils.isNull(outParamHuaJia.getSetlId())) {
				Log.error("医保结算成功，未返回结算id");
			}
			
			//成功后在查询登记信息
			InsQgybVisit visit = qryVisitInfo(pkPv,outParamHuaJia.getInsutype());
			
			// 保存医保结算信息
			InsQgybSt insQgybSt = new InsQgybSt();
			ApplicationUtils.copyProperties(insQgybSt, outParamHuaJia);
			insQgybSt.setPkPv(pkPv);
			insQgybSt.setPkPi(visit.getPkPi());
			insQgybSt.setPkHp(visit.getPkHp());
			insQgybSt.setPkVisit(visit.getPkVisit());
			insQgybSt.setDateSt(new Date());

			insQgybSt.setAmount(outParamHuaJia.getMedfeeSumamt());// 总金额
			// insQgybSt.setAmtGrzhzf(outParamHuaJia.getAcctPay());// 个人账户
			insQgybSt.setAmtGrzf(outParamHuaJia.getPsnCashPay());// 现金
			insQgybSt.setAmtJjzf(outParamHuaJia.getFundPaySumamt());// 基金
			insQgybSt.setYbPksettle(outParamHuaJia.getSetlId());
			insertInsSt(insQgybSt);

			// 保存医保结算基金分项信息
			List<Map<String, Object>> setldetail = (List<Map<String, Object>>) retMap.get("setldetail");
			List<InsQgybStDt> insStDts = JsonUtil.readValue(JsonUtil.writeValueAsString(setldetail),
					new TypeReference<List<InsQgybStDt>>() {
					});
			for (InsQgybStDt insDt : insStDts) {

				ApplicationUtils.setDefaultValue(insDt, true);
				insDt.setPkInsst(insQgybSt.getPkInsst());
			}

			DataBaseHelper.batchUpdate(DataBaseHelper.getInsertSql(InsQgybStDt.class), insStDts);
			
			//保存费用信息 5204
			searchStChargeDtls(JsonUtil.writeValueAsString(insQgybSt),user);
			return insQgybSt;


		}else{
			throw new BusException("功能号2207，医保结算失败 ："+CommonUtils.getString(retMap.get("err_msg")));
		}

		
	}
	
	public void searchStChargeDtls(String params, IUser user){
		 Map<String, Object> paramMap = JsonUtil.readValue(params, Map.class);
	        Map<String, Object> reqMap = new HashMap<String, Object>(16);
	        reqMap.put("mdtrt_id", CommonUtils.getPropValueStr(paramMap,"mdtrt_id"));
	        reqMap.put("psn_no", CommonUtils.getPropValueStr(paramMap,"psn_no"));
	        reqMap.put("setl_id", CommonUtils.getPropValueStr(paramMap,"setl_id"));
			reqMap.put("port", "5204");
			
			String retStr = getHealthInsuranceEntry(reqMap, user);
			retStr=ZsrmQGUtils.underlineToCamel(retStr);
	        //出参保存至ins_qgyb_st_cg
	        List<InsQgybStCg> stCgList = JsonUtil.readValue(
	                JsonUtil.getJsonNode(retStr, "output"),
	                new TypeReference<List<InsQgybStCg>>() {
	                });

	        if(stCgList!=null && stCgList.size()>0){
	            //查询结算信息
	            InsQgybSt stInfo = zsrmQGMapper.qryInsuStInfo(paramMap);

	            stCgList.stream().forEach(stVo->{ 
	                ApplicationUtils.setDefaultValue(stVo,true);
	                stVo.setPkInsst(stInfo.getPkInsst());
	                stVo.setPkPi(stInfo.getPkPi());
	                stVo.setPkPv(stInfo.getPkPv());
	            });

	            //保存先先删除该结算信息关联的费用明细
	            DataBaseHelper.execute("delete from ins_qgyb_st_cg where pk_insst = ?",new Object[]{paramMap.get("pkInsst")});

	            //保存
	            DataBaseHelper.batchUpdate(DataBaseHelper.getInsertSql(InsQgybStCg.class),stCgList);
	        }

	}
	

	/**
	 * 015001013005
	 * @param
	 * 门诊退费
	 */
	public Map<String, Object> mzHpSetttleCancel(String params, IUser user) {
		Map<String, Object> paramMap = JsonUtil.readValue(params, Map.class);

		if (CommonUtils.isNull(paramMap.get("pkPv")))// his结算失败 医保退费
		{
			return insSettleCancle(params, user);
		} else {// 门诊退费
			if (CommonUtils.isNull(paramMap.get("pkSettle"))) {
				throw new BusException("未传入结算主键");
			}


			InsQgybSt insQgybSt = DataBaseHelper.queryForBean("select * from ins_qgyb_st where pk_settle= ? ",
					InsQgybSt.class, paramMap.get("pkSettle"));
			
			InsQgybVisit visit = qryVisitInfo(CommonUtils.getString(paramMap.get("pkPv")),insQgybSt.getInsutype());
			

			Map<String, Object> retMap = new HashMap<String, Object>();
			if (insQgybSt != null) {
				retMap.put("ybPkSettle", insQgybSt.getYbPksettle());
				retMap=insSettleCancle(JsonUtil.writeValueAsString(retMap), user);//全部退费

				Map<String, Object> reqMap = new HashMap<String, Object>();
				paramMap = (Map<String, Object>) paramMap.get("ybPreReturnInfo");
				paramMap=(Map<String, Object>) paramMap.get("yBPreIntoParam");
				//部分退逻辑
				if(paramMap!=null)
				{
					InsQgybStCg insQgybStCg = DataBaseHelper.queryForBean("select chrg_bchno AS chrgBchno from ins_qgyb_st_cg where pk_pv = ? and SETL_ID = ? group by chrg_bchno",
							InsQgybStCg.class, insQgybSt.getPkPv(),insQgybSt.getSetlId());
					if(insQgybStCg.getChrgBchno() != null){
						reqMap.clear();
						reqMap.put("port", "2205");
						reqMap.put("mdtrt_id", visit.getMdtrtId());
						reqMap.put("psn_no", visit.getPsnNo());
						reqMap.put("chrg_bchno", insQgybStCg.getChrgBchno());
						String retStrs = getHealthInsuranceEntry(reqMap, user);
					}
					paramMap.put("port", "2207");
					String retStr="";
					retStr = getHealthInsuranceEntry(paramMap, user);

					retMap = JsonUtil.readValue(retStr, Map.class);
					if ("0".equals(CommonUtils.getString(retMap.get("infcode")))) {

						retMap = (Map<String, Object>) ((Map<String, Object>) retMap.get("output")).get("setlinfo");
						OutParamHuaJia outParamHuaJia = JsonUtil.readValue(JsonUtil.writeValueAsString(retMap),
								OutParamHuaJia.class);

						if (CommonUtils.isNull(outParamHuaJia.getSetlId())) {
							Log.error("医保结算成功，未返回结算id");
						}
						InsQgybSt reInsQgybSt = new InsQgybSt();
						ApplicationUtils.copyProperties(reInsQgybSt, outParamHuaJia);
						reInsQgybSt.setPkPv(visit.getPkPv());
						reInsQgybSt.setPkPi(visit.getPkPi());
						reInsQgybSt.setPkHp(visit.getPkHp());
						reInsQgybSt.setPkVisit(visit.getPkVisit());
						reInsQgybSt.setDateSt(new Date());

						reInsQgybSt.setAmount(outParamHuaJia.getMedfeeSumamt());// 总金额
						//insQgybSt.setAmtGrzhzf(outParamHuaJia.getAcctPay());// 个人账户
						reInsQgybSt.setAmtGrzf(outParamHuaJia.getPsnCashPay());// 现金
						reInsQgybSt.setAmtJjzf(outParamHuaJia.getFundPaySumamt());// 基金
						reInsQgybSt.setYbPksettle(outParamHuaJia.getSetlId());
						insertInsSt(reInsQgybSt);
						
						//保存费用信息 5204
						searchStChargeDtls(JsonUtil.writeValueAsString(reInsQgybSt),user);
						
						return (Map<String, Object>) ApplicationUtils.beanToMap(reInsQgybSt);


					}else{
						throw new BusException("功能号2207，医保结算失败 ："+CommonUtils.getString(retMap.get("err_msg")));
					}
					
				}else{
					//retMap.put("ybPksettle", null);
					return null;
				}
				

				//return retMap;

			} else {
				return null;
				//throw new BusException("结算主键" + paramMap.get("pkSettle") + " 未查询到医保结算记录");
			}
		}
	}

	/**
	 * 015001013006
	 * @param
	 * 将PkSettle结算主键更新到INS_QGYB_ST表
	 */
	public void updatePkSettle(String params, IUser user) {
		Map<String, Object> paramMap = JsonUtil.readValue(params, Map.class);
		String pkPv = paramMap.get("pkPv").toString();

		String pkSettle = null;
		String pkSettleCanc = null;
		String ywlx = null;// 业务类型
		String setlId = null;// 医保结算主键

		// 结算主键
		if (paramMap.containsKey("pkSettle") && paramMap.get("pkSettle") != null) {
			pkSettle = CommonUtils.getString(paramMap.get("pkSettle"));
		}
		// 取消结算主键
		if (paramMap.containsKey("pkSettleCanc") && paramMap.get("pkSettleCanc") != null) {
			pkSettleCanc = CommonUtils.getString(paramMap.get("pkSettleCanc"));
		}
		if (paramMap.containsKey("yWLX") && paramMap.get("yWLX") != null) {
			ywlx = CommonUtils.getString(paramMap.get("yWLX"));
		}
		// 医保结算ID
		if (paramMap.containsKey("pkPtmzjs") && paramMap.get("pkPtmzjs") != null) {
			setlId = CommonUtils.getString(paramMap.get("pkPtmzjs"));// jsId
		}
		if ("1".equals(CommonUtils.getString(ywlx))) {// 门诊结算
			// 收费
			if (CommonUtils.isNotNull(pkSettle) && CommonUtils.isNotNull(setlId)) {
				DataBaseHelper.execute(
						"update ins_qgyb_st set pk_settle =?  where del_flag='0' and PK_PV=? and setl_id = ?", pkSettle,
						pkPv, setlId);
				return;
			}
			// 退费
			if (CommonUtils.isNotNull(pkSettleCanc) && CommonUtils.isNotNull(setlId)) {
				DataBaseHelper.execute(
						"update ins_qgyb_st set pk_settle =?  where del_flag='0' and PK_PV=? and setl_id = ?",
						pkSettleCanc, pkPv, setlId);
				return;
			}
			if (!CommonUtils.isEmptyString(setlId)) {
				DataBaseHelper.execute(
						"update ins_qgyb_st set pk_settle=? where del_flag='0' and PK_PV=? and setl_id = ?", pkSettle,
						pkPv, setlId);
			}
		} else {

		}

	}

	/**
	 * @param 015001013007
	 *            门诊退费预结算
	 */
	public Map<String, Object> PreReturnSettlement(String params, IUser user) {
		Map<String, Object> paramMap = JsonUtil.readValue(params, Map.class);

		// 查询医保结算信息
		InsQgybSt insSt = DataBaseHelper.queryForBean("select * from ins_qgyb_st where pk_settle=? ",
				InsQgybSt.class, paramMap.get("pkSettle"));
		
		// 不退费费用明细
		List<BlOpDt> noCancleDt = noCancleDt(params);
		
		//无医保结算记录，为特需费用结算
		if (insSt == null ) {
			Double dtAmt = 0d;
			
			//部分退，计算重收费用
			if (noCancleDt!=null && noCancleDt.size()>0) {
				Set<String> pkBlOpDt = new HashSet<String>();
				for (BlOpDt blOpDt : noCancleDt) {
					pkBlOpDt.add(blOpDt.getPkCgop());
				}

				Double amtAdd = 0d;//特诊加收金额
				Double amtSpec = 0d;//特需费用
				Map<String, Object> reqMap = paramMap;
				reqMap.put("pkCgops", pkBlOpDt);
				reqMap.put("rePay", "1");
				List<BlOpDt> allDts = zsrmQGMapper.qryChargeDts(reqMap);
				for (BlOpDt dt : allDts) {
					amtAdd= amtAdd + dt.getAmountAdd();
					if("96".equals(dt.getCodeBill())){
						amtSpec =amtSpec + dt.getAmount();	
					}		
				}
				dtAmt=amtAdd+amtSpec;
			}
			InsQgybPreSt preSt = new InsQgybPreSt();
			preSt.setAggregateAmount(dtAmt);
			preSt.setMedicarePayments(0d);
			preSt.setPatientsPay(dtAmt);
			return (Map<String, Object>) ApplicationUtils.beanToMap(preSt);
		}
		// 查询医保登记信息
		InsQgybVisit visit = qryVisitInfo(CommonUtils.getString(paramMap.get("pkPv")),insSt.getInsutype());
		if (visit == null) {
			throw new BusException("医保预退费失败，未查询到对应的医保登记记录");
		}

		// 部分退医保逻辑
		if (noCancleDt != null && noCancleDt.size() > 0) {
			//validaMedType(visit.getPkPv());
			Map<String, Object> rePayIns = rePayIns(visit, noCancleDt, insSt, user, params);
			if (rePayIns != null) {
				return rePayIns;
			} else {
				throw new BusException("医保预退费失败，部分退返回信息错误");
			}
		}
		

		Map<String, Object> stMap = zsrmQGMapper.qryBlSt(paramMap);

		if (stMap != null) {
			//此处返回金额为重收金额，故全退时返回0
			stMap.put("aggregate_amount", 0);
			stMap.put("patients_pay", 0);
			stMap.put("medicare_payments", 0);
			return stMap;
		} else {
			throw new BusException("医保预退费失败，未查询到对应的结算记录");
		}

	}

	/**
	 * @param visit
	 * @param newOpList
	 * @param user
	 * @param preParam
	 * @return
	 */
	private Map<String, Object> rePayIns(InsQgybVisit visit, List<BlOpDt> newOpList,InsQgybSt insSt, IUser user, String preParam) {

		Map<String, Object> reqMap = new HashMap<String, Object>();
		Map<String, Object> retMap = new HashMap<String, Object>();
		String pkPv = visit.getPkPv();
		String reqStr = "";
		String retStr = "";
		Set<String> pkCgs = new HashSet<String>();
		for (BlOpDt blOpDt : newOpList) {
			pkCgs.add(blOpDt.getPkCgop());
		}

		// 上传明细信息
		String chrgBchno = ApplicationUtils.getCode("0608");// 收费批次号
		reqMap.put("chrgBchno", chrgBchno);
		reqMap.put("rePay", "1");
		reqMap.put("pkCgops", pkCgs);
		reqMap.put("pkPv", pkPv);
		reqMap.put("insutype", visit.getInsutype());
		List<Map<String, Object>> dts = zsrmQGMapper.qryChargeDetailNoUpload(reqMap);

		Double amtAdd = 0d;//特诊加收金额
		Double amtSpec = 0d;//特需费用
		//单条明细部分退，金额=单价*重收数量
		for(BlOpDt newOp :newOpList){	
			for(Map<String, Object> dt :dts){
				if(newOp.getPkCgop().equals(dt.get("pkCgop"))){
					dt.put("cnt", newOp.getQuan());
					dt.remove("detItemFeeSumamt");
					dt.put("det_item_fee_sumamt", MathUtils.mul(newOp.getQuan(), CommonUtils.getDoubleObject(dt.get("pric"))));
					if(newOp.getAmountAdd()>0){
						amtAdd= amtAdd + newOp.getQuan()*(newOp.getPrice()-newOp.getPriceOrg());
					}

				}
			}
		 if("96".equals(newOp.getCodeBill())){
				amtSpec =amtSpec + newOp.getAmount();	
			}
		}		

		InsQgybPreSt preSt = new InsQgybPreSt();
		
		preSt.setMedicarePayments(0d);
		preSt.setPatientsPay(amtSpec+amtAdd);
		preSt.setAggregateAmount(preSt.getPatientsPay());
		
		if (dts == null || dts.size() == 0) {
			return (Map<String, Object>) ApplicationUtils.beanToMap(preSt);
		}

		dts = ZsrmQGUtils.humpToLineListMap(dts);
		//撤销费用上传
		retMap.clear();
		retMap.put("pkPv", visit.getPkPv());
		retMap.put("insutype", visit.getInsutype());
		mzBillDel(JsonUtil.writeValueAsString(reqMap), user);
		
		//上传本次就诊明细
		Map<String, Object> uplod = new HashMap<String, Object>();
		reqStr = "";
		uplod.put("port", "2204");
		uplod.put("params", dts);
		uplod.put("chrg_bchno", chrgBchno);
		uplod.put("re_pay", "1");
		uplod.put("pk_cgops", pkCgs);
		uplod.put("pk_pv", pkPv);	
		retStr = getHealthInsuranceEntry(uplod, user);
		retMap = JsonUtil.readValue(retStr, Map.class);
		if (!"0".equals(CommonUtils.getString(retMap.get("infcode")))) {
			Log.error(CommonUtils.getString(retMap.get("err_msg")));
			throw new BusException("上传费用明细失败，失败原因：" + CommonUtils.getString(retMap.get("err_msg")));
		}

		retMap = (Map<String, Object>) retMap.get("output");
		retStr = JsonUtil.writeValueAsString(retMap.get("result"));
		List<InsQgybCg> cgs = JsonUtil.readValue(retStr, new TypeReference<List<InsQgybCg>>() {
		});
		BigDecimal medfeeSumamt = BigDecimal.ZERO;
		for (InsQgybCg insQgybCg : cgs) {
			medfeeSumamt = medfeeSumamt.add(new BigDecimal(insQgybCg.getDetItemFeeSumamt()));
			ApplicationUtils.setDefaultValue(insQgybCg, true);
			insQgybCg.setPkPv(visit.getPkPv());
			for(Map<String,Object> dt :dts){
				if(insQgybCg.getFeedetlSn().equals(dt.get("feedetl_sn"))){
					insQgybCg.setPkCgop(dt.get("pk_cgop").toString());
					break;
				}
			}
		}
		// 关闭事务自动提交
		DefaultTransactionDefinition def = new DefaultTransactionDefinition();
		def.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRES_NEW);
		TransactionStatus status = platformTransactionManager.getTransaction(def);

		try {
			DataBaseHelper.batchUpdate(DataBaseHelper.getInsertSql(InsQgybCg.class), cgs);

			platformTransactionManager.commit(status); // 提交事务
		} catch (Exception e) {
			platformTransactionManager.rollback(status); // 添加失败 回滚事务；
			e.printStackTrace();
			throw new BusException("上传费用信息成功，保存失败：" + e);
		}

		// 调用预结算
		retMap.clear();
		reqMap.clear();
		retStr = "";
		reqMap.put("port", "2206");
		reqMap.put("psn_no", visit.getPsnNo());
		reqMap.put("mdtrt_cert_type", visit.getMdtrtCertType());
		reqMap.put("mdtrt_cert_no", visit.getMdtrtCertNo());
		reqMap.put("med_type", insSt.getMedType());//结算后医生可能修改医疗类别，此处取上次结算的医疗类别
		reqMap.put("medfee_sumamt", medfeeSumamt);
		reqMap.put("psn_setlway", "01");
		reqMap.put("mdtrt_id", visit.getMdtrtId());
		reqMap.put("chrg_bchno", chrgBchno);
		reqMap.put("acct_used_flag", "0");
		reqMap.put("insutype", visit.getInsutype());
		retStr = getHealthInsuranceEntry(reqMap, user);

		retMap = JsonUtil.readValue(retStr, Map.class);
		if ("0".equals(CommonUtils.getString(retMap.get("infcode")))) {

			retMap = (Map<String, Object>) ((Map<String, Object>) retMap.get("output")).get("setlinfo");

			//InsQgybPreSt preSt = new InsQgybPreSt();

			OutParamHuaJia outParamHuaJia = JsonUtil.readValue(JsonUtil.writeValueAsString(retMap),
					OutParamHuaJia.class);
			InsQgybSt insQgybSt = new InsQgybSt();
			ApplicationUtils.copyProperties(insQgybSt, outParamHuaJia);
			insQgybSt.setPkPv(visit.getPkPv());
			insQgybSt.setPkPi(visit.getPkPi());
			insQgybSt.setPkHp(visit.getPkHp());
			insQgybSt.setPkVisit(visit.getPkVisit());

			insQgybSt.setAmount(outParamHuaJia.getMedfeeSumamt());// 总金额
			insQgybSt.setAmtGrzhzf(outParamHuaJia.getAcctPay());// 个人账户
			insQgybSt.setAmtGrzf(outParamHuaJia.getPsnCashPay());// 现金
			insQgybSt.setAmtJjzf(outParamHuaJia.getFundPaySumamt());// 基金

			outParamHuaJia.setAmount(outParamHuaJia.getMedfeeSumamt());// 总金额
			outParamHuaJia.setAmtGrzf(outParamHuaJia.getPsnCashPay());// 现金
			outParamHuaJia.setAmtJjzf(outParamHuaJia.getFundPaySumamt());// 基金

			preSt.setPreIntoParam(preParam); // 退费预结算入参
			preSt.setyBPreIntoParam(reqMap);// 退费预结算 医保入参
			preSt.setYbPreSettleInfo(outParamHuaJia);// 退费预结算 医保入参

			//preSt.setAggregateAmount(preSt.getAggregateAmount()+outParamHuaJia.getMedfeeSumamt());
			preSt.setPatientsPay(preSt.getPatientsPay()+outParamHuaJia.getPsnCashPay());
			preSt.setMedicarePayments(outParamHuaJia.getFundPaySumamt());
			preSt.setAggregateAmount(preSt.getPatientsPay()+preSt.getMedicarePayments());
			return (Map<String, Object>) ApplicationUtils.beanToMap(preSt);

		} else {
			logger.error(retStr);
			// 预结算失败，撤销明细上传
			//reqMap.clear();
			//reqMap.put("pkPv", visit.getPkPv());
			//mzBillDel(JsonUtil.writeValueAsString(reqMap), user);
			throw new BusException("医保预结算失败：" + CommonUtils.getString(retMap.get("err_msg")));
		}

	}
	/**
	 * 医保退费
	 * ybPksettle=setl_id
	 */
	public Map<String, Object> insSettleCancle(String params, IUser user) {
		Map<String, Object> paramMap = JsonUtil.readValue(params, Map.class);

		if (CommonUtils.isNotNull(paramMap.get("ybPkSettle")))// his结算失败 医保退费
		{
			InsQgybSt insQgybSt = DataBaseHelper.queryForBean("select * from ins_qgyb_st where setl_id= ? ",
					InsQgybSt.class, paramMap.get("ybPkSettle"));
			Map<String, Object> retMap = new HashMap<String, Object>();
			if (insQgybSt != null) {
				Map<String, Object> reqMap = new HashMap<String, Object>();
				reqMap.put("port", "2208");
				reqMap.put("mdtrt_id", insQgybSt.getMdtrtId());
				reqMap.put("psn_no", insQgybSt.getPsnNo());
				reqMap.put("setl_id", insQgybSt.getSetlId());
				String retStr = getHealthInsuranceEntry(reqMap, user);

				retMap = JsonUtil.readValue(retStr, Map.class);
				if ("0".equals(CommonUtils.getString(retMap.get("infcode")))) {
					retMap = (Map<String, Object>) ((Map<String, Object>) retMap.get("output")).get("setlinfo");
					InsQgybSt insStCancel = JsonUtil.readValue(JsonUtil.writeValueAsString(retMap), InsQgybSt.class);

					insStCancel.setAmount(insStCancel.getMedfeeSumamt());// 总金额
					//insStCancel.setAmtGrzhzf(insStCancel.getAcctPay());// 个人账户
					insStCancel.setAmtJjzf(insStCancel.getFundPaySumamt());// 基金
					insStCancel.setAmtGrzf(insStCancel.getPsnCashPay());// 现金

					insStCancel.setPkInsstCancel(insQgybSt.getPkInsst());
					insStCancel.setSetlIdCancel(insQgybSt.getSetlId());
					insStCancel.setPkHp(insQgybSt.getPkHp());
					insStCancel.setPkPi(insQgybSt.getPkPi());
					insStCancel.setPkPv(insQgybSt.getPkPv());
					insStCancel.setPkVisit(insQgybSt.getPkVisit());
					insStCancel.setPkHp(insQgybSt.getPkHp());
					insStCancel.setPsnType(insQgybSt.getPsnType());
					insStCancel.setYbPksettle(insStCancel.getSetlId());
					insStCancel.setPsnNo(insQgybSt.getPsnNo());
					insStCancel.setDateSt(new Date());
					ApplicationUtils.setDefaultValue(insStCancel, true);

					//DataBaseHelper.execute("update INS_QGYB_ST set del_flag='1' where pk_pv=? and setl_id =?", insStCancel.getPkPv(),insStCancel.getSetlId());
					insertInsSt(insStCancel);
					
					return (Map<String, Object>) ApplicationUtils.beanToMap(insStCancel);
				}else if(CommonUtils.getString(retMap.get("err_msg")).contains("该笔结算已撤销，请勿重复办理")){
					retMap.clear();
					InsQgybSt insSt = DataBaseHelper.queryForBean(
							"select * from ins_qgyb_st where SETL_ID_CANCEL=? and del_flag='0' ", InsQgybSt.class, insQgybSt.getSetlId());
					retMap.put("ybPksettle", paramMap.get("ybPkSettle"));
					retMap.put("amtJjzf", insSt.getFundPaySumamt());
					return retMap;
				}
				else {
					
					Log.error("功能号2208，医保退费失败" + CommonUtils.getString(retMap.get("err_msg")));
					throw new BusException("功能号2208，医保退费失败" + CommonUtils.getString(retMap.get("err_msg")));
				}
			}else{
				Log.error("医保退费失败,结算ID未获取到结算记录：" + params);
				throw new BusException("医保退费失败，结算ID未获取到结算记录"+paramMap.get("ybPksettle"));
			}
			
		} else {
			Log.error("医保退费失败,参数：" + params);
			throw new BusException("医保退费失败，未获取到结算ID");
		}
		
	}
	private List<BlOpDt> noCancleDt(String params) {
		Map<String, Object> paramMap = JsonUtil.readValue(params, Map.class);

		// 查询本次结算的的所有明细
		List<BlOpDt> allList = DataBaseHelper.queryForList(
				"select * from bl_op_dt where pk_pv=? and pk_settle =? and del_flag='0' ", BlOpDt.class,
				paramMap.get("pkPv"), paramMap.get("pkSettle"));

		JsonNode jsonnode = JsonUtil.getJsonNode(params, "returnPkcgop");
		if (jsonnode == null) {
			return null;
			//throw new BusException("医保预退费失败，未查询到对应的医保结算记录");
		}

		List<BlOpDt> returnPkcgop = JsonUtil.readValue(jsonnode, new TypeReference<List<BlOpDt>>() {});
		List<BlOpDt> newOpList = new ArrayList<BlOpDt>();
		for (BlOpDt opdt : allList) {
			boolean flag = false;
			for (BlOpDt oldDt : returnPkcgop) {
				if (opdt.getPkCgop().equals(oldDt.getPkCgop())) {
					if(opdt.getQuan().equals(oldDt.getQuanBack()))
					{
						flag = true;
						break;
					}

					opdt.setAmountAdd(opdt.getAmountAdd()-oldDt.getQuanBack()*(opdt.getAmountAdd()/opdt.getQuan()));
					
					opdt.setQuan(opdt.getQuan()-oldDt.getQuanBack());//bl_op_dt明细部分退费
					opdt.setAmount(opdt.getAmount()-oldDt.getAmount());
					
					break;
				}
			}
			if (!flag) {

				newOpList.add(opdt);
			}
		}
		return newOpList;
	}
	/**
	 * 插入医保结算表 
	 */
	private void insertInsSt(InsQgybSt insSt) {
		DefaultTransactionDefinition def = new DefaultTransactionDefinition();
		def.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRES_NEW);
		TransactionStatus status = platformTransactionManager.getTransaction(def);
		try {
			DataBaseHelper.execute("update INS_QGYB_ST set del_flag='1' where pk_pv=? and setl_id =?", insSt.getPkPv(),insSt.getSetlId());
			DataBaseHelper.insertBean(insSt);
			platformTransactionManager.commit(status); // 提交事务
		} catch (Exception e) {
			platformTransactionManager.rollback(status); // 添加失败 回滚事务；
			e.printStackTrace();
			throw new BusException("保存医保结算信息失败，请重试：" + e);
		}
	}

	/**
	 *慢特病备案记录保存
	 *015001013030
	 */
	public void saveRecordMt(String params, IUser user){
		Map<String, Object> paramMap = JsonUtil.readValue(params, Map.class);
		Map<String, Object> retMap = new HashMap<>();
		paramMap.put("port", "2503");
		String ret = getHealthInsuranceEntry(paramMap, user);
		retMap = JsonUtil.readValue(ret, Map.class);
		if ("0".equals(CommonUtils.getString(retMap.get("infcode")))) {

			retMap = (Map<String, Object>) ((Map<String, Object>) retMap.get("output")).get("result");
			if (retMap.get("trt_dcla_detl_sn") != null) {
				InsQgybPidise piDise = JsonUtil.readValue(params, InsQgybPidise.class);
				piDise.setTrtDclaDetlSn(retMap.get("trt_dcla_detl_sn").toString());
				ApplicationUtils.setDefaultValue(piDise, true);
				DataBaseHelper.insertBean(piDise);
			} else {
				throw new BusException("功能号2503，慢特病备案未返回待遇申报明细流水号！");
			}

		} else {
			throw new BusException("功能号2503，慢特病备案失败：" + CommonUtils.getString(retMap.get("err_msg")));
		}

	}
	/**
	 *慢特病备案记录取消
	 *015001013031
	 */
	public void cancelRecordMt(String params, IUser user){
		Map<String, Object> paramMap = JsonUtil.readValue(params, Map.class);
		if (paramMap.get("trt_dcla_detl_sn") != null) {
			Map<String, Object> retMap = new HashMap<>();
			paramMap.put("port", "2504");
			String ret = getHealthInsuranceEntry(paramMap, user);
			retMap = JsonUtil.readValue(ret, Map.class);
			if ("0".equals(CommonUtils.getString(retMap.get("infcode")))) {			
				InsQgybPidise piDise=DataBaseHelper.queryForBean("select * from ins_qgyb_pidise where trt_dcla_detl_sn=? and del_flag='0'",
						InsQgybPidise.class, paramMap.get("trt_dcla_detl_sn").toString());
				if(piDise!=null){
					piDise.setFlagCanc("1");
					piDise.setCancelTime(new Date());
					User userInfo = (User) user;
					piDise.setCancelor(userInfo.getUserName());
					DataBaseHelper.updateBeanByPk(piDise);
				}
			} else {
				throw new BusException("功能号2503，慢特病备案失败：" + CommonUtils.getString(retMap.get("err_msg")));
			}
		} else {
			throw new BusException("功能号2503，慢特病备案失败：未获取到 待遇申报明细流水号trt_dcla_detl_sn");
		}
	}
	/**
	 *慢特病备案记录查询
	 *015001013029
	 */
	public Map<String,Object> qryRecordMt(String params, IUser user){
		Map<String, Object> paramMap = JsonUtil.readValue(params, Map.class);
		paramMap = ZsrmQGUtils.humpToMap(paramMap);
		if (paramMap.get("psn_no") != null) {
			paramMap.put("port", "5301");
			String ret = getHealthInsuranceEntry(paramMap, user);

			Map<String, Object> retMap = JsonUtil.readValue(ret, Map.class);
			if ("0".equals(CommonUtils.getString(retMap.get("infcode")))) {
				
				List<InsQgybPidise> piDiseList=DataBaseHelper.queryForList("select * from ins_qgyb_pidise where psn_no=? and del_flag='0'", 
						InsQgybPidise.class,paramMap.get("psn_no").toString());
				List<Map<String, Object>> ins=  (List<Map<String, Object>>) ((Map<String,Object>)retMap.get("output")).get("feedetail");
				Map<String,Object> map=new HashMap<String, Object>(); 
				map.put("insRecordMt", ins);
				map.put("hisRecordMt", piDiseList);
				return map;
			} else {
				throw new BusException("功能号5301，慢特病备案记录查询失败：" + CommonUtils.getString(retMap.get("err_msg")));
			}
		}else{
			throw new BusException("功能号5301，慢特病备案记录查询失败：未获取到人员编号");
		}
	}
	/**
	 *人员定点记录查询
	 *015001013032
	 */
	public List<Map<String,Object>> qryRecordDd(String params, IUser user){
		Map<String, Object> paramMap = JsonUtil.readValue(params, Map.class);

		if (paramMap.get("psn_no") != null) {
			paramMap.put("port", "5302");
			String ret = getHealthInsuranceEntry(paramMap, user);

			Map<String, Object> retMap = JsonUtil.readValue(ret, Map.class);
			if ("0".equals(CommonUtils.getString(retMap.get("infcode")))) {
				return (List<Map<String, Object>>) ((Map<String,Object>)retMap.get("output")).get("psnfixmedin");
			} else {
				throw new BusException("功能号5302，人员定点信息查询失败：" + CommonUtils.getString(retMap.get("err_msg")));
			}
		}else{
			throw new BusException("功能号5302，人员定点信息查询失败：未获取到人员编号");
		}
	}
	
	public String fileUpload(MultipartFile file, IUser user) {
		String rs = "";
		Map<String, String> map = this.getHeaderElement();
		try {
			rs = HttpClientUtil.sendHttpPost(HSA_URL + "9101", file, FIXMEDINS_CODE, map);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		logger.info("出参参数：" + rs);
		return rs;
	}

	@SuppressWarnings("unchecked")
	public String getHealthInsuranceEntry(Map<String, Object> paramMap, IUser user) {
		//Map<String, Object> paramMap = JsonUtil.readValue(params, Map.class);
		String rs = "";
		String port = paramMap.get("port").toString();
		Map<String, String> map = this.getHeaderElement();
		String jsonStr = "";

		if(paramMap.containsKey("pk_pi")){
			paramMap.remove("pk_pi");
		}
		if(paramMap.containsKey("pk_pv")){
			paramMap.remove("pk_pv");
		}
		paramMap.remove("port");
		
		switch (port) {
		/* 获取人员信息 */
		case "1101":
			jsonStr = getJsonStr(paramMap, "data", port, user);
			break;
		/* 获取字典 */
		case "1901":
			jsonStr = getJsonStr(paramMap, "data", port, user);
			break;
		/* 签到 */
		case "9001":
			jsonStr = getJsonStr(paramMap, "signIn", port, user);
			break;
		/* 签退 */
		case "9002":
			jsonStr = getJsonStr(paramMap, "signOut", port, user);
			break;
		/* 门诊挂号 */
		case "2201":
			jsonStr = getJsonStr(paramMap, "data", port, user);
			break;
		/* 门诊挂号撤销 */
		case "2202":
			jsonStr = getJsonStr(paramMap, "data", port, user);
			break;
		/* 门诊就诊信息上传 */
		case "2203A":
			JSONObject jsonObject = JSONObject.fromObject(paramMap);
			jsonStr = getJsonStr(jsonObject, port, user);
			break;
		/* 门诊费用明细信息上传 */
		case "2204":
			jsonStr = getJsonStr(paramMap.get("params"), "feedetail", port, user);
			break;
		/* 门诊费用明细信息撤销 */
		case "2205":
			jsonStr = getJsonStr(paramMap, "data", port, user);
			break;
		/* 门诊预结算 */
		case "2206":
			jsonStr = getJsonStr(paramMap, "data", port, user);
			break;
		/* 门诊结算 */
		case "2207":
			jsonStr = getJsonStr(paramMap, "data", port, user);
			break;
		/* 门诊结算撤销 */
		case "2208":
			jsonStr = getJsonStr(paramMap, "data", port, user);
			break;
		/* 待遇检查 */
		case "2001":
			jsonStr = getJsonStr(paramMap, "data", port, user);
			break;
		/* 人员累计信息查询 */
		case "5206":
			jsonStr = getJsonStr(paramMap, "data", port, user);
			break;
		/* 科室信息上传 */
		case "3401":
			jsonStr = getJsonStr(paramMap, "deptinfo", port, user);
			break;
		/* 科室信息变更 */
		case "3402":
			jsonStr = getJsonStr(paramMap, "deptinfo", port, user);
			break;
		/* 科室信息撤销 */
		case "3403":
			jsonStr = getJsonStr(paramMap, "data", port, user);
			break;
		/* 核对总账 */
		case "3201":
			jsonStr = getJsonStr(paramMap, "data", port, user);
			break;
		/* 医药机构费用结算对明细账 */
		case "3202":
			jsonStr = getJsonStr(paramMap, "data", port, user);
			break;
		/* 目录对照上传 */
		case "3301":
			//jsonStr = getJsonStr(paramMap, "catalogcompin", port, user);
			jsonStr = getJsonStr(paramMap.get("params"), "data", port, user);
			break;
		/* 目录对照撤销 */
		case "3302":
			jsonStr = getJsonStr(paramMap, "catalogcompin", port, user);
			break;
		/* 冲正交易 */
		case "2601":
			jsonStr = getJsonStr(paramMap, "data", port, user);
			break;
		/* 医疗目录与医保目录匹配信息查询 */
		case "1317":
			jsonStr = getJsonStr(paramMap, "data", port, user);
			break;
		case "9101":
			jsonStr = getJsonStr(paramMap, "fsUploadIn", port, user);
			break;
		/* 慢特病备案 */	
		case "2503":
			jsonStr = getJsonStr(paramMap, "data", port, user);
			break;	
		/* 慢特病备案撤销 */	
		case "2504":
			jsonStr = getJsonStr(paramMap, "data", port, user);
			break;
		/* 慢特病备案查询 */
		case "5301":
			jsonStr = getJsonStr(paramMap, "data", port, user);
			break;
		/* 人员定点信息查询 */
		case "5302":
			jsonStr = getJsonStr(paramMap, "data", port, user);
			break;
		/* 参保缴费查询 */
		case "90100":
			jsonStr = getJsonStr(paramMap, "data", port, user);
			break;
		case "4101":
			JSONObject jsonObject1 = JSONObject.fromObject(paramMap.get("param"));
			jsonStr = getJsonStr(jsonObject1, port, user);
			break;
		/* 费用明细查询 */
		case "5204":
			jsonStr = getJsonStr(paramMap, "data", port, user);
			break;
		default:
			break;
		}
		User userInfo = (User) user;
		logger.info(port + "入参参数：" + jsonStr);
		
		/*
		InsQgybLog log=new InsQgybLog();
		log.setBuesType(port);
		log.setId(JsonUtil.getFieldValue(jsonStr, "msgid"));
		log.setContent(jsonStr);
		log.setPkPi(pkPi);
		log.setPkPv(pkPv);
		log.setCodeEmp(userInfo.getCodeEmp());
		insertLog(log);
		*/
		
		try {
			//rs = HttpClientUtil.sendHttpPost(HSA_URL + HSA_ACCOUNT_CODE + "/poc/hsa/hgs/" + port, jsonStr, map);
			rs = HttpClientUtil.sendHttpPost(HSA_URL + port, jsonStr, map);
			logger.info(port + "出参参数：" + rs);
			JSONObject obj = JSONObject.fromObject(rs);
			obj.put("msgid", JSONObject.fromObject(jsonStr).getString("msgid"));
			rs = obj.toString();
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("医保接口调用失败，接口port " + port);
			throw new BusException("医保返回失败" + port + e.getMessage());
		}

		return rs;
	}
	
	private String getJsonStr(Map<String, Object> paramMap, String key, String interfaceNO, IUser user) {
		//String d = DateUtils.dateToStr("yyyy-MM-dd HH:mm:ss", new Date());
		User userInfo = (User) user;
		
		//userInfo.setCodeEmp("1111");
		//userInfo.setNameEmp("测试");

		JSONObject jsonObject = new JSONObject();
		JSONObject obj = new JSONObject();
		JSONObject params = new JSONObject();

		for (Map.Entry<String, Object> entry : paramMap.entrySet()) {
			params.put(entry.getKey(), entry.getValue());
		}

		obj.put(key, params);
		jsonObject.put("recer_sys_code", "01");
		// jsonObject.put("msgid", "H44200100026202010161614560001");
		jsonObject.put("msgid", FIXMEDINS_CODE + DateUtils.dateToStr("yyyyMMddHHmmss", new Date()) + getSNcode());
		jsonObject.put("dev_no", "");
		jsonObject.put("dev_safe_info", "");
		jsonObject.put("signtype", "SM3");
		jsonObject.put("cainfo", "");
		jsonObject.put("inf_time", DateUtils.dateToStr("yyyy-MM-dd HH:mm:ss", new Date()) );
		jsonObject.put("input", obj);
		/* 经办人类别 1-经办人；2-自助终端；3-移动终端 */
		jsonObject.put("opter_type", "1");
		jsonObject.put("opter_name", userInfo.getNameEmp());
		jsonObject.put("opter", userInfo.getCodeEmp());
		jsonObject.put("insuplc_admdvs", "442000");
		jsonObject.put("mdtrtarea_admvs", "442000");
		jsonObject.put("recer_admvs", "442000");/* 接收方医保区划代码 */
		jsonObject.put("infver", VERSION);
		jsonObject.put("infno", interfaceNO);
		jsonObject.put("sign_no", "");
		jsonObject.put("fixmedins_name", FIXMEDINS_NAME);
		jsonObject.put("fixmedins_code", FIXMEDINS_CODE);

		return jsonObject.toString();
	}

	private String getJsonStr(JSONObject paramObj, String interfaceNO, IUser user) {
		//String d = DateUtils.dateToStr("yyyy-MM-dd HH:mm:ss", new Date());
		User userInfo = (User) user;

		JSONObject jsonObject = new JSONObject();

		jsonObject.put("recer_sys_code", "01");
		jsonObject.put("msgid", FIXMEDINS_CODE + DateUtils.dateToStr("yyyyMMddHHmmss", new Date()) + getSNcode());
		jsonObject.put("dev_no", "");
		jsonObject.put("dev_safe_info", "");
		jsonObject.put("signtype", "SM3");
		jsonObject.put("cainfo", "");
		jsonObject.put("inf_time", DateUtils.dateToStr("yyyy-MM-dd HH:mm:ss", new Date()) );
		jsonObject.put("input", paramObj);
		/* 经办人类别 1-经办人；2-自助终端；3-移动终端 */
		jsonObject.put("opter_type", "1");
		jsonObject.put("opter_name", userInfo.getNameEmp());
		jsonObject.put("opter", userInfo.getCodeEmp());
		jsonObject.put("insuplc_admdvs", "442000");
		jsonObject.put("mdtrtarea_admvs", "442000");
		jsonObject.put("recer_admvs", "442000");/* 接收方医保区划代码 */
		jsonObject.put("infver", VERSION);
		jsonObject.put("infno", interfaceNO);
		jsonObject.put("sign_no", "");
		jsonObject.put("fixmedins_name", FIXMEDINS_NAME);
		jsonObject.put("fixmedins_code", FIXMEDINS_CODE);

		return jsonObject.toString();
	}

	private String getJsonStr(Object param, String key, String interfaceNO, IUser user) {
		//String d = DateUtils.dateToStr("yyyy-MM-dd HH:mm:ss", new Date());
		User userInfo = (User) user;
		//userInfo.setCodeEmp("300552");
		JSONObject jsonObject = new JSONObject();
		Map<String,Object> objMap = new HashMap<>(16);
		objMap.put(key,param);

		jsonObject.put("recer_sys_code", "01");
		jsonObject.put("msgid", FIXMEDINS_CODE + DateUtils.dateToStr("yyyyMMddHHmmss", new Date()) + getSNcode());
		jsonObject.put("dev_no", "");
		jsonObject.put("dev_safe_info", "");
		jsonObject.put("signtype", "SM3");
		jsonObject.put("cainfo", "");
		jsonObject.put("inf_time", DateUtils.dateToStr("yyyy-MM-dd HH:mm:ss", new Date()) );
		jsonObject.put("input", objMap);
		/* 经办人类别 1-经办人；2-自助终端；3-移动终端 */
		jsonObject.put("opter_type", "1");
		jsonObject.put("opter_name", userInfo.getNameEmp());
		jsonObject.put("opter", userInfo.getCodeEmp());
		jsonObject.put("insuplc_admdvs", "442000");
		jsonObject.put("mdtrtarea_admvs", "442000");
		jsonObject.put("recer_admvs", "442000");/* 接收方医保区划代码 */
		jsonObject.put("infver", VERSION);
		jsonObject.put("infno", interfaceNO);
		jsonObject.put("sign_no", "");
		jsonObject.put("fixmedins_name", FIXMEDINS_NAME);
		jsonObject.put("fixmedins_code", FIXMEDINS_CODE);

		return jsonObject.toString();
	}

	private String getJsonStr(Object object, String interfaceNO, IUser user) {
		//String d = DateUtils.dateToStr("yyyy-MM-dd HH:mm:ss", new Date());
		User userInfo = (User) user;
		//userInfo.setCodeEmp("300552");
		HashMap<String, Object> map = new HashMap<>();

		map.put("recer_sys_code", "01");
		map.put("msgid", FIXMEDINS_CODE + DateUtils.dateToStr("yyyyMMddHHmmss", new Date()) + getSNcode());
		map.put("dev_no", "");
		map.put("dev_safe_info", "");
		map.put("signtype", "SM3");
		map.put("cainfo", "");
		map.put("inf_time", DateUtils.dateToStr("yyyy-MM-dd HH:mm:ss", new Date()) );
		map.put("input", object);
		/* 经办人类别 1-经办人；2-自助终端；3-移动终端 */
		map.put("opter_type", "1");
		map.put("opter_name", userInfo.getNameEmp());
		map.put("opter", userInfo.getCodeEmp());
		map.put("insuplc_admdvs", "442000");
		map.put("mdtrtarea_admvs", "442000");
		map.put("recer_admvs", "442000");/* 接收方医保区划代码 */
		map.put("infver", VERSION);
		map.put("infno", interfaceNO);
		map.put("sign_no", "");
		map.put("fixmedins_name", FIXMEDINS_NAME);
		map.put("fixmedins_code", FIXMEDINS_CODE);
		return JSON.toJSONString(map,SerializerFeature.WriteMapNullValue , SerializerFeature.WriteNullStringAsEmpty,SerializerFeature.WriteNullNumberAsZero);
	}

	private Map<String, String> getHeaderElement() {
		Map<String, String> map = new HashMap<String, String>();
		map.put("x-tif-paasid", HSA_PAASID);
		map.put("x-tif-nonce", RandomStringUtils.random(6, true, true));
		map.put("x-tif-timestamp", Long.toString(ZsrmQGUtils.getCurrentUnixSeconds()));
		map.put("x-tif-signature", ZsrmQGUtils.getSHA256Str(
				map.get("x-tif-timestamp") + SECRETKEY + map.get("x-tif-nonce") + map.get("x-tif-timestamp")));
		return map;
	}

	/**
	 * 查询HIS药品字典对照信息
	 * 交易号：015001013014
	 * @param param
	 */
	public PagingVo getHisPd(String param, IUser user){
		Map<String,Object> paraMap = JsonUtil.readValue(param, Map.class);
		PagingVo pagingVo = new PagingVo();
		// 分页操作
		int pageIndex = CommonUtils.getInteger(MapUtils.getString(paraMap,"pageIndex"));
		int pageSize = CommonUtils.getInteger(MapUtils.getString(paraMap,"pageSize"));
		MyBatisPage.startPage(pageIndex,pageSize);
		List<MedicalCharges> itemList = zsrmQGMapper.qryHisPdIns(paraMap);
		pagingVo.setItemList(zsrmQGMapper.qryHisPdIns(paraMap));
		Page<List<MedicalCharges>> page = MyBatisPage.getPage();
		pagingVo.setTotalCount(page.getTotalCount());
		return pagingVo;
	}

	/**
	 * 查询HIS收费项目字典对照信息
	 * 交易号：015001013015
	 * @param param
	 */
	public PagingVo getHisItem(String param, IUser user){
		Map<String,Object> paraMap = JsonUtil.readValue(param, Map.class);
		PagingVo pagingVo = new PagingVo();
		// 分页操作
		int pageIndex = CommonUtils.getInteger(MapUtils.getString(paraMap,"pageIndex"));
		int pageSize = CommonUtils.getInteger(MapUtils.getString(paraMap,"pageSize"));
		MyBatisPage.startPage(pageIndex,pageSize);
		pagingVo.setItemList(zsrmQGMapper.qryHisItemIns(paraMap));
		Page<List<MedicalCharges>> page = MyBatisPage.getPage();
		pagingVo.setTotalCount(page.getTotalCount());
		return pagingVo;
	}

    /**
     * 查询医保目录信息
	 * 交易号：015001013016
     * @param param
     */
	public PagingVo getCatalog(String param, IUser user){
		Map<String,Object> paraMap = JsonUtil.readValue(param, Map.class);
		PagingVo pagingVo = new PagingVo();
		// 分页操作
		int pageIndex = CommonUtils.getInteger(MapUtils.getString(paraMap,"pageIndex"));
		int pageSize = CommonUtils.getInteger(MapUtils.getString(paraMap,"pageSize"));
		MyBatisPage.startPage(pageIndex,pageSize);
		pagingVo.setInsur(zsrmQGMapper.qryInsQgybItem(paraMap));
		Page<List<MedicalCharges>> page = MyBatisPage.getPage();
		pagingVo.setTotalCount(page.getTotalCount());
		return pagingVo;
	}

	/**
	 * 获取对照信息
	 * 交易号：015001013017
	 * @param param
	 */
	public PagingVo getcontrast(String param, IUser user){
		Map<String,Object> paraMap = JsonUtil.readValue(param, Map.class);
		PagingVo pagingVo = new PagingVo();
		// 分页操作
		int pageIndex = CommonUtils.getInteger(MapUtils.getString(paraMap,"pageIndex"));
		int pageSize = CommonUtils.getInteger(MapUtils.getString(paraMap,"pageSize"));
		MyBatisPage.startPage(pageIndex,pageSize);
		String type=MapUtils.getString(paraMap,"projectType");
		if(StringUtils.isNotBlank(type))
		{
			if("1".equals(type))
			{
				pagingVo.setItemList(zsrmQGMapper.qryInsQgybItemmapPd(paraMap));
			}else if("3".equals(type))//耗材
			{
				pagingVo.setItemList(zsrmQGMapper.qryInsQgybItemmapItemHC(paraMap));
			}else//项目
			{
				pagingVo.setItemList(zsrmQGMapper.qryInsQgybItemmapItemXM(paraMap));
			}
		}
		Page<List<MedicalCharges>> page = MyBatisPage.getPage();
		pagingVo.setTotalCount(page.getTotalCount());
		return pagingVo;
	}

	/**
	 * 保存对照信息
	 * 交易号：015001013018
	 * @param param
	 */
	public void saveContrast(String param, IUser user){
		List<InsQgybItemMap> insItemlist = JsonUtil.readValue(param,new TypeReference<List<InsQgybItemMap>>(){});
		for(InsQgybItemMap insItemmap:insItemlist){
			Map<String,Object> paraMap = new HashMap<String,Object>();
			paraMap.put("codeHosp",insItemmap.getCodeHosp());
			List<Map<String, Object>> list = zsrmQGMapper.queryHisContrast(paraMap);
			if (list.size()>0){
				throw new BusException("该项目已对照，不能重复对照！");
			}
			insItemmap.setPkItemmap(NHISUUID.getKeyId());
			//目录类别(list_type):101:西药;102:中成药;103:中药饮片;104:自制剂;201:服务项目;301:医用材料;105:民族药
			if("Medical".equals(insItemmap.getHisType())){//物品标志1：物品；0：药品
				insItemmap.setFlagPd("0");
				switch (insItemmap.getListType()) {
					case "2":
						insItemmap.setListType("102");
						break;
					case "3":
						insItemmap.setListType("103");
						break;
					default:
						insItemmap.setListType("101");
						break;
				}
			}else{
				insItemmap.setFlagPd("1");
				if("07".equals(insItemmap.getListType().substring(1,2))){
					insItemmap.setListType("301");
				}else{
					insItemmap.setListType("201");
				}
			}
			insItemmap.setState("0");
			ApplicationUtils.setDefaultValue(insItemmap, true);
			DataBaseHelper.insertBean(insItemmap);
		}
	}

	/**
	 * 删除对照信息
	 * 交易号：015001013019
	 * @param param
	 * @param user
	 */
	public void deleteContrast(String param, IUser user){
		Map<String,Object> paraMap = JsonUtil.readValue(param, Map.class);
		if(StringUtils.isEmpty(MapUtils.getString(paraMap,"pkItemmap"))){
			throw new BusException("对照主键不能为空");
		}
		zsrmQGMapper.delInsQgybItemmap(paraMap);
	}

	/**
	 * 上传全国医保目录对照
	 *  交易号：015001013020
	 * @param param
	 * @param user
	 */
	public void uploadMatchCatalog(String param, IUser user){
		HisItemInfo hisItemInfo = JsonUtil.readValue(param, HisItemInfo.class);
		getHealthInsuranceEntry((Map<String, Object>) ApplicationUtils.beanToMap(hisItemInfo),user);
		for(String  PkItemmap :hisItemInfo.getPkItemmapList()){
			DataBaseHelper.update("UPDATE INS_QGYB_ITEMMAP SET STATE = '1', UPLOAD_DATE = ? WHERE PK_ITEMMAP = ?", new Object[]{new Date(),PkItemmap});
		}
	}

	/**
	 * 撤销医保目录对照
	 * 交易号：015001013021
	 * @param param
	 * @param user
	 */
	public void CancelMatchCatalog(String param, IUser user){
		HisItemInfo hisItemInfo = JsonUtil.readValue(param, HisItemInfo.class);
		for(HisItem hisItem:hisItemInfo.getParams()){
			hisItem.setFixmedinsCode(FIXMEDINS_CODE);
		}
		getHealthInsuranceEntry((Map<String, Object>) ApplicationUtils.beanToMap(hisItemInfo),user);
		for(String  PkItemmap :hisItemInfo.getPkItemmapList()){
			DataBaseHelper.update("UPDATE INS_QGYB_ITEMMAP SET STATE = '3' WHERE PK_ITEMMAP = ?",new Object[]{PkItemmap});
		}
	}
	
	
	/**
	 * 医疗目录与医保目录匹配信息查询
	 * 交易号：015001013022
	 * @param user
	 * @throws ParseException 
	 */
	public void ybMatchCatalog(String param, IUser user){
		HisItemInfo hisItemInfo = JsonUtil.readValue(param, HisItemInfo.class);
		if(hisItemInfo.getPkItemmapList().size()>0){
			for(String pkItemMap:hisItemInfo.getPkItemmapList()){
				Map<String, Object> paraMap = zsrmQGMapper.qryItemMap(pkItemMap);
				if(MapUtils.isNotEmpty(paraMap)){
					paraMap.put("port", "1317");
					paraMap.put("page_num", String.valueOf(hisItemInfo.getPageIndex()));
					paraMap.put("page_size", String.valueOf(hisItemInfo.getPageSize()));
					paraMap.put("fixmedins_code", FIXMEDINS_CODE);
					String result = getHealthInsuranceEntry(paraMap, user);
					if(StringUtils.isNotEmpty(result)){
						com.alibaba.fastjson.JSONObject jsonObject = com.alibaba.fastjson.JSONObject.parseObject(result);
						List<Map<String, Object>> dataList = JsonUtil.readValue(jsonObject.getJSONObject("output").getString("data"), new TypeReference<List<Map<String,Object>>>() {});
						if(dataList.size()>0){
							if(("1").equals(MapUtils.getString(dataList.get(0),"vali_flag")) && MapUtils.getString(paraMap,"HIS_ITEM_CODE").equals(MapUtils.getString(dataList.get(0),"medins_list_codg")) &&  MapUtils.getString(paraMap,"HILIST_CODE").equals(MapUtils.getString(dataList.get(0),"med_list_codg"))){
								DataBaseHelper.update("UPDATE INS_QGYB_ITEMMAP SET STATE = '2' WHERE PK_ITEMMAP = ?",new Object[]{pkItemMap});
							}else{
								DataBaseHelper.update("UPDATE INS_QGYB_ITEMMAP SET STATE = '9' WHERE PK_ITEMMAP = ?",new Object[]{pkItemMap});
							}
						}
					}
				}
			}
		}
	}

    /**
     * 全国医保费用结算总账对账查询
     * @param param
     * @param user
     */
    public List<Map<String, Object>> getCheckInsuranceSum(String param, IUser user){
        Map<String, Object> mapParam = JsonUtil.readValue(param,Map.class);
        List<Map<String, Object>> insuraList = zsrmQGMapper.qryQGYBMedicalInsuranceSum(mapParam);
        if(insuraList.size() >0){
			for(Map<String, Object> map:insuraList){
				Map<String, Object> insurMap = new HashMap<>();
				insurMap.put("port","3201");//医药机构费用结算对总账接口编码
				insurMap.put("insutype",MapUtils.getString(map,"insutype"));//险种
				insurMap.put("clr_type",MapUtils.getString(map,"clrType"));//清算类别
				insurMap.put("setl_optins","442000");//结算经办机构
				insurMap.put("stmt_begndate",MapUtils.getString(mapParam,"deBegn"));//对账开始日期
				insurMap.put("stmt_enddate",MapUtils.getString(mapParam,"deEnd"));//对账结束日期
				insurMap.put("medfee_sumamt",MapUtils.getString(map,"amountSt"));//医疗费总额
				insurMap.put("fund_pay_sumamt",MapUtils.getString(map,"amountInsu"));//基金支付总额
				insurMap.put("acct_pay",MapUtils.getString(map,"amountPi"));//个人账户支付金额
				insurMap.put("fixmedins_setl_cnt",MapUtils.getString(map,"dataNum"));//定点医药机构结算笔数
				String respon = getHealthInsuranceEntry(insurMap, user);
				if(StringUtils.isNotEmpty(respon)){
					com.alibaba.fastjson.JSONObject jsonObject = com.alibaba.fastjson.JSONObject.parseObject(respon);
					if ("0".equals(jsonObject.getString("infcode"))) {
						Map<String, Object> reqsMap = JsonUtil.readValue(jsonObject.getJSONObject("output").getString("stmtinfo"), new TypeReference<Map<String,Object>>() {});
                        map.put("stmtRsltDscr",MapUtils.getString(reqsMap,"stmt_rslt_dscr"));
					}else{
						map.put("stmtRsltDscr","医保反馈：" + jsonObject.getString("err_msg"));
					}
				}
			}
		}
        return insuraList;
    }

	/**
	 * 医保费用明细上传对账
	 * @param param
	 * @param user
	 */
	public void uploadCostDetailed(String param, IUser user){
		Map<String, Object> mapParam = JsonUtil.readValue(param,Map.class);
		//根据客户端传递开始时间、结束时间、险种查询相关费用明细
		List<Map<String, Object>> insuraDetailedList = zsrmQGMapper.qryQGYBMedicalInsuranceDetailed(mapParam);
		//费用明细上次文件号
		String uploadfileQuryNo;
		//调用全国医保明细上传方法
		String respUpl = uploadDetailedFile(insuraDetailedList,user);
		if(StringUtils.isNotEmpty(respUpl)){
			com.alibaba.fastjson.JSONObject jsonObject = com.alibaba.fastjson.JSONObject.parseObject(respUpl);
			if ("0".equals(jsonObject.getString("infcode"))) {
				Map<String, Object> reqsMap = JsonUtil.readValue(jsonObject.getString("output"), new TypeReference<Map<String,Object>>() {});
				uploadfileQuryNo = MapUtils.getString(reqsMap,"file_qury_no");
			}else{
				throw new BusException("医保反馈：" + jsonObject.getString("err_msg"));
			}
		}else{
			throw new BusException("全国医保结算费用明细上传响应为空!!!");
		}

        if(StringUtils.isNotEmpty(uploadfileQuryNo)){
        	////根据客户端传递开始时间、结束时间、险种查询相关总费用
			List<Map<String, Object>> insuraSumList = zsrmQGMapper.qryQGYBMedicalInsuranceSum(mapParam);
			if(insuraSumList.size()>0){
				Map<String, Object> insurMap = new HashMap<>();
				insurMap.put("port","3202");//医药机构费用结算对总账接口编码
				insurMap.put("setl_optins","442000");//结算经办机构
				insurMap.put("file_qury_no",uploadfileQuryNo);//文件查询号
				insurMap.put("stmt_begndate",MapUtils.getString(mapParam,"deBegn"));//对账开始日期
				insurMap.put("stmt_enddate",MapUtils.getString(mapParam,"deEnd"));//对账结束日期
				insurMap.put("medfee_sumamt",MapUtils.getString(insuraSumList.get(0),"amountSt"));//医疗费总额
				insurMap.put("fund_pay_sumamt",MapUtils.getString(insuraSumList.get(0),"amountInsu"));//基金支付总额
				insurMap.put("cash_payamt",MapUtils.getString(insuraSumList.get(0),"amountPi"));//现金支付金额
				insurMap.put("fixmedins_setl_cnt",MapUtils.getString(insuraSumList.get(0),"dataNum"));//定点医药机构结算笔数
				String respon = getHealthInsuranceEntry(insurMap, user);
				if(StringUtils.isNotEmpty(respon)){
					com.alibaba.fastjson.JSONObject jsonObject = com.alibaba.fastjson.JSONObject.parseObject(respon);
					if ("0".equals(jsonObject.getString("infcode"))) {
						Map<String, Object> reqsMap = JsonUtil.readValue(jsonObject.getJSONObject("output").getString("fileinfo"), new TypeReference<Map<String,Object>>() {});
					}else{
					}
				}
			}else{
				throw new BusException("未查询到该险种总费用信息");
			}

		}else{
			throw new BusException("未获取的上传明细后反馈的文件号");
		}
	}

	/**
	 * 全国医保费用结算明细组装并上传
	 * @param paramList
	 * @param user
	 * @return
	 */
	public String uploadDetailedFile(List<Map<String, Object>> paramList,IUser user){
		if(paramList.size()<=0){
			return null;
		}
		String respon;
		String fileName = DateUtils.formatDate(new Date(),"yyyyMMddHHmmss");
		//在当前路径下配置文件夹
		File file = new File(UploadFilePath+fileName+".zip");
		// 先得到文件的上级目录，判断并创建文件
		if(!file.getParentFile().exists()){
			file.getParentFile().mkdir();
		}
		try {
			//执行创建
			file.createNewFile();
			//输出流
			FileOutputStream fos = new FileOutputStream(file);
			//zip写入流
			ZipOutputStream zos = new ZipOutputStream(fos);
			//获取最终命名的规则(此处以.txt命名，也可以其它方式命名)
			String name = new String((fileName).getBytes("UTF-8"))+ ".txt";
			//创建ZIP实体，并添加进压缩包
			ZipEntry zipEntry = new ZipEntry(name);
			zos.putNextEntry(zipEntry);
			for(int i=0;i<paramList.size();i++){
				//读取待压缩的文件并写进压缩包里
				zos.write((MapUtils.getString(paramList.get(i),"setlId","") + "\t").getBytes("UTF-8"));
				zos.write((MapUtils.getString(paramList.get(i),"mdtrtId","") + "\t").getBytes("UTF-8"));
				zos.write((MapUtils.getString(paramList.get(i),"psnNo","") + "\t").getBytes("UTF-8"));
				zos.write((MapUtils.getString(paramList.get(i),"medfeeSumamt","") + "\t").getBytes("UTF-8"));
				zos.write((MapUtils.getString(paramList.get(i),"fundPaySumamt","") + "\t").getBytes("UTF-8"));
				zos.write((MapUtils.getString(paramList.get(i),"acctPay","") + "\t").getBytes("UTF-8"));
				if(i==(paramList.size()-1)){
					zos.write((MapUtils.getString(paramList.get(i),"refdSetlFlag","")).getBytes("UTF-8"));
				}else{
					zos.write((MapUtils.getString(paramList.get(i),"refdSetlFlag","")+"\n").getBytes("UTF-8"));
				}
			}
			//最终记得要关闭流
			zos.flush();
			zos.close();
			fos.close();
			Map<String, Object> insurMap = new HashMap<>();
			insurMap.put("port","9101");//文件上传
			//insurMap.put("in",file);
			insurMap.put("in",FileUtils.readFileToByteArray(file));
			insurMap.put("filename",file.getName());
			insurMap.put("fixmedins_code",FIXMEDINS_CODE);
			respon = getHealthInsuranceEntry(insurMap, user);
		} catch (IOException e) {
			throw new BusException(e.getMessage());
		}
		return respon;
	}


	public void downloadDetailedFile(){

	}
	//预留接口，字典下载
	public void downLoadInsDict(String param, IUser user) {
		Map<String, Object> paramMap = JsonUtil.readValue(param, Map.class);

		Map<String, Object> retMap = new HashMap<>();
		paramMap.put("port", "1901");
		paramMap.put("type", "");
		String ret = getHealthInsuranceEntry(paramMap, user);
		retMap = JsonUtil.readValue(ret, Map.class);

		if ("0".equals(CommonUtils.getString(retMap.get("infcode")))) {
			retMap = (Map<String, Object>) retMap.get("output");
			List<InsQgybDict> dictList = JsonUtil.readValue(JsonUtil.writeValueAsString(retMap.get("list")),
					new TypeReference<List<InsQgybDict>>() {
					});
			for (InsQgybDict dict : dictList) {
				ApplicationUtils.setDefaultValue(dict, true);
				dict.setCodeType(dict.getCodeType().toLowerCase());
				dict.setCreator("yl20210106");
				dict.setEuHpdicttype("1");
				dict.setFlagDef("0");
				dict.setStopFlag("0");
				dict.setFlagChd("1");

			}

			DataBaseHelper.batchUpdate(DataBaseHelper.getInsertSql(InsQgybDict.class), dictList);
		}
	}
	

	/* 截取0607医保交互顺序号后四位 */
	private String getSNcode() {
		String SNcode = ApplicationUtils.getCode("0607");
		if (SNcode != null && SNcode.length() >= 4) {
			SNcode = SNcode.substring(SNcode.length() - 4, SNcode.length());
			return SNcode;
		} else {
			throw new BusException("根据编码规则【0607】未获取的有效的医保顺序号SNcode");
		}
	}
	/**
	 * 参保缴费查询
	 * 015001013034
	 * @param param
	 * @param user
	 */
	public List<Map<String, Object>> qryInsFees(String params, IUser user){
		Map<String, Object> paramMap = JsonUtil.readValue(params, Map.class);
		paramMap = ZsrmQGUtils.humpToMap(paramMap);
		if (paramMap.get("psn_no") != null) {
			paramMap.put("port", "90100");
			String ret = getHealthInsuranceEntry(paramMap, user);

			Map<String, Object> retMap = JsonUtil.readValue(ret, Map.class);
			if ("0".equals(CommonUtils.getString(retMap.get("infcode")))) {
				
				List<Map<String, Object>> insFees= (List<Map<String, Object>>)retMap.get("output"); // (List<Map<String, Object>>) ((Map<String,Object>)retMap.get("output"));
				
				for (Map<String, Object> fee : insFees) {
					fee.put("accrymBegn", fee.get("accrym_begn"));
					fee.remove("accrym_begn");
					fee.put("accrymEnd", fee.get("accrym_end"));
					fee.remove("accrym_end");	
					fee.put("PoolareaNo", fee.get("poolarea_no"));
					fee.remove("poolarea_no");
					fee.put("InsutypeName", fee.get("insutype_name"));
					fee.remove("insutype_name");
					fee.put("ClctTypeName", fee.get("clct_type_name"));
					fee.remove("clct_type_name");
					fee.put("ClctFlagName", fee.get("clct_flag_name"));
					fee.remove("clct_flag_name");
					fee.put("ClctTime", fee.get("clct_time"));
					fee.remove("clct_time");
				}

				return insFees;
			} else {
				throw new BusException("功能号90100，参保缴费信息查询失败：" + CommonUtils.getString(retMap.get("err_msg")));
			}
		}else{
			throw new BusException("功能号90100，参保缴费信息查询失败：未获取到人员编号");
		}
	}
	
/**
 * @param params
 * @param user
 */
public void upLoadSettle(String params, IUser user){
		Map paramMap = JsonUtil.readValue(params, Map.class);

		User userInfo = (User) user;

		Map<String, Object> rtnMap = new HashMap<>(16);
		List<Map<String,Object>> stList= new ArrayList<>(); 

		//查询就诊信息
		PvEncounter pvInfo = zsrmQGMapper.qryPvInfoByPkSettle(paramMap);
		//医保支付明细信息
		List<Map<String,Object>> insuPayList = new ArrayList<>();

		paramMap.put("pkpv",pvInfo.getPkPv());
		InsQgybSettleList insQgybSettleList = new InsQgybSettleList();

//		if(EnumerateParameter.THREE.equals(pvInfo.getEuPvtype())){
//			//查询结算信息
//			insQgybSettleList.setSetlinfo(zsrmQGMapper.qryStInfo(paramMap).get(0)) ;
//
//			//查询医保基金支付信息
//			//insuPayList = zsrmQGMapper.qryInsuPayDtls(paramMap);
//
//			//查询住院诊断信息
//			rtnMap.put("stDiagList",zsrmQGMapper.qryDiseinfoList(paramMap));
//
//			//查询收费项目信息
//			rtnMap.put("stChargeList",zsrmQGMapper.qryChargeDtls(paramMap));
//
//			//查询手术信息
//			rtnMap.put("stOperTeList",zsrmQGMapper.qryIclDtls(paramMap));
//		}else{
//			//查询结算信息
//			InsQgybSetInfo insQgybSetInfo=zsrmQGMapper.qryOpStInfo(paramMap).get(0);
//			insQgybSetInfo.setFixmedinsCode(FIXMEDINS_CODE);
//			insQgybSetInfo.setFixmedinsCode(FIXMEDINS_NAME);
//			insQgybSetInfo.setIptMedType("1");//华资回复门特填1
//			insQgybSetInfo.setHiSetlLv("02");
//			insQgybSetInfo.setNtly("CHN");
//			insQgybSetInfo.setNaty("99");
//
//			insQgybSetInfo.setHsorg(FIXMEDINS_NAME);
//			insQgybSetInfo.setHsorgOpter(userInfo.getNameEmp());
//			insQgybSetInfo.setMedinsFillDept(userInfo.getPkDept());
//			insQgybSetInfo.setMedinsFillPsn(userInfo.getCodeEmp());
//
//			insQgybSettleList.setSetlinfo(insQgybSetInfo) ;
//			//查询医保支付明细信息
//			insQgybSettleList.setPayinfo( zsrmQGMapper.qryInsuPayDtls(paramMap));;
//
//			//查询门诊慢特病诊断信息（节点标识：opspdiseinfo）
//			insQgybSettleList.setOpspdiseinfo(zsrmQGMapper.qryoOpspDiseinfoList(paramMap));
//			//查询住院诊断信息
//			insQgybSettleList.setDiseinfo(zsrmQGMapper.qryDiseinfoList(paramMap));
//
//			//查询收费项目信息
//			insQgybSettleList.setIteminfo(zsrmQGMapper.qryOpChargeDtls(paramMap));
//			//重症监护信息
//			insQgybSettleList.setIcuinfo(new ArrayList<InsQgybIcuInfo>());
//
//			//门诊无手术，必填项，暂存临时值
//			InsQgybOprnInfo insQgybOprnInfo=new InsQgybOprnInfo();
//			insQgybOprnInfo.setOprnOprtType("-");
//			insQgybOprnInfo.setOprnOprtName("-");
//			insQgybOprnInfo.setOperDrCode("-");
//			insQgybOprnInfo.setOperDrName("-");
//			insQgybOprnInfo.setOperDrCode("-");
//			insQgybOprnInfo.setAnstDrCode("-");
//			insQgybOprnInfo.setAnstWay("-");
//			insQgybOprnInfo.setAnstDrName("-");
//			insQgybOprnInfo.setOprnOprtCode("-");
//			insQgybOprnInfo.setOprnOprtDate(new Date());
//			List<InsQgybOprnInfo> insQgybOprnInfos=new ArrayList<InsQgybOprnInfo>();
//			insQgybOprnInfos.add(insQgybOprnInfo);
//			insQgybSettleList.setOprninfo(insQgybOprnInfos);
//		}
		
		HashMap<String, Object> map = new HashMap<>();
		map.put("port","4101");
		map.put("param",JsonUtil.writeValueAsString(ApplicationUtils.beanToMap(insQgybSettleList)));
		String retString=getHealthInsuranceEntry(map,user);

			//rtnMap.put("stInfo",ApplicationUtils.beanToMap(stList.get(0)));
		}

	/**
	 * 医保保障基金结算清单上传
	 * @param params
	 * @param user
	 * @return
	 */
	public void upLoadSettleList(String params, IUser user) {
		User userInfo = (User) user;
//		根据时间范围或者就诊主键查询结算清单
		Map paramMap = JsonUtil.readValue(params, Map.class);
//		检验参数
		String pkPv = (String) paramMap.get("pkPv");
		Object beginTime = paramMap.get("beginTime");
		Object endTime = paramMap.get("endTime");
		if(StringUtils.isBlank(pkPv) && (beginTime == null || endTime == null)){
			throw new BusException("无法获取到参数信息");
		}
		try {
			if(beginTime != null) {
				Date dtStart = DateUtils.getDefaultDateFormat().parse((String) beginTime);
				paramMap.put("beginTime", DateUtils.dateToStr("yyyy-MM-dd", dtStart) + " 00:00:00");
			}
			if(endTime != null) {
				Date dtEnd = DateUtils.getDefaultDateFormat().parse((String) endTime);
				paramMap.put("endTime", DateUtils.dateToStr("yyyy-MM-dd", dtEnd) + " 23:59:59");
			}
		} catch (ParseException e) {
			e.printStackTrace();
			throw new BusException("日期转换出错");
		}
		List<Map<String, Object>> list = zsrmQGMapper.qrySettleList(paramMap);
		if (!list.isEmpty()) {
			String pkDept = userInfo.getPkDept();
			String deptName = "";
			if(StringUtils.isNotBlank(pkDept)) {
				deptName = DataBaseHelper.queryForScalar("select name_dept from bd_ou_dept where pk_dept=?", String.class, pkDept);
			}
			HashMap<String, InsQgybSettleList> settleMap = new HashMap<>();
			ArrayList<String> setIdList = new ArrayList<>();
            for (Map<String, Object> map : list) {
                String setlId = (String) map.get("setlId");
				setIdList.add(setlId);
//		1、结算清单信息,节点标识:setlinfo
				InsQgybSetInfo insQgybSetInfo = new InsQgybSetInfo();
				ZsrmQGUtils.mapToBean(insQgybSetInfo, map);
//					结算信息中添加属性
				insQgybSetInfo.setFixmedinsName(FIXMEDINS_NAME);
				insQgybSetInfo.setFixmedinsCode(FIXMEDINS_CODE);
				insQgybSetInfo.setDclaTime(new Date(System.currentTimeMillis()));
				Date bithDate = insQgybSetInfo.getBrdy();
				Calendar calendar = Calendar.getInstance();
				calendar.setTime(bithDate);
				long time = calendar.getTimeInMillis();
				long days = (System.currentTimeMillis() - time) / (1000 * 3600 * 24);
				if (days > 365L) {
					insQgybSetInfo.setAge((int) days / 365);
				} else {
					insQgybSetInfo.setNwbAge((int) days);
				}
				insQgybSetInfo.setHsorg(FIXMEDINS_NAME);
				insQgybSetInfo.setHsorgOpter(userInfo.getUserName());
				insQgybSetInfo.setMedinsFillDept(deptName);
				insQgybSetInfo.setMedinsFillPsn(userInfo.getUserName());
                List<InsQgybPayInfo> payList = new LinkedList<>();
                List<InsQgybOpspdiseinfo> opspdiseList = new LinkedList<>();
                List<InsQgybDiseinfo> diseList = new LinkedList<>();
                List<InsQgybIteminfo> itemList = new LinkedList<>();
                List<InsQgybOprnInfo> oprnList = new LinkedList<>();
                List<InsQgybIcuInfo> icuList = new LinkedList<>();
                InsQgybSettleList insQgybSettleList = new InsQgybSettleList(insQgybSetInfo, payList, opspdiseList, diseList, itemList, oprnList, icuList);
                settleMap.put(setlId,insQgybSettleList);
			}
//		2、基金支付信息,节点标识：payinfo
			List<Map<String,Object>> payInfoList = zsrmQGMapper.qryInsQgybPayInfo(setIdList);
            if(!payInfoList.isEmpty()) {
				for (Map<String, Object> map : payInfoList) {
					InsQgybPayInfo insQgybPayInfo = new InsQgybPayInfo();
					ZsrmQGUtils.mapToBean(insQgybPayInfo, map);
					String setlId = (String) map.get("setlId");
					settleMap.get(setlId).getPayinfo().add(insQgybPayInfo);
				}
			}
			List<Map<String,String>> diagList = zsrmQGMapper.qryInsQgybDiagInfo(setIdList);
            if(!diagList.isEmpty()){
				for (Map<String, String> map : diagList) {
					String clrType = map.get("clrType");
					String setlId = map.get("setlId");
					if("21".equals(clrType)){
//						4、住院诊断信息（节点标识：diseinfo）
						InsQgybDiseinfo insQgybDiseinfo = new InsQgybDiseinfo();
						ZsrmQGUtils.mapToBean(insQgybDiseinfo,map);
						settleMap.get(setlId).getDiseinfo().add(insQgybDiseinfo);
					}else{
//						3、门诊慢特病诊断信息（节点标识：opspdiseinfo）
						InsQgybOpspdiseinfo insQgybOpspdiseinfo = new InsQgybOpspdiseinfo();
						ZsrmQGUtils.mapToBean(insQgybOpspdiseinfo,map);
						settleMap.get(setlId).getOpspdiseinfo().add(insQgybOpspdiseinfo);
					}
				}
			}
//		5、收费项目信息（节点标识：iteminfo）
			List<Map<String,String>> itemList = zsrmQGMapper.qryInsQgybItemInfo(setIdList);
			String[] medChrgitmTypes = new String[]{"01","02","03","04","05","06","07","08","09","10","11","12","13","14"};
			for (Map<String, String> map : itemList) {
				String setlId = map.get("setlId");
				InsQgybIteminfo insQgybIteminfo = new InsQgybIteminfo();
				ZsrmQGUtils.mapToBean(insQgybIteminfo,map);
				insQgybIteminfo.setOthAmt(insQgybIteminfo.getAmt().subtract(insQgybIteminfo.getClaaSumfee()).subtract(insQgybIteminfo.getClabAmt()).subtract(insQgybIteminfo.getFulamtOwnpayAmt()));
				String medChrgitm = insQgybIteminfo.getMedChrgitm();
				if (Arrays.binarySearch(medChrgitmTypes,medChrgitm)<0){
					insQgybIteminfo.setMedChrgitm("14");
				}
				settleMap.get(setlId).getIteminfo().add(insQgybIteminfo);
			}
//		6、手术操作信息（节点标识：oprninfo）
			List<Map<String,Object>> oprnList = zsrmQGMapper.qryInsQgybOprnInfo(setIdList);
            if(!oprnList.isEmpty()){
				for (Map<String, Object> map : oprnList) {
					InsQgybOprnInfo insQgybOprnInfo = new InsQgybOprnInfo();
					ZsrmQGUtils.mapToBean(insQgybOprnInfo,map);
					String setlId = (String) map.get("setlId");
					settleMap.get(setlId).getOprninfo().add(insQgybOprnInfo);
				}
			}

//		7、重症监护信息（节点标识：icuinfo）

//			一次只能上传一个患者的信息
			for (Map.Entry<String, InsQgybSettleList> entry : settleMap.entrySet()) {
				InsQgybSettleList value = entry.getValue();
				HashMap<String, Object> map = new HashMap<>();
				map.put("port","4101");
				map.put("param",value);
				getHealthInsuranceEntry(map,user);
			}
		}
	}

	/**
	 * 批量处理医保退费
	 * psn_no,mdtrt_id,setl_id
	 */
	public Map<String, Object> SpecialTreatment(String params, IUser user) {
		List<Map<String, Object>> paramMap = JsonUtil.readValue(params, List.class);
		User u = (User)user;
		InsQgybSt insStCancel = null;
		for (int i = 0; i < paramMap.size(); i++) {
			Map<String, Object> retMap = new HashMap<String, Object>();
			Map<String, Object> reqMap = new HashMap<String, Object>();
			reqMap.put("port", "2208");
			reqMap.put("mdtrt_id", paramMap.get(i).get("mdtrtId"));
			reqMap.put("psn_no", paramMap.get(i).get("psnNo"));
			reqMap.put("setl_id", paramMap.get(i).get("setlId"));
			String retStr = getHealthInsuranceEntry(reqMap, user);
			retMap = JsonUtil.readValue(retStr, Map.class);
			if ("0".equals(CommonUtils.getString(retMap.get("infcode")))) {
				insStCancel = null;
				retMap = (Map<String, Object>) ((Map<String, Object>) retMap.get("output")).get("setlinfo");
				insStCancel = JsonUtil.readValue(JsonUtil.writeValueAsString(retMap), InsQgybSt.class);
				ApplicationUtils.setDefaultValue(insStCancel, true);
				insStCancel.setPsnType("");
				insStCancel.setPkVisit(u.getPkOrg());
				insStCancel.setPkHp(u.getPkOrg());
				insStCancel.setPkPi(u.getPkOrg());
				insStCancel.setPkPv(u.getPkOrg());
				insStCancel.setPkInsstCancel(u.getPkOrg());
				insStCancel.setAmount(insStCancel.getMedfeeSumamt());// 总金额
				insStCancel.setAmtGrzhzf(insStCancel.getAcctPay());// 个人账户
				insStCancel.setAmtJjzf(insStCancel.getFundPaySumamt());// 基金
				insStCancel.setAmtGrzf(insStCancel.getPsnCashPay());// 现金
				insStCancel.setSetlIdCancel(paramMap.get(i).get("setlId").toString());
				insStCancel.setYbPksettle(insStCancel.getSetlId());
				insStCancel.setPsnNo(paramMap.get(i).get("psnNo").toString());
				insStCancel.setDateSt(new Date());
				insStCancel.setCreator("新his工程师06-25");
				insertInsSt(insStCancel);
			}else if(CommonUtils.getString(retMap.get("err_msg")).contains("该笔结算已撤销，请勿重复办理")){
				retMap.clear();
				InsQgybSt insSt = DataBaseHelper.queryForBean(
						"select * from ins_qgyb_st where SETL_ID_CANCEL=? and del_flag='0' ", InsQgybSt.class, paramMap.get(i).get("setlId").toString());
				retMap.put("ybPksettle", paramMap.get(i).get("setlId").toString());
				retMap.put("amtJjzf", insSt.getFundPaySumamt());
				return retMap;
			} else {
				Log.error("功能号2208，医保退费失败" + CommonUtils.getString(retMap.get("err_msg")));
				throw new BusException("功能号2208，医保退费失败" + CommonUtils.getString(retMap.get("err_msg")));
			}
		}
		return (Map<String, Object>) ApplicationUtils.beanToMap(insStCancel);
	}

}