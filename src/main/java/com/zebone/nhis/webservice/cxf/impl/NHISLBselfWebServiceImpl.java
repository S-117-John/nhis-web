package com.zebone.nhis.webservice.cxf.impl;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.sql.Blob;
import java.util.*;

import com.zebone.nhis.common.module.base.bd.srv.BdItem;
import com.zebone.nhis.common.module.base.bd.srv.BdItemPrice;
import com.zebone.nhis.common.module.bl.BlDeposit;
import com.zebone.nhis.common.module.bl.opcg.BlOpDt;
import com.zebone.nhis.common.module.mybatis.MyBatisPage;
import com.zebone.nhis.common.module.pay.BlExtPay;
import com.zebone.nhis.common.module.pi.PiAcc;
import com.zebone.nhis.common.module.pi.PiMaster;
import com.zebone.nhis.common.module.sch.plan.SchTicket;
import com.zebone.nhis.common.support.ApplicationUtils;
import com.zebone.nhis.common.support.BeanUtils;
import com.zebone.nhis.common.support.CommonUtils;
import com.zebone.nhis.common.support.DateUtils;
import com.zebone.nhis.common.support.EnumerateParameter;
import com.zebone.nhis.common.support.MathUtils;
import com.zebone.nhis.common.support.XmlUtil;
import com.zebone.nhis.ma.pub.platform.pskq.utils.MapUtils;
import com.zebone.nhis.webservice.cxf.INHISLBselfWebService;
import com.zebone.nhis.webservice.dao.BdPubForWsMapper;
import com.zebone.nhis.webservice.dao.CnPubForWsMapper;
import com.zebone.nhis.webservice.dao.SchPubForWsMapper;
import com.zebone.nhis.webservice.dao.BlPubForWsMapper;
import com.zebone.nhis.webservice.dao.PvPubForWsMapper;
import com.zebone.nhis.webservice.service.BlPubForWsService;
import com.zebone.nhis.webservice.service.InvPubForWsService;
import com.zebone.nhis.webservice.service.LbPubForWsService;
import com.zebone.nhis.webservice.service.PvPubForWsService;
import com.zebone.nhis.webservice.service.SchPubForWsService;
import com.zebone.nhis.webservice.support.Constant;
import com.zebone.nhis.webservice.support.LbSelfUtil;
import com.zebone.nhis.webservice.support.OtherRespJson;
import com.zebone.nhis.webservice.vo.*;
import com.zebone.platform.common.support.NHISUUID;
import com.zebone.platform.common.support.User;
import com.zebone.platform.common.support.UserContext;
import com.zebone.platform.modules.dao.DataBaseHelper;
import com.zebone.platform.modules.utils.JsonUtil;
import com.zebone.platform.web.support.ResponseJson;

import javax.annotation.Resource;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import javax.sql.rowset.serial.SerialBlob;

import oracle.sql.BLOB;
import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @Classname NHISLBselfWebServiceImpl
 * @Description TODO
 * @Date 2019/8/2 10:12
 * @Created by wuqiang
 */
@WebService
@SOAPBinding(style = SOAPBinding.Style.RPC)
public class NHISLBselfWebServiceImpl implements INHISLBselfWebService {

	@Resource
	private BlPubForWsService blPubForWsService;
	@Resource
	private BdPubForWsMapper bdPubForWsMapper;
	@Autowired
	private SchPubForWsMapper schMapper;
	@Resource
	private SchPubForWsService schPubForWsService;
	@Resource
	private PvPubForWsMapper pvPubForWsMapper;
	@Resource
	private BlPubForWsMapper blPubForWsMapper;
	@Resource
	private PvPubForWsService pvPubForWsService;
	@Resource
	private InvPubForWsService invPubForWsService;
	@Resource
	private LbPubForWsService lbPubForWsService;
	@Autowired
	private CnPubForWsMapper cnPubForWsMapper;

	/**
	 * 公共服务-结算业务
	 *
	 */
	private Logger logger = LoggerFactory.getLogger("nhis.lbWebServiceLog");
	ApplicationUtils apputil = new ApplicationUtils();

	@Override
	public String QueryHospitalInfo(String param) {
		// TODO Auto-generated method stub
		// 医院信息
		BdOuOrgVo bdOuOrg = new BdOuOrgVo();
		try {
			// 医院信息
			List<Map<String, Object>> orgMap = bdPubForWsMapper.LbqueryOrgs();

			for (int i = 0; i < orgMap.size(); i++) {
				if (("0101").equals(orgMap.get(i).get("codeOrg"))) {
					bdOuOrg.setCodeOrg(orgMap.get(i).get("codeOrg").toString());
					bdOuOrg.setNameOrg(orgMap.get(i).get("nameOrg").toString());
					break;
				}
			}
			orgMap = null;
		} catch (Exception e) {
			return XmlUtil.beanToXml(LbSelfUtil.common(Constant.RESFAIL, "失败"),LbSHResponseVo.class, false);
		}

		bdOuOrg.setResVo(LbSelfUtil.commonVo(Constant.RESSUC, "成功"));

		return XmlUtil.beanToXml(bdOuOrg, bdOuOrg.getClass(), false);
	}

	@Override
	public String QueryDeptInfo(String param) {
		logger.info("QueryDeptInfo查询科室信息接口："+param);
		// TODO Auto-generated method stub
		LbSHRequestVo requ = (LbSHRequestVo) XmlUtil.XmlToBean(param,
				LbSHRequestVo.class);
		Map<String, Object> reqMap = new HashMap<String ,Object>();
		if (StringUtils.isNotBlank(param)) {
			requ = (LbSHRequestVo) XmlUtil.XmlToBean(param, LbSHRequestVo.class);
			if(CommonUtils.isNotNull(requ.getPyCode())){
				reqMap.put("pyCode", requ.getPyCode());
			}
			if(CommonUtils.isNotNull(requ.getDeptCode())){
				reqMap.put("codeDept", requ.getDeptCode());
			}
		}

		List<BdOuDeptVo> ListDept = new ArrayList<>();
		// 响应结果
		LbSHResponseVo responseVo = new LbSHResponseVo();
		reqMap.put("flagOp", "1");
		try {
			List<Map<String, Object>> deptList = bdPubForWsMapper.queryDept(reqMap);

			for (int i = 0; i < deptList.size(); i++) {
				if (deptList.get(i).get("codeDept").toString().length() > 2) {
					// 科室信息
					BdOuDeptVo dept = new BdOuDeptVo();
					dept.setCodeDept(LbSelfUtil.getPropValueStr(deptList.get(i),
							"codeDept"));
					dept.setNameDept(LbSelfUtil.getPropValueStr(deptList.get(i),
							"nameDept"));
					dept.setPyCode(LbSelfUtil.getPropValueStr(deptList.get(i),
							"pyCode"));
					dept.setDeptDesc(LbSelfUtil.getPropValueStr(deptList.get(i),
							"deptDesc"));
					ListDept.add(dept);
				}
			}
			// 添加结果集到响应实体
			responseVo.setDeptList(ListDept);
		} catch (Exception e) {
			logger.error("查询科室信息接口：{}",e);
			return XmlUtil.beanToXml(LbSelfUtil.common(Constant.RESFAIL, "失败"), LbSHResponseVo.class, false);
		}
		responseVo.setCommonVo(LbSelfUtil.commonVo(Constant.RESSUC, "成功"));
		logger.info("查询科室信息接口："+XmlUtil.beanToXml(responseVo, responseVo.getClass(), false));
		return XmlUtil.beanToXml(responseVo, responseVo.getClass(), false);
	}

	@Override
	public String QueryDoctorInfos(String param) {
		logger.info("查询医生信息接口{}",param);
		LbSHRequestVo req = (LbSHRequestVo) XmlUtil.XmlToBean(param,LbSHRequestVo.class);
		//LbQueDocInfoReqVo req = (LbQueDocInfoReqVo) XmlUtil.XmlToBean(param,LbQueDocInfoReqVo.class);
		Map<String, Object> paramMap = LbSelfUtil.toMap(req);
		String resXml=null;
		// 响应结果3
	    LbSHResponseVo responseVo = new LbSHResponseVo();
		try {
			List<Map<String, Object>> queryEmployee = bdPubForWsMapper
					.queryEmployee(paramMap);
			if (queryEmployee != null) {
				List<LbQuedocinfoResTemVo> itemlist = new ArrayList<>();
				for (Map<String, Object> map : queryEmployee) {
					LbQuedocinfoResTemVo lbRestemVo = ApplicationUtils.mapToBean(map,LbQuedocinfoResTemVo.class);

					itemlist.add(lbRestemVo);
				}
				responseVo.setResTemList(itemlist);
			}
		} catch (Exception e) {
			logger.info("查询医生信息接口："+e);
			return XmlUtil.beanToXml(LbSelfUtil.common(Constant.RESFAIL, "未获取到自助机编码"), LbSHResponseVo.class, false);
		}
		responseVo.setCommonVo(LbSelfUtil.commonVo(Constant.RESSUC, "成功"));
		resXml=XmlUtil.beanToXml(responseVo, responseVo.getClass(), false);
		logger.info("查询医生信息接口{}",resXml);
		return resXml;
	}

	@Override
	public String queryAllDoctorInfos(String param) {
		logger.info("查询医生信息接口{}",param);
		LbSHRequestVo req = (LbSHRequestVo) XmlUtil.XmlToBean(param,LbSHRequestVo.class);
		//LbQueDocInfoReqVo req = (LbQueDocInfoReqVo) XmlUtil.XmlToBean(param,LbQueDocInfoReqVo.class);
		Map<String, Object> paramMap = LbSelfUtil.toMap(req);
		String resXml=null;
		// 响应结果3
		LbSHResponseVo responseVo = new LbSHResponseVo();
		try {
			List<Map<String, Object>> queryEmployee = bdPubForWsMapper
					.queryAllEmployee(paramMap);
			if (queryEmployee != null) {
				List<LbQuedocinfoResTemVo> itemlist = new ArrayList<>();
				for (Map<String, Object> map : queryEmployee) {
					LbQuedocinfoResTemVo lbRestemVo = ApplicationUtils.mapToBean(map,LbQuedocinfoResTemVo.class);

					itemlist.add(lbRestemVo);
				}
				responseVo.setResTemList(itemlist);
			}
		} catch (Exception e) {
			logger.info("查询医生信息接口："+e);
			return XmlUtil.beanToXml(LbSelfUtil.common(Constant.RESFAIL, "未获取到自助机编码"), LbSHResponseVo.class, false);
		}
		responseVo.setCommonVo(LbSelfUtil.commonVo(Constant.RESSUC, "成功"));
		resXml=XmlUtil.beanToXml(responseVo, responseVo.getClass(), false);
		logger.info("查询医生信息接口{}",resXml);
		return resXml;
	}

	@Override
	public String QuerySchDept(String param) {
		logger.info("排班科室查询："+param);
		LbSHRequestVo requ = null;
		DateTime dt = DateTime.now();
		// 响应结果
		LbSHResponseVo responseVo = new LbSHResponseVo();
		if (null != param && !("").equals(param)) {
			requ = (LbSHRequestVo) XmlUtil.XmlToBean(param, LbSHRequestVo.class);
			if(StringUtils.isBlank(requ.getDeviceid())){
				return XmlUtil.beanToXml(LbSelfUtil.common(Constant.RESFAIL, "未获取到自助机编码"), LbSHResponseVo.class, false);
			}
		} else{
			return XmlUtil.beanToXml(LbSelfUtil.common(Constant.RESFAIL, "未获取到信息"), LbSHResponseVo.class, false);
		}

		User user = LbSelfUtil.getDefaultUser(requ.getDeviceid());
		if(!BeanUtils.isNotNull(user)){
			return XmlUtil.beanToXml(LbSelfUtil.common(Constant.RESFAIL, "该自助机编码未注册请联系管理员"), LbSHResponseVo.class, false);
		}

		try {
			Map<String, Object> paramMap = new HashMap<String, Object>();
			paramMap.put("pkOrg", user.getPkOrg());

			//获取指定日期的排班科室
			if(BeanUtils.isNotNull(requ.getEndDate()) && BeanUtils.isNotNull(requ.getStartDate())){
				paramMap.put("StartDate", requ.getStartDate());// 当天的日期
				paramMap.put("EndDate", requ.getEndDate());// 七天后的日期
			}else {
				//根据日期判断查询预约排班信息，正常排班信息
				//判断获取时间是否大于当前时间
				if(BeanUtils.isNotNull(requ.getRegDate()) && requ.getRegDate().compareTo(dt.toString("YYYY-MM-dd"))>0){
					paramMap.put("nowDate", requ.getRegDate());
					paramMap.put("total","0");
				}else{
					paramMap.put("nowDate", dt.toString("YYYY-MM-dd"));// 当天的日期
					//paramMap.put("nowTime", dt.toString("HH:mm:ss"));// 当前时刻
				}
			}

			paramMap.put("codeDept", requ.getDeptCode());//获取科室编码
			paramMap.put("doctCode", requ.getDoctCode());//获取医生编码
			paramMap.put("flagStop", "0");//停用标志
			/** 1.获取当天目前可用排班 */
			List<Map<String, Object>> todaylist = schMapper.LbTodaySchInfosByDate(paramMap);
			List<BdOuDeptVo> todaySet = new ArrayList<BdOuDeptVo>();
			for (Map<String, Object> today:todaylist) {
				boolean ifset = true;
				if(todaylist.size()>0){
					for(BdOuDeptVo tofmsp: todaySet){
						if(tofmsp.getCodeDept().equals(LbSelfUtil.getPropValueStr(today,"codeDept"))){
							ifset = false;
						}
					}
				}
				if(ifset){
					// 科室信息
					BdOuDeptVo dept = new BdOuDeptVo();
					dept.setCodeDept(LbSelfUtil.getPropValueStr(today,"codeDept"));
					dept.setNameDept(LbSelfUtil.getPropValueStr(today,"nameDept"));
					dept.setDeptDesc(LbSelfUtil.getPropValueStr(today,"deptDesc"));
					dept.setSortno(LbSelfUtil.getPropValueStr(today,"sortno"));
					todaySet.add(dept);//去重复科室信息
				}
			}
			// 结果集添加到响应实体
			responseVo.setDeptList(todaySet);

		} catch (Exception e) {
			logger.info("排班科室查询："+e);
			return XmlUtil.beanToXml(LbSelfUtil.common(Constant.RESFAIL, "失败"), LbSHResponseVo.class, false);
		}

		responseVo.setCommonVo(LbSelfUtil.commonVo(Constant.RESSUC, "成功"));
		logger.info("排班科室查询："+XmlUtil.beanToXml(responseVo, responseVo.getClass(), false));
		return XmlUtil.beanToXml(responseVo, responseVo.getClass(), false);
	}
	/**
	 * 查询排班医生信息接口
	 * @param param
	 * @return
	 */
	@Override
	public String QuerySchDoctor(String param) {
		// TODO Auto-generated method stub
		logger.info("排班医生查询："+param);
		LbSHRequestVo requ = null;
		DateTime dt = DateTime.now();
		// 响应结果
		LbSHResponseVo responseVo = new LbSHResponseVo();
		if (null != param && !("").equals(param)) {
			requ = (LbSHRequestVo) XmlUtil.XmlToBean(param, LbSHRequestVo.class);
			if(CommonUtils.isEmptyString(requ.getDeviceid())){
				return XmlUtil.beanToXml(LbSelfUtil.common(Constant.RESFAIL, "未获取到自助机编码"), LbSHResponseVo.class, false);
			}
		} else{
			return XmlUtil.beanToXml(LbSelfUtil.common(Constant.RESFAIL, "未获取到信息"), LbSHResponseVo.class, false);
		}

		User user = LbSelfUtil.getDefaultUser(requ.getDeviceid());
		if(!BeanUtils.isNotNull(user)){
			return XmlUtil.beanToXml(LbSelfUtil.common(Constant.RESFAIL, "该自助机编码未注册请联系管理员"), LbSHResponseVo.class, false);
		}

		try {
			Map<String, Object> paramMap = new HashMap<String, Object>();
			paramMap.put("pkOrg", user.getPkOrg());
			//获取指定日期的排班科室
			if(BeanUtils.isNotNull(requ.getEndDate()) && BeanUtils.isNotNull(requ.getStartDate())){
				paramMap.put("StartDate", requ.getStartDate());// 当天的日期
				paramMap.put("EndDate", requ.getEndDate());// 七天后的日期
			}else {
				//根据日期判断查询预约排班信息，正常排班信息
				//判断获取时间是否大于当前时间
				if(BeanUtils.isNotNull(requ.getRegDate()) && requ.getRegDate().compareTo(dt.toString("YYYY-MM-dd"))>0){
					paramMap.put("nowDate", requ.getRegDate());
					paramMap.put("total","0");
				}else{
					paramMap.put("nowDate", dt.toString("YYYY-MM-dd"));// 当天的日期
					//paramMap.put("nowTime", dt.toString("HH:mm:ss"));// 当前时刻
				}
			}
			paramMap.put("codeDept", requ.getDeptCode());//获取科室编码
			paramMap.put("doctCode", requ.getDoctCode());//获取医生编码
			paramMap.put("flagStop", "0");//停用标志
			/** 1.获取当天目前可用排班 */
			List<Map<String, Object>> todaylist = schMapper.LbTodaySchInfosByDate(paramMap);
			List<LbQuedocinfoResTemVo> todaySet = new ArrayList<LbQuedocinfoResTemVo>();
			for (Map<String, Object> today:todaylist) {
				boolean ifset = true;
				if(todaylist.size()>0){
					for(LbQuedocinfoResTemVo tofmsp: todaySet){
						if(tofmsp.getDoctCode().equals(LbSelfUtil.getPropValueStr(today,"codeEmp"))){
							ifset = false;
						}
					}
				}
				if(ifset){
					//医生工号
					LbQuedocinfoResTemVo doc = new LbQuedocinfoResTemVo();
					doc.setDoctCode(LbSelfUtil.getPropValueStr(today,"codeEmp"));
					doc.setDoctName(LbSelfUtil.getPropValueStr(today,"nameEmp"));
					doc.setCodeDept(LbSelfUtil.getPropValueStr(today,"codeDept"));
					doc.setDeptName(LbSelfUtil.getPropValueStr(today,"nameDept"));
					doc.setEmpsrvtype(LbSelfUtil.getPropValueStr(today,"name"));
					doc.setDoctDesc(LbSelfUtil.getPropValueStr(today,"spec"));

					todaySet.add(doc);
				}
			}
			// 结果集添加到响应实体
			responseVo.setResTemList(todaySet);
		} catch (Exception e) {
			logger.info("排班医生查询："+e);
			return XmlUtil.beanToXml(LbSelfUtil.common(Constant.RESFAIL, "失败"), LbSHResponseVo.class, false);
		}
		responseVo.setCommonVo(LbSelfUtil.commonVo(Constant.RESSUC, "成功"));
		logger.info("排班医生查询："+XmlUtil.beanToXml(responseVo, responseVo.getClass(), false));
		return XmlUtil.beanToXml(responseVo, responseVo.getClass(), false);

	}
	@Override
	public String QuerySch(String param) {
		// TODO Auto-generated method stub
		logger.info("排班号源查询："+param);
		LbSHRequestVo requ = null;
		// 响应结果
		LbSHResponseVo responseVo = new LbSHResponseVo();
		List<LBQueSchVo> queSchList = new ArrayList<>();

		if (null != param && !("").equals(param)) {
			requ = (LbSHRequestVo) XmlUtil.XmlToBean(param, LbSHRequestVo.class);
			if(CommonUtils.isEmptyString(requ.getDeviceid())){
				return XmlUtil.beanToXml(LbSelfUtil.common(Constant.RESFAIL, "未获取到自助机编码"), LbSHResponseVo.class, false);
			}
		} else{
			return XmlUtil.beanToXml(LbSelfUtil.common(Constant.RESFAIL, "未获取到信息"), LbSHResponseVo.class, false);
		}

		User user = LbSelfUtil.getDefaultUser(requ.getDeviceid());
		if(!BeanUtils.isNotNull(user)){
			return XmlUtil.beanToXml(LbSelfUtil.common(Constant.RESFAIL, "该自助机编码未注册请联系管理员"), LbSHResponseVo.class, false);
		}
		UserContext.setUser(user);

		try {
			// 科室医生号源查询
			queSchList = schPubForWsService.getQuerySch(requ, user);
			responseVo.setQueSchVo(queSchList);
		} catch (Exception e) {
			logger.info("排班号源查询："+e);
			return XmlUtil.beanToXml(LbSelfUtil.common(Constant.RESFAIL, "失败"), LbSHResponseVo.class, false);
		}
		responseVo.setCommonVo(LbSelfUtil.commonVo(Constant.RESSUC, "成功"));
		logger.info("排班号源查询："+XmlUtil.beanToXml(responseVo, responseVo.getClass(), false));
		return XmlUtil.beanToXml(responseVo, responseVo.getClass(), false);
	}

	@Override
	public String QueryCardIss(String param) {
		// TODO Auto-generated method stub
		logger.info("查询就诊卡发卡缴纳费用信息接口："+param);
		LbSHRequestVo requ = null;
		// 响应结果实体
	    LbSHResponseVo responseVo = new LbSHResponseVo();
	    if(StringUtils.isNotBlank(param)){
			requ = (LbSHRequestVo)XmlUtil.XmlToBean(param, LbSHRequestVo.class);
			if(CommonUtils.isEmptyString(requ.getDeviceid())){
				return XmlUtil.beanToXml(LbSelfUtil.common(Constant.RESFAIL, "未获取到自助机编码！！"), LbSHResponseVo.class, false);
			}
			if(CommonUtils.isEmptyString(requ.getRepCard())){
				return XmlUtil.beanToXml(LbSelfUtil.common(Constant.RESFAIL, "未获发卡模式编码！！"), LbSHResponseVo.class, false);
			}
		}else{
			return XmlUtil.beanToXml(LbSelfUtil.common(Constant.RESFAIL, "未获取到信息！！"), LbSHResponseVo.class, false);
		}
		User user = LbSelfUtil.getDefaultUser(requ.getDeviceid());
		try {
			//查询就诊卡押金金额
			Map<String, Object> depositFeeMap = DataBaseHelper.queryForMap("select * from bd_sysparam where del_flag='0' and code='PI0002' and pk_org=? ",user.getPkOrg());
			responseVo.setDepositFee(depositFeeMap.get("val").toString());

			//查询就诊卡费模式
			Map<String, Object> cardTypeMap = DataBaseHelper.queryForMap("select * from bd_sysparam where del_flag = '0' and  code='PI0005' and pk_org=?",user.getPkOrg());

			//查询就诊卡费
			Map<String, Object> cardFeeMap = DataBaseHelper.queryForMap("select * from bd_sysparam where del_flag = '0' and  code='PI0004' and pk_org=?",user.getPkOrg());
			responseVo.setCardFee(cardFeeMap.get("val").toString());

			String card[] = cardTypeMap.get("val").toString().split(",");

			BigDecimal amtAcc = BigDecimal.ZERO;
			for (int i = 0; i < card.length; i++) {
				if(("0").equals(card[i])){
					responseVo.setTotalFee("0.00");
					break;
				}else if((requ.getRepCard()).equals(card[i])){//收取卡费模式对比
					Double pric = Double.parseDouble(cardFeeMap.get("val").toString());
					BigDecimal amt=BigDecimal.valueOf(pric);
					amtAcc=amtAcc.add(amt);
				}else if(("3").equals(card[i])){
					Double pric = Double.parseDouble(depositFeeMap.get("val").toString());
					BigDecimal amt=BigDecimal.valueOf(pric);
					amtAcc=amtAcc.add(amt);
				}
			}
			responseVo.setTotalFee(amtAcc.toString());


		} catch (Exception e) {
			logger.info("查询就诊卡发卡缴纳费用信息接口："+e);
			return XmlUtil.beanToXml(LbSelfUtil.common(Constant.RESFAIL, "失败"), LbSHResponseVo.class, false);
		}
		responseVo.setCommonVo(LbSelfUtil.commonVo(Constant.RESSUC, "成功"));
		logger.info("查询就诊卡发卡缴纳费用信息接口："+XmlUtil.beanToXml(responseVo, responseVo.getClass(), false));
		return XmlUtil.beanToXml(responseVo, responseVo.getClass(), false);

	}

	@Override
	public String QueryPiInfo(String param) {
		logger.info("查询患者信息接口："+param);
		LbSHRequestVo requ = null;
		Map<String, Object> paramMap = null;
		if (StringUtils.isNotBlank(param)) {
			requ = (LbSHRequestVo) XmlUtil.XmlToBean(param, LbSHRequestVo.class);
			if (!StringUtils.isBlank(requ.getCardType())) {
				//如果卡类型不为空，则卡号也不能为空
				if (StringUtils.isBlank(requ.getCardNo())) {
					return XmlUtil.beanToXml(LbSelfUtil.common(Constant.RESFAIL, "证件卡类型不为空时，请传入证件号码！"),LbSHResponseVo.class, false);
				}
				//判断是否住院号
				if(requ.getCardType().equals("2")){
					requ.setCodeIp(requ.getCardNo());
					requ.setCardNo(null);
					requ.setCardType(null);
				}
			}else if (!StringUtils.isBlank(requ.getDtCardType())) {
				//如果卡类型不为空，则卡号也不能为空
				if (StringUtils.isBlank(requ.getDtCardNo())) {
					return XmlUtil.beanToXml(LbSelfUtil.common(Constant.RESFAIL, "院内卡类型不为空时，请传入卡号！"),LbSHResponseVo.class, false);
				}
			}else{
				return XmlUtil.beanToXml(LbSelfUtil.common(Constant.RESFAIL, "证件类型为空！"),LbSHResponseVo.class, false);
			}
		    paramMap = LbSelfUtil.toMap(requ);
		}

		LbQuePiInfoResTemVo piInfo = new LbQuePiInfoResTemVo();
		try {
			List<Map<String, Object>> queryPiMaster = pvPubForWsMapper.queryPiMaster(paramMap);
			if (queryPiMaster.size()>0){
					piInfo = (LbQuePiInfoResTemVo)LbSelfUtil.mapToBean(queryPiMaster.get(0), piInfo);
					if(!StringUtils.isNotBlank(piInfo.getDtcardNo())){
						piInfo.setCardStatus("-1");
					}
					piInfo.setBirthday(DateUtils.formatDate(DateUtils.parseDate(queryPiMaster.get(0).get("birthday").toString()),new Object[]{}));
					piInfo.setAge(DateUtils.getAgeByBirthday(DateUtils.strToDate(queryPiMaster.get(0).get("birthday").toString()),new Date()));
			}else{
				//LB自助机获取患者信息校验"0"为双方约定判断数据修改需要联系自助机
				return XmlUtil.beanToXml(LbSelfUtil.common(Constant.RESSUC, Constant.RESFAIL), LbSHResponseVo.class, false);
			}
		} catch (Exception e) {
			logger.info("查询患者信息接口："+e);
			return XmlUtil.beanToXml(LbSelfUtil.common(Constant.RESFAIL, "失败"), LbSHResponseVo.class, false);
		}
		piInfo.setCommonVo(LbSelfUtil.commonVo(Constant.RESSUC, "成功"));
		logger.info("查询患者信息接口："+XmlUtil.beanToXml(piInfo, LbQuePiInfoResTemVo.class, false));
		return  XmlUtil.beanToXml(piInfo, LbQuePiInfoResTemVo.class, false);
	}
	/*
	 * 患者建档/患者信息修改
	 * 灵璧自助机webService接口
	 */
	@Override
	public String CreatPi(String param) {
		logger.info("患者建档/患者信息修改"+param);
		LbSHRequestVo requ = null;
		PiMaster pi = new PiMaster();

		LbSHResponseVo responseVo = new LbSHResponseVo();
		if (StringUtils.isNotBlank(param)) {
			requ = (LbSHRequestVo) XmlUtil.XmlToBean(param, LbSHRequestVo.class);
			if(CommonUtils.isEmptyString(requ.getDeviceid())){
				return XmlUtil.beanToXml(LbSelfUtil.common(Constant.RESFAIL, "未获取到自助机编码"), LbSHResponseVo.class, false);
			}
		}else{
			return XmlUtil.beanToXml(LbSelfUtil.common(Constant.RESFAIL, "未获取到信息"), LbSHResponseVo.class, false);
		}

		if (CommonUtils.isEmptyString(requ.getName())) {
			return XmlUtil.beanToXml(LbSelfUtil.common(Constant.RESFAIL, "未获取到患者姓名!"),LbSHResponseVo.class, false);
		}else if (CommonUtils.isEmptyString(requ.getCardType())) {
			return XmlUtil.beanToXml(LbSelfUtil.common(Constant.RESFAIL, "未获取到患者证件类型!"),LbSHResponseVo.class, false);
		}else if (CommonUtils.isEmptyString(requ.getCardNo())) {
			return XmlUtil.beanToXml(LbSelfUtil.common(Constant.RESFAIL, "未获取到患者证件号!"),LbSHResponseVo.class, false);
		}else if (CommonUtils.isEmptyString(requ.getPhoneno())) {
			return XmlUtil.beanToXml(LbSelfUtil.common(Constant.RESFAIL, "未获取到患者手机号!"),LbSHResponseVo.class, false);
		}
		User user = LbSelfUtil.getDefaultUser(requ.getDeviceid());
		if(!BeanUtils.isNotNull(user)){
			return XmlUtil.beanToXml(LbSelfUtil.common(Constant.RESFAIL, "该自助机编码未注册请联系管理员"), LbSHResponseVo.class, false);
		}
		try {
			//患者信息校验修改保存
			pi = lbPubForWsService.lbSavePiMaster(pi, requ,user);

			responseVo.setPatientCode(pi.getCodePi());
			responseVo.setPatientId(pi.getPkPi());
		} catch (Exception e) {
			logger.info("患者建档/患者信息修改"+e);
			return XmlUtil.beanToXml(LbSelfUtil.common(Constant.RESFAIL, "异常"),LbSHResponseVo.class, false);
		}

		       /* //发送患者信息
		        Map<String,Object> msgParam = new HashMap<String,Object>();
                Map<String, Object> map = new HashMap<String, Object>();
                map.put("pkPi",pi.getPkPi());
				msgParam.put("pi", map);
				PlatFormSendUtils.sendPiMasterMsg(msgParam);*/

		responseVo.setCommonVo(LbSelfUtil.commonVo(Constant.RESSUC, "操作成功"));
		logger.info("患者建档/患者信息修改"+XmlUtil.beanToXml(responseVo, LbSHResponseVo.class, false));
		return XmlUtil.beanToXml(responseVo, LbSHResponseVo.class, false);
	}

	@Override
	public String CardIssue(String param) {
		LbSHRequestVo requ = null;
		logger.info("发卡"+param);
		Calendar instance = Calendar.getInstance();
		Date time = instance.getTime();
		if (null != param && !("").equals(param)) {
			requ = (LbSHRequestVo) XmlUtil.XmlToBean(param, LbSHRequestVo.class);
			if(StringUtils.isBlank(requ.getDeviceid())){
				return XmlUtil.beanToXml(LbSelfUtil.common(Constant.RESFAIL, "未获取到自助机编码"), LbSHResponseVo.class, false);
			}
			if (StringUtils.isBlank(requ.getPatientId())){
				return XmlUtil.beanToXml(LbSelfUtil.common(Constant.RESFAIL, "未获取到患者id!"),LbSHResponseVo.class, false);
			}
			if (StringUtils.isBlank(requ.getDtCardType())){
				return XmlUtil.beanToXml(LbSelfUtil.common(Constant.RESFAIL, "未获取到卡类型编码"),LbSHResponseVo.class, false);
			}
			if (StringUtils.isBlank(requ.getDtCardNo())){
				return XmlUtil.beanToXml(LbSelfUtil.common(Constant.RESFAIL, "未获取到卡号!"),LbSHResponseVo.class, false);
			}
		} else {
			return XmlUtil.beanToXml(LbSelfUtil.common(Constant.RESFAIL, "未获取到有效信息！"),LbSHResponseVo.class, false);
		}

		User user = LbSelfUtil.getDefaultUser(requ.getDeviceid());
		@SuppressWarnings("unused")
		UserContext userContext = new UserContext();
		UserContext.setUser(user);
		if(!BeanUtils.isNotNull(user)){
			return XmlUtil.beanToXml(LbSelfUtil.common(Constant.RESFAIL, "该自助机编码未注册请联系管理员"), LbSHResponseVo.class, false);
		}
		try {
			LbPiCardVo piCardVo = new LbPiCardVo();
			piCardVo.setPkPi(requ.getPatientId());// 患者主键
			piCardVo.setDtCardtype(requ.getDtCardType());// 卡类型
			piCardVo.setCardNo(requ.getDtCardNo());// 卡号
			piCardVo.setDelFlag("0");
			piCardVo.setDateBegin(time);

			//查询就诊卡有效时间
			Map<String, Object> depDateMap = DataBaseHelper.queryForMap("select * from bd_sysparam where del_flag='0' and code='PI0001' and pk_org=? ",user.getPkOrg());
			int day = Integer.valueOf(depDateMap.get("val").toString());
			instance.add(Calendar.DAY_OF_MONTH, +day);
			piCardVo.setDateEnd(instance.getTime());

			//查询就诊卡押金金额
			Map<String, Object> depositFeeMap = DataBaseHelper.queryForMap("select * from bd_sysparam where del_flag='0' and code='PI0002' and pk_org=? ",user.getPkOrg());
			//查询就诊卡费模式
			Map<String, Object> cardTypeMap = DataBaseHelper.queryForMap("select * from bd_sysparam where del_flag = '0' and  code='PI0005' and pk_org=?",user.getPkOrg());
			//查询就诊卡费
			Map<String, Object> cardFeeMap = DataBaseHelper.queryForMap("select * from bd_sysparam where del_flag = '0' and  code='PI0004' and pk_org=?",user.getPkOrg());

			String card[] = cardTypeMap.get("val").toString().split(",");

			BigDecimal amtAcc = BigDecimal.ZERO;//需要支付金额
			//BigDecimal payAcc = BigDecimal.ZERO;//支付金额
			for (int i = 0; i < card.length; i++) {
				if((requ.getRepCard()).equals(card[i])){//收取卡费模式对比
					Double pric = Double.parseDouble(cardFeeMap.get("val").toString());
					BigDecimal amt=BigDecimal.valueOf(pric);
					amtAcc=amtAcc.add(amt);
				}else if(("3").equals(card[i])){
					Double pric = Double.parseDouble(depositFeeMap.get("val").toString());
					BigDecimal amt=BigDecimal.valueOf(pric);
					amtAcc=amtAcc.add(amt);
				}
			}

			if(("7").equals(requ.getPayType()) || ("8").equals(requ.getPayType())){
				piCardVo.setPayInfo(requ.getQrCodeInfoVo().get(0).getOrderno());//订单号

			}else if(("1").equals(requ.getPayType())){//现金支付金额判断

			}

			if(!Double.valueOf(amtAcc.toString()).equals(requ.getQrCodeInfoVo().get(0).getPayamt())){
				logger.info("发卡失败："+amtAcc+"c:->"+BigDecimal.valueOf(requ.getQrCodeInfoVo().get(0).getPayamt())+"消息体："+param);
				return XmlUtil.beanToXml(LbSelfUtil.common(Constant.RESFAIL, "发卡金额有误,请核对！"), LbSHResponseVo.class, false);
			}
			piCardVo.setDtPaymode(requ.getPayType());//支付类型
			ResponseJson  result =  apputil.execService("PI", "carddealService", "saveCardInfos", piCardVo,user);
			if(result.getStatus()==0){
				if(!StringUtils.isBlank(requ.getPayAmt()) && !("0.0").equals(requ.getPayAmt()) && !("0").equals(requ.getPayAmt())&& !("0.00").equals(requ.getPayAmt())){
	            	/**
	    			 * 支付信息写入外部支付接口记录表bl_ext_pay
	    			 */
	            	Map<String, Object> patMap = new HashMap<>();
					patMap.put("pkPi", requ.getPatientId());
					requ.getQrCodeInfoVo().get(0).setPaymethod("自助机办卡支付");
					@SuppressWarnings("unchecked")
					Map<String, Object> pamMap = (Map<String, Object>)result.getData();
					String pkDepopi = LbSelfUtil.getPropValueStr(pamMap,"pkDepopi");
	         		blPubForWsService.LbPayment(null,requ,patMap,user,pkDepopi);
	            }
			}else{
				logger.info("自助机患者发卡/补卡接口失败："+param);
				return XmlUtil.beanToXml(LbSelfUtil.common(Constant.RESFAIL, result.getDesc()), LbSHResponseVo.class, false);
			}
		} catch (Exception e) {
			logger.info("自助机患者发卡/补卡接口失败："+e);
			return XmlUtil.beanToXml(LbSelfUtil.common(Constant.RESFAIL, "异常"), LbSHResponseVo.class, false);
		}
		 LbSHResponseVo responseVo = new LbSHResponseVo();
		 responseVo.setDtcardNo(LbSelfUtil.getPropValueStr(DataBaseHelper.queryForMap("select CARD_NO from PI_CARD where FLAG_ACTIVE = '1' AND EU_STATUS ='0' and DT_CARDTYPE ='01' and pk_pi=? ORDER BY create_time DESC", new Object[]{requ.getPatientId()}),"cardNo"));
		 responseVo.setCommonVo(LbSelfUtil.commonVo("1", "操作成功！"));
		 logger.info("自助机发卡："+XmlUtil.beanToXml(responseVo, LbSHResponseVo.class, false));
		return XmlUtil.beanToXml(responseVo, LbSHResponseVo.class, false);
	}

