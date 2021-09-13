package com.zebone.nhis.emr.rec.rec.service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import com.zebone.nhis.common.module.emr.mgr.EmrOperateLogs;
import com.zebone.nhis.common.module.emr.rec.rec.EmrLisResult;
import com.zebone.nhis.common.module.emr.rec.rec.EmrMedRec;
import com.zebone.nhis.common.module.emr.rec.rec.EmrOpOrdList;
import com.zebone.nhis.common.module.emr.rec.rec.EmrOrdList;
import com.zebone.nhis.common.module.emr.rec.rec.EmrRisResult;
import com.zebone.nhis.common.module.emr.rec.rec.EmrTxtData;
import com.zebone.nhis.common.support.ApplicationUtils;
import com.zebone.nhis.emr.rec.rec.dao.EmrRsltQryMapper;
import com.zebone.nhis.emr.rec.rec.vo.EmrVitalSigns;
import com.zebone.platform.Application;
import com.zebone.platform.common.support.UserContext;
import com.zebone.platform.modules.dao.DataBaseHelper;

/**
 * 病历书写-检查检验接口查询
 * 
 * @author chengjia
 *
 */
@Service
public class ReqRsltQryService {

	@Resource
	private EmrRsltQryMapper qryMapper;

	/**
	 * 查询检验结果(syx)
	 * 
	 * @param param
	 * @param user
	 * @return
	 */
	@Transactional(propagation=Propagation.REQUIRES_NEW) 
	public List<EmrLisResult> queryPatLisResultSyxBak(Map map) {
		List<EmrLisResult> rtnList = new ArrayList<EmrLisResult>();
		List<EmrLisResult> reqList = qryMapper.queryPatLisReqSyx(map);
		if(reqList==null||reqList.size()==0) return rtnList;
		List<String> codes = new ArrayList<String>();
		int i=0;
		for(i=0;i<reqList.size();i++){
			codes.add(reqList.get(i).getSmpNo());
		}
		List<EmrLisResult> rptList = qryMapper.queryPatLisResultSyx2(codes);
		if(rptList==null||rptList.size()==0) return rtnList;
		for(i=0;i<rptList.size();i++){
			EmrLisResult item = rptList.get(i);
			
			for(int j=0;j<reqList.size();j++){
				EmrLisResult req=reqList.get(j);
				if(req.getSmpNo()!=null&&item.getSmpNo()!=null&&req.getSmpNo().equals(item.getSmpNo())){
					item.setReqDate(req.getReqDate());
					item.setTestDate(req.getTestDate());
					item.setTestName(req.getTestName());
					item.setReqNo(req.getReqNo());
					break;
				}
			}
			rtnList.add(item);
		}
		
		return rtnList;
	}
	
	/**
	 * 查询患者检验申请(syx)
	 * 
	 * @param param
	 * @param user
	 * @return
	 */
	@Transactional(propagation=Propagation.REQUIRES_NEW) 
	public List<EmrLisResult> queryPatLisReqSyx(Map map) {
		List<EmrLisResult> rtnList = qryMapper.queryPatLisReqSyx(map);
		return rtnList;
	}
	
	/**
	 * 查询患者检验结果(syx)
	 * 
	 * @param param
	 * @param user
	 * @return
	 */
	@Transactional(propagation=Propagation.REQUIRES_NEW) 
	public List<EmrLisResult> queryPatLisResultSyx(String smpNos) {
		List<EmrLisResult> rtnList = qryMapper.queryPatLisResultSyx(smpNos);
		return rtnList;
	}
	/**
	 * 查询患者检验结果(syx)
	 * 
	 * @param param
	 * @param user
	 * @return
	 */
	@Transactional(propagation=Propagation.REQUIRES_NEW) 
	public List<EmrLisResult> queryPatLisResultSyx2(List<String> codes) {
		List<EmrLisResult> rtnList = qryMapper.queryPatLisResultSyx2(codes);
		return rtnList;
	}
	/**
	 * 查询患者检查记录(syx)
	 * 
	 * @param param
	 * @param user
	 * @return
	 */
	@Transactional(propagation=Propagation.REQUIRES_NEW) 
	public List<EmrRisResult> queryPatRisResultSyx(Map map) {
		List<EmrRisResult> rtnList=new ArrayList<EmrRisResult>();
		try {
			rtnList = qryMapper.queryPatRisResultSyx(map);
		} catch (Exception e) {
			// TODO: handle exception
			//e.printStackTrace();
			System.out.println("error:"+e.getMessage());
		}
		
		return rtnList;
	}
	/**
	 * 查询患者检查记录(syx 超声)
	 * 
	 * @param param
	 * @param user
	 * @return
	 */
	@Transactional(propagation=Propagation.REQUIRES_NEW) 
	public List<EmrRisResult> queryPatRisSupperResultSyx(Map map) {
		List<EmrRisResult> rtnList=new ArrayList<EmrRisResult>();
		try {
			rtnList = qryMapper.queryPatRisSupperResultSyx(map);
		} catch (Exception e) {
			// TODO: handle exception
			//e.printStackTrace();
			System.out.println("error:"+e.getMessage());
		}
		
		return rtnList;
	}
	/**
	 * 查询患者检查记录(syx 核医学)
	 * 
	 * @param param
	 * @param user
	 * @return
	 */
	@Transactional(propagation=Propagation.REQUIRES_NEW) 
	public List<EmrRisResult> queryPatRisResultSyxHyx(Map map) {
		List<EmrRisResult> rtnList = qryMapper.queryPatRisResultSyxHyx(map);
		return rtnList;
	}
	
