package com.zebone.nhis.pro.zsba.mz.ins.zsba.service.qgyb;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

import javax.annotation.Resource;

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

import com.esotericsoftware.minlog.Log;
import com.zebone.nhis.common.module.base.ou.BdOuOrg;
import com.zebone.nhis.common.module.bl.opcg.BlOpDt;
import com.zebone.nhis.common.module.pi.PiMaster;
import com.zebone.nhis.common.module.pv.PvEncounter;
import com.zebone.nhis.common.support.ApplicationUtils;
import com.zebone.nhis.common.support.CommonUtils;
import com.zebone.nhis.common.support.DateUtils;
import com.zebone.nhis.common.support.EnumerateParameter;
import com.zebone.nhis.common.support.HttpClientUtil;
import com.zebone.nhis.common.support.RedisUtils;
import com.zebone.nhis.compay.ins.zsrm.service.qgyb.ZsrmQGUtils;
import com.zebone.nhis.pro.zsba.compay.ins.qgyb.utils.FileUtils;
import com.zebone.nhis.pro.zsba.mz.ins.zsba.dao.ZsbaQGMapper;
import com.zebone.nhis.pro.zsba.mz.ins.zsba.vo.InsPiInfoVo;
import com.zebone.nhis.pro.zsba.mz.ins.zsba.vo.InsQgybCg;
import com.zebone.nhis.pro.zsba.mz.ins.zsba.vo.InsQgybIdet;
import com.zebone.nhis.pro.zsba.mz.ins.zsba.vo.InsQgybInsutype;
import com.zebone.nhis.pro.zsba.mz.ins.zsba.vo.InsQgybLog;
import com.zebone.nhis.pro.zsba.mz.ins.zsba.vo.InsQgybMaster;
import com.zebone.nhis.pro.zsba.mz.ins.zsba.vo.InsQgybPidise;
import com.zebone.nhis.pro.zsba.mz.ins.zsba.vo.InsQgybPreSt;
import com.zebone.nhis.pro.zsba.mz.ins.zsba.vo.InsQgybPv;
import com.zebone.nhis.pro.zsba.mz.ins.zsba.vo.InsQgybSt;
import com.zebone.nhis.pro.zsba.mz.ins.zsba.vo.InsQgybStDt;
import com.zebone.nhis.pro.zsba.mz.ins.zsba.vo.InsQgybTrtinfo;
import com.zebone.nhis.pro.zsba.mz.ins.zsba.vo.InsQgybVisit;
import com.zebone.nhis.pro.zsba.mz.ins.zsba.vo.InsRegParam;
import com.zebone.nhis.pro.zsba.mz.ins.zsba.vo.OutParamHuaJia;
import com.zebone.platform.common.support.User;
import com.zebone.platform.common.support.UserContext;
import com.zebone.platform.framework.support.IUser;
import com.zebone.platform.modules.dao.DataBaseHelper;
import com.zebone.platform.modules.exception.BusException;
import com.zebone.platform.modules.utils.JsonUtil;
//import com.zebone.platform.modules.utils.RedisUtils;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

@SuppressWarnings({ "unchecked", "unused" })
@Service
public class ZsbaQGService {
	
	private Logger logger = LoggerFactory.getLogger("nhis.ZsbaQGLog");
	
	@Value("#{applicationProperties['qgyb.url']}")
	private String HSA_URL;
	@Value("#{applicationProperties['qgyb.accountCode']}")
	private String HSA_ACCOUNT_CODE;
	@Value("#{applicationProperties['qgyb.paasid']}")
	private String HSA_PAASID;
	@Value("#{applicationProperties['qgyb.secretKey']}")
	private String SECRETKEY;
//	@Value("#{applicationProperties['qgyb.fixmedins_name']}")
//	private String FIXMEDINS_NAME;
	private String FIXMEDINS_NAME = "中山市博爱医院";
	@Value("#{applicationProperties['qgyb.fixmedins_code']}")
	private String FIXMEDINS_CODE;
	@Value("#{applicationProperties['qgyb.version']}")
	private String VERSION;

	@Resource private ZsbaQGMapper zsbaQGMapper;

	// 用来控制手动事物
	@Resource(name = "transactionManager")
	private PlatformTransactionManager platformTransactionManager;
	
	/**
	 * 通过身份证号码查询存在中山医保(根据参保地区划判断)
	 * 交易号：022003027119
	 */
	public boolean isPersonInfoFromZs(String params, IUser user) {
		Map<String, Object> paramMap = JsonUtil.readValue(params, Map.class);
		paramMap = ZsbaQGUtils.humpToMap(paramMap);
		paramMap.put("port", "1101");
		// 证件号处理；=15位转为18位
		if (paramMap.containsKey("mdtrt_cert_no")) {
			String cernNo = CommonUtils.getString(paramMap.get("mdtrt_cert_no"));
			if (cernNo.length() == 15) {
				paramMap.put("mdtrt_cert_no", ZsbaQGUtils.getEighteenIDCard(cernNo));// 15位身份证号转为18位
			}
		}
		paramMap.put("mdtrt_cert_type", "02");//类型固定身份证号码
		// 发起交易、解析结果
		String ret = getHealthInsuranceEntry(paramMap, user, null);
		Map<String, Object> retMap = JsonUtil.readValue(ret, Map.class);
		if ("0".equals(CommonUtils.getString(retMap.get("infcode")))) {
			retMap = (Map<String, Object>) retMap.get("output");
			retMap = ZsbaQGUtils.underlineToCamel(retMap);
			InsPiInfoVo inspi = JsonUtil.readValue(ApplicationUtils.objectToJson(retMap), InsPiInfoVo.class);
			// 险种处理: 只保存险种正常的信息
			String insu390 = "";
			String insu310 = "";
			List<InsQgybInsutype> insutypeList = new ArrayList<InsQgybInsutype>();
			boolean isZs = false;
			for (InsQgybInsutype insu : inspi.getInsuinfo()) {
				if("442000".equals(insu.getInsuplcAdmdvs())){
					isZs = true;
					break;
				}
			}
			return isZs;
		}else{
			throw new BusException("功能号1101，医保获取人员信息失败  " + CommonUtils.getString(retMap.get("err_msg")));
		}
	}