	/*
	 * 锁号
	 */
	@Override
	public String LockReg(String param) {
		logger.info("锁号"+param);
		LbSHRequestVo requ = null;
		SchTicket ticket = null;
		LbSHResponseVo responseVo = new LbSHResponseVo();
		if (StringUtils.isNotBlank(param)) {
			requ = (LbSHRequestVo) XmlUtil.XmlToBean(param, LbSHRequestVo.class);
			if (StringUtils.isBlank(requ.getRegId())) {
				return XmlUtil.beanToXml(LbSelfUtil.common(Constant.RESFAIL, "未获取到号源信息"),LbSHResponseVo.class, false);
			}
			if(CommonUtils.isEmptyString(requ.getDeviceid())){
				return XmlUtil.beanToXml(LbSelfUtil.common(Constant.RESFAIL, "未获取到自助机编码"), LbSHResponseVo.class, false);
			}
			if(CommonUtils.isEmptyString(requ.getPatientId())){
				return XmlUtil.beanToXml(LbSelfUtil.common(Constant.RESFAIL, "未获取到患者信息"), LbSHResponseVo.class, false);
			}
			if(CommonUtils.isEmptyString(requ.getDeptCode())){
				return XmlUtil.beanToXml(LbSelfUtil.common(Constant.RESFAIL, "未获取到科室编码"), LbSHResponseVo.class, false);
			}
		} else {
			return XmlUtil.beanToXml(LbSelfUtil.common(Constant.RESFAIL, "未获取到信息"),LbSHResponseVo.class, false);
		}
		User user = LbSelfUtil.getDefaultUser(requ.getDeviceid());
		@SuppressWarnings("unused")
		UserContext userContext = new UserContext();
		UserContext.setUser(user);
		if(!BeanUtils.isNotNull(user)){
			return XmlUtil.beanToXml(LbSelfUtil.common(Constant.RESFAIL, "该自助机编码未注册请联系管理员"), LbSHResponseVo.class, false);
		}
		try {
			//儿科年龄上限
			Map<String, Object> cardTypeMap = DataBaseHelper.queryForMap("select val from bd_sysparam where del_flag = '0' and  code='PV0019' and pk_org=?",user.getPkOrg());
			//儿科科室
			Map<String, Object> codeTypeMap = DataBaseHelper.queryForMap("select val from bd_sysparam where del_flag = '0' and  code='CN0001' and pk_org=?",user.getPkOrg());
			LbPiMasterRegVo regvo = DataBaseHelper.queryForBean("SELECT * FROM pi_master pi LEFT JOIN PI_INSURANCE hp on hp.pk_pi = pi.pk_pi and hp.del_flag = '0' WHERE pi.del_flag = '0' and pi.pk_pi = ?", LbPiMasterRegVo.class, new Object[]{requ.getPatientId()});
			if(null != codeTypeMap){
				String card[] = codeTypeMap.get("val").toString().split(",");
				for (int i = 0; i < card.length; i++) {
					  if((card[i]).equals(requ.getDeptCode())){
						int age = DateUtils.getAge(regvo.getBirthDate());
						int Maximum = Integer.valueOf(cardTypeMap.get("val").toString());
						if(age-Maximum>0){
							logger.info("所选挂号科室年龄最大:"+Maximum+"岁，患者实际："+age+"岁自助机挂号年龄："+param);
							return XmlUtil.beanToXml(LbSelfUtil.common(Constant.RESFAIL, "所选挂号科室年龄最大:"+Maximum+"岁，患者实际："+age+"岁"), LbSHResponseVo.class, false);
						}
					  }

					}
			}
			responseVo = lbPubForWsService.lbLockReg(requ, ticket);
		} catch (Exception e) {
			logger.info("锁号"+e);
			return XmlUtil.beanToXml(LbSelfUtil.common(Constant.RESFAIL, "占号异常"),LbSHResponseVo.class, false);
		}
		logger.info("锁号"+XmlUtil.beanToXml(responseVo, LbSHResponseVo.class, false));
		return XmlUtil.beanToXml(responseVo, LbSHResponseVo.class, false);
	}

	/*
	 * 提交挂号
	 */
	@Override
	public String Register(String param) {
		logger.info("提交挂号"+param);
		LbSHRequestVo requ = null;
		//响应结果
		LbSHResponseVo responseVo = new LbSHResponseVo();
		if(StringUtils.isNotBlank(param)){
			requ = (LbSHRequestVo)XmlUtil.XmlToBean(param, LbSHRequestVo.class);
			if(CommonUtils.isEmptyString(requ.getDeviceid())){
				return XmlUtil.beanToXml(LbSelfUtil.common(Constant.RESFAIL, "未获取到自助机编码"), LbSHResponseVo.class, false);
			}
			if (StringUtils.isBlank(requ.getRegId())) {
				return XmlUtil.beanToXml(LbSelfUtil.common(Constant.RESFAIL, "未获取到号源信息"),LbSHResponseVo.class, false);
			}
			if (StringUtils.isBlank(requ.getDeptCode())) {
				return XmlUtil.beanToXml(LbSelfUtil.common(Constant.RESFAIL, "未获取到科室编码信息"),LbSHResponseVo.class, false);
			}
			if (StringUtils.isBlank(requ.getPayAmt())) {
				return XmlUtil.beanToXml(LbSelfUtil.common(Constant.RESFAIL, "未获取支付金额信息"),LbSHResponseVo.class, false);
			}
			if (StringUtils.isBlank(requ.getPayType())) {
				return XmlUtil.beanToXml(LbSelfUtil.common(Constant.RESFAIL, "未获取到支付方式"),LbSHResponseVo.class, false);
			}
			if (StringUtils.isBlank(requ.getPatientId())) {
				return XmlUtil.beanToXml(LbSelfUtil.common(Constant.RESFAIL, "未获取到患者有效信息"),LbSHResponseVo.class, false);
			}
		}else{
			return XmlUtil.beanToXml(LbSelfUtil.common(Constant.RESFAIL, "未获取到信息"), LbSHResponseVo.class, false);
		}

		User user = LbSelfUtil.getDefaultUser(requ.getDeviceid());
		UserContext.setUser(user);
		if(!BeanUtils.isNotNull(user)){
			return XmlUtil.beanToXml(LbSelfUtil.common(Constant.RESFAIL, "该自助机编码未注册请联系管理员"), LbSHResponseVo.class, false);
		}
		LbPiMasterRegVo regvo = DataBaseHelper.queryForBean("SELECT pi.*,hp.pk_hp,hp.pk_insurance FROM pi_master pi LEFT JOIN PI_INSURANCE hp on hp.pk_pi = pi.pk_pi and hp.del_flag = '0' WHERE pi.del_flag = '0' and pi.pk_pi = ?", LbPiMasterRegVo.class, new Object[]{requ.getPatientId()});
		//查询自费医保计划主键
		Map<String, Object> bdhpMap = DataBaseHelper.queryForMap("SELECT pk_hp FROM bd_hp WHERE eu_hptype=? and del_flag='0' ", "0");
		//2020-7-17 lb门诊挂号默认医保类型全自费
		regvo.setPkHp(LbSelfUtil.getPropValueStr(bdhpMap,"pkHp"));

		try{
		//儿科年龄上限
		Map<String, Object> cardTypeMap = DataBaseHelper.queryForMap("select val from bd_sysparam where del_flag = '0' and  code='PV0019' and pk_org=?",user.getPkOrg());
		//儿科科室
		Map<String, Object> codeTypeMap = DataBaseHelper.queryForMap("select val from bd_sysparam where del_flag = '0' and  code='CN0001' and pk_org=?",user.getPkOrg());
		if(null != codeTypeMap){
			String card[] = codeTypeMap.get("val").toString().split(",");
			for (int i = 0; i < card.length; i++) {
				  if((card[i]).equals(requ.getDeptCode())){
					int age = DateUtils.getAge(regvo.getBirthDate());
					int Maximum = Integer.valueOf(cardTypeMap.get("val").toString());
					if(age-Maximum>0){
						logger.info("所选挂号科室年龄最大:"+Maximum+"岁，患者实际："+age+"岁自助机挂号年龄："+param);
						return XmlUtil.beanToXml(LbSelfUtil.common(Constant.RESFAIL, "所选挂号科室年龄最大:"+Maximum+"岁，患者实际："+age+"岁"), LbSHResponseVo.class, false);
					}
				  }

				}
		}
		responseVo = lbPubForWsService.register(param,requ, user,regvo);
		}catch(Exception e){
			 logger.info("自助机挂号失败：{}",e.getMessage());
			return XmlUtil.beanToXml(LbSelfUtil.common(Constant.RESFAIL, "挂号失败！请联系管理员"), LbSHResponseVo.class, false);
		}

    	logger.info("提交挂号"+XmlUtil.beanToXml(responseVo, LbSHResponseVo.class, false));
		return XmlUtil.beanToXml(responseVo, LbSHResponseVo.class, false);
	}

	/*
	 * 挂号解锁
	 */
	@Override
	public String UnLockReg(String param) {
		logger.info("挂号解锁"+param);
		LbSHRequestVo requ = null;
		if (StringUtils.isNotBlank(param)) {
			requ = (LbSHRequestVo) XmlUtil.XmlToBean(param, LbSHRequestVo.class);
			if (StringUtils.isBlank(requ.getRegId())) {
				return XmlUtil.beanToXml(LbSelfUtil.common(Constant.RESFAIL, "未获取到号源信息"), LbSHResponseVo.class, false);
			}
		} else {
			return XmlUtil.beanToXml(LbSelfUtil.common(Constant.RESFAIL, "未获取到信息"),LbSHResponseVo.class, false);
		}
		try {
			//解锁
		    lbPubForWsService.lbUnLockReg(requ.getRegId());
		} catch (Exception e) {
			logger.info("挂号解锁"+e);
			return XmlUtil.beanToXml(LbSelfUtil.common(Constant.RESFAIL, "解锁失败！"), LbSHResponseVo.class, false);
		}

		LbSHResponseVo responseVo = new LbSHResponseVo();
		responseVo.setCommonVo(LbSelfUtil.commonVo(Constant.RESSUC, "操作成功！"));
		logger.info("挂号解锁"+XmlUtil.beanToXml(responseVo, LbSHResponseVo.class, false));
		return XmlUtil.beanToXml(responseVo, LbSHResponseVo.class, false);
	}

    //门诊就诊患者查询
	@Override
	public String QueryInpatientOpInfo(String param) {
		logger.info("门诊就诊患者查询"+param);
		LbSHRequestVo requ =null;
		// 响应结果实体
	    LbSHResponseVo responseVo = new LbSHResponseVo();
		if(StringUtils.isNotBlank(param)){
			requ = (LbSHRequestVo) XmlUtil.XmlToBean(param,LbSHRequestVo.class);
			if (!StringUtils.isBlank(requ.getCardType())) {
				//如果卡类型不为空，则卡号也不能为空
				if (StringUtils.isBlank(requ.getCardNo())) {
					return XmlUtil.beanToXml(LbSelfUtil.common(Constant.RESFAIL, "证件卡类型不为空时，请传入证件号码！"),LbSHResponseVo.class, false);
				}
				//判断是否住院号
				if(requ.getCardType().equals("2")){
					requ.setCodeIp(requ.getCardNo());
					requ.setCardNo(null);
					requ.setCardType(null);
				}
			}else if (!StringUtils.isBlank(requ.getDtCardType())) {
				//如果卡类型不为空，则卡号也不能为空
				if (StringUtils.isBlank(requ.getDtCardNo())) {
					return XmlUtil.beanToXml(LbSelfUtil.common(Constant.RESFAIL, "院内卡类型不为空时，请传入卡号！"),LbSHResponseVo.class, false);
				}
			}else if (StringUtils.isBlank(requ.getPatientId())){
				return XmlUtil.beanToXml(LbSelfUtil.common(Constant.RESFAIL, "未获取到查询条件"),LbSHResponseVo.class, false);
			}

		} else {
			return XmlUtil.beanToXml(LbSelfUtil.common(Constant.RESFAIL, "未获取到信息"),LbSHResponseVo.class, false);
		}

		Map<String, Object> paramMap = LbSelfUtil.toMap(requ);
		List<LbQueOpPvInfoResTemVo> opPvList = new ArrayList<>();
		try {
			List<Map<String, Object>> queryPiMaster = pvPubForWsMapper.queryPiMaster(paramMap);
			if (queryPiMaster.size()>0){
				for(Map<String, Object> map:queryPiMaster){
					LbQueOpPvInfoResTemVo piInfo =new LbQueOpPvInfoResTemVo();
					piInfo = (LbQueOpPvInfoResTemVo)LbSelfUtil.mapToBean(map, piInfo);
					if(!StringUtils.isNotBlank(piInfo.getDtcardNo())){
						piInfo.setCardStatus("-1");
					}
					piInfo.setBirthday(DateUtils.formatDate(DateUtils.parseDate(map.get("birthday").toString()),new Object[]{}));
					piInfo.setAge(DateUtils.getAgeByBirthday(DateUtils.strToDate(map.get("birthday").toString()),new Date()));

					//账户余额查询
					Map<String, Object> pumMap=new HashMap<String, Object>();
					pumMap.put("pkPi", map.get("patientid"));
					ResponseJson  blResult =  apputil.execService("bl", "PareAccoutService", "getPatiAccountAvailableBalance", pumMap,null);
					if(blResult.getStatus()==0){
					    @SuppressWarnings("unchecked")
						Map<String, Object> pamMap = (Map<String, Object>)blResult.getData();
					    if(!pamMap.isEmpty()){
					    	//查询押金不可用押金
						    String sql=" select nvl(sum(DEPOSIT) ,0) as deposit from PI_CARD where FLAG_ACTIVE in ('0','1') and EU_STATUS in ('0','1','2') and PK_PI= ?";
							Map<String,Object > cardMap=DataBaseHelper.queryForMap(sql,map.get("patientid"));
							BigDecimal nAmout=BigDecimal.valueOf(Double.valueOf(String.valueOf(cardMap.get("deposit"))));
							BigDecimal BalanceAmout =BigDecimal.valueOf(Double.valueOf(LbSelfUtil.getPropValueStr(pamMap,"acc")));

							piInfo.setBalance(BalanceAmout.subtract(nAmout).toString());//可用余额
						   }else{
							piInfo.setBalance("0");//可用余额
						   }
					}
					opPvList.add(piInfo);
				}
				responseVo.setQuePiOpInfo(opPvList);
			}else{
				//LB自助机获取患者信息校验"0"为双方约定判断数据修改需要联系自助机
				return XmlUtil.beanToXml(LbSelfUtil.common(Constant.RESFAIL, "未获取到该患者就诊信息，请先办卡建档"), LbSHResponseVo.class, false);
			}
		} catch (Exception e) {
			logger.info("门诊就诊患者查询"+e);
			return XmlUtil.beanToXml(LbSelfUtil.common(Constant.RESFAIL, "失败"), LbSHResponseVo.class, false);
		}
		responseVo.setCommonVo(LbSelfUtil.commonVo(Constant.RESSUC, "成功"));
		logger.info("门诊就诊患者查询"+XmlUtil.beanToXml(responseVo, LbSHResponseVo.class, false));
		return  XmlUtil.beanToXml(responseVo, LbSHResponseVo.class, false);

	}
	//住院就诊患者查询
	@Override
	public String QueryInpatientIpInfo(String param) {
		logger.info("住院就诊患者查询"+param);
		LbSHRequestVo requ =null;
		if(StringUtils.isNotBlank(param)){
			requ = (LbSHRequestVo)XmlUtil.XmlToBean(param, LbSHRequestVo.class);
			if (!StringUtils.isBlank(requ.getCardType())) {
				//如果卡类型不为空，则卡号也不能为空
				if (StringUtils.isBlank(requ.getCardNo())) {
					return XmlUtil.beanToXml(LbSelfUtil.common(Constant.RESFAIL, "证件卡类型不为空时，请传入证件号码！"),LbSHResponseVo.class, false);
				}
				//判断是否住院号
				if(requ.getCardType().equals("2")){
					requ.setCodeIp(requ.getCardNo());
					requ.setCardNo(null);
					requ.setCardType(null);
				}
			}else if (!StringUtils.isBlank(requ.getDtCardType())) {
				//如果卡类型不为空，则卡号也不能为空
				if (StringUtils.isBlank(requ.getDtCardNo())) {
					return XmlUtil.beanToXml(LbSelfUtil.common(Constant.RESFAIL, "院内卡类型不为空时，请传入卡号！"),LbSHResponseVo.class, false);
				}
			}else if (StringUtils.isBlank(requ.getPatientId())){
				return XmlUtil.beanToXml(LbSelfUtil.common(Constant.RESFAIL, "未获取到查询条件"),LbSHResponseVo.class, false);
			}
		}else{
			return XmlUtil.beanToXml(LbSelfUtil.common(Constant.RESFAIL, "未获取到信息"),LbSHResponseVo.class, false);
		}

		Map<String, Object> paramMap = LbSelfUtil.toMap(requ);
		LbQueIpPvInfoResTemVo temVo = new LbQueIpPvInfoResTemVo();
		try {
			List<Map<String, Object>> pvInfoByOp = pvPubForWsMapper.getPvInfoByIp(paramMap);
			if (pvInfoByOp.size()>0) {
				for(Map<String, Object> map : pvInfoByOp) {
					temVo = (LbQueIpPvInfoResTemVo)LbSelfUtil.mapToBean(pvInfoByOp.get(0),temVo);
                    if(null != map.get("birthday")&& !("").equals(map.get("birthday"))){
    					temVo.setBirthday(DateUtils.formatDate(DateUtils.parseDate(LbSelfUtil.getPropValueStr(map,"birthday")),new Object[]{}));
    					temVo.setAge(DateUtils.getAgeByBirthday(DateUtils.strToDate(LbSelfUtil.getPropValueStr(map,"birthday")),new Date()));
                    }
                    if(null != map.get("inDate")&& !("").equals(map.get("inDate"))){
    					temVo.setInDate(DateUtils.formatDate(DateUtils.parseDate(LbSelfUtil.getPropValueStr(map,"inDate")),new Object[]{}));
                    }
					List<Map<String, Object>> blDepoList = DataBaseHelper.queryForList("SELECT * FROM BL_DEPOSIT WHERE EU_DIRECT=1 and PK_PV=?", temVo.getPkPv());
			        if(blDepoList.size()>0){
				        BigDecimal amtAcc = BigDecimal.ZERO;
			            for(Map<String, Object> blDepomap : blDepoList){
			        	   Double pric = Double.parseDouble(blDepomap.get("amount").toString());
						   BigDecimal amt=BigDecimal.valueOf(pric);
						   amtAcc=amtAcc.add(amt);
			             }
			            temVo.setTotalPrePayment(amtAcc.toString());;
			        }
				}
			}else{
				return XmlUtil.beanToXml(LbSelfUtil.common(Constant.RESFAIL, "未查到该患者有效信息"), LbSHResponseVo.class, false);
			}
		} catch (Exception e) {
			logger.info("住院就诊患者查询:"+e);
			return XmlUtil.beanToXml(LbSelfUtil.common(Constant.RESFAIL, "失败"),LbSHResponseVo.class, false);
		}
		temVo.setRespCommon(LbSelfUtil.commonVo(Constant.RESSUC, "添加成功"));
		logger.info("住院就诊患者查询:"+XmlUtil.beanToXml(temVo, LbQueIpPvInfoResTemVo.class, false));
		return XmlUtil.beanToXml(temVo, LbQueIpPvInfoResTemVo.class, false);
	}
    //查询患者门诊待缴费记录
	@Override
	public String QueryUnpaidRecordList(String param) {
		logger.info("查询患者门诊待缴费记录:"+param);
		LbSHRequestVo requ =null;
		String resXml=null;
		LbSHResponseVo responseVo = new LbSHResponseVo();
		if(StringUtils.isNotBlank(param)){
			requ = (LbSHRequestVo)XmlUtil.XmlToBean(param, LbSHRequestVo.class);
			if(CommonUtils.isEmptyString(requ.getStartDate())){
				return XmlUtil.beanToXml(LbSelfUtil.common(Constant.RESFAIL, "未获到开始时间"), LbSHResponseVo.class, false);
			}
			if(CommonUtils.isEmptyString(requ.getEndDate())){
				return XmlUtil.beanToXml(LbSelfUtil.common(Constant.RESFAIL, "未获到结束时间"), LbSHResponseVo.class, false);
			}
			if(CommonUtils.isEmptyString(requ.getVisitId())){
				return XmlUtil.beanToXml(LbSelfUtil.common(Constant.RESFAIL, "未获取到就诊主键"), LbSHResponseVo.class, false);
			}else{
				//查询自费医保计划主键
				Map<String, Object> bdhpMap = DataBaseHelper.queryForMap("SELECT pk_hp FROM bd_hp WHERE eu_hptype=? and del_flag='0' ", "0");
				//查询患者医保计划
				String masSql="select pk_insu from pv_encounter where pk_pv= ?";
				Map<String, Object> pkInsuMap = DataBaseHelper.queryForMap(masSql, requ.getVisitId());
				//判断患者医保计划是否是全自费
				if(!(LbSelfUtil.getPropValueStr(bdhpMap,"pkHp")).equals(LbSelfUtil.getPropValueStr(pkInsuMap,"pkInsu"))){
					resXml=XmlUtil.beanToXml(LbSelfUtil.common(Constant.RESFAIL, "就诊类型非自费！请去窗口进行医保结算"), LbSHResponseVo.class, false);
					logger.info("医保计划非自费{}",resXml);
					return resXml;
				}
			}
		}else{
			return XmlUtil.beanToXml(LbSelfUtil.common(Constant.RESFAIL, "未获取到信息"),LbSHResponseVo.class, false);
		}
		List<LbQueOpUnpaidResTemVo> resTemList = new ArrayList<>();
		try {
			requ.setSettle("0");
			Date StartDate=DateUtils.strToDate(requ.getStartDate(), "yyyy-MM-dd");

			int days=DateUtils.getDateSpace(StartDate, new Date());


			requ.setStartDate(DateUtils.getDateTimeStr(DateUtils.strToDate(requ.getStartDate(), "yyyy-MM-dd")));

			requ.setEndDate(DateUtils.getDateTimeStr(DateUtils.strToDate(requ.getEndDate(), "yyyy-MM-dd")).substring(0, 8)+"235959");

			List<LbRecipesVo>  recList = new ArrayList<>();

			Map<String, Object> pamMap = LbSelfUtil.toMap(requ);
			BigDecimal amtAcc = BigDecimal.ZERO;
			LbQueOpUnpaidResTemVo resTemVo = new LbQueOpUnpaidResTemVo();

    		Map<String, Object> hpCodeMap=pvPubForWsMapper.LbgetPvHp(pamMap);
    		   if(hpCodeMap!=null && LbSelfUtil.getPropValueStr(hpCodeMap,"code").equals("NH")){
    			  resTemVo.setSettleType("1");//结算类型
    		   }else{
    			  resTemVo.setSettleType("0");//结算类型
    		   }

    			List<Map<String, Object>> opCgDayDetail = cnPubForWsMapper.LbqueryCnOrderUnpaid(pamMap);
    			if(opCgDayDetail.size()>0){
                	resTemVo.setVisitId(LbSelfUtil.getPropValueStr(opCgDayDetail.get(0),"visitId"));//就诊主键

                	resTemVo.setDeptName(LbSelfUtil.getPropValueStr(opCgDayDetail.get(0),"deptName"));//科室
                	resTemVo.setDoctName(LbSelfUtil.getPropValueStr(opCgDayDetail.get(0),"doctName"));//医生
                	//就诊日期
                	resTemVo.setVisitDate(DateUtils.formatDate(DateUtils.parseDate(opCgDayDetail.get(0).get("visitDate").toString()),new Object[]{}));
                	for(Map<String, Object> map :opCgDayDetail){
                		LbRecipesVo rec = new LbRecipesVo();
                		List<LbBlCgIpVo> blOpDtList = new ArrayList<>();
                		BigDecimal amtRec = BigDecimal.ZERO;
                		rec = (LbRecipesVo)LbSelfUtil.mapToBean(map,rec);
                		rec.setRecipeTime(DateUtils.formatDate(DateUtils.parseDate(map.get("recipeTime").toString()),new Object[]{}));

                		String sql = "select itemcate.name as item_type,dt.* from bl_op_dt dt INNER JOIN bd_itemcate itemcate ON itemcate.pk_itemcate = dt.pk_itemcate where pk_cnord =?";
                		List<Map<String, Object>> blOpList=DataBaseHelper.queryForList(sql, rec.getRecipeId());
                		if(blOpList.size()>0){
                			for(Map<String, Object> Opmap:blOpList){
                				LbBlCgIpVo lbVo = new LbBlCgIpVo();
                				lbVo.setItemId(LbSelfUtil.getPropValueStr(Opmap,"pkCgop"));//项目主键
                				lbVo.setItemType(LbSelfUtil.getPropValueStr(Opmap,"itemType"));//项目类别
                				lbVo.setItemName(LbSelfUtil.getPropValueStr(Opmap,"nameCg"));//项目名称
                				lbVo.setSpecs(LbSelfUtil.getPropValueStr(Opmap,"spec"));//规格
                				lbVo.setQty(LbSelfUtil.getPropValueStr(Opmap,"quan"));//项目数量
                				lbVo.setPrice(LbSelfUtil.getPropValueStr(Opmap,"price"));//项目单价
                				lbVo.setItemFee(LbSelfUtil.getPropValueStr(Opmap,"amount"));//项目总费用
                				if(StringUtils.isNotBlank(Opmap.get("amount").toString())){
        							Double pric = Double.parseDouble(Opmap.get("amount").toString());
        							BigDecimal amt=BigDecimal.valueOf(pric);
        							amtAcc=amtAcc.add(amt);
        							amtRec=amtRec.add(amt);
        						}

                        		blOpDtList.add(lbVo);
                    		}
                		}

                		rec.setLbBlCgIpVo(blOpDtList);
                        rec.setRecipeFee(amtRec.toString());
                        recList.add(rec);
                	}
                }else{
                	//查询患者就诊信息
        			String masSql="SELECT pv.name_emp_phy,dept.name_dept,to_char(pv.date_begin,'YYYY-MM-DD') date_begin FROM pv_encounter pv INNER JOIN BD_OU_DEPT dept on dept.pk_dept=pv.pk_dept where pv.pk_pv= ?";
        			Map<String, Object> PatMap = DataBaseHelper.queryForMap(masSql, requ.getVisitId());
        			resTemVo.setVisitId(requ.getVisitId());//就诊主键
        			resTemVo.setDeptName(LbSelfUtil.getPropValueStr(PatMap,"nameDept"));//科室
                	resTemVo.setDoctName(LbSelfUtil.getPropValueStr(PatMap,"nameEmpPhy"));//医生
                	//就诊日期
                	resTemVo.setVisitDate(LbSelfUtil.getPropValueStr(PatMap,"dateBegin"));
                }

                	String sql = "select itemcate.name as item_type,dt.* from bl_op_dt dt INNER JOIN bd_itemcate itemcate ON itemcate.pk_itemcate = dt.pk_itemcate where dt.flag_settle='0' and dt.pk_settle is NULL and dt.pk_cnord is NULL and dt.pk_pv =?";
            		List<Map<String, Object>> blOpList=DataBaseHelper.queryForList(sql, requ.getVisitId());
            		if(blOpList.size()>0){
            			LbRecipesVo rec = new LbRecipesVo();
            			List<LbBlCgIpVo> blOpDtList = new ArrayList<>();
            			BigDecimal amtRec = BigDecimal.ZERO;
            			rec.setRecipeId("1");
            			rec.setRecipeName("其他");
            			rec.setRecipeTime(resTemVo.getVisitDate());
                        rec.setExecDept(resTemVo.getDeptName());

            			for(Map<String, Object> Opmap:blOpList){
            				LbBlCgIpVo lbVo = new LbBlCgIpVo();
            				lbVo.setItemId(LbSelfUtil.getPropValueStr(Opmap,"pkCgop"));//项目主键
            				lbVo.setItemType(LbSelfUtil.getPropValueStr(Opmap,"itemType"));//项目类别
            				lbVo.setItemName(LbSelfUtil.getPropValueStr(Opmap,"nameCg"));//项目名称
            				lbVo.setSpecs(LbSelfUtil.getPropValueStr(Opmap,"spec"));//规格
            				lbVo.setQty(LbSelfUtil.getPropValueStr(Opmap,"quan"));//项目数量
            				lbVo.setPrice(LbSelfUtil.getPropValueStr(Opmap,"price"));//项目单价
            				lbVo.setItemFee(LbSelfUtil.getPropValueStr(Opmap,"amount"));//项目总费用
            				if(StringUtils.isNotBlank(Opmap.get("amount").toString())){
    							Double pric = Double.parseDouble(Opmap.get("amount").toString());
    							BigDecimal amt=BigDecimal.valueOf(pric);
    							amtAcc=amtAcc.add(amt);
    							amtRec=amtRec.add(amt);
    						}
                    		blOpDtList.add(lbVo);
                		}

            			rec.setLbBlCgIpVo(blOpDtList);
                        rec.setRecipeFee(amtRec.toString());
                        recList.add(rec);
            		}else{
            			if(opCgDayDetail==null || opCgDayDetail.size()<=0){
            				return XmlUtil.beanToXml(LbSelfUtil.common(Constant.RESFAIL, "暂无待缴费用！"), LbSHResponseVo.class, false);
            			}
                    }

    		resTemVo.setTotalFee(amtAcc.toString());//总费用
        	resTemVo.setRecipes(recList);
    		resTemList.add(resTemVo);
        	responseVo.setLbUnpaidResTemVo(resTemList);
		} catch (Exception e) {
			logger.info("查询患者门诊待缴费记录:"+e);
			return XmlUtil.beanToXml(LbSelfUtil.common(Constant.RESFAIL, "失败"), LbSHResponseVo.class, false);
		}
		responseVo.setCommonVo(LbSelfUtil.commonVo(Constant.RESSUC, "成功"));
		resXml=XmlUtil.beanToXml(responseVo, responseVo.getClass(), false);
		logger.info("查询患者门诊待缴费记录:{}",resXml);
		return resXml;
	}