	/**
	 * 查询患者检查记录(syx 病理)
	 * 
	 * @param param
	 * @param user
	 * @return
	 */
	@Transactional(propagation=Propagation.REQUIRES_NEW) 
	public List<EmrRisResult> queryPatRisResultSyxPa(Map map) {
		List<EmrRisResult> rtnList = qryMapper.queryPatRisResultSyxPa(map);
		return rtnList;
	}
	
	/**
	 * 查询患者生命体征数据
	 * @param param
	 * @param user
	 * @return
	 */
	@Transactional(propagation=Propagation.REQUIRES_NEW) 
	public List<EmrVitalSigns> queryVitalSignsSyx(String codePv) {
		List<EmrVitalSigns> list = qryMapper.queryVitalSignsSyx(codePv);
		return list;
	}
	/**
	 * 病案首页上传数据失败插入日志信息
	 * @param medRec
	 * @param now
	 * @param strCode
	 * @param paraCode
	 * @param docTxt
	 */
	public void saveEmrOperLogs(EmrOperateLogs emrOpeLogs) {
		String sql="select * from emr_operate_logs where pk_pv=? and del_flag='0'";
		EmrOperateLogs exist=DataBaseHelper.queryForBean(sql, EmrOperateLogs.class, emrOpeLogs.getPkPv());
		if(exist==null){
			DataBaseHelper.insertBean(emrOpeLogs);
		}
	}
	
	/**
	 * 病案首页重传成功则日志信息的删除标志置为1
	 * @param medRec
	 * @param now
	 * @param strCode
	 * @param paraCode
	 * @param docTxt
	 */
	public void updEmrOperLogs(String pkPv) {
		String sql="select * from emr_operate_logs where pk_pv=? and del_flag='0'";
		EmrOperateLogs exist=DataBaseHelper.queryForBean(sql, EmrOperateLogs.class, pkPv);
		if(exist!=null){
			DataBaseHelper.update("update emr_operate_logs set del_flag='1' where pk_pv=?", new Object[]{pkPv});
		}
	}
	/**
	 * 病案首页上传更新PatRec表里的首页上传标志和时间
	 * @param pkPv
	 */
	public void updPatRecPageUpload(String pkPv) {
		Date date=new Date();
		SimpleDateFormat sf = new SimpleDateFormat("yyyyMMddHHmmss");
		String sql="update emr_pat_rec set flag_page_upload='1',page_upload_date=to_date('"+sf.format(date)+"','yyyymmddhh24miss') where pk_pv=? and del_flag='0'";
		DataBaseHelper.update(sql, new Object[]{pkPv});
	}
	/**
	 * 病案编码完成时修改patRec状态
	 * @param pkPv
	 * @param codeEmp
	 */
	public void updPatRecFlagCode(String pkPv,String codeEmp) {
		Date date=new Date();
		SimpleDateFormat sf = new SimpleDateFormat("yyyyMMddHHmmss");
		String sql="update emr_pat_rec set flag_code='1',pk_emp_code='"+codeEmp+"',code_date=to_date('"+sf.format(date)+"','yyyymmddhh24miss') where pk_pv=? and del_flag='0'";
		DataBaseHelper.update(sql, new Object[]{pkPv});
		
	}
	public void updHomeTimes(String pkPv,Integer max) {
		if(pkPv!=null){
			try {
				DataBaseHelper.update("update EMR_HOME_PAGE set TIMES=? where PK_PV=? and DEL_FLAG='0'", new Object[]{max,pkPv});
			} catch (Exception e) {
				// TODO: handle exception
			}
		}
	}
	
