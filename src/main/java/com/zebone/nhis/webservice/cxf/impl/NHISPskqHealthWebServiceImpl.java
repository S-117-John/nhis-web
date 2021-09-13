package com.zebone.nhis.webservice.cxf.impl;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;

import org.apache.commons.collections.MapUtils;
import org.apache.shiro.codec.Base64;
import org.apache.shiro.crypto.hash.SimpleHash;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import com.zebone.nhis.bl.pub.vo.ItemPriceVo;
import com.zebone.nhis.common.module.bl.BlDeposit;
import com.zebone.nhis.common.module.pv.PvEncounter;
import com.zebone.nhis.common.module.sch.appt.SchAppt;
import com.zebone.nhis.common.module.sch.plan.SchPlanWeekDt;
import com.zebone.nhis.common.module.sch.pub.SchSrv;
import com.zebone.nhis.common.support.ApplicationUtils;
import com.zebone.nhis.common.support.BeanUtils;
import com.zebone.nhis.common.support.CommonUtils;
import com.zebone.nhis.common.support.DateUtils;
import com.zebone.nhis.common.support.HttpClientUtil;
import com.zebone.nhis.common.support.XmlUtil;
import com.zebone.nhis.ma.pub.sd.vo.EnoteReqVo;
import com.zebone.nhis.ma.pub.sd.vo.EnoteResDataVo;
import com.zebone.nhis.pv.reg.service.RegSyxService;
import com.zebone.nhis.sch.plan.vo.SchPlanWeekDtVo;
import com.zebone.nhis.webservice.cxf.INHISPskqHealthWebService;
import com.zebone.nhis.webservice.dao.BdPubForWsMapper;
import com.zebone.nhis.webservice.dao.SchPubForWsMapper;
import com.zebone.nhis.webservice.pskq.config.SpringContextHolder;
import com.zebone.nhis.webservice.pskq.dao.ExLabOccMessageMapper;
import com.zebone.nhis.webservice.pskq.dao.PskqSchPubForWsMapper;
import com.zebone.nhis.webservice.service.BdPubForWsService;
import com.zebone.nhis.webservice.service.BlPubForWsService;
import com.zebone.nhis.webservice.service.CnPubForWsService;
import com.zebone.nhis.webservice.service.ExPubForWsService;
import com.zebone.nhis.webservice.service.InvPubForWsService;
import com.zebone.nhis.webservice.service.LbPubForWsService;
import com.zebone.nhis.webservice.service.PskqPubForWsService;
import com.zebone.nhis.webservice.service.PvPubForWsService;
import com.zebone.nhis.webservice.service.SchPubForWsService;
import com.zebone.nhis.webservice.support.Constant;
import com.zebone.nhis.webservice.support.LbSelfUtil;
import com.zebone.nhis.webservice.support.OtherRespJson;
import com.zebone.nhis.webservice.support.PskqSelfUtil;
import com.zebone.nhis.webservice.vo.EnoteInvInfo;
import com.zebone.nhis.webservice.vo.LbPiMasterRegVo;
import com.zebone.nhis.webservice.vo.wechatvo.HisMerchantDetailResVO;
import com.zebone.nhis.webservice.vo.wechatvo.HisMerchantSumResVO;
import com.zebone.nhis.webservice.vo.wechatvo.ItemsVO;
import com.zebone.nhis.webservice.vo.wechatvo.SumItemsVO;
import com.zebone.nhis.webservice.vo.wechatvo.WebChatReqVO;
import com.zebone.platform.common.support.NHISUUID;
import com.zebone.platform.common.support.User;
import com.zebone.platform.common.support.UserContext;
import com.zebone.platform.modules.dao.DataBaseHelper;
import com.zebone.platform.modules.exception.BusException;
import com.zebone.platform.modules.utils.JsonUtil;
import com.zebone.platform.web.support.ResponseJson;

/**
 *深圳坪山口腔医院-对外健康网公共webservice接口服务实现-json
 * @author zhangtao
 *
 */
@WebService
@SOAPBinding(style = SOAPBinding.Style.RPC)
public class NHISPskqHealthWebServiceImpl implements INHISPskqHealthWebService {

	// 用来控制手动事物
	@Resource(name = "transactionManager")
	private PlatformTransactionManager platformTransactionManager;
	
	@Resource
	private PvPubForWsService pvPubForWsService;
	@Resource
	private ExPubForWsService exPubForWsService;
	@Resource
	private BlPubForWsService blPubForWsService;
	@Resource
	private CnPubForWsService cnPubForWsService;
	@Resource
	private InvPubForWsService invPubForWsService;
	@Resource
	private SchPubForWsService schPubForWsService;
	@Resource
    private BdPubForWsService bdPubForWsService;
	@Resource
	private BdPubForWsMapper bdPubForWsMapper;
	@Resource
	private SchPubForWsMapper schPubForWsMapper;
	@Resource
	private LbPubForWsService lbPubForWsService;
	@Resource
	private PskqPubForWsService pskqPubForWsService;

	@Resource
	private ExLabOccMessageMapper exLabOccMessageMapper;
	
	@Resource
	private PskqSchPubForWsMapper pskqSchPubForWsMapper;
	private Logger logger = LoggerFactory.getLogger("nhis.EHealthLog");

	SimpleDateFormat sd = new SimpleDateFormat("yyyyMMdd");
	ApplicationUtils apputil = new ApplicationUtils();

	/**
	 * 查询患者信息
	 *
	 * @param param
	 * @return
	 */
	@Override
	@SuppressWarnings("unchecked")
	public String getPiMasterInfo(String param) {
		logger.info("查询患者信息接口:请求参数\n"+param);
		OtherRespJson respJson = new OtherRespJson();
		try {
 			Map<String,Object> paramMap = JsonUtil.readValue(param, Map.class);
			Map<String,Object> result = new HashMap<String,Object>();	
			if(paramMap == null){
				respJson = resErrorReturn(respJson,"参数不能为空！");
				String resp = respJson.toString();
				logger.info("查询患者信息{}",resp);
				return resp;
			}	
			if(CommonUtils.isEmptyString(CommonUtils.getPropValueStr(paramMap,"codePi"))
					&& CommonUtils.isEmptyString(CommonUtils.getPropValueStr(paramMap,"idNo"))
					&& CommonUtils.isEmptyString(CommonUtils.getPropValueStr(paramMap,"hicNo"))
					&& CommonUtils.isEmptyString(CommonUtils.getPropValueStr(paramMap,"codeIp"))
					&& CommonUtils.isEmptyString(CommonUtils.getPropValueStr(paramMap,"codeCard"))){
				respJson = resErrorReturn(respJson,"【codePi】【idNo】【hicNo】【codeIp】【codeCard】至少有一个参数不能为空！");
				String resp = respJson.toString();
				logger.info("查询患者信息{}",resp);
				return resp;
			}
			paramMap.put("idno",CommonUtils.getPropValueStr(paramMap,"idNo"));
			result = pvPubForWsService.getPiMasterInfo(paramMap);
			respJson = resSuccReturn(respJson,JsonUtil.writeValueAsString(result,"yyyy-MM-dd HH:mm:ss"),1);
		} catch (Exception e) {
			respJson = resErrorReturn(respJson,e.getMessage());
		}
		logger.info("查询患者信息接口:响应参数\n"+respJson.toString());
		return respJson.toString();
	}

	/**
	 * 查询患者住院记录
	 *
	 * @param param
	 * @return
	 */
	@Override
	public String getPvInfoByIp(String param) {
		logger.info("查询患者住院记录:请求参数\n"+param);
		OtherRespJson respJson = new OtherRespJson();
		try {
 			Map<String,Object> paramMap = JsonUtil.readValue(param, Map.class);
 			List<Map<String,Object>> result= new ArrayList<Map<String,Object>>();
			if(paramMap == null){
				respJson = resErrorReturn(respJson,"参数不能为空！");
				String resp = respJson.toString();
				logger.info("查询患者住院记录{}",resp);
				return resp;
			}	
			if(CommonUtils.isEmptyString(CommonUtils.getPropValueStr(paramMap, "codePi"))){
				respJson = resErrorReturn(respJson,"【codePi】患者编号不能为空！");
				String resp = respJson.toString();
				logger.info("查询患者住院记录{}",resp);
				return resp;
			}
			result.addAll(pvPubForWsService.getPvInfoByIp(paramMap));
			respJson = resSuccReturn(respJson,JsonUtil.writeValueAsString(result,"yyyy-MM-dd HH:mm:ss"),result.size());
		} catch (Exception e) {
			respJson = resErrorReturn(respJson,e.getMessage());
		}
		logger.info("查询患者住院记录:响应参数\n"+respJson.toString());
		return respJson.toString();
	}