	/*
	 * 自助机住院预交金充值保存
	 */
	@Override
	public String RechargeInpatientDeposit(String param) {
		logger.info("自助机住院预交金充值保存:"+param);
		LbSHRequestVo requ = null;
		String resXml=null;
		LbSHResponseVo responseVo = new LbSHResponseVo();
		BlDeposit vo = new BlDeposit();
		if(StringUtils.isNotBlank(param)){
			requ = (LbSHRequestVo)XmlUtil.XmlToBean(param, LbSHRequestVo.class);
			if(CommonUtils.isEmptyString(requ.getDeviceid())){
				return XmlUtil.beanToXml(LbSelfUtil.common(Constant.RESFAIL, "未获取到自助机编码"), LbSHResponseVo.class, false);
			}
			if(CommonUtils.isEmptyString(requ.getPayAmt())){
				return XmlUtil.beanToXml(LbSelfUtil.common(Constant.RESFAIL, "未获取充值金额"), LbSHResponseVo.class, false);
			}
			if(CommonUtils.isEmptyString(requ.getPayType())){
				return XmlUtil.beanToXml(LbSelfUtil.common(Constant.RESFAIL, "未获取到收费类型"), LbSHResponseVo.class, false);
			}
		}else{
			return XmlUtil.beanToXml(LbSelfUtil.common("0", "未获取到信息"), LbSHResponseVo.class, false);
		}

		//查询患者就诊信息
		Map<String, Object> PatMap = DataBaseHelper.queryForMap("select * from pv_encounter pv left join pi_master pi on pv.pk_pi=pi.pk_pi and pi.del_flag='0' where pv.eu_pvtype= '3' and (pv.eu_status = '0' or pv.eu_status = '1') and pi.code_ip= ?", requ.getInPatientNo());
		if(!BeanUtils.isNotNull(PatMap)){
			logger.info("自助机住院预交金保存失败："+param);
			return XmlUtil.beanToXml(LbSelfUtil.common(Constant.RESFAIL, "未查询到该患者有效信息"), LbSHResponseVo.class, false);
		}
		User user = LbSelfUtil.getDefaultUser(requ.getDeviceid());
		if(!BeanUtils.isNotNull(user)){
			return XmlUtil.beanToXml(LbSelfUtil.common(Constant.RESFAIL, "该自助机编码未注册请联系管理员"), LbSHResponseVo.class, false);
		}
		UserContext.setUser(user);
        try {
    			/**
    			 * 获取当前可用票据
    			 */
    			//Map<String, Object> queryForMap = invPubForWsService.getCanUsedEmpinv("5", "自助机"+requ.getDeviceid(), user.getPkEmp(), "0", user.getPkOrg());

    			String sql = "select inv.inv_prefix,inv.cur_no,cate.length,inv.cnt_use,inv.pk_empinv,inv.pk_invcate, cate.prefix from bl_emp_invoice inv "
    					+ "inner join bd_invcate cate on inv.pk_invcate=cate.pk_invcate where inv.del_flag = '0' and inv.pk_org = ? "
    					+ "and inv.flag_use ='1' and inv.flag_active ='1' and cate.eu_type = ? and inv.pk_emp_opera = ?";
    			Map<String, Object> queryForMap = new HashMap<String, Object>();

    			queryForMap = DataBaseHelper.queryForMap(sql, user.getPkOrg(), Constant.HOSPREPAY, user.getPkEmp());

    			if(queryForMap!=null){
    			String curNo = null;
    				if(queryForMap.get("curNo") == null){
    					return XmlUtil.beanToXml(LbSelfUtil.common(Constant.RESFAIL, "当前存在可用票据，但是当前号为空！"), LbSHResponseVo.class, false);
    				}else{
    					curNo = queryForMap.get("curNo").toString();
    				}
    				//票据号=票据分类前缀+票据前缀+号段组成
    				String prefix = queryForMap.get("prefix") == null?"":queryForMap.get("prefix").toString(); //票据分类前缀
    				String invPrefix = queryForMap.get("invPrefix") == null?"":queryForMap.get("invPrefix").toString();
    				if(queryForMap.get("length") != null){
    					long length = Long.parseLong(queryForMap.get("length").toString());
    					curNo = invPubForWsService.flushLeft("0", length, curNo);
    					String curCodeInv = prefix + invPrefix + curNo;
    					queryForMap.put("curCodeInv", curCodeInv);
    				}else{
    					String curCodeInv = prefix + invPrefix + curNo;
    					queryForMap.put("curCodeInv", curCodeInv);
    				}
    			}else{
    				return XmlUtil.beanToXml(LbSelfUtil.common(Constant.RESFAIL, "已无可用票据！，请联系管理员维护发票信息！"), LbSHResponseVo.class, false);
    			}

    			//确认使用票据服务{ "pkEmpinv":"领用主键", "cnt":"使用张数" }
    			//invPubForWsService.LbconfirmUseEmpInv(queryForMap.get("pkEmpinv").toString(), (long)1,user);

    			Long cnt = (long)1;

    			Map<String, Object> empinv = DataBaseHelper.queryForMap("select cnt_use,pk_emp_opera from bl_emp_invoice where del_flag = '0' and pk_empinv = ?", queryForMap.get("pkEmpinv").toString());
    			Long cntUse = Long.parseLong(empinv.get("cntUse").toString());
    			String pkEmpOpera = empinv.get("pkEmpOpera").toString();
    			if (cntUse - cnt < 0) {
    				return XmlUtil.beanToXml(LbSelfUtil.common(Constant.RESFAIL, "更新后的票据可用张数为" + (cntUse - cnt) + "，小于0！发票不足！"), LbSHResponseVo.class, false);
    			}

    			/**
        		 * 住院预交金写表BL_DEPOSIT
        		 */
    			vo =blPubForWsService.LbInsertBlDeposit(Constant.OTHERINV,vo,queryForMap,requ,PatMap,user);
    			ResponseJson  result =  apputil.execService("BL", "BlPrePayService", "saveDeposit", vo,user);
     			if(result.getStatus()== Constant.SUC){
     				BlDeposit bldVo = JsonUtil.readValue(JsonUtil.writeValueAsString(result.getData()), BlDeposit.class);
     				if(("7").equals(requ.getPayType()) || ("8").equals(requ.getPayType())){
        				/**
            			 * 支付信息写入外部支付接口记录表bl_ext_pay
            			 */
                 		 blPubForWsService.LbPayment(bldVo,requ,PatMap,user,null);
        			}

             		 responseVo.setTranSerNo(bldVo.getCodeDepo());//收款时生成流水号
                     responseVo.setOperateTime(DateUtils.getDateTimeStr(bldVo.getCreateTime()));//操作时间
                     List<Map<String, Object>> blDepoList = DataBaseHelper.queryForList("SELECT * FROM BL_DEPOSIT WHERE EU_DIRECT=1 and PK_PV=?", bldVo.getPkPv());
                     BigDecimal amtAcc = BigDecimal.ZERO;
                     for(Map<String, Object> map : blDepoList){
                     	Double pric = Double.parseDouble(map.get("amount").toString());
             			BigDecimal amt=BigDecimal.valueOf(pric);
             			amtAcc=amtAcc.add(amt);
                     }
                     responseVo.setBalance(amtAcc.toString());
     			}else{
     				resXml=XmlUtil.beanToXml(LbSelfUtil.common(Constant.RESFAIL, result.getDesc()),LbSHResponseVo.class, false);
					logger.info("自助机住院预交金保存失败{}",resXml);
					   return resXml;
     			}
		} catch (Exception e) {
			// TODO: handle exception
			logger.info("自助机住院预交金保存失败：{}",e.getMessage());
			return XmlUtil.beanToXml(LbSelfUtil.common(Constant.RESFAIL, "自助机住院预交金保存失败!请联系相关管理人"), LbSHResponseVo.class, false);
		}

        //组织响应消息
        responseVo.setCommonVo(LbSelfUtil.commonVo(Constant.RESSUC, "添加成功"));
        resXml=XmlUtil.beanToXml(responseVo, LbSHResponseVo.class, false);
        logger.info("自助机住院预交金充值保存:{}",resXml);
		return resXml;
	}
	/*
	 * 自助机住院预交金查询
	 */
	@Override
	public String QueryRechargeRecords(String param) {
		// TODO Auto-generated method stub
		logger.info("自助机住院预交金查询:"+param);
		LbSHRequestVo requ = null;
		LbSHResponseVo responseVo = new LbSHResponseVo();
		//患者就诊信息
		Map<String, Object> PatMap = null;

		if(StringUtils.isNotBlank(param)){
			requ = (LbSHRequestVo)XmlUtil.XmlToBean(param, LbSHRequestVo.class);
			if(CommonUtils.isEmptyString(requ.getPatientId())){
				if(CommonUtils.isEmptyString(requ.getInPatientNo())){
					return XmlUtil.beanToXml(LbSelfUtil.common(Constant.RESFAIL, "未获取患者有效信息，请联系管理员"), LbSHResponseVo.class, false);
				}else{
					PatMap = DataBaseHelper.queryForMap("select * from pv_encounter pv left join pi_master pi on pv.pk_pi=pi.pk_pi and pi.del_flag='0' where pv.eu_pvtype= '3' and (pv.eu_status = '0' or pv.eu_status = '1') and pi.code_ip= ?", requ.getInPatientNo());
				}
			}else{
			 PatMap = DataBaseHelper.queryForMap("select * from pv_encounter where eu_pvtype= '3' and (eu_status = '0' or eu_status = '1') and pk_pi= ?", requ.getPatientId());
			}
		}else{
			return XmlUtil.beanToXml(LbSelfUtil.common(Constant.RESFAIL, "未获取到信息"), LbSHResponseVo.class, false);
		}
		if(null != PatMap){
			List<LbQueBlBeposit> lbList = new ArrayList<>();
			List<Map<String, Object>> blDepoList = blPubForWsMapper.LbgetBldeposit(LbSelfUtil.getPropValueStr(PatMap,"pkPv"));
			for(Map<String, Object> map : blDepoList){
				LbQueBlBeposit LbBlbeposit = new LbQueBlBeposit();
				LbBlbeposit.setTranSerNo(LbSelfUtil.getPropValueStr(map,"CODE_DEPO"));//流水号
				LbBlbeposit.setInvoiceNo(LbSelfUtil.getPropValueStr(map,"REPT_NO"));//发票号
				LbBlbeposit.setAmount(LbSelfUtil.getPropValueStr(map,"AMOUNT"));//金额
				LbBlbeposit.setPayType(LbSelfUtil.getPropValueStr(map,"PAYMODE"));//支付方式
				LbBlbeposit.setOperateTime(LbSelfUtil.getPropValueStr(map,"DATE_PAY"));//操作日期
				LbBlbeposit.setOperator(LbSelfUtil.getPropValueStr(map,"NAME_EMP_PAY"));//操作员
				LbBlbeposit.setReserved(LbSelfUtil.getPropValueStr(map,"NOTE"));//备注
				lbList.add(LbBlbeposit);
				LbBlbeposit=null;
	        }
			responseVo.setQueBlBeposit(lbList);
		}else{
			return XmlUtil.beanToXml(LbSelfUtil.common(Constant.RESFAIL, "未查询到该患者有效信息"), LbSHResponseVo.class, false);
		}
		//组织响应消息
        responseVo.setCommonVo(LbSelfUtil.commonVo(Constant.RESSUC, "成功"));
        logger.info("自助机住院预交金查询:"+XmlUtil.beanToXml(responseVo, LbSHResponseVo.class, true));
        return XmlUtil.beanToXml(responseVo, LbSHResponseVo.class, true);
	}
	/**
	 * 查询预约号源记录
	 * 灵璧自助机外部接口xml调用处理
	 * @param param
	 * @return
	 */
	@Override
	public String QueryRegisteredRecords(String param) {
		// TODO Auto-generated method stub
		logger.info("查询预约号源记录:"+param);
		LbSHRequestVo requ = null;
		LbSHResponseVo responseVo = new LbSHResponseVo();
		if(StringUtils.isNotBlank(param)){
			requ = (LbSHRequestVo)XmlUtil.XmlToBean(param, LbSHRequestVo.class);
			if(CommonUtils.isEmptyString(requ.getPatientId())){
				return XmlUtil.beanToXml(LbSelfUtil.common(Constant.RESFAIL, "未获取到患者信息"), LbSHResponseVo.class, false);
			}
		}else{
			return XmlUtil.beanToXml(LbSelfUtil.common(Constant.RESFAIL, "未获取到信息"), LbSHResponseVo.class, false);
		}

		DateTime dt = DateTime.now();
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("patientId", requ.getPatientId());
		//判断是否传递时间，默认当前日期
		if(BeanUtils.isNotNull(requ.getRegDate())){
			paramMap.put("nowDate", requ.getRegDate());
		}else{
			paramMap.put("nowDate", dt.toString("YYYY-MM-dd"));// 当天的日期
		}
		List<LBQueSchVo> queSchList = new ArrayList<>();
		try {
			// 科室医生号源查询
			queSchList = schPubForWsService.getQuerySchAppt(paramMap);
			responseVo.setQueSchVo(queSchList);
		} catch (Exception e) {
			logger.info("查询预约号源记录:"+e);
			return XmlUtil.beanToXml(LbSelfUtil.commonVo(Constant.RESFAIL, "失败"),
					RespCommonVo.class, true);
		}
		responseVo.setCommonVo(LbSelfUtil.commonVo(Constant.RESSUC, "成功"));
		logger.info("查询预约号源记录:"+XmlUtil.beanToXml(responseVo, responseVo.getClass(), false));
		return XmlUtil.beanToXml(responseVo, responseVo.getClass(), false);
	}

	/**
	 * 预约挂号
	 * 灵璧自助机外部接口xml调用处理
	 * @param param
	 * @return
	 */
	@Override
	public String AppointmentRegister(String param) {
		// TODO Auto-generated method stub
		logger.info("预约挂号{}",param);
		LbSHRequestVo requ = null;
		String resXml=null;
		LbSHResponseVo responseVo = new LbSHResponseVo();
		if(StringUtils.isNotBlank(param)){
			requ = (LbSHRequestVo)XmlUtil.XmlToBean(param, LbSHRequestVo.class);
			if(CommonUtils.isEmptyString(requ.getDeviceid())){
				return XmlUtil.beanToXml(LbSelfUtil.common(Constant.RESFAIL, "未获取到自助机编码"), LbSHResponseVo.class, false);
			}
			if (StringUtils.isBlank(requ.getRegId())) {
				return XmlUtil.beanToXml(LbSelfUtil.common(Constant.RESFAIL, "未获取到号源信息"),LbSHResponseVo.class, false);
			}
			if (StringUtils.isBlank(requ.getDeptCode())) {
				return XmlUtil.beanToXml(LbSelfUtil.common(Constant.RESFAIL, "未获取到科室编码信息"),LbSHResponseVo.class, false);
			}
			if (StringUtils.isBlank(requ.getPayAmt())) {
				return XmlUtil.beanToXml(LbSelfUtil.common(Constant.RESFAIL, "未获取支付金额信息"),LbSHResponseVo.class, false);
			}
			if (StringUtils.isBlank(requ.getPayType())) {
				return XmlUtil.beanToXml(LbSelfUtil.common(Constant.RESFAIL, "未获取到支付方式"),LbSHResponseVo.class, false);
			}
			if (StringUtils.isBlank(requ.getPatientId())) {
				return XmlUtil.beanToXml(LbSelfUtil.common(Constant.RESFAIL, "未获取到患者有效信息"),LbSHResponseVo.class, false);
			}
		}else{
			return XmlUtil.beanToXml(LbSelfUtil.common(Constant.RESFAIL, "未获取到信息"), LbSHResponseVo.class, false);
		}
		User user = LbSelfUtil.getDefaultUser(requ.getDeviceid());
		UserContext.setUser(user);

		LbPiMasterRegVo regvo = DataBaseHelper.queryForBean("SELECT * FROM pi_master pi LEFT JOIN PI_INSURANCE hp on hp.pk_pi = pi.pk_pi and hp.del_flag = '0' WHERE pi.del_flag = '0' and pi.pk_pi = ?", LbPiMasterRegVo.class, new Object[]{requ.getPatientId()});
		//查询自费医保计划主键
		Map<String, Object> bdhpMap = DataBaseHelper.queryForMap("SELECT * FROM bd_hp WHERE eu_hptype=? and del_flag='0' ", "0");
		//2020-07-17 lb门诊挂号默认医保类型未自费
		regvo.setPkHp(LbSelfUtil.getPropValueStr(bdhpMap,"pkHp"));

		//儿科年龄上限
		Map<String, Object> cardTypeMap = DataBaseHelper.queryForMap("select val from bd_sysparam where del_flag = '0' and  code='PV0019' and pk_org=?",user.getPkOrg());
		//儿科科室
		Map<String, Object> codeTypeMap = DataBaseHelper.queryForMap("select val from bd_sysparam where del_flag = '0' and  code='CN0001' and pk_org=?",user.getPkOrg());

		try {
			if(null != codeTypeMap){
				   String card[] = codeTypeMap.get("val").toString().split(",");
				   for (int i = 0; i < card.length; i++) {
						if((card[i]).equals(requ.getDeptCode())){
							int age = DateUtils.getAge(regvo.getBirthDate());
							int Maximum = Integer.valueOf(cardTypeMap.get("val").toString());
							if(age-Maximum>0){
								logger.info("预约挂号所选挂号科室年龄最大:{},岁，患者实际：{},岁自助机挂号年龄：{}",Maximum,age,param);
								return XmlUtil.beanToXml(LbSelfUtil.common(Constant.RESFAIL, "所选挂号科室年龄最大:"+Maximum+"岁，患者实际："+age+"岁"), LbSHResponseVo.class, false);
							}
						}
					}
			}
			Map<String, Object> paramMap = new HashMap<String, Object>();
			paramMap.put("pkSch", requ.getRegId());
			//根据号源主键查新排班信息
			List<Map<String, Object>> schPlanList = schMapper.LbgetSchPlanInfo(paramMap);
			if(schPlanList.size()<=0){
				logger.info("预约挂号号源信息有误：{}",param);
				 return XmlUtil.beanToXml(LbSelfUtil.common(Constant.RESFAIL, "未查询到相关排班信息，请重新获取"), LbSHResponseVo.class, false);
			}
			if(schPlanList.get(0)==null){
				 logger.info("预约挂号号源信息有误：{}",param);
				 return XmlUtil.beanToXml(LbSelfUtil.common(Constant.RESFAIL, "号源信息有误，请重新获取"), LbSHResponseVo.class, false);
			}
			 regvo.setDateReg(new Date());//挂号日期--排班日期
			 regvo.setPkDept(LbSelfUtil.getPropValueStr(schPlanList.get(0),"pkDept"));//挂号科室
			 regvo.setEuSchclass("0");//排班类型
			 regvo.setPkDateslotsec(requ.getPhaseCode());//时段主键
				if(LbSelfUtil.getPropValueStr(schPlanList.get(0),"euSrvtype").equals("9")){
				   regvo.setEuPvtype("2");//就诊类型
				}else{
				   regvo.setEuPvtype("1");//就诊类型
				}
			 regvo.setDtPaymode(requ.getPayType());//支付类型
			 regvo.setPkSchres(LbSelfUtil.getPropValueStr(schPlanList.get(0),"pkSchres"));
			 regvo.setPkDateslot(LbSelfUtil.getPropValueStr(schPlanList.get(0),"pkDateslot"));//日期分组
			 regvo.setTicketNo(LbSelfUtil.getPropValueStr(schPlanList.get(0),"ticketno"));//预约票号
			 regvo.setPkSch(LbSelfUtil.getPropValueStr(schPlanList.get(0),"pkSch"));//排班主键

			 regvo.setPkSchsrv(LbSelfUtil.getPropValueStr(schPlanList.get(0),"pkSchsrv"));//排班服务主键

			 BigDecimal amt=BigDecimal.ZERO;
			 if(!CommonUtils.isEmptyString(requ.getPayAmt()) && !("0.0").equals(requ.getPayAmt()) && !("0").equals(requ.getPayAmt())&& !("0.00").equals(requ.getPayAmt())){
				 BlDeposit dep =new BlDeposit();
				 Double pric = Double.parseDouble(requ.getPayAmt());
				 amt=BigDecimal.valueOf(pric);

				 //账户支付可用余额校验
				 if(("4").equals(requ.getPayType())){
					 logger.info("预约挂号挂号暂不支持账户支付{}",param);
					 return XmlUtil.beanToXml(LbSelfUtil.common(Constant.RESFAIL, "挂号暂不支持账户支付"), LbSHResponseVo.class, false);
				}else if(!("1").equals(requ.getPayType()) && !("4").equals(requ.getPayType())){
					 dep.setPayInfo(requ.getQrCodeInfoVo().get(0).getFlowno());
				 }


				//门诊诊察费明细
				List<ItemPriceVo>  itemList = bdPubForWsMapper.LbgetBdItemInfo(LbSelfUtil.getPropValueStr(schPlanList.get(0),"pkSchsrv"));
				for(ItemPriceVo vo:itemList){
					vo.setRatioDisc(1D);
					vo.setRatioSelf(1D);
					vo.setNameCg(vo.getName());
					vo.setPriceOrg(vo.getPrice());
				}
				regvo.setItemList(itemList);
				regvo.setInvStatus("-2");

				dep.setAmount(amt);
				dep.setDtPaymode(requ.getPayType());//支付方式
				List<BlDeposit> depList =new ArrayList<>();
				depList.add(dep);
				regvo.setDepositList(depList);
			 }

			 //判断是否允许挂号
				String date = regvo.getDateAppt()!=null?DateUtils.formatDate(regvo.getDateAppt(), "yyyyMMdd"): (regvo.getDateReg()!=null?DateUtils.formatDate(regvo.getDateReg(), "yyyyMMdd"):null);
				if(StringUtils.isNotBlank(date)) {
					if(DataBaseHelper.queryForScalar("select count(*) from PV_ENCOUNTER t WHERE t.PK_PI=? AND to_char(t.DATE_BEGIN,'yyyyMMdd') =? AND t.PK_DEPT=? AND t.EU_PVTYPE in('1','2') AND t.EU_STATUS in('0','1')",
							Integer.class, new Object[]{regvo.getPkPi(),date,regvo.getPkDept()}) >0){
						logger.info("预约挂号该患者已经存在当前日期和科室的挂号记录！{}",param);
						return XmlUtil.beanToXml(LbSelfUtil.common(Constant.RESFAIL, "该患者已经存在当前日期和科室的挂号记录！"), LbSHResponseVo.class, false);
					}
				}

				//预约挂号
				ResponseJson  NowSjh =  apputil.execService("PV", "RegSyxHandler", "saveApptPvRegInfo",regvo,user);
				if(NowSjh.getStatus()==Constant.SUC){
			    LbPiMasterRegVo regvos = JsonUtil.readValue(JsonUtil.writeValueAsString(NowSjh.getData()), LbPiMasterRegVo.class);
				responseVo.setCodeNo(regvos.getCodePv());//门诊流水号
				//查询患者就诊信息
				//DataBaseHelper.queryForMap("select * from pv_encounter pv left join pi_master pi on pv.pk_pi=pi.pk_pi and pi.del_flag='0' where pv.eu_pvtype= '3' and (pv.eu_status = '0' or pv.eu_status = '1') and pi.pk_pi= ?", requ.getPatientId());
				Map<String, Object> PatMap = new HashMap<String, Object>();
				PatMap.put("pkPi", regvo.getPkPi());
				PatMap.put("pkPv", regvo.getPkPv());
				PatMap.put("pkEmp", regvo.getPkEmp());

				if(("7").equals(requ.getPayType()) || ("8").equals(requ.getPayType())){
					/**
					 * 支付信息写入外部支付接口记录表bl_ext_pay
					 */
					BlDeposit vo =regvos.getDepositList().get(0);
				 	blPubForWsService.LbPayment(vo,requ,PatMap,user,null);
				}
				 Map<String, Object> piCardMap = DataBaseHelper.queryForMap("select CARD_NO from PI_CARD where FLAG_ACTIVE = '1' AND EU_STATUS ='0' and DT_CARDTYPE ='01' and pk_pi=? ORDER BY create_time DESC", new Object[]{requ.getPatientId()});
		 		 if(piCardMap != null){
		 			responseVo.setDtcardNo(LbSelfUtil.getPropValueStr(piCardMap,"cardNo"));
		 		 }else{
		 			responseVo.setDtcardNo(regvo.getCodeOp());
		 		 }
		 		 responseVo.setTotalFee(requ.getPayAmt());
				 responseVo.setDeptName(LbSelfUtil.getPropValueStr(schPlanList.get(0),"name"));//挂后科室名称
				 responseVo.setLocation(LbSelfUtil.getPropValueStr(schPlanList.get(0),"namePlace"));//候诊地点
				 responseVo.setWaitNo("预"+regvos.getTicketNo());
				 responseVo.setDoctName(LbSelfUtil.getPropValueStr(schPlanList.get(0),"nameEmp"));//医生姓名
				 responseVo.setTypeName(LbSelfUtil.getPropValueStr(schPlanList.get(0),"stvName"));
				 responseVo.setCodeOp(regvo.getCodeOp());
		    	 responseVo.setCommonVo(LbSelfUtil.commonVo(Constant.RESSUC, "操作成功！"));

		    	}else{
					resXml=XmlUtil.beanToXml(LbSelfUtil.common(Constant.RESFAIL, NowSjh.getDesc()),LbSHResponseVo.class, false);
					logger.info("预约挂号{}",resXml);
					   return resXml;
				}
		} catch(Exception e){
			 logger.info("自助机挂号失败：{},{}",param,e.getMessage());
			return XmlUtil.beanToXml(LbSelfUtil.common(Constant.RESFAIL, "预约挂号失败！请联系管理员"), LbSHResponseVo.class, false);
		}
		resXml=XmlUtil.beanToXml(responseVo, LbSHResponseVo.class, false);
		logger.info("预约挂号{}",resXml);
		return resXml;
	}
	/**
	 * 预约登记
	 * 灵璧自助机外部接口xml调用处理
	 * @param param
	 * @return
	 */
	@Override
	public String AppointmentRegistration(String param) {
		// TODO Auto-generated method stub
		logger.info("预约登记{}",param);
		LbSHRequestVo requ = null;
		String resXml=null;
		LbSHResponseVo responseVo = new LbSHResponseVo();
		if(StringUtils.isNotBlank(param)){
			requ = (LbSHRequestVo)XmlUtil.XmlToBean(param, LbSHRequestVo.class);
			if(CommonUtils.isEmptyString(requ.getDeviceid())){
				return XmlUtil.beanToXml(LbSelfUtil.common(Constant.RESFAIL, "未获取到有效编码"), LbSHResponseVo.class, false);
			}
			if (StringUtils.isBlank(requ.getRegId())) {
				return XmlUtil.beanToXml(LbSelfUtil.common(Constant.RESFAIL, "未获取到号源信息"),LbSHResponseVo.class, false);
			}
			if (StringUtils.isBlank(requ.getPatientId())) {
				return XmlUtil.beanToXml(LbSelfUtil.common(Constant.RESFAIL, "未获取到患者有效信息"),LbSHResponseVo.class, false);
			}
		}else{
			return XmlUtil.beanToXml(LbSelfUtil.common(Constant.RESFAIL, "未获取到信息"), LbSHResponseVo.class, false);
		}
		User user = LbSelfUtil.getDefaultUser(requ.getDeviceid());
		UserContext.setUser(user);
		try {
			List<Map<String, Object>> schAppList =DataBaseHelper.queryForList("SELECT * FROM sch_appt WHERE pk_sch=? and pk_pi=?", new Object[]{requ.getRegId(),requ.getPatientId()});
			if(schAppList.size()>0){
				return XmlUtil.beanToXml(LbSelfUtil.common(Constant.RESFAIL, "当前排班已预约"), LbSHResponseVo.class, false);
			}
			LbPiMasterRegVo regvo =DataBaseHelper.queryForBean("SELECT * FROM pi_master pi LEFT JOIN PI_INSURANCE hp on hp.pk_pi = pi.pk_pi and hp.del_flag = '0' WHERE pi.del_flag = '0' and pi.pk_pi = ?", LbPiMasterRegVo.class, new Object[]{requ.getPatientId()});

            Map<String, Object> paramMap = new HashMap<String, Object>();
			paramMap.put("pkSch", requ.getRegId());
		  //根据号源主键查新排班信息
		  List<Map<String, Object>> getSch = schMapper.LbTodaySchInfosByDate(paramMap);
		   if(getSch.size()>0){
				//儿科年龄上限
				Map<String, Object> cardTypeMaps = DataBaseHelper.queryForMap("select val from bd_sysparam where del_flag = '0' and  code='PV0019' and pk_org=?",user.getPkOrg());
				//儿科科室
				Map<String, Object> codeTypeMap = DataBaseHelper.queryForMap("select val from bd_sysparam where del_flag = '0' and  code='CN0001' and pk_org=?",user.getPkOrg());
				if(null != codeTypeMap){
					   String card[] = codeTypeMap.get("val").toString().split(",");
					   for (int i = 0; i < card.length; i++) {
							if((card[i]).equals(getSch.get(0).get("codeDept"))){
								int age = DateUtils.getAge(regvo.getBirthDate());
								int Maximum = Integer.valueOf(cardTypeMaps.get("val").toString());
								if(age-Maximum>0){
									logger.info("预约挂号所选挂号科室年龄最大:{},岁，患者实际：{},岁自助机挂号年龄：{}",Maximum,age,param);
									return XmlUtil.beanToXml(LbSelfUtil.common(Constant.RESFAIL, "所选挂号科室年龄最大:"+Maximum+"岁，患者实际："+age+"岁"), LbSHResponseVo.class, false);
								}
							}
						}
				}

				regvo.setEuSchclass("0");//排班类型
				if(LbSelfUtil.getPropValueStr(getSch.get(0),"euSrvtype").equals("9")){
				   regvo.setEuPvtype("2");//就诊类型
				}else{
				   regvo.setEuPvtype("1");//就诊类型
				}
				regvo.setPkSch(LbSelfUtil.getPropValueStr(getSch.get(0), "pkSch"));
				regvo.setPkSchplan(LbSelfUtil.getPropValueStr(getSch.get(0), "pkSchplan"));
				regvo.setPkPi(regvo.getPkPi());
				regvo.setIdNo(requ.getCardNo());
				regvo.setPkSchsrv(LbSelfUtil.getPropValueStr(getSch.get(0), "pkSchsrv"));
				regvo.setPkSchres(LbSelfUtil.getPropValueStr(getSch.get(0), "pkSchres"));
				regvo.setPkDateslot(LbSelfUtil.getPropValueStr(getSch.get(0), "pkDateslot"));
				regvo.setPkDateslotsec(requ.getPhaseCode());//时段主键
				regvo.setDateAppt(DateUtils.parseDate(LbSelfUtil.getPropValueStr(getSch.get(0), "dateWork"),"yyyy-MM-dd"));//预约日期


				ResponseJson  NowSjh =  apputil.execService("PV", "RegSyxHandler", "saveApptSchRegInfo",regvo,user);
				if(NowSjh.getStatus()==Constant.SUC){
					Map<String, Object> pamMap = (Map<String, Object>)LbSelfUtil.beanToMap1(NowSjh.getData());
					responseVo.setOrderCode(LbSelfUtil.getPropValueStr(pamMap,"apptCode"));//预约订单编码
					responseVo.setCardType("01");//证件号码类型
					responseVo.setCardNo(requ.getCardNo());//身份证
					Map<String, Object> piCardMap = DataBaseHelper.queryForMap("select CARD_NO from PI_CARD where FLAG_ACTIVE = '1' AND EU_STATUS ='0' and DT_CARDTYPE ='01' and pk_pi=? ORDER BY create_time DESC", new Object[]{regvo.getPkPi()});
					responseVo.setDtCardType("01");//院内卡类型
					if(piCardMap != null){
		    			responseVo.setDtcardNo(LbSelfUtil.getPropValueStr(piCardMap,"cardNo"));
		    		}else{
		    			responseVo.setDtcardNo(regvo.getCodeOp());
		    		}
					responseVo.setOperTime(LbSelfUtil.getPropValueStr(getSch.get(0), "dateWork"));//预约时间
					responseVo.setRangeTime(LbSelfUtil.getPropValueStr(getSch.get(0),"timeBegin").substring(0, 5)+"-"+LbSelfUtil.getPropValueStr(getSch.get(0),"timeEnd").substring(0, 5));//就诊时间段hh:mm:ss-hh:mm:ss
					responseVo.setTotalFee(LbSelfUtil.getPropValueStr(getSch.get(0),"price"));//挂号费
					Map<String, Object> cardTypeMap = DataBaseHelper.queryForMap("SELECT ticket_no FROM sch_appt WHERE code=?",responseVo.getOrderCode());
					responseVo.setWaitNo(LbSelfUtil.getPropValueStr(cardTypeMap,"ticketNo"));//诊号
					//responseVo.setPatientName(regvo.getNamePi());//患者姓名
					responseVo.setDeptName(LbSelfUtil.getPropValueStr(getSch.get(0),"nameDept"));//科室名称
					responseVo.setDoctName(LbSelfUtil.getPropValueStr(getSch.get(0),"nameEmp"));//医生名称
					responseVo.setCommonVo(LbSelfUtil.commonVo(Constant.RESSUC, "操作成功！"));
				}else{
					resXml=XmlUtil.beanToXml(LbSelfUtil.common(Constant.RESFAIL, NowSjh.getDesc()), LbSHResponseVo.class, false);
					logger.info("预约登记{}",resXml);
					return resXml;
				}
		   }else{
			   resXml=XmlUtil.beanToXml(LbSelfUtil.common(Constant.RESFAIL, "未查询到可预约号源"), LbSHResponseVo.class, false);
			   logger.info("预约登记{}",resXml);
			   return resXml;
		   }
		} catch (Exception e) {
			// TODO: handle exception
			 resXml=XmlUtil.beanToXml(LbSelfUtil.common(Constant.RESFAIL, "未查询到可预约号源"), LbSHResponseVo.class, false);
			 logger.error("预约登记{}",e.getMessage());
			 return resXml;
		}
		resXml=XmlUtil.beanToXml(responseVo, LbSHResponseVo.class, false);
		logger.info("预约登记{}",resXml);
		resXml=XmlUtil.beanToXml(responseVo, LbSHResponseVo.class, false);
		return resXml;
	}
	//自助机网络测试
	@Override
    public String NetTest(String param) {
		// TODO Auto-generated method stub
		/*OtherRespJson respJson = new OtherRespJson();
		respJson.setStatus(Constant.RESFAIL);
		respJson.setDesc("成功");
		return respJson.toString();*/
		return XmlUtil.beanToXml(LbSelfUtil.common(Constant.RESSUC, "成功"), LbSHResponseVo.class, false);
	}