	/**
	 * 查询患者检查结果(boai)
	 * @param map
	 * @return
	 */
	@Transactional(propagation=Propagation.REQUIRES_NEW) 
	public List<EmrLisResult> queryPatLisReqBoai(Map map) {
		List<EmrLisResult> rtnList=new ArrayList<EmrLisResult>();
		try {
			String strs=ApplicationUtils.getPropertyValue("EmrLisReqBabyDept", "");
			List<String> listType = java.util.Arrays.asList(strs.split("|"));
			String nowDept=UserContext.getUser().getPkDept();
			if(listType!=null&&listType.contains(nowDept)){
				rtnList = qryMapper.queryPatLisReqBoaiBaby(map);
			}else{
				rtnList = qryMapper.queryPatLisReqBoai(map);
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
		return rtnList;
	}
	/**
	 * 查询患者检查结果根据检验项目查(boai)
	 * @param map
	 * @return
	 */
	@Transactional(propagation=Propagation.REQUIRES_NEW) 
	public List<EmrLisResult> queryPatLisReqByObjBoai(Map map) {
		List<EmrLisResult> rtnList=new ArrayList<EmrLisResult>();
		try {
			
			rtnList = qryMapper.queryPatLisReqByObjBoai(map);
		} catch (Exception e) {
			// TODO: handle exception
		}
		return rtnList;
	}
	
	/**
	 * 查询患者检查记录(boai)
	 * 
	 * @param param
	 * @param user
	 * @return
	 */
	@Transactional(propagation=Propagation.REQUIRES_NEW) 
	public List<EmrRisResult> queryPatRisResultBoai(Map map) {
		List<EmrRisResult> rtnList=new ArrayList<EmrRisResult>();
		try {
			rtnList = qryMapper.queryPatRisResultBoai(map);
		} catch (Exception e) {
			
		}
		
		return rtnList;
	}
	/**
	 * 查询患者检查记录(心电)(boai)
	 * 
	 * @param param
	 * @param user
	 * @return
	 */
	@Transactional(propagation=Propagation.REQUIRES_NEW) 
	public List<EmrRisResult> queryPatRisnlResultBoai(Map map) {
		List<EmrRisResult> rtnList=new ArrayList<EmrRisResult>();
		try {
			rtnList = qryMapper.queryPatRisnlResultBoai(map);
		} catch (Exception e) {
			
		}
		
		return rtnList;
	}
	/**
	 * 查询患者检查记录首页查病理结果用(boai)
	 * 
	 * @param param
	 * @param user
	 * @return
	 */
	@Transactional(propagation=Propagation.REQUIRES_NEW) 
	public List<EmrRisResult> queryPatRisResultBoaiHomePage(Map map) {
		List<EmrRisResult> rtnList=new ArrayList<EmrRisResult>();
		try {
			rtnList = qryMapper.queryPatRisResultBoaiHomePage(map);
		} catch (Exception e) {
			// TODO: handle exception
		}
		
		return rtnList;
	}
	
	/**
	 * 查询第三方的医嘱视图--博爱
	 * 
	 * @param param
	 * @param user
	 * @return
	 */
	@Transactional(propagation=Propagation.REQUIRES_NEW) 
	public List<EmrOrdList> queryPatOrdResultThird(Map map) {
		List<EmrOrdList> rtnList = qryMapper.queryPatOrdResultThird(map);
		return rtnList;
	}
	/**
	 * 查询第三方的手麻视图--博爱
	 * 
	 * @param param
	 * @param user
	 * @return
	 */
	@Transactional(propagation=Propagation.REQUIRES_NEW) 
	public List<Map<String, Object>> queryPatOpsResultThird(Map map) {
		List<Map<String, Object>> rtnList = qryMapper.queryPatOpsResultThird(map);
		return rtnList;
	}
	/**
	 * 查询第三方的输血过程--博爱
	 * 
	 * @param param
	 * @param user
	 * @return
	 */
	@Transactional(propagation=Propagation.REQUIRES_NEW) 
	public List<Map<String, Object>> queryBloodList(Map map) {
		List<Map<String, Object>> rtnList = qryMapper.queryBloodList(map);
		return rtnList;
	}
}
