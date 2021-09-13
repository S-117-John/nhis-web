package com.zebone.nhis.webservice.syx.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.zebone.nhis.common.support.ApplicationUtils;
import com.zebone.nhis.common.support.CommonUtils;
import com.zebone.nhis.common.support.DateUtils;
import com.zebone.nhis.webservice.syx.dao.ScmHrMapper;
import com.zebone.nhis.webservice.syx.vo.scmhr.DataPrescription;
import com.zebone.nhis.webservice.syx.vo.scmhr.DataPrescriptionDetail;
import com.zebone.nhis.webservice.syx.vo.scmhr.DcDictDrug;
import com.zebone.nhis.webservice.syx.vo.scmhr.Request;
import com.zebone.nhis.webservice.syx.vo.scmhr.RequestDataVo;
import com.zebone.nhis.webservice.syx.vo.scmhr.ResSubject;
import com.zebone.nhis.webservice.syx.vo.scmhr.Response;
import com.zebone.nhis.webservice.syx.vo.scmhr.ResponseDataVo;
import com.zebone.nhis.webservice.syx.vo.scmhr.Result;
import com.zebone.nhis.webservice.syx.vo.scmhr.Root;
import com.zebone.platform.modules.dao.DataBaseHelper;

/**
 * 孙逸仙门诊华润包药机服务
 * @author jd
 *
 */
@Service
public class ScmHrService {
	private Logger logger = LoggerFactory.getLogger("syx.hrSyxConsis");
	
	@Resource
	private ScmHrMapper hrMapper;
	/**
	 * S112 药房药品货位数据查询服务
	 * @param req
	 * @param dateBegin
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public Response qryDrugSpacePosi(Request req,Date dateBegin){
		Response resvo=new Response();
		try {
			Map<String,Object> paramMap=new HashMap<String,Object>();
			paramMap=(Map<String, Object>) ApplicationUtils.beanToMap(req.getSubject());
			List<ResSubject> resSubList=hrMapper.qryDrugSpacePosi(paramMap);
			if(resSubList==null || resSubList.size()<=0){
				doDrugPosiRes(resvo,req,"AD");
			}else{
				doDrugPosiRes(resvo,req,"AA");
				resvo.setResSubject(resSubList);
			}
		} catch (Exception e) {
			doDrugPosiRes(resvo,req,"AE");
			logger.info("====================华润包药机查询药品货位【S112】接口：状态【失败】，时间："+DateUtils.getDate("yyyy-MM-dd HH:mm:ss")+"，异常原因："+e.getMessage()+"====================");
		}
		long timeStart=dateBegin.getTime();
		long timeEnd=new Date().getTime();
		logger.info("====================华润包药机查询药品货位接口【S112】结束：共耗时"+(timeEnd-timeStart)+"ms====================");
		return resvo;
	}
	
	/**
	 * 构建药房货位返回实例
	 * @param resvo
	 * @param type
	 */
	private void doDrugPosiRes(Response resvo,Request req,String type){
		resvo.setActionId("PharmacyDrugSpaceQuery");
		resvo.setActionName("药房药品货位数据查询服务");
		Result res= resvo.getResult();
		res.setId(type);
		if("AA".equals(type)){
			res.setText("处理成功！");
		}else{
			res.setText("处理失败！");
		}
		res.setRequestId(req.getId());
		res.setRequestTime(req.getCreateTime());
	
	}
	