	//查询住院费用清单
	@Override
	public String QueryInpatientBillList(String param) {
		logger.info("查询住院费用清单:"+param);
		// TODO Auto-generated method stub
		LbSHRequestVo requ = null;
		LbSHResponseVo responseVo = new LbSHResponseVo();
		if(StringUtils.isNotBlank(param)){
			requ = (LbSHRequestVo)XmlUtil.XmlToBean(param, LbSHRequestVo.class);
			if(CommonUtils.isEmptyString(requ.getInPatientNo())){
				return XmlUtil.beanToXml(LbSelfUtil.common(Constant.RESFAIL, "未获住院号"), LbSHResponseVo.class, false);
			}
			if(CommonUtils.isEmptyString(requ.getStartDate())){
				return XmlUtil.beanToXml(LbSelfUtil.common(Constant.RESFAIL, "未获到开始时间"), LbSHResponseVo.class, false);
			}
			if(CommonUtils.isEmptyString(requ.getEndDate())){
				return XmlUtil.beanToXml(LbSelfUtil.common(Constant.RESFAIL, "未获到结束时间"), LbSHResponseVo.class, false);
			}
		}else{
			return XmlUtil.beanToXml(LbSelfUtil.common(Constant.RESFAIL, "未获取到信息"), LbSHResponseVo.class, false);
		}
		requ.setStartDate(DateUtils.getDateTimeStr(DateUtils.strToDate(requ.getStartDate(), "yyyy-MM-dd")));
		requ.setEndDate(DateUtils.getDateTimeStr(DateUtils.strToDate(requ.getEndDate(),"yyyy-MM-dd")).substring(0, 8)+"235959");
		Map<String, Object> map = LbSelfUtil.toMap(requ);
		List<LbBlCgIpVo>  blCgIpList = new ArrayList<>();
		try {
			if(null != requ.getPageInfo()){
			   LbPageInfo page = requ.getPageInfo().get(0);
				if(!StringUtils.isEmpty(page.getPageStart())){
					int pageIndex = CommonUtils.getInteger(page.getPageStart());
					int pageSize = CommonUtils.getInteger(page.getPageSize());
					// 分页操作
					MyBatisPage.startPage(pageIndex, pageSize);
				}
			}
		    List<Map<String,Object>> result =  blPubForWsMapper.LbqueryBlCgIpSummer(map);
		    responseVo.setTotalNum(String.valueOf(result.size()));
			if (result.size()>0) {
				for(Map<String, Object> blmap : result) {
					//住院费明细响应消息体
					LbBlCgIpVo blCpIp = new LbBlCgIpVo();
					blCpIp = (LbBlCgIpVo)LbSelfUtil.mapToBean(blmap,blCpIp);
					blCpIp.setOperateTime(DateUtils.formatDate(DateUtils.parseDate(blmap.get("operateTime").toString()),new Object[]{}));
					blCgIpList.add(blCpIp);
				}
			}else{
				return XmlUtil.beanToXml(LbSelfUtil.common(Constant.RESFAIL, "未查到有效信息"), LbSHResponseVo.class, false);
			}
		} catch (Exception e) {
			// TODO: handle exception
			logger.info("查询住院费用清单:"+e);
			return XmlUtil.beanToXml(LbSelfUtil.common(Constant.RESFAIL, "失败"),LbSHResponseVo.class, false);
		}
		//组织响应消息
		responseVo.setLbBlCgIpVo(blCgIpList);
        responseVo.setCommonVo(LbSelfUtil.commonVo(Constant.RESSUC, "成功"));
        logger.info("查询住院费用清单:"+XmlUtil.beanToXml(responseVo, LbSHResponseVo.class, false));
        return XmlUtil.beanToXml(responseVo, LbSHResponseVo.class, false);
	}
    //查询已缴费门诊列表
	@Override
	public String QueryPaymentRecordList(String param) {
		logger.info("查询已缴费门诊列表记:"+param);
		// TODO Auto-generated method stub
		LbSHRequestVo requ = null;
		String resXml=null;
		LbSHResponseVo responseVo = new LbSHResponseVo();
		if(StringUtils.isNotBlank(param)){
			requ = (LbSHRequestVo)XmlUtil.XmlToBean(param, LbSHRequestVo.class);
			if (CommonUtils.isEmptyString(requ.getVisitId())) {
				return XmlUtil.beanToXml(LbSelfUtil.common(Constant.RESFAIL, "未获取到就诊信息"),LbSHResponseVo.class, false);
			}
			if(CommonUtils.isEmptyString(requ.getStartDate())){
				return XmlUtil.beanToXml(LbSelfUtil.common(Constant.RESFAIL, "未获到开始时间"), LbSHResponseVo.class, false);
			}
			if(CommonUtils.isEmptyString(requ.getEndDate())){
				return XmlUtil.beanToXml(LbSelfUtil.common(Constant.RESFAIL, "未获到结束时间"), LbSHResponseVo.class, false);
			}
		}else{
			return XmlUtil.beanToXml(LbSelfUtil.common(Constant.RESFAIL, "未获取到信息"), LbSHResponseVo.class, false);
		}
		List<LbQueOpUnpaidResTemVo> resTemList = new ArrayList<>();
		try {
			requ.setSettle("1");
			requ.setStartDate(DateUtils.getDateTimeStr(DateUtils.strToDate(requ.getStartDate(), "yyyy-MM-dd")));
			requ.setEndDate(DateUtils.getDateTimeStr(DateUtils.strToDate(requ.getEndDate(),"yyyy-MM-dd")).substring(0, 8)+"235959");

			List<LbRecipesVo>  recList = new ArrayList<>();

			Map<String, Object> pamMap = LbSelfUtil.toMap(requ);
			BigDecimal amtAcc = BigDecimal.ZERO;
			LbQueOpUnpaidResTemVo resTemVo = new LbQueOpUnpaidResTemVo();

    			List<Map<String, Object>> opCgDayDetail = cnPubForWsMapper.LbqueryCnOrderUnpaid(pamMap);
    			if(opCgDayDetail.size()>0){
                	resTemVo.setVisitId(LbSelfUtil.getPropValueStr(opCgDayDetail.get(0),"visitId"));//就诊主键

                	resTemVo.setDeptName(LbSelfUtil.getPropValueStr(opCgDayDetail.get(0),"deptName"));//科室
                	resTemVo.setDoctName(LbSelfUtil.getPropValueStr(opCgDayDetail.get(0),"doctName"));//医生
                	//就诊日期
                	resTemVo.setVisitDate(DateUtils.formatDate(DateUtils.parseDate(opCgDayDetail.get(0).get("visitDate").toString()),new Object[]{}));
                	for(Map<String, Object> map :opCgDayDetail){
                		LbRecipesVo rec = new LbRecipesVo();
                		List<LbBlCgIpVo> blOpDtList = new ArrayList<>();
                		BigDecimal amtRec = BigDecimal.ZERO;
                		rec = (LbRecipesVo)LbSelfUtil.mapToBean(map,rec);
                		rec.setRecipeTime(DateUtils.formatDate(DateUtils.parseDate(map.get("recipeTime").toString()),new Object[]{}));

                		String sql = "select itemcate.name as item_type,dt.* from bl_op_dt dt INNER JOIN bd_itemcate itemcate ON itemcate.pk_itemcate = dt.pk_itemcate where pk_cnord =?";
                		List<Map<String, Object>> blOpList=DataBaseHelper.queryForList(sql, rec.getRecipeId());
                		if(blOpList.size()>0){
                			for(Map<String, Object> Opmap:blOpList){
                				LbBlCgIpVo lbVo = new LbBlCgIpVo();
                				lbVo.setItemId(LbSelfUtil.getPropValueStr(Opmap,"pkCgop"));//项目主键
                				lbVo.setItemType(LbSelfUtil.getPropValueStr(Opmap,"itemType"));//项目类别
                				lbVo.setItemName(LbSelfUtil.getPropValueStr(Opmap,"nameCg"));//项目名称
                				lbVo.setSpecs(LbSelfUtil.getPropValueStr(Opmap,"spec"));//规格
                				lbVo.setQty(LbSelfUtil.getPropValueStr(Opmap,"quan"));//项目数量
                				lbVo.setPrice(LbSelfUtil.getPropValueStr(Opmap,"price"));//项目单价
                				lbVo.setItemFee(LbSelfUtil.getPropValueStr(Opmap,"amount"));//项目总费用
                				if(StringUtils.isNotBlank(Opmap.get("amount").toString())){
        							Double pric = Double.parseDouble(Opmap.get("amount").toString());
        							BigDecimal amt=BigDecimal.valueOf(pric);
        							amtAcc=amtAcc.add(amt);
        							amtRec=amtRec.add(amt);
        						}

                        		blOpDtList.add(lbVo);
                    		}
                		}

                		rec.setLbBlCgIpVo(blOpDtList);
                        rec.setRecipeFee(amtRec.toString());
                        recList.add(rec);
                	}
                }else{
                	//查询患者就诊信息
        			String masSql="SELECT pv.name_emp_phy,dept.name_dept,to_char(pv.date_begin,'YYYY-MM-DD') date_begin FROM pv_encounter pv INNER JOIN BD_OU_DEPT dept on dept.pk_dept=pv.pk_dept where pv.pk_pv= ?";
        			Map<String, Object> PatMap = DataBaseHelper.queryForMap(masSql, requ.getVisitId());
        			resTemVo.setVisitId(requ.getVisitId());//就诊主键
        			resTemVo.setDeptName(LbSelfUtil.getPropValueStr(PatMap,"nameDept"));//科室
                	resTemVo.setDoctName(LbSelfUtil.getPropValueStr(PatMap,"nameEmpPhy"));//医生
                	//就诊日期
                	resTemVo.setVisitDate(LbSelfUtil.getPropValueStr(PatMap,"dateBegin"));
                }

                	String sql = "select itemcate.name as item_type,dt.* from bl_op_dt dt INNER JOIN bd_itemcate itemcate ON itemcate.pk_itemcate = dt.pk_itemcate where dt.flag_settle='1' and dt.pk_cnord is NULL and dt.pk_pv =?";
            		List<Map<String, Object>> blOpList=DataBaseHelper.queryForList(sql, requ.getVisitId());
            		if(blOpList.size()>0){
            			LbRecipesVo rec = new LbRecipesVo();
            			List<LbBlCgIpVo> blOpDtList = new ArrayList<>();
            			BigDecimal amtRec = BigDecimal.ZERO;
            			rec.setRecipeId("1");
            			rec.setRecipeName("其他");
            			rec.setRecipeTime(resTemVo.getVisitDate());
                        rec.setExecDept(resTemVo.getDeptName());

            			for(Map<String, Object> Opmap:blOpList){
            				LbBlCgIpVo lbVo = new LbBlCgIpVo();
            				lbVo.setItemId(LbSelfUtil.getPropValueStr(Opmap,"pkCgop"));//项目主键
            				lbVo.setItemType(LbSelfUtil.getPropValueStr(Opmap,"itemType"));//项目类别
            				lbVo.setItemName(LbSelfUtil.getPropValueStr(Opmap,"nameCg"));//项目名称
            				lbVo.setSpecs(LbSelfUtil.getPropValueStr(Opmap,"spec"));//规格
            				lbVo.setQty(LbSelfUtil.getPropValueStr(Opmap,"quan"));//项目数量
            				lbVo.setPrice(LbSelfUtil.getPropValueStr(Opmap,"price"));//项目单价
            				lbVo.setItemFee(LbSelfUtil.getPropValueStr(Opmap,"amount"));//项目总费用
            				if(StringUtils.isNotBlank(Opmap.get("amount").toString())){
    							Double pric = Double.parseDouble(Opmap.get("amount").toString());
    							BigDecimal amt=BigDecimal.valueOf(pric);
    							amtAcc=amtAcc.add(amt);
    							amtRec=amtRec.add(amt);
    						}
                    		blOpDtList.add(lbVo);
                		}

            			rec.setLbBlCgIpVo(blOpDtList);
                        rec.setRecipeFee(amtRec.toString());
                        recList.add(rec);
            		}else{
            			if(opCgDayDetail==null || opCgDayDetail.size()<=0){
            				return XmlUtil.beanToXml(LbSelfUtil.common(Constant.RESFAIL, "暂无已缴费用！"), LbSHResponseVo.class, false);
            			}
                    }

    		resTemVo.setTotalFee(amtAcc.toString());//总费用
        	resTemVo.setRecipes(recList);
    		resTemList.add(resTemVo);
        	responseVo.setLbUnpaidResTemVo(resTemList);
		} catch (Exception e) {
			// TODO: handle exception
			logger.info(e+"查询已缴费门诊列表记:"+param);
			return XmlUtil.beanToXml(LbSelfUtil.common(Constant.RESFAIL, "失败"),LbSHResponseVo.class, false);
		}
		responseVo.setCommonVo(LbSelfUtil.commonVo(Constant.RESSUC, "成功"));
		resXml=XmlUtil.beanToXml(responseVo, responseVo.getClass(), false);
		logger.info("查询已缴费门诊列表记:"+resXml);
		return resXml;
	}
    //门诊支付
	@Override
	public String Payment(String param) {
		logger.info("门诊支付:"+param);
		LbSHRequestVo requ = null;
		LbSHResponseVo responseVo = new LbSHResponseVo();
		if(StringUtils.isNotBlank(param)){
			requ = (LbSHRequestVo)XmlUtil.XmlToBean(param, LbSHRequestVo.class);
			if(CommonUtils.isEmptyString(requ.getDeviceid())){
				return XmlUtil.beanToXml(LbSelfUtil.common(Constant.RESFAIL, "未获取到自助机编码"), LbSHResponseVo.class, false);
			}
			if (CommonUtils.isEmptyString(requ.getVisitId())) {
				return XmlUtil.beanToXml(LbSelfUtil.common(Constant.RESFAIL, "未获取到就诊信息"),LbSHResponseVo.class, false);
			}
			if(CommonUtils.isEmptyString(requ.getRecipeIds())){
				return XmlUtil.beanToXml(LbSelfUtil.common(Constant.RESFAIL, "未获取到项目编码"), LbSHResponseVo.class, false);
			}
			if(CommonUtils.isEmptyString(requ.getPayAmt())){
				return XmlUtil.beanToXml(LbSelfUtil.common(Constant.RESFAIL, "未获取到支付金额金额"), LbSHResponseVo.class, false);
			}
			if(CommonUtils.isEmptyString(requ.getPayType())){
				return XmlUtil.beanToXml(LbSelfUtil.common(Constant.RESFAIL, "未获取到收费类型"), LbSHResponseVo.class, false);
			}
		}else{
			return XmlUtil.beanToXml(LbSelfUtil.common(Constant.RESFAIL, "未获取到信息"), LbSHResponseVo.class, false);
		}
		User user = LbSelfUtil.getDefaultUser(requ.getDeviceid());
		UserContext.setUser(user);

		if(!BeanUtils.isNotNull(user)){
			return XmlUtil.beanToXml(LbSelfUtil.common(Constant.RESFAIL, "该自助机编码未注册请联系管理员"), LbSHResponseVo.class, false);
		}
		//查询患者就诊信息
		String masSql="select ma.code_op,ous.code_emp,dept.name_dept,pv.* from pv_encounter pv INNER JOIN pi_master ma on ma.pk_pi=pv.pk_pi INNER JOIN BD_OU_DEPT dept on dept.pk_dept=pv.pk_dept INNER JOIN bd_ou_employee ous on ous.pk_emp=pv.pk_emp_phy where pk_pv= ?";
		Map<String, Object> PatMap = DataBaseHelper.queryForMap(masSql, requ.getVisitId());
		if(!BeanUtils.isNotNull(PatMap)){
			logger.info("未查到患信息："+param);
			return XmlUtil.beanToXml(LbSelfUtil.common(Constant.RESFAIL, "未查询到该患者有效信息"), LbSHResponseVo.class, false);
		}
		try {
			List<BlOpDt> noSettleBlOpDts  = new ArrayList<BlOpDt>();//未结算集合
			List<BlOpDt> noPkcnordBlOpDts  = new ArrayList<BlOpDt>();//未结算集合
			Set<String> noSettlePkBlOpDt = new HashSet<String>();
			BigDecimal amtAcc = BigDecimal.ZERO;
			Set<String> pkBlOpDt = new HashSet<String>();
			Set<String> pkCnordS = new HashSet<String>();
				//判断指定字符是否出现在该字符串的第一位  是--返回下标1   否--返回下标0
				int beginIndex = requ.getRecipeIds().indexOf(",") == 0 ? 1 : 0;
				//判断指定字符是否出现在该字符串的最后一位  是--返回出现的位置   否--返回字符长度
				int endIndex = requ.getRecipeIds().lastIndexOf(",") + 1 == requ.getRecipeIds().length() ? requ.getRecipeIds().lastIndexOf(",") : requ.getRecipeIds().length();
				String source = requ.getRecipeIds().substring(beginIndex, endIndex);
				String recip[] = requ.getRecipeIds().split(",");
				for (String recipeid : recip) {
					if(pkBlOpDt.add(recipeid)){
					 String sql = "select * from bl_op_dt where pk_cgop='"+recipeid+"'";
					 List<BlOpDt> blOpDts = DataBaseHelper.queryForList(sql, BlOpDt.class, new Object[] {});
					 BigDecimal Payamt = BigDecimal.ZERO;
					 for (BlOpDt blOpDt : blOpDts) {
							if(Constant.RESFAIL.equals(blOpDt.getFlagSettle())&& null == blOpDt.getPkSettle()){
								if(null != blOpDt.getAmount()){
									BigDecimal amts=BigDecimal.valueOf(blOpDt.getAmount());
									BigDecimal amt=BigDecimal.valueOf(blOpDt.getAmount());
									amtAcc=amtAcc.add(amt);
									Payamt=Payamt.add(amts);
								}
								if(blOpDt.getPkCnord()!=null){
									pkCnordS.add(blOpDt.getPkCnord());
								}else{
									noPkcnordBlOpDts.add(blOpDt);
								}
								noSettleBlOpDts.add(blOpDt);
								noSettlePkBlOpDt.add(blOpDt.getPkCgop());
								}
						}
					}
				}

			if(noSettleBlOpDts ==null || noSettleBlOpDts.size()<=0){
				return XmlUtil.beanToXml(LbSelfUtil.common(Constant.RESFAIL, "未查询都收费项目，请重新查询！"), LbSHResponseVo.class, false);
			}else{
				if(!MathUtils.equ(amtAcc.doubleValue(), Double.valueOf(requ.getPayAmt()))){
					logger.info("门诊支付失败："+amtAcc+"c:->"+requ.getPayAmt()+"消息体："+param);
					return XmlUtil.beanToXml(LbSelfUtil.common(Constant.RESFAIL, "支付金额有误,请核对！"), LbSHResponseVo.class, false);
				}
			}
			List<BlDeposit> BlDepositList = new ArrayList<>();//支付信息
			BlDeposit blDeposit = new BlDeposit();
			OpCgTransforVo opCgTransforVo =new OpCgTransforVo();
			if(!("1").equals(requ.getPayType()) && !("4").equals(requ.getPayType())){
				if(requ.getInsurePayment()!=null && requ.getInsurePayment()>0){
					blDeposit.setAmount(new BigDecimal(requ.getSelfPayment()));//自费金额
					opCgTransforVo.setAmtInsuThird(BigDecimal.valueOf(requ.getInsurePayment()));//医保支付金额
				}else{
					blDeposit.setAmount(amtAcc);//自费金额
					opCgTransforVo.setAmtInsuThird(BigDecimal.valueOf(0D));//医保支付金额
				}
			     //支付信息写入外部支付接口记录表bl_ext_pay
				 blPubForWsService.LbPayment(null,requ,PatMap,user,null);
				 blDeposit.setPayInfo(requ.getQrCodeInfoVo().get(0).getOrderno());//订单号
				 //blDeposit.setAmount(new BigDecimal(requ.getQrCodeInfoVo().get(0).getPayamt()));//支付金额
			}else if(("4").equals(requ.getPayType())){
				//账户余额查询
				ResponseJson  blResult =  apputil.execService("bl", "PareAccoutService", "getPatiAccountAvailableBalance", PatMap,null);
				if(blResult.getStatus()== Constant.SUC){
				    Map<String, Object> pamMap = (Map<String, Object>)blResult.getData();
				    if(pamMap.isEmpty()){
				    	logger.info("门诊支付账户校验失败,患者账户异常："+param);
						return XmlUtil.beanToXml(LbSelfUtil.common(Constant.RESFAIL, "患者账户异常！！"), LbSHResponseVo.class, false);
					   }
					//查询押金不可用押金
				    String sql=" select nvl(sum(DEPOSIT) ,0) as deposit from PI_CARD where FLAG_ACTIVE in ('0','1') and EU_STATUS in ('0','1','2') and PK_PI= ?";
					Map<String,Object > cardMap=DataBaseHelper.queryForMap(sql,PatMap.get("pkPi"));
					BigDecimal nAmout=BigDecimal.valueOf(Double.valueOf(String.valueOf(cardMap.get("deposit"))));
					BigDecimal BalanceAmout =BigDecimal.valueOf(Double.valueOf(LbSelfUtil.getPropValueStr(pamMap,"acc")));
					if(requ.getInsurePayment()!=null && requ.getInsurePayment()>0){
						if(requ.getSelfPayment().compareTo(BalanceAmout.subtract(nAmout).doubleValue()) == 1){
							logger.info("门诊账户支付失败："+amtAcc+"c:->"+BalanceAmout.subtract(nAmout)+"消息体："+param);
							return XmlUtil.beanToXml(LbSelfUtil.common(Constant.RESFAIL, "可用余额不足，请选择其他方式支付！"), LbSHResponseVo.class, false);
						}
						 blDeposit.setAmount(new BigDecimal(requ.getSelfPayment()));//自费金额
						opCgTransforVo.setAmtInsuThird(BigDecimal.valueOf(requ.getInsurePayment()));//医保支付金额
					}else{
						if(amtAcc.compareTo(BalanceAmout.subtract(nAmout)) == 1){
							logger.info("门诊账户支付失败："+amtAcc+"c:->"+BalanceAmout.subtract(nAmout)+"消息体："+param);
							return XmlUtil.beanToXml(LbSelfUtil.common(Constant.RESFAIL, "可用余额不足，请选择其他方式支付！"), LbSHResponseVo.class, false);
						}
						 blDeposit.setAmount(amtAcc);//自费金额
						opCgTransforVo.setAmtInsuThird(BigDecimal.valueOf(0D));//医保支付金额
					}
				}else{
					logger.info("门诊支付失败,获取账户收据失败："+param);
					return XmlUtil.beanToXml(LbSelfUtil.common(Constant.RESFAIL, "获取数据失败！请重试"), LbSHResponseVo.class, false);
				}
			}
			blDeposit.setDtPaymode(requ.getPayType());//支付方式
			BlDepositList.add(blDeposit);
			BlPubSettleVo blPubSettleVo = new BlPubSettleVo();
			blPubSettleVo.setPkPi(LbSelfUtil.getPropValueStr(PatMap,"pkPi"));
			blPubSettleVo.setPkPv(LbSelfUtil.getPropValueStr(PatMap,"pkPv"));
			blPubSettleVo.setBlOpDts(noSettleBlOpDts);
			//blPubSettleVo.setInvoiceInfo(invoList); // 获取发票列表
			blPubSettleVo.setDepositList(BlDepositList);
			blPubSettleVo.setPkBlOpDtInSql(CommonUtils.convertSetToSqlInPart(noSettlePkBlOpDt, "pk_cgop"));//收费明细主键
			blPubSettleVo.setEuPvType(requ.getPayType());//支付方式
			opCgTransforVo.setPkPi(LbSelfUtil.getPropValueStr(PatMap,"pkPi"));
			opCgTransforVo.setPkPv(LbSelfUtil.getPropValueStr(PatMap,"pkPv"));
			opCgTransforVo.setBlOpDts(noSettleBlOpDts);
			opCgTransforVo.setBlDeposits(BlDepositList);
			opCgTransforVo.setInvStatus("-2");

			ResponseJson  result =  apputil.execService("BL", "opCgSettlementService", "mergeOpcgAccountedSettlement", opCgTransforVo,user);
			if(result.getStatus()== Constant.SUC){
				//生成熊响应消息
			    List<LbRecipesVo>  recList = new ArrayList<>();
			    if(noPkcnordBlOpDts.size()>0){
			    	LbRecipesVo rec = new LbRecipesVo();
			    	BigDecimal amtBlOp = BigDecimal.ZERO;
			    	List<LbBlCgIpVo> blOpDtList = new ArrayList<>();
			    	 for(BlOpDt blOpDt:noPkcnordBlOpDts){
					    	if(blOpDt.getPkCnord()==null){
					    		LbBlCgIpVo lbVo = new LbBlCgIpVo();

		        				lbVo.setItemName(blOpDt.getNameCg());//项目名称
		        				lbVo.setSpecs(blOpDt.getSpec());//规格
		        				if(blOpDt.getQuan() !=null){
		        					lbVo.setQty(blOpDt.getQuan().toString());//项目数量
		        				}
		        				if(blOpDt.getPrice()!=null){
		        					lbVo.setPrice(blOpDt.getPrice().toString());//项目单价
		        				}
		        				if(blOpDt.getAmount()!=null){
		        					lbVo.setItemFee(blOpDt.getAmount().toString());//项目总费用
		        				}
		                		blOpDtList.add(lbVo);
		                		if(StringUtils.isNotBlank(lbVo.getItemFee())){
			               			 Double pric = Double.parseDouble(lbVo.getItemFee());
			                  		  BigDecimal amt=BigDecimal.valueOf(pric);
			                  		  amtBlOp=amtBlOp.add(amt);
			               		}
					    	}
					    }
			    	    rec.setRecipeId("1");
			    	    rec.setRecipeName("其他");
			    	    rec.setRecipeTime(DateUtils.formatDate(DateUtils.parseDate(LbSelfUtil.getPropValueStr(PatMap,"dateBegin")),new Object[]{}));
		        		rec.setExecDept(LbSelfUtil.getPropValueStr(PatMap,"nameDept"));//执行科室
	            		rec.setRecipeFee(amtBlOp.toString());
	            		rec.setLbBlCgIpVo(blOpDtList);

	            		recList.add(rec);
			    }

			    for(String pkCnord :pkCnordS){
				    LbRecipesVo rec = new LbRecipesVo();
				    List<LbBlCgIpVo> blOpDtList = new ArrayList<>();
	        	    Map<String, Object>  cnordMap = new HashMap<String, Object>();
	        	    BigDecimal amtBlOp = BigDecimal.ZERO;
	        	    cnordMap.put("pkCnord",pkCnord);
	        	    List<Map<String, Object>> opCgDayDetail = cnPubForWsMapper.LbqueryCnOrderUnpaid(cnordMap);
	        	    rec = (LbRecipesVo)LbSelfUtil.mapToBean(opCgDayDetail.get(0),rec);
	        		rec.setRecipeTime(DateUtils.formatDate(DateUtils.parseDate(opCgDayDetail.get(0).get("recipeTime").toString()),new Object[]{}));

	        		String sql = "select itemcate.name as item_type,dt.* from bl_op_dt dt INNER JOIN bd_itemcate itemcate ON itemcate.pk_itemcate = dt.pk_itemcate where pk_cnord =?";
	        		List<Map<String, Object>> blOpList=DataBaseHelper.queryForList(sql, pkCnord);
	        		if(blOpList.size()>0){
	        			for(Map<String, Object> Opmap:blOpList){
	        				LbBlCgIpVo lbVo = new LbBlCgIpVo();
	        				lbVo.setItemType(LbSelfUtil.getPropValueStr(Opmap,"itemType"));//项目类别
	        				lbVo.setItemName(LbSelfUtil.getPropValueStr(Opmap,"nameCg"));//项目名称
	        				lbVo.setSpecs(LbSelfUtil.getPropValueStr(Opmap,"spec"));//规格
	        				lbVo.setQty(LbSelfUtil.getPropValueStr(Opmap,"quan"));//项目数量
	        				lbVo.setPrice(LbSelfUtil.getPropValueStr(Opmap,"price"));//项目单价
	        				lbVo.setItemFee(LbSelfUtil.getPropValueStr(Opmap,"amount"));//项目总费用
	                		blOpDtList.add(lbVo);
	                		if(StringUtils.isNotBlank(lbVo.getItemFee())){
	               			 Double pric = Double.parseDouble(lbVo.getItemFee());
	                  		  BigDecimal amt=BigDecimal.valueOf(pric);
	                  		  amtBlOp=amtBlOp.add(amt);
	               		     }
	            		}
	        		}
	        		rec.setRecipeFee(amtBlOp.toString());//费用
	        		rec.setLbBlCgIpVo(blOpDtList);
	        		recList.add(rec);
	        	}

        		responseVo.setRecipes(recList);
        		responseVo.setDoctName(LbSelfUtil.getPropValueStr(PatMap,"nameEmpPhy"));//医生名称
	    		responseVo.setDoctCode(LbSelfUtil.getPropValueStr(PatMap,"codeEmp"));//医生编码
	    		responseVo.setTotalFee(amtAcc.toString());//总费用
	    		responseVo.setCodeOp(LbSelfUtil.getPropValueStr(PatMap,"codeOp"));
	        	//responseVo.setInvoiceNo(queryForMap.get("curCodeInv").toString());//发票号
	    		Map<String, Object> piCardMap = DataBaseHelper.queryForMap("select CARD_NO from PI_CARD where FLAG_ACTIVE = '1' AND EU_STATUS ='0' and DT_CARDTYPE ='01' and pk_pi=? ORDER BY create_time DESC", new Object[]{LbSelfUtil.getPropValueStr(PatMap,"pkPi")});
	    		 if(piCardMap != null){
	    			responseVo.setDtcardNo(LbSelfUtil.getPropValueStr(piCardMap,"cardNo"));
	    		 }else{
	    			responseVo.setDtcardNo(LbSelfUtil.getPropValueStr(PatMap,"codeOp"));
	    		 }
	        	responseVo.setCommonVo(LbSelfUtil.commonVo(Constant.RESSUC, "成功"));
			}else{
				if(BeanUtils.isNotNull(requ.getQrCodeInfoVo())){
					//修改bl_ext_pay表flag_pay为'0'
					Map<String, Object> paramExtPay =new HashMap<String, Object>();
					paramExtPay.put("tradeNo", requ.getQrCodeInfoVo().get(0).getFlowno());
					paramExtPay.put("serialNo", requ.getQrCodeInfoVo().get(0).getOrderno());
					blPubForWsService.lbUpdateBlExtPay(paramExtPay);
				}
				logger.info("门诊支付失败："+param);
			    return XmlUtil.beanToXml(LbSelfUtil.common(Constant.RESFAIL,result.getDesc()), LbSHResponseVo.class, false);
			}
	    } catch (Exception e) {
		// TODO: handle exception
		    logger.info("门诊支付失败："+e);
		    return XmlUtil.beanToXml(LbSelfUtil.common(Constant.RESFAIL, "门诊收费失败!"), LbSHResponseVo.class, false);
	     }
		logger.info("门诊支付:"+XmlUtil.beanToXml(responseVo, LbSHResponseVo.class, false));
		    return XmlUtil.beanToXml(responseVo, LbSHResponseVo.class, false);
	}
    //查询就诊记录
	@Override
	public String QueryClinicRecordList(String param) {
		logger.info("查询当前患者就诊结束状态的门诊就诊信息"+param);
		// TODO Auto-generated method stub
		LbSHRequestVo requ = null;
		LbSHResponseVo responseVo = new LbSHResponseVo();
		if(StringUtils.isNotBlank(param)){
			requ = (LbSHRequestVo)XmlUtil.XmlToBean(param, LbSHRequestVo.class);
			if(CommonUtils.isEmptyString(requ.getPatientId())){
				return XmlUtil.beanToXml(LbSelfUtil.common(Constant.RESFAIL, "未获取到患者信息"), LbSHResponseVo.class, false);
			}
			if(CommonUtils.isEmptyString(requ.getStartDate())){
				return XmlUtil.beanToXml(LbSelfUtil.common(Constant.RESFAIL, "未获到开始时间"), LbSHResponseVo.class, false);
			}
			if(CommonUtils.isEmptyString(requ.getEndDate())){
				return XmlUtil.beanToXml(LbSelfUtil.common(Constant.RESFAIL, "未获到结束时间"), LbSHResponseVo.class, false);
			}
		}else{
			return XmlUtil.beanToXml(LbSelfUtil.common(Constant.RESFAIL, "未获取到信息"), LbSHResponseVo.class, false);
		}
		try {
			requ.setStartDate(DateUtils.getDateTimeStr(DateUtils.strToDate(requ.getStartDate(), "yyyy-MM-dd")));
			requ.setEndDate(DateUtils.getDateTimeStr(DateUtils.strToDate(requ.getEndDate(),"yyyy-MM-dd")).substring(0, 8)+"235959");
			Map<String, Object> map = LbSelfUtil.toMap(requ);
			List<LbQueOpPvInfoResTemVo> opPvList = new ArrayList<>();
			List<Map<String, Object>> queryPiOpList = pvPubForWsMapper.getPvInfoByOpStatus(map);
			if (queryPiOpList.size()>0){
				for(Map<String, Object> PiOpmap:queryPiOpList){
					LbQueOpPvInfoResTemVo piInfo =new LbQueOpPvInfoResTemVo();
					piInfo = (LbQueOpPvInfoResTemVo)LbSelfUtil.mapToBean(PiOpmap, piInfo);
					//查询押金不可用押金
				    String sql="SELECT sum(amount) as amount FROM bl_op_dt WHERE pk_pv=? and pk_settle is NULL and flag_settle ='0'";
				    Map<String,Object > cardMap=DataBaseHelper.queryForMap(sql,piInfo.getVisitId());
				    if(null != cardMap && null != cardMap.get("amount")){
				    	piInfo.setTotalFee(cardMap.get("amount").toString());
				    }else{
				    	piInfo.setTotalFee("无");
				    }
					opPvList.add(piInfo);
				}
				responseVo.setQuePiOpInfo(opPvList);
			}else{
				return XmlUtil.beanToXml(LbSelfUtil.common(Constant.RESFAIL, "未查询到该患者就诊信息"), LbSHResponseVo.class, false);
			}
		} catch (Exception e) {
			// TODO: handle exception
			logger.info("查询当前患者就诊结束状态的门诊就诊信息"+e);
		}
		responseVo.setCommonVo(LbSelfUtil.commonVo(Constant.RESSUC, "成功"));
		logger.info("查询当前患者就诊结束状态的门诊就诊信息"+XmlUtil.beanToXml(responseVo, LbSHResponseVo.class, false));
		return  XmlUtil.beanToXml(responseVo, LbSHResponseVo.class, false);
	}