	/**
	 * 获取患者基本信息
	 * 015001013001->022003027062
	 * @param params
	 * @param user
	 * @return		基本信息+参保信息+身份信息
	 */
	public InsPiInfoVo getPersonInfo(String params, IUser user) {
		Map<String, Object> paramMap = JsonUtil.readValue(params, Map.class);
		paramMap = ZsbaQGUtils.humpToMap(paramMap);
		paramMap.put("port", "1101");
		String pkPi = CommonUtils.getPropValueStr(paramMap, "pk_pi");
		String pkPv = CommonUtils.getPropValueStr(paramMap, "pk_pv");

		// 证件号处理；=15位转为18位，<15位 港澳台
		if (paramMap.containsKey("mdtrt_cert_no")) {
			String cernNo = CommonUtils.getString(paramMap.get("mdtrt_cert_no"));
			if (cernNo.length() == 15) {
				paramMap.put("mdtrt_cert_no", ZsbaQGUtils.getEighteenIDCard(cernNo));// 15位身份证号转为18位
			} else {
				if (CommonUtils.isNotNull(pkPi)) {
					PiMaster pi = DataBaseHelper.queryForBean("select * from pi_master where pk_pi=?", PiMaster.class, pkPi);
					if (pi != null && !"01".equals(pi.getDtIdtype())) {
						// 港澳台、外籍护照
						if ("02".equals(pi.getDtIdtype()) || "04".equals(pi.getDtIdtype()) || "05".equals(pi.getDtIdtype())) {
							//其他有效证件
							paramMap.put("mdtrt_cert_type", "99");
							paramMap.put("certno", pi.getIdNo());
						} else {
							//社会保障卡
							paramMap.put("mdtrt_cert_type", "03");
						}
					}
				}
			}
		}
		
		// 发起交易、解析结果
		String ret = getHealthInsuranceEntry(paramMap, user, null);
		Map<String, Object> retMap = JsonUtil.readValue(ret, Map.class);
		if ("0".equals(CommonUtils.getString(retMap.get("infcode")))) {
			
			return getInsPiInfo(retMap, paramMap, pkPi, pkPv, user);
			
		}else if(!"99".equals(paramMap.get("mdtrt_cert_type")) && CommonUtils.getString(retMap.get("err_msg")).contains("该参保人的基本信息为空")){
			//20210602部分特殊患者处理方式，查不到人员信息则就诊凭证类型改为其他再试
			paramMap.put("mdtrt_cert_type", "99");
			paramMap.put("certno", paramMap.get("mdtrt_cert_no")!=null?String.valueOf(paramMap.get("mdtrt_cert_no")):"");
			paramMap.put("port", "1101");
			ret = getHealthInsuranceEntry(paramMap, user, null);
			retMap = JsonUtil.readValue(ret, Map.class);
			if ("0".equals(CommonUtils.getString(retMap.get("infcode")))) {
				
				return getInsPiInfo(retMap, paramMap, pkPi, pkPv, user);
				
			}else {
				throw new BusException("功能号1101，医保获取人员信息失败。" + CommonUtils.getString(retMap.get("err_msg")));
			}
		}else{
			throw new BusException("功能号1101，医保获取人员信息失败。" + CommonUtils.getString(retMap.get("err_msg")));
		}
	}
	private InsPiInfoVo getInsPiInfo(Map<String, Object> retMap, Map<String, Object> paramMap, String pkPi, String pkPv, IUser user) {

		retMap = (Map<String, Object>) retMap.get("output");
		retMap = ZsbaQGUtils.underlineToCamel(retMap);
		InsPiInfoVo inspi = JsonUtil.readValue(ApplicationUtils.objectToJson(retMap), InsPiInfoVo.class);

		// 提供在预结算时使用
		String insu390 = "";
		String insu310 = "";
		Set<Map<String, Object>> insuInfoList = new HashSet<Map<String,Object>>();
		if (CommonUtils.isNotNull(pkPv)) {
			// 先删后增: 险种处理 + 身份信息
			String sql = "select * from ins_qgyb_master where psn_cert_type='01' and certno=? ";
			InsQgybMaster insMaster = DataBaseHelper.queryForBean(sql, InsQgybMaster.class, inspi.getBaseinfo().getCertno());
			if (insMaster != null) {
				DataBaseHelper.deleteBeanByPk(insMaster);
				DataBaseHelper.execute("delete ins_qgyb_insutype where pk_insupi=?", insMaster.getPkInsupi());
				DataBaseHelper.execute("delete ins_qgyb_idet where pk_insupi=?", insMaster.getPkInsupi());
			}
			insMaster = inspi.getBaseinfo();
			insMaster.setPkPi(pkPi);
			ApplicationUtils.setDefaultValue(insMaster, true);
			DataBaseHelper.insertBean(insMaster);
			
			List<InsQgybInsutype> insutypeList = new ArrayList<InsQgybInsutype>();
			for (InsQgybInsutype insu : inspi.getInsuinfo()) {
				// 如果不是广东省的，不走医保
				if(!insu.getInsuplcAdmdvs().startsWith("44")){
					continue;
				}
				// 如果不是中山的，要限制 生育医保和计生医保 不给进行医保结算
				if(!"442000".equals(insu.getInsuplcAdmdvs())){
					String pvHpSql = "select hp.CODE, hp.NAME from PV_ENCOUNTER pv INNER JOIN BD_HP hp on hp.PK_HP=pv.PK_INSU where pv.PK_PV=?";
					Map<String, Object> pvHpMap = DataBaseHelper.queryForMap(pvHpSql, pkPv);
					if(pvHpMap!=null && pvHpMap.containsKey("code") ) {
						if("00052".equals(pvHpMap.get("code").toString()) || "00053".equals(pvHpMap.get("code").toString())) {
							continue;
						}
					}
				}
				// 正常状态
				if(EnumerateParameter.ONE.equals(insu.getPsnInsuStas())) {
					if("390".equals(insu.getInsutype())){
						Map<String, Object> infoMap = new HashMap<String, Object>();
						infoMap.put("insuType", insu.getInsutype());
						infoMap.put("nameInsuType", "城乡居民基本医疗保险");
						infoMap.put("insuPlcAdmdvs", insu.getInsuplcAdmdvs());
						insuInfoList.add(infoMap);
					}
					if("310".equals(insu.getInsutype())){
						Map<String, Object> infoMap = new HashMap<String, Object>();
						infoMap.put("insuType", insu.getInsutype());
						infoMap.put("nameInsuType", "职工基本医疗保险");
						infoMap.put("insuPlcAdmdvs", insu.getInsuplcAdmdvs());
						insuInfoList.add(infoMap);
					}
					if("510".equals(insu.getInsutype())){
						//20210602发现310暂停参保但是510正常参保时用310有报销，所以这里增加一种情况：510正常参保时，默认返回310进行结算
						Map<String, Object> infoMap = new HashMap<String, Object>();
						infoMap.put("insuType", "310");
						infoMap.put("nameInsuType", "职工基本医疗保险");
						infoMap.put("insuPlcAdmdvs", insu.getInsuplcAdmdvs());
						insuInfoList.add(infoMap);
					}
					insu.setPkInsupi(insMaster.getPkInsupi());
					ApplicationUtils.setDefaultValue(insu, true);
					insutypeList.add(insu);
				}else{
					// 因为参保状态是当前的，但是否可以进行报销是使用上月正常缴费的，所以不能简单过滤参保状态
					if("390".equals(insu.getInsutype())){
						insu390 = getInsuType(inspi.getBaseinfo().getPsnNo(), insu.getInsutype(), user, pkPv);
						if(StringUtils.isNotEmpty(insu390)){
							insu.setPkInsupi(insMaster.getPkInsupi());
							ApplicationUtils.setDefaultValue(insu, true);
							insutypeList.add(insu);
							
							Map<String, Object> infoMap = new HashMap<String, Object>();
							infoMap.put("insuType", insu.getInsutype());
							infoMap.put("nameInsuType", "城乡居民基本医疗保险");
							infoMap.put("insuPlcAdmdvs", insu.getInsuplcAdmdvs());
							insuInfoList.add(infoMap);
						}
					}
					if("310".equals(insu.getInsutype())){
						insu310 = getInsuType(inspi.getBaseinfo().getPsnNo(), insu.getInsutype(), user, pkPv);
						if(StringUtils.isNotEmpty(insu310)){
							insu.setPkInsupi(insMaster.getPkInsupi());
							ApplicationUtils.setDefaultValue(insu, true);
							insutypeList.add(insu);
							
							Map<String, Object> infoMap = new HashMap<String, Object>();
							infoMap.put("insuType", insu.getInsutype());
							infoMap.put("nameInsuType", "职工基本医疗保险");
							infoMap.put("insuPlcAdmdvs", insu.getInsuplcAdmdvs());
							insuInfoList.add(infoMap);
						}
					}
				}
			}
			inspi.setInsuinfo(insutypeList);
			if(insutypeList != null && insutypeList.size() > 0) {
				DataBaseHelper.batchUpdate(DataBaseHelper.getInsertSql(InsQgybInsutype.class), insutypeList);
			}	
			// 身份信息
			for (InsQgybIdet idet : inspi.getIdetinfo()) {
				idet.setPkInsupi(insMaster.getPkInsupi());
				ApplicationUtils.setDefaultValue(idet, true);
			}
			DataBaseHelper.batchUpdate(DataBaseHelper.getInsertSql(InsQgybIdet.class), inspi.getIdetinfo());
			
			/*
			 *  更新医保计划：险种获取方式(1、保存患者所有险种  2、先取310-职工，没有。取390-居民)
			 *  2021-04-08 lipz 因为后续险种不在从医保主计划中获取，所以不需要再去更新数据
			 */
			//paramMap.put("insutype", CommonUtils.isNotNull(insu310) ? insu310 : insu390);
			//paramMap.put("pkPv", pkPv);
			//zsbaQGMapper.updatePv(paramMap);
			
			
		}else{
			for (InsQgybInsutype insu : inspi.getInsuinfo()) {
				// 如果不是广东省的，不走医保
				if(!insu.getInsuplcAdmdvs().startsWith("44")){
					continue;
				}
				// 如果不是中山的，要限制 生育医保和计生医保 不给进行医保结算
				if(!"442000".equals(insu.getInsuplcAdmdvs())){
					String pvHpSql = "select hp.CODE, hp.NAME from PV_ENCOUNTER pv INNER JOIN BD_HP hp on hp.PK_HP=pv.PK_INSU where pv.PK_PV=?";
					Map<String, Object> pvHpMap = DataBaseHelper.queryForMap(pvHpSql, pkPv);
					if(pvHpMap!=null && pvHpMap.containsKey("code") ) {
						if("00052".equals(pvHpMap.get("code").toString()) || "00053".equals(pvHpMap.get("code").toString())) {
							continue;
						}
					}
				}
				// 正常状态
				if(EnumerateParameter.ONE.equals(insu.getPsnInsuStas())) {
					if("390".equals(insu.getInsutype())){
						Map<String, Object> infoMap = new HashMap<String, Object>();
						infoMap.put("insuType", insu.getInsutype());
						infoMap.put("nameInsuType", "城乡居民基本医疗保险");
						infoMap.put("insuPlcAdmdvs", insu.getInsuplcAdmdvs());
						insuInfoList.add(infoMap);
					}
					if("310".equals(insu.getInsutype())){
						Map<String, Object> infoMap = new HashMap<String, Object>();
						infoMap.put("insuType", insu.getInsutype());
						infoMap.put("nameInsuType", "职工基本医疗保险");
						infoMap.put("insuPlcAdmdvs", insu.getInsuplcAdmdvs());
						insuInfoList.add(infoMap);
					}
					if("510".equals(insu.getInsutype())){
						//20210602发现310暂停参保但是510正常参保时用310有报销，所以这里增加一种情况：510正常参保时，默认返回310进行结算
						Map<String, Object> infoMap = new HashMap<String, Object>();
						infoMap.put("insuType", "310");
						infoMap.put("nameInsuType", "职工基本医疗保险");
						infoMap.put("insuPlcAdmdvs", insu.getInsuplcAdmdvs());
						insuInfoList.add(infoMap);
					}
				}else{
					// 因为参保状态是当前的，但是否可以进行报销是使用上月正常缴费的，所以不能简单过滤参保状态
					if("390".equals(insu.getInsutype())){
						insu390 = getInsuType(inspi.getBaseinfo().getPsnNo(), insu.getInsutype(), user, pkPv);
						if(StringUtils.isNotEmpty(insu390)) {
							Map<String, Object> infoMap = new HashMap<String, Object>();
							infoMap.put("insuType", insu.getInsutype());
							infoMap.put("nameInsuType", "城乡居民基本医疗保险");
							infoMap.put("insuPlcAdmdvs", insu.getInsuplcAdmdvs());
							insuInfoList.add(infoMap);
						}
					}
					if("310".equals(insu.getInsutype())){
						insu310 = getInsuType(inspi.getBaseinfo().getPsnNo(), insu.getInsutype(), user, pkPv);
						if(StringUtils.isNotEmpty(insu310)) {
							Map<String, Object> infoMap = new HashMap<String, Object>();
							infoMap.put("insuType", insu.getInsutype());
							infoMap.put("nameInsuType", "职工基本医疗保险");
							infoMap.put("insuPlcAdmdvs", insu.getInsuplcAdmdvs());
							insuInfoList.add(infoMap);
						}
					}
				}
			}
		}
		
		/**如果310和390都没有则根据1101不论状态如何默认返回一个(优先310)**/
		if(insuInfoList.size() < 1) {
			for (InsQgybInsutype insu : inspi.getInsuinfo()) {
				// 如果不是广东省的，不走医保
				if(!insu.getInsuplcAdmdvs().startsWith("44")){
					continue;
				}
				// 如果不是中山的，要限制 生育医保和计生医保 不给进行医保结算
				if(!"442000".equals(insu.getInsuplcAdmdvs())){
					String pvHpSql = "select hp.CODE, hp.NAME from PV_ENCOUNTER pv INNER JOIN BD_HP hp on hp.PK_HP=pv.PK_INSU where pv.PK_PV=?";
					Map<String, Object> pvHpMap = DataBaseHelper.queryForMap(pvHpSql, pkPv);
					if(pvHpMap!=null && pvHpMap.containsKey("code") ) {
						if("00052".equals(pvHpMap.get("code").toString()) || "00053".equals(pvHpMap.get("code").toString())) {
							continue;
						}
					}
				}
				if("310".equals(insu.getInsutype())){
					Map<String, Object> infoMap = new HashMap<String, Object>();
					infoMap.put("insuType", insu.getInsutype());
					infoMap.put("nameInsuType", "职工基本医疗保险");
					infoMap.put("insuPlcAdmdvs", insu.getInsuplcAdmdvs());
					insuInfoList.add(infoMap);
					break;
				}
				if("510".equals(insu.getInsutype())){
					Map<String, Object> infoMap = new HashMap<String, Object>();
					infoMap.put("insuType", "310");
					infoMap.put("nameInsuType", "职工基本医疗保险");
					infoMap.put("insuPlcAdmdvs", insu.getInsuplcAdmdvs());
					insuInfoList.add(infoMap);
					break;
				}
				if("390".equals(insu.getInsutype())){
					Map<String, Object> infoMap = new HashMap<String, Object>();
					infoMap.put("insuType", insu.getInsutype());
					infoMap.put("nameInsuType", "城乡居民基本医疗保险");
					infoMap.put("insuPlcAdmdvs", insu.getInsuplcAdmdvs());
					insuInfoList.add(infoMap);
					break;
				}
			}

			if(insuInfoList.size() < 1) {
				throw new BusException("功能号1101，未获取到患者有效的参保信息。");
			}
		}
		
		// 设置参保地区划名称,从数据库对照表获取
		String sql = "SELECT top 1 * FROM ins_qgyb_gdqh where code=?";
		for (Map<String, Object> infoMap : insuInfoList) {
			Map<String, Object> qhMap = DataBaseHelper.queryForMap(sql, infoMap.getOrDefault("insuPlcAdmdvs", ""));
			if(qhMap!=null) {
				infoMap.put("nameInsuPlcAdmdvs", qhMap.getOrDefault("name", ""));
			}else {
				infoMap.put("nameInsuPlcAdmdvs", infoMap.getOrDefault("insuPlcAdmdvs", ""));//没找到名称对照则返回参保地区划代码
			}
		}
		inspi.setInsuTypeAndPlaceList(insuInfoList);//提供在预结算时使用
		return inspi;
	}
	//通过90100接口验证对应险种是否有正常缴费参保
	private String getInsuType(String psnNo, String insuType, IUser user, String pkPv){
		String currDate = DateUtils.addDate(new Date(), -1, 2, "yyyyMM");
		Map<String, Object> queryMap = new HashMap<String, Object>();
		queryMap.put("port", "90100");
		queryMap.put("psn_no", psnNo);
		// 发起交易、解析结果
		String result = "";
		String queryRet = getHealthInsuranceEntry(queryMap, user, pkPv);
		JSONObject retJson = JSONObject.fromObject(queryRet);
		if ("0".equals(CommonUtils.getString(retJson.get("infcode")))) {
			JSONArray dataArr = retJson.getJSONArray("output");
			if(dataArr!=null){
				for (int i=0; i<dataArr.size(); i++) {
					JSONObject outJson = dataArr.getJSONObject(i);
					String clct_flag = outJson.getString("clct_flag");//clct_flag 到账类型:0=未到账、1=足额缴费
					if("1".equals(clct_flag)){
						String insutype = outJson.getString("insutype"); //insutype 险种类型代码
						if(insuType.equals(insutype)){
							String accrym_begn = outJson.getString("accrym_begn");//accrym_begn 费款所属期开始日期
							String accrym_end = outJson.getString("accrym_end");//accrym_end 费款所属期结束日期
							//20210604发现一种情况1101全是暂停参保，但是90100一次性补缴费款所属期开始和结束跨度比较大，只要上月在此范围内也可享受待遇
							if(Integer.parseInt(currDate)>=Integer.parseInt(accrym_begn) && Integer.parseInt(currDate)<=Integer.parseInt(accrym_end)) {
								result = insuType;
								break;
							}
						}
					}
				}
			}
		}
		return result;
	}
	
