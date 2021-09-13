package com.zebone.nhis.emr.rec.rec.handler;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.alibaba.druid.sql.visitor.functions.Now;
import com.zebone.nhis.ma.pub.platform.PlatFormSendUtils;
import com.zebone.nhis.ma.pub.platform.emr.vo.sendvo.ReqEmrPv;
import com.zebone.platform.modules.exception.BusException;

import org.springframework.stereotype.Service;

import com.zebone.nhis.common.module.emr.mgr.EmrOperateLogs;
import com.zebone.nhis.common.module.emr.rec.dict.ViewEmrPatList;
import com.zebone.nhis.common.module.emr.rec.rec.EmrLisResult;
import com.zebone.nhis.common.module.emr.rec.rec.EmrOpOrdList;
import com.zebone.nhis.common.module.emr.rec.rec.EmrOrdList;
import com.zebone.nhis.common.module.emr.rec.rec.EmrRisResult;
import com.zebone.nhis.common.module.pv.PvEncounter;
import com.zebone.nhis.common.support.ApplicationUtils;
import com.zebone.nhis.emr.rec.rec.dao.EmrRecMapper;
import com.zebone.nhis.emr.rec.rec.service.ReqRsltQryService;
import com.zebone.nhis.emr.rec.rec.vo.EmrVitalSigns;
import com.zebone.platform.common.support.User;
import com.zebone.platform.common.support.UserContext;
import com.zebone.platform.framework.support.IUser;
import com.zebone.platform.modules.dao.DataBaseHelper;
import com.zebone.platform.modules.dao.jdbc.DataSourceRoute;
import com.zebone.platform.modules.utils.JsonUtil;

/**
 * 检查检验结果查询处理类
 * @author chengjia
 *
 */
@Service
public class ReqRsltQryHandler {
	
	@Resource
	private ReqRsltQryService reqRsltQryService;

	@Resource
	private EmrRecMapper recMapper;

	/**
	 * 添加状态
	 **/
	public static final String AddState = "_ADD";

	/**
	 * 更新状态
	 */
	public static final String UpdateState = "_UPDATE";

	/**
	 * 删除状态
	 */
	public static final String DelState = "_DELETE";

	/**查询患者检验记录(gz)
	 * @param list
	 * @param u
	 * @return
	 */
	public List<EmrLisResult> queryPatLisResultSyx(Map map){
		List<EmrLisResult> rtnList= new ArrayList<EmrLisResult>();
		
		if(map.get("work")==null) return null;
		
		String work = map.get("work").toString();
		if(work.equals("0")){
			DataSourceRoute.putAppId("syxlisreq");
			try {
				rtnList = reqRsltQryService.queryPatLisReqSyx(map);
			} catch (Exception e) {
				System.out.println(e.getMessage());
				return rtnList;
				//throw e;
			}finally{
				DataSourceRoute.putAppId("default");
			}
		}else{
			if(map.get("smpNo")==null) return null;
			
			String text = map.get("smpNo").toString();
			String smpNos=text.replaceAll("\\b", "'");
			List<EmrLisResult> rptList = new ArrayList<EmrLisResult>();
			List<EmrLisResult> reqList= new ArrayList<EmrLisResult>();
			DataSourceRoute.putAppId("syxlisrpt");
			try {
				rtnList = reqRsltQryService.queryPatLisResultSyx(smpNos);
			} catch (Exception e) {
				System.out.println(e.getMessage());
				return rptList;
				//throw e;
			}finally{
				DataSourceRoute.putAppId("default");
			}
		}
		return rtnList;
	}
	