	//账户充值
	@Override
	public String AccountRecharge(String param) {
		// TODO Auto-generated method stub
		logger.info("账户充值："+param);
		LbSHRequestVo requ = null;
		LbSHResponseVo responseVo = new LbSHResponseVo();
		if(StringUtils.isNotBlank(param)){
			requ = (LbSHRequestVo)XmlUtil.XmlToBean(param, LbSHRequestVo.class);
			if(CommonUtils.isEmptyString(requ.getDeviceid())){
				return XmlUtil.beanToXml(LbSelfUtil.common(Constant.RESFAIL, "未获取到自助机编码"), LbSHResponseVo.class, false);
			}
			if(CommonUtils.isEmptyString(requ.getPatientId())){
				return XmlUtil.beanToXml(LbSelfUtil.common(Constant.RESFAIL, "未获取到患者信息"), LbSHResponseVo.class, false);
			}
			if(CommonUtils.isEmptyString(requ.getPayAmt())){
				return XmlUtil.beanToXml(LbSelfUtil.common(Constant.RESFAIL, "未获取到支付金额金额"), LbSHResponseVo.class, false);
			}
			if(CommonUtils.isEmptyString(requ.getPayType())){
				return XmlUtil.beanToXml(LbSelfUtil.common(Constant.RESFAIL, "未获取到收费类型"), LbSHResponseVo.class, false);
			}
		}else{
			return XmlUtil.beanToXml(LbSelfUtil.common(Constant.RESFAIL, "未获取到信息"), LbSHResponseVo.class, false);
		}
		User user = LbSelfUtil.getDefaultUser(requ.getDeviceid());
		UserContext.setUser(user);

		String sql = "select * from pi_acc where pk_pi = ? and (del_flag = '0' or del_flag is null)";
		PiAcc piAcc = DataBaseHelper.queryForBean(sql, PiAcc.class, requ.getPatientId());
		if(null ==piAcc){
			return XmlUtil.beanToXml(LbSelfUtil.common(Constant.RESFAIL, "账户异常，请联系管理员"), LbSHResponseVo.class, false);
		}
		try {
			//当前收据号
			ResponseJson  NowSjh =  apputil.execService("PI", "CarddealService", "getNowSjh",null,user);

			Map<String, Object> tMap = new HashMap<>();
            tMap.put("euOptype", EnumerateParameter.ONE);
            tMap.put("dtPaymode",  requ.getPayType());
            tMap.put("euDirect", EnumerateParameter.ONE);
            tMap.put("pkPi", requ.getPatientId());
            tMap.put("amount", requ.getPayAmt());
            tMap.put("datePay", new Date());
            tMap.put("pkDept", ((User) user).getPkDept());
            tMap.put("pkEmpPay", ((User) user).getPkEmp());
            tMap.put("nameEmpPay", ((User) user).getNameEmp());
            tMap.put("reptNo", NowSjh.getData());
            if(!CommonUtils.isEmptyString(requ.getPayAmt()) && !("0.0").equals(requ.getPayAmt()) && !("0").equals(requ.getPayAmt())&& !("0.00").equals(requ.getPayAmt())){
				/**
    			 * 支付信息写入外部支付接口记录表bl_ext_pay
    			 */
				if(("7").equals(requ.getPayType()) || ("8").equals(requ.getPayType())){
            	Map<String, Object> patMap = new HashMap<>();
				patMap.put("pkPi", requ.getPatientId());
				requ.getQrCodeInfoVo().get(0).setPaymethod("自助机账户充值"+requ.getPayAmt());
         		blPubForWsService.LbPayment(null,requ,patMap,user,null);

         		tMap.put("payInfo", requ.getQrCodeInfoVo().get(0).getOrderno());
				}
            }

			ResponseJson  result =  apputil.execService("PI", "CarddealService", "saveMonOperation", tMap,user);
			if(result.getStatus()==0){

				//账户余额查询
			   ResponseJson  blResult =  apputil.execService("bl", "PareAccoutService", "getPatiAccountAvailableBalance", tMap,null);
			   if(blResult.getStatus()==0){
				   Map<String, Object> pamMap = (Map<String, Object>)blResult.getData();

				    //查询押金不可用押金
					sql=" select nvl(sum(DEPOSIT) ,0) as deposit from PI_CARD where FLAG_ACTIVE in ('0','1') and EU_STATUS in ('0','1','2') and PK_PI= ?";
					Map<String,Object > map=DataBaseHelper.queryForMap(sql,requ.getPatientId());
					BigDecimal nAmout=BigDecimal.valueOf(Double.valueOf(String.valueOf(map.get("deposit"))));
					BigDecimal BalanceAmout =BigDecimal.valueOf(Double.valueOf(LbSelfUtil.getPropValueStr(pamMap,"acc")));

					responseVo.setBalance(BalanceAmout.subtract(nAmout).toString());//充值余额
			   }

			   responseVo.setTranSerNo(NowSjh.getData().toString());//流水号
			}else{
				logger.info(result.getDesc()+"账户充值失败："+param);
				return XmlUtil.beanToXml(LbSelfUtil.common(Constant.RESFAIL, result.getDesc()), LbSHResponseVo.class, false);
			}
		} catch (Exception e) {
			// TODO: handle exception
			logger.info(e+"账户充值："+param);
		}
		responseVo.setCommonVo(LbSelfUtil.commonVo(Constant.RESSUC, "成功"));
		logger.info("账户充值:"+XmlUtil.beanToXml(responseVo, LbSHResponseVo.class, false));
	    return XmlUtil.beanToXml(responseVo, LbSHResponseVo.class, false);
	}
    //门诊账户余额查询
	@Override
	public String QueryAccount(String param) {
		// TODO Auto-generated method stub
		logger.info("查询患者门诊待缴费记录:"+param);
		LbSHRequestVo requ =null;
		String resXml=null;
		Map<String, Object> paramMap = null;
		LbSHResponseVo responseVo = new LbSHResponseVo();
		if (StringUtils.isNotBlank(param)) {
			requ = (LbSHRequestVo) XmlUtil.XmlToBean(param, LbSHRequestVo.class);
			if (!StringUtils.isBlank(requ.getCardType())) {
				//如果卡类型不为空，则卡号也不能为空
				if (StringUtils.isBlank(requ.getCardNo())) {
					return XmlUtil.beanToXml(LbSelfUtil.common(Constant.RESFAIL, "证件卡类型不为空时，请传入证件号码！"),LbSHResponseVo.class, false);
				}
				//判断是否住院号
				if(requ.getCardType().equals("2")){
					requ.setCodeIp(requ.getCardNo());
					requ.setCardNo(null);
					requ.setCardType(null);
				}
			}else if (!StringUtils.isBlank(requ.getDtCardType())) {
				//如果卡类型不为空，则卡号也不能为空
				if (StringUtils.isBlank(requ.getDtCardNo())) {
					return XmlUtil.beanToXml(LbSelfUtil.common(Constant.RESFAIL, "院内卡类型不为空时，请传入卡号！"),LbSHResponseVo.class, false);
				}
			}else{
				return XmlUtil.beanToXml(LbSelfUtil.common(Constant.RESFAIL, "证件类型为空！"),LbSHResponseVo.class, false);
			}
		    paramMap = LbSelfUtil.toMap(requ);
		}

		List<Map<String, Object>> queryPiMaster = pvPubForWsMapper.queryPiMaster(paramMap);
		if(queryPiMaster.size()>0){
			//账户余额查询
		    Map<String, Object> PatMap=new HashMap<String, Object>();
			PatMap.put("pkPi", queryPiMaster.get(0).get("patientid"));
			ResponseJson  blResult =  apputil.execService("bl", "PareAccoutService", "getPatiAccountAvailableBalance", PatMap,null);
			if(blResult.getStatus()==0){
			    Map<String, Object> pamMap = (Map<String, Object>)blResult.getData();
				   if(pamMap.isEmpty()){
					   logger.info("患者暂无账户，请联系管理员,消息体："+param);
					   return XmlUtil.beanToXml(LbSelfUtil.common(Constant.RESFAIL, "患者暂无账户,"), LbSHResponseVo.class, false);
				   }
				//查询押金不可用押金
			    String sql=" select nvl(sum(DEPOSIT) ,0) as deposit from PI_CARD where FLAG_ACTIVE in ('0','1') and EU_STATUS in ('0','1','2') and PK_PI= ?";
				Map<String,Object > cardMap=DataBaseHelper.queryForMap(sql,queryPiMaster.get(0).get("patientid"));
				BigDecimal nAmout=BigDecimal.valueOf(Double.valueOf(String.valueOf(cardMap.get("deposit"))));
				BigDecimal BalanceAmout =BigDecimal.valueOf(Double.valueOf(LbSelfUtil.getPropValueStr(pamMap,"acc")));
				//账户余额
				responseVo.setBalance(BalanceAmout.subtract(nAmout).toString());
				//账户总额
				responseVo.setRental(BalanceAmout.toString());
				//账户押金
				responseVo.setCashPledge(BalanceAmout.toString());
			}else{
				logger.info("获取账户收据失败："+param);
				return XmlUtil.beanToXml(LbSelfUtil.common(Constant.RESFAIL, "获取数据失败！请重试"), LbSHResponseVo.class, false);
			}
		}
		   responseVo.setCommonVo(LbSelfUtil.commonVo(Constant.RESSUC, "成功"));
		   resXml=XmlUtil.beanToXml(responseVo, responseVo.getClass(), false);
		   logger.info("查询患者门诊待缴费记录:"+resXml);
		   return resXml;
	}
	//退款校验
	@Override
	public String RefundCheck(String param) {
		// TODO Auto-generated method stub
		logger.info("退款校验："+param);
		LbSHRequestVo requ = null;
		String resXml=null;
		LbSHResponseVo responseVo = new LbSHResponseVo();
		if(StringUtils.isNotBlank(param)){
			requ = (LbSHRequestVo)XmlUtil.XmlToBean(param, LbSHRequestVo.class);
		}else{
			resXml=XmlUtil.beanToXml(LbSelfUtil.common(Constant.RESFAIL, "未获取到信息"), LbSHResponseVo.class, false);
			return resXml;
		}
		String sql = "SELECT * FROM bl_ext_pay WHERE serial_no=? or trade_no=?";
		List<Map<String, Object>> blextList= DataBaseHelper.queryForList(sql,requ.getOrderNo(),requ.getFlowNo());
		if(blextList.size()>0){
			responseVo.setReturnState("1");
		}else{
			responseVo.setReturnState("0");
		}
		responseVo.setCommonVo(LbSelfUtil.commonVo(Constant.RESSUC, "成功"));
		resXml=XmlUtil.beanToXml(responseVo, LbSHResponseVo.class, false);
		logger.info("退款校验:"+resXml);
	    return resXml;
	}


	/**
	 * 2.1查询科室列表--便民
	 * 灵璧便民平台
	 * @param param
	 * @return
	 */
	@Override
	public String queryDeptInfos(String param) {
		// TODO Auto-generated method stub
		logger.info("2.1查询科室列表--便民："+param);
		Map<String, Object> reqMap = new HashMap<String ,Object>();
		String resXml=null;
		// 响应结果
		LbBmptResponse responseVo = new LbBmptResponse();

		List<LbBmptDepartment> ListDept = new ArrayList<>();
		reqMap.put("flagOp", "1");
		try {
			List<Map<String, Object>> deptList = bdPubForWsMapper.queryDept(reqMap);

			for (int i = 0; i < deptList.size(); i++) {
				if (deptList.get(i).get("codeDept").toString().length() > 2) {
					// 科室信息
					LbBmptDepartment  Department = new LbBmptDepartment();
					Department.setDepartmentCode(LbSelfUtil.getPropValueStr(deptList.get(i),"codeDept"));
					Department.setDepartmentName(LbSelfUtil.getPropValueStr(deptList.get(i),"nameDept"));
					Department.setDepartmentDesc(LbSelfUtil.getPropValueStr(deptList.get(i),"deptDesc"));
					ListDept.add(Department);
				}
			}
			responseVo.setDepartment(ListDept);
			responseVo.setCycleNum(ListDept.size());
		} catch (Exception e) {
			resXml=XmlUtil.beanToXml(LbSelfUtil.failed(responseVo, "失败"), LbBmptResponse.class, false);
			logger.info("2.1查询科室列表--便民"+e+resXml);
			return resXml;
		}
		resXml=XmlUtil.beanToXml(LbSelfUtil.success(responseVo), responseVo.getClass(), false);
		logger.info("2.1查询科室列表--便民"+resXml);
		return resXml;
	}
	/**
	 * 2.2查询排班列表--便民
	 * 灵璧便民平台
	 * @param param
	 * @return
	 */
	@Override
	public String querySchedules(String param) {
		// TODO Auto-generated method stub
		logger.info("2.2查询排班列表--便民："+param);
		LbBmptRequest requ = null;
		String resXml =null;
		// 响应结果
		LbBmptResponse responseVo = new LbBmptResponse();

		if (StringUtils.isNotBlank(param)) {
			requ = (LbBmptRequest) XmlUtil.XmlToBean(param, LbBmptRequest.class);
		} else{
			resXml =XmlUtil.beanToXml(LbSelfUtil.failed(responseVo, "未获取到信息"), LbBmptResponse.class, false);
			logger.info("2.2查询排班列表--便民："+resXml);
			return resXml;
		}
		try {
			Map<String, Object> paramMap = new HashMap<String, Object>();
			if(DateUtils.formatDate(new Date(), "yyyy-MM-dd").compareTo(requ.getStartDate())>=0){
				paramMap.put("StartDate", DateUtils.addDate(new Date(), 1, 3, "yyyy-MM-dd"));
			}else{
				paramMap.put("StartDate", requ.getStartDate());
			}
			paramMap.put("EndDate", requ.getEndDate());
			paramMap.put("codeDept", requ.getDepartmentCode());// 科室编码
			paramMap.put("doctCode", requ.getDoctorCode());// 医生编码
			if(StringUtils.isNotBlank(requ.getSessionCode())){
				if(requ.getSessionCode().equals("AM")){
					paramMap.put("nameDateslot","上午");
				}else if(requ.getSessionCode().equals("PM")){
					paramMap.put("nameDateslot","下午");
				}
			}
			paramMap.put("total","0");
			paramMap.put("flagStop", "0");//停用标志
			// 科室医生号源查询
			List<LbBmptSchedule> Schedules = schPubForWsService.lbBmptgetQuerySch(paramMap);
			responseVo.setCycleNum(Schedules.size());
			responseVo.setSchedule(Schedules);
		} catch (Exception e) {
			resXml=XmlUtil.beanToXml(LbSelfUtil.common(Constant.RESFAIL, "失败"), LbSHResponseVo.class, false);
			logger.info(e+"2.2查询排班列表--便民："+resXml);
			return resXml;
		}
		resXml=XmlUtil.beanToXml(LbSelfUtil.success(responseVo), responseVo.getClass(), false);
		logger.info("2.2查询排班列表--便民"+resXml);
		return resXml;
	}
	/**
	 * 2.3查询医生可预约时段列表--便民
	 * 灵璧便民平台
	 * @param param
	 * @return
	 */
	@Override
	public String queryAppointTimes(String param) {
		// TODO Auto-generated method stub
		logger.info("2.3查询医生可预约时段列表--便民："+param);
		LbBmptRequest requ = null;
		String resXml =null;
		// 响应结果
		LbBmptResponse responseVo = new LbBmptResponse();

		if (null != param && !("").equals(param)) {
			requ = (LbBmptRequest) XmlUtil.XmlToBean(param, LbBmptRequest.class);
		}
		try {
			Map<String, Object> paramMap = new HashMap<String, Object>();

			if(StringUtils.isNotBlank(requ.getSessionCode())){
				if(requ.getSessionCode().equals("AM")){
					paramMap.put("nameDateslot","上午");
				}else if(requ.getSessionCode().equals("PM")){
					paramMap.put("nameDateslot","下午");
				}
			}
			paramMap.put("nowDate", requ.getServiceDate());
			paramMap.put("codeDept", requ.getDepartmentCode());// 科室编码
			paramMap.put("doctCode", requ.getDoctorCode());// 医生编码
			paramMap.put("total","0");
			paramMap.put("flagStop", "0");//停用标志
			// 科室医生号源查询
			List<LbBmptTimeInfo> TimeInfos = schPubForWsService.lbBmptTimeInfo(paramMap);
			responseVo.setCycleNum(TimeInfos.size());
			responseVo.setTimeInfo(TimeInfos);
		} catch (Exception e) {
			resXml=XmlUtil.beanToXml(LbSelfUtil.common(Constant.RESFAIL, "失败"), LbSHResponseVo.class, false);
			logger.info(e+"2.3查询医生可预约时段列表--便民："+resXml);
			return resXml;
		}
		resXml=XmlUtil.beanToXml(LbSelfUtil.success(responseVo), responseVo.getClass(), false);
		logger.info("2.3查询医生可预约时段列表--便民："+resXml);
		return resXml;
	}

	/**
	 *2.4预约接口--便民
	 * 灵璧便民平台
	 * @param param
	 * @return
	 */
	@Override
	public String bookOrder(String param) {
		// TODO Auto-generated method stub
		logger.info("2.4预约接口--便民："+param);
		LbBmptRequest requ = null;
		String resXml=null;
		LbBmptResponse responseVo = new LbBmptResponse();
		if(StringUtils.isNotBlank(param)){
			requ = (LbBmptRequest)XmlUtil.XmlToBean(param, LbBmptRequest.class);
			if(CommonUtils.isEmptyString(requ.getHospitalMark())){
				return XmlUtil.beanToXml(LbSelfUtil.common(Constant.RESFAIL, "未获取到医院标识"), LbSHResponseVo.class, false);
			}
			if(CommonUtils.isEmptyString(requ.getPatientName())){
				return XmlUtil.beanToXml(LbSelfUtil.common(Constant.RESFAIL, "未获取到患者姓名"), LbSHResponseVo.class, false);
			}
			if(CommonUtils.isEmptyString(requ.getCardNo())){
				return XmlUtil.beanToXml(LbSelfUtil.common(Constant.RESFAIL, "未获取到患者身份证号"), LbSHResponseVo.class, false);
			}
			if(CommonUtils.isEmptyString(requ.getPhone())){
				return XmlUtil.beanToXml(LbSelfUtil.common(Constant.RESFAIL, "未获取到手机号"), LbSHResponseVo.class, false);
			}
			if(CommonUtils.isEmptyString(requ.getServiceDate())){
				return XmlUtil.beanToXml(LbSelfUtil.common(Constant.RESFAIL, "未获取到号源日期"), LbSHResponseVo.class, false);
			}
			if(CommonUtils.isEmptyString(requ.getDepartmentCode())){
				return XmlUtil.beanToXml(LbSelfUtil.common(Constant.RESFAIL, "未获取到科室编码"), LbSHResponseVo.class, false);
			}
			if(CommonUtils.isEmptyString(requ.getDoctorCode())){
				return XmlUtil.beanToXml(LbSelfUtil.common(Constant.RESFAIL, "未获取到医生编码"), LbSHResponseVo.class, false);
			}
			if(CommonUtils.isEmptyString(requ.getStartTime())){
				return XmlUtil.beanToXml(LbSelfUtil.common(Constant.RESFAIL, "未获取到就诊开始时间"), LbSHResponseVo.class, false);
			}
		}else{
			return XmlUtil.beanToXml(LbSelfUtil.common(Constant.RESFAIL, "未获取到信息"), LbSHResponseVo.class, false);
		}
		User user = LbSelfUtil.getDefaultUser(requ.getHospitalMark());
		UserContext.setUser(user);
		try {
			PiMaster pi = new PiMaster();
			pi.setIdNo(requ.getCardNo());
			pi.setDtIdtype("01");// 证件号类型
			pi.setNamePi(requ.getPatientName());//患者姓名
			pi.setMobile(requ.getPhone());//联系方式
			//患者信息校验修改保存
		    pi = lbPubForWsService.lbSavePiMaster(pi,null,user);

            Map<String, Object> paramMap = new HashMap<String, Object>();
			paramMap.put("nowDate", requ.getServiceDate());
			paramMap.put("nowTime", requ.getStartTime());
			paramMap.put("codeDept", requ.getDepartmentCode());// 科室编码
			paramMap.put("doctCode", requ.getDoctorCode());// 医生编码
			paramMap.put("flagStop", "0");//停用标志
		  //根据号源主键查新排班信息
		  List<Map<String, Object>> getSch = schMapper.LbTodaySchInfosByDate(paramMap);
		   if(getSch.size()>0){
			   Map<String, Object> codeTypeMap = DataBaseHelper.queryForMap("select pk_dateslotsec from bd_code_dateslot_sec WHERE pk_dateslot=? and time_begin=? and time_end=?",LbSelfUtil.getPropValueStr(getSch.get(0), "pkDateslot"),requ.getEndTime(),requ.getStartTime());
			    LbPiMasterRegVo regvo =new LbPiMasterRegVo();
				regvo.setEuSchclass("0");//排班类型
				if(LbSelfUtil.getPropValueStr(getSch.get(0),"euSrvtype").equals("9")){
				   regvo.setEuPvtype("2");//就诊类型
				}else{
				   regvo.setEuPvtype("1");//就诊类型
				}
				regvo.setPkSch(LbSelfUtil.getPropValueStr(getSch.get(0), "pkSch"));
				regvo.setPkSchplan(LbSelfUtil.getPropValueStr(getSch.get(0), "pkSchplan"));
				regvo.setPkPi(pi.getPkPi());
				regvo.setIdNo(requ.getCardNo());
				regvo.setPkSchsrv(LbSelfUtil.getPropValueStr(getSch.get(0), "pkSchsrv"));
				regvo.setPkSchres(LbSelfUtil.getPropValueStr(getSch.get(0), "pkSchres"));
				regvo.setPkDateslot(LbSelfUtil.getPropValueStr(getSch.get(0), "pkDateslot"));
				regvo.setDateAppt(DateUtils.parseDate(requ.getServiceDate(),"yyyy-MM-dd"));//预约日期
				if(BeanUtils.isNotNull(codeTypeMap)){
					regvo.setPkDateslotsec(LbSelfUtil.getPropValueStr(codeTypeMap, "pkDateslotsec"));//日期分组时段主键
				}
				regvo.setOrderSource("5");

				ResponseJson  NowSjh =  apputil.execService("PV", "RegSyxHandler", "saveApptSchRegInfo",regvo,user);
				if(NowSjh.getStatus()==Constant.SUC){
					Map<String, Object> pamMap = (Map<String, Object>)LbSelfUtil.beanToMap1(NowSjh.getData());
					responseVo.setOrderCode(LbSelfUtil.getPropValueStr(pamMap,"apptCode"));//预约订单编码
					responseVo.setCardNo(requ.getCardNo());//身份证
					responseVo.setServiceDate(requ.getServiceDate());
					responseVo.setRangeTime(LbSelfUtil.getPropValueStr(getSch.get(0),"timeBegin")+"-"+LbSelfUtil.getPropValueStr(getSch.get(0),"timeEnd"));//就诊时间段hh:mm:ss-hh:mm:ss
					responseVo.setFee(LbSelfUtil.getPropValueStr(getSch.get(0),"price"));//挂号费
					Map<String, Object> cardTypeMap = DataBaseHelper.queryForMap("SELECT ticket_no FROM sch_appt WHERE code=?",responseVo.getOrderCode());
					responseVo.setNum(LbSelfUtil.getPropValueStr(cardTypeMap,"ticketNo"));//诊号
					responseVo.setPatientName(requ.getPatientName());//患者姓名
					responseVo.setDepartmentName(LbSelfUtil.getPropValueStr(getSch.get(0),"nameDept"));//科室名称
					responseVo.setDoctorName(LbSelfUtil.getPropValueStr(getSch.get(0),"nameEmp"));//医生名称
				}else{
					resXml=XmlUtil.beanToXml(LbSelfUtil.failed(responseVo, NowSjh.getDesc()), LbBmptResponse.class, false);
					   logger.info("2.4预约接口--便民：失败"+resXml);
					   return resXml;
				}
		   }else{
			   resXml=XmlUtil.beanToXml(LbSelfUtil.failed(responseVo, "未查询到可预约号源"), LbBmptResponse.class, false);
			   logger.info("2.4预约接口--便民：失败"+resXml);
			   return resXml;
		   }
		} catch (Exception e) {
			// TODO: handle exception
			 resXml=XmlUtil.beanToXml(LbSelfUtil.failed(responseVo, "未查询到可预约号源"), LbBmptResponse.class, false);
			 logger.info(e+"2.4预约接口--便民：异常"+resXml);
			 return resXml;
		}
		resXml=XmlUtil.beanToXml(LbSelfUtil.success(responseVo), responseVo.getClass(), false);
		logger.info("2.4预约接口--便民："+resXml);
		return resXml;
	}
	/**
	 *2.5取消接口--便民
	 * 灵璧便民平台
	 * @param param
	 * @return
	 */
	@Override
	public String cancelOrder(String param) {
		// TODO Auto-generated method stub
	    logger.info("2.5取消接口--便民："+param);
		LbBmptRequest requ = null;
		String resXml=null;
		LbBmptResponse responseVo = new LbBmptResponse();
		if(StringUtils.isNotBlank(param)){
			requ = (LbBmptRequest)XmlUtil.XmlToBean(param, LbBmptRequest.class);
			if(CommonUtils.isEmptyString(requ.getHospitalMark())){
				return XmlUtil.beanToXml(LbSelfUtil.common(Constant.RESFAIL, "未获取到医院标识"), LbSHResponseVo.class, false);
			}
			if(CommonUtils.isEmptyString(requ.getOrderCode())){
				return XmlUtil.beanToXml(LbSelfUtil.common(Constant.RESFAIL, "未获取到预约订单编号"), LbSHResponseVo.class, false);
			}
		}else{
			return XmlUtil.beanToXml(LbSelfUtil.common(Constant.RESFAIL, "未获取到信息"), LbSHResponseVo.class, false);
		}
		User user = LbSelfUtil.getDefaultUser(requ.getHospitalMark());
		UserContext.setUser(user);
		try {
			Map<String, Object> schApptMap = DataBaseHelper.queryForMap("SELECT * FROM sch_appt WHERE eu_status='0' and code=?",requ.getOrderCode());
	    	if(schApptMap!=null){
	    		if(lbPubForWsService.lbCancelOrder(schApptMap, user)>0){
	    			responseVo.setOrderCode(requ.getOrderCode());
	    		}else{
	    			 resXml=XmlUtil.beanToXml(LbSelfUtil.failed(responseVo, "操作失败，请稍后再次尝试"), LbBmptResponse.class, false);
	    			 logger.info("2.5取消接口--便民：取消失败"+resXml);
	    			 return resXml;
	    		}
	    	}else{
	    		 resXml=XmlUtil.beanToXml(LbSelfUtil.failed(responseVo, "未查询到取消预约信息"), LbBmptResponse.class, false);
				 logger.info("2.5取消接口--便民:查询失败"+resXml);
				 return resXml;
	    	}
		} catch (Exception e) {
			// TODO: handle exception
			 resXml=XmlUtil.beanToXml(LbSelfUtil.failed(responseVo, "未查询到可预约号源"), LbBmptResponse.class, false);
			 logger.info(e+"2.5取消接口--便民：异常"+resXml);
			 return resXml;
		}
		resXml=XmlUtil.beanToXml(LbSelfUtil.success(responseVo), responseVo.getClass(), false);
		logger.info("2.5取消接口--便民："+resXml);
		return resXml;
	}
	/**
	 *2.6查询预约记录接口--便民
	 * 灵璧便民平台
	 * @param param
	 * @return
	 */
	@Override
	public String queryOrdersStatus(String param) {
		// TODO Auto-generated method stub
		logger.info("2.6查询预约记录接口--便民："+param);
		  LbBmptRequest requ = null;
		   String resXml =null;
		  // 响应结果
		  LbBmptResponse responseVo = new LbBmptResponse();
		if (null != param && !("").equals(param)) {
			requ = (LbBmptRequest) XmlUtil.XmlToBean(param, LbBmptRequest.class);
		}else{
			return XmlUtil.beanToXml(LbSelfUtil.common(Constant.RESFAIL, "未获取到信息"), LbSHResponseVo.class, false);
		}
		try {
			List<LbBmptOrderInfo> orderInfoList = new ArrayList<>();
			Map<String, Object> map = LbSelfUtil.toMap(requ);
			List<Map<String, Object>> ApptList = schMapper.LbgetSchAppt(map);
			if(ApptList.size()>0){
				for(Map<String, Object> ApptMap:ApptList){
					LbBmptOrderInfo OrderInfo =new LbBmptOrderInfo();
					OrderInfo.setOrderCode(LbSelfUtil.getPropValueStr(ApptMap, "code"));//预约订单编号
					OrderInfo.setStartDate(LbSelfUtil.getPropValueStr(ApptMap, "beginTime"));//就诊开始时间
					OrderInfo.setEndDate(LbSelfUtil.getPropValueStr(ApptMap, "endTime"));//就诊结束时间
					//排班时段代码
					if(LbSelfUtil.getPropValueStr(ApptMap, "codeDateslot").equals(Constant.MORNING)){
						OrderInfo.setSessionCode("AM");//上午
					}else if(LbSelfUtil.getPropValueStr(ApptMap, "codeDateslot").equals(Constant.AFTERNOON)){
						OrderInfo.setSessionCode("PM");//下午
					}
					OrderInfo.setOrderDate(LbSelfUtil.getPropValueStr(ApptMap, "createTime"));//创建时间
					OrderInfo.setDepartmentCode(LbSelfUtil.getPropValueStr(ApptMap, "codeDept"));//科室编码
					OrderInfo.setDepartmentName(LbSelfUtil.getPropValueStr(ApptMap, "nameDept"));//科室名称
					OrderInfo.setDoctorCode(LbSelfUtil.getPropValueStr(ApptMap, "codeEmp"));//医生编码
					OrderInfo.setDoctorName(LbSelfUtil.getPropValueStr(ApptMap, "nameEmp"));//医生名称
					OrderInfo.setRegFee(LbSelfUtil.getPropValueStr(ApptMap, "price"));//总费用
					OrderInfo.setPatientName(LbSelfUtil.getPropValueStr(ApptMap, "namePi"));//患者姓名
					OrderInfo.setPhone(LbSelfUtil.getPropValueStr(ApptMap, "mobile"));//手机号
					OrderInfo.setOrderStatus(LbSelfUtil.getPropValueStr(ApptMap, "euStatus"));//订单状态
					orderInfoList.add(OrderInfo);
					OrderInfo = null;
				}
				responseVo.setOrderInfo(orderInfoList);
				responseVo.setCycleNum(orderInfoList.size());
			}else{
				resXml=XmlUtil.beanToXml(LbSelfUtil.common(Constant.RESFAIL, "未查询到有效数据"), LbSHResponseVo.class, false);
				logger.info("2.6查询预约记录接口--便民"+resXml);
				return resXml;
			}
		} catch (Exception e) {
			resXml=XmlUtil.beanToXml(LbSelfUtil.common(Constant.RESFAIL, "失败"), LbSHResponseVo.class, false);
			logger.info(e+"2.6查询预约记录接口--便民"+resXml);
			return resXml;
		}
		resXml=XmlUtil.beanToXml(LbSelfUtil.success(responseVo), responseVo.getClass(), false);
		logger.info("2.6查询预约记录接口--便民"+resXml);
		return resXml;
	}
	/**
	 *2.7停诊列表接口--便民
	 * 灵璧便民平台
	 * @param param
	 * @return
	 */
	@Override
	public String queryStopSchedules(String param) {
		// TODO Auto-generated method stub
		logger.info("2.7停诊列表接口--便民："+param);
		LbBmptRequest requ = null;
		String resXml =null;
		// 响应结果
		LbBmptResponse responseVo = new LbBmptResponse();
		
		if (StringUtils.isNotBlank(param)) {
			requ = (LbBmptRequest) XmlUtil.XmlToBean(param, LbBmptRequest.class);
		} else{
			resXml =XmlUtil.beanToXml(LbSelfUtil.failed(responseVo, "未获取到信息"), LbBmptResponse.class, false);
			logger.info("2.7停诊列表接口--便民："+resXml);
			return resXml;
		}
		try {
			Map<String, Object> paramMap = new HashMap<String, Object>();
			paramMap.put("StartDate", requ.getStartDate());
			paramMap.put("EndDate", requ.getEndDate());
			paramMap.put("codeDept", requ.getDepartmentCode());// 科室编码
			paramMap.put("doctCode", requ.getDoctorCode());// 医生编码
			if(StringUtils.isNotBlank(requ.getSessionCode())){
				if(requ.getSessionCode().equals("AM")){
					paramMap.put("nameDateslot","上午");
				}else if(requ.getSessionCode().equals("PM")){
					paramMap.put("nameDateslot","下午");
				}	
			}
			paramMap.put("total","0");
			paramMap.put("flagStop", "1");//停用标志
			// 科室医生号源查询
			List<LbBmptSchedule> Schedules = schPubForWsService.lbBmptgetQuerySch(paramMap);
			responseVo.setCycleNum(Schedules.size());
			responseVo.setSchedule(Schedules);
		} catch (Exception e) {
			resXml=XmlUtil.beanToXml(LbSelfUtil.common(Constant.RESFAIL, "失败"), LbSHResponseVo.class, false);
			logger.info(e+"2.7停诊列表接口--便民："+resXml);
			return resXml;
		}
		resXml=XmlUtil.beanToXml(LbSelfUtil.success(responseVo), responseVo.getClass(), false);
		logger.info("2.7停诊列表接口--便民:"+resXml);
		return resXml;
	}
	/**
	 *2.8查询医生列表--便民
	 * 灵璧便民平台
	 * @param param
	 * @return
	 */
	@Override
	public String queryDoctors(String param) {
		// TODO Auto-generated method stub
		logger.info("2.8查询医生列表--便民"+param);
		  LbBmptRequest requ = null;
		   String resXml =null;
		  // 响应结果
		  LbBmptResponse responseVo = new LbBmptResponse();
		if (StringUtils.isNotBlank(param)) {
			requ = (LbBmptRequest) XmlUtil.XmlToBean(param, LbBmptRequest.class);
		}
		try {
			Map<String, Object> map = LbSelfUtil.toMap(requ);
			List<Map<String, Object>> queryEmployee = bdPubForWsMapper.LbgetOuEmployee(map);
			if (queryEmployee.size()>0) {
                List<LbBmptDoctor> DoctorList = new ArrayList<>();
				for (Map<String, Object> EmployeeMap : queryEmployee) {
					LbBmptDoctor doctor = new LbBmptDoctor();
					doctor.setDoctorCode(LbSelfUtil.getPropValueStr(EmployeeMap, "codeEmp"));//医生编码
					doctor.setDoctorName(LbSelfUtil.getPropValueStr(EmployeeMap, "nameEmp"));//医生名称
					doctor.setDepartmentCode(LbSelfUtil.getPropValueStr(EmployeeMap, "codeDept"));//科室编码
					doctor.setDepartmentName(LbSelfUtil.getPropValueStr(EmployeeMap, "nameDept"));//科室名称
					//医生简介
					switch (LbSelfUtil.getPropValueStr(EmployeeMap, "euDrtype")) {
					case "0":
						doctor.setDoctorDesc("中西医师");
						break;
					case "1":
						doctor.setDoctorDesc("中医师");
						break;
					case "2":
						doctor.setDoctorDesc("西医师");
						break;
					case "3":
						doctor.setDoctorDesc("放射医师");
						break;
					default:
						break;
					}
					doctor.setDoctorTitle(LbSelfUtil.getPropValueStr(EmployeeMap, "name"));//医生职称
					doctor.setDoctorSpec(LbSelfUtil.getPropValueStr(EmployeeMap, "spec"));//医生擅长
					//doctor.setDoctorImg(LbSelfUtil.getPropValueStr(EmployeeMap, "photo"));//医生头像
					//医生性别
					if(LbSelfUtil.getPropValueStr(EmployeeMap, "dtSex").equals("02")){
						doctor.setDoctorGender("1");//男
					}else{
					  doctor.setDoctorGender("2");//女
					}
					DoctorList.add(doctor);
				}
				//Doctors.setDoctorList(DoctorList);
				//responseVo.setDoctors(Doctors);
				responseVo.setDoctor(DoctorList);
				responseVo.setCycleNum(DoctorList.size());
			}else{
				resXml=XmlUtil.beanToXml(LbSelfUtil.common(Constant.RESFAIL, "未查询到有效数据"), LbSHResponseVo.class, false);
				logger.info("2.8查询医生列表--便民"+resXml);
				return resXml;
			}
		} catch (Exception e) {
			resXml=XmlUtil.beanToXml(LbSelfUtil.common(Constant.RESFAIL, "失败"), LbSHResponseVo.class, false);
			logger.info(e+"2.8查询医生列表--便民"+resXml);
			return resXml;
		}
		resXml=XmlUtil.beanToXml(LbSelfUtil.success(responseVo), responseVo.getClass(), false);
		logger.info("2.8查询医生列表--便民"+resXml);
		return resXml;
	}