	/**
	 * 查询药房窗口信息
	 * @param req
	 * @param dateBegin
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public Response qryDrugWindows(Request req,Date dateBegin){
		Response resvo=new Response();
		try {
			Map<String,Object> paramMap=new HashMap<String,Object>();
			paramMap=(Map<String, Object>) ApplicationUtils.beanToMap(req.getSubject());
			List<ResSubject> resSubList=hrMapper.qryDrugWindows(paramMap);
			if(resSubList==null || resSubList.size()<=0){
				doDrugWindowsRes(resvo,req,"AD");
			}else{
				doDrugWindowsRes(resvo,req,"AA");
				resvo.setResSubject(resSubList);
			}
		} catch (Exception e) {
			doDrugWindowsRes(resvo,req,"AE");
			logger.info("====================华润包药机查询窗口信息【S114】接口：状态【失败】，时间："+DateUtils.getDate("yyyy-MM-dd HH:mm:ss")+"，异常原因："+e.getMessage()+"====================");
		}
		long timeStart=dateBegin.getTime();
		long timeEnd=new Date().getTime();
		logger.info("====================华润包药机查询窗口信息【S114】接口结束：共耗时"+(timeEnd-timeStart)+"ms==========");
		return resvo;
	}
	
	/**
	 * 查询药房窗口接口消息构建
	 * @param resvo
	 * @param req
	 * @param type
	 */
	private void doDrugWindowsRes(Response resvo,Request req,String type){
		resvo.setActionId("DispensingWindowBasicDataQuery");
		resvo.setActionName("发药窗口基本数据查询服务");
		Result res= resvo.getResult();
		res.setId(type);
		if("AA".equals(type)){
			res.setText("处理成功！");
		}else{
			res.setText("处理失败！");
		}
		res.setRequestId(req.getId());
		res.setRequestTime(req.getCreateTime());
	
	}
	
	/**
	 * 药品字典查询服务
	 * @param req
	 * @param dateBegin
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public Response qryDrugDict(Request req,Date dateBegin){
		Response resvo=new Response();
		try {
			Map<String,Object> paramMap=new HashMap<String,Object>();
			paramMap=(Map<String, Object>) ApplicationUtils.beanToMap(req.getSubject());
			List<DcDictDrug> resSubList=hrMapper.qryDrugDict(paramMap);
			if(resSubList==null || resSubList.size()<=0){
				doDrugDictQuery(resvo,req,"AD");
			}else{
				doDrugDictQuery(resvo,req,"AA");
				resvo.getSubject().setDcDictDrug(resSubList);
			}
		} catch (Exception e) {			
			doDrugDictQuery(resvo,req,"AE");
			logger.info("====================华润包药机药品字典查询服务【S163】接口：状态【失败】，时间："+DateUtils.getDate("yyyy-MM-dd HH:mm:ss")+"，异常原因："+e.getMessage()+"====================");
		}
		long timeStart=dateBegin.getTime();
		long timeEnd=new Date().getTime();
		logger.info("====================华润包药机药品字典查询服务【S163】接口结束：共耗时"+(timeEnd-timeStart)+"ms====================");
		return resvo;
	}

	/**
	 * 药品字典查询服务返回参数构建
	 * @param resvo
	 * @param req
	 * @param type
	 */
	private void doDrugDictQuery(Response resvo,Request req,String type){
		resvo.setActionId("DrugDictQuery");
		resvo.setActionName("药品字典查询服务");
		Result res= resvo.getResult();
		res.setId(type);
		if("AA".equals(type)){
			res.setText("处理成功！");
		}else{
			res.setText("处理失败！");
		}
		res.setRequestId(req.getId());
		res.setRequestTime(req.getCreateTime());
	}
	
	/**
	 * 300 处方配药开始信息
	 * @param dataVo
	 * @param dateBegin
	 * @return
	 */
	public ResponseDataVo drugPreqStart(RequestDataVo dataVo,Date dateBegin){
		ResponseDataVo resDatavo=new ResponseDataVo();
		try {
			if(dataVo.getConsisPrescMstvw()!=null){
				String  presNo=dataVo.getConsisPrescMstvw().getPrescNo();
				String 	winno=dataVo.getOpwinid();
				if(CommonUtils.isEmptyString(presNo)){
					resDatavo.setRetcode("0");
					resDatavo.setRetmsg("调用开始配药【300】接口,未获得处方号！");
					resDatavo.setRetval("0");
				}else if(CommonUtils.isEmptyString(winno)){
					resDatavo.setRetcode("0");
					resDatavo.setRetmsg("调用开始配药【300】接口,未获得处方号！");
					resDatavo.setRetval("0");
				}else{
					StringBuffer sql=new StringBuffer();
					sql.append("update ex_pres_occ set flag_reg='1',date_reg=? ,winno_conf=?,eu_status='1'");
					sql.append(" where pres_no=? and  EU_STATUS in ('0','1') and flag_prep='0' and flag_conf='0'");
					int count=DataBaseHelper.update(sql.toString(), new Object[]{new Date(),winno,presNo});
					if(count==1){
						resDatavo.setRetcode("1");
						resDatavo.setRetmsg("调用开始配药接口【300】，开始配药处理成功！");
						resDatavo.setRetval("0");
					}else{
						resDatavo.setRetcode("0");
						resDatavo.setRetmsg("调用开始配药接口【300】，开始配药处理失败！");
						resDatavo.setRetval("0");
					}
				}
			}else{
				resDatavo.setRetcode("0");
				resDatavo.setRetmsg("调用开始配药接口【300】，未获得处方数据！");
				resDatavo.setRetval("0");
			}
		} catch (Exception e) {
			resDatavo.setRetcode("0");
			resDatavo.setRetmsg("开始配药接口调用失败！");
			resDatavo.setRetval("0");
			logger.info("====================华润包开始配药【300】接口：状态【失败】，时间："+DateUtils.getDate("yyyy-MM-dd HH:mm:ss")+"，异常原因："+e.getMessage()+"====================");
		}
		long timeStart=dateBegin.getTime();
		long timeEnd=new Date().getTime();
		logger.info("====================华润包开始配药【300】接口结束：共耗时"+(timeEnd-timeStart)+"ms====================");
		return resDatavo;
	}
	