	/**查询患者检查记录(gz)
	 * @param list
	 * @param u
	 * @return
	 */
	public List<EmrRisResult> queryPatRisResultSyx(Map map){
		List<EmrRisResult> rtnList=new ArrayList<EmrRisResult>();
		List<EmrRisResult> listTmp=new ArrayList<EmrRisResult>();
		//1、PACS放射系统
		DataSourceRoute.putAppId("syxpacs");
		try {
			listTmp = reqRsltQryService.queryPatRisResultSyx(map);
			if(listTmp!=null&&listTmp.size()>0){
				rtnList.addAll(listTmp);
			}
		} catch (Exception e) {
			//return null;
		}finally{
			DataSourceRoute.putAppId("default");
		}
		listTmp=new ArrayList<EmrRisResult>();
		//2、心电
		DataSourceRoute.putAppId("syxnl");
		try {
			listTmp = reqRsltQryService.queryPatRisResultSyx(map);
			if(listTmp!=null&&listTmp.size()>0){
				rtnList.addAll(listTmp);
			}
		} catch (Exception e) {
			//return null;
		}finally{
			DataSourceRoute.putAppId("default");
		}
		listTmp=new ArrayList<EmrRisResult>();
		//3、超声
		DataSourceRoute.putAppId("syxus");
		try {
			listTmp = reqRsltQryService.queryPatRisSupperResultSyx(map);
			if(listTmp!=null&&listTmp.size()>0){
				for (EmrRisResult emrRisResult : listTmp) {
					emrRisResult.setSuperSound("1");
				}
				rtnList.addAll(listTmp);
			}
		} catch (Exception e) {
			//return null;
		}finally{
			DataSourceRoute.putAppId("default");
		}
		listTmp=new ArrayList<EmrRisResult>();
		//4、内镜
		DataSourceRoute.putAppId("syxes");
		try {
			listTmp = reqRsltQryService.queryPatRisResultSyx(map);
			if(listTmp!=null&&listTmp.size()>0){
				rtnList.addAll(listTmp);
			}
		} catch (Exception e) {
			System.out.println("syxes-error:"+e.getMessage());
			//return null;
		}finally{
			DataSourceRoute.putAppId("default");
		}
		listTmp=new ArrayList<EmrRisResult>();
		//5、病理
		DataSourceRoute.putAppId("syxpa");
		try {
			listTmp = reqRsltQryService.queryPatRisResultSyxPa(map);
			if(listTmp!=null&&listTmp.size()>0){
				rtnList.addAll(listTmp);
			}
		} catch (Exception e) {
			System.out.println("syxpa-error:"+e.getMessage());
			//return null;
		}finally{
			DataSourceRoute.putAppId("default");
		}
		listTmp=new ArrayList<EmrRisResult>();
		//6、核医学
		DataSourceRoute.putAppId("syxnm");
		try {
			listTmp = reqRsltQryService.queryPatRisResultSyxHyx(map);
			if(listTmp!=null&&listTmp.size()>0){
				rtnList.addAll(listTmp);
			}
		} catch (Exception e) {
			System.out.println("syxnm-error:"+e.getMessage());
		}finally{
			DataSourceRoute.putAppId("default");
		}
	
		return rtnList;
	}
	