	/**
	 * 高值耗材收费项目同步到-灵璧单独版
	 * @param param
	 * @return
	 */
    @Override
    public String saveItemAndHpSetPrices(String param) {
    	logger.info("高值耗材数据同步{}",param);
		OtherRespJson respJson = new OtherRespJson();
		//响应数据
		String json =null;
		//判断数据是否为空
		if(StringUtils.isBlank(param)){
			respJson.setStatus(Constant.UNUSUAL);
			respJson.setDesc("未获取到相关数据");
			json = respJson.toString();
			return json;
		}
        BdItemAndHpSetPricesParam itemAndHpSetPrices = JsonUtil.readValue(param, BdItemAndHpSetPricesParam.class);
		BdItem item = itemAndHpSetPrices.getItem();
		if(CommonUtils.isEmptyString(item.getName())){
			respJson.setStatus(Constant.UNUSUAL);
			respJson.setDesc("未获取到相关名称信息");
			json = respJson.toString();
			return json;
		}
		if(StringUtils.isBlank(item.getCode())){
			respJson.setStatus(Constant.UNUSUAL);
			respJson.setDesc("未获取到收费项目编码信息");
			json = respJson.toString();
			return json;
		}
		if(CommonUtils.isEmptyString(item.getPkUnit())){
			respJson.setStatus(Constant.UNUSUAL);
			respJson.setDesc("未获取到单位信息");
			json = respJson.toString();
			return json;
		}else{
			String pkUnit=LbSelfUtil.getPropValueStr(DataBaseHelper.queryForMap("select pk_unit from BD_UNIT WHERE del_flag = '0' and name=? order by code",item.getPkUnit()), "pkUnit");
			if(StringUtils.isBlank(pkUnit)){
				respJson.setStatus(Constant.UNUSUAL);
				respJson.setDesc("未获取到有效单位信息，请维护");
				json = respJson.toString();
				return json;
			}else{
				item.setPkUnit(pkUnit);
			}
		}
		if(CommonUtils.isEmptyString(item.getPkItemcate())){
			respJson.setStatus(Constant.UNUSUAL);
			respJson.setDesc("未获取到费用分类信息");
			json = respJson.toString();
			return json;
		}else{
			String pkItemcate=LbSelfUtil.getPropValueStr(DataBaseHelper.queryForMap("select pk_itemcate from BD_ITEMCATE WHERE del_flag = '0' and code=? ",item.getPkItemcate()), "pkItemcate");
			if(StringUtils.isBlank(pkItemcate)){
				respJson.setStatus(Constant.UNUSUAL);
				respJson.setDesc("未获取到有效单位信息，请维护");
				json = respJson.toString();
				return json;
			}else{
				item.setPkItemcate(pkItemcate);
			}
		}
		if(CommonUtils.isEmptyString(item.getSpcode())){
			respJson.setStatus(Constant.UNUSUAL);
			respJson.setDesc("未获取到拼音码信息");
			json = respJson.toString();
			return json;
		}
		//判断费用
		if(item.getPrice()<=0){
			respJson.setStatus(Constant.UNUSUAL);
			respJson.setDesc("费用信息为0元");
			json = respJson.toString();
			return json;
		}

		//创造操作人
		User user = LbSelfUtil.getDefaultUser(item.getCreator());
		if(!BeanUtils.isNotNull(user)){
			respJson.setStatus(Constant.UNUSUAL);
			respJson.setDesc("未获取到相关操作人信息");
			json = respJson.toString();
			logger.info("高值耗材数据同步{}",json);
			return json;
		}
		UserContext.setUser(user);

        //根据名称查询是否已存在相同名称收费项目
		BdItem querItem = DataBaseHelper.queryForBean("select * from bd_item where code = ? ",BdItem.class,item.getCode());
		if(BeanUtils.isNotNull(querItem)){
			item.setPkItem(querItem.getPkItem());//收费项目主键
			itemAndHpSetPrices.setItem(item);
			itemAndHpSetPrices.setIsAll("1");
			//BdItemPrice,更新价格
			List<BdItemPrice> bdItemPriceList =DataBaseHelper.queryForList("select * from BD_ITEM_PRICE where pk_item = ? ",BdItemPrice.class,querItem.getPkItem());
			for(BdItemPrice bdItemPrice:bdItemPriceList){
				bdItemPrice.setPrice(item.getPrice());
				bdItemPrice.setFlagModify("1");
			}
			itemAndHpSetPrices.setItemPrices(bdItemPriceList);
		}else{
			item.setFlagPd("0");//物品标志
			item.setDelFlag("0");//删除标记
			item.setFlagSet("0");//组套标志
			item.setEuPricemode("0");//定价模式
			itemAndHpSetPrices.setItemAttr(bdPubForWsMapper.getBdItemAttrtemp(user.getPkOrg()));
			
			BdItemPrice bdItemPrice = new BdItemPrice();
			bdItemPrice.setPkOrg(user.getPkOrg());
			bdItemPrice.setEuPricetype("3");
			if("1".equals(item.getFlagActive())){
				bdItemPrice.setFlagStop("0");
			}else{
				bdItemPrice.setFlagStop("1");
			}
			bdItemPrice.setDelFlag("0");
			bdItemPrice.setDateBegin(new Date());
			bdItemPrice.setPrice(item.getPrice());
			bdItemPrice.setDateEnd(DateUtils.getTimeForOneYear(90));
			
			List<BdItemPrice> itemPrices = new ArrayList<>();
			itemPrices.add(bdItemPrice);
			
			itemAndHpSetPrices.setItemPrices(itemPrices);
			item.setCreator(user.getPkEmp());
		}

        //请求收费项目存储接口
        ResponseJson  NowBD =  apputil.execService("BD", "SrvService", "saveItemAndHpSetPrices",itemAndHpSetPrices,user);
		if(NowBD.getStatus()==Constant.SUC){
			//respJson.setData();
			//respJson.setTotal(PiHospList.size());
			respJson.setStatus(Constant.RESFAIL);
			respJson.setDesc("成功");
			json = respJson.toString();
			logger.info("高值耗材数据同步{}",json);
			return json;
		}else{
			respJson.setStatus(Constant.UNUSUAL);
			respJson.setDesc(NowBD.getDesc());
			json = respJson.toString();
			logger.info("高值耗材数据同步{}",json);
			return json;
		}
    }

	@Override
	public String getBills(String param) {
		LbzyRequestVo requ = null;
		LbzyResponseVo response = new LbzyResponseVo();
		if(StringUtils.isNotBlank(param)){
			requ = (LbzyRequestVo)XmlUtil.XmlToBean(param, LbzyRequestVo.class);
		}else{
            response.setResultCode("-1");
            response.setErrorMsg("未获取到相关数据");
			return XmlUtil.beanToXml(response, LbzyResponseVo.class, false);
		}
        List<LbzyResponItemVo> itemList = DataBaseHelper.queryForList("select * from view_reconciliation where to_char(date_pay,'yyyy-MM-dd')=? ",LbzyResponItemVo.class, requ.getBillDate());
        response.setItemVos(itemList);
        response.setResultCode("0");
        response.setBillDate(requ.getBillDate());
        response.setTransCode(requ.getTransCode());
		return XmlUtil.beanToXml(response, LbzyResponseVo.class, false);
	}

	@Override
	public String nhisLbzySelfService(String param) {
		logger.info(param);
		String resXml = null;
		LbzyRequestVo requ = null;
		//判断入参
		if(StringUtils.isNotBlank(param)){
			//xml-->对象
			requ = (LbzyRequestVo)XmlUtil.XmlToBean(param, LbzyRequestVo.class);
			//自主设备编码判断
			if(StringUtils.isNotBlank(requ.getUserID())){
				//查询自助机设备编码相关信息
				User user = LbSelfUtil.getDefaultUser(requ.getUserID());
				if(BeanUtils.isNotNull(user)){
					UserContext.setUser(user);
					if(StringUtils.isNotBlank(requ.getTransCode())){
						resXml = nhisLbzySelfComm(requ);
					}else{
						resXml = LbSelfUtil.selfLbzyError("未获取到相关业务编码");
					}
				}else{
					resXml = LbSelfUtil.selfLbzyError("自主设备编码未注册");
				}
			}else{
				resXml = LbSelfUtil.selfLbzyError("未获取到自助机设备编码");
			}
		}else{
			resXml = LbSelfUtil.selfLbzyError("未获取到相关数据");
		}
		logger.info(resXml);
		return resXml;
	}

	@Override
	public byte[] queryDoctorsImage(String param) {
		logger.info("查询医生信息接口{}",param);
		LbSHRequestVo req = (LbSHRequestVo) XmlUtil.XmlToBean(param,LbSHRequestVo.class);
		//LbQueDocInfoReqVo req = (LbQueDocInfoReqVo) XmlUtil.XmlToBean(param,LbQueDocInfoReqVo.class);
		Map<String, Object> paramMap = LbSelfUtil.toMap(req);
		String resXml=null;
		// 响应结果3
		LbSHResponseVo responseVo = new LbSHResponseVo();
		try {
			List<Map<String, Object>> queryEmployee = bdPubForWsMapper
					.queryEmployee(paramMap);
			if (queryEmployee != null) {
				List<LbQuedocinfoResTemVo> itemlist = new ArrayList<>();
				for (Map<String, Object> map : queryEmployee) {
					LbQuedocinfoResTemVo lbRestemVo = ApplicationUtils.mapToBean(map,LbQuedocinfoResTemVo.class);
					BLOB blob = (BLOB) MapUtils.getObject(map,"photo");

					return blob.getBytes(1, (int) blob.length());
				}

			}
		} catch (Exception e) {
			return new byte[1];
		}

		return new byte[1];
	}

	/**lbzy-自助机汇总接口**/
	public String nhisLbzySelfComm(LbzyRequestVo requ){
		switch (requ.getTransCode()){
			case "1001"://服务器时间获取
				return queryLbzySelfTime();
			case "1002"://获取患者档案信息
				return queryLbzyCardPiInfo(requ);
			case "1003"://建立档案信息
				return lbzyCreatPi(requ);
			case "1006"://实名认证/更新患者信息
				return lbzyCreatPi(requ);
			case "1007"://查询就诊卡信息
				return querLbzyCardInfo(requ);
			case "3001"://查询挂号类别
				return querLbzySrvInfo();
			case "3002"://查询挂号类别
				return querLbzySchDeptInfo(requ);
			case "3003"://查询医生列表
				return querLbzySchInfo(requ);
			case "3004"://挂号
				return saveLbzyRegister(requ);
			case "3006"://查询预约时间段列表
				return querLbzySchAppSec(requ);
			case "3007"://查询预约号记录
				return querLbzyAppRecordList(requ);
			case "3008"://预约登记
				return saveLbzyAppRegistration(requ);
			case "3009"://预约挂号取号
				return saveLbzyAppGetMun(requ);
			case "3013"://查询挂号时段
				return querLbzySchSec(requ);
			case "3014"://挂号锁号
				return upLbzyLockReg(requ);
			case "3015"://取消锁号
				return upLbzyUnLockReg(requ);
			case "4001"://查询就诊信息列表
				return querLbzyClinicRecordList(requ);
			case "4003"://查询待缴费
				return querLbzyUnpaidRecordList(requ);
			case "4005"://门诊支付
				return upLbzyPayment(requ);
			case "5001"://患者在院信息查询
				return querLbzyInpatientIpInfo(requ);
			case "5002"://患者在院信息查询
				return saveLbzyRechargeInpatientDeposit(requ);
			default:
				return "业务编码无效";
		}
	}

	/**lbzy-获取服务器时间**/
	public String queryLbzySelfTime(){
		LbzyResponseVo response = new LbzyResponseVo();
		response.setResultCode(Constant.RESFAIL);
		response.sethISDateTime(DateUtils.getDateTime());
		return XmlUtil.beanToXml(response, LbzyResponseVo.class, false);
	}

	/**lbzy-获取患者档案信息**/
	public String queryLbzyCardPiInfo(LbzyRequestVo requ){
		String resXml = null;
		if(StringUtils.isNotBlank(requ.getCardTypeID()) && StringUtils.isNotBlank(requ.getCardNO())){
			Map<String, Object> paramMap = new HashMap<>();
			switch (requ.getCardTypeID()){
				case "1"://诊疗卡
					paramMap.put("dtCardType","01");
					paramMap.put("dtCardNo",requ.getCardNO());
					break;
				case "2"://身份证
					paramMap.put("idno",requ.getCardNO());
					break;
				case "3"://社保卡
					paramMap.put("insurNo",requ.getCardNO());
					break;
				case "5"://病人ID
					paramMap.put("pkPi",requ.getCardNO());
					break;
				case "6"://居民健康卡
					paramMap.put("hicNo",requ.getCardNO());
					break;
				default:
					return LbSelfUtil.selfLbzyError("卡类型数据无效");
			}
			try {
				List<Map<String, Object>> queryPiMaster = pvPubForWsMapper.queryPiMaster(paramMap);
				if(queryPiMaster.size() == 1){
					LbzyResponseVo response = new LbzyResponseVo();
					response.setResultCode(Constant.RESFAIL);
					response.setPatientID(MapUtils.getString(queryPiMaster.get(0),"patientid"));//pkpi
					response.setPatientName(MapUtils.getString(queryPiMaster.get(0),"name"));//患者姓名
					String sex = MapUtils.getString(queryPiMaster.get(0),"sex");
					if("02".equals(sex)){//患者性别
						response.setPatientSexID("1");
					}else if("03".equals(sex)){
						response.setPatientSexID("2");
					}else{
						response.setPatientSexID("9");
					}
					response.setiDCardNO(MapUtils.getString(queryPiMaster.get(0),"idCardNo"));//身份证号
					response.setBirthday(DateUtils.formatDate(DateUtils.parseDate(MapUtils.getString(queryPiMaster.get(0),"birthday")),new Object[]{}));//出生日期
					response.setPatientAge(DateUtils.getAgeByBirthday(DateUtils.strToDate(queryPiMaster.get(0).get("birthday").toString()),new Date()));//患者年龄
					if(!StringUtils.isNotBlank(MapUtils.getString(queryPiMaster.get(0),"dtcardNo",""))){
						response.setCardStatus("0");//卡状态
					}else{
						response.setCardStatus("3");//卡状态
					}
					response.setMobile(MapUtils.getString(queryPiMaster.get(0),"phoneNo"));//手机号码
					response.setCardNO(MapUtils.getString(queryPiMaster.get(0),"dtcardNo",""));////诊疗卡号
					resXml = XmlUtil.beanToXml(response, LbzyResponseVo.class, false);
				}else{
					resXml = LbSelfUtil.selfLbzyError("未查询到相关患者信息或患者信息有多个");
				}
			} catch (Exception e) {
				resXml = LbSelfUtil.selfLbzyError(e.getMessage());
			}
		}else{
			resXml = LbSelfUtil.selfLbzyError("未获取到卡类型数据");
		}
		return resXml;
	}

	/**lbzy-建立档案信息**/
	public String lbzyCreatPi(LbzyRequestVo requ){
        String resXml = null;
        try {
        	PiMaster pi = new PiMaster();
			if(StringUtils.isNotBlank(requ.getCardTypeID()) && StringUtils.isNotBlank(requ.getCardNO())){
				Map<String, Object> paramMap = new HashMap<>();
				switch (requ.getCardTypeID()){
					case "1"://诊疗卡
						return LbSelfUtil.selfLbzyError("暂不是支持使用就诊卡建档");
					case "2"://身份证
						paramMap.put("idno",requ.getCardNO());
						break;
					case "3"://社保卡
						paramMap.put("hicNo",requ.getCardNO());
						break;
					case "5"://病人ID
						paramMap.put("pkPi",requ.getCardNO());
						break;
					case "6"://居民健康卡
						paramMap.put("hicNo",requ.getCardNO());
						break;
					default:
						return LbSelfUtil.selfLbzyError("卡类型数据无效");
				}
				if(StringUtils.isNotBlank(requ.getiDCardNO())) {
					paramMap.put("idno",requ.getiDCardNO());
				}
				List<PiMaster> piList = pvPubForWsMapper.queryLbzyPiMaster(paramMap);
				if(piList.size()>0){
					pi = piList.get(0);
				}
				
				switch (requ.getCardTypeID()){
				case "2"://身份证
					pi.setDtIdtype("01");
					pi.setIdNo(requ.getCardNO());
					break;
				case "3"://社保卡
					pi.setInsurNo(requ.getCardNO());
					break;
				case "6"://居民健康卡
					pi.setHicNo(requ.getCardNO());
					break;
				default:
					break;
			    }
			}else{
				return LbSelfUtil.selfLbzyError("未获取到卡相关数据");
			}
			if(StringUtils.isNotBlank(requ.getPatientName())){//患者姓名
				pi.setNamePi(requ.getPatientName());
			}else{
				return LbSelfUtil.selfLbzyError("未获取到姓名相关数据");
			}
			if(StringUtils.isNotBlank(requ.getPatientSexID())){//患者性别
				if("1".equals(requ.getPatientSexID())){
					pi.setDtSex("02");
				}else if("2".equals(requ.getPatientSexID())){
					pi.setDtSex("03");
				}
			}
			if(StringUtils.isNotBlank(requ.getBirthday())){// 出生日期
				pi.setBirthDate(DateUtils.parseDate(requ.getBirthday(),"yyyy-MM-dd"));
			}
			if(StringUtils.isNotBlank(requ.getiDCardNO())) {
				pi.setDtIdtype("01");
				pi.setIdNo(requ.getiDCardNO());
			}
			if(StringUtils.isNotBlank(requ.getMobile())){//手机号
				pi.setMobile(requ.getMobile());
			}
			if(StringUtils.isNotBlank(requ.getAddress())){//地址
				pi.setAddrCur(requ.getAddress());
			}
            //患者信息校验修改保存
            pi = lbPubForWsService.lbSavePiMaster(pi,UserContext.getUser());
            LbzyResponseVo response = new LbzyResponseVo();
            response.setPatientID(pi.getPkPi());
            response.setResultCode(Constant.RESFAIL);
			resXml = XmlUtil.beanToXml(response, LbzyResponseVo.class, false);
        } catch (Exception e) {
            resXml = LbSelfUtil.selfLbzyError(e.getMessage());
        }
        return resXml;
	}

	/**lbzy-查询就诊卡信息**/
	public String querLbzyCardInfo(LbzyRequestVo requ){
		String resXml = null;
		if(StringUtils.isNotBlank(requ.getiDCardNO())){//身份证号
			LbzyResponseVo response = new LbzyResponseVo();
			List<LbzyResponItemVo> ponList = new ArrayList<>();
			String sql = "select ca.card_no,ca.eu_status from pi_card ca  INNER JOIN pi_master pi on pi.pk_pi=ca.pk_pi where pi.id_no =? ";
			List<Map<String,Object>> listMap = DataBaseHelper.queryForList(sql, new Object[]{requ.getiDCardNO()});
            if(listMap.size()>0){
            	for(Map<String,Object> map:listMap){
					LbzyResponItemVo pon = new LbzyResponItemVo();
					pon.setCardNO(MapUtils.getString(map,"cardNo"));
					if("0".equals(MapUtils.getString(map,"euStatus"))){
						pon.setCardStatus(MapUtils.getString(map,"euStatus"));
					}else if("1".equals(MapUtils.getString(map,"euStatus"))){
						pon.setCardStatus(MapUtils.getString(map,"euStatus"));
					}else if("9".equals(MapUtils.getString(map,"euStatus"))){
						pon.setCardStatus("2");
					}
					ponList.add(pon);
				}
			}else{
				LbzyResponItemVo pon = new LbzyResponItemVo();
				pon.setCardStatus("3");
				ponList.add(pon);
			}
			response.setItemVos(ponList);
			response.setResultCode(Constant.RESFAIL);
			resXml = XmlUtil.beanToXml(response, LbzyResponseVo.class, false);
		}else{
			LbSelfUtil.selfLbzyError("未获取到相关卡数据");
		}
		return resXml;
	}

	/**lbzy-查询排班服务分类**/
	public String querLbzySrvInfo(){
		String resXml = null;
		List<Map<String, Object>> srvList = schMapper.getLbzySchSrv();
		if(srvList.size()>0){
			LbzyResponseVo response = new LbzyResponseVo();
			List<LbzyResponItemVo> ponList = new ArrayList<>();
			for(Map<String, Object> srvMap:srvList){
				LbzyResponItemVo pon = new LbzyResponItemVo();
				pon.setTypeID(MapUtils.getString(srvMap,"code"));//类别代码
				pon.setTypeName(MapUtils.getString(srvMap,"name"));//类别名称
				pon.setCost(MapUtils.getString(srvMap,"price"));//金额
				pon.setReglevlValid("1");//有效性1表示有效 0表示无效
				ponList.add(pon);
			}
			response.setItemVos(ponList);
			response.setResultCode(Constant.RESFAIL);
			resXml = XmlUtil.beanToXml(response, LbzyResponseVo.class, false);
		}else{
			resXml = LbSelfUtil.selfLbzyError("未查询到相关数据");
		}
		return resXml;
	}

	/**lbzy-查询排班科室**/
	public String querLbzySchDeptInfo(LbzyRequestVo requ){
		String resXml = null;
		DateTime dt = DateTime.now();
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("pkOrg", UserContext.getUser().getPkOrg());
        //根据日期判断查询预约排班信息，正常排班信息
		//判断获取时间是否大于当前时间
		if(BeanUtils.isNotNull(requ.getDate()) && requ.getDate().compareTo(dt.toString("YYYY-MM-dd"))>0){
			paramMap.put("nowDate", requ.getDate());
			paramMap.put("total","0");
		}else{
			paramMap.put("nowDate", dt.toString("YYYY-MM-dd"));// 当天的日期
		}
		paramMap.put("srvCode",requ.getRegTypeID());
		paramMap.put("flagStop", "0");//停用标志
		/** 1.获取当天目前可用排班 */
		List<Map<String, Object>> todaylist = schMapper.LbTodaySchInfosByDate(paramMap);
		if(todaylist.size()>0){
			LbzyResponseVo response = new LbzyResponseVo();
			List<LbzyResponItemVo> ponList = new ArrayList<>();
			for (Map<String, Object> today:todaylist) {
				boolean ifset = true;
				if(todaylist.size()>0){
					for(LbzyResponItemVo tofmsp: ponList){
						if(tofmsp.getDepartmentID().equals(LbSelfUtil.getPropValueStr(today,"codeDept"))){
							ifset = false;
						}
					}
				}
				if(ifset){
					LbzyResponItemVo pon = new LbzyResponItemVo();
					pon.setDepartmentID(MapUtils.getString(today,"codeDept"));//科室编码
					pon.setDepartmentName(MapUtils.getString(today,"nameDept"));//科室名称
					pon.setCost(MapUtils.getString(today,"price"));//金额
					pon.setIsEndPoint("1");//是否是终结点	1是，0不是（变更1）
					pon.setIsChooseDoctor("1");//是否有医生	1是，0不是（变更1）
					ponList.add(pon);//去重复科室信息
				}
			}
			response.setItemVos(ponList);
			response.setResultCode(Constant.RESFAIL);
			resXml = XmlUtil.beanToXml(response, LbzyResponseVo.class, false);
		}else{
			resXml = LbSelfUtil.selfLbzyError("未查询到相关数据");
		}
		return resXml;
	}

	/**lbzy-查询医生列表**/
	public String querLbzySchInfo(LbzyRequestVo requ){
		String resXml = null;
		DateTime dt = DateTime.now();
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("pkOrg", UserContext.getUser().getPkOrg());
		//根据日期判断查询预约排班信息，正常排班信息
		//判断获取时间是否大于当前时间
		if(BeanUtils.isNotNull(requ.getDate()) && requ.getDate().compareTo(dt.toString("YYYY-MM-dd"))>0){
			paramMap.put("nowDate", requ.getDate());
			paramMap.put("total","0");
		}else{
			paramMap.put("nowDate", dt.toString("YYYY-MM-dd"));// 当天的日期
		}
		paramMap.put("srvCode",requ.getRegTypeID());//排班服务类型
		paramMap.put("codeDept", requ.getDepartmentID());// 科室编码
		//paramMap.put("doctCode", requ.getDoctCode());// 医生编码
		//科室编码
		paramMap.put("flagStop", "0");//停用标志
		/** 1.获取当天目前可用排班 */
		List<Map<String, Object>> todaylist = schMapper.LbTodaySchInfosByDate(paramMap);
		if(todaylist.size()>0){
			LbzyResponseVo response = new LbzyResponseVo();
			List<LbzyResponItemVo> ponList = new ArrayList<>();
			for (Map<String, Object> today:todaylist) {
				LbzyResponItemVo pon = new LbzyResponItemVo();
                pon.setScheduleCode(MapUtils.getString(today,"pkSch"));//排班主键
				pon.setDoctorID(MapUtils.getString(today,"codeEmp"));//医生编码
				pon.setDoctorName(MapUtils.getString(today,"nameEmp"));//医生姓名
				pon.setFeeType(MapUtils.getString(today,"name"));//收费类别
				pon.setScheduleTime(MapUtils.getString(today,"nameDateslot")+"("+MapUtils.getString(today,"timeBegin")+"-"+MapUtils.getString(today,"timeEnd")+")");//出诊安排
				pon.setRegCount(MapUtils.getString(today,"cntTotal"));//挂号数
				int cntTotal = Integer.valueOf(MapUtils.getString(today,"cntTotal"));
				int cntUsed = Integer.valueOf(MapUtils.getString(today,"cntUsed"));
				pon.setLimitCount(String.valueOf(cntTotal-cntUsed));//挂号剩余数
				//pon.setWaitCount;//等待数
				pon.setDoctorLevel(MapUtils.getString(today,"typename"));//医生职务
				pon.setTotalCost(MapUtils.getString(today,"price"));//总价
				ponList.add(pon);
			}
			response.setItemVos(ponList);
			response.setResultCode(Constant.RESFAIL);
			resXml = XmlUtil.beanToXml(response, LbzyResponseVo.class, false);
		}else{
			resXml = LbSelfUtil.selfLbzyError("未查询到相关数据");
		}
		return resXml;
	}

	/**lbzy-挂号**/
	public String saveLbzyRegister(LbzyRequestVo requ){
		String resXml = null;
		if(StringUtils.isBlank(requ.getScheduleCode())){//号源id
			return LbSelfUtil.selfLbzyError("未获取到相关号源数据");
		}
		if(requ.getItemVos().size()>0 && StringUtils.isBlank(requ.getItemVos().get(0).getPayTypeID())){//支付方式
			return LbSelfUtil.selfLbzyError("未获取到支付方式相关数据");
		}
		if(requ.getItemVos().size()>0 && StringUtils.isBlank(requ.getItemVos().get(0).getAmount())){//支付金额
			return LbSelfUtil.selfLbzyError("未获取到支付金额相关数据");
		}
		LbzyRequItemVo requItemVo = requ.getItemVos().get(0);
		String recip[] = requItemVo.getpOSTransNO().split("|");
		if(recip.length<2) {
			return LbSelfUtil.selfLbzyError("支付数据不缺失！！!");
		}
		if(StringUtils.isBlank(requ.getPatientID())){//患者id
			return LbSelfUtil.selfLbzyError("未获取到患者ID相关数据");
		}
		try{
			LbPiMasterRegVo regvo = DataBaseHelper.queryForBean("SELECT pi.*,hp.pk_hp,hp.pk_insurance FROM pi_master pi LEFT JOIN PI_INSURANCE hp on hp.pk_pi = pi.pk_pi and hp.del_flag = '0' WHERE pi.del_flag = '0' and pi.pk_pi = ?", LbPiMasterRegVo.class, new Object[]{requ.getPatientID()});
			//查询自费医保计划主键
			Map<String, Object> bdhpMap = DataBaseHelper.queryForMap("SELECT pk_hp FROM bd_hp WHERE eu_hptype=? and del_flag='0' ", "0");
			//2020-7-17 lb门诊挂号默认医保类型全自费
			regvo.setPkHp(LbSelfUtil.getPropValueStr(bdhpMap,"pkHp"));

			Map<String, Object> paramMap = new HashMap<String, Object>();
			paramMap.put("pkSchticket", requ.getScheduleCode());//获取号源id
			//根据号源主键查新排班信息
			List<Map<String, Object>> schPlanlist = schMapper.LbgetSchPlanInfo(paramMap);
			String codeDept = null;//挂号科室
			if(schPlanlist.size()<=0 || schPlanlist.get(0)==null){
				return LbSelfUtil.selfLbzyError("号源信息有误，请重新获取");
			}else{
				Map<String, Object> schPlanMap = schPlanlist.get(0);
				codeDept = MapUtils.getString(schPlanMap,"codeDept");

				//儿科年龄上限
				Map<String, Object> cardTypeMap = DataBaseHelper.queryForMap("select val from bd_sysparam where del_flag = '0' and  code='PV0019' and pk_org=?",UserContext.getUser().getPkOrg());
				//儿科科室
				Map<String, Object> codeTypeMap = DataBaseHelper.queryForMap("select val from bd_sysparam where del_flag = '0' and  code='CN0001' and pk_org=?",UserContext.getUser().getPkOrg());
				if(null != codeTypeMap){
					String card[] = codeTypeMap.get("val").toString().split(",");
					for (int i = 0; i < card.length; i++) {
						if((card[i]).equals(codeDept)){
							int age = DateUtils.getAge(regvo.getBirthDate());
							int Maximum = Integer.valueOf(cardTypeMap.get("val").toString());
							if(age-Maximum>0){
								return LbSelfUtil.selfLbzyError("所选挂号科室年龄最大:"+Maximum+"岁，患者实际："+age+"岁");
							}
						}

					}
				}
				//判断是否允许挂号
				String date = regvo.getDateAppt()!=null?DateUtils.formatDate(regvo.getDateAppt(), "yyyyMMdd"): (regvo.getDateReg()!=null?DateUtils.formatDate(regvo.getDateReg(), "yyyyMMdd"):null);
				if(StringUtils.isNotBlank(date)) {
					if(DataBaseHelper.queryForScalar("select count(*) from PV_ENCOUNTER t WHERE t.PK_PI=? AND to_char(t.DATE_BEGIN,'yyyyMMdd') =? AND t.PK_DEPT=? AND t.EU_PVTYPE in('1','2') AND t.EU_STATUS in('0','1')",
							Integer.class, new Object[]{regvo.getPkPi(),date,regvo.getPkDept()}) >0){
						return LbSelfUtil.selfLbzyError("该患者已经存在当前日期和科室的挂号记录！");
					}
				}
				
				//组装数据
				boolean Iffeel = false;
				regvo.setDateReg(new Date());//挂号日期--排班日期
				regvo.setPkDept(MapUtils.getString(schPlanMap,"pkDept"));//挂号科室
				if(LbSelfUtil.getPropValueStr(schPlanMap,"euSrvtype").equals("9")){
					regvo.setEuPvtype("2");//就诊类型
				}
				if("7".equals(requItemVo.getPayTypeID())){
					regvo.setDtPaymode("7");//支付方式7：微信，8：支付宝
				}else if("6".equals(requItemVo.getPayTypeID())){
					regvo.setDtPaymode("8");//支付方式7：微信，8：支付宝
				}
				regvo.setPkSchres(LbSelfUtil.getPropValueStr(schPlanMap,"pkSchres"));
				regvo.setPkDateslot(LbSelfUtil.getPropValueStr(schPlanMap,"pkDateslot"));//日期分组
				regvo.setTicketNo(LbSelfUtil.getPropValueStr(schPlanMap,"ticketno"));//预约票号
				regvo.setPkSch(LbSelfUtil.getPropValueStr(schPlanMap,"pkSch"));//排班主键
				regvo.setPkSchsrv(LbSelfUtil.getPropValueStr(schPlanMap,"pkSchsrv"));//排班服务主键
                //组装收费信息
				BigDecimal amt=BigDecimal.ZERO;
				if(!StringUtils.isBlank(requItemVo.getAmount()) && !("0.0").equals(requItemVo.getAmount()) && !("0").equals(requItemVo.getAmount())&& !("0.00").equals(requItemVo.getAmount())){
					BlDeposit dep =new BlDeposit();
					Double pric = Double.parseDouble(requItemVo.getAmount());
					amt=BigDecimal.valueOf(pric);

					//账户支付可用余额校验
					if(("6").equals(requItemVo.getPayTypeID()) || ("7").equals(requItemVo.getPayTypeID())){
						dep.setAmount(amt);
						dep.setDtPaymode(requItemVo.getPayTypeID());//支付方式
						List<BlDeposit> depList =new ArrayList<>();
						depList.add(dep);
						regvo.setDepositList(depList);
						Iffeel = true;
						//门诊诊察费明细
						List<ItemPriceVo>  itemList = bdPubForWsMapper.LbgetBdItemInfo(LbSelfUtil.getPropValueStr(schPlanlist.get(0),"pkSchsrv"));
						regvo.setItemList(itemList);
					}else{
						return LbSelfUtil.selfLbzyError("暂不支持微信、支付宝以外的支付方式");
					}
				}

				//保存挂号信息（含保存患者信息）
				regvo =lbPubForWsService.savePvRegInfo(Iffeel, regvo, UserContext.getUser());
				if(Iffeel){
					if(("7").equals(requItemVo.getPayTypeID()) || ("8").equals(requItemVo.getPayTypeID())){
						//支付信息写入外部支付接口记录表bl_ext_pay
						BlDeposit vo =regvo.getDepositList().get(0);
						BlExtPay extPay = new BlExtPay();
						extPay.setPkExtpay(NHISUUID.getKeyId());
						extPay.setPkOrg(UserContext.getUser().getPkOrg());
						extPay.setAmount(BigDecimal.valueOf(Double.valueOf(requItemVo.getAmount())));
						if("7".equals(requItemVo.getPayTypeID())){
							extPay.setEuPaytype("7");//支付方式7：微信，8：支付宝
						}else if("6".equals(requItemVo.getPayTypeID())){
							extPay.setEuPaytype("8");//支付方式7：微信，8：支付宝
						}
						extPay.setFlagPay("1");
						extPay.setSerialNo(recip[1]);//订单号
						extPay.setTradeNo(recip[2]);//交易流水号
						extPay.setPkPi(regvo.getPkPi());
						extPay.setPkPv(regvo.getPkPv());
						extPay.setDateRefund(null);
						extPay.setEuBill("0");
						extPay.setDateBill(null);
						extPay.setDescPay("自助机金额缴纳"+requItemVo.getAmount());
						extPay.setDatePay(new Date());
						extPay.setPkDepo(vo.getPkDepo());//BL_DEPOSIT主键
						extPay.setCreator(UserContext.getUser().getPkEmp());
						extPay.setCreateTime(new Date());
						extPay.setDelFlag("0");
						extPay.setTs(new Date());
						extPay.setModifier(regvo.getPkEmp());
						blPubForWsService.inserBlExtPay(extPay);
					}
				}

				LbzyResponseVo response = new LbzyResponseVo();
				response.setTranSerialNO(regvo.getCodePv());//交易流水号
				response.setRegFlow(regvo.getCodePv());//挂号单据号
				response.setDoctorID(MapUtils.getString(schPlanMap,"codeEmp"));//医生编码
				//response.setSeeTime();//就诊时间
				response.setSeeNO(MapUtils.getString(schPlanMap,"ticketno"));//就诊序号
				response.setRegType(MapUtils.getString(schPlanMap,"stvName"));//挂号种类
				response.setPatientName(regvo.getNamePi());//患者姓名
				response.setPatientID(regvo.getPkPi());//患者ID
				response.setDepartmentName(MapUtils.getString(schPlanMap,"name"));//科室名称就诊科室
				response.setDepartmentAddress(MapUtils.getString(schPlanMap,"namePlace"));//科室位置就诊位置
				response.setDoctorName(MapUtils.getString(schPlanMap,"nameEmp"));//医生姓名
				response.setResultCode(Constant.RESFAIL);
				resXml = XmlUtil.beanToXml(response, LbzyResponseVo.class, false);
			}
		} catch (Exception e) {
			resXml = LbSelfUtil.selfLbzyError("挂号失败信息："+e.getMessage());
		}
		return resXml;
	}

	/**lbzy-查询预约时间段列表**/
	public String querLbzySchAppSec(LbzyRequestVo requ){
		String resXml = null;
		if(StringUtils.isNotBlank(requ.getScheduleCode())){//排班主键pkSch
			Map<String, Object> paramMap = new HashMap<String, Object>();
			paramMap.put("pkSch", requ.getScheduleCode());
			List<Map<String, Object>> schpubList = schMapper.LbTodaySchInfosByDate(paramMap);
			if(schpubList.size()>0) {
				List<Map<String, Object>> schSecList = schMapper.querySchAppSec(schpubList.get(0));
				if(schSecList.size()>0) {
					LbzyResponseVo response = new LbzyResponseVo();
					response.setResultCode(Constant.RESFAIL);
					List<LbzyResponItemVo> ponList = new ArrayList<>();
					for(Map<String, Object> schSecMap:schSecList){
						if(0<Integer.valueOf(MapUtils.getString(schSecMap,"cntAppt"))){
							LbzyResponItemVo pon = new LbzyResponItemVo();
							pon.setCode(MapUtils.getString(schSecMap,"pkDateslotsec"));//班次代码
							pon.setName(MapUtils.getString(schpubList.get(0),"nameDateslot"));//班次名称
							pon.setTimeRange(MapUtils.getString(schSecMap,"timeBegin").substring(0, 5)+"~"+MapUtils.getString(schSecMap,"timeEnd").substring(0, 5));//时间段
							pon.setRemainNum(MapUtils.getString(schSecMap,"cntAppt"));//该时段可预约数量
							//pon.setBeginTime(MapUtils.getString(schSecMap,"timeBegin").substring(0, 5));//开始时间
							//pon.setEndTime(MapUtils.getString(schSecMap,"timeEnd").substring(0, 5));//结束时间
							ponList.add(pon);
						}
					}
					resXml = XmlUtil.beanToXml(response, LbzyResponseVo.class, false);
				}else {
					resXml = LbSelfUtil.selfLbzyError("未查询到排班相关时段信息");
				}
			}else {
				resXml = LbSelfUtil.selfLbzyError("未查询到排班相关数据");
			}
		}else {
			resXml = LbSelfUtil.selfLbzyError("未获取到排班主键数据");
		}
		return resXml;
	}
	
