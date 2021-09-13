package com.zebone.nhis.pro.zsba.mz.pub.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringJoiner;

import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;

import com.zebone.nhis.bl.pub.vo.InvoiceInfo;
import com.zebone.nhis.bl.pub.vo.OpCgTransforVo;
import com.zebone.nhis.common.module.bl.BlInvoice;
import com.zebone.nhis.common.module.bl.BlInvoiceDt;
import com.zebone.nhis.common.module.bl.BlSettle;
import com.zebone.nhis.common.module.bl.BlStInv;
import com.zebone.nhis.common.support.CommonUtils;
import com.zebone.nhis.pro.zsba.common.support.DownLoadFileUtil;
import com.zebone.nhis.pro.zsba.mz.pub.support.ZsbaInvPrintProcessUtils;
import com.zebone.platform.common.support.User;
import com.zebone.platform.common.support.UserContext;
import com.zebone.platform.framework.support.IUser;
import com.zebone.platform.modules.dao.DataBaseHelper;
import com.zebone.platform.modules.exception.BusException;
import com.zebone.platform.modules.utils.JsonUtil;

import net.sf.json.JSONObject;

/**
 * 电子票据开立接口（交易号）
 * @author zhangtao
 *
 */
@Service
public class ZsbaBlInvEBillService {
	
	/**
	 * 交易号：007002003058->022003027098
	 * 开立门诊结算电子票据
	 * @param param
	 * @param user
	 */
	public OpCgTransforVo eBillMzOutpatient(String param,IUser user){
		OpCgTransforVo opCgTransforVo = JsonUtil.readValue(param, OpCgTransforVo.class);
		if(opCgTransforVo == null) {
			throw new BusException("未传入参数信息，请检查！");
		}
		if(CommonUtils.isNull(opCgTransforVo.getPkSettle())) {
			throw new BusException("未传入结算主键pkSettle，请检查！");
		}
		if(CommonUtils.isNull(opCgTransforVo.getPkPv())) {
			throw new BusException("未传入患者就诊主键pkPv，请检查！");
		}
		if(CommonUtils.isNull(opCgTransforVo.getPkPi())) {
			String sql = "select pk_pi from PV_ENCOUNTER where pk_pv=?";
			Map<String, Object> pvMap = DataBaseHelper.queryForMap(sql.toString(), opCgTransforVo.getPkPv());
			if(pvMap.isEmpty()){
				throw new BusException("未传入患者主键pkPi，请检查！");
			}
			opCgTransforVo.setPkPi(pvMap.get("pkPi").toString());
		}
		if(CommonUtils.isNull(opCgTransforVo.getMachineName())) {
			throw new BusException("未传入本地计算机名称machineName，请检查！");
		}
		List<InvoiceInfo> invoiceInfos = opCgTransforVo.getInvoiceInfo();
//		if(invoiceInfos == null || invoiceInfos.size() == 0 && "1".equals(opCgTransforVo.getFlagPrint())) {
//			throw new BusException("未传入发票信息invoiceInfo，请检查！");
//		}
		
		/*
		 * 2021-07-22 lipz
		 * 1.从公共字典中获取已配置的就诊身份
		 * 2.判断当前就诊身份是否在配置中
		 * 3.存在配置中的提示 不需要开立发票
		 */
		String notEleBillHpCodeSql = "select CODE from BD_DEFDOC where CODE_DEFDOCLIST='BABKFPHP'"; 
		List<Map<String, Object>> notEleBillHpList = DataBaseHelper.queryForList(notEleBillHpCodeSql);
		if(notEleBillHpList!=null && notEleBillHpList.size()>0) {
			StringJoiner notEleBillHpjoin = new StringJoiner(",");
			for (Map<String, Object> hpMap : notEleBillHpList) {
				notEleBillHpjoin.add(hpMap.get("code").toString());
			}
			String notEleBillHpStr = ","+notEleBillHpjoin.toString()+",";
			
			String hpSql = "SELECT hp.name, hp.CODE FROM BL_SETTLE st INNER JOIN BD_HP hp on hp.PK_HP=st.PK_INSURANCE WHERE st.PK_SETTLE=?";
			Map<String, Object> hpMap = DataBaseHelper.queryForMap(hpSql, opCgTransforVo.getPkSettle());
			if(hpMap!=null && hpMap.get("code")!=null) {
				if(notEleBillHpStr.contains(","+hpMap.get("code").toString()+",")) {
					throw new BusException("结算身为【"+hpMap.get("code").toString()+"】【"+hpMap.get("name").toString()+"】，不需要开立发票！");
				}
			}
		}
		
		// 兼容自助机、公众号没有登录的情况
		if(StringUtils.isNotEmpty(opCgTransforVo.getMachineName()) && opCgTransforVo.getMachineName().startsWith("999")){
			String sql = "SELECT emp.PK_ORG, job.PK_DEPT, emp.PK_EMP, emp.CODE_EMP name_emp FROM BD_OU_EMPLOYEE emp INNER JOIN BD_OU_EMPJOB job ON job.PK_EMP = emp.PK_EMP WHERE emp.CODE_EMP=?";
			Map<String, Object> empMap = DataBaseHelper.queryForMap(sql.toString(), opCgTransforVo.getMachineName());
			User emp = new User();
			emp.setPkOrg(empMap.get("pkOrg").toString());
			emp.setPkDept(empMap.get("pkDept").toString());
			emp.setPkEmp(empMap.get("pkEmp").toString());
			emp.setNameEmp(empMap.get("nameEmp").toString());
			UserContext.setUser(emp);
		}
		
		if(CommonUtils.isNull(opCgTransforVo.getFlagPrint())) {
			opCgTransforVo.setFlagPrint("0");
		}
		BlSettle bs = new BlSettle();
		//通过结算主键查询(这里传过来的结算实体只有几个主键信息)
		bs = DataBaseHelper.queryForBean("select * from BL_SETTLE where PK_SETTLE=?", BlSettle.class, opCgTransforVo.getPkSettle());
		bs.setPkSettle(opCgTransforVo.getPkSettle());
		bs.setPkPv(opCgTransforVo.getPkPv());
		bs.setPkPi(opCgTransforVo.getPkPi());
		bs.setDtSttype("01");
		if(invoiceInfos == null || invoiceInfos.size() == 0){
			invoiceInfos = new ArrayList<>();
			InvoiceInfo invInfo = new InvoiceInfo();
			invInfo.setMachineName(opCgTransforVo.getMachineName());
			invInfo.setFlagPrint(opCgTransforVo.getFlagPrint());
			invoiceInfos.add(invInfo);
		}else{
			invoiceInfos.get(0).setMachineName(opCgTransforVo.getMachineName());
			invoiceInfos.get(0).setFlagPrint(opCgTransforVo.getFlagPrint());
		}

		Map<String,Object> invMap = ZsbaInvPrintProcessUtils.saveOpInvInfo(bs, invoiceInfos);
		if(invMap==null || invMap.size()<=0){
			throw new BusException("收费结算时，发票信息组织失败，请检查！");
		}
		List<BlInvoice> blInvoices = (List<BlInvoice>)invMap.get("inv");
		List<BlInvoiceDt> blInvoiceDts = (List<BlInvoiceDt>)invMap.get("invDt");
		List<BlStInv> blStInvs = (List<BlStInv>)invMap.get("stInv");
		
		if(blInvoices!=null && blInvoices.size() > 0){
			DataBaseHelper.batchUpdate(DataBaseHelper.getInsertSql(BlInvoice.class), blInvoices); // 批量插入发票
		}
		if(blInvoiceDts!=null && blInvoiceDts.size() > 0){
			DataBaseHelper.batchUpdate(DataBaseHelper.getInsertSql(BlInvoiceDt.class), blInvoiceDts); // 批量插入发票明细
		}
		if(blStInvs!=null && blStInvs.size() > 0){
			DataBaseHelper.batchUpdate(DataBaseHelper.getInsertSql(BlStInv.class), blStInvs); // 批量写发票与结算的关系
		}
		opCgTransforVo.setBlInvoiceDts(blInvoiceDts);
		opCgTransforVo.setBlInvoices(blInvoices);
		return opCgTransforVo;
	}