	/**
	 * 查询患者生命体征数据
	 * @param map
	 * @return
	 */
	public List<EmrVitalSigns> queryVitalSignsSyx(Map map){
		
		List<EmrVitalSigns> list = new ArrayList<EmrVitalSigns>();
		DataSourceRoute.putAppId("syxnis");
		try {
			String codePv="";
			if(map.containsKey("codePv")){
				codePv=map.get("codePv").toString();
				if(codePv!=null) codePv="'"+codePv+"'";
			}
			list = reqRsltQryService.queryVitalSignsSyx(codePv);
		} catch (Exception e) {
			System.out.println(e.getMessage());
			return list;
			//throw e;
		}finally{
			DataSourceRoute.putAppId("default");
		}
		
		return list;
	}
	/**
	 * 科室质控点击完成时调用省病案接口存储过程
	 */
	public void updateProcba(String param, IUser user) {
		Map map = JsonUtil.readValue(param, Map.class);
		String hosCode=ApplicationUtils.getPropertyValue("emr.hos.code", "");
		if(hosCode!=null&& (hosCode.equals("syx") || hosCode.equals("zsba") || hosCode.equals("szkq"))){
			String val = ApplicationUtils.getSysparam("DeptQCExecMRInf", true);
			if(val!=null&&val.equals("1"))
			{
				PvEncounter pvEncounter =DataBaseHelper.queryForBean("select pk_pi,pk_pv from pv_encounter where code_pv=?",PvEncounter.class, map.get("codePv"));
				BigDecimal times =new BigDecimal(map.get("times").toString());
				if(hosCode.equals("syx")){
					DataSourceRoute.putAppId("syxprocba");
				}else if(hosCode.equals("zsba")){
					DataSourceRoute.putAppId("BAGL_bayy");
				}else if(hosCode.equals("szkq")){
					DataSourceRoute.putAppId("bagl_szkq");
				}
				
				int result=0;
				try {
					//查询病案表里有没有这条数据,2020-07-09改为用住院流水号校验
					/*String sql="select count(*) from his_ba1 where FPRN=? and FCYDATE=?";
					SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
					Date dt = sdf.parse(map.get("disTime").toString());
					sdf = new SimpleDateFormat("yyyy-MM-dd");
					Integer isnull=DataBaseHelper.queryForScalar(sql, Integer.class, new Object[]{map.get("codeIp"),sdf.format(dt)});*/
					String sql="select count(1) from his_ba1 where fzyid=?";
					Integer isnull=DataBaseHelper.queryForScalar(sql, Integer.class, new Object[]{map.get("codePv")});
					if(isnull==0){
						//如果没有则查询上传的住院次数有没有重复
						String cfTimeSql="select count(*) from HIS_BA1 where FTIMES=? and FPRN=?";
						Integer isTime=DataBaseHelper.queryForScalar(cfTimeSql, Integer.class, new Object[]{map.get("times"),map.get("codeIp")});
						if(isTime!=0){
							//如果重复则取最大的值加一
							String maxSql="select max(FTIMES) from his_ba1 where FPRN=?";
							Integer maxtime=DataBaseHelper.queryForScalar(maxSql, Integer.class,new Object[]{map.get("codeIp")});
							DataSourceRoute.putAppId("default");
							try {
								//修改咱系统首页的住院次数
								reqRsltQryService.updHomeTimes(pvEncounter.getPkPv(), maxtime+1); 
							} catch (Exception e) {
								// TODO: handle exception
							}finally{
								if(hosCode.equals("syx")){
									DataSourceRoute.putAppId("syxprocba");
								}else if(hosCode.equals("zsba")){
									DataSourceRoute.putAppId("BAGL_bayy");
								}
							}
							map.put("times", maxtime+1);
							result=recMapper.updateProc(map);
						}else{
							result=recMapper.updateProc(map);
						}
					}
				} catch (Exception e) {
					e.printStackTrace();
					DataSourceRoute.putAppId("default");
					EmrOperateLogs emrOpeLogs = new EmrOperateLogs();
					emrOpeLogs.setCode("page_upload");
					emrOpeLogs.setName("病案上传");
					emrOpeLogs.setDelFlag("0");
					emrOpeLogs.setEuStatus("0");
					emrOpeLogs.setPkOrg(UserContext.getUser().getPkOrg());
					emrOpeLogs.setPkPv(pvEncounter.getPkPv());
					emrOpeLogs.setPkPi(pvEncounter.getPkPi());
					emrOpeLogs.setTimes(times);
					if(e.getMessage().length()>1000){
						emrOpeLogs.setOperateTxt(e.getMessage().substring(0, 1000));
					}else{
						emrOpeLogs.setOperateTxt(e.getMessage());
					}
					reqRsltQryService.saveEmrOperLogs(emrOpeLogs);
				}finally{
					DataSourceRoute.putAppId("default");
					reqRsltQryService.updPatRecPageUpload(pvEncounter.getPkPv());
					if(result!=0){
						reqRsltQryService.updEmrOperLogs(pvEncounter.getPkPv());
					}
				}
			}
		}
	}
	/**
	 * 病案编码完成时调用存储过程
	 * @param param
	 * @param user
	 */
	public void updateProcbaSub(String param, IUser user){
		Map map = JsonUtil.readValue(param, Map.class);
		User u = (User) user;
		map.put("empCode", u.getCodeEmp());
		map.put("empName", u.getNameEmp());
		String hosCode=ApplicationUtils.getPropertyValue("emr.hos.code", "");
		if(hosCode!=null&&hosCode.equals("syx") || hosCode.equals("zsba")){
			String val = ApplicationUtils.getSysparam("DeptQCExecMRInf", true);
			if(val!=null&&val.equals("1"))
			{
				String sql="select times from emr_home_page where pk_pv=? and del_flag='0'";
				String times=DataBaseHelper.queryForScalar(sql, String.class, map.get("pkPv"));
				map.put("times", times);
				if(hosCode.equals("syx")){
					DataSourceRoute.putAppId("syxprocba");
				}else if(hosCode.equals("zsba")){
					DataSourceRoute.putAppId("BAGL_bayy");
				}
				int result=0;
				try {
					result=recMapper.updateProcbaSubIcd(map);
					result=recMapper.updateProcbaSub(map);
				} catch (Exception e) {
					// TODO: handle exception
					e.printStackTrace();
				}finally{
					DataSourceRoute.putAppId("default");
					if(result==0){
						reqRsltQryService.updPatRecFlagCode(map.get("pkPv").toString(),u.getCodeEmp());
					}
				}
			}
		}
	}
	