	/**lbzy-查询预约号记录**/
	public String querLbzyAppRecordList(LbzyRequestVo requ){
		String resXml = null;
		if(StringUtils.isBlank(requ.getPatientID())){//患者id
			return LbSelfUtil.selfLbzyError("未获取到患者ID相关数据");
		}
		DateTime dt = DateTime.now();
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("patientId", requ.getPatientID());
		//判断是否传递时间，默认当前日期
		if(BeanUtils.isNotNull(requ.getStartDate()) && BeanUtils.isNotNull(requ.getEndDate())){
			paramMap.put("nowDate", requ.getStartDate());
			paramMap.put("endDate", requ.getEndDate());
		}else{
			paramMap.put("nowDate", dt.toString("YYYY-MM-dd"));// 当天的日期
		}
		List<LBQueSchVo> queSchList = new ArrayList<>();
		try {
			LbzyResponseVo response = new LbzyResponseVo();
			response.setResultCode(Constant.RESFAIL);
			List<LbzyResponItemVo> ponList = new ArrayList<>();
			List<Map<String, Object>> schpubList = schMapper.QueryRegisteredRecords(paramMap);
			for (Map<String, Object> schpubMap:schpubList){
				LbzyResponItemVo pon = new LbzyResponItemVo();
				pon.setDepartmentName(MapUtils.getString(schpubMap,"nameDept"));//科室名称
				//pon.setScheduleID();//排班号
				pon.setBookDate(MapUtils.getString(schpubMap,"dateWork"));//预约日期
				pon.setNoon(MapUtils.getString(schpubMap,"nameDateslot"));//上下午
				pon.setDoctorName(MapUtils.getString(schpubMap,"nameEmp"));//医生姓名
				pon.setReglevelName(MapUtils.getString(schpubMap,"name"));//挂号级别
				pon.setRegCost(MapUtils.getString(schpubMap,"price"));//挂号费
				//pon.setDiagCost();//检查费
				//pon.setOtherCost();//其他费用
				pon.setClinicTime(MapUtils.getString(schpubMap,"timeBegin").substring(0, 5)+"-"+MapUtils.getString(schpubMap,"timeEnd").substring(0, 5));//预约的时间段
				pon.setRegFlow(MapUtils.getString(schpubMap,"ticketNo"));//流水号	取号、取消时用
				//pon.setMedType();//预约挂号取号用
				//pon.setDiagCode();//预约挂号取号用
				//pon.setDiagName();//预约挂号取号用

				ponList.add(pon);
			}
			response.setItemVos(ponList);
			resXml = XmlUtil.beanToXml(response, LbzyResponseVo.class, false);
		} catch (Exception e) {
			resXml = LbSelfUtil.selfLbzyError(e.getMessage());
		}
		return resXml;
	}

	/**lbzy-预约挂号**/
	public String saveLbzyAppRegister(LbzyRequestVo requ){
		String resXml = null;
		if(StringUtils.isBlank(requ.getScheduleCode())){//号源id
			return LbSelfUtil.selfLbzyError("未获取到相关号源数据");
		}
		if(requ.getItemVos().size()>0 && StringUtils.isBlank(requ.getItemVos().get(0).getPayTypeID())){//支付方式
			return LbSelfUtil.selfLbzyError("未获取到支付方式相关数据");
		}
		if(requ.getItemVos().size()>0 && StringUtils.isBlank(requ.getItemVos().get(0).getAmount())){//支付金额
			return LbSelfUtil.selfLbzyError("未获取到支付金额相关数据");
		}
		LbzyRequItemVo requItemVo = requ.getItemVos().get(0);
		String recip[] = requItemVo.getpOSTransNO().split("|");
		if(recip.length<2) {
			return LbSelfUtil.selfLbzyError("支付数据不缺失！！!");
		}
		if(StringUtils.isBlank(requ.getPatientID())){//患者id
			return LbSelfUtil.selfLbzyError("未获取到患者ID相关数据");
		}
		try{
			LbPiMasterRegVo regvo = DataBaseHelper.queryForBean("SELECT pi.*,hp.pk_hp,hp.pk_insurance FROM pi_master pi LEFT JOIN PI_INSURANCE hp on hp.pk_pi = pi.pk_pi and hp.del_flag = '0' WHERE pi.del_flag = '0' and pi.pk_pi = ?", LbPiMasterRegVo.class, new Object[]{});
			//查询自费医保计划主键
			Map<String, Object> bdhpMap = DataBaseHelper.queryForMap("SELECT pk_hp FROM bd_hp WHERE eu_hptype=? and del_flag='0' ", "0");
			//2020-7-17 lb门诊挂号默认医保类型全自费
			regvo.setPkHp(LbSelfUtil.getPropValueStr(bdhpMap,"pkHp"));

			Map<String, Object> paramMap = new HashMap<String, Object>();
			paramMap.put("pkSch", requ.getScheduleCode());//获取号源id
			//根据号源主键查新排班信息
			List<Map<String, Object>> schPlanlist = schMapper.LbgetSchPlanInfo(paramMap);
			String pkDept = null;//挂号科室
			if(schPlanlist.size()<=0 || schPlanlist.get(0)==null){
				return LbSelfUtil.selfLbzyError("号源信息有误，请重新获取");
			}else{
				Map<String, Object> schPlanMap = schPlanlist.get(0);
				pkDept = MapUtils.getString(schPlanMap,"pkDept");

				//儿科年龄上限
				Map<String, Object> cardTypeMap = DataBaseHelper.queryForMap("select val from bd_sysparam where del_flag = '0' and  code='PV0019' and pk_org=?",UserContext.getUser().getPkOrg());
				//儿科科室
				Map<String, Object> codeTypeMap = DataBaseHelper.queryForMap("select val from bd_sysparam where del_flag = '0' and  code='CN0001' and pk_org=?",UserContext.getUser().getPkOrg());
				if(null != codeTypeMap){
					String card[] = codeTypeMap.get("val").toString().split(",");
					for (int i = 0; i < card.length; i++) {
						if((card[i]).equals(pkDept)){
							int age = DateUtils.getAge(regvo.getBirthDate());
							int Maximum = Integer.valueOf(cardTypeMap.get("val").toString());
							if(age-Maximum>0){
								return LbSelfUtil.selfLbzyError("所选挂号科室年龄最大:"+Maximum+"岁，患者实际："+age+"岁");
							}
						}

					}
				}

                //组装数据
				regvo.setDateReg(new Date());//挂号日期--排班日期
				regvo.setPkDept(MapUtils.getString(schPlanMap,"pkDept"));//挂号科室
				regvo.setEuSchclass("0");//排班类型
				//regvo.setPkDateslotsec(requ.getPhaseCode());//时段主键
				if(MapUtils.getString(schPlanMap,"euSrvtype").equals("9")){
					regvo.setEuPvtype("2");//就诊类型
				}else{
					regvo.setEuPvtype("1");//就诊类型
				}
				if("7".equals(requItemVo.getPayTypeID())){
					regvo.setDtPaymode("7");//支付方式7：微信，8：支付宝
				}else if("6".equals(requItemVo.getPayTypeID())){
					regvo.setDtPaymode("8");//支付方式7：微信，8：支付宝
				}
				regvo.setPkSchres(MapUtils.getString(schPlanMap,"pkSchres"));
				regvo.setPkDateslot(MapUtils.getString(schPlanMap,"pkDateslot"));//日期分组
				regvo.setTicketNo(MapUtils.getString(schPlanMap,"ticketno"));//预约票号
				regvo.setPkSch(MapUtils.getString(schPlanMap,"pkSch"));//排班主键
				regvo.setPkSchsrv(MapUtils.getString(schPlanMap,"pkSchsrv"));//排班服务主键

				//判断是否允许挂号
				String date = regvo.getDateAppt()!=null?DateUtils.formatDate(regvo.getDateAppt(), "yyyyMMdd"): (regvo.getDateReg()!=null?DateUtils.formatDate(regvo.getDateReg(), "yyyyMMdd"):null);
				if(StringUtils.isNotBlank(date)) {
					if(DataBaseHelper.queryForScalar("select count(*) from PV_ENCOUNTER t WHERE t.PK_PI=? AND to_char(t.DATE_BEGIN,'yyyyMMdd') =? AND t.PK_DEPT=? AND t.EU_PVTYPE in('1','2') AND t.EU_STATUS in('0','1')",
							Integer.class, new Object[]{regvo.getPkPi(),date,regvo.getPkDept()}) >0){
						return LbSelfUtil.selfLbzyError("该患者已经存在当前日期和科室的挂号记录！");
					}
				}

				BigDecimal amt=BigDecimal.ZERO;
				if(!StringUtils.isBlank(requItemVo.getAmount()) && !("0.0").equals(requItemVo.getAmount()) && !("0").equals(requItemVo.getAmount())&& !("0.00").equals(requItemVo.getAmount())){
					Double pric = Double.parseDouble(requItemVo.getAmount());
					amt=BigDecimal.valueOf(pric);

					//支付校验
					if(("6").equals(requItemVo.getPayTypeID()) || ("7").equals(requItemVo.getPayTypeID())){
						//门诊诊察费明细
						List<ItemPriceVo>  itemList = bdPubForWsMapper.LbgetBdItemInfo(LbSelfUtil.getPropValueStr(schPlanlist.get(0),"pkSchsrv"));
						for(ItemPriceVo vo:itemList){
							vo.setRatioDisc(1D);
							vo.setRatioSelf(1D);
							vo.setNameCg(vo.getName());
							vo.setPriceOrg(vo.getPrice());
						}
						regvo.setItemList(itemList);
						regvo.setInvStatus("-2");//打印发票控制参数

						BlDeposit dep =new BlDeposit();
						dep.setAmount(amt);
						if("7".equals(requItemVo.getPayTypeID())){
							dep.setDtPaymode("7");//支付方式7：微信，8：支付宝
						}else if("6".equals(requItemVo.getPayTypeID())){
							dep.setDtPaymode("8");//支付方式7：微信，8：支付宝
						}
						List<BlDeposit> depList =new ArrayList<>();
						depList.add(dep);
						regvo.setDepositList(depList);
					}else{
						return LbSelfUtil.selfLbzyError("暂不支持微信、支付宝以外的支付方式");
					}
				}

				//预约挂号
				ResponseJson  NowSjh =  apputil.execService("PV", "RegSyxHandler", "saveApptPvRegInfo",regvo,UserContext.getUser());
				if(NowSjh.getStatus()==Constant.SUC) {
					LbPiMasterRegVo regvos = JsonUtil.readValue(JsonUtil.writeValueAsString(NowSjh.getData()), LbPiMasterRegVo.class);
					if(("7").equals(requItemVo.getPayTypeID()) || ("8").equals(requItemVo.getPayTypeID())){
						//支付信息写入外部支付接口记录表bl_ext_pay
						BlDeposit vo =regvo.getDepositList().get(0);
						BlExtPay extPay = new BlExtPay();
						extPay.setPkExtpay(NHISUUID.getKeyId());
						extPay.setPkOrg(UserContext.getUser().getPkOrg());
						extPay.setAmount(BigDecimal.valueOf(Double.valueOf(requItemVo.getAmount())));
						if("7".equals(requItemVo.getPayTypeID())){
							extPay.setEuPaytype("7");//支付方式7：微信，8：支付宝
						}else if("6".equals(requItemVo.getPayTypeID())){
							extPay.setEuPaytype("8");//支付方式7：微信，8：支付宝
						}
						extPay.setFlagPay("1");
						extPay.setSerialNo(recip[1]);//订单号
						extPay.setTradeNo(recip[2]);//交易流水号
						extPay.setPkPi(regvos.getPkPi());
						extPay.setPkPv(regvos.getPkPv());
						extPay.setDateRefund(null);
						extPay.setEuBill("0");
						extPay.setDateBill(null);
						extPay.setDescPay("自助机金额缴纳"+requItemVo.getAmount());
						extPay.setDatePay(new Date());
						extPay.setPkDepo(regvos.getDepositList().get(0).getPkDepo());//BL_DEPOSIT主键
						extPay.setCreator(UserContext.getUser().getPkEmp());
						extPay.setCreateTime(new Date());
						extPay.setDelFlag("0");
						extPay.setTs(new Date());
						extPay.setModifier(regvos.getPkEmp());
						blPubForWsService.inserBlExtPay(extPay);
					}

					LbzyResponseVo response = new LbzyResponseVo();
					response.setTranSerialNO(regvos.getCodePv());//交易流水号
					response.setRegFlow(regvos.getCodePv());//挂号单据号
					response.setDoctorID(MapUtils.getString(schPlanMap,"codeEmp"));//医生编码
					//response.setSeeTime();//就诊时间
					response.setSeeNO(MapUtils.getString(schPlanMap,"ticketno"));//就诊序号
					response.setRegType(MapUtils.getString(schPlanMap,"stvName"));//挂号种类
					response.setPatientName(regvos.getNamePi());//患者姓名
					response.setPatientID(regvos.getPkPi());//患者ID
					response.setDepartmentName(MapUtils.getString(schPlanMap,"name"));//科室名称就诊科室
					response.setDepartmentAddress(MapUtils.getString(schPlanMap,"namePlace"));//科室位置就诊位置
					response.setDoctorName(MapUtils.getString(schPlanMap,"nameEmp"));//医生姓名
					response.setResultCode(Constant.RESFAIL);
					resXml = XmlUtil.beanToXml(response, LbzyResponseVo.class, false);
				}else{
					resXml = LbSelfUtil.selfLbzyError("预约挂号失败信息："+NowSjh.getDesc());
				}
			}
		} catch (Exception e) {
			resXml = LbSelfUtil.selfLbzyError("预约挂号失败信息："+e.getMessage());
		}
		return resXml;
	}

	/**lbzy-预约登记**/
	public String saveLbzyAppRegistration(LbzyRequestVo requ){
		String resXml = null;
		if(StringUtils.isBlank(requ.getScheduleCode())){//排班id
			return LbSelfUtil.selfLbzyError("未获取到相关排班数据");
		}
		if(StringUtils.isBlank(requ.getPatientID())){//患者id
			return LbSelfUtil.selfLbzyError("未获取到患者ID相关数据");
		}
		if(StringUtils.isBlank(requ.getScheduleItemCode())){//时间段主键
			return LbSelfUtil.selfLbzyError("未获取到时间段主键或预约班次编码");
		}
		try {
			List<Map<String, Object>> schAppList =DataBaseHelper.queryForList("SELECT * FROM sch_appt WHERE pk_sch=? and pk_pi=?", new Object[]{requ.getScheduleCode(),requ.getPatientID()});
			if(schAppList.size()>0){
				return XmlUtil.beanToXml(LbSelfUtil.common(Constant.RESFAIL, "当前排班已预约"), LbSHResponseVo.class, false);
			}
			LbPiMasterRegVo regvo =DataBaseHelper.queryForBean("SELECT * FROM pi_master pi LEFT JOIN PI_INSURANCE hp on hp.pk_pi = pi.pk_pi and hp.del_flag = '0' WHERE pi.del_flag = '0' and pi.pk_pi = ?", LbPiMasterRegVo.class, new Object[]{requ.getPatientID()});

			Map<String, Object> paramMap = new HashMap<String, Object>();
			paramMap.put("pkSch", requ.getScheduleCode());
			//根据号源主键查新排班信息
			List<Map<String, Object>> getSch = schMapper.LbTodaySchInfosByDate(paramMap);
			if(getSch.size()>0){
				Map<String, Object> schPlanMap = getSch.get(0);
				//儿科年龄上限
				Map<String, Object> cardTypeMaps = DataBaseHelper.queryForMap("select val from bd_sysparam where del_flag = '0' and  code='PV0019' and pk_org=?",UserContext.getUser().getPkOrg());
				//儿科科室
				Map<String, Object> codeTypeMap = DataBaseHelper.queryForMap("select val from bd_sysparam where del_flag = '0' and  code='CN0001' and pk_org=?",UserContext.getUser().getPkOrg());
				if(null != codeTypeMap){
					String card[] = codeTypeMap.get("val").toString().split(",");
					for (int i = 0; i < card.length; i++) {
						if((card[i]).equals(MapUtils.getString(schPlanMap,"codeDept"))){
							int age = DateUtils.getAge(regvo.getBirthDate());
							int Maximum = Integer.valueOf(cardTypeMaps.get("val").toString());
							if(age-Maximum>0){
								return LbSelfUtil.selfLbzyError("所选挂号科室年龄最大:"+Maximum+"岁，患者实际："+age+"岁");
							}
						}
					}
				}
				regvo.setEuSchclass("0");//排班类型
				if("9".equals(MapUtils.getString(schPlanMap,"euSrvtype"))){
					regvo.setEuPvtype("2");//就诊类型
				}else{
					regvo.setEuPvtype("1");//就诊类型
				}
				regvo.setPkSch(MapUtils.getString(schPlanMap, "pkSch"));
				regvo.setPkSchplan(MapUtils.getString(schPlanMap, "pkSchplan"));
				regvo.setPkPi(regvo.getPkPi());
				regvo.setIdNo(regvo.getIdNo());
				regvo.setPkSchsrv(MapUtils.getString(schPlanMap, "pkSchsrv"));
				regvo.setPkSchres(MapUtils.getString(schPlanMap, "pkSchres"));
				regvo.setPkDateslot(MapUtils.getString(schPlanMap, "pkDateslot"));
				regvo.setPkDateslotsec(requ.getScheduleItemCode());//时段主键
				regvo.setDateAppt(DateUtils.parseDate(MapUtils.getString(schPlanMap, "dateWork"),"yyyy-MM-dd"));//预约日期
				//调用预约登记接口
				ResponseJson  NowSjh =  apputil.execService("PV", "RegSyxHandler", "saveApptSchRegInfo",regvo,UserContext.getUser());
				if(NowSjh.getStatus()==Constant.SUC){
					Map<String, Object> pamMap = (Map<String, Object>)LbSelfUtil.beanToMap1(NowSjh.getData());
					LbzyResponseVo response = new LbzyResponseVo();
					response.setResultCode(Constant.RESFAIL);
					response.setTranSerialNO(MapUtils.getString(pamMap,"apptCode"));//交易流水号
					response.setRegFlow(MapUtils.getString(pamMap,"apptCode"));//挂号单据号
					response.setDoctorID(MapUtils.getString(schPlanMap,"codeEmp"));//医生编码
					//response.setSeeTime();//就诊时间
					response.setSeeNO(MapUtils.getString(schPlanMap,"ticketno"));//就诊序号
					response.setRegType(MapUtils.getString(schPlanMap,"stvName"));//挂号种类
					response.setPatientName(regvo.getNamePi());//患者姓名
					response.setPatientID(regvo.getPkPi());//患者ID
					response.setDepartmentName(MapUtils.getString(schPlanMap,"name"));//科室名称就诊科室
					response.setDepartmentAddress(MapUtils.getString(schPlanMap,"namePlace"));//科室位置就诊位置
					response.setDoctorName(MapUtils.getString(schPlanMap,"nameEmp"));//医生姓名
					resXml = XmlUtil.beanToXml(response, LbzyResponseVo.class, false);
				}else{
					resXml = LbSelfUtil.selfLbzyError(NowSjh.getDesc());
				}
			}else{
				resXml = LbSelfUtil.selfLbzyError("未查询到可预约号源");
			}
		} catch (Exception e) {
			resXml = LbSelfUtil.selfLbzyError(e.getMessage());
		}
		return resXml;
	}

	/**lbzy-预约取号**/
	public String saveLbzyAppGetMun(LbzyRequestVo requ){
		String resXml = null;
		if(StringUtils.isBlank(requ.getScheduleCode())){//号源id
			return LbSelfUtil.selfLbzyError("未获取到相排班数据");
		}
		//if(StringUtils.isBlank(requ.getScheduleCode())){//票号
		if(true){
			return LbSelfUtil.selfLbzyError("未获取到预约票号数据");
		}
		if(requ.getItemVos().size()>0 && StringUtils.isBlank(requ.getItemVos().get(0).getPayTypeID())){//支付方式
			return LbSelfUtil.selfLbzyError("未获取到支付方式相关数据");
		}
		if(requ.getItemVos().size()>0 && StringUtils.isBlank(requ.getItemVos().get(0).getAmount())){//支付金额
			return LbSelfUtil.selfLbzyError("未获取到支付金额相关数据");
		}
		LbzyRequItemVo requItemVo = requ.getItemVos().get(0);
		String recip[] = requItemVo.getpOSTransNO().split("|");
		if(recip.length<2) {
			return LbSelfUtil.selfLbzyError("支付数据不缺失！！!");
		}
		if(StringUtils.isBlank(requ.getPatientID())){//患者id
			return LbSelfUtil.selfLbzyError("未获取到患者ID相关数据");
		}
		try{
			LbPiMasterRegVo regvo = DataBaseHelper.queryForBean("SELECT pi.*,hp.pk_hp,hp.pk_insurance FROM pi_master pi LEFT JOIN PI_INSURANCE hp on hp.pk_pi = pi.pk_pi and hp.del_flag = '0' WHERE pi.del_flag = '0' and pi.pk_pi = ?", LbPiMasterRegVo.class, new Object[]{requ.getPatientID()});
			//查询自费医保计划主键
			Map<String, Object> bdhpMap = DataBaseHelper.queryForMap("SELECT pk_hp FROM bd_hp WHERE eu_hptype=? and del_flag='0' ", "0");
			//2020-7-17 lb门诊挂号默认医保类型全自费
			regvo.setPkHp(LbSelfUtil.getPropValueStr(bdhpMap,"pkHp"));

			Map<String, Object> paramMap = new HashMap<String, Object>();
			paramMap.put("pkSch", requ.getScheduleCode());//排班主键源id
			//paramMap.put("ticketno", );//预约票号
			//根据号源主键查新排班信息
			List<Map<String, Object>> schPlanlist = schMapper.LbgetSchPlanInfo(paramMap);
			String codeDept = null;//挂号科室
			if(schPlanlist.size()<=0 || schPlanlist.get(0)==null){
				return LbSelfUtil.selfLbzyError("号源信息有误，请重新获取");
			}else{
				Map<String, Object> schPlanMap = schPlanlist.get(0);
				codeDept = MapUtils.getString(schPlanMap,"codeDept");

				//儿科年龄上限
				Map<String, Object> cardTypeMap = DataBaseHelper.queryForMap("select val from bd_sysparam where del_flag = '0' and  code='PV0019' and pk_org=?",UserContext.getUser().getPkOrg());
				//儿科科室
				Map<String, Object> codeTypeMap = DataBaseHelper.queryForMap("select val from bd_sysparam where del_flag = '0' and  code='CN0001' and pk_org=?",UserContext.getUser().getPkOrg());
				if(null != codeTypeMap){
					String card[] = codeTypeMap.get("val").toString().split(",");
					for (int i = 0; i < card.length; i++) {
						if((card[i]).equals(codeDept)){
							int age = DateUtils.getAge(regvo.getBirthDate());
							int Maximum = Integer.valueOf(cardTypeMap.get("val").toString());
							if(age-Maximum>0){
								return LbSelfUtil.selfLbzyError("所选挂号科室年龄最大:"+Maximum+"岁，患者实际："+age+"岁");
							}
						}

					}
				}
				//判断是否允许挂号
				String date = regvo.getDateAppt()!=null?DateUtils.formatDate(regvo.getDateAppt(), "yyyyMMdd"): (regvo.getDateReg()!=null?DateUtils.formatDate(regvo.getDateReg(), "yyyyMMdd"):null);
				if(StringUtils.isNotBlank(date)) {
					if(DataBaseHelper.queryForScalar("select count(*) from PV_ENCOUNTER t WHERE t.PK_PI=? AND to_char(t.DATE_BEGIN,'yyyyMMdd') =? AND t.PK_DEPT=? AND t.EU_PVTYPE in('1','2') AND t.EU_STATUS in('0','1')",
							Integer.class, new Object[]{regvo.getPkPi(),date,regvo.getPkDept()}) >0){
						return LbSelfUtil.selfLbzyError("该患者已经存在当前日期和科室的挂号记录！");
					}
				}

				//组装数据
				boolean Iffeel = false;
				regvo.setDateReg(new Date());//挂号日期--排班日期
				regvo.setPkDept(MapUtils.getString(schPlanMap,"pkDept"));//挂号科室
				if(LbSelfUtil.getPropValueStr(schPlanMap,"euSrvtype").equals("9")){
					regvo.setEuPvtype("2");//就诊类型
				}
				if("7".equals(requItemVo.getPayTypeID())){
					regvo.setDtPaymode("7");//支付方式7：微信，8：支付宝
				}else if("6".equals(requItemVo.getPayTypeID())){
					regvo.setDtPaymode("8");//支付方式7：微信，8：支付宝
				}
				regvo.setPkSchres(LbSelfUtil.getPropValueStr(schPlanMap,"pkSchres"));
				regvo.setPkDateslot(LbSelfUtil.getPropValueStr(schPlanMap,"pkDateslot"));//日期分组
				regvo.setTicketNo(LbSelfUtil.getPropValueStr(schPlanMap,"ticketno"));//预约票号
				regvo.setPkSch(LbSelfUtil.getPropValueStr(schPlanMap,"pkSch"));//排班主键
				regvo.setPkSchsrv(LbSelfUtil.getPropValueStr(schPlanMap,"pkSchsrv"));//排班服务主键
				//组装收费信息
				BigDecimal amt=BigDecimal.ZERO;
				if(!StringUtils.isBlank(requItemVo.getAmount()) && !("0.0").equals(requItemVo.getAmount()) && !("0").equals(requItemVo.getAmount())&& !("0.00").equals(requItemVo.getAmount())){
					BlDeposit dep =new BlDeposit();
					Double pric = Double.parseDouble(requItemVo.getAmount());
					amt=BigDecimal.valueOf(pric);

					//账户支付可用余额校验
					if(("6").equals(requItemVo.getPayTypeID()) || ("7").equals(requItemVo.getPayTypeID())){
						dep.setAmount(amt);
						dep.setDtPaymode(requItemVo.getPayTypeID());//支付方式
						List<BlDeposit> depList =new ArrayList<>();
						depList.add(dep);
						regvo.setDepositList(depList);
						Iffeel = true;
						//门诊诊察费明细
						List<ItemPriceVo>  itemList = bdPubForWsMapper.LbgetBdItemInfo(LbSelfUtil.getPropValueStr(schPlanlist.get(0),"pkSchsrv"));
						regvo.setItemList(itemList);
					}else{
						return LbSelfUtil.selfLbzyError("暂不支持微信、支付宝以外的支付方式");
					}
				}

				//保存挂号信息（含保存患者信息）
				regvo =lbPubForWsService.savePvRegInfo(Iffeel, regvo, UserContext.getUser());
				if(Iffeel){
					if(("7").equals(requItemVo.getPayTypeID()) || ("8").equals(requItemVo.getPayTypeID())){
						//支付信息写入外部支付接口记录表bl_ext_pay
						BlDeposit vo =regvo.getDepositList().get(0);
						BlExtPay extPay = new BlExtPay();
						extPay.setPkExtpay(NHISUUID.getKeyId());
						extPay.setPkOrg(UserContext.getUser().getPkOrg());
						extPay.setAmount(BigDecimal.valueOf(Double.valueOf(requItemVo.getAmount())));
						if("7".equals(requItemVo.getPayTypeID())){
							extPay.setEuPaytype("7");//支付方式7：微信，8：支付宝
						}else if("6".equals(requItemVo.getPayTypeID())){
							extPay.setEuPaytype("8");//支付方式7：微信，8：支付宝
						}
						extPay.setFlagPay("1");
						extPay.setSerialNo(recip[1]);//订单号
						extPay.setTradeNo(recip[2]);//交易流水号
						extPay.setPkPi(regvo.getPkPi());
						extPay.setPkPv(regvo.getPkPv());
						extPay.setDateRefund(null);
						extPay.setEuBill("0");
						extPay.setDateBill(null);
						extPay.setDescPay("自助机金额缴纳"+requItemVo.getAmount());
						extPay.setDatePay(new Date());
						extPay.setPkDepo(vo.getPkDepo());//BL_DEPOSIT主键
						extPay.setCreator(UserContext.getUser().getPkEmp());
						extPay.setCreateTime(new Date());
						extPay.setDelFlag("0");
						extPay.setTs(new Date());
						extPay.setModifier(regvo.getPkEmp());
						blPubForWsService.inserBlExtPay(extPay);
					}
				}

				LbzyResponseVo response = new LbzyResponseVo();
				response.setTranSerialNO(regvo.getCodePv());//交易流水号
				response.setRegFlow(regvo.getCodePv());//挂号单据号
				response.setDoctorID(MapUtils.getString(schPlanlist.get(0),"codeEmp"));//医生编码
				//response.setSeeTime();//就诊时间
				response.setSeeNO(MapUtils.getString(schPlanlist.get(0),"ticketno"));//就诊序号
				response.setRegType(MapUtils.getString(schPlanlist.get(0),"stvName"));//挂号种类
				response.setPatientName(regvo.getNamePi());//患者姓名
				response.setPatientID(regvo.getPkPi());//患者ID
				response.setDepartmentName(MapUtils.getString(schPlanlist.get(0),"name"));//科室名称就诊科室
				response.setDepartmentAddress(MapUtils.getString(schPlanlist.get(0),"namePlace"));//科室位置就诊位置
				response.setDoctorName(MapUtils.getString(schPlanlist.get(0),"nameEmp"));//医生姓名
				response.setResultCode(Constant.RESFAIL);
				resXml = XmlUtil.beanToXml(response, LbzyResponseVo.class, false);
			}
		} catch (Exception e) {
			resXml = LbSelfUtil.selfLbzyError("挂号失败信息："+e.getMessage());
		}
		return resXml;
	}

	/**lbzy-排班时段查询**/
	public String querLbzySchSec(LbzyRequestVo requ){
		String resXml = null;
		if(StringUtils.isNotBlank(requ.getScheduleCode())){//排班主键pkSch
			Map<String, Object> paramMap = new HashMap<String, Object>();
			paramMap.put("pkSch", requ.getScheduleCode());
			List<Map<String, Object>> schpubList = schMapper.LbTodaySchInfosByDate(paramMap);
			if(schpubList.size()>0) {
				List<Map<String, Object>> schSecList = schMapper.querySchAppSec(schpubList.get(0));
				if(schSecList.size()>0) {
					LbzyResponseVo response = new LbzyResponseVo();
					response.setResultCode(Constant.RESFAIL);
					List<LbzyResponItemVo> ponList = new ArrayList<>();
					for(Map<String, Object> schSecMap:schSecList){
						if(0<Integer.valueOf(MapUtils.getString(schSecMap,"cnt"))){
							String dateEnd = MapUtils.getString(schpubList.get(0),"dateWork")+" "+MapUtils.getString(schSecMap,"timeEnd");
			                Date clinicDate = DateUtils.strToDate(dateEnd, "yyyy-MM-dd HH:mm:ss");
			                if(new Date().before(clinicDate)){
			                	LbzyResponItemVo pon = new LbzyResponItemVo();
								pon.setCode(MapUtils.getString(schSecMap,"pkDateslotsec"));//班次代码
								pon.setName(MapUtils.getString(schpubList.get(0),"nameDateslot"));//班次名称
								pon.setRemainNum(MapUtils.getString(schSecMap,"cnt"));//剩余号源数
								pon.setBeginTime(MapUtils.getString(schSecMap,"timeBegin").substring(0, 5));//开始时间
								pon.setEndTime(MapUtils.getString(schSecMap,"timeEnd").substring(0, 5));//结束时间
								ponList.add(pon);
			                }
						}
						
					}
					resXml = XmlUtil.beanToXml(response, LbzyResponseVo.class, false);
				}else {
					resXml = LbSelfUtil.selfLbzyError("未查询到排班相关时段信息");
				}
			}else {
				resXml = LbSelfUtil.selfLbzyError("未查询到排班相关数据");
			}
		}else {
			resXml = LbSelfUtil.selfLbzyError("未获取到排班主键数据");
		}
		return resXml;
	}

	/**lbzy-锁号**/
	public String upLbzyLockReg(LbzyRequestVo requ){
		String resXml = null;
		String pkSch = null;
		if(StringUtils.isBlank(requ.getScheduleCode())){//号源id
			return LbSelfUtil.selfLbzyError("未获取到相关号源数据");
		}else{
			pkSch = requ.getScheduleCode();
		}
		if(StringUtils.isBlank(requ.getPatientID())){//患者id
			return LbSelfUtil.selfLbzyError("未获取到患者ID相关数据");
		}
		try{
			Map<String, Object> paramMap = new HashMap<String, Object>();
			paramMap.put("pkSch", pkSch);
			//根据号源主键查新排班信息
			List<Map<String, Object>> schPlanlist = schMapper.LbgetSchPlanInfo(paramMap);
			//儿科年龄上限
			Map<String, Object> cardTypeMap = DataBaseHelper.queryForMap("select val from bd_sysparam where del_flag = '0' and  code='PV0019' and pk_org=?",UserContext.getUser().getPkOrg());
			//儿科科室
			Map<String, Object> codeTypeMap = DataBaseHelper.queryForMap("select val from bd_sysparam where del_flag = '0' and  code='CN0001' and pk_org=?",UserContext.getUser().getPkOrg());
			LbPiMasterRegVo regvo = DataBaseHelper.queryForBean("SELECT * FROM pi_master pi LEFT JOIN PI_INSURANCE hp on hp.pk_pi = pi.pk_pi and hp.del_flag = '0' WHERE pi.del_flag = '0' and pi.pk_pi = ?", LbPiMasterRegVo.class, new Object[]{requ.getPatientID()});
			if(null != codeTypeMap){
				String card[] = codeTypeMap.get("val").toString().split(",");
				for (int i = 0; i < card.length; i++) {
					if((card[i]).equals(MapUtils.getString(schPlanlist.get(0),"codeDept"))){
						int age = DateUtils.getAge(regvo.getBirthDate());
						int Maximum = Integer.valueOf(cardTypeMap.get("val").toString());
						if(age-Maximum>0){
							return LbSelfUtil.selfLbzyError("所选挂号科室年龄最大:"+Maximum+"岁，患者实际："+age+"岁");
						}
					}

				}
			}
			SchTicket ticket = null;
			LbSHRequestVo schReq = new LbSHRequestVo();
			schReq.setRegId(pkSch);//排班主键
			schReq.setPhaseCode(requ.getScheduleItemCode());//时段编码
			LbSHResponseVo responseVo = lbPubForWsService.lbLockReg(schReq, ticket);
			LbzyResponseVo response = new LbzyResponseVo();
			response.setResultCode(Constant.RESFAIL);
			response.setLockQueueNo(responseVo.getWaitNo());//候诊号
			response.setScheduleCode(responseVo.getRegId());//号源主键
			resXml = XmlUtil.beanToXml(response, LbzyResponseVo.class, false);
		} catch (Exception e) {
			resXml = LbSelfUtil.selfLbzyError("挂号失败信息："+e.getMessage());
		}
		return resXml;
	}