	/**
	 * 301 处方配药完成信息 
	 * @param dataVo
	 * @param dateBegin 服务开始时间
	 * @return
	 */
	public ResponseDataVo drugPreqFinish(RequestDataVo dataVo,Date dateBegin){
		ResponseDataVo resDatavo=new ResponseDataVo();
		try {
			if(dataVo.getConsisPrescMstvw()!=null){
				String presNo=dataVo.getConsisPrescMstvw().getPrescNo();
				if(CommonUtils.isEmptyString(presNo)){
					resDatavo.setRetcode("0");
					resDatavo.setRetmsg("调用配药完成【301】接口，未获得处方号！");
					resDatavo.setRetval("0");
				}else{
					String pkEmp=getPkEmpByCode(dataVo.getOpmanno());
					StringBuffer sql=new StringBuffer();
					sql.append("update ex_pres_occ set eu_status='2',flag_prep='1',flag_susp='0', date_prep=?,pk_emp_prep=?,  name_emp_prep=?,winno_conf=?");
					sql.append(" where pres_no=? and eu_status in ('0','1','8','9') and flag_prep='0' and flag_canc='0'");
					
					int count=DataBaseHelper.update(sql.toString(), new Object[]{new Date(),pkEmp,dataVo.getOpmanname(),dataVo.getOpwinid(),presNo});
					if(count==1){
						resDatavo.setRetcode("1");
						resDatavo.setRetmsg("调用配药完成接口，处方 "+presNo+" 配药状态更新成功");
						resDatavo.setRetval("0");
					}else{
						resDatavo.setRetcode("0");
						resDatavo.setRetmsg("调用配药完成接口，处方 "+presNo+" 配药状态更新失败");
						resDatavo.setRetval("0"); 
					}
				}
			}else{
				resDatavo.setRetcode("0");
				resDatavo.setRetmsg("调用配药完成【301】接口，未获得处方数据！");
				resDatavo.setRetval("0");
			}
		} catch (Exception e) {
			resDatavo.setRetcode("0");
			resDatavo.setRetmsg("配药完成接口调用失败！");
			resDatavo.setRetval("0");
			logger.info("====================华润包完成配药【301】接口：状态【失败】，时间："+DateUtils.getDate("yyyy-MM-dd HH:mm:ss")+"，异常原因："+e.getMessage()+"====================");
		}
		long timeStart=dateBegin.getTime();
		long timeEnd=new Date().getTime();
		logger.info("====================华润包完成配药【301】接口结束：共耗时"+(timeEnd-timeStart)+"ms====================");
		return resDatavo;
	} 
	