	/**
	 * 查询检查检验等相关信息syx
	 * @param map
	 */
	public Map<String,String> getPatObsInfo(Map map){
		Map<String,String> rtnMap = new HashMap<>();
		List<EmrLisResult> reqList=new ArrayList<EmrLisResult>();
		//System.out.println("getPatObsInfo:"+new Date());
		//取检验血型信息
		DataSourceRoute.putAppId("syxlisreq");
		try {
			reqList = reqRsltQryService.queryPatLisReqSyx(map);
		} catch (Exception e) {
			System.out.println(e.getMessage());
			//throw e;
		}finally{
			DataSourceRoute.putAppId("default");
		}
		//System.out.println("queryPatLisReqSyx:"+new Date());
		String testNo = "";
		if(reqList!=null&&reqList.size()>0){
			for(int i=0;i<reqList.size();i++){
				EmrLisResult lis=reqList.get(i);
				if(lis.getTestName()!=null&&lis.getTestName().equals("血型鉴定")){
					testNo = lis.getSmpNo();
				}
			}
		}
		if(testNo!=null&&!testNo.equals("")){
			String smpNos=testNo.replaceAll("\\b", "'");
			
			List<EmrLisResult> rptList = new ArrayList<EmrLisResult>();
			DataSourceRoute.putAppId("syxlisrpt");
			try {
				rptList = reqRsltQryService.queryPatLisResultSyx(smpNos);
				//System.out.println("queryPatLisResultSyx:"+new Date());
				if(rptList!=null&&rptList.size()>0){
					for(int j=0;j<rptList.size();j++){
						EmrLisResult rpt=rptList.get(j);
						if(rpt.getItemName()==null) continue;
						String result = rpt.getResult();
						if(result==null) continue;
						
						if(rpt.getItemName().equals("ABO血型")){
							if(result.equals("A型")){
								//01/1
								rtnMap.put("bloodCodeAbo", "1");
							}else if(result.equals("B型")){
								//02/2
								rtnMap.put("bloodCodeAbo", "2");
							}else if(result.equals("AB型")){
								//03/3
								rtnMap.put("bloodCodeAbo", "4");
							}else if(result.equals("O型")){
								//04/4
								rtnMap.put("bloodCodeAbo", "3");
							}
						}else if(rpt.getItemName().equals("Rh血型")){
							
							if(result.indexOf("阴性")>=0){//阳性(+)
								//01/1
								rtnMap.put("bloodCodeRh", "1");
							}else if(result.indexOf("阳性")>=0){
								//02/2
								rtnMap.put("bloodCodeRh", "2");
							}
								
						}
					}
				}
			} catch (Exception e) {
				System.out.println(e.getMessage());
				//throw e;
			}finally{
				DataSourceRoute.putAppId("default");
			}
		}
		
		
		//取病理信息
		List<EmrRisResult> risList=new ArrayList<EmrRisResult>();
		//病理
		DataSourceRoute.putAppId("syxpa");
		try {
			if(map.containsKey("beginDate")){map.put("beginDateStr", map.get("beginDate"));}
			risList = reqRsltQryService.queryPatRisResultSyxPa(map);
			//System.out.println("queryPatRisResultSyxPa:"+new Date());
			if(risList!=null&&risList.size()>0){
				String reqNo = risList.get(0).getReqNo();
				String checkDiags=risList.get(0).getCheckDiags();
				if(reqNo!=null&&!reqNo.equals("")){
					rtnMap.put("pathoNo", reqNo);
				}
				if(checkDiags!=null&&!checkDiags.equals("")){
					rtnMap.put("diagNamePatho", checkDiags);
				}
			}
			
		} catch (Exception e) {
			//return null;
		}finally{
			DataSourceRoute.putAppId("default");
		}
		return rtnMap;
	}
	/**病历归档请求
	 * create by: gao shiheng
	 *
	 * @Param: null
	 * @return
	 */
	public void emrFilingRequest(String param, IUser user){
		Map<String, String> map = JsonUtil.readValue(param, Map.class);
		
		String sendmsg=ApplicationUtils.getPropertyValue("emr.dept.qc.sendmsg", "");
		if(sendmsg!=null&&sendmsg.equals("1")){
			if (map.get("codePv") == null || map.get("codePi") == null || map.get("codeIp") == null || map.get("ipTimes") == null){
				throw new BusException("参数异常");
			}

			//发送消息至平台
			Map<String,Object> msgParam = new HashMap<String,Object>();
			msgParam.put("REV", JsonUtil.readValue(param, ReqEmrPv.class));
			msgParam.put("STATUS", this.UpdateState);
			PlatFormSendUtils.sendEmrMsg(msgParam);
			msgParam = null;
		}
		
	}
	