	/**lbzy-取消锁号**/
	public String upLbzyUnLockReg(LbzyRequestVo requ){
		String resXml = null;
		String regId = null;
		if(StringUtils.isBlank(requ.getScheduleCode())){//号源id
			regId = requ.getScheduleCode();
			return LbSelfUtil.selfLbzyError("未获取到相关号源数据");
		}
		try {
			//解锁
			lbPubForWsService.lbUnLockReg(regId);
			LbzyResponseVo response = new LbzyResponseVo();
			response.setResultCode(Constant.RESFAIL);
			resXml = XmlUtil.beanToXml(response, LbzyResponseVo.class, false);
		} catch (Exception e) {
			resXml = LbSelfUtil.selfLbzyError("解锁失败！");
		}
		return resXml;
	}

    /**lbzy-查询就诊记录**/
	public String querLbzyClinicRecordList(LbzyRequestVo requ){
		String resXml = null;
		if(StringUtils.isNotBlank(requ.getPatientID())){//患者id
			Map<String, Object> paramMap = new HashMap<String, Object>();
			paramMap.put("pkPi", requ.getPatientID());
			List<Map<String, Object>> queryPiOpList = pvPubForWsMapper.getPvInfoByOpStatus(paramMap);
			if (queryPiOpList.size()>0){
				LbzyResponseVo response = new LbzyResponseVo();
				response.setResultCode(Constant.RESFAIL);
				List<LbzyResponItemVo> ponList = new ArrayList<>();
				for(Map<String, Object> PiOpmap:queryPiOpList){
					LbzyResponItemVo pon = new LbzyResponItemVo();
					pon.setRegFlow(MapUtils.getString(PiOpmap,"codePv"));//挂号单据号
					pon.setPatientID(MapUtils.getString(PiOpmap,"pkPi"));//患者ID
					//pon.setPactCode;//参保编码
					pon.setDepartmentID(MapUtils.getString(PiOpmap,"deptCode"));//挂号科室编码
					pon.setDepartmentName(MapUtils.getString(PiOpmap,"deptName"));//挂号科室名称
					pon.setDoctorID(MapUtils.getString(PiOpmap,"empCode"));//医生编号
					pon.setDoctorName(MapUtils.getString(PiOpmap,"empName"));//医生姓名
					//pon.setRecipeNO(MapUtils.getString(PiOpmap,""));//单据号
					//pon.setRecipeSEQ;//处方号
					pon.setSeeTime(MapUtils.getString(PiOpmap,"birthday"));//日期
					String sql = "select sum(amount) from bl_op_dt where flag_settle='0' and pk_settle is NULL and pk_pv=?";
					String cost = DataBaseHelper.queryForScalar(sql, String.class,MapUtils.getString(PiOpmap,"visitId"));
					pon.setTotalCost(cost);//总额
					ponList.add(pon);
				}
				response.setItemVos(ponList);
				resXml = XmlUtil.beanToXml(response, LbzyResponseVo.class, false);
			}else{
				LbSelfUtil.selfLbzyError("未查询到该患者就诊信息");
			}
		}else{
			LbSelfUtil.selfLbzyError("未获取到患者ID");
		}
		return resXml;
	}

	/**lbzy-查询待缴费用明细**/
	public String querLbzyUnpaidRecordList(LbzyRequestVo requ){
		String resXml = null;
		if(StringUtils.isNotBlank(requ.getRegFlow())){//单据号
            //查询自费医保计划主键
			Map<String, Object> bdhpMap = DataBaseHelper.queryForMap("SELECT pk_hp FROM bd_hp WHERE eu_hptype=? and del_flag='0' ", "0");
			//查询患者医保计划
			String masSql="select pk_insu,pk_pv from pv_encounter where code_pv= ?";
			Map<String, Object> pkInsuMap = DataBaseHelper.queryForMap(masSql, requ.getRegFlow());
			String pkPv = MapUtils.getString(pkInsuMap,"pkPv");
			//判断患者医保计划是否是全自费
			if(!(MapUtils.getString(bdhpMap,"pkHp")).equals(MapUtils.getString(pkInsuMap,"pkInsu"))){
				return LbSelfUtil.selfLbzyError("就诊类型非自费！请去窗口进行医保结算");
			}
			LbzyResponseVo response = new LbzyResponseVo();
			response.setResultCode(Constant.RESFAIL);
			List<LbzyResponItemVo> ponList = new ArrayList<>();

			Map<String, Object> paramMap = new HashMap<String, Object>();
			paramMap.put("visitId",pkPv);
			//查询处方信息
			List<Map<String, Object>> opCgDayDetail = cnPubForWsMapper.LbqueryCnOrderUnpaid(paramMap);
			if(opCgDayDetail.size()>0){
				for(Map<String, Object> map :opCgDayDetail){
					BigDecimal amtRec = BigDecimal.ZERO;
					LbzyResponItemVo pon = new LbzyResponItemVo();
					pon.setRecipeSEQ(MapUtils.getString(map,"ordsn"));//处方号
					pon.setNum(MapUtils.getString(map,"ordsn"));//项目序号
					pon.setCode(MapUtils.getString(map,"codeOrd"));//项目编码
					pon.setName(MapUtils.getString(map,"recipeName"));//项目名称
					//pon.setUnit();//项目单位
					//pon.setSpec();//项目规格
					//pon.setPrice();//单价
					pon.setQuantity(MapUtils.getString(map,"quan"));//数量
					//pon.setRemark();//备注

					String sql = "select itemcate.name as item_type,dt.* from bl_op_dt dt INNER JOIN bd_itemcate itemcate ON itemcate.pk_itemcate = dt.pk_itemcate where pk_cnord =?";
					List<Map<String, Object>> blOpList=DataBaseHelper.queryForList(sql,MapUtils.getString(map,"recipeId"));
					if(blOpList.size()>0){
						for(Map<String, Object> Opmap:blOpList){
							/*
							LbBlCgIpVo lbVo = new LbBlCgIpVo();
							lbVo.setItemId(LbSelfUtil.getPropValueStr(Opmap,"pkCgop"));//项目主键
							lbVo.setItemType(LbSelfUtil.getPropValueStr(Opmap,"itemType"));//项目类别
							lbVo.setItemName(LbSelfUtil.getPropValueStr(Opmap,"nameCg"));//项目名称
							lbVo.setSpecs(LbSelfUtil.getPropValueStr(Opmap,"spec"));//规格
							lbVo.setQty(LbSelfUtil.getPropValueStr(Opmap,"quan"));//项目数量
							lbVo.setPrice(LbSelfUtil.getPropValueStr(Opmap,"price"));//项目单价
							lbVo.setItemFee(LbSelfUtil.getPropValueStr(Opmap,"amount"));//项目总费用
							 */
							if(StringUtils.isNotBlank(Opmap.get("amount").toString())){
								Double pric = Double.parseDouble(Opmap.get("amount").toString());
								BigDecimal amt=BigDecimal.valueOf(pric);
								//amtAcc=amtAcc.add(amt);
								amtRec=amtRec.add(amt);
							}
						}
					}
					pon.setCost(amtRec.toString());//金额
					ponList.add(pon);
				}
			}
			//查询无处方费用信息
			String sql = "select itemcate.name as item_type,dt.* from bl_op_dt dt INNER JOIN bd_itemcate itemcate ON itemcate.pk_itemcate = dt.pk_itemcate where dt.flag_settle='0' and dt.pk_settle is NULL and dt.pk_cnord is NULL and dt.pk_pv =?";
			List<Map<String, Object>> blOpList=DataBaseHelper.queryForList(sql,pkPv);
			if(blOpList.size()>0){
				BigDecimal amtRec = BigDecimal.ZERO;
				LbzyResponItemVo pon = new LbzyResponItemVo();
				pon.setRecipeSEQ("0");//处方号
				pon.setNum("0");//项目序号
				pon.setCode("0");//项目编码
				pon.setName("其它");//项目名称
				//pon.setUnit();//项目单位
				//pon.setSpec();//项目规格
				pon.setQuantity("1");//数量
				//pon.setRemark();//备注

				for(Map<String, Object> Opmap:blOpList){
					/*
					LbBlCgIpVo lbVo = new LbBlCgIpVo();
					lbVo.setItemId(LbSelfUtil.getPropValueStr(Opmap,"pkCgop"));//项目主键
					lbVo.setItemType(LbSelfUtil.getPropValueStr(Opmap,"itemType"));//项目类别
					lbVo.setItemName(LbSelfUtil.getPropValueStr(Opmap,"nameCg"));//项目名称
					lbVo.setSpecs(LbSelfUtil.getPropValueStr(Opmap,"spec"));//规格
					lbVo.setQty(LbSelfUtil.getPropValueStr(Opmap,"quan"));//项目数量
					lbVo.setPrice(LbSelfUtil.getPropValueStr(Opmap,"price"));//项目单价
					lbVo.setItemFee(LbSelfUtil.getPropValueStr(Opmap,"amount"));//项目总费用

					 */
					if(StringUtils.isNotBlank(Opmap.get("amount").toString())){
						Double pric = Double.parseDouble(Opmap.get("amount").toString());
						BigDecimal amt=BigDecimal.valueOf(pric);
						//amtAcc=amtAcc.add(amt);
						amtRec=amtRec.add(amt);
					}
				}
				pon.setPrice(amtRec.toString());//单价
				pon.setCost(amtRec.toString());//金额
				ponList.add(pon);
			}
			response.setItemVos(ponList);
			resXml = XmlUtil.beanToXml(response, LbzyResponseVo.class, false);
		}else{
			resXml = LbSelfUtil.selfLbzyError("未获取到单据号");
		}
		return resXml;
	}

	/**lbzy-门诊支付**/
	public String upLbzyPayment(LbzyRequestVo requ){
		String resXml = null;
		String recipeIds = null;
		if(true){//费用明细id
			return LbSelfUtil.selfLbzyError("未获取到费用明细信息");
		}
		if(true){//患者id
			return LbSelfUtil.selfLbzyError("未获取到就诊相关数据");
		}
		if(requ.getItemVos().size()>0 && StringUtils.isBlank(requ.getItemVos().get(0).getPayTypeID())){//支付方式
			return LbSelfUtil.selfLbzyError("未获取到支付方式相关数据");
		}
		if(requ.getItemVos().size()>0 && StringUtils.isBlank(requ.getItemVos().get(0).getAmount())){//支付金额
			return LbSelfUtil.selfLbzyError("未获取到支付金额相关数据");
		}
		LbzyRequItemVo requItemVo = requ.getItemVos().get(0);
		String recipPay[] = requItemVo.getpOSTransNO().split("|");
		if(recipPay.length<2) {
			return LbSelfUtil.selfLbzyError("支付数据不缺失！！!");
		}
        //查询患者就诊信息
		String masSql="select ma.code_op,ous.code_emp,dept.name_dept,pv.* from pv_encounter pv INNER JOIN pi_master ma on ma.pk_pi=pv.pk_pi INNER JOIN BD_OU_DEPT dept on dept.pk_dept=pv.pk_dept INNER JOIN bd_ou_employee ous on ous.pk_emp=pv.pk_emp_phy where pk_pv= ?";
		Map<String, Object> PatMap = DataBaseHelper.queryForMap(masSql, "");
		if(BeanUtils.isNotNull(PatMap)){
			try{
				//1.去重查询结算费用明细信息
				List<BlOpDt> noSettleBlOpDts  = new ArrayList<BlOpDt>();//未结算集合
				List<BlOpDt> noPkcnordBlOpDts  = new ArrayList<BlOpDt>();//未结算集合
				Set<String> noSettlePkBlOpDt = new HashSet<String>();
				BigDecimal amtAcc = BigDecimal.ZERO;
				Set<String> pkBlOpDt = new HashSet<String>();
				Set<String> pkCnordS = new HashSet<String>();
				//判断指定字符是否出现在该字符串的第一位  是--返回下标1   否--返回下标0
				int beginIndex = recipeIds.indexOf(",") == 0 ? 1 : 0;
				//判断指定字符是否出现在该字符串的最后一位  是--返回出现的位置   否--返回字符长度
				int endIndex = recipeIds.lastIndexOf(",") + 1 == recipeIds.length() ? recipeIds.lastIndexOf(",") : recipeIds.length();
				String source = recipeIds.substring(beginIndex, endIndex);
				String recip[] = recipeIds.split(",");
				for (String recipeid : recip) {
					if(pkBlOpDt.add(recipeid)){
						String sql = "select * from bl_op_dt where pk_cgop='"+recipeid+"'";
						List<BlOpDt> blOpDts = DataBaseHelper.queryForList(sql, BlOpDt.class, new Object[] {});
						BigDecimal Payamt = BigDecimal.ZERO;
						for (BlOpDt blOpDt : blOpDts) {
							if(Constant.RESFAIL.equals(blOpDt.getFlagSettle())&& null == blOpDt.getPkSettle()){
								if(null != blOpDt.getAmount()){
									BigDecimal amts=BigDecimal.valueOf(blOpDt.getAmount());
									BigDecimal amt=BigDecimal.valueOf(blOpDt.getAmount());
									amtAcc=amtAcc.add(amt);
									Payamt=Payamt.add(amts);
								}
								if(blOpDt.getPkCnord()!=null){
									pkCnordS.add(blOpDt.getPkCnord());
								}else{
									noPkcnordBlOpDts.add(blOpDt);
								}
								noSettleBlOpDts.add(blOpDt);
								noSettlePkBlOpDt.add(blOpDt.getPkCgop());
							}
						}
					}
				}
				if(noSettleBlOpDts ==null || noSettleBlOpDts.size()<=0){
					return LbSelfUtil.selfLbzyError("未查询都收费项目，请重新查询！");
				}else{
					if(!MathUtils.equ(amtAcc.doubleValue(), Double.valueOf(requ.getItemVos().get(0).getAmount()))){
						return LbSelfUtil.selfLbzyError("支付金额有误,请核对！");
					}
				}
				List<BlDeposit> BlDepositList = new ArrayList<>();//支付信息
				BlDeposit blDeposit = new BlDeposit();
				OpCgTransforVo opCgTransforVo =new OpCgTransforVo();
				opCgTransforVo.setAmtInsuThird(BigDecimal.valueOf(0D));//医保支付金额

				//支付信息写入外部支付接口记录表bl_ext_pay
				BlExtPay extPay = new BlExtPay();
				extPay.setPkExtpay(NHISUUID.getKeyId());
				extPay.setPkOrg(UserContext.getUser().getPkOrg());
				extPay.setAmount(BigDecimal.valueOf(Double.valueOf(requItemVo.getAmount())));
				if("7".equals(requItemVo.getPayTypeID())){
					extPay.setEuPaytype("7");//支付方式7：微信，8：支付宝
				}else if("6".equals(requItemVo.getPayTypeID())){
					extPay.setEuPaytype("8");//支付方式7：微信，8：支付宝
				}
				extPay.setFlagPay("1");
				extPay.setSerialNo(recipPay[1]);//订单号
				extPay.setTradeNo(recipPay[2]);//交易流水号
				extPay.setPkPi(MapUtils.getString(PatMap,"pkPi"));
				extPay.setPkPv(MapUtils.getString(PatMap,"pkPv"));
				extPay.setDateRefund(null);
				extPay.setEuBill("0");
				extPay.setDateBill(null);
				extPay.setDescPay("自助机门诊支付"+requItemVo.getAmount());
				extPay.setDatePay(new Date());
				extPay.setCreator(UserContext.getUser().getPkEmp());
				extPay.setCreateTime(new Date());
				extPay.setDelFlag("0");
				extPay.setTs(new Date());
				blPubForWsService.inserBlExtPay(extPay);

				blDeposit.setPayInfo(recipPay[1]);//订单号
				blDeposit.setDtPaymode(extPay.getEuPaytype());//支付方式
				BlDepositList.add(blDeposit);
				BlPubSettleVo blPubSettleVo = new BlPubSettleVo();
				blPubSettleVo.setPkPi(MapUtils.getString(PatMap,"pkPi"));
				blPubSettleVo.setPkPv(MapUtils.getString(PatMap,"pkPv"));
				blPubSettleVo.setBlOpDts(noSettleBlOpDts);
				//blPubSettleVo.setInvoiceInfo(invoList); // 获取发票列表
				blPubSettleVo.setDepositList(BlDepositList);
				blPubSettleVo.setPkBlOpDtInSql(CommonUtils.convertSetToSqlInPart(noSettlePkBlOpDt, "pk_cgop"));//收费明细主键
				blPubSettleVo.setEuPvType(extPay.getEuPaytype());//支付方式
				opCgTransforVo.setPkPi(MapUtils.getString(PatMap,"pkPi"));
				opCgTransforVo.setPkPv(MapUtils.getString(PatMap,"pkPv"));
				opCgTransforVo.setBlOpDts(noSettleBlOpDts);
				opCgTransforVo.setBlDeposits(BlDepositList);
				opCgTransforVo.setInvStatus("-2");//控制打印发票参数

				ResponseJson  result =  apputil.execService("BL", "opCgSettlementService", "mergeOpcgAccountedSettlement", opCgTransforVo,UserContext.getUser());
				if(result.getStatus()== Constant.SUC){
					/*
					//生成熊响应消息
					List<LbRecipesVo>  recList = new ArrayList<>();
					if(noPkcnordBlOpDts.size()>0){
						LbRecipesVo rec = new LbRecipesVo();
						BigDecimal amtBlOp = BigDecimal.ZERO;
						List<LbBlCgIpVo> blOpDtList = new ArrayList<>();
						for(BlOpDt blOpDt:noPkcnordBlOpDts){
							if(blOpDt.getPkCnord()==null){
								LbBlCgIpVo lbVo = new LbBlCgIpVo();

								lbVo.setItemName(blOpDt.getNameCg());//项目名称
								lbVo.setSpecs(blOpDt.getSpec());//规格
								if(blOpDt.getQuan() !=null){
									lbVo.setQty(blOpDt.getQuan().toString());//项目数量
								}
								if(blOpDt.getPrice()!=null){
									lbVo.setPrice(blOpDt.getPrice().toString());//项目单价
								}
								if(blOpDt.getAmount()!=null){
									lbVo.setItemFee(blOpDt.getAmount().toString());//项目总费用
								}
								blOpDtList.add(lbVo);
								if(StringUtils.isNotBlank(lbVo.getItemFee())){
									Double pric = Double.parseDouble(lbVo.getItemFee());
									BigDecimal amt=BigDecimal.valueOf(pric);
									amtBlOp=amtBlOp.add(amt);
								}
							}
						}
						rec.setRecipeId("1");
						rec.setRecipeName("其他");
						rec.setRecipeTime(DateUtils.formatDate(DateUtils.parseDate(LbSelfUtil.getPropValueStr(PatMap,"dateBegin")),new Object[]{}));
						rec.setExecDept(LbSelfUtil.getPropValueStr(PatMap,"nameDept"));//执行科室
						rec.setRecipeFee(amtBlOp.toString());
						rec.setLbBlCgIpVo(blOpDtList);

						recList.add(rec);
					}

					for(String pkCnord :pkCnordS){
						LbRecipesVo rec = new LbRecipesVo();
						List<LbBlCgIpVo> blOpDtList = new ArrayList<>();
						Map<String, Object>  cnordMap = new HashMap<String, Object>();
						BigDecimal amtBlOp = BigDecimal.ZERO;
						cnordMap.put("pkCnord",pkCnord);
						List<Map<String, Object>> opCgDayDetail = cnPubForWsMapper.LbqueryCnOrderUnpaid(cnordMap);
						rec = (LbRecipesVo)LbSelfUtil.mapToBean(opCgDayDetail.get(0),rec);
						rec.setRecipeTime(DateUtils.formatDate(DateUtils.parseDate(opCgDayDetail.get(0).get("recipeTime").toString()),new Object[]{}));

						String sql = "select itemcate.name as item_type,dt.* from bl_op_dt dt INNER JOIN bd_itemcate itemcate ON itemcate.pk_itemcate = dt.pk_itemcate where pk_cnord =?";
						List<Map<String, Object>> blOpList=DataBaseHelper.queryForList(sql, pkCnord);
						if(blOpList.size()>0){
							for(Map<String, Object> Opmap:blOpList){
								LbBlCgIpVo lbVo = new LbBlCgIpVo();
								lbVo.setItemType(LbSelfUtil.getPropValueStr(Opmap,"itemType"));//项目类别
								lbVo.setItemName(LbSelfUtil.getPropValueStr(Opmap,"nameCg"));//项目名称
								lbVo.setSpecs(LbSelfUtil.getPropValueStr(Opmap,"spec"));//规格
								lbVo.setQty(LbSelfUtil.getPropValueStr(Opmap,"quan"));//项目数量
								lbVo.setPrice(LbSelfUtil.getPropValueStr(Opmap,"price"));//项目单价
								lbVo.setItemFee(LbSelfUtil.getPropValueStr(Opmap,"amount"));//项目总费用
								blOpDtList.add(lbVo);
								if(StringUtils.isNotBlank(lbVo.getItemFee())){
									Double pric = Double.parseDouble(lbVo.getItemFee());
									BigDecimal amt=BigDecimal.valueOf(pric);
									amtBlOp=amtBlOp.add(amt);
								}
							}
						}
						rec.setRecipeFee(amtBlOp.toString());//费用
						rec.setLbBlCgIpVo(blOpDtList);
						recList.add(rec);
					}
					responseVo.setRecipes(recList);
					responseVo.setDoctName(LbSelfUtil.getPropValueStr(PatMap,"nameEmpPhy"));//医生名称
					responseVo.setDoctCode(LbSelfUtil.getPropValueStr(PatMap,"codeEmp"));//医生编码
					responseVo.setTotalFee(amtAcc.toString());//总费用
					responseVo.setCodeOp(LbSelfUtil.getPropValueStr(PatMap,"codeOp"));
					//responseVo.setInvoiceNo(queryForMap.get("curCodeInv").toString());//发票号
					Map<String, Object> piCardMap = DataBaseHelper.queryForMap("select CARD_NO from PI_CARD where FLAG_ACTIVE = '1' AND EU_STATUS ='0' and DT_CARDTYPE ='01' and pk_pi=? ORDER BY create_time DESC", new Object[]{LbSelfUtil.getPropValueStr(PatMap,"pkPi")});
					if(piCardMap != null){
						responseVo.setDtcardNo(LbSelfUtil.getPropValueStr(piCardMap,"cardNo"));
					}else{
						responseVo.setDtcardNo(LbSelfUtil.getPropValueStr(PatMap,"codeOp"));
					}
					 */
					LbzyResponseVo response = new LbzyResponseVo();
					response.setResultCode(Constant.RESFAIL);

					resXml = XmlUtil.beanToXml(response, LbzyResponseVo.class, false);
				}else{
					Map<String, Object> paramExtPay =new HashMap<String, Object>();
					paramExtPay.put("tradeNo", recipPay[2]);//订单号
					paramExtPay.put("serialNo", recipPay[1]);//交易流水号
					//修改bl_ext_pay表flag_pay为'0'
					blPubForWsService.lbUpdateBlExtPay(paramExtPay);
					resXml = LbSelfUtil.selfLbzyError(result.getDesc());
				}
			}catch (Exception e) {
				resXml = LbSelfUtil.selfLbzyError(e.getMessage());
			}
		}else{
			resXml = LbSelfUtil.selfLbzyError("未查询到该患者有效信息");
		}
		return resXml;
	}

	/**lbzy-查询住院患者信息**/
	public String querLbzyInpatientIpInfo(LbzyRequestVo requ){
		String resXml = null;

		if(StringUtils.isNotBlank(requ.getCardTypeID()) && StringUtils.isNotBlank(requ.getCardNO())){
			Map<String, Object> paramMap = new HashMap<>();
			switch (requ.getCardTypeID()){
				case "1"://诊疗卡
					paramMap.put("dtCardType","01");
					paramMap.put("dtCardNo",requ.getCardNO());
					break;
				case "2"://身份证
					paramMap.put("idno",requ.getCardNO());
					break;
				case "3"://社保卡
					paramMap.put("hicNo",requ.getCardNO());
					break;
				case "5"://病人ID
					paramMap.put("pkPi",requ.getCardNO());
					break;
				case "6"://居民健康卡
					paramMap.put("hicNo",requ.getCardNO());
					break;
				default:
					return LbSelfUtil.selfLbzyError("卡类型数据无效");
			}
			try {
				List<Map<String, Object>> pvInfo = pvPubForWsMapper.getPvInfoByIp(paramMap);
				if(pvInfo.size()>0){
					LbzyResponseVo response = new LbzyResponseVo();
					response.setResultCode(Constant.RESFAIL);
					List<LbzyResponItemVo> ponList = new ArrayList<>();
					for (Map<String, Object> today:pvInfo) {
						LbzyResponItemVo pon = new LbzyResponItemVo();

						ponList.add(pon);//去重复科室信息
					}
					response.setItemVos(ponList);
					resXml = XmlUtil.beanToXml(response, LbzyResponseVo.class, false);
				}else{
					resXml = LbSelfUtil.selfLbzyError("未查询到相关数据");
				}
			} catch (Exception e) {
				resXml = LbSelfUtil.selfLbzyError(e.getMessage());
			}
		}else{
			resXml = LbSelfUtil.selfLbzyError("未获取到卡类型数据");
		}
		return resXml;
	}

	/**lbzy-保存住院预交金**/
	public String saveLbzyRechargeInpatientDeposit(LbzyRequestVo requ){
		String resXml = null;
		if(StringUtils.isBlank(requ.getInPatientID())){//号源id
			return LbSelfUtil.selfLbzyError("未获取到相关住院号数据");
		}
		if(requ.getItemVos().size()>0 && StringUtils.isBlank(requ.getItemVos().get(0).getPayTypeID())){//支付方式
			return LbSelfUtil.selfLbzyError("未获取到支付方式相关数据");
		}
		if(requ.getItemVos().size()>0 && StringUtils.isBlank(requ.getItemVos().get(0).getAmount())){//支付金额
			return LbSelfUtil.selfLbzyError("未获取到支付金额相关数据");
		}
		LbzyRequItemVo requItemVo = requ.getItemVos().get(0);
		String recip[] = requItemVo.getpOSTransNO().split("|");
		if(recip.length<2) {
			return LbSelfUtil.selfLbzyError("支付数据不缺失！！!");
		}
		//查询患者就诊信息
		Map<String, Object> PatMap = DataBaseHelper.queryForMap("select * from pv_encounter pv left join pi_master pi on pv.pk_pi=pi.pk_pi and pi.del_flag='0' where pv.eu_pvtype= '3' and (pv.eu_status = '0' or pv.eu_status = '1') and pi.code_ip= ?", requ.getInPatientID());
		if(!BeanUtils.isNotNull(PatMap)){
			return LbSelfUtil.selfLbzyError("未查询到该患者有效信息");
		}
		try {
			/**
			 * 获取当前可用票据
			 */
			//Map<String, Object> queryForMap = invPubForWsService.getCanUsedEmpinv("5", "自助机"+requ.getDeviceid(), user.getPkEmp(), "0", user.getPkOrg());

			String sql = "select inv.inv_prefix,inv.cur_no,cate.length,inv.cnt_use,inv.pk_empinv,inv.pk_invcate, cate.prefix from bl_emp_invoice inv "
					+ "inner join bd_invcate cate on inv.pk_invcate=cate.pk_invcate where inv.del_flag = '0' and inv.pk_org = ? "
					+ "and inv.flag_use ='1' and inv.flag_active ='1' and cate.eu_type = ? and inv.pk_emp_opera = ?";
			Map<String, Object> queryForMap = new HashMap<String, Object>();

			queryForMap = DataBaseHelper.queryForMap(sql, UserContext.getUser().getPkOrg(), Constant.HOSPREPAY, UserContext.getUser().getPkEmp());

			if(queryForMap!=null){
				String curNo = null;
				if(queryForMap.get("curNo") == null){
					return LbSelfUtil.selfLbzyError("当前存在可用票据，但是当前号为空！");
				}else{
					curNo = queryForMap.get("curNo").toString();
				}
				//票据号=票据分类前缀+票据前缀+号段组成
				String prefix = queryForMap.get("prefix") == null?"":queryForMap.get("prefix").toString(); //票据分类前缀
				String invPrefix = queryForMap.get("invPrefix") == null?"":queryForMap.get("invPrefix").toString();
				if(queryForMap.get("length") != null){
					long length = Long.parseLong(queryForMap.get("length").toString());
					curNo = invPubForWsService.flushLeft("0", length, curNo);
					String curCodeInv = prefix + invPrefix + curNo;
					queryForMap.put("curCodeInv", curCodeInv);
				}else{
					String curCodeInv = prefix + invPrefix + curNo;
					queryForMap.put("curCodeInv", curCodeInv);
				}
			}else{
				return LbSelfUtil.selfLbzyError("已无可用票据！，请联系管理员维护发票信息！");
			}
			Long cnt = (long)1;

			Map<String, Object> empinv = DataBaseHelper.queryForMap("select cnt_use,pk_emp_opera from bl_emp_invoice where del_flag = '0' and pk_empinv = ?", queryForMap.get("pkEmpinv").toString());
			Long cntUse = Long.parseLong(empinv.get("cntUse").toString());
			String pkEmpOpera = empinv.get("pkEmpOpera").toString();
			if (cntUse - cnt < 0) {
				return LbSelfUtil.selfLbzyError("更新后的票据可用张数为" + (cntUse - cnt) + "，小于0！发票不足！");
			}

			/**
			 * 住院预交金写表BL_DEPOSIT
			 */
			BlDeposit vo = new BlDeposit();
			if("7".equals(requItemVo.getPayTypeID())){
				vo.setDtPaymode("7");//支付方式7：微信，8：支付宝
			}else if("6".equals(requItemVo.getPayTypeID())){
				vo.setDtPaymode("8");//支付方式7：微信，8：支付宝
			}
			vo.setEuDptype(Constant.OTHERINV);//收付款类型9:住院预交金
			vo.setPkPi(MapUtils.getString(PatMap,"pkPi"));
			vo.setPkPv(MapUtils.getString(PatMap,"pkPv"));
			vo.setEuDirect("1");//收退方向
			vo.setAmount(BigDecimal.valueOf(Double.valueOf(requItemVo.getAmount())));
			if(("7").equals(vo.getDtPaymode()) || ("8").equals(vo.getDtPaymode())){
				if(StringUtils.isNotBlank(recip[1])){
					vo.setPayInfo(recip[1]);
				}
			}
			vo.setDatePay(new Date());
			if(null !=queryForMap){
				vo.setPkEmpinvoice(MapUtils.getString(queryForMap,"pkEmpinv"));
				vo.setReptNo(MapUtils.getString(queryForMap,"curCodeInv"));
			}
			vo.setPkEmpPay(UserContext.getUser().getPkEmp());
			vo.setNameEmpPay("自助机:"+requ.getUserID());

			//查询患者账户主键
			Map<String, Object> piAccMap = DataBaseHelper.queryForMap("select * from pi_acc where pk_pi= ?", LbSelfUtil.getPropValueStr(PatMap,"pkPi"));

			vo.setPkAcc(MapUtils.getString(piAccMap,"pkPiacc"));//患者账户主键
			vo.setFlagSettle("0");//结算标志
			vo.setFlagAcc("0");//账户支付标志
			vo.setNote("住院预交金");
			vo.setEuPvtype(MapUtils.getString(PatMap,"euPvtype"));

			vo.setPkDept(UserContext.getUser().getPkDept());
			vo.setPkOrg(UserContext.getUser().getPkOrg());
			vo.setCreator(UserContext.getUser().getPkEmp());
			vo.setCreateTime(new Date());
			vo.setDelFlag("0");
			vo.setTs(new Date());
			vo.setModifier(UserContext.getUser().getPkEmp());

			ResponseJson  result =  apputil.execService("BL", "BlPrePayService", "saveDeposit", vo,UserContext.getUser());
			if(result.getStatus()== Constant.SUC){
				BlDeposit bldVo = JsonUtil.readValue(JsonUtil.writeValueAsString(result.getData()), BlDeposit.class);
				if(("7").equals(requItemVo.getPayTypeID()) || ("8").equals(requItemVo.getPayTypeID())){
					//支付信息写入外部支付接口记录表bl_ext_pay
					BlExtPay extPay = new BlExtPay();
					extPay.setPkExtpay(NHISUUID.getKeyId());
					extPay.setPkOrg(UserContext.getUser().getPkOrg());
					extPay.setAmount(BigDecimal.valueOf(Double.valueOf(requItemVo.getAmount())));
					if("7".equals(requItemVo.getPayTypeID())){
						extPay.setEuPaytype("7");//支付方式7：微信，8：支付宝
					}else if("6".equals(requItemVo.getPayTypeID())){
						extPay.setEuPaytype("8");//支付方式7：微信，8：支付宝
					}
					extPay.setFlagPay("1");
					extPay.setSerialNo(recip[1]);//订单号
					extPay.setTradeNo(recip[2]);//交易流水号
					extPay.setPkPi(bldVo.getPkPi());
					extPay.setPkPv(bldVo.getPkPv());
					extPay.setDateRefund(null);
					extPay.setEuBill("0");
					extPay.setDateBill(null);
					extPay.setDescPay("自助机金额缴纳"+requItemVo.getAmount());
					extPay.setDatePay(new Date());
					extPay.setPkDepo(bldVo.getPkDepo());//BL_DEPOSIT主键
					extPay.setCreator(UserContext.getUser().getPkEmp());
					extPay.setCreateTime(new Date());
					extPay.setDelFlag("0");
					extPay.setTs(new Date());
					blPubForWsService.inserBlExtPay(extPay);
				}

				LbzyResponseVo response = new LbzyResponseVo();
				response.setResultCode(Constant.RESFAIL);
				response.setTranSerialNO(bldVo.getCodeDepo());//医院交易流水号	充值成功与否的凭证
				//查询住院预交金总额
				List<Map<String, Object>> blDepoList = DataBaseHelper.queryForList("SELECT * FROM BL_DEPOSIT WHERE EU_DIRECT=1 and PK_PV=?", bldVo.getPkPv());
				BigDecimal amtAcc = BigDecimal.ZERO;
				for(Map<String, Object> map : blDepoList){
					Double pric = Double.parseDouble(map.get("amount").toString());
					BigDecimal amt=BigDecimal.valueOf(pric);
					amtAcc=amtAcc.add(amt);
				}
				response.setInBalance(amtAcc.toString());//住院余额
				resXml = XmlUtil.beanToXml(response, LbzyResponseVo.class, false);
			}else{
				return LbSelfUtil.selfLbzyError(result.getDesc());
			}
		} catch (Exception e) {
			resXml = LbSelfUtil.selfLbzyError(e.getMessage());
		}
		return resXml;
	}
}