	/**
	 * 302 取消处方配药信息
	 * @param dataVo
	 * @param dateBegin 服务开始时间
	 * @return
	 */
	public ResponseDataVo drugPreqCancel(RequestDataVo dataVo,Date dateBegin){
		ResponseDataVo resDatavo=new ResponseDataVo();
		try {
			if(dataVo.getConsisPrescMstvw()!=null){
				String presNo=dataVo.getConsisPrescMstvw().getPrescNo();
				if(CommonUtils.isEmptyString(presNo)){
					resDatavo.setRetcode("0");
					resDatavo.setRetmsg("调用配药完成【301】接口，未获得处方号！");
					resDatavo.setRetval("0");
				}else{
					StringBuffer sql=new StringBuffer();
					sql.append("update ex_pres_occ  set eu_status='1', flag_prep='0',date_prep=null,pk_emp_prep=null,name_emp_prep=null");
					sql.append(" where pres_no=? and eu_status='2'");
					int count=DataBaseHelper.update(sql.toString(), new Object[]{presNo});
					if(count==1){
						resDatavo.setRetcode("1");
						resDatavo.setRetmsg("调用取消配药【302】接口，处方 "+presNo+" 配药状态更新成功");
						resDatavo.setRetval("0");
					}else{
						resDatavo.setRetcode("0");
						resDatavo.setRetmsg("调用配药完成接口，处方 "+presNo+" 配药状态更新失败");
						resDatavo.setRetval("0");
					}
				}
			}else{
				resDatavo.setRetcode("0");
				resDatavo.setRetmsg("调用取消配药【302】接口，未获得处方数据！");
				resDatavo.setRetval("0");
			}
		} catch (Exception e) {
			resDatavo.setRetcode("0");
			resDatavo.setRetmsg("调用取消配药接口处理失败！");
			resDatavo.setRetval("0");
			logger.info("====================华润包取消配药【302】接口：状态【失败】，时间："+DateUtils.getDate("yyyy-MM-dd HH:mm:ss")+"，异常原因："+e.getMessage()+"====================");
		}
		long timeStart=dateBegin.getTime();
		long timeEnd=new Date().getTime();
		logger.info("====================华润包取消配药【302】接口结束：共耗时"+(timeEnd-timeStart)+"ms====================");
		return resDatavo;
	} 
	
	/**
	 * 303 处方重新分配窗口信息
	 * @param dataVo
	 * @param dateBegin
	 */
	public ResponseDataVo drugResetWinNo(RequestDataVo dataVo,Date dateBegin){
		ResponseDataVo resDatavo=new ResponseDataVo();
		try {
			if(dataVo.getConsisPrescMstvw()!=null){
				String presNo=dataVo.getConsisPrescMstvw().getPrescNo();
				String opwinid=dataVo.getOpwinid();
				if(CommonUtils.isEmptyString(presNo)){
					resDatavo.setRetcode("0");
					resDatavo.setRetmsg("调用配药完成【303】接口，未获得处方号！");
					resDatavo.setRetval("0");
				}else{
					StringBuffer sql=new StringBuffer();
					sql.append("update ex_pres_occ set winno_prep=?");
					sql.append(" where pres_no=?");
					int count=DataBaseHelper.update(sql.toString(), new Object[]{opwinid,presNo});
					if(count>0){
						resDatavo.setRetcode("1");
						resDatavo.setRetmsg("调用处方重新分配窗口信息【303】接口，处方 "+presNo+" 重新分配状态更新成功");
						resDatavo.setRetval("0");
					}else{
						resDatavo.setRetcode("0");
						resDatavo.setRetmsg("调用处方重新分配窗口信息，处方 "+presNo+" 重新分配状态更新失败");
						resDatavo.setRetval("0");
					}
				}
			}else{
				resDatavo.setRetcode("0");
				resDatavo.setRetmsg("调用处方重新分配窗口信息【303】接口，未获得处方数据！");
				resDatavo.setRetval("0");
			}
		} catch (Exception e) {
			resDatavo.setRetcode("0");
			resDatavo.setRetmsg("调用处方重新分配窗口信息【303】接口失败！");
			resDatavo.setRetval("0");
			logger.info("====================华润包药机重新分配窗口信息【303】接口：状态【失败】，时间："+DateUtils.getDate("yyyy-MM-dd HH:mm:ss")+"，异常原因："+e.getMessage()+"====================");
		}
		long timeStart=dateBegin.getTime();
		long timeEnd=new Date().getTime();
		logger.info("====================华润包药机重新分配窗口信息【303】接口结束：共耗时"+(timeEnd-timeStart)+"ms====================");
		return resDatavo;
	}
	