	/**
	 * 查询检验项目
	 * @param map
	 * @return
	 */
	public List<EmrLisResult> queryPatLisResultboai(Map map){
		List<EmrLisResult> rtnList= new ArrayList<EmrLisResult>();
		if(map.get("work")==null) return null;
		String work = map.get("work").toString();
		if(work.equals("0")){
			DataSourceRoute.putAppId("LIS_bayy");
			try {
				rtnList = reqRsltQryService.queryPatLisReqBoai(map);
			} catch (Exception e) {
				System.out.println(e.getMessage());
				return rtnList;
				//throw e;
			}finally{
				DataSourceRoute.putAppId("default");
			}
		}else{
			DataSourceRoute.putAppId("LIS_bayy");
			try {
				rtnList = reqRsltQryService.queryPatLisReqByObjBoai(map);
			} catch (Exception e) {
				System.out.println(e.getMessage());
				return rtnList;
				//throw e;
			}finally{
				DataSourceRoute.putAppId("default");
			}
			return rtnList;
		}
		
		return rtnList;
	}
	/**查询患者检查记录(boai)
	 * @param list
	 * @param u
	 * @return
	 */
	public List<EmrRisResult> queryPatRisResultBoai(Map map){
		List<EmrRisResult> rtnList=new ArrayList<EmrRisResult>();
		List<EmrRisResult> listTmp=new ArrayList<EmrRisResult>();
		List<EmrRisResult> nlTmp=new ArrayList<EmrRisResult>();
		DataSourceRoute.putAppId("baPacs");
		try {
			listTmp = reqRsltQryService.queryPatRisResultBoai(map);
			if(listTmp!=null&&listTmp.size()>0){
				rtnList.addAll(listTmp);
			}
		} catch (Exception e) {
			//return null;
		}finally{
			DataSourceRoute.putAppId("default");
		}
		//2、心电
		DataSourceRoute.putAppId("banl");
		try {
			nlTmp = reqRsltQryService.queryPatRisnlResultBoai(map);
			if(nlTmp!=null&&nlTmp.size()>0){
				rtnList.addAll(nlTmp);
			}
		} catch (Exception e) {
			//return null;
		}finally{
			DataSourceRoute.putAppId("default");
		}
		return rtnList;
	}
	