	/**
	 * 住院预交金充值
	 *
	 * @param param
	 * @return
	 */
	@Override
	public String prePayRecharge(String param) {
		logger.info("住院预交金充值:请求参数\n"+param);
		OtherRespJson respJson = new OtherRespJson();
		Map<String,Object> result = new HashMap<String,Object>(16);
		BlDeposit vo = new BlDeposit();
		try {
			Map<String,Object> paramMap = JsonUtil.readValue(param, Map.class);
			if(paramMap == null){
				respJson = resErrorReturn(respJson,"参数不能为空！");
				String resp = respJson.toString();
				logger.info("住院预交金充值{}",resp);
				return resp;
			}	
			if(CommonUtils.isEmptyString(CommonUtils.getPropValueStr(paramMap, "codeIp"))){
				respJson = resErrorReturn(respJson,"【codeIp】住院号不能为空！");
				String resp = respJson.toString();
				logger.info("住院预交金充值{}",resp);
				return resp;
			}
			if(CommonUtils.isEmptyString(CommonUtils.getPropValueStr(paramMap, "payType"))){
				respJson = resErrorReturn(respJson,"【payType】支付方式不能为空！");
				String resp = respJson.toString();
				logger.info("住院预交金充值{}",resp);
				return resp;
			}
			if(CommonUtils.isEmptyString(CommonUtils.getPropValueStr(paramMap, "payAmt"))){
				respJson = resErrorReturn(respJson,"【payAmt】充值金额不能为空！");
				String resp = respJson.toString();
				logger.info("住院预交金充值{}",resp);
				return resp;
			}
			if(CommonUtils.isEmptyString(CommonUtils.getPropValueStr(paramMap, "orderno"))){
				respJson = resErrorReturn(respJson,"【orderno】订单号不能为空！");
				String resp = respJson.toString();
				logger.info("住院预交金充值{}",resp);
				return resp;
			}
			if(CommonUtils.isEmptyString(CommonUtils.getPropValueStr(paramMap, "paytime"))){
				respJson = resErrorReturn(respJson,"【paytime】订单号不能为空！");
				String resp = respJson.toString();
				logger.info("住院预交金充值{}",resp);
				return resp;
			}
			if(CommonUtils.isEmptyString(CommonUtils.getPropValueStr(paramMap, "flowno"))){
				respJson = resErrorReturn(respJson,"【flowno】订单流水号不能为空！");
				String resp = respJson.toString();
				logger.info("住院预交金充值{}",resp);
				return resp;
			}
			if(CommonUtils.isEmptyString(CommonUtils.getPropValueStr(paramMap, "sysname"))){
				respJson = resErrorReturn(respJson,"【sysname】渠道商名称不能为空！");
				String resp = respJson.toString();
				logger.info("住院预交金充值{}",resp);
				return resp;
			}
			if(CommonUtils.isEmptyString(CommonUtils.getPropValueStr(paramMap, "deviceid"))){
				respJson = resErrorReturn(respJson,"【deviceid】操作员工号不能为空！");
				String resp = respJson.toString();
				logger.info("住院预交金充值{}",resp);
				return resp;
			}
			
			//查询患者就诊信息
			Map<String, Object> PatMap = DataBaseHelper.queryForMap("select * from pv_encounter pv left join pi_master pi on pv.pk_pi=pi.pk_pi and pi.del_flag='0' where pv.eu_pvtype= '3' and (pv.eu_status = '0' or pv.eu_status = '1') and pi.code_ip= ?", paramMap.get("codeIp").toString());
			if(!BeanUtils.isNotNull(PatMap)){
				respJson = resErrorReturn(respJson,"未查询到该患者有效信息，请先注册患者！");
				String resp = respJson.toString();
				logger.info("住院预交金充值{}",resp);
				return resp;
			}
			User user = PskqSelfUtil.getDefaultUser(CommonUtils.getPropValueStr(paramMap, "deviceid"));
			if(!BeanUtils.isNotNull(user)){
				respJson = resErrorReturn(respJson,"【deviceid】操作员工号未注册，请先联系his管理员注册！");
				String resp = respJson.toString();
				logger.info("住院预交金充值{}",resp);
				return resp;
			}
			UserContext userContext = new UserContext();
			userContext.setUser(user);

			/**
			 * 获取当前可用票据
			 */
			String sql = "select inv.inv_prefix,inv.cur_no,cate.length,inv.cnt_use,inv.pk_empinv,inv.pk_invcate, cate.prefix from bl_emp_invoice inv "
					+ "inner join bd_invcate cate on inv.pk_invcate=cate.pk_invcate where inv.del_flag = '0' and inv.pk_org = ? "
					+ "and inv.flag_use ='1' and inv.flag_active ='1' and cate.eu_type = ? and inv.pk_emp_opera = ?";
			Map<String, Object> queryForMap = new HashMap<String, Object>();

			queryForMap = DataBaseHelper.queryForMap(sql, user.getPkOrg(), Constant.HOSPREPAY, user.getPkEmp());

			if(queryForMap!=null){
				String curNo = null;
				if(queryForMap.get("curNo") == null){
					respJson = resErrorReturn(respJson,"当前存在可用票据，但是当前号为空！");
					String resp = respJson.toString();
					logger.info("住院预交金充值{}",resp);
					return resp;
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
				respJson = resErrorReturn(respJson,"已无可用票据！，请联系管理员维护发票信息！");
				String resp = respJson.toString();
				logger.info("住院预交金充值{}",resp);
				return resp;
			}
			Long cnt = (long)1;

			Map<String, Object> empinv = DataBaseHelper.queryForMap("select cnt_use,pk_emp_opera from bl_emp_invoice where del_flag = '0' and pk_empinv = ?", queryForMap.get("pkEmpinv").toString());
			Long cntUse = Long.parseLong(empinv.get("cntUse").toString());
			if (cntUse - cnt < 0) {
				respJson = resErrorReturn(respJson,"更新后的票据可用张数为\" + (cntUse - cnt) + \"，小于0！发票不足！");
				String resp = respJson.toString();
				logger.info("住院预交金充值{}",resp);
				return resp;
			}

			/**
    		 * 住院预交金写表BL_DEPOSIT
    		 */
			vo = blPubForWsService.PskqHealthInsertBlDeposit(Constant.OTHERINV,vo,queryForMap,paramMap,PatMap,user);
			ResponseJson rs = apputil.execService("BL", "BlPrePayService", "saveDeposit", vo,user);
 			if(rs.getStatus()== Constant.SUC){
 				BlDeposit bldVo = JsonUtil.readValue(JsonUtil.writeValueAsString(rs.getData()), BlDeposit.class);
				/**
    			 * 支付信息写入外部支付接口记录表bl_ext_pay
    			 */
         		blPubForWsService.PskqHealthPayment(bldVo,paramMap,PatMap,user,null);
         		result.put("pkDepo", bldVo.getPkDepo());
         		result.put("pkSettle", bldVo.getPkSettle());
         		result.put("codeDepo", bldVo.getCodeDepo());
         		result.put("reptNo", bldVo.getReptNo());
		        Map<String, Object> amtMap = DataBaseHelper.queryForMap("SELECT sum(amount) as amtAcc  FROM BL_DEPOSIT WHERE eu_dptype = '9' and PK_PV=?", bldVo.getPkPv());
		        BigDecimal amtAcc = BigDecimal.ZERO;
		        if(amtMap != null) {
		        	amtAcc = amtAcc.add(new BigDecimal(CommonUtils.getPropValueStr(amtMap, "amtAcc")));
		        }
		        result.put("balance", amtAcc);

 			}else{
				respJson = resErrorReturn(respJson,rs.getDesc());
				String resp = respJson.toString();
				logger.info("住院预交金充值{}",resp);
				return resp;
 			}
 			respJson = resSuccReturn(respJson,JsonUtil.writeValueAsString(result,"yyyy-MM-dd HH:mm:ss"),1);
		} catch (Exception e) {
			respJson = resErrorReturn(respJson,e.getMessage());
		}
		logger.info("住院预交金充值:响应参数\n"+respJson.toString());
		return respJson.toString();
	}

	/**
	 * 查询住院预交金充值记录
	 *
	 * @param param
	 * @return
	 */
	@Override
	public String getPrePayDetail(String param) {
		logger.info("查询住院预交金充值记录:请求参数\n"+param);
		OtherRespJson respJson = new OtherRespJson();
		try {
 			Map<String,Object> paramMap = JsonUtil.readValue(param, Map.class);
 			List<Map<String,Object>> result= new ArrayList<Map<String,Object>>();
			if(paramMap == null){
				respJson = resErrorReturn(respJson,"参数不能为空！");
				String resp = respJson.toString();
				logger.info("查询住院预交金充值记录{}",resp);
				return resp;
			}	
			if(CommonUtils.isEmptyString(CommonUtils.getPropValueStr(paramMap, "codePi"))){
				respJson = resErrorReturn(respJson,"【codePi】患者编号不能为空！");
				String resp = respJson.toString();
				logger.info("查询住院预交金充值记录{}",resp);
				return resp;
			}
			if(CommonUtils.isEmptyString(CommonUtils.getPropValueStr(paramMap, "orderno"))){
				respJson = resErrorReturn(respJson,"【orderno】订单号不能为空！");
				String resp = respJson.toString();
				logger.info("查询住院预交金充值记录{}",resp);
				return resp;
			}
			result.addAll(blPubForWsService.getPrePayDetail(paramMap));
			respJson = resSuccReturn(respJson,JsonUtil.writeValueAsString(result,"yyyy-MM-dd HH:mm:ss"),result.size());
		} catch (Exception e) {
			respJson = resErrorReturn(respJson,e.getMessage());
		}
		logger.info("查询住院预交金充值记录:响应参数\n"+respJson.toString());
		return respJson.toString();
	}

	/**
	 * 住院总费用查询
	 *
	 * @param param
	 * @return
	 */
	@Override
	public String getIpCgDetail(String param) {
		logger.info("查询住院总费用:请求参数\n"+param);
		OtherRespJson respJson = new OtherRespJson();
		try {
 			Map<String,Object> paramMap = JsonUtil.readValue(param, Map.class);
 			List<Map<String,Object>> result= new ArrayList<Map<String,Object>>();
 			if(paramMap == null){
				respJson = resErrorReturn(respJson,"参数不能为空！");
				String resp = respJson.toString();
				logger.info("查询住院总费用{}",resp);
				return resp;
			}	
			if(CommonUtils.isEmptyString(CommonUtils.getPropValueStr(paramMap, "codePi"))){
				respJson = resErrorReturn(respJson,"【codePi】患者编号不能为空！");
				String resp = respJson.toString();
				logger.info("查询住院总费用{}",resp);
				return resp;
			}
			result.addAll(blPubForWsService.getIpCgDetail(paramMap));
			respJson = resSuccReturn(respJson,JsonUtil.writeValueAsString(result,"yyyy-MM-dd HH:mm:ss"),result.size());
		} catch (Exception e) {
			respJson = resErrorReturn(respJson,e.getMessage());
		}
		logger.info("查询住院总费用:响应参数\n"+respJson.toString());
		return respJson.toString();
	}

	/**
	 * 住院日费用类别查询
	 *
	 * @param param
	 * @return
	 */
	@Override
	public String getIpCgDay(String param) {
		logger.info("查询住院日费用类别:请求参数\n"+param);
		OtherRespJson respJson = new OtherRespJson();
		try {
 			Map<String,Object> paramMap = JsonUtil.readValue(param, Map.class);
 			List<Map<String,Object>> result= new ArrayList<Map<String,Object>>();
 			if(paramMap == null){
				respJson = resErrorReturn(respJson,"参数不能为空！");
				String resp = respJson.toString();
				logger.info("查询住院日费用类别{}",resp);
				return resp;
			}	
			if(CommonUtils.isEmptyString(CommonUtils.getPropValueStr(paramMap, "codePi"))){
				respJson = resErrorReturn(respJson,"【codePi】患者编号不能为空！");
				String resp = respJson.toString();
				logger.info("查询住院日费用类别{}",resp);
				return resp;
			}
			if(CommonUtils.isEmptyString(CommonUtils.getPropValueStr(paramMap, "dateCg"))){
				respJson = resErrorReturn(respJson,"【dateCg】费用日期不能为空！");
				String resp = respJson.toString();
				logger.info("查询住院日费用类别{}",resp);
				return resp;
			}
			//传递的是 yyyy-MM-dd 格式 ： 转换为 yyyyMMddHHmmss 格式
			String dateCg = CommonUtils.getString(paramMap.get("dateCg"));
			paramMap.put("dateCg", dateCg.replaceAll("-","")+"000000");
			result.addAll(blPubForWsService.getIpCgDayDetail(paramMap));
			respJson = resSuccReturn(respJson,JsonUtil.writeValueAsString(result,"yyyy-MM-dd HH:mm:ss"),result.size());
		} catch (Exception e) {
			respJson = resErrorReturn(respJson,e.getMessage());
		}
		logger.info("查询住院日费用类别:响应参数\n"+respJson.toString());
		return respJson.toString();
	}

	/**
	 * 住院日费用明细查询
	 *
	 * @param param
	 * @return
	 */
	@Override
	public String getIpCgDayDetail(String param) {
		logger.info("查询住院日费用明细:请求参数\n"+param);
		OtherRespJson respJson = new OtherRespJson();
		try {
 			Map<String,Object> paramMap = JsonUtil.readValue(param, Map.class);
 			List<Map<String,Object>> result= new ArrayList<Map<String,Object>>();
			if(paramMap == null){
				respJson = resErrorReturn(respJson,"参数不能为空！");
				String resp = respJson.toString();
				logger.info("查询住院日费用明细{}",resp);
				return resp;
			}	
			if(CommonUtils.isEmptyString(CommonUtils.getPropValueStr(paramMap, "codePi"))){
				respJson = resErrorReturn(respJson,"【codePi】患者编号不能为空！");
				String resp = respJson.toString();
				logger.info("查询住院日费用明细{}",resp);
				return resp;
			}
			if(CommonUtils.isEmptyString(CommonUtils.getPropValueStr(paramMap, "dateCg"))){
				respJson = resErrorReturn(respJson,"【dateCg】费用日期不能为空！");
				String resp = respJson.toString();
				logger.info("查询住院日费用明细{}",resp);
				return resp;
			}
			if(CommonUtils.isEmptyString(CommonUtils.getPropValueStr(paramMap, "catecode"))){
				respJson = resErrorReturn(respJson,"【catecode】费用分类不能为空！");
				String resp = respJson.toString();
				logger.info("查询住院日费用明细{}",resp);
				return resp;
			}
			//传递的是 yyyy-MM-dd 格式 ： 转换为 yyyyMMddHHmmss 格式
			String dateCg = CommonUtils.getString(paramMap.get("dateCg"));
			paramMap.put("dateCg", dateCg.replaceAll("-","")+"000000");
			result.addAll(blPubForWsService.getIpCgDayDetail(paramMap));
			respJson = resSuccReturn(respJson,JsonUtil.writeValueAsString(result,"yyyy-MM-dd HH:mm:ss"),result.size());
		} catch (Exception e) {
			respJson = resErrorReturn(respJson,e.getMessage());
		}
		logger.info("查询住院日费用明细:响应参数\n"+respJson.toString());
		return respJson.toString();
	}

	/**
	 * 保存患者信息
	 * @param
	 * @return
	 */
	@Override
	public String savePiMaster(String param) {
		logger.info("保存患者信息:请求参数\n"+param);
		OtherRespJson respJson = new OtherRespJson();
		try {
			Map<String,Object> result = new HashMap<String,Object>(16);
			Map<String, Object> paramMap = JsonUtil.readValue(param,Map.class);
			if(paramMap == null){
				respJson = resErrorReturn(respJson,"参数不能为空！");
				String resp = respJson.toString();
				logger.info("保存患者信息{}",resp);
				return resp;
			}	
			if(CommonUtils.isEmptyString(CommonUtils.getPropValueStr(paramMap, "idNo"))){
				respJson = resErrorReturn(respJson,"【idNo】证件号不能为空！");
				String resp = respJson.toString();
				logger.info("保存患者信息{}",resp);
				return resp;
			}
			if(CommonUtils.isEmptyString(CommonUtils.getPropValueStr(paramMap, "namePi"))){
				respJson = resErrorReturn(respJson,"【namePi】患者姓名不能为空！");
				String resp = respJson.toString();
				logger.info("保存患者信息{}",resp);
				return resp;
			}
			if(CommonUtils.isEmptyString(CommonUtils.getPropValueStr(paramMap, "dtSex"))){
				respJson = resErrorReturn(respJson,"【dtSex】性别不能为空！");
				String resp = respJson.toString();
				logger.info("保存患者信息{}",resp);
				return resp;
			}
			if(CommonUtils.isEmptyString(CommonUtils.getPropValueStr(paramMap, "mobile"))){
				respJson = resErrorReturn(respJson,"【mobile】手机号不能为空！");
				String resp = respJson.toString();
				logger.info("保存患者信息{}",resp);
				return resp;
			}
			if(CommonUtils.isEmptyString(CommonUtils.getPropValueStr(paramMap, "dtIdType"))){
				respJson = resErrorReturn(respJson,"【dtIdType】证件类型不能为空！");
				String resp = respJson.toString();
				logger.info("保存患者信息{}",resp);
				return resp;
			}
			if(CommonUtils.isEmptyString(CommonUtils.getPropValueStr(paramMap, "birthDate"))){
				respJson = resErrorReturn(respJson,"【birthDate】出生日期不能为空！");
				String resp = respJson.toString();
				logger.info("保存患者信息{}",resp);
				return resp;
			}
			if(CommonUtils.isEmptyString(CommonUtils.getPropValueStr(paramMap, "deviceid"))){
				respJson = resErrorReturn(respJson,"【deviceid】操作员工号不能为空！");
				String resp = respJson.toString();
				logger.info("保存患者信息{}",resp);
				return resp;
			}
			User user = PskqSelfUtil.getDefaultUser(CommonUtils.getPropValueStr(paramMap, "deviceid"));
			if(!BeanUtils.isNotNull(user)){
				respJson = resErrorReturn(respJson,"【deviceid】操作员工号未注册，请先联系his管理员注册！");
				String resp = respJson.toString();
				logger.info("保存患者信息{}",resp);
				return resp;
			}
			UserContext.setUser(user);

			result = pvPubForWsService.piMasterRegister(paramMap,user);
			if(result == null){
				respJson.setStatus(Constant.UNUSUAL);
				respJson.setDesc("失败");
				respJson.setErrorMessage("保存失败，请联系his管理员");
				String resp = respJson.toString();
				logger.info("保存患者信息{}",resp);
				return resp;
			}
			if("false".equals(result.get("result"))){
				respJson.setStatus(Constant.UNUSUAL);
				respJson.setDesc("失败");
				respJson.setErrorMessage(CommonUtils.getString(result.get("message")));
				String resp = respJson.toString();
				logger.info("保存患者信息{}",resp);
				return resp;
			}
			respJson = resSuccReturn(respJson,JsonUtil.writeValueAsString(result,"yyyy-MM-dd HH:mm:ss"),1);
		} catch (Exception e) {
			respJson = resErrorReturn(respJson,e.getMessage());
		}
		logger.info("保存患者信息:响应参数\n"+respJson.toString());
		return respJson.toString();
	}
	
	/**
	* 查询医院信息
	* @author zhangtao
	* @return
	*/
	@Override
	public String getOrgList() {
		logger.info("查询医院信息");
		List<Map<String,Object>> result = new ArrayList<Map<String,Object>>();
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("flagActive", "1");
		paramMap.put("delFlag", "0");
		paramMap.put("pkFather", "~                               ");
		OtherRespJson respJson = new OtherRespJson();
		try {
			result.addAll(bdPubForWsService.getOrgList(paramMap));
			respJson = resSuccReturn(respJson,JsonUtil.writeValueAsString(result,"yyyy-MM-dd HH:mm:ss"),result.size());
		} catch (Exception e) {
			respJson = resErrorReturn(respJson,e.getMessage());
		}
		logger.info("查询医院信息:响应参数\n"+respJson.toString());
		return respJson.toString();
	}

	/**
	* 查询科室信息
	* @author zhangtao
	* @param : deviceid:操作员编码
	* @return
	*/
	@Override
	public String getDeptInfo(String param) {
		logger.info("查询科室信息:请求参数\n"+param);
		OtherRespJson respJson = new OtherRespJson();
		try {
			List<Map<String,Object>> result  = new ArrayList<Map<String,Object>>();
			Map<String, Object> paramMap = JsonUtil.readValue(param,Map.class);
			if(paramMap == null){
				respJson = resErrorReturn(respJson,"参数不能为空！");
				String resp = respJson.toString();
				logger.info("查询科室信息{}",resp);
				return resp;
			}	
			if(CommonUtils.isEmptyString(CommonUtils.getPropValueStr(paramMap, "deviceid"))){
				respJson = resErrorReturn(respJson,"【deviceid】操作员工号不能为空！");
				String resp = respJson.toString();
				logger.info("查询科室信息{}",resp);
				return resp;
			}
			User user = PskqSelfUtil.getDefaultUser(CommonUtils.getPropValueStr(paramMap, "deviceid"));
			if(!BeanUtils.isNotNull(user)){
				respJson = resErrorReturn(respJson,"【deviceid】操作员工号未注册，请先联系his管理员注册！");
				String resp = respJson.toString();
				logger.info("查询科室信息{}",resp);
				return resp;
			}
			paramMap.put("pkOrg", user.getPkOrg());
			result.addAll(bdPubForWsService.getDeptInfo(paramMap));
			respJson = resSuccReturn(respJson,JsonUtil.writeValueAsString(result,"yyyy-MM-dd HH:mm:ss"),result.size());
		} catch (Exception e) {
			respJson = resErrorReturn(respJson,e.getMessage());
		}
		return respJson.toString();
	}

	/**
	* 查询医生信息
	* @author zhangtao
	* @param : deviceid：操作员编码
	* @return
	*/
	@Override
	public String getEmpInfo(String param) {
		logger.info("查询医生信息:请求参数\n"+param);
		OtherRespJson respJson = new OtherRespJson();
		try {
			List<Map<String,Object>> result = new ArrayList<Map<String,Object>>();
			Map<String, Object> paramMap = JsonUtil.readValue(param,Map.class);
			if(paramMap == null){
				respJson = resErrorReturn(respJson,"参数不能为空！");
				String resp = respJson.toString();
				logger.info("查询医生信息{}",resp);
				return resp;
			}	
			if(CommonUtils.isEmptyString(CommonUtils.getPropValueStr(paramMap, "deviceid"))){
				respJson = resErrorReturn(respJson,"【deviceid】操作员工号不能为空！");
				String resp = respJson.toString();
				logger.info("查询医生信息{}",resp);
				return resp;
			}
			User user = PskqSelfUtil.getDefaultUser(CommonUtils.getPropValueStr(paramMap, "deviceid"));
			if(!BeanUtils.isNotNull(user)){
				respJson = resErrorReturn(respJson,"【deviceid】操作员工号未注册，请先联系his管理员注册！");
				String resp = respJson.toString();
				logger.info("查询医生信息{}",resp);
				return resp;
			}
			paramMap.put("pkOrg", user.getPkOrg());
			result.addAll(bdPubForWsService.getEmpInfo(paramMap));
			for (Map<String, Object> map : result) {
				map.remove("photo");
			}
			respJson = resSuccReturn(respJson,JsonUtil.writeValueAsString(result,"yyyy-MM-dd HH:mm:ss"),result.size());
			} catch (Exception e) {
				respJson = resErrorReturn(respJson,e.getMessage());
			}
		return respJson.toString();
	}
	
	/**
	 * 查询排班科室信息
	 * @param param{deviceid：必传}
	 * @return
	 */
	@Override
	public String getSchDept(String param) {	
		logger.info("查询排班科室信息:请求参数\n"+param);
		OtherRespJson respJson = new OtherRespJson();
		try {
			List<Map<String,Object>> result = new ArrayList<Map<String,Object>>();
			Map<String, Object> paramMap = JsonUtil.readValue(param,Map.class);
			if(paramMap == null){
				respJson = resErrorReturn(respJson,"参数不能为空！");
				String resp = respJson.toString();
				logger.info("查询排班科室信息{}",resp);
				return resp;
			}	
			if(CommonUtils.isEmptyString(CommonUtils.getPropValueStr(paramMap, "deviceid"))){
				respJson = resErrorReturn(respJson,"【deviceid】操作员工号不能为空！");
				String resp = respJson.toString();
				logger.info("查询排班科室信息{}",resp);
				return resp;
			}
			User user = PskqSelfUtil.getDefaultUser(CommonUtils.getPropValueStr(paramMap, "deviceid"));
			if(!BeanUtils.isNotNull(user)){
				respJson = resErrorReturn(respJson,"【deviceid】操作员工号未注册，请先联系his管理员注册！");
				String resp = respJson.toString();
				logger.info("查询排班科室信息{}",resp);
				return resp;
			}
			paramMap.put("pkOrg", user.getPkOrg());
			result.addAll(schPubForWsMapper.getSchDeptInfo(paramMap));
			respJson = resSuccReturn(respJson,JsonUtil.writeValueAsString(result,"yyyy-MM-dd HH:mm:ss"),result.size());
		} catch (Exception e) {
			respJson = resErrorReturn(respJson,e.getMessage());
		}
		logger.info("查询排班科室信息:响应参数\n"+respJson.toString());
		return respJson.toString();
	}

	/**
	 * 查询排班信息
	 * @param {deviceid,scheduleType}
	 * @return
	 */
	@Override
	public String getSchInfo(String param) {
		logger.info("查询排班信息:请求参数\n"+param);
		OtherRespJson respJson = new OtherRespJson();
		try {
			List<Map<String,Object>> result = new ArrayList<Map<String,Object>>();
			Map<String, Object> paramMap = JsonUtil.readValue(param,Map.class);
			if(paramMap == null){
				respJson = resErrorReturn(respJson,"参数不能为空！");
				String resp = respJson.toString();
				logger.info("查询排班信息{}",resp);
				return resp;
			}	
			if(CommonUtils.isEmptyString(CommonUtils.getPropValueStr(paramMap, "deviceid"))){
				respJson = resErrorReturn(respJson,"【deviceid】操作员工号不能为空！");
				String resp = respJson.toString();
				logger.info("查询排班信息{}",resp);
				return resp;
			}
			User user = PskqSelfUtil.getDefaultUser(CommonUtils.getPropValueStr(paramMap, "deviceid"));
			if(!BeanUtils.isNotNull(user)){
				respJson = resErrorReturn(respJson,"【deviceid】操作员工号未注册，请先联系his管理员注册！");
				String resp = respJson.toString();
				logger.info("查询排班信息{}",resp);
				return resp;
			}
			paramMap.put("pkOrg", user.getPkOrg());
			DateTime dt = DateTime.now();
			//1为预约挂号  99-当日挂号 （当日挂号只查询当天的排班信息）
			if("1".equals(CommonUtils.getPropValueStr(paramMap, "scheduleType"))){
				paramMap.put("total","0");
				paramMap.put("StartDate", CommonUtils.getPropValueStr(paramMap, "dateBegin"));//开始时间
				paramMap.put("EndDate", CommonUtils.getPropValueStr(paramMap, "dateEnd"));//结束时间
			}else{
				paramMap.put("nowDate", dt.toString("YYYY-MM-dd"));// 当天的日期
			}
			paramMap.put("flagStop", "0");//停用标志
			result.addAll(schPubForWsMapper.LbTodaySchInfosByDate(paramMap));
			respJson = resSuccReturn(respJson,JsonUtil.writeValueAsString(result,"yyyy-MM-dd HH:mm:ss"),result.size());
		} catch (Exception e) {
			respJson = resErrorReturn(respJson,e.getMessage());
		}
		logger.info("查询排班信息:响应参数\n"+respJson.toString());
		return respJson.toString();
	}


	/**
	 * 查询号源信息
	 * @param param{pkSch,scheduleType}必传
	 * @return
	 */
	@Override
	public String getTickets(String param) {
		logger.info("查询号源信息:请求参数\n"+param);
		OtherRespJson respJson = new OtherRespJson();
		try {
        	List<Map<String, Object>> result = new ArrayList<>();
			Map<String, Object> paramMap = JsonUtil.readValue(param,Map.class);
			if(paramMap == null){
				respJson = resErrorReturn(respJson,"参数不能为空！");
				String resp = respJson.toString();
				logger.info("查询号源信息{}",resp);
				return resp;
			}	
			if(CommonUtils.isEmptyString(CommonUtils.getPropValueStr(paramMap, "pkSch"))){
				respJson = resErrorReturn(respJson,"【pkSch】排班主键不能为空！");
				String resp = respJson.toString();
				logger.info("查询号源信息{}",resp);
				return resp;
			}
			if(CommonUtils.isEmptyString(CommonUtils.getPropValueStr(paramMap, "scheduleType"))){
				respJson = resErrorReturn(respJson,"【scheduleType】排班类型不能为空！");
				String resp = respJson.toString();
				logger.info("查询号源信息{}",resp);
				return resp;
			}
			if(CommonUtils.isEmptyString(CommonUtils.getPropValueStr(paramMap, "deviceid"))){
				respJson = resErrorReturn(respJson,"【deviceid】操作员工号不能为空！");
				String resp = respJson.toString();
				logger.info("查询号源信息{}",resp);
				return resp;
			}
			User user = PskqSelfUtil.getDefaultUser(CommonUtils.getPropValueStr(paramMap, "deviceid"));
			if(!BeanUtils.isNotNull(user)){
				respJson = resErrorReturn(respJson,"【deviceid】操作员工号未注册，请先联系his管理员注册！");
				String resp = respJson.toString();
				logger.info("查询号源信息{}",resp);
				return resp;
			}
			UserContext.setUser(user);
			//是否启用预约渠道控制
			String ifFilter = ApplicationUtils.getSysparam("SCH0016", false);
			
			DateTime dt = DateTime.now();
			boolean timDouble = true;
			//1为预约挂号  99-当日挂号 （当日挂号只查询当天的排班信息）
			if("1".equals(CommonUtils.getPropValueStr(paramMap, "scheduleType"))){
				paramMap.put("total","0");
				timDouble = false;
			}else{
				paramMap.put("nowDate",dt.toString("YYYY-MM-dd"));// 当天的日期

			}
			paramMap.put("flagStop", "0");//停用标志
	    	List<Map<String, Object>> schpubList = schPubForWsMapper.LbTodaySchInfosByDate(paramMap);

	    	for (Map<String, Object> schpubMap : schpubList){
	    		Map<String, Object> schpub = new HashMap<>();
	    		//当日挂号只查询当前时段的号源信息）
	    		if(timDouble){
	    			schpubMap.put("time", dt.toString("HH:mm:ss"));// 当前时刻
	    		}
	    		//复诊号控制（过滤诊间预约）
	    		if("1".equals(ifFilter)) {
	    			schpubMap.put("dtApptype", "0");//预约渠道：诊间预约 	
	    		}
	    		schpubMap.put("flag_appt", "1");
	    		List<Map<String, Object>> querySchAppSec = schPubForWsMapper.querySchAppSec(schpubMap);
	    		Integer cntTotal = 0;
	    		Integer apptCount = 0;
	    		for (Map<String, Object> map : schpubList) {
    				cntTotal += Integer.valueOf(LbSelfUtil.getPropValueStr(map,"cntTotal")) ;
    				apptCount += Integer.valueOf(LbSelfUtil.getPropValueStr(map,"cntAppt")) ;
				}
	    		schpub.put("schseclist", querySchAppSec);
				schpub.put("pkSch", CommonUtils.getPropValueStr(schpubMap, "pkSch"));
	    		schpub.put("dateWork", CommonUtils.getPropValueStr(schpubMap, "dateWork"));
	    		schpub.put("cntAppt", apptCount);
	    		schpub.put("cntUsed", CommonUtils.getPropValueStr(schpubMap, "cntUsed"));
	    		schpub.put("cntTotal", cntTotal);
	    		schpub.put("price", CommonUtils.getPropValueStr(schpubMap, "price"));
	    		result.add(schpub);
			}
			respJson = resSuccReturn(respJson,JsonUtil.writeValueAsString(result,"yyyy-MM-dd HH:mm:ss"),result.size());
		} catch (Exception e) {
			respJson = resErrorReturn(respJson,e.getMessage());
		}
		logger.info("查询号源信息:响应参数\n"+respJson.toString());
		return respJson.toString();
	}


	/**
	 * 锁定号源
	 * @param param{pkSch,deviceid,patientId：必传}
	 * @return
	 */
	@Override
	public String lockReg(String param) {
		logger.info("锁定号源:请求参数\n"+param);
		OtherRespJson respJson = new OtherRespJson();
		try {
			Map<String,Object> result = new HashMap<String, Object>(16);
			Map<String, Object> paramMap = JsonUtil.readValue(param,Map.class);
			if(paramMap == null){
				respJson = resErrorReturn(respJson,"参数不能为空！");
				String resp = respJson.toString();
				logger.info("锁定号源{}",resp);
				return resp;
			}	
			if(CommonUtils.isEmptyString(CommonUtils.getPropValueStr(paramMap, "pkDateslotsec"))){
				respJson = resErrorReturn(respJson,"【pkDateslotsec】时段主键不能为空！");
				String resp = respJson.toString();
				logger.info("锁定号源{}",resp);
				return resp;
			}
			if(CommonUtils.isEmptyString(CommonUtils.getPropValueStr(paramMap, "pkSch"))){
				respJson = resErrorReturn(respJson,"【pkSch】排班主键不能为空！");
				String resp = respJson.toString();
				logger.info("锁定号源{}",resp);
				return resp;
			}
			if(CommonUtils.isEmptyString(CommonUtils.getPropValueStr(paramMap, "deviceid"))){
				respJson = resErrorReturn(respJson,"【deviceid】操作员工号不能为空！");
				String resp = respJson.toString();
				logger.info("锁定号源{}",resp);
				return resp;
			}
			User user = PskqSelfUtil.getDefaultUser(CommonUtils.getPropValueStr(paramMap, "deviceid"));
			if(!BeanUtils.isNotNull(user)){
				respJson = resErrorReturn(respJson,"【deviceid】操作员工号未注册，请先联系his管理员注册！");
				String resp = respJson.toString();
				logger.info("锁定号源{}",resp);
				return resp;
			}
			result = schPubForWsService.lockReg(paramMap, user);
			if(result == null){
				respJson = resErrorReturn(respJson,"锁定号源失败，请联系his管理员");
				String resp = respJson.toString();
				logger.info("锁定号源{}",resp);
				return resp;
			}
			if("false".equals(result.get("result"))){
				respJson = resErrorReturn(respJson,CommonUtils.getString(result.get("message")));
				String resp = respJson.toString();
				logger.info("锁定号源{}",resp);
				return resp;
			}
			respJson = resSuccReturn(respJson,JsonUtil.writeValueAsString(result,"yyyy-MM-dd HH:mm:ss"),1);
		} catch (Exception e) {
			respJson = resErrorReturn(respJson,e.getMessage());
		}
		logger.info("锁定号源:响应参数\n"+respJson.toString());
		return respJson.toString();
	}

	/**
	 * 号源解锁
	 * @param param{pkSchticket：必传}
	 * @return
	 */

	@Override
	public String unLockReg(String param) {
		logger.info("号源解锁:请求参数\n"+param);
		OtherRespJson respJson = new OtherRespJson();
		try {
			Map<String, Object> paramMap = JsonUtil.readValue(param,Map.class);
			if(paramMap == null){
				respJson = resErrorReturn(respJson,"参数不能为空！");
				String resp = respJson.toString();
				logger.info("号源解锁{}",resp);
				return resp;
			}	
			if(CommonUtils.isEmptyString(CommonUtils.getPropValueStr(paramMap, "pkSchticket"))){
				respJson = resErrorReturn(respJson,"【pkSchticket】号源主键不能为空！");
				String resp = respJson.toString();
				logger.info("号源解锁{}",resp);
				return resp;
			}
			//解锁
		    lbPubForWsService.lbUnLockReg(CommonUtils.getPropValueStr(paramMap, "pkSchticket"));
			respJson = resSuccReturn(respJson,null,1);
		} catch (Exception e) {
			respJson = resErrorReturn(respJson,e.getMessage());
		}
		logger.info("号源解锁:响应参数\n"+respJson.toString());
		return respJson.toString();
	}

	/**
	 * 预约登记--参数全部必传
	 * @param param{patientId,pkSch,pkDateslotsec,deviceid,idNo}
	 * @return
	 */
	@Override
	public String saveAppointment(String param) {
		logger.info("预约登记:请求参数\n"+param);
		OtherRespJson respJson = new OtherRespJson();
		try {
			Map<String,Object> result = new HashMap<String,Object>();
			Map<String, Object> paramMap = JsonUtil.readValue(param,Map.class);
			if(paramMap == null){
				respJson = resErrorReturn(respJson,"参数不能为空！");
				String resp = respJson.toString();
				logger.info("预约登记{}",resp);
				return resp;
			}	
			if(CommonUtils.isEmptyString(CommonUtils.getPropValueStr(paramMap, "pkSch"))){
				respJson = resErrorReturn(respJson,"【pkSch】排班主键不能为空！");
				String resp = respJson.toString();
				logger.info("预约登记{}",resp);
				return resp;
			}
			if(CommonUtils.isEmptyString(CommonUtils.getPropValueStr(paramMap, "codePi"))){
				respJson = resErrorReturn(respJson,"【codePi】患者编码不能为空！");
				String resp = respJson.toString();
				logger.info("预约登记{}",resp);
				return resp;
			}
			if(CommonUtils.isEmptyString(CommonUtils.getPropValueStr(paramMap, "idNo"))){
				respJson = resErrorReturn(respJson,"【idNo】身份证号不能为空！");
				String resp = respJson.toString();
				logger.info("预约登记{}",resp);
				return resp;
			}
			if(CommonUtils.isEmptyString(CommonUtils.getPropValueStr(paramMap, "pkDateslotsec"))){
				respJson = resErrorReturn(respJson,"【pkDateslotsec】时段主键不能为空！");
				String resp = respJson.toString();
				logger.info("预约登记{}",resp);
				return resp;
			}
			if(CommonUtils.isEmptyString(CommonUtils.getPropValueStr(paramMap, "deviceid"))){
				respJson = resErrorReturn(respJson,"【deviceid】操作员工号不能为空！");
				String resp = respJson.toString();
				logger.info("预约登记{}",resp);
				return resp;
			}
			User user = PskqSelfUtil.getDefaultUser(paramMap.get("deviceid").toString());
			if(!BeanUtils.isNotNull(user)){
				respJson = resErrorReturn(respJson,"【deviceid】操作员工号未注册，请先联系his管理员注册！");
				String resp = respJson.toString();
				logger.info("预约登记{}",resp);
				return resp;
			}
			UserContext userContext = new UserContext();
			userContext.setUser(user);

			LbPiMasterRegVo regvo =DataBaseHelper.queryForBean("SELECT * FROM pi_master pi LEFT JOIN PI_INSURANCE hp on hp.pk_pi = pi.pk_pi and hp.del_flag = '0' WHERE pi.del_flag = '0' and pi.code_pi = ?", LbPiMasterRegVo.class, new Object[]{CommonUtils.getPropValueStr(paramMap, "codePi")});
			if(regvo == null){
				respJson.setStatus(Constant.UNUSUAL);
				respJson.setDesc("失败");
				respJson.setErrorMessage("当前患者未建档，请先建档！");
				String resp = respJson.toString();
				logger.info("预约登记{}",resp);
				return resp;
			}

			List<Map<String, Object>> schAppList =DataBaseHelper.queryForList("SELECT * FROM sch_appt WHERE eu_status < '9' and flag_cancel ='0' AND pk_sch=? and pk_pi=?", new Object[]{CommonUtils.getPropValueStr(paramMap, "pkSch"),regvo.getPkPi()});
			if(schAppList.size()>0){
				respJson = resErrorReturn(respJson,"当前排班已预约！");
				String resp = respJson.toString();
				logger.info("预约登记{}",resp);
				return resp;
			}

			//根据号源主键查新排班信息
			List<Map<String, Object>> getSch = schPubForWsMapper.LbTodaySchInfosByDate(paramMap);
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
									respJson = resErrorReturn(respJson,"所选挂号科室年龄最大:"+Maximum+"岁，患者实际："+age+"岁");
									String resp = respJson.toString();
									logger.info("预约登记{}",resp);
									return resp;
								}
							}
						}
				}
				regvo.setEuSchclass("0");//排班类型
				if(CommonUtils.getPropValueStr(getSch.get(0),"euSrvtype").equals("9")){
				   regvo.setEuPvtype("2");//就诊类型
				}else{
				   regvo.setEuPvtype("1");//就诊类型
				}
				regvo.setPkSch(CommonUtils.getPropValueStr(getSch.get(0), "pkSch"));
				regvo.setPkSchplan(CommonUtils.getPropValueStr(getSch.get(0), "pkSchplan"));
				regvo.setPkPi(regvo.getPkPi());
				regvo.setIdNo(CommonUtils.getPropValueStr(paramMap, "idNo"));
				regvo.setPkSchsrv(CommonUtils.getPropValueStr(getSch.get(0), "pkSchsrv"));
				regvo.setPkSchres(CommonUtils.getPropValueStr(getSch.get(0), "pkSchres"));
				regvo.setPkDateslot(CommonUtils.getPropValueStr(getSch.get(0), "pkDateslot"));
				regvo.setPkDateslotsec(CommonUtils.getPropValueStr(paramMap, "pkDateslotsec"));//时段主键
				regvo.setDateAppt(DateUtils.parseDate(CommonUtils.getPropValueStr(getSch.get(0), "dateWork"),"yyyy-MM-dd"));//预约日期
				//预约渠道 10 = 健康网 ；维护系统字典：020100
				regvo.setOrderSource("jkm".equals(user.getCodeEmp()) ? "10" : "1");
				
				ResponseJson  NowSjh =  apputil.execService("PV", "RegSyxHandler", "saveApptSchRegInfo",regvo,user);
				if(NowSjh.getStatus()==Constant.SUC){
					Map<String, Object> pamMap = (Map<String, Object>)PskqSelfUtil.beanToMap1(NowSjh.getData());
					result.put("pkAppt", CommonUtils.getPropValueStr(pamMap,"pkAppt"));//预约订单主键
					result.put("apptCode", CommonUtils.getPropValueStr(pamMap,"apptCode"));//预约订单编码
					result.put("pkSchticket", CommonUtils.getPropValueStr(pamMap,"pkSchticket"));//号源主键
					result.put("idNo", CommonUtils.getPropValueStr(paramMap, "idNo"));//证件号码
					result.put("dtIdtype", "01");//证件号码类型
					Map<String, Object> piCardMap = DataBaseHelper.queryForMap("select CARD_NO from PI_CARD where FLAG_ACTIVE = '1' AND EU_STATUS ='0' and DT_CARDTYPE ='01' and pk_pi=? ORDER BY create_time DESC", new Object[]{regvo.getPkPi()});
					if(piCardMap != null){
						result.put("cardNo", CommonUtils.getPropValueStr(piCardMap,"cardNo"));//院内就诊卡号
						result.put("dtCardType", "01");//院内卡类型
		    		}else{
		    			result.put("cardNo", null);//院内就诊卡号
		    			result.put("dtCardType", null);//院内卡类型
		    		}
					result.put("pkSchsrv", CommonUtils.getPropValueStr(getSch.get(0),"pkSchsrv"));//科室名称
					result.put("pkDept", CommonUtils.getPropValueStr(getSch.get(0),"pkDept"));//科室名称
					result.put("nameDept", CommonUtils.getPropValueStr(getSch.get(0),"nameDept"));//科室名称
					result.put("codeDept", CommonUtils.getPropValueStr(getSch.get(0),"codeDept"));//科室名称
					result.put("nameEmp", CommonUtils.getPropValueStr(getSch.get(0),"nameEmp"));//医生名称
					result.put("codeEmp", CommonUtils.getPropValueStr(getSch.get(0),"codeEmp"));//医生编码
					Map<String, Object> cardTypeMap = DataBaseHelper.queryForMap("SELECT ticket_no FROM sch_appt WHERE code=?",CommonUtils.getPropValueStr(pamMap,"apptCode"));
					result.put("ticketno", CommonUtils.getPropValueStr(cardTypeMap,"ticketNo"));//诊号
					result.put("namePi", regvo.getNamePi());//患者姓名
					result.put("price", CommonUtils.getPropValueStr(getSch.get(0),"price"));//挂号费
					result.put("dateWork", CommonUtils.getPropValueStr(getSch.get(0), "dateWork"));//预约时间
					result.put("time", CommonUtils.getPropValueStr(getSch.get(0),"timeBegin").substring(0, 5)+"-"+CommonUtils.getPropValueStr(getSch.get(0),"timeEnd").substring(0, 5));//就诊时间段hh:mm:ss-hh:mm:ss

				}else{
					respJson = resErrorReturn(respJson,NowSjh.getDesc());
					String resp = respJson.toString();
					logger.info("预约登记{}",resp);
					return resp;
				}
		    }else{
				respJson = resErrorReturn(respJson,"未查询到可预约号源!");
				String resp = respJson.toString();
				logger.info("预约登记{}",resp);
				return resp;
		    }
			respJson = resSuccReturn(respJson,JsonUtil.writeValueAsString(result,"yyyy-MM-dd HH:mm:ss"),1);
		} catch (Exception e) {
			respJson = resErrorReturn(respJson,e.getMessage());
		}
		logger.info("预约登记:响应参数\n"+respJson.toString());
		return respJson.toString();
	}

	/**
	 * 预约登记撤销
	 * @param param
	 * @return
	 */
	@Override
	public String cancelAppointment(String param) {
		logger.info("预约登记撤销:请求参数\n"+param);
		OtherRespJson respJson = new OtherRespJson();
		try {
			Map<String,Object> result = new HashMap<String,Object>(16);
			Map<String, Object> paramMap = JsonUtil.readValue(param,Map.class);
			if(paramMap == null){
				respJson = resErrorReturn(respJson,"参数不能为空！");
				String resp = respJson.toString();
				logger.info("预约登记撤销{}",resp);
				return resp;
			}	
			if(CommonUtils.isEmptyString(CommonUtils.getPropValueStr(paramMap, "apptCode"))){
				respJson = resErrorReturn(respJson,"【apptCode】预约订单编码不能为空！");
				String resp = respJson.toString();
				logger.info("预约登记撤销{}",resp);
				return resp;
			}					
			SchAppt schAppt = DataBaseHelper.queryForBean("select * from sch_appt where del_flag = '0' and code = ?",
					SchAppt.class, CommonUtils.getString(paramMap.get("apptCode")));
			if(schAppt == null){
				respJson = resErrorReturn(respJson,"预约单号错误，apptCode："+ CommonUtils.getString(paramMap.get("apptCode")));
				String resp = respJson.toString();
				logger.info("预约登记撤销{}",resp);
				return resp;
			}
			paramMap.put("pkSchappt", schAppt.getPkSchappt());
			result = schPubForWsService.cancelAppointment(paramMap);
			if(result == null){
				respJson = resErrorReturn(respJson,"保存失败，请联系his管理员");
				String resp = respJson.toString();
				logger.info("预约登记撤销{}",resp);
				return resp;
			}
			if("false".equals(result.get("result"))){
				respJson = resErrorReturn(respJson,CommonUtils.getString(result.get("message")));
				String resp = respJson.toString();
				logger.info("预约登记撤销{}",resp);
				return resp;
			}
			respJson = resSuccReturn(respJson,JsonUtil.writeValueAsString(result,"yyyy-MM-dd HH:mm:ss"),1);
		} catch (Exception e) {
			respJson = resErrorReturn(respJson,e.getMessage());
		}
		logger.info("预约登记撤销:响应参数\n"+respJson.toString());
		return respJson.toString();
	}

	/**
	 * 提交挂号
	 * @param param
	 * @return
	 */
	@Override
	public String registerConfirg(String param) {
		logger.info("提交挂号:请求参数\n"+param);
		OtherRespJson respJson = new OtherRespJson();
		try {
			Map<String,Object> result = new HashMap<String,Object>(16);
			Map<String, Object> paramMap = JsonUtil.readValue(param,Map.class);
			if(paramMap == null){
				respJson = resErrorReturn(respJson,"参数不能为空！");
				String resp = respJson.toString();
				logger.info("提交挂号{}",resp);
				return resp;
			}	
			if(CommonUtils.isEmptyString(CommonUtils.getPropValueStr(paramMap, "deviceid"))){
				respJson = resErrorReturn(respJson,"【deviceid】操作员工号不能为空！");
				String resp = respJson.toString();
				logger.info("提交挂号{}",resp);
				return resp;
			}
			if(CommonUtils.isEmptyString(CommonUtils.getPropValueStr(paramMap, "codePi"))){
				respJson = resErrorReturn(respJson,"【codePi】患者编码不能为空！");
				String resp = respJson.toString();
				logger.info("提交挂号{}",resp);
				return resp;
			}
			if(CommonUtils.isEmptyString(CommonUtils.getPropValueStr(paramMap, "pkSchticket"))){
				respJson = resErrorReturn(respJson,"【pkSchticket】号源主键不能为空！");
				String resp = respJson.toString();
				logger.info("提交挂号{}",resp);
				return resp;
			}
			if(CommonUtils.isEmptyString(CommonUtils.getPropValueStr(paramMap, "payAmt"))){
				respJson = resErrorReturn(respJson,"【payAmt】自付支付金额不能为空！");
				String resp = respJson.toString();
				logger.info("提交挂号{}",resp);
				return resp;
			}
			if(CommonUtils.isEmptyString(CommonUtils.getPropValueStr(paramMap, "totalPayment"))){
				respJson = resErrorReturn(respJson,"【totalPayment】支付总金额不能为空！");
				String resp = respJson.toString();
				logger.info("提交挂号{}",resp);
				return resp;
			}
			if(CommonUtils.isEmptyString(CommonUtils.getPropValueStr(paramMap, "codeDept"))){
				respJson = resErrorReturn(respJson,"【codeDept】科室编码不能为空！");
				String resp = respJson.toString();
				logger.info("提交挂号{}",resp);
				return resp;
			}
			if(CommonUtils.isEmptyString(CommonUtils.getPropValueStr(paramMap, "medicalType"))){
				respJson = resErrorReturn(respJson,"【medicalType】支付类型为空！");
				String resp = respJson.toString();
				logger.info("提交挂号{}",resp);
				return resp;
			}
			if(CommonUtils.isEmptyString(CommonUtils.getPropValueStr(paramMap, "payType"))){
				respJson = resErrorReturn(respJson,"【payType】支付方式不能为空！");
				String resp = respJson.toString();
				logger.info("提交挂号{}",resp);
				return resp;
			}
			if(CommonUtils.isEmptyString(CommonUtils.getPropValueStr(paramMap, "euPvType"))){
				respJson = resErrorReturn(respJson,"【euPvType】就诊类型不能为空！");
				String resp = respJson.toString();
				logger.info("提交挂号{}",resp);
				return resp;
			}
			if(CommonUtils.isEmptyString(CommonUtils.getPropValueStr(paramMap, "orderno"))){
				respJson = resErrorReturn(respJson,"【orderno】订单号不能为空！");
				String resp = respJson.toString();
				logger.info("提交挂号{}",resp);
				return resp;
			}
			if(CommonUtils.isEmptyString(CommonUtils.getPropValueStr(paramMap, "sysname"))){
				respJson = resErrorReturn(respJson,"【sysname】渠道商不能为空！");
				String resp = respJson.toString();
				logger.info("提交挂号{}",resp);
				return resp;
			}
			if(CommonUtils.isEmptyString(CommonUtils.getPropValueStr(paramMap, "flowno"))){
				respJson = resErrorReturn(respJson,"【flowno】订单流水号不能为空！");
				String resp = respJson.toString();
				logger.info("提交挂号{}",resp);
				return resp;
			}
			User user = PskqSelfUtil.getDefaultUser(paramMap.get("deviceid").toString());
			if(!BeanUtils.isNotNull(user)){
				respJson = resErrorReturn(respJson,"【deviceid】操作员工号未注册，请先联系his管理员注册！");
				String resp = respJson.toString();
				logger.info("提交挂号{}",resp);
				return resp;
			}
			//医保参数验证
            if("1".equals(CommonUtils.getPropValueStr(paramMap,"medicalType"))) {
            	if(CommonUtils.isEmptyString(CommonUtils.getPropValueStr(paramMap, "serialNumber"))) {
            		respJson = resErrorReturn(respJson,"交易流水号:serialNumber，不能为空！");
    				String resp = respJson.toString();
    				logger.info("提交挂号{}",resp);
    				return resp;
            	}
            	if(CommonUtils.isEmptyString(CommonUtils.getPropValueStr(paramMap, "bke384"))) {
            		respJson = resErrorReturn(respJson,"机构结算业务号:bke384，不能为空！");
    				String resp = respJson.toString();
    				logger.info("提交挂号{}",resp);
    				return resp;
            	}
            	if(CommonUtils.isEmptyString(CommonUtils.getPropValueStr(paramMap, "bcc334"))) {
            		respJson = resErrorReturn(respJson,"缴费人员类别:bcc334，不能为空！");
    				String resp = respJson.toString();
    				logger.info("提交挂号{}",resp);
    				return resp;
            	}
            	if(CommonUtils.isEmptyString(CommonUtils.getPropValueStr(paramMap, "akc190"))) {
            		respJson = resErrorReturn(respJson,"费用流水号:akc190，不能为空！");
    				String resp = respJson.toString();
    				logger.info("提交挂号{}",resp);
    				return resp;
            	}
            	if(CommonUtils.isEmptyString(CommonUtils.getPropValueStr(paramMap, "ylzh"))) {
            		respJson = resErrorReturn(respJson,"医疗证号加密串:ylzh，不能为空！");
    				String resp = respJson.toString();
    				logger.info("提交挂号{}",resp);
    				return resp;
            	}
            	if(CommonUtils.isEmptyString(CommonUtils.getPropValueStr(paramMap, "aaz500"))) {
            		respJson = resErrorReturn(respJson,"社保卡号（医疗证号）:aaz500，不能为空！");
    				String resp = respJson.toString();
    				logger.info("提交挂号{}",resp);
    				return resp;
            	}
            	if(CommonUtils.isEmptyString(CommonUtils.getPropValueStr(paramMap, "aka130"))) {
            		respJson = resErrorReturn(respJson,"医疗类别:aka130，不能为空！");
    				String resp = respJson.toString();
    				logger.info("提交挂号{}",resp);
    				return resp;
            	}
            	if(CommonUtils.isEmptyString(CommonUtils.getPropValueStr(paramMap, "akc264"))) {
            		respJson = resErrorReturn(respJson,"费用总额:akc264，不能为空！");
    				String resp = respJson.toString();
    				logger.info("提交挂号{}",resp);
    				return resp;
            	}
            	if(CommonUtils.isEmptyString(CommonUtils.getPropValueStr(paramMap, "akb068"))) {
            		respJson = resErrorReturn(respJson,"基金支付金额:akb068，不能为空！");
    				String resp = respJson.toString();
    				logger.info("提交挂号{}",resp);
    				return resp;
            	}
            	if(CommonUtils.isEmptyString(CommonUtils.getPropValueStr(paramMap, "akb066"))) {
            		respJson = resErrorReturn(respJson,"个人账户支付金额:akb066，不能为空！");
    				String resp = respJson.toString();
    				logger.info("提交挂号{}",resp);
    				return resp;
            	}
            	if(CommonUtils.isEmptyString(CommonUtils.getPropValueStr(paramMap, "akb067"))) {
            		respJson = resErrorReturn(respJson,"个人支付金额:akb067，不能为空！");
    				String resp = respJson.toString();
    				logger.info("提交挂号{}",resp);
    				return resp;
            	}
            }
			UserContext.setUser(user);

			//1、如果是预约支付，先校验预约信息
			String dateAppt=null;
	    	String apptCode = CommonUtils.getPropValueStr(paramMap,"apptCode");
	    	if(!CommonUtils.isEmptyString(apptCode)){
	    		Map<String, Object> apptMap = DataBaseHelper.queryForMap("select appt.*,case when to_char(appt.date_reg,'yyyyMMdd') = to_char(appt.begin_time,'yyyyMMdd') then '1' else '0' end as timePosition ,to_char(appt.begin_time,'yyyyMMddHH24miss') beginStr,apptpv.pk_emp_phy from  sch_appt appt left join sch_appt_pv apptpv on apptpv.pk_schappt = appt.pk_schappt   where appt.code = ?  and appt.del_flag = '0' and appt.eu_status = '0' ", apptCode);
				if(apptMap==null){
					String message = "";
			    	if(DataBaseHelper.queryForScalar("select COUNT(1) from  sch_appt appt where appt.code = ? and appt.eu_status = '1' and appt.flag_pay = '0' and  appt.del_flag = '0' ", Integer.class,apptCode)==1){
						message = "预约号(" + apptCode + ")已成功预约,未支付,请勿重复推送";
			    	}
			    	else if(DataBaseHelper.queryForScalar("select COUNT(1) from  sch_appt appt where appt.code = ? and appt.eu_status = '1' and appt.flag_pay = '1' and  appt.del_flag = '0' ", Integer.class,apptCode)==1){
						message = "预约号(" + apptCode + ")已成功预约,已支付,请勿重复推送";
			    	}else{
			    		message = "未查询到该预约号(" + apptCode + ")信息";
			    	}
					respJson = resErrorReturn(respJson,message);
					String resp = respJson.toString();
					logger.info("提交挂号{}",resp);
					return resp;
				}else{
					dateAppt=CommonUtils.getPropValueStr(apptMap,"beginTime");
				}
	    	}
	    	//修改为手动事物 , 关闭事务自动提交
			DefaultTransactionDefinition def = new DefaultTransactionDefinition();
			def.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRES_NEW);
			TransactionStatus status = platformTransactionManager.getTransaction(def);			
			try {
				
				//2、新增外部接口支付明细,避免单边账
				if(!CommonUtils.isEmptyString(CommonUtils.getPropValueStr(paramMap,"orderno"))&&!"4".equals(CommonUtils.getPropValueStr(paramMap,"euPvType"))){
					schPubForWsService.saveBlExtPay(paramMap,user);
				}
				
				LbPiMasterRegVo regvo = DataBaseHelper.queryForBean("SELECT pi.*,hp.pk_hp,hp.pk_insurance FROM pi_master pi LEFT JOIN PI_INSURANCE hp on hp.pk_pi = pi.pk_pi and hp.del_flag = '0' WHERE pi.del_flag = '0' and pi.code_pi = ?", LbPiMasterRegVo.class, new Object[]{paramMap.get("codePi")});
				
				//医保/自费
				Map<String, Object> bdhpMap = null;
				if("1".equals(CommonUtils.getPropValueStr(paramMap,"medicalType"))){
					//通过bcc334 缴费人员类别 获取医保计划
					bdhpMap = DataBaseHelper.queryForMap("SELECT pk_hp FROM bd_hp WHERE code = ? and del_flag='0' ", CommonUtils.getPropValueStr(paramMap,"bcc334"));
					if(bdhpMap == null){
						platformTransactionManager.rollback(status);	
						respJson = resErrorReturn(respJson,"未查询到该("+CommonUtils.getPropValueStr(paramMap,"bcc334")+")医保编号所对应的医保主建信息");
						String resp = respJson.toString();
						logger.info("提交挂号{}",resp);
						return resp;
					}
					//第三方医保支付金额
					regvo.setAmtInsuThird(new BigDecimal(CommonUtils.getPropValueStr(paramMap,"amtInsuThird")==""?0:Double.valueOf(CommonUtils.getPropValueStr(paramMap,"amtInsuThird"))));
				}else{
					//获取自费医保类型
					bdhpMap = DataBaseHelper.queryForMap("SELECT pk_hp FROM bd_hp WHERE eu_hptype=? and del_flag='0' ", "0");
				}
				//门诊挂号类型
				regvo.setPkHp(CommonUtils.getPropValueStr(bdhpMap,"pkHp"));
				
				//第三方使用工号作为工作站计算机名，电子票据使用
				regvo.setNameMachine(CommonUtils.getPropValueStr(paramMap, "deviceid").toUpperCase());
				regvo.setInvStatus(CommonUtils.getPropValueStr(paramMap, "invStatus").toUpperCase());
	
				//儿科年龄上限
				Map<String, Object> cardTypeMap = DataBaseHelper.queryForMap("select val from bd_sysparam where del_flag = '0' and  code='PV0019' and pk_org=?",user.getPkOrg());
				//儿科科室
				Map<String, Object> codeTypeMap = DataBaseHelper.queryForMap("select val from bd_sysparam where del_flag = '0' and  code='CN0001' and pk_org=?",user.getPkOrg());
				if(null != codeTypeMap){
					String card[] = codeTypeMap.get("val").toString().split(",");
					for (int i = 0; i < card.length; i++) {
						if((card[i]).equals(CommonUtils.getPropValueStr(paramMap, "codeDept"))){
							int age = DateUtils.getAge(regvo.getBirthDate());
							int Maximum = Integer.valueOf(cardTypeMap.get("val").toString());
							if(age-Maximum>0){
								platformTransactionManager.rollback(status);	
								respJson = resErrorReturn(respJson,"所选挂号科室年龄最大:"+Maximum+"岁，患者实际："+age+"岁");
								String resp = respJson.toString();
								logger.info("提交挂号{}",resp);
								return resp;
							}
						}
					}
				}
				Map<String, Object> paramMap1 = new HashMap<String, Object>();
				paramMap1.put("pkSchticket",paramMap.get("pkSchticket"));
				//根据号源主键查新排班信息
				List<Map<String, Object>> schPlanlist = schPubForWsMapper.LbgetSchPlanInfo(paramMap);
				if(schPlanlist.size()<=0){
					respJson = resErrorReturn(respJson,"号源信息有误，请重新获取");
					String resp = respJson.toString();
					logger.info("提交挂号{}",resp);
					return resp;
				}
				SchSrv schSrv = DataBaseHelper.queryForBean("select * from SCH_SRV where PK_SCHSRV = ? ",SchSrv.class, CommonUtils.getPropValueStr(schPlanlist.get(0),"pkSchsrv"));
				//获取收费项目
	            ApplicationContext applicationContext = SpringContextHolder.getApplicationContext();
	            Map<String, Object> insurParam = new HashMap<>();
	    		insurParam.put("dateBirth", DateUtils.dateToStr("YYYY-MM-DD", regvo.getBirthDate()));
	    		insurParam.put("flagSpec", (schSrv.getEuSrvtype().equals("2"))?"1":"0");
	    		insurParam.put("pkPicate", regvo.getPkPicate());
	    		insurParam.put("pkSchsrv", schSrv.getPkSchsrv());
	    		insurParam.put("pkInsu", bdhpMap.get("pkHp"));
	    		insurParam.put("nameInsu",bdhpMap.get("name"));
	    		insurParam.put("euPvType",(schSrv.getEuSrvtype().equals("9"))?"2":"1");
	    		insurParam.put("pkEmp",regvo.getPkPi());
	    		String paramQuery = JsonUtil.writeValueAsString(insurParam);
	            RegSyxService regSyxService = applicationContext.getBean("regSyxService", RegSyxService.class);   
	            List<ItemPriceVo> listItem = regSyxService.getItemBySrv(paramQuery, user);
	            List<com.zebone.nhis.webservice.vo.ItemPriceVo> listItemVo = new ArrayList<com.zebone.nhis.webservice.vo.ItemPriceVo>();
	            for (ItemPriceVo itemPriceVo : listItem) {
	            	com.zebone.nhis.webservice.vo.ItemPriceVo itemPrice = new com.zebone.nhis.webservice.vo.ItemPriceVo();
	                ApplicationUtils.copyProperties(itemPrice, itemPriceVo);
	                listItemVo.add(itemPrice);
	            }
	            regvo.setItemList(listItemVo);
	            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	            regvo.setDateAppt(sdf.parse(dateAppt));
				result = pskqPubForWsService.register(param,paramMap, user,regvo);
				
				if(result == null){
					platformTransactionManager.rollback(status);	
					respJson = resErrorReturn(respJson,"保存失败，请联系his管理员！");
					String resp = respJson.toString();
					logger.info("提交挂号{}",resp);
					return resp;
				}
				if("false".equals(result.get("result"))){	
					platformTransactionManager.rollback(status);	
					respJson = resErrorReturn(respJson,CommonUtils.getString(result.get("message")));
					String resp = respJson.toString();
					logger.info("提交挂号{}",resp);
					return resp;
				}
				respJson = resSuccReturn(respJson,JsonUtil.writeValueAsString(result,"yyyy-MM-dd HH:mm:ss"),1);
				platformTransactionManager.commit(status);	
			} catch (Exception e) {
				platformTransactionManager.rollback(status);	
				respJson = resErrorReturn(respJson,e.getMessage());
			}
		} catch (Exception e) {
			respJson = resErrorReturn(respJson,e.getMessage());
		}
		logger.info("提交挂号:响应参数\n"+respJson.toString());
		return respJson.toString();
	}

	/**
	 * 取消挂号
	 * @param param
	 * @return
	 */
	@Override
	public String cancelRegister(String param) {
		logger.info("取消挂号:请求参数\n"+param);
		OtherRespJson respJson = new OtherRespJson();
		try {
			List<Map<String, Object>> result = new ArrayList<Map<String, Object>>();
			Map<String, Object> paramMap = JsonUtil.readValue(param,Map.class);	
			if(paramMap == null){
				respJson = resErrorReturn(respJson,"参数不能为空！");
				String resp = respJson.toString();
				logger.info("取消挂号{}",resp);
				return resp;
			}	
			if(CommonUtils.isEmptyString(CommonUtils.getPropValueStr(paramMap, "deviceid"))){
				respJson = resErrorReturn(respJson,"【deviceid】操作员工号不能为空！");
				String resp = respJson.toString();
				logger.info("取消挂号{}",resp);
				return resp;
			}
			if(CommonUtils.isEmptyString(CommonUtils.getPropValueStr(paramMap, "codePv"))){
				respJson = resErrorReturn(respJson,"【codePv】患者就诊编码不能为空！");
				String resp = respJson.toString();
				logger.info("取消挂号{}",resp);
				return resp;
			}
			if(CommonUtils.isEmptyString(CommonUtils.getPropValueStr(paramMap, "ticketno"))){
				respJson = resErrorReturn(respJson,"【ticketno】票号不能为空！");
				String resp = respJson.toString();
				logger.info("取消挂号{}",resp);
				return resp;
			}
			if(CommonUtils.isEmptyString(CommonUtils.getPropValueStr(paramMap, "pkSch"))){
				respJson = resErrorReturn(respJson,"【pkSch】排班主键不能为空！");
				String resp = respJson.toString();
				logger.info("取消挂号{}",resp);
				return resp;
			}

			User user = PskqSelfUtil.getDefaultUser(paramMap.get("deviceid").toString());
			if(!BeanUtils.isNotNull(user)){
				respJson = resErrorReturn(respJson,"【deviceid】操作员工号未注册，请先联系his管理员注册！");
				String resp = respJson.toString();
				logger.info("取消挂号{}",resp);
				return resp;
			}
			UserContext userContext = new UserContext();
			userContext.setUser(user);

			PvEncounter pv = DataBaseHelper.queryForBean("select * from PV_ENCOUNTER where code_pv = ?",
					PvEncounter.class, CommonUtils.getString(paramMap.get("codePv")));
			if(pv == null){
				respJson = resErrorReturn(respJson,"未获取到患者就诊信息，codePv："+ CommonUtils.getString(paramMap.get("codePv")));
				String resp = respJson.toString();
				logger.info("取消挂号{}",resp);
				return resp;
			}

			paramMap.put("ticketNo", paramMap.get("ticketno"));
			paramMap.put("pkPv", pv.getPkPv());
			
			paramMap.put("nameMachine", CommonUtils.getPropValueStr(paramMap, "deviceid").toUpperCase());
			
			String apptCode = CommonUtils.getPropValueStr(paramMap,"apptCode");
	    	if(!CommonUtils.isEmptyString(apptCode)){
	    		Map<String, Object> apptMap = DataBaseHelper.queryForMap("select * from sch_appt where code = ? ", apptCode);
				if(apptMap!=null){
					paramMap.put("pkAppt",CommonUtils.getPropValueStr(apptMap,"pkSchappt"));
				}
	    	}
			ResponseJson rs = apputil.execService("PV", "RefundSyxService", "cancelReg",paramMap,user);
			if(rs.getStatus()== Constant.SUC){
				List<BlDeposit> blDepositList = (List<BlDeposit>)rs.getData();
				for (BlDeposit blDeposit : blDepositList) {
					Map<String,Object> reg = new HashMap<String,Object>(16);
					reg.put("pkSettle", blDeposit.getPkSettle());
					String sql = "select inv.ebillbatchcode, inv.ebillno, inv.checkcode,inv.date_ebill,inv.qrcode_ebill,inv.url_ebill,inv.url_netebill, "
					 + " inv.ebillbatchcode_cancel,inv.ebillno_cancel,inv.checkcode_cancel,inv.date_ebill_cancel,inv.qrcode_ebill_cancel, "
					 + " inv.url_ebill_cancel,inv.url_netebill_cancel from bl_settle st left join bl_st_inv invst on invst.pk_settle = st.pk_settle_canc "
					 + " left join BL_INVOICE inv on  inv.pk_invoice=invst.pk_invoice where st.pk_settle = ? ";
					List<Map<String, Object>> invListMap = DataBaseHelper.queryForList(sql, blDeposit.getPkSettle());
					if(invListMap != null && invListMap.size() > 0) {
						reg.put("ebillbatchcode", CommonUtils.getPropValueStr(invListMap.get(0), "ebillbatchcode"));
						reg.put("ebillno", CommonUtils.getPropValueStr(invListMap.get(0), "ebillno"));
						reg.put("checkcode", CommonUtils.getPropValueStr(invListMap.get(0), "checkcode"));
						reg.put("dateEbill", CommonUtils.getPropValueStr(invListMap.get(0), "dateEbill"));
						reg.put("qrcodeEbill", CommonUtils.getPropValueStr(invListMap.get(0), "qrcodeEbill"));
						reg.put("urlEbill", CommonUtils.getPropValueStr(invListMap.get(0), "urlEbill"));
						reg.put("urlNetebill", CommonUtils.getPropValueStr(invListMap.get(0), "urlNetebill"));
						reg.put("ebillbatchcodeCancel", CommonUtils.getPropValueStr(invListMap.get(0), "ebillbatchcodeCancel"));
						reg.put("ebillnoCancel", CommonUtils.getPropValueStr(invListMap.get(0), "ebillnoCancel"));
						reg.put("checkcodeCancel", CommonUtils.getPropValueStr(invListMap.get(0), "checkcodeCancel"));
						reg.put("dateNbillCancel", CommonUtils.getPropValueStr(invListMap.get(0), "dateNbillCancel"));
						reg.put("qrcodeNbillCancel", CommonUtils.getPropValueStr(invListMap.get(0), "qrcodeNbillCancel"));
						reg.put("urlNbillCancel", CommonUtils.getPropValueStr(invListMap.get(0), "urlNbillCancel"));
						reg.put("urlNetebillCancel", CommonUtils.getPropValueStr(invListMap.get(0), "urlNetebillCancel"));
					}
					result.add(reg);
				}
				respJson = resSuccReturn(respJson,JsonUtil.writeValueAsString(result,"yyyy-MM-dd HH:mm:ss"),result.size());
			}else{
				respJson = resErrorReturn(respJson,rs.getDesc());
				String resp = respJson.toString();
				logger.info("取消挂号{}",resp);
				return resp;
			}
		} catch (Exception e) {
			respJson = resErrorReturn(respJson,e.getMessage());
		}
		logger.info("取消挂号:响应参数\n"+respJson.toString());
		return respJson.toString();
	}

	/**
	 * 查询门诊有效就诊记录
	 * @param param
	 * @return
	 */
	@Override
	public String getEffectPvInfo(String param) {
		logger.info("查询门诊有效就诊记录:请求参数\n"+param);
		OtherRespJson respJson = new OtherRespJson();
		try {
			List<Map<String, Object>> result = new ArrayList<Map<String,Object>>();
			Map<String, Object> paramMap = JsonUtil.readValue(param,Map.class);
			if(paramMap == null){
				respJson = resErrorReturn(respJson,"参数不能为空！");
				String resp = respJson.toString();
				logger.info("查询门诊有效就诊记录{}",resp);
				return resp;
			}	
			if(CommonUtils.isEmptyString(CommonUtils.getPropValueStr(paramMap, "codePi"))){
				respJson = resErrorReturn(respJson,"【codePi】患者编码不能为空！");
				String resp = respJson.toString();
				logger.info("查询门诊有效就诊记录{}",resp);
				return resp;
			}
			if(CommonUtils.isEmptyString(CommonUtils.getPropValueStr(paramMap, "onlyBlFlag"))){
				respJson = resErrorReturn(respJson,"【onlyBlFlag】有效就诊记录标志不能为空！");
				String resp = respJson.toString();
				logger.info("查询门诊有效就诊记录{}",resp);
				return resp;
			}
			result.addAll(pskqPubForWsService.getEffectPvInfo(paramMap));
			respJson = resSuccReturn(respJson,JsonUtil.writeValueAsString(result,"yyyy-MM-dd HH:mm:ss"),result.size());
		} catch (Exception e) {
			respJson = resErrorReturn(respJson,e.getMessage());
		}
		logger.info("查询门诊有效就诊记录:响应参数\n"+respJson.toString());
		return respJson.toString();
	}
	
	/**
	 * 查询门诊待缴费列表
	 * @param param
	 * @return
	 */
	@Override
	public String getPvToChargeInfo(String param) {
		logger.info("查询门诊待缴费列表:请求参数\n"+param);
		OtherRespJson respJson = new OtherRespJson();
		try {
			List<Map<String, Object>> result = new ArrayList<Map<String,Object>>();
			Map<String, Object> paramMap = JsonUtil.readValue(param,Map.class);
			if(paramMap == null){
				respJson = resErrorReturn(respJson,"参数不能为空！");
				String resp = respJson.toString();
				logger.info("查询门诊待缴费列表{}",resp);
				return resp;
			}	
			if(CommonUtils.isEmptyString(CommonUtils.getPropValueStr(paramMap, "codePv"))){
				respJson = resErrorReturn(respJson,"【codePv】患者就诊编码不能为空！");
				String resp = respJson.toString();
				logger.info("查询门诊待缴费列表{}",resp);
				return resp;
			}
			result.addAll(blPubForWsService.getPvToChargeInfo(paramMap));
			respJson = resSuccReturn(respJson,JsonUtil.writeValueAsString(result,"yyyy-MM-dd HH:mm:ss"),result.size());
		} catch (Exception e) {
			respJson = resErrorReturn(respJson,e.getMessage());
		}
		logger.info("查询门诊待缴费列表:响应参数\n"+respJson.toString());
		return respJson.toString();
	}

	/**
	 * 查询门诊已缴费列表（第三方缴费记录）
	 * @param param
	 * @return
	 */
	@Override
	public String getThirdPaidFeeByOp(String param) {
		logger.info("查询门诊已缴费列表:请求参数\n"+param);
		OtherRespJson respJson = new OtherRespJson();
		try {
			List<Map<String, Object>> result = new ArrayList<Map<String,Object>>();
			Map<String, Object> paramMap = JsonUtil.readValue(param,Map.class);
			if(paramMap == null){
				respJson = resErrorReturn(respJson,"参数不能为空！");
				String resp = respJson.toString();
				logger.info("查询门诊已缴费列表{}",resp);
				return resp;
			}	
			if(CommonUtils.isEmptyString(CommonUtils.getPropValueStr(paramMap, "codePv"))){
				respJson = resErrorReturn(respJson,"【codePv】患者就诊编码不能为空！");
				String resp = respJson.toString();
				logger.info("查询门诊已缴费列表{}",resp);
				return resp;
			}
			if(CommonUtils.isEmptyString(CommonUtils.getPropValueStr(paramMap, "orderno"))){
				respJson = resErrorReturn(respJson,"【orderno】订单号不能为空！");
				String resp = respJson.toString();
				logger.info("查询门诊已缴费列表{}",resp);
				return resp;
			}
		
			paramMap.put("payInfo", paramMap.get("orderno"));
			result.addAll(blPubForWsService.getThirdPaidFeeByOp(paramMap));
			respJson = resSuccReturn(respJson,JsonUtil.writeValueAsString(result,"yyyy-MM-dd HH:mm:ss"),result.size());
		} catch (Exception e) {
			respJson = resErrorReturn(respJson,e.getMessage());
		}
		logger.info("查询门诊已缴费列表:响应参数\n"+respJson.toString());
		return respJson.toString();
	}

	/**
     * 门诊收费结算（诊中支付）
     *
     * @param param
     * @return
     */
	@Override
	public String chargeOpSettle(String param) {
		logger.info("门诊收费结算:请求参数\n"+param);
		OtherRespJson respJson = new OtherRespJson();
		try {
			Map<String, Object> result = null;
			Map<String, Object> paramMap = JsonUtil.readValue(param,Map.class);
			if(paramMap == null){
				respJson = resErrorReturn(respJson,"参数不能为空！");
				String resp = respJson.toString();
				logger.info("门诊收费结算{}",resp);
				return resp;
			}	
			if(CommonUtils.isEmptyString(CommonUtils.getPropValueStr(paramMap, "codePv"))){
				respJson = resErrorReturn(respJson,"【codePv】患者就诊编码不能为空！");
				String resp = respJson.toString();
				logger.info("门诊收费结算{}",resp);
				return resp;
			}
			if(CommonUtils.isEmptyString(CommonUtils.getPropValueStr(paramMap, "orderno"))){
				respJson = resErrorReturn(respJson,"【orderno】订单号不能为空！");
				String resp = respJson.toString();
				logger.info("门诊收费结算{}",resp);
				return resp;
			}
			if(CommonUtils.isEmptyString(CommonUtils.getPropValueStr(paramMap, "amountMsgStr"))){
				respJson = resErrorReturn(respJson,"【amountMsgStr】处方总金额不能为空！");
				String resp = respJson.toString();
				logger.info("门诊收费结算{}",resp);
				return resp;
			}
			if(CommonUtils.isEmptyString(CommonUtils.getPropValueStr(paramMap, "payType"))){
				respJson = resErrorReturn(respJson,"【payType】支付方式不能为空！");
				String resp = respJson.toString();
				logger.info("门诊收费结算{}",resp);
				return resp;
			}
			if(CommonUtils.isEmptyString(CommonUtils.getPropValueStr(paramMap, "payAmt"))){
				respJson = resErrorReturn(respJson,"【payAmt】支付金额不能为空！");
				String resp = respJson.toString();
				logger.info("门诊收费结算{}",resp);
				return resp;
			}
			if(CommonUtils.isEmptyString(CommonUtils.getPropValueStr(paramMap, "flowno"))){
				respJson = resErrorReturn(respJson,"【flowno】订单流水号不能为空！");
				String resp = respJson.toString();
				logger.info("门诊收费结算{}",resp);
				return resp;
			}
			if(CommonUtils.isEmptyString(CommonUtils.getPropValueStr(paramMap, "sysname"))){
				respJson = resErrorReturn(respJson,"【sysname】渠道商不能为空！");
				String resp = respJson.toString();
				logger.info("门诊收费结算{}",resp);
				return resp;
			}
			if(CommonUtils.isEmptyString(CommonUtils.getPropValueStr(paramMap, "paytime"))){
				respJson = resErrorReturn(respJson,"【paytime】支付时间不能为空！");
				String resp = respJson.toString();
				logger.info("门诊收费结算{}",resp);
				return resp;
			}
			if(CommonUtils.isEmptyString(CommonUtils.getPropValueStr(paramMap, "medicalType"))){
				respJson = resErrorReturn(respJson,"【medicalType】支付类型不能为空！");
				String resp = respJson.toString();
				logger.info("门诊收费结算{}",resp);
				return resp;
			}
			if(CommonUtils.isEmptyString(CommonUtils.getPropValueStr(paramMap, "deviceid"))){
				respJson = resErrorReturn(respJson,"【deviceid】操作员工号不能为空！");
				String resp = respJson.toString();
				logger.info("门诊收费结算{}",resp);
				return resp;
			}
			User user = PskqSelfUtil.getDefaultUser(paramMap.get("deviceid").toString());
			if(!BeanUtils.isNotNull(user)){
				respJson = resErrorReturn(respJson,"【deviceid】操作员工号未注册，请先联系his管理员注册！");
				String resp = respJson.toString();
				logger.info("门诊收费结算{}",resp);
				return resp;
			}
			UserContext userContext = new UserContext();
			userContext.setUser(user);
			Map<String, Object> res=new HashMap<String, Object>();
			//ResponseJson res = null;
			String medicalType = CommonUtils.getPropValueStr(paramMap,"medicalType");
			if("1".equals(medicalType)){
				//诊中支付-医保方法
				res=pskqPubForWsService.clinicPaymentYB(JsonUtil.writeValueAsString(paramMap), user);
			}else if ("99".equals(medicalType)){
				//诊中支付-自费方法
				res=pskqPubForWsService.clinicPaymentZF(JsonUtil.writeValueAsString(paramMap), user);
			}else{
				respJson = resErrorReturn(respJson,"【medicalType】支付类型不正确，既不是医保也不是自费！");
				String resp = respJson.toString();
				logger.info("门诊收费结算{}",resp);
				return resp;
			}
			result = (Map<String, Object>)res.get("data");
			if(result == null){
				respJson = resErrorReturn(respJson,"结算失败，请联系his管理员！");
				String resp = respJson.toString();
				logger.info("门诊收费结算{}",resp);
			}
			if("false".equals(result.get("result"))){
				respJson = resErrorReturn(respJson,CommonUtils.getString(result.get("message")));
				String resp = respJson.toString();
				logger.info("门诊收费结算{}",resp);
			}
			respJson = resSuccReturn(respJson,JsonUtil.writeValueAsString(result,"yyyy-MM-dd HH:mm:ss"),result != null ? 1 : 0);
		} catch (Exception e) {
			respJson = resErrorReturn(respJson,e.getMessage());
		}
		logger.info("门诊收费结算:响应参数\n"+respJson.toString());
		return respJson.toString();

	}
	
	/**
     * 查询预约号源记录
     *
     * @param param
     * @return
     */
	@Override
	public String getApptRegRecords(String param) {
		
		logger.info("查询预约号源记录:请求参数\n"+param);
		OtherRespJson respJson = new OtherRespJson();
		try {
			List<Map<String, Object>> result = new ArrayList<Map<String,Object>>();
			Map<String, Object> paramMap = JsonUtil.readValue(param,Map.class);
			if(paramMap == null){
				respJson = resErrorReturn(respJson,"参数不能为空！");
				String resp = respJson.toString();
				logger.info("查询预约号源记录{}",resp);
				return resp;
			}	
			if(CommonUtils.isEmptyString(CommonUtils.getPropValueStr(paramMap, "codePi"))){
				respJson = resErrorReturn(respJson,"【codePi】患者编码不能为空！");
				String resp = respJson.toString();
				logger.info("查询预约号源记录{}",resp);
				return resp;
			}
			if(CommonUtils.isEmptyString(CommonUtils.getPropValueStr(paramMap, "nowDate"))){
				respJson = resErrorReturn(respJson,"【nowDate】指定日期不能为空！");
				String resp = respJson.toString();
				logger.info("查询预约号源记录{}",resp);
				return resp;
			}
			result.addAll(schPubForWsMapper.getApptRegRecords(paramMap));
			respJson = resSuccReturn(respJson,JsonUtil.writeValueAsString(result,"yyyy-MM-dd HH:mm:ss"),result.size());
		} catch (Exception e) {
			respJson = resErrorReturn(respJson,e.getMessage());
		}
		logger.info("查询预约号源记录:响应参数\n"+respJson.toString());
		return respJson.toString();
	}
	
	/**
     * 查询电子票据信息
     *
     * @param param
     * @return
     */
	@Override
	public String getEnoteInvInfo(String param) {
		logger.info("查询电子票据信息:请求参数\n"+param);
		OtherRespJson respJson = new OtherRespJson();
		try {
			List<EnoteInvInfo> result = new ArrayList<EnoteInvInfo>();
			Map<String, Object> paramMap = JsonUtil.readValue(param,Map.class);
			if(paramMap == null){
				respJson = resErrorReturn(respJson,"参数不能为空！");
				String resp = respJson.toString();
				logger.info("查询电子票据信息{}",resp);
				return resp;
			}	
	
			if(CommonUtils.isEmptyString(CommonUtils.getPropValueStr(paramMap, "pkInvoice"))){
				respJson = resErrorReturn(respJson,"【pkInvoice】发票明细ID不能为空！");
				String resp = respJson.toString();
				logger.info("查询电子票据信息{}",resp);
				return resp;
			}
			result.addAll(pskqPubForWsService.qryEnoteInvInfo(paramMap));
			respJson = resSuccReturn(respJson,JsonUtil.writeValueAsString(result,"yyyy-MM-dd HH:mm:ss"),result.size());
		} catch (Exception e) {
			respJson = resErrorReturn(respJson,e.getMessage());
		}
		logger.info("查询电子票据信息:响应参数\n"+respJson.toString());
		return respJson.toString();
	}
	
	/**
     * 获取医院微信公众号每日账单
     *
     * @param param
     * @return
     */
	@Override
	public String HisMerchantSum(String param) {
		logger.info("获取医院每日账单："+param);
		WebChatReqVO requ = null;
		// 响应结果
		HisMerchantSumResVO responseVo = new HisMerchantSumResVO();
		if (!CommonUtils.isEmptyString(param)) {
			requ = (WebChatReqVO) XmlUtil.XmlToBean(param, WebChatReqVO.class);
			if(CommonUtils.isEmptyString(requ.getDeviceid())){
				responseVo.setResultCode(Constant.RESSUC);
				responseVo.setResultMsg("【deviceid】操作员编码不能为空！");
				return XmlUtil.beanToXml(responseVo, HisMerchantSumResVO.class, false);
			}
			if(CommonUtils.isEmptyString(requ.getStartDate())){
				responseVo.setResultCode(Constant.RESSUC);
				responseVo.setResultMsg("【startDate】开始时间不能为空！");
				return XmlUtil.beanToXml(responseVo, HisMerchantSumResVO.class, false);
			}
			if(CommonUtils.isEmptyString(requ.getEndDate())){
				responseVo.setResultCode(Constant.RESSUC);
				responseVo.setResultMsg("【endDate】结束时间不能为空！");
				return XmlUtil.beanToXml(responseVo, HisMerchantSumResVO.class, false);
			}
		} else{
			responseVo.setResultCode(Constant.RESSUC);
			responseVo.setResultMsg("未获取到参数信息！");
			return XmlUtil.beanToXml(responseVo, HisMerchantSumResVO.class, false);
		}
		String deviceid = "";
		if("2510".equals(requ.getDeviceid())) {
			deviceid = "wxgzh";
		}else if("2500".equals(requ.getDeviceid())) {
			deviceid = "zzj";
		}
		User user = PskqSelfUtil.getDefaultUser(deviceid);
		if(user == null){
			responseVo.setResultCode(Constant.RESSUC);
			responseVo.setResultMsg(requ.getDeviceid() + "，该操作员编码未注册请联系管理员！");
			return XmlUtil.beanToXml(responseVo, HisMerchantSumResVO.class, false);
		}
		
		try {
			Map<String, Object> paramMap = new HashMap<String, Object>();
			paramMap.put("pkOrg", user.getPkOrg());	
			paramMap.put("startDate", requ.getStartDate());// 开始时间
			paramMap.put("endDate", requ.getEndDate());// 结束时间
			paramMap.put("sysname", requ.getDeviceid());// 渠道来源  2500-自助机 2510-微信		

			//获取医院微信公众号每日账单
			List<SumItemsVO> sumItems = pskqPubForWsService.getHisMerchantSum(paramMap);
			// 结果集添加到响应实体
			responseVo.setSumItems(sumItems);

		} catch (Exception e) {
			logger.info("获取医院每日账单："+e.getMessage());
			responseVo.setResultCode(Constant.RESSUC);
			responseVo.setResultMsg("失败原因：" + e.getMessage());
			return XmlUtil.beanToXml(responseVo, HisMerchantSumResVO.class, false);
		}
		responseVo.setResultCode(Constant.RESFAIL);
		String beanToXml = XmlUtil.beanToXml(responseVo, responseVo.getClass(), false);
		logger.info("获取医院每日账单：" + beanToXml);
		return beanToXml;	
	}
	
	/**
     * 获取医院微信公众号每日账单详情
     *
     * @param param
     * @return
     */
	@Override
	public String HisMerchantDetail(String param) {
		logger.info("获取医院每日账单明细："+param);
		WebChatReqVO requ = null;
		// 响应结果
		HisMerchantDetailResVO responseVo = new HisMerchantDetailResVO();
		if (!CommonUtils.isEmptyString(param)) {
			requ = (WebChatReqVO) XmlUtil.XmlToBean(param, WebChatReqVO.class);
			if(CommonUtils.isEmptyString(requ.getDeviceid())){
				responseVo.setResultCode(Constant.RESSUC);
				responseVo.setResultMsg("【deviceid】操作员编码不能为空！");
				return XmlUtil.beanToXml(responseVo, HisMerchantSumResVO.class, false);
			}
			if(CommonUtils.isEmptyString(requ.getStartDate())){
				responseVo.setResultCode(Constant.RESSUC);
				responseVo.setResultMsg("【startDate】开始时间不能为空！");
				return XmlUtil.beanToXml(responseVo, HisMerchantSumResVO.class, false);
			}
			if(CommonUtils.isEmptyString(requ.getEndDate())){
				responseVo.setResultCode(Constant.RESSUC);
				responseVo.setResultMsg("【endDate】结束时间不能为空！");
				return XmlUtil.beanToXml(responseVo, HisMerchantSumResVO.class, false);
			}
		} else{
			responseVo.setResultCode(Constant.RESSUC);
			responseVo.setResultMsg("未获取到参数信息！");
			return XmlUtil.beanToXml(responseVo, HisMerchantSumResVO.class, false);
		}
		String deviceid = "";
		if("2510".equals(requ.getDeviceid())) {
			deviceid = "wxgzh";
		}else if("2500".equals(requ.getDeviceid())) {
			deviceid = "zzj";
		}else if("健康网".equals(requ.getDeviceid())){
			deviceid = "jkm";
		}else if("2520".equals(requ.getDeviceid())){
			deviceid = "jk160";
		}
		User user = PskqSelfUtil.getDefaultUser(deviceid);
		if(user == null){
			responseVo.setResultCode(Constant.RESSUC);
			responseVo.setResultMsg(requ.getDeviceid() + "，该操作员编码未注册请联系管理员！");
			return XmlUtil.beanToXml(responseVo, HisMerchantSumResVO.class, false);
		}
		try {
			Map<String, Object> paramMap = new HashMap<String, Object>();
			paramMap.put("pkOrg", user.getPkOrg());	
			paramMap.put("startDate", requ.getStartDate());// 开始时间
			paramMap.put("endDate", requ.getEndDate());// 结束时间
			paramMap.put("sysname", requ.getDeviceid());// 渠道来源  2500-自助机 2510-微信		
			//获取医院微信公众号每日账单
			List<ItemsVO> sumItems = pskqPubForWsService.getHisMerchantDetail(paramMap);
			// 结果集添加到响应实体
			responseVo.setItems(sumItems);

		} catch (Exception e) {
			logger.info("获取医院每日账单明细："+e.getMessage());
			responseVo.setResultCode(Constant.RESSUC);
			responseVo.setResultMsg("失败原因：" + e.getMessage());
			return XmlUtil.beanToXml(responseVo, HisMerchantSumResVO.class, false);
		}
		responseVo.setResultCode(Constant.RESFAIL);
		String beanToXml = XmlUtil.beanToXml(responseVo, responseVo.getClass(), false);
		logger.info("获取医院每日账单明细：" + beanToXml);
		return beanToXml;
	}
	
	
	/**
	 * 通用反馈消息失败
	 * @param message 失败原因
	 */
	private OtherRespJson resErrorReturn(OtherRespJson respJson,String message) {
		respJson.setStatus(Constant.UNUSUAL);
		respJson.setDesc("失败");
		respJson.setErrorMessage(message);
		return respJson;
	}



	/**
	 * 通用反馈成功响应集
	 *
	 * @param id 消息唯一标识
	 */
	private OtherRespJson resSuccReturn(OtherRespJson respJson,String data,Integer total) {
		respJson.setData(data);
		respJson.setTotal(total);
		respJson.setStatus(Constant.RESFAIL);
		respJson.setDesc("成功");
		return respJson;
	}
	
	/**
     * 发送订单给资源池
     *
     * @param param
     * @return
     */
	@Override
	public String sendResourcePool(String param) {	
		logger.info("发送订单给资源池:请求参数\n"+param);
		OtherRespJson respJson = new OtherRespJson();
		try {
			Map<String, Object> paramMap = JsonUtil.readValue(param,Map.class);
			User user = PskqSelfUtil.getDefaultUser("zzj");
			
			
			//调用红冲吊电子票据接口
			//入参数格式：{"reason":"退费","busDateTime":"20201028225027649","billNo":"0010535766","operator":"欧阳鑫","billBatchCode":"44060119","placeCode":"05003"}
			enoteRts(param,"1.0","writeOffEBill");

			/*
			paramMap.put("isAdd", IsAdd.UPDATE);
			paramMap.put("pkPv", paramMap.get("pkPv"));
			paramMap.put("pkEmp", ((User)user).getPkEmp());
			paramMap.put("nameEmp", ((User)user).getNameEmp());
			paramMap.put("codeEmp", ((User)user).getCodeEmp());
			PlatFormSendUtils.sendPvOpCancelRegMsg(paramMap);
			//PlatFormSendUtils.sendPvOpRegMsg(paramMap);
			*/
			/*
			if(paramMap == null){
				respJson = resErrorReturn(respJson,"参数不能为空！");
				String resp = respJson.toString();
				logger.info("发送订单给资源池{}",resp);
				return resp;
			}	
			if(CommonUtils.isEmptyString(CommonUtils.getPropValueStr(paramMap, "pkPv"))){
				respJson = resErrorReturn(respJson,"【pkPv】患者就诊编码不能为空！");
				String resp = respJson.toString();
				logger.info("发送订单给资源池{}",resp);
				return resp;
			}
			//resourcePD001   resourcePD003  resourcePD004
			if(CommonUtils.isEmptyString(CommonUtils.getPropValueStr(paramMap, "resource"))){
				respJson = resErrorReturn(respJson,"【resource】不能为空！");
				String resp = respJson.toString();
				logger.info("发送订单给资源池{}",resp);
				return resp;
			}
			//User user = PskqSelfUtil.getDefaultUser("czq");
			if(!BeanUtils.isNotNull(user)){
				respJson = resErrorReturn(respJson,"【deviceid】操作员工号未注册，请先联系his管理员注册！");
				String resp = respJson.toString();
				logger.info("发送订单给资源池{}",resp);
				return resp;
			}
			UserContext.setUser(user);
			Map<String, Object> poolMap = new HashMap<>(16);
			poolMap.put("pkPv", CommonUtils.getPropValueStr(paramMap, "pkPv"));
            Object pool = ExtSystemProcessUtils.processExtMethod("ResourcePool", CommonUtils.getPropValueStr(paramMap, "resource"), new Object[]{poolMap});
			respJson = resSuccReturn(respJson,JsonUtil.writeValueAsString(pool,"yyyy-MM-dd HH:mm:ss"),1);
			*/
		} catch (Exception e) {
			respJson = resErrorReturn(respJson,e.getMessage());
		}
		logger.info("发送订单给资源池:响应参数\n"+respJson.toString());
		return respJson.toString();
	}
	
	/**
	 * 组织主参数，调用http服务
	 * @param dataJson  数据参数
	 * @param version	服务版本
	 * @param serviceName	服务名称	
	 */
	private void enoteRts(String dataJson,String version,String serviceName){
		String dataBs64Str = "";
		try {
			dataBs64Str= Base64.encodeToString(dataJson.getBytes("UTF-8"));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		
		//组织sign参数
		StringBuffer signSbf = new StringBuffer("");
		String noiseStr = NHISUUID.getKeyId();
		
		signSbf.append(String.format("appid=%s", "NFYKDXSCKQYYPS9643071"));//appid
		signSbf.append(String.format("&data=%s", dataBs64Str));//data
		signSbf.append(String.format("&noise=%s", noiseStr));//noise UUID
		signSbf.append(String.format("&key=%s", "6cb01cd1d3a27e3fc13a01b049"));//key
		signSbf.append(String.format("&version=%s", version));//version
		
		String signMd5 = new SimpleHash("md5",signSbf.toString()).toHex().toUpperCase();
		
		//组织主参数
		EnoteReqVo reqVo = new EnoteReqVo();
		reqVo.setAppid("NFYKDXSCKQYYPS9643071");
		reqVo.setData(dataBs64Str);
		reqVo.setNoise(noiseStr);
		reqVo.setVersion(version);
		reqVo.setSign(signMd5);
		
		//请求参数转为json格式
		String reqJson = JsonUtil.writeValueAsString(reqVo);
		/**调用服务接口*/
		String url ="http://192.168.4.7:7002/medical-web/api/medical/";
		StringBuffer urlStr = new StringBuffer(String.format("%s%s", url,serviceName));
		String resJson = HttpClientUtil.sendHttpPostJson(urlStr.toString(), reqJson);
		//解析响应参数
		EnoteReqVo enoteResVo = JsonUtil.readValue(resJson, EnoteReqVo.class);

		//对返回参数data做base64解码处理
		String datajson = Base64.decodeToString(enoteResVo.getData());
		//转换为data实体类
		EnoteResDataVo dataVo = JsonUtil.readValue(datajson, EnoteResDataVo.class);
		try {
			String jsonStr = Base64.decodeToString(dataVo.getMessage().getBytes("UTF-8"));
			System.out.println(jsonStr);
		} catch (Exception e) {
			// TODO: handle exception
		}
			
	}
   

}
