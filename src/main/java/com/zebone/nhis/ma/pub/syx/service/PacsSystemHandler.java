package com.zebone.nhis.ma.pub.syx.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.common.collect.Maps;
import com.zebone.nhis.ma.pub.syx.dao.PacsSystemMapper;
import com.zebone.nhis.ma.pub.syx.vo.Tfunctionitemlistforpacs;
import com.zebone.nhis.ma.pub.syx.vo.Tfunctionrequestforpacs;
import com.zebone.platform.framework.support.IUser;
import com.zebone.platform.modules.dao.DataBaseHelper;
import com.zebone.platform.modules.dao.jdbc.DataSourceRoute;
import com.zebone.platform.modules.exception.BusException;
import com.zebone.platform.modules.utils.JsonUtil;

/**
 * pacs系统业务处理服务(无事物)
 * 
 * @author yangxue
 * 
 */
@Service
public class PacsSystemHandler {

	@Autowired
	PacsSystemMapper pacsSystemMapper;

	@Autowired
	PacsSystemService pacsSystemService;
	
	/**
	 * 业务处理方法转换器
	 * 
	 * @param methodName
	 * @param args
	 */
	public Object invokeMethod(String methodName, Object... args) {
		Object result = null;
		switch (methodName) {
		case "savePacsApply":
			this.savePacsApply(args);
			break;
		case "savePacsEx":
			this.savePacsEx(args);
			break;
		case "qryPacsReportUrl":
			result = this.qryPacsReportUrl(args);
			break;
		}
		return result;
	}

	private Object qryPacsReportUrl(Object... args) {
		Map<String, Object> urlMap = null;
		if (args != null) {
			String codePv = (String) args[0];
			try {
				DataSourceRoute.putAppId("yy0040020");
				urlMap = DataBaseHelper.queryForMap("select ReportURL,ImageURL from VMFunctionReport where InPatientID=?", codePv);
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				DataSourceRoute.putAppId("default");
			}
		}
		return urlMap;
	}

