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

	// ????????????????????????
	@Resource(name = "transactionManager")
	private PlatformTransactionManager platformTransactionManager;

	/**
	 * ????????????????????????
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
 
		// ??????????????????=15?????????18??????<15??? ?????????
		if (paramMap.containsKey("mdtrt_cert_no") && paramMap.get("mdtrt_cert_no")!=null) {
			String cernNo = CommonUtils.getString(paramMap.get("mdtrt_cert_no"));
			if (cernNo.length() == 15) {
				// 15?????????????????????18???
				paramMap.put("mdtrt_cert_no", ZsrmQGUtils.getEighteenIDCard(cernNo));
			} else {
				if (pi != null && !"01".equals(pi.getDtIdtype())) {
					//?????????????????????????????????(1,2)????????????????????????????????????3??????
					if(pi.getIdNo().length()<=3){
						throw new BusException("?????????1101????????????????????????: "+pi.getIdNo());
					}
			
					// ????????????????????????
					if ("02".equals(pi.getDtIdtype()) || "04".equals(pi.getDtIdtype()) || "05".equals(pi.getDtIdtype())) {
						paramMap.put("mdtrt_cert_type", "99");
						paramMap.put("certno", pi.getIdNo());
					} else {
						paramMap.put("mdtrt_cert_type", "03");
						// throw new BusException("?????????????????????????????? "+pi.getDtIdtype());
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
					//??????????????????????????????
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

				//?????????????????????1???????????????????????????  2?????????310-?????????????????????390-??????
				if(CommonUtils.isNotNull(insu390) || CommonUtils.isNotNull(insu310)) {
					paramMap.put("insutype", CommonUtils.isNotNull(insu310) ? insu310 : insu390);
					paramMap.put("pkPv", pkPv);
					zsrmQGMapper.updatePv(paramMap);
				}
			}
			return inspi;
		
		}
		//??????????????????????????????
		else if("99".equals(paramMap.get("mdtrt_cert_type")) && CommonUtils.getString(retMap.get("err_msg")).contains("?????????????????????????????????")){
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
				throw new BusException("?????????1101?????????????????????????????????  " + CommonUtils.getString(retMap.get("err_msg")));
			}
		}
		else{
			throw new BusException("?????????1101?????????????????????????????????  " + CommonUtils.getString(retMap.get("err_msg")));
		}
		
	}
	/**
	 * ????????????
	 */
	public String insCheck(String params, IUser user){
		Map<String, Object> paramMap = JsonUtil.readValue(params, Map.class);
		paramMap = ZsrmQGUtils.humpToMap(paramMap);
		String psnNo = CommonUtils.getPropValueStr(paramMap, "psn_no");
		//??????????????????
		if (CommonUtils.isNull(psnNo)) {
			InsPiInfoVo insPi = getPersonInfo(params,user);
			paramMap.put("psn_no", insPi.getBaseinfo().getPsnNo());
		}
		
		//????????????
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
						throw new BusException("????????????????????????");
					}else{
						throw new BusException("????????????????????????");
					}
				}
			}
			throw new BusException("??????????????????????????????");
		}else{
			throw new BusException("?????????2001??????????????????????????? " + CommonUtils.getString(retMap.get("err_msg")));
		}

	}

	/**
	 * ????????????
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
			throw new BusException("?????????2201,??????????????????  " + CommonUtils.getString(retMap.get("err_msg")));
		}

		return retMap;
	}

	/**
	 * @param params
	 *            015001013002 ?????????????????????
	 */
	public Map<String,Object> mzHpHuaJia(String params, IUser user) throws Exception {
		Map<String, Object> paramMap = JsonUtil.readValue(params, Map.class);

		// ????????????
		String errMsg = validaParam(paramMap);
		if (CommonUtils.isNotNull(errMsg)) {
			throw new BusException(errMsg);
		}

		// ????????????
		Map<String, Object> reqMap = new HashMap<>();
		Map<String, Object> retMap = new HashMap<>();
		String retStr = "";
		String pkPv = paramMap.get("pkPv").toString();
		User userInfo = (User) user;
		
		//??????
		signOper(paramMap,userInfo);

		//????????????????????????
		InsQgybVisit visit = getPvVist(paramMap, user);
		if (visit == null || CommonUtils.isNull(visit.getMedType())) {
			throw new BusException("????????????????????????????????????");
		}
		paramMap.put("YBRegInfo", visit);

		InsQgybVisit insMedtype = qryVisitInfo(pkPv);
		if (insMedtype == null || insMedtype.getMedType() == null) {
			throw new BusException("???????????????????????????????????????");
		}
		
		
		// ??????????????????
		String chrgBchno = ApplicationUtils.getCode("0608");// ???????????????
		String strSn=chrgBchno;//?????????????????????
		if (strSn != null && strSn.length() >= 4) {
			strSn =strSn.substring(strSn.length() - 4,strSn.length());
		}
		
							
		paramMap.put("chrgBchno", chrgBchno);
		paramMap.put("strSn", strSn);
		paramMap.put("insutype", visit.getInsutype());
		List<Map<String, Object>> dts = zsrmQGMapper.qryChargeDetailNoUpload(paramMap);
		
		//???????????????????????????????????????????????????????????????
		if(dts==null || dts.size()==0){
			InsQgybPreSt preSt = new InsQgybPreSt();
			Double dtAmt=0d;
			
			//?????????????????????
			List<BlOpDt> dtRet=zsrmQGMapper.qryChargeDetailNoUploadSpec(paramMap);
			for(BlOpDt dt :dtRet){
				dtAmt=dtAmt+dt.getAmount();
			}
			
			preSt.setAggregateAmount(dtAmt);
			preSt.setMedicarePayments(0d);
			preSt.setPatientsPay(dtAmt);
			return (Map<String, Object>) ApplicationUtils.beanToMap(preSt);
		}

		// ????????????????????????????????????????????????????????????????????????????????????????????????????????????
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
			// ?????????????????????????????????
			if (errorMsg != null && !CommonUtils.isEmptyString(errorMsg.toString())) {
				throw new BusException("HIS?????????????????????????????????????????????\r\n" + errorMsg.toString());
			}
		}		
		dts = ZsrmQGUtils.humpToLineListMap(dts);
		retMap.clear();
		reqMap.put("pkPv", pkPv);
		reqMap.put("insutype", visit.getInsutype());
		//??????????????????
		mzBillDel(JsonUtil.writeValueAsString(reqMap), user);
		
		//??????????????????
		reqMap.put("port", "2204");
		reqMap.put("params", dts);
		reqMap.put("pk_pv", pkPv);
		//Map<String,Object> uplod = ZsrmQGUtils.humpToMap(reqMap);
		Map<String,Object> uplod = reqMap;

		retStr = getHealthInsuranceEntry(uplod, user);
		retMap = JsonUtil.readValue(retStr, Map.class);
		if (!"0".equals(CommonUtils.getString(retMap.get("infcode")))) {
			throw new BusException("?????????2204?????????????????????????????????????????????" + CommonUtils.getString(retMap.get("err_msg")));
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
		// ????????????????????????
		DefaultTransactionDefinition def = new DefaultTransactionDefinition();
		def.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRES_NEW);
		TransactionStatus status = platformTransactionManager.getTransaction(def);

		try {
			DataBaseHelper.batchUpdate(DataBaseHelper.getInsertSql(InsQgybCg.class), cgs);

			platformTransactionManager.commit(status); // ????????????
		} catch (Exception e) {
			platformTransactionManager.rollback(status); // ???????????? ???????????????
			e.printStackTrace();
			throw new BusException("??????????????????????????????????????????" + e);
		}

		// ???????????????
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

			outParamHuaJia.setAmount(outParamHuaJia.getMedfeeSumamt());// ?????????
			//outParamHuaJia.setAmtGrzhzf(outParamHuaJia.getAcctPay());// ????????????
			outParamHuaJia.setAmtGrzf(outParamHuaJia.getPsnCashPay());// ??????
			outParamHuaJia.setAmtJjzf(outParamHuaJia.getFundPaySumamt());// ??????

			preSt.setYbPreSettleInfo(outParamHuaJia);
			preSt.setYbPreSettleParam(reqMap);
			return (Map<String, Object>) ApplicationUtils.beanToMap(preSt);

		} else {
			logger.error(retStr);
			// ????????????????????????????????????
			reqMap.clear();
			reqMap.put("pkPv", pkPv);
			reqMap.put("insutype", visit.getInsutype());
			mzBillDel(JsonUtil.writeValueAsString(reqMap), user);
			throw new BusException("?????????2206???????????????????????????" + CommonUtils.getString(retMap.get("err_msg")));
		}
	}
	// ?????? qgybSignIn+codeEmp
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
	 * ?????????????????????????????????????????????
	 */
	private void validaMedType(String pkPv) {

		InsQgybPv insQgybPv = DataBaseHelper.queryForBean("select * from ins_qgyb_pv where pk_pv=? and del_flag='0' ",
				InsQgybPv.class, pkPv);
		if (insQgybPv == null) {
			throw new BusException("??????????????????????????????????????????????????? ");
		}
		
		String errMsg = null;
		InsQgybSt insStLast = qryInsSt(insQgybPv.getPkPv());
		if (insStLast != null) {
			// ????????????????????????????????????
			if (insQgybPv.getMedType() == null && "11".equals(insStLast.getMedType())) {
				return;
			} else if (insQgybPv.getMedType().equals(insStLast.getMedType())) {
				return;		
			}

			errMsg = "?????????????????????????????????" + getQgybDict("med_type", insStLast.getMedType()).getName() + "???????????????"
					+ getQgybDict("med_type", insQgybPv.getMedType()).getName() + "??????????????????????????????????????????";
		}
		
		if(errMsg!=null){
			throw new BusException(errMsg);
		}
	}

	/**
	 * @param codeType ????????????
	 * @param code ??????
	 * @return
	 */
	private InsQgybDict getQgybDict(String codeType,String code){
		InsQgybDict dict=DataBaseHelper.queryForBean("select * from (select * from ins_qgyb_dict where code_type= ? and code= ? and del_flag='0' ) where ROWNUM=1",
				InsQgybDict.class, codeType,code);
		
		return dict;
	}
				
	/**
	 * @param params
	 *            015001013003 ????????????
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
				if(CommonUtils.getString(paramMap.get("err_msg")).contains("???????????????????????????????????????")
						|| CommonUtils.getString(paramMap.get("err_msg")).contains("????????????List????????????")){
					return;
				}
				Log.error(CommonUtils.getString(paramMap.get("err_msg")));
				throw new BusException("?????????2205??????????????????????????????????????? " + CommonUtils.getString(paramMap.get("err_msg")));
			}
		}
	}

	/**
	 * ???????????????????????????????????????
	 */
	private String validaParam(Map<String, Object> paramMap) {
		String errMsg = "";

		if (paramMap == null) {
			errMsg = "????????????????????????";
			return errMsg;
		}

		if (CommonUtils.isNull(paramMap.get("pkPv"))) {
			errMsg = "???????????????????????????pkPv";
			return errMsg;
		}

		if (CommonUtils.isNull(paramMap.get("pkCgops"))) {
			errMsg = "?????????????????????(pkCgops)";
			return errMsg;
		}
		Set<String> pkCgops = new HashSet<String>((List<String>) paramMap.get("pkCgops"));

		int cnt =  DataBaseHelper.queryForScalar("select count(1) from bl_op_dt where flag_settle='1' and pk_cgop in (" 
					+ CommonUtils.convertSetToSqlInPart(pkCgops, "pk_cgop") + ") and pk_pv = ? ", Integer.class,paramMap.get("pkPv"));
		if (cnt > 0) {
			errMsg = "?????????????????????????????????";
			return errMsg;
		}
		return errMsg;
	}

	/**
	 * ????????????????????????
	 */
	private InsQgybVisit getPvVist(Map<String, Object> paramMap, IUser user) {
		//Map<String, Object> paramMap = JsonUtil.readValue(params, Map.class);
		String pkPv = CommonUtils.getPropValueStr(paramMap, "pkPv");
		if (CommonUtils.isNull(pkPv)) {
			throw new BusException("???????????????????????????pkPv");
		}
		String medType = "11";


		//????????????????????????????????????????????????
		PvEncounter pvencounter = DataBaseHelper.queryForBean("select * from pv_encounter where pk_pv=? and eu_pvtype in (1,2,4) and del_flag='0' ",
				PvEncounter.class, pkPv);
		BdHp hp = DataBaseHelper.queryForBean("select * from bd_hp where pk_hp=? and del_flag='0' ",
				BdHp.class, pvencounter.getPkInsu());
		
		// ????????????????????????
		Map<String, Object> piVisit = zsrmQGMapper.qryInsVisit(pkPv,hp.getCode());
		
		//?????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????
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
		
		if ((piVisit == null || piVisit.size() == 0) || flagReg)// ????????????
		{
			// ????????????????????????
			Map<String, Object> paraInput = new HashMap<String, Object>();
			paraInput = zsrmQGMapper.qryInsBasePre(pkPv);
			InsPiInfoVo insPi =getPersonInfo(ApplicationUtils.beanToJson(paraInput), user);

			if (insPi != null) {
				/*
				if(!pvencounter.getNamePi().equals(insPi.getBaseinfo().getPsnName())){
					throw new BusException("????????????(" + pvencounter.getNamePi()+") ????????????????????????("+insPi.getBaseinfo().getPsnName()+") ?????????");
				}
				*/
				
				InsRegParam insRegParam = new InsRegParam();
				insRegParam.setPsnNo(insPi.getBaseinfo().getPsnNo());
				insRegParam.setInsutype(hp.getCode());
				insRegParam.setMdtrtCertType("02");
				insRegParam.setMdtrtCertNo(insPi.getBaseinfo().getCertno());
				insRegParam.setPkPv(pkPv);

				// ??????
				Map<String, Object> retRegMap = OutPatientRegister(ApplicationUtils.beanToJson(insRegParam), user);
				// ??????????????????
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
				
				// ????????????????????????
				DefaultTransactionDefinition def = new DefaultTransactionDefinition();
				def.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRES_NEW);
				TransactionStatus status = platformTransactionManager.getTransaction(def);
				
				try {
					DataBaseHelper.insertBean(visit);
					platformTransactionManager.commit(status); // ????????????
				} catch (Exception e) {
					platformTransactionManager.rollback(status); // ???????????? ???????????????
					e.printStackTrace();
					throw new BusException("???????????????????????????????????????" + e);
				}
			} else {
				throw new BusException("??????????????????");
			}
		}
		
		// ?????????????????????
		InsQgybVisit visit = qryVisitInfo(pkPv,hp.getCode());
		//?????????????????????????????????????????????
		//validaMedType(visit.getPkPv());
		
		Map<String, Object> upPv = new HashMap<>();
		List<Map<String, Object>> pvDiags = zsrmQGMapper.qryPvDiag(pkPv);// ??????????????????

		// piVisit = zsrmQGMapper.qryInsVisit(pkPv) ;//?????????????????????????????????????????????????????????????????????
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
			throw new BusException("?????????2203A??????????????????????????????" + CommonUtils.getString(upPv.get("err_msg")));
		}
		// ???????????????????????????????????????
		//visit.setFlagInsu("1");
		//DataBaseHelper.updateBeanByPk(visit);

		return visit;
	}

	/**
	 * @param 015001013004
	 *            ????????????
	 */
	public InsQgybSt mzHpJiaokuan(String params, IUser user) {
		Map<String, Object> paramMap = JsonUtil.readValue(params, Map.class);

		// ????????????
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
				Log.error("????????????????????????????????????id");
			}
			
			//??????????????????????????????
			InsQgybVisit visit = qryVisitInfo(pkPv,outParamHuaJia.getInsutype());
			
			// ????????????????????????
			InsQgybSt insQgybSt = new InsQgybSt();
			ApplicationUtils.copyProperties(insQgybSt, outParamHuaJia);
			insQgybSt.setPkPv(pkPv);
			insQgybSt.setPkPi(visit.getPkPi());
			insQgybSt.setPkHp(visit.getPkHp());
			insQgybSt.setPkVisit(visit.getPkVisit());
			insQgybSt.setDateSt(new Date());

			insQgybSt.setAmount(outParamHuaJia.getMedfeeSumamt());// ?????????
			// insQgybSt.setAmtGrzhzf(outParamHuaJia.getAcctPay());// ????????????
			insQgybSt.setAmtGrzf(outParamHuaJia.getPsnCashPay());// ??????
			insQgybSt.setAmtJjzf(outParamHuaJia.getFundPaySumamt());// ??????
			insQgybSt.setYbPksettle(outParamHuaJia.getSetlId());
			insertInsSt(insQgybSt);

			// ????????????????????????????????????
			List<Map<String, Object>> setldetail = (List<Map<String, Object>>) retMap.get("setldetail");
			List<InsQgybStDt> insStDts = JsonUtil.readValue(JsonUtil.writeValueAsString(setldetail),
					new TypeReference<List<InsQgybStDt>>() {
					});
			for (InsQgybStDt insDt : insStDts) {

				ApplicationUtils.setDefaultValue(insDt, true);
				insDt.setPkInsst(insQgybSt.getPkInsst());
			}

			DataBaseHelper.batchUpdate(DataBaseHelper.getInsertSql(InsQgybStDt.class), insStDts);
			
			//?????????????????? 5204
			searchStChargeDtls(JsonUtil.writeValueAsString(insQgybSt),user);
			return insQgybSt;


		}else{
			throw new BusException("?????????2207????????????????????? ???"+CommonUtils.getString(retMap.get("err_msg")));
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
	        //???????????????ins_qgyb_st_cg
	        List<InsQgybStCg> stCgList = JsonUtil.readValue(
	                JsonUtil.getJsonNode(retStr, "output"),
	                new TypeReference<List<InsQgybStCg>>() {
	                });

	        if(stCgList!=null && stCgList.size()>0){
	            //??????????????????
	            InsQgybSt stInfo = zsrmQGMapper.qryInsuStInfo(paramMap);

	            stCgList.stream().forEach(stVo->{ 
	                ApplicationUtils.setDefaultValue(stVo,true);
	                stVo.setPkInsst(stInfo.getPkInsst());
	                stVo.setPkPi(stInfo.getPkPi());
	                stVo.setPkPv(stInfo.getPkPv());
	            });

	            //??????????????????????????????????????????????????????
	            DataBaseHelper.execute("delete from ins_qgyb_st_cg where pk_insst = ?",new Object[]{paramMap.get("pkInsst")});

	            //??????
	            DataBaseHelper.batchUpdate(DataBaseHelper.getInsertSql(InsQgybStCg.class),stCgList);
	        }

	}
	

	/**
	 * 015001013005
	 * @param
	 * ????????????
	 */
	public Map<String, Object> mzHpSetttleCancel(String params, IUser user) {
		Map<String, Object> paramMap = JsonUtil.readValue(params, Map.class);

		if (CommonUtils.isNull(paramMap.get("pkPv")))// his???????????? ????????????
		{
			return insSettleCancle(params, user);
		} else {// ????????????
			if (CommonUtils.isNull(paramMap.get("pkSettle"))) {
				throw new BusException("?????????????????????");
			}


			InsQgybSt insQgybSt = DataBaseHelper.queryForBean("select * from ins_qgyb_st where pk_settle= ? ",
					InsQgybSt.class, paramMap.get("pkSettle"));
			
			InsQgybVisit visit = qryVisitInfo(CommonUtils.getString(paramMap.get("pkPv")),insQgybSt.getInsutype());
			

			Map<String, Object> retMap = new HashMap<String, Object>();
			if (insQgybSt != null) {
				retMap.put("ybPkSettle", insQgybSt.getYbPksettle());
				retMap=insSettleCancle(JsonUtil.writeValueAsString(retMap), user);//????????????

				Map<String, Object> reqMap = new HashMap<String, Object>();
				paramMap = (Map<String, Object>) paramMap.get("ybPreReturnInfo");
				paramMap=(Map<String, Object>) paramMap.get("yBPreIntoParam");
				//???????????????
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
							Log.error("????????????????????????????????????id");
						}
						InsQgybSt reInsQgybSt = new InsQgybSt();
						ApplicationUtils.copyProperties(reInsQgybSt, outParamHuaJia);
						reInsQgybSt.setPkPv(visit.getPkPv());
						reInsQgybSt.setPkPi(visit.getPkPi());
						reInsQgybSt.setPkHp(visit.getPkHp());
						reInsQgybSt.setPkVisit(visit.getPkVisit());
						reInsQgybSt.setDateSt(new Date());

						reInsQgybSt.setAmount(outParamHuaJia.getMedfeeSumamt());// ?????????
						//insQgybSt.setAmtGrzhzf(outParamHuaJia.getAcctPay());// ????????????
						reInsQgybSt.setAmtGrzf(outParamHuaJia.getPsnCashPay());// ??????
						reInsQgybSt.setAmtJjzf(outParamHuaJia.getFundPaySumamt());// ??????
						reInsQgybSt.setYbPksettle(outParamHuaJia.getSetlId());
						insertInsSt(reInsQgybSt);
						
						//?????????????????? 5204
						searchStChargeDtls(JsonUtil.writeValueAsString(reInsQgybSt),user);
						
						return (Map<String, Object>) ApplicationUtils.beanToMap(reInsQgybSt);


					}else{
						throw new BusException("?????????2207????????????????????? ???"+CommonUtils.getString(retMap.get("err_msg")));
					}
					
				}else{
					//retMap.put("ybPksettle", null);
					return null;
				}
				

				//return retMap;

			} else {
				return null;
				//throw new BusException("????????????" + paramMap.get("pkSettle") + " ??????????????????????????????");
			}
		}
	}

	/**
	 * 015001013006
	 * @param
	 * ???PkSettle?????????????????????INS_QGYB_ST???
	 */
	public void updatePkSettle(String params, IUser user) {
		Map<String, Object> paramMap = JsonUtil.readValue(params, Map.class);
		String pkPv = paramMap.get("pkPv").toString();

		String pkSettle = null;
		String pkSettleCanc = null;
		String ywlx = null;// ????????????
		String setlId = null;// ??????????????????

		// ????????????
		if (paramMap.containsKey("pkSettle") && paramMap.get("pkSettle") != null) {
			pkSettle = CommonUtils.getString(paramMap.get("pkSettle"));
		}
		// ??????????????????
		if (paramMap.containsKey("pkSettleCanc") && paramMap.get("pkSettleCanc") != null) {
			pkSettleCanc = CommonUtils.getString(paramMap.get("pkSettleCanc"));
		}
		if (paramMap.containsKey("yWLX") && paramMap.get("yWLX") != null) {
			ywlx = CommonUtils.getString(paramMap.get("yWLX"));
		}
		// ????????????ID
		if (paramMap.containsKey("pkPtmzjs") && paramMap.get("pkPtmzjs") != null) {
			setlId = CommonUtils.getString(paramMap.get("pkPtmzjs"));// jsId
		}
		if ("1".equals(CommonUtils.getString(ywlx))) {// ????????????
			// ??????
			if (CommonUtils.isNotNull(pkSettle) && CommonUtils.isNotNull(setlId)) {
				DataBaseHelper.execute(
						"update ins_qgyb_st set pk_settle =?  where del_flag='0' and PK_PV=? and setl_id = ?", pkSettle,
						pkPv, setlId);
				return;
			}
			// ??????
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
	 *            ?????????????????????
	 */
	public Map<String, Object> PreReturnSettlement(String params, IUser user) {
		Map<String, Object> paramMap = JsonUtil.readValue(params, Map.class);

		// ????????????????????????
		InsQgybSt insSt = DataBaseHelper.queryForBean("select * from ins_qgyb_st where pk_settle=? ",
				InsQgybSt.class, paramMap.get("pkSettle"));
		
		// ?????????????????????
		List<BlOpDt> noCancleDt = noCancleDt(params);
		
		//?????????????????????????????????????????????
		if (insSt == null ) {
			Double dtAmt = 0d;
			
			//??????????????????????????????
			if (noCancleDt!=null && noCancleDt.size()>0) {
				Set<String> pkBlOpDt = new HashSet<String>();
				for (BlOpDt blOpDt : noCancleDt) {
					pkBlOpDt.add(blOpDt.getPkCgop());
				}

				Double amtAdd = 0d;//??????????????????
				Double amtSpec = 0d;//????????????
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
		// ????????????????????????
		InsQgybVisit visit = qryVisitInfo(CommonUtils.getString(paramMap.get("pkPv")),insSt.getInsutype());
		if (visit == null) {
			throw new BusException("???????????????????????????????????????????????????????????????");
		}

		// ?????????????????????
		if (noCancleDt != null && noCancleDt.size() > 0) {
			//validaMedType(visit.getPkPv());
			Map<String, Object> rePayIns = rePayIns(visit, noCancleDt, insSt, user, params);
			if (rePayIns != null) {
				return rePayIns;
			} else {
				throw new BusException("???????????????????????????????????????????????????");
			}
		}
		

		Map<String, Object> stMap = zsrmQGMapper.qryBlSt(paramMap);

		if (stMap != null) {
			//??????????????????????????????????????????????????????0
			stMap.put("aggregate_amount", 0);
			stMap.put("patients_pay", 0);
			stMap.put("medicare_payments", 0);
			return stMap;
		} else {
			throw new BusException("?????????????????????????????????????????????????????????");
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

		// ??????????????????
		String chrgBchno = ApplicationUtils.getCode("0608");// ???????????????
		reqMap.put("chrgBchno", chrgBchno);
		reqMap.put("rePay", "1");
		reqMap.put("pkCgops", pkCgs);
		reqMap.put("pkPv", pkPv);
		reqMap.put("insutype", visit.getInsutype());
		List<Map<String, Object>> dts = zsrmQGMapper.qryChargeDetailNoUpload(reqMap);

		Double amtAdd = 0d;//??????????????????
		Double amtSpec = 0d;//????????????
		//??????????????????????????????=??????*????????????
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
		//??????????????????
		retMap.clear();
		retMap.put("pkPv", visit.getPkPv());
		retMap.put("insutype", visit.getInsutype());
		mzBillDel(JsonUtil.writeValueAsString(reqMap), user);
		
		//????????????????????????
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
			throw new BusException("??????????????????????????????????????????" + CommonUtils.getString(retMap.get("err_msg")));
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
		// ????????????????????????
		DefaultTransactionDefinition def = new DefaultTransactionDefinition();
		def.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRES_NEW);
		TransactionStatus status = platformTransactionManager.getTransaction(def);

		try {
			DataBaseHelper.batchUpdate(DataBaseHelper.getInsertSql(InsQgybCg.class), cgs);

			platformTransactionManager.commit(status); // ????????????
		} catch (Exception e) {
			platformTransactionManager.rollback(status); // ???????????? ???????????????
			e.printStackTrace();
			throw new BusException("??????????????????????????????????????????" + e);
		}

		// ???????????????
		retMap.clear();
		reqMap.clear();
		retStr = "";
		reqMap.put("port", "2206");
		reqMap.put("psn_no", visit.getPsnNo());
		reqMap.put("mdtrt_cert_type", visit.getMdtrtCertType());
		reqMap.put("mdtrt_cert_no", visit.getMdtrtCertNo());
		reqMap.put("med_type", insSt.getMedType());//??????????????????????????????????????????????????????????????????????????????
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

			insQgybSt.setAmount(outParamHuaJia.getMedfeeSumamt());// ?????????
			insQgybSt.setAmtGrzhzf(outParamHuaJia.getAcctPay());// ????????????
			insQgybSt.setAmtGrzf(outParamHuaJia.getPsnCashPay());// ??????
			insQgybSt.setAmtJjzf(outParamHuaJia.getFundPaySumamt());// ??????

			outParamHuaJia.setAmount(outParamHuaJia.getMedfeeSumamt());// ?????????
			outParamHuaJia.setAmtGrzf(outParamHuaJia.getPsnCashPay());// ??????
			outParamHuaJia.setAmtJjzf(outParamHuaJia.getFundPaySumamt());// ??????

			preSt.setPreIntoParam(preParam); // ?????????????????????
			preSt.setyBPreIntoParam(reqMap);// ??????????????? ????????????
			preSt.setYbPreSettleInfo(outParamHuaJia);// ??????????????? ????????????

			//preSt.setAggregateAmount(preSt.getAggregateAmount()+outParamHuaJia.getMedfeeSumamt());
			preSt.setPatientsPay(preSt.getPatientsPay()+outParamHuaJia.getPsnCashPay());
			preSt.setMedicarePayments(outParamHuaJia.getFundPaySumamt());
			preSt.setAggregateAmount(preSt.getPatientsPay()+preSt.getMedicarePayments());
			return (Map<String, Object>) ApplicationUtils.beanToMap(preSt);

		} else {
			logger.error(retStr);
			// ????????????????????????????????????
			//reqMap.clear();
			//reqMap.put("pkPv", visit.getPkPv());
			//mzBillDel(JsonUtil.writeValueAsString(reqMap), user);
			throw new BusException("????????????????????????" + CommonUtils.getString(retMap.get("err_msg")));
		}

	}
	/**
	 * ????????????
	 * ybPksettle=setl_id
	 */
	public Map<String, Object> insSettleCancle(String params, IUser user) {
		Map<String, Object> paramMap = JsonUtil.readValue(params, Map.class);

		if (CommonUtils.isNotNull(paramMap.get("ybPkSettle")))// his???????????? ????????????
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

					insStCancel.setAmount(insStCancel.getMedfeeSumamt());// ?????????
					//insStCancel.setAmtGrzhzf(insStCancel.getAcctPay());// ????????????
					insStCancel.setAmtJjzf(insStCancel.getFundPaySumamt());// ??????
					insStCancel.setAmtGrzf(insStCancel.getPsnCashPay());// ??????

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
				}else if(CommonUtils.getString(retMap.get("err_msg")).contains("??????????????????????????????????????????")){
					retMap.clear();
					InsQgybSt insSt = DataBaseHelper.queryForBean(
							"select * from ins_qgyb_st where SETL_ID_CANCEL=? and del_flag='0' ", InsQgybSt.class, insQgybSt.getSetlId());
					retMap.put("ybPksettle", paramMap.get("ybPkSettle"));
					retMap.put("amtJjzf", insSt.getFundPaySumamt());
					return retMap;
				}
				else {
					
					Log.error("?????????2208?????????????????????" + CommonUtils.getString(retMap.get("err_msg")));
					throw new BusException("?????????2208?????????????????????" + CommonUtils.getString(retMap.get("err_msg")));
				}
			}else{
				Log.error("??????????????????,??????ID???????????????????????????" + params);
				throw new BusException("???????????????????????????ID????????????????????????"+paramMap.get("ybPksettle"));
			}
			
		} else {
			Log.error("??????????????????,?????????" + params);
			throw new BusException("???????????????????????????????????????ID");
		}
		
	}
	private List<BlOpDt> noCancleDt(String params) {
		Map<String, Object> paramMap = JsonUtil.readValue(params, Map.class);

		// ????????????????????????????????????
		List<BlOpDt> allList = DataBaseHelper.queryForList(
				"select * from bl_op_dt where pk_pv=? and pk_settle =? and del_flag='0' ", BlOpDt.class,
				paramMap.get("pkPv"), paramMap.get("pkSettle"));

		JsonNode jsonnode = JsonUtil.getJsonNode(params, "returnPkcgop");
		if (jsonnode == null) {
			return null;
			//throw new BusException("???????????????????????????????????????????????????????????????");
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
					
					opdt.setQuan(opdt.getQuan()-oldDt.getQuanBack());//bl_op_dt??????????????????
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
	 * ????????????????????? 
	 */
	private void insertInsSt(InsQgybSt insSt) {
		DefaultTransactionDefinition def = new DefaultTransactionDefinition();
		def.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRES_NEW);
		TransactionStatus status = platformTransactionManager.getTransaction(def);
		try {
			DataBaseHelper.execute("update INS_QGYB_ST set del_flag='1' where pk_pv=? and setl_id =?", insSt.getPkPv(),insSt.getSetlId());
			DataBaseHelper.insertBean(insSt);
			platformTransactionManager.commit(status); // ????????????
		} catch (Exception e) {
			platformTransactionManager.rollback(status); // ???????????? ???????????????
			e.printStackTrace();
			throw new BusException("?????????????????????????????????????????????" + e);
		}
	}

	/**
	 *???????????????????????????
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
				throw new BusException("?????????2503?????????????????????????????????????????????????????????");
			}

		} else {
			throw new BusException("?????????2503???????????????????????????" + CommonUtils.getString(retMap.get("err_msg")));
		}

	}
	/**
	 *???????????????????????????
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
				throw new BusException("?????????2503???????????????????????????" + CommonUtils.getString(retMap.get("err_msg")));
			}
		} else {
			throw new BusException("?????????2503??????????????????????????????????????? ???????????????????????????trt_dcla_detl_sn");
		}
	}
	/**
	 *???????????????????????????
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
				throw new BusException("?????????5301???????????????????????????????????????" + CommonUtils.getString(retMap.get("err_msg")));
			}
		}else{
			throw new BusException("?????????5301???????????????????????????????????????????????????????????????");
		}
	}
	/**
	 *????????????????????????
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
				throw new BusException("?????????5302????????????????????????????????????" + CommonUtils.getString(retMap.get("err_msg")));
			}
		}else{
			throw new BusException("?????????5302????????????????????????????????????????????????????????????");
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
		logger.info("???????????????" + rs);
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
		/* ?????????????????? */
		case "1101":
			jsonStr = getJsonStr(paramMap, "data", port, user);
			break;
		/* ???????????? */
		case "1901":
			jsonStr = getJsonStr(paramMap, "data", port, user);
			break;
		/* ?????? */
		case "9001":
			jsonStr = getJsonStr(paramMap, "signIn", port, user);
			break;
		/* ?????? */
		case "9002":
			jsonStr = getJsonStr(paramMap, "signOut", port, user);
			break;
		/* ???????????? */
		case "2201":
			jsonStr = getJsonStr(paramMap, "data", port, user);
			break;
		/* ?????????????????? */
		case "2202":
			jsonStr = getJsonStr(paramMap, "data", port, user);
			break;
		/* ???????????????????????? */
		case "2203A":
			JSONObject jsonObject = JSONObject.fromObject(paramMap);
			jsonStr = getJsonStr(jsonObject, port, user);
			break;
		/* ?????????????????????????????? */
		case "2204":
			jsonStr = getJsonStr(paramMap.get("params"), "feedetail", port, user);
			break;
		/* ?????????????????????????????? */
		case "2205":
			jsonStr = getJsonStr(paramMap, "data", port, user);
			break;
		/* ??????????????? */
		case "2206":
			jsonStr = getJsonStr(paramMap, "data", port, user);
			break;
		/* ???????????? */
		case "2207":
			jsonStr = getJsonStr(paramMap, "data", port, user);
			break;
		/* ?????????????????? */
		case "2208":
			jsonStr = getJsonStr(paramMap, "data", port, user);
			break;
		/* ???????????? */
		case "2001":
			jsonStr = getJsonStr(paramMap, "data", port, user);
			break;
		/* ???????????????????????? */
		case "5206":
			jsonStr = getJsonStr(paramMap, "data", port, user);
			break;
		/* ?????????????????? */
		case "3401":
			jsonStr = getJsonStr(paramMap, "deptinfo", port, user);
			break;
		/* ?????????????????? */
		case "3402":
			jsonStr = getJsonStr(paramMap, "deptinfo", port, user);
			break;
		/* ?????????????????? */
		case "3403":
			jsonStr = getJsonStr(paramMap, "data", port, user);
			break;
		/* ???????????? */
		case "3201":
			jsonStr = getJsonStr(paramMap, "data", port, user);
			break;
		/* ???????????????????????????????????? */
		case "3202":
			jsonStr = getJsonStr(paramMap, "data", port, user);
			break;
		/* ?????????????????? */
		case "3301":
			//jsonStr = getJsonStr(paramMap, "catalogcompin", port, user);
			jsonStr = getJsonStr(paramMap.get("params"), "data", port, user);
			break;
		/* ?????????????????? */
		case "3302":
			jsonStr = getJsonStr(paramMap, "catalogcompin", port, user);
			break;
		/* ???????????? */
		case "2601":
			jsonStr = getJsonStr(paramMap, "data", port, user);
			break;
		/* ????????????????????????????????????????????? */
		case "1317":
			jsonStr = getJsonStr(paramMap, "data", port, user);
			break;
		case "9101":
			jsonStr = getJsonStr(paramMap, "fsUploadIn", port, user);
			break;
		/* ??????????????? */	
		case "2503":
			jsonStr = getJsonStr(paramMap, "data", port, user);
			break;	
		/* ????????????????????? */	
		case "2504":
			jsonStr = getJsonStr(paramMap, "data", port, user);
			break;
		/* ????????????????????? */
		case "5301":
			jsonStr = getJsonStr(paramMap, "data", port, user);
			break;
		/* ???????????????????????? */
		case "5302":
			jsonStr = getJsonStr(paramMap, "data", port, user);
			break;
		/* ?????????????????? */
		case "90100":
			jsonStr = getJsonStr(paramMap, "data", port, user);
			break;
		case "4101":
			JSONObject jsonObject1 = JSONObject.fromObject(paramMap.get("param"));
			jsonStr = getJsonStr(jsonObject1, port, user);
			break;
		/* ?????????????????? */
		case "5204":
			jsonStr = getJsonStr(paramMap, "data", port, user);
			break;
		default:
			break;
		}
		User userInfo = (User) user;
		logger.info(port + "???????????????" + jsonStr);
		
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
			logger.info(port + "???????????????" + rs);
			JSONObject obj = JSONObject.fromObject(rs);
			obj.put("msgid", JSONObject.fromObject(jsonStr).getString("msgid"));
			rs = obj.toString();
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("?????????????????????????????????port " + port);
			throw new BusException("??????????????????" + port + e.getMessage());
		}

		return rs;
	}
	
	private String getJsonStr(Map<String, Object> paramMap, String key, String interfaceNO, IUser user) {
		//String d = DateUtils.dateToStr("yyyy-MM-dd HH:mm:ss", new Date());
		User userInfo = (User) user;
		
		//userInfo.setCodeEmp("1111");
		//userInfo.setNameEmp("??????");

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
		/* ??????????????? 1-????????????2-???????????????3-???????????? */
		jsonObject.put("opter_type", "1");
		jsonObject.put("opter_name", userInfo.getNameEmp());
		jsonObject.put("opter", userInfo.getCodeEmp());
		jsonObject.put("insuplc_admdvs", "442000");
		jsonObject.put("mdtrtarea_admvs", "442000");
		jsonObject.put("recer_admvs", "442000");/* ??????????????????????????? */
		jsonObject.put("infver", VERSION);
		jsonObject.put("infno", interfaceNO);
		jsonObject.put("sign_no", "");
		jsonObject.put("fixmedins_name", FIXMEDINS_NAME);
		jsonObject.put("fixmedins_code", FIXMEDINS_CODE);

		return jsonObject.toString();
	}

	private String getJsonStr(JSONObject paramObj, String interfaceNO, IUser user) {
		//String d = DateUtils.dateToStr("yyyy-MM-dd??HH:mm:ss", new Date());
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
		/* ??????????????? 1-????????????2-???????????????3-???????????? */
		jsonObject.put("opter_type", "1");
		jsonObject.put("opter_name", userInfo.getNameEmp());
		jsonObject.put("opter", userInfo.getCodeEmp());
		jsonObject.put("insuplc_admdvs", "442000");
		jsonObject.put("mdtrtarea_admvs", "442000");
		jsonObject.put("recer_admvs", "442000");/* ??????????????????????????? */
		jsonObject.put("infver", VERSION);
		jsonObject.put("infno", interfaceNO);
		jsonObject.put("sign_no", "");
		jsonObject.put("fixmedins_name", FIXMEDINS_NAME);
		jsonObject.put("fixmedins_code", FIXMEDINS_CODE);

		return jsonObject.toString();
	}

	private String getJsonStr(Object param, String key, String interfaceNO, IUser user) {
		//String d = DateUtils.dateToStr("yyyy-MM-dd??HH:mm:ss", new Date());
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
		/* ??????????????? 1-????????????2-???????????????3-???????????? */
		jsonObject.put("opter_type", "1");
		jsonObject.put("opter_name", userInfo.getNameEmp());
		jsonObject.put("opter", userInfo.getCodeEmp());
		jsonObject.put("insuplc_admdvs", "442000");
		jsonObject.put("mdtrtarea_admvs", "442000");
		jsonObject.put("recer_admvs", "442000");/* ??????????????????????????? */
		jsonObject.put("infver", VERSION);
		jsonObject.put("infno", interfaceNO);
		jsonObject.put("sign_no", "");
		jsonObject.put("fixmedins_name", FIXMEDINS_NAME);
		jsonObject.put("fixmedins_code", FIXMEDINS_CODE);

		return jsonObject.toString();
	}

	private String getJsonStr(Object object, String interfaceNO, IUser user) {
		//String d = DateUtils.dateToStr("yyyy-MM-dd??HH:mm:ss", new Date());
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
		/* ??????????????? 1-????????????2-???????????????3-???????????? */
		map.put("opter_type", "1");
		map.put("opter_name", userInfo.getNameEmp());
		map.put("opter", userInfo.getCodeEmp());
		map.put("insuplc_admdvs", "442000");
		map.put("mdtrtarea_admvs", "442000");
		map.put("recer_admvs", "442000");/* ??????????????????????????? */
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
	 * ??????HIS????????????????????????
	 * ????????????015001013014
	 * @param param
	 */
	public PagingVo getHisPd(String param, IUser user){
		Map<String,Object> paraMap = JsonUtil.readValue(param, Map.class);
		PagingVo pagingVo = new PagingVo();
		// ????????????
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
	 * ??????HIS??????????????????????????????
	 * ????????????015001013015
	 * @param param
	 */
	public PagingVo getHisItem(String param, IUser user){
		Map<String,Object> paraMap = JsonUtil.readValue(param, Map.class);
		PagingVo pagingVo = new PagingVo();
		// ????????????
		int pageIndex = CommonUtils.getInteger(MapUtils.getString(paraMap,"pageIndex"));
		int pageSize = CommonUtils.getInteger(MapUtils.getString(paraMap,"pageSize"));
		MyBatisPage.startPage(pageIndex,pageSize);
		pagingVo.setItemList(zsrmQGMapper.qryHisItemIns(paraMap));
		Page<List<MedicalCharges>> page = MyBatisPage.getPage();
		pagingVo.setTotalCount(page.getTotalCount());
		return pagingVo;
	}

    /**
     * ????????????????????????
	 * ????????????015001013016
     * @param param
     */
	public PagingVo getCatalog(String param, IUser user){
		Map<String,Object> paraMap = JsonUtil.readValue(param, Map.class);
		PagingVo pagingVo = new PagingVo();
		// ????????????
		int pageIndex = CommonUtils.getInteger(MapUtils.getString(paraMap,"pageIndex"));
		int pageSize = CommonUtils.getInteger(MapUtils.getString(paraMap,"pageSize"));
		MyBatisPage.startPage(pageIndex,pageSize);
		pagingVo.setInsur(zsrmQGMapper.qryInsQgybItem(paraMap));
		Page<List<MedicalCharges>> page = MyBatisPage.getPage();
		pagingVo.setTotalCount(page.getTotalCount());
		return pagingVo;
	}

	/**
	 * ??????????????????
	 * ????????????015001013017
	 * @param param
	 */
	public PagingVo getcontrast(String param, IUser user){
		Map<String,Object> paraMap = JsonUtil.readValue(param, Map.class);
		PagingVo pagingVo = new PagingVo();
		// ????????????
		int pageIndex = CommonUtils.getInteger(MapUtils.getString(paraMap,"pageIndex"));
		int pageSize = CommonUtils.getInteger(MapUtils.getString(paraMap,"pageSize"));
		MyBatisPage.startPage(pageIndex,pageSize);
		String type=MapUtils.getString(paraMap,"projectType");
		if(StringUtils.isNotBlank(type))
		{
			if("1".equals(type))
			{
				pagingVo.setItemList(zsrmQGMapper.qryInsQgybItemmapPd(paraMap));
			}else if("3".equals(type))//??????
			{
				pagingVo.setItemList(zsrmQGMapper.qryInsQgybItemmapItemHC(paraMap));
			}else//??????
			{
				pagingVo.setItemList(zsrmQGMapper.qryInsQgybItemmapItemXM(paraMap));
			}
		}
		Page<List<MedicalCharges>> page = MyBatisPage.getPage();
		pagingVo.setTotalCount(page.getTotalCount());
		return pagingVo;
	}

	/**
	 * ??????????????????
	 * ????????????015001013018
	 * @param param
	 */
	public void saveContrast(String param, IUser user){
		List<InsQgybItemMap> insItemlist = JsonUtil.readValue(param,new TypeReference<List<InsQgybItemMap>>(){});
		for(InsQgybItemMap insItemmap:insItemlist){
			Map<String,Object> paraMap = new HashMap<String,Object>();
			paraMap.put("codeHosp",insItemmap.getCodeHosp());
			List<Map<String, Object>> list = zsrmQGMapper.queryHisContrast(paraMap);
			if (list.size()>0){
				throw new BusException("??????????????????????????????????????????");
			}
			insItemmap.setPkItemmap(NHISUUID.getKeyId());
			//????????????(list_type):101:??????;102:?????????;103:????????????;104:?????????;201:????????????;301:????????????;105:?????????
			if("Medical".equals(insItemmap.getHisType())){//????????????1????????????0?????????
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
	 * ??????????????????
	 * ????????????015001013019
	 * @param param
	 * @param user
	 */
	public void deleteContrast(String param, IUser user){
		Map<String,Object> paraMap = JsonUtil.readValue(param, Map.class);
		if(StringUtils.isEmpty(MapUtils.getString(paraMap,"pkItemmap"))){
			throw new BusException("????????????????????????");
		}
		zsrmQGMapper.delInsQgybItemmap(paraMap);
	}

	/**
	 * ??????????????????????????????
	 *  ????????????015001013020
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
	 * ????????????????????????
	 * ????????????015001013021
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
	 * ?????????????????????????????????????????????
	 * ????????????015001013022
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
     * ??????????????????????????????????????????
     * @param param
     * @param user
     */
    public List<Map<String, Object>> getCheckInsuranceSum(String param, IUser user){
        Map<String, Object> mapParam = JsonUtil.readValue(param,Map.class);
        List<Map<String, Object>> insuraList = zsrmQGMapper.qryQGYBMedicalInsuranceSum(mapParam);
        if(insuraList.size() >0){
			for(Map<String, Object> map:insuraList){
				Map<String, Object> insurMap = new HashMap<>();
				insurMap.put("port","3201");//?????????????????????????????????????????????
				insurMap.put("insutype",MapUtils.getString(map,"insutype"));//??????
				insurMap.put("clr_type",MapUtils.getString(map,"clrType"));//????????????
				insurMap.put("setl_optins","442000");//??????????????????
				insurMap.put("stmt_begndate",MapUtils.getString(mapParam,"deBegn"));//??????????????????
				insurMap.put("stmt_enddate",MapUtils.getString(mapParam,"deEnd"));//??????????????????
				insurMap.put("medfee_sumamt",MapUtils.getString(map,"amountSt"));//???????????????
				insurMap.put("fund_pay_sumamt",MapUtils.getString(map,"amountInsu"));//??????????????????
				insurMap.put("acct_pay",MapUtils.getString(map,"amountPi"));//????????????????????????
				insurMap.put("fixmedins_setl_cnt",MapUtils.getString(map,"dataNum"));//??????????????????????????????
				String respon = getHealthInsuranceEntry(insurMap, user);
				if(StringUtils.isNotEmpty(respon)){
					com.alibaba.fastjson.JSONObject jsonObject = com.alibaba.fastjson.JSONObject.parseObject(respon);
					if ("0".equals(jsonObject.getString("infcode"))) {
						Map<String, Object> reqsMap = JsonUtil.readValue(jsonObject.getJSONObject("output").getString("stmtinfo"), new TypeReference<Map<String,Object>>() {});
                        map.put("stmtRsltDscr",MapUtils.getString(reqsMap,"stmt_rslt_dscr"));
					}else{
						map.put("stmtRsltDscr","???????????????" + jsonObject.getString("err_msg"));
					}
				}
			}
		}
        return insuraList;
    }

	/**
	 * ??????????????????????????????
	 * @param param
	 * @param user
	 */
	public void uploadCostDetailed(String param, IUser user){
		Map<String, Object> mapParam = JsonUtil.readValue(param,Map.class);
		//?????????????????????????????????????????????????????????????????????????????????
		List<Map<String, Object>> insuraDetailedList = zsrmQGMapper.qryQGYBMedicalInsuranceDetailed(mapParam);
		//???????????????????????????
		String uploadfileQuryNo;
		//????????????????????????????????????
		String respUpl = uploadDetailedFile(insuraDetailedList,user);
		if(StringUtils.isNotEmpty(respUpl)){
			com.alibaba.fastjson.JSONObject jsonObject = com.alibaba.fastjson.JSONObject.parseObject(respUpl);
			if ("0".equals(jsonObject.getString("infcode"))) {
				Map<String, Object> reqsMap = JsonUtil.readValue(jsonObject.getString("output"), new TypeReference<Map<String,Object>>() {});
				uploadfileQuryNo = MapUtils.getString(reqsMap,"file_qury_no");
			}else{
				throw new BusException("???????????????" + jsonObject.getString("err_msg"));
			}
		}else{
			throw new BusException("????????????????????????????????????????????????!!!");
		}

        if(StringUtils.isNotEmpty(uploadfileQuryNo)){
        	////??????????????????????????????????????????????????????????????????????????????
			List<Map<String, Object>> insuraSumList = zsrmQGMapper.qryQGYBMedicalInsuranceSum(mapParam);
			if(insuraSumList.size()>0){
				Map<String, Object> insurMap = new HashMap<>();
				insurMap.put("port","3202");//?????????????????????????????????????????????
				insurMap.put("setl_optins","442000");//??????????????????
				insurMap.put("file_qury_no",uploadfileQuryNo);//???????????????
				insurMap.put("stmt_begndate",MapUtils.getString(mapParam,"deBegn"));//??????????????????
				insurMap.put("stmt_enddate",MapUtils.getString(mapParam,"deEnd"));//??????????????????
				insurMap.put("medfee_sumamt",MapUtils.getString(insuraSumList.get(0),"amountSt"));//???????????????
				insurMap.put("fund_pay_sumamt",MapUtils.getString(insuraSumList.get(0),"amountInsu"));//??????????????????
				insurMap.put("cash_payamt",MapUtils.getString(insuraSumList.get(0),"amountPi"));//??????????????????
				insurMap.put("fixmedins_setl_cnt",MapUtils.getString(insuraSumList.get(0),"dataNum"));//??????????????????????????????
				String respon = getHealthInsuranceEntry(insurMap, user);
				if(StringUtils.isNotEmpty(respon)){
					com.alibaba.fastjson.JSONObject jsonObject = com.alibaba.fastjson.JSONObject.parseObject(respon);
					if ("0".equals(jsonObject.getString("infcode"))) {
						Map<String, Object> reqsMap = JsonUtil.readValue(jsonObject.getJSONObject("output").getString("fileinfo"), new TypeReference<Map<String,Object>>() {});
					}else{
					}
				}
			}else{
				throw new BusException("????????????????????????????????????");
			}

		}else{
			throw new BusException("?????????????????????????????????????????????");
		}
	}

	/**
	 * ?????????????????????????????????????????????
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
		//?????????????????????????????????
		File file = new File(UploadFilePath+fileName+".zip");
		// ??????????????????????????????????????????????????????
		if(!file.getParentFile().exists()){
			file.getParentFile().mkdir();
		}
		try {
			//????????????
			file.createNewFile();
			//?????????
			FileOutputStream fos = new FileOutputStream(file);
			//zip?????????
			ZipOutputStream zos = new ZipOutputStream(fos);
			//???????????????????????????(?????????.txt????????????????????????????????????)
			String name = new String((fileName).getBytes("UTF-8"))+ ".txt";
			//??????ZIP??????????????????????????????
			ZipEntry zipEntry = new ZipEntry(name);
			zos.putNextEntry(zipEntry);
			for(int i=0;i<paramList.size();i++){
				//?????????????????????????????????????????????
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
			//????????????????????????
			zos.flush();
			zos.close();
			fos.close();
			Map<String, Object> insurMap = new HashMap<>();
			insurMap.put("port","9101");//????????????
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
	//???????????????????????????
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
	

	/* ??????0607?????????????????????????????? */
	private String getSNcode() {
		String SNcode = ApplicationUtils.getCode("0607");
		if (SNcode != null && SNcode.length() >= 4) {
			SNcode = SNcode.substring(SNcode.length() - 4, SNcode.length());
			return SNcode;
		} else {
			throw new BusException("?????????????????????0607???????????????????????????????????????SNcode");
		}
	}
	/**
	 * ??????????????????
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
				throw new BusException("?????????90100????????????????????????????????????" + CommonUtils.getString(retMap.get("err_msg")));
			}
		}else{
			throw new BusException("?????????90100????????????????????????????????????????????????????????????");
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

		//??????????????????
		PvEncounter pvInfo = zsrmQGMapper.qryPvInfoByPkSettle(paramMap);
		//????????????????????????
		List<Map<String,Object>> insuPayList = new ArrayList<>();

		paramMap.put("pkpv",pvInfo.getPkPv());
		InsQgybSettleList insQgybSettleList = new InsQgybSettleList();

//		if(EnumerateParameter.THREE.equals(pvInfo.getEuPvtype())){
//			//??????????????????
//			insQgybSettleList.setSetlinfo(zsrmQGMapper.qryStInfo(paramMap).get(0)) ;
//
//			//??????????????????????????????
//			//insuPayList = zsrmQGMapper.qryInsuPayDtls(paramMap);
//
//			//????????????????????????
//			rtnMap.put("stDiagList",zsrmQGMapper.qryDiseinfoList(paramMap));
//
//			//????????????????????????
//			rtnMap.put("stChargeList",zsrmQGMapper.qryChargeDtls(paramMap));
//
//			//??????????????????
//			rtnMap.put("stOperTeList",zsrmQGMapper.qryIclDtls(paramMap));
//		}else{
//			//??????????????????
//			InsQgybSetInfo insQgybSetInfo=zsrmQGMapper.qryOpStInfo(paramMap).get(0);
//			insQgybSetInfo.setFixmedinsCode(FIXMEDINS_CODE);
//			insQgybSetInfo.setFixmedinsCode(FIXMEDINS_NAME);
//			insQgybSetInfo.setIptMedType("1");//?????????????????????1
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
//			//??????????????????????????????
//			insQgybSettleList.setPayinfo( zsrmQGMapper.qryInsuPayDtls(paramMap));;
//
//			//???????????????????????????????????????????????????opspdiseinfo???
//			insQgybSettleList.setOpspdiseinfo(zsrmQGMapper.qryoOpspDiseinfoList(paramMap));
//			//????????????????????????
//			insQgybSettleList.setDiseinfo(zsrmQGMapper.qryDiseinfoList(paramMap));
//
//			//????????????????????????
//			insQgybSettleList.setIteminfo(zsrmQGMapper.qryOpChargeDtls(paramMap));
//			//??????????????????
//			insQgybSettleList.setIcuinfo(new ArrayList<InsQgybIcuInfo>());
//
//			//?????????????????????????????????????????????
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
	 * ????????????????????????????????????
	 * @param params
	 * @param user
	 * @return
	 */
	public void upLoadSettleList(String params, IUser user) {
		User userInfo = (User) user;
//		??????????????????????????????????????????????????????
		Map paramMap = JsonUtil.readValue(params, Map.class);
//		????????????
		String pkPv = (String) paramMap.get("pkPv");
		Object beginTime = paramMap.get("beginTime");
		Object endTime = paramMap.get("endTime");
		if(StringUtils.isBlank(pkPv) && (beginTime == null || endTime == null)){
			throw new BusException("???????????????????????????");
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
			throw new BusException("??????????????????");
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
//		1?????????????????????,????????????:setlinfo
				InsQgybSetInfo insQgybSetInfo = new InsQgybSetInfo();
				ZsrmQGUtils.mapToBean(insQgybSetInfo, map);
//					???????????????????????????
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
//		2?????????????????????,???????????????payinfo
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
//						4???????????????????????????????????????diseinfo???
						InsQgybDiseinfo insQgybDiseinfo = new InsQgybDiseinfo();
						ZsrmQGUtils.mapToBean(insQgybDiseinfo,map);
						settleMap.get(setlId).getDiseinfo().add(insQgybDiseinfo);
					}else{
//						3????????????????????????????????????????????????opspdiseinfo???
						InsQgybOpspdiseinfo insQgybOpspdiseinfo = new InsQgybOpspdiseinfo();
						ZsrmQGUtils.mapToBean(insQgybOpspdiseinfo,map);
						settleMap.get(setlId).getOpspdiseinfo().add(insQgybOpspdiseinfo);
					}
				}
			}
//		5???????????????????????????????????????iteminfo???
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
//		6???????????????????????????????????????oprninfo???
			List<Map<String,Object>> oprnList = zsrmQGMapper.qryInsQgybOprnInfo(setIdList);
            if(!oprnList.isEmpty()){
				for (Map<String, Object> map : oprnList) {
					InsQgybOprnInfo insQgybOprnInfo = new InsQgybOprnInfo();
					ZsrmQGUtils.mapToBean(insQgybOprnInfo,map);
					String setlId = (String) map.get("setlId");
					settleMap.get(setlId).getOprninfo().add(insQgybOprnInfo);
				}
			}

//		7???????????????????????????????????????icuinfo???

//			???????????????????????????????????????
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
	 * ????????????????????????
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
				insStCancel.setAmount(insStCancel.getMedfeeSumamt());// ?????????
				insStCancel.setAmtGrzhzf(insStCancel.getAcctPay());// ????????????
				insStCancel.setAmtJjzf(insStCancel.getFundPaySumamt());// ??????
				insStCancel.setAmtGrzf(insStCancel.getPsnCashPay());// ??????
				insStCancel.setSetlIdCancel(paramMap.get(i).get("setlId").toString());
				insStCancel.setYbPksettle(insStCancel.getSetlId());
				insStCancel.setPsnNo(paramMap.get(i).get("psnNo").toString());
				insStCancel.setDateSt(new Date());
				insStCancel.setCreator("???his?????????06-25");
				insertInsSt(insStCancel);
			}else if(CommonUtils.getString(retMap.get("err_msg")).contains("??????????????????????????????????????????")){
				retMap.clear();
				InsQgybSt insSt = DataBaseHelper.queryForBean(
						"select * from ins_qgyb_st where SETL_ID_CANCEL=? and del_flag='0' ", InsQgybSt.class, paramMap.get(i).get("setlId").toString());
				retMap.put("ybPksettle", paramMap.get(i).get("setlId").toString());
				retMap.put("amtJjzf", insSt.getFundPaySumamt());
				return retMap;
			} else {
				Log.error("?????????2208?????????????????????" + CommonUtils.getString(retMap.get("err_msg")));
				throw new BusException("?????????2208?????????????????????" + CommonUtils.getString(retMap.get("err_msg")));
			}
		}
		return (Map<String, Object>) ApplicationUtils.beanToMap(insStCancel);
	}

}