	/**
	 * 311 窗口状态信息 
	 * @param dataVo
	 * @param dateBegin
	 * @return
	 */
	public ResponseDataVo WinStatusMsg(RequestDataVo dataVo,Date dateBegin){
		ResponseDataVo resDatavo=new ResponseDataVo();
		if(dataVo.getConsisPrescMstvw()!=null){
			try {
				String opwinidStatus=dataVo.getConsisPrescMstvw().getOpwinidStatus();
				String dispensary=dataVo.getConsisPrescMstvw().getDispensary();
				String opwinid=dataVo.getOpwinid();
				String sql1 = "select PK_DEPT from BD_OU_DEPT where code_dept=? ";
				List<Map<String, Object>> list = DataBaseHelper.queryForList(sql1, new Object[]{dispensary});
				if(opwinidStatus.equals("Y")){
					StringBuffer sql=new StringBuffer();
					sql.append("update bd_dept_unit du set du.flag_online='1'");
					sql.append(" where du.eu_unittype='1' and du.eu_butype='1' and du.pk_dept=? and du.code=? and du.flag_online='0'");
					int count=DataBaseHelper.update(sql.toString(), new Object[]{list.get(0).get("pkDept"),opwinid});
					if(count>0){
						resDatavo.setRetcode("1");
						resDatavo.setRetmsg("窗口状态更新成功");
						resDatavo.setRetval("0");
					}else{
						resDatavo.setRetcode("0");
						resDatavo.setRetmsg("窗口状态更新失败");
						resDatavo.setRetval("0");
					}
				}else{
					StringBuffer sql=new StringBuffer();
					sql.append("update bd_dept_unit du set du.flag_online='0'");
					sql.append(" where du.eu_unittype='1' and du.eu_butype='1' and du.pk_dept=? and du.code=? and du.flag_online='1'");
					int count=DataBaseHelper.update(sql.toString(), new Object[]{list.get(0).get("pkDept"),opwinid});
					if(count>0){
						resDatavo.setRetcode("1");
						resDatavo.setRetmsg("窗口状态更新成功");
						resDatavo.setRetval("0");
					}else{
						resDatavo.setRetcode("0");
						resDatavo.setRetmsg("窗口状态更新失败");
						resDatavo.setRetval("0");
					}
				}
			} catch (Exception e) {
				resDatavo.setRetcode("0");
				resDatavo.setRetmsg("窗口状态更新失败");
				resDatavo.setRetval("0");
				logger.info("====================华润包药机更新窗口状态【311】接口：状态【失败】，时间："+DateUtils.getDate("yyyy-MM-dd HH:mm:ss")+"，异常原因："+e.getMessage()+"====================");

			}
		}else{
			resDatavo.setRetcode("0");
			resDatavo.setRetmsg("调用窗口状态更新【311】接口，未获得窗口状态！");
			resDatavo.setRetval("0");
		}
		long timeStart=dateBegin.getTime();
		long timeEnd=new Date().getTime();
		logger.info("====================华润包药机更新窗口状态【311】接口结束：共耗时"+(timeEnd-timeStart)+"ms==========");
		return resDatavo;
		
	}
	