	public List<Map<String, Object>> qryRisLisApply(String param, IUser user) {
		Map<String, Object> qryMap = JsonUtil.readValue(param, Map.class);
		String codePi = (String)qryMap.get("codePi");//患者编码
		String codeIp = (String)qryMap.get("codeIp");//住院号
		String isOp = (String)qryMap.get("isOp");//住院号

		List<Map<String, Object>> rtnList = new ArrayList<Map<String, Object>>();

		if (StringUtils.isNotBlank(codeIp)) {
			try {
				String risOrLis = (String) qryMap.get("risOrLis");
				if ("lis".equals(risOrLis)) {
					if("1".equals(isOp)){
						DataSourceRoute.putAppId("syxlisreqop");
					}else{
						DataSourceRoute.putAppId("syxlisreq");						
					}
					rtnList = pacsSystemMapper.qryLisApply(codePi,codeIp);
				} else {
					String risTypeCode = (String) qryMap.get("risTypeCode");
					switch (risTypeCode) {
					case "PACS73":
						//放射
						DataSourceRoute.putAppId("syxpacs");
						break;
					case "NL124":
						//心电
						DataSourceRoute.putAppId("syxnl");
						break;
					case "US125":
						//超声
						DataSourceRoute.putAppId("syxus");
						break;
					case "ES127":
						//内镜
						DataSourceRoute.putAppId("syxes");
						break;
					case "PA70252":
						//病理
						DataSourceRoute.putAppId("syxpa");
						break;
					case "NM32":
						//核医学
						DataSourceRoute.putAppId("syxnm");
						break;
					}
					if ("PA70252".equals(risTypeCode)) {
						//病理
						rtnList = pacsSystemMapper.qryPaApply(codePi,codeIp);
					} else if ("NM32".equals(risTypeCode)) {
						//核医学
						rtnList = pacsSystemMapper.qryNmApply(codePi,codeIp);
					} else if ("NL124".equals(risTypeCode)) {
						//心电图
						rtnList = pacsSystemMapper.qryXnlApply(codePi,codeIp);
					}else if ("US125".equals(risTypeCode)) {
						//超声
						rtnList = pacsSystemMapper.qryXusApply(codePi,codeIp);
					}else {
						//放射、内镜
						rtnList = pacsSystemMapper.qryRisApply(codePi,codeIp);
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				DataSourceRoute.putAppId("default");
			}
		}
		return rtnList;
	}

	public List<Map<String, Object>> qryLisResult(String param, IUser user) {
		Map<String, Object> qryMap = JsonUtil.readValue(param, Map.class);
		String lisLableNo = (String) qryMap.get("lisLableNo");
		List<Map<String, Object>> rtnList = new ArrayList<Map<String, Object>>();
		if (StringUtils.isNotBlank(lisLableNo)) {
			try {
				DataSourceRoute.putAppId("syxlisrpt");
				rtnList = pacsSystemMapper.qryLisResult("'"+lisLableNo+"'");
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				DataSourceRoute.putAppId("default");
			}
		}
		int i = 1;
		for (Map<String, Object> map : rtnList) {
			map.put("rownum", i);
			i++;
		}
		return rtnList;
	}

	/**
	 * 保存检查执行单
	 * 
	 * @param args
	 */
	private void savePacsEx(Object... args) {
		if (args != null) {
			List<String> pkExoccAllList = (List<String>) args[0]; // 全部医嘱执行单主键
			List<String> pkCnordAllList = null;
			if (args.length > 1) {
				pkCnordAllList = (List<String>) args[1];// 医嘱主键
			}
			boolean isOp = false;
			if (args.length > 2) {
				isOp = (boolean) args[2];//是否门诊
			}
			// 查询中间表需要的数据
			if ((pkExoccAllList != null && pkExoccAllList.size() > 0) || (pkCnordAllList != null && pkCnordAllList.size() > 0)) {
				Map<String, Object> pkMap = Maps.newHashMap();
				pkMap.put("pkExoccAllList", pkExoccAllList);
				pkMap.put("pkCnordAllList", pkCnordAllList);
				
				List<Tfunctionrequestforpacs> tfunctionrequestforpacsList;
				List<Tfunctionitemlistforpacs> tfunctionitemlistforpacs;
				if(isOp){
					tfunctionrequestforpacsList = pacsSystemMapper.qryOpRequestForPacs(pkMap);
					tfunctionitemlistforpacs = pacsSystemMapper.qryOpItemListForPacs(pkMap);
				}else{
					tfunctionrequestforpacsList = pacsSystemMapper.qryRequestForPacs(pkMap);
					tfunctionitemlistforpacs = pacsSystemMapper.qryItemListForPacs(pkMap);
				}
				
				
//				if ((tfunctionrequestforpacsList != null && tfunctionrequestforpacsList.size() > 0) || (tfunctionitemlistforpacs != null && tfunctionitemlistforpacs.size() > 0)){
//					if (tfunctionrequestforpacsList == null || tfunctionrequestforpacsList.size() <= 0) {
//						throw new BusException("数据上传至检查系统失败！\n未查询到检查申请单！");
//					}
//					if (tfunctionitemlistforpacs == null || tfunctionitemlistforpacs.size() <= 0) {
//						throw new BusException("数据上传至检查系统失败！\n未查询到该申请单对应的检查医嘱！");
//					}
//				}
				List<Tfunctionitemlistforpacs> savaPacsList = null;
				try {
					DataSourceRoute.putAppId("SYX_HIS_PACS");// 切换数据源 醫院正式庫
					// DataSourceRoute.putAppId("yy0040020");// 切换数据源 測試數據庫

					// 调用Service类保存数据并提交事务
					savaPacsList = pacsSystemService.savaPacs(tfunctionitemlistforpacs, tfunctionrequestforpacsList);
				} catch (Exception e) {
					e.printStackTrace();
					throw new BusException("数据上传至检查系统失败！\n"+e.getMessage());
				} finally {
					DataSourceRoute.putAppId("default");// 切换数据源
				}
				if (savaPacsList != null && savaPacsList.size() > 0)
					pacsSystemService.updateRisApply(savaPacsList);
			}
		}
	}

	/**
	 * 保存检查申请单(此方法定义仅为示例)
	 * 
	 * @param
	 */
	public void savePacsApply(Object... args) {
		if (args != null) {
			try {
				DataSourceRoute.putAppId("yy0040020");// 切换数据源
				// List<Map<String, Object>> queryForList2 =
				// DataBaseHelper.queryForList("select * from bd_ou_dept where pk_dept=?",
				// "817AFAE58A084C3FABA7690BE478C9CA");
				// List<Map<String, Object>> queryForList =
				// DataBaseHelper.queryForList("select * from atf_ypxx", new
				// Object[] {});
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				DataSourceRoute.putAppId("default");// 切换数据源
			}
		}
	}
	
	//查询在院患者列表：010005002008
	public List<Map<String, Object>> qryPatientList(String param, IUser user){
		
		Map<String, Object> map = JsonUtil.readValue(param, Map.class);
		
		return pacsSystemMapper.qryPatientList(map);
	}
	
	//查询出院患者列表：010005002010
	public List<Map<String, Object>> qryLeavePatientList(String param, IUser user){
		
		Map<String, Object> map = JsonUtil.readValue(param, Map.class);
		Object dateBegin = map.get("dateBegin");
		if(dateBegin != null){
			String dateBeginStr = dateBegin.toString();
			map.put("dateBegin", dateBeginStr.substring(0, 8)+"000000");
		}
		Object dateEnd = map.get("dateEnd");
		if(dateEnd != null){
			String dateEndStr = dateEnd.toString();
			map.put("dateEnd", dateEndStr.substring(0, 8)+"235959");
		}
				
		return pacsSystemMapper.qryLeavePatientList(map);
	}
	
	//查询转科患者列表：010005002011
	public List<Map<String, Object>> qryChangePatientList(String param, IUser user){
		
		Map<String, Object> map = JsonUtil.readValue(param, Map.class);
		
		return pacsSystemMapper.qryChangePatientList(map);
	}
	
	//查询会诊患者列表：010005002012
	public List<Map<String, Object>> qryConsultationPatientList(String param, IUser user){
		
		Map<String, Object> map = JsonUtil.readValue(param, Map.class);
		
		return pacsSystemMapper.qryConsultationPatientList(map);
	}
	
}