	/**
	 * 获取电子票据Base64编码地址
	 * @param param
	 * @param user
	 * @return
	 */
	public Map<String, Object> getEleBillByPkSettle(String param,IUser user){
		Map<String, Object> resultMap = new HashMap<String, Object>();
		Map<String, Object> params = JsonUtil.readValue(param, Map.class);
		if(params.get("pkSettle")==null){
			throw new BusException("未传入参数结算主键！");
		}
		if(params.get("pkPv")==null){
			throw new BusException("未传入参数就诊主键！");
		}
		if(params.get("pkPi")==null) {
			String sql = "select pk_pi from PV_ENCOUNTER where pk_pv=?";
			Map<String, Object> pvMap = DataBaseHelper.queryForMap(sql.toString(), params.get("pkPv"));
			if(pvMap.isEmpty()){
				throw new BusException("未传入参数患者主键！");
			}
			params.put("pkPi", pvMap.get("pkPi").toString());
		}
		if(params.get("codeEmp")==null){
			throw new BusException("未传入参数操作员工号！");
		}
		String pkSettle = params.get("pkSettle").toString();
		String pkPi = params.get("pkPi").toString();
		String pkPv = params.get("pkPv").toString();
		String codeEmp = params.get("codeEmp").toString();
		
		String urlEbill = "";
		String sql = "SELECT i.URL_EBILL, i.CODE_INV FROM BL_ST_INV si LEFT JOIN [dbo].[BL_INVOICE] i on i.PK_INVOICE = si.PK_INVOICE WHERE si.PK_SETTLE=?";
		Map<String, Object> dataMap = DataBaseHelper.queryForMap(sql, pkSettle);
		if(dataMap!=null){
			if(dataMap.containsKey("codeInv") && dataMap.get("codeInv")!=null && StringUtils.isNotEmpty(dataMap.get("codeInv").toString())){
				throw new BusException("已开立纸质票据，请在换开纸质发票中重打！");
			}
			if(dataMap.containsKey("urlEbill") && dataMap.get("urlEbill")!=null && StringUtils.isNotEmpty(dataMap.get("urlEbill").toString())){
				urlEbill = dataMap.get("urlEbill").toString();//已开立的
			}
			if(StringUtils.isEmpty(urlEbill)){
				//未开立的重新开立
				JSONObject queryMap = new JSONObject();
				queryMap.put("pkPi", pkPi);
				queryMap.put("pkPv", pkPv);
				queryMap.put("pkSettle", pkSettle);
				queryMap.put("machineName", codeEmp);//本地计算机名称，自助机、公众号传终端号
				
				OpCgTransforVo octv = eBillMzOutpatient(queryMap.toString(), UserContext.getUser());
				if(octv!=null && octv.getBlInvoices()!=null && octv.getBlInvoices().size()>0){
					urlEbill = octv.getBlInvoices().get(0).getUrlEbill();
				}
			}
		}else{
			//未开立的重新开立
			JSONObject queryMap = new JSONObject();
			queryMap.put("pkPi", pkPi);
			queryMap.put("pkPv", pkPv);
			queryMap.put("pkSettle", pkSettle);
			queryMap.put("machineName", codeEmp);//本地计算机名称，自助机、公众号传终端号
			
			OpCgTransforVo octv = eBillMzOutpatient(queryMap.toString(), UserContext.getUser());
			if(octv!=null && octv.getBlInvoices()!=null && octv.getBlInvoices().size()>0){
				urlEbill = octv.getBlInvoices().get(0).getUrlEbill();
			}
		}
		
		if(StringUtils.isEmpty(urlEbill)){
			throw new BusException("开立电子票据失败，请联系管理员处理！");
		}
		
		String base64Url = getImgToBase64(urlEbill);
		if(StringUtils.isEmpty(urlEbill)){
			throw new BusException("电子票据转换失败，请联系管理员处理！");
		}
		resultMap.put("imgBase64Url", base64Url);
		
		return resultMap;
	}
	