	/**
	 * 门诊药房签到机签到服务
	 * 1.华润签到机发送患者标识数据；
	 * 2.调用his接口，his将处方信息发送给包药机
	 * @param dataVo
	 * @param dateBegin
	 * @return
	 */
	public Response drugRegPres(Request req,Date dateBegin){
		Response resvo=new Response();
		try {
			Map<String,Object> paramMap=new HashMap<String,Object>();
			if(req.getSubject().getPrescribeTime()!=null){
				String dateStart=req.getSubject().getPrescribeTime().getPrescribeTimeStart()+"000000";
				String dateEnd=req.getSubject().getPrescribeTime().getPrescribeTimeStop()+"235959";
				paramMap.put("dateStart", dateStart);
				paramMap.put("dateEnd", dateEnd);
			}
			paramMap.put("id", req.getSubject().getId());
			paramMap.put("codePati", req.getSubject().getCodePati());
			paramMap.put("registerId", req.getSubject().getRegisterId());
			paramMap.put("dispensary", req.getSubject().getDispensary());
			List<DataPrescription> presList=hrMapper.qryDataPresInfo(paramMap);
			if(presList!=null && presList.size()>0){
				List<DataPrescriptionDetail> presDtList=new ArrayList<DataPrescriptionDetail>();
				List<String> pkPresocces=new ArrayList<String>();
				for (int i = 0; i < presList.size(); i++) {
					pkPresocces.add(presList.get(i).getPkPresocc());
				}
				if(pkPresocces.size()>0){
					presDtList=hrMapper.qryDataPrescriptionDetails(pkPresocces);
				}
				Integer itemNo=1;
				for (DataPrescription dataPres : presList) {
					for (DataPrescriptionDetail dataDts : presDtList) {
						if(dataDts.getPkPresocc().equals(dataPres.getPkPresocc())){
							dataDts.setItemNo(itemNo.toString());
							dataDts.setId(dataPres.getId());
							dataPres.getDataPrescriptionDetail().add(dataDts);
						}
					}
					itemNo++;
				}
				doQryPresInfoRes(resvo,req,"AA");
				resvo.getSubject().setDataPrescriptions(presList);
			}else{
				doQryPresInfoRes(resvo,req,"AE");
				resvo.getResult().setText("未查询到处方信息！");
			}
		} catch (Exception e) {
			doQryPresInfoRes(resvo,req,"AE");
			logger.info("====================华润包药机查询处方信息【S161】接口：状态【失败】，时间："+DateUtils.getDate("yyyy-MM-dd HH:mm:ss")+",失败原因："+e.getMessage());
		}
		long timeStart=dateBegin.getTime();
		long timeEnd=new Date().getTime();
		logger.info("====================华润包药机查询处方信息【S161】接口结束：共耗时"+(timeEnd-timeStart)+"ms==========");
		return resvo;
	}
	
	/**
	 * 处理调用处方返回消息
	 * @param res
	 * @param req
	 * @param status
	 */
	private void doQryPresInfoRes(Response res,Request req ,String status){
		res.setActionId("PrescriptionQuery");
		res.setActionName("处方查询服务");
		Result result=res.getResult();
		result.setId(status);
		if("AA".equals(status)){
			result.setText("处理成功！");
		}else{
			result.setText("处理失败");
		}
		result.setRequestId(req.getId());
		result.setRequestTime(req.getCreateTime());
	}
	
	/**
	 * 门诊分配窗口服务
	 * 1.华润包药机将处方分配的窗口数据；
	 * 2.his保存窗口数据以及处理签到标记
	 * @param dataVo
	 * @param dateBegin
	 * @return
	 */
	public Response drugDoWinno(Request dataVo,Date dateBegin){
		Response resvo=new Response();
		Root root=dataVo.getRoot();
		String message="";
		try {
			if(CommonUtils.isEmptyString(root.getPrescNo())){
				message="门诊分配窗口服务失败，处方号未传入！";
			}else if(CommonUtils.isEmptyString(root.getRetmsg())){
				message="门诊分配窗口服务失败，发药窗口未传入";
			}
			hrMapper.updateWinnoByPresNo(root);
			resvo.getResult().setId("AA");
			resvo.getResult().setText("分配窗口服务成功！");
			resvo.getResult().setPrescNo(root.getPrescNo());
		} catch (Exception e) {
			resvo.getResult().setId("AE");
			resvo.getResult().setText(message);
			resvo.getResult().setPrescNo(root.getPrescNo());
			logger.info("====================华润包药机门诊分配窗口服务【S181】接口：状态【失败】，时间："+DateUtils.getDate("yyyy-MM-dd HH:mm:ss")+",失败原因："+e.getMessage());
		}
		long timeStart=dateBegin.getTime();
		long timeEnd=new Date().getTime();
		logger.info("====================华润包药机门诊分配窗口服务【S181】接口结束：共耗时"+(timeEnd-timeStart)+"ms==========");
		return resvo;
	}
	
	/**
	 * 通过人员编码获取人员主键
	 * @param codeEmp
	 * @return
	 */
	private String getPkEmpByCode(String codeEmp){
		String sql="select pk_emp  from bd_ou_employee where code_emp=? and del_flag='0'";
		List<Map<String,Object>> resMap=DataBaseHelper.queryForList(sql, new Object[]{codeEmp});
		if(resMap==null||resMap.size()<=0){
			return "";
		}else{
			return resMap.get(0).get("pkEmp").toString();
		}
	}
}