	/**
	 * 首页查询血型病历号等相关信息boai
	 * @param map
	 */
	public Map<String,String> getPatObsInfoBoai(Map map){
		Map<String,String> rtnMap = new HashMap<>();
		List<EmrLisResult> reqList=new ArrayList<EmrLisResult>();
		//System.out.println("getPatObsInfo:"+new Date());
		//取检验血型信息
		DataSourceRoute.putAppId("LIS_bayy");
		try {
			reqList = reqRsltQryService.queryPatLisReqByObjBoai(map);
			if(reqList!=null&&reqList.size()>0){
				for(int j=0;j<reqList.size();j++){
					EmrLisResult rpt=reqList.get(j);
					if(rpt.getItemName()==null) continue;
					String result = rpt.getResult();
					if(result==null) continue;
					if(rpt.getItemName().equals("ABO血型")){
						if(result.contains("A")){
							//01/1
							rtnMap.put("bloodCodeAbo", "1");
						}else if(result.contains("B")){
							//02/2
							rtnMap.put("bloodCodeAbo", "2");
						}else if(result.contains("AB")){
							//03/3
							rtnMap.put("bloodCodeAbo", "4");
						}else if(result.contains("O")){
							//04/4
							rtnMap.put("bloodCodeAbo", "3");
						}
					}else if(rpt.getItemName().equals("Rh血型")){
						
						if(result.indexOf("阴性")>=0){//阳性(+)
							//01/1
							rtnMap.put("bloodCodeRh", "1");
						}else if(result.indexOf("阳性")>=0){
							//02/2
							rtnMap.put("bloodCodeRh", "2");
						}
					}
				}
			}
		} catch (Exception e) {
			System.out.println(e.getMessage());
			//throw e;
		}finally{
			DataSourceRoute.putAppId("default");
		}
		
		//取病理信息
		List<EmrRisResult> risList=new ArrayList<EmrRisResult>();
		DataSourceRoute.putAppId("baPacs");
		try {
			if(map.containsKey("beginDate")){map.put("beginDateStr", map.get("beginDate"));}
			risList = reqRsltQryService.queryPatRisResultBoaiHomePage(map);
			if(risList!=null&&risList.size()>0){
				String reqNo = risList.get(0).getReqNo();
				String checkDiags=risList.get(0).getCheckDiags();
				if(reqNo!=null&&!reqNo.equals("")){
					rtnMap.put("pathoNo", reqNo);
				}
				if(checkDiags!=null&&!checkDiags.equals("")){
					rtnMap.put("diagNamePatho", checkDiags);
				}
			}
		} catch (Exception e) {
			//return null;
		}finally{
			DataSourceRoute.putAppId("default");
		}
		return rtnMap;
	}
	/**
	 * 根据第三方提供数据同步至患者信息表中(门诊)
	 * @param param
	 * @param user
	 */
	public String syncPatInfoOpByThird(String param, IUser user){
		Map map = JsonUtil.readValue(param, Map.class);
		PvEncounter pv=new PvEncounter();
		try {
			recMapper.syncPatInfoOpByThird(map);
		} catch (Exception e) {
			// TODO: handle exception
			//throw new BusException("未获取到HIS系统中病人就诊记录！");
			e.printStackTrace();
		}finally{
			String sqlHis = "select Convert(varchar(12),date_begin,112) datebegin,CODE_emp_tre empcode from [HISDB].tjhisdb.dbo.VIEW_EMR_HIS_ENC_OP where  code_pi='"+map.get("patientId")+"'and op_times='"+map.get("times")+"'";
			Map<String, Object> data = DataBaseHelper.queryForMap(sqlHis);
			if(data==null){
				throw new BusException("未获取到HIS系统中病人就诊记录！");
			}
			String sname = map.get("patientId")+data.get("datebegin").toString()+data.get("empcode").toString();
			String sql="SELECT * FROM PV_ENCOUNTER WHERE code_pv = '"+sname+"'";
			pv=DataBaseHelper.queryForBean(sql, PvEncounter.class,new Object[] {});
			if(pv!=null)
			{
				return pv.getPkPv();
			}
		}
		return null;
	}
	/**
	 * 根据第三方提供数据同步至患者信息表中(住院)
	 * @param param
	 * @param user
	 */
	public String syncPatInfoIpByThird(String param, IUser user){
		Map map = JsonUtil.readValue(param, Map.class);
		PvEncounter pv=new PvEncounter();
		try {
			recMapper.syncPatInfoIpByThird(map);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}finally{
			String sql="SELECT * FROM PV_ENCOUNTER WHERE code_pv = '"+map.get("patientId")+"/"+map.get("times")+"'";
			pv=DataBaseHelper.queryForBean(sql, PvEncounter.class,new Object[] {});
			if(pv!=null)
			{
				return pv.getPkPv();
			}
		}
		return null;
	}
	/**
	 * 查询第三方提供的医嘱视图
	 * @param param
	 * @param user
	 * @return
	 */
	public List<EmrOrdList> queryPatOrdResultThird(String param, IUser user){
		Map map = JsonUtil.readValue(param, Map.class);
		List<EmrOrdList> rtnList= new ArrayList<EmrOrdList>();
		String hosCode=ApplicationUtils.getPropertyValue("emr.hos.code", "");
		if(hosCode!=null&& hosCode.equals("sdtj")){
			DataSourceRoute.putAppId("tjhis_bayy");
			try {
				map.put("codePi", map.get("codePv").toString().split("/")[0]);
				map.put("times", map.get("codePv").toString().split("/")[1]);
				rtnList = reqRsltQryService.queryPatOrdResultThird(map);
			} catch (Exception e) {
				System.out.println(e.getMessage());
				return rtnList;
			}finally{
				DataSourceRoute.putAppId("default");
			}
			
		}
		return rtnList;
	}