	private String getImgToBase64(String eleBillUrl) {
		String base64Url = "";
		try {
			String orgUrl="http://59.33.44.118:7001/";
			String newUrl="http://192.168.200.36:7001/";

			eleBillUrl = eleBillUrl.replace(orgUrl, newUrl);
			
			// 发起请求并生成doc
			Document document = Jsoup.connect(eleBillUrl).timeout(10000).get();
			// 获取头部script
			Elements e = document.getElementsByTag("script").eq(0);
			// 定义要解析的js值变量
			String ciphertext = "",method = "",appId = "";
			
			/* 循环遍历script下面的JS变量 */
			for (Element element : e) {
				/* 取得JS变量数组 */
				String[] data = element.data().toString().split("var");
				/* 取得单个JS变量 */
				for (String variable : data) {
					/* 过滤variable为空的数据 */
					if (variable.contains("=")) {
						if (variable.contains("ciphertext")) {
							ciphertext = variable.substring(variable.indexOf("\"")+1, variable.lastIndexOf("\""));
						}
						if (variable.contains("method")) {
							method = variable.substring(variable.indexOf("\"")+1, variable.lastIndexOf("\""));
						}
						if (variable.contains("appId")) {
							appId = variable.substring(variable.indexOf("\"")+1, variable.lastIndexOf("\""));
						}
					}
				}
			}

			if(StringUtils.isNotEmpty(ciphertext) && StringUtils.isNotEmpty(method) && StringUtils.isNotEmpty(appId)){
				
				String queryUrl = newUrl + "medical-web/peripheral/queryBill/queryEbillImg.do?ciphertext={0}&appId={1}&method={2}";
				queryUrl = queryUrl.replace("{0}", ciphertext).replace("{1}", appId).replace("{2}", method);
				
				/* 存本地再转换
				String dir = "dzBill";
				String fileName = "dzBillImg.jpg";
				String savePath = SystemPathUtils.filePath(dir);
				DownLoadFileUtil.downLoadFromUrl2(queryUrl, fileName, savePath);
				base64Url = DownLoadFileUtil.localImageToBase64(savePath);
				*/
				
				// 从网络直接转换
				base64Url = DownLoadFileUtil.netImageToBase64(queryUrl);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return base64Url;
	}

	
}