	/**
	 * 待遇检查
	 * 015001013028->022003027063
	 * @param params
	 * @param user
	 * @return
	 */
	public String insCheck(String params, IUser user){
		Map<String, Object> paramMap = JsonUtil.readValue(params, Map.class);
		paramMap = ZsbaQGUtils.humpToMap(paramMap);
		String psnNo = CommonUtils.getPropValueStr(paramMap, "psn_no");
		//获取人员信息
		if (CommonUtils.isNull(psnNo)) {
			InsPiInfoVo insPi = getPersonInfo(params,user);
			paramMap.put("psn_no", insPi.getBaseinfo().getPsnNo());
		}
		//待遇检查
		paramMap.put("fixmedins_code",FIXMEDINS_CODE);
		paramMap.put("port", "2001");
		// 发起交易、解析结果
		String ret = getHealthInsuranceEntry(paramMap, user, null);
		Map<String, Object> retMap = JsonUtil.readValue(ret, Map.class);
		Object object = retMap.get("infcode");
		if ("0".equals(CommonUtils.getString(object))) {
			retMap = (Map<String, Object>) retMap.get("output");
			List<InsQgybTrtinfo> trtinfo = JsonUtil.readValue(JsonUtil.writeValueAsString(retMap.get("trtinfo")), new TypeReference<List<InsQgybTrtinfo>>(){});
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
	 * @param params
	 * @param user
	 * @return
	 */
	public Map<String, Object> OutPatientRegister(String params, IUser user, String insuplcAdmdvs) {
		Map<String, Object> paramMap = JsonUtil.readValue(params, Map.class);
		// 封装请求参数
		paramMap.putAll(zsbaQGMapper.qryInsRegPre(paramMap.get("pkPv").toString()));
		paramMap.put("insuplcAdmdvs", insuplcAdmdvs);
		paramMap.put("port", "2201");
		Map<String, Object> ret = ZsbaQGUtils.humpToMap(paramMap);
		// 发起交易、解析结果
		String strRet = getHealthInsuranceEntry(ret, user, paramMap.get("pkPv").toString());
		Map<String, Object> retMap = JsonUtil.readValue(strRet, Map.class);
		if ("0".equals(CommonUtils.getString(retMap.get("infcode")))) {
			retMap = (Map<String, Object>) retMap.get("output");
			if (retMap.get("data") != null) {
				retMap = (Map<String, Object>) retMap.get("data");
				return retMap;
			}
		}else{
			throw new BusException("医保登记失败  " + CommonUtils.getString(retMap.get("err_msg")));
		}
		return retMap;
	}
	
	/**
	 * 门诊收费-预结算
	 * 015001013002->022003027018
	 * @param params
	 * @param user
	 * @return			医保预结算参数+预结算结果
	 */
	public Map<String,Object> mzHpHuaJia(String params, IUser user){
		Map<String, Object> paramMap = JsonUtil.readValue(params, Map.class);
		// 校验入参
		String errMsg = validaParam(paramMap);
		if (CommonUtils.isNotNull(errMsg)) {
			throw new BusException(errMsg);
		}
		// 公共参数
		String pkPv = paramMap.get("pkPv").toString();
		String insuType = paramMap.get("insuType")!=null?paramMap.get("insuType").toString():"";
		String insuPlcAdmdvs = paramMap.get("insuPlcAdmdvs")!=null?paramMap.get("insuPlcAdmdvs").toString():"";
		User userInfo = (User) user;
		
		// 签到
		signOper(paramMap,userInfo);
		
		// 获取患者基本信息
		Map<String, Object> paraInput = new HashMap<String, Object>();
		paraInput = zsbaQGMapper.qryInsBasePre(pkPv);
		paraInput.put("pkPv", pkPv);
		InsPiInfoVo insPi = getPersonInfo(ApplicationUtils.beanToJson(paraInput), user);
		if (insPi == null) {
			throw new BusException("医保登记失败，未获取到参保人员基本信息！");
		}
		if(insPi.getInsuTypeAndPlaceList().size() < 1){
			throw new BusException("医保登记失败，请检查患者参保险种以及上个月医保缴费是否到账等信息！");
		}
		insPi.setPkPi(paraInput.get("pkPi").toString());
		
		/*
		 * 2021-07-22 
		 * 根据入参判断，入参非空直接使用入参 进行预结算
		 * 入参为空则根据1101返回的集合遍历 进行预结算
		 */
		InsQgybPreSt preSt = null;
		if(StringUtils.isNotEmpty(insuType) && StringUtils.isNotEmpty(insuPlcAdmdvs)) {
			
			preSt = preSettle(paramMap, insPi, insuType, insuPlcAdmdvs, pkPv, user);
			
		}else {
			/**20210611判断是否有指定本次就诊医保结算险种，否则只用指定险种结算**/
			Set<Map<String, Object>> insuTypeAndAdmdvs = new HashSet<Map<String,Object>>();
			InsQgybPv insQgybPv = DataBaseHelper.queryForBean("select top 1 * from ins_qgyb_pv where pk_pv=? and del_flag='0'", InsQgybPv.class, pkPv);
			if(insQgybPv!=null && StringUtils.isNotEmpty(insQgybPv.getInsutype())){
				for(Map<String, Object> info : insPi.getInsuTypeAndPlaceList()) {
					if("310".equals(CommonUtils.getString(info.get("insuType")))) {
						insuTypeAndAdmdvs.add(info);
					}
					if("390".equals(CommonUtils.getString(info.get("insuType")))) {
						insuTypeAndAdmdvs.add(info);
					}
				}
			}
			if(insuTypeAndAdmdvs.size() < 1) {
				insuTypeAndAdmdvs = insPi.getInsuTypeAndPlaceList();
			}
			
			for (Map<String, Object> info  : insuTypeAndAdmdvs) {
				if(info.get("insuType")==null || info.get("insuPlcAdmdvs")==null) {
					continue;
				}
				
				preSt = preSettle(paramMap, insPi, info.get("insuType").toString(), info.get("insuPlcAdmdvs").toString(), pkPv, user);
				
				if(preSt.getYbPreSettleInfo().getFundPaySumamt() > 0){
					break;//有报销则直接退出
				}
			}
		}
		return (Map<String, Object>) ApplicationUtils.beanToMap(preSt);
	}
	private InsQgybPreSt preSettle(Map<String, Object> paramMap, InsPiInfoVo insPi, String insuType, String insuPlcAdmdvs, String pkPv, IUser user) {
		InsQgybPreSt preSt = null;
		//获取挂号登记信息 
		InsQgybVisit visit = getPvVist(paramMap, insPi, insuType, insuPlcAdmdvs, user);
		if (visit == null || CommonUtils.isNull(visit.getMedType())) {
			throw new BusException("获取医保登记信息信息失败");
		}
		paramMap.put("YBRegInfo", visit);
		// 上传明细信息
		String chrgBchno = ApplicationUtils.getCode("0608");// 收费批次号
		paramMap.put("chrgBchno", chrgBchno);
		List<Map<String, Object>> dts = zsbaQGMapper.qryChargeDetailNoUpload(paramMap);
		updateHerbalPresForHospApprFlag(dts);//根据中药饮片规则更新处方药品医院审批标志
		
		//待上传明细为空，可能只有特需费用，直接返回
		String retStr = "";
		Map<String, Object> reqMap = new HashMap<>();
		Map<String, Object> retMap = new HashMap<>();
		BigDecimal medfeeSumamt = BigDecimal.ZERO;
		if(dts==null || dts.size()==0){
			preSt = new InsQgybPreSt();
			Double dtAmt=0d;
			//检索非特需费用(博爱目前应该没有特需费用这个东西)
			List<BlOpDt> dtRet=zsbaQGMapper.qryChargeDetailNoUploadSpec(paramMap);
			for(BlOpDt dt :dtRet){
				dtAmt = dtAmt+dt.getAmount();
			}
			preSt.setAggregateAmount(dtAmt);
			preSt.setMedicarePayments(0d);
			preSt.setPatientsPay(dtAmt);
			return preSt;
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
		dts = ZsbaQGUtils.humpToLineListMap(dts);
		
		//撤销费用上传
		reqMap.put("pkPv", pkPv);
		mzBillDel(JsonUtil.writeValueAsString(reqMap), user);
		
		//上传本次费用
		reqMap.put("port", "2204");
		reqMap.put("params", dts);
		reqMap.put("pk_pv", pkPv);
		
		// 发起请求，解析结果
		retStr = getHealthInsuranceEntry(reqMap, user, pkPv);
		retMap = JsonUtil.readValue(retStr, Map.class);
		if (!"0".equals(CommonUtils.getString(retMap.get("infcode")))) {
			throw new BusException("功能号2204，上传费用明细失败，失败原因：" + CommonUtils.getString(retMap.get("err_msg")));
		}
		
		retMap = (Map<String, Object>) retMap.get("output");
		retStr = JsonUtil.writeValueAsString(retMap.get("result"));
		List<InsQgybCg> cgs = JsonUtil.readValue(retStr, new TypeReference<List<InsQgybCg>>(){});
		
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
			//由于测试中发现定价上限金额会出现很大的情况导致值变成科学计数法后入库会出现格式转换的错误，所以暂时对这个字段做下转换处理
			for (InsQgybCg insQgbCg : cgs) {
				String pricUplmtAmt = new BigDecimal(insQgbCg.getPricUplmtAmt()).setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString();
				insQgbCg.setPricUplmtAmt(pricUplmtAmt);
			}
			DataBaseHelper.batchUpdate(DataBaseHelper.getInsertSql(InsQgybCg.class), cgs);
		
			platformTransactionManager.commit(status); // 提交事务
		} catch (Exception e) {
			platformTransactionManager.rollback(status); // 添加失败 回滚事务；
			e.printStackTrace();
			throw new BusException("上传费用信息成功，保存失败：" + e);
		}
		retMap.clear();
		reqMap.clear();
		retStr = "";
		
		// 预结算参数封装
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
		// 发起预结算
		retStr = getHealthInsuranceEntry(reqMap, user, pkPv);
		retMap = JsonUtil.readValue(retStr, Map.class);
		if (!"0".equals(CommonUtils.getString(retMap.get("infcode")))) {
			logger.error(retStr);
			// 预结算失败，撤销明细上传
			reqMap.clear();
			reqMap.put("pkPv", pkPv);
			mzBillDel(JsonUtil.writeValueAsString(reqMap), user);
			throw new BusException("功能号2206，医保预结算失败：" + CommonUtils.getString(retMap.get("err_msg")));
		}
		
		//解析预结算结果
		Map<String, Object> zretMap = (Map<String, Object>) ((Map<String, Object>) retMap.get("output")).get("setlinfo");
		OutParamHuaJia outParamHuaJia = JsonUtil.readValue(JsonUtil.writeValueAsString(zretMap), OutParamHuaJia.class);
		//outParamHuaJia.setAmtGrzhzf(outParamHuaJia.getAcctPay());		// 个人账户
		outParamHuaJia.setAmount(outParamHuaJia.getMedfeeSumamt());		// 总金额
		outParamHuaJia.setAmtGrzf(outParamHuaJia.getPsnCashPay());		// 现金
		outParamHuaJia.setAmtJjzf(outParamHuaJia.getFundPaySumamt());	// 基金
		// 封装回参
		preSt = new InsQgybPreSt();
		preSt.setYbPreSettleInfo(outParamHuaJia);
		preSt.setYbPreSettleParam(reqMap);
		
		return preSt;
	}
	
	/**
	 * 将上传费用明细列表数据根据中药饮片规则更新处方药品医院审批标志(0无需审批、1审批通过、2审批不通过)
	 * @param dts
	 */
	public void updateHerbalPresForHospApprFlag(List<Map<String, Object>> dts) {
		List<String> pkPresList = new ArrayList<String>();
		String pkPres = "";//处方主键
		//1.首先需要拿到上传费用明细里包含几张处方主键PK_PRES列表
		for(Map<String, Object> dt : dts) {
			pkPres = dt.get("pkPres")!=null?String.valueOf(dt.get("pkPres")):"";
			if(StringUtils.isNotEmpty(pkPres) && !pkPresList.contains(pkPres)) {
				pkPresList.add(pkPres);
			}
		}
		if(pkPresList.size()>=1) {//有处方主键才执行后续业务
			logger.debug("本次上传费用明细处方列表："+StringUtils.strip(pkPresList.toString(),"[]"));
			//2.通过处方主键列表查询所有中药饮片的相关信息（不管B类，统一归到A类）
			List<Map<String, Object>> herbalInfoList = zsbaQGMapper.qryHerbalListInfoByPkPres(pkPresList);
			if(herbalInfoList.size()>=1) {//本次上传费用明细包含中药饮片数据才执行后续业务
				logger.debug("本次上传费用明细处方中药饮片数："+herbalInfoList.size());
				//3.然后遍历处方主键PK_PRES列表，内层再用当前遍历的处方主键判断第2步的结果集，根据drugType判断该处方是否包含C、D类药品按规则更新医院审批标志
				boolean flagC = false;//C类标记
				boolean flagD = false;//D类标记
				List<String> pkCgopList = null;//D类药品计费明细主键列表
				for(String pkPre : pkPresList) {
					flagC = false;flagD = false;//默认当前遍历处方C、D类标记为false
					pkCgopList = new ArrayList<String>();//D类药品计费明细主键列表
					//3.1.判断该处方下的药品是否包含C、D类，D类则同时记录下该药品计费明细主键
					for(Map<String, Object> herbalInfo : herbalInfoList) {
						if(pkPre.equals(String.valueOf(herbalInfo.get("pkPres")))) {
							if(String.valueOf(herbalInfo.get("drugType")).equals("C")) {
								flagC = true;
							}
							if(String.valueOf(herbalInfo.get("drugType")).equals("D")) {
								flagD = true;
								if(!pkCgopList.contains(String.valueOf(herbalInfo.get("pkCgop")))) {
									pkCgopList.add(String.valueOf(herbalInfo.get("pkCgop")));
								}
							}
						}
					}
					logger.debug("当前遍历处方中药饮片规则标记：处方主键=>"+pkPre+"=>C类标记=>"+flagC+"=>D类标记=>"+flagD+"=>D类药品计费主键列表=>"+StringUtils.strip(pkCgopList.toString(),"[]"));
					//3.2.根据C、D类标记遍历上传费用明细列表，将所有等于该处方主键的药品根据中药饮片医保支付政策规则更新医院审批标志
					for(Map<String, Object> dt : dts) {
						if(dt.get("pkPres")!=null && pkPre.equals(String.valueOf(dt.get("pkPres")))) {
							if(!flagC && !flagD) {
								//C、D类都不包含则整张处方做A、B类处理整张处方不可报销
								dt.put("hosp_appr_flag", "2");
							}else {
								if(flagC) {
									if(flagD) {
										//包含C且包含D则除D类不可报销，其他都是可报销
										if(pkCgopList.contains(String.valueOf(dt.get("pkCgop")))) {
											dt.put("hosp_appr_flag", "2");
										}else {
											dt.put("hosp_appr_flag", "1");
										}
									}else {
										//包含C但不包含D整张处方可报销
										dt.put("hosp_appr_flag", "1");
									}
								}else {
									//不包含C则整张处方不可报销
									dt.put("hosp_appr_flag", "2");
								}
							}
							logger.debug("当前更新处方药品医院审核标记：处方主键=>"+pkPre+"=>计费主键=>"+String.valueOf(dt.get("pkCgop"))+"=>药品名称=>"+String.valueOf(dt.get("nameCg"))+"=>医院审批标志=>"+String.valueOf(dt.get("hospApprFlag")));
						}
					}
				}
			}else {
				logger.debug("中药饮片业务提示：本次上传费用明细无中药饮片数据！");
			}
		}else {
			logger.debug("中药饮片业务提示：本次上传费用明细无处方主键列表！");
		}
	}
	
	/*
	 * 签到 qgybSignIn+codeEmp 
	 * @param paramMap
	 * @param userInfo
	 */
	private void signOper(Map<String, Object> paramMap, User userInfo) {	
		if ("true".equals(ApplicationUtils.getPropertyValue("qgyb.signInFlag", ""))) {
			Object obj = RedisUtils.getCacheObj("qgybSignIn" + userInfo.getCodeEmp());
			if (obj == null) {
				Map<String, Object> reqMap = new HashMap<String, Object>();
				reqMap.put("opter_no", paramMap.get("opterNo"));
				reqMap.put("mac", paramMap.get("mac"));
				reqMap.put("ip", paramMap.get("ip"));
				reqMap.put("port", "9001");
				String retStr = getHealthInsuranceEntry(reqMap, userInfo, paramMap.get("pkPv").toString());
				Map<String, Object> retMap = JsonUtil.readValue(retStr, Map.class);
				if (!"0".equals(CommonUtils.getString(retMap.get("infcode")))) {
					if (CommonUtils.isNotNull(retMap.get("output"))) {
						retMap = (Map<String, Object>) retMap.get("output");
					}
					reqMap.putAll(retMap);
					RedisUtils.setCacheObj("qgybSignIn" + userInfo.getCodeEmp(), reqMap, 60*60*24);
				}
			}
		}
	}

	/*
	 * 获取最新的一次院内医保就诊信息
	 * @param pkPv
	 * @return
	 */
	private InsQgybVisit qryVisitInfo(String pkPv){
		return DataBaseHelper.queryForBean("select top 1 * from ins_qgyb_visit where pk_pv=?  order by create_time desc", InsQgybVisit.class, pkPv);
	}
				
	/**
	 * 撤销上传
	 * 015001013003->022003027019
	 * @param params
	 * @param user
	 */
	public void mzBillDel(String params, IUser user) {
		Map<String, Object> paramMap = JsonUtil.readValue(params, Map.class);
		String pkPv = CommonUtils.getString(paramMap.get("pkPv"));
		InsQgybVisit visit = qryVisitInfo(pkPv);
		if (visit != null) {
			paramMap.clear();
			paramMap.put("port", "2205");
			paramMap.put("mdtrt_id", visit.getMdtrtId());
			paramMap.put("psn_no", visit.getPsnNo());
			paramMap.put("chrg_bchno", "0000");
			String retStr = getHealthInsuranceEntry(paramMap, user, pkPv);
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

	/*
	 * 校验就诊主键、费用明细主键
	 * @param paramMap
	 * @return
	 */
	private String validaParam(Map<String, Object> paramMap) {
		String errMsg = "";
		if (paramMap == null) {
			errMsg = "未获取到参数信息";
			return errMsg;
		}
		if (CommonUtils.isNull(paramMap.get("pkPv"))) {
			errMsg = "未传入就诊信息主键：pkPv";
			return errMsg;
		} 
//		TODO: 2021-07-22 lipz 等公众号-自助机按先查参保信息进行预结算后，需要去掉注释
//		if (CommonUtils.isNull(paramMap.get("insuType"))) {
//			errMsg = "未传入险种：insuType";
//			return errMsg;
//		}
//		if (CommonUtils.isNull(paramMap.get("insuPlcAdmdvs"))) {
//			errMsg = "未传入参保区划：insuPlcAdmdvs";
//			return errMsg;
//		}
		if (CommonUtils.isNull(paramMap.get("pkCgops"))) {
			if(paramMap.containsKey("opterNo") && paramMap.get("opterNo").toString().startsWith("999")){
				errMsg = "";//兼容 自助机、公众号不传入明细的情况
			}else{
				errMsg = "未传入费用信息(pkCgops)";
				return errMsg;
			}
		}else{
			Set<String> pkCgops = new HashSet<String>((List<String>) paramMap.get("pkCgops"));
			String pks = CommonUtils.convertSetToSqlInPart(pkCgops, "pk_cgop");
			String sql = "select count(1) from bl_op_dt where flag_settle='1' and pk_cgop in (" + pks + ") and pk_pv = ? ";
			int cnt =  DataBaseHelper.queryForScalar(sql, Integer.class,paramMap.get("pkPv"));
			if (cnt > 0) {
				errMsg = "数据已变更，请刷新重试";
				return errMsg;
			}
		}
		return errMsg;
	}

	/*
	 * 获取医保登记信息
	 * @param paramMap
	 * @param user
	 * @return
	 */
	private InsQgybVisit getPvVist(Map<String, Object> paramMap, InsPiInfoVo insPi, String insuType, String insuplcAdmdvs, IUser user) {
		String pkPv = CommonUtils.getPropValueStr(paramMap, "pkPv");
		if (CommonUtils.isNull(pkPv)) {
			throw new BusException("未传入就诊信息主键pkPv");
		}
		//获取患者院内就诊信息
		String pvSql = "select * from pv_encounter where pk_pv=? and eu_pvtype in (1,2,4) and del_flag='0' ";
		PvEncounter pvencounter = DataBaseHelper.queryForBean(pvSql, PvEncounter.class, pkPv);
		if(pvencounter==null){
			throw new BusException("未查询到对应的院内就诊信息[pkPv:" + pkPv +"]");
		}
		//获取患者院内建卡信息
		String piSql = "select * from pi_master where pk_pi=? and del_flag='0' ";
		PiMaster piMaster = DataBaseHelper.queryForBean(piSql, PiMaster.class, pvencounter.getPkPi());
		if(piMaster==null){
			throw new BusException("未查询到对应的院内建卡信息[pkPi:" + pvencounter.getPkPi() +"]");
		}
		//验证建卡信息和医保平台姓名
		String namePi = piMaster.getNamePi().trim();
		if(!namePi.equals(insPi.getBaseinfo().getPsnName())){
			throw new BusException("患者资料姓名["+ namePi +"] 与 医保平台返回姓名["+ insPi.getBaseinfo().getPsnName() +"] 不符！请在【人工办卡】界面核对修改[姓名]与[本人身份证号码]。");
		}
		// 就诊姓名 与 建卡姓名 不一致的，使用建卡姓名更新就诊姓名
		if(!pvencounter.getNamePi().equals(namePi)){
			String updatePvSql = "update ins_qgyb_pv set NAME_PI=? where pk_pv=? and del_flag='0'";
			DataBaseHelper.execute(updatePvSql, new Object[]{namePi, pkPv});
			String updateVisitSql = "update ins_qgyb_visit set NAME_PI=? where pk_pv=? and del_flag='0'";
			DataBaseHelper.execute(updateVisitSql, new Object[]{namePi, pkPv});
			String updatePvEncounterSql = "update pv_encounter set NAME_PI=? where pk_pv=? and del_flag='0'";
			DataBaseHelper.execute(updatePvEncounterSql, new Object[]{namePi, pkPv});
		}
		
		String medType = "11";//医疗类别：默认普通门诊
		
		/*
		 * 2021-04-16通过就诊记录中的医保主计划获取对应的拓展属性中的医疗类别(0330)更新到医保登记信息表
		 */
		StringBuffer hpDictSql = new StringBuffer();
		hpDictSql.append(" select top 1 att.val_attr from bd_dictattr att ");
		hpDictSql.append(" inner join bd_dictattr_temp tmp on att.pk_dictattrtemp=tmp.pk_dictattrtemp ");
		hpDictSql.append(" where att.pk_dict=? and tmp.code_attr='0330' ");
		Map<String,Object> hpDictMap = DataBaseHelper.queryForMap(hpDictSql.toString(), new Object[]{pvencounter.getPkInsu()});
		if(hpDictMap!=null && hpDictMap.get("valAttr")!=null && StringUtils.isNotEmpty(String.valueOf(hpDictMap.get("valAttr")))){
			medType = String.valueOf(hpDictMap.get("valAttr"));
			/**20210602如果不存在医保PV记录则创建(可能不用医保身份就诊不会创建pv表记录，导致后面取不到直接默认医疗类别11进行结算)**/
			String insPvSql = "select top 1 * from ins_qgyb_pv where pk_pv=? and del_flag='0' ";
			InsQgybPv insQgybPv = DataBaseHelper.queryForBean(insPvSql, InsQgybPv.class, pkPv);
			if(insQgybPv!=null) {
				String updatePvSql = "update ins_qgyb_pv set med_type=? where pk_pv=? and del_flag='0'";
				DataBaseHelper.execute(updatePvSql, new Object[]{medType, pkPv});
			}else {
				//获取博爱机构信息
				BdOuOrg bdOuOrg = DataBaseHelper.queryForBean("select * from BD_OU_ORG where CODE_ORG=?",BdOuOrg.class,new Object[]{"202"});
				insQgybPv = new InsQgybPv();
				ApplicationUtils.setDefaultValue(insQgybPv, true);
				insQgybPv.setPkOrg(bdOuOrg.getPkOrg());
				insQgybPv.setPkHp(pvencounter.getPkInsu());
				insQgybPv.setPkPi(piMaster.getPkPi());
				insQgybPv.setPkPv(pkPv);
				insQgybPv.setNamePi(namePi);
				insQgybPv.setMedType(medType);
				DataBaseHelper.insertBean(insQgybPv);
			}
			//20210723增加验证：慢特病医保必须有慢病病种、计生医保必须有计生手术类别、计生手术或生育日期
			if(medType.equals("14") && (StringUtils.isEmpty(insQgybPv.getDiseCodg()) || StringUtils.isEmpty(insQgybPv.getDiseName()))) {
				throw new BusException("慢特病医保结算必须登记病种名称、病种编码，请登记信息后再试！");
			}
			if(medType.equals("5301") && (StringUtils.isEmpty(insQgybPv.getBirctrlType()) || insQgybPv.getBirctrlMatnDate()==null)) {
				throw new BusException("计生医保结算必须登记计生手术类别、计生手术日期或生育日期，联系医生登记信息后再试！");
			}
			
			//更新医保就诊登记信息
			String updateVisitSql = "update ins_qgyb_visit set med_type=? where pk_pv=? and del_flag='0'";
			DataBaseHelper.execute(updateVisitSql, new Object[]{medType, pkPv});
		}else{
			String insPvSql = "select * from ins_qgyb_pv where pk_pv=? and del_flag='0' ";
			InsQgybPv insQgybPv = DataBaseHelper.queryForBean(insPvSql, InsQgybPv.class, pkPv);
			if (insQgybPv != null && insQgybPv.getMedType() != null) {
				medType = insQgybPv.getMedType();
			}
		}
		
//		// 查询医保登记信息
//		Map<String, Object> piVisit = zsbaQGMapper.qryInsVisit(pkPv);
//		//有就诊登记信息时则校验上次挂号的险种类型和当前就诊的险种类型是否一致，不一致时重新调用挂号接口
//		boolean flagReg = false;
//		if(piVisit!=null && piVisit.size()>0){
//			if(piVisit.containsKey("insutype") && !insuType.equals(CommonUtils.getPropValueStr(piVisit,"insutype"))){
//				flagReg = true;
//			}
//		}
	
		// 医保挂号
//		if ((piVisit == null || piVisit.size() == 0) || flagReg){
			InsRegParam insRegParam = new InsRegParam();
			insRegParam.setPsnNo(insPi.getBaseinfo().getPsnNo());
			insRegParam.setInsutype(insuType);
			insRegParam.setMdtrtCertType("02");
			insRegParam.setMdtrtCertNo(insPi.getBaseinfo().getCertno());
			insRegParam.setPkPv(pkPv);
			// 挂号
			Map<String, Object> retRegMap = OutPatientRegister(ApplicationUtils.beanToJson(insRegParam), user, insuplcAdmdvs);
			// 保存挂号信息
			InsQgybVisit visit = new InsQgybVisit();
			visit.setPkPv(pkPv);
			visit.setMdtrtId(retRegMap.get("mdtrt_id").toString());
			visit.setPsnNo(retRegMap.get("psn_no").toString());
			// visit.setIptOtpNo(retRegMap.get("ipt_otp_no").toString());
			visit.setInsutype(insRegParam.getInsutype());
			visit.setMdtrtCertType("02");
			visit.setMdtrtCertNo(insRegParam.getMdtrtCertNo());
			visit.setPkPi(insPi.getPkPi());
			visit.setMedType(medType);
			visit.setBegntime(new Date());
			visit.setNamePi(insPi.getBaseinfo().getPsnName());
			visit.setPkHp(pvencounter.getPkInsu());
			visit.setInsuplcAdmdvs(insuplcAdmdvs);// TODO: 20210709 lipz 增加参保地区划
			// 关闭事务自动提交
			DefaultTransactionDefinition def = new DefaultTransactionDefinition();
			def.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRES_NEW);
			TransactionStatus status = platformTransactionManager.getTransaction(def);
			try {
				ApplicationUtils.setDefaultValue(visit, true);
				DataBaseHelper.insertBean(visit);
				platformTransactionManager.commit(status); 		// 提交事务
			} catch (Exception e) {
				platformTransactionManager.rollback(status); 	// 添加失败 回滚事务
				e.printStackTrace();
				throw new BusException("保存挂号信息失败，请重试：" + e);
			}
//		}

		// 先上传就诊信息
		List<Map<String, Object>> pvDiags = zsbaQGMapper.qryPvDiag(pkPv);// 查询诊断信息
		StringBuffer iqvSql = new StringBuffer();
		iqvSql.append(" SELECT TOP 1 visit.mdtrt_id, visit.psn_no, visit.flag_insu, CONVERT(VARCHAR(19), visit.begntime, 120) as begntime ");
		iqvSql.append(" ,visit.insutype,case when inspv.med_type is null then '11' else inspv.med_type end as med_type,inspv.geso_val ");
		iqvSql.append(" ,inspv.main_cond_dscr,inspv.dise_codg,inspv.dise_name,inspv.birctrl_type,convert(varchar(10), inspv.birctrl_matn_date, 120) birctrl_matn_date ");
		iqvSql.append(" FROM ins_qgyb_visit visit ");
		iqvSql.append(" LEFT JOIN ins_qgyb_pv inspv on inspv.PK_PV=visit.PK_PV and inspv.del_flag='0' ");
		iqvSql.append(" WHERE visit.pk_pv=? ");
		iqvSql.append(" ORDER BY visit.create_time DESC ");
		Map<String, Object> piVisit = DataBaseHelper.queryForMap(iqvSql.toString(), pkPv);

		pvDiags = ZsbaQGUtils.humpToLineListMap(pvDiags);
		String mdtrtinfo = ZsbaQGUtils.humpToLine(piVisit);
		String diseinfo = ZsbaQGUtils.humpToLineListStr(pvDiags);
		Map<String, Object> pvParam = new HashMap<String, Object>();
		pvParam.put("port", "2203A");
		pvParam.put("mdtrtinfo", mdtrtinfo);
		pvParam.put("diseinfo", diseinfo);
		pvParam.put("pk_pv",pkPv);
		
		// 执行上传
		String strRet = getHealthInsuranceEntry(pvParam, user, pkPv);

		Map<String, Object> upPv = JsonUtil.readValue(strRet, Map.class);
		if (!"0".equals(CommonUtils.getString(upPv.get("infcode")))) {
			throw new BusException("功能号2203A，上传就诊信息失败："+CommonUtils.getString(upPv.get("err_msg")));
		}
		
		// 查询医保就诊信息，更新上传标志，保存就诊信息
		InsQgybVisit insVisit = qryVisitInfo(pkPv);
		insVisit.setFlagInsu("1");
		DataBaseHelper.updateBeanByPk(insVisit);
		return insVisit;
	}

	/**
	 * 门诊缴费-正式结算
	 * 015001013004->022003027020
	 * @param params
	 * @param user
	 * @return
	 */
	public InsQgybSt mzHpJiaokuan(String params, IUser user) {
		Map<String, Object> paramMap = JsonUtil.readValue(params, Map.class);
		//公共参数
		String pkPv = paramMap.get("pkPv").toString();
		//发起请求
		Map<String, Object> reqMap = (Map<String, Object>) paramMap.get("ybPreSettlParam");
		reqMap.put("port", "2207");
		String retStr = getHealthInsuranceEntry(reqMap, user, pkPv);
		//解析结果
		Map<String, Object> retMap = JsonUtil.readValue(retStr, Map.class);
		if (!"0".equals(CommonUtils.getString(retMap.get("infcode")))) {
			throw new BusException("功能号2207，医保结算失败 ："+CommonUtils.getString(retMap.get("err_msg")));
		}

		Map<String, Object> outputMap = ((Map<String, Object>) retMap.get("output"));
		Map<String, Object> setlinfo = (Map<String, Object>) outputMap.get("setlinfo");
		OutParamHuaJia outParamHuaJia = JsonUtil.readValue(JsonUtil.writeValueAsString(setlinfo), OutParamHuaJia.class);
		if (CommonUtils.isNull(outParamHuaJia.getSetlId())) {
			Log.error("医保结算成功，未返回结算id");
		}
		//成功后在查询登记信息
		InsQgybVisit visit = qryVisitInfo(pkPv);
		// 保存医保结算信息
		InsQgybSt insQgybSt = new InsQgybSt();
		ApplicationUtils.copyProperties(insQgybSt, outParamHuaJia);
		insQgybSt.setPkPv(pkPv);
		insQgybSt.setPkPi(visit.getPkPi());
		insQgybSt.setPkHp(visit.getPkHp());
		insQgybSt.setPkVisit(visit.getPkVisit());
		insQgybSt.setDateSt(new Date());

		// insQgybSt.setAmtGrzhzf(outParamHuaJia.getAcctPay());		// 个人账户
		insQgybSt.setAmount(outParamHuaJia.getMedfeeSumamt());		// 总金额
		insQgybSt.setAmtGrzf(outParamHuaJia.getPsnCashPay());		// 现金
		insQgybSt.setAmtJjzf(outParamHuaJia.getFundPaySumamt());	// 基金
		insQgybSt.setYbPksettle(outParamHuaJia.getSetlId());
		insertInsSt(insQgybSt);

		// 保存医保结算基金分项信息
		List<Map<String, Object>> setldetail = (List<Map<String, Object>>) outputMap.get("setldetail");
		List<InsQgybStDt> insStDts = JsonUtil.readValue(JsonUtil.writeValueAsString(setldetail), new TypeReference<List<InsQgybStDt>>() {});
		for (InsQgybStDt insDt : insStDts) {
			ApplicationUtils.setDefaultValue(insDt, true);
			insDt.setPkInsst(insQgybSt.getPkInsst());
		}
		DataBaseHelper.batchUpdate(DataBaseHelper.getInsertSql(InsQgybStDt.class), insStDts);
		
		return insQgybSt;
	}

	/**
	 * 门诊退费
	 * 015001013005->022003027021
	 * @param params
	 * @param user
	 * @return
	 */
	public Map<String, Object> mzHpSetttleCancel(String params, IUser user) {
		Map<String, Object> paramMap = JsonUtil.readValue(params, Map.class);
		// his结算失败>医保退费
		if (CommonUtils.isNull(paramMap.get("pkPv"))){
			return insSettleCancle(params, user);	
		} 
		if (CommonUtils.isNull(paramMap.get("pkSettle"))) {
			throw new BusException("未传入结算主键");
		}
		InsQgybVisit visit = qryVisitInfo(CommonUtils.getString(paramMap.get("pkPv")));
		if(visit == null){
			throw new BusException("就诊主键：" + paramMap.get("pkPv") + "，未查询到医保就诊记录！");
		}
		InsQgybSt insQgybSt = DataBaseHelper.queryForBean("select * from ins_qgyb_st where pk_settle=?", InsQgybSt.class, paramMap.get("pkSettle"));
		if (insQgybSt == null) {
			throw new BusException("结算主键：" + paramMap.get("pkSettle") + "，未查询到医保结算记录！");
		}
		Map<String, Object> retMap = new HashMap<String, Object>();
		retMap.put("ybPkSettle", insQgybSt.getYbPksettle());
		retMap = insSettleCancle(JsonUtil.writeValueAsString(retMap), user);//全部退费
		
		// 退费医保结算主键要返回前端传到HIS正式退费结算接口用作HIS退费结算主键等数据回写
		String ybCancelPksettle = retMap.get("ybPksettle")!=null?String.valueOf(retMap.get("ybPksettle")):"";
		logger.info("全国医保退费结算主键==>>"+ybCancelPksettle);
		
		// 门诊退费
		paramMap = (Map<String, Object>) paramMap.get("ybPreReturnInfo");
		paramMap = (Map<String, Object>) paramMap.get("yBPreIntoParam");
		Map<String, Object> returnMap = new HashMap<String, Object>();
		if(paramMap!=null){
			// 发起请求
			paramMap.put("port", "2207");
			String retStr = getHealthInsuranceEntry(paramMap, user, CommonUtils.getString(paramMap.get("pkPv")));
			// 解析结果
			retMap = JsonUtil.readValue(retStr, Map.class);
			if (!"0".equals(CommonUtils.getString(retMap.get("infcode")))) {
				throw new BusException("功能号2207，医保结算失败 ："+CommonUtils.getString(retMap.get("err_msg")));
			}

			retMap = (Map<String, Object>) ((Map<String, Object>) retMap.get("output")).get("setlinfo");
			OutParamHuaJia outParamHuaJia = JsonUtil.readValue(JsonUtil.writeValueAsString(retMap), OutParamHuaJia.class);
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

			//insQgybSt.setAmtGrzhzf(outParamHuaJia.getAcctPay());		// 个人账户
			reInsQgybSt.setAmount(outParamHuaJia.getMedfeeSumamt());	// 总金额
			reInsQgybSt.setAmtGrzf(outParamHuaJia.getPsnCashPay());		// 现金
			reInsQgybSt.setAmtJjzf(outParamHuaJia.getFundPaySumamt());	// 基金
			reInsQgybSt.setYbPksettle(outParamHuaJia.getSetlId());
			insertInsSt(reInsQgybSt);
			
			logger.info("全国医保部分退费主键==>>"+ybCancelPksettle+"；重新结算主键==>>"+reInsQgybSt.getSetlId());
			returnMap = (Map<String, Object>) ApplicationUtils.beanToMap(reInsQgybSt);
			returnMap.put("ybCancelPksettle", ybCancelPksettle);
			return returnMap;
		}else{
			returnMap.put("ybCancelPksettle", ybCancelPksettle);
			return returnMap;
		}
	}

	/**
	 * 将PkSettle结算主键更新到INS_QGYB_ST表
	 * 015001013006->022003027022
	 * @param params
	 * @param user
	 */
	public void updatePkSettle(String params, IUser user) {
		logger.debug("将PkSettle结算主键更新到INS_QGYB_ST表==>>"+params);
		Map<String, Object> paramMap = JsonUtil.readValue(params, Map.class);
		String pkPv = paramMap.get("pkPv").toString();

		String pkSettle = null;
		String pkSettleCanc = null;
		String ywlx = null;// 业务类型
		String setlId = null;// 医保结算主键
		String reChargeSetlId = null;// 医保部分退重新结算主键

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
		// 医保结算ID(收费或者退费)
		if (paramMap.containsKey("pkPtmzjs") && paramMap.get("pkPtmzjs") != null) {
			setlId = CommonUtils.getString(paramMap.get("pkPtmzjs"));
		}
		// 部分退医保重结ID
		if (paramMap.containsKey("jsId") && paramMap.get("jsId") != null) {
			reChargeSetlId = CommonUtils.getString(paramMap.get("jsId"));
		}
		if ("1".equals(CommonUtils.getString(ywlx))) {// 门诊结算
			// 部分退重结
			if (CommonUtils.isNotNull(pkSettle) && CommonUtils.isNotNull(pkSettleCanc) && CommonUtils.isNotNull(setlId) && CommonUtils.isNotNull(reChargeSetlId)) {
				DataBaseHelper.execute("update ins_qgyb_st set pk_settle =?  where del_flag='0' and PK_PV=? and setl_id = ?", pkSettle, pkPv, reChargeSetlId);//重新结算
				DataBaseHelper.execute("update ins_qgyb_st set pk_settle =?  where del_flag='0' and PK_PV=? and setl_id = ?", pkSettleCanc, pkPv, setlId);//全退
				return;
			}
			// 收费
			if (CommonUtils.isNotNull(pkSettle) && CommonUtils.isNotNull(setlId)) {
				DataBaseHelper.execute("update ins_qgyb_st set pk_settle =?  where del_flag='0' and PK_PV=? and setl_id = ?", pkSettle, pkPv, setlId);
				return;
			}
			// 退费
			if (CommonUtils.isNotNull(pkSettleCanc) && CommonUtils.isNotNull(setlId)) {
				DataBaseHelper.execute("update ins_qgyb_st set pk_settle =?  where del_flag='0' and PK_PV=? and setl_id = ?", pkSettleCanc, pkPv, setlId);
				return;
			}
			/*if (!CommonUtils.isEmptyString(setlId)) {
				DataBaseHelper.execute("update ins_qgyb_st set pk_settle=? where del_flag='0' and PK_PV=? and setl_id = ?", pkSettle, pkPv, setlId);
			}*/
		} 
	}

	/**
	 * 门诊退费-预结算
	 * 015001013007->022003027023
	 * @param params
	 * @param user
	 * @return
	 */
	public Map<String, Object> PreReturnSettlement(String params, IUser user) {
		Map<String, Object> paramMap = JsonUtil.readValue(params, Map.class);
		// 查询医保登记信息
		InsQgybVisit visit = qryVisitInfo(CommonUtils.getString(paramMap.get("pkPv")));
		if (visit == null) {
			throw new BusException("医保预退费失败，未查询到对应的医保登记记录");
		}
		// 查询医保结算信息
		InsQgybSt insSt = DataBaseHelper.queryForBean("select * from ins_qgyb_st where pk_settle=? ", InsQgybSt.class, paramMap.get("pkSettle"));
		// 不退费费用明细
		List<BlOpDt> noCancleDt = noCancleDt(params);
		//无医保结算记录，为特需费用结算
		if (insSt == null ) {/**博爱无特需费用**/
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
				List<BlOpDt> allDts = zsbaQGMapper.qryChargeDts(reqMap);
				for (BlOpDt dt : allDts) {
					amtAdd= amtAdd + dt.getAmountAdd();
					if("96".equals(dt.getCodeBill())){
						amtSpec = amtSpec + dt.getAmount();	
					}		
				}
				dtAmt = amtAdd + amtSpec;
			}
			InsQgybPreSt preSt = new InsQgybPreSt();
			preSt.setAggregateAmount(dtAmt);
			preSt.setMedicarePayments(0d);
			preSt.setPatientsPay(dtAmt);
			return (Map<String, Object>) ApplicationUtils.beanToMap(preSt);
		}
		// 部分退医保逻辑
		if (noCancleDt != null && noCancleDt.size() > 0) {
			Map<String, Object> rePayIns = rePayIns(visit, noCancleDt, insSt, user, params);
			if (rePayIns != null) {
				return rePayIns;
			} else {
				throw new BusException("医保预退费失败，部分退返回信息错误");
			}
		}
		Map<String, Object> stMap = zsbaQGMapper.qryBlSt(paramMap);
		if (stMap != null) {
			return stMap;
		} else {
			throw new BusException("医保预退费失败，未查询到对应的结算记录");
		}
	}

	/*
	 * 部分退医保
	 * @param visit
	 * @param newOpList
	 * @param insSt
	 * @param user
	 * @param preParam
	 * @return
	 */
	private Map<String, Object> rePayIns(InsQgybVisit visit, List<BlOpDt> newOpList,InsQgybSt insSt, IUser user, String preParam) {

		Map<String, Object> reqMap = new HashMap<String, Object>();
		Map<String, Object> retMap = new HashMap<String, Object>();
		String pkPv = visit.getPkPv();
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
		List<Map<String, Object>> dts = zsbaQGMapper.qryChargeDetailNoUpload(reqMap);
		updateHerbalPresForHospApprFlag(dts);//根据中药饮片规则更新处方药品医院审批标志

		Double amtAdd = 0d;//特诊加收金额
		Double amtSpec = 0d;//特需费用
		//单条明细部分退，金额=单价*重收数量(博爱需要按最小包装量计算)
		for(BlOpDt newOp :newOpList){	
			for(Map<String, Object> dt :dts){
				if(newOp.getPkCgop().equals(dt.get("pkCgop"))){
					//单价
					if(newOp.getFlagPd().equals("1")){
						if(newOp.getPackSize()==1){
							dt.put("pric", (newOp.getAmount()-newOp.getAmountAdd())/newOp.getQuan());
						}else{
							dt.put("pric", ((newOp.getAmount()-newOp.getAmountAdd())/newOp.getQuan())/newOp.getPackSize());
						}
					}else{
						dt.put("pric", (newOp.getAmount()-newOp.getAmountAdd())/newOp.getQuan());
					}
					//数量
					if(newOp.getFlagPd().equals("1")){
						if(newOp.getPackSize()==1){
							dt.put("cnt", newOp.getQuan());
						}else{
							dt.put("cnt", (newOp.getQuan()*newOp.getPackSize()));
						}
					}else{
						dt.put("cnt", newOp.getQuan());
					}
					//总额
					if(newOp.getFlagPd().equals("1")){
						if(newOp.getPackSize()==1){
							dt.put("det_item_fee_sumamt", (newOp.getAmount()-newOp.getAmountAdd())/newOp.getQuan()*newOp.getQuan());
						}else{
							dt.put("det_item_fee_sumamt", ((newOp.getAmount()-newOp.getAmountAdd())/newOp.getQuan())/newOp.getPackSize()*(newOp.getQuan()*newOp.getPackSize()));
						}
					}else{
						dt.put("det_item_fee_sumamt", ((newOp.getAmount()-newOp.getAmountAdd())/newOp.getQuan())*newOp.getQuan());
					}
					
					/**博爱特诊加收部分在预结算方法增加**/
					/*dt.put("cnt", newOp.getQuan());
					dt.remove("detItemFeeSumamt");
					dt.put("det_item_fee_sumamt", MathUtils.mul(newOp.getQuan(), CommonUtils.getDoubleObject(dt.get("pric"))));
					if(newOp.getAmountAdd()>0){
						amtAdd = amtAdd + newOp.getQuan() * (newOp.getPrice() - newOp.getPriceOrg());
					}*/
				}
			}
			if("96".equals(newOp.getCodeBill())){/**博爱暂无特需费用**/
				amtSpec = amtSpec + newOp.getAmount();	
			}
		}

		InsQgybPreSt preSt = new InsQgybPreSt();
		preSt.setMedicarePayments(0d);
		preSt.setPatientsPay(amtSpec + amtAdd);
		preSt.setAggregateAmount(preSt.getPatientsPay());
		
		if (dts == null || dts.size() == 0) {
			return (Map<String, Object>) ApplicationUtils.beanToMap(preSt);
		}
		dts = ZsbaQGUtils.humpToLineListMap(dts);
		//撤销费用上传
		retMap.clear();
		retMap.put("pkPv", visit.getPkPv());
		mzBillDel(JsonUtil.writeValueAsString(reqMap), user);
		
		//上传本次就诊明细
		Map<String, Object> uplod = new HashMap<String, Object>();
		uplod.put("port", "2204");
		uplod.put("params", dts);
		uplod.put("chrg_bchno", chrgBchno);
		uplod.put("re_pay", "1");
		uplod.put("pk_cgops", pkCgs);
		uplod.put("pk_pv", pkPv);	
		retStr = getHealthInsuranceEntry(uplod, user, pkPv);
		retMap = JsonUtil.readValue(retStr, Map.class);
		if (!"0".equals(CommonUtils.getString(retMap.get("infcode")))) {
			Log.error(CommonUtils.getString(retMap.get("err_msg")));
			throw new BusException("上传费用明细失败，失败原因：" + CommonUtils.getString(retMap.get("err_msg")));
		}

		retMap = (Map<String, Object>) retMap.get("output");
		retStr = JsonUtil.writeValueAsString(retMap.get("result"));
		List<InsQgybCg> cgs = JsonUtil.readValue(retStr, new TypeReference<List<InsQgybCg>>() {});
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
			//由于测试中发现定价上限金额会出现很大的情况导致值变成科学计数法后入库会出现格式转换的错误，所以暂时对这个字段做下转换处理
			for (InsQgybCg insQgbCg : cgs) {
				String pricUplmtAmt = new BigDecimal(insQgbCg.getPricUplmtAmt()).setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString();
				insQgbCg.setPricUplmtAmt(pricUplmtAmt);
			}
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
		retStr = getHealthInsuranceEntry(reqMap, user, pkPv);

		retMap = JsonUtil.readValue(retStr, Map.class);
		if ("0".equals(CommonUtils.getString(retMap.get("infcode")))) {

			retMap = (Map<String, Object>) ((Map<String, Object>) retMap.get("output")).get("setlinfo");

			OutParamHuaJia outParamHuaJia = JsonUtil.readValue(JsonUtil.writeValueAsString(retMap), OutParamHuaJia.class);
			outParamHuaJia.setAmount(outParamHuaJia.getMedfeeSumamt());		// 总金额
			outParamHuaJia.setAmtGrzf(outParamHuaJia.getPsnCashPay());		// 现金
			outParamHuaJia.setAmtJjzf(outParamHuaJia.getFundPaySumamt());	// 基金

			preSt.setPreIntoParam(preParam); 			// 退费预结算入参
			preSt.setyBPreIntoParam(reqMap);			// 退费预结算 医保入参
			preSt.setYbPreSettleInfo(outParamHuaJia);	// 退费预结算 医保入参

			//preSt.setAggregateAmount(preSt.getAggregateAmount()+outParamHuaJia.getMedfeeSumamt());
			preSt.setPatientsPay(preSt.getPatientsPay() + outParamHuaJia.getPsnCashPay());
			preSt.setMedicarePayments(outParamHuaJia.getFundPaySumamt());
			preSt.setAggregateAmount(preSt.getPatientsPay() + preSt.getMedicarePayments());
			return (Map<String, Object>) ApplicationUtils.beanToMap(preSt);

		} else {
			logger.error(retStr);
			throw new BusException("医保预结算失败：" + CommonUtils.getString(retMap.get("err_msg")));
		}

	}
	
	/**
	 * 医保退费
	 * @param params	ybPksettle = setl_id
	 * @param user
	 * @return
	 */
	public Map<String, Object> insSettleCancle(String params, IUser user) {
		Map<String, Object> paramMap = JsonUtil.readValue(params, Map.class);
		if (!CommonUtils.isNotNull(paramMap.get("ybPkSettle"))){
			Log.error("医保退费失败,参数：" + params);
			throw new BusException("医保退费失败，未获取到结算ID");
		}
		InsQgybSt insQgybSt = DataBaseHelper.queryForBean("select * from ins_qgyb_st where setl_id= ? ", InsQgybSt.class, paramMap.get("ybPkSettle"));
		Map<String, Object> retMap = new HashMap<String, Object>();
		if (insQgybSt == null) {
			Log.error("医保退费失败,结算ID未获取到结算记录：" + params);
			throw new BusException("医保退费失败，结算ID未获取到结算记录"+paramMap.get("ybPksettle"));
		}
		
		/*
		 *  his结算失败 医保退费
		 */
		Map<String, Object> reqMap = new HashMap<String, Object>();
		reqMap.put("port", "2208");
		reqMap.put("mdtrt_id", insQgybSt.getMdtrtId());
		reqMap.put("psn_no", insQgybSt.getPsnNo());
		reqMap.put("setl_id", insQgybSt.getSetlId());
		String retStr = getHealthInsuranceEntry(reqMap, user, insQgybSt.getPkPv());

		retMap = JsonUtil.readValue(retStr, Map.class);
		if (!"0".equals(CommonUtils.getString(retMap.get("infcode")))) {
			if(CommonUtils.getString(retMap.get("err_msg")).contains("该笔结算已撤销，请勿重复办理")){
				retMap.clear();
				InsQgybSt insSt = DataBaseHelper.queryForBean("select * from ins_qgyb_st where SETL_ID_CANCEL=? and del_flag='0' ", InsQgybSt.class, insQgybSt.getSetlId());
				retMap.put("ybPksettle", paramMap.get("ybPkSettle"));
				retMap.put("amtJjzf", insSt.getFundPaySumamt());
				return retMap;
			} else {
				Log.error("功能号2208，医保退费失败" + CommonUtils.getString(retMap.get("err_msg")));
				throw new BusException("功能号2208，医保退费失败" + CommonUtils.getString(retMap.get("err_msg")));
			}
		}
			
		retMap = (Map<String, Object>) ((Map<String, Object>) retMap.get("output")).get("setlinfo");
		InsQgybSt insStCancel = JsonUtil.readValue(JsonUtil.writeValueAsString(retMap), InsQgybSt.class);
		
		//insStCancel.setAmtGrzhzf(insStCancel.getAcctPay());		// 个人账户
		insStCancel.setAmount(insStCancel.getMedfeeSumamt());		// 总金额
		insStCancel.setAmtJjzf(insStCancel.getFundPaySumamt());		// 基金
		insStCancel.setAmtGrzf(insStCancel.getPsnCashPay());		// 现金

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
		/**20210518查询如果存在相同医保结算主键的记录则不再重复插入**/
		InsQgybSt insSt = DataBaseHelper.queryForBean("select * from ins_qgyb_st where YB_PKSETTLE=?", InsQgybSt.class, insStCancel.getYbPksettle());
		if(insSt==null){
			insertInsSt(insStCancel);
		}
		DataBaseHelper.execute("update INS_QGYB_ST set del_flag='1' where pk_pv=? and setl_id =?", insStCancel.getPkPv(), insStCancel.getSetlId());
		return (Map<String, Object>) ApplicationUtils.beanToMap(insStCancel);
	}
	
	/*
	 * 取部分退的数据
	 * @param params
	 * @return
	 */
	private List<BlOpDt> noCancleDt(String params) {
		Map<String, Object> paramMap = JsonUtil.readValue(params, Map.class);

		// 查询本次结算的的所有明细
		List<BlOpDt> allList = DataBaseHelper.queryForList(
				"select * from bl_op_dt where pk_pv=? and pk_settle =? and del_flag='0' ", BlOpDt.class, paramMap.get("pkPv"), paramMap.get("pkSettle"));

		JsonNode jsonnode = JsonUtil.getJsonNode(params, "returnPkcgop");
		if (jsonnode == null) {
			return null;
		}

		List<BlOpDt> returnPkcgop = JsonUtil.readValue(jsonnode, new TypeReference<List<BlOpDt>>() {});
		List<BlOpDt> newOpList = new ArrayList<BlOpDt>();
		for (BlOpDt opdt : allList) {
			boolean flag = false;
			for (BlOpDt oldDt : returnPkcgop) {
				if (opdt.getPkCgop().equals(oldDt.getPkCgop())) {
					if(opdt.getQuan().equals(oldDt.getQuanBack())) {
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
	
	/*
	 * 插入医保结算表 
	 * @param insSt
	 */
	private void insertInsSt(InsQgybSt insSt) {
		DefaultTransactionDefinition def = new DefaultTransactionDefinition();
		def.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRES_NEW);
		TransactionStatus status = platformTransactionManager.getTransaction(def);
		try {
			DataBaseHelper.insertBean(insSt);
			platformTransactionManager.commit(status); // 提交事务
		} catch (Exception e) {
			platformTransactionManager.rollback(status); // 添加失败 回滚事务；
			e.printStackTrace();
			throw new BusException("保存医保结算信息失败，请重试：" + e);
		}
	}
	
	/*
	 * 插入医保日志 
	 * @param log
	 */
	private void insertLog(InsQgybLog log){
		DefaultTransactionDefinition def = new DefaultTransactionDefinition();
		def.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRES_NEW);
		TransactionStatus status = platformTransactionManager.getTransaction(def);
		try {
			DataBaseHelper.insertBean(log);
			platformTransactionManager.commit(status); // 提交事务
		} catch (Exception e) {
			platformTransactionManager.rollback(status); // 添加失败 回滚事务；
			e.printStackTrace();
			throw new BusException("保存医保结算信息失败，请重试：" + e);
		}
	}
	
	/**
	 * 慢特病备案记录保存
	 * 015001013030->022003027024
	 * @param params
	 * @param user
	 */
	public void saveRecordMt(String params, IUser user){
		Map<String, Object> paramMap = JsonUtil.readValue(params, Map.class);
		paramMap.put("port", "2503");
		String ret = getHealthInsuranceEntry(paramMap, user, null);
		Map<String, Object> retMap = JsonUtil.readValue(ret, Map.class);
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
	 * 慢特病备案记录取消
	 * 015001013031->022003027025
	 * @param params
	 * @param user
	 */
	public void cancelRecordMt(String params, IUser user){
		Map<String, Object> paramMap = JsonUtil.readValue(params, Map.class);
		if (paramMap.get("trt_dcla_detl_sn") != null) {
			paramMap.put("port", "2504");
			String ret = getHealthInsuranceEntry(paramMap, user, null);
			Map<String, Object> retMap = JsonUtil.readValue(ret, Map.class);
			if ("0".equals(CommonUtils.getString(retMap.get("infcode")))) {			
				InsQgybPidise piDise=DataBaseHelper.queryForBean(
						"select * from ins_qgyb_pidise where trt_dcla_detl_sn=? and del_flag='0'", InsQgybPidise.class, paramMap.get("trt_dcla_detl_sn").toString());
				if(piDise!=null){
					piDise.setFlagCanc("1");
					piDise.setCancelTime(new Date());
					User userInfo = (User) user;
					piDise.setCancelor(userInfo.getUserName());
					DataBaseHelper.updateBeanByPk(piDise);
				}
			} else {
				throw new BusException("功能号2504，慢特病备案失败：" + CommonUtils.getString(retMap.get("err_msg")));
			}
		} else {
			throw new BusException("功能号2504，慢特病备案失败：未获取到 待遇申报明细流水号trt_dcla_detl_sn");
		}
	}
	
	/**
	 * 慢特病备案记录查询
	 * 015001013029->022003027026
	 * @param params
	 * @param user
	 * @return
	 */
	public Map<String,Object> qryRecordMt(String params, IUser user){
		Map<String, Object> paramMap = JsonUtil.readValue(params, Map.class);
		paramMap = ZsbaQGUtils.humpToMap(paramMap);
		if (paramMap.get("psn_no") != null) {
			paramMap.put("port", "5301");
			String ret = getHealthInsuranceEntry(paramMap, user, null);

			Map<String, Object> retMap = JsonUtil.readValue(ret, Map.class);
			if ("0".equals(CommonUtils.getString(retMap.get("infcode")))) {
				List<InsQgybPidise> piDiseList=DataBaseHelper.queryForList(
						"select * from ins_qgyb_pidise where psn_no=? and del_flag='0'", InsQgybPidise.class,paramMap.get("psn_no").toString());
				
				List<Map<String, Object>> ins=  (List<Map<String, Object>>) ((Map<String,Object>)retMap.get("output")).get("feedetail");
				Map<String,Object> map = new HashMap<String, Object>(); 
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
	 * 
	 * 慢特病备案记录查询(全部)
	 * 交易号：022003027134
	 */
	public List<Map<String,Object>> qryRecordMtList(String param, IUser user) {
		Map<String, Object> paramMap = JsonUtil.readValue(param, Map.class);
		List<Map<String,Object>> list = zsbaQGMapper.qryRecordMtList(paramMap);
		return list;
	}
	
	/**
	 * 人员定点记录查询
	 * 015001013032->022003027027
	 * @param params
	 * @param user
	 * @return
	 */
	public List<Map<String,Object>> qryRecordDd(String params, IUser user){
		Map<String, Object> paramMap = JsonUtil.readValue(params, Map.class);
		if (paramMap.get("psn_no") != null) {
			paramMap.put("port", "5302");
			String ret = getHealthInsuranceEntry(paramMap, user, null);

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
	
	/**
	 * 参保缴费查询
	 * 015001013034->022003027161
	 * @param param
	 * @param user
	 */
	public List<Map<String, Object>> qryInsFees(String params, IUser user){
		Map<String, Object> paramMap = JsonUtil.readValue(params, Map.class);
		paramMap = ZsrmQGUtils.humpToMap(paramMap);
		if (paramMap.get("psn_no") != null) {
			paramMap.put("port", "90100");
			String ret = getHealthInsuranceEntry(paramMap, user, null);

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
	 * 文件上传
	 * @param file
	 * @param user
	 * @return
	 */
	public String fileUpload(MultipartFile file, IUser user) {
		String rs = "";
		Map<String, String> map = this.getHeaderElement();
		try {
			rs = HttpClientUtil.sendHttpPost(HSA_URL + "9101", file, FIXMEDINS_CODE, map);
		} catch (IOException e) {
			e.printStackTrace();
		}
		logger.info("出参参数：" + rs);
		return rs;
	}

	/**
	 * 请求数据处理，发起请求，返回结果
	 * @param paramMap
	 * @param user
	 * @return
	 */
	public String getHealthInsuranceEntry(Map<String, Object> paramMap, IUser user, String pkPv) {
		String port = paramMap.get("port").toString();
		Map<String, String> map = this.getHeaderElement();
		String jsonStr = "";
		
		// TODO：2021-07-09 lipz 加入参保地区划查询
		String insuplcAdmdvs = "";
		if(paramMap.containsKey("insuplc_admdvs") && paramMap.get("insuplc_admdvs")!=null 
				&& StringUtils.isNotEmpty(paramMap.get("insuplc_admdvs").toString())) {
			insuplcAdmdvs = paramMap.get("insuplc_admdvs").toString();
		}
		if(StringUtils.isNotEmpty(pkPv) && StringUtils.isEmpty(insuplcAdmdvs)) {
			InsQgybVisit insVisit = qryVisitInfo(pkPv);
			if(insVisit!=null && insVisit.getInsuplcAdmdvs()!=null && StringUtils.isNotEmpty(insVisit.getInsuplcAdmdvs().trim())) {
				insuplcAdmdvs = insVisit.getInsuplcAdmdvs().trim();
			}
		}

		// 清空不必要的参数
		if(paramMap.containsKey("pk_pi")){
			paramMap.remove("pk_pi");
		}
		if(paramMap.containsKey("pk_pv")){
			paramMap.remove("pk_pv");
		}
		if(paramMap.containsKey("insuplc_admdvs")){
			paramMap.remove("insuplc_admdvs");
		}
		paramMap.remove("port");
		
		// 根据功能号处理入参
		switch (port) {
			/* 获取人员信息 */
			case "1101":
				jsonStr = getJsonStr(paramMap, "data", port, user, insuplcAdmdvs);
				break;
			/* 获取字典 */
			case "1901":
				jsonStr = getJsonStr(paramMap, "data", port, user, insuplcAdmdvs);
				break;
			/* 签到 */
			case "9001":
				jsonStr = getJsonStr(paramMap, "signIn", port, user, insuplcAdmdvs);
				break;
			/* 签退 */
			case "9002":
				jsonStr = getJsonStr(paramMap, "signOut", port, user, insuplcAdmdvs);
				break;
			/* 门诊挂号 */
			case "2201":
				jsonStr = getJsonStr(paramMap, "data", port, user, insuplcAdmdvs);
				break;
			/* 门诊挂号撤销 */
			case "2202":
				jsonStr = getJsonStr(paramMap, "data", port, user, insuplcAdmdvs);
				break;
			/* 门诊就诊信息上传 */
			case "2203A":
				JSONObject jsonObject = JSONObject.fromObject(paramMap);
				jsonStr = getJsonStr(jsonObject, port, user, insuplcAdmdvs);
				break;
			/* 门诊费用明细信息上传 */
			case "2204":
				jsonStr = getJsonStr(paramMap.get("params"), "feedetail", port, user, insuplcAdmdvs);
				break;
			/* 门诊费用明细信息撤销 */
			case "2205":
				jsonStr = getJsonStr(paramMap, "data", port, user, insuplcAdmdvs);
				break;
			/* 门诊预结算 */
			case "2206":
				jsonStr = getJsonStr(paramMap, "data", port, user, insuplcAdmdvs);
				break;
			/* 门诊结算 */
			case "2207":
				jsonStr = getJsonStr(paramMap, "data", port, user, insuplcAdmdvs);
				break;
			/* 门诊结算撤销 */
			case "2208":
				jsonStr = getJsonStr(paramMap, "data", port, user, insuplcAdmdvs);
				break;
			/* 待遇检查 */
			case "2001":
				jsonStr = getJsonStr(paramMap, "data", port, user, insuplcAdmdvs);
				break;
			/* 人员累计信息查询 */
			case "5206":
				jsonStr = getJsonStr(paramMap, "data", port, user, insuplcAdmdvs);
				break;
			/* 科室信息上传 */
			case "3401":
				jsonStr = getJsonStr(paramMap, "deptinfo", port, user, insuplcAdmdvs);
				break;
			/* 科室信息变更 */
			case "3402":
				jsonStr = getJsonStr(paramMap, "deptinfo", port, user, insuplcAdmdvs);
				break;
			/* 科室信息撤销 */
			case "3403":
				jsonStr = getJsonStr(paramMap, "data", port, user, insuplcAdmdvs);
				break;
			/* 核对总账 */
			case "3201":
				jsonStr = getJsonStr(paramMap, "data", port, user, insuplcAdmdvs);
				break;
			/* 医药机构费用结算对明细账 */
			case "3202":
				jsonStr = getJsonStr(paramMap, "data", port, user, insuplcAdmdvs);
				break;
			/* 目录对照上传 */
			case "3301":
				jsonStr = getJsonStr(paramMap.get("params"), "data", port, user, insuplcAdmdvs);
				break;
			/* 目录对照撤销 */
			case "3302":
				jsonStr = getJsonStr(paramMap, "catalogcompin", port, user, insuplcAdmdvs);
				break;
			/* 冲正交易 */
			case "2601":
				jsonStr = getJsonStr(paramMap, "data", port, user, insuplcAdmdvs);
				break;
			/* 医疗目录与医保目录匹配信息查询 */
			case "1317":
				jsonStr = getJsonStr(paramMap, "data", port, user, insuplcAdmdvs);
				break;
			case "9101":
				jsonStr = getJsonStr(paramMap, "fsUploadIn", port, user, insuplcAdmdvs);
				break;
			/* 慢特病备案 */	
			case "2503":
				jsonStr = getJsonStr(paramMap, "data", port, user, insuplcAdmdvs);
				break;	
			/* 慢特病备案撤销 */	
			case "2504":
				jsonStr = getJsonStr(paramMap, "data", port, user, insuplcAdmdvs);
				break;
			/* 慢特病备案查询 */
			case "5301":
				jsonStr = getJsonStr(paramMap, "data", port, user, insuplcAdmdvs);
				break;
			/* 人员定点信息查询 */
			case "5302":
				jsonStr = getJsonStr(paramMap, "data", port, user, insuplcAdmdvs);
				break;
			/* 缴费查询接口 */
			case "90100":
				jsonStr = getJsonStr(paramMap, "data", port, user, insuplcAdmdvs);
				break;
			default:
				break;
		}
		
		// 发起请求
		String rs = "";
		try {
			logger.info(port + "入参参数：" + jsonStr);
			FileUtils.writerAndCreateFile("\n"+DateUtils.getDateTime()+" 入参：医保功能号："+port + "\n"+jsonStr, "mzyb-"+UserContext.getUser().getCodeEmp()+".txt");
			rs = HttpClientUtil.sendHttpPost(HSA_URL + port, jsonStr, map);
			logger.info(port + "出参参数：" + rs);
			FileUtils.writerAndCreateFile("\n"+DateUtils.getDateTime()+" 出参：医保功能号："+port + "\n"+rs, "mzyb-"+UserContext.getUser().getCodeEmp()+".txt");
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
	
	/*
	 * 将Map转json格式字符串
	 * @param paramMap
	 * @param key
	 * @param interfaceNO
	 * @param user
	 * @return
	 */
	private String getJsonStr(Map<String, Object> paramMap, String key, String interfaceNO, IUser user, String insuplcAdmdvs) {
		User userInfo = (User) user;
		/**后台集团管理员测试调用时这两个属性为空**/
		if(StringUtils.isEmpty(userInfo.getCodeEmp())){
			userInfo.setCodeEmp("00000");
		}
		if(StringUtils.isEmpty(userInfo.getNameEmp())){
			userInfo.setNameEmp("集团管理员");
		}

		JSONObject jsonObject = new JSONObject();
		JSONObject obj = new JSONObject();
		JSONObject params = new JSONObject();

		for (Map.Entry<String, Object> entry : paramMap.entrySet()) {
			params.put(entry.getKey(), entry.getValue());
		}

		obj.put(key, params);
		jsonObject.put("recer_sys_code", "01");
		jsonObject.put("msgid", FIXMEDINS_CODE + DateUtils.dateToStr("yyyyMMddHHmmss", new Date()) + getSNcode());
		jsonObject.put("dev_no", "");
		jsonObject.put("dev_safe_info", "");
		jsonObject.put("signtype", "SM3");
		jsonObject.put("cainfo", "");
		jsonObject.put("inf_time", DateUtils.dateToStr("yyyy-MM-dd HH:mm:ss", new Date()) );
		jsonObject.put("input", obj);
		/* 经办人类别 1-经办人；2-自助终端；3-移动终端 */
		jsonObject.put("opter_type", (StringUtils.isNotEmpty(userInfo.getCodeEmp()) && userInfo.getCodeEmp().startsWith("999"))?"2":"1");
		jsonObject.put("opter_name", userInfo.getNameEmp());
		jsonObject.put("opter", userInfo.getCodeEmp());
		jsonObject.put("insuplc_admdvs", StringUtils.isNotEmpty(insuplcAdmdvs)?insuplcAdmdvs:"442000");//参保地医保区划,为空时默认使用中山
		jsonObject.put("mdtrtarea_admvs", "442000");//就医地医保区划
		jsonObject.put("recer_admvs", "442000");/* 接收方医保区划代码 */
		jsonObject.put("infver", VERSION);
		jsonObject.put("infno", interfaceNO);
		jsonObject.put("sign_no", "");
		jsonObject.put("fixmedins_name", FIXMEDINS_NAME);
		jsonObject.put("fixmedins_code", FIXMEDINS_CODE);
		return jsonObject.toString();
	}

	/*
	 * 将JSONObject转json格式字符串
	 * @param paramObj
	 * @param interfaceNO
	 * @param user
	 * @return
	 */
	private String getJsonStr(JSONObject paramObj, String interfaceNO, IUser user,String insuplcAdmdvs) {
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
		jsonObject.put("opter_type", userInfo.getCodeEmp().startsWith("999")?"2":"1");
		jsonObject.put("opter_name", userInfo.getNameEmp());
		jsonObject.put("opter", userInfo.getCodeEmp());
		jsonObject.put("insuplc_admdvs", StringUtils.isNotEmpty(insuplcAdmdvs)?insuplcAdmdvs:"442000");//参保地医保区划,为空时默认使用中山
		jsonObject.put("mdtrtarea_admvs", "442000");//就医地医保区划
		jsonObject.put("recer_admvs", "442000");/* 接收方医保区划代码 */
		jsonObject.put("infver", VERSION);
		jsonObject.put("infno", interfaceNO);
		jsonObject.put("sign_no", "");
		jsonObject.put("fixmedins_name", FIXMEDINS_NAME);
		jsonObject.put("fixmedins_code", FIXMEDINS_CODE);

		return jsonObject.toString();
	}

	/*
	 * 将Object转json格式字符串
	 * @param param
	 * @param key
	 * @param interfaceNO
	 * @param user
	 * @return
	 */
	private String getJsonStr(Object param, String key, String interfaceNO, IUser user,String insuplcAdmdvs) {
		User userInfo = (User) user;
		
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
		jsonObject.put("opter_type", userInfo.getCodeEmp().startsWith("999")?"2":"1");
		jsonObject.put("opter_name", userInfo.getNameEmp());
		jsonObject.put("opter", userInfo.getCodeEmp());
		jsonObject.put("insuplc_admdvs", StringUtils.isNotEmpty(insuplcAdmdvs)?insuplcAdmdvs:"442000");//参保地医保区划,为空时默认使用中山
		jsonObject.put("mdtrtarea_admvs", "442000");//就医地医保区划
		jsonObject.put("recer_admvs", "442000");/* 接收方医保区划代码 */
		jsonObject.put("infver", VERSION);
		jsonObject.put("infno", interfaceNO);
		jsonObject.put("sign_no", "");
		jsonObject.put("fixmedins_name", FIXMEDINS_NAME);
		jsonObject.put("fixmedins_code", FIXMEDINS_CODE);

		return jsonObject.toString();
	}

	/*
	 * 获取请求头数据
	 * @return
	 */
	private Map<String, String> getHeaderElement() {
		Map<String, String> map = new HashMap<String, String>();
		map.put("x-tif-paasid", HSA_PAASID);
		map.put("x-tif-nonce", RandomStringUtils.random(6, true, true));
		map.put("x-tif-timestamp", Long.toString(ZsbaQGUtils.getCurrentUnixSeconds()));
		map.put("x-tif-signature", ZsbaQGUtils.getSHA256Str(map.get("x-tif-timestamp") + SECRETKEY + map.get("x-tif-nonce") + map.get("x-tif-timestamp")));
		return map;
	}
	
	/*
	 * 截取0607医保交互顺序号后四位
	 * @return
	 */
	private String getSNcode() {
		String SNcode = ApplicationUtils.getCode("0607");
		if (SNcode != null && SNcode.length() >= 4) {
			SNcode = SNcode.substring(SNcode.length() - 4, SNcode.length());
			return SNcode;
		} else {
			throw new BusException("根据编码规则【0607】未获取的有效的医保顺序号SNcode");
		}
	}
	


}