	/**
	 * 查询患者的手术记录--博爱手麻
	 * @param param
	 * @param user
	 * @return
	 */
	public List<Map<String, Object>> queryOpsList(String param, IUser user) {
		Map<String, Object> map = JsonUtil.readValue(param, Map.class);
		List<Map<String, Object>> result=new ArrayList<Map<String, Object>>();
		DataSourceRoute.putAppId("pt_op");
		try {
			result=reqRsltQryService.queryPatOpsResultThird(map);
		}catch (Exception e) {
			System.out.println(e.getMessage());
			return result;
		}finally{
			DataSourceRoute.putAppId("default");
		}
		return result;
	}
	/**
	 * 查询患者的输血记录--博爱输血
	 * @param param
	 * @param user
	 * @return
	 */
	public List<Map<String, Object>> queryBloodList(String param, IUser user) {
		Map<String, Object> map = JsonUtil.readValue(param, Map.class);
		List<Map<String, Object>> result=new ArrayList<Map<String, Object>>();
		DataSourceRoute.putAppId("blood_bayy");
		try {
			result=reqRsltQryService.queryBloodList(map);
		}catch (Exception e) {
			System.out.println(e.getMessage());
			return result;
		}finally{
			DataSourceRoute.putAppId("default");
		}
		return result;
	}
	public Integer syncToHisTjDiag(String param, IUser user){
		Map map = JsonUtil.readValue(param, Map.class);
		int diagCount = recMapper.syncToHisTjDiag(map);
		return diagCount;
	}
